package simpplle.comcode;

import java.util.*;
import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>The idea behind this class is that we needed an entity to represent
 * a fire event.  Where the unit of origin and units it spread to are stored
 * as well as holding the methods that determine where and how a fire event
 * will spread.  
 * <p>It was not named FireEvent as that name is already taken to
 * represent a kind of process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */


public class ProcessOccurrenceSpreadingFire extends ProcessOccurrenceSpreading implements Externalizable {
  static final long serialVersionUID = -7840370421868476956L;
  static final int  version          = 1;

  protected int     hoursBurning=0;
  protected int     burningPeriods=0;
  protected int     weatherProb;
  protected boolean isWeatherProbSet=false;
  protected int     weatherProbAcresRangeNumber=-1;
  protected boolean isExtreme=false;
  protected boolean isExtremeSet=false;
  protected Climate.Season fireSeason;
  protected boolean isFireSeasonSet=false;
  protected boolean fireSuppressed=false;
  protected boolean fireSuppressedSet=false;
  protected boolean eventFireSuppRandomDrawn=false;
  protected int     eventFireSuppRandomNumber=0;
  protected Node    lineProductionNode = null;
  protected boolean firstProduction=true;
  protected int     totalLineProduced=0;
  
  public enum EventStop { OTHER, WEATHER, LINE}
  
  protected EventStop eventStopReason=EventStop.OTHER;
  
  protected ArrayList<Integer> lineSuppUnits = new ArrayList<Integer>();

//  public void destoryMe() {
//    fireSeason = null;
//    lineProductionNode = null;
//    super.destoryMe();
//  }
  /**
   * 
   */
  public ProcessOccurrenceSpreadingFire() {
    super();
  }
  public ProcessOccurrenceSpreadingFire(Evu evu, Lifeform lifeform,
                                        ProcessProbability processData, int timeStep) {
    super(evu,lifeform,processData,timeStep);
  }

  public String getEventStopReason() {
    return eventStopReason.toString();
  }
  
  public boolean isExtremeEvent() { return isExtreme; }

  public Climate.Season getFireSeason() { return fireSeason; }
  public boolean isSuppressed() { return fireSuppressed; }

  @SuppressWarnings("unchecked")
  protected int calculatePerimeter() {
    int result = 0;
    if (root == null) { return 0;  }

    LinkedList queue= new LinkedList();
    queue.add(root);

    Node           node;
    AdjacentData[] adjData;
    while (queue.size() > 0) {
      node = (Node)queue.removeFirst();
//      if (node.data.getProcessProbability() == Evu.SUPP) { continue; }
      if (node.data.getUnit().isSuppressed()) { continue; }
      adjData  = node.data.getUnit().getAdjacentData();
      for (int i=0; i<adjData.length; i++) {
        VegSimStateData adjState = adjData[i].evu.getState();
        if (adjState == null || adjState.getProcess().isFireProcess()) {
          continue;
        }
        result += adjData[i].evu.getSideLength();
      }

      if (node.toNodes == null) { continue; }
      for (int i=0; i<node.toNodes.length; i++) {
        queue.add(node.toNodes[i]);
      }
    }

    return result;
  }
  
  public int getLineProduced() { return totalLineProduced; }
  /**
   * 
   * @return
   */
  public int calculateApproxPerimeter() {
    float fEventAcres = Area.getFloatAcres(eventAcres);
    int   eventSideLength = (int)Math.round(Math.sqrt(fEventAcres*43560));
    
    int eventPerimeter = eventSideLength * 4;
    return eventPerimeter;
  }

  /**
   * Find out if this unit has any neighbors that are not burning.
   * If so we can build line here.  More ideal would be finding only
   * perimeter units, but not sure how best to achieve that right now
   * without significantly affecting performance.
   * @param unit
   * @return
   */
  private boolean hasNonBurningNeighbors(Evu unit) {
    AdjacentData[] adjData;
    
    adjData = unit.getAdjacentData();
    if (adjData != null) {
      for (int i = 0; i < adjData.length; i++) {
        if (!adjData[i].evu.hasFireAnyLifeform()) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  private Evu getNonBurningLowestNeighbor(Evu unit) {
    int lowestElevation = 1000000;
    int unitElevation = 0;
    Evu lowestUnit = null;
    AdjacentData[] adjData;
    
    adjData = unit.getAdjacentData();
    if (adjData != null) {
      for (int i = 0; i < adjData.length; i++) {
        unitElevation = adjData[i].evu.getElevation();

        if (!adjData[i].evu.hasFireAnyLifeform() && unitElevation < lowestElevation) {
          lowestElevation = unitElevation;
          lowestUnit = adjData[i].evu;
        }
      }
    }
    
    return lowestUnit;
  }

  /**
   * Find the node whose unit is lowest in elevation relative to the root node.
   * Also make sure that the we only consider node that don't have
   * stand replacing fire or fire line, as suppression forces cannot build line there.
   * 8/23/11  Added check for beyond A suppression
   * 8/24/11  Added check for non burning neighbors
   * @return a Node, the lowest non-stand-replace-fire non-suppressed node.
   */
  @SuppressWarnings("unchecked")
  protected Node findLowestElevationNonSrfNonSuppNode() {
    FireSuppBeyondClassALogic logicInst = FireSuppBeyondClassALogic.getInstance();
    int ts = Simulation.getCurrentTimeStep();

    if (root == null) { return null;  }
    LinkedList queue= new LinkedList();

    int lowestElevation = 1000000;
    
    queue.add(root);
    Node node, lowestNode=root;
    while (queue.size() > 0) {
      node = (Node)queue.removeFirst();
      VegSimStateData state = node.data.getUnit().getState();
      boolean doLine = false;
      if (state != null) { 
        doLine = logicInst.isSuppressedUniform(this,state.getVeg(),state.getProcess(),isExtreme,node.data.getUnit(),ts,lifeform);
      }
      
      boolean nonBurningNeighbors = hasNonBurningNeighbors(node.data.getUnit());
      int nodeElevation = node.data.getUnit().getElevation();

      if ((nodeElevation < lowestElevation) &&
          doLine &&
          nonBurningNeighbors &&
          (node.data.getProcess().equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
          (node.data.getUnit().isSuppressed() == false)) {
        lowestNode = node;
        lowestElevation = nodeElevation;
      }

      if (node.toNodes == null) { continue; }
      for (int i=0; i<node.toNodes.length; i++) {
        queue.add(node.toNodes[i]);
      }
    }

    return lowestNode;
  }


  /**
   * 23 August 2004.
   *   Modified code to allow any event to
   *   spread as extreme if the FireEvent.isExtremeSpread() return true.
   *   Previously we had it restricted so that only events that originate
   *   in a StandReplacingFire unit could become extreme from probability.
   *
   * will check if suppression forces are here and do line calculations
   *       when starting at the from node and getting ready to spread.  Will
   *       need to start building suppression line at the lowest elevation node.
   *
   * Pop a node off the queue and try to spread to its adjacent units,
   * then return to allow spreading of other events to occur.
   */
  @SuppressWarnings("unchecked")
  public void doSpread() {
    Node           spreadingNode;
    AdjacentData[] adjData;
    Evu            fromUnit, toUnit;
    RegionalZone   zone = Simpplle.getCurrentZone();
    int            newLine, sideLength;
    int            prodRate;
//    boolean        firstProduction=true;

    tmpToUnits.clear();

    boolean hasUniformPolygons = Simpplle.getCurrentArea().hasUniformSizePolygons();
    double  spreadTime, responseTime, suppTime=0.0;
    boolean fireSuppression = Simpplle.getCurrentSimulation().fireSuppression();

    responseTime = Fmz.getResponseTime(root.data.getUnit());
    sideLength   = root.data.getUnit().getSideLength();

//    if (getProcess().equals(ProcessType.STAND_REPLACING_FIRE)) {
    if (!isExtremeSet) {
      isExtreme = simpplle.comcode.process.FireEvent.isExtremeSpread();
      isExtremeSet = true;
    }
    if (!isFireSeasonSet && getProcess().isFireProcess()) {
      fireSeason = simpplle.comcode.process.FireEvent.getFireSeason();
      isFireSeasonSet = true;
      
    }
    
    if (!eventFireSuppRandomDrawn) {
      eventFireSuppRandomNumber = Simulation.getInstance().random();
      eventFireSuppRandomDrawn = true;
    }

    if (!fireSuppressedSet) {
      Evu originEvu = root.data.getUnit();
      fireSuppressed = FireSuppEventLogic.getInstance().isSuppressed(originEvu,eventFireSuppRandomNumber);
    }
    
//    if (!fireSuppressed) {
//      fireSuppression = false;
//    }
    fireSuppression = (Simulation.getInstance().fireSuppression()) ? fireSuppressed : false; 
    
//    while (spreadQueue.size() > 0) {
    if (spreadQueue.size() == 0) {
      finished = true;
      eventStopReason = EventStop.OTHER;

      if (Simulation.getInstance().isDoSimLoggingFile()) {
        PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
        int originUnitId = root.data.getUnit().getId();
        int firePerimeter = calculateApproxPerimeter();

        logOut.printf("Time: %d, Origin Unit: %d, Nowhere left to Spread, Line Produced: %d, Event Perimeter: %d %n",
            Simulation.getCurrentTimeStep(),originUnitId,totalLineProduced,firePerimeter);
      }
      return;
    }

    // Originally prob set just once, then changed to every call of this method.
    // Now modified to get new prob every time event acres changes to new acres
    // range.
//    if (!isWeatherProbSet) {
//      weatherProb = Simulation.getInstance().random();
//      isWeatherProbSet = true;
//    }
      
    int rangeNum = FireSuppWeatherData.getAcresRangeNumber(getEventAcres());
    if (!isWeatherProbSet) {
      weatherProb = Simulation.getInstance().random();
      isWeatherProbSet = true;
      weatherProbAcresRangeNumber = rangeNum;
    }
    else if (rangeNum != weatherProbAcresRangeNumber) {
      weatherProb = Simulation.getInstance().random();
      weatherProbAcresRangeNumber = rangeNum;      
    }
      
      
      
      if (simpplle.comcode.process.FireEvent.doSpreadEndingWeather(zone,getEventAcres(),getFireSeason(),weatherProb)) {
        finished = true;
        eventStopReason = EventStop.WEATHER;

        if (Simulation.getInstance().isDoSimLoggingFile()) {
          PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
          int ts = Simulation.getCurrentTimeStep();
          int originUnitId = root.data.getUnit().getId();

          logOut.printf("Time: %d, Origin Unit of event where weather ended fire: %d%n",ts, originUnitId);

          int firePerimeter = calculateApproxPerimeter();

          logOut.printf("Time: %d, Origin Unit: %d, Weather Ending, Line Produced: %d, Event Perimeter: %d %n",
                        ts,originUnitId,totalLineProduced,firePerimeter);
        }
        return;
      }

      if (Area.getFloatAcres(eventAcres) > simpplle.comcode.process.FireEvent.getExtremeEventAcres()) {
        isExtreme = true;
      }

      spreadingNode = (Node) spreadQueue.removeFirst();

      fromUnit = spreadingNode.data.getUnit();
      Area.currentLifeform = fromUnit.getDominantLifeform();

      fireSuppressed = FireSuppEventLogic.getInstance().isSuppressed(fromUnit,eventFireSuppRandomNumber);
      fireSuppression = (Simulation.getInstance().fireSuppression()) ? fireSuppressed : false; 

      int ts = Simulation.getCurrentTimeStep();
      
      if (hasUniformPolygons && fireSuppression) {
        VegSimStateData state    = fromUnit.getState();
        VegetativeType  vegType  = state.getVeg();
        Lifeform        lifeform = state.getLifeform();

        spreadTime = FireSuppSpreadRateLogic.getInstance().getRate(vegType,isExtreme,fromUnit,ts,lifeform);
        hoursBurning += spreadTime;

        if (hoursBurning > responseTime) {
//          suppTime += ( (firstProduction) ? (hoursBurning - responseTime) : spreadTime);
//          firstProduction=false;
          
          do {
            lineProductionNode = findLowestElevationNonSrfNonSuppNode();
            Evu theUnit = lineProductionNode.data.getUnit();
            if (theUnit.isSuppressed()) {
              break;
            }


            state    = theUnit.getState(theUnit.getDominantLifeform());
            vegType  = state.getVeg();
            lifeform = state.getLifeform();

            spreadTime = FireSuppSpreadRateLogic.getInstance().getRate(vegType,isExtreme,theUnit,ts,lifeform);
            suppTime += spreadTime;

            prodRate = FireSuppProductionRateLogic.getInstance().getRate(eventAcres,vegType,theUnit,ts,lifeform);
            newLine = (int) Math.round( (double) prodRate * suppTime);
            totalLineProduced += newLine;
            
            int firePerimeter = calculateApproxPerimeter();

            if (totalLineProduced > firePerimeter) {
              finished=true;
              eventStopReason = EventStop.LINE;

              if (Simulation.getInstance().isDoSimLoggingFile()) {
                PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
                int originUnitId = root.data.getUnit().getId();

                logOut.printf("Time: %d, Origin Unit: %d Event stopped due to line production exceeding perimeter size %n",
                    Simulation.getCurrentTimeStep(),originUnitId);

                logOut.printf("Time: %d, Origin Unit: %d, Line Produced: %d, Event Perimeter: %d %n",
                              ts,originUnitId,totalLineProduced,firePerimeter);
              }
              return;
            }
            
            double pctLine = (double)newLine / (double)sideLength;
            
            if (newLine >= sideLength) {
              theUnit = lineProductionNode.data.getUnit();
              theUnit.suppressFire(false);
              
              if (Simulation.getInstance().isDoSimLoggingFile()) {
                PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
                logOut.printf("Time: %d, Origin Unit %d further spread from Unit %d suppressed due to line production: %n",
                    Simulation.getCurrentTimeStep(), root.data.getUnit().getId(), theUnit.getId());
              }
              double extraLine = newLine - sideLength;
              double fProdRate = (double)prodRate;
              suppTime = -(extraLine / fProdRate);
            }
            else if (pctLine > 0.5) {
              Evu lowAdj = getNonBurningLowestNeighbor(fromUnit);
              if (lowAdj != null && !lineSuppUnits.contains(lowAdj.getId())) {
                lineSuppUnits.add(lowAdj.getId());

                if (Simulation.getInstance().isDoSimLoggingFile()) {
                  PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
                  logOut.printf("Time: %d, Origin Unit %d spread into Unit %d suppressed due to line production: %n",
                      Simulation.getCurrentTimeStep(), root.data.getUnit().getId(), lowAdj.getId());
                }
              }
            }
            else { break; }
          }
          while (true);
        }
      }

      simpplle.comcode.process.FireEvent.currentEvent = this;
      // TODO Getting a null pointer exception when getDominantLifeformFire returns null.
      //      This apparently is result of isSuppressed returning false when should be true.
      //      cannot get error to repeat after numerous attempts.
      if (!fromUnit.isSuppressed() &&  fromUnit.getDominantLifeformFire() != null ) {
        adjData = fromUnit.getAdjacentData();
        if (adjData != null) {
          for (int i = 0; i < adjData.length; i++) {
            toUnit = adjData[i].evu;
            if (lineSuppUnits.contains(toUnit.getId())) {
              continue;
            }
            
            if (Evu.doSpread(fromUnit, toUnit,fromUnit.getDominantLifeformFire())) {
              tmpToUnits.add(toUnit);
//              addSpreadEvent(fromUnit, toUnit);
//              spreadQueue.add( (Node) nodeLookup.get(toUnit));
            }
          }
          doFireSpotting(fromUnit);
        }
      }
      addSpreadEvent(spreadingNode,tmpToUnits,lifeform);
      finished = (spreadQueue.size() == 0);

      simpplle.comcode.process.FireEvent.currentEvent = null;
      Area.currentLifeform = null;
//      thread.yield();  // Give other thread a chance to run.
//    }
  }

  /**
   * July 2004.
   * Removed restriction that said adjacent units had to be
   *   above the from unit in order to start a spot fire.
   *
   * 23 August 2004.
   *  Modified spotting so that it can only
   *   spot from a unit with StandReplacingFire.  Previously there was no
   *   restriction on fire type.
   *
   * @param fromEvu The unit we are trying to spot a fire from.
   * @return true if fire spotting has occurred. (is this still used??)
   */
  @SuppressWarnings("unchecked")
  public void doFireSpotting(Evu fromEvu) {
    AdjacentData[] adjacentData;
    Evu            adj, fromAdj;
    ArrayList<Evu> spotFrom, newSpotFrom, tmpList, unitsTried;
    int            i, j, k;

    VegSimStateData state = fromEvu.getState();
    if (state == null || Utility.getFireSpotting() == false) {
      return;
    }

    adjacentData = fromEvu.getAdjacentData();
    if (adjacentData == null) { return; }
    spotFrom    = new ArrayList<Evu>();
    newSpotFrom = new ArrayList<Evu>();
    unitsTried    = new ArrayList<Evu>();

    unitsTried.add(fromEvu);
    for(i=0;i<adjacentData.length;i++) {
      adj = adjacentData[i].evu;
      unitsTried.add(adj);
      if (fromEvu.isAdjDownwind(adj) &&
          spotFrom.contains(adj) != true) {
        spotFrom.add(adj);
      }
    }

    boolean uniformPoly = Simpplle.getCurrentArea().hasUniformSizePolygons();
    
    int levelsOut = 0;
    while (spotFrom != null && spotFrom.size() > 0) {
      levelsOut++;
      
      for(j=0;j<spotFrom.size();j++) {
        fromAdj = (Evu) spotFrom.get(j);
        adjacentData = fromAdj.getAdjacentData();

        for(k=0;k<adjacentData.length;k++) {
          adj = adjacentData[k].evu;
          if (adj == null) { continue; }
          
          if (unitsTried.contains(adj)) { continue; }
          unitsTried.add(adj);
          
          if (!uniformPoly && levelsOut > 3) {
            continue;
          }
          else if (!FireEventLogic.getInstance().isWithinMaxFireSpottingDistance(fromEvu, adj)) {
            continue;
          }
          
          if (fromAdj.isAdjDownwind(adj) && newSpotFrom.contains(adj) != true) {
            newSpotFrom.add(adj);
            if (determineSpotFire(fromEvu,adj)) {
              tmpToUnits.add(adj);
            }
          }
        }
      }
      // Swap.
      tmpList     = spotFrom;
      spotFrom    = newSpotFrom;
      newSpotFrom = tmpList;
      newSpotFrom.clear();
    }
    
    return;
  }

  private boolean determineSpotFire(Evu fromEvu, Evu toEvu) {
    Lifeform fromLifeform = fromEvu.getDominantLifeform();

    VegSimStateData state = fromEvu.getState(fromLifeform);
    if (state == null) { return false; }

    ProcessType processType = state.getProcess();
    
    Lifeform toLifeform = toEvu.getDominantLifeform();
    VegSimStateData toState = toEvu.getState(toLifeform);
    if (toState == null) { return false; }

    ProcessType toProcessType = toState.getProcess();
    if (toProcessType.isFireProcess()) {
      return false;
    }

    int prob = FireEventLogic.getInstance().getFireSpottingProbability(fromEvu, toEvu, processType, isExtreme);

    int rand = Simulation.getInstance().random();
    prob *= 100;

    boolean isSpot = (rand < prob);

    ProcessType fireType;

    if (isSpot)
    {
      fireType = simpplle.comcode.process.FireEvent.getTypeOfFire(Simpplle.getCurrentZone(), toEvu, toLifeform);
      if (fireType == ProcessType.NONE) { return false; }

      if (Area.multipleLifeformsEnabled()) {
        toEvu.updateCurrentStateAllLifeforms(fireType,(short)Evu.SFS,season);
      }
      else {
        toEvu.updateCurrentProcess(toLifeform,fireType);
        toEvu.updateCurrentProb(toLifeform,Evu.SFS);
      }
      return true;
    }
    return false;
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    @SuppressWarnings("unused")
    int version = in.readInt();
    super.readExternal(in);
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);
  }

//  public void run() {
//    doSpread();
//    ((MyThreadGroup)thread.getThreadGroup()).decThreadCount();
//  }

}




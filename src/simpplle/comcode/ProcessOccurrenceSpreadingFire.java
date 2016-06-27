package simpplle.comcode;

import java.util.*;
import java.io.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * Represents a spreading fire event. An event starts at an origin unit and gradually spreads to adjacent units until
 * the fire is extinguished due to weather, a fire suppression line, or running out of vegetation units. This class
 * was not named FireEvent as that name is already taken to represent a kind of process.
 * <p>
 * Original source code authorship: Kirk A. Moeller
 */


public class ProcessOccurrenceSpreadingFire extends ProcessOccurrenceSpreading implements Externalizable {
  static final long serialVersionUID = -7840370421868476956L;
  static final int  version          = 1;

  protected int     hoursBurning=0;
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
  protected int     totalLineProduced=0;
  
  public enum EventStop { OTHER, WEATHER, LINE}
  
  protected EventStop eventStopReason = EventStop.OTHER;
  
  protected ArrayList<Integer> lineSuppUnits = new ArrayList<Integer>();

  /**
   * Creates a spreading fire event with an origin unit.
   * @param evu A vegetation unit
   * @param lifeform A lifeform
   * @param processData A process probability
   * @param timeStep A time step (unused)
   */
  public ProcessOccurrenceSpreadingFire(Evu evu, Lifeform lifeform, ProcessProbability processData, int timeStep) {
    super(evu,lifeform,processData,timeStep);
  }

  /**
   * @return The reason that this event stopped, or OTHER if it hasn't stopped.
   */
  public String getEventStopReason() {
    return eventStopReason.toString();
  }

  /**
   * @return True if this is an extreme fire event.
   */
  public boolean isExtremeEvent() {
    return isExtreme;
  }

  /**
   * @return The season {spring,summer,fall,winter} that this fire occurs.
   */
  public Climate.Season getFireSeason() {
    return fireSeason;
  }

  /**
   * @return True if this event uses fire suppression.
   */
  public boolean isSuppressed() {
    return fireSuppressed;
  }

  /**
   * Calculates an exact fire perimeter.
   * @return The fire perimeter in feet
   */
  @SuppressWarnings("unchecked")
  protected int calculatePerimeter() {

    int perimeter = 0;

    if (root == null) return 0;

    LinkedList queue = new LinkedList();
    queue.add(root);

    Node node;
    AdjacentData[] adjData;

    while (queue.size() > 0) {

      node = (Node)queue.removeFirst();
      if (node.data.getUnit().isSuppressed()) continue;

      adjData = node.data.getUnit().getAdjacentData();

      for (int i=0; i<adjData.length; i++) {

        VegSimStateData adjState = adjData[i].evu.getState();
        if (adjState == null || adjState.getProcess().isFireProcess()) continue;

        perimeter += adjData[i].evu.getSideLength();

      }

      if (node.toNodes == null) continue;

      for (int i=0; i<node.toNodes.length; i++) {
        queue.add(node.toNodes[i]);
      }
    }

    return perimeter;

  }

  /**
   * @return The total length of fire suppression line produced in feet.
   */
  public int getLineProduced() {
    return totalLineProduced;
  }

  /**
   * Calculates an approximate fire perimeter. The approximation assumes that the fire shape is square.
   * @return The fire perimeter in feet
   */
  public int calculateApproxPerimeter() {
    float fEventAcres = Area.getFloatAcres(eventAcres);
    int eventSideLength = (int)Math.round(Math.sqrt(fEventAcres*43560));
    return eventSideLength * 4;
  }

  /**
   * Determine if this unit has any neighbors that are not burning. If so, we can build line here. More ideal would be
   * finding only perimeter units, but not sure how best to achieve that right now without significantly affecting
   * performance.
   * @param unit A vegetation unit with neighbors
   * @return True if a neighbor is burning
   */
  private boolean hasNonBurningNeighbors(Evu unit) {

    AdjacentData[] adjDataArray = unit.getAdjacentData();

    if (adjDataArray != null) {
      for (AdjacentData adjData : adjDataArray) {
        if (!adjData.evu.hasFireAnyLifeform()) {
          return true;
        }
      }
    }
    
    return false;

  }

  /**
   * Finds the neighbor with the lowest elevation that is not burning.
   * @param unit A vegetation unit with neighbors
   * @return A non-burning vegetation unit
   */
  private Evu getNonBurningLowestNeighbor(Evu unit) {

    int lowestElevation = Integer.MAX_VALUE;
    Evu lowestUnit = null;

    AdjacentData[] adjDataArray = unit.getAdjacentData();

    if (adjDataArray != null) {
      for (AdjacentData adjData : adjDataArray) {
        int unitElevation = adjData.evu.getElevation();
        if (!adjData.evu.hasFireAnyLifeform() && unitElevation < lowestElevation) {
          lowestElevation = unitElevation;
          lowestUnit = adjData.evu;
        }
      }
    }
    
    return lowestUnit;

  }

  /**
   * Finds the neighbor with the lowest elevation that does not have a stand replacing fire or a fire line. These
   * qualities are important as suppression forces cannot build line there.
   *
   * 8/23/11  Added check for beyond A suppression
   * 8/24/11  Added check for non burning neighbors
   *
   * @return A vegetation unit fitting the requirements
   */
  @SuppressWarnings("unchecked")
  protected Node findLowestElevationNonSrfNonSuppNode() {

    FireSuppBeyondClassALogic logicInst = FireSuppBeyondClassALogic.getInstance();

    int ts = Simulation.getCurrentTimeStep();

    if (root == null) return null;

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
   * Spreads fire from a single vegetation unit to immediate neighbors. A fire is suppressed when it hits a fire line,
   * runs out of vegetation units, or ends due to weather. Fire line is built at the lowest non-burning unit. Continue
   * calling this method until the event is 'finished'. Spreading from only a single vegetation unit gives other
   * spreading events a chance to spread.
   *
   * 8/23/04 Modified code to allow any event to spread as extreme if FireEvent.isExtremeSpread() returns true.
   *         Previously it was restricted so that only events that start with a stand replacing fire could become
   *         extreme using probabilities.
   */
  @SuppressWarnings("unchecked")
  public void doSpread() {

    if (!isExtremeSet) {
      isExtreme = FireEvent.isExtremeSpread();
      isExtremeSet = true;
    }

    if (!isFireSeasonSet && getProcess().isFireProcess()) {
      fireSeason = FireEvent.getFireSeason();
      isFireSeasonSet = true;
    }
    
    if (!eventFireSuppRandomDrawn) {
      eventFireSuppRandomNumber = Simulation.getInstance().random(); // Greg's Note: Can't this be local in the next block?
      eventFireSuppRandomDrawn = true;
    }

    if (!fireSuppressedSet) {
      Evu originEvu = root.data.getUnit();
      fireSuppressed = FireSuppEventLogic.getInstance().isSuppressed(originEvu,eventFireSuppRandomNumber);
    }

    int rangeNum = FireSuppWeatherData.getAcresRangeNumber(getEventAcres());

    if (!isWeatherProbSet) {

      weatherProb = Simulation.getInstance().random();
      isWeatherProbSet = true;
      weatherProbAcresRangeNumber = rangeNum;

    } else if (rangeNum != weatherProbAcresRangeNumber) {

      weatherProb = Simulation.getInstance().random();
      weatherProbAcresRangeNumber = rangeNum;

    }

    RegionalZone zone = Simpplle.getCurrentZone();
      
    if (FireEvent.doSpreadEndingWeather(zone,getEventAcres(),getFireSeason(),weatherProb)) {

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

    if (Area.getFloatAcres(eventAcres) > FireEvent.getExtremeEventAcres()) {
      isExtreme = true;
    }

    Node spreadingNode = (Node) spreadQueue.removeFirst();
    Evu fromUnit = spreadingNode.data.getUnit();
    Area.currentLifeform = fromUnit.getDominantLifeform();

    fireSuppressed = FireSuppEventLogic.getInstance().isSuppressed(fromUnit,eventFireSuppRandomNumber);

    boolean fireSuppression = (Simulation.getInstance().fireSuppression()) ? fireSuppressed : false;
    boolean hasUniformPolygons = Simpplle.getCurrentArea().hasUniformSizePolygons();
    double responseTime = Fmz.getResponseTime(root.data.getUnit());
    int sideLength = root.data.getUnit().getSideLength();

    int ts = Simulation.getCurrentTimeStep();

    if (hasUniformPolygons && fireSuppression) {

      VegSimStateData state = fromUnit.getState();
      VegetativeType vegType = state.getVeg();
      Lifeform lifeform = state.getLifeform();

      double spreadTime = FireSuppSpreadRateLogic.getInstance().getRate(vegType,isExtreme,fromUnit,ts,lifeform);
      hoursBurning += spreadTime;

      if (hoursBurning > responseTime) {

        double suppTime = 0.0;

        do {

          lineProductionNode = findLowestElevationNonSrfNonSuppNode();
          Evu theUnit = lineProductionNode.data.getUnit();
          if (theUnit.isSuppressed()) break;

          state    = theUnit.getState(theUnit.getDominantLifeform());
          vegType  = state.getVeg();
          lifeform = state.getLifeform();

          spreadTime = FireSuppSpreadRateLogic.getInstance().getRate(vegType,isExtreme,theUnit,ts,lifeform);
          suppTime += spreadTime;

          int prodRate = FireSuppProductionRateLogic.getInstance().getRate(eventAcres,vegType,theUnit,ts,lifeform);
          int newLine = (int) Math.round( (double) prodRate * suppTime);
          totalLineProduced += newLine;

          int firePerimeter = calculateApproxPerimeter();

          if (totalLineProduced > firePerimeter) {

            finished = true;
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

          } else if (pctLine > 0.5) {
            Evu lowAdj = getNonBurningLowestNeighbor(fromUnit);
            if (lowAdj != null && !lineSuppUnits.contains(lowAdj.getId())) {
              lineSuppUnits.add(lowAdj.getId());

              if (Simulation.getInstance().isDoSimLoggingFile()) {
                PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
                logOut.printf("Time: %d, Origin Unit %d spread into Unit %d suppressed due to line production: %n",
                    Simulation.getCurrentTimeStep(), root.data.getUnit().getId(), lowAdj.getId());
              }
            }
          } else {
            break;
          }
        } while (true);
      }
    }

    FireEvent.currentEvent = this;

    tmpToUnits.clear();

    if (!fromUnit.isSuppressed() &&  fromUnit.getDominantLifeformFire() != null ) {

      AdjacentData[] adjData = fromUnit.getAdjacentData();

      if (adjData != null) {
        for (int i = 0; i < adjData.length; i++) {

          Evu toUnit = adjData[i].evu;

          if (lineSuppUnits.contains(toUnit.getId())) {
            continue;
          }

          if (Evu.doSpread(fromUnit, toUnit,fromUnit.getDominantLifeformFire())) {
            tmpToUnits.add(toUnit);
          }
        }

        doFireSpotting(fromUnit);

      }
    }

    addSpreadEvent(spreadingNode,tmpToUnits,lifeform);

    finished = (spreadQueue.size() == 0);

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

    FireEvent.currentEvent = null;
    Area.currentLifeform = null;

  }

  /**
   * Creates spot fires from blowing embers. All vegetation units in the area that are downwind and within the maximum
   * fire spotting distance are tested for spot fires. Spot fires start based on a fire spotting probability entered
   * in the 'Fire Event Logic' dialog.
   *
   * 7/??/04  Removed restriction that said adjacent units had to be above the from unit in order to start a spot fire.
   * 8/23/04  Modified spotting so it can only start from a stand replacing fire. Previously there was no restriction.
   *
   * @param fromEvu The unit we are trying to spot a fire from
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
    if (adjacentData == null) return;

    spotFrom    = new ArrayList<Evu>();
    newSpotFrom = new ArrayList<Evu>();
    unitsTried  = new ArrayList<Evu>();

    unitsTried.add(fromEvu);
    for (i = 0; i < adjacentData.length; i++) {
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
      
      for(j = 0; j < spotFrom.size(); j++) {
        fromAdj = (Evu) spotFrom.get(j);
        adjacentData = fromAdj.getAdjacentData();

        for(k = 0; k < adjacentData.length; k++) {

          adj = adjacentData[k].evu;
          if (adj == null) continue;
          
          if (unitsTried.contains(adj)) continue;
          unitsTried.add(adj);
          
          if (!uniformPoly && levelsOut > 3) {
            continue;
          } else if (!FireEventLogic.getInstance().isWithinMaxFireSpottingDistance(fromEvu, adj)) {
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
  }

  /**
   * Determines if a spot fire should start based on the fire spotting probabilities in the "Fire Event Logic" dialog.
   *
   * @param fromEvu A burning vegetation unit
   * @param toEvu A non-burning vegetation unit
   * @return True if a spot fire starts
   */
  private boolean determineSpotFire(Evu fromEvu, Evu toEvu) {

    Lifeform fromLifeform = fromEvu.getDominantLifeform();

    VegSimStateData state = fromEvu.getState(fromLifeform);
    if (state == null) return false;

    ProcessType processType = state.getProcess();
    
    Lifeform toLifeform = toEvu.getDominantLifeform();
    VegSimStateData toState = toEvu.getState(toLifeform);
    if (toState == null) return false;

    ProcessType toProcessType = toState.getProcess();
    if (toProcessType.isFireProcess()) return false;

    int prob = FireEventLogic.getInstance().getFireSpottingProbability(fromEvu, toEvu, processType, isExtreme);
    prob *= 100;

    int rand = Simulation.getInstance().random();

    boolean isSpot = (rand < prob);

    ProcessType fireType;

    if (isSpot) {

      fireType = FireEvent.getTypeOfFire(Simpplle.getCurrentZone(), toEvu, toLifeform);
      if (fireType == ProcessType.NONE) return false;

      if (Area.multipleLifeformsEnabled()) {
        toEvu.updateCurrentStateAllLifeforms(fireType,(short)Evu.SFS,season);
      } else {
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




/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.*;

/**
 * Represents a spreading fire event. An event starts at an origin unit and gradually spreads to adjacent units until
 * the fire is extinguished due to weather, a fire suppression line, or running out of vegetation units. This class
 * was not named FireEvent as that name is already taken to represent a kind of process.
 * <p>
 * Original source code authorship: Kirk A. Moeller
 */

public class ProcessOccurrenceSpreadingFire extends ProcessOccurrenceSpreading implements Externalizable {
  static final long serialVersionUID = -7840370421868476956L;
  static final int  version          = 1;

  public enum SpreadModel { SIMPPLLE, KEANE }

  private static SpreadModel spreadModel = SpreadModel.SIMPPLLE;

  /**
   * The multiplicative factor to apply to the wind speed when this fire event is extreme.
   */
  private static double keaneExtremeWindMultiplier = 4.0;

  /**
   * A wind speed variability in miles per hour.
   */
  private static double keaneWindSpeedVariability = 5.0;

  /**
   * Actual amount that wind speeds are changed,
   * calculated when a fire event is created.
   */
  private double keaneWindSpeedOffset;

  /**
   * A wind direction variability in degrees.
   */
  private static double keaneWindDirectionVariability = 45.0;

  /**
   * Actual amount that the wind direction will change,
   * calculated when a fire event is created.
   */
  private double keaneWindDirectionOffset;

  public enum EventStop { OTHER, WEATHER, LINE }

  private int            hoursBurning;
  private int            weatherProb;
  private int            weatherRangeIndex;
  private boolean        isExtreme;
  private Climate.Season fireSeason;
  private boolean        fireSuppressed;
  private int            fireSuppRandomNumber;
  private Node           lineProductionNode;
  private int            totalLineProduced;

  private EventStop eventStopReason = EventStop.OTHER;

  private ArrayList<Integer> lineSuppUnits = new ArrayList<>();

  /**
   * Constructs a spreading fire event. This is required for deserializing .simdata files.
   */
  public ProcessOccurrenceSpreadingFire() {
    super();
  }

  /**
   * Constructs a spreading fire event with an origin unit.
   *
   * @param evu A vegetation unit
   * @param lifeform A life form
   * @param processData A process probability
   * @param timeStep A time step (unused)
   */
  public ProcessOccurrenceSpreadingFire(Evu evu, Lifeform lifeform, ProcessProbability processData, int timeStep) {

    super(evu,lifeform,processData,timeStep);

    hoursBurning         = 0;
    weatherProb          = 0;
    weatherRangeIndex    = -1;
    isExtreme            = FireEvent.isExtremeSpread();
    fireSeason           = FireEvent.getFireSeason();
    fireSuppressed       = false;
    fireSuppRandomNumber = Simulation.getInstance().random();
    lineProductionNode   = null;
    totalLineProduced    = 0;

    if (spreadModel == SpreadModel.KEANE)
      rollKeaneOffsets();
  }

  /**
   * Finds wind speed and direction offset values used for this fire.
   *
   * The nextGaussian function returns random numbers with a mean of 0 and std dev of 1
   * Multiplying by 1/3 ensures that >99% percent of values will fall between -1 and 1.
   * @see java.util.Random#nextGaussian()
   */
  private void rollKeaneOffsets(){
    Random random = new Random();
    double newStdDev = 1.0/3.0;
    keaneWindSpeedOffset     = random.nextGaussian() * newStdDev * keaneWindSpeedVariability;
    keaneWindDirectionOffset = random.nextGaussian() * newStdDev * keaneWindDirectionVariability;
  }

  /**
   * @param value A fire spreading model
   */
  public static void setSpreadModel(SpreadModel value) {
    spreadModel = value;
  }

  /**
   * Update variable and let SystemKnowledge know it has been updated.
   * @param value An extreme wind multiplier for Keane spreading
   */
  public static void setKeaneExtremeWindMultiplier(double value) {
    keaneExtremeWindMultiplier = value;
    SystemKnowledge.markChanged(SystemKnowledge.KEANE_PARAMETERS);
  }

  /**
   * @return An extreme wind multiplier for Keane spreading
   */
  public static double getKeaneExtremeWindMultiplier() {
    return keaneExtremeWindMultiplier;
  }

  /**
   * Update variable and let SystemKnowledge know it has been updated.
   * @param value A wind speed variability factor for Keane spreading
   */
  public static void setKeaneWindSpeedVariability(double value) {
    keaneWindSpeedVariability = value;
    SystemKnowledge.markChanged(SystemKnowledge.KEANE_PARAMETERS);
  }

  /**
   * @return A wind speed variability factor for Keane spreading
   */
  public static double getKeaneWindSpeedVariability() {
    return keaneWindSpeedVariability;
  }

  /**
   * Update variable and let SystemKnowledge know it has been updated.
   * @param value A wind direction variability in degrees for Keane spreading
   */
  public static void setKeaneWindDirectionVariability(double value) {
    keaneWindDirectionVariability = value;
    SystemKnowledge.markChanged(SystemKnowledge.KEANE_PARAMETERS);
  }

  /**
   * @return A wind direction variability in degrees for Keane spreading
   */
  public static double getKeaneWindDirectionVariability() {
    return keaneWindDirectionVariability;
  }

  /**
   * @return True if this is an extreme fire event.
   */
  public boolean isExtremeEvent() {
    return isExtreme;
  }

  /**
   * @return True if this event uses fire suppression.
   */
  public boolean isSuppressed() {
    return fireSuppressed;
  }

  /**
   * @return The reason that this event stopped, or OTHER if it hasn't stopped.
   */
  public String getEventStopReason() {
    return eventStopReason.toString();
  }

  /**
   * @return The season {spring,summer,fall,winter} that this fire occurs.
   */
  public Climate.Season getFireSeason() {
    return fireSeason;
  }

  /**
   * @return The total length of fire suppression line produced in feet.
   */
  public int getLineProduced() {
    return totalLineProduced;
  }

  /**
   * De-serializes this object from an object stream.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    @SuppressWarnings("unused")
    int version = in.readInt();
    super.readExternal(in);
  }

  /**
   * Serializes this object to an object stream.
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);
  }

  /**
   * Writes Keane Parameters, used in System Knowledge save
   * @param printWriter an open print writer
   */
  public static void saveKeaneParameters(PrintWriter printWriter){
    printWriter.println(keaneExtremeWindMultiplier + ", " + keaneWindSpeedVariability + ", " +
        keaneWindDirectionVariability);
  }

  /**
   * Load Keane Parameters from a saved System Knowledge file
   * @param bufferedReader open stream
   */
  public static void loadKeaneParameters(BufferedReader bufferedReader) throws SimpplleError {
    try{
      String line = bufferedReader.readLine();
      if (line == null) {
        throw new ParseError("Keane Parameters file is empty");
      }
      String[] group = line.split(",");
      if (group.length > 3){
        throw new ParseError("Keane Parameters file has too many items.");
      } else if (group.length < 3){
        throw new ParseError("Keane Parameters file has too few items.");
      }

      keaneExtremeWindMultiplier    = Double.parseDouble(group[0]);
      keaneWindSpeedVariability     = Double.parseDouble(group[1]);
      keaneWindDirectionVariability = Double.parseDouble(group[2]);
    }
    catch (ParseError error){
      throw new SimpplleError(error.msg, error);
    } catch (IOException ex){
      throw new SimpplleError("Problems reading from fire season data file.");
    } catch (Exception e) {
      e.printStackTrace();
      throw new SimpplleError("Invalid or missing data in Fire Season Data File.");
    }
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

    int rangeIndex = FireSuppWeatherData.getAcresRangeNumber(getEventAcres());

    if (rangeIndex != weatherRangeIndex) {

      weatherProb = Simulation.getInstance().random();
      weatherRangeIndex = rangeIndex;

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

    fireSuppressed = FireSuppEventLogic.getInstance().isSuppressed(fromUnit, fireSuppRandomNumber);

    boolean fireSuppression = (Simulation.getInstance().fireSuppression()) ? fireSuppressed : false;
    boolean hasUniformPolygons = Simpplle.getCurrentArea().hasUniformSizePolygons();

    if (hasUniformPolygons && fireSuppression) {

      VegSimStateData state = fromUnit.getState();
      VegetativeType vegType = state.getVeg();
      Lifeform lifeform = state.getLifeform();

      int ts = Simulation.getCurrentTimeStep();

      double responseTime = root.data.getUnit().getFmz().getResponseTime();
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
              logFireLineStop(ts, firePerimeter);
            }
            return;
          }

          int sideLength = root.data.getUnit().getSideLength();

          double pctLine = (double)newLine / (double)sideLength;

          if (newLine >= sideLength) {

            theUnit = lineProductionNode.data.getUnit();
            theUnit.suppressFire(false);

            if (Simulation.getInstance().isDoSimLoggingFile()) {
              logFireLineSuppression(theUnit);
            }

            double extraLine = newLine - sideLength;
            double fProdRate = (double)prodRate;

            suppTime = -(extraLine / fProdRate);

          } else if (pctLine > 0.5) {
            Evu lowAdj = getNonBurningLowestNeighbor(fromUnit);
            if (lowAdj != null && !lineSuppUnits.contains(lowAdj.getId())) {
              lineSuppUnits.add(lowAdj.getId());

              if (Simulation.getInstance().isDoSimLoggingFile()) {
                logFireLineSuppression(lowAdj);
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

    switch (spreadModel) {
      case SIMPPLLE:
        doSimpplleSpread(fromUnit,tmpToUnits);
        break;
      case KEANE:
        doKeaneSpread(fromUnit,tmpToUnits);
        break;
    }

    doFireSpotting(fromUnit,tmpToUnits);
    addSpreadEvent(spreadingNode,tmpToUnits,lifeform);

    if (spreadQueue.size() == 0) {

      finished = true;
      eventStopReason = EventStop.OTHER;

      if (Simulation.getInstance().isDoSimLoggingFile()) {
        logFireEnd(Simulation.getCurrentTimeStep());
      }
      return;
    }

    FireEvent.currentEvent = null;
    Area.currentLifeform = null;
  }

  private void logFireEnd(int timeStep){
    PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
    int originUnitId = root.data.getUnit().getId();
    int firePerimeter = calculateApproxPerimeter();
    logOut.printf("%d,%d, Nowhere left to Spread, Line Produced: %d, Event Perimeter: %d %n",
        timeStep, originUnitId, totalLineProduced, firePerimeter);
  }

  private void logFireLineSuppression(Evu lowAdj){
    PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
    logOut.printf("%d,%d, Spread into Unit %d suppressed due to line production: %n",
        Simulation.getCurrentTimeStep(), root.data.getUnit().getId(), lowAdj.getId());
    //TODO: use same message for both?
//    logOut.printf("Time: %d, Origin Unit %d further spread from Unit %d suppressed due to line production: %n",
//        Simulation.getCurrentTimeStep(), root.data.getUnit().getId(), theUnit.getId());
  }

  private void logFireLineStop(int timeStep, int firePerimeter){
    PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
    int originUnitId = root.data.getUnit().getId();
    logOut.printf("%d,%d, Event stopped due to line production exceeding perimeter size %n",
        Simulation.getCurrentTimeStep(),originUnitId);
    logOut.printf("%d,%d, Line Produced: %d, Event Perimeter: %d %n",
        timeStep, originUnitId, totalLineProduced, firePerimeter);
  }

  /**
   * Spreads fire from a burning vegetation unit to adjacent vegetation units. If a unit catches on fire, then it is
   * added to the array list of 'to' units. This algorithm applies fire spreading logic to immediate neighbors.
   *
   * @param fromUnit A burning vegetation unit
   * @param toUnits A list to store units that have been spread to
   */
  private void doSimpplleSpread(Evu fromUnit, ArrayList<Evu> toUnits) {

    AdjacentData[] adjacentArray = fromUnit.getNeighborhood();

    if (adjacentArray != null) {

      for (AdjacentData adjacent : adjacentArray) {

        if (adjacent != null) {
          Evu toUnit = adjacent.evu;

          if (lineSuppUnits.contains(toUnit.getId())) continue;

          boolean alreadyBurning = toUnit.hasFireAnyLifeform();
          if (Evu.doSpread(fromUnit, toUnit, fromUnit.getDominantLifeformFire())) {
            if (!alreadyBurning) {
              toUnit.fromEvuId = fromUnit.getId();
              toUnits.add(toUnit);
            }
          }
        }
      }
    }
  }

  /**
   * Spreads fire from a burning vegetation unit to adjacent vegetation units. If a unit catches on fire, then it is
   * added to the array list of 'to' units. This algorithm applies fire spreading logic to neighbors around the 'from'
   * unit. The number of neighbors tested in each direction depends on wind direction, wind speed, and the slope of
   * each neighboring unit.
   *
   * @param fromUnit A burning vegetation unit
   * @param toUnits A list to store units that have been spread to
   */
  private void doKeaneSpread(Evu fromUnit, ArrayList<Evu> toUnits) {

    AdjacentData[] adjacentArray = fromUnit.getNeighborhood();

    if (adjacentArray != null) {

      for (AdjacentData adjacent : adjacentArray) {

        if (adjacent != null) {

          double windSpeed = adjacent.getWindSpeed();     // Miles per hour
          double windDirection = adjacent.getWindDirection(); // Degrees azimuth
          double spreadDirection = adjacent.getSpread();        // Degrees azimuth
          double slope = adjacent.getSlope();         // Percent slope / 100

          windSpeed += keaneWindSpeedOffset;

          // Use wind multiplier for extreme fires
          if (isExtreme) windSpeed *= keaneExtremeWindMultiplier;

          // Limit wind to 30mph max
          windSpeed = Math.min(30, windSpeed);

          // Offset the wind direction and truncate angle within 360 degrees
          windDirection = (windDirection + keaneWindDirectionOffset) % 360;

          double windSpread;

          if (windSpeed > 0.5) {

            // Compute a coefficient that reflects wind direction
            double coeff = Math.toRadians(fromUnit.getAzimuthDifference(spreadDirection, windDirection));

            // Compute the length:width ratio from Andrews (1986)
            double lwr = 1.0 + (0.125 * windSpeed);

            // Scale the coefficient between 0 and 1
            coeff = (Math.cos(coeff) + 1.0) / 2.0;

            // Scale the function based on wind speed between 1 and 10
            windSpread = lwr * Math.pow(coeff, Math.pow(windSpeed, 0.6));

          } else {

            windSpread = 1.0;

          }

          double slopeSpread;

          if (slope > 0.0) {

            slopeSpread = 4.0 / (1.0 + 3.5 * Math.exp(-10 * slope));

          } else {

            slopeSpread = Math.exp(-3 * slope * slope);

          }

          double spix = windSpread + slopeSpread;

          // Compensate for longer distances on corners
          if (spreadDirection == 45.0 ||
              spreadDirection == 135.0 ||
              spreadDirection == 225.0 ||
              spreadDirection == 315.0) {

            spix /= Math.sqrt(2);

          }

          List<AdjacentData> neighbors = fromUnit.getNeighborsAlongDirection(adjacent.getSpread(), rollSpix(spix));

          Evu prevUnit = fromUnit;

          for (AdjacentData neighbor : neighbors) {
            if (lineSuppUnits.contains(neighbor.evu.getId())) break;

            neighbor.setWind(prevUnit.isDownwind(spreadDirection, windDirection));

            boolean toUnitWasBurning = neighbor.evu.hasFireAnyLifeform();

            if (Evu.doSpread(prevUnit, neighbor.evu, prevUnit.getDominantLifeformFire())) {
              if (!toUnitWasBurning) {
                toUnits.add(neighbor.evu);
              }
            } else {
              break;
            }
            prevUnit = neighbor.evu;
          }
        }
      }
    }
  }

  /**
   * Function takes the fractional part (f) of the given spix, and has an
   * 'f %' chance to return the ceiling of the spix, otherwise returns the floor.
   *
   * @param spix floating point value
   * @return either the floor or ceiling of spix, based on the fractional part of spix
   */
  private int rollSpix(double spix){
    // find fraction
    double decimal = spix % 1;
    // get random decimal
    double r = Math.random();
    // round up if successful
    return decimal >= r ? (int)Math.ceil(spix) : (int)Math.floor(spix);
  }

  /**
   * Calculates an exact fire perimeter.
   *
   * @return The fire perimeter in feet
   */
  @SuppressWarnings("unchecked")
  public int calculatePerimeter() {

    int perimeter = 0;

    if (root == null) return 0;

    LinkedList queue = new LinkedList();
    queue.add(root);

    Node node;
    AdjacentData[] adjData;

    while (queue.size() > 0) {

      node = (Node)queue.removeFirst();
      if (node.data.getUnit().isSuppressed()) continue;

      adjData = node.data.getUnit().getNeighborhood();

      for (AdjacentData neighbor : adjData) {
        if (neighbor != null) {
          VegSimStateData adjState = neighbor.evu.getState();
          if (adjState == null || adjState.getProcess().isFireProcess()) continue;
          perimeter += neighbor.evu.getSideLength();
        }
      }

      if (node.toNodes == null) continue;

      for (int i=0; i<node.toNodes.length; i++) {
        queue.add(node.toNodes[i]);
      }
    }

    return perimeter;

  }

  /**
   * Calculates an approximate fire perimeter. The approximation assumes that the fire shape is square.
   *
   * @return The fire perimeter in feet
   */
  public int calculateApproxPerimeter() {
    float fEventAcres = Area.getFloatAcres(eventAcres);
    int eventSideLength = (int)Math.round(Math.sqrt(fEventAcres * 43560));
    return eventSideLength * 4;
  }

  /**
   * Determine if this unit has any neighbors that are not burning. If so, we can build line here. More ideal would be
   * finding only perimeter units, but not sure how best to achieve that right now without significantly affecting
   * performance.
   *
   * @param unit A vegetation unit with neighbors
   * @return True if a neighbor is burning
   */
  private boolean hasNonBurningNeighbors(Evu unit) {

    AdjacentData[] adjDataArray = unit.getNeighborhood();

    if (adjDataArray != null) {
      for (AdjacentData adjData : adjDataArray) {
        if (adjData != null) {
          if (!adjData.evu.hasFireAnyLifeform()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Finds the neighbor with the lowest elevation that is not burning.
   *
   * @param unit A vegetation unit with neighbors
   * @return A non-burning vegetation unit
   */
  private Evu getNonBurningLowestNeighbor(Evu unit) {

    int lowestElevation = Integer.MAX_VALUE;
    Evu lowestUnit = null;

    AdjacentData[] adjDataArray = unit.getNeighborhood();

    if (adjDataArray != null) {
      for (AdjacentData adjData : adjDataArray) {
        if (adjData != null) {
          int unitElevation = adjData.evu.getElevation();
          if (!adjData.evu.hasFireAnyLifeform() && unitElevation < lowestElevation) {
            lowestElevation = unitElevation;
            lowestUnit = adjData.evu;
          }
        }
      }
    }
    
    return lowestUnit;

  }

  /**
   * Finds the unit with the lowest elevation that does not have a stand replacing fire or a fire line. These
   * qualities are important as suppression forces choose to build line in this type of unit.
   *
   * 8/23/11  Added check for beyond A suppression
   * 8/24/11  Added check for non burning neighbors
   *
   * @return A vegetation unit fitting the requirements
   */
  @SuppressWarnings("unchecked")
  private Node findLowestElevationNonSrfNonSuppNode() {

    if (root == null) return null;

    FireSuppBeyondClassALogic logicInst = FireSuppBeyondClassALogic.getInstance();

    int ts = Simulation.getCurrentTimeStep();

    Node lowestNode = root;
    int lowestElevation = 1000000;

    LinkedList<Node> queue = new LinkedList<>();
    queue.add(root);

    while (queue.size() > 0) {

      Node node = queue.removeFirst();

      VegSimStateData state = node.data.getUnit().getState();

      int nodeElevation = node.data.getUnit().getElevation();

      if (state != null &&
          nodeElevation < lowestElevation &&
          logicInst.isSuppressedUniform(this,state.getVeg(),state.getProcess(),isExtreme,node.data.getUnit(),ts,lifeform) &&
          hasNonBurningNeighbors(node.data.getUnit()) &&
          !node.data.getProcess().equals(ProcessType.STAND_REPLACING_FIRE) &&
          !node.data.getUnit().isSuppressed()) {

        lowestNode = node;
        lowestElevation = nodeElevation;

      }

      if (node.toNodes != null) {
        for (Node toNode : node.toNodes) {
          queue.add(toNode);
        }
      }
    }

    return lowestNode;

  }

  /**
   * Creates spot fires from blowing embers. All vegetation units in the area that are downwind and within the maximum
   * fire spotting distance are tested for spot fires. Spot fires start based on a fire spotting rules entered in the
   * 'Fire Event Logic' dialog. If a fire starts, the type of the fire is determined by rules in the same dialog.
   *
   * @param fromUnit A burning vegetation unit
   * @param toUnits A list to store units that have been ignited
   */
  private void doFireSpotting(Evu fromUnit, ArrayList<Evu> toUnits) {

    if (!Utility.getFireSpotting()) return;

    Set<Evu> visited = new HashSet<>();
    Set<Evu> checkNow = new HashSet<>();
    Set<Evu> checkLater = new HashSet<>();

    visited.add(fromUnit);
    checkNow.add(fromUnit);

    boolean uniformPoly = Simpplle.getCurrentArea().hasUniformSizePolygons();
    
    int levelsOut = 0;

    while (checkNow.size() > 0) {
      for (Evu fromEvu : checkNow) {

        AdjacentData[] adjacencies = fromEvu.getNeighborhood();
        if (adjacencies == null) continue;

        for (AdjacentData adjacent : adjacencies) {

          if (adjacent != null) {

            Evu toEvu = adjacent.evu;
            if (toEvu == null) continue;

            if (visited.contains(toEvu)) continue;
            visited.add(toEvu);

            if (!uniformPoly && levelsOut > 3) {
              continue;
            }

            if (!FireEventLogic.getInstance().isWithinMaxFireSpottingDistance(fromUnit, toEvu)) {
              continue;
            }

            if (fromEvu.isAdjDownwind(toEvu) && !checkLater.contains(toEvu)) {
              checkLater.add(toEvu);
              if (determineSpotFire(fromUnit, toEvu)) {
                toUnits.add(toEvu);
              }
            }
          }
        }
      }

      Set<Evu> swap = checkNow;
      checkNow = checkLater;
      checkLater = swap;

      checkLater.clear();

      levelsOut++;

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
    Lifeform toLifeform = toEvu.getDominantLifeform();

    VegSimStateData fromState = fromEvu.getState(fromLifeform);
    if (fromState == null) return false;

    VegSimStateData toState = toEvu.getState(toLifeform);
    if (toState == null) return false;

    ProcessType fromProcess = fromState.getProcess();
    ProcessType toProcess = toState.getProcess();
    if (toProcess.isFireProcess()) return false;

    int prob = FireEventLogic.getInstance().getFireSpottingProbability(fromEvu, toEvu, fromProcess, isExtreme);
    int rand = Simulation.getInstance().random();

    boolean isSpot = rand < (prob * 100);

    if (isSpot) {

      ProcessType fireType = FireEvent.getTypeOfFire(Simpplle.getCurrentZone(), toEvu, toLifeform);
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

//  public void run() {
//    doSpread();
//    ((MyThreadGroup)thread.getThreadGroup()).decThreadCount();
//  }

}




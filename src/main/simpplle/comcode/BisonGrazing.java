/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/** 
 * This class is for Bison Grazing, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */

public class BisonGrazing extends Process {
  private int version = 5;

  private static Integer             defaultProb;
  private static ArrayList<Object[]> speciesProbData;
  private static ArrayList<Integer[]> fireHistData;
  private static ArrayList<Integer[]> waterDistData;
  private static ArrayList<Integer[]> landBurnedData;

  private static int fireHistWeight   = 33;
  private static int waterDistWeight  = 34;
  private static int landBurnedWeight = 33;


  private static final int SPECIES_PROB_DATA_COLUMN_COUNT = 2;
  private static final int FIRE_HIST_COLUMN_COUNT = 5;
  private static final int WATER_DIST_DATA_COLUMN_COUNT = 8;
  private static final int LAND_BURNED_DATA_COLUMN_COUNT = 5;

  public enum DataKind {SPECIES_PROB, FIRE_HIST, WATER_DIST, LAND_BURNED};

  private static int light = -1;
  private static int moderate = -1;
  private static int heavy    = -1;

  public static int getLight() { return light; }
  public static int getModerate() { return moderate; }
  public static int getHeavy()    { return heavy; }

  // used in simulations
  private static Evu.WaterUnitData isPermanentWater = new Evu.WaterUnitData();
/**
 * Constructor for Bison Grazing, sets the spreading to false, description to Bison Grazing and uniqueUI to true.
 */
  public BisonGrazing () {
    super();

    spreading   = false;
    description = "Bison Grazing";
    uniqueUI    = true;
  }

//  static {
//    defaultProb = 100;
//
//    speciesProbData = new ArrayList<Object[]>();
//    speciesProbData.add(new Object[] { Species.get("BARREN"), 0 });
//    speciesProbData.add(new Object[] { Species.get("RIPARIAN"), 0 });
//    speciesProbData.add(new Object[] { Species.get("WATER"), 0 });
//    speciesProbData.add(new Object[] { Species.get("PASM-VUOC-SPCO"), 0 });
//
//    fireHistData = new ArrayList<Integer[]>();
//    fireHistData.add(new Integer[] { 1, 2, 33, 0, 0});
//    fireHistData.add(new Integer[] { 3, 4, 0, 33, 0});
//    fireHistData.add(new Integer[] { 4, 9999, 0, 0, 33 });
//
//    waterDistData = new ArrayList<Integer[]>();
//    waterDistData.add(new Integer[] { 0, 700, 34, 0, 0, 20, 0, 0});
//    waterDistData.add(new Integer[] { 701, 1400, 0, 34, 0, 0, 20, 0});
//    waterDistData.add(new Integer[] { 1401, Integer.MAX_VALUE, 0, 0, 34, 0, 0, 20});
//
//    landBurnedData = new ArrayList<Integer[]>();
//    landBurnedData.add(new Integer[] { 0, 9, 33, 0, 0});
//    landBurnedData.add(new Integer[] { 10, 24, 0, 33, 0});
//    landBurnedData.add(new Integer[] { 25, 100, 0, 0, 33});
//  }
//
  /**
   * Gets the static default probability variable.
   * @return
   */
  public static Integer getDefaultProb() { return defaultProb; }

  /**
   * checks if fire history weight, the time since last fire+ water distance weight + land burned weight = 100 
   * @return true if equals 100, false if >100 or <100
   */
  public static boolean checkWeightTotals() {
    return ((fireHistWeight + waterDistWeight + landBurnedWeight) == 100);
  }

  /**
   * 
   * @param kind from datakind enumeration choices are SPECIES_PROB, FIRE_HIST, WATER_DIST, LAND_BURNED
   * @return returns true if total of all three weights (fire History, water distance, and land burned) total to the given weight for each process each
   * this will be used in Bison Grazing logic editor GUI
   */
  public static boolean checkTotals(DataKind kind) {
    switch (kind) {
      case FIRE_HIST:   return checkFireHistTotal();
      case WATER_DIST:  return checkWaterDistTotal();
      case LAND_BURNED: return checkLandBurnedTotal();
    }
    return true;
  }

  /**
   * Checks the data entered = fire historical weight.  
   * @return true if total of data array index 2-4 = total fire history weight, false if > or < fire history weight
   */
  private static boolean checkFireHistTotal() {
    for (Integer[] data : fireHistData) {
      int tot = data[2] + data[3] + data[4];
      if (tot != fireHistWeight) { return false; }
    }
    return true;
  }
  
  /**
   * Checks if total of water distance data array of both indexes 2-4 and indexes 5-7 = water distance weight  
   * @return true if sum of indexes 2-4 and indexes 5-7 both = water distance weight, false if either does not sum to waterDistWeight.
   */
  private static boolean checkWaterDistTotal() {
    for (Integer[] data : waterDistData) {
      int tot = data[2] + data[3] + data[4];
      if (tot != waterDistWeight) { return false; }

      tot = data[5] + data[6] + data[7];
      if (tot != waterDistWeight) { return false; }
    }
    return true;
  }
  
  /**
   * checks land burned total to make sure that sum of land burned data array indexes 2-4 = total land burned weight.   
   * @return true if the sum = total land burned weight
   */
  private static boolean checkLandBurnedTotal() {
    for (Integer[] data : landBurnedData) {
      int tot = data[2] + data[3] + data[4];
      if (tot != landBurnedWeight) { return false; }
    }
    return true;
  }
  /**
   * Gets the Historical fire weight.  
   * @return historical fire weight by default this is 33
   */
  public static int getFireHistWeight() {
    return fireHistWeight;
  }
/**
 * Gets the int representation of land burned weight
 * @return land burned weight.  By default this is 33
 */
  public static int getLandBurnedWeight() {
    return landBurnedWeight;
  }
/**
 * Gets the distance to water weight
 * @return distance to water weight.  by default this is 34
 */
  public static int getWaterDistWeight() {
    return waterDistWeight;
  }

  /**
   * Sets the default probability for bison grazing.  
   * @param value the Integer value that is to be set as the default probablity 
   * <p>and marks the PROCESS_PROB_LOGIC System Knowledge changed if the default probablity is not the current default probability
   *  
   */
  public static void setDefaultProb(Integer value) {
    if (defaultProb == value) {
      return;
    }

    defaultProb = value;
    SystemKnowledge.markChanged(SystemKnowledge.PROCESS_PROB_LOGIC);
  }

  /**
   * Sets the fire historical weight.
   * <p>If the weight set is different then the current weight, the weight value is changed and the System Knowledge PROCESS_PROB_LOGIC id marked changed 
   * @param weight the weight value to be set
   */
  public static void setFireHistWeight(int weight) {
    if (fireHistWeight == weight) { return; }
    fireHistWeight = weight;
    SystemKnowledge.markChanged(SystemKnowledge.PROCESS_PROB_LOGIC);
  }

  /**
   * Sets the Land Burned Weight.
   * <p>If the weight set is different then the current weight, the weight value is changed and the System Knowledge PROCESS_PROB_LOGIC marked as changed 
   * @param weight the weight value to be set
   */
  public static void setLandBurnedWeight(int weight) {
    if (landBurnedWeight == weight) { return; }
    landBurnedWeight = weight;
    SystemKnowledge.markChanged(SystemKnowledge.PROCESS_PROB_LOGIC);
  }
  
  /**
   * Sets the water distance weight
   * <p>If the weight set is different then the current weight, 
   * the weight value is changed and the System Knowledge PROCESS_PROB_LOGIC marked as changed 
   * @param weight the weight value to be set
   */

  public static void setWaterDistWeight(int weight) {
    if (waterDistWeight == weight) { return; }
    waterDistWeight = weight;
    SystemKnowledge.markChanged(SystemKnowledge.PROCESS_PROB_LOGIC);
  }
  
  /**
   * Gets the species probablity. if there is no species probability data return -1
   * <li> else loops through the entire ArrayList<Object[]> and pulls out the integer in the data[] at index 1.   Because of arrayList the array must be cast to Object[]
   * @param sp the species to be found
   * @return a -1 if species probability data is null or the species sought is not in the object array
   */

  public static int findSpeciesProb(Species sp) {
    if (speciesProbData == null) { return -1; }
    for(int i=0; i<speciesProbData.size(); i++) {
      Object[] data = (Object[])speciesProbData.get(i);
      if (data[0].equals(sp)) {
        return (Integer) data[1];
      }
    }
    return -1;
  }
  /**
   * calculate the probability of bison grazing in a given evaluated vegetative unit either light, moderate, or heavy based on season, distance to water
   * fire history, and land burned data.  
   * 
   * @param evu vegetative unit to have its Bison grazing calculated
   */
  public static void calcProbability(Evu evu) {
    int lightProb=0, moderateProb=0, heavyProb=0;

    light = 0;
    moderate = 0;
    heavy = 0;

    Climate.Season currentSeason  = Simpplle.getCurrentSimulation().getCurrentSeason();
    short          fireSeasonProb = evu.getFireSeasonProb();
    Climate.Season fireSeason     = evu.getFireSeason();
    switch (currentSeason) {
      case SPRING: return;
      case SUMMER:
        if (fireSeasonProb == 0) {
          break;
        }
        return;
      case FALL:
      case WINTER:
        if (fireSeasonProb > 0 && fireSeason == Climate.Season.SPRING) {
          break;
        }
        return;
    }

    if (currentSeason == Climate.Season.SPRING) {
      return;
    }


    int prob;

    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return; }

    prob = findSpeciesProb(species);
    if (prob == -1) {
      prob = defaultProb;
    }
    if (prob == 0) { return; }

    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    int ts;
    for (ts=cTime-1; ts>=0; ts--) {
      if (evu.hasProcess(ts,ProcessType.STAND_REPLACING_FIRE)) {
        break;
      }
    }

    int timeSinceFire = (ts > 0) ? (cTime - ts) : 50;
    Integer[] data;
    for (int i=0; i<fireHistData.size(); i++) {
      data = fireHistData.get(i);
      if (timeSinceFire >= data[0] && timeSinceFire <= data[1]) {
        heavyProb = data[2];
        moderateProb = data[3];
        lightProb    = data[4];
        break;
      }
    }

    
    isPermanentWater.permanentWater = true;
    double distToWater=evu.distanceToWater(isPermanentWater);

    boolean hasWater = isPermanentWater.permanentWater;

    for (int i=0; i<waterDistData.size(); i++) {
      data = waterDistData.get(i);
      if (distToWater >= data[0] && distToWater <= data[1]) {
        heavyProb    += hasWater ? data[2] : data[5];
        moderateProb += hasWater ? data[3] : data[6];
        lightProb    += hasWater ? data[4] : data[7];
        break;
      }
    }

    int fireAcres = Simpplle.getAreaSummary().getTotalFireEventAcres(cTime-1);
    int totAcres = Simpplle.getCurrentArea().getAcres();

    long pct = Math.round(((double)fireAcres / (double)totAcres) * 100.0);

    for (int i=0; i<landBurnedData.size(); i++) {
      data = landBurnedData.get(i);
      if (pct >= data[0] && pct <= data[1]) {
        heavyProb    += data[2];
        moderateProb += data[3];
        lightProb    += data[4];
      }
    }

    if (heavyProb > moderateProb && heavyProb > lightProb) {
      heavy = prob;
    }
    else if (moderateProb > lightProb && moderateProb > heavyProb) {
      moderate = prob;
    }
    else {
      light = prob;
    }
  }
/**
 * checks to see if an entered distance to water is the same as the permanent distance to water
 * @param tempDistance variable representing a measurement at a time, to water 
 * @param permDistance the permanent distance to water
 * @return true if permanent and temp distance are same
 */
  public static boolean isSameCategory(double tempDistance, double permDistance) {
    Integer[] data;
    for (int i=0; i<waterDistData.size(); i++) {
      data = waterDistData.get(i);
      if ((tempDistance >= data[0] && tempDistance <= data[1]) &&
          (permDistance >= data[0] && permDistance <= data[1])) {
        return true;
      }
    }
    return false;
  }
/**
 * Resets heavy variable to default (-1).
 */
  public static void resetHeavy() {
    heavy = -1;
  }
  /**
   * Resets moderate variable to default (-1).
   */
  public static void resetModerate() {
    moderate = -1;
  }
  /**
   * Resets light variable to default (-1).
   */
  public static void resetLight() {
    light = -1;
  }
/**
 * Based on the parameter data kind this method gets the probability data object in the corresponding data kind arraylist.  Species is a species object arraylist.  
 * All others are Integer objects.  
 * @param kind the data kind used to get the corresponding data arraylist.  
 * @param row the row where data is to be found
 * @param col the column which is an index into the object arrays.  
 * @return
 */
  public static Object getValueAt(DataKind kind, int row, int col) {
    switch (kind) {
      case SPECIES_PROB: return speciesProbData.get(row)[col];
      case FIRE_HIST: return fireHistData.get(row)[col];
      case WATER_DIST: return waterDistData.get(row)[col];
      case LAND_BURNED: return landBurnedData.get(row)[col];
      default: return null;
    }
  }
/**
 *  SPECIES_PROB_DATA_COLUMN_COUNT = 2;  FIRE_HIST_COLUMN_COUNT = 5;  WATER_DIST_DATA_COLUMN_COUNT = 8; LAND_BURNED_DATA_COLUMN_COUNT = 5
 * @param kind data kind used to find the type of columns 
 * @return column count, either 2, 5, 8, or 5 
 */
  public static int getColumnCount(DataKind kind) {
    switch (kind) {
      case SPECIES_PROB: return SPECIES_PROB_DATA_COLUMN_COUNT;
      case FIRE_HIST: return FIRE_HIST_COLUMN_COUNT;
      case WATER_DIST: return WATER_DIST_DATA_COLUMN_COUNT;
      case LAND_BURNED: return LAND_BURNED_DATA_COLUMN_COUNT;
      default: return 0;
    }
  }
/**
 * Method to get the number of rows for Species, Fire History, Water Distance, or Land burned.  
 * @param kind Data kind used to find row.  
 * @return the size of the arraylist for a particular species, fire history, water distance, or land burned.  The size is the count.  
 */
  public static int getRowCount(DataKind kind) {
    switch (kind) {
      case SPECIES_PROB: return speciesProbData.size();
      case FIRE_HIST: return fireHistData.size();
      case WATER_DIST: return waterDistData.size();
      case LAND_BURNED: return landBurnedData.size();
      default: return 0;
    }
  }
  /**
   * Gets the column class.  Since all data in Bison Grazing are integers, this returns Integer 
   * @param col
   * @return Integer.class
   */
  public static Class getColumnClass(int col) {
    return Integer.class;
  }
  
  /**
   * Gets Bison Grazing column name, based on a datakind and column Id.  
   * @param kind from enumeration choices are SPECIES_PROB, FIRE_HIST, WATER_DIST, LAND_BURNED 
   * @param col column number 
   * @return descriptor
   */

  public static String getColumnName(DataKind kind, int col) {
    switch (kind) {
      case SPECIES_PROB:
        switch (col) {
          case 0: return "Species";
          case 1: return "Probability";
          default: return "";
        }
      case FIRE_HIST:
        switch (col) {
          case 0: return "Min Time Since Fire";
          case 1: return "Max Time Since Fire";
          case 2: return "Heavy Grazing";
          case 3: return "Moderate Grazing";
          case 4: return "Light Grazing";
          default: return "";
        }
      case WATER_DIST:
        switch (col) {
          case 0: return "Min Distance";
          case 1: return "Max Distance";
          case 2: return "Perm. Heavy";
          case 3: return "Perm. Moderate";
          case 4: return "Perm. Light";
          case 5: return "Temp Heavy";
          case 6: return "Temp Moderate";
          case 7: return "Temp Light";
          default: return "";
        }
      case LAND_BURNED:
        switch (col) {
          case 0: return "Min % Land Burned";
          case 1: return "Max % Land Burned";
          case 2: return "Heavy Grazing";
          case 3: return "Moderate Grazing";
          case 4: return "Light Grazing";
          default: return "";
        }
      default:
        return "";
    }
  }
/**
 * Returns true if cell is editable.  By default this is false, but returns true for species if not column 0, fire history, water distance, and land burned. 
 * @param kind from enumeration choices are SPECIES_PROB, FIRE_HIST, WATER_DIST, LAND_BURNED
 * @param row
 * @param col
 * @return true if the cell is in column for fire_history, water distance, land burned, or species probablity is not 0 column, false for all other cells 
 */
  public static boolean isCellEditable(DataKind kind, int row, int col) {
    switch (kind) {
      case SPECIES_PROB: return col != 0;
      case FIRE_HIST: return true;
      case WATER_DIST: return true;
      case LAND_BURNED: return true;
      default:
        return false;
    }
  }
  
  /**
   * Sets the value at a particular column and row.
   * @param kind
   * @param aValue
   * @param row
   * @param col
   * @return
   */
  public static boolean setValueAt(DataKind kind, Object aValue, int row, int col) {
    if ((aValue instanceof Integer) == false) { return true; }
    Integer value = (Integer)aValue;
    Integer[] data;
    boolean   goodTotal=true;
    switch (kind) {
      case SPECIES_PROB:
        Object[] tmpData = speciesProbData.get(row);
        if (tmpData[col] == value) { return true; }
        tmpData[col] = value;
        SystemKnowledge.markChanged(SystemKnowledge.PROCESS_PROB_LOGIC);
        return true;
      case FIRE_HIST:
        data = fireHistData.get(row);
        break;
      case WATER_DIST:
        data = waterDistData.get(row);
        break;
      case LAND_BURNED:
        data = landBurnedData.get(row);
        break;
      default: return true;
    }
    if (col == 0 && value > data[col+1]) { return true; }
    if (col == 1 && value < data[col-1]) { return true; }
    if (data[col] == value) { return true; }

    data[col] = value;
    SystemKnowledge.markChanged(SystemKnowledge.PROCESS_PROB_LOGIC);

    return checkTotals(kind);
  }

/**
 * Adds a row to the species table.  This is because there is another species to be displayed.  
 * @param species 
 * @return true if there is a another species which then needs a new row.  
 */
  public static boolean addRow(Species species) {
    for (Object elem : speciesProbData) {
      Object[] data = (Object[])elem;
      if (species.equals(data[0])) { return false; }
    }
    Object[] data = new Object[2];
    data[0] = species;
    data[1] = 0;
    speciesProbData.add(data);

    return true;
  }
/**
 * Deletes a particular row.
 * @param row
 */
  public static void deleteRow(int row) {
    speciesProbData.remove(row);
  }
  /**
   * Deletes multiple rows
   * @param rows array of rows to be deleted.
   */
  public static void deleteRows(int[] rows) {
    for (int row : rows) {
      deleteRow(row);
    }
  }

  private ArrayList<Integer[]> readIntegerData(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int size = in.readInt();
    ArrayList<Integer[]> newData = new ArrayList<Integer[]>(size);
    for (int i=0; i<size; i++) {
      int itemSize = in.readInt();
      Integer[] item = new Integer[itemSize];
      for (int j=0; j<itemSize; j++) {
        item[j] = (Integer)in.readObject();
      }
      newData.add(item);
    }
    return newData;
  }
  /**
   * Method to read in logic to calculate bison grazing probability.  This includes fire history data, water distance data, 
   * land burned history data, fire historical weight, water distance weight, land burned weight
   *    
   * if version is less than version 5 the data must be cast to ArrayList<Integer>, if version is less than 3 weights must be gotten from integer data . 
   * 
   */
  public void readExternalProbabilityLogic(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    if (version >= 5) {
      fireHistData = readIntegerData(in);
      waterDistData = readIntegerData(in);
      landBurnedData = readIntegerData(in);
    }
    else {
      fireHistData = (ArrayList < Integer[] > ) in.readObject();
      waterDistData = (ArrayList < Integer[] > ) in.readObject();
      landBurnedData = (ArrayList < Integer[] > ) in.readObject();
    }

    if (version >= 3) {
      fireHistWeight = in.readInt();
      waterDistWeight = in.readInt();
      landBurnedWeight = in.readInt();
    }
    else {
      Integer[] data = fireHistData.get(0);
      fireHistWeight = data[2] + data[3] + data[4];

      data = waterDistData.get(0);
      waterDistWeight = data[2] + data[3] + data[4];

      data = landBurnedData.get(0);
      landBurnedWeight = data[2] + data[3] + data[4];
    }
    if (version >= 2) {
      defaultProb     = in.readInt();
      speciesProbData = (ArrayList<Object[]>)in.readObject();
    }
  }
/**
 * method to write data.  loops through the Integer [] stored in ArrayList<Integer[]> and writes out the data sored in each
 * @param out the object to be written
 * @param data the integer data arrays stored in an arrayList
 * @throws IOException
 */
  private void writeIntegerData(ObjectOutput out, ArrayList<Integer[]> data) throws IOException {
    out.writeInt(data.size());
    for (Integer[] item : data) {
      out.writeInt(item.length);
      for (int i=0; i<item.length; i++) {
        out.writeObject(item[i]);
      }
    }
  }
  /**
   * Writes to an extrernal source the logic used to calculate bison grazing probability.  
   * This will be in order, fire history data, water distance data, land burned data, 
   * fire historical weight, water distance weight, land burned weight, default probability and species probability data.  
   */
  public void writeExternalProbabilityLogic(ObjectOutput out) throws IOException {
    out.writeInt(version);

    writeIntegerData(out,fireHistData);
    writeIntegerData(out,waterDistData);
    writeIntegerData(out,landBurnedData);
    out.writeInt(fireHistWeight);
    out.writeInt(waterDistWeight);
    out.writeInt(landBurnedWeight);
    out.writeInt(defaultProb);
    out.writeObject(speciesProbData);
  }
}



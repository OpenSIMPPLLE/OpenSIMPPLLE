package simpplle.comcode;

import java.io.*;
import java.util.Formatter;
import java.util.Locale;
import java.util.zip.*;
import java.util.ArrayList;

import simpplle.comcode.zone.ColoradoFrontRange;
import simpplle.comcode.zone.ColoradoPlateau;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppression Weather Data.  Data to decide if fire will be suppressed by weather.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.logic.BaseLogic
 * 
 */
public class FireSuppWeatherData {
  private static final int version = 1;

  public static transient final int MIN_ACRES_COL   = 0;
  public static transient final int MAX_ACRES_COL   = 1;
  public static transient final int SPRING_COL = 2;
  public static transient final int SUMMER_COL = 3;
  public static transient final int FALL_COL   = 4;
  public static transient final int WINTER_COL = 5;

  public static transient final int NUM_COLUMNS = 6;

  public static ArrayList<FireSuppWeatherData> instances = new ArrayList<FireSuppWeatherData>();

  public static int getRowCount() { return instances.size(); }
  public static int getColumnCount() { return NUM_COLUMNS; }

  private Range acresRange;
  private int[] probability=new int[4];

  public static void save(ObjectOutputStream os) throws SimpplleError {
    try {
      os.writeInt(version);
      os.writeInt(instances.size());
      for (FireSuppWeatherData inst : instances) {
        os.writeInt(inst.acresRange.getLower());
        os.writeInt(inst.acresRange.getUpper());
        os.writeInt(inst.probability.length);
        for (int prob : inst.probability) {
          os.writeInt(prob);
        }
      }
      os.flush();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems reading file.",ex);
    }
  }

    /**
     * Reads in Firesuppweatherdata object
     * @param in
     * @throws SimpplleError
     */
  public static void read(ObjectInputStream in) throws SimpplleError {
    try {
      int version = in.readInt();
      int size = in.readInt();
      instances.clear();
      FireSuppWeatherData inst;
      for (int i=0; i<size; i++) {
        inst = new FireSuppWeatherData();
        inst.acresRange = new Range(in.readInt(),in.readInt());
        int probSize = in.readInt();
        for (int j=0; j<probSize; j++) {
          inst.probability[j] = in.readInt();
        }
        instances.add(inst);
      }
      setMaxToAreaAcres();
    }
    catch (Exception ex) {
      throw new SimpplleError("Problems writing file.",ex);
    }
  }

  private FireSuppWeatherData() {}

  private static void convert(int[] data) {
    instances.clear();
    RegionalZone zone = Simpplle.getCurrentZone();
    if ((zone instanceof simpplle.comcode.zone.SouthCentralAlaska) ||
        (zone instanceof ColoradoFrontRange) ||
        (zone instanceof ColoradoPlateau)) {
      convertSeasonsVersion(data);
    }
    else if (zone.isWyoming()) {
      convertSeasonsVersion(data);
    }
    else {
      convertNonSeasonsVersion(data);
    }
  }
  private static void convertSeasonsVersion(int[] data) {
    Range[] ranges = new Range[6];
    ranges[0] = new Range(25,50000);
    ranges[1] = new Range(50100,100000);
    ranges[2] = new Range(100100,500000);
    ranges[3] = new Range(500100,1000000);
    ranges[4] = new Range(1000100,5000000);
    ranges[5] = new Range(5000000,Integer.MAX_VALUE);

    for (int i=0; i<ranges.length; i++) {
      FireSuppWeatherData inst = new FireSuppWeatherData();
      inst.acresRange = ranges[i];
      inst.probability[Climate.Season.SPRING.ordinal()] = data[0];
      inst.probability[Climate.Season.SUMMER.ordinal()] = data[1];
      inst.probability[Climate.Season.FALL.ordinal()] = data[2];
      inst.probability[Climate.Season.WINTER.ordinal()] = data[3];
      instances.add(inst);
    }
  }
  private static void convertNonSeasonsVersion(int[] data) {
    Range[] ranges = new Range[6];
    ranges[0] = new Range(25,50000);
    ranges[1] = new Range(50100,100000);
    ranges[2] = new Range(100100,500000);
    ranges[3] = new Range(500100,1000000);
    ranges[4] = new Range(1000100,5000000);
    ranges[5] = new Range(5000000,Integer.MAX_VALUE);

    for (int i=0; i<ranges.length; i++) {
      FireSuppWeatherData inst = new FireSuppWeatherData();
      inst.acresRange = ranges[i];
      inst.probability[Climate.Season.SPRING.ordinal()] = data[i];
      inst.probability[Climate.Season.FALL.ordinal()]   = data[i];
      inst.probability[Climate.Season.SUMMER.ordinal()] = data[i];
      inst.probability[Climate.Season.WINTER.ordinal()] = data[i];
      instances.add(inst);
    }
  }

  private static void markDataChanged() {
    SystemKnowledge.markChanged(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
  }

  /**
    * Load a user provided Fire Suppression Weather data file.
    * @param path is a File, the pathname of the data file.
    * @return a boolean, true if file was successfully loaded.
    */
  public static void loadData(File path) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(path));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      readData(fin);
      fin.close();
      gzip_in.close();
    }
    catch (IOException e) {
      String msg = "Problems reading from Fire Suppression Weather data file:" +
                   path;
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  /**
   * Weather Probability data in the file is very simple.
   *
   * Each line contains groups of rows seperated by commas.
   * Each group delimited by commas is delimited by colons
   * the values are for WETTER:NORMAL:DRIER
   */
  public static void readData(BufferedReader fin) throws SimpplleError {
    String              line;
    simpplle.comcode.utility.StringTokenizerPlus strTok;

    try {
      int[] data = readDataRow(fin);
      convert(data);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A, false);
      setMaxToAreaAcres();
    }
    catch (NumberFormatException nfe) {
      String msg = "Invalid value found in fire suppression weather data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (ParseError pe) {
      System.out.println(pe.msg);
      throw new SimpplleError(pe.msg);
    }
    catch (IOException e) {
      String msg = "Problems read from fire suppression weather data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (Exception e) {
      String msg = "Invalid or missing data in Fire Suppression Weather Data File.";
      System.out.println(msg);
      e.printStackTrace();
      throw new SimpplleError(msg,e);
    }
  }

  private static int[] readDataRow(BufferedReader fin)
    throws ParseError, IOException
  {
    simpplle.comcode.utility.StringTokenizerPlus strTok, values;
    String              line;
    int                 numRows;
    int[]             result;

    line    = fin.readLine();
    strTok  = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
    numRows = strTok.countTokens();
    result  = new int[numRows];

    for(int row=0;row<numRows;row++) {
      result[row] = strTok.getIntToken();
    }
    return result;
  }

  // old file extension: firesuppweather

  /**
   *
   * @param zone
   * @param eventAcres rationalized number of acres in the event
   * @return
   */
  public static int getProbability(RegionalZone zone, int eventAcres, Climate.Season fireSeason) {
    for (int i=0; i<instances.size(); i++) {
      FireSuppWeatherData inst = instances.get(i);
      if (inst.acresRange.inRange(eventAcres)) {
        return inst.probability[fireSeason.ordinal()];
      }
    }
    return 0;
  }
  
  public static int getAcresRangeNumber(int eventAcres) {
    for (int i=0; i<instances.size(); i++) {
      FireSuppWeatherData inst = instances.get(i);
      if (inst.acresRange.inRange(eventAcres)) {
        return i;
      }
    }
    return 0;
  }


  // *** JTable Stuff ***
  // ********************
  public static Object getValueAt(int row, int col) {
    return ((FireSuppWeatherData)instances.get(row)).getValue(col);
  }

  public static FireSuppWeatherData getValueAt(int row) {
    return instances.get(row);
  }

  public Object getValue(int col) {
    switch (col) {
      case MIN_ACRES_COL: return simpplle.comcode.utility.Utility.getFloatAcres(acresRange.lower,Area.getAcresPrecision());
      case MAX_ACRES_COL: return simpplle.comcode.utility.Utility.getFloatAcres(acresRange.upper,Area.getAcresPrecision());
      case SPRING_COL: return probability[Climate.Season.SPRING.ordinal()];
      case FALL_COL:   return probability[Climate.Season.FALL.ordinal()];
      case SUMMER_COL: return probability[Climate.Season.SUMMER.ordinal()];
      case WINTER_COL: return probability[Climate.Season.WINTER.ordinal()];
      default: return "";
    }
  }
  public void setValueAt(int col, Object value) {
    switch (col) {
      case MIN_ACRES_COL: break;
      case MAX_ACRES_COL: break;
      case SPRING_COL: probability[Climate.Season.SPRING.ordinal()] = (Integer)value; break;
      case FALL_COL:   probability[Climate.Season.FALL.ordinal()]   = (Integer)value; break;
      case SUMMER_COL: probability[Climate.Season.SUMMER.ordinal()] = (Integer)value; break;
      case WINTER_COL: probability[Climate.Season.WINTER.ordinal()] = (Integer)value; break;
    }
    markDataChanged();
  }
  public static String getColumnName(int col) {
    switch (col) {
      case MIN_ACRES_COL: return "Event Acres Min";
      case MAX_ACRES_COL: return "Event Acres Max";
      case SPRING_COL:    return "Spring";
      case FALL_COL:      return "Fall";
      case SUMMER_COL:    return "Summer";
      case WINTER_COL:    return "Winter";
      default: return "";
    }
  }

  public static void splitRow(int row, int splitInegerAcres){
    int splitValue = Area.getRationalAcres(splitInegerAcres);

    FireSuppWeatherData instance = instances.get(row);

    FireSuppWeatherData newInst = new FireSuppWeatherData();

    int origLower = instance.acresRange.getLower();

    newInst.acresRange = new Range(origLower,splitValue);

    for(int i=0; i<newInst.probability.length; i++){
      newInst.probability[i] = instance.probability[i];
    }

    int oneAcre = Area.getRationalAcres(1);
    instance.acresRange.setLower(splitValue + oneAcre);

    instances.add(row,newInst);

    markDataChanged();
  }
  public static void mergeRowUp(int row) {
    FireSuppWeatherData rowData = instances.get(row);
    FireSuppWeatherData prevRowData = instances.get(row-1);

    int lower = prevRowData.acresRange.getLower();
    int upper = rowData.acresRange.getLower();

    rowData.acresRange.setLower(lower);
    rowData.acresRange.setUpper(upper);

    for (int i=0; i<rowData.probability.length; i++){
      rowData.probability[i] = Math.round((float)((rowData.probability[i] + prevRowData.probability[i]) /2));
    }
  }
  public static void mergeRowDown(int row) {
    FireSuppWeatherData rowData = instances.get(row);
    FireSuppWeatherData nextRowData = instances.get(row+1);

    int lower = rowData.acresRange.getLower();
    int upper = nextRowData.acresRange.getUpper();

    rowData.acresRange.setLower(lower);
    rowData.acresRange.setUpper(upper);

    for(int i=0; i<rowData.probability.length; i++){
      rowData.probability[i] = Math.round((float)((rowData.probability[i] + nextRowData.probability[i])/2));
    }
    instances.remove(row+1);
  }
  public static boolean isValidSplitAcres(int row, int splitIntegerAcres) {
    int splitValue = Area.getRationalAcres(splitIntegerAcres);

    FireSuppWeatherData instance = instances.get(row);

    int oneAcre = Area.getRationalAcres(1);
    int twoAcres = Area.getRationalAcres(2);
    int lower = instance.acresRange.getLower();
    int upper = instance.acresRange.getUpper() - twoAcres;
    float fLower = simpplle.comcode.utility.Utility.getFloatAcres(lower, Area.getAcresPrecision());
    if(fLower < 1.0){
      lower =1;
    }
    else {
      lower += oneAcre;
    }

    return (splitValue >= lower) && (splitValue <= upper);
  }

  public static String getValidSplitAcresDescription(int row) {
    FireSuppWeatherData instance = instances.get(row);

    int oneAcre = Area.getRationalAcres(1);
    int twoAcres = Area.getRationalAcres(2);
    int upperRat = instance.acresRange.getUpper() - twoAcres;

    float lower = simpplle.comcode.utility.Utility.getFloatAcres(instance.acresRange.getLower() + oneAcre, Area.getAcresPrecision());
    float upper = simpplle.comcode.utility.Utility.getFloatAcres(upperRat, Area.getAcresPrecision());

    StringBuilder sb = new StringBuilder();
    Formatter formatter = new Formatter(sb, Locale.US);

    if (lower < 1) {
      lower = 1.0f;
    }

    formatter.format("(%.0f-%.0f)", lower, upper);

    return sb.toString();
  }

  public static int getMinSplitAcres(int row){
    FireSuppWeatherData instance = instances.get(row);

    int oneAcre = Area.getRationalAcres(1);
    int lower = instance.acresRange.getLower()+oneAcre;

    float fLower = simpplle.comcode.utility.Utility.getFloatAcres(lower, Area.getAcresPrecision());

    return Math.round(fLower);
  }

  public static void setMaxToAreaAcres() {
    FireSuppWeatherData instance = instances.get(instances.size()-1);
    Area currentArea = Simpplle.getCurrentArea();
    if (currentArea == null) {
      setMaxToDefaultAcres();
      return;
    }

    int areaAcres = currentArea.getAcres();
    instance.acresRange.setUpper(areaAcres);
  }

  public static void setMaxToDefaultAcres(){
    FireSuppWeatherData instance = instances.get(instances.size()-1);
    instance.acresRange.setUpper(Integer.MAX_VALUE);
  }



}






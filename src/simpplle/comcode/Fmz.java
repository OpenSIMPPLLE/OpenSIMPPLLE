/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */


package simpplle.comcode;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.HashMap;

/**
 * This class contains methods for Fire Management Zones.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class Fmz {
  private static final int version = 1;

  public static final int A = 0;
  public static final int B = 1;
  public static final int C = 2;
  public static final int D = 3;
  public static final int E = 4;
  public static final int F = 5;

  private static final int NUM_CLASSES = 6;

  private static final float DEFAULT_RESPONSE_TIME = 0.5f;

  private String name;
  //private int    id;
  private float  naturalFires[];
  private float  manmadeFires[];
  private float  percentFires[];
  private float  cost[];
  private float  totalFires;
  private float  acres;
  private float  firesPerAcre;
  private float  suppressionCost;

  private float responseTime;

  private static boolean changed;

  //private static int lastId = 0;

  public static HashMap<Short,Fmz> simIdHm =
    new HashMap<Short,Fmz>();
  private short simId=-1; // Random Access File ID
  public static short nextSimId=0;
  public short getSimId() {
    if (simId == -1) {
      simId = nextSimId;
      nextSimId++;
      simIdHm.put(simId,this);
    }
    return simId;
  }

  /**
   * Needs to be present for database, does nothing.
   * @param id short
   */
  public void setSimId(short id) { }

  public static Fmz lookUp(short simId) { return simIdHm.get(simId); }

  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      Fmz fmz = (Fmz)in.readObject();
      simIdHm.put(id,fmz);
      if ( (id+1) > nextSimId) {
        nextSimId = (short)(id+1);
      }
    }
  }
  public static void writeExternalSimIdHm(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeInt(simIdHm.size());
    for (Short id : simIdHm.keySet()) {
      out.writeShort(id);
      Fmz fmz = simIdHm.get(id);
      out.writeObject(fmz);
    }
  }

/**
 * Constructor for firemanagement zones.  Makes and initializes naturalfires, manmadefires, percentfires, cost arrays, then 
 * sets the acres, totalFirs, firesperacre, and suppression cost variables for this Fire Management Zone.  
 */
  public Fmz() {
    naturalFires = new float[NUM_CLASSES];
    manmadeFires = new float[NUM_CLASSES];
    percentFires = new float[NUM_CLASSES];
    cost         = new float[NUM_CLASSES];

    for(int i=0;i<NUM_CLASSES;i++) {
      acres           = 0.0f;
      totalFires      = 0;
      firesPerAcre    = 0;
      suppressionCost = 0;
      naturalFires[i] = 0.0f;
      manmadeFires[i] = 0.0f;
      percentFires[i] = 0.0f;
      cost[i]         = 0.0f;
    }
    //id           = 0;
  }
/**
 * Overloaded fire management zone.  Creates a new Fire management zone object and sets its information. 
 * @param name name of fire management zone
 * @param acres acres array 
 * @param nf natural fires array
 * @param mmf manmade fires array
 * @param cost cost array
 * @param time
 */
  public Fmz(String name, float acres, float nf[], float mmf[], float cost[], float time) {
    this();
    setName(name);
    updateFmz(acres,nf,mmf,cost, time);
  }
/**
 * Overloaded fire management zone constructor.  Used to temporarily create a fmz object for use in creating new areas.  
 * sets the name to parameter names and makes the natural fires, manamade fires, percent fires, and cost arrays null.  
 * @param newName
 */
  // Use to temporarily create an fmz for use in creating new areas.
  public Fmz(String newName) {
    naturalFires = null;
    manmadeFires = null;
    percentFires = null;
    cost         = null;
    name         = newName;
  }
/**
 * Sets the default fmz for current zone. 
 */
  public static void makeDefault() {
    String name = Simpplle.getCurrentZone().getDefaultFmzName();
    Fmz    newFmz = new Fmz();
    newFmz.setName(name);
    Simpplle.getCurrentZone().addFmz(newFmz);
  }

  /**
   * Used when loading areas.  If the loaded fmz does not exist,
   * then create one which has that name but is a copy of the
   * default fmz.  This way if a user later on loads an fmz data
   * file the units fmz will get correctly mapped to the newly
   * loaded version.
   */
  public Fmz duplicate(String newName) {
    Fmz tmpFmz = new Fmz();
    tmpFmz.setName(newName);
    tmpFmz.updateFmz(acres,naturalFires,manmadeFires,cost,responseTime);
    return tmpFmz;
  }
/**
 * Gets the fire management zone name.  
 */
  public String toString() {
    return name;
  }
/**
 * Checks if this fire management zone object is the current zones default fmz.  
 * @return true if this fire management zone object is the current zones default.  
 */
  public boolean isDefault() {
    RegionalZone zone = Simpplle.getCurrentZone();

    return (name.equals(zone.getDefaultFmzName()));
  }
/**
 * Gets the number of fire management classes.  This will return 6.  FmClasses are 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
 * @return 6 = number of fire management classes
 */
  public static int getNumClasses() { return NUM_CLASSES; }
/**
 * Gets this fire management zone name.  
 * @return name of fmz 
 */
  public String getName() { return name; }
  /**
   * Sets the name of this fire management zone. 
   * @param newName the name to be set for this fmz
   */
  public void setName(String newName) { name = newName; }
/**
 * Gets this fire management zones acreage - in float form.  
 * @return acreage of fire management zone. 
 */
  public float getAcres() { return acres; }
  /**
   * Sets this fire management zones acreage - in float form. Then updates the fmz totals and sets marks system knowledge changed.
   * @param newAcres
   */
  public void setAcres(float newAcres) {
    acres = newAcres;
    updateFmzTotals();
    markChanged();
  }
/**
 * Gets the natural fires of a particular class.  
 * @param classId Fire Classes are 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
 * @return  the natural fires of a particular fire class
 */
  public float getNaturalFires(int classId) { return naturalFires[classId]; }
  /**
   * Sets the natural fires float value for a particuar fire class.  Marks the knowledge changed.  
   * @param classId Fire Classes are 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
   * @param value float value of natural fires 
   */
  public void setNaturalFires(int classId, float value) {
    naturalFires[classId] = value;
    updateFmzTotals();
    markChanged();
  }
  /**
   * Gets the manmade fires of a particular class.  
   * @param classId Fire Classes are 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
   * @return  the manmade fires of a particular fire class
   */
  public float getManmadeFires(int classId) { return manmadeFires[classId]; }
  
  /**
   * Sets the manmade fires float value for a particuar fire class.  Marks the knowledge changed.  
   * @param classId Fire Classes are 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
   * @param value float value of manmade fires 
   */ 
  public void setManmadeFires(int classId, float value) {
    manmadeFires[classId] = value;
    updateFmzTotals();
    markChanged();
  }

  public static double getFloatCost(long cost, double divisor) {
    return ((double)cost / divisor );
  }
  public static int getRationalCost(float cost) {
    return Math.round(100.0f * cost);
  }
  public float getCost(int classId) { return cost[classId]; }
  public void setCost(int classId, float value) {
    cost[classId] = value;
    markChanged();
  }

  public static boolean hasChanged() { return changed; }
  private static void markChanged() {
    setChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.FMZ);
  }
  private static void setChanged(boolean value) { changed = value; }

  //public int getId() { return id; }
  //private void setId() {
  //  id = lastId;
  //  lastId++;
  //}
/**
 * Updates all the variables for this fire management zones  
 * @param acres float representing fmz acreage
 * @param nf natural fires array 
 * @param mmf manmade fires array
 * @param cost cost array
 * @param time response time.  
 */
  public void updateFmz(float acres, float nf[], float mmf[], float cost[], float time) {
    this.acres        = acres;
    this.responseTime = time;

    for(int i=0;i<nf.length;i++) {
      naturalFires[i] = nf[i];
      manmadeFires[i] = mmf[i];
      this.cost[i]    = cost[i];
    }
    updateFmzTotals();
  }
/**
 * Updates the fmz totals by adding all the fires together, calculating fires per acre, and calculating a percentage of fires, 
 */
  public void updateFmzTotals() {
    int i;

    totalFires = 0;
    for(i=0;i<naturalFires.length;i++) {
      totalFires += naturalFires[i] + manmadeFires[i];
    }
    if (acres > 0.0f) {
      firesPerAcre = totalFires / acres;
    }
    else {
      firesPerAcre = 0.0f;
    }

    for(i=0;i<naturalFires.length;i++) {
      if (totalFires > 0.0f) {
        percentFires[i] = (naturalFires[i] + manmadeFires[i]) / totalFires;
        percentFires[i] *= 100;
      }
      else {
        percentFires[i] = 0.0f;
        continue;
      }
    }
  }

  public int getRationalSuppressionCost(int acres) {
    int index = getRationalSizeClass(acres);

    return Math.round(100.0f * cost[index]);
  }
  public float getSuppressionCost(float acres) {
    int index = getSizeClass(acres);

    return cost[index];
  }
/**
 * Calculates the rational size class of a fire.   Fire Classes are 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
 * @param acres
 * @return
 */
  public static int getRationalSizeClass(int acres) {
    final int ACRES_1    = Area.getRationalAcres(1.0f);
    final int ACRES_10   = Area.getRationalAcres(10.0f);
    final int ACRES_100  = Area.getRationalAcres(100.0f);
    final int ACRES_300  = Area.getRationalAcres(300.0f);
    final int ACRES_1000 = Area.getRationalAcres(1000.0f);

    if      (acres < ACRES_1) { return A; }
    else if (acres >= ACRES_1   && (acres < ACRES_10)) { return B; }
    else if (acres >= ACRES_10  && (acres < ACRES_100)) { return C; }
    else if (acres >= ACRES_100 && (acres < ACRES_300)) { return D; }
    else if (acres >= ACRES_300 && (acres < ACRES_1000)) { return E; }
    else { return F; }  // acres > 1000.0
  }

  public static int getSizeClass(float acres) {
    if      (acres < 1.0)                        { return A; }
    else if (acres >=   1.0 && (acres <   10.0)) { return B; }
    else if (acres >=  10.0 && (acres <  100.0)) { return C; }
    else if (acres >= 100.0 && (acres <  300.0)) { return D; }
    else if (acres >= 300.0 && (acres < 1000.0)) { return E; }
    else { return F; }  // acres > 1000.0
  }

  public double calculateProbability(float unitAcres) {
    return ( (firesPerAcre * unitAcres) * 100.0);
  }
/**
 * Gets a percentage of fires in a particular fire class. Fire Classes are 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
 * @param fireClass
 * @return
 */
  public float getPercentFires(int fireClass) {
    return percentFires[fireClass];
  }

  // ** File I/O Related **
  // **********************

  public static void setFilename(File file) {
    SystemKnowledge.setFile(SystemKnowledge.FMZ,file);
    SystemKnowledge.markChanged(SystemKnowledge.FMZ);
  }
/**
 * Clears the system knowledge fire managment zone file.  
 */
  public static void clearFilename() {
    SystemKnowledge.clearFile(SystemKnowledge.FMZ);
  }
/**
 * Loads fmz data from file.
 * @param infile
 * @throws SimpplleError
 */
  public static void loadData (File infile) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(infile));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      readData(fin);
      setFilename(infile);
      fin.close();
      gzip_in.close();
    }
    catch (IOException e) {
      String msg = "Problems reading from FMZ data file:" + infile;
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }
/**
 * Loads fmz data from input stream.  
 * @param is
 * @throws SimpplleError
 */
  public static void loadData(InputStream is) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader fin;

    try {
      gzip_in = new GZIPInputStream(is);
      fin = new BufferedReader(new InputStreamReader(gzip_in));
      readData(fin);
      // *** Important ***
      // DO NOT CLOSE THESE STREAMS.
      // IT WILL CAUSE READING FROM JAR FILES TO FAIL.
    }
    catch (IOException e) {
      String msg = "Problems reading from FMZ data file\n" +
                   "knowledge.jar file may be missing or damaged.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }
/**
 * 
 * @param fin
 * @throws SimpplleError
 * Each line represents an FMZ.
 * The line is comma delimited the fields are:
 * name, id, acres, natural fires, manmade fires, cost.
 * the last three fields are further subdived using colons.
 * Note: The first line of the input file is the number
 * of lines in the file.
 */
  
  public static void readData(BufferedReader fin) throws SimpplleError {
    String              line, field;
    StringTokenizerPlus strTok, fieldStrTok;
    int                 count, i, j;
    Fmz                 fmz;
    String              fmzNames[];
    String              theName;
    float               theAcres;
    float               theNaturalFires[], theManMadeFires[], theCost[];
    RegionalZone        zone = Simpplle.getCurrentZone();
    String              msg;
    boolean             newFmz = false;
    int                 id = 0;

    try {
      line   = fin.readLine();
      if (line == null) {
        throw new ParseError("FMZ Data file is empty.");
      }

      // Read in the the list of Fmz names contained in this file.
      // We want to delete all the fmz's in the allFmz
      // hashtable that are not contained in this file.
      strTok = new StringTokenizerPlus(line,",");
      count  = strTok.countTokens();
      fmzNames = new String[count];
      for(i=0;i<count;i++) {
        fmzNames[i] = strTok.getToken().toLowerCase();
      }
      zone.updateAllFmz(fmzNames);

      theNaturalFires = new float[NUM_CLASSES];
      theManMadeFires = new float[NUM_CLASSES];
      theCost         = new float[NUM_CLASSES];

      for(i=0;i<count;i++) {
        line    = fin.readLine();
        strTok  = new StringTokenizerPlus(line,",");
        if (strTok.countTokens() < 5) {
          msg = "Incorrect number of fields in the following line:\n"
                + line + "\n" + "There should be at least five fields.";
          throw new ParseError(msg);
        }

        theName = strTok.getToken().toLowerCase();
        fmz     = zone.getFmz(theName);
        if (fmz == null) {
          fmz    = new Fmz();
          newFmz = true;
        }
        else { newFmz = false; }

        theAcres = strTok.getFloatToken();

        // Get the Natural Fires.
        field       = strTok.getToken();
        fieldStrTok = new StringTokenizerPlus(field,":");
        if (fieldStrTok.countTokens() != NUM_CLASSES) {
          msg = "Incorrect number of items in Natural " +
                "Fires Field of FMZ " + theName;
          throw new ParseError(msg);
        }
        for(j=0;j<NUM_CLASSES;j++) {
          theNaturalFires[j] = fieldStrTok.getFloatToken();
        }

        // Get the Man Made Fires.
        field       = strTok.getToken();
        fieldStrTok = new StringTokenizerPlus(field,":");
        if (fieldStrTok.countTokens() != NUM_CLASSES) {
          msg = "Incorrect number of items in Man Made " +
                "Fires Field of FMZ " + theName;
          throw new ParseError(msg);
        }
        for(j=0;j<NUM_CLASSES;j++) {
          theManMadeFires[j] = fieldStrTok.getFloatToken();
        }

        // Get the Cost.
        field       = strTok.getToken();
        fieldStrTok = new StringTokenizerPlus(field,":");
        if (fieldStrTok.countTokens() != NUM_CLASSES) {
          msg = "Incorrect number of items in Cost " +
                "Field of FMZ " + theName;
          throw new ParseError(msg);
        }
        for(j=0;j<NUM_CLASSES;j++) {
          theCost[j] = fieldStrTok.getFloatToken();
        }

        float time;
        if (strTok.hasMoreTokens()) {
          time = strTok.getFloatToken();
        }
        else {
          time = DEFAULT_RESPONSE_TIME;
        }
        fmz.updateFmz(theAcres,theNaturalFires,theManMadeFires,theCost,time);
        if (newFmz) {
          fmz.setName(theName);
          //fmz.setId();
          zone.addFmz(fmz);
        }
      }
      setChanged(false);
    }
    catch (NumberFormatException nfe) {
      msg = "Invalid numeric value found in Fmz data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (ParseError pe) {
      System.out.println(pe.msg);
      throw new SimpplleError(pe.msg);
    }
    catch (IOException e) {
      msg = "Problems read from FMZ data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  public static void saveAs(File outfile) {
    setFilename(Utility.makeSuffixedPathname(outfile,"","fmz"));
    save();
  }
/**
 * Method to save fmz information to a system knowledge file for FMZ.  
 */
  public static void save() {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.FMZ);

    GZIPOutputStream out;
    PrintWriter      fout;

    try {
      out = new GZIPOutputStream(new FileOutputStream(outfile));
      fout = new PrintWriter(out);
    }
    catch (IOException e) {
      System.out.println("Problems opening output file.");
      return;
    }

    save(fout);
    fout.flush();
    fout.close();

    setChanged(false);
  }

  public static void save(PrintWriter fout) {
    RegionalZone zone = Simpplle.getCurrentZone();
    Fmz[]        allFmz = zone.getAllFmz();

    int i;
    for(i=0;i<allFmz.length;i++) {
      if (i != 0) { fout.print(","); }
      fout.print(allFmz[i].getName());
    }
    fout.println();

    for(i=0;i<allFmz.length;i++) {
      allFmz[i].saveData(fout);
      fout.println();
    }
  }

  public void saveData(PrintWriter fout) {
    int i;

    fout.print(getName() + "," + getAcres() + ",");

    for(i=0;i<NUM_CLASSES;i++) {
      if (i != 0) { fout.print(":"); }
      fout.print(getNaturalFires(i));
    }
    fout.print(",");

    for(i=0;i<NUM_CLASSES;i++) {
      if (i != 0) { fout.print(":"); }
      fout.print(getManmadeFires(i));
    }
    fout.print(",");

    for(i=0;i<NUM_CLASSES;i++) {
      if (i != 0) { fout.print(":"); }
      fout.print(getCost(i));
    }
    fout.print(",");

    fout.print(responseTime);
  }

  public static void closeFile() {
    clearFilename();
    setChanged(false);
  }

  // ** Response time related methods.
  /**
   * Gets the response time for a particular EVU
   * @param unit evu being evaluated for response time
   * @return the response time
   */
  public static float getResponseTime(Evu unit) {
    return unit.getFmz().getResponseTime();
  }
/**
 * Gets the response time for fmz.
 * @return
 */
  public float getResponseTime() {
    return responseTime;
  }
  /**
   * Sets the response time for fmz 
   * @param newTime
   */
  public void setResponseTime(float newTime) {
    responseTime = newTime;
    markChanged();
  }

}




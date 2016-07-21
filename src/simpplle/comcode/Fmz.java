/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */


package simpplle.comcode;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.HashMap;

/**
 * This class contains methods for Fire Management Zones.
 * Fire classes: 0 = A, 1 = B, 2 = C ,3 = D, 4 = E, 5 = F
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

  private float acres;
  private float firesPerAcre;
  private float responseTime;
  private float totalFires;

  private float[] naturalFires;
  private float[] manmadeFires;
  private float[] percentFires;
  private float[] cost;

  private static boolean changed;

  public static Map<Short,Fmz> simIdHm = new HashMap<>();
  private short simId=-1; // Random Access File ID
  public static short nextSimId=0;

  /**
   * Creates a fire management zone.
   */
  public Fmz() {

    naturalFires = new float[NUM_CLASSES];
    manmadeFires = new float[NUM_CLASSES];
    percentFires = new float[NUM_CLASSES];
    cost         = new float[NUM_CLASSES];

    for (int i = 0; i < NUM_CLASSES; i++) {

      acres           = 0.0f;
      totalFires      = 0;
      firesPerAcre    = 0;
      naturalFires[i] = 0.0f;
      manmadeFires[i] = 0.0f;
      percentFires[i] = 0.0f;
      cost[i]         = 0.0f;

    }
  }

  /**
   * Creates a named fire management zone with specific values.
   *
   * @param name A name
   * @param acres An array of acres; one per fire class
   * @param nf An array of natural fires; one per fire class
   * @param mmf An array of manmade fires; one per fire class
   * @param cost An array of suppression costs; one per fire class
   * @param time Response time
   */
  public Fmz(String name, float acres, float nf[], float mmf[], float cost[], float time) {
    this();
    setName(name);
    updateFmz(acres,nf,mmf,cost, time);
  }

  /**
   * Creates a temporary named fire management zone. This is used while creating new areas.
   *
   * @param name A name
   */
  public Fmz(String name) {
    this.naturalFires = null;
    this.manmadeFires = null;
    this.percentFires = null;
    this.cost         = null;
    this.name         = name;
  }

  /**
   * Returns a unique numeric identifier for this fire management zone.
   */
  public short getSimId() {
    if (simId == -1) {
      simId = nextSimId;
      nextSimId++;
      simIdHm.put(simId,this);
    }
    return simId;
  }

  /**
   * Sets the default fmz for current zone.
   */
  public static void makeDefault() {
    Fmz newFmz = new Fmz();
    newFmz.setName(Simpplle.getCurrentZone().getDefaultFmzName());
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
   * Checks if this fire management zone object is the current zones default fmz.
   * @return True if this fire management zone is default
   */
  public boolean isDefault() {
    RegionalZone zone = Simpplle.getCurrentZone();
    return (name.equals(zone.getDefaultFmzName()));
  }

  /**
   * Returns the number of fire management classes.
   */
  public static int getNumClasses() {
    return NUM_CLASSES;
  }

  /**
   * Returns the name of this fire management zone.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this fire management zone.
   */
  public void setName(String newName) {
    name = newName;
  }

  /**
   * Returns the number of acres in this fire management zone.
   */
  public float getAcres() {
    return acres;
  }

  /**
   * Sets the number of acres in this fire management zone.
   */
  public void setAcres(float newAcres) {
    acres = newAcres;
    updateFmzTotals();
    markChanged();
  }

  /**
   * Returns natural fires in a particular fire class.
   */
  public float getNaturalFires(int classId) { return naturalFires[classId]; }

  /**
   * Sets natural fires in a particular fire class.
   */
  public void setNaturalFires(int classId, float value) {
    naturalFires[classId] = value;
    updateFmzTotals();
    markChanged();
  }

  /**
   * Returns human caused fires in a particular class.
   */
  public float getManmadeFires(int classId) {
    return manmadeFires[classId];
  }
  
  /**
   * Sets human caused fires in a particular class.
   */ 
  public void setManmadeFires(int classId, float value) {
    manmadeFires[classId] = value;
    updateFmzTotals();
    markChanged();
  }

  /**
   * Returns the fire suppression cost for a fire class.
   */
  public float getCost(int classId) {
    return cost[classId];
  }

  /**
   * Sets the fire suppression cost for a fire class.
   */
  public void setCost(int classId, float value) {
    cost[classId] = value;
    markChanged();
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

  /**
   * Returns true if values in this fire management zone have changed.
   */
  public static boolean hasChanged() {
    return changed;
  }

  /**
   * Flags one or more changes to this fire management zone.
   */
  private static void markChanged() {
    setChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.FMZ);
  }

  // TODO: Remove this method
  private static void setChanged(boolean value) {
    changed = value;
  }

  /**
   * Converts a fixed-point fire suppression cost to floating-point.
   */
  public static double rationalToFloatCost(long cost, double divisor) {
    return ((double)cost / divisor );
  }

  /**
   * Converts a floating-point fire suppression cost to fixed-point.
   */
  public static int floatToRationalCost(float cost) {
    return Math.round(100.0f * cost);
  }

  /**
   * Updates all the variables for this fire management zones
   * @param acres float representing fmz acreage
   * @param nf natural fires array
   * @param mmf manmade fires array
   * @param cost cost array
   * @param time response time.
   */
  // TODO: Remove this method and just use setters
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

    totalFires = 0;
    for (int i = 0; i < naturalFires.length; i++) {
      totalFires += naturalFires[i] + manmadeFires[i];
    }

    if (acres > 0.0f) {
      firesPerAcre = totalFires / acres;
    } else {
      firesPerAcre = 0.0f;
    }

    for (int i = 0; i < naturalFires.length; i++) {
      if (totalFires > 0.0f) {
        percentFires[i] = (naturalFires[i] + manmadeFires[i]) / totalFires;
        percentFires[i] *= 100;
      } else {
        percentFires[i] = 0.0f;
      }
    }
  }

  /**
   *
   * @param acres
   * @return
   */
  public int getRationalSuppressionCost(int acres) {
    int index = getRationalSizeClass(acres);

    return Math.round(100.0f * cost[index]);
  }

  /**
   *
   * @param acres
   * @return
   */
  public float getSuppressionCost(float acres) {
    int index = getSizeClass(acres);

    return cost[index];
  }

  /**
   * Calculates the rational size class of a fire.
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

  /**
   *
   * @param acres
   * @return
   */
  public static int getSizeClass(float acres) {
    if      (acres < 1.0)                        { return A; }
    else if (acres >=   1.0 && (acres <   10.0)) { return B; }
    else if (acres >=  10.0 && (acres <  100.0)) { return C; }
    else if (acres >= 100.0 && (acres <  300.0)) { return D; }
    else if (acres >= 300.0 && (acres < 1000.0)) { return E; }
    else { return F; }  // acres > 1000.0
  }

  /**
   *
   * @param unitAcres
   * @return
   */
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

  /**
   * Reads a fire management zone from an object input stream.
   *
   * @param in An object input stream
   * @throws IOException
   * @throws ClassNotFoundException
   */
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

  /**
   * Writes a fire management zone to an object output stream.
   *
   * @param out An object output stream
   * @throws IOException
   */
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
   *
   * @param file
   */
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
    try {
      GZIPInputStream gzip_in = new GZIPInputStream(new FileInputStream(infile));
      BufferedReader fin = new BufferedReader(new InputStreamReader(gzip_in));
      readData(fin);
      setFilename(infile);
      fin.close();
      gzip_in.close();
    } catch (IOException e) {
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
   * Each line represents an FMZ.
   * The line is comma delimited the fields are:
   * name, id, acres, natural fires, manmade fires, cost.
   * the last three fields are further subdived using colons.
   * Note: The first line of the input file is the number
   * of lines in the file.
   *
   * @param fin
   * @throws SimpplleError
   */
  public static void readData(BufferedReader fin) throws SimpplleError {

    try {

      String line = fin.readLine();
      if (line == null) {
        throw new ParseError("FMZ Data file is empty.");
      }

      // Read in the the list of Fmz names contained in this file.
      // We want to delete all the fmz's in the allFmz
      // hashtable that are not contained in this file.

      StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");
      int fmzCount = strTok.countTokens();
      String[] fmzNames = new String[fmzCount];
      for (int i = 0; i < fmzCount; i++) {
        fmzNames[i] = strTok.getToken().toLowerCase();
      }

      RegionalZone zone = Simpplle.getCurrentZone();
      zone.updateAllFmz(fmzNames);

      float[] naturalFires = new float[NUM_CLASSES];
      float[] manmadeFires = new float[NUM_CLASSES];
      float[] cost         = new float[NUM_CLASSES];

      for (int i = 0; i < fmzCount; i++) {

        line = fin.readLine();
        strTok = new StringTokenizerPlus(line,",");
        if (strTok.countTokens() < 5) {
          String msg = "Incorrect number of fields in the following line:\n" +
                       line + "\n" +
                       "There should be at least five fields.";
          throw new ParseError(msg);
        }

        String fmzName = strTok.getToken().toLowerCase();
        float theAcres = strTok.getFloatToken();

        // Get the Natural Fires.
        String field = strTok.getToken();
        StringTokenizerPlus fieldStrTok = new StringTokenizerPlus(field,":");
        if (fieldStrTok.countTokens() != NUM_CLASSES) {
          String msg = "Incorrect number of items in Natural Fires field of FMZ " + fmzName;
          throw new ParseError(msg);
        }
        for (int j = 0; j < NUM_CLASSES; j++) {
          naturalFires[j] = fieldStrTok.getFloatToken();
        }

        // Get the Man Made Fires.
        field = strTok.getToken();
        fieldStrTok = new StringTokenizerPlus(field,":");
        if (fieldStrTok.countTokens() != NUM_CLASSES) {
          String msg = "Incorrect number of items in Man Made " +
                "Fires Field of FMZ " + fmzName;
          throw new ParseError(msg);
        }
        for (int j = 0; j < NUM_CLASSES; j++) {
          manmadeFires[j] = fieldStrTok.getFloatToken();
        }

        // Get the Cost.
        field = strTok.getToken();
        fieldStrTok = new StringTokenizerPlus(field,":");
        if (fieldStrTok.countTokens() != NUM_CLASSES) {
          String msg = "Incorrect number of items in Cost " +
                "Field of FMZ " + fmzName;
          throw new ParseError(msg);
        }
        for(int j = 0; j < NUM_CLASSES; j++) {
          cost[j] = fieldStrTok.getFloatToken();
        }

        float time;
        if (strTok.hasMoreTokens()) {
          time = strTok.getFloatToken();
        } else {
          time = DEFAULT_RESPONSE_TIME;
        }

        Fmz fmz = zone.getFmz(fmzName);
        boolean newFmz;
        if (fmz == null) {
          fmz = new Fmz();
          newFmz = true;
        } else {
          newFmz = false;
        }
        fmz.updateFmz(theAcres,naturalFires,manmadeFires,cost,time);
        if (newFmz) {
          fmz.setName(fmzName);
          //fmz.setId();
          zone.addFmz(fmz);
        }
      }

      setChanged(false);

    } catch (NumberFormatException nfe) {

      String msg = "Invalid numeric value found in Fmz data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);

    } catch (ParseError pe) {

      System.out.println(pe.msg);
      throw new SimpplleError(pe.msg);

    } catch (IOException e) {

      String msg = "Problems read from FMZ data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);

    }
  }

  /**
   *
   * @param outfile
   */
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

  /**
   *
   * @param fout
   */
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

  /**
   *
   * @param fout
   */
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

  /**
   *
   */
  public static void closeFile() {
    clearFilename();
    setChanged(false);
  }

  /**
   * Returns the name of this fire management zone.
   */
  public String toString() {
    return name;
  }

}




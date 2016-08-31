/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;

import org.hibernate.*;
import simpplle.*;
import simpplle.comcode.Climate.*;

/**
 * This class defines an Object describing a Forest Landscape.  Hierarchy for landscapes are
 * Regional Zone -> Area -> Natural Element
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class Area implements Externalizable {
  static final long serialVersionUID = 338559481281909130L;
  static final int  version          = 7;
  static final int  simDataVersion   = 7;

  public static final int EVU = 0;
  public static final int EAU = 1;
  public static final int ELU = 2;

  // Manmade Elements
  public enum ManmadeUnitKinds { ROADS, TRAILS };
  public static final ManmadeUnitKinds ROADS  = ManmadeUnitKinds.ROADS;
  public static final ManmadeUnitKinds TRAILS = ManmadeUnitKinds.TRAILS;

  private static boolean disableMultipleLifeforms=false;

  /**
   *  This area has specific Adjacency Data, and Keane spreading logic can be used.
   */
  private boolean hasKeaneAttributes;

  private String                name;
  private String                date;
  private String                path;
  private int                   kind;
  private int                   acres;
  private int                   totalLength;
  private Evu[]                 allEvu;
  private ExistingAquaticUnit[] allEau;
  private ExistingLandUnit[]    allElu;
  private Roads[]               allRoads;
  private Trails[]              allTrails;
  private NaturalElement[][]    allUnits = new NaturalElement[3][];
  private ManmadeElement[][]    allManmadeUnits = new ManmadeElement[2][];
  private int                   fileVersion;
  private static TreatmentSchedule treatmentSchedule;
  private static ProcessSchedule   processSchedule;

  private int elevationRelativePosition = 100; // Added with file version 7

  private int polygonWidth;

  // Used for search Units
  private Vector allHtGrp;
  private Vector allSpecies;
  private Vector allSizeClass;
  private Vector allAge;
  private Vector allDensity;
  private Vector allOwnership;
  private Vector allSpecialArea;
  private Vector allFmz;

  public static Lifeform currentLifeform;

  public static Lifeform getCurrentLifeform(Evu evu) {
    return (currentLifeform != null ? currentLifeform : evu.getDominantLifeform());
  }
  /**
   * Temporary storage for adjacent data upon loading or creating an area.
   The key is Evu and the Value is Vector of int[]
  */

  private Hashtable<Evu, Vector> tmpAdjacentData;

  private boolean manualGC = false;

  //acres is stored as an int.  see note with get getPrecision in this class
  public static final int ACRES_PRECISION = 2;

  public static final int LENGTH_PRECISION = 2;


  private static int maxEvuId;


  // This way these potentially very large arrays,
  // only have to be allocated once.
//  private static boolean[] origin;
//  private static boolean[] spread;
//  private static int[]     weatherProb;

//  private static Hashtable extremeFires;
//  private static Hashtable fireSeason;


  //private static final char DELIM = ',';

  /**
   * Used to indicate the Area holds previously simulated data.
   */
  public static final int SIMULATED = 0;

  /**
   * Used to indicated the Area is a user defined Area.
   */
  public static final int USER      = 1;

  /**
   * Used to indicate that the Area is a sample Area.
   */
  public static final int SAMPLE    = 2;

  // ** Parsing Stuff **
  private static String KEYWORD[] = {"CLASS",
                                     "NAME",
                                     "ACRES",
                                     "KIND",
                                     "END-CLASS",
                                     "AREA",
                                     "EVU",
                                     "ALL-EVU",
                                     "AREA-SUMMARY",
                                     "END",
                                     "MAX-EVU-ID",
                                     "SIMULATION",
                                     "PRECISION",
                                     "VERSION",
                                     "EAU",
                                     "ALL-EAU",
                                     "LENGTH" };

  private static final int CLASS        = 0;
  private static final int NAME         = 1;
  private static final int ACRES        = 2;
  private static final int KIND         = 3;
  private static final int END_CLASS    = 4;
  private static final int AREA         = 5;
  private static final int EVU_TOKEN    = 6;
  private static final int ALL_EVU      = 7;
  private static final int AREA_SUMMARY = 8;
  private static final int END          = 9;
  private static final int MAX_EVU_ID   = 10;
  private static final int SIMULATION   = 11;
  private static final int PRECISION    = 12;
  private static final int VERSION      = 13;
  private static final int EAU_TOKEN    = 14;
  private static final int ALL_EAU      = 15;
  private static final int LENGTH       = 16;

  private static final int EOF          = 17;

  // ** End Parsing Stuff **

  private static final SimpplleType.Types SPECIES    = SimpplleType.SPECIES;
  private static final SimpplleType.Types SIZE_CLASS = SimpplleType.SIZE_CLASS;
  private static final SimpplleType.Types DENSITY    = SimpplleType.DENSITY;
  private static final SimpplleType.Types PROCESS    = SimpplleType.PROCESS;

  private static final int CURRENT_FILE_VERSION = 2;

  private static StringBuffer strBuf = new StringBuffer();

  public static final String ALL_PROB_STEP = "n";

  // An attempt to reduce temporary memory usage.
  private static final String SLINK_STR = "Slink";
  private static final String SLINK_STR_CAPS = "SLINK";
  private static final String COMMA_STR = ",";

  // Conversion from acres to sq feet
  private static final int ACRES_TO_FEET = 43560;

  /**
   * Area constructor.  Initializes acres, maxEVUId, and length to 0, sets current date to system current date
   */
  public Area () {
    maxEvuId         = 0;
    acres            = 0;
    this.totalLength = 0;
    date             = Simpplle.currentDate();
    fileVersion      = CURRENT_FILE_VERSION;
    manualGC         = false;
    tmpAdjacentData  = null;
    processSchedule = null;
    treatmentSchedule = null;
  }

  /**
   * Calls the default constructor and initializes some fields
   * with the provided parameters.
   * @param aName the Area's print name.
   * @param aPath is a pathname of the input data file.
   * @param kind is an int, (SIMULATED, USER, or SAMPLE)
   */
  public Area (String aName, String aPath, int kind) {
    this();
    name      = aName;
    path      = aPath;
    this.kind = kind;
  }

  /**
   * Calls the default constructor and initializes some fields
   * with the provided parameters. Used when loading or creating
   * an area, since the name its not yet know until the area
   * has already been loaded or created.
   * @param file is a pathname of the input data file.
   * @param kind is an int, (SIMULATED, USER, or SAMPLE)
   */
  public Area (String file, int kind) {
    this();
    path      = file;
    this.kind = kind;
  }
/**
 * Overloaded constructor for Area.  Calls the default constructor and sets the file path variable
 * by invoking the File.get file method and the kind for this area to parameter kind.
 * @param file
 * @param kind is an int, (SIMULATED, USER, or SAMPLE)
 */
  public Area (File file, int kind) {
    this();
    path      = file.getPath();
    this.kind = kind;
  }
/**
 *
 * @param kind is an int, (SIMULATED, USER, or SAMPLE)
 */
  public Area(int kind) {
    this();
    this.kind = kind;
  }

  public boolean doManualGC() { return manualGC; }
/**
 * Gets the file version
 * @return
 */
  public int getFileVersion() { return fileVersion; }
/**
 * Checks if multiple lifeforms are enabled, by negating the disable multiple lifeform boolean.
 * @return
 */
  public static boolean multipleLifeformsEnabled() { return !disableMultipleLifeforms; }

  /**
    * Converts the acres parameter to a floating point
    * by dividing it by (10^ACRES_PRECISION)
    * acres is stored in this manner to avoid the
    * inaccuracies of dealing with floating point numbers.
    * In addition acres is used in comparison in Opensimpplle
    * and to be accurate needs to be an integer.
    * Finally it is simply easier to do this instead of
    * changing the code to deal with floating point acres.
    * @param acresVal is an integer
    * @return a float
    */
  public static float getFloatAcres(int acresVal) {
    return ( (float)acresVal / (float)Utility.pow(10,getAcresPrecision()) );
  }
  /**
   * length like acres is stored as an int to avoid inaccuracies of floating point numbers.  to convert to a floating point
   * number it is multiplied by a power of 10.
   * @param lengthVal int representing length.
   * @return length as a floating point number
   */
  public static float getFloatLength(int lengthVal) {
    return ( (float)lengthVal / (float)Utility.pow(10,getLengthPrecision()) );
  }

  /**
   * Acres is really a float with n digits of precision.
   * It is stored as an int, to avoid the complications that floating point numbers can cause, especially
   * with respect to comparisons. Number of digits of precision is set in a the following variable.
   * Acres is displayed to the user as a floating point number by dividing acres by (10^n)
   *
   * @return the desired precision (n variable in 10^n - set at 2 as a final int)
   */
  public static int getAcresPrecision() { return ACRES_PRECISION; }

  public static int getRationalAcres(float acres) {
    return Math.round((float)Utility.pow(10,getAcresPrecision()) * acres);
  }
/**
 *
 * @return required precision which will be the 'n' variable in 10^n for determining the floating point representation of length
 * this is set at 2 as a final int
 */
  public static int getLengthPrecision() { return LENGTH_PRECISION; }

  public static int getRationalLength(float length) {
    return Math.round((float)Utility.pow(10,getLengthPrecision()) * length);
  }

  /**
   * Gets the print name of the Area.
   * @return a String.
   */
  public String getName () {
    return name;
  }
/**
 * Sets the name of this area to parameter string name.
 * @param newName the new name of this area.
 */
  public void setName(String newName) {
    name = newName;
  }
/**
 * Returns the name of this area.
 */
  public String toString () { return getName(); }

  /**
   * Gets the Area's input file pathname.
   * @return a File.
   */
  public String getPath () {
    return path;
  }
/**
 * Sets the file path of this area to the string new path parameter.
 * @param newPath
 */
  public void setPath(String newPath) {
    path = newPath;
  }
/**
 * sets the file pathway
 * @param newPath File to be designated as new pathway
 */
  public void setPath(File newPath) {
    path = newPath.getPath();
  }

  /**
   * Determines whether or not the Area is a user defined area.
   * @return a boolean, true = User defined Area.
   */
  public boolean isUser () {
    return (kind == USER);
  }

  /**
   * Gets the Area total acres.
   * @return total acres
   */
  public int getAcres() { return acres; }
  /**
   * Sets area's acres.
   * @param newAcres
   */
  private void setAcres(int newAcres) { acres = newAcres; }

  /**
   * Gets the Area's total Eau Length.
   * @return total area length
   */
  public int getLength() { return totalLength; }
  /**
   * Sets an Area's total Eau length to the parameter length
   * @param newLength the new length
   */
  private void setLength(int newLength) { totalLength = newLength; }
/**
 * Updates the ownership information for an area, totals and sets any increase in Evu acres and totals and sets any increase in Eau lengths.
 */
  public void updateArea() {
    Hashtable allOwnershipHt   = new  Hashtable();
    Hashtable allSpecialAreaHt = new  Hashtable();
    int       tmpAcres = 0;

    for(int i = 0;i <= maxEvuId; i++) {
      if (allEvu[i] != null) {
        tmpAcres += allEvu[i].getAcres();

        allOwnershipHt.put(allEvu[i].getOwnership(),Boolean.TRUE);
        allSpecialAreaHt.put(allEvu[i].getSpecialArea(),Boolean.TRUE);

      }
    }
    setAcres(tmpAcres);

    int tmpLength = 0;
    if (allEau != null) {
      for (int i=0; i<allEau.length; i++) {
        if (allEau[i] == null) { continue; }
        tmpLength += allEau[i].getLength();
      }
      setLength(tmpLength);
    }

    allOwnership   = Utility.vectorKeys(allOwnershipHt);
    allSpecialArea = Utility.vectorKeys(allSpecialAreaHt);

    allOwnershipHt   = null;
    allSpecialAreaHt = null;
  }

  /**
   * Gets an Evu in this Area by its Evu ID.
   * @param id the Evu's ID.
   * @return an Evu.
   */
  public Evu getEvu(int id) {
    if (id < 0 || id > maxEvuId) { return null; }
    return allEvu[id];
  }
  /**
   * gets an Eau from Area by its Eau ID, if one exists
   * @param id the Eau ID
   * @return the Eau
   */
  public ExistingAquaticUnit getEau(int id) {
    if (allEau == null || id < 0 || id > allEau.length-1) { return null; }
    return allEau[id];
  }
  /**
   * Makes a new Eau object with parameter Id, then adds to Eau.
   * @param id
   * @return
   */
  public ExistingAquaticUnit getNewEau(int id) {
    ExistingAquaticUnit unit = new ExistingAquaticUnit(id);
    allEau[id] = unit;
    return unit;
  }

  /**
   * Adds an existing vegetation unit to the area's natural elements.
   * @param unit
   */
  public void addEvu(Evu unit) {
    allUnits[EVU][unit.getId()] = unit;
  }

  /**
   * Adds an existing land unit to the area's natural elements.
   * @param unit
   */
  public void addElu(ExistingLandUnit unit) {
    allUnits[ELU][unit.getId()] = unit;
  }

  /**
   * Adds a roads unit to the area's manmade elements.
   * @param unit
   */
  public void addRoadUnit(Roads unit) {
    allManmadeUnits[ROADS.ordinal()][unit.getId()] = unit;
  }

  /**
   * Adds a trails unit to the area's manmade elements.
   * @param unit
   */
  public void addTrailUnit(Trails unit) {
    allManmadeUnits[TRAILS.ordinal()][unit.getId()] = unit;
  }

  // *** Manmade Unit Methods ***
  // ****************************

  /**
   * Returns a specific kind of manmade unit with a matching id.
   * @param id
   * @param kind The kind of manmade unit (ROAD,TRAIL)
   * @return
   */
  private ManmadeElement getManmadeUnit(int id, ManmadeUnitKinds kind) {
    if (allManmadeUnits[kind.ordinal()] == null || id < 0 || id > allManmadeUnits[kind.ordinal()].length-1) {
      return null;
    }
    return allManmadeUnits[kind.ordinal()][id];
  }

  /**
   * Returns a road unit with a matching id.
   * @param id Road ID
   * @return A road unit
   */
  public Roads getRoadUnit(int id) {
    return (Roads)getManmadeUnit(id,ROADS);
  }

  /**
   * Checks if this area has roads units.
   * @return True if the roads array is not null
   */
  public boolean hasRoads() {
    return allRoads != null;
  }

  /**
   * Returns all the roads units in this area.
   * @return An array of roads
   */
  public Roads[] getAllRoads() {
    return allRoads;
  }

  /**
   * Replaces all roads units in this area.
   * @param newAllRoads An array of roads
   */
  public void setAllRoads(Roads[] newAllRoads) {
    allManmadeUnits[ROADS.ordinal()] = newAllRoads;
    allRoads = (Roads[])allManmadeUnits[ROADS.ordinal()];
  }

  /**
   * Returns a trail unit with a matching id.
   * @param id Trail ID
   * @return A trail unit
   */
  public Trails getTrailUnit(int id) {
    return (Trails)getManmadeUnit(id,TRAILS);
  }

  /**
   * Checks if this area has trail units.
   * @return True if the trails array is not null
   */
  public boolean hasTrails() {
    return allTrails != null;
  }

  /**
   * Returns all the trails units in this area.
   * @return An array of trails
   */
  public Trails[] getAllTrails() {
    return allTrails;
  }

  /**
   * Replaces all trail units in this area.
   * @param newAllTrails An array of trails
   */
  public void setAllTrails(Trails[] newAllTrails) {
    allManmadeUnits[TRAILS.ordinal()] = newAllTrails;
    allTrails = (Trails[])allManmadeUnits[TRAILS.ordinal()];
  }

  // *** ExistingLandUnit Methods **
  // *******************************
/**
 * Gets a natural element unit in this area by indexing into the 2D natural element array:  [kind][unit id]
 * @param id unit id
 * @param kind Choices for natural elements are Evu = 0, Eau =1, Elu = 2.
 * @return
 */
  public NaturalElement getUnit(int id, int kind) {
    if (allUnits[kind] == null || id < 0 || id > allUnits[kind].length-1) { return null; }
    return allUnits[kind][id];
  }
  /**
   * Gets the first natural element unit in this area by indexing into the 2D natural element array:  [kind][unit id]
   * @param kind Choices for natural elements are Evu = 0, Eau =1, Elu = 2.
   * @return
   */
  private NaturalElement getFirstUnit(int kind) {
    if (allUnits[kind] == null) { return null;  }
    for(int i=0; i<allUnits[kind].length; i++) {
      if (allUnits[kind][i] != null) { return allUnits[kind][i]; }
    }
    return null;
  }
  /**
   * Gets the previous natural element unit in this area by indexing into the 2D natural element array:  [kind][unit id]
   * @param unit unit Id
   * @param kind Choices for natural elements are Evu = 0, Eau =1, Elu = 2
   * @return
   */
  private NaturalElement getPrevUnit(NaturalElement unit, int kind) {
    int id = unit.getId();

    int i;
    for (i=id-1; i>=0; i--) {
      if (allUnits[kind][i] != null) { return allUnits[kind][i]; }
    }
    for (i=allUnits[kind].length-1; i>id; i--) {
      if (allUnits[kind][i] != null) { return allUnits[kind][i]; }
    }
    return null;
  }
/**
 * Gets the next natural element unit in this area by indexing into the 2D natural element array:  [kind][unit id]
 * @param unit
 * @param kind Choices for natural elements are Evu = 0, Eau =1, Elu = 2
 * @return
 */
  private NaturalElement getNextUnit(NaturalElement unit, int kind) {
    int id = unit.getId(), i;
    for (i=id+1; i<allUnits[kind].length; i++) {
      if (allUnits[kind][i] != null) { return allUnits[kind][i]; }
    }
    for (i=0;i<id;i++) {
      if (allUnits[kind][i] != null) { return allUnits[kind][i]; }
    }
    return null;
  }
  /**
   * Gets the previous Invalid Natural Element unit in this area by indexing into the 2D natural element array:  [kind][unit id]
   * @param unit unit Id
   * @param kind Choices for natural elements are Evu = 0, Eau =1, Elu = 2
   * @return
   */
  private NaturalElement getPrevInvalidUnit(NaturalElement unit, int kind) {
    int id = unit.getId(), i;
    for (i=id-1;i>=0;i--) {
      if (allUnits[kind][i] != null && allUnits[kind][i].isValid() == false) {
        return allUnits[kind][i];
      }
    }
    for (i=allUnits[kind].length-1;i>id;i--) {
      if (allUnits[kind][i] != null && allUnits[kind][i].isValid() == false) {
        return allEvu[i];
      }
    }
    return null;
  }
  /**
   * Gets the next invalid Natural Element unit in this area by indexing into the 2D natural element array:  [kind][unit id]
   * @param unit
   * @param kind Choices for natural elements are Evu = 0, Eau =1, Elu = 2
   * @return
   */
  private NaturalElement getNextInvalidUnit(NaturalElement unit, int kind) {
    int id = unit.getId(), i;
    for (i=id+1;i<=allUnits[kind].length-1;i++) {
      if (allUnits[kind][i] != null && allUnits[kind][i].isValid() == false) {
        return allUnits[kind][i];
      }
    }
    for (i=0;i<id;i++) {
      if (allUnits[kind][i] != null && allUnits[kind][i].isValid() == false) {
        return allUnits[kind][i];
      }
    }
    return null;
  }

/**
 * Gets an Elu by an Integer object Id.
 * @param id Elu Id
 * @return the Elu
 */
  public ExistingLandUnit getElu(Integer id) {
    return (ExistingLandUnit) getUnit(id.intValue(), ELU);
  }
  /**
   * Gets an Elu by its Id.
   * @param id Elu Id
   * @return the Elu
   */
  public ExistingLandUnit getElu(int id) {
    return (ExistingLandUnit) getUnit(id, ELU);
  }
  /**
   * Makes a new Elu with parameter Id, adds it to all Natural units 2D array, then returns theElu
   * @param id new Elu Id
   * @return new Elu object
   */
  public ExistingLandUnit getNewElu(int id) {
    ExistingLandUnit unit = new ExistingLandUnit(id);
    allUnits[ELU][id] = unit;
    return unit;
  }
  /**
   * Gets the first Elu in this Area
   * @return the first Elu in this Area
   */
  public ExistingLandUnit getFirstElu() {
    return (ExistingLandUnit) getFirstUnit(ELU);
  }
  /**
   * Uses parameter Elu to get the previous one.
   * @param unit the Elu used to find the previous.
   * @return previous Elu
   */
  public ExistingLandUnit getPrevElu(ExistingLandUnit unit) {
    return (ExistingLandUnit) getPrevUnit(unit, ELU);
  }
  /**
   * Uses parameter Elu to get the next Elu.
   * @param unit the Elu used to find the next Elu.
   * @return next Elu
   */
  public ExistingLandUnit getNextElu(ExistingLandUnit unit) {
    return (ExistingLandUnit) getNextUnit(unit, ELU);
  }
  /**
   * Uses parameter Elu to get the previous invalid Elu.
   * @param unit the Elu used to find the previous invalid Elu.
   * @return previous Elu
   */
  public ExistingLandUnit getPrevInvalidUnit(ExistingLandUnit unit) {
    return (ExistingLandUnit) getPrevInvalidUnit(unit, ELU);
  }
  /**
   * Uses parameter Elu to get the next invalid Elu.
   * @param unit the Elu used to find the next invalid Elu.
   * @return next invalid Elu
   */
  public ExistingLandUnit getNextInvalidUnit(ExistingLandUnit unit) {
    return (ExistingLandUnit) getNextInvalidUnit(unit, ELU);
  }


  // *******************************************************************
/**
 * Gets an Evu by its Integer object Id.
 * @param id Evu Id
 * @return the Evu
 */
  public Evu getEvu(Integer id) {
    return getEvu(id.intValue());
  }
  /**
   * Gets an Eau by its Id.
   * @param id Eau Id
   * @return the Eau object
   */
  public ExistingAquaticUnit getEau(Integer id) {
    return getEau(id.intValue());
  }
  /**
   * Gets the first Evu in this Area
   * @return the first Evu in this Area
   */
  public Evu getFirstEvu() {
    for(int i=0;i<=maxEvuId;i++) {
      if (allEvu[i] != null) { return allEvu[i]; }
    }
    return null;
  }
  /**
   * Gets the first Eau in this Area
   * @return the first Eau in this Area
   */
  public ExistingAquaticUnit getFirstEau() {
    if (allEau == null) { return null;  }
    for(int i=0; i<allEau.length; i++) {
      if (allEau[i] != null) { return allEau[i]; }
    }
    return null;
  }
/**
 * Gets the previous Evu in the all Evu array based on the parameter Evu id.
 * @param evu the Evu that will be used to find the previous
 * @return the previous Evu
 */
  public Evu getPrevEvu(Evu evu) {
    int id = evu.getId(), i;
    for (i=id-1;i>=0;i--) {
      if (allEvu[i] != null) { return allEvu[i]; }
    }
    for (i=maxEvuId;i>id;i--) {
      if (allEvu[i] != null) { return allEvu[i]; }
    }
    return null;
  }
  /**
   * Gets the previous Eau based in the all Evu array based on the parameter Eau id.
   * @param eau the Evu that will be used to find the previous
   * @return the previous Eau
   */
  public ExistingAquaticUnit getPrevEau(ExistingAquaticUnit eau) {
    int id = eau.getId(), i;
    for (i=id-1;i>=0;i--) {
      if (allEau[i] != null) { return allEau[i]; }
    }
    for (i=allEau.length-1;i>id;i--) {
      if (allEau[i] != null) { return allEau[i]; }
    }
    return null;
  }
  /**
   * Uses parameter Evu to get the previous one.
   * @param evu the Evu used to find the previous.
   * @return previous Evu
   */
  public Evu getNextEvu(Evu evu) {
    int id = evu.getId(), i;
    for (i=id+1;i<=maxEvuId;i++) {
      if (allEvu[i] != null) { return allEvu[i]; }
    }
    for (i=0;i<id;i++) {
      if (allEvu[i] != null) { return allEvu[i]; }
    }
    return null;
  }
  /**
   * Uses parameter eau to get the previous one from array of Area's all Eau's.
   * @param eau the Evu used to find the previous.
   * @return previous Evu
   */
  public ExistingAquaticUnit getNextEau(ExistingAquaticUnit eau) {
    int id = eau.getId(), i;
    for (i=id+1; i<allEau.length; i++) {
      if (allEau[i] != null) { return allEau[i]; }
    }
    for (i=0;i<id;i++) {
      if (allEau[i] != null) { return allEau[i]; }
    }
    return null;
  }
  /**
   * Gets the previous invalid Evu in this Area's all Evu array based on the parameter Evu id.
   * @param evu the Evu that will be used to find the previous invalid Id.
   * @return the previous Evu
   */
  public Evu getPrevInvalidEvu(Evu evu) {
    int id = evu.getId(), i;
    for (i=id-1;i>=0;i--) {
      if (allEvu[i] != null && allEvu[i].isValid() == false) {
        return allEvu[i];
      }
    }
    for (i=maxEvuId;i>id;i--) {
      if (allEvu[i] != null && allEvu[i].isValid() == false) {
        return allEvu[i];
      }
    }
    return null;
  }
  /**
   * Gets the previous invalid Eau in this Area's all Eau array based on the parameter Eau id.
   * @param eau the Eau that will be used to find the previous invalid Eau Id.
   * @return the previous invalid Eau
   */
  public ExistingAquaticUnit getPrevInvalidEau(ExistingAquaticUnit eau) {
    int id = eau.getId(), i;
    for (i=id-1;i>=0;i--) {
      if (allEau[i] != null && allEau[i].isValid() == false) {
        return allEau[i];
      }
    }
    for (i=allEau.length-1; i>id; i--) {
      if (allEau[i] != null && allEau[i].isValid() == false) {
        return allEau[i];
      }
    }
    return null;
  }
  /**
   * Gets the next invalid Evu in this Area's all Evu array based on the parameter Evu id.
   * @param evu the Evu that will be used to find the next invalid Id.
   * @return the next invalid Evu
   */
  public Evu getNextInvalidEvu(Evu evu) {
    int id = evu.getId(), i;
    for (i=id+1;i<=maxEvuId;i++) {
      if (allEvu[i] != null && allEvu[i].isValid() == false) {
        return allEvu[i];
      }
    }
    for (i=0;i<id;i++) {
      if (allEvu[i] != null && allEvu[i].isValid() == false) {
        return allEvu[i];
      }
    }
    return null;
  }
  /**
   * Gets the next invalid Eau in this Area's all Eau array based on the parameter Eau id.
   * @param eau the Eau that will be used to find the next invalid Id.
   * @return the next invalid Eau
   */
  public ExistingAquaticUnit getNextInvalidEvu(ExistingAquaticUnit eau) {
    int id = eau.getId(), i;
    for (i=id+1; i<allEau.length; i++) {
      if (allEau[i] != null && allEau[i].isValid() == false) {
        return allEau[i];
      }
    }
    for (i=0; i<id; i++) {
      if (allEau[i] != null && allEau[i].isValid() == false) {
        return allEau[i];
      }
    }
    return null;
  }

/**
 * @return the array of all Evu's for this area.
 */
  public Evu[] getAllEvu() { return allEvu; }

/**
 * First sets the new Evu array into the all natural element 2D array at the Evu (0) index,
 * then sets the allEvu array for this array to the newAllEvu array.
 * @param newAllEvu the array of Evus to be set.
 */
  public void setAllEvu(Evu[] newAllEvu) {
    allUnits[EVU] = newAllEvu;
    allEvu = (Evu[])allUnits[EVU];
  }

/**
 * @return all the Eaus for this area.
 */
  public ExistingAquaticUnit[] getAllEau() { return allEau; }

  /**
   * Used when importing a new area that does not include isolated stream
   * segments in the aquatics-aquatics section of the spatial-relate file.
   * @param newEau ExistingAquaticUnit
   */
  public void setEau(ExistingAquaticUnit newEau) {
    // Make array larger if need be.
    if (newEau.getId() > allEau.length-1) {
      ExistingAquaticUnit[] units = new ExistingAquaticUnit[newEau.getId()+1];
      for (int i=0; i<allEau.length; i++) {
        units[i] = allEau[i];
      }
      for (int i=allEau.length; i<units.length; i++) {
        units[i] = null;
      }
      allEau = units;
      units = null;
    }
    allEau[newEau.getId()] = newEau;
  }

  /**
   * Sets both the array of all Eaus in Area and the natural element 2d array at index EAU
   * @param newAllEau array of al the new Eaus for an area.
   */
  public void setAllEau(ExistingAquaticUnit[] newAllEau) {
    allUnits[EAU] = newAllEau;
    allEau = (ExistingAquaticUnit[])allUnits[EAU];
  }

/**
 * @return all the natural elements (kinds are EVU, EAU, ELU) for this area.
 */
  private NaturalElement[] getAllUnits(int kind) {
    return allUnits[kind];
  }
  public ExistingLandUnit[] getAllElu() {
    return (ExistingLandUnit[])getAllUnits(ELU);
  }
  public void setAllElu(ExistingLandUnit[] newAllElu) {
    allUnits[ELU] = newAllElu;
    allElu = (ExistingLandUnit[])allUnits[ELU];
  }

  public void addAllElu(ExistingLandUnit[] newAllElu) {
    if (newAllElu == null) {
      allUnits[ELU] = null;
      allElu        = null;
      return;
    }

    for (int i=0; i<newAllElu.length; i++) {
      if (newAllElu[i] != null) {
        allUnits[ELU][newAllElu[i].getId()] = newAllElu[i];
      }
    }
  }

  public void addAllRoads(Roads[] newAllRoads) {
    if (newAllRoads == null) {
      allManmadeUnits[ROADS.ordinal()] = null;
      allRoads                         = null;
      return;
    }

    for (int i=0; i<newAllRoads.length; i++) {
      if (newAllRoads[i] != null) {
        allManmadeUnits[ROADS.ordinal()][newAllRoads[i].getId()] = newAllRoads[i];
      }
    }
  }

  public void addAllTrails(Trails[] newAllTrails) {
    if (newAllTrails == null) {
      allManmadeUnits[TRAILS.ordinal()] = null;
      allTrails                         = null;
      return;
    }

    for (int i=0; i<newAllTrails.length; i++) {
      if (newAllTrails[i] != null) {
        allManmadeUnits[TRAILS.ordinal()][newAllTrails[i].getId()] = newAllTrails[i];
      }
    }
  }

  public boolean existAquaticUnits() { return (allEau != null); }

  public boolean existLandUnits() { return allUnits[ELU] != null; }

  public int getMaxEvuId() { return maxEvuId; }

  public void setMaxEvuId(int newMaxId) { maxEvuId = newMaxId; }

    public void setDisableMultipleLifeforms(boolean disableMultipleLifeforms) {
      Area.disableMultipleLifeforms = disableMultipleLifeforms;
    }
/**
 * Gets the highest Eau Id in this area, by counting the length of the allEau array and subracting 1.
 * @return highest Eau ID in this area.
 */
    public int getMaxEauId() {
    if (allEau == null) { return -1;  }
    else {
      return allEau.length-1;
    }
  }

/**
 * Gets the highest Elu Id, by finding the length of the 2D array at index [ELU][] - 1
 * @return the highest Existing Land Unit Id
 */
  public int getMaxEluId() {
    if (allUnits[ELU] == null) { return -1; }
    return allUnits[ELU].length - 1;
  }

/**
 * Checks if an Evu Id is a valid Id based on Integer object.
 * @param id the Evu Id being checked
 * @return true if valid
 */
  public boolean isValidUnitId(Integer id) { return (getEvu(id) != null); }

  public boolean isValidUnitId(int id) { return (getEvu(id) != null); }

  public boolean isValidAquaticUnitId(int id) { return (getEau(id) != null); }

  public boolean isValidLandUnitId(int id) { return (getElu(id) != null); }

  public boolean existAnyInvalidVegUnits() {
    return existAnyInvalidUnits(EVU);
  }

  /**
   * Checks if there are any invalid Eau's by cycling through the Natural Element 2D array at [EAU][]
   * @return true if there is any invalid Eau's
   */
  public boolean existAnyInvalidAquaticUnits() {
    return existAnyInvalidUnits(EAU);
  }

  /**
   * Checks if there are any invalid Eau's by cycling through the Natural Element 2D array at [ELU][]
   * @return true if there is any invalid ELu, Id's
   */
  public boolean existAnyInvalidLandUnits() {
    return existAnyInvalidUnits(ELU);
  }

  /**
   * Method to check if there are any invalid EVU, EAU, or ELU Id's in the Natural Element 2D array based on the int kind
   *
   * @param kind EVU = 0, EAU = 1, ELU = 2
   * @return true if there exists any invalid EVU, EAU, or ELU Id's based on parameter kind.
   */
  private boolean existAnyInvalidUnits(int kind) {
    if (allUnits[kind] == null) { return false; }
    for(int i=0; i<allUnits[kind].length; i++) {
      if (allUnits[kind][i] == null) { continue; }
      if (!allUnits[kind][i].isValid()) {
        return true;
      }
    }
    return false;
  }

/**
 * finds the Evu ID from a vector of units to be removed and their index in allEvu to null, and removes the now invalid adjacents.
 * @param units a vector of units to be removed
 */
  public void removeUnits(Vector units) {
    int i;
    for (i=0; i<units.size(); i++) {
      allEvu[((Integer)units.elementAt(i)).intValue()] = null;
    }
    for (i=0; i<allEvu.length; i++) {
      if (allEvu[i] != null) {
        allEvu[i].removeInvalidAdjacents();
      }
    }

  }

  /**
   * Updates all contained EVUs so they reference the
   * Traverse the units and update the fmz,
   * changing invalid ones to the default fmz.
   * Also remap valid ones to the newly loaded version.
   * If any are changed return true.
   * @return a boolean
   */
  public boolean updateFmzData() {
    boolean changed = false;
    for (int i = 0; i <= maxEvuId; i++) {
      Evu evu = allEvu[i];
      if (evu != null) {
        if (evu.updateFmz()) {
          changed = true;
        }
      }
    }
    return changed;
  }

  /**
   * Updates VegetativeType and Habitat Type Group data, if user loads their own pathway group
   * in the units.
   */
  public void updatePathwayData() {
    Evu     evu;
    boolean changed = false, result;

    for (int i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu == null) {continue; }
      result = evu.updatePathwayData();
      if (result) { changed = true; }
    }
  }

  /**
   * Resets the state in all EVUs so they are ready for simulation.
   */
  public void makeSimulationReady() {
    for (int i = 0; i <= maxEvuId; i++) {
      if (allEvu[i] != null) {
        allEvu[i].makeSimulationReady();
      }
    }
  }

  // ** Parsing Stuff **

  private boolean keyMatch (String key, int keyid) {
    return key.equalsIgnoreCase(KEYWORD[keyid]);
  }

  /**
   * method to get the ordinal within the enumeration based on string key parsed using string tokenizer and tries to match value with key ID
   * @param strTok
   * @return the int whithin
   * @throws ParseError
   * @throws IOException
   */
  private int getKeyword (StringTokenizerPlus strTok) throws ParseError, IOException {
    String value;

    value = strTok.nextToken();

    if      (keyMatch(value,CLASS))        { return CLASS; }
    else if (keyMatch(value,NAME))         { return NAME;}
    else if (keyMatch(value,ACRES))        { return ACRES;}
    else if (keyMatch(value,LENGTH))       { return LENGTH;}
    else if (keyMatch(value,KIND))         { return KIND;}
    else if (keyMatch(value,END_CLASS))    { return END_CLASS;}
    else if (keyMatch(value,END))          { return END;}
    else if (keyMatch(value,AREA_SUMMARY)) { return AREA_SUMMARY; }
    else if (keyMatch(value,SIMULATION))   { return SIMULATION; }
    else if (keyMatch(value,PRECISION))    { return PRECISION; }
    else if (keyMatch(value,VERSION))      { return VERSION; }
    else {
      throw new ParseError("Unknown Keyword: " + value);
    }
  }

/**
 * Reads in information constructing an area.
 * @param fin
 * @throws ParseError
 * @throws IOException
 */
  private void readArea (BufferedReader fin) throws ParseError, IOException {
    int                 key = EOF;
    String              value, line, msg;
    StringTokenizerPlus strTok;

    fileVersion = 1; // 1 is the files that don't have version specified.

    // loop until we find the "END", "CLASS", or EOF.
    do {
      line   = fin.readLine();
      if (line == null) { key = EOF; continue;}

      strTok = new StringTokenizerPlus(line," ");
      if (strTok.hasMoreTokens() == false) {continue;}

      key = getKeyword(strTok);
      if (key != END && strTok.hasMoreTokens() == false) {
        throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
      }

      switch (key) {
        case NAME:
          // There seems to be a bug in JRE 1.3 that doesn't not
          // correctly use " as a delimeter, so do this instead.
          name = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
          break;
        case KIND:
          value = strTok.nextToken();
          if (value.equalsIgnoreCase("SIMULATED"))   { kind = SIMULATED;}
          else if (value.equalsIgnoreCase("USER"))   { kind = USER;}
          else if (value.equalsIgnoreCase("SAMPLE")) { kind = SAMPLE;}
          else {
            throw new ParseError ("Invalid value for KIND: " + value);
          }
          break;
        case ACRES:
          try {
            acres = Integer.parseInt(strTok.nextToken());
          }
          catch (NumberFormatException NFE) {
            throw new ParseError("Invalid value for Acres, must be a number.");
          }
          break;
        case LENGTH:
          try {
            totalLength= Integer.parseInt(strTok.nextToken());
          }
          catch (NumberFormatException NFE) {
            throw new ParseError("Invalid value for Length, must be a number.");
          }
          break;
        // This code placed here in case it is needed in the future.
        case PRECISION:
          try {
            int dummy = Integer.parseInt(strTok.nextToken());
            if (dummy <= 0) {
              throw new NumberFormatException();
            }
          }
          catch (NumberFormatException NFE) {
            msg = "Invalid value for Precision, must be a positive number";
            throw new ParseError(msg);
          }
          break;
        case VERSION:
          fileVersion = strTok.getIntToken();
          if (fileVersion == -1) {
            fileVersion = 1;
            msg = "Invalid file version.";
            throw new ParseError(msg);
          }
      }
    }
    while (key != EOF && key != CLASS && key != END);

    if (key == CLASS) {
      throw new ParseError("No END keyword found while reading AREA data.");
    }
    else if (key == EOF) {
      throw new ParseError("Invalid Area file, no EVU records found.");
    }
  }

  private void readDelimitedEvu(BufferedReader fin) throws ParseError {
    Evu     evuData;
    String  line;
    boolean theEnd = false;
    int     id = 0;
    int     totalAcres=0;

    Hashtable allOwnershipHt   = new  Hashtable();
    Hashtable allSpecialAreaHt = new  Hashtable();

    try {
      line = fin.readLine();
      maxEvuId = Integer.parseInt(line.trim());

      if (maxEvuId > 10000) { manualGC = true; }

      setAllEvu(new Evu[maxEvuId+1]);

      int c;
      for(int i=0;i<=maxEvuId;i++) {
        strBuf.delete(0,strBuf.length());
        if (i == 0) {
          line = fin.readLine();
          strBuf.ensureCapacity(line.length() + 500);
          strBuf.append(line);
        }
        else {
          c = fin.read();
          while (c != '\n' && c != '\r' && c != -1) {
            strBuf.append((char)c);
            c = fin.read();
          }
          long numSkip = 1;
          if (c == '\r') { numSkip = fin.skip(1); }
          if (numSkip != 1) {
            throw new ParseError("Problems reading EVU records.");
          }
        }

        evuData = new Evu();
        theEnd = (new LegacyEvu(evuData).readDelimitedData(strBuf));
        if (theEnd) {break;}
        id = evuData.getId();
        allEvu[id] = evuData;
        totalAcres += evuData.getAcres();

        allOwnershipHt.put(evuData.getOwnership(),Boolean.TRUE);
        allSpecialAreaHt.put(evuData.getSpecialArea(),Boolean.TRUE);

        // Call GC every 20000 units read if running on Mac OS X.
        if (/*System.getProperty("os.name").equalsIgnoreCase("Mac OS X") &&*/
            manualGC && ((i % 10000) == 0))
        {
          System.gc();
        }
      }
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid value for Maximum EVU ID");
    }
    catch (IOException IOX) {
      throw new ParseError("Problems reading EVU records.");
    }

    // Make sure that the Area acres figure is correct.
    if (getAcres() != totalAcres) {
      setAcres(totalAcres);
    }
    finishAddingAdjacentData();

    allOwnership   = Utility.vectorKeys(allOwnershipHt);
    allSpecialArea = Utility.vectorKeys(allSpecialAreaHt);

    allOwnershipHt   = null;
    allSpecialAreaHt = null;
  }

  /**
   * Reads from a CDF (comma delineated file) the information to create an Eau
   * @throws ParseError
   */
  private void readDelimitedEau(BufferedReader fin) throws ParseError {
    ExistingAquaticUnit unitData;
    String              line;
    boolean             theEnd = false;
    int                 id = 0;
    int                 totLength = 0;

    try {
      line = fin.readLine();
      int maxEauId = Integer.parseInt(line.trim());

      if (maxEauId > 10000) { manualGC = true; }

      allEau = new ExistingAquaticUnit[maxEauId+1];

      int c;
      for(int i=0; i<allEau.length; i++) {
        strBuf.delete(0,strBuf.length());
        if (i == 0) {
          line = fin.readLine();
          strBuf.ensureCapacity(line.length() + 500);
          strBuf.append(line);
        }
        else {
          c = fin.read();
          while (c != '\n' && c != '\r' && c != -1) {
            strBuf.append((char)c);
            c = fin.read();
          }
          long numSkip = 1;
          if (c == '\r') { numSkip = fin.skip(1); }
          if (numSkip != 1) {
            throw new ParseError("Problems reading Aquatic Unit records.");
          }
        }

        unitData = ExistingAquaticUnit.readDelimitedData(strBuf);
        if (unitData == null) {break;}
        id = unitData.getId();
        allEau[id] = unitData;
        totLength += unitData.getLength();

        // Call GC every 20000 units read if running on Mac OS X.
        if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X") &&
            manualGC && ((i % 20000) == 0))
        {
          System.gc();
        }
      }
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid value for Maximum Aquatic Unit ID");
    }
    catch (IOException IOX) {
      throw new ParseError("Problems reading Aquatic Unit records.");
    }

    // Make sure that the Area acres figure is correct.
    if (getLength() != totLength) {
      setLength(totLength);
    }
    /** @todo call function to remove invalid adjacents and invalid units */
//    finishAddingAdjacentData();
  }

  /**
   * @return an ArrayList of all the ownerships in this Area.
   */
  public ArrayList getAllOwnership() { return new ArrayList(allOwnership); }

  /**
   * @return an ArrayList of all the ownerships in this Area.
   */
  public ArrayList getAllSpecialArea() { return new ArrayList(allSpecialArea); }

  /**
   * Reads in Area Summary data.
   * @throws ParseError
   */
  private void readAreaSummary (BufferedReader fin) throws ParseError {
    try {
      Simpplle.getAreaSummary().readSimulation(fin);
    }
    catch (IOException e) {
      System.out.println("Error while trying to read Area Summary data.");
    }
  }

  /**
   * The purpose of this function is to search through the Area file
   * and find the place where the data for this class is stored.
   * @param fin is the file input stream
   * @return a stream located at the start of AreaSummary data.
   */
  private BufferedReader findSimulation(BufferedReader fin)
    throws ParseError
  {
    char[]         buf = new char[1];
    int            nLines;

    try {
      // First find out how many lines make up the Evu's.
      String line = fin.readLine();
      if (line == null) {
        throw new ParseError("problems reading simulation file");
      }
      while (line != null && !line.equals("CLASS ALL-EVU")) {
        line = fin.readLine();
      }
      line = fin.readLine();
      if (line == null) {
        throw new ParseError("problems reading simulation file");
      }
      nLines = Integer.parseInt(line.trim());

      // Skip past all of the Evu's to the Area-Summary section.
      int numRead = fin.read(buf);
      for (int i=0; i<nLines; i++) {
        while (numRead != -1 && buf[0] != -1 && buf[0] != '\n') {
          numRead = fin.read(buf);
        }
        if (numRead == -1 || buf[0] == -1) {
          throw new ParseError("problems reading simulation file");
        }
      }

      // Now read lines until we find the area-summary section.
      line = fin.readLine();
      if (line == null) {
        throw new ParseError("problems reading simulation file");
      }
      while (line != null && !line.equals("CLASS SIMULATION")) {
        line = fin.readLine();
      }
      if (line != null && line.equals("CLASS SIMULATION")) {
        return fin;
      }
      else {
        throw new ParseError("problems reading simulation file");
      }
    }
    catch (Exception err) {
      throw new ParseError(err.getMessage());
    }
  }

  public BufferedReader findAreaSummary(BufferedReader fin) throws ParseError {
    char[]         buf = new char[1];
    int            nLines;

    try {
      String line = fin.readLine();
      if (line == null) {
        throw new ParseError("Unable to find Simulation data in file");
      }
      while (line != null && !line.equals("CLASS AREA-SUMMARY")) {
        line = fin.readLine();
      }
      if (line != null) {
        return fin;
      }
      else {
        throw new ParseError("Unable to find Simulation data in file");
      }
    }
    catch (Exception err) {
      throw new ParseError(err.getMessage());
    }
  }

  public void readAreaSummary(File areaFile, Simulation simulation)
    throws ParseError, IOException
  {
    BufferedReader fin;

    fin = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(areaFile))));

    findSimulation(fin);
    simulation.readSimulation(fin);

    findAreaSummary(fin);
    simulation.getAreaSummary().readSimulation(fin,simulation);
  }

  private void readSimulation(BufferedReader fin) throws ParseError {
    try {
      Simpplle.getCurrentSimulation().readSimulation(fin);
    }
    catch (IOException e) {
      System.out.println("Error while tring to read Simulation data.");
    }
  }

  /**
   * Loads the input data file for the Area using the pathname
   * provided in the class constructor.
   */
  public void loadArea () throws SimpplleError {
    try {
      BufferedReader fin = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path))));

      loadArea(fin);
      fin.close();
    }
    catch (IOException IOX) {
      throw new SimpplleError("Problems opening input file.");
    }
  }

  public void loadArea(BufferedReader fin) throws SimpplleError {
    int                 key = EOF, i;
    String              value, line;
    StringTokenizerPlus strTok;

    try {
      do {
        line   = fin.readLine();
        if (line == null) { key = EOF; continue;}

        strTok = new StringTokenizerPlus(line," ");
        if (!strTok.hasMoreTokens()) {continue;}

        key = getKeyword(strTok);
        if (!strTok.hasMoreTokens()) {
          throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
        }

        if (key == CLASS) {
          value = strTok.nextToken();
        }
        else {
          throw new ParseError("Invalid record, first keyword must be CLASS");
        }

        if (keyMatch(value,AREA)) {
          readArea(fin);
        }
        else if (keyMatch(value,ALL_EVU)) {
          readDelimitedEvu(fin);
          if (doManualGC()) { System.gc(); }
        }
        else if (keyMatch(value,ALL_EAU)) {
          readDelimitedEau(fin);
          if (doManualGC()) { System.gc(); }
        }
        else if (keyMatch(value,AREA_SUMMARY)) {
          readAreaSummary(fin);
        }
        else if (keyMatch(value,SIMULATION)) {
          if (Simpplle.getCurrentSimulation() == null) {
            JSimpplle.getComcode().makeNewSimulation();
          }
          readSimulation(fin);
        }
        else {
          throw new ParseError ("Invalid Class Specified:" + value);
        }
      }
      while (key != EOF);
      buildSpatialRelations();


    }
    catch (ParseError PX) {
      throw new SimpplleError(PX.msg);
    }
    catch (IOException IOE) {
      throw new SimpplleError("Problems while trying to read input file.");
    }

    // File version can now go to the current version
    // since we have now loaded the area.
    fileVersion = CURRENT_FILE_VERSION;
    System.gc();
  }

  private void buildSpatialRelations() {
    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] != null) { allEvu[i].clearAssociatedAquaticUnits(); }
    }

    for (int i=0; i<allEau.length; i++) {
      if (allEau[i] == null) { continue; }

      ArrayList adjEvus = allEau[i].getAdjacentEvus();
      ArrayList upEvus  = allEau[i].getUplandEvus();

      if (adjEvus != null) {
        for (int j = 0; j < adjEvus.size(); j++) {
          Evu evu = (Evu) adjEvus.get(j);
          evu.addAssociatedAquaticUnit(allEau[i]);
        }
      }
      if (upEvus != null) {
        for (int j = 0; j < upEvus.size(); j++) {
          Evu evu = (Evu) upEvus.get(j);
          evu.addAssociatedAquaticUnit(allEau[i]);
        }
      }
    }
  }


//  private void saveAreaData (PrintWriter fout) throws IOException {
//    Simulation  simulation  = Simpplle.getCurrentSimulation();
//    AreaSummary areaSummary = Simpplle.getAreaSummary();
//    int         tmpKind     = kind;
//
//    fout.println("CLASS AREA");
//    fout.println(" NAME " + "\"" + name + "\"");
//    fout.println(" ACRES " + acres);
//    fout.println(" LENGTH " + totalLength);
//    // Currently not used but placed in file in case we
//    // decide to make precision a variable in the future.
//    fout.println(" PRECISION " + ACRES_PRECISION);
//    fout.print(" KIND ");
//
//    // SAMPLE areas are created by the developers only, manually
//    // by editing the output file and changing USER to SAMPLE and
//    // adding the result to the list of Sample areas within the code.
//    tmpKind = (simulation != null && areaSummary != null) ? SIMULATED : USER;
//
//    switch (tmpKind) {
//      case SAMPLE:
//        fout.println("SAMPLE");
//        break;
//      case SIMULATED:
//        fout.println("SIMULATED");
//        break;
//      case USER:
//      default:
//        fout.println("USER");
//    }
//    fout.println("VERSION " + fileVersion);
//    fout.println("END");
//    fout.println();
//  }

  public void printIndividualSummary(File outputFile) throws SimpplleError {
    PrintWriter      fout;
    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      printIndividualSummary(fout);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      String msg = "Problems writing output file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  private void printIndividualSummary(PrintWriter fout) throws SimpplleError {
    Evu evu;

    for(int i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu != null) {
        evu.printHistory(fout);
      }
    }
  }

  /**
   * Prints the Area and all Evu's in a human readable format
   * to a file.
   * @param outputFile is a File.
   */
  public void printAll (File outputFile) throws SimpplleError {
    PrintWriter      fout;
    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      printAll(fout);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }

  private void printAll(PrintWriter fout) throws SimpplleError {
    fout.println("CLASS TYPE = AREA");
    fout.println("NAME       = " + name);
    fout.println("PATH       = " + path);

    fout.print("KIND       = ");
    switch (kind) {
      case SAMPLE:    fout.println("SAMPLE");    break;
      case USER:      fout.println("USER");      break;
      case SIMULATED: fout.println("SIMULATED"); break;
      default:        fout.println("");
    }
    fout.println("ACRES      = " + acres);
    fout.println("-----------------------------------------");
    fout.println();

    printIndividualSummary(fout);
  }

  // ---------------
  // ** Schedules **
  // ---------------

  public static void removeProcessSchedule() {
    processSchedule = null;
  }

  public static ProcessSchedule createProcessSchedule() {
    processSchedule = new ProcessSchedule();
    return getProcessSchedule();
  }

  public static ProcessSchedule getProcessSchedule() { return processSchedule; }

  public static void readProcessSchedule(File inputFile) throws SimpplleError {
    createProcessSchedule();
    processSchedule.read(inputFile);
  }

  /**
   * Creates a new treatment schedule for this area.
   * @return the treatment schedule
   */
  public static TreatmentSchedule createTreatmentSchedule() {
    treatmentSchedule = new TreatmentSchedule();
    return getTreatmentSchedule();
  }

  /**
   * Makes the treatment schedule null.
   */
  public static void removeTreatmentSchedule() {
    treatmentSchedule = null;
  }

  public static TreatmentSchedule getTreatmentSchedule() {
    return treatmentSchedule;
  }

  public int getPolygonWidth() {
    return polygonWidth;
  }

  /**
   * Remove the follow up applications that were added during the simulation.
   */
  public static void resetTreatmentSchedule() {
    if (treatmentSchedule != null) {
      treatmentSchedule.removeFollowUpApplications();
    }
  }

  public static void readTreatmentSchedule(File input_file) throws SimpplleError {
    createTreatmentSchedule();
    treatmentSchedule.read(input_file);
  }

  // ------------------------
  // ** Simulation Methods **
  // ------------------------

  public boolean extremeFireEvent(Evu evu) {
    int cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    ProcessOccurrenceSpreadingFire event;

    event = (ProcessOccurrenceSpreadingFire)Simpplle.getAreaSummary().getProcessEventSpreadingFire(evu.getOriginUnitFire(),cStep);
    return ((event != null) && event.isExtremeEvent());
  }
//  private void addExtremeFireEvent(Evu evu, Boolean isExtreme) {
//    extremeFires.put(evu,isExtreme);
//  }

//  public int getFireSeason(Evu evu) {
//    Integer value = (Integer)fireSeason.get(evu);
//    return (value == null) ? -1 : value.intValue();
//  }
//  public void addFireSeason(Evu evu, int season) {
//    fireSeason.put(evu,new Integer(season));
//  }

//  private void initOriginSpread(int maxId) {
//    if (origin == null || (maxId+1) != origin.length) {
//      origin = new boolean[maxId+1];
//    }
//    if (spread == null || (maxId+1) != spread.length) {
//      spread = new boolean[maxId+1];
//    }
//    if (weatherProb == null || (maxId+1) != weatherProb.length) {
//      weatherProb = new int[maxId+1];
//    }
//  }

  /**
   * Clears simulation data in existing vegetation units, roads, and trails.
   */
  public void restoreInitialConditions() {

    for(int i = 0; i <= maxEvuId; i++) {
      if (allEvu[i] != null) allEvu[i].restoreInitialConditions();
    }

    if (allRoads != null) {
      for (int i = 0; i < allRoads.length; i++) {
        if (allRoads[i] != null) allRoads[i].initSimulation();
      }
    }

    if (allTrails != null) {
      for (int i = 0; i < allTrails.length; i++) {
        if (allTrails[i] != null) allTrails[i].initSimulation();
      }
    }
  }

  /**
   * Initializes the cumulative probability for each process type, clears recorded water units, and clears state
   * stored in EVUs, EAUs, roads, and trails.
   */
  public void initSimulation() {

    // Treatment and Process Schedule stay until removed by user.

//    initOriginSpread(maxEvuId);

    Evu.initCumulProb();
    Evu.staticInitSimulation();

    for(int i=0;i<allEvu.length;i++) {
      if (allEvu[i] != null) allEvu[i].initSimulation();
    }

    if (allEau != null) {
      for (int i = 0; i < allEau.length; i++) {
        if (allEau[i] != null) allEau[i].initSimulation();
      }
    }

    if (allRoads != null) {
      for (int i = 0; i < allRoads.length; i++) {
        if (allRoads[i] != null) allRoads[i].initSimulation();
      }
    }

    if (allTrails != null) {
      for (int i = 0; i < allTrails.length; i++) {
        if (allTrails[i] != null) allTrails[i].initSimulation();
      }
    }
  }

  /**
   * Initialize some stuff prior to starting a multiple run
   * simulation.
   */
  public void initMultipleSimulation() {
    Evu evu;

    for(int i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu != null) {
        evu.initMultipleSimulation();
      }
    }
  }

  /**
   * Updates the multiple run summary data for each evu.
   */
  public void updateSummaries(MultipleRunSummary multipleRunSummary) {
    Evu        evu;
    Simulation simulation = Simpplle.getCurrentSimulation();
    boolean    trackSpecialArea, trackOwnership;

    trackSpecialArea = simulation.trackSpecialArea();
    trackOwnership   = simulation.trackOwnership();

    for(int i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu != null) {
//        multipleRunSummary.updateSummaries(evu);
        if (trackSpecialArea) {
          multipleRunSummary.updateSpecialAreaSummaries(evu);
        }
        if (trackOwnership) {
          multipleRunSummary.updateOwnershipSummaries(evu);
        }
      }
    }
  }

  /**
   * Goes through all of the evu's and calls a function in the
   * MultipleRunSummary class which stores a list all possible
   * special area's and ownership.
   * @see simpplle.comcode.MultipleRunSummary
   * @param simulation is the current Simulation instance.
   * @param multipleRunSummary is the current MultipleRunSummary instance.
   */
  public void initializeSpecialLists(Simulation simulation,
                                     MultipleRunSummary multipleRunSummary) {
    Evu     evu;
    boolean trackSpecialArea, trackOwnership;

    trackSpecialArea = simulation.trackSpecialArea();
    trackOwnership   = simulation.trackOwnership();

    if ((!trackSpecialArea) && (!trackOwnership)) {
      return;
    }

    for(int i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu != null) {
        if (trackSpecialArea) {
          multipleRunSummary.updateAllSpecialArea(evu.getSpecialArea());
        }
        if (trackOwnership) {
          multipleRunSummary.updateAllOwnership(evu.getOwnership());
        }
      }
    }
  }

/**
 * Goes through all the Evu's in an area and finds the nearest road and trail.  This comes from an array of length 2 for each Evu.
 * [0] = nearest road, [1]= nearest trail
 */
  public void findNearestRoadsTrails() {
    for (int i = 0; i < allEvu.length; i++) {
      if (allEvu[i] == null) {
        continue;
      }

      if (allRoads != null) {
        allEvu[i].findNearestRoad();
      }
      if (allTrails != null) {
        allEvu[i].findNearestTrail();
      }
    }
  }

  public void swapRowColumn() {
    for(int i=0;i<allEvu.length;i++) {
      if (allEvu[i] != null) {
        allEvu[i].swayXandY();
      }
    }
  }

  /**
   * Project the Area into the future one time step.
   */
  public void doFuture() throws SimpplleError {
    currentLifeform = null;

    FireSuppEventLogic.getInstance().clearSuppressed();

    for(int i=0;i<allEvu.length;i++) {
      if (allEvu[i] != null) {
        allEvu[i].setBeginTimeStepState();
      }
    }

    for(int i=0;i<allEvu.length;i++) {
      if (allEvu[i] != null) {
        allEvu[i].reset();
        allEvu[i].doHazard();
      }
    }

    if (hasRoads()) {
      for (int i = 0; i < allRoads.length; i++) {
        if (allRoads[i] != null) {
          allRoads[i].doBeginTimeStep();
        }
      }
    }

    if (hasTrails()) {
      for (int i = 0; i < allTrails.length; i++) {
        if (allTrails[i] != null) {
          allTrails[i].doBeginTimeStep();
        }
      }
    }

    boolean isWyoming = RegionalZone.isWyoming();
    if (!isWyoming ||
        (isWyoming && Simulation.getInstance().getCurrentSeason() == Season.SPRING)) {
      doTreatments();
    }
    doLockInProcesses();

    Simulation  simulation = Simpplle.getCurrentSimulation();
    AreaSummary areaSummary = Simpplle.getAreaSummary();

    // Proceses and Probabilities
    RegionalZone zone = Simpplle.getCurrentZone();

    if (isWyoming) {
      Evu.findWaterUnits();
    }


    int cStep = simulation.getCurrentTimeStep();
      for (int i = 0; i < allEvu.length; i++) {
        if (allEvu[i] == null) { continue; }

        if (allRoads != null && simulation.needNearestRoadTrailInfo()) {
          allEvu[i].updateNearestRoad();
        }
        if (allTrails != null && simulation.needNearestRoadTrailInfo()) {
          allEvu[i].updateNearestTrail();
        }

        if (isWyoming) {
          allEvu[i].determineFireSeason();
        }
        if (simulation.isStandDevelopment() == false) {
          allEvu[i].doProbability();
        }
        allEvu[i].doGetProcess();
        if (Area.multipleLifeformsEnabled() == false) {
          VegSimStateData state = allEvu[i].getState(cStep);
          ProcessProbability processData =
            new ProcessProbability(state.getProcess(), state.getProb());
          areaSummary.updateProcessOriginatedIn(allEvu[i], Lifeform.NA,
                                                processData, cStep);
        }
      }

    if (simulation.isStandDevelopment() == false) {
      doEvuSpread();
    }

//    doEvuProducingSeed();
    doEvuNextState();

    if (RegionalZone.isWyoming()) {
      for (Evu evu : allEvu) {
        if (evu != null) {
          evu.clearDummyProcesses();
        }
      }
    }
    // Need to restore the pre-treatment state to the units.
//    for(int i=0; i<allEvu.length; i++) {
//      if (allEvu[i] != null) { allEvu[i].restorePreTreatmentState(); }
//    }


    if (simulation.isDoInvasiveSpecies()) {
      Simulation.InvasiveKind invasiveKind = simulation.getInvasiveSpeciesKind();
      switch (invasiveKind) {
        case MESA_VERDE_NP:
          if (InvasiveSpeciesLogic.hasData()) {
            InvasiveSpeciesLogic.getInstance().doInvasive();
          }
          break;
        case R1:
          break;
        case MSU:
          if (InvasiveSpeciesLogicMSU.hasData()) {
            InvasiveSpeciesLogicMSU.getInstance().doInvasive();
          }
          break;
      }
    }
    else if (simulation.isDoInvasiveSpecies() &&
             ((zone instanceof EastsideRegionOne) ||
              (zone instanceof SierraNevada) ||
              (zone instanceof SouthernCalifornia) ||
              (zone instanceof Teton) ||
              (zone instanceof NorthernCentralRockies))) {
      //      doEvuProducingSeed(true);
      doEvuWeedEncroachment();
    }

    if ((zone instanceof EastsideRegionOne) ||
        (zone instanceof Teton) ||
        (zone instanceof NorthernCentralRockies)) {
      doEvuConiferEncroachment();
    }

    MultipleRunSummary mrSummary = simulation.getMultipleRunSummary();
    for (int i = 0; i < allEvu.length; i++) {
      if (allEvu[i] == null) {
        continue;
      }
  //      areaSummary.updateProcessData(allEvu[i]);
      areaSummary.updateEmissions(allEvu[i]);
      if (mrSummary != null) {
        mrSummary.updateSummaries(allEvu[i]);
      }

    }
    areaSummary.finishEmissions();

    if (Simulation.getInstance().getWriteDatabase()) {
      writeSimulationDatabase();
    }
    if (Simulation.getInstance().getWriteAccess()) {
      PrintWriter fout = Simulation.getInstance().getAccessEvuSimDataOut();
      PrintWriter trackOut = Simulation.getInstance().getAccessTrackingSpeciesOut();
      writeSimulationAccessFiles(fout,trackOut);
    }
  }

  private void doLockInProcesses() {
    int cStep = Simulation.getCurrentTimeStep();
    if (processSchedule == null) { return; }

    Vector applications = processSchedule.getApplications();
    if (applications == null) { return; }

    for (int i = 0; i < applications.size(); i++) {
      ProcessApplication processApp = (ProcessApplication) applications.elementAt(i);

      ProcessType process = processApp.getProcess().getType();
      Vector chosenUnits = processApp.getUserChosenUnits();
      if (chosenUnits == null) { continue; }

      if (RegionalZone.isWyoming() &&
          (process != ProcessType.PRAIRIE_DOG_ACTIVE &&
           process != ProcessType.PRAIRIE_DOG_INACTIVE)) {
        continue;
      }
      for (int j = 0; j < chosenUnits.size(); j++) {
        Evu evu = (Evu) chosenUnits.elementAt(j);
        ArrayList<Lifeform> lives;
        if (this.multipleLifeformsEnabled()) {
          lives = Process.getProcessLifeforms(process);
        }
        else {
          lives = Lifeform.getAllValuesList();
        }
        for (int l=0; l<lives.size(); l++) {
          if (evu.hasLifeform(lives.get(l),cStep)) {
            evu.updateState(lives.get(l),process, (short) Evu.L, Climate.Season.YEAR);
          }
        }
      }
    }
  }
/**
 * Does user treatments, system treatments, attribute treatments, and process treatments.
 */
  private void doTreatments() {
    if (treatmentSchedule == null) { return; }

    int tStep =  Simpplle.getCurrentSimulation().getCurrentTimeStep();

    Vector applications = treatmentSchedule.getApplications(tStep);
    if (applications == null) { return; }

    doUserTreatments(applications);

    doSystemTreatments(applications);

    doAttributeTreatments(applications);

    doProcessProbTreatments(applications);
  }

  private void doProcessProbTreatments(Vector applications) {
    for(int i=0;i<applications.size();i++) {
      TreatmentApplication app = (TreatmentApplication) applications.elementAt(i);
      TreatmentType treatmentType  = app.getTreatmentType();
      Vector        treatmentUnits = app.getProcessProbChosenUnits();
      if (treatmentUnits == null) { continue; }

      for(int j=0;j<treatmentUnits.size();j++) {
        Evu evu = (Evu) treatmentUnits.elementAt(j);
        // Don't treat an already treated unit.
        if (evu.getCurrentTreatment() != null) { continue; }

        Treatment treatment = new Treatment(treatmentType);
        treatment.setPreventReTreatment(app.preventReTreatment());
        treatment.setPreventReTreatmentTimeSteps(app.getPreventReTreatmentTimeSteps());
        treatment.doChange(evu);

        // Do not do follow up if treatment was not applied.
        int status = treatment.getStatus();
        if (status == Treatment.NOT_APPLIED ||
            status == Treatment.INFEASIBLE) {
          app.removeFollowUpTreatmentUnitId(evu.getId());
        }
      }
    }
  }

  private void doAttributeTreatments(Vector applications) {
    for(int i=0;i<applications.size();i++) {
      TreatmentApplication app = (TreatmentApplication) applications.elementAt(i);
      TreatmentType        treatmentType  = app.getTreatmentType();
      Vector               treatmentUnits = app.getAttributeChosenUnits();
      if (treatmentUnits == null) { continue; }

      for(int j=0;j<treatmentUnits.size();j++) {
        Evu evu = (Evu) treatmentUnits.elementAt(j);
        Treatment treatment = new Treatment(treatmentType);
        treatment.setPreventReTreatment(app.preventReTreatment());
        treatment.setPreventReTreatmentTimeSteps(app.getPreventReTreatmentTimeSteps());
        treatment.doChange(evu);

        // Do not do follow up if treatment was not applied.
        int status = treatment.getStatus();
        if (status == Treatment.NOT_APPLIED ||
            status == Treatment.INFEASIBLE) {
          app.removeFollowUpTreatmentUnitId(evu.getId());
        }
      }
    }
  }

  private void doSystemTreatments(Vector applications) {
    for(int i=0;i<applications.size();i++) {
      TreatmentApplication app = (TreatmentApplication) applications.elementAt(i);
      if (!app.isSystemGenerated()) { continue; }

      TreatmentType treatmentType  = app.getTreatmentType();
      Vector        treatmentUnits = app.getUserChosenUnits();
      if (treatmentUnits == null) { continue; }

      for(int j=0;j<treatmentUnits.size();j++) {
        Evu evu = (Evu) treatmentUnits.elementAt(j);
        // Don't treat an already treated unit.
        if (evu.getCurrentTreatment() != null) { continue; }

        Treatment treatment = new Treatment(treatmentType);
        treatment.setPreventReTreatment(app.preventReTreatment());
        treatment.setPreventReTreatmentTimeSteps(app.getPreventReTreatmentTimeSteps());
        treatment.doChange(evu);

        // Do not do follow up if treatment was not applied.
        int status = treatment.getStatus();
        if (status == Treatment.NOT_APPLIED ||
            status == Treatment.INFEASIBLE) {
          app.removeFollowUpTreatmentUnitId(evu.getId());
        }
      }
    }
  }

  private void doUserTreatments(Vector applications) {
    for(int i=0;i<applications.size();i++) {
      TreatmentApplication app = (TreatmentApplication) applications.elementAt(i);
      TreatmentType        treatmentType  = app.getTreatmentType();
      Vector               treatmentUnits = app.getUserChosenUnits();
      if (treatmentUnits == null || app.isSystemGenerated()) {
        continue;
      }

      for(int j=0;j<treatmentUnits.size();j++) {
        Evu evu = (Evu) treatmentUnits.elementAt(j);
        // Don't treat an already treated unit.
        if (evu.getCurrentTreatment() != null) { continue; }

        Treatment treatment = new Treatment(treatmentType);
        treatment.setPreventReTreatment(app.preventReTreatment());
        treatment.setPreventReTreatmentTimeSteps(app.getPreventReTreatmentTimeSteps());
        treatment.doChange(evu);

        // Do not do follow up if treatment was not applied.
        int status = treatment.getStatus();
        if (status == Treatment.NOT_APPLIED ||
            status == Treatment.INFEASIBLE) {
          app.removeFollowUpTreatmentUnitId(evu.getId());
        }
      }
    }
  }

  public void doEmissions() {
    AreaSummary areaSummary = Simpplle.getAreaSummary();
    int         tSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    int j;
    for(int i=0;i<=maxEvuId;i++) {
      if (allEvu[i] == null) { continue; }
      for(j=1; j<=tSteps; j++) {
        areaSummary.updateEmissions(allEvu[i], j);
      }
    }
    for(j=1; j<=tSteps; j++) {
      areaSummary.finishEmissions(j);
    }
  }

  public void doEvuSpread() {
    Simpplle.getAreaSummary().doSpread();
  }


  /**
   * Spread a Process that originated in an Evu to its
   * adjacent units if conditions are correct.
   */
//  private void doEvuSpreadOld() {
//    int           i=0, j=0;
//    Evu           evu;
//    Process       process;
//    ProcessType   processType, originProcessType;
//    TreatmentType currentTreatment;
//    Treatment     treat;
//    RegionalZone  zone = Simpplle.currentZone;
//    boolean       moreSpread = false, result = false;
//    boolean       extreme;
//    boolean[]     tmp;
//
//    for(i=0;i<=maxEvuId;i++) {
//      origin[i]      = false;
//      spread[i]      = false;
//      weatherProb[i] = -1;
//    }
//
//    extremeFires = new Hashtable();
//    fireSeason   = new Hashtable();
//
//    // First determine which units have spreading processes.
//    for(i=0;i<=maxEvuId;i++) {
//      evu = allEvu[i];
//      if (evu == null) { continue; }
//      process          = evu.getCurrentProcess();
//      processType      = process.getType();
// //     treat            = evu.getCurrentTreatment();
// //     currentTreatment = (treat != null) ? treat.getType() : null;
//
//      if (process.isSpreading()) {
//        weatherProb[i] = 0;
//        evu.setOriginUnit(evu);
//        evu.setOriginProcess(process);
//        origin[i] = true;
//        moreSpread = true;
//
//        if (processType.equals(ProcessType.STAND_REPLACING_FIRE)) {
//          extreme = FireEvent.isExtremeSpread();
//          addExtremeFireEvent(evu,new Boolean(extreme));
//        }
//        if (processType.equals(ProcessType.STAND_REPLACING_FIRE) ||
//            processType.equals(ProcessType.MIXED_SEVERITY_FIRE)  ||
//            processType.equals(ProcessType.LIGHT_SEVERITY_FIRE)) {
//          addFireSeason(evu,FireEvent.getFireSeason());
//        }
//      }
//    }
//
//    // Now spread from adjacent units to their adjacent units,
//    // and so on, until we can't spread anymore.
//    int prob;
//
//    while (moreSpread) {
//      moreSpread = false;
//
//      /* Assign a random probability for each Fire Event.
//         This will be used later to determine a weather event
//         will stop the spread of the fire event.  We want a
//         new probability value af each iteration through the units.
//         In addition check to see if the Fire Event has grown large
//         enough to make it extreme spread.
//      */
//      for (i=0; i<allEvu.length; i++) {
//        if (allEvu[i] == null || weatherProb[i] == -1) { continue; }
//
//        prob = Simpplle.currentSimulation.random();
//        weatherProb[allEvu[i].getId()] = prob;
//
//        if (FireEvent.isExtremeEvent(allEvu[i])) {
//          addExtremeFireEvent(allEvu[i], new Boolean(true));
//        }
//      }
//
//      for(i=0;i<allEvu.length;i++) {
//        evu = allEvu[i];
//        if (evu != null && origin[i]) {
//          prob = weatherProb[evu.getOriginUnit().getId()];
//          if (FireEvent.doSpreadEndingWeather(zone,evu,prob)) {
//            continue;
//          }
//
//          processType       = evu.getCurrentProcess().getType();
//          process           = evu.getOriginProcess();
//          originProcessType = process.getType();
//
//          // This check is probably not necessary,
//          // but I will leave just in case.
//          if (processType.isFireProcess() &&
//              originProcessType.isFireProcess() == false) {
//            continue;
//          }
//          else {
//            result = evu.doSpread(origin,spread);
//            if (result) { moreSpread = true; }
//          }
//        }
//      }
//      if (moreSpread) {
//        tmp    = origin;
//        origin = spread;
//        spread = tmp;
//        for(j=0;j<=maxEvuId;j++) {spread[j] = false;}
//      }
//    }
//  }

//  private void doEvuProducingSeed() {
//    doEvuProducingSeed(false);
//  }

//  private void doEvuProducingSeed(boolean postTreatment) {
//    for(int i=0;i<allEvu.length;i++) {
//      if (allEvu[i] != null) {
//        allEvu[i].calculateProducingSeed(postTreatment);
//      }
//    }
//  }

  /**
   * Determine the next VegetativeType for the Evu.
   */
  private void doEvuNextState() {
    for(int i=0;i<allEvu.length;i++) {
      if (allEvu[i] != null) {
        if (multipleLifeformsEnabled()) {
          allEvu[i].doNextStateMultipleLifeform();
          DoCompetitionLogic.getInstance().doLogic(allEvu[i]);
//          allEvu[i].doCompetition();
        }
        else {
          allEvu[i].doNextState();
        }
      }
    }
  }

  private void doEvuWeedEncroachment() {
    int                  i, j;
    Evu                  evu, adj;
    AdjacentData[]       adjacentData;
    HabitatTypeGroupType groupType;
    Species              species;
    SizeClass            sizeClass;
    Density              density;
    int                  age;
    HabitatTypeGroup     htGrp, adjHtGrp;
    VegetativeType       newState = null;
    RegionalZone         zone = Simpplle.getCurrentZone();
    int                  zoneId = zone.getId();
    boolean              effectiveSpray = false;
    Treatment            treat;
    TreatmentType        treatType;

    for(i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu != null) {
        htGrp     = evu.getHabitatTypeGroup();
        groupType = htGrp.getType();

        VegSimStateData state = evu.getState();
        if (state == null) { continue; }
        species   = state.getVeg().getSpecies();
        sizeClass = state.getVeg().getSizeClass();
        age       = state.getVeg().getAge();
        density   = state.getVeg().getDensity();


        treat     = evu.getLastTreatment();
        treatType = TreatmentType.NONE;
        if (treat != null) { treatType = treat.getType(); }

        effectiveSpray = (treatType == TreatmentType.HERBICIDE_SPRAYING &&
                          treat.getStatus() == Treatment.EFFECTIVE);

        if ((groupType.equals(HabitatTypeGroupType.NF1A) ||
             groupType.equals(HabitatTypeGroupType.NF1B) ||
             groupType.equals(HabitatTypeGroupType.NF1C) ||
             groupType.equals(HabitatTypeGroupType.NF2A) ||
             groupType.equals(HabitatTypeGroupType.NF2B) ||
             groupType.equals(HabitatTypeGroupType.NF2C) ||
             groupType.equals(HabitatTypeGroupType.NF2D) ||
             groupType.equals(HabitatTypeGroupType.NF3A) ||
             // Sierra Nevada and Southern California Zones.
             groupType.equals(HabitatTypeGroupType.FTH_M) ||
             groupType.equals(HabitatTypeGroupType.FTH_X) ||
             groupType.equals(HabitatTypeGroupType.LM_M)  ||
             groupType.equals(HabitatTypeGroupType.LM_X)  ||
             groupType.equals(HabitatTypeGroupType.UM_M)  ||
             groupType.equals(HabitatTypeGroupType.UM_X)  ||
             groupType.equals(HabitatTypeGroupType.SA)) &&
            ((age == 1 && density == Density.TWO && sizeClass == SizeClass.UNIFORM &&
              (species == Species.ALTERED_NOXIOUS || species == Species.NOXIOUS)) ||
             // Sierra Nevada and Southern California Zones.
             (species == Species.EXOTIC_GRASSES && sizeClass == SizeClass.UNIFORM &&
              age == 1 && density == Density.TWO) ||
             (species == Species.CSS_EXOTICS && sizeClass == SizeClass.OPEN_MID_SHRUB &&
              age == 1 && density == Density.ONE))) {

          if (effectiveSpray) {
            continue;
          }

          adjacentData = evu.getAdjacentData();
          for(j=0;j<adjacentData.length;j++) {
            adj = adjacentData[j].getEvu();
            treat     = adj.getLastTreatment();
            treatType = TreatmentType.NONE;
            if (treat != null) { treatType = treat.getType(); }

            effectiveSpray = (treatType == TreatmentType.HERBICIDE_SPRAYING &&
                              treat.getStatus() == Treatment.EFFECTIVE);

            if (adj.isWeedCandidate() && !effectiveSpray &&
                evu.weedWillSpread(adj)) {
              adjHtGrp = adj.getHabitatTypeGroup();
              if (zoneId == ValidZones.SIERRA_NEVADA ||
                  zoneId == ValidZones.SOUTHERN_CALIFORNIA) {
                if (species == Species.CSS) {
                  newState = adjHtGrp.getVegetativeType(
                      Species.CSS_EXOTICS,SizeClass.OPEN_MID_SHRUB,Density.ONE);
                }
                else {
                  newState = adjHtGrp.getVegetativeType(
                      Species.EXOTIC_GRASSES, SizeClass.UNIFORM, Density.ONE);
                }
              }
              else {
                newState = adjHtGrp.getVegetativeType(
                   Species.NOXIOUS,SizeClass.UNIFORM,Density.TWO);
              }
              if (newState != null) {
                adj.updateState(newState);
              }
            }
          }
        }
      }
    }
  }

  private void doEvuConiferEncroachment() {
    AdjacentData[]       adjacentData;
    Hashtable            seedSource;
    Species              species, encroachmentSpecies;
    HabitatTypeGroupType groupType;
    int                  acres;
    Species[]            sortedKeys;
    Species[]            key;
    int[]                value;
    Integer              keyValueIndex;
    int                  index;
    VegetativeType       newState = null;
    HabitatTypeGroup     htGrp;
    Evu                  evu, adj;
    int                  i, j;
    int                  numDecades = 0;
    RegionalZone         zone = Simpplle.currentZone;
//    int                  five=5, ten=10, thirty=30, fifty=50;
//    int                  multiplier;
//
//    multiplier = Utility.pow(10,getAcresPrecision());
//    five   *= multiplier;
//    ten    *= multiplier;
//    thirty *= multiplier;
//    fifty  *= multiplier;

    for (i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu != null) {
        htGrp   = evu.getHabitatTypeGroup();
        groupType = htGrp.getType();

        if (evu.hasLifeform(Lifeform.TREES)) {
          continue;
        }
        Species evuSpecies = (Species)evu.getState(SimpplleType.SPECIES);
        if (evuSpecies == null) { continue; }

//        if (evuSpecies == Species.PP || evuSpecies == Species.PP_DF ||
//            evuSpecies == Species.DF_PP_LP ||
//            evuSpecies == Species.DF || evuSpecies == Species.DF_AF ||
//            evuSpecies == Species.DF_LP || evuSpecies == Species.DF_ES ||
//            evuSpecies == Species.DF_AF_ES || evuSpecies == Species.DF_LP_ES ||
//            evuSpecies == Species.PF || evuSpecies == Species.PF_LP ||
//            evuSpecies == Species.LP ||
//            evuSpecies == Species.L_LP || evuSpecies == Species.LP_AF ||
//            evuSpecies == Species.LP_GF || evuSpecies == Species.ES_LP ||
//            evuSpecies == Species.JUSC ||
//            evuSpecies == Species.DF || evuSpecies == Species.AF ||
//            evuSpecies == Species.LP || evuSpecies == Species.ES ||
//            evuSpecies == Species.QA || evuSpecies == Species.QA_MC ||
//            evuSpecies == Species.CW_MC || evuSpecies == Species.ES_AF ||
//            evuSpecies == Species.ES_LP || evuSpecies == Species.LP_AF ||
//            evuSpecies == Species.DF_LP || evuSpecies == Species.DF_ES ||
//            evuSpecies == Species.DF_AF || evuSpecies == Species.DF_LP_AF ||
//            evuSpecies == Species.DF_LP_ES || evuSpecies == Species.AF_ES_LP) {
//          continue;
//        }

        if ((groupType.equals(HabitatTypeGroupType.NF1A) == false) &&
            (groupType.equals(HabitatTypeGroupType.NF1B) == false) &&
            (groupType.equals(HabitatTypeGroupType.NF1C) == false) &&
            (groupType.equals(HabitatTypeGroupType.NF2A) == false) &&
            (groupType.equals(HabitatTypeGroupType.NF2B) == false) &&
            (groupType.equals(HabitatTypeGroupType.NF2C) == false) &&
            (groupType.equals(HabitatTypeGroupType.NF2D) == false) &&
            (groupType.equals(HabitatTypeGroupType.NF4B) == false)) {
          continue;
        }

        newState = null;
        acres = evu.getAcres();

        numDecades = ConiferEncroachmentLogicData.getTimeValue(acres);
        if (numDecades == ConiferEncroachmentLogicData.NONE) {
          continue;
        }

//        if (acres > fifty) {
//          continue;
//        }
//        else if (acres <= fifty && acres > thirty) {
//          numDecades = 20;
//        }
//        else if (acres <= thirty && acres > ten) {
//          numDecades = 15;
//        }
//        else if (acres <= ten && acres > five) {
//          numDecades = 10;
//        }
//        else {
//          numDecades = 5;
//        }

        adjacentData = evu.getAdjacentData();
        if (adjacentData == null) { continue; }
        seedSource = new Hashtable(adjacentData.length);
        key        = new Species[adjacentData.length];
        value      = new int[adjacentData.length];
        index      = 0;

        for(j=0;j<key.length;j++) {
          key[j]   = null;
          value[j] = -1;
        }

        // Build a hashtable: key = species, value = acres;
        //   if species is seed producing add its acres to the
        //   the value already in the hashtable.
        for(j=0;j<adjacentData.length;j++) {
          adj = adjacentData[j].getEvu();

          if (adj.succession_n_decades(numDecades) &&
              adj.producingSeed(numDecades)) {
            species = (Species)adj.getState(SimpplleType.SPECIES);
            keyValueIndex = (Integer) seedSource.get(species);
            if (keyValueIndex == null) {
              acres         = adj.getAcres();
              keyValueIndex = new Integer(index);
              seedSource.put(species,keyValueIndex);
              index++;
            }
            else {
              acres = value[keyValueIndex.intValue()] + adj.getAcres();
            }
            key[keyValueIndex.intValue()]   = species;
            value[keyValueIndex.intValue()] = acres;
          }
        }

        if (seedSource.size() == 0) {
          continue;
        }

        Utility.sort(key,value);
        sortedKeys = key;

        for(j=0;j<sortedKeys.length;j++) {
          if (sortedKeys[j] == null) {continue;}

          species = sortedKeys[j];

          if (species == Species.PP || species == Species.PP_DF ||
              species == Species.DF_PP_LP) {
            encroachmentSpecies = Species.PP;
          }
          else if (species == Species.DF       || species == Species.DF_AF ||
                   species == Species.DF_LP    || species == Species.DF_ES ||
                   species == Species.DF_AF_ES || species == Species.DF_LP_ES) {
            encroachmentSpecies = Species.DF;
          }
          else if (species == Species.PF || species == Species.PF_LP) {
            encroachmentSpecies = Species.PF;
          }
          else if (species == Species.LP    || /*species == Species.LP_DF ||*/
                   species == Species.L_LP  || species == Species.LP_AF ||
                   species == Species.LP_GF || species == Species.ES_LP
                   /*species == Species.WB_LP*/) {
            encroachmentSpecies = Species.LP;
          }
          else if (species == Species.JUSC) {
            encroachmentSpecies = Species.JUSC;
          }
          else {
            encroachmentSpecies = null;
          }

          if (groupType.equals(HabitatTypeGroupType.NF4B) &&
              (species == Species.DF       || species == Species.AF       ||
               species == Species.LP       || species == Species.ES       ||
               species == Species.QA       || species == Species.QA_MC    ||
               species == Species.CW_MC    || species == Species.ES_AF    ||
               species == Species.ES_LP    || species == Species.LP_AF    ||
               species == Species.DF_LP    || species == Species.DF_ES    ||
               species == Species.DF_AF    || species == Species.DF_LP_AF ||
               species == Species.DF_LP_ES || species == Species.AF_ES_LP)) {
            encroachmentSpecies = species;
          }

          if (encroachmentSpecies != null) {
            newState = htGrp.getSeedSapState(encroachmentSpecies);
            if (newState != null) {
              break;
            }
          }
        }
        if (newState != null) {
//          evu.updateState(newState);
          evu.addNewLifeformState(newState);
        }
      }
    }
  }

  // *************
  // ** Reports **
  // *************

  private String determineOwnerSpecialKey(Evu evu, int option) {
    switch (option) {
      case Reports.OWNERSHIP:    return evu.getOwnership();
      case Reports.SPECIAL_AREA: return evu.getSpecialArea();
      case Reports.OWNER_SPECIAL:
        return evu.getOwnership() + "/" + evu.getSpecialArea();
      default:
        return "UNKNOWN";
    }
  }

//  public ArrayList findUnitsComplex(HashMap specialAreaHm, HashMap ownershipHm) {
//    ArrayList specialAreaList=null;
//    ArrayList ownershipList=null;
//
//    if (specialAreaHm != null && specialAreaHm.size() > 0) {
//      specialAreaList = new ArrayList(specialAreaHm.keySet());
//    }
//    if (ownershipHm != null && ownershipHm.size() > 0) {
//      ownershipList = new ArrayList(ownershipHm.keySet());
//    }
//
//    return findUnitsComplex(null,specialAreaList,ownershipList);
//  }
//  public ArrayList findUnitsComplex(ArrayList groupList,
//                                    ArrayList specialAreaList,
//                                    ArrayList ownershipList) {
//    ArrayList units = new ArrayList();
//    HabitatTypeGroupType group;
//    String               specialArea;
//    String               ownership;
//
//    for (int i=0; i<allEvu.length; i++) {
//      if (allEvu[i] == null) { continue; }
//
//      group       = allEvu[i].getHabitatTypeGroup().getType();
//      specialArea = allEvu[i].getSpecialArea();
//      ownership   = allEvu[i].getOwnership();
//
//      if (groupList != null && groupList.contains(group) == false) {
//        continue;
//      }
//      if (specialAreaList != null && specialAreaList.contains(specialArea) == false) {
//        continue;
//      }
//      if (ownershipList != null && ownershipList.contains(ownership) == false) {
//        continue;
//      }
//
//      units.add(allEvu[i]);
//    }
//    return units;
//  }

//  private void addListToQuery(StringBuffer strBuf, ArrayList<?> list) {
//    strBuf.append("(");
//    Object value;
//    for (Iterator it=list.iterator(); it.hasNext(); ) {
//      value = it.next();
//      if (value instanceof SimpplleType) { strBuf.append("'"); }
//      strBuf.append(value);
//      if (value instanceof SimpplleType) { strBuf.append("'"); }
//      if (it.hasNext()) { strBuf.append(", "); }
//    }
//    strBuf.append(") ");
//  }

  /**
    * collect treatment data into a hashtable
    * keys are treatment names.
    * value is an array of acres by time step
    * @param timeSteps is the number of times steps run.
    * @param kind is either Treatment.APPLIED, Treatment.INFEASIBLE or Treatment.NOT_APPLIED
    * return a hashtable
    */
  public Hashtable collectTreatmentData (int timeSteps, int kind) {
    Hashtable      data;
    Treatment      treat;
    int            status;
    String         key;
    int            i, j, k;
    int[]          acres;
    Evu            evu;
    boolean        applied;

    data = new Hashtable();

    for(i=0;i<=timeSteps;i++) {
      for(j=0;j<=maxEvuId;j++) {
        evu = allEvu[j];
        if (evu == null) { continue; }

        treat = evu.getTreatment(i,false);
        if (treat == null) { continue; }

        status  = treat.getStatus();
        switch (kind) {
          case Treatment.APPLIED:
            if (status != Treatment.APPLIED &&
                status != Treatment.EFFECTIVE)
            { continue; }
            break;
          case Treatment.NOT_APPLIED:
            if (status != Treatment.NOT_APPLIED) { continue; }
            break;
          case Treatment.INFEASIBLE:
            if (status != Treatment.INFEASIBLE) { continue; }
            break;
          default:
            continue;
        }

        key   = treat.toString();
        acres = (int[]) data.get(key);
        if (acres == null) {
          acres = new int[timeSteps+1];
          for(k=0;k<=timeSteps;k++) { acres[k] = 0; }
          data.put(key,acres);
        }
        acres[i] += evu.getAcres();
      }
    }
    return data;
  }

  /**
    * collect treatment data into a hashtable
    * keys are either special area or ownership strings
    * value is another hashtable
    *    key   is the treatment name
    *    value is an array of acres by time step
    * @param timeSteps is the number of times steps run.
    * @param kind is either Treatment.APPLIED, Treatment.INFEASIBLE or Treatment.NOT_APPLIED
    * @param option is one of:
    *    Reports.OWNERSHIP, Reports.SPECIAL_AREA, Reports.OWNER_SPECIAL
    * return a hashtable
    */
  public Hashtable collectTreatmentData(int timeSteps, int kind, int option) {
    Hashtable      data, optionHt;
    Treatment      treat;
    int            status;
    String         key;
    int            i, j, k;
    int[]          acres;
    Evu            evu;
    boolean        applied;

    optionHt = new Hashtable();
    data     = new Hashtable();

    for(i=0;i<=timeSteps;i++) {
      for(j=0;j<=maxEvuId;j++) {
        evu = allEvu[j];
        if (evu == null) { continue; }

        treat = evu.getTreatment(i,false);
        if (treat == null) { continue; }

        status  = treat.getStatus();
        switch (kind) {
          case Treatment.APPLIED:
            if (status != Treatment.APPLIED &&
                status != Treatment.EFFECTIVE)
            { continue; }
            break;
          case Treatment.NOT_APPLIED:
            if (status != Treatment.NOT_APPLIED) { continue; }
            break;
          case Treatment.INFEASIBLE:
            if (status != Treatment.INFEASIBLE) { continue; }
            break;
          default:
            continue;
        }

        key  = determineOwnerSpecialKey(evu,option);
        data = (Hashtable) optionHt.get(key);
        if (data == null) {
          data = new Hashtable();
          optionHt.put(key,data);
        }

        key   = treat.toString();
        acres = (int[]) data.get(key);
        if (acres == null) {
          acres = new int[timeSteps+1];
          for(k=0;k<=timeSteps;k++) { acres[k] = 0; }
          data.put(key,acres);
        }
        acres[i] += evu.getAcres();
      }
    }
    return optionHt;
  }

/**
 * Checks to see if any of the evu's in this area have a particular lifeform at a particular time step.
 * @param lifeform lifeform being checked
 * @param tStep time step
 * @return true if there is an evu in this are with the searched lifeform
 */
  private boolean hasLifeformEvus(Lifeform lifeform, int tStep) {
    if (lifeform == null) { return true; }
    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] != null && allEvu[i].hasLifeform(lifeform,tStep)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Writes out files of GIS data suitable for reading into arcview.
   * One file is written for the each time step in a simulation,
   * at the end of each time step.
   * The fields written are:
   *    Slink, species, size class, density, process, and treatment.
   * @param outputFile is a File.
   */
  public void produceArcFiles(File outputFile) throws SimpplleError {
    // Make sure that gis files for each run are put in a separate directory
    if (Simulation.getInstance().isMultipleRun()) {
      String dir = outputFile.getParent();
      String name = outputFile.getName();
      File newDir = new File(dir, "gis_run" + (Simulation.getCurrentRun()+1));
      if (!newDir.exists() && !newDir.mkdir()) {
        throw new SimpplleError("Unable to create directory: " +
                                newDir.toString());
      }
      outputFile = new File(newDir, name);
    }

    int cStep = Simulation.getCurrentTimeStep();
    if (Area.multipleLifeformsEnabled()) {
      Lifeform[] lives = Lifeform.getAllValues();
      for (int i=0; i<lives.length; i++) {
        if (hasLifeformEvus(lives[i],cStep)) {
          produceArcFiles(outputFile, cStep, lives[i],false);
        }
      }
    }
    produceArcFiles(outputFile, cStep, null,false); // Dominant Lifeform
    produceArcFiles(outputFile, cStep, null,true); // Combined Lifeforms
  }

  public void produceArcFiles(File outputFile, Lifeform lifeform,boolean combineLives) throws SimpplleError {
    int timeSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    // <= because we are including initial conditions.
    for(int i=0;i<=timeSteps;i++) {
      if (hasLifeformEvus(lifeform,i)) {
        produceArcFiles(outputFile, i, lifeform,combineLives);
      }
    }
  }

  public static File createLifeformOutputFile(File outputFile, Lifeform lifeform, boolean combineLives) throws SimpplleError {
    File result = outputFile;

    if (lifeform != null) {
      String dir = outputFile.getParent();
      String name = outputFile.getName();
      File newDir = new File(dir, lifeform.toString().toLowerCase());
      if (!newDir.exists() && !newDir.mkdir()) {
        throw new SimpplleError("Unable to create directory: " + newDir.toString());
      }
      result = new File(newDir, name);
    }
    else if (combineLives) {
      String dir = outputFile.getParent();
      String name = outputFile.getName();
      File newDir = new File(dir, "all");
      if (!newDir.exists() && !newDir.mkdir()) {
        throw new SimpplleError("Unable to create directory: " + newDir.toString());
      }
      result = new File(newDir, name);

    }
    return result;
  }

  /**
   * Write out GIS files for a particular time Step.
   * @param outputFile is a File.
   * @param tStep is the time step number.
   */
  private void produceArcFiles(File outputFile, int tStep, Lifeform lifeform, boolean combineLives) throws SimpplleError {
    PrintWriter fout;
    File        tmpFile, newFile;

    try {
      outputFile = createLifeformOutputFile(outputFile,lifeform,combineLives);

      tmpFile = Utility.makeNumberedPathname(outputFile, tStep);
      newFile = Utility.makeSuffixedPathname(tmpFile, "-update", "txt");

      fout = new PrintWriter(new FileOutputStream(newFile));

      produceArcFiles(fout,tStep,lifeform,combineLives);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
      throw new SimpplleError(IOX.getMessage(),IOX);
    }
  }

  private void produceArcFiles(PrintWriter fout, int tStep, Lifeform lifeform, boolean combineLives) {
    VegSimStateData state;
    Evu            evu;
    ProcessType    process;
    Treatment      treatment;
    String         species;
    String         sizeClass;
    String         processStr, treatmentStr; /*probStr*/
    float          prob;
    String         density;
    int            slink;

    String lifeStr;
    if (lifeform == null) {
      lifeStr = (combineLives ? " Combined Lifeforms" : "");
    }
    else {
      lifeStr = " Lifeform: " + lifeform.toString();
    }

    StringBuffer buf = new StringBuffer(40+lifeStr.length());
    buf.append("Generating GIS File for Time Step: ");
    buf.append(tStep);
    buf.append(lifeStr);
    Simpplle.setStatusMessage(buf.toString());
    buf=null;

    fout.println("Slink,sim_Species,sim_Size,sim_Canopy,sim_Process,sim_Treatment,sim_Probability");
//    fout.println("Slink,sim_Species,sim_Size,sim_Canopy,sim_Process,sim_Treatment");

    for(int i=0;i<=maxEvuId;i++) {
      evu = allEvu[i];
      if (evu == null) { continue; }

      if (lifeform != null) {
        state = evu.getState(tStep,lifeform);
      }
      else {
        state = evu.getStateMostDominant(tStep);
      }
      if (state == null) { continue; }

      if (!combineLives) {
        species = state.getVegType().getSpecies().toString();
        sizeClass = state.getVegType().getSizeClass().toString();
        density = state.getVegType().getDensity().toString();

        process = state.getProcess();
        processStr = (process != null) ? process.toString() : "NIL";
//        processStr = Utility.dashesToUnderscores(processStr);
      }
      else {
        species    = evu.getStateCombineLives(tStep,SimpplleType.SPECIES);
        sizeClass  = evu.getStateCombineLives(tStep,SimpplleType.SIZE_CLASS);
        density    = evu.getStateCombineLives(tStep,SimpplleType.DENSITY);
        processStr = evu.getStateCombineLives(tStep,SimpplleType.PROCESS);
      }

      treatmentStr = "NONE";
      treatment    = evu.getTreatment(tStep);

//      probStr = evu.getProbStr(tStep);
      prob = Evu.getFloatProb(state.getProb());

      if (treatment != null) {
        switch (treatment.getStatus()) {
          case Treatment.APPLIED:
            treatmentStr = treatment.toString(); break;
          case Treatment.NOT_APPLIED:
            treatmentStr = "NOT-APPLIED"; break;
          case Treatment.INFEASIBLE:
            treatmentStr = "INFEASIBLE"; break;
          default:
            treatmentStr = "NONE";
        }
      }
      treatmentStr = Utility.dashesToUnderscores(treatmentStr);

      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(2);

      fout.print(evu.getId());
      fout.print(",");
      fout.print(species);
      fout.print(",");
      fout.print(sizeClass);
      fout.print(",");
      fout.print(density);
      fout.print(",");
      fout.print(processStr);
      fout.print(",");
      fout.print(treatmentStr);
      fout.print(",");
      fout.println(nf.format(prob));
    }
  }

  /**
   * Writes out a files of GIS Spread data suitable for reading into arcview.
   * One file is written for each time step in a simulation.
   * The fields written are:
   *    Slink + all of the summaryProcesses.
   * @param outputFile is a File.
   */
  public void produceSpreadArcFiles(File outputFile) throws SimpplleError {
    produceSpreadArcFiles(outputFile,true);
  }
/**
 * Writes out a files of GIS Spread data suitable for reading into arcview.
 * One file is written for each time step in a simulation.
 *
 * @param outputFile
 * @param viaUserMenuAction
 * @throws SimpplleError
 */
  public void produceSpreadArcFiles(File outputFile, boolean viaUserMenuAction) throws SimpplleError {
    if (viaUserMenuAction == false) {
      // Make sure that gis files for each run are put in a separate directory
      if (Simulation.getInstance().isMultipleRun()) {
        String dir = outputFile.getParent();
        String name = outputFile.getName();
        File newDir = new File(dir, "gis_run" + (Simulation.getCurrentRun() + 1));
        if (!newDir.exists() && !newDir.mkdir()) {
          throw new SimpplleError("Unable to create directory: " +
                                  newDir.toString());
        }
        outputFile = new File(newDir, name);
      }
      produceSpreadArcFiles(outputFile, Simulation.getCurrentTimeStep());
    }
    else {
      int timeSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

      for (int ts = 1; ts <= timeSteps; ts++) {
        produceSpreadArcFiles(outputFile, ts);
      }
    }
  }
  /**
   * Write out GIS Spread Files for a particular time Step.
   * @param outputFile is a File.
   * @param tStep is the time step number.
   */
  public void produceSpreadArcFiles(File outputFile, int tStep) {
    PrintWriter fout;
    File tmpFile, newFile;

    tmpFile = Utility.makeNumberedPathname(outputFile, tStep);
    newFile = Utility.makeSuffixedPathname(tmpFile,"-spread","txt");

    try {
      fout = new PrintWriter(new FileOutputStream(newFile));

      produceSpreadArcFiles(fout,tStep);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
    catch (SQLException err) {
      System.out.println(err.getMessage() +  " Problems writing spread data to database");
    }
  }

  private void produceSpreadArcFiles(PrintWriter fout, int tStep) throws SQLException {
    RegionalZone  zone        = Simpplle.getCurrentZone();
    AreaSummary   areaSummary = Simpplle.getAreaSummary();
    ProcessType[] summaryProcesses = Process.getSummaryProcesses();
    int           i, j;
    String        value;

    StringBuffer buf = new StringBuffer(45);
    buf.append("Generating GIS Spread File for Time Step ");
    buf.append(tStep);
    Simpplle.setStatusMessage(buf.toString());
    buf=null;

    int count = 0;

    fout.print("Slink");
    for(i=0;i<summaryProcesses.length;i++) {
      fout.print(",");
      fout.print(summaryProcesses[i].getGISPrintName());
    }
    fout.println();

    areaSummary.collectGisSpreadData(tStep);
    for(i=0;i<=maxEvuId;i++) {
      if (allEvu[i] == null) { continue; }

      fout.print(allEvu[i].getId());
      for (j=0; j<summaryProcesses.length; j++) {
        value = areaSummary.getGisSpreadData(allEvu[i], summaryProcesses[j], tStep);
        fout.print(",");
        fout.print(value);
      }
      fout.println();
    }

    areaSummary.clearGisSpreadData();
  }

  /**
   * This function will go through all units and determine
   * the frequency of occurrence for each species, size class,
   * density, or process that occurred during the simulation.
   *
   */
  public int calculateAcreFrequencies(int desiredFreq, SimpplleType[] attributes)
  throws SimpplleError
  {
    int          acres = 0;
    MyInteger    freq;
    SimpplleType.Types[]        kinds;
    int          k;
    HashMap<SimpplleType,MyInteger> freqHm;
    SimpplleType key;
    boolean      freqHighEnough;
    MultipleRunSummary mrSummary = Simpplle.getCurrentSimulation().getMultipleRunSummary();

    kinds = new SimpplleType.Types[] {Evu.PROCESS, Evu.SPECIES, Evu.SIZE_CLASS, Evu.DENSITY};

    for(int i=0; i<=maxEvuId; i++) {
      if (allEvu[i] == null) { continue; }

      freqHighEnough = true;
      for(k=0; k<kinds.length; k++) {
        if (attributes[kinds[k].ordinal()] == null) { continue; }

        freqHm = mrSummary.getFrequency(allEvu[i],kinds[k]);
        key    = attributes[kinds[k].ordinal()];
        freq   = freqHm.get(key);

        if (freq == null || freq.intValue() < desiredFreq) {
          freqHighEnough = false;
          break;
        }
      }
      if (freqHighEnough) {
        acres += allEvu[i].getAcres();
      }
    }
    return Math.round(Area.getFloatAcres(acres));
  }

  public void produceDecadeProbabilityArcFiles(File outputFile)
    throws SimpplleError
  {
    PrintWriter[] fout = new PrintWriter[SimpplleType.MAX];
    File          file;
    SimpplleType.Types[] types =
      new SimpplleType.Types[] {SimpplleType.SPECIES,
                                SimpplleType.SIZE_CLASS,
                                SimpplleType.DENSITY,
                                SimpplleType.PROCESS};

    try {
      String[] suffix = new String[SimpplleType.MAX];
      int      nSteps = Simulation.getInstance().getNumTimeSteps();
      int      j;
      for (int i=0;i<=nSteps;i++) {

        suffix[SimpplleType.SPECIES.ordinal()]    = "-" + Integer.toString(i) + "-species";
        suffix[SimpplleType.SIZE_CLASS.ordinal()] = "-" + Integer.toString(i) + "-size";
        suffix[SimpplleType.DENSITY.ordinal()]    = "-" + Integer.toString(i) + "-canopy";
        suffix[SimpplleType.PROCESS.ordinal()]    = "-" + Integer.toString(i) + "-process";

        for(j=0; j<types.length; j++) {
          file = Utility.makeSuffixedPathname(outputFile, suffix[types[j].ordinal()], "txt");
          fout[types[j].ordinal()] = new PrintWriter(new FileOutputStream(file));
        }

        String msg = "Generating GIS Decade Probability " +
                     "Files for Time Step " + i;
        Simpplle.setStatusMessage(msg);

        produceProbabilityArcFiles(fout, types, i);

        for(j=0; j<types.length; j++) {
          fout[types[j].ordinal()].flush();
          fout[types[j].ordinal()].close();
        }
        Simpplle.clearStatusMessage();

      }
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output files.");
    }
    catch (SQLException | HibernateException ex) {
      throw new SimpplleError("Problems writing to database" + ex.getMessage());
    }
  }

  public void produceProbabilityArcFiles(File outputFile)
    throws SimpplleError
  {
    PrintWriter[] fout = new PrintWriter[SimpplleType.MAX];
    File[]        file = new File[SimpplleType.MAX];
    SimpplleType.Types[] types =
      new SimpplleType.Types[] {SimpplleType.SPECIES,
                                SimpplleType.SIZE_CLASS,
                                SimpplleType.DENSITY,
                                SimpplleType.PROCESS};

    String[] suffix = new String[SimpplleType.MAX];
    suffix[SimpplleType.SPECIES.ordinal()]    = "-" + ALL_PROB_STEP + "-species";
    suffix[SimpplleType.SIZE_CLASS.ordinal()] = "-" + ALL_PROB_STEP + "-size";
    suffix[SimpplleType.DENSITY.ordinal()]    = "-" + ALL_PROB_STEP + "-canopy";
    suffix[SimpplleType.PROCESS.ordinal()]    = "-" + ALL_PROB_STEP + "-process";

    for(int i=0; i<types.length; i++) {
      file[types[i].ordinal()] =
          Utility.makeSuffixedPathname(outputFile, suffix[types[i].ordinal()], "txt");
    }


    try {
      for(int i=0; i<types.length; i++) {
        fout[types[i].ordinal()] = new PrintWriter(new FileOutputStream(file[types[i].ordinal()]));
      }

      Simpplle.setStatusMessage("Generating GIS Probability files...");

      produceProbabilityArcFiles(fout, types, -1);

      for(int i=0; i<types.length; i++) {
        fout[types[i].ordinal()].flush();
        fout[types[i].ordinal()].close();
      }
      Simpplle.clearStatusMessage();

    }
    catch (IOException IOX) {
      System.out.println("Problems writing output files.");
    }
    catch (SQLException ex) {
      throw new SimpplleError("Problems writing to database:" + ex.getMessage());
    }
    catch (HibernateException ex) {
      throw new SimpplleError("Problems writing to database" + ex.getMessage());
    }
  }

  /**
   *
   * @param fout array of File Streams, one for each attribute
   * @param types array of the attribute identifiers
   * @param timeStep array of times steps to use in computations.
   */
  private void produceProbabilityArcFiles(PrintWriter[] fout, SimpplleType.Types[] types,
                                          int timeStep)
    throws SQLException, SimpplleError, HibernateException
  {
    StringBuffer[] expression = new StringBuffer[types.length];

    Simulation         simulation = Simpplle.getCurrentSimulation();
    MultipleRunSummary mrSummary = simulation.getMultipleRunSummary();

    SimpplleType[][] attributes = mrSummary.getAllAttributes();

    // *** Write the header ***
    String fieldName;
    for(int i=0; i<types.length; i++) {
      fout[types[i].ordinal()].print(SLINK_STR);

      for(int j=0; j<attributes[types[i].ordinal()].length; j++) {
        fout[types[i].ordinal()].print(COMMA_STR);
        fieldName = attributes[types[i].ordinal()][j].getGISPrintName();
        fout[types[i].ordinal()].print(fieldName);
      }
      fout[types[i].ordinal()].println();
    }


    HashMap[] attribCount = new HashMap[SimpplleType.MAX];
    MyInteger   freqObj;
    int       expCount=0;
    int       freq;

    for (Evu evu : allEvu) {
      if (evu == null) {
        continue;
      }

      if (timeStep >= 0) {
        attribCount[SPECIES.ordinal()] = mrSummary.getFrequency(evu,SPECIES,timeStep);
        attribCount[SIZE_CLASS.ordinal()] = mrSummary.getFrequency(evu,SIZE_CLASS,timeStep);
        attribCount[DENSITY.ordinal()] = mrSummary.getFrequency(evu,DENSITY,timeStep);
        attribCount[PROCESS.ordinal()] = mrSummary.getFrequency(evu,PROCESS,timeStep);
      }
      else {
        for (int t = 0; t < types.length; t++) {
          attribCount[types[t].ordinal()] = mrSummary.getFrequency(evu, types[t]);
        }
      }

      for (int t = 0; t < types.length; t++) {
        fout[types[t].ordinal()].print(evu.getId());

        for (int j = 0; j < attributes[types[t].ordinal()].length; j++) {
          freqObj = (MyInteger) attribCount[types[t].ordinal()].get(attributes[types[t].ordinal()][j]);
          freq = (freqObj != null) ? freqObj.intValue() : 0;

          fout[types[t].ordinal()].print(COMMA_STR);
          fout[types[t].ordinal()].print(IntToString.get(freq));
        }
        fout[types[t].ordinal()].println();
      }
    }
  }


  public void produceReburnProbabilityFile(File outfile) throws SimpplleError {
    File newOutfile = Utility.makeSuffixedPathname(outfile, "-reburn", "txt");
    PrintWriter fout;
    try {
      fout = new PrintWriter(new FileOutputStream(newOutfile));
    }
    catch (IOException e) {
      throw new SimpplleError("Unable to write output file");
    }

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    float prob;
    fout.println("SLINK,REBURN_PROB");
    for(int i=0; i<=maxEvuId; i++) {
      if (allEvu[i] == null) { continue; }
      prob = allEvu[i].getReburnProbability();
      fout.println(allEvu[i].getId() + "," + nf.format(prob));
    }
    fout.flush();
    fout.close();
  }

  // ** Routines to fix problems with created units.
  public void fixIncorrectStates() {
    Species.clearFixData();

    for(int i=0; i<=maxEvuId; i++) {
      if (allEvu[i] == null || allEvu[i].isValid()) { continue; }
      allEvu[i].fixIncorrectState();
    }
  }

  public void fixEmptyDataUnits() {
    for(int i=0; i<=maxEvuId; i++) {
      if (allEvu[i] == null || allEvu[i].isValid()) { continue; }
      allEvu[i].fixEmptyData();
    }
  }

  /**
   * This function goes thru all of the units that match the given
   * old HabitatTypeGroup and old VegetativeType and changes the values
   * to new ones.  This is called via simpplle.gui.EvuGlobalEditor which
   * is called by simpplle.gui.EvuEditor
   * @see simpplle.gui.EvuEditor
   * @see simpplle.gui.EvuGlobalEditor
   */
  public void makeGlobalUnitChange(HabitatTypeGroup oldHtGrp,
                                   VegetativeType oldVegetativeType,
                                   HabitatTypeGroup newHtGrp,
                                   VegetativeType newVegetativeType,
                                   Lifeform lifeform) {
    String oldHtGrpStr, oldVegTypeStr;

    for(int i=0; i<=maxEvuId; i++) {
      if (allEvu[i] == null) { continue; }
      oldHtGrpStr = oldHtGrp.toString();
      oldVegTypeStr = oldVegetativeType.toString();

      /*
        Most likely the units are being changed because the values are
        invalid.  In that case we need to compare the values rather
        than simply the object as each evu will have different one.
      */
      VegSimStateData state = allEvu[i].getState(lifeform);
      if (state != null &&
          oldHtGrpStr.equals(allEvu[i].getHabitatTypeGroup().toString()) &&
          oldVegTypeStr.equals(state.getVeg().toString())) {
        allEvu[i].setHabitatTypeGroup(newHtGrp);
        allEvu[i].setState(newVegetativeType,Climate.Season.YEAR);
        allEvu[i].updateInitialTreatmentSavedState(newVegetativeType);
      }
    }
  }

  public void printInvalidUnits(File outfile) throws SimpplleError {
    PrintWriter fout;
    try {
      fout = new PrintWriter(new FileWriter(outfile));
    }
    catch (IOException err) {
      throw new SimpplleError("Unable to write to output file.");
    }

    String htGrpStr, vegTypeStr;
    int    invalidCount=0;
    for(int i=0; i<=maxEvuId; i++) {
      if (allEvu[i] == null ||
          (allEvu[i].isHabitatTypeGroupValid() &&
           allEvu[i].isCurrentStateValid())) {
        continue;
      }

      if (allEvu[i].getHabitatTypeGroup() == null) {
        htGrpStr = "";
      }
      else {
        htGrpStr = allEvu[i].getHabitatTypeGroup().toString();
      }
      if (allEvu[i].isHabitatTypeGroupValid() == false) {
        htGrpStr = htGrpStr + "*";
      }

      Set<Lifeform> lives = allEvu[i].getLifeforms(Season.YEAR);
      for (Lifeform life : lives) {
        VegSimStateData state = allEvu[i].getState(life);
        if (state == null || allEvu[i].isCurrentStateValid(life)) {
          continue;
        }
        fout.println(allEvu[i].getId() + "," + htGrpStr + "," +
                     state.toString() + "*");
      }

      invalidCount++;
    }
    fout.println();
    fout.println("* Following an item indicates it is not valid");
    fout.println();
    fout.println("Number of Invalid Units: " + invalidCount);
    fout.flush();
    fout.close();
  }

  public void exportCreationFiles(File outfile) throws SimpplleError {
    try {
      File prefix = Utility.stripExtension(outfile);
      File neighborsFile = Utility.makeSuffixedPathname(prefix,"","spatialrelate");
      File attributesFile = Utility.makeSuffixedPathname(prefix,"","attributesall");

      PrintWriter fout = new PrintWriter(new FileWriter(neighborsFile));
      exportNeighbors(fout);
      fout.flush();
      fout.close();

      fout = new PrintWriter(new FileWriter(attributesFile));
      exportAttributes(fout);
      fout.flush();
      fout.close();
    }
    catch (Exception err) {
      err.printStackTrace();
      throw new SimpplleError("unable to write to output file");
    }
  }

  private void exportNeighbors(PrintWriter fout) {
    if (hasKeaneAttributes){
      fout.println("BEGIN VEGETATION-VEGETATION-KEANE");
      for (Evu anAllEvu : allEvu) {
        if (anAllEvu == null) {
          continue;
        }
        anAllEvu.exportNeighborsKeane(fout);
      }
      fout.println("END");
      fout.println();
    } else {
      fout.println("BEGIN VEGETATION-VEGETATION");
      for (Evu anAllEvu : allEvu) {
        if (anAllEvu == null) {
          continue;
        }
        anAllEvu.exportNeighbors(fout);
      }
      fout.println("END");
      fout.println();
    }
    if (allEau != null) {
      fout.println("BEGIN AQUATIC-AQUATIC");
      for (ExistingAquaticUnit anAllEau : allEau) {
        if (anAllEau == null) {
          continue;
        }

        anAllEau.exportNeighbors(fout);
      }
      fout.println("END");
      fout.println();
    }

    if (allEau != null) {
      fout.println("BEGIN VEGETATION-AQUATIC");
      for (ExistingAquaticUnit anAllEau : allEau) {
        if (anAllEau == null) {
          continue;
        }

        anAllEau.exportNeighborsVegetation(fout);
      }
      fout.println("END");
      fout.println();
    }

    if (allElu != null) {
      fout.println("BEGIN LANDFORM-LANDFORM");
      for (ExistingLandUnit anAllElu : allElu) {
        if (anAllElu == null) {
          continue;
        }

        anAllElu.exportNeighbors(fout);
      }
      fout.println("END");
      fout.println();
    }

    if (allElu != null) {
      fout.println("BEGIN VEGETATION-LANDFORM");
      for (ExistingLandUnit anAllElu : allElu) {
        if (anAllElu == null) {
          continue;
        }

        anAllElu.exportNeighborsVegetation(fout);
      }
      fout.println("END");
    }

    // *** Roads ***

    if (allRoads != null) {
      fout.println("BEGIN ROADS-ROADS");
      for (Roads allRoad : allRoads) {
        if (allRoad == null) {
          continue;
        }

        allRoad.exportNeighbors(fout);
      }
      fout.println("END");
      fout.println();
    }

    if (allRoads != null) {
      fout.println("BEGIN VEGETATION-ROADS");
      for (Roads allRoad : allRoads) {
        if (allRoad == null) {
          continue;
        }

        allRoad.exportNeighborsVegetation(fout);
      }
      fout.println("END");
    }

    // *** Trails ***

    if (allTrails != null) {
      fout.println("BEGIN TRAILS-TRAILS");
      for (Trails allTrail : allTrails) {
        if (allTrail == null) {
          continue;
        }

        allTrail.exportNeighbors(fout);
      }
      fout.println("END");
      fout.println();
    }

    if (allTrails != null) {
      fout.println("BEGIN VEGETATION-TRAILS");
      for (Trails allTrail : allTrails) {
        if (allTrail == null) {
          continue;
        }

        allTrail.exportNeighborsVegetation(fout);
      }
      fout.println("END");
    }
    // *************
  }
  
  private void exportAttributes(PrintWriter fout) {
    fout.println("BEGIN VEGETATION");
    for(int i=0; i<allEvu.length; i++) {
      if (allEvu[i] == null) { continue; }

      allEvu[i].exportAttributes(fout);
    }
    fout.println("END");
    fout.println();

    if (allEau != null) {
      fout.println("BEGIN AQUATIC");
      for (int i = 0; i < allEau.length; i++) {
        if (allEau[i] == null) {
          continue;
        }

        allEau[i].exportAttributes(fout);
      }
      fout.println("END");
      fout.println();
    }

    if (allElu != null) {
      fout.println("BEGIN LANDFORM");
      for (int i = 0; i < allElu.length; i++) {
        if (allElu[i] == null) {
          continue;
        }

        allElu[i].exportAttributes(fout);
      }
      fout.println("END");
    }

    if (allRoads != null) {
      fout.println("BEGIN ROADS");
      for (int i = 0; i < allRoads.length; i++) {
        if (allRoads[i] == null) {
          continue;
        }

        allRoads[i].exportAttributes(fout);
      }
      fout.println("END");
    }

    if (allTrails != null) {
      fout.println("BEGIN TRAILS");
      for (int i = 0; i < allTrails.length; i++) {
        if (allTrails[i] == null) {
          continue;
        }

        allTrails[i].exportAttributes(fout);
      }
      fout.println("END");
    }
  }

  /**
   * Adds the adjacent data information to a temp data structure until
   * we have all the Evu instances created and can put the data in each
   * Evu.
   * @param evu The instance Evu that has adjId as an adjacent unit.
   * @param adjId The id of the Adjacent unit.
   * @param pos The Position of the Adjacent unit relative to evu.
   * @param wind the direction the wind is coming from in the adjacent unit.
   */
  public void addAdjacentData(Evu evu, int adjId, char pos, char wind) {
    // get vector from hash table
    Vector<double[]> v = addAdjacentHelper(evu);
    // add adjacent data to vector
    double[] data = new double[3];
    data[0] = adjId;
    data[1] = (double) pos;  // convert to ascii value
    data[2] = (double) wind; // convert to ascii value
    v.addElement(data);
  }

  /**
   *  Overloaded.
   * Adds the adjacent data information adjId a temp data structure until
   * we have all the Evu instances created and can put the data in each
   * Evu.
   *
   * @param evu The instance Evu that has 'adjId' as an adjacent unit.
   * @param adjId The id of the adjacent unit.
   * @param pos The Position of the Adjacent unit relative to evu.
   * @param wind 'D' for downwind, 'N' for no wind
   * @param spread the “Degrees Azimuth” between the FROM_POLY and the TO_POLY
   * @param windSpeed Wind Speed
   * @param windDir direction that the wind is coming adjId.
   */
  public void addAdjacentData(Evu evu, int adjId, char pos, char wind, double spread, double windSpeed, double windDir){
    // get adjacency vector adjId hash table
    Vector<double[]> v = addAdjacentHelper(evu);
    // add adjacent data adjId vector
    double[] data = {(double) adjId, pos, wind, spread, windSpeed, windDir};
    v.addElement(data);
  }

  /**
   * Helper method for the overloaded addAdjacentData methods (reduce code duplication).
   * @param evu The instance Evu
   * @return vector for the given evu
   */
  public Vector<double[]> addAdjacentHelper(Evu evu){
    if (tmpAdjacentData == null) {
      tmpAdjacentData = new Hashtable<>();
    }
    Vector v = tmpAdjacentData.get(evu);
    if (v == null) {
      v = new Vector<double[]>();
      tmpAdjacentData.put(evu,v);
    }
    return v;
  }

  /**
   * Go through the temp storage for adjacent data and put the data in the appropriate Evu's. This
   * had to wait until all instances of Evu were created, so that the AdjacentData.evu could be
   * filled in. This method also has the side effect of eliminating any invalid adjacent units.
   */
  public void finishAddingAdjacentData() {
    finishAddingAdjacentData(null);
  }

  /**
   * Go through the temp storage for adjacent data and put the data in the appropriate Evu's. This
   * had to wait until all instances of Evu were created, so that the AdjacentData.evu could be
   * filled in. This method also has the side effect of eliminating any invalid adjacent units.
   */
  public void finishAddingAdjacentData(PrintWriter logFile) {

    for (Evu evu : tmpAdjacentData.keySet()) {

      Vector v = tmpAdjacentData.get(evu);

      // Count the number valid adjacent units.
      int numAdj = 0;
      for (int i = 0; i < v.size(); i++) {
        double[] data = (double[])v.elementAt(i);
        Evu adjEvu = getEvu((int)data[0]);
        if (adjEvu != null) { numAdj++; }
      }

      // if no adjacent units then eliminate this evu.
      if (numAdj == 0) {
        evu.setAdjacentData(null);
        if (logFile != null) {
          logFile.println("Evu-" + evu.getId() +
                          " does not have any valid adjacent units.");
          logFile.println("At least one valid adjacent unit is required.");
          logFile.println("This evu has been deleted");
        }
        allEvu[evu.getId()] = null;
        continue;
      }

      double spread, windSpeed, windDir;
      char pos, wind;
      AdjacentData[] adjData = new AdjacentData[numAdj];
      int adjIndex = 0;
      for (int i = 0; i < v.size(); i++) {
        double[] data = (double[])v.elementAt(i);
        int dataLength = data.length;
        Evu adjEvu = getEvu((int)data[0]);
        // skip null Evus
        if (adjEvu == null) { continue; }

        // Find or calculate position
        if (!evu.isElevationValid() || !adjEvu.isElevationValid()) {
          pos = (char)data[1]; // ascii value back to char
        } else {
          pos = 'E'; // Means use elevation
        }
        wind = (char) data[2]; // ascii value back to char

        if (dataLength < 4) {
          // Legacy spatial relation
          adjData[adjIndex] = new AdjacentData(adjEvu, pos, wind);
        } else {
          // Keane spatial relation, more attributes available
          spread    = data[3];
          windSpeed = data[4];
          windDir   = data[5];
          adjData[adjIndex] = new AdjacentData(adjEvu, pos, wind, spread, windSpeed, windDir);
        }

        adjIndex++;

      }
      evu.setAdjacentData(adjData);
    }
    tmpAdjacentData = null;
  }

  public void calcRelativeSlopes(){
    for (Evu evu : allEvu){
      if (evu != null){
        for (AdjacentData a : evu.getAdjacentData()){
          a.setSlope(calcSlope(evu, a));
        }
      }
    }
  }

  /**
   * @param evu current evu
   * @param adjData data representing the relationship between current and adj
   * @return slope (as a decimal) moving from the current evu to an adjacent
   */
  public double calcSlope(Evu evu, AdjacentData adjData){

    Evu adj = adjData.getEvu();
    // side length of a unit
    double distanceFeet = Math.sqrt(evu.getFloatAcres() * ACRES_TO_FEET);

    // Math class needs radians
    double radianSpread = Math.toRadians(adjData.getSpread());
    // find distance to edge of square based on spread (angle of adjacency)
    double abs_sin = (Math.abs(Math.sin(radianSpread)));
    double abs_cos = (Math.abs(Math.cos(radianSpread)));

    // must use both sin and cos to avoid dividing by 0
    if (abs_sin <= abs_cos) distanceFeet = distanceFeet / abs_cos;
    else                    distanceFeet = distanceFeet / abs_sin;
    // rise over run
    // make sure to use elevation in feet
    return (adj.getElevationFeet() - evu.getElevationFeet()) / distanceFeet;
  }

  public char calcRelativePosition(Evu evu, AdjacentData adjData) {
    Evu adj = adjData.getEvu();

    if (!evu.isElevationValid() || !adj.isElevationValid()) {
      return adjData.getPosition();
    }

    boolean isUniformArea = hasUniformSizePolygons();

    int evuElev = evu.getElevation();
    int adjElev = adj.getElevation();


    int elevDiff = Math.abs(adjElev - evuElev);
    if (isUniformArea) {
      double distance = evu.distanceToEvuMeters(adj);
      double pctDiff = ((double)elevDiff / distance) * 100;
      if (pctDiff > elevationRelativePosition) {
        return (adjElev > evuElev) ? Evu.ABOVE : Evu.BELOW;
      }
    }
    else {
      if (elevDiff > elevationRelativePosition) {
        return (adjElev > evuElev) ? Evu.ABOVE : Evu.BELOW;
      }
    }

    return Evu.NEXT_TO;
  }

  public Vector parseEvuIdList (String value) throws ParseError, IOException {
    StringTokenizer strTok;
    Vector          result = null;
    int             id;
    Evu             evu;
    String          str = null;
    boolean         moreTokens;

    if (value == null) { return null;}

    strTok = new StringTokenizer(value,":");
    if (strTok.hasMoreTokens()) {
      str = strTok.nextToken();
      if (str != null && str.length() == 1 && str.charAt(0) == '?') { str = null; }
    }
    if (str == null) {return null;}

    result = new Vector(strTok.countTokens()+1);

    do {
      try {
        id = Integer.parseInt(str);
      }
      catch (NumberFormatException NFE) {
        throw new ParseError("Invalid number found when reading unit Id's.");
      }
      evu = getEvu(id);
      if (evu != null) { result.addElement(evu); }

      moreTokens = strTok.hasMoreTokens();
      if (moreTokens) { str = strTok.nextToken();}
    }
    while (moreTokens);

    return result;
  }

  public void magisProcessAndTreatmentFiles(File outfile) throws SimpplleError {
    PrintWriter fout;
    File        processFile, treatmentFile;

    processFile   = Utility.makeSuffixedPathname(outfile,"-magisprocess","txt");
    treatmentFile = Utility.makeSuffixedPathname(outfile,"-magistreatment","txt");

    try {
      fout = new PrintWriter(new BufferedWriter(new FileWriter(processFile)));
    }
    catch (IOException err) {
      throw new SimpplleError("Could not open " + processFile + "for writing.");
    }
    magisProcessFile(fout);
    fout.flush();
    fout.close();

    try {
      fout = new PrintWriter(new BufferedWriter(new FileWriter(treatmentFile)));
    }
    catch (IOException err) {
      throw new SimpplleError("Could not open " + treatmentFile + "for writing.");
    }
    magisTreatmentFile(fout);
    fout.flush();
    fout.close();
  }

  private void magisProcessFile(PrintWriter fout) {
    int         ts;
    int         nSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] == null) { continue; }

      fout.print(allEvu[i].getId());
      for (ts=0; ts<=nSteps; ts++) {
        VegSimStateData state = allEvu[i].getState(ts);
        String processStr = (state != null ? state.getProcess().toString() : "NONE");
        fout.print(" " + processStr);
      }
      fout.println();
    }
  }

  private void magisTreatmentFile(PrintWriter fout) {
    int       ts;
    int       nSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();
    Treatment treatment;

    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] == null) { continue; }

      fout.print(allEvu[i].getId());
      for (ts=1; ts<=nSteps; ts++) {
        treatment = allEvu[i].getTreatment(ts,true);
        if (treatment != null) {
          fout.print(" " + treatment.toString());
        }
        else {
          fout.print(" NONE");
        }
      }
      fout.println();
    }
  }


  /**
   * The purpose of this is to search through the units and find
   * those that have attributes that match the parameters to this
   * function.  A parameter value of null indicates that the we don't
   * care what the value for that attribute is.
   *
   * @param htGrp a HabitatTypeGroupType
   * @param speciesd a Species
   * @param sizeClass a SizeClass
   * @param age an int, -1 indicates no preference
   * @param density a Density
   * @param ownership a String, the ownership
   * @param specialArea a String, the Special Area value
   * @param fmz an Fmz, the desired fire management zone
   * @param roadStatus, an int, -1 indicates no preference
   * @return
   */
//  public Vector findUnits(HabitatTypeGroupType htGrp,
//                          int                  tStep,
//                          Species              species,
//                          SizeClass            sizeClass,
//                          int                  age,
//                          Density              density,
//                          String               ownership,
//                          String               specialArea,
//                          Fmz                  fmz,
//                          int                  roadStatus,
//                          ProcessType          processType)
//  {
//    Vector units = new Vector();
//
//    for (int i=0; i<allEvu.length; i++) {
//      if (allEvu[i] == null) { continue; }
//      if (htGrp != null &&
//          (htGrp != HabitatTypeGroupType.get(allEvu[i].getHabitatTypeGroup()))) {
//        continue;
//      }
//
//      VegSimStateData state = allEvu[i].getState(tStep);
//      if (state == null) { continue; }
//      Species   evuSpecies   = state.getVeg().getSpecies();
//      SizeClass evuSizeClass = state.getVeg().getSizeClass();
//      Density   evuDensity   = state.getVeg().getDensity();
//      int       evuAge       = state.getVeg().getAge();
//
//      if (species != null &&
//          (species != evuSpecies)) { continue; }
//
//      if (sizeClass != null &&
//          (sizeClass != evuSizeClass)) { continue; }
//
//      if (age != -1 && (age != evuAge)) { continue; }
//
//      if (density != null &&
//          (density != evuDensity)) { continue; }
//
//      if (ownership != null &&
//          (ownership.equalsIgnoreCase(allEvu[i].getOwnership()) == false)) { continue; }
//
//      if (specialArea != null &&
//          (specialArea.equalsIgnoreCase(allEvu[i].getSpecialArea()) == false)) { continue; }
//
//      if (fmz != null && (fmz != allEvu[i].getFmz())) { continue; }
//
//      if (roadStatus != -1 && (roadStatus != allEvu[i].getRoadStatus())) {
//        continue;
//      }
//
//      if (processType != null && allEvu[i].hasProcessAnyLifeform(tStep,processType) == false) {
//        continue;
//      }
//
//      units.addElement(allEvu[i]);
//    }
//    if (units.size() == 0) { units = null; }
//
//    return units;
//  }

  /**
   * @return True if ten units and their neighbors have the same number of acres
   */
  public boolean hasUniformSizePolygons() {

    int count = 0;

    for (Evu evu : allEvu) {

      if (evu == null) continue;

      if (evu.getAcres() == 0 || !evu.hasSameSizeNeighbors()) {
        return false;
      }

      if (count++ > 10) break;

    }

    return true;

  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    // for future zone restriction if needed.
    String zoneName = (String)in.readObject();

    name = (String)in.readObject();
    date = (String)in.readObject();
    path = (String)in.readObject();

    String kindStr = (String)in.readObject();
    if (kindStr.equals("SIMULATED")) {
      kind = SIMULATED;
    }
    else if (kindStr.equals("SAMPLE")) {
      kind = SAMPLE;
    }
    else {
      kind = USER;
    }


    acres       = in.readInt();
    totalLength = in.readInt();

    Evu[] units = (Evu[])in.readObject();
    maxEvuId = units.length - 1;
    if (maxEvuId > 15000) { manualGC = true; }

    setAllEvu(units);


    int count = in.readInt();
    Evu evu;
    for (int i=0; i<count; i++) {
      evu = this.getEvu(in.readInt());
      if (evu == null) { continue; }
      evu.readExternalAdjacentData(in,this);
    }

    initPolygonWidth();

    allOwnership   = (Vector)in.readObject();
    allSpecialArea = (Vector)in.readObject();

    Simpplle.setCurrentArea(this);
    if (version >= 2) {
      ExistingAquaticUnit[] aquaUnits = (ExistingAquaticUnit[])in.readObject();
      setAllEau(aquaUnits);

      if (aquaUnits != null) {
        count = in.readInt();
        ExistingAquaticUnit eau;
        for (int i = 0; i < count; i++) {
          eau = this.getEau(in.readInt());
          if (eau == null) { continue; }
          eau.readExternalNeighbors(in, this);
        }
      }
    }

    if (version >= 4) {
      int size = in.readInt();
      setAllElu(new ExistingLandUnit[size]);
      ExistingLandUnit[] elus = (ExistingLandUnit[])in.readObject();
      addAllElu(elus);
    }

    if (version >= 5) {
      {
        int size = in.readInt();
        setAllRoads(new Roads[size]);
        Roads[] roads = (Roads[]) in.readObject();
        addAllRoads(roads);
      }

      {
        int size = in.readInt();
        setAllTrails(new Trails[size]);
        Trails[] trails = (Trails[]) in.readObject();
        addAllTrails(trails);
      }
    }
    // Had to put this in to insure future compatibility with version 3.0
    // Had to add elevationRelativePosition in version 3.0 which to file
    // to version 7, (6 added climate data).
    if (version >= 6) {
      @SuppressWarnings("unused")
      int size = in.readInt();
    }

    if (version >= 3) {
      int size = in.readInt();
      for (int i=0; i<size; i++) {
        evu = getEvu(in.readInt());
        evu.readSpatialRelations(in,this,version);
      }
    }
    if (version > 6) {
      elevationRelativePosition = in.readInt();
    }
    else {
      elevationRelativePosition = hasUniformSizePolygons() ? 10 : 100;
    }

    if (version >= 9){
      /**
       * Keane attributes are read in the Evu.readExternalAdjacentData method.
       */
      hasKeaneAttributes = in.readBoolean();
    }
  }
  public void initPolygonWidth() {
    if (hasUniformSizePolygons()) {
      for (int i=0; i<allEvu.length; i++) {
        if (allEvu[i] != null) {
          polygonWidth = (int)Math.sqrt(allEvu[i].getFloatAcres() * ACRES_TO_FEET);
          break;
        }
      }
    }
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeObject(Simpplle.getCurrentZone().getName());
    out.writeObject(name);
    out.writeObject(date);
    out.writeObject(path);

    switch (kind) {
      case SIMULATED: out.writeObject("SIMULATED"); break;
      case SAMPLE:    out.writeObject("SAMPLE"); break;
      case USER:
      default:        out.writeObject("USER"); break;
    }
    out.writeInt(acres);
    out.writeInt(totalLength);
    out.writeObject(allEvu);

    int evuValidCount = 0;
    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] != null) { evuValidCount++; }
    }

    out.writeInt(evuValidCount);
    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] == null) { continue; }
      out.writeInt(allEvu[i].getId());
      allEvu[i].writeExternalAdjacentData(out);
    }
    out.writeObject(allOwnership);
    out.writeObject(allSpecialArea);

    out.writeObject(allEau);
    int validCount = 0;
    if (allEau != null) {
      validCount = 0;
      for (int i = 0; i < allEau.length; i++) {
        if (allEau[i] != null) {
          validCount++;
        }
      }

      out.writeInt(validCount);
      for (int i = 0; i < allEau.length; i++) {
        if (allEau[i] == null) {
          continue;
        }
        out.writeInt(allEau[i].getId());
        allEau[i].writeExternalNeighbors(out);
      }
    }

    {
      int size = allElu != null ? allElu.length : 0;
      out.writeInt(size);
      out.writeObject(allElu);
    }

    {
      int size = allRoads != null ? allRoads.length : 0;
      out.writeInt(size);
      out.writeObject(allRoads);
    }

    {
      int size = allTrails != null ? allTrails.length : 0;
      out.writeInt(size);
      out.writeObject(allTrails);
    }

    {
      out.writeInt(0); // Dummy for climate data
    }

    out.writeInt(evuValidCount);
    for (Evu evu : allEvu) {
      if (evu == null) { continue; }
      out.writeInt(evu.getId());
      evu.writeSpatialRelations(out);
    }
    out.writeInt(elevationRelativePosition);
    out.writeBoolean(hasKeaneAttributes);
  }

  public void readExternalSimData(ObjectInput in, int run) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    {
      int count = in.readInt();

      boolean moreData = true;

      Evu evu;
      int id;
      int i = 0;
      // I changed this to a while loop, that looks for an id of -1, so that
      // I could convert old format data files to the new format.  Currently,
      // such functionality is not implemented, but may be in the future.
      while (moreData) {
        id = in.readInt();
        if (id == -1) {
          break;
        }

        evu = getEvu(id);
        if (evu == null) {
          continue;
        }
        evu.readExternalSimData(in, run);
        i++;
        moreData = ((count == -1) || (i < count));
      }
    }

    if (version <= 6) { return; }

    {
      int count = in.readInt();

      boolean moreData = (count > 0);

      Roads road;
      int i = 0;
      while (moreData) {
        road = getRoadUnit(in.readInt());
        if (road == null) { continue; }

        road.readExternalSimData(in);
        i++;
        moreData = ((count == -1) || (i < count));
      }
    }

    {
      int count = in.readInt();

      boolean moreData = (count > 0);

      Trails trail;
      int i = 0;
      while (moreData) {
        trail = getTrailUnit(in.readInt());
        if (trail == null) { continue; }

        trail.readExternalSimData(in);
        i++;
        moreData = ((count == -1) || (i < count));
      }
    }

  }
  public void writeExternalSimData(ObjectOutputStream out) throws IOException {
    out.writeInt(simDataVersion);

    {
      int validCount = 0;
      for (int i = 0; i < allEvu.length; i++) {
        if (allEvu[i] != null) {
          validCount++;
        }
      }
      out.writeInt(validCount);

      for (int i = 0; i < allEvu.length; i++) {
        if (allEvu[i] == null) {
          continue;
        }
        out.writeInt(allEvu[i].getId());
        allEvu[i].writeExternalSimData(out);
      }
    }

    {
      int validCount = 0;
      int size = hasRoads() ? allRoads.length : 0;
      for (int i = 0; i < size; i++) {
        if (allRoads[i] != null) {
          validCount++;
        }
      }
      out.writeInt(validCount);

      for (int i = 0; i < size; i++) {
        if (allRoads[i] == null) {
          continue;
        }
        out.writeInt(allRoads[i].getId());
        allRoads[i].writeExternalSimData(out);
      }
    }

    {
      int validCount = 0;
      int size = hasTrails() ? allTrails.length : 0;
      for (int i = 0; i < size; i++) {
        if (allTrails[i] != null) {
          validCount++;
        }
      }
      out.writeInt(validCount);

      for (int i = 0; i < size; i++) {
        if (allTrails[i] == null) {
          continue;
        }
        out.writeInt(allTrails[i].getId());
        allTrails[i].writeExternalSimData(out);
      }
    }

  }

  public void writeAccumDatabase() throws SimpplleError {
    try {
      int    doneCount=0, pctFinish;
      for (Evu evu : allEvu) {
        if (evu == null) {
          continue;
        }
        pctFinish = Math.round(((float)doneCount / (float)allEvu.length) * 100.0f);
        String msg = "Writing to database " + pctFinish + "% Finished";
        Simpplle.setStatusMessage(msg);

        Session session = DatabaseCreator.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        evu.writeAccumDatabase(session);
        tx.commit();
        session.close();

        doneCount++;
      }
      if (allEau == null) { return; }

      for (ExistingAquaticUnit eau : allEau) {
        if (eau == null) {
          continue;
        }

        Session session = DatabaseCreator.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        eau.writeAccumDatabase(session);
        tx.commit();
        session.close();
      }
    }
    catch (HibernateException ex) {
      throw new SimpplleError("Problems writing database",ex);
    }
    catch (SQLException ex) {
      throw new SimpplleError("Problems writing database",ex);
    }
  }

  public void writeRandomAccessFile(RandomAccessFile simFile)
    throws SimpplleError
  {
    int doneCount = 0, pctFinish;

    int ts = Simulation.getCurrentTimeStep();
    for (Evu evu : allEvu) {
      if (evu == null) {
        continue;
      }
      pctFinish = Math.round(((float) doneCount / (float) allEvu.length)*100.0f);
      if (pctFinish % 10 == 0) {
        String msg = "Writing Time Step #" + ts + " to data file " + pctFinish + "% Finished";
        Simpplle.setStatusMessage(msg);
      }
      try {
        evu.writeRandomAccessFile(simFile);
        doneCount++;
      }
      catch (SimpplleError ex) {
        Simpplle.clearStatusMessage();
        throw ex;
      }
    }
    Simpplle.clearStatusMessage();
  }


  /**
   * Write simulation data for the current time step to the database.
   * @throws SimpplleError
   */
  public void writeSimulationDatabase() throws SimpplleError {
    int doneCount = 0, pctFinish;

    Session     session = DatabaseCreator.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    int ts = Simulation.getCurrentTimeStep();
    try {
      for (Evu evu : allEvu) {
        if (evu == null) {
          continue;
        }
        pctFinish = Math.round(((float) doneCount / (float) allEvu.length) *
                               100.0f);
        if (pctFinish % 5 == 0) {
          String msg = "Writing Time Step #" + ts + " to database " + pctFinish +
                       "% Finished";
          Simpplle.setStatusMessage(msg);
        }
        evu.writeSimulationDatabase(session);

        doneCount++;
      }
    }
    catch (SimpplleError err) {
      throw err;
    }
    finally {
      tx.commit();
      session.close();
      Simpplle.clearStatusMessage();
    }
  }
  public void writeSimulationAccessFiles(PrintWriter fout, PrintWriter trackOut) throws SimpplleError {
    int doneCount = 0, pctFinish;

    int ts = Simulation.getCurrentTimeStep();

    String msg = "Writing Time Step #" + ts + " to textdata files.";
    Simpplle.setStatusMessage(msg);

    for (Evu evu : allEvu) {
      if (evu == null) {
        continue;
      }
//      pctFinish = Math.round(((float) doneCount / (float) allEvu.length) *
//          100.0f);
//      if (pctFinish % 10 == 0) {
//        String msg = "Writing Time Step #" + ts + " to textdata " + pctFinish +
//        "% Finished";
//        Simpplle.setStatusMessage(msg);
//      }
      evu.writeSimulationAccessFiles(fout,trackOut);

      doneCount++;
    }
  }

    public void setMultipleLifeformStatus() {
        for (int i=0; i<allEvu.length; i++) {
            if (allEvu[i] != null && allEvu[i].hasMultipleLifeforms()) {
                disableMultipleLifeforms = false;
                return;
            }
        }
        disableMultipleLifeforms = true;
    }

  public void makeMultipleLifeforms() {
    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] != null) {
        allEvu[i].makeMultipleLife();
      }
    }
    disableMultipleLifeforms = false;
  }
  public void validateLifeformStorageMatch() {
    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] == null) { continue; }

      Lifeform[] lives = Lifeform.getAllValues();
      for (int j = 0; j < lives.length; j++) {
        VegSimStateData state = allEvu[i].getState(0,lives[j]);
        if (state == null) { continue; }

        Lifeform stateSpeciesLife = state.getVeg().getSpecies().getLifeform();
        Lifeform stateLife = state.getLifeform();

        if (stateLife == lives[j] && stateSpeciesLife == lives[j] && stateLife == stateSpeciesLife) {
          continue;
        }
        System.out.println("Evu ID=" + allEvu[i].getId() + " has mismatch in lifeform data storage");
      }
    }
  }

  /**
   * Gets the elevation relative position.
   */
  public int getElevationRelativePosition() {
    return elevationRelativePosition;
  }

  public void setElevationRelativePosition(int elevationRelativePosition) {
    this.elevationRelativePosition = elevationRelativePosition;
  }

  /**
   * If Area has uniform polygons sets the relative elevation position to 10,
   * otherwise it sets to 100
   */
  public void setElevationRelativePositionDefault() {
    if (hasUniformSizePolygons()) {
      setElevationRelativePosition(10);
    } else {
      setElevationRelativePosition(100);
    }
  }

  public boolean getHasKeaneAttributes() {
    return hasKeaneAttributes;
  }

  public void setHasKeaneAttributes(boolean hasKeaneAttributes) {
    this.hasKeaneAttributes = hasKeaneAttributes;
  }
}


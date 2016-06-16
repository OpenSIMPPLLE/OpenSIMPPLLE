package simpplle.comcode;

import java.io.*;
import java.util.HashMap;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Size Class, a Simpplle Type.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */
public class SizeClass extends SimpplleType implements Externalizable {
  static final long serialVersionUID = -2448009931932921976L;;
  static final int  version          = 1;

  public static final int COLUMN_COUNT = 2;

  public static final int CODE_COL          = 0;
  public static final int STRUCTURE_COL     = 1;

  public enum Structure { NON_FOREST, SINGLE_STORY, MULTIPLE_STORY;
    public static Structure lookupOrdinal(int ordinal) {
      if (NON_FOREST.ordinal() == ordinal) { return NON_FOREST; }
      else if (SINGLE_STORY.ordinal() == ordinal) { return SINGLE_STORY; }
      else if (MULTIPLE_STORY.ordinal() == ordinal) { return MULTIPLE_STORY; }
      else { return null; }
    }
  };


  public static final Structure NON_FOREST = Structure.NON_FOREST;
  public static final Structure SINGLE_STORY   = Structure.SINGLE_STORY;
  public static final Structure MULTIPLE_STORY = Structure.MULTIPLE_STORY;

  private String    sizeClass;
  private Structure structure;

  public static HashMap<Short,SizeClass> simIdHm = new HashMap<Short,SizeClass>();
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
  public void setSimId(short id) {}

  public static SizeClass lookUpSizeClass(short simId) { return simIdHm.get(simId); }
  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      SizeClass sizeClass = (SizeClass)readExternalSimple(in,SimpplleType.SIZE_CLASS);
      simIdHm.put(id,sizeClass);
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
      SizeClass sizeClass = simIdHm.get(id);
      sizeClass.writeExternalSimple(out);
    }
  }


  public static final SizeClass UNKNOWN = new SizeClass("UNKNOWN");

  // ************************************
  // *** Common to more than one zone ***
  // ************************************
  public static final SizeClass AGR               = new SizeClass("AGR",NON_FOREST,true);
  public static final SizeClass CLOSED_LOW_SHRUB  = new SizeClass("CLOSED-LOW-SHRUB",true);
  public static final SizeClass CLOSED_MID_SHRUB  = new SizeClass("CLOSED-MID-SHRUB",true);
  public static final SizeClass CLOSED_TALL_SHRUB = new SizeClass("CLOSED-TALL-SHRUB",true);
  public static final SizeClass CLUMPED           = new SizeClass("CLUMPED",NON_FOREST,true);
  public static final SizeClass DEAD              = new SizeClass("DEAD",true);
  public static final SizeClass LARGE             = new SizeClass("LARGE",SINGLE_STORY,true);
  public static final SizeClass LMU               = new SizeClass("LMU",MULTIPLE_STORY,true);
  public static final SizeClass LTS               = new SizeClass("LTS",MULTIPLE_STORY,true);
  public static final SizeClass MEDIUM            = new SizeClass("MEDIUM",SINGLE_STORY,true);
  public static final SizeClass MMU               = new SizeClass("MMU",MULTIPLE_STORY,true);
  public static final SizeClass MTS               = new SizeClass("MTS",MULTIPLE_STORY,true);
  public static final SizeClass ND                = new SizeClass("ND",NON_FOREST,true);
  public static final SizeClass NF                = new SizeClass("NF",NON_FOREST,true);
  public static final SizeClass NS                = new SizeClass("NS",NON_FOREST,true);
  public static final SizeClass OPEN_LOW_SHRUB    = new SizeClass("OPEN-LOW-SHRUB",NON_FOREST,true);
  public static final SizeClass OPEN_MID_SHRUB    = new SizeClass("OPEN-MID-SHRUB",NON_FOREST,true);
  public static final SizeClass OPEN_TALL_SHRUB   = new SizeClass("OPEN-TALL-SHRUB",NON_FOREST,true);
  public static final SizeClass PMU               = new SizeClass("PMU",MULTIPLE_STORY,true);
  public static final SizeClass POLE              = new SizeClass("POLE",SINGLE_STORY,true);
  public static final SizeClass PTS               = new SizeClass("PTS",MULTIPLE_STORY,true);
  public static final SizeClass SS                = new SizeClass("SS",SINGLE_STORY,true);
  public static final SizeClass UNIFORM           = new SizeClass("UNIFORM",true);
  public static final SizeClass VERY_LARGE        = new SizeClass("VERY-LARGE",SINGLE_STORY,true);
  public static final SizeClass VLMU              = new SizeClass("VLMU",MULTIPLE_STORY,true);
  public static final SizeClass VLTS              = new SizeClass("VLTS",MULTIPLE_STORY,true);
  public static final SizeClass WATER             = new SizeClass("WATER",NON_FOREST,true);

  // ***********************************
  // *** Eastside and Westside Zones ***
  // ***********************************
  public static final SizeClass CLOSED_HERB       = new SizeClass("CLOSED-HERB",NON_FOREST,true);
  public static final SizeClass OPEN_HERB         = new SizeClass("OPEN-HERB",NON_FOREST,true);

  // ***************************
  // *** Westside Region One ***
  // ***************************
  public static final SizeClass WOODLAND          = new SizeClass("WOODLAND",true);

  // ***************************
  // *** Eastside Region One ***
  // ***************************
  public static final SizeClass SCATTERED         = new SizeClass("SCATTERED",NON_FOREST,true);


  // ***************************************************
  // *** Sierra Nevada and Southern California Zones ***
  // ***************************************************

  // ********************************
  // *** Southern California Zone ***
  // ********************************
  public static final SizeClass BURNED_URBAN = new SizeClass("BURNED-URBAN",true);
  public static final SizeClass URBAN        = new SizeClass("URBAN",NON_FOREST,true);

  // *****************
  // *** Gila Zone ***
  // *****************
  public static final SizeClass GRA        = new SizeClass("GRA",NON_FOREST,true); // Grass
  public static final SizeClass SHR        = new SizeClass("SHR",NON_FOREST,true); // Shrub

  // *********************************
  // *** South Central Alaska Zone ***
  // *********************************
  public static final SizeClass SS_LARGE          = new SizeClass("SS-LARGE",true);
  public static final SizeClass SS_SS             = new SizeClass("SS-SS",true);
  public static final SizeClass SS_POLE           = new SizeClass("SS-POLE",true);
  public static final SizeClass SS_LARGE_POLE     = new SizeClass("SS-LARGE-POLE",true);
  public static final SizeClass SS_POLE_LARGE     = new SizeClass("SS-POLE-LARGE",true);
  public static final SizeClass SS_POLE_POLE      = new SizeClass("SS-POLE-POLE",true);
  public static final SizeClass SS_LARGE_LARGE    = new SizeClass("SS-LARGE-LARGE",true);
  public static final SizeClass SS_LARGE_SS       = new SizeClass("SS-LARGE-SS",true);
  public static final SizeClass SS_POLE_SS        = new SizeClass("SS-POLE-SS",true);
  public static final SizeClass SS_SS_POLE        = new SizeClass("SS-SS-POLE",true);
  public static final SizeClass SS_SS_LARGE       = new SizeClass("SS-SS-LARGE",true);
  public static final SizeClass SS_SS_SS          = new SizeClass("SS-SS-SS",true);
  public static final SizeClass POLE_POLE         = new SizeClass("POLE-POLE",true);
  public static final SizeClass POLE_SS           = new SizeClass("POLE-SS",true);
  public static final SizeClass POLE_LARGE        = new SizeClass("POLE-LARGE",true);
  public static final SizeClass POLE_SS_LARGE     = new SizeClass("POLE-SS-LARGE",true);
  public static final SizeClass POLE_LARGE_SS     = new SizeClass("POLE-LARGE-SS",true);
  public static final SizeClass POLE_POLE_LARGE   = new SizeClass("POLE-POLE-LARGE",true);
  public static final SizeClass POLE_POLE_SS      = new SizeClass("POLE-POLE-SS",true);
  public static final SizeClass POLE_SS_SS        = new SizeClass("POLE-SS-SS",true);
  public static final SizeClass POLE_LARGE_LARGE  = new SizeClass("POLE-LARGE-LARGE",true);
  public static final SizeClass POLE_SS_POLE      = new SizeClass("POLE-SS-POLE",true);
  public static final SizeClass POLE_LARGE_POLE   = new SizeClass("POLE-LARGE-POLE",true);
  public static final SizeClass POLE_POLE_POLE    = new SizeClass("POLE-POLE-POLE",true);
  public static final SizeClass LARGE_LARGE       = new SizeClass("LARGE-LARGE",true);
  public static final SizeClass LARGE_POLE        = new SizeClass("LARGE-POLE",true);
  public static final SizeClass LARGE_SS          = new SizeClass("LARGE-SS",true);
  public static final SizeClass LARGE_SS_POLE     = new SizeClass("LARGE-SS-POLE",true);
  public static final SizeClass LARGE_POLE_SS     = new SizeClass("LARGE-POLE-SS",true);
  public static final SizeClass LARGE_SS_SS       = new SizeClass("LARGE-SS-SS",true);
  public static final SizeClass LARGE_POLE_POLE   = new SizeClass("LARGE-POLE-POLE",true);
  public static final SizeClass LARGE_LARGE_SS    = new SizeClass("LARGE-LARGE-SS",true);
  public static final SizeClass LARGE_LARGE_POLE  = new SizeClass("LARGE-LARGE-POLE",true);
  public static final SizeClass LARGE_POLE_LARGE  = new SizeClass("LARGE-POLE-LARGE",true);
  public static final SizeClass LARGE_SS_LARGE    = new SizeClass("LARGE-SS-LARGE",true);
  public static final SizeClass LARGE_LARGE_LARGE = new SizeClass("LARGE-LARGE-LARGE",true);
  public static final SizeClass TALL_SHRUB        = new SizeClass("TALL-SHRUB",true);
  public static final SizeClass LOW_SHRUB         = new SizeClass("LOW-SHRUB",true);
  public static final SizeClass DWARF_SHRUB       = new SizeClass("DWARF-SHRUB",true);
  public static final SizeClass HERB              = new SizeClass("HERB",true);
  public static final SizeClass GH                = new SizeClass("GH",true);
  public static final SizeClass AQU               = new SizeClass("AQU",true);
  public static final SizeClass OCEAN             = new SizeClass("OCEAN",true);
  public static final SizeClass ALP               = new SizeClass("ALP",true);
  public static final SizeClass MSH               = new SizeClass("MSH",true);

  // **********************
  // *** Southwest Utah ***
  // **********************

  public static final SizeClass LS        = new SizeClass("LS",NON_FOREST,true);
  public static final SizeClass MS        = new SizeClass("MS",NON_FOREST,true);
  public static final SizeClass TS        = new SizeClass("TS",NON_FOREST,true);
  public static final SizeClass BARREN    = new SizeClass("BARREN",true);
  public static final SizeClass GF        = new SizeClass("GF",NON_FOREST,true);

  public static final SizeClass RIPARIAN  = new SizeClass("RIPARIAN",NON_FOREST,true);
  public static final SizeClass ALPINE    = new SizeClass("ALPINE",true);
  public static final SizeClass ROCK_BARE = new SizeClass("ROCK-BARE",NON_FOREST,true);
  public static final SizeClass AGR_URB   = new SizeClass("AGR-URB",NON_FOREST,true);

  // Others
  public static final SizeClass GRASS = new SizeClass("GRASS",NON_FOREST,true);
  public static final SizeClass MU    = new SizeClass("MU",true);

  // *** Colorado Front Range ***
  // ****************************

  public static final SizeClass BA          = new SizeClass("BA",NON_FOREST,true);
  public static final SizeClass MEDIUM_SH   = new SizeClass("MEDIUM-SH",NON_FOREST,true);
  public static final SizeClass LARGE_SH    = new SizeClass("LARGE-SH",NON_FOREST,true);
  public static final SizeClass SMALL_SH    = new SizeClass("SMALL-SH",NON_FOREST,true);
  public static final SizeClass E           = new SizeClass("E",SINGLE_STORY,true);
  public static final SizeClass GRA_SHR    = new SizeClass("GRA-SHR",false);
  public static final SizeClass AGR_URBAN  = new SizeClass("AGR-URBAN",false);


  // *** Western Great Plains Steppe ***
  // ***********************************
  public static final SizeClass NA = new SizeClass("NA",NON_FOREST,true);
/**
 * Constructor for size class.  Sets the size class to null, and structure to non forest. 
 */
  public SizeClass() {
    sizeClass = null;
    structure = NON_FOREST;
  }
  /**
   * Overloaded size class constructor.  Creates a size class object by referencing default constructor and initializing the string name, structure, and 
   * valid variable.  Then updates the size class arraylist, and the all types hashmap.  
   */
  public SizeClass(String sizeClass, Structure structure, boolean isValid) {
    this.sizeClass = sizeClass.toUpperCase();
    this.structure = structure;

    updateAllData(this,SIZE_CLASS);
  }
  /**
   * Overloaded constructor for size class.  References default constructor and passes in Non_forest for size class.  
   * @param sizeClass
   * @param isValid
   */
  public SizeClass(String sizeClass, boolean isValid) {
    this(sizeClass,NON_FOREST,isValid);
  }
/**
 * This overloaded constructor passes false for is valid to size class. 
 * @param sizeClass
 */
  public SizeClass(String sizeClass) {
    this(sizeClass,false);
  }
/**
 * toString of this sizeClass.  this is as string.  A list of these are in the static variables in SizeClass.java
 */
  public String toString() { return sizeClass; }
/**
 * Gets the structure of this size class.  Choices are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY
 * @return structure of this size class.  
 */
  public Structure getStructure() { return structure; }
/**
 * Gets the current size class object.
 * @return this size class 
 */
  public String getSizeClass() {
    return sizeClass;
  }
/**
 * Calculates count of size classes by getting size of size class hashmap
 * @return size of size class hashmap which represents a counting
 */
  public static int count() { return allSizeClassHm.size(); }

  /**
   * Checks to see if the size class passed in parameter equals the current size class based on the size class name
   * @param name used to get the size class object to compare to this size class object
   * @return true named size class equals this size class
   */
  public boolean lookupEquals(String name) {
    return equals(get(name));
  }
/**
 * Method to check if object in parameter is a size class, and if it is equal to this size class. 
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof SizeClass) {
      if (sizeClass == null || obj == null) { return false; }

      return sizeClass.equals(((SizeClass)obj).sizeClass);
    }
    return false;
  }
/**
 * Gets the hash code for this size class object.
 */
  public int hashCode() {
    return sizeClass.hashCode();
  }
/**
 * Comparable method implementation.  Compares the object parameters to string (which will be size class name) to the this size class 
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return sizeClass.compareTo(o.toString());
  }

  public boolean isValid() { return SizeClass.get(sizeClass) != null; }
/**
 * Gets the size class based on string name.  A new size class will not be created if the size class does not exist in hash map
 * @param sizeClassStr  the name of size class
 * @return size class
 */
  public static SizeClass get(String sizeClassStr) {
    return get(sizeClassStr,false);
  }
  /**
   * Gets the size class if one exists.  If a size class does not exist and the create boolean is true, a new size class is created. 
   * @param sizeClassStr string version of size class
   * @param create true if a new size class should be created.  
   * @return either the size class from hash map, or newly created size class
   */
  public static SizeClass get(String sizeClassStr, boolean create) {
    SizeClass sizeClass = (SizeClass)allSizeClassHm.get(sizeClassStr.toUpperCase());

    if (sizeClass == null && create) {
      sizeClass = new SizeClass(sizeClassStr.toUpperCase(),true);
    }
    return sizeClass;
  }
/**
 * Checks if the size class is not forested.  This is true if UNIFORM, CLUMPED, SCATTERED, OPEN_HERB, CLOSED_HERB, OPEN_LOW_SHRUB,
 * CLOSED_LOW_SHRUB, OPEN_MID_SHRUB, CLOSED_MID_SHRUB, OPEN_TALL_SHRUB, CLOSED_TALL_SHRUB, GRASS, BURNED_URBAN, URBAN, WATER, or AGR
 * Basically if it is a size class that corresponds to shrubs, herbacious, burned, urban, agriculture or water it will not be forested.  
 * Which makes sense.  
 * @param sizeClass the size class being evaluated
 * @return true if not a forested size class
 */
  public static boolean isNonForested(SizeClass sizeClass) {
    return (sizeClass == UNIFORM           || sizeClass == CLUMPED         ||
            sizeClass == SCATTERED         || sizeClass == OPEN_HERB       ||
            sizeClass == CLOSED_HERB       || sizeClass == OPEN_LOW_SHRUB  ||
            sizeClass == CLOSED_LOW_SHRUB  || sizeClass == OPEN_MID_SHRUB  ||
            sizeClass == CLOSED_MID_SHRUB  || sizeClass == OPEN_TALL_SHRUB ||
            sizeClass == CLOSED_TALL_SHRUB || sizeClass == GRASS           ||
            sizeClass == BURNED_URBAN      || sizeClass == URBAN           ||
            sizeClass == WATER             || sizeClass == AGR);
  }
/**
 * Checks if the current size class is a forested size class. 
 * @param sizeClass size class being evaluated
 * @return true if not non forested meaning this is a forested size class
 */
  public static boolean isForested(SizeClass sizeClass) {
    return (!isNonForested(sizeClass));
  }
/**
 * Gets the base size class.  
 * @return choices for base size class are POLE (PTS, PMU, OR POLE), MEDIUM (MTS, MMU, OR MEDIUM), LARGE(LTS, LMU, OR LARGE), 
 * VERY LARGE(VLTS, VLMU, OR VERY_LARGE), or the current size class
 */
  public SizeClass getBase() {
    if (this == PTS || this == PMU || this == POLE) {
      return POLE;
    }
    else if (this == MTS || this == MMU || this == MEDIUM) {
      return MEDIUM;
    }
    else if (this == LTS || this == LMU || this == LARGE) {
      return LARGE;
    }
    else if (this == VLTS || this == VLMU || this == VERY_LARGE) {
      return VERY_LARGE;
    }
    else { return this; }
  }
/**
 * Reads from external source the size class and size class structure.  Choices for the former can be found at the top of 
 * SizeClass.java, the choices for the latter are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    sizeClass           = (String)in.readObject();
    String structStr = (String)in.readObject();

    if (structStr.equals("NON-FOREST")) {
      structure = NON_FOREST;
    }
    else if (structStr.equals("MULTIPLE-STORY")) {
      structure = MULTIPLE_STORY;
    }
    else if (structStr.equals("SINGLE-STORY")) {
      structure = SINGLE_STORY;
    }
    else {
      structure = NON_FOREST;
    }

  }
  /**
   * Writes to external location the size class structure. The choices for these are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY 
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(sizeClass);
    if (structure == NON_FOREST) {
      out.writeObject("NON-FOREST");
    }
    else if (structure == MULTIPLE_STORY) {
      out.writeObject("MULTIPLE-STORY");
    }
    else if (structure == SINGLE_STORY) {
      out.writeObject("SINGLE-STORY");
    }
    else {
      out.writeObject("NON-FOREST");
    }
  }
  /**
   * Reads teh size class object and sets the size class object to the values.  The two variables read in are size class and structure.  
   * @return
   * @throws java.io.ObjectStreamException
   */
  private Object readResolve () throws java.io.ObjectStreamException
  {
    SizeClass sizeClassObj = SizeClass.get(sizeClass,true);

    sizeClassObj.sizeClass = this.sizeClass;
    sizeClassObj.structure = this.structure;

    updateAllData(sizeClassObj,SIZE_CLASS);
    return sizeClassObj;
  }
/**
 * Sets the size class to the parameter string of the size class.  A list of size classes can be found at top of SizeClass.java.
 * @param sizeClass
 */
  public void setSizeClass(String sizeClass) {
    this.sizeClass = sizeClass;
  }

  // *** JTable section ***
  // **********************
/**
 * Gets the size class data based on column ID.  These are 0 - size class data, 1 size class structure data
 */
  public Object getColumnData(int col) {
    switch (col) {
      case CODE_COL:
        return this;
      case STRUCTURE_COL:
        return getStructure();
      default: return null;
    }
  }
  /**
   * Sets the column data if the parameter column id is Structure column (1)
   */
  public void setColumnData(Object value, int col) {
    switch (col) {
      case STRUCTURE_COL:
        structure = (Structure)value;
        break;
      default: return;
    }
//    SystemKnowledge.markChanged(SystemKnowledge.SIZE_CLASS);
  }
/**
 * Gets the column name based on column ID.  This will either be Size Class, Structure, or empty string. This is called from the GUI table model.  
 * @param col column id
 * @return string name of column
 */
  public static String getColumnName(int col) {
    switch (col) {
      case CODE_COL:            return "Size Class";
      case STRUCTURE_COL:     return "Structure";
      default: return "";
    }
  }

}




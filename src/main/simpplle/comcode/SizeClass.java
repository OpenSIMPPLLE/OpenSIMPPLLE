/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.HashMap;

/**
 * A vegetation size classification used in vegetative pathways.
 */

public class SizeClass extends SimpplleType implements Externalizable {

  static final long serialVersionUID = -2448009931932921976L;
  static final int  version          = 1;

  public static final int CODE_COL      = 0;
  public static final int STRUCTURE_COL = 1;
  public static final int COLUMN_COUNT  = 2;

  public static HashMap<Short, SizeClass> simIdHm = new HashMap<>();
  public static short nextSimId = 0;

  // ************************************
  // *** Common to more than one zone ***
  // ************************************

  public static final SizeClass AGR               = new SizeClass("AGR", Structure.NON_FOREST);
  public static final SizeClass CLOSED_LOW_SHRUB  = new SizeClass("CLOSED-LOW-SHRUB");
  public static final SizeClass CLOSED_MID_SHRUB  = new SizeClass("CLOSED-MID-SHRUB");
  public static final SizeClass CLOSED_TALL_SHRUB = new SizeClass("CLOSED-TALL-SHRUB");
  public static final SizeClass CLUMPED           = new SizeClass("CLUMPED", Structure.NON_FOREST);
  public static final SizeClass DEAD              = new SizeClass("DEAD");
  public static final SizeClass LARGE             = new SizeClass("LARGE", Structure.SINGLE_STORY);
  public static final SizeClass LMU               = new SizeClass("LMU", Structure.MULTIPLE_STORY);
  public static final SizeClass LTS               = new SizeClass("LTS", Structure.MULTIPLE_STORY);
  public static final SizeClass MEDIUM            = new SizeClass("MEDIUM", Structure.SINGLE_STORY);
  public static final SizeClass MMU               = new SizeClass("MMU", Structure.MULTIPLE_STORY);
  public static final SizeClass MTS               = new SizeClass("MTS", Structure.MULTIPLE_STORY);
  public static final SizeClass ND                = new SizeClass("ND", Structure.NON_FOREST);
  public static final SizeClass NF                = new SizeClass("NF", Structure.NON_FOREST);
  public static final SizeClass NS                = new SizeClass("NS", Structure.NON_FOREST);
  public static final SizeClass OPEN_LOW_SHRUB    = new SizeClass("OPEN-LOW-SHRUB", Structure.NON_FOREST);
  public static final SizeClass OPEN_MID_SHRUB    = new SizeClass("OPEN-MID-SHRUB", Structure.NON_FOREST);
  public static final SizeClass OPEN_TALL_SHRUB   = new SizeClass("OPEN-TALL-SHRUB", Structure.NON_FOREST);
  public static final SizeClass PMU               = new SizeClass("PMU", Structure.MULTIPLE_STORY);
  public static final SizeClass POLE              = new SizeClass("POLE", Structure.SINGLE_STORY);
  public static final SizeClass PTS               = new SizeClass("PTS", Structure.MULTIPLE_STORY);
  public static final SizeClass SS                = new SizeClass("SS", Structure.SINGLE_STORY);
  public static final SizeClass UNIFORM           = new SizeClass("UNIFORM");
  public static final SizeClass UNKNOWN           = new SizeClass("UNKNOWN");
  public static final SizeClass VERY_LARGE        = new SizeClass("VERY-LARGE", Structure.SINGLE_STORY);
  public static final SizeClass VLMU              = new SizeClass("VLMU", Structure.MULTIPLE_STORY);
  public static final SizeClass VLTS              = new SizeClass("VLTS", Structure.MULTIPLE_STORY);
  public static final SizeClass WATER             = new SizeClass("WATER", Structure.NON_FOREST);

  // ***********************************
  // *** Eastside and Westside Zones ***
  // ***********************************

  public static final SizeClass CLOSED_HERB       = new SizeClass("CLOSED-HERB", Structure.NON_FOREST);
  public static final SizeClass OPEN_HERB         = new SizeClass("OPEN-HERB", Structure.NON_FOREST);

  // ***************************
  // *** Westside Region One ***
  // ***************************

  public static final SizeClass WOODLAND          = new SizeClass("WOODLAND");

  // ***************************
  // *** Eastside Region One ***
  // ***************************

  public static final SizeClass SCATTERED         = new SizeClass("SCATTERED", Structure.NON_FOREST);

  // ********************************
  // *** Southern California Zone ***
  // ********************************

  public static final SizeClass BURNED_URBAN      = new SizeClass("BURNED-URBAN");
  public static final SizeClass URBAN             = new SizeClass("URBAN", Structure.NON_FOREST);

  // *****************
  // *** Gila Zone ***
  // *****************

  public static final SizeClass GRA               = new SizeClass("GRA", Structure.NON_FOREST); // Grass
  public static final SizeClass SHR               = new SizeClass("SHR", Structure.NON_FOREST); // Shrub

  // *********************************
  // *** South Central Alaska Zone ***
  // *********************************

  public static final SizeClass SS_LARGE          = new SizeClass("SS-LARGE");
  public static final SizeClass SS_SS             = new SizeClass("SS-SS");
  public static final SizeClass SS_POLE           = new SizeClass("SS-POLE");
  public static final SizeClass SS_LARGE_POLE     = new SizeClass("SS-LARGE-POLE");
  public static final SizeClass SS_POLE_LARGE     = new SizeClass("SS-POLE-LARGE");
  public static final SizeClass SS_POLE_POLE      = new SizeClass("SS-POLE-POLE");
  public static final SizeClass SS_LARGE_LARGE    = new SizeClass("SS-LARGE-LARGE");
  public static final SizeClass SS_LARGE_SS       = new SizeClass("SS-LARGE-SS");
  public static final SizeClass SS_POLE_SS        = new SizeClass("SS-POLE-SS");
  public static final SizeClass SS_SS_POLE        = new SizeClass("SS-SS-POLE");
  public static final SizeClass SS_SS_LARGE       = new SizeClass("SS-SS-LARGE");
  public static final SizeClass SS_SS_SS          = new SizeClass("SS-SS-SS");
  public static final SizeClass POLE_POLE         = new SizeClass("POLE-POLE");
  public static final SizeClass POLE_SS           = new SizeClass("POLE-SS");
  public static final SizeClass POLE_LARGE        = new SizeClass("POLE-LARGE");
  public static final SizeClass POLE_SS_LARGE     = new SizeClass("POLE-SS-LARGE");
  public static final SizeClass POLE_LARGE_SS     = new SizeClass("POLE-LARGE-SS");
  public static final SizeClass POLE_POLE_LARGE   = new SizeClass("POLE-POLE-LARGE");
  public static final SizeClass POLE_POLE_SS      = new SizeClass("POLE-POLE-SS");
  public static final SizeClass POLE_SS_SS        = new SizeClass("POLE-SS-SS");
  public static final SizeClass POLE_LARGE_LARGE  = new SizeClass("POLE-LARGE-LARGE");
  public static final SizeClass POLE_SS_POLE      = new SizeClass("POLE-SS-POLE");
  public static final SizeClass POLE_LARGE_POLE   = new SizeClass("POLE-LARGE-POLE");
  public static final SizeClass POLE_POLE_POLE    = new SizeClass("POLE-POLE-POLE");
  public static final SizeClass LARGE_LARGE       = new SizeClass("LARGE-LARGE");
  public static final SizeClass LARGE_POLE        = new SizeClass("LARGE-POLE");
  public static final SizeClass LARGE_SS          = new SizeClass("LARGE-SS");
  public static final SizeClass LARGE_SS_POLE     = new SizeClass("LARGE-SS-POLE");
  public static final SizeClass LARGE_POLE_SS     = new SizeClass("LARGE-POLE-SS");
  public static final SizeClass LARGE_SS_SS       = new SizeClass("LARGE-SS-SS");
  public static final SizeClass LARGE_POLE_POLE   = new SizeClass("LARGE-POLE-POLE");
  public static final SizeClass LARGE_LARGE_SS    = new SizeClass("LARGE-LARGE-SS");
  public static final SizeClass LARGE_LARGE_POLE  = new SizeClass("LARGE-LARGE-POLE");
  public static final SizeClass LARGE_POLE_LARGE  = new SizeClass("LARGE-POLE-LARGE");
  public static final SizeClass LARGE_SS_LARGE    = new SizeClass("LARGE-SS-LARGE");
  public static final SizeClass LARGE_LARGE_LARGE = new SizeClass("LARGE-LARGE-LARGE");
  public static final SizeClass TALL_SHRUB        = new SizeClass("TALL-SHRUB");
  public static final SizeClass LOW_SHRUB         = new SizeClass("LOW-SHRUB");
  public static final SizeClass DWARF_SHRUB       = new SizeClass("DWARF-SHRUB");
  public static final SizeClass HERB              = new SizeClass("HERB");
  public static final SizeClass GH                = new SizeClass("GH");
  public static final SizeClass AQU               = new SizeClass("AQU");
  public static final SizeClass OCEAN             = new SizeClass("OCEAN");
  public static final SizeClass ALP               = new SizeClass("ALP");
  public static final SizeClass MSH               = new SizeClass("MSH");

  // **********************
  // *** Southwest Utah ***
  // **********************

  public static final SizeClass LS                = new SizeClass("LS", Structure.NON_FOREST);
  public static final SizeClass MS                = new SizeClass("MS", Structure.NON_FOREST);
  public static final SizeClass TS                = new SizeClass("TS", Structure.NON_FOREST);
  public static final SizeClass BARREN            = new SizeClass("BARREN");
  public static final SizeClass GF                = new SizeClass("GF", Structure.NON_FOREST);
  public static final SizeClass RIPARIAN          = new SizeClass("RIPARIAN", Structure.NON_FOREST);
  public static final SizeClass ALPINE            = new SizeClass("ALPINE");
  public static final SizeClass ROCK_BARE         = new SizeClass("ROCK-BARE", Structure.NON_FOREST);
  public static final SizeClass AGR_URB           = new SizeClass("AGR-URB", Structure.NON_FOREST);
  public static final SizeClass GRASS             = new SizeClass("GRASS", Structure.NON_FOREST);
  public static final SizeClass MU                = new SizeClass("MU");

  // ****************************
  // *** Colorado Front Range ***
  // ****************************

  public static final SizeClass BA                = new SizeClass("BA", Structure.NON_FOREST);
  public static final SizeClass MEDIUM_SH         = new SizeClass("MEDIUM-SH", Structure.NON_FOREST);
  public static final SizeClass LARGE_SH          = new SizeClass("LARGE-SH", Structure.NON_FOREST);
  public static final SizeClass SMALL_SH          = new SizeClass("SMALL-SH", Structure.NON_FOREST);
  public static final SizeClass E                 = new SizeClass("E", Structure.SINGLE_STORY);
  public static final SizeClass GRA_SHR           = new SizeClass("GRA-SHR");
  public static final SizeClass AGR_URBAN         = new SizeClass("AGR-URBAN");

  // ***********************************
  // *** Western Great Plains Steppe ***
  // ***********************************

  public static final SizeClass NA                = new SizeClass("NA", Structure.NON_FOREST);

  private String name;
  private Structure structure;
  private short simId = -1;

  /**
   * Creates an unnamed non-forested size class.
   */
  public SizeClass() {
    name = null;
    structure = Structure.NON_FOREST;
  }

  /**
   * Creates a non-forested size class.
   *
   * @param name a human-readable name
   */
  public SizeClass(String name) {
    this(name, Structure.NON_FOREST);
  }

  /**
   * Creates a size class.
   *
   * @param name a human-readable name
   * @param structure the structure of the vegetation
   */
  public SizeClass(String name, Structure structure) {
    this.name = name.toUpperCase();
    this.structure = structure;
    updateAllData(this, SIZE_CLASS);
  }

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

/**
 * toString of this sizeClass.  this is as string.  A list of these are in the static variables in SizeClass.java
 */
  public String toString() { return name; }
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
    return name;
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
      if (name == null || obj == null) { return false; }

      return name.equals(((SizeClass)obj).name);
    }
    return false;
  }
/**
 * Gets the hash code for this size class object.
 */
  public int hashCode() {
    return name.hashCode();
  }
/**
 * Comparable method implementation.  Compares the object parameters to string (which will be size class name) to the this size class 
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }

  public boolean isValid() { return SizeClass.get(name) != null; }
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
      sizeClass = new SizeClass(sizeClassStr.toUpperCase());
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

    name = (String)in.readObject();
    String structStr = (String)in.readObject();

    if (structStr.equals("NON-FOREST")) {
      structure = Structure.NON_FOREST;
    }
    else if (structStr.equals("MULTIPLE-STORY")) {
      structure = Structure.MULTIPLE_STORY;
    }
    else if (structStr.equals("SINGLE-STORY")) {
      structure = Structure.SINGLE_STORY;
    }
    else {
      structure = Structure.NON_FOREST;
    }

  }
  /**
   * Writes to external location the size class structure. The choices for these are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY 
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(name);
    if (structure == Structure.NON_FOREST) {
      out.writeObject("NON-FOREST");
    }
    else if (structure == Structure.MULTIPLE_STORY) {
      out.writeObject("MULTIPLE-STORY");
    }
    else if (structure == Structure.SINGLE_STORY) {
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
    SizeClass sizeClassObj = SizeClass.get(name,true);

    sizeClassObj.name = this.name;
    sizeClassObj.structure = this.structure;

    updateAllData(sizeClassObj,SIZE_CLASS);
    return sizeClassObj;
  }
/**
 * Sets the size class to the parameter string of the size class.  A list of size classes can be found at top of SizeClass.java.
 * @param sizeClass
 */
  public void setSizeClass(String sizeClass) {
    this.name = sizeClass;
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
      case CODE_COL:
        return "Size Class";
      case STRUCTURE_COL:
        return "Structure";
      default:
        return "";
    }
  }

}




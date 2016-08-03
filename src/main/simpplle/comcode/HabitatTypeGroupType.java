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
 * This class manages Habitat Type Group Type information
 * 
 * @author Documentation by Brian Losi
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class HabitatTypeGroupType
  extends SimpplleType
  implements Externalizable
{

  public static final HabitatTypeGroupType ANY = new HabitatTypeGroupType("ANY",false);

  // ************************************
  // *** Common to more than one zone ***
  // ************************************
  public static final HabitatTypeGroupType ND  = new HabitatTypeGroupType("ND",false);
  public static final HabitatTypeGroupType XX1 = new HabitatTypeGroupType("XX1",false);
  public static final HabitatTypeGroupType XX3 = new HabitatTypeGroupType("XX3",false);
  public static final HabitatTypeGroupType XX4 = new HabitatTypeGroupType("XX4",false);
  public static final HabitatTypeGroupType XX5 = new HabitatTypeGroupType("XX5",false);

  // ***********************************
  // *** Eastside and Westside Zones ***
  // ***********************************
  public static final HabitatTypeGroupType A1   = new HabitatTypeGroupType("A1",false);
  public static final HabitatTypeGroupType A2   = new HabitatTypeGroupType("A2",false);
  public static final HabitatTypeGroupType B1   = new HabitatTypeGroupType("B1",false);
  public static final HabitatTypeGroupType B2   = new HabitatTypeGroupType("B2",false);
  public static final HabitatTypeGroupType B3   = new HabitatTypeGroupType("B3",false);
  public static final HabitatTypeGroupType C1   = new HabitatTypeGroupType("C1",false);
  public static final HabitatTypeGroupType C2   = new HabitatTypeGroupType("C2",false);
  public static final HabitatTypeGroupType D1   = new HabitatTypeGroupType("D1",false);
  public static final HabitatTypeGroupType D2   = new HabitatTypeGroupType("D2",false);
  public static final HabitatTypeGroupType D3   = new HabitatTypeGroupType("D3",false);
  public static final HabitatTypeGroupType E1   = new HabitatTypeGroupType("E1",false);
  public static final HabitatTypeGroupType E2   = new HabitatTypeGroupType("E2",false);
  public static final HabitatTypeGroupType F1   = new HabitatTypeGroupType("F1",false);
  public static final HabitatTypeGroupType F2   = new HabitatTypeGroupType("F2",false);
  public static final HabitatTypeGroupType G1   = new HabitatTypeGroupType("G1",false);
  public static final HabitatTypeGroupType G2   = new HabitatTypeGroupType("G2",false);

  public static final HabitatTypeGroupType NF1A = new HabitatTypeGroupType("NF1A",false);
  public static final HabitatTypeGroupType NF2A = new HabitatTypeGroupType("NF2A",false);

  // ***************************
  // *** Westside Region One ***
  // ***************************
  public static final HabitatTypeGroupType NF1  = new HabitatTypeGroupType("NF1",false);
  public static final HabitatTypeGroupType NF2  = new HabitatTypeGroupType("NF2",false);
  public static final HabitatTypeGroupType NF3  = new HabitatTypeGroupType("NF3",false);
  public static final HabitatTypeGroupType NF4  = new HabitatTypeGroupType("NF4",false);
  public static final HabitatTypeGroupType NF5  = new HabitatTypeGroupType("NF5",false);

  // ***************************
  // *** Eastside Region One ***
  // ***************************
  public static final HabitatTypeGroupType NF1B = new HabitatTypeGroupType("NF1B",false);
  public static final HabitatTypeGroupType NF1C = new HabitatTypeGroupType("NF1C",false);

  public static final HabitatTypeGroupType NF2B = new HabitatTypeGroupType("NF2B",false);
  public static final HabitatTypeGroupType NF2C = new HabitatTypeGroupType("NF2C",false);
  public static final HabitatTypeGroupType NF2D = new HabitatTypeGroupType("NF2D",false);

  public static final HabitatTypeGroupType NF3A = new HabitatTypeGroupType("NF3A",false);
  public static final HabitatTypeGroupType NF3B = new HabitatTypeGroupType("NF3B",false);
  public static final HabitatTypeGroupType NF3C = new HabitatTypeGroupType("NF3C",false);
  public static final HabitatTypeGroupType NF3D = new HabitatTypeGroupType("NF3D",false);

  public static final HabitatTypeGroupType NF4A = new HabitatTypeGroupType("NF4A",false);
  public static final HabitatTypeGroupType NF4B = new HabitatTypeGroupType("NF4B",false);
  public static final HabitatTypeGroupType NF4C = new HabitatTypeGroupType("NF4C",false);
  public static final HabitatTypeGroupType NF4D = new HabitatTypeGroupType("NF4D",false);
  public static final HabitatTypeGroupType NF4E = new HabitatTypeGroupType("NF4E",false);

  public static final HabitatTypeGroupType NF5A = new HabitatTypeGroupType("NF5A",false);
  public static final HabitatTypeGroupType NF5B = new HabitatTypeGroupType("NF5B",false);

  // ***************************************************
  // *** Sierra Nevada and Southern California Zones ***
  // ***************************************************
  public static final HabitatTypeGroupType FTH_M = new HabitatTypeGroupType("FTH-M",false);
  public static final HabitatTypeGroupType FTH_X = new HabitatTypeGroupType("FTH-X",false);
  public static final HabitatTypeGroupType LM_M  = new HabitatTypeGroupType("LM-M",false);
  public static final HabitatTypeGroupType LM_X  = new HabitatTypeGroupType("LM-X",false);
  public static final HabitatTypeGroupType SA    = new HabitatTypeGroupType("SA",false);
  public static final HabitatTypeGroupType UM_M  = new HabitatTypeGroupType("UM-M",false);
  public static final HabitatTypeGroupType UM_X  = new HabitatTypeGroupType("UM-X",false);

  // ***************************
  // *** South Central Alaska ***
  // ***************************
  public static final HabitatTypeGroupType KENAI = new HabitatTypeGroupType("KENAI",false);

  // ************
  // *** Gila ***
  // ************
  public static final HabitatTypeGroupType NF = new HabitatTypeGroupType("NF",false); // Non Forest
  public static final HabitatTypeGroupType W  = new HabitatTypeGroupType("W",false);  // Woodland
  public static final HabitatTypeGroupType R  = new HabitatTypeGroupType("R",false);  // Riparian
  public static final HabitatTypeGroupType FW = new HabitatTypeGroupType("FW",false); // Forested Wet
  public static final HabitatTypeGroupType FD = new HabitatTypeGroupType("FD",false); // Forested Dry

  // **********************
  // *** Southwest Utah ***
  // **********************
  public static final HabitatTypeGroupType NON_FOREST = new HabitatTypeGroupType("0",false);
  public static final HabitatTypeGroupType ONE        = new HabitatTypeGroupType("1",false);
  public static final HabitatTypeGroupType TWO        = new HabitatTypeGroupType("2",false);
  public static final HabitatTypeGroupType THREE      = new HabitatTypeGroupType("3",false);
  public static final HabitatTypeGroupType FOUR       = new HabitatTypeGroupType("4",false);
  public static final HabitatTypeGroupType FIVE       = new HabitatTypeGroupType("5",false);
  public static final HabitatTypeGroupType SIX        = new HabitatTypeGroupType("6",false);
  public static final HabitatTypeGroupType SEVEN      = new HabitatTypeGroupType("7",false);

  // ****************
  // *** Colorado ***
  // ****************

  public static final HabitatTypeGroupType LOWER_MONTANE  = new HabitatTypeGroupType("LOWER-MONTANE",false);
  public static final HabitatTypeGroupType ALPINE         = new HabitatTypeGroupType("ALPINE",false);
  public static final HabitatTypeGroupType UPPER_MONTANE  = new HabitatTypeGroupType("UPPER-MONTANE",false);
  public static final HabitatTypeGroupType SUBALPINE      = new HabitatTypeGroupType("SUBALPINE",false);
  public static final HabitatTypeGroupType FOOTHILLS      = new HabitatTypeGroupType("FOOTHILLS",false);
  public static final HabitatTypeGroupType PLAINS         = new HabitatTypeGroupType("PLAINS",false);



  // *** Western Great Plains Steppe ***
  // ***********************************
  public static final HabitatTypeGroupType CLAYEY_OVERFLOW = new HabitatTypeGroupType("CLAYEY_OVERFLOW",false);

  static final long serialVersionUID = -7681946489535177124L;;
  static final int  version          = 1;
  static final int simpleVersion     = 1;

  private String   name;
  private String   description;
  private boolean  userCreated;

  public static HashMap<Short,HabitatTypeGroupType> simIdHm =
    new HashMap<Short,HabitatTypeGroupType>();
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

  public static HabitatTypeGroupType lookUp(short simId) { return simIdHm.get(simId); }

  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      HabitatTypeGroupType group = (HabitatTypeGroupType)in.readObject();
      simIdHm.put(id,group);
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
      HabitatTypeGroupType group = simIdHm.get(id);
      out.writeObject(group);
    }
  }




  public static final int COLUMN_COUNT = 1;
  public static final int CODE_COL     = 0;

/**
 * Constructor for Habitat type group type object.  These objects have a name, description and boolean if user created.  
 * There are many different habitat type groups that are often different for each regional zone.  An example of habitat type group types for a 
 * regional zone are the following for GIla:
 *
 * HabitatTypeGroupType NF = new HabitatTypeGroupType("NF",false); // Non Forest
 * HabitatTypeGroupType W  = new HabitatTypeGroupType("W",false);  // Woodland
 * HabitatTypeGroupType R  = new HabitatTypeGroupType("R",false);  // Riparian
 * HabitatTypeGroupType FW = new HabitatTypeGroupType("FW",false); // Forested Wet
 * HabitatTypeGroupType FD = new HabitatTypeGroupType("FD",false); // Forested Dry
 * 
 */
  public HabitatTypeGroupType() {
    this.name        = null;
    this.description = null;
    this.userCreated = true;
  }
  /**
   * Overloaded habitat type group which exists to allow users to create a new habitat type group type.   
   * @param group group name, which will be passed to another constructor with the parameter name as both the group name
   * and group description, plus true for user created boolean.  
   */
  public HabitatTypeGroupType(String group) {
    this(group,group,true);
  }
  /**
   * Overloaded constructor for habitat type group type.  There are many different habitat type groups.  This method is called from the 
   * constructor used to define regional zones habitat type group types and user created groups.
   * The group name is passed in as the group name and group description.  Example from Gila:
   * HabitatTypeGroupType("NF", "NF, false); // Non Forest
   * @param group the 
   * @param description
   * @param userCreated
   */
  public HabitatTypeGroupType(String group, String description, boolean userCreated) {
    this.name        = group.toUpperCase();
    this.description = description;
    this.userCreated = userCreated;

    updateAllData(this,GROUP);
  }
  /**
   * Overloaded constructor for Habitat type group type object.  This constructor is used to create define a regional zones habitat type groups
   * An example of habitat type group types for a regional zone is the following for Gila:
   *
   * HabitatTypeGroupType NF = new HabitatTypeGroupType("NF",false); // Non Forest
   * HabitatTypeGroupType W  = new HabitatTypeGroupType("W",false);  // Woodland
   * HabitatTypeGroupType R  = new HabitatTypeGroupType("R",false);  // Riparian
   * HabitatTypeGroupType FW = new HabitatTypeGroupType("FW",false); // Forested Wet
   * HabitatTypeGroupType FD = new HabitatTypeGroupType("FD",false); // Forested Dry
   * 
   */
  public HabitatTypeGroupType(String group, boolean userCreated) {
    this(group,group,userCreated);
  }
/**
 * Looks up a habitat group by its string name (ex. "NF" - Non Forest) and sees if it equals this habitat type group type.  
 * @param name
 * @return
 */
  public boolean lookupEquals(String name) {
    return equals(get(name));
  }
/**
 * Checks if parameter object is a habitat type group type object and if it's name equals this one.  
 * @return true if parameter object is a habitat type group type object and if it's name equals this one
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof HabitatTypeGroupType) {
      if (name == null || obj == null) { return false; }

      return name.equals(((HabitatTypeGroupType)obj).name);
    }
    return false;
  }
/**
 * Returns the hash code for this habitat type group type based on its name.  
 */
  public int hashCode() {
    return name.hashCode();
  }
/**
 * Required compareTo method which compares the names of parameter habitat type group type object and this one.  
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }
/**
 * Returns the name of this habitat type group type (ex. "NF" - Non Forest).
 */
  public String toString() { return name; }

  public static HabitatTypeGroupType get(HabitatTypeGroup group) {
    return get(group.toString());
  }
  /**
   * Gets the name of the habitat type group type object using the parameter name.    
   * @param htGrpName 
   * @return
   */
  public static HabitatTypeGroupType get(String htGrpName) {
    return ( (HabitatTypeGroupType)allGroupHm.get(htGrpName.toUpperCase()) );
  }
  /**
   * First tries to get the HabitatTypeGroupType from the allGroupHM keyed by uppercase name.  If does not exist and create is true, creates a new
   * HabitatTypeGroupType object with the parameter name.  
   * @param htGrpName used to look up HabitatTypeGroupType object, and make a new one if needed
   * @param create true if should create a new HabitatTypeGroupType object
   * @return either a new HabitatTypeGroupType with parameter name, or the HabitatTypeGroupType in the allGroupHM
   */
  public static HabitatTypeGroupType get(String htGrpName, boolean create) {
    HabitatTypeGroupType group =
        ( (HabitatTypeGroupType)allGroupHm.get(htGrpName.toUpperCase()) );

    if (group == null && create) {
      group = new HabitatTypeGroupType(htGrpName,false);
    }
    return group;
  }
/**
 * Checks whether this habitat type group type is user created.  
 * @return
 */
  public boolean isUserCreated() { return userCreated; }
  /**
   * Returns the name of this habitat type group type (ex. "NF" - Non Forest).
   * @return the name of habitat type group type (ex. "NF" - Non Forest).
   */
  public String getName() {
    return name;
  }
/**
 * Gets an arraylist of all habitat type group types
 * @return
 */
  public static ArrayList getAllLoadedGroups() {
    ArrayList groups = HabitatTypeGroup.getAllLoadedGroups();
    ArrayList aList = new ArrayList(groups.size());

    for (int i=0; i<groups.size(); i++) {
      aList.add(get((HabitatTypeGroup)groups.get(i)));
    }
    return aList;
  }
  /**
   * Reads from an external source the variables that define a habitat type group type.  
   * These are the name, description, and usercreated boolean (true if user created)
   */
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    name        = (String)in.readObject();
    description = (String)in.readObject();
    userCreated = in.readBoolean();

  }
   /**
   * Writes to an external source the variables that define a habitat type group type.  
   * These are the name, description, and userCreated boolean (true if user created)
    */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(name);
    out.writeObject(description);
    out.writeBoolean(userCreated);
  }
  /**
   * Read resolve method which will get the habitatTypeGroupType object and set this objects description and userCreated variables.  
   * then updates all OpenSsimpplle type objects hashmap.   
   * @return
   * @throws java.io.ObjectStreamException
   */
  private Object readResolve () throws java.io.ObjectStreamException
  {
    HabitatTypeGroupType groupObj = this.get(name,true);

    groupObj.description          = this.description;
    groupObj.userCreated          = this.userCreated;

    updateAllData(groupObj,GROUP);
    return groupObj;
  }
  public static HabitatTypeGroupType readExternalSimple(ObjectInput in) throws IOException, ClassNotFoundException {
   int version = in.readInt();

   String name        = (String)in.readObject();
   return HabitatTypeGroupType.get(name);
  }
  public void writeExternalSimple(ObjectOutput out) throws IOException {
    out.writeInt(simpleVersion);
     out.writeObject(name);
  }
/**
 * Sets the name of this habitat type group type (ex. "NF" - Non Forest).
 * 
 * @param name the name of habitat type group type (ex. "NF" - Non Forest).
 */
  public void setName(String name) {
    this.name = name;
  }
/**
 * Used in GUI table models.  Gets the data in a column by column Id.  If col = CODE_COL (0) will return this object else returns null. 
 */
  public Object getColumnData(int col) {
    switch (col) {
      case CODE_COL:
        return this;
      default: return null;
    }
  }
  /**
   * Used in GUI table models.  Doesn't really do anything currrently. 
   */
  public void setColumnData(Object value, int col) {
    switch (col) {
      default: return;
    }
//    SystemKnowledge.markChanged(SystemKnowledge.TREATMENT_TYPE);
  }
  /**
   * Used in GUI table models.  Gets the column name by column Id.  If col = CODE_COL (0) will "Eco Group", else returns empty string. 
   */
  public static String getColumnName(int col) {
    switch (col) {
      case CODE_COL: return "Eco Group";
      default: return "";
    }
  }

}





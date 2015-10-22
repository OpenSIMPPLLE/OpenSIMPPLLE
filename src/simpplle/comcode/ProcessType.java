package simpplle.comcode;

import java.io.*;
import java.util.HashMap;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Process Type, a Simpplle Type.  
 * <p>It contains methods for database, then a set of constants which define the different process type, and methods.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */
public class ProcessType extends SimpplleType implements Externalizable {
  static final long serialVersionUID = 8238984030408782100L;
  static final int  version          = 1;
  static final int  simpleVersion    = 1;

  public static final int COLUMN_COUNT = 1;
  public static final int CODE_COL     = 0;

  private String processName;
  private String className;
  private boolean spreading;

  public static HashMap<Short,ProcessType> simIdHm = new HashMap<Short,ProcessType>();
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
/**
 * Looks up process type by simulation id.  
 * @param simId
 * @return
 */
  public static ProcessType lookUpProcessType(short simId) { return simIdHm.get(simId); }

  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      ProcessType process = readExternalSimple(in);
      simIdHm.put(id,process);
      if ( (id+1) > nextSimId) {
        nextSimId = (short)(id+1);
      }
    }
  }
  /**
   * Writes an external simulation id 
   * @param out
   * @throws IOException
   */
  public static void writeExternalSimIdHm(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeInt(simIdHm.size());
    for (Short id : simIdHm.keySet()) {
      out.writeShort(id);
      ProcessType process = simIdHm.get(id);
      process.writeExternalSimple(out);
    }
  }



  public static final ProcessType UNKNOWN              = new ProcessType("UNKNOWN",null,false);
  public static final ProcessType NONE                 = new ProcessType("NONE",null,false);
  public static final ProcessType NIL                  = new ProcessType("NIL",null,false);

  public static final ProcessType SUCCESSION           = new ProcessType("SUCCESSION","Succession",false);
  public static final ProcessType FIRE_EVENT           = new ProcessType("FIRE-EVENT","FireEvent",false);
  public static final ProcessType STAND_REPLACING_FIRE = new ProcessType("STAND-REPLACING-FIRE","StandReplacingFire",true);
  public static final ProcessType SRF                  = STAND_REPLACING_FIRE;
  public static final ProcessType SPOT_SRF             = new ProcessType("SRF*","StandReplacingFire",true);
  public static final ProcessType MIXED_SEVERITY_FIRE  = new ProcessType("MIXED-SEVERITY-FIRE","MixedSeverityFire",true);
  public static final ProcessType MSF                  = MIXED_SEVERITY_FIRE;
  public static final ProcessType SPOT_MSF             = new ProcessType("MSF*","MixedSeverityFire",true);
  public static final ProcessType LIGHT_SEVERITY_FIRE  = new ProcessType("LIGHT-SEVERITY-FIRE","LightSeverityFire",true);
  public static final ProcessType LSF                  = LIGHT_SEVERITY_FIRE;
  public static final ProcessType SPOT_LSF             = new ProcessType("LSF*","LightSeverityFire",true);
  public static final ProcessType ROOT_DISEASE         = new ProcessType("ROOT-DISEASE","RootDisease",false);
  public static final ProcessType HS_ROOT_DISEASE      = new ProcessType("HIGH-SEVERITY-ROOT-DISEASE","HighSeverityRootDisease",false);
  public static final ProcessType MS_ROOT_DISEASE      = new ProcessType("MODERATE-SEVERITY-ROOT-DISEASE","ModerateSeverityRootDisease",false);
  public static final ProcessType LS_ROOT_DISEASE      = new ProcessType("LIGHT-SEVERITY-ROOT-DISEASE","LightSeverityRootDisease",false);

  public static final ProcessType SRF_SPRING = new ProcessType("SRF-SPRING","StandReplacingFire",true);
  public static final ProcessType SRF_SUMMER = new ProcessType("SRF-SUMMER","StandReplacingFire",true);
  public static final ProcessType SRF_FALL   = new ProcessType("SRF-FALL","StandReplacingFire",true);
  public static final ProcessType SRF_WINTER = new ProcessType("SRF-WINTER","StandReplacingFire",true);

  // *****************************************
  // *** Eastside, Westside and Gila Zones ***
   // *****************************************
  public static final ProcessType LIGHT_WSBW               = new ProcessType("LIGHT-WSBW","LightWsbw",true);
  public static final ProcessType SEVERE_WSBW              = new ProcessType("SEVERE-WSBW","SevereWsbw",true);
  public static final ProcessType LIGHT_LP_MPB             = new ProcessType("LIGHT-LP-MPB","LightLpMpb",true);
  public static final ProcessType SEVERE_LP_MPB            = new ProcessType("SEVERE-LP-MPB","SevereLpMpb",true);
  public static final ProcessType PP_MPB                   = new ProcessType("PP-MPB","PpMpb",true);
  public static final ProcessType WINDTHROW                = new ProcessType("WINDTHROW","Windthrow",false);
  public static final ProcessType WINTER_DROUGHT           = new ProcessType("WINTER-DROUGHT","WinterDrought",false);
  public static final ProcessType COLD_INJURY_BARK_BEETLES = new ProcessType("COLD-INJURY-BARK-BEETLES","ColdInjuryBarkBeetles",false);
  public static final ProcessType BARK_BEETLES             = new ProcessType("BARK-BEETLES","BarkBeetles",false);
  public static final ProcessType WBP_MPB                  = new ProcessType("WBP-MPB","WbpMpb",false);
  public static final ProcessType WP_MPB                   = new ProcessType("WP-MPB","WpMpb",false);
  public static final ProcessType DF_BEETLE                = new ProcessType("DF-BEETLE","DfBeetle",false);
  public static final ProcessType SPRUCE_BEETLE            = new ProcessType("SPRUCE-BEETLE","SpruceBeetle",false);

  public static final ProcessType WSBW                     = new ProcessType("WSBW","Wsbw",false);

  // ***************************************************
  // *** Sierra Nevada and Southern California Zones ***
  // ***************************************************
  public static final ProcessType DROUGHT_MORTALITY   = new ProcessType("DROUGHT-MORTALITY","DroughtMortality",false);
  public static final ProcessType LIGHT_BARK_BEETLES  = new ProcessType("LIGHT-BARK-BEETLES","LightBarkBeetles",false);
  public static final ProcessType SEVERE_BARK_BEETLES = new ProcessType("SEVERE-BARK-BEETLES","SevereBarkBeetles",true);
  public static final ProcessType BB_RD_DM_COMPLEX    = new ProcessType("BB-RD-DM-COMPLEX","BbRdDmComplex",false);
  public static final ProcessType BLISTER_RUST        = new ProcessType("BLISTER-RUST","BlisterRust",false);

  // *****************
  // *** Gila Zone ***
  // *****************
  public static final ProcessType ELK_HERBIVORY = new ProcessType("ELK-HERBIVORY","ElkHerbivory",false);

  // ****************************
  // *** South Central Alaska ***
  // ****************************
  public static final ProcessType LIGHT_SB          = new ProcessType("LIGHT-SB","LightSb",false);
  public static final ProcessType MEDIUM_SB         = new ProcessType("MEDIUM-SB","MediumSb",false);
  public static final ProcessType HIGH_SB           = new ProcessType("HIGH-SB","HighSb",false);
  public static final ProcessType LOW_WINDTHROW     = new ProcessType("LOW-WINDTHROW","LowWindthrow",false);
  public static final ProcessType MEDIUM_WINDTHROW  = new ProcessType("MEDIUM-WINDTHROW","MediumWindthrow",false);
  public static final ProcessType HIGH_WINDTHROW    = new ProcessType("HIGH-WINDTHROW","HighWindthrow",false);
  public static final ProcessType FLOOD             = new ProcessType("FLOOD","Flood",false);
  public static final ProcessType SNOW_AVALANCHE    = new ProcessType("SNOW-AVALANCHE","SnowAvalanche",false);
  public static final ProcessType WILDLIFE_BROWSING = new ProcessType("WILDLIFE-BROWSING","WildlifeBrowsing",false);

  // Colorado Front Range
  public static final ProcessType PJ_DECLINE     = new ProcessType("PJ-DECLINE","PJDecline",false);
  public static final ProcessType PRAIRIE_DOG    = new ProcessType("PRAIRIE-DOG","PrairieDog",false);
  public static final ProcessType TUSSOCK_MOTH   = new ProcessType("TUSSOCK-MOTH","TussockMoth",false);
  public static final ProcessType WET_SUCCESSION = new ProcessType("WET-SUCCESSION","WetSuccession",false);
  public static final ProcessType DRY_SUCCESSION = new ProcessType("DRY-SUCCESSION","DrySuccession",false);
  public static final ProcessType PIED_BB      = new ProcessType("PIED-BB","PiedBB",false);

  // Colorado Plateau
  public static final ProcessType BLACK_STAIN_RD = new ProcessType("BLACK-STAIN-RD","BlackStainRD",false);
  public static final ProcessType HM_PD   = new ProcessType("HM-PD","HMPrairieDog",false);
  public static final ProcessType LM_PD   = new ProcessType("LM-PD","LMPrairieDog",false);
  public static final ProcessType DROUGHT = new ProcessType("DROUGHT","Drought",false);




  // ***********************************
  // *** Western Great Plains Steppe ***
  // ***********************************
  public static final ProcessType LIGHT_BISON_GRAZING    = new ProcessType("LIGHT-BISON-GRAZING","LightBisonGrazing",false);
  public static final ProcessType MODERATE_BISON_GRAZING = new ProcessType("MODERATE-BISON-GRAZING","ModerateBisonGrazing",false);
  public static final ProcessType HEAVY_BISON_GRAZING    = new ProcessType("HEAVY-BISON-GRAZING","HeavyBisonGrazing",false);
  public static final ProcessType PRAIRIE_DOG_ACTIVE     = new ProcessType("PRAIRIE-DOG-ACTIVE","PrairieDog",false);
  public static final ProcessType PRAIRIE_DOG_INACTIVE   = new ProcessType("PRAIRIE-DOG-INACTIVE","PrairieDog",false);

  // Used for prob logic purposes (not simulation)
  public static final ProcessType BISON_GRAZING = new ProcessType("BISON-GRAZING","BisonGrazing",false);

// PRAIRIE-DOG-ACTIVE

  // *************************
  // *** Aquatic Processes ***
  // *************************
  public static final ProcessType STREAM_DEVELOPMENT     = new ProcessType("STREAM-DEVELOPMENT","StreamDevelopment",false);
  public static final ProcessType DEBRIS_EVENT           = new ProcessType("DEBRIS-EVENT","DebrisEvent",false);
  public static final ProcessType FLOOD_EVENT            = new ProcessType("FLOOD-EVENT","FloodEvent",false);
  public static final ProcessType TEN_YEAR_FLOOD         = new ProcessType("TEN-YEAR-FLOOD","TenYearFlood",false);
  public static final ProcessType FIFTY_YEAR_FLOOD       = new ProcessType("FIFTY-YEAR-FLOOD","FiftyYearFlood",false);
  public static final ProcessType ONE_HUNDRED_YEAR_FLOOD = new ProcessType("ONE-HUNDRED-YEAR-FLOOD","OneHundredYearFlood",false);
  public static final ProcessType MINOR_VEG_DISTURB      = new ProcessType("MINOR-VEG-DISTURB","MinorVegDisturb",false);
  public static final ProcessType SEVERE_VEG_DISTURB     = new ProcessType("SEVERE-VEG-DISTURB","SevereVegDisturb",false);
  public static final ProcessType VEG_REPLACEMENT        = new ProcessType("VEG-REPLACEMENT","VegReplacement",false);

/**
 * Constructor for process type.  Initializes process name, class name and spreading to false. 
 */
  public ProcessType() {
    processName = null;
    className   = null;
    spreading   = false;
  }
/**
 * Overloaded constructor.  Initializes process and class name, and spreading to passed in arguments.  
 * example.  the following is a process type dissected below.  
 * public static final ProcessType DEBRIS_EVENT           = new ProcessType("DEBRIS-EVENT","DebrisEvent",false);
 * DEBRIS_EVENT (variable name) 
 * "DEBRIS-EVENT" - process name
 * "DebrisEvent" - className 
 * false - spreading
 * 
 * @param processName
 * @param className
 * @param spreading
 */
  protected ProcessType(String processName, String className, boolean spreading) {
    this.processName = processName.toUpperCase();
    this.className   = className;
    this.spreading   = spreading;

    updateAllData(this,PROCESS);

    if (this.processName.equals("STAND-REPLACING-FIRE")) {
      allProcessHm.put("SRF",this);
    }
    else if (this.processName.equals("MIXED-SEVERITY-FIRE")) {
      allProcessHm.put("MSF",this);
    }
    else if (this.processName.equals("LIGHT-SEVERITY-FIRE")) {
      allProcessHm.put("LSF",this);
    }
  }
/**
 * Overloaded constructor.  Used if process needs to be created by setting the class name to InvalidProcess.    
 * @param processName
 */
  protected ProcessType(String processName) {
    this.processName = processName.toUpperCase();
    this.className   = "InvalidProcess";
    this.spreading   = false;
  }
/**
 * toString of Process type .  This will simply return the process name which is an uppercase string.  e.g. "BLACK-STAIN-RD"
 */
  public String toString() { return processName; }
/**
 * gets the shortened name of a fire process type  
 * @return LSF, MSF, or SRF for types of fire events, or the toString methods defined in their classes.
 */
  public String getShortName() {
    if (equals(ProcessType.LIGHT_SEVERITY_FIRE)) {
      return "LSF";
    }
    else if (equals(ProcessType.MIXED_SEVERITY_FIRE)) {
      return "MSF";
    }
    else if (equals(ProcessType.STAND_REPLACING_FIRE)) {
      return "SRF";
    }
    else {
      return toString();
    }
  }
/**
 * checks if process type is spreading
 * @return true if process is spreading
 */
  public boolean isSpreading() { return spreading; }
/**
 * gets the string literal of current process type
 * @return process name
 */
  public String getProcessName() {
    return processName;
  }
/**
 * makes a new process
 * @return the process
 * @throws SimpplleError for ClassNotFound, IllegalAccess, or Instantiation Exceptions gets caught in GUI
 */
  public Process makeProcess() throws SimpplleError {
    try {
      Process p = (Process) Class.forName("simpplle.comcode." + className).newInstance();
      p.setType(this);
      return p;
    }
    catch (ClassNotFoundException ex) {
      throw new SimpplleError (ex.getMessage(),ex);
    }
    catch (IllegalAccessException ex) {
      throw new SimpplleError (ex.getMessage(),ex);
    }
    catch (InstantiationException ex) {
      throw new SimpplleError (ex.getMessage(),ex);
    }
  }
/**
 * Checks to see if parameter object is an instance of a Process Type.  If so it compares parameter process type with this process type
 * by their process name e.g. this.processName = "BLACK-STAIN-RD" obj.processName = "BLACK-STAIN-RD" will return true.  
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof ProcessType) {
      if (processName == null || obj == null) { return false; }

      return processName.equals(((ProcessType)obj).processName);
    }
    return false;
  }
/**
 * Returns a hashcode for the process type name.  
 */
  public int hashCode() {
    return processName.hashCode();
  }
/**
 * Comparable method for two process objects.  
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return processName.compareTo(o.toString());
  }
/**
 * 
 * @param process
 * @return the process type string literal.  Usually this is the description set in individual process type class constructor. 
 */
  public static ProcessType get(Process process) {
    return get(process.toString());
  }
  /**
   * Gets a particular process type from the all processes hash map according to parameter processName.
   * @param processName
   * @return
   */
  public static ProcessType get(String processName) {
    return ( (ProcessType)allProcessHm.get(processName.toUpperCase()) );
  }
/**
 * Gets a process type based on process name.  If the process needs to be created it will call the processType constructor for invalid process 
 * @param processName the string literal of a process type
 * @param create true if needs to be created, false if already exists
 * @return the process type object
 */
  public static ProcessType get(String processName, boolean create) {
    ProcessType processType = ((ProcessType)allProcessHm.get(processName.toUpperCase()) );

    if (processType == null && create) {
      processType = new ProcessType(processName,"InvalidProcess",false);
    }

    return processType;
  }
/**
 * Makes an invalid instance of process type.  This can be used to create a new process type.  
 * @param processName
 * @return Process type
 */
  public static ProcessType makeInvalidInstance(String processName) {
    return new ProcessType(processName,"InvalidProcess",false);
  }
/**
 * Gets the description for the process type in this particular instance. 
 * @return description of process type.  
 */
  public String getDescription() {
    return Process.findInstance(this).getDescription();
  }
/**
 * Method to figure if process type is a fire process
 * @return true if process type is a fire process
 */
  public boolean isFireProcess() {
    if (equals(ProcessType.STAND_REPLACING_FIRE) ||
        equals(ProcessType.MIXED_SEVERITY_FIRE) ||
        equals(ProcessType.LIGHT_SEVERITY_FIRE)) {
      return true;
    }
    return false;
  }

  /**
   *
   * @return
   */
  public boolean isRootDisease() {
    if (equals(ROOT_DISEASE) ||
        equals(HS_ROOT_DISEASE) ||
        equals(MS_ROOT_DISEASE) ||
        equals(LS_ROOT_DISEASE)) {
      return true;
    }
    return false;
  }
/**
 * Calculates intensity of fires.  
 * @param p the process type.  this should be either SRF - stand replacing fire or MSF mixed severity fire. any other input will return false 
 * @return the levels of fire in decreasing order are SRF, MSF and LSF.  This method returns true if input fire type is less intense then current.
 */
  public boolean isFireLessIntense(ProcessType p) {
    if ((this == MSF) && (p == SRF)) {
      return true;
    }
    else if ((this == LSF) && ((p == MSF) || (p == SRF))) {
      return true;
    }
    return false;
  }
  /**
   * Reads a process type object from external source.  
   * These are read in following order: process name, class name, spreading boolean.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    processName = (String)in.readObject();
    className   = (String)in.readObject();
    spreading   = in.readBoolean();
  }
  /**
   * Writes to an external source the process name, class name, and spreading boolean.
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(processName);
    out.writeObject(className);
    out.writeBoolean(spreading);
  }
  private Object readResolve () throws java.io.ObjectStreamException
  {
    ProcessType processObj =  ProcessType.get(processName);

    processObj.className = this.className;
    processObj.spreading = this.spreading;

    updateAllData(processObj,PROCESS);
    return processObj;
  }
/**
 * Reads a process type from external source.  
 * @param in
 * @return
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public static ProcessType readExternalSimple(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    String processName = (String)in.readObject();
    return ProcessType.get(processName);
  }
  /**
   * Writes to an external source the process name.  
   */
  public void writeExternalSimple(ObjectOutput out) throws IOException {
    out.writeInt(simpleVersion);
    out.writeObject(processName);
  }
/**
 * Sets the process type objects name from passed process string name. 
 * @param processName the name of process.  
 */
  public void setProcessName(String processName) {
    this.processName = processName;
  }

  //  public int getId() {
//    Process processInst = getProcessInstance();
//    if (processInst == null) { return ValidProcess.NIL; }
//    else {
//      return processInst.getId();
//    }
//  }

  // *** JTable section ***
  // **********************
/**
 * Gets the column data at a particular column.  Will be null unless the column is the CODE_COL
 */
  public Object getColumnData(int col) {
    switch (col) {
      case CODE_COL:
        return this;
      default: return null;
    }
  }
  /**
   * Sets the data at a particular column. 
   */
  public void setColumnData(Object value, int col) {
    switch (col) {
      default: return;
    }
//    SystemKnowledge.markChanged(SystemKnowledge.DENSITY);
  }
/**
 * Get the column name if column is CODE_COL.  It will equall Process or empty string
 * @param col the column number.
 * @return
 */
  public static String getColumnName(int col) {
    switch (col) {
      case CODE_COL: return "Process";
      default: return "";
    }
  }

}











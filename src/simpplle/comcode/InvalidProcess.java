package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class is used to create an initialProcess from a process name
 * that is not valid.  This allows the user to later correct the Evu's
 * process to a valid one.
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */

public class InvalidProcess extends Process {
  private static final String printName = "UNKNOWN";
  private String name;

  /**
   * InvalidProcess constructor.  Inherits from Process superclass and initializes spreading to false, name and description to "UNKNOWN"
   * 
   */
  public InvalidProcess() {
    super();

    spreading   = false;
    name        = "UNKNOWN";
    description = "Unknown";
  }

  /**
   * Overloaded constructor which references default, primary constructor 
   * @param processType new process type to be entered
   * @param name new name of process 
   */
  public InvalidProcess(ProcessType processType, String name) {
    this();
    this.name        = name;
    this.processType = processType;
  }
/**
 * Invalid Processes cannot happen in Westside Region 1 so doProbability is set to 0 by default.
 */
  public int doProbability (WestsideRegionOne zone, Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Eastside Region 1 so doProbability is set to 0 by default.
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Sierra Nevada so doProbability is set to 0 by default.
   */
  public int doProbability (SierraNevada zone, Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Southern California so doProbability is set to 0 by default.
   */
  public int doProbability (SouthernCalifornia zone, Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Gila so doProbability is set to 0 by default.
   */
  public int doProbability (Gila zone, Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in South Central Alaska so doProbability is set to 0 by default.
   */
  public int doProbability (SouthCentralAlaska zone, Evu evu) {return 0;}
//********************************************
  /**
   * Invalid Processes cannot happen in Westside Region 1 so doSpread is set to false by default.
   */
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {return false;}
/**
 * Invalid Processes cannot happen in Eastside Region 1 so doSpread is set to false by default.
 */
  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in Sierra Nevada so doSpread is set to false by default.
   */
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in Southern California so doSpread is set to false by default.
   */
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in Gila so doSpread is set to false by default.
   */
  public boolean doSpread (Gila zone, Evu fromEvu, Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in South Central Alaska so doSpread is set to false by default.
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {return false;}

  /**
   * outputs "UNKNOWN"
   */
  public String toString () {
    return printName;
  }
}
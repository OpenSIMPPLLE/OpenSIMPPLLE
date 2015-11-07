package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;

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
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Eastside Region 1 so doProbability is set to 0 by default.
   */
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Sierra Nevada so doProbability is set to 0 by default.
   */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Southern California so doProbability is set to 0 by default.
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in Gila so doProbability is set to 0 by default.
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu) {return 0;}
  /**
   * Invalid Processes cannot happen in South Central Alaska so doProbability is set to 0 by default.
   */
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) {return 0;}
//********************************************
  /**
   * Invalid Processes cannot happen in Westside Region 1 so doSpread is set to false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {return false;}
/**
 * Invalid Processes cannot happen in Eastside Region 1 so doSpread is set to false by default.
 */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in Sierra Nevada so doSpread is set to false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in Southern California so doSpread is set to false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in Gila so doSpread is set to false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {return false;}
  /**
   * Invalid Processes cannot happen in South Central Alaska so doSpread is set to false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {return false;}

  /**
   * outputs "UNKNOWN"
   */
  public String toString () {
    return printName;
  }
}
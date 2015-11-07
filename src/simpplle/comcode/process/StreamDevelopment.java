package simpplle.comcode.process;

import simpplle.comcode.Process;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class deals with stream development, a type of process. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 */

public class StreamDevelopment extends Process {
  private static final String printName = "STREAM-DEVELOPMENT";

  /**
   * Constructor for stream development.  Inherits from Process superclass and initializes spreading for false and
   * sets default visible columns for row and probability.  
   */
  public StreamDevelopment() {
    super();

    spreading   = false;
    description = "STREAM DEVELOPMENT";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }

  public String toString() { return printName; }

  private int doProbabilityCommon(simpplle.comcode.element.Evu evu) {
    return 0;
  }
/**
 * Westside Region 1 probability
 * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
 */
  protected int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Eastside Region 1 probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Sierra Nevada probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Southern California  probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Gila probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * South Central Alaska probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }

  
  private boolean doSpreadCommon(simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }

  /**
   * Westside Region 1 spread
   * Returns boolean the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  
  protected boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Eastside Region 1 spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Sierra Nevada spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Southern California spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Gila spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * South Central Alaska spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }

}




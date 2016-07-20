/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

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

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  public String toString() { return printName; }

  private int doProbabilityCommon(Evu evu) {
    return 0;
  }
/**
 * Westside Region 1 probability
 * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
 */
  protected int doProbability (WestsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Eastside Region 1 probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (EastsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Sierra Nevada probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (SierraNevada       zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Southern California  probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * Gila probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (Gila               zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * South Central Alaska probability
   * Returns the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  protected int doProbability (SouthCentralAlaska zone, Evu evu) {
    return doProbabilityCommon(evu);
  }

  
  private boolean doSpreadCommon(Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Westside Region 1 spread
   * Returns boolean the result of call to doProbablityCommon (Evu evu).  This inherits from the doProbablity class.  
   */
  
  protected boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Eastside Region 1 spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Sierra Nevada spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Southern California spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * Gila spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (Gila zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * South Central Alaska spread
   * Returns boolean of call to doSpreadCommon (Evu evu).  This inherits from the doSpreadCommon class.  
   */
  protected boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }

}




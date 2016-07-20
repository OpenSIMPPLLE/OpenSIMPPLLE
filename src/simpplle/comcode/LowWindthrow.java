/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Low Windthrow, a type of Process.  
 * It is done in South Central Alaska and must be locked-in to be used. 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */

public class LowWindthrow extends Process {
  private static final String printName = "LOW-WINDTHROW";
  public LowWindthrow() {
    super();

    spreading   = false;
    description = "Low Windthrow";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  /**
   * Westside Region 1 does not have this process, doProbability returns a 
   */
  public int doProbability (WestsideRegionOne zone, Evu evu)  { return 0; }
  /**
   * Eastside Region 1 does not have this process, doProbability returns a 
   */
  public int doProbability (EastsideRegionOne zone, Evu evu)  { return 0; }
  /**
   * Sierra Nevada does not have this process, doProbability returns a 
   */
  public int doProbability (SierraNevada zone, Evu evu)       { return 0; }
  /**
   * Southern California does not have this process, doProbability returns a 
   */
  public int doProbability (SouthernCalifornia zone, Evu evu) { return 0; }
  /**
   * Gila does not have this process, doProbability returns a 
   */
  public int doProbability (Gila zone, Evu evu)               { return 0; }

  public int doProbability (SouthCentralAlaska zone, Evu evu) {
    return doProbability(evu);
  }

 /**
  * Westside Region 1 does not have this process, doSpread returns false.
  */
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
/**
 * Eastside Region 1 does not have this process, doSpread returns false.
 */
  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  /**
   * Sierra Nevada does not have this process, doSpread returns false.
   */
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu)       { return false; }
  /**
   * Southern California does not have this process, doSpread returns false.
   */
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * Gila does not have this process, doSpread returns false.
   */
  public boolean doSpread (Gila zone, Evu fromEvu, Evu evu)               { return false; }

  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return false;
  }
/**
 * output "LOW-WINDTHROW"
 */
  public String toString () {
    return printName;
  }

}



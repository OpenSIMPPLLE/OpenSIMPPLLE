/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class contains methods for Light Spruce Beetles, a type of Process
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class LightSb extends Process {
  private static final String printName = "LIGHT-SB";
  /**
   * Constructor for Light Spruce Beetle.  Inherits from Process superclass, and initializes spreading to false, sets description and initializes default visible columns
   */
  public LightSb() {
    super();

    spreading   = false;
    description = "Light Spruce Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

/**
 * Westside Region 1 does not have this process so returns a 0.
 */
  public int doProbability (WestsideRegionOne zone, Evu evu)  { return 0; }
  /**
   * Eastside Region 1 does not have this process so returns a 0.
   */
  public int doProbability (EastsideRegionOne zone, Evu evu)  { return 0; }
  /**
   * Sierra Nevada does not have this process so returns a 0.
   */
  public int doProbability (SierraNevada zone, Evu evu)       { return 0; }
  /**
   * Southern California does not have this process so returns a 0.
   */
  public int doProbability (SouthernCalifornia zone, Evu evu) { return 0; }
  /**
   * Gila does not have this process so returns a 0.
   */
  public int doProbability (Gila zone, Evu evu)               { return 0; }

  public int doProbability (SouthCentralAlaska zone, Evu evu) {
    SpruceBeetleRisk.compute(evu);
    return SpruceBeetleRisk.getLowProbability();
  }

 /**
  * Westside Region 1 does not have this process so returns false by default.
  */
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  /**
   *Eastside Region 1 does not have this process so returns false by default.
   */
  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  /**
   * Sierra Nevada does not have this process so returns false by default.
   */
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu)       { return false; }
  /**
   * Southern California does not have this process so returns false by default.
   */
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * Gila does not have this process so returns false by default.
   */
  public boolean doSpread (Gila zone, Evu fromEvu, Evu evu)               { return false; }

  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return false;
  }
/**
 * outputs "LIGHT-SB"
 */
  public String toString () {
    return printName;
  }

}
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
 * <p>This class contains methods for Medium Spruce Beetle, a type of Process.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */

public class MediumSb extends Process {
  private static final String printName = "MEDIUM-SB";
  /**
   * Constructor for Medium Spruce Beetle.  Inherits from Process superclass and initializes spreading to false, description and the default visible columns
   */
  public MediumSb() {
    super();

    spreading   = false;
    description = "Medium Spruce Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  /**
   * Westside Region 1 does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, Evu evu)  { return 0; }
  /**
   * Eastside Region 1 does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, Evu evu)  { return 0; }
  /**
   * Sierra Nevada does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, Evu evu)       { return 0; }
  /**
   * Southern California does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, Evu evu) { return 0; }
  /**
   * Gila does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, Evu evu)               { return 0; }

  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, Evu evu) {
    return SpruceBeetleRisk.getModerateProbability();
  }

  /**
   * Westside Region 1 does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  /**
   * Eastside Region 1 does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  /**
   * Sierra Nevada does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, Evu fromEvu, Evu evu)       { return false; }
  /**
   * Southern California does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * Gila does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, Evu fromEvu, Evu evu)               { return false; }

  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return false;
  }
/**
 * outputs "MEDIUM-SB"
 */
  public String toString () {
    return printName;
  }
}




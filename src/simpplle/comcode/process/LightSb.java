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
 * <p>This class contains methods for Light Spruce Beetles, a type of Process
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *
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

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }

/**
 * Westside Region 1 does not have this process so returns a 0.
 */
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu)  { return 0; }
  /**
   * Eastside Region 1 does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu)  { return 0; }
  /**
   * Sierra Nevada does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu)       { return 0; }
  /**
   * Southern California does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) { return 0; }
  /**
   * Gila does not have this process so returns a 0.
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu)               { return 0; }

  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) {
    SpruceBeetleRisk.compute(evu);
    return SpruceBeetleRisk.getLowProbability();
  }

 /**
  * Westside Region 1 does not have this process so returns false by default.
  */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)  { return false; }
  /**
   *Eastside Region 1 does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)  { return false; }
  /**
   * Sierra Nevada does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)       { return false; }
  /**
   * Southern California does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }
  /**
   * Gila does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)               { return false; }

  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }
/**
 * outputs "LIGHT-SB"
 */
  public String toString () {
    return printName;
  }

}

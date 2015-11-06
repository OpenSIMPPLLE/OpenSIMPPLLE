
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
 * <p>This class defines the Winter Drought, a type of Process
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */
public class WinterDrought extends Process {
  private static final String printName = "WINTER-DROUGHT";
  public WinterDrought () {
    super();

    spreading   = false;
    description = "Winter Drought";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, Evu evu) {
    return doProbability(evu);
  }

  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, Evu evu) {
    return doProbability(evu);
  }
  public int doProbability (simpplle.comcode.zone.Teton zone, Evu evu) {
    return doProbability(evu);
  }
  public int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, Evu evu) {
    return doProbability(evu);
  }

  /**
   * Winter Drought does not occur in Sierra Nevada so returns 0
   */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, Evu evu) {
    return 0;
  }

  /**
   * Winter Drought does not occur in Southern California,  so returns 0
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, Evu evu) {
    return 0;
  }

  /**
   * Winter Drought does not occur in Gila, so returns 0 
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, Evu evu) { return 0; }
  /**
   * Winter Drought does not occur in South Central Alaska so returns 0
   */
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }

  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  /**
   * Sierra Nevada does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Southern California does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Gila does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * South Central Alaska does not have this process so returns false by default.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  /**
   * outputs "WINTER-DROUGHT"
   */
  public String toString () {
    return printName;
  }

}



package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;


/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods for Cold Injury Bark Beetles, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.Process
 */

public class ColdInjuryBarkBeetles extends Process {
  private static final String printName = "COLD-INJURY-BARK-BEETLES";
  /**
   * constructor.  References the Process superclass.
   */
  public ColdInjuryBarkBeetles () {
    super();

    spreading   = false;
    description = "Cold Injury Bark Beetles";

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
   * Cold Injury Bark Beetles does not occur in Sierra Nevada, so returns 0.
   */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, Evu evu) {
    return 0;
  }

  /**
   * Cold Injury Bark Beetles does not occur in Southern California, so returns 0.
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, Evu evu) {
    return 0;
  }

  /**
   * Cold Injury Bark Beetles does not occur in Gila, so returns 0.
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, Evu evu) { return 0; }
  /**
   * Cold Injury Bark Beetles does not occur in South Central Alaska, so returns 0.
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
  public boolean doSpread (simpplle.comcode.zone.Teton zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }
  public boolean doSpread (simpplle.comcode.zone.NorthernCentralRockies zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  /**
   * Cold Injury Bark Beetles does not occur in Sierra Nevada, so returns false meaning no spread calculation.
   */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Cold Injury Bark Beetles does not occur in Southern California, so returns false meaning no spread calculation.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Cold Injury Bark Beetles does not occur in Gila, so returns false meaning no spread calculation.
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * Cold Injury Bark Beetles does not occur in South Central Alaska, so returns false meaning no spread calculation.
   */
  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  /**
   * outputs "COLD-INJURY-BARK-BEETLES"
   */
  public String toString () {
    return printName;
  }

}



package simpplle.comcode.process;

import simpplle.comcode.Process;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods for Bark Beetles, a type of Process 
 * 
 * @author Documentation by Brian Losi
 * <p> Original source code authorship: Kirk A. Moeller
 * 
 *
 * @see simpplle.comcode.Process
 */
public class BarkBeetles extends Process {
  private static final String printName = "BARK-BEETLES";
  public BarkBeetles () {
    super();

    spreading   = false;
    description = "Bark Beetles";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
 * for a specified Evu.
 */
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbability(evu);
  }
  /**
   * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
   * for a specified Evu.
   */
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbability(evu);
  }
  /**
   * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
   * for a specified Evu.
   */
  public int doProbability (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu evu) {
    return doProbability(evu);
  }
  /**
   * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
   * for a specified Evu.
   */
  public int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu evu) {
    return doProbability(evu);
  }

  /**
   * does not occur in Sierra Nevada zone - returns 0 
   */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) { return 0; }
  /**
   * does not occur in Southern California zone - returns 0 
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) { return 0; }
  /**
   * does not occur in Gila zone - returns 0 
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu) { return 0; }
  /**
   * does not occur in South Central Alaska zone - returns 0 
   */
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }
/**
 * Returns false.
 */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }
  /**
   * Returns false.
   */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }
  /**
   * Returns false.
   */
  public boolean doSpread (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }
  /**
   * Returns false.
   */
  public boolean doSpread (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }


  /**
   * bark beetles do not occur in SierraNevada zone so no spread- returns false
   */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }
  /**
   * bark beetles do not occur in Southern California zone so no spread- returns false 
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }
  /**
   * bark beetles do not occur in Gila zone so no spread- returns false
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }
  /**
   * bark beetles do not occur in South Central Alaska zone so no spread- returns false
   */
  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }

  /**
   * to string returns "BARK-BEETLES"
   */
  
  public String toString () {
    return printName;
  }
}


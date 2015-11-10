
package simpplle.comcode;

import java.awt.Color;

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

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
 * for a specified Evu.
 */
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    return doProbability(evu);
  }
  /**
   * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
   * for a specified Evu.
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return doProbability(evu);
  }
  /**
   * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
   * for a specified Evu.
   */
  public int doProbability (Teton zone, Evu evu) {
    return doProbability(evu);
  }
  /**
   * Passes to the doProbability method of simpplle.comcode.Process.doProbability(Evu evu) where it will do the Bark Beetle probability 
   * for a specified Evu.
   */
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    return doProbability(evu);
  }

  /**
   * does not occur in Sierra Nevada zone - returns 0 
   */
  public int doProbability (SierraNevada       zone, Evu evu) { return 0; }
  /**
   * does not occur in Southern California zone - returns 0 
   */
  public int doProbability (SouthernCalifornia zone, Evu evu) { return 0; }
  /**
   * does not occur in Gila zone - returns 0 
   */
  public int doProbability (Gila               zone, Evu evu) { return 0; }
  /**
   * does not occur in South Central Alaska zone - returns 0 
   */
  public int doProbability (SouthCentralAlaska zone, Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }
/**
 * Returns false.
 */
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }
  /**
   * Returns false.
   */
  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }
  /**
   * Returns false.
   */
  public boolean doSpread (Teton zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }
  /**
   * Returns false.
   */
  public boolean doSpread (NorthernCentralRockies zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }


  /**
   * bark beetles do not occur in SierraNevada zone so no spread- returns false
   */
  public boolean doSpread (SierraNevada       zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * bark beetles do not occur in Southern California zone so no spread- returns false 
   */
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * bark beetles do not occur in Gila zone so no spread- returns false
   */
  public boolean doSpread (Gila               zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * bark beetles do not occur in South Central Alaska zone so no spread- returns false
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  /**
   * to string returns "BARK-BEETLES"
   */
  
  public String toString () {
    return printName;
  }
}


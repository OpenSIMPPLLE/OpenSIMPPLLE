package simpplle.comcode.process;

import simpplle.comcode.Process;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Severe Vegetative Disturbance, a type of process.  
 * This can occur in all zones and therefore has methods for all.  BUT Probability for this is not applicable.  
 * <p>A severe disturbance is an unpredicted event. 
 * Therefore the doProbability common which all zones refer to returns 0.  Also a severe vegetative disturbance is not going to spread.  
 * So all zones return false for doSpreadCommon
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 *
 *@see simpplle.comcode.Process
 *
 */

public class SevereVegDisturb extends Process {
  private static final String printName = "SEVERE-VEG-DISTURB";
/**
 * Constructor for Severe Vegetative Disturbance.  Inherits from Process superclass and initializes spreading to false, and default visible columns row and probability columns.  
 */
  public SevereVegDisturb() {
    super();

    spreading   = false;
    description = "SEVERE VEG DISTURB";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * This is an event that cannot be modeled using probabilistic logic.  Therefore returns 0.
 * @param evu
 * @return 0 - all methods in this class return this for probability
 */
  private int doProbabilityCommon(simpplle.comcode.element.Evu evu) {
    return 0;
  }
/**
 * @return 0
 */
  protected int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
/**
 * 
 * @param fromEvu originating evu
 * @param evu destination evu
 * @return false for all class as severe disturbances do not spread
 */
  private boolean doSpreadCommon(simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }
/**
 * @return false
 */
  protected boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * outputs  "SEVERE-VEG-DISTURB"
   */
    public String toString() { return printName; }


}

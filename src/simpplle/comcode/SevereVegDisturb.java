package simpplle.comcode;

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

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * This is an event that cannot be modeled using probabilistic logic.  Therefore returns 0.
 * @param evu
 * @return 0 - all methods in this class return this for probability
 */
  private int doProbabilityCommon(Evu evu) {
    return 0;
  }
/**
 * @return 0
 */
  protected int doProbability (WestsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (EastsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (Teton  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (NorthernCentralRockies  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (SierraNevada       zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (Gila               zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  /**
   * @return 0
   */
  protected int doProbability (SouthCentralAlaska zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
/**
 * 
 * @param fromEvu originating evu
 * @param evu destination evu
 * @return false for all class as severe disturbances do not spread
 */
  private boolean doSpreadCommon(Evu fromEvu, Evu evu) {
    return false;
  }
/**
 * @return false
 */
  protected boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (Teton zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (NorthernCentralRockies zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (Gila zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * @return false
   */
  protected boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * outputs  "SEVERE-VEG-DISTURB"
   */
    public String toString() { return printName; }


}

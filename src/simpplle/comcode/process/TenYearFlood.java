package simpplle.comcode.process;

import simpplle.comcode.Process;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Ten Year Flood, a type of process.  
 * Since this method like many of the flood processes can occur in any zone, there 
 * are methods for all regional zones. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 * @see simpplle.comcode.Process
 */

public class TenYearFlood extends Process {
  private static final String printName = "TEN-YEAR-FLOOD";
/**
 * Constructor for ten year flood.  Inherits from process superclass and initializes spreading to false and sets default visible columns for row and probability.  
 */
  public TenYearFlood() {
    super();

    spreading   = false;
    description = "TEN YEAR FLOOD";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }

  

  private int doProbabilityCommon(simpplle.comcode.element.Evu evu) {
    return 0;
  }

  protected int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }

  private boolean doSpreadCommon(simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }

  protected boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  /**
   * outputs  "TEN-YEAR-FLOOD"
   */
  public String toString() { return printName; }

}

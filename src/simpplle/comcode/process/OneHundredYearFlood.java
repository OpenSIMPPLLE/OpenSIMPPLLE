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
 *<p>This class defines the zone for One Hundred Year Flood, a type of Process.
 *
 *<p>Note: this process can occurr in any region therefore there are doProbabilityCommon and doSpreadCommon methods
 *plus overloaded doProbability and doSpread methods for each region.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */


public class OneHundredYearFlood extends Process {
  private static final String printName = "ONE-HUNDRED-YEAR-FLOOD";

  public OneHundredYearFlood() {
    super();

    spreading   = false;
    description = "ONE HUNDRED YEAR FLOOD";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }

  public String toString() { return printName; }

  private int doProbabilityCommon(simpplle.comcode.element.Evu evu) {
    return 0;
  }

  protected int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu evu) {
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
  protected boolean doSpread (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
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

}

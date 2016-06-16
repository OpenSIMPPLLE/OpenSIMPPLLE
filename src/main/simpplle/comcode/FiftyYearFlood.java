package simpplle.comcode;

import java.awt.Color;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains method for Fifty Year Flood, a type of Process.  Since it can occur in all regions and all zones, the doProbability and doSpread methods 
 * all return a value.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */

public class FiftyYearFlood extends Process {
  private static final String printName = "FIFTY-YEAR-FLOOD";

  public FiftyYearFlood() {
    super();

    spreading   = false;
    description = "FIFTY YEAR FLOOD";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  public String toString() { return printName; }

  private int doProbabilityCommon(Evu evu) {
    return 0;
  }

  protected int doProbability (WestsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (EastsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (Teton  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (NorthernCentralRockies  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (SierraNevada       zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (Gila               zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (SouthCentralAlaska zone, Evu evu) {
    return doProbabilityCommon(evu);
  }

  private boolean doSpreadCommon(Evu fromEvu, Evu evu) {
    return false;
  }

  protected boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (Teton zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (NorthernCentralRockies zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (Gila zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }

}

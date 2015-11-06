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
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class WildlifeBrowsing extends Process {
  private static final String printName = "WILDLIFE-BROWSING";
  public WildlifeBrowsing() {
    super();

    spreading   = false;
    description = "Wildlife Browsing";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  // These zones don't have this process
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, Evu evu)  { return 0; }
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, Evu evu)  { return 0; }
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, Evu evu)       { return 0; }
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, Evu evu) { return 0; }
  public int doProbability (simpplle.comcode.zone.Gila zone, Evu evu)               { return 0; }

  // Occur's here but can only be occur via user locking-in.
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, Evu evu) { return 0; }

  // These zones don't have this process
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, Evu fromEvu, Evu evu)       { return false; }
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, Evu fromEvu, Evu evu) { return false; }
  public boolean doSpread (simpplle.comcode.zone.Gila zone, Evu fromEvu, Evu evu)               { return false; }

  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public String toString () {
    return printName;
  }

}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 *
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
  public int doProbability (WestsideRegionOne zone, Evu evu)  { return 0; }
  public int doProbability (EastsideRegionOne zone, Evu evu)  { return 0; }
  public int doProbability (SierraNevada zone, Evu evu)       { return 0; }
  public int doProbability (SouthernCalifornia zone, Evu evu) { return 0; }
  public int doProbability (Gila zone, Evu evu)               { return 0; }

  // Occur's here but can only be occur via user locking-in.
  public int doProbability (SouthCentralAlaska zone, Evu evu) { return 0; }

  // These zones don't have this process
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu)  { return false; }
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu)       { return false; }
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) { return false; }
  public boolean doSpread (Gila zone, Evu fromEvu, Evu evu)               { return false; }

  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public String toString () {
    return printName;
  }

}

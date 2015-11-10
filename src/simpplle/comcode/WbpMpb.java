
package simpplle.comcode;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines methods for Mountain Pine Beetle in White Bark Pine, a type of Process.  Logic for 
 * mpb in whitebark pine is based on the degree of whitebark pine in the community, size class/structure, density and past fire processes.    
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */
public class WbpMpb extends Process {
  private static final String printName = "WBP-MPB";
  public WbpMpb () {
    super();

    spreading   = false;
    description = "WhiteBark Pine Mountain Pine Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SPECIES_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SIZE_CLASS_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.DENSITY_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  private int doProbabilityCommon (RegionalZone zone, Evu evu) {
    int         page = -1, row = -1, col = 0;
    int         cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();
    Density     density     = state.getVeg().getDensity();

    VegSimStateData priorState = evu.getState(cTime-1);
    if (priorState == null) { return 0; }

    ProcessType processType = evu.getState(cTime - 1).getProcess();

    if (species == Species.WB) {
      page = 0;
    }
    else if (species == Species.WB_ES_AF || species == Species.AL_WB_AF) {
      page = 1;
    }
    else {
      page = -1;
    }

    if (page != -1) {
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
          sizeClass == SizeClass.LMU) {
        row = 0;
      }
      else if (sizeClass == SizeClass.VERY_LARGE ||
               sizeClass == SizeClass.VLTS || sizeClass == SizeClass.VLMU) {
        row = 3;
      }
      else {
        row = -1;
      }
    }

    if (row != -1) {
      if (density == Density.TWO) {} // Don't need to change row.
      else if (density == Density.THREE) { row++; }
      else if (density == Density.FOUR)  {row += 2; }
      else { row = -1; }
    }

    if (row != -1) {
      if ((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
          (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) {
        col++;
      }
    }

    if (page != -1 && row != -1) {
      return getProbData(page,row,col);
    }
    else {
      return 0;
    }
  }

  public int doProbability (WestsideRegionOne zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }
  public int doProbability (Teton zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  // Does not occur
  public int doProbability (SierraNevada zone, Evu evu) {
    return 0;
  }

  // Does not occur
  public int doProbability (SouthernCalifornia zone, Evu evu) {
    return 0;
  }

  // These zones don't have this process
  public int doProbability (SouthCentralAlaska zone, Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }

  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  // Does not occur in Sierra Nevada
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

  // Does not occur in Southern California
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }

  // This Process does not occur in these zones.
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  public String toString () {
    return printName;
  }

}



package simpplle.comcode;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the Mountain Pine Beetle in White Pine, a type of Process.  The logic for this is based on 
 * the degree of white pine in the community, the size-class/structure, density and past fire processes.
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 * @since V2.3 SIMPPLLE
 *  
 * @see simpplle.comcode.Process
 */

public class WpMpb extends Process {
  private static final String printName = "WP-MPB";
  /**
   * Constructor for White Pine Mountain Pine Beetle.  Inherits from Process superclass and initializes spreading to false
   * and default visible columns on row, species, size class, density process, and probability.
   */
  public WpMpb () {
    super();

    spreading   = false;
    description = "White Pine Mountain Pine Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SPECIES_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SIZE_CLASS_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.DENSITY_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * Probability common method, based on process type, current simulation time step, species, size class, density, process type and a combination of species
 * @param zone Regional zone
 * @param evu 
 * @return probability 
 */
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

    if (species == Species.WP || species == Species.RRWP) {
      page = 0;
    }
    else if (species == Species.DF_WP   || species == Species.L_WP ||
             species == Species.DF_RRWP || species == Species.L_RRWP) {
      page = 1;
    }
    else if (species == Species.DF_WP_GF  || species == Species.L_DF_WP    ||
             species == Species.L_WP_GF   || species == Species.DF_RRWP_GF ||
             species == Species.L_DF_RRWP || species == Species.L_RRWP_GF) {
      page = 2;
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
      else if (density == Density.FOUR) { row += 2; }
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

 /**
  * White Pine Mountain Pine Beetle does not occur in Eastside Region 1 so returns 0
  */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return 0;
  }

  /**
   * White Pine Mountain Pine Beetle does not occur in Sierra Nevada,  so returns 0
   */
  public int doProbability (SierraNevada zone, Evu evu) {
    return 0;
  }

  /**
   * White Pine Mountain Pine Beetle does not occur in Southern California, so returns 0
   */
  public int doProbability (SouthernCalifornia zone, Evu evu) {
    return 0;
  }

  /**
   * White Pine Mountain Pine Beetle does not occur in South Central Alaska, so returns 0
   */
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

 /**
  * Sierra Nevada does not have this process so returns false by default.
  */
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

 /**
  * Southern California does not have this process so returns false by default.
  */
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * South Central Alaska does not have this process so returns false by default.
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  /**
   * outputs "WP-MPB"
   */
  public String toString () {
    return printName;
  }

}


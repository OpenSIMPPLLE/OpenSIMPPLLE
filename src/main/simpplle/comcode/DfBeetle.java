/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * Defines the Douglas-Fir Beetle class, a type of Process.  This logic uses a combination of the
 * abundance of Douglas-fir, size class/structure, density and the occurrence of past light (LSF)
 * or mixed severity fire (MSF).  No spread logic is used.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class DfBeetle extends Process {
  private static final String printName = "DF-BEETLE";
  
  /**
   * Constructor for DFBeetle.  Inherits from process superclass and sets the VisibleColumns 
   * 
   * @see simpplle.comcode.Process.Process()
   */
  public DfBeetle () {
    super();

    spreading   = false;
    description = "Douglas-Fir Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SPECIES_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SIZE_CLASS_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.DENSITY_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.PROCESS_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.TEMP_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.MOISTURE_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.ADJ_PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  /**
   * @return 0 as default 
   */
  protected int doProbability (Evu evu) {
    return 0;
  }
/**
 * 
 * @param zone Regional zone: Westside Region 1, Eastside Region 1, Teton, Northern Central Rockies, Colorado Front Range or Colorado Plateau 
 * @param evu Evaluated Vegetative Unit.  
 * @return probability data 
 */
  private int doProbabilityCommon (RegionalZone zone, Evu evu) {
    Species     species;
    SizeClass   sizeClass;
    Density     density;
    ProcessType processType;
    int         page = -1, row = -1, col = 0;
    int         prob = 0;
    int         cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    VegSimStateData state = evu.getState(cTime-1);

    processType = (state != null) ? state.getProcess() : null;

    state = evu.getState();
    if (state == null) { return 0; }
    species     = state.getVeg().getSpecies();
    sizeClass   = state.getVeg().getSizeClass();
    density     = state.getVeg().getDensity();

    if (species == Species.DF) {
      page = 0;
    }
    else if (species == Species.PP_DF || species == Species.L_DF || species == Species.DF_GF ||
             species == Species.DF_LP || species == Species.DF_WP) {
      page = 1;
    }
    else if (species == Species.L_DF_PP  || species == Species.L_DF_GF  ||
             species == Species.DF_PP_GF || species == Species.DF_PP_LP ||
             species == Species.L_DF_LP  || species == Species.DF_WP_GF ||
             species == Species.L_DF_WP) {
      page = 2;
    }
    else { page = -1; }

    if (page != -1) {
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
          sizeClass == SizeClass.LMU) {
        row = 0;
      }
      else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLTS ||
               sizeClass == SizeClass.VLMU) {
        row = 3;
      }
      else { row = -1;}
    }

    if (row != -1) {
      // Density.TWO the row doesn't change.
      if (density == Density.THREE) { row++; }
      else if (density == Density.FOUR) { row += 2; }
      else { row = -1; }
    }

    if (row != -1) {
      if ((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
          (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) {
        col++;
      }
    }

    if (page == -1 || row == -1) {
      return 0;
    }
    else {
      return getProbData(page,row,col);
    }
  }
/**
 * Probability of DF Beetle in Westside Region 1
 * 
 */
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  /**
   * Probablity of DF Beetle in Eastside Region 1. 
   * If species is not DF Beetle and not Powderpost beetles or no df beetle  
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.DF && species != Species.PP_DF &&
        species != Species.DF_LP && species != Species.DF_PP_LP) {
      return 0;
    }
    else {
      return doProbabilityCommon(zone,evu);
    }
  }
  public int doProbability (Teton zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.DF && species != Species.PP_DF &&
        species != Species.DF_LP && species != Species.DF_PP_LP) {
      return 0;
    }
    else {
      return doProbabilityCommon(zone,evu);
    }
  }
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.DF && species != Species.PP_DF &&
        species != Species.DF_LP && species != Species.DF_PP_LP) {
      return 0;
    }
    else {
      return doProbabilityCommon(zone,evu);
    }
  }

  public int doProbability (ColoradoFrontRange zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species   species     = state.getVeg().getSpecies();
    SizeClass sizeClass   = state.getVeg().getSizeClass();
    Density   density     = state.getVeg().getDensity();

    int page = -1;
    int row  = -1;
    if (species == Species.PSME) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU) {
        row = 0;
      }
      else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) {
        row = 3;
      }
      else { return 0; }
    }
    else if (species.contains(Species.PSME)) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU) {
        row = 6;
      }
      else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) {
        row = 9;
      }
      else { return 0; }
    }
    else {
      return 0;
    }

    if (density == Density.ONE || density == Density.TWO) {
      row += 0;
    }
    else if (density == Density.THREE) {
      row += 1;
    }
    else if (density == Density.FOUR) {
      row += 2;
    }
    else {
      System.out.println("hello=" + density + "," + density.getValue());
      return 0;
    }

    int            cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    ProcessType    adjProcess;
    AdjacentData[] adjData = evu.getNeighborhood();
    boolean        isAdjSrf=false;

    for (AdjacentData neighbor : adjData) {
      if (neighbor != null) {
        VegSimStateData adjState = neighbor.evu.getState(cTime - 1);
        if (adjState == null) {
          continue;
        }
        adjProcess = adjState.getProcess();
        if (adjProcess == ProcessType.STAND_REPLACING_FIRE) {
          isAdjSrf = true;
          break;
        }
      }
    }

    boolean[] cols = new boolean[] { true, false, false, false, false};
    VegSimStateData priorState = evu.getState(cTime-1);
    ProcessType processType = (priorState != null ? priorState.getProcess() : null);

    cols[1] = isAdjSrf;
    cols[2] = (processType == ProcessType.MIXED_SEVERITY_FIRE || processType == ProcessType.LIGHT_SEVERITY_FIRE);
    cols[3] = (Simpplle.getClimate().getTemperature() == Temperature.WARMER &&
               Simpplle.getClimate().getMoisture()    == Moisture.DRIER);
    cols[4] = (processType == ProcessType.TUSSOCK_MOTH);

    int prob = getProbData(page,row,0);
    if (cols[1]) { prob += getProbData(page,row,1); }
    if (cols[2]) { prob += getProbData(page,row,2); }
    if (cols[3]) { prob += getProbData(page,row,3); }
    if (cols[4]) { prob += getProbData(page,row,4); }

    return prob;
  }

  public int doProbability (ColoradoPlateau zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species   species     = state.getVeg().getSpecies();
    SizeClass sizeClass   = state.getVeg().getSizeClass();
    Density   density     = state.getVeg().getDensity();

    int page = -1;
    int row  = -1;
    if (species == Species.PSME) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU) {
        row = 0;
      }
      else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) {
        row = 3;
      }
      else { return 0; }
    }
    else if (species.contains(Species.PSME)) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU) {
        row = 6;
      }
      else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) {
        row = 9;
      }
      else { return 0; }
    }
    else {
      return 0;
    }

    if (density == Density.ONE || density == Density.TWO) {
      row += 0;
    }
    else if (density == Density.THREE) {
      row += 1;
    }
    else if (density == Density.FOUR) {
      row += 2;
    }
    else {
      System.out.println("hello=" + density + "," + density.getValue());
      return 0;
    }

    int            cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    ProcessType    adjProcess;
    AdjacentData[] adjData = evu.getNeighborhood();
    boolean        isAdjSrf=false;

    for (AdjacentData neighbor : adjData) {
      if (neighbor != null) {
        VegSimStateData adjState = neighbor.evu.getState(cTime - 1);
        if (adjState == null) {
          continue;
        }

        adjProcess = adjState.getProcess();
        if (adjProcess == ProcessType.STAND_REPLACING_FIRE) {
          isAdjSrf = true;
          break;
        }
      }
    }

    boolean[] cols = new boolean[] { true, false, false, false, false};

    VegSimStateData priorState = evu.getState(cTime-1);
    ProcessType processType = (priorState != null ? priorState.getProcess() : null);

    cols[1] = isAdjSrf;
    cols[2] = (processType == ProcessType.MIXED_SEVERITY_FIRE || processType == ProcessType.LIGHT_SEVERITY_FIRE);
    cols[3] = (Simpplle.getClimate().getTemperature() == Temperature.WARMER &&
               Simpplle.getClimate().getMoisture()    == Moisture.DRIER);
    cols[4] = (processType == ProcessType.TUSSOCK_MOTH);

    int prob = getProbData(page,row,0);
    if (cols[1]) { prob += getProbData(page,row,1); }
    if (cols[2]) { prob += getProbData(page,row,2); }
    if (cols[3]) { prob += getProbData(page,row,3); }
    if (cols[4]) { prob += getProbData(page,row,4); }

    return prob;
  }

/**
 * Outputs "DF-BEETLE"
 */
  public String toString () {
    return printName;
  }

}


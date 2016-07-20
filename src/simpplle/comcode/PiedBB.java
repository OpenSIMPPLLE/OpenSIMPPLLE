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
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines methods for Pinyon Pine Bark Beetle, a type of Process.   
 * Pinyon Pine Bark Beetle only has methods to calculate probability for ColoradoFrontRange zone and ColoradoPlateau zone.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */


public class PiedBB extends Process {
  private static final String printName = "PIED-BB";
  /**
   * Constructor for Pinyon Pine Bark Beetle.  Inherits from Process superclass and initializes spreading for false and sets the base logic 
   * default visible columns 
   * for species, size class, density, process, temperature, moisture, adjacent process, and probability
   */
  public PiedBB() {
    super();

    spreading   = false;
    description = "Pinyon Pine Bark Beetle";

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

  protected int doProbability (Evu evu) {
    return 0;
  }
/**
 * doProbability method for Colorado Front range.  Probability is affected by size class whether large, medium, or very large and density
 */
  public int doProbability (ColoradoFrontRange zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();
    Density     density     = state.getVeg().getDensity();

    int page = -1;
    int row  = -1;
    if (species == Species.PIED) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
          sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU) {
        row = 0;
      }
      else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) {
        row = 3;
      }
      else { return 0; }
    }
    else if (species.contains(Species.PIED)) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
          sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU) {
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
      return 0;
    }

    int            cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    ProcessType    adjProcess;
    AdjacentData[] adjData = evu.getAdjacentData();
    boolean        isAdjSrf=false;

    for (int i=0; i<adjData.length; i++) {
      VegSimStateData adjState = adjData[i].evu.getState(cTime-1);
      if (adjState == null) { continue; }
      adjProcess = adjState.getProcess();
      if (adjProcess == ProcessType.STAND_REPLACING_FIRE) {
        isAdjSrf = true;
        break;
      }
    }

    boolean[] cols = new boolean[] { true, false, false, false};

    VegSimStateData priorState = evu.getState(cTime-1);
    ProcessType processType = (priorState != null ? priorState.getProcess() : ProcessType.NONE);

    Climate.Season season = Simpplle.getCurrentSimulation().getCurrentSeason();

    cols[1] = isAdjSrf;
    cols[2] = (processType == ProcessType.MIXED_SEVERITY_FIRE || processType == ProcessType.LIGHT_SEVERITY_FIRE);
    cols[3] = (Simpplle.getClimate().getTemperature(season) == Climate.WARMER &&
               Simpplle.getClimate().getMoisture(season)    == Climate.DRIER);

    int prob = getProbData(page,row,0);
    if (cols[1]) { prob += getProbData(page,row,1); }
    if (cols[2]) { prob += getProbData(page,row,2); }
    if (cols[3]) { prob += getProbData(page,row,3); }

    return prob;
  }
  /**
   * doProbability method for Colorado Plateau.  Probability is affected by size class whether large, medium, or very large and density
   */
  public int doProbability (ColoradoPlateau zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();
    Density     density     = state.getVeg().getDensity();

    int page = -1;
    int row  = -1;
    if (species == Species.PIED) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
          sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU) {
        row = 0;
      }
      else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) {
        row = 3;
      }
      else { return 0; }
    }
    else if (species.contains(Species.PIED)) {
      page = 0;
      if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
          sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU) {
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
      return 0;
    }

    int            cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    ProcessType    adjProcess;
    AdjacentData[] adjData = evu.getAdjacentData();
    boolean        isAdjSrf=false;

    for (int i=0; i<adjData.length; i++) {
      VegSimStateData adjState = adjData[i].evu.getState(cTime-1);
      if (adjState == null) { continue; }
      adjProcess = adjState.getProcess();
      if (adjProcess == ProcessType.STAND_REPLACING_FIRE) {
        isAdjSrf = true;
        break;
      }
    }

    boolean[] cols = new boolean[] { true, false, false, false};

    VegSimStateData priorState = evu.getState(cTime-1);
    ProcessType processType = (priorState != null ? priorState.getProcess() : ProcessType.NONE);

    Climate.Season season = Simpplle.getCurrentSimulation().getCurrentSeason();

    cols[1] = isAdjSrf;
    cols[2] = (processType == ProcessType.MIXED_SEVERITY_FIRE || processType == ProcessType.LIGHT_SEVERITY_FIRE);
    cols[3] = (Simpplle.getClimate().getTemperature(season) == Climate.WARMER &&
               Simpplle.getClimate().getMoisture(season)    == Climate.DRIER);

    int prob = getProbData(page,row,0);
    if (cols[1]) { prob += getProbData(page,row,1); }
    if (cols[2]) { prob += getProbData(page,row,2); }
    if (cols[3]) { prob += getProbData(page,row,3); }

    return prob;
  }
/**
 * returns "PIED-BB"
 */
  public String toString () {
    return printName;
  }

}

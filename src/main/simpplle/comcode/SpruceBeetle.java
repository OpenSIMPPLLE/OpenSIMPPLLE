
package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>Defines the SpruceBeetle class, a type of Process.  The logic is based on habitat type groups, degree of presence of 
 * susceptible species, size-class and if a past light or mixed severity fire has occurred.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */


public class SpruceBeetle extends Process {
  private static final String printName = "SPRUCE-BEETLE";
  public SpruceBeetle () {
    super();

    spreading   = false;
    description = "Spruce Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SPECIES_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SIZE_CLASS_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  private int doProbabilityCommon (RegionalZone zone, Evu evu) {
    int         page = 0, row = -1, col = 0;
    int         cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    HabitatTypeGroupType groupType;

    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();
    groupType   = evu.getHabitatTypeGroup().getType();

    VegSimStateData priorState = evu.getState(cTime-1);
    if (priorState == null) { return 0; }
    ProcessType processType = priorState.getProcess();

    if (groupType.equals(HabitatTypeGroupType.D2) &&
        (species == Species.ES    || species == Species.ES_AF ||
         species == Species.DF_ES || species == Species.L_ES) &&
        (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS        ||
         sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
         sizeClass == SizeClass. VLTS || sizeClass == SizeClass.VLMU)) {
      row = 0;
    }
    else if (groupType.equals(HabitatTypeGroupType.D3) &&
             (species == Species.ES || species == Species.ES_AF ||
              species == Species.L_ES) &&
             (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS        ||
              sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
              sizeClass == SizeClass. VLTS || sizeClass == SizeClass.VLMU)) {
      row = 1;
    }
    else if (groupType.equals(HabitatTypeGroupType.E1) &&
             (species == Species.ES || species == Species.ES_AF ||
              species == Species.L_ES) &&
             (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS        ||
              sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
              sizeClass == SizeClass. VLTS || sizeClass == SizeClass.VLMU)) {
      row = 2;
    }
    else if (groupType.equals(HabitatTypeGroupType.F1) &&
             (species == Species.ES || species == Species.ES_AF ||
              species == Species.L_ES) &&
             (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS        ||
              sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
              sizeClass == SizeClass. VLTS || sizeClass == SizeClass.VLMU)) {
      row = 3;
    }
    else { row = -1; }

    if (row != -1) {
      if ((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
          (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) {
        col++;
      }
    }

    if (row != -1) {
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
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.ES    && species != Species.ES_AF &&
        species != Species.DF_ES && species != Species.L_ES) {
      return 0;
    }
    else {
      return doProbabilityCommon(zone,evu);
    }
  }
  public int doProbability (Teton zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.ES    && species != Species.ES_AF &&
        species != Species.DF_ES && species != Species.L_ES) {
      return 0;
    }
    else {
      return doProbabilityCommon(zone,evu);
    }
  }
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.ES    && species != Species.ES_AF &&
        species != Species.DF_ES && species != Species.L_ES) {
      return 0;
    }
    else {
      return doProbabilityCommon(zone,evu);
    }
  }

  public int doProbability (ColoradoFrontRange zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();

    if (species.contains(Species.PIEN) == false) {
      return 0;
    }

    if (sizeClass != SizeClass.LARGE && sizeClass != SizeClass.LMU &&
        sizeClass != SizeClass.VERY_LARGE && sizeClass != SizeClass.VLMU) {
      return 0;
    }

    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    VegSimStateData priorState = evu.getState(cTime-1);
    if (priorState == null) { return 0; }
    ProcessType process = priorState.getProcess();

    int col = 0;
    if ((process != ProcessType.LIGHT_SEVERITY_FIRE) &&
        (process != ProcessType.MIXED_SEVERITY_FIRE)) {
      col++;
    }

    return getProbData(0,0,col);
  }
  public int doProbability (ColoradoPlateau zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();

    if (species.contains(Species.PIEN) == false) {
      return 0;
    }

    if (sizeClass != SizeClass.LARGE && sizeClass != SizeClass.LMU &&
        sizeClass != SizeClass.VERY_LARGE && sizeClass != SizeClass.VLMU) {
      return 0;
    }

    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    VegSimStateData priorState = evu.getState(cTime-1);
    if (priorState == null) { return 0; }
    ProcessType process = priorState.getProcess();

    int col = 0;
    if ((process != ProcessType.LIGHT_SEVERITY_FIRE) &&
        (process != ProcessType.MIXED_SEVERITY_FIRE)) {
      col++;
    }

    return getProbData(0,0,col);
  }
/**
 * outputs "SPRUCE-BEETLE"
 */
  public String toString () {
    return printName;
  }

}


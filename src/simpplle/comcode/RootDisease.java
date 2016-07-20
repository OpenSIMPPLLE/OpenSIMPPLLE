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
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Root Disease, a type of process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *@see simpplle.comcode.Process
 *
 */
public class RootDisease extends Process {
  private static final String printName = "ROOT-DISEASE";
  public RootDisease () {
    super();

    spreading   = false;
    description = "Root Disease";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.ECO_GROUP_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SPECIES_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.TREATMENT_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  protected int doProbability (Evu evu) {
    return 0;
  }

  /**
   
   * Colorado will not have any root-disease.
   *   However, we will leave it as a valid process that the user can
   *   lock-in if they desire.
   *
   * @param evu the unit we are considering for this process
   * @return the probability of this process - will be zero unless Colorado decides to change;
   */
  public int doProbability (ColoradoFrontRange zone, Evu evu) {
    return 0;
  }
  /**
 
  * Colorado Plateau will not have any root-disease.
  *   However, it will be used as a valid process that the user can 
  *   lock-in if they desire.
  *
  * @param evu the unit we are considering for this process
  * @return the probability of this process - will be 0 since no RD;
  */
  public int doProbability (ColoradoPlateau zone, Evu evu) {
    return 0;
  }
  /**
   * Method to calculate probability for Root Disease in westside region one, based on species, associated land type, and habitat type group
   * and treatment type.
   */
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    int                  page = 0, row = -1, col = 0;
    int                  probability = 0;
    HabitatTypeGroupType groupType;
    Species              species;
    int                  cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    String assocLandtype = evu.getLandtype();
    if (assocLandtype != null && assocLandtype.equalsIgnoreCase("NS")) {
      return 0;
    }

    groupType = evu.getHabitatTypeGroup().getType();
    species   = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    Treatment     treat = evu.getTreatment(cTime - 1);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    if (groupType.equals(HabitatTypeGroupType.B1) &&
        (species == Species.DF    || species == Species.GF    ||
         species == Species.DF_GF || species == Species.DF_LP ||
         species == Species.DF_PP_GF)) {
      row = 0;
    }
    else if (groupType.equals(HabitatTypeGroupType.B2) &&
             (species == Species.DF    || species == Species.GF    ||
              species == Species.DF_GF || species == Species.DF_LP ||
              species == Species.DF_PP_GF)) {
      row = 1;
    }
    else if (groupType.equals(HabitatTypeGroupType.B3) &&
             (species == Species.DF    || species == Species.GF    ||
              species == Species.DF_GF || species == Species.DF_LP ||
              species == Species.DF_PP_GF)) {
      row = 2;
    }
    else if (groupType.equals(HabitatTypeGroupType.C1) &&
             (species == Species.DF       || species == Species.GF    ||
              species == Species.DF_GF    || species == Species.DF_LP ||
              species == Species.DF_PP_GF || species == Species.L_DF_GF)) {
      row = 3;
    }
    else if (groupType.equals(HabitatTypeGroupType.C2) &&
             (species == Species.DF       || species == Species.GF    ||
              species == Species.DF_GF    || species == Species.DF_LP ||
              species == Species.DF_PP_GF || species == Species.L_DF_GF)) {
      row = 4;
    }
    else if (groupType.equals(HabitatTypeGroupType.D1) &&
             (species == Species.DF    || species == Species.GF    ||
              species == Species.DF_GF || species == Species.DF_LP ||
              species == Species.L_DF_GF)) {
      row = 5;
    }
    else if (groupType.equals(HabitatTypeGroupType.D2) &&
             (species == Species.DF || species == Species.GF    ||
              species == Species.AF || species == Species.DF_GF ||
              species == Species.DF_LP)) {
      row = 6;
    }
    else if (groupType.equals(HabitatTypeGroupType.D3) &&
             (species == Species.DF    || species == Species.GF    ||
              species == Species.AF    || species == Species.DF_GF ||
              species == Species.DF_LP || species == Species.DF_AF ||
              species == Species.L_DF_AF)) {
      row = 7;
    }
    else if (groupType.equals(HabitatTypeGroupType.F1) &&
             (species == Species.DF    || species == Species.AF ||
              species == Species.DF_AF || species == Species.L_DF_AF)) {
      row = 8;
    }
    else {
      row = -1;
    }

    if (treatType == TreatmentType.PRECOMMERCIAL_THINNING   ||
        treatType == TreatmentType.COMMERCIAL_THINNING      ||
        treatType == TreatmentType.SANITATION_SALVAGE       ||
        treatType == TreatmentType.IMPROVEMENT_CUT          ||
        treatType == TreatmentType.GROUP_SELECTION_CUT      ||
        treatType == TreatmentType.INDIVIDUAL_SELECTION_CUT ||
        treatType == TreatmentType.LIBERATION_CUT) {
      col = 0;
    }
    else {
      col = 1;
    }

    if (page != -1 && row != -1 && col != -1) {
      probability = getProbData(page,row,col);
    }

    return probability;
  }
/**
 * @see simpplle.comcode.RootDisease.doProbabilityEastAndSimilar(Evu evu)
 */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return doProbabilityEastAndSimilar(evu);
  }
  /**
   * @see simpplle.comcode.RootDisease.doProbabilityEastAndSimilar(Evu evu)
   */
  public int doProbability (Teton zone, Evu evu) {
    return doProbabilityEastAndSimilar(evu);
  }
  /**
   * @see simpplle.comcode.RootDisease.doProbabilityEastAndSimilar(Evu evu)
   */
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    return doProbabilityEastAndSimilar(evu);
  }
  /**
   * method to evaluate Root Disease probabilities for Teton, Northern Centra lRockies, Eastside Region 1.  
   * This is based on species, treatment type, and habitat type group.  
   * @param evu existing vegetative unit being evaluated
   * @return probability of Root Disease 
   */
  private int doProbabilityEastAndSimilar (Evu evu) {
    int     page = 0, row = -1, col = 0;
    int     probability = 0;
    int     cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();
    Species              species   = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    Treatment     treat = evu.getTreatment(cTime - 1);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    if (species != Species.PP_DF && species != Species.DF_LP &&
        species != Species.DF_AF) {
      return 0;
    }

    if (groupType.equals(HabitatTypeGroupType.B1) &&
        (species == Species.DF    || species == Species.DF_AF ||
         species == Species.DF_LP || species == Species.PP_DF)) {
      row = 0;
    }
    else if (groupType.equals(HabitatTypeGroupType.B2) &&
             (species == Species.DF || species == Species.DF_LP ||
              species == Species.PP_DF)) {
      row = 1;
    }
    else if (groupType.equals(HabitatTypeGroupType.B3) &&
             (species == Species.DF || species == Species.DF_LP ||
              species == Species.PP_DF)) {
      row = 2;
    }
    else if (groupType.equals(HabitatTypeGroupType.C1) &&
             (species == Species.DF || species == Species.DF_LP ||
              species == Species.PP_DF)) {
      row = 3;
    }
    else if (groupType.equals(HabitatTypeGroupType.C2) &&
             (species == Species.DF || species == Species.DF_LP ||
              species == Species.PP_DF)) {
      row = 4;
    }
    else if (groupType.equals(HabitatTypeGroupType.D1) &&
             (species == Species.DF || species == Species.DF_LP)) {
      row = 5;
    }
    else if (groupType.equals(HabitatTypeGroupType.D2) &&
             (species == Species.DF    || species == Species.AF ||
              species == Species.DF_AF || species == Species.DF_LP)) {
      row = 6;
    }
    else if (groupType.equals(HabitatTypeGroupType.D3) &&
             (species == Species.DF    || species == Species.AF ||
              species == Species.DF_AF || species == Species.DF_LP)) {
      row = 7;
    }
    else if (groupType.equals(HabitatTypeGroupType.F1) &&
             (species == Species.DF || species == Species.AF ||
              species == Species.DF_AF)) {
      row = 8;
    }
    else {
      row = -1;
    }

    if (treatType != TreatmentType.PRECOMMERCIAL_THINNING   ||
        treatType != TreatmentType.COMMERCIAL_THINNING      ||
        treatType != TreatmentType.SANITATION_SALVAGE       ||
        treatType != TreatmentType.IMPROVEMENT_CUT          ||
        treatType != TreatmentType.GROUP_SELECTION_CUT      ||
        treatType != TreatmentType.INDIVIDUAL_SELECTION_CUT ||
        treatType != TreatmentType.LIBERATION_CUT) {
      col++;
    }

    if (page != -1 && row != -1 && col != -1) {
      probability = getProbData(page,row,col);
    }

    return probability;
  }
/**
 * Method to calculate Root Disease probability of Sierra Nevada.  This is based on vegetative state, species, treatment type, and habitat type group.  
 */
  public int doProbability (SierraNevada zone, Evu evu) {
    int       page = 0, row = -1, col = 0;
    int       probability = 0;
    int       cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    HabitatTypeGroupType groupType;

    groupType = evu.getHabitatTypeGroup().getType();

    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();

    Treatment     treat = evu.getTreatment(cTime - 1);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    if (species != Species.PC && species != Species.PP        &&
        species != Species.JP && species != Species.DF        &&
        species != Species.PJ && species != Species.LP        &&
        species != Species.WP && species != Species.WH        &&
        species != Species.MH && species != Species.WB        &&
        species != Species.LP_RF && species != Species.MC_DF  &&
        species != Species.MC_PP && species != Species.MC_WF  &&
        species != Species.MC_SEQ && species != Species.MC_IC &&
        species != Species.MC_RF) {
      return 0;
    }

    if (sizeClass != SizeClass.MU) {
      return 0;
    }

    if (density != Density.FOUR) {
      return 0;
    }

    if (groupType.equals(HabitatTypeGroupType.LM_X) &&
        (species == Species.PC || species == Species.PP ||
         species == Species.JP || species == Species.DF ||
         species == Species.PJ || species == Species.LP ||
         species == Species.WP || species == Species.WH ||
         species == Species.MH || species == Species.WB ||
         species == Species.LP_RF) &&
        (sizeClass == SizeClass.PTS || sizeClass == SizeClass.MTS  ||
         sizeClass == SizeClass.LTS || sizeClass == SizeClass.VLTS ||
         sizeClass == SizeClass.PMU || sizeClass == SizeClass.MMU  ||
         sizeClass == SizeClass.LMU || sizeClass == SizeClass.VLMU) &&
        (density == Density.FOUR)) {
      row = 0;
    }
    else if (groupType.equals(HabitatTypeGroupType.LM_X) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass.PTS  || sizeClass == SizeClass.MTS  ||
              sizeClass == SizeClass.LTS  || sizeClass == SizeClass.VLTS ||
              sizeClass == SizeClass.PMU  || sizeClass == SizeClass.MMU  ||
              sizeClass == SizeClass.LMU  || sizeClass == SizeClass.VLMU) &&
             (density == Density.FOUR)) {
      row = 1;
    }
    else if (groupType.equals(HabitatTypeGroupType.LM_M) &&
            (species == Species.PC || species == Species.PP ||
             species == Species.JP || species == Species.DF ||
             species == Species.PJ || species == Species.LP ||
             species == Species.WP || species == Species.WH ||
             species == Species.MH || species == Species.WB ||
             species == Species.LP_RF) &&
             (sizeClass == SizeClass.PTS  || sizeClass == SizeClass.MTS  ||
              sizeClass == SizeClass.LTS  || sizeClass == SizeClass.VLTS ||
              sizeClass == SizeClass.PMU  || sizeClass == SizeClass.MMU  ||
              sizeClass == SizeClass.LMU  || sizeClass == SizeClass.VLMU) &&
             (density == Density.FOUR)) {
      row = 2;
    }
    else if (groupType.equals(HabitatTypeGroupType.LM_M) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass.PTS || sizeClass == SizeClass.MTS  ||
              sizeClass == SizeClass.LTS || sizeClass == SizeClass.VLTS ||
              sizeClass == SizeClass.PMU || sizeClass == SizeClass.MMU  ||
              sizeClass == SizeClass.LMU  || sizeClass == SizeClass.VLMU) &&
             (density == Density.FOUR)) {
      row = 3;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_X) &&
            (species == Species.PC || species == Species.PP ||
             species == Species.JP || species == Species.DF ||
             species == Species.PJ || species == Species.LP ||
             species == Species.WP || species == Species.WH ||
             species == Species.MH || species == Species.WB ||
             species == Species.LP_RF) &&
             (sizeClass == SizeClass.PTS  || sizeClass == SizeClass.MTS  ||
              sizeClass == SizeClass.LTS  || sizeClass == SizeClass.VLTS ||
              sizeClass == SizeClass.PMU  || sizeClass == SizeClass.MMU  ||
              sizeClass == SizeClass.LMU  || sizeClass == SizeClass.VLMU) &&
             (density == Density.FOUR)) {
      row = 4;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_X) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass.PTS || sizeClass == SizeClass.MTS  ||
              sizeClass == SizeClass.LTS || sizeClass == SizeClass.VLTS ||
              sizeClass == SizeClass.PMU || sizeClass == SizeClass.MMU  ||
              sizeClass == SizeClass.LMU || sizeClass == SizeClass.VLMU) &&
             (density == Density.FOUR)) {
      row = 5;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_M) &&
            (species == Species.PC || species == Species.PP ||
             species == Species.JP || species == Species.DF ||
             species == Species.PJ || species == Species.LP ||
             species == Species.WP || species == Species.WH ||
             species == Species.MH || species == Species.WB ||
             species == Species.LP_RF) &&
             (sizeClass == SizeClass.PTS  || sizeClass == SizeClass.MTS  ||
              sizeClass == SizeClass.LTS  || sizeClass == SizeClass.VLTS ||
              sizeClass == SizeClass.PMU  || sizeClass == SizeClass.MMU  ||
              sizeClass == SizeClass.LMU  || sizeClass == SizeClass.VLMU) &&
             (density == Density.FOUR)) {
      row = 5;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_M) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass.PTS  || sizeClass == SizeClass.MTS  ||
              sizeClass == SizeClass.LTS  || sizeClass == SizeClass.VLTS ||
              sizeClass == SizeClass.PMU  || sizeClass == SizeClass.MMU  ||
              sizeClass == SizeClass.LMU  || sizeClass == SizeClass.VLMU) &&
             (density == Density.FOUR)) {
      row = 6;
    }
    else {
      row = -1;
    }

    if (treatType != TreatmentType.PRECOMMERCIAL_THINNING   ||
        treatType != TreatmentType.COMMERCIAL_THINNING      ||
        treatType != TreatmentType.SANITATION_SALVAGE       ||
        treatType != TreatmentType.IMPROVEMENT_CUT          ||
        treatType != TreatmentType.GROUP_SELECTION_CUT      ||
        treatType != TreatmentType.INDIVIDUAL_SELECTION_CUT ||
        treatType != TreatmentType.LIBERATION_CUT) {
      col++;
    }

    if (page != -1 && row != -1 && col != -1) {
      probability = getProbData(page,row,col);
    }

    if (Simpplle.getClimate().isDrought()) {
      probability *= 2;
    }

    return probability;
  }
/**
 * Method to calculate Root Disease probability.  This is based on species, size class, density, habitat type group, and treatment type.  
 */
  public int doProbability (SouthernCalifornia zone, Evu evu) {
    int       page = 0, row = -1, col = 0;
    int       probability = 0;
    int       cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    HabitatTypeGroupType groupType;

    groupType = evu.getHabitatTypeGroup().getType();

    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();

    Treatment     treat = evu.getTreatment(cTime - 1);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    if (species != Species.PC && species != Species.PP &&
        species != Species.JP && species != Species.DF &&
        species != Species.PJ && species != Species.LP &&
        species != Species.WP && species != Species.WH &&
        species != Species.MH && species != Species.WB &&
        species != Species.LP_RF && species != Species.MC_DF &&
        species != Species.MC_PP && species != Species.MC_WF &&
        species != Species.MC_SEQ && species != Species.MC_IC &&
        species != Species.MC_RF) {
      return 0;
    }

    if (sizeClass != SizeClass.MU) {
      return 0;
    }

    if (density != Density.FOUR) {
      return 0;
    }

    if (groupType.equals(HabitatTypeGroupType.LM_X) &&
        (species == Species.PC || species == Species.PP ||
         species == Species.JP || species == Species.DF ||
         species == Species.PJ || species == Species.LP ||
         species == Species.WP || species == Species.WH ||
         species == Species.MH || species == Species.WB ||
         species == Species.LP_RF) &&
        (sizeClass == SizeClass. PTS || sizeClass == SizeClass. MTS  ||
         sizeClass == SizeClass. LTS || sizeClass == SizeClass. VLTS ||
         sizeClass == SizeClass. PMU || sizeClass == SizeClass. MMU  ||
         sizeClass == SizeClass. LMU || sizeClass == SizeClass. VLMU) &&
        (density == Density.FOUR)) {
      row = 0;
    }
    else if (groupType.equals(HabitatTypeGroupType.LM_X) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass. PTS  || sizeClass == SizeClass. MTS  ||
              sizeClass == SizeClass. LTS  || sizeClass == SizeClass. VLTS ||
              sizeClass == SizeClass. PMU  || sizeClass == SizeClass. MMU  ||
              sizeClass == SizeClass. LMU  || sizeClass == SizeClass. VLMU) &&
             (density == Density.FOUR)) {
      row = 1;
    }
    else if (groupType.equals(HabitatTypeGroupType.LM_M) &&
            (species == Species.PC || species == Species.PP ||
             species == Species.JP || species == Species.DF ||
             species == Species.PJ || species == Species.LP ||
             species == Species.WP || species == Species.WH ||
             species == Species.MH || species == Species.WB ||
             species == Species.LP_RF) &&
             (sizeClass == SizeClass. PTS  || sizeClass == SizeClass. MTS  ||
              sizeClass == SizeClass. LTS  || sizeClass == SizeClass. VLTS ||
              sizeClass == SizeClass. PMU  || sizeClass == SizeClass. MMU  ||
              sizeClass == SizeClass. LMU  || sizeClass == SizeClass. VLMU) &&
             (density == Density.FOUR)) {
      row = 2;
    }
    else if (groupType.equals(HabitatTypeGroupType.LM_M) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass. PTS || sizeClass == SizeClass. MTS  ||
              sizeClass == SizeClass. LTS || sizeClass == SizeClass. VLTS ||
              sizeClass == SizeClass. PMU || sizeClass == SizeClass. MMU  ||
              sizeClass == SizeClass. LMU || sizeClass == SizeClass. VLMU) &&
             (density == Density.FOUR)) {
      row = 3;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_X) &&
            (species == Species.PC || species == Species.PP ||
             species == Species.JP || species == Species.DF ||
             species == Species.PJ || species == Species.LP ||
             species == Species.WP || species == Species.WH ||
             species == Species.MH || species == Species.WB ||
             species == Species.LP_RF) &&
             (sizeClass == SizeClass. PTS || sizeClass == SizeClass. MTS  ||
              sizeClass == SizeClass. LTS || sizeClass == SizeClass. VLTS ||
              sizeClass == SizeClass. PMU || sizeClass == SizeClass. MMU  ||
              sizeClass == SizeClass. LMU || sizeClass == SizeClass. VLMU) &&
             (density == Density.FOUR)) {
      row = 4;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_X) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass. PTS || sizeClass == SizeClass. MTS  ||
              sizeClass == SizeClass. LTS || sizeClass == SizeClass. VLTS ||
              sizeClass == SizeClass. PMU || sizeClass == SizeClass. MMU  ||
              sizeClass == SizeClass. LMU || sizeClass == SizeClass. VLMU) &&
             (density == Density.FOUR)) {
      row = 5;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_M) &&
            (species == Species.PC || species == Species.PP ||
             species == Species.JP || species == Species.DF ||
             species == Species.PJ || species == Species.LP ||
             species == Species.WP || species == Species.WH ||
             species == Species.MH || species == Species.WB ||
             species == Species.LP_RF) &&
             (sizeClass == SizeClass. PTS || sizeClass == SizeClass. MTS  ||
              sizeClass == SizeClass. LTS || sizeClass == SizeClass. VLTS ||
              sizeClass == SizeClass. PMU || sizeClass == SizeClass. MMU  ||
              sizeClass == SizeClass. LMU || sizeClass == SizeClass. VLMU) &&
             (density == Density.FOUR)) {
      row = 5;
    }
    else if (groupType.equals(HabitatTypeGroupType.UM_M) &&
             (species == Species.MC_DF || species == Species.MC_PP  ||
              species == Species.MC_WF || species == Species.MC_SEQ ||
              species == Species.MC_IC || species == Species.MC_RF) &&
             (sizeClass == SizeClass. PTS || sizeClass == SizeClass. MTS  ||
              sizeClass == SizeClass. LTS || sizeClass == SizeClass. VLTS ||
              sizeClass == SizeClass. PMU || sizeClass == SizeClass. MMU  ||
              sizeClass == SizeClass. LMU || sizeClass == SizeClass. VLMU) &&
             (density == Density.FOUR)) {
      row = 6;
    }
    else {
      row = -1;
    }

    if (treatType != TreatmentType.PRECOMMERCIAL_THINNING   ||
        treatType != TreatmentType.COMMERCIAL_THINNING      ||
        treatType != TreatmentType.SANITATION_SALVAGE       ||
        treatType != TreatmentType.IMPROVEMENT_CUT          ||
        treatType != TreatmentType.GROUP_SELECTION_CUT      ||
        treatType != TreatmentType.INDIVIDUAL_SELECTION_CUT ||
        treatType != TreatmentType.LIBERATION_CUT) {
      col++;
    }

    if (page != -1 && row != -1 && col != -1) {
      probability = getProbData(page,row,col);
    }

    if (Simpplle.getClimate().isDrought()) {
      probability *= 2;
    }

    return probability;
  }
/**
 * Method to calculate Root Disease probability for South Central Alaska.  This is based on treatment type, species, and habitat type gropu.  
 */
  public int doProbability (SouthCentralAlaska zone, Evu evu) {
    int     page = 0, row = -1, col = 0;
    int     probability = 0;
    Species species;
    int     cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    HabitatTypeGroupType groupType;

    groupType = evu.getHabitatTypeGroup().getType();
    species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    Treatment     treat = evu.getTreatment(cTime - 1);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    if (groupType.equals(HabitatTypeGroupType.KENAI) &&
        (species == Species.WS    || species == Species.BS ||
         species == Species.WS_BS || species == Species.BS_WS)) {
      row = 0;
    }
    else {
      row = -1;
    }

    if (treatType != TreatmentType.PRECOMMERCIAL_THINNING   ||
        treatType != TreatmentType.COMMERCIAL_THINNING      ||
        treatType != TreatmentType.SANITATION_SALVAGE       ||
        treatType != TreatmentType.IMPROVEMENT_CUT          ||
        treatType != TreatmentType.GROUP_SELECTION_CUT      ||
        treatType != TreatmentType.INDIVIDUAL_SELECTION_CUT ||
        treatType != TreatmentType.LIBERATION_CUT) {
      col++;
    }

    if (page != -1 && row != -1 && col != -1) {
      probability = getProbData(page,row,col);
    }

    return probability;
  }


/**
 * outputs "ROOT-DISEASE"
 */
  public String toString () {
    return printName;
  }

}


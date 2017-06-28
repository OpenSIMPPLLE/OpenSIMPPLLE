/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.ArrayList;

/**
 * This class contains methods to handle MSU Invasive Species Logic, a type of Base Logic.  Invasive species logic is created
 * for Eastside Region 1 and Colorado Plateau.  This is the Eastside Region 1.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class InvasiveSpeciesLogicMSU extends BaseLogic {
  private static final int version = 1;

  public enum Kinds { PROBABILITY, CHANGE_RATE };

  public static final Kinds  PROBABILITY     = Kinds.PROBABILITY;
  public static final String PROBABILITY_STR = PROBABILITY.toString();

  public static final Kinds  CHANGE_RATE     = Kinds.CHANGE_RATE;
  public static final String CHANGE_RATE_STR = CHANGE_RATE.toString();

  public static final int INVASIVE_SPECIES_COL = BaseLogic.LAST_COL+1;
  public static final int INTERCEPT_COL        = BaseLogic.LAST_COL+2;
  public static final int ELEV_COL             = BaseLogic.LAST_COL+3;
  public static final int SLOPE_COL            = BaseLogic.LAST_COL+4;
  public static final int C0SASP_COL           = BaseLogic.LAST_COL+5;
  public static final int SINASP_COL           = BaseLogic.LAST_COL+6;
  public static final int ANNRAD_COL           = BaseLogic.LAST_COL+7;
  public static final int DISTROAD_COL         = BaseLogic.LAST_COL+8;
  public static final int DISTTRAIL_COL        = BaseLogic.LAST_COL+9;
  public static final int PROCESS_COEFF_COL    = BaseLogic.LAST_COL+10;
  public static final int TREATMENT_COEFF_COL  = BaseLogic.LAST_COL+11;
  public static final int SHRUB_COL            = BaseLogic.LAST_COL+12;
  public static final int GRASS_COL            = BaseLogic.LAST_COL+13;
  public static final int TREE_COL             = BaseLogic.LAST_COL+14;
  public static final int START_VALUE_COL      = BaseLogic.LAST_COL+15;


  public static final int CHANGE_RATE_COL            = BaseLogic.LAST_COL+2;
  public static final int CHANGE_AS_PERCENT_COL      = BaseLogic.LAST_COL+3;
  public static final int STATE_CHANGE_THRESHOLD_COL = BaseLogic.LAST_COL+4;

  private static InvasiveSpeciesLogicMSU instance;
  /**
   * Gets this instance of Invasive Species Logic MSU.
   * @return
   */
  public static InvasiveSpeciesLogicMSU getInstance() { return instance; }

  public static void initialize() {
    instance = new InvasiveSpeciesLogicMSU();
  }
/**
 * Constructor.  Inherits from Invasive Species Logic and adds both columns and visible columns for probability and change rate of 
 * invasive species , invasive species description, default invasive species description, representative species,
 * start value, intercept, elevation, slope, cosine aspect, sine aspec, annrad, distance to road, distance to trail, 
 * shrup grass, and tree coefficients, plus pocess coefficient data, process coefficient data descripition, 
 * default process coefficient data, treatment coefficient data , treatment coefficient data description
 
 */
  public InvasiveSpeciesLogicMSU() {
    super(new String[] { PROBABILITY_STR, CHANGE_RATE_STR });
    sysKnowKind = SystemKnowledge.Kinds.INVASIVE_SPECIES_LOGIC_MSU;

    addColumn(PROBABILITY_STR,"INVASIVE_SPECIES_COL");
    addColumn(PROBABILITY_STR,"INTERCEPT_COL");
    addColumn(PROBABILITY_STR,"ELEV_COL");
    addColumn(PROBABILITY_STR,"SLOPE_COL");
    addColumn(PROBABILITY_STR,"C0SASP_COL");
    addColumn(PROBABILITY_STR,"SINASP_COL");
    addColumn(PROBABILITY_STR,"ANNRAD_COL");
    addColumn(PROBABILITY_STR,"DISTROAD_COL");
    addColumn(PROBABILITY_STR,"DISTTRAIL_COL");
    addColumn(PROBABILITY_STR,"PROCESS_COEFF_COL");
    addColumn(PROBABILITY_STR,"TREATMENT_COEFF_COL");
    addColumn(PROBABILITY_STR,"SHRUB_COL");
    addColumn(PROBABILITY_STR,"GRASS_COL");
    addColumn(PROBABILITY_STR,"TREE_COL");
    addColumn(PROBABILITY_STR,"START_VALUE_COL");



    addColumn(CHANGE_RATE_STR,"INVASIVE_SPECIES_COL");
    addColumn(CHANGE_RATE_STR,"CHANGE_RATE_COL");
    addColumn(CHANGE_RATE_STR,"CHANGE_AS_PERCENT_COL");
    addColumn(CHANGE_RATE_STR,"STATE_CHANGE_THRESHOLD_COL");

    addVisibleColumn(PROBABILITY_STR,ROW_COL);
    addVisibleColumn(PROBABILITY_STR,BaseLogic.SPECIES_COL);
    addVisibleColumn(PROBABILITY_STR,INVASIVE_SPECIES_COL);
    addVisibleColumn(PROBABILITY_STR,INTERCEPT_COL);
    addVisibleColumn(PROBABILITY_STR,ELEV_COL);
    addVisibleColumn(PROBABILITY_STR,SLOPE_COL);
    addVisibleColumn(PROBABILITY_STR,C0SASP_COL);
    addVisibleColumn(PROBABILITY_STR,SINASP_COL);
    addVisibleColumn(PROBABILITY_STR,ANNRAD_COL);
    addVisibleColumn(PROBABILITY_STR,DISTROAD_COL);
    addVisibleColumn(PROBABILITY_STR,DISTTRAIL_COL);
    addVisibleColumn(PROBABILITY_STR,PROCESS_COEFF_COL);
    addVisibleColumn(PROBABILITY_STR,TREATMENT_COEFF_COL);
    addVisibleColumn(PROBABILITY_STR,SHRUB_COL);
    addVisibleColumn(PROBABILITY_STR,GRASS_COL);
    addVisibleColumn(PROBABILITY_STR,TREE_COL);
    addVisibleColumn(PROBABILITY_STR,START_VALUE_COL);

    addVisibleColumn(CHANGE_RATE_STR,ROW_COL);
    addVisibleColumn(CHANGE_RATE_STR,BaseLogic.SPECIES_COL);
    addVisibleColumn(CHANGE_RATE_STR,BaseLogic.PROCESS_COL);
    addVisibleColumn(CHANGE_RATE_STR,BaseLogic.TREATMENT_COL);
    addVisibleColumn(CHANGE_RATE_STR,INVASIVE_SPECIES_COL);
    addVisibleColumn(CHANGE_RATE_STR,CHANGE_RATE_COL);
    addVisibleColumn(CHANGE_RATE_STR,CHANGE_AS_PERCENT_COL);
    addVisibleColumn(CHANGE_RATE_STR,STATE_CHANGE_THRESHOLD_COL);

    SystemKnowledge.setHasChanged(SystemKnowledge.INVASIVE_SPECIES_LOGIC_MSU,false);
    SystemKnowledge.setHasUserData(SystemKnowledge.INVASIVE_SPECIES_LOGIC_MSU,false);
  }

  public void duplicateRow(int row,int insertPos, String kindStr) {
    AbstractLogicData logicData = getValueAt(row,kindStr);
    super.addRow(insertPos,kindStr,logicData.duplicate());
  }

  public void addRow(int insertPos, String kindStr) {
    AbstractLogicData logicData;
    Kinds kind = Kinds.valueOf(kindStr);

    switch (kind) {
      case PROBABILITY:
        logicData = new InvasiveSpeciesLogicDataMSU();
        break;
      case CHANGE_RATE:
        logicData = new InvasiveSpeciesChangeLogicData();
        break;
      default:
        return;
    }
    super.addRow(insertPos,kind.toString(),logicData);
  }

  public static boolean hasData() {
    return (instance.getData(PROBABILITY_STR) != null &&
            instance.getData(PROBABILITY_STR).size() > 0);

  }
/**
 * method to update invasive species precentage or change state, see if an invasive species needs to be added to unit, 
 * 
 */
  public void doInvasive() {
    Area area = Simpplle.getCurrentArea();
    Evu[] evus = area.getAllEvu();

    for (int i=0; i<evus.length; i++) {
      if (evus[i] == null) { continue; }


      boolean hasInvasiveState = evus[i].hasInvasiveState();

      for (Lifeform lifeform : evus[i].getLifeforms()) {
        // Update Invasive Species Percent And/Or Change State.
        ArrayList<InclusionRuleSpecies>
          spList = evus[i].getInvasiveTrackingSpecies(lifeform);

        if (spList != null) {
          for (int k = 0; k < spList.size(); k++) {
            InvasiveSpeciesChangeLogicData changeLogicData =
              getMatchingChangeLogicRule(evus[i], spList.get(k));
            if (changeLogicData != null) {
              changeLogicData.doChange(evus[i], lifeform, spList.get(k),
                                       hasInvasiveState);
            }
          }
        }
      }

      ArrayList<InvasiveSpeciesLogicDataMSU> rules = getMatchingLogicRule(evus[i]);
      if (rules == null || rules.size() == 0) { continue; }

      double distRoad  = evus[i].findDistanceNearestRoad();
      double distTrail = evus[i].findDistanceNearestTrail();

      for (int j=0; j<rules.size(); j++) {
        InvasiveSpeciesLogicDataMSU logicData = rules.get(j);
        Species species = logicData.getRepSpecies();

        Lifeform lifeform = species.getLifeform();

        ArrayList<InclusionRuleSpecies>
          spList = evus[i].getInvasiveTrackingSpecies(lifeform);

        // Make sure unit doesn't already have this invasive species.
        if (spList != null) {
          InclusionRuleSpecies repSpecies = InclusionRuleSpecies.get(species.toString());
          if (spList.contains(repSpecies)) {
            continue;
          }
        }

        // See If we need to add an invasive species to the unit.
        int prob = logicData.calculateProbability(evus[i], distRoad, distTrail);
        if (prob > Simulation.getRationalProbability(100)) {
          prob = Simulation.getRationalProbability(100);
        }
        int rand = Simpplle.getCurrentSimulation().random();
        if (rand <= prob) {
          int startValue = logicData.getStartValue();

          VegSimStateData state = evus[i].getState(species.getLifeform());
          if (state == null) {
            Density density = Density.getFromPercentCanopy(startValue);
            HabitatTypeGroup group = evus[i].getHabitatTypeGroup();
            VegetativeType vt = group.getVegetativeType(species, density);
            if (vt != null) {
              Climate.Season season = Simulation.getInstance().getCurrentSeason();

              int ts = Simulation.getCurrentTimeStep();
              int run = Simulation.getCurrentRun();
              state = evus[i].createAndStoreState(ts, run, vt, ProcessType.SUCCESSION,
                                       (short) 100, season);
            }
          }
          if (state != null) {
            InclusionRuleSpecies trkSpecies =
              InclusionRuleSpecies.get(species.toString(), true);
            state.addTrackSpecies(trkSpecies, startValue);

            if (!hasInvasiveState) {
              InvasiveSpeciesChangeLogicData changeLogicData =
                getMatchingChangeLogicRule(evus[i], trkSpecies);
              if (changeLogicData != null) {
                changeLogicData.doChange(evus[i], lifeform, trkSpecies, hasInvasiveState, true);
                hasInvasiveState = evus[i].hasInvasiveState();
              }
            }

          }
        }
      }
    }

  }

  private ArrayList<InvasiveSpeciesLogicDataMSU> getMatchingLogicRule(Evu evu) {
    ArrayList<InvasiveSpeciesLogicDataMSU> rules = new ArrayList<InvasiveSpeciesLogicDataMSU>();

    for (int i=0; i<getData(PROBABILITY_STR).size(); i++) {
      InvasiveSpeciesLogicDataMSU logicData;
      logicData = (InvasiveSpeciesLogicDataMSU)getData(PROBABILITY_STR).get(i);
      if (logicData.isMatch(evu)) {
        rules.add(logicData);
      }
    }

    return rules;
  }
  private InvasiveSpeciesChangeLogicData getMatchingChangeLogicRule(Evu evu, InclusionRuleSpecies species) {
    InvasiveSpeciesChangeLogicData logicData;
    if (getData(CHANGE_RATE_STR) == null) { return null; }

    for (int i=0; i<getData(CHANGE_RATE_STR).size(); i++) {
      logicData = (InvasiveSpeciesChangeLogicData)getData(CHANGE_RATE_STR).get(i);
      if (logicData.isMatch(evu,species)) {
        return logicData;
      }
    }

    return null;
  }

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Invasive Species")) {
      return INVASIVE_SPECIES_COL;
    }
    else if (name.equalsIgnoreCase("Intercept")) {
      return INTERCEPT_COL;
    }
    else if (name.equalsIgnoreCase("Elev")) {
      return ELEV_COL;
    }
    else if (name.equalsIgnoreCase("Slope")) {
      return SLOPE_COL;
    }
    else if (name.equalsIgnoreCase("COSASP")) {
      return C0SASP_COL;
    }
    else if (name.equalsIgnoreCase("SINASP")) {
      return SINASP_COL;
    }
    else if (name.equalsIgnoreCase("ANNRAD")) {
      return ANNRAD_COL;
    }
    else if (name.equalsIgnoreCase("Dist Road")) {
      return DISTROAD_COL;
    }
    else if (name.equalsIgnoreCase("Dist Trail")) {
      return DISTTRAIL_COL;
    }
    else if (name.equalsIgnoreCase("Process Coeff")) {
      return PROCESS_COEFF_COL;
    }
    else if (name.equalsIgnoreCase("Treatment Coeff")) {
      return TREATMENT_COEFF_COL;
    }
    else if (name.equalsIgnoreCase("Shrub")) {
      return SHRUB_COL;
    }
    else if (name.equalsIgnoreCase("Grass")) {
      return GRASS_COL;
    }
    else if (name.equalsIgnoreCase("Tree")) {
      return TREE_COL;
    }
    else if (name.equalsIgnoreCase("Start Value")) {
      return START_VALUE_COL;
    }
    else if (name.equalsIgnoreCase("Rate of Change")) {
      return CHANGE_RATE_COL;
    }
    else if (name.equalsIgnoreCase("Change as %")) {
      return CHANGE_AS_PERCENT_COL;
    }
    else if (name.equalsIgnoreCase("State Change Threshold")) {
      return STATE_CHANGE_THRESHOLD_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }
  private String getColumnNameProbability(int col) {
    switch (col) {
      case INVASIVE_SPECIES_COL: return "Invasive Species";
      case INTERCEPT_COL:        return "Intercept";
      case ELEV_COL:             return "Elev";
      case SLOPE_COL:            return "Slope";
      case C0SASP_COL:           return "COSASP";
      case SINASP_COL:           return "SINASP";
      case ANNRAD_COL:           return "ANNRAD";
      case DISTROAD_COL:         return "Dist Road";
      case DISTTRAIL_COL:        return "Dist Trail";
      case PROCESS_COEFF_COL:    return "Process Coeff";
      case TREATMENT_COEFF_COL:  return "Treatment Coeff";
      case SHRUB_COL:            return "Shrub";
      case GRASS_COL:            return "Grass";
      case TREE_COL:             return "Tree";
      case START_VALUE_COL:      return "Start Value";
      default:
        return super.getColumnName(col);
    }

  }
  private String getColumnNameChangeRate(int col) {
    switch (col) {
      case INVASIVE_SPECIES_COL:       return "Invasive Species";
      case CHANGE_RATE_COL:            return "Rate of Change";
      case CHANGE_AS_PERCENT_COL:      return "Change as %";
      case STATE_CHANGE_THRESHOLD_COL: return "State Change Threshold";
      default:
        return super.getColumnName(col);
    }
  }

  public String getColumnName(String kindStr,int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    Kinds kind = Kinds.valueOf(kindStr);
    switch (kind) {
      case PROBABILITY: return getColumnNameProbability(col);
      case CHANGE_RATE: return getColumnNameChangeRate(col);
      default: return "";
    }
  }

}


/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains methods to handle Invasive Species Logic, a type of Base Logic
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class InvasiveSpeciesLogic extends BaseLogic {
  private static final int version = 1;

  public enum Kinds { PROBABILITY, CHANGE_RATE };

  public static final Kinds  PROBABILITY     = Kinds.PROBABILITY;
  public static final String PROBABILITY_STR = PROBABILITY.toString();

  public static final Kinds  CHANGE_RATE     = Kinds.CHANGE_RATE;
  public static final String CHANGE_RATE_STR = CHANGE_RATE.toString();

  public static final int INVASIVE_SPECIES_COL = BaseLogic.LAST_COL+1;
  public static final int SOIL_TYPE_COL      = BaseLogic.LAST_COL+2;
  public static final int VEG_FUNC_GROUP_COL = BaseLogic.LAST_COL+3;
  public static final int DIST_TO_SEED_COL   = BaseLogic.LAST_COL+4;
  public static final int START_VALUE_COL    = BaseLogic.LAST_COL+5;
  public static final int PROB_COL           = BaseLogic.LAST_COL+6;

  public static final int CHANGE_RATE_COL            = BaseLogic.LAST_COL+2;
  public static final int CHANGE_AS_PERCENT_COL      = BaseLogic.LAST_COL+3;
  public static final int STATE_CHANGE_THRESHOLD_COL = BaseLogic.LAST_COL+4;

  private static InvasiveSpeciesLogic instance;

  public static void initialize() {
    instance = new InvasiveSpeciesLogic();
  }
  /**
   * Constructor.  Initializes columns and visible columns, 
   */
  private InvasiveSpeciesLogic() {
    super(new String[] { PROBABILITY_STR, CHANGE_RATE_STR });
    sysKnowKind = SystemKnowledge.Kinds.INVASIVE_SPECIES_LOGIC;

    addColumn(PROBABILITY_STR,"INVASIVE_SPECIES_COL");
    addColumn(PROBABILITY_STR,"SOIL_TYPE_COL");
    addColumn(PROBABILITY_STR,"VEG_FUNC_GROUP_COL");
    addColumn(PROBABILITY_STR,"DIST_TO_SEED_COL");
    addColumn(PROBABILITY_STR,"START_VALUE_COL");
    addColumn(PROBABILITY_STR,"PROB_COL");

    addColumn(CHANGE_RATE_STR,"INVASIVE_SPECIES_COL");
    addColumn(CHANGE_RATE_STR,"CHANGE_RATE_COL");
    addColumn(CHANGE_RATE_STR,"CHANGE_AS_PERCENT_COL");
    addColumn(CHANGE_RATE_STR,"STATE_CHANGE_THRESHOLD_COL");

    addVisibleColumn(PROBABILITY_STR,ROW_COL);
    addVisibleColumn(PROBABILITY_STR,BaseLogic.SPECIES_COL);
    addVisibleColumn(PROBABILITY_STR,INVASIVE_SPECIES_COL);
    addVisibleColumn(PROBABILITY_STR,SOIL_TYPE_COL);
    addVisibleColumn(PROBABILITY_STR,VEG_FUNC_GROUP_COL);
    addVisibleColumn(PROBABILITY_STR,DIST_TO_SEED_COL);
    addVisibleColumn(PROBABILITY_STR,START_VALUE_COL);
    addVisibleColumn(PROBABILITY_STR,PROB_COL);

    addVisibleColumn(CHANGE_RATE_STR,ROW_COL);
    addVisibleColumn(CHANGE_RATE_STR,BaseLogic.SPECIES_COL);
    addVisibleColumn(CHANGE_RATE_STR,BaseLogic.PROCESS_COL);
    addVisibleColumn(CHANGE_RATE_STR,BaseLogic.TREATMENT_COL);
    addVisibleColumn(CHANGE_RATE_STR,INVASIVE_SPECIES_COL);
    addVisibleColumn(CHANGE_RATE_STR,CHANGE_RATE_COL);
    addVisibleColumn(CHANGE_RATE_STR,CHANGE_AS_PERCENT_COL);
    addVisibleColumn(CHANGE_RATE_STR,STATE_CHANGE_THRESHOLD_COL);

    SystemKnowledge.setHasChanged(SystemKnowledge.INVASIVE_SPECIES_LOGIC,false);
    SystemKnowledge.setHasUserData(SystemKnowledge.INVASIVE_SPECIES_LOGIC,false);
  }
/**
 * duplicates row by calling Base Logic superclass add row method, passes the insert position, kind, and logicdata duplicate
 */
  public void duplicateRow(int row,int insertPos, String kindStr) {
    AbstractLogicData logicData = getValueAt(row,kindStr);
    super.addRow(insertPos,kindStr,logicData.duplicate());
  }
/**
 * 
 * @return the invasive species logic instance 
 */
  public static InvasiveSpeciesLogic getInstance() { return instance; }

/**
 * uses superclass BaseLogic add row method according to kind of logic data to be added 
 * PROBABILITY = invasive species logic data, CHANGE_RATE = invasive species change logic data
 */
  public void addRow(int insertPos, String kindStr) {
    AbstractLogicData logicData;
    Kinds kind = Kinds.valueOf(kindStr);

    switch (kind) {
      case PROBABILITY:
        logicData = new InvasiveSpeciesLogicData();
        break;
      case CHANGE_RATE:
        logicData = new InvasiveSpeciesChangeLogicData();
        break;
      default:
        return;
    }
    super.addRow(insertPos,kind.toString(),logicData);
  }

/**
 * 
 */
  public void doInvasive() {
    ArrayList<Evu> invasiveUnits = buildInvasiveList();

    Area area = Simpplle.getCurrentArea();
    Evu[] evus = area.getAllEvu();

    for (int i = 0; i < evus.length; i++) {
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

      InvasiveSpeciesLogicData logicData = getMatchingLogicRule(evus[i],
        invasiveUnits);
      if (logicData != null) {
        Species  species = logicData.getRepSpecies();
        Lifeform lifeform = species.getLifeform();

        ArrayList<InclusionRuleSpecies> spList = evus[i].getInvasiveTrackingSpecies(lifeform);

        // Make sure unit doesn't already have this invasive species.
        if (spList != null) {
          InclusionRuleSpecies repSpecies = InclusionRuleSpecies.get(species.toString());
          if (spList.contains(repSpecies)) { continue; }
        }

        int prob = logicData.getProb() * 100;
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
              state = evus[i].newState(ts, run, vt, ProcessType.SUCCESSION,
                                       (short) 100, season);
            }
          }
          if (state != null) {
            InclusionRuleSpecies trkSpecies =
              InclusionRuleSpecies.get(species.toString(), true);
            state.addTrackSpecies(trkSpecies, startValue);
          }
        }
      }
    }
  }
/**
 * checks to make sure the instance of invasive species has data
 * @return
 */
  public static boolean hasData() {
    return (instance.getData(PROBABILITY_STR) != null &&
            instance.getData(PROBABILITY_STR).size() > 0);

  }
  /**
   * 
   * @param evu
   * @param invasiveUnits an array list of evu's in an area with invasive species
   * @return the invasive species logic data that maches the evu and invasive unit parameters
   */
  private InvasiveSpeciesLogicData getMatchingLogicRule(Evu evu,ArrayList<Evu> invasiveUnits) {
    InvasiveSpeciesLogicData logicData;
    for (int i=0; i<getData(PROBABILITY_STR).size(); i++) {
      logicData = (InvasiveSpeciesLogicData)getData(PROBABILITY_STR).get(i);
      if (logicData.isMatch(evu,invasiveUnits)) {
        return logicData;
      }
    }

    return null;
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
/**
 * creates an list of all evu's in an area with invasive species  
 * @return a list of evu's in an area with invasive species
 */
  private ArrayList<Evu> buildInvasiveList() {
    ArrayList<Evu> invasiveUnits = new ArrayList<Evu>();

    Area area = Simpplle.getCurrentArea();
    Evu[] evus = area.getAllEvu();
    for (int i=0; i<evus.length; i++) {
      if (evus[i] == null) { continue; }

      Lifeform[] lifeforms = Lifeform.getAllValues();
      for (int j=0; j<lifeforms.length; j++) {
        if (evus[i].hasInvasiveState(lifeforms[j]) || evus[i].hasInvasive(lifeforms[j])) {
          invasiveUnits.add(evus[i]);
          break;
        }
      }
    }
    return invasiveUnits;
  }


  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Invasive Species")) {
      return INVASIVE_SPECIES_COL;
    }
    else if (name.equalsIgnoreCase("Soil Type")) {
      return SOIL_TYPE_COL;
    }
    else if (name.equalsIgnoreCase("Veg Functional Group")) {
      return VEG_FUNC_GROUP_COL;
    }
    else if (name.equalsIgnoreCase("Dist to Seed Source")) {
      return DIST_TO_SEED_COL;
    }
    else if (name.equalsIgnoreCase("Start Value")) {
      return START_VALUE_COL;
    }
    else if (name.equalsIgnoreCase("Probability")) {
      return PROB_COL;
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
      case SOIL_TYPE_COL:      return "Soil Type";
      case VEG_FUNC_GROUP_COL: return "Veg Functional Group";
      case DIST_TO_SEED_COL:   return "Dist to Seed Source";
      case START_VALUE_COL:    return "Start Value";
      case PROB_COL:           return "Probability";
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

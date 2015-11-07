package simpplle.comcode.logic;

import simpplle.comcode.*;

import java.util.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppression Rate Logic, a type of Base Logic
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see BaseLogic
 * 
 */
public class FireSuppProductionRateLogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { FIRE_SUPP_PRODUCTION_RATE_LOGIC };

  public static final Kinds  FIRE_SUPP_PRODUCTION_RATE_LOGIC = Kinds.FIRE_SUPP_PRODUCTION_RATE_LOGIC;

  public static final int MIN_EVENT_ACRES_COL = BaseLogic.LAST_COL+1;
  public static final int MAX_EVENT_ACRES_COL = BaseLogic.LAST_COL+2;
  public static final int SLOPE_COL           = BaseLogic.LAST_COL+3;
  public static final int RATE_COL            = BaseLogic.LAST_COL+4;

  private static FireSuppProductionRateLogic instance;
  /**
   * initializes a new instance of Fire Suppression Production Rate Logic class
   */
  public static void initialize() {
    instance = new FireSuppProductionRateLogic();
  }
  public static FireSuppProductionRateLogic getInstance() { return instance; }
/**
 * 
 */
  public FireSuppProductionRateLogic() {
    super(new String[] { "FIRE_SUPP_PRODUCTION_RATE_LOGIC" });
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_PRODUCTION_RATE_LOGIC;

    addColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),"MIN_EVENT_ACRES_COL");
    addColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),"MAX_EVENT_ACRES_COL");
    addColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),"SLOPE_COL");
    addColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),"RATE_COL");

    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),ROW_COL);
    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),SPECIES_COL);
    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),SIZE_CLASS_COL);
    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),PROCESS_COL);
    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),TREATMENT_COL);

    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),MIN_EVENT_ACRES_COL);
    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),MAX_EVENT_ACRES_COL);
    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),SLOPE_COL);
    addVisibleColumn(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString(),RATE_COL);
  }

  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new FireSuppProductionRateLogicData();
    super.addRow(insertPos,kind,logicData);
  }

  public void clearData() {
    super.clearData("FIRE_SUPP_PRODUCTION_RATE_LOGIC");
  }
  public void addOldRule(int rate)
  {

    FireSuppProductionRateLogicData logicData = new FireSuppProductionRateLogicData();
    super.addRow("FIRE_SUPP_PRODUCTION_RATE_LOGIC",logicData);

    logicData.setRate(rate);
  }

  public void addOldRule(ArrayList species, boolean anyExceptProcess,
                         ArrayList processes, boolean anyExceptTreat,
                         ArrayList treatments, float slope, int rate)
  {

    FireSuppProductionRateLogicData logicData = new FireSuppProductionRateLogicData();
    super.addRow("FIRE_SUPP_PRODUCTION_RATE_LOGIC",logicData);

    if (species != null) {
      for (Object elem : species) {
        logicData.addSimpplleType((Species) elem, SimpplleType.SPECIES);
      }
      logicData.setDescriptionDefault(SimpplleType.SPECIES);
    }
    if (processes != null) {
      for (Object elem : processes) {
        logicData.addSimpplleType((ProcessType) elem, SimpplleType.PROCESS);
      }
      logicData.processAnyExcept = anyExceptProcess;
      logicData.setDescriptionDefault(SimpplleType.PROCESS);
    }
    if (treatments != null) {
      for (Object elem : treatments) {
        logicData.addSimpplleType((TreatmentType) elem, SimpplleType.TREATMENT);
      }
      logicData.treatmentAnyExcept = anyExceptTreat;
      logicData.setDescriptionDefault(SimpplleType.TREATMENT);
    }
    if (Float.isNaN(slope)) {
      slope = 0.0f;
    }
    logicData.setSlope(slope);
    logicData.setRate(rate);

  }

  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Min Acres")) {
      return MIN_EVENT_ACRES_COL;
    }
    else if (name.equalsIgnoreCase("Max Acres")) {
      return MAX_EVENT_ACRES_COL;
    }
    else if (name.equalsIgnoreCase("Slope")) {
      return SLOPE_COL;
    }
    else if (name.equalsIgnoreCase("Rate")) {
      return RATE_COL;
    }
     else {
      return super.getColumnNumFromName(name);
    }
  }
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case MIN_EVENT_ACRES_COL: return "Min Acres";
      case MAX_EVENT_ACRES_COL: return "Max Acres";
      case SLOPE_COL:           return "Slope";
      case RATE_COL:            return "Rate";
      default:
        return super.getColumnName(col);
    }
  }

  public int getRate(int eventAcres, VegetativeType vegType, simpplle.comcode.element.Evu evu, int tStep, Lifeform lifeform)
  {
    ArrayList<AbstractLogicData> dataList = getData(FIRE_SUPP_PRODUCTION_RATE_LOGIC.toString());
    if (dataList == null) { return 0; }

    for (int i=0; i<dataList.size(); i++) {
      FireSuppProductionRateLogicData logicData = (FireSuppProductionRateLogicData)dataList.get(i);
      if (logicData.isMatch(evu,tStep,lifeform,vegType,eventAcres)) {
        return logicData.getRate();
      }
    }
    return 0;

  }

}



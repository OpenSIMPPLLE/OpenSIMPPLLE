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
 * <p>This class defines Fire Spread Rate Logic, a type of Base Logic
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see BaseLogic
 * 
 */
public class FireSuppSpreadRateLogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { FIRE_SUPP_SPREAD_RATE_LOGIC };

  public static final Kinds  FIRE_SUPP_SPREAD_RATE_LOGIC = Kinds.FIRE_SUPP_SPREAD_RATE_LOGIC;

  public static final int SLOPE_COL        = BaseLogic.LAST_COL+1;
  public static final int AVERAGE_RATE_COL = BaseLogic.LAST_COL+2;
  public static final int EXTREME_RATE_COL = BaseLogic.LAST_COL+3;


  private static FireSuppSpreadRateLogic instance;
  public static void initialize() {
    instance = new FireSuppSpreadRateLogic();
  }
  /**
   * 
   * @return
   */
  public static FireSuppSpreadRateLogic getInstance() { return instance; }

  public FireSuppSpreadRateLogic() {
    super(new String[] { "FIRE_SUPP_SPREAD_RATE_LOGIC" });
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_SPREAD_RATE_LOGIC;

    addColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),"SLOPE_COL");
    addColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),"AVERAGE_RATE_COL");
    addColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),"EXTREME_RATE_COL");

    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),ROW_COL);
    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),SPECIES_COL);
    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),SIZE_CLASS_COL);
    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),PROCESS_COL);
    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),TREATMENT_COL);

    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),SLOPE_COL);
    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),AVERAGE_RATE_COL);
    addVisibleColumn(FIRE_SUPP_SPREAD_RATE_LOGIC.toString(),EXTREME_RATE_COL);
  }

  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new FireSuppSpreadRateLogicData();
    super.addRow(insertPos,kind,logicData);
  }

  public void clearData() {
    super.clearData("FIRE_SUPP_SPREAD_RATE_LOGIC");
  }

  public void addOldRule(int averageRate, int extremeRate)
  {

    FireSuppSpreadRateLogicData logicData = new FireSuppSpreadRateLogicData();
    super.addRow("FIRE_SUPP_SPREAD_RATE_LOGIC",logicData);

    logicData.setAverageRate(averageRate);
    logicData.setExtremeRate(extremeRate);
  }

  public void addOldRule(ArrayList species, boolean anyExceptProcess,
                         ArrayList processes, boolean anyExceptTreat,
                         ArrayList treatments, float slope, int averageRate,
                         int extremeRate)
  {

    FireSuppSpreadRateLogicData logicData = new FireSuppSpreadRateLogicData();
    super.addRow("FIRE_SUPP_SPREAD_RATE_LOGIC",logicData);

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
    logicData.setAverageRate(averageRate);
    logicData.setExtremeRate(extremeRate);

  }


  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Slope")) {
      return SLOPE_COL;
    }
    else if (name.equalsIgnoreCase("Average Rate")) {
      return AVERAGE_RATE_COL;
    }
    else if (name.equalsIgnoreCase("Extreme Rate")) {
      return EXTREME_RATE_COL;
    }
     else {
      return super.getColumnNumFromName(name);
    }
  }
  /**
   * Gets column name based on the column Id calculated by using the parameter kindStr to key the visible columns hashmap.  
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case SLOPE_COL: return "Slope";
      case AVERAGE_RATE_COL:  return "Average Rate";
      case EXTREME_RATE_COL:  return "Extreme Rate";
      default:
        return super.getColumnName(col);
    }
  }
/**
 * Warning this is only valid if Evu is a polygon.  Gets the Evu polygon side length and casts to double.
 * If rate is greater than 0 length/rate = spreadtime.  otherwise it reaturns the length.  
 * @param unit
 * @param rate
 * @return
 */
  private double spreadTime(simpplle.comcode.element.Evu unit, int rate) {
    int len  = unit.getSideLength();

    double fLength = (double)len;
    double fRate   = (double)rate;
    
    return (rate > 0) ? (fLength / fRate) : fLength;

  }
/**
 * Matches the Evu fire spreading from, the time step, lifeforms, and vegetative type returns the spread time based on whether the fire is extreme.
 * Gets the Evu polygon side length and casts to double. If rate is greater than 0 length/rate = spreadtime. otherwise it reaturns the length.
 * @param vegType the vegetative type
 * @param isExtreme boolean for fire type.  true if extreme
 * @param fromEvu the Evu where the fire is spreading from
 * @param tStep time step
 * @param lifeform choices are trees, shrubs, herbacious, agriculture, or NA (no classification)
 * @return
 */
  public double getRate(VegetativeType vegType, boolean isExtreme,
                        simpplle.comcode.element.Evu fromEvu, int tStep, Lifeform lifeform)
  {
    ArrayList<AbstractLogicData> dataList = getData(FIRE_SUPP_SPREAD_RATE_LOGIC.toString());
    if (dataList == null) { return 0; }

    for (int i=0; i<dataList.size(); i++) {
      FireSuppSpreadRateLogicData logicData = (FireSuppSpreadRateLogicData)dataList.get(i);
      if (logicData.isMatch(fromEvu,tStep,lifeform,vegType)) {
        return spreadTime(fromEvu,logicData.getRate(isExtreme));
      }
    }
    return 0;

  }

}



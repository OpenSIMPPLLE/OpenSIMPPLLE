/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.*;

/**
 * This class defines Fire Suppression Weather Class A Logic Data, a type of Logic Data
 *
 * Fire Process logic
 * determine all process probabilities for each evu ->use probabilities to select process
 * if selected process is fire event->if fire suppresssion ->determine probability of staying class size A due to weather or fire suppression → if yes change process for evu to succession and record a class A fire with suppression costs
 * if not suppressed at Class A level → determine type of fire and fire spread → at end of simulation calculate fire suppression costs and emissions
 *
 * if selected process is fire and fire suppression is no, determine probability of staying class A size due to weather → if it spreads beyond class A size determine type of firefighter and fire → at end of simulation calculate emissions
 *
 * if stays at class A size due to weather->change process for evu to succession and record class A fire
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.BaseLogic
 */

public class FireSuppWeatherClassALogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { FIRE_SUPP_WEATHER_CLASS_A_LOGIC };

  public static final Kinds  FIRE_SUPP_WEATHER_CLASS_A_LOGIC = Kinds.FIRE_SUPP_WEATHER_CLASS_A_LOGIC;

  public static final int PROB_COL = BaseLogic.LAST_COL+1;

  private static FireSuppWeatherClassALogic instance;
  public static void initialize() {
    instance = new FireSuppWeatherClassALogic();
  }
  public static FireSuppWeatherClassALogic getInstance() { return instance; }

  public FireSuppWeatherClassALogic() {
    super(new String[] { "FIRE_SUPP_WEATHER_CLASS_A_LOGIC" });
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_WEATHER_CLASS_A_LOGIC;

    addColumn(FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString(),"PROB_COL");

    addVisibleColumn(FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString(),ROW_COL);
    addVisibleColumn(FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString(),ECO_GROUP_COL);
    addVisibleColumn(FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString(),SPECIES_COL);
    addVisibleColumn(FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString(),MOISTURE_COL);

    addVisibleColumn(FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString(),PROB_COL);
  }

  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new FireSuppWeatherClassALogicData();
    super.addRow(insertPos,kind,logicData);
  }

  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Probability")) {
      return PROB_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case PROB_COL: return "Probability";
      default:
        return super.getColumnName(col);
    }
  }

  public int getProbability(VegetativeType vegType, Evu evu, int tStep, Lifeform lifeform)
  {
    ArrayList<AbstractLogicData> dataList = getData(FIRE_SUPP_WEATHER_CLASS_A_LOGIC.toString());
    if (dataList == null) { return 0; }

    for (int i=0; i<dataList.size(); i++) {
      FireSuppWeatherClassALogicData logicData = (FireSuppWeatherClassALogicData)dataList.get(i);
      if (logicData.isMatch(evu,tStep,lifeform,vegType)) {
        return logicData.getProb();
      }
    }
    return 0;

  }

}



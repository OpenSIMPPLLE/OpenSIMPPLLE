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
 * This class has methods pertaining to Competition Logic, a type of LogicData.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class DoCompetitionLogic extends BaseLogic {
  static final int  version          = 1;

  public enum Kinds { COMPETITION };

  public static final Kinds  COMPETITION     = Kinds.COMPETITION;
  public static final String COMPETITION_STR = COMPETITION.toString();

  public static final int SELECTED_COL         = BaseLogic.LAST_COL+1;
  public static final int LIFEFORM_COL         = BaseLogic.LAST_COL+2;
  public static final int MIN_CANOPY_COL       = BaseLogic.LAST_COL+3;
  public static final int MAX_CANOPY_COL       = BaseLogic.LAST_COL+4;
  public static final int DENSITY_CHANGE_COL   = BaseLogic.LAST_COL+5;
  public static final int CHANGE_LIFEFORMS_COL = BaseLogic.LAST_COL+6;
  public static final int ACTION_COL           = BaseLogic.LAST_COL+7;

  private static DoCompetitionLogic instance;
  public static void initialize() {
    instance = new DoCompetitionLogic();
  }
  public static DoCompetitionLogic getInstance() { return instance; }

  public DoCompetitionLogic() {
    super(new String[] { "COMPETITION" });
    sysKnowKind = SystemKnowledge.Kinds.DOCOMPETITION_LOGIC;

    addColumn(COMPETITION.toString(),"SELECTED_COL");
    addColumn(COMPETITION.toString(),"LIFEFORM_COL");
    addColumn(COMPETITION.toString(),"MIN_CANOPY_COL");
    addColumn(COMPETITION.toString(),"MAX_CANOPY_COL");
    addColumn(COMPETITION.toString(),"DENSITY_CHANGE_COL");
    addColumn(COMPETITION.toString(),"CHANGE_LIFEFORMS_COL");
    addColumn(COMPETITION.toString(),"ACTION_COL");

    addVisibleColumn(COMPETITION.toString(),ROW_COL);
    addVisibleColumn(COMPETITION.toString(),BaseLogic.SIZE_CLASS_COL);

    addVisibleColumn(COMPETITION.toString(),SELECTED_COL);
    addVisibleColumn(COMPETITION.toString(),LIFEFORM_COL);
    addVisibleColumn(COMPETITION.toString(),MIN_CANOPY_COL);
    addVisibleColumn(COMPETITION.toString(),MAX_CANOPY_COL);
    addVisibleColumn(COMPETITION.toString(),DENSITY_CHANGE_COL);
    addVisibleColumn(COMPETITION.toString(),CHANGE_LIFEFORMS_COL);
    addVisibleColumn(COMPETITION.toString(),ACTION_COL);
  }

//  String str;
//    str =
//      "If trees other than E or SS, other lifeforms are not represented";
//    data.add(new DoCompetitionData(75,100,str));
//
//    str =
//      "If trees other than E or SS, other lifeforms can follow succession " +
//      "pathway density changes";
//    data.add(new DoCompetitionData(0,10,str));
//
//    str =
//      "If trees other than E or SS, an increase in density must be accompanied " +
//      "by a decrease in the shrub and density classes by one.";
//    data.add(new DoCompetitionData(11,74,str));
//
//    str =
//      "If trees other than E or SS, a decrease in canopy coverage class " +
//      "can be accompanied by an increase in the shrub and grass density " +
//      "by one class if succession pathway has it.";
//    data.add(new DoCompetitionData(11,74,str));
//
//    str = "If no trees, grass lifeforms are not represented";
//    data.add(new DoCompetitionData(75,100,str));
//
//    str =
//      "If not trees, grass lifeform can follow succession pathway density " +
//      "changes";
//    data.add(new DoCompetitionData(0,10,str));
//
//    str =
//      "If no trees, an increase in the canopy coverage class must be accompanied " +
//      "by a decrease in the grass density by one class";
//    data.add(new DoCompetitionData(11,74,str));
//
//    str =
//      "If no trees, a decrease in the canopy coverage class can be accompanied " +
//      "by an increase in the grass density by one class if the succession " +
//      "pathway has it.";
//    data.add(new DoCompetitionData(11,74,str));

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Use Logic")) {
      return SELECTED_COL;
    }
    else if (name.equalsIgnoreCase("Lifeform")) {
      return LIFEFORM_COL;
    }
    else if (name.equalsIgnoreCase("Min % Canopy")) {
      return MIN_CANOPY_COL;
    }
    else if (name.equalsIgnoreCase("Max % Canopy")) {
      return MAX_CANOPY_COL;
    }
    else if (name.equalsIgnoreCase("Density Change")) {
      return DENSITY_CHANGE_COL;
    }
    else if (name.equalsIgnoreCase("Change Lifeforms")) {
      return CHANGE_LIFEFORMS_COL;
    }
    else if (name.equalsIgnoreCase("Action")) {
      return ACTION_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }

  /**
   * returns a column name in plain english format.  
   */
  public String getColumnName(String kindStr,int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case SELECTED_COL:         return "Use Logic";
      case LIFEFORM_COL:         return "Lifeform";
      case MIN_CANOPY_COL:       return "Min % Canopy";
      case MAX_CANOPY_COL:       return "Max % Canopy";
      case DENSITY_CHANGE_COL:   return "Density Change";
      case CHANGE_LIFEFORMS_COL: return "Change Lifeforms";
      case ACTION_COL:           return "Action";
      default:
        return super.getColumnName(col);
    }
  }

  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new DoCompetitionData();
    super.addRow(insertPos,kind,logicData);
  }
  public void duplicateRow(int row,int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }

  public boolean doLogic(Evu evu) {
    ArrayList<AbstractLogicData> dataList = getData(COMPETITION.toString());
    if (dataList == null) { return false; }

    for (int i=0; i<dataList.size(); i++) {
      DoCompetitionData logicData =
          (DoCompetitionData)dataList.get(i);
      if (logicData.isMatch(evu)) {
        logicData.doAction(evu);
        return true;
      }
    }
    return false;
  }

}


package simpplle.comcode;

import java.util.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> This class defines Gap National Land Cover Data, a type of Base Logic 
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *  @see simpplle.comcode.BaseLogic
 */

public class GapProcessLogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { GAP_PROCESSES };

  public static final Kinds  GAP_PROCESSES = Kinds.GAP_PROCESSES;

  public static final int GAP_PROCESS_COL = BaseLogic.LAST_COL+1;
  public static final int PROB_COL        = BaseLogic.LAST_COL+2;

  private static GapProcessLogic instance;
 /**
  * instantiates a new instance of GapProcessLogic
  */
  public static void initialize() {
    instance = new GapProcessLogic();
  }
  public static GapProcessLogic getInstance() { return instance; }
 /**
  * Constructor.  Inherits from Base Logic and initializes the System Knoledge kind ot gap process logic, and sets system knowledge has changed and has user data to false
  */
  public GapProcessLogic() {
    super(new String[] { "GAP_PROCESSES" });
    sysKnowKind = SystemKnowledge.Kinds.GAP_PROCESS_LOGIC;

    addColumn(GAP_PROCESSES.toString(),"GAP_PROCESS_COL");
    addColumn(GAP_PROCESSES.toString(),"PROB_COL");

    this.addVisibleColumnAll(GAP_PROCESSES.toString());

    addVisibleColumn(GAP_PROCESSES.toString(),PROB_COL);

    SystemKnowledge.setHasChanged(SystemKnowledge.GAP_PROCESS_LOGIC,false);
    SystemKnowledge.setHasUserData(SystemKnowledge.GAP_PROCESS_LOGIC,false);
  }

  /**
   * adds a row to the base logic with position, kind, and GapProcess data
   */
  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new GapProcessLogicData();
    super.addRow(insertPos,kind,logicData);
  }
/**
 * duplicates a row in baseLogic
 */
  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }
/**
 * method to get column name based on string literal passed as parameter.  choices are Gap process, and Probability 
 */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("GAP Process")) {
      return GAP_PROCESS_COL;
    }
    else if (name.equalsIgnoreCase("Probability")) {
      return PROB_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }
  /**
   * This gets the column name to be displayed in the GUI.  Those defined here are Gap Process, and Probability.  
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case GAP_PROCESS_COL: return "GAP Process";
      case PROB_COL:        return "Probability";
      default:
        return super.getColumnName(col);
    }
  }

  /**
   * Check the evu for GAP Processes and return true if the unit
   * had a GAP Process.
   * @param evu Evu
   * @param lifeform Lifeform
   * @return boolean
   */
  public boolean doLogic(Evu evu, Lifeform lifeform) {
    ArrayList<AbstractLogicData> dataList = getData(GAP_PROCESSES.toString());
    if (dataList == null) { return false; }

    for (int i=0; i<dataList.size(); i++) {
      GapProcessLogicData logicData = (GapProcessLogicData)dataList.get(i);
      if (logicData.isMatch(evu,lifeform)) {
        logicData.doAction(evu,lifeform);
        return true;
      }
    }
    return false;
  }
}



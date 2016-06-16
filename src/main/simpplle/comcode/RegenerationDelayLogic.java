package simpplle.comcode;

import java.util.ArrayList;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Regeneration Delay Logic, a type of Base Logic.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 */
public class RegenerationDelayLogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { REGEN_DELAY };

  public static final Kinds  REGEN_DELAY     = Kinds.REGEN_DELAY;
  public static final String REGEN_DELAY_STR = REGEN_DELAY.toString();

  public static final int DELAY_COL         = BaseLogic.LAST_COL+1;

  private static RegenerationDelayLogic instance;
  /**
   * creates an instance of RegenerationDelayLogic class
   */
  public static void initialize() {
    instance = new RegenerationDelayLogic();
  }
  public static RegenerationDelayLogic getInstance() { return instance; }
/**
 * Constructor for Regeneration Delay Logic.  
 * Inherits from Base Logic superclass and initializes the system knowledge to regeneration delay and adds a nonvisible column for Delay and 
 * visible columns for row, eco grou, species, and Delay.
 */
  public RegenerationDelayLogic() {
    super(new String[] { "REGEN_DELAY" });
    sysKnowKind = SystemKnowledge.Kinds.REGEN_DELAY_LOGIC;

    addColumn(REGEN_DELAY.toString(),"DELAY_COL");

    addVisibleColumn(REGEN_DELAY.toString(),ROW_COL);
    addVisibleColumn(REGEN_DELAY.toString(),BaseLogic.ECO_GROUP_COL);
    addVisibleColumn(REGEN_DELAY.toString(),BaseLogic.SPECIES_COL);
    addVisibleColumn(REGEN_DELAY.toString(),DELAY_COL);
  }
/**
 * Adds a Regeneration Delay Logic Data column at the insertion postion, and with string name of logic kind.
 */
  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new RegenerationDelayLogicData();
    super.addRow(insertPos,kind,logicData);
  }
/**
 * Duplicates Regeneration Delay Logic Data row according to passed row, position, and string name of logic kind.  
 */
  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }
/**
 * gets column code name matching input string parameter.  
 */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Delay Time Steps")) {
      return DELAY_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }
  /**
   * returns a string literal of column name.  Designated in this class are Delay Time Steps.  Else it defaults to Logic Data superclass to get column name.  
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case DELAY_COL: return "Delay Time Steps";
      default:
        return super.getColumnName(col);
    }
  }

  /**
   * Gets the regeneration delay for a particular lifeform in an Evu.  If delay is greater than 0 returns the delay, otherwise defaults to 0.  
   * @param evu the evu where the lifeform with regeneration delay is to be found. 
   * @param lifeform lifeform evaluated for regeneration delay
   * @return the integer value of delay.  
   */
  public int getDelay(Evu evu, Lifeform lifeform) {
    int delay = 0;
    ArrayList<AbstractLogicData> dataList = getData(REGEN_DELAY.toString());
    if (dataList == null) { return delay; }

    for (int i=0; i<dataList.size(); i++) {
      RegenerationDelayLogicData logicData =
          (RegenerationDelayLogicData)dataList.get(i);
      delay = logicData.getDelay(evu,lifeform);
      if (delay > 0) { return delay; }
    }
    return delay;
  }

}

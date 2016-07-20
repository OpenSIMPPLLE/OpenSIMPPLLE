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
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppression Event Logic, a type of Base Logic.  This class contains variables to be set about season and fire probability.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 * @see simpplle.comcode.BaseLogic
 * 
 */
public class FireSuppEventLogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { FIRE_SUPP_EVENT_LOGIC };

  public static final Kinds  FIRE_SUPP_EVENT_LOGIC = Kinds.FIRE_SUPP_EVENT_LOGIC;

  public static final int FIRE_SEASON_COL = BaseLogic.LAST_COL+1;
  public static final int PROB_COL        = BaseLogic.LAST_COL+2;

  private HashMap<Integer,Boolean> suppHm = new HashMap<Integer,Boolean>();
  
  private static FireSuppEventLogic instance;
  /**
   * initializes a new instance of fire suppression event logic
   */
  public static void initialize() {
    instance = new FireSuppEventLogic();
  }
  public static FireSuppEventLogic getInstance() { return instance; }
/**
 * Constructor for Fire Suppression Event Logic.  Inherits from Base Logic and adds columns for probability and fire season
 * as well as visible columns for row, fire season, and probability
 * 
 */
  public FireSuppEventLogic() {
    super(new String[] { "FIRE_SUPP_EVENT_LOGIC" });
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_EVENT_LOGIC;

    addColumn(FIRE_SUPP_EVENT_LOGIC.toString(),"PROB_COL");
    addColumn(FIRE_SUPP_EVENT_LOGIC.toString(),"FIRE_SEASON_COL");

    addVisibleColumn(FIRE_SUPP_EVENT_LOGIC.toString(),ROW_COL);
    addVisibleColumn(FIRE_SUPP_EVENT_LOGIC.toString(),FIRE_SEASON_COL);
    addVisibleColumn(FIRE_SUPP_EVENT_LOGIC.toString(),PROB_COL);
  }

  @Override
  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new FireSuppEventLogicData();
    super.addRow(insertPos,kind,logicData);
  }

  /**
   * Duplicates a row by creating a new instance of FireSuppEventLogic and adding it to abstract logic data table.  
   */
  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }
  /**
   * Uses string literal to return column Id.   Specific to this class is FIRE_SEASON_COL=16, and PROB_COL=17.  Otherwise passes up to 
   * the 15 base logic columns.  
   */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Fire Season")) {
      return FIRE_SEASON_COL;
    }
    else if (name.equalsIgnoreCase("Prob")) {
      return PROB_COL;
    }
     else {
      return super.getColumnNumFromName(name);
    }
  }
  
  /**
   * Gets the column name (string) from column Id.  From this class returns either "Fire Season" or "Prob".  Otherwise passes up to 
   * the 15 base logic column names.  
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case FIRE_SEASON_COL: return "Fire Season";
      case PROB_COL:        return "Prob";
      default:
        return super.getColumnName(col);
    }
  }
/**
 * Clears the suppressed hashmap.  
 */
  public void clearSuppressed() {
    suppHm.clear();
  }
  /**
   * Calculates whether a fire event is suppressed. If origin Evu is in suppressed hashmap, returns true.  
   * If not checks fire suppression event logic data to see.  If there is no data, fire not suppressed.  
   * Otherwise will conduct a random probability test true if random instance variable is <= rational probability.  
   * @param originEvu where fire comes from
   * @param random random number.  
   * @return
   */
  public boolean isSuppressed(Evu originEvu, int random)
  {
    if (suppHm.containsKey(originEvu.getId())) {
      return suppHm.get(originEvu.getId());
    }
    
    ArrayList<AbstractLogicData> dataList = getData(FIRE_SUPP_EVENT_LOGIC.toString());
    if (dataList == null) {
      suppHm.put(originEvu.getId(), false);
      return false;
    }

    for (int i=0; i<dataList.size(); i++) {
      FireSuppEventLogicData logicData = (FireSuppEventLogicData)dataList.get(i);
      if (logicData.isMatch(originEvu)) {
        boolean suppressed = logicData.isSuppressed(random);
        suppHm.put(originEvu.getId(), suppressed);
        return suppressed;
      }
    }
    
    suppHm.put(originEvu.getId(), false);
    return false;

  }
  
}

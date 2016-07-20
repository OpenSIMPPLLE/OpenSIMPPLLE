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
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> This class manages the search logic for EVU, a type of Base Logic.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.BaseLogic
 */

public class EvuSearchLogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { EVU_SEARCH };

  public static final Kinds  EVU_SEARCH = Kinds.EVU_SEARCH;

  public static final int TIME_STEP_COL    = BaseLogic.LAST_COL+1;
  public static final int AGE_COL          = BaseLogic.LAST_COL+2;
  public static final int FMZ_COL          = BaseLogic.LAST_COL+3;
  public static final int PROB_COL         = BaseLogic.LAST_COL+4;

  private static EvuSearchLogic instance;
  public static void initialize() {
    instance = new EvuSearchLogic();
  }
  public static EvuSearchLogic getInstance() { return instance; }

  /**
   * Constructor.  Initializes the system knowledge kind and adds columns for time step, age, fire management zone, probablity
   * 
   */
  public EvuSearchLogic() {
    super(new String[] { "EVU_SEARCH" });
    sysKnowKind = SystemKnowledge.Kinds.EVU_SEARCH_LOGIC;

    addColumn(EVU_SEARCH.toString(),"TIME_STEP_COL");
    addColumn(EVU_SEARCH.toString(),"AGE_COL");
    addColumn(EVU_SEARCH.toString(),"FMZ_COL");
    addColumn(EVU_SEARCH.toString(),"PROB_COL");

    this.addVisibleColumnAll(EVU_SEARCH.toString());
  }

/**
 * adds a row by inserting position, system knowledge kind, and instance of AbstractLogicData by calling the superclass simpplle.comcode.BaseLogic addRow method
 */
  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new EvuSearchData();
    super.addRow(insertPos,kind,logicData);
  }
/**
 * 
 * dublicates a row by getting the abstract logic data from  from row, then position, system knowledge kind, and instance of AbstractLogicData are passed to the superclass simpplle.comcode.BaseLogic addRow method
 *@row - the row to be copied - gets the abstract logic data at that row
 */
  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }
/**
 * method to take in a string literal version of the search logic types and return the SearchLogic column number.  
 * for time step, age, fmz, and prob number refers to forumla declared within this class, else refers to superclass method  
 * 
 */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Time Step")) {
      return TIME_STEP_COL;
    }
    else if (name.equalsIgnoreCase("Age")) {
      return AGE_COL;
    }
    else if (name.equalsIgnoreCase("Fmz")) {
      return FMZ_COL;
    }
    else if (name.equalsIgnoreCase("Probability")) {
      return PROB_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }
  /**
   * @return a string literal of type of search logic.  if time step, age, fmz, or prob overrides super class and refers to declaration above.  otherwise refers to superclass
   * 
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case EvuSearchLogic.TIME_STEP_COL:    return "Time Step";
      case EvuSearchLogic.AGE_COL:          return "Age";
     case EvuSearchLogic.FMZ_COL:          return "Fmz";
      case EvuSearchLogic.PROB_COL:         return "Probability";
      default:
        return super.getColumnName(col);
    }
  }
/**
 * method to check if all evu in current area match those at specified row
 * @param row the row to be matched
 * @return arrayList with the matching evu units 
 */
  public ArrayList<Evu> findMatchingUnits(int row) {
    ArrayList<Evu> units = new ArrayList<Evu>();

    ArrayList<AbstractLogicData> dataList = getData(EVU_SEARCH.toString());
    if (dataList == null) { return units; }

    EvuSearchData logicData = (EvuSearchData)dataList.get(row);
    if (logicData == null) { return units; }

    Evu[] allEvu = Simpplle.getCurrentArea().getAllEvu();
    for (int i=0; i<allEvu.length; i++) {
      if (allEvu[i] == null) { continue; }

      if (logicData.isMatch(allEvu[i])) {
        units.add(allEvu[i]);
      }

    }

    return units;
  }

}




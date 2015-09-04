package simpplle.comcode;

import java.util.ArrayList;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class VegUnitFireTypeLogic extends AbstractBaseLogic {
  static final int  version = 1;

  public enum Kinds { UNIT_FIRE_TYPE };

  public static final Kinds  UNIT_FIRE_TYPE = Kinds.UNIT_FIRE_TYPE;

  public static final int TREES_COL  = AbstractBaseLogic.LAST_COL+1;
  public static final int SHRUBS_COL = AbstractBaseLogic.LAST_COL+2;
  public static final int GRASS_COL  = AbstractBaseLogic.LAST_COL+3;
  public static final int RESULT_COL = AbstractBaseLogic.LAST_COL+4;

  private static VegUnitFireTypeLogic instance;
  public static void initialize() {
    instance = new VegUnitFireTypeLogic();
  }
  public static VegUnitFireTypeLogic getInstance() { return instance; }

  public VegUnitFireTypeLogic() {
    super(new String[] { "UNIT_FIRE_TYPE" });
    sysKnowKind = SystemKnowledge.Kinds.VEG_UNIT_FIRE_TYPE_LOGIC;

    addColumn(UNIT_FIRE_TYPE.toString(),"TREES_COL");
    addColumn(UNIT_FIRE_TYPE.toString(),"SHRUBS_COL");
    addColumn(UNIT_FIRE_TYPE.toString(),"GRASS_COL");
    addColumn(UNIT_FIRE_TYPE.toString(),"RESULT_COL");

    this.addVisibleColumnAll(UNIT_FIRE_TYPE.toString());

    SystemKnowledge.setHasChanged(SystemKnowledge.VEG_UNIT_FIRE_TYPE_LOGIC,false);
    SystemKnowledge.setHasUserData(SystemKnowledge.VEG_UNIT_FIRE_TYPE_LOGIC,false);
  }

  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new VegUnitFireTypeLogicData();
    super.addRow(insertPos,kind,logicData);
  }

  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }

  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case TREES_COL:  return "Trees";
      case SHRUBS_COL: return "Shrubs";
      case GRASS_COL:  return "Herbacious";
      case RESULT_COL: return "Unit Fire";
      default:
        return super.getColumnName(col);
    }
  }

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Trees")) {
      return TREES_COL;
    }
    else if (name.equalsIgnoreCase("Shrubs")) {
      return SHRUBS_COL;
    }
    else if (name.equalsIgnoreCase("Herbacious")) {
      return GRASS_COL;
    }
    else if (name.equalsIgnoreCase("Unit Fire")) {
      return RESULT_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }
/**
 * Gets the fire type logic data from Unit Fire Type data, and checks to see if logic data matches the tree, shrub, and grass fire processes.
 * Ensures that the result returned if there are no matching rules is the dominant process inside the if, else if conditional.    
 * @param treeProcess
 * @param shrubProcess
 * @param grassProcess
 * @return
 */
  public ProcessType getUnitProcess(ProcessType treeProcess,
                                    ProcessType shrubProcess,
                                    ProcessType grassProcess)
  {
    ProcessType result=null;

    // Make sure result is dominant process in case there are no matching rules.
    if (treeProcess != null) { result = treeProcess; }
    else if (shrubProcess != null) { result = shrubProcess; }
    else if (grassProcess != null) { result = grassProcess; }

    ArrayList<AbstractLogicData> dataList = getData(UNIT_FIRE_TYPE.toString());
    if (dataList == null) { return result; }

    for (int i=0; i<dataList.size(); i++) {
      VegUnitFireTypeLogicData logicData = (VegUnitFireTypeLogicData)dataList.get(i);
      if (logicData.isMatch(treeProcess,shrubProcess,grassProcess)) {
        return logicData.getResult();
      }
    }
    return result;

  }

}



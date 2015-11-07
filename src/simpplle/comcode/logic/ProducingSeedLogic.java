package simpplle.comcode.logic;

import simpplle.comcode.*;

import java.util.ArrayList;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Producing Seed Logic, a type of Base Logic.
 * All default probabilities for this are either 0 or 100.  Logic for producing seed incorporates the species, size class, regeneration type, 
 * and whether a disturbance process has occured in a past time step.   
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class ProducingSeedLogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { PRODUCING_SEED };

  public static final Kinds  PRODUCING_SEED = Kinds.PRODUCING_SEED;

  public static final int REGEN_TYPE_COL     = BaseLogic.LAST_COL+1;
  public static final int PRODUCING_SEED_COL = BaseLogic.LAST_COL+2;

  private static ProducingSeedLogic instance;
  public static void initialize() {
    instance = new ProducingSeedLogic();
  }
  public static ProducingSeedLogic getInstance() { return instance; }
/**
 * Constructor for Producing Seed Logic.  Passes to Base Logic the producing seed array and initializes the
 * system knowledge to producing seed logic, and adds columns and visible columns.  
 */
  public ProducingSeedLogic() {
    super(new String[] { "PRODUCING_SEED" });
    sysKnowKind = SystemKnowledge.Kinds.PRODUCING_SEED_LOGIC;

    addColumn(PRODUCING_SEED.toString(),"REGEN_TYPE_COL");
    addColumn(PRODUCING_SEED.toString(),"PRODUCING_SEED_COL");

    addVisibleColumn(PRODUCING_SEED.toString(),ROW_COL);
    addVisibleColumn(PRODUCING_SEED.toString(),SPECIES_COL);
    addVisibleColumn(PRODUCING_SEED.toString(),SIZE_CLASS_COL);
    addVisibleColumn(PRODUCING_SEED.toString(),PROCESS_COL);

    addVisibleColumn(PRODUCING_SEED.toString(),REGEN_TYPE_COL);
    addVisibleColumn(PRODUCING_SEED.toString(),PRODUCING_SEED_COL);

  }
/**
 * adds a row Producing Seed Logic Data row according to insert position and kind.
 */
  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new ProducingSeedLogicData();
    super.addRow(insertPos,kind,logicData);
  }
/**
 * duplicates a Producing Seed Logic Data row with insert position and kind.  
 */
  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }
/**
 * get the column number based on passed string name of type.  IN this class seed probability and regen type are defined, else it passes to super class
 */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Seed Probability")) {
      return PRODUCING_SEED_COL;
    }
    else if (name.equalsIgnoreCase("Regen Type")) {
      return REGEN_TYPE_COL;
    }
     else {
      return super.getColumnNumFromName(name);
    }
  }
  /**
   * returns the string literal of a specific column name
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case REGEN_TYPE_COL:     return "Regen Type";
      case PRODUCING_SEED_COL: return "Seed Probability";
      default:
        return super.getColumnName(col);
    }
  }
/**
 * method to designate if seed producing.  Uses the is match function of logic data class to match with producing seed logic data. 
 * @param vegType vegetative type
 * @param evu
 * @param tStep time step
 * @param lifeform
 * @param regenType
 * @return true if seed producing.  
 */
  public boolean isSeedProducing(VegetativeType vegType, simpplle.comcode.element.Evu evu,
                                 int tStep, Lifeform lifeform,
                                 simpplle.comcode.element.Evu.RegenTypes regenType)
  {
    ArrayList<AbstractLogicData> dataList = getData(PRODUCING_SEED.toString());
    if (dataList == null) { return false; }

    for (int i=0; i<dataList.size(); i++) {
      ProducingSeedLogicData logicData = (ProducingSeedLogicData)dataList.get(i);
      if (logicData.isMatch(evu,tStep,lifeform,vegType,regenType)) {
        return logicData.getProducingSeed();
      }
    }
    return false;

  }

}



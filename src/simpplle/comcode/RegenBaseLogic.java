package simpplle.comcode;


import simpplle.comcode.RegenerationLogic.DataKinds;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * <p>This class contains methods for a Regeneration Base Logic, a type of Base Logic.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */
public class RegenBaseLogic extends BaseLogic {
  private DataKinds kind;
  /**
   * Constructor for Regeneration Base Logic.  Inherits from Base Logic superclass and initializes some variables like fire and succession
   * @param kind
   */
  public RegenBaseLogic(DataKinds kind) {
    super(new String[] { kind.toString() });
    this.kind = kind;


    String[] columnNames;
    int[]    columns;
    switch (kind) {
      case FIRE:       columnNames = FireRegenerationData.getColumnNames();
                       columns     = FireRegenerationData.getColumns();
                       sysKnowKind = SystemKnowledge.Kinds.REGEN_LOGIC_FIRE;
                       break;
      case SUCCESSION: columnNames = SuccessionRegenerationData.getColumnNames();
                       columns     = SuccessionRegenerationData.getColumns();
                       sysKnowKind = SystemKnowledge.Kinds.REGEN_LOGIC_SUCC;
                       break;
      default: return;
    }
    for (int i=0; i<columnNames.length; i++) {
      addColumn(kind.toString(),columnNames[i]);
    }

    addVisibleColumn(kind.toString(),ROW_COL);
    addVisibleColumn(kind.toString(),SIZE_CLASS_COL);
    addVisibleColumn(kind.toString(),DENSITY_COL);

    for (int i=0; i<columns.length; i++) {
      addVisibleColumn(kind.toString(),columns[i]);
    }
  }
  /**
   * addRow
   *
   * @param insertPos int
   * @param kind String
   * TODO Implement this simpplle.comcode.BaseLogic method
   */
  public void addRow(int insertPos, String kind) {
    RegenerationLogic.addDataRow(insertPos,DataKinds.valueOf(kind));
  }

  /**
   * duplicateRow
   *
   * @param row int
   * @param insertPos int
   * @param kind String
   * TODO Implement this simpplle.comcode.BaseLogic method
   */
  public void duplicateRow(int row, int insertPos, String kind) {
    RegenerationLogic.duplicateRow(row,insertPos,DataKinds.valueOf(kind));
  }

  /**
   * getColumnName
   *
   * @param kind String
   * @param col int
   * @return String
   * TODO Implement this simpplle.comcode.BaseLogic method
   */
  public String getColumnName(String kind, int col) {
    return RegenerationLogic.getColumnName(DataKinds.valueOf(kind),col);
  }
/**
 * Gets the column id based on regeneration kind (choices are FIRE, SUCCESSION) and parameter string name.    
 */
  public int getColumnNumFromName(String name) {
    int col = RegenerationLogic.getColumnNumFromName(kind,name);
    if (col == -1) {
      col = super.getColumnNumFromName(name);
    }
    return col;
  }

}

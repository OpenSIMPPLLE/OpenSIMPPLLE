package simpplle.gui;

import javax.swing.table.TableColumn;

import simpplle.comcode.logic.BaseLogic;
import simpplle.comcode.Climate;
import simpplle.comcode.logic.FireSuppEventLogic;
import simpplle.comcode.SystemKnowledge.Kinds;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class sets up Fire Suppression Beyond Class A LogicPanel, a type of Vegetative Logic Panel, which inherits from Base Panel.
 * Class A fires are 0-.25 Acres.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
@SuppressWarnings("serial")
public class FireSuppEventLogicPanel extends VegLogicPanel {
  public FireSuppEventLogicPanel(FireSuppEventLogicDlg dialog,
      String kind, BaseLogic logicInst, Kinds sysKnowKind) {
    super(dialog,kind,logicInst,sysKnowKind);
    try {
      initialize();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  protected void initialize() {
    initializeBase();
  }

/**
 * Init columns are fire season, 
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == FireSuppEventLogic.FIRE_SEASON_COL) {
      column.setIdentifier(FireSuppEventLogic.FIRE_SEASON_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(Climate.allSeasons));
      column.setCellEditor(new MyJComboBoxEditor(Climate.allSeasons));
    }
    else if (col == FireSuppEventLogic.PROB_COL) {
      column.setIdentifier(FireSuppEventLogic.PROB_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
}

}

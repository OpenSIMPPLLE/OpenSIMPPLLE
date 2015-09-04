package simpplle.gui;

import javax.swing.table.TableColumn;
import simpplle.comcode.BaseLogic;
import simpplle.comcode.FireEventLogic;
import simpplle.comcode.SystemKnowledge;
import simpplle.comcode.SystemKnowledge.Kinds;
import simpplle.comcode.*;
import javax.swing.table.*;


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
public class FireSuppBeyondClassALogicPanel extends VegLogicPanel {
	/**
	 * Constructor for Fire Suppression Beyond Class A Logic Panel
	 * @param dialog
	 * @param kind
	 * @param logicInst
	 * @param sysKnowKind
	 */
  public FireSuppBeyondClassALogicPanel(FireSuppBeyondClassALogicDlg dialog,
                                 String kind, BaseLogic logicInst, Kinds sysKnowKind) {
    super(dialog,kind,logicInst,sysKnowKind);
    try {
      initialize();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  /**
   * Initializes Fire Suppression beyond Class A Logic Panel, by calling base logic panels initializeBase().
   */
  protected void initialize() {
    initializeBase();
  }

/**
 * Initializes the columns and colors for Fire Suppression beyond Class A Logic, based on the column int entered.  Possibilities are.  
 * <li>Suppress</li> 
 * <li>Spread Kind</li> 
 * <li>Fire Type</li>
 * <li>Probability</li>
 * @param col the integer value of the column 
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == FireSuppBeyondClassALogic.SUPPRESS_COL) {
      column.setIdentifier(FireSuppBeyondClassALogic.SUPPRESS_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == FireSuppBeyondClassALogic.SPREAD_KIND_COL) {
      column.setIdentifier(FireSuppBeyondClassALogic.SPREAD_KIND_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(FireSuppBeyondClassALogicData.SpreadKind.values()));
      column.setCellEditor(new MyJComboBoxEditor(FireSuppBeyondClassALogicData.SpreadKind.values()));
    }
    else if (col == FireSuppBeyondClassALogic.FIRE_TYPE_COL) {
      column.setIdentifier(FireSuppBeyondClassALogic.FIRE_TYPE_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(FireSuppBeyondClassALogicData.FireType.values()));
      column.setCellEditor(new MyJComboBoxEditor(FireSuppBeyondClassALogicData.FireType.values()));
    }
    else if (col == FireSuppBeyondClassALogic.PROB_COL) {
      column.setIdentifier(FireSuppBeyondClassALogic.PROB_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}

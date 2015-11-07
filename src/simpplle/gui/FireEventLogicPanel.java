package simpplle.gui;

import simpplle.comcode.logic.BaseLogic;
import javax.swing.table.TableColumn;

import simpplle.comcode.logic.FireEventLogic;
import simpplle.comcode.logic.FireSpottingLogicData;
import simpplle.comcode.Process;
import java.util.ArrayList;

import simpplle.comcode.logic.FireSpottingLogicData.SpreadType;
import simpplle.comcode.SystemKnowledge.Kinds;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the FireEventLogicPanel a type of Vegetative Logic Panel. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */

@SuppressWarnings("serial")
public class FireEventLogicPanel extends VegLogicPanel {
	/**
	 * Constructor for Fire Event Logic Panel.  Inherits from Vegetative Logic Panel which inherits from Base logic Panel.   
	 * @param dialog
	 * @param kind
	 * @param logicInst
	 * @param sysKnowKind
	 */
  public FireEventLogicPanel(AbstractLogicDialog dialog,
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
 * Initializes the fire event logic panel
 */
  protected void initialize() {
    initializeBase();
  }
/**
 * Initializes the columns by passing to specific fire logics based on system knowledge kind.  Choices will be either fire type, fire spotting, or fire spread.  
 */
  protected void initColumns(TableColumn column, int col) {
    if (kind.equals(FireEventLogic.TYPE_STR.toString())) {
      initFireTypeTable(column,col);
    }
    else if (kind.equals(FireEventLogic.FIRE_SPOTTING_STR)) {
      initFireSpottingTable(column,col);
    }
    else {
      initFireSpreadTable(column,col);
    }
  }
  /**
   * Initializes the fire spread table with columns for either: Origin process, Position, Average, and extreme
   * @param column 
   * @param col
   */
  private void initFireSpreadTable(TableColumn column, int col) {
    // ** Origin Process Column **
    if (col == FireEventLogic.ORIGIN_PROCESS_COL) {
      column.setIdentifier(FireEventLogic.ORIGIN_PROCESS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,FireEventLogic.ORIGIN_PROCESS_COL,"Origin Process",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Position Column **
    else if (col == FireEventLogic.POSITION_COL) {
      column.setIdentifier(FireEventLogic.POSITION_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,FireEventLogic.POSITION_COL,"Position",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Average Column **
    else if (col == FireEventLogic.AVERAGE_COL) {
      column.setIdentifier(FireEventLogic.AVERAGE_COL);
      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(true);
      column.setCellRenderer(new MyJComboBoxRenderer(fireProcesses));
      column.setCellEditor(new MyJComboBoxEditor(fireProcesses));
      Utility.setColumnCellColor(column);
    }
    // ** Extreme Column **
    else if (col == FireEventLogic.EXTREME_COL) {
      column.setIdentifier(FireEventLogic.EXTREME_COL);
      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(true);
      column.setCellRenderer(new MyJComboBoxRenderer(fireProcesses));
      column.setCellEditor(new MyJComboBoxEditor(fireProcesses));
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
  /**
   * Initializes the fire type table with columns for either wetter, normal, or drier. 
   * @param column
   * @param col
   */
  private void initFireTypeTable(TableColumn column, int col) {
    // ** Wetter Column **
    if (col == FireEventLogic.WETTER_COL) {
      column.setIdentifier(FireEventLogic.WETTER_COL);
      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(true);
      column.setCellRenderer(new MyJComboBoxRenderer(fireProcesses));
      column.setCellEditor(new MyJComboBoxEditor(fireProcesses));
      Utility.setColumnCellColor(column);
    }
    // ** Normal Column **
    else if (col == FireEventLogic.NORMAL_COL) {
      column.setIdentifier(FireEventLogic.NORMAL_COL);
      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(true);
      column.setCellRenderer(new MyJComboBoxRenderer(fireProcesses));
      column.setCellEditor(new MyJComboBoxEditor(fireProcesses));
      Utility.setColumnCellColor(column);
    }
    // ** Drier Column **
    else if (col == FireEventLogic.DRIER_COL) {
      column.setIdentifier(FireEventLogic.DRIER_COL);
      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(true);
      column.setCellRenderer(new MyJComboBoxRenderer(fireProcesses));
      column.setCellEditor(new MyJComboBoxEditor(fireProcesses));
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column, col);
    }
  }
  
  /**
   * Sets the fire spotting table for either fire processes, spread type, start distance, end distance, or Probability
   * @param column
   * @param col
   */
  private void initFireSpottingTable(TableColumn column, int col) {
    if (col == FireEventLogic.FIRE_PROCESS_COL) {
      column.setIdentifier(FireEventLogic.FIRE_PROCESS_COL);
      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(false);
      column.setCellRenderer(new MyJComboBoxRenderer(fireProcesses));
      column.setCellEditor(new MyJComboBoxEditor(fireProcesses));
      Utility.setColumnCellColor(column);
    }
    else if (col == FireEventLogic.SPREAD_TYPE_COL) {
      column.setIdentifier(FireEventLogic.SPREAD_TYPE_COL);
      SpreadType[] spreadTypes = FireSpottingLogicData.getSpreadTypes();
      
      column.setCellRenderer(new MyJComboBoxRenderer(spreadTypes));
      column.setCellEditor(new MyJComboBoxEditor(spreadTypes));
      Utility.setColumnCellColor(column);
    }
    else if (col == FireEventLogic.START_DIST_COL) {
      column.setIdentifier(FireEventLogic.START_DIST_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
      column.setPreferredWidth(125);
    }
    else if (col == FireEventLogic.END_DIST_COL) {
      column.setIdentifier(FireEventLogic.END_DIST_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
      column.setPreferredWidth(125);
    }
    else if (col == FireEventLogic.PROB_COL) {
      column.setIdentifier(FireEventLogic.PROB_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column, col);
    }
  }

}

package simpplle.gui;

import simpplle.comcode.BaseLogic;
import javax.swing.table.TableColumn;
import simpplle.comcode.ProcessProbLogic;
import simpplle.comcode.SystemKnowledge.Kinds;
import javax.swing.table.*;
import java.awt.*;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the Process Probability Logic Panel, a type of VegLogicPanel, itself a type of JPanel.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */
public class ProcessProbabilityLogicPanel extends VegLogicPanel {
	/**
	 * Constructor for Process Probabilty Logic Panel
	 * @param dialog the logic dialog.  For this object it will be process probability logic dialog  
	 * @param kind
	 * @param logicInst the logic instance
	 * @param sysKnowKind the system knowledge
	 */
  public ProcessProbabilityLogicPanel(AbstractLogicDialog dialog,
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
   * Initializes the Process Probability Logic Panel by calling the superclasses initializeBase() which 
   * sets the information on the base panel for this class, sets the column selection to false, 
   * the row selection to true and initializes the column width and sends to GUI Utility functions to size the column width based on the 
   * current JTable, which will then pass to another initColumnWidth and resize based on #of columns
   */
  protected void initialize() {
    initializeBase();
  }
  /**
   * Initialize the columns for this Process Probability Logic Panel.  
   * Columns will for this are ADJ_PROCESS_COL, MPB_HAZARD_COL, ADJ_MOD_HAZARD_COL, ADJ_HIGH_HAZARD_COL, PROB_COL, or the base columns.  
   */
  protected void initColumns(TableColumn column, int col) {
    // ** Adjacent Process Column **
    if (col == ProcessProbLogic.ADJ_PROCESS_COL) {
      column.setIdentifier(ProcessProbLogic.ADJ_PROCESS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog, logicTable, dataModel,
                                               ProcessProbLogic.ADJ_PROCESS_COL,
                                               "Adjacent Process", true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }

    // ** Mountain Pine Beetle(LPMPB) Hazard Column **
    else if (col == ProcessProbLogic.MPB_HAZARD_COL) {
      column.setIdentifier(ProcessProbLogic.MPB_HAZARD_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,ProcessProbLogic.MPB_HAZARD_COL,"MPB Hazard",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }

    else if (col == ProcessProbLogic.MPB_HAZARD_COL) {
      column.setIdentifier(ProcessProbLogic.MPB_HAZARD_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == ProcessProbLogic.ADJ_MOD_HAZARD_COL) {
      column.setIdentifier(ProcessProbLogic.ADJ_MOD_HAZARD_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == ProcessProbLogic.ADJ_HIGH_HAZARD_COL) {
      column.setIdentifier(ProcessProbLogic.ADJ_HIGH_HAZARD_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == ProcessProbLogic.PROB_COL) {
      column.setIdentifier(ProcessProbLogic.PROB_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column, col);
    }
  }

}



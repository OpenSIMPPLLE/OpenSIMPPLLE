package simpplle.gui;

import javax.swing.table.TableColumn;
import simpplle.comcode.logic.BaseLogic;
import simpplle.comcode.SystemKnowledge.Kinds;

/** The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the GapProcess Logic Panel, a type of VegLogicPanel, which in turn is a type of Base Logic Panel (a JPanel).
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */

public class GapProcessLogicPanel extends VegLogicPanel {
	/**
	 * Creates the Gap Process Logic Panel.  
	 * @param dialog the gap process logic dialog
	 * @param kind
	 * @param logicInst the gap process logic instance
	 * @param sysKnowKind the system knowledge kind.  
	 */
  public GapProcessLogicPanel(AbstractLogicDialog dialog,
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
   * Initilizes the Gap Process Logic Panel by calling the superclasses initializeBase() which 
   * sets the information on the base panel for this class, sets the column selection to false, 
   * the row selection to true and initializes the column width and sends to GUI Utility functions to size the column width based on the 
   * current JTable, which will then pass to another initColumnWidth and resize based on #of columns
   */
  protected void initialize() {
    initializeBase();
  }
/**
 * Initialize the columns for this Gap Process dialog.  If the column 
 * equals GAP_PROCESS_COL renders combo boxes and sets the combo box editor to to the summary processes for a simulation.
 * If it is PROB_COL then it sets the colunn identifier to PROB_COL (17)
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == simpplle.comcode.logic.GapProcessLogic.GAP_PROCESS_COL) {
      column.setIdentifier(simpplle.comcode.logic.GapProcessLogic.GAP_PROCESS_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(simpplle.comcode.Process.getSummaryProcesses()));
      column.setCellEditor(new MyJComboBoxEditor(simpplle.comcode.Process.getSummaryProcesses()));
    }
    else if (col == simpplle.comcode.logic.GapProcessLogic.PROB_COL) {
      column.setIdentifier(simpplle.comcode.logic.GapProcessLogic.PROB_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}

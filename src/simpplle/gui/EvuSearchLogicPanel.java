package simpplle.gui;

import javax.swing.table.TableColumn;
import simpplle.comcode.logic.BaseLogic;
import simpplle.comcode.SystemKnowledge.Kinds;

/**
*
* The University of Montana owns copyright of the designated documentation contained 
* within this file as part of the software product designated by Uniform Resource Identifier 
* UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
* Open Source License Contract pertaining to this documentation and agrees to abide by all 
* restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
* <p>This class creates Evu Search Logic Panel, a type of VegLogicPanel.
* 
* @author Documentation by Brian Losi
* <p>Original source code authorship: Kirk A. Moeller 
* 
* 
*/

public class EvuSearchLogicPanel extends VegLogicPanel {
/**
 * Constructor for Evu Search Logic Panel.  Passes to Veg Logic Panel the input Evu Search Logic dialog, kind, base logic and system knowledge kind.
 * @param dialog 
 * @param kind 
 * @param logicInst 
 * @param sysKnowKind the system knowledge kind
 */
  public EvuSearchLogicPanel(EvuSearchLogicDlg dialog,
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
   * Initializes the Evu Search Logic panel by calling to superclass Base Logic Panel initializeBase()
   * void simpplle.gui.BaseLogicPanel.initializeBase()
   */
  protected void initialize() {
    initializeBase();
  }
/**
 * Initializes the columns depending on whether it is time step, age, fire management zone, or probabilty.  
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == simpplle.comcode.logic.EvuSearchLogic.TIME_STEP_COL) {
      column.setIdentifier(simpplle.comcode.logic.EvuSearchLogic.TIME_STEP_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.EvuSearchLogic.TIME_STEP_COL,"Time Steps",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.EvuSearchLogic.AGE_COL) {
      column.setIdentifier(simpplle.comcode.logic.EvuSearchLogic.AGE_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.EvuSearchLogic.AGE_COL,"Age",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.EvuSearchLogic.FMZ_COL) {
      column.setIdentifier(simpplle.comcode.logic.EvuSearchLogic.FMZ_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.EvuSearchLogic.FMZ_COL,"Fmz",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.EvuSearchLogic.PROB_COL) {
      column.setIdentifier(simpplle.comcode.logic.EvuSearchLogic.PROB_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.EvuSearchLogic.PROB_COL,"Process Probability",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
/**
 * Updates the row selected in the Evu search logic dialog.
 */
  protected void rowSelected() {
    ((EvuSearchLogicDlg)dialog).updateSelectedRow(selectedRow);
  }
}

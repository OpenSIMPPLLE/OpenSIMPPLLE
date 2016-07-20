/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.TableColumn;
import simpplle.comcode.BaseLogic;
import simpplle.comcode.SystemKnowledge;
import simpplle.comcode.SystemKnowledge.Kinds;
import simpplle.comcode.*;
import javax.swing.table.*;
import java.util.ArrayList;

/** 
 * This class creates the Producing Seed Logic Dialog, a type of VegLogicDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ProducingSeedLogicPanel extends VegLogicPanel {
	/**
	 * Constructor for Producing Seed Logic Panel.  
	 * @param dialog
	 * @param kind
	 * @param logicInst
	 * @param sysKnowKind
	 */
  public ProducingSeedLogicPanel(ProducingSeedLogicDlg dialog,
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
   * Initializes the Producing Seed Logic Panel by calling the superclasses initializeBase() which 
   * sets the information on the base panel for this class, sets the column selection to false, 
   * the row selection to true and initializes the column width and sends to GUI Utility functions to size the column width based on the 
   * current JTable, which will then pass to another initColumnWidth and resize based on #of columns
   */
  protected void initialize() {
    initializeBase();
  }

  /**
   * Initialize the columns for this Producing Seed Logic Panel by column ID.  
   * Columns will be either PRODUCING_SEED_COL, REGEN_TYPE_COL, or the base columns.  The base columns are 
   *  Row Col (from Base Logic superclass), (following from VegLogicPanel) Ecological Grouping, Species, Size Class, Density, Process, Treatment,
   *  Season, Moisture, Temperature, Tracking Species, Ownership, Special Area, Road Status, Trail Status, Landtype
 *   
   */
  protected void initColumns(TableColumn column, int col) {
    if (col == ProducingSeedLogic.PRODUCING_SEED_COL) {
      column.setIdentifier(ProducingSeedLogic.PRODUCING_SEED_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == ProducingSeedLogic.REGEN_TYPE_COL) {
      column.setIdentifier(ProducingSeedLogic.REGEN_TYPE_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,ProducingSeedLogic.REGEN_TYPE_COL,"Regen Type",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}

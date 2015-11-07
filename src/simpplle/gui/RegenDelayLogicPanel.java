package simpplle.gui;

import javax.swing.table.TableColumn;
import simpplle.comcode.BaseLogic;
import simpplle.comcode.SystemKnowledge.Kinds;
import simpplle.comcode.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the Regeneration Delay Logic Panel Seed Logic Dialog, a type of VegLogicDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */
public class RegenDelayLogicPanel extends VegLogicPanel {
  public RegenDelayLogicPanel(AbstractLogicDialog dialog,
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
   * Passes to VegLogicPanel initializeBase() method which 
   * sets the information on the base panel for this class, 
   * sets the column selection to false, the row selection to true and initializes 
   * the column width and sends to GUI Utility functions to size the column width based on the current JTable, 
   * which will then pass to another initColumnWidth and resize based on #of columns
   */
  protected void initialize() {
    initializeBase();
  }
/**
 * Initializes the column specific to Delay with identifiers and colors or passes to superclass initBaseColumns.  These are depending on 
 * col parameter: Row Col (from Base Logic superclass), Ecological Grouping, Species, Size Class, Density, Process
 * Treatment, Season, Moisture, Temperature, Tracking Species, Ownership, Special Area, Road Status, Trail Status, or Landtype
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == RegenerationDelayLogic.DELAY_COL) {
      column.setIdentifier(RegenerationDelayLogic.DELAY_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}

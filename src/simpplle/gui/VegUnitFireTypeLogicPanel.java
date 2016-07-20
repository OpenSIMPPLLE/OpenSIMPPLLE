/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.AbstractBaseLogic;
import simpplle.comcode.SystemKnowledge.Kinds;
import javax.swing.table.*;
import simpplle.comcode.VegUnitFireTypeLogic;
import simpplle.comcode.Process;
import java.util.ArrayList;
import simpplle.comcode.*;
import java.awt.*;

/** 
 * This class sets up the columns for Vegetative Unit Fire Type Logic panel, a type of Base Logic Panel, which itself is a type of JPanel.
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */

public class VegUnitFireTypeLogicPanel extends BaseLogicPanel {
  private static Color RESULT_COL_COLOR = new Color(255,255,0);
/**
 * Constructor for Vegetative Fire type logic panel.  
 * @param dialog
 * @param kind
 * @param logicInst
 * @param sysKnowKind
 */
  public VegUnitFireTypeLogicPanel(AbstractLogicDialog dialog, String kind,
                                   AbstractBaseLogic logicInst,
                                   Kinds sysKnowKind) {
    super(dialog, kind, logicInst, sysKnowKind);
    initializeBase();
    logicTable.setGridColor(Color.BLACK);
  }
/**
 * Initializes columns for vegetative fire type logic panel.  Choices are Trees, Shrubs, Grass, and results.  
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == VegUnitFireTypeLogic.TREES_COL) {
      column.setIdentifier(VegUnitFireTypeLogic.TREES_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,VegUnitFireTypeLogic.TREES_COL,"Trees",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == VegUnitFireTypeLogic.SHRUBS_COL) {
      column.setIdentifier(VegUnitFireTypeLogic.SHRUBS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,VegUnitFireTypeLogic.SHRUBS_COL,"Shrubs",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == VegUnitFireTypeLogic.GRASS_COL) {
      column.setIdentifier(VegUnitFireTypeLogic.GRASS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,VegUnitFireTypeLogic.GRASS_COL,"Herbacious",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == VegUnitFireTypeLogic.RESULT_COL) {
      column.setIdentifier(VegUnitFireTypeLogic.RESULT_COL);
      ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(false);
      column.setCellRenderer(new MyJComboBoxRenderer(fireProcesses,RESULT_COL_COLOR));
      column.setCellEditor(new MyJComboBoxEditor(fireProcesses));
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }

}

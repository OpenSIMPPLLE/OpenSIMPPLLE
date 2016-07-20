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
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class sets up Fire Suppression Beyond Class A LogicPanel, a type of Vegetative Logic Panel.
 * Class A fires are 0-.25 Acres.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class FireSuppClassALogicPanel extends VegLogicPanel {
  public FireSuppClassALogicPanel(FireSuppClassALogicDlg dialog,
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
 * Choices for columns are Probability column or the superclass base columns.  
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == FireSuppClassALogic.PROB_COL) {
      column.setIdentifier(FireSuppClassALogic.PROB_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}

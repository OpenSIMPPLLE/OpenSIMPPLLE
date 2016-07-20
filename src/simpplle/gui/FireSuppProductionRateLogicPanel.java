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
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class FireSuppProductionRateLogicPanel extends VegLogicPanel {
  public FireSuppProductionRateLogicPanel(FireSuppProductionRateLogicDlg dialog,
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


  protected void initColumns(TableColumn column, int col) {
    if (col == FireSuppProductionRateLogic.MIN_EVENT_ACRES_COL) {
      column.setIdentifier(FireSuppProductionRateLogic.MIN_EVENT_ACRES_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == FireSuppProductionRateLogic.MAX_EVENT_ACRES_COL) {
      column.setIdentifier(FireSuppProductionRateLogic.MAX_EVENT_ACRES_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == FireSuppProductionRateLogic.SLOPE_COL) {
      column.setIdentifier(FireSuppProductionRateLogic.SLOPE_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == FireSuppProductionRateLogic.RATE_COL) {
      column.setIdentifier(FireSuppProductionRateLogic.RATE_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}

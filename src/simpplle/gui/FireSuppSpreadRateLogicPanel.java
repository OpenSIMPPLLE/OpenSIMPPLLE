package simpplle.gui;

import javax.swing.table.TableColumn;
import simpplle.comcode.logic.BaseLogic;
import simpplle.comcode.SystemKnowledge.Kinds;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class FireSuppSpreadRateLogicPanel extends VegLogicPanel {
  public FireSuppSpreadRateLogicPanel(FireSuppSpreadRateLogicDlg dialog,
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
    if (col == simpplle.comcode.logic.FireSuppSpreadRateLogic.SLOPE_COL) {
      column.setIdentifier(simpplle.comcode.logic.FireSuppSpreadRateLogic.SLOPE_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == simpplle.comcode.logic.FireSuppSpreadRateLogic.AVERAGE_RATE_COL) {
      column.setIdentifier(simpplle.comcode.logic.FireSuppSpreadRateLogic.AVERAGE_RATE_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == simpplle.comcode.logic.FireSuppSpreadRateLogic.EXTREME_RATE_COL) {
      column.setIdentifier(simpplle.comcode.logic.FireSuppSpreadRateLogic.EXTREME_RATE_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}

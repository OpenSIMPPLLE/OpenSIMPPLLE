/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.ProcessType;
import simpplle.comcode.SimpplleType;

/**
 * This class defines Process Logic Table Data Model, a type of SimpplleTypeTableDataModel, itself a type of javax AbstractTableModel.
 *
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */

public class LogicProcessTableDataModel extends SimpplleTypeTableDataModel {
  public static final int PROCESS_COL = 1;
/**
 * Constructor for Process Logic Table Data Models.  References SimpplleTypeTableDataModel superclass and passes in the simpplle type
 * @param kind
 */
  public LogicProcessTableDataModel(SimpplleType.Types kind) {
    super(kind);
  }
/**
 * Gets the column count for process type logic table data model.  This will return two representing SELECTED_COL, and PROCESS_COL
 */
  public int getColumnCount() {
    return 2;
  }
/**
 * Gets the column id from input column id.  If this is a process column this will return the code_col variable from processType which is 0. 
 */
  protected int getCol(int c) {
    int col;
    switch(c) {
      case SELECTED_COL:  col = c; break;
      case PROCESS_COL: col = ProcessType.CODE_COL; break;
      default: col = c;
    }
    return col;
  }

}

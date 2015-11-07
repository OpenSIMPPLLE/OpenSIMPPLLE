package simpplle.gui;

import javax.swing.table.*;

import java.util.*;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */class ListTableDataModel extends AbstractTableModel {
  public static final int SELECTED_COL = 0;
  public static final int DATA_COL     = 1;

  protected simpplle.comcode.logic.AbstractLogicData logicData;
  protected ArrayList list;
  protected int userClassDataCol;

  public ListTableDataModel() {
    super();
  }

  public void initData(simpplle.comcode.logic.AbstractLogicData logicData, ArrayList list, int userClassDataCol) {
    this.logicData  = logicData;
    this.list       = list;
    this.userClassDataCol = userClassDataCol;
  }

  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  public boolean isCellEditable(int row, int col) {
    return (col == SELECTED_COL);
  }

  public int getColumnCount() {
    return 2;
  }

  public int getRowCount() {
    return (list != null ? list.size() : 0);
  }
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (columnIndex == SELECTED_COL) {
      if (list == null) { return false; }
      return logicData.hasListValue(userClassDataCol,list.get(rowIndex));
    }
    else {
      return list.get(rowIndex);
    }
  }
  public void setValueAt(Object value, int row, int col) {
    if (logicData == null) { return; }
    if (col == SELECTED_COL) {
      Boolean selected = (Boolean)value;
      if (selected) {
        logicData.addListValueAt(userClassDataCol,getValueAt(row,DATA_COL));
      }
      else {
        logicData.removeListValueAt(userClassDataCol,getValueAt(row,DATA_COL));
      }
      fireTableCellUpdated(row,col);
    }
  }

  public String getColumnName(int column) {
    switch (column) {
      case SELECTED_COL: return "Chosen";
      case DATA_COL:     return simpplle.comcode.logic.BaseLogic.getColumnName(userClassDataCol);
      default: return "";
    }
  }


}


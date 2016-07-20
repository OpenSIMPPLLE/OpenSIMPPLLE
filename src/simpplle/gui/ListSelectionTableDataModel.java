/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import simpplle.comcode.AbstractLogicData;

/** 
 * This class defines the JDialog for fire season.
 * 
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */
public class ListSelectionTableDataModel extends AbstractTableModel {
  public static final int SELECTED_COL  = 0;
  public static final int VALUES_COL    = 1;
/**
 * returns 2 for the column count.  
 */
  public int getColumnCount() {
    return 2;
  }

  ArrayList allValues;
  ArrayList selectedValues;
  AbstractLogicData logicData;
/**
 * 
 * @param logicData
 * @param valuesCol
 */
  public ListSelectionTableDataModel(AbstractLogicData logicData, int valuesCol) {
    allValues      = logicData.getPossibleValues(valuesCol);
    selectedValues = (ArrayList)logicData.getValueAt(valuesCol);
    this.logicData = logicData;
  }
/**
 * Checks if there is data present in logic data possible values
 * @return
 */
  public boolean isDataPresent() {
    return allValues != null && allValues.size() > 0;
  }
/**
 * Gets the class specific by input column id and row set to 0.  
 */
  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }
/**
 * Checks if a cell is editable.  True if column is selected column.  
 */
  public boolean isCellEditable(int row, int col) {
    return col == SELECTED_COL;
  }
/**
 * Gets an object from specified cell.  This is located by the row and column variables passed as parameters.  
 */
  public Object getValueAt(int row, int c) {
    if (c == SELECTED_COL) {
      return (selectedValues.contains(allValues.get(row)));
    }
    else if (c == VALUES_COL) {
      return allValues.get(row);
    }
    return new String("Problem");
  }

  public void setValueAt(Object value, int row, int col) {
    if (col != SELECTED_COL) { return; }

    Boolean selected = (Boolean)value;

    if (selected) {
      selectedValues.add(allValues.get(row));
    }
    else {
      selectedValues.remove(allValues.get(row));
    }
    logicData.markChanged();
    fireTableCellUpdated(row,col);
  }
  /**
   * Gets the number of rows by accessing the all values arraylist size.  
   */
  public int getRowCount() {
    return allValues.size();
  }
  /**
   * 
   */
  public String getColumnName(int column) {
    switch (column) {
      case SELECTED_COL: return "Selected";
      case VALUES_COL:   return "Values";
      default:           return super.getColumnName(column);
    }
  }

}

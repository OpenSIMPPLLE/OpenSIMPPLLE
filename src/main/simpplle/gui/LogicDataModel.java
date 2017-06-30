/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.AbstractBaseLogic;
import simpplle.comcode.AbstractLogicData;
import simpplle.comcode.RegenerationData;
import simpplle.comcode.RegenerationLogic;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/** 
 * LogicDataModel interrogates tabular data from system knowledge. The data interrogated by this
 * class is displayed and manipulated within a JTable.
 */

public class LogicDataModel extends AbstractTableModel {

  private AbstractBaseLogic logicInst;
  private String kindStr;
  private JTable table;
  private boolean isRegen = false;

  /**
   * Creates a table model with a reference to model data and a table displaying the data.
   *
   * @param kind The name of a kind of knowledge
   * @param logicInst A collection of knowledge
   * @param table A table view
   */
  public LogicDataModel(String kind, AbstractBaseLogic logicInst, JTable table) {
    this.kindStr = kind;
    this.logicInst = logicInst;
    this.table = table;
  }

  /**
   * Returns the kind of knowledge interrogated by this model.
   */
  public String getKind() {
    return kindStr;
  }

  /**
   * Returns the collection of knowledge interrogated by this model.
   */
  public AbstractBaseLogic getLogicInst() {
    return ( (isRegen) ? RegenerationLogic.getLogicInstance(kindStr) : logicInst);
  }

  /**
   *
   */
  public void setIsRegen(boolean regen) {
    isRegen = regen;
  }

  /**
   * Returns the most specific superclass for all the cell values in the column.
   */
  public Class getColumnClass(int columnIndex) {
    if (getLogicInst().isDataPresent(kindStr)) {
      return getValueAt(0, columnIndex).getClass();
    }
    return Object.class;
  }

  /**
   * Returns the number of columns in the model.
   */
  public int getColumnCount() {
    return getLogicInst().getVisibleColumnCount(kindStr);
  }

  /**
   * Returns the name of a column.
   */
  public String getColumnName(int columnIndex) {
    String result = getLogicInst().getColumnName(kindStr,columnIndex);
    return ((result == null) ? super.getColumnName(columnIndex) : result);
  }

  /**
   * Returns an adjusted column index.
   */
  private int getRealColumnValue(int col) {
    TableColumn column = table.getColumnModel().getColumn(col);
    if (column.getIdentifier() instanceof String) {
      return getLogicInst().getColumnNumFromName((String)column.getIdentifier());
    }
    return (Integer)column.getIdentifier();
  }

  /**
   * Shows a hidden column.
   */
  public void addVisibleColumn(int columnIndex) {
    getLogicInst().addVisibleColumn(kindStr,columnIndex);
  }

  /**
   * Hides a visible column.
   */
  public void removeVisibleColumn(int columnIndex) {
    getLogicInst().removeVisibleColumn(kindStr,columnIndex);
  }

  /**
   * Returns an array of visible column indices.
   */
  public int[] getVisibleColumns() {
    return getLogicInst().getVisibleColumns(kindStr);
  }

  /**
   * Returns true if the column is visible.
   */
  public boolean isVisibleColumn(int columnIndex) {
    return getLogicInst().isVisibleColumn(kindStr,columnIndex);
  }

  /**
   * Returns the number of rows in the model.
   */
  public int getRowCount() {
    AbstractBaseLogic inst = getLogicInst();
    return (inst != null) ? inst.getRowCount(kindStr) : 0;
  }

  /**
   * Returns a single row of logic data.
   */
  public AbstractLogicData getValueAt(int rowIndex) { // Rename to getRowData
    return getLogicInst().getValueAt(rowIndex,kindStr);
  }

  /**
   * Adds a row of logic data and forwards the change to listeners.
   *
   * @param insertPos The index where the new row will be placed
   */
  public void addRow(int insertPos) {
    getLogicInst().addRow(insertPos,kindStr);
    fireTableRowsInserted(0, getRowCount());
    fireTableDataChanged();
  }

  /**
   * Duplicates a row of logic data and forwards the change to listeners.
   *
   * @param rowIndex The row to be duplicated
   * @param insertPos The index where the duplicate will be placed
   */
  public void duplicateRow(int rowIndex, int insertPos) {
    getLogicInst().duplicateRow(rowIndex,insertPos,kindStr);
    fireTableRowsInserted(0, getRowCount());
    fireTableDataChanged();
  }

  /**
   * Deletes a row of logic data and forwards the change to listeners.
   *
   * @param rowIndex The row to be deleted
   */
  public void deleteRow(int rowIndex) {
    getLogicInst().removeRow(rowIndex,kindStr);
    fireTableRowsDeleted(rowIndex,rowIndex);
  }

  /**
   * Decrements the index of a row and forwards the change to listeners.
   *
   * @param rowIndex The row whose index will be decremented
   * @return The new row index
   */
  public int moveRowUp(int rowIndex) {
    int newIndex = getLogicInst().moveRowUp(rowIndex,kindStr);
    fireTableDataChanged();
    return newIndex;
  }

  /**
   * Increments the index of a row and forwards the change to listeners.
   *
   * @param rowIndex The row whose index will be incremented
   * @return The new row index
   */
  public int moveRowDown(int rowIndex) {
    int newIndex = getLogicInst().moveRowDown(rowIndex,kindStr);
    fireTableDataChanged();
    return newIndex;
  }

  /**
   * Returns the value for the cell at columnIndex and rowIndex.
   *
   * @param rowIndex The row whose value is to be queried
   * @param columnIndex The column whose value is to be queried
   * @return The value at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    columnIndex = getRealColumnValue(columnIndex);
    if (columnIndex == getLogicInst().ROW_COL) { return rowIndex; }
    Object o = getLogicInst().getValueAt(rowIndex,columnIndex,kindStr);
    return o;
  }

  /**
   * Sets the value in the cell at columnIndex and rowIndex.
   *
   * @param value The new value
   * @param rowIndex The row whose value is to be changed
   * @param columnIndex The column whose value is to be changed
   */
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    columnIndex = getRealColumnValue(columnIndex);
    getLogicInst().setData(value,rowIndex,columnIndex,kindStr);
    fireTableCellUpdated(rowIndex,columnIndex);
  }

  /**
   * Returns true if the cell at rowIndex and columnIndex is editable.
   *
   * @param rowIndex The row whose value is to be queried
   * @param columnIndex The column whose value is to be queried
   * @return True if the cell is editable
   */
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    columnIndex = getRealColumnValue(columnIndex);
    if (isRegen) {
      return (columnIndex != getLogicInst().ROW_COL &&
          columnIndex != RegenerationData.SPECIES_CODE_COL);
    } else {
      return (columnIndex != getLogicInst().ROW_COL);
    }
  }

  /**
   * Returns true if logic data is present for the model's kind of knowledge.
   */
  public boolean isDataPresent() {
    return getLogicInst().isDataPresent(kindStr);
  }

}

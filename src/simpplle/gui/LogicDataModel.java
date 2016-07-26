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
 * This class has methods the Logic Data Model.  It is subclass of Abstract Table Model
 * rows in this table correspond to a the logic data kind.  Row count is achieved by counting the logic data kinds. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see  javax.swing.table.AbstractTableModel
 */

public class LogicDataModel extends AbstractTableModel {
  private AbstractBaseLogic logicInst;
  private String    kindStr;
  private JTable    table;
  private boolean   isRegen=false;
/**
 * Constructor for Logic Data Model.  It takes in a string representing process kind, abstract base logic instance, and Jtable.
 * @param kind
 * @param logicInst
 * @param table
 */
  public LogicDataModel(String kind, AbstractBaseLogic logicInst, JTable table) {
    this.kindStr   = kind;
    this.logicInst = logicInst;
    this.table     = table;
  }
/**
 * Gets the process kind in the Logic Data Model.
 * @return
 */
  public String getKind() { return kindStr; }
  /**
   * Sets whether it is regeneration.  
   * @param regen true if is regeneration.  
   */
  public void setIsRegen(boolean regen) { isRegen = regen; }

  /**
   * Gets the logic instance.  If it is a regeneration instance gets regeneration logic instance for the process.  Otherwise returns the logic instance for the data model.
   * @return
   */
  public AbstractBaseLogic getLogicInst() {
    return ( (isRegen) ? RegenerationLogic.getLogicInstance(kindStr) : logicInst);
  }
/**
 * This gets the column model for this logic data instance and then gets a particular column and returns the column identifier.  If the column identifier is a 
 * string it will get the column integer number from this string.  
 * @param col
 * @return
 */
  private int getRealColumnValue(int col) {
    TableColumn column = table.getColumnModel().getColumn(col);
    if (column.getIdentifier() instanceof String) {
      return getLogicInst().getColumnNumFromName((String)column.getIdentifier());
    }

    return (Integer)column.getIdentifier();
  }
/**
 * Counts the number of visible columns for a particular logic data kind.  
 */
  public int getColumnCount() {
    return getLogicInst().getColumnCount(kindStr);
  }
/**
 * First checks to make sure there is data of the particular logic data kind.    
 */
  public Class getColumnClass(int c) {
    if (getLogicInst().isDataPresent(kindStr)) {
      return getValueAt(0, c).getClass();
    }
    return Object.class;
  }
/**
 * If is a regeneration logic data instance and the column default 0 column or past the base logic columns will return false, else true.  Otherwise if 
 * the column is anything but the default 0 column will return true.   
 */
  public boolean isCellEditable(int row, int col) {
    col = getRealColumnValue(col);
    if (isRegen) {
      return (col != getLogicInst().ROW_COL &&
              col != RegenerationData.SPECIES_CODE_COL);
    }
    else {
      return (col != getLogicInst().ROW_COL);
    }
  }
/**
 * Gets the value of an object at a particular cell (row and column entered as parameter).  
 * 
 */
  public Object getValueAt(int row, int col) {
    col = getRealColumnValue(col);
    if (col == getLogicInst().ROW_COL) { return row; }
    Object o = getLogicInst().getValueAt(row,col,kindStr);
    return o;
  }
/**
 * 
 * @param row
 * @return Gets the value of a particular row based on the logic data instance and kind. 
 */
  public AbstractLogicData getValueAt(int row) {
    return getLogicInst().getValueAt(row,kindStr);
  }
/**
 * Sets the object in a particular cell.  Then notifies all listeners for the table that that particular cell has been updated.  
 * 
 */
  public void setValueAt(Object value, int row, int col) {
    col = getRealColumnValue(col);
    getLogicInst().setData(value,row,col,kindStr);
    fireTableCellUpdated(row,col);
  }
  /**
   * Gets the current logic data instance.  If it is not null gets the row count by counting the size of the data kind arraylist.  
   */
  public int getRowCount() {
    AbstractBaseLogic inst = getLogicInst();
    return (inst != null) ? inst.getRowCount(kindStr) : 0;
  }
/**
 * Adds a row and then notifies all listeners for the table that a row has been added between the 0th row and the Nth row.  
 * @param insertPos the position where the row will be inserted
 */
  public void addRow(int insertPos) {
    getLogicInst().addRow(insertPos,kindStr);
    fireTableRowsInserted(0, getRowCount());
    fireTableDataChanged();
  }
  /**
   * Duplicates a row by taking in the row, and insertion position.  
   * Then notifies all listeners for the table that a row has been duplicated  between the 0th row and the Nth row.
   * @param row
   * @param insertPos
   */
  public void duplicateRow(int row, int insertPos) {
    getLogicInst().duplicateRow(row,insertPos,kindStr);
    fireTableRowsInserted(0, getRowCount());
    fireTableDataChanged();
  }
  /**
   * Deletes a particular row.
   * @param row the row to be deleted.  
   */
  public void deleteRow(int row) {
    getLogicInst().removeRow(row,kindStr);
    fireTableRowsDeleted(row,row);
  }
  /**
   * Moves a row upward.  Then notifies listeners for a table that all cells in the table have been changed.  
   * @param row the row to be moved upward
   * @return the integer location of the new row
   */
  public int moveRowUp(int row) {
    int newRow = getLogicInst().moveRowUp(row,kindStr);
    fireTableDataChanged();
    return newRow;
  }
  /**
   * Moves a row downward.  Then notifies listeners for a table that all cells in the table have been changed.  
   * @param row the number of row to be moved downward
   * @return the integer location of the new row
   */
  public int moveRowDown(int row) {
    int newRow = getLogicInst().moveRowDown(row,kindStr);
    fireTableDataChanged();
    return newRow;
  }
  /**
   * Gets the string literal column name from the integer value of the column.  If it is null will return the abstract table model column name, if not returns
   * the logical instance column name.  
   */
  public String getColumnName(int column) {
    String result = getLogicInst().getColumnName(kindStr,column);
    return ((result == null) ? super.getColumnName(column) : result);
  }
/**
 * Checks to see if there is data present in Logic Data Model.  This will be true if either this is regeneration data or instance data 
 * @return true if data is present.  
 */
  public boolean isDataPresent() {
    return getLogicInst().isDataPresent(kindStr);
  }

  /**
   * This method is called with the correct column value.  It adds a visible column to the data model.  
   * @param col column # to be added. 
   */

  public void addVisibleColumn(int col) {
    getLogicInst().addVisibleColumn(kindStr,col);
  }
  /**
   * This method is called with the correct column value.  It removes a visible column to the data model.  
   * @param col column # to be removed. 
   */

  public void removeVisibleColumn(int col) {
    getLogicInst().removeVisibleColumn(kindStr,col);
  }
  /**
   * This method is called with the correct column value.  It adds a visible column to the data model.  
   * @param col column # to be added. 
   */

  /**
   * Gets an array of all the visible column.  
   * @return
   */
  public int[] getVisibleColumns() {
    return getLogicInst().getVisibleColumns(kindStr);
  }
  /**
   * This method is called with the correct column value.  It checks if the column number corresponds to a visible column..  
   * @param col column # to bechecked for visibilty. 
   */

  public boolean isVisibleColumn(int col) {
    return getLogicInst().isVisibleColumn(kindStr,col);
  }

}

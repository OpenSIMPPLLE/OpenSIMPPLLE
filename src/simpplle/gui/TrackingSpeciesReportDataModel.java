/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.Species;
import simpplle.comcode.SimpplleType;
import javax.swing.table.AbstractTableModel;
import simpplle.comcode.TrackingSpeciesReportData;
import java.util.ArrayList;

/** 
 * This class creates a Species Table Data Model, an extension of the Abstract table model.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class TrackingSpeciesReportDataModel extends AbstractTableModel {
  public static final int SPECIES_COL   = TrackingSpeciesReportData.Columns.SPECIES.ordinal();
  public static final int START_PCT_COL = TrackingSpeciesReportData.Columns.START_PCT.ordinal();
  public static final int END_PCT_COL   = TrackingSpeciesReportData.Columns.END_PCT.ordinal();
  public static final int CATEGORY_COL  = TrackingSpeciesReportData.Columns.CATEGORY.ordinal();

  private TrackingSpeciesReportData getData() {
    return TrackingSpeciesReportData.getInstance();
  }
  /**
   * Constructor for Tracking Species Report Data Model.  Creates an instance of Tracking species report data
   */
  public TrackingSpeciesReportDataModel() {
    TrackingSpeciesReportData data = TrackingSpeciesReportData.getInstance();
    if (data == null) {
      TrackingSpeciesReportData.makeInstance();
      TrackingSpeciesReportData.updateAllSpeciesList();
      data.initialize();
    }
  }
/**
 * Gets all the tracking species in the tracking species reports arraylist.
 * @return the arraylist with all the tracking species in it.  
 */
  public ArrayList<Species> getAllSpeciesList() {
    return TrackingSpeciesReportData.getAllSpeciesList();
  }
/**
 * Checks if there is tracking species report data.  
 * @return true if there is tracking species report data. 
 */
  public boolean isDataPresent() { return getData() != null; }
/**
 * Returns 4.  This represents the following columns: SPECIES, CATEGORY, START_PCT, END_PCT
 */
  public int getColumnCount() {
    return TrackingSpeciesReportData.getColumnCount();
  }
/**
 * Gets the class of a particular column.  Choices for this are 0= SPECIES = Species, 1 = CATEGORY = String, 2 = START_PCT = Integer, 3= END_PCT = Integer or Object by default
 */
  public Class getColumnClass(int c) {
    return TrackingSpeciesReportData.getColumnClass(c);
  }
/**
 * As long is column is not the species column, the cell located by parameter row and column can be edited.
 */
  public boolean isCellEditable(int row, int col) {
    return (col != SPECIES_COL);
  }
  /**
   * Gets the object value at a particular row located by the input row and column.  
   */
  public Object getValueAt(int row, int c) {
    Object o = getData().getValueAt(row,c);
    if (o == null) { o = new Object();}

    return o;
  }

  public void setValueAt(Object value, int row, int col) {
    getData().setValueAt(row, col, value);
    fireTableCellUpdated(row, col);
    if (col == START_PCT_COL || col == END_PCT_COL) {
      fireTableCellUpdated(row,CATEGORY_COL);
    }
  }
  /**
   * Gets the row count by using the size() method for the Category arraylist containing the Tracking Species Categories objects.
   */
  public int getRowCount() {
    if (getData() == null) { return 0; }

    return getData().getRowCount();
  }
  /**
   * Returns the column names as string.  Choices are: "Track Species", "Start %", "End %", "Category Name".
   */
  public String getColumnName(int column) {
    return TrackingSpeciesReportData.getColumnNames(column);
  }
/**
 * Duplicates a row.  Then notifies all listeners that a row has been inserted and notifies all listeners for the table that the table data has changed.   
 * @param row the row to be duplicated
 * @param insertPos
 */
  public void duplicateRow(int row, int insertPos) {
    getData().duplicateRow(row,insertPos);
    fireTableRowsInserted(0, getRowCount());
    fireTableDataChanged();
  }
  /**
   * Adds a row.   Then notifies all listeners that a row has been inserted and notifies all listeners for the table that the table data has changed.   
   * @param insertPos
   * @param species
   */
  public void addRow(int insertPos, Species species) {
    getData().addRow(insertPos, species);
    fireTableRowsInserted(0, getRowCount());
    fireTableDataChanged();
  }
  /**
   * Deletes a particular row.  Notifies all listeners for the data model that a row has been deleted.  
   * @param row
   */
  public void deleteRow(int row) {
    getData().removeRow(row);
    fireTableRowsDeleted(row,row);
  }
  /**
   * Moves a row up, then notifies all listeners that the table data has changed.  
   * @param row
   * @return the new row
   */
  public int moveRowUp(int row) {
    int newRow = getData().moveRowUp(row);
    fireTableDataChanged();
    return newRow;
  }
  /**
   * Moves a row down.  Then notifies all listeners that the table data has changed. 
   * @param row
   * @return
   */
  public int moveRowDown(int row) {
    int newRow = getData().moveRowDown(row);
    fireTableDataChanged();
    return newRow;
  }

}

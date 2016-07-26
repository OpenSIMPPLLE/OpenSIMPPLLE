/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.AbstractTableModel;
import simpplle.comcode.BisonGrazing;
import simpplle.comcode.Species;

/** 
 * This class defines the Logic Data Model for Bison Grazing.  It is a type of Abstract Table Model.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see javax.swing.table.AbstractTableModel
 */

public class BisonGrazingLogicDataModel extends AbstractTableModel {
  protected BisonGrazing.DataKind dataKind;
  private BisonGrazingLogicEditor editor;

  /**
   * Constructor for Bison Grazing Logic Data Model.  Inherits from javax.swing.table.AbstractTableModel.AbstractTableModel()
   * methods which need to be implemented are getRowCount(), getColumnCount(), getValueAt(int row, int column);
   * @see javax.swing.table.AbstractTableModel.AbstractTableModel()
  */
  
  public BisonGrazingLogicDataModel() {
    super();
  }
/**
 * Sets the bison grazing logic editor to parameter editor 
 * @param editor the editor the bison grazing logic is set to.
 */
  public void setEditor(BisonGrazingLogicEditor editor) {
    this.editor = editor;
  }
  /**
   * Sets the bison grazing data kind to parameter data kind
   * @param dataKind
   */
  public void setDataKind(BisonGrazing.DataKind dataKind) {
    this.dataKind = dataKind;
  }
/**
 * gets the column class from bison grazing.  
 */
  public Class<?> getColumnClass(int columnIndex) {
    return BisonGrazing.getColumnClass(columnIndex);
  }
/**
 * gets the column count using the bison grazing editor data kind.  
 */
  public int getColumnCount() {
    return BisonGrazing.getColumnCount(dataKind);
  }
  /**
   * gets the column named at specified index.  
   */

  public String getColumnName(int columnIndex) {
    return BisonGrazing.getColumnName(dataKind,columnIndex);
  }
/**
 * Gets the bison grazing editor row count based on the current data kind.  
 */
  public int getRowCount() {
    return BisonGrazing.getRowCount(dataKind);
  }
/**
 * Gets the object in a particular cell designated by row and column indexes. 
 */
  public Object getValueAt(int rowIndex, int columnIndex) {
    return BisonGrazing.getValueAt(dataKind,rowIndex,columnIndex);
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return BisonGrazing.isCellEditable(dataKind,rowIndex,columnIndex);
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    boolean goodTotals = BisonGrazing.setValueAt(dataKind,aValue,rowIndex,columnIndex);
    fireTableCellUpdated(rowIndex,columnIndex);
    editor.updateWeightMatchMessage(dataKind,goodTotals);
  }
/**
 * ensures the dataKind is a Bison Grazing species probability, if so adds to species to bison grazing GUI
 * @param species
 */
  public void addRow(Species species) {
    if (dataKind != BisonGrazing.DataKind.SPECIES_PROB) { return; }
    if (BisonGrazing.addRow(species)) { fireTableDataChanged(); }
  }
  /**
   * ensures the dataKind is a Bison Grazing species probability, if so deletes the row in bison grazing GUI
   * @param row
   */
  public void deleteRow(int row) {
    if (dataKind != BisonGrazing.DataKind.SPECIES_PROB) { return; }
    BisonGrazing.deleteRow(row);
    fireTableDataChanged();
  }
  /**
   * deletes multiple rows.  ensures the dataKind is a Bison Grazing species probability, if so deletes the rows in bison grazing GUI
   * @param rows integer array of rows to be deleted
   */
  public void deleteRows(int[] rows) {
    if (dataKind != BisonGrazing.DataKind.SPECIES_PROB) { return; }
    BisonGrazing.deleteRows(rows);
    fireTableDataChanged();
  }

}



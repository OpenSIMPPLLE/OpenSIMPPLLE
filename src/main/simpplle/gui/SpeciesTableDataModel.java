/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.Species;
import simpplle.JSimpplle;
import javax.swing.table.*;
import java.util.HashMap;
import simpplle.comcode.SimpplleType;

/** 
 * This class creates a Species Table Data Model, an extension of the Abstract table model.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class SpeciesTableDataModel extends AbstractTableModel {
  private static final HashMap dummy = new HashMap(1);
/**
 * Constructor for the SpeciesTableDataModel object.  Does not initialize any variables.  
 */
  public SpeciesTableDataModel() {
  }
  /**
   * This is a static final variable from the Species.java class.  It will return 7
   */
  public int getColumnCount() {
    return Species.COLUMN_COUNT;
  }
  /**
   * Gets the object value at row 0 and parameter column Id.  Choices for this are:Hashmap = 0, String = 1, String  = 2, Fire resistance = 3,
   * HashMap = 4, Boolean = 5, Boolean = 6; Will return the dummy class of Hashmap if class at a particular cell is null.
   */
  public Class getColumnClass(int c) {
    Object value = getValueAt(0, c);
    return (value != null) ? value.getClass() : dummy.getClass();
  }
  /**
   * If the cell is located in DESCRIPTION_COL = 1, LIFEFORM_COL = 2, FIRE_RESISTANCE_COL = 3, RESISTANCE_COND_COL = 4, INVASIVE_COL  = 5;
   */
  public boolean isCellEditable(int row, int col) {
    return ((col != Species.CODE_COL) &&
            (col != Species.PATHWAY_PRESENT_COL));
  }
  /**
   * Gets the species object at a particular cell found using parameter row and column indexes.  
   */
  public Object getValueAt(int row, int col) {
    Object o = SimpplleType.getValueAt(row,col,SimpplleType.SPECIES);
    return o;
  }
  /**
   * Sets the species object at a particular cell. 
   */
  public void setValueAt(Object value, int row, int col) {
    SimpplleType.setValueAt(value,row,col,SimpplleType.SPECIES);
    fireTableCellUpdated(row,col);
  }
/**
 * Calculates the row count by using the size() method for the arraylist of species objects.
 */
  public int getRowCount() {
    return SimpplleType.getRowCount(SimpplleType.SPECIES);
  }
  /**
   * Gets the name of the column by column Id.  Choices from this are "Species", "Description", "Lifeform Type", "Fire Resistance", "Conditional Fire Resistance";
   * "Invasive", "Pathway Present";
   */
  public String getColumnName(int column) {
    String result = Species.getColumnName(column);
    return ((result == null) ? super.getColumnName(column) : result);
  }

//  public void addRow(Species species, int insertPos) {
//    RegenerationLogic.addDataRow(species,insertPos);
//    fireTableRowsInserted(insertPos,insertPos);
//  }
//  public void addRows(Vector v) {
//    int startRow = getRowCount();
//    for (int i=0; i<v.size(); i++) {
//      RegenerationLogic.addDataRow((Species)v.elementAt(i),getRowCount());
//    }
//    fireTableRowsInserted(startRow,getRowCount());
//    fireTableDataChanged();
//  }
  /**
   * Deletes species objectdata at a particular row. It removes the simpplle type object from the particular hash map of simpplle type. 
   * Then removes the value at particular row from the all types list and all types hash map.
   * @param row
   */
  public void deleteRow(int row) {
    SimpplleType.deleteDataRow(row,SimpplleType.SPECIES);
    fireTableRowsDeleted(row,row);
  }
}

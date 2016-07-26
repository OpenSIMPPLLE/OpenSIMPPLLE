/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.InvasiveSpeciesLogicData;
import javax.swing.table.AbstractTableModel;
import simpplle.comcode.SoilType;
import java.io.File;

/**
 * This class creates a SoilType Data Model, an extension of the Abstract table model.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class SoilTypeDataModel extends AbstractTableModel {
  public static final int SOIL_TYPE_COL = 0;
  public static final int SELECTED_COL  = 1;

  protected InvasiveSpeciesLogicData logicData=null;
  /**
   * Constructor for the SoilTypeTableDataModel object.  Does not initialize any variables.  
   */
  public SoilTypeDataModel() {
  }
/**
 * Sets the logic data for for this soil type data model to invasive species logic data instance. 
 * @param logicData
 */
  public void setLogicData(InvasiveSpeciesLogicData logicData) {
    this.logicData = logicData;
  }
/**
 * Gets the column count.  Will be 1 if invasive species logic data is null, 2 otherwise.  
 */
  public int getColumnCount() {
    if (logicData == null) {
      return 1;
    }
    return 2;
  }
  /**
   * Gets the object value at row 0 and parameter column Id.  This will be 
   */
  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  public boolean isCellEditable(int row, int col) {
    return (col == SELECTED_COL);
  }
  public Object getValueAt(int row, int c) {
    if (c == SELECTED_COL) {
      SoilType soilType = SoilType.getValueAt(row);
      return logicData.isMemberSoilType(soilType);
    }
    else if (c == SOIL_TYPE_COL) {
      return getValueAt(row).toString();
    }

    return null;
  }
  /**
   * Gets teh value at a p
   * @param row
   * @return
   */
  public SoilType getValueAt(int row) {
    return SoilType.getValueAt(row);
  }
/**
 * Sets a soil type object at a particular cell.  If selected this adds a soil type, if not removes it.  THen notifies all listeners that the cell at
 * a particular location has been updated.  
 */
  public void setValueAt(Object value, int row, int col) {
    if (logicData == null) { return; }
    if (col == SELECTED_COL) {
      SoilType soilType = (SoilType) getValueAt(row);
      Boolean selected = (Boolean)value;
      if (selected) {
        logicData.addSoilType(soilType);
      }
      else {
        logicData.removeSoilType(soilType);
      }
      fireTableCellUpdated(row,col);
    }
  }
  /**
   * Gets the row count by using the size() method of the soil type arraylist.
   */
  public int getRowCount() {
    return SoilType.getRowCount();
  }
  /**
   * Gets the column name from the column Id.  Choises for this are "Chosen" and "Soil Type"
   */
  public String getColumnName(int column) {
    switch (column) {
      case SELECTED_COL:  return "Chosen";
      case SOIL_TYPE_COL: return "Soil Type";
      default: return "";
    }
  }
/**
 * Adds a row to this soil type data model.  This represents a new soil type which will has the name passed in parameter.  
 * Then notifies all the table listeners that the table data has changed.  
 * @param value
 */
  public void addRow(String value) {
    SoilType.add(value);
    fireTableDataChanged();
  }
  /**
   * Adds rows from file by using a scanner to read in the text file. 
   * Then notifies all the table listeners that the table data has changed.  
   * @param file
   */
  public void addRowsFromFile(File file) {
    SoilType.parseTextFile(file);
    fireTableDataChanged();
  }


}

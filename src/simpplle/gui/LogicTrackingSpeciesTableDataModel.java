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
import simpplle.comcode.LogicData;
import javax.swing.table.AbstractTableModel;

/**
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 *
 * @see javax.swing.table.AbstractTableModel
 */
public class LogicTrackingSpeciesTableDataModel extends AbstractTableModel {
  public static final int SELECTED_COL = 0;
  public static final int SPECIES_COL  = 1;
  public static final int PERCENT_COL  = 2;

  protected LogicData logicData;
/**
 * Constructor for Tracking Species Table Data Model.  Does not initialize any variables.  
 */
  public LogicTrackingSpeciesTableDataModel() {
  }
/**
 * Sets the logic data for this tracking species table data model.  
 * @param logicData
 */
  public void setLogicData(LogicData logicData) {
    this.logicData = logicData;
  }
/**
 * Gets the column count.  This is 3.  They are SELECTED_COL, SPECIES_COL, PERCENT_COL.
 */
  public int getColumnCount() {
    return 3;
  }
/**
 * Gets the object class at 0th row and column id parameter.  
 */
  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }
/**
 * Checks if a cell is editable.  Cell is located using row and column parameters.  Returns true if this is SELECTED_COL OR PERCENT_COL.
 */
  public boolean isCellEditable(int row, int col) {
    return (col == SELECTED_COL || col == PERCENT_COL);
  }
  /**
   * Gets the object at a particular cell located with the row and column parameters.  
   * If the column id is SELECTED_COL
   */
  public Object getValueAt(int row, int c) {
    if (c == SELECTED_COL) {
      Species species = (Species)getValueAt(row);
      return logicData.hasTrackingSpecies(species);
    }
    else if (c == PERCENT_COL) {
      Species species = (Species) getValueAt(row);
      if (logicData.hasTrackingSpecies(species)) {
        return logicData.getTrackingSpeciesPercent(species);
      }
      return 0;
    }
    else {
       Object o = SimpplleType.getValueAt(row,Species.CODE_COL,
                                          SimpplleType.Types.SPECIES);
      return o;
    }
  }
  public SimpplleType getValueAt(int row) {
    return SimpplleType.getValueAt(row,SimpplleType.Types.SPECIES);
  }

  public void setValueAt(Object value, int row, int col) {
    if (logicData == null) { return; }
    if (col == PERCENT_COL) {
      Species species = (Species) getValueAt(row);
      logicData.addTrackingSpecies(species,(Integer)value);
      fireTableCellUpdated(row,col);
    }
    else if (col == SELECTED_COL) {
      Species species = (Species) getValueAt(row);
      Boolean selected = (Boolean)value;
      if (selected) {
        logicData.addTrackingSpecies(species,0);
      }
      else {
        logicData.removeTrackingSpecies(species);
      }
      fireTableCellUpdated(row,col);
    }
  }
  /**
   * Calculates the row count by getting the arraylist of species in an area
   */
  public int getRowCount() {
    return SimpplleType.getRowCount(SimpplleType.Types.SPECIES);
  }
  public String getColumnName(int column) {
    switch (column) {
      case SELECTED_COL: return "Chosen";
      case PERCENT_COL:  return "Percent";
      case SPECIES_COL:  return "Species";
      default: return "";
    }
  }


}

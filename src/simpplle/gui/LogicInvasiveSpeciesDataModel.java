/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.AbstractTableModel;

import simpplle.comcode.*;

/** 
 * This class has methods for Invasive Species Logic Data Model, a type of Abstract Table Model.
 * Only two regions currently support invasive species logic.  They are Western Region 1 and Teton.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class LogicInvasiveSpeciesDataModel extends AbstractTableModel {
  public static final int INVASIVE_SPECIES_COL = 0;
  public static final int SELECTED_COL         = 1;

  protected LogicData logicData=null;
  protected LogicInvasiveSpeciesChooser dialog;
/**
 * Primary constructof for Invasive Species Logic Data Model.  It is a default constructor with no initialization.  
 */
  public LogicInvasiveSpeciesDataModel() {
  }
/**
 * Sets the logic data for Invasive Species Logic Data Model.  
 * @param logicData the logic data to be used.
 */
  public void setLogicData(LogicData logicData) {
    this.logicData = logicData;
  }
/**
 * Sets the Invasive Species Logic Data Model to Invasive Species Chooser dialog 
 * @param dialog 
 */
  public void setDialog(LogicInvasiveSpeciesChooser dialog) {
    this.dialog = dialog;
  }
/**
 * Gets column count, which is 2 by default.
 */
  public int getColumnCount() {
    return 2;
  }
/**
 * Gets the object class for oth row, cth column (input in parameter)
 */
  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }
/**
 * Checks to see if particular cell is editable.  
 */
  public boolean isCellEditable(int row, int col) {
    return (col == SELECTED_COL);
  }
  /**
   * Gets the object, either null, a boolean or invasive species toString in a particular cell, designated by the row and column input in parameters.  
   * If c is selected column (column index 1) and logic data is invasive species or invasive species change logic and row process is a member of 
   * invasive species logic returns true.  If column choosen is Invasive Species column (index 0)(automatically true) gets the invasive species toString.
   * 
   */
  public Object getValueAt(int row, int c) {
    if (c == SELECTED_COL) {
      Species species = Species.getInvasiveSpecies(row);
      if (logicData instanceof InvasiveSpeciesLogicData) {
        return ((InvasiveSpeciesLogicData)logicData).isMemberInvasiveSpecies(species);
      }
      else if (logicData instanceof InvasiveSpeciesChangeLogicData) {
        return ((InvasiveSpeciesChangeLogicData)logicData).isMemberInvasiveSpecies(species);
      }
      return false;
    }
    else if (c == INVASIVE_SPECIES_COL) {
      return Species.getInvasiveSpecies(row).toString();
    }

    return null;
  }
/**
 * Sets the value in a particular cell.  Casts the object to a boolean if column is SELECTED_COL (index 1).  Since this boolean will be ture if instance of invasive species
 * it adds the species in row to a the invasive species list.  If false it removes the species from invasive species list.  
 * Then notifies all table listeners that there has been a change to the table.  
 */
  public void setValueAt(Object value, int row, int col) {
    if (logicData == null) { return; }
    if (col == SELECTED_COL) {
      Species species = Species.getInvasiveSpecies(row);
      Boolean selected = (Boolean)value;

      if (selected) {
        if (logicData instanceof InvasiveSpeciesLogicData) {
          ((InvasiveSpeciesLogicData)logicData).addInvasiveSpecies(species);
        }
        else if (logicData instanceof InvasiveSpeciesChangeLogicData) {
          ((InvasiveSpeciesChangeLogicData)logicData).addInvasiveSpecies(species);
        }
      }
      else {
        if (logicData instanceof InvasiveSpeciesLogicData) {
          InvasiveSpeciesLogicData data = (InvasiveSpeciesLogicData)logicData;
          if (data.getInvasiveSpeciesCount() > 1) {
            data.removeInvasiveSpecies(species);
          }
        }
        else if (logicData instanceof InvasiveSpeciesChangeLogicData) {
          InvasiveSpeciesChangeLogicData data = (InvasiveSpeciesChangeLogicData)logicData;
          if (data.getInvasiveSpeciesCount() > 1) {
            data.removeInvasiveSpecies(species);
          }
        }
      }
      dialog.updateDialog();
      fireTableCellUpdated(row,col);
    }
  }
  /**
   * Gets the row count by returning the size of invasive species arraylist.  
   */
  public int getRowCount() {
    return Species.getInvasiveSpeciesRowCount();
  }
  /**
   * Returns the string name of a column.  Column 1 = selected column - returns Chosen, else index 0 = invasive species
   */
  public String getColumnName(int column) {
    switch (column) {
      case SELECTED_COL:         return "Chosen";
      case INVASIVE_SPECIES_COL: return "Invasive Species";
      default: return "";
    }
  }

}

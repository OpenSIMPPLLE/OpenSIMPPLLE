package simpplle.gui;

import javax.swing.table.AbstractTableModel;

import simpplle.comcode.logic.LogicData;
import simpplle.comcode.SimpplleType;
import simpplle.comcode.Density;
import simpplle.comcode.Species;
import simpplle.comcode.SizeClass;
import simpplle.comcode.ProcessType;
import simpplle.comcode.TreatmentType;
import simpplle.comcode.HabitatTypeGroupType;
import simpplle.comcode.logic.ProcessProbLogicData;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This abstract class creates the Simpplle Type Table Data Model, a type of Abstract Table Model.
 * Table models are used throughout the simple GUI and will be subclassed often.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */

public abstract class SimpplleTypeTableDataModel extends AbstractTableModel {
  public static final int SELECTED_COL  = 0;

  protected LogicData logicData;
  protected SimpplleType.Types kind;
  protected boolean            nonBaseCol=false;
/**
 * Constructor for simpplle type data model 
 * @param kind
 */
  public SimpplleTypeTableDataModel(SimpplleType.Types kind) {
    this.kind = kind;
  }
/**
 * Sets the logic data to be used in a particular table (or dialog).
 * @param logicData
 */
  public void setLogicData(LogicData logicData) {
    this.logicData = logicData;
  }
/**
 * Sets the boolean for non base column which represents an adjacent process.  
 * @param adjProcessCol true if adjacent process 
 */
  public void setNonBaseCol(boolean adjProcessCol) {
    this.nonBaseCol = adjProcessCol;
  }
/**
 * Gets the column id.  
 * @param c the column to have its id found
 * @return column id
 */
  protected abstract int getCol(int c);
/**
 * Gets the class of a particular column.  Uses 0 to represent row, and c for column id.  
 */
  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }
/**
 * True if column is Selected_Col.  By default this is 0.
 */
  public boolean isCellEditable(int row, int col) {
    return col == SELECTED_COL;
  }
/**
 * Gets the object at a particular cell.  This will either be a boolean object if it is the SELECTED_COL or the SimpplleType object in the cell if not.
 *  
 *  */
  public Object getValueAt(int row, int c) {
    if (c == SELECTED_COL) {
      SimpplleType item = getValueAt(row);
      if (logicData != null && isMember(item)) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    int col = getCol(c);
    Object o = SimpplleType.getValueAt(row,col,kind);
    return o;
  }
/**
 * Method to check if parameter SimpplleType object is a member of a particular group.  If it is a non base column checks if it is an adjacent process
 * or an invasive species.  If it is a base column checks to make sure is a member of simpplle types 
 * @param item
 * @return
 */
  private boolean isMember(SimpplleType item) {
    if (logicData instanceof ProcessProbLogicData && nonBaseCol) {
      return ((ProcessProbLogicData)logicData).isMemberAdjProcess((ProcessType)item);
    }
    else if (logicData instanceof simpplle.comcode.logic.InvasiveSpeciesLogicDataMSU && nonBaseCol) {
      return ((simpplle.comcode.logic.InvasiveSpeciesLogicDataMSU)logicData).isMemberSimpplleType(item);
    }
    return logicData.isMemberSimpplleType(item,kind);
  }
  /**
   * If a process is selected from a non base column it is added to either the adjacent processes list or invasive species simplle type list (for MSU).
   * If a process is selected from a base column, it adds the process to simpplle type arraylist
   * If process is not selected and is a not a base column, will remove from adjacent processes of invasive species simpplle types list (for MSU)
   * @param selected true if user selected a process
   * @param row the row where process is found
   */
  private void processSelected(boolean selected, int row) {
    if (selected) {
      if (logicData instanceof ProcessProbLogicData && nonBaseCol) {
        ((ProcessProbLogicData)logicData).addAdjProcess((ProcessType)getValueAt(row));
      }
      else if (logicData instanceof simpplle.comcode.logic.InvasiveSpeciesLogicDataMSU && nonBaseCol) {
        ((simpplle.comcode.logic.InvasiveSpeciesLogicDataMSU)logicData).addSimpplleType(getValueAt(row));
      }
      else {
        logicData.addSimpplleType(getValueAt(row), kind);
      }
    }
    else {
      if (logicData instanceof ProcessProbLogicData && nonBaseCol) {
        ((ProcessProbLogicData)logicData).removeAdjProcess((ProcessType)getValueAt(row));
      }
      else if (logicData instanceof simpplle.comcode.logic.InvasiveSpeciesLogicDataMSU && nonBaseCol) {
        ((simpplle.comcode.logic.InvasiveSpeciesLogicDataMSU)logicData).removeSimpplleType(getValueAt(row));
      }
      else {
        logicData.removeSimpplleType(getValueAt(row), kind);
      }
    }
  }
/**
 * Gets the value at a particular cell based on input row and current kind. 
 * @param row the row used to locate cell
 * @return the simpplle type at a particular cell
 */
  public SimpplleType getValueAt(int row) {
    return SimpplleType.getValueAt(row,kind);
  }
/**
 * Sets the value at a particular cell.  If selected column 
 */
  public void setValueAt(Object value, int row, int col) {
    if (col != SELECTED_COL || logicData == null) { return; }

    Boolean selected = (Boolean)value;
    processSelected(selected,row);

    fireTableCellUpdated(row,col);
  }
  /**
   * Gets the row count for this particular simpplle type table model.  
   */
  public int getRowCount() {
    return SimpplleType.getRowCount(kind);
  }
  /**
   * Gets the name of column by column ID.  If the column is a simpplle type column will return either Species, size class, density, process, 
   */
  public String getColumnName(int column) {
    int col = getCol(column);
    if (column == SELECTED_COL) { return "Chosen"; }

    switch(kind) {
      case SPECIES:    return Species.getColumnName(col);
      case SIZE_CLASS: return SizeClass.getColumnName(col);
      case DENSITY:    return Density.getColumnName(col);
      case PROCESS:    return ProcessType.getColumnName(col);
      case TREATMENT:  return TreatmentType.getColumnName(col);
      case GROUP: return HabitatTypeGroupType.getColumnName(col);
    }

    return super.getColumnName(column);
  }

//  public void addRow(Density density, int insertPos) {
//  }
//  public void addRows(Vector v) {
//  }
//  public void deleteRow(int row) {
//  }
}

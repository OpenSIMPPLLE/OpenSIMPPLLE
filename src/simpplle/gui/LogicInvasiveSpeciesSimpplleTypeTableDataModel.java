/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.ProcessType;
import simpplle.comcode.SimpplleType;
import simpplle.comcode.InvasiveSpeciesLogicDataMSU;
import simpplle.comcode.*;

/**
 * This class contains methods to handle Invasive Species Simpplle Type Data Model, a type of SimpplleTypeTableDataModel, itself a type of
 * Abstract Table Model.  This allows users to pick the invasive species.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 */

public class LogicInvasiveSpeciesSimpplleTypeTableDataModel extends SimpplleTypeTableDataModel {
  public static final int SIMPPLLE_TYPE_COL = 1;
  public static final int COEFF_COL         = 2;
  public static final int TIME_STEPS_COL    = 3;

  public LogicInvasiveSpeciesSimpplleTypeTableDataModel(SimpplleType.Types kind) {
    super(kind);
  }
/**
 * Gets the column count in Invasive Species Logic SimpplleType TableDataModel.  
 * This returns 4 representing SELECTED_COL (0), SIMPPLLE_TYPE_COL(1), COEFF_COL(2), or TIME_STEPS_COL(3)
 */
  public int getColumnCount() {
    return 4;
  }
/**
 * Checks if a cell is editable, cell is coordinated by row and column entered.  Returns true if column is 0,2, or 3 meaning selected, coefficient, or time steps
 * The later two are text fields which allow for entering.  
 */
  public boolean isCellEditable(int row, int col) {
    return col == SELECTED_COL || col == COEFF_COL || col == TIME_STEPS_COL;
  }

  /**
   * Gets column in table model.  Choices are SELECTED_COL (0), SIMPPLLE_TYPE_COL(1), COEFF_COL(2), or TIME_STEPS_COL(3).  
   * If col = 1, this will get the simpplle type either process or treatment.   
   
   */
  protected int getCol(int c) {
    int col;
    switch(c) {
      case SELECTED_COL:   col = c; break;
      case SIMPPLLE_TYPE_COL:
        col = (kind == SimpplleType.PROCESS ? ProcessType.CODE_COL : TreatmentType.CODE_COL);
        break;
      case COEFF_COL:      col = c; break;
      case TIME_STEPS_COL: col = c; break;
      default: col = c;
    }
    return col;
  }
/**
 * If column is coefficient or time steps, gets the value located at cell designated by parameter row and column and casts to InvasiveSpeciewsLogicDataMSU.  Otherwise will get the value at specified cell
 * by referring to superclass.
 */
  public Object getValueAt(int row, int c) {
    if (c == COEFF_COL) {
      SimpplleType sType = (SimpplleType)super.getValueAt(row,SIMPPLLE_TYPE_COL);
      return ((InvasiveSpeciesLogicDataMSU)logicData).getCoeffData(sType);
    }
    else if (c == TIME_STEPS_COL) {
      SimpplleType sType = (SimpplleType)super.getValueAt(row,SIMPPLLE_TYPE_COL);
      return ((InvasiveSpeciesLogicDataMSU)logicData).getTimeStepsData(sType);
    }
    else {
      return super.getValueAt(row,c);
    }
  }
  /**
   * If coefficient or time step column, refers to the super and gets the value at row and simpplle type column, then sets either the coefficient or time step data
   * and notifies all table listeners that the table cell has been updated.  
   */
  public void setValueAt(Object value, int row, int col) {
    if (col == COEFF_COL) {
      SimpplleType sType = (SimpplleType)super.getValueAt(row,SIMPPLLE_TYPE_COL);
      ((InvasiveSpeciesLogicDataMSU)logicData).setCoeffData(sType,(Double)value);
      fireTableCellUpdated(row,col);

      // In case we indirectly added by editing an uncheck row.
      fireTableCellUpdated(row,SELECTED_COL);
    }
    else if (col == TIME_STEPS_COL) {
      SimpplleType sType = (SimpplleType)super.getValueAt(row,SIMPPLLE_TYPE_COL);
      ((InvasiveSpeciesLogicDataMSU)logicData).setTimeStepsData(sType,(Integer)value);
      fireTableCellUpdated(row,col);

      // In case we indirectly added by editing an uncheck row.
      fireTableCellUpdated(row,SELECTED_COL);
    }
    else {
      super.setValueAt(value,row,col);
    }

  }
  /**
   * Gets the string version of column name.  
   */
  public String getColumnName(int column) {
    if (column == COEFF_COL) { return "Coefficient"; }
    else if (column == TIME_STEPS_COL) { return "Time Steps"; }
    else {
      return super.getColumnName(column);
    }
  }

}

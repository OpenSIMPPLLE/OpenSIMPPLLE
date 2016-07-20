/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.AbstractTableModel;
import simpplle.comcode.VegetativeType;
import simpplle.comcode.ProcessType;
import java.util.ArrayList;
import simpplle.comcode.InclusionRuleSpecies;

/** 
 * This class creates the vegetative pathway diagrams.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class PathwaySpeciesChangeDataModel extends AbstractTableModel {
  VegetativeType vt;
  ArrayList data;
  public PathwaySpeciesChangeDataModel() {
    vt = null;
    data = null;
  }
  public void setData(VegetativeType vt) {
    this.vt = vt;
    this.data = vt.makeSpeciesChangeArray();
  }

  public int getColumnCount() {
    return VegetativeType.getColumnCount();
  }

  public int getRowCount() {
    return (data != null) ? data.size() : 1;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if (data == null) { return ""; }

    Object[] colData = (Object[])data.get(rowIndex);
    return colData[columnIndex];
  }
  public void setValueAt(Object value, int row, int col) {
    if (col != 2 || value == null) { return; }

    Object[] colData = (Object[])data.get(row);
    ProcessType process = (ProcessType)colData[0];
    InclusionRuleSpecies species = (InclusionRuleSpecies)colData[1];
    vt.setSpeciesChange(process,species,(Float)value);
    colData[2] = (Float)value;
    fireTableCellUpdated(row,col);
 }

  public Class getColumnClass(int c) {
    Object o = getValueAt(0,c);
    if (o == null) {
      switch (c) {
        case 0: return ProcessType.class;
        case 1: return InclusionRuleSpecies.class;
        case 2: return Float.class;
        default: return String.class;
      }
    }
    else {
      return getValueAt(0, c).getClass();
    }
  }

  public boolean isCellEditable(int row, int col) {
    return (col > 1);
  }

  public String getColumnName(int column) {
    String result = VegetativeType.getSpeciesChangeColumnName(column);
    return ((result == null) ? super.getColumnName(column) : result);
  }

  public void addRow(ProcessType process, InclusionRuleSpecies species) {
    vt.addSpeciesChange(process,species);
    data = vt.makeSpeciesChangeArray();
    fireTableDataChanged();
  }
  public void deleteRow(int row) {
    Object[] colData = (Object[])data.get(row);
    vt.removeSpeciesChange((ProcessType)colData[0],(InclusionRuleSpecies)colData[1]);
    data = vt.makeSpeciesChangeArray();
    fireTableDataChanged();
  }
  public void deleteRows(int[] rows) {
    for (int row : rows) {
      Object[] colData = (Object[])data.get(row);
      vt.removeSpeciesChange((ProcessType)colData[0],(InclusionRuleSpecies)colData[1]);
    }
    data = vt.makeSpeciesChangeArray();
    fireTableDataChanged();
  }

}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.*;
import simpplle.comcode.RegenerationLogic;
import simpplle.comcode.FireRegenerationData;
import simpplle.comcode.Species;

import java.util.Vector;
import simpplle.comcode.HabitatTypeGroupType;
import simpplle.comcode.RegenerationData;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class is deprecated and is proposed for elimination in OpenSimpplle v1.0
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */

//public class RegenTableDataModel extends AbstractTableModel {
//  RegenerationLogic.DataKinds kind;
//
//  public RegenTableDataModel(RegenerationLogic.DataKinds kind) {
//    this.kind = kind;
//  }
//
//  public int getColumnCount() {
//    return RegenerationLogic.getColumnCount(kind);
//  }
//
//  public Class getColumnClass(int c) {
//    return getValueAt(0, c).getClass();
//  }
//
//  public boolean isCellEditable(int row, int col) {
//    return (col != RegenerationData.SPECIES_CODE_COL);
//  }
//
//  public Object getValueAt(int row, int col) {
//    Object o = RegenerationLogic.getValueAt(row,col,kind);
//    return o;
//  }
//  public void setValueAt(Object value, int row, int col) {
//    RegenerationLogic.setData(value,row,col,kind);
//    fireTableCellUpdated(row,col);
//  }
//  public int getRowCount() {
//    return RegenerationLogic.getRowCount(kind);
//  }
//  public String getColumnName(int column) {
//    String result = RegenerationLogic.getColumnName(column,kind);
//    return ((result == null) ? super.getColumnName(column) : result);
//  }
//
//  public void addRow(Species species, int insertPos) {
//    RegenerationLogic.addDataRow(species,insertPos,kind);
//    fireTableRowsInserted(insertPos,insertPos);
//  }
//  public void addRows(Vector v) {
//    int startRow = getRowCount();
//    for (int i=0; i<v.size(); i++) {
//      RegenerationLogic.addDataRow((Species)v.elementAt(i),getRowCount(),kind);
//    }
//    fireTableRowsInserted(startRow,getRowCount());
//    fireTableDataChanged();
//  }
//  public void deleteRow(int row) {
//    RegenerationLogic.deleteDataRow(row,kind);
//    fireTableRowsDeleted(row,row);
//  }

//}





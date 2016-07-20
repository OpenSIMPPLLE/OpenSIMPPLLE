/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.*;

import java.util.Vector;
import simpplle.comcode.FireSuppWeatherData;

/** 
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class FireSuppWeatherTableDataModel extends AbstractTableModel {

  public FireSuppWeatherTableDataModel() {
  }
  public int getColumnCount() {
    return FireSuppWeatherData.getColumnCount();
  }

  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  public boolean isCellEditable(int row, int col) {
    return (col != FireSuppWeatherData.MIN_ACRES_COL &&
            col != FireSuppWeatherData.MAX_ACRES_COL);
  }

  public Object getValueAt(int row, int col) {
    Object o = FireSuppWeatherData.getValueAt(row,col);
    return o;
  }

  public FireSuppWeatherData getValueAt(int row) {
    return FireSuppWeatherData.getValueAt(row);
  }

  public void setValueAt(Object value, int row, int col) {
    FireSuppWeatherData inst = FireSuppWeatherData.getValueAt(row);
    inst.setValueAt(col,value);
    fireTableCellUpdated(row,col);
  }
  public int getRowCount() {
    return FireSuppWeatherData.getRowCount();
  }
  public String getColumnName(int column) {
    String result = FireSuppWeatherData.getColumnName(column);
    return ((result == null) ? super.getColumnName(column) : result);
  }
}





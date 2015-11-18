

package simpplle.gui;

import simpplle.JSimpplle;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;
import java.util.Enumeration;
import java.util.Collections;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This Class implements common convience utilities used
 * throughout the program.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public abstract class Utility {
  public static Cursor getWaitCursor() {
    return Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
  }
/**
 * Gets the normal cursor
 * @return
 */
  public static Cursor getNormalCursor() {
    return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
  }

  public static File getSaveFile(Component parent, String title) {
    return getSaveFile(parent,title,null);
  }

  // Display a Save file dialog with given title.
  // Returns either null or the chosen File.
  public static File getSaveFile(Component parent, String title,
                                 MyFileFilter extFilter) {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    int          returnVal;

    if (extFilter != null) {
      chooser.setFileFilter(extFilter);
      chooser.setAcceptAllFileFilterUsed(false);
    }
    chooser.setDialogTitle(title);
    returnVal = chooser.showSaveDialog(parent);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      if (extFilter != null) {
        file = addFileExt(file,extFilter.getExtension());
      }
      return file;
    }
    else {
      return null;
    }
  }
  // Display a Save directory dialog with given title.
  // Returns either null or the chosen Directory.
  public static File getSaveDir(Component parent, String title) {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setAcceptAllFileFilterUsed(false);
    int          returnVal;
    chooser.setDialogTitle(title);
    returnVal = chooser.showSaveDialog(parent);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      File dir = chooser.getSelectedFile();
      return dir;
    }
    else {
      return null;
    }
  }

  public static String getFileExt(File file) {
    String name = file.getName();
    int    index = name.lastIndexOf('.');
    if (index == -1) { return ""; }
    return name.substring(index+1);
  }

  public static File addFileExt(File file, String ext) {
    String dir = file.getParent();
    String name = file.getName();
    String str;

    str = getFileExt(file);
    if (ext.equals(getFileExt(file))) {
      return file;
    }
    else {
      return new File(dir,name + "." + ext);
    }
  }

  public static File getOpenFile(Component parent, String title) {
    return getOpenFile(parent,title,null);
  }
  public static File[] getOpenFiles(Component parent, String title) {
    return getOpenFiles(parent,title,null);
  }

  // Display a Open file dialog with given title.
  // Returns either null or the chosen File.
  public static File getOpenFile(Component parent, String title,
                                 javax.swing.filechooser.FileFilter extFilter) {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    int          returnVal;

    if (extFilter != null) {
      chooser.setFileFilter(extFilter);
      chooser.setAcceptAllFileFilterUsed(false);
    }
    chooser.setDialogTitle(title);
    returnVal = chooser.showOpenDialog(parent);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    }
    else {
      return null;
    }
  }
  public static File[] getOpenFiles(Component parent, String title,
                                 javax.swing.filechooser.FileFilter extFilter) {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    int          returnVal;

    if (extFilter != null) {
      chooser.setFileFilter(extFilter);
      chooser.setAcceptAllFileFilterUsed(false);
    }
    chooser.setDialogTitle(title);
    chooser.setMultiSelectionEnabled(true);
    returnVal = chooser.showOpenDialog(parent);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFiles();
    }
    else {
      return null;
    }
  }

  public static boolean askYesNoQuestion(Component parent, String msg, String title) {
    int choice = JOptionPane.showConfirmDialog(parent, msg,
                                               title,
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    return (choice == JOptionPane.YES_OPTION);
  }

  public static String getFileExtension(File file) {
    String name = file.getName();
    int index = name.lastIndexOf(".");
    return (index == -1) ? "" : name.substring(index+1);
  }

  public static void initColumnWidth(JTable logicTable) {
//    logicTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    Enumeration e = logicTable.getColumnModel().getColumns();

    int i=0;
    while (e.hasMoreElements()) {
      initColumnWidth(logicTable,(TableColumn)e.nextElement(),i);
      i++;
    }
  }

  public static void initColumnWidth(JTable logicTable, TableColumn column, int columnIndex) {
    int         width;
    final int   MAX_WIDTH = 250;

    TableModel  model = logicTable.getModel();
    TableCellRenderer headerRenderer = column.getHeaderRenderer();
    if (headerRenderer == null) {
      headerRenderer = logicTable.getTableHeader().getDefaultRenderer();
    }

    int minWidth = 75;
    if (model.getColumnName(columnIndex).equalsIgnoreCase("priority")) {
      minWidth = 20;
    }

    Component comp = headerRenderer.getTableCellRendererComponent(
        logicTable, column.getHeaderValue(),false, false, 0, 0);
    width = comp.getPreferredSize().width;


    // Get maximum width of column data
    for (int r=0; r<logicTable.getRowCount(); r++) {
      TableCellRenderer renderer = logicTable.getCellRenderer(r, columnIndex);
      comp = renderer.getTableCellRendererComponent(
          logicTable, logicTable.getValueAt(r, columnIndex), false, false, r, columnIndex);
      width = Math.max(width, comp.getPreferredSize().width);
    }


    if (width > MAX_WIDTH) { width = MAX_WIDTH; }
    if (width < minWidth) { width = minWidth; }

    int margin = 2;
    width += 2*margin;
    column.setPreferredWidth(width);
  }
/**
 * Sets the color for a particular column by creating an instance of  AlternateRowColorDefaultTableCellRenderer
 * @param column the column id which will have its cell color set.  
 */
  public static void setColumnCellColor(TableColumn column) {
//    DefaultTableCellRenderer hr = new DefaultTableCellRenderer();
//    hr.setBackground(Color.YELLOW);
    AlternateRowColorDefaultTableCellRenderer hr =
      new AlternateRowColorDefaultTableCellRenderer(SimpplleMain.RESULT_COL_COLOR);
    column.setCellRenderer(hr);
  }

}



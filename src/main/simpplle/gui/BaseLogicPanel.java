/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.AbstractBaseLogic;
import simpplle.comcode.BaseLogic;
import simpplle.comcode.SystemKnowledge;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

/** 
 * This class defines Base Logic Panel, a type of JPanel.   This is the base class for all of the logic panels.
 * All other logic panels extend from this class which in turn extends from
 * the JPanel.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see javax.swing.JPanel
 */

public class BaseLogicPanel extends JPanel {

  protected AbstractLogicDialog dialog;
  protected String kind;
  public LogicDataModel dataModel;
  protected int selectedRow = -1;
  protected boolean inColumnInit = false;
  protected AbstractBaseLogic logicInst;
  protected SystemKnowledge.Kinds sysKnowKind;

  protected JPanel northPanel = new JPanel();
  protected JPanel centerPanel = new JPanel(new BorderLayout());
  protected JScrollPane tableScrollPane = new JScrollPane();
  protected JTable logicTable = new JTable();

  /**
   * Constructor for Base Logic Panel.  Sets the Abstract Logic Dialog, system knowledge kind, logic data model, logic instance
   * @param dialog
   * @param kind
   * @param logicInst
   * @param sysKnowKind
   */
  public BaseLogicPanel(AbstractLogicDialog dialog,
                    String kind, AbstractBaseLogic logicInst,
                    SystemKnowledge.Kinds sysKnowKind) {
    try {
      this.dialog  = dialog;
      this.kind = kind;
      this.dataModel = new LogicDataModel(kind,logicInst,logicTable);
      this.logicInst = logicInst;
      this.sysKnowKind = sysKnowKind;
      jbInit();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  /**
   * sets the layout, center panel, table scroll pane and north panel
   * @throws Exception
   */
  protected void jbInit() throws Exception {
    setLayout(new BorderLayout());
    add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(tableScrollPane, BorderLayout.CENTER);
    tableScrollPane.getViewport().add(logicTable);
    add(northPanel, BorderLayout.NORTH);
  }
  /**
   *
   */
  public void updateColumns() {
    TableColumn column;
    Enumeration e = logicTable.getColumnModel().getColumns();

    while (e.hasMoreElements()) {
      column = (TableColumn)e.nextElement();
      String colName = (String)column.getHeaderValue();
      int col = dataModel.getLogicInst().getColumnNumFromName(colName);

      initColumns(column,col);
    }
  }
  /**
   * initializes the table columns by referring to javax.swing.table.TableColumn TableColumn .  returns nothing, sets nothing
   * @param column
   * @param col
   */
  protected void initColumns(TableColumn column, int col) {}
  /**
   * initialized the base columns by setting the identifier to baselogic row_col and setting the row color to alternate
   * @param column
   * @param col
   */
  protected void initBaseColumns(TableColumn column, int col) { 
    if (col == BaseLogic.ROW_COL) {
      column.setIdentifier(BaseLogic.ROW_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
  }
  /**
   * sets the information on the base panel for this class, sets the column selection to false, the row selection to true and initializes the column width and sends to GUI Utility
   * functions to size the column width based on the current JTable, which will then pass to another initColumnWidth and resize based on #of columns  
   */
  protected void initializeBase() {
    logicTable.setModel(dataModel);
    logicTable.setColumnSelectionAllowed(false);
    logicTable.setRowSelectionAllowed(true);

    updateColumns();

    logicTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    if (dataModel.isDataPresent()) {
      Utility.initColumnWidth(logicTable);
    }

    logicTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    ListSelectionModel rowSM = logicTable.getSelectionModel();
    rowSM.addListSelectionListener(this::valueChanged);

    selectedRow = -1;
    updateDialog();

  }
  // TODO: Look into removing this function...
  protected void rowSelected() {}

  protected void updateColumnWidth() {
    Utility.initColumnWidth(logicTable);
  }

  public SystemKnowledge.Kinds getSystemKnowledgeKind() { return sysKnowKind; }
  /**
   * Adds a visible column and sets the fire table structure changed
   * @param col Index of column to be modified
   */
  public void addVisibleColumn(int col) {
    dataModel.addVisibleColumn(col);
    dataModel.fireTableStructureChanged();
  }
  /**
   * removes a visible column and sets the fire table structure changed
   * @param col Index of column to be modified
   */
  public void removeVisibleColumn(int col) {
    dataModel.removeVisibleColumn(col);
    dataModel.fireTableStructureChanged();
  }

  public boolean isVisibleColumn(int col) {
    return dataModel.isVisibleColumn(col);
  }
//  protected void initColumnVisibility() {
//    RegionalZone zone = Simpplle.getCurrentZone();
//
//    inColumnInit = true;
//    for (int i=1; i<columns.size(); i++) {
//      hideColumn(i);
//    }
//    int[] cols = dataModel.getVisibleColumns();
//    if (cols != null && cols.length > 0){
//      for (int i=0; i<cols.length; i++) {
//        showColumn(cols[i]);
//      }
//    }
//    else {
//      for (int i=1; i<columns.size(); i++) {
//        showColumn(i);
//      }
//    }
//    inColumnInit = false;
//  }

//  public void showColumn(int col) {
//    if (inColumnInit) { dialog.setColumnMenuItemSelected(true,col); }
//    Utility.initColumnWidth(logicTable,columns.get(col),col);
//  }
//  public void hideColumn(int col) {
//    if (inColumnInit) { dialog.setColumnMenuItemSelected(false,col); }
//    columns.get(col).setPreferredWidth(0);
//  }
  /**
   * Updates the BaseLogic dialog, by calling refresh table and updating the graphics
   */
  public void updateDialog() {
    refreshTable();
    update(getGraphics());
  }
  /**
   * Refreshes the table by notifying all listeners that the table has changed and the JTable should redraw from scratch
   */
  public void refreshTable() {
    dataModel.fireTableDataChanged();
  }
  /**
   * moves a row up by sending to LogicDataModel GUI class
   */
  public void moveRowUp() {
    int newRow = dataModel.moveRowUp(selectedRow);
    logicTable.setRowSelectionInterval(newRow,newRow);
    selectedRow = newRow;
    // Fixes a problem if moving a row right after editing something.
    logicTable.removeEditor();
  }
  /**
   * moves a row down by sending to LogicDataModel GUI class
   */
  public void moveRowDown() {
    int newRow = dataModel.moveRowDown(selectedRow);
    logicTable.setRowSelectionInterval(newRow,newRow);
    selectedRow = newRow;
    // Fixes a problem if moving a row right after editing something.
    logicTable.removeEditor();
  }
  /**
   * Inserts a duplicated at to the position of the selected row. If there is no selection, then the row is
   * appended to the end.
   */
  public void duplicateSelectedRow() {
    int position = (selectedRow != -1) ? selectedRow : dataModel.getRowCount() + 1;
    dataModel.duplicateRow(selectedRow,position);
  }
  /**
   * first checks if user really wants to delete row with a JOption Pane.  If yes, deletes row by calling LogicDataModel class.  
   */
  public void deleteSelectedRow() {
    int row = logicTable.getSelectedRow();
    String msg =
      "Delete Currently Selected Row!\n\n" +
      "Are You Sure?";
    int choice = JOptionPane.showConfirmDialog(dialog,msg,"Delete Selected Row",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      dataModel.deleteRow(row);
      logicTable.clearSelection();
    }
  }
  /**
   * Inserts a row according to position. The position is determined by a user row selection to be inserted above
   * or below, if there is no row selected then the row is appended to the end.
   */
  public void insertRow() {
    int position = (selectedRow != -1) ? selectedRow : dataModel.getRowCount() + 1;
    dataModel.addRow(position);
  }
  /**
   * Calls to AbstractBaseLogic to get ArrayList of indices whose columns are empty
   *
   * @return Returns ArrayList of column indices that are empty
   */
  private ArrayList<Integer> emptyColumns() {return logicInst.checkEmpty(kind);}
  /**
   * Hides all of the empty columns
   */
  void hideEmpty(){

    ArrayList<Integer> emptyCols = emptyColumns();
    emptyCols.forEach(this::removeVisibleColumn);
  }
  /**
   * Reveals all of the empty columns*
   */
  void showEmpty(){
    ArrayList<Integer> emptyCols = emptyColumns();
    emptyCols.forEach(this::addVisibleColumn);
  }

  public void valueChanged(ListSelectionEvent e) {
    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
    if (lsm.isSelectionEmpty() == false) {
      selectedRow = lsm.getMinSelectionIndex();
      rowSelected();
    }
    else { selectedRow = -1; }
    dialog.menuActionDeleteSelectedRule.setEnabled(!lsm.isSelectionEmpty());
    dialog.menuActionMoveRuleUp.setEnabled(!lsm.isSelectionEmpty());
    dialog.menuActionMoveRuleDown.setEnabled(!lsm.isSelectionEmpty());
    dialog.menuActionDuplicateSelectedRule.setEnabled(!lsm.isSelectionEmpty());
  }
}

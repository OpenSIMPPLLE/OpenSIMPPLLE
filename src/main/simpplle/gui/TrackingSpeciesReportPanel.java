package simpplle.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import simpplle.comcode.*;
import java.util.ArrayList;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class TrackingSpeciesReportPanel extends JPanel {
  protected TrackingSpeciesReportDlg dialog;
  TrackingSpeciesReportDataModel dataModel = new TrackingSpeciesReportDataModel();
  protected int selectedRow = -1;

  protected BorderLayout borderLayout1 = new BorderLayout();
  protected JPanel centerPanel = new JPanel();
  protected BorderLayout borderLayout2 = new BorderLayout();
  protected JScrollPane tableScrollPane = new JScrollPane();
  protected JTable logicTable = new JTable();

  public TrackingSpeciesReportPanel() {
    super();
    try {
      jbInit();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  protected void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    centerPanel.setLayout(borderLayout2);
    logicTable.setModel(dataModel);
    this.add(centerPanel, java.awt.BorderLayout.CENTER);
    centerPanel.add(tableScrollPane, java.awt.BorderLayout.CENTER);
    tableScrollPane.setViewportView(logicTable);
  }

  public void initialize(TrackingSpeciesReportDlg dlg) {
    this.dialog = dlg;
    logicTable.setColumnSelectionAllowed(false);
    logicTable.setRowSelectionAllowed(true);

    logicTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    if (dataModel.isDataPresent()) {
      Utility.initColumnWidth(logicTable);
    }

    logicTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    ListSelectionModel rowSM = logicTable.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
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
    });

    selectedRow = -1;
    updateDialog();

  }

  protected void rowSelected() {}

  protected void updateColumnWidth() {
    Utility.initColumnWidth(logicTable);
  }

  public void updateDialog() {
    refreshTable();
    update(getGraphics());
  }

  public void refreshTable() {
    dataModel.fireTableDataChanged();
  }

  public void moveRowUp() {
    int newRow = dataModel.moveRowUp(selectedRow);
    logicTable.setRowSelectionInterval(newRow,newRow);
    selectedRow = newRow;
    // Fixes a problem if moving a row right after editing something.
    logicTable.removeEditor();
  }
  public void moveRowDown() {
    int newRow = dataModel.moveRowDown(selectedRow);
    logicTable.setRowSelectionInterval(newRow,newRow);
    selectedRow = newRow;
    // Fixes a problem if moving a row right after editing something.
    logicTable.removeEditor();
  }
  public void duplicateSelectedRow() {
    int position = dataModel.getRowCount() + 1;
    dataModel.duplicateRow(selectedRow,position);
  }
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

  public void insertRow() {
    ArrayList<Species> allSpeciesList = dataModel.getAllSpeciesList();

    if (allSpeciesList == null || allSpeciesList.size() == 0) {
      JOptionPane.showMessageDialog(this,
                                    "No Tracking Species",
                                    "No Tracking Species",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    Species[] possibleValues = allSpeciesList.toArray(new Species[allSpeciesList.size()]);

    Species selectedValue = (Species)JOptionPane.showInputDialog(null,
        "Choose Tracking Species", "Choose Tracking Species",
        JOptionPane.INFORMATION_MESSAGE, null,
        possibleValues, possibleValues[0]);

    if (selectedValue == null) { return; }

    int position = (selectedRow != -1) ? selectedRow : dataModel.getRowCount() + 1;
    dataModel.addRow(position, selectedValue);
    dataModel.fireTableDataChanged();
  }

}




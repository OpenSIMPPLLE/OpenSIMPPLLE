package simpplle.gui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import simpplle.comcode.logic.AbstractLogicData;
import javax.swing.JScrollPane;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 *   
 *     
 */
public class JTableListSelectionDialog extends JDialog {
  private JPanel mainPanel = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel centerPanel = new JPanel();
  private JScrollPane scrollPane = new JScrollPane();
  private JTable table = new JTable();

  private ListSelectionTableDataModel dataModel;

  public JTableListSelectionDialog(JDialog owner, String title, boolean modal,
                                   AbstractLogicData logicData, int dataColumn) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();

      dataModel = new ListSelectionTableDataModel(logicData,dataColumn);
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public JTableListSelectionDialog() {
    this(new JDialog(), "JTableListSelectionDialog", false,null,0);
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    getContentPane().add(mainPanel);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    centerPanel.add(scrollPane);
    scrollPane.getViewport().add(table);
  }

  private void initialize() {
    table.setModel(dataModel);
    table.setColumnSelectionAllowed(false);
    table.setRowSelectionAllowed(true);

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    if (dataModel.isDataPresent()) {
      Utility.initColumnWidth(table);
    }

    dataModel.fireTableDataChanged();
    update(getGraphics());
  }
}

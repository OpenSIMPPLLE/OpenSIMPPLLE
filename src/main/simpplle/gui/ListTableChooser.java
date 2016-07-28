/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import java.awt.SystemColor;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import simpplle.comcode.LogicData;
import java.util.ArrayList;

/**
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ListTableChooser extends JDialog {
  ListTableDataModel dataModel = new ListTableDataModel();
  LogicData logicData;
  ArrayList theList;
  int       column;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Table");
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JScrollPane spScroll = new JScrollPane();
  JTable table = new JTable();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel jPanel1 = new JPanel();
  JTextField descText = new JTextField();
  JLabel descLabel = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel descPanel = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JLabel currentDesc = new JLabel();

  public ListTableChooser(JDialog owner, String title, boolean modal,
                         LogicData logicData, ArrayList dataList,
                         int column)
  {
    super(owner, title, modal);
    this.logicData = logicData;
    this.theList   = dataList;
    this.column    = column;
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public ListTableChooser() {
    this(new JDialog(), "ListTableChooser", false,null,null,0);
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    spGroupPanel.setLayout(borderLayout3);
    gridLayout1.setRows(3);
    northPanel.setLayout(borderLayout2);
    spTablePanel.setLayout(borderLayout4);
    spTablePanel.setBorder(border6);
    centerPanel.setLayout(flowLayout3);
    jPanel1.setLayout(borderLayout5);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    descText.setCaretPosition(0);
    descText.setColumns(40);
    descLabel.setText("Description (Cell Value).  Leave blank for default");
    centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    descPanel.setLayout(borderLayout6);
    this.addWindowListener(new ListTableChooser_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    northPanel.add(spTablePanel, java.awt.BorderLayout.CENTER);
    spTablePanel.add(spScroll, java.awt.BorderLayout.NORTH);
    spScroll.getViewport().add(table);
    northPanel.add(spGroupPanel, java.awt.BorderLayout.NORTH);
    centerPanel.add(jPanel1);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(northPanel, java.awt.BorderLayout.NORTH);
    descPanel.add(descText, java.awt.BorderLayout.WEST);
    descPanel.add(descLabel, java.awt.BorderLayout.NORTH);
    jPanel1.add(currentDesc, java.awt.BorderLayout.CENTER);
    jPanel1.add(descPanel, java.awt.BorderLayout.NORTH);
  }

  private void initialize() {
    dataModel.initData(logicData,theList,column);
    table.setModel(dataModel);

    if (logicData != null) {
      descLabel.setVisible(true);
      descText.setVisible(true);
      currentDesc.setVisible(true);

      descText.setText("");
      if (logicData.isDefaultListDescription(column) == false) {
        descText.setText(logicData.getListDescription(column));
      }
      currentDesc.setText(logicData.getListDescription(column));
    }
    else {
      descLabel.setVisible(false);
      descText.setVisible(false);
      currentDesc.setVisible(false);
    }
  }

  public void this_windowClosing(WindowEvent e) {
    if (logicData != null) {
      logicData.setListDescription(column,descText.getText().trim());
    }
  }

}


class ListTableChooser_this_windowAdapter extends WindowAdapter {
  private ListTableChooser adaptee;
  ListTableChooser_this_windowAdapter(ListTableChooser adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import simpplle.comcode.VegetativeType;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessType;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import simpplle.comcode.InclusionRuleSpecies;

/** 
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class PathwaySpeciesChangeEditDialog extends JDialog {
  HabitatTypeGroup group;
  PathwaySpeciesChangeDataModel dataModel = new PathwaySpeciesChangeDataModel();

  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTable table = new JTable();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu menuAction = new JMenu();
  JMenuItem menuActionNewEntry = new JMenuItem();
  JMenuItem menuActionDeleteSelected = new JMenuItem();
  JMenuItem menuActionAddSpecies = new JMenuItem();

  public PathwaySpeciesChangeEditDialog(Frame owner, String title,
                                        boolean modal) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public void initialize(VegetativeType vt, HabitatTypeGroup group) {
    dataModel.setData(vt);
    this.group = group;

    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    ListSelectionModel rowSM = table.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        menuActionDeleteSelected.setEnabled(!lsm.isSelectionEmpty());
      }
    });
  }

  public PathwaySpeciesChangeEditDialog() {
    this(new Frame(), "PathwaySpeciesChangeEditDialog", false);
  }

  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout2);
    mainPanel.setLayout(borderLayout3);
    mainPanel.setPreferredSize(new Dimension(750, 400));
    table.setModel(dataModel);
    this.setJMenuBar(jMenuBar1);
    menuAction.setText("Action");
    menuActionNewEntry.setText("New Entry");
    menuActionNewEntry.addActionListener(new
        PathwaySpeciesChangeEditDialog_jMenuItem1_actionAdapter(this));
    menuActionAddSpecies.setText("Add Species");
    menuActionAddSpecies.addActionListener(new
        PathwaySpeciesChangeEditDialog_menuActionAddSpecies_actionAdapter(this));
    menuActionDeleteSelected.setEnabled(false);
    menuActionDeleteSelected.setText("Delete Selected Row(s)");
    menuActionDeleteSelected.addActionListener(new
        PathwaySpeciesChangeEditDialog_jMenuItem2_actionAdapter(this));
    jScrollPane1.getViewport().add(table);
    jMenuBar1.add(menuAction);
    menuAction.add(menuActionNewEntry);
    menuAction.add(menuActionAddSpecies);
    menuAction.add(menuActionDeleteSelected);
    mainPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
    this.getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

  }

  public void menuActionAddSpecies_actionPerformed(ActionEvent e) {
    String name = JOptionPane.showInputDialog("Type a New Species");
    if (name == null) { return; }
    InclusionRuleSpecies species = new InclusionRuleSpecies(name);
    JOptionPane.showMessageDialog(this,species.toString() + " Added to Valid List",
                                  "Species Added",JOptionPane.INFORMATION_MESSAGE);
  }

  public void menuActionNewEntry_actionPerformed(ActionEvent e) {

    ProcessType[] processList = Process.getSummaryProcesses();
    ProcessType[] processes = new ProcessType[processList.length+5];
    int i=0;
    for (Object o : processList) {
      ProcessType p = (ProcessType)o;
      if (p.equals(ProcessType.SRF)) {
        processes[i++] = ProcessType.SRF_SPRING;
        processes[i++] = ProcessType.SRF_SUMMER;
        processes[i++] = ProcessType.SRF_FALL;
        processes[i++] = ProcessType.SRF_WINTER;
      }
      else if (p.equals(ProcessType.SUCCESSION)) {
        processes[i++] = p;
        processes[i++] = ProcessType.WET_SUCCESSION;
        processes[i++] = ProcessType.DRY_SUCCESSION;
      }
      else {
        processes[i++] = p;
      }
    }

    ProcessType process = (ProcessType)JOptionPane.showInputDialog(null,
        "Choose a Process", "Choose a Process",
        JOptionPane.INFORMATION_MESSAGE, null,
        processes, processes[0]);
    if (process == null) { return; }

    InclusionRuleSpecies[] allSpecies = InclusionRuleSpecies.getAllValues();
    InclusionRuleSpecies species =
      (InclusionRuleSpecies)JOptionPane.showInputDialog(null,
        "Choose a Species", "Choose a Species",
        JOptionPane.INFORMATION_MESSAGE, null,
        allSpecies, allSpecies[0]);

    if (process != null && species != null) {
      dataModel.addRow(process,species);
    }

  }

  public void menuActionDeleteSelected_actionPerformed(ActionEvent e) {
    int[] rows = table.getSelectedRows();
    String msg =
      "Delete Currently Selected Row(s)!\n\n" +
      "Are You Sure?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Delete Selected Row",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      dataModel.deleteRows(rows);
      table.clearSelection();
    }
    update(getGraphics());
  }

}

class PathwaySpeciesChangeEditDialog_menuActionAddSpecies_actionAdapter implements
    ActionListener {
  private PathwaySpeciesChangeEditDialog adaptee;
  PathwaySpeciesChangeEditDialog_menuActionAddSpecies_actionAdapter(
      PathwaySpeciesChangeEditDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionAddSpecies_actionPerformed(e);
  }
}

class PathwaySpeciesChangeEditDialog_jMenuItem2_actionAdapter implements
    ActionListener {
  private PathwaySpeciesChangeEditDialog adaptee;
  PathwaySpeciesChangeEditDialog_jMenuItem2_actionAdapter(
      PathwaySpeciesChangeEditDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionDeleteSelected_actionPerformed(e);
  }
}

class PathwaySpeciesChangeEditDialog_jMenuItem1_actionAdapter implements
    ActionListener {
  private PathwaySpeciesChangeEditDialog adaptee;
  PathwaySpeciesChangeEditDialog_jMenuItem1_actionAdapter(
      PathwaySpeciesChangeEditDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionNewEntry_actionPerformed(e);
  }
}


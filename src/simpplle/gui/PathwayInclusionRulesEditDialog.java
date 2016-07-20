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
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import simpplle.comcode.VegetativeType;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import simpplle.comcode.HabitatTypeGroup;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import simpplle.comcode.InclusionRuleSpecies;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This class allows users to edit an inclusion rule.  
 * Inclusion rules consist of Inclusion Rule Species name, lower percent range, and upper percent range.  
 * This class allows users to create, add, or delete an inclusion rule by species name.  Much of this can be done using the Action JMenu.
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class PathwayInclusionRulesEditDialog extends JDialog {
  PathwayInclusionRulesDataModel dataModel = new PathwayInclusionRulesDataModel();
  HabitatTypeGroup group;

  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTable table = new JTable();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu actionMenu = new JMenu();
  JMenuItem menuActionNew = new JMenuItem();
  JMenuItem menuActionDeleteSelected = new JMenuItem();
  JMenuItem menuActionAdd = new JMenuItem();
/**
 * Constructor for inclusion rules Edit Dialog.  
 * @param owner frame that owns the inclusion rules editor
 * @param title
 * @param modal
 */
  public PathwayInclusionRulesEditDialog(Frame owner, String title,
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

  public PathwayInclusionRulesEditDialog() {
    this(new Frame(), "PathwayInclusionRulesEditDialog", false);
  }
  /**
   * Initialize the Pathway Inclusion Rule Editor 
   * @param vt
   * @param group
   */
  public void initialize(VegetativeType vt, HabitatTypeGroup group) {
    dataModel.setData(vt);
    this.group   = group;

    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    ListSelectionModel rowSM = table.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        menuActionDeleteSelected.setEnabled(!lsm.isSelectionEmpty());
      }
    });
  }
/**
 * Sets up the inclusion rules with an Action menu, and data model which consists of Inclusion Rule Species, Lower range and Upper Range.  
 * @throws Exception
 */
  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout2);
    mainPanel.setLayout(borderLayout3);
    mainPanel.setPreferredSize(new Dimension(750, 400));
    table.setModel(dataModel);
    actionMenu.setText("Action");
    menuActionNew.setText("New Entry");
    menuActionNew.addActionListener(new
        PathwayInclusionRulesEditDialog_menuActionNew_actionAdapter(this));
    menuActionDeleteSelected.setEnabled(false);
    menuActionDeleteSelected.setText("Delete Selected");
    menuActionDeleteSelected.addActionListener(new
        PathwayInclusionRulesEditDialog_menuActionDeleteSelected_actionAdapter(this));
    this.setJMenuBar(jMenuBar1);
    menuActionAdd.setText("Add Entry");
    menuActionAdd.addActionListener(new
        PathwayInclusionRulesEditDialog_menuActionAdd_actionAdapter(this));
    jScrollPane1.getViewport().add(table);
    jMenuBar1.add(actionMenu);
    actionMenu.add(menuActionNew);
    actionMenu.add(menuActionAdd);
    actionMenu.add(menuActionDeleteSelected);
    mainPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
    this.getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

  }
/**
 * Allows users to create a new inclusion rule species by name into a JOptionPane.  
 * @param e
 */
  public void menuActionNew_actionPerformed(ActionEvent e) {
    String name = JOptionPane.showInputDialog("Type a New Species");
    if (name != null) {
      dataModel.addRow(new InclusionRuleSpecies(name.toUpperCase()));
    }
  }
  /**
   * Adds an inclusion rule to the vegetative pathways.  Displays all the inclusion rule species and allows the user to choose one.  
   * @param e
   */
  public void menuActionAdd_actionPerformed(ActionEvent e) {
    InclusionRuleSpecies[] allSpecies = InclusionRuleSpecies.getAllValues();

    InclusionRuleSpecies species = (InclusionRuleSpecies)JOptionPane.showInputDialog(null,
        "Choose a Species", "Choose a Species",
        JOptionPane.INFORMATION_MESSAGE, null,
        allSpecies, allSpecies[0]);
    if (species != null) {
      dataModel.addRow(species);
    }
  }
/**
 * Deletes an inclusion rule by deleting a selected row.  
 * @param e
 */
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
/**
 * Adapter class that allows user to add an inclusion rule.  It will pop up a Joption Pane which allows the user to select from all the Inclusion Rule 
 * Species.  
 *
 */
class PathwayInclusionRulesEditDialog_menuActionAdd_actionAdapter implements
    ActionListener {
  private PathwayInclusionRulesEditDialog adaptee;
  PathwayInclusionRulesEditDialog_menuActionAdd_actionAdapter(
      PathwayInclusionRulesEditDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionAdd_actionPerformed(e);
  }
}
/**
 * An adapter class used to edit handle the delete selected row.  It is a convenience method designed to simplify Action Listeners.  
 * It will delete a row, and therefore an inclusion rule.    
 * 
 *
 */
class PathwayInclusionRulesEditDialog_menuActionDeleteSelected_actionAdapter implements
    ActionListener {
  private PathwayInclusionRulesEditDialog adaptee;
  PathwayInclusionRulesEditDialog_menuActionDeleteSelected_actionAdapter(
      PathwayInclusionRulesEditDialog adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Calls to menuActionDeleteSelected_actionPerformed(e) to delete row (inclusion rule)
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionDeleteSelected_actionPerformed(e);
  }
}

class PathwayInclusionRulesEditDialog_menuActionNew_actionAdapter implements
    ActionListener {
  private PathwayInclusionRulesEditDialog adaptee;
  PathwayInclusionRulesEditDialog_menuActionNew_actionAdapter(
      PathwayInclusionRulesEditDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionNew_actionPerformed(e);
  }
}

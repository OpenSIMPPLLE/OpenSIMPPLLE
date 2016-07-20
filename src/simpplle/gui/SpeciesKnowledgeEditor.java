/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.Species;
import simpplle.comcode.FireResistance;
import simpplle.comcode.Lifeform;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.Enumeration;
import java.awt.event.*;
import simpplle.comcode.SystemKnowledge;
import java.io.*;
import simpplle.comcode.Simpplle;
import java.util.Vector;
import simpplle.comcode.*;
import java.util.ArrayList;

/** 
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class SpeciesKnowledgeEditor extends JDialog {
  private SpeciesTableDataModel dataModel = new SpeciesTableDataModel();
  private static final int CODE_COL            = Species.CODE_COL;
  private static final int DESCRIPTION_COL     = Species.DESCRIPTION_COL;
  private static final int LIFEFORM_COL        = Species.LIFEFORM_COL;
  private static final int FIRE_RESISTANCE_COL = Species.FIRE_RESISTANCE_COL;

  private static boolean initialized = false;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel tablePanel = new JPanel();
  JScrollPane tableScrollPane = new JScrollPane();
  BorderLayout borderLayout2 = new BorderLayout();
  JTable table = new JTable();
  JMenuBar menuBar = new JMenuBar();
  JMenu jMenu1 = new JMenu();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileLoadDefaults = new JMenuItem();
  JMenuItem menuFileQuit = new JMenuItem();
  JMenuItem menuFileNew = new JMenuItem();
  JMenuItem menuFileImport = new JMenuItem();
  JMenu jMenu2 = new JMenu();
  JMenuItem menuActionDeleteSpecies = new JMenuItem();

  public SpeciesKnowledgeEditor(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public SpeciesKnowledgeEditor() {
    this(null, "", false);
  }
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    tablePanel.setLayout(borderLayout2);
    table.setModel(dataModel);
    this.setJMenuBar(menuBar);
    jMenu1.setText("File");
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new SpeciesKnowledgeEditor_menuFileOpen_actionAdapter(this));
    menuFileClose.setEnabled(false);
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new SpeciesKnowledgeEditor_menuFileClose_actionAdapter(this));
    menuFileSave.setEnabled(false);
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new SpeciesKnowledgeEditor_menuFileSave_actionAdapter(this));
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new SpeciesKnowledgeEditor_menuFileSaveAs_actionAdapter(this));
    menuFileLoadDefaults.setText("Load Defaults");
    menuFileLoadDefaults.addActionListener(new SpeciesKnowledgeEditor_menuFileLoadDefaults_actionAdapter(this));
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new SpeciesKnowledgeEditor_menuFileQuit_actionAdapter(this));
    menuFileNew.setText("New Species");
    menuFileNew.addActionListener(new SpeciesKnowledgeEditor_menuFileNew_actionAdapter(this));
    menuFileImport.setText("Import Text File");
    menuFileImport.addActionListener(new SpeciesKnowledgeEditor_menuFileImport_actionAdapter(this));
    jMenu2.setText("Action");
    menuActionDeleteSpecies.setText("Delete Selected Species");
    menuActionDeleteSpecies.addActionListener(new SpeciesKnowledgeEditor_menuActionDeleteSpecies_actionAdapter(this));
    getContentPane().add(mainPanel);
    mainPanel.add(tablePanel,  BorderLayout.CENTER);
    tablePanel.add(tableScrollPane, BorderLayout.CENTER);
    tableScrollPane.getViewport().add(table, null);
    menuBar.add(jMenu1);
    menuBar.add(jMenu2);
    jMenu1.add(menuFileNew);
    jMenu1.add(menuFileImport);
    jMenu1.addSeparator();
    jMenu1.add(menuFileOpen);
    jMenu1.add(menuFileClose);
    jMenu1.addSeparator();
    jMenu1.add(menuFileSave);
    jMenu1.add(menuFileSaveAs);
    jMenu1.addSeparator();
    jMenu1.add(menuFileLoadDefaults);
    jMenu1.addSeparator();
    jMenu1.add(menuFileQuit);
    jMenu2.add(menuActionDeleteSpecies);
  }

  private void initialize() {
    initialized = false;

    ArrayList<SimpplleType> list = Species.getList(SimpplleType.SPECIES);
    if (list == null || list.size() == 0) { return; }

    initialized = true;
    TableColumn column;

    // ** Species Code Column **
    // not editable, use defaults.
    column = table.getColumnModel().getColumn(Species.CODE_COL);
    column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());

    // ** Species Description Column **
    // not editable, use defaults.
    column = table.getColumnModel().getColumn(Species.DESCRIPTION_COL);
    column.setCellRenderer(new MyJTextAreaRenderer());
//    column.setCellRenderer(table.getDefaultRenderer(String.class));
//    column.setCellEditor(new MyJTextFieldEditor(""));
    column.setCellEditor(table.getDefaultEditor(String.class));

    // ** Lifeform Column **
    column = table.getColumnModel().getColumn(Species.LIFEFORM_COL);
    column.setCellRenderer(new MyJComboBoxRenderer(Lifeform.getAllValues()));
    column.setCellEditor(new MyJComboBoxEditor(Lifeform.getAllValues()));

    // ** Fire Resistance Column **
    column = table.getColumnModel().getColumn(Species.FIRE_RESISTANCE_COL);
    column.setCellRenderer(new MyJComboBoxRenderer(FireResistance.getAllValues()));
    column.setCellEditor(new MyJComboBoxEditor(FireResistance.getAllValues()));

    // ** Resistance Conditional Column **
    column = table.getColumnModel().getColumn(Species.RESISTANCE_COND_COL);
    column.setCellRenderer(new MyJTextFieldRenderer());
    column.setCellEditor(new FireResistanceJButtonEditor(this,table,""));

    column = table.getColumnModel().getColumn(Species.INVASIVE_COL);
    column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());

    column = table.getColumnModel().getColumn(Species.PATHWAY_PRESENT_COL);
    column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());


    table.setColumnSelectionAllowed(false);
    table.setRowSelectionAllowed(true);
    initColumnWidth();

    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    ListSelectionModel rowSM = table.getSelectionModel();
//    rowSM.addListSelectionListener(new ListSelectionListener() {
//      public void valueChanged(ListSelectionEvent e) {
//        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
//        menuTableOptionsDelete.setEnabled(!lsm.isSelectionEmpty());
//        menuTableOptionsInsert.setEnabled(!lsm.isSelectionEmpty());
//      }
//    });

    rowSM.setSelectionInterval(5,5);
    tableScrollPane.setPreferredSize(new Dimension(table.getPreferredSize().width+25,600));

    table.clearSelection();
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    update(getGraphics());
  }

  private void initColumnWidth() {
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    TableModel  model = table.getModel();
    Enumeration e = table.getColumnModel().getColumns();
    TableColumn col;
    Component   comp;
    int         headerWidth, cellWidth, i=0;
    final int   MAX_WIDTH = 150;
    final int   MIN_WIDTH = 100;

    TableCellRenderer headerRenderer =
      table.getTableHeader().getDefaultRenderer();

    while (e.hasMoreElements()) {
      col = (TableColumn)e.nextElement();


      comp = headerRenderer.getTableCellRendererComponent(
                                 null, col.getHeaderValue(),
                                 false, false, 0, 0);
      headerWidth = comp.getPreferredSize().width;

      comp = table.getDefaultRenderer(model.getColumnClass(i)).
                          getTableCellRendererComponent(
                               table, table.getValueAt(0,i),false, false, 0, i);
      cellWidth  = comp.getPreferredSize().width;
      if (cellWidth > MAX_WIDTH) { cellWidth = MAX_WIDTH; }
      if (cellWidth < MIN_WIDTH) { cellWidth = MIN_WIDTH; }
      col.setPreferredWidth(Math.max(headerWidth, cellWidth));
      i++;
    }
  }

  void menuFileOpen_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this, SystemKnowledge.SPECIES, menuFileSave,
                                  menuFileClose);
    if (!initialized) {
      initialize();
    }
    update(getGraphics());
  }


  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.SPECIES);
    SystemKnowledgeFiler.saveFile(this, outfile, SystemKnowledge.SPECIES,
                                  menuFileSave, menuFileClose);
    update(getGraphics());
  }
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.SPECIES, menuFileSave,
                                  menuFileClose);
    update(getGraphics());
  }

  void menuFileClose_actionPerformed(ActionEvent e) {
    loadDefaults();
    update(getGraphics());
  }
  void menuFileLoadDefaults_actionPerformed(ActionEvent e) {
    loadDefaults();
    update(getGraphics());
  }
  private void loadDefaults() {
    int choice;
    try {
      String msg = "This will load the default Species Data.\n\n" +
                   "Do you wish to continue?";
      String title = "Load Default Species Data";

      if (Utility.askYesNoQuestion(this,msg,title)) {
        SystemKnowledge.readZoneDefault(SystemKnowledge.SPECIES);
      }
    }
    catch (simpplle.comcode.SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void menuFileQuit_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  void menuFileNew_actionPerformed(ActionEvent e) {
    String name = JOptionPane.showInputDialog(this, "Species", "",
                                              JOptionPane.PLAIN_MESSAGE);

    if (name == null) { return; }

    Species species = new Species(name,true);
    if (!initialized) {
      initialize();
    }
    update(getGraphics());
  }

  void menuFileImport_actionPerformed(ActionEvent e) {
    try {
      File file = Utility.getOpenFile(this, "Import Species Text File");
      if (file == null) { return; }
      Species.Import(file);

      if (!initialized) {
        initialize();
      }
      update(getGraphics());
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Import problems",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void menuActionDeleteSpecies_actionPerformed(ActionEvent e) {
    int[] rows = table.getSelectedRows();
    if (rows == null || rows.length == 0) { return; }

    Vector  allSpecies = HabitatTypeGroup.getValidSpecies();
    Species species;
    int     deleteCount=0;

    for (int i=0; i<rows.length; i++) {
      species = (Species) dataModel.getValueAt(rows[i], CODE_COL);

      if (allSpecies.contains(species)) {
        String msg = "Cannot Delete: " + species + "! It is part of existing Pathways.";
        JOptionPane.showMessageDialog(this, msg, "Unable to Delete",
                                      JOptionPane.WARNING_MESSAGE);
      }
      else {
        dataModel.deleteRow(rows[i]);
        deleteCount++;
      }
    }
    JOptionPane.showMessageDialog(this, (deleteCount + " Species deleted."), "",
                                  JOptionPane.INFORMATION_MESSAGE);
    table.clearSelection();
  }





}

class SpeciesKnowledgeEditor_menuFileOpen_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileOpen_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileOpen_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuFileClose_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileClose_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileClose_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuFileSave_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileSave_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSave_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuFileSaveAs_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileSaveAs_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSaveAs_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuFileLoadDefaults_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileLoadDefaults_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileLoadDefaults_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuFileQuit_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileQuit_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileQuit_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuFileNew_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileNew_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileNew_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuFileImport_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuFileImport_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileImport_actionPerformed(e);
  }
}

class SpeciesKnowledgeEditor_menuActionDeleteSpecies_actionAdapter implements java.awt.event.ActionListener {
  SpeciesKnowledgeEditor adaptee;

  SpeciesKnowledgeEditor_menuActionDeleteSpecies_actionAdapter(SpeciesKnowledgeEditor adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionDeleteSpecies_actionPerformed(e);
  }
}





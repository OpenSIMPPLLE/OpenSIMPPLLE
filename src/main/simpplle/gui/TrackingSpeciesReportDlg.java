/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import simpplle.comcode.SystemKnowledge;
import javax.swing.JOptionPane;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import simpplle.JSimpplle;
import java.awt.event.ComponentEvent;
import simpplle.comcode.*;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class TrackingSpeciesReportDlg extends JDialog {
  protected SystemKnowledge.Kinds sysKnowKind;

  protected JPanel mainPanel = new JPanel();
  protected BorderLayout borderLayout1 = new BorderLayout();
  protected BorderLayout borderLayout2 = new BorderLayout();
  protected JMenuBar menuBar = new JMenuBar();
  protected JMenu menuFile = new JMenu();
  protected JMenuItem menuFileOpen = new JMenuItem();
  protected JMenuItem menuFileClose = new JMenuItem();
  protected JMenuItem menuFileSave = new JMenuItem();
  protected JMenuItem menuFileSaveAs = new JMenuItem();
  protected JMenuItem menuFileLoadDefault = new JMenuItem();
  protected JMenuItem menuFileQuit = new JMenuItem();
  protected JMenu menuAction = new JMenu();
  protected JMenuItem menuActionMoveRuleUp = new JMenuItem();
  protected JMenuItem menuActionMoveRuleDown = new JMenuItem();
  protected JMenuItem menuActionInsertNewRule = new JMenuItem();
  protected JMenuItem menuActionDeleteSelectedRule = new JMenuItem();
  protected JMenuItem menuActionDuplicateSelectedRule = new JMenuItem();
  protected JMenu menuKnowledgeSource = new JMenu();
  protected JMenuItem menuKnowledgeSourceEdit = new JMenuItem();
  protected TrackingSpeciesReportPanel tablePanel;

  public TrackingSpeciesReportDlg(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      sysKnowKind = SystemKnowledge.TRACKING_SPECIES_REPORT;
      jbInit();

      tablePanel = new TrackingSpeciesReportPanel();
      tablePanel.initialize(this);
      mainPanel.add(tablePanel, java.awt.BorderLayout.CENTER);

      updateMenuItems();
      updateDialog();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public TrackingSpeciesReportDlg() {
    this(new Frame(), "TrackingSpeciesReportDlg", false);
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    menuFile.setText("File");
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new
        TrackingSpeciesReportDlg_menuFileOpen_actionAdapter(this));
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new
        TrackingSpeciesReportDlg_menuFileClose_actionAdapter(this));
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new
        TrackingSpeciesReportDlg_menuFileSave_actionAdapter(this));
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new
        TrackingSpeciesReportDlg_menuFileSaveAs_actionAdapter(this));
    menuFileLoadDefault.setText("Load Default");
    menuFileLoadDefault.addActionListener(new
        TrackingSpeciesReportDlg_menuFileLoadDefault_actionAdapter(this));
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new
        TrackingSpeciesReportDlg_menuFileQuit_actionAdapter(this));
    menuAction.setText("Action");
    menuActionMoveRuleUp.setText("Move Rule Up");
    menuActionMoveRuleUp.addActionListener(new
        TrackingSpeciesReportDlg_menuActionMoveRuleUp_actionAdapter(this));
    menuActionMoveRuleDown.setText("Move Rule Down");
    menuActionMoveRuleDown.addActionListener(new
        TrackingSpeciesReportDlg_menuActionMoveRuleDown_actionAdapter(this));
    menuActionInsertNewRule.setText("Insert New Rule");
    menuActionInsertNewRule.addActionListener(new
        TrackingSpeciesReportDlg_menuActionInsertNewRule_actionAdapter(this));
    menuActionDeleteSelectedRule.setText("Delete Selected Rule");
    menuActionDeleteSelectedRule.addActionListener(new
        TrackingSpeciesReportDlg_menuActionDeleteSelectedRule_actionAdapter(this));
    menuActionDuplicateSelectedRule.setText("Duplicate Selected Rule");
    menuActionDuplicateSelectedRule.addActionListener(new
        TrackingSpeciesReportDlg_menuActionDuplicateSelectedRule_actionAdapter(this));
    menuKnowledgeSource.setText("Knowledge Source");
    menuKnowledgeSourceEdit.setText("Display/Edit");
    menuKnowledgeSourceEdit.addActionListener(new
      TrackingSpeciesReportDlg_menuKnowledgeSourceEdit_actionAdapter(this));

    getContentPane().add(mainPanel);
    this.setJMenuBar(menuBar);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      public void componentResized(ComponentEvent e) {
        this_componentResized(e);
      }
    });
    menuBar.add(menuFile);
    menuBar.add(menuAction);
    menuBar.add(menuKnowledgeSource);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileLoadDefault);
    menuAction.add(menuActionMoveRuleUp);
    menuAction.add(menuActionMoveRuleDown);
    menuAction.addSeparator();
    menuAction.add(menuActionInsertNewRule);
    menuAction.add(menuActionDeleteSelectedRule);
    menuAction.add(menuActionDuplicateSelectedRule);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    menuKnowledgeSource.add(menuKnowledgeSourceEdit);
    mainPanel.setPreferredSize(new Dimension(825, 400));
  }

  protected void this_componentResized(ComponentEvent e) {
    tablePanel.updateColumnWidth();
    updateDialog();
  }
  protected void updateDialog() {
    update(getGraphics());
  }
  protected void updateMenuItems() {
    boolean hasFile =  (SystemKnowledge.getFile(sysKnowKind) != null);
    menuFileSave.setEnabled(
      SystemKnowledge.hasChangedOrUserData(sysKnowKind) && hasFile);
    menuFileClose.setEnabled(hasFile);
  }
  public void menuFileOpen_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this,sysKnowKind,menuFileSave,menuFileClose);
    //  This line must be done first!
    tablePanel.dataModel.fireTableStructureChanged();

    updateMenuItems();
    tablePanel.updateDialog();
    updateDialog();
  }
  protected void loadDefaults() {
    String msg = "This will load the default Logic.\n\n" +
                 "Do you wish to continue?";
    String title = "Load Default Logic";

    if (Utility.askYesNoQuestion(this, msg, title)) {
      TrackingSpeciesReportData.getInstance().initialize();
      //  This line must be done first!
      tablePanel.dataModel.fireTableStructureChanged();
      updateMenuItems();
      tablePanel.updateDialog();
      updateDialog();
      menuFileSave.setEnabled(isSaveNeeded());
      menuFileClose.setEnabled(false);
    }
  }

  public void menuFileClose_actionPerformed(ActionEvent e) {
    loadDefaults();
  }

  protected boolean isSaveNeeded() {
    return ( (SystemKnowledge.getFile(sysKnowKind) != null) &&
             (SystemKnowledge.hasChangedOrUserData(sysKnowKind)) );
  }
  public void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(sysKnowKind);
    SystemKnowledgeFiler.saveFile(this,outfile,sysKnowKind,menuFileSave,menuFileClose);
  }

  public void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this,sysKnowKind,menuFileSave,menuFileClose);
  }

  public void menuFileLoadDefault_actionPerformed(ActionEvent e) {
    loadDefaults();
  }

  public void menuFileQuit_actionPerformed(ActionEvent e) {
    setVisible(false);
  }

  public void menuActionMoveRuleUp_actionPerformed(ActionEvent e) {
    tablePanel.moveRowUp();
    updateDialog();
  }

  public void menuActionMoveRuleDown_actionPerformed(ActionEvent e) {
    tablePanel.moveRowDown();
    updateDialog();
  }

  public void menuActionInsertNewRule_actionPerformed(ActionEvent e) {
    tablePanel.insertRow();
    updateDialog();
  }

  public void menuActionDeleteSelectedRule_actionPerformed(ActionEvent e) {
    tablePanel.deleteSelectedRow();
    updateDialog();
  }
  public void menuActionDuplicateSelectedRule_actionPerformed(ActionEvent e) {
    tablePanel.duplicateSelectedRow();
    updateDialog();
  }

  public void menuKnowledgeSourceEdit_actionPerformed(ActionEvent e) {
    String str = SystemKnowledge.getKnowledgeSource(sysKnowKind);
    String title = "Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);

    String newKnowledge = dlg.getText();
    if (newKnowledge != null) {
      SystemKnowledge.setKnowledgeSource(sysKnowKind,newKnowledge);
    }
  }

}

class TrackingSpeciesReportDlg_menuActionDeleteSelectedRule_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuActionDeleteSelectedRule_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionDeleteSelectedRule_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuActionDuplicateSelectedRule_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuActionDuplicateSelectedRule_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionDuplicateSelectedRule_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuActionInsertNewRule_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuActionInsertNewRule_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionInsertNewRule_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuActionMoveRuleDown_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuActionMoveRuleDown_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionMoveRuleDown_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuActionMoveRuleUp_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuActionMoveRuleUp_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionMoveRuleUp_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuFileQuit_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuFileQuit_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileQuit_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuFileLoadDefault_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuFileLoadDefault_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileLoadDefault_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuFileSaveAs_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuFileSaveAs_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSaveAs_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuFileSave_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuFileSave_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSave_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuFileClose_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuFileClose_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileClose_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuFileOpen_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuFileOpen_actionAdapter(
      TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileOpen_actionPerformed(e);
  }
}

class TrackingSpeciesReportDlg_menuKnowledgeSourceEdit_actionAdapter implements
    ActionListener {
  private TrackingSpeciesReportDlg adaptee;
  TrackingSpeciesReportDlg_menuKnowledgeSourceEdit_actionAdapter(TrackingSpeciesReportDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuKnowledgeSourceEdit_actionPerformed(e);
  }
}


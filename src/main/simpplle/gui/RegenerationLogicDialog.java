/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.*;
import simpplle.comcode.RegenerationLogic.DataKinds;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

/**
 * This class creates the Regeneration Delay Logic Dialog, a type of VegLogicDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class RegenerationLogicDialog extends VegLogicDialog {
  JMenuItem menuOptionsAddAllSpecies = new JMenuItem();
  JMenuItem menuFileOldFormat = new JMenuItem();

  JMenu     menuOptions = new JMenu("Options");
  JMenuItem menuOptionsDelayLogic = new JMenuItem();
  /**
   * Constructor for Regeneration Logic Dialog.  This sets the frame owner, string title and modality.  
   * @param frame that owns the dialog
   * @param title name of dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   */
  public RegenerationLogicDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
	/**
	 * Overloaded Regen Logic Dialog constructor. 
	 * Creates a new frame as owner, sets the title to empty string and sets modality to modeless. 
	 */
  public RegenerationLogicDialog() {
    this(null, "", false);
  }
  /**
   * Initializes the dialog with menuitems, componenets, text, menu bar, and listeners for Regeneration Logic Dialog.
   * @throws Exception
   */
  void jbInit() throws Exception {
    menuOptionsAddAllSpecies.setActionCommand("Add All Species");
    menuOptionsAddAllSpecies.setText("Add All Species");
    menuOptionsAddAllSpecies.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuTableOptionsAddAllSpecies_actionPerformed(e);
      }
    });
    menuOptionsDelayLogic.setActionCommand("Delay Logic");
    menuOptionsDelayLogic.setText("Delay Logic");
    menuOptionsDelayLogic.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuOptionsDelayLogic_actionPerformed(e);
      }
    });
    menuFileOldFormat.setText("Import Old Format File");
    menuFileOldFormat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOldFormat_actionPerformed(e);
      }
    });

    menuBar.add(menuOptions);
    menuFile.add(menuFileOldFormat);
    menuOptions.add(menuOptionsAddAllSpecies);
    menuOptions.add(menuOptionsDelayLogic);
  }
  private void initialize() {
    sysKnowKind = SystemKnowledge.REGEN_LOGIC_FIRE;

    String[] kinds = new String[] { RegenerationLogic.FIRE_STR, RegenerationLogic.SUCCESSION_STR };
    super.initialize(kinds);

    tabPanels = new VegLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      if (kind.equals(RegenerationLogic.FIRE_STR)) {
        tabPanels[i] = new RegenerationLogicFireTable(this, SystemKnowledge.REGEN_LOGIC_FIRE);
      }
      else {
        tabPanels[i] = new RegenerationLogicSuccTable(this, SystemKnowledge.REGEN_LOGIC_SUCC);
      }
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }

  protected void loadDefaults() {
    if (currentPanelKind.equals(RegenerationLogic.FIRE_STR)) {
      ((RegenerationLogicFireTable)currentPanel).setDefaultEcoGroup();
    }
    else if (currentPanelKind.equals(RegenerationLogic.SUCCESSION_STR)){
      ((RegenerationLogicSuccTable)currentPanel).setDefaultEcoGroup();
    }
    super.loadDefaults();
  }

  public void tabbedPane_stateChanged(ChangeEvent e) {
    super.tabbedPane_stateChanged(e);
    if (currentPanel != null) {
      sysKnowKind = ((VegLogicPanel) currentPanel).getSystemKnowledgeKind();
    }
  }

  protected void updateMenuItems() {
    super.updateMenuItems();
    if (currentPanelKind.equals(RegenerationLogic.FIRE_STR)) {
      menuFileOpen.setText("Open Fire");
      menuFileSave.setText("Save Fire");
      menuFileSaveAs.setText("Save Fire As");
      menuFileClose.setText("Close Fire");
      menuFileLoadDefault.setText("Load Default Fire");

    }
    else if (currentPanelKind.equals(RegenerationLogic.SUCCESSION_STR)){
      menuFileOpen.setText("Open Type of Succession");
      menuFileSave.setText("Save Type of Succession");
      menuFileSaveAs.setText("Save Type of Succession As");
      menuFileClose.setText("Close Type of Succession");
      menuFileLoadDefault.setText("Load Default Type of Succession");
    }
    else {
      menuFileOpen.setText("Open");
      menuFileSave.setText("Save");
      menuFileSaveAs.setText("Save As");
      menuFileClose.setText("Close");
      menuFileLoadDefault.setText("Load Default");
    }
  }

  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    File         infile;
    MyFileFilter extFilter;
    String       title = "Select a Regenerationo Logic file.";

    extFilter = new MyFileFilter("regenlogic",
                                 "Regeneration Logic Files (*.regenlogic)");

    setCursor(Utility.getWaitCursor());

    infile = Utility.getOpenFile(this,title,extFilter);
    if (infile != null) {
      try {
        RegenerationLogic.readDataLegacy(infile);
        updateDialog();
        currentPanel.updateDialog();
        updateMenuItems();
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),
                                      "Error Loading Regeneration Logic File",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
    setCursor(Utility.getNormalCursor());
    update(getGraphics());
  }

/**
 * Quits if window closing event occurs
 * @param e
 */
  void this_windowClosing(WindowEvent e) {
    quit();
  }

  private void quit() {
    RegenerationLogic.setCurrentEcoGroup(null,null);
    setVisible(false);
    dispose();
  }

  void menuTableOptionsAddAllSpecies_actionPerformed(ActionEvent e) {
    Vector  v          = HabitatTypeGroup.getValidSpecies();
    Vector  newSpecies = new Vector();
    Species species;
    for (int i=0; i<v.size(); i++) {
      species = (Species)v.elementAt(i);
      if (RegenerationLogic.isSpeciesPresent(species,DataKinds.valueOf(currentPanelKind)) == false) {
        newSpecies.addElement(species);
      }
    }
    if (currentPanelKind.equals(RegenerationLogic.FIRE_STR)) {
      ((RegenerationLogicFireTable)currentPanel).addRows(newSpecies);
    }
    else if (currentPanelKind.equals(RegenerationLogic.SUCCESSION_STR)) {
      ((RegenerationLogicSuccTable)currentPanel).addRows(newSpecies);
    }

    update(getGraphics());
  }

  public void menuOptionsDelayLogic_actionPerformed(ActionEvent e) {
    RegenDelayLogicDlg dlg =
      new RegenDelayLogicDlg(JSimpplle.getSimpplleMain(),"Regeneration Delay Logic",true);
    dlg.setVisible(true);

  }

  public RegenerationLogic.DataKinds getLogicKind() {
    return RegenerationLogic.DataKinds.valueOf(currentPanelKind);
  }
}




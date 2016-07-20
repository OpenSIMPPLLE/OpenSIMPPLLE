/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import simpplle.comcode.SystemKnowledge;
import simpplle.comcode.FireSpreadDataLegacy;

import simpplle.comcode.SimpplleError;
import javax.swing.JOptionPane;
import java.io.File;
import simpplle.comcode.FireSpreadDataNewerLegacy;
import simpplle.comcode.FireEventLogic;
import javax.swing.event.ChangeEvent;
import simpplle.comcode.FireTypeDataLegacy;
import simpplle.comcode.FireTypeDataNewerLegacy;

/** 
 * This class defines the FireEventLogicDialog a type of vegetative logic dialog.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class FireEventLogicDialog extends VegLogicDialog {
  JMenuItem menuImportOldFile = new JMenuItem();
/**
 * Constructor for Fire Event Logic Dialog.  Inherits from Vegetative Logic Dialog superclass and sets the frame owner, dialog title and modality.
 * @param owner frame owner
 * @param title	Jdialog title
 * @param modal
 */
  public FireEventLogicDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
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
/**
 * Overloaded FireEvntLogicDialog
 */
  public FireEventLogicDialog() {
    super();
  }
/**
 * sets the text of import old file, and imports it into the base vegetative Logic dialog.
 * @throws Exception
 */
  private void jbInit() throws Exception {
    menuImportOldFile.setText("Import old format File");
    menuImportOldFile.addActionListener(new
      FireSpreadLogic_menuImportOldFile_actionAdapter(this));
    menuFile.add(menuImportOldFile,3);
  }
/**
 * Initializes the system knowledge to fire spread logic.  Creates tab panels to fire spread, type, and fire spotting, then adds to a tabbed pane.  
 */
  private void initialize() {
    sysKnowKind = SystemKnowledge.FIRE_SPREAD_LOGIC;

    String[] kinds = new String[] { FireEventLogic.SPREAD_STR, FireEventLogic.TYPE_STR, FireEventLogic.FIRE_SPOTTING_STR };
    super.initialize(kinds);

    tabPanels = new FireEventLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      if (kind.equals(FireEventLogic.SPREAD_STR)) {
        tabPanels[i] = new FireEventLogicPanel(this, kind,
                                               FireEventLogic.getInstance(),
                                               SystemKnowledge.FIRE_SPREAD_LOGIC);
      }
      else if (kind.equals(FireEventLogic.FIRE_SPOTTING_STR)) {
        tabPanels[i] = new FireEventLogicPanel(this, kind,
                                               FireEventLogic.getInstance(),
                                               SystemKnowledge.FIRE_SPOTTING_LOGIC);
      }
      else {
        tabPanels[i] = new FireEventLogicPanel(this, kind,
                                               FireEventLogic.getInstance(),
                                               SystemKnowledge.FIRE_TYPE_LOGIC);
      }
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }
/**
 * if tabbed pane state changes ges the current system knowledge. 
 */
  public void tabbedPane_stateChanged(ChangeEvent e) {
    super.tabbedPane_stateChanged(e);
    if (currentPanel != null) {
      sysKnowKind = ((FireEventLogicPanel) currentPanel).getSystemKnowledgeKind();
    }
  }
/**
 * Update the menu items depending on whether it is spread (Open spread, save spread, save spread as, close spread, load default spread
 * fire type (open type, save type, save type as, close type, or load default type.  or fire spotting (open fire spotting, save fire spotting, save fires spotting as, close fire spotting or default fire spotting.)
 * If none of these puts up the standard menu items.
 */
  protected void updateMenuItems() {
    super.updateMenuItems();
    if (currentPanelKind.equals(FireEventLogic.SPREAD_STR)) {
      menuFileOpen.setText("Open Spread");
      menuFileSave.setText("Save Spread");
      menuFileSaveAs.setText("Save Spread As");
      menuFileClose.setText("Close Spread");
      menuFileLoadDefault.setText("Load Default Spread");
      menuImportOldFile.setEnabled(true);
    }
    else if (currentPanelKind.equals(FireEventLogic.TYPE_STR)){
      menuFileOpen.setText("Open Type of Fire");
      menuFileSave.setText("Save Type of Fire");
      menuFileSaveAs.setText("Save Type of Fire As");
      menuFileClose.setText("Close Type of Fire");
      menuFileLoadDefault.setText("Load Default Type of Fire");
      menuImportOldFile.setEnabled(true);
    }
    else if (currentPanelKind.equals(FireEventLogic.FIRE_SPOTTING_STR)){
      menuFileOpen.setText("Open Fire Spotting");
      menuFileSave.setText("Save Fire Spotting");
      menuFileSaveAs.setText("Save Fire Spotting As");
      menuFileClose.setText("Close Fire Spotting");
      menuFileLoadDefault.setText("Load Default Fire Spotting");
      menuImportOldFile.setEnabled(false);
    }
    else {
      menuFileOpen.setText("Open");
      menuFileSave.setText("Save");
      menuFileSaveAs.setText("Save As");
      menuFileClose.setText("Close");
      menuFileLoadDefault.setText("Load Default");
      menuImportOldFile.setEnabled(true);
    }
  }
/**
 * If import old fire file type menu item selected will return either fire type or fire spread logic, used to get the file.  
 * @param e
 */
  public void menuImportOldFile_actionPerformed(ActionEvent e) {
    switch (sysKnowKind) {
      case FIRE_TYPE_LOGIC:
        menuImportOldFileTypeOfFire();
        break;
      case FIRE_SPREAD_LOGIC:
        menuImportOldFileSpread();
        break;
      default: return;
    }

  }
  /**
   * Used if old file type is selected.  Will get the old files with .firetype and .firetypedata extensions.
   */
  public void menuImportOldFileTypeOfFire() {
    File         inputFile;
    MyFileFilter extFilter;
    String       title = "Select a Fire Type Data File";
    String       ext;

    extFilter = new MyFileFilter(new String[] {"firetype", "firetypedata"},
                                 "Fire Type Data Files (.firetype, .firetypedata)");

    inputFile = Utility.getOpenFile(this,title,extFilter);

    if (inputFile != null) {
      try {
        ext = Utility.getFileExt(inputFile);
        if (ext.equalsIgnoreCase("firetype")) {
          FireTypeDataLegacy.loadData(inputFile);
          FireTypeDataNewerLegacy.setFromLegacyData();
          FireTypeDataNewerLegacy.convertToFireTypeLogic();
        }
        else if (ext.equalsIgnoreCase("firetypedata")) {
          FireTypeDataNewerLegacy.readData(inputFile);
          FireTypeDataNewerLegacy.convertToFireTypeLogic();
        }
        else { return; }

        updateDialog();
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  /**
   * Method to import old type of fire spread file.  Will have extension of .firespread.  
   */
  public void menuImportOldFileSpread() {
    File         inputFile;
    MyFileFilter extFilter;
    String       ext;
    String       title = "Select a Fire Spread Data File";

    extFilter = new MyFileFilter(new String[] {"firespread","firespreaddata"},
                                 "Fire Spread Data Files (*.firespread)");

    inputFile = Utility.getOpenFile(this,title,extFilter);

    if (inputFile != null) {
      try {
        ext = Utility.getFileExt(inputFile);
        if (ext.equalsIgnoreCase("firespread")) {
          FireSpreadDataLegacy.loadData(inputFile);
          FireSpreadDataNewerLegacy.setFromLegacyData();
          FireSpreadDataNewerLegacy.convertToFireSpreadLogic();
        }
        else if (ext.equalsIgnoreCase("firespreaddata")) {
          FireSpreadDataNewerLegacy.readData(inputFile);
          FireSpreadDataNewerLegacy.convertToFireSpreadLogic();
        }
        else { return; }

        updateDialog();
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }

}
/**
 * Menu adaptor used to get fire spread logic import old file.  
 * 
 *
 */

class FireSpreadLogic_menuImportOldFile_actionAdapter implements ActionListener {
  private FireEventLogicDialog adaptee;
  FireSpreadLogic_menuImportOldFile_actionAdapter(FireEventLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuImportOldFile_actionPerformed(e);
  }
}


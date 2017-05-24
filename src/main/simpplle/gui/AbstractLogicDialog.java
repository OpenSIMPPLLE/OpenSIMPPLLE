/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.SystemKnowledge;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.io.File;

/**
 * Abstract Logic Dialog class. This is not an abstract class despite its title.  Therefore it can be referenced directly.
 * It's main purpose it to set up the base for all Logic Dialogs.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class AbstractLogicDialog extends JDialog {

  protected String[]              panelKinds;
  protected String                currentPanelKind;
  protected BaseLogicPanel         currentPanel;
  protected BaseLogicPanel[]       tabPanels;
  protected SystemKnowledge.Kinds sysKnowKind;

  protected JPanel mainPanel = new JPanel(new BorderLayout());
  protected JTabbedPane tabbedPane = new JTabbedPane();
  protected JMenuItem menuFileClose = new JMenuItem();
  protected JMenuItem menuFileSave = new JMenuItem();

  /**
   * Constructor which calls jbInit() to start abstract logic UI
   * @param owner
   * @param title
   * @param modal
   */
  protected AbstractLogicDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public AbstractLogicDialog() {
    this(new Frame(), "LogicDialog", false);
  }
  /**
   * 
   * @throws Exception
   */
  private void jbInit() throws Exception {

    JPanel tabsPanel = new JPanel(new BorderLayout());
    JTabbedPane tabbedPane = new JTabbedPane();
    JMenuBar menuBar = new JMenuBar();

    JMenu menuFile = new JMenu("File");
    JMenuItem menuFileOpen = new JMenuItem("Open");
    menuFileOpen.addActionListener(this::open);
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(this::close);
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(this::save);
    JMenuItem menuFileSaveAs = new JMenuItem("Save As");
    menuFileSaveAs.addActionListener(this::saveAs);
    JMenuItem menuFileLoadDefault = new JMenuItem("Load Default");
    menuFileLoadDefault.addActionListener(this::loadDefault);
    JMenuItem menuFileQuit = new JMenuItem("Close Dialog");
    menuFileQuit.addActionListener(this::quit);
    JMenu menuAction = new JMenu("Action");
    JMenuItem menuActionMoveRuleUp = new JMenuItem("Move Rule Up");
    menuActionMoveRuleUp.addActionListener(this::moveRuleUp);
    JMenuItem menuActionMoveRuleDown = new JMenuItem("Move Rule Down");
    menuActionMoveRuleDown.addActionListener(this::moveRuleDown);
    JMenuItem menuActionInsertNewRule = new JMenuItem("Insert New Rule");
    menuActionInsertNewRule.addActionListener(this::insertNewRule);
    JMenuItem menuActionDeleteSelectedRule = new JMenuItem("Delete Selected Rule");
    menuActionDeleteSelectedRule.addActionListener(this::deleteSelectedRule);
    JMenuItem menuActionDuplicateSelectedRule = new JMenuItem("Duplicate Selected Rule");
    menuActionDuplicateSelectedRule.addActionListener(this::duplicateSelectedRule);
    tabbedPane.addChangeListener(this::tabbedPane_stateChanged);
    JMenu menuKnowledgeSource = new JMenu("Knowledge Source");
    JMenuItem menuKnowledgeSourceEdit = new JMenuItem("Display/Edit");
    menuKnowledgeSourceEdit.addActionListener(this::menuKnowledgeSourceEdit);

    getContentPane().add(mainPanel);
    mainPanel.add(tabsPanel, java.awt.BorderLayout.CENTER);
    tabsPanel.add(tabbedPane, java.awt.BorderLayout.CENTER);

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

  protected void initialize(String[] kinds) {
    this.panelKinds = kinds;
//      tabPanels[i].addPropertyChangeListener("rule.changed",new PropertyChangeListener() {
//        public void propertyChange(PropertyChangeEvent e) {
//          ruleChanged(e);
//        }
//      });

  }

  protected void this_componentResized(ComponentEvent e) {

    // These lines cause excessive flickering and do not seem to be
    // necessary during resizing.

    //currentPanel.updateColumnWidth();
    //updateDialog();

  }
  /**
   * Updates the Abstract Logic Dialog.
   */
  protected void updateDialog() {
    update(getGraphics());
  }
  /**
   * Checks if System Knowledge class has a file and that file has changed or user data, if so enables File save, if has file enables close menus
   */
  protected void updateMenuItems() {
    boolean hasFile =  (SystemKnowledge.getFile(sysKnowKind) != null);
    menuFileSave.setEnabled(
      SystemKnowledge.hasChangedOrUserData(sysKnowKind) && hasFile);
    menuFileClose.setEnabled(hasFile);
  }
  /**
   * If open is selected from JMenu, will open the file containing this type of 
   * @param e
   */
  public void open(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this,sysKnowKind,menuFileSave,menuFileClose);
    //  This line must be done first!
    for (int i=0; i<tabPanels.length; i++) {
      tabPanels[i].dataModel.fireTableStructureChanged();
      tabPanels[i].updateColumns();
    }

//    currentPanel.dataModel.fireTableStructureChanged();
//    currentPanel.updateColumns();
    updateMenuItems();
    currentPanel.updateDialog();
    updateDialog();
  }
  /**
   * Loads default logic from System Knowledge class.  
   * @throws SimpplleError catches the error thrown from SystemKnowledge loadZoneKnowledge
   */
  protected void loadDefaults() {
    try {
      String msg = "This will load the default Logic.\n\n" +
                   "Do you wish to continue?";
      String title = "Load Default Logic";

      if (Utility.askYesNoQuestion(this,msg,title)) {
        SystemKnowledge.loadZoneKnowledge(sysKnowKind);
        //  This line must be done first!
        currentPanel.dataModel.fireTableStructureChanged();
        currentPanel.updateColumns();
        updateMenuItems();
        currentPanel.updateDialog();
        updateDialog();
        menuFileSave.setEnabled(isSaveNeeded());
        menuFileClose.setEnabled(false);
      }
    }
    catch (simpplle.comcode.SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
  /**
   * If close is choosen from menu file close loads defaults.
   * @param e
   */
  public void close(ActionEvent e) {
    loadDefaults();
  }
  /**
   * Checks if save file menu should be enabled.  
   * @return true if System Knowledge has a file and system knowledge changed or has user data input.  
   */
  protected boolean isSaveNeeded() {
    return ( (SystemKnowledge.getFile(sysKnowKind) != null) &&
             (SystemKnowledge.hasChangedOrUserData(sysKnowKind)) );
  }
  /**
   * If save menu item selected, SystemKnowledgeFiler class saves to a file using SystemKnowledge getFile method.  
   * @param e save menu item selected
   * @see SystemKnowledgeFiler()
   */
  public void save(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(sysKnowKind);
    SystemKnowledgeFiler.saveFile(this,outfile,sysKnowKind,menuFileSave,menuFileClose);
  }
  /**
   * 'Save as' function sends to overloaded SystemKnowledgeFiler.saveFile method.
   * @param e Save As menu item selected
   */
  public void saveAs(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this,sysKnowKind,menuFileSave,menuFileClose);
  }

  private void loadDefault(ActionEvent e) {
    loadDefaults();
  }

  public void quit(ActionEvent e) {
    setVisible(false);
  }

  private void moveRuleUp(ActionEvent e) {
    currentPanel.moveRowUp();
    updateDialog();
  }

  private void moveRuleDown(ActionEvent e) {
    currentPanel.moveRowDown();
    updateDialog();
  }
  /**
   * Inserts a new row in the currentPanel.
   * @param e "Insert New Rule" menu item selected
   */
  private void insertNewRule(ActionEvent e) {
    currentPanel.insertRow();
    updateDialog();
  }
  /**
   * Deletes selected rule containing row.  
   * @param e "Delete Selected Rule" selected
   */
  private void deleteSelectedRule(ActionEvent e) {
    currentPanel.deleteSelectedRow();
    updateDialog();
  }

  private void duplicateSelectedRule(ActionEvent e) {
    currentPanel.duplicateSelectedRow();
    updateDialog();
  }
  /**
   * 
   * @param e
   */
  public void tabbedPane_stateChanged(ChangeEvent e) {
    int index = tabbedPane.getSelectedIndex();
    currentPanelKind = panelKinds[index];
    currentPanel     = tabPanels[index];

    tabPanels[index].updateDialog();

    updateMenuItems();
    updateDialog();
  }

  public void menuKnowledgeSourceEdit(ActionEvent e) {
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

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.SystemKnowledge;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.File;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>Abstract Logic Dialog class. This is not an abstract class despite its title.  Therefore it can be referenced directly.  
 * It's main purpose it to set up the base for all Logic Dialogs.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *
 */

public class AbstractLogicDialog extends JDialog {
  protected String[]              panelKinds;
  protected String                currentPanelKind;
  protected BaseLogicPanel         currentPanel;
  protected BaseLogicPanel[]       tabPanels;
  protected SystemKnowledge.Kinds sysKnowKind;

  protected JPanel mainPanel = new JPanel();
  protected BorderLayout borderLayout1 = new BorderLayout();
  protected JPanel tabsPanel = new JPanel();
  protected BorderLayout borderLayout2 = new BorderLayout();
  protected JTabbedPane tabbedPane = new JTabbedPane();
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

    mainPanel.setLayout(borderLayout1);
    tabsPanel.setLayout(borderLayout2);

    menuFile.setText("File");
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new
        AbstractLogicDialog_menuFileOpen_actionAdapter(this));
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new
        AbstractLogicDialog_menuFileClose_actionAdapter(this));
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new
        AbstractLogicDialog_menuFileSave_actionAdapter(this));
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new
        AbstractLogicDialog_menuFileSaveAs_actionAdapter(this));
    menuFileLoadDefault.setText("Load Default");
    menuFileLoadDefault.addActionListener(new
        AbstractLogicDialog_menuFileLoadDefault_actionAdapter(this));
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new
        AbstractLogicDialog_menuFileQuit_actionAdapter(this));

    menuAction.setText("Action");
    menuActionMoveRuleUp.setText("Move Rule Up");
    menuActionMoveRuleUp.addActionListener(new
        AbstractLogicDialog_menuActionMoveRuleUp_actionAdapter(this));
    menuActionMoveRuleDown.setText("Move Rule Down");
    menuActionMoveRuleDown.addActionListener(new
        AbstractLogicDialog_menuActionMoveRuleDown_actionAdapter(this));
    menuActionInsertNewRule.setText("Insert New Rule");
    menuActionInsertNewRule.addActionListener(new
        AbstractLogicDialog_menuActionInsertNewRule_actionAdapter(this));
    menuActionDeleteSelectedRule.setText("Delete Selected Rule");
    menuActionDeleteSelectedRule.addActionListener(new
        AbstractLogicDialog_menuActionDeleteSelectedRule_actionAdapter(this));
    menuActionDuplicateSelectedRule.setText("Duplicate Selected Rule");
    menuActionDuplicateSelectedRule.addActionListener(new
        AbstractLogicDialog_menuActionDuplicateSelectedRule_actionAdapter(this));
    tabbedPane.addChangeListener(new
        AbstractLogicDialog_tabbedPane_changeAdapter(this));
    menuKnowledgeSource.setText("Knowledge Source");
    menuKnowledgeSourceEdit.setText("Display/Edit");
    menuKnowledgeSourceEdit.addActionListener(new
      AbstractLogicDialog_menuKnowledgeSourceEdit_actionAdapter(this));

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
  public void menuFileOpen_actionPerformed(ActionEvent e) {
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
   * @throws SimpplleError catches the error thrown from SystemKnowledge readZoneDefault
   */
  protected void loadDefaults() {
    try {
      String msg = "This will load the default Logic.\n\n" +
                   "Do you wish to continue?";
      String title = "Load Default Logic";
    
      if (Utility.askYesNoQuestion(this,msg,title)) {
        SystemKnowledge.readZoneDefault(sysKnowKind);
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
  public void menuFileClose_actionPerformed(ActionEvent e) {
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
  public void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(sysKnowKind);
    SystemKnowledgeFiler.saveFile(this,outfile,sysKnowKind,menuFileSave,menuFileClose);
  }
/**
 * 'Save as' function sends to overloaded SystemKnowledgeFiler.saveFile method.   
 * @param e Save As menu item selected
 */
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
    currentPanel.moveRowUp();
    updateDialog();
  }

  public void menuActionMoveRuleDown_actionPerformed(ActionEvent e) {
    currentPanel.moveRowDown();
    updateDialog();
  }
/**
 * Inserts a new row in the currentPanel.  
 * @param e "Insert New Rule" menu item selected
 */
  public void menuActionInsertNewRule_actionPerformed(ActionEvent e) {
    currentPanel.insertRow();
    updateDialog();
  }

  /**
   * Deletes selected rule containing row.  
   * @param e "Delete Selected Rule" selected
   */
  public void menuActionDeleteSelectedRule_actionPerformed(ActionEvent e) {
    currentPanel.deleteSelectedRow();
    updateDialog();
  }
  public void menuActionDuplicateSelectedRule_actionPerformed(ActionEvent e) {
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

class AbstractLogicDialog_tabbedPane_changeAdapter implements
    ChangeListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_tabbedPane_changeAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void stateChanged(ChangeEvent e) {
    adaptee.tabbedPane_stateChanged(e);
  }
}

class AbstractLogicDialog_menuActionDeleteSelectedRule_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuActionDeleteSelectedRule_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionDeleteSelectedRule_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuActionDuplicateSelectedRule_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuActionDuplicateSelectedRule_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionDuplicateSelectedRule_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuActionInsertNewRule_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuActionInsertNewRule_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionInsertNewRule_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuActionMoveRuleDown_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuActionMoveRuleDown_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionMoveRuleDown_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuActionMoveRuleUp_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuActionMoveRuleUp_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionMoveRuleUp_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuFileQuit_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuFileQuit_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileQuit_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuFileLoadDefault_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuFileLoadDefault_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileLoadDefault_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuFileSaveAs_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuFileSaveAs_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSaveAs_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuFileSave_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuFileSave_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSave_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuFileClose_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuFileClose_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileClose_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuFileOpen_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuFileOpen_actionAdapter(
      AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileOpen_actionPerformed(e);
  }
}

class AbstractLogicDialog_menuKnowledgeSourceEdit_actionAdapter implements
    ActionListener {
  private AbstractLogicDialog adaptee;
  AbstractLogicDialog_menuKnowledgeSourceEdit_actionAdapter(AbstractLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuKnowledgeSourceEdit_actionPerformed(e);
  }
}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JDialog;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import simpplle.JSimpplle;
import simpplle.comcode.LogicRule;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SystemKnowledge;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class InsectDiseaseLogic extends JDialog {
  private LogicRuleBasicPanel[] tabPanels;
  private ArrayList<ProcessType>         processes;

  private javax.swing.JPanel jContentPane = null;

  private JPanel mainPanel = null;
  private JPanel northPanel = null;
  private JPanel centralPanel = null;
  private JTabbedPane tabbedPane = null;
        private JMenuBar jJMenuBar = null;
        private JMenu menuFile = null;
        private JMenuItem menuFileNew = null;
        private JMenuItem menuFileSaveAs = null;
        private JMenuItem menuFileClose = null;
        private JMenuItem menuFileSave = null;
        private JMenuItem menuFileOpen = null;
  /**
   * @throws java.awt.HeadlessException
   */
  public InsectDiseaseLogic() throws HeadlessException {
    super();
    // TODO Auto-generated constructor stub

    initialize();
 }

  /**
   * @param owner
   * @throws java.awt.HeadlessException
   */
  public InsectDiseaseLogic(Frame owner) throws HeadlessException {
    super(owner);
    // TODO Auto-generated constructor stub

    initialize();
    myInitialize();
 }

  /**
   * This method initializes this
   *
   * @return void
   */
  private void initialize() {
    this.setTitle("Insect -- Disease Logic");
    this.setJMenuBar(getJJMenuBar());
    this.setSize(627, 423);
    this.setContentPane(getJContentPane());
  }
  private void myInitialize() {
    processes = Process.getProbLogicProcesses();
    JPanel panel;

    tabPanels = new LogicRuleBasicPanel[processes.size()];
    for (int i = 0; i < processes.size(); i++) {
      ProcessType process = processes.get(i);
      tabPanels[i] = new LogicRuleBasicPanel(this,process);
      tabbedPane.add(tabPanels[i],process.toString());

      tabPanels[i].addPropertyChangeListener("rule.changed",new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e) {
          ruleChanged(e);
        }
      });
    }
//    tabbedPane.add(new JPanel(),ProcessType.WSBW.toString());

    setSize(getPreferredSize().width+25,500);
    update(getGraphics());
  }

  private void updatePanels() {
    for (int i = 0; i < tabPanels.length; i++) {
      tabPanels[i].updateAll();
    }
  }

  /**
   * This method initializes jContentPane
   *
   * @return javax.swing.JPanel
   */
  private javax.swing.JPanel getJContentPane() {
    if(jContentPane == null) {
      jContentPane = new javax.swing.JPanel();
      jContentPane.setLayout(new java.awt.BorderLayout());
      jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
    }
    return jContentPane;
  }
  /**
   * This method initializes mainPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getMainPanel() {
    if (mainPanel == null) {
      mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(getNorthPanel(), java.awt.BorderLayout.NORTH);
      mainPanel.add(getCentralPanel(), java.awt.BorderLayout.CENTER);
    }
    return mainPanel;
  }
  /**
   * This method initializes northPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getNorthPanel() {
    if (northPanel == null) {
      northPanel = new JPanel();
    }
    return northPanel;
  }
  /**
   * This method initializes centralPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getCentralPanel() {
    if (centralPanel == null) {
      centralPanel = new JPanel();
      centralPanel.setLayout(new BorderLayout());
      centralPanel.add(getTabbedPane(), java.awt.BorderLayout.CENTER);
    }
    return centralPanel;
  }
  /**
   * This method initializes tabbedPane
   *
   * @return javax.swing.JTabbedPane
   */
  private JTabbedPane getTabbedPane() {
    if (tabbedPane == null) {
      tabbedPane = new JTabbedPane();
      tabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
    }
    return tabbedPane;
  }
        /**
         * This method initializes jJMenuBar
         *
         * @return javax.swing.JMenuBar
         */
        private JMenuBar getJJMenuBar() {
                if (jJMenuBar == null) {
                        jJMenuBar = new JMenuBar();
                        jJMenuBar.setName("");
                        jJMenuBar.add(getMenuFile());
                }
                return jJMenuBar;
        }
        /**
         * This method initializes menuFile
         *
         * @return javax.swing.JMenu
         */
        private JMenu getMenuFile() {
                if (menuFile == null) {
                        menuFile = new JMenu();
                        menuFile.setText("File");
                        menuFile.add(getMenuFileNew());
                        menuFile.add(getMenuFileOpen());
                        menuFile.add(getMenuFileClose());
                        menuFile.add(getMenuFileSave());
                        menuFile.add(getMenuFileSaveAs());
                }
                return menuFile;
        }
        /**
         * This method initializes menuFileNew
         *
         * @return javax.swing.JMenuItem
         */
        private JMenuItem getMenuFileNew() {
                if (menuFileNew == null) {
                        menuFileNew = new JMenuItem();
                        menuFileNew.setText("New Rule");
                        menuFileNew.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                        menuFileNew_actionPerformed(e);
                                }
                        });
                }
                return menuFileNew;
        }
  public void menuFileNew_actionPerformed(ActionEvent e) {
    int index = tabbedPane.getSelectedIndex();
    LogicRule rule = new LogicRule(Process.findInstance(processes.get(index)).getProbabilityLabels().length);
    LogicRuleBuilder dlg = new LogicRuleBuilder(JSimpplle.getSimpplleMain(),rule);
    dlg.setVisible(true);
    if (dlg.updated()) {
      tabPanels[index].AddNewRule(rule);
    }
  }
        /**
         * This method initializes menuFileSaveAs
         *
         * @return javax.swing.JMenuItem
         */
        private JMenuItem getMenuFileSaveAs() {
                if (menuFileSaveAs == null) {
                        menuFileSaveAs = new JMenuItem();
                        menuFileSaveAs.setText("Save As");
                        menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
          menuFileSaveAs_actionPerformed(e);
                                }
                        });
                }
                return menuFileSaveAs;
        }
  public void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.PROCESS_PROB_LOGIC,
        menuFileSave, menuFileClose);
  }
        /**
         * This method initializes menuFileClose
         *
         * @return javax.swing.JMenuItem
         */
        private JMenuItem getMenuFileClose() {
                if (menuFileClose == null) {
                        menuFileClose = new JMenuItem();
                        menuFileClose.setText("Close");
                        menuFileClose.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                        menuFileClose_actionPerformed(e);
                                }
                        });
                }
                return menuFileClose;
        }
  public void menuFileClose_actionPerformed(ActionEvent e) {
    loadDefaults();
  }
  private boolean isSaveNeeded() {
    return ((SystemKnowledge.getFile(SystemKnowledge.PROCESS_PROB_LOGIC) != null) &&
            (SystemKnowledge.hasChangedOrUserData(SystemKnowledge.PROCESS_PROB_LOGIC)));
  }
  private void loadDefaults() {
    int choice;
    try {
      String msg = "This will load the default Insect/Disease Logic.\n\n" +
                   "Do you wish to continue?";
      String title = "Load Default Insect/Disease Logic";

      if (Utility.askYesNoQuestion(this,msg,title)) {
        SystemKnowledge.readZoneDefault(SystemKnowledge.PROCESS_PROB_LOGIC);
        updatePanels();
        menuFileSave.setEnabled(isSaveNeeded());
        menuFileClose.setEnabled(false);
      }
    }
    catch (simpplle.comcode.SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
  public void markChanged() {
    SystemKnowledge.markChanged(SystemKnowledge.PROCESS_PROB_LOGIC);
  }
  public void ruleChanged(PropertyChangeEvent e) {
    markChanged();
    menuFileSave.setEnabled(isSaveNeeded());
  }
        /**
         * This method initializes menuFileSave
         *
         * @return javax.swing.JMenuItem
         */
        private JMenuItem getMenuFileSave() {
                if (menuFileSave == null) {
                        menuFileSave = new JMenuItem();
                        menuFileSave.setText("Save");
                        menuFileSave.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                        menuFileSave_actionPerformed(e);
                                }
                        });
                }
                return menuFileSave;
        }
  public void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.PROCESS_PROB_LOGIC);
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.PROCESS_PROB_LOGIC,
        menuFileSave, menuFileClose);
  }
        /**
         * This method initializes menuFileOpen
         *
         * @return javax.swing.JMenuItem
         */
        private JMenuItem getMenuFileOpen() {
                if (menuFileOpen == null) {
                        menuFileOpen = new JMenuItem();
                        menuFileOpen.setText("Open");
                        menuFileOpen.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                        menuFileOpen_actionPerformed(e);
                                }
                        });
                }
                return menuFileOpen;
        }
  void menuFileOpen_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this,SystemKnowledge.PROCESS_PROB_LOGIC,menuFileSave,menuFileClose);
    updatePanels();
  }
       }  //  @jve:decl-index=0:visual-constraint="10,10"




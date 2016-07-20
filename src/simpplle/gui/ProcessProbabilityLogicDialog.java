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
import javax.swing.JTabbedPane;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Simpplle;
import javax.swing.JMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import simpplle.comcode.SystemKnowledge;
import javax.swing.JCheckBoxMenuItem;
import simpplle.comcode.ProcessProbLogic;
import java.util.ArrayList;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the Process Probability Logic Dialog, a type of VegLogicDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */

public class ProcessProbabilityLogicDialog extends VegLogicDialog {
  private JCheckBoxMenuItem menuAdjProcess = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuLpMpbHazard = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuLpMpbModHazard = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuLpMpbHighHazard = new JCheckBoxMenuItem();
  public ProcessProbabilityLogicDialog(Frame owner, String title, boolean modal) {
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
 * Constructor for Process Probability Logic Dialog.  Calls the VegLogicDialog() superclass.  
 */
  public ProcessProbabilityLogicDialog() {
    super();
  }
/**
 * 
 * @throws Exception
 */
  private void jbInit() throws Exception {
    menuAdjProcess.setText("Adjacent Process");
    menuAdjProcess.addActionListener(new
        ProcessProbabilityLogicDialog_menuAdjProcess_actionAdapter(this));
    menuLpMpbHazard.setText("MPB Hazard");
    menuLpMpbHazard.addActionListener(new
        ProcessProbabilityLogicDialog_menuLpMpbHazard_actionAdapter(this));
    menuLpMpbModHazard.setText("Adjacent MPB Moderate Hazard");
    menuLpMpbModHazard.addActionListener(new
        ProcessProbabilityLogicDialog_menuLpMpbModHazard_actionAdapter(this));
    menuLpMpbHighHazard.setText("Adjacent MPB High Hazard");
    menuLpMpbHighHazard.addActionListener(new
        ProcessProbabilityLogicDialog_menuLpMpbHighHazard_actionAdapter(this));

    menuColumns.add(menuAdjProcess);
    menuColumns.add(menuLpMpbHazard);
    menuColumns.add(menuLpMpbModHazard);
    menuColumns.add(menuLpMpbHighHazard);
  }

  private void initialize() {
    sysKnowKind = SystemKnowledge.PROCESS_PROB_LOGIC;
    ArrayList<ProcessType> processes = simpplle.comcode.Process.getProbLogicProcesses();

    String[] kinds = new String[processes.size()];
    for (int i=0; i<processes.size(); i++) {
      kinds[i] = processes.get(i).toString();
    }
    super.initialize(kinds);

    colMenuItems.add(ProcessProbLogic.ADJ_PROCESS_COL, menuAdjProcess);
    colMenuItems.add(ProcessProbLogic.MPB_HAZARD_COL, menuLpMpbHazard);
    colMenuItems.add(ProcessProbLogic.ADJ_MOD_HAZARD_COL, menuLpMpbHighHazard);
    colMenuItems.add(ProcessProbLogic.ADJ_HIGH_HAZARD_COL, menuLpMpbModHazard);

    tabPanels = new ProcessProbabilityLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new ProcessProbabilityLogicPanel(this, kind,
                                           ProcessProbLogic.getInstance(),
                                           sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }


    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);

//  ProcessProbLogic.fillNewRoots();
    updateDialog();
  }

  public void menuAdjProcess_actionPerformed(ActionEvent e) {
    columnMenuClicked(ProcessProbLogic.ADJ_PROCESS_COL);
  }
  public void menuLpMpbHazard_actionPerformed(ActionEvent e) {
    columnMenuClicked(ProcessProbLogic.MPB_HAZARD_COL);
  }
  public void menuLpMpbModHazard_actionPerformed(ActionEvent e) {
    columnMenuClicked(ProcessProbLogic.ADJ_MOD_HAZARD_COL);
  }
  public void menuLpMpbHighHazard_actionPerformed(ActionEvent e) {
    columnMenuClicked(ProcessProbLogic.ADJ_HIGH_HAZARD_COL);
  }
}

class ProcessProbabilityLogicDialog_menuLpMpbHighHazard_actionAdapter implements
    ActionListener {
  private ProcessProbabilityLogicDialog adaptee;
  ProcessProbabilityLogicDialog_menuLpMpbHighHazard_actionAdapter(
      ProcessProbabilityLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuLpMpbHighHazard_actionPerformed(e);
  }
}

class ProcessProbabilityLogicDialog_menuLpMpbModHazard_actionAdapter implements
    ActionListener {
  private ProcessProbabilityLogicDialog adaptee;
  ProcessProbabilityLogicDialog_menuLpMpbModHazard_actionAdapter(
      ProcessProbabilityLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuLpMpbModHazard_actionPerformed(e);
  }
}

class ProcessProbabilityLogicDialog_menuLpMpbHazard_actionAdapter implements
    ActionListener {
  private ProcessProbabilityLogicDialog adaptee;
  ProcessProbabilityLogicDialog_menuLpMpbHazard_actionAdapter(
      ProcessProbabilityLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuLpMpbHazard_actionPerformed(e);
  }
}

class ProcessProbabilityLogicDialog_menuAdjProcess_actionAdapter implements
    ActionListener {
  private ProcessProbabilityLogicDialog adaptee;
  ProcessProbabilityLogicDialog_menuAdjProcess_actionAdapter(
      ProcessProbabilityLogicDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuAdjProcess_actionPerformed(e);
  }
}


/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.JDialog;


import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JList;

import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.LogicRule;
import simpplle.comcode.SimpplleType;
import simpplle.comcode.SizeClass;
import simpplle.comcode.Species;
import simpplle.comcode.Process;
import simpplle.comcode.Treatment;

import javax.swing.JTabbedPane;

/**
 * This class is deprecated and will be deleted in upcoming versions of OpenSimpplle
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 *
 * @deprecated
 */

public class LogicRuleBuilder extends JDialog {
  private final String groupPrototypeItem = "UPPER-MONTANE  ";
  private final String speciesPrototypeItem = "RIPARIAN_GRASSES  ";
  private final String sizeClassPrototypeItem = "CLOSED_TALL_SHRUB  ";
  private final String densityPrototypeItem = "W      ";
  private final String processPrototypeItem = "ONE_HUNDRED_YEAR_FLOOD  ";
  private final String treatmentPrototypeItem = "ECOSYSTEM_MANAGEMENT_THIN";

  private LogicRule currentRule=null;
  private boolean   updated=false;

  private javax.swing.JPanel jContentPane = null;

  private JPanel mainPanel = null;
  private JPanel listScrollPanel = null;
  private JPanel groupListPanel = null;
  private JPanel groupListSourcePanel = null;
  private JPanel groupListDropPanel = null;
  private JScrollPane groupListSourceScroll = null;
  private JScrollPane groupListDropScroll = null;
  private DragSourceList groupListSourceList = null;
  private DragDropList groupListDropList = null;
  private JPanel speciesListPanel = null;
  private JPanel speciesListSourcePanel = null;
  private JPanel speciesListDropPanel = null;
  private JScrollPane speciesListSourceScroll = null;
  private JScrollPane speciesListDropScroll = null;
  private DragSourceList speciesListSourceList = null;
  private DragDropList speciesListDropList = null;
  private JPanel sizeClassListPanel = null;
  private JPanel sizeClassListSourcePanel = null;
  private JPanel sizeClassListDropPanel = null;
  private JScrollPane sizeClassListSourceScroll = null;
  private JScrollPane sizeClassListDropScroll = null;
  private DragSourceList sizeClassListSourceList = null;
  private DragDropList sizeClassListDropList = null;
  private JPanel densityListPanel = null;
  private JPanel densityListSourcePanel = null;
  private JPanel densityListDropPanel = null;
  private JScrollPane densityListSourceScroll = null;
  private JScrollPane densityListDropScroll = null;
  private DragSourceList densityListSourceList = null;
  private DragDropList densityListDropList = null;
  private JPanel processListPanel = null;
  private JPanel processListSoucePanel = null;
  private JPanel processListDropPanel = null;
  private JScrollPane processListSourceScroll = null;
  private JScrollPane processListDropScroll = null;
  private DragSourceList processListSourceList = null;
  private DragDropList processListDropList = null;
  private JPanel treatmentListPanel = null;
  private JPanel treatmentListSourcePanel = null;
  private JPanel treatmentListDropPanel = null;
  private JScrollPane treatmentListSourceScroll = null;
  private JScrollPane treatmentListDropScroll = null;
  private DragSourceList treatmentListSourceList = null;
  private DragDropList treatmentListDropList = null;
  private JPanel adjacentEditPanel = null;
  private JButton adjacentEditPB = null;
  private JPanel tabbedPanePanel = null;
  private JTabbedPane tabbedPane = null;
        private JPanel buttonPanel = null;
        private JButton okPB = null;
        private JButton cancelPB = null;
  /**
   * @param owner
   * @throws java.awt.HeadlessException
   */
  public LogicRuleBuilder(Frame owner, LogicRule rule) throws HeadlessException {
    super(owner);
    currentRule = rule;
    initialize();
    initializeDialog();
  }
  public LogicRuleBuilder(Dialog owner, LogicRule rule) throws HeadlessException {
    super(owner);
    currentRule = rule;
    initialize();
    initializeDialog();
  }

  public LogicRuleBuilder(Frame owner) throws HeadlessException {
    this(owner,null);
  }
  /**
   * This is the default constructor
   */
  public LogicRuleBuilder() {
    super();
    currentRule = null;
    initialize();
    initializeDialog();
  }

  /**
   * This method initializes this
   *
   * @return void
   */
  private void initialize() {
    this.setModal(true);
    this.setTitle("Logic Rule Builder");
    this.setSize(837, 515);
    this.setContentPane(getJContentPane());
    this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                    this_windowClosing(e);
            }
    });
  }

  private void initializeDialog() {
    groupListSourceList.setPrototypeCellValue(groupPrototypeItem);
    groupListDropList.setPrototypeCellValue(groupPrototypeItem);

    speciesListSourceList.setPrototypeCellValue(speciesPrototypeItem);
    speciesListDropList.setPrototypeCellValue(speciesPrototypeItem);

    sizeClassListSourceList.setPrototypeCellValue(sizeClassPrototypeItem);
    sizeClassListDropList.setPrototypeCellValue(sizeClassPrototypeItem);

    densityListSourceList.setPrototypeCellValue(densityPrototypeItem);
    densityListDropList.setPrototypeCellValue(densityPrototypeItem);

    processListSourceList.setPrototypeCellValue(processPrototypeItem);
    processListDropList.setPrototypeCellValue(processPrototypeItem);

    treatmentListSourceList.setPrototypeCellValue(treatmentPrototypeItem);
    treatmentListDropList.setPrototypeCellValue(treatmentPrototypeItem);

    if (currentRule != null) {
      groupListSourceList.setListData(HabitatTypeGroup.getLoadedGroupNames());
      speciesListSourceList.setListData(Species.getList(SimpplleType.SPECIES));
      sizeClassListSourceList.setListData(HabitatTypeGroup.getValidSizeClass());
      densityListSourceList.setListData(HabitatTypeGroup.getValidDensity());
      processListSourceList.setListData(Process.getSimulationProcesses());
      treatmentListSourceList.setListData(Treatment.getLegalTreatmentList());

      SimpplleType[] groupList = currentRule.getGroups();
        groupListDropList.setListData(groupList);
        groupListDropList.setDialogDragSourceList(groupListSourceList);

      SimpplleType[] speciesList = currentRule.getSpecies();
        speciesListDropList.setListData(speciesList);
        speciesListDropList.setDialogDragSourceList(speciesListSourceList);

      SimpplleType[] sizeClassList = currentRule.getSizeClasses();
        sizeClassListDropList.setListData(sizeClassList);
        sizeClassListDropList.setDialogDragSourceList(sizeClassListSourceList);

      SimpplleType[] densityList = currentRule.getDensities();
        densityListDropList.setListData(densityList);
        densityListDropList.setDialogDragSourceList(densityListSourceList);

      SimpplleType[] processList = currentRule.getProcesses();
        processListDropList.setListData(processList);
        processListDropList.setDialogDragSourceList(processListSourceList);

      SimpplleType[] treatmentList = currentRule.getTreatments();
        treatmentListDropList.setListData(treatmentList);
        treatmentListDropList.setDialogDragSourceList(treatmentListSourceList);
    }
    update(getGraphics());
  }

  // **********************
  // *** Event Handlers ***
  // **********************
  public void adjacentEditPB_actionPerformed(java.awt.event.ActionEvent e) {
    LogicRule rule = currentRule.getAdjacentRule();
    boolean   newRule = false;
    if (rule == null) {
      rule = new LogicRule();
      newRule = true;
    }

    LogicRuleBuilder dlg = new LogicRuleBuilder(this,rule);
    dlg.tabbedPane.removeTabAt(6);
    dlg.setTitle("Adjacent Units Logic Rule");
    dlg.setLocation(getLocation().x+50,getLocation().y+50);
    dlg.setVisible(true);

    if (dlg.updated() && newRule) {
      currentRule.setAdjacentRule(rule);
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
      mainPanel.add(getListScrollPanel(), java.awt.BorderLayout.CENTER);
      mainPanel.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
    }
    return mainPanel;
  }
  /**
   * This method initializes listScrollPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getListScrollPanel() {
    if (listScrollPanel == null) {
      listScrollPanel = new JPanel();
      listScrollPanel.setLayout(new BorderLayout());
      listScrollPanel.add(getTabbedPanePanel(), java.awt.BorderLayout.NORTH);
    }
    return listScrollPanel;
  }
  /**
   * This method initializes groupListPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getGroupListPanel() {
    if (groupListPanel == null) {
      FlowLayout flowLayout6 = new FlowLayout();
      groupListPanel = new JPanel();
      groupListPanel.setLayout(flowLayout6);
      groupListPanel.setVisible(true);
      groupListPanel.add(getGroupListSourcePanel(), null);
      groupListPanel.add(getGroupListDropPanel(), null);
    }
    return groupListPanel;
  }
  /**
   * This method initializes groupListSourcePanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getGroupListSourcePanel() {
    if (groupListSourcePanel == null) {
      FlowLayout flowLayout7 = new FlowLayout();
      groupListSourcePanel = new JPanel();
      groupListSourcePanel.setLayout(flowLayout7);
      groupListSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      groupListSourcePanel.add(getGroupListSourceScroll(), null);
    }
    return groupListSourcePanel;
  }
  /**
   * This method initializes groupListDropPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getGroupListDropPanel() {
    if (groupListDropPanel == null) {
      FlowLayout flowLayout8 = new FlowLayout();
      groupListDropPanel = new JPanel();
      groupListDropPanel.setLayout(flowLayout8);
      groupListDropPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chosen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      groupListDropPanel.add(getGroupListDropScroll(), null);
    }
    return groupListDropPanel;
  }
  /**
   * This method initializes groupListSourceScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getGroupListSourceScroll() {
    if (groupListSourceScroll == null) {
      groupListSourceScroll = new JScrollPane();
      groupListSourceScroll.setViewportView(getGroupListSourceList());
    }
    return groupListSourceScroll;
  }
  /**
   * This method initializes groupListDropScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getGroupListDropScroll() {
    if (groupListDropScroll == null) {
      groupListDropScroll = new JScrollPane();
      groupListDropScroll.setViewportView(getGroupListDropList());
    }
    return groupListDropScroll;
  }
  /**
   * This method initializes groupListSourceList
   *
   * @return javax.swing.JList
   */
  private JList getGroupListSourceList() {
    if (groupListSourceList == null) {
      groupListSourceList = new DragSourceList(true);
      groupListSourceList.setVisibleRowCount(20);
    }
    return groupListSourceList;
  }
  /**
   * This method initializes groupListDropList
   *
   * @return javax.swing.JList
   */
  private JList getGroupListDropList() {
    if (groupListDropList == null) {
      groupListDropList = new DragDropList();
      groupListDropList.setVisibleRowCount(20);
    }
    return groupListDropList;
  }


  /**
   * This method initializes speciesListPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getSpeciesListPanel() {
    if (speciesListPanel == null) {
      speciesListPanel = new JPanel();
      speciesListPanel.setLayout(new FlowLayout());
      speciesListPanel.add(getSpeciesListSourcePanel(), null);
      speciesListPanel.add(getSpeciesListDropPanel(), null);
      speciesListPanel.setVisible(true);
    }
    return speciesListPanel;
  }
  /**
   * This method initializes speciesListSourcePanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getSpeciesListSourcePanel() {
    if (speciesListSourcePanel == null) {
      speciesListSourcePanel = new JPanel();
      speciesListSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      speciesListSourcePanel.add(getSpeciesListSourceScroll(), null);
    }
    return speciesListSourcePanel;
  }
  /**
   * This method initializes speciesListDropPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getSpeciesListDropPanel() {
    if (speciesListDropPanel == null) {
      speciesListDropPanel = new JPanel();
      speciesListDropPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chosen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("MS Sans Serif", java.awt.Font.PLAIN, 11), java.awt.Color.black));
      speciesListDropPanel.add(getSpeciesListDropScroll(), null);
    }
    return speciesListDropPanel;
  }
  /**
   * This method initializes speciesListSourceScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getSpeciesListSourceScroll() {
    if (speciesListSourceScroll == null) {
      speciesListSourceScroll = new JScrollPane();
      speciesListSourceScroll.setViewportView(getSpeciesListSourceList());
    }
    return speciesListSourceScroll;
  }
  /**
   * This method initializes speciesListDropScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getSpeciesListDropScroll() {
    if (speciesListDropScroll == null) {
      speciesListDropScroll = new JScrollPane();
      speciesListDropScroll.setViewportView(getSpeciesListDropList());
    }
    return speciesListDropScroll;
  }
  /**
   * This method initializes speciesListSourceList
   *
   * @return javax.swing.JList
   */
  private JList getSpeciesListSourceList() {
    if (speciesListSourceList == null) {
      speciesListSourceList = new DragSourceList(true);
      speciesListSourceList.setVisibleRowCount(20);
    }
    return speciesListSourceList;
  }
  /**
   * This method initializes speciesListDropList
   *
   * @return javax.swing.JList
   */
  private JList getSpeciesListDropList() {
    if (speciesListDropList == null) {
      speciesListDropList = new DragDropList();
      speciesListDropList.setVisibleRowCount(20);
    }
    return speciesListDropList;
  }
  /**
   * This method initializes sizeClassListPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getSizeClassListPanel() {
    if (sizeClassListPanel == null) {
      sizeClassListPanel = new JPanel();
      sizeClassListPanel.add(getSizeClassListSourcePanel(), null);
      sizeClassListPanel.add(getSizeClassListDropPanel(), null);
    }
    return sizeClassListPanel;
  }
  /**
   * This method initializes sizeClassListSourcePanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getSizeClassListSourcePanel() {
    if (sizeClassListSourcePanel == null) {
      sizeClassListSourcePanel = new JPanel();
      sizeClassListSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      sizeClassListSourcePanel.add(getSizeClassListSourceScroll(), null);
    }
    return sizeClassListSourcePanel;
  }
  /**
   * This method initializes sizeClassListDropPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getSizeClassListDropPanel() {
    if (sizeClassListDropPanel == null) {
      sizeClassListDropPanel = new JPanel();
      sizeClassListDropPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chosen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      sizeClassListDropPanel.add(getSizeClassListDropScroll(), null);
    }
    return sizeClassListDropPanel;
  }
  /**
   * This method initializes sizeClassListSourceScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getSizeClassListSourceScroll() {
    if (sizeClassListSourceScroll == null) {
      sizeClassListSourceScroll = new JScrollPane();
      sizeClassListSourceScroll.setViewportView(getSizeClassListSourceList());
    }
    return sizeClassListSourceScroll;
  }
  /**
   * This method initializes sizeClassListDropScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getSizeClassListDropScroll() {
    if (sizeClassListDropScroll == null) {
      sizeClassListDropScroll = new JScrollPane();
      sizeClassListDropScroll.setViewportView(getSizeClassListDropList());
    }
    return sizeClassListDropScroll;
  }
  /**
   * This method initializes sizeClassListSourceList
   *
   * @return javax.swing.JList
   */
  private JList getSizeClassListSourceList() {
    if (sizeClassListSourceList == null) {
      sizeClassListSourceList = new DragSourceList(true);
      sizeClassListSourceList.setVisibleRowCount(20);
    }
    return sizeClassListSourceList;
  }
  /**
   * This method initializes sizeClassListDropList
   *
   * @return javax.swing.JList
   */
  private JList getSizeClassListDropList() {
    if (sizeClassListDropList == null) {
      sizeClassListDropList = new DragDropList();
      sizeClassListDropList.setVisibleRowCount(20);
    }
    return sizeClassListDropList;
  }
  /**
   * This method initializes densityListPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getDensityListPanel() {
    if (densityListPanel == null) {
      densityListPanel = new JPanel();
      densityListPanel.add(getDensityListSourcePanel(), null);
      densityListPanel.add(getDensityListDropPanel(), null);
    }
    return densityListPanel;
  }
  /**
   * This method initializes densityListSourcePanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getDensityListSourcePanel() {
    if (densityListSourcePanel == null) {
      densityListSourcePanel = new JPanel();
      densityListSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      densityListSourcePanel.add(getDensityListSourceScroll(), null);
    }
    return densityListSourcePanel;
  }
  /**
   * This method initializes densityListDropPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getDensityListDropPanel() {
    if (densityListDropPanel == null) {
      densityListDropPanel = new JPanel();
      densityListDropPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chosen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      densityListDropPanel.add(getDensityListDropScroll(), null);
    }
    return densityListDropPanel;
  }
  /**
   * This method initializes densityListSourceScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getDensityListSourceScroll() {
    if (densityListSourceScroll == null) {
      densityListSourceScroll = new JScrollPane();
      densityListSourceScroll.setViewportView(getDensityListSourceList());
    }
    return densityListSourceScroll;
  }
  /**
   * This method initializes densityListDropScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getDensityListDropScroll() {
    if (densityListDropScroll == null) {
      densityListDropScroll = new JScrollPane();
      densityListDropScroll.setViewportView(getDensityListDropList());
    }
    return densityListDropScroll;
  }
  /**
   * This method initializes densityListSourceList
   *
   * @return javax.swing.JList
   */
  private JList getDensityListSourceList() {
    if (densityListSourceList == null) {
      densityListSourceList = new DragSourceList(true);
      densityListSourceList.setVisibleRowCount(20);
    }
    return densityListSourceList;
  }
  /**
   * This method initializes densityListDropList
   *
   * @return javax.swing.JList
   */
  private JList getDensityListDropList() {
    if (densityListDropList == null) {
      densityListDropList = new DragDropList();
      densityListDropList.setVisibleRowCount(20);
    }
    return densityListDropList;
  }
  /**
   * This method initializes processListPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getProcessListPanel() {
    if (processListPanel == null) {
      processListPanel = new JPanel();
      processListPanel.add(getProcessListSoucePanel(), null);
      processListPanel.add(getProcessListDropPanel(), null);
    }
    return processListPanel;
  }
  /**
   * This method initializes processListSoucePanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getProcessListSoucePanel() {
    if (processListSoucePanel == null) {
      processListSoucePanel = new JPanel();
      processListSoucePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      processListSoucePanel.add(getProcessListSourceScroll(), null);
    }
    return processListSoucePanel;
  }
  /**
   * This method initializes processListDropPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getProcessListDropPanel() {
    if (processListDropPanel == null) {
      processListDropPanel = new JPanel();
      processListDropPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chosen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      processListDropPanel.add(getProcessListDropScroll(), null);
    }
    return processListDropPanel;
  }
  /**
   * This method initializes processListSourceScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getProcessListSourceScroll() {
    if (processListSourceScroll == null) {
      processListSourceScroll = new JScrollPane();
      processListSourceScroll.setViewportView(getProcessListSourceList());
    }
    return processListSourceScroll;
  }
  /**
   * This method initializes processListDropScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getProcessListDropScroll() {
    if (processListDropScroll == null) {
      processListDropScroll = new JScrollPane();
      processListDropScroll.setViewportView(getProcessListDropList());
    }
    return processListDropScroll;
  }
  /**
   * This method initializes processListSourceList
   *
   * @return javax.swing.JList
   */
  private JList getProcessListSourceList() {
    if (processListSourceList == null) {
      processListSourceList = new DragSourceList(true);
      processListSourceList.setVisibleRowCount(20);
    }
    return processListSourceList;
  }
  /**
   * This method initializes processListDropList
   *
   * @return javax.swing.JList
   */
  private JList getProcessListDropList() {
    if (processListDropList == null) {
      processListDropList = new DragDropList();
      processListDropList.setVisibleRowCount(20);
    }
    return processListDropList;
  }
  /**
   * This method initializes treatmentListPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getTreatmentListPanel() {
    if (treatmentListPanel == null) {
      treatmentListPanel = new JPanel();
      treatmentListPanel.add(getTreatmentListSourcePanel(), null);
      treatmentListPanel.add(getTreatmentListDropPanel(), null);
    }
    return treatmentListPanel;
  }
  /**
   * This method initializes treatmentListSourcePanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getTreatmentListSourcePanel() {
    if (treatmentListSourcePanel == null) {
      treatmentListSourcePanel = new JPanel();
      treatmentListSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      treatmentListSourcePanel.add(getTreatmentListSourceScroll(), null);
    }
    return treatmentListSourcePanel;
  }
  /**
   * This method initializes treatmentListDropPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getTreatmentListDropPanel() {
    if (treatmentListDropPanel == null) {
      treatmentListDropPanel = new JPanel();
      treatmentListDropPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chosen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
      treatmentListDropPanel.add(getTreatmentListDropScroll(), null);
    }
    return treatmentListDropPanel;
  }
  /**
   * This method initializes treatmentListSourceScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getTreatmentListSourceScroll() {
    if (treatmentListSourceScroll == null) {
      treatmentListSourceScroll = new JScrollPane();
      treatmentListSourceScroll.setViewportView(getTreatmentListSourceList());
    }
    return treatmentListSourceScroll;
  }
  /**
   * This method initializes treatmentListDropScroll
   *
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getTreatmentListDropScroll() {
    if (treatmentListDropScroll == null) {
      treatmentListDropScroll = new JScrollPane();
      treatmentListDropScroll.setViewportView(getTreatmentListDropList());
    }
    return treatmentListDropScroll;
  }
  /**
   * This method initializes treatmentListSourceList
   *
   * @return javax.swing.JList
   */
  private JList getTreatmentListSourceList() {
    if (treatmentListSourceList == null) {
      treatmentListSourceList = new DragSourceList(true);
      treatmentListSourceList.setVisibleRowCount(20);
    }
    return treatmentListSourceList;
  }
  /**
   * This method initializes treatmentListDropList
   *
   * @return javax.swing.JList
   */
  private JList getTreatmentListDropList() {
    if (treatmentListDropList == null) {
      treatmentListDropList = new DragDropList();
      treatmentListDropList.setVisibleRowCount(20);
    }
    return treatmentListDropList;
  }
  /**
   * This method initializes adjacentEditPanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getAdjacentEditPanel() {
    if (adjacentEditPanel == null) {
      adjacentEditPanel = new JPanel();
      adjacentEditPanel.add(getAdjacentEditPB(), null);
    }
    return adjacentEditPanel;
  }
  /**
   * This method initializes adjacentEditPB
   *
   * @return javax.swing.JButton
   */
  private JButton getAdjacentEditPB() {
    if (adjacentEditPB == null) {
      adjacentEditPB = new JButton();
      adjacentEditPB.setText("Edit Adjacent Units Rule");
      adjacentEditPB.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          adjacentEditPB_actionPerformed(e);
        }
      });
    }
    return adjacentEditPB;
  }
  /**
   * This method initializes tabbedPanePanel
   *
   * @return javax.swing.JPanel
   */
  private JPanel getTabbedPanePanel() {
    if (tabbedPanePanel == null) {
      tabbedPanePanel = new JPanel();
      tabbedPanePanel.setLayout(new BorderLayout());
      tabbedPanePanel.add(getTabbedPane(), java.awt.BorderLayout.NORTH);
    }
    return tabbedPanePanel;
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
      tabbedPane.addTab("Habitat Type Group", null, getGroupListPanel(), null);
      tabbedPane.addTab("Species", null, getSpeciesListPanel(), null);
      tabbedPane.addTab("Size Class", null, getSizeClassListPanel(), null);
      tabbedPane.addTab("Density", null, getDensityListPanel(), null);
      tabbedPane.addTab("Process", null, getProcessListPanel(), null);
      tabbedPane.addTab("Treatment", null, getTreatmentListPanel(), null);
      tabbedPane.addTab("Adjacent Units", null, getAdjacentEditPanel(), null);
    }
    return tabbedPane;
  }
        /**
         * This method initializes buttonPanel
         *
         * @return javax.swing.JPanel
         */
        private JPanel getButtonPanel() {
                if (buttonPanel == null) {
                        buttonPanel = new JPanel();
                        buttonPanel.add(getOkPB(), null);
                        buttonPanel.add(getCancelPB(), null);
                }
                return buttonPanel;
        }
        /**
         * This method initializes okPB
         *
         * @return javax.swing.JButton
         */
        private JButton getOkPB() {
                if (okPB == null) {
                        okPB = new JButton();
                        okPB.setText("Ok");
                        okPB.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                        okPB_actionPerformed(e);
                                }
                        });
                }
                return okPB;
        }
  public void okPB_actionPerformed(ActionEvent e) {
    currentRule.setGroups(getListData((MyDefaultListModel)groupListDropList.getModel(),SimpplleType.HTGRP));
    currentRule.setSpecies(getListData((MyDefaultListModel)speciesListDropList.getModel(),SimpplleType.SPECIES));
    currentRule.setSizeClasses(getListData((MyDefaultListModel)sizeClassListDropList.getModel(),SimpplleType.SIZE_CLASS));
    currentRule.setDensities(getListData((MyDefaultListModel)densityListDropList.getModel(),SimpplleType.DENSITY));
    currentRule.setProcesses(getListData((MyDefaultListModel)processListDropList.getModel(),SimpplleType.PROCESS));
    currentRule.setTreatments(getListData((MyDefaultListModel)treatmentListDropList.getModel(),SimpplleType.TREATMENT));

    updated=true;
    setVisible(true);
    dispose();
  }

  private SimpplleType[] getListData(MyDefaultListModel model, SimpplleType.Types kind) {
    if (model.size() == 0) { return null; }
    SimpplleType[] data = new SimpplleType[model.size()];
    for(int i=0; i<model.size(); i++) {
      data[i] = SimpplleType.get(kind,(String)model.elementAt(i));
    }
    return data;
  }
  /**
   * If cancel button pushes, closes without saving. 
   * @param e 'cancel'
   */
  public void cancelPB_actionPerformed(ActionEvent e) {
    closeWithoutSaving();
  }
  /**
   * If window closes, closes without saving. 
   * @param e
   */
  public void this_windowClosing(WindowEvent e) {
    closeWithoutSaving();
  }
  /**
   * If either cancel or window closing event happens, this closes without saving data
   */
  private void closeWithoutSaving() {
    updated=false;
    setVisible(false);
    dispose();
  }
  public boolean updated() { return updated; }

        /**
         * This method initializes closeBtn
         *
         * @return javax.swing.JButton
         */
        private JButton getCancelPB() {
                if (cancelPB == null) {
                        cancelPB = new JButton();
                        cancelPB.setText("Cancel");
                        cancelPB.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                        cancelPB_actionPerformed(e);
                                }
                        });
                }
                return cancelPB;
        }
   }  //  @jve:decl-index=0:visual-constraint="26,27"

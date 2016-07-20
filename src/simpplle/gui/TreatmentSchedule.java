/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import simpplle.JSimpplle;
import simpplle.comcode.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.GridLayout;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This allows users to create, open, and modify treatment schedules. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class TreatmentSchedule extends JDialog {
  private simpplle.comcode.TreatmentSchedule    schedule;

  private TreatmentApplication treatment;
  private Vector               apps;
  private int                  appsIndex;
  private MyInteger[]          allTimeSteps;
  private MyInteger            currentTimeStep;
  private boolean              noTimeStepCBAction=false;
  private boolean              nextTreatmentCBInit=false;


  private boolean focusLost;
  private boolean inInit;

  JPanel mainPanel = new JPanel();
  JMenuBar menuBar = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileQuit = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileUnload = new JMenuItem();
  JList htGrpDropList = new JList();
  JList htGrpSourceList = new JList();
  JLabel htGrpLabel = new JLabel();
  JPanel htButtonPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel centerPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel listPanel = new JPanel();
  JButton appendButton = new JButton();
  JButton insertButton = new JButton();
  BorderLayout borderLayout5 = new BorderLayout();
  JLabel speciesLabel = new JLabel();
  JList speciesDropList = new JList();
  JList speciesSourceList = new JList();
  JPanel speciesButtonPanel = new JPanel();
  JButton speciesInsertPB = new JButton();
  JButton speciesAppendPB = new JButton();
  JLabel sizeClassLabel = new JLabel();
  JList sizeClassSourceList = new JList();
  JList sizeClassDropList = new JList();
  JPanel sizeClassPBPanel = new JPanel();
  JButton sizeClassInsertPB = new JButton();
  JButton sizeClassAppendPB = new JButton();
  GridLayout gridLayout4 = new GridLayout();
  JPanel southListPanel = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JLabel densityLabel = new JLabel();
  JLabel processLabel = new JLabel();
  JList processDropList = new JList();
  JList processSourceList = new JList();
  JList densityDropList = new JList();
  JList densitySourceList = new JList();
  JPanel densityPBPanel = new JPanel();
  JPanel processPBPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout6 = new GridLayout();
  JButton densityAppendPB = new JButton();
  JButton densityInsertPB = new JButton();
  JButton processAppendPB = new JButton();
  JButton processInsertPB = new JButton();
  JLabel timeStepLabel = new JLabel();
  JLabel treatmentLabel = new JLabel();
  JLabel acresLabel = new JLabel();
  JTextField acresEdit = new JTextField();
  JLabel selectionLabel = new JLabel();
  JPanel unitIdNorthPanel = new JPanel();
  JPanel selectionLabelPanel = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  TitledBorder possibleBorder;
  FlowLayout flowLayout10 = new FlowLayout();
  JButton speciesRemovePB = new JButton();
  JButton sizeClassRemovePB = new JButton();
  JButton densityRemovePB = new JButton();
  JButton processRemovePB = new JButton();
  JButton htGrpRemovePB = new JButton();
  JLabel treatmentValue = new JLabel();
  JPanel CBInnerPanel = new JPanel();
  GridLayout gridLayout8 = new GridLayout();
  JCheckBox enableIdAttributesCB = new JCheckBox();
  JCheckBox enableUnitSelectionCB = new JCheckBox();
  JPanel northPanel = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  JPanel CBPanel = new JPanel();
  JLabel UnitIdLabel = new JLabel();
  JTextField unitIdTextEdit = new JTextField();
  JButton unitIdAddPB = new JButton();
  JButton unitIdRemovePB = new JButton();
  JPanel unitIdListPanel = new JPanel();
  BorderLayout borderLayout8 = new BorderLayout();
  JList unitIdChosenList = new JList();
  JLabel unitIdChosenLabel = new JLabel();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel attributeSelectionTab = new JPanel();
  BorderLayout borderLayout9 = new BorderLayout();
  JPanel unitSelectionTab = new JPanel();
  BorderLayout borderLayout10 = new BorderLayout();
  JPanel unitIdChosenPanel = new JPanel();
  FlowLayout flowLayout12 = new FlowLayout();
  JPanel generalTab = new JPanel();
  BorderLayout borderLayout11 = new BorderLayout();
  JPanel generalEditPanel = new JPanel();
  JLabel generalTimeStepLabel = new JLabel();
  JPanel topPanel = new JPanel();
  JButton nextPB = new JButton();
  JButton previousPB = new JButton();
  JMenuItem menuFileNew = new JMenuItem();
  JMenuItem menuFileCopy = new JMenuItem();
  JScrollPane htGrpSourceScroll = new JScrollPane();
  GridLayout gridLayout2 = new GridLayout();
  JScrollPane htGrpDropScroll = new JScrollPane();
  JScrollPane speciesSourceScroll = new JScrollPane();
  JScrollPane speciesDropScroll = new JScrollPane();
  GridLayout gridLayout3 = new GridLayout();
  JScrollPane sizeClassDropScroll = new JScrollPane();
  JScrollPane sizeClassSourceScroll = new JScrollPane();
  JScrollPane densitySourceScroll = new JScrollPane();
  JScrollPane densityDropScroll = new JScrollPane();
  JScrollPane processDropScroll = new JScrollPane();
  JScrollPane processSourceScroll = new JScrollPane();
  JPanel processProbTab = new JPanel();
  JPanel processProbNorthPanel = new JPanel();
  JLabel processProbSelectionLabel = new JLabel();
  FlowLayout flowLayout14 = new FlowLayout();
  BorderLayout borderLayout15 = new BorderLayout();
  JPanel processProbControlsPanel = new JPanel();
  FlowLayout flowLayout15 = new FlowLayout();
  JPanel processProbEntryPanel = new JPanel();
  BorderLayout borderLayout16 = new BorderLayout();
  JPanel processProbLabelPanel = new JPanel();
  JPanel processProbScrollPanel = new JPanel();
  FlowLayout flowLayout16 = new FlowLayout();
  FlowLayout flowLayout17 = new FlowLayout();
  JTextField processProbText = new JTextField();
  JLabel processProbLabel = new JLabel();
  JScrollPane processProbListScroll = new JScrollPane();
  JList processProbList = new JList();
  JPanel processProbButtonPanel = new JPanel();
  GridLayout gridLayout10 = new GridLayout();
  JButton processProbAppendPB = new JButton();
  JButton processProbInsertPB = new JButton();
  JPanel processProbChosenPanel = new JPanel();
  BorderLayout borderLayout17 = new BorderLayout();
  JLabel processProbChosenLabel = new JLabel();
  JScrollPane processProbChosenScroll = new JScrollPane();
  JList processProbChosenList = new JList();
  JButton processProbRemovePB = new JButton();
  JPanel processProbCenterPanel = new JPanel();
  BorderLayout borderLayout18 = new BorderLayout();
  FlowLayout flowLayout11 = new FlowLayout();
  JLabel generalNoteLabel1 = new JLabel();
  JPanel generalNotePanel = new JPanel();
  GridLayout gridLayout11 = new GridLayout();
  JLabel generalNoteLabel2 = new JLabel();
  JLabel generalNoteLabel3 = new JLabel();
  JComboBox roadStatusCB = new JComboBox();
  JCheckBox enableProcessProbCB = new JCheckBox();
  JMenuItem menuFileLoadProbData = new JMenuItem();
  JPanel generalCenterPanel = new JPanel();
  BorderLayout borderLayout19 = new BorderLayout();
  JPanel nextTreatmentPanel = new JPanel();
  JPanel nextTreatmentInnerPanel = new JPanel();
  FlowLayout flowLayout19 = new FlowLayout();
  GridLayout gridLayout12 = new GridLayout();
  JPanel NextTreatmentTimeStepPanel = new JPanel();
  JPanel nextTreatmentNamePanel = new JPanel();
  FlowLayout flowLayout21 = new FlowLayout();
  FlowLayout flowLayout22 = new FlowLayout();
  JLabel nextTreatmentLabel = new JLabel();
  JLabel nextTreatmentTimeStepLabel = new JLabel();
  JTextField nextTreatmentTimeStepText = new JTextField();
  JCheckBox roadStatusCheckBox = new JCheckBox();
  JPanel generalEditOuterPanel = new JPanel();
  JMenuItem menuFileRemoveTreatment = new JMenuItem();
  JPanel jPanel2 = new JPanel();
  GridLayout gridLayout13 = new GridLayout();
  JLabel unitNoteLabel1 = new JLabel();
  JLabel unitNoteLabel2 = new JLabel();
  JLabel unitNoteLabel3 = new JLabel();
  JMenuItem menuFileLoadUnitIdFile = new JMenuItem();
  FlowLayout flowLayout24 = new FlowLayout();
  JPanel attribLabelPanel = new JPanel();
  GridLayout gridLayout5 = new GridLayout();
  JPanel possibleChoicesPanel = new JPanel();
  GridLayout gridLayout14 = new GridLayout();
  JPanel controlsPanel = new JPanel();
  GridLayout gridLayout15 = new GridLayout();
  JPanel chosenItemsPanel = new JPanel();
  GridLayout gridLayout16 = new GridLayout();
  JPanel removeItemPBPanel = new JPanel();
  GridLayout gridLayout17 = new GridLayout();
  TitledBorder chosenBorder;
  TitledBorder controlsBorder;
  JScrollPane unitIdScroll = new JScrollPane();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel infoPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel infoValuePanel = new JPanel();
  JPanel infoLabelPanel = new JPanel();
  GridLayout gridLayout18 = new GridLayout();
  GridLayout gridLayout19 = new GridLayout();
  TitledBorder followUpBorder;
  JCheckBox followUpTreatmentCB = new JCheckBox();
  JPanel preventReTreatmentPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JCheckBox preventReTreatmentCB = new JCheckBox();
  JTextField preventReTreatmentNumStepsText = new JTextField();
  JLabel preventNote = new JLabel();
  JMenu menuUtility = new JMenu();
  JMenuItem menuUtilityViewTreatments = new JMenuItem();
  JPanel treatmentTitlePanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JPanel generalEditFlowPanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  JPanel generalValuePanel = new JPanel();
  JPanel generalLabelPanel = new JPanel();
  GridLayout gridLayout7 = new GridLayout();
  GridLayout gridLayout20 = new GridLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel timeStepValuePanel = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  JLabel jLabel1 = new JLabel();
  JLabel timeStepValue = new JLabel();
  JButton timeStepPB = new JButton();
  JComboBox timeStepCB = new JComboBox();
  JMenuItem menuFileOldFormat = new JMenuItem();
  private JComboBox nextTreatmentCB = new JComboBox();
  private final JPanel panel = new JPanel();
  private final JPanel aggregateAcresPanel = new JPanel();
  private final JPanel minAggregateAcresPanel = new JPanel();
  private final JPanel maxAggregateAcresPanel = new JPanel();
  private final JLabel minAggregateAcresLabel = new JLabel();
  private final JLabel maxAggregateAcresLabel = new JLabel();
  private final JNumberTextField minAggregateAcres = new JNumberTextField();
  private final JNumberTextField maxAggregateAcres = new JNumberTextField();
  private final JPanel anyExceptPanel = new JPanel();
  private final JCheckBox ecologicalGroupingCheckBox = new JCheckBox();
  private final JCheckBox speciesCheckBox = new JCheckBox();
  private final JCheckBox sizeClassCheckBox = new JCheckBox();
  private final JCheckBox densityCheckBox = new JCheckBox();
  private final JCheckBox processesCheckBox = new JCheckBox();
  private final JPanel specialAreaPanel = new JPanel();
  private final JPanel ownershipPanel = new JPanel();
  private final JScrollPane specialAreaScroll = new JScrollPane();
  private final JScrollPane ownershipScroll = new JScrollPane();
  private final JList specialAreaList = new JList();
  private final JList ownershipList = new JList();
  private final JLabel specialAreaLabel = new JLabel();
  private final JLabel ownershipLabel = new JLabel();
  public TreatmentSchedule(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public TreatmentSchedule() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {
    possibleBorder = new TitledBorder("");
    chosenBorder = new TitledBorder("");
    controlsBorder = new TitledBorder("");
    followUpBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Follow-Up Treatment");
    mainPanel.setLayout(borderLayout1);
    this.setResizable(false);
    this.setJMenuBar(menuBar);
    menuFile.setText("File");
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileOpen_actionPerformed(e);
      }
    });
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileQuit_actionPerformed(e);
      }
    });
    menuFileSave.setEnabled(false);
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuFileSaveAs.setEnabled(false);
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSaveAs_actionPerformed(e);
      }
    });
    menuFileUnload.setToolTipText("Unload the currently loaded schedule");
    menuFileUnload.setEnabled(false);
    menuFileUnload.setText("Unload Current Schedule ...");
    menuFileUnload.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileUnload_actionPerformed(e);
      }
    });
    htGrpDropList.setBackground(Color.white);
    htGrpDropList.setToolTipText("");
    htGrpDropList.setSelectionBackground(Color.blue);
    htGrpDropList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        htGrpDropList_valueChanged(e);
      }
    });
    htGrpSourceList.setBackground(Color.white);
    htGrpSourceList.setToolTipText("");
    htGrpSourceList.setSelectionBackground(Color.blue);
    htGrpSourceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        htGrpSourceList_valueChanged(e);
      }
    });
    htGrpLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    htGrpLabel.setText("Ecological Grouping");
    centerPanel.setLayout(borderLayout2);
    listPanel.setLayout(borderLayout6);
    htButtonPanel.setLayout(gridLayout2);
    appendButton.setEnabled(false);
    appendButton.setToolTipText("Append selection to chosen list");
    appendButton.setText("Append");
    appendButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        appendButton_actionPerformed(e);
      }
    });
    insertButton.setEnabled(false);
    insertButton.setToolTipText("Insert Selection above selected item in Chosen List");
    insertButton.setText("Insert");
    insertButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        insertButton_actionPerformed(e);
      }
    });
    speciesLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    speciesLabel.setText("Species");
    speciesSourceList.setBackground(Color.white);
    speciesSourceList.setSelectionBackground(Color.blue);
    speciesSourceList.setSelectionForeground(Color.white);
    speciesSourceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        speciesSourceList_valueChanged(e);
      }
    });
    speciesDropList.setBackground(Color.white);
    speciesDropList.setSelectionBackground(Color.blue);
    speciesDropList.setSelectionForeground(Color.white);
    speciesDropList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        speciesDropList_valueChanged(e);
      }
    });
    speciesInsertPB.setEnabled(false);
    speciesInsertPB.setToolTipText("Insert Selection above selected item in Chosen List");
    speciesInsertPB.setText("Insert");
    speciesInsertPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        speciesInsertPB_actionPerformed(e);
      }
    });
    speciesAppendPB.setEnabled(false);
    speciesAppendPB.setToolTipText("Append selection to chosen list");
    speciesAppendPB.setText("Append");
    speciesAppendPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        speciesAppendPB_actionPerformed(e);
      }
    });
    speciesButtonPanel.setLayout(gridLayout3);
    sizeClassLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    sizeClassLabel.setText("Size Class");
    sizeClassInsertPB.setEnabled(false);
    sizeClassInsertPB.setToolTipText("Insert Selection above selected item in Chosen List");
    sizeClassInsertPB.setText("Insert");
    sizeClassInsertPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        sizeClassInsertPB_actionPerformed(e);
      }
    });
    sizeClassAppendPB.setEnabled(false);
    sizeClassAppendPB.setToolTipText("Append selection to chosen list");
    sizeClassAppendPB.setText("Append");
    sizeClassAppendPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        sizeClassAppendPB_actionPerformed(e);
      }
    });
    sizeClassPBPanel.setLayout(gridLayout4);
    gridLayout4.setColumns(1);
    gridLayout4.setRows(2);
    gridLayout4.setVgap(5);
    sizeClassSourceList.setBackground(Color.white);
    sizeClassSourceList.setSelectionBackground(Color.blue);
    sizeClassSourceList.setSelectionForeground(Color.white);
    sizeClassSourceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        sizeClassSourceList_valueChanged(e);
      }
    });
    sizeClassDropList.setBackground(Color.white);
    sizeClassDropList.setSelectionBackground(Color.blue);
    sizeClassDropList.setSelectionForeground(Color.white);
    sizeClassDropList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        sizeClassDropList_valueChanged(e);
      }
    });
    southListPanel.setLayout(flowLayout24);
    densityLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    densityLabel.setText("Density");
    processLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    processLabel.setText("Processes");
    densityPBPanel.setLayout(gridLayout1);
    processPBPanel.setLayout(gridLayout6);
    gridLayout1.setColumns(1);
    gridLayout1.setRows(2);
    gridLayout1.setVgap(5);
    gridLayout6.setColumns(1);
    gridLayout6.setRows(2);
    gridLayout6.setVgap(5);
    densityAppendPB.setEnabled(false);
    densityAppendPB.setToolTipText("Append selection to chosen list");
    densityAppendPB.setText("Append");
    densityAppendPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        densityAppendPB_actionPerformed(e);
      }
    });
    densityInsertPB.setEnabled(false);
    densityInsertPB.setToolTipText("Insert Selection above selected item in Chosen List");
    densityInsertPB.setText("Insert");
    densityInsertPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        densityInsertPB_actionPerformed(e);
      }
    });
    processAppendPB.setEnabled(false);
    processAppendPB.setToolTipText("Append selection to chosen list");
    processAppendPB.setText("Append");
    processAppendPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processAppendPB_actionPerformed(e);
      }
    });
    processInsertPB.setEnabled(false);
    processInsertPB.setToolTipText("Insert Selection above selected item in Chosen List");
    processInsertPB.setText("Insert");
    processInsertPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processInsertPB_actionPerformed(e);
      }
    });
    densitySourceList.setBackground(Color.white);
    densitySourceList.setSelectionBackground(Color.blue);
    densitySourceList.setSelectionForeground(Color.white);
    densitySourceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        densitySourceList_valueChanged(e);
      }
    });
    densityDropList.setBackground(Color.white);
    densityDropList.setSelectionBackground(Color.blue);
    densityDropList.setSelectionForeground(Color.white);
    densityDropList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        densityDropList_valueChanged(e);
      }
    });
    processSourceList.setBackground(Color.white);
    processSourceList.setSelectionBackground(Color.blue);
    processSourceList.setSelectionForeground(Color.white);
    processSourceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        processSourceList_valueChanged(e);
      }
    });
    processDropList.setBackground(Color.white);
    processDropList.setSelectionBackground(Color.blue);
    processDropList.setSelectionForeground(Color.white);
    processDropList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        processDropList_valueChanged(e);
      }
    });
    listPanel.setBorder(BorderFactory.createEtchedBorder());
    timeStepLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    timeStepLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    timeStepLabel.setText("Time Step");
    treatmentLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    treatmentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    treatmentLabel.setText("Treatment");
    acresLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    acresLabel.setHorizontalAlignment(SwingConstants.LEFT);
    acresLabel.setText("Desired Acres");
    acresEdit.setBackground(Color.white);
    acresEdit.setSelectionColor(Color.blue);
    acresEdit.setColumns(6);
    acresEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        acresEdit_focusLost(e);
      }
    });
    acresEdit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        acresEdit_actionPerformed(e);
      }
    });
    borderLayout1.setVgap(15);
    selectionLabel.setFont(new java.awt.Font("Dialog", 0, 18));
    selectionLabel.setText("Identify Attributes for Evu Selection");
    selectionLabelPanel.setLayout(flowLayout9);
    selectionLabelPanel.setBorder(BorderFactory.createEtchedBorder());
    flowLayout9.setVgap(0);
    borderLayout6.setVgap(5);
    unitIdNorthPanel.setLayout(flowLayout10);
    speciesRemovePB.setEnabled(false);
    speciesRemovePB.setToolTipText("Remove the selected item in the chosen list");
    speciesRemovePB.setHorizontalTextPosition(SwingConstants.CENTER);
    speciesRemovePB.setText("Remove Item");
    speciesRemovePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        speciesRemovePB_actionPerformed(e);
      }
    });
    sizeClassRemovePB.setEnabled(false);
    sizeClassRemovePB.setToolTipText("Remove the selected item in the chosen list");
    sizeClassRemovePB.setText("Remove Item");
    sizeClassRemovePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        sizeClassRemovePB_actionPerformed(e);
      }
    });
    densityRemovePB.setEnabled(false);
    densityRemovePB.setToolTipText("Remove the selected item in the chosen list");
    densityRemovePB.setText("Remove Item");
    densityRemovePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        densityRemovePB_actionPerformed(e);
      }
    });
    processRemovePB.setEnabled(false);
    processRemovePB.setToolTipText("Remove the selected item in the chosen list");
    processRemovePB.setText("Remove Item");
    processRemovePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processRemovePB_actionPerformed(e);
      }
    });
    htGrpRemovePB.setEnabled(false);
    htGrpRemovePB.setToolTipText("Remove the selected item in the chosen list");
    htGrpRemovePB.setText("Remove Item");
    htGrpRemovePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        htGrpRemovePB_actionPerformed(e);
      }
    });
    treatmentValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    treatmentValue.setForeground(Color.blue);
    treatmentValue.setToolTipText("");
    treatmentValue.setText("ECOSYSTEM-MANAGEMENT-THIN-AND-UNDERBURN");
    CBInnerPanel.setLayout(gridLayout8);
    gridLayout8.setColumns(1);
    gridLayout8.setRows(4);
    enableIdAttributesCB.setNextFocusableComponent(enableUnitSelectionCB);
    enableIdAttributesCB.setText("Enable Identifying of Attributes for Unit Selection");
    enableIdAttributesCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        enableIdAttributesCB_itemStateChanged(e);
      }
    });
    enableUnitSelectionCB.setNextFocusableComponent(enableProcessProbCB);
    enableUnitSelectionCB.setText("Enable selection of specific units by id");
    enableUnitSelectionCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        enableUnitSelectionCB_itemStateChanged(e);
      }
    });
    northPanel.setLayout(borderLayout7);
    CBPanel.setLayout(flowLayout11);
    CBInnerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    UnitIdLabel.setText("Unit Id");
    unitIdTextEdit.setBackground(Color.white);
    unitIdTextEdit.setNextFocusableComponent(unitIdAddPB);
    unitIdTextEdit.setSelectionColor(Color.blue);
    unitIdTextEdit.setColumns(6);
    unitIdTextEdit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        unitIdTextEdit_actionPerformed(e);
      }
    });
    flowLayout10.setAlignment(FlowLayout.LEFT);
    unitIdAddPB.setText("Add");
    unitIdAddPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        unitIdAddPB_actionPerformed(e);
      }
    });
    unitIdRemovePB.setEnabled(false);
    unitIdRemovePB.setText("Remove Selected");
    unitIdRemovePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        unitIdRemovePB_actionPerformed(e);
      }
    });
    unitIdListPanel.setLayout(borderLayout8);
    unitIdChosenLabel.setFont(new java.awt.Font("Dialog", 1, 12));
    unitIdChosenLabel.setHorizontalAlignment(SwingConstants.CENTER);
    unitIdChosenLabel.setText("Chosen Units");
    unitIdChosenList.setBackground(Color.white);
    unitIdChosenList.setSelectionBackground(Color.blue);
    unitIdChosenList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        unitIdChosenList_valueChanged(e);
      }
    });
    attributeSelectionTab.setLayout(borderLayout9);
    unitSelectionTab.setLayout(borderLayout10);
    unitIdChosenPanel.setLayout(flowLayout12);
    flowLayout12.setAlignment(FlowLayout.LEFT);
    flowLayout12.setVgap(10);
    generalTab.setLayout(borderLayout11);
    generalEditPanel.setLayout(borderLayout3);
    generalTimeStepLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    generalTimeStepLabel.setHorizontalAlignment(SwingConstants.LEFT);
    generalTimeStepLabel.setText("Time Step");
    borderLayout11.setVgap(10);
    topPanel.setLayout(flowLayout1);
    nextPB.setEnabled(false);
    nextPB.setIcon(new ImageIcon(simpplle.gui.TreatmentSchedule.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(simpplle.gui.TreatmentSchedule.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    previousPB.setEnabled(false);
    previousPB.setIcon(new ImageIcon(simpplle.gui.TreatmentSchedule.class.getResource("images/prev.gif")));
    previousPB.setMargin(new Insets(0, 0, 0, 0));
    previousPB.setPressedIcon(new ImageIcon(simpplle.gui.TreatmentSchedule.class.getResource("images/prevg.gif")));
    previousPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        previousPB_actionPerformed(e);
      }
    });
    menuFileNew.setText("New Treatment");
    menuFileNew.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileNew_actionPerformed(e);
      }
    });
    menuFileCopy.setEnabled(false);
    menuFileCopy.setText("Copy Current Treatment");
    menuFileCopy.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileCopy_actionPerformed(e);
      }
    });
    gridLayout2.setRows(2);
    gridLayout2.setVgap(5);
    gridLayout3.setRows(2);
    gridLayout3.setVgap(5);
    processProbSelectionLabel.setFont(new java.awt.Font("Dialog", 0, 18));
    processProbSelectionLabel.setText("Identify Processes and Probabilities for Evu Selection");
    processProbNorthPanel.setLayout(flowLayout14);
    processProbNorthPanel.setBorder(BorderFactory.createEtchedBorder());
    processProbTab.setLayout(borderLayout15);
    processProbControlsPanel.setLayout(flowLayout15);
    processProbEntryPanel.setLayout(borderLayout16);
    processProbLabelPanel.setLayout(flowLayout16);
    processProbScrollPanel.setLayout(flowLayout17);
    processProbLabel.setText("Probability >=");
    processProbText.setBackground(Color.white);
    processProbText.setEnabled(false);
    processProbText.setNextFocusableComponent(processProbAppendPB);
    processProbText.setSelectionColor(Color.blue);
    processProbText.setColumns(4);
    processProbText.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        processProbText_focusLost(e);
      }
    });
    processProbText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processProbText_actionPerformed(e);
      }
    });
    processProbButtonPanel.setLayout(gridLayout10);
    gridLayout10.setRows(2);
    gridLayout10.setVgap(5);
    processProbAppendPB.setEnabled(false);
    processProbAppendPB.setNextFocusableComponent(processProbInsertPB);
    processProbAppendPB.setText("Append");
    processProbAppendPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processProbAppendPB_actionPerformed(e);
      }
    });
    processProbInsertPB.setEnabled(false);
    processProbInsertPB.setNextFocusableComponent(processProbChosenList);
    processProbInsertPB.setText("Insert");
    processProbInsertPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processProbInsertPB_actionPerformed(e);
      }
    });
    processProbEntryPanel.setBorder(BorderFactory.createEtchedBorder());
    processProbChosenPanel.setLayout(borderLayout17);
    processProbChosenLabel.setText("Chosen Processes and Probabilities");
    flowLayout15.setHgap(10);
    processProbRemovePB.setEnabled(false);
    processProbRemovePB.setNextFocusableComponent(processProbList);
    processProbRemovePB.setText("Remove Item");
    processProbRemovePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        processProbRemovePB_actionPerformed(e);
      }
    });
    processProbCenterPanel.setLayout(borderLayout18);
    borderLayout15.setVgap(5);
    flowLayout11.setAlignment(FlowLayout.LEFT);
    generalNoteLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
    generalNoteLabel1.setText("NOTE");
    generalNotePanel.setLayout(gridLayout11);
    gridLayout11.setRows(4);
    generalNoteLabel2.setText("      Probability Data file must be loaded before process probability " +
    "selection tab will be functional.");
    generalNoteLabel3.setText("      Probability Data file can be loaded via the file menu.");
    generalNotePanel.setBorder(BorderFactory.createRaisedBevelBorder());
    enableProcessProbCB.setText("Enable selection of process probability");
    enableProcessProbCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        enableProcessProbCB_itemStateChanged(e);
      }
    });
    roadStatusCB.setNextFocusableComponent(enableIdAttributesCB);
    roadStatusCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        roadStatusCB_itemStateChanged(e);
      }
    });
    menuFileLoadProbData.setText("Load Probability Data File");
    menuFileLoadProbData.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileLoadProbData_actionPerformed(e);
      }
    });
    processProbList.setBackground(Color.white);
    processProbList.setNextFocusableComponent(processProbText);
    processProbList.setSelectionBackground(Color.blue);
    processProbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    processProbList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        processProbList_valueChanged(e);
      }
    });
    processProbChosenList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        processProbChosenList_valueChanged(e);
      }
    });
    processProbChosenList.setBackground(Color.white);
    processProbChosenList.setNextFocusableComponent(processProbRemovePB);
    processProbChosenList.setSelectionBackground(Color.blue);
    generalCenterPanel.setLayout(borderLayout19);
    nextTreatmentPanel.setLayout(flowLayout19);
    nextTreatmentInnerPanel.setLayout(gridLayout12);
    gridLayout12.setRows(3);
    gridLayout12.setVgap(2);
    nextTreatmentNamePanel.setLayout(flowLayout21);
    NextTreatmentTimeStepPanel.setLayout(flowLayout22);
    nextTreatmentLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    nextTreatmentLabel.setText("Treatment       ");
    nextTreatmentTimeStepLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    nextTreatmentTimeStepLabel.setText("Time Steps Later");
    nextTreatmentTimeStepText.setBackground(Color.white);
    nextTreatmentTimeStepText.setEnabled(false);
    nextTreatmentTimeStepText.setSelectionColor(Color.blue);
    nextTreatmentTimeStepText.setColumns(4);
    nextTreatmentTimeStepText.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        nextTreatmentTimeStepText_focusLost(e);
      }
    });
    nextTreatmentTimeStepText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        nextTreatmentTimeStepText_actionPerformed(e);
      }
    });
    flowLayout19.setAlignment(FlowLayout.LEFT);
    flowLayout21.setAlignment(FlowLayout.LEFT);
    flowLayout21.setVgap(0);
    flowLayout22.setAlignment(FlowLayout.LEFT);
    flowLayout22.setVgap(0);
    nextTreatmentInnerPanel.setBorder(followUpBorder);
    nextTreatmentPanel.setEnabled(false);
    roadStatusCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    roadStatusCheckBox.setText("Road Status");
    roadStatusCheckBox.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        roadStatusCheckBox_itemStateChanged(e);
      }
    });
    borderLayout19.setVgap(5);
    generalEditOuterPanel.setLayout(new BoxLayout(generalEditOuterPanel, BoxLayout.Y_AXIS));
    menuFileRemoveTreatment.setEnabled(false);
    menuFileRemoveTreatment.setText("Remove Current Treatment");
    menuFileRemoveTreatment.setActionCommand("Remove Current Treatment ...");
    menuFileRemoveTreatment.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileRemoveTreatment_actionPerformed(e);
      }
    });
    jPanel2.setLayout(gridLayout13);
    jPanel2.setBorder(BorderFactory.createRaisedBevelBorder());
    unitNoteLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
    unitNoteLabel1.setText("NOTE");
    gridLayout13.setRows(3);
    unitNoteLabel2.setText("      Treatment of these units will always be attempted, regardless " +
    "of what \'Desired Acres\' is set to.");
    unitNoteLabel3.setText("      However, these units count towards the total treated acres.");
    menuFileLoadUnitIdFile.setText("Load Unit Id File");
    menuFileLoadUnitIdFile.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileLoadUnitIdFile_actionPerformed(e);
      }
    });
    attribLabelPanel.setLayout(gridLayout5);
    gridLayout5.setRows(5);
    gridLayout5.setVgap(60);
    possibleChoicesPanel.setLayout(gridLayout14);
    gridLayout14.setRows(5);
    controlsPanel.setLayout(gridLayout15);
    gridLayout15.setRows(5);
    gridLayout15.setVgap(15);
    chosenItemsPanel.setLayout(gridLayout16);
    gridLayout16.setRows(5);
    removeItemPBPanel.setLayout(gridLayout17);
    gridLayout17.setRows(5);
    gridLayout17.setVgap(45);
    possibleChoicesPanel.setBorder(possibleBorder);
    possibleChoicesPanel.setPreferredSize(new Dimension(180, 420));
    chosenItemsPanel.setBorder(chosenBorder);
    chosenItemsPanel.setPreferredSize(new Dimension(180, 420));
    possibleBorder.setTitle("Possible Choices");
    chosenBorder.setTitle("Chosen Items");
    controlsPanel.setBorder(controlsBorder);
    controlsBorder.setTitle("Controls");
    controlsBorder.setTitlePosition(1);
    flowLayout24.setAlignment(FlowLayout.LEFT);
    unitIdScroll.setPreferredSize(new Dimension(100, 250));
    processProbListScroll.setPreferredSize(new Dimension(170, 80));
    processProbChosenScroll.setPreferredSize(new Dimension(200, 80));
    infoPanel.setLayout(flowLayout2);
    infoLabelPanel.setLayout(gridLayout18);
    infoValuePanel.setLayout(gridLayout19);
    gridLayout18.setRows(2);
    gridLayout19.setRows(2);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    followUpBorder.setTitleFont(new java.awt.Font("Dialog", 1, 16));
    followUpTreatmentCB.setEnabled(true);
    followUpTreatmentCB.setSelected(false);
    followUpTreatmentCB.setText("Schedule Follow Up Treatment");
    followUpTreatmentCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        followUpTreatmentCB_actionPerformed(e);
      }
    });
    preventReTreatmentPanel.setLayout(flowLayout3);
    preventReTreatmentCB.setSelected(true);
    preventReTreatmentCB.setText("Prevent re-treatment of system chosen units in next (n) time steps " +
    "(1).  n =");
    preventReTreatmentCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        preventReTreatmentCB_itemStateChanged(e);
      }
    });
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(0);
    flowLayout3.setVgap(0);
    preventReTreatmentNumStepsText.setText("5");
    preventReTreatmentNumStepsText.setColumns(3);
    preventReTreatmentNumStepsText.setHorizontalAlignment(SwingConstants.RIGHT);
    preventReTreatmentNumStepsText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        preventReTreatmentNumStepsText_keyTyped(e);
      }
    });
    preventReTreatmentNumStepsText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        preventReTreatmentNumStepsText_focusLost(e);
      }
    });
    preventNote.setText("      (1) If checked system will not treat units again unless specified " +
    "in the unit-id tab.");
    menuUtility.setText("Utility");
    menuUtilityViewTreatments.setEnabled(false);
    menuUtilityViewTreatments.setText("View/Order Treatments");
    menuUtilityViewTreatments.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityViewTreatments_actionPerformed(e);
      }
    });
    treatmentTitlePanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    treatmentTitlePanel.setBorder(BorderFactory.createLoweredBevelBorder());
    generalEditFlowPanel.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    generalLabelPanel.setLayout(gridLayout7);
    gridLayout7.setRows(3);
    gridLayout7.setVgap(5);
    generalValuePanel.setLayout(gridLayout20);
    gridLayout20.setRows(3);
    gridLayout20.setVgap(5);
    borderLayout3.setHgap(5);
    borderLayout3.setVgap(5);
    timeStepValuePanel.setLayout(flowLayout6);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout6.setHgap(0);
    flowLayout6.setVgap(0);
    jLabel1.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel1.setText("   ");
    timeStepValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    timeStepValue.setText("1");
    timeStepPB.setFont(new java.awt.Font("Monospaced", 0, 12));
    timeStepPB.setToolTipText("");
    timeStepPB.setMargin(new Insets(0, 0, 0, 0));
    timeStepPB.setText("Change");
    timeStepPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        timeStepPB_actionPerformed(e);
      }
    });
    timeStepCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        timeStepCB_actionPerformed(e);
      }
    });
    menuFileOldFormat.setText("Import Old Format File");
    menuFileOldFormat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOldFormat_actionPerformed(e);
      }
    });
    nextTreatmentCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextTreatmentCB_actionPerformed(e);
      }
    });
    nextTreatmentInnerPanel.add(followUpTreatmentCB, null);
    menuBar.add(menuFile);
    menuBar.add(menuUtility);
    menuFile.add(menuFileNew);
    menuFile.add(menuFileCopy);
    menuFile.addSeparator();
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileLoadProbData);
    menuFile.add(menuFileLoadUnitIdFile);
    menuFile.addSeparator();
    menuFile.add(menuFileOldFormat);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileRemoveTreatment);
    menuFile.add(menuFileUnload);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(topPanel, BorderLayout.CENTER);
    topPanel.add(previousPB, null);
    topPanel.add(nextPB, null);
    topPanel.add(infoPanel, null);
    infoPanel.add(infoLabelPanel, null);
    infoLabelPanel.add(timeStepLabel, null);
    infoPanel.add(infoValuePanel, null);
    infoValuePanel.add(timeStepCB, null);
    mainPanel.add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.add(generalTab, "General");
    generalTab.add(generalNotePanel, BorderLayout.SOUTH);
    generalNotePanel.add(generalNoteLabel1, null);
    generalNotePanel.add(generalNoteLabel2, null);
    generalNotePanel.add(generalNoteLabel3, null);
    generalNotePanel.add(preventNote, null);
    generalTab.add(generalCenterPanel, BorderLayout.CENTER);
    generalCenterPanel.add(CBPanel, BorderLayout.NORTH);
    CBPanel.add(CBInnerPanel, null);
    CBInnerPanel.add(enableIdAttributesCB, null);
    CBInnerPanel.add(enableUnitSelectionCB, null);
    CBInnerPanel.add(enableProcessProbCB, null);
    CBInnerPanel.add(preventReTreatmentPanel, null);
    preventReTreatmentPanel.add(preventReTreatmentCB, null);
    preventReTreatmentPanel.add(preventReTreatmentNumStepsText, null);
    generalCenterPanel.add(nextTreatmentPanel, BorderLayout.CENTER);
    nextTreatmentPanel.add(nextTreatmentInnerPanel, null);
    nextTreatmentInnerPanel.add(nextTreatmentNamePanel, null);
    nextTreatmentNamePanel.add(nextTreatmentLabel, null);
    nextTreatmentNamePanel.add(nextTreatmentCB);
    nextTreatmentInnerPanel.add(NextTreatmentTimeStepPanel, null);
    NextTreatmentTimeStepPanel.add(nextTreatmentTimeStepLabel, null);
    NextTreatmentTimeStepPanel.add(nextTreatmentTimeStepText, null);
    generalTab.add(generalEditOuterPanel, BorderLayout.NORTH);
    generalEditOuterPanel.add(treatmentTitlePanel, null);
    treatmentTitlePanel.add(treatmentLabel, null);
    treatmentTitlePanel.add(treatmentValue, null);
    generalEditOuterPanel.add(generalEditFlowPanel, null);
    generalEditFlowPanel.add(generalEditPanel, null);
    generalEditPanel.add(generalLabelPanel,  BorderLayout.WEST);
    generalLabelPanel.add(generalTimeStepLabel, null);
    generalLabelPanel.add(acresLabel, null);
    generalLabelPanel.add(roadStatusCheckBox, null);
    generalEditPanel.add(generalValuePanel,  BorderLayout.EAST);
    generalValuePanel.add(timeStepValuePanel, null);
    timeStepValuePanel.add(timeStepValue, null);
    timeStepValuePanel.add(jLabel1, null);
    timeStepValuePanel.add(timeStepPB, null);
    generalValuePanel.add(acresEdit, null);
    generalValuePanel.add(roadStatusCB, null);

    generalEditFlowPanel.add(specialAreaPanel);
    specialAreaPanel.setLayout(new BorderLayout());
    
    specialAreaPanel.add(specialAreaScroll);
    
    specialAreaScroll.setViewportView(specialAreaList);
    specialAreaList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    specialAreaList.setToolTipText("Use Ctrl or Shift to select Multiple Items (or clear selection)");
    specialAreaList.addListSelectionListener(new SpecialAreaListListSelectionListener());
    
    specialAreaPanel.add(specialAreaLabel, BorderLayout.NORTH);
    specialAreaLabel.setText("Special Area");
    
    generalEditFlowPanel.add(ownershipPanel);
    ownershipPanel.setLayout(new BorderLayout());
    
    ownershipPanel.add(ownershipScroll);
    
    ownershipScroll.setViewportView(ownershipList);
    ownershipList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    ownershipList.setToolTipText("Use Ctrl or Shift to select Multiple Items (or clear selection)");
    ownershipList.addListSelectionListener(new OwnershipListListSelectionListener());
    
    ownershipPanel.add(ownershipLabel, BorderLayout.NORTH);
    ownershipLabel.setText("Ownership");
    tabbedPane.add(attributeSelectionTab, "Attribute Selection");
    tabbedPane.add(unitSelectionTab, "Unit Id Selection");
    unitSelectionTab.add(unitIdNorthPanel, BorderLayout.NORTH);
    unitIdNorthPanel.add(UnitIdLabel, null);
    unitIdNorthPanel.add(unitIdTextEdit, null);
    unitIdNorthPanel.add(unitIdAddPB, null);
    unitSelectionTab.add(unitIdChosenPanel, BorderLayout.CENTER);
    unitIdChosenPanel.add(unitIdListPanel, null);
    unitIdListPanel.add(unitIdChosenLabel, BorderLayout.NORTH);
    unitIdListPanel.add(unitIdScroll, BorderLayout.SOUTH);
    unitIdScroll.getViewport().add(unitIdChosenList, null);
    unitIdChosenPanel.add(unitIdRemovePB, null);
    unitSelectionTab.add(jPanel2, BorderLayout.SOUTH);
    jPanel2.add(unitNoteLabel1, null);
    jPanel2.add(unitNoteLabel2, null);
    jPanel2.add(unitNoteLabel3, null);
    tabbedPane.add(processProbTab, "Process Probability Selection");
    processProbTab.add(processProbNorthPanel, BorderLayout.NORTH);
    processProbNorthPanel.add(processProbSelectionLabel, null);
    processProbTab.add(processProbCenterPanel, BorderLayout.CENTER);
    processProbCenterPanel.add(processProbControlsPanel, BorderLayout.CENTER);
    processProbControlsPanel.add(processProbEntryPanel, null);
    processProbEntryPanel.add(processProbLabelPanel, BorderLayout.NORTH);
    processProbLabelPanel.add(processProbLabel, null);
    processProbLabelPanel.add(processProbText, null);
    processProbEntryPanel.add(processProbScrollPanel, BorderLayout.CENTER);
    processProbScrollPanel.add(processProbListScroll, null);
    processProbControlsPanel.add(processProbButtonPanel, null);
    processProbButtonPanel.add(processProbAppendPB, null);
    processProbButtonPanel.add(processProbInsertPB, null);
    processProbControlsPanel.add(processProbChosenPanel, null);
    processProbChosenPanel.add(processProbChosenLabel, BorderLayout.NORTH);
    processProbChosenPanel.add(processProbChosenScroll, BorderLayout.CENTER);
    processProbControlsPanel.add(processProbRemovePB, null);
    processProbChosenScroll.getViewport().add(processProbChosenList, null);
    processProbListScroll.getViewport().add(processProbList, null);
    attributeSelectionTab.add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(listPanel, BorderLayout.SOUTH);
    listPanel.add(southListPanel, BorderLayout.NORTH);
    southListPanel.add(attribLabelPanel, null);
    attribLabelPanel.add(htGrpLabel, null);
    attribLabelPanel.add(speciesLabel, null);
    attribLabelPanel.add(sizeClassLabel, null);
    attribLabelPanel.add(densityLabel, null);
    attribLabelPanel.add(processLabel, null);
    southListPanel.add(possibleChoicesPanel, null);
    possibleChoicesPanel.add(htGrpSourceScroll, null);
    possibleChoicesPanel.add(speciesSourceScroll, null);
    speciesSourceScroll.getViewport().add(speciesSourceList, null);
    htGrpSourceScroll.getViewport().add(htGrpSourceList, null);
    possibleChoicesPanel.add(sizeClassSourceScroll, null);
    sizeClassSourceScroll.getViewport().add(sizeClassSourceList, null);
    possibleChoicesPanel.add(densitySourceScroll, null);
    densitySourceScroll.getViewport().add(densitySourceList, null);
    possibleChoicesPanel.add(processSourceScroll, null);
    processSourceScroll.getViewport().add(processSourceList, null);
    southListPanel.add(controlsPanel, null);
    controlsPanel.add(htButtonPanel, null);
    htButtonPanel.add(appendButton, null);
    htButtonPanel.add(insertButton, null);
    controlsPanel.add(speciesButtonPanel, null);
    speciesButtonPanel.add(speciesAppendPB, null);
    speciesButtonPanel.add(speciesInsertPB, null);
    controlsPanel.add(sizeClassPBPanel, null);
    sizeClassPBPanel.add(sizeClassAppendPB, null);
    sizeClassPBPanel.add(sizeClassInsertPB, null);
    controlsPanel.add(densityPBPanel, null);
    densityPBPanel.add(densityAppendPB, null);
    densityPBPanel.add(densityInsertPB, null);
    controlsPanel.add(processPBPanel, null);
    processPBPanel.add(processAppendPB, null);
    processPBPanel.add(processInsertPB, null);
    southListPanel.add(chosenItemsPanel, null);
    chosenItemsPanel.add(htGrpDropScroll, null);
    chosenItemsPanel.add(speciesDropScroll, null);
    speciesDropScroll.getViewport().add(speciesDropList, null);
    htGrpDropScroll.getViewport().add(htGrpDropList, null);
    chosenItemsPanel.add(sizeClassDropScroll, null);
    chosenItemsPanel.add(densityDropScroll, null);
    chosenItemsPanel.add(processDropScroll, null);
    processDropScroll.getViewport().add(processDropList, null);
    densityDropScroll.getViewport().add(densityDropList, null);
    sizeClassDropScroll.getViewport().add(sizeClassDropList, null);
    southListPanel.add(removeItemPBPanel, null);
    removeItemPBPanel.add(htGrpRemovePB, null);
    removeItemPBPanel.add(speciesRemovePB, null);
    removeItemPBPanel.add(sizeClassRemovePB, null);
    removeItemPBPanel.add(densityRemovePB, null);
    removeItemPBPanel.add(processRemovePB, null);
    
    listPanel.add(panel, BorderLayout.SOUTH);
    final FlowLayout flowLayout = new FlowLayout();
    flowLayout.setAlignment(FlowLayout.LEFT);
    panel.setLayout(flowLayout);
    
    panel.add(aggregateAcresPanel);
    aggregateAcresPanel.setLayout(new GridLayout(0, 1));
    aggregateAcresPanel.setBorder(new TitledBorder(null, "Aggregate Acres", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    
    aggregateAcresPanel.add(minAggregateAcresPanel);
    
    minAggregateAcresPanel.add(minAggregateAcresLabel);
    minAggregateAcresLabel.setFont(new Font("", Font.PLAIN, 14));
    minAggregateAcresLabel.setText("Minimum");
    
    minAggregateAcresPanel.add(minAggregateAcres);
    minAggregateAcres.addFocusListener(new MinAggregateAcresFocusListener());
    minAggregateAcres.addActionListener(new MinAggregateAcresActionListener());
    minAggregateAcres.setColumns(10);
    
    aggregateAcresPanel.add(maxAggregateAcresPanel);
    
    maxAggregateAcresPanel.add(maxAggregateAcresLabel);
    maxAggregateAcresLabel.setFont(new Font("", Font.PLAIN, 14));
    maxAggregateAcresLabel.setText("Maximum");
    
    maxAggregateAcresPanel.add(maxAggregateAcres);
    maxAggregateAcres.addFocusListener(new MaxAggregateAcresFocusListener());
    maxAggregateAcres.addActionListener(new MaxAggregateAcresActionListener());
    maxAggregateAcres.setColumns(10);
    
    panel.add(anyExceptPanel);
    anyExceptPanel.setBorder(new TitledBorder(null, "Any Except Chosen", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    anyExceptPanel.setLayout(new GridLayout(0, 1));
    
    anyExceptPanel.add(ecologicalGroupingCheckBox);
    ecologicalGroupingCheckBox.addActionListener(new EcologicalGroupingCheckBoxActionListener());
    ecologicalGroupingCheckBox.setText("Ecological Grouping");
    
    anyExceptPanel.add(speciesCheckBox);
    speciesCheckBox.addActionListener(new SpeciesCheckBoxActionListener());
    speciesCheckBox.setText("Species");
    
    anyExceptPanel.add(sizeClassCheckBox);
    sizeClassCheckBox.addActionListener(new SizeClassCheckBoxActionListener());
    sizeClassCheckBox.setText("Size Class");
    
    anyExceptPanel.add(densityCheckBox);
    densityCheckBox.addActionListener(new DensityCheckBoxActionListener());
    densityCheckBox.setText("Density");
    
    anyExceptPanel.add(processesCheckBox);
    processesCheckBox.addActionListener(new ProcessesCheckBoxActionListener());
    processesCheckBox.setText("Processes");
    centerPanel.add(selectionLabelPanel, BorderLayout.NORTH);
    selectionLabelPanel.add(selectionLabel, null);
    menuUtility.add(menuUtilityViewTreatments);
    getContentPane().add(mainPanel);
  }

  private void initialize() {
    inInit  = true;
    Area area = Simpplle.getCurrentArea();

    doNewSchedule();

    focusLost = false;

    // Ownership & Special Area Lists
    {
      ArrayList values = area.getAllOwnership();
      DefaultListModel model = new DefaultListModel();
      ownershipList.setModel(model);

      for (int i = 0; i < values.size(); i++) {
        model.addElement(values.get(i));
      }
    }
    {
      ArrayList values = area.getAllSpecialArea();
      DefaultListModel model = new DefaultListModel();
      specialAreaList.setModel(model);

      for (int i = 0; i < values.size(); i++) {
        model.addElement(values.get(i));
      }
    }


    Roads.Status[] allRoadStatus = Roads.Status.values();
    for(int i=0;i<allRoadStatus.length;i++) {
      roadStatusCB.addItem(allRoadStatus[i]);
    }
    roadStatusCB.setSelectedIndex(0);

    updateDialog();
    inInit = false;
  }

  private void updateNextTreatmentList() {
    ArrayList treatList =  Treatment.getLegalTreatmentList();
    for (int i=0; i<treatList.size(); i++) {
      nextTreatmentCB.addItem((TreatmentType)treatList.get(i));
    }
  }
  private void setTabEnabled (String title, boolean state) {
    int index = tabbedPane.indexOfTab(title);
    tabbedPane.setEnabledAt(index,state);
  }

  private void setGeneralTabEnabled(boolean state) {
    setTabEnabled("General", state);
    acresEdit.setEnabled(state);
    timeStepPB.setEnabled(state);
    enableIdAttributesCB.setEnabled(state);
    enableUnitSelectionCB.setEnabled(state);
    enableProcessProbCB.setEnabled(state);
  }

  private void cancel() {
    setVisible(false);
    dispose();
  }
  private void refresh() { update(getGraphics()); }

  private void doNewSchedule() {
    nextTreatmentCBInit = true;
    updateNextTreatmentList();
    nextTreatmentCBInit = false;

    Area area = Simpplle.getCurrentArea();

    schedule  = area.getTreatmentSchedule();
    if (schedule == null) { schedule = area.createTreatmentSchedule(); }

    apps            = null;
    treatment       = null;
    currentTimeStep = null;
    allTimeSteps = schedule.getAllTimeSteps();
    if (allTimeSteps == null) { return; }
    updateAllTimeSteps();

    apps = schedule.getApplications(currentTimeStep);
  }

  private boolean isExistingTimeStep(MyInteger time) {
    if (allTimeSteps == null) { return false; }
    for (int i=0; i<allTimeSteps.length; i++) {
      if (allTimeSteps[i].equals(time)) { return true; }
    }
    return false;
  }

  private void updateAllTimeSteps() {
    int index=-1;
    allTimeSteps = schedule.getAllTimeSteps();
    if (allTimeSteps == null) { return; }

    noTimeStepCBAction = true;
    timeStepCB.removeAllItems();
    for (int i=0; i<allTimeSteps.length; i++) {
      timeStepCB.addItem(allTimeSteps[i]);
      if (allTimeSteps[i].equals(currentTimeStep)) {
        index = i;
      }
    }
    if (index == -1) { currentTimeStep = allTimeSteps[0]; }

    timeStepCB.setSelectedIndex(index);
    timeStepCB.setEnabled(true);
    noTimeStepCBAction = false;
  }

  private void updateDialog() {
    if (apps != null) {
      if (treatment == null) {
        appsIndex = 0;
        treatment = (TreatmentApplication)apps.elementAt(appsIndex);
      }
      updateDialogTreatmentsLoaded();
    }
    else {
      treatment = null;
      updateDialogNoTreatmentsLoaded();
    }
  }

  private void updateDialogTreatmentsLoaded() {
    // Menu Items
    menuFileCopy.setEnabled(true);
    menuFileSave.setEnabled((SystemKnowledge.getFile(SystemKnowledge.TREATMENT_SCHEDULE) != null));
    menuFileSaveAs.setEnabled(true);
    menuFileRemoveTreatment.setEnabled(true);
    menuFileUnload.setEnabled(true);
    menuUtilityViewTreatments.setEnabled(true);

    previousPB.setEnabled(true);
    nextPB.setEnabled(true);

    // Set values in attribute selection tab
    RegionalZone zone = Simpplle.getCurrentZone();
    htGrpSourceList.setListData(HabitatTypeGroup.getAllLoadedTypes());
    speciesSourceList.setListData(zone.getAllSpecies());
    sizeClassSourceList.setListData(zone.getAllSizeClass());
    densitySourceList.setListData(zone.getAllDensity());
    processSourceList.setListData(treatment.getValidProcesses());

    htGrpDropList.setListData(treatment.getHtGroups());
    speciesDropList.setListData(treatment.getSpecies());
    sizeClassDropList.setListData(treatment.getSizeClass());
    densityDropList.setListData(treatment.getDensity());
    processDropList.setListData(treatment.getPrevProcess());

    ecologicalGroupingCheckBox.setSelected(treatment.isNotHtGroups());
    speciesCheckBox.setSelected(treatment.isNotSpecies());
    sizeClassCheckBox.setSelected(treatment.isNotSizeClass());
    densityCheckBox.setSelected(treatment.isNotDensity());
    processesCheckBox.setSelected(treatment.isNotPrevProcess());

    minAggregateAcres.setValue(treatment.getMinAggregateAcres());
    maxAggregateAcres.setValue(treatment.getMaxAggregateAcres());

    // Set Main window and General Tab values
    treatmentValue.setText(treatment.getTreatmentType().toString());




    String time  = "";
    String acres = "";
    if (treatment.isTimeStepSet()) {
      time = Integer.toString(treatment.getTimeStep());
    }
    if (treatment.isAcresSet()) {
      NumberFormat nf = NumberFormat.getInstance();
      nf.setGroupingUsed(false);
      nf.setMaximumFractionDigits(0);

      acres = nf.format(treatment.getFloatAcres());
    }

    inInit = true;
    // Ownership
    ownershipList.clearSelection();
    ownershipList.setEnabled(true);
    ArrayList<String> ownershipValues = treatment.getOwnershipList();
    if (ownershipValues != null && ownershipValues.size() > 0) {

      DefaultListModel model =  (DefaultListModel)ownershipList.getModel();

      int[] indices = new int[ownershipValues.size()];
      int i=0;
      for (String ownership : ownershipValues) {
        indices[i++] = model.indexOf(ownership);
      }
      ownershipList.setSelectedIndices(indices);
    }

    // Special Area
    specialAreaList.clearSelection();
    specialAreaList.setEnabled(true);
    ArrayList<String> specialAreaValues = treatment.getSpecialAreaList();
    if (specialAreaValues != null && specialAreaValues.size() > 0) {

      DefaultListModel model =  (DefaultListModel)specialAreaList.getModel();

      int[] indices = new int[specialAreaValues.size()];
      int i=0;
      for (String specialArea : specialAreaValues) {
        indices[i++] = model.indexOf(specialArea);
      }
      specialAreaList.setSelectedIndices(indices);
    }
    inInit = false;

    timeStepValue.setText(time);
    acresEdit.setText(acres);

    roadStatusCB.setSelectedItem(Roads.Status.lookup(treatment.getRoadStatus()));

    roadStatusCheckBox.setSelected(treatment.useRoadStatus());

    enableIdAttributesCB.setSelected(treatment.useAttributes());
    enableUnitSelectionCB.setSelected(treatment.useUnits());
    enableProcessProbCB.setSelected(treatment.useProcessProb());

    // Set values in unit id selected tab
    unitIdChosenList.setListData(treatment.getUnitId());

    // Set Values in process probability tab
    processProbList.setListData(treatment.getValidProcesses());
    processProbChosenList.setListData(treatment.getProcessProbList());

    followUpTreatmentCB.setSelected(false);
    followUpTreatmentCB.setEnabled(true);
    nextTreatmentCB.setEnabled(true);
    time = Integer.toString(treatment.getWaitSteps());
    nextTreatmentTimeStepText.setText(time);

    // Set values for follow up treatment (if any)
    if (treatment.isNextTreatment()) {
      nextTreatmentCB.setSelectedItem(treatment.getNextTreatment());

      // Set values for Follow Up check Box.
      followUpTreatmentCB.setSelected(treatment.useFollowUpTreatment());
    }
    else {
      nextTreatmentCB.setSelectedIndex(0);
    }

    // Enabled Tabbed Panes as appropriate
    setGeneralTabEnabled(true);
    setTabEnabled("Attribute Selection",treatment.useAttributes());
    setTabEnabled("Unit Id Selection",treatment.useUnits());
    boolean useProcessProb = (treatment.useProcessProb() &&
                              schedule.isProbDataLoaded());
    setTabEnabled("Process Probability Selection",useProcessProb);

    // Enable appropriate items in general tab
    enableIdAttributesCB.setEnabled(true);
    enableUnitSelectionCB.setEnabled(true);
    enableProcessProbCB.setEnabled(schedule.isProbDataLoaded());

    acresEdit.setEnabled(true);
    timeStepPB.setEnabled(true);

    roadStatusCheckBox.setEnabled(true);
    roadStatusCB.setEnabled(treatment.useRoadStatus());

    // Enable and set Follow-Up Treatment if needed.
    boolean isNextTreatment = treatment.isNextTreatment();

    nextTreatmentLabel.setEnabled(isNextTreatment);
    nextTreatmentTimeStepLabel.setEnabled(isNextTreatment);
    nextTreatmentTimeStepText.setEnabled(isNextTreatment);

    int index = tabbedPane.indexOfTab("General");
    tabbedPane.setSelectedIndex(index);

    preventReTreatmentCB.setSelected(treatment.preventReTreatment());
    preventReTreatmentNumStepsText.setEnabled(treatment.preventReTreatment());
    preventReTreatmentNumStepsText.setText(
            Integer.toString(treatment.getPreventReTreatmentTimeSteps()));

    noTimeStepCBAction = true;
    for (int j=0; j<allTimeSteps.length; j++) {
      if (currentTimeStep.equals(allTimeSteps[j])) {
        timeStepCB.setSelectedIndex(j);
        break;
      }
    }
    noTimeStepCBAction = false;

    refresh();
  }

  private void updateDialogNoTreatmentsLoaded() {
    // Reset the tabbed pane
    setGeneralTabEnabled(false);
    setTabEnabled("Attribute Selection",false);
    setTabEnabled("Unit Id Selection",false);
    setTabEnabled("Process Probability Selection",false);

    // Disable stuff in General Tab
    acresEdit.setEnabled(false);
    timeStepPB.setEnabled(false);

    specialAreaList.clearSelection();
    specialAreaList.setEnabled(false);
    ownershipList.clearSelection();
    ownershipList.setEnabled(false);

    roadStatusCheckBox.setEnabled(false);
    roadStatusCB.setEnabled(false);
    enableIdAttributesCB.setEnabled(false);
    enableUnitSelectionCB.setEnabled(false);
    enableProcessProbCB.setEnabled(false);

    nextTreatmentCB.setEnabled(false);
    followUpTreatmentCB.setEnabled(false);
    nextTreatmentLabel.setEnabled(false);
    nextTreatmentTimeStepLabel.setEnabled(false);
    nextTreatmentTimeStepText.setEnabled(false);

    previousPB.setEnabled(false);
    nextPB.setEnabled(false);

    // Clear some text fields
    treatmentValue.setText("");
    timeStepValue.setText("");
    acresEdit.setText("");

    // Menu Items
    menuFileSave.setEnabled(false);
    menuFileSaveAs.setEnabled(false);
    menuFileCopy.setEnabled(false);
    menuFileRemoveTreatment.setEnabled(false);
    menuFileUnload.setEnabled(false);
    menuUtilityViewTreatments.setEnabled(false);

    int index = tabbedPane.indexOfTab("General");
    tabbedPane.setSelectedIndex(index);

    timeStepCB.setEnabled(false);
    timeStepCB.removeAllItems();

    refresh();
  }

  void menuFileOpen_actionPerformed(ActionEvent e) {
    if (SystemKnowledgeFiler.openFile(this,SystemKnowledge.TREATMENT_SCHEDULE,menuFileSave,null)) {
      doNewSchedule();
      updateDialog();
    }
    refresh();
  }

  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    File         outfile;
    MyFileFilter extFilter;
    String       title = "Select a Treatment schedule file.";

    extFilter = new MyFileFilter("treatment",
                                 "Treatment Schedule Files (*.treatment)");

    setCursor(Utility.getWaitCursor());

    outfile = Utility.getOpenFile(this,title,extFilter);
    if (outfile == null) { return; }

    try {
      Simpplle.getCurrentArea().readTreatmentSchedule(outfile);
      doNewSchedule();
      updateDialog();
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this, err.getMessage(),
                                    "Error Loading Treatment File",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());
    refresh();
  }
  void menuFileLoadProbData_actionPerformed(ActionEvent e) {
    File         outfile;
    MyFileFilter extFilter = new MyFileFilter("txt", "Text Files (*.txt)");
    String       title     = "Select a Process Probability file";

    setCursor(Utility.getWaitCursor());

    outfile = Utility.getOpenFile(this,title,extFilter);
    if (outfile != null) {
      schedule.readProbData(outfile);
    }
    setCursor(Utility.getNormalCursor());
    updateDialog();
  }

  void menuFileLoadUnitIdFile_actionPerformed(ActionEvent e) {
    File         outfile;
    MyFileFilter extFilter = new MyFileFilter("txt", "Text Files (*.txt)");
    String       title     = "Select a Unit Id file";

    setCursor(Utility.getWaitCursor());

    outfile = Utility.getOpenFile(this,title,extFilter);
    if (outfile == null) {
      setCursor(Utility.getNormalCursor());
      return;
    }

    try {
      schedule.readUnitIdFile(outfile);
    }
    catch (simpplle.comcode.SimpplleError err) {
      String msg = "Loading of Unit Id's was only partially " +
                   "successfull due to the following error:\n\n" +
                   err.getMessage();
      JOptionPane.showMessageDialog(this,msg,"Problems reading file",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());
    updateAllTimeSteps();
    if (apps == null) { apps = schedule.getApplications(currentTimeStep); }
    updateDialog();
  }

  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.TREATMENT_SCHEDULE);
    SystemKnowledgeFiler.saveFile(this, outfile,SystemKnowledge.TREATMENT_SCHEDULE,menuFileSave, null);
  }
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this,SystemKnowledge.TREATMENT_SCHEDULE,menuFileSave, null);
  }

  void menuFileQuit_actionPerformed(ActionEvent e) {
    cancel();
  }

  void menuFileRemoveTreatment_actionPerformed(ActionEvent e) {
    String msg = "This will Delete the Currently displayed\n" +
                 "treatment.\n\n" +
                 "Continue and delete the treatment?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Delete Treatment",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      schedule.removeApplication(treatment);
      if (apps.size() > 0) {
        appsIndex = ( (appsIndex-1) > 0 ) ? appsIndex - 1 : apps.size() - 1;
        treatment = (TreatmentApplication)apps.elementAt(appsIndex);
      }
      else {
        updateAllTimeSteps();
        treatment = null;
        apps = schedule.getApplications(currentTimeStep);
      }
      updateDialog();
    }
  }

  void menuFileUnload_actionPerformed(ActionEvent e) {
    String msg = "This will remove the currently loaded schedule\n" +
                 "from memory.  Do this if you do not wish to\n" +
                 "apply the treatments to the next simulation.\n\n" +
                 "Continue and remove the schedule?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Unload Schedule",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      Simpplle.getCurrentArea().removeTreatmentSchedule();
      doNewSchedule();
      updateDialog();
    }
  }

  void menuFileNew_actionPerformed(ActionEvent e) {
    Frame         theFrame = JSimpplle.getSimpplleMain();
    NewTreatment  dlg = new NewTreatment(theFrame,"Select a new Treatment",true);
    String        treatmentName;
    TreatmentType treatType;
    int                  timeStep;
    boolean              existingTimeStep=false;
    boolean              timeStepChange=false;

    dlg.setVisible(true);
    treatmentName = dlg.getSelection();
    if (treatmentName != null) {
      if (currentTimeStep == null) {
        timeStep = AskNumber.getInput("Enter a Time Step","Time Step",1);
        if (timeStep == -1) { return; }
        currentTimeStep = new MyInteger(timeStep);
        timeStepChange  = true;
      }

      existingTimeStep = isExistingTimeStep(currentTimeStep);

      treatType = TreatmentType.get(treatmentName);
      treatment = schedule.newTreatment(treatType,currentTimeStep.intValue());

      if (existingTimeStep == false) {
        updateAllTimeSteps();
      }
      if (timeStepChange) {
        apps = schedule.getApplications(currentTimeStep);
      }
      appsIndex = apps.indexOf(treatment);
      updateDialog();
    }
  }

  void menuFileCopy_actionPerformed(ActionEvent e) {
    int timeStep = AskNumber.getInput("Enter a Time Step","Time Step",currentTimeStep.intValue());
    if (timeStep == -1) { return; }

    boolean   timeStepChanged = false, existingTimeStep = false;
    MyInteger newTimeStep = new MyInteger(timeStep);

    timeStepChanged  = (currentTimeStep.equals(newTimeStep) == false);
    currentTimeStep  = newTimeStep;
    existingTimeStep = isExistingTimeStep(currentTimeStep);
    treatment        = schedule.copyApplication(treatment,timeStep);

    if (existingTimeStep == false) {
      updateAllTimeSteps();
    }
    if (timeStepChanged) {
      apps = schedule.getApplications(currentTimeStep);
    }
    appsIndex = apps.indexOf(treatment);
    updateDialog();
    refresh();
  }


  // -- General Tab --
  // -----------------

  void acresEdit_actionPerformed(ActionEvent e) {
    acresEdit.getNextFocusableComponent().requestFocus();
  }

  void acresEdit_focusLost(FocusEvent e) {
    float  acres;

    if (treatment == null) { return; }

    // This prevents multiple calls and error messages.
    if (focusLost) { return; }

    try {
      // This allows a user to enter nothing for acres,
      // causing a zero to appear as a result.
      if (acresEdit.getText().trim().equals("")) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(0);
        acresEdit.setText(nf.format(treatment.getFloatAcres()));
      }
      acres = Float.valueOf(acresEdit.getText()).floatValue();
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      JOptionPane.showMessageDialog(this,"Invalid value for acres.",
                                    "Invalid value", JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          acresEdit.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
    treatment.setAcres(acres);
  }

  void roadStatusCheckBox_itemStateChanged(ItemEvent e) {
    boolean isSelected = roadStatusCheckBox.isSelected();

    treatment.setUseRoadStatus(isSelected);
    roadStatusCB.setEnabled(isSelected);
  }

  void roadStatusCB_itemStateChanged(ItemEvent e) {
    if (inInit) { return; }
    Roads.Status item = (Roads.Status) e.getItem();

    if (item != null && treatment != null) {
      treatment.setRoadStatus(item);
    }
  }

  void enableIdAttributesCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      treatment.setUseAttributes(false);
      setTabEnabled("Attribute Selection",false);
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      treatment.setUseAttributes(true);
      setTabEnabled("Attribute Selection",true);
    }
  }

  void enableUnitSelectionCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      treatment.setUseUnits(false);
      setTabEnabled("Unit Id Selection",false);
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      treatment.setUseUnits(true);
      setTabEnabled("Unit Id Selection",true);
    }
  }

  void enableProcessProbCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      treatment.setUseProcessProb(false);
      setTabEnabled("Process Probability Selection",false);
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      treatment.setUseProcessProb(true);
      setTabEnabled("Process Probability Selection",true);
    }
  }

  void nextTreatmentTimeStepText_actionPerformed(ActionEvent e) {
    nextTreatmentTimeStepText.getNextFocusableComponent().requestFocus();
  }

  void nextTreatmentTimeStepText_focusLost(FocusEvent e) {
    int waitSteps;

    if (treatment == null) { return; }

    // This prevents multiple calls and error messages.
    if (focusLost) { return; }

    try {
      waitSteps = Integer.parseInt(nextTreatmentTimeStepText.getText());
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      String msg = "Time steps to wait should be a number";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          nextTreatmentTimeStepText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
    treatment.setWaitSteps(waitSteps);
  }

  void followUpTreatmentCB_actionPerformed(ActionEvent e) {
    treatment.setUseFollowUpTreatment(followUpTreatmentCB.isSelected());
  }

  // -- Attribute Tab --
  // -------------------

  // Habitat Type Group Related Events.
  void appendButton_actionPerformed(ActionEvent e) {
    Object[] selected = htGrpSourceList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.addHtGrp((HabitatTypeGroupType)selected[i]);
    }
    htGrpDropList.setListData(treatment.getHtGroups());
    htGrpSourceList.clearSelection();
  }

  void insertButton_actionPerformed(ActionEvent e) {
    HabitatTypeGroupType   insertItem;
    insertItem = (HabitatTypeGroupType) htGrpDropList.getSelectedValue();

    Object[] selected = htGrpSourceList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
    //for(int i=(selected.length-1);i>=0;i--) {
      treatment.insertHtGrp((HabitatTypeGroupType)selected[i],insertItem);
    }
    htGrpDropList.setListData(treatment.getHtGroups());
    htGrpSourceList.clearSelection();
    insertButton.setEnabled(false);
    htGrpRemovePB.setEnabled(false);
  }

  void htGrpRemovePB_actionPerformed(ActionEvent e) {
    Object[] selected = htGrpDropList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.removeHtGrp((HabitatTypeGroupType)selected[i]);
    }
    htGrpDropList.setListData(treatment.getHtGroups());
    insertButton.setEnabled(false);
    htGrpRemovePB.setEnabled(false);
  }


  void htGrpSourceList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (htGrpSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (htGrpDropList.isSelectionEmpty() == false);

    appendButton.setEnabled(sourceSelected);
    insertButton.setEnabled((sourceSelected && dropSelected));
  }

  void htGrpDropList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (htGrpSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (htGrpDropList.isSelectionEmpty() == false);

    insertButton.setEnabled((sourceSelected && dropSelected));
    htGrpRemovePB.setEnabled((dropSelected));
  }


  // Species Related Events.
  void speciesAppendPB_actionPerformed(ActionEvent e) {
    Object[] selected = speciesSourceList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.addSpecies((Species)selected[i]);
    }
    speciesDropList.setListData(treatment.getSpecies());
    speciesSourceList.clearSelection();
  }

  void speciesInsertPB_actionPerformed(ActionEvent e) {
    Object[] selected   = speciesSourceList.getSelectedValues();
    Species  insertItem = (Species) speciesDropList.getSelectedValue();

    if (selected == null) { return; }

    for(int i=(selected.length-1);i>=0;i--) {
      treatment.insertSpecies((Species)selected[i],insertItem);
    }
    speciesDropList.setListData(treatment.getSpecies());
    speciesSourceList.clearSelection();
    speciesInsertPB.setEnabled(false);
    speciesRemovePB.setEnabled(false);
  }

  void speciesRemovePB_actionPerformed(ActionEvent e) {
    Object[] selected = speciesDropList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.removeSpecies((Species)selected[i]);
    }
    speciesDropList.setListData(treatment.getSpecies());
    speciesInsertPB.setEnabled(false);
    speciesRemovePB.setEnabled(false);
  }

  void speciesSourceList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (speciesSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (speciesDropList.isSelectionEmpty() == false);

    speciesAppendPB.setEnabled(sourceSelected);
    speciesInsertPB.setEnabled((sourceSelected && dropSelected));
  }

  void speciesDropList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (speciesSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (speciesDropList.isSelectionEmpty() == false);

    speciesInsertPB.setEnabled((sourceSelected && dropSelected));
    speciesRemovePB.setEnabled((dropSelected));
  }

  // Size Class Related Events.
  void sizeClassAppendPB_actionPerformed(ActionEvent e) {
    Object[] selected = sizeClassSourceList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.addSizeClass((SizeClass)selected[i]);
    }
    sizeClassDropList.setListData(treatment.getSizeClass());
    sizeClassSourceList.clearSelection();
  }

  void sizeClassInsertPB_actionPerformed(ActionEvent e) {
    Object[]  selected   = sizeClassSourceList.getSelectedValues();
    SizeClass insertItem = (SizeClass) sizeClassDropList.getSelectedValue();

    if (selected == null) { return; }

    for(int i=(selected.length-1);i>=0;i--) {
      treatment.insertSizeClass((SizeClass)selected[i],insertItem);
    }
    sizeClassDropList.setListData(treatment.getSizeClass());
    sizeClassSourceList.clearSelection();
    sizeClassInsertPB.setEnabled(false);
    sizeClassRemovePB.setEnabled(false);
  }

  void sizeClassRemovePB_actionPerformed(ActionEvent e) {
    Object[] selected = sizeClassDropList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.removeSizeClass((SizeClass)selected[i]);
    }
    sizeClassDropList.setListData(treatment.getSizeClass());
    sizeClassInsertPB.setEnabled(false);
    sizeClassRemovePB.setEnabled(false);
  }

  void sizeClassSourceList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (sizeClassSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (sizeClassDropList.isSelectionEmpty() == false);

    sizeClassAppendPB.setEnabled(sourceSelected);
    sizeClassInsertPB.setEnabled((sourceSelected && dropSelected));
  }

  void sizeClassDropList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (sizeClassSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (sizeClassDropList.isSelectionEmpty() == false);

    sizeClassInsertPB.setEnabled((sourceSelected && dropSelected));
    sizeClassRemovePB.setEnabled((dropSelected));
  }

  // Density Related Events.
  void densityAppendPB_actionPerformed(ActionEvent e) {
    Object[] selected = densitySourceList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.addDensity((Density)selected[i]);
    }
    densityDropList.setListData(treatment.getDensity());
    densitySourceList.clearSelection();
  }

  void densityInsertPB_actionPerformed(ActionEvent e) {
    Object[] selected   = densitySourceList.getSelectedValues();
    Density  insertItem = (Density)densityDropList.getSelectedValue();

    if (selected == null) { return; }

    for(int i=(selected.length-1);i>=0;i--) {
      treatment.insertDensity((Density)selected[i],insertItem);
    }
    densityDropList.setListData(treatment.getDensity());
    densitySourceList.clearSelection();
    densityInsertPB.setEnabled(false);
    densityRemovePB.setEnabled(false);
  }

  void densityRemovePB_actionPerformed(ActionEvent e) {
    Object[] selected = densityDropList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.removeDensity((Density)selected[i]);
    }
    densityDropList.setListData(treatment.getDensity());
    densityInsertPB.setEnabled(false);
    densityRemovePB.setEnabled(false);
  }

  void densitySourceList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (densitySourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (densityDropList.isSelectionEmpty() == false);

    densityAppendPB.setEnabled(sourceSelected);
    densityInsertPB.setEnabled((sourceSelected && dropSelected));
  }

  void densityDropList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (densitySourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (densityDropList.isSelectionEmpty() == false);

    densityInsertPB.setEnabled((sourceSelected && dropSelected));
    densityRemovePB.setEnabled((dropSelected));
  }

  // Process Related Events.
  void processAppendPB_actionPerformed(ActionEvent e) {
    Object[] selected = processSourceList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.addPrevProcess((ProcessType)selected[i]);
    }
    processDropList.setListData(treatment.getPrevProcess());
    processSourceList.clearSelection();
  }

  void processInsertPB_actionPerformed(ActionEvent e) {
    Object[]    selected   = processSourceList.getSelectedValues();
    ProcessType insertItem = (ProcessType) processDropList.getSelectedValue();

    if (selected == null) { return; }

    for(int i=(selected.length-1);i>=0;i--) {
      treatment.insertPrevProcess((ProcessType)selected[i],insertItem);
    }
    processDropList.setListData(treatment.getPrevProcess());
    processSourceList.clearSelection();
    processInsertPB.setEnabled(false);
    processRemovePB.setEnabled(false);
  }

  void processRemovePB_actionPerformed(ActionEvent e) {
    Object[] selected = processDropList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.removePrevProcess((ProcessType)selected[i]);
    }
    processDropList.setListData(treatment.getPrevProcess());
    processInsertPB.setEnabled(false);
    processRemovePB.setEnabled(false);
  }

  void processSourceList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (processSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (processDropList.isSelectionEmpty() == false);

    processAppendPB.setEnabled(sourceSelected);
    processInsertPB.setEnabled((sourceSelected && dropSelected));
  }

  void processDropList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (processSourceList.isSelectionEmpty() == false);
    boolean dropSelected   = (processDropList.isSelectionEmpty() == false);

    processInsertPB.setEnabled((sourceSelected && dropSelected));
    processRemovePB.setEnabled((dropSelected));
  }

  // Previous and Next Application Events.
  void previousPB_actionPerformed(ActionEvent e) {
    appsIndex--;
    if (appsIndex < 0) { appsIndex = apps.size() - 1; }

    treatment = (TreatmentApplication)apps.elementAt(appsIndex);
    updateDialog();
  }

  void nextPB_actionPerformed(ActionEvent e) {
    appsIndex++;
    if (appsIndex == apps.size()) { appsIndex = 0; }

    treatment = (TreatmentApplication)apps.elementAt(appsIndex);
    updateDialog();
  }

  // Unit Id Selection
  private void addUnitId() {
    int unitId;
    Area area = Simpplle.getCurrentArea();

    try {
      unitId = Integer.parseInt(unitIdTextEdit.getText());
      if (area.isValidUnitId(unitId) == false) {
        JOptionPane.showMessageDialog(this,"Invalid Unit Id","Invalid Unit Id",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
      treatment.addUnitId(unitId);
      unitIdChosenList.setListData(treatment.getUnitId());
      unitIdTextEdit.setText("");
    }
    catch (NumberFormatException nfe) {
      String msg = "Please enter a valid unit Id.";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
  }
  void unitIdTextEdit_actionPerformed(ActionEvent e) {
    addUnitId();
  }

  void unitIdAddPB_actionPerformed(ActionEvent e) {
    addUnitId();
  }

  void unitIdChosenList_valueChanged(ListSelectionEvent e) {
    boolean selected = (unitIdChosenList.isSelectionEmpty() == false);

    unitIdRemovePB.setEnabled(selected);
  }

  void unitIdRemovePB_actionPerformed(ActionEvent e) {
    Object[] selected = unitIdChosenList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      treatment.removeUnitId((Integer)selected[i]);
    }
    unitIdChosenList.setListData(treatment.getUnitId());
    unitIdRemovePB.setEnabled(false);
  }

  // Process Probability Selection Events

  void processProbText_actionPerformed(ActionEvent e) {
    processProbText.getNextFocusableComponent().requestFocus();
  }

  // This function serves only to verify input.
  // Data is set in treatment when user appends or inserts
  void processProbText_focusLost(FocusEvent e) {
    int prob;

    if (processProbText.isEnabled() == false) {
      Runnable doRequestFocus = new Runnable() {
        public void run() { processProbList.requestFocus(); }
      };
    }

    if (treatment == null) { return; }

    // This prevents multiple calls and error messages.
    if (focusLost) { return; }

    try {
      prob = Integer.parseInt(processProbText.getText());
      if (prob < 0 || prob > 100) {
        throw new NumberFormatException();
      }
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      String msg = "Probability should be a number (0-100)";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          processProbText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
  }

  void processProbList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (processProbList.isSelectionEmpty() == false);
    boolean chosenSelected = (processProbChosenList.isSelectionEmpty() == false);
    String  processName, str;
    Integer prob;

    processProbText.setEnabled(sourceSelected);
    processProbAppendPB.setEnabled(sourceSelected);
    processProbInsertPB.setEnabled((sourceSelected && chosenSelected));

    if (sourceSelected) {
      processName = (String) processProbList.getSelectedValue();
      prob        = treatment.getProcessProb(processName);

      str  = (prob != null) ? prob.toString() : "";
      processProbText.setText(str);
    }
    else {
      processProbText.setText("");
    }
  }

  private Integer getProcessProb() {
    Integer prob;

    try {
      prob = Integer.valueOf(processProbText.getText());
      return prob;
    }
    catch (NumberFormatException nfe) {
      String msg = "Probability should be a number (0-100)";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }

  void processProbAppendPB_actionPerformed(ActionEvent e) {
    String  processName = (String) processProbList.getSelectedValue();
    Integer prob        = getProcessProb();

    if (processName == null || prob == null) { return; }

    treatment.addProcessProb(processName,prob);

    processProbChosenList.setListData(treatment.getProcessProbList());
    processProbList.clearSelection();
  }

  void processProbInsertPB_actionPerformed(ActionEvent e) {
    String  processName     = (String) processProbList.getSelectedValue();
    String  insertSelection = (String) processProbChosenList.getSelectedValue();
    Integer prob            = getProcessProb();
    String  insertItem;
    int     index;

    if (processName == null || insertSelection == null || prob == null) {
      return;
    }

    index      = insertSelection.indexOf(" >=");
    insertItem = insertSelection.substring(0,index);
    treatment.insertProcessProb(processName,prob,insertItem);

    processProbChosenList.setListData(treatment.getProcessProbList());
    processProbList.clearSelection();
    processProbInsertPB.setEnabled(false);
    processProbRemovePB.setEnabled(false);
  }

  void processProbChosenList_valueChanged(ListSelectionEvent e) {
    boolean sourceSelected = (processProbList.isSelectionEmpty() == false);
    boolean chosenSelected = (processProbChosenList.isSelectionEmpty() == false);

    processProbInsertPB.setEnabled((sourceSelected && chosenSelected));
    processProbRemovePB.setEnabled((chosenSelected));
  }

  void processProbRemovePB_actionPerformed(ActionEvent e) {
    Object[] selected = processProbChosenList.getSelectedValues();
    String   str, processName;
    int      index;

    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      str = (String)selected[i];
      index = str.indexOf(" >=");
      processName = str.substring(0,index);
      treatment.removeProcessProb(processName);
    }
    processProbChosenList.setListData(treatment.getProcessProbList());
    processProbInsertPB.setEnabled(false);
    processProbRemovePB.setEnabled(false);
  }

  void preventReTreatmentCB_itemStateChanged(ItemEvent e) {
    treatment.setPreventReTreatment(e.getStateChange() == ItemEvent.SELECTED);
    preventReTreatmentNumStepsText.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
  }

  void preventReTreatmentNumStepsText_focusLost(FocusEvent e) {
    if (preventReTreatmentNumStepsText.getText().length() == 0) {
      int ns = treatment.getPreventReTreatmentTimeSteps();
      preventReTreatmentNumStepsText.setText(Integer.toString(ns));
    }
  }

  private boolean digitTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
      return false;
    }
    return (key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE);
  }
  void preventReTreatmentNumStepsText_keyTyped(KeyEvent e) {
    if (digitTyped(e) == false) { return; }

    Runnable doTimeStepUpdate = new Runnable() {
      public void run() {
        if (preventReTreatmentNumStepsText.getText().length() > 0) {
          treatment.setPreventReTreatmentTimeSteps(
                 Integer.parseInt(preventReTreatmentNumStepsText.getText()));
        }
      }
    };
    SwingUtilities.invokeLater(doTimeStepUpdate);
  }

  void menuUtilityViewTreatments_actionPerformed(ActionEvent e) {
    String title = "View/Order treatments";

    TreatmentScheduleListViewer dlg =
      new TreatmentScheduleListViewer(JSimpplle.getSimpplleMain(),title,
                                      true,schedule,currentTimeStep);

    dlg.setVisible(true);
    if (dlg.getSelectedApp() != null) {
      treatment = dlg.getSelectedApp();
      if (treatment.getTimeStep() != currentTimeStep.intValue()) {
        currentTimeStep = new MyInteger(treatment.getTimeStep());
        apps = schedule.getApplications(currentTimeStep);
      }
    }
    appsIndex = apps.indexOf(treatment);
    updateDialog();
  }

  void timeStepPB_actionPerformed(ActionEvent e) {
    int timeStep = AskNumber.getInput("Enter a Time Step","Time Step",1);
    if (timeStep == -1) { return; }
    if (timeStep == treatment.getTimeStep()) { return; }
    MyInteger newTimeStep = new MyInteger(timeStep);

    schedule.removeApplication(treatment);
    treatment.setTimeStep(timeStep);
    schedule.addApplication(treatment);

    currentTimeStep = newTimeStep;
    if ((isExistingTimeStep(newTimeStep) == false) ||
        apps.size() == 0) {
      updateAllTimeSteps();
    }
    apps = schedule.getApplications(currentTimeStep);
    appsIndex = apps.indexOf(treatment);
    updateDialog();
  }

  void timeStepCB_actionPerformed(ActionEvent e) {
    if (inInit || noTimeStepCBAction) { return; }

    MyInteger newTimeStep = (MyInteger)timeStepCB.getSelectedItem();
    if (newTimeStep == null || newTimeStep.equals(currentTimeStep)) { return; }

    currentTimeStep = newTimeStep;
    apps            = schedule.getApplications(currentTimeStep);
    treatment       = null;
    updateDialog();
  }

  public void nextTreatmentCB_actionPerformed(ActionEvent e) {
    if (inInit || noTimeStepCBAction || nextTreatmentCBInit) { return; }

    treatment.setNextTreatment((TreatmentType)nextTreatmentCB.getSelectedItem());
  }
  private class MinAggregateAcresActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      minAggregateAcres_actionPerformed(e);
    }
  }
  private class MinAggregateAcresFocusListener extends FocusAdapter {
    public void focusLost(FocusEvent e) {
      minAggregateAcres_focusLost(e);
    }
  }
  private class MaxAggregateAcresActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      maxAggregateAcres_actionPerformed(e);
    }
  }
  private class MaxAggregateAcresFocusListener extends FocusAdapter {
    public void focusLost(FocusEvent e) {
      maxAggregateAcres_focusLost(e);
    }
  }
  private class EcologicalGroupingCheckBoxActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      ecologicalGroupingCheckBox_actionPerformed(e);
    }
  }
  private class SpeciesCheckBoxActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      speciesCheckBox_actionPerformed(e);
    }
  }
  private class SizeClassCheckBoxActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      sizeClassCheckBox_actionPerformed(e);
    }
  }
  private class DensityCheckBoxActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      densityCheckBox_actionPerformed(e);
    }
  }
  private class ProcessesCheckBoxActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      processesCheckBox_actionPerformed(e);
    }
  }
  private class SpecialAreaListListSelectionListener implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      specialAreaList_valueChanged(e);
    }
  }
  private class OwnershipListListSelectionListener implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      ownershipList_valueChanged(e);
    }
  }
  protected void minAggregateAcres_actionPerformed(ActionEvent e) {
    treatment.setMinAggregateAcres(minAggregateAcres.getValue());
  }
  protected void minAggregateAcres_focusLost(FocusEvent e) {
    treatment.setMinAggregateAcres(minAggregateAcres.getValue());
  }
  protected void maxAggregateAcres_actionPerformed(ActionEvent e) {
    treatment.setMaxAggregateAcres(maxAggregateAcres.getValue());
  }
  protected void maxAggregateAcres_focusLost(FocusEvent e) {
    treatment.setMaxAggregateAcres(maxAggregateAcres.getValue());
  }
  protected void ecologicalGroupingCheckBox_actionPerformed(ActionEvent e) {
    treatment.setNotHtGroups(ecologicalGroupingCheckBox.isSelected());
  }
  protected void speciesCheckBox_actionPerformed(ActionEvent e) {
    treatment.setNotSpecies(speciesCheckBox.isSelected());
  }
  protected void sizeClassCheckBox_actionPerformed(ActionEvent e) {
    treatment.setNotSizeClass(sizeClassCheckBox.isSelected());
  }
  protected void densityCheckBox_actionPerformed(ActionEvent e) {
    treatment.setNotDensity(densityCheckBox.isSelected());
  }
  protected void processesCheckBox_actionPerformed(ActionEvent e) {
    treatment.setNotPrevProcess(processesCheckBox.isSelected());
  }
  protected void specialAreaList_valueChanged(ListSelectionEvent e) {
    if (inInit || treatment == null) { return; }

    Object[] values = specialAreaList.getSelectedValues();

    if (values == null || values.length == 0) {
      treatment.clearSpecialArea();
      return;
    }

    treatment.clearSpecialArea();
    for (Object value : values) {
      String sa = (String)value;
      treatment.addSpecialArea(sa);
    }
  }
  protected void ownershipList_valueChanged(ListSelectionEvent e) {
    if (inInit || treatment == null) { return; }

    Object[] values = ownershipList.getSelectedValues();

    if (values == null || values.length == 0) {
      treatment.clearOwnership();
      return;
    }

    treatment.clearOwnership();
    for (Object value : values) {
      String owner = (String)value;
      treatment.addOwnership(owner);
    }
  }



}




package simpplle.gui;
import  simpplle.JSimpplle;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import simpplle.comcode.Simpplle;
import simpplle.comcode.*;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import com.borland.jbcl.layout.VerticalFlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.border.EtchedBorder;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This dialog allows the user to set various parameters that control how the simulation in performed.
 * The dialog titled "Set Simulation Parameters" and has methods to input # of simulations, time steps, sim method, invasive species logic, 
 * options such as yearly time steps, fire suppression, output options, discarding unnecessary simulation data, all states reports, and tracking species reports.
 * 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class SimParam extends JDialog {
  private simpplle.comcode.Simpplle comcode;
  private int numSteps = 5;
  private int numSims = 1;

  private boolean focusLost;
  private boolean fireSuppression;
  private boolean discountCost;
  private boolean trackOwnership;
  private boolean trackSpecialArea;
  private boolean yearlySteps;
  private File    outputFile;
  private File    allStatesRulesFile;
  private int     maxTimeSteps;
  private boolean doInvasiveSpecies=true;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonPanel = new JPanel();
  JButton runButton = new JButton();
  JButton cancelButton = new JButton();
  JMenuBar menuBar = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileQuit = new JMenuItem();
  JTextField numSimText = new JTextField();
  JTextField numStepText = new JTextField();
  JLabel numSimLabel = new JLabel();
  JLabel numStepLabel = new JLabel();
  JPanel southPanel = new JPanel();
  JCheckBox fireSuppCB = new JCheckBox();
  JPanel northPanel = new JPanel();
  JCheckBox discountCB = new JCheckBox();
  JPanel discountPanel = new JPanel();
  JTextField discountText = new JTextField();
  JPanel fireSuppCBPanel = new JPanel();
  JPanel specialAreaPanel = new JPanel();
  JPanel ownershipPanel = new JPanel();
  JCheckBox ownershipCB = new JCheckBox();
  JCheckBox specialAreaCB = new JCheckBox();
  JPanel outfilePanel = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  JTextField outfileText = new JTextField();
  JButton outfileButton = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();

  // This need to be down here so designer will work correctly.
  private SimpplleMain simpplleMain;
  JPanel yearlyStepPanel = new JPanel();
  JCheckBox yearlyStepCB = new JCheckBox();
  JPanel simMethodPanel = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  JComboBox simMethodCB = new JComboBox();
  JLabel simMethodLabel = new JLabel();
  JPanel numSimPanel = new JPanel();
  JPanel numStepPanel = new JPanel();
  FlowLayout flowLayout8 = new FlowLayout();
  FlowLayout flowLayout10 = new FlowLayout();
  JCheckBox databaseWriteCB = new JCheckBox();
  FlowLayout flowLayout11 = new FlowLayout(FlowLayout.LEFT);
  JPanel databaseWritePanel = new JPanel(flowLayout11);
  private JPanel invasiveSpeciesPanel = new JPanel();
  private FlowLayout flowLayout13 = new FlowLayout();
  private JPanel discardDataPanel = new JPanel();
  private JCheckBox discardDataCB = new JCheckBox();
  private FlowLayout flowLayout7 = new FlowLayout();
  private JPanel allStatesRulesFilePanel = new JPanel();
  private FlowLayout flowLayout12 = new FlowLayout();
  private JButton allStatesFilePB = new JButton();
  private JTextField allStatesFileText = new JTextField();
  private Border border1 = BorderFactory.createLineBorder(SystemColor.
    controlText, 2);
  private Border border2 = new TitledBorder(border1,
                                            "All States Report Rules File");
  private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  private JPanel timeStepsInMemoryPanel = new JPanel();
  private FlowLayout flowLayout14 = new FlowLayout();
  private JTextField tsInMemoryText = new JTextField();
  private JLabel tsInMemoryLabel = new JLabel();
  private JPanel jPanel1 = new JPanel();
  private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  private Border border3 = BorderFactory.createLineBorder(SystemColor.
    controlText, 2);
  private Border border4 = new TitledBorder(border3, "Memory Saving Options");
  private JPanel allStatesPanel = new JPanel();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JPanel allStatesCBPanel = new JPanel();
  private JCheckBox allStatesCB = new JCheckBox();
  private FlowLayout flowLayout15 = new FlowLayout();
  private Border border5 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
    Color.white, new Color(148, 145, 140));
  private Border border6 = new TitledBorder(border5, "All States Report");
  private JPanel outputOptionsPanel = new JPanel();
  private Border border7 = BorderFactory.createLineBorder(SystemColor.
    controlText, 2);
  private Border border8 = new TitledBorder(border7, "Results Output Options");
  private BorderLayout borderLayout4 = new BorderLayout();
  private JPanel outputOptionsCBPanel = new JPanel();
  private JCheckBox gisUpdateSpreadCB = new JCheckBox();
  private JLabel gisUpdateSpreadLabel = new JLabel();
  private FlowLayout flowLayout16 = new FlowLayout();
  private JComboBox invasiveSpeciesCB = new JComboBox();
  private JLabel invasiveSpeciesLabel = new JLabel();
  private JPanel optionsPanel = new JPanel();
  private GridLayout gridLayout1 = new GridLayout();
  private BorderLayout borderLayout5 = new BorderLayout();
  private BorderLayout borderLayout6 = new BorderLayout();
  private BorderLayout borderLayout7 = new BorderLayout();
  private BorderLayout borderLayout8 = new BorderLayout();
  private BorderLayout borderLayout9 = new BorderLayout();
  private JPanel optionsOuterPanel = new JPanel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private Border border9 = BorderFactory.createLineBorder(SystemColor.
    controlText, 2);
  private Border border10 = new TitledBorder(border9, "Options");
  private FlowLayout flowLayout2 = new FlowLayout();
  private JPanel trackingSpeciesPanel = new JPanel();
  private BorderLayout borderLayout10 = new BorderLayout();
  private JPanel trackSpeciesCBPanel = new JPanel();
  private JPanel trackSpeciesCategoryButtonPanel = new JPanel();
  private FlowLayout flowLayout3 = new FlowLayout();
  private FlowLayout flowLayout4 = new FlowLayout();
  private JCheckBox trackingSpeciesCB = new JCheckBox();
  private JButton trackingSpeciesCategoryPB = new JButton();
  private Border border11 = BorderFactory.createLineBorder(Color.black, 2);
  private Border border12 = new TitledBorder(border11,
                                             "Tracking Species Report");
  private final JPanel panel = new JPanel();
  private final JPanel panel_1 = new JPanel();
  private final JCheckBox writeAccessFilesCB = new JCheckBox();
  public SimParam(SimpplleMain frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    simpplleMain = frame;
    initialize();
  }

  public SimParam() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    border12 = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
                                "Tracking Species Report");
    border10 = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
                                "Options");
    border8 = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
                               "Results Output Options");
    border2 = new TitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.
      RAISED, Color.white, new Color(148, 145, 140)), "Rules File (optional)");
    border6 = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
                               "All States Report");
    mainPanel.setLayout(borderLayout1);
    runButton.setNextFocusableComponent(cancelButton);
    runButton.setText("Run Simulation");
    runButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        runButton_actionPerformed(e);
      }
    });
    cancelButton.setMaximumSize(new Dimension(119, 27));
    cancelButton.setMinimumSize(new Dimension(119, 27));
    cancelButton.setPreferredSize(new Dimension(119, 27));
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });
    menuFile.setText("File");
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileQuit_actionPerformed(e);
      }
    });
    buttonPanel.setAlignmentX((float) 0.0);
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    numSimText.setBackground(Color.white);
    numSimText.setNextFocusableComponent(numStepText);
    numSimText.setSelectionColor(Color.blue);
    numSimText.setText("1");
    numSimText.setColumns(4);
    numSimText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        numSimText_actionPerformed(e);
      }
    });
    numSimText.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        numSimText_focusLost(e);
      }
    });
    numStepText.setBackground(Color.white);
    numStepText.setNextFocusableComponent(fireSuppCB);
    numStepText.setSelectionColor(Color.blue);
    numStepText.setText("5");
    numStepText.setColumns(4);
    numStepText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        numStepText_actionPerformed(e);
      }
    });
    numStepText.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        numStepText_focusLost(e);
      }
    });
    numSimLabel.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    numSimLabel.setLabelFor(numSimText);
    numSimLabel.setText("Number of Simulations ");
    numStepLabel.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    numStepLabel.setLabelFor(numStepText);
    numStepLabel.setText("Number of Time Steps  ");
    southPanel.setLayout(borderLayout2);
    mainPanel.setAlignmentX((float) 0.0);
    this.setTitle("Set Simulation Parameters");
    this.setJMenuBar(menuBar);
    fireSuppCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    fireSuppCB.setNextFocusableComponent(discountCB);
    fireSuppCB.setText("Fire Suppression");
    fireSuppCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        fireSuppCB_itemStateChanged(e);
      }
    });
    northPanel.setLayout(verticalFlowLayout1);
    discountCB.setEnabled(false);
    discountCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    discountCB.setNextFocusableComponent(discountText);
    discountCB.setText("Discounted Cost");
    discountCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        discountCB_itemStateChanged(e);
      }
    });
    discountText.setBackground(Color.white);
    discountText.setEnabled(false);
    discountText.setNextFocusableComponent(ownershipCB);
    discountText.setSelectionColor(Color.blue);
    discountText.setText("1.04");
    discountText.setColumns(4);
    discountPanel.setLayout(borderLayout7);
    fireSuppCBPanel.setLayout(borderLayout9);
    ownershipPanel.setLayout(borderLayout6);
    ownershipCB.setEnabled(false);
    ownershipCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    ownershipCB.setNextFocusableComponent(specialAreaCB);
    ownershipCB.setText("Track Ownership");
    ownershipCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        ownershipCB_itemStateChanged(e);
      }
    });
    specialAreaCB.setEnabled(false);
    specialAreaCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    specialAreaCB.setText("Track Special Area");
    specialAreaCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        specialAreaCB_itemStateChanged(e);
      }
    });
    specialAreaPanel.setLayout(borderLayout8);
    outfilePanel.setLayout(flowLayout6);
    outfileText.setBackground(Color.white);
    outfileText.setNextFocusableComponent(allStatesFilePB);
    outfileText.setEditable(false);
    outfileText.setSelectionColor(Color.blue);
    outfileText.setColumns(40);
    outfileButton.setMinimumSize(new Dimension(40, 27));
    outfileButton.setNextFocusableComponent(outfileText);
    outfileButton.setPreferredSize(new Dimension(40, 27));
    outfileButton.setToolTipText("Set the output files prefex.");
    outfileButton.setHorizontalTextPosition(SwingConstants.LEFT);
    outfileButton.setIcon(new ImageIcon(simpplle.gui.SimParam.class.getResource("images/save.gif")));
    outfileButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        outfileButton_actionPerformed(e);
      }
    });
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout6.setHgap(10);
    flowLayout6.setVgap(1);
    borderLayout1.setHgap(5);
    borderLayout1.setVgap(5);
    yearlyStepPanel.setLayout(borderLayout5);
    yearlyStepCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    yearlyStepCB.setText("Yearly Time Steps");
    yearlyStepCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        yearlyStepCB_itemStateChanged(e);
      }
    });
    simMethodPanel.setLayout(flowLayout9);
    simMethodLabel.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    simMethodLabel.setText("Simulation Method     ");
    flowLayout9.setAlignment(FlowLayout.LEFT);
    flowLayout9.setHgap(10);
    flowLayout9.setVgap(1);
    numSimPanel.setLayout(flowLayout8);
    numStepPanel.setLayout(flowLayout10);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    flowLayout8.setHgap(10);
    flowLayout8.setVgap(1);
    flowLayout10.setAlignment(FlowLayout.LEFT);
    flowLayout10.setHgap(10);
    flowLayout10.setVgap(1);
    databaseWriteCB.setEnabled(false);
    databaseWriteCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    databaseWriteCB.setToolTipText("This will write simulation data to a database");
    databaseWriteCB.setText("Use Database (saves memory, slows run)");
//    writeProbFilesCB.setEnabled(false);
//    writeProbFilesCB.setToolTipText("Write GIS Probability Files (slow)");
//    writeProbFilesCB.setText("Write GIS Probability Files (slow)");
    invasiveSpeciesPanel.setLayout(flowLayout13);
    flowLayout13.setAlignment(FlowLayout.LEFT);
    flowLayout13.setHgap(10);
    flowLayout13.setVgap(1);
    discardDataCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    discardDataCB.setText(
      "Discard Unnecessary Simulation Data, needed for extremely long term " +
      "simulations.");
    discardDataCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        discardDataCB_actionPerformed(e);
      }
    });
    discardDataPanel.setLayout(flowLayout7);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout7.setVgap(0);
    allStatesRulesFilePanel.setLayout(flowLayout12);
    flowLayout12.setAlignment(FlowLayout.LEFT);
    flowLayout12.setHgap(10);
    allStatesFilePB.setIcon(new ImageIcon(simpplle.gui.SimParam.class.getResource("images/save.gif")));
    allStatesFilePB.setMinimumSize(new Dimension(40, 27));
    allStatesFilePB.setNextFocusableComponent(runButton);
    allStatesFilePB.setPreferredSize(new Dimension(40, 27));
    allStatesFilePB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        allStatesFilePB_actionPerformed(e);
      }
    });
    allStatesFileText.setEditable(false);
    allStatesFileText.setBackground(Color.white);
    allStatesFileText.setSelectionColor(Color.blue);
    allStatesFileText.setColumns(40);
    allStatesRulesFilePanel.setBorder(border2);
    timeStepsInMemoryPanel.setLayout(flowLayout14);
    flowLayout14.setAlignment(FlowLayout.LEFT);
    flowLayout14.setVgap(0);
    tsInMemoryText.setEnabled(false);
    tsInMemoryText.setNextFocusableComponent(allStatesFilePB);
    tsInMemoryText.setText("10");
    tsInMemoryText.setColumns(4);
    tsInMemoryText.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        tsInMemoryText_focusLost(e);
      }
    });
    tsInMemoryText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tsInMemoryText_actionPerformed(e);
      }
    });
    tsInMemoryLabel.setEnabled(false);
    tsInMemoryLabel.setText("Time Steps kept in memory (minimum 10)");
    jPanel1.setLayout(verticalFlowLayout2);
    jPanel1.setBorder(border4);
    allStatesPanel.setLayout(borderLayout3);
    allStatesCB.setText(
      "Enable All States Report (needed if above discard option is checked or doing multiple runs)");
    allStatesCBPanel.setLayout(flowLayout15);
    flowLayout15.setAlignment(FlowLayout.LEFT);
    flowLayout15.setVgap(0);
    allStatesPanel.setBorder(border6);
    outputOptionsPanel.setBorder(border8);
    outputOptionsPanel.setLayout(borderLayout4);
    invasiveSpeciesLabel.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    invasiveSpeciesLabel.setText("Invasive Species Logic");
    optionsPanel.setLayout(gridLayout1);
    gridLayout1.setColumns(2);
    gridLayout1.setHgap(10);
    gridLayout1.setRows(3);
    optionsOuterPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(0);
    flowLayout1.setVgap(0);
    optionsOuterPanel.setBorder(border10);
    verticalFlowLayout2.setVgap(0);
    databaseWritePanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setVgap(0);
    verticalFlowLayout1.setVgap(2);
    trackingSpeciesPanel.setLayout(borderLayout10);
    trackSpeciesCBPanel.setLayout(flowLayout3);
    trackSpeciesCategoryButtonPanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setVgap(0);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setVgap(0);
    trackingSpeciesCB.setText(
      "Generate Tracking Species Report (needed if discarding data or multiple " +
      "runs)");
    trackingSpeciesCategoryPB.setText("Adjust Categories");
    trackingSpeciesCategoryPB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        trackingSpeciesCategoryPB_actionPerformed(e);
      }
    });
    trackingSpeciesPanel.setBorder(border12);
    menuBar.add(menuFile);
    menuFile.add(menuFileQuit);
    buttonPanel.add(runButton, null);
    buttonPanel.add(cancelButton, null);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    southPanel.add(buttonPanel, BorderLayout.NORTH);
    numSimPanel.add(numSimLabel, null);
    numSimPanel.add(numSimText, null);
    northPanel.add(numSimPanel, null);
    northPanel.add(numStepPanel, null);
    numStepPanel.add(numStepLabel, null);
    numStepPanel.add(numStepText, null);
    northPanel.add(simMethodPanel, null);
    simMethodPanel.add(simMethodLabel, null);
    simMethodPanel.add(simMethodCB, null);
    northPanel.add(invasiveSpeciesPanel);
    invasiveSpeciesPanel.add(invasiveSpeciesLabel);
    invasiveSpeciesPanel.add(invasiveSpeciesCB);
    northPanel.add(optionsOuterPanel);
    optionsOuterPanel.add(optionsPanel);

    optionsPanel.add(yearlyStepPanel);
    yearlyStepPanel.add(yearlyStepCB, java.awt.BorderLayout.CENTER);
    optionsPanel.add(ownershipPanel);
    ownershipPanel.add(ownershipCB, java.awt.BorderLayout.CENTER);
    optionsPanel.add(fireSuppCBPanel);
    fireSuppCBPanel.add(fireSuppCB, java.awt.BorderLayout.CENTER);
    optionsPanel.add(specialAreaPanel);
    specialAreaPanel.add(specialAreaCB, java.awt.BorderLayout.CENTER);
    optionsPanel.add(discountPanel);

    northPanel.add(outputOptionsPanel);
    outputOptionsPanel.add(outfilePanel, java.awt.BorderLayout.NORTH);
    outfilePanel.add(outfileButton, null);
    outfilePanel.add(outfileText, null);
    
    outputOptionsPanel.add(panel, BorderLayout.SOUTH);
    panel.setLayout(new GridLayout(2, 0));
    gisUpdateSpreadCB.setEnabled(false);
    gisUpdateSpreadCB.setText("GIS Update/Spread Files");
    gisUpdateSpreadCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gisUpdateSpreadCB_actionPerformed(e);
      }
    });
    outputOptionsCBPanel.setLayout(flowLayout16);
    panel.add(outputOptionsCBPanel);
    flowLayout16.setAlignment(FlowLayout.LEFT);
    flowLayout16.setVgap(0);
    outputOptionsCBPanel.add(gisUpdateSpreadCB);
    outputOptionsCBPanel.add(gisUpdateSpreadLabel);
    
    panel.add(panel_1);
    final FlowLayout flowLayout = new FlowLayout();
    flowLayout.setAlignment(FlowLayout.LEFT);
    panel_1.setLayout(flowLayout);
    
    panel_1.add(writeAccessFilesCB);
    writeAccessFilesCB.setEnabled(false);
    writeAccessFilesCB.setSelected(false);
    writeAccessFilesCB.addActionListener(new WriteAccessFilesCBActionListener());
    writeAccessFilesCB.setText("Write Data to Text Files (Suitable for import into Access or other Programs)");
    northPanel.add(jPanel1);
    jPanel1.add(databaseWritePanel);
    databaseWritePanel.add(databaseWriteCB, null);
    jPanel1.add(discardDataPanel);
    discardDataPanel.add(discardDataCB);
    jPanel1.add(timeStepsInMemoryPanel);
    timeStepsInMemoryPanel.add(tsInMemoryText);
    timeStepsInMemoryPanel.add(tsInMemoryLabel);
    northPanel.add(allStatesPanel);
    northPanel.add(trackingSpeciesPanel);
    trackSpeciesCBPanel.add(trackingSpeciesCB);
    trackSpeciesCategoryButtonPanel.add(trackingSpeciesCategoryPB);
    allStatesRulesFilePanel.add(allStatesFilePB);
    allStatesRulesFilePanel.add(allStatesFileText);
    this.getContentPane().add(mainPanel, java.awt.BorderLayout.WEST);
    allStatesPanel.add(allStatesCBPanel, java.awt.BorderLayout.NORTH);
    allStatesCBPanel.add(allStatesCB);
    allStatesPanel.add(allStatesRulesFilePanel, java.awt.BorderLayout.CENTER);
    discountPanel.add(discountText, java.awt.BorderLayout.CENTER);
    discountPanel.add(discountCB, java.awt.BorderLayout.WEST);
    trackingSpeciesPanel.add(trackSpeciesCategoryButtonPanel,
                             java.awt.BorderLayout.CENTER);
    trackingSpeciesPanel.add(trackSpeciesCBPanel, java.awt.BorderLayout.NORTH);
  }
/**
 * This initializes the simulation parameters.  Since most of the parameters need to be choosen in order to be used, the booleans governing them 
 * are set to false.  Simulation methods are stochastic, stand development(succession), and highest - the three probability methods used in OpenSimpplle.  
 * There are some numbers used by default... simulations = 1, time steps = 5, sim method = stochastic. invasive species logic = none.  These are set in 
 * Simulation.java.   
 * Tracking species is only to be used where there is logic for it.  These are Eastside Region 1 and Colorado Plateau (mesa verde).
 * Yearly time steps must be ussed for wyoming regions (grasslands) all others have the option to choose yearly or decade time steps.   
 */
  private void initialize() {
    comcode           = JSimpplle.getComcode();
    focusLost         = false;
    fireSuppression   = false;
    discountCost      = false;
    trackOwnership    = false;
    trackSpecialArea  = false;
    yearlySteps       = false;
    outputFile        = null;
    allStatesRulesFile = null;
    numSims           = 1;
    doInvasiveSpecies = true;

    maxTimeSteps = simpplle.comcode.Simulation.MAX_TIME_STEPS;

    simMethodCB.addItem("STOCHASTIC");
    simMethodCB.addItem("STAND DEVELOPMENT");
    simMethodCB.addItem("HIGHEST");
    simMethodCB.setSelectedIndex(0);

    Simulation.InvasiveKind[] kinds = Simulation.InvasiveKind.values();

    for (int i=0; i<kinds.length; i++) {
      if (kinds[i] == Simulation.InvasiveKind.MSU &&
          ((ExistingLandUnit.hasNumericAspect() == false) ||
           (InvasiveSpeciesLogicMSU.hasData() == false))) {
        continue;
      }
      if (kinds[i] == Simulation.InvasiveKind.MESA_VERDE_NP &&
          ((InvasiveSpeciesLogic.hasData() == false))) {
        continue;
      }
      invasiveSpeciesCB.addItem(kinds[i]);
    }
    invasiveSpeciesCB.setSelectedItem(Simulation.InvasiveKind.NONE);

    boolean isWyoming =  (RegionalZone.isWyoming());
    yearlyStepCB.setEnabled(!isWyoming);
    yearlyStepCB.setSelected(isWyoming);
  }
/**
 * Refreshes the dialog.  
 */
  public void refresh() {
    update(getGraphics());
  }
/**
 * If 'cancel' button is pressed, disposes the simulation parameter dialog.  
 */
  private void cancel () {
    setVisible(false);
    dispose();
  }
/**
 * Runs the simulation (if 'Run Simulation' button is pushed).  Basically it parses in the user set parameters and calls the runsimulation method from comcode Simpplle.java which then creates 
 * a new simulation instance with the passed parameters.   
 * @return
 */
  private boolean runSimulation() {
    int     numSimulations, numSteps;
    int     tsInMemory;
    float   discount;
    String  simulationMethod;

    try {
      numSimulations = Integer.parseInt(numSimText.getText());
      numSteps       = Integer.parseInt(numStepText.getText());
      tsInMemory     = Integer.parseInt(tsInMemoryText.getText());
      /* -- JDK 1.2 -- discount = Float.parseFloat(discountText.getText()); */
      if (discountCost) {
        discount = Float.valueOf(discountText.getText()).floatValue();
      }
      else {
        discount = 1.0f;
      }

      simulationMethod = (String) simMethodCB.getSelectedItem();
    }
    catch (NumberFormatException nfe) {
      String msg = "One of the following fields is invalid:" +
                   JSimpplle.endl +
                   "  - Number of Simulations" + JSimpplle.endl +
                   "  - Number of Time Steps" + JSimpplle.endl +
                   "  - Fire Suppression Cost Discount.";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (outputFile == null && numSimulations > 1) {
      JOptionPane.showMessageDialog(this,"An output file must be specified",
                                    "Missing Output filename",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    simpplleMain.setStatusMessage("Running Simulation ...");
    refresh();

    try {
      comcode.runSimulation(numSimulations,numSteps,fireSuppression,outputFile,
                          discount,trackSpecialArea,trackOwnership,yearlySteps,
                          simulationMethod,
                          databaseWriteCB.isSelected(),
                          writeAccessFilesCB.isSelected(), /*write access files*/
                          /*writeProbFilesCB.isSelected()*/true,
                          (Simulation.InvasiveKind)invasiveSpeciesCB.getSelectedItem(),
                          tsInMemory,allStatesRulesFile,discardDataCB.isSelected(),
                          allStatesCB.isSelected(),
                          trackingSpeciesCB.isSelected(),
                          gisUpdateSpreadCB.isSelected());
    }
    catch (simpplle.comcode.SimpplleError e) {
      JOptionPane.showMessageDialog(this,e.getError(),"Simulation Failed",
                                    JOptionPane.ERROR_MESSAGE);
      simpplleMain.clearStatusMessage();
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      
      MyErrorHandler errorHandler = new MyErrorHandler();
      errorHandler.handle(e);
      
      return false;
    }

    simpplleMain.clearStatusMessage();
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    JOptionPane.showMessageDialog(this,"Simulation Successful.",
                                    "Simulation Finished.",
                                    JOptionPane.INFORMATION_MESSAGE);
    return true;
  }

  // Event handlers
  // **************
/**
 * Handles the pushing of the "Run Simulation" button.  
 */
  void runButton_actionPerformed(ActionEvent e) {
    boolean success = runSimulation();

    if (success) {
      simpplleMain.enableSimulationControls();
    }
    else {
      return;
    }
    setVisible(false);
    dispose();
  }
/**
 * If 'Cancel'button is pressed will dispose of the Simulation Parameter dialog.  
 * @param e
 */
  void cancelButton_actionPerformed(ActionEvent e) {
    cancel();
  }
  /**
   * If 'Quit' menu item is selected will dispose of the Simulation Parameter dialog.  
   * @param e
   */
  void menuFileQuit_actionPerformed(ActionEvent e) {
    cancel();
  }
/**
 * Handles the losing of focus from the 'Number of Simulations" text field.  It parses the number of simulations.  
 * These must be between 1 - 100, as that is the limit of OpenSimpplle simulations.  
 * If spread Check box is selected, the spread files that show the origin and spread of the spreading disturbances will be generated.
 * If number of simulations is greater than 1 will enable the ownership and special area tracking check boxes.  
 * @param e loss of focus from 'Number of Simulations' text field.  
 */
  void numSimText_focusLost(FocusEvent e) {
    if (focusLost) { return; }
    try {
      numSims = Integer.parseInt(numSimText.getText());
      if (numSims < 1 || numSims > 100) {
        throw new NumberFormatException();
      }

      if (outputFile != null && gisUpdateSpreadCB.isSelected()) {
        String nFiles = Integer.toString(numSims * numSteps * 2);
        gisUpdateSpreadLabel.setText("(# of Files " + nFiles + ")");
      }
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      String msg = "Invalid number of simulations." +
                   JSimpplle.endl +
                   "Please enter a number between 1-100";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          numSimText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
    if (numSims > 1) {
      ownershipCB.setEnabled(true);
      specialAreaCB.setEnabled(true);
    }
    else {
      ownershipCB.setEnabled(false);
      specialAreaCB.setEnabled(false);
    }
  }
  /**
   * Handles the losing of focus from the 'Number of Time Steps" text field.  It parses the number of time steps.  
   * These can be set as yearly or decade.  
   * If spread Check box is selected, the spread files that show the origin and spread of the spreading disturbances will be generated.
   * @param e loss of focus from 'Number of Simulations' text field.  
   */
  void numStepText_focusLost(FocusEvent e) {
    if (focusLost) { return; }
    try {
      numSteps = Integer.parseInt(numStepText.getText());
      if (numSteps < 1 || numSteps > maxTimeSteps) {
        throw new NumberFormatException();
      }

      if (outputFile != null && gisUpdateSpreadCB.isSelected()) {
        String nFiles = Integer.toString(numSims * numSteps * 2);
        gisUpdateSpreadLabel.setText("(# of Files " + nFiles + ")");
      }
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      String msg = "Invalid number of time steps." + JSimpplle.endl +
                   "Please enter a number between 1-" +
                   maxTimeSteps;
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          numStepText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
  }

  void numSimText_actionPerformed(ActionEvent e) {
    numSimText.getNextFocusableComponent().requestFocus();
  }

  void numStepText_actionPerformed(ActionEvent e) {
     numStepText.getNextFocusableComponent().requestFocus();
  }
/**
 *Handles the check box which allows for yearly or decade time steps.  Yearly time steps are hard coded to be used for Wyoming zone, all others can choose yearly or decade.  
 *
 * @param e
 */
  void yearlyStepCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      yearlySteps = false;
      maxTimeSteps = simpplle.comcode.Simulation.MAX_TIME_STEPS;
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      yearlySteps = true;
      maxTimeSteps = simpplle.comcode.Simulation.MAX_TIME_STEPS * 10;
    }
  }
/**
 * Handles check box for fire suppression.  Simulations without fire suppresion are most commonly used with long term simulations to create an interaction between 
 * fire, insect, and disease disturbances.  
 * @param e fire suppression selected or deselected (false by default - selecting makes fire suppression true). 
 */
  void fireSuppCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      fireSuppression = false;
      discountCB.setEnabled(false);
      discountText.setEnabled(false);
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      fireSuppression = true;
      discountCB.setEnabled(true);
      discountText.setEnabled(discountCost);
    }
  }

  void discountCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      discountCost = false;
      discountText.setEnabled(false);
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      discountCost = true;
      discountText.setEnabled(true);
    }
  }

  void ownershipCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      trackOwnership = false;
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      trackOwnership = true;
    }
  }
/**
 * Handles the Track Special Area check box.  
 * @param e
 */
  void specialAreaCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      trackSpecialArea = false;
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      trackSpecialArea = true;
    }
  }

  void outfileButton_actionPerformed(ActionEvent e) {
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());

    chooser.setDialogTitle("Please provide an output file prefix");
    int returnVal = chooser.showDialog(this,"Ok");
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      outputFile = chooser.getSelectedFile();
      outfileText.setText(outputFile.getPath());
      databaseWriteCB.setEnabled(true);
      gisUpdateSpreadCB.setEnabled(true);
      gisUpdateSpreadCB.setSelected(true);

      if (gisUpdateSpreadCB.isSelected()) {
        String nFiles = Integer.toString(numSims * numSteps * 2);
        gisUpdateSpreadLabel.setText("(# of Files " + nFiles + ")");
      }
      writeAccessFilesCB.setEnabled(true);
      writeAccessFilesCB.setSelected(false);
    }
    else {
      outputFile = null;
      outfileText.setText("");
      databaseWriteCB.setEnabled(false);
      databaseWriteCB.setSelected(false);
      gisUpdateSpreadCB.setEnabled(false);
      gisUpdateSpreadCB.setSelected(false);
      gisUpdateSpreadLabel.setText("");
      writeAccessFilesCB.setEnabled(false);
      writeAccessFilesCB.setSelected(false);
//      writeProbFilesCB.setEnabled(false);
//      writeProbFilesCB.setSelected(false);
    }
  }

  public void allStatesFilePB_actionPerformed(ActionEvent e) {
    allStatesRulesFile = Utility.getOpenFile(this, "All States Customization file");
    String str = (allStatesRulesFile != null) ? allStatesRulesFile.getPath() : "";
    allStatesFileText.setText(str);

    if (allStatesRulesFile != null) {
      allStatesCB.setSelected(true);
    }
  }

  public void tsInMemoryText_actionPerformed(ActionEvent e) {
    tsInMemoryText.getNextFocusableComponent().requestFocus();
  }
/**
 * Handles if focus is lost from "Time Steps kept in memory(minimum 10) text field.  
 * @param e
 */
  public void tsInMemoryText_focusLost(FocusEvent e) {
    int numSteps;
    if (focusLost) { return; }
    try {
      numSteps = Integer.parseInt(tsInMemoryText.getText());
      if (numSteps < 10) {
        throw new NumberFormatException();
      }
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      String msg = "Invalid # of time steps in Memory." + JSimpplle.endl +
                   "Please enter a number >= 10";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          numStepText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }

  }
/**
 * If discard data check box is selected, allows the user to discard time step data - for longer term simulations.  (minimum kept in memory must be 10)
 * @param e
 */
  public void discardDataCB_actionPerformed(ActionEvent e) {
    tsInMemoryText.setEnabled(discardDataCB.isSelected());
    tsInMemoryLabel.setEnabled(discardDataCB.isSelected());

    if (discardDataCB.isSelected()) {
      allStatesCB.setSelected(true);
    }
  }

  public void gisUpdateSpreadCB_actionPerformed(ActionEvent e) {
    if (outputFile != null && gisUpdateSpreadCB.isSelected()) {
      String nFiles = Integer.toString(numSims * numSteps * 2);
      gisUpdateSpreadLabel.setText("(# of Files " + nFiles + ")");
    }
    else {
      gisUpdateSpreadLabel.setText("");
    }

  }
/**
 * If "Adjust Categories" check box is selected - will create a new tracking species report dialog.    
 * @param e
 */
  public void trackingSpeciesCategoryPB_actionPerformed(ActionEvent e) {
    TrackingSpeciesReportDlg dlg =
      new TrackingSpeciesReportDlg(JSimpplle.getSimpplleMain(),"Tracking Species Report Categories",true);
    dlg.setVisible(true);
  }
  private class WriteAccessFilesCBActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      writeAccessFilesCB_actionPerformed(e);
    }
  }
  protected void writeAccessFilesCB_actionPerformed(ActionEvent e) {
    // Nothing needs doing here.
  }

}






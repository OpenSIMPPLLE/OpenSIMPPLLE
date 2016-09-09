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

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Vector;

/**
 * This dialog allows the user to set various parameters that control how the simulation in performed.
 * The dialog titled "Set Simulation Parameters" and has methods to input # of simulations, time steps, sim method, invasive species logic, 
 * options such as yearly time steps, fire suppression, output options, discarding unnecessary simulation data, all states reports, and tracking species reports.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class SimParam extends JDialog {
  private simpplle.comcode.Simpplle comcode;
  private int numSteps = 5;
  private int numSims = 1;

  private boolean focusLost;
  private boolean fireSuppression;
  private boolean discountCost;
  private boolean fixedSeed;
  private boolean trackOwnership;
  private boolean trackSpecialArea;
  private boolean yearlySteps;
  private File    outputFile;
  private File    allStatesRulesFile;
  private int     maxTimeSteps;
  /**
   * Used to dynamically populate available spread models
   */
  private Vector<String> fireSpreadModels;

  // Elements
  private JLabel tsInMemoryLabel = new JLabel();
  private JButton runButton       = new JButton();
  private JButton cancelButton    = new JButton();
  private JButton outfileButton   = new JButton();
  private JButton allStatesFilePB = new JButton();
  private JTextField numSimText   = new JTextField();
  private JTextField numStepText  = new JTextField();
  private JTextField discountText = new JTextField();
  private JTextField outfileText  = new JTextField();
  private JCheckBox fireSuppCB    = new JCheckBox();
  private JCheckBox discountCB    = new JCheckBox();
  private JCheckBox ownershipCB   = new JCheckBox();
  private JCheckBox specialAreaCB = new JCheckBox();
  private JCheckBox yearlyStepCB  = new JCheckBox();
  private JComboBox simMethodCB   = new JComboBox();
  private JCheckBox discardDataCB = new JCheckBox();
  private JCheckBox allStatesCB   = new JCheckBox();
  private JCheckBox databaseWriteCB = new JCheckBox();
  private JTextField allStatesFileText = new JTextField();
  private JTextField tsInMemoryText = new JTextField();
  private JButton trackingSpeciesCategoryPB = new JButton();
  private JCheckBox gisUpdateSpreadCB = new JCheckBox();
  private JComboBox invasiveSpeciesCB = new JComboBox();
  private JComboBox fireSpreadModelCB;
  private JCheckBox trackingSpeciesCB = new JCheckBox();
  private JCheckBox writeAccessFilesCB = new JCheckBox();
  //Seed Simulation Options
  private JCheckBox fixedSeedCB    = new JCheckBox();
  private JTextField fixedSeedText = new JTextField();
  //  Option to disable writing probability Arc Files. Currently, this information is not used in output processing.
  private JCheckBox writeAreaProbFilesCB = new JCheckBox();
  // Option to write area summary file.
  private JCheckBox writeAreaSummaryCB = new JCheckBox();

  // This need to be down here so designer will work correctly.
  private SimpplleMain simpplleMain;

  /**
   * Creates a modal simulation parameters dialog.
   *
   * @param frame A parent frame
   * @param fireSpreadModels A vector of fire spread model names
   */
  public SimParam(SimpplleMain frame, Vector<String> fireSpreadModels) {

    super(frame, "Simulation Parameters", true);

    this.simpplleMain = frame;
    this.fireSpreadModels = fireSpreadModels;

    try  {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }

    initialize();

  }

  void jbInit() throws Exception {

    Font monospaced = new Font("Monospaced", Font.PLAIN, 14);

    /* Number of Simulations */

    numSimText.setText("1");
    numSimText.setBackground(Color.white);
    numSimText.setSelectionColor(Color.blue);
    numSimText.setPreferredSize(new Dimension(100,27));
    numSimText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        numSimText_actionPerformed(e);
      }
    });
    numSimText.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        numSimText_focusLost(e);
      }
    });

    JLabel numSimLabel = new JLabel();
    numSimLabel.setFont(monospaced);
    numSimLabel.setText("Number of Simulations ");

    FlowLayout numSimLayout = new FlowLayout();
    numSimLayout.setAlignment(FlowLayout.LEFT);
    numSimLayout.setHgap(10);
    numSimLayout.setVgap(1);

    JPanel numSimPanel = new JPanel();
    numSimPanel.setLayout(numSimLayout);
    numSimPanel.add(numSimLabel, null);
    numSimPanel.add(numSimText, null);

    /* Number of Time Steps */

    numStepText.setText("5");
    numStepText.setBackground(Color.white);
    numStepText.setSelectionColor(Color.blue);
    numStepText.setPreferredSize(new Dimension(100,27));
    numStepText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        numStepText_actionPerformed(e);
      }
    });
    numStepText.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        numStepText_focusLost(e);
      }
    });

    JLabel numStepLabel = new JLabel();
    numStepLabel.setFont(monospaced);
    numStepLabel.setText("Number of Time Steps  ");

    FlowLayout numStepLayout = new FlowLayout();
    numStepLayout.setAlignment(FlowLayout.LEFT);
    numStepLayout.setHgap(10);
    numStepLayout.setVgap(1);

    JPanel numStepPanel = new JPanel();
    numStepPanel.setLayout(numStepLayout);
    numStepPanel.add(numStepLabel, null);
    numStepPanel.add(numStepText, null);

    /* Simulation Method */

    simMethodCB.setPreferredSize(new Dimension(200,27));

    JLabel simMethodLabel = new JLabel();
    simMethodLabel.setFont(monospaced);
    simMethodLabel.setText("Simulation Method     ");

    FlowLayout methodLayout = new FlowLayout();
    methodLayout.setAlignment(FlowLayout.LEFT);
    methodLayout.setHgap(10);
    methodLayout.setVgap(1);

    JPanel simMethodPanel = new JPanel();
    simMethodPanel.setLayout(methodLayout);
    simMethodPanel.add(simMethodLabel, null);
    simMethodPanel.add(simMethodCB, null);

    /* Invasive Species Logic */

    invasiveSpeciesCB.setPreferredSize(new Dimension(200,27));

    JLabel invasiveSpeciesLabel = new JLabel();
    invasiveSpeciesLabel.setFont(monospaced);
    invasiveSpeciesLabel.setText("Invasive Species Logic");

    FlowLayout invasiveSpeciesLayout = new FlowLayout();
    invasiveSpeciesLayout.setAlignment(FlowLayout.LEFT);
    invasiveSpeciesLayout.setHgap(10);
    invasiveSpeciesLayout.setVgap(1);

    JPanel invasiveSpeciesPanel = new JPanel();
    invasiveSpeciesPanel.setLayout(invasiveSpeciesLayout);
    invasiveSpeciesPanel.add(invasiveSpeciesLabel);
    invasiveSpeciesPanel.add(invasiveSpeciesCB);

    /* Fire Spread Model */

    JLabel fireSpreadModelLabel = new JLabel();
    fireSpreadModelLabel.setFont(monospaced);
    fireSpreadModelLabel.setText("Fire Spread Model     ");

    fireSpreadModelCB = new JComboBox(fireSpreadModels);
    fireSpreadModelCB.setPreferredSize(new Dimension(200,27));

    FlowLayout fireSpreadModelLayout = new FlowLayout();
    fireSpreadModelLayout.setAlignment(FlowLayout.LEFT);
    fireSpreadModelLayout.setHgap(10);
    fireSpreadModelLayout.setVgap(1);

    JPanel fireSpreadModelPanel = new JPanel();
    fireSpreadModelPanel.setLayout(fireSpreadModelLayout);
    fireSpreadModelPanel.add(fireSpreadModelLabel,null);
    fireSpreadModelPanel.add(fireSpreadModelCB,null);

    /* Yearly Time Steps */

    yearlyStepCB.setFont(monospaced);
    yearlyStepCB.setText("Yearly Time Steps");
    yearlyStepCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        yearlyStepCB_itemStateChanged(e);
      }
    });

    BorderLayout yearlyStepLayout = new BorderLayout();

    JPanel yearlyStepPanel = new JPanel();
    yearlyStepPanel.setLayout(yearlyStepLayout);
    yearlyStepPanel.add(yearlyStepCB);

    /* Track Ownership */

    ownershipCB.setText("Track Ownership");
    ownershipCB.setFont(monospaced);
    ownershipCB.setEnabled(false);
    ownershipCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        ownershipCB_itemStateChanged(e);
      }
    });

    BorderLayout ownershipLayout = new BorderLayout();

    JPanel ownershipPanel = new JPanel();
    ownershipPanel.setLayout(ownershipLayout);
    ownershipPanel.add(ownershipCB);

    /* Fire Suppression */

    fireSuppCB.setText("Fire Suppression");
    fireSuppCB.setFont(monospaced);
    fireSuppCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        fireSuppCB_itemStateChanged(e);
      }
    });

    BorderLayout fireSuppLayout = new BorderLayout();

    JPanel fireSuppCBPanel = new JPanel();
    fireSuppCBPanel.setLayout(fireSuppLayout);
    fireSuppCBPanel.add(fireSuppCB);

    /* Track Special Area */

    specialAreaCB.setText("Track Special Area");
    specialAreaCB.setFont(monospaced);
    specialAreaCB.setEnabled(false);
    specialAreaCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        specialAreaCB_itemStateChanged(e);
      }
    });

    BorderLayout spAreaPanel = new BorderLayout();

    JPanel specialAreaPanel = new JPanel();
    specialAreaPanel.setLayout(spAreaPanel);
    specialAreaPanel.add(specialAreaCB);

    /* Discounted Cost */

    discountText.setText("1.04");
    discountText.setEnabled(false);
    discountText.setBackground(Color.white);
    discountText.setSelectionColor(Color.blue);
    discountText.setColumns(4);

    discountCB.setText("Discounted Cost");
    discountCB.setFont(monospaced);
    discountCB.setEnabled(false);
    discountCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        discountCB_itemStateChanged(e);
      }
    });

    BorderLayout discountLayout = new BorderLayout();

    JPanel discountPanel = new JPanel();
    discountPanel.setLayout(discountLayout);
    discountPanel.add(discountText, BorderLayout.CENTER);
    discountPanel.add(discountCB, BorderLayout.WEST);

    /* Fixed Seed */

    fixedSeedText.setText("0");
    fixedSeedText.setEnabled(false);
    fixedSeedText.setBackground(Color.white);
    fixedSeedText.setSelectionColor(Color.blue);
    fixedSeedText.setColumns(4);

    fixedSeedCB.setText("Fixed Seed");
    fixedSeedCB.setFont(monospaced);
    fixedSeedCB.setEnabled(true);
    fixedSeedCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        fixedSeedCB_itemStateChanged(e);
      }
    });

    BorderLayout fixedSeedLayout = new BorderLayout();

    JPanel fixedSeedPanel = new JPanel();
    fixedSeedPanel.setLayout(fixedSeedLayout);
    fixedSeedPanel.add(fixedSeedText, BorderLayout.CENTER);
    fixedSeedPanel.add(fixedSeedCB, BorderLayout.WEST);

    /*
     *    Options Section
     */

    Border optionsBorder  = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Options");

    GridLayout optionsLayout = new GridLayout();
    optionsLayout.setColumns(2);
    optionsLayout.setHgap(10);
    optionsLayout.setRows(3);

    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(optionsLayout);
    optionsPanel.add(yearlyStepPanel);
    optionsPanel.add(ownershipPanel);
    optionsPanel.add(fireSuppCBPanel);
    optionsPanel.add(specialAreaPanel);
    optionsPanel.add(discountPanel);
    optionsPanel.add(fixedSeedPanel);

    GridLayout outputLayout = new GridLayout();
    outputLayout.setColumns(2);
    outputLayout.setHgap(10);
    outputLayout.setRows(2);

    JPanel outputPanel = new JPanel();
    outputPanel.setLayout(outputLayout);


    FlowLayout outerOptionsLayout = new FlowLayout();
    outerOptionsLayout.setAlignment(FlowLayout.LEFT);
    outerOptionsLayout.setHgap(0);
    outerOptionsLayout.setVgap(0);

    JPanel optionsOuterPanel = new JPanel();
    optionsOuterPanel.setLayout(outerOptionsLayout);
    optionsOuterPanel.setBorder(optionsBorder);
    optionsOuterPanel.add(optionsPanel);

    /* Results Output File Chooser */

    outfileButton.setMinimumSize(new Dimension(40, 27));
    outfileButton.setPreferredSize(new Dimension(40, 27));
    outfileButton.setToolTipText("Set the output files prefix");
    outfileButton.setHorizontalTextPosition(SwingConstants.LEFT);
    outfileButton.setIcon(new ImageIcon(SimParam.class.getResource("images/save.gif")));
    outfileButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        outfileButton_actionPerformed(e);
      }
    });

    outfileText.setBackground(Color.white);
    outfileText.setEditable(false);
    outfileText.setSelectionColor(Color.blue);
    outfileText.setColumns(40);

    FlowLayout outfileLayout = new FlowLayout();
    outfileLayout.setAlignment(FlowLayout.LEFT);
    outfileLayout.setHgap(10);
    outfileLayout.setVgap(1);

    JPanel outfilePanel = new JPanel();
    outfilePanel.setLayout(outfileLayout);
    outfilePanel.add(outfileButton, null);
    outfilePanel.add(outfileText, null);

    /* Write Data to Text Files */

    writeAccessFilesCB.setText("Text Files");
    writeAccessFilesCB.setEnabled(false);
    outputPanel.add(writeAccessFilesCB);

    /* Write probability reports for multiple simulations */

    writeAreaProbFilesCB.setText("Probability Reports");
    writeAreaProbFilesCB.setToolTipText("Writes probability reports for multiple simulations");
    writeAreaProbFilesCB.setEnabled(false);
    outputPanel.add(writeAreaProbFilesCB);

    /* Write Area Summary */

    writeAreaSummaryCB.setText("Area Summary");
    writeAreaSummaryCB.setEnabled(false);
    outputPanel.add(writeAreaSummaryCB);


    /* GIS Update/Spread Files */

    gisUpdateSpreadCB.setText("GIS Update and Spread Files");
    gisUpdateSpreadCB.setEnabled(false);
    gisUpdateSpreadCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gisUpdateSpreadCB_actionPerformed(e);
      }
    });

    FlowLayout outputOptionsLayout = new FlowLayout();
    outputOptionsLayout.setAlignment(FlowLayout.LEFT);
    outputOptionsLayout.setVgap(0);

    outputPanel.add(gisUpdateSpreadCB);

    /* Results Output Options */

    JPanel outputOptionsPanel = new JPanel();
    outputOptionsPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Results Output Options"));
    outputOptionsPanel.setLayout(new BoxLayout(outputOptionsPanel,BoxLayout.Y_AXIS));
    outputOptionsPanel.add(outfilePanel);

    // add Grid of Checkboxes to Options Panel
    outputOptionsPanel.add(outputPanel);

    /* Use Database */

    databaseWriteCB.setEnabled(false);
    databaseWriteCB.setText("Write time steps to database");
    databaseWriteCB.setToolTipText("Decreases memory usage but increases simulation run time");

    FlowLayout databaseWriteLayout = new FlowLayout();
    databaseWriteLayout.setAlignment(FlowLayout.LEFT);
    databaseWriteLayout.setVgap(0);

    JPanel databaseWritePanel = new JPanel();
    databaseWritePanel.setLayout(databaseWriteLayout);
    databaseWritePanel.add(databaseWriteCB, null);

    /* Discard Unnecessary Simulation Data */

    discardDataCB.setText("Limit time steps kept in memory");
    discardDataCB.setToolTipText("Decreases memory usage for extremely long term simulations");
    discardDataCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        discardDataCB_actionPerformed(e);
      }
    });

    FlowLayout discardDataLayout = new FlowLayout();
    discardDataLayout.setAlignment(FlowLayout.LEFT);
    discardDataLayout.setVgap(0);

    JPanel discardDataPanel = new JPanel();
    discardDataPanel.setLayout(discardDataLayout);
    discardDataPanel.add(discardDataCB);

    /* Time Steps kept in memory */

    tsInMemoryText.setEnabled(false);
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
    tsInMemoryLabel.setText("Time steps kept in memory (minimum 10)");

    FlowLayout stepsInMemoryLayout = new FlowLayout();
    stepsInMemoryLayout.setAlignment(FlowLayout.LEFT);
    stepsInMemoryLayout.setVgap(0);

    JPanel timeStepsInMemoryPanel = new JPanel();
    timeStepsInMemoryPanel.setLayout(stepsInMemoryLayout);
    timeStepsInMemoryPanel.add(tsInMemoryText);
    timeStepsInMemoryPanel.add(tsInMemoryLabel);

    /* Memory Saving Options */

    Border memoryBorder = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Memory Saving Options");

    JPanel memorySavingOptionsPanel = new JPanel();
    memorySavingOptionsPanel.setLayout(new BoxLayout(memorySavingOptionsPanel, BoxLayout.Y_AXIS));
    memorySavingOptionsPanel.setBorder(memoryBorder);
    memorySavingOptionsPanel.add(databaseWritePanel);
    memorySavingOptionsPanel.add(discardDataPanel);
    memorySavingOptionsPanel.add(timeStepsInMemoryPanel);

    /* Enable All States Report */

    allStatesCB.setText("Enable All States Report");
    allStatesCB.setToolTipText("Required if discarding data or performing multiple runs");

    FlowLayout allStatesLayoutCB = new FlowLayout();
    allStatesLayoutCB.setAlignment(FlowLayout.LEFT);
    allStatesLayoutCB.setVgap(0);

    JPanel allStatesCBPanel = new JPanel();
    allStatesCBPanel.setLayout(allStatesLayoutCB);
    allStatesCBPanel.add(allStatesCB);

    /* Rules File */

    Border rulesBorder = new TitledBorder(BorderFactory.createEtchedBorder(), "Rules File (optional)");

    allStatesFilePB.setIcon(new ImageIcon(SimParam.class.getResource("images/save.gif")));
    allStatesFilePB.setMinimumSize(new Dimension(40, 27));
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

    FlowLayout rulesFileLayout = new FlowLayout();
    rulesFileLayout.setAlignment(FlowLayout.LEFT);
    rulesFileLayout.setHgap(10);

    JPanel allStatesRulesFilePanel = new JPanel();
    allStatesRulesFilePanel.setLayout(rulesFileLayout);
    allStatesRulesFilePanel.setBorder(rulesBorder);
    allStatesRulesFilePanel.add(allStatesFilePB);
    allStatesRulesFilePanel.add(allStatesFileText);

    /* All States Report */

    Border statesBorder   = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "All States Report");

    BorderLayout allStatesLayout = new BorderLayout();

    JPanel allStatesPanel = new JPanel();
    allStatesPanel.setLayout(allStatesLayout);
    allStatesPanel.setBorder(statesBorder);
    allStatesPanel.add(allStatesCBPanel, BorderLayout.NORTH);
    allStatesPanel.add(allStatesRulesFilePanel, BorderLayout.CENTER);

    /* Generate Tracking Species Report */

    trackingSpeciesCB.setText("Generate Tracking Species Report");
    trackingSpeciesCB.setToolTipText("Required if discarding data or performing multiple runs");

    FlowLayout trackSpeciesCBLayout = new FlowLayout();
    trackSpeciesCBLayout.setAlignment(FlowLayout.LEFT);
    trackSpeciesCBLayout.setVgap(0);

    JPanel trackSpeciesCBPanel = new JPanel();
    trackSpeciesCBPanel.setLayout(trackSpeciesCBLayout);
    trackSpeciesCBPanel.add(trackingSpeciesCB);

    /* Adjust Categories */

    trackingSpeciesCategoryPB.setText("Adjust Categories");
    trackingSpeciesCategoryPB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        trackingSpeciesCategoryPB_actionPerformed(e);
      }
    });

    FlowLayout trackSpeciesButtonLayout = new FlowLayout();
    trackSpeciesButtonLayout.setAlignment(FlowLayout.LEFT);
    trackSpeciesButtonLayout.setVgap(0);

    JPanel trackSpeciesCategoryButtonPanel = new JPanel();
    trackSpeciesCategoryButtonPanel.setLayout(trackSpeciesButtonLayout);
    trackSpeciesCategoryButtonPanel.add(trackingSpeciesCategoryPB);

    /* Tracking Species Report */

    Border trackingBorder = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Tracking Species Report");

    BorderLayout trackSpeciesLayout = new BorderLayout();

    JPanel trackingSpeciesPanel = new JPanel();
    trackingSpeciesPanel.setLayout(trackSpeciesLayout);
    trackingSpeciesPanel.setBorder(trackingBorder);
    trackingSpeciesPanel.add(trackSpeciesCategoryButtonPanel, BorderLayout.CENTER);
    trackingSpeciesPanel.add(trackSpeciesCBPanel, BorderLayout.NORTH);

    /* Button Panel */

    runButton.setText("Run Simulation");
    runButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        runButton_actionPerformed(e);
      }
    });

    cancelButton.setText("Cancel");
    cancelButton.setMaximumSize(new Dimension(119, 27));
    cancelButton.setMinimumSize(new Dimension(119, 27));
    cancelButton.setPreferredSize(new Dimension(119, 27));
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.setAlignmentX(0.0f);
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    buttonPanel.add(runButton, null);
    buttonPanel.add(cancelButton, null);

    /* Main Panel */

    JPanel northPanel = new JPanel();
    northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
    northPanel.add(numSimPanel);
    northPanel.add(numStepPanel);
    northPanel.add(simMethodPanel);
    northPanel.add(invasiveSpeciesPanel);
    northPanel.add(fireSpreadModelPanel);
    northPanel.add(optionsOuterPanel);
    northPanel.add(outputOptionsPanel);
    northPanel.add(memorySavingOptionsPanel);
    northPanel.add(allStatesPanel);
    northPanel.add(trackingSpeciesPanel);

    BorderLayout southLayout = new BorderLayout();

    JPanel southPanel = new JPanel();
    southPanel.setLayout(southLayout);
    southPanel.add(buttonPanel, BorderLayout.NORTH);

    BorderLayout mainLayout = new BorderLayout();
    mainLayout.setHgap(5);
    mainLayout.setVgap(5);

    JPanel mainPanel = new JPanel();
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    mainPanel.setLayout(mainLayout);
    mainPanel.setAlignmentX(0.0f);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    mainPanel.add(southPanel, BorderLayout.SOUTH);

    getContentPane().add(mainPanel, BorderLayout.WEST);

  }

/**
 * This initializes the simulation parameters.  Since most of the parameters need to be chosen in order to be used, the booleans governing them
 * are set to false.  Simulation methods are stochastic, stand development(succession), and highest - the three probability methods used in OpenSimpplle.  
 * There are some numbers used by default... simulations = 1, time steps = 5, sim method = stochastic. invasive species logic = none.  These are set in 
 * Simulation.java.   
 * Tracking species is only to be used where there is logic for it.  These are Eastside Region 1 and Colorado Plateau (mesa verde).
 * Yearly time steps must be used for wyoming regions (grasslands) all others have the option to choose yearly or decade time steps.
 */
  private void initialize() {
    comcode            = JSimpplle.getComcode();
    focusLost          = false;
    fireSuppression    = false;
    discountCost       = false;
    fixedSeed          = false;
    trackOwnership     = false;
    trackSpecialArea   = false;
    yearlySteps        = false;
    outputFile         = null;
    allStatesRulesFile = null;
    numSims            = 1;

    maxTimeSteps = simpplle.comcode.Simulation.MAX_TIME_STEPS;

    simMethodCB.addItem("STOCHASTIC");
    simMethodCB.addItem("STAND DEVELOPMENT");
    simMethodCB.addItem("HIGHEST");
    simMethodCB.setSelectedIndex(0);

    Simulation.InvasiveKind[] kinds = Simulation.InvasiveKind.values();

    for (int i=0; i<kinds.length; i++) {
      if (kinds[i] == Simulation.InvasiveKind.MSU &&
          (!ExistingLandUnit.hasNumericAspect() ||
           !InvasiveSpeciesLogicMSU.hasData())) {
        continue;
      }
      if (kinds[i] == Simulation.InvasiveKind.MESA_VERDE_NP &&
          ((!InvasiveSpeciesLogic.hasData()))) {
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
 * Runs the simulation (if 'Run Simulation' button is pushed).  Basically it
 * parses in the user set parameters and calls the runSimulation method
 * from comcode Simpplle.java which then creates a new simulation instance
 * with the passed parameters.
 * @return if running simulation was successful.
 */
  private boolean runSimulation() {
    int     numSimulations, numSteps;
    int     tsInMemory;
    float   discount;
    //Seed to run the Simulation with
    long seed;
    String  simulationMethod;

    try {
      numSimulations = Integer.parseInt(numSimText.getText());
      numSteps       = Integer.parseInt(numStepText.getText());
      tsInMemory     = Integer.parseInt(tsInMemoryText.getText());
      /* -- JDK 1.2 -- discount = Float.parseFloat(discountText.getText()); */
      if (discountCost) {
        discount = Float.valueOf(discountText.getText());
      }
      else {
        discount = 1.0f;
      }
      if (fixedSeed) {
        seed = Long.valueOf(fixedSeedText.getText());
      }
      else{
        seed = -1;
      }
      simulationMethod = (String) simMethodCB.getSelectedItem();
    }
    catch (NumberFormatException nfe) {
      String msg = "One of the following fields is invalid:" +
                   JSimpplle.endl +
                   "  - Number of Simulations" + JSimpplle.endl +
                   "  - Number of Time Steps" + JSimpplle.endl +
                   "  - Fire Suppression Cost Discount."+ JSimpplle.endl +
                   "  - Fixed Seed.";
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
        comcode.runSimulation(numSimulations,
            numSteps,
            fireSuppression,
            outputFile,
            discount,
            trackSpecialArea,
            trackOwnership,
            yearlySteps,
            simulationMethod,
            databaseWriteCB.isSelected(),
            writeAccessFilesCB.isSelected(), /*write access files*/
              /*writeProbFilesCB.isSelected()*/true,
            (Simulation.InvasiveKind) invasiveSpeciesCB.getSelectedItem(),
            tsInMemory,
            allStatesRulesFile,
            discardDataCB.isSelected(),
            writeAreaProbFilesCB.isSelected(),
            allStatesCB.isSelected(),
            trackingSpeciesCB.isSelected(),
            gisUpdateSpreadCB.isSelected(),
            fixedSeed,
            seed,
            writeAreaSummaryCB.isSelected());
      } catch (simpplle.comcode.SimpplleError e) {
        JOptionPane.showMessageDialog(this, e.getError(), "Simulation Failed",
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

    String selectedModel = (String)fireSpreadModelCB.getSelectedItem();

    if (selectedModel.equals("KEANE")) {

      ProcessOccurrenceSpreadingFire.setSpreadModel(ProcessOccurrenceSpreadingFire.SpreadModel.KEANE);

    } else {

      ProcessOccurrenceSpreadingFire.setSpreadModel(ProcessOccurrenceSpreadingFire.SpreadModel.SIMPPLLE);

    }

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
 * If 'Cancel' button is pressed, will dispose of the Simulation Parameter dialog.
 * @param e
 */
  void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
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
 * Handles the check box which allows for yearly or decade time steps.
 * Yearly time steps are hard coded to be used for Wyoming zone,
 * all others can choose yearly or decade.
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
 * Handles check box for fire suppression.  Simulations without fire suppression
 * are most commonly used with long term simulations to create an interaction between
 * fire, insect, and disease disturbances.  
 * @param e fire suppression selected or deselected (false by default -
 *          selecting makes fire suppression true).
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

  void fixedSeedCB_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      fixedSeed = false;
      fixedSeedText.setEnabled(false);
    }
    else if (e.getStateChange() == ItemEvent.SELECTED) {
      fixedSeed = true;
      fixedSeedText.setEnabled(true);
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
      gisUpdateSpreadCB.setSelected(false);

      writeAccessFilesCB.setEnabled(true);
      writeAccessFilesCB.setSelected(true);
      writeAreaProbFilesCB.setEnabled(true);
      writeAreaSummaryCB.setEnabled(true);
    }
    else {
      outputFile = null;
      outfileText.setText("");
      databaseWriteCB.setEnabled(false);
      databaseWriteCB.setSelected(false);
      gisUpdateSpreadCB.setEnabled(false);
      gisUpdateSpreadCB.setSelected(false);
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
 * If discard data check box is selected, allows the user to discard time step data
 * - for longer term simulations.  (minimum kept in memory must be 10)
 */
  public void discardDataCB_actionPerformed(ActionEvent e) {
    tsInMemoryText.setEnabled(discardDataCB.isSelected());
    tsInMemoryLabel.setEnabled(discardDataCB.isSelected());

    if (discardDataCB.isSelected()) {
      allStatesCB.setSelected(true);
    }
  }

  public void gisUpdateSpreadCB_actionPerformed(ActionEvent e) {

  }
/**
 * If "Adjust Categories" check box is selected - will create a new tracking
 * species report dialog.
 */
  public void trackingSpeciesCategoryPB_actionPerformed(ActionEvent e) {
    TrackingSpeciesReportDlg dlg =
      new TrackingSpeciesReportDlg(JSimpplle.getSimpplleMain(),
              "Tracking Species Report Categories",true);
    dlg.setVisible(true);
  }

}






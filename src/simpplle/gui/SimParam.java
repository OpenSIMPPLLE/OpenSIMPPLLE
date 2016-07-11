
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
  /**
   * Used to dynamically populate available spread models
   */
  private Vector<String> fireSpreadModels;

  // Layouts
  private BorderLayout mainLayout  = new BorderLayout();
  private BorderLayout southLayout = new BorderLayout();
  private BorderLayout yearlyStepLayout = new BorderLayout();
  private BorderLayout ownershipLayout = new BorderLayout();
  private BorderLayout discountLayout = new BorderLayout();
  private BorderLayout spAreaPanel = new BorderLayout();
  private BorderLayout fireSuppLayout = new BorderLayout();
  private BorderLayout allStatesLayout = new BorderLayout();
  private BorderLayout outOptionsLayout = new BorderLayout();
  private BorderLayout trackSpeciesLayout = new BorderLayout();
  private FlowLayout outfileLayout = new FlowLayout();
  private FlowLayout methodLayout = new FlowLayout();
  private FlowLayout numSimLayout = new FlowLayout();
  private FlowLayout numStepLayout = new FlowLayout();
  private FlowLayout invasiveSpeciesLayout = new FlowLayout();
  private FlowLayout fireSpreadModelLayout = new FlowLayout();
  private FlowLayout discardDataLayout = new FlowLayout();
  private FlowLayout rulesFileLayout = new FlowLayout();
  private FlowLayout stepsInMemoryLayout = new FlowLayout();
  private FlowLayout allStatesLayoutCB = new FlowLayout();
  private FlowLayout outputOptionsLayout = new FlowLayout();
  private FlowLayout outerOptionsLayout = new FlowLayout();
  private FlowLayout databaseWriteLayout = new FlowLayout();
  private FlowLayout trackSpeciesCBLayout = new FlowLayout();
  private FlowLayout trackSpeciesButtonLayout = new FlowLayout();
  private FlowLayout probReportsLayout = new FlowLayout();
  private GridLayout optionsLayout = new GridLayout();

  // Panels
  private JPanel mainPanel        = new JPanel();
  private JPanel buttonPanel      = new JPanel();
  private JPanel northPanel       = new JPanel();
  private JPanel southPanel       = new JPanel();
  private JPanel discountPanel    = new JPanel();
  private JPanel fireSuppCBPanel  = new JPanel();
  private JPanel specialAreaPanel = new JPanel();
  private JPanel ownershipPanel   = new JPanel();
  private JPanel outfilePanel     = new JPanel();
  private JPanel numSimPanel      = new JPanel();
  private JPanel numStepPanel     = new JPanel();
  private JPanel discardDataPanel = new JPanel();
  private JPanel discardTextPanel = new JPanel();
  private JPanel simMethodPanel   = new JPanel();
  private JPanel yearlyStepPanel  = new JPanel();
  private JPanel jPanel1          = new JPanel();
  private JPanel allStatesPanel   = new JPanel();
  private JPanel allStatesCBPanel = new JPanel();
  private JPanel optionsPanel     = new JPanel();
  private JPanel optionsOuterPanel = new JPanel();
  private JPanel databaseWritePanel = new JPanel();
  private JPanel outputOptionsPanel = new JPanel();
  private JPanel trackSpeciesCBPanel = new JPanel();
  private JPanel invasiveSpeciesPanel = new JPanel();
  private JPanel fireSpreadModelPanel = new JPanel();
  private JPanel outputOptionsCBPanel = new JPanel();
  private JPanel trackingSpeciesPanel = new JPanel();
  private JPanel timeStepsInMemoryPanel = new JPanel();
  private JPanel allStatesRulesFilePanel = new JPanel();
  private JPanel trackSpeciesCategoryButtonPanel = new JPanel();
  private JPanel panel = new JPanel();
  private JPanel writeAccessPanel = new JPanel();

   // Labels
  private JLabel numStepLabel   = new JLabel();
  private JLabel numSimLabel    = new JLabel();
  private JLabel simMethodLabel = new JLabel();
  private JLabel tsInMemoryLabel = new JLabel();
  private JLabel gisUpdateSpreadLabel = new JLabel();
  private JLabel invasiveSpeciesLabel = new JLabel();
  private JLabel fireSpreadModelLabel = new JLabel();

  // Elements
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
  //  Option to disable writing probability Arc Files. Currently, this information is not used in output processing.
  private JCheckBox writeAreaProbFilesCB = new JCheckBox();

  // This need to be down here so designer will work correctly.
  private SimpplleMain simpplleMain;

  // Overloaded Constructor
  public SimParam(SimpplleMain frame, String title, boolean modal, Vector<String> fireSpreadModels) {
    super(frame, title, modal);
    this.fireSpreadModels = fireSpreadModels;
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
    this(null, "", false, new Vector<>());
  }

  void jbInit() throws Exception {

    // default font
    Font defaultFont = new java.awt.Font("Monospaced", Font.PLAIN, 14);

    // build borders
    Border border3 = BorderFactory.createLineBorder(SystemColor.
            controlText, 2);
    Border memoryBorder = new TitledBorder(border3, "Memory Saving Options");
    Border trackingBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
            "Tracking Species Report");
    Border optionsBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
            "Options");
    Border outputBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
            "Results Output Options");
    Border rulesBorder = new TitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.
            RAISED, Color.white, new Color(148, 145, 140)), "Rules File (optional)");
    Border statesBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 2),
            "All States Report");

    // layout options
    mainLayout.setHgap(5);
    mainLayout.setVgap(5);
    outfileLayout.setAlignment(FlowLayout.LEFT);
    outfileLayout.setHgap(10);
    outfileLayout.setVgap(1);
    probReportsLayout.setAlignment(FlowLayout.LEFT);
    methodLayout.setAlignment(FlowLayout.LEFT);
    methodLayout.setHgap(10);
    methodLayout.setVgap(1);
    numSimLayout.setAlignment(FlowLayout.LEFT);
    numSimLayout.setHgap(10);
    numSimLayout.setVgap(1);
    numStepLayout.setAlignment(FlowLayout.LEFT);
    numStepLayout.setHgap(10);
    numStepLayout.setVgap(1);
    invasiveSpeciesLayout.setAlignment(FlowLayout.LEFT);
    invasiveSpeciesLayout.setHgap(10);
    invasiveSpeciesLayout.setVgap(1);
    fireSpreadModelLayout.setAlignment(FlowLayout.LEFT);
    fireSpreadModelLayout.setHgap(10);
    fireSpreadModelLayout.setVgap(1);
    discardDataLayout.setAlignment(FlowLayout.LEFT);
    discardDataLayout.setVgap(0);
    rulesFileLayout.setAlignment(FlowLayout.LEFT);
    rulesFileLayout.setHgap(10);
    stepsInMemoryLayout.setAlignment(FlowLayout.LEFT);
    stepsInMemoryLayout.setVgap(0);
    optionsLayout.setColumns(2);
    optionsLayout.setHgap(10);
    optionsLayout.setRows(3);
    outerOptionsLayout.setAlignment(FlowLayout.LEFT);
    outerOptionsLayout.setHgap(0);
    outerOptionsLayout.setVgap(0);
    databaseWriteLayout.setAlignment(FlowLayout.LEFT);
    databaseWriteLayout.setVgap(0);
    trackSpeciesButtonLayout.setAlignment(FlowLayout.LEFT);
    trackSpeciesButtonLayout.setVgap(0);
    trackSpeciesCBLayout.setAlignment(FlowLayout.LEFT);
    trackSpeciesCBLayout.setVgap(0);
    allStatesLayoutCB.setAlignment(FlowLayout.LEFT);
    allStatesLayoutCB.setVgap(0);

    // Set layouts
    mainPanel.setLayout(mainLayout);
    southPanel.setLayout(southLayout);
    northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
    discountPanel.setLayout(discountLayout);
    fireSuppCBPanel.setLayout(fireSuppLayout);
    ownershipPanel.setLayout(ownershipLayout);
    specialAreaPanel.setLayout(spAreaPanel);
    outfilePanel.setLayout(outfileLayout);
    yearlyStepPanel.setLayout(yearlyStepLayout);
    simMethodPanel.setLayout(methodLayout);
    numSimPanel.setLayout(numSimLayout);
    numStepPanel.setLayout(numStepLayout);
    invasiveSpeciesPanel.setLayout(invasiveSpeciesLayout);
    fireSpreadModelPanel.setLayout(fireSpreadModelLayout);
    discardDataPanel.setLayout(discardDataLayout);
    allStatesRulesFilePanel.setLayout(rulesFileLayout);
    timeStepsInMemoryPanel.setLayout(stepsInMemoryLayout);
    jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
    allStatesPanel.setLayout(allStatesLayout);
    allStatesCBPanel.setLayout(allStatesLayoutCB);
    outputOptionsPanel.setLayout(outOptionsLayout);
    optionsPanel.setLayout(optionsLayout);
    optionsOuterPanel.setLayout(outerOptionsLayout);
    databaseWritePanel.setLayout(databaseWriteLayout);
    trackingSpeciesPanel.setLayout(trackSpeciesLayout);
    trackSpeciesCBPanel.setLayout(trackSpeciesCBLayout);
    trackSpeciesCategoryButtonPanel.setLayout(trackSpeciesButtonLayout);
    outputOptionsCBPanel.setLayout(outputOptionsLayout);
    panel.setLayout(new GridLayout(2, 0));
    writeAccessPanel.setLayout(probReportsLayout);
    discardTextPanel.setLayout(probReportsLayout);

    // Initialize
    fireSpreadModelCB = new JComboBox(fireSpreadModels);

    // Run
    runButton.setNextFocusableComponent(cancelButton);
    runButton.setText("Run Simulation");
    runButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        runButton_actionPerformed(e);
      }
    });
    // Cancel
    cancelButton.setMaximumSize(new Dimension(119, 27));
    cancelButton.setMinimumSize(new Dimension(119, 27));
    cancelButton.setPreferredSize(new Dimension(119, 27));
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
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
    numSimLabel.setFont(defaultFont);
    numSimLabel.setLabelFor(numSimText);
    numSimLabel.setText("Number of Simulations ");
    numStepLabel.setFont(defaultFont);
    numStepLabel.setLabelFor(numStepText);
    numStepLabel.setText("Number of Time Steps  ");
    mainPanel.setAlignmentX((float) 0.0);
    this.setTitle("Set Simulation Parameters");
    fireSuppCB.setFont(defaultFont);
    fireSuppCB.setNextFocusableComponent(discountCB);
    fireSuppCB.setText("Fire Suppression");
    fireSuppCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        fireSuppCB_itemStateChanged(e);
      }
    });
    discountCB.setEnabled(false);
    discountCB.setFont(defaultFont);
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
    ownershipCB.setEnabled(false);
    ownershipCB.setFont(defaultFont);
    ownershipCB.setNextFocusableComponent(specialAreaCB);
    ownershipCB.setText("Track Ownership");
    ownershipCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        ownershipCB_itemStateChanged(e);
      }
    });
    specialAreaCB.setEnabled(false);
    specialAreaCB.setFont(defaultFont);
    specialAreaCB.setText("Track Special Area");
    specialAreaCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        specialAreaCB_itemStateChanged(e);
      }
    });
    // Output File Options
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
    yearlyStepCB.setFont(defaultFont);
    yearlyStepCB.setText("Yearly Time Steps");
    yearlyStepCB.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        yearlyStepCB_itemStateChanged(e);
      }
    });
    simMethodLabel.setFont(defaultFont);
    simMethodLabel.setText("Simulation Method     ");
    databaseWriteCB.setEnabled(false);
    databaseWriteCB.setFont(defaultFont);
    databaseWriteCB.setToolTipText("This will write simulation data to a database");
    databaseWriteCB.setText("Use Database (saves memory, slows run)");
//    writeProbFilesCB.setEnabled(false);
//    writeProbFilesCB.setToolTipText("Write GIS Probability Files (slow)");
//    writeProbFilesCB.setText("Write GIS Probability Files (slow)");
    discardDataCB.setFont(defaultFont);
    discardDataCB.setText(
      "Discard Unnecessary Simulation Data, needed for extremely long term " +
        "simulations.");
    discardDataCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        discardDataCB_actionPerformed(e);
      }
    });
    writeAreaProbFilesCB.setFont(defaultFont);
    writeAreaProbFilesCB.setText(
      "Write probability reports for multiple simulations." );
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
    allStatesRulesFilePanel.setBorder(rulesBorder);
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
    jPanel1.setBorder(memoryBorder);
    allStatesCB.setText(
      "Enable All States Report (needed if above discard option is checked or doing multiple runs)");
    allStatesPanel.setBorder(statesBorder);
    outputOptionsPanel.setBorder(outputBorder);
    invasiveSpeciesLabel.setFont(defaultFont);
    invasiveSpeciesLabel.setText("Invasive Species Logic");
    fireSpreadModelLabel.setFont(defaultFont);
    fireSpreadModelLabel.setText("Fire Spread Model     ");
    optionsOuterPanel.setBorder(optionsBorder);
    trackingSpeciesCB.setText(
      "Generate Tracking Species Report (needed if discarding data or multiple " +
      "runs)");
    trackingSpeciesCategoryPB.setText("Adjust Categories");
    trackingSpeciesCategoryPB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        trackingSpeciesCategoryPB_actionPerformed(e);
      }
    });
    trackingSpeciesPanel.setBorder(trackingBorder);
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
    northPanel.add(fireSpreadModelPanel);
    fireSpreadModelPanel.add(fireSpreadModelLabel,null);
    fireSpreadModelPanel.add(fireSpreadModelCB,null);
    northPanel.add(optionsOuterPanel);
    optionsOuterPanel.add(optionsPanel);
    optionsPanel.add(yearlyStepPanel);
    yearlyStepPanel.add(yearlyStepCB);
    optionsPanel.add(ownershipPanel);
    ownershipPanel.add(ownershipCB);
    optionsPanel.add(fireSuppCBPanel);
    fireSuppCBPanel.add(fireSuppCB);
    optionsPanel.add(specialAreaPanel);
    specialAreaPanel.add(specialAreaCB);
    optionsPanel.add(discountPanel);
    northPanel.add(outputOptionsPanel);
    outputOptionsPanel.add(outfilePanel, java.awt.BorderLayout.NORTH);
    outfilePanel.add(outfileButton, null);
    outfilePanel.add(outfileText, null);
    outputOptionsPanel.add(panel, BorderLayout.SOUTH);
    gisUpdateSpreadCB.setEnabled(false);
    gisUpdateSpreadCB.setText("GIS Update/Spread Files");
    gisUpdateSpreadCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gisUpdateSpreadCB_actionPerformed(e);
      }
    });
    panel.add(outputOptionsCBPanel);
    outputOptionsLayout.setAlignment(FlowLayout.LEFT);
    outputOptionsLayout.setVgap(0);
    outputOptionsCBPanel.add(gisUpdateSpreadCB);
    outputOptionsCBPanel.add(gisUpdateSpreadLabel);
    panel.add(writeAccessPanel);
    // write Access Files
    writeAccessPanel.add(writeAccessFilesCB);
    writeAccessFilesCB.setEnabled(false);
    writeAccessFilesCB.setSelected(false);
    writeAccessFilesCB.setText("Write Data to Text Files (Suitable for import into Access or other Programs)");
    // write Area prob
    writeAreaProbFilesCB.setEnabled(false);
    discardTextPanel.add(writeAreaProbFilesCB);
    outputOptionsPanel.add(discardTextPanel);
    northPanel.add(jPanel1);
    jPanel1.add(databaseWritePanel);
    databaseWritePanel.add(databaseWriteCB, null);
    discardDataPanel.add(discardDataCB);
    jPanel1.add(discardDataPanel);
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
 * If 'cancel' button is pressed, disposes the simulation parameter dialog.  
 */
  private void cancel () {
    setVisible(false);
    dispose();
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
              (Simulation.InvasiveKind)invasiveSpeciesCB.getSelectedItem(),
              tsInMemory,
              allStatesRulesFile,
              discardDataCB.isSelected(),
              writeAreaProbFilesCB.isSelected(),
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
      writeAreaProbFilesCB.setEnabled(true);
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
    if (outputFile != null && gisUpdateSpreadCB.isSelected()) {
      String nFiles = Integer.toString(numSims * numSteps * 2);
      gisUpdateSpreadLabel.setText("(# of Files " + nFiles + ")");
    }
    else {
      gisUpdateSpreadLabel.setText("");
    }

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






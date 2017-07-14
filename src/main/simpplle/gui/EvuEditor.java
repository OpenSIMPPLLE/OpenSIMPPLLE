/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.*;
import simpplle.comcode.Climate.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.util.*;

/**
 * This class creates an Existing Vegetative Unit Editor dialog.  It allows the user to edit Evu attributes.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class EvuEditor extends JDialog {
  private Frame theFrame;

  private Evu[] allEvu;
  private Evu currentEvu;
  private Area currentArea;
  private RegionalZone currentZone;
  private Lifeform currentLife;

  private boolean inInit = false;
  private boolean isMultiLifeArea = true;

  private int showStatus;
  private int numInvalid = 0;
  private static final int SHOW_ALL = 0;
  private static final int SHOW_INVALID = 1;

  private JComboBox roadStatusCB = new JComboBox();
  JMenuBar menuBar = new JMenuBar();
  private JMenu menuUtility = new JMenu();
  private JMenuItem menuUtilityGlobalChange = new JMenuItem();
  private ButtonGroup lifeformRBGroup = new ButtonGroup();

  private JPanel mainPanel = new JPanel();
  private JPanel htGrpPanel = new JPanel();
  private JPanel statePanel = new JPanel();
  private JPanel speciesPanel = new JPanel();
  private JPanel acresPanel = new JPanel();
  private JPanel fmzPanel = new JPanel();
  private JPanel densityPanel = new JPanel();
  private JPanel sizeClassPanel = new JPanel();
  private JPanel mainAttributePanel = new JPanel();
  private JPanel otherAttributePanel = new JPanel();
  private JPanel agePanel = new JPanel();
  private JPanel mainNorthPanel = new JPanel(new BorderLayout());
  private JPanel radioPanel = new JPanel();
  private JPanel radioOuterPanel = new JPanel();
  private JPanel evuIdPanel = new JPanel();
  private JPanel mainValuesPanel = new JPanel();
  private JPanel mainLabelsPanel = new JPanel();
  private JPanel otherValuesPanel = new JPanel();
  private JPanel otherLabelsPanel = new JPanel();
  private JPanel prevNextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
  private JPanel buttonPanel = new JPanel();
  private JPanel idEditPanel = new JPanel();
  private JPanel jPanel7 = new JPanel();
  private JPanel roadStatPanel = new JPanel();
  private JPanel ownershipPanel = new JPanel();
  private JPanel initProcessPanel = new JPanel();
  private JPanel lifeformPanel = new JPanel();

  private JLabel htGrpLabel = new JLabel();
  private JLabel vegTypeLabel = new JLabel();
  private JLabel speciesLabel = new JLabel();
  private JLabel sizeClassLabel = new JLabel();
  private JLabel densityLabel = new JLabel();
  private JLabel densityInvalidLabel = new JLabel();
  private JLabel sizeClassInvalidLabel = new JLabel();
  private JLabel speciesInvalidLabel = new JLabel();
  private JLabel stateInvalidLabel = new JLabel();
  private JLabel htGrpInvalidLabel = new JLabel();
  private JLabel fmzLabel = new JLabel();
  private JLabel fmzInvalidLabel = new JLabel();
  private JLabel unitNumberLabel = new JLabel();
  private JLabel acresLabel = new JLabel();
  private JLabel ownershipLabel = new JLabel();
  private JLabel roadStatusLabel = new JLabel();
  private JLabel specialAreaLabel = new JLabel();
  private JLabel ageLabel = new JLabel();
  private JLabel ageInvalidLabel = new JLabel();
  private JLabel searchLabel = new JLabel("Search");
  private JLabel acresInvalidLabel = new JLabel();
  private JLabel initProcessLabel = new JLabel();
  private JLabel initProcessInvalidLabel = new JLabel();

  private FlowLayout flowLayout = new FlowLayout();
  private FlowLayout centeredFlow = new FlowLayout();
  private FlowLayout outterRadioLayout = new FlowLayout();
  private FlowLayout mainAttribLayout = new FlowLayout();

  private BorderLayout mainLayout = new BorderLayout();

  private GridLayout radioLayout = new GridLayout();
  private GridLayout otherLabelsLayout = new GridLayout();
  private GridLayout lifeformLayout = new GridLayout();
  private GridLayout gridLayout4 = new GridLayout();
  private GridLayout gridLayout6 = new GridLayout();
  private GridLayout gridLayout7 = new GridLayout();

  private JTextField vegTypeEdit = new JTextField();
  private JTextField htGrpEdit = new JTextField();
  private JTextField speciesEdit = new JTextField();
  private JTextField sizeClassEdit = new JTextField();
  private JTextField densityEdit = new JTextField();
  private JTextField fmzEdit = new JTextField();
  private JTextField unitNumberEdit = new JTextField();
  private JTextField acresEdit = new JTextField();
  private JTextField ownershipEdit = new JTextField();
  private JTextField specialAreaEdit = new JTextField();
  private JTextField ageEdit = new JTextField();
  private JTextField evuIdEdit = new JTextField();
  private JTextField initProcessEdit = new JTextField();

  JButton nextPB = new JButton("Next");
  JButton prevPB = new JButton("Previous");

  private JRadioButton showAllRB = new JRadioButton();
  private JRadioButton showInvalidRB = new JRadioButton();
  private JRadioButton treesRB = new JRadioButton();
  private JRadioButton otherRB = new JRadioButton();
  private JRadioButton agricultureRB = new JRadioButton();
  private JRadioButton herbaciousRB = new JRadioButton();
  private JRadioButton shrubsRB = new JRadioButton();

  private Border border1 = BorderFactory.createLineBorder(Color.gray, 2);
  private Border border2 = new TitledBorder(border1, "Lifeform");

  /**
   * Constructor for Evu Editor.  Sets the frame owner, name, and modality.
   * @param frame
   * @param title
   * @param modal
   */
  public EvuEditor(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.theFrame = frame;
    initialize();
  }

  /**
   * Overloaded Evu Editor constructor.  References the primary and passes null for frame owner, empty string for name, and false modality
   */
  public EvuEditor() {
    this(null, "", false);
  }

  /**
   * Sets the panel, components, colors, layouts, listeners, and borders for Evu Editor. 
   * @throws Exception
   */
  void jbInit() throws Exception {

    mainPanel.setLayout(mainLayout);
    htGrpLabel.setText("Ecological Grouping");
    htGrpPanel.setLayout(flowLayout);
    flowLayout.setAlignment(FlowLayout.LEFT);
    htGrpEdit.setBackground(Color.white);
    htGrpEdit.setSelectionColor(Color.blue);
    htGrpEdit.setColumns(14);
    htGrpEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        htGrpEdit_focusLost(e);
      }
    });
    htGrpEdit.addActionListener(e -> htGrpEdit_actionPerformed());
    statePanel.setLayout(flowLayout);
    vegTypeLabel.setText("Vegetative Type");
    vegTypeEdit.setBackground(Color.gray);
    vegTypeEdit.setEnabled(false);
    vegTypeEdit.setDisabledTextColor(Color.white);
    vegTypeEdit.setEditable(false);
    vegTypeEdit.setSelectionColor(Color.blue);
    vegTypeEdit.setText("RIPARIAN-GRASSES/CLOSED-TALL-SHRUB/1");
    vegTypeEdit.setColumns(28);
    speciesPanel.setLayout(flowLayout);
    speciesLabel.setText("Species");
    speciesEdit.setBackground(Color.white);
    speciesEdit.setSelectionColor(Color.blue);
    speciesEdit.setColumns(15);
    speciesEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        speciesEdit_focusLost(e);
      }
    });
    speciesEdit.addActionListener(e -> speciesEdit_actionPerformed());
    sizeClassPanel.setLayout(flowLayout);
    sizeClassLabel.setText("Size Class");
    sizeClassEdit.setBackground(Color.white);
    sizeClassEdit.setSelectionColor(Color.blue);
    sizeClassEdit.setColumns(15);
    sizeClassEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        sizeClassEdit_focusLost(e);
      }
    });
    sizeClassEdit.addActionListener(e -> sizeClassEdit_actionPerformed());
    densityPanel.setLayout(flowLayout);
    densityLabel.setText("Density");
    densityEdit.setBackground(Color.white);
    densityEdit.setSelectionColor(Color.blue);
    densityEdit.setColumns(6);
    densityEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        densityEdit_focusLost(e);
      }
    });
    densityEdit.addActionListener(e -> densityEdit_actionPerformed());
    Font serif = new Font("Serif", Font.BOLD, 14);
    densityInvalidLabel.setFont(serif);
    sizeClassInvalidLabel.setFont(serif);
    speciesInvalidLabel.setFont(serif);
    stateInvalidLabel.setFont(serif);
    stateInvalidLabel.setText("(invalid)");
    htGrpInvalidLabel.setFont(serif);
    fmzPanel.setLayout(flowLayout);
    fmzLabel.setText("Fire Management Zone");
    fmzEdit.setBackground(Color.white);
    fmzEdit.setSelectionColor(Color.blue);
    fmzEdit.setColumns(6);
    fmzEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        fmzEdit_focusLost(e);
      }
    });
    fmzEdit.addActionListener(e -> fmzEdit_actionPerformed());
    fmzInvalidLabel.setFont(serif);
    unitNumberLabel.setText("Unit Number");
    acresPanel.setLayout(flowLayout);
    acresLabel.setText("Acres");
    ownershipLabel.setText("Ownership");
    roadStatusLabel.setText("Road Status");
    specialAreaLabel.setText("Special Area");
    mainAttributePanel.setLayout(mainAttribLayout);
    otherAttributePanel.setLayout(flowLayout);
    mainAttributePanel.setBorder(BorderFactory.createEtchedBorder());
    mainLayout.setVgap(5);
    unitNumberEdit.setBackground(Color.white);
    unitNumberEdit.setSelectionColor(Color.blue);
    unitNumberEdit.setColumns(6);
    unitNumberEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        unitNumberEdit_focusLost(e);
      }
    });
    unitNumberEdit.addActionListener(e -> unitNumberEdit_actionPerformed());
    acresEdit.setBackground(Color.white);
    acresEdit.setSelectionColor(Color.blue);
    acresEdit.setColumns(10);
    acresEdit.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        acresEdit_focusLost(e);
      }
    });
    acresEdit.addActionListener(e -> acresEdit_actionPerformed());
    ownershipEdit.setBackground(Color.white);
    ownershipEdit.setSelectionColor(Color.blue);
    ownershipEdit.setColumns(15);
    ownershipEdit.addFocusListener(new FocusAdapter() {

      public void focusLost(FocusEvent e) {
        ownershipEdit_focusLost(e);
      }
    });
    ownershipEdit.addActionListener(e -> ownershipEdit_actionPerformed());
    specialAreaEdit.setBackground(Color.white);
    specialAreaEdit.setSelectionColor(Color.blue);
    specialAreaEdit.setColumns(20);
    specialAreaEdit.addFocusListener(new FocusAdapter() {

      public void focusLost(FocusEvent e) {
        specialAreaEdit_focusLost(e);
      }
    });
    specialAreaEdit.addActionListener(e -> specialAreaEdit_actionPerformed());
    agePanel.setLayout(flowLayout);
    ageLabel.setText("Age");
    ageEdit.setBackground(Color.white);
    ageEdit.setSelectionColor(Color.blue);
    ageEdit.setColumns(4);
    ageEdit.addFocusListener(new FocusAdapter() {

      public void focusLost(FocusEvent e) {
        ageEdit_focusLost(e);
      }
    });
    ageEdit.addActionListener(e -> ageEdit_actionPerformed());
    ageInvalidLabel.setFont(serif);
    radioPanel.setLayout(radioLayout);
    radioLayout.setRows(2);
    showAllRB.setSelected(true);
    showAllRB.setText("Show All Units");
    showAllRB.addActionListener(e -> showAllRB_actionPerformed());
    showInvalidRB.setText("Show Only Invalid Units");
    showInvalidRB.addActionListener(e -> showInvalidRB_actionPerformed());
    Dimension buttonDimension = new Dimension(80, 35);
    nextPB.setPreferredSize(buttonDimension);
    nextPB.addActionListener(e -> nextPB_actionPerformed());
    searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
    prevPB.setPreferredSize(buttonDimension);
    prevPB.addActionListener(e -> prevPB_actionPerformed());
    BoxLayout boxLayout = new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS);
    buttonPanel.setLayout(boxLayout);
    radioOuterPanel.setLayout(outterRadioLayout);
    outterRadioLayout.setAlignment(FlowLayout.LEFT);
    outterRadioLayout.setHgap(20);
    roadStatusCB.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        roadStatusCB_itemStateChanged(e);
      }
    });
    this.setTitle("Existing Vegetative Unit Editor");
    this.setJMenuBar(menuBar);
    this.addWindowListener(new java.awt.event.WindowAdapter() {

      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    evuIdPanel.setLayout(centeredFlow);
    evuIdEdit.setBackground(Color.white);
    evuIdEdit.setToolTipText("Change to desired Unit Id and press enter");
    evuIdEdit.setSelectionColor(Color.blue);
    evuIdEdit.setColumns(6);
    evuIdEdit.addActionListener(e -> evuIdEdit_actionPerformed());
    acresInvalidLabel.setFont(serif);
    mainLabelsPanel.setLayout(gridLayout4);
    gridLayout4.setRows(9);
    gridLayout4.setVgap(20);
    mainValuesPanel.setLayout(gridLayout6);
    gridLayout6.setRows(9);
    mainAttribLayout.setAlignment(FlowLayout.LEFT);
    mainAttribLayout.setVgap(0);
    otherLabelsPanel.setLayout(otherLabelsLayout);
    otherValuesPanel.setLayout(gridLayout7);
    otherLabelsLayout.setRows(4);
    otherLabelsLayout.setVgap(16);
    gridLayout7.setRows(4);
    idEditPanel.setLayout(flowLayout);
    ownershipPanel.setLayout(flowLayout);
    roadStatPanel.setLayout(flowLayout);
    jPanel7.setLayout(flowLayout);
    otherAttributePanel.setBorder(BorderFactory.createEtchedBorder());
    initProcessLabel.setText("Initial Process");
    initProcessPanel.setLayout(centeredFlow);
    initProcessInvalidLabel.setFont(serif);
    initProcessInvalidLabel.setText("(invalid)");
    initProcessEdit.setBackground(Color.white);
    initProcessEdit.setSelectionColor(Color.blue);
    initProcessEdit.setText("COLD-INJURY-BARK-BEETLES");
    initProcessEdit.setColumns(28);
    initProcessEdit.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        initProcessEdit_focusLost(e);
      }
    });
    initProcessEdit.addActionListener(e -> initProcessEdit_actionPerformed());
    menuUtility.setText("Utility");
    menuUtilityGlobalChange.setToolTipText("Change Ecological Grouping and Vegatative Type of all units.");
    menuUtilityGlobalChange.setText("Make Global Change ...");
    menuUtilityGlobalChange.addActionListener(e -> menuUtilityGlobalChange_actionPerformed());
    lifeformPanel.setLayout(lifeformLayout);
    lifeformLayout.setColumns(3);
    lifeformLayout.setRows(2);
    treesRB.setSelected(true);
    treesRB.setText("Trees");
    treesRB.addActionListener(e -> treesRB_actionPerformed());
    otherRB.setText("Other");
    otherRB.addActionListener(e -> otherRB_actionPerformed());
    agricultureRB.setText("Agriculture");
    agricultureRB.addActionListener(e -> agricultureRB_actionPerformed());
    herbaciousRB.setText("Herbacious");
    herbaciousRB.addActionListener(e -> herbaciousRB_actionPerformed());
    shrubsRB.setText("Shrubs");
    shrubsRB.addActionListener(e -> shrubsRB_actionPerformed());
    lifeformPanel.setBorder(border2);

    getContentPane().add(mainPanel);
    mainPanel.add(mainNorthPanel, BorderLayout.NORTH);
    mainNorthPanel.add(otherAttributePanel, BorderLayout.NORTH);
    otherAttributePanel.add(otherLabelsPanel);
    otherLabelsPanel.add(unitNumberLabel);
    otherLabelsPanel.add(ownershipLabel);
    otherLabelsPanel.add(roadStatusLabel);
    otherLabelsPanel.add(specialAreaLabel);
    otherAttributePanel.add(otherValuesPanel);
    otherValuesPanel.add(idEditPanel);
    idEditPanel.add(unitNumberEdit);
    otherValuesPanel.add(ownershipPanel);
    ownershipPanel.add(ownershipEdit);
    otherValuesPanel.add(roadStatPanel);
    roadStatPanel.add(roadStatusCB);
    otherValuesPanel.add(jPanel7);
    jPanel7.add(specialAreaEdit);
    mainNorthPanel.add(mainAttributePanel, BorderLayout.SOUTH);
    mainAttributePanel.add(mainLabelsPanel);
    mainLabelsPanel.add(htGrpLabel);
    mainLabelsPanel.add(vegTypeLabel);
    mainLabelsPanel.add(speciesLabel);
    mainLabelsPanel.add(sizeClassLabel);
    mainLabelsPanel.add(densityLabel);
    mainLabelsPanel.add(ageLabel);
    mainLabelsPanel.add(fmzLabel);
    mainLabelsPanel.add(acresLabel);
    mainLabelsPanel.add(initProcessLabel);
    mainAttributePanel.add(mainValuesPanel);
    mainValuesPanel.add(htGrpPanel);
    htGrpPanel.add(htGrpEdit);
    htGrpPanel.add(htGrpInvalidLabel);
    mainValuesPanel.add(statePanel);
    statePanel.add(vegTypeEdit);
    statePanel.add(stateInvalidLabel);
    mainValuesPanel.add(speciesPanel);
    speciesPanel.add(speciesEdit);
    speciesPanel.add(speciesInvalidLabel);
    mainValuesPanel.add(sizeClassPanel);
    sizeClassPanel.add(sizeClassEdit);
    sizeClassPanel.add(sizeClassInvalidLabel);
    mainValuesPanel.add(densityPanel);
    densityPanel.add(densityEdit);
    densityPanel.add(densityInvalidLabel);
    mainValuesPanel.add(agePanel);
    agePanel.add(ageEdit);
    agePanel.add(ageInvalidLabel);
    mainValuesPanel.add(fmzPanel);
    fmzPanel.add(fmzEdit);
    fmzPanel.add(fmzInvalidLabel);
    mainValuesPanel.add(acresPanel);
    acresPanel.add(acresEdit);
    acresPanel.add(acresInvalidLabel);
    mainValuesPanel.add(initProcessPanel);
    initProcessPanel.add(initProcessEdit);
    initProcessPanel.add(initProcessInvalidLabel);
    mainPanel.add(radioOuterPanel, BorderLayout.CENTER);
    radioOuterPanel.add(radioPanel);
    radioPanel.add(showAllRB);
    radioPanel.add(showInvalidRB);
    radioOuterPanel.add(lifeformPanel);
    lifeformPanel.add(treesRB);
    lifeformPanel.add(shrubsRB);
    lifeformPanel.add(herbaciousRB);
    lifeformPanel.add(agricultureRB);
    lifeformPanel.add(otherRB);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    prevNextPanel.add(prevPB);
    prevNextPanel.add(nextPB);
    evuIdPanel.add(searchLabel);
    evuIdPanel.add(evuIdEdit);
    buttonPanel.add(evuIdPanel);
    buttonPanel.add(Box.createRigidArea(new Dimension(240, buttonPanel.getHeight())));
    buttonPanel.add(prevNextPanel);

    ButtonGroup group = new ButtonGroup();
    group.add(showAllRB);
    group.add(showInvalidRB);
    menuBar.add(menuUtility);
    menuUtility.add(menuUtilityGlobalChange);
    lifeformRBGroup.add(treesRB);
    lifeformRBGroup.add(shrubsRB);
    lifeformRBGroup.add(herbaciousRB);
    lifeformRBGroup.add(agricultureRB);
    lifeformRBGroup.add(otherRB);
  }
  /**
   * Initializes the Evu Editor.  Sets a combo box with road status.  Area and zone are set to current simpplle area and zone. Gathers all the current area's Evus
   * and sets the current life form to trees and current Evu to first Evu.
   */
  private void initialize() {
    Roads.Status[] allRoadStatus = Roads.Status.values();
    for(int i=0;i<allRoadStatus.length;i++) {
      roadStatusCB.addItem(allRoadStatus[i]);
    }
    roadStatusCB.setSelectedIndex(0);

    currentArea = Simpplle.getCurrentArea();
    currentZone = Simpplle.getCurrentZone();

    showStatus = (showAllRB.isSelected()) ? SHOW_ALL : SHOW_INVALID;
    allEvu     = currentArea.getAllEvu();

    currentLife = Lifeform.TREES;

    currentEvu = currentArea.getFirstEvu();
    if (showStatus == SHOW_INVALID && currentEvu.isValid()) {
      getNextUnit();
    }
    else {
      updateDialog();
    }

    isMultiLifeArea = currentArea.multipleLifeformsEnabled();
  }
  /**
   * Updates the Evu Editor dialog.  Creates a set of all the life froms in Evu.  Then sets enabled trees, shrubs, herbacious, agriculture, and other radiobuttons.
   * Displays text of the Evu ID, habitat type group, current vegetative type, species, size class, density, age, fire management zone, acres, initial process,
   * Evu ID, ownership, road status, and special area.
   */
  private void updateDialog() {
    String str;
    boolean isValid;
    numInvalid = 0;

    inInit = true;
    Set<Lifeform> lives = currentEvu.getLifeforms(Season.YEAR);

    treesRB.setEnabled(lives.contains(Lifeform.TREES));
    shrubsRB.setEnabled(lives.contains(Lifeform.SHRUBS));
    herbaciousRB.setEnabled(lives.contains(Lifeform.HERBACIOUS));
    agricultureRB.setEnabled(lives.contains(Lifeform.AGRICULTURE));
    otherRB.setEnabled(lives.contains(Lifeform.NA));

    if (lives.contains(currentLife) == false) {
      if (treesRB.isEnabled()) {
        currentLife = Lifeform.TREES;
        treesRB.setSelected(true);
      }
      else if (shrubsRB.isEnabled()) {
        currentLife = Lifeform.SHRUBS;
        shrubsRB.setSelected(true);
      }
      else if (herbaciousRB.isEnabled()) {
        currentLife = Lifeform.HERBACIOUS;
        herbaciousRB.setSelected(true);
      }
      else if (agricultureRB.isEnabled()) {
        currentLife = Lifeform.AGRICULTURE;
        agricultureRB.setSelected(true);
      }
      else if (otherRB.isEnabled()) {
        currentLife = Lifeform.NA;
        otherRB.setSelected(true);
      }
    }
    inInit = false;

    // Habitat Type Group
    HabitatTypeGroup htGrp = currentEvu.getHabitatTypeGroup();
    String htGrpStr = (htGrp == null) ? "" : htGrp.toString();
    htGrpEdit.setText(htGrpStr);
    isValid = currentEvu.isHabitatTypeGroupValid();
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    htGrpInvalidLabel.setText(str);

    // Vegetative Type
    VegSimStateData state = currentEvu.getState(currentLife);
    if (state == null || state.getVeg() == null) {
      vegTypeEdit.setText("");
      isValid = false;
    }
    else {
      vegTypeEdit.setText(state.getVeg().toString());
      isValid = currentEvu.isCurrentStateValid(currentLife);
    }
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    stateInvalidLabel.setText(str);

    // Species
    if (state == null || state.getVeg() == null) {
      speciesEdit.setText("");
      isValid = false;
    }
    else {
      speciesEdit.setText(state.getVeg().getSpecies().toString());
      isValid = currentEvu.isSpeciesValid();
    }
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    speciesInvalidLabel.setText(str);

    // Size Class
    if (state == null || state.getVeg() == null) {
      sizeClassEdit.setText("");
      isValid = false;
    }
    else {
      sizeClassEdit.setText(state.getVeg().getSizeClass().toString());
      isValid = currentEvu.isSizeClassValid();
    }
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    sizeClassInvalidLabel.setText(str);

    // Density
    if (state == null || state.getVeg() == null) {
      densityEdit.setText("");
      isValid = false;
    }
    else {
      densityEdit.setText(state.getVeg().getDensity().toString());
      isValid = currentEvu.isDensityValid();
    }
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    densityInvalidLabel.setText(str);

    // Age
    if (state == null || state.getVeg() == null) {
      ageEdit.setText("");
      isValid = false;
    }
    else {
      ageEdit.setText(Integer.toString(state.getVeg().getAge()));
      isValid = currentEvu.isAgeValid();
    }
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    ageInvalidLabel.setText(str);

    // Fire Management Zone
    Fmz fmz = currentEvu.getFmz();
    str = (fmz == null) ? "" : fmz.toString();
    fmzEdit.setText(str);
    isValid = currentEvu.isFmzValid();
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    fmzInvalidLabel.setText(str);

    // Acres
    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(Area.getAcresPrecision());

    acresEdit.setText(nf.format(currentEvu.getFloatAcres()));
    if (state == null) {
      isValid = false;
    }
    else {
      isValid = currentEvu.isAcresValid();
    }
    numInvalid += (isValid) ? 0 : 1;
    str = (isValid) ? "" : "(invalid)";
    acresInvalidLabel.setText(str);

    // Initial Process
    ProcessType p =  currentEvu.getInitialProcess(currentLife);
    str = (p == null) ? "" : p.toString();
    initProcessEdit.setText(str);
    isValid = currentEvu.isInitialProcessValid();
    str = (isValid) ? "" : "(invalid)";
    initProcessInvalidLabel.setText(str);

    // Unit Number
    str = currentEvu.getUnitNumber();
    if (str == null) { str = ""; }
    unitNumberEdit.setText(str);


    // Ownership
    str = currentEvu.getOwnershipEditor();
    if (str == null) { str = ""; }
    ownershipEdit.setText(str);

    // RoadStatus
    roadStatusCB.setSelectedItem(currentEvu.getRoadStatusNew());

    // Special Area
    str = currentEvu.getSpecialAreaEditor();
    if (str == null) { str = ""; }
    specialAreaEdit.setText(str);
  }

  /**
   * Evu Id Edit text field listener.  parses the evuIdEdit text field, checks to makes sure it is a number, and a valid Evu ID,
   * then creates a new Evu Object with same ID as the current one being edited, and makes that the current Evu.
   */
  private void evuIdEdit_actionPerformed() {
    Evu newEvu;
    int id;

    try {
      id = Integer.parseInt(evuIdEdit.getText());
    }
    catch (NumberFormatException nfe) {
      JOptionPane.showMessageDialog(this,"Unit Id must be a number",
          "Invalid Id",JOptionPane.ERROR_MESSAGE);
      evuIdEdit.setText(Integer.toString(currentEvu.getId()));
      return;
    }
    if (currentArea.isValidUnitId(id)) {
      newEvu = currentArea.getEvu(id);
    }
    else {
      newEvu = null;
    }
    if (newEvu == null) {
      String msg = id + " is not a valid Unit Id.";
      JOptionPane.showMessageDialog(this,msg,"Unit Id not found",
          JOptionPane.ERROR_MESSAGE);
      evuIdEdit.setText(Integer.toString(currentEvu.getId()));
    }
    else {
      currentEvu = newEvu;
      updateDialog();
    }
  }

  /**
   * Gets the previous Evu when previous button pushed.
   */
  private void prevPB_actionPerformed() {
    getPrevUnit();
  }
  /**
   * Gets the previous Evu.  First creates a temporary Evu to hold the current Evu info in case the previous is null.  Then makes the current evu the
   * areas previous Evu.  If it is not null updates, otherwise defaults back to old Evu temporary variable.
   */
  private void getPrevUnit() {
    Evu oldEvu = currentEvu;

    if (showStatus == SHOW_ALL) {
      currentEvu = currentArea.getPrevEvu(currentEvu);
    }
    else {
      currentEvu = currentArea.getPrevInvalidEvu(currentEvu);
    }

    if (currentEvu == null) {
      currentEvu = oldEvu;
      if (currentEvu.isValid()) {
        showAllRB.setSelected(true);
        showStatus = SHOW_ALL;

        JOptionPane.showMessageDialog(this,"All Units are now Valid",
            "All Units are Valid",
            JOptionPane.INFORMATION_MESSAGE);
      }
    }
    updateDialog();
  }

  /**
   * Gets the next unit if next button is pushed
   */
  void nextPB_actionPerformed() {
    getNextUnit();
  }
  /**
   * Gets the next Evu.  First creates a temporary Evu to hold the current Evu info in case there is no next Evu.  Then makes the current evu the 
   * area's next Evu.  If new current (really the next Evu) is not null, updates, otherwise defaults back to old Evu temporary variable.  
   */
  private void getNextUnit() {
    Evu oldEvu = currentEvu;

    if (showStatus == SHOW_ALL) {
      currentEvu = currentArea.getNextEvu(currentEvu);
    }
    else {
      currentEvu = currentArea.getNextInvalidEvu(currentEvu);
    }

    if (currentEvu == null) {
      currentEvu = oldEvu;
      if (currentEvu.isValid()) {
        showAllRB.setSelected(true);
        showStatus = SHOW_ALL;

        JOptionPane.showMessageDialog(this,"All Units are now Valid",
            "All Units are Valid",
            JOptionPane.INFORMATION_MESSAGE);
      }
    }
    updateDialog();
  }

  /**
   * Shows all status radio buttons.
   */
  private void showAllRB_actionPerformed() {
    if (showStatus != SHOW_ALL) {
      showStatus = SHOW_ALL;
    }
  }

/**
 * Shows the Invalid vegetative units radio buttons
 * @param e 'show invalid'
 */
  void showInvalidRB_actionPerformed(ActionEvent e) {
    boolean existInvalid = currentArea.hasInvalidVegetationUnits();
    if (showStatus != SHOW_INVALID && existInvalid) {
      showStatus = SHOW_INVALID;
      if (numInvalid == 0) {
        getNextUnit();
      }
    }
    else {
      JOptionPane.showMessageDialog(this,"There are no Invalid units",
          "No Invalid Units found",
          JOptionPane.INFORMATION_MESSAGE);
      showAllRB.setSelected(true);
    }
  }

  /**
   * If habitat type group edit selected sends to newHtGrp().
   */
  private void htGrpEdit_actionPerformed() {
    newHtGrp();
  }
  /**
   * If habitat type group edit loses focus sends to newHtGrp(). 
   * @param e 'habitat type group edit' loses focus
   */
  private void htGrpEdit_focusLost(FocusEvent e) {
    newHtGrp();
  }
  /**
   * Sets the current Evu's habitat type group to the name of habitat type group and current life.
   */
  private void newHtGrp() {
    String htGrpStr = htGrpEdit.getText().trim().toUpperCase();

    currentEvu.setHabitatTypeGroup(htGrpStr,currentLife);
    updateDialog();
  }

  /**
   * If species edit selected sends to newSpecies(). 
   */
  private void speciesEdit_actionPerformed() {
    newSpecies();
  }
  /**
   * If species edit loses focus sends to newSpecies(). 
   * @param e 'species' loses focus
   */
  private void speciesEdit_focusLost(FocusEvent e) {
    newSpecies();
  }
  /**
   * Uses species name from species edit text field to get species.  If species is null, creates a new species instance from the current string.
   * Then sets the current Evu species to the current life.
   */
  private void newSpecies() {
    String  speciesStr = speciesEdit.getText().trim().toUpperCase();
    Species species = Species.get(speciesStr);
    if (species == null) { species = new Species(speciesStr); }

    if (!isMultiLifeArea || species.getLifeform() == currentLife) {
      currentEvu.setSpecies(species,currentLife);
    }
    else {
      JOptionPane.showMessageDialog(this, "Invalid Species", "Invalid Species",
          JOptionPane.INFORMATION_MESSAGE);
    }
    updateDialog();
  }

  /**
   * If size class edit text input sends to newSizeClass(). 
   */
  private  void sizeClassEdit_actionPerformed() {
    newSizeClass();
  }
  /**
   * If size class edit text field loses focus sends to newSizeClass(). 
   * @param e size class text field loses focus
   */
  private  void sizeClassEdit_focusLost(FocusEvent e) {
    newSizeClass();
  }
  /**
   * Uses size class text size class edit text field to get size class.  If size class  is null, creates a new size class instance from the input string.  
   * Then sets the current Evu size class.  
   */
  private void newSizeClass() {
    String    sizeClassStr = sizeClassEdit.getText().trim().toUpperCase();
    SizeClass sizeClass    = SizeClass.get(sizeClassStr);
    if (sizeClass == null) { sizeClass = new SizeClass(sizeClassStr, Structure.NON_FOREST); }

    currentEvu.setSizeClass(sizeClass,currentLife);
    updateDialog();
  }

  /**
   * If density edit text input sends to newDensity(). 
   */
  private  void densityEdit_actionPerformed() {
    newDensity();
  }
  /**
   * If density edit text field loses focus sends to newDensity(). 
   * @param e density text field loses focus
   */
  private void densityEdit_focusLost(FocusEvent e) {
    newDensity();
  }
  /**
   * Uses density text size class edit text field to get density.  If density  is null, creates a new density instance from the input string.  
   * Then sets the current Evu density.  
   */
  private void newDensity() {
    String densityStr = densityEdit.getText().trim();
    Density density   = Density.get(densityStr);
    if (density == null) { density = new Density(densityStr); }

    currentEvu.setDensity(density,currentLife);
    updateDialog();
  }

  /**
   * If age edit text input sends to newAge(). 
   */
  private void ageEdit_actionPerformed() {
    newAge();
  }
  /**
   * If age edit text field loses focus sends to newAge(). 
   * @param e age text field loses focus
   */
  private  void ageEdit_focusLost(FocusEvent e) {
    newAge();
  }
  /**
   * Uses age text edit text field to get age.  If age is a number, creates a new age instance from the input string.  
   * Then sets the current Evu age.  
   */
  private void newAge() {
    try {
      int age = Integer.parseInt(ageEdit.getText().trim());
      currentEvu.setAge(age,currentLife);
    }
    catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,"Age Must be a number greater than 0",
          "Invalid value for age",
          JOptionPane.ERROR_MESSAGE);
    }
    updateDialog();
  }

  /**
   * If fire management zone edit text input sends to newFmz(). 
   */
  private void fmzEdit_actionPerformed() {
    newFmz();
  }
  /**
   * If fire management zone edit text field loses focus sends to newFmz(). 
   * @param e fire management zone text field loses focus
   */
  private void fmzEdit_focusLost(FocusEvent e) {
    newFmz();
  }
  /**
   * Uses fire management zone edit text field input to get fire management zone     
   * Then sets the current Evu fire management zone.  
   */
  private void newFmz() {
    currentEvu.setFmz(fmzEdit.getText().trim());
    updateDialog();
  }

  /**
   * If acres edit text input sends to newAcres()). 
   */
  private void acresEdit_actionPerformed() {
    newAcres();
  }
  /**
   * If acres edit text field loses focus sends to newAcres(). 
   * @param e acres text field loses focus
   */
  private void acresEdit_focusLost(FocusEvent e) {
    newAcres();
  }
  /**
   * Uses acres edit text field input to make float representation of acres.  This will later be changed to an int as all acres are stored as int.     
   * Then sets the current Evu acres to float value.  
   */
  private void newAcres() {
    try {
      float acres = Float.valueOf(acresEdit.getText().trim()).floatValue();
      currentEvu.setAcres(acres);
    }
    catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,"Acres Must be a number greater than 0",
          "Invalid value for Acres",
          JOptionPane.ERROR_MESSAGE);
    }
    updateDialog();
  }

  /**
   * If initial process edit text input sends to newInitProcess(). 
   */
  private  void initProcessEdit_actionPerformed() {
    newInitProcess();
  }
  /**
   * If initial process edit text field loses focus sends to newInitProcess(). 
   * @param e initial process text field loses focus
   */
  private void initProcessEdit_focusLost(FocusEvent e) {
    newInitProcess();
  }
  /**
   * Uses initial process edit text field input to set process name for initial process.      
   * Then sets the current Evu initial process to the process.  
   */
  private void newInitProcess() {
    String processName = initProcessEdit.getText().trim().toUpperCase();

    currentEvu.setInitialProcess(processName);
    updateDialog();
  }

  /**
   * If Evu ID edit text input sends to newUnitNumber(). 
   */
  private  void unitNumberEdit_actionPerformed() {
    newUnitNumber();
  }
  /**
   * If Evu ID edit text field loses focus sends to newUnitNumber(). 
   * @param e Evu ID text field loses focus
   */
  private void unitNumberEdit_focusLost(FocusEvent e) {
    newUnitNumber();
  }
  /**
   * Uses Evu ID edit text field input to set new Evu ID.      
   * Then sets the current Evu ID to that ID.  
   */
  private void newUnitNumber() {
    currentEvu.setUnitNumber(unitNumberEdit.getText().trim());
    updateDialog();
  }

  /**
   * If ownership edit text input in field sends to newOwnership(). 
   */
  private  void ownershipEdit_actionPerformed() {
    newOwnership();
  }
  /**
   * If ownership edit text field loses focus sends to newOwnership(). 
   * @param e ownership ID text field loses focus
   */
  private void ownershipEdit_focusLost(FocusEvent e) {
    newOwnership();
  }
  /**
   * Uses Ownership text entered into field to change ownership of the current Evu.      
   * Then sets the current Evu ownership to that ownership.  
   */
  private void newOwnership() {
    String str = ownershipEdit.getText().trim();
    if (str.length() > 0) {
      currentEvu.setOwnership(str);
    }
  }

  /**
   * If an item changed in the road status combo box, it gets the item that was changed and sets the road status of the item.
   * @param e
   */
  private void roadStatusCB_itemStateChanged(ItemEvent e) {
    Roads.Status item = (Roads.Status) e.getItem();
    if (item == null || currentEvu == null) { return; }

    currentEvu.setRoadStatus(item);
  }

  /**
   * If special area text input into edit field sends to newSpecialArea(). 
   */
  private void specialAreaEdit_actionPerformed() {
    newSpecialArea();
  }
  /**
   * If special area edit text field loses focus sends to newSpecialArea(). 
   */
  private void specialAreaEdit_focusLost(FocusEvent e) {
    newSpecialArea();
  }
  /**
   * Uses special area text entered into field to make the special area.      
   * Then sets the special area of Evu to that the input special area.  
   */
  private void newSpecialArea() {
    String str = specialAreaEdit.getText().trim();
    if (str.length() > 0) {
      currentEvu.setSpecialArea(str);
    }
  }

  /**
   * Method to quite the Evu Editor and updates area.
   */
  private void quit() {
    simpplle.JSimpplle.getSimpplleMain().updateAreaValidity();
    Area area = Simpplle.getCurrentArea();
    area.updateArea();
    setVisible(false);
    dispose();
  }
  /**
   * Window closing event quits.
   * @param e window closing event
   */
  void this_windowClosing(WindowEvent e) {
    quit();
  }

  /**
   * Creates an Evu Global Editor dialog with the current frame and title Change All Units
   */
  private void menuUtilityGlobalChange_actionPerformed() {
    EvuGlobalEditor dlg =
        new EvuGlobalEditor(theFrame,"Change All Units",true,currentEvu,currentLife);
    dlg.setVisible(true);
    updateDialog();
  }
  /**
   * If trees radio button pushed makes the current life form trees.
   */
  private  void treesRB_actionPerformed() {
    currentLife = Lifeform.TREES;
    if (!inInit) { updateDialog(); }
  }
  /**
   * If shrubs radio button pushed makes the current life form shrubs.
   */
  private  void shrubsRB_actionPerformed() {
    currentLife = Lifeform.SHRUBS;
    if (!inInit) { updateDialog(); }
  }
  /**
   * If herbacious radio button pushed makes the current life form herbacious.
   */
  private  void herbaciousRB_actionPerformed() {
    currentLife = Lifeform.HERBACIOUS;
    if (!inInit) { updateDialog(); }
  }
  /**
   * If agriculture radio button pushed makes the current life form agriculture.
   */
  private void agricultureRB_actionPerformed() {
    currentLife = Lifeform.AGRICULTURE;
    if (!inInit) { updateDialog(); }
  }
  /**
   * If other radio button pushed makes the current life form NA.
   */
  private  void otherRB_actionPerformed() {
    currentLife = Lifeform.NA;
    if (!inInit) { updateDialog(); }
  }
}

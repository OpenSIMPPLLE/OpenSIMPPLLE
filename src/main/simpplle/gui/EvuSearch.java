package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Simpplle;
import simpplle.comcode.Evu;
import simpplle.comcode.Area;
import simpplle.comcode.Fmz;
import simpplle.comcode.HabitatTypeGroupType;
import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.Species;
import simpplle.comcode.SizeClass;
import simpplle.comcode.Density;
import simpplle.comcode.TreatmentSchedule;
import simpplle.comcode.TreatmentApplication;
import simpplle.comcode.TreatmentType;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessSchedule;
import simpplle.comcode.ProcessType;
import simpplle.comcode.ProcessApplication;
import simpplle.comcode.Simulation;

import java.util.Vector;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
*
* The University of Montana owns copyright of the designated documentation contained 
* within this file as part of the software product designated by Uniform Resource Identifier 
* UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
* Open Source License Contract pertaining to this documentation and agrees to abide by all 
* restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
* <p>This class creates Evu Search dialog.  It allows the user to search for Evu's that match one or more attributes (i.e. ownership, age, size class).  
* 
* @author Documentation by Brian Losi
* <p>Original source code authorship: Kirk A. Moeller 
* 
* 
*/

public class EvuSearch extends JDialog {
  String      prototypeCellValue = "117000 - ALTERED-GRASSES/CLOSED-TALL-SHRUB/1";
  EvuAnalysis evuAnalysisDlg;
  Vector      units;

  public static boolean isOpen = false;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel choicesPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JPanel htGrpPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JComboBox htGrpCB = new JComboBox();
  JPanel roadStatusPanel = new JPanel();
  JComboBox roadStatusCB = new JComboBox();
  FlowLayout flowLayout10 = new FlowLayout();
  JCheckBox roadStatusCheckBox = new JCheckBox();
  JCheckBox htGrpCheckBox = new JCheckBox();
  JPanel fmzPanel = new JPanel();
  JComboBox fmzCB = new JComboBox();
  FlowLayout flowLayout3 = new FlowLayout();
  JCheckBox fmzCheckBox = new JCheckBox();
  JPanel specialAreaPanel = new JPanel();
  JComboBox specialAreaCB = new JComboBox();
  FlowLayout flowLayout4 = new FlowLayout();
  JCheckBox specialAreaCheckBox = new JCheckBox();
  JPanel ownershipPanel = new JPanel();
  JComboBox ownershipCB = new JComboBox();
  FlowLayout flowLayout5 = new FlowLayout();
  JCheckBox ownershipCheckBox = new JCheckBox();
  JPanel densityPanel = new JPanel();
  JComboBox densityCB = new JComboBox();
  FlowLayout flowLayout6 = new FlowLayout();
  JCheckBox densityCheckBox = new JCheckBox();
  JPanel agePanel = new JPanel();
  FlowLayout flowLayout7 = new FlowLayout();
  JCheckBox ageCheckBox = new JCheckBox();
  JPanel sizeClassPanel = new JPanel();
  JComboBox sizeClassCB = new JComboBox();
  FlowLayout flowLayout8 = new FlowLayout();
  JCheckBox sizeClassCheckBox = new JCheckBox();
  JPanel speciesPanel = new JPanel();
  JComboBox speciesCB = new JComboBox();
  FlowLayout flowLayout9 = new FlowLayout();
  JCheckBox speciesCheckBox = new JCheckBox();
  JPanel centerPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel resultsPanel = new JPanel();
  JPanel jPanel2 = new JPanel();
  FlowLayout flowLayout11 = new FlowLayout();
  JButton searchPB = new JButton();
  TitledBorder titledBorder1;
  JPanel actionPanel = new JPanel();
  FlowLayout flowLayout12 = new FlowLayout();
  TitledBorder resultsBorder;
  Border border1;
  TitledBorder titledBorder3;
  JTextField ageText = new JTextField();
  JScrollPane resultsScrollPane = new JScrollPane();
  JList resultsList = new JList();
  JPanel processActionPanel = new JPanel();
  JPanel treatmentActionPanel = new JPanel();
  FlowLayout flowLayout14 = new FlowLayout();
  FlowLayout flowLayout15 = new FlowLayout();
  JButton makeTreatmentPB = new JButton();
  JButton lockInProcessPB = new JButton();
  JPanel timeStepPanel = new JPanel();
  FlowLayout flowLayout13 = new FlowLayout();
  JLabel timeStepLabel = new JLabel();
  JComboBox timeStepCB = new JComboBox();
  JPanel processPanel = new JPanel();
  FlowLayout flowLayout16 = new FlowLayout();
  JCheckBox processCheckBox = new JCheckBox();
  JComboBox processCB = new JComboBox();
/**
 * Constructor for Evu Search.  Sets the frame owner, title, modality.  All of which reference the JDialog superclass. The final parameter is 
 * an Evu Analysis dialog
 * @param frame owner of the dialog
 * @param title name of the dialog
 * @param modal modality 
 * @param dlg the Evu Analysis dialog
 */
  public EvuSearch(Frame frame, String title, boolean modal, EvuAnalysis dlg) {
    super(frame, title, modal);
    try {
      jbInit();
      isOpen = true;
      evuAnalysisDlg = dlg;
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
/**
 * Overloaded Evu Search.  References primary constructor and passes null for owner empty string for title, false for modality and null for 
 * Evu Analysis Dialog.  
 */
  public EvuSearch() {
    this(null, "", false,null);
  }
  /**
   * Sets borders, panels, layouts, components, text, and listeners for Evu Search.  
   * @throws Exception
   */
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(178, 178, 178)),"Searching");
    resultsBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(178, 178, 178)),"Results");
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(178, 178, 178));
    titledBorder3 = new TitledBorder(border1,"Action");
    mainPanel.setLayout(borderLayout1);
    northPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(10);
    flowLayout1.setVgap(0);
    choicesPanel.setLayout(gridLayout1);
    gridLayout1.setRows(11);
    gridLayout1.setVgap(5);
    htGrpPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setVgap(0);
    roadStatusPanel.setLayout(flowLayout10);
    flowLayout10.setAlignment(FlowLayout.LEFT);
    flowLayout10.setVgap(0);
    roadStatusCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    roadStatusCheckBox.setText("Road Status         ");
    roadStatusCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        roadStatusCheckBox_actionPerformed(e);
      }
    });
    htGrpCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    htGrpCheckBox.setText("Ecological Grouping ");
    htGrpCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpCheckBox_actionPerformed(e);
      }
    });
    fmzPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setVgap(0);
    fmzCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    fmzCheckBox.setText("Fire Management Zone");
    fmzCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fmzCheckBox_actionPerformed(e);
      }
    });
    specialAreaPanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setVgap(0);
    specialAreaCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    specialAreaCheckBox.setText("Special Area        ");
    specialAreaCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        specialAreaCheckBox_actionPerformed(e);
      }
    });
    ownershipPanel.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setVgap(0);
    ownershipCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    ownershipCheckBox.setText("Ownership           ");
    ownershipCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ownershipCheckBox_actionPerformed(e);
      }
    });
    densityPanel.setLayout(flowLayout6);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout6.setVgap(0);
    densityCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    densityCheckBox.setText("Density             ");
    densityCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityCheckBox_actionPerformed(e);
      }
    });
    agePanel.setLayout(flowLayout7);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout7.setVgap(0);
    ageCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    ageCheckBox.setText("Age                 ");
    ageCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ageCheckBox_actionPerformed(e);
      }
    });
    sizeClassPanel.setLayout(flowLayout8);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    flowLayout8.setVgap(0);
    sizeClassCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    sizeClassCheckBox.setText("Size Class          ");
    sizeClassCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassCheckBox_actionPerformed(e);
      }
    });
    speciesPanel.setLayout(flowLayout9);
    flowLayout9.setAlignment(FlowLayout.LEFT);
    flowLayout9.setVgap(0);
    speciesCheckBox.setFont(new java.awt.Font("Monospaced", 0, 12));
    speciesCheckBox.setText("Species             ");
    speciesCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesCheckBox_actionPerformed(e);
      }
    });
    centerPanel.setLayout(borderLayout2);
    jPanel2.setLayout(flowLayout11);
    searchPB.setEnabled(false);
    searchPB.setText("Search");
    searchPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchPB_actionPerformed(e);
      }
    });
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    northPanel.setBorder(titledBorder1);
    resultsPanel.setLayout(flowLayout12);
    flowLayout12.setAlignment(FlowLayout.LEFT);
    flowLayout12.setHgap(3);
    flowLayout12.setVgap(0);
    actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
    resultsPanel.setBorder(resultsBorder);
    actionPanel.setBorder(titledBorder3);
    ageText.setEnabled(false);
    ageText.setText("1");
    ageText.setColumns(3);
    ageText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        ageText_keyTyped(e);
      }
    });
    resultsList.setToolTipText("double-click selection to display in Anaylsis Dialog");
    resultsList.setPrototypeCellValue(prototypeCellValue);
    resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    resultsList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        resultsList_mouseClicked(e);
      }
    });
    htGrpCB.setEnabled(false);
    speciesCB.setEnabled(false);
    sizeClassCB.setEnabled(false);
    densityCB.setEnabled(false);
    specialAreaCB.setEnabled(false);
    ownershipCB.setEnabled(false);
    fmzCB.setEnabled(false);
    roadStatusCB.setEnabled(false);
    treatmentActionPanel.setLayout(flowLayout14);
    flowLayout14.setAlignment(FlowLayout.LEFT);
    flowLayout14.setHgap(0);
    flowLayout14.setVgap(0);
    processActionPanel.setLayout(flowLayout15);
    flowLayout15.setAlignment(FlowLayout.LEFT);
    flowLayout15.setHgap(0);
    flowLayout15.setVgap(0);
    makeTreatmentPB.setEnabled(false);
    makeTreatmentPB.setToolTipText("Create a treatment which will apply to resulting units");
    makeTreatmentPB.setText("Make Treatment");
    makeTreatmentPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        makeTreatmentPB_actionPerformed(e);
      }
    });
    lockInProcessPB.setEnabled(false);
    lockInProcessPB.setToolTipText("Create a Lock-in Process for resulting units");
    lockInProcessPB.setText("Lock in Process");
    lockInProcessPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lockInProcessPB_actionPerformed(e);
      }
    });
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    timeStepPanel.setLayout(flowLayout13);
    flowLayout13.setAlignment(FlowLayout.LEFT);
    flowLayout13.setVgap(0);
    timeStepLabel.setEnabled(false);
    timeStepLabel.setText("Time Step");
    timeStepCB.setEnabled(false);
    processPanel.setLayout(flowLayout16);
    processCheckBox.setText("Process");
    processCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        processCheckBox_actionPerformed(e);
      }
    });
    processCB.setEnabled(false);
    flowLayout16.setAlignment(FlowLayout.LEFT);
    flowLayout16.setVgap(0);
    htGrpPanel.add(htGrpCheckBox, null);
    roadStatusPanel.add(roadStatusCheckBox, null);
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel,  BorderLayout.NORTH);
    northPanel.add(choicesPanel, null);
    northPanel.add(jPanel2, null);
    jPanel2.add(searchPB, null);
    htGrpPanel.add(htGrpCB, null);
    roadStatusPanel.add(roadStatusCB, null);
    fmzPanel.add(fmzCheckBox, null);
    fmzPanel.add(fmzCB, null);
    specialAreaPanel.add(specialAreaCheckBox, null);
    specialAreaPanel.add(specialAreaCB, null);
    ownershipPanel.add(ownershipCheckBox, null);
    ownershipPanel.add(ownershipCB, null);
    densityPanel.add(densityCheckBox, null);
    densityPanel.add(densityCB, null);

    choicesPanel.add(htGrpPanel, null);
    choicesPanel.add(timeStepPanel, null);
    choicesPanel.add(speciesPanel, null);
    choicesPanel.add(sizeClassPanel, null);
    choicesPanel.add(agePanel, null);
    choicesPanel.add(densityPanel, null);
    choicesPanel.add(specialAreaPanel, null);
    choicesPanel.add(ownershipPanel, null);
    choicesPanel.add(fmzPanel, null);
    choicesPanel.add(roadStatusPanel, null);
    choicesPanel.add(processPanel, null);

    processPanel.add(processCheckBox, null);
    processPanel.add(processCB, null);

    agePanel.add(ageCheckBox, null);
    agePanel.add(ageText, null);
    sizeClassPanel.add(sizeClassCheckBox, null);
    sizeClassPanel.add(sizeClassCB, null);
    speciesPanel.add(speciesCheckBox, null);
    speciesPanel.add(speciesCB, null);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(resultsPanel,  BorderLayout.NORTH);
    resultsPanel.add(resultsScrollPane, null);
    centerPanel.add(actionPanel,  BorderLayout.CENTER);
    actionPanel.add(treatmentActionPanel, null);
    actionPanel.add(processActionPanel, null);
    resultsScrollPane.getViewport().add(resultsList, null);
    treatmentActionPanel.add(makeTreatmentPB, null);
    processActionPanel.add(lockInProcessPB, null);
    timeStepPanel.add(timeStepLabel, null);
    timeStepPanel.add(timeStepCB, null);
  }
/**
 * Initializes the Evu Search.  Gets the current zone and area.  Loads all habitat type groups, species, size class, density, ownerships, special areas,
 * fire management zones, process types, and number time steps.  It then adds their items to combo boxes.  
 */
  private void initialize() {
    RegionalZone zone = Simpplle.getCurrentZone();
    Area         area = Simpplle.getCurrentArea();

    Vector v;
    int    i;

    v = HabitatTypeGroup.getAllLoadedTypes();
    for (i = 0; i < v.size(); i++) { htGrpCB.addItem(v.elementAt(i)); }

    v = zone.getAllSpecies();
    for (i=0; i<v.size(); i++) { speciesCB.addItem(v.elementAt(i)); }

    v = zone.getAllSizeClass();
    for (i=0; i<v.size(); i++) { sizeClassCB.addItem(v.elementAt(i)); }

    ageText.setText("1");

    v = zone.getAllDensity();
    for (i=0; i<v.size(); i++) { densityCB.addItem(v.elementAt(i)); }

    {
      ArrayList values = area.getAllOwnership();
      for (i = 0; i < values.size(); i++) {
        ownershipCB.addItem(values.get(i));
      }
    }
    {
      ArrayList values = area.getAllSpecialArea();
      for (i = 0; i < values.size(); i++) {
        specialAreaCB.addItem(values.get(i));
      }
    }

    Fmz[] fmzItems = zone.getAllFmz();
    for (i=0; i<fmzItems.length; i++) { fmzCB.addItem(fmzItems[i]); }

//    String[] roadStatusItems = Evu.getAllRoadStatus();
//    for (i=0; i<roadStatusItems.length; i++) { roadStatusCB.addItem(roadStatusItems[i]); }

    ProcessType[] processTypes = Process.getSummaryProcesses();
    for (i=0; i<processTypes.length; i++) {
      processCB.addItem(processTypes[i]);
    }

    v = null;

    Simulation simulation = Simpplle.getCurrentSimulation();
    int nSteps = (simulation != null) ? simulation.getNumTimeSteps() : 0;
    for (i=0; i<=nSteps; i++) { timeStepCB.addItem(new Integer(i)); }
    timeStepCB.setSelectedIndex(0);

    setSize(getPreferredSize());
    update(getGraphics());
  }
/**
 * Method to decipher the age input into age text field.  Consumes any keys that are not digits and beeps.  
 * @param e
 */
  void ageText_keyTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }
/**
 * Gets a vector of Evu's 
 * @return Evu vector
 */
  public Vector getUnits() { return units; }
/**
 * If units is null informs user of no Evu's found and disables the make treatment and lock in process buttons.  Otherwise it passes into 
 * results list the vector of Evu's then enables the treatment and lock in process buttons.  Reports back the total acres from all Evu's   
 */
  private void updateResults() {
    if (units == null) {
      resultsList.setVisible(false);
      makeTreatmentPB.setEnabled(false);
      lockInProcessPB.setEnabled(false);
      JOptionPane.showMessageDialog(this,"No units Found","No Units Found",
                                    JOptionPane.INFORMATION_MESSAGE);
      resultsBorder.setTitle("Results");
    }
    else {
      resultsList.setListData(units);
      resultsList.setVisible(true);
      makeTreatmentPB.setEnabled(true);
      lockInProcessPB.setEnabled(true);

      int acres=0;
      for (int i=0; i<units.size(); i++) {
        acres += ((Evu)units.elementAt(i)).getAcres();
      }
      acres = Math.round(Area.getFloatAcres(acres));
      resultsBorder.setTitle("Results (Total Acres " + Integer.toString(acres) + ")");

      setSize(getPreferredSize());
    }
    update(getGraphics());
  }
/**
 * Handles the search.  Gets the current area.  Creates a set of null objects for habitat type group, species, size class, density, ownership, special area,
 * fire management zone, and process type.  Then depending on whether one or more check boxes are selected sets the specified Evu attribute values.  
 * 
 * 
 * @param e 'Search'
 */
  void searchPB_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();

    HabitatTypeGroupType htGrp       = null;
    int                  timeStep    = 0;
    Species              species     = null;
    SizeClass            sizeClass   = null;
    int                  age         = -1;
    Density              density     = null;
    String               ownership   = null;
    String               specialArea = null;
    Fmz                  fmz         = null;
    int                  roadStatus  = -1;
    ProcessType          processType = null;

    if (htGrpCheckBox.isSelected()) {
      htGrp = (HabitatTypeGroupType)htGrpCB.getSelectedItem();
    }
    if (speciesCheckBox.isSelected() || sizeClassCheckBox.isSelected() ||
        ageCheckBox.isSelected()     || densityCheckBox.isSelected() ||
        processCheckBox.isSelected())
    {
      timeStep = ((Integer)timeStepCB.getSelectedItem()).intValue();
    }
    if (speciesCheckBox.isSelected()) {
      species = (Species)speciesCB.getSelectedItem();
    }
    if (sizeClassCheckBox.isSelected()) {
      sizeClass = (SizeClass)sizeClassCB.getSelectedItem();
    }
    if (ageCheckBox.isSelected()) {
      try {
        age = Integer.parseInt(ageText.getText());
      }
      catch (NumberFormatException err) {
        ageText.setText("1");
        age = 1;
      }
    }
    if (densityCheckBox.isSelected()) {
      density = (Density)densityCB.getSelectedItem();
    }
    if (ownershipCheckBox.isSelected()) {
      ownership = (String)ownershipCB.getSelectedItem();
    }
    if (specialAreaCheckBox.isSelected()) {
      specialArea = (String)specialAreaCB.getSelectedItem();
    }
    if (fmzCheckBox.isSelected()) {
      fmz = (Fmz)fmzCB.getSelectedItem();
    }
    if (roadStatusCheckBox.isSelected()) {
    }
    if (processCheckBox.isSelected()) {
      processType = (ProcessType)processCB.getSelectedItem();
    }

    evuAnalysisDlg.setResultUnits(new ArrayList(Arrays.asList(units)));
    updateResults();
  }
/**
 * Uses the results list to get Evu's and put them on Evu Analysis when mouse button pushed.  
 * @param e double mouse click
 */
  void resultsList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2 && evuAnalysisDlg != null) {
      Evu evu = (Evu)resultsList.getSelectedValue();
      evuAnalysisDlg.goUnit(evu);
    }
  }
/**
 * Enables the search button if any of the search criteria combo boxes are selected.  
 */
  private void updateSearchPBEnabled() {
    searchPB.setEnabled(htGrpCheckBox.isSelected() ||
                        speciesCheckBox.isSelected() ||
                        sizeClassCheckBox.isSelected() ||
                        ageCheckBox.isSelected() ||
                        densityCheckBox.isSelected() ||
                        ownershipCheckBox.isSelected() ||
                        specialAreaCheckBox.isSelected() ||
                        fmzCheckBox.isSelected() ||
                        roadStatusCheckBox.isSelected() ||
                        processCheckBox.isSelected());
  }
/**
 * Enables habitat type group combo box is habitat group check box is selected and enables search button.    
 * @param e habitat group check box selected
 */
  void htGrpCheckBox_actionPerformed(ActionEvent e) {
    htGrpCB.setEnabled(htGrpCheckBox.isSelected());
    updateSearchPBEnabled();
  }
/**
 * If any or all species, size class, age, density, or process checkboxes are selected enables the time step combo box.  
 */
  private void updateTimeStepControls() {
    boolean enabled = (speciesCheckBox.isSelected() ||
                       sizeClassCheckBox.isSelected() ||
                       ageCheckBox.isSelected() ||
                       densityCheckBox.isSelected() ||
                       processCheckBox.isSelected());

    timeStepLabel.setEnabled(enabled);
    timeStepCB.setEnabled(enabled);
  }
/**
 * Enables species combo box if species check box is selected.  Updates time step controls and enables search button.
 * @param e species check box checked
 */
  void speciesCheckBox_actionPerformed(ActionEvent e) {
    speciesCB.setEnabled(speciesCheckBox.isSelected());
    updateTimeStepControls();
    updateSearchPBEnabled();
  }
  /**
   * Enables size class combo box if size class check box is selected.  Updates time step controls and enables search button.
   * @param e size class check box checked
   */
  void sizeClassCheckBox_actionPerformed(ActionEvent e) {
    sizeClassCB.setEnabled(sizeClassCheckBox.isSelected());
    updateTimeStepControls();
    updateSearchPBEnabled();
  }

  /**
   * Enables age combo box if age check box is selected.  Updates time step controls and enables search button.
   * @param e age check box checked
   */
  void ageCheckBox_actionPerformed(ActionEvent e) {
    ageText.setEnabled(ageCheckBox.isSelected());
    updateTimeStepControls();
    updateSearchPBEnabled();
  }
  /**
   * Enables density combo box if density check box is selected.  Updates time step controls and enables search button.
   * @param e density check box checked
   */
  void densityCheckBox_actionPerformed(ActionEvent e) {
    densityCB.setEnabled(densityCheckBox.isSelected());
    updateTimeStepControls();
    updateSearchPBEnabled();
  }
  /**
   * Enables special area combo box if special area check box is selected.  Enables search button.
   * @param e special area check box checked
   */
  void specialAreaCheckBox_actionPerformed(ActionEvent e) {
    specialAreaCB.setEnabled(specialAreaCheckBox.isSelected());
    updateSearchPBEnabled();
  }
  /**
   * Enables ownership combo box if ownership check box is selected.  Enables search button.
   * @param e ownership check box checked
   */
  void ownershipCheckBox_actionPerformed(ActionEvent e) {
    ownershipCB.setEnabled(ownershipCheckBox.isSelected());
    updateSearchPBEnabled();
  }
  /**
   * Enables fire management zone combo box if fire management zone check box is selected.  Enables search button.
   * @param e fire management zone check box checked
   */
  void fmzCheckBox_actionPerformed(ActionEvent e) {
    fmzCB.setEnabled(fmzCheckBox.isSelected());
    updateSearchPBEnabled();
  }
  /**
   * Enables road status combo box if road status check box is selected.  Enables search button.
   * @param e road status check box checked
   */
  void roadStatusCheckBox_actionPerformed(ActionEvent e) {
    roadStatusCB.setEnabled(roadStatusCheckBox.isSelected());
    updateSearchPBEnabled();
  }
  /**
   * Enables process combo box if process check box is selected.  Enables search button.
   * @param e process check box checked
   */
  void processCheckBox_actionPerformed(ActionEvent e) {
    processCB.setEnabled(processCheckBox.isSelected());
    updateTimeStepControls();
    updateSearchPBEnabled();
  }
/**
 * Disposes of the dialog if window closing event occurs.
 * @param e 'X' window closing button pushed  
 */
  void this_windowClosing(WindowEvent e) {
    isOpen = false;
    setVisible(false);
    dispose();
  }
/**
 * Checks if Evu Search dialog is open.
 * @return true if Evu Search dialog is open
 */
  public static boolean isOpen() { return isOpen; }
/**
 * Makes a treatment if make treatment button pushed.  Gets the current area and its treatment schedule.  If there is no treatment scheduled, creates a new
 * treatment, else it creates a new treatment dialog.  Using the time step it gets the treatment type, schedule, and the treatment Evu ID's.
 * @param e 'Make Treatment'
 */
  void makeTreatmentPB_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();
    TreatmentSchedule schedule = area.getTreatmentSchedule();

    if (schedule == null) {
      schedule = area.createTreatmentSchedule();
    }

    Frame                theFrame = JSimpplle.getSimpplleMain();
    NewTreatment         dlg = new NewTreatment(theFrame,"Select a new Treatment",true);
    String               treatmentName;
    TreatmentType        treatType;
    TreatmentApplication treatment;

    dlg.setVisible(true);
    treatmentName = dlg.getSelection();

    int tStep = AskNumber.getInput("Pick a Time Step", "Time Step");
    if (tStep == -1) { return; }

    if (treatmentName != null) {
      treatType = TreatmentType.get(treatmentName);
      treatment = schedule.newTreatment(treatType,tStep);

      for (int i=0; i<units.size(); i++) {
        treatment.addUnitId( ((Evu)units.elementAt(i)).getId() );
      }
      treatment.setUseUnits(true);
      treatment.setTimeStep(tStep);

      String msg = "The Treatment " + treatmentName + " has been created\n" +
                   "Please see the Treatment Scheduler for further options";
      JOptionPane.showMessageDialog(this,msg,"Treatment Created",
                                    JOptionPane.INFORMATION_MESSAGE);
    }


  }
/**
 * Gets current area and process schedule, makes a List Selection Dialog with legal processes.  Then adds the process types to from the Evu Id's and sets the time step.  
 * @param e 'Lock in Process
 */
  void lockInProcessPB_actionPerformed(ActionEvent e) {
    Area               area = Simpplle.getCurrentArea();
    ProcessSchedule    schedule = area.getProcessSchedule();
    ProcessApplication processApp;

    if (schedule == null) {
      schedule = area.createProcessSchedule();
    }

    ProcessType         processType;
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;

    dlg = new ListSelectionDialog(theFrame,"Select a new Process",true,
                                  Process.getLegalProcesses());

    dlg.setVisible(true);
    processType = (ProcessType)dlg.getSelection();

    int tStep = AskNumber.getInput("Pick a Time Step", "Time Step");
    if (tStep == -1) { return; }

    if (processType != null) {
      processApp = schedule.newApplication(processType);

      for (int i=0; i<units.size(); i++) {
        processApp.addUnitId( ((Evu)units.elementAt(i)).getId() );
      }
      processApp.setTimeStep(tStep);

      String msg = "The Lock-in Process " + processType.toString() + " has been created\n" +
                   "Please see the Process Scheduler for further options";
      JOptionPane.showMessageDialog(this,msg,"Lock-in Process Created",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
  }

}




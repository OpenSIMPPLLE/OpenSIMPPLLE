/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import simpplle.comcode.BisonGrazing;
import java.awt.Dimension;
import java.io.File;
import simpplle.comcode.SystemKnowledge;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import simpplle.comcode.Species;
import simpplle.comcode.HabitatTypeGroup;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.BoxLayout;
import java.beans.*;
import java.awt.*;

/** 
 * This class defines Bison Grazing Logic Editor which allows the user to edit some attributes of Bison Grazing.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class BisonGrazingLogicEditor extends JDialog {
  BisonGrazingLogicDataModel speciesProbDataModel = new BisonGrazingLogicDataModel();
  BisonGrazingLogicDataModel initProbDataModel = new BisonGrazingLogicDataModel();
  BisonGrazingLogicDataModel waterAdjDataModel = new BisonGrazingLogicDataModel();
  BisonGrazingLogicDataModel grazingKindDataModel = new BisonGrazingLogicDataModel();

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel tablesPanel = new JPanel();
  JPanel grazingKindPanel = new JPanel();
  JPanel waterAdjustPanel = new JPanel();
  JPanel initProbPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();
  JScrollPane jScrollPane3 = new JScrollPane();
  JTable initProbTable = new JTable();
  JTable waterAdjustTable = new JTable();
  JTable grazingKindTable = new JTable();
  TitledBorder speciesProbBorder = new TitledBorder("Species Specific Probabilities");
  TitledBorder initProbBorder = new TitledBorder("Time Since Fire Table");
  TitledBorder waterAdjustBorder =
      new TitledBorder("Distance to Water Probablity Adjustment Table");
  TitledBorder grazingKindBorder = new TitledBorder("Percent Landscape Burned Table");
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileCloseDialog = new JMenuItem();
  JMenuItem menuFileLoadDefaults = new JMenuItem();
  JPanel infoPanel = new JPanel();
  JLabel infoLabel1 = new JLabel();
  JLabel infoLabel2 = new JLabel();
  JPanel speciesProbPanel = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JScrollPane jScrollPane4 = new JScrollPane();
  JTable speciesProbTable = new JTable();
  JPanel defaultProbPanel = new JPanel();
  JProbabilityTextField defaultProbTextField = new JProbabilityTextField(100);
  JLabel jLabel3 = new JLabel();
  JMenu jMenu1 = new JMenu();
  JMenuItem menuActionAddSpeciesEntry = new JMenuItem();
  JMenuItem menuActionRemoveSpeciesEntry = new JMenuItem();
  JPanel jPanel1 = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JProbabilityTextField initProbWeightTF = new JProbabilityTextField(0);
  JLabel initProbWeightLabel = new JLabel();
  JPanel initProbWeightLabelPanel = new JPanel();
  JLabel initProbWeightLabel2 = new JLabel();
  JLabel initProbWeightLabel3 = new JLabel();
  JPanel waterAdjustWeightPanel = new JPanel();
  JPanel grazingKindWeightPanel = new JPanel();
  JProbabilityTextField waterAdjustWeightTF = new JProbabilityTextField(0);
  JProbabilityTextField grazingKindWeightTF = new JProbabilityTextField(0);
  JPanel waterAdjustWeightLabelPanel = new JPanel();
  JPanel grazingKindWeightLabelPanel = new JPanel();
  JLabel waterAdjustWeightLabel = new JLabel();
  JLabel waterAdjustWeightLabel2 = new JLabel();
  JLabel waterAdjustWeightLabel3 = new JLabel();
  JLabel grazingKindWeightLabel = new JLabel();
  JLabel grazingKindWeightLabel2 = new JLabel();
  JLabel grazingKindWeightLabel3 = new JLabel();
  JPanel jPanel2 = new JPanel();
  JLabel defaultProbInfoLabel = new JLabel();
  
  /**
   * Primary constructor for BisonGrazing Logic Editor, takes in the owner frame, title and mode true or false
   * @param owner
   * @param title
   * @param modal
   */
  public BisonGrazingLogicEditor(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * overloaded Bison Grazing Logic Editor constructor.  References the currently owned frame, sets the title and turns off modality.  
 */
  public BisonGrazingLogicEditor() {
    this(new Frame(), "BisonGrazingLogicEditor", false);
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
    initProbPanel.setLayout(borderLayout2);
    waterAdjustPanel.setLayout(borderLayout3);
    grazingKindPanel.setLayout(borderLayout4);
    speciesProbPanel.setBorder(speciesProbBorder);
    initProbPanel.setBorder(initProbBorder);
    waterAdjustPanel.setBorder(waterAdjustBorder);
    grazingKindPanel.setBorder(grazingKindBorder);
    menuFile.setText("File");
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new
        BisonGrazingLogicEditor_menuFileSave_actionAdapter(this));
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new
        BisonGrazingLogicEditor_menuFileSaveAs_actionAdapter(this));
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new
        BisonGrazingLogicEditor_menuFileClose_actionAdapter(this));
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new
        BisonGrazingLogicEditor_menuFileOpen_actionAdapter(this));
    menuFileLoadDefaults.setText("Load Defaults");
    menuFileLoadDefaults.addActionListener(new
        BisonGrazingLogicEditor_menuFileLoadDefaults_actionAdapter(this));
    menuFileCloseDialog.setText("Close Dialog");
    menuFileCloseDialog.addActionListener(new
        BisonGrazingLogicEditor_menuFileCloseDialog_actionAdapter(this));
    this.addWindowListener(new BisonGrazingLogicEditor_this_windowAdapter(this));
    this.setJMenuBar(jMenuBar1);
    infoPanel.setLayout(new BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));
    infoLabel1.setText(
        "Grazing with the highest total percent will be assigned to the unit.");
    infoLabel2.setText(" ");
    speciesProbPanel.setLayout(borderLayout6);
    defaultProbPanel.setLayout(new BoxLayout(defaultProbPanel, javax.swing.BoxLayout.Y_AXIS));
    defaultProbTextField.setText("100");
    defaultProbTextField.addKeyListener(new
        BisonGrazingLogicEditor_defaultProbTextField_keyAdapter(this));
    defaultProbTextField.addActionListener(new
        BisonGrazingLogicEditor_defaultProbTextField_actionAdapter(this));
    defaultProbTextField.addFocusListener(new
        BisonGrazingLogicEditor_defaultProbTextField_focusAdapter(this));
    jLabel3.setText("Default Probability");
    jMenu1.setText("Action");
    menuActionAddSpeciesEntry.setText("Add Species Entry");
    menuActionAddSpeciesEntry.addActionListener(new
        BisonGrazingLogicEditor_menuActionAddSpeciesEntry_actionAdapter(this));
    menuActionRemoveSpeciesEntry.setText("Remove Species Entry");
    menuActionRemoveSpeciesEntry.addActionListener(new
        BisonGrazingLogicEditor_menuActionRemoveSpeciesEntry_actionAdapter(this));
    jPanel1.setLayout(flowLayout2);
    initProbWeightTF.setText("");
    initProbWeightTF.setColumns(5);
    initProbWeightTF.addKeyListener(new
        BisonGrazingLogicEditor_initProbWeightTF_keyAdapter(this));
    initProbWeightTF.addFocusListener(new
        BisonGrazingLogicEditor_initProbWeightTF_focusAdapter(this));
    initProbWeightTF.addActionListener(new
        BisonGrazingLogicEditor_initProbWeightTF_actionAdapter(this));
    initProbWeightLabel.setToolTipText("");
    initProbWeightLabel.setText(
        "This weight must be distributed to the three grazing intensities for each value of the component.");
    initProbWeightLabelPanel.setLayout(new BoxLayout(initProbWeightLabelPanel, javax.swing.BoxLayout.Y_AXIS));
    initProbWeightLabel2.setToolTipText("");
    initProbWeightLabel2.setText("Each row must total to the weight.");
    initProbWeightLabel3.setText("");
    waterAdjustWeightTF.setText("");
    waterAdjustWeightTF.setColumns(5);
    waterAdjustWeightTF.addKeyListener(new
        BisonGrazingLogicEditor_waterAdjustWeightTF_keyAdapter(this));
    waterAdjustWeightTF.addFocusListener(new
        BisonGrazingLogicEditor_waterAdjustWeightTF_focusAdapter(this));
    waterAdjustWeightTF.addActionListener(new
        BisonGrazingLogicEditor_waterAdjustWeightTF_actionAdapter(this));
    grazingKindWeightTF.setText("");
    grazingKindWeightTF.setColumns(5);
    grazingKindWeightTF.addKeyListener(new
        BisonGrazingLogicEditor_grazingKindWeightTF_keyAdapter(this));
    grazingKindWeightTF.addFocusListener(new
        BisonGrazingLogicEditor_grazingKindWeightTF_focusAdapter(this));
    grazingKindWeightTF.addActionListener(new
        BisonGrazingLogicEditor_grazingKindWeightTF_actionAdapter(this));
    grazingKindWeightLabelPanel.setLayout(new BoxLayout(grazingKindWeightLabelPanel, javax.swing.BoxLayout.Y_AXIS));
    waterAdjustWeightLabelPanel.setLayout(new BoxLayout(waterAdjustWeightLabelPanel, javax.swing.BoxLayout.Y_AXIS));
    waterAdjustWeightLabel.setText("This weight must be distributed to the three grazing intensities for each value of the component.");
    waterAdjustWeightLabel2.setText("Each row must total to the weight.");
    waterAdjustWeightLabel3.setText("");
    grazingKindWeightLabel.setText("This weight must be distributed to the three grazing intensities for each value of the component.");
    grazingKindWeightLabel2.setText("Each row must total to the weight.");
    grazingKindWeightLabel3.setText("");
    defaultProbInfoLabel.setText(
        "This probability will be used for all existing vegetation units unless " +
        "a different value is specified below.");
    getContentPane().add(mainPanel);
    jScrollPane1.getViewport().add(initProbTable);
    jScrollPane2.getViewport().add(waterAdjustTable);
    waterAdjustPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);
    waterAdjustPanel.add(waterAdjustWeightPanel, java.awt.BorderLayout.NORTH);
    waterAdjustWeightPanel.add(waterAdjustWeightTF);
    initProbPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
    initProbPanel.add(jPanel1, java.awt.BorderLayout.NORTH);
    jPanel1.add(initProbWeightTF);
    jPanel1.add(initProbWeightLabelPanel);
    initProbWeightLabelPanel.add(initProbWeightLabel);
    initProbWeightLabelPanel.add(initProbWeightLabel2);
    initProbWeightLabelPanel.add(initProbWeightLabel3);

    grazingKindPanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);
    jScrollPane3.getViewport().add(grazingKindTable);
    mainPanel.add(tablesPanel, java.awt.BorderLayout.CENTER);
    tablesPanel.add(defaultProbPanel);
    defaultProbPanel.add(jPanel2);
    defaultProbPanel.add(defaultProbInfoLabel);

    jPanel2.add(jLabel3);
    jPanel2.add(defaultProbTextField);
    tablesPanel.add(speciesProbPanel);
    jScrollPane4.getViewport().add(speciesProbTable);
    tablesPanel.add(initProbPanel, null);
    tablesPanel.add(waterAdjustPanel, null);
    tablesPanel.add(grazingKindPanel, null);
    tablesPanel.add(infoPanel);
    infoPanel.add(infoLabel1);
    infoPanel.add(infoLabel2);
    jMenuBar1.add(menuFile);
    jMenuBar1.add(jMenu1);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuFileLoadDefaults);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileCloseDialog);
    speciesProbPanel.add(jScrollPane4, java.awt.BorderLayout.CENTER);
    jMenu1.add(menuActionAddSpeciesEntry);
    jMenu1.add(menuActionRemoveSpeciesEntry);
    grazingKindPanel.add(grazingKindWeightPanel, java.awt.BorderLayout.NORTH);
    grazingKindWeightPanel.add(grazingKindWeightTF);
    grazingKindWeightPanel.add(grazingKindWeightLabelPanel);

    waterAdjustWeightLabelPanel.add(waterAdjustWeightLabel);
    waterAdjustWeightLabelPanel.add(waterAdjustWeightLabel2);
    waterAdjustWeightLabelPanel.add(waterAdjustWeightLabel3);
    waterAdjustWeightPanel.add(waterAdjustWeightLabelPanel);

    grazingKindWeightLabelPanel.add(grazingKindWeightLabel);
    grazingKindWeightLabelPanel.add(grazingKindWeightLabel2);
    grazingKindWeightLabelPanel.add(grazingKindWeightLabel3);
    speciesProbPanel.setPreferredSize(new Dimension(850, 200));
    initProbPanel.setPreferredSize(new Dimension(850, 150));
    waterAdjustPanel.setPreferredSize(new Dimension(850, 150));
    grazingKindPanel.setPreferredSize(new Dimension(850, 150));

    initProbWeightLabel3.setForeground(Color.red);
    waterAdjustWeightLabel3.setForeground(Color.red);
    grazingKindWeightLabel3.setForeground(Color.red);

    initProbWeightLabel3.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));
    waterAdjustWeightLabel3.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));
    grazingKindWeightLabel3.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));

    infoLabel2.setForeground(Color.red);
    infoLabel2.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));
  }
/**
 * Create instances of Bison Grazing Logic Data Model for Species, Fire History, Distance to Water, and Land Burned, sets the probability model for these
 * if at beginning of process pulls fire history weight, water distance weight, and land burned weight 
 */
  public void initialize() {
    speciesProbDataModel.setDataKind(BisonGrazing.DataKind.SPECIES_PROB);
    initProbDataModel.setDataKind(BisonGrazing.DataKind.FIRE_HIST);
    waterAdjDataModel.setDataKind(BisonGrazing.DataKind.WATER_DIST);
    grazingKindDataModel.setDataKind(BisonGrazing.DataKind.LAND_BURNED);

    speciesProbDataModel.setEditor(this);
    initProbDataModel.setEditor(this);
    waterAdjDataModel.setEditor(this);
    grazingKindDataModel.setEditor(this);

    speciesProbTable.setModel(speciesProbDataModel);
    initProbTable.setModel(initProbDataModel);
    waterAdjustTable.setModel(waterAdjDataModel);
    grazingKindTable.setModel(grazingKindDataModel);

    defaultProbTextField.setProbability(BisonGrazing.getDefaultProb());

    initProbTable.setRowSelectionAllowed(false);
    initProbTable.setColumnSelectionAllowed(false);

    waterAdjustTable.setRowSelectionAllowed(false);
    waterAdjustTable.setColumnSelectionAllowed(false);

    grazingKindTable.setRowSelectionAllowed(false);
    grazingKindTable.setColumnSelectionAllowed(false);

    menuFileSave.setEnabled(false);
    menuFileClose.setEnabled(false);
    menuActionRemoveSpeciesEntry.setEnabled(false);

    speciesProbTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    ListSelectionModel rowSM = speciesProbTable.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        menuActionRemoveSpeciesEntry.setEnabled(!lsm.isSelectionEmpty());
      }
    });
    defaultProbTextField.setProbability(BisonGrazing.getDefaultProb());

    if (Beans.isDesignTime() == false) {
      initProbWeightTF.setProbability(BisonGrazing.getFireHistWeight());
      waterAdjustWeightTF.setProbability(BisonGrazing.getWaterDistWeight());
      grazingKindWeightTF.setProbability(BisonGrazing.getLandBurnedWeight());
    }

    updateDialog();
  }
/**
 * Updates the JDialog - checks that Fire History, Water Distance, Land Burned weights = the total weight for each. 
 */
  private void updateDialog() {
    initProbDataModel.fireTableDataChanged();
    waterAdjDataModel.fireTableDataChanged();
    grazingKindDataModel.fireTableDataChanged();
    defaultProbTextField.setProbability(BisonGrazing.getDefaultProb());

    boolean matching = BisonGrazing.checkTotals(BisonGrazing.DataKind.FIRE_HIST);
    updateWeightMatchMessage(BisonGrazing.DataKind.FIRE_HIST,matching);

    matching = BisonGrazing.checkTotals(BisonGrazing.DataKind.WATER_DIST);
    updateWeightMatchMessage(BisonGrazing.DataKind.WATER_DIST,matching);

    matching = BisonGrazing.checkTotals(BisonGrazing.DataKind.LAND_BURNED);
    updateWeightMatchMessage(BisonGrazing.DataKind.LAND_BURNED,matching);

    updateWeightTotalsMessage();

    update(getGraphics());
  }
  /**
   * Handles the 'Open' file menu item.  Opens the bison grazing process probability logic file.   
   * @param e
   */
  void menuFileOpen_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this,SystemKnowledge.PROCESS_PROB_LOGIC,menuFileSave,menuFileClose);
    updateDialog();
  }
  /**
   * Handles the 'Save' file menu item.  Saves the bison grazing process probability logic file, if one exists.   
   * @param e
   */
  public void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.PROCESS_PROB_LOGIC);
    SystemKnowledgeFiler.saveFile(this, outfile, SystemKnowledge.PROCESS_PROB_LOGIC,
        menuFileSave, menuFileClose);
  }
  /**
   * Handles the 'Save As' file menu item.  Saves the bison grazing process probability logic file.   
   * @param e
   */
  public void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.PROCESS_PROB_LOGIC,
        menuFileSave, menuFileClose);
  }
/**
 * Checks to see if the process probability data is not null and has changed.  
 * 
 * @return true if a data has changed or been input.  
 */
  private boolean isSaveNeeded() {
    return ((SystemKnowledge.getFile(SystemKnowledge.PROCESS_PROB_LOGIC) != null) &&
            (SystemKnowledge.hasChangedOrUserData(SystemKnowledge.PROCESS_PROB_LOGIC)));
  }
/**
 * loads the menu defaults
 * @param e
 */
  public void menuFileLoadDefaults_actionPerformed(ActionEvent e) {
    loadDefaults();
  }
  /**
   * Loads the menu defaults if the menu close choose is selected
   * @param e
   */
  public void menuFileClose_actionPerformed(ActionEvent e) {
    loadDefaults();
  }
  /**
   * Loads the default Bison Grazing Logic for a particular zone.   
   */
  private void loadDefaults() {
    int choice;
    try {
      String msg = "This will load the default Bison Grazing Logic.\n\n" +
                   "Do you wish to continue?";
      String title = "Load Default Bison Grazing Logic";

      if (Utility.askYesNoQuestion(this,msg,title)) {
        SystemKnowledge.readZoneDefault(SystemKnowledge.PROCESS_PROB_LOGIC);
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
 * Quits the bison grazing logic editor if close is choosen from file menu.  
 * @param e "close" 
 */
  public void menuFileCloseDialog_actionPerformed(ActionEvent e) {
    quit();
  }
  /**
    * Quits the bison grazing logic editor if close is choosen from file menu.
   * @param e "quit"
   */
  public void this_windowClosing(WindowEvent e) {
    quit();
  }
  /**
   * Quits Bison Grazing Logic Editor by setting it not visible and disposing it. 
   */
  public void quit() {
    setVisible(false);
    dispose();
  }
/**
 * Sets the input probability text field to default bison grazing probability if it loses focus.
 * @param e loss of focus on default text field.
 */
  public void defaultProbTextField_focusLost(FocusEvent e) {
    BisonGrazing.setDefaultProb(defaultProbTextField.getProbability());
  }
/**
 * Sets the input probability text field to default bison grazing probability if text field is choosen.
 * @param e
 */
  public void defaultProbTextField_actionPerformed(ActionEvent e) {
    BisonGrazing.setDefaultProb(defaultProbTextField.getProbability());
  }
/**
 * Action event for adding species to the bison grazing logic within a JOption pane.  It presents all the valid species in the habitat type group 
 * to choose from.  It then adds a row to the species probability data model.  
 * @param e
 */
  public void menuActionAddSpeciesEntry_actionPerformed(ActionEvent e) {
    Vector v = HabitatTypeGroup.getValidSpecies();
    Species s;
    Species[] allSpecies = (Species[])v.toArray(new Species[v.size()]);

    Species species = (Species)JOptionPane.showInputDialog(null,
        "Choose a Species", "Choose a Species",
        JOptionPane.INFORMATION_MESSAGE, null,
        allSpecies, allSpecies[0]);
    if (species != null) {
      speciesProbDataModel.addRow(species);
    }
  }
/**
 * Removes a species in the selected row.  Will confirm with user via a JoptionPane whether 
 * definitely want to delete a species.
 * @param e
 */
  public void menuActionRemoveSpeciesEntry_actionPerformed(ActionEvent e) {
    int[] rows = speciesProbTable.getSelectedRows();

    String msg =
      "Delete Currently Selected Row(s)!\n\n" +
      "Are You Sure?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Delete Selected Row",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      speciesProbDataModel.deleteRows(rows);
      speciesProbTable.clearSelection();
    }
    update(getGraphics());
  }
/**
 * Gets the default probability from a text field.  
 * @param e
 */
  public void defaultProbTextField_keyTyped(KeyEvent e) {
    defaultProbTextField.localKeyTyped(e);
  }

/**
 * Updates the probability weight if it has changed  
 */
  public void initProbWeightChanged() {
    BisonGrazing.setFireHistWeight(initProbWeightTF.getProbability());

    boolean matching = BisonGrazing.checkTotals(BisonGrazing.DataKind.FIRE_HIST);
    updateWeightMatchMessage(BisonGrazing.DataKind.FIRE_HIST,matching);

    updateWeightTotalsMessage();
  }
  public void initProbWeightTF_actionPerformed(ActionEvent e) {
    initProbWeightChanged();
  }
  public void initProbWeightTF_focusLost(FocusEvent e) {
    initProbWeightChanged();
  }
/**
 * Updates distance to water weight with the probability entered into the text field, checks to ensure the water distance data total matches with water distance weight 
 */
  public void waterAdjustWeightChanged() {
    BisonGrazing.setWaterDistWeight(waterAdjustWeightTF.getProbability());

    boolean matching = BisonGrazing.checkTotals(BisonGrazing.DataKind.WATER_DIST);
    updateWeightMatchMessage(BisonGrazing.DataKind.WATER_DIST,matching);

    updateWeightTotalsMessage();
  }
  /**
   * Updates distance to water weight if an action event occurs
   * @param e
   */
  public void waterAdjustWeightTF_actionPerformed(ActionEvent e) {
    waterAdjustWeightChanged();
  }
  /**
   * Updates water distance weight if text field loses focus like a tab away
   * @param e
   */
  public void waterAdjustWeightTF_focusLost(FocusEvent e) {
    waterAdjustWeightChanged();
  }
/**
 * Sets the land burned weight, check totals, and 
 * updates weight totals matching and weight totals messages.  (are matching totals ok.
 */
  public void grazingKindWeightChanged() {
    BisonGrazing.setLandBurnedWeight(grazingKindWeightTF.getProbability());

    boolean matching = BisonGrazing.checkTotals(BisonGrazing.DataKind.LAND_BURNED);
    updateWeightMatchMessage(BisonGrazing.DataKind.LAND_BURNED,matching);

    updateWeightTotalsMessage();
  }
  /**
   * changes the grazing kind weight from the text field if an action event occurs
   * @param e
   */
  public void grazingKindWeightTF_actionPerformed(ActionEvent e) {
    grazingKindWeightChanged();
  }
  /**
   * changes the grazing kind wieght if focus is lost from text field
   * @param e
   */
  public void grazingKindWeightTF_focusLost(FocusEvent e) {
    grazingKindWeightChanged();
  }
/**
 * calls to BisonGrazing in comcode package and checks to make sure the weights of
 * fire history weight + water distance weight + land burned weight = 100 
 */
  public void updateWeightTotalsMessage() {
    String msg = (BisonGrazing.checkWeightTotals()) ? "" :
                 "Weights do not total 100";
    infoLabel2.setText(msg);
  }
  /**
   * informs user if table values do not equal to weight for each of Fire history, water distance, and land burned
   * @param kind
   * @param matching
   */
  public void updateWeightMatchMessage(BisonGrazing.DataKind kind, boolean matching) {
    String msg = (matching) ? "" : "Table values do not match Weight!";
    switch (kind) {
      case FIRE_HIST:
        initProbWeightLabel3.setText(msg);
        break;
      case WATER_DIST:
        waterAdjustWeightLabel3.setText(msg);
        break;
      case LAND_BURNED:
        grazingKindWeightLabel3.setText(msg);
        break;
    }
  }
/**
 * sets the probability weight by typing into a text field
 * @param e - key typed in text field
 */
  public void initProbWeightTF_keyTyped(KeyEvent e) {
    initProbWeightTF.localKeyTyped(e);
  }
/**
  * sets the water distance probability weight by typing into a text field
  * @param e - key typed in text field
 */
  public void waterAdjustWeightTF_keyTyped(KeyEvent e) {
    waterAdjustWeightTF.localKeyTyped(e);
  }
/**
 * sets the grazing kind probability weight by typing into a text field
 * @param e - key typed in text field
 */
  public void grazingKindWeightTF_keyTyped(KeyEvent e) {
    grazingKindWeightTF.localKeyTyped(e);
  }
}
/**
 * Sets the bison grazing logic editor adaptee
 * 
 */
class BisonGrazingLogicEditor_grazingKindWeightTF_focusAdapter extends
    FocusAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_grazingKindWeightTF_focusAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }
/**
 * if grazing kind weight loses focus
 */
  public void focusLost(FocusEvent e) {
    adaptee.grazingKindWeightTF_focusLost(e);
  }
}

class BisonGrazingLogicEditor_waterAdjustWeightTF_focusAdapter extends
    FocusAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_waterAdjustWeightTF_focusAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void focusLost(FocusEvent e) {
    adaptee.waterAdjustWeightTF_focusLost(e);
  }
}

class BisonGrazingLogicEditor_initProbWeightTF_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_initProbWeightTF_actionAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.initProbWeightTF_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_waterAdjustWeightTF_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_waterAdjustWeightTF_actionAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.waterAdjustWeightTF_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_grazingKindWeightTF_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_grazingKindWeightTF_actionAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.grazingKindWeightTF_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_grazingKindWeightTF_keyAdapter extends KeyAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_grazingKindWeightTF_keyAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void keyTyped(KeyEvent e) {
    adaptee.grazingKindWeightTF_keyTyped(e);
  }
}

class BisonGrazingLogicEditor_initProbWeightTF_focusAdapter extends
    FocusAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_initProbWeightTF_focusAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void focusLost(FocusEvent e) {
    adaptee.initProbWeightTF_focusLost(e);
  }
}

class BisonGrazingLogicEditor_waterAdjustWeightTF_keyAdapter extends KeyAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_waterAdjustWeightTF_keyAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void keyTyped(KeyEvent e) {
    adaptee.waterAdjustWeightTF_keyTyped(e);
  }
}

class BisonGrazingLogicEditor_initProbWeightTF_keyAdapter extends KeyAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_initProbWeightTF_keyAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void keyTyped(KeyEvent e) {
    adaptee.initProbWeightTF_keyTyped(e);
  }
}

class BisonGrazingLogicEditor_menuActionRemoveSpeciesEntry_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuActionRemoveSpeciesEntry_actionAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionRemoveSpeciesEntry_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_menuActionAddSpeciesEntry_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuActionAddSpeciesEntry_actionAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionAddSpeciesEntry_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_defaultProbTextField_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_defaultProbTextField_actionAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.defaultProbTextField_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_defaultProbTextField_focusAdapter extends
    FocusAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_defaultProbTextField_focusAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void focusLost(FocusEvent e) {
    adaptee.defaultProbTextField_focusLost(e);
  }
}

class BisonGrazingLogicEditor_defaultProbTextField_keyAdapter extends
    KeyAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_defaultProbTextField_keyAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void keyTyped(KeyEvent e) {
    adaptee.defaultProbTextField_keyTyped(e);
  }
}

class BisonGrazingLogicEditor_this_windowAdapter extends WindowAdapter {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_this_windowAdapter(BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}

class BisonGrazingLogicEditor_menuFileCloseDialog_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuFileCloseDialog_actionAdapter(
      BisonGrazingLogicEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileCloseDialog_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_menuFileSaveAs_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuFileSaveAs_actionAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSaveAs_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_menuFileSave_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuFileSave_actionAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileSave_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_menuFileClose_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuFileClose_actionAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileClose_actionPerformed(e);
  }
}

class BisonGrazingLogicEditor_menuFileOpen_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuFileOpen_actionAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileOpen_actionPerformed(e);
  }
}
class BisonGrazingLogicEditor_menuFileLoadDefaults_actionAdapter implements
    ActionListener {
  private BisonGrazingLogicEditor adaptee;
  BisonGrazingLogicEditor_menuFileLoadDefaults_actionAdapter(BisonGrazingLogicEditor
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuFileLoadDefaults_actionPerformed(e);
  }
}


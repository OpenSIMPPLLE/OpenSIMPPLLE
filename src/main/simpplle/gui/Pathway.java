/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.LtaValleySegmentGroup;
import simpplle.comcode.Simpplle;
import simpplle.comcode.Area;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.VegetativeType;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Process;
import simpplle.comcode.Species;
import simpplle.comcode.SystemKnowledge;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import simpplle.comcode.*;
import java.util.*;
import java.util.Arrays;
import java.util.List;

/**
 * The pathway dialog allows users to edit pathways. Each pathway group is in individual pathway
 * containing multiple states. Changes between these states occur over time based on the current
 * process applied to the species or aquatic class. This dialog enables users to load, save, edit,
 * and analyze pathways.
 */

public class Pathway extends JDialog {

  /**
   * The current grouping
   */
  private String pathwayGroup;

  /**
   * The current species
   */
  private Species species;

  /**
   * The current process
   */
  private String process;

  /**
   * Blocks the eco group, species, and process combo boxes from responding to changes
   */
  private boolean inInit = true;

  /**
   * Flag indicating if the pathways are for aquatic states
   */
  private boolean aquaticsMode = false;

  /**
   * The process that the last modified arrow represented
   */
  private Process savedArrowProcess;

  /**
   * The state that the last modified arrow pointed to
   */
  private VegetativeType savedArrowNextState;

  /**
   * The state that the last modified arrow pointed from
   */
  private VegetativeType savedArrowState;

  private PathwayCanvas canvas = new PathwayCanvas();
  private JMenuItem menuFileOpen;
  private JMenuItem menuFileOpenOld;
  private JMenuItem menuFileSave;
  private JMenuItem menuFileSaveAs;
  private JMenuItem menuFileUnloadPathway;
  private JMenuItem menuFileLoadPathway;
  private JMenuItem menuFileLoadAllPathway;
  private JMenuItem menuFileQuit;
  private JMenuItem menuExportPathway;
  private JMenuItem menuExportCoordinates;
  private JMenuItem menuExportHabitatTypeGroups;
  private JMenuItem menuExportVegetativeTypes;
  private JMenuItem menuImportPathway;
  private JMenuItem menuImportSpeciesChange;
  private JMenuItem menuImportSpeciesInclusion;
  private JMenuItem menuImportCoordinates;
  private JMenuItem menuImportHabitatTypeGroups;
  private JMenuItem menuImportVegetativeTypes;
  private JMenuItem menuEditUndoArrow;
  private JMenuItem menuPathwayPosition;
  private JMenuItem menuPathwayPositionAll;
  private JMenuItem menuPathwayCollapseAll;
  private JMenuItem menuPathwayDetailAll;
  private JMenuItem menuPathwayStepCounter;
  private JMenuItem menuPathwaysNewState;
  private JCheckBoxMenuItem menuPathwayShowGridLines;
  private JCheckBoxMenuItem menuPathwayShowAllLabels;
  private JMenuItem menuGroupSetYearlyLifeforms;
  private JMenuItem menuKnowledgeSourceDisplay;
  private JPanel infoPanel = new JPanel();
  private JPanel labelPanel = new JPanel();
  private JPanel mainPanel = new JPanel();
  private JPanel northPanel = new JPanel();
  private JPanel pathwayGroupPanel = new JPanel();
  private JPanel processPanel = new JPanel();
  private JPanel selectionPanel = new JPanel();
  private JPanel speciesPanel = new JPanel();
  private JPanel textPanel = new JPanel();
  private JLabel zoneLabel = new JLabel();
  private JLabel zoneText = new JLabel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private GridLayout gridLayout1 = new GridLayout();
  private GridLayout gridLayout2 = new GridLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private FlowLayout flowLayout2 = new FlowLayout();
  private FlowLayout flowLayout3 = new FlowLayout();
  private FlowLayout flowLayout4 = new FlowLayout();
  private FlowLayout flowLayout5 = new FlowLayout();
  private TitledBorder pathwayGroupBorder;
  private TitledBorder processBorder;
  private TitledBorder speciesBorder;
  private JComboBox pathwayGroupCB = new JComboBox();
  private JComboBox processCB = new JComboBox();
  private JComboBox speciesCB = new JComboBox();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private StepCounter stepCounterDialog;

  /**
   * Creates a non-modal dialog for editing either aquatic or vegetative pathways.
   *
   * @param frame the owner dialog from which the dialog is displayed
   * @param aquatic true if editing aquatic pathways
   */
  public Pathway(Frame frame, boolean aquatic) {

    super(frame, "Pathways", false);

    try  {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }

    this.aquaticsMode = aquatic;

    initialize();

  }

  /**
   * Populates the dialog with user interface elements.
   *
   * @throws Exception if initialization fails
   */
  void jbInit() throws Exception {

    menuFileOpen = new JMenuItem("Open");
    menuFileOpen.addActionListener(this::open);

    menuFileOpenOld = new JMenuItem("Open Old Format File");
    menuFileOpenOld.addActionListener(this::openOldFormatFile);

    menuFileSave = new JMenuItem("Save");
    menuFileSave.addActionListener(this::save);

    menuFileSaveAs = new JMenuItem("Save As…");
    menuFileSaveAs.addActionListener(this::saveAs);

    menuFileUnloadPathway = new JMenuItem("Unload Pathway…");
    menuFileUnloadPathway.addActionListener(this::unloadPathway);

    menuFileLoadPathway = new JMenuItem("Load Default Pathway…");
    menuFileLoadPathway.addActionListener(this::loadDefaultPathway);

    menuFileLoadAllPathway = new JMenuItem("Load All Default Pathways");
    menuFileLoadAllPathway.addActionListener(this::loadAllDefaultPathways);

    menuFileQuit = new JMenuItem("Close");
    menuFileQuit.addActionListener(this::closeDialog);

    menuExportPathway = new JMenuItem("Pathway Text File");
    menuExportPathway.addActionListener(this::exportPathway);

    menuExportCoordinates = new JMenuItem("Coordinate Table");
    menuExportCoordinates.addActionListener(this::exportCoordinateTable);

    menuExportHabitatTypeGroups = new JMenuItem("Habitat Type Group Table");
    menuExportHabitatTypeGroups.addActionListener(this::exportHabitatTypeGroupTable);

    menuExportVegetativeTypes = new JMenuItem("Vegetative Type Table");
    menuExportVegetativeTypes.addActionListener(this::exportVegetativeTypeTable);

    menuImportPathway = new JMenuItem("Pathway Text File");
    menuImportPathway.addActionListener(this::importPathway);

    menuImportSpeciesChange = new JMenuItem("Species Change");
    menuImportSpeciesChange.addActionListener(this::importSpeciesChange);

    menuImportSpeciesInclusion = new JMenuItem("Species Inclusion");
    menuImportSpeciesInclusion.addActionListener(this::importSpeciesInclusion);

    menuImportCoordinates = new JMenuItem("Coordinate Table");
    menuImportCoordinates.addActionListener(this::importCoordinateTable);

    menuImportHabitatTypeGroups = new JMenuItem("Habitat Type Group Table");
    menuImportHabitatTypeGroups.addActionListener(this::importHabitatTypeGroupTable);

    menuImportVegetativeTypes = new JMenuItem("Vegetative Type Table");
    menuImportVegetativeTypes.addActionListener(this::importVegetativeTypeTable);

    menuPathwaysNewState = new JMenuItem("Set State…");
    menuPathwaysNewState.addActionListener(this::newState);

    menuPathwayPosition = new JMenuItem("Auto Position States…");
    menuPathwayPosition.addActionListener(this::autoPositionStates);

    menuPathwayPositionAll = new JMenuItem("Auto Position All States…");
    menuPathwayPositionAll.addActionListener(this::autoPositionAllStates);

    menuPathwayShowAllLabels = new JCheckBoxMenuItem("Show All Labels");
    menuPathwayShowAllLabels.addActionListener(this::toggleShowAllLabels);

    menuPathwayShowGridLines = new JCheckBoxMenuItem("Show Grid Lines");
    menuPathwayShowGridLines.addActionListener(this::toggleShowGridLines);

    menuPathwayCollapseAll = new JMenuItem("Collapse All");
    menuPathwayCollapseAll.addActionListener(this::collapseAll);

    menuPathwayDetailAll = new JMenuItem("Detail All");
    menuPathwayDetailAll.addActionListener(this::detailAll);

    menuPathwayStepCounter = new JMenuItem("Step Counter");
    menuPathwayStepCounter.addActionListener(this::openStepCounter);

    menuGroupSetYearlyLifeforms = new JMenuItem("Set Yearly Lifeforms");
    menuGroupSetYearlyLifeforms.addActionListener(this::setLifeformYearlyStatus);

    menuEditUndoArrow = new JMenuItem("Undo Last Arrow Move");
    menuEditUndoArrow.addActionListener(this::undoLastArrowMove);
    menuEditUndoArrow.setEnabled(false);

    menuKnowledgeSourceDisplay = new JMenuItem("Display");
    menuKnowledgeSourceDisplay.addActionListener(this::displayKnowledgeSource);

    JMenu menuImport = new JMenu("Import");
    menuImport.add(menuImportPathway);
    menuImport.add(menuImportSpeciesChange);
    menuImport.add(menuImportSpeciesInclusion);
    menuImport.add(menuImportCoordinates);
    menuImport.add(menuImportHabitatTypeGroups);
    menuImport.add(menuImportVegetativeTypes);

    JMenu menuExport = new JMenu("Export");
    menuExport.add(menuExportPathway);
    menuExport.add(menuExportCoordinates);
    menuExport.add(menuExportHabitatTypeGroups);
    menuExport.add(menuExportVegetativeTypes);

    JMenu menuFile = new JMenu("File");
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileOpenOld);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuImport);
    menuFile.add(menuExport);
    menuFile.addSeparator();
    menuFile.add(menuFileUnloadPathway);
    menuFile.add(menuFileLoadPathway);
    menuFile.add(menuFileLoadAllPathway);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);

    JMenu menuEdit = new JMenu("Edit");
    menuEdit.add(menuEditUndoArrow);

    JMenu menuPathways = new JMenu("Pathways");
    menuPathways.add(menuPathwaysNewState);
    menuPathways.addSeparator();
    menuPathways.add(menuPathwayPosition);
    menuPathways.add(menuPathwayPositionAll);
    menuPathways.addSeparator();
    menuPathways.add(menuPathwayCollapseAll);
    menuPathways.add(menuPathwayDetailAll);
    menuPathways.addSeparator();
    menuPathways.add(menuPathwayShowAllLabels);
    menuPathways.add(menuPathwayShowGridLines);
    menuPathways.addSeparator();
    menuPathways.add(menuPathwayStepCounter);

    JMenu menuGroup = new JMenu("Group");
    menuGroup.add(menuGroupSetYearlyLifeforms);

    JMenu menuKnowledgeSource = new JMenu("Knowledge Source");
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(menuFile);
    menuBar.add(menuEdit);
    menuBar.add(menuPathways);
    menuBar.add(menuGroup);
    menuBar.add(menuKnowledgeSource);

    jScrollPane1.setPreferredSize(new Dimension(800, 500));
    jScrollPane1.getViewport().add(canvas, null);

    pathwayGroupCB.addActionListener(this::selectPathway);
    pathwayGroupBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Ecological Grouping");
    pathwayGroupBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    pathwayGroupPanel.setBorder(pathwayGroupBorder);
    pathwayGroupPanel.setMinimumSize(new Dimension(146, 58));
    pathwayGroupPanel.setPreferredSize(new Dimension(146, 58));
    pathwayGroupPanel.setLayout(flowLayout3);
    pathwayGroupPanel.add(pathwayGroupCB, null);

    speciesCB.addActionListener(this::selectSpecies);
    speciesBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Species");
    speciesBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    speciesPanel.setBorder(speciesBorder);
    speciesPanel.setLayout(flowLayout4);
    speciesPanel.add(speciesCB, null);

    processCB.addActionListener(this::selectProcess);
    processBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Process");
    processBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    processPanel.setBorder(processBorder);
    processPanel.setLayout(flowLayout5);
    processPanel.add(processCB, null);

    selectionPanel.setLayout(flowLayout2);
    selectionPanel.add(pathwayGroupPanel, null);
    selectionPanel.add(speciesPanel, null);
    selectionPanel.add(processPanel, null);

    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(10);
    flowLayout2.setAlignment(FlowLayout.LEFT);

    zoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    zoneLabel.setText("Current Zone");

    labelPanel.setLayout(gridLayout1);
    labelPanel.add(zoneLabel, null);

    zoneText.setForeground(Color.blue);
    zoneText.setText("EASTSIDE REGION ONE");

    textPanel.setLayout(gridLayout2);
    textPanel.add(zoneText, null);

    infoPanel.setLayout(flowLayout1);
    infoPanel.add(labelPanel, null);
    infoPanel.add(textPanel, null);

    northPanel.setBorder(BorderFactory.createEtchedBorder());
    northPanel.setLayout(borderLayout2);
    northPanel.add(infoPanel, BorderLayout.NORTH);
    northPanel.add(selectionPanel, BorderLayout.SOUTH);

    mainPanel.setLayout(borderLayout1);
    mainPanel.setMinimumSize(new Dimension(200, 200));
    mainPanel.add(jScrollPane1, BorderLayout.CENTER);
    mainPanel.add(northPanel, BorderLayout.NORTH);

    setTitle("Vegetative Pathways");
    setJMenuBar(menuBar);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    getContentPane().add(mainPanel);

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
  }

  /**
   * Initializes the user interface. Initialization involves clearing the current eco group,
   * species, and process, displaying the zone name, and adjusting the names of the combo boxes
   * based on the type of pathway being edited.
   */
  private void initialize() {

    species      = null;
    pathwayGroup = null;
    process      = null;

    canvas.setPathwayDlg(this);

    zoneText.setText(Simpplle.getCurrentZone().getName());

    if (aquaticsMode) {
      pathwayGroupBorder.setTitle("LTA Valley Segment Group");
      speciesBorder.setTitle("Aquatic Class");
      menuGroupSetYearlyLifeforms.setVisible(false);
    } else {
      pathwayGroupBorder.setTitle("Ecological Grouping");
      speciesBorder.setTitle("Species");
      menuGroupSetYearlyLifeforms.setVisible(true);
    }

    updateDialog();

  }

  /**
   * Updates the dialog by toggling visibility of menu items, updating combo box contents, clearing
   * undo history, setting the canvas view based on the combo box selection, and painting the
   * dialog.
   */
  public void updateDialog() {

    inInit = true;

    // Get the names of loaded groups
    String[] groups;
    if (aquaticsMode) {
      groups = LtaValleySegmentGroup.getLoadedGroupNames();
    } else {
      groups = HabitatTypeGroup.getLoadedGroupNames();
    }

    if (groups == null) {

      // Remove all combo box items
      pathwayGroupCB.removeAllItems();
      speciesCB.removeAllItems();
      processCB.removeAllItems();

      // Disable the combo boxes
      pathwayGroupCB.setEnabled(false);
      speciesCB.setEnabled(false);
      processCB.setEnabled(false);

      // Disable saving and loading
      menuFileSave.setEnabled(false);
      menuFileSaveAs.setEnabled(false);
      menuFileUnloadPathway.setEnabled(false);
      menuFileLoadAllPathway.setEnabled(false);
      menuFileLoadPathway.setEnabled(false);
      menuExportPathway.setEnabled(false);
      menuExportCoordinates.setEnabled(false);
      menuExportHabitatTypeGroups.setEnabled(false);
      menuExportVegetativeTypes.setEnabled(false);

      // Disable state manipulation
      menuPathwaysNewState.setEnabled(false);
      menuPathwayPositionAll.setEnabled(false);
      menuPathwayPosition.setEnabled(false);

      return;

    } else {

      // Enable the combo boxes
      pathwayGroupCB.setEnabled(true);
      speciesCB.setEnabled(true);
      processCB.setEnabled(true);

      // Enable saving and loading
      menuFileSaveAs.setEnabled(true);
      menuFileUnloadPathway.setEnabled(true);
      menuFileLoadAllPathway.setEnabled(true);
      menuFileLoadPathway.setEnabled(true);
      menuExportPathway.setEnabled(true);
      menuExportCoordinates.setEnabled(true);
      menuExportHabitatTypeGroups.setEnabled(true);
      menuExportVegetativeTypes.setEnabled(true);

      // Enable state manipulation
      menuPathwaysNewState.setEnabled(true);
      menuPathwayPositionAll.setEnabled(true);
      menuPathwayPosition.setEnabled(true);

    }

    // Sort the groups alphabetically
    Arrays.sort(groups);

    // Populate combo box with groups
    String currentGroup = pathwayGroup;
    pathwayGroup = groups[0];
    pathwayGroupCB.removeAllItems();
    for (String group : groups) {
      if (group.equals(currentGroup)) {
        pathwayGroup = currentGroup;
      }
      pathwayGroupCB.addItem(group);
    }
    pathwayGroupCB.setSelectedItem(pathwayGroup);

    // Populate combo box with species
    Species currentSpecies = species;
    Species[] allSpecies = HabitatTypeGroup.findInstance(pathwayGroup).getAllSpecies();
    speciesCB.removeAllItems();
    if (allSpecies.length > 0) {
      species = allSpecies[0];
      for (Species s : allSpecies) {
        if (s == currentSpecies) {
          species = s;
        }
        speciesCB.addItem(s);
      }
      speciesCB.setSelectedItem(species);
    }

    // Populate combo box with processes
    String currentProcess = process;
    String[] allProcess = HabitatTypeGroup.findInstance(pathwayGroup).getAllProcesses(species);
    process = ProcessType.SUCCESSION.toString();
    processCB.removeAllItems();
    for (String p : allProcess) {
      if (p.equals(currentProcess)) {
        process = p;
      }
      processCB.addItem(p);
    }
    processCB.setSelectedItem(process);

    inInit = false;

    // Update the canvas
    canvas.setHtGrp(pathwayGroup);
    canvas.setSpecies(species);
    canvas.setProcess(process);
    canvas.refreshDiagram();

    // Clear the undo history
    savedArrowState = null;
    savedArrowProcess = null;
    savedArrowNextState = null;
    menuEditUndoArrow.setEnabled(false);

    // Enable saving if the group has changed and has an associated file
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    if (group != null && group.getFilename() != null && group.hasChanged()) {
      menuFileSave.setEnabled(true);
    } else {
      menuFileSave.setEnabled(false);
    }

    // Redraw the dialog
    update(getGraphics());

  }

  /**
   * Prompts the user to decide if simulation data should be deleted.
   *
   * @return true if the user doesn't need the data
   */
  private boolean deleteSimulationCheck() {

    String msg = "An area is loaded that has simulation data.\n" +
                 "If an invalid vegetative state or ecological grouping is created \n" +
                 "as a result of loading a new pathway file,\n" +
                 "then the state will be marked as invalid, and a result the area as well.\n" +
                 "** In addition all simulation data will be removed from memory. **\n\n" +
                 "Do you wish to continue?\n";

    int choice = JOptionPane.showConfirmDialog(this,msg,
                                               "Warning: Simulation Data Exists",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.WARNING_MESSAGE);

    if (choice == JOptionPane.NO_OPTION) {
      return true;
    } else {
      Simpplle.resetSimulation();
      return false;
    }
  }

  /**
   * Marks the current area as invalid if any vegetation units are invalid. If this is the case, a
   * message is displayed to the user describing how to fix the units.
   */
  private void doInvalidAreaCheck() {
    Area area = Simpplle.getCurrentArea();
    if (area.existAnyInvalidVegUnits()) {
      String msg = "Invalid states were created as a result of loading the new pathway\n" +
                   "file. In addition any simulation data that may have existed has\n" +
                   "been erased from memory.\n" +
                   "The area can be made valid again by either running the Unit Editor\n" +
                   "found under the Utilities menu of the main application window, or\n" +
                   "by loading a pathway file that contains the missing states.\n";
      JOptionPane.showMessageDialog(this,msg,
                                    "Invalid units found",
                                    JOptionPane.INFORMATION_MESSAGE);
      JSimpplle.getSimpplleMain().markAreaInvalid();
    } else {
      JSimpplle.getSimpplleMain().markAreaValid();
    }
  }

  public PathwayCanvas getPathwayCanvas() {
    return canvas;
  }

  public void setSpecies(VegetativeType veg) {
    species = veg.getSpecies();
    updateDialog();
  }

  public void setSpeciesAndProcess(VegetativeType veg, Process p) {
    species = veg.getSpecies();
    process = p.toString();
    updateDialog();
  }

  public void saveArrowChange(VegetativeType state, Process p, VegetativeType nextState) {
    savedArrowState = state;
    savedArrowProcess = p;
    savedArrowNextState = nextState;
    menuEditUndoArrow.setEnabled(true);
  }

  private void quit () {
    JSimpplle.getSimpplleMain().setVegPathwayDlgClosed();
    setVisible(false);
    try {
      dispose();
      System.out.println("Console print");
    } catch (NullPointerException ex) {
      System.out.print(ex.getStackTrace());
    }
  }

  private void open(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    if (SystemKnowledgeFiler.openFile(this,SystemKnowledge.VEGETATION_PATHWAYS,menuFileSave, null)) {
      HabitatTypeGroup newGroup = SystemKnowledge.getLastVegetativePathwayLoaded();
      pathwayGroupCB.setSelectedItem(newGroup.getName());

      updateDialog();
      update(getGraphics());

      // Update the units and check for invalid ones.
      if (area != null) {
        area.updatePathwayData();
        doInvalidAreaCheck();
      }
    }
  }

  private void openOldFormatFile(ActionEvent e) {

    Area area = Simpplle.getCurrentArea();
    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    setCursor(Utility.getWaitCursor());

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setFileFilter(new MyFileFilter("pathway","Pathway Data Files (*.pathway)"));
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setDialogTitle("Select a Pathway Data File");

    int choice = chooser.showOpenDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      HabitatTypeGroup group;
      try {
        group = Simpplle.getCurrentZone().loadPathway(chooser.getSelectedFile());
        pathwayGroupCB.setSelectedItem(group.getName());
        updateDialog();
        update(getGraphics());
        if (area != null) {
          area.updatePathwayData();
          doInvalidAreaCheck();
        }
      } catch (SimpplleError error) {
        JOptionPane.showMessageDialog(this, error.getError(),
                                      "Error Loading File",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }

    setCursor(Utility.getNormalCursor());

  }

  private void importPathway(ActionEvent e) {

    setCursor(Utility.getWaitCursor());

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setAcceptAllFileFilterUsed(true);
    chooser.setMultiSelectionEnabled(true);
    chooser.setDialogTitle("Select Pathway Files");

    int choice = chooser.showOpenDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      for (File file : chooser.getSelectedFiles()) {
        try {
          HabitatTypeGroup group = Simpplle.getCurrentZone().importPathway(file);
          pathwayGroupCB.setSelectedItem(group.getName());
          updateDialog();
          Area area = Simpplle.getCurrentArea();
          if (area != null) {
            area.updatePathwayData();
            doInvalidAreaCheck();
          }
        } catch (SimpplleError error) {
          JOptionPane.showMessageDialog(this, error.getError(),
              "Error Importing Files",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    }

    setCursor(Utility.getNormalCursor());

  }

  private void importSpeciesChange(ActionEvent e) {

    setCursor(Utility.getWaitCursor());

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setAcceptAllFileFilterUsed(true);
    chooser.setDialogTitle("Select a Species Change File");

    int choice = chooser.showOpenDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      try {
        HabitatTypeGroup.importSpeciesChangeFile(chooser.getSelectedFile());
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Error Loading File",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }

    setCursor(Utility.getNormalCursor());

  }

  private void importSpeciesInclusion(ActionEvent e) {

    setCursor(Utility.getWaitCursor());

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setAcceptAllFileFilterUsed(true);
    chooser.setDialogTitle("Select a Species Inclusion File");

    int choice = chooser.showOpenDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      try {
        HabitatTypeGroup.importInclusionFile(chooser.getSelectedFile());
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Error Loading File",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }

    setCursor(Utility.getNormalCursor());

  }

  private void importCoordinateTable(ActionEvent e) {

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setFileFilter(new FileNameExtensionFilter("CSV Tables","csv"));
    chooser.setDialogTitle("Import Coordinate Table");

    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      try {
        HabitatTypeGroup.importCoordinateTable(chooser.getSelectedFile());
      } catch (SimpplleError error) {
        JOptionPane.showMessageDialog(this,
                                      error.getMessage(),
                                      "Error Importing Coordinates",
                                      JOptionPane.ERROR_MESSAGE);
      }

      canvas.refreshDiagram();

      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }
  }

  private void importHabitatTypeGroupTable(ActionEvent e) {

    int clear = JOptionPane.showConfirmDialog(this,
                                              "Existing pathways will be cleared. Continue?",
                                              "Clear Existing Pathways",
                                              JOptionPane.YES_NO_OPTION,
                                              JOptionPane.INFORMATION_MESSAGE);

    if (clear == JOptionPane.YES_OPTION) {

      JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
      chooser.setFileFilter(new FileNameExtensionFilter("CSV Tables","csv"));
      chooser.setDialogTitle("Import Habitat Type Group Table");

      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        HabitatTypeGroup.clearGroups();

        try {
          HabitatTypeGroup.importHabitatTypeGroupTable(chooser.getSelectedFile());
        } catch (SimpplleError error) {
          JOptionPane.showMessageDialog(this,
                                        error.getMessage(),
                                        "Error Importing Habitat Type Groups",
                                        JOptionPane.ERROR_MESSAGE);
        }

        updateDialog();

        canvas.refreshDiagram();

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

      }
    }
  }

  private void importVegetativeTypeTable(ActionEvent e) {

  }

  private void exportPathway(ActionEvent e) {

    ListSelectionDialog selectDlg = new ListSelectionDialog(JSimpplle.getSimpplleMain(),
                                                            "Select Ecological Groupings",
                                                            true,
                                                            HabitatTypeGroup.getLoadedGroupNames(),
                                                            true);
    selectDlg.setLocation(getLocation());
    selectDlg.setVisible(true);
    Object[] objects = selectDlg.getSelections();

    List<String> names = new ArrayList<>();
    for (Object object : objects) {
      names.add((String) object);
    }
    if (names.isEmpty()) return;

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setDialogTitle("Select Output Directory");

    int choice = chooser.showOpenDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      File directory = chooser.getSelectedFile();
      if (directory == null) return;

      setCursor(Utility.getWaitCursor());

      for (String name : names) {
        try {
          HabitatTypeGroup group = HabitatTypeGroup.findInstance(name);
          File file = new File(directory.getAbsolutePath() + File.separatorChar + name + ".txt");
          group.export(file);
        } catch (SimpplleError error) {
          JOptionPane.showMessageDialog(this, error.getMessage(),
                                        "Error Exporting Pathway",
                                        JOptionPane.ERROR_MESSAGE);
        }
      }

      setCursor(Utility.getNormalCursor());

    }
  }

  private void exportCoordinateTable(ActionEvent e) {

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setFileFilter(new FileNameExtensionFilter("CSV Tables","csv"));
    chooser.setDialogTitle("Export Coordinate Table");

    int option = chooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      try {
        File file = chooser.getSelectedFile();
        if (!file.getAbsolutePath().toLowerCase().endsWith(".csv")) {
          file = new File(file.getAbsolutePath() + ".csv");
        }
        HabitatTypeGroup.exportCoordinateTable(file);
      } catch (SimpplleError error) {
        JOptionPane.showMessageDialog(this,
                                      error.getMessage(),
                                      "Error exporting coordinates",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  private void exportHabitatTypeGroupTable(ActionEvent e) {

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setFileFilter(new FileNameExtensionFilter("CSV Tables","csv"));
    chooser.setDialogTitle("Export Habitat Type Group Table");

    int option = chooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      try {
        File file = chooser.getSelectedFile();
        if (!file.getAbsolutePath().toLowerCase().endsWith(".csv")) {
          file = new File(file.getAbsolutePath() + ".csv");
        }
        HabitatTypeGroup.exportHabitatTypeGroupTable(file);
      } catch (SimpplleError error) {
        JOptionPane.showMessageDialog(this,
                                      error.getMessage(),
                                      "Error exporting ecological groupings",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  private void exportVegetativeTypeTable(ActionEvent e) {

  }

  private void unloadPathway(ActionEvent e) {

    Area area = Simpplle.getCurrentArea();
    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) return;
    }

    ListSelectionDialog dlg = new ListSelectionDialog(JSimpplle.getSimpplleMain(),
                                                      "Select an Ecological Grouping",
                                                      true,
                                                      HabitatTypeGroup.getLoadedGroupNames());
    dlg.setLocation(getLocation());
    dlg.setVisible(true);

    String result = (String) dlg.getSelection();
    if (result == null) return;

    setCursor(Utility.getWaitCursor());

    try {
      RegionalZone zone = Simpplle.getCurrentZone();
      zone.removePathway(result);
    } catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),
                                    "Error Unloading Pathway",
                                    JOptionPane.ERROR_MESSAGE);
    }

    setCursor(Utility.getNormalCursor());

    pathwayGroup = null;
    species = null;
    process = null;

    updateDialog();
    update(getGraphics());

    if (area != null) {
      area.updatePathwayData();
      doInvalidAreaCheck();
    }
  }

  private void save(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    File outfile = group.getFilename();
    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.VEGETATION_PATHWAYS,
                                  menuFileSave,null,group);
    update(getGraphics());
  }

  private void saveAs(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.VEGETATION_PATHWAYS,
                                  menuFileSave, null, group);
    update(getGraphics());
  }

  private void closeDialog(ActionEvent e) {
    quit();
  }

  private void loadDefaultPathway(ActionEvent e) {

    Area area = Simpplle.getCurrentArea();
    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    ListSelectionDialog dlg = new ListSelectionDialog(JSimpplle.getSimpplleMain(),
                                                      "Select an Ecological Grouping",
                                                      true,
                                                      HabitatTypeGroup.getLoadedGroupNames());
    dlg.setLocation(getLocation());
    dlg.setVisible(true);

    String groupName = (String) dlg.getSelection();
    if (groupName != null) {
      try {
        RegionalZone zone = Simpplle.getCurrentZone();
        zone.loadPathway(groupName);
        pathwayGroupCB.setSelectedItem(groupName);
      } catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),
                                      "Error Loading Pathway",
                                      JOptionPane.ERROR_MESSAGE);
      }

      if (groupName.equals(pathwayGroup)) {
        species = null;
        process = null;
      }

      if (area != null) {
        area.updatePathwayData();
        doInvalidAreaCheck();
      }
    }

    updateDialog();

  }

  private void loadAllDefaultPathways(ActionEvent e) {

    Area area = Simpplle.getCurrentArea();
    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) return;
    }

    setCursor(Utility.getWaitCursor());

    try {
      RegionalZone zone = Simpplle.getCurrentZone();
      zone.loadAllPathways();
    } catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),
                                    "Error Loading Pathways",
                                    JOptionPane.ERROR_MESSAGE);
    }

    setCursor(Utility.getNormalCursor());

    pathwayGroup = null;
    species = null;
    process = null;

    if (area != null) {
      area.updatePathwayData();
      doInvalidAreaCheck();
    }

    updateDialog();

  }

  private void undoLastArrowMove(ActionEvent e) {
    if (savedArrowState == null || savedArrowProcess == null || savedArrowNextState == null) {
      return;
    }
    savedArrowState.setProcessNextState(savedArrowProcess,savedArrowNextState);
    savedArrowState = null;
    savedArrowProcess = null;
    savedArrowNextState = null;
    menuEditUndoArrow.setEnabled(false);
  }

  private void newState(ActionEvent e) {

    PathwayNewState dlg = new PathwayNewState(JSimpplle.getSimpplleMain(),
                                              "Create a New State",
                                              true, pathwayGroup, species);
    dlg.setLocation(getLocation());
    dlg.setVisible(true);

    updateDialog();

  }

  private void setLifeformYearlyStatus(ActionEvent e) {

    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    Lifeform[] values = group.getYearlyPathwayLifeforms();
    ArrayList<Lifeform> yearlyLives;
    if (values != null) {
      yearlyLives = new ArrayList<>(Arrays.asList(values));
    } else {
      yearlyLives = new ArrayList<>();
    }

    String title = "Yearly Pathway Lifeforms";
    ArrayList<Lifeform> lives = new ArrayList<>(Arrays.asList(Lifeform.getAllValues()));
    CheckBoxChooser dlg = new CheckBoxChooser(this,title,true,lives,yearlyLives);
    dlg.setVisible(true);
    group.setYearlyPathwayLifeforms(dlg.getChosenItems());

  }

  private void toggleShowAllLabels(ActionEvent e) {
    canvas.toggleShowAllLabels();
    canvas.refreshDiagram();
  }

  private void collapseAll(ActionEvent e) {
    CollapsedPathwayShape.collapseAll(canvas.getShapeHashtable());
    update(getGraphics());
  }

  private void detailAll(ActionEvent e) {
    CollapsedPathwayShape.detailAll(canvas.getShapeHashtable());
  }

  private void openStepCounter(ActionEvent e) {
    if (stepCounterDialog == null) {
      stepCounterDialog = new StepCounter(this);
    }
    stepCounterDialog.setVisible(true);
  }

  private void toggleShowGridLines(ActionEvent e) {
    if (menuPathwayShowGridLines.getState()) {
      canvas.showGridLines();
    } else {
      canvas.hideGridLines();
    }
    canvas.repaint();
  }

  private void displayKnowledgeSource(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    if (group != null) {
      String str = group.getKnowledgeSource();
      String title = "Knowledge Source for " + group.toString();
      KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
      dlg.setVisible(true);
      String newKnowledge = dlg.getText();
      if (newKnowledge != null) {
        group.setKnowledgeSource(newKnowledge);
      }
    }
  }

  private void autoPositionStates(ActionEvent e) {
    String msg = "This will position all visible states.\n\nContinue?";
    int choice = JOptionPane.showConfirmDialog(this, msg,
                                               "Auto Position States",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
      group.autoPositionSpecies(species);
      canvas.refreshDiagram();
      update(getGraphics());
    }
  }

  private void autoPositionAllStates(ActionEvent e) {
    String msg = "This will position all states in the current ecological grouping.\n\nContinue?";
    int choice = JOptionPane.showConfirmDialog(this, msg,
                                               "Auto Position All States",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
      group.autoPositionAllSpecies();
      canvas.refreshDiagram();
      update(getGraphics());
    }
  }

  private void selectPathway(ActionEvent e) {
    if (!inInit) {
      String result = (String) pathwayGroupCB.getSelectedItem();
      if (result != null) {
        pathwayGroup = result;
      }
      updateDialog();
    }
  }

  private void selectSpecies(ActionEvent e) {
    if (!inInit) {
      Species result = (Species) speciesCB.getSelectedItem();
      if (result != null) {
        species = result;
      }
      updateDialog();
    }
  }

  private void selectProcess(ActionEvent e) {
    if (!inInit) {
      String result = (String) processCB.getSelectedItem();
      if (result != null) {
        process = result;
      }
      updateDialog();
    }
  }

  private void this_windowClosing(WindowEvent e) {
    quit();
  }
}







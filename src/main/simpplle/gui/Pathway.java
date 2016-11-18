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
import simpplle.comcode.*;
import java.util.*;
import java.util.Arrays; //Added to sort the string array for ecological grouping (pathwayGroupCB)
import java.util.List;

/**
 * This class creates the Pathway dialog.  It allows users to open, create, and edit vegetative pathways.
 * The title of this dialog is "Vegetative Pathways".
 * Pathways are constructed as Trees have changes based on a decade time between states, and shrubs and herbaceous have yearly time changes.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class Pathway extends JDialog {

  /**
   * The current ecological grouping
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
  private JPanel mainPanel = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JMenuBar jMenuBar1 = new JMenuBar();
  private JMenu menuFile = new JMenu();
  private JMenuItem menuFileOpen = new JMenuItem();
  private JMenuItem menuFileQuit = new JMenuItem();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JMenu menuPathways = new JMenu();
  private JMenuItem menuLoadAllPathway = new JMenuItem();
  private JMenuItem menuLoadPathway = new JMenuItem();
  private JMenuItem menuFileSaveAs = new JMenuItem();
  private JMenuItem menuFileSave = new JMenuItem();
  private JMenuItem menuFileUnloadPathway = new JMenuItem();
  private JMenuItem menuFileImport = new JMenuItem();
  private JMenuItem menuFileExport = new JMenuItem();
  private JMenuItem menuPathwaysNewState = new JMenuItem();
  private JMenuItem menuPathwayCollapseAll = new JMenuItem();
  private JMenuItem menuPathwayDetailAll = new JMenuItem();
  private JMenuItem menuPathwayStepCounter = new JMenuItem();
  private JCheckBoxMenuItem menuPathwayGridLines = new JCheckBoxMenuItem();
  private JPanel northPanel = new JPanel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JLabel zoneLabel = new JLabel();
  private JLabel zoneText = new JLabel();
  private JPanel textPanel = new JPanel();
  private JPanel labelPanel = new JPanel();
  private GridLayout gridLayout2 = new GridLayout();
  private GridLayout gridLayout1 = new GridLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JPanel infoPanel = new JPanel();
  private JPanel selectionPanel = new JPanel();
  private FlowLayout flowLayout2 = new FlowLayout();
  private JPanel processPanel = new JPanel();
  private JPanel speciesPanel = new JPanel();
  private JPanel pathwayGroupPanel = new JPanel();
  private JComboBox pathwayGroupCB = new JComboBox();
  private JComboBox speciesCB = new JComboBox();
  private JComboBox processCB = new JComboBox();
  private Border border1;
  private TitledBorder pathwayGroupBorder;
  private Border border2;
  private TitledBorder speciesBorder;
  private Border border3;
  private TitledBorder processBorder;
  private FlowLayout flowLayout3 = new FlowLayout();
  private FlowLayout flowLayout4 = new FlowLayout();
  private FlowLayout flowLayout5 = new FlowLayout();
  private JMenuItem autoPositionStates = new JMenuItem();
  private JMenu menuKnowledgeSource = new JMenu();
  private JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
  private JMenuItem autoPositionAllStates = new JMenuItem();
  private JCheckBoxMenuItem menuPathwayShowAllLabels = new JCheckBoxMenuItem();
  private JMenuItem menuFileOldFormat = new JMenuItem();
  private StepCounter stepCounterDialog;
  private JMenu jMenu1 = new JMenu();
  private JMenuItem menuEditUndoArrow = new JMenuItem();
  private JMenuItem menuFileSpeciesChange = new JMenuItem();
  private JMenuItem menuFileSpeciesInclusion = new JMenuItem();
  private JMenuItem menuPathwaySetLifeformYearlyStatus = new JMenuItem();
  private JMenu menuGroup = new JMenu();

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
   * Populates the dialog with user interface elements. This code was generated by Borland JBuilder.
   *
   * @throws Exception if initialization fails
   */
  void jbInit() throws Exception {
    border1 = BorderFactory.createLineBorder(SystemColor.controlText,1);
    pathwayGroupBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Ecological Grouping");
    border2 = BorderFactory.createEmptyBorder();
    speciesBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Species");
    border3 = BorderFactory.createLineBorder(Color.black,1);
    processBorder = new TitledBorder(border3,"Process");
    mainPanel.setLayout(borderLayout1);
    mainPanel.setMinimumSize(new Dimension(200, 200));
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
    this.setTitle("Vegetative Pathways");
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.setJMenuBar(jMenuBar1);
    menuPathways.setText("Pathways");
    menuGroup.setText("Group");
    jScrollPane1.setPreferredSize(new Dimension(800, 500));
    menuFileUnloadPathway.setText("Unload Pathway ...");
    menuFileUnloadPathway.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileUnloadPathway_actionPerformed(e);
      }
    });
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuFileSaveAs.setText("SaveAs");
    menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSaveAs_actionPerformed(e);
      }
    });
    menuLoadPathway.setText("Load Default Pathway ...");
    menuLoadPathway.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLoadPathway_actionPerformed(e);
      }
    });
    menuLoadAllPathway.setText("Load All Default Pathways");
    menuLoadAllPathway.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLoadAllPathway_actionPerformed(e);
      }
    });
    menuFileImport.setText("Import Pathway Text File");
    menuFileImport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileImport_actionPerformed(e);
      }
    });
    menuFileExport.setText("Export Pathway(s) to Text File ...");
    menuFileExport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileExport_actionPerformed(e);
      }
    });
    menuPathwaysNewState.setText("New State ...");
    menuPathwaysNewState.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPathwaysNewState_actionPerformed(e);
      }
    });
    menuPathwayCollapseAll.setText("Collapse All");
    menuPathwayCollapseAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
                    menuPathwayCollapseAll_actionPerformed(e);
      }
    });
    menuPathwayDetailAll.setText("Detail All");
    menuPathwayDetailAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
                    menuPathwayDetailAll_actionPerformed(e);
      }
    });
    menuPathwayStepCounter.setText("Step Counter");
    menuPathwayStepCounter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
                    menuPathwayStepCounter_actionPerformed(e);
      }
    });
    menuPathwayGridLines.setText("Grid Lines");
    menuPathwayGridLines.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
                    menuPathwayGridLines_actionPerformed(e);
      }
    });
    menuPathwaySetLifeformYearlyStatus.setText("Set Yearly Lifeforms");
    menuPathwaySetLifeformYearlyStatus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPathwaySetLifeformYearlyStatus_actionPerformed(e);
      }
    });
    northPanel.setLayout(borderLayout2);
    zoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    zoneLabel.setText("Current Zone");
    zoneText.setForeground(Color.blue);
    zoneText.setText("EASTSIDE REGION ONE");
    textPanel.setLayout(gridLayout2);
    labelPanel.setLayout(gridLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(10);
    infoPanel.setLayout(flowLayout1);
    northPanel.setBorder(BorderFactory.createEtchedBorder());
    selectionPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    pathwayGroupPanel.setBorder(pathwayGroupBorder);
    pathwayGroupPanel.setMinimumSize(new Dimension(146, 58));
    pathwayGroupPanel.setPreferredSize(new Dimension(146, 58));
    pathwayGroupPanel.setLayout(flowLayout3);
    speciesPanel.setBorder(speciesBorder);
    speciesPanel.setLayout(flowLayout4);
    pathwayGroupBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    speciesBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    processPanel.setBorder(processBorder);
    processPanel.setLayout(flowLayout5);
    processBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    pathwayGroupCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pathwayGroupCB_actionPerformed(e);
      }
    });
    speciesCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesCB_actionPerformed(e);
      }
    });
    processCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        processCB_actionPerformed(e);
      }
    });
    autoPositionStates.setText("Auto Position States ...");
    autoPositionStates.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        autoPositionStates_actionPerformed(e);
      }
    });
    menuKnowledgeSource.setText("Knowledge Source");
    menuKnowledgeSourceDisplay.setText("Display");
    menuKnowledgeSourceDisplay.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuKnowledgeSourceDisplay_actionPerformed(e);
      }
    });
    autoPositionAllStates.setText("Auto Position All States ...");
    autoPositionAllStates.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        autoPositionAllStates_actionPerformed(e);
      }
    });
    menuPathwayShowAllLabels.setText("Show All Labels");
    menuPathwayShowAllLabels.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPathwayShowAllLabels_actionPerformed(e);
      }
    });
    menuFileOldFormat.setText("Open Old Format File");
    menuFileOldFormat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOldFormat_actionPerformed(e);
      }
    });
    jMenu1.setText("Edit");
    menuEditUndoArrow.setEnabled(false);
    menuEditUndoArrow.setText("Undo Last Arrow Move");
    menuEditUndoArrow.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuEditUndoArrow_actionPerformed(e);
      }
    });
    menuFileSpeciesChange.setText("Import Species Change File");
    menuFileSpeciesChange.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSpeciesChange_actionPerformed(e);
      }
    });
    menuFileSpeciesInclusion.setText("Import Species Inclusion File");
    menuFileSpeciesInclusion.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSpeciesInclusion_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    jMenuBar1.add(menuFile);
    jMenuBar1.add(jMenu1);
    jMenuBar1.add(menuPathways);
    jMenuBar1.add(menuGroup);
    jMenuBar1.add(menuKnowledgeSource);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileUnloadPathway);
    menuFile.addSeparator();
    menuFile.add(menuFileOldFormat);
    menuFile.addSeparator();
    menuFile.add(menuFileImport);
    menuFile.add(menuFileExport);
    menuFile.add(menuFileSpeciesChange);
    menuFile.add(menuFileSpeciesInclusion);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuLoadPathway);
    menuFile.add(menuLoadAllPathway);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    mainPanel.add(jScrollPane1, BorderLayout.CENTER);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(infoPanel, BorderLayout.NORTH);
    infoPanel.add(labelPanel, null);
    labelPanel.add(zoneLabel, null);
    infoPanel.add(textPanel, null);
    textPanel.add(zoneText, null);
    northPanel.add(selectionPanel, BorderLayout.SOUTH);
    selectionPanel.add(pathwayGroupPanel, null);
    pathwayGroupPanel.add(pathwayGroupCB, null);
    selectionPanel.add(speciesPanel, null);
    speciesPanel.add(speciesCB, null);
    selectionPanel.add(processPanel, null);
    processPanel.add(processCB, null);
    jScrollPane1.getViewport().add(canvas, null);
    menuPathways.add(menuPathwaysNewState);
    menuPathways.add(autoPositionStates);
    menuPathways.add(autoPositionAllStates);
    menuPathways.addSeparator();
    menuPathways.add(menuPathwayShowAllLabels);
    menuPathways.add(menuPathwayCollapseAll);
    menuPathways.add(menuPathwayDetailAll);
    menuPathways.add(menuPathwayStepCounter);
    menuPathways.add(menuPathwayGridLines);
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);
    menuGroup.add(menuPathwaySetLifeformYearlyStatus);
    jMenu1.add(menuEditUndoArrow);
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
      menuPathwaySetLifeformYearlyStatus.setVisible(false);
    } else {
      pathwayGroupBorder.setTitle("Ecological Grouping");
      speciesBorder.setTitle("Species");
      menuPathwaySetLifeformYearlyStatus.setVisible(true);
    }

    updateDialog();

  }

  public PathwayCanvas getPathwayCanvas() {
    return canvas;
  }

  /**
   * Updates the dialog by toggling visibility of menu items, updating combo box contents, clearing
   * undo history, setting the canvas view based on the combo box selection, and painting the
   * dialog.
   */
  public void updateDialog() {

    inInit = true;

    String[] groups;
    if (aquaticsMode) {
      groups = LtaValleySegmentGroup.getLoadedGroupNames();
    } else {
      groups = HabitatTypeGroup.getLoadedGroupNames();
    }

    if (groups == null) {

      pathwayGroupCB.removeAllItems();
      speciesCB.removeAllItems();
      processCB.removeAllItems();

      pathwayGroupCB.setEnabled(false);
      speciesCB.setEnabled(false);
      processCB.setEnabled(false);
      menuFileSave.setEnabled(false);
      menuFileSaveAs.setEnabled(false);
      menuFileUnloadPathway.setEnabled(false);
      menuLoadAllPathway.setEnabled(false);
      menuLoadPathway.setEnabled(false);
      menuFileExport.setEnabled(false);
      menuPathwaysNewState.setEnabled(false);
      autoPositionAllStates.setEnabled(false);
      autoPositionStates.setEnabled(false);

      return;

    } else {

      pathwayGroupCB.setEnabled(true);
      speciesCB.setEnabled(true);
      processCB.setEnabled(true);
      menuFileSaveAs.setEnabled(true);
      menuFileUnloadPathway.setEnabled(true);
      menuLoadAllPathway.setEnabled(true);
      menuLoadPathway.setEnabled(true);
      menuFileExport.setEnabled(true);
      menuPathwaysNewState.setEnabled(true);
      autoPositionAllStates.setEnabled(true);
      autoPositionStates.setEnabled(true);

    }

    Arrays.sort(groups);

    if (pathwayGroup == null) {
      pathwayGroup = groups[0];
    }
    pathwayGroupCB.removeAllItems();
    for (String group : groups) {
      pathwayGroupCB.addItem(group);
    }
    pathwayGroupCB.setSelectedItem(pathwayGroup);

    speciesCB.removeAllItems();
    Species[] allSpecies = HabitatTypeGroup.findInstance(pathwayGroup).getAllSpecies();
    Species currentSpecies = species;
    species = allSpecies[0];
    for (Species s : allSpecies) {
      if (s == currentSpecies) {
        species = s;
      }
      speciesCB.addItem(s);
    }
    speciesCB.setSelectedItem(species);

    processCB.removeAllItems();
    String[] allProcess = HabitatTypeGroup.findInstance(pathwayGroup).getAllProcesses(species);
    String currentProcess = process;
    process = ProcessType.SUCCESSION.toString();
    for (String p : allProcess) {
      if (p.equals(currentProcess)) {
        process = p;
      }
      processCB.addItem(p);
    }
    processCB.setSelectedItem(process);

    inInit = false;

    canvas.setHtGrp(pathwayGroup);
    canvas.setSpecies(species);
    canvas.setProcess(process);
    canvas.refreshDiagram();

    savedArrowState     = null;
    savedArrowProcess   = null;
    savedArrowNextState = null;
    menuEditUndoArrow.setEnabled(false);

    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    if (group != null && group.getFilename() != null && group.hasChanged()) {
      menuFileSave.setEnabled(true);
    } else {
      menuFileSave.setEnabled(false);
    }

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

  private void menuFileOpen_actionPerformed(ActionEvent e) {
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

  private void menuFileOldFormat_actionPerformed(ActionEvent e) {

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

  private void menuFileSpeciesChange_actionPerformed(ActionEvent e) {

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

  private void menuFileSpeciesInclusion_actionPerformed(ActionEvent e) {

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

  private void menuFileImport_actionPerformed(ActionEvent e) {

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

  private void menuFileExport_actionPerformed(ActionEvent e) {

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

  private void menuFileUnloadPathway_actionPerformed(ActionEvent e) {

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

  private void menuFileSave_actionPerformed(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    File outfile = group.getFilename();
    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.VEGETATION_PATHWAYS,
                                  menuFileSave,null,group);
    update(getGraphics());
  }

  private void menuFileSaveAs_actionPerformed(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.VEGETATION_PATHWAYS,
                                  menuFileSave, null, group);
    update(getGraphics());
  }

  private void menuLoadPathway_actionPerformed(ActionEvent e) {

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

  private void menuLoadAllPathway_actionPerformed(ActionEvent e) {

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

  private void quit () {
    JSimpplle.getSimpplleMain().setVegPathwayDlgClosed();
    setVisible(false);
    try {
      dispose();
    }
    // For some reason this exception is getting thrown on dispose.
    // I don't know why.  Just ignore it.
    catch (NullPointerException ex) {
    }
  }

  private void menuFileQuit_actionPerformed(ActionEvent e) {
    quit();
  }

  private void this_windowClosing(WindowEvent e) {
    quit();
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

  private void menuPathwaysNewState_actionPerformed(ActionEvent e) {

    PathwayNewState dlg = new PathwayNewState(JSimpplle.getSimpplleMain(),
                                              "Create a New State",
                                              true, pathwayGroup, species);
    dlg.setLocation(getLocation());
    dlg.setVisible(true);

    updateDialog();

  }

  private void pathwayGroupCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    String result = (String)pathwayGroupCB.getSelectedItem();
    if (result != null) {
      pathwayGroup = result;
    }
    updateDialog();
  }

  private void speciesCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    Species result = (Species) speciesCB.getSelectedItem();
    if (result != null) {
      species = result;
    }
    updateDialog();
  }

  private void processCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    String result = (String) processCB.getSelectedItem();
    if (result != null) {
      process = result;
    }
    updateDialog();
  }

  private void autoPositionStates_actionPerformed(ActionEvent e) {
    HabitatTypeGroup group;
    String msg = "This will position currently shown states.\n\n" + "Continue?";
    int choice = JOptionPane.showConfirmDialog(this,msg,
                                           "Auto Position States",
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      group = HabitatTypeGroup.findInstance(pathwayGroup);
      group.autoPositionSpecies(species);
      updateDialog();
    }
  }

  private void autoPositionAllStates_actionPerformed(ActionEvent e) {
    HabitatTypeGroup group;
    String msg = "This will position All states in the current Ecological Grouping.\n\n" +
                 "Continue?";
    int choice = JOptionPane.showConfirmDialog(this,msg,
                                           "Auto Position All States",
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      group = HabitatTypeGroup.findInstance(pathwayGroup);
      group.autoPositionAllSpecies();
      updateDialog();
    }
  }

  private void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    if (group == null) { return; }

    String str = group.getKnowledgeSource();
    String title = "Knowledge Source for " + group.toString();

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);

    String newKnowledge = dlg.getText();
    if (newKnowledge != null) {
      group.setKnowledgeSource(newKnowledge);
    }
  }

  private void menuPathwaySetLifeformYearlyStatus_actionPerformed(ActionEvent e) {
    ArrayList<Lifeform> lives = new ArrayList<Lifeform>(Arrays.asList(Lifeform.getAllValues()));

    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    Lifeform[] values = group.getYearlyPathwayLifeforms();
    ArrayList<Lifeform> yearlyLives;

    if (values != null) {
      yearlyLives = new ArrayList<Lifeform>(Arrays.asList(values));
    }
    else {
      yearlyLives = new ArrayList<Lifeform>();
    }

    String title = "Yearly Pathway Lifeforms";

    CheckBoxChooser dlg =
      new CheckBoxChooser(this,title,true,lives,yearlyLives);
    dlg.setVisible(true);

    group.setYearlyPathwayLifeforms(dlg.getChosenItems());
  }

  private void menuPathwayShowAllLabels_actionPerformed(ActionEvent e) {
    canvas.toggleShowAllLabels();
    canvas.refreshDiagram();
  }

  private void menuPathwayCollapseAll_actionPerformed(ActionEvent e) {
                CollapsedPathwayShape.collapseAll(canvas.getShapeHashtable());
                update(getGraphics());
  }

  private void menuPathwayDetailAll_actionPerformed(ActionEvent e) {
                CollapsedPathwayShape.detailAll(canvas.getShapeHashtable());
  }

  private void menuPathwayStepCounter_actionPerformed(ActionEvent e) {
          if(stepCounterDialog==null)
                  stepCounterDialog = new StepCounter(this);
          stepCounterDialog.setVisible(true);
  }

  private void menuPathwayGridLines_actionPerformed(ActionEvent e) {
          if (menuPathwayGridLines.getState()) {
            canvas.showGridLines();
          } else {
            canvas.hideGridLines();
          }
          canvas.repaint();
  }

  private void menuEditUndoArrow_actionPerformed(ActionEvent e) {
    if (savedArrowState == null || savedArrowProcess == null ||
        savedArrowNextState == null) {
      return;
    }

    savedArrowState.setProcessNextState(savedArrowProcess,savedArrowNextState);
    savedArrowState     = null;
    savedArrowProcess   = null;
    savedArrowNextState = null;
    menuEditUndoArrow.setEnabled(false);
  }

  public void saveArrowChange(VegetativeType state, Process p, VegetativeType nextState) {
    savedArrowState     = state;
    savedArrowProcess   = p;
    savedArrowNextState = nextState;
    menuEditUndoArrow.setEnabled(true);
  }
}







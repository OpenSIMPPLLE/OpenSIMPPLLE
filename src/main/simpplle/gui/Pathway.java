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
import simpplle.comcode.AquaticClass;
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
  private Species      species;
  private AquaticClass aquaticClass;
  private String       pathwayGroup;
  private String       process;
  private boolean      inInit = true;
  private boolean      aquaticsMode = false;
  private Process          savedArrowProcess;
  private VegetativeType   savedArrowNextState, savedArrowState;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileQuit = new JMenuItem();
  PathwayCanvas canvas = new PathwayCanvas();
  JScrollPane jScrollPane1 = new JScrollPane();
  JMenu menuPathways = new JMenu();
  JMenuItem menuLoadAllPathway = new JMenuItem();
  JMenuItem menuLoadPathway = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileUnloadPathway = new JMenuItem();
  JMenuItem menuFileImport = new JMenuItem();
  JMenuItem menuFileExport = new JMenuItem();
  JMenuItem menuPathwaysNewState = new JMenuItem();
  JMenuItem menuPathwayCollapseAll = new JMenuItem();
  JMenuItem menuPathwayDetailAll = new JMenuItem();
  JMenuItem menuPathwayStepCounter = new JMenuItem();
  JCheckBoxMenuItem menuPathwayGridLines = new JCheckBoxMenuItem();
  JPanel northPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JLabel zoneLabel = new JLabel();
  JLabel zoneText = new JLabel();
  JPanel textPanel = new JPanel();
  JPanel labelPanel = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  GridLayout gridLayout1 = new GridLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel infoPanel = new JPanel();
  JPanel selectionPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel processPanel = new JPanel();
  JPanel speciesPanel = new JPanel();
  JPanel pathwayGroupPanel = new JPanel();
  JComboBox pathwayGroupCB = new JComboBox();
  JComboBox speciesCB = new JComboBox();
  JComboBox processCB = new JComboBox();
  Border border1;
  TitledBorder pathwayGroupBorder;
  Border border2;
  TitledBorder speciesBorder;
  Border border3;
  TitledBorder processBorder;
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  JMenuItem autoPositionStates = new JMenuItem();
  JMenu menuKnowledgeSource = new JMenu();
  JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
  JMenuItem autoPositionAllStates = new JMenuItem();
  JCheckBoxMenuItem menuPathwayShowAllLabels = new JCheckBoxMenuItem();
  JMenuItem menuFileOldFormat = new JMenuItem();
  StepCounter stepCounterDialog;
  JMenu jMenu1 = new JMenu();
  JMenuItem menuEditUndoArrow = new JMenuItem();
  private JMenuItem menuFileSpeciesChange = new JMenuItem();
  private JMenuItem menuFileSpeciesInclusion = new JMenuItem();
  private JMenuItem menuPathwaySetLifeformYearlyStatus = new JMenuItem();
  private JMenu menuGroup = new JMenu();
/**
 * Constructor for Vegetative Pathways dialog.  
 * @param frame owner of Pathway dialog
 * @param title
 * @param modal specifies whether dialog blocks user input to other top-level windows when shown
 * @param aquaticsMode
 */
  public Pathway(Frame frame, String title, boolean modal, boolean aquaticsMode) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.aquaticsMode = aquaticsMode;
    initialize();
  }
  /**
   * Overloaded constructor for Vegetative pathways
   * @param frame owner of Pathways dialog
   * @param title title of pathway dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   */
  public Pathway(Frame frame, String title, boolean modal) {
    this(frame,title,modal,false);
  }

  public Pathway() {
    this(null, "", false);
    initialize();
  }
/**
 * Inits the dialog with components, borders, title, colors, and borders.  
 * 
 * Menu titems are File, Edit, Pathways, and Knowledge Source.  
 * 
 * @throws Exception
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
 * Initializes the Pathway dialog.  Sets the PathwayCanvas pathway dialog to this one, clears out the species, aquatic class, pathway group and process.
 * Aquatics pathways are only available for LTA Valley Segment Group (pathway group = LTA Valley Segment Group, Species = aquatic class).  
 * Otherwise it is a vegetative pathway (pathway group = Ecological grouping, Species = species).     
 */
  private void initialize() {
//    menuFileSpeciesChange.setVisible(JSimpplle.developerMode());
//    menuFileSpeciesInclusion.setVisible(JSimpplle.developerMode());

    canvas.setPathwayDlg(this);
    species      = null;
    aquaticClass = null;
    pathwayGroup = null;
    process      = null;

    zoneText.setText(Simpplle.getCurrentZone().getName());

    if (aquaticsMode) {
      pathwayGroupBorder.setTitle("LTA Valley Segment Group");
      speciesBorder.setTitle("Aquatic Class");
      menuPathwaySetLifeformYearlyStatus.setVisible(false);
    }
    else {
      pathwayGroupBorder.setTitle("Ecological Grouping");
      speciesBorder.setTitle("Species");
      menuPathwaySetLifeformYearlyStatus.setVisible(true);
    }

    updateDialog();
  }
/**
 * Updates the dialog.  
 */
  public void updateDialog() {
    int i;
    RegionalZone zone = Simpplle.getCurrentZone();

    inInit = true;
    String[] groups = (aquaticsMode) ? LtaValleySegmentGroup.getLoadedGroupNames() :
                                       HabitatTypeGroup.getLoadedGroupNames();

    // **new code**
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
    }
    else {
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
    // *** end new code ***
    Arrays.sort(groups); // Sort array of groups
    if (pathwayGroup == null) { pathwayGroup = groups[0]; }
    pathwayGroupCB.removeAllItems();
    for(i=0; i<groups.length; i++) {
      pathwayGroupCB.addItem(groups[i]);
    }
    pathwayGroupCB.setSelectedItem(pathwayGroup);

    speciesCB.removeAllItems();
    Species[] allSpecies     = HabitatTypeGroup.findInstance(pathwayGroup).getAllSpecies();
    Species   currentSpecies = species;
    species = allSpecies[0];
    for(i=0; i<allSpecies.length; i++) {
      if (allSpecies[i] == currentSpecies) {
        species = allSpecies[i];
      }
      speciesCB.addItem(allSpecies[i]);
    }
    speciesCB.setSelectedItem(species);

    processCB.removeAllItems();
    String[] allProcess = HabitatTypeGroup.findInstance(pathwayGroup).getAllProcesses(species);
    String currentProcess = process;
    process = ProcessType.SUCCESSION.toString();
    for(i=0; i<allProcess.length; i++) {
      if (allProcess[i].equals(currentProcess)) {
        process = allProcess[i];
      }
      processCB.addItem(allProcess[i]);
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
    menuFileSave.setEnabled( ((group != null) &&
                             (group.getFilename() != null) &&
                             group.hasChanged()) );
    update(getGraphics());
  }
/**
 * Allows the user to delete previous simulation data.  
 * @return
 */
  private boolean deleteSimulationCheck() {
    String msg =
      "An area is loaded that has simulation data.\n" +
      "If an invalid Vegetative state or Ecological Grouping is created \n" +
      "as a result of loading a new pathway file,\n" +
      "then the state will be marked as invalid, and a result the area as well.\n" +
      "** In addition all simulation data will be removed from memory. **\n\n" +
      "Do you wish to continue?\n";

    int choice = JOptionPane.showConfirmDialog(this,msg,
                                           "Warning: Simlation Data exists.",
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.WARNING_MESSAGE);

    if (choice == JOptionPane.NO_OPTION) {
      return true;
    }
    else {
      Simpplle.resetSimulation();
      return false;
    }
  }
/**
 * Checks if an invalid state was created as a result of loading a new pathway.  If one is invalid will mark the OpenSimpplle main frame invalid, 
 * and allow user to import fix states, edit units, or print invalid report.
 */
  private void doInvalidAreaCheck() {
    Area area = Simpplle.getCurrentArea();

    if (area.existAnyInvalidVegUnits()) {
      String msg =
        "Invalid states were created as a result of loading the new pathway\n" +
        "file.  In addition any simulation data that may have existed has\n" +
        "been erased from memory\n" +
        "The area can be made valid again by either running the Unit Editor\n" +
        "found under the Utilities menu of the main application window, or\n" +
        "by loading a pathway file that contains the missing states\n";

      JOptionPane.showMessageDialog(this,msg,"Invalid units found",
                                    JOptionPane.INFORMATION_MESSAGE);
      JSimpplle.getSimpplleMain().markAreaInvalid();
    }
    else {
      JSimpplle.getSimpplleMain().markAreaValid();
    }
  }
/**
 * If user elects to open a file in the File JMenu, opens a file with SystemKnowledgeFiler for Vegetative Pathways, then loads the habitat type group 
 * to last pathway loaded and updates all the vegetative pathways.  
 * @param e
 */
  void menuFileOpen_actionPerformed(ActionEvent e) {
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
  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();
    File         inputFile;
    MyFileFilter extFilter;
    String       title = "Select a Pathway Data File";

    extFilter = new MyFileFilter("pathway",
                                 "pathway Data Files (*.pathway)");

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    // Ask user for file
    HabitatTypeGroup newGroup;

    setCursor(Utility.getWaitCursor());
    inputFile = Utility.getOpenFile(this,title,extFilter);
    if (inputFile != null) {
      try {
        newGroup = Simpplle.getCurrentZone().loadPathway(inputFile);
        pathwayGroupCB.setSelectedItem(newGroup.getName());
        updateDialog();
     }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
    setCursor(Utility.getNormalCursor());
    update(getGraphics());

    // Update the units and check for invalid ones.
    if (area != null) {
      area.updatePathwayData();
      doInvalidAreaCheck();
    }
  }
  public void menuFileSpeciesChange_actionPerformed(ActionEvent e) {
    setCursor(Utility.getWaitCursor());

    File infile = Utility.getOpenFile(this,"Species Change File");
    try {
      BufferedReader fin = new BufferedReader(new FileReader(infile));
      HabitatTypeGroup.importSpeciesChangeFile(fin);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
    setCursor(Utility.getNormalCursor());

  }
  /**
   * 
   * @param e
   */
  public void menuFileSpeciesInclusion_actionPerformed(ActionEvent e) {
    setCursor(Utility.getWaitCursor());

    File infile = Utility.getOpenFile(this,"Species Inclusion File");
    try {
      HabitatTypeGroup.importInclusionFile(infile);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
    setCursor(Utility.getNormalCursor());

  }

  void menuFileImport_actionPerformed(ActionEvent e) {
    setCursor(Utility.getWaitCursor());

    Area             area    = Simpplle.getCurrentArea();
    File[]           infiles = Utility.getOpenFiles(this,"Pathway Text File(s)");
    HabitatTypeGroup newGroup;
    if (infiles != null && infiles.length > 0) {
      try {
        for (int i=0; i<infiles.length; i++) {
          newGroup = Simpplle.getCurrentZone().importPathway(infiles[i]);
          pathwayGroupCB.setSelectedItem(newGroup.getName());
        }
        updateDialog();
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error loading files",
                                      JOptionPane.ERROR_MESSAGE);
        updateDialog();
        setCursor(Utility.getNormalCursor());
        return;
      }
      // Update the units and check for invalid ones.
      if (area != null) {
        area.updatePathwayData();
        doInvalidAreaCheck();
      }
    }
    setCursor(Utility.getNormalCursor());
  }

  void menuFileExport_actionPerformed(ActionEvent e) {
    RegionalZone zone = Simpplle.getCurrentZone();
    Area         area = Simpplle.getCurrentArea();
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;
    List<String> results = new ArrayList<>();

    dlg = new ListSelectionDialog(theFrame,"Select Ecological Grouping(s)",true,
            HabitatTypeGroup.getLoadedGroupNames(), true);

    dlg.setLocation(getLocation());
    dlg.setVisible(true);
    // get selections back in generic objects
    Object[] objs = dlg.getSelections();
    for(Object o: objs){
      results.add((String)o);
    }
//    Object a = dlg.getSelections();
    if (results.isEmpty()) { return; }

    // get directory
    File dir = Utility.getSaveDir(this,"Select a Directory to Export to");
    if (dir == null) { return; }

    setCursor(Utility.getWaitCursor());
    for (String habitat: results){

      try {
        HabitatTypeGroup pathwayGroupInst = HabitatTypeGroup.findInstance(habitat);
        System.out.println(habitat);
        File outfile = new File(dir.getAbsolutePath()+File.separatorChar+habitat+".txt");
        pathwayGroupInst.export(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this, err.getMessage(),
                "Error exporting pathway",
                JOptionPane.ERROR_MESSAGE);
      }
    }
    setCursor(Utility.getNormalCursor());
  }
  void menuFileUnloadPathway_actionPerformed(ActionEvent e) {
    RegionalZone zone = Simpplle.getCurrentZone();
    Area         area = Simpplle.getCurrentArea();
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;
    String              result;

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    dlg = new ListSelectionDialog(theFrame,"Select a Ecological Grouping",true,
                                  HabitatTypeGroup.getLoadedGroupNames());

    dlg.setLocation(getLocation());
    dlg.setVisible(true);
    result = (String)dlg.getSelection();
    if (result == null) { return; }

    setCursor(Utility.getWaitCursor());
    try {
      zone.removePathway(result);
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),
                                    "Error unloading pathway",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());
    species = null;
    pathwayGroup   = null;
    process = null;
    updateDialog();
    update(getGraphics());

    // Update the units and check for invalid ones.
    if (area != null) {
      area.updatePathwayData();
      doInvalidAreaCheck();
    }
  }
/**
 * If user selects JMenu item save, uses the system filer to save the vegetation pathways file.  
 * @param e
 */
  void menuFileSave_actionPerformed(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);
    File             outfile = group.getFilename();

    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.VEGETATION_PATHWAYS,
                                  menuFileSave,null,group);
    update(getGraphics());
  }
  /**
   * If user selects JMenu item Save As, uses the system filer to save the vegetation pathways file.  
   * @param e
   */
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    HabitatTypeGroup group = HabitatTypeGroup.findInstance(pathwayGroup);

    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.VEGETATION_PATHWAYS,
                                  menuFileSave, null, group);
    update(getGraphics());
  }
/**
 * Loads vegetative pathway for current zone and area.  It creates a new ListSelectionDialog which allows users to select an ecological grouping.  
 * @param e
 */
  void menuLoadPathway_actionPerformed(ActionEvent e) {
    RegionalZone        zone = Simpplle.getCurrentZone();
    Area                area = Simpplle.getCurrentArea();
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;
    String              result;

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    dlg = new ListSelectionDialog(theFrame,"Select a Ecological Grouping",true,
                                  HabitatTypeGroup.getLoadedGroupNames());

    dlg.setLocation(getLocation());
    dlg.setVisible(true);
    result = (String)dlg.getSelection();
    if (result != null) {
      try {
        zone.loadPathway(result);
        pathwayGroupCB.setSelectedItem(result);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading pathway",
                                      JOptionPane.ERROR_MESSAGE);
      }
      if (result.equals(pathwayGroup)) {
        species = null;
        process = null;
      }
      // Update the units and check for invalid ones.
      if (area != null) {
        area.updatePathwayData();
        doInvalidAreaCheck();
      }
    }
    updateDialog();
  }

  void menuLoadAllPathway_actionPerformed(ActionEvent e) {
    RegionalZone        zone = Simpplle.getCurrentZone();
    Area                area = Simpplle.getCurrentArea();
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;
    String              result;

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    setCursor(Utility.getWaitCursor());
    try {
      zone.loadAllPathways();
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Error loading pathways",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());

    pathwayGroup   = null;
    species = null;
    process = null;
    updateDialog();

    // Update the units and check for invalid ones.
    if (area != null) {
      area.updatePathwayData();
      doInvalidAreaCheck();
    }
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

  void menuFileQuit_actionPerformed(ActionEvent e) {
    quit();
  }

  void this_windowClosing(WindowEvent e) {
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

  void menuPathwaysNewState_actionPerformed(ActionEvent e) {
    String title = "Create a New State";
    Frame  theFrame = JSimpplle.getSimpplleMain();
    PathwayNewState dlg = new PathwayNewState(theFrame,title,true,pathwayGroup,species);

    dlg.setLocation(getLocation());
    dlg.setVisible(true);
    updateDialog();
  }

  void pathwayGroupCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    String result = (String)pathwayGroupCB.getSelectedItem();
    if (result != null) {
      pathwayGroup = result;
    }
    updateDialog();
  }

  void speciesCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    Species result = (Species) speciesCB.getSelectedItem();
    if (result != null) {
      species = result;
    }
    updateDialog();
  }

  void processCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    String result = (String) processCB.getSelectedItem();
    if (result != null) {
      process = result;
    }
    updateDialog();
  }

  void autoPositionStates_actionPerformed(ActionEvent e) {
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
  void autoPositionAllStates_actionPerformed(ActionEvent e) {
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

  void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
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

  void menuPathwaySetLifeformYearlyStatus_actionPerformed(ActionEvent e) {
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

  void menuPathwayShowAllLabels_actionPerformed(ActionEvent e) {
    canvas.toggleShowAllLabels();
    canvas.refreshDiagram();
  }

  void menuPathwayCollapseAll_actionPerformed(ActionEvent e) {
                CollapsedPathwayShape.collapseAll(canvas.getShapeHashtable());
                update(getGraphics());
  }

  void menuPathwayDetailAll_actionPerformed(ActionEvent e) {
                CollapsedPathwayShape.detailAll(canvas.getShapeHashtable());
  }
  void menuPathwayStepCounter_actionPerformed(ActionEvent e) {
          if(stepCounterDialog==null)
                  stepCounterDialog = new StepCounter(this);
          stepCounterDialog.setVisible(true);
  }

  void menuPathwayGridLines_actionPerformed(ActionEvent e) {
          if (menuPathwayGridLines.getState()) {
            canvas.showGridLines();
          } else {
            canvas.hideGridLines();
          }
          canvas.repaint();
  }

  void menuEditUndoArrow_actionPerformed(ActionEvent e) {
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







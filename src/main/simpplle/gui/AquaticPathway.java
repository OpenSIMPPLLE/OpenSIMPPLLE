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
import simpplle.comcode.LtaValleySegmentGroup;
import simpplle.comcode.Simpplle;
import simpplle.comcode.Area;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.PotentialAquaticState;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Process;
import simpplle.comcode.AquaticClass;
import simpplle.comcode.SystemKnowledge;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/** 
 * This class defines the GUI for Aquatic Pathway, a type of JDialog.
 * Aquatic pathways were set up in Westside Region 1 as an experiment.  It is 
 * not available in most zones.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class AquaticPathway extends JDialog {
  private AquaticClass aquaticClass;
  private String       pathwayGroup;
  private String       process;
  private boolean      inInit = true;

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
  JPanel aquaticClassPanel = new JPanel();
  JPanel pathwayGroupPanel = new JPanel();
  JComboBox pathwayGroupCB = new JComboBox();
  JComboBox aquaticClassCB = new JComboBox();
  JComboBox processCB = new JComboBox();
  Border border1;
  TitledBorder pathwayGroupBorder;
  Border border2;
  TitledBorder aquaticClassBorder;
  Border border3;
  TitledBorder processBorder;
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  JMenuItem autoPositionStates = new JMenuItem();
  JMenu menuKnowledgeSource = new JMenu();
  JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
  JMenuItem menuFileOldFormat = new JMenuItem();

  /**
   * Primary constructor for Aquatic Pathway GUI.  Inherits from Javax.swing.JDialog
   * @param frame
   * @param title
   * @param modal
   */
  public AquaticPathway(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
/**
 * Overloaded Aquatic Pathway constructor.  Calls to primary constructor, makes owner null, the title to empy string, and mode to false
 */
  public AquaticPathway() {
    this(null, "", false);
    initialize();
  }
/**
 * jbInit sets the border, layouts, panels, and menu item's text and action listeners
 * @throws Exception
 */
  void jbInit() throws Exception {
    border1 = BorderFactory.createLineBorder(SystemColor.controlText,1);
    pathwayGroupBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"LTA Valley Segment Group");
    border2 = BorderFactory.createEmptyBorder();
    aquaticClassBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Aquatic Class");
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
    this.setTitle("Aquatic Pathways");
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    this.setJMenuBar(jMenuBar1);
    menuPathways.setText("Pathways");
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
    menuFileExport.setText("Export Pathway to Text File ...");
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
    aquaticClassPanel.setBorder(aquaticClassBorder);
    aquaticClassPanel.setLayout(flowLayout4);
    pathwayGroupBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    aquaticClassBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    processPanel.setBorder(processBorder);
    processPanel.setLayout(flowLayout5);
    processBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    pathwayGroupCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pathwayGroupCB_actionPerformed(e);
      }
    });
    aquaticClassCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        aquaticClassCB_actionPerformed(e);
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
    menuFileOldFormat.setText("Open Old Format File");
    menuFileOldFormat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOldFormat_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    jMenuBar1.add(menuFile);
    jMenuBar1.add(menuPathways);
    jMenuBar1.add(menuKnowledgeSource);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileUnloadPathway);
    menuFile.addSeparator();
    menuFile.add(menuFileOldFormat);
    menuFile.addSeparator();
    menuFile.add(menuFileImport);
    menuFile.add(menuFileExport);
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
    selectionPanel.add(aquaticClassPanel, null);
    aquaticClassPanel.add(aquaticClassCB, null);
    selectionPanel.add(processPanel, null);
    processPanel.add(processCB, null);
    jScrollPane1.getViewport().add(canvas, null);
    menuPathways.add(menuPathwaysNewState);
    menuPathways.add(autoPositionStates);
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);
  }
/**
 * initialized is called in both aquatic pathways constructors, makes aquatic class, pathway group, and process null, then 
 * sets the zone label to the current simpple zone name.  Then calls the update Dialog method which operates as the primary data exchange for this class
 */
  private void initialize() {
//    canvas.setPathwayDlg(this);

    aquaticClass = null;
    pathwayGroup = null;
    process      = null;

    zoneText.setText(Simpplle.getCurrentZone().getName());
    updateDialog();
  }
/**
 * sets the zone as current zone, adds pathway groups to combo boxes, adds aquatic classes combo boxes, adds process combo boxes
 * all these are from the current zone and current simulation.
 */
  public void updateDialog() {
    int i;
    RegionalZone zone = Simpplle.getCurrentZone();

    inInit = true;
    String[] groups = LtaValleySegmentGroup.getLoadedGroupNames();

    if (pathwayGroup == null) { pathwayGroup = groups[0]; }
    pathwayGroupCB.removeAllItems();
    for(i=0; i<groups.length; i++) {
      pathwayGroupCB.addItem(groups[i]);
    }
    pathwayGroupCB.setSelectedItem(pathwayGroup);

    aquaticClassCB.removeAllItems();
    AquaticClass[] allAquaticClass     = LtaValleySegmentGroup.findInstance(pathwayGroup).getAllAquaticClass();
    AquaticClass   currentAquaticClass = aquaticClass;
    aquaticClass = allAquaticClass[0];
    for(i=0; i<allAquaticClass.length; i++) {
      if (allAquaticClass[i] == currentAquaticClass) {
        aquaticClass = allAquaticClass[i];
      }
      aquaticClassCB.addItem(allAquaticClass[i]);
    }
    aquaticClassCB.setSelectedItem(aquaticClass);

    processCB.removeAllItems();
    String[] allProcess     = LtaValleySegmentGroup.findInstance(pathwayGroup).getAllProcesses(aquaticClass);
    String   currentProcess = process;
    process = ProcessType.STREAM_DEVELOPMENT.toString();
    for(i=0; i<allProcess.length; i++) {
      if (allProcess[i].equals(currentProcess)) {
        process = allProcess[i];
      }
      processCB.addItem(allProcess[i]);
    }
    processCB.setSelectedItem(process);
    inInit = false;

//    canvas.setHtGrp(pathwayGroup);
//    canvas.setSpecies(aquaticClass);
//    canvas.setProcess(process);

    LtaValleySegmentGroup group = LtaValleySegmentGroup.findInstance(pathwayGroup);
    menuFileSave.setEnabled( ((group != null) &&
                             (group.getFilename() != null) &&
                             group.hasChanged()) );
    update(getGraphics());
  }
/**
 * checks if user wants to delete a simulation.  this will be triggered if there is simulation data present, and a chance of deleting current simulation data.
 * 
 * @return false if user selects JOptionPane yes - and will reset simulation, true if user selects JOptionPane NO which will exit out of the open file method
 */
  private boolean deleteSimulationCheck() {
    String msg =
      "An area is loaded that has simulation data.\n" +
      "If an invalid Potential Aquatic state or LTA Valley Segment Group is created \n" +
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
 * calls to area class to see if any of the Evu's are invalid within the new file pathway
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
 * gets simulation current area if area and simulation are not null and has passed (false) the delete simulation check, then the system knowledge file is read and 
 * pathway group combo boxes are set
 * @param e
 */
  void menuFileOpen_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    if (SystemKnowledgeFiler.openFile(this,SystemKnowledge.VEGETATION_PATHWAYS,menuFileSave, null)) {
      LtaValleySegmentGroup newGroup = SystemKnowledge.getLastAquaticPathwayLoaded();
      pathwayGroupCB.setSelectedItem(newGroup.getName());

      updateDialog();
      update(getGraphics());

      // Update the units and check for invalid ones.
//      if (area != null) {
//        area.updatePathwayData();
//        doInvalidAreaCheck();
//      }
    }
  }

  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();
    File         inputFile;
    MyFileFilter extFilter;
    String       title = "Select a Pathway Data File";

    extFilter = new MyFileFilter("aquapath",
                                 "pathway Data Files (*.aquapath)");

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    // Ask user for file
    setCursor(Utility.getWaitCursor());
    inputFile = Utility.getOpenFile(this,title,extFilter);

    if (inputFile != null) {
      try {
        Simpplle.getCurrentZone().loadAquaticPathway(inputFile);
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
//    if (area != null) {
      /** @todo implement this for aquatics. */
//      area.updatePathwayData();
//      doInvalidAreaCheck();
//    }
  }
  /**
   * file import method triggered by menu file import item, gets current area, imports aquatic pathways and updates the gui, if an error occurs during import will be caught here and present an error option pane to user
   * @param e
   */
  void menuFileImport_actionPerformed(ActionEvent e) {
    setCursor(Utility.getWaitCursor());

    Area area   = Simpplle.getCurrentArea();
    File infile = Utility.getOpenFile(this,"Pathway Text File");
    if (infile != null) {
      try {
        Simpplle.getCurrentZone().importAquaticPathway(infile);
        updateDialog();
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error loading file",
                                      JOptionPane.ERROR_MESSAGE);
        updateDialog();
        setCursor(Utility.getNormalCursor());
        return;
      }
      // Update the units and check for invalid ones.
//      if (area != null) {
        /** @todo implement this for aquatics. */
//        area.updatePathwayData();
//        doInvalidAreaCheck();
//      }
    }
    setCursor(Utility.getNormalCursor());
  }

  void menuFileExport_actionPerformed(ActionEvent e) {
    RegionalZone zone = Simpplle.getCurrentZone();
    Area         area = Simpplle.getCurrentArea();
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;
    String              result;

    dlg = new ListSelectionDialog(theFrame,"Select a LTA Valley Segment Group",true,
                                  LtaValleySegmentGroup.getLoadedGroupNames());

    dlg.setLocation(getLocation());
    dlg.setVisible(true);
    result = (String)dlg.getSelection();
    if (result == null) { return; }

    File outfile = Utility.getSaveFile(this,"File to export to?");
    if (outfile == null) { return; }

    setCursor(Utility.getWaitCursor());
    try {
      LtaValleySegmentGroup pathwayGroupInst = LtaValleySegmentGroup.findInstance(result);
      pathwayGroupInst.export(outfile);
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),
                                    "Error exporting pathway",
                                    JOptionPane.ERROR_MESSAGE);
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

    dlg = new ListSelectionDialog(theFrame,"Select a LTA Valley Segment Group",true,
                                  LtaValleySegmentGroup.getLoadedGroupNames());

    dlg.setLocation(getLocation());
    dlg.setVisible(true);
    result = (String)dlg.getSelection();
    if (result == null) { return; }

    setCursor(Utility.getWaitCursor());
    try {
      zone.removeAquaticPathway(result);
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),
                                    "Error unloading pathway",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());
    aquaticClass = null;
    pathwayGroup = null;
    process      = null;
    updateDialog();
    update(getGraphics());

    // Update the units and check for invalid ones.
//    if (area != null) {
      /** @todo implement the aquatic version of these */
//      area.updatePathwayData();
//      doInvalidAreaCheck();
//    }
  }

 void menuFileSave_actionPerformed(ActionEvent e) {
    LtaValleySegmentGroup group = LtaValleySegmentGroup.findInstance(pathwayGroup);
    File outfile = group.getFilename();

    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.AQUATIC_PATHWAYS,
                                  menuFileSave,null,group);
    update(getGraphics());
  }

  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    LtaValleySegmentGroup group = LtaValleySegmentGroup.findInstance(pathwayGroup);

    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.AQUATIC_PATHWAYS,
                                  menuFileSave, null, group);
    update(getGraphics());
  }
/**
 * action triggered by menu load pathway item.  reads current zone, area, checks if user is ok with deleting simulation, and sets a new JDialog with group names as object selection values
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

    dlg = new ListSelectionDialog(theFrame,"Select a Lta Valley Segment Group",true,
                                  LtaValleySegmentGroup.getLoadedGroupNames());

    dlg.setLocation(getLocation());
    dlg.setVisible(true);
    result = (String)dlg.getSelection();
    if (result != null) {
      try {
        zone.loadAquaticPathway(result);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading pathway",
                                      JOptionPane.ERROR_MESSAGE);
      }
      if (result.equals(pathwayGroup)) {
        aquaticClass = null;
        process = null;
      }
      // Update the units and check for invalid ones.
//      if (area != null) {
        /** @todo implement this for aquatics */
//        area.updatePathwayData();
//        doInvalidAreaCheck();
//      }
    }
    updateDialog();
  }
/**
 * 
 * @param e
 */
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
      zone.loadAllAquaticPathways();
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Error loading pathways",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());

    pathwayGroup = null;
    aquaticClass = null;
    process      = null;
    updateDialog();

    // Update the units and check for invalid ones.
//    if (area != null) {
        /** @todo implement this for aquatics */
//      area.updatePathwayData();
//      doInvalidAreaCheck();
//    }
  }
/**
 * method to quit from aquatic pathways JDialog. 
 */
  private void quit () {
    JSimpplle.getSimpplleMain().setAquaticPathwayDlgClosed();
    setVisible(false);
    dispose();
  }
/**
 * action event for 'Quit' menu item
 * @param e
 */
  void menuFileQuit_actionPerformed(ActionEvent e) {
    quit();
  }

  void this_windowClosing(WindowEvent e) {
    quit();
  }

  public void setAquaticClass(PotentialAquaticState state) {
    aquaticClass = state.getAquaticClass();
    updateDialog();
  }

  public void setAquaticClassAndProcess(PotentialAquaticState state, Process p) {
    aquaticClass = state.getAquaticClass();
    process = p.toString();
    updateDialog();
  }

  void menuPathwaysNewState_actionPerformed(ActionEvent e) {
//    String title = "Create a New State";
//    Frame  theFrame = JSimpplle.getSimpplleMain();
//    PathwayNewState dlg = new PathwayNewState(theFrame,title,true,pathwayGroup,aquaticClass);
//
//    dlg.setLocation(getLocation());
//    dlg.show();
//    updateDialog();
  }
/**
 * action event for pathway group combo box.  
 * @param e
 */
  void pathwayGroupCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    String result = (String)pathwayGroupCB.getSelectedItem();
    if (result != null) {
      pathwayGroup = result;
    }
    updateDialog();
  }
/**
 * action event which distinguishes the aquatic class selected
 * @param e
 */
  void aquaticClassCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    AquaticClass result = (AquaticClass) aquaticClassCB.getSelectedItem();
    if (result != null) {
      aquaticClass = result;
    }
    updateDialog();
  }
/**
 * action event for processes combo box
 * @param e
 */
  void processCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    String result = (String) processCB.getSelectedItem();
    if (result != null) {
      process = result;
    }
    updateDialog();
  }

  void autoPositionStates_actionPerformed(ActionEvent e) {
//    HabitatTypeGroup group;
//    String msg = "This will position currently shown states.\n\n" + "Continue?";
//    int choice = JOptionPane.showConfirmDialog(this,msg,
//                                           "Auto Position States",
//                                           JOptionPane.YES_NO_OPTION,
//                                           JOptionPane.QUESTION_MESSAGE);
//
//    if (choice == JOptionPane.YES_OPTION) {
//      group = Simpplle.getCurrentZone().getLtaVsGroup(pathwayGroup);
//      group.autoPositionAquaticClass(aquaticClass);
//      updateDialog();
//    }
  }

  void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.AQUATIC_PATHWAYS);
    String title = "Aquatic Pathways Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }


}







/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import com.mchange.v1.util.ArrayUtils;
import org.hsqldb.util.DatabaseManagerSwing;
import simpplle.JSimpplle;
import simpplle.comcode.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * This is the main screen for the OpenSimpplle gui.  It consists of a menu bar and the following menu items
 * File, System Knowledge, Import, Export, Reports, Intepretations, View Results, Utilities, Help.  Thre are also buttons to create new simulation, and 
 * labels for current zone and area.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
@SuppressWarnings("serial")
public class SimpplleMain extends JFrame {
  public static final String VERSION      = "1.3.0";
  public static final String RELEASE_KIND = "Douglas Fir";
  public static final String BUILD_DATE   = "July 2016";

  public static Color RESULT_COL_COLOR    = new Color(90,190,190);
  public static Color ROW_HIGHLIGHT_COLOR = new Color(162,200,157);

  @SuppressWarnings("unused")
  //private HelpBroker helpBroker;
  private simpplle.comcode.Simpplle comcode;
  private static final int MIN_WIDTH  = 718;
  private static final int MIN_HEIGHT = 300;
  private boolean vegPathwayDlgOpen     = false;
  private boolean aquaticPathwayDlgOpen = false;

  /**
   * Populates Combo Box dynamically. SIMPPLLE is the default and always available.
   */
  private Vector<String> fireSpreadModels = new Vector<>(2);

  JMenuBar menuBar1 = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileQuit = new JMenuItem();
  JToolBar toolBar = new JToolBar();
  JButton newZone = new JButton();
  JButton newArea = new JButton();
  JButton runSimulation = new JButton();
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JMenu menuSysKnow = new JMenu();
  JMenu menuImport = new JMenu();
  JMenu menuExport = new JMenu();
  JMenu menuReports = new JMenu();
  JMenu menuInterpretations = new JMenu();
  JMenu menuViewResult = new JMenu();
  JMenu menuHelp = new JMenu();
  JMenuItem menuFileWorkDir = new JMenuItem();
  JMenu menuUtility = new JMenu();
  JMenuItem menuUtilityReset = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenu menuSysKnowPath = new JMenu();
  JMenuItem menuSysKnowPathVeg = new JMenuItem();
  JMenuItem menuSysKnowPathAquatic = new JMenuItem();
  JMenu menuSysKnowVegProc = new JMenu();
  JMenuItem menuSysKnowVegProcLock = new JMenuItem();
  JMenuItem menuSysKnowFireEventLogic = new JMenuItem();
  JMenuItem menuSysKnowFireProb = new JMenuItem();
  JMenuItem menuSysKnowFireCost = new JMenuItem();
  JMenuItem menuSysKnowFireInput = new JMenuItem();
  JMenuItem jMenuItem1 = new JMenuItem();
  JMenuItem menuSysKnowAquaticTreat = new JMenuItem();
  JMenu menuExportGIS = new JMenu();
  JMenuItem menuReportsSummary = new JMenuItem();
  JMenuItem menuReportsUnit = new JMenuItem();
  JMenu menuReportsMult = new JMenu();
  JMenuItem menuReportsMultNormal = new JMenuItem();
  JMenuItem menuReportsMultSpecial = new JMenuItem();
  JMenuItem menuReportsMultOwner = new JMenuItem();
  JMenuItem menuReportsFire = new JMenuItem();
  JMenuItem menuReportsFireCost = new JMenuItem();
  JMenuItem menuReportsEmission = new JMenuItem();
  JMenuItem menuInterpretProbCalc = new JMenuItem();
  JMenuItem menuInterpretWildlife = new JMenuItem();
  JMenuItem menuResultVegUnit = new JMenuItem();
  JMenuItem menuResultVegSum = new JMenuItem();
  JMenuItem menuResultAquaticUnit = new JMenuItem();
  JMenuItem menuResultAquaticSum = new JMenuItem();
  JMenuItem menuResultAquaticDiagram = new JMenuItem();
  JMenuItem menuUtilityDescription = new JMenuItem();
  JMenuItem menuUtilitySimReady = new JMenuItem();
  JMenuItem menuHelpAbout = new JMenuItem();
  JMenuItem menuHelpDocs = new JMenuItem();
  JMenuItem menuUtilityPrintArea = new JMenuItem();
  JPanel northPanel = new JPanel();
  JPanel currentPanel = new JPanel();
  JLabel areaLabel = new JLabel();
  JLabel zoneLabel = new JLabel();
  GridLayout gridLayout1 = new GridLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel zonePanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel areaPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JLabel zoneValueLabel = new JLabel();
  JLabel areaValueLabel = new JLabel();
  JMenuItem menuExportGISDecadeProb = new JMenuItem();
  JMenuItem menuImportCreate = new JMenuItem();
  JMenuItem menuImportFixStates = new JMenuItem();
  JMenuItem menuImportEditUnits = new JMenuItem();
  JMenuItem menuUtilityUnitEditor = new JMenuItem();
  JLabel areaInvalidLabel = new JLabel();
  JMenuItem menuSysKnowRegionalClimate = new JMenuItem();
  JMenuItem menuSysKnowInsectDiseaseSpread = new JMenuItem();
  JMenuItem menuUtilitiesConsole = new JMenuItem();
  JMenuItem menuImportAttributeData = new JMenuItem();
  JMenuItem menuUtilityAreaName = new JMenuItem();
  JMenuItem menuInterpretRestoration = new JMenuItem();
  JMenuItem menuSysKnowOpen = new JMenuItem();
  JMenuItem menuSysKnowSave = new JMenuItem();
  JMenuItem menuInterpretRiskCalc = new JMenuItem();
  JMenuItem menuSysKnowRegen = new JMenuItem();
  JMenuItem menuSysKnowConiferEncroach = new JMenuItem();
  JMenu menuSysKnowVegTreat = new JMenu();
  JMenuItem menuSysKnowVegTreatSchedule = new JMenuItem();
  JMenuItem menuSysKnowVegTreatLogic = new JMenuItem();
  JMenuItem menuUtilityExcelFile = new JMenuItem();
  JMenuItem menuSysKnowRestoreDefaults = new JMenuItem();
  JMenuItem menuExportGISReburn = new JMenuItem();
  JMenuItem menuImportInvalidReport = new JMenuItem();
  JMenu menuSysKnowWeatherEvent = new JMenu();
  JMenuItem menuWeatherEventClassA = new JMenuItem();
  JMenuItem menuWeatherEventNotClassA = new JMenuItem();
  JMenuItem menuExportAttributes = new JMenuItem();
  JMenuItem menuUtilityDatabaseTest = new JMenuItem();
  JCheckBoxMenuItem menuSysKnowUseRegenPulse = new JCheckBoxMenuItem();
  JMenu menuSysKnowFireSuppLogic = new JMenu();
  JMenuItem menuSysKnowFireSuppLogicClassA = new JMenuItem();
  JMenuItem menuSysKnowFireSuppLogicBeyondClassA = new JMenuItem();
  JMenu menuMagis = new JMenu();
  JMenuItem menuMagisProcessTreatmentFiles = new JMenuItem();
  JMenuItem menuMagisAllVegStates = new JMenuItem();
  JMenuItem menuUtilityDeleteUnits = new JMenuItem();
  JMenuItem menuReportsFireSuppCostAll = new JMenuItem();
  JMenuItem menuSysKnowWSBWLogic = new JMenuItem();
  JMenuItem menuGisUpdateSpread = new JMenuItem();
  JMenuItem menuResultLandformSum = new JMenuItem();
  JMenuItem menuResultLandformUnit = new JMenuItem();
  JMenuItem menuResultHydrologicResponse = new JMenuItem();
  JMenuItem menuSysKnowVegTreatDesired = new JMenuItem();
  JMenuItem menuUtilityGISFiles = new JMenuItem();
  JMenuItem menuSysKnowFireSeason = new JMenuItem();
  JMenu menuSysKnowFireSpread = new JMenu("Fire Spread Model");
  JMenuItem menuSysKnowCellPerc = new JMenuItem("Keane Cell Percolation");
  JMenuItem menuHelpUserGuide = new JMenuItem();
  JMenuItem MenuUtilityJavaHeap = new JMenuItem();
  JMenuItem menuSysKnowFireSuppProdRate = new JMenuItem();
  JMenuItem menuSysKnowFireSuppSpreadRate = new JMenuItem();
  JMenuItem menuSysKnowFireSuppResponseTime = new JMenuItem();
  JMenuItem menuSysKnowSpeciesKnowledgeEditor = new JMenuItem();
  JCheckBoxMenuItem menuUtilityZoneEdit = new JCheckBoxMenuItem();
  JMenuItem menuImportFSLandscape = new JMenuItem();
  JMenuItem menuSysKnowImportProcesses = new JMenuItem();
  JMenuItem menuSysKnowShowLegalProcesses = new JMenuItem();
  JMenuItem menuSysKnowShowZoneTreatments = new JMenuItem();
  JMenuItem menuSysKnowImportZoneTreatments = new JMenuItem();
  JMenuItem menuFileSaveZone = new JMenuItem();
  JMenuItem menuReportsAllStates = new JMenuItem();
  JMenuItem buildSimpplleTypeFiles = new JMenuItem();
  JMenuItem buildSimpplleTypesSource = new JMenuItem();
  JMenuItem menuSysKnowWildlifeBrowsing = new JMenuItem();
  JMenuItem menuSysKnowWindthrow = new JMenuItem();
  JMenuItem menuSysKnowTussockMoth = new JMenuItem();
  private JMenuItem menuUtilityTestNewDialog = new JMenuItem();
  private JMenuItem menuSysKnowProcessProbLogic = new JMenuItem();
  private JCheckBoxMenuItem menuSysKnowDisableWsbw = new JCheckBoxMenuItem();
  private JMenuItem menuUtilityHibern8IDE = new JMenuItem();
  private JMenuItem menuUtilityDatabaseManager = new JMenuItem();
  JMenuItem menuBisonGrazingLogic = new JMenuItem();
  JMenuItem menuUtilityMemoryUse = new JMenuItem();
  private JMenuItem menuSysKnowDoCompetition = new JMenuItem();
  private JMenuItem menuSysKnowGapProcessLogic = new JMenuItem();
  private JMenuItem menuSysKnowProducingSeedLogic = new JMenuItem();
  private JMenuItem menuSysKnowVegUnitFireTypeLogic = new JMenuItem();
  private JMenu menuSysKnowInvasive = new JMenu();
  private JMenuItem menuSysKnowInvasiveLogicR1 = new JMenuItem();
  private JMenuItem menuSysKnowInvasiveLogicMSU = new JMenuItem();
  private JMenuItem menuSysKnowInvasiveLogicMesaVerdeNP = new JMenuItem();
  private JMenuItem menuUtilityMakeAreaMultipleLife = new JMenuItem();
  private JMenuItem menuReportsTrackingSpecies = new JMenuItem();
  private JMenuItem menuUtilityCombineLSFiles = new JMenuItem();

  //Construct the frame
  private final JMenuItem menuUtilityExportPathways = new JMenuItem();
  private final JMenuItem menuUtilityElevRelPos = new JMenuItem();
  private final JMenuItem menuUtilitySwapRowCol = new JMenuItem();
  private final JMenuItem menuSysKnowFireSuppEvent = new JMenuItem();
  /**
   * This is the SimpplleMain constructor.  
   */
  public SimpplleMain() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    initialize();
  }
/**
 * Initializes the simpplle main by getting the simpplle image and initializing some tests if in developer mode.  There is also a helpset file directory set, but it is not currently functional.
 * 
 */
  private void initialize() {
    // Set icon that is show in the title bar and minimized icon.
    ImageIcon tmpImage = new ImageIcon(
        simpplle.gui.SimpplleMain.class.getResource("images/simpplle16.gif"));
    setIconImage(tmpImage.getImage());

    //HelpSet hs = null;
    comcode = JSimpplle.getComcode();

    // Prepare help system
   /* try {
      URL hsURL = HelpSet.findHelpSet(null, "simpplle/docs/simpplle.hs");
      hs = new HelpSet(null, hsURL);
    }
    catch (Exception ee) {
      System.out.println("HelpSet not found");
      return;
    }
    if (hs == null) { return; }*/

//    helpBroker = hs.createHelpBroker();
//    if (helpBroker != null) {
//      menuHelpDocs.addActionListener(new CSH.DisplayHelpFromSource(helpBroker));
//    }
    menuHelpDocs.setVisible(false);
    menuUtilityDatabaseTest.setVisible(JSimpplle.developerMode());
    menuUtilityDatabaseTest.setEnabled(JSimpplle.developerMode());

    menuUtilityHibern8IDE.setVisible(JSimpplle.developerMode());
    menuUtilityHibern8IDE.setEnabled(JSimpplle.developerMode());
    menuUtilityDatabaseManager.setVisible(true);
    menuUtilityDatabaseManager.setEnabled(true);

    menuUtilityZoneEdit.setVisible(false);
    menuUtilityTestNewDialog.setVisible(JSimpplle.developerMode());
    menuUtilityTestNewDialog.setEnabled(JSimpplle.developerMode());

    menuSysKnowDisableWsbw.setState(false);

    menuReportsFireSuppCostAll.setVisible(false);
    fireSpreadModels.add("SIMPPLLE");
  }

  //Component initialization
  /**
   * Initializes the SImpplleMain frame with a host of components, the most important of which are all the JMenu's and their JMenuItems.   
   * @throws Exception
   */
  private void jbInit() throws Exception  {
    titledBorder1 = new TitledBorder("");
    this.getContentPane().setLayout(borderLayout1);
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.setSize(new Dimension(1000, 600));
    this.setLocation(100,100);
    this.setTitle("OpenSIMPPLLE " + SimpplleMain.VERSION + " (" + SimpplleMain.BUILD_DATE + ")");
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      public void componentResized(ComponentEvent e) {
        this_componentResized(e);
      }
    });
    statusBar.setBackground(Color.black);
    statusBar.setForeground(Color.white);
    statusBar.setMaximumSize(new Dimension(1280, 20));
    statusBar.setMinimumSize(new Dimension(718, 20));
    statusBar.setOpaque(true);
    statusBar.setPreferredSize(new Dimension(100, 20));
    menuFile.setText("File");
    menuFileQuit.setText("Quit");
    menuFileQuit.addActionListener(new ActionListener()  {

      public void actionPerformed(ActionEvent e) {
        fileExit_actionPerformed(e);
      }
    });
    newZone.setToolTipText("Load a new zone.");
    newZone.setText("New Zone");
    newZone.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        newZone_actionPerformed(e);
      }
    });
    newArea.setEnabled(false);
    newArea.setToolTipText("Load an Area.");
    newArea.setText("New Area");
    newArea.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        newArea_actionPerformed();
      }
    });
    runSimulation.setEnabled(false);
    runSimulation.setToolTipText("Help");
    runSimulation.setText("Run Simulation");
    runSimulation.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        runSimulation_actionPerformed(e);
      }
    });
    menuSysKnow.setText("System Knowledge");
    menuImport.setText("Import");
    menuExport.setText("Export");
    menuReports.setText("Reports");
    menuInterpretations.setText("Interpretations");
    menuViewResult.setText("View Results");
    menuHelp.setText("Help");
    menuFileWorkDir.setText("Set Working Directory ...");
    menuFileWorkDir.setActionCommand("Set Working Directory");
    menuFileWorkDir.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileWorkDir_actionPerformed(e);
      }
    });
    menuUtility.setText("Utilities");
    menuUtilityReset.setEnabled(false);
    menuUtilityReset.setToolTipText("Removes Simulation and restores Area to initial conditions");
    menuUtilityReset.setText("Reset Area/Simulation");
    menuUtilityReset.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuUtilityReset_actionPerformed(e);
      }
    });
    menuFileSave.setEnabled(false);
    menuFileSave.setText("Save Landscape ...");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuSysKnowPath.setEnabled(false);
    menuSysKnowPath.setText("Pathways");
    menuSysKnowPathVeg.setText("Vegetative Pathways");
    menuSysKnowPathVeg.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowPathVeg_actionPerformed(e);
      }
    });
    menuSysKnowPathAquatic.setEnabled(false);
    menuSysKnowPathAquatic.setText("Aquatic Pathways");
    menuSysKnowPathAquatic.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowPathAquatic_actionPerformed(e);
      }
    });
    menuSysKnowVegProc.setText("Vegetative Process");
    menuSysKnowVegProcLock.setEnabled(false);
    menuSysKnowVegProcLock.setText("Lock in Processes");
    menuSysKnowVegProcLock.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowVegProcLock_actionPerformed(e);
      }
    });
    menuSysKnowFireEventLogic.setEnabled(false);
    menuSysKnowFireEventLogic.setText("Fire Event Logic");
    menuSysKnowFireEventLogic.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowFireEventLogic_actionPerformed(e);
      }
    });
    menuSysKnowFireProb.setEnabled(false);
    menuSysKnowFireProb.setText("Extreme Fire Spread Probability");
    menuSysKnowFireProb.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowFireProb_actionPerformed(e);
      }
    });
    menuSysKnowFireCost.setEnabled(false);
    menuSysKnowFireCost.setText("Fire Suppression Cost");
    menuSysKnowFireCost.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowFireCost_actionPerformed(e);
      }
    });
    menuSysKnowFireInput.setEnabled(false);
    menuSysKnowFireInput.setText("Fire Occurrence Input");
    menuSysKnowFireInput.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowFireInput_actionPerformed(e);
      }
    });
    jMenuItem1.setEnabled(false);
    jMenuItem1.setText("Aquatic Process");
    menuSysKnowAquaticTreat.setEnabled(false);
    menuSysKnowAquaticTreat.setText("Aquatic Treatments");
    menuExportGIS.setEnabled(false);
    menuExportGIS.setText("GIS Simulation Files");
    menuReportsSummary.setEnabled(false);
    menuReportsSummary.setText("Summary");
    menuReportsSummary.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsSummary_actionPerformed(e);
      }
    });
    menuReportsUnit.setEnabled(false);
    menuReportsUnit.setText("Individual Unit");
    menuReportsUnit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsUnit_actionPerformed(e);
      }
    });
    menuReportsMult.setEnabled(false);
    menuReportsMult.setText("Multiple Simulation");
    menuReportsMultNormal.setText("Normal");
    menuReportsMultNormal.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsMultNormal_actionPerformed(e);
      }
    });
    menuReportsMultSpecial.setEnabled(false);
    menuReportsMultSpecial.setText("By Special Area");
    menuReportsMultSpecial.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsMultSpecial_actionPerformed(e);
      }
    });
    menuReportsMultOwner.setEnabled(false);
    menuReportsMultOwner.setText("By Ownership");
    menuReportsMultOwner.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsMultOwner_actionPerformed(e);
      }
    });
    menuReportsFire.setEnabled(false);
    menuReportsFire.setText("Detailed Fire");
    menuReportsFire.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsFire_actionPerformed(e);
      }
    });
    menuReportsFireCost.setEnabled(false);
    menuReportsFireCost.setText("Fire Suppression Cost");
    menuReportsFireCost.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsFireCost_actionPerformed(e);
      }
    });
    menuReportsEmission.setEnabled(false);
    menuReportsEmission.setToolTipText("Emissions based on adaptations of work by hardy, etc.");
    menuReportsEmission.setText("Emissions");
    menuReportsEmission.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuReportsEmission_actionPerformed(e);
      }
    });
    menuInterpretProbCalc.setEnabled(false);
    menuInterpretProbCalc.setText("Attribute Probability Calculator");
    menuInterpretProbCalc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuInterpretProbCalc_actionPerformed(e);
      }
    });


    menuInterpretWildlife.setEnabled(false);
    menuInterpretWildlife.setText("Wildlife Habitat");
    menuInterpretWildlife.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuInterpretWildlife_actionPerformed(e);
      }
    });
    menuResultVegUnit.setEnabled(false);
    menuResultVegUnit.setText("Vegetative Unit Analysis");
    menuResultVegUnit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuResultVegUnit_actionPerformed(e);
      }
    });
    menuResultVegSum.setEnabled(false);
    menuResultVegSum.setText("Vegetative Condition Summary");
    menuResultVegSum.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuResultVegSum_actionPerformed(e);
      }
    });
    menuResultAquaticUnit.setEnabled(false);
    menuResultAquaticUnit.setText("Aquatic Unit Analysis");
    menuResultAquaticUnit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuResultAquaticUnit_actionPerformed(e);
      }
    });
    menuResultAquaticSum.setEnabled(false);
    menuResultAquaticSum.setText("Aquatic Condition Summary");
    menuResultAquaticSum.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuResultAquaticSum_actionPerformed(e);
      }
    });
    menuResultAquaticDiagram.setEnabled(false);
    menuResultAquaticDiagram.setText("Aquatic Stream Diagram");
    menuUtilitySimReady.setEnabled(false);
    menuUtilitySimReady.setText("Make Area Simulation Ready");
    menuUtilitySimReady.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilitySimReady_actionPerformed(e);
      }
    });
    menuHelpAbout.setText("About");
    menuHelpAbout.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuHelpAbout_actionPerformed(e);
      }
    });
    menuHelpDocs.setText("Documentation");
    menuUtilityPrintArea.setEnabled(false);
    menuUtilityPrintArea.setText("Print Current Area to File");
    menuUtilityPrintArea.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuUtilityPrintArea_actionPerformed(e);
      }
    });
    areaLabel.setText("Current Area:");
    zoneLabel.setText("Current Zone");
    currentPanel.setLayout(gridLayout1);
    gridLayout1.setColumns(1);
    gridLayout1.setHgap(1);
    gridLayout1.setRows(2);
    northPanel.setLayout(borderLayout2);
    currentPanel.setBorder(BorderFactory.createEtchedBorder());
    zonePanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(10);
    areaPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(10);
    areaValueLabel.setForeground(Color.blue);
    zoneValueLabel.setForeground(Color.blue);
    menuExportGISDecadeProb.setEnabled(false);
    menuExportGISDecadeProb.setText("Create GIS Probability Files by Decade");
    menuExportGISDecadeProb.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuExportGISDecadeProb_actionPerformed(e);
      }
    });
    menuImportCreate.setEnabled(false);
    menuImportCreate.setText("Create New Area");
    menuImportCreate.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuImportCreate_actionPerformed(e);
      }
    });
    menuImportFixStates.setEnabled(false);
    menuImportFixStates.setText("Fix Incorrect States");
    menuImportFixStates.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuImportFixStates_actionPerformed(e);
      }
    });
    menuImportEditUnits.setEnabled(false);
    menuImportEditUnits.setText("Edit Units");
    menuImportEditUnits.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuImportEditUnits_actionPerformed(e);
      }
    });
    menuUtilityUnitEditor.setEnabled(false);
    menuUtilityUnitEditor.setText("Open Unit Editor");
    menuUtilityUnitEditor.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuUtilityUnitEditor_actionPerformed(e);
      }
    });
    areaInvalidLabel.setFont(new java.awt.Font("Serif", 1, 14));
    areaInvalidLabel.setMaximumSize(new Dimension(60, 20));
    areaInvalidLabel.setMinimumSize(new Dimension(60, 20));
    areaInvalidLabel.setPreferredSize(new Dimension(60, 20));
    menuSysKnowRegionalClimate.setEnabled(false);
    menuSysKnowRegionalClimate.setText("Regional Climate");
    menuSysKnowRegionalClimate.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowRegionalClimate_actionPerformed(e);
      }
    });
    menuSysKnowInsectDiseaseSpread.setEnabled(false);
    menuSysKnowInsectDiseaseSpread.setText("Insect/Disease Spread Logic");
    menuSysKnowInsectDiseaseSpread.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuSysKnowInsectDiseaseSpread_actionPerformed(e);
      }
    });
    menuUtilitiesConsole.setText("Display Console Messages");
    menuUtilitiesConsole.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuUtilitiesConsole_actionPerformed(e);
      }
    });
    menuImportAttributeData.setEnabled(false);
    menuImportAttributeData.setText("Import Attribute Data");
    menuImportAttributeData.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuImportAttributeData_actionPerformed(e);
      }
    });
    menuUtilityAreaName.setEnabled(false);
    menuUtilityAreaName.setText("Change Area Name");
    menuUtilityAreaName.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuUtilityAreaName_actionPerformed(e);
      }
    });
    menuInterpretRestoration.setText("Ecosystem Restoration");
    menuInterpretRestoration.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuInterpretRestoration_actionPerformed(e);
      }
    });
    menuSysKnowOpen.setEnabled(false);
    menuSysKnowOpen.setToolTipText("Load files specified in the loaded " +
    "file for the following areas  (Fmz, Fire Spread, Type of Fire, Insect/Disease " +
    "Probability, Treatments, Lock-in Processes, and Pathways)");
    menuSysKnowOpen.setText("Open User Knowledge File");
    menuSysKnowOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowOpen_actionPerformed(e);
      }
    });
    menuSysKnowSave.setEnabled(false);
    menuSysKnowSave.setToolTipText("Saves a file with information on what user files are currently loaded " +
    "(Fmz, Fire Spread, Type of Fire, Insect/Disease Probability, Treatments, " +
    "Lock-in Processes, and Pathways)");
    menuSysKnowSave.setText("Save User Knowledge File");
    menuSysKnowSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowSave_actionPerformed(e);
      }
    });
    menuInterpretRiskCalc.setEnabled(false);
    menuInterpretRiskCalc.setText("Risk Calculator");

    menuSysKnowRegen.setEnabled(false);
    menuSysKnowRegen.setText("Regeneration");
    menuSysKnowRegen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowRegen_actionPerformed(e);
      }
    });
    menuSysKnowConiferEncroach.setEnabled(false);
    menuSysKnowConiferEncroach.setText("Conifer Encroachment");
    menuSysKnowConiferEncroach.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowConiferEncroach_actionPerformed(e);
      }
    });
    menuSysKnowVegTreat.setEnabled(false);
    menuSysKnowVegTreat.setText("Vegetative Treatments");
    menuSysKnowVegTreatSchedule.setEnabled(false);
    menuSysKnowVegTreatSchedule.setText("Treatment Schedule");
    menuSysKnowVegTreatSchedule.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowVegTreatSchedule_actionPerformed(e);
      }
    });
    menuSysKnowVegTreatLogic.setEnabled(false);
    menuSysKnowVegTreatLogic.setText("Treatment Logic");
    menuSysKnowVegTreatLogic.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowVegTreatLogic_actionPerformed(e);
      }
    });

    menuSysKnowRestoreDefaults.setEnabled(false);
    menuSysKnowRestoreDefaults.setToolTipText("Restores System Knowledge to defaults.");
    menuSysKnowRestoreDefaults.setText("Restore All Defaults");
    menuSysKnowRestoreDefaults.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowRestoreDefaults_actionPerformed(e);
      }
    });
    menuExportGISReburn.setText("Probability of Reburn");
    menuExportGISReburn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuExportGISReburn_actionPerformed(e);
      }
    });
    menuImportInvalidReport.setEnabled(false);
    menuImportInvalidReport.setText("Invalid Units Report");
    menuImportInvalidReport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuImportInvalidReport_actionPerformed(e);
      }
    });
    menuSysKnowWeatherEvent.setEnabled(false);
    menuSysKnowWeatherEvent.setText("Weather Ending Events");
    menuWeatherEventClassA.setText("Fires Less than .25 acres");
    menuWeatherEventClassA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWeatherEventClassA_actionPerformed(e);
      }
    });
    menuWeatherEventNotClassA.setText("Fires greater than .25 acres");
    menuWeatherEventNotClassA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWeatherEventNotClassA_actionPerformed(e);
      }
    });
    menuExportAttributes.setEnabled(false);
    menuExportAttributes.setText("Area Creation Files");
    menuExportAttributes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuExportAttributes_actionPerformed(e);
      }
    });
    menuUtilityDatabaseTest.setEnabled(false);
    menuUtilityDatabaseTest.setText("Database test");
    menuUtilityDatabaseTest.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityDatabaseTest_actionPerformed(e);
      }
    });
    menuSysKnowUseRegenPulse.setText("Use Regen Pulse");
    menuSysKnowUseRegenPulse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowUseRegenPulse_actionPerformed(e);
      }
    });
    menuSysKnowFireSuppLogic.setEnabled(false);
    menuSysKnowFireSuppLogic.setText("Fire Suppression Logic");
    
    menuSysKnowFireSuppLogic.add(menuSysKnowFireSuppEvent);
    menuSysKnowFireSuppEvent.addActionListener(new MenuSysKnowFireSuppEventActionListener());
    menuSysKnowFireSuppEvent.setText("Event Probability");
    menuSysKnowFireSuppLogicClassA.setText("Class A");
    menuSysKnowFireSuppLogicClassA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowFireSuppLogicClassA_actionPerformed(e);
      }
    });
    menuSysKnowFireSuppLogicBeyondClassA.setText("Beyond Class A");
    menuSysKnowFireSuppLogicBeyondClassA.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuSysKnowFireSuppLogicBeyondClassA_actionPerformed(e);
        }
    });
    menuMagis.setEnabled(false);
    menuMagis.setText("Magis");
    
    menuMagisProcessTreatmentFiles.setEnabled(false);
    menuMagisProcessTreatmentFiles.setText("Process & Treatment Files");
    menuMagisProcessTreatmentFiles.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuMagisProcessTreatmentFiles_actionPerformed(e);
        }
    });
    menuMagisAllVegStates.setEnabled(false);
    menuMagisAllVegStates.setText("All Vegetative States");
    menuMagisAllVegStates.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuMagisAllVegStates_actionPerformed(e);
        }
    });
    menuUtilityDeleteUnits.setEnabled(false);
    menuUtilityDeleteUnits.setText("Delete Units...");
    menuUtilityDeleteUnits.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuUtilityDeleteUnits_actionPerformed(e);
        }
    });
    menuReportsFireSuppCostAll.setEnabled(false);
    menuReportsFireSuppCostAll.setText("Fire Supp Cost (all runs)");
    menuReportsFireSuppCostAll.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuReportsFireSuppCostAll_actionPerformed(e);
        }
    });
    menuSysKnowWSBWLogic.setEnabled(false);
    menuSysKnowWSBWLogic.setText("Western Spruce Budworm Logic");
    menuSysKnowWSBWLogic.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuSysKnowWSBWLogic_actionPerformed(e);
        }
    });
    menuGisUpdateSpread.setText("Update and Spread Files");
    menuGisUpdateSpread.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuGisUpdateSpread_actionPerformed(e);
      }
    });
    menuResultLandformUnit.setEnabled(false);
    menuResultLandformUnit.setText("Landform Unit Analysis");
    menuResultLandformUnit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuResultLandformUnit_actionPerformed(e);
      }
    });
    menuResultLandformSum.setEnabled(false);
    menuResultLandformSum.setText("Landform Condition Summary");
    menuResultLandformSum.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuResultLandformSum_actionPerformed(e);
      }
    });

    menuSysKnowVegTreatDesired.setEnabled(false);
    menuSysKnowVegTreatDesired.setText("Desired Future Conditions");
    menuSysKnowVegTreatDesired.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowVegTreatDesired_actionPerformed(e);
      }
    });
    menuUtilityGISFiles.setEnabled(false);
    menuUtilityGISFiles.setText("Copy GIS Files");
    menuUtilityGISFiles.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityGISFiles_actionPerformed(e);
      }
    });
    menuSysKnowFireSeason.setEnabled(false);
    menuSysKnowFireSeason.setText("Fire Season");
    menuSysKnowFireSeason.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuSysKnowFireSeason_actionPerformed(e);
        }
    });
    menuSysKnowCellPerc.setEnabled(false);
    menuSysKnowCellPerc.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          menuSysKnowCellPerc_actionPerformed(e);
        }
    });
    menuHelpUserGuide.setText("User\'s Guide");
    menuHelpUserGuide.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuHelpUserGuide_actionPerformed(e);
        }
    });
    MenuUtilityJavaHeap.setText("Change Java Heap Size");
    MenuUtilityJavaHeap.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            MenuUtilityJavaHeap_actionPerformed(e);
        }
    });
    menuSysKnowFireSuppProdRate.setText("Production Rate");
    menuSysKnowFireSuppProdRate.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuSysKnowFireSuppProdRate_actionPerformed(e);
        }
    });
    menuSysKnowFireSuppSpreadRate.setText("Spread Rate");
    menuSysKnowFireSuppSpreadRate.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuSysKnowFireSuppSpreadRate_actionPerformed(e);
        }
    });
    menuSysKnowFireSuppResponseTime.setText("Response Time");
    menuSysKnowFireSuppResponseTime.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowFireSuppResponseTime_actionPerformed(e);
      }
    });
    menuSysKnowSpeciesKnowledgeEditor.setEnabled(false);
    menuSysKnowSpeciesKnowledgeEditor.setText("Species Attribute Editor");
    menuSysKnowSpeciesKnowledgeEditor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowSpeciesKnowledgeEditor_actionPerformed(e);
      }
    });
    menuUtilityZoneEdit.setText("Enable Zone Editing");
    menuUtilityZoneEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, java.awt.event.KeyEvent.CTRL_MASK | java.awt.event.KeyEvent.ALT_MASK | java.awt.event.KeyEvent.SHIFT_MASK, false));
    menuUtilityZoneEdit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuUtilityZoneEdit_actionPerformed(e);
        }
    });
    menuImportFSLandscape.setText("Run FS Landscape");
    menuImportFSLandscape.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuImportFSLandscape_actionPerformed(e);
      }
    });
    menuSysKnowImportProcesses.setEnabled(false);
    menuSysKnowImportProcesses.setText("Import Zone Processes");
    menuSysKnowImportProcesses.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowImportProcesses_actionPerformed(e);
      }
    });
    menuSysKnowShowLegalProcesses.setEnabled(false);
    menuSysKnowShowLegalProcesses.setText("Show Zone Processes");
    menuSysKnowShowLegalProcesses.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowShowLegalProcesses_actionPerformed(e);
      }
    });
    menuSysKnowShowZoneTreatments.setEnabled(false);
    menuSysKnowShowZoneTreatments.setText("Show Zone Treatments");
    menuSysKnowShowZoneTreatments.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowShowZoneTreatments_actionPerformed(e);
      }
    });
    menuSysKnowImportZoneTreatments.setEnabled(false);
    menuSysKnowImportZoneTreatments.setText("Import Zone Treatments");
    menuSysKnowImportZoneTreatments.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowImportZoneTreatments_actionPerformed(e);
      }
    });
    menuFileSaveZone.setEnabled(false);
    menuFileSaveZone.setText("Save Zone");
    menuFileSaveZone.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSaveZone_actionPerformed(e);
      }
    });
    menuReportsAllStates.setEnabled(false);
    menuReportsAllStates.setText("All States Report");
    menuReportsAllStates.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuReportsAllStates_actionPerformed(e);
      }
    });
    buildSimpplleTypeFiles.setEnabled(true);
    buildSimpplleTypeFiles.setText("Build All SimpplleType Files");
    buildSimpplleTypeFiles.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buildSimpplleTypeFiles_actionPerformed(e);
      }
    });
    buildSimpplleTypesSource.setActionCommand("Build  SimpplleTypes Source");
    buildSimpplleTypesSource.setText("Build SimpplleTypes Source");
    buildSimpplleTypesSource.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buildSimpplleTypesSource_actionPerformed(e);
      }
    });
    menuSysKnowWindthrow.setEnabled(false);
    menuSysKnowWindthrow.setText("Windthrow");
    menuSysKnowWindthrow.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowWindthrow_actionPerformed(e);
      }
    });
    menuSysKnowWildlifeBrowsing.setEnabled(false);
    menuSysKnowWildlifeBrowsing.setText("Wildlife Browsing");
    menuSysKnowWildlifeBrowsing.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowWildlifeBrowsing_actionPerformed(e);
      }
    });
    menuSysKnowTussockMoth.setEnabled(false);
    menuSysKnowTussockMoth.setText("Tussock Moth");
    menuSysKnowTussockMoth.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowTussockMoth_actionPerformed(e);
      }
    });
    menuUtilityTestNewDialog.setEnabled(false);
    menuUtilityTestNewDialog.setText("New Dialog Testing");
    menuUtilityTestNewDialog.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityTestNewDialog_actionPerformed(e);
      }
    });
    menuSysKnowProcessProbLogic.setEnabled(false);
    menuSysKnowProcessProbLogic.setText("Process Probability Logic");
    menuSysKnowProcessProbLogic.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowProcessProbLogic_actionPerformed(e);
      }
    });
    menuSysKnowDisableWsbw.setEnabled(false);
    menuSysKnowDisableWsbw.setText("Disable Western Spruce Budworm");
    menuSysKnowDisableWsbw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowDisableWsbw_actionPerformed(e);
      }
    });
    //Quack - Disable WSBW Logic
    //Disables WSBW logic manually, instead of using menu item directly above, since that has been hidden
	Wsbw.setEnabled(false);

    menuUtilityDatabaseManager.setEnabled(false);
    menuUtilityDatabaseManager.setText("DatabaseManager");
    menuUtilityDatabaseManager.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityDatabaseManager_actionPerformed(e);
      }
    });
    menuBisonGrazingLogic.setText("Bison Grazing Logic");
    menuBisonGrazingLogic.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuBisonGrazingLogic_actionPerformed(e);
      }
    });
    menuUtilityMemoryUse.setText("Memory Use Display");
    menuUtilityMemoryUse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityMemoryUse_actionPerformed(e);
      }
    });
    menuSysKnowDoCompetition.setEnabled(false);
    menuSysKnowDoCompetition.setText("Lifeform Competition");
    menuSysKnowDoCompetition.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowDoCompetition_actionPerformed(e);
      }
    });
    menuSysKnowGapProcessLogic.setEnabled(false);
    menuSysKnowGapProcessLogic.setText("Gap Process Logic");
    menuSysKnowGapProcessLogic.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowGapProcessLogic_actionPerformed(e);
      }
    });
    menuSysKnowProducingSeedLogic.setEnabled(false);
    menuSysKnowProducingSeedLogic.setText("Producing Seed Logic");
    menuSysKnowProducingSeedLogic.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowProducingSeedLogic_actionPerformed(e);
      }
    });
    menuSysKnowVegUnitFireTypeLogic.setEnabled(false);
    menuSysKnowVegUnitFireTypeLogic.setText("Vegetative -- Unit Fire Type Logic");
    menuSysKnowVegUnitFireTypeLogic.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowVegUnitFireTypeLogic_actionPerformed(e);
      }
    });

    menuSysKnowInvasive.setEnabled(false);
    menuSysKnowInvasive.setText("Invasive Species Logic");
    menuSysKnowInvasiveLogicR1.setEnabled(false);
    menuSysKnowInvasiveLogicR1.setText("Region One Logic");
    menuSysKnowInvasiveLogicR1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowInvasiveLogicR1_actionPerformed(e);
      }
    });
    menuSysKnowInvasiveLogicMSU.setEnabled(false);
    menuSysKnowInvasiveLogicMSU.setText("Montana State University Logic");
    menuSysKnowInvasiveLogicMSU.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowInvasiveLogicMSU_actionPerformed(e);
      }
    });
    menuSysKnowInvasiveLogicMesaVerdeNP.setEnabled(false);
    menuSysKnowInvasiveLogicMesaVerdeNP.setText("Mesa Verde NP");
    menuSysKnowInvasiveLogicMesaVerdeNP.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuSysKnowInvasiveLogicMesaVerdeNP_actionPerformed(e);
      }
    });
    menuUtilityMakeAreaMultipleLife.setEnabled(false);
    menuUtilityMakeAreaMultipleLife.setText("Change Area to Multiple Lifeform");
    menuUtilityMakeAreaMultipleLife.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityMakeAreaMultipleLife_actionPerformed(e);
      }
    });
    menuReportsTrackingSpecies.setEnabled(false);
    menuReportsTrackingSpecies.setText("Tracking Species Report");
    menuReportsTrackingSpecies.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuReportsTrackingSpecies_actionPerformed(e);
      }
    });
    menuUtilityCombineLSFiles.setText("Combine LS Files");
    menuUtilityCombineLSFiles.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityCombineLSFiles_actionPerformed(e);
      }
    });
    menuFile.add(menuFileSave);
    menuFile.addSeparator();
    menuFile.add(menuFileSaveZone);
    menuFile.addSeparator();
    menuFile.addSeparator();
    menuFile.add(menuFileWorkDir);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    menuBar1.add(menuFile);
    menuBar1.add(menuSysKnow);
    menuBar1.add(menuImport);
    menuBar1.add(menuExport);
    menuBar1.add(menuReports);
    menuBar1.add(menuInterpretations);
    menuBar1.add(menuViewResult);
    menuBar1.add(menuUtility);
    menuBar1.add(menuHelp);
    this.setJMenuBar(menuBar1);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    this.getContentPane().add(northPanel, BorderLayout.NORTH);
    northPanel.add(currentPanel, BorderLayout.SOUTH);
    currentPanel.add(zonePanel, null);
    zonePanel.add(zoneLabel, null);
    zonePanel.add(zoneValueLabel, null);
    currentPanel.add(areaPanel, null);
    areaPanel.add(areaLabel, null);
    areaPanel.add(areaValueLabel, null);
    areaPanel.add(areaInvalidLabel, null);
    northPanel.add(toolBar, BorderLayout.NORTH);
    toolBar.add(newZone);
    toolBar.add(newArea);
    toolBar.add(runSimulation);
    menuUtility.add(menuUtilityAreaName);
    menuUtility.add(menuUtilityUnitEditor);
    
    menuUtility.add(menuUtilityElevRelPos);
    menuUtilityElevRelPos.addActionListener(new MenuUtilityElevRelPosActionListener());
    menuUtilityElevRelPos.setText("Elevation Relative Position ...");
    
    menuUtility.add(menuUtilitySwapRowCol);
    menuUtilitySwapRowCol.addActionListener(new MenuUtilitySwapRowColActionListener());
    menuUtilitySwapRowCol.setText("Swap ROW/COL");
    menuUtility.addSeparator();
    menuUtility.add(menuUtilityCombineLSFiles);
    menuUtility.addSeparator();
    menuUtility.add(menuUtilityGISFiles);
    
    menuUtility.add(menuUtilityExportPathways);
    menuUtilityExportPathways.addActionListener(new MenuUtilityExportPathwaysActionListener());
    menuUtilityExportPathways.setText("Export Pathways");
    menuUtilityExportPathways.setEnabled(false);
    menuUtility.addSeparator();
    menuUtility.add(menuUtilityDeleteUnits);
    menuUtility.addSeparator();
    menuUtility.add(menuUtilitySimReady);
    menuUtility.addSeparator();
    menuUtility.add(menuUtilityMakeAreaMultipleLife);
    menuUtility.addSeparator();
    menuUtility.add(menuUtilityReset);
    menuUtility.addSeparator();
    menuUtility.add(menuUtilityPrintArea);
    menuUtility.addSeparator();
    menuUtility.add(menuMagis);
    menuUtility.addSeparator();
    menuUtility.add(menuUtilitiesConsole);
    menuUtility.add(MenuUtilityJavaHeap);
    menuUtility.add(menuUtilityDatabaseTest);
    menuUtility.add(menuUtilityDatabaseManager);
    menuUtility.add(menuUtilityZoneEdit);
    menuUtility.add(buildSimpplleTypeFiles);
    menuUtility.add(buildSimpplleTypesSource);
    menuUtility.add(menuUtilityTestNewDialog);
    menuUtility.add(menuUtilityMemoryUse);
    menuSysKnow.add(menuSysKnowPath);
    menuSysKnow.addSeparator();
    menuSysKnow.add(menuSysKnowVegProc);
    menuSysKnow.add(jMenuItem1);
    menuSysKnow.addSeparator();
    menuSysKnow.add(menuSysKnowVegTreat);
    menuSysKnow.add(menuSysKnowAquaticTreat);
    menuSysKnow.addSeparator();
    menuSysKnow.add(menuSysKnowRegionalClimate);
    menuSysKnow.addSeparator();
    menuSysKnow.add(menuSysKnowOpen);
    menuSysKnow.add(menuSysKnowSave);
    menuSysKnow.addSeparator();
    menuSysKnow.add(menuSysKnowRestoreDefaults);
    menuSysKnowPath.add(menuSysKnowPathVeg);
    menuSysKnowPath.add(menuSysKnowPathAquatic);
    menuSysKnowVegProc.add(menuSysKnowVegProcLock);
    menuSysKnowVegProc.addSeparator();
    menuSysKnowVegProc.add(menuSysKnowFireEventLogic);
    menuSysKnowVegProc.add(menuSysKnowFireProb);
    menuSysKnowVegProc.add(menuSysKnowFireCost);
    menuSysKnowVegProc.add(menuSysKnowFireInput);
    menuSysKnowVegProc.add(menuSysKnowFireSeason);
    menuSysKnowVegProc.add(menuSysKnowFireSpread);
    menuSysKnowFireSpread.add(menuSysKnowCellPerc);
    menuSysKnowVegProc.add(menuSysKnowFireSuppLogic);
    menuSysKnowVegProc.add(menuSysKnowWeatherEvent);
    menuSysKnowVegProc.add(menuSysKnowVegUnitFireTypeLogic);
    menuSysKnowVegProc.addSeparator();
    menuSysKnowVegProc.add(menuSysKnowSpeciesKnowledgeEditor);
    menuSysKnowVegProc.addSeparator();
    menuSysKnowVegProc.add(menuBisonGrazingLogic);
    //Quack - WSBW Logic Menu
    //menuSysKnowVegProc.add(menuSysKnowWSBWLogic);
    //menuSysKnowVegProc.add(menuSysKnowDisableWsbw);
    menuSysKnowVegProc.add(menuSysKnowProcessProbLogic);
    menuSysKnowVegProc.add(menuSysKnowGapProcessLogic);
    menuSysKnowVegProc.add(menuSysKnowInsectDiseaseSpread);
    menuSysKnowVegProc.add(menuSysKnowWindthrow);
    menuSysKnowVegProc.add(menuSysKnowWildlifeBrowsing);
    menuSysKnowVegProc.add(menuSysKnowTussockMoth);
    menuSysKnowVegProc.addSeparator();
    menuSysKnowVegProc.add(menuSysKnowInvasive);
    menuSysKnowVegProc.addSeparator();
    menuSysKnowVegProc.add(menuSysKnowUseRegenPulse);
    menuSysKnowVegProc.add(menuSysKnowRegen);
    menuSysKnowVegProc.add(menuSysKnowProducingSeedLogic);
    menuSysKnowVegProc.add(menuSysKnowConiferEncroach);
    menuSysKnowVegProc.addSeparator();
    menuSysKnowVegProc.add(menuSysKnowDoCompetition);
    menuSysKnowVegProc.addSeparator();
    menuSysKnowVegProc.add(menuSysKnowShowLegalProcesses);
    menuSysKnowVegProc.add(menuSysKnowImportProcesses);
    menuImport.addSeparator();
    menuImport.add(menuImportCreate);
    menuImport.add(menuImportAttributeData);
    menuImport.addSeparator();
    menuImport.add(menuImportFixStates);
    menuImport.add(menuImportEditUnits);
    menuImport.add(menuImportInvalidReport);
    menuImport.addSeparator();
    menuImport.add(menuImportFSLandscape);
    menuExport.add(menuExportGIS);
    menuExport.add(menuExportAttributes);
    menuExportGIS.add(menuGisUpdateSpread);
    menuExportGIS.add(menuExportGISDecadeProb);
    menuExportGIS.add(menuExportGISReburn);
    menuReports.add(menuReportsSummary);
    menuReports.add(menuReportsUnit);
    menuReports.add(menuReportsAllStates);
    menuReports.add(menuReportsTrackingSpecies);
    menuReports.add(menuReportsMult);
    menuReports.add(menuReportsFire);
    menuReports.add(menuReportsFireCost);
    menuReports.add(menuReportsFireSuppCostAll);
    menuReports.add(menuReportsEmission);
    menuReportsMult.add(menuReportsMultNormal);
    menuReportsMult.add(menuReportsMultSpecial);
    menuReportsMult.add(menuReportsMultOwner);
    menuInterpretations.add(menuInterpretProbCalc);
    menuInterpretations.add(menuInterpretRestoration);
    menuInterpretations.add(menuInterpretWildlife);
    menuViewResult.add(menuResultVegUnit);
    menuViewResult.add(menuResultVegSum);
    menuViewResult.addSeparator();
    menuViewResult.add(menuResultAquaticUnit);
    menuViewResult.add(menuResultAquaticSum);
    menuViewResult.addSeparator();
    menuViewResult.add(menuResultLandformUnit);
    menuViewResult.add(menuResultLandformSum);
    menuViewResult.addSeparator();
    menuViewResult.add(menuResultAquaticDiagram);
    menuHelp.add(menuHelpDocs);
    menuHelp.add(menuHelpUserGuide);
    menuHelp.add(menuHelpAbout);
    menuSysKnowVegTreat.add(menuSysKnowVegTreatSchedule);
    menuSysKnowVegTreat.add(menuSysKnowVegTreatLogic);
    menuSysKnowVegTreat.add(menuSysKnowVegTreatDesired);
    menuSysKnowVegTreat.addSeparator();
    menuSysKnowVegTreat.add(menuSysKnowShowZoneTreatments);
    menuSysKnowVegTreat.add(menuSysKnowImportZoneTreatments);
    menuSysKnowWeatherEvent.add(menuWeatherEventClassA);
    menuSysKnowWeatherEvent.add(menuWeatherEventNotClassA);
    menuSysKnowFireSuppLogic.add(menuSysKnowFireSuppLogicClassA);
    menuSysKnowFireSuppLogic.add(menuSysKnowFireSuppLogicBeyondClassA);
    menuSysKnowFireSuppLogic.addSeparator();
    menuSysKnowFireSuppLogic.add(menuSysKnowFireSuppProdRate);
    menuSysKnowFireSuppLogic.add(menuSysKnowFireSuppSpreadRate);
    menuSysKnowFireSuppLogic.add(menuSysKnowFireSuppResponseTime);
    menuMagis.add(menuMagisProcessTreatmentFiles);
    menuMagis.add(menuMagisAllVegStates);
    menuSysKnowInvasive.add(menuSysKnowInvasiveLogicR1);
    menuSysKnowInvasive.add(menuSysKnowInvasiveLogicMesaVerdeNP);
    menuSysKnowInvasive.add(menuSysKnowInvasiveLogicMSU);
  }

  public void refresh() {
    update(getGraphics());
  }
/**
 * Clears the status message by setting it to empty string.
 */
  public void clearStatusMessage() {
    setStatusMessage("");
  }
/**
 * Sets the status message to the string message in parameter.  Does not wait.  
 * @param msg the message to be displayed
 */
  public void setStatusMessage(String msg) {
    setStatusMessage(msg,false);
  }
  /**
   * If wait is enabled, shows a messageDialog which has the user press Ok to continue.
   * @param msg message to be displayed in the status bar.  
   * @param wait if true will ask the user to press Ok to continue.
   */
  public void setStatusMessage(String msg, boolean wait) {
    statusBar.setText(msg);
    if (wait) {
      JOptionPane.showMessageDialog(this,"Press Ok to continue","Profiling use",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    refresh();
  }
/**
 * Enables the smulation controls by getting the current simulation and setting the simulation JMenu items to enabled
 */
  public void enableSimulationControls() {
    Simulation simulation = Simpplle.getCurrentSimulation();

    menuReportsSummary.setEnabled(true);
    menuReportsUnit.setEnabled(true);
    menuReportsFire.setEnabled(true);
    menuReportsFireCost.setEnabled(simulation.fireSuppression());
    menuReportsEmission.setEnabled(true);
    menuExportGIS.setEnabled(true);
    menuFileSave.setEnabled(true);

    // We do not want the user to be able to edit units after a simulation.
    menuImportEditUnits.setEnabled(false);
    menuUtilityUnitEditor.setEnabled(false);

    menuUtilityDeleteUnits.setEnabled(false);

    if (simulation.isMultipleRun() &&
        simulation.existsMultipleRunSummary()) {
      menuReportsMult.setEnabled(true);
      if (simulation.trackOwnership()) {
        menuReportsMultOwner.setEnabled(true);
      }
      if (simulation.trackSpecialArea()) {
        menuReportsMultSpecial.setEnabled(true);
      }
      menuInterpretProbCalc.setEnabled(true);
    }
    if (simulation.isMultipleRun()) {
      menuExportGISDecadeProb.setEnabled(true);
    }
    menuImportAttributeData.setEnabled(false);

    menuReportsFireSuppCostAll.setEnabled(false);

    if (simulation.isDiscardData()) {
      menuReportsFire.setEnabled(false);
      menuExportGIS.setEnabled(false);
    }

    if ((simulation.isDiscardData() &&
        simulation.isDoAllStatesSummary() == false) ||
        (simulation.isMultipleRun() /*&& simulation.existsMultipleRunSummary()*/)) {
      menuReportsAllStates.setEnabled(false);
      menuReportsTrackingSpecies.setEnabled(false);
    }
    else {
      menuReportsAllStates.setEnabled(true);
      menuReportsTrackingSpecies.setEnabled(true);
    }
  }
/**
 * Disables simulation controls by setting the enabled simulation methods of the JMenu items to false.  
 */
  public void disableSimulationControls() {
    menuReportsSummary.setEnabled(false);
//    menuReportsUnit.setEnabled(false);
    menuReportsAllStates.setEnabled(false);
    menuReportsTrackingSpecies.setEnabled(false);
    menuReportsFire.setEnabled(false);
    menuReportsFireCost.setEnabled(false);
    menuReportsEmission.setEnabled(false);
    menuExportGIS.setEnabled(false);
    menuReportsMult.setEnabled(false);
    menuReportsMultOwner.setEnabled(false);
    menuReportsMultSpecial.setEnabled(false);
    menuExportGISDecadeProb.setEnabled(false);
    menuInterpretProbCalc.setEnabled(false);

    menuReportsFireSuppCostAll.setEnabled(true);
  }
/**
 * Enables the area JMenu items.  
 */
  public void enableAreaControls() {
    menuUtilityPrintArea.setEnabled(true);
    menuUtilityAreaName.setEnabled(true);
    runSimulation.setEnabled(true);
    menuResultVegUnit.setEnabled(true);
    menuResultVegSum.setEnabled(true);
    menuSysKnowVegTreatSchedule.setEnabled(true);
    menuSysKnowVegProcLock.setEnabled(true);
    menuUtilityUnitEditor.setEnabled(true);
    menuUtilityAreaName.setEnabled(true);
    menuUtilitySimReady.setEnabled(true);
    menuUtilityDeleteUnits.setEnabled(true);
    menuUtilityReset.setEnabled(true);

    menuFileSave.setEnabled(true);
    menuImportFixStates.setEnabled(false);
    menuImportEditUnits.setEnabled(false);
    menuImportInvalidReport.setEnabled(false);
    menuImportAttributeData.setEnabled(true);
    menuInterpretWildlife.setEnabled(simpplle.comcode.WildlifeHabitat.isZoneValid());
    menuExportAttributes.setEnabled(true);

    menuMagisProcessTreatmentFiles.setEnabled(true);

    menuReportsFireSuppCostAll.setEnabled((Simpplle.getCurrentSimulation() == null));

    menuResultAquaticUnit.setEnabled((Simpplle.getCurrentArea().existAquaticUnits()));

    menuResultLandformUnit.setEnabled((Simpplle.getCurrentArea().existLandUnits()));

    menuReportsUnit.setEnabled(true);

    menuUtilityMakeAreaMultipleLife.setEnabled(true);
    
    menuUtilitySwapRowCol.setEnabled(true);
   }
/**
 * Disables Jmenu items by first checking if there is an invalid units and enabling or disabling Jmenu items to fix the states.  
 * THen calls the  disableAreaControls() which sets the enabled methods of all area JMenu items to false.  
 * @param invalidUnits
 */
  public void disableAreaControls(boolean invalidUnits) {
    menuImportFixStates.setEnabled(invalidUnits);
    menuImportEditUnits.setEnabled(invalidUnits);
    menuImportInvalidReport.setEnabled(invalidUnits);
    menuUtilityDeleteUnits.setEnabled(false);
    disableAreaControls();
  }
/**
 * Sets the enabled methods of all area JMenu items to false.
 */
  public void disableAreaControls() {
    menuUtilityPrintArea.setEnabled(false);
    runSimulation.setEnabled(false);
    menuResultVegUnit.setEnabled(false);
    menuResultAquaticUnit.setEnabled(false);
    menuResultVegSum.setEnabled(false);
    menuSysKnowVegTreatSchedule.setEnabled(false);
    menuSysKnowVegProcLock.setEnabled(false);
    menuUtilityUnitEditor.setEnabled(false);
    menuUtilityAreaName.setEnabled(false);
    menuUtilitySimReady.setEnabled(false);
    menuInterpretWildlife.setEnabled(false);
    menuExportAttributes.setEnabled(false);
    menuUtilityDeleteUnits.setEnabled(false);
    menuUtilityReset.setEnabled(false);

    menuMagisProcessTreatmentFiles.setEnabled(true);

    menuFileSave.setEnabled(false);

    menuReportsFireSuppCostAll.setEnabled(false);

    menuUtilityMakeAreaMultipleLife.setEnabled(false);
    menuUtilitySwapRowCol.setEnabled(false);
  }
/**
 * Gets the current area and checks if there are any invalid vegetative units.  
 * If there are invalid Evu's  enables or Jmenu items to fix the states and lets the user know there are invalid Evu
 * Then calls the  disableAreaControls() which sets the enabled methods of all area JMenu items to false.  
 * If there are it 
 */
  public void updateAreaValidity() {
    if (Simpplle.getCurrentArea().existAnyInvalidVegUnits()) {
      disableAreaControls(true);
      areaInvalidLabel.setText("(Invalid)");
    }
    else {
      enableAreaControls();
      areaInvalidLabel.setText("");
    }
  }
  /**
   * Enables the zone JMenu items.  Unlike the simulation and area enable controls methods, this also has specific menu items for particular zones.  
   *  
   */
  public void enableZoneControls() {
    newArea.setEnabled(true);
    menuSysKnowFireEventLogic.setEnabled(true);
    menuSysKnowFireCost.setEnabled(true);
    menuSysKnowFireInput.setEnabled(true);
    menuSysKnowFireProb.setEnabled(true);
    menuSysKnowRegionalClimate.setEnabled(true);
    menuImportCreate.setEnabled(true);
    menuSysKnowPath.setEnabled(true);
    menuSysKnowPathVeg.setEnabled(true);
    menuSysKnowFireSuppLogic.setEnabled(true);
    menuSysKnowWeatherEvent.setEnabled(true);
    menuSysKnowVegTreat.setEnabled(true);
    menuSysKnowVegTreatLogic.setEnabled(true);
    menuMagis.setEnabled(true);
    menuMagisAllVegStates.setEnabled(true);
    menuSysKnowWSBWLogic.setEnabled(true);
    menuSysKnowRegen.setEnabled(true);
    menuSysKnowDisableWsbw.setEnabled(true);

    menuSysKnowFireSeason.setEnabled(true);
    menuSysKnowCellPerc.setEnabled(true);

    menuSysKnowPathAquatic.setEnabled(Simpplle.getCurrentZone().hasAquatics());

    menuUtilityGISFiles.setEnabled(JSimpplle.isWindowsOS());
    menuUtilityExportPathways.setEnabled(true);
    menuSysKnowSpeciesKnowledgeEditor.setEnabled(true);
    menuSysKnowOpen.setEnabled(true);
    menuSysKnowSave.setEnabled(true);
    menuSysKnowRestoreDefaults.setEnabled(true);

    menuSysKnowConiferEncroach.setEnabled(true);
    
    boolean doConiferEncroach =
      Simpplle.getCurrentZone().getId() == ValidZones.EASTSIDE_REGION_ONE ||
      Simpplle.getCurrentZone().getId() == ValidZones.TETON ||
      Simpplle.getCurrentZone().getId() == ValidZones.NORTHERN_CENTRAL_ROCKIES;
      
    menuSysKnowConiferEncroach.setVisible(doConiferEncroach);


    boolean enableZoneEditControls =
        Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_FRONT_RANGE ||
        Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_PLATEAU ||
        Simpplle.getCurrentZone().getId() == ValidZones.WESTERN_GREAT_PLAINS_STEPPE ||
        Simpplle.getCurrentZone().getId() == ValidZones.GREAT_PLAINS_STEPPE ||
        Simpplle.getCurrentZone().getId() == ValidZones.MIXED_GRASS_PRAIRIE;

    menuSysKnowImportProcesses.setEnabled(enableZoneEditControls);
    menuSysKnowShowLegalProcesses.setEnabled(enableZoneEditControls);
    menuSysKnowShowZoneTreatments.setEnabled(enableZoneEditControls);
    menuSysKnowImportZoneTreatments.setEnabled(enableZoneEditControls);
    menuFileSaveZone.setEnabled(enableZoneEditControls);

    menuSysKnowProcessProbLogic.setEnabled(true);
    menuSysKnowProcessProbLogic.setVisible(true);

    menuSysKnowImportProcesses.setVisible(enableZoneEditControls);
    menuSysKnowShowLegalProcesses.setVisible(enableZoneEditControls);
    menuSysKnowShowZoneTreatments.setVisible(enableZoneEditControls);
    menuSysKnowImportZoneTreatments.setVisible(enableZoneEditControls);
    menuFileSaveZone.setVisible(enableZoneEditControls);

    buildSimpplleTypeFiles.setVisible(JSimpplle.developerMode());
    buildSimpplleTypesSource.setVisible(JSimpplle.developerMode());

    boolean isColorado =  (Simpplle.getCurrentZone() instanceof ColoradoFrontRange);
    menuSysKnowWindthrow.setVisible(isColorado);
    menuSysKnowWindthrow.setEnabled(isColorado);
    menuSysKnowWildlifeBrowsing.setVisible(isColorado);
    menuSysKnowWildlifeBrowsing.setEnabled(isColorado);
    menuSysKnowTussockMoth.setEnabled(isColorado);
    menuSysKnowTussockMoth.setVisible(isColorado);



    boolean isColoradoPlateau =  (Simpplle.getCurrentZone() instanceof ColoradoPlateau);
    menuSysKnowWindthrow.setVisible(isColoradoPlateau);
    menuSysKnowWindthrow.setEnabled(isColoradoPlateau);
    menuSysKnowWildlifeBrowsing.setVisible(isColoradoPlateau);
    menuSysKnowWildlifeBrowsing.setEnabled(isColoradoPlateau);

    menuWeatherEventClassA.setEnabled(! (isColoradoPlateau ));
    menuWeatherEventClassA.setVisible(! (isColoradoPlateau ));

    boolean isWyoming =  (RegionalZone.isWyoming());
    menuBisonGrazingLogic.setEnabled(isWyoming);
    menuBisonGrazingLogic.setVisible(isWyoming);
    menuSysKnowProcessProbLogic.setVisible(!isWyoming);

    menuSysKnowDoCompetition.setEnabled(true);
    menuSysKnowGapProcessLogic.setEnabled(true);
    menuSysKnowProducingSeedLogic.setEnabled(true);
    menuSysKnowVegUnitFireTypeLogic.setEnabled(true);

    menuSysKnowInvasive.setEnabled(true);
    menuSysKnowInvasiveLogicR1.setEnabled(false);
    menuSysKnowInvasiveLogicMesaVerdeNP.setEnabled(true);
    menuSysKnowInvasiveLogicMSU.setEnabled(true);
    
  }

  // Disabling of zone controls not necessary.
  // One currently cannot unload the current zone.
  public void setWaitState(String msg) {
    setCursor(Utility.getWaitCursor());
    setStatusMessage(msg);
  }
/**
 * Sets the cursor to normal cursor location.
 */
  public void setNormalState() {
    clearStatusMessage();
    setCursor(Utility.getNormalCursor());
  }
/**
 * Sets the dialog location in relation to the Simpplle Main frame
 * @param dlg dialog whose location will be set.
 */
  public void setDialogLocation(JDialog dlg) {
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    Point loc = getLocation();
    int screen_height = (int) dim.height;
    int screen_width = (int) dim.width;

    int x, y;
    x = (frmSize.width - dlgSize.width) / 2 + loc.x;
    y = (loc.y + frmSize.height) - statusBar.getHeight() - dlgSize.height - 5;
    if (x < 0) { x = 0; }
    if (y < 0) { y = 0; }

    // If dialog appears off screen, set to the center
    if (x > screen_width || y > screen_height) {
      x = screen_width / 2;
      y = screen_height / 2;
    }
    dlg.setLocation(x,y);
  }

  // Event Handlers
  // **************

  /**
   * File Exit action performed handler.  will write the properties file.  and close the hibernate connection.
   * @param e
   */
  public void fileExit_actionPerformed(ActionEvent e) {
    JSimpplle.writePropertiesFile();
    try {
      simpplle.comcode.DatabaseCreator.closeHibernate();
    }
    catch (SimpplleError ex) {
    }
    System.exit(0);
  }

  //Overridden so we can exit on System Close
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if(e.getID() == WindowEvent.WINDOW_CLOSING) {
      fileExit_actionPerformed(null);
    }
  }
/**
 * Displays the 'about OpenSimpplle' dialog
 * @param e
 */
  void menuHelpAbout_actionPerformed(ActionEvent e) {
    SimpplleMain_AboutBox dlg = new SimpplleMain_AboutBox(this);
    setDialogLocation(dlg);
    dlg.setModal(true);
    dlg.setVisible(true);
  }
/**
 * Allows the user to select a zone.  Then enables zone controls, and disables area contols.    
 * @param e
 */
  void newZone_actionPerformed(ActionEvent e) {
    String str;
    simpplle.comcode.RegionalZone zone;

    NewZone   dlg = new NewZone(this,true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    if (Simpplle.getCurrentZone() != null && dlg.isNewZone()) {
      enableZoneControls();
      disableAreaControls();
      zone = Simpplle.getCurrentZone();
      if (zone.isHistoric()) {
        str = zone.getName() + " (Historic Pathways)";
      }
      else {
        str = zone.getName();
      }
      zoneValueLabel.setText(str);
      areaValueLabel.setText("");
    }
    refresh();
  }
/**
 * Gets the previous simulation files 
 * @return
 */
  private File[] getPreviousSimulationFiles() {
    ChooseSimulation dlg = new ChooseSimulation(this,"Choose Simulation",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);

    simpplle.comcode.Simpplle.setRecreateMrSummary(dlg.recreateMrSummary());
    return dlg.getFiles();
  }
  /**
   * Gets the area to be used depending on user input.  Choices are Sample, user defined,
   * previous, previous old, or none (if nothing selected)
   */
  void newArea_actionPerformed() {
    int           choice;
    File          outfile;
    NewArea       dlg = new NewArea(this,true);
    NewSampleArea sampleDlg;
    String        str;
    MyFileFilter  extFilter = new MyFileFilter("area",
                                               "OpenSimpplle Area Files (*.area)");
    @SuppressWarnings("unused")
    MyFileFilter  simFilter = new MyFileFilter("simdata",
                                               "OpenSimpplle Simulation Data Files (*.simdata)");

    setDialogLocation(dlg);
    dlg.setVisible(true);
    choice = dlg.getChoice();
    try {
      if (choice == NewArea.SAMPLE) {
        sampleDlg = new NewSampleArea(this,true);
        setDialogLocation(sampleDlg);
        sampleDlg.setVisible(true);
      }
      else if (choice == NewArea.USER_DEFINED) {
        outfile = Utility.getOpenFile(this,"User Defined Area File?",extFilter);
        if (outfile != null) {
          setWaitState("Loading User Defined Area ...");
          comcode.loadArea(outfile);
        }
      }
      else if (choice == NewArea.PREVIOUS) {
        File[] files = getPreviousSimulationFiles();
        if (files != null) {
          setWaitState("Loading Previously Simulated Area ...");
          comcode.loadPreviousSimulation(files);
        }
      }
      else if (choice == NewArea.PREVIOUS_OLD) {
        outfile = Utility.getOpenFile(this, "Previously Simulated Data File?",
                                      extFilter);
        if (outfile != null) {
          setWaitState("Loading Previously Simulated Area ...");
          comcode.loadArea(outfile);
        }
      }
      else if (choice == NewArea.NONE) {
        setNormalState();
        return;
      }
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
                                    JOptionPane.ERROR_MESSAGE);
    }
    finally {
      setNormalState();
    }

    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      if ((choice == NewArea.SAMPLE) || (choice == NewArea.USER_DEFINED)) {
        doInvalidAreaCheck();
      }
      FireEvent.setUseRegenPulse(area.getName().equalsIgnoreCase("cheesman"));
      menuSysKnowUseRegenPulse.setSelected(FireEvent.useRegenPulse());
      enableAreaControls();
      if (Simpplle.getCurrentSimulation() != null) {
        disableSimulationControls();
        enableSimulationControls();
      }
      else {
        disableSimulationControls();
      }
      str = area.getName();
      areaValueLabel.setText(str);
//      areaInvalidLabel.setText("");
      area.setMultipleLifeformStatus();
      updateSpreadModels(area.getHasKeaneAttributes());

      // Some areas seem to have been created incorrectly so we need
      // to make sure that if they only have one lifeform in all units
      // that we make sure that lifeform is marked as NA.
      if (!area.multipleLifeformsEnabled() &&
          choice != NewArea.PREVIOUS &&
          choice != NewArea.PREVIOUS_OLD) {
        // Need to change Evu's to be single lifeform;
        Evu[] evus = area.getAllEvu();
        for (Evu evu : evus) {
          if (evu != null) {
            evu.makeSingleLife();
          }
        }
      }
      TrackingSpeciesReportData.getInstance().initialize();

      FireSuppWeatherData.setMaxToAreaAcres();
    }
    else {
      disableAreaControls();
      disableSimulationControls();
    }
    refresh();
  }
/**
 * Handles the action event when user chooses to run simulation.  
 * @param e
 */
  void runSimulation_actionPerformed(ActionEvent e) {
    SimParam  dlg = new SimParam(this, fireSpreadModels);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }
/**
 * Allows a user to select a working directory which is then set in a JFileChooser.  
 * @param e
 */
  void menuFileWorkDir_actionPerformed(ActionEvent e) {
    File         workingDir;
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    String       msg = "Select the new working diretory and press Ok.";

    chooser.setDialogTitle(msg);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setApproveButtonToolTipText(msg);
    int returnVal = chooser.showDialog(this,"Ok");
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      workingDir = chooser.getSelectedFile();
      JSimpplle.setWorkingDir(workingDir);
    }
    refresh();
  }
/**
 * Resizes a component.  
 * @param e
 */
  void this_componentResized(ComponentEvent e) {
    Dimension d = getSize();

    if (d.width < MIN_WIDTH && d.height < MIN_HEIGHT) {
      setSize(MIN_WIDTH,MIN_HEIGHT);
    }
    else if (d.width < MIN_WIDTH) {
      setSize(MIN_WIDTH,d.height);
    }
    else if (d.height < MIN_HEIGHT) {
      setSize(d.width,MIN_HEIGHT);
    }
  }

  /**
    * Used to restore the units to initial conditions. 
    * It is used to restore the units to their original state, as well as deleting any simulation related stuff. 
    * This is no longer used before a simulation, the reseting of things is done as needed when the simulation is run. The name resetSimulation is kept primary for consistency with prior versions of this software.
    */
  void menuUtilityReset_actionPerformed(ActionEvent e) {
    Simpplle.resetSimulation();
    disableSimulationControls();
    JOptionPane.showMessageDialog(this,"Reset Complete","Reset Complete",
                                  JOptionPane.INFORMATION_MESSAGE);
  }

  void menuReportsSummary_actionPerformed(ActionEvent e) {
    ReportOption dlg = new ReportOption(this,"Choose an Option",true,false);

    setDialogLocation(dlg);
    dlg.setVisible(true);

    int selection = dlg.getSelection();
    if (selection == ReportOption.NO_SELECTION) { return; }

    File outfile;

    outfile = Utility.getSaveFile(this,"Summary Report");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      try {
        comcode.summaryReport(outfile,selection,dlg.isCombineLifeforms());
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuReportsUnit_actionPerformed(ActionEvent e) {
    File outfile;
    Area area = Simpplle.getCurrentArea();

    outfile = Utility.getSaveFile(this,"Individual Unit Report");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      try {
        area.printIndividualSummary(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }

  }

  void menuReportsAllStates_actionPerformed(ActionEvent e) {
    File rulesFile=null;
    if (Simulation.getInstance().isDiscardData() == false) {

      String msg =
        "Do you wish to load a file with rules to customize this report?";
      int choice = JOptionPane.showConfirmDialog(this, msg,
                                                 "Load Customization File",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.WARNING_MESSAGE);

      rulesFile = null;

      if (choice == JOptionPane.YES_OPTION) {
        rulesFile = Utility.getOpenFile(this, "All States Customization file");
      }
    }


    File outfile = Utility.getSaveFile(this,"All States Report Output");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      try {
        if (Simulation.getInstance().isDoAllStatesSummary() == false) {
          Simulation.getInstance().doAllStatesSummaryAllTimeSteps(rulesFile);
        }
        if (rulesFile != null) {
          Reports.generateAllStatesReport(rulesFile,outfile);
        }
        else {
          Reports.generateAllStatesReport(outfile);
        }
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }

  }
/**
 * Handles the event when tracking species menu item is selected.  It creates a new Tracking Species report dialog
 * @param e
 */
  public void menuReportsTrackingSpecies_actionPerformed(ActionEvent e) {
    TrackingSpeciesReportDlg dlg = new TrackingSpeciesReportDlg(this,"Tracking Species Report Categories",true);
    dlg.setVisible(true);

    TrackingSpeciesReportData data = TrackingSpeciesReportData.getInstance();
    if (data == null || data.hasData() == false) {
      return;
    }

    File outfile = Utility.getSaveFile(this,"Tracking Species Report Output");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      try {
//        if (Simulation.getInstance().isDoTrackingSpeciesReport() == false) {
          Simulation.getInstance().doTrackingSpeciesReportAllTimeSteps();
//        }
        Reports.generateTrackingSpeciesReport(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }

  }

  void menuReportsMultNormal_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Multiple Run Summary Report");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      comcode.multipleRunSummaryReport(outfile);
      setNormalState();
    }
  }

  void menuReportsMultSpecial_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Multiple Run Summary Report By Special Area");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      comcode.saMultipleRunSummaryReport(outfile);
      setNormalState();
    }
 }

  void menuReportsMultOwner_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Multiple Run Summary Report By Ownership");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      comcode.ownershipMultipleRunSummaryReport(outfile);
      setNormalState();
    }
  }

  void menuReportsFire_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Detailed Fire Report");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      comcode.fireSpreadReport(outfile);
      setNormalState();
    }
  }

  void menuReportsFireCost_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Fire Suppression Cost Report");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      comcode.fireSuppressionCostReport(outfile);
      setNormalState();
    }
  }

  void menuReportsEmission_actionPerformed(ActionEvent e) {
    ReportOption dlg = new ReportOption(this,"Choose an Option",true);

    setDialogLocation(dlg);
    dlg.setVisible(true);

    int selection = dlg.getSelection();
    if (selection == ReportOption.NO_SELECTION) { return; }

    File outfile = Utility.getSaveFile(this,"Emissions Report");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      try {
        if (selection == Simpplle.FORMATTED) {
          comcode.emissionsReport(outfile);
        }
        else if (selection == Simpplle.CDF) {
          comcode.emissionsReportCDF(outfile);
        }
        else {
          setNormalState();
          return;
        }
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Failure",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuGisUpdateSpread_actionPerformed(ActionEvent e) {
    Lifeform lifeform = null;
    if (Area.multipleLifeformsEnabled()) {
      LifeformTypeChooser dlg = new LifeformTypeChooser(this,"Chooser Lifeform",true);
      dlg.setVisible(true);

      lifeform = dlg.getChosenLife();
      if (dlg.okPushed() == false) { return; }
    }


    File outfile=null;
//    JCheckBox databaseCB = new JCheckBox("Write GIS Database");

//    databaseCB.setSelected(false);

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
//    chooser.setAccessory(databaseCB);
    chooser.setDialogTitle("GIS Files prefix?");
    if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      outfile = chooser.getSelectedFile();
    }

    if (outfile != null) {
      setWaitState("Generating GIS Update & Spread Files ...");
      try {
        comcode.createGisUpdateSpreadFiles(outfile,lifeform);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuExportGISDecadeProb_actionPerformed(ActionEvent e) {
    File outfile=null;

    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());
    chooser.setDialogTitle("GIS Decade Probability Files prefix?");
    if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      outfile = chooser.getSelectedFile();
    }

    if (outfile != null) {
      setWaitState("Generating GIS Decade Probability Files ...");
      try {
        comcode.createGisDecadeProbabilityFiles(outfile);
      }
      catch (SimpplleError ex) {
        JOptionPane.showMessageDialog(this,ex.getMessage(),"Failure",JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }
/**
 * Reburn function is a hoped for future improvement in OpenSimpplle.  
 * @param e
 */
  void menuExportGISReburn_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Probability of Reburn File prefix?");
    if (outfile != null) {
      setWaitState("Generating GIS Reburn Probability File ...");
      try {
        Simpplle.getCurrentArea().produceReburnProbabilityFile(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error writing file",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuExportAttributes_actionPerformed(ActionEvent e) {
    String msg = "This will export two files (*.spatialrelate and *.attributesall)" +
                 " which are needed to create an area using the import function\n" +
                 "In the following dialog please provide a filename prefix.\n" +
                 "** Existing files will be overwritten **";
    JOptionPane.showMessageDialog(this,msg,"Information",JOptionPane.INFORMATION_MESSAGE);


    File outfile;
    MyFileFilter  extFilter = new MyFileFilter("spatialrelate",
                                               "SIMPPLLE Area Creation Files (*.spatialrelate)");

    outfile = Utility.getSaveFile(this,"Export Area Creation files.",extFilter);
    if (outfile != null) {
      setWaitState("Exporting Area Creation Files ...");
      try {
        Simpplle.getCurrentArea().exportCreationFiles(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Error writing file",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuUtilityPrintArea_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Print Current Area to File");
    if (outfile != null) {
      setWaitState("Printing Area to File ...");
      try {
        comcode.printCurrentArea(outfile);
      }
      catch (SimpplleError ex) {
        JOptionPane.showMessageDialog(this,ex.getMessage(),"",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuResultVegUnit_actionPerformed(ActionEvent e) {
    if (EvuAnalysis.isOpen()) { return; }

    EvuAnalysis dlg = new EvuAnalysis(this,"Vegetative Unit Analysis",false);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuResultVegSum_actionPerformed(ActionEvent e) {
    VegSummary dlg = new VegSummary(this,"Vegetative Condition Summary",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuFileSave_actionPerformed(ActionEvent e) {
    File       outfile;
    Simulation simulation = Simpplle.getCurrentSimulation();
    String     title, waitMsg;
    MyFileFilter filter = new MyFileFilter("area","Area Files (*.area)");

    if (simulation == null) {
      title   = "Save Current Area";
      waitMsg = "Saving Current Area ...";
    }
    else {
      title   = "Save Simulation";
      waitMsg = "Saving Simulation ...";
    }


    outfile = Utility.getSaveFile(this,title,filter);
    if (outfile != null) {
      setWaitState(waitMsg);
      try {
        comcode.saveCurrentArea(outfile);
      }
      catch (SimpplleError ex) {
          JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",
                                        JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }

  }

  void menuSysKnowFireEventLogic_actionPerformed(ActionEvent e) {
    FireEventLogicDialog dlg = new FireEventLogicDialog(this,"Fire Event Logic",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowFireCost_actionPerformed(ActionEvent e) {
    menuSysKnowFireInput_actionPerformed(e);
  }

  void menuSysKnowFireInput_actionPerformed(ActionEvent e) {
    FmzEditor dlg = new FmzEditor(this,"Fmz Editor", true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowFireProb_actionPerformed(ActionEvent e) {
    FireSpreadProb dlg = new FireSpreadProb(this,"Extreme Fire Spread Probability",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowFireSuppLogicBeyondClassA_actionPerformed(ActionEvent e) {
    FireSuppBeyondClassALogicDlg dlg = new FireSuppBeyondClassALogicDlg(this,"Fire Suppression Logic for Fires Beyond Class A",true);

//    FireSuppressionDialogBeyondClassA dlg =
//      new FireSuppressionDialogBeyondClassA(this,"Fire Suppression for Fires Beyond Class A",true);

    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowFireSuppLogicClassA_actionPerformed(ActionEvent e) {
    FireSuppClassALogicDlg dlg = new FireSuppClassALogicDlg(this,"Fire Suppression for Class A Fires Logic",true);

//    FireSuppression dlg = new FireSuppression(this,"Fire Suppression for Class A Fires",true);

    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuWeatherEventClassA_actionPerformed(ActionEvent e) {
    String title = "Weather Ending Events -- Fires < 0.25 acres";
    FireSuppWeatherClassALogicDlg dlg = new FireSuppWeatherClassALogicDlg(this,title,false);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuWeatherEventNotClassA_actionPerformed(ActionEvent e) {
    String title = "Weather Ending Events -- Fires > 0.25 acres";
    FireSuppWeather dlg = new FireSuppWeather(this,title,true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowVegTreatSchedule_actionPerformed(ActionEvent e) {
    TreatmentSchedule dlg;

    dlg = new TreatmentSchedule(this,"Treatment Schedule",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowVegTreatLogic_actionPerformed(ActionEvent e) {
    if (arePathwaysLoaded() == false) { return; }

    TreatmentLogic dlg;

    dlg = new TreatmentLogic(this,"Treatment Logic",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowInsectDiseaseSpread_actionPerformed(ActionEvent e) {
  }

  void menuSysKnowRegionalClimate_actionPerformed(ActionEvent e) {
    ClimateDialogWrapper dlg;

    dlg = new ClimateDialogWrapper(this,"Regional Climate Schedule",true);
    setDialogLocation(dlg.getDialog());
    dlg.getDialog().setVisible(true);
    refresh();
  }

  void menuSysKnowVegProcLock_actionPerformed(ActionEvent e) {
    ProcessSchedule dlg = new ProcessSchedule(this,"Process Schedule",true);

    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  public boolean isVegPathwayDlgOpen() { return vegPathwayDlgOpen; }
  private void setVegPathwayDlgOpen() {
    menuSysKnowPathVeg.setEnabled(false);
    vegPathwayDlgOpen = true;
  }
  public void setVegPathwayDlgClosed() {
    menuSysKnowPathVeg.setEnabled(true);
    vegPathwayDlgOpen = false;
  }

  public boolean isAquaticPathwayDlgOpen() { return aquaticPathwayDlgOpen; }
  private void setAquaticPathwayDlgOpen() {
    menuSysKnowPathAquatic.setEnabled(false);
    aquaticPathwayDlgOpen = true;
  }
  public void setAquaticPathwayDlgClosed() {
    menuSysKnowPathAquatic.setEnabled(true);
    aquaticPathwayDlgOpen = false;
  }

  void menuSysKnowPathVeg_actionPerformed(ActionEvent e) {
    if (isVegPathwayDlgOpen()) { return; }

    Pathway dlg = new Pathway(this,"Pathways",false);

    setDialogLocation(dlg);
    setVegPathwayDlgOpen();
    dlg.setVisible(true);
    refresh();
  }
  void menuSysKnowPathAquatic_actionPerformed(ActionEvent e) {
    if (isAquaticPathwayDlgOpen()) { return; }

    AquaticPathway dlg = new AquaticPathway(this,"Aquatic Pathways",false);

    setDialogLocation(dlg);
    setAquaticPathwayDlgOpen();
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowFireSeason_actionPerformed(ActionEvent e) {
    FireSeason dlg = new FireSeason(this,"Fire Season Probabilities",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuSysKnowCellPerc_actionPerformed(ActionEvent e) {
    KeaneCellPercolation dlg = new KeaneCellPercolation(this);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  private boolean deleteSimulationCheck() {
    String msg =
      "An area is loaded that has simulation data.\n" +
      "If unit data is made invalid by loading of one or more knowledge files" +
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
      return false;
    }
  }

  private void doInvalidAreaCheck() {
    Area area = Simpplle.getCurrentArea();

    if (area.existAnyInvalidVegUnits()) {
      String msg =
        "Invalid data was found in the units after loading\n" +
        "In addition any simulation data that may have existed has\n" +
        "been erased from memory\n" +
        "The area can be made valid again by either running the Unit Editor\n" +
        "found under the Utilities menu of the main application window, or\n" +
        "by loading a system knowledge file that contains the missing data\n";

      JOptionPane.showMessageDialog(this,msg,"Invalid units found",
                                    JOptionPane.INFORMATION_MESSAGE);
      markAreaInvalid();
    }
    else {
      markAreaValid();
    }
  }

  void menuSysKnowOpen_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    SystemKnowledgeLoadSave dlg =
        new SystemKnowledgeLoadSave(this,"Load System Knowledge File",true,false);

    setDialogLocation(dlg);

    File sysKnowFile = dlg.getAndSetInputFile();
    if (sysKnowFile != null) {
      dlg.setVisible(true);
    }
    else {
      return;
    }

    // Update the units and check for invalid ones.
    if (area != null && dlg.isDialogCanceled() == false) {
      area.updatePathwayData();
      doInvalidAreaCheck();

      boolean changed = area.updateFmzData();
      if (changed) {
        String msg = "Fmz's in the currently loaded area that\n" +
                     "referred to fmz's not currently loaded\n" +
                     "were changed to the default fmz.\n\n" +
                     "If this is not desired load the correct\n" +
                     "fmz data file for the area and then\n" +
                     "reload the current area.";
        JOptionPane.showMessageDialog(this,msg,"Warning",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  void menuSysKnowSave_actionPerformed(ActionEvent e) {
    SystemKnowledgeLoadSave dlg =
        new SystemKnowledgeLoadSave(this,"Save System Knowledge",true,true);

    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuSysKnowRestoreDefaults_actionPerformed(ActionEvent e) {
    Area                  area = Simpplle.getCurrentArea();

    if (area != null && Simpplle.getCurrentSimulation() != null) {
      if (deleteSimulationCheck()) { return; }
    }

    String msg =
      "This option will restore system knowledge to the defaults.\n" +
      "This has essentially the same effect as reloading the current zone.\n" +
      "\nDo you wish to continue?";

    int choice = JOptionPane.showConfirmDialog(this,msg,
                                           "Restore System Knowledge Defaults",
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.NO_OPTION) { return; }

    try {
      SystemKnowledge.loadAllDefaults();
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),
                                    "Could not Restore defaults",
                                    JOptionPane.ERROR_MESSAGE);
    }

    // Update the units and check for invalid ones.
    if (area != null) {
      area.updatePathwayData();
      doInvalidAreaCheck();

      boolean changed = area.updateFmzData();
      if (changed) {
        msg = "Fmz's in the currently loaded area that\n" +
              "referred to fmz's not currently loaded\n" +
              "were changed to the default fmz.\n\n" +
              "If this is not desired load the correct\n" +
              "fmz data file for the area and then\n" +
              "reload the current area.";
        JOptionPane.showMessageDialog(this,msg,"Warning",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }

  }

  void menuImportCreate_actionPerformed(ActionEvent e) {
    String                msg;
    int                   choice;
    File                  inputFile = null;
    MyFileFilter          extFilter;

    if (Simpplle.getCurrentArea() != null) {
      msg = "This will remove the Current Area\n\n" +
            "This will create a new area using any files\n" +
            "found in the current directory with the same prefix\n" +
            "(e.g. myarea.nbr, myarea.land, etc...)\n\n" +
            "Do you wish to continue?";
      choice = JOptionPane.showConfirmDialog(this,msg,"Remove Current Area",
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);

      if (choice == JOptionPane.NO_OPTION) {
        return;
      }
    }

    // ** This case is special because we will be using two
    // ** different file extensions.
    // ** Utility.getOpenFile does not handle this
    JFileChooser chooser = new JFileChooser(JSimpplle.getWorkingDir());

    MyFileFilter mainFilter = new MyFileFilter("spatialrelate",
                                                "SIMPPLLE Spatial Relations File (*.spatialrelate)");
    chooser.addChoosableFileFilter(mainFilter);
    chooser.setAcceptAllFileFilterUsed(false);



    extFilter = new MyFileFilter("newarea",
                                 "SIMPPLLE New Area Files (*.newarea)");
    chooser.addChoosableFileFilter(extFilter);
    chooser.setFileFilter(mainFilter);
    chooser.setDialogTitle("Choose Area Import File");
    if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      inputFile = chooser.getSelectedFile();
    }
    if (inputFile == null) {
      refresh();
      return;
    }

    createArea(inputFile);
  }

  private void createArea(File inputFile) {
    String                msg;
    MyFileFilter          extFilter;
    simpplle.comcode.Area area;

   // Create the new area.
    comcode.removeCurrentArea();
    Simpplle.resetSimulation();
    disableAreaControls();
    areaValueLabel.setText("");
    setStatusMessage("Creating New Area ...");


    boolean attributeSuccess = false;
    try {
      attributeSuccess = comcode.importArea(inputFile);
    }
    catch (SimpplleError ex) {
    }

    setNormalState();

    area = Simpplle.getCurrentArea();
    if (area == null) {
      msg = "Creation of a new Area failed.\n" +
          "A log file has been written to the same directory with more information";
      JOptionPane.showMessageDialog(this,msg,"Area Import Failed",
                                    JOptionPane.ERROR_MESSAGE);
      refresh();
      return;
    }

    if (!attributeSuccess) {
      msg = "No Attribute data was found in the input file.\n" +
            "In the following file dialog please specify\n" +
            "the file containing the data.\n\n" +
            "If there is no file yet, data can be added later\n" +
            "via the import menu.  Just press cancel in the next dialog.";

      JOptionPane.showMessageDialog(this,msg,"No Attribute Data Found",
                                    JOptionPane.INFORMATION_MESSAGE);

      extFilter = new MyFileFilter("attributes",
                                   "SIMPPLLE Atrribute Files (*.attributes)");

      inputFile = Utility.getOpenFile(this,"Attributes Import File?",extFilter);
      if (inputFile != null) {
        setWaitState("Adding attribute Data ...");
        try {
          comcode.importAttributeData(inputFile);
        }
        catch (SimpplleError err) {
          JOptionPane.showMessageDialog(this,err.getError(),"Import Failed",
                                        JOptionPane.ERROR_MESSAGE);
        }
        setNormalState();
      }
    }
    area.setMultipleLifeformStatus();

    if (area.existAnyInvalidVegUnits()) {
      area.fixEmptyDataUnits();
      msg = "Creation of the Area was only partially successful.\n" +
            "One or more of the units had invalid attribute data.\n" +
            "It was most likely caused by one of the following:\n" +
            "  -- no attributes file loaded\n" +
            "  -- an invalid habitat type group\n" +
            "  -- an invalid species, size class, or density\n" +
            "  -- other invalid attribute data\n\n" +
            "Please look at the log file for details:\n\n" +
            "The import menu has three options that will help " +
            "to fix problems.\n" +
            "  1. Fix Incorrect States\n" +
            "       Attempt to fix incorrect states.\n" +
            "  2. Edit Units\n" +
            "       Allows editing of unit attribute data.\n\n" +
            "  3. Import Attribute Data\n" +
            "       Add data to existing units.\n" +
            "Please note save of the area will not be allowed\n" +
            "Until \"ALL\" Errors have been fixed.\n";
      JOptionPane.showMessageDialog(this,msg,"Area Import Partially Succeeded",
                                    JOptionPane.WARNING_MESSAGE);
      area.setName("No Name (to change use Utility-->Edit Area)");
      String str = Simpplle.getCurrentArea().getName();
      areaValueLabel.setText(str);
      markAreaInvalid();
    }
    // Import was successful.
    else {
      msg = "Creation of the Area was successful.\n" +
           "Please give the area a name using the \"Change Area Name\"" +
           " function under the utility menu.\n\n" +
           "*** Do not to forget to save the area (File Menu) ***";
      JOptionPane.showMessageDialog(this,msg,"Area Import Succeeded",
                                    JOptionPane.INFORMATION_MESSAGE);
      area.setName("No Name (to change use Utility-->Change Area Name)");
      String str = Simpplle.getCurrentArea().getName();
      areaValueLabel.setText(str);
      updateSpreadModels(area.getHasKeaneAttributes());
      markAreaValid();
      disableSimulationControls();
    }
    refresh();
  }
/**
 * Marks an area invalid and allows users to import fix states, edit units, or
 * print invalid report.
 */
  public void markAreaInvalid() {
    areaInvalidLabel.setText("(invalid)");
    disableAreaControls();
    disableSimulationControls();
    menuImportFixStates.setEnabled(true);
    menuImportEditUnits.setEnabled(true);
    menuImportInvalidReport.setEnabled(true);
    menuUtilityUnitEditor.setEnabled(true);
  }

/**
 * Marks the area as valid, then enables area controls.
 * @see #markAreaInvalid()
 */
  public void markAreaValid() {
    areaInvalidLabel.setText("");
    enableAreaControls();
    menuImportFixStates.setEnabled(false);
    menuImportEditUnits.setEnabled(false);
    menuImportInvalidReport.setEnabled(false);
  }

  void menuImportAttributeData_actionPerformed(ActionEvent e) {
    String                msg;
    File                  inputFile;
    MyFileFilter          extFilter;
    Area                  area = Simpplle.getCurrentArea();

    extFilter = new MyFileFilter("attributes",
                                 "SIMPPLLE Atrribute Files (*.attributes)");

    inputFile = Utility.getOpenFile(this,"Attributes Import File?",extFilter);
    if (inputFile == null) {
      refresh();
      return;
    }

    setWaitState("Adding attribute Data ...");
    try {
        comcode.importAttributeData(inputFile);
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Import Failed",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setNormalState();

    if (area.existAnyInvalidVegUnits()) {
      msg = "Adding Attributes was only partially successful.\n" +
                       "One or more of the units had invalid attribute data.\n" +
                       "It was most likely caused by one of the following:\n" +
                       "  -- an invalid habitat type group\n" +
                       "  -- an invalid species, size class, or density\n" +
                       "  -- other invalid attribute data\n\n" +
                       "Please look at the following log file for details:\n\n" +
                       "The import menu has two options that will help " +
                       "to fix problems.\n" +
                       "  1. Fix Incorrect States\n" +
                       "       Attempt to fix incorrect states.\n" +
                       "  2. Edit Units\n" +
                       "       Allows editing of unit attribute data.\n\n" +
                       "Please note save of the area will not be allowed\n" +
                       "Until \"ALL\" Errors have been fixed.\n";
      JOptionPane.showMessageDialog(this,msg,"Partial Success",
                                    JOptionPane.WARNING_MESSAGE);
      menuImportFixStates.setEnabled(true);
      menuImportEditUnits.setEnabled(true);
      menuImportInvalidReport.setEnabled(true);
    }
    // Import was successful.
    else {
      msg =
         "Adding Attritues was successful.\n" +
         "Please give the area a name using the \"Change Area Name\"" +
         " function under the utility menu.\n\n" +
         "*** Do not to forget to save the area (File Menu) ***";
      JOptionPane.showMessageDialog(this,msg,"Success",
                                    JOptionPane.INFORMATION_MESSAGE);
      markAreaValid();
      areaInvalidLabel.setText("");
      enableAreaControls();
      disableSimulationControls();

      menuImportFixStates.setEnabled(false);
      menuImportEditUnits.setEnabled(false);
      menuImportInvalidReport.setEnabled(false);
    }
  }

  void menuImportFixStates_actionPerformed(ActionEvent e) {
    RegionalZone zone = Simpplle.getCurrentZone();
    Area         area = Simpplle.getCurrentArea();
    int          zoneId = zone.getId();

    if (zoneId != ValidZones.EASTSIDE_REGION_ONE) {
      JOptionPane.showMessageDialog(this,"Not available for the zone yet.",
                                    "Not Yet Implemented",
                                    JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    else {
      setWaitState("Attempting to fix incorrect states ...");
      Simpplle.getCurrentArea().fixIncorrectStates();
      setNormalState();
    }
    updateAreaValidity();

    if (area.existAnyInvalidVegUnits()) {
      String msg =
        "Fix of incorrect states was only partially successful.\n" +
        "Remaining errors can be fixed by using the \"Edit Units\"\n" +
        "menu item under the Import Menu, or by using the \"Unit Editor\"\n" +
        "menu item under the Utilities Menu";
      JOptionPane.showMessageDialog(this,msg,"Fixing of States Unsuccessful",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    else {
      String msg =
        "Fixing of incorrect states was completely successful.\n" +
        "Remember to name the new area by using \"Change Area Name\"\n" +
        "under the Utility Menu.  Then save the area using \"Save Area\"\n" +
        "under the File Menu.";
      JOptionPane.showMessageDialog(this,msg,"Fixing of States Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
      markAreaValid();
      areaInvalidLabel.setText("");
      enableAreaControls();
      disableSimulationControls();

      menuImportFixStates.setEnabled(false);
      menuImportEditUnits.setEnabled(false);
      menuImportInvalidReport.setEnabled(false);
    }
  }

  void menuImportEditUnits_actionPerformed(ActionEvent e) {
    EvuEditor dlg = new EvuEditor(this,"",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuImportInvalidReport_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Invalid Units Report File");
    if (outfile != null) {
      setWaitState("Generating Report ...");
      try {
        Simpplle.getCurrentArea().printInvalidUnits(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),
                                      "Unable to write file",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuInterpretProbCalc_actionPerformed(ActionEvent e) {
    String title = "Attribute Probability Calculator";
    AttributeProbabilityCalculator dlg =
         new AttributeProbabilityCalculator(this, title, true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuInterpretRestoration_actionPerformed(ActionEvent e) {
    RestorationReport dlg = new RestorationReport(this,"Ecosystem Restoration",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }



  void menuInterpretWildlife_actionPerformed(ActionEvent e) {
    String title = "Wildlife Habitat Interpretations";
    WildlifeHabitat dlg = new WildlifeHabitat(this,title,false);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }


  void menuUtilitySimReady_actionPerformed(ActionEvent e) {
    String msg =
      "This will take the current state of units at the end of the simulation,\n" +
      "and make this state the new vegetative state for the unit.\n" +
      "In addition the simulation will be reset, thus removing all simulation data.\n\n" +
      "Do you wish to continue?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Make Area Simulation Ready",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      disableSimulationControls();
      Simpplle.makeAreaSimulationReady();
      enableAreaControls();
    }
  }

  void menuUtilityUnitEditor_actionPerformed(ActionEvent e) {
    EvuEditor dlg = new EvuEditor(this,"",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuUtilityAreaName_actionPerformed(ActionEvent e) {
    String msg   = "New Area Name";
    String title = "New Area Name";
    String name  = JOptionPane.showInputDialog(this,msg,title,
                                               JOptionPane.PLAIN_MESSAGE);
    if (name != null) {
      Simpplle.getCurrentArea().setName(name);
      String str = Simpplle.getCurrentArea().getName();
      areaValueLabel.setText(str);
    }
    refresh();
  }

  void menuUtilitiesConsole_actionPerformed(ActionEvent e) {
    ConsoleMessages dlg = new ConsoleMessages(this,"Console Messages",false);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }
  public void menuUtilityMemoryUse_actionPerformed(ActionEvent e) {
    MemoryDisplay frame = new MemoryDisplay();
    frame.setVisible(true);
  }

  void menuUtilityGISFiles_actionPerformed(ActionEvent e) {
    CopyGis dlg = new CopyGis(this,"Copy GIS Files",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuUtilityDatabaseTest_actionPerformed(ActionEvent e) {
//    simpplle.comcode.DatabaseCreator.doIt();
    try {
      File workDir = simpplle.JSimpplle.getWorkingDir();
      simpplle.comcode.DatabaseCreator.initHibernate(true,new File(workDir,"test"));
      Simpplle.getCurrentArea().writeSimulationDatabase();
      simpplle.comcode.DatabaseCreator.closeHibernate();
    }
    catch (SimpplleError ex) {
      ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
  }

  public void menuUtilityDatabaseManager_actionPerformed(ActionEvent e) {
    MyFileFilter  extFilter = new MyFileFilter("data",
                                               "Simpplle Simulation Database Files (*.data)");
    File outfile = Utility.getOpenFile(this,"User Defined Area File?",extFilter);
    outfile = simpplle.comcode.Utility.stripExtension(outfile);

    String url = "jdbc:hsqldb:file:" + outfile;
    DatabaseManagerSwing.main(new String[] {"-url", url});
  }



  void menuSysKnowUseRegenPulse_actionPerformed(ActionEvent e) {
    FireEvent.setUseRegenPulse(menuSysKnowUseRegenPulse.isSelected());
  }

  void menuSysKnowConiferEncroach_actionPerformed(ActionEvent e) {
    ConiferEncroachmentDialog dlg = new ConiferEncroachmentDialog(this,"Conifer Encroachment Logic",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }


  void menuMagisProcessTreatmentFiles_actionPerformed(ActionEvent e) {
    File outfile;
    outfile = Utility.getSaveFile(this,"Magis Process/Treatment Files Prefix?");

    if (outfile != null) {
      setWaitState("Generating Magis Process and Treatment Files ...");
      try {
        Simpplle.getCurrentArea().magisProcessAndTreatmentFiles(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),"Problem writing files",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuMagisAllVegStates_actionPerformed(ActionEvent e) {
    String       msg = "File to Output Vegetative States to?";
    MyFileFilter extFilter = new MyFileFilter("txt",
                                               "Text Files (*.txt)");

    File outfile = Utility.getSaveFile(this,msg,extFilter);
    if (outfile != null) {
      try {
        setWaitState("Writing All Vegetative Types...");
        HabitatTypeGroup.magisAllVegTypes(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),
                                      "Problems generating file",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }

  void menuUtilityDeleteUnits_actionPerformed(ActionEvent e) {
    RemoveUnits dlg = new RemoveUnits(this,"Delete Units",true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuReportsFireSuppCostAll_actionPerformed(ActionEvent e) {
//    File         simFile, simPrefix, outfile;
//    String       str;
//    int          index;
//    PrintWriter  fout;
//    Simulation   simulation = new Simulation();
//    MyFileFilter  extFilter = new MyFileFilter("area",
//                                               "Simpplle Area Files (*.area)");
//
//    simFile = Utility.getOpenFile(this,"Select (Any) Simulation File?",extFilter);
//    if (simFile != null) {
//      try {
//        // Find the Simulation Prefix
//        str   = simFile.toString();
//        index = str.indexOf(".area");
//        if (Character.isDigit(str.charAt(index-1)) == false) {
//          throw new SimpplleError(simFile.toString() + " is not a valid saved simulation.");
//        }
//        while (str.charAt(index) != '-') { index--; }
//        simPrefix = new File(str.substring(0,index));
//
//        // Make output file name
//        outfile = simpplle.comcode.Utility.makeSuffixedPathname(simPrefix,"-ls-cost","txt");
//
//        // Read the Simulation Data
//        simulation.readAllSimulationFiles(simPrefix);
//
//        // Write out the Report
//        fout = new PrintWriter(new FileWriter(outfile));
//
//        Simpplle.setStatusMessage("Writing Fire Suppression Cost Report to: " + outfile.toString());
//        simulation.getMultipleRunSummary().asciiFireSuppressionCostSummaryReport(fout,simulation);
//
//        fout.flush();
//        fout.close();
//
//        Simpplle.clearStatusMessage();
//      }
//      catch (Exception err) {
//        JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
//                                      JOptionPane.ERROR_MESSAGE);
//      }
//    }
  }

  void menuSysKnowWSBWLogic_actionPerformed(ActionEvent e) {
    if (Simpplle.getCurrentZone() instanceof ColoradoFrontRange ||
        Simpplle.getCurrentZone() instanceof ColoradoPlateau) {
      String title = "Western Spruce Budworm";
      String text =
          "Needs to be Locked in through the 'Lock in Processes' Screen";

      JTextAreaDialog dlg = new JTextAreaDialog(this, title, true, text);
      setDialogLocation(dlg);
      dlg.setVisible(true);
      return;
    }

    if (JSimpplle.developerMode() == false) {
      JOptionPane.showMessageDialog(this, "Under Construction",
                                    "Under Construction",
                                    JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (Simpplle.getCurrentZone().getUserProbProcesses() == null) {
      JOptionPane.showMessageDialog(this, "Nothing Defined for this zone",
                                    "No Processes Found",
                                    JOptionPane.INFORMATION_MESSAGE);
      return;
    }

//    String logicText = WsbwLogicDiagrams.getLogic();
//
//    JTextAreaDialog dlg = new JTextAreaDialog(this,title,true,logicText);
//    setDialogLocation(dlg);
//    dlg.setVisible(true);

    InsectDiseaseLogic dlg = new InsectDiseaseLogic(this);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  private boolean arePathwaysLoaded() {
    Vector v = HabitatTypeGroup.getAllLoadedTypes();
    if (v == null || v.size() == 0) {
      String msg = "Please load some pathways first";
      JOptionPane.showMessageDialog(this, msg, "No Pathways Loaded",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  void menuSysKnowRegen_actionPerformed(ActionEvent e) {
    RegenerationLogic.setDefaultEcoGroup(RegenerationLogic.FIRE);
    RegenerationLogic.setDefaultEcoGroup(RegenerationLogic.SUCCESSION);
    if (arePathwaysLoaded() == false) { return; }
    if (RegenerationLogic.isDataPresent(RegenerationLogic.FIRE) == false) {
      RegenerationLogic.makeBlankLogic(RegenerationLogic.FIRE);
    }
    if (RegenerationLogic.isDataPresent(RegenerationLogic.SUCCESSION) == false) {
      RegenerationLogic.makeBlankLogic(RegenerationLogic.SUCCESSION);
    }

    RegenerationLogicDialog dlg = new RegenerationLogicDialog(this,"Regeneration Logic",false);

//    setDialogLocation(dlg);
    dlg.setVisible(true);
  }



  void menuResultLandformSum_actionPerformed(ActionEvent e) {

  }

  void menuResultLandformUnit_actionPerformed(ActionEvent e) {
    UnitAnalysis dlg = new UnitAnalysis(this,"Unit Analysis",false);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

 

  void menuSysKnowVegTreatDesired_actionPerformed(ActionEvent e) {

  }

  void menuResultAquaticUnit_actionPerformed(ActionEvent e) {
    if (EauAnalysis.isOpen()) { return; }

    EauAnalysis dlg = new EauAnalysis(this,"Aquatic Unit Analysis",false);
    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  void menuResultAquaticSum_actionPerformed(ActionEvent e) {

  }

  void menuHelpUserGuide_actionPerformed(ActionEvent e) {
    String msg =
      "The User's Guide is available on the OpenSIMPPLLE site.";

    JOptionPane.showMessageDialog(this,msg,"",JOptionPane.INFORMATION_MESSAGE);
  }

  private void updateJavaHeapSize() {
    File batFile = new File(JSimpplle.getInstallDirectory(),"SIMPPLLE.bat");
    if (batFile.exists()) {
      updateJavaHeapSizeBAT();
      return;
    }

    File iniFile = new File(JSimpplle.getInstallDirectory(),"SIMPPLLE.ini");
    if (iniFile.exists()) {
      updateJavaHeapSizeIni();
      return;
    }
    
  }
    
  @SuppressWarnings("unused")
  private void updateJavaHeapSizeIni() {
    File   iniFile, newIniFile;
    int    newHeapSize;
    String msg;

    iniFile = new File(JSimpplle.getInstallDirectory(),"SIMPPLLE.ini");
    try {
      if (iniFile.exists() == false) {
        throw new SimpplleError("Could not find SIMPPLLE.ini file to modify.\n");
      }
      
//      String memStr = System.getProperty("simpplle.javamem");
      long maxMem  = Runtime.getRuntime().maxMemory();

      maxMem  = (maxMem / 1024) / 1024;

//      int mem = Integer.parseInt(memStr);

      msg = "Enter Java Max Heap Size in MB";
      newHeapSize = AskNumber.getInput("Java Max Heap Size (MB)",msg,(int)maxMem);
      if (newHeapSize == -1) { return; }

      newIniFile = new File(JSimpplle.getInstallDirectory(),"SIMPPLLE.ini.tmp");

      BufferedReader fin = new BufferedReader(new FileReader(iniFile));
      PrintWriter    fout = new PrintWriter(new FileWriter(newIniFile));

      String line = fin.readLine();
      while (line != null) {
        if (line.startsWith("Virtual Machine")) {
          int index = line.indexOf("-Xm");

          fout.print(line.substring(0, index));
          fout.print("-Xms" + newHeapSize + "M");

          int beginIndex = line.indexOf(" ", index);
          index = line.lastIndexOf("-Xm");

          fout.print(line.substring(beginIndex,index));
          fout.print("-Xmx" + newHeapSize + "M");

          beginIndex = line.indexOf(" ", index);

          fout.println(line.substring(beginIndex));
        }
        else { fout.println(line); }
        line = fin.readLine();
      }
      fin.close();
      fout.flush();
      fout.close();

      if (iniFile.delete()) {
        newIniFile.renameTo(iniFile);
      }
    }
    catch (NumberFormatException err) {
      JOptionPane.showMessageDialog(this,"Invalid Java heap size","",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    catch (Exception err) {
      msg = "Unable to edit " + iniFile.toString() + "\n" + err.getMessage();
      JOptionPane.showMessageDialog(this,msg,"",JOptionPane.ERROR_MESSAGE);
      return;
    }
    msg = "Change successful. Restart SIMPPLLE for changes to take effect.";
    JOptionPane.showMessageDialog(this,msg,"",JOptionPane.ERROR_MESSAGE);
  }

  private void updateJavaHeapSizeBAT() {
    File   batFile, newBatFile;
    int    newHeapSize;
    String msg;

    batFile = new File(JSimpplle.getInstallDirectory(),"SIMPPLLE.bat");
    try {
      if (batFile.exists() == false) {
        throw new SimpplleError("Could not find SIMPPLLE.bat file to modify.\n");
      }
      
      String memStr = System.getProperty("simpplle.javamem");
      int mem = Integer.parseInt(memStr);
      
      msg = "Enter Java Max Heap Size in MB";
      newHeapSize = AskNumber.getInput("Java Max Heap Size (MB)",msg,mem);
      if (newHeapSize == -1) { return; }

      newBatFile = new File(JSimpplle.getInstallDirectory(),"SIMPPLLE.bat.tmp");

      BufferedReader fin = new BufferedReader(new FileReader(batFile));
      PrintWriter    fout = new PrintWriter(new FileWriter(newBatFile));

      String line = fin.readLine();
      while (line != null) {
        if (line.startsWith("SET JAVAMEM")) {
          fout.println("SET JAVAMEM=" + newHeapSize);
        }
        else { fout.println(line); }
        line = fin.readLine();
      }
      fin.close();
      fout.flush();
      fout.close();

      if (batFile.delete()) {
        newBatFile.renameTo(batFile);
      }
    }
    catch (NumberFormatException err) {
      JOptionPane.showMessageDialog(this,"Invalid Java heap size","",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    catch (Exception err) {
      msg = "Unable to edit " + batFile.toString() + "\n" + err.getMessage();
      JOptionPane.showMessageDialog(this,msg,"",JOptionPane.ERROR_MESSAGE);
      return;
    }
    msg = "Change successful. Restart SIMPPLLE for changes to take effect.";
    JOptionPane.showMessageDialog(this,msg,"",JOptionPane.ERROR_MESSAGE);
  }
  void MenuUtilityJavaHeap_actionPerformed(ActionEvent e) {
    updateJavaHeapSize();
  }

  void menuSysKnowFireSuppProdRate_actionPerformed(ActionEvent e) {
    String title = "Fire Suppression Line Production Rate Logic";
    FireSuppProductionRateLogicDlg dlg = new FireSuppProductionRateLogicDlg(this,title,true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuSysKnowFireSuppSpreadRate_actionPerformed(ActionEvent e) {
    String title = "Fire Suppression Spread Rate Logic";
    FireSuppSpreadRateLogicDlg dlg = new FireSuppSpreadRateLogicDlg(this,title,true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuSysKnowFireSuppResponseTime_actionPerformed(ActionEvent e) {
    String title = "Fire Suppression Response Time";
    FireSuppResponseTimeLogicBuilder dlg = new FireSuppResponseTimeLogicBuilder(this, title, true);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }



  void menuSysKnowSpeciesKnowledgeEditor_actionPerformed(ActionEvent e) {
    String title = "Species Knowledge Editor";
    SpeciesKnowledgeEditor dlg = new SpeciesKnowledgeEditor(this,title,false);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuUtilityZoneEdit_actionPerformed(ActionEvent e) {
    boolean enabled = menuUtilityZoneEdit.isSelected();
    JSimpplle.setZoneEdit(enabled);
    menuUtilityZoneEdit.setVisible(enabled);
  }

  void menuImportFSLandscape_actionPerformed(ActionEvent e) {
    doFSLandscape();
  }

  private void doFSLandscape() {
    try {
      String osName = System.getProperty("os.name");
      String[] cmd = new String[7];
      File dir;
      if (JSimpplle.developerMode()) {
        dir = new File("C:\\MyDocuments\\MyProjects\\SIMPPLLE-installAnywhere\\gis\\fslandscape");
      }
      else {
        dir = RegionalZone.getSystemFSLandscapeDiretory();
      }
      File batchFile = new File(dir,"fslandscape.bat");

      if (osName.equalsIgnoreCase("Windows NT")   ||
          osName.equalsIgnoreCase("Windows 2000") ||
          osName.equalsIgnoreCase("Windows XP")) {
        cmd[0] = "cmd.exe";
        cmd[1] = "/c";
        cmd[2] = "start";
        cmd[3] = "/i/d";
        cmd[4] = JSimpplle.getWorkingDir().getAbsolutePath();
        cmd[5] = batchFile.getAbsolutePath();
        cmd[6] = dir.getAbsolutePath();
      }
      else { return; }

      Runtime rt = Runtime.getRuntime();

      StringBuffer strBuf = new StringBuffer();
      strBuf.append("Running");
      for (int i=0; i<cmd.length; i++) { strBuf.append(" " + cmd[i]); }

      setWaitState(strBuf.toString());
      java.lang.Process proc = rt.exec(cmd);
      // any error message?
      StreamGobbler errorGobbler = new
                                   StreamGobbler(proc.getErrorStream(), "ERROR");

      // any output?
      StreamGobbler outputGobbler = new
                                    StreamGobbler(proc.getInputStream(),
                                                  "OUTPUT");

      // kick them off
      errorGobbler.start();
      outputGobbler.start();

      // any error???
      @SuppressWarnings("unused")
      int exitVal = proc.waitFor();
      setNormalState();
//      System.out.println("ExitValue: " + exitVal);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }

  void menuSysKnowImportProcesses_actionPerformed(ActionEvent e) {
    File filename = Utility.getOpenFile(this,"Import Process Definition File");
    if (filename == null) { return; }

    try {
      simpplle.comcode.Process.importLegalFile(filename);
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this,ex.getMessage(),"",JOptionPane.ERROR_MESSAGE);
    }
  }

  void menuSysKnowShowLegalProcesses_actionPerformed(ActionEvent e) {
    ProcessType[] processes = simpplle.comcode.Process.getLegalProcesses();
    StringBuffer strBuf = new StringBuffer();

    for (int i=0; i<processes.length; i++) {
      strBuf.append(processes[i].toString());
      strBuf.append((processes[i].isSpreading() ? " -- spreading\n" : " -- not spreading\n"));
    }

    JTextAreaDialog dlg = new JTextAreaDialog(this,"Legal Processes",false,strBuf.toString());
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuSysKnowImportZoneTreatments_actionPerformed(ActionEvent e) {
    File filename = Utility.getOpenFile(this,"Import Treatment Definition File");
    if (filename == null) { return; }

    try {
      simpplle.comcode.Treatment.importLegalFile(filename);
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this,ex.getMessage(),"",JOptionPane.ERROR_MESSAGE);
    }
  }

  void menuSysKnowShowZoneTreatments_actionPerformed(ActionEvent e) {
    TreatmentType[] treatments = simpplle.comcode.Treatment.getLegalTreatments();
    StringBuffer strBuf = new StringBuffer();

    for (int i=0; i<treatments.length; i++) {
      strBuf.append(treatments[i].toString());
      strBuf.append("\n");
    }

    JTextAreaDialog dlg = new JTextAreaDialog(this,"Legal Treatments",false,strBuf.toString());
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuFileSaveZone_actionPerformed(ActionEvent e) {
    MyFileFilter  extFilter = new MyFileFilter("simpplle_zone",
                                               "Simpplle Zone Files (*.simpplle_zone)");
    File zoneName = Utility.getSaveFile(this,"Zone Name",extFilter);
    if (zoneName == null) { return; }

    try {
      SystemKnowledge.saveZone(zoneName);
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Problems saving",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void buildSimpplleTypeFiles_actionPerformed(ActionEvent e) {
    try {
      File prefix = Utility.getSaveFile(this, "Simpplle Types Prefix");
      if (prefix == null) { return; }
      HabitatTypeGroup.makeAllSimpplleTypeFiles(prefix);
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Problems writing file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void buildSimpplleTypesSource_actionPerformed(ActionEvent e) {
    try {
      File filename = Utility.getOpenFile(this, "Simpplle Types File");
      if (filename == null) { return; }
      HabitatTypeGroup.makeAllSimpplleTypesSourceFiles(filename);
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Problems writing file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void menuSysKnowWindthrow_actionPerformed(ActionEvent e) {
    String title = "Windthrow";
    String text = "Needs to be Locked in through the 'Lock in Processes' Screen";

    JTextAreaDialog dlg = new JTextAreaDialog(this,title,true,text);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuSysKnowWildlifeBrowsing_actionPerformed(ActionEvent e) {
    String title = "Wildlife Browsing";
    String text = "Needs to be Locked in through the 'Lock in Processes' Screen";

    JTextAreaDialog dlg = new JTextAreaDialog(this,title,true,text);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  void menuSysKnowTussockMoth_actionPerformed(ActionEvent e) {
    String title = "Tussock Moth";
    String text = "Needs to be Locked in through the 'Lock in Processes' Screen";

    JTextAreaDialog dlg = new JTextAreaDialog(this,title,true,text);
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  public void menuUtilityTestNewDialog_actionPerformed(ActionEvent e) {

    Simpplle.getCurrentArea().validateLifeformStorageMatch();
  
  }

  public void menuSysKnowDisableWsbw_actionPerformed(ActionEvent e) {
    //Quack - Disable WSBW Logic
	//Wsbw.setEnabled(!menuSysKnowDisableWsbw.isSelected());
	  Wsbw.setEnabled(false);
	 System.out.println(Wsbw.isEnabled());
  }

  public void menuSysKnowProcessProbLogic_actionPerformed(ActionEvent e) {
//    InsectDiseaseLogic dlg = new InsectDiseaseLogic(this);
    ProcessProbabilityLogicDialog dlg =
      new ProcessProbabilityLogicDialog(this,"Process Probability Logic",true);
    dlg.setVisible(true);
  }

  public void menuBisonGrazingLogic_actionPerformed(ActionEvent e) {
    String title = "Bison Grazing Logic";
    BisonGrazingLogicEditor dlg = new BisonGrazingLogicEditor(this,title,true);

    dlg.initialize();
    setDialogLocation(dlg);
    dlg.setVisible(true);
  }

  public void menuSysKnowDoCompetition_actionPerformed(ActionEvent e) {
    DoCompetitionDlg dlg = new DoCompetitionDlg(this,"Lifeform Competition",true);
    dlg.setVisible(true);
  }

  public void menuSysKnowGapProcessLogic_actionPerformed(ActionEvent e) {
    GapProcessLogicDlg dlg = new GapProcessLogicDlg(this,"Gap Process Logic",true);
    dlg.setVisible(true);

  }

  public void menuSysKnowProducingSeedLogic_actionPerformed(ActionEvent e) {
    ProducingSeedLogicDlg dlg = new ProducingSeedLogicDlg(this,"Producing Seed Logic",true);
    dlg.setVisible(true);
  }

  public void menuSysKnowVegUnitFireTypeLogic_actionPerformed(ActionEvent e) {
    VegUnitFireTypeLogicDlg dlg = new VegUnitFireTypeLogicDlg(this,"Veg Unit Fire Type Logic",true);
    dlg.setVisible(true);
  }

  public void menuSysKnowInvasiveLogicR1_actionPerformed(ActionEvent e) {
  }

  public void menuSysKnowInvasiveLogicMSU_actionPerformed(ActionEvent e) {
    InvasiveSpeciesMSULogicDialog dlg =
      new InvasiveSpeciesMSULogicDialog(this,"Invasive Species Logic (Montana State University)",true);
    dlg.setVisible(true);
  }

  public void menuSysKnowInvasiveLogicMesaVerdeNP_actionPerformed(ActionEvent e) {
    InvasiveSpeciesLogicDialog dlg =
      new InvasiveSpeciesLogicDialog(this,"Invasive Species Logic (Mesa Verde NP)",true);
    dlg.setVisible(true);
  }

  public void menuUtilityMakeAreaMultipleLife_actionPerformed(ActionEvent e) {
    Simpplle.getCurrentArea().makeMultipleLifeforms();
  }

  public void menuUtilityCombineLSFiles_actionPerformed(ActionEvent e) {
    String[]     suffixes = new String[] {"-ls"};
    MyFileFilter extFilter = new MyFileFilter(suffixes,"txt",
                                               "Landcape Summary Files (*-ls.txt)");

    File[] files = Utility.getOpenFiles(this,"LS Files to Combine",extFilter);
    if (files == null) {
      return;
    }

    try {
      String outfile = LandscapeSummaryFileCombiner.combineLandscapeSummaryFiles(files);

      JOptionPane.showMessageDialog(this,outfile,"File Successfully Written",
                                    JOptionPane.ERROR_MESSAGE);

    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error",
                                    JOptionPane.ERROR_MESSAGE);
    }
    finally {
      setNormalState();
    }
  }
  private class MenuUtilityExportPathwaysActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      menuUtilityExportPathways_actionPerformed(e);
    }
  }
  private class MenuUtilityElevRelPosActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      menuUtilityElevRelPos_actionPerformed(e);
    }
  }
  private class MenuUtilitySwapRowColActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      menuUtilitySwapRowCol_actionPerformed(e);
    }
  }
  private class MenuSysKnowFireSuppEventActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      menuSysKnowFireSuppEvent_actionPerformed(e);
    }
  }
  protected void menuUtilityExportPathways_actionPerformed(ActionEvent e) {
    File outfile;

    outfile = Utility.getSaveFile(this,"Export Pathways to Text File");
    if (outfile != null) {
      setWaitState("Exporting Pathways ...");
      try {
        HabitatTypeGroup.exportGISTable(outfile);
      }
      catch (SimpplleError ex) {
        JOptionPane.showMessageDialog(this,ex.getMessage(),"",
                                      JOptionPane.ERROR_MESSAGE);
      }
      setNormalState();
    }
  }
  protected void menuUtilityElevRelPos_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();
    ElevationRelativePosition dlg = new ElevationRelativePosition(simpplle.JSimpplle.getSimpplleMain(),"Elevation Relative Position",true,area);
    
    dlg.setVisible(true);
    
    int value = dlg.getValue();
    
    area.setElevationRelativePosition(value);
  }
  protected void menuUtilitySwapRowCol_actionPerformed(ActionEvent e) {
    String msg =
      "This will swap the Row and Column values in the units.\n\n" +
      "Do you wish to continue?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Swap Row/Column",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      Simpplle.getCurrentArea().swapRowColumn();
    }
  }
  protected void menuSysKnowFireSuppEvent_actionPerformed(ActionEvent e) {
    FireSuppEventLogicDlg dlg = new FireSuppEventLogicDlg(this,"Fire Suppression Event Probability",true);


    setDialogLocation(dlg);
    dlg.setVisible(true);
    refresh();
  }

  protected void updateSpreadModels(boolean hasKeane){
    if(hasKeane) { // New area has Keane data
      if (fireSpreadModels.contains("KEANE"))
          return;  // Already exists, do nothing.
      fireSpreadModels.add("KEANE"); // Not in vector, add it.
      Collections.reverse(fireSpreadModels); // Should be at the top of the list
    }
    else { // New area does not have keane data
      if (fireSpreadModels.contains("KEANE"))
        fireSpreadModels.remove("KEANE");
    }
  }

}



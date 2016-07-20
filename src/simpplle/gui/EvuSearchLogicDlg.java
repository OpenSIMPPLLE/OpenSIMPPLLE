/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Frame;
import simpplle.comcode.SystemKnowledge;
import simpplle.comcode.EvuSearchLogic;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;
import java.awt.FlowLayout;
import java.util.Vector;
import java.util.ArrayList;
import simpplle.comcode.Evu;
import javax.swing.JOptionPane;
import simpplle.comcode.Area;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import simpplle.JSimpplle;
import simpplle.comcode.TreatmentType;
import simpplle.comcode.TreatmentApplication;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Simpplle;
import simpplle.comcode.ProcessSchedule;
import simpplle.comcode.TreatmentSchedule;
import simpplle.comcode.ProcessApplication;

/**
*
* The University of Montana owns copyright of the designated documentation contained 
* within this file as part of the software product designated by Uniform Resource Identifier 
* UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
* Open Source License Contract pertaining to this documentation and agrees to abide by all 
* restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
* <p>This class creates Evu Search Logic dialog, a type of VegLogicDialog. 
* 
* @author Documentation by Brian Losi
* <p>Original source code authorship: Kirk A. Moeller 
* 
* 
*/
public class EvuSearchLogicDlg extends VegLogicDialog {
  protected String         prototypeCellValue = "117000 - ALTERED-GRASSES/CLOSED-TALL-SHRUB/1";
  protected EvuAnalysis    evuAnalysisDlg;
  protected ArrayList<Evu> units;
  protected int            selectedRow;

  public static boolean isOpen = false;

  protected JPanel       southPanel    = new JPanel();
  protected BorderLayout borderLayout3 = new BorderLayout();
  protected JPanel       resultsPanel  = new JPanel();
  protected TitledBorder resultsBorder;
  protected JScrollPane  resultsScrollPane = new JScrollPane();
  protected JList        resultsList       = new JList();
  protected FlowLayout   resultsLayout     = new FlowLayout();
  protected JToolBar     toolBar           = new JToolBar();
  protected JButton      searchPB          = new JButton();

  protected JMenu     menuOptions = new JMenu("Options");
  protected JMenuItem menuOptionsMakeTreatments      = new JMenuItem();
  protected JMenuItem menuOptionsMakeLockInProcesses = new JMenuItem();
/**
 * Constructor for Evu Search Logic Dialog.  References the VegLogicDialog superclass and passes the frame owner, dialog title, and modality.  
 * The last parameter is the Evu Analysis dialog.  
 * @param owner the frame which ownes the Evu Search Logic Dialog
 * @param title The title of the dialog
 * @param modal modality
 * @param dlg The Evu analysis dialog which has values for currently being analyzed.  
 */
  public EvuSearchLogicDlg(Frame owner, String title, boolean modal, EvuAnalysis dlg) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      isOpen = true;
      evuAnalysisDlg = dlg;
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  /**
   * Overloaded constructor for Evu Search Logic.  References the default constructor and creates a new frame, title, modality = false, and null for Evu
   * Analysis Dialog. 
   */
  public EvuSearchLogicDlg() {
    this(new Frame(), "EvuSearchLogicDlg", false,null);
  }
  /**
   * Initializes panels, tool bar,  results list, components, borders, layouts, and menu options.  
   * @throws Exception
   */
  private void jbInit() throws Exception {
    mainPanel.add(toolBar, BorderLayout.NORTH);
    toolBar.add(searchPB);
    southPanel.setLayout(borderLayout3);
    mainPanel.add(southPanel, java.awt.BorderLayout.SOUTH);

    resultsBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(178, 178, 178)),"Results");
    resultsPanel.setLayout(resultsLayout);
    resultsPanel.setBorder(resultsBorder);

    resultsList.setToolTipText("double-click selection to display in Anaylsis Dialog");
    resultsList.setPrototypeCellValue(prototypeCellValue);
    resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    resultsList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        resultsList_mouseClicked(e);
      }
    });

    southPanel.add(resultsPanel,  BorderLayout.NORTH);
    resultsPanel.add(resultsScrollPane, null);
    resultsScrollPane.getViewport().add(resultsList, null);

    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });

    searchPB.setToolTipText("Search");
    searchPB.setText("Search");
    searchPB.setEnabled(false);
    searchPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        searchPB_actionPerformed(e);
      }
    });



    menuOptionsMakeTreatments.setActionCommand("Make Treatment Schedule");
    menuOptionsMakeTreatments.setText("Make Treatment Schedule");
    menuOptionsMakeTreatments.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuOptionsMakeTreatments_actionPerformed(e);
      }
    });
    menuOptionsMakeLockInProcesses.setActionCommand("Make Lock-in Process Schedule");
    menuOptionsMakeLockInProcesses.setText("Make Lock-in Process Schedule");
    menuOptionsMakeLockInProcesses.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuOptionsMakeLockInProcesses_actionPerformed(e);
      }
    });

    menuBar.add(menuOptions);
    menuOptions.add(menuOptionsMakeTreatments);
    menuOptions.add(menuOptionsMakeLockInProcesses);


  }
/**
 * Initializes the Evu Search logic dialog.  Sets the system knowledge to Evu Search Logic and initializes the kinds in super class.
 * Creates a tabbed panel with panels for evu search logic instances.  
 */
  private void initialize() {
    sysKnowKind = SystemKnowledge.EVU_SEARCH_LOGIC;
    String[] kinds = new String[] {EvuSearchLogic.EVU_SEARCH.toString()};
    super.initialize(kinds);

    tabPanels = new EvuSearchLogicPanel[panelKinds.length];
    for (int i = 0; i < panelKinds.length; i++) {
      String kind = panelKinds[i];
      tabPanels[i] =
          new EvuSearchLogicPanel(this, kind,
                                      EvuSearchLogic.getInstance(),
                                      sysKnowKind);
      tabbedPane.add(tabPanels[i], kind);
    }

    tabbedPane.setSelectedIndex(0);
    tabbedPane_stateChanged(null);
    updateDialog();
  }
/**
 * Gets an array list of Evu units - used in search. 
 * @return arraylist of Evu units.  
 */
  public ArrayList<Evu> getUnits() { return units; }
/**
 * Updates the results of search.  If search units is null hides the results list and outputs no units found.  Otherwise sets the results list with the list data of Evu result data
 * Calculates acres by totaling acreage of evu's in search results.
 */
  private void updateResults() {
    if (units == null) {
      resultsList.setVisible(false);
      JOptionPane.showMessageDialog(this,"No units Found","No Units Found",
                                    JOptionPane.INFORMATION_MESSAGE);
      resultsBorder.setTitle("Results");
    }
    else {
      resultsList.setListData(units.toArray());
      resultsList.setVisible(true);

      int acres=0;
      for (int i=0; i<units.size(); i++) {
        acres += units.get(i).getAcres();
      }
      acres = Math.round(Area.getFloatAcres(acres));
      resultsBorder.setTitle("Results (Total Acres " + Integer.toString(acres) + ")");

      setSize(getPreferredSize());
    }
    update(getGraphics());
  }
/**
 * Creates a units arraylist with evu results of matching units at select rows.  Then sets these units as the results of Evu Analysis dialog.
 * @param e 'Search'
 */
  void searchPB_actionPerformed(ActionEvent e) {
    units = EvuSearchLogic.getInstance().findMatchingUnits(selectedRow);
    evuAnalysisDlg.setResultUnits(units);
    updateResults();
  }
/**
 * Selects a particular Evu from the results list and uses it to get the Evu Analysis dialog going.  
 * @param e mouse double click
 */
  void resultsList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2 && evuAnalysisDlg != null) {
      Evu evu = (Evu)resultsList.getSelectedValue();
      evuAnalysisDlg.goUnit(evu);
    }
  }
/**
 * If window closing event occurs disposes of Evu Search Logic dialog.  
 * @param e 'X' window closing event
 */
  void this_windowClosing(WindowEvent e) {
    isOpen = false;
    setVisible(false);
    dispose();
  }
/**
 * Checks if Evu Search Logic dialog is open.
 * @return true if Evu Search Logic dialog is open.  
 */
  public static boolean isOpen() { return isOpen; }
/**
 * Sets the selected row to be matched in Evu search.
 * @param row the row that will be considered the selected row.    
 */
  public void updateSelectedRow(int row) {
    selectedRow = row;
    searchPB.setEnabled(true);
  }
/**
 * Gets the current area and treatment schedule for area.  If there is no schedule, creates one.  Otherwise creates a new treatment dialog to allow user to 
 * select a treatment.  First a time step is picked by user, then the user picks the treatment, and adds the Evu units to the treatments units.  
 * @param e 'Make Treatments'
 */
  void menuOptionsMakeTreatments_actionPerformed(ActionEvent e) {
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
        treatment.addUnitId( ((Evu)units.get(i)).getId() );
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
   * Method to allow user to lock in process.  Gets current area and process schedule.  User picks a time step to lock in a process to evu units.  
   * @param e 'Make Lock in process'
   */

  void menuOptionsMakeLockInProcesses_actionPerformed(ActionEvent e) {
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
        processApp.addUnitId( ((Evu)units.get(i)).getId() );
      }
      processApp.setTimeStep(tStep);

      String msg = "The Lock-in Process " + processType.toString() + " has been created\n" +
                   "Please see the Process Scheduler for further options";
      JOptionPane.showMessageDialog(this,msg,"Lock-in Process Created",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
  }


}




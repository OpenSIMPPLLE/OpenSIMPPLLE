/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.io.*;

import simpplle.JSimpplle;
import simpplle.comcode.Simpplle;
import simpplle.comcode.ProcessApplication;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Area;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Process;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import simpplle.comcode.SystemKnowledge;

/**
 * This class creates a dialog which allows the user to create, open, and modify Process schedules.
 * For a new process, user can chooser from available system knowledge processes.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class ProcessSchedule extends JDialog {
  private simpplle.comcode.ProcessSchedule schedule;

  private ProcessApplication processApp;
  private boolean            focusLost;


  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JMenuBar menuBar = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileNew = new JMenuItem();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileUnload = new JMenuItem();
  JMenuItem menuFileQuit = new JMenuItem();
  JMenuItem menuFileLoadUnitIdFile = new JMenuItem();
  JPanel northPanel = new JPanel();
  JPanel timeStepPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JLabel timeStepLabel = new JLabel();
  JTextField timeStepText = new JTextField();
  JPanel otherPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel unitIdPanel = new JPanel();
  JButton addPB = new JButton();
  JTextField unitIdText = new JTextField();
  JLabel unitIdLabel = new JLabel();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel chosenPanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JPanel chosenUnitsPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JList chosenUnitsList = new JList();
  JLabel chosenUnitsLabel = new JLabel();
  JButton removeSelectedPB = new JButton();
  JMenuItem menuFileRemoveProcess = new JMenuItem();
  JMenuItem menuFileCopy = new JMenuItem();
  JPanel timeStepOuterPanel = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel prevNextPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton nextPB = new JButton();
  JButton prevPB = new JButton();
  JLabel processNameValue = new JLabel();
  JLabel timeText = new JLabel();
  JPanel timeProcessPanel = new JPanel();
  JPanel processNamePanel = new JPanel();
  JPanel timePanel = new JPanel();
  JLabel processNameLabel = new JLabel();
  JLabel timeLabel = new JLabel();
  GridLayout gridLayout3 = new GridLayout();
  GridLayout gridLayout4 = new GridLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JMenuItem menuFileOldFormat = new JMenuItem();

  public ProcessSchedule(Frame frame, String title, boolean modal) {
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

  public ProcessSchedule() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    menuFile.setText("File");
    menuFileNew.setText("New");
    menuFileNew.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileNew_actionPerformed(e);
      }
    });
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileOpen_actionPerformed(e);
      }
    });
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSaveAs_actionPerformed(e);
      }
    });
    menuFileUnload.setText("Unload");
    menuFileUnload.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileUnload_actionPerformed(e);
      }
    });
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileQuit_actionPerformed(e);
      }
    });
    menuFileLoadUnitIdFile.setText("Load Unit Id File");
    menuFileLoadUnitIdFile.setActionCommand("Load Unit Id File");
    menuFileLoadUnitIdFile.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileLoadUnitIdFile_actionPerformed(e);
      }
    });
    this.setJMenuBar(menuBar);
    northPanel.setLayout(borderLayout4);
    timeStepPanel.setLayout(flowLayout2);
    timeStepLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    timeStepLabel.setText("Time Step");
    timeStepText.setBackground(Color.white);
    timeStepText.setNextFocusableComponent(unitIdText);
    timeStepText.setSelectionColor(Color.blue);
    timeStepText.setColumns(6);
    timeStepText.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        timeStepText_focusLost(e);
      }
    });
    timeStepText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        timeStepText_actionPerformed(e);
      }
    });
    flowLayout2.setAlignment(FlowLayout.LEFT);
    otherPanel.setLayout(borderLayout2);
    addPB.setEnabled(false);
    addPB.setText("Add");
    addPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        addPB_actionPerformed(e);
      }
    });
    unitIdText.setBackground(Color.white);
    unitIdText.setEnabled(false);
    unitIdText.setNextFocusableComponent(chosenUnitsList);
    unitIdText.setSelectionColor(Color.blue);
    unitIdText.setColumns(5);
    unitIdText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        unitIdText_actionPerformed(e);
      }
    });
    unitIdLabel.setText("Unit Id");
    unitIdPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    borderLayout1.setVgap(10);
    chosenPanel.setLayout(flowLayout4);
    chosenUnitsPanel.setLayout(borderLayout3);
    chosenUnitsLabel.setFont(new java.awt.Font("Dialog", 1, 14));
    chosenUnitsLabel.setText("Chosen Units");
    flowLayout4.setAlignment(FlowLayout.LEFT);
    chosenUnitsList.setBackground(Color.white);
    chosenUnitsList.setNextFocusableComponent(removeSelectedPB);
    chosenUnitsList.setSelectionBackground(Color.blue);
    chosenUnitsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        chosenUnitsList_valueChanged(e);
      }
    });
    removeSelectedPB.setEnabled(false);
    removeSelectedPB.setNextFocusableComponent(prevPB);
    removeSelectedPB.setText("Remove Selected");
    removeSelectedPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        removeSelectedPB_actionPerformed(e);
      }
    });
    menuFileRemoveProcess.setText("Remove Current Process");
    menuFileRemoveProcess.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileRemoveProcess_actionPerformed(e);
      }
    });
    menuFileCopy.setText("Copy Current Process");
    menuFileCopy.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileCopy_actionPerformed(e);
      }
    });
    timeStepOuterPanel.setLayout(gridLayout2);
    prevNextPanel.setLayout(flowLayout1);
    nextPB.setNextFocusableComponent(timeStepText);
    nextPB.setIcon(new ImageIcon(simpplle.gui.ProcessSchedule.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(simpplle.gui.ProcessSchedule.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    prevPB.setNextFocusableComponent(nextPB);
    prevPB.setIcon(new ImageIcon(simpplle.gui.ProcessSchedule.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(simpplle.gui.ProcessSchedule.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    borderLayout4.setVgap(10);
    timeStepOuterPanel.setBorder(BorderFactory.createEtchedBorder());
    timeProcessPanel.setLayout(flowLayout5);
    timePanel.setLayout(gridLayout3);
    processNamePanel.setLayout(gridLayout4);
    processNameLabel.setFont(new java.awt.Font("Dialog", 3, 12));
    processNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    processNameLabel.setText("Process:");
    timeLabel.setFont(new java.awt.Font("Dialog", 3, 12));
    timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    timeLabel.setText("Time Step:");
    timeText.setForeground(Color.blue);
    timeText.setText("1");
    processNameValue.setForeground(Color.blue);
    processNameValue.setToolTipText("");
    processNameValue.setText("Stand Replacing Fire                    ");
    flowLayout1.setAlignment(FlowLayout.LEFT);
    gridLayout3.setColumns(1);
    gridLayout3.setRows(2);
    gridLayout4.setColumns(1);
    gridLayout4.setRows(2);
    jScrollPane1.setPreferredSize(new Dimension(100, 250));
    menuFileOldFormat.setText("Import Old Format File");
    menuFileOldFormat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOldFormat_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    menuBar.add(menuFile);
    menuFile.add(menuFileNew);
    menuFile.add(menuFileCopy);
    menuFile.addSeparator();
    menuFile.add(menuFileOpen);
    menuFile.addSeparator();
    menuFile.add(menuFileOldFormat);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileLoadUnitIdFile);
    menuFile.addSeparator();
    menuFile.add(menuFileRemoveProcess);
    menuFile.add(menuFileUnload);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(timeStepOuterPanel, BorderLayout.CENTER);
    timeStepOuterPanel.add(timeStepPanel, null);
    timeStepPanel.add(timeStepLabel, null);
    timeStepPanel.add(timeStepText, null);
    northPanel.add(prevNextPanel, BorderLayout.NORTH);
    prevNextPanel.add(prevPB, null);
    prevNextPanel.add(nextPB, null);
    prevNextPanel.add(timeProcessPanel, null);
    timeProcessPanel.add(timePanel, null);
    timePanel.add(timeLabel, null);
    timePanel.add(processNameLabel, null);
    timeProcessPanel.add(processNamePanel, null);
    processNamePanel.add(timeText, null);
    processNamePanel.add(processNameValue, null);
    mainPanel.add(otherPanel, BorderLayout.CENTER);
    otherPanel.add(unitIdPanel, BorderLayout.NORTH);
    unitIdPanel.add(unitIdLabel, null);
    unitIdPanel.add(unitIdText, null);
    unitIdPanel.add(addPB, null);
    otherPanel.add(chosenPanel, BorderLayout.CENTER);
    chosenPanel.add(chosenUnitsPanel, null);
    chosenUnitsPanel.add(chosenUnitsLabel, BorderLayout.NORTH);
    chosenUnitsPanel.add(jScrollPane1, BorderLayout.SOUTH);
    jScrollPane1.getViewport().add(chosenUnitsList, null);
    chosenPanel.add(removeSelectedPB, null);
  }

/**
 * This initializes the process scheduler with a current process application.  
 */
  private void initialize() {
    Area area = Simpplle.getCurrentArea();

    schedule = area.getProcessSchedule();
    if (schedule == null) {
      schedule = area.createProcessSchedule();
    }
    processApp = schedule.getCurrentApplication();

    focusLost = false;

    updateDialog();
  }

  private void refresh() {
    update(getGraphics());
  }

  private void updateDialog() {
    if (processApp != null) {
      updateDialogProcessScheduleLoaded();
    }
    else {
      updateDialogNoProcessScheduleLoaded();
    }
    //setSize(getPreferredSize());
    refresh();
  }
/**
 * This updates the process scheduler with a loaded process schedule.  Enables the copy, save (if a PS file already exists, save as, remove process, and file unload
 * ,menu items. Displays the process, time step, and the choosen units for this scheduled process.    
 */
  private void updateDialogProcessScheduleLoaded() {
    // Menu Items
    menuFileCopy.setEnabled(true);
    menuFileSave.setEnabled((SystemKnowledge.getFile(SystemKnowledge.PROCESS_SCHEDULE) != null));
    menuFileSaveAs.setEnabled(true);

    menuFileRemoveProcess.setEnabled(true);
    menuFileUnload.setEnabled(true);

    prevPB.setEnabled(true);
    nextPB.setEnabled(true);

    processNameValue.setText(processApp.getProcessType().toString());

    String time  = "";
    if (processApp.isTimeStepSet()) {
      time = Integer.toString(processApp.getTimeStep());
    }

    timeText.setText(time);
    timeStepText.setText(time);
    timeStepText.setEnabled(true);

    chosenUnitsList.setListData(processApp.getUnitId());

    unitIdText.setEnabled(true);
    addPB.setEnabled(true);
  }
/**
 * If no process schedule basically clears out the process schedule dialog by disabling previous, next buttons, and menu items for save, save as, copy, remove and unload.  
 * It clears out the process name and time step labels.  
 */
  private void updateDialogNoProcessScheduleLoaded() {
    timeStepText.setEnabled(false);
    prevPB.setEnabled(false);
    nextPB.setEnabled(false);

    // Clear some text fields
    processNameValue.setText("");
    timeStepText.setText("");
    timeText.setText("");

    // Menu Items
    menuFileSave.setEnabled(false);
    menuFileSaveAs.setEnabled(false);
    menuFileCopy.setEnabled(false);
    menuFileRemoveProcess.setEnabled(false);
    menuFileUnload.setEnabled(false);

    chosenUnitsList.setListData(new String[1]);

    unitIdText.setEnabled(false);
    addPB.setEnabled(false);
    removeSelectedPB.setEnabled(false);
  }

  // *** Menu's ***
  // **************
/**
 * 
 * @param e
 */
  void menuFileNew_actionPerformed(ActionEvent e) {
    ProcessType         processType;
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;

    dlg = new ListSelectionDialog(theFrame,"Select a new Process",true,
                                  Process.getSimulationProcessesArray());

    dlg.setVisible(true);
    processType = (ProcessType)dlg.getSelection();
    if (processType != null) {
      processApp = schedule.newApplication(processType);
      updateDialog();
    }
    refresh();
  }

  void menuFileCopy_actionPerformed(ActionEvent e) {
    schedule.copyCurrentApplication();
    processApp = schedule.getCurrentApplication();
    updateDialog();
    refresh();
  }

  void menuFileOpen_actionPerformed(ActionEvent e) {
    if (SystemKnowledgeFiler.openFile(this,SystemKnowledge.PROCESS_SCHEDULE,menuFileSave,null)) {

      schedule = Simpplle.getCurrentArea().getProcessSchedule();
      processApp = schedule.getCurrentApplication();
      if (processApp == null) {
        schedule = Simpplle.getCurrentArea().createProcessSchedule();
        return;
      }
      updateDialog();
    }
    refresh();
  }

  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    MyFileFilter extFilter;
    String       title = "Select a Process schedule file.";

    extFilter = new MyFileFilter("process",
                                 "Process Schedule Files (*.process)");

    setCursor(Utility.getWaitCursor());

    File infile = Utility.getOpenFile(this,title,extFilter);
    try {
      if (infile != null) {
        Simpplle.getCurrentArea().readProcessSchedule(infile);

        schedule   = Simpplle.getCurrentArea().getProcessSchedule();
        processApp = schedule.getCurrentApplication();
        if (processApp == null) {
          schedule = Simpplle.getCurrentArea().createProcessSchedule();
          return;
        }
        updateDialog();
      }
    }
    catch (SimpplleError se) {
      JOptionPane.showMessageDialog(this,se.getError(),"Error reading file",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());
    refresh();
  }
  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.PROCESS_SCHEDULE);
    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.PROCESS_SCHEDULE,
                                  menuFileSave, null);
  }
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.PROCESS_SCHEDULE,
                                  menuFileSave, null);
  }


  void menuFileLoadUnitIdFile_actionPerformed(ActionEvent e) {
    File         outfile;
    MyFileFilter extFilter = new MyFileFilter("txt", "Text Files (*.txt)");
    String       title     = "Select a Unit Id file";

    setCursor(Utility.getWaitCursor());

    outfile = Utility.getOpenFile(this,title,extFilter);
    if (outfile == null) {
      refresh();
      return;
    }

    try {
      schedule.readUnitIdFile(outfile);
    }
    catch (SimpplleError err) {
      String msg = "Loading of Unit Id's was only partially " +
                   "successfull due to the following error:\n\n" +
                   err.getMessage();
      JOptionPane.showMessageDialog(this,msg,"Problems reading file",
                                    JOptionPane.ERROR_MESSAGE);
      setCursor(Utility.getNormalCursor());
    }
    setCursor(Utility.getNormalCursor());
    processApp = schedule.getCurrentApplication();
    updateDialog();
    refresh();
  }

  void menuFileRemoveProcess_actionPerformed(ActionEvent e) {
    String msg = "This will Delete the Currently displayed\n" +
                 "Process.\n\n" +
                 "Continue and delete the schedule?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Delete Process",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      schedule.removeCurrentApplication();
      processApp = schedule.getCurrentApplication();
      updateDialog();
    }
    refresh();
  }

  void menuFileUnload_actionPerformed(ActionEvent e) {
    String msg = "This will remove the currently loaded schedule\n" +
                 "from memory.  Do this if you do not wish to\n" +
                 "apply the processes to the next simulation.\n\n" +
                 "Continue and remove the schedule?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Unload Schedule",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      processApp = null;
      Simpplle.getCurrentArea().removeProcessSchedule();
      schedule  = Simpplle.getCurrentArea().createProcessSchedule();

      updateDialog();
    }
    refresh();
  }

  void menuFileQuit_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  // *** Events ***
  // **************

  void timeStepText_actionPerformed(ActionEvent e) {
    timeStepText.getNextFocusableComponent().requestFocus();
  }

  void timeStepText_focusLost(FocusEvent e) {
    int    timeStep;

    if (processApp == null) { return; }

    // This prevents multiple calls and error messages.
    if (focusLost) { return; }

    try {
      timeStep = Integer.parseInt(timeStepText.getText());
      if (timeStep < 1 || timeStep > 50) {
        throw new NumberFormatException();
      }
      timeText.setText(Integer.toString(timeStep));
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      String msg = "Time step should be a number (1-50)";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          timeStepText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
    processApp.setTimeStep(timeStep);
  }

  // Unit Id Selection
  private void addUnitId() {
    int  unitId;
    Area area = Simpplle.getCurrentArea();

    try {
      unitId = Integer.parseInt(unitIdText.getText());
      if (area.isValidUnitId(unitId) == false) {
        JOptionPane.showMessageDialog(this,"Invalid Unit Id","Invalid Unit Id",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
      processApp.addUnitId(unitId);
      chosenUnitsList.setListData(processApp.getUnitId());
      unitIdText.setText("");
    }
    catch (NumberFormatException nfe) {
      String msg = "Please enter a valid unit Id.";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
  }
/**
 * 
 * @param e
 */
  void unitIdText_actionPerformed(ActionEvent e) {
    addUnitId();
  }

  void addPB_actionPerformed(ActionEvent e) {
    addUnitId();
  }

  void chosenUnitsList_valueChanged(ListSelectionEvent e) {
    boolean selected = (chosenUnitsList.isSelectionEmpty() == false);

    removeSelectedPB.setEnabled(selected);
  }

  void removeSelectedPB_actionPerformed(ActionEvent e) {
    Object[] selected = chosenUnitsList.getSelectedValues();
    if (selected == null) { return; }

    for(int i=0;i<selected.length;i++) {
      processApp.removeUnitId((Integer)selected[i]);
    }
    updateDialog();
    removeSelectedPB.setEnabled(false);
    chosenUnitsList.clearSelection();
  }

  void prevPB_actionPerformed(ActionEvent e) {
    processApp = schedule.getPrevApplication();
    updateDialog();
  }

  void nextPB_actionPerformed(ActionEvent e) {
    processApp = schedule.getNextApplication();
    updateDialog();
  }

}

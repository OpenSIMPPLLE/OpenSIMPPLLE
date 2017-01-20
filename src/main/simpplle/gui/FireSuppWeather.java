/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.FireSuppWeatherData;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SystemKnowledge;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import simpplle.comcode.ValidZones;
import simpplle.comcode.RegionalZone;

/** 
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class FireSuppWeather extends JDialog {

  private FireSuppWeatherTableDataModel dataModel = new FireSuppWeatherTableDataModel();
  private JMenuItem menuFileSave = new JMenuItem();
  private JMenuItem menuFileClose = new JMenuItem();
  private JTable table = new JTable();

  public FireSuppWeather(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }

  public FireSuppWeather() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {

    setLayout(new BorderLayout());
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel probPanel = new JPanel(new BorderLayout());
    JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    JMenuItem menuFileSaveAs = new JMenuItem("Save As");
    JMenu menuFile = new JMenu("File");
    JMenuItem menuFileQuit = new JMenuItem("Close Dialog");
    JMenuItem menuFileOpen = new JMenuItem("Open");
    JMenuItem menuFileClose = new JMenuItem("Close");
    JMenuItem menuFileSave = new JMenuItem("Save");
    JMenuBar menuBar = new JMenuBar();
    JMenuItem menuFileLoadDefaults = new JMenuItem("Load Defaults");
    JMenu menuKnowledgeSource = new JMenu("Knowledge Source");
    JMenuItem menuKnowledgeSourceDisplay = new JMenuItem("Display");
    JMenuItem menuFileOldFormat = new JMenuItem("Import Old Format File");
    JScrollPane scrollPane = new JScrollPane(table);
    JMenu menuOptions = new JMenu("Options");
    JMenuItem menuOptionsSplitRange = new JMenuItem("Split Selected Range...");
    JMenuItem menuOptionsMergeUp = new JMenuItem("Merge Selected with Previous");
    JMenuItem menuOptionsMergeDown = new JMenuItem("Merge Selected with Next");

    menuFileSaveAs.addActionListener(this::menuFileSaveAs_actionPerformed);

    menuFileQuit.addActionListener(this::menuFileQuit_actionPerformed);

    menuFileOpen.addActionListener(this::menuFileOpen_actionPerformed);

    menuFileClose.addActionListener(this::menuFileClose_actionPerformed);

    menuFileSave.addActionListener(this::menuFileSave_actionPerformed);

    menuFileLoadDefaults.addActionListener(this::menuFileLoadDefaults_actionPerformed);

    menuKnowledgeSourceDisplay.addActionListener(this::menuKnowledgeSourceDisplay_actionPerformed);

    menuFileOldFormat.addActionListener(this::menuFileOldFormat_actionPerformed);

    menuOptionsSplitRange.addActionListener(this::menuOptionsSplitRange_actionPerformed);

    menuOptionsMergeUp.addActionListener(this::menuOptionsMergeUp_actionPerformed);

    menuOptionsMergeDown.addActionListener(this::menuOptionsMergeDown_actionPerformed);

    add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(probPanel, null);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuFileOldFormat);
    menuFile.addSeparator();
    menuFile.add(menuFileLoadDefaults);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    menuOptions.add(menuOptionsSplitRange);
    menuOptions.addSeparator();
    menuOptions.add(menuOptionsMergeUp);
    menuOptions.add(menuOptionsMergeDown);
    menuBar.add(menuFile);
    menuBar.add(menuOptions);
    menuBar.add(menuKnowledgeSource);
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);
    probPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
    setJMenuBar(menuBar);
    table.setModel(dataModel);
  }

  private void initialize() {
    boolean seasonZone =
        (Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_FRONT_RANGE ||
         Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_PLATEAU ||
         RegionalZone.isWyoming());

    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    updateDialog();
  }

  private void updateDialog() {
    File file = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
    menuFileClose.setEnabled((file != null));
    menuFileSave.setEnabled((file != null));
//    menuFileSave.setEnabled((FireSuppWeatherData.hasDataChanged() &&
//                             FireSuppWeatherData.getDataFilename() != null));
    update(getGraphics());
  }

  private void quit() {
    setVisible(false);
    dispose();
  }

  // *** Menus ***
  // *************
  void menuFileOpen_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this,
                                  SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,menuFileSave,menuFileClose);
    updateDialog();
    update(getGraphics());
  }

  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    File         inputFile;
    MyFileFilter extFilter;
    String       title = "Fire Suppression Data (Weather)?";

    extFilter = new MyFileFilter("firesuppweather",
                                 "(*.firesuppweather)");

    setCursor(Utility.getWaitCursor());
    inputFile = Utility.getOpenFile(this,title,extFilter);

    if (inputFile != null) {
      try {
        FireSuppWeatherData.loadData(inputFile);
        updateDialog();
//        menuFileSave.setEnabled(true);
//        menuFileClose.setEnabled(true);
      }
      catch (simpplle.comcode.SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
    setCursor(Utility.getNormalCursor());
    update(getGraphics());
  }

  void menuFileLoadDefaults_actionPerformed(ActionEvent e) {
    int choice;
    try {
      String msg = "This will load the default Fire " +
                   "Suppression Weather Probability Data.\n\n" +
                   "Do you wish to continue?";
      String title = "Load Default Fire Suppression Weather Data";
      choice = JOptionPane.showConfirmDialog(this,msg,title,
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);
      if (choice == JOptionPane.YES_OPTION) {
        SystemKnowledge.loadZoneKnowledge(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
        updateDialog();
      }
    }
    catch (simpplle.comcode.SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void menuFileClose_actionPerformed(ActionEvent e) {
    int    choice;
    String msg;

    if (SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A) != null &&
        SystemKnowledge.hasChangedOrUserData(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A)) {
      msg = "Changes have been made.\n" +
            "If you continue these changes will be lost.\n\n" +
            "Do you wish to continue?";
      choice = JOptionPane.showConfirmDialog(this,msg,"Close Current File.",
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);

      if (choice == JOptionPane.NO_OPTION) {
        update(getGraphics());
        return;
      }
    }

    try {
      SystemKnowledge.loadZoneKnowledge(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
    }
    catch (simpplle.comcode.SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
                                    JOptionPane.ERROR_MESSAGE);
    }
    updateDialog();
  }

  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,
                                  menuFileSave, menuFileClose);
    update(getGraphics());
  }

  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this,
                                  SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,
                                  menuFileSave, menuFileClose);
    update(getGraphics());
  }

  void menuFileQuit_actionPerformed(ActionEvent e) {
    quit();
  }

  void menuOptionsSplitRange_actionPerformed(ActionEvent e) {
    int row = table.getSelectedRow();
    if (row == -1) {
      JOptionPane.showMessageDialog(simpplle.JSimpplle.getSimpplleMain(),
              "Please Select a row in the table.","Nothing Selected",JOptionPane.WARNING_MESSAGE);
      return;
    }

    Point dlgLocation = this.getLocation();
    int minLower = FireSuppWeatherData.getMinSplitAcres(row);
    String msg = "Acres at which to split the range " + FireSuppWeatherData.getValidSplitAcresDescription(row);
    int splitAcres= AskNumber.getInput("Split Range",msg,minLower,dlgLocation);

    if (splitAcres == -1) {
      return;
    }

    if (!FireSuppWeatherData.isValidSplitAcres(row,splitAcres)) {
      JOptionPane.showMessageDialog(simpplle.JSimpplle.getSimpplleMain(),
              "Invalid Acres","Acres not within selected Range",JOptionPane.WARNING_MESSAGE);
      return;
    }

    FireSuppWeatherData.splitRow(row,splitAcres);
    dataModel.fireTableDataChanged();
  }

  void menuOptionsMergeUp_actionPerformed(ActionEvent e) {
    int row = table.getSelectedRow();
    if (row == -1) {
      JOptionPane.showMessageDialog(simpplle.JSimpplle.getSimpplleMain(),
              "Please Select a row in the table.","Nothing Selected",JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (row == 0) {
      JOptionPane.showMessageDialog(simpplle.JSimpplle.getSimpplleMain(),
              "No Previous Row to Merge with","No Previous Row",JOptionPane.WARNING_MESSAGE);
      return;
    }

    String msg =  "Merge Selected Row with Row Above\n" +
            "Probabilities of the two rows will be averaged.\n\n" +
            "Do you wish to continue?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Close Current File.",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.NO_OPTION) {
      update(getGraphics());
      return;
    }
    FireSuppWeatherData.mergeRowUp(row);
    dataModel.fireTableDataChanged();
  }
  void menuOptionsMergeDown_actionPerformed(ActionEvent e) {
    int row = table.getSelectedRow();
    if (row == -1) {
      JOptionPane.showMessageDialog(simpplle.JSimpplle.getSimpplleMain(),
              "Please Select a row in the table.","Nothing Selected",JOptionPane.WARNING_MESSAGE);
      return;
    }
    int rowCount = table.getRowCount();
    if (row == rowCount-1) {
      JOptionPane.showMessageDialog(simpplle.JSimpplle.getSimpplleMain(),
              "No Next Row to Merge with","No Next Row",JOptionPane.WARNING_MESSAGE);
      return;
    }

    String msg =  "Merge Selected Row with Row Below\n" +
            "Probabilities of the two rows will be averaged.\n\n" +
            "Do you wish to continue?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Close Current File.",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.NO_OPTION) {
      update(getGraphics());
      return;
    }
    FireSuppWeatherData.mergeRowDown(row);
    dataModel.fireTableDataChanged();
  }
  // *** Probability Values ***
  // **************************

  void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
    String title = "Weather Ending Fire Suppression (Beyond Class A) Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }

}






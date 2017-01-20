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

    // File menu
    JMenuItem menuFileOpen = new JMenuItem("Open");
    menuFileOpen.addActionListener(this::openFile);

    JMenuItem menuFileImportOldFormat = new JMenuItem("Import Old Format File");
    menuFileImportOldFormat.addActionListener(this::importOldFormatFile);

    JMenuItem menuFileLoadDefaults = new JMenuItem("Load Defaults");
    menuFileLoadDefaults.addActionListener(this::loadDefaultData);

    JMenuItem menuFileSave = new JMenuItem("Save");
    menuFileSave.addActionListener(this::saveFile);

    JMenuItem menuFileSaveAs = new JMenuItem("Save As");
    menuFileSaveAs.addActionListener(this::saveFileAs);

    JMenuItem menuFileQuit = new JMenuItem("Close Dialog");
    menuFileQuit.addActionListener(this::closeDialogue);

    JMenu menuFile = new JMenu("File");
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileImportOldFormat);
    menuFile.add(menuFileLoadDefaults);
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.add(menuFileQuit);

    // Options menu
    JMenuItem menuOptionsSplitRange = new JMenuItem("Split Selected Range...");
    menuOptionsSplitRange.addActionListener(this::splitRange);

    JMenuItem menuOptionsMergeUp = new JMenuItem("Merge Selected with Previous");
    menuOptionsMergeUp.addActionListener(this::mergeUp);

    JMenuItem menuOptionsMergeDown = new JMenuItem("Merge Selected with Next");
    menuOptionsMergeDown.addActionListener(this::mergeDown);

    JMenu menuOptions = new JMenu("Options");
    menuOptions.add(menuOptionsSplitRange);
    menuOptions.addSeparator();
    menuOptions.add(menuOptionsMergeUp);
    menuOptions.add(menuOptionsMergeDown);

    // Knowledge Source menu
    JMenuItem menuKnowledgeSourceDisplay = new JMenuItem("Display");
    menuKnowledgeSourceDisplay.addActionListener(this::displayKnowledgeSource);

    JMenu menuKnowledgeSource = new JMenu("Knowledge Source");
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(menuFile);
    menuBar.add(menuOptions);
    menuBar.add(menuKnowledgeSource);

    JScrollPane scrollPane = new JScrollPane(table);

    JPanel probPanel = new JPanel(new BorderLayout());
    probPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    northPanel.add(probPanel, null);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(northPanel, BorderLayout.NORTH);

    setLayout(new BorderLayout());
    add(mainPanel, BorderLayout.CENTER);

    setJMenuBar(menuBar);

  }

  private void initialize() {
    boolean seasonZone =
        (Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_FRONT_RANGE ||
         Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_PLATEAU ||
         RegionalZone.isWyoming());

    table.setModel(dataModel);
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

  // *** Menus ***
  // *************
  private void openFile(ActionEvent e) {
    SystemKnowledgeFiler.openFile(this,
                                  SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,menuFileSave,menuFileClose);
    updateDialog();
  }

  private void importOldFormatFile(ActionEvent e) {
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

  private void loadDefaultData(ActionEvent e) {

    try {

      if (SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A) != null &&
          SystemKnowledge.hasChangedOrUserData(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A)) {

        String msg = "Changes have been made.\n" +
                     "If you continue these changes will be lost.\n\n" +
                     "Do you wish to continue?";

        int choice = JOptionPane.showConfirmDialog(this,msg,"Close Current File.",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.NO_OPTION) {
          update(getGraphics());
          return;
        }
      }

      String msg = "This will load the default Fire " +
                   "Suppression Weather Probability Data.\n\n" +
                   "Do you wish to continue?";

      String title = "Load Default Fire Suppression Weather Data";

      int choice = JOptionPane.showConfirmDialog(this,msg,title,
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);

      if (choice == JOptionPane.YES_OPTION) {

        SystemKnowledge.loadZoneKnowledge(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
        updateDialog();
      }
    } catch (simpplle.comcode.SimpplleError err) {

      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void saveFile(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,
                                  menuFileSave, menuFileClose);
    update(getGraphics());
  }

  private void saveFileAs(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this,
                                  SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,
                                  menuFileSave, menuFileClose);
    update(getGraphics());
  }

  private void closeDialogue(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  private void splitRange(ActionEvent e) {
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

  private void mergeUp(ActionEvent e) {
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

  private void mergeDown(ActionEvent e) {
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

  private void displayKnowledgeSource(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
    String title = "Weather Ending Fire Suppression (Beyond Class A) Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }

}






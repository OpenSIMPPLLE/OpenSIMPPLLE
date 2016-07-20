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
import simpplle.comcode.SimpplleError;
import simpplle.comcode.SystemKnowledge;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
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

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel probPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileQuit = new JMenuItem();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuBar menuBar = new JMenuBar();
  JMenuItem menuFileLoadDefaults = new JMenuItem();
  BorderLayout borderLayout2 = new BorderLayout();
  JMenu menuKnowledgeSource = new JMenu();
  JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
  JMenuItem menuFileOldFormat = new JMenuItem();
  JScrollPane scrollPane = new JScrollPane();
  JTable table = new JTable();
  BorderLayout borderLayout3 = new BorderLayout();

  JMenu menuOptions = new JMenu();
  JMenuItem menuOptionsSplitRange = new JMenuItem();
  JMenuItem menuOptionsMergeUp = new JMenuItem();
  JMenuItem menuOptionsMergeDown = new JMenuItem();

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
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    probPanel.setLayout(borderLayout3);
    northPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSaveAs_actionPerformed(e);
      }
    });
    menuFile.setText("File");
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileQuit_actionPerformed(e);
      }
    });
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOpen_actionPerformed(e);
      }
    });
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileClose_actionPerformed(e);
      }
    });
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuFileLoadDefaults.setText("Load Defaults");
    menuFileLoadDefaults.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileLoadDefaults_actionPerformed(e);
      }
    });
    this.getContentPane().setLayout(borderLayout2);
    menuKnowledgeSource.setText("Knowledge Source");
    menuKnowledgeSourceDisplay.setText("Display");
    menuKnowledgeSourceDisplay.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuKnowledgeSourceDisplay_actionPerformed(e);
      }
    });
    menuFileOldFormat.setText("Import Old Format File");
    menuFileOldFormat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOldFormat_actionPerformed(e);
      }
    });

    menuOptions.setText("Options");
    menuOptionsSplitRange.setText("Split Selected Range...");
    menuOptionsSplitRange.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuOptionsSplitRange_actionPerformed(e);
      }
    });
    menuOptionsMergeUp.setText("Merge Selected with Previous");
    menuOptionsMergeUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuOptionsMergeUp_actionPerformed(e);
      }
    });
    menuOptionsMergeDown.setText("Merge Selected with Next");
    menuOptionsMergeDown.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuOptionsMergeDown_actionPerformed(e);
      }
    });

    getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(probPanel, null);
    scrollPane.getViewport().add(table);
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
    this.setJMenuBar(menuBar);
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
        SystemKnowledge.readZoneDefault(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
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
      SystemKnowledge.readZoneDefault(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
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






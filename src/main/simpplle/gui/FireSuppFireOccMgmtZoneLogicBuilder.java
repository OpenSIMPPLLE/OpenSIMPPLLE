/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders;
import java.util.Vector;
import simpplle.JSimpplle;
import simpplle.comcode.*;

/**
 * This dialog allows users to create and edit fire management zones. Users can create additional
 * zones and delete non-default zones. Each zone has a number of acres, the number of fire starts
 * in the past 10 years, and a response time in hours.
 */

class FireSuppFireOccMgmtZoneLogicBuilder extends JDialog {

  private RegionalZone currentZone;
  private Vector<Fmz> allFmz;

  private JPanel innerPanel;

  private JMenuItem menuActionCreate;
  private JMenuItem menuActionDelete;
  private JMenuItem menuActionDeleteAll;
  private JMenuItem menuFileLoadDefaultData;
  private JMenuItem menuFileImportOldFormat;
  private JMenuItem menuFileOpen;
  private JMenuItem menuCloseDialog;
  private JMenuItem menuFileSave;
  private JMenuItem menuFileSaveAs;
  private JMenuItem menuKnowledgeSourceDisplay;

  FireSuppFireOccMgmtZoneLogicBuilder(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      initialize();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    menuFileOpen = new JMenuItem("Open");
    menuFileOpen.addActionListener(this::openFile);

    menuFileImportOldFormat = new JMenuItem("Import Old Format File");
    menuFileImportOldFormat.addActionListener(this::importOldFormatFile);

    menuFileLoadDefaultData = new JMenuItem("Load Default Data");
    menuFileLoadDefaultData.setToolTipText("Load default fmz data file for the current zone");
    menuFileLoadDefaultData.addActionListener(this::loadDefaultData);

    menuFileSave = new JMenuItem("Save");
    menuFileSave.setEnabled(false);
    menuFileSave.addActionListener(this::saveFile);

    menuFileSaveAs = new JMenuItem("Save As");
    menuFileSaveAs.addActionListener(this::saveFileAs);

    menuCloseDialog = new JMenuItem("Close Dialog");
    menuCloseDialog.addActionListener(this::closeDialog);

    JMenu menuFile = new JMenu();
    menuFile.setText("File");
    menuFile.add(menuFileOpen);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileImportOldFormat);
    menuFile.add(menuFileLoadDefaultData);
    menuFile.addSeparator();
    menuFile.add(menuCloseDialog);

    menuActionCreate = new JMenuItem("Create New Zone");
    menuActionCreate.addActionListener(this::createNewZone);

    menuActionDelete = new JMenuItem("Delete Zone");
    menuActionDelete.addActionListener(this::deleteZone);
    menuActionDelete.setEnabled(false);

    menuActionDeleteAll = new JMenuItem("Delete All Zones");
    menuActionDeleteAll.addActionListener(this::deleteAllZones);

    JMenu menuAction = new JMenu("Actions");
    menuAction.add(menuActionCreate);
    menuAction.add(menuActionDelete);
    menuAction.add(menuActionDeleteAll);

    menuKnowledgeSourceDisplay = new JMenuItem("Display");
    menuKnowledgeSourceDisplay.addActionListener(this::displayKnowledgeSource);

    JMenu menuKnowledgeSource = new JMenu("Knowledge Source");
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(menuFile);
    menuBar.add(menuAction);
    menuBar.add(menuKnowledgeSource);

    setJMenuBar(menuBar);

    JLabel zoneLabel = new JLabel("Zone");
    zoneLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
    zoneLabel.setHorizontalAlignment(JLabel.CENTER);
    zoneLabel.setVerticalAlignment(JLabel.CENTER);

    JLabel acreLabel = new JLabel("Acres");
    acreLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
    acreLabel.setHorizontalAlignment(JLabel.CENTER);
    acreLabel.setVerticalAlignment(JLabel.CENTER);

    JLabel fireTotalLabel = new JLabel("Starts Per Decade");
    fireTotalLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
    fireTotalLabel.setHorizontalAlignment(JLabel.CENTER);
    fireTotalLabel.setVerticalAlignment(JLabel.CENTER);

    JLabel responseLabel = new JLabel("Response Time (Hours)");
    responseLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
    responseLabel.setHorizontalAlignment(JLabel.CENTER);
    responseLabel.setVerticalAlignment(JLabel.CENTER);

    JPanel headerPanel = new JPanel(new GridLayout(1, 4, 5, 0));
    headerPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    headerPanel.setMaximumSize(new Dimension(1000, 30));
    headerPanel.setBorder(new EmptyBorder(5,5,5,5));
    headerPanel.add(zoneLabel);
    headerPanel.add(acreLabel);
    headerPanel.add(fireTotalLabel);
    headerPanel.add(responseLabel);

    JPanel headerGlue = new JPanel();
    headerGlue.setLayout(new BoxLayout(headerGlue, BoxLayout.Y_AXIS));
    headerGlue.add(headerPanel);

    innerPanel = new JPanel();
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(innerPanel);
    scrollPane.getVerticalScrollBar().setUnitIncrement(8);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    JPanel outerPanel = new JPanel(new BorderLayout());
    outerPanel.add(headerGlue, BorderLayout.NORTH);
    outerPanel.add(scrollPane, BorderLayout.CENTER);

    add(outerPanel);

    setPreferredSize(new Dimension(700, 600));
    setMinimumSize(new Dimension(600, 300));

  }

  private void initialize() {

    currentZone = Simpplle.getCurrentZone();

    allFmz = currentZone.getAllFmzNames();
    if (allFmz == null || allFmz.size() == 0) {
      Fmz.makeDefault();
      allFmz = currentZone.getAllFmzNames();
    }

    for (Fmz item : currentZone.getAllFmz()) {
      innerPanel.add(new FmzPanel(item));
    }

    Fmz[] fmzArray = currentZone.getAllFmz();
    menuActionDelete.setEnabled(fmzArray.length > 1);

  }

  private void refresh() {
    update(getGraphics());
  }

  private void update() {

    innerPanel.removeAll();
    for (Fmz item : currentZone.getAllFmz()) {
      innerPanel.add(new FmzPanel(item));
    }

    Fmz[] fmzArray = currentZone.getAllFmz();
    menuActionDelete.setEnabled(fmzArray.length > 1); // Checks if non-default are present

    File file = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    menuFileSave.setEnabled(file != null);

    repaint();
    revalidate();
  }

  private void openFile(ActionEvent e) {
    if (Simpplle.getCurrentArea() != null) {
      int choice = JOptionPane.showConfirmDialog(this,
                                                 "Any unit assigned an FMZ that is not present\n" +
                                                 "in the new file will be reassigned to the\n" +
                                                 "default zone.\n\n" +
                                                 "Do you wish to continue?",
                                                 "Reassign Unit Zones",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE);
      if (choice == JOptionPane.NO_OPTION) {
        return;
      }
    }

    SystemKnowledgeFiler.openFile(this, SystemKnowledge.FMZ, menuFileSave, null);

    allFmz = currentZone.getAllFmzNames();

    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      area.updateFmzData();
    }

    update();

  }

  private void importOldFormatFile(ActionEvent e) {

    if (Simpplle.getCurrentArea() != null) {
      int choice = JOptionPane.showConfirmDialog(this,
                                                 "All units in the current area will be\n" +
                                                 "reassigned to the default zone.\n\n" +
                                                 "Do you wish to continue?",
                                                 "Reassign Unit Zones",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE);
      if (choice == JOptionPane.NO_OPTION) {
        return;
      }
    }

    MyFileFilter filter = new MyFileFilter("fmz", "FMZ Data Files (*.fmz)");
    File file = Utility.getOpenFile(this, "Select a Fire Management Zone Data File", filter);
    if (file == null) {
      return;
    }

    setCursor(Utility.getWaitCursor());

    try {
      Fmz.loadData(file);
    } catch (SimpplleError error) {
      JOptionPane.showMessageDialog(this, error.getError(),
                                    "Error loading file",
                                    JOptionPane.ERROR_MESSAGE);
    }

    setCursor(Utility.getNormalCursor());

    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      area.updateFmzData();
    }

    allFmz = currentZone.getAllFmzNames();
    menuFileSave.setEnabled(true);
    refresh();
  }

  private void loadDefaultData(ActionEvent e) {
    if (Fmz.hasChanged()) {
      int choice = JOptionPane.showConfirmDialog(this,
                                                 "Loading the default data will overwrite\n" +
                                                 "existing changes.\n\n" +
                                                 "Do you wish to continue?",
                                                 "Discard Changes",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE);
      if (choice == JOptionPane.NO_OPTION) {
        return;
      }
    }
    if (Simpplle.getCurrentArea() != null) {
      int choice = JOptionPane.showConfirmDialog(this,
                                                 "All units in the current area will be\n" +
                                                 "reassigned to the default zone.\n\n" +
                                                 "Do you wish to continue?",
                                                 "Reassign Unit Zones",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE);
      if (choice == JOptionPane.NO_OPTION) {
        return;
      }
    }
    try {
      Fmz.closeFile();
      SystemKnowledge.loadZoneKnowledge(SystemKnowledge.FMZ);
      Area area = Simpplle.getCurrentArea();
      if (area != null) {
        area.updateFmzData();
      }
      update();
    } catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,
                                    err.getError(),
                                    "Error Loading File",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void saveFile(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    SystemKnowledgeFiler.saveFile(this, outfile, SystemKnowledge.FMZ, menuFileSave, null);
    refresh();
  }

  private void saveFileAs(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.FMZ, menuFileSave, null);
    refresh();
  }

  private void closeDialog(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  private void createNewZone(ActionEvent e) {
    String name = JOptionPane.showInputDialog(this,
                                              "Fire Management Zone Name",
                                              "Create New Zone",
                                              JOptionPane.PLAIN_MESSAGE);
    if (name != null) {
      Fmz fmz = new Fmz();
      fmz.setName(name.toLowerCase());
      currentZone.addFmz(fmz);
      update();
    }
  }

  private void deleteZone(ActionEvent e) {
    DeleteFmzDialog dlg = new DeleteFmzDialog(JSimpplle.getSimpplleMain(),"Delete Zone",true);
    dlg.setVisible(true);
    update();
  }

  private void deleteAllZones(ActionEvent e) {
    int choice = JOptionPane.showConfirmDialog(this,
                                               "This action will delete all non-default fire\n" +
                                               "management zones. If an area is loaded, then\n" +
                                               "all units will be assigned the default zone.\n\n" +
                                               "Do you wish to continue?",
                                               "Delete current FMZ.",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      currentZone.removeAllFmz();
      update();
    }
  }

  private void displayKnowledgeSource(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.FMZ);
    String title = "Fire Occurrence Input Knowledge Source";
    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }
}

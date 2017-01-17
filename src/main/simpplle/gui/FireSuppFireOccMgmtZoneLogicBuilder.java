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
  private JPanel outerPanel;
  private JPanel mainPanel;

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

    // File Menu

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

    // Action menu

    menuActionCreate = new JMenuItem("Create New Zone");
    menuActionCreate.addActionListener(this::createNewZone);

    menuActionDelete = new JMenuItem("Delete Fmz");
    menuActionDelete.setActionCommand("Select an Fmz to delete");
    menuActionDelete.addActionListener(this::deleteZone);
    menuActionDelete.setEnabled(false);

    menuActionDeleteAll = new JMenuItem("Delete All Zones");
    menuActionDeleteAll.addActionListener(this::deleteAllZones);

    JMenu menuAction = new JMenu("Actions");
    menuAction.add(menuActionCreate);
    menuAction.add(menuActionDelete);
    menuAction.add(menuActionDeleteAll);

    // Knowledge Source Menu

    menuKnowledgeSourceDisplay = new JMenuItem("Display");
    menuKnowledgeSourceDisplay.addActionListener(this::displayKnowledgeSource);

    JMenu menuKnowledgeSource = new JMenu("Knowledge Source");
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);

    // Header Panel

    JLabel zoneLabel = new JLabel(Formatting.fixedField("Zone", 0, true));
    zoneLabel.setHorizontalTextPosition(SwingConstants.LEFT);
    zoneLabel.setFont(new java.awt.Font("Monospaced", 0, 12));

    JLabel acreLabel = new JLabel(Formatting.fixedField("Acres", 6));
    acreLabel.setFont(new java.awt.Font("Monospaced", 0, 12));

    JLabel fireTotalLabel = new JLabel(Formatting.fixedField("Starts per 10 years", 5));
    fireTotalLabel.setHorizontalTextPosition(SwingConstants.LEADING);
    fireTotalLabel.setFont(new java.awt.Font("Monospaced", 0, 12));

    JLabel responseLabel = new JLabel(Formatting.fixedField("Response time (hours)", 0, true));
    responseLabel.setFont(new java.awt.Font("Monospaced", 0, 12));

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 60, 0));
    headerPanel.add(zoneLabel);
    headerPanel.add(acreLabel);
    headerPanel.add(fireTotalLabel);
    headerPanel.add(responseLabel);

    // Menu Bar

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(menuFile);
    menuBar.add(menuAction);
    menuBar.add(menuKnowledgeSource);
    setJMenuBar(menuBar);

    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

    JScrollPane scrollBar = new JScrollPane(mainPanel);
    scrollBar.setPreferredSize(new Dimension(600, 600));
    scrollBar.setColumnHeaderView(headerPanel);

    innerPanel = new JPanel(new BorderLayout());
    innerPanel.add(scrollBar, BorderLayout.CENTER);

    outerPanel = new JPanel(new BorderLayout());
    outerPanel.add(innerPanel, BorderLayout.CENTER);

    add(outerPanel);
  }

  private void initialize() {

    currentZone = Simpplle.getCurrentZone();

    allFmz = currentZone.getAllFmzNames();
    if (allFmz == null || allFmz.size() == 0) {
      Fmz.makeDefault();
      allFmz = currentZone.getAllFmzNames();
    }

    for (Fmz item : currentZone.getAllFmz()) {
      mainPanel.add(new FmzPanel(item));
    }

    Fmz[] fmzArray = currentZone.getAllFmz();
    menuActionDelete.setEnabled(fmzArray.length > 1);

  }

  private void refresh() {
    update(getGraphics());
  }

  private void update() {

    mainPanel.removeAll();
    for (Fmz item : currentZone.getAllFmz()) {
      mainPanel.add(new FmzPanel(item));
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
    File outfile;
    boolean changed;
    Area area;
    MyFileFilter extFilter;
    String title = "Select a Fire Management Zone Data File";

    extFilter = new MyFileFilter("fmz", "FMZ Data Files (*.fmz)");

    setCursor(Utility.getWaitCursor());

    outfile = Utility.getOpenFile(this, title, extFilter);
    open:
    try {
      if (outfile == null) {
        break open;
      }

      Fmz.loadData(outfile);

      allFmz = currentZone.getAllFmzNames();
//      updateDialog();

      menuFileSave.setEnabled(true);

      area = Simpplle.getCurrentArea();
      if (area == null) {
        break open;
      }

      changed = area.updateFmzData();
      if (changed) {
        String msg = "Fmz's in the currently loaded area that\n" +
            "referred to fmz's not currently loaded\n" +
            "were changed to the default fmz.\n\n" +
            "If this is not desired load the correct\n" +
            "fmz data file for the area and then\n" +
            "reload the current area.";
        JOptionPane.showMessageDialog(this, msg, "Warning",
            JOptionPane.WARNING_MESSAGE);
      }
    } catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this, err.getError(), "Error loading file",
          JOptionPane.ERROR_MESSAGE);
    } finally {
      setCursor(Utility.getNormalCursor());
      refresh();
    }
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

  // Action menu
  private void createNewZone(ActionEvent e) {
    String msg = "Fire Management Zone Name";
    String title = "Create new Fire Management Zone";
    String name = JOptionPane.showInputDialog(this, msg, title,
        JOptionPane.PLAIN_MESSAGE);
    if (name == null) {
      return;
    }

    name = name.toLowerCase();
    Fmz newFmz = new Fmz();
    newFmz.setName(name);

    currentZone.addFmz(newFmz);
    update();
  }

  private void deleteZone(ActionEvent e) {
    DeleteFmzDialog dlg = new DeleteFmzDialog(JSimpplle.getSimpplleMain(),"Delete Fmz",true);
    dlg.setVisible(true);
    update();
  }

  private void deleteAllZones(ActionEvent e) {
    String msg;
    int    choice;

    msg = "This action will delete all Fire Management Zones,\n" +
        "except the default zone.\n" +
        "** If an area is loaded all units will be set to\n" +
        "default FMZ.\n\n" +
        "Are you sure?";
    choice = JOptionPane.showConfirmDialog(this,msg,"Delete current FMZ.",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      currentZone.removeAllFmz();
      update();
    }
  }

  // Knowledge source
  private void displayKnowledgeSource(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.FMZ);
    String title = "Fire Occurrence Input Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }
}

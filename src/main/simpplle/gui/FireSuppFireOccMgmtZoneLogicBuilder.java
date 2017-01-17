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
  private JPanel outterPanel;
  private JPanel mainPanel;

  private JMenuItem menuActionCreate;
  private JMenuItem menuActionDelete;
  private JMenuItem menuActionDeleteAll;
  private JMenuItem menuFileClose;
  private JMenuItem menuFileDefault;
  private JMenuItem menuFileImportOldFile;
  private JMenuItem menuFileOpen;
  private JMenuItem menuFileQuit;
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
    menuFileOpen.addActionListener(this::menuFileOpen_actionPerformed);

    menuFileClose = new JMenuItem("Close");
    menuFileClose.setEnabled(false);
    menuFileClose.addActionListener(this::menuFileClose_actionPerformed);

    menuFileImportOldFile = new JMenuItem("Import Old Format File");
    menuFileImportOldFile.addActionListener(this::menuImportOldFile_actionPerformed);

    menuFileDefault = new JMenuItem("Load Default Data");
    menuFileDefault.setToolTipText("Load default fmz data file for the current zone");
    menuFileDefault.addActionListener(this::menuFileDefault_actionPerformed);

    menuFileSave = new JMenuItem("Save");
    menuFileSave.setEnabled(false);
    menuFileSave.addActionListener(this::menuFileSave_actionPerformed);

    menuFileSaveAs = new JMenuItem("Save As");
    menuFileSaveAs.addActionListener(this::menuFileSaveAs_actionPerformed);

    menuFileQuit = new JMenuItem("Close Dialog");
    menuFileQuit.addActionListener(this::menuFileQuit_actionPerformed);

    JMenu menuFile = new JMenu();
    menuFile.setText("File");
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuFileImportOldFile);
    menuFile.addSeparator();
    menuFile.add(menuFileDefault);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);

    // Action menu

    menuActionCreate = new JMenuItem("Create New Zone");
    menuActionCreate.addActionListener(this::menuActionCreate_actionPerformed);

    menuActionDelete = new JMenuItem("Delete Fmz");
    menuActionDelete.setEnabled(false);
    menuActionDelete.setActionCommand("Select an Fmz to delete");
    menuActionDelete.addActionListener(this::menuActionDelete_actionPerformed);

    menuActionDeleteAll = new JMenuItem("Delete All Zones");
    menuActionDeleteAll.addActionListener(this::menuActionDeleteAll_actionPerformed);

    JMenu menuAction = new JMenu("Actions");
    menuAction.add(menuActionCreate);
    menuAction.add(menuActionDelete);
    menuAction.add(menuActionDeleteAll);

    // Knowledge Source Menu

    menuKnowledgeSourceDisplay = new JMenuItem("Display");
    menuKnowledgeSourceDisplay.addActionListener(this::menuKnowledgeSourceDisplay_actionPerformed);

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

    outterPanel = new JPanel(new BorderLayout());
    outterPanel.add(innerPanel, BorderLayout.CENTER);

    add(outterPanel);
  }

  private void initialize() {
    currentZone = Simpplle.getCurrentZone();
    allFmz = currentZone.getAllFmzNames();

    if (allFmz == null || allFmz.size() == 0) {
      Fmz.makeDefault();
      allFmz = currentZone.getAllFmzNames();
    }
    Fmz[] fmzArray = currentZone.getAllFmz();
    drawInfoPanels();
    menuActionDelete.setEnabled((fmzArray.length != 1));
  }

  private void refresh() {
    update(getGraphics());
  }

  private boolean continueDespiteLoadedArea() {
    Area area = Simpplle.getCurrentArea();

    if (area != null) {
      String msg;
      int choice;
      msg = "Any unit assigned an FMZ that is not present in\n" +
          "the new file will be assigned the default FMZ.\n\n" +
          "Are you sure?";
      choice = JOptionPane.showConfirmDialog(this, msg, "Area Currently Loaded, Proceed?.",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);

      if (choice != JOptionPane.YES_OPTION) {
        return false;
      }
    }
    return true;
  }

  private void drawInfoPanels() {
    for (Fmz item : currentZone.getAllFmz()) {
      mainPanel.add(new FmzPanel(item));
    }
  }

  private void Update() {
    Fmz[] fmzArray = currentZone.getAllFmz();

    outterPanel.remove(innerPanel);
    mainPanel.removeAll();

    // Redraw
    drawInfoPanels();

    // Add everything back to window
    outterPanel.add(innerPanel, BorderLayout.CENTER);
    add(outterPanel);

    // Checks weather only default is present
    menuActionDelete.setEnabled((fmzArray.length != 1)); // nope

    File filename = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    menuFileClose.setEnabled((filename != null));
    menuFileSave.setEnabled((filename != null));

    repaint();
    revalidate();
  }

  private void loadDefaultDataFile() { // Loads default FMZs for the current zone
    try {
      Area area = Simpplle.getCurrentArea();
      if (area != null && !continueDespiteLoadedArea()) {
        return;
      }
      SystemKnowledge.loadZoneKnowledge(SystemKnowledge.FMZ);

      // Make sure EVU's who point to this fmz no longer present
      // are reset to the default fmz.
      if (area != null) {
        area.updateFmzData();
      }
      Update();
    } catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  // File Menu
  private void menuFileOpen_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();
    if (area != null && !continueDespiteLoadedArea()) {
      return;
    }

    SystemKnowledgeFiler.openFile(this, SystemKnowledge.FMZ, menuFileSave, menuFileClose);

    allFmz = currentZone.getAllFmzNames();

    // Make sure EVU's who point to this fmz no longer present
    // are reset to the default fmz.
    if (area != null) {
      area.updateFmzData();
    }
    Update();
  }

  private void menuFileClose_actionPerformed(ActionEvent e) {
    int    choice;
    String msg;

    File filename = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    if (filename != null && Fmz.hasChanged()) {
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
    Fmz.closeFile();
    loadDefaultDataFile();
  }

  private void menuImportOldFile_actionPerformed(ActionEvent e) {
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
      menuFileClose.setEnabled(true);

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

  private void menuFileDefault_actionPerformed(ActionEvent e) {
    loadDefaultDataFile();
  }

  private void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    SystemKnowledgeFiler.saveFile(this, outfile, SystemKnowledge.FMZ,
        menuFileSave, menuFileClose);

    refresh();
  }

  private void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.FMZ, menuFileSave,
        menuFileClose);

    refresh();
  }

  private void menuFileQuit_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  // Action menu
  private void menuActionCreate_actionPerformed(ActionEvent e) {
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
    Update();
  }

  private void menuActionDelete_actionPerformed(ActionEvent e) {
    DeleteFmzDialog dlg = new DeleteFmzDialog(JSimpplle.getSimpplleMain(),"Delete Fmz",true);
    dlg.setVisible(true);
    Update();
  }

  private void menuActionDeleteAll_actionPerformed(ActionEvent e) {
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
      Update();
    }
  }

  // Knowledge source
  private void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.FMZ);
    String title = "Fire Occurrence Input Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }
}

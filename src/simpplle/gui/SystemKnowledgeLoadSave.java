/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.LtaValleySegmentGroup;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.SystemKnowledge;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;

/** 
 * SystemKnowledgeLoadSave is a dialog for selecting the kinds of knowledge to load or save.
 */

public class SystemKnowledgeLoadSave extends JDialog {

  /**
   * A flag indicating if the user cancelled the dialog.
   */
  private boolean dialogCanceled;

  /**
   * The file that will be loaded from or saved to.
   */
  private File loadSaveFile;

  /**
   * A flag indicating if this is a save dialog.
   */
  private boolean save;

  /**
   * Maps a kind of knowledge to a checkbox.
   */
  private HashMap<SystemKnowledge.Kinds,JCheckBox> checkBoxes = new HashMap<>();

  private JScrollPane CBScroll;
  private JTextField outputFileText;
  private JButton cancelPB;
  private JButton loadSavePB;
  private JButton pickFilePB;
  private JButton selectAllPB;
  private JButton selectNonePB;
  private JCheckBox aquaticPathwaysCB;
  private JCheckBox climateCB;
  private JCheckBox lifeformCompLogicCB;
  private JCheckBox coniferEncroachLogicCB;
  private JCheckBox extremeFireDataCB;
  private JCheckBox fireSeasonCB;
  private JCheckBox fireSpreadCB;
  private JCheckBox fireSpottingCB;
  private JCheckBox fireSuppBeyondClassACB;
  private JCheckBox fireSuppClassACB;
  private JCheckBox fireSuppEventProb;
  private JCheckBox fireSuppProductionRateCB;
  private JCheckBox fireSuppSpreadRateCB;
  private JCheckBox fireSuppWeatherBeyondClassACB;
  private JCheckBox fireSuppWeatherClassACB;
  private JCheckBox fireTypeLogicCB;
  private JCheckBox fmzCB;
  private JCheckBox gapProcessLogicCB;
  private JCheckBox insectDiseaseProbCB;
  private JCheckBox invasiveSpeciesLogicCB;
  private JCheckBox invasiveSpeciesLogicMsuCB;
  private JCheckBox processProbLogicCB;
  private JCheckBox processScheduleCB;
  private JCheckBox producingSeedLogicCB;
  private JCheckBox fireRegenLogicCB;
  private JCheckBox regenLogicSuccCB;
  private JCheckBox regenDelayLogicCB;
  private JCheckBox speciesCB;
  private JCheckBox treatmentLogicCB;
  private JCheckBox treatmentScheduleCB;
  private JCheckBox vegetationPathwaysCB;
  private JCheckBox vegUnitFireTypeLogicLogicCB;
  private JCheckBox keaneParametersCB;

  /**
   * Creates a new modal dialog
   *
   * @param frame The parent frame
   * @param title The title for this dialog
   * @param modal Flag indicating if this dialog will block input to the parent
   * @param save Flag indicating if this is a save dialog
   */
  public SystemKnowledgeLoadSave(Frame frame, String title, boolean modal, boolean save) {

    super(frame, title, modal);

    this.save = save;

    try {
      jbInit();
      initialize();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Populates the dialog with controls.
   *
   * @throws Exception
   */
  private void jbInit() throws Exception {

    /* Selection */

    selectAllPB = new JButton("Select All");
    selectAllPB.addActionListener(this::pressedSelectAll);

    selectNonePB = new JButton("Select None");
    selectNonePB.addActionListener(this::pressedSelectNone);

    JPanel selectPanel = new JPanel();
    selectPanel.add(selectAllPB, null);
    selectPanel.add(selectNonePB, null);

    /* Fire Knowledge */

    fireSuppClassACB = new JCheckBox("Class A Fire Suppression");
    fireSuppWeatherClassACB = new JCheckBox("Class A Fire Suppression due to Weather");
    extremeFireDataCB = new JCheckBox("Extreme Fire Settings");
    fmzCB = new JCheckBox("Fire Occurrence / Suppression Costs / Management Zones");
    fireSeasonCB = new JCheckBox("Fire Season");
    fireSpottingCB = new JCheckBox("Fire Spotting");
    fireSpreadCB = new JCheckBox("Fire Spread");
    fireSuppBeyondClassACB = new JCheckBox("Fire Suppression Beyond Class A");
    fireSuppWeatherBeyondClassACB = new JCheckBox("Fire Suppression Beyond Class A due to Weather");
    fireSuppEventProb = new JCheckBox("Fire Suppression Event Probability");
    fireSuppProductionRateCB = new JCheckBox("Fire Suppression Production Rate");
    fireSuppSpreadRateCB = new JCheckBox("Fire Suppression Spread Rate");
    speciesCB = new JCheckBox("Species Settings (e.g. Fire Resistance)");
    fireTypeLogicCB = new JCheckBox("Type of Fire Logic");
    keaneParametersCB = new JCheckBox("Keane Cell Percolation Parameters");

    Box fireKnowledgeColA = new Box(BoxLayout.Y_AXIS);
    fireKnowledgeColA.add(fireSuppClassACB, null);
    fireKnowledgeColA.add(fireSuppWeatherClassACB, null);
    fireKnowledgeColA.add(extremeFireDataCB, null);
    fireKnowledgeColA.add(fmzCB, null);
    fireKnowledgeColA.add(fireSeasonCB, null);
    fireKnowledgeColA.add(fireSpottingCB);
    fireKnowledgeColA.add(fireSpreadCB, null);
    fireKnowledgeColA.add(fireSuppBeyondClassACB, null);

    Box fireKnowledgeColB = new Box(BoxLayout.Y_AXIS);
    fireKnowledgeColB.add(fireSuppWeatherBeyondClassACB, null);
    fireKnowledgeColB.add(fireSuppEventProb, null);
    fireKnowledgeColB.add(fireSuppProductionRateCB, null);
    fireKnowledgeColB.add(fireSuppSpreadRateCB, null);
    fireKnowledgeColB.add(keaneParametersCB, null);
    fireKnowledgeColB.add(speciesCB, null);
    fireKnowledgeColB.add(fireTypeLogicCB, null);

    TitledBorder fireKnowledgeBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Fire Knowledge");

    JPanel fireKnowledgePanel = new JPanel();
    fireKnowledgePanel.setBorder(fireKnowledgeBorder);
    fireKnowledgePanel.setLayout(new BoxLayout(fireKnowledgePanel,BoxLayout.X_AXIS));
    fireKnowledgePanel.add(fireKnowledgeColA);
    fireKnowledgePanel.add(Box.createHorizontalGlue());
    fireKnowledgePanel.add(fireKnowledgeColB);

    /* Pathways */

    aquaticPathwaysCB = new JCheckBox("Aquatic Pathways");
    aquaticPathwaysCB.setEnabled(false);
    vegetationPathwaysCB = new JCheckBox("Vegetative Pathways");

    TitledBorder pathwaysBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Pathways");

    JPanel pathwaysPanel = new JPanel();
    pathwaysPanel.setBorder(pathwaysBorder);
    pathwaysPanel.setLayout(new GridLayout(2,0));
    pathwaysPanel.add(aquaticPathwaysCB, null);
    pathwaysPanel.add(vegetationPathwaysCB, null);

    /* Schedules */

    processScheduleCB = new JCheckBox("Process Schedules");
    treatmentScheduleCB = new JCheckBox("Treatment Schedules");

    TitledBorder schedulesBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Schedules");

    JPanel schedulesPanel = new JPanel();
    schedulesPanel.setBorder(schedulesBorder);
    schedulesPanel.setLayout(new GridLayout(2,0));
    schedulesPanel.add(processScheduleCB, null);
    schedulesPanel.add(treatmentScheduleCB, null);

    /* Miscellaneous */

    climateCB = new JCheckBox("Climate");
    coniferEncroachLogicCB = new JCheckBox("Conifer Encroachment Logic");
    fireRegenLogicCB = new JCheckBox("Fire Regeneration Logic");
    gapProcessLogicCB = new JCheckBox("Gap Process Logic");
    insectDiseaseProbCB = new JCheckBox("Insect/Disease Probabilities");
    invasiveSpeciesLogicCB = new JCheckBox("Invasive Species Logic");
    invasiveSpeciesLogicMsuCB = new JCheckBox("Invasive Species Logic MSU");
    lifeformCompLogicCB = new JCheckBox("Lifeform Competition Logic");
    processProbLogicCB = new JCheckBox("Process Probability Logic");
    producingSeedLogicCB = new JCheckBox("Producing Seed Logic");
    regenDelayLogicCB = new JCheckBox("Regeneration Delay Logic");
    regenLogicSuccCB = new JCheckBox("Succession Regeneration Logic");
    treatmentLogicCB = new JCheckBox("Treatment Logic");
    vegUnitFireTypeLogicLogicCB = new JCheckBox("Veg Unit Fire Type Logic");

    Box miscColA = new Box(BoxLayout.Y_AXIS);
    miscColA.add(climateCB, null);
    miscColA.add(coniferEncroachLogicCB, null);
    miscColA.add(fireRegenLogicCB, null);
    miscColA.add(gapProcessLogicCB);
    miscColA.add(insectDiseaseProbCB, null);

    Box miscColB = new Box(BoxLayout.Y_AXIS);
    miscColB.add(invasiveSpeciesLogicCB);
    miscColB.add(invasiveSpeciesLogicMsuCB);
    miscColB.add(lifeformCompLogicCB);
    miscColB.add(processProbLogicCB);
    miscColB.add(producingSeedLogicCB);

    Box miscColC = new Box(BoxLayout.Y_AXIS);
    miscColC.add(regenDelayLogicCB);
    miscColC.add(regenLogicSuccCB, null);
    miscColC.add(treatmentLogicCB, null);
    miscColC.add(vegUnitFireTypeLogicLogicCB);
    miscColC.add(Box.createVerticalGlue());

    TitledBorder miscBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Miscellaneous");

    JPanel miscPanel = new JPanel();
    miscPanel.setBorder(miscBorder);
    miscPanel.setLayout(new BoxLayout(miscPanel,BoxLayout.X_AXIS));
    miscPanel.add(miscColA);
    miscPanel.add(Box.createHorizontalGlue());
    miscPanel.add(miscColB);
    miscPanel.add(Box.createHorizontalGlue());
    miscPanel.add(miscColC);

    /* Output File */

    outputFileText = new JTextField();
    outputFileText.setBackground(Color.white);
    outputFileText.setEnabled(false);
    outputFileText.setDisabledTextColor(Color.black);
    outputFileText.setEditable(false);
    outputFileText.setColumns(30);

    JScrollPane fileScrollPane = new JScrollPane();
    fileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    fileScrollPane.getViewport().add(outputFileText, null);

    JPanel fileScrollPanel = new JPanel();
    fileScrollPanel.setLayout(new BorderLayout());
    fileScrollPanel.add(fileScrollPane, BorderLayout.NORTH);

    pickFilePB = new JButton("Pick");
    pickFilePB.addActionListener(this::pressedPick);

    TitledBorder pickFileBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "File Name");

    JPanel pickFilePanel = new JPanel();
    pickFilePanel.setBorder(pickFileBorder);
    pickFilePanel.setLayout(new BorderLayout());
    pickFilePanel.add(pickFilePB, BorderLayout.WEST);
    pickFilePanel.add(fileScrollPanel, BorderLayout.CENTER);

    loadSavePB = new JButton("Save");
    loadSavePB.addActionListener(this::pressedLoadSave);

    cancelPB = new JButton("Cancel");
    cancelPB.addActionListener(this::pressedCancel);

    JPanel loadSaveCancelPanel = new JPanel();
    loadSaveCancelPanel.add(loadSavePB, null);
    loadSaveCancelPanel.add(cancelPB, null);

    JPanel outputFilePanel = new JPanel();
    outputFilePanel.setLayout(new BorderLayout());
    outputFilePanel.add(pickFilePanel, BorderLayout.NORTH);
    outputFilePanel.add(loadSaveCancelPanel, BorderLayout.SOUTH);

    /* Main */

    JPanel CBMainPanel = new JPanel();
    CBMainPanel.setLayout(new BoxLayout(CBMainPanel, BoxLayout.Y_AXIS));
    CBMainPanel.add(fireKnowledgePanel, null);
    CBMainPanel.add(pathwaysPanel, null);
    CBMainPanel.add(schedulesPanel, null);
    CBMainPanel.add(miscPanel, null);
    CBMainPanel.add(selectPanel, null);

    CBScroll = new JScrollPane();
    CBScroll.getViewport().add(CBMainPanel, null);
    Dimension size = CBScroll.getPreferredSize();
    CBScroll.setPreferredSize(new Dimension(size.width + 25, 560));

    JPanel CBPanel = new JPanel();
    CBPanel.setLayout(new BorderLayout());
    CBPanel.add(CBScroll, BorderLayout.CENTER);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(CBPanel, BorderLayout.NORTH);
    mainPanel.add(outputFilePanel, BorderLayout.SOUTH);
    mainPanel.add(CBPanel, BorderLayout.CENTER);

    getContentPane().add(mainPanel);

  }

  /**
   * Initializes the controls in the dialog and populates the checkbox map.
   */
  private void initialize() {

    checkBoxes.put(SystemKnowledge.CONIFER_ENCROACHMENT,coniferEncroachLogicCB);
    checkBoxes.put(SystemKnowledge.SPECIES,speciesCB);
    checkBoxes.put(SystemKnowledge.AQUATIC_PATHWAYS,aquaticPathwaysCB);
    checkBoxes.put(SystemKnowledge.VEGETATION_PATHWAYS,vegetationPathwaysCB);
    checkBoxes.put(SystemKnowledge.TREATMENT_LOGIC,treatmentLogicCB);
    checkBoxes.put(SystemKnowledge.TREATMENT_SCHEDULE,treatmentScheduleCB);
    checkBoxes.put(SystemKnowledge.PROCESS_SCHEDULE,processScheduleCB);
    checkBoxes.put(SystemKnowledge.INSECT_DISEASE_PROB,insectDiseaseProbCB);
    checkBoxes.put(SystemKnowledge.PROCESS_PROB_LOGIC,processProbLogicCB);
    checkBoxes.put(SystemKnowledge.CLIMATE,climateCB);
    checkBoxes.put(SystemKnowledge.REGEN_LOGIC_FIRE, fireRegenLogicCB);
    checkBoxes.put(SystemKnowledge.REGEN_LOGIC_SUCC,regenLogicSuccCB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_SPREAD_RATE_LOGIC,fireSuppSpreadRateCB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_PRODUCTION_RATE_LOGIC,fireSuppProductionRateCB);
    checkBoxes.put(SystemKnowledge.FIRE_SEASON,fireSeasonCB);
    checkBoxes.put(SystemKnowledge.EXTREME_FIRE_DATA, extremeFireDataCB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,fireSuppWeatherBeyondClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_WEATHER_CLASS_A_LOGIC,fireSuppWeatherClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A_LOGIC,fireSuppBeyondClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_CLASS_A_LOGIC,fireSuppClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SPREAD_LOGIC,fireSpreadCB);
    checkBoxes.put(SystemKnowledge.FMZ,fmzCB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_EVENT_LOGIC,fireSuppEventProb);
    checkBoxes.put(SystemKnowledge.FIRE_TYPE_LOGIC,fireTypeLogicCB);
    checkBoxes.put(SystemKnowledge.FIRE_SPOTTING_LOGIC,fireSpottingCB);
    checkBoxes.put(SystemKnowledge.REGEN_DELAY_LOGIC,regenDelayLogicCB);
    checkBoxes.put(SystemKnowledge.GAP_PROCESS_LOGIC,gapProcessLogicCB);
    checkBoxes.put(SystemKnowledge.DOCOMPETITION_LOGIC, lifeformCompLogicCB);
    checkBoxes.put(SystemKnowledge.INVASIVE_SPECIES_LOGIC,invasiveSpeciesLogicCB);
    checkBoxes.put(SystemKnowledge.INVASIVE_SPECIES_LOGIC_MSU, invasiveSpeciesLogicMsuCB);
    checkBoxes.put(SystemKnowledge.PRODUCING_SEED_LOGIC,producingSeedLogicCB);
    checkBoxes.put(SystemKnowledge.VEG_UNIT_FIRE_TYPE_LOGIC,vegUnitFireTypeLogicLogicCB);
    checkBoxes.put(SystemKnowledge.KEANE_PARAMETERS, keaneParametersCB);

    if (save) {

      loadSavePB.setText("Save");
      loadSavePB.setEnabled(false);
      initCheckBoxes();

    } else {

      loadSavePB.setText("Load");

    }

    checkBoxes.get(SystemKnowledge.AQUATIC_PATHWAYS).setEnabled(LtaValleySegmentGroup.loaded());

//    checkBoxes.get(SystemKnowledge.FIRE_SEASON).setEnabled(
//       (Simpplle.getCurrentZone().getId() == ValidZones.SOUTH_CENTRAL_ALASKA));
//
//    checkBoxes.get(SystemKnowledge.PROCESS_PROB_LOGIC).setEnabled(
//       (Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_FRONT_RANGE) ||
//       (Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_PLATEAU));

    setSize(getPreferredSize());
    update(getGraphics());

  }

  /**
   * Displays a file selection dialog and initializes the checkboxes.
   *
   * @return The selected file, or null
   */
  public File selectFile() {
    pressedPick(null);
    return loadSaveFile;
  }

  /**
   * Returns true if the user cancelled the dialog.
   *
   * @return True if the dialog was cancelled
   */
  public boolean isDialogCanceled() {
    return dialogCanceled;
  }

  /**
   * Initializes the state of the checkboxes. If this is a save dialog, checkboxes corresponding
   * to changed or user data are checked and enabled. If this is a load dialog, checkboxes
   * corresponding to data present in the file are checked and enabled.
   */
  private void initCheckBoxes() {
    for (SystemKnowledge.Kinds kind : checkBoxes.keySet()) {
      JCheckBox checkbox = checkBoxes.get(kind);
      if (checkbox != null) {
        if ((kind == SystemKnowledge.PROCESS_SCHEDULE ||
             kind == SystemKnowledge.TREATMENT_SCHEDULE) &&
             Simpplle.getCurrentArea() == null) {
          checkbox.setSelected(false);
          checkbox.setEnabled(false);
        } else {
          if (save) {
            boolean option = SystemKnowledge.hasChangedOrUserData(kind);
            checkbox.setSelected(option);
            checkbox.setEnabled(true);
          } else {
            boolean option = SystemKnowledge.isPresentInFile(kind);
            checkbox.setSelected(option);
            checkbox.setEnabled(option);
          }
        }
      }
    }
  }

  private void pressedPick(ActionEvent e) {

    MyFileFilter extFilter = new MyFileFilter("sysknowledge", "Sys Knowledge File (*.sysknowledge)");

    File filename;
    if (save) {
      filename = Utility.getSaveFile(JSimpplle.getSimpplleMain(), "System Knowledge File", extFilter);
    } else {
      filename = Utility.getOpenFile(JSimpplle.getSimpplleMain(), "System Knowledge File", extFilter);
    }
    if (filename == null) return;

    outputFileText.setText(filename.toString());
    loadSavePB.setEnabled(true);
    loadSaveFile = filename;

    try {
      if (!save) {
        SystemKnowledge.processInputFileEntries(loadSaveFile);
        initCheckBoxes();
      }
    } catch (SimpplleError ex) {

    }
  }

  private void pressedLoadSave(ActionEvent e) {

    boolean noSelection = true;
    for (JCheckBox cb : checkBoxes.values()) {
      if (cb != null && cb.isSelected()) {
        noSelection = false;
        break;
      }
    }

    if (save && noSelection) {
      JOptionPane.showMessageDialog(this,
                                    "Nothing is selected.",
                                    "",
                                    JOptionPane.WARNING_MESSAGE);
      return;
    }

    for (SystemKnowledge.Kinds kind : checkBoxes.keySet()) {
      JCheckBox checkbox = checkBoxes.get(kind);
      if (checkbox != null) {
        SystemKnowledge.setLoadSaveOption(kind, checkbox.isSelected());
      }
    }
    SystemKnowledge.setLoadSaveOption(SystemKnowledge.TRACKING_SPECIES_REPORT,false);
    SystemKnowledge.setLoadSaveOption(SystemKnowledge.EVU_SEARCH_LOGIC,false);

    try {

      if (save) {
        SystemKnowledge.saveInputFile(loadSaveFile);
        JOptionPane.showMessageDialog(this,
                                      "File saved successfully.",
                                      "",
                                      JOptionPane.INFORMATION_MESSAGE);
      } else {
        SystemKnowledge.readInputFile(loadSaveFile);
        JOptionPane.showMessageDialog(this,
                                      "File loaded successfully.",
                                      "",
                                      JOptionPane.INFORMATION_MESSAGE);
      }

      dialogCanceled = false;
      setVisible(false);
      dispose();

    } catch (SimpplleError ex) {

      JOptionPane.showMessageDialog(this,
                                    ex.getMessage(),
                                    "Problems processing file",
                                    JOptionPane.ERROR_MESSAGE);

    }
  }

  private void pressedCancel(ActionEvent e) {
    dialogCanceled = true;
    setVisible(false);
    dispose();
  }

  private void pressedSelectAll(ActionEvent e) {
    for (JCheckBox cb : checkBoxes.values()) {
      cb.setSelected(cb.isEnabled());
    }
  }

  private void pressedSelectNone(ActionEvent e) {
    for (JCheckBox cb : checkBoxes.values()) {
      cb.setSelected(false);
    }
  }

}

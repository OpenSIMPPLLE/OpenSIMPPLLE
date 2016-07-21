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
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
@SuppressWarnings("serial")
public class SystemKnowledgeLoadSave extends JDialog {

  private boolean dialogCanceled;
  private File loadSaveFile;
  private boolean save;

  private HashMap<SystemKnowledge.Kinds,JCheckBox> checkBoxes = new HashMap<SystemKnowledge.Kinds,JCheckBox>();

  private JButton cancelPB = new JButton();
  private JButton loadSavePB = new JButton();
  private JButton pickFilePB = new JButton();
  private JButton selectAllPB = new JButton();
  private JButton selectNonePB = new JButton();
  private JCheckBox aquaticPathwaysCB = new JCheckBox();
  private JCheckBox climateCB = new JCheckBox();
  private JCheckBox lifeformCompLogicCB = new JCheckBox();
  private JCheckBox coniferEncroachLogicCB = new JCheckBox();
  private JCheckBox extremeFireDataCB = new JCheckBox();
  private JCheckBox fireSeasonCB = new JCheckBox();
  private JCheckBox fireSpreadCB = new JCheckBox();
  private JCheckBox fireSpottingCB = new JCheckBox();
  private JCheckBox fireSuppBeyondClassACB = new JCheckBox();
  private JCheckBox fireSuppClassACB = new JCheckBox();
  private JCheckBox fireSuppEventProb = new JCheckBox();
  private JCheckBox fireSuppProductionRateCB = new JCheckBox();
  private JCheckBox fireSuppSpreadRateCB = new JCheckBox();
  private JCheckBox fireSuppWeatherBeyondClassACB = new JCheckBox();
  private JCheckBox fireSuppWeatherClassACB = new JCheckBox();
  private JCheckBox fireTypeLogicCB = new JCheckBox();
  private JCheckBox fmzCB = new JCheckBox();
  private JCheckBox gapProcessLogicCB = new JCheckBox();
  private JCheckBox insectDiseaseProbCB = new JCheckBox();
  private JCheckBox invasiveSpeciesLogicCB = new JCheckBox();
  private JCheckBox invasiveSpeciesLogicMsuCB = new JCheckBox();
  private JCheckBox processProbLogicCB = new JCheckBox();
  private JCheckBox processScheduleCB = new JCheckBox();
  private JCheckBox producingSeedLogicCB = new JCheckBox();
  private JCheckBox fireRegenLogicCB = new JCheckBox();
  private JCheckBox regenLogicSuccCB = new JCheckBox();
  private JCheckBox regenDelayLogicCB = new JCheckBox();
  private JCheckBox speciesCB = new JCheckBox();
  private JCheckBox treatmentLogicCB = new JCheckBox();
  private JCheckBox treatmentScheduleCB = new JCheckBox();
  private JCheckBox vegetationPathwaysCB = new JCheckBox();
  private JCheckBox vegUnitFireTypeLogicLogicCB = new JCheckBox();
  private JScrollPane CBScroll = new JScrollPane();
  private JTextField outputFileText = new JTextField();
  private TitledBorder mainTitleBorder;

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

  private void jbInit() throws Exception {

    /* Selection */

    selectAllPB.setText("Select All");
    selectAllPB.addActionListener(this::selectAllPB_actionPerformed);
    selectNonePB.setText("Select None");
    selectNonePB.addActionListener(this::selectNonePB_actionPerformed);

    JPanel selectPanel = new JPanel();
    selectPanel.add(selectAllPB, null);
    selectPanel.add(selectNonePB, null);

    /* Fire Knowledge */

    fireSuppClassACB.setText("Class A Fire Suppression");
    fireSuppWeatherClassACB.setText("Class A Fire Suppression due to Weather");
    extremeFireDataCB.setText("Extreme Fire Settings");
    fmzCB.setText("Fire Occurrence / Suppression Costs / Management Zones");
    fireSeasonCB.setText("Fire Season");
    fireSpottingCB.setText("Fire Spotting");
    fireSpreadCB.setText("Fire Spread");
    fireSuppBeyondClassACB.setText("Fire Suppression Beyond Class A");
    fireSuppWeatherBeyondClassACB.setText("Fire Suppression Beyond Class A due to Weather");
    fireSuppEventProb.setText("Fire Suppression Event Probability");
    fireSuppProductionRateCB.setText("Fire Suppression Production Rate");
    fireSuppSpreadRateCB.setText("Fire Suppression Spread Rate");
    speciesCB.setText("Species Settings (e.g. Fire Resistance)");
    fireTypeLogicCB.setText("Type of Fire Logic");
    fireTypeLogicCB.setActionCommand("Type of Fire Logic");

    Box fireKnowledgeColA = new Box(BoxLayout.Y_AXIS);
    fireKnowledgeColA.add(fireSuppClassACB, null);
    fireKnowledgeColA.add(fireSuppWeatherClassACB, null);
    fireKnowledgeColA.add(extremeFireDataCB, null);
    fireKnowledgeColA.add(fmzCB, null);
    fireKnowledgeColA.add(fireSeasonCB, null);
    fireKnowledgeColA.add(fireSpottingCB);
    fireKnowledgeColA.add(fireSpreadCB, null);

    Box fireKnowledgeColB = new Box(BoxLayout.Y_AXIS);
    fireKnowledgeColB.add(fireSuppBeyondClassACB, null);
    fireKnowledgeColB.add(fireSuppWeatherBeyondClassACB, null);
    fireKnowledgeColB.add(fireSuppEventProb, null);
    fireKnowledgeColB.add(fireSuppProductionRateCB, null);
    fireKnowledgeColB.add(fireSuppSpreadRateCB, null);
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

    aquaticPathwaysCB.setText("Aquatic Pathways");
    aquaticPathwaysCB.setEnabled(false);
    vegetationPathwaysCB.setText("Vegetative Pathways");

    TitledBorder pathwaysBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Pathways");

    JPanel pathwaysPanel = new JPanel();
    pathwaysPanel.setBorder(pathwaysBorder);
    pathwaysPanel.setLayout(new GridLayout(2,0));
    pathwaysPanel.add(aquaticPathwaysCB, null);
    pathwaysPanel.add(vegetationPathwaysCB, null);

    /* Schedules */

    processScheduleCB.setText("Process Schedules");
    treatmentScheduleCB.setText("Treatment Schedules");

    TitledBorder schedulesBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Schedules");

    JPanel schedulesPanel = new JPanel();
    schedulesPanel.setBorder(schedulesBorder);
    schedulesPanel.setLayout(new GridLayout(2,0));
    schedulesPanel.add(processScheduleCB, null);
    schedulesPanel.add(treatmentScheduleCB, null);

    /* Miscellaneous */

    climateCB.setText("Climate");
    coniferEncroachLogicCB.setText("Conifer Encroachment Logic");
    fireRegenLogicCB.setText("Fire Regeneration Logic");
    gapProcessLogicCB.setText("Gap Process Logic");
    insectDiseaseProbCB.setText("Insect/Disease Probabilities");
    invasiveSpeciesLogicCB.setText("Invasive Species Logic");
    invasiveSpeciesLogicMsuCB.setText("Invasive Species Logic MSU");
    lifeformCompLogicCB.setText("Lifeform Competition Logic");
    processProbLogicCB.setText("Process Probability Logic");
    producingSeedLogicCB.setText("Producing Seed Logic");
    regenDelayLogicCB.setText("Regeneration Delay Logic");
    regenLogicSuccCB.setText("Succession Regeneration Logic");
    treatmentLogicCB.setText("Treatment Logic");
    vegUnitFireTypeLogicLogicCB.setText("Veg Unit Fire Type Logic");

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

    pickFilePB.setText("Pick");
    pickFilePB.addActionListener(this::pickFilePB_actionPerformed);

    TitledBorder pickFileBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "File Name");

    JPanel pickFilePanel = new JPanel();
    pickFilePanel.setBorder(pickFileBorder);
    pickFilePanel.setLayout(new BorderLayout());
    pickFilePanel.add(pickFilePB, BorderLayout.WEST);
    pickFilePanel.add(fileScrollPanel, BorderLayout.CENTER);

    loadSavePB.setText("Save");
    loadSavePB.addActionListener(this::loadSavePB_actionPerformed);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(this::cancelPB_actionPerformed);

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

    CBScroll.getViewport().add(CBMainPanel, null);

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

  private void initialize() {

    Dimension size = CBScroll.getPreferredSize();
    CBScroll.setPreferredSize(new Dimension(size.width+25,560));

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

  private void setLoadSaveOptions() {
    for (SystemKnowledge.Kinds kind : checkBoxes.keySet()) {
      JCheckBox cb = checkBoxes.get(kind);
      if (cb != null) {
        SystemKnowledge.setLoadSaveOption(kind, cb.isSelected());
      }
    }
    SystemKnowledge.setLoadSaveOption(SystemKnowledge.TRACKING_SPECIES_REPORT,false);
    SystemKnowledge.setLoadSaveOption(SystemKnowledge.EVU_SEARCH_LOGIC,false);
  }

  private void initCheckBoxes() {
    boolean option;
    for (SystemKnowledge.Kinds kind : checkBoxes.keySet()) {
      JCheckBox cb = checkBoxes.get(kind);
      if (save) {
        option = SystemKnowledge.hasChangedOrUserData(kind);
      }
      else {
        option = SystemKnowledge.isPresentInFile(kind);
      }
      if (cb != null) {
        if ((kind == SystemKnowledge.PROCESS_SCHEDULE ||
             kind == SystemKnowledge.TREATMENT_SCHEDULE) &&
             Simpplle.getCurrentArea() == null) {
          cb.setSelected(false);
          cb.setEnabled(false);
        }
        else {
          cb.setSelected(option);
          cb.setEnabled(save || (!save && option));
        }
      }
    }
  }

  private boolean isAnythingSelected() {
    for (JCheckBox cb : checkBoxes.values()) {
      if (cb != null && cb.isSelected()) { return true; }
    }
    return false;
  }

  public File getAndSetInputFile() {
    pickFilePB_actionPerformed(null);
    return loadSaveFile;
  }

  void pickFilePB_actionPerformed(ActionEvent e) {
    File         filename;
    MyFileFilter extFilter;

    extFilter = new MyFileFilter("sysknowledge",
                                 "Sys Knowledge File (*.sysknowledge)");

    if (save) {
      filename = Utility.getSaveFile(JSimpplle.getSimpplleMain(),
                                "System Knowledge File", extFilter);
    } else {
      filename = Utility.getOpenFile(JSimpplle.getSimpplleMain(),
                                "System Knowledge File", extFilter);
    }

    if (filename == null) { return; }

    outputFileText.setText(filename.toString());
    loadSavePB.setEnabled(true);
    loadSaveFile = filename;
    try {
      if (!save) {
        SystemKnowledge.processInputFileEntries(loadSaveFile);
        initCheckBoxes();
      }
    } catch (SimpplleError ex) {}
  }

  void loadSavePB_actionPerformed(ActionEvent e) {
    if (save && (isAnythingSelected() == false)) {
      JOptionPane.showMessageDialog(this, "No items Checked!",
                                    "Nothing Selected",
                                    JOptionPane.WARNING_MESSAGE);
      return;
    }

    setLoadSaveOptions();

    try {

      if (save) {
        SystemKnowledge.saveInputFile(loadSaveFile);
      } else {
        SystemKnowledge.readInputFile(loadSaveFile);
      }

      String msg = (save) ? "File Saved Successfully" :
                            "File Loaded Successfully";
      JOptionPane.showMessageDialog(this, msg, "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
      dialogCanceled = false;
      setVisible(false);
      dispose();

    } catch (SimpplleError ex) {

      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Problems processing file",
                                    JOptionPane.ERROR_MESSAGE);

    }
  }

  private void quit() {
    dialogCanceled = false;
    setVisible(false);
    dispose();
  }

  void cancelPB_actionPerformed(ActionEvent e) {
    dialogCanceled = true;
    setVisible(false);
    dispose();
  }

  void selectAllPB_actionPerformed(ActionEvent e) {
    for (JCheckBox cb : checkBoxes.values()) {
      cb.setSelected(cb.isEnabled());
    }
  }

  void selectNonePB_actionPerformed(ActionEvent e) {
    for (JCheckBox cb : checkBoxes.values()) {
      cb.setSelected(false);
    }
  }

  public boolean isDialogCanceled() {
    return dialogCanceled;
  }
}

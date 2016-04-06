package simpplle.gui;

import com.borland.jbcl.layout.VerticalFlowLayout;
import simpplle.JSimpplle;
import simpplle.comcode.LtaValleySegmentGroup;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.SystemKnowledge;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;


/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
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
  private JCheckBox competitionLogicCB = new JCheckBox();
  private JCheckBox coniferEncroachLogicCB = new JCheckBox();
  private JCheckBox exteremeFireDataCB = new JCheckBox();
  private JCheckBox fireSeasonCB = new JCheckBox();
  private JCheckBox fireSpreadCB = new JCheckBox();
  private JCheckBox fireSpottingCB = new JCheckBox();
  private JCheckBox fireSuppBeyondClassACB = new JCheckBox();
  private JCheckBox fireSuppClassACB = new JCheckBox();
  private JCheckBox fireSuppProductionRateCB = new JCheckBox();
  private JCheckBox fireSuppSpreadRateCB = new JCheckBox();
  private JCheckBox fireSuppWeatherBeyondClassACB = new JCheckBox();
  private JCheckBox fireSuppWeatherClassACB = new JCheckBox();
  private JCheckBox fireTypeLogicCB = new JCheckBox();
  private JCheckBox fmzCB = new JCheckBox();
  private JCheckBox gapProcessLogicCB = new JCheckBox();
  private JCheckBox insectDiseaseProbCB = new JCheckBox();
  private JCheckBox invasiveSpeciesLogicCB = new JCheckBox();
  private JCheckBox invasiveSpeciesLogicMSUCB = new JCheckBox();
  private JCheckBox processProbLogicCB = new JCheckBox();
  private JCheckBox processScheduleCB = new JCheckBox();
  private JCheckBox producingSeedLogicCB = new JCheckBox();
  private JCheckBox regenLogicFireCB = new JCheckBox();
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
  private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

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

  public SystemKnowledgeLoadSave() {
    this(null, "", false,true);
  }

  private void jbInit() throws Exception {

    /* Selection */

    selectAllPB.setText("Select All");
    selectAllPB.addActionListener(new SystemKnowledgeLoadSave_selectAllPB_actionAdapter(this));
    selectNonePB.setText("Select None");
    selectNonePB.addActionListener(new SystemKnowledgeLoadSave_selectNonePB_actionAdapter(this));

    JPanel selectPanel = new JPanel();
    selectPanel.add(selectAllPB, null);
    selectPanel.add(selectNonePB, null);

    /* Fire Knowledge */

    speciesCB.setText("Species Settings (e.g. Fire Resistance)");
    fireSpottingCB.setText("Fire Spotting");
    fireSeasonCB.setText("Fire Season");
    fireSuppSpreadRateCB.setText("Fire Spread Rate");
    fireSuppProductionRateCB.setText("Fire Suppression Production Rate");
    exteremeFireDataCB.setText("Extreme Fire Settings");
    fireSuppWeatherBeyondClassACB.setText("Fire Suppression Beyond Class A due to Weather");
    fireSuppWeatherClassACB.setText("Class A Fire Suppression due to Weather");
    fireSuppBeyondClassACB.setText("Fire Suppression Beyond Class A");
    fireSuppClassACB.setText("Class A Fire Suppression");
    fireSpreadCB.setText("Fire Spread");
    fmzCB.setText("Fire Occurrence -- Fire Suppression Costs -- Fire Management Zones");
    fireTypeLogicCB.setText("Type of Fire Logic");
    fireTypeLogicCB.setActionCommand("Type of Fire Logic");

    TitledBorder fireKnowledgeBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Fire Knowledge");
    JPanel fireKnowledgePanel = new JPanel();
    fireKnowledgePanel.setBorder(fireKnowledgeBorder);
    fireKnowledgePanel.setLayout(new GridLayout(7,0));
    fireKnowledgePanel.add(fireSpottingCB);
    fireKnowledgePanel.add(fmzCB, null);
    fireKnowledgePanel.add(fireSpreadCB, null);
    fireKnowledgePanel.add(fireTypeLogicCB, null);
    fireKnowledgePanel.add(fireSuppClassACB, null);
    fireKnowledgePanel.add(fireSuppBeyondClassACB, null);
    fireKnowledgePanel.add(fireSuppWeatherClassACB, null);
    fireKnowledgePanel.add(fireSuppWeatherBeyondClassACB, null);
    fireKnowledgePanel.add(exteremeFireDataCB, null);
    fireKnowledgePanel.add(fireSuppSpreadRateCB, null);
    fireKnowledgePanel.add(fireSuppProductionRateCB, null);
    fireKnowledgePanel.add(fireSeasonCB, null);
    fireKnowledgePanel.add(speciesCB, null);

    /* Pathways */

    vegetationPathwaysCB.setText("Vegetative Pathways");
    aquaticPathwaysCB.setText("Aquatic Pathways");
    aquaticPathwaysCB.setEnabled(false);

    TitledBorder pathwaysBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Pathways");
    JPanel pathwaysPanel = new JPanel();
    pathwaysPanel.setBorder(pathwaysBorder);
    pathwaysPanel.setLayout(new GridLayout(2,0));
    pathwaysPanel.add(vegetationPathwaysCB, null);
    pathwaysPanel.add(aquaticPathwaysCB, null);

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

    regenLogicFireCB.setText("Fire Regeneration Logic");
    treatmentLogicCB.setText("Treatment Logic");
    coniferEncroachLogicCB.setText("Conifer Encroachment Logic");
    insectDiseaseProbCB.setText("Insect/Disease Probabilities");
    regenLogicSuccCB.setText("Succession Regeneration Logic");
    climateCB.setText("Climate");
    processProbLogicCB.setText("Process Probability Logic");
    regenDelayLogicCB.setText("Regeneration Delay Logic");
    gapProcessLogicCB.setText("Gap Process Logic");
    competitionLogicCB.setText("Lifeform Competition Logic");
    invasiveSpeciesLogicCB.setText("Invasive Species Logic");
    invasiveSpeciesLogicMSUCB.setText("Invasive Species Logic MSU");
    producingSeedLogicCB.setText("Producing Seed Logic");
    vegUnitFireTypeLogicLogicCB.setText("Veg Unit Fire Type Logic");

    TitledBorder miscBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), "Miscellaneous");
    JPanel miscPanel = new JPanel();
    miscPanel.setBorder(miscBorder);
    miscPanel.setLayout(new GridLayout(6,2));
    miscPanel.add(regenLogicFireCB, null);
    miscPanel.add(regenLogicSuccCB, null);
    miscPanel.add(coniferEncroachLogicCB, null);
    miscPanel.add(treatmentLogicCB, null);
    miscPanel.add(insectDiseaseProbCB, null);
    miscPanel.add(processProbLogicCB);
    miscPanel.add(invasiveSpeciesLogicCB);
    miscPanel.add(invasiveSpeciesLogicMSUCB);
    miscPanel.add(regenDelayLogicCB);
    miscPanel.add(gapProcessLogicCB);
    miscPanel.add(competitionLogicCB);
    miscPanel.add(producingSeedLogicCB);
    miscPanel.add(vegUnitFireTypeLogicLogicCB);
    miscPanel.add(climateCB, null);

    /* Output File */

    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new SystemKnowledgeLoadSave_cancelPB_actionAdapter(this));
    loadSavePB.setToolTipText("");
    loadSavePB.setText("Save");
    loadSavePB.addActionListener(new SystemKnowledgeLoadSave_loadSavePB_actionAdapter(this));

    JPanel loadSaveCancelPanel = new JPanel();
    loadSaveCancelPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    loadSaveCancelPanel.add(loadSavePB, null);
    loadSaveCancelPanel.add(cancelPB, null);

    outputFileText.setBackground(Color.white);
    outputFileText.setEnabled(false);
    outputFileText.setDisabledTextColor(Color.black);
    outputFileText.setEditable(false);
    outputFileText.setColumns(30);

    JScrollPane fileScrollPane = new JScrollPane();
    fileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    fileScrollPane.getViewport().add(outputFileText, null);

    JPanel fileScrollPanel = new JPanel();
    fileScrollPanel.setLayout(new BorderLayout());
    fileScrollPanel.add(fileScrollPane, BorderLayout.NORTH);

    pickFilePB.setText("Pick");
    pickFilePB.addActionListener(new SystemKnowledgeLoadSave_pickFilePB_actionAdapter(this));

    TitledBorder pickFileBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "File Name");
    JPanel pickFilePanel = new JPanel();
    pickFilePanel.setBorder(pickFileBorder);
    pickFilePanel.setLayout(new BorderLayout());
    pickFilePanel.add(pickFilePB, BorderLayout.WEST);
    pickFilePanel.add(fileScrollPanel, BorderLayout.CENTER);

    JPanel pickLoadSaveCancelPanel = new JPanel();
    pickLoadSaveCancelPanel.setLayout(new BorderLayout());
    pickLoadSaveCancelPanel.add(pickFilePanel, BorderLayout.NORTH);
    pickLoadSaveCancelPanel.add(loadSaveCancelPanel, BorderLayout.SOUTH);

    JPanel outputFilePanel = new JPanel();
    outputFilePanel.setLayout(new BorderLayout());
    outputFilePanel.add(pickLoadSaveCancelPanel, BorderLayout.NORTH);

    /* Main */

    mainTitleBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Check what data you wish saved");

    JPanel CBMainPanel = new JPanel();
    CBMainPanel.setBorder(mainTitleBorder);
    CBMainPanel.setLayout(verticalFlowLayout1);
    CBMainPanel.add(selectPanel, null);
    CBMainPanel.add(fireKnowledgePanel, null);
    CBMainPanel.add(pathwaysPanel, null);
    CBMainPanel.add(schedulesPanel, null);
    CBMainPanel.add(miscPanel, null);

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
    checkBoxes.put(SystemKnowledge.REGEN_LOGIC_FIRE,regenLogicFireCB);
    checkBoxes.put(SystemKnowledge.REGEN_LOGIC_SUCC,regenLogicSuccCB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_SPREAD_RATE_LOGIC,fireSuppSpreadRateCB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_PRODUCTION_RATE_LOGIC,fireSuppProductionRateCB);
    checkBoxes.put(SystemKnowledge.FIRE_SEASON,fireSeasonCB);
    checkBoxes.put(SystemKnowledge.EXTREME_FIRE_DATA,exteremeFireDataCB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A,fireSuppWeatherBeyondClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_WEATHER_CLASS_A_LOGIC,fireSuppWeatherClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A_LOGIC,fireSuppBeyondClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SUPP_CLASS_A_LOGIC,fireSuppClassACB);
    checkBoxes.put(SystemKnowledge.FIRE_SPREAD_LOGIC,fireSpreadCB);
    checkBoxes.put(SystemKnowledge.FMZ,fmzCB);

    checkBoxes.put(SystemKnowledge.FIRE_TYPE_LOGIC,fireTypeLogicCB);
    checkBoxes.put(SystemKnowledge.FIRE_SPOTTING_LOGIC,fireSpottingCB);

    checkBoxes.put(SystemKnowledge.REGEN_DELAY_LOGIC,regenDelayLogicCB);
    checkBoxes.put(SystemKnowledge.GAP_PROCESS_LOGIC,gapProcessLogicCB);
    checkBoxes.put(SystemKnowledge.DOCOMPETITION_LOGIC,competitionLogicCB);
    checkBoxes.put(SystemKnowledge.INVASIVE_SPECIES_LOGIC,invasiveSpeciesLogicCB);
    checkBoxes.put(SystemKnowledge.INVASIVE_SPECIES_LOGIC_MSU,invasiveSpeciesLogicMSUCB);
    checkBoxes.put(SystemKnowledge.PRODUCING_SEED_LOGIC,producingSeedLogicCB);
    checkBoxes.put(SystemKnowledge.VEG_UNIT_FIRE_TYPE_LOGIC,vegUnitFireTypeLogicLogicCB);

    if (save) {

      mainTitleBorder.setTitle("Check what data you wish to save");
      loadSavePB.setText("Save");
      loadSavePB.setEnabled(false);
      initCheckBoxes();

    } else {

      mainTitleBorder.setTitle("Check what data you wish to load");
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
    this.pickFilePB_actionPerformed(null);
    if (loadSaveFile == null) { return null; }

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

class SystemKnowledgeLoadSave_pickFilePB_actionAdapter implements java.awt.event.ActionListener {
  SystemKnowledgeLoadSave adaptee;

  SystemKnowledgeLoadSave_pickFilePB_actionAdapter(SystemKnowledgeLoadSave adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.pickFilePB_actionPerformed(e);
  }
}

class SystemKnowledgeLoadSave_loadSavePB_actionAdapter implements java.awt.event.ActionListener {
  SystemKnowledgeLoadSave adaptee;

  SystemKnowledgeLoadSave_loadSavePB_actionAdapter(SystemKnowledgeLoadSave adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.loadSavePB_actionPerformed(e);
  }
}

class SystemKnowledgeLoadSave_cancelPB_actionAdapter implements java.awt.event.ActionListener {
  SystemKnowledgeLoadSave adaptee;

  SystemKnowledgeLoadSave_cancelPB_actionAdapter(SystemKnowledgeLoadSave adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelPB_actionPerformed(e);
  }
}

class SystemKnowledgeLoadSave_selectAllPB_actionAdapter implements java.awt.event.ActionListener {
  SystemKnowledgeLoadSave adaptee;

  SystemKnowledgeLoadSave_selectAllPB_actionAdapter(SystemKnowledgeLoadSave adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.selectAllPB_actionPerformed(e);
  }
}

class SystemKnowledgeLoadSave_selectNonePB_actionAdapter implements java.awt.event.ActionListener {
  SystemKnowledgeLoadSave adaptee;

  SystemKnowledgeLoadSave_selectNonePB_actionAdapter(SystemKnowledgeLoadSave adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.selectNonePB_actionPerformed(e);
  }
}

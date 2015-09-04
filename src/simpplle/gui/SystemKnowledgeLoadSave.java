package simpplle.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.borland.jbcl.layout.*;
import simpplle.*;
import simpplle.comcode.*;


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
  private HashMap<SystemKnowledge.Kinds,JCheckBox> checkBoxes =
      new HashMap<SystemKnowledge.Kinds,JCheckBox>();

  private JPanel mainPanel = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  JPanel CBMainPanel = new JPanel();
  TitledBorder mainTitleBorder;
  JCheckBox coniferEncroachLogicCB = new JCheckBox();
  JCheckBox speciesCB = new JCheckBox();
  JCheckBox aquaticPathwaysCB = new JCheckBox();
  JCheckBox vegetationPathwaysCB = new JCheckBox();
  JCheckBox treatmentLogicCB = new JCheckBox();
  JCheckBox treatmentScheduleCB = new JCheckBox();
  JCheckBox processScheduleCB = new JCheckBox();
  JCheckBox insectDiseaseProbCB = new JCheckBox();
  JCheckBox climateCB = new JCheckBox();
  JCheckBox regenLogicFireCB = new JCheckBox();
  JCheckBox regenLogicSuccCB = new JCheckBox();
  JCheckBox fireSuppSpreadRateCB = new JCheckBox();
  JCheckBox fireSuppProductionRateCB = new JCheckBox();
  JCheckBox fireSeasonCB = new JCheckBox();
  JCheckBox exteremeFireDataCB = new JCheckBox();
  JCheckBox fireSuppWeatherBeyondClassACB = new JCheckBox();
  JCheckBox fireSuppWeatherClassACB = new JCheckBox();
  JCheckBox fireSuppBeyondClassACB = new JCheckBox();
  JCheckBox fireSuppClassACB = new JCheckBox();
  JCheckBox fireSpreadCB = new JCheckBox();
  JCheckBox fmzCB = new JCheckBox();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel4 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border1;
  TitledBorder titledBorder2;
  JPanel jPanel5 = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  Border border2;
  TitledBorder titledBorder3;
  JPanel jPanel6 = new JPanel();
  JPanel jPanel7 = new JPanel();
  Border border3;
  TitledBorder titledBorder4;
  GridLayout gridLayout3 = new GridLayout();
  GridLayout gridLayout4 = new GridLayout();
  Border border4;
  TitledBorder titledBorder5;
  JPanel jPanel8 = new JPanel();
  JButton selectNonePB = new JButton();
  JButton selectAllPB = new JButton();
  JPanel jPanel9 = new JPanel();
  JPanel jPanel10 = new JPanel();
  JButton pickFilePB = new JButton();
  Border border5;
  TitledBorder titledBorder6;
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel jPanel11 = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextField outputFileText = new JTextField();
  JButton cancelPB = new JButton();
  JButton loadSavePB = new JButton();
  JCheckBox fireTypeLogicCB = new JCheckBox();
  JPanel CBPanel = new JPanel();
  JScrollPane CBScroll = new JScrollPane();
  BorderLayout borderLayout6 = new BorderLayout();
  JCheckBox processProbLogicCB = new JCheckBox();
  JCheckBox regenDelayLogicCB = new JCheckBox();
  JCheckBox gapProcessLogicCB = new JCheckBox();
  JCheckBox competitionLogicCB = new JCheckBox();
  JCheckBox invasiveSpeciesLogicCB = new JCheckBox();
  JCheckBox producingSeedLogicCB = new JCheckBox();
  JCheckBox vegUnitFireTypeLogicLogicCB = new JCheckBox();
  JCheckBox invasiveSpeciesLogicMSUCB = new JCheckBox();

  private final JCheckBox fireSpottingCB = new JCheckBox();
  public SystemKnowledgeLoadSave(Frame frame, String title, boolean modal, boolean save) {
    super(frame, title, modal);
    this.save = save;
    try {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public SystemKnowledgeLoadSave() {
    this(null, "", false,true);
  }
  private void jbInit() throws Exception {
    mainTitleBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Check what data you wish saved");
    border1 = BorderFactory.createLineBorder(Color.white,1);
    titledBorder2 = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Fire Knowledge");
    border2 = BorderFactory.createLineBorder(SystemColor.controlText,1);
    titledBorder3 = new TitledBorder(BorderFactory.createLineBorder(Color.black,1),"Pathways");
    border3 = BorderFactory.createLineBorder(Color.black,1);
    titledBorder4 = new TitledBorder(border3,"Schedules");
    border4 = BorderFactory.createLineBorder(Color.black,1);
    titledBorder5 = new TitledBorder(border4,"Miscellaneous");
    border5 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder6 = new TitledBorder(border5,"Output File");
    mainPanel.setLayout(borderLayout1);
    CBMainPanel.setBorder(mainTitleBorder);
    CBMainPanel.setLayout(verticalFlowLayout1);
    coniferEncroachLogicCB.setText("Conifer Encroachment Logic");
    speciesCB.setText("Species Settings (e.g. Fire Resistance)");
    aquaticPathwaysCB.setEnabled(false);
    aquaticPathwaysCB.setText("Aquatic Pathways");
    vegetationPathwaysCB.setText("Vegetative Pathways");
    treatmentLogicCB.setText("Treatment Logic");
    treatmentScheduleCB.setText("Treatment Schedules");
    processScheduleCB.setText("Process Schedules");
    insectDiseaseProbCB.setText("Insect/Disease Probabilities");
    climateCB.setText("Climate");
    regenLogicFireCB.setText("Fire Regeneration Logic");
    regenLogicSuccCB.setText("Succession Regeneration Logic");
    fireSuppSpreadRateCB.setText("Fire Spread Rate");
    fireSuppProductionRateCB.setText("Fire Suppression Production Rate");
    fireSeasonCB.setText("Fire Season");
    exteremeFireDataCB.setText("Extreme Fire Settings");
    fireSuppWeatherBeyondClassACB.setText("Fire Suppression Beyond Class A due to Weather");
    fireSuppWeatherClassACB.setText("Class A Fire Suppression due to Weather");
    fireSuppBeyondClassACB.setText("Fire Suppression Beyond Class A");
    fireSuppClassACB.setText("Class A Fire Suppression");
    fireSpreadCB.setText("Fire Spread");
    
    jPanel4.add(fireSpottingCB);
    fireSpottingCB.setText("Fire Spotting");
    fmzCB.setText("Fire Occurrence -- Fire Suppression Costs -- Fire Management Zones");
    jPanel4.setLayout(gridLayout1);
    gridLayout1.setRows(7);
    jPanel4.setBorder(titledBorder2);
    jPanel5.setLayout(gridLayout2);
    gridLayout2.setRows(2);
    jPanel5.setBorder(titledBorder3);
    jPanel7.setBorder(titledBorder4);
    jPanel7.setLayout(gridLayout3);
    gridLayout3.setRows(2);
    jPanel6.setLayout(gridLayout4);
    gridLayout4.setRows(6);
    gridLayout4.setColumns(2);
    jPanel6.setBorder(titledBorder5);
    selectNonePB.setText("Select None");
    selectNonePB.addActionListener(new SystemKnowledgeLoadSave_selectNonePB_actionAdapter(this));
    selectAllPB.setText("Select All");
    selectAllPB.addActionListener(new SystemKnowledgeLoadSave_selectAllPB_actionAdapter(this));
    pickFilePB.setText("Pick");
    pickFilePB.addActionListener(new SystemKnowledgeLoadSave_pickFilePB_actionAdapter(this));
    jPanel10.setBorder(titledBorder6);
    jPanel10.setLayout(borderLayout4);
    jPanel3.setLayout(borderLayout2);
    jPanel2.setLayout(borderLayout3);
    jPanel11.setLayout(borderLayout5);
    outputFileText.setBackground(Color.white);
    outputFileText.setEnabled(false);
    outputFileText.setDisabledTextColor(Color.black);
    outputFileText.setEditable(false);
    outputFileText.setText("C:/MyDocuments/MyProjects/testing/poorman/test.sysknowledge");
    outputFileText.setColumns(30);
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new SystemKnowledgeLoadSave_cancelPB_actionAdapter(this));
    loadSavePB.setToolTipText("");
    loadSavePB.setText("Save");
    loadSavePB.addActionListener(new SystemKnowledgeLoadSave_loadSavePB_actionAdapter(this));
    jPanel9.setBorder(BorderFactory.createLoweredBevelBorder());
    titledBorder6.setTitle("File Name");
    fireTypeLogicCB.setActionCommand("Type of Fire Logic");
    fireTypeLogicCB.setText("Type of Fire Logic");
    CBPanel.setLayout(borderLayout6);
    processProbLogicCB.setText("Process Probability Logic");
    regenDelayLogicCB.setText("Regeneration Delay Logic");
    gapProcessLogicCB.setText("Gap Process Logic");
    competitionLogicCB.setText("Lifeform Competition Logic");
    invasiveSpeciesLogicCB.setText("Invasive Species Logic");
    invasiveSpeciesLogicMSUCB.setText("Invasive Species Logic MSU");
    producingSeedLogicCB.setText("Producing Seed Logic");
    vegUnitFireTypeLogicLogicCB.setText("Veg Unit Fire Type Logic");
    jPanel3.add(jPanel10,  BorderLayout.NORTH);
    jPanel10.add(pickFilePB,  BorderLayout.WEST);
    jPanel10.add(jPanel11,  BorderLayout.CENTER);
    jPanel11.add(jScrollPane1, BorderLayout.NORTH);
    jScrollPane1.getViewport().add(outputFileText, null);
    jPanel3.add(jPanel9,  BorderLayout.SOUTH);
    CBMainPanel.add(jPanel8, null);
    jPanel8.add(selectAllPB, null);
    jPanel8.add(selectNonePB, null);
    getContentPane().add(mainPanel);
    mainPanel.add(CBPanel,  BorderLayout.NORTH);
    CBMainPanel.add(jPanel4, null);
    jPanel4.add(fmzCB, null);
    jPanel4.add(fireSpreadCB, null);
    jPanel4.add(fireTypeLogicCB, null);
    jPanel4.add(fireSuppClassACB, null);
    jPanel4.add(fireSuppBeyondClassACB, null);
    jPanel4.add(fireSuppWeatherClassACB, null);
    jPanel4.add(fireSuppWeatherBeyondClassACB, null);
    jPanel4.add(exteremeFireDataCB, null);
    jPanel4.add(fireSuppSpreadRateCB, null);
    jPanel4.add(fireSuppProductionRateCB, null);
    jPanel4.add(fireSeasonCB, null);
    jPanel4.add(speciesCB, null);
    CBMainPanel.add(jPanel5, null);
    jPanel5.add(vegetationPathwaysCB, null);
    jPanel5.add(aquaticPathwaysCB, null);
    CBMainPanel.add(jPanel7, null);
    jPanel7.add(processScheduleCB, null);
    jPanel7.add(treatmentScheduleCB, null);
    CBMainPanel.add(jPanel6, null);
    jPanel6.add(regenLogicFireCB, null);
    jPanel6.add(regenLogicSuccCB, null);
    jPanel6.add(coniferEncroachLogicCB, null);
    jPanel6.add(treatmentLogicCB, null);
    jPanel6.add(insectDiseaseProbCB, null);
    jPanel6.add(processProbLogicCB);
    jPanel6.add(invasiveSpeciesLogicCB);
    jPanel6.add(invasiveSpeciesLogicMSUCB);
    jPanel6.add(regenDelayLogicCB);
    jPanel6.add(gapProcessLogicCB);
    jPanel6.add(competitionLogicCB);
    jPanel6.add(producingSeedLogicCB);
    jPanel6.add(vegUnitFireTypeLogicLogicCB);
    jPanel6.add(climateCB, null);
    mainPanel.add(jPanel2, BorderLayout.SOUTH);
    jPanel2.add(jPanel3, BorderLayout.NORTH);
    jPanel9.add(loadSavePB, null);
    jPanel9.add(cancelPB, null);
    mainPanel.add(CBPanel, BorderLayout.CENTER);
    CBPanel.add(CBScroll, BorderLayout.CENTER);
    CBScroll.getViewport().add(CBMainPanel, null);
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
      outputFileText.setText("");
      loadSavePB.setEnabled(false);
      initCheckBoxes();
    }
    else {
      mainTitleBorder.setTitle("Check what data you wish to load");
      loadSavePB.setText("Load");
    }

    checkBoxes.get(SystemKnowledge.AQUATIC_PATHWAYS).setEnabled(LtaValleySegmentGroup.loaded());

    checkBoxes.get(SystemKnowledge.FIRE_SEASON).setEnabled(
       (Simpplle.getCurrentZone().getId() == ValidZones.SOUTH_CENTRAL_ALASKA));

    checkBoxes.get(SystemKnowledge.PROCESS_PROB_LOGIC).setEnabled(
       (Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_FRONT_RANGE) ||
       (Simpplle.getCurrentZone().getId() == ValidZones.COLORADO_PLATEAU));

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
    }
    else {
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
    }
    catch (SimpplleError ex) {
    }
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
      }
      else {
        SystemKnowledge.readInputFile(loadSaveFile);
      }
      String msg = (save) ? "File Saved Successfully" :
                            "File Loaded Successfully";
      JOptionPane.showMessageDialog(this, msg, "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
      dialogCanceled = false;
      setVisible(false);
      dispose();
    }
    catch (SimpplleError ex) {
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

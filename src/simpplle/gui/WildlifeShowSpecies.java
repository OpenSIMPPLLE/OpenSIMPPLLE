/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import simpplle.comcode.WildlifeHabitatData;
import javax.swing.border.*;

/** 
 * This class sets up Wildlife Show Species, a type of JDialog.  This class sets up a dialog to choose individual mammals, birds, amphibians, or reptiles.
 * It gives user the choice of a report for entire group or individuals members of it.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class WildlifeShowSpecies extends JDialog {
  WildlifeHabitatData modelData;
  String              species;
  int                 modelId;
  String              selectedHtGrp;
  boolean             inInit = false;
  int                 elevationIndex = 0;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel landCoverPanel = new JPanel();
  JScrollPane landCoverScrollPane = new JScrollPane();
  JTextArea landCoverText = new JTextArea();
  JPanel modelPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JRadioButton region1RB = new JRadioButton();
  JRadioButton IDGapRB = new JRadioButton();
  JRadioButton MTGapRB = new JRadioButton();
  ButtonGroup modelButtonGroup = new ButtonGroup();
  JPanel dataPanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  JPanel buffersPanel = new JPanel();
  JLabel aspectLabel = new JLabel();
  FlowLayout flowLayout6 = new FlowLayout();
  JLabel aspectValue = new JLabel();
  JPanel aspectPanel = new JPanel();
  JLabel slopeLabel = new JLabel();
  FlowLayout flowLayout7 = new FlowLayout();
  JLabel slopeValue = new JLabel();
  JPanel slopePanel = new JPanel();
  JTextArea buffersText = new JTextArea();
  JLabel buffersLabel = new JLabel();
  JPanel htGrpPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JComboBox htGrpCB = new JComboBox();
  TitledBorder validHabitatBorder;
  JPanel messagePanel = new JPanel();
  JLabel messageLabel2 = new JLabel();
  JLabel messageLabel1 = new JLabel();
  JPanel elevationPanel = new JPanel();
  JLabel maxElevLabel = new JLabel();
  JLabel maxElevValue = new JLabel();
  FlowLayout flowLayout8 = new FlowLayout();
  JPanel maxElevPanel = new JPanel();
  JLabel minElevLabel = new JLabel();
  FlowLayout flowLayout3 = new FlowLayout();
  JLabel minElevValue = new JLabel();
  JPanel minElevPanel = new JPanel();
  JPanel descriptionPanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JLabel descriptionValue = new JLabel();
  JLabel descriptionLabel = new JLabel();
  TitledBorder titledBorder1;
  JPanel prevNextPanel = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  JButton nextPB = new JButton();

  public WildlifeShowSpecies(Frame frame, String title, boolean modal,
                             String species)
  {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.species   = species;
    initialize();
  }

  public WildlifeShowSpecies() {
    this(null, "", false, null);
  }
  void jbInit() throws Exception {
    validHabitatBorder = new TitledBorder(BorderFactory.createEmptyBorder(),"Valid Habitat");
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Elevation");
    mainPanel.setLayout(borderLayout1);
    northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
    landCoverPanel.setLayout(new BoxLayout(landCoverPanel, BoxLayout.Y_AXIS));
    landCoverText.setEditable(false);
    landCoverText.setColumns(80);
    landCoverText.setRows(10);
    modelPanel.setLayout(flowLayout1);
    modelPanel.setBorder(BorderFactory.createEtchedBorder());
    region1RB.setEnabled(false);
    region1RB.setText("Region 1");
    region1RB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        region1RB_actionPerformed(e);
      }
    });
    IDGapRB.setEnabled(false);
    IDGapRB.setText("Idaho GAP");
    IDGapRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        IDGapRB_actionPerformed(e);
      }
    });
    MTGapRB.setEnabled(false);
    MTGapRB.setSelected(true);
    MTGapRB.setText("Montana GAP");
    MTGapRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MTGapRB_actionPerformed(e);
      }
    });
    dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
    buffersPanel.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setHgap(10);
    aspectLabel.setText("Aspect*       ");
    aspectLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    flowLayout6.setHgap(10);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    aspectValue.setText(">=60%");
    aspectValue.setForeground(Color.blue);
    aspectValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    aspectPanel.setLayout(flowLayout6);
    slopeLabel.setText("Slope*        ");
    slopeLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    flowLayout7.setHgap(10);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    slopeValue.setText("70 - 300 degree");
    slopeValue.setForeground(Color.blue);
    slopeValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    slopePanel.setLayout(flowLayout7);
    buffersText.setEditable(false);
    buffersText.setText("Select cover types within 450 m buffer around slopes >=60% having " +
    "aspects 70-300 degrees");
    buffersText.setColumns(50);
    buffersText.setLineWrap(true);
    buffersText.setRows(3);
    buffersText.setWrapStyleWord(true);
    buffersLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    buffersLabel.setText("Buffers*      ");
    htGrpPanel.setLayout(flowLayout2);
    htGrpCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpCB_actionPerformed(e);
      }
    });
    landCoverPanel.setBorder(validHabitatBorder);
    validHabitatBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    messageLabel2.setFont(new java.awt.Font("Monospaced", 2, 12));
    messageLabel2.setText("  complete report has to be done in the Arcview extension.");
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    messageLabel1.setFont(new java.awt.Font("Monospaced", 2, 12));
    messageLabel1.setText("* These items cannot be determined within SIMPPLLE,");
    elevationPanel.setLayout(new BoxLayout(elevationPanel, BoxLayout.Y_AXIS));
    maxElevLabel.setText("Max*       ");
    maxElevLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    maxElevValue.setText("3900");
    maxElevValue.setForeground(Color.blue);
    maxElevValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    flowLayout8.setHgap(10);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    maxElevPanel.setLayout(flowLayout8);
    minElevLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    minElevLabel.setText("Min*       ");
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(10);
    minElevValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    minElevValue.setForeground(Color.blue);
    minElevValue.setText("1350");
    minElevPanel.setLayout(flowLayout3);
    descriptionPanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setHgap(10);
    descriptionValue.setText("East Continental Divide");
    descriptionValue.setForeground(Color.blue);
    descriptionValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    descriptionLabel.setText("Description");
    descriptionLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    elevationPanel.setBorder(titledBorder1);
    prevNextPanel.setLayout(flowLayout9);
    nextPB.setMargin(new Insets(2, 2, 2, 2));
    nextPB.setText("Next");
    nextPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    elevationPanel.add(prevNextPanel, null);
    messagePanel.add(messageLabel1, null);
    messagePanel.add(messageLabel2, null);
    buffersPanel.add(buffersLabel, null);
    buffersPanel.add(buffersText, null);
    slopePanel.add(slopeLabel, null);
    slopePanel.add(slopeValue, null);
    dataPanel.add(elevationPanel, null);
    minElevPanel.add(minElevLabel, null);
    minElevPanel.add(minElevValue, null);
    elevationPanel.add(descriptionPanel, null);
    elevationPanel.add(minElevPanel, null);
    elevationPanel.add(maxElevPanel, null);
    maxElevPanel.add(maxElevLabel, null);
    maxElevPanel.add(maxElevValue, null);
    dataPanel.add(slopePanel, null);
    dataPanel.add(aspectPanel, null);
    aspectPanel.add(aspectLabel, null);
    aspectPanel.add(aspectValue, null);
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    htGrpPanel.add(htGrpCB, null);
    northPanel.add(modelPanel, null);
    modelPanel.add(MTGapRB, null);
    modelPanel.add(IDGapRB, null);
    modelPanel.add(region1RB, null);
    northPanel.add(htGrpPanel, null);
    northPanel.add(dataPanel, null);
    northPanel.add(landCoverPanel, null);
    northPanel.add(messagePanel, null);
    landCoverPanel.add(landCoverScrollPane, null);
    landCoverScrollPane.getViewport().add(landCoverText, null);
    modelButtonGroup.add(MTGapRB);
    modelButtonGroup.add(IDGapRB);
    modelButtonGroup.add(region1RB);
    dataPanel.add(buffersPanel, null);
    descriptionPanel.add(descriptionLabel, null);
    descriptionPanel.add(descriptionValue, null);
    prevNextPanel.add(nextPB, null);
  }
/**
 * 
 */
  private void initialize() {
    modelId = simpplle.comcode.WildlifeHabitat.MT_GAP;
    MTGapRB.setEnabled(simpplle.comcode.WildlifeHabitat.isValidModel(species,modelId));

    modelId = simpplle.comcode.WildlifeHabitat.ID_GAP;
    IDGapRB.setEnabled(simpplle.comcode.WildlifeHabitat.isValidModel(species,modelId));

    modelId = simpplle.comcode.WildlifeHabitat.R1_WHR;
    region1RB.setEnabled(simpplle.comcode.WildlifeHabitat.isValidModel(species,modelId));

    if (MTGapRB.isEnabled()) {
      modelId = simpplle.comcode.WildlifeHabitat.MT_GAP;
    }
    else if (IDGapRB.isEnabled()) {
      modelId = simpplle.comcode.WildlifeHabitat.ID_GAP;
    }
    else if (region1RB.isEnabled()) {
      modelId = simpplle.comcode.WildlifeHabitat.R1_WHR;
    }
    else {
      modelId = simpplle.comcode.WildlifeHabitat.MT_GAP;
    }

    updateModel();
  }
/**
 * 
 */
  private void updateModel() {
    inInit = true;
    modelData = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,modelId);

    htGrpCB.removeAllItems();
    String[] htGrpNames = modelData.getHtGrpNames();
    for (int i=0; i<htGrpNames.length; i++) {
      htGrpCB.addItem(htGrpNames[i]);
    }
    htGrpCB.setSelectedIndex(0);
    selectedHtGrp = (String)htGrpCB.getSelectedItem();

    elevationIndex = 0;
    inInit = false;
    updateDialog();
  }

  private void updateDialog() {
    if (inInit) { return; }

    landCoverText.setText(modelData.getLandCoverText(selectedHtGrp));
    landCoverText.setCaretPosition(0);

    // Elevation
    if (modelData.isElevationData()) {
      nextPB.setVisible((modelData.isSingleElevation() == false));
      String description = modelData.getElevationDescription(elevationIndex);

      descriptionPanel.setVisible((description != null));
      descriptionLabel.setVisible((description != null));
      descriptionValue.setVisible((description != null));
      if (description != null) {
        descriptionValue.setText(description);
      }

      int min = modelData.getElevationMin(elevationIndex);
      if (min == -1) { minElevValue.setText(""); }
      else { minElevValue.setText(Integer.toString(min)); }

      int max = modelData.getElevationMax(elevationIndex);
      if (max == -1) { maxElevValue.setText(""); }
      else { maxElevValue.setText(Integer.toString(max)); }
    }
    else {
      nextPB.setVisible(false);
      descriptionPanel.setVisible(false);
      descriptionLabel.setVisible(false);
      descriptionValue.setVisible(false);
      minElevValue.setText("n/a");
      maxElevValue.setText("n/a");
    }

    String slope = modelData.getSlope();
    slopeLabel.setVisible(slope != null);
    slopeValue.setVisible(slope != null);
    if (slope != null) { slopeValue.setText(slope); }

    String aspect = modelData.getAspect();
    aspectLabel.setVisible(aspect != null);
    aspectValue.setVisible(aspect != null);
    if (aspect != null) { aspectValue.setText(aspect); }

    String buffers = modelData.getBuffers();
    buffersLabel.setVisible(buffers != null);
    buffersText.setVisible(buffers != null);
    if (buffers != null) { buffersText.setText(buffers); }

    update(getGraphics());
  }

  void htGrpCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }

    selectedHtGrp = (String)htGrpCB.getSelectedItem();
    updateDialog();
  }

  void MTGapRB_actionPerformed(ActionEvent e) {
    if (MTGapRB.isSelected()) {
      modelId = simpplle.comcode.WildlifeHabitat.MT_GAP;
      updateModel();
    }
  }

  void region1RB_actionPerformed(ActionEvent e) {
    if (region1RB.isSelected()) {
      modelId = simpplle.comcode.WildlifeHabitat.R1_WHR;
      updateModel();
    }
  }

  void IDGapRB_actionPerformed(ActionEvent e) {
    if (IDGapRB.isSelected()) {
      modelId = simpplle.comcode.WildlifeHabitat.ID_GAP;
      updateModel();
    }
  }

  void nextPB_actionPerformed(ActionEvent e) {
    elevationIndex = modelData.getNextElevationIndex(elevationIndex);
    updateDialog();
  }
}



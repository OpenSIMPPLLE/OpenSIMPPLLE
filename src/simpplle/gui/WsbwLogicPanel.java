package simpplle.gui;

import java.awt.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;
import java.awt.event.*;
import simpplle.comcode.Species;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class sets up the Western Spruce Bud Worm Logic Panel 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class WsbwLogicPanel extends JPanel {
  private Species prototypeCellValue = Species.RIPARIAN_GRASSES;

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel centerPanel = new JPanel();
  JScrollPane mainScroll = new JScrollPane();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel scrollPanel = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel characterOfAdjacentIndexPanel = new JPanel();
  JPanel regionalClimateIndexPanel = new JPanel();
  JPanel siteClimateIndexPanel = new JPanel();
  JPanel maturityIndexPanel = new JPanel();
  JPanel standVigorIndexPanel = new JPanel();
  JPanel structureIndexPanel = new JPanel();
  JPanel densityIndexPanel = new JPanel();
  JPanel percentClimateHostIndexPanel = new JPanel();
  JPanel percentHostIndexPanel = new JPanel();
  JPanel oneOfHostsPanel = new JPanel();
  JPanel susceptibilityIndexPanel = new JPanel();
  JPanel probabilityPanel = new JPanel();
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  TitledBorder titledBorder2;
  Border border3;
  TitledBorder titledBorder3;
  Border border4;
  TitledBorder titledBorder4;
  Border border5;
  TitledBorder titledBorder5;
  Border border6;
  TitledBorder titledBorder6;
  Border border7;
  TitledBorder titledBorder7;
  Border border8;
  TitledBorder titledBorder8;
  Border border9;
  TitledBorder titledBorder9;
  Border border10;
  TitledBorder titledBorder10;
  Border border11;
  TitledBorder titledBorder11;
  Border border12;
  TitledBorder titledBorder12;
  JPanel severeBorderPanel = new JPanel();
  Border border13;
  TitledBorder titledBorder13;
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  JPanel lightBorderPanel = new JPanel();
  Border border14;
  TitledBorder titledBorder14;
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel severeBottomPanel = new JPanel();
  JPanel severeTopPanel = new JPanel();
  JLabel severeTopLeftLabel = new JLabel();
  JLabel severeBottomLeftLabel = new JLabel();
  FlowLayout flowLayout3 = new FlowLayout();
  JLabel severeTopRightLabel1 = new JLabel();
  JTextField severeMinSIndexText = new JTextField();
  JLabel severeTopRightGreaterLabel = new JLabel();
  FlowLayout flowLayout4 = new FlowLayout();
  JLabel severeBottomRightLabel = new JLabel();
  JPanel severePanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout5 = new BorderLayout();
  JTextField severeDefaultProbText = new JTextField();
  JPanel severeTopLeftPanel = new JPanel();
  JPanel severeTopRightPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel severeBottomRightPanel = new JPanel();
  JPanel severeBottomLeftPanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  FlowLayout flowLayout6 = new FlowLayout();
  JPanel lightPanel = new JPanel();
  JPanel lightTopPanel = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JPanel lightBottomPanel = new JPanel();
  JPanel lightTopRightPanel = new JPanel();
  JPanel lightBottomRightPanel = new JPanel();
  FlowLayout flowLayout7 = new FlowLayout();
  FlowLayout flowLayout8 = new FlowLayout();
  JPanel LightTopLeftPanel = new JPanel();
  JPanel lightBottomLeftPanel = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  FlowLayout flowLayout10 = new FlowLayout();
  FlowLayout flowLayout11 = new FlowLayout();
  FlowLayout flowLayout12 = new FlowLayout();
  JLabel lightTopLeftLabel = new JLabel();
  JTextField jTextField4 = new JTextField();
  JLabel lightTopRightLessLabel2 = new JLabel();
  JLabel lightTopRightSIndexLabel = new JLabel();
  JLabel lightTopRightLessLabel1 = new JLabel();
  JTextField lightMaxSIndexText = new JTextField();
  JLabel lightBottomLeftLabel = new JLabel();
  JTextField lightDefaultProbText = new JTextField();
  JLabel lightBottomRightLabel = new JLabel();
  BorderLayout borderLayout7 = new BorderLayout();
  JPanel oneOfborderPanel = new JPanel();
  JPanel OneOfPanel8 = new JPanel();
  JPanel OneOfPanel1 = new JPanel();
  FlowLayout flowLayout13 = new FlowLayout();
  FlowLayout flowLayout14 = new FlowLayout();
  JCheckBox OneOfPercentHostCB = new JCheckBox();
  JLabel OneOfLabel1 = new JLabel();
  JPanel OneOfPanel = new JPanel();
  VerticalFlowLayout verticalFlowLayout4 = new VerticalFlowLayout();
  BorderLayout borderLayout8 = new BorderLayout();
  TitledBorder titledBorder15;
  JCheckBox oneOfRegionalClimateCB = new JCheckBox();
  JLabel OneOfLabel8 = new JLabel();
  JPanel oneOfInitPanel = new JPanel();
  FlowLayout flowLayout15 = new FlowLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JPanel OneOfPanel9 = new JPanel();
  FlowLayout flowLayout16 = new FlowLayout();
  JLabel OneOfLabel9 = new JLabel();
  JCheckBox oneOfCharacterOfAdjCB = new JCheckBox();
  FlowLayout flowLayout17 = new FlowLayout();
  JPanel OneOfPanel7 = new JPanel();
  JCheckBox oneOfSiteClimateCB = new JCheckBox();
  JLabel OneOfLabel7 = new JLabel();
  FlowLayout flowLayout18 = new FlowLayout();
  JPanel OneOfPanel6 = new JPanel();
  JCheckBox oneOfMaturityCB = new JCheckBox();
  JLabel OneOfLabel6 = new JLabel();
  FlowLayout flowLayout19 = new FlowLayout();
  JPanel OneOfPanel5 = new JPanel();
  JCheckBox oneOfStandVigorCB = new JCheckBox();
  JLabel OneOfLabel5 = new JLabel();
  FlowLayout flowLayout110 = new FlowLayout();
  JPanel OneOfPanel4 = new JPanel();
  JCheckBox oneOfStructureCB = new JCheckBox();
  JLabel OneOfLabel4 = new JLabel();
  FlowLayout flowLayout111 = new FlowLayout();
  JPanel oneOfPanel3 = new JPanel();
  JCheckBox oneOfDensityCB = new JCheckBox();
  JLabel OneOfLabel3 = new JLabel();
  FlowLayout flowLayout112 = new FlowLayout();
  JPanel OneOfPanel2 = new JPanel();
  JCheckBox oneOfPercentClimateCB = new JCheckBox();
  JLabel OneOfLabel2 = new JLabel();
  JPanel OneOfFinalPanel = new JPanel();
  JLabel oneOfFinalLabel2 = new JLabel();
  VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
  JLabel OneOfFinalLabel1 = new JLabel();
  JPanel notOneOfBorderPanel = new JPanel();
  JPanel notOneOfPanel = new JPanel();
  BorderLayout borderLayout9 = new BorderLayout();
  FlowLayout flowLayout20 = new FlowLayout();
  JLabel notOneOfLabel = new JLabel();
  TitledBorder titledBorder16;
  JPanel susceptibleSpeciesPanel = new JPanel();
  BorderLayout borderLayout10 = new BorderLayout();
  FlowLayout flowLayout21 = new FlowLayout();
  JScrollPane susceptibleSpeciesScroll = new JScrollPane();
  JList susceptibleSpeciesList = new JList();
  JButton chooseSusceptibleSpeciesPB = new JButton();
/**
 * Constructor for Western Spruce Budworm Logic panel
 */
  public WsbwLogicPanel() {
    try {
      jbInit();
      initialize();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  /**
   * Initializes the borders, text, colors, panels, components, and layouts for Western Spruce Bud Worm panel.
   * @throws Exception
   */
  void jbInit() throws Exception {
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Probability");
    border2 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Susceptibility Index (sIndex)");
    border3 = BorderFactory.createEmptyBorder();
    titledBorder3 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Susceptible Hosts");
    border4 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder4 = new TitledBorder(border4,"Percent Host Index");
    border5 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder5 = new TitledBorder(border5,"percent Climate Host Index");
    border6 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder6 = new TitledBorder(border6,"density Index");
    border7 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder7 = new TitledBorder(border7,"Structure Index");
    border8 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder8 = new TitledBorder(border8,"Stand Vigor Index");
    border9 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder9 = new TitledBorder(border9,"Maturity Index");
    border10 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder10 = new TitledBorder(border10,"Site Climate Index");
    border11 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder11 = new TitledBorder(border11,"Regional Climate Index");
    border12 = BorderFactory.createEmptyBorder();
    titledBorder12 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Character of Adjacent Index");
    border13 = BorderFactory.createLineBorder(Color.white,1);
    titledBorder13 = new TitledBorder(BorderFactory.createLineBorder(Color.white,1),"Severe WSBW");
    border14 = BorderFactory.createLineBorder(Color.white,1);
    titledBorder14 = new TitledBorder(border14,"Light WSBW");
    titledBorder15 = new TitledBorder(BorderFactory.createMatteBorder(6,6,6,6,new Color(153, 153, 153)),"For susceptible species sIndex is the rounded value of:");
    titledBorder16 = new TitledBorder(BorderFactory.createMatteBorder(6,6,6,6,new Color(153, 153, 153)),"For NON susceptible Species");
    this.setLayout(borderLayout1);
    centerPanel.setLayout(borderLayout2);
    scrollPanel.setLayout(verticalFlowLayout1);
    probabilityPanel.setBorder(titledBorder1);
    probabilityPanel.setLayout(verticalFlowLayout2);
    susceptibilityIndexPanel.setBorder(titledBorder2);
    susceptibilityIndexPanel.setLayout(borderLayout7);
    oneOfHostsPanel.setBorder(titledBorder3);
    oneOfHostsPanel.setLayout(borderLayout10);
    percentHostIndexPanel.setBorder(titledBorder4);
    percentClimateHostIndexPanel.setBorder(titledBorder5);
    densityIndexPanel.setBorder(titledBorder6);
    structureIndexPanel.setBorder(titledBorder7);
    standVigorIndexPanel.setBorder(titledBorder8);
    maturityIndexPanel.setBorder(titledBorder9);
    siteClimateIndexPanel.setBorder(titledBorder10);
    regionalClimateIndexPanel.setBorder(titledBorder11);
    characterOfAdjacentIndexPanel.setBorder(titledBorder12);
    severeBorderPanel.setLayout(borderLayout3);
    severeBorderPanel.setBorder(titledBorder13);
    lightBorderPanel.setBorder(titledBorder14);
    lightBorderPanel.setLayout(borderLayout4);
    borderLayout4.setHgap(5);
    severeTopLeftLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    severeTopLeftLabel.setText("Probability = sIndex");
    severeBottomLeftLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    severeBottomLeftLabel.setText("Probability = ");
    severeTopPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(3);
    flowLayout3.setVgap(0);
    severeTopRightLabel1.setFont(new java.awt.Font("Monospaced", 2, 12));
    severeTopRightLabel1.setText("sIndex");
    severeMinSIndexText.setText("20");
    severeMinSIndexText.setColumns(4);
    severeMinSIndexText.setHorizontalAlignment(SwingConstants.RIGHT);
    severeTopRightGreaterLabel.setFont(new java.awt.Font("Monospaced", 1, 18));
    severeTopRightGreaterLabel.setForeground(Color.black);
    severeTopRightGreaterLabel.setText(">");
    severeBottomPanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setHgap(3);
    flowLayout4.setVgap(0);
    severeBottomRightLabel.setFont(new java.awt.Font("Monospaced", 2, 12));
    severeBottomRightLabel.setText(" for any Other value of sIndex");
    severePanel.setLayout(borderLayout5);
    severeDefaultProbText.setText("0");
    severeDefaultProbText.setColumns(4);
    severeDefaultProbText.setHorizontalAlignment(SwingConstants.RIGHT);
    severeTopPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    severeBottomPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    verticalFlowLayout2.setHorizontalFill(true);
    severeTopLeftPanel.setLayout(flowLayout2);
    severeTopRightPanel.setLayout(flowLayout1);
    severeTopLeftPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    severeTopRightPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    severeBottomLeftPanel.setLayout(flowLayout5);
    severeBottomRightPanel.setLayout(flowLayout6);
    severeBottomLeftPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setHgap(0);
    flowLayout5.setVgap(0);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout6.setHgap(0);
    flowLayout6.setVgap(0);
    severeBottomRightPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(0);
    flowLayout2.setVgap(0);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(3);
    flowLayout1.setVgap(0);
    borderLayout5.setVgap(5);
    lightPanel.setLayout(borderLayout6);
    lightTopPanel.setLayout(flowLayout7);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout7.setHgap(0);
    flowLayout7.setVgap(0);
    lightTopPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    lightBottomPanel.setLayout(flowLayout8);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    flowLayout8.setHgap(0);
    flowLayout8.setVgap(0);
    lightBottomPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    LightTopLeftPanel.setLayout(flowLayout9);
    flowLayout9.setAlignment(FlowLayout.LEFT);
    flowLayout9.setHgap(0);
    flowLayout9.setVgap(0);
    lightTopRightPanel.setLayout(flowLayout10);
    flowLayout10.setAlignment(FlowLayout.LEFT);
    flowLayout10.setHgap(3);
    flowLayout10.setVgap(0);
    lightBottomLeftPanel.setLayout(flowLayout11);
    flowLayout11.setAlignment(FlowLayout.LEFT);
    flowLayout11.setHgap(0);
    flowLayout11.setVgap(0);
    lightBottomRightPanel.setLayout(flowLayout12);
    flowLayout12.setAlignment(FlowLayout.LEFT);
    flowLayout12.setHgap(0);
    flowLayout12.setVgap(0);
    borderLayout6.setVgap(5);
    lightTopLeftLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    lightTopLeftLabel.setText("Probability = sIndex");
    jTextField4.setText("0");
    jTextField4.setColumns(4);
    jTextField4.setHorizontalAlignment(SwingConstants.RIGHT);
    lightTopRightLessLabel2.setFont(new java.awt.Font("Monospaced", 1, 18));
    lightTopRightLessLabel2.setHorizontalAlignment(SwingConstants.LEADING);
    lightTopRightLessLabel2.setText("<");
    lightTopRightSIndexLabel.setFont(new java.awt.Font("Monospaced", 2, 12));
    lightTopRightSIndexLabel.setText("sIndex");
    lightTopRightLessLabel1.setFont(new java.awt.Font("Monospaced", 1, 18));
    lightTopRightLessLabel1.setText("<");
    lightMaxSIndexText.setText("50");
    lightMaxSIndexText.setColumns(4);
    LightTopLeftPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    lightTopRightPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    lightBottomLeftLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    lightBottomLeftLabel.setText("Probability = ");
    lightDefaultProbText.setText("0");
    lightDefaultProbText.setColumns(4);
    lightDefaultProbText.setHorizontalAlignment(SwingConstants.RIGHT);
    lightBottomLeftPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    lightBottomRightPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    lightBottomRightLabel.setFont(new java.awt.Font("Monospaced", 2, 12));
    lightBottomRightLabel.setText("for any other value of sIndex");
    oneOfborderPanel.setLayout(borderLayout8);
    OneOfPanel1.setLayout(flowLayout13);
    OneOfPanel8.setLayout(flowLayout14);
    flowLayout13.setAlignment(FlowLayout.LEFT);
    flowLayout13.setHgap(2);
    flowLayout13.setVgap(0);
    flowLayout14.setAlignment(FlowLayout.LEFT);
    flowLayout14.setHgap(2);
    flowLayout14.setVgap(0);
    OneOfLabel1.setFont(new java.awt.Font("Monospaced", 0, 12));
    OneOfLabel1.setText("(Percent Host Index)");
    OneOfPercentHostCB.setFont(new java.awt.Font("Monospaced", 2, 12));
    OneOfPercentHostCB.setSelected(true);
    OneOfPercentHostCB.setText("Index = Index *");
    OneOfPanel.setLayout(verticalFlowLayout4);
    oneOfborderPanel.setBorder(titledBorder15);
    oneOfRegionalClimateCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfRegionalClimateCB.setSelected(true);
    oneOfRegionalClimateCB.setText("Index = Index *");
    OneOfLabel8.setFont(new java.awt.Font("Monospaced", 0, 12));
    OneOfLabel8.setText("(Regional Climate Index)");
    jLabel1.setText("*");
    oneOfInitPanel.setLayout(flowLayout15);
    jLabel1.setFont(new java.awt.Font("Monospaced", 1, 12));
    jLabel1.setText("Index = 1");
    flowLayout15.setAlignment(FlowLayout.LEFT);
    flowLayout15.setHgap(5);
    flowLayout15.setVgap(0);
    jLabel3.setFont(new java.awt.Font("Monospaced", 2, 12));
    jLabel3.setText("(to start, modify as follows, check to include)");
    OneOfPanel9.setLayout(flowLayout16);
    flowLayout16.setAlignment(FlowLayout.LEFT);
    flowLayout16.setHgap(2);
    flowLayout16.setVgap(0);
    OneOfLabel9.setText("(Character of Adjacent Index)");
    OneOfLabel9.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfCharacterOfAdjCB.setText("Index = Index *");
    oneOfCharacterOfAdjCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfCharacterOfAdjCB.setSelected(true);
    flowLayout17.setVgap(0);
    flowLayout17.setHgap(2);
    flowLayout17.setAlignment(FlowLayout.LEFT);
    OneOfPanel7.setLayout(flowLayout17);
    oneOfSiteClimateCB.setText("Index = Index *");
    oneOfSiteClimateCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfSiteClimateCB.setSelected(true);
    OneOfLabel7.setText("(Site Climate Index)");
    OneOfLabel7.setFont(new java.awt.Font("Monospaced", 0, 12));
    flowLayout18.setVgap(0);
    flowLayout18.setHgap(2);
    flowLayout18.setAlignment(FlowLayout.LEFT);
    OneOfPanel6.setLayout(flowLayout18);
    oneOfMaturityCB.setText("Index = Index *");
    oneOfMaturityCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfMaturityCB.setSelected(true);
    OneOfLabel6.setText("(Maturity Index)");
    OneOfLabel6.setFont(new java.awt.Font("Monospaced", 0, 12));
    flowLayout19.setVgap(0);
    flowLayout19.setHgap(2);
    flowLayout19.setAlignment(FlowLayout.LEFT);
    OneOfPanel5.setLayout(flowLayout19);
    oneOfStandVigorCB.setText("Index = Index *");
    oneOfStandVigorCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfStandVigorCB.setSelected(true);
    OneOfLabel5.setText("(Stand Vigor Index)");
    OneOfLabel5.setFont(new java.awt.Font("Monospaced", 0, 12));
    flowLayout110.setVgap(0);
    flowLayout110.setHgap(2);
    flowLayout110.setAlignment(FlowLayout.LEFT);
    OneOfPanel4.setLayout(flowLayout110);
    oneOfStructureCB.setText("Index = Index *");
    oneOfStructureCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfStructureCB.setSelected(true);
    OneOfLabel4.setText("(Structure Index)");
    OneOfLabel4.setFont(new java.awt.Font("Monospaced", 0, 12));
    flowLayout111.setVgap(0);
    flowLayout111.setHgap(2);
    flowLayout111.setAlignment(FlowLayout.LEFT);
    oneOfPanel3.setLayout(flowLayout111);
    oneOfDensityCB.setText("Index = Index *");
    oneOfDensityCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfDensityCB.setSelected(true);
    OneOfLabel3.setText("(Density Index)");
    OneOfLabel3.setFont(new java.awt.Font("Monospaced", 0, 12));
    flowLayout112.setVgap(0);
    flowLayout112.setHgap(2);
    flowLayout112.setAlignment(FlowLayout.LEFT);
    OneOfPanel2.setLayout(flowLayout112);
    oneOfPercentClimateCB.setText("Index = Index *");
    oneOfPercentClimateCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfPercentClimateCB.setSelected(true);
    OneOfLabel2.setText("(Percent Climate Host Index)");
    OneOfLabel2.setFont(new java.awt.Font("Monospaced", 0, 12));
    oneOfFinalLabel2.setFont(new java.awt.Font("Monospaced", 2, 12));
    oneOfFinalLabel2.setText("sIndex = 0 (if none checked)");
    OneOfFinalPanel.setLayout(verticalFlowLayout3);
    OneOfFinalLabel1.setText("sIndex = Index (rounded  to whole number)");
    OneOfFinalLabel1.setFont(new java.awt.Font("Monospaced", 2, 12));
    notOneOfBorderPanel.setLayout(borderLayout9);
    notOneOfPanel.setLayout(flowLayout20);
    flowLayout20.setAlignment(FlowLayout.LEFT);
    flowLayout20.setHgap(2);
    flowLayout20.setVgap(0);
    notOneOfLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    notOneOfLabel.setText("sIndex = -1 (thus leading to a probability = 0)");
    notOneOfPanel.setBorder(titledBorder16);
    susceptibleSpeciesPanel.setLayout(flowLayout21);
    flowLayout21.setAlignment(FlowLayout.LEFT);
    flowLayout21.setHgap(0);
    flowLayout21.setVgap(0);
    chooseSusceptibleSpeciesPB.setText("Choose Susceptible Species");
    chooseSusceptibleSpeciesPB.addActionListener(new WsbwLogicPanel_chooseSusceptibleSpeciesPB_actionAdapter(this));
    susceptibleSpeciesList.setPrototypeCellValue(prototypeCellValue);
    this.add(jPanel1,  BorderLayout.NORTH);
    this.add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(mainScroll,  BorderLayout.CENTER);
    mainScroll.getViewport().add(scrollPanel, null);
    scrollPanel.add(probabilityPanel, null);
    probabilityPanel.add(lightBorderPanel, null);
    lightBorderPanel.add(lightPanel, BorderLayout.CENTER);
    lightPanel.add(lightTopPanel,  BorderLayout.NORTH);
    lightTopPanel.add(LightTopLeftPanel, null);
    LightTopLeftPanel.add(lightTopLeftLabel, null);
    lightTopPanel.add(lightTopRightPanel, null);
    lightTopRightPanel.add(jTextField4, null);
    lightTopRightPanel.add(lightTopRightLessLabel1, null);
    lightTopRightPanel.add(lightTopRightSIndexLabel, null);
    lightTopRightPanel.add(lightTopRightLessLabel2, null);
    lightTopRightPanel.add(lightMaxSIndexText, null);
    lightPanel.add(lightBottomPanel, BorderLayout.CENTER);
    lightBottomPanel.add(lightBottomLeftPanel, null);
    lightBottomLeftPanel.add(lightBottomLeftLabel, null);
    lightBottomLeftPanel.add(lightDefaultProbText, null);
    lightBottomPanel.add(lightBottomRightPanel, null);
    lightBottomRightPanel.add(lightBottomRightLabel, null);
    probabilityPanel.add(severeBorderPanel, null);
    severeBorderPanel.add(severePanel,  BorderLayout.CENTER);
    severeTopPanel.add(severeTopLeftPanel, null);
    severeTopPanel.add(severeTopRightPanel, null);
    severeTopRightPanel.add(severeTopRightLabel1, null);
    severeTopRightPanel.add(severeTopRightGreaterLabel, null);
    severeTopRightPanel.add(severeMinSIndexText, null);
    severeTopLeftPanel.add(severeTopLeftLabel, null);
    severePanel.add(severeBottomPanel, BorderLayout.CENTER);
    severeBottomPanel.add(severeBottomLeftPanel, null);
    severeBottomLeftPanel.add(severeBottomLeftLabel, null);
    severeBottomLeftPanel.add(severeDefaultProbText, null);
    severeBottomPanel.add(severeBottomRightPanel, null);
    severeBottomRightPanel.add(severeBottomRightLabel, null);
    severePanel.add(severeTopPanel, BorderLayout.NORTH);
    scrollPanel.add(susceptibilityIndexPanel, null);
    susceptibilityIndexPanel.add(oneOfborderPanel,  BorderLayout.NORTH);
    oneOfborderPanel.add(OneOfPanel, BorderLayout.CENTER);
    OneOfPanel7.add(oneOfSiteClimateCB, null);
    OneOfPanel7.add(OneOfLabel7, null);
    OneOfPanel6.add(oneOfMaturityCB, null);
    OneOfPanel6.add(OneOfLabel6, null);
    OneOfPanel5.add(oneOfStandVigorCB, null);
    OneOfPanel5.add(OneOfLabel5, null);
    OneOfPanel4.add(oneOfStructureCB, null);
    OneOfPanel4.add(OneOfLabel4, null);
    oneOfPanel3.add(oneOfDensityCB, null);
    oneOfPanel3.add(OneOfLabel3, null);
    OneOfPanel2.add(oneOfPercentClimateCB, null);
    OneOfPanel2.add(OneOfLabel2, null);
    OneOfPanel.add(oneOfInitPanel, null);
    OneOfPanel.add(OneOfPanel1, null);
    OneOfPanel.add(OneOfPanel2, null);
    OneOfPanel.add(oneOfPanel3, null);
    OneOfPanel.add(OneOfPanel4, null);
    OneOfPanel.add(OneOfPanel5, null);
    OneOfPanel.add(OneOfPanel6, null);
    OneOfPanel.add(OneOfPanel7, null);
    OneOfPanel.add(OneOfPanel9, null);
    OneOfPanel9.add(oneOfCharacterOfAdjCB, null);
    OneOfPanel9.add(OneOfLabel9, null);
    OneOfPanel.add(OneOfFinalPanel, null);
    OneOfFinalPanel.add(OneOfFinalLabel1, null);
    OneOfFinalPanel.add(oneOfFinalLabel2, null);
    susceptibilityIndexPanel.add(notOneOfBorderPanel, BorderLayout.CENTER);
    notOneOfBorderPanel.add(notOneOfPanel, BorderLayout.CENTER);
    notOneOfPanel.add(notOneOfLabel, null);
    oneOfInitPanel.add(jLabel1, null);
    oneOfInitPanel.add(jLabel3, null);
    OneOfPanel8.add(oneOfRegionalClimateCB, null);
    OneOfPanel8.add(OneOfLabel8, null);
    OneOfPanel1.add(OneOfPercentHostCB, null);
    OneOfPanel1.add(OneOfLabel1, null);
    OneOfPanel.add(OneOfPanel8, null);
    scrollPanel.add(oneOfHostsPanel, null);
    oneOfHostsPanel.add(susceptibleSpeciesPanel, BorderLayout.CENTER);
    susceptibleSpeciesPanel.add(susceptibleSpeciesScroll, null);
    susceptibleSpeciesPanel.add(chooseSusceptibleSpeciesPB, null);
    susceptibleSpeciesScroll.getViewport().add(susceptibleSpeciesList, null);
    scrollPanel.add(percentHostIndexPanel, null);
    scrollPanel.add(percentClimateHostIndexPanel, null);
    scrollPanel.add(densityIndexPanel, null);
    scrollPanel.add(structureIndexPanel, null);
    scrollPanel.add(standVigorIndexPanel, null);
    scrollPanel.add(maturityIndexPanel, null);
    scrollPanel.add(siteClimateIndexPanel, null);
    scrollPanel.add(regionalClimateIndexPanel, null);
    scrollPanel.add(characterOfAdjacentIndexPanel, null);
  }
/**
 * Initializes the susceptible species list to a variety of species.  
 */
  private void initialize() {
    susceptibleSpeciesList.setListData(new Species[] {
      Species.DF,
      Species.AF,
      Species.ES,
      Species.GF,
      Species.L_DF_AF,
      Species.L_DF_GF,
      Species.DF_LP_AF,
      Species.ES_AF,
      Species.DF_AF,
      Species.PP_DF,
      Species.L_DF,
      Species.DF_LP,
      Species.L_DF_PP});
  }
  void chooseSusceptibleSpeciesPB_actionPerformed(ActionEvent e) {

  }
}
/**
 * Creates an action adaptor for letting user choose susceptible species.   
 * 
 *
 */
class WsbwLogicPanel_chooseSusceptibleSpeciesPB_actionAdapter implements java.awt.event.ActionListener {
  WsbwLogicPanel adaptee;

  WsbwLogicPanel_chooseSusceptibleSpeciesPB_actionAdapter(WsbwLogicPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * allows user to choose susceptible species
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.chooseSusceptibleSpeciesPB_actionPerformed(e);
  }
}




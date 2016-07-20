/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.io.File;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

//import com.borland.jbcl.layout.VerticalFlowLayout;
import simpplle.JSimpplle;
import simpplle.comcode.*;
import java.awt.event.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class TreatmentLogic extends JDialog {
  private Frame theFrame;
  private String htGrpCellValue     = "--> FTH-M  ";
  private String speciesCellValue   = "--> RIPARIAN_GRASSES   ";
  private String sizeClassCellValue = "--> CLOSED-TALL-SHRUB  ";
  private String densityCellValue   = "--> 1";

  private TreatmentType selectedTreatment;
  private boolean       inInit = false;
  private boolean       inInitChange = false;

  private FeasibilityLogic feasibilityLogic;
  private Vector           changeLogic;
  private ChangeLogic      changeLogicRule;
  private int              changeLogicIndex;

  private JRadioButton[] evalANDRB  = new JRadioButton[5];
  private JRadioButton[] evalORRB   = new JRadioButton[5];
  private JRadioButton[] evalANDRBC = new JRadioButton[5];
  private JRadioButton[] evalORRBC  = new JRadioButton[5];

  private JPanel[] evalBoolPanel  = new JPanel[5];
  private JPanel[] evalBoolPanelC = new JPanel[5];

  private JLabel[] evalLabel  = new JLabel[6];
  private JLabel[] evalLabelC = new JLabel[6];

  private ListItem[] htGrpItems, speciesItems, sizeClassItems, densityItems;
  private ListItem[] htGrpItemsC, speciesItemsC, sizeClassItemsC, densityItemsC;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainInnerPanel = new JPanel();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel changePane = new JPanel();
  TitledBorder htGrpBorder;
  TitledBorder speciesBorder;
  TitledBorder sizeClassBorder;
  TitledBorder densityBorder;
  ButtonGroup htGrpRBGroup = new ButtonGroup();
  ButtonGroup speciesRBGroup = new ButtonGroup();
  ButtonGroup sizeClassRBGroup = new ButtonGroup();
  ButtonGroup densityRBGroup = new ButtonGroup();
  ButtonGroup evalBoolRBGroup0 = new ButtonGroup();
  ButtonGroup evalBoolRBGroup1 = new ButtonGroup();
  ButtonGroup evalBoolRBGroup2 = new ButtonGroup();
  ButtonGroup evalBoolRBGroup3 = new ButtonGroup();
  ButtonGroup evalBoolRBGroup4 = new ButtonGroup();
  TitledBorder stateBorder;
  TitledBorder callFunctionBorder;
  TitledBorder feasibleBorder;
  ButtonGroup feasibleRBGroup = new ButtonGroup();
  JPanel southPanelC = new JPanel();
  JPanel centerPanelC = new JPanel();
  JPanel densityPanelC = new JPanel();
  JPanel centerInnerPanelC = new JPanel();
  JScrollPane densityScrollPaneC = new JScrollPane();
  JPanel changeMainPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel speciesPanelC = new JPanel();
  JScrollPane sizeClassScrollPaneC = new JScrollPane();
  JPanel sizeClassPanelC = new JPanel();
  JPanel northPanelC = new JPanel();
  TitledBorder ruleBorder;
  JPanel toPanel = new JPanel();
  TitledBorder densityToBorder;
  TitledBorder sizeClassToBorder;
  TitledBorder speciesToBorder;
  TitledBorder changeOneOfBorder;
  TitledBorder stateToBorder;
  TitledBorder CallFunctionToBorder;
  Border border1;
  TitledBorder toPanelBorder;
  JButton statePickPBC = new JButton();
  FlowLayout flowLayout18 = new FlowLayout();
  JLabel stateValueC = new JLabel();
  JPanel statePanelC = new JPanel();
  JPanel htGrpPanelC = new JPanel();
  JScrollPane htGrpScrollPaneC = new JScrollPane();
  BorderLayout borderLayout6 = new BorderLayout();
  JPanel northMainPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JComboBox treatmentCB = new JComboBox();
  JLabel treatmentLabel = new JLabel();
  JPanel southPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel densityPanel = new JPanel();
  JPanel centerInnerPanel = new JPanel();
  JScrollPane densityScrollPane = new JScrollPane();
  JPanel feasibilityMainPanel = new JPanel();
  JScrollPane speciesScrollPane = new JScrollPane();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel speciesPanel = new JPanel();
  JPanel stateOuterPanel = new JPanel();
  JScrollPane sizeClassScrollPane = new JScrollPane();
  JPanel feasibilityMainPane = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  JPanel sizeClassPanel = new JPanel();
  JPanel northPanel = new JPanel();
  JScrollPane htGrpScrollPane = new JScrollPane();
  JPanel htGrpPanel = new JPanel();
  JRadioButton htGrpNotOneOfRB = new JRadioButton();
  JRadioButton htGrpOneOfRB = new JRadioButton();
  JPanel htGrpRBpanel = new JPanel();
  JRadioButton speciesOneOfRB = new JRadioButton();
  JRadioButton speciesNotOneOfRB = new JRadioButton();
  JPanel speciesRBPanel = new JPanel();
  JRadioButton sizeClassNotOneOfRB = new JRadioButton();
  JRadioButton sizeClassOneOfRB = new JRadioButton();
  JPanel sizeClassRBPanel = new JPanel();
  JRadioButton densityOneOfRB = new JRadioButton();
  JPanel densityRBPanel = new JPanel();
  JRadioButton densityNotOneOfRB = new JRadioButton();
  JPanel statePanel = new JPanel();
  FlowLayout flowLayout10 = new FlowLayout();
  JButton statePickPB = new JButton();
  JLabel stateValue = new JLabel();
  JPanel callFunctionOuterPanel = new JPanel();
  FlowLayout flowLayout7 = new FlowLayout();
  JTextArea callFunctionText = new JTextArea();
  JComboBox callFunctionCB = new JComboBox();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel callFunctionCBPanel = new JPanel();
  JPanel callFunctionPanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JPanel htGrpRBPanelC = new JPanel();
  JRadioButton htGrpNotOneOfRBC = new JRadioButton();
  JRadioButton htGrpOneOfRBC = new JRadioButton();
  JRadioButton speciesOneOfRBC = new JRadioButton();
  JRadioButton speciesNotOneOfRBC = new JRadioButton();
  JPanel speciesRBPanelC = new JPanel();
  JPanel sizeClassRBPanelC = new JPanel();
  JRadioButton sizeClassNotOneOfRBC = new JRadioButton();
  JRadioButton sizeClassOneOfRBC = new JRadioButton();
  JRadioButton densityNotOneOfRBC = new JRadioButton();
  JPanel densityRBPanelC = new JPanel();
  JRadioButton densityOneOfRBC = new JRadioButton();
  ButtonGroup toOneOfRBGroup = new ButtonGroup();
  TitledBorder checkBoxBorder;
  ButtonGroup toRBGroup = new ButtonGroup();
  ButtonGroup htGrpRBGroupC = new ButtonGroup();
  ButtonGroup evalBoolRBGroupC0 = new ButtonGroup();
  ButtonGroup speciesRBGroupC = new ButtonGroup();
  ButtonGroup evalBoolRBGroupC1 = new ButtonGroup();
  ButtonGroup sizeClassRBGroupC = new ButtonGroup();
  ButtonGroup evalBoolRBGroupC2 = new ButtonGroup();
  ButtonGroup densityRBGroupC = new ButtonGroup();
  ButtonGroup evalBoolRBGroupC3 = new ButtonGroup();
  ButtonGroup stateRBGroupC = new ButtonGroup();
  JMenuBar menuBar = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuLoadDefault = new JMenuItem();
  JMenuItem menuQuit = new JMenuItem();
  JMenu menuFeasibility = new JMenu();
  JMenuItem menuFeasibilityEvalOrder = new JMenuItem();
  JMenu menuChange = new JMenu();
  JMenuItem menuChangeNew = new JMenuItem();
  JMenuItem menuChangeEvalOrder = new JMenuItem();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileDefaultAll = new JMenuItem();
  FlowLayout flowLayout29 = new FlowLayout();
  JTabbedPane toTabbedPane = new JTabbedPane();
  JComboBox speciesToCB = new JComboBox();
  JRadioButton speciesToRB = new JRadioButton();
  JPanel stateToItemPanel = new JPanel();
  JPanel speciesToPanel = new JPanel();
  JPanel sizeClassToPanel = new JPanel();
  JPanel densityToPanel = new JPanel();
  JComboBox densityToCB = new JComboBox();
  JComboBox sizeClassToCB = new JComboBox();
  JRadioButton densityToRB = new JRadioButton();
  JRadioButton sizeClassToRB = new JRadioButton();
  JPanel stateToItemInnerPanel = new JPanel();
  FlowLayout flowLayout28 = new FlowLayout();
  FlowLayout flowLayout25 = new FlowLayout();
  FlowLayout flowLayout24 = new FlowLayout();
  FlowLayout flowLayout23 = new FlowLayout();
  FlowLayout flowLayout22 = new FlowLayout();
  JPanel toStateItemPanel = new JPanel();
  FlowLayout flowLayout20 = new FlowLayout();
  JPanel stateToOuterPanel = new JPanel();
  JLabel stateToValue = new JLabel();
  FlowLayout flowLayout30 = new FlowLayout();
  JPanel stateToPanel = new JPanel();
  JPanel toStatePanel = new JPanel();
  JButton statePickToPB = new JButton();
  FlowLayout flowLayout110 = new FlowLayout();
  FlowLayout flowLayout21 = new FlowLayout();
  JPanel toFunctionCallPanel = new JPanel();
  FlowLayout flowLayout17 = new FlowLayout();
  FlowLayout flowLayout14 = new FlowLayout();
  JPanel callFunctionOuterPanelC = new JPanel();
  JPanel callFunctionPanelC = new JPanel();
  JTextArea callFunctionTextC = new JTextArea();
  FlowLayout flowLayout31 = new FlowLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel callFunctionCBPanelC = new JPanel();
  JComboBox callFunctionCBC = new JComboBox();
  JPanel evalOrderPanel = new JPanel();
  JPanel topFeasiblePanel = new JPanel();
  FlowLayout flowLayout33 = new FlowLayout();
  FlowLayout flowLayout34 = new FlowLayout();
  JCheckBox densityCB = new JCheckBox();
  JPanel checkBoxPanel = new JPanel();
  JCheckBox callFunctionCheckBox = new JCheckBox();
  JCheckBox speciesCB = new JCheckBox();
  JCheckBox htGrpCB = new JCheckBox();
  GridLayout gridLayout14 = new GridLayout();
  JCheckBox stateCB = new JCheckBox();
  JCheckBox sizeClassCB = new JCheckBox();
  JRadioButton feasibleNOTRB = new JRadioButton();
  JRadioButton feasibleRB = new JRadioButton();
  JPanel feasiblePanel = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  JLabel evalLabel5 = new JLabel();
  JRadioButton evalANDRB4 = new JRadioButton();
  JRadioButton evalORRB4 = new JRadioButton();
  JPanel evalBoolPanel4 = new JPanel();
  JLabel evalLabel4 = new JLabel();
  JRadioButton evalORRB3 = new JRadioButton();
  JPanel evalBoolPanel3 = new JPanel();
  GridLayout gridLayout13 = new GridLayout();
  JRadioButton evalANDRB3 = new JRadioButton();
  JLabel evalLabel3 = new JLabel();
  JRadioButton evalORRB2 = new JRadioButton();
  GridLayout gridLayout11 = new GridLayout();
  JPanel evalBoolPanel2 = new JPanel();
  JRadioButton evalANDRB2 = new JRadioButton();
  JLabel evalLabel2 = new JLabel();
  JLabel evalLabel1 = new JLabel();
  JRadioButton evalORRB1 = new JRadioButton();
  JRadioButton evalANDRB1 = new JRadioButton();
  GridLayout gridLayout10 = new GridLayout();
  JPanel evalBoolPanel1 = new JPanel();
  JPanel evalBoolPanel0 = new JPanel();
  GridLayout gridLayout9 = new GridLayout();
  JRadioButton evalORRB0 = new JRadioButton();
  JRadioButton evalANDRB0 = new JRadioButton();
  JLabel evalLabel0 = new JLabel();
  GridLayout gridLayout12 = new GridLayout();
  TitledBorder evalOrderTitle;
  JPanel evalOrderPanelC = new JPanel();
  JPanel topChangePanel = new JPanel();
  FlowLayout flowLayout8 = new FlowLayout();
  FlowLayout flowLayout35 = new FlowLayout();
  JCheckBox htGrpCBC = new JCheckBox();
  JCheckBox sizeClassCBC = new JCheckBox();
  JCheckBox stateCBC = new JCheckBox();
  JCheckBox densityCBC = new JCheckBox();
  JCheckBox speciesCBC = new JCheckBox();
  JPanel checkBoxPanelC = new JPanel();
  GridLayout gridLayout110 = new GridLayout();
  JButton showTextPB = new JButton();
  JPanel rulePBPanel = new JPanel();
  JButton prevPB = new JButton();
  JPanel rulePanel = new JPanel();
  FlowLayout flowLayout26 = new FlowLayout();
  JButton nextPB = new JButton();
  FlowLayout flowLayout12 = new FlowLayout();
  JLabel evalLabelC0 = new JLabel();
  JLabel evalLabelC4 = new JLabel();
  JLabel evalLabelC3 = new JLabel();
  JLabel evalLabelC2 = new JLabel();
  JLabel evalLabelC1 = new JLabel();
  JRadioButton evalORRBC0 = new JRadioButton();
  GridLayout gridLayout18 = new GridLayout();
  JPanel evalBoolPanelC0 = new JPanel();
  JRadioButton evalANDRBC0 = new JRadioButton();
  JRadioButton evalANDRBC1 = new JRadioButton();
  GridLayout gridLayout17 = new GridLayout();
  JRadioButton evalORRBC1 = new JRadioButton();
  JPanel evalBoolPanelC1 = new JPanel();
  JPanel evalBoolPanelC3 = new JPanel();
  JRadioButton evalORRBC3 = new JRadioButton();
  JRadioButton evalANDRBC3 = new JRadioButton();
  GridLayout gridLayout15 = new GridLayout();
  JRadioButton evalORRBC2 = new JRadioButton();
  JRadioButton evalANDRBC2 = new JRadioButton();
  GridLayout gridLayout16 = new GridLayout();
  JPanel evalBoolPanelC2 = new JPanel();
  JMenuItem menuChangeDelete = new JMenuItem();
  JPanel evalBoolPanelC4 = new JPanel();
  JLabel evalLabelC5 = new JLabel();
  GridLayout gridLayout27 = new GridLayout();
  JRadioButton evalANDRBC4 = new JRadioButton();
  JRadioButton evalORRBC4 = new JRadioButton();
  ButtonGroup evalBoolRBGroupC4 = new ButtonGroup();
  JCheckBox callFunctionCheckBoxC = new JCheckBox();
  JPanel callFunctionEvalInnerPanelC = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  JPanel callFunctionEvalCBPanelC = new JPanel();
  FlowLayout flowLayout32 = new FlowLayout();
  JTextArea callFunctionEvalTextC = new JTextArea();
  JComboBox callFunctionEvalCBC = new JComboBox();
  BorderLayout borderLayout8 = new BorderLayout();
  BorderLayout borderLayout9 = new BorderLayout();
  BorderLayout borderLayout10 = new BorderLayout();
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout36 = new FlowLayout();
  FlowLayout flowLayout37 = new FlowLayout();
  FlowLayout flowLayout38 = new FlowLayout();
  FlowLayout flowLayout39 = new FlowLayout();
  FlowLayout flowLayout41 = new FlowLayout();
  FlowLayout flowLayout43 = new FlowLayout();
  FlowLayout flowLayout45 = new FlowLayout();
  JTabbedPane conditionTabbedPaneC = new JTabbedPane();
  FlowLayout flowLayout13 = new FlowLayout();
  FlowLayout flowLayout16 = new FlowLayout();
  FlowLayout flowLayout19 = new FlowLayout();
  FlowLayout flowLayout51 = new FlowLayout();
  JScrollPane speciesScrollPaneC = new JScrollPane();
  JTextArea speciesTextC = new JTextArea();
  JPanel speciesControlPanelC = new JPanel();
  JButton speciesSelectPickPBC = new JButton();
  FlowLayout flowLayout42 = new FlowLayout();
  JPanel sizeClassControlPanelC = new JPanel();
  FlowLayout flowLayout49 = new FlowLayout();
  JButton sizeClassSelectPickPBC = new JButton();
  JPanel densityControlPanelC = new JPanel();
  JPanel htGrpControlPanelC = new JPanel();
  FlowLayout flowLayout44 = new FlowLayout();
  FlowLayout flowLayout48 = new FlowLayout();
  JButton densitySelectPickPBC = new JButton();
  JButton htGrpSelectPickPBC = new JButton();
  JTabbedPane conditionTabbedPane = new JTabbedPane();
  JPanel htGrpControlPanel = new JPanel();
  JPanel speciesControlPanel = new JPanel();
  JPanel sizeClassControlPanel = new JPanel();
  JPanel densityControlPanel = new JPanel();
  FlowLayout flowLayout40 = new FlowLayout();
  JButton htGrpSelectPickPB = new JButton();
  FlowLayout flowLayout46 = new FlowLayout();
  FlowLayout flowLayout47 = new FlowLayout();
  FlowLayout flowLayout50 = new FlowLayout();
  JButton speciesSelectPickPB = new JButton();
  JButton sizeClassSelectPickPB = new JButton();
  JButton densitySelectPickPB = new JButton();
  JTextArea densityText = new JTextArea();
  JTextArea sizeClassText = new JTextArea();
  JTextArea speciesText = new JTextArea();
  JTextArea htGrpText = new JTextArea();
  JTextArea htGrpTextC = new JTextArea();
  JTextArea sizeClassTextC = new JTextArea();
  JTextArea densityTextC = new JTextArea();
  JMenu menuUtility = new JMenu();
  JMenuItem menuPrintTreatment = new JMenuItem();
  JMenu menuKnowledgeSource = new JMenu();
  JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
  JMenuItem menuFileOldFormat = new JMenuItem();
  JMenuItem menuFileCreate = new JMenuItem();
  private JProbabilityTextField isEffectiveProbText = new JProbabilityTextField(100,5);

  public TreatmentLogic(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    theFrame = frame;
    initialize();
  }

  public TreatmentLogic() {
    this(null, "", false);
  }
  void jbInit() throws Exception {
    htGrpBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Habitat Type");
    speciesBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Species");
    sizeClassBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Size Class");
    densityBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Density");
    stateBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Current State");
    callFunctionBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Call Function");
    feasibleBorder = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(148, 145, 140),new Color(103, 101, 98)),"Treatment is:");
    ruleBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Rule");
    densityToBorder = new TitledBorder(BorderFactory.createEmptyBorder(),"Species");
    sizeClassToBorder = new TitledBorder(BorderFactory.createEmptyBorder(),"Size Class");
    speciesToBorder = new TitledBorder(BorderFactory.createEmptyBorder(),"Species");
    changeOneOfBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Change One of:");
    stateToBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"To State");
    CallFunctionToBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"To Resulting State of Function Call");
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    toPanelBorder = new TitledBorder(border1,"Change To:");
    checkBoxBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Check Item to Activate");
    evalOrderTitle = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Evaluation Left to Right according to below");
    mainPanel.setLayout(borderLayout1);
    mainInnerPanel.setLayout(borderLayout6);
    changePane.setLayout(borderLayout8);
    southPanelC.setLayout(flowLayout51);
    centerPanelC.setLayout(flowLayout19);
    densityPanelC.setLayout(new BoxLayout(densityPanelC, BoxLayout.Y_AXIS));
    densityPanelC.setBorder(BorderFactory.createEtchedBorder());
    centerInnerPanelC.setLayout(flowLayout16);
    changeMainPanel.setLayout(borderLayout5);
    speciesPanelC.setLayout(new BoxLayout(speciesPanelC, BoxLayout.Y_AXIS));
    speciesPanelC.setBorder(BorderFactory.createEtchedBorder());
    sizeClassPanelC.setLayout(new BoxLayout(sizeClassPanelC, BoxLayout.Y_AXIS));
    sizeClassPanelC.setBorder(BorderFactory.createEtchedBorder());
    northPanelC.setLayout(new BoxLayout(northPanelC, BoxLayout.Y_AXIS));
    toPanel.setLayout(flowLayout13);
    toPanel.setBorder(toPanelBorder);
    statePickPBC.setMargin(new Insets(0, 0, 0, 0));
    statePickPBC.setText("Choose Items from List");
    statePickPBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        statePickPBC_actionPerformed(e);
      }
    });
    flowLayout18.setAlignment(FlowLayout.LEFT);
    flowLayout18.setVgap(0);
    stateValueC.setFont(new java.awt.Font("Monospaced", 0, 12));
    stateValueC.setBorder(BorderFactory.createEtchedBorder());
    stateValueC.setText("                                    ");
    statePanelC.setLayout(flowLayout18);
    statePanelC.setBorder(BorderFactory.createEtchedBorder());
    htGrpPanelC.setLayout(new BoxLayout(htGrpPanelC, BoxLayout.Y_AXIS));
    htGrpPanelC.setBorder(BorderFactory.createEtchedBorder());
    northMainPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    northMainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    treatmentLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    treatmentLabel.setText("Current Treatment");
    borderLayout6.setVgap(10);
    southPanel.setLayout(flowLayout6);
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    densityPanel.setLayout(new BoxLayout(densityPanel, BoxLayout.Y_AXIS));
    densityPanel.setBorder(BorderFactory.createEtchedBorder());
    centerInnerPanel.setLayout(new BoxLayout(centerInnerPanel, BoxLayout.Y_AXIS));
    feasibilityMainPanel.setLayout(borderLayout2);
    speciesPanel.setLayout(new BoxLayout(speciesPanel, BoxLayout.Y_AXIS));
    speciesPanel.setBorder(BorderFactory.createEtchedBorder());
    stateOuterPanel.setLayout(flowLayout5);
    feasibilityMainPane.setLayout(borderLayout10);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout6.setHgap(0);
    flowLayout6.setVgap(0);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setHgap(0);
    flowLayout5.setVgap(0);
    sizeClassPanel.setLayout(new BoxLayout(sizeClassPanel, BoxLayout.Y_AXIS));
    sizeClassPanel.setBorder(BorderFactory.createEtchedBorder());
    northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
    htGrpPanel.setLayout(new BoxLayout(htGrpPanel, BoxLayout.Y_AXIS));
    htGrpNotOneOfRB.setText("NOT one of:");
    htGrpNotOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpNotOneOfRB_actionPerformed(e);
      }
    });
    htGrpOneOfRB.setSelected(true);
    htGrpOneOfRB.setText("one of:");
    htGrpOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpOneOfRB_actionPerformed(e);
      }
    });
    htGrpRBpanel.setLayout(flowLayout3);
    speciesOneOfRB.setSelected(true);
    speciesOneOfRB.setText("one of:");
    speciesOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesOneOfRB_actionPerformed(e);
      }
    });
    speciesNotOneOfRB.setText("NOT one of:");
    speciesNotOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesNotOneOfRB_actionPerformed(e);
      }
    });
    speciesRBPanel.setLayout(flowLayout36);
    sizeClassNotOneOfRB.setText("NOT one of:");
    sizeClassNotOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassNotOneOfRB_actionPerformed(e);
      }
    });
    sizeClassOneOfRB.setSelected(true);
    sizeClassOneOfRB.setText("one of:");
    sizeClassOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassOneOfRB_actionPerformed(e);
      }
    });
    sizeClassRBPanel.setLayout(flowLayout37);
    densityOneOfRB.setSelected(true);
    densityOneOfRB.setText("one of:");
    densityOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityOneOfRB_actionPerformed(e);
      }
    });
    densityRBPanel.setLayout(flowLayout38);
    densityNotOneOfRB.setText("NOT one of:");
    densityNotOneOfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityNotOneOfRB_actionPerformed(e);
      }
    });
    statePanel.setLayout(flowLayout10);
    statePanel.setBorder(BorderFactory.createEtchedBorder());
    flowLayout10.setAlignment(FlowLayout.LEFT);
    flowLayout10.setVgap(0);
    statePickPB.setMargin(new Insets(0, 0, 0, 0));
    statePickPB.setText("Choose Items from List");
    statePickPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        statePickPB_actionPerformed(e);
      }
    });
    stateValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    stateValue.setBorder(BorderFactory.createEtchedBorder());
    stateValue.setText("                                    ");
    callFunctionOuterPanel.setLayout(flowLayout7);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout7.setHgap(0);
    flowLayout7.setVgap(0);
    callFunctionText.setFont(new java.awt.Font("Monospaced", 0, 12));
    callFunctionText.setEditable(false);
    callFunctionText.setColumns(105);
    callFunctionText.setRows(3);
    borderLayout3.setVgap(5);
    callFunctionCBPanel.setLayout(flowLayout4);
    callFunctionPanel.setLayout(borderLayout3);
    callFunctionPanel.setBorder(BorderFactory.createEtchedBorder());
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setHgap(0);
    flowLayout4.setVgap(0);
    changeOneOfBorder.setTitle("To One of:");
    toPanelBorder.setTitle("Change");
    htGrpRBPanelC.setLayout(flowLayout39);
    htGrpNotOneOfRBC.setText("NOT one of:");
    htGrpNotOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpNotOneOfRBC_actionPerformed(e);
      }
    });
    htGrpOneOfRBC.setSelected(true);
    htGrpOneOfRBC.setText("one of:");
    htGrpOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpOneOfRBC_actionPerformed(e);
      }
    });
    speciesOneOfRBC.setSelected(true);
    speciesOneOfRBC.setText("one of");
    speciesOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesOneOfRBC_actionPerformed(e);
      }
    });
    speciesNotOneOfRBC.setText("NOT one of");
    speciesNotOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesNotOneOfRBC_actionPerformed(e);
      }
    });
    speciesRBPanelC.setLayout(flowLayout41);
    sizeClassRBPanelC.setLayout(flowLayout43);
    sizeClassNotOneOfRBC.setText("NOT one of");
    sizeClassNotOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassNotOneOfRBC_actionPerformed(e);
      }
    });
    sizeClassOneOfRBC.setSelected(true);
    sizeClassOneOfRBC.setText("one of");
    sizeClassOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassOneOfRBC_actionPerformed(e);
      }
    });
    densityNotOneOfRBC.setText("NOT one of:");
    densityNotOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityNotOneOfRBC_actionPerformed(e);
      }
    });
    densityRBPanelC.setLayout(flowLayout45);
    densityOneOfRBC.setSelected(true);
    densityOneOfRBC.setText("one of:");
    densityOneOfRBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityOneOfRBC_actionPerformed(e);
      }
    });
    treatmentCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        treatmentCB_actionPerformed(e);
      }
    });
    callFunctionCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        callFunctionCB_actionPerformed(e);
      }
    });
    this.setJMenuBar(menuBar);
    this.setModal(true);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    densityToBorder.setTitle("Density");
    menuFile.setText("File");
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOpen_actionPerformed(e);
      }
    });
    menuFileSave.setEnabled(false);
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileSaveAs_actionPerformed(e);
      }
    });
    menuLoadDefault.setToolTipText("Load Default data for the current treatment");
    menuLoadDefault.setText("Load Default");
    menuLoadDefault.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuLoadDefault_actionPerformed(e);
      }
    });
    menuQuit.setText("Close Dialog");
    menuQuit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuQuit_actionPerformed(e);
      }
    });
    menuFeasibility.setText("Feasibility");
    menuFeasibilityEvalOrder.setText("Evaluation Order");
    menuFeasibilityEvalOrder.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFeasibilityEvalOrder_actionPerformed(e);
      }
    });
    menuChange.setText("Change");
    menuChangeNew.setText("New Change Rule");
    menuChangeNew.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuChangeNew_actionPerformed(e);
      }
    });
    menuChangeEvalOrder.setEnabled(false);
    menuChangeEvalOrder.setText("Evaluation Order");
    menuChangeEvalOrder.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuChangeEvalOrder_actionPerformed(e);
      }
    });
    menuFileClose.setEnabled(false);
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileClose_actionPerformed(e);
      }
    });
    menuFileDefaultAll.setToolTipText("Load Default data for all Treatments");
    menuFileDefaultAll.setText("Load All Defaults");
    menuFileDefaultAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileDefaultAll_actionPerformed(e);
      }
    });
    flowLayout29.setVgap(0);
    flowLayout29.setHgap(0);
    flowLayout29.setAlignment(FlowLayout.LEFT);
    speciesToCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesToCB_actionPerformed(e);
      }
    });
    speciesToRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesToRB_actionPerformed(e);
      }
    });
    speciesToRB.setSelected(true);
    stateToItemPanel.setLayout(flowLayout20);
    speciesToPanel.setBorder(speciesToBorder);
    speciesToPanel.setLayout(flowLayout25);
    sizeClassToPanel.setBorder(sizeClassToBorder);
    sizeClassToPanel.setLayout(flowLayout24);
    densityToPanel.setLayout(flowLayout23);
    densityToPanel.setBorder(densityToBorder);
    densityToCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityToCB_actionPerformed(e);
      }
    });
    sizeClassToCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassToCB_actionPerformed(e);
      }
    });
    densityToRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityToRB_actionPerformed(e);
      }
    });
    sizeClassToRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassToRB_actionPerformed(e);
      }
    });
    stateToItemInnerPanel.setLayout(flowLayout22);
    stateToItemInnerPanel.setBorder(changeOneOfBorder);
    flowLayout28.setAlignment(FlowLayout.LEFT);
    flowLayout28.setHgap(0);
    flowLayout28.setVgap(0);
    flowLayout25.setAlignment(FlowLayout.LEFT);
    flowLayout25.setHgap(0);
    flowLayout25.setVgap(0);
    flowLayout24.setAlignment(FlowLayout.LEFT);
    flowLayout24.setHgap(0);
    flowLayout24.setVgap(0);
    flowLayout23.setAlignment(FlowLayout.LEFT);
    flowLayout23.setHgap(0);
    flowLayout23.setVgap(0);
    flowLayout22.setAlignment(FlowLayout.LEFT);
    flowLayout22.setHgap(0);
    flowLayout22.setVgap(0);
    toStateItemPanel.setLayout(flowLayout28);
    flowLayout20.setAlignment(FlowLayout.LEFT);
    flowLayout20.setHgap(0);
    flowLayout20.setVgap(0);
    toStateItemPanel.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        toStateItemPanel_focusGained(e);
      }
    });
    stateToOuterPanel.setLayout(flowLayout21);
    stateToValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    stateToValue.setBorder(BorderFactory.createEtchedBorder());
    stateToValue.setText("                                    ");
    flowLayout30.setAlignment(FlowLayout.LEFT);
    flowLayout30.setHgap(0);
    flowLayout30.setVgap(0);
    stateToPanel.setBorder(stateToBorder);
    stateToPanel.setLayout(flowLayout110);
    toStatePanel.setLayout(flowLayout30);
    toStatePanel.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        toStatePanel_focusGained(e);
      }
    });
    statePickToPB.setMargin(new Insets(0, 0, 0, 0));
    statePickToPB.setText("Choose Items from List");
    statePickToPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        statePickToPB_actionPerformed(e);
      }
    });
    flowLayout110.setAlignment(FlowLayout.LEFT);
    flowLayout110.setVgap(0);
    flowLayout21.setAlignment(FlowLayout.LEFT);
    flowLayout21.setHgap(0);
    flowLayout21.setVgap(0);
    toFunctionCallPanel.setLayout(flowLayout31);
    toFunctionCallPanel.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        toFunctionCallPanel_focusGained(e);
      }
    });
    flowLayout17.setAlignment(FlowLayout.LEFT);
    flowLayout17.setHgap(0);
    flowLayout17.setVgap(0);
    flowLayout14.setAlignment(FlowLayout.LEFT);
    flowLayout14.setHgap(0);
    flowLayout14.setVgap(0);
    callFunctionOuterPanelC.setLayout(flowLayout14);
    callFunctionPanelC.setLayout(borderLayout4);
    callFunctionPanelC.setBorder(CallFunctionToBorder);
    callFunctionTextC.setFont(new java.awt.Font("Monospaced", 0, 12));
    callFunctionTextC.setColumns(105);
    callFunctionTextC.setRows(3);
    flowLayout31.setAlignment(FlowLayout.LEFT);
    flowLayout31.setHgap(0);
    flowLayout31.setVgap(0);
    borderLayout4.setVgap(5);
    callFunctionCBPanelC.setLayout(flowLayout17);
    callFunctionCBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        callFunctionCBC_actionPerformed(e);
      }
    });
    topFeasiblePanel.setLayout(flowLayout33);
    flowLayout33.setAlignment(FlowLayout.LEFT);
    flowLayout33.setHgap(0);
    flowLayout33.setVgap(0);
    evalOrderPanel.setLayout(flowLayout34);
    flowLayout34.setAlignment(FlowLayout.LEFT);
    flowLayout34.setVgap(0);
    densityCB.setSelected(true);
    densityCB.setText("Density");
    densityCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityCB_actionPerformed(e);
      }
    });
    checkBoxPanel.setLayout(gridLayout14);
    checkBoxPanel.setBorder(checkBoxBorder);
    callFunctionCheckBox.setSelected(true);
    callFunctionCheckBox.setText("Call Function");
    callFunctionCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        callFunctionCheckBox_actionPerformed(e);
      }
    });
    speciesCB.setSelected(true);
    speciesCB.setText("Species");
    speciesCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesCB_actionPerformed(e);
      }
    });
    htGrpCB.setSelected(true);
    htGrpCB.setText("Habitat Type");
    htGrpCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpCB_actionPerformed(e);
      }
    });
    gridLayout14.setColumns(3);
    gridLayout14.setRows(2);
    stateCB.setSelected(true);
    stateCB.setText("Current State");
    stateCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stateCB_actionPerformed(e);
      }
    });
    sizeClassCB.setSelected(true);
    sizeClassCB.setText("Size Class");
    sizeClassCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassCB_actionPerformed(e);
      }
    });
    feasibleNOTRB.setFont(new java.awt.Font("Monospaced", 1, 12));
    feasibleNOTRB.setMargin(new Insets(0, 0, 0, 0));
    feasibleNOTRB.setText("NOT Feasible");
    feasibleNOTRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        feasibleNOTRB_actionPerformed(e);
      }
    });
    feasibleRB.setFont(new java.awt.Font("Monospaced", 1, 12));
    feasibleRB.setMargin(new Insets(0, 0, 0, 0));
    feasibleRB.setSelected(true);
    feasibleRB.setText("Feasible");
    feasibleRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        feasibleRB_actionPerformed(e);
      }
    });
    feasiblePanel.setLayout(flowLayout9);
    feasiblePanel.setBorder(feasibleBorder);
    flowLayout9.setAlignment(FlowLayout.LEFT);
    flowLayout9.setHgap(0);
    flowLayout9.setVgap(0);
    evalLabel5.setText("Call Function");
    evalANDRB4.setMargin(new Insets(0, 0, 0, 0));
    evalANDRB4.setSelected(true);
    evalANDRB4.setText("AND");
    evalANDRB4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRB4_actionPerformed(e);
      }
    });
    evalORRB4.setMargin(new Insets(0, 0, 0, 0));
    evalORRB4.setText("OR");
    evalORRB4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRB4_actionPerformed(e);
      }
    });
    evalBoolPanel4.setLayout(gridLayout12);
    evalLabel4.setText("Current State");
    evalORRB3.setMargin(new Insets(0, 0, 0, 0));
    evalORRB3.setText("OR");
    evalORRB3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRB3_actionPerformed(e);
      }
    });
    evalBoolPanel3.setLayout(gridLayout13);
    gridLayout13.setRows(2);
    evalANDRB3.setMargin(new Insets(0, 0, 0, 0));
    evalANDRB3.setSelected(true);
    evalANDRB3.setText("AND");
    evalANDRB3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRB3_actionPerformed(e);
      }
    });
    evalLabel3.setText("Density");
    evalORRB2.setMargin(new Insets(0, 0, 0, 0));
    evalORRB2.setText("OR");
    evalORRB2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRB2_actionPerformed(e);
      }
    });
    gridLayout11.setRows(2);
    evalBoolPanel2.setLayout(gridLayout11);
    evalANDRB2.setMargin(new Insets(0, 0, 0, 0));
    evalANDRB2.setSelected(true);
    evalANDRB2.setText("AND");
    evalANDRB2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRB2_actionPerformed(e);
      }
    });
    evalLabel2.setText("Size Class");
    evalLabel1.setText("Species");
    evalORRB1.setMargin(new Insets(0, 0, 0, 0));
    evalORRB1.setText("OR");
    evalORRB1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRB1_actionPerformed(e);
      }
    });
    evalANDRB1.setMargin(new Insets(0, 0, 0, 0));
    evalANDRB1.setSelected(true);
    evalANDRB1.setText("AND");
    evalANDRB1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRB1_actionPerformed(e);
      }
    });
    gridLayout10.setRows(2);
    evalBoolPanel1.setLayout(gridLayout10);
    evalBoolPanel0.setLayout(gridLayout9);
    gridLayout9.setRows(2);
    evalORRB0.setMargin(new Insets(0, 0, 0, 0));
    evalORRB0.setText("OR");
    evalORRB0.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRB0_actionPerformed(e);
      }
    });
    evalANDRB0.setMargin(new Insets(0, 0, 0, 0));
    evalANDRB0.setSelected(true);
    evalANDRB0.setText("AND");
    evalANDRB0.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRB0_actionPerformed(e);
      }
    });
    evalLabel0.setText("Habitat Type");
    evalBoolPanel0.setBorder(BorderFactory.createEtchedBorder());
    evalBoolPanel1.setBorder(BorderFactory.createEtchedBorder());
    evalBoolPanel2.setBorder(BorderFactory.createEtchedBorder());
    evalBoolPanel3.setBorder(BorderFactory.createEtchedBorder());
    evalBoolPanel4.setBorder(BorderFactory.createEtchedBorder());
    gridLayout12.setRows(2);
    evalOrderPanel.setBorder(evalOrderTitle);
    topChangePanel.setLayout(flowLayout8);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    flowLayout8.setHgap(0);
    flowLayout8.setVgap(0);
    evalOrderPanelC.setLayout(flowLayout35);
    flowLayout35.setAlignment(FlowLayout.LEFT);
    flowLayout35.setVgap(0);
    htGrpCBC.setSelected(true);
    htGrpCBC.setText("Habitat Type");
    htGrpCBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpCBC_actionPerformed(e);
      }
    });
    sizeClassCBC.setSelected(true);
    sizeClassCBC.setText("Size Class");
    sizeClassCBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassCBC_actionPerformed(e);
      }
    });
    stateCBC.setSelected(true);
    stateCBC.setText("Current State");
    stateCBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stateCBC_actionPerformed(e);
      }
    });
    densityCBC.setSelected(true);
    densityCBC.setText("Density");
    densityCBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densityCBC_actionPerformed(e);
      }
    });
    speciesCBC.setSelected(true);
    speciesCBC.setText("Species");
    speciesCBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesCBC_actionPerformed(e);
      }
    });
    checkBoxPanelC.setBorder(checkBoxBorder);
    checkBoxPanelC.setLayout(gridLayout110);
    gridLayout110.setColumns(3);
    gridLayout110.setRows(2);
    showTextPB.setText("Pseudo Code  Text");
    showTextPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showTextPB_actionPerformed(e);
      }
    });
    rulePBPanel.setLayout(flowLayout26);
    prevPB.setIcon(new ImageIcon(TreatmentLogic.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(TreatmentLogic.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    rulePanel.setLayout(flowLayout12);
    rulePanel.setBorder(ruleBorder);
    flowLayout26.setAlignment(FlowLayout.LEFT);
    flowLayout26.setVgap(0);
    nextPB.setIcon(new ImageIcon(TreatmentLogic.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(TreatmentLogic.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    flowLayout12.setAlignment(FlowLayout.LEFT);
    flowLayout12.setVgap(0);
    evalLabelC0.setText("Habitat Type");
    evalLabelC4.setText("Current State");
    evalLabelC3.setText("Density");
    evalLabelC2.setText("Size Class");
    evalLabelC1.setText("Species");
    evalORRBC0.setMargin(new Insets(0, 0, 0, 0));
    evalORRBC0.setText("OR");
    evalORRBC0.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRBC0_actionPerformed(e);
      }
    });
    gridLayout18.setRows(2);
    evalBoolPanelC0.setLayout(gridLayout18);
    evalANDRBC0.setMargin(new Insets(0, 0, 0, 0));
    evalANDRBC0.setSelected(true);
    evalANDRBC0.setText("AND");
    evalANDRBC0.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRBC0_actionPerformed(e);
      }
    });
    evalANDRBC1.setActionMap(null);
    evalANDRBC1.setMargin(new Insets(0, 0, 0, 0));
    evalANDRBC1.setSelected(true);
    evalANDRBC1.setText("AND");
    evalANDRBC1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRBC1_actionPerformed(e);
      }
    });
    gridLayout17.setRows(2);
    evalORRBC1.setMargin(new Insets(0, 0, 0, 0));
    evalORRBC1.setText("OR");
    evalORRBC1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRBC1_actionPerformed(e);
      }
    });
    evalBoolPanelC1.setLayout(gridLayout17);
    evalBoolPanelC3.setLayout(gridLayout15);
    evalORRBC3.setMargin(new Insets(0, 0, 0, 0));
    evalORRBC3.setText("OR");
    evalORRBC3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRBC3_actionPerformed(e);
      }
    });
    evalANDRBC3.setMargin(new Insets(0, 0, 0, 0));
    evalANDRBC3.setSelected(true);
    evalANDRBC3.setText("AND");
    evalANDRBC3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRBC3_actionPerformed(e);
      }
    });
    gridLayout15.setRows(2);
    evalBoolPanelC1.setBorder(BorderFactory.createEtchedBorder());
    evalBoolPanelC3.setBorder(BorderFactory.createEtchedBorder());
    evalBoolPanelC0.setBorder(BorderFactory.createEtchedBorder());
    evalOrderPanelC.setBorder(evalOrderTitle);
    evalORRBC2.setMargin(new Insets(0, 0, 0, 0));
    evalORRBC2.setText("OR");
    evalORRBC2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalORRBC2_actionPerformed(e);
      }
    });
    evalANDRBC2.setMargin(new Insets(0, 0, 0, 0));
    evalANDRBC2.setSelected(true);
    evalANDRBC2.setText("AND");
    evalANDRBC2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        evalANDRBC2_actionPerformed(e);
      }
    });
    gridLayout16.setRows(2);
    menuChangeDelete.setEnabled(false);
    menuChangeDelete.setText("Delete Current Rule");
    menuChangeDelete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuChangeDelete_actionPerformed(e);
      }
    });
    evalLabelC5.setText("Call Function");
    evalBoolPanelC4.setLayout(gridLayout27);
    gridLayout27.setRows(2);
    evalANDRBC4.setMargin(new Insets(0, 0, 0, 0));
    evalANDRBC4.setSelected(true);
    evalANDRBC4.setText("AND");
    evalORRBC4.setMargin(new Insets(0, 0, 0, 0));
    evalORRBC4.setText("OR");
    evalBoolPanelC4.setBorder(BorderFactory.createEtchedBorder());
    callFunctionCheckBoxC.setSelected(true);
    callFunctionCheckBoxC.setText("Call Function");
    callFunctionCheckBoxC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        callFunctionCheckBoxC_actionPerformed(e);
      }
    });
    callFunctionEvalInnerPanelC.setLayout(borderLayout7);
    callFunctionEvalInnerPanelC.setBorder(BorderFactory.createEtchedBorder());
    callFunctionEvalCBPanelC.setLayout(flowLayout32);
    flowLayout32.setAlignment(FlowLayout.LEFT);
    flowLayout32.setVgap(0);
    borderLayout7.setVgap(5);
    callFunctionEvalTextC.setEditable(false);
    callFunctionEvalTextC.setColumns(105);
    callFunctionEvalTextC.setRows(3);
    callFunctionEvalCBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        callFunctionEvalCBC_actionPerformed(e);
      }
    });
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(0);
    flowLayout3.setVgap(0);
    flowLayout38.setAlignment(FlowLayout.LEFT);
    flowLayout38.setHgap(0);
    flowLayout38.setVgap(0);
    flowLayout37.setAlignment(FlowLayout.LEFT);
    flowLayout37.setHgap(0);
    flowLayout37.setVgap(0);
    flowLayout36.setAlignment(FlowLayout.LEFT);
    flowLayout36.setHgap(0);
    flowLayout36.setVgap(0);
    flowLayout39.setAlignment(FlowLayout.LEFT);
    flowLayout39.setHgap(0);
    flowLayout39.setVgap(0);
    flowLayout41.setAlignment(FlowLayout.LEFT);
    flowLayout41.setHgap(0);
    flowLayout41.setVgap(0);
    flowLayout43.setAlignment(FlowLayout.LEFT);
    flowLayout43.setHgap(0);
    flowLayout43.setVgap(0);
    flowLayout45.setAlignment(FlowLayout.LEFT);
    flowLayout45.setHgap(0);
    flowLayout45.setVgap(0);
    flowLayout13.setAlignment(FlowLayout.LEFT);
    flowLayout13.setHgap(0);
    flowLayout13.setVgap(0);
    flowLayout16.setAlignment(FlowLayout.LEFT);
    flowLayout16.setHgap(0);
    flowLayout16.setVgap(0);
    flowLayout19.setAlignment(FlowLayout.LEFT);
    flowLayout19.setHgap(0);
    flowLayout19.setVgap(0);
    flowLayout51.setAlignment(FlowLayout.LEFT);
    flowLayout51.setHgap(0);
    flowLayout51.setVgap(0);
    speciesTextC.setEditable(false);
    speciesTextC.setColumns(105);
    speciesTextC.setRows(1);
    speciesSelectPickPBC.setMargin(new Insets(0, 0, 0, 0));
    speciesSelectPickPBC.setText("Choose Items from List");
    speciesSelectPickPBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesSelectPickPBC_actionPerformed(e);
      }
    });
    speciesControlPanelC.setLayout(flowLayout42);
    flowLayout42.setAlignment(FlowLayout.LEFT);
    flowLayout42.setVgap(0);
    speciesRBPanelC.setBorder(BorderFactory.createEtchedBorder());
    sizeClassControlPanelC.setLayout(flowLayout49);
    flowLayout49.setAlignment(FlowLayout.LEFT);
    flowLayout49.setVgap(0);
    sizeClassSelectPickPBC.setMargin(new Insets(0, 0, 0, 0));
    sizeClassSelectPickPBC.setText("Choose Items from List");
    sizeClassSelectPickPBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassSelectPickPBC_actionPerformed(e);
      }
    });
    sizeClassRBPanelC.setBorder(BorderFactory.createEtchedBorder());
    densityControlPanelC.setLayout(flowLayout44);
    flowLayout44.setAlignment(FlowLayout.LEFT);
    flowLayout44.setVgap(0);
    htGrpControlPanelC.setLayout(flowLayout48);
    flowLayout48.setAlignment(FlowLayout.LEFT);
    flowLayout48.setVgap(0);
    densitySelectPickPBC.setMargin(new Insets(0, 0, 0, 0));
    densitySelectPickPBC.setText("Choose Items from List");
    densitySelectPickPBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densitySelectPickPBC_actionPerformed(e);
      }
    });
    htGrpSelectPickPBC.setMargin(new Insets(0, 0, 0, 0));
    htGrpSelectPickPBC.setText("Choose Items from List");
    htGrpSelectPickPBC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpSelectPickPBC_actionPerformed(e);
      }
    });
    densityRBPanelC.setBorder(BorderFactory.createEtchedBorder());
    htGrpRBPanelC.setBorder(BorderFactory.createEtchedBorder());
    htGrpPanel.setBorder(BorderFactory.createEtchedBorder());
    htGrpControlPanel.setLayout(flowLayout40);
    flowLayout40.setAlignment(FlowLayout.LEFT);
    flowLayout40.setVgap(0);
    htGrpSelectPickPB.setMargin(new Insets(0, 0, 0, 0));
    htGrpSelectPickPB.setText("Choose Items from List");
    htGrpSelectPickPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpSelectPickPB_actionPerformed(e);
      }
    });
    densityControlPanel.setLayout(flowLayout46);
    flowLayout46.setAlignment(FlowLayout.LEFT);
    flowLayout46.setVgap(0);
    sizeClassControlPanel.setLayout(flowLayout47);
    flowLayout47.setAlignment(FlowLayout.LEFT);
    flowLayout47.setVgap(0);
    speciesControlPanel.setLayout(flowLayout50);
    flowLayout50.setAlignment(FlowLayout.LEFT);
    flowLayout50.setVgap(0);
    speciesSelectPickPB.setMargin(new Insets(0, 0, 0, 0));
    speciesSelectPickPB.setText("Choose Items from List");
    speciesSelectPickPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesSelectPickPB_actionPerformed(e);
      }
    });
    sizeClassSelectPickPB.setMargin(new Insets(0, 0, 0, 0));
    sizeClassSelectPickPB.setText("Choose Items from List");
    sizeClassSelectPickPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sizeClassSelectPickPB_actionPerformed(e);
      }
    });
    densitySelectPickPB.setMargin(new Insets(0, 0, 0, 0));
    densitySelectPickPB.setText("Choose Items from List");
    densitySelectPickPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        densitySelectPickPB_actionPerformed(e);
      }
    });
    htGrpRBpanel.setBorder(BorderFactory.createEtchedBorder());
    densityText.setEditable(false);
    densityText.setColumns(105);
    densityText.setRows(1);
    sizeClassText.setEditable(false);
    sizeClassText.setColumns(105);
    sizeClassText.setRows(1);
    speciesText.setEditable(false);
    speciesText.setColumns(105);
    speciesText.setRows(1);
    htGrpText.setEditable(false);
    htGrpText.setColumns(105);
    htGrpText.setRows(1);
    htGrpTextC.setEditable(false);
    htGrpTextC.setColumns(105);
    sizeClassTextC.setEditable(false);
    sizeClassTextC.setColumns(105);
    densityTextC.setEditable(false);
    densityTextC.setColumns(105);
    toTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        toTabbedPane_stateChanged(e);
      }
    });
    menuUtility.setText("Utility");
    menuPrintTreatment.setText("Display Printed Treatment");
    menuPrintTreatment.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPrintTreatment_actionPerformed(e);
      }
    });
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
    menuFileCreate.setText("Create Treatment");
    menuFileCreate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileCreate_actionPerformed(e);
      }
    });
    isEffectiveProbText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isEffectiveProbText_actionPerformed(e);
      }
    });
    isEffectiveProbText.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent focusEvent) {
        isEffectiveProbText_focusLost(focusEvent);
      }
    });
    htGrpPanelC.add(htGrpControlPanelC, null);
    speciesScrollPaneC.getViewport().add(speciesTextC, null);
    northPanelC.add(topChangePanel, null);
    topChangePanel.add(checkBoxPanelC, null);
    checkBoxPanelC.add(htGrpCBC, null);
    checkBoxPanelC.add(speciesCBC, null);
    checkBoxPanelC.add(sizeClassCBC, null);
    checkBoxPanelC.add(densityCBC, null);
    checkBoxPanelC.add(stateCBC, null);
    checkBoxPanelC.add(callFunctionCheckBoxC, null);
    topChangePanel.add(rulePanel, null);
    rulePanel.add(prevPB, null);
    rulePanel.add(nextPB, null);
    rulePanel.add(rulePBPanel, null);
    rulePBPanel.add(showTextPB, null);
    northPanelC.add(evalOrderPanelC, null);
    northMainPanel.add(treatmentLabel, null);
    northMainPanel.add(treatmentCB, null);
    mainInnerPanel.add(tabbedPane, BorderLayout.CENTER);
    htGrpRBPanelC.add(htGrpOneOfRBC, null);
    htGrpRBPanelC.add(htGrpNotOneOfRBC, null);
    htGrpPanelC.add(htGrpScrollPaneC, null);
    htGrpScrollPaneC.getViewport().add(htGrpTextC, null);
    getContentPane().add(mainPanel);
    mainPanel.add(mainInnerPanel,  BorderLayout.NORTH);
    tabbedPane.add(feasibilityMainPane,  "Feasibility");
    tabbedPane.add(changePane,   "Change");
    changePane.add(changeMainPanel,  BorderLayout.NORTH);
    speciesRBPanelC.add(speciesOneOfRBC, null);
    speciesRBPanelC.add(speciesNotOneOfRBC, null);
    speciesPanelC.add(speciesControlPanelC, null);
    speciesControlPanelC.add(speciesRBPanelC, null);
    speciesControlPanelC.add(speciesSelectPickPBC, null);
    speciesPanelC.add(speciesScrollPaneC, null);
    sizeClassRBPanelC.add(sizeClassOneOfRBC, null);
    sizeClassRBPanelC.add(sizeClassNotOneOfRBC, null);
    sizeClassPanelC.add(sizeClassControlPanelC, null);
    sizeClassPanelC.add(sizeClassScrollPaneC, null);
    sizeClassScrollPaneC.getViewport().add(sizeClassTextC, null);
    densityRBPanelC.add(densityOneOfRBC, null);
    densityRBPanelC.add(densityNotOneOfRBC, null);
    densityPanelC.add(densityControlPanelC, null);
    densityPanelC.add(densityScrollPaneC, null);
    densityScrollPaneC.getViewport().add(densityTextC, null);
    changeMainPanel.add(southPanelC, BorderLayout.SOUTH);
    southPanelC.add(centerInnerPanelC, null);
    changeMainPanel.add(centerPanelC, BorderLayout.CENTER);
    statePanelC.add(stateValueC, null);
    statePanelC.add(statePickPBC, null);
    changeMainPanel.add(northPanelC, BorderLayout.NORTH);
    centerInnerPanelC.add(toPanel, null);
    mainInnerPanel.add(northMainPanel,  BorderLayout.NORTH);
    htGrpRBpanel.add(htGrpOneOfRB, null);
    htGrpRBpanel.add(htGrpNotOneOfRB, null);
    htGrpPanel.add(htGrpControlPanel, null);
    htGrpControlPanel.add(htGrpRBpanel, null);
    htGrpControlPanel.add(htGrpSelectPickPB, null);
    htGrpPanel.add(htGrpScrollPane, null);
    centerPanel.add(conditionTabbedPane, null);
    speciesRBPanel.add(speciesOneOfRB, null);
    speciesRBPanel.add(speciesNotOneOfRB, null);
    speciesPanel.add(speciesControlPanel, null);
    speciesPanel.add(speciesScrollPane, null);
    sizeClassRBPanel.add(sizeClassOneOfRB, null);
    sizeClassRBPanel.add(sizeClassNotOneOfRB, null);
    sizeClassPanel.add(sizeClassControlPanel, null);
    sizeClassPanel.add(sizeClassScrollPane, null);
    sizeClassScrollPane.getViewport().add(sizeClassText, null);
    densityRBPanel.add(densityOneOfRB, null);
    densityRBPanel.add(densityNotOneOfRB, null);
    densityPanel.add(densityControlPanel, null);
    densityPanel.add(densityScrollPane, null);
    densityScrollPane.getViewport().add(densityText, null);
    feasibilityMainPanel.add(southPanel, BorderLayout.SOUTH);
    southPanel.add(centerInnerPanel, null);
    feasibilityMainPanel.add(centerPanel, BorderLayout.CENTER);
    statePanel.add(stateValue, null);
    statePanel.add(statePickPB, null);
    centerInnerPanel.add(stateOuterPanel, null);
    centerInnerPanel.add(callFunctionOuterPanel, null);
    callFunctionPanel.add(callFunctionText, BorderLayout.CENTER);
    callFunctionPanel.add(callFunctionCBPanel, BorderLayout.NORTH);
    callFunctionCBPanel.add(callFunctionCB, null);
    feasibilityMainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(topFeasiblePanel, null);
    topFeasiblePanel.add(checkBoxPanel, null);
    checkBoxPanel.add(htGrpCB, null);
    checkBoxPanel.add(speciesCB, null);
    checkBoxPanel.add(sizeClassCB, null);
    checkBoxPanel.add(densityCB, null);
    checkBoxPanel.add(stateCB, null);
    checkBoxPanel.add(callFunctionCheckBox, null);
    topFeasiblePanel.add(feasiblePanel, null);
    feasiblePanel.add(feasibleRB, null);
    feasiblePanel.add(feasibleNOTRB, null);
    northPanel.add(evalOrderPanel, null);
    feasibilityMainPane.add(feasibilityMainPanel,  BorderLayout.NORTH);
    htGrpRBGroup.add(htGrpOneOfRB);
    htGrpRBGroup.add(htGrpNotOneOfRB);
    speciesRBGroup.add(speciesOneOfRB);
    speciesRBGroup.add(speciesNotOneOfRB);
    sizeClassRBGroup.add(sizeClassOneOfRB);
    sizeClassRBGroup.add(sizeClassNotOneOfRB);
    densityRBGroup.add(densityOneOfRB);
    densityRBGroup.add(densityNotOneOfRB);
    htGrpRBGroupC.add(htGrpOneOfRBC);
    htGrpRBGroupC.add(htGrpNotOneOfRBC);
    speciesRBGroupC.add(speciesOneOfRBC);
    speciesRBGroupC.add(speciesNotOneOfRBC);
    sizeClassRBGroupC.add(sizeClassOneOfRBC);
    sizeClassRBGroupC.add(sizeClassNotOneOfRBC);
    densityRBGroupC.add(densityOneOfRBC);
    densityRBGroupC.add(densityNotOneOfRBC);
    menuBar.add(menuFile);
    menuBar.add(menuFeasibility);
    menuBar.add(menuChange);
    menuBar.add(menuUtility);
    menuBar.add(menuKnowledgeSource);
    menuFile.add(menuFileCreate);
    menuFile.addSeparator();
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuFileOldFormat);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuLoadDefault);
    menuFile.add(menuFileDefaultAll);
    menuFile.addSeparator();
    menuFile.add(menuQuit);
    menuFeasibility.add(menuFeasibilityEvalOrder);
    menuChange.add(menuChangeNew);
    menuChange.add(menuChangeEvalOrder);
    menuChange.addSeparator();
    menuChange.add(menuChangeDelete);
    stateToItemPanel.add(stateToItemInnerPanel, null);
    sizeClassToPanel.add(sizeClassToRB, null);
    sizeClassToPanel.add(sizeClassToCB, null);
    stateToItemInnerPanel.add(speciesToPanel, null);
    stateToItemInnerPanel.add(sizeClassToPanel, null);
    speciesToPanel.add(speciesToRB, null);
    speciesToPanel.add(speciesToCB, null);
    stateToItemInnerPanel.add(densityToPanel, null);
    densityToPanel.add(densityToRB, null);
    densityToPanel.add(densityToCB, null);
    toTabbedPane.add(toStateItemPanel, "To State Item");
    toStateItemPanel.add(stateToItemPanel, null);
    toTabbedPane.add(toStatePanel,   "To State");
    toStatePanel.add(stateToOuterPanel, null);
    stateToOuterPanel.add(stateToPanel, null);
    stateToPanel.add(stateToValue, null);
    stateToPanel.add(statePickToPB, null);
    toTabbedPane.add(toFunctionCallPanel,    "To Function Call");
    toFunctionCallPanel.add(callFunctionOuterPanelC, null);
    callFunctionOuterPanelC.add(callFunctionPanelC, null);
    callFunctionPanelC.add(callFunctionTextC, BorderLayout.CENTER);
    callFunctionPanelC.add(callFunctionCBPanelC, BorderLayout.NORTH);
    callFunctionCBPanelC.add(callFunctionCBC, null);
    toPanel.add(toTabbedPane, null);
    feasibleRBGroup.add(feasibleRB);
    feasibleRBGroup.add(feasibleNOTRB);
    evalBoolPanel4.add(evalANDRB4, null);
    evalBoolPanel4.add(evalORRB4, null);
    evalBoolPanel3.add(evalANDRB3, null);
    evalBoolPanel3.add(evalORRB3, null);
    evalBoolPanel2.add(evalANDRB2, null);
    evalBoolPanel2.add(evalORRB2, null);
    evalBoolPanel1.add(evalANDRB1, null);
    evalBoolPanel1.add(evalORRB1, null);
    evalBoolPanel0.add(evalANDRB0, null);
    evalBoolPanel0.add(evalORRB0, null);
    evalOrderPanel.add(evalLabel0, null);
    evalOrderPanel.add(evalBoolPanel0, null);
    evalOrderPanel.add(evalLabel1, null);
    evalOrderPanel.add(evalBoolPanel1, null);
    evalOrderPanel.add(evalLabel2, null);
    evalOrderPanel.add(evalBoolPanel2, null);
    evalOrderPanel.add(evalLabel3, null);
    evalOrderPanel.add(evalBoolPanel3, null);
    evalOrderPanel.add(evalLabel4, null);
    evalOrderPanel.add(evalBoolPanel4, null);
    evalOrderPanel.add(evalLabel5, null);
    evalBoolRBGroup0.add(evalANDRB0);
    evalBoolRBGroup0.add(evalORRB0);
    evalBoolRBGroup1.add(evalANDRB1);
    evalBoolRBGroup1.add(evalORRB1);
    evalBoolRBGroup2.add(evalANDRB2);
    evalBoolRBGroup2.add(evalORRB2);
    evalBoolRBGroup3.add(evalANDRB3);
    evalBoolRBGroup3.add(evalORRB3);
    evalBoolRBGroup4.add(evalANDRB4);
    evalBoolRBGroup4.add(evalORRB4);
    evalBoolPanelC1.add(evalANDRBC1, null);
    evalBoolPanelC1.add(evalORRBC1, null);
    evalBoolPanelC3.add(evalANDRBC3, null);
    evalBoolPanelC3.add(evalORRBC3, null);
    evalBoolPanelC0.add(evalANDRBC0, null);
    evalBoolPanelC0.add(evalORRBC0, null);
    evalBoolPanelC2.setLayout(gridLayout16);
    evalBoolPanelC2.setBorder(BorderFactory.createEtchedBorder());
    evalBoolPanelC2.add(evalANDRBC2, null);
    evalBoolPanelC2.add(evalORRBC2, null);

    evalOrderPanelC.add(evalLabelC0, null);
    evalOrderPanelC.add(evalBoolPanelC0, null);
    evalOrderPanelC.add(evalLabelC1, null);
    evalOrderPanelC.add(evalBoolPanelC1, null);
    evalOrderPanelC.add(evalLabelC2, null);
    evalOrderPanelC.add(evalBoolPanelC2, null);
    evalOrderPanelC.add(evalLabelC3, null);
    evalOrderPanelC.add(evalBoolPanelC3, null);
    evalOrderPanelC.add(evalLabelC4, null);
    evalOrderPanelC.add(evalBoolPanelC4, null);
    evalOrderPanelC.add(evalLabelC5, null);

    evalBoolRBGroupC0.add(evalANDRBC0);
    evalBoolRBGroupC0.add(evalORRBC0);
    evalBoolRBGroupC1.add(evalANDRBC1);
    evalBoolRBGroupC1.add(evalORRBC1);
    evalBoolRBGroupC2.add(evalANDRBC2);
    evalBoolRBGroupC2.add(evalORRBC2);
    evalBoolRBGroupC3.add(evalANDRBC3);
    evalBoolRBGroupC3.add(evalORRBC3);
    evalBoolPanelC4.add(evalANDRBC4, null);
    evalBoolPanelC4.add(evalORRBC4, null);
    evalBoolRBGroupC4.add(evalANDRBC4);
    evalBoolRBGroupC4.add(evalORRBC4);
    callFunctionEvalInnerPanelC.add(callFunctionEvalCBPanelC,  BorderLayout.NORTH);
    callFunctionEvalCBPanelC.add(callFunctionEvalCBC, null);
    callFunctionEvalCBPanelC.add(isEffectiveProbText);
    callFunctionEvalInnerPanelC.add(callFunctionEvalTextC, BorderLayout.CENTER);
    conditionTabbedPaneC.add(htGrpPanelC, "Ecological Grouping");
    conditionTabbedPaneC.add(speciesPanelC, "Species");
    conditionTabbedPaneC.add(sizeClassPanelC, "Size Class");
    conditionTabbedPaneC.add(densityPanelC, "Density");
    centerPanelC.add(conditionTabbedPaneC, null);
    conditionTabbedPaneC.add(statePanelC, "Current State");
    conditionTabbedPaneC.add(callFunctionEvalInnerPanelC,  "Call Function");
    sizeClassControlPanelC.add(sizeClassRBPanelC, null);
    sizeClassControlPanelC.add(sizeClassSelectPickPBC, null);
    densityControlPanelC.add(densityRBPanelC, null);
    densityControlPanelC.add(densitySelectPickPBC, null);
    htGrpControlPanelC.add(htGrpRBPanelC, null);
    htGrpControlPanelC.add(htGrpSelectPickPBC, null);
    conditionTabbedPane.add(htGrpPanel, "Ecological Grouping");
    conditionTabbedPane.add(speciesPanel,  "Species");
    conditionTabbedPane.add(sizeClassPanel,  "Size Class");
    conditionTabbedPane.add(densityPanel,  "Density");
    conditionTabbedPane.add(statePanel,  "Current State");
    conditionTabbedPane.add(callFunctionPanel,  "Call Function");
    speciesControlPanel.add(speciesRBPanel, null);
    speciesControlPanel.add(speciesSelectPickPB, null);
    sizeClassControlPanel.add(sizeClassRBPanel, null);
    sizeClassControlPanel.add(sizeClassSelectPickPB, null);
    densityControlPanel.add(densityRBPanel, null);
    densityControlPanel.add(densitySelectPickPB, null);
    speciesScrollPane.getViewport().add(speciesText, null);
    htGrpScrollPane.getViewport().add(htGrpText, null);
    stateRBGroupC.add(speciesToRB);
    stateRBGroupC.add(sizeClassToRB);
    stateRBGroupC.add(densityToRB);
    menuUtility.add(menuPrintTreatment);
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);
  }

  private void initialize() {
    inInit = true;

    isEffectiveProbText.setVisible(false);

    RegionalZone zone = Simpplle.getCurrentZone();

    fillComboBox(treatmentCB,zone.getLegalTreatments());

    htGrpItems     = ListItem.fillList(HabitatTypeGroup.getAllLoadedTypes());
    speciesItems   = ListItem.fillList(zone.getAllSpecies());
    sizeClassItems = ListItem.fillList(zone.getAllSizeClass());
    densityItems   = ListItem.fillList(zone.getAllDensity());

    setEnabledChangeTab(true);

    fillComboBox(callFunctionCB,Treatment.getFeasibilityFunctions());
    String desc = Treatment.getFeasibilityFunctionDesc((String)callFunctionCB.getSelectedItem());
    callFunctionText.setText(desc);

    fillComboBox(callFunctionCBC,Treatment.getChangeFunctions());
    desc = Treatment.getChangeFunctionDesc((String)callFunctionCBC.getSelectedItem());
    callFunctionTextC.setText(desc);

    fillComboBox(callFunctionEvalCBC,Treatment.getChangeEvalFunctions());
    desc = Treatment.getChangeEvalFunctionDesc((String)callFunctionEvalCBC.getSelectedItem());
    callFunctionEvalTextC.setText(desc);

    fillComboBox(speciesToCB,zone.getAllSpecies());
    fillComboBox(sizeClassToCB,zone.getAllSizeClass());
    fillComboBox(densityToCB,zone.getAllDensity());

    evalANDRB[0] = evalANDRB0;
    evalANDRB[1] = evalANDRB1;
    evalANDRB[2] = evalANDRB2;
    evalANDRB[3] = evalANDRB3;
    evalANDRB[4] = evalANDRB4;

    evalORRB[0] = evalORRB0;
    evalORRB[1] = evalORRB1;
    evalORRB[2] = evalORRB2;
    evalORRB[3] = evalORRB3;
    evalORRB[4] = evalORRB4;

    evalANDRBC[0] = evalANDRBC0;
    evalANDRBC[1] = evalANDRBC1;
    evalANDRBC[2] = evalANDRBC2;
    evalANDRBC[3] = evalANDRBC3;
    evalANDRBC[4] = evalANDRBC4;

    evalORRBC[0] = evalORRBC0;
    evalORRBC[1] = evalORRBC1;
    evalORRBC[2] = evalORRBC2;
    evalORRBC[3] = evalORRBC3;
    evalORRBC[4] = evalORRBC4;

    evalBoolPanel[0] = evalBoolPanel0;
    evalBoolPanel[1] = evalBoolPanel1;
    evalBoolPanel[2] = evalBoolPanel2;
    evalBoolPanel[3] = evalBoolPanel3;
    evalBoolPanel[4] = evalBoolPanel4;

    evalBoolPanelC[0] = evalBoolPanelC0;
    evalBoolPanelC[1] = evalBoolPanelC1;
    evalBoolPanelC[2] = evalBoolPanelC2;
    evalBoolPanelC[3] = evalBoolPanelC3;
    evalBoolPanelC[4] = evalBoolPanelC4;

    evalLabel[0] = evalLabel0;
    evalLabel[1] = evalLabel1;
    evalLabel[2] = evalLabel2;
    evalLabel[3] = evalLabel3;
    evalLabel[4] = evalLabel4;
    evalLabel[5] = evalLabel5;

    evalLabelC[0] = evalLabelC0;
    evalLabelC[1] = evalLabelC1;
    evalLabelC[2] = evalLabelC2;
    evalLabelC[3] = evalLabelC3;
    evalLabelC[4] = evalLabelC4;
    evalLabelC[5] = evalLabelC5;

    newTreatment();

    /*
    Check to make sure the preferred size is not large than
    the available space on the screen.  If it is then set the
    size to the screen width and a little smaller than the
    screen height (to accomodate the taskbar in windows).
    */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlg = getPreferredSize();

    int width  = (dlg.width > screen.width)   ? screen.width  : dlg.width;
    int height = (dlg.height > screen.height) ? screen.height - 75 : dlg.height;
    setSize(width,height);

    int index = tabbedPane.indexOfTab("Feasibility");
    tabbedPane.setEnabledAt(index,true);

    updateDialog();
    inInit = false;
  }

  private void updateDialog() {
//    setSize(getPreferredSize());
    if (JSimpplle.isCurrentOS(JSimpplle.MAC_OS_X) == false) {
      update(getGraphics());
    }
    File treatFile = SystemKnowledge.getFile(SystemKnowledge.TREATMENT_LOGIC);
    menuFileSave.setEnabled((treatFile != null));
    menuFileClose.setEnabled((treatFile != null));
  }

  private void updateComboBoxValues(JComboBox cb, Object[] items) {
    inInit = true;
    fillComboBox(cb,items);
    inInit = false;
  }
  private void fillComboBox(JComboBox cb, Object[] items) {
    cb.removeAllItems();
    for(int i=0; i<items.length; i++) {
      cb.addItem(items[i]);
    }
    cb.setSelectedIndex(0);
  }

  private void fillComboBox(JComboBox cb, Vector items) {
    cb.removeAllItems();
    for(int i=0; i<items.size(); i++) {
      cb.addItem(items.elementAt(i));
    }
    cb.setSelectedIndex(0);
  }


  private String chooseVegetativeType() {
    String title   = "Vegetative Type Chooser";

    VegetativeTypeChooser dlg =
      new VegetativeTypeChooser(this,title,true);

    dlg.setVisible(true);

    VegetativeType selection = dlg.getSelection();
    return ( (selection != null) ? selection.toString() : null);
  }

  private void newTreatment() {
    selectedTreatment = (TreatmentType)treatmentCB.getSelectedItem();
    feasibilityLogic  = Treatment.getFeasibilityLogic(selectedTreatment);
    changeLogic       = Treatment.getChangeLogic(selectedTreatment);

    changeLogicIndex = 0;
    initializeChange();

    int i;
    for(i=0; i<htGrpItems.length; i++) {
      htGrpItems[i].selected = feasibilityLogic.isSelectedHtGrp(htGrpItems[i].item);
    }
    for(i=0; i<speciesItems.length; i++) {
      speciesItems[i].selected = feasibilityLogic.isSelectedSpecies(speciesItems[i].item);
    }
    for(i=0; i<sizeClassItems.length; i++) {
      sizeClassItems[i].selected = feasibilityLogic.isSelectedSizeClass(sizeClassItems[i].item);
    }
    for(i=0; i<densityItems.length; i++) {
      densityItems[i].selected = feasibilityLogic.isSelectedDensity(densityItems[i].item);
    }

    updateConditionalText(htGrpText, htGrpItems);
    updateConditionalText(speciesText, speciesItems);
    updateConditionalText(sizeClassText, sizeClassItems);
    updateConditionalText(densityText, densityItems);

    htGrpCB.setSelected(feasibilityLogic.useHtGrp());
    speciesCB.setSelected(feasibilityLogic.useSpecies());
    sizeClassCB.setSelected(feasibilityLogic.useSizeClass());
    densityCB.setSelected(feasibilityLogic.useDensity());
    stateCB.setSelected(feasibilityLogic.useState());
    callFunctionCheckBox.setSelected(feasibilityLogic.useCallFunction());

    htGrpCB_actionPerformed(null);
    speciesCB_actionPerformed(null);
    sizeClassCB_actionPerformed(null);
    densityCB_actionPerformed(null);
    stateCB_actionPerformed(null);
    callFunctionCheckBox_actionPerformed(null);

    if (feasibilityLogic.useState()) {
      stateValue.setText(feasibilityLogic.getState());
    }
    else { stateValue.setText("                                 "); }

    if (feasibilityLogic.useCallFunction()) {
      callFunctionCB.setSelectedItem(feasibilityLogic.getCallFunction());
    }
    else { callFunctionCB.setSelectedIndex(0); }

    String evalBool;
    for(i=0; i<evalANDRB.length; i++) {
      evalBool = feasibilityLogic.getBoolChoice(i);
      evalANDRB[i].setSelected((evalBool == TreatmentLogicData.AND));
      evalORRB[i].setSelected((evalBool == TreatmentLogicData.OR));
    }

    feasibleRB.setSelected(feasibilityLogic.isFeasible());
    feasibleNOTRB.setSelected(feasibilityLogic.isFeasible() == false);

    htGrpOneOfRB.setSelected(feasibilityLogic.isOneOfHtGrp());
    htGrpNotOneOfRB.setSelected(feasibilityLogic.isOneOfHtGrp() == false);

    speciesOneOfRB.setSelected(feasibilityLogic.isOneOfSpecies());
    speciesNotOneOfRB.setSelected(feasibilityLogic.isOneOfSpecies() == false);

    sizeClassOneOfRB.setSelected(feasibilityLogic.isOneOfSizeClass());
    sizeClassNotOneOfRB.setSelected(feasibilityLogic.isOneOfSizeClass() == false);

    densityOneOfRB.setSelected(feasibilityLogic.isOneOfDensity());
    densityNotOneOfRB.setSelected(feasibilityLogic.isOneOfDensity() == false);

    setSelectedTab(conditionTabbedPane,feasibilityLogic);
  }

  private void updateSelectedTab(JTabbedPane tp, TreatmentLogicData treatData,
                                 boolean selected, int index)
  {
    if (selected) {
      tp.setSelectedIndex(index);
    }
    else if (index == tp.getSelectedIndex()) {
      // if the one we disabled is not the current focused one then do this.
      setSelectedTab(tp,treatData);
    }
  }
  private void updateSelectedTabF(boolean selected, int index) {
    updateSelectedTab(conditionTabbedPane,feasibilityLogic,selected,index);
  }
  private void updateSelectedTabC(boolean selected, int index) {
    updateSelectedTab(conditionTabbedPaneC,changeLogicRule,selected,index);
  }

  private void setSelectedTab(JTabbedPane tp, TreatmentLogicData treatData) {
    if (treatData.useHtGrp()) {
      setSelectedTab(tp,"Ecological Grouping");
    }
    else if (treatData.useSpecies()) {
      setSelectedTab(tp,"Species");
    }
    else if (treatData.useSizeClass()) {
      setSelectedTab(tp,"Size Class");
    }
    else if (treatData.useDensity()) {
      setSelectedTab(tp,"Density");
    }
    else if (treatData.useState()) {
      setSelectedTab(tp,"Current State");
    }
    else if (treatData.useCallFunction()) {
      setSelectedTab(tp,"Call Function");
    }
    else {
      setSelectedTab(tp,"Ecological Grouping");
    }
  }
  private void setSelectedTab(JTabbedPane tp, String title) {
    tp.setSelectedIndex(tp.indexOfTab(title));
  }

  private void initializeChange() {
    inInitChange = true;
    changeLogicRule = null;
    if (changeLogic.size() != 0) {
      changeLogicRule = (ChangeLogic)changeLogic.elementAt(changeLogicIndex);
    }

    if (changeLogicRule != null) {
      setEnabledChangeTab(true);
      htGrpCBC.setSelected(changeLogicRule.useHtGrp());
      speciesCBC.setSelected(changeLogicRule.useSpecies());
      sizeClassCBC.setSelected(changeLogicRule.useSizeClass());
      densityCBC.setSelected(changeLogicRule.useDensity());
      stateCBC.setSelected(changeLogicRule.useState());
      menuChangeDelete.setEnabled(true);
    }
    else {
      setEnabledChangeTab(false);
      htGrpCBC.setSelected(false);
      speciesCBC.setSelected(false);
      sizeClassCBC.setSelected(false);
      densityCBC.setSelected(false);
      stateCBC.setSelected(false);
      callFunctionCheckBoxC.setSelected(false);
      menuChangeDelete.setEnabled(false);

      tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Feasibility"));
      return;
    }

    RegionalZone zone = Simpplle.getCurrentZone();
    htGrpItemsC     = ListItem.fillList(HabitatTypeGroup.getAllLoadedTypes());
    speciesItemsC   = ListItem.fillList(zone.getAllSpecies());
    sizeClassItemsC = ListItem.fillList(zone.getAllSizeClass());
    densityItemsC   = ListItem.fillList(zone.getAllDensity());

    /*
    htGrpItemsC     = ListItem.fillList(Treatment.getValidHabitatTypeGroups(selectedTreatment));
    speciesItemsC   = ListItem.fillList(Treatment.getValidSpecies(selectedTreatment));
    sizeClassItemsC = ListItem.fillList(Treatment.getValidSizeClass(selectedTreatment));
    densityItemsC   = ListItem.fillList(Treatment.getValidDensity(selectedTreatment));
    */

    int i;
    for(i=0; i<htGrpItemsC.length; i++) {
      htGrpItemsC[i].selected = changeLogicRule.isSelectedHtGrp(htGrpItemsC[i].item);
    }
    for(i=0; i<speciesItemsC.length; i++) {
      speciesItemsC[i].selected = changeLogicRule.isSelectedSpecies(speciesItemsC[i].item);
    }
    for(i=0; i<sizeClassItemsC.length; i++) {
      sizeClassItemsC[i].selected = changeLogicRule.isSelectedSizeClass(sizeClassItemsC[i].item);
    }
    for(i=0; i<densityItemsC.length; i++) {
      densityItemsC[i].selected = changeLogicRule.isSelectedDensity(densityItemsC[i].item);
    }

    updateConditionalText(htGrpTextC, htGrpItemsC);
    updateConditionalText(speciesTextC, speciesItemsC);
    updateConditionalText(sizeClassTextC, sizeClassItemsC);
    updateConditionalText(densityTextC, densityItemsC);

    htGrpCBC.setSelected(changeLogicRule.useHtGrp());
    speciesCBC.setSelected(changeLogicRule.useSpecies());
    sizeClassCBC.setSelected(changeLogicRule.useSizeClass());
    densityCBC.setSelected(changeLogicRule.useDensity());
    stateCBC.setSelected(changeLogicRule.useState());
    callFunctionCheckBoxC.setSelected(changeLogicRule.useCallFunction());

    htGrpCBC_actionPerformed(null);
    speciesCBC_actionPerformed(null);
    sizeClassCBC_actionPerformed(null);
    densityCBC_actionPerformed(null);
    stateCBC_actionPerformed(null);
    callFunctionCheckBoxC_actionPerformed(null);

    if (changeLogicRule.useState()) {
      stateValueC.setText(changeLogicRule.getState());
    }
    else { stateValueC.setText("                                 "); }

    if (changeLogicRule.useCallFunction()) {
      callFunctionEvalCBC.setSelectedItem(changeLogicRule.getCallFunction());
      updateIsEffectiveProbText();
    }
    else { callFunctionEvalCBC.setSelectedIndex(0); }

    stateToValue.setText("                                 ");

    // make sure event handlers know to do nothing.
    boolean inInitSaved = inInit;
    inInit = true;

    speciesToCB.setSelectedIndex(0);
    sizeClassToCB.setSelectedIndex(0);
    densityToCB.setSelectedIndex(0);
    callFunctionCBC.setSelectedIndex(0);

    inInit = inInitSaved;

    speciesToRB.setSelected(true);
    speciesToCB.setEnabled(false);
    sizeClassToRB.setSelected(false);
    sizeClassToCB.setEnabled(false);
    densityToRB.setSelected(false);
    densityToCB.setEnabled(false);

    updateToPanel();

    String evalBoolC;
    for(i=0; i<evalANDRBC.length; i++) {
      evalBoolC = changeLogicRule.getBoolChoice(i);
      evalANDRBC[i].setSelected((evalBoolC == TreatmentLogicData.AND));
      evalORRBC[i].setSelected((evalBoolC == TreatmentLogicData.OR));
    }

    htGrpOneOfRBC.setSelected(changeLogicRule.isOneOfHtGrp());
    htGrpNotOneOfRBC.setSelected(changeLogicRule.isOneOfHtGrp() == false);

    speciesOneOfRBC.setSelected(changeLogicRule.isOneOfSpecies());
    speciesNotOneOfRBC.setSelected(changeLogicRule.isOneOfSpecies() == false);

    sizeClassOneOfRBC.setSelected(changeLogicRule.isOneOfSizeClass());
    sizeClassNotOneOfRBC.setSelected(changeLogicRule.isOneOfSizeClass() == false);

    densityOneOfRBC.setSelected(changeLogicRule.isOneOfDensity());
    densityNotOneOfRBC.setSelected(changeLogicRule.isOneOfDensity() == false);

    int ruleNum = changeLogic.indexOf(changeLogicRule);
    String title = "Rule #" + ruleNum;
    ruleBorder.setTitle(title);
    rulePanel.update(rulePanel.getGraphics());

    setSelectedTab(conditionTabbedPaneC,changeLogicRule);
    inInitChange = false;
  }

  private void updateToPanel() {
    String toChoice = changeLogicRule.getToChoice();
    String toValue  = changeLogicRule.getToValue();


    boolean toState        = (toChoice == ChangeLogic.TO_STATE);
    boolean toFunctionCall = (toChoice == ChangeLogic.TO_FUNCTION_CALL);
    boolean toSpecies      = (toChoice == ChangeLogic.TO_SPECIES);
    boolean toSizeClass    = (toChoice == ChangeLogic.TO_SIZE_CLASS);
    boolean toDensity      = (toChoice == ChangeLogic.TO_DENSITY);

    if (toState) {
      stateToValue.setText(toValue);
      enableToState();
    }
    else if (toSpecies || toSizeClass || toDensity) {
      if (toSpecies) {
        // ChangeLogic is first set to species when created,
        // and the toValue is set to null. Thus the necessity of the following.
        if (toValue == null) {
          toValue = ((SimpplleType)speciesToCB.getSelectedItem()).toString();
        }
        speciesToCB.setSelectedItem(Species.get(toValue));
      }
      else if (toSizeClass) {
        sizeClassToCB.setSelectedItem(SizeClass.get(toValue));
      }
      else if (toDensity) {
        densityToCB.setSelectedItem(Density.get(toValue));
      }

      enableToStateItem(toChoice);
    }
    else if (toChoice == ChangeLogic.TO_FUNCTION_CALL) {
      callFunctionCBC.setSelectedItem(toValue);
      enableToFunctionCall();
    }
  }

  private void enableToStateItem(String toChoice) {
    boolean toSpecies      = (toChoice == ChangeLogic.TO_SPECIES);
    boolean toSizeClass    = (toChoice == ChangeLogic.TO_SIZE_CLASS);
    boolean toDensity      = (toChoice == ChangeLogic.TO_DENSITY);

    speciesToCB.setEnabled(toSpecies);
    sizeClassToCB.setEnabled(toSizeClass);
    densityToCB.setEnabled(toDensity);

    speciesToRB.setEnabled(true);
    sizeClassToRB.setEnabled(true);
    densityToRB.setEnabled(true);

    speciesToRB.setSelected(toSpecies);
    sizeClassToRB.setSelected(toSizeClass);
    densityToRB.setSelected(toDensity);

    stateToValue.setEnabled(false);
    statePickToPB.setEnabled(false);
    callFunctionCBC.setEnabled(false);
    callFunctionTextC.setEnabled(false);

    int index = toTabbedPane.indexOfTab("To State Item");
//    toTabbedPane.setEnabledAt(index,true);
    toTabbedPane.setSelectedIndex(index);

    index = toTabbedPane.indexOfTab("To State");
//    toTabbedPane.setEnabledAt(index,false);

    index = toTabbedPane.indexOfTab("To Function Call");
//    toTabbedPane.setEnabledAt(index,false);
  }

  private void enableToState() {
    speciesToCB.setEnabled(false);
    speciesToRB.setEnabled(false);
    sizeClassToCB.setEnabled(false);
    sizeClassToRB.setEnabled(false);
    densityToCB.setEnabled(false);
    densityToRB.setEnabled(false);
    stateToValue.setEnabled(true);
    statePickToPB.setEnabled(true);
    callFunctionCBC.setEnabled(false);
    callFunctionTextC.setEnabled(false);

    int index = toTabbedPane.indexOfTab("To State Item");
//    toTabbedPane.setEnabledAt(index,false);

    index = toTabbedPane.indexOfTab("To State");
//    toTabbedPane.setEnabledAt(index,true);
    toTabbedPane.setSelectedIndex(index);

    index = toTabbedPane.indexOfTab("To Function Call");
//    toTabbedPane.setEnabledAt(index,false);
  }

  private void enableToFunctionCall() {
    speciesToCB.setEnabled(false);
    speciesToRB.setEnabled(false);
    sizeClassToCB.setEnabled(false);
    sizeClassToRB.setEnabled(false);
    densityToCB.setEnabled(false);
    densityToRB.setEnabled(false);
    stateToValue.setEnabled(false);
    statePickToPB.setEnabled(false);
    callFunctionCBC.setEnabled(true);
    callFunctionTextC.setEnabled(true);

    int index = toTabbedPane.indexOfTab("To State Item");
//    toTabbedPane.setEnabledAt(index,false);

    index = toTabbedPane.indexOfTab("To State");
//    toTabbedPane.setEnabledAt(index,false);

    index = toTabbedPane.indexOfTab("To Function Call");
//    toTabbedPane.setEnabledAt(index,true);
    toTabbedPane.setSelectedIndex(index);
  }
  private void setEnabledChangeTab(boolean bool) {
    int index = tabbedPane.indexOfTab("Change");
    tabbedPane.setEnabledAt(index,bool);
  }

  void treatmentCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }

    newTreatment();
    updateDialog();
  }

  void feasibleRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setFeasible(true);
    initializeChange();
    updateDialog();
  }

  void feasibleNOTRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setFeasible(false);
    initializeChange();
    updateDialog();
  }

  void htGrpOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfHtGrp(true);
  }

  void htGrpNotOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfHtGrp(false);
  }

  private void updateConditionalText(JTextArea textArea, ListItem[] items) {
    StringBuffer strBuf = new StringBuffer();
    boolean      firstPrinted = false;
    for (int i=0; i<items.length; i++) {
      if (items[i].isSelected() == false) { continue; }
      if (firstPrinted) { strBuf.append(", "); }
      strBuf.append(items[i].item.toString());
      firstPrinted = true;
    }
    strBuf.append("      ");  //  Make sure scroll bar appears when needed.
    textArea.setText(strBuf.toString());
    textArea.setCaretPosition(0);
  }

  void htGrpSelectPickPB_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Ecological Grouping(s)",true,htGrpItems);
    dlg.setVisible(true);
    feasibilityLogic.clearHtGrpItems();
    for(int i=0; i<htGrpItems.length; i++) {
      if (htGrpItems[i].isSelected()) {
        feasibilityLogic.addHtGrp(htGrpItems[i].item);
      }
    }
    updateConditionalText(htGrpText, htGrpItems);
  }

  void evalANDRB0_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(0,TreatmentLogicData.AND);
  }

  void evalORRB0_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(0,TreatmentLogicData.OR);
  }

  void speciesOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfSpecies(true);
  }

  void speciesNotOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfSpecies(false);
  }

  void speciesSelectPickPB_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Species",true,speciesItems);
    dlg.setVisible(true);
    feasibilityLogic.clearSpeciesItems();
    for(int i=0; i<speciesItems.length; i++) {
      if (speciesItems[i].isSelected()) {
        feasibilityLogic.addSpecies(speciesItems[i].item);
      }
    }
    updateConditionalText(speciesText, speciesItems);
  }

  void evalANDRB1_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(1,TreatmentLogicData.AND);
  }

  void evalORRB1_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(1,TreatmentLogicData.OR);
  }

  void sizeClassOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfSizeClass(true);
  }

  void sizeClassNotOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfSizeClass(false);
  }

  void sizeClassSelectPickPB_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Size Class(s)",true,sizeClassItems);
    dlg.setVisible(true);
    feasibilityLogic.clearSizeClassItems();
    for(int i=0; i<sizeClassItems.length; i++) {
      if (sizeClassItems[i].isSelected()) {
        feasibilityLogic.addSizeClass(sizeClassItems[i].item);
      }
    }
    updateConditionalText(sizeClassText, sizeClassItems);
  }

  void evalANDRB2_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(2,TreatmentLogicData.AND);
  }

  void evalORRB2_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(2,TreatmentLogicData.OR);
  }

  void densityOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfDensity(true);
  }

  void densityNotOneOfRB_actionPerformed(ActionEvent e) {
    feasibilityLogic.setOneOfDensity(false);
  }

  void densitySelectPickPB_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Density(s)",true,densityItems);
    dlg.setVisible(true);
    feasibilityLogic.clearDensityItems();
    for(int i=0; i<densityItems.length; i++) {
      if (densityItems[i].isSelected()) {
        feasibilityLogic.addDensity(densityItems[i].item);
      }
    }
    updateConditionalText(densityText, densityItems);
  }

  void evalANDRB3_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(3,TreatmentLogicData.AND);
  }

  void evalORRB3_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(3,TreatmentLogicData.OR);
  }

  void callFunctionCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }

    String selection = (String)callFunctionCB.getSelectedItem();
    String desc = Treatment.getFeasibilityFunctionDesc(selection);
    callFunctionText.setText(desc);

    if (callFunctionCheckBox.isSelected()) {
      feasibilityLogic.setCallFunction(selection);
    }
    updateDialog();
  }

  void evalANDRB4_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(4,TreatmentLogicData.AND);
  }

  void evalORRB4_actionPerformed(ActionEvent e) {
    feasibilityLogic.setBoolChoice(4,TreatmentLogicData.OR);
  }

  void statePickPB_actionPerformed(ActionEvent e) {
    String result = chooseVegetativeType();
    if (result == null) { return; }

    stateValue.setText(result);
    feasibilityLogic.setState(result);
  }

  private boolean checkRuleValidity() {
    if (changeLogicRule != null && changeLogicRule.getToValue() == null) {
      String msg = "Please pick a state to change to or select a different TO Option";
      String title = "Invalid new State";
      JOptionPane.showMessageDialog(this,msg,title,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }
  void prevPB_actionPerformed(ActionEvent e) {
    if (checkRuleValidity() == false) { return; }

    if (changeLogicIndex == 0) {
      changeLogicIndex = changeLogic.size() - 1;
    }
    else {
      changeLogicIndex--;
    }
    initializeChange();
  }

  void nextPB_actionPerformed(ActionEvent e) {
    if (checkRuleValidity() == false) { return; }

    changeLogicIndex++;

    if (changeLogicIndex == changeLogic.size()) {
      changeLogicIndex = 0;
    }

    initializeChange();
  }



  void htGrpOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfHtGrp(true);
  }

  void htGrpNotOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfHtGrp(false);
  }

  void htGrpSelectPickPBC_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Ecological Grouping(s)",true,htGrpItemsC);
    dlg.setVisible(true);
    changeLogicRule.clearHtGrpItems();
    for(int i=0; i<htGrpItemsC.length; i++) {
      if (htGrpItemsC[i].isSelected()) {
        changeLogicRule.addHtGrp(htGrpItemsC[i].item);
      }
    }
    updateConditionalText(htGrpTextC, htGrpItemsC);
  }

  void speciesOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfSpecies(true);
  }

  void speciesNotOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfSpecies(false);
  }

  void speciesSelectPickPBC_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Species",true,speciesItemsC);
    dlg.setVisible(true);
    changeLogicRule.clearSpeciesItems();
    for(int i=0; i<speciesItemsC.length; i++) {
      if (speciesItemsC[i].isSelected()) {
        changeLogicRule.addSpecies(speciesItemsC[i].item);
      }
    }
    updateConditionalText(speciesTextC, speciesItemsC);
  }

  void evalANDRBC0_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(0,TreatmentLogicData.AND);
  }

  void evalORRBC0_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(0,TreatmentLogicData.OR);
  }

  void evalANDRBC1_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(1,TreatmentLogicData.AND);
  }

  void evalORRBC1_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(1,TreatmentLogicData.OR);
  }

  void sizeClassOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfSizeClass(true);
  }

  void sizeClassNotOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfSizeClass(false);
  }

  void sizeClassSelectPickPBC_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Size Class(s)",true,sizeClassItemsC);
    dlg.setVisible(true);
    changeLogicRule.clearSizeClassItems();
    for(int i=0; i<sizeClassItemsC.length; i++) {
      if (sizeClassItemsC[i].isSelected()) {
        changeLogicRule.addSizeClass(sizeClassItemsC[i].item);
      }
    }
    updateConditionalText(sizeClassTextC, sizeClassItemsC);
  }

  void evalANDRBC2_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(2,TreatmentLogicData.AND);
  }

  void evalORRBC2_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(2,TreatmentLogicData.OR);
  }

  void densityOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfDensity(true);
  }

  void densityNotOneOfRBC_actionPerformed(ActionEvent e) {
    changeLogicRule.setOneOfDensity(false);
  }


  void densitySelectPickPBC_actionPerformed(ActionEvent e) {
    ChooseSimpplleTypes dlg =
      new ChooseSimpplleTypes(theFrame,"Choose Density(s)",true,densityItemsC);
    dlg.setVisible(true);
    changeLogicRule.clearDensityItems();
    for(int i=0; i<densityItemsC.length; i++) {
      if (densityItemsC[i].isSelected()) {
        changeLogicRule.addDensity(densityItemsC[i].item);
      }
    }
    updateConditionalText(densityTextC, densityItemsC);
  }

  void evalANDRBC3_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(3,TreatmentLogicData.AND);
  }

  void evalORRBC3_actionPerformed(ActionEvent e) {
    changeLogicRule.setBoolChoice(3,TreatmentLogicData.OR);
  }

  void statePickPBC_actionPerformed(ActionEvent e) {
    String result = chooseVegetativeType();
    if (result == null) { return; }

    stateValueC.setText(result);
    changeLogicRule.setState(result);
  }

  void statePickToPB_actionPerformed(ActionEvent e) {
    String result = chooseVegetativeType();
    if (result == null) { return; }

    stateToValue.setText(result);
    changeLogicRule.setToChoice(ChangeLogic.TO_STATE);
    changeLogicRule.setToValue(result);
  }

  private void densityChanged() {
    String value = ((SimpplleType)densityToCB.getSelectedItem()).toString();
    changeLogicRule.setToChoice(ChangeLogic.TO_DENSITY);
    changeLogicRule.setToValue(value);
  }
  void densityToRB_actionPerformed(ActionEvent e) {
    speciesToCB.setEnabled(false);
    sizeClassToCB.setEnabled(false);
    densityToCB.setEnabled(true);
    densityChanged();
  }
  void densityToCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    densityChanged();
  }

  private boolean isPreviousEvalSelected(boolean[] selected, int evalPos) {
    for (int i=evalPos-1; i>=0; i--) {
      if (selected[i]) { return true; }
    }
    return false;
  }

  private void updateEvalBoolPanel() {
    boolean[] selected = new boolean[evalLabel.length];

    selected[feasibilityLogic.getHtGrpEvalPos()]     = htGrpCB.isSelected();
    selected[feasibilityLogic.getSpeciesEvalPos()]   = speciesCB.isSelected();
    selected[feasibilityLogic.getSizeClassEvalPos()] = sizeClassCB.isSelected();
    selected[feasibilityLogic.getDensityEvalPos()]   = densityCB.isSelected();
    selected[feasibilityLogic.getStateEvalPos()]     = stateCB.isSelected();
    selected[feasibilityLogic.getCallFunctionEvalPos()] = callFunctionCheckBox.isSelected();

    for (int i=0; i<selected.length; i++) {
      if (i != 0) {
        evalBoolPanel[i-1].setVisible(selected[i] && isPreviousEvalSelected(selected, i));
      }
      evalLabel[i].setVisible(selected[i]);
    }
    updateDialog();
  }

  void htGrpCB_actionPerformed(ActionEvent e) {
    boolean selected = htGrpCB.isSelected();
    htGrpOneOfRB.setEnabled(selected);
    htGrpNotOneOfRB.setEnabled(selected);
    htGrpSelectPickPB.setEnabled(selected);

    feasibilityLogic.setUseHtGrp(selected);
    if (selected == false) {
      feasibilityLogic.clearHtGrpItems();
      htGrpText.setText("");
    }
    int index = conditionTabbedPane.indexOfTab("Ecological Grouping");
    conditionTabbedPane.setEnabledAt(index,selected);
    updateSelectedTabF(selected,index);
    updateEvalBoolPanel();
  }

  void speciesCB_actionPerformed(ActionEvent e) {
    boolean selected = speciesCB.isSelected();
    speciesOneOfRB.setEnabled(selected);
    speciesNotOneOfRB.setEnabled(selected);
    speciesSelectPickPB.setEnabled(selected);

    feasibilityLogic.setUseSpecies(selected);
    if (selected == false) {
      feasibilityLogic.clearSpeciesItems();
      speciesText.setText("");
    }
    int index = conditionTabbedPane.indexOfTab("Species");
    conditionTabbedPane.setEnabledAt(index,selected);
    updateSelectedTabF(selected,index);
    updateEvalBoolPanel();
  }

  void sizeClassCB_actionPerformed(ActionEvent e) {
    boolean selected = sizeClassCB.isSelected();
    sizeClassOneOfRB.setEnabled(selected);
    sizeClassNotOneOfRB.setEnabled(selected);
    sizeClassSelectPickPB.setEnabled(selected);

    feasibilityLogic.setUseSizeClass(selected);
    if (selected == false) {
      feasibilityLogic.clearSizeClassItems();
      sizeClassText.setText("");
    }
    int index = conditionTabbedPane.indexOfTab("Size Class");
    conditionTabbedPane.setEnabledAt(index,selected);
    updateSelectedTabF(selected,index);
    updateEvalBoolPanel();
  }

  void densityCB_actionPerformed(ActionEvent e) {
    boolean selected = densityCB.isSelected();
    densityOneOfRB.setEnabled(selected);
    densityNotOneOfRB.setEnabled(selected);
    densitySelectPickPB.setEnabled(selected);

    feasibilityLogic.setUseDensity(selected);
    if (selected == false) {
      feasibilityLogic.clearDensityItems();
      densityText.setText("");
    }
    int index = conditionTabbedPane.indexOfTab("Density");
    conditionTabbedPane.setEnabledAt(index,selected);
    updateSelectedTabF(selected,index);
    updateEvalBoolPanel();
  }

  void callFunctionCheckBox_actionPerformed(ActionEvent e) {
    boolean selected = callFunctionCheckBox.isSelected();
    callFunctionCB.setEnabled(selected);
    callFunctionText.setEnabled(selected);

    int index = conditionTabbedPane.indexOfTab("Call Function");
    conditionTabbedPane.setEnabledAt(index,selected);
    updateEvalBoolPanel();

    feasibilityLogic.setUseCallFunction(selected);
    if (selected == false) {
      feasibilityLogic.clearCallFunction();
      callFunctionCB.setSelectedIndex(0);
    }
    updateSelectedTabF(selected,index);
    if (!selected) { feasibilityLogic.clearCallFunction(); }
    else {
      String item = feasibilityLogic.getCallFunction();
      if (item == null) { item = (String)callFunctionCB.getSelectedItem(); }
      feasibilityLogic.setCallFunction(item);
    }
  }

  void stateCB_actionPerformed(ActionEvent e) {
    boolean selected = stateCB.isSelected();
    stateValue.setEnabled(selected);
    statePickPB.setEnabled(selected);

    int index = conditionTabbedPane.indexOfTab("Current State");
    conditionTabbedPane.setEnabledAt(index,selected);
    updateEvalBoolPanel();

    feasibilityLogic.setUseState(selected);
    if (selected == false) {
      feasibilityLogic.clearState();
      stateValue.setText("                                    ");
    }
    updateSelectedTabF(selected,index);
    if (!selected) { feasibilityLogic.clearState(); }
  }

  private void sizeClassChanged() {
    String value = ((SimpplleType)sizeClassToCB.getSelectedItem()).toString();
    changeLogicRule.setToChoice(ChangeLogic.TO_SIZE_CLASS);
    changeLogicRule.setToValue(value);
  }
  void sizeClassToRB_actionPerformed(ActionEvent e) {
    speciesToCB.setEnabled(false);
    sizeClassToCB.setEnabled(true);
    densityToCB.setEnabled(false);
    sizeClassChanged();
  }
  void sizeClassToCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    sizeClassChanged();
  }

  private void speciesChanged() {
    String value = ((SimpplleType)speciesToCB.getSelectedItem()).toString();
    changeLogicRule.setToChoice(ChangeLogic.TO_SPECIES);
    changeLogicRule.setToValue(value);
  }
  void speciesToRB_actionPerformed(ActionEvent e) {
    speciesToCB.setEnabled(true);
    sizeClassToCB.setEnabled(false);
    densityToCB.setEnabled(false);
    speciesChanged();
  }
  void speciesToCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    speciesChanged();
  }

  private void callFunctionChanged() {
    String function = (String)callFunctionCBC.getSelectedItem();
    String desc = Treatment.getChangeFunctionDesc(function);
    callFunctionTextC.setText(desc);

    changeLogicRule.setToChoice(ChangeLogic.TO_FUNCTION_CALL);
    changeLogicRule.setToValue(function);
    updateDialog();
  }
  void callFunctionCBC_actionPerformed(ActionEvent e) {
    if (inInit) { return; }

    callFunctionChanged();
  }

  private void updateEvalBoolPanelC() {
    boolean[] selected = new boolean[evalLabelC.length];

    selected[changeLogicRule.getHtGrpEvalPos()]     = htGrpCBC.isSelected();
    selected[changeLogicRule.getSpeciesEvalPos()]   = speciesCBC.isSelected();
    selected[changeLogicRule.getSizeClassEvalPos()] = sizeClassCBC.isSelected();
    selected[changeLogicRule.getDensityEvalPos()]   = densityCBC.isSelected();
    selected[changeLogicRule.getStateEvalPos()]     = stateCBC.isSelected();
    selected[changeLogicRule.getCallFunctionEvalPos()] = callFunctionCheckBoxC.isSelected();

    for (int i=0; i<selected.length; i++) {
      if (i != 0) {
        evalBoolPanelC[i-1].setVisible(selected[i] && isPreviousEvalSelected(selected, i));
      }
      evalLabelC[i].setVisible(selected[i]);
    }
    updateDialog();
  }

  void htGrpCBC_actionPerformed(ActionEvent e) {
    boolean selected = htGrpCBC.isSelected();
    htGrpOneOfRBC.setEnabled(selected);
    htGrpNotOneOfRBC.setEnabled(selected);
    htGrpSelectPickPBC.setEnabled(selected);

    int index = conditionTabbedPane.indexOfTab("Ecological Grouping");
    conditionTabbedPaneC.setEnabledAt(index,selected);
    changeLogicRule.setUseHtGrp(selected);
    if (selected == false) {
      changeLogicRule.clearHtGrpItems();
      htGrpTextC.setText("");
    }
    updateSelectedTabC(selected,index);
    updateEvalBoolPanelC();
  }

  void speciesCBC_actionPerformed(ActionEvent e) {
    boolean selected = speciesCBC.isSelected();
    speciesOneOfRBC.setEnabled(selected);
    speciesNotOneOfRBC.setEnabled(selected);
    speciesSelectPickPBC.setEnabled(selected);

    changeLogicRule.setUseSpecies(selected);
    if (selected == false) {
      changeLogicRule.clearSpeciesItems();
      speciesTextC.setText("");
    }
    int index = conditionTabbedPane.indexOfTab("Species");
    conditionTabbedPaneC.setEnabledAt(index,selected);
    updateSelectedTabC(selected,index);
    updateEvalBoolPanelC();
  }

  void sizeClassCBC_actionPerformed(ActionEvent e) {
    boolean selected = sizeClassCBC.isSelected();
    sizeClassOneOfRBC.setEnabled(selected);
    sizeClassNotOneOfRBC.setEnabled(selected);
    sizeClassSelectPickPBC.setEnabled(selected);

    changeLogicRule.setUseSizeClass(selected);
    if (selected == false) {
      changeLogicRule.clearSizeClassItems();
      sizeClassTextC.setText("");
    }
    int index = conditionTabbedPane.indexOfTab("Size Class");
    conditionTabbedPaneC.setEnabledAt(index,selected);
    updateSelectedTabC(selected,index);
    updateEvalBoolPanelC();
  }

  void densityCBC_actionPerformed(ActionEvent e) {
    boolean selected = densityCBC.isSelected();
    densityOneOfRBC.setEnabled(selected);
    densityNotOneOfRBC.setEnabled(selected);
    densitySelectPickPBC.setEnabled(selected);

    changeLogicRule.setUseDensity(selected);
    if (selected == false) {
      changeLogicRule.clearDensityItems();
      densityTextC.setText("");
    }
    int index = conditionTabbedPane.indexOfTab("Density");
    conditionTabbedPaneC.setEnabledAt(index,selected);
    updateSelectedTabC(selected,index);
    updateEvalBoolPanelC();
  }

  void stateCBC_actionPerformed(ActionEvent e) {
    boolean selected = stateCBC.isSelected();
    stateValueC.setEnabled(selected);
    statePickPBC.setEnabled(selected);

    changeLogicRule.setUseState(selected);
    if (selected == false) {
      changeLogicRule.clearState();
      stateValueC.setText("                                    ");
    }
    int index = conditionTabbedPane.indexOfTab("Current State");
    conditionTabbedPaneC.setEnabledAt(index,selected);
    updateSelectedTabC(selected,index);
    updateEvalBoolPanelC();

    if (!selected) { changeLogicRule.clearState(); }
  }

  void menuFileOpen_actionPerformed(ActionEvent e) {
    if (SystemKnowledgeFiler.openFile(this,SystemKnowledge.TREATMENT_LOGIC,menuFileSave,menuFileClose)) {
      updateComboBoxValues(treatmentCB,Simpplle.getCurrentZone().getLegalTreatments());
      newTreatment();
    }
    updateDialog();
  }

  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    File         infile;
    MyFileFilter extFilter;
    String       title = "Select a Treatment Logic file.";

    extFilter = new MyFileFilter("treatmentlogic",
                                 "Treatment Logic Files (*.treatmentlogic)");

    setCursor(Utility.getWaitCursor());

    infile = Utility.getOpenFile(this,title,extFilter);
    if (infile != null) {
      try {
        Treatment.readLogic(infile);
        updateComboBoxValues(treatmentCB,Simpplle.getCurrentZone().getLegalTreatments());
        newTreatment();
        menuFileSave.setEnabled(true);
        menuFileClose.setEnabled(true);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getMessage(),
                                      "Error Loading Treatment Logic File",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
    setCursor(Utility.getNormalCursor());
    updateDialog();
  }

  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.TREATMENT_LOGIC);
    SystemKnowledgeFiler.saveFile(this, outfile,
                                  SystemKnowledge.TREATMENT_LOGIC, menuFileSave,
                                  menuFileClose);
  }
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.TREATMENT_LOGIC,
                                  menuFileSave, menuFileClose);
  }

  void menuFileClose_actionPerformed(ActionEvent e) {
    menuFileDefaultAll_actionPerformed(null);
  }
  void menuFileDefaultAll_actionPerformed(ActionEvent e) {
    try {
      SystemKnowledge.readZoneDefault(SystemKnowledge.TREATMENT_LOGIC);
      updateComboBoxValues(treatmentCB,Simpplle.getCurrentZone().getLegalTreatments());
      newTreatment();
      updateDialog();
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error reading file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
  void menuLoadDefault_actionPerformed(ActionEvent e) {
    try {
      Treatment.readLogic(selectedTreatment);
      newTreatment();
      updateDialog();
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getMessage(),"Error reading file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void quit() {
    setVisible(false);
    dispose();
  }

  void menuQuit_actionPerformed(ActionEvent e) { quit(); }
  void this_windowClosing(WindowEvent e) { quit(); }

  void menuFeasibilityEvalOrder_actionPerformed(ActionEvent e) {

  }

  void menuChangeNew_actionPerformed(ActionEvent e) {
    if (checkRuleValidity() == false) { return; }

    Treatment.addChangeRule(selectedTreatment);
    changeLogicIndex = changeLogic.size() - 1;
    initializeChange();
    updateDialog();

    int index = tabbedPane.indexOfTab("Change");
    tabbedPane.setSelectedIndex(index);
  }

  void menuChangeEvalOrder_actionPerformed(ActionEvent e) {

  }
  void menuChangeDelete_actionPerformed(ActionEvent e) {
    String msg = "Delete Current Rule!\n\n Are You Sure?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Delete Current Rule",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.NO_OPTION) { return;}

    ChangeLogic rule = changeLogicRule;
    if (changeLogic.size() > 1) {
      if (changeLogicIndex != 0) { prevPB_actionPerformed(null); }

      Treatment.removeChangeRule(selectedTreatment, rule);
      initializeChange();
      updateDialog();
    }
    else {
      Treatment.removeChangeRule(selectedTreatment, rule);
      newTreatment();
      updateDialog();
    }
  }

  void showTextPB_actionPerformed(ActionEvent e) {
    String code = changeLogicRule.getPrintCode();

    JTextAreaDialog textDlg = new JTextAreaDialog(JSimpplle.getSimpplleMain(),
                                                  "Printed Rule",true,code);

    textDlg.setVisible(true);
  }

  void callFunctionCheckBoxC_actionPerformed(ActionEvent e) {
    boolean selected = callFunctionCheckBoxC.isSelected();
    callFunctionEvalCBC.setEnabled(selected);
    callFunctionEvalTextC.setEnabled(selected);

    int index = conditionTabbedPane.indexOfTab("Call Function");
    conditionTabbedPaneC.setEnabledAt(index,selected);
    updateEvalBoolPanelC();

    changeLogicRule.setUseCallFunction(selected);
    if (selected == false) {
      changeLogicRule.clearCallFunction();
      callFunctionEvalCBC.setSelectedIndex(0);
    }
    updateSelectedTabC(selected,index);
    if (!selected) {
      changeLogicRule.clearCallFunction();
    }
    else {
      String item = changeLogicRule.getCallFunction();
      if (item == null) { item = (String)callFunctionEvalCBC.getSelectedItem(); }
      changeLogicRule.setCallFunction(item);
      updateIsEffectiveProbText();
    }
    updateDialog();
  }

  private void updateIsEffectiveProbText() {
    if (changeLogicRule.isEffectiveCallFunction()) {
      isEffectiveProbText.setVisible(true);
      int prob = (int)Math.round(Simulation.getFloatProbability(changeLogicRule.getIsEffectiveProb()));
      isEffectiveProbText.setProbability(prob);
    }
    else {
      isEffectiveProbText.setVisible(false);
    }
  }
  void callFunctionEvalCBC_actionPerformed(ActionEvent e) {
    if (inInit) { return; }

    String selection = (String)callFunctionEvalCBC.getSelectedItem();
    String desc = Treatment.getChangeEvalFunctionDesc(selection);
    callFunctionEvalTextC.setText(desc);

    if (callFunctionCheckBoxC.isSelected()) {
      changeLogicRule.setCallFunction(selection);
      updateIsEffectiveProbText();
    }
    updateDialog();
  }

  public void isEffectiveProbText_actionPerformed(ActionEvent e) {
    int prob = Simulation.getRationalProbability(isEffectiveProbText.getProbability());
    changeLogicRule.setIsEffectiveProb(prob);
  }

  public void isEffectiveProbText_focusLost(FocusEvent focusEvent) {
    int prob = Simulation.getRationalProbability(isEffectiveProbText.getProbability());
    changeLogicRule.setIsEffectiveProb(prob);
  }

  void toTabbedPane_stateChanged(ChangeEvent e) {
    if (inInit || inInitChange || changeLogicRule == null) { return; }

    int index = toTabbedPane.getSelectedIndex();
    if (index == 0) {
      if (speciesToRB.isSelected()) { speciesChanged(); }
      else if (sizeClassToRB.isSelected()) { sizeClassChanged(); }
      else if (densityToRB.isSelected()) { densityChanged(); }
      enableToStateItem(changeLogicRule.getToChoice());
    }
    else if (index == 1) {
      changeLogicRule.setToChoice(ChangeLogic.TO_STATE);
      String toValue = stateToValue.getText();
      if (toValue != null && toValue.trim().length() == 0) {
        toValue = null;
      }
      changeLogicRule.setToValue(toValue);
      enableToState();
    }
    else if (index == 2) {
      callFunctionChanged();
      enableToFunctionCall();
    }

  }
  void toStateItemPanel_focusGained(FocusEvent e) {
  }

  void toStatePanel_focusGained(FocusEvent e) {
  }

  void toFunctionCallPanel_focusGained(FocusEvent e) {
  }

  void menuPrintTreatment_actionPerformed(ActionEvent e) {
    String code = Treatment.getPrintedTreatmentLogic(selectedTreatment);

    JTextAreaDialog textDlg = new JTextAreaDialog(JSimpplle.getSimpplleMain(),
                                                  "Printed Logic",true,code);

    textDlg.setVisible(true);
  }

  void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.TREATMENT_LOGIC);
    String title = "Treatment Logic Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }

  void menuFileCreate_actionPerformed(ActionEvent e) {
    String name = JOptionPane.showInputDialog(this, "Treatment Name", "",
                                              JOptionPane.PLAIN_MESSAGE);

    if (name == null) { return; }

    name = name.toUpperCase();
    if (TreatmentType.get(name) != null) {
      JOptionPane.showMessageDialog(this, "Treatment Exists already",
                                    "Treatment Exists",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }


    String msg = "Create a new Treatment Named \"" + name + "\"";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Create a new Treatment",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      TreatmentType newTreat = Treatment.addLegalTreatment(name);

      updateComboBoxValues(treatmentCB,Treatment.getLegalTreatments());
      treatmentCB.setSelectedItem(newTreat);
    }
  }


}


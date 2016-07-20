/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

import simpplle.*;
import simpplle.comcode.*;

/** 
 * This class defines the JDialog for fire spread.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class FireSpread extends JDialog {
  private String process;
  private String position;
  private String density;
  private String sizeKind;
  private String resistance;
  private int    page;
  private boolean inInit;

  JPopupMenu       popupMenu = new JPopupMenu("Resistant Species");
  JMenuItem        popupLowResist = new JMenuItem("Show Low Species");
  JMenuItem        popupModerateResist = new JMenuItem("Show Moderate Species");
  JMenuItem        popupHighResist = new JMenuItem("Show High Species");

  JPopupMenu       popupMenuFarsite = new JPopupMenu("FARSITE INFO");
  JMenuItem        popupFarsite = new JMenuItem("FARSITE Indicies");

  JPanel mainPanel = new JPanel();
  BorderLayout mainLayout = new BorderLayout();
  JPanel diagramInnerPanel = new JPanel();
  GridLayout diagramInnerLayout = new GridLayout();
  JPanel diagramPanel1 = new JPanel();
  JPanel diagramPanel3 = new JPanel();
  JPanel diagramPanel2 = new JPanel();
  JPanel diagram1Panel = new JPanel();
  JPanel diagram3Panel = new JPanel();
  JPanel diagram2Panel = new JPanel();
  JPanel controlsPanel = new JPanel();
  FlowLayout controlsLayout = new FlowLayout();
  JPanel processPanel = new JPanel();
  GridLayout processLayout = new GridLayout();
  JRadioButton lmsfRB = new JRadioButton();
  JRadioButton srfRB = new JRadioButton();
  TitledBorder processBorder;
  JPanel resistPanel = new JPanel();
  TitledBorder resistBorder;
  GridLayout resistLayout = new GridLayout();
  JRadioButton highResistRB = new JRadioButton();
  JRadioButton moderateResistRB = new JRadioButton();
  JRadioButton lowResistRB = new JRadioButton();
  JPanel sizeKindPanel = new JPanel();
  JPanel densityPanel = new JPanel();
  JPanel PositionPanel = new JPanel();
  JRadioButton nonForestRB = new JRadioButton();
  JRadioButton oneTwoRB = new JRadioButton();
  JRadioButton aboveRB = new JRadioButton();
  JRadioButton belowNextRB = new JRadioButton();
  JRadioButton threeFourRB = new JRadioButton();
  JRadioButton multiStoryRB = new JRadioButton();
  JRadioButton singleStoryRB = new JRadioButton();
  TitledBorder sizeKindBorder;
  TitledBorder positionBorder;
  TitledBorder densityBorder;
  FlowLayout diagram1PanelLayout = new FlowLayout();
  FlowLayout diagram2PanelLayout = new FlowLayout();
  FlowLayout diagram3PanelLayout = new FlowLayout();
  BorderLayout diagram1Layout = new BorderLayout();
  BorderLayout diagram2Layout = new BorderLayout();
  BorderLayout diagram3Layout = new BorderLayout();
  JPanel diagramPanel = new JPanel();
  JPanel diagramHeaderPanel = new JPanel();
  JTextArea diagramHeaderText = new JTextArea();
  JPanel prevNextPanel = new JPanel();
  JButton nextPB = new JButton();
  JButton prevPB = new JButton();
  BorderLayout diagramLayout = new BorderLayout();
  FlowLayout diagramHeaderLayout = new FlowLayout();
  FlowLayout prevNextLayout = new FlowLayout();
  GridLayout positionLayout = new GridLayout();
  GridLayout densityLayout = new GridLayout();
  GridLayout sizeKindLayout = new GridLayout();
  ButtonGroup positionGroup = new ButtonGroup();
  ButtonGroup densityGroup = new ButtonGroup();
  ButtonGroup sizeKindGroup = new ButtonGroup();
  ButtonGroup resistanceGroup = new ButtonGroup();
  JMenuBar menuBar = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileQuit = new JMenuItem();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileLoadDefaults = new JMenuItem();
  JPanel extremeCBPanel1 = new JPanel();
  JPanel averageCBPanel1 = new JPanel();
  JComboBox averageCB1 = new JComboBox();
  JComboBox extremeCB1 = new JComboBox();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel extremeCBPanel2 = new JPanel();
  JPanel averageCBPanel2 = new JPanel();
  JPanel extremeCBPanel3 = new JPanel();
  JPanel averageCBPanel3 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JComboBox averageCB2 = new JComboBox();
  JComboBox extremeCB2 = new JComboBox();
  BorderLayout borderLayout5 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  JComboBox averageCB3 = new JComboBox();
  JComboBox extremeCB3 = new JComboBox();
  JPanel diagramTextPanel1 = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  JTextArea diagramText1 = new JTextArea();
  JPanel diagramTextPanel2 = new JPanel();
  JPanel diagramTextPanel3 = new JPanel();
  BorderLayout borderLayout8 = new BorderLayout();
  BorderLayout borderLayout9 = new BorderLayout();
  JTextArea diagramText2 = new JTextArea();
  JTextArea diagramText3 = new JTextArea();
  JScrollPane diagramScrollPane = new JScrollPane();
  JMenu menuKnowledgeSource = new JMenu();
  JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
  JLabel diagramHeaderLabel = new JLabel();
/**
 * Constructor for Fire Spread 
 * @param frame owner
 * @param title name of jdialog
 * @param modal modality
 */
  public FireSpread(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
/**
 * Overloaded constructor for Fire Spread dialog, sets owner to null, title to empty string, and modality to false
 */
  public FireSpread() {
    this(null, "", false);
  }
/**
 * Sets the borders, layout, panels, and componenets for Fire Spread Jdialog
 * @throws Exception
 */
  void jbInit() throws Exception {
    processBorder = new TitledBorder("Process");
    resistBorder = new TitledBorder("Resistance");
    sizeKindBorder = new TitledBorder("Size/Structure");
    positionBorder = new TitledBorder("Relative Position");
    densityBorder = new TitledBorder("Density");
    titledBorder1 = new TitledBorder(BorderFactory.createEmptyBorder(),"Average");
    titledBorder2 = new TitledBorder(BorderFactory.createEmptyBorder(),"Extreme");
    mainPanel.setLayout(mainLayout);
    diagramInnerPanel.setLayout(diagramInnerLayout);
    diagramInnerLayout.setColumns(1);
    diagramInnerLayout.setHgap(1);
    diagramInnerLayout.setRows(3);
    diagramPanel1.setLayout(diagram1Layout);
    diagramPanel2.setLayout(diagram2Layout);
    diagramPanel3.setLayout(diagram3Layout);
    controlsPanel.setLayout(controlsLayout);
    processPanel.setLayout(processLayout);
    processLayout.setRows(3);
    lmsfRB.setSelected(true);
    lmsfRB.setText("Light or Mixed Severity Fire");
    lmsfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lmsfRB_actionPerformed(e);
      }
    });
    srfRB.setText("Stand Replacing Fire");
    srfRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        srfRB_actionPerformed(e);
      }
    });
    processPanel.setBorder(processBorder);
    controlsLayout.setAlignment(FlowLayout.LEFT);
    resistPanel.setLayout(resistLayout);
    resistPanel.setBorder(resistBorder);
    resistPanel.setToolTipText("Right-Click for Options");
    resistPanel.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        resistPanel_mouseClicked(e);
      }
    });
    resistLayout.setRows(3);
    highResistRB.setToolTipText("Right-Click for Options");
    highResistRB.setText("High");
    highResistRB.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        highResistRB_mouseClicked(e);
      }
    });
    highResistRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        highResistRB_actionPerformed(e);
      }
    });
    moderateResistRB.setToolTipText("Right-Click for Options");
    moderateResistRB.setText("Moderate");
    moderateResistRB.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        moderateResistRB_mouseClicked(e);
      }
    });
    moderateResistRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        moderateResistRB_actionPerformed(e);
      }
    });
    lowResistRB.setToolTipText("Right-Click for Options");
    lowResistRB.setSelected(true);
    lowResistRB.setText("Low");
    lowResistRB.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        lowResistRB_mouseClicked(e);
      }
    });
    lowResistRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lowResistRB_actionPerformed(e);
      }
    });
    nonForestRB.setSelected(true);
    nonForestRB.setText("Non Forest");
    nonForestRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nonForestRB_actionPerformed(e);
      }
    });
    oneTwoRB.setSelected(true);
    oneTwoRB.setText("1 or 2");
    oneTwoRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        oneTwoRB_actionPerformed(e);
      }
    });
    aboveRB.setSelected(true);
    aboveRB.setText("Above");
    aboveRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        aboveRB_actionPerformed(e);
      }
    });
    belowNextRB.setText("Below or Next to");
    threeFourRB.setText("3 or 4");
    threeFourRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        threeFourRB_actionPerformed(e);
      }
    });
    multiStoryRB.setText("Multiple Story");
    singleStoryRB.setText("Single Story");
    sizeKindPanel.setBorder(sizeKindBorder);
    sizeKindPanel.setLayout(sizeKindLayout);
    PositionPanel.setBorder(positionBorder);
    PositionPanel.setLayout(positionLayout);
    densityPanel.setBorder(densityBorder);
    densityPanel.setLayout(densityLayout);
    diagram1Panel.setLayout(diagram1PanelLayout);
    diagram2Panel.setLayout(diagram2PanelLayout);
    diagram3Panel.setLayout(diagram3PanelLayout);
    diagram3PanelLayout.setAlignment(FlowLayout.LEFT);
    diagram3PanelLayout.setHgap(0);
    diagram3PanelLayout.setVgap(0);
    diagram2PanelLayout.setAlignment(FlowLayout.LEFT);
    diagram2PanelLayout.setHgap(0);
    diagram2PanelLayout.setVgap(0);
    diagram1PanelLayout.setAlignment(FlowLayout.LEFT);
    diagram1PanelLayout.setHgap(0);
    diagram1PanelLayout.setVgap(0);
    diagramPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    diagramPanel.setLayout(diagramLayout);
    nextPB.setEnabled(false);
    nextPB.setIcon(new ImageIcon(simpplle.gui.FireSpread.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(simpplle.gui.FireSpread.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    prevPB.setEnabled(false);
    prevPB.setIcon(new ImageIcon(simpplle.gui.FireSpread.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(simpplle.gui.FireSpread.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    diagramHeaderPanel.setLayout(diagramHeaderLayout);
    diagramHeaderLayout.setAlignment(FlowLayout.LEFT);
    diagramHeaderLayout.setHgap(0);
    diagramHeaderLayout.setVgap(0);
    prevNextPanel.setLayout(prevNextLayout);
    positionLayout.setRows(3);
    belowNextRB.setText("Below or Next to");
    belowNextRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        belowNextRB_actionPerformed(e);
      }
    });
    densityLayout.setRows(3);
    sizeKindLayout.setRows(3);
    singleStoryRB.setText("Single Story");
    singleStoryRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        singleStoryRB_actionPerformed(e);
      }
    });
    multiStoryRB.setText("Multiple Story");
    multiStoryRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        multiStoryRB_actionPerformed(e);
      }
    });
    diagramHeaderText.setLineWrap(true);
    diagramHeaderText.setColumns(87);
    diagramHeaderText.setRows(5);
    diagramHeaderText.setBackground(Color.white);
    diagramHeaderText.setSelectionColor(Color.blue);
    diagramHeaderText.setEditable(false);
    diagramHeaderText.setFont(new java.awt.Font("Monospaced", 0, 12));
    mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {

      public void componentResized(ComponentEvent e) {
        mainPanel_componentResized(e);
      }
    });
    menuFile.setText("File");
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileQuit_actionPerformed(e);
      }
    });
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOpen_actionPerformed(e);
      }
    });
    menuFileClose.setEnabled(false);
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileClose_actionPerformed(e);
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
    menuFileLoadDefaults.setText("Load Defaults");
    menuFileLoadDefaults.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileLoadDefaults_actionPerformed(e);
      }
    });
    this.setModal(true);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    averageCB1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        averageCB1_actionPerformed(e);
      }
    });
    extremeCB1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        extremeCB1_actionPerformed(e);
      }
    });
    averageCBPanel1.setLayout(borderLayout1);
    extremeCBPanel1.setLayout(borderLayout2);
    averageCBPanel1.setBorder(titledBorder1);
    averageCBPanel1.setToolTipText("Right-Click for Options");
    averageCBPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        averageCBPanel1_mouseClicked(e);
      }
    });
    extremeCBPanel1.setBorder(titledBorder2);
    extremeCBPanel1.setToolTipText("Right-Click for Options");
    extremeCBPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        extremeCBPanel1_mouseClicked(e);
      }
    });
    averageCBPanel2.setLayout(borderLayout3);
    extremeCBPanel2.setLayout(borderLayout4);
    averageCB2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        averageCB2_actionPerformed(e);
      }
    });
    extremeCB2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        extremeCB2_actionPerformed(e);
      }
    });
    averageCBPanel2.setBorder(titledBorder1);
    averageCBPanel2.setToolTipText("Right-Click for Options");
    averageCBPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        averageCBPanel2_mouseClicked(e);
      }
    });
    extremeCBPanel2.setBorder(titledBorder2);
    extremeCBPanel2.setToolTipText("Right-Click for Options");
    extremeCBPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        extremeCBPanel2_mouseClicked(e);
      }
    });
    averageCBPanel3.setLayout(borderLayout5);
    extremeCBPanel3.setLayout(borderLayout6);
    averageCBPanel3.setBorder(titledBorder1);
    averageCBPanel3.setToolTipText("Right-Click for Options");
    averageCBPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        averageCBPanel3_mouseClicked(e);
      }
    });
    averageCB3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        averageCB3_actionPerformed(e);
      }
    });
    extremeCB3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        extremeCB3_actionPerformed(e);
      }
    });
    extremeCBPanel3.setBorder(titledBorder2);
    extremeCBPanel3.setToolTipText("Right-Click for Options");
    extremeCBPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        extremeCBPanel3_mouseClicked(e);
      }
    });
    diagramTextPanel1.setLayout(borderLayout7);
    diagramText1.setLineWrap(true);
    diagramText1.setColumns(87);
    diagramText1.setRows(7);
    diagramText1.setBackground(Color.white);
    diagramText1.setSelectionColor(Color.blue);
    diagramText1.setEditable(false);
    diagramText1.setFont(new java.awt.Font("Monospaced", 0, 12));
    diagramTextPanel2.setLayout(borderLayout8);
    diagramTextPanel3.setLayout(borderLayout9);
    diagramText2.setLineWrap(true);
    diagramText2.setColumns(87);
    diagramText2.setRows(7);
    diagramText2.setBackground(Color.white);
    diagramText2.setSelectionColor(Color.blue);
    diagramText2.setEditable(false);
    diagramText2.setFont(new java.awt.Font("Monospaced", 0, 12));
    diagramText3.setLineWrap(true);
    diagramText3.setColumns(87);
    diagramText3.setRows(7);
    diagramText3.setBackground(Color.white);
    diagramText3.setSelectionColor(Color.blue);
    diagramText3.setEditable(false);
    diagramText3.setFont(new java.awt.Font("Monospaced", 0, 12));
    menuKnowledgeSource.setText("Knowledge Source");
    menuKnowledgeSourceDisplay.setText("Display");
    menuKnowledgeSourceDisplay.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuKnowledgeSourceDisplay_actionPerformed(e);
      }
    });
    diagramHeaderLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    diagramHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
    diagramHeaderLabel.setText(" Burning Conditions");
    getContentPane().add(mainPanel);
    mainPanel.add(controlsPanel, BorderLayout.NORTH);
    controlsPanel.add(processPanel, null);
    processPanel.add(lmsfRB, null);
    processPanel.add(srfRB, null);
    controlsPanel.add(PositionPanel, null);
    PositionPanel.add(aboveRB, null);
    PositionPanel.add(belowNextRB, null);
    controlsPanel.add(densityPanel, null);
    densityPanel.add(oneTwoRB, null);
    densityPanel.add(threeFourRB, null);
    controlsPanel.add(sizeKindPanel, null);
    sizeKindPanel.add(nonForestRB, null);
    sizeKindPanel.add(singleStoryRB, null);
    sizeKindPanel.add(multiStoryRB, null);
    controlsPanel.add(resistPanel, null);
    resistPanel.add(lowResistRB, null);
    resistPanel.add(moderateResistRB, null);
    resistPanel.add(highResistRB, null);
    controlsPanel.add(prevNextPanel, null);
    prevNextPanel.add(prevPB, null);
    mainPanel.add(diagramScrollPane, BorderLayout.CENTER);
    diagramScrollPane.getViewport().add(diagramPanel, null);
    diagramPanel.add(diagramHeaderPanel, BorderLayout.NORTH);
    diagramHeaderPanel.add(diagramHeaderText, BorderLayout.NORTH);
    diagramHeaderPanel.add(diagramHeaderLabel, null);
    diagramPanel.add(diagramInnerPanel, BorderLayout.CENTER);
    diagramInnerPanel.add(diagramPanel1, null);
    diagramPanel1.add(diagram1Panel, BorderLayout.EAST);
    diagram1Panel.add(averageCBPanel1, null);
    averageCBPanel1.add(averageCB1, BorderLayout.CENTER);
    diagram1Panel.add(extremeCBPanel1, null);
    extremeCBPanel1.add(extremeCB1, BorderLayout.CENTER);
    diagramPanel1.add(diagramTextPanel1, BorderLayout.WEST);
    diagramTextPanel1.add(diagramText1, BorderLayout.CENTER);
    diagramInnerPanel.add(diagramPanel2, null);
    diagramPanel2.add(diagram2Panel, BorderLayout.EAST);
    diagram2Panel.add(averageCBPanel2, null);
    averageCBPanel2.add(averageCB2, BorderLayout.CENTER);
    diagram2Panel.add(extremeCBPanel2, null);
    extremeCBPanel2.add(extremeCB2, BorderLayout.CENTER);
    diagramPanel2.add(diagramTextPanel2, BorderLayout.WEST);
    diagramTextPanel2.add(diagramText2, BorderLayout.WEST);
    diagramInnerPanel.add(diagramPanel3, null);
    diagramPanel3.add(diagram3Panel, BorderLayout.EAST);
    diagram3Panel.add(averageCBPanel3, null);
    averageCBPanel3.add(averageCB3, BorderLayout.NORTH);
    diagram3Panel.add(extremeCBPanel3, null);
    extremeCBPanel3.add(extremeCB3, BorderLayout.CENTER);
    diagramPanel3.add(diagramTextPanel3, BorderLayout.WEST);
    diagramTextPanel3.add(diagramText3, BorderLayout.WEST);
    prevNextPanel.add(nextPB, null);

    ButtonGroup processGroup = new ButtonGroup();
    processGroup.add(lmsfRB);
    processGroup.add(srfRB);
    positionGroup.add(aboveRB);
    positionGroup.add(belowNextRB);
    densityGroup.add(oneTwoRB);
    densityGroup.add(threeFourRB);
    sizeKindGroup.add(nonForestRB);
    sizeKindGroup.add(singleStoryRB);
    sizeKindGroup.add(multiStoryRB);
    resistanceGroup.add(lowResistRB);
    resistanceGroup.add(moderateResistRB);
    resistanceGroup.add(highResistRB);
    menuBar.add(menuFile);
    menuBar.add(menuKnowledgeSource);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuFileLoadDefaults);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);
    this.setJMenuBar(menuBar);

  }

  private void initialize() {
//    // Setup the right-click menu;
//    this.popupLowResist.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        popupLowResist_actionPerformed(e);
//      }
//    });
//    popupMenu.add(popupLowResist);
//
//    this.popupModerateResist.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        popupModerateResist_actionPerformed(e);
//      }
//    });
//    popupMenu.add(popupModerateResist);
//
//    this.popupHighResist.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        popupHighResist_actionPerformed(e);
//      }
//    });
//    popupMenu.add(popupHighResist);
//
//    this.popupFarsite.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        popupFarsite_actionPerformed(e);
//      }
//    });
//    this.popupMenuFarsite.add(popupFarsite);
//
//    int numRows, numCols;
//
//    inInit = true;
//
//    if (Simpplle.getCurrentZone().getId() == ValidZones.SOUTH_CENTRAL_ALASKA) {
//      oneTwoRB.setText("1");
//      threeFourRB.setText("2 or 3");
//    }
//    else {
//      oneTwoRB.setText("1 or 2");
//      threeFourRB.setText("3 or 4");
//    }
//
//    numCols = FireSpreadDiagrams.getNumCols();
//    numRows = FireSpreadDiagrams.getNumRows();
//
//    diagramHeaderText.setColumns(numCols);
//    diagramHeaderText.setSize(diagramHeaderText.getPreferredSize());
//    diagramHeaderText.setText(FireSpreadDiagrams.getHeader());
//    diagramHeaderText.setCaretPosition(0);
//
//    diagramText1.setColumns(numCols);
//    diagramText1.setRows(numRows);
//    diagramText1.setSize(diagramText1.getPreferredSize());
//
//    diagramText2.setColumns(numCols);
//    diagramText2.setRows(numRows);
//    diagramText2.setSize(diagramText2.getPreferredSize());
//
//    diagramText3.setColumns(numCols);
//    diagramText3.setRows(numRows);
//    diagramText3.setSize(diagramText3.getPreferredSize());
//
//    process    = "LMSF";
//    position   = "A";
//    density    = "LOW";
//    sizeKind   = "NF";
//    resistance = "LOW";
//    page       = 0;
//
//    ProcessType[] processTypes =
//        new ProcessType[] {ProcessType.LSF, ProcessType.MSF, ProcessType.SRF, ProcessType.NONE};
//
//    for(int i=0; i<processTypes.length; i++) {
//      averageCB1.addItem(processTypes[i].getShortName());
//      extremeCB1.addItem(processTypes[i].getShortName());
//
//      averageCB2.addItem(processTypes[i].getShortName());
//      extremeCB2.addItem(processTypes[i].getShortName());
//
//      averageCB3.addItem(processTypes[i].getShortName());
//      extremeCB3.addItem(processTypes[i].getShortName());
//    }
//    updateDialog();
//
//    /*
//    Check to make sure the preferred size is not large than
//    the available space on the screen.  If it is then set the
//    size to the screen width and a little smaller than the
//    screen height (to accomodate the taskbar in windows).
//    */
//    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
//    Dimension dlg = getPreferredSize();
//
//    int width  = (dlg.width > screen.width)   ? screen.width : dlg.width;
//    int height = (dlg.height > screen.height) ? screen.height - 75 : dlg.height;
//    setSize(width+10,height-15);
//
//    inInit = false;
  }

//  private void updateDialog() {
//    String[]     str = null;
//    RegionalZone zone = Simpplle.getCurrentZone();
//
//    try {
//      if (zone.getId() == ValidZones.SOUTH_CENTRAL_ALASKA) {
//        str = FireSpreadDiagrams.getDiagram(process,position,density,resistance,sizeKind,page);
//      }
//      else {
//        str = FireSpreadDiagrams.getDiagram(process,position,density,sizeKind,page);
//      }
//    }
//    catch (simpplle.comcode.SimpplleError err) {
//      return;
//    }
//
//    String tmpStr;
//
//    tmpStr = (str[0] == null) ? "" : str[0];
//    diagramText1.setText(tmpStr);
//    diagramText1.setCaretPosition(0);
//
//    tmpStr = (str[1] == null) ? "" : str[1];
//    diagramText2.setText(tmpStr);
//    diagramText2.setCaretPosition(0);
//
//    tmpStr = (str[2] == null) ? "" : str[2];
//    diagramText3.setText(tmpStr);
//    diagramText3.setCaretPosition(0);
//
//    if ((str[0] == null) && (str[1] == null) && (str[2] == null)) {
//      diagramHeaderText.setText("");
//    }
//    else {
//      diagramHeaderText.setText(FireSpreadDiagrams.getHeader());
//    }
//    diagramHeaderText.setCaretPosition(0);
//
//    if (FireSpreadDiagrams.getNumPages(sizeKind) > 1) {
//      prevPB.setEnabled(true);
//      nextPB.setEnabled(true);
//    }
//    else {
//      prevPB.setEnabled(false);
//      nextPB.setEnabled(false);
//    }
//
//    // Now set the data
//    ProcessType[][] data =
//      FireEvent.getSpreadData(process,position,density,sizeKind,resistance,page);
//
//    inInit = true;
//    averageCB1.setVisible((str[0] != null));
//    extremeCB1.setVisible((str[0] != null));
//    if (str[0] != null) {
//      averageCB1.setSelectedItem(data[0][0].getShortName());
//      extremeCB1.setSelectedItem(data[0][1].getShortName());
//    }
//
//    averageCB2.setVisible((str[1] != null));
//    extremeCB2.setVisible((str[1] != null));
//    if (str[1] != null) {
//      averageCB2.setSelectedItem(data[1][0].getShortName());
//      extremeCB2.setSelectedItem(data[1][1].getShortName());
//    }
//
//    averageCB3.setVisible((str[2] != null));
//    extremeCB3.setVisible((str[2] != null));
//    if (str[2] != null) {
//      averageCB3.setSelectedItem(data[2][0].getShortName());
//      extremeCB3.setSelectedItem(data[2][1].getShortName());
//    }
//    inInit = false;
//
//    menuFileClose.setEnabled((FireEvent.getFireSpreadDataFilename() != null));
//    menuFileSave.setEnabled((FireEvent.getFireSpreadDataFilename() != null));
////    menuFileSave.setEnabled((FireEvent.hasFireSpreadDataChanged() &&
////                             FireEvent.getFireSpreadDataFilename() != null));
//    update(getGraphics());
//  }

  void mainPanel_componentResized(ComponentEvent e) {
//    updateDialog();
  }

  private void quit() {
    setVisible(false);
    dispose();
  }

  void this_windowClosing(WindowEvent e) {
    quit();
  }

  void lmsfRB_actionPerformed(ActionEvent e) {
    page    = 0;
    process = "LMSF";
//    updateDialog();
  }

  void srfRB_actionPerformed(ActionEvent e) {
    page    = 0;
    process = "SRF";
//    updateDialog();
  }

  void aboveRB_actionPerformed(ActionEvent e) {
    page     = 0;
    position = "A";
//    updateDialog();
  }

  void belowNextRB_actionPerformed(ActionEvent e) {
    page     = 0;
    position = "BN";
//    updateDialog();
  }

  void oneTwoRB_actionPerformed(ActionEvent e) {
    page    = 0;
    density = "LOW";
//    updateDialog();
  }

  void threeFourRB_actionPerformed(ActionEvent e) {
    page    = 0;
    density = "HIGH";
//    updateDialog();
  }

  void nonForestRB_actionPerformed(ActionEvent e) {
    page    = 0;
    sizeKind = "NF";
//    updateDialog();
  }

  void singleStoryRB_actionPerformed(ActionEvent e) {
    page    = 0;
    sizeKind = "SS";
//    updateDialog();
  }

  void multiStoryRB_actionPerformed(ActionEvent e) {
    page    = 0;
    sizeKind = "MS";
//    updateDialog();
  }

  void lowResistRB_actionPerformed(ActionEvent e) {
    resistance = "LOW";
//    updateDialog();
  }

  void moderateResistRB_actionPerformed(ActionEvent e) {
    resistance = "MODERATE";
//    updateDialog();
  }

  void highResistRB_actionPerformed(ActionEvent e) {
    resistance = "HIGH";
//    updateDialog();
  }

  void prevPB_actionPerformed(ActionEvent e) {
//    page--;
//    if (page < 0) {
//      int numPages = FireSpreadDiagrams.getNumPages(sizeKind);
//      page = numPages - 1;
//    }
//    updateDialog();
  }

  void nextPB_actionPerformed(ActionEvent e) {
//    int numPages = FireSpreadDiagrams.getNumPages(sizeKind);
//
//    page++;
//    if (page == numPages) {
//      page = 0;
//    }
//    updateDialog();
  }

  void averageCB1_actionPerformed(ActionEvent e) {
//    ProcessType item = ProcessType.get((String)averageCB1.getSelectedItem());
//
//    if (item == null || inInit) { return; }
//
//    FireEvent.setSpreadData(process,position,density,sizeKind,resistance,page,
//                            0,0,item);
  }

  void extremeCB1_actionPerformed(ActionEvent e) {
//    ProcessType item = ProcessType.get((String)extremeCB1.getSelectedItem());
//    if (item == null || inInit) { return; }
//
//    FireEvent.setSpreadData(process,position,density,sizeKind,resistance,page,
//                            0,1,item);
  }

  void averageCB2_actionPerformed(ActionEvent e) {
//    ProcessType item = ProcessType.get((String)averageCB2.getSelectedItem());
//    if (item == null || inInit) { return; }
//
//    FireEvent.setSpreadData(process,position,density,sizeKind,resistance,page,
//                            1,0,item);
  }

  void extremeCB2_actionPerformed(ActionEvent e) {
//    ProcessType item = ProcessType.get((String)extremeCB2.getSelectedItem());
//    if (item == null || inInit) { return; }
//
//    FireEvent.setSpreadData(process,position,density,sizeKind,resistance,page,
//                            1,1,item);
  }

  void averageCB3_actionPerformed(ActionEvent e) {
//    ProcessType item = ProcessType.get((String)averageCB3.getSelectedItem());
//    if (item == null || inInit) { return; }
//
//    FireEvent.setSpreadData(process,position,density,sizeKind,resistance,page,
//                            2,0,item);
  }

  void extremeCB3_actionPerformed(ActionEvent e) {
//    ProcessType item = ProcessType.get((String)extremeCB3.getSelectedItem());
//    if (item == null || inInit) { return; }
//
//    FireEvent.setSpreadData(process,position,density,sizeKind,resistance,page,
//                            2,1,item);
  }

  void menuFileOpen_actionPerformed(ActionEvent e) {
//    File         inputFile;
//    MyFileFilter extFilter;
//    String       title = "Select a Fire Spread Data File";
//
//    extFilter = new MyFileFilter("firespread",
//                                 "Fire Spread Data Files (*.firespread)");
//
//    setCursor(Utility.getWaitCursor());
//    inputFile = Utility.getOpenFile(this,title,extFilter);
//
//    if (inputFile != null) {
//      try {
//        FireEvent.loadSpreadData(inputFile);
//        updateDialog();
//        menuFileSave.setEnabled(true);
//        menuFileSaveAs.setEnabled(true);
//        menuFileClose.setEnabled(true);
//      }
//      catch (simpplle.comcode.SimpplleError err) {
//        JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
//                                      JOptionPane.ERROR_MESSAGE);
//      }
//    }
//    setCursor(Utility.getNormalCursor());
//    update(getGraphics());
  }

  void menuFileClose_actionPerformed(ActionEvent e) {
//    int    choice;
//    String msg;
//
//    if (FireEvent.getFireSpreadDataFilename() != null &&
//        FireEvent.hasFireSpreadDataChanged()) {
//      msg = new String("Changes have been made.\n" +
//                       "If you continue these changes will be lost.\n\n" +
//                       "Do you wish to continue?");
//      choice = JOptionPane.showConfirmDialog(this,msg,"Close Current File.",
//                                             JOptionPane.YES_NO_OPTION,
//                                             JOptionPane.QUESTION_MESSAGE);
//
//      if (choice == JOptionPane.NO_OPTION) {
//        update(getGraphics());
//        return;
//      }
//    }
//
//    FireEvent.closeFireSpreadDataFile();
//    try {
//      Simpplle.getCurrentZone().readFireSpreadDataFile();
//    }
//    catch (simpplle.comcode.SimpplleError err) {
//      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
//                                    JOptionPane.ERROR_MESSAGE);
//    }
//    updateDialog();
  }

  void menuFileSave_actionPerformed(ActionEvent e) {
//    try {
//      FireEvent.saveSpreadData();
//    }
//    catch (SimpplleError err) {
//      JOptionPane.showMessageDialog(this,err.getError(),"Unable to write file",
//                                    JOptionPane.ERROR_MESSAGE);
//    }
//    update(getGraphics());
  }

  void menuFileSaveAs_actionPerformed(ActionEvent e) {
//    saveAs();
//    update(getGraphics());
  }

//  private void saveAs() {
//    File         outfile;
//    PrintWriter  fout;
//    MyFileFilter extFilter;
//    String       title = "Save Fire Spread Data File";
//
//    extFilter = new MyFileFilter("firespread",
//                                 "Fire Spread Data Files (*.firespread)");
//
//    outfile = Utility.getSaveFile(this,title,extFilter);
//    if (outfile != null) {
//      try {
//        FireEvent.saveSpreadDataAs(outfile);
//        menuFileSave.setEnabled(true);
//        menuFileClose.setEnabled(true);
//      }
//      catch (SimpplleError err) {
//        JOptionPane.showMessageDialog(this,err.getError(),"Unable to write file",
//                                      JOptionPane.ERROR_MESSAGE);
//      }
//    }
//  }

  void menuFileLoadDefaults_actionPerformed(ActionEvent e) {
//    int choice;
//    try {
//      String msg = new String ("This will load the default Fire Spread Data.\n\n" +
//                               "Do you wish to continue?");
//      String title = "Load Default Fire Spread Data";
//      choice = JOptionPane.showConfirmDialog(this,msg,title,
//                                             JOptionPane.YES_NO_OPTION,
//                                             JOptionPane.QUESTION_MESSAGE);
//      if (choice == JOptionPane.YES_OPTION) {
//        Simpplle.getCurrentZone().readFireSpreadDataFile();
//        FireEvent.closeFireSpreadDataFile();
//        updateDialog();
//      }
//    }
//    catch (simpplle.comcode.SimpplleError err) {
//      JOptionPane.showMessageDialog(this,err.getError(),"Unable to load file",
//                                    JOptionPane.ERROR_MESSAGE);
//    }
  }

  void menuFileQuit_actionPerformed(ActionEvent e) {
    quit();
  }

  void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
//    String str = SystemKnowledge.getSource(SystemKnowledge.FIRE_SPREAD);
//    String title = "Fire Spread Knowledge Source";
//
//    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
//    dlg.show();
  }

  private void showResistanceSpecies(FireResistance resistance) {
//    StringBuffer strBuf = new StringBuffer();
//    ArrayList    speciesList = Species.getResistanceSpecies(resistance);
//    ArrayList    groups;
//    Species      species;
//
//    for (int i=0; i<speciesList.size(); i++) {
//      species = (Species)speciesList.get(i);
//      strBuf.append(species.toString());
//      if (species.getFireResistance() == FireResistance.CONDITIONAL) {
//        groups = species.getResistanceGroups(resistance);
//        if (groups != null) {
//          strBuf.append(" (Habitat Type Group must be -> ");
//          strBuf.append(groups.toString());
//          strBuf.append(" )");
//        }
//      }
//
//      strBuf.append("\n");
//    }
//
//    String title = "";
//    if (resistance == FireResistance.LOW) { title = "Low Resistant Species"; }
//    else if (resistance == FireResistance.LOW) { title = "Moderate Resistant Species"; }
//    else if (resistance == FireResistance.LOW) { title = "High Resistant Species"; }
//
//    JTextAreaDialog dlg =
//      new JTextAreaDialog(JSimpplle.getSimpplleMain(),title,false,strBuf.toString());
//    dlg.show();
  }
  private void showLowResistanceSpecies() {
//    showResistanceSpecies(FireResistance.LOW);
  }
  private void showModerateResistanceSpecies() {
//    showResistanceSpecies(FireResistance.MODERATE);
  }
  private void showHighResistanceSpecies() {
//    showResistanceSpecies(FireResistance.HIGH);
  }

  private boolean isRightClick(MouseEvent e) {
    return (e.getModifiers () & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK;
  }
  void resistPanel_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenu.show(e.getComponent(),e.getX(),e.getY());
    }
  }
  void lowResistRB_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenu.show(e.getComponent(),e.getX(),e.getY());
    }
  }
  void moderateResistRB_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenu.show(e.getComponent(),e.getX(),e.getY());
    }
  }
  void highResistRB_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenu.show(e.getComponent(),e.getX(),e.getY());
    }
  }

  void popupLowResist_actionPerformed(ActionEvent e) {
    showLowResistanceSpecies();
  }
  void popupModerateResist_actionPerformed(ActionEvent e) {
    showModerateResistanceSpecies();
  }
  void popupHighResist_actionPerformed(ActionEvent e) {
    showHighResistanceSpecies();
  }

  void popupFarsite_actionPerformed(ActionEvent e) {
    String str = "Not yet implemented";
    JOptionPane.showMessageDialog(this,str,str,JOptionPane.INFORMATION_MESSAGE);
  }

  void averageCBPanel1_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenuFarsite.show(e.getComponent(),e.getX(),e.getY());
    }
  }
  void extremeCBPanel1_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenuFarsite.show(e.getComponent(),e.getX(),e.getY());
    }
  }
  void averageCBPanel2_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenuFarsite.show(e.getComponent(),e.getX(),e.getY());
    }
  }

  void extremeCBPanel2_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenuFarsite.show(e.getComponent(),e.getX(),e.getY());
    }
  }

  void averageCBPanel3_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenuFarsite.show(e.getComponent(),e.getX(),e.getY());
    }
  }

  void extremeCBPanel3_mouseClicked(MouseEvent e) {
    if (isRightClick(e)) {
      popupMenuFarsite.show(e.getComponent(),e.getX(),e.getY());
    }
  }



}


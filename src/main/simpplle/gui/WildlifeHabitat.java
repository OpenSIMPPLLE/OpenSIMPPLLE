package simpplle.gui;

import simpplle.comcode.SimpplleError;
import simpplle.comcode.WildlifeHabitatData;
import simpplle.JSimpplle;
import simpplle.comcode.SystemKnowledge;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This class sets up Wildlife Habitat Dialog, a type of JDialog.  This class sets up a dialog of possible mammals, birds, amphibians, reptiles, 
 * It gives user the choice of a report for entire group or individuals members of it. It is available for the two Region 1 zones.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class WildlifeHabitat extends JDialog {
  private static final int ENTIRE_GROUP       = 0;
  private static final int INDIVIDUAL_SPECIES = 1;

  public static final int MAMMALS    = simpplle.comcode.WildlifeHabitat.MAMMALS;
  public static final int BIRDS      = simpplle.comcode.WildlifeHabitat.BIRDS;
  public static final int AMPHIBIANS = simpplle.comcode.WildlifeHabitat.AMPHIBIANS;
  public static final int REPTILES   = simpplle.comcode.WildlifeHabitat.REPTILES;

  public static final int MT_GAP = simpplle.comcode.WildlifeHabitat.MT_GAP;
  public static final int ID_GAP = simpplle.comcode.WildlifeHabitat.ID_GAP;
  public static final int R1_WHR = simpplle.comcode.WildlifeHabitat.R1_WHR;

  private int mammalsSelection;
  private int birdsSelection;
  private int amphibiansSelection;
  private int reptilesSelection;

  private Frame theFrame;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel speciesGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JPanel mammalsPanel = new JPanel();
  JPanel mammalsPanel1 = new JPanel();
  ButtonGroup mammalsRBGroup = new ButtonGroup();
  JPanel mammalsGapPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JCheckBox mammalsR1CB = new JCheckBox();
  JCheckBox mammalsIdGapCB = new JCheckBox();
  JCheckBox mammalsMtGapCB = new JCheckBox();
  FlowLayout flowLayout3 = new FlowLayout();
  TitledBorder GAPModelsBorder;
  JPanel mammalsRBPanel = new JPanel();
  JRadioButton mammalsEntireGroupRB = new JRadioButton();
  GridLayout gridLayout3 = new GridLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel reptilesPanel = new JPanel();
  JPanel amphibiansPanel = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  JPanel birdsPanel = new JPanel();
  JCheckBox birdsR1CB = new JCheckBox();
  JPanel birdsRBPanel = new JPanel();
  JPanel birdsGapPanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  GridLayout gridLayout4 = new GridLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  JPanel birdsPanel1 = new JPanel();
  JCheckBox birdsIdGapCB = new JCheckBox();
  JRadioButton birdsEntireGroupRB = new JRadioButton();
  JCheckBox birdsMtGapCB = new JCheckBox();
  FlowLayout flowLayout7 = new FlowLayout();
  FlowLayout flowLayout8 = new FlowLayout();
  JCheckBox amphibiansR1CB = new JCheckBox();
  JPanel amphibiansRBPanel = new JPanel();
  JPanel amphibiansGapPanel = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  GridLayout gridLayout5 = new GridLayout();
  FlowLayout flowLayout10 = new FlowLayout();
  JPanel amphibiansPanel1 = new JPanel();
  JCheckBox amphibiansIdGapCB = new JCheckBox();
  JRadioButton amphibiansEntireGroupRB = new JRadioButton();
  JCheckBox amphibiansMtGapCB = new JCheckBox();
  JCheckBox reptilesR1CB = new JCheckBox();
  JPanel reptilesRBPanel = new JPanel();
  JPanel reptilesGapPanel = new JPanel();
  FlowLayout flowLayout11 = new FlowLayout();
  GridLayout gridLayout6 = new GridLayout();
  FlowLayout flowLayout12 = new FlowLayout();
  JPanel reptilesPanel1 = new JPanel();
  JCheckBox reptilesIdGapCB = new JCheckBox();
  JRadioButton reptilesEntireGroupRB = new JRadioButton();
  JCheckBox reptilesMtGapCB = new JCheckBox();
  ButtonGroup birdsRBGroup = new ButtonGroup();
  ButtonGroup amphibiansRBGroup = new ButtonGroup();
  ButtonGroup reptilesRBGroup = new ButtonGroup();
  TitledBorder speciesGroupBorder;
  JPanel jPanel1 = new JPanel();
  FlowLayout flowLayout13 = new FlowLayout();
  JButton generateReportPB = new JButton();
  JCheckBox mammalsCB = new JCheckBox();
  JCheckBox birdsCB = new JCheckBox();
  JCheckBox amphibiansCB = new JCheckBox();
  JCheckBox reptilesCB = new JCheckBox();
  JPanel mammalsSelectPanel = new JPanel();
  FlowLayout flowLayout14 = new FlowLayout();
  JRadioButton mammalsSelectRB = new JRadioButton();
  JButton mammalsPB = new JButton();
  JPanel birdsSelectPanel = new JPanel();
  FlowLayout flowLayout15 = new FlowLayout();
  JRadioButton birdsSelectRB = new JRadioButton();
  JButton birdsPB = new JButton();
  JPanel amphibiansSelectPanel = new JPanel();
  FlowLayout flowLayout16 = new FlowLayout();
  JRadioButton amphibiansSelectRB = new JRadioButton();
  JButton amphibiansPB = new JButton();
  JPanel reptilesSelectPanel = new JPanel();
  FlowLayout flowLayout17 = new FlowLayout();
  JRadioButton reptilesSelectRB = new JRadioButton();
  JButton reptilesPB = new JButton();
  TitledBorder modelsBorder;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu menuKnowledgeSource = new JMenu();
  JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
/**
 * 
 * @param frame
 * @param title
 * @param modal
 */
  public WildlifeHabitat(Frame frame, String title, boolean modal) {
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
/**
 * Constructor for Wildlife Habitat.  Sets the frame owner to null, title to empty string, and false for modularity.  
 */
  public WildlifeHabitat() {
    this(null, "", false);
  }
  /**
   * Sets the borders, panels, components, listeners, and text of Wildlife Habitat.  
   * @throws Exception
   */
  void jbInit() throws Exception {
    GAPModelsBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"GAP Models");
    speciesGroupBorder = new TitledBorder(BorderFactory.createEmptyBorder(),"Species Groups");
    modelsBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Models");
    mainPanel.setLayout(borderLayout1);
    speciesGroupPanel.setLayout(gridLayout1);
    gridLayout1.setRows(4);
    mammalsPanel.setLayout(flowLayout1);
    mammalsPanel1.setLayout(flowLayout3);
    mammalsGapPanel.setLayout(flowLayout2);
    mammalsR1CB.setSelected(true);
    mammalsR1CB.setText("Region 1");
    mammalsR1CB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mammalsR1CB_actionPerformed(e);
      }
    });
    mammalsIdGapCB.setSelected(true);
    mammalsIdGapCB.setText("Idaho GAP");
    mammalsIdGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mammalsIdGapCB_actionPerformed(e);
      }
    });
    mammalsMtGapCB.setSelected(true);
    mammalsMtGapCB.setText("Montana GAP");
    mammalsMtGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mammalsMtGapCB_actionPerformed(e);
      }
    });
    flowLayout3.setAlignment(FlowLayout.LEFT);
    mammalsGapPanel.setBorder(modelsBorder);
    mammalsEntireGroupRB.setSelected(true);
    mammalsEntireGroupRB.setText("Entire Group");
    mammalsEntireGroupRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mammalsEntireGroupRB_actionPerformed(e);
      }
    });
    mammalsRBPanel.setLayout(gridLayout3);
    gridLayout3.setRows(2);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    amphibiansPanel.setLayout(flowLayout6);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    birdsR1CB.setSelected(true);
    birdsR1CB.setText("Region 1");
    birdsR1CB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        birdsR1CB_actionPerformed(e);
      }
    });
    birdsRBPanel.setLayout(gridLayout4);
    birdsGapPanel.setBorder(modelsBorder);
    birdsGapPanel.setLayout(flowLayout5);
    gridLayout4.setRows(2);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    birdsPanel1.setLayout(flowLayout4);
    birdsIdGapCB.setSelected(true);
    birdsIdGapCB.setText("Idaho GAP");
    birdsIdGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        birdsIdGapCB_actionPerformed(e);
      }
    });
    birdsEntireGroupRB.setSelected(true);
    birdsEntireGroupRB.setText("Entire Group");
    birdsEntireGroupRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        birdsEntireGroupRB_actionPerformed(e);
      }
    });
    birdsMtGapCB.setSelected(true);
    birdsMtGapCB.setText("Montana GAP");
    birdsMtGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        birdsMtGapCB_actionPerformed(e);
      }
    });
    birdsPanel.setLayout(flowLayout7);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    reptilesPanel.setLayout(flowLayout8);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    amphibiansR1CB.setSelected(true);
    amphibiansR1CB.setText("Region 1");
    amphibiansR1CB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        amphibiansR1CB_actionPerformed(e);
      }
    });
    amphibiansRBPanel.setLayout(gridLayout5);
    amphibiansGapPanel.setBorder(modelsBorder);
    amphibiansGapPanel.setLayout(flowLayout9);
    gridLayout5.setRows(2);
    flowLayout10.setAlignment(FlowLayout.LEFT);
    amphibiansPanel1.setLayout(flowLayout10);
    amphibiansIdGapCB.setSelected(true);
    amphibiansIdGapCB.setText("Idaho GAP");
    amphibiansIdGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        amphibiansIdGapCB_actionPerformed(e);
      }
    });
    amphibiansEntireGroupRB.setSelected(true);
    amphibiansEntireGroupRB.setText("Entire Group");
    amphibiansEntireGroupRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        amphibiansEntireGroupRB_actionPerformed(e);
      }
    });
    amphibiansMtGapCB.setSelected(true);
    amphibiansMtGapCB.setText("Montana GAP");
    amphibiansMtGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        amphibiansMtGapCB_actionPerformed(e);
      }
    });
    reptilesR1CB.setSelected(true);
    reptilesR1CB.setText("Region 1");
    reptilesR1CB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reptilesR1CB_actionPerformed(e);
      }
    });
    reptilesRBPanel.setLayout(gridLayout6);
    reptilesGapPanel.setBorder(modelsBorder);
    reptilesGapPanel.setLayout(flowLayout11);
    gridLayout6.setRows(2);
    flowLayout12.setAlignment(FlowLayout.LEFT);
    reptilesPanel1.setLayout(flowLayout12);
    reptilesIdGapCB.setSelected(true);
    reptilesIdGapCB.setText("Idaho GAP");
    reptilesIdGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reptilesIdGapCB_actionPerformed(e);
      }
    });
    reptilesEntireGroupRB.setSelected(true);
    reptilesEntireGroupRB.setText("Entire Group");
    reptilesEntireGroupRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reptilesEntireGroupRB_actionPerformed(e);
      }
    });
    reptilesMtGapCB.setSelected(true);
    reptilesMtGapCB.setText("Montana GAP");
    reptilesMtGapCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reptilesMtGapCB_actionPerformed(e);
      }
    });
    this.setJMenuBar(jMenuBar1);
    this.setTitle("Wildlife Habitat Interpretations");
    speciesGroupPanel.setBorder(speciesGroupBorder);
    speciesGroupBorder.setTitleFont(new java.awt.Font("Dialog", 1, 12));
    speciesGroupBorder.setTitleColor(Color.blue);
    jPanel1.setLayout(flowLayout13);
    generateReportPB.setText("Generate Report");
    generateReportPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        generateReportPB_actionPerformed(e);
      }
    });
    mammalsCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    mammalsCB.setSelected(true);
    mammalsCB.setText("Mammals   ");
    mammalsCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mammalsCB_actionPerformed(e);
      }
    });
    birdsCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    birdsCB.setSelected(true);
    birdsCB.setText("Birds     ");
    birdsCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        birdsCB_actionPerformed(e);
      }
    });
    amphibiansCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    amphibiansCB.setSelected(true);
    amphibiansCB.setText("Amphibians");
    amphibiansCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        amphibiansCB_actionPerformed(e);
      }
    });
    reptilesCB.setFont(new java.awt.Font("Monospaced", 0, 12));
    reptilesCB.setSelected(true);
    reptilesCB.setText("Reptiles  ");
    reptilesCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reptilesCB_actionPerformed(e);
      }
    });
    mammalsSelectPanel.setLayout(flowLayout14);
    mammalsSelectRB.setText("Select Individuals");
    mammalsSelectRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mammalsSelectRB_actionPerformed(e);
      }
    });
    flowLayout14.setAlignment(FlowLayout.LEFT);
    flowLayout14.setHgap(0);
    flowLayout14.setVgap(0);
    mammalsPB.setEnabled(false);
    mammalsPB.setFont(new java.awt.Font("Monospaced", 0, 12));
    mammalsPB.setToolTipText("Display list of Species");
    mammalsPB.setMargin(new Insets(0, 0, 0, 0));
    mammalsPB.setText("Pick ...");
    mammalsPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mammalsPB_actionPerformed(e);
      }
    });
    birdsSelectPanel.setLayout(flowLayout15);
    flowLayout15.setAlignment(FlowLayout.LEFT);
    flowLayout15.setHgap(0);
    flowLayout15.setVgap(0);
    birdsSelectRB.setText("Select Individuals");
    birdsSelectRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        birdsSelectRB_actionPerformed(e);
      }
    });
    birdsPB.setEnabled(false);
    birdsPB.setFont(new java.awt.Font("Monospaced", 0, 12));
    birdsPB.setToolTipText("Display list of Species");
    birdsPB.setMargin(new Insets(0, 0, 0, 0));
    birdsPB.setText("Pick ...");
    birdsPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        birdsPB_actionPerformed(e);
      }
    });
    amphibiansSelectPanel.setLayout(flowLayout16);
    flowLayout16.setAlignment(FlowLayout.LEFT);
    flowLayout16.setHgap(0);
    flowLayout16.setVgap(0);
    amphibiansSelectRB.setText("Select Individuals");
    amphibiansSelectRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        amphibiansSelectRB_actionPerformed(e);
      }
    });
    amphibiansPB.setEnabled(false);
    amphibiansPB.setFont(new java.awt.Font("Monospaced", 0, 12));
    amphibiansPB.setToolTipText("Display list of Species");
    amphibiansPB.setMargin(new Insets(0, 0, 0, 0));
    amphibiansPB.setText("Pick ...");
    amphibiansPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        amphibiansPB_actionPerformed(e);
      }
    });
    reptilesSelectPanel.setLayout(flowLayout17);
    flowLayout17.setAlignment(FlowLayout.LEFT);
    flowLayout17.setHgap(0);
    flowLayout17.setVgap(0);
    reptilesSelectRB.setText("Select Individuals");
    reptilesSelectRB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reptilesSelectRB_actionPerformed(e);
      }
    });
    reptilesPB.setEnabled(false);
    reptilesPB.setFont(new java.awt.Font("Monospaced", 0, 12));
    reptilesPB.setToolTipText("Display list of Species");
    reptilesPB.setMargin(new Insets(0, 0, 0, 0));
    reptilesPB.setText("Pick ...");
    reptilesPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reptilesPB_actionPerformed(e);
      }
    });
    reptilesPanel.setBorder(BorderFactory.createEtchedBorder());
    amphibiansPanel.setBorder(BorderFactory.createEtchedBorder());
    birdsPanel.setBorder(BorderFactory.createEtchedBorder());
    mammalsPanel.setBorder(BorderFactory.createEtchedBorder());
    menuKnowledgeSource.setText("Knowledge Source");
    menuKnowledgeSourceDisplay.setText("Display");
    menuKnowledgeSourceDisplay.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuKnowledgeSourceDisplay_actionPerformed(e);
      }
    });
    reptilesPanel1.add(reptilesCB, null);
    amphibiansPanel1.add(amphibiansCB, null);
    birdsPanel1.add(birdsCB, null);
    getContentPane().add(mainPanel);
    mainPanel.add(speciesGroupPanel,  BorderLayout.NORTH);
    speciesGroupPanel.add(mammalsPanel, null);
    mammalsPanel.add(mammalsPanel1, null);
    mammalsPanel1.add(mammalsCB, null);
    mammalsPanel1.add(mammalsRBPanel, null);
    mammalsRBPanel.add(mammalsEntireGroupRB, null);
    mammalsPanel1.add(mammalsGapPanel, null);
    mammalsGapPanel.add(mammalsMtGapCB, null);
    mammalsGapPanel.add(mammalsIdGapCB, null);
    mammalsGapPanel.add(mammalsR1CB, null);
    speciesGroupPanel.add(birdsPanel, null);
    birdsPanel.add(birdsPanel1, null);
    birdsPanel1.add(birdsRBPanel, null);
    birdsRBPanel.add(birdsEntireGroupRB, null);
    birdsPanel1.add(birdsGapPanel, null);
    birdsGapPanel.add(birdsMtGapCB, null);
    birdsGapPanel.add(birdsIdGapCB, null);
    birdsGapPanel.add(birdsR1CB, null);
    speciesGroupPanel.add(amphibiansPanel, null);
    amphibiansPanel.add(amphibiansPanel1, null);
    amphibiansPanel1.add(amphibiansRBPanel, null);
    amphibiansRBPanel.add(amphibiansEntireGroupRB, null);
    amphibiansPanel1.add(amphibiansGapPanel, null);
    amphibiansGapPanel.add(amphibiansMtGapCB, null);
    amphibiansGapPanel.add(amphibiansIdGapCB, null);
    amphibiansGapPanel.add(amphibiansR1CB, null);
    speciesGroupPanel.add(reptilesPanel, null);
    reptilesPanel.add(reptilesPanel1, null);
    reptilesPanel1.add(reptilesRBPanel, null);
    reptilesRBPanel.add(reptilesEntireGroupRB, null);
    reptilesPanel1.add(reptilesGapPanel, null);
    reptilesGapPanel.add(reptilesMtGapCB, null);
    reptilesGapPanel.add(reptilesIdGapCB, null);
    reptilesGapPanel.add(reptilesR1CB, null);
    mainPanel.add(jPanel1,  BorderLayout.SOUTH);
    jPanel1.add(generateReportPB, null);
    birdsRBGroup.add(birdsEntireGroupRB);
    birdsRBGroup.add(birdsSelectRB);
    mammalsRBGroup.add(mammalsEntireGroupRB);
    mammalsRBGroup.add(mammalsSelectRB);
    amphibiansRBGroup.add(amphibiansEntireGroupRB);
    amphibiansRBGroup.add(amphibiansSelectRB);
    reptilesRBGroup.add(reptilesEntireGroupRB);
    reptilesRBGroup.add(reptilesSelectRB);
    mammalsRBPanel.add(mammalsSelectPanel, null);
    mammalsSelectPanel.add(mammalsSelectRB, null);
    mammalsSelectPanel.add(mammalsPB, null);
    birdsRBPanel.add(birdsSelectPanel, null);
    birdsSelectPanel.add(birdsSelectRB, null);
    birdsSelectPanel.add(birdsPB, null);
    amphibiansRBPanel.add(amphibiansSelectPanel, null);
    amphibiansSelectPanel.add(amphibiansSelectRB, null);
    amphibiansSelectPanel.add(amphibiansPB, null);
    reptilesRBPanel.add(reptilesSelectPanel, null);
    reptilesSelectPanel.add(reptilesSelectRB, null);
    reptilesSelectPanel.add(reptilesPB, null);
    jMenuBar1.add(menuKnowledgeSource);
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);
  }
/**
 * Initializes the selections to 0 set as a final variable ENTIRE_GROUP.  This basically sets the 
 */
  private void initialize() {
    mammalsSelection    = ENTIRE_GROUP;
    birdsSelection      = ENTIRE_GROUP;
    amphibiansSelection = ENTIRE_GROUP;
    reptilesSelection   = ENTIRE_GROUP;
  }

/**
 * Sets the title of next dialog (which will be the individual mammals selector) to Wildlife Habitat Interpretation.  
 * This method then handles the mammals push button for picking individual mammals, by calling a new wildlife Select Species dialog with Simpple main 
 * as the owner, title (set from above) and false for modality, and mammals for the group.   
 * @param e 'Pick...'
 */
  void mammalsPB_actionPerformed(ActionEvent e) {
    String title = "Wildlife Habitat Interpretations";
    int   group = MAMMALS;

    WildlifeSelectSpecies dlg =
      new WildlifeSelectSpecies(simpplle.JSimpplle.getSimpplleMain(),title,false,group);

    dlg.setVisible(true);
  }
/**
 * Handles when user selects Entire Group radio button.  It calls to do entire group method, and then enables check boxes for Mt Gap, Id Gap, and Region 1. 
 * @param e 'Entire Group'
 */
  void mammalsEntireGroupRB_actionPerformed(ActionEvent e) {
    mammalsSelection = ENTIRE_GROUP;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(MAMMALS,true);
    mammalsMtGapCB.setEnabled(true);
    mammalsIdGapCB.setEnabled(true);
    mammalsR1CB.setEnabled(true);
    mammalsPB.setEnabled(false);
  }
/**
 * Handles what happens if select individual species is selected.  Disables check boxes for MT Gap, ID Gap, and Region 1, then enables the Pick.... species button. 
 * @param e 'Select Individual'
 */
  void mammalsSelectRB_actionPerformed(ActionEvent e) {
    mammalsSelection = INDIVIDUAL_SPECIES;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(MAMMALS,false);
    mammalsMtGapCB.setEnabled(false);
    mammalsIdGapCB.setEnabled(false);
    mammalsR1CB.setEnabled(false);
    mammalsPB.setEnabled(true);
  }
/**
 * Handles Mt Gap check box checked.  Calls to Wildlife Habitat for Mt Gap
 * @param e 'Montana GAP'
 */
  void mammalsMtGapCB_actionPerformed(ActionEvent e) {
    boolean selected = mammalsMtGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(MAMMALS,MT_GAP,selected);
  }
/**
 * Handles ID Gap check box checked.  Calls to comcode for Wildlife Habitat for ID Gap
 * @param e 'Idaho GAP'
 */
  void mammalsIdGapCB_actionPerformed(ActionEvent e) {
    boolean selected = mammalsIdGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(MAMMALS,ID_GAP,selected);
  }
  /**
   * Handles Region 1 check box checked.  Calls to comcode for Wildlife Habitat for Region 1 gap.
   * @param e 'Region 1'
   */
  void mammalsR1CB_actionPerformed(ActionEvent e) {
    boolean selected = mammalsR1CB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(MAMMALS,R1_WHR,selected);
  }

  /**
   * Sets the title of next dialog (which will be the individual birds selector) to Wildlife Habitat Interpretation.  
   * This method then handles the birds Pick button for picking individual birds, by calling a new wildlife Select Species dialog with Simpple main 
   * as the owner, title (set from above) and false for modality, and BIRDS for the group.   
   * @param e 'Pick...'
   */
  void birdsPB_actionPerformed(ActionEvent e) {
    String title = "Wildlife Habitat Interpretations";
    int   group = BIRDS;

    WildlifeSelectSpecies dlg =
      new WildlifeSelectSpecies(theFrame,title,false,group);

    dlg.setVisible(true);
  }
  /**
   * Handles when user selects Entire Group radio button.  It calls to do entire group method, and then enables check boxes for Mt Gap, Id Gap, and Region 1. 
   * @param e 'Entire Group'
   */
  void birdsEntireGroupRB_actionPerformed(ActionEvent e) {
    birdsSelection = ENTIRE_GROUP;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(BIRDS,true);
    birdsMtGapCB.setEnabled(true);
    birdsIdGapCB.setEnabled(true);
    birdsR1CB.setEnabled(true);
    birdsPB.setEnabled(false);
  }
  /**
   * Handles what happens if select individual species is selected.  Disables check boxes for MT Gap, ID Gap, and Region 1, then enables the Pick.... species button. 
   * @param e 'Select Individual'
   */
  void birdsSelectRB_actionPerformed(ActionEvent e) {
    birdsSelection = INDIVIDUAL_SPECIES;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(BIRDS,false);
    birdsMtGapCB.setEnabled(false);
    birdsIdGapCB.setEnabled(false);
    birdsR1CB.setEnabled(false);
    birdsPB.setEnabled(true);
  }
  /**
   * Handles Mt Gap check box checked.  Calls to comcodes Wildlife Habitat for Mt Gap
   * @param e 'Montana GAP'
   */
  void birdsMtGapCB_actionPerformed(ActionEvent e) {
    boolean selected = birdsMtGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(BIRDS,MT_GAP,selected);
  }
  /**
   * Handles ID Gap check box checked.  Calls to comcode for Wildlife Habitat for ID Gap
   * @param e 'Idaho GAP'
   */
  void birdsIdGapCB_actionPerformed(ActionEvent e) {
    boolean selected = birdsIdGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(BIRDS,ID_GAP,selected);
  }
  /**
   * Handles Region 1 check box checked.  Calls to comcode for Wildlife Habitat for Region 1
   * @param e 'Region 1'
   */
  void birdsR1CB_actionPerformed(ActionEvent e) {
    boolean selected = birdsR1CB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(BIRDS,R1_WHR,selected);
  }

  /**
   * Sets the title of next dialog (which will be the individual Amphibians selector) to Wildlife Habitat Interpretation.  
   * This method then handles the amphibians Pick button for picking individual mammals, by calling a new wildlife Select Species dialog with Simpple main 
   * as the owner, title (set from above) and false for modality, and AMPHIBIANS for the group.   
   * @param e 'Pick...'
   */
  void amphibiansPB_actionPerformed(ActionEvent e) {
    String title = "Wildlife Habitat Interpretations";
    int   group = AMPHIBIANS;

    WildlifeSelectSpecies dlg =
      new WildlifeSelectSpecies(theFrame,title,false,group);

    dlg.setVisible(true);
  }
  /**
   * Handles when user selects Entire Group radio button.  It calls to setDoEntireGroup, and then enables check boxes for Mt Gap, Id Gap, and Region 1. 
   * @param e 'Entire Group'
   */
  void amphibiansEntireGroupRB_actionPerformed(ActionEvent e) {
    amphibiansSelection = ENTIRE_GROUP;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(AMPHIBIANS,true);
    amphibiansMtGapCB.setEnabled(true);
    amphibiansIdGapCB.setEnabled(true);
    amphibiansR1CB.setEnabled(true);
    amphibiansPB.setEnabled(false);
  }
  /**
   * Handles what happens if select individual species is selected.  Disables check boxes for MT Gap, ID Gap, and Region 1, then enables the Pick.... species button. 
   * @param e 'Select Individual'
   */
  void amphibiansSelectRB_actionPerformed(ActionEvent e) {
    amphibiansSelection = INDIVIDUAL_SPECIES;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(AMPHIBIANS,false);
    amphibiansMtGapCB.setEnabled(false);
    amphibiansIdGapCB.setEnabled(false);
    amphibiansR1CB.setEnabled(false);
    amphibiansPB.setEnabled(true);
  }
  /**
   * Handles Mt Gap check box checked.  Calls to comcodes Wildlife Habitat for Mt Gap
   * @param e 'Montana GAP'
   */
  void amphibiansMtGapCB_actionPerformed(ActionEvent e) {
    boolean selected = amphibiansMtGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(AMPHIBIANS,MT_GAP,selected);
  }
  /**
   * Handles ID Gap check box checked.  Calls to comcode's Wildlife Habitat for ID Gap
   * @param e 'Idaho GAP'
   */
  void amphibiansIdGapCB_actionPerformed(ActionEvent e) {
    boolean selected = amphibiansIdGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(AMPHIBIANS,ID_GAP,selected);
  }
  /**
   * Handles Region 1 Gap check box checked.  Calls to comcode's Wildlife Habitat for Region 1 Gap
   * @param e 'Region 1'
   */
  void amphibiansR1CB_actionPerformed(ActionEvent e) {
    boolean selected = amphibiansR1CB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(AMPHIBIANS,R1_WHR,selected);
  }

  /**
   * Sets the title of next dialog (which will be the individual reptiles selector) to Wildlife Habitat Interpretation.  
   * This method then handles the reptiles Pick button for picking individual reptiles, by calling a new wildlife Select Species dialog with Simpple main 
   * as the owner, title (set from above) and false for modality, and REPTILES for the group.   
   * @param e 'Pick...'
   */
  void reptilesPB_actionPerformed(ActionEvent e) {
    String title = "Wildlife Habitat Interpretations";
    int   group = REPTILES;

    WildlifeSelectSpecies dlg =
      new WildlifeSelectSpecies(theFrame,title,false,group);

    dlg.setVisible(true);
  }
  /**
   * Handles when user selects Entire Group radio button.  It calls to setDoEntireGroup, and then enables check boxes for Mt Gap, Id Gap, and Region 1. 
   * @param e 'Entire Group'
   */
  void reptilesEntireGroupRB_actionPerformed(ActionEvent e) {
    reptilesSelection = ENTIRE_GROUP;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(REPTILES,true);
    reptilesMtGapCB.setEnabled(true);
    reptilesIdGapCB.setEnabled(true);
    reptilesR1CB.setEnabled(true);
    reptilesPB.setEnabled(false);
  }
  /**
   * Handles what happens if select individual species is selected.  Disables check boxes for MT Gap, ID Gap, and Region 1, then enables the Pick.... species button. 
   * @param e 'Select Individual'
   */
  void reptilesSelectRB_actionPerformed(ActionEvent e) {
    reptilesSelection = INDIVIDUAL_SPECIES;
    simpplle.comcode.WildlifeHabitat.setDoEntireGroup(REPTILES,false);
    reptilesMtGapCB.setEnabled(false);
    reptilesIdGapCB.setEnabled(false);
    reptilesR1CB.setEnabled(false);
    reptilesPB.setEnabled(false);
  }
  /**
   * Handles Mt Gap check box checked.  Calls to comcode's Wildlife Habitat for Mt Gap
   * @param e 'Montana GAP'
   */
  void reptilesMtGapCB_actionPerformed(ActionEvent e) {
    boolean selected = reptilesMtGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(REPTILES,MT_GAP,selected);
  }
  /**
   * Handles ID Gap check box checked.  Calls to comcode's Wildlife Habitat for ID Gap
   * @param e 'Idaho GAP'
   */
  void reptilesIdGapCB_actionPerformed(ActionEvent e) {
    boolean selected = reptilesIdGapCB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(REPTILES,ID_GAP,selected);
  }
  /**
   * Handles Region 1 Gap check box checked.  Calls to comcode's Wildlife Habitat for Region 1 Gap
   * @param e 'Region 1'
   */
  void reptilesR1CB_actionPerformed(ActionEvent e) {
    boolean selected = reptilesR1CB.isSelected();
    simpplle.comcode.WildlifeHabitat.setDoModel(REPTILES,R1_WHR,selected);
  }
/**
 * When generate report is pushed, gets the wildlife habitat report from comcode with the results of this dialog, the title, and .txt 
 * @param e 'Generate Report
 */
  void generateReportPB_actionPerformed(ActionEvent e) {
    File         outfile;
    MyFileFilter extFilter;
    String       title = "Provide a report file name";

    extFilter = new MyFileFilter("txt",
                                 "Text Files (*.txt)");

    outfile = Utility.getSaveFile(this,title,extFilter);
    if (outfile != null) {
      try {
        simpplle.comcode.WildlifeHabitat.report(outfile);
      }
      catch (SimpplleError err) {
        JOptionPane.showMessageDialog(this,err.getError(),"Unable to write file",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }
/**
 * Handles check box for mammals checked.  Calls to comcode's setDoGroup() for MAMMALS
 * @param e 'Mammals'
 */
  void mammalsCB_actionPerformed(ActionEvent e) {
    boolean selected    = mammalsCB.isSelected();
    boolean entireGroup = mammalsEntireGroupRB.isSelected();

    simpplle.comcode.WildlifeHabitat.setDoGroup(MAMMALS,selected);

    mammalsEntireGroupRB.setEnabled(selected);
    mammalsSelectRB.setEnabled(selected);
    mammalsPB.setEnabled(selected && (!entireGroup));
    mammalsMtGapCB.setEnabled(selected && entireGroup);
    mammalsIdGapCB.setEnabled(selected && entireGroup);
    mammalsR1CB.setEnabled(selected && entireGroup);
  }
  /**
   * Handles check box for birds checked.  Calls to comcode's setDoGroup() for BIRDS
   * @param e 'Birds'
   */
  void birdsCB_actionPerformed(ActionEvent e) {
    boolean selected    = birdsCB.isSelected();
    boolean entireGroup = birdsEntireGroupRB.isSelected();

    simpplle.comcode.WildlifeHabitat.setDoGroup(BIRDS,selected);

    birdsEntireGroupRB.setEnabled(selected);
    birdsSelectRB.setEnabled(selected);
    birdsPB.setEnabled(selected && (!entireGroup));
    birdsMtGapCB.setEnabled(selected && entireGroup);
    birdsIdGapCB.setEnabled(selected && entireGroup);
    birdsR1CB.setEnabled(selected && entireGroup);
  }
  /**
   * Handles check box for amphibians checked.  Calls to comcode's setDoGroup() for AMPHIBIANS
   * @param e 'Amphibians'
   */
  void amphibiansCB_actionPerformed(ActionEvent e) {
    boolean selected    = amphibiansCB.isSelected();
    boolean entireGroup = amphibiansEntireGroupRB.isSelected();

    simpplle.comcode.WildlifeHabitat.setDoGroup(AMPHIBIANS,selected);

    amphibiansEntireGroupRB.setEnabled(selected);
    amphibiansSelectRB.setEnabled(selected);
    amphibiansPB.setEnabled(selected && (!entireGroup));
    amphibiansMtGapCB.setEnabled(selected && entireGroup);
    amphibiansIdGapCB.setEnabled(selected && entireGroup);
    amphibiansR1CB.setEnabled(selected && entireGroup);
  }
  /**
   * Handles check box for reptiles checked.  Calls to comcode's setDoGroup() for REPTILES
   * @param e 'Reptiles'
   */
  void reptilesCB_actionPerformed(ActionEvent e) {
    boolean selected    = reptilesCB.isSelected();
    boolean entireGroup = reptilesEntireGroupRB.isSelected();

    simpplle.comcode.WildlifeHabitat.setDoGroup(REPTILES,selected);

    reptilesEntireGroupRB.setEnabled(selected);
    reptilesSelectRB.setEnabled(selected);
    reptilesPB.setEnabled(selected && (!entireGroup));
    reptilesMtGapCB.setEnabled(selected && entireGroup);
    reptilesIdGapCB.setEnabled(selected && entireGroup);
    reptilesR1CB.setEnabled(selected && entireGroup);
  }

  void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.WILDLIFE);
    String title = "Wildlife Habitat Knowledge Source";

    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }
}

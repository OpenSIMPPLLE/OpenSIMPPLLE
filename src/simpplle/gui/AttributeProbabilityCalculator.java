/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.Area;
import simpplle.comcode.Evu;
import simpplle.comcode.MultipleRunSummary;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SimpplleType;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Species;
import simpplle.comcode.SizeClass;
import simpplle.comcode.Density;

import java.util.Vector;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import simpplle.comcode.*;

/** 
 * This class AttributeProbabilityCalculator GUI, a type of javax.swing.JDialog.  It is used to allow users to input frequencies of
 * processes, species, size class, and density
 *
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 *
 * @see javax.swing.JDialog
 */

public class AttributeProbabilityCalculator extends JDialog {
  private MultipleRunSummary mrSummary;
  private Area               currentArea;
  private JComboBox[]        comboBoxes;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder speciesTitle;
  TitledBorder probabilityTitle;
  TitledBorder acresTitle;
  TitledBorder emptyTitle;
  TitledBorder processTitle;
  TitledBorder sizeClassTitle;
  TitledBorder densityTitle;
  JPanel selectionPanel = new JPanel();
  FlowLayout flowLayout22 = new FlowLayout();
  JComboBox densityCB = new JComboBox();
  FlowLayout flowLayout11 = new FlowLayout();
  JPanel densityCBPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  FlowLayout flowLayout8 = new FlowLayout();
  JPanel probPanel = new JPanel();
  JPanel calcPBPanel = new JPanel();
  JLabel geLabel = new JLabel();
  FlowLayout flowLayout7 = new FlowLayout();
  JTextField probText = new JTextField();
  FlowLayout flowLayout6 = new FlowLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  JButton calcPB = new JButton();
  JPanel probabilityLabelPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel probabilityLabel = new JLabel();
  JPanel controlsPanel = new JPanel();
  JPanel geLabelPanel = new JPanel();
  JPanel sizeClassCBPanel = new JPanel();
  JComboBox sizeClassCB = new JComboBox();
  FlowLayout flowLayout111 = new FlowLayout();
  JComboBox speciesCB = new JComboBox();
  JPanel speciesCBPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel processCBPanel = new JPanel();
  JComboBox processCB = new JComboBox();
  FlowLayout flowLayout17 = new FlowLayout();
  JPanel acresPanel = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  JTextField acresText = new JTextField();
  JLabel acresLabel = new JLabel();

  /**
   * Primary constructor for Attribute Probability Calculator.  
   * @param frame
   * @param title
   * @param modal
   */
  public AttributeProbabilityCalculator(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
/**
 * Overloaded constructor for Attribute Probability Calculator.  
 */
  public AttributeProbabilityCalculator() {
    this(null, "", false);
  }
  void jbInit() throws Exception {
    speciesTitle = new TitledBorder(BorderFactory.createEmptyBorder(),"Species");
    probabilityTitle = new TitledBorder(BorderFactory.createEmptyBorder(),"Probability");
    acresTitle = new TitledBorder(BorderFactory.createEmptyBorder(),"Acres");
    emptyTitle = new TitledBorder(BorderFactory.createEmptyBorder(),"  ");
    processTitle = new TitledBorder(BorderFactory.createEmptyBorder(),"Process");
    sizeClassTitle = new TitledBorder(BorderFactory.createEmptyBorder(),"Size Class");
    densityTitle = new TitledBorder(BorderFactory.createEmptyBorder(),"Density");
    mainPanel.setLayout(borderLayout1);
    densityTitle.setTitleFont(new java.awt.Font("Dialog", 1, 12));
    sizeClassTitle.setTitleFont(new java.awt.Font("Dialog", 1, 12));
    processTitle.setTitleFont(new java.awt.Font("Dialog", 1, 12));
    speciesTitle.setTitleFont(new java.awt.Font("Dialog", 1, 12));
    probabilityTitle.setTitleFont(new java.awt.Font("Dialog", 1, 10));
    acresTitle.setTitleFont(new java.awt.Font("Dialog", 1, 10));
    this.setTitle("Attribute Probability Calulator");
    selectionPanel.setLayout(flowLayout22);
    flowLayout11.setAlignment(FlowLayout.LEFT);
    flowLayout11.setHgap(0);
    flowLayout11.setVgap(0);
    densityCBPanel.setLayout(flowLayout11);
    densityCBPanel.setBorder(densityTitle);
    centerPanel.setLayout(flowLayout8);
    probPanel.setLayout(flowLayout3);
    probPanel.setBorder(probabilityTitle);
    calcPBPanel.setLayout(flowLayout5);
    calcPBPanel.setBorder(emptyTitle);
    geLabel.setText(">=");
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout7.setHgap(0);
    flowLayout7.setVgap(2);
    probText.setColumns(7);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout6.setHgap(0);
    flowLayout6.setVgap(0);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setHgap(0);
    flowLayout5.setVgap(0);
    calcPB.setText("Calculate Acres");
    calcPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        calcPB_actionPerformed(e);
      }
    });
    probabilityLabelPanel.setLayout(flowLayout7);
    probabilityLabelPanel.setBorder(emptyTitle);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(0);
    flowLayout3.setVgap(0);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(0);
    flowLayout1.setVgap(0);
    probabilityLabel.setFont(new java.awt.Font("Dialog", 1, 12));
    probabilityLabel.setText("Probability");
    controlsPanel.setLayout(flowLayout1);
    geLabelPanel.setLayout(flowLayout6);
    geLabelPanel.setBorder(emptyTitle);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    flowLayout22.setAlignment(FlowLayout.LEFT);
    sizeClassCBPanel.setLayout(flowLayout111);
    sizeClassCBPanel.setBorder(sizeClassTitle);
    flowLayout111.setAlignment(FlowLayout.LEFT);
    flowLayout111.setHgap(0);
    flowLayout111.setVgap(0);
    speciesCBPanel.setLayout(flowLayout2);
    speciesCBPanel.setBorder(speciesTitle);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(0);
    flowLayout2.setVgap(0);
    processCBPanel.setLayout(flowLayout17);
    processCBPanel.setBorder(processTitle);
    flowLayout17.setAlignment(FlowLayout.LEFT);
    flowLayout17.setHgap(0);
    flowLayout17.setVgap(0);
    acresPanel.setLayout(flowLayout9);
    acresText.setFont(new java.awt.Font("Dialog", 0, 30));
    acresText.setEditable(false);
    acresText.setColumns(8);
    acresLabel.setFont(new java.awt.Font("Dialog", 1, 30));
    acresLabel.setText("Acres");
    acresPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    getContentPane().add(mainPanel);
    mainPanel.add(selectionPanel, BorderLayout.NORTH);
    selectionPanel.add(processCBPanel, null);
    processCBPanel.add(processCB, null);
    selectionPanel.add(speciesCBPanel, null);
    speciesCBPanel.add(speciesCB, null);
    selectionPanel.add(sizeClassCBPanel, null);
    sizeClassCBPanel.add(sizeClassCB, null);
    selectionPanel.add(densityCBPanel, null);
    densityCBPanel.add(densityCB, null);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(controlsPanel, null);
    controlsPanel.add(probabilityLabelPanel, null);
    probabilityLabelPanel.add(probabilityLabel, null);
    controlsPanel.add(geLabelPanel, null);
    geLabelPanel.add(geLabel, null);
    controlsPanel.add(probPanel, null);
    probPanel.add(probText, null);
    controlsPanel.add(calcPBPanel, null);
    calcPBPanel.add(calcPB, null);
    mainPanel.add(acresPanel, BorderLayout.SOUTH);
    acresPanel.add(acresLabel, null);
    acresPanel.add(acresText, null);
  }
/**
 * Initializes the Attribute Probability Calculater GUI with combo boxes for processes, species, size class, and density for a currrent area and simulation.
 *
 */
  private void initialize() {
    Vector v;
    JComboBox cb;
    int       i,j;

    currentArea = Simpplle.getCurrentArea();
    mrSummary   = Simpplle.getCurrentSimulation().getMultipleRunSummary();

    int[] kinds = new int[] {Evu.PROCESS.ordinal(), Evu.SPECIES.ordinal(),
                             Evu.SIZE_CLASS.ordinal(), Evu.DENSITY.ordinal()};

    comboBoxes = new JComboBox[kinds.length];
    comboBoxes[Evu.PROCESS.ordinal()]    = processCB;
    comboBoxes[Evu.SPECIES.ordinal()]    = speciesCB;
    comboBoxes[Evu.SIZE_CLASS.ordinal()] = sizeClassCB;
    comboBoxes[Evu.DENSITY.ordinal()]    = densityCB;

    for(i=0; i< kinds.length; i++) {
      v = mrSummary.getAllStateProcessNames(kinds[i]);
      v.insertElementAt("No Preference",0);
      cb = comboBoxes[kinds[i]];

      for(j=0; j<v.size(); j++) {
        cb.addItem(v.elementAt(j));
      }
    }
    update(getGraphics());
  }

  /**
   * Takes in an int between 0-100 from JTextField.  Checks to make sure it is between 0-100 - as it represents frequency percentages. 
   * @param textField input will be an int between 0-100
   * @return Integer object representing frequency
   */
  private int getFreq(JTextField textField) {
    int freq;

    try {
      freq = Integer.parseInt(textField.getText());
    }
    catch (NumberFormatException ex) {
      String msg = "Probability must be a value from 0 to 100";
      JOptionPane.showMessageDialog(this,msg,"Invalid Probability",
                                    JOptionPane.ERROR_MESSAGE);
      freq = -1;
    }
    return freq;
  }
/**
 * Calculates the frequency of occurrence for processes, species, size class, and density within an area.  
 * @param e
 */
  void calcPB_actionPerformed(ActionEvent e) {
    SimpplleType[] attributes = new SimpplleType[comboBoxes.length];
    int            freq;

    freq = getFreq(probText);
    attributes[Evu.PROCESS.ordinal()]    = ProcessType.get((String)comboBoxes[Evu.PROCESS.ordinal()].getSelectedItem());
    attributes[Evu.SPECIES.ordinal()]    = Species.get((String) comboBoxes[Evu.SPECIES.ordinal()].getSelectedItem());
    attributes[Evu.SIZE_CLASS.ordinal()] = SizeClass.get((String) comboBoxes[Evu.SIZE_CLASS.ordinal()].getSelectedItem());
    attributes[Evu.DENSITY.ordinal()]    = Density.get((String) comboBoxes[Evu.DENSITY.ordinal()].getSelectedItem());

    setCursor(Utility.getWaitCursor());
    int acres = 0;
    try {
      acres = currentArea.calculateAcreFrequencies(freq, attributes);
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(this,ex.getMessage(),"",
                                    JOptionPane.ERROR_MESSAGE);
    }
    setCursor(Utility.getNormalCursor());

    acresText.setText(Integer.toString(acres));
    update(getGraphics());
  }

}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simpplle.comcode.Lifeform;

/** 
 * This class defines the Lifeform Type Chooser, a type of JDialog.  It allows users to choose lifeforms to be used in simulation.
 * Lifeform choices are Trees, Shrubs, Herbacious, Agriculture, or No Classification.
 * 
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */

public class LifeformTypeChooser extends JDialog {
  private Lifeform chosenLife;
  private boolean  okPushed=false;

  private JPanel mainPanel = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel lifeformPanel = new JPanel();
  private JPanel buttonPanel = new JPanel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JButton cancelPB = new JButton();
  private JButton okPB = new JButton();
  private JRadioButton naRB = new JRadioButton();
  private JRadioButton agricultureRB = new JRadioButton();
  private JRadioButton herbaciousRB = new JRadioButton();
  private JRadioButton shrubsRB = new JRadioButton();
  private JRadioButton treesRB = new JRadioButton();
  private JRadioButton dominantRB = new JRadioButton();
  private GridLayout gridLayout1 = new GridLayout();
  private TitledBorder titledBorder1 = new TitledBorder("");
  private Border border1 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  private Border border2 = new TitledBorder(border1, "Lifeform Choices");
  private ButtonGroup lifeformRBGroup = new ButtonGroup();
/**
 * Constructor for Lifeform Type Chooser.  Calls to JDialog superclass and sets the dispose method to dispose on close.  
 * 
 * @param owner the frame which owns Lifeform type chooser.  
 * @param title the name of life form type chooser dialog
 * @param modal modality 
 */
  public LifeformTypeChooser(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Overloaded constructor for Lifeform Type Chooser.  
 */
  public LifeformTypeChooser() {
    this(new Frame(), "LifeformTypeChooser", false);
  }

  /**
   * Initializes the Lifeform Type Chooser with panels, components, text, borders and layouts.  
   * @throws Exception
   */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    buttonPanel.setLayout(flowLayout1);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    okPB.setText("Ok");
    okPB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    naRB.setText("No Classification");
    agricultureRB.setText("Agriculture");
    herbaciousRB.setText("Herbacious");
    shrubsRB.setText("Shrubs");
    treesRB.setText("Trees");
    dominantRB.setSelected(true);
    dominantRB.setText("Dominant Lifeform");
    lifeformPanel.setLayout(gridLayout1);
    gridLayout1.setRows(6);
    lifeformPanel.setBorder(border2);
    getContentPane().add(mainPanel);
    mainPanel.add(lifeformPanel, java.awt.BorderLayout.CENTER);
    lifeformPanel.add(dominantRB);
    lifeformPanel.add(treesRB);
    lifeformPanel.add(shrubsRB);
    lifeformPanel.add(herbaciousRB);
    lifeformPanel.add(agricultureRB);
    lifeformPanel.add(naRB);
    mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
    buttonPanel.add(okPB);
    buttonPanel.add(cancelPB);
    lifeformRBGroup.add(dominantRB);
    lifeformRBGroup.add(treesRB);
    lifeformRBGroup.add(shrubsRB);
    lifeformRBGroup.add(herbaciousRB);
    lifeformRBGroup.add(agricultureRB);
    lifeformRBGroup.add(naRB);
  }
/**
 * If ok button is pushed returns true.  
 * @return true if ok button pushed.  
 */
  public boolean okPushed() { return okPushed; }
/**
 * Method to handle the actions when OK button is pushed.  This will depend on whether dominant, trees, shrubs, herbacious, agricultrura or not applicable radio 
 * buttons are pushed.  Also sets okPushed boolean to true and makes the dialog no longer visible.  
 * @param e 'OK'
 */
  public void okPB_actionPerformed(ActionEvent e) {
    if (dominantRB.isSelected()) {
      chosenLife = null;
    }
    else if (treesRB.isSelected()) {
      chosenLife = Lifeform.TREES;
    }
    else if (shrubsRB.isSelected()) {
      chosenLife = Lifeform.SHRUBS;
    }
    else if (herbaciousRB.isSelected()) {
      chosenLife = Lifeform.HERBACIOUS;
    }
    else if (agricultureRB.isSelected()) {
      chosenLife = Lifeform.AGRICULTURE;
    }
    else if (naRB.isSelected()) {
      chosenLife = Lifeform.NA;
    }
    else {
      chosenLife = null;
    }
    okPushed = true;
    setVisible(false);
  }
/**
 * When user presses cancel button sets the choosen life to null, okpushed to false (so not to save any info from choosen radio buttons) and makes the dialog not visible.
 * 
 * @param e 'Cancel'
 */
  public void cancelPB_actionPerformed(ActionEvent e) {
    chosenLife = null;
    okPushed = false;
    setVisible(false);
  }
/**
 * Gets teh choosen life.  THis will be a lifeform of either dominant lifeform, Trees, shrubs, herbacious, agriculture, not applicable.  
 * @return
 */
  public Lifeform getChosenLife() {
    return chosenLife;
  }
}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import simpplle.comcode.*;

/**
 * This class creates an Existing Vegetative Unit Global Editor dialog.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class EvuGlobalEditor extends JDialog {
  private Frame theFrame;
  private HabitatTypeGroup oldHtGrp;
  private VegetativeType   oldVegetativeType;
  private HabitatTypeGroup newHtGrp;
  private VegetativeType   newVegetativeType;
  private Lifeform         lifeform;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel oldValuesPanel = new JPanel();
  JPanel oldHtGrpPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  JTextField oldHtGrpValue = new JTextField();
  JLabel oldhtGrpLabel = new JLabel();
  JPanel oldVegetativeTypePanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JTextField oldVegetativeTypeValue = new JTextField();
  JLabel oldVegetativeTypeLabel = new JLabel();
  TitledBorder titledBorder1;
  JPanel newValuesPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JPanel newValuesInnerPanel = new JPanel();
  GridLayout gridLayout3 = new GridLayout();
  JPanel newHtGrpPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel newVegetativeTypePanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  TitledBorder titledBorder2;
  JButton showListPB = new JButton();
  JTextField newVegetativeTypeValue = new JTextField();
  JLabel newVegetativeTypeLabel = new JLabel();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  JButton newHtGrpPB = new JButton();
  JTextField newHtGrpValue = new JTextField();
  JLabel newHtGrpLabel = new JLabel();
  JButton cancelPB = new JButton();
  JButton makeChangesPB = new JButton();
/**
 * Constructor for Evu Global Editor.  Sends to JDialog superclass with frame owner, name, and modality and has two other parameters for Evu and lifeform
 * @param frame owner of dialog
 * @param title  of dialog
 * @param modal modality
 * @param evu
 * @param lifeform
 */
  public EvuGlobalEditor(Frame frame, String title, boolean modal,
                         Evu evu, Lifeform lifeform) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.theFrame = frame;
    this.lifeform = lifeform;
    initialize(evu);
  }
/**
 * overloaded constructor for Evu Global Editor.  References default constructor and passes null for owner, empty string for name, false for modality and null for both Evu and Lifeform.
 * 
 */
  public EvuGlobalEditor() {
    this(null, "", false, null, null);
  }
/**
 * init method sets bordes, panels, layouts, components, and listeners for Evu Global Editor.  
 * @throws Exception
 */
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Original Values");
    titledBorder2 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"New Values for All Matching Invalid Units");
    mainPanel.setLayout(borderLayout1);
    oldValuesPanel.setLayout(gridLayout1);
    gridLayout1.setRows(2);
    oldHtGrpPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    oldHtGrpValue.setForeground(Color.blue);
    oldHtGrpValue.setEditable(false);
    oldHtGrpValue.setText("B3");
    oldHtGrpValue.setColumns(10);
    oldhtGrpLabel.setFont(new java.awt.Font("Monospaced", 1, 10));
    oldhtGrpLabel.setText("Ecological Grouping");
    oldVegetativeTypePanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    oldVegetativeTypeValue.setForeground(Color.blue);
    oldVegetativeTypeValue.setEditable(false);
    oldVegetativeTypeValue.setText("ALTERED-GRASSES/UNIFORM/1");
    oldVegetativeTypeValue.setColumns(37);
    oldVegetativeTypeLabel.setFont(new java.awt.Font("Monospaced", 1, 10));
    oldVegetativeTypeLabel.setText("Vegetative Type   ");
    oldValuesPanel.setBorder(titledBorder1);
    newValuesPanel.setLayout(borderLayout2);
    newValuesInnerPanel.setLayout(gridLayout3);
    gridLayout3.setRows(2);
    newHtGrpPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    newVegetativeTypePanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    newValuesInnerPanel.setBorder(titledBorder2);
    showListPB.setText("Show List");
    showListPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showListPB_actionPerformed(e);
      }
    });
    newVegetativeTypeValue.setForeground(Color.blue);
    newVegetativeTypeValue.setEditable(false);
    newVegetativeTypeValue.setText("ALTERED-GRASSES/OPEN-HERB/1");
    newVegetativeTypeValue.setColumns(37);
    newVegetativeTypeLabel.setFont(new java.awt.Font("Monospaced", 1, 10));
    newVegetativeTypeLabel.setText("Vegetative Type   ");
    buttonPanel.setLayout(flowLayout5);
    newHtGrpPB.setText("Change");
    newHtGrpPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        newHtGrpPB_actionPerformed(e);
      }
    });
    newHtGrpValue.setFont(new java.awt.Font("Monospaced", 1, 10));
    newHtGrpValue.setForeground(Color.blue);
    newHtGrpValue.setEditable(false);
    newHtGrpValue.setText("B3");
    newHtGrpValue.setColumns(10);
    newHtGrpLabel.setFont(new java.awt.Font("Monospaced", 1, 10));
    newHtGrpLabel.setText("Ecological Grouping");
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    makeChangesPB.setText("Make Changes");
    makeChangesPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        makeChangesPB_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    mainPanel.add(oldValuesPanel, BorderLayout.NORTH);
    oldValuesPanel.add(oldHtGrpPanel, null);
    oldHtGrpPanel.add(oldhtGrpLabel, null);
    oldHtGrpPanel.add(oldHtGrpValue, null);
    oldValuesPanel.add(oldVegetativeTypePanel, null);
    oldVegetativeTypePanel.add(oldVegetativeTypeLabel, null);
    oldVegetativeTypePanel.add(oldVegetativeTypeValue, null);
    mainPanel.add(newValuesPanel, BorderLayout.CENTER);
    newValuesPanel.add(newValuesInnerPanel, BorderLayout.NORTH);
    newValuesInnerPanel.add(newHtGrpPanel, null);
    newHtGrpPanel.add(newHtGrpLabel, null);
    newHtGrpPanel.add(newHtGrpValue, null);
    newHtGrpPanel.add(newHtGrpPB, null);
    newValuesInnerPanel.add(newVegetativeTypePanel, null);
    newVegetativeTypePanel.add(newVegetativeTypeLabel, null);
    newVegetativeTypePanel.add(newVegetativeTypeValue, null);
    newVegetativeTypePanel.add(showListPB, null);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(makeChangesPB, null);
    buttonPanel.add(cancelPB, null);
  }
/**
 * Initializes the Evu Global Editor using passed Evu.  Uses temporary variables to hold the current habitat and vegetative state gotten from the Evu.  
 * @param evu
 */
  private void initialize(Evu evu) {
    RegionalZone zone = Simpplle.getCurrentZone();
    HabitatTypeGroup htGrp = evu.getHabitatTypeGroup();

    if (evu.isHabitatTypeGroupValid()) {
      oldHtGrp = evu.getHabitatTypeGroup();
      newHtGrp = oldHtGrp;
    }
    else {
      oldHtGrp = evu.getHabitatTypeGroup();
      newHtGrp = HabitatTypeGroup.findInstance(newHtGrp());
    }
    VegSimStateData state = evu.getState(lifeform);
    if (state == null) { quit(); }

    oldVegetativeType = state.getVeg();

    if (htGrp == null) {
      quit();
    }
    newVegetativeTypeValue.setText("");
    makeChangesPB.setEnabled(false);
    updateDialog();
  }
/**
 * Set the old habitat type group text field to temporary habitat type group.  Uses the new habitat type group to set the text in new habitat text field
 */
  private void updateDialog() {
    oldHtGrpValue.setText(oldHtGrp.toString());
    oldVegetativeTypeValue.setText(oldVegetativeType.toString());
    newHtGrpValue.setText(newHtGrp.toString());
//    update(getGraphics());
  }
/**
 * Method to quit global Evu editor.
 */
  private void quit() {
    setVisible(false);
    dispose();
  }
/**
 * Makes a new vegetative type chooser object and passes the current Evu Global Editor frame owner, title, modality, new habitat type group and species.  
 * Then enables make changes button.  
 * @param e 'show list' button.  
 */
  void showListPB_actionPerformed(ActionEvent e) {
    String  title   = "Vegetative Type Chooser";
    Species species = oldVegetativeType.getSpecies();

    VegetativeTypeChooser dlg =
      new VegetativeTypeChooser(this,title,true,newHtGrp,species);

    dlg.setVisible(true);

    newVegetativeType = dlg.getSelection();
    if (newVegetativeType != null) {
      newVegetativeTypeValue.setText(newVegetativeType.toString());
      makeChangesPB.setEnabled(true);
    }
  }
/**
 * Makes a global unit change for the current area and passes in the old habitat type group, old vegetative type, new habitat type group, new vegetative type and lifeform.
 * @param e 'Make Changes' 
 */
  void makeChangesPB_actionPerformed(ActionEvent e) {
    Simpplle.getCurrentArea().makeGlobalUnitChange(oldHtGrp,oldVegetativeType,
                                                   newHtGrp,newVegetativeType,
                                                   lifeform);
    quit();
  }
/**
 * Gets a new habitat type group description using current zone, and a list selection dialog.  
 * @return string of habitat group choosen from the list selection dialog.
 */
  private String newHtGrp() {
    RegionalZone        zone     = Simpplle.getCurrentZone();
    ListSelectionDialog dlg;
    String              result;

    dlg = new ListSelectionDialog(theFrame,"Select a New Habitat Type Group",true,
                                  HabitatTypeGroup.getLoadedGroupNames());

    dlg.setLocation(getLocation());
    dlg.setVisible(true);

    return (String)dlg.getSelection();
  }
/**
 * Creates a new habitat type group using the new habitat type group calculated in this class.  
 * @param e 'Change'
 */
  void newHtGrpPB_actionPerformed(ActionEvent e) {
    String       result;
    RegionalZone zone = Simpplle.getCurrentZone();

    result = newHtGrp();
    if (result != null) {
      newHtGrp = HabitatTypeGroup.findInstance(result);
      updateDialog();
    }
  }
/**
 * Cancels the Evu Global Editor by quitting
 * @param e 'Cancel'
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    quit();
  }
}














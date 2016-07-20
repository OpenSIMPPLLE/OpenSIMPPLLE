/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.Species;
import simpplle.comcode.VegetativeType;

/** 
 * This class sets up Vegetative Type Chooser, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */
public class VegetativeTypeChooser extends JDialog {
  private HabitatTypeGroup htGrp;
  private Species          species;
  private VegetativeType   selection;
  private VegetativeType[] selectedValues;
  private boolean          inInit;
  private boolean          multiSelect;

  private String protoCellValue =
    "XERIC_FS_SHRUBS/CLOSED-TALL-SHRUB10/1          ";

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel selectionPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel selectSpeciesPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  JComboBox speciesCB = new JComboBox();
  JLabel selectSpeciesLabel = new JLabel();
  JPanel jPanel1 = new JPanel();
  JList vegetativeTypeList = new JList();
  JScrollPane vegetativeTypeListScroll = new JScrollPane();
  BorderLayout borderLayout4 = new BorderLayout();

  public VegetativeTypeChooser(JDialog dialog, String title, boolean modal,
                               HabitatTypeGroup htGrp, Species species, boolean multiSelect) {
    super(dialog, title, modal);
    try {
      jbInit();
      initialize(htGrp,species,multiSelect);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public VegetativeTypeChooser(JDialog dialog, String title, boolean modal,
                               HabitatTypeGroup htGrp, Species species) {
    this(dialog,title,modal,htGrp,species,false);
  }
  public VegetativeTypeChooser(JDialog dialog, String title, boolean modal,
                               HabitatTypeGroup htGrp) {
    this(dialog,title,modal,htGrp,null,false);
  }
  public VegetativeTypeChooser(JDialog dialog, String title, boolean modal) {
    this(dialog,title,modal,null,null,false);
  }
  public VegetativeTypeChooser(JDialog dialog, String title, boolean modal, boolean multiSelect) {
    this(dialog,title,modal,null,null,multiSelect);
  }

  public VegetativeTypeChooser() {
    this(null, "", false,null,null,false);
  }
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    selectionPanel.setLayout(borderLayout2);
    centerPanel.setLayout(borderLayout3);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    okPB.setEnabled(false);
    okPB.setText("Ok");
    okPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    buttonPanel.setLayout(flowLayout1);
    selectSpeciesPanel.setLayout(flowLayout2);
    selectSpeciesLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    selectSpeciesLabel.setText("Select a Species");
    flowLayout2.setAlignment(FlowLayout.LEFT);
    speciesCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesCB_actionPerformed(e);
      }
    });
    this.setTitle("Vegetative Type Chooser");
    jPanel1.setLayout(borderLayout4);
    vegetativeTypeList.setPrototypeCellValue(protoCellValue);
    vegetativeTypeList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        vegetativeTypeList_mouseClicked(e);
      }
    });
    vegetativeTypeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        vegetativeTypeList_valueChanged(e);
      }
    });
    getContentPane().add(mainPanel);
    mainPanel.add(selectionPanel, BorderLayout.NORTH);
    selectionPanel.add(selectSpeciesPanel, BorderLayout.NORTH);
    selectSpeciesPanel.add(selectSpeciesLabel, null);
    selectSpeciesPanel.add(speciesCB, null);
    selectionPanel.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(vegetativeTypeListScroll, BorderLayout.CENTER);
    vegetativeTypeListScroll.getViewport().add(vegetativeTypeList, null);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.CENTER);
    buttonPanel.add(okPB, null);
    buttonPanel.add(cancelPB, null);
  }

  public VegetativeType   getSelection() { return selection; }
  public VegetativeType[] getSelectedValues() { return selectedValues; }

  private void initialize(HabitatTypeGroup aHtGrp, Species aSpecies, boolean aMultiSelect) {
    htGrp          = aHtGrp;
    species        = aSpecies;
    multiSelect    = aMultiSelect;
    selection      = null;
    selectedValues = null;

    inInit = true;
    Species[] allSpecies;
    int       speciesIndex = 0;

    vegetativeTypeList.setSelectionMode(
      (multiSelect ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION :
                     ListSelectionModel.SINGLE_SELECTION));

    if (htGrp == null) {
      Vector tmpSpecies = HabitatTypeGroup.getValidSpecies();
      allSpecies = new Species[tmpSpecies.size()];
      for(int j=0; j<tmpSpecies.size(); j++) {
        allSpecies[j] = (Species)tmpSpecies.elementAt(j);
      }
    }
    else {
      allSpecies = htGrp.getAllSpecies();
    }

    if (species == null && allSpecies != null) { species = allSpecies[0]; }
    for(int i=0; i<allSpecies.length; i++) {
      if (species == allSpecies[i]) { speciesIndex = i; }
      speciesCB.addItem(allSpecies[i]);
    }
    speciesCB.setSelectedIndex(speciesIndex);
    updateDialog();
    inInit = false;
  }

  private void updateDialog() {
    if (species == null) { return; }

    String[] vegTypes;
    if (htGrp == null) {
      vegTypes = HabitatTypeGroup.getAllSortedMatchingSpeciesTypes(species);
    }
    else  {
      vegTypes = htGrp.getSortedMatchingSpeciesTypes(species);
    }
    vegetativeTypeList.setListData(vegTypes);
    selection      = null;
    selectedValues = null;
    update(getGraphics());
  }

  void speciesCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    Species item = (Species) speciesCB.getSelectedItem();
    if (item != null) {
      species = item;
      updateDialog();
    }
  }

  void vegetativeTypeList_valueChanged(ListSelectionEvent e) {
    okPB.setEnabled((vegetativeTypeList.isSelectionEmpty() == false));
  }

  void vegetativeTypeList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2 && (!multiSelect)) { done(); }
  }

  private void done() {
    if (multiSelect) {
      selection = null;
      doneMultipleSelection();
    }

    String selectedValue = (String)vegetativeTypeList.getSelectedValue();
    if (htGrp == null) {
      selection = HabitatTypeGroup.getVegType(selectedValue);
    }
    else {
      selection = htGrp.getVegetativeType(selectedValue);
    }
    setVisible(false);
    dispose();
  }

  private void doneMultipleSelection() {
    Object[] values = (Object[])vegetativeTypeList.getSelectedValues();
    selectedValues  = new VegetativeType[values.length];

    for (int i=0; i<values.length; i++) {
      if (htGrp == null) {
        selectedValues[i] = HabitatTypeGroup.getVegType((String)values[i]);
      }
      else {
        selectedValues[i] = htGrp.getVegetativeType((String)values[i]);
      }
    }
    setVisible(false);
    dispose();
  }

  void okPB_actionPerformed(ActionEvent e) {
    done();
  }

  void cancelPB_actionPerformed(ActionEvent e) {
    selection      = null;
    selectedValues = null;
    setVisible(false);
    dispose();
  }
}
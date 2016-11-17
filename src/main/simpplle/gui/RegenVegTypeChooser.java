/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.Species;
import simpplle.comcode.VegetativeType;
import java.util.ArrayList;

/**
 * This class creates the Regeneration Succession Chooser dialog, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class RegenVegTypeChooser extends JDialog {
  private Species          species;
  private DefaultListModel sourceListModel;
  private DefaultListModel targetListModel;
  private Hashtable        targetLookupHt;
  private boolean          inInit;
  private VegetativeType[] vegTypes;
  private boolean          multiValued;

  private String protoCellValue =
    "XERIC_FS_SHRUBS/CLOSED-TALL-SHRUB10/1          ";

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel listsPanel = new JPanel();
  JPanel SourceTargetPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel targetPanel = new JPanel();
  JPanel sourcePanel = new JPanel();
  JScrollPane sourceScrollPane = new JScrollPane();
  DragSourceList sourceList = new DragSourceList();
  JScrollPane targetScrollPane = new JScrollPane();
  DragDropList targetList = new DragDropList();
  BorderLayout borderLayout2 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel jPanel1 = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JComboBox sourceSpeciesCB = new JComboBox();
  JLabel sourceSpeciesLabel = new JLabel();
  JPanel jPanel2 = new JPanel();
  JLabel infoLabel3 = new JLabel();
  JLabel infoLabel2 = new JLabel();
  JLabel infoLabel1 = new JLabel();

  public RegenVegTypeChooser(JDialog dialog, String title, boolean modal,
                             Species species, ArrayList<VegetativeType> targetStates, boolean multiValued)
  {
    super(dialog, title, modal);
    try {
      jbInit();
      this.multiValued = multiValued;
      targetList.setMultiValued(multiValued);
      initialize(species, targetStates);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public RegenVegTypeChooser() {
    this(null, "", false,null,null,true);
  }
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Available States");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Chosen States (1st is highest priority)");
    mainPanel.setLayout(borderLayout1);
    listsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(0);
    flowLayout1.setVgap(0);
    SourceTargetPanel.setLayout(borderLayout2);
    sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.Y_AXIS));
    targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.Y_AXIS));
    sourcePanel.setBorder(titledBorder1);
    targetPanel.setBorder(titledBorder2);
    jPanel1.setLayout(flowLayout2);
    sourceSpeciesLabel.setText("Select a Species");
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setVgap(0);
    sourceSpeciesCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sourceSpeciesCB_actionPerformed(e);
      }
    });
    targetList.setToolTipText("");
    targetList.setPrototypeCellValue(protoCellValue);
    targetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sourceList.setPrototypeCellValue(protoCellValue);
    this.setModal(true);
    jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
    infoLabel2.setFont(new java.awt.Font("Monospaced", 1, 12));
    infoLabel2.setText("Use mouse to order shapes as desired.");
    infoLabel1.setFont(new java.awt.Font("Monospaced", 1, 12));
    infoLabel1.setText("Drag shapes to chosen list on right.");
    infoLabel3.setFont(new java.awt.Font("Monospaced", 1, 12));
    infoLabel3.setText("Most preferred state is first in list.");
    jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
    titledBorder2.setTitle("Chosen States");
    SourceTargetPanel.add(sourcePanel, BorderLayout.WEST);
    getContentPane().add(mainPanel);
    mainPanel.add(listsPanel,  BorderLayout.CENTER);
    listsPanel.add(SourceTargetPanel, null);
    SourceTargetPanel.add(targetPanel,  BorderLayout.EAST);
    sourcePanel.add(jPanel1, null);
    jPanel1.add(sourceSpeciesLabel, null);
    jPanel1.add(sourceSpeciesCB, null);
    sourcePanel.add(sourceScrollPane, null);
    sourceScrollPane.getViewport().add(sourceList, null);
    targetPanel.add(targetScrollPane, null);
    mainPanel.add(jPanel2,  BorderLayout.SOUTH);
    jPanel2.add(infoLabel1, null);
    jPanel2.add(infoLabel2, null);
    jPanel2.add(infoLabel3, null);
    targetScrollPane.getViewport().add(targetList, null);
  }

  private void initialize(Species aSpecies, ArrayList<VegetativeType> chosenStates) {
    species        = aSpecies;
    targetLookupHt = new Hashtable();

    inInit = true;
    Species[] allSpecies;
    int       speciesIndex = 0;

    Vector tmpSpecies = HabitatTypeGroup.getValidSpecies();
    allSpecies = new Species[tmpSpecies.size()];
    for(int j=0; j<tmpSpecies.size(); j++) {
      allSpecies[j] = (Species)tmpSpecies.elementAt(j);
    }

    if (species == null && allSpecies != null) { species = allSpecies[0]; }
    for(int i=0; i<allSpecies.length; i++) {
      if (species == allSpecies[i]) { speciesIndex = i; }
      sourceSpeciesCB.addItem(allSpecies[i]);
    }
    sourceSpeciesCB.setSelectedIndex(speciesIndex);

    sourceListModel = new DefaultListModel();
    targetListModel = new DefaultListModel();

    sourceList.setModel(sourceListModel);
    targetList.setModel(targetListModel);

    String           stateStr;
    VegetativeType   vt;
    for (int j=0; j<chosenStates.size(); j++) {
      vt       = (VegetativeType)chosenStates.get(j);
      stateStr = vt.toString();
      targetListModel.addElement(stateStr);
      targetLookupHt.put(stateStr,vt);
    }

    vegTypes = HabitatTypeGroup.getAllRegenerationStates();
    updateDialog();

    if (!multiValued) {
      infoLabel2.setVisible(false);
      infoLabel3.setVisible(false);
    }
    inInit = false;
  }

  private void updateDialog() {
    if (species == null) { return; }

    sourceListModel.removeAllElements();
    for (int i=0; i<vegTypes.length; i++) {
      if (species == vegTypes[i].getSpecies()) {
        sourceListModel.addElement(vegTypes[i].toString());
      }
    }
    update(getGraphics());
  }

  void sourceSpeciesCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    Species item = (Species) sourceSpeciesCB.getSelectedItem();
    if (item != null) {
      species = item;
      updateDialog();
    }
  }

  /**
   * This method will clear the elements from the vector passed to this dialog
   * and replace them with the new list created by the user.
   * This will update the original vector passed from outside thus there is
   * no need for this Dialog to return anything.
   */
  public void finishUp(ArrayList<VegetativeType> v) {
    String         stateStr;
    VegetativeType vt;

    v.clear();
    for (int i=0; i<targetListModel.getSize(); i++) {
      stateStr = (String)targetListModel.elementAt(i);
      vt = (VegetativeType)targetLookupHt.get(stateStr);
      if (vt == null) { vt = HabitatTypeGroup.getVegType(stateStr); }
      v.add(vt);
    }
  }


}







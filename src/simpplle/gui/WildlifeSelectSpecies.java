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
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;

import simpplle.JSimpplle;
import simpplle.comcode.Formatting;
import simpplle.comcode.WildlifeHabitat;
import simpplle.comcode.WildlifeHabitatData;

/** 
 * This class sets up Wildlife Select Species, a type of JDialog.  This class sets up a dialog to choose individual mammals, birds, amphibians, or reptiles.
 * It gives user the choice of a report for entire group or individuals members of it.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class WildlifeSelectSpecies extends JDialog {
  public static final int MT_GAP = simpplle.comcode.WildlifeHabitat.MT_GAP;
  public static final int ID_GAP = simpplle.comcode.WildlifeHabitat.ID_GAP;
  public static final int R1_WHR = simpplle.comcode.WildlifeHabitat.R1_WHR;

  boolean montanaValid, idahoValid, region1Valid;

  JPanel[]    listItemPanel;
  JCheckBox[] speciesCB;
  JButton[]   speciesPB;

  Border      listItemBorder;

  JPanel[]    gapPanel;
  JCheckBox[] montanaCB, idahoCB, region1CB;
  FlowLayout  gapFlowLayout;
  FlowLayout  listItemFlowLayout;

  int lastAdded = 0;
  int group;
  Hashtable speciesIndex = new Hashtable();

  String spaces;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane scrollPane = new JScrollPane();
  JPanel scrollPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  GridLayout listGridLayout = new GridLayout();
  JPanel listPanel = new JPanel();
  JLabel listLabel = new JLabel();

/**
 * Constructor for Wildlife Selector Gui.  This is called from the Wildlife Habitat dialog 
 * @param frame
 * @param title if passed in will be "Wildlife Habitat Interpretations"
 * @param modal
 * @param group choices are mammals, birds, amphibians, or reptiles
 */
  public WildlifeSelectSpecies(Frame frame, String title, boolean modal, int group) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.group = group;
    initialize();
  }
/**
 * Overloaded constructor for wildlife selector.  This one sets the owner to null, title to empty string, modality to false.  This constructor sets the species group to mammals by default. 
 * 
 */
  public WildlifeSelectSpecies() {
    this(null, "", false,simpplle.comcode.WildlifeHabitat.MAMMALS);
  }
/**
 * Sets the panels, layouts, components, text, colors, for wildlife chooser.  
 * @throws Exception
 */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    scrollPanel.setLayout(gridLayout1);
    gridLayout1.setColumns(2);
    listGridLayout.setRows(3);
    listGridLayout.setVgap(0);
    listPanel.setLayout(listGridLayout);
    listLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    listLabel.setForeground(Color.blue);
    listLabel.setHorizontalAlignment(SwingConstants.CENTER);
    listLabel.setText("Species / Models");
    getContentPane().add(mainPanel);
    mainPanel.add(scrollPane,  BorderLayout.CENTER);
    scrollPane.getViewport().add(listPanel, null);
    listPanel.add(listLabel, null);
  }
/**
 * Initializes the Wildlife Selector with check boxes for all the species of a particular wildlife group (mammals, birds, amphibians, or reptiles)
 * and check boxes for the gap models (Montana, Idaho or Region 1)
 */
  private void initialize() {
    Vector  groupSpecies = simpplle.comcode.WildlifeHabitat.getAllGroupSpecies(group);
    int     numSpecies = groupSpecies.size();
    lastAdded = -1;

    listItemFlowLayout = new FlowLayout();
    listItemFlowLayout.setAlignment(FlowLayout.LEFT);
    listItemFlowLayout.setHgap(5);
    listItemFlowLayout.setVgap(0);

    gapFlowLayout = new FlowLayout();
    gapFlowLayout.setAlignment(FlowLayout.LEFT);
    gapFlowLayout.setHgap(0);
    gapFlowLayout.setVgap(0);

    listItemPanel  = new JPanel[numSpecies];
    listItemBorder = BorderFactory.createEtchedBorder();
    speciesCB      = new JCheckBox[numSpecies];
    speciesPB      = new JButton[numSpecies];


//    gapPanel  = new JPanel[numSpecies];
    montanaCB = new JCheckBox[numSpecies];
    idahoCB   = new JCheckBox[numSpecies];
    region1CB = new JCheckBox[numSpecies];

    listGridLayout.setRows(numSpecies+1);

    String str = WildlifeHabitat.getGroupName(group) + " / Models";
    listLabel.setText(str);

    montanaValid = WildlifeHabitat.isModelValid(MT_GAP);
    idahoValid   = WildlifeHabitat.isModelValid(ID_GAP);
    region1Valid = WildlifeHabitat.isModelValid(R1_WHR);

    WildlifeHabitatData data;
    String  species;
    boolean selected;
    int     i;

    // Find the longest species name;
    int maxSpeciesLength = -1;
    for(i=0; i<groupSpecies.size(); i++) {
      species = (String)groupSpecies.elementAt(i);
      if (species.length() > maxSpeciesLength) { maxSpeciesLength = species.length(); }
    }

    boolean speciesSelected;
    for(i=0; i<groupSpecies.size(); i++) {
      species          = (String)groupSpecies.elementAt(i);
      speciesSelected = WildlifeHabitat.doSpecies(species);

      addNewListItemPanel(species, maxSpeciesLength);

      data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,MT_GAP);
      speciesCB[lastAdded].setSelected(speciesSelected);

      montanaCB[lastAdded].setEnabled((data != null) && speciesSelected && montanaValid);
      selected = (data != null) ? data.isSelected() : false;
      montanaCB[lastAdded].setSelected(selected);

      data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,ID_GAP);
      idahoCB[lastAdded].setEnabled((data != null) && speciesSelected && idahoValid);
      selected = (data != null) ? data.isSelected() : false;
      idahoCB[lastAdded].setSelected(selected);

      data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,R1_WHR);
      region1CB[lastAdded].setEnabled((data != null) && speciesSelected && region1Valid);
      selected = (data != null) ? data.isSelected() : false;
      region1CB[lastAdded].setSelected(selected);
    }

    setSize(getPreferredSize());
    update(getGraphics());
  }
/**
 * This method is what actually adds the species to the list panel of the wildlife selector dialog.  
 * @param speciesName name of species i.e "Black-footed Ferret"
 * @param maxSpeciesLength how many species in a group
 */
  private void addNewListItemPanel(String speciesName, int maxSpeciesLength) {
    String paddedName = Formatting.padRight(speciesName,maxSpeciesLength - speciesName.length());

    lastAdded++;
    listItemPanel[lastAdded] = new JPanel(listItemFlowLayout);
    listItemPanel[lastAdded].setBorder(listItemBorder);

    speciesIndex.put(speciesName,new Integer(lastAdded));

    speciesCB[lastAdded] = new JCheckBox(paddedName);
    speciesCB[lastAdded].setFont(new java.awt.Font("Monospaced", 1, 12));
    speciesPB[lastAdded] = new JButton();
    speciesPB[lastAdded].setIcon(new ImageIcon(ClassLoader.getSystemResource("simpplle/gui/images/Info.gif")));
    speciesPB[lastAdded].setToolTipText("Press for more Info");
    speciesPB[lastAdded].setMargin(new Insets(0, 0, 0, 0));

//    gapPanel[lastAdded]  = new JPanel(gapFlowLayout);
    montanaCB[lastAdded] = new JCheckBox("Montana Gap");
    idahoCB[lastAdded]   = new JCheckBox("Idaho Gap");
    region1CB[lastAdded] = new JCheckBox("Region 1");

    listItemPanel[lastAdded].add(speciesPB[lastAdded],0);
    listItemPanel[lastAdded].add(speciesCB[lastAdded],1);

    listItemPanel[lastAdded].add(montanaCB[lastAdded],2);
    listItemPanel[lastAdded].add(idahoCB[lastAdded],3);
    listItemPanel[lastAdded].add(region1CB[lastAdded],4);
    listPanel.add(listItemPanel[lastAdded],lastAdded+1);

    // Add the event handlers
    final int speciesPBindex = lastAdded;
    speciesPB[lastAdded].addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        speciesPB_actionPerformed(e,speciesPBindex);
      }
    });

    final int montanaCBindex = lastAdded;
    montanaCB[lastAdded].addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        montanaCB_itemStateChanged(e,montanaCBindex);
      }
    });

    final int idahoCBindex = lastAdded;
    idahoCB[lastAdded].addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        idahoCB_itemStateChanged(e,idahoCBindex);
      }
    });

    final int region1CBindex = lastAdded;
    region1CB[lastAdded].addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        region1CB_itemStateChanged(e,region1CBindex);
      }
    });

    final int speciesCBindex = lastAdded;
    speciesCB[lastAdded].addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        speciesCB_itemStateChanged(e,speciesCBindex);
      }
    });
  }
/**
 * Handles when a species combo box is selected along with the GAP check boxes. 
 * @param e
 * @param index
 */
  private void speciesCB_itemStateChanged(ItemEvent e, int index) {
    WildlifeHabitatData data;
    String  species = speciesCB[index].getText().trim();
    boolean selected;
    boolean itemSelected = (e.getStateChange() == ItemEvent.SELECTED);

    WildlifeHabitat.setDoSpecies(species,itemSelected);

    data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,MT_GAP);
    montanaCB[index].setEnabled(itemSelected && data != null && montanaValid);
    selected = (data != null) ? data.isSelected() : false;
    montanaCB[index].setSelected(selected);

    data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,ID_GAP);
    idahoCB[index].setEnabled(itemSelected && data != null && idahoValid);
    selected = (data != null) ? data.isSelected() : false;
    idahoCB[index].setSelected(selected);

    data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,R1_WHR);
    region1CB[index].setEnabled(itemSelected && data != null && region1Valid);
    selected = (data != null) ? data.isSelected() : false;
    region1CB[index].setSelected(selected);
  }
/**
 * If check box for a particular species is checked.  
 * @param e check box for particular species selected.
 * @param index the index into species check box array where a particular species (that was selected) can be found
 */
  private void speciesPB_actionPerformed(ActionEvent e, int index) {
    String species = speciesCB[index].getText().trim();
    String title = species;

    WildlifeShowSpecies dlg =
      new WildlifeShowSpecies(JSimpplle.getSimpplleMain(),title,true,species);

    dlg.setVisible(true);
  }
  /**
   * If check box for MT gap is checked.  
   * @param e 'Montana GAP'
   * @param index the index of a particular species in the Wildlife Selector
   */
  private void montanaCB_itemStateChanged(ItemEvent e, int index) {
    WildlifeHabitatData data;
    String species = speciesCB[index].getText().trim();

    data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,MT_GAP);
    if (data == null) { return; }
    data.setSelected(montanaCB[index].isSelected());
  }
  /**
   * If check box for ID gap is checked.  
   * @param e 'Idaho GAP'
   * @param index the index of a particular species in the Wildlife Selector
   */
  private void idahoCB_itemStateChanged(ItemEvent e, int index) {
    WildlifeHabitatData data;
    String species = speciesCB[index].getText().trim();

    data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,ID_GAP);
    if (data == null) { return; }
    data.setSelected(idahoCB[index].isSelected());
  }
/**
 * If check box for Region 1 gap is checked.  
 * @param e 'Region 1'
 * @param index the index of a particular species in the Wildlife Selector
 */
  private void region1CB_itemStateChanged(ItemEvent e, int index) {
    WildlifeHabitatData data;
    String species = speciesCB[index].getText().trim();

    data = simpplle.comcode.WildlifeHabitat.getSpeciesModelData(species,R1_WHR);
    if (data == null) { return; }
    data.setSelected(region1CB[index].isSelected());
  }

}







/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.Area;
import simpplle.comcode.Simpplle;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.VegetativeType;
import simpplle.comcode.Species;
import simpplle.comcode.SizeClass;
import simpplle.comcode.Density;

import java.util.Vector;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/** 
 * This class allows users to create a new vegetative state.  This is used in vegetative Pathways. Vegetative shapes include species, size class, age, density, and habitat type group.
 * The dialog is titled "Create a New State"
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class PathwayNewState extends JDialog {
  private String    protoCellValue = "FTH-X     ";
  private String[]  htGrps;
  private Species   species;
  private int       age;
  private boolean   focusLost = false;
  private Frame     theFrame;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel valuesPanel1 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JPanel speciesPanel = new JPanel();
  JPanel densityPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel speciesValue = new JLabel();
  JLabel speciesLabel = new JLabel();
  FlowLayout flowLayout3 = new FlowLayout();
  JComboBox densityCB = new JComboBox();
  JLabel densityLabel = new JLabel();
  JPanel buttonPanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JButton CancelPB = new JButton();
  JButton createPB = new JButton();
  JPanel agePanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel sizeClassPanel = new JPanel();
  JComboBox sizeClassCB = new JComboBox();
  JLabel sizeClassLabel = new JLabel();
  FlowLayout flowLayout5 = new FlowLayout();
  JTextField ageText = new JTextField();
  JLabel ageLabel = new JLabel();
  JPanel northPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel htGrpPanel = new JPanel();
  JPanel valuesPanel = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  FlowLayout flowLayout7 = new FlowLayout();
  JPanel htGrpPanel1 = new JPanel();
  FlowLayout flowLayout8 = new FlowLayout();
  JButton htGrpChangePB = new JButton();
  TitledBorder htGrpBorder;
  JScrollPane htGrpScroll = new JScrollPane();
  JList htGrpList = new JList();
/**
 * Constructor for 'Create a New State' dialog.
 * @param frame
 * @param title
 * @param modal
 * @param htGrp
 * @param species
 */
  public PathwayNewState(Frame frame, String title, boolean modal,
                         String htGrp, Species species)
  {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.theFrame  = frame;
    this.htGrps    = new String[1];
    this.htGrps[0] = htGrp;
    this.species   = species;
    initialize();
  }

  public PathwayNewState() {
    this(null, "", false, null, null);
  }
  void jbInit() throws Exception {
    htGrpBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black,2),"Ecological Grouping");
    mainPanel.setLayout(borderLayout1);
    valuesPanel1.setLayout(gridLayout1);
    gridLayout1.setRows(4);
    speciesPanel.setLayout(flowLayout1);
    speciesValue.setFont(new java.awt.Font("Monospaced", 1, 14));
    speciesValue.setForeground(Color.blue);
    speciesValue.setText("PP");
    speciesLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    speciesLabel.setText("Species   ");
    flowLayout1.setAlignment(FlowLayout.LEFT);
    densityPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    densityLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    densityLabel.setText("Density   ");
    buttonPanel.setLayout(flowLayout4);
    CancelPB.setText("Cancel");
    CancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CancelPB_actionPerformed(e);
      }
    });
    createPB.setText("Create");
    createPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        createPB_actionPerformed(e);
      }
    });
    flowLayout2.setAlignment(FlowLayout.LEFT);
    sizeClassPanel.setLayout(flowLayout2);
    sizeClassLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    sizeClassLabel.setText("Size Class");
    agePanel.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    ageText.setText("1");
    ageText.setColumns(5);
    ageText.setHorizontalAlignment(SwingConstants.RIGHT);
    ageText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        ageText_focusLost(e);
      }
    });
    ageText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ageText_actionPerformed(e);
      }
    });
    ageLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    ageLabel.setText("Age       ");
    northPanel.setLayout(borderLayout2);
    valuesPanel.setLayout(flowLayout6);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    flowLayout6.setHgap(0);
    flowLayout6.setVgap(0);
    htGrpPanel.setLayout(flowLayout7);
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout7.setHgap(0);
    flowLayout7.setVgap(0);
    htGrpPanel1.setLayout(flowLayout8);
    htGrpChangePB.setText("Change");
    htGrpChangePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        htGrpChangePB_actionPerformed(e);
      }
    });
    htGrpPanel1.setBorder(htGrpBorder);
    htGrpBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    this.setModal(true);
    htGrpList.setPrototypeCellValue(protoCellValue);
    htGrpList.setVisibleRowCount(6);
    getContentPane().add(mainPanel);
    valuesPanel.add(valuesPanel1,BorderLayout.NORTH);
    valuesPanel1.add(speciesPanel, null);
    speciesPanel.add(speciesLabel, null);
    speciesPanel.add(speciesValue, null);
    valuesPanel1.add(sizeClassPanel, null);
    sizeClassPanel.add(sizeClassLabel, null);
    sizeClassPanel.add(sizeClassCB, null);
    valuesPanel1.add(agePanel, null);
    agePanel.add(ageLabel, null);
    agePanel.add(ageText, null);
    valuesPanel1.add(densityPanel, null);
    densityPanel.add(densityLabel, null);
    densityPanel.add(densityCB, null);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(createPB, null);
    buttonPanel.add(CancelPB, null);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(htGrpPanel, BorderLayout.NORTH);
    htGrpPanel.add(htGrpPanel1, null);
    htGrpPanel1.add(htGrpScroll, null);
    htGrpScroll.getViewport().add(htGrpList, null);
    htGrpPanel1.add(htGrpChangePB, null);
    northPanel.add(valuesPanel, BorderLayout.CENTER);
  }

  private void initialize() {
    RegionalZone     zone = Simpplle.getCurrentZone();

    // Fill the Habitat Type Group List
    htGrpList.setListData(htGrps);

    // Set the Species
    speciesValue.setText(species.toString());

    // Size Class
    int i;
    Vector sizeClass = zone.getAllSizeClass();
    for(i=0; i<sizeClass.size(); i++) {
      sizeClassCB.addItem((SizeClass)sizeClass.elementAt(i));
    }

    // Age
    age = 1;
    ageText.setText(Integer.toString(age));

    // Density
    Vector density = zone.getAllDensity();
    for(i=0; i<density.size(); i++) {
      densityCB.addItem((Density)density.elementAt(i));
    }
    setSize(getPreferredSize());
    update(getGraphics());
  }
/**
 * Method to change age of state.  Parses the age from the age text field.  As long as it is greater than 1, sets the age.  
 */
  private void ageChanged() {
    if (focusLost) { return; }
    try {
      age = Integer.parseInt(ageText.getText());
      if (age < 1) {
        throw new NumberFormatException();
      }
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      String msg = "Invalid Age.\n" +
                   "Please enter a number greater than 0";
      JOptionPane.showMessageDialog(this,msg,"Invalid value",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          ageText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
  }
/**
 * An age is typed in the Age text field.  (must be greater than 1)
 * @param e
 */
  void ageText_actionPerformed(ActionEvent e) {
    ageChanged();
  }
/**
 * Focus is lost in Age text field.  (must be greater than 1)
 * @param e
 */
  void ageText_focusLost(FocusEvent e) {
    ageChanged();
  }
/**
 * If user presses "Create" button, this will create a new vegetative state with (species, size class, age, and density), if it doesn't already exist.  
 * @param e
 */
  void createPB_actionPerformed(ActionEvent e) {
    SizeClass sizeClass = (SizeClass)sizeClassCB.getSelectedItem();
    Density   density   = (Density)densityCB.getSelectedItem();

    RegionalZone     zone = Simpplle.getCurrentZone();
    HabitatTypeGroup htGrpInst;
    VegetativeType   vegType;
    for(int i=0; i<htGrps.length; i++) {
      htGrpInst = HabitatTypeGroup.findInstance(htGrps[i]);
      vegType = htGrpInst.getVegetativeType(species,sizeClass,age,density);

      // Only create if it doesn't already exist.
      if (vegType == null) {
        vegType = new VegetativeType(htGrpInst,species,sizeClass,age,density);
        htGrpInst.addVegetativeType(vegType);

       // Update the units and check for invalid ones.
       Area area = Simpplle.getCurrentArea();
       if (area != null) {
        area.updatePathwayData();
        if (area.existAnyInvalidVegUnits() == false) {
          JSimpplle.getSimpplleMain().markAreaValid();
        }
       }
      }
    }
    setVisible(false);
    dispose();
  }
/**
 * If user presses "Change" button this will load a List Selection Dialog with all the habitat type groups.  The user can then select a habitat type group.  
 * @param e
 */
  void htGrpChangePB_actionPerformed(ActionEvent e) {
    RegionalZone        zone = Simpplle.getCurrentZone();
    Frame               theFrame = JSimpplle.getSimpplleMain();
    ListSelectionDialog dlg;
    Object[]            result;

    dlg = new ListSelectionDialog(theFrame,"Select Ecological Grouping",true,
                                  HabitatTypeGroup.getLoadedGroupNames(),true);

    dlg.setLocation(getLocation());
    dlg.setSelectedItems(htGrps);
    dlg.setVisible(true);
    result = (Object[])dlg.getSelections();
    if (result != null) {
      htGrps = new String[result.length];
      for (int i=0; i<result.length; i++) { htGrps[i] = (String)result[i]; }
      htGrpList.setListData(htGrps);
    }
  }
/**
 * If user pushes 'Cancel' disposes the Pathway New State dialog.  
 * @param e
 */
  void CancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }
}








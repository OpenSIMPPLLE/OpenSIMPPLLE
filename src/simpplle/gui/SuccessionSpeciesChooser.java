package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.VerticalFlowLayout;
import simpplle.comcode.Species;
import java.util.ArrayList;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the Succession Species Chooser dialog, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */

public class SuccessionSpeciesChooser extends JDialog {
  private DefaultListModel sourceListModel;
  private DefaultListModel targetListModel;
  private Hashtable        targetLookupHt;
  private boolean          inInit;

  private String protoCellValue = "XERIC_FS_SHRUBS          ";

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel listsPanel = new JPanel();
  JPanel SourceTargetPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel targetPanel = new JPanel();
  JPanel sourcePanel = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JScrollPane sourceScrollPane = new JScrollPane();
  DragSourceList sourceList = new DragSourceList();
  JScrollPane targetScrollPane = new JScrollPane();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  DragDropList targetList = new DragDropList();
  BorderLayout borderLayout2 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel infoPanel = new JPanel();
  JLabel infoLabel2 = new JLabel();
  JLabel infoLabel1 = new JLabel();
  VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
  JLabel infoLabel3 = new JLabel();

  public SuccessionSpeciesChooser(JDialog dialog, String title, boolean modal,
                                  Vector validSpecies, ArrayList<Species> chosenSpecies)
  {
    super(dialog, title, modal);
    try {
      jbInit();
      initialize(validSpecies, chosenSpecies);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public SuccessionSpeciesChooser() {
    this(null, "", false,null,null);
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
    sourcePanel.setLayout(verticalFlowLayout1);
    targetPanel.setLayout(verticalFlowLayout2);
    verticalFlowLayout1.setVerticalFill(true);
    verticalFlowLayout2.setVerticalFill(true);
    sourcePanel.setBorder(titledBorder1);
    targetPanel.setBorder(titledBorder2);
    targetList.setToolTipText("");
    targetList.setPrototypeCellValue(protoCellValue);
    targetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sourceList.setPrototypeCellValue(protoCellValue);
    this.setModal(true);
    titledBorder1.setTitle("Available Species");
    titledBorder2.setTitle("Chosen Species");
    infoPanel.setLayout(verticalFlowLayout3);
    infoLabel2.setFont(new java.awt.Font("Monospaced", 1, 12));
    infoLabel2.setText("Use mouse to order species as desired.");
    infoLabel1.setFont(new java.awt.Font("Monospaced", 1, 12));
    infoLabel1.setText("Drag species to chosen list on right.");
    infoLabel3.setFont(new java.awt.Font("Monospaced", 1, 12));
    infoLabel3.setText("Most preferred species is first in list.");
    infoPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    SourceTargetPanel.add(sourcePanel, BorderLayout.WEST);
    getContentPane().add(mainPanel);
    mainPanel.add(listsPanel,  BorderLayout.CENTER);
    listsPanel.add(SourceTargetPanel, null);
    SourceTargetPanel.add(targetPanel,  BorderLayout.EAST);
    sourcePanel.add(sourceScrollPane, null);
    sourceScrollPane.getViewport().add(sourceList, null);
    targetPanel.add(targetScrollPane, null);
    mainPanel.add(infoPanel,  BorderLayout.SOUTH);
    infoPanel.add(infoLabel1, null);
    infoPanel.add(infoLabel2, null);
    infoPanel.add(infoLabel3, null);
    targetScrollPane.getViewport().add(targetList, null);
  }
  /**
   * Initialize the Succession Species Chooser with default list models for source and target lists.
   * Then create a source list model from all the source items and a target list model from all the target items
   *  (both come from vector of valid species objects).  
   * The sampleItems are just an simpplle type object from the arraylist.  Will be used later to in conditional of update vector method.  
   * Note: the toString method of simpplle types returns all upper case process names ex "DEBRIS_EVENT"
   * SourceList and targetList are drag source lists
   * @see simpplle.gui.dragsourcelist
   * @param sourceItems vector of simpplle type objects to be set
   * @param targetItems
   */
  private void initialize(Vector validSpecies, ArrayList<Species> chosenSpecies) {
    inInit = true;

    sourceListModel = new DefaultListModel();
    targetListModel = new DefaultListModel();

    sourceList.setModel(sourceListModel);
    targetList.setModel(targetListModel);

    // Must used Strings, because will not work for drag-n-drop
    for (int i=0; i<validSpecies.size(); i++) {
      sourceListModel.addElement(((Species)validSpecies.get(i)).toString());
    }
    // Must used Strings, because will not work for drag-n-drop
    for (int i=0; i<chosenSpecies.size(); i++) {
      targetListModel.addElement((chosenSpecies.get(i)).toString());
    }
    inInit = false;
  }

  /**
   * This method will clear the elements from the vector passed to this dialog
   * and replace them with the new list created by the user.
   * This will update the original vector passed from outside thus there is
   * no need for this Dialog to return anything.
   */
  public void finishUp(ArrayList<Species> v) {
    v.clear();
    for (int i=0; i<targetListModel.getSize(); i++) {
      v.add(Species.get((String)targetListModel.elementAt(i)));
    }
  }
}




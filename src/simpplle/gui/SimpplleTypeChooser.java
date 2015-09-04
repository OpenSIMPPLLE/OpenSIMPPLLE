package simpplle.gui;

import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.SimpplleType;
import simpplle.comcode.SizeClass;
import simpplle.comcode.Density;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates a Simpplle Type Chooser, which allows users to make selections of the simpplle types.  
 * Choices of these are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */

public class SimpplleTypeChooser extends JDialog {
  private static final String sizeClassPrototype   = "CLOSED-TALL-SHRUB    ";
  private DefaultListModel sourceListModel;
  private DefaultListModel targetListModel;
  private boolean          inInit=false;
  private SimpplleType     sampleItem;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel listsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel targetListPanel = new JPanel();
  JPanel sourceListPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  DragSourceList sourceList = new DragSourceList(true);
  JScrollPane sourceListScroll = new JScrollPane();
  DragDropList targetList = new DragDropList(true);
  JScrollPane targetListScroll = new JScrollPane();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPopupMenu menuPopup = new JPopupMenu();
  JMenuItem menuPopupDelete = new JMenuItem();
/**
 * Constructer for Simpplle Type Chooser.  This sets the owner, dialog title, modality and two vectors of simpplle types which will form 
 * default list models for source and target items.   
 * @param dialog owner of dialog
 * @param title name of dialog
 * @param modal specifies whether dialog blocks user input to other top-level windows when shown
 * @param sourceItems simpplle type objects
 * @param targetItems simpplle type objects
 */
  public SimpplleTypeChooser(JDialog dialog, String title, boolean modal, Vector sourceItems, Vector targetItems) {
    super(dialog, title, modal);
    try {
      jbInit();
      initialize(sourceItems, targetItems);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public SimpplleTypeChooser() {
    this(null, "", false,null,null);
  }
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Available Size Classes");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Chosen Size Classes");
    mainPanel.setLayout(borderLayout1);
    listsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    sourceListPanel.setLayout(borderLayout2);
    targetListPanel.setLayout(borderLayout3);
    sourceList.setFont(new java.awt.Font("Monospaced", 0, 12));
    sourceList.setFixedCellWidth(150);
    sourceList.setPrototypeCellValue(sizeClassPrototype);
    sourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    targetList.setFont(new java.awt.Font("Monospaced", 0, 12));
    targetList.setFixedCellWidth(150);
    targetList.setPrototypeCellValue(sizeClassPrototype);
    targetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    targetList.addMouseListener(new SimpplleTypeChooser_targetList_mouseAdapter(this));
    targetList.addKeyListener(new SimpplleTypeChooser_targetList_keyAdapter(this));
    sourceListPanel.setBorder(titledBorder1);
    targetListPanel.setBorder(titledBorder2);
    titledBorder1.setTitle("Available");
    titledBorder2.setTitle("Chosen");
    menuPopupDelete.setText("Delete Item");
    menuPopupDelete.addActionListener(new SimpplleTypeChooser_menuPopupDelete_actionAdapter(this));
    listsPanel.add(sourceListPanel, null);
    listsPanel.add(targetListPanel, null);
    getContentPane().add(mainPanel);
    mainPanel.add(listsPanel,  BorderLayout.CENTER);
    sourceListPanel.add(sourceListScroll, BorderLayout.CENTER);
    sourceListScroll.add(sourceList, null);
    targetListPanel.add(targetListScroll, BorderLayout.CENTER);
    targetListScroll.add(targetList, null);
    sourceListScroll.getViewport().add(sourceList, null);
    targetListScroll.getViewport().add(targetList, null);
    menuPopup.add(menuPopupDelete);
  }
/**
 * Initialize the SimpplleTypeChooser with default list models for source and target lists.
 * Then create a source list model from all the source items and a target list model from all the target items
 *  (both come from vector of simpplle objects).  
 *  The sampleItems are just an simpplle type object from the arraylist.  Will be used later to in conditional of update vector method.  
 * Note: the toString method of simpplle types returns all upper case process names ex "DEBRIS_EVENT"
 * SourceList and targetList are drag source lists
 * @see simpplle.gui.dragsourcelist
 * @param sourceItems vector of simpplle type objects to be set
 * @param targetItems
 */
  private void initialize(Vector sourceItems, Vector targetItems) {
    inInit = true;

    sourceListModel = new DefaultListModel();
    targetListModel = new DefaultListModel();

    sourceList.setModel(sourceListModel);
    targetList.setModel(targetListModel);


    if (sourceItems.size() > 0) {
      sampleItem = (SimpplleType)sourceItems.elementAt(0);
    }
    else {
      sampleItem = (SimpplleType)targetItems.elementAt(0);
    }

    for (int i=0; i<sourceItems.size(); i++) {
      sourceListModel.addElement(sourceItems.elementAt(i).toString());
    }

    for (int j=0; j<targetItems.size(); j++) {
      targetListModel.addElement(((SimpplleType)targetItems.elementAt(j)).toString());
    }

    update(getGraphics());
    inInit = false;
  }

//  public Vector getSizeClasses() {
//    if (targetListModel.getSize() == 0) { return null; }
//
//    int    size = targetListModel.getSize();
//    Vector v = new Vector(size);
//
//    for (int i=0; i<size; i++) {
//      v.addElement(SizeClass.get((String)targetListModel.elementAt(i)));
//    }
//    return v;
//  }
/**
 * Empties the parameter vector, if the simpplle type is a Size Class or Density object, adds all the target list model elements to the vector 
 * @param v vector passed. 
 */
  public void updateChosenVector(Vector v) {
    if (targetListModel.getSize() == 0) {
      return;
    }
    v.removeAllElements();

    int i;
    if (sampleItem instanceof SizeClass) {
      for (i=0; i<targetListModel.getSize(); i++) {
        v.addElement(SizeClass.get((String)targetListModel.elementAt(i)));
      }
    }
    else if (sampleItem instanceof Density) {
      for (i=0; i<targetListModel.getSize(); i++) {
        v.addElement(Density.get((String)targetListModel.elementAt(i)));
      }
    }
    return;
  }
  /**
   * This method will clear the elements from the vector passed to this dialog
   * and replace them with the new list created by the user.
   * This will update the original vector passed from outside thus there is
   * no need for this Dialog to return anything.
   */
//  public void finishUp(Vector v) {
//    // Don't allow deletion via removing all elements, user must use appropriate
//    // menu actions for this.
//    if (targetListModel.getSize() == 0) {
//      return;
//    }
//    v.removeAllElements();
//    for (int i=0; i<targetListModel.getSize(); i++) {
//      v.addElement(SizeClass.get((String)targetListModel.elementAt(i)));
//    }
//  }
/**
 * Gets current list model and adds user selected target element to it, then deletes the element in target list.   
 */
  private void deleteTargetItem() {
    sourceList.addElement(targetList.getSelectedValue());
    targetList.removeElementAt(targetList.getSelectedIndex());
  }
  /**
   * Handles the key pressed event.  Gets the selected item from target list.  If user pressed 'delete', deletes the item in the list.    
   * @param e 'delete'
   */
  void targetList_keyPressed(KeyEvent e) {
    int index = targetList.getSelectedIndex();
    if (e.getKeyCode() == KeyEvent.VK_DELETE && index != -1) {
      deleteTargetItem();
    }
  }
  /**
   * As long as the selected index is not -1 which indicates there is no selection, this will delete the user choosen item.  
   * @param e
   */
  void menuPopupDelete_actionPerformed(ActionEvent e) {
    if (targetList.getSelectedIndex() != -1) {
      deleteTargetItem();
    }
  }
  /**
   * Checks if right click was made on mouse.  
   * @param e mouse event occured
   * @return true if mouse event was a right click.  
   */
  private boolean isRightClick(MouseEvent e) {
    return (e.getModifiers () & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK;
  }
/**
 * Handles mouse pressed event in the target drag drop list.  
 * Left Click: It will return the cell index closest to where the user clicked the mouse.  
 * -1 indicates the model was empty  (the user clicked in the wrong spot).   
 * Right Click: shows a popup menu at the spot where right click was made.  
 * 
 * @param e mouse click
 */
  void targetList_mousePressed(MouseEvent e) {
    int index = targetList.locationToIndex(e.getPoint());
    if (index == -1) {
      return;
    }
    else if (isRightClick(e)) {
      targetList.setSelectedIndex(index);
      menuPopup.show(e.getComponent(), e.getX(), e.getY());
    }
  }

}
/**
 * Creates a key adapter to handle the key pressed event.  This saves on work in implementing key event listeners.  
 * It passes to method within SimpplleTypeChooser class.  
 *
 */
class SimpplleTypeChooser_targetList_keyAdapter extends java.awt.event.KeyAdapter {
  SimpplleTypeChooser adaptee;

  SimpplleTypeChooser_targetList_keyAdapter(SimpplleTypeChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Calls to targetList_keyPressed(e) within SimpplleTypeChooser class when key pressed.  
   */
  public void keyPressed(KeyEvent e) {
    adaptee.targetList_keyPressed(e);
  }
}
/**
 * Creates a window adapter to handle the popup menu delete action.  This saves on work in implementing Action listeners.  
 * It passes to method within SimpplleTypeChooser class.  
 *
 */
class SimpplleTypeChooser_menuPopupDelete_actionAdapter implements java.awt.event.ActionListener {
  SimpplleTypeChooser adaptee;

  SimpplleTypeChooser_menuPopupDelete_actionAdapter(SimpplleTypeChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Handles the delete button action in popup menu by passing to menuPopupDelete_actionPerformed(e).  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuPopupDelete_actionPerformed(e);
  }
}
/**
 * Creates a mouse adapter to handle the mouse pressed event.  This saves on work in implementing the mouse listener.  
 * It passes to method within SimpplleTypeChooser class.  
 *
 */
class SimpplleTypeChooser_targetList_mouseAdapter extends java.awt.event.MouseAdapter {
  SimpplleTypeChooser adaptee;

  SimpplleTypeChooser_targetList_mouseAdapter(SimpplleTypeChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Handles the mouse pressed event by passing to targetList_mousePressed(), where both right and left mouse clicks are dealt with.  
   */
  public void mousePressed(MouseEvent e) {
    adaptee.targetList_mousePressed(e);
  }
}




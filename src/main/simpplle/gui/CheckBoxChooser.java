package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This class has methods for creating a Check Box Chooser. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */


public class CheckBoxChooser extends JDialog {
  private ArrayList items;
  private ArrayList chosenItems;
  private JCheckBox[] boxes;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel cbPanel = new JPanel();
  GridLayout cbGridLayout = new GridLayout();

  /**
   * Primary constructor for Check Box Chooser.  
   * Provides methods to bring in an arraylist of items to be made into check boxes and chosen items to keep track of those choosen.  
   * @param owner the dialog that owns the check box chooser
   * @param title the title of this dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   * @param items arraylist of items to chooser
   * @param chosenItems array list of choosen items.  
   */
  public CheckBoxChooser(JDialog owner, String title, boolean modal,
                              ArrayList items, ArrayList chosenItems) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.items = new ArrayList(items);
      this.chosenItems = new ArrayList(chosenItems);
      jbInit();
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Overloaded CheckBoxChooser.  Sets a new dialog as the owner, name to origin process, modeless, null for items, and null for selected items.  
 */
  public CheckBoxChooser() {
    this(new JDialog(), "Origin Process", false,null,null);
  }
/**
 * sets the layout, for main panel, and check box panel - to three rows, adds a listener and makes the main panel the content pane
 * @throws Exception
 */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    cbPanel.setLayout(cbGridLayout);
    cbGridLayout.setRows(3);
    this.addWindowListener(new ThreeItemChooser_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    mainPanel.add(cbPanel, java.awt.BorderLayout.NORTH);
  }
/**
 * initializes the check box chooser, makes the check boxes makes the text of each the toString method.  
 */
  private void initialize() {
    boxes = new JCheckBox[items.size()];
    cbGridLayout.setRows(boxes.length);
    for (int i=0; i<boxes.length; i++) {
      boxes[i] = new JCheckBox(items.get(i).toString(),chosenItems.contains(items.get(i)));
      cbPanel.add(boxes[i]);
    }
  }
/**
 * If window closing event occurs goes through all the check boxes and adds any that are selected to the choosenItems arraylist.  
 * Then sets the visibility to false and disposes the dialog.  
 * @param e
 */
  public void this_windowClosing(WindowEvent e) {
    chosenItems.clear();
    for (int i=0; i<boxes.length; i++) {
      if (boxes[i].isSelected()) { chosenItems.add(items.get(i)); };
    }
    setVisible(false);
    dispose();
  }
/**
 * Gets the arraylist of check boxes that have been selected by user.  
 * @return
 */
  public ArrayList getChosenItems() {
    return chosenItems;
  }
}
/**
 * This is an adapter class to handle the check box choosing.  It is a convenience to make making handling window events easier.  
 * 
 *
 */
class ThreeItemChooser_this_windowAdapter extends WindowAdapter {
  private CheckBoxChooser adaptee;
  ThreeItemChooser_this_windowAdapter(CheckBoxChooser adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Handles a window closing event by passing to this_windowClosing(e) in CheckBoxChooser class.  There it goes through all the check boxes and adds any that are selected to the choosenItems arraylist.  
 * Then sets the visibility to false and disposes the dialog. 
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}

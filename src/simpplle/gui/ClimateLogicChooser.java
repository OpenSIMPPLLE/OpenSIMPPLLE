/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;

/** 
 * This class defines the Climate Logic Chooser, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ClimateLogicChooser extends JDialog {
  private Object[] items;
  private ArrayList chosenItems;
  private int       countTimeStep, numTimeStep;
  private JCheckBox[] boxes;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel cbPanel = new JPanel();
  GridLayout cbGridLayout = new GridLayout();
  private JPanel tsPanel = new JPanel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JLabel trailLabel = new JLabel();
  private JNumberTextField numTimeText = new JNumberTextField();
  private JLabel middleLabel = new JLabel();
  private JNumberTextField countTimeText = new JNumberTextField();
/**
 * Constructor for Climate Logic Chooser.  Inherits from JDialog superclass and sets items, choosen items, time step count and time step number
 * then calls jbInit
 * @param owner
 * @param title
 * @param modal
 * @param items
 * @param chosenItems
 * @param timeStepCount
 * @param numTimeStep
 */
  public ClimateLogicChooser(JDialog owner, String title, boolean modal,
                              Object[] items, ArrayList chosenItems,
                              int timeStepCount, int numTimeStep) {
    super(owner, title, modal);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.items = items;
      this.chosenItems = new ArrayList(chosenItems);
      this.countTimeStep = timeStepCount;
      this.numTimeStep   = numTimeStep;
      jbInit();
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Overloaded constructor for Climate Logic Chooser.  Set up to initiate an origin process, this creates passes 
 * to default constructor a new JDialog, title = origin process, false for modality, null items, null choosen items, time step count to 1 
 * and time step num to 1 meaning initial.  
 */
  public ClimateLogicChooser() {
    this(new JDialog(), "Origin Process", false,null,null,1,1);
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    cbPanel.setLayout(cbGridLayout);
    cbGridLayout.setRows(3);
    this.addWindowListener(new ClimateLogicChooser_this_windowAdapter(this));
    tsPanel.setLayout(flowLayout1);
    trailLabel.setText("Time Steps");
    trailLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));
    trailLabel.setForeground(Color.blue);
    numTimeText.setText("5");
    numTimeText.setColumns(5);
    numTimeText.setHorizontalAlignment(SwingConstants.TRAILING);
    middleLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 14));
    middleLabel.setForeground(Color.blue);
    middleLabel.setText("of");
    countTimeText.setText("3");
    countTimeText.setColumns(5);
    countTimeText.setHorizontalAlignment(SwingConstants.TRAILING);
    getContentPane().add(mainPanel);
    mainPanel.add(cbPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(tsPanel, java.awt.BorderLayout.NORTH);
    tsPanel.add(countTimeText);
    tsPanel.add(middleLabel);
    tsPanel.add(numTimeText);
    tsPanel.add(trailLabel);
  }
/**
 * Initializes Climate logic chooser dialog, by setting check boxes depending on items entered in constructor, sets layout, 
 * and adds to panel.  Sets the time text field to the sum of time steps, and number time text to the num time step.  
 */
  private void initialize() {
    boxes = new JCheckBox[items.length];
    cbGridLayout.setRows(boxes.length);
    for (int i=0; i<boxes.length; i++) {
      boxes[i] = new JCheckBox(items[i].toString(),chosenItems.contains(items[i]));
      cbPanel.add(boxes[i]);
    }

    countTimeText.setValue(countTimeStep);
    numTimeText.setValue(numTimeStep);
  }
/**
 * If window closing X event occurs, clears out the chosen items, adds selected items to chosen item  array.  
 * Gets the time step count from text field, gets the number of time step from text field, sets the dialog to not visible and disposes.
 * @param e
 */
  public void this_windowClosing(WindowEvent e) {
    chosenItems.clear();
    for (int i=0; i<boxes.length; i++) {
      if (boxes[i].isSelected()) { chosenItems.add(items[i]); };
    }
    countTimeStep = countTimeText.getValue();
    numTimeStep   = numTimeText.getValue();
    setVisible(false);
    dispose();
  }
/**
 * Gets the times previously choosen.
 * @return
 */
  public ArrayList getChosenItems() {
    return chosenItems;
  }
/**
 * Gets the number of time step.  
 * @return the time step number
 */
  public int getNumTimeStep() {
    return numTimeStep;
  }
/**
 * Gets the count (sum) of all time steps.
 * @return the total count of time steps
 */
  public int getCountTimeStep() {
    return countTimeStep;
  }

}
/**
 * 
 * Uses a window adaptor to create a handler for window closing event.    
 *
 */
class ClimateLogicChooser_this_windowAdapter extends WindowAdapter {
  private ClimateLogicChooser adaptee;
  ClimateLogicChooser_this_windowAdapter(ClimateLogicChooser adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Closes the window adaptor.  
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}

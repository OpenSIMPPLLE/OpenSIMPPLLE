
package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.Simpplle;
import simpplle.comcode.RegionalZone;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>List selection dialogs are used to allow users to select from a series of values.  An example would be selecting a habitat type group from the 
 * array of habitat type groups available for a specific region.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */

public class ListSelectionDialog extends JDialog {
  private Object   selection;
  private Object[] selections;
  private String protoCellValue = "STAND-REPLACING-FIRE                      ";
  private boolean multipleSelect = false;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel titleLabel = new JLabel();
  JList theList = new JList();
  JPanel buttonPanel = new JPanel();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JScrollPane theListScroll = new JScrollPane();

  public ListSelectionDialog(Frame frame, String title, boolean modal,
                             Object[] selectionValues) {
    this(frame,title,modal,selectionValues,false);
  }
/**
 * List selection dialogs are used to allow users to select from a series of values.  An example would be selecting a habitat type group from the 
 * array of habitat type groups available for a specific region.  
 * @param frame
 * @param title
 * @param modal
 * @param selectionValues
 * @param multipleSelect
 */
  public ListSelectionDialog(Frame frame, String title, boolean modal,
                             Object[] selectionValues, boolean multipleSelect) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.multipleSelect = multipleSelect;
    initialize(title, selectionValues);
  }

  public ListSelectionDialog() {
    this(null, "", false,null);
  }
/**
 * Initializes the dialog with panels, components, listeners, borders, layouts and and panels.  
 * @throws Exception
 */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    titleLabel.setFont(new java.awt.Font("Dialog", 1, 16));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setText("Title");
    buttonPanel.setLayout(flowLayout1);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    okPB.setMaximumSize(new Dimension(73, 27));
    okPB.setMinimumSize(new Dimension(73, 27));
    okPB.setPreferredSize(new Dimension(73, 27));
    okPB.setText("Ok");
    okPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    theList.setBackground(Color.darkGray);
    theList.setForeground(Color.white);
    theList.setPrototypeCellValue(protoCellValue);
    theList.setSelectionBackground(Color.white);
    theList.setSelectionForeground(Color.orange);
    theList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    theList.setVisibleRowCount(10);
    this.setResizable(false);
    getContentPane().add(mainPanel);
    mainPanel.add(titleLabel, BorderLayout.NORTH);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(okPB, null);
    buttonPanel.add(cancelPB, null);
    mainPanel.add(theListScroll, BorderLayout.CENTER);
    theListScroll.getViewport().add(theList, null);
  }
/**
 * Initializes the dialog with current regional zone, 
 * @param title
 * @param selectionValues
 */
  private void initialize(String title, Object[] selectionValues) {
    RegionalZone zone = Simpplle.getCurrentZone();

    if (multipleSelect) {
      theList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    titleLabel.setText(title);
    selection = null;
    theList.setListData(selectionValues);
  }
/**
 * Gets the object that was selected by user.  
 * @return the user selected object.  
 */
  public Object getSelection() {
    return selection;
  }
/**
 * Gets the array of objects selected.  
 * @return array of user selected objects.  
 */
  public Object[] getSelections() {
    return selections;
  }
/**
 * Loops through the input objects (example: process objects) and sets the corresponding list items at that index to true (meaning selected).  
 * Then creates an array of the indexes that were selected and sets those list indices as selected.
 * @param values
 */
  public void setSelectedItems(Object[] values) {
    int[] indicies = new int[values.length];
    for(int i=0; i<values.length; i++) {
      theList.setSelectedValue(values[i],true);
      indicies[i] = theList.getSelectedIndex();
    }
    theList.setSelectedIndices(indicies);
  }
/**
 * Handles the 'OK' button pressed event.  If not selection has been made, gives a warning message.  
 * If multiple selections have been made by user an object array is created and the selections are passed to it.  
 * Otherwise it will get the one selected value  
 * @param e 'OK'
 */
  void okPB_actionPerformed(ActionEvent e) {
    if (theList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,"Please select a Item",
                                    "No Item Selected.",
                                    JOptionPane.WARNING_MESSAGE);
      return;
    }

    if (multipleSelect) {
      selections = (Object[]) theList.getSelectedValues();
//      Object[] tmp = (Object[]) theList.getSelectedValues();
//      selections = new String[tmp.length];
//      for(int i=0; i<tmp.length; i++) {
//        selections[i] = (String) tmp[i];
//      }
    }
    else {
      selection = theList.getSelectedValue();
    }
    setVisible(false);
    dispose();
  }
/**
 * Handles the 'Cancel' button pressed event.  Sets the dialog to not visible and disposes of it.  
 * @param e 'Cancel' 
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

}

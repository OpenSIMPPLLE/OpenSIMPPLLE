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

/** 
 * This class defines Choose Simpplle Types Dialog, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */

public class ChooseSimpplleTypes extends JDialog {
  private String listCellValue   = "--> RIPARIAN_GRASSES   ";

  private ListItem[] originalItems;
  private ListItem[] items;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JPanel buttonInnerPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel listPBPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JButton allPB = new JButton();
  JButton nonePB = new JButton();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
  JScrollPane listScroll = new JScrollPane();
  JList list = new JList();
  FlowLayout flowLayout1 = new FlowLayout();
/**
 * Primary constructor for Choose Simpplle Types GUI.  Invokes jbInit method and sets the owner frame, string title, and modality
 * @param frame
 * @param title title of Choose Simpplle Types dialog
 * @param modal specifies whether dialog blocks user input to other top-level windows when shown
 * @param originalItems
 */
  public ChooseSimpplleTypes(Frame frame, String title, boolean modal, ListItem[] originalItems) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.originalItems = originalItems;
    initialize();
  }
/**
 * Overloaded constructor which references the primary and sets to null owner, title, modality (false = modeless) and items []
 */
  public ChooseSimpplleTypes() {
    this(null, "", false,null);
  }
  /**
   * Sets the panels, layouts, 
   * @throws Exception
   */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    this.setModal(true);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    buttonPanel.setLayout(borderLayout2);
    buttonInnerPanel.setLayout(flowLayout1);
    northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
    listPBPanel.setLayout(flowLayout3);
    allPB.setMargin(new Insets(2, 2, 2, 2));
    allPB.setText("All");
    allPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        allPB_actionPerformed(e);
      }
    });
    nonePB.setMargin(new Insets(2, 2, 2, 2));
    nonePB.setText("None");
    nonePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nonePB_actionPerformed(e);
      }
    });
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    okPB.setText("Ok");
    okPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    list.setPrototypeCellValue(listCellValue);
    list.setVisibleRowCount(20);
    list.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        list_mousePressed(e);
      }
    });
//    verticalFlowLayout1.setHgap(0);
//    verticalFlowLayout1.setVgap(0);
    northPanel.setBorder(BorderFactory.createEtchedBorder());
    northPanel.add(listPBPanel, null);
    northPanel.add(listScroll, null);
    listScroll.add(list, null);
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel,  BorderLayout.NORTH);
    mainPanel.add(buttonPanel,  BorderLayout.CENTER);
    buttonPanel.add(buttonInnerPanel,  BorderLayout.SOUTH);
    listPBPanel.add(allPB, null);
    listPBPanel.add(nonePB, null);
    buttonInnerPanel.add(okPB, null);
    buttonInnerPanel.add(cancelPB, null);
  }
/**
 *Uses the list of OpenSimpplle types created in the ListItem class to create a new array of simpplle types and sets them to selected 
 */
  private void initialize() {
    // ** This code is necessary due to an apparent bug
    // ** in JBuilder's Designer which does not do this.

    listScroll.getViewport().add(list);
    // ** end of bug fix code

    items = new ListItem[originalItems.length];
    for (int i=0; i<items.length; i++) {
      items[i] = new ListItem(originalItems[i].item);
      items[i].setSelected(originalItems[i].isSelected());
    }

    list.setListData(items);
    setSize(getPreferredSize());
    updateDialog();
  }
/**
 * Creates a graphic context for the choose OpenSimpplle types dialog, in order to update the GUI
 */
  private void updateDialog() {
    update(getGraphics());
  }
/**
 * Method to handle the action event created when the "All" types button is selected.  
 * @param e
 */
  void allPB_actionPerformed(ActionEvent e) {
    ListItem.setListSelection(items,true);
    updateDialog();
  }
/**
 * handles the action event created when the "None" types button is selected
 * @param e
 */
  void nonePB_actionPerformed(ActionEvent e) {
    ListItem.setListSelection(items,false);
    updateDialog();
  }
/**
 * method to handle the mouse event when a simpplle type is choosen
 * @param e
 */
  void list_mousePressed(MouseEvent e) {
    ListItem item = (ListItem)list.getSelectedValue();
    if (item != null) {
      item.changeSelected();
    }
    updateDialog();
  }
/**
 * Sets the original items to the selected items when 'OK' button is pushed
 * @param e "OK"
 */
  void okPB_actionPerformed(ActionEvent e) {
    for (int i=0; i<originalItems.length; i++) {
      originalItems[i].setSelected(items[i].isSelected());
    }
    items = null;
    setVisible(false);
    dispose();
  }
/**
 * Method to quit simple types chooser.  Sets items to null and dialog to not visible, then disposes.
 */
  private void quit() {
    items = null;
    setVisible(false);
    dispose();
  }
/**
 * Quits ChooseSimpplleTypes dialog if quit button pushed.
 * @param e "quit"
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    quit();
  }

  /**
 * Quits ChooseSimpplleTypes dialog if window close X is pushed.
 * @param e window closing X pushed
   */
  void this_windowClosing(WindowEvent e) {
    quit();
  }
}
/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.JSimpplle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** 
 * This class creates the AskNumber dialog, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class AskNumber extends JDialog {
  int value;

  JPanel mainPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JPanel valuesPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JTextField numText = new JTextField();
  JLabel label = new JLabel();
  FlowLayout flowLayout3 = new FlowLayout();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
  BorderLayout borderLayout1 = new BorderLayout();
/**
 * Constructor for AskNumber method. Calls the jbInit(), sets the label, integer value, sets the size and repaints the graphics.    
 * @param frame
 * @param title
 * @param modal
 * @param msg
 * @param value
 */
  public AskNumber(Frame frame, String title, boolean modal, String msg, int value) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    label.setText(msg);
    this.value = value;
    numText.setText(Integer.toString(this.value));
    setSize(getPreferredSize());
    update(getGraphics());
  }
  /**
   * Overloaded AskNumber constructor.
   * @param frame
   * @param title the string literal name of this frame
   * @param modal
   * @param msg
   */
  public AskNumber(Frame frame, String title, boolean modal, String msg) {
    this(frame,title,modal,msg,1);
  }
  /**
   * Overloaded AskNumber constructor.  Sets frame as null, title to empty string, modality false, and message to null.  
   */
  public AskNumber() {
    this(null, "", false,null);
  }
  /**
   * Sets the layout, panels, action listeners, and text for Ask Number
   * @throws Exception
   */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    valuesPanel.setLayout(flowLayout2);
    numText.setText("1");
    numText.setColumns(8);
    numText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        numText_keyTyped(e);
      }
    });
    numText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        numText_actionPerformed(e);
      }
    });
    buttonPanel.setLayout(flowLayout3);
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
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    mainPanel.add(valuesPanel,  BorderLayout.NORTH);
    valuesPanel.add(label, null);
    valuesPanel.add(numText, null);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);
    getContentPane().add(mainPanel);
    buttonPanel.add(okPB, null);
    buttonPanel.add(cancelPB, null);
  }
/**
 * Creates a new instance of askNumber dialog, sets visible and returns the dialog value.  
 * @param title
 * @param msg
 * @param value
 * @return
 */
  public static int getInput(String title, String msg, int value) {
    return getInput(title, msg, value, null);
  }
  public static int getInput(String title, String msg, int value, Point location){
    AskNumber dlg = new AskNumber(JSimpplle.getSimpplleMain(),title,true,msg,value);
    if (location != null){
      dlg.setLocation(location);
    }
    dlg.setVisible(true);
    return dlg.value;
  }
  /**
   * Overloaded getInput takes in only the title and message, then calls the getInput method with 1 as the value.
   * @param title
   * @param msg
   * @return
   */
  public static int getInput(String title, String msg) {
    return AskNumber.getInput(title,msg,1);
  }
  /**
   * Parses the number in number (the number being asked) text field casts to Integer object.  
   * @throws NumberFormatException.  Will beep if no value is entered and set value as -1 ... a flag 
   * else sets the AskNumber dialog to not visible and disposes.
   */
  private void finish() {
    try {
      String str = numText.getText();
      if (str == null || str.length() == 0) { throw new NumberFormatException(); }
      value = Integer.parseInt(numText.getText());
    }
    catch (NumberFormatException err) {
      java.awt.Toolkit.getDefaultToolkit().beep();
      value = -1;
      return;
    }
    setVisible(false);
    dispose();
  }
/**
 * If an event occurs in ask number text field sends to finish()
 * @param e
 */
  void numText_actionPerformed(ActionEvent e) {
    finish();
  }
/*
 * if user pushes the ok button.  sends to finish()
 */
  void okPB_actionPerformed(ActionEvent e) {
    finish();
  }
/**
 * If user pushes the cancel button.  Sets the value of number text field to -1 and closes the AskNumber dialog.  
 * @param e
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    value = -1;
    setVisible(false);
    dispose();
  }
/**
 * If input number is not a digit will beep.  Consumes either the delete or backspace keyed in by user.   
 * @param e
 */
  void numText_keyTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }
/**
 * If a window closing event occurs, the ask number dialog closes, and ask number value is set to -1. 
 * @param e
 */
  void this_windowClosing(WindowEvent e) {
    value = -1;
    setVisible(false);
    dispose();
  }
}

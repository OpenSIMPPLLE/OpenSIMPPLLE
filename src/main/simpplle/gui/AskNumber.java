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
  private int value;

  private JPanel mainPanel = new JPanel();
  private JPanel buttonPanel = new JPanel();
  private JPanel valuesPanel = new JPanel();
  private JTextField numText = new JTextField();
  private JLabel label = new JLabel();
  private JButton cancelPB = new JButton();
  private JButton okPB = new JButton();
  private FlowLayout flowLayout2 = new FlowLayout();
  private FlowLayout flowLayout3 = new FlowLayout();
  private BorderLayout borderLayout1 = new BorderLayout();

  /**
  * Constructor for AskNumber method. Calls the jbInit(), sets the label, integer value, sets the size and repaints the graphics.
  * @param frame
  * @param title
  * @param modal
  * @param msg
  * @param value
  */
  AskNumber(Frame frame, String title, boolean modal, String msg, int value) {
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
  AskNumber(Frame frame, String title, boolean modal, String msg) {
    this(frame,title,modal,msg,1);
  }

  /**
   * Overloaded AskNumber constructor.  Sets frame as null, title to empty string, modality false, and message to null.  
   */
  AskNumber() {
    this(null, "", false,null);
  }

  /**
   * Sets the layout, panels, action listeners, and text for Ask Number
   * @throws Exception
   */
  private void jbInit() throws Exception {

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });

    valuesPanel.setLayout(flowLayout2);
    buttonPanel.setLayout(flowLayout3);
    mainPanel.setLayout(borderLayout1);

    numText.setText("1");
    numText.setColumns(8);
    numText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        numText_keyTyped(e);
      }
    });
    numText.addActionListener(this::numText_actionPerformed);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(this::cancelPB_actionPerformed);
    okPB.setText("Ok");
    okPB.addActionListener(this::okPB_actionPerformed);

    valuesPanel.add(label);
    valuesPanel.add(numText);
    buttonPanel.add(okPB);
    buttonPanel.add(cancelPB);
    mainPanel.add(valuesPanel, BorderLayout.NORTH);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);
    add(mainPanel);
  }

  /**
  * Creates a new instance of askNumber dialog, sets visible and returns the dialog value.
  * @param title
  * @param msg
  * @param value
  * @return
  */
  static int getInput(String title, String msg, int value) {
    return getInput(title, msg, value, null);
  }

  static int getInput(String title, String msg, int value, Point location){
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
  static int getInput(String title, String msg) {
    return AskNumber.getInput(title,msg,1);
  }

  /**
   * Parses the number in number (the number being asked) text field casts to Integer object.  
   * @throws NumberFormatException().  Will beep if no value is entered and set value as -1 ... a flag
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
   * Sets the value of number text field to -1 and closes the AskNumber dialog.
   */
  private void cancel(){
    value = -1;
    setVisible(false);
    dispose();
  }

  /**
  * If an event occurs in ask number text field sends to finish()
  */
  private void numText_actionPerformed(ActionEvent e) {
    finish();
  }

  /**
   * if user pushes the ok button. sends to finish()
   */
  private void okPB_actionPerformed(ActionEvent e) {
    finish();
  }

  /**
   * If user pushes the cancel button. Sets the value of number text field to -1 and closes the AskNumber dialog.
   */
  private void cancelPB_actionPerformed(ActionEvent e) {
    cancel();
  }

  /**
   * If input number is not a digit will beep.  Consumes either the delete or backspace keyed in by user.
   * @param e key typed
   */
  private void numText_keyTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }

  /**
  * If a window closing event occurs. Sets the value of number text field to -1 and closes the AskNumber dialog.
  * @param e
  */
  private void this_windowClosing(WindowEvent e) {
    cancel();
  }
}

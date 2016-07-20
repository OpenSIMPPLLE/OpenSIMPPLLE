/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.SwingUtilities;

/**
 * This class creates JLabeledNumberTextField, a type of JPanel.
 * This differs from the JLabeledDoubleTextField in that while both have labels, this has an integer value.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */
public class JLabeledNumberTextField extends JPanel {
  private int value;

  JLabel label = new JLabel();
  JTextField textField = new JTextField();
  FlowLayout flowLayout1 = new FlowLayout();
  /**
   * Overloaded constructor. Passes to primary constructor empty string for label text and 1 for value.  
   */
  public JLabeledNumberTextField() {
    this("",1);
  }
  /**
   * Constructor for JLabeledNumberTextField.  Initializes the label text, value, and text field text to integer toString  
   * @param label
   * @param value
   */
  public JLabeledNumberTextField(String label, int value) {
    try {
      jbInit();
      this.label.setText(label);
      this.value = value;
      this.textField.setText(Integer.toString(value));
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  /**
   * Sets up the JLabeledNumberTextField with a label and text field in a flowlayout.
   * @throws Exception
   */
  private void jbInit() throws Exception {
    this.setLayout(flowLayout1);
    label.setText("jLabel1");
    textField.setText("jTextField1");
    textField.setColumns(10);
    textField.addKeyListener(new JLabeledNumberTextField_textField_keyAdapter(this));
    flowLayout1.setAlignment(FlowLayout.LEFT);
    this.add(label, null);
    this.add(textField, null);
  }
  /**
   * Gets the value.  
   * @return value
   */
  public int getValue() { return value; }
  /**
   * Sets the value for this JLabeledNumberTextField to passed int, and sets the text field text to its toString. 
   * @param value double to be set
   */
  public void setValue(int value) {
    this.value = value;
    textField.setText(Integer.toString(value));
  }
/**
 * Sets the label text.  
 * @param value
 */
  public void setLabel(String value) {
    label.setText(value);
  }
  /**
   * Handles key event.  If the key event is a digit, and not delete, backspace, or enter, returns true.  
   * Otherwise it consumes the key event, returns false, and beeps.
   * @param e key event (goal of this method is the key event will be a digit)
   * @return true if key event is a digit
   */
  private boolean digitTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE &&
        key != KeyEvent.VK_ENTER) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
      return false;
    }
//    return (key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE && key != KeyEvent.VK_ENTER);
    return true;
  }
  /**
   * Handles a key event in the text field.  If the key event is a not a digit, or is delete, backspace, or enter it consumes the event.
   * Otherwise it creates a new runnable which lasts the length of input into textfield and parses the integer typed there.  
   * @param e
   */
  public void textField_keyTyped(KeyEvent e) {
    if (!digitTyped(e)) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doUpdate = new Runnable() {
      public void run() {
        value = (textField.getText().length() > 0) ?
                Integer.parseInt(textField.getText()) : value;
      }
    };
    SwingUtilities.invokeLater(doUpdate);
  }
}
/**
 * Creates a key adapter to handle the key event.  This saves on work in implementing key event listeners.  
 * It passes to method within JLabeledNumberTextField class.  
 *
 */
class JLabeledNumberTextField_textField_keyAdapter extends KeyAdapter {
  private JLabeledNumberTextField adaptee;
  JLabeledNumberTextField_textField_keyAdapter(JLabeledNumberTextField adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key adaptor which calls textField_keyTyped(e) to handle typing in the text field  
   */
    
  public void keyTyped(KeyEvent e) {
    adaptee.textField_keyTyped(e);
  }
}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates JNumberTextField, a type JTextField
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 * 
 *     
 */
public class JNumberTextField extends JTextField {
  private int value;
/**
 * Constructor for JNumberTextField.  Calls the JTextField superclass to create a new text field, adds key listener (which is a key adaptor)
 */
  public JNumberTextField() {
    super();
    addKeyListener(new JNumberTextField_textField_keyAdapter(this));
  }
  /**
   * Handles key event.  If the key event is a digit, and not delete, backspace, or enter, returns true.  
   * Otherwise it consumes the key event, returns false and beeps
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
  public void textField_keyTyped(KeyEvent e) {
    if (!digitTyped(e)) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doUpdate = new Runnable() {
      public void run() {
        value = (getText().length() > 0) ?
                Integer.parseInt(getText()) : value;
      }
    };
    SwingUtilities.invokeLater(doUpdate);
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
    super.setText(Integer.toString(value));
  }
}

class JNumberTextField_textField_keyAdapter extends KeyAdapter {
  private JNumberTextField adaptee;
  JNumberTextField_textField_keyAdapter(JNumberTextField adaptee) {
    this.adaptee = adaptee;
  }

  public void keyTyped(KeyEvent e) {
    adaptee.textField_keyTyped(e);
  }
}

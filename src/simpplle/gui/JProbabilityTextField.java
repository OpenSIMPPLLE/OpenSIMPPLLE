/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

/**
 * This class creates JProbabilityTextField, a type JDataTextField, which itself is a type of JTextField.
 * Note: Probability is taken in as a an int in this class.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */
public class JProbabilityTextField extends JDataTextField {
  int probability=0;
  /**
   * Creates a new JProbabilityTextField field with the number of columns set to 5 passed to JDataTextField superclass
   * along with the integer probability as the datasource, the reason for this special class.    
   * @param prob.  Integer probability, which is made into Integer object and passed to JDataTextField as datasource variable.  
   */
  public JProbabilityTextField(int prob) {
    super(5, new Integer(prob));
  }
  /**
   * Creates a new JProbabilityTextField with the parameter number of columns passed to JDataTextField superclass along
   * with the Integer probability object as the datasource. 
   * Also sets the datasource for this JDataTextField, the reason for this special class.  
   * @param prob.  Integer probability, which is made into Integer object and passed to JDataTextField as datasource variable.
   * @param columns number of columns to make in text field.  
   */
  public JProbabilityTextField(int prob, int columns) {
    super(columns, new Integer(prob));
    init();
  }
  /**
   * Creates a new JProbabilityTextField with the parameter number of columns passed to JDataTextField superclass along
   * with the object datasource which is passed in a parameter 
   * Also sets the datasource for this JDataTextField, the reason for this special class.  
   * @param columns number of columns to make in text field. 
   * @param datasource.  the object datasource 
   */
  public JProbabilityTextField(int columns, Object dataSource) {
    super(columns, dataSource);
    init();
  }
  /**
   * Overloaded constructor.  Takes in the probability integer and makes into a string.  
   * Creates a new JProbabilityTextField with the number of columns passed to JDataTextField superclass.  
   * Also sets the datasource and passes string probability variable, the reason for this special class. 
   * The data variable is set to -1, a flag.  
   * @param text new variable added to JDataTextField 
   * @param columns number of columns for the JDataTextField
   * @param dataSource new variable added to JDataTextField
   */
  public JProbabilityTextField(int prob, int columns, Object dataSource) {
    super(Integer.toString(prob), columns, dataSource);
    init();
  }
  /**
   * Overloaded constructor.  Takes in the probability integer and makes into a string.  
   * Then creates a new text field with the number of columns and string of probability passed to JDataTextField superclass.  
   * Also sets the datasource and data variable for this JProbabilityTextField, the reason for this special class. 
   * @param text text used to initialize JTextFieldnew variable added to JTextField 
   * @param columns number of columns for the JTextField
   * @param dataSource new variable added to JTextField
   * @param data
   */
  public JProbabilityTextField(int prob, int columns, Object dataSource, int data) {
    super(Integer.toString(prob), columns, dataSource, data);
    init();
  }
/**
 * Init method that adds a key listener (is actually a key adapter, which provides a shortcut for key listeners) 
 */
  private void init() {
    this.addKeyListener(new java.awt.event.KeyAdapter() {
    	/**
    	 * Calls the localKeyTyped method to get the probability.  
    	 */
      public void keyTyped(KeyEvent e) {
        localKeyTyped(e);
      }
    });

  }
  /**
   * Gets the probability from text field.
   * @return
   */
  public int getProbability() { return probability; }
  public void setProbability(int prob) {
    probability = prob;
    setText(Integer.toString(prob));
  }
  /**
   * Handles key event.  If the key event is a digit, and not delete, backspace, or enter, returns true.  
   * Otherwise it consumes the key event, returns false, and beeps.
   * @param e key event (goal of this method is the key event will be a digit)
   * @return true if key event is not delete, back space, or enter.
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
    return (key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE && key != KeyEvent.VK_ENTER);
  }
  /**
   * Handles a key event in the text field.  If the key event is a not a digit and text length is 3, or is delete, backspace, or enter it consumes the event.
   * Otherwise it creates a new runnable which lasts the length of input into textfield and parses the integer typed there.  
   * @param e
   */
  void localKeyTyped(KeyEvent e) {
    if (digitTyped(e) && getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doProbUpdate = new Runnable() {
      public void run() {
        probability = (getText().length() > 0) ?
                      Integer.parseInt(getText()) : 0;
      }
    };
    SwingUtilities.invokeLater(doProbUpdate);
  }
}



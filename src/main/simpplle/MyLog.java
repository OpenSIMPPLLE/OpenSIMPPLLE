/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle;

import javax.swing.JTextArea;

/** 
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public final class MyLog {
  private static JTextArea textArea = null;
  private static boolean   useSystemOut = false;
  private static boolean   useSystemErr = false;
  private static int       caretPos     = 0;

  /**
   * Sets the default system to the SystemOut and SystemErr
   * 
   */
  public static void restoreDefaults() {
    useSystemOut(true);
    useSystemErr(true);
  }
  
  /**
   * Method to set system properties to specific system or the default system.   
   * @param b  If true use the default SystemOut, false do not use default System
   */
  public static void useSystemOut(boolean b) { useSystemOut = b; }
  
  /**
   * Method to set system error properties to specific system or the default system.   
   * @param b  If true use the default SystemErr, false do not use default System.  
   */
  public static void useSystemErr(boolean b) { useSystemErr = b; }

  /**
   * Method which which allows users to change the current system and system error.  
   * Overrides the default settings of SystemOut and SystemErr.  
   * Sets the cursor position (caretPos) in the text field to the beginning of the text field. 
   * @param ta JTextArea with text to set the system properties.   
   */
  public static void setTextArea(JTextArea ta) {
    useSystemOut = false;
    useSystemErr = false;
    textArea     = ta;
    caretPos     = 0;
  }

  /**
   * Sets the system to the default settings if the SystemOut properties are true or the text area for user designated system input is blank.  
   * Prints the system properties to console.  If SystemOut properties are false or text area contains text, 
   * the contents of JTextArea are printed to console and 
   *   
   */
  public static class stdout {
    public static void println(String s) {
      if (useSystemOut || textArea == null) {
        System.out.println(s);
      }
      else {
        textArea.append(s + "\n");
        caretPos += s.length() + 1;
        textArea.setCaretPosition(caretPos);
      }
    }
  }
  
  /**
   * Sets the system to the default error settings if the SystemErr properties are true or the text area for user designated system input is blank.  
   * Prints the system properties to console.  If SystemOut properties are false or text area contains text, 
   * the contents of JTextArea are printed to console and 
   *   
   */

  public static class stderr {
    public static void println(String s) {
      if (useSystemErr || textArea == null) {
        System.err.println(s);
      }
      else {
        textArea.append(s + "\n");
        caretPos += s.length() + 1;
        textArea.setCaretPosition(caretPos);
      }
    }
  }

}





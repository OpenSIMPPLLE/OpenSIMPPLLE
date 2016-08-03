/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class handles errors thrown
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public class MyErrorHandler {
  /**
   * Constructor for MyErrorHandler.  Does not initialize any variables.  
   */
	public MyErrorHandler() {
  }
/**
 * Has a hard coded error message that will be printed if it is either an out of memory error, or another type of error.  
 * This will send the thrown error stack trace to printwriter.  In addition a string is created with copy of stack trace that will be printed in a 
 * new dialog.  
 * @param thrown the error being thrown.  
 */
  public void handle(Throwable thrown) {
    thrown.printStackTrace();

    String msg, stackTrace;
    if (thrown instanceof java.lang.OutOfMemoryError) {
      msg = "The Java VM has run out of memory.\n" +
          "It is strongly advised that the application be closed and restarted\n" +
          "The Utilities menu has an option for changing the \"Java Heap Size\"\n" +
          "Increasing the heap size may help\n\n";
    }
    else {
      msg = "The application has encountered an error condition.\n" +
            "This error may have left the application in an unstable condition.\n" +
            "It is advised that the application be closed and restarted.\n" +
            "Please copy this text (select with mouse and type Control-C or Cmd-C on Mac)\n\n" +
            "Send the text in an email to OpenSimpplle bug reporting. \n\n";
    }

    StringWriter strWriter = new StringWriter();
    PrintWriter fout = new PrintWriter(strWriter);
    thrown.printStackTrace(fout);
    fout.flush();
    stackTrace = strWriter.toString();
    fout.close();

    JTextAreaDialog dlg = new JTextAreaDialog(simpplle.JSimpplle.getSimpplleMain(),
        "*** Warning: Runtime Error Encountered ***", true, msg + stackTrace);
    dlg.setVisible(true);
  }
}




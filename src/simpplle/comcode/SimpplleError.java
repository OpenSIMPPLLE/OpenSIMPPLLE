package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for OpenSimpplle errors, these are commonly thrown after catching other exceptions e.g. an IOException 
 * Often these are caught in the GUI and displayed to inform user of issues.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */


public class SimpplleError extends Exception {
  public SimpplleError(String msg) {
    super(msg);
    printInfo();
  }
  public SimpplleError(String msg, Throwable cause) {
    super(msg,cause);
    printInfo();
  }

  private void printInfo() {
    System.err.println(getMessage());

    if (simpplle.JSimpplle.developerMode()) {
      printStackTrace();
    }
  }
  public String getError() { return getMessage(); }
}

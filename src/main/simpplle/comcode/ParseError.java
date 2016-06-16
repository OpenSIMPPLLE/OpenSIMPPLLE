

package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>ParseError class part of knowledge file conversion system.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Ray Ford
 *
 */


public class ParseError extends Exception
  {public String msg;
/**
 * Constructor for Parse Error which includes a setting to print the strack trace in developer mode
 * @param msg
 */
   public ParseError(String msg) {
     super(msg);
     this.msg = msg;
     System.err.println(msg);

     if (simpplle.JSimpplle.developerMode()) {
       printStackTrace();
     }
   }
/**
 * Prints the throwable message.  
 * @return throwable message
 */
   public String getError() { return getMessage(); }
}


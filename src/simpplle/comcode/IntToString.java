/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>The purpose of this class is to provide pre-made string versions of
 * integers to avoid the creation of millions of temporary Strings during
 * simulations.
 * 
 *  <p>Note: Probability numbers are rationalized thus we need 10000 of these.
 *  This is a lot of strings, but 10000 strings is much better
 *  than millions.
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 */

public abstract class IntToString {
  private static final String[] intStrings;

  static {

    intStrings = new String[10100];
    for (int i=0; i<intStrings.length; i++) {
      intStrings[i] = Integer.toString(i);
    }
  }
/**
 * Gets the String representation of an integer at a particular parameter index into integer strings array
 * @param value the index into array
 * @return string representation of integer
 */
  public static String get(int value) {
//    if (value < 0 || value >= intStrings.length) {
//      return Integer.toString(value);
//    }
    return intStrings[value];
  }

}
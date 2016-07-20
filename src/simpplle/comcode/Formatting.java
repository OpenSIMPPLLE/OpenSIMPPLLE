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
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>A utility class with some functions to provide formatting.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */

public abstract class Formatting {
  // Pad a string on the left with spaces.
	
	/**
	 * method to pad a string with spaces on the left
	 * @param str
	 * @param numChars
	 * @param padRight true if pad right method, false for pad left
	 * @return a string of ' ' chars appended to the right for padRight, and inserted to the left for padLeft
	 */
  private static String pad(String str,int numChars, boolean padRight) {
    StringBuffer strBuf = new StringBuffer(str);
    char[] spaces;

    spaces = new char[numChars];

    for(int i=0;i<numChars;i++) {
      spaces[i] = ' ';
    }
    if (padRight) {
      strBuf.append(spaces);
      return (strBuf.toString());
    }
    else {
      strBuf.insert(0,spaces);
      return (strBuf.toString());
    }
  }
/**
 * calls the pad function with boolean padRight == false meaning insert spaces to left
 * @param str
 * @param numChars
 * @return left padded string
 */
  public static String padLeft(String str,int numChars) {
    return pad(str,numChars,false);
  }
/**
 * calls the pad function with booolean padRight == true meaning append speces to right
 * @param str
 * @param numChars
 * @return right padded string
 */
  
  public static String padRight(String str,int numChars) {
    return pad(str,numChars,true);
  }

  /**
   * puts a string into a fixed field width
   * @param str
   * @param fieldWidth the integer width of  text field
   * @return passes to overloaded fixedField method for calculation  
   */
  public static String fixedField(String str, int fieldWidth) {
    return fixedField(str,fieldWidth,false);
  }
/**
 * pads the field with appropriate amount of ' ' chars to make a well-formatted field 
 * @param str
 * @param fieldWidth integer width of text field
 * @param padRight true if padRight, false if padLeft
 * @return
 */
  public static String fixedField(String str, int fieldWidth,
                                  boolean padRight) {

    fieldWidth = fieldWidth - str.length();
    if (fieldWidth > 0) {
      return pad(str,fieldWidth,padRight);
    }
    else { return str; }
  }
/**
 *converts an integer object to a int and passes to overloaded fixedField method for padding
 * @param num the number to be inserted (passed as Integer so must be int.value to be made into string)
 * @param fieldWidth the integer width of the text field
 * @return a fixed text field properly formatted with padding left or right
 */
  public static String fixedField(Integer num, int fieldWidth) {
    return fixedField(num.intValue(), fieldWidth);
  }

  /**
   * overloaded fixedField method, puts an integer into a fixed field width and pads on the left  
   * @param num
   * @param fieldWidth
   * @return calls to fixedField will returns 
   */
  public static String fixedField(int num, int fieldWidth) {
    return fixedField(num,fieldWidth,false);
  }
/**
 * converts int number to be fit into field to a string then passes onto the pad method if needs left padding, else returns the number as string
 * @param num
 * @param fieldWidth
 * @param padRight
 * @return
 */
  public static String fixedField(int num, int fieldWidth, boolean padRight) {
    String tmp = String.valueOf(num);

    fieldWidth = fieldWidth - tmp.length();
    if (fieldWidth > 0) {
      return pad(tmp,fieldWidth,padRight);
    }
    else { return tmp; }
  }
/**
 * 
 * @param num the number to be fitted into field
 * @param fieldWidth integer value of text field width
 * @return a fitted string with long number converted to string and padded as needed 
 */
  public static String fixedField(long num, int fieldWidth) {
    return fixedField(num,fieldWidth,false);
  }
/**
 * converts a long into a string then passes to pad if needs left padding, or returns string if does not need padding
 * @param num
 * @param fieldWidth
 * @param padRight
 * @return
 */
  public static String fixedField(long num, int fieldWidth, boolean padRight) {
    String tmp = String.valueOf(num);

    fieldWidth = fieldWidth - tmp.length();
    if (fieldWidth > 0) {
      return pad(tmp,fieldWidth,padRight);
    }
    else { return tmp; }
  }

  /**
   * passes to overloaded fixedField method 
   * @param num the floating point number to be padded if needed
   * @param fieldWidth integer value of text field width
   * @return a fitted string padded converts a long into a string then passes to pad if needs left padding, or returns string if does not need padding to the left if needed
   */
  public static String fixedField(float num, int fieldWidth) {
    return fixedField(num,fieldWidth,false);
  }
/**
 * converts floating point number to a string and passes to pad method if left padding is needed 
 * @param num
 * @param fieldWidth
 * @param padRight
 * @return a formatted string with floating point number converted to string and padded to the left as needed or the converted floating pad if no padding needed
 */
  public static String fixedField(float num, int fieldWidth,
                                  boolean padRight) {
    String tmp = String.valueOf(num);

    fieldWidth = fieldWidth - tmp.length();
    if (fieldWidth > 0) {
      return pad(tmp,fieldWidth,padRight);
    }
    else { return tmp; }
  }
}

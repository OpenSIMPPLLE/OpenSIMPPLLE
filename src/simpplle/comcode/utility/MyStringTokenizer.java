package simpplle.comcode.utility;

import simpplle.comcode.ParseError;

import java.io.*;
import java.util.*;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains a customized string tokenizer.  Possible delimiters are ','  or ':'
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */

public class MyStringTokenizer {
  private static final char DEF_DELIM = ',';
  private static final char DEF_LIST_DELIM = ':';

  private StringBuffer strBuf;
  private char         delim;
  private char         listDelim = ':';
  private int          pos;
  private char         noData = '?';


  public MyStringTokenizer(StringBuffer strBuf, char delim, char listDelim) {
    pos            = 0;
    this.strBuf    = strBuf;
    this.delim     = delim;
    this.listDelim = listDelim;
  }

  public MyStringTokenizer(StringBuffer strBuf, char delim) {
    this(strBuf,delim,DEF_LIST_DELIM);
  }

  public MyStringTokenizer(StringBuffer strBuf) {
    this(strBuf,DEF_DELIM);
  }
/**
 * Checks if a char is a delimiter.   Possible delimiters are ','  or ':'
 * @param c
 * @param checkList
 * @return
 */
  private boolean isDelim(char c, boolean checkList) {
    if (checkList && (c == delim || c == listDelim)) {
      return true;
    }
    else if (checkList == false && c == delim) {
      return true;
    }
    else { return false; }
  }

  public String nextToken() { return nextToken(false); }
/**
 * 
 * @param list
 * @return
 */
  public String nextToken(boolean list) {
    if (pos >= strBuf.length()) { return null; }

    String result = null;
    int    start = pos;
    char   c = strBuf.charAt(pos);

    while (isDelim(c,list) == false) {
      pos++;
      if (pos == strBuf.length()) { break; }
      c = strBuf.charAt(pos);
    }
    if (start != pos) {
      result = strBuf.substring(start,pos);
      if (result != null) { result = result.intern(); }
    }
    pos++;

    return result;
  }
/**
 * Resets the position to the initial position of 0.
 */
  public void reset() { pos = 0; }

  public void setNoData(char c) { noData = c;}
/**
 * Checks if there is no data left in the strength.  (If string length ==1 and the char which represents the string length 1 is '?'.  Then 
 * this boolean will be true meaning no data is left, otherwise false meaning keep going.
 * @param str the string to be checked for data presence
 * @return true if reaches end of string with data - '?'
 */
  private boolean noData (String str) {
    if (str.length() == 1 && str.charAt(0) == noData) {
      return true;
    }
    else {
      return false;
    }
  }
/**
 * Checks if there are more tokens to be parsed.  
 * @return true if more tokens.  
 */
  public boolean hasMoreTokens() {
    return (pos < (strBuf.length() - 1));
  }
/**
 * 
 * @return string parsed from delimited file
 * @throws ParseError thrown if not enough fields, caught in GUI
 */
  public String getToken() throws ParseError {
    String result = null;

    if (hasMoreTokens()) {
      result = nextToken();
      if (noData(result)) { result = null;}

      return result;
    }
    else {
      throw new ParseError("Not enough fields.");
    }
  }

  /**
   * Variation on getToken(), the difference being that this will throw an
   * exception if the value found is null.
   * @return the non-null String value read.
   * @throws ParseError
   */
  public String getStringToken() throws ParseError {
    String str = getToken();
    if (str == null) {
      throw new ParseError("Empty field found.");
    }
    else {
      return str;
    }
  }
/**
 * 
 * @return an int parsed from a string
 * @throws ParseError catches number format exception and throws parse error caught in GUI
 */
  public int getIntToken() throws ParseError {
    String str;
    int value;

    try {
      str = getToken();
      if (str != null) {
        value = Integer.parseInt(str);
      }
      else { value = -1;}
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid Integer found.");
    }
    return value;
  }
/**
 * 
 * @return a float value parsed from a string
 * @throws ParseError catches number format exception and throws a parse error caught in GUI
 */
  public float getFloatToken() throws ParseError {
    String str = "";
    float  value;

    try {
      str = getToken();
      if (str != null) {
        value = Float.valueOf(str).floatValue();
      }
      else { value = -1.0f;}
    }
    catch (NumberFormatException NFE) {
      String msg = "Invalid floating point number found: " + str;
      throw new ParseError(msg);
    }
    return value;
  }
  /**
   * 
   * @return a double value parsed from a string
   * @throws ParseError catches number format error than throws parse error caught in GUI
   */

  public double getDoubleToken() throws ParseError {
    String str = "";
    double value;

    try {
      str = getToken();
      if (str != null) {
        value = Double.valueOf(str).doubleValue();
      }
      else { value = -1.0;}
    }
    catch (NumberFormatException NFE) {
      String msg = "Invalid floating point number found: " + str;
      throw new ParseError(msg);
    }
    return value;
  }
/**
 * counts the delimters in a string buffer.  the string delimter is ','
 * @return a count +1 of the delimiters
 */
  public int countTokens() {
    int savedPos = pos;
    int count = 0;

    while (pos < strBuf.length()) {
      if (strBuf.charAt(pos) == delim) { count++; }
      pos++;
    }
    pos = savedPos;
    return (count + 1);
  }
/**
 * pos = position in boolean list.  the list delimiter is ':'
 * @param list
 * @return a count +1 from a boolean list 
 */
  public int countTokens(boolean list) {
    int savedPos = pos;
    int count = 0;

    while (pos < strBuf.length() && strBuf.charAt(pos) != delim) {
      if (strBuf.charAt(pos) == listDelim) {
        count++;
      }
      pos++;
    }
    pos = savedPos;
    return (count + 1);
  }
  public Vector getListValue () throws ParseError, IOException {
    return getListValue(false);
  }

  private int getPos() { return pos; }
  private void setPos(int newPos) { pos = newPos; }

 /**
  * read in a delimited list then put into vector
  * @param getInt
  * @return
  * @throws ParseError is a number format error.  caught in GUI
  * @throws IOException caught in GUI
  */
  public Vector getListValue (boolean getInt) throws ParseError, IOException {
    Vector            result = null;
    Integer           intVal;
    String            value = null;
    boolean           moreTokens;

    int count = countTokens(true);
    if (count == 0) { return null; }

    value = nextToken(true);
    if (noData(value)) { return null; }

    result = new Vector(count);

    do {
      if (!getInt) {
        result.addElement(value);
      }
      else {
        try {
          intVal = Integer.valueOf(value);
        }
        catch (NumberFormatException NFE) {
          throw new ParseError("Invalid number found when reading a list.");
        }
        result.addElement(intVal);
      }
      count--;
     if (count > 0) { value = nextToken(true);}
    }
    while (count > 0);

    return result;
  }

}
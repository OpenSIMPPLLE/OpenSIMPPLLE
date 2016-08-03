/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.*;

/** 
 * This class entends the StringTokenizer class, providing a few more function which are useful to this program.
 *   
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class StringTokenizerPlus extends StringTokenizer {
  String listDelim = ":";
  char   noData = '?';

  public StringTokenizerPlus (String str) {
    super(str);
  }

  public StringTokenizerPlus (String str, String delim) {
    super(str, delim);
  }

  public StringTokenizerPlus (String str, String delim, String listDelim) {
    this(str,delim);
    this.listDelim = listDelim;
  }

  public void setNoData(char c) { noData = c;}

  private boolean noData (String str) {
    if (str.length() == 1 && str.charAt(0) == noData) {
      return true;
    }
    else {
      return false;
    }
  }

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

  public int getIntToken() throws ParseError {
    return getIntToken(-1);
  }
  public int getIntToken(int invalidValue) throws ParseError {
    String str;
    int value;

    try {
      str = getToken();
      if (str != null) {
        value = Integer.parseInt(str);
      }
      else { value = invalidValue; }
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid Integer found.");
    }
    return value;
  }

  public float getFloatToken() throws ParseError {
    return getFloatToken(-1.0f);
  }
  public float getFloatToken(float invalidValue) throws ParseError {
    String str = "";
    float  value;

    try {
      str = getToken();
      if (str != null) {
        value = Float.valueOf(str).floatValue();
      }
      else { value = invalidValue;}
    }
    catch (NumberFormatException NFE) {
      String msg = "Invalid floating point number found: " + str;
      throw new ParseError(msg);
    }
    return value;
  }
  public double getDoubleToken() throws ParseError {
    return getDoubleToken(-1.0);
  }

  public double getDoubleToken(double invalidValue) throws ParseError {
    String str = "";
    double value;

    try {
      str = getToken();
      if (str != null) {
        value = Double.valueOf(str).doubleValue();
      }
      else { value = invalidValue;}
    }
    catch (NumberFormatException NFE) {
      String msg = "Invalid floating point number found: " + str;
      throw new ParseError(msg);
    }
    return value;
  }

  public Vector getListValue () throws ParseError, IOException {
    return getListValue(false);
  }

  // Read in a delimited list, return in a vector.
  public Vector getListValue (boolean getInt) throws ParseError, IOException {
    StringTokenizer strTok;
    Vector          result = null;
    Integer         intVal;
    String          value = null;
    boolean         moreTokens;

    value = getToken();
    if (value == null) { return null;}

    strTok = new StringTokenizer(value,listDelim);
    value = null;

    if (strTok.hasMoreTokens()) {
      value = strTok.nextToken();
      if (noData(value)) { value = null;}
    }
    if (value == null) {return null;}

    result = new Vector(strTok.countTokens());

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
      moreTokens = strTok.hasMoreTokens();
      if (moreTokens) { value = strTok.nextToken();}
    }
    while (moreTokens);

    return result;
  }

}



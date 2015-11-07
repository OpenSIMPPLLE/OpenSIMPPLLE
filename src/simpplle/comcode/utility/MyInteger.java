package simpplle.comcode.utility;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for MyInteger which makes a custom Integer object. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public final class MyInteger implements Comparable {
  private int value;
/**
 * My Integer constructor, which gives integer its one variable which is a value (will be an intvalue) and initializes it to 0.
 */
  public MyInteger() {
    value = 0;
  }
/**
 * Sets the MyInteger value variable (will be an int) to the parameter int value. 
 * @param theValue
 */
  public MyInteger(int theValue) {
    value = theValue;
  }
/*
  public MyInteger(Integer theValue) {
    value = theValue.intValue();
  }

  public MyInteger(MyInteger theValue) {
    value = theValue.intValue();
  }
*/
  /**
   * Sets the MyInteger value.
   * @param v since this is an MyInteger object, the value will be a Integer object.  
   */
  public void setValue(int v) {
    value = v;
  }
/*
  public void setValue(Integer v) {
    value = v.intValue();
  }

  public void setValue(MyInteger v) {
    value = v.intValue();
  }
*/
  /**
   * Gets the value of MyInteger 
   * @return
   */
  public int intValue() { return value; }
/**
 * Method to +=1 to myInteger value
 */
  public void plusplus() { plus(1); }
/**
 * Method to make += java addition of parameter to the MyInteger value. 
 * @param val
 */
  public void plus(int val) {
    value += val;
  }
  /**
   * Method to -=1 to myInteger value
   */
  public void minusminus() { minus(1); }
  /**
   * Method to make -= java addition of parameter to the MyInteger value. 
   * @param val
   */
  public void minus(int val) {
    value -= val;
  }

  public boolean equals(Object obj) {
          if ((obj != null) && (obj instanceof MyInteger)) {
            return value == ((MyInteger)obj).intValue();
          }
        return false;
  }

  public int hashCode() {
    return value;
  }
/**
 * Requisite compareTo method.  Checks if the int value of a MyInteger object is greater or lesser than this MyInteger object.  
 */
  public int compareTo(Object o) {
    if ((o != null) && (o instanceof MyInteger)) {
      if (this.equals(o)) {
        return 0;
      }
      else if (intValue() < ((MyInteger)o).intValue()) {
        return -1;
      }
      else {
        return 1;
      }
    }
    return -1;
  }
/**
 * toString of MyInteger object, which will be an Integer object cast to string.  
 */
  public String toString() { return Integer.toString(value); }
}


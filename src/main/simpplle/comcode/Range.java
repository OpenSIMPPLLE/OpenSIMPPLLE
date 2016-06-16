package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * <p>This class contains methods for Range, primarily methods to set, calculate, and get the lower and upper range limits.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 */
public class Range implements Comparable {
  int lower, upper;

  /**
   * Constructor for range.  takes in argument for lower and upper limits of range.  
   * @param lower 
   * @param upper
   */
  public Range(int lower, int upper) {
    this.lower = lower;
    this.upper = upper;
  }
/**
 * Gets lower limit of range.  
 * @return lower limit of range
 */
  public int getLower() {
    return lower;
  }
/**
 * Gets upper limit of range.  
 * @return upper limit of range
 */
  public int getUpper() {
    return upper;
  }
/**
 * Sets the lower limit of range from parameter.  
 * @param lower the lower range limit. 
 */
  public void setLower(int lower) {
    this.lower = lower;
  }
/**
 * Sets upper limit of range
 * @param upper upper range limit to be set. 
 */
  public void setUpper(int upper) {
    this.upper = upper;
  }
/**
 * Checks if parameter int value is above the lower limit and below the upper limit.  
 * @param value the int value to be checked if is in range.  
 * @return true if value being evaluated is above the lower limit and below the upper limit.
 */
  public boolean inRange(int value) {
    return (value >= lower && value <= upper);
  }
  /**
   * Float version of range check to see if parameterfloat value is above the lower limit and below the upper limit.  
   * @param value the float value to be checked if is in range.  
   * @return true if value being evaluated is above the lower limit and below the upper limit.
   */
  public boolean inRange(float value) {
    return (value >= lower && value <= upper);
  }
  /**
   * Checks if two parameter int values a lower and upper are between or equal to the corresponding lower and upper limits.
   * @param lowerVal the lower value to be evaluated against the lower limit
   * @param upperVal the upper value to be evaluated against the upper limit
   * @return true if the two parameter int values a lower and upper are between or equal to the corresponding lower and upper limits.
   */
  public boolean withinRange(int lowerVal, int upperVal) {
    return (lower >= lowerVal && upper <= upperVal);
  }
/**
 * Required compareTo method of Comparable interface.  Compares two range objects to make sure lower limits and upper limits are the same.  
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    if (lower < ((Range) o).lower) {
      return -1;
    }
    else if (lower == ((Range) o).lower) {
      if (upper < ((Range) o).upper) {
        return -1;
      }
      else if (upper == ((Range) o).upper) {
        return 0;
      }
      else {
        return 1;
      }
    }
    else {
      return 1;
    }
  }
  /**
   * Equality method to check if a parameter Range object and this one's lower and upper limits are both the same.  
   */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof Range) {
      return (lower == ((Range)obj).lower) &&
             (upper == ((Range)obj).upper);
    }
    return false;
  }
  /**
   * Creates the hash code for this range object.  
   */
  public int hashCode() {
    return (new Integer(lower).hashCode() ^ new Integer(upper).hashCode());
  }

}



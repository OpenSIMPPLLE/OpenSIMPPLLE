package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains the Range methods for double values.  
 * This differs from the Range class in that it takes in doubles instead of ints.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 */
public class RangeDouble {
  double lower, upper;
/**
 * Constructor for double range.  This differs from the Range class in that it takes in doubles instead of ints.  
 * @param lower
 * @param upper
 */
  public RangeDouble(double lower, double upper) {
    this.lower = lower;
    this.upper = upper;
  }
/**
 * Gets the lower range limit .
 * @return lower range limit.
 */
  public double getLower() {
    return lower;
  }
/**
 * Gets the upper range limit.  
 * @return upper range limit.  
 */
  public double getUpper() {
    return upper;
  }
/**
 * Sets the lower range limit from parameter.
 * @param lower the lower range limit to be set
 */
  public void setLower(double lower) {
    this.lower = lower;
  }
/**
 * Sets the upper range limit from parameter.
 * @param upper the upper range limit to be set.  
 */
  public void setUpper(double upper) {
    this.upper = upper;
  }
/**
 * Calculates if parameter double value is above the lower and below the upper range limits.  Basically is the value in the range.  
 * @param value the double to be evaluated for whether it is in range
 * @return true if the value is between the lower and upper range limits.  
 */
  public boolean inRange(double value) {
    return (value > lower && value < upper);
  }

}



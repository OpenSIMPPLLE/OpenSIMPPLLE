/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.ArrayList;
import java.io.*;

/**
 * This class contains methods for Natural Elements.  Natural elements are EVU, EAU, and ELU.
 * This class will be extended by the classes that define these objects.  
 * 
 * <p>Note:
 * Special note on acres
 * Acres is really a float with n digits of precision.
 * It is stored as an int, to avoid the complications that floating point numbers can cause, especially
 * with respect to comparisons. Number of digits of precision is set in a static variable in the Area class.
 * Acres is displayed to the user as a floating point number by dividing acres by (10^n)
 *   
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public abstract class NaturalElement implements Externalizable {

  static final long serialVersionUID = -4390181849356261650L;
  static final int  version          = 3;

  protected static final String COMMA         = ",";
  protected static final String COLON         = ":";
  protected static final String SEMICOLON     = ";";
  protected static final String QUESTION_MARK = "?";

  protected int    id;
  protected int    acres;
  /**
   *  Elevation is recorded in Meters
   */
  protected int    elevation;
  protected String aspectName;
  protected double aspect;
  protected float  slope;

  protected ArrayList<NaturalElement> neighbors;

  protected static boolean hasNumericAspect = false;

  public static final int INVALID_ELEV = -999999;

  /**
   * Natural Element method initializes values elevation to -999999, aspect name to null and aspect to NaN
   */
  protected NaturalElement() {
    elevation  = INVALID_ELEV;
    aspectName = null;
    aspect     = Double.NaN;
  }

  /**
   * Overloaded Natural Element method references the other natural element but sets the natural element ID 
   * @param id
   */
  protected NaturalElement(int id) {
    this();
    this.id = id;
  }

  /**
   * Gets the natural elements id.
   * @return a natural element ID
   */
  public int getId() { return id; }

  public void setId(int value) { id = value; }

  public int getAcres() { return acres; }

  /**
   * overloaded setAcres method from int value, acres are stored as an int
   * @param value
   */
  public void setAcres(int value) { acres = value; }

  /**
   * overloaded setAcres method from a float value - acres are stored as an int
   * @param val
   */
  public void setAcres(float val) {
    acres = Math.round(val * Utility.pow(10,Area.getAcresPrecision()));
  }

  /**
   * Gets the acres.  This is stored as an int so it must be cast to float.
   * @return float version of natural element acreage.  
   */
  public float getFloatAcres() {
    return ( (float)acres / (float)Utility.pow(10,Area.getAcresPrecision()) );
  }

  /**
   * Gets the elevation of this natural element.
   * @return elevation of natural element
   */
  public int getElevation() { return elevation; }

  /**
   * @return elevation of element converted to feet
   */
  public double getElevationFeet(){
    return elevation * 3.28084;
  }

  public void setElevation(int value) { elevation = value; }

  /**
   * invalid elevation set as -999999 above
   * @return true if does not equal invalid elevation 
   */
  public boolean isElevationValid() { return elevation != INVALID_ELEV; }

  /**
   * Gets the natural element aspect name.
   * @return aspect name
   */
  public String getAspectName() { return aspectName; }

  /**
   * Sets the natural element aspect name
   * @param value the aspect name
   */
  public void setAspectName(String value) { aspectName = value; }

  /**
   * Gets the natural element aspect.
   * @return aspect
   */
  public double getAspect() { return aspect; }

  /**
   * Sets the natural elements aspect and changes the boolean for has numeric aspect to true.
   * @param value
   */
  public void setAspect(double value) {
    aspect = value;
    hasNumericAspect = true;
  }

  /**
   * Checks if natural element has numeric aspect
   * @return true if natural element has numeric aspect
   */
  public static boolean hasNumericAspect() { return hasNumericAspect; }

  /**
   * Gets the slope of the natural element.
   * @return the natural element slope
   */
  public float getSlope() { return slope; }

  /**
   * Sets the slope of the natural element.  
   */
  public void setSlope(float value) { slope = value; }

  /**
   * Adds a natural element object to neighbor arraylist if is not already in list
   * @param value
   */
  public void addNeighbor(NaturalElement value) {
    if (neighbors == null) { neighbors = new ArrayList(); }
    if (neighbors.contains(value)) { return; }
    neighbors.add(value);
  }

  /**
   * Gets the adjacent natural elements.
   * @return adjacent natural elements.
   */
  public ArrayList getNeighbors() { return neighbors; }

  /**
   * Checks if acres is greater than 0.
   * @return true if acres are greater than 0
   */
  public boolean isAcresValid() {
    return (acres > 0);
  }

  /**
   * Gets the string version of Natural Elements ID
   * @return id of natural element (in string form)
   */
  public String toString() { return Integer.toString(id); }

  public abstract boolean isValid();

  /**
   * Method to copy variables from one natural element object to another.
   * @param copyUnit Natural element object to to be copied
   */
  protected void copyFrom(NaturalElement copyUnit) {
    acres      = copyUnit.acres;
    elevation  = copyUnit.elevation;
    aspectName = copyUnit.aspectName;
    aspect     = copyUnit.aspect;
    slope      = copyUnit.slope;
    neighbors  = new ArrayList(copyUnit.neighbors);
  }

  /**
   * Reads Natural Element object from external source.  Reads, in order, the following:
   * ID, acres, elevation, aspect name, aspect, slope, neighbors and gets the evu from id.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    id        = in.readInt();
    acres     = in.readInt();
    elevation = in.readInt();
    aspectName    = (String)in.readObject();
    if (version > 2) {
      aspect = in.readDouble();
      if (aspect != Double.NaN) { hasNumericAspect = true; }
    }
    slope     = in.readFloat();

    if (version == 1) {
      neighbors = (ArrayList) in.readObject();
    } else {
      Area area = Simpplle.getCurrentArea();
      int size = in.readInt();
      neighbors = new ArrayList<NaturalElement>(size);
      for (int i=0; i<size; i++) {
        int id = in.readInt();
        if (this instanceof ExistingLandUnit) {
          ExistingLandUnit unit = area.getElu(id);
          if (unit == null) {
            unit = new ExistingLandUnit(id);
            area.addElu(unit);
          }
          neighbors.add(unit);
        } else if (this instanceof Evu) {
          Evu unit = area.getEvu(id);
          if (unit == null) {
            unit = new Evu(id);
            area.addEvu(unit);
          }
          neighbors.add(unit);
        }
      }
    }
  }

  /**
   * Writes Natural Element object to an external location, in order, the following:
   * version, ID, acres, elevation, aspect name, aspect, slope, size of neighbors class, and id of neighbors
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeInt(id);
    out.writeInt(acres);
    out.writeInt(elevation);
    out.writeObject(aspectName);
    out.writeDouble(aspect);
    out.writeFloat(slope);

    int size = neighbors != null ? neighbors.size() : 0;
    out.writeInt(size);
    for (int i=0; i<size; i++) {
      out.writeInt(neighbors.get(i).getId());
    }
  }

}



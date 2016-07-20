/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.HashMap;
import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-EarthSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Resistence.  The choices for this are LOW, MODERATE, HIGH, UNKNOWN, CONDITIONAL.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */

public class FireResistance implements Externalizable, Comparable {

  static final long serialVersionUID = -9196226766379590119L;
  static final int  version          = 1;

  private String name;
  private int    id;

  private static transient HashMap allResistance = new HashMap(3);

  public static final FireResistance LOW         = new FireResistance("LOW",FireEvent.LOW);
  public static final FireResistance MODERATE    = new FireResistance("MODERATE",FireEvent.MODERATE);
  public static final FireResistance HIGH        = new FireResistance("HIGH",FireEvent.HIGH);
  public static final FireResistance UNKNOWN     = new FireResistance("UNKNOWN/NA",-1);
  public static final FireResistance CONDITIONAL = new FireResistance("CONDITIONAL",-2);

  private static FireResistance[] allValues = new FireResistance[] {LOW, MODERATE, HIGH, UNKNOWN, CONDITIONAL};

  /**
   * Primary constructor.  No variables are set.
   */
  public FireResistance() { }

  /**
   * Overloaded constructor.  References primary, default constructor and initializes name and id of Fire Resistance.
   * Also puts the FireResistance object into hashmap keyed by the fire resistance name.
   * @param name
   * @param id
   */
  public FireResistance(String name, int id) {
    this.name = name;
    this.id   = id;

    allResistance.put(name,this);
  }

  /**
   * Gets all the fire resistance values. These are LOW, MODERATE, HIGH, UNKNOWN, CONDITIONAL
   * @return array of all fire reisistance values.
   */
  public static FireResistance[] getAllValues() { return allValues; }

  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }

  /**
   * This fire resistance objects id.
   * @return fire resisitance objects id.  This is set in constructor only.
   */
  public int getId() { return id; }

  /**
   * Returns the string name of this fire resistance object.  
   * Choices are "LOW", "MODERATE", "HIGH", "UNKNOWN", "CONDITIONAL"
   */
  public String toString() { return name; }

  /**
   * gets the fire resistance from allResistance hashmap.  Note: the cast to FireResistance object
   * @param name
   * @return
   */
  public static FireResistance get(String name) {
    return (FireResistance) allResistance.get(name.toUpperCase());
  }

  /**
   * while the default is UNKNOWN no fire resistance is allowed to be this, will be caught in FireEventLogic class
   * @param id  key into the swich choices are 0=low, 1=moderate, 2=high, -2 = conditional, and default = UNKNOWN = -1
   * @return
   */
  public static FireResistance get(int id) {
    switch (id) {
      case FireEvent.LOW:      return LOW;
      case FireEvent.MODERATE: return MODERATE;
      case FireEvent.HIGH:     return HIGH;
      case -2:                 return CONDITIONAL;
      default:                 return UNKNOWN;
    }
  }

  /**
   * Reads a fire resistance object from an external source.  The variables read in are the fire resistance name and Id.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    name = (String)in.readObject();
    id   = in.readInt();
  }

  /**
   * Writes the version, name (passed in constructor) and id
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeObject(name);
    out.writeInt(id);
  }

  /**
   * Read resolve methods in OpenSimpplle take in a string name and returns the object corresponding to the name. 
   * In this case the name will reference a particular Fire Reisistance object.  
   * @return
   */
  private Object readResolve() {
    if (name.equals("UNKNOWN")) { name = "UNKNOWN/NA"; }
    return FireResistance.get(name);
  }

}

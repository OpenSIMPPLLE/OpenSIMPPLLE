/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class defines the land types.  It is not used in the current version of OpenSimpplle.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class Landtype implements Comparable, Externalizable {
  private static final long serialVersionUID = 2068463170153588170L;
  static final int  version          = 1;

  private String name;
  
  private static HashMap<String,Landtype> allInstancesHm =
    new HashMap<String,Landtype>();

  private static ArrayList<Landtype> instances = new ArrayList<Landtype>();

  public static void reset() {
    allInstancesHm.clear();
    instances.clear();
  }
/**
 * Primary, default constructor.  It contains a variable for name, which is initialized to null.
 */
  public Landtype() {
    this.name = null;
  }
  /**
   * Overloaded constructor.  
   * @param name
   */
  public Landtype(String name) {
    this.name = name;
    
    updateAllInstances(this);
  }

  public static ArrayList getAllInstances() { return instances; }
/**
 * if land type is not present in hash map, put in land type and put in land type arraylist
 * @param landtype the land type to be added
 */
  public static void updateAllInstances(Landtype landtype) {
    if (allInstancesHm.containsValue(landtype) == false) {
      allInstancesHm.put(landtype.name,landtype);
      instances.add(landtype);
    }
  }
/**
 * Gets the name of this land type.  
 * @return the name of this land type.
 */
  public String getName() {
    return name;
  }
/**
 * Sets the name of this land type
 * @param name the name of this land type
 */
  public void setName(String name) {
    this.name = name;
  }

/**
 * Name of this land type.  The primary distinguisher of land types.  
 */
  public String toString() { return name; }
/**
 * method to see if object passed is a land type object and if it is equal based on name
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof Landtype) {
      if (name == null || obj == null) { return false; }

      return name.equals(((Landtype)obj).name);
    }
    return false;
  }
/**
 * Makes a hash code of this land type based on its string name.  
 */
  public int hashCode() {
    return name.hashCode();
  }
  /**
   * Requisite compareTo method.  Compares to Landtypes by their name, the defining characteristic of land types.  
   */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }
/**
 * Gets the land type from its name.  If it does not exist will not create a new one.  
 * @param name the landtype name
 * @return the Landtype object
 */
  public static Landtype get(String name) {
    return get(name,false);
  }
  /**
   * Gets the land type from its name.  If it does not exist will create a new land type, set its name to the parameter and 
   * put it into the landtype hashmap keyed by the landtype name.  
   * @param name the land type
   * @param ifNoExistCreate true if need to create a new land type
   * @return the landtype.  if it did not exist will create a new one, if it existed returns that one.  
   */
  
  public static Landtype get(String name, boolean ifNoExistCreate) {
    if (name == null || name.length() == 0) { return null; }

    boolean exists = allInstancesHm.containsKey(name.toUpperCase());
    if (!exists && ifNoExistCreate) {
      return new Landtype(name);
    }
    return allInstancesHm.get(name);
  }
  /**
   * Uses the arraylist size() method on the land type arraylist
   * @return
   */
  public static int getRowCount() { return instances.size(); }

  public static Landtype getValueAt(int row) {
    return instances.get(row);
  }
/**
 * Reads the name of this land type from an external source.  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    name = (String)in.readObject();
  }
/**
 * Writes this landtype to an external source by writing its name.  
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(name);
  }
  /**
   * Read resolve is the standard OpenSimpplle method to read in an object and add it to the appropriate memory data structure for OpenSimpplle.  
   * @return the land type object read in.  
   * @throws java.io.ObjectStreamException
   */
  private Object readResolve () throws java.io.ObjectStreamException
  {
    updateAllInstances(this);
    Landtype landtypeObj = Landtype.get(name);
    if (landtypeObj == null) {
      allInstancesHm.put(this.name, this);
      instances.add(this);
      return this;
    }
    return landtypeObj;
  }

}



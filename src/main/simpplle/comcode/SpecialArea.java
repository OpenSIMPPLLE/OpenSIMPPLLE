package simpplle.comcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines a special area.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.RegionalZone
 *
 */
public class SpecialArea implements Comparable, Externalizable {
  static final long serialVersionUID = -4420304668490598908L;
  static final int  version          = 1;

  private String name;

  public static HashMap<Short,SpecialArea> simIdHm =
    new HashMap<Short,SpecialArea>();
  private short simId=-1; // Random Access File ID
  public static short nextSimId=0;
  public short getSimId() {
    if (simId == -1) {
      simId = nextSimId;
      nextSimId++;
      simIdHm.put(simId,this);
    }
    return simId;
  }

  /**
   * Needs to be present for database, does nothing.
   * @param id short
   */
  public void setSimId(short id) {}

  public static SpecialArea lookUp(short simId) { return simIdHm.get(simId); }

  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      SpecialArea sa = (SpecialArea)in.readObject();
      simIdHm.put(id,sa);
      if ( (id+1) > nextSimId) {
        nextSimId = (short)(id+1);
      }
    }
  }
  public static void writeExternalSimIdHm(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeInt(simIdHm.size());
    for (Short id : simIdHm.keySet()) {
      out.writeShort(id);
      SpecialArea sa = simIdHm.get(id);
      out.writeObject(sa);
    }
  }

  private static HashMap<String,SpecialArea> allInstancesHm =
      new HashMap<String,SpecialArea>();
  
  private static ArrayList<SpecialArea> instances = new ArrayList<SpecialArea>();
  
  public static void reset() {
    allInstancesHm.clear();
    instances.clear();
  }
/**
 * Constructor for special area object.  It sets the name for it to null.  
 */
  public SpecialArea() {
    this.name = null;
  }
/**
 * Overloaded constructor for special area object.  It sets the name for to parameter name. 
 * @param name
 */
  public SpecialArea(String name) {
    this.name = name;

    updateAllInstances(this);
  }
/**
 * Gets the arraylist of all special areas. 
 * @return
 */
  public static ArrayList getAllInstances() { return instances; }
/**
 * Updates the arraylist of all special area objects by adding the special area the the all instances hashmap, keyed by string name with 
 * value being the special area object.  Then adds the special area object to the special area arraylist.   
 * @param sa the special area
 */
  public static void updateAllInstances(SpecialArea sa) {
    if (allInstancesHm.containsValue(sa) == false) {
      allInstancesHm.put(sa.name,sa);
      instances.add(sa);
    }
  }
/**
 * Gets the name of the special area.  
 * @return
 */
  public String getName() {
    return name;
  }
/**
 * Sets the name of this special area to parameter name.
 * @param name the new name of this special area.
 */
  public void setName(String name) {
    this.name = name;
  }

/**
 * Returns the name of this special area.  
 */
  public String toString() { return name; }

  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof SpecialArea) {
      if (name == null || obj == null) { return false; }

      return name.equals(((SpecialArea)obj).name);
    }
    return false;
  }
/**
 * Makes a hashcode from this special area's name.  
 */
  public int hashCode() {
    return name.hashCode();
  }
/**
 * Requisite compareTo method.  Compares a parameter special are object to this one, by their names.  
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }
/**
 * Gets the name of a special area.  If it does not exist in the all instances hashmap, it does not make a new one.  
 * @param name
 * @return
 */
  public static SpecialArea get(String name) {
    return get(name,false);
  }
  /**
   * Gets the name of the special area object.  If it does not exist in the all instances hashmap, it  makes a new one.
   * @param name the name of special area
   * @param ifNoExistCreate if true and special area does not exist in all instances hashmap, makes a new special area object with paramenter name.
   * @return the special area object found using its name
   */
  public static SpecialArea get(String name, boolean ifNoExistCreate) {
    if (name == null || name.length() == 0) { return null; }

    boolean exists = allInstancesHm.containsKey(name);
    if (!exists && ifNoExistCreate) {
      return new SpecialArea(name);
    }
    return allInstancesHm.get(name);
  }
  
  public static int getRowCount() { return instances.size(); }

  public static SpecialArea getValueAt(int row) {
    return instances.get(row);
  }
/**
 * Reads from an external source the Special Area object's name.  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    name = (String)in.readObject();
  }
  /**
   * Writes to an external source the special area's by name.  
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(name);
  }
  private Object readResolve () throws java.io.ObjectStreamException
  {
    updateAllInstances(this);
    SpecialArea saObj = SpecialArea.get(name);
    if (saObj == null) {
      allInstancesHm.put(this.name, this);
      instances.add(this);
      return this;
    }
    return saObj;
  }

}



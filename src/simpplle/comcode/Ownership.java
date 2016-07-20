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
import java.util.ArrayList;

/**
 * This class defines Ownership.
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 */

public class Ownership implements Comparable, Externalizable {
  static final long serialVersionUID = 7396793919297681236L;
  static final int  version          = 1;

  private String name;

  public static HashMap<Short,Ownership> simIdHm =
    new HashMap<Short,Ownership>();
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

  public static Ownership lookUp(short simId) { return simIdHm.get(simId); }

  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      Ownership owner = (Ownership)in.readObject();
      simIdHm.put(id,owner);
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
      Ownership owner = simIdHm.get(id);
      out.writeObject(owner);
    }
  }


  private static HashMap<String,Ownership> allInstancesHm =
      new HashMap<String,Ownership>();

  private static ArrayList<Ownership> instances = new ArrayList<Ownership>();

  public static final Ownership NF_WILDERNESS = new Ownership("NF-WILDERNESS");
  public static final Ownership NF_OTHER = new Ownership("NF-OTHER");

/**
 * clears all ownership information from instances and all instances hash map and 
 * adds National forest and national forest wilderness name 
 */
  public static void reset() {
    allInstancesHm.clear();
    instances.clear();

    allInstancesHm.put(NF_WILDERNESS.name,NF_WILDERNESS);
    allInstancesHm.put(NF_OTHER.name,NF_OTHER);
    instances.add(NF_WILDERNESS);
    instances.add(NF_OTHER);
 }
  /**
   * Primary constructor.  Sets name to null. 
   */
  public Ownership() {
    this.name = null;
  }
/**
 * Overloaded constructor.  References primary and sets the name.  
 * @param name
 */
  public Ownership(String name) {
    this.name = name;

    updateAllInstances(this);
  }
/**
 * updates the instances hash map.  
 * @param owner the owner to be added to instances hash map
 */
  public static void updateAllInstances(Ownership owner) {
    if (allInstancesHm.containsValue(owner) == false) {
      allInstancesHm.put(owner.name,owner);
      instances.add(owner);
    }
  }
/**
 * Gets the ownership name choices for this are NF-WILDERNESS and NF-OTHER
 * @return
 */
  public String getName() {
    return name;
  }
/**
 * Sets the name of this ownership object.  Choices are NF-WILDERNESS and NF-OTHER
 * @param name
 */
  public void setName(String name) {
    this.name = name;
  }

/**
 * Returns the name of this ownership object.  Choices are NF-WILDERNESS and NF-OTHER
 */
  public String toString() { return name; }
/**
 * Compares two ownership objects.  First checks that the object passed in parameter is an ownership object, and then compares that object
 * and this ownership object by name, its defining characteristic.
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof Ownership) {
      if (name == null || obj == null) { return false; }

      return name.equals(((Ownership)obj).name);
    }
    return false;
  }
/**
 * The hashcode found for a certain ownership name.
 */
  public int hashCode() {
    return name.hashCode();
  }
/**
 * Required method of Comparable interface.  Compares two Ownership objects based on their name.  Choices of name are NF-WILDERNESS and NF-OTHER.  
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }
/**
 * Gets the name of the ownership object with the if no exist create boolean set to false.  
 * Therefore the ownership object is assumed to exist.  
 * @param name ownership object name.  Choices are NF-WILDERNESS and NF-OTHER
 * @return Ownership object
 */
  public static Ownership get(String name) {
    return get(name,false);
  }
  /**
   * returns Ownership object from all instances hash map
   * @param name
   * @param ifNoExistCreate
   * @return
   */
  public static Ownership get(String name, boolean ifNoExistCreate) {
    if (name == null || name.length() == 0) { return null; }

    boolean exists = allInstancesHm.containsKey(name);
    if (!exists && ifNoExistCreate) {
      return new Ownership(name);
    }
    return allInstancesHm.get(name);
  }

  public static int getRowCount() { return instances.size(); }
/**
 * Used in GUI tables. Uses the row value to index into the Ownership arraylist.  
 * @param row used to index into ownership arraylist
 * @return ownership object at a particular row
 */
  public static Ownership getValueAt(int row) {
    return instances.get(row);
  }
/**
 * Gets the arraylist which contains all the ownership objects.  
 * @return
 */
  public static ArrayList getAllInstances() { return instances; }
/**
 * Reads from an external source this Ownership objects name.
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    name = (String)in.readObject();
  }
  /**
   * Writes to an external source the ownership object.  Choices for this are NF-WILDERNESS and NF-OTHER.
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(name);
  }
  /**
   * Reads from external source the ownership object.  If ownership object is null, places into allInstancesHm this 
   * ownership object's name which keys this object.  It then adds this object to the ownership arraylist.  If there is an ownership object it returns it. 
   * @return
   * @throws java.io.ObjectStreamException
   */
  private Object readResolve () throws java.io.ObjectStreamException
  {
    updateAllInstances(this);
    Ownership ownerObj = Ownership.get(name);
    if (ownerObj == null) {
      allInstancesHm.put(this.name, this);
      instances.add(this);
      return this;
    }
    return ownerObj;
  }

}



/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

/**
 * This class contains methods for Inclusion Rule species, a Simpplle Type.
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.SimpplleType
 */

public class InclusionRuleSpecies extends SimpplleType implements Externalizable {
  static final long serialVersionUID = -3763669945717049504L;
  static final int  version          = 1;

  private String name;

  public static HashMap<Short,InclusionRuleSpecies> simIdHm = new HashMap<Short,InclusionRuleSpecies>();
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
   * @param id simulation id
   */
  public void setSimId(short id) {}

  public static InclusionRuleSpecies lookUpSpecies(short simId) { return simIdHm.get(simId); }
/**
 * Needs to be present for database, does nothing.
 * @param in
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      InclusionRuleSpecies sp = (InclusionRuleSpecies)in.readObject();
      simIdHm.put(id,sp);
      if ( (id+1) > nextSimId) {
        nextSimId = (short)(id+1);
      }
    }
  }
  /**
   * Needs to be present for database, does nothing.
   * @param out object to be written
   * @throws IOException
   */
  public static void writeExternalSimIdHm(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeInt(simIdHm.size());
    for (Short id : simIdHm.keySet()) {
      out.writeShort(id);
      InclusionRuleSpecies sp = simIdHm.get(id);
      out.writeObject(sp);
    }
  }

  private static HashMap<String,InclusionRuleSpecies> allInstancesHm =
      new HashMap<String,InclusionRuleSpecies>();
/**
 * gets the species collection from allInstances hash map
 * @return the values (not keys) of allInstancesHm hash map
 */
  public static Collection getAllSpecies() {
    return allInstancesHm.values();
  }

  /**
   * Primary constructor.  Calls primary constructor and initializes the inclusion rule species name to null   
   */
  public InclusionRuleSpecies() {
    this.name = null;
  }
  /**
   * overloaded constructor.  Sets name of Inclusion rule species to passed in name, calls update All instances
   * @param name name of inclusion rule species
   */
  public InclusionRuleSpecies(String name) {
    this.name = name.toUpperCase();

    updateAllInstances(this);
  }
/**
 * clears instances hash map
 */
  public static void clearAllInstances() {
    allInstancesHm.clear();
  }
  /**
   * if the allinstances hash map does not contain a species, it puts it in there as a value keyed to species name
   * @param species
   */
  public static void updateAllInstances(InclusionRuleSpecies species) {
    if (allInstancesHm.containsValue(species) == false) {
      allInstancesHm.put(species.name,species);
    }
  }
/**
 * Gets the name of this Inclusion Rule Species object.  
 */
  public String toString() { return name; }
/**
 * while this is called count it uses the size function of allInstances hash map
 * @return the size of allInstances hash map
 */
  public static int count() { return allInstancesHm.size(); }
/**
 * equality check of object with inclusion rule species.  checks if it is an Inclusion Rule Species object then checks name and null status 
 * if the objects are equal returns true, false otherwise
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof InclusionRuleSpecies) {
      if (name == null || obj == null) { return false; }

      return name.equals(((InclusionRuleSpecies)obj).name);
    }
    return false;
  }
/**
 * returns a hash code number looked up by Inclusion Rule Species name
 */
  public int hashCode() {
    return name.hashCode();
  }
/**
 * Requisite compareTo method.  Compares to inclusion rules species by their name.  
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }

  public boolean isValid() { return InclusionRuleSpecies.get(name) != null; }

  public static InclusionRuleSpecies get(String name) {
    return get(name,false);
  }
  /**
   * Gets an inclusion rules species by their name.  If the species does not exist and boolean for ifNoExistCreate is true will 
   * create a new inclusion rules speceies.  
   * @param name
   * @param ifNoExistCreate
   * @return
   */
  public static InclusionRuleSpecies get(String name, boolean ifNoExistCreate) {
    boolean exists = allInstancesHm.containsKey(name.toUpperCase());
    if (!exists && ifNoExistCreate) {
      return new InclusionRuleSpecies(name);
    }
    return allInstancesHm.get(name);
  }
  /**
   * Gets akk the inclusion rule species in system knowledge.  
   * @return
   */
  public static InclusionRuleSpecies[] getAllValues() {
    int size = allInstancesHm.size();
    return allInstancesHm.values().toArray(new InclusionRuleSpecies[size]);
  }
  /**
   * reads in an Inclusion Rule Species Object name and a version
   */

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    name = (String)in.readObject();
  }
  /**
   * writes an object, version and name
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(name);
  }
  private Object readResolve () throws java.io.ObjectStreamException
  {
    updateAllInstances(this);
    InclusionRuleSpecies speciesObj = InclusionRuleSpecies.get(name);
    if (speciesObj == null) {
      allInstancesHm.put(this.name, this);
      return this;
    }
    return speciesObj;
  }
/**
 * Gets the inclusion rules species name.  
 * @return
 */
  public String getName() {
    return name;
  }
/**
 * Sets this inclusion rules species object's name. 
 * @param name
 */
  public void setName(String name) {
    this.name = name;
  }


}

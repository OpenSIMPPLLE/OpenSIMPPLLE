/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.*;

/**
 * This class is one of the OpenSIMPPLLE's aquatic types, these are a type of SimpplleType.
 * It was developed as a prototype and is not currently supported.  
 * <p>Aquatic classes were based on Rosgen types assigned to the stream unit.  
 * Choices for these are: A1, A2, A3, A4, B2, B3, B4, C4, D4, E4, G3, G4.  
 * 
 * <p> The other aquatic types are: AQUATIC_ATTRIBUTE, AQUATIC_PROCESS, AQUATIC_TREATMENT, LTA_VS_GROUP
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller
 */

public class AquaticClass extends SimpplleType {
  private String     aquaticClass;
  private static Hashtable allAquaticClassHt = new Hashtable(12);

  public static final AquaticClass A1 = new AquaticClass("A1",true);
  public static final AquaticClass A2 = new AquaticClass("A2",true);
  public static final AquaticClass A3 = new AquaticClass("A3",true);
  public static final AquaticClass A4 = new AquaticClass("A4",true);

  public static final AquaticClass B2 = new AquaticClass("B2",true);
  public static final AquaticClass B3 = new AquaticClass("B3",true);
  public static final AquaticClass B4 = new AquaticClass("B4",true);

  public static final AquaticClass C4 = new AquaticClass("C4",true);

  public static final AquaticClass D4 = new AquaticClass("D4",true);

  public static final AquaticClass E4 = new AquaticClass("E4",true);

  public static final AquaticClass G3 = new AquaticClass("G3",true);
  public static final AquaticClass G4 = new AquaticClass("G4",true);

  /**
   * Primary constructor for AquaticClass.
   */
  public AquaticClass() {
  }
  /**
   * Overloaded constructor of Aquatic Class which calls default constructor and adds parameters of class name and boolean to distinguish validness.  
   * @param aquaticClass name of class
   * @param valid if true the aquatic class name will be put into aquatic class hastable (key = string name of class, value of class
   */
  public AquaticClass(String aquaticClass, boolean valid) {
    this.aquaticClass = aquaticClass.toUpperCase();
    if (valid) {
      allAquaticClassHt.put(this.aquaticClass,this);
    }
  }
  /**
   * Overloaded constructor of AquaticClass to make an aquatic class by name but not put in allAquaticClassHT 
   * @param aquaticClass name of aquatic class. 
   */
  public AquaticClass(String aquaticClass) {
    this(aquaticClass,false);
  }

  /**
   * Uses the string name of aquatic class to create a hashcode. 
   */
  public int hashCode() {
    return aquaticClass.hashCode();
  }
  
  /**
   * Method to check if parameter AquaticClass object is equal to the current object, by name, example "E4" = "E4"   
   * @return false if name is null or not aquatic class object, true if name equals passed in AquaticClass object
   */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof AquaticClass) {
      if (aquaticClass == null || obj == null) { return false; }

      return aquaticClass.equals(((AquaticClass)obj).aquaticClass);
    }
    return false;
  }
  /**
   * Requisite compareTo method.  Compares this aquatic class to parameter aquatic class by their string names e.g. "E4" = "E4" 
   *  
   */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return aquaticClass.compareTo(o.toString());
  }
  /**
   * The name of aquatic class.  e.g. "E4" 
   */
  public String toString() { return aquaticClass; }

  /**
   * Counts all the aquatic classes, calling hash table size
   * @return count of all the aquatic classes
   */
  public static int count() { return allAquaticClassHt.size(); }
/**
 * Checks if an aquatic class is in the hash table of all aquatic classes.
 * @return true if valid aquatic class
 */
  public boolean isValid() { return AquaticClass.get(aquaticClass) != null; }
  /**
   * Gets an aquatic class from the the hash table of all aquatic classes.
   * @param aquaticClassStr name of aquatic class sought
   * @return AquaticClass object 
   */
  public static AquaticClass get(String aquaticClassStr) {
    return ( (AquaticClass)allAquaticClassHt.get(aquaticClassStr.toUpperCase()) );
  }
/**
 * sets an aquatic class from string parameter 
 * choices are A1, A2, A3, A4, B2, B3, B4, C4, D4, E4, G3, G4
 * @param aquaticClass
 */
  public void setAquaticClass(String aquaticClass) {
    this.aquaticClass = aquaticClass;
  }
/**
 * 
 * @return string literal of aquatic class choices are A1, A2, A3, A4, B2, B3, B4, C4, D4, E4, G3, G4
 */
  public String getAquaticClass() {
    return aquaticClass;
  }
}





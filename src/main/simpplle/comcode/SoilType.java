/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.HashMap;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.io.File;


/**
 * This class defines soil type.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class SoilType implements Externalizable, Comparable  {
  static final long serialVersionUID = 7102140875501814402L;
  static final int  version          = 1;

/**
 * This is used to read in soil types from a text file.  Uses the scanner object to read in the file.  
 * @param file
 */
  public static void parseTextFile(File file) {
    try {
      Scanner scanner = new Scanner(file);
//      scanner.useDelimiter(Pattern.compile(","));
      while (scanner.hasNext()) {
        String str = scanner.next().trim();
        new SoilType(str);
      }
      scanner.close();
    }
    catch (IOException ioe) {
      System.out.println("Problem with Soil Type file.");
    }

  }

  private String soilType;
  private static HashMap<String,SoilType> soilTypeHm = new HashMap<String,SoilType>();
  private static ArrayList<SoilType> soilTypeList = new ArrayList<SoilType>();

  /**
   * Primary constructor.  
   */
  public SoilType() {}

  /**
   * Overloaded constructor which sets this soil type's object name, and puts the soil type into a hash map.  This is keyed 
   * by the soil type name (in upper case), the value is the corresponding soil type object.  
   * @param soilType 
   */
  public SoilType(String soilType) {
    setSoilType(soilType);
    soilTypeHm.put(soilType.toUpperCase(),this);
    soilTypeList.add(this);
  }
/**
 * Creates a new soil type from the passed in soil type name.  
 * @param soilType
 */
  public static void add(String soilType) {
    new SoilType(soilType);
  }
  /**
   * Uses the size() method of the soil type arraylist.  Since this contains all the soil types, it counts the rows.  
   * @return
   */
  public static int getRowCount() { return soilTypeList.size(); }
/**
 * Gets the soil type by using the row to index into the soil type arraylist.  
 * @param row the row number to be used as index into soil type arraylist
 * @return
 */
  public static SoilType getValueAt(int row) {
    return soilTypeList.get(row);
  }
/**
 * Method to check if parameter object is equal to this soil type object.  First checks to make sure the object 
 * is a soil type object, then uses the string comparison .equals method to compare the names of the soil types.  
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof SoilType) {
      if (soilType == null || obj == null) { return false; }

      return soilType.equals(((SoilType)obj).soilType);
    }
    return false;
  }
  /**
   * Gets the soil type hash code.  
   */
  public int hashCode() {
    return soilType.hashCode();
  }
  /**
   * Requisite compareTo method.  Compares two soil types by their names (which is found by the toString).  
   */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return soilType.compareTo(o.toString());
  }
/**
 * Gets a particular soil type object by its name.  WIll not create a new one if one does not exist.  
 * @param name name of the soil type object being sought.  (will be found in soil type hash map keyed by soil type name).
 * @return the soil type which is the value in the soil type hashmap keyed by soil type name.  
 */
  public static SoilType get(String name) {
    return get(name,false);
  }
/**
 * Gets a particular soil type object by its name.  Checks if there is a matching soil type.  If not it creates a new soil type.  
 * @param name
 * @param create
 * @return
 */
  public static SoilType get(String name, boolean create) {
    SoilType soil = soilTypeHm.get(name.toUpperCase());
    if (soil == null) { return new SoilType(name); }

    return soil;
  }
  /**
   * Sets this soil type objects name.  
   * @param soilType the soil type objects name, which is a string.  
   */
  public void setSoilType(String soilType) {
    this.soilType = soilType;
  }
/**
 * Gets the name of the soil type.
 * @return the name of the soil type.
 */
  public String getSoilType() {
    return soilType;
  }
/**
 * Returns the name of the soil type. 
 */
  public String toString() { return soilType.toString(); }
/**
 * Writes the soil type object to an external source.  
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(soilType);
  }
/**
 * Reads from an external source the information defining this soil type object.  
 */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int version = in.readInt();

    soilType = (String)in.readObject();
  }
/**
 * Reads in a soil type .  If it does not exist it creates a new one.  
 * @return
 * @throws java.io.ObjectStreamException
 */
  private Object readResolve () throws java.io.ObjectStreamException
  {
    // If we add other fields this will need to be modified.
    return get(soilType,true);
  }
}



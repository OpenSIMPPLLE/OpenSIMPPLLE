/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.HashMap;

/**
 * This class has methods pertaining to Density, a Simpplle type.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class Density extends SimpplleType implements Externalizable {

  static final long serialVersionUID = 6515843635790946668L;
  static final int  version          = 2;

  public static final int CODE_COL       = 0;
  public static final int MIN_CANOPY_COL = 1;
  public static final int MAX_CANOPY_COL = 2;
  public static final int COLUMN_COUNT   = 3;

  public static final Density UNKNOWN = new Density("UNKNOWN", 0);
  public static final Density NA      = new Density("NA", 1); // Western Great Plains Steppe
  public static final Density ONE     = new Density("1", 1);
  public static final Density TWO     = new Density("2", 2);
  public static final Density THREE   = new Density("3", 3);
  public static final Density FOUR    = new Density("4", 4);
  public static final Density W       = new Density("W", 1); // Woodland
  public static final Density O       = new Density("O", 2); // Open
  public static final Density C       = new Density("C", 3); // Closed

  private String name;
  private int    value;
  private Range  pctCanopy;

  public static HashMap<Short,Density> simIdHm = new HashMap<>();
  private short simId = -1; // Random Access File ID
  public static short nextSimId = 0;

  /**
   * Creates a new density.
   *
   * @param name a case-insensitive name
   * @param value a numeric value for comparing densities, where lower is less dense
   */
  public Density(String name, int value) {

    this.name = name.toUpperCase();
    this.value = value;

    updateAllData(this, DENSITY);

  }

  /**
   * Creates a new density with a value of zero.
   *
   * @param name a case-insensitive name
   */
  public Density(String name) {
    this(name, 0);
  }

  /**
   * Creates a new density with no name and a value of zero.
   */
  public Density() {
    this("", 0);
  }

  public static void resetSimIds() {
    nextSimId = 0;
    for (Density density : simIdHm.values()) {
      density.simId = -1;
    }
    simIdHm.clear();
  }

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

  public static Density lookUpDensity(short simId) { return simIdHm.get(simId); }

  public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    int size = in.readInt();
    for (int i=0; i<size; i++) {
      short id = in.readShort();
      Density density = (Density)readExternalSimple(in,SimpplleType.DENSITY);
      simIdHm.put(id,density);
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
      Density density = simIdHm.get(id);
      density.writeExternalSimple(out);
    }
  }

  /**
   * Initializes the absolute canopy coverage ranges of the predefined densities based on the
   * current zone. Gila and Southwest Utah are ignored.
   */
  public static void initPercentCanopy() {

    RegionalZone zone = Simpplle.getCurrentZone();

    if (zone instanceof Gila) {

      // Warning: pctCanopy not initialized

      // 1 = non forest (<10%)
      // 2 = Low        (11 - 49%)
      // 3 = Medium     (50 - 69%)
      // 4 = High       (70 - 100%)

    } else if (zone instanceof SouthwestUtah) {

      // Warning: pctCanopy not initialized

      // 1 = (<10%)
      // 2 = (41-70%)
      // 3 = (>70%)

    } else if (zone instanceof ColoradoPlateau) {

      ONE.pctCanopy   = new Range(  0,  10 );
      TWO.pctCanopy   = new Range( 11,  40 );
      THREE.pctCanopy = new Range( 41,  70 );
      FOUR.pctCanopy  = new Range( 71, 100 );

    } else {

      ONE.pctCanopy   = new Range(  0,  10 );
      TWO.pctCanopy   = new Range( 11,  49 );
      THREE.pctCanopy = new Range( 50,  69 );
      FOUR.pctCanopy  = new Range( 70, 100 );

    }
  }

  /**
   * Checks if a lower and upper bound are within the pct canopy range which means >=lower<=pct canopy.  
   * @param lower lower bound
   * @param upper upper bound
   * @return true if percent canopy is in the range lower upper inclusive
   */
  public boolean inPctCanopyRange(int lower, int upper) {
    return pctCanopy.withinRange(lower,upper);
  }

  public static Density getFromPercentCanopy(int pctCanopy) {
    if (Density.ONE.pctCanopy.inRange(pctCanopy)) {
      return Density.ONE;
    } else if (Density.TWO.pctCanopy.inRange(pctCanopy)) {
      return Density.TWO;
    } else if (Density.THREE.pctCanopy.inRange(pctCanopy)) {
      return Density.THREE;
    } else if (Density.FOUR.pctCanopy.inRange(pctCanopy)) {
      return Density.FOUR;
    } else {
      return Density.ONE;
    }
  }

  /**
   * Density name. Choices for name are "1", "2","3","4","W" (woodland), "O" (Open),  "C" (Closed), "NA"
   */
  public String toString() { return name; }

  /**
   * Gets the value choices for this are  1,2,3,4
   * Corresponding names and values are  for name are "1"-1, "2"-2,"3"-3,"4"-3,"W"-1 (woodland), "O"-2 (Open),  "C"-3 (Closed), "NA" -1
   * @return
   */
  public int getValue() { return value; }

  /**
   * Method to look up a density by name and compare to this density object.
   * @param name the name of density object
   * @return true if the density sought equals this one.
   */
  public boolean lookupEquals(String name) {
    return equals(get(name));
  }

  /**
   * Compares this Density with parameter Density object.  Returns true if they have the same density variable, which is essentially the name.
   *  Choices for density variable  are "1", "2","3","4","W" (woodland), "O" (Open),  "C" (Closed), "NA"
   */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof Density) {
      if (name == null || obj == null) { return false; }

      return name.equals(((Density)obj).name);
    }
    return false;
  }

  /**
   * Makes a hashcode from the string of density variable.
   */
  public int hashCode() {
    return name.hashCode();
  }

  /**
   * Requisite compareTo method.  Compares this Density object with parameter object by density variable.
   *  Choices for density variable are are "1", "2","3","4","W" (woodland), "O" (Open),  "C" (Closed), "NA"
   */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return name.compareTo(o.toString());
  }

  /**
   * Checks if a density is valid.
   * @return
   */
  public boolean isValid() { return Density.get(name) != null; }

  /**
   * Returns a predefined 1, 2, 3, or 4 density based on the associated value. Otherwise the
   * a density is returned with a name that matches the passed in value.
   *
   * @param value a numeric value
   * @return an existing density, or null
   */
  public static Density get(int value) {
    switch (value) {
      case 1:  return Density.ONE;
      case 2:  return Density.TWO;
      case 3:  return Density.THREE;
      case 4:  return Density.FOUR;
      default: return get(Integer.toString(value));
    }
  }

  /**
   * Returns an existing density with the provided name.
   *
   * @param name a case-insensitive name
   * @return an existing density, or null
   */
  public static Density get(String name) {
    return (Density)allDensityHm.get(name.toUpperCase());
  }

  /**
   * Returns an existing density or creates a missing density with the provided name.
   *
   * @param name a case-insensitive name
   * @return a new or existing density
   */
  public static Density getOrCreate(String name) {
    Density density = get(name);
    if (density == null) {
      return new Density(name.toUpperCase());
    } else {
      return density;
    }
  }

  /**
   * Calculates the next lower Density object.
   * @param density the Density object to find a lower one than
   * @return
   */
  public static Density getLowerDensity(Density density) {
    if (density == Density.FOUR) {
      return Density.THREE;
    } else if (density == Density.THREE) {
      return Density.TWO;
    } else if (density == Density.TWO) {
      return Density.ONE;
    } else if (density == Density.C) { // Not sure of the validity
      return Density.O;
    } else if (density == Density.O) { // Not sure of the validity
      return Density.W;
    }

    return null;
  }

  /**
   * Gets the next higher Density object 
   * @param density the Density object to fine a higher one than.  
   * @return next higher Density
   */
  public static Density getHigherDensity(Density density) {
    if (density == Density.ONE) {
      return Density.TWO;
    } else if (density == Density.TWO) {
      return Density.THREE;
    } else if (density == Density.THREE) {
      return Density.FOUR;
    } else if (density == Density.W) { // Not sure of the validity
      return Density.O;
    } else if (density == Density.O) { // Not sure of the validity
      return Density.C;
    }

    return null;
  }

  /**
   * Gets the GIS print name for each Density object.
   * @return name of Density object ONE, TWO, THREE, or FOUR
   */
  public String getGisPrintName() {

    if      (this == Density.ONE)   return "ONE";
    else if (this == Density.TWO)   return "TWO";
    else if (this == Density.THREE) return "THREE";
    else if (this == Density.FOUR)  return "FOUR";
    else                            return toString();

  }

  /**
   * Reads from an external source the density variable, int density value, pct canopy lower and upper range which then creates a Range object.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    name = (String)in.readObject();
    value   = in.readInt();
    if (version > 1) {
      if (in.readBoolean()) {
        int lower = in.readInt();
        int upper = in.readInt();
        pctCanopy = new Range(lower, upper);
      }
    }
  }

  /**
   * Writes to an external source the Density object variables: density, value, pct canopy lower and upper ranges
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeObject(name);
    out.writeInt(value);
    out.writeBoolean(pctCanopy != null);
    if (pctCanopy != null) {
      out.writeInt(pctCanopy.getLower());
      out.writeInt(pctCanopy.getUpper());
    }
  }

  /**
   * Replaces the density read from the stream if one with the same name already exists.
   *
   * @return an existing density or the object read from the stream
   * @throws java.io.ObjectStreamException if there is a stream exception
   */
  private Object readResolve () throws java.io.ObjectStreamException {
    Density density = get(name);
    if (density == null) {
      return this;
    } else {
      return density;
    }
  }

  // *** JTable section ***
  // **********************
  /**
   * Gets the Density Column data at the Code_col
   */
  public Object getColumnData(int col) {
    switch (col) {
      case CODE_COL: return this;
      default:       return null;
    }
  }

  public void setColumnData(Object value, int col) {
    switch (col) {
      default: return;
    }
//    SystemKnowledge.markChanged(SystemKnowledge.DENSITY);
  }

  /**
   * If  Code_col returns Density
   * @param col
   * @return
   */
  public static String getColumnName(int col) {
    switch (col) {
      case CODE_COL: return "Density";
      default:       return "";
    }
  }
}


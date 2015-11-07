package simpplle.comcode.element;

import simpplle.comcode.Area;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SoilType;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p> This class describes an Existing Land Unit (ELU).  This along with EVU and EAU are the Natural Elements in OpenSimpplle.  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class ExistingLandUnit extends NaturalElement implements Externalizable {
  static final long serialVersionUID = 7582130807609896827L;
  static final int  version          = 4;

  private ArrayList<simpplle.comcode.element.Evu> assocVegUnits = new ArrayList<simpplle.comcode.element.Evu>();  // Evu Class instances
  private SoilType soilType;
  private String    parentMaterial;
  private String    landform;
  private String    depth;
  private double latitude;
  private double longitude;
/**
 * Constructor for ELU.  Sets the latitude to 0.0.
 */
  public ExistingLandUnit() {
    latitude = 0.0;
  }
/**
 * Overloaded constructor for ELU.  Creates a ELU object by ELU Id.
 * @param id the Elu Id.
 */
  public ExistingLandUnit(int id) {
    super(id);
  }
/**
 * Gets the soil type for this Elu
 * @return the soil type
 */
  public SoilType getSoilType() { return soilType; }
  /**
   * Sets the soil type for this Elu.
   * @param soilType
   */
  public void setSoilType(SoilType soilType) { this.soilType = soilType; }

  public String getParentMaterial() { return parentMaterial; }
  public void setParentMaterial(String value) { parentMaterial = value; }
/**
 * Gets the landform for this Elu.  
 * @return the landform for this ELU
 */
  public String getLandform() { return landform; }
  /**
   * Sets the landform for this elu
   * @param value the landform to be set. 
   */
  public void setLandform(String value) { landform = value; }
/**
 * Gets the Evu's associated with this Elu.  
 * @return arraylist of all Evu's associated with this Elu.  
 */
  public ArrayList<simpplle.comcode.element.Evu> getAssociatedVegUnits() { return assocVegUnits; }
  /**
   * Adds an Existing vegetative unit to the Evu arraylist of all Evu's associated with this Elu.  
   * @param evu the Evu to be added to arraylist of all Existing vegetative units associated with the Existing Land Unit.   
   */
  public void addAssociatedVegUnit(simpplle.comcode.element.Evu evu)  {
    if (assocVegUnits == null) { assocVegUnits = new ArrayList<simpplle.comcode.element.Evu>(); }
    if (assocVegUnits.contains(evu) == false) {
      assocVegUnits.add(evu);
    }
  }
/**
 * Sets the depth for this Elu
 * @param depth
 */
  public void setDepth(String depth) { this.depth = depth; }
/**
 * Sets the latitude of this Existing Land Unit. 
 * @param latitude the latitude of this Elu.
 */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }
/**
 * Sets the longitude of this Existing Land Unit. 
 * @param longitude the longitude of this Elu.
 */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
/**
 * Gets the depth of this Elu
 * @return the depth of the Elu
 */
  public String getDepth() { return depth; }
/**
 * Gets the latitude of this Elu
 * @return the latitude of this Existing Land Unit
 */
  public double getLatitude() {
    return latitude;
  }
  /**
   * Gets the longitude of this Elu
   * @return the longitude of this Existing Land Unit
   */
  public double getLongitude() {
    return longitude;
  }
/**
 * Checks that this Elu is a valid Elu.  This is usually checked by Id.  In this case however it returns true by default.  
 */
  public boolean isValid() { return true; }

  public void exportNeighbors(PrintWriter fout) {
    if (neighbors == null) { return; }

    // unit, adj, elev, downwind
    for (int i=0; i<neighbors.size(); i++) {
      fout.print(getId());
      fout.print(COMMA);
      fout.print(((ExistingLandUnit)neighbors.get(i)).getId());
      fout.print(COMMA);
      fout.print(getElevation());
      fout.print(COMMA);
      fout.print("''");
      fout.println();
    }
  }
  /**
   * Export the Evu objects that are neighbors of this Elu.  
   * @param fout
   */
  public void exportNeighborsVegetation(PrintWriter fout) {
    if (assocVegUnits == null) { return; }

    for (int i=0; i<assocVegUnits.size(); i++) {
      fout.print(assocVegUnits.get(i).getId());
      fout.print(COMMA);
      fout.print(getId());
      fout.println();
    }
  }
  /**
   * method to print attributes.  they are put in file in order: slink(id), acres, soil type, landform, aspec, slope, parent material, depth, latitude, longitude
   * @param fout - delimited  by comma
   */
  public void exportAttributes(PrintWriter fout) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);

    fout.print(getId());
    fout.print(COMMA);
    fout.print(nf.format(Area.getFloatAcres(getAcres())));
    fout.print(COMMA);
    fout.print(getSoilType());
    fout.print(COMMA);
    fout.print(getLandform());
    fout.print(COMMA);
    fout.print(getAspectName());
    fout.print(COMMA);
    fout.print(nf.format(getSlope()));
    fout.print(COMMA);
    fout.print(getParentMaterial());
    fout.print(COMMA);
    fout.print(getDepth());
    fout.print(COMMA);
    fout.print(getLatitude());
    fout.print(COMMA);
    fout.print(getLongitude());
    fout.println();
  }

//  private ArrayList assocVegUnits;  // Evu Class instances
//  private SoilType  soilType;
//  private String    parentMaterial;
//  private String    landform;
//  private float     depth;
/**
 * method to write version, associated vegetative unit arraylist size, associated vegetative unit id of all in avu arraylist, soil type, parent material, landform, depth, 
 * latitude, longitude 
 * @throws IOException - caught in gui
 * 
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(version);

    out.writeInt(assocVegUnits.size());
    for (int i=0; i<assocVegUnits.size(); i++) {
      out.writeInt(assocVegUnits.get(i).getId());
    }

    out.writeObject(soilType);
    out.writeObject(parentMaterial);
    out.writeObject(landform);
    out.writeObject(depth);
    out.writeDouble(latitude);
    out.writeDouble(longitude);
  }
/**
 * method to read objects, clears associated vegetative unit arraylist, adds all evu read in and adds to associated vegetative unit arraylist, 
 * then reads soil type, parent material, landform, depth (converted to string from double) latitude, longitude 
 */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(in);

    int version = in.readInt();

    Area area = Simpplle.getCurrentArea();
    int size = in.readInt();
    assocVegUnits.clear();
    for (int i=0; i<size; i++) {
      int id = in.readInt();
      simpplle.comcode.element.Evu evu = area.getEvu(id);
      if (evu != null) {
        assocVegUnits.add(evu);
      }
    }

    soilType = (SoilType)in.readObject();
    parentMaterial = (String)in.readObject();
    landform = (String)in.readObject();
    if (version > 1) {
      depth = (String)in.readObject();
    }
    else {
      depth = Float.toString(in.readFloat());
    }

    if (version > 2) {
      latitude = (double)in.readDouble();
    }
    if (version > 3) {
      longitude = (double)in.readDouble();
    }
  }

  protected void copyFrom(ExistingLandUnit copyUnit) {
    super.copyFrom(copyUnit);
    assocVegUnits  = new ArrayList<simpplle.comcode.element.Evu>(copyUnit.assocVegUnits);
    soilType       = copyUnit.soilType;
    parentMaterial = copyUnit.parentMaterial;
    landform       = copyUnit.landform;
    depth          = copyUnit.depth;
    latitude       = copyUnit.latitude;
  }

  private Object readResolve () throws java.io.ObjectStreamException
  {
    Area area = Simpplle.getCurrentArea();

    ExistingLandUnit unit = area.getElu(id);
    if (unit == null) { return this; }

    unit.copyFrom(this);
    return unit;
  }
/**
 * calculates Abreviated Notice of Resource Area Delineation  
 * @return the anrad calculation 
 * @todo - correct spelling of annrad and fix all dependencies
 */
  public double getANNRAD() {
    double latitudeRadians = Math.toRadians(latitude);
    double slopeRadians    = Math.toRadians(slope);
    double foldedAspect    = 180 - (aspect - 180);
    double radFoldedAspect = Math.toRadians(foldedAspect);

    double cosSlope    = Math.cos(slopeRadians);
    double sinSlope    = Math.sin(slopeRadians);
    double cosLatitude = Math.cos(latitudeRadians);
    double sinLatitude = Math.sin(latitudeRadians);
    double cosAspect   = Math.cos(radFoldedAspect);
    double sinAspect   = Math.sin(radFoldedAspect);

    double annrad = 1.467 +
                    (1.582 * cosLatitude * cosSlope) -
                    (1 * cosAspect * sinSlope * sinLatitude) -
                    (0.262 * sinLatitude * sinSlope) +
                    (0.607 * sinAspect * sinSlope);

    return annrad;
  }
}




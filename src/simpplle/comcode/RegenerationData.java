/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * <p>This class contains methods for a Regeneration Data, a type of Logic Data.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 */
public class RegenerationData extends LogicData implements Externalizable, Comparable<RegenerationData> {
  static final long serialVersionUID = 1464573527576887979L;
  static final int  version          = 2;

  public static final int SPECIES_CODE_COL = BaseLogic.LAST_COL+1;
  protected static final int LAST_COL = SPECIES_CODE_COL;

  protected Species              species = null;
  protected HabitatTypeGroupType ecoGroup = null;
/**
 * Primary constructor for Regeneration Data.  Initializes the habitat type group to ANY
 */
  public RegenerationData() {
    this.ecoGroup = HabitatTypeGroupType.ANY;
  }
  /**
   * Overloaded constructor for Regeneration Data.  References primary constructor and sets the habitat type group to passed argument.  
   * @param theGroup
   */
  public RegenerationData(HabitatTypeGroupType theGroup) {
    this();
    if (theGroup != null) {
      this.ecoGroup = theGroup;
    }
  }
/**
 * Overloaded constructor for Regeneration Data. References primary constructor and sets the species to passed argument.  
 * @param species
 */
  public RegenerationData(Species species) {
    this();
    this.species = species;
  }
  public AbstractLogicData duplicate() {return null;}
  /**
   * Makes a duplicate of this Regeneration Data Object.  
   */
  public void duplicate(LogicData logicData) {
    super.duplicate(logicData);

    ((RegenerationData)logicData).species  = species;
    ((RegenerationData)logicData).ecoGroup = ecoGroup;
  }
/**
 * Gets the species for this regeneration data. 
 * @return
 */
  public Species getSpecies() {
    return species;
  }
  /**
   * Sets the species for this regeneration data.  
   * @param s species
   */
  public void setSpecies(Species s) {
    this.species = s;
  }
  /**
   * sets the habitat type group for this particular regeneration data.   
   * @param ecoGroup
   */
  public void setEcoGroup(HabitatTypeGroupType ecoGroup) {
    this.ecoGroup = ecoGroup;
  }
/**
 * Gets the habitat type group of this particular regeneration data.  
 * @return
 */
  public HabitatTypeGroupType getEcoGroup() {
    return ecoGroup;
  }
/**
 * Used in Gui tables this gets the value at a particular column id.  If it is in the SPECIES_CODE_COL returns the 
 * species object which will have name, description and associated lifeform.  
 */
  public Object getValueAt(int col) {
    switch (col) {
      case SPECIES_CODE_COL:       return species;
      default:                     return super.getValueAt(col);
    }
  }

  public void setValueAt(int col, Object value) {
    switch (col) {
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * gets the string literal of a particular column.  In this class Species is specified, if not defaults to Base Logic
 * @param column
 * @return column name
 */
  public static String getColumnName(int column) {
    switch (column) {
      case SPECIES_CODE_COL:       return "Species";
      default:                     return BaseLogic.getColumnName(column);
    }
  }
/**
 * gets a column name in column code form based on base logic and string name.  
 * @param logic 
 * @param name 
 * @return the species code form of a column, if name does not equal species returns -1
 */
  public static int getColumnNumFromName(BaseLogic logic, String name) {
    if (name.equalsIgnoreCase("Species")) {
      return SPECIES_CODE_COL;
    }
    else {
      return -1;
    }
  }
/**
 * compares evu's based on the species in existing vegetative unit.  
 * is match is used to match logic data with logic classes.
 * @return true if a match
 */
  public boolean isMatch(Evu evu) {
    if ((Species)evu.getState(SimpplleType.SPECIES) == species) {
      return super.isMatch(evu);
    }
    return false;
  }
  /**
   * Overloaded isMatch compares evu's based on the species in existing vegetative unit, vegetative state, time step, 
   * lifeform and the state vegetative species.  
   * isMatch is used to match logic data with logic classes.
   */
  public boolean isMatch(Evu evu, VegSimStateData state, int tStep, Lifeform lifeform) {
    if (state.getVeg().getSpecies() == species) {
      return super.isMatch(evu,state,tStep,lifeform);
    }
    return false;
  }
  /**
   * Overloaded is match which compares and evu based on a specified time step and lifeform and the species vegetative type .  
   * isMatch is used to match logic data with corresponding logic classes.
   */
  public boolean isMatch(Evu evu, Integer tStep, Lifeform lifeform) {
    if (evu.getState(tStep,lifeform).getVegType().getSpecies() == species) {
      return super.isMatch(evu,tStep,lifeform);
    }
    return false;
  }
/**
 * writes to an external location.  Object outputs specified in this class are, in order: version, species, and ecogroup
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    species.writeExternalSimple(out);
    ecoGroup.writeExternalSimple(out);
  }
  public void readExternal(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    int version = in.readInt();
    if (version > 1) {
      super.readExternal(in);
    }
    species = (Species)SimpplleType.readExternalSimple(in,SimpplleType.SPECIES);
    ecoGroup = (HabitatTypeGroupType)SimpplleType.readExternalSimple(in,SimpplleType.HTGRP);
  }

  /**
   * Compares this Regeneration Data object with parameter Regeneration Data object.  Basically if the species is the same will return 0.
   *
   * @param value RegenerationData, the value to be compared.
   * @return a negative integer, zero, or a positive integer as this object is
   *   less than, equal to, or greater than the specified object.
   * TODO Implement this java.lang.Comparable method
   */
  public int compareTo(RegenerationData value) {
    return species.compareTo(value.species);
  }
}

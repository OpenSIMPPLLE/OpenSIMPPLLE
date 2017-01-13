/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Externalizable;

/**
 * This class contains methods to handle Invasive Species LogicData, a type of Logic Data.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class InvasiveSpeciesLogicData extends LogicData implements Externalizable {
  static final long serialVersionUID = -7387163082924503825L;
  static final int  version          = 1;

  protected ArrayList<Species> invasiveSpeciesList;
  protected String             invasiveSpeciesDesc;
  protected boolean            defaultInvasiveSpeciesDesc;

  protected Species            repSpecies;

  protected ArrayList<SoilType> soilTypeList;
  protected String              soilTypeDesc;
  protected boolean             defaultSoilTypeDesc;

  public enum VegFunctionalGroup { RESPROUTING, NON_RESPROUTING };

  public static final VegFunctionalGroup RESPROUTING = VegFunctionalGroup.RESPROUTING;
  public static final VegFunctionalGroup NON_RESPROUTING = VegFunctionalGroup.NON_RESPROUTING;

  protected VegFunctionalGroup vegFuncGroup;
  protected long             distToSeed;
  protected int              startValue;

  protected int prob;
/**
 * Constructor for Invasive Species Logic Data. Initializes the system knowledge for invasive species logic.  
 */
  public InvasiveSpeciesLogicData() {
    invasiveSpeciesList        = new ArrayList<Species>();
    invasiveSpeciesDesc        = "[]";
    defaultInvasiveSpeciesDesc = true;

    soilTypeList        = new ArrayList<SoilType>();
    soilTypeDesc        = "[]";
    defaultSoilTypeDesc = true;

    vegFuncGroup = RESPROUTING;
    distToSeed   = 0;
    startValue   = 1;

    prob            = 0;
    sysKnowKind     = SystemKnowledge.Kinds.INVASIVE_SPECIES_LOGIC;
  }

  public AbstractLogicData duplicate() {
    InvasiveSpeciesLogicData logicData = new InvasiveSpeciesLogicData();
    super.duplicate(logicData);

    logicData.vegFuncGroup = vegFuncGroup;
    logicData.distToSeed   = distToSeed;
    logicData.startValue   = startValue;

    return logicData;
  }
/**
 * Checks whether parameter species is in the invasive species list.  
 * @param species the species to be checked
 * @return true if species is in the invasive species list
 */
  public boolean isMemberInvasiveSpecies(Species species) {
    return invasiveSpeciesList.contains(species);
  }
/**
 * Adds and invasive species to the invasive species list if it is not already in there.  Marks system knowledge changed. 
 * @param species
 */
  public void addInvasiveSpecies(Species species) {
    if (isMemberInvasiveSpecies(species) == false) {
      invasiveSpeciesList.add(species);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes an invasive species from the invasive species arraylist if the arraylist contains that species.  Then marks system knowledge changed.
   * @param species the invasive species to be removed
   */
  public void removeInvasiveSpecies(Species species) {
    if (invasiveSpeciesList.contains(species)) {
      invasiveSpeciesList.remove(species);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  public int getInvasiveSpeciesCount() { return invasiveSpeciesList.size(); }
/**
 * Get the invasive species arraylist.
 * @return invasice species arraylist.  
 */
  public ArrayList<Species> getInvasiveSpeciesList() { return invasiveSpeciesList; }
/**
 * 
 * @return
 */
  public Species getRepSpecies() {
    return repSpecies;
  }
/**
 * Checks to see if soil type, vegetative functional group (lifeforms, resprouting state, and adjacent evu at a given time step), and distance to seed 
 * source are a match.
 * @param evu
 * @param invasiveUnits
 * @return
 */
  public boolean isMatch(Evu evu,ArrayList<Evu> invasiveUnits) {
    if (super.isMatch(evu) == false) { return false; }

    { // *** Soil Type ***
      boolean aMatch = false;
      if (soilTypeList != null && soilTypeList.size() > 0) {
        ArrayList<ExistingLandUnit> elus = evu.getAssociatedLandUnits();
        if (elus == null || elus.size() == 0) { return false; }

        for (int i = 0; i < elus.size(); i++) {
            SoilType soilType = elus.get(i).getSoilType();
          if (soilTypeList.contains(soilType) == false) {
            aMatch = true;
            break;
          }
        }
        if (!aMatch) { return false; }
      }
    } // *****************

    { // *** Vegetation Functional Group ***
      VegFunctionalGroup funcGroup = null;
      int cStep = Simulation.getCurrentTimeStep();

      Lifeform[] lifeforms = Lifeform.getAllValues();
      for (int l=0; l<lifeforms.length; l++) {
        HabitatTypeGroupType ecoGroup = evu.getHabitatTypeGroup().getType();

        if (evu.hasLifeform(lifeforms[l]) &&
            RegenerationLogic.getResproutingState(ecoGroup, evu, lifeforms[l]) != null) {
          funcGroup = RESPROUTING;
        }

        AdjacentData[] adjacentData = evu.getNeighborhood();
        for (int i = 0; i < adjacentData.length; i++) {
          Evu adj = adjacentData[i].evu;
          Lifeform life = Area.getCurrentLifeform(adj);
          if (adj.hasLifeform(life) &&
              RegenerationLogic.getAdjResproutingState(ecoGroup, adj, cStep,life) != null) {
            funcGroup = RESPROUTING;
          }
        }

        if (funcGroup != null && vegFuncGroup != funcGroup) {
          return false;
        }

        if (funcGroup != null && vegFuncGroup == funcGroup) {
          break;
        }
      }
      if (funcGroup == null) { funcGroup = NON_RESPROUTING; }

      if (vegFuncGroup != funcGroup) {
        return false;
      }
    } // ************************************

    { // *** Dist to Seed Source ***
      if (distToSeed > 0 && invasiveUnits.size() > 0) {
        long seedDist = 0;
        double minDist = Double.MAX_VALUE;
        Evu closestEvu;
        for (int i = 0; i < invasiveUnits.size(); i++) {
          double dist = evu.distanceToEvu(invasiveUnits.get(i));
          if (dist < minDist) {
            minDist = dist;
            seedDist = Math.round(dist);
            closestEvu = invasiveUnits.get(i);
          }
        }

        if (seedDist > distToSeed) {
          return false;
        }
      }
    }

    return true;
  }
/**
 * Method to sort invasive species and soil type lists.  This uses the default Collections sorting method of ascending order.  
 * then calls to the set invasive species and set soil type default methods
 */
  public void sortLists() {
    super.sortLists();
    Collections.sort(invasiveSpeciesList);
    Collections.sort(soilTypeList);
    setInvasiveSpeciesDescDefault();
    setSoilTypeDescDefault();
  }
/**
 * Checks to see if passed soil type is contained in the soil type list
 * @param soilType soil type to be checked
 * @return true if in list, false otherwise
 */
  public boolean isMemberSoilType(SoilType soilType) {
    return soilTypeList.contains(soilType);
  }
  /**
   * Adds a soil type to the list of soil types
   * @param soilType soil type to be added
   */
  public void addSoilType(SoilType soilType) {
    if (soilTypeList.contains(soilType) == false) {
      soilTypeList.add(soilType);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes a specified soil type from the soil type list
   * @param soilType soil type to be removed
   */
  public void removeSoilType(SoilType soilType) {
    if (soilTypeList.contains(soilType)) {
      soilTypeList.remove(soilType);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * sets the soil type description based on passed string.  makes the system knowledge changed and deactivates the defaultsoil type
   * @param desc the soil type description, if string passed is null or length of 0 calls the default soil type 
   */
  public void setSoilTypeDescription(String desc) {
    if (desc == null || desc.length() == 0) {
      setSoilTypeDescDefault();
      return;
    }
    soilTypeDesc = desc;
    defaultSoilTypeDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * 
   * @return true if the current simulation soil type is the default soil type
   */
  public boolean isDefaultSoilTypeDescription() {
    return defaultSoilTypeDesc;
  }
  /**
   *@return the soil type description as string literal
   */
  public String getSoilTypeDescription() { return soilTypeDesc; }

  /**
   * Sets the soil type default description.  If there is a soil type list it will print the contents string.  If there is nothing will print an 
   * empty array bracket.  
   */
  public void setSoilTypeDescDefault() {
    soilTypeDesc = (soilTypeList != null) ? soilTypeList.toString() : "[]";
    defaultSoilTypeDesc = true;
  }

  public void setInvasiveSpeciesDescription(String desc) {
    if (desc == null || desc.length() == 0) {
      setInvasiveSpeciesDescDefault();
      return;
    }
    invasiveSpeciesDesc = desc;
    defaultInvasiveSpeciesDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Used in the GUI to check if invasive species description from the chooser dialog is the default invasive species description.  
   * @return
   */
  public boolean isDefaultInvasiveSpeciesDescription() {
    return defaultInvasiveSpeciesDesc;
  }
  /**
   * Gets the invasive species description in string literal form.  This will be displayed in GUI description text fields and current text label.
   * @return invasive species description.
   */
  public String getInvasiveSpeciesDescription() { return invasiveSpeciesDesc; }
/**
 * Sets the invasive species default description.
 */
  public void setInvasiveSpeciesDescDefault() {
    invasiveSpeciesDesc = (invasiveSpeciesList != null) ? invasiveSpeciesList.toString() : "[]";
    defaultInvasiveSpeciesDesc = true;
  }

/**
 * gets the object falue at a specified column: choices, in order, are Invasive species, soil type, vegetative functional grpu, distance to seed
 * start value and probability. 
 */
  public Object getValueAt(int col) {
    switch (col) {
      case InvasiveSpeciesLogic.INVASIVE_SPECIES_COL: return invasiveSpeciesDesc;
      case InvasiveSpeciesLogic.SOIL_TYPE_COL:      return soilTypeDesc;
      case InvasiveSpeciesLogic.VEG_FUNC_GROUP_COL: return vegFuncGroup;
      case InvasiveSpeciesLogic.DIST_TO_SEED_COL:   return distToSeed;
      case InvasiveSpeciesLogic.START_VALUE_COL:    return startValue;
      case InvasiveSpeciesLogic.PROB_COL:           return prob;
      default:
        return super.getValueAt(col);
    }
  }
  /**
   * sets the value at a particular column.  choices, in order, are invasive species, soil type, vegetative function group, distance to seed, start value, and probability
   * 
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case InvasiveSpeciesLogic.INVASIVE_SPECIES_COL: break; // set in table editor;
      case InvasiveSpeciesLogic.SOIL_TYPE_COL:     break; // set in table editor;
      // Can't test equality of double so always mark as changed.
      case InvasiveSpeciesLogic.VEG_FUNC_GROUP_COL:
        if (vegFuncGroup != (VegFunctionalGroup)value) {
          vegFuncGroup = (VegFunctionalGroup) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case InvasiveSpeciesLogic.DIST_TO_SEED_COL:
        distToSeed = (Long)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogic.START_VALUE_COL:
        if (startValue != (Integer)value) {
          startValue = (Integer)value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case InvasiveSpeciesLogic.PROB_COL:
        if (prob != (Integer)value) {
          prob = (Integer) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * writes out an object in following order: version, invasive species list, invasive species description
 * default invasive species description, representitive species, soil type list, soil type descritption, 
 * default soil type description, vegetative functional group, distance to seed, start value, and probability
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(version);

    out.writeObject(invasiveSpeciesList);
    out.writeObject(invasiveSpeciesDesc);
    out.writeBoolean(defaultInvasiveSpeciesDesc);

    out.writeObject(repSpecies);

    out.writeObject(soilTypeList);
    out.writeObject(soilTypeDesc);
    out.writeBoolean(defaultSoilTypeDesc);

    out.writeObject(vegFuncGroup);
    out.writeLong(distToSeed);
    out.writeInt(startValue);

    out.writeInt(prob);
  }
/**
 * Reads in an invasive species logic data object.  
 */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(in);
    int version = in.readInt();

    invasiveSpeciesList        = (ArrayList<Species>)in.readObject();
    invasiveSpeciesDesc        = (String)in.readObject();
    defaultInvasiveSpeciesDesc = in.readBoolean();

    repSpecies = (Species)in.readObject();

    soilTypeList        = (ArrayList<SoilType>)in.readObject();
    soilTypeDesc        = (String)in.readObject();
    defaultSoilTypeDesc = in.readBoolean();

    vegFuncGroup = (VegFunctionalGroup)in.readObject();
    distToSeed   = in.readLong();
    startValue   = in.readInt();

    prob            = in.readInt();
  }
/**
 * Gets the probability as an int.
 * @return
 */
    public int getProb() {
      return prob;
    }
/**
 * Gets the start value
 * @return
 */
    public int getStartValue() {
      return startValue;
    }
/**
 * Sets the representative species for this Invasive Speices Logic Data object.  
 * @param repSpecies
 */
    public void setRepSpecies(Species repSpecies) {
      this.repSpecies = repSpecies;
    }
  }




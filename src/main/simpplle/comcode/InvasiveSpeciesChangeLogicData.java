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
import java.util.Collections;

/**
 * This class contains methods to handle Invasive Species Change Logic Data, a type of Logic Data
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class InvasiveSpeciesChangeLogicData extends LogicData implements
    Externalizable {
  static final long serialVersionUID = -2991136615581781170L;
  static final int  version          = 2;

  protected ArrayList<Species> invasiveSpeciesList;
  protected String             invasiveSpeciesDesc;
  protected boolean            defaultInvasiveSpeciesDesc;

  private int       changeRate = 0;
  private boolean   changeAsPercent=false;
  private int       stateChangeThreshold = 0;
  protected Species repSpecies;

  /**
   * Constructor. Does invoke LogicData superclass.  Initializes invasive species list arraylist, invasive species to "[]", change as percent to true, default invasiveSpeciesDesc to true 
   * initializes system knowledge to Invasive Species Logic
   */
  public InvasiveSpeciesChangeLogicData() {
    invasiveSpeciesList        = new ArrayList<Species>();
    invasiveSpeciesDesc        = "[]";
    changeAsPercent            = false;
    defaultInvasiveSpeciesDesc = true;

    sysKnowKind     = SystemKnowledge.Kinds.INVASIVE_SPECIES_LOGIC;
  }
/**
 * duplicates Invasive Species Change Logic references LogicData superclass
 */
  public AbstractLogicData duplicate() {
    InvasiveSpeciesChangeLogicData logicData = new InvasiveSpeciesChangeLogicData();
    super.duplicate(logicData);

    logicData.invasiveSpeciesList        = new ArrayList<Species>(invasiveSpeciesList);
    logicData.invasiveSpeciesDesc        = invasiveSpeciesDesc;
    logicData.defaultInvasiveSpeciesDesc = defaultInvasiveSpeciesDesc;
    logicData.changeRate                 = changeRate;
    logicData.changeAsPercent            = changeAsPercent;
    logicData.stateChangeThreshold       = stateChangeThreshold;
    logicData.repSpecies                 = repSpecies;

    return logicData;
  }
/**
 * calls LogicData list sorter function.  This uses the Java Collections sort (List<t> list) which puts the list into ascending order
 */
  public void sortLists() {
    super.sortLists();
    Collections.sort(invasiveSpeciesList);
    setInvasiveSpeciesDescDefault();
  }
/**
 * 
 * @param species the species to be evaluated as an invasive species
 * @return true if species is an invasive species
 */
  public boolean isMemberInvasiveSpecies(Species species) {
    return invasiveSpeciesList.contains(species);
  }
/**
 * Adds a species to the invasive species list and marks system knowledge as changed.  This is invoked from the GUI table model for invasive species.  
 * @param species
 */
  public void addInvasiveSpecies(Species species) {
    if (isMemberInvasiveSpecies(species) == false) {
      invasiveSpeciesList.add(species);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * removes a species from the invasive species list, if the list contains that species.  Then marks system knowledge changed.  
   * @param species
   */
  public void removeInvasiveSpecies(Species species) {
    if (invasiveSpeciesList.contains(species)) {
      invasiveSpeciesList.remove(species);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * count invokes the list.size function
   * @return the size of the invasive species list
   */
  public int getInvasiveSpeciesCount() { return invasiveSpeciesList.size(); }
  /**
   * gets the invasive species list
   * @return the species arraylist
   */
  public ArrayList<Species> getInvasiveSpeciesList() { return invasiveSpeciesList; }

  /**
   * passes false no update rate plus all parameters to overloaded doChange method
   * @param evu ecological vegetative unit
   * @param lifeform 
   * @param invSpecies 
   * @param hasInvasive true if evu has invasive
   * @return result of overloaded doChange method, it will be a boolean
   */
  public boolean doChange(Evu evu, Lifeform lifeform,
                          InclusionRuleSpecies invSpecies, boolean hasInvasive)
  {
    return doChange(evu,lifeform,invSpecies,hasInvasive,false);
  }
  /**
   * 
   * @param evu
   * @param lifeform
   * @param invSpecies
   * @param hasInvasive
   * @param noUpdateRate
   * @return
   */
  public boolean doChange(Evu evu, Lifeform lifeform,
                          InclusionRuleSpecies invSpecies, boolean hasInvasive,
                          boolean noUpdateRate)
  {
    if (!noUpdateRate) {
      evu.getState(lifeform).updateTrackingSpecies(invSpecies, changeRate, changeAsPercent);
    }
//    evu.updateInvasiveTrackSpecies(changeRate,lifeform);

    if (hasInvasive) { return false; }

    float currentValue = evu.getTrackingSpeciesPercent(lifeform,invSpecies);
    if (stateChangeThreshold < currentValue) {
      Species species = Species.get(invSpecies.toString());
      if (species == null) { return false; }

      Density density = Density.getFromPercentCanopy(Math.round(currentValue));
      HabitatTypeGroup htGrp = evu.getHabitatTypeGroup();
      VegetativeType vt = htGrp.getVegetativeType(species,density);
      if (vt == null) { return false; }

      VegSimStateData state = evu.getState(species.getLifeform());
      if (state == null) {
        Climate.Season season = Simulation.getInstance().getCurrentSeason();
        int ts  = Simulation.getCurrentTimeStep();
        int run = Simulation.getCurrentRun();
        evu.createAndStoreState(ts,run,vt,ProcessType.SUCCESSION,(short)100,season);
      }
      else {
        state.setVegType(vt);
      }
      return true;
    }
    return false;
  }

  public boolean isMatch(Evu evu, InclusionRuleSpecies species) {
    if (super.isMatch(evu) == false) { return false; }

    return invasiveSpeciesList.contains(Species.get(species.toString()));
  }
  public int getChangeRate() {
    return changeRate;
  }

  public int getStateChangeThreshold() {
    return stateChangeThreshold;
  }
/**
 * Gets the representative species.  
 * @return
 */
  public Species getRepSpecies() {
    return repSpecies;
  }
/**
 * Sets the invasive species change rate.  
 * @param changeRate the new invasive species change rate.  
 */
  public void setChangeRate(int changeRate) {
    this.changeRate = changeRate;
  }
/**
 * Sets the state change threshold.  
 * @param stateChangeThreshold
 */
  public void setStateChangeThreshold(int stateChangeThreshold) {
    this.stateChangeThreshold = stateChangeThreshold;
  }
/**
 * Sets the representative invasive species.  
 * @param repSpecies
 */
  public void setRepSpecies(Species repSpecies) {
    this.repSpecies = repSpecies;
  }
/**
 * sets the invasive species description to the string passed
 * changes default description to fault
 * @param desc the description of invasive species
 */
  public void setInvasiveSpeciesDescription(String desc) {
    if (desc == null || desc.length() == 0) {
      setInvasiveSpeciesDescDefault();
      return;
    }
    invasiveSpeciesDesc = desc;
    defaultInvasiveSpeciesDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  public boolean isDefaultInvasiveSpeciesDescription() {
    return defaultInvasiveSpeciesDesc;
  }
  /**
   * gets the Invasive Species Description 
   * @return invasive species description, this is a string
   */
  public String getInvasiveSpeciesDescription() { return invasiveSpeciesDesc; }

  /**
   * if invasive species list is null set to "[]" else to strings the invasive species list which sets the default as what is in there.  
   * changes default invasive species description boolean to true meaning the system has a default invasive species description 
   */
  public void setInvasiveSpeciesDescDefault() {
    invasiveSpeciesDesc = (invasiveSpeciesList != null) ? invasiveSpeciesList.toString() : "[]";
    defaultInvasiveSpeciesDesc = true;
  }

  public Object getValueAt(int col) {
    switch (col) {
      case InvasiveSpeciesLogic.INVASIVE_SPECIES_COL:       return invasiveSpeciesDesc;
      case InvasiveSpeciesLogic.CHANGE_RATE_COL:            return changeRate;
      case InvasiveSpeciesLogic.CHANGE_AS_PERCENT_COL:      return changeAsPercent;
      case InvasiveSpeciesLogic.STATE_CHANGE_THRESHOLD_COL: return stateChangeThreshold;
      default:
        return super.getValueAt(col);
    }
  }
  /**
   * sets invasive species logic at appropriate column, includes change rate, change as percent, state change threshold
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case InvasiveSpeciesLogic.INVASIVE_SPECIES_COL: break;
      case InvasiveSpeciesLogic.CHANGE_RATE_COL:
        if (changeRate != (Integer)value) {
          changeRate = (Integer) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case InvasiveSpeciesLogic.CHANGE_AS_PERCENT_COL:
        if (changeAsPercent != (Boolean)value) {
          changeAsPercent = (Boolean) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case InvasiveSpeciesLogic.STATE_CHANGE_THRESHOLD_COL:
        if (stateChangeThreshold != (Integer)value) {
          stateChangeThreshold = (Integer) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * writes the version, invasive species list, invasive species description, default invasive species description, change rate, change as percent, and state change threshold
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(version);

    out.writeObject(invasiveSpeciesList);
    out.writeObject(invasiveSpeciesDesc);
    out.writeBoolean(defaultInvasiveSpeciesDesc);

    out.writeInt(changeRate);
    out.writeBoolean(changeAsPercent);
    out.writeInt(stateChangeThreshold);
  }
/**
 * method to read in invasive species change logic data
 * note: casting is needed for ArrayList<Species>,
 * ifversion >1 the boolean for change as percent is read in, if version <1 set to  false 
 */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(in);
    int version          = in.readInt();

    invasiveSpeciesList = (ArrayList<Species>) in.readObject();
    invasiveSpeciesDesc        = (String)in.readObject();
    defaultInvasiveSpeciesDesc = in.readBoolean();

    changeRate           = in.readInt();
    changeAsPercent = false;
    if (version > 1) {
      changeAsPercent = in.readBoolean();
    }
    stateChangeThreshold = in.readInt();
  }

}

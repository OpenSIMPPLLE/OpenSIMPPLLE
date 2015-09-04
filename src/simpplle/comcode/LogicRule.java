package simpplle.comcode;

import java.io.Externalizable;
import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Logic Rule.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 */

public class LogicRule implements Externalizable {
  static final long serialVersionUID = 274843090722923215L;
  static final int  version          = 1;

  private SimpplleType[] groupList;
  private SimpplleType[] speciesList;
  private SimpplleType[] sizeClassList;
  private SimpplleType[] densityList;
  private SimpplleType[] processList;
  private SimpplleType[] treatmentList;
  private LogicRule      adjacentRule;

  private boolean groupNot     = false;
  private boolean speciesNot   = false;
  private boolean sizeClassNot = false;
  private boolean densityNot   = false;
  private boolean processNot   = false;
  private boolean treatmentNot = false;

  private int[] probs;
/**
 * Primary constructor for Logic Rule.  Contains no variables
 */
  public LogicRule() {
  }
  /**
   * Overloaded constructor for logic rule.  Takes in an integer which will be used to make the size of a new probability array which will 
   * be loaded with 0's.   
   * @param numProbs the size of new probability array
   */
  public LogicRule(int numProbs) {
    probs = new int[numProbs];
    for (int i = 0; i < probs.length; i++) { probs[i] = 0; }
  }
  /**
   * Checks whether a particular simpplle type is a member of passed in simpplle type array.  
   * @param items the simpplle type array
   * @param value the simpplle type object being evaluated
   * @return true if parameter simpplle type object is in the parameter simpplle type array
   */
  private boolean isMember(SimpplleType[] items, SimpplleType value) {
    for (int i=0; i<items.length; i++) {
      if (items[i] == value) { return true; }
    }
    return false;
  }
/**
 * Checks to see if Evu habitat group is a member of the simpplle type groupList.  Then compares the current evu state and checks to see 
 * if species, size class, density, process, and treatment list matches the corresponding values in the logic rules.   
 * @param evu the existing vegetative unit being evaluated
 * @return true if evu species, size class, density, process, and treatment is a match for the logic rules
 */
  public boolean isUnitMatch(Evu evu) {
    boolean match=false;
    if (groupList != null) {
      match = isMember(groupList,evu.getHabitatTypeGroup().getType());
      if (groupNot) { match = !match; }
      if (!match) { return false; }
    }

    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();
    ProcessType process = state.getProcess();

    if (speciesList != null) {
      match = isMember(speciesList,species);
      if (speciesNot) { match = !match; }
      if (!match) { return false; }
    }

    if (sizeClassList != null) {
      match = isMember(sizeClassList,sizeClass);
      if (sizeClassNot) { match = !match; }
      if (!match) { return false; }
    }

    if (densityList != null) {
      match = isMember(densityList,density);
      if (densityNot) { match = !match; }
      if (!match) { return false; }
    }

    if (processList != null) {
      match = isMember(processList,process);
      if (processNot) { match = !match; }
      if (!match) { return false; }
    }

    if (treatmentList != null) {
      Treatment treatment = evu.getCurrentTreatment();
      if (treatment != null) {
        match = isMember(treatmentList,treatment.getType());
        if (treatmentNot) { match = !match; }
        if (!match) { return false; }
      }
    }

    /**
     * @todo consider whether this will have a performance penalty.
     */
    if (adjacentRule != null) {
      AdjacentData[] adjData = evu.getAdjacentData();
      LogicRule      rule;
      for (int i=0; i<adjData.length; i++) {
        match = adjacentRule.isUnitMatch(adjData[i].evu);
        if (match) { break; }
      }
      if (!match) { return false; }
    }


    return match;
  }
/**
 * Sets the habitat groups for the logic rule.  
 * @param values the habitat types to be used in group. 
 */
  public void setGroups(SimpplleType[] values) { groupList = values; }
  /**
   * Gets the logic rule habitat types groups array.
   * @return habitat group array
   */
  public SimpplleType[] getGroups() { return groupList; }
  /**
   * Sets the group not boolean 
   * @param value true if habitat group not to be included in logic rule
   */
  public void setGroupNot(boolean value) { groupNot = value; }
/**
 * Sets the adjacent evu logic rule.  
 * @param rule the adjacent evu logic rule
 */
  public void setAdjacentRule(LogicRule rule) { adjacentRule = rule; }
  /**
   * Gets the adjacent evu logic rule.
   * @return adjacent evu logic rule
   */
  public LogicRule getAdjacentRule() { return adjacentRule; }
/**
 * Sets the logic rule species.  
 * @param values the species list to be set in the logic rule
 */
  public void setSpecies(SimpplleType[] values) { speciesList = values; }
  /**
   * Gets the Logic Rule's species array.  
   * @return logic rule species array.
   */
  public SimpplleType[] getSpecies() { return speciesList; }
  /**
   * Sets the species not boolean variable.
   * @param value  true if not to include species in Logic Rule match
   */
  public void setSpeciesNot(boolean value) { speciesNot = value; }
/**
 * Set the size classes in the logic rule. 
 * @param values the size classes to be included in logic rule. 
 */
  public void setSizeClasses(SimpplleType[] values) { sizeClassList = values; }
  /**
   * Gets all the size classes in the logic rule.  
   * @return all the size classes in the logic rule in an array
   */
  public SimpplleType[] getSizeClasses() { return sizeClassList; }
  /**
   * Sets the size class not boolean which designates if size class should be included in logic rule match.  
   * @param value true if size class should be included in Logic Rule matching
   */
  public void setSizeClassNot(boolean value) { sizeClassNot = value; }
/**
 * Sets the Logic Rule densities.  
 * @param values the new densities to be set in the logic rule
 */
  public void setDensities(SimpplleType[] values) { densityList = values; }
  /**
   * Gets the Logic Rules densities.  
   * @return the density list which is the list of densities in the Logic Rule
   */
  public SimpplleType[] getDensities() { return densityList; }
  /**
   * Sets the density not boolean, which designates if density should be included in logic rule match.  
   * @param value true if density should not be included in logic rule matching
   */
  public void setDensityNot(boolean value) { densityNot = value; }
/**
 * Sets the Logic Rule processes.   
 * @param values the list of processes to be included in logic rule
 */
  public void setProcesses(SimpplleType[] values) { processList = values; }
  /**
   * Gets the processes included in Logic Rule
   * @return the list of logic rules processes
   */
  public SimpplleType[] getProcesses() { return processList; }
  /**
   * Sets the processes not boolean, which designates if processes should be included in logic rule matching.  
   * @param value true if processes should not be included in logic rule matching
   */
  public void setProcessNot(boolean value) { processNot = value; }
/**
 * Sets the Logic Rules treatments.  
 * @param values the list of treatments to be included in logic rule
 */
  public void setTreatments(SimpplleType[] values) { treatmentList = values; }
  /**
   * Gets the treatments included in logic rules.
   * @return list of treatments included in Logic Rule
   */
  public SimpplleType[] getTreatments() { return treatmentList; }
  /**
   * Sets the treatments not boolean, which designates if treatments should be included in logic rule matching.  
   * @param value true if treatments should not be included in logic rule matching
   */
  public void setTreatmentNot(boolean value) { treatmentNot = value; }
/**
 * Gets a particular probability in logic rule
 * @param which the index into probability array
 * @return the probability at designated index
 */
  public int getProbability(int which) { return probs[which]; }
  /**
   * Sets the probability at a particular index
   * @param which
   * @param value
   */
  public void setProbability(int which, int value) { probs[which] = value; }
  /**
   * Gets all the probabilities in Logic Rule
   * @return all the probabilities
   */
  public int[] getProbabilities() { return probs; }
  /**
   * Sets all the probabilities in logic rule.  
   * @param values the array of probabilities to be included in Logic Rule.  
   */
  public void setAllProbability(int[] values) { probs = values; }

  /**
   * Reads logic rule objects from external source items in following order: group list, species list, size class list, density list, process list, 
   * treatment list, group, species, size class, density, process, treatment - booleans, probabilities
   */
  public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {
    int version = in.readInt();

    groupList     = (SimpplleType[])in.readObject();
    speciesList   = (SimpplleType[])in.readObject();
    sizeClassList = (SimpplleType[])in.readObject();
    densityList   = (SimpplleType[])in.readObject();
    processList   = (SimpplleType[])in.readObject();
    treatmentList = (SimpplleType[])in.readObject();

    groupNot     = in.readBoolean();
    speciesNot   = in.readBoolean();
    sizeClassNot = in.readBoolean();
    densityNot   = in.readBoolean();
    processNot   = in.readBoolean();
    treatmentNot = in.readBoolean();

    probs = (int[])in.readObject();
  }
  
  /**
   * Writes logic rules objects to external location.  These will be written in following order 
   * group list, species list, size class list, density list, process list, 
   * treatment list, group, species, size class, density, process, treatment - booleans, probabilities
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(groupList);
    out.writeObject(speciesList);
    out.writeObject(sizeClassList);
    out.writeObject(densityList);
    out.writeObject(processList);
    out.writeObject(treatmentList);

    out.writeBoolean(groupNot);
    out.writeBoolean(speciesNot);
    out.writeBoolean(sizeClassNot);
    out.writeBoolean(densityNot);
    out.writeBoolean(processNot);
    out.writeBoolean(treatmentNot);

    out.writeObject(probs);
  }
}




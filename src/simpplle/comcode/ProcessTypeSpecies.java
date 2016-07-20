/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Species Process Type. This is a combination of a process object and species object.   
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class ProcessTypeSpecies implements Comparable {
  ProcessType process;
  Species     species;
/**
 * Constructor for Species process types.  Sets a process object and species object to the parameter process and species.  
 * @param process
 * @param species
 */
  public ProcessTypeSpecies(ProcessType process, Species species) {
    this.process = process;
    this.species = species;
  }
/**
 * Creates a species hash code by creating a process hashcode to the power of the species hashcode.
 */
  public int hashCode() {
    return process.hashCode() ^ species.hashCode();
  }
  /**
   * Checks to see if parameter object is an instance of a Process Type Species.  If so it compares parameter process type species
   * with this process type species.  Returns true if their process name is equal and their species name is equal.     
   */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof ProcessTypeSpecies) {
      if (species == null || process == null || obj == null) { return false; }

      return process.equals(((ProcessTypeSpecies)obj).process) &&
             species.equals(((ProcessTypeSpecies)obj).species);
    }
    return false;
  }
/**
 * Implementation of the required compareTo method for Comparable interfaces.  Compares to processes with each other.  
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    if (o instanceof ProcessTypeSpecies) {
      ProcessTypeSpecies ps = (ProcessTypeSpecies) o;
      int c = process.compareTo(ps.process);
      if (c == 0) {
        return species.compareTo(ps.species);
      }
      else {
        return c;
      }
    }
    return -1;
  }
/**
 * Gets the Species process object.  This will contain both the process name and species name.    
 * @return SpeciesProcess object
 */
  public ProcessType getProcess() {
    return process;
  }
/**
 * Gets the species of this process. 
 * @return the species of this SpeciesProcess
 */
  public Species getSpecies() {
    return species;
  }
}

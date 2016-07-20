/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Regeneration Succession Info. Regeneration is a process that depends on spatial information.  
 * Regeneration logic is applied under either succession or fire regeneration logic.
 * 
 * <p>This class specifically gets the succession regeneration logic.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 */

public class RegenerationSuccessionInfo implements Externalizable {
  static final long serialVersionUID = -3042155723299739387L;
  static final int  version          = 1;

  public Species        seedSpecies;
  public VegetativeType nextState;
/**
 * Primary, default constructor.  Has no arguments, initializes no data.  
 */
  public RegenerationSuccessionInfo() {}
/**
 * Overloaded Regeneration succession info constructor.  Initializes seed species and the next vegetative type state
 * @param seedSpecies
 * @param nextState
 */
  public RegenerationSuccessionInfo(Species seedSpecies, VegetativeType nextState) {
    this.seedSpecies = seedSpecies;
    this.nextState   = nextState;
  }
/**
 * Overloaded regeneration succession info constructor.  
 * @param infoString
 * @throws ParseError thrown if species or state is invalid - caught in GUI
 */
  public RegenerationSuccessionInfo(String infoString) throws ParseError {
    int index = infoString.indexOf("=>");
    seedSpecies = Species.get(infoString.substring(0,index));
    nextState = HabitatTypeGroup.getVegType(infoString.substring(index+2));

    if (seedSpecies == null || nextState == null) {
      throw new ParseError("Either the Species or State in the following is invalid.\n" +
                           "Perhaps a pathway file needs to be loaded first\n" +
                           "Invalid information: " + infoString);
    }
  }
/**
 * the tostring outputs seed species and next state.  
 */
  public String toString() {
    return seedSpecies.toString() + "=>" + nextState.toString();
  }
/**
 * writes to external location theseed species and next state objects.
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    seedSpecies.writeExternalSimple(out);
    out.writeObject(nextState);

  }
/**
 * reads Regeneration succession objects from external source in order, version, seed species, and vegetative next state
 */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int version = in.readInt();
    seedSpecies = (Species)SimpplleType.readExternalSimple(in,SimpplleType.SPECIES);
    nextState = (VegetativeType)in.readObject();
  }

}

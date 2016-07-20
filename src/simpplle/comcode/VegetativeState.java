/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.ArrayList;
import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines a Vegetative State.  Formerly this
 * information was in the form of the Current State member
 * of the Class Evu.  However this was changed to accomodate
 * multiple lifeforms (Trees, Shrubs, Herbacious).
 * In addition each lifeform will have it own process, so
 * Process information will move here as well.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 *
 * @see Evu
 * @deprecated
 *
 */

public class VegetativeState  implements Externalizable {
  static final long serialVersionUID = 6458806305172458144L;

  // Not sure why this is version 2 instead of 1, as it appears that
  // nothing really changed in the serialized file between 1 and 2.
  // I think perhaps I changed this version, when I should have changed
  // the version for the lifeform class.
  static final int  version          = 2;

  public static final VegetativeState UNKNOWN = new VegetativeState(
      VegetativeType.UNKNOWN);

  public static final VegetativeState ND = new VegetativeState(
      VegetativeType.ND,ProcessType.NONE);

  private static class LifeformState {
    // There was no version 2 (due to my mistake), however this is
    // the 3rd version of the serialization code so version 3 makes sense.
    static final int  version = 3;

    public VegetativeType veg;
    public ProcessType    process;
    public int            processProb;

    public LifeformState() {}
    public LifeformState(VegetativeType veg) {
      this(veg,ProcessType.SUCCESSION,Evu.NOPROB);
    }
    public LifeformState(VegetativeType veg, ProcessType process) {
      this(veg,process,Evu.NOPROB);
    }
    public LifeformState(VegetativeType veg, ProcessType process, int prob) {
      this.veg         = veg;
      this.process     = process;
      this.processProb = prob;
    }
    public Lifeform getLifeform() {
      return veg.getSpecies().getLifeform();
    }
    /**
     * @todo Need to change version# to 3 and put in code to handle version 2.
     * @param in
     * @throws IOException
     * @throws java.lang.ClassNotFoundException
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
      int version = in.readInt();

      if (version == 1 || version == 2) {
        veg = (VegetativeType)in.readObject();
        process = (ProcessType)in.readObject();
      }
      else {
        veg = VegetativeType.readExternalData(in);
        process = ProcessType.readExternalSimple(in);
      }

      String str = (String)in.readObject();
      // This multiple reads below is due to a error in the case statement
      // in writeExternal where I forgot the break statement.  Thus
      // leading to fall through and excessive writing of probs.
      // Commented out multiple reads as stream seems to advance by itself
      // skipping the extra data.
      if (str.equals(Evu.D_STR)) {
        processProb = Evu.D;
        if (version == 1) {
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
        }
      }
      else if (str.equals(Evu.L_STR)) {
        processProb = Evu.L;
        if (version == 1) {
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
        }
      }
      else if (str.equals(Evu.S_STR)) {
        processProb = Evu.S;
        if (version == 1) {
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
        }
      }
      else if (str.equals(Evu.SE_STR)) {
        processProb = Evu.SE;
        if (version == 1) {
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
        }
      }
      else if (str.equals(Evu.SFS_STR)) {
        processProb = Evu.SFS;
        if (version == 1) {
//          str = (String)in.readObject();
//          str = (String)in.readObject();
//          str = (String)in.readObject();
        }
      }
      else if (str.equals(Evu.SUPP_STR)) {
        processProb = Evu.SUPP;
        if (version == 1) {
//          str = (String)in.readObject();
//          str = (String)in.readObject();
        }
      }
      else if (str.equals(Evu.COMP_STR)) {
        processProb = Evu.COMP;
      }
      else if (str.equals(Evu.NOPROB_STR)) {
        processProb = Evu.NOPROB;
        if (version == 1) {
//          str = (String)in.readObject();
        }
      }
      else {
        try {
          processProb = Integer.parseInt(str);
        }
        catch (NumberFormatException ex) {
          processProb = Evu.NOPROB;
        }
      }

    }
    public void writeExternal(ObjectOutput out) throws IOException {
      VegetativeType.setLimitedSerialization();

      out.writeInt(version);
      veg.writeExternal(out);
      process.writeExternalSimple(out);
      switch (processProb) {
        case Evu.D:    out.writeObject(Evu.D_STR); break;
        case Evu.L:    out.writeObject(Evu.L_STR); break;
        case Evu.S:    out.writeObject(Evu.S_STR); break;
        case Evu.SE:   out.writeObject(Evu.SE_STR); break;
        case Evu.SFS:  out.writeObject(Evu.SFS_STR); break;
        case Evu.SUPP: out.writeObject(Evu.SUPP_STR); break;
        case Evu.COMP: out.writeObject(Evu.COMP_STR); break;
        case Evu.NOPROB: out.writeObject(Evu.NOPROB_STR); break;
        default:   out.writeObject(IntToString.get(processProb)); break;
      }

      VegetativeType.clearLimitedSerialization();
    }
  }



  // Will either be a LifeformState or LifeformState[]
  // Done this way to save memory.
  private Object vegStates;

  public VegetativeState() {
    vegStates = null;
  }
  public VegetativeState(int numLifeforms) {
    vegStates = new LifeformState[numLifeforms];
  }

  public VegetativeState(VegetativeType state) {
    vegStates = new LifeformState(state);
  }

  public VegetativeState(VegetativeType[] states) {
    this(states.length);
    LifeformState[] tmpStates = (LifeformState[])vegStates;
    for (int i=0; i<states.length; i++) {
      tmpStates[i] = new LifeformState(states[i]);
    }
  }

  public VegetativeState(ProcessType process) {
    this(null,process);
  }
  public VegetativeState(VegetativeType state, ProcessType process) {
    vegStates = new LifeformState(state,process);
  }

  /**
   * Order of dominance for lifeforms is: TREES, SHRUBS, HERBACIOUS
   * @return
   */
  private LifeformState findDominantLife() {
    if (vegStates instanceof LifeformState) { return (LifeformState)vegStates; }

    LifeformState dominant=null, state;
    Lifeform      kind;
    LifeformState[] states = (LifeformState[])vegStates;

    for (int i=0; i<states.length; i++) {
      kind = states[i].veg.getSpecies().getLifeform();

      if (dominant == null) {
        dominant = states[i];
      }
      else if (kind == Lifeform.TREES) {
        dominant = states[i];
      }
      else if (dominant.getLifeform() != Lifeform.TREES &&
               kind == Lifeform.SHRUBS) {
        dominant = states[i];
      }
      else if (dominant.getLifeform() != Lifeform.TREES &&
               dominant.getLifeform() != Lifeform.SHRUBS &&
               kind == Lifeform.HERBACIOUS) {
        dominant = states[i];
      }
      else if (dominant.getLifeform() != Lifeform.TREES &&
               dominant.getLifeform() != Lifeform.SHRUBS &&
               dominant.getLifeform() != Lifeform.HERBACIOUS &&
               kind == Lifeform.AGRICULTURE) {
        dominant = states[i];
      }
      else if (dominant.getLifeform() != Lifeform.TREES &&
               dominant.getLifeform() != Lifeform.SHRUBS &&
               dominant.getLifeform() != Lifeform.HERBACIOUS &&
               dominant.getLifeform() != Lifeform.AGRICULTURE &&
               kind == Lifeform.NA) {
        dominant = states[i];
      }
    }
    return dominant;
  }

  private LifeformState findLifeformState(Lifeform kind) {
    LifeformState state=null;

    if (vegStates instanceof LifeformState) {
      state = (LifeformState)vegStates;
      return (state.getLifeform() == kind) ? state : null;
    }


    LifeformState[] states = (LifeformState[])vegStates;
    for (int i=0; i<states.length; i++) {
      if (states[i].getLifeform() == kind) { return state; }
    }

    return state;
  }

  /**
   * Set the process for the given lifeform.
   */
  public void setProcess(ProcessType p, Lifeform kind) {
    LifeformState state = findLifeformState(kind);
    if (state != null) { state.process = p; }
  }
  public void setProcess(ProcessType p) {
    LifeformState state = findDominantLife();
    if (state != null) { state.process = p; }
  }

  /**
   * Will return the process associated with the dominant vegetative
   * lifeform.
   *
   * @return a Process, the process associated with the dominant
   *         lifeform.
   */
  public ProcessType getProcess() {
    return findDominantLife().process;
  }

  /**
   * Return the Process associated with the given lifeform type.
   */
  public ProcessType getProcess(Lifeform kind) {
    LifeformState state = findLifeformState(kind);
    if (state != null) { return state.process; }

    return null;
  }

  /**
   * Set the process for the given lifeform.
   */
  public void setProcessProb(int prob) {
    LifeformState state = findDominantLife();
    if (state != null) { state.processProb = prob; }
  }
  public void setProcessProb(int prob, Lifeform kind) {
    LifeformState state = findLifeformState(kind);
    if (state != null) { state.processProb = prob; }
  }


  /**
   * Will return the probability of the process associated with the
   * dominant lifeform.  Probabilities and processes are assigned
   * during a simulation.
   */
  public int getProcessProb() {
    return findDominantLife().processProb;
  }

  /**
   * Will return the probability of the process associated with the
   * given lifeform.  Probabilities and processes are assigned
   * during a simulation.
   * @param kind is a Lifeform type.
   */
  public int getProcessProb(Lifeform kind) {
    LifeformState state = findLifeformState(kind);
    if (state != null) { return state.processProb; }

    return Evu.NOPROB;
  }

  /**
   * Will return the VegetativeType associated with the dominant
   * vegetative lifeform.
   *
   * @return a VegetativeType, the VegetativeType associated
   *         with the dominant lifeform.
   */
  public VegetativeType getVegetativeType() {
    return findDominantLife().veg;
  }

  public void setVegetativeType(VegetativeType state) {
    findDominantLife().veg = state;
  }

  /**
   * Return the VegetativeType associated with the given lifeform type.
   */
  public VegetativeType getVegetativeType(Lifeform kind) {
    LifeformState state = findLifeformState(kind);
    if (state != null) { return state.veg; }

    return null;
  }

  public String toString() {
    return (getVegetativeType().toString());
  }

  // ** Serialization Methods **
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    LifeformState state;
    int           size = in.readInt();

    if (size == 1) {
      state = new LifeformState();
      state.readExternal(in);
      vegStates = state;
    }
    else {
      LifeformState[] states = new LifeformState[size];
      for (int i = 0; i < size; i++) {
        states[i] = new LifeformState();
        states[i].readExternal(in);
      }
      vegStates = states;
    }
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    LifeformState state;
    if (vegStates instanceof LifeformState) {
      out.writeInt(1);
      ((LifeformState)vegStates).writeExternal(out);
    }
    else {
      LifeformState[] states = (LifeformState[]) vegStates;
      out.writeInt(states.length);
      for (int i = 0; i < states.length; i++) {
        states[i].writeExternal(out);
      }
    }

    VegetativeType.clearLimitedSerialization();
  }
}




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
import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.List;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Roads, a type of Manmade Element.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.ManmadeElement
 *
 */

public class Roads extends ManmadeElement implements Externalizable {
  static final long serialVersionUID = 6665420592844344851L;
  static final int  version          = 1;
  static final int  simDataVersion   = 1;

  public static final int NIL = 0; // Unknown
  public static final int N   = 1; // None
  public static final int O   = 2; // Open Roaded
  public static final int C   = 3; // Closed Roaded
  public static final int P   = 4; // Proposed
  public static final int E   = 5; // Eliminated

  public static final String NIL_STR = "NIL";
  public static final String N_STR   = "N";
  public static final String O_STR   = "O";
  public static final String C_STR   = "C";
  public static final String P_STR   = "P";
  public static final String E_STR   = "E";

  public enum Status {

    OPEN(O), CLOSED(C), PROPOSED(P), ELIMINATED(E), UNKNOWN(NIL), NONE(N);

    private final int value;

    public int getValue() {
      return value;
    }

    Status(int value) {
      this.value = value;
    }

    public String getSaveName() {
      switch (this) {
        case UNKNOWN:    return NIL_STR;
        case NONE:       return N_STR;
        case OPEN:       return O_STR;
        case CLOSED:     return C_STR;
        case PROPOSED:   return P_STR;
        case ELIMINATED: return E_STR;
        default:         return super.toString();
      }
    }

    public static Status lookup(int id) {
      switch(id) {
        case NIL: return UNKNOWN;
        case N:   return NONE;
        case O:   return OPEN;
        case C:   return CLOSED;
        case P:   return PROPOSED;
        case E:   return ELIMINATED;
        default: return UNKNOWN;
      }
    }

    public static Status lookup(String str) {
      if (str.equals(NIL_STR) || str.equals("UNKNOWN")) {
        return UNKNOWN;
      } else if (str.equals(N_STR) || str.equals("NONE")) {
        return NONE;
      } else if (str.equals(O_STR) || str.equals("OPEN_ROADED") || str.equals("OPEN ROADED")) {
        return OPEN;
      } else if (str.equals(C_STR) || str.equals("CLOSED_ROADED") || str.equals("CLOSED ROADED")) {
        return CLOSED;
      } else if (str.equals(P_STR)) {
        return PROPOSED;
      } else if (str.equals(E_STR)) {
        return ELIMINATED;
      } else {
        return UNKNOWN;
      }
    }
  }

  public enum Kind { SINGLE_LANE, DOUBLE_LANE, UNIMPROVED, SYSTEM, NONSYSTEM, UNKNOWN}

  protected static final String COMMA = ",";

  private List<Evu> assocVegUnits = new ArrayList<>();

  private Status status;
  private Kind kind;

  private RoadsSimData[] simData;

  /**
   * Primary, default constructor. Inherits from Manmade Element superclass, does not initialize any variables.
   */
  public Roads() {
    super();
  }

  /**
   * Overloaded constructor. Inherits from Manmade Element superclass (which only sets the manmade element Id.) and passes id of road type.
   * Choices are open, closed, proposed, eliminated, unknown, none
   * @param id
   */
  public Roads(int id) {
    super(id);
  }

  /**
   * Reallocates an array of simulation data. The size of the array equals the number of time steps in the current
   * simulation. The first entry is initialized with the status of this road unit.
   */
  public void initSimulation() {

    int numSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    simData = new RoadsSimData[numSteps + 1];
    simData[0] = new RoadsSimData();
    simData[0].setStatus(status);

  }

  /**
   * Creates simulation data for the current time step with the status of the previous time step.
   */
  public void doBeginTimeStep() {
    int ts = Simulation.getCurrentTimeStep();
    simData[ts] = new RoadsSimData();
    simData[ts].setStatus(simData[ts - 1].getStatus());
  }

  /**
   * Returns the first associated vegetation unit. This is used to calculate the distance between a road and EVU. It
   * is not a 100% accurate distance as the road unit will cover several EVUs, but it saves computation time.
   * @return An existing vegetation unit
   */
  public Evu getFirstVegUnit() {
    if (assocVegUnits != null && assocVegUnits.size() > 0) {
      return assocVegUnits.get(0);
    }
    return null;
  }

  /**
   * Returns an array of associated vegetation units.
   * @return Associated vegetation units
   */
  public List<Evu> getAssociatedVegUnits() {
    return assocVegUnits;
  }

  /**
   * Adds an existing vegetation unit to the list of associated vegetation units.
   * @param evu An existing vegetation unit
   */
  public void addAssociatedVegUnit(Evu evu)  {
    if (assocVegUnits.contains(evu) == false) {
      assocVegUnits.add(evu);
    }
  }

  /**
   * Returns the kind of road.
   * @return Kind of road
   */
  public simpplle.comcode.Roads.Kind getKind() {
    return kind;
  }

  /**
   * Returns the road status.
   * @return Road status
   */
  public simpplle.comcode.Roads.Status getStatus() {
    return status;
  }

  /**
   * Sets the kind of this road.
   * @param kind Road kind
   */
  public void setKind(simpplle.comcode.Roads.Kind kind) {
    this.kind = kind;
  }

  /**
   * Sets the status of this road.
   * @param status Road status
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * Returns the status of this road at the current time step in the current simulation. If the simulation is not
   * running, the last time step is used.
   * @return Road status
   */
  public Roads.Status getSimStatus() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int ts = 0;
    if (simulation != null) {
      if (simulation.isSimulationRunning()) {
        ts = simulation.getCurrentTimeStep();
      } else {
        ts = simulation.getNumTimeSteps();
      }
    }
    return getSimStatus(ts);
  }

  /**
   * Returns the status of this road at the requested time step in the current simulation.
   * @param ts Time step
   * @return Road status
   */
  public simpplle.comcode.Roads.Status getSimStatus(int ts) {
    if (simData == null) return status;
    return simData[ts].getStatus();
  }

  /**
   * Sets the status of this road at the current time step in the current simulation.
   * @param status Road status
   */
  public void setSimStatus(Status status) {
    int ts = Simulation.getCurrentTimeStep();
    this.simData[ts].setStatus(status);
  }

  /**
   * Writes neighboring roads as one pair of comma separated IDs per line. This ID, neighbor ID.
   * @param fout Output writer
   */
  public void exportNeighbors(PrintWriter fout) {

    if (neighbors == null) return;

    for (ManmadeElement element : neighbors) {
      fout.print(getId());
      fout.print(COMMA);
      fout.print(element.getId());
      fout.println();
    }
  }

  /**
   * Writes the neighboring vegetation units as a pair of comma separated IDs per line. Neighbor ID, this ID.
   * @param fout Output writer
   */
  public void exportNeighborsVegetation(PrintWriter fout) {

    if (assocVegUnits == null) return;

    for (Evu evu : assocVegUnits) {
      fout.print(evu.getId());
      fout.print(COMMA);
      fout.print(getId());
      fout.println();
    }
  }

  /**
   * Writes the attributes of this road unit at the current time step in the following order: slink(id), status, kind
   * @param fout
   */
  public void exportAttributes(PrintWriter fout) {
    fout.print(getId());
    fout.print(COMMA);
    fout.print(getSimStatus());
    fout.print(COMMA);
    fout.print(getKind());
    fout.println();
  }

  /**
   * Writes this instance to a serialization stream. The attributes include: version, status, kind, number of
   * associated vegetative units, and the ID of each associated vegetative unit.
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(version);
    out.writeObject(status.toString());
    out.writeObject(kind.toString());
    out.writeInt(assocVegUnits.size());
    for (Evu evu : assocVegUnits) {
      out.writeInt(evu.getId());
    }
  }
  
  /**
   * Reads attributes from a deserialization stream. The attributes include: version, status, kind, area (from the
   * current simulation), size, and associated vegetative unit ids.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    super.readExternal(in);

    int version = in.readInt();

    status = status.valueOf((String)in.readObject());

    kind = kind.valueOf((String)in.readObject());

    Area area = Simpplle.getCurrentArea();
    int size = in.readInt();
    assocVegUnits.clear();
    for (int i=0; i<size; i++) {
      addAssociatedVegUnit(area.getEvu(in.readInt()));
    }
  }

  public void readExternalSimData(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    int size = in.readInt();
    if (size == 0) { return; }

    simData = new RoadsSimData[size];
    for (int i=0; i<size; i++) {
      simData[i] = (RoadsSimData)in.readObject();
    }
  }

  public void writeExternalSimData(ObjectOutput out) throws IOException {
    out.writeInt(simDataVersion);

    int size = (simData != null) ? simData.length : 0;

    out.writeInt(size);
    for (int i=0; i<size; i++) {
      out.writeObject(simData[i]);
    }
  }
}

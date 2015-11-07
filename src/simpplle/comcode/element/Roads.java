package simpplle.comcode.element;

import simpplle.comcode.*;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.text.NumberFormat;

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
 * @see simpplle.comcode.element.ManmadeElement
 *
 */

public class Roads extends simpplle.comcode.element.ManmadeElement implements Externalizable {
  static final long serialVersionUID = 6665420592844344851L;
  static final int  version          = 1;
  static final int  simDataVersion   = 1;

  public static final int NIL = 0; // Unknown
  public static final int N   = 1;  // None
  public static final int O   = 2;  // Open roaded
  public static final int C   = 3;  // Closed roaded
  public static final int P   = 4;  // Proposed
  public static final int E   = 5;  // Eliminated

  public static final String NIL_STR = "NIL";
  public static final String N_STR   = "N";
  public static final String O_STR   = "O";
  public static final String C_STR   = "C";
  public static final String P_STR   = "P";
  public static final String E_STR   = "E";

  public enum Status {
    OPEN(O), CLOSED(C), PROPOSED(P), ELIMINATED(E), UNKNOWN(NIL), NONE(N);

    private final int value;
    public int getValue() { return value; }
    Status(int value) { this.value = value; }

    // For compatiblity purposes.
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
      }
      else if (str.equals(N_STR) || str.equals("NONE")) {
        return NONE;
      }
      else if (str.equals(O_STR) || str.equals("OPEN_ROADED") || str.equals("OPEN ROADED")) {
        return OPEN;
      }
      else if (str.equals(C_STR) || str.equals("CLOSED_ROADED") || str.equals("CLOSED ROADED")) {
        return CLOSED;
      }
      else if (str.equals(P_STR)) {
        return PROPOSED;
      }
      else if (str.equals(E_STR)) {
        return ELIMINATED;
      }
      else {
        return UNKNOWN;
      }
    }
  };

  public enum Kind { SINGLE_LANE, DOUBLE_LANE, UNIMPROVED, SYSTEM, NONSYSTEM, UNKNOWN};

  protected static final String COMMA         = ",";

  private ArrayList<Evu> assocVegUnits = new ArrayList<Evu>();

  private Status status;
  private Kind   kind;

  // Simulation
  private RoadsSimData[] simData;
/**
 * Primary, default constructor.  Inherits from Manmade Element superclass, does not initialize any variables.
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

  public void initSimulation() {
    int numSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    simData = new RoadsSimData[numSteps+1];
    simData[0] = new RoadsSimData();
    simData[0].setStatus(status);
  }

  public void doBeginTimeStep() {
    int ts = Simulation.getCurrentTimeStep();
    simData[ts] = new RoadsSimData();
    simData[ts].setStatus(simData[ts-1].getStatus());
  }
  /**
   * Get the first associated Vegetative unit.  Used in distance to road
   * calculations. Not 100% accurate distance as the road unit will cover
   * several evu's, but it will save a lot of computation time, in finding the
   * closest road.
   * @return Evu
   */
  public Evu getFirstVegUnit() {
    if (assocVegUnits != null && assocVegUnits.size() > 0) {
      return assocVegUnits.get(0);
    }
    return null;
  }
  public ArrayList<Evu> getAssociatedVegUnits() { return assocVegUnits; }
  /**
   * 
   * @param evu Evu to be added to associated vegetative unit
   */
  public void addAssociatedVegUnit(Evu evu)  {
    if (assocVegUnits.contains(evu) == false) {
      assocVegUnits.add(evu);
    }
  }
/**
 * Choices are SINGLE_LANE, DOUBLE_LANE, UNIMPROVED, SYSTEM, NONSYSTEM, UNKNOWN
 * @return kind of road
 */
  public Roads.Kind getKind() {
    return kind;
  }
/**
 * Gets the status of this road.  Choices are OPEN(O), CLOSED(C), PROPOSED(P), ELIMINATED(E), UNKNOWN(NIL), NONE(N)
 * @return status 
 */
  public Roads.Status getStatus() {
    return status;
  }
/**
 * Sets the kind of this road object. Choices are SINGLE_LANE, DOUBLE_LANE, UNIMPROVED, SYSTEM, NONSYSTEM, UNKNOWN 
 * @param kind
 */
  public void setKind(Roads.Kind kind) {
    this.kind = kind;
  }
/**
 * Sets the status of this road.  Choices are OPEN(O), CLOSED(C), PROPOSED(P), ELIMINATED(E), UNKNOWN(NIL), NONE(N)
 * @param status
 */
  public void setStatus(Roads.Status status) {
    this.status = status;
  }
/**
 * gets the simulation status of a roads based on time step and current simulation.
 * @return
 */
  public Roads.Status getSimStatus() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int ts = 0;
    if (simulation != null && simulation.isSimulationRunning()) {
      ts = simulation.getCurrentTimeStep();
    }
    else if (simulation != null) {
      ts = simulation.getNumTimeSteps();
    }
    return getSimStatus(ts);
  }
  public Roads.Status getSimStatus(int ts) {
    if (simData == null) {
      return status;
    }
    return simData[ts].getStatus();
  }
  /**
   * sets the simulation status of roads based on time step.
   * @param status
   */
  public void setSimStatus(Roads.Status status) {
    int ts = Simulation.getCurrentTimeStep();
    this.simData[ts].setStatus(status);
  }
/**
 * gets roads in adjacent areas and writes them
 * @param fout
 */
  public void exportNeighbors(PrintWriter fout) {
    if (neighbors == null) { return; }

    // unit, adj
    for (int i=0; i<neighbors.size(); i++) {
      fout.print(getId());
      fout.print(COMMA);
      fout.print(((Roads)neighbors.get(i)).getId());
      fout.println();
    }
  }
  /**
   * gets the adjacent vegetation units  and writes them 
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
   * writes out attributes in following order: slink(id), status, kind
   * @param fout
   */
  public void exportAttributes(PrintWriter fout) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);

    fout.print(getId());
    fout.print(COMMA);
    fout.print(getSimStatus());
    fout.print(COMMA);
    fout.print(getKind());
    fout.println();
  }
/**
 * writes to an external source, in order, version, status, kind, associated vegetative unit size and associative vegetative units id
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
   * read from an external source attributes in following order: version, status, kind, area (from current simulation), size, and associated vegetative unit size and id
   */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(in);

    int version = in.readInt();

    status = status.valueOf((String)in.readObject());
    kind   = kind.valueOf((String)in.readObject());

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

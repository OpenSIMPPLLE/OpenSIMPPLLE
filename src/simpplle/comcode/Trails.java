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
import java.text.NumberFormat;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Trails, a type of Manmade Element.
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.ManmadeElement
 *
 */

public class Trails extends ManmadeElement implements Externalizable {
  static final long serialVersionUID = -6495013117293386445L;
  static final int  version          = 1;
  static final int  simDataVersion   = 1;

  public enum Status { OPEN, CLOSED, PROPOSED, ELIMINATED, UNKNOWN }
  public enum Kind { HIKE, UNKNOWN }

  protected static final String COMMA = ",";

  private ArrayList<Evu> assocVegUnits = new ArrayList<>();

  private Status status;
  private Kind kind;

  private TrailsSimData[] simData;

  /**
   * Calls super constructor.
   */
  public Trails() {
    super();
  }

  /**
   * Calls super constructor with the given ID.
   * @param id A unique identifier
   */
  public Trails(int id) {
    super(id);
  }

  /**
   * Reallocates an array of simulation data. The size of the array equals the number of time steps in the current
   * simulation. The first entry is initialized with the status of this trail unit.
   */
  public void initSimulation() {

    int numSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    simData = new TrailsSimData[numSteps + 1];
    simData[0] = new TrailsSimData();
    simData[0].setStatus(status);

  }

  /**
   * Creates simulation data for the current time step with the status of the previous time step.
   */
  public void doBeginTimeStep() {

    int ts = Simulation.getCurrentTimeStep();

    simData[ts] = new TrailsSimData();
    simData[ts].setStatus(simData[ts - 1].getStatus());
  }

  /**
   * Returns the first associated vegetation unit.
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
  public ArrayList<Evu> getAssociatedVegUnits() {
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

  public Kind getKind() {
    return kind;
  }

  public Status getStatus() {
    return status;
  }

  public void setKind(Kind kind) {
    this.kind = kind;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * Returns the status of this trail at the current time step in the current simulation. If the simulation is not
   * running, the last time step is used.
   * @return Trail status
   */
  public Trails.Status getSimStatus() {
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
   * Returns the status of this trail at the requested time step in the current simulation.
   * @param ts Time step
   * @return Trail status
   */
  public Status getSimStatus(int ts) {
    if (simData == null) return status;
    return simData[ts].getStatus();
  }

  /**
   * Sets the status of this road at the current time step in the current simulation.
   * @param status Road status
   */
  public void setSimStatus(Status status) {
    int ts = Simulation.getCurrentTimeStep();
    simData[ts].setStatus(status);
  }

  /**
   * Writes neighboring trails as one pair of comma separated IDs per line. This ID, neighbor ID.
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
   * Writes the attributes of this trail unit at the current time step in the following order: slink(id), status, kind
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

    simData = new TrailsSimData[size];
    for (int i=0; i<size; i++) {
      simData[i] = (TrailsSimData)in.readObject();
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

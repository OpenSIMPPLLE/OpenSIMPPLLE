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
 * <p>This class defines Trails, one of two types of Manmade Elements (the other is Roads).
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.element.ManmadeElement
 *
 */


public class Trails extends simpplle.comcode.element.ManmadeElement implements Externalizable {
  static final long serialVersionUID = -6495013117293386445L;
  static final int  version          = 1;
  static final int  simDataVersion   = 1;

  public enum Status { OPEN, CLOSED, PROPOSED, ELIMINATED, UNKNOWN };
  public enum Kind { HIKE, UNKNOWN };

  protected static final String COMMA         = ",";

  private ArrayList<Evu> assocVegUnits = new ArrayList<Evu>();

  private Status status;
  private Kind   kind;

  // Simulation
  private TrailsSimData[] simData;
/**
 * Constructor for trails.  Inherits from Manmade Element superclass.  
 */
  public Trails() {
    super();
  }
  /**
   * Constructor for trails.  Inherits from Manmade Element superclass and passes in and ID
   * @param id
   */
  public Trails(int id) {
    super(id);
  }
/**
 * initializes a trails simulation data array of size = time steps
 */
  public void initSimulation() {
    int numSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    simData = new TrailsSimData[numSteps+1];
    simData[0] = new TrailsSimData();
    simData[0].setStatus(status);
  }

  public void doBeginTimeStep() {
    int ts = Simulation.getCurrentTimeStep();
    simData[ts] = new TrailsSimData();
    simData[ts].setStatus(simData[ts-1].getStatus());
  }

  /**
   * Get the first associated Vegetative unit.  Used in distance to trail
   * calculations. Not 100% accurate distance as the trail unit will cover
   * several evu's, but it will save a lot of computation time, in finding the
   * closest trail.
   * @return Evu
   */
  public Evu getFirstVegUnit() {
    if (assocVegUnits != null && assocVegUnits.size() > 0) {
      return assocVegUnits.get(0);
    }
    return null;
  }
  public ArrayList<Evu> getAssociatedVegUnits() { return assocVegUnits; }
  public void addAssociatedVegUnit(Evu evu)  {
    if (assocVegUnits.contains(evu) == false) {
      assocVegUnits.add(evu);
    }
  }

  public Trails.Kind getKind() {
    return kind;
  }

  public Trails.Status getStatus() {
    return status;
  }

  public void setKind(Trails.Kind kind) {
    this.kind = kind;
  }

  public void setStatus(Trails.Status status) {
    this.status = status;
  }
/**
 * gets the current simulation status, this is looked up by the current time step
 * @return
 */
  public Trails.Status getSimStatus() {
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
  public Trails.Status getSimStatus(int ts) {
    if (simData == null) {
      return status;
    }
    return simData[ts].getStatus();
  }

  public void setSimStatus(Trails.Status status) {
    int ts = Simulation.getCurrentTimeStep();
    this.simData[ts].setStatus(status);
  }

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
   * method to export the adjacent area vegetation unit id's in comma separated format.  
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
   * Attributes stored in the file in following order: slink (id), status, kind.  This is a comma separated format.  
   * @param fout
   */
  public void exportAttributes(PrintWriter fout) {
    // attributes in the file in the following order:
    // slink(id),status,kind
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
 * writes to an external source, in order, version, status, kind, assiciated vegetative units size, and associated vegetative units ID
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
   * reads from an external source.  Attributes are stored in file in following order: version, status, kind, size, 
   * and associated vegetative unit (evu) ID
   * 
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

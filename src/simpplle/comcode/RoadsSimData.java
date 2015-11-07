package simpplle.comcode;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Roads Simulation Data.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class RoadsSimData implements Externalizable {
  static final long serialVersionUID = 3189835244576017961L;
  static final int  version          = 1;

  private Roads.Status status;
/**
 * Constructor.  Inherits from java Object class.  
 */
  public RoadsSimData() {
    super();
  }
/**
 * Sets the status of the roads in the simulation data to a new status.   Choices are OPEN(O), CLOSED(C), PROPOSED(P), ELIMINATED(E), UNKNOWN(NIL), NONE(N)
 * @param newStatus
 */
  public void setStatus(Roads.Status newStatus) {
    this.status = newStatus;
  }
  /**
   * Gets the status of the roads.  Choices are OPEN(O), CLOSED(C), PROPOSED(P), ELIMINATED(E), UNKNOWN(NIL), NONE(N)
   * @return
   */
  public Roads.Status getStatus() {
    return status;
  }
/**
 * reads from an external source the roads status
 * @throws IOException and ClassNotFoundException caught in GUI
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    status = (Roads.Status) in.readObject();
  }
  /**
   * writes to an external source the road status
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(status);
  }
}




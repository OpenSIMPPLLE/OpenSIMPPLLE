package simpplle.comcode;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Externalizable;
/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Trails Simulation Data.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */


public class TrailsSimData implements Externalizable  {
    static final long serialVersionUID = -6804428826375799553L;
    static final int  version          = 1;

    private simpplle.comcode.element.Trails.Status status;
/**
 * Constructor for Trails simulation data.  Inherits from Java object superclass.
 * @see java.lang.Object.Object()
 */
    public TrailsSimData() {
      super();
    }
/**
 * Sets the simulation trails status.  
 * @param newStatus
 */
    public void setStatus(simpplle.comcode.element.Trails.Status newStatus) {
      this.status = newStatus;
    }
    /**
     * Gets this simulation trails status.  
     * @return
     */
    public simpplle.comcode.element.Trails.Status getStatus() {
      return status;
    }
/**
 * reads for an external source the trails status.  
 * @throws IOException and ClassNotFoundException caught in GUI
 */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
      int version = in.readInt();

      status = (simpplle.comcode.element.Trails.Status) in.readObject();
    }
    /**
     * writes to an external source  and trail status data
     */
    public void writeExternal(ObjectOutput out) throws IOException {
      out.writeInt(version);

      out.writeObject(status);
    }
}

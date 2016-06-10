package simpplle.comcode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p> Manmade element is the base class for roads and trails.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ManmadeElement implements Externalizable {

  static final long serialVersionUID = -4527534884546485333L;
  static final int  version = 1;

  protected int id;

  protected List<ManmadeElement> neighbors = new ArrayList<>();

  /**
   * Constructs a manmade element.
   */
  public ManmadeElement() {}

  /**
   * Constructs a manmade element with an ID.
   * @param id
   */
  public ManmadeElement(int id) {
    this.id = id;
  }

  /**
   * Returns the ID of this element.
   * @return The identifier of this element
   */
  public int getId() {
    return id;
  }

  /**
   * Copies neighbors from another manmade element.
   * @param source A manmade element
   */
  protected void copyFrom(ManmadeElement source) {
    neighbors = new ArrayList<>(source.neighbors);
  }

  /**
   * Writes this instance to a serialization stream.
   * @throws IOException caught in GUI
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeInt(id);
    if (neighbors == null) {
      out.writeInt(0);
    } else {
      out.writeInt(neighbors.size());
      for (ManmadeElement element : neighbors) {
        out.writeInt(element.getId());
      }
    }
  }

  /**
   * Reads attributes from a deserialization stream.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    id = in.readInt();

    Area area = Simpplle.getCurrentArea();

    int size = in.readInt();
    neighbors = new ArrayList<>(size);

    for (int i = 0; i < size; i++) {

      int id = in.readInt();

      if (this instanceof Roads) {
        Roads unit = area.getRoadUnit(id);
        if (unit == null) {
          unit = new Roads(id);
          area.addRoadUnit(unit);
        }
        neighbors.add(unit);
      }

      if (this instanceof Trails) {
        Trails unit = area.getTrailUnit(id);
        if (unit == null) {
          unit = new Trails(id);
          area.addTrailUnit(unit);
        }
        neighbors.add(unit);
      }
    }
  }
}

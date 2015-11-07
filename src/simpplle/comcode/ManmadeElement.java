package simpplle.comcode;

import java.io.*;
import java.util.ArrayList;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p> This class contains methods for manmade elements.  It is extended by the roads and trails classes.  
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */
public class ManmadeElement implements Externalizable {
  static final long serialVersionUID = -4527534884546485333L;
  static final int  version          = 1;

  protected int       id;
  protected ArrayList<ManmadeElement> neighbors; // NaturalElement instances
/**
 * Constructor for manmade element.  This constructor does not initialize any variables.
 */
  public ManmadeElement() {}
/**
 * Overloaded manmade element object constructor.  Gives the manmade element an Id, and creates a new manmade element arraylist which will contain
 * the neighboring manmade elements.  
 * @param id
 */
  public ManmadeElement(int id) {
    this.id = id;
    neighbors = new ArrayList<ManmadeElement>();
  }

  protected void copyFrom(ManmadeElement copyUnit) {
    neighbors = new ArrayList(copyUnit.neighbors);
  }
/**
 * writes to an external source the manmade element objects information in the following order, manmade element id, neighbors size, neighbor id
 * @throws IOException caught in GUI
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeInt(id);

    int size = neighbors != null ? neighbors.size() : 0;
    out.writeInt(size);
    for (int i=0; i<size; i++) {
      out.writeInt(neighbors.get(i).getId());
    }
  }
  /**
   * Reads an information about this manmade element object from an external source in following order: version, manmade element id, size of manmade
   * element neighbors arraylist size and the roads and trailsin it.  
   * The arraylist of manmade element objects contains roads and trails -as these are the only manmade elements OpenSimpplle tracks.  .
   */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int version = in.readInt();
    id        = in.readInt();

    Area area = Simpplle.getCurrentArea();
    int size = in.readInt();
    neighbors = new ArrayList<ManmadeElement>(size);
    for (int i=0; i<size; i++) {
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

  public int getId() {
    return id;
  }
}

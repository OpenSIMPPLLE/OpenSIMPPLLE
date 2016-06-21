package simpplle.comcode;

import java.io.*;

/**

 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates an AdjacentData object.  
 * Adjacent data contains Evu, wind, and position (above, below, or next to)
 *
 */
public class AdjacentData implements Externalizable {
  static final long serialVersionUID = 8588855941181047793L;
  static final int  version          = 2;

   /*
    * Deprecated EarthSIMPPLLE V1.0 - will no longer be supported
   * Position Values:  (legacy only, now calculated dynamically.
   *   A = Above
   *   B = Below
   *   N = Next to
   *   E = Use Elevation
   */

  Evu  evu;
  char position;
  char wind;
  int spread;
  int windSpeed;
  int windDirection;
  double slope;

  /**
   * Overloaded constructor for Keane spatial relations.
   * @param evu adjacent evu.
   * @param position appears to be deprecated? see above comment
   * @param wind valid values are 'D' (down wind) or 'N' (no wind)
   * @param spread Degrees Azimuth between the Adjacent polygons TODO: from source unit to adjacent?
   * @param windSpeed Integer speed value
   * @param windDirection Direction that the wind is coming TODO: from source unit to adjacent?
   * @param slope percent slope TODO: from source unit to adjacent?
   */
  public AdjacentData(Evu evu, char position, char wind, int spread, int windSpeed, int windDirection, double slope) {
    this.evu = evu;
    this.position = position;
    this.wind = wind;
    this.spread = spread;
    this.windSpeed = windSpeed;
    this.windDirection = windDirection;
    this.slope = slope;
  }

  /**
   * Overloaded constructor for Legacy spatial relations.
   */
  public AdjacentData(Evu evu,  char position,  char wind) {
    this.evu      = evu;
    this.position = position;
    this.wind     = wind;
  }

  /**
   * Default constructor.
   */
  public AdjacentData() {
    evu      = null;
    position = 'N';
    wind     = 'N';
  }

  /**
   * Method to read in Adjacent data objects.
   *@throws IOException
   *@throws ClassNotFoundException
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    if (version == 1) readV1(in);
    else if (version == 2) readV2(in);
  }

  /**
   * Read file version 1 (Legacy spatial relations)
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void readV1(ObjectInput in) throws IOException, ClassNotFoundException {
    evu      = (Evu)in.readObject();
    position = in.readChar();
    wind     = in.readChar();
  }

  /**
   * Read file version 2 (Keane spatial relations)
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void readV2(ObjectInput in) throws IOException, ClassNotFoundException {
    evu           = (Evu)in.readObject();
    position      = in.readChar();
    wind          = in.readChar();
    spread        = in.readInt();
    windSpeed     = in.readInt();
    windDirection = in.readInt();
  }

  /**
   * Writes Adjacent Data object to an external source
   * @throws IOException
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeObject(evu);
    out.writeChar(position);
    out.writeChar(wind);
    out.writeInt(spread);
    out.writeInt(windSpeed);
    out.writeInt(windDirection);
  }
}






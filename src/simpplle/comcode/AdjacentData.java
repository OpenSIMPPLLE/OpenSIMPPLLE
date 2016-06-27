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
  private char position;
  private char wind;
  private double spread;
  /**
   * Wind speed is in meters per second (m/s)
   */
  private double windSpeed;
  private double windDirection;
  /**
   *  percent slope from source to adjacent.
   */
  private double slope;

  /**
   * Overloaded constructor for Keane spatial relations.
   * @param evu adjacent evu.
   * @param position appears to be deprecated? see above comment
   * @param wind valid values are 'D' (down wind) or 'N' (no wind)
   * @param spread Degrees Azimuth between the Adjacent polygons
   * @param windSpeed speed value
   * @param windDirection Direction that the wind is coming
   */
  public AdjacentData(Evu evu, char position, char wind, double spread, double windSpeed, double windDirection) {
    this.evu = evu;
    this.position = position;
    this.wind = wind;
    this.spread = spread;
    this.windSpeed = windSpeed;
    this.windDirection = windDirection;
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
    spread        = in.readDouble();
    windSpeed     = in.readDouble();
    windDirection = in.readDouble();
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
    out.writeDouble(spread);
    out.writeDouble(windSpeed);
    out.writeDouble(windDirection);
  }

  public double getSlope() {
    return slope;
  }

  public void setSlope(double slope) {
    this.slope = slope;
  }

  public double getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(int windSpeed) {
    this.windSpeed = windSpeed;
  }

  public double getWindDirection() {
    return windDirection;
  }

  public void setWindDirection(int windDirection) {
    this.windDirection = windDirection;
  }

  public double getSpread() {
    return spread;
  }

  public void setSpread(int spread) {
    this.spread = spread;
  }

  public char getPosition() {
    return position;
  }

  public void setPosition(char position) {
    this.position = position;
  }

  public char getWind() {
    return wind;
  }

  public void setWind(char wind) {
    this.wind = wind;
  }

  public Evu getEvu() {
    return evu;
  }

  public void setEvu(Evu evu) {
    this.evu = evu;
  }
}






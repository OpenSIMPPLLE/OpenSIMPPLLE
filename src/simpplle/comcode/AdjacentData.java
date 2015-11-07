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
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class AdjacentData implements Externalizable {
  static final long serialVersionUID = 8588855941181047793L;
  static final int  version          = 1;

  
   /*
    * Deprecated EarthSIMPPLLE V1.0 - will no longer be supported
   * Position Values:  (legacy only, now caclulated dynamically.
   *   A = Above
   *   B = Below
   *   N = Next to
   *   E = Use Elevation
   */

  public simpplle.comcode.element.Evu evu;
  public char position;
  public char wind;

  /**
   * Default constructor.  Set Evu to null, position and wind to 'N'
   */
  public AdjacentData() {
    evu      = null;
    position = 'N';
    wind     = 'N';
  }

  /**
   * Overloaded constructor.  Creates an adjacent data object with Existing Vegetation Unit (evu), position, and wind
   * @param evu 
   * @param position
   * @param wind
   */
  public AdjacentData(simpplle.comcode.element.Evu evu, char position, char wind) {
    this.evu      = evu;
    this.position = position;
    this.wind     = wind;
  }
  
  /**
   * Method to read in Adjacent data objects.  These are stored in file as Evu object, position, wind.  
   *@throws IOException, ClassNotFoundException
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    evu      = (simpplle.comcode.element.Evu)in.readObject();
    position = in.readChar();
    wind     = in.readChar();
  }
  /**
   * Writes to an external source the Adjacent Data object, in the following order: evu object, position, and wind.
   * *@throws IOException
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(evu);
    out.writeChar(position);
    out.writeChar(wind);
  }
}






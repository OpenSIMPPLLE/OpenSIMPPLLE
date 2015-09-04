package simpplle.comcode;

import java.io.Externalizable;
import java.io.*;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class ConiferEncroachmentLogicData implements Externalizable {
  static final long serialVersionUID =  -2826593067720949335L;
  static final int  version          = 2;

  public static final int NONE = -1;

  private static int[] data=null;;
/**
 * Constructor for Conifer Encroachment logic data.  
 */
  public ConiferEncroachmentLogicData() {
  }
/**
 * Marks the system knowledge changed for Conifer encroachment.
 */
  private static void markChanged() {
    SystemKnowledge.markChanged(SystemKnowledge.CONIFER_ENCROACHMENT);
  }
  /**
   * Sets the system knowledge to not changed, but passing conifer encroachment as kind, and false as value.
   */
  private static void setNotChanged() {
    SystemKnowledge.setHasChanged(SystemKnowledge.CONIFER_ENCROACHMENT,false);

  }
  /**
   * Gets all the time values associated with conifer encroachment.
   * @return
   */
  public static int[] getTimeValues() {
    return data;
  }
/**
 * Sets the time values in int array.  If the time values in the parameter are of lesser count than current time values, will set the loop 
 * exit to the new time values length, or will keep the length of current data.  In other words the lowest length will be used.  
 * In loop if current values are not the same as new vaalues at same index changes sets this objects time values to the new time values at that index.
 * Then marks system changed.   
 * @param newValues
 */
  public static void setTimeValues(int[] newValues) {
    int length = (newValues.length < data.length) ? newValues.length : data.length;

    for (int i=0; i<length; i++) {
      if (data[i] != newValues[i]) { markChanged(); }
      data[i] = newValues[i];
    }
  }

  /**
   * 
   * This method gets the time value for a particular Conifer Encroachment.  
   * Uses the utility function to get acres and multiplies by a constant to create ranges to access time values stored in array.  
   * source code edit 9/20/13 - Brian Losi
   * 
   * unless otherwise specified default multiplier is base to power 2
   * @param acres 
   * @return
   */
  public static int getTimeValue(int acres) {
    int multiplier = Utility.pow(10,Area.getAcresPrecision());

    int five       =  5 * multiplier;
    int ten        = 10 * multiplier;
    int fifteen    = 15 * multiplier;
    int twenty     = 20 * multiplier;
    int twentyfive = 25 * multiplier;
    int thirty     = 30 * multiplier;
    int thirtyfive = 35 * multiplier;
    int forty      = 40 * multiplier;
    int fortyfive  = 45 * multiplier;
    int fifty      = 50 * multiplier;

    if (acres > 0 && acres <= five) {
      return data[0];
    }
    else if (acres > five && acres <= ten ) {
      return data[1];
    }
    else if (acres > ten && acres <= fifteen ) {
      return data[2];
    }
    else if (acres > fifteen && acres <= twenty ) {
      return data[3];
    }
    else if (acres > twenty && acres <= twentyfive ) {
      return data[4];
    }
    else if (acres > twentyfive && acres <= thirty ) {
      return data[5];
    }
    else if (acres > thirty && acres <= thirtyfive ) {
      return data[6];
    }
    else if (acres > thirtyfive && acres <= forty ) {
      return data[7];
    }
    else if (acres > forty && acres <= fortyfive ) {
      return data[8];
    }
    else if (acres > fortyfive && acres <= fifty ) {
      return data[9];
    }
    return data[10];
  }
/**
 * Reads from Conifer Encroachment objects from an external source. 
 *  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    // This correct a bug in the original version that failed to save
    // the correct size array.
    // Chances are no such file exists but need this because the buggy build
    // was released.
    if (version == 1) {
      int[] tmpData = (int[])in.readObject();
      data = new int[tmpData.length+1];
      for (int i=0; i<tmpData.length; i++) {
        data[i] = tmpData[i];
      }
      data[data.length-1] = tmpData[tmpData.length-1];
      data[data.length-2] = tmpData[tmpData.length-2];
    }
    else {
      data = (int[]) in.readObject();
    }
    setNotChanged();
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(data);
  }

  public static void read(JarInputStream stream) {
      try {
        ObjectInputStream s = new ObjectInputStream(stream);
        s.readObject();  // Don't need instance
      }
      catch (ClassNotFoundException ex) {
        ex.printStackTrace();
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
  }
  public static void save(JarOutputStream stream) {
    try {
      ObjectOutputStream s = new ObjectOutputStream(stream);
      s.writeObject(new ConiferEncroachmentLogicData());
      setNotChanged();
    }
    catch (IOException ex) {
    }
  }

}





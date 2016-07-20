/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.io.InputStream;

/**
 * This class contains methods for MyObjectInputStream.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see java.io.ObjectInputStream
 */

public class MyObjectInputStream extends ObjectInputStream {
  public MyObjectInputStream(InputStream in) throws IOException {
    super(in);
  }
  /**
   * In the 06Aug Thru 16Aug Simpplle releases the serialVerionUID for the class
   * simpplle.comcode.VegetativeType changed due to failure to set the
   * variable in previous version.  This variable is set to the value
   * it should have had before the 06Aug2004 release.  This code below is to
   * ensure that the 06Aug Thru 16Aug releases with the bad code can still be
   * read.
   * @return ObjectStreamClass
   * @throws IOException
   * @throws java.lang.ClassNotFoundException
   */
  protected ObjectStreamClass readClassDescriptor()
      throws IOException, ClassNotFoundException
  {
      ObjectStreamClass desc = super.readClassDescriptor();
      if ((desc.getName().equals("simpplle.comcode.VegetativeType")) &&
          (desc.getSerialVersionUID() == VegetativeType.badSerialVersionUID)) {
        return ObjectStreamClass.lookup(VegetativeType.class);
      }
      return desc;
  }

}
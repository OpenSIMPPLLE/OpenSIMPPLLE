/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.*;
import java.io.*;

/**
 * 
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This is an I/O class which creates an InputStream reader and a BufferedStream reader.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 *
 */


public class StreamGobbler extends Thread {
  InputStream is;
  String type;

  /**
   * Constructor for StreamGobbler
   * @param is inputstream 
   * @param type
   */
  public StreamGobbler(InputStream is, String type) {
    this.is = is;
    this.type = type;
  }

  /**
   * Method to create a buffered reader
   * @throws IOException
   */
  public void run() {
    try {
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line = null;
      while ((line = br.readLine()) != null)
        System.out.println(type + ">" + line);
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
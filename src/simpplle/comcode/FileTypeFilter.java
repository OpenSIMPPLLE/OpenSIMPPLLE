/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>Class to check Filenames.  
 * 
 * Uses java.IO.FilenameFilter interface: from javadoc:
 * Instances of classes that implement this interface are used to filter filenames. 
 * These instances are used to filter directory listings in the list method of class File, 
 * and by the Abstract Window Toolkit's file dialog component.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
  */

public class FileTypeFilter implements FilenameFilter {
  private String filter;

  /**
   * Initializes filter to "".
   */
  public FileTypeFilter() { filter = ""; }

  /**
   * Initializes the file filter to the provided parameter.
   * @param s the filename type filter. (e.g. "txt")
   */
  public FileTypeFilter(String s) { filter = s; }

  /**
   * Implements the accept method.  Checks to see if the parameter
   * name ends with the filter String provided in the class constructor.
   * @param dir a File, the directory.
   * @param name a String, the filename string.
   * @return true if ends with filter
   */
  public boolean accept(File dir, String name) {
    if (name.endsWith(filter)) return true;
    else return false;
  }
}

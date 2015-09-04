package simpplle.gui;



import java.io.File;

import javax.swing.filechooser.FileFilter;


/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class is an implementation of javax FileFilter (an abstract class).  It creates a customized way to handle files of a certain extension.
 * There is a series of constructors which often pass suffixes, extensions, and descriptions amongst themselves ending depending on the 
 * needs required for the file filter.   
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */
public class MyFileFilter extends javax.swing.filechooser.FileFilter {
  private String[] extensions;
  private String   description;
  private String[] suffixes;
/**
 * Constructor for MyFileFilter.  Sets the suffixes array to null.  
 */
  public MyFileFilter() {
    super();
    suffixes = null;
  }
/**
 * Overloaded MyFileFilter constructor.  Takes in a string which will become both the extension and description.  
 * @param str
 */
  public MyFileFilter(String str) {
    this(str, str + " Files");
  }
/**
 * Overloaded MyFileFilter constructor.  
 * @param extension string representing file extension, will be made into extension array.  
 * @param description the file description.  
 */
  public MyFileFilter(String extension, String description) {
    this(new String[] { extension },description);
  }
/**
 * Overloaded MyFileFilter constructor.  Sets the extensions array and description.  
 * @param extensions the string array of file extensions
 * @param description the string name of file description.  
 */
  public MyFileFilter(String[] extensions, String description) {
    this();
    this.extensions   = extensions;
    this.description = description;
  }
/**
 * Overloaded MyFileFilter constructor. Takes in a suffix and turns it into a string array. 
 * @param suffix the suffix of file, which will be made into an array.  
 * @param extension the file extension (example .txt)
 * @param description the file description. 
 */
  public MyFileFilter(String suffix, String extension, String description) {
    this(new String[] {suffix}, extension,description);
  }
  /**
   * Overloaded MyFileFilter constructor.  Takes in a string array of suffixes, and a string file extension which will be sent to another 
   * constructor to be turned into an extension array.  
   * @param suffixes array of string suffixes which will be set as this objects suffixes
   * @param extension file extensions to be made into an array in another consructor
   * @param description file description
   */
  public MyFileFilter(String[] suffixes, String extension, String description) {
    this(extension,description);
    this.suffixes = suffixes;
  }
/**
 * Checks whether the file pathname should be accepted.  True if should be accepted.  
 * Makes sure file is not null, file name is not null, extension is correct.  If file is a directory returns true.  
 * For extensions of file, will look between "." and end of extension string.  (example: .txt).  If file extension substring is in extensions array, returns true and breaks 
 * out of extension checking.  If false at end of checking, file will not be accepted.  
 * For suffixes. Returns true if suffix is null, but extesion correct.  Othwise will return true if there is a suffix.  
 */
  public boolean accept (File f) {
    if (f == null) { return false; }

    if (f.isDirectory()) { return true; }

    String name = f.getName();
    if (name == null) { return false; }

    int index = name.lastIndexOf(".");
    String ext = name.substring(index+1,name.length());

    boolean extensionCorrect = false;
    for (int j=0; j<extensions.length; j++) {
      if (extensions[j].equals(ext)) {
        extensionCorrect = true;
        break;
      }
    }
    if (!extensionCorrect) { return false; }

    if (suffixes == null && extensionCorrect) {
      return true;
    }
    else {
      for(int i=0; i<suffixes.length; i++) {
        index = name.lastIndexOf(suffixes[i]);
        if (index != -1) { return true; }
      }
    }
    return false;
  }
/**
 * Gets the file description.  
 */
  public String getDescription() { return description; }

  /**
   * Gets the file extension, which is only used in saving files (which only have one extension possibility)
   * @return
   */
  public String getExtension() { return extensions[0]; }

}

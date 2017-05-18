/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.lang.reflect.Array;
import java.util.jar.*;

/**
 * This class contains methods for a bunch of essential functions which fall under the category of file management like making file pathways, etc...
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 */
public final class Utility {
	/**
	 * Default constructor for Utility.  As may be expected in a utility class it has no variables.  
	 * The methods in this class are the reason for its existence.   
	 */
  private Utility () {}
/**
 * Makes a vector from a string array.  
 * @param strings
 * @return
 */
  public static Vector makeVector(String[] strings) {
    Vector v = new Vector();
    for(int i=0; i<strings.length; i++) {
      v.addElement(strings[i]);
    }
    return v;
  }
/**
 * Essentially Math.pow.  takes an int base^pow
 * @param base
 * @param power
 * @return
 */
  public static int pow(int base, int power) {
    int val = 1;
    for(int i=0;i<power;i++) { val *= base; }
    return val;
  }


 

  /**
   * This method checks the value of the
   * system property: simpplle.fire.spotting.
   * It its value is "disabled" the method will
   * return false, otherwise it returns true.
   * @return a boolean, true indicates fire spotting is enabled.
   */
  public static boolean getFireSpotting () {
    String prop = System.getProperty("simpplle.fire.spotting");

    if (prop == null) { return true; }

    return (prop.equalsIgnoreCase("disabled") == false);
  }

  /**
    * This method creates a pathname which uses the separator
    * found in jar files.  This method is used to create pathnames
    * of data files found in the knowledge.jar jar file.
    *
    * @return a String made from the input parameters.
    * @param dir a string containing a directory pathname.
    * @param path an array of strings, each of which is used to
    *        create a valid pathname.
    */
  public static String makePathname (String dir, String[] path) {
    StringBuffer strBuf = new StringBuffer(dir);

    for(int i=0;i<path.length;i++) {
      strBuf.append("/");
      strBuf.append(path[i]);
    }

    return strBuf.toString();
  }
/**
 * Makes the pathname for system to find file.  Example: From the Regional zone pathname for (homeDir, gisDir) will pass
 * ("knowledge/zones/sierra-nevada" , "gis").  This will make a pathname of "knowledge/zones/sierra-nevada/gis"
 * @param dir
 * @param path
 * @return the pathname used to locate file.  From above example the returned pathname is "knowledge/zones/sierra-nevada/gis"
 */
  public static String makePathname(String dir, String path) {
    return (dir + "/" + path);
  }
/**
 * Makes the pathname for system to find file.  
 * @param dir
 * @param path
 * @param kind
 * @return
 */
  public static String makePathname(String dir, String path, String kind) {
    return (dir + "/" + path + "." + kind);
  }
/**
 * makes the file pathname suffix from the string kind 
 * @param path
 * @param suffix
 * @param kind
 * @return
 */
  public static File makeSuffixedPathname(File path, String suffix,
                                          String kind) {
    String dir, name, str;
    int    end;

    dir  = path.getParent();
    name = path.getName();

    str = "." + kind.toLowerCase();
    if (name.toLowerCase().endsWith(str)) {
      end = name.lastIndexOf(str);
      name = name.substring(0,end);
    }
    name = name + suffix + str;

    return (new File(dir,name));
  }

  public static File makeNumberedPathname(File path, int num) {
    return makeNumberedPathname(path, num, null);
  }

  public static File makeNumberedPathname(File path, int num, String kind) {
    String dir  = path.getParent();
    String name = path.getName();

    if (kind == null) {
      return new File(dir, name + "-" + num);
    }
    else {  
      return new File(dir, name + "-" + num + "." + kind.toLowerCase());
    }
  }

  public static PrintWriter openPrintWriter(File filename, String ext)
  throws SimpplleError
  {
    return openPrintWriter(Utility.makeSuffixedPathname(filename,"",ext));
  }
/**
 * Uses a print writer to write a file that has been streamed in fileoutputstream into a GzipOutputStream.
 * @param outfile
 * @return
 * @throws SimpplleError
 */
  public static PrintWriter openPrintWriter(File outfile)
    throws SimpplleError
  {
    GZIPOutputStream out;
    PrintWriter      fout;

    try {
      out = new GZIPOutputStream(new FileOutputStream(outfile));
      fout = new PrintWriter(out);
    }
    catch (IOException e) {
      String msg = "Problems opening output file: " + outfile.toString();
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    return fout;
  }

  /**
   * This method sorts the keys of a hashtable by their value.
   * It sorts the keys in the hashtable in descending
   * order by the value in the hashtable.
   * @return a Integer array.
   * @param tbl is a Hashtable, (keys & values must of type Integer)
   */
  public static Integer[] sort(Hashtable tbl) {
    Integer[]   keys;
    int[]       values;
    Integer     val;
    boolean     swapped;
    int         i=0,j, tmp;
    Enumeration e;

    keys   = new Integer[tbl.size()];
    values = new int[keys.length];

    e = tbl.keys();
    while (e.hasMoreElements()) {
      keys[i]   = (Integer) e.nextElement();
      val       = (Integer) tbl.get(keys[i]);
      values[i] = val.intValue();
      i++;
    }

    for (i=keys.length-1; i>=0; i--) {
      swapped = false;
      for (j=0;j<i;j++) {
        if (values[j] < values[j+1]) {
          tmp = values[j];
          values[j]   = values[j+1];
          values[j+1] = tmp;

          val = keys[j];
          keys[j]   = keys[j+1];
          keys[j+1] = val;
          swapped = true;
        }
      }
      if (!swapped) {
        return keys;
      }
    }
    return null;
  }

  /**
   * A variation on the above sort function.
   */
  public static void sort(int[] key, int[] value) {
    boolean     swapped;
    int         i=0, j, val, tmp;

    for (i=key.length-1; i>=0; i--) {
      swapped = false;
      for (j=0;j<i;j++) {
        if (value[j] < value[j+1]) {
          tmp = value[j];
          value[j]   = value[j+1];
          value[j+1] = tmp;

          val = key[j];
          key[j]   = key[j+1];
          key[j+1] = val;
          swapped = true;
        }
      }
      if (!swapped) {
        return;
      }
    }
  }

/**
 * Method to sort an array of simpplle types.  
 * @param key the array of simpplle types to be sorted.  
 * @param value
 */
  public static void sort(SimpplleType[] key, int[] value) {
    boolean      swapped;
    int          i=0, j, tmp;
    SimpplleType val;

    for (i=key.length-1; i>=0; i--) {
      swapped = false;
      for (j=0;j<i;j++) {
        if (value[j] < value[j+1]) {
          tmp = value[j];
          value[j]   = value[j+1];
          value[j+1] = tmp;

          val = key[j];
          key[j]   = key[j+1];
          key[j+1] = val;
          swapped = true;
        }
      }
      if (!swapped) {
        return;
      }
    }
  }
/**
 * Method to sort arraylist of Evu's based on array 
 * @param key the arraylist of Evu's 
 * @param value
 */
  public static void sort(ArrayList<Evu> key, ArrayList<MyInteger> value) {
    boolean swapped;
    int     i=0, j, tmp;
    Evu     val;

    for (i=key.size()-1; i>=0; i--) {
      swapped = false;
      for (j=0;j<i;j++) {
        if (value.get(j).intValue() < value.get(j+1).intValue()) {
          tmp = value.get(j).intValue();
          value.get(j).setValue(value.get(j+1).intValue());
          value.get(j+1).setValue(tmp);

          val = key.get(j);
          key.set(j,key.get(j+1));
          key.set(j+1,val);
          swapped = true;
        }
      }
      if (!swapped) {
        return;
      }
    }
  }
/**
 * Sorts an evu array and int array
 * @param key
 * @param value
 */
  public static void sort(Evu[] key, int[] value) {
    boolean swapped;
    int     i=0, j, tmp;
    Evu     val;

    for (i=key.length-1; i>=0; i--) {
      swapped = false;
      for (j=0;j<i;j++) {
        if (value[j] < value[j+1]) {
          tmp = value[j];
          value[j]   = value[j+1];
          value[j+1] = tmp;

          val = key[j];
          key[j]   = key[j+1];
          key[j+1] = val;
          swapped = true;
        }
      }
      if (!swapped) {
        return;
      }
    }
  }

  /**
   * Sorts an array of strings in alphabetical order.
   */
  public static void sort(String[] str) {
    boolean     swapped;
    int         i=0, j;
    String      val, tmp;

    for (i=str.length-1; i>=0; i--) {
      swapped = false;
      for (j=0;j<i;j++) {
        if (str[j].compareTo(str[j+1]) > 0) {
          val      = str[j];
          str[j]   = str[j+1];
          str[j+1] = val;
          swapped  = true;
        }
      }
      if (!swapped) {
        return;
      }
    }
  }
/**
 * Makes and returns a vector of from a hashtables elements enumeration.  
 * @param ht
 * @return
 */
  public static Vector vectorKeys(Hashtable ht) {
    Vector keys = new Vector();

    for (Enumeration e = ht.keys(); e.hasMoreElements(); ) {
      Object obj = e.nextElement();
      keys.addElement(obj);
    }
    return keys;
  }

  public static Vector vectorValues(Hashtable ht) {
    Vector values = new Vector();

    for (Enumeration e = ht.keys(); e.hasMoreElements(); ) {
      values.addElement(ht.get(e.nextElement()));
    }
    return values;
  }
/**
 * Prints an array of objects.  If 
 * @param fout
 * @param data
 * @param delim 
 */
  public static void printArray(PrintWriter fout, Object[] data, String delim) {
    if (data == null) {
      fout.print("?");
      return;
    }
    for (int i=0; i<data.length; i++) {
      if (data[i] == null) { continue; }
      if (i != 0) { fout.print(delim); }
      fout.print(data[i].toString());
    }
  }

  /**
   * Produces an array of indices with random ordering.
   *
   * @param maxIndex the value of the last index
   * @return an array of maxIndex + 1 indices
   */
  public static int[] makeRandomIndexSequence(int maxIndex) {

    int[] sequence = new int[maxIndex + 1];

    // Create an ordered sequence of indices
    for (int i = 0; i < sequence.length; i++) {
      sequence[i] = i;
    }

    Random random = Simulation.getInstance().getRandom();

    // Swap the indices at two random locations
    for (int i = 0; i < maxIndex; i++) {
      int index1 = random.nextInt(maxIndex + 1);
      int index2 = random.nextInt(maxIndex + 1);
      int swap = sequence[index1];
      sequence[index1] = sequence[index2];
      sequence[index2] = swap;
    }

    return sequence;

  }

/**
 * turns spaces into underscores.  This is a method which will be needed on occassion for making column names
 * @param str
 * @return
 */
  public static String spacesToUnderscores(String str) {
    char[] newStr = new char[str.length()];
    char   c;

    for (int i=0; i<str.length(); i++) {
      c = str.charAt(i);
      if (c == ' ') { c = '_'; }
      newStr[i] = c;
    }
    return new String(newStr);
  }

  public static String underscoresToDashes(String str) {
    StringBuffer strBuf = new StringBuffer(str);
    return underscoresToDashes(strBuf);
  }
  /**
   * changes a space in a string from a string buffer into dashes 
   * @param str
   * @return
   */
  public static String underscoresToDashes(StringBuffer str) {
    char[] newStr = new char[str.length()];
    char   c;

    for (int i=0; i<str.length(); i++) {
      c = str.charAt(i);
      if (c == '_') { c = '-'; }
      newStr[i] = c;
    }
    return new String(newStr);
  }
/**
 * takes a string buffered string and replaces the dashes with underscores
 * @param str
 * @return a new string with underscores in place of dashes
 */
  public static String dashesToUnderscores(StringBuffer str) {
    char[] newStr = new char[str.length()];
    char   c;

    for (int i=0; i<str.length(); i++) {
      c = str.charAt(i);
      if (c == '-') { c = '_'; }
      newStr[i] = c;
    }
    return new String(newStr);
  }
/**
 * 
 * @param str
 * @return result of dashesToUnderscores
 */
  public static String dashesToUnderscores(String str) {
    StringBuffer strBuf = new StringBuffer(str);
    return dashesToUnderscores(strBuf);
  }

  public static String convertFileUrl(String str) {
    StringBuffer newStr = new StringBuffer();
    char   c;

    if (str.startsWith("file:")) {
      str = str.substring(5);
    }
    int index = str.indexOf("%20",0);
    while (index != -1) {
      newStr.append(str.substring(0,index));
      index = str.indexOf("%20",index+3);
    }
    return newStr.toString();
  }

  public static File makeUniqueLogFile(File prefix, String suffix) {
    // Create a log file name
    String dir  = prefix.getParent();
    String name = prefix.getName();

    int  n=1;
    File logFile = Utility.makeSuffixedPathname(prefix,suffix,"log");

    while (logFile.exists()) {
      logFile = Utility.makeSuffixedPathname(prefix,suffix + "-" + n++,"log");
    }

    return logFile;
  }
/**
 * strips extension from the file
 * @param file
 * @return a file 
 */
  public static File stripExtension(File file) {
    String dir  = file.getParent();
    String name = file.getName();

    int index = name.lastIndexOf(".");
    if (index == -1) { return file; }
    return new File(dir,name.substring(0,index));
  }

  public static String getFileExtension(File file) {
    String name = file.getName();
    int    index = name.lastIndexOf(".");
    if (index == -1) { return null; }

    return name.substring(index+1);
  }

  public static String preProcessInputLine(String line) {
    return preProcessInputLine(line,"?");
  }
  public static String preProcessInputLine(String line, String dummyValue) {
    // Get rid of ' marks.
    StringBuffer strBuf = new StringBuffer();
    int begin  = 0;
    int index = line.indexOf("'");
    while (index != -1) {
      strBuf.append(line.substring(begin,index));
      begin = index + 1;
      index = line.indexOf("'",(index+1));
    }
    strBuf.append(line.substring(begin));
    line = strBuf.toString();

    // Get rid of " marks.
    strBuf = new StringBuffer();
    begin  = 0;
    index = line.indexOf("\"");
    while (index != -1) {
      strBuf.append(line.substring(begin,index));
      begin = index + 1;
      index = line.indexOf("\"",(index+1));
    }
    strBuf.append(line.substring(begin));
    line = strBuf.toString();
    strBuf = null;

    // Replace instances of ",,", with dummyValue
    // As well as filling in the empty space at end of line
    strBuf = new StringBuffer();
    begin = 0;
    index = line.indexOf(",,");
    while (index != -1) {
      strBuf.append(line.substring(begin,index+1));
      strBuf.append(dummyValue);
      begin = index + 1;
      index = line.indexOf(",,",(index+1));
    }
    strBuf.append(line.substring(begin));
    if (line.charAt(line.length()-1) == ',') {
      strBuf.append(dummyValue);
    }

    return strBuf.toString();
  }

  /**
   * Add the elements of list2 into the elements of list1 if list1 does not
   * already contains the element.
   *
   * @param list1 ArrayList
   * @param list2 ArrayList
   * @return a sorted merging of list1 and list2 in list1
   */
  public static ArrayList combineLists(ArrayList list1, ArrayList list2) {
    for (Object item : list2) {
      if (list1.contains(item) == false) { list1.add(item); }
    }
    Collections.sort(list1);
    return list1;
  }
/**
 * Takes the acres which are stored in the system as an int and turns them into float acres.  
 * @param acresVal this is stored in the system as an int. 
 * @param decimalPlaces
 * @return the agreage as a float
 */
  public static float getFloatAcres(int acresVal, int decimalPlaces) {
    return ( (float)acresVal / (float)Utility.pow(10,decimalPlaces) );
  }

/*
   rows = GP.SearchCursor("D:/St_Johns/data.mdb/roads")
   row = rows.Next()
   # Calculate the total length of all roads
   length = 0
   while row:
       # Create the geometry object
       feat = row.shape
       centroid = feat.Centroid
       x = centroid.X
       y = centroid.Y
       row = rows.Next()

       # Describe feature class
       fc = "D:/St_Johns/data.mdb/roads"
       desc = GP.Describe(fc)
       # Get the spatial reference
       SR = desc.SpatialReference
       # Check if the feature class is in projected space
       if SR.Type == "Projected":
          GP.Copy(fc,"D:/St_Johns/data.mdb/UTM")
  */
  /**
   * Makes a latitude longitude distance.  
   * @param lat1
   * @param long1
   * @param lat2
   * @param long2
   * @return
   */
  public double latLongDistance(double lat1, double long1, double lat2, double long2) {
    double distance=0.0;
    double latMissoulaMontana = 46.872;
    double earthRadius=(3963 - (13 * Math.sin(Math.toRadians(latMissoulaMontana))));

    lat1  = Math.toRadians(lat1);
    long1 = Math.toRadians(long1);
    lat2  = Math.toRadians(lat2);
    long2 = Math.toRadians(long2);

    double diffLong = long2 - long1;
    double diffLat  = lat2 - lat1;

    double a = Math.sin(diffLat/2);
    double b = Math.sin(diffLong/2);
    double tmp = a*a + (Math.cos(lat1) * Math.cos(lat2) * b*b);
    double tmp2 = 2 * Math.asin(Math.min(1,Math.sqrt(tmp)));

    distance = earthRadius * tmp2;

    return distance;
  }
/**
 * Closes the Jar Input Steram.  
 * @param in
 */
  public static void close(JarInputStream in) {
    if (in != null) {
      try {
        in.close();
      }
      catch (IOException e) {}
    }
  }
/**
 * Closes the BufferedReader.  
 * @param in
 */
  public static void close(BufferedReader in) {
    if (in != null) {
      try {
        in.close();
      }
      catch (IOException e) {}
    }
  }


}





package simpplle.comcode;

import java.util.Hashtable;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppression Beyond A Data. This class is deprecated and will most likely be eliminated in 
 * OpenSimpplle v1.0
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @deprecated
 */

public class FireSuppressionBeyondAData {
  private static final int NF_WILDERNESS = FireEvent.NF_WILDERNESS;
  private static final int NF_OTHER      = FireEvent.NF_OTHER;
  private static final int OTHER         = FireEvent.OTHER;

  private static final int UNKNOWN       = Roads.Status.UNKNOWN.getValue();
  private static final int NONE          = Roads.Status.NONE.getValue();
  private static final int OPEN_ROADED   = Roads.Status.OPEN.getValue();
  private static final int CLOSED_ROADED = Roads.Status.CLOSED.getValue();

  private static boolean dataChanged;

  public static boolean hasDataChanged() { return dataChanged; }
  private static void setDataChanged(boolean value) { dataChanged = value; }

  private static void setDataFilename(File file) {
    SystemKnowledge.setFile(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A,file);
    SystemKnowledge.markChanged(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A);
  }

  public static void clearDataFilename() {
    SystemKnowledge.clearFile(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A);
  }

  // Key is ProcessType (SRF, MSF, LSF)
  // Value is boolean[ownership]
  private static Hashtable suppress = new Hashtable();

  // Key is ProcessType (SRF, MSF, LSF)
  // Value is boolean[ownership][roadstatus]
  private static Hashtable suppressRoad = new Hashtable();

  public static void initialize() {
    boolean[][] tmp;
    boolean[]   tmpOwn;

    // *** Stand Replacing Fire **
    tmp = new boolean[3][4];
    tmp[NF_WILDERNESS][NONE]          = false;
    tmp[NF_WILDERNESS][UNKNOWN]       = tmp[NF_WILDERNESS][NONE];
    tmp[NF_WILDERNESS][OPEN_ROADED]   = false;
    tmp[NF_WILDERNESS][CLOSED_ROADED] = false;

    tmp[NF_OTHER][NONE]          = false;
    tmp[NF_OTHER][UNKNOWN]       = tmp[NF_OTHER][NONE];
    tmp[NF_OTHER][OPEN_ROADED]   = false;
    tmp[NF_OTHER][CLOSED_ROADED] = false;

    tmp[OTHER][NONE]          = false;
    tmp[OTHER][UNKNOWN]       = tmp[OTHER][NONE];
    tmp[OTHER][OPEN_ROADED]   = false;
    tmp[OTHER][CLOSED_ROADED] = false;

    suppressRoad.put(ProcessType.STAND_REPLACING_FIRE,tmp);

    tmpOwn = new boolean[3];
    tmpOwn[NF_WILDERNESS] = true;
    tmpOwn[NF_OTHER]      = true;
    tmpOwn[OTHER]         = true;

    suppress.put(ProcessType.STAND_REPLACING_FIRE,tmpOwn);

    // *** Mixed Severity Fire **
    tmp = new boolean[3][4];
    tmp[NF_WILDERNESS][NONE]          = false;
    tmp[NF_WILDERNESS][UNKNOWN]       = tmp[NF_WILDERNESS][NONE];
    tmp[NF_WILDERNESS][OPEN_ROADED]   = false;
    tmp[NF_WILDERNESS][CLOSED_ROADED] = false;

    tmp[NF_OTHER][NONE]          = false;
    tmp[NF_OTHER][UNKNOWN]       = tmp[NF_OTHER][NONE];
    tmp[NF_OTHER][OPEN_ROADED]   = false;
    tmp[NF_OTHER][CLOSED_ROADED] = false;

    tmp[OTHER][NONE]          = false;
    tmp[OTHER][UNKNOWN]       = tmp[OTHER][NONE];
    tmp[OTHER][OPEN_ROADED]   = false;
    tmp[OTHER][CLOSED_ROADED] = false;

    suppressRoad.put(ProcessType.MIXED_SEVERITY_FIRE,tmp);

    tmpOwn = new boolean[3];
    tmpOwn[NF_WILDERNESS] = true;
    tmpOwn[NF_OTHER]      = true;
    tmpOwn[OTHER]         = true;

    suppress.put(ProcessType.MIXED_SEVERITY_FIRE,tmpOwn);

    // *** Light Severity Fire **
    tmp = new boolean[3][4];
    tmp[NF_WILDERNESS][NONE]          = false;
    tmp[NF_WILDERNESS][UNKNOWN]       = tmp[NF_WILDERNESS][NONE];
    tmp[NF_WILDERNESS][OPEN_ROADED]   = false;
    tmp[NF_WILDERNESS][CLOSED_ROADED] = false;

    tmp[NF_OTHER][NONE]          = false;
    tmp[NF_OTHER][UNKNOWN]       = tmp[NF_OTHER][NONE];
    tmp[NF_OTHER][OPEN_ROADED]   = false;
    tmp[NF_OTHER][CLOSED_ROADED] = false;

    tmp[OTHER][NONE]          = false;
    tmp[OTHER][UNKNOWN]       = tmp[OTHER][NONE];
    tmp[OTHER][OPEN_ROADED]   = false;
    tmp[OTHER][CLOSED_ROADED] = false;

    suppressRoad.put(ProcessType.LIGHT_SEVERITY_FIRE,tmp);

    tmpOwn = new boolean[3];
    tmpOwn[NF_WILDERNESS] = true;
    tmpOwn[NF_OTHER]      = true;
    tmpOwn[OTHER]         = true;

    suppress.put(ProcessType.LIGHT_SEVERITY_FIRE,tmpOwn);
  }

  /**
    * Load a user provided Fire Suppression data file.
    * @param path is a File, the pathname of the data file.
    * @return a boolean, true if file was successfully loaded.
    */
  public static void loadData(File path) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(path));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      read(fin);
      setDataFilename(path);
      fin.close();
      gzip_in.close();
    }
    catch (IOException e) {
      String msg = "Problems reading data file:" + path;
      throw new SimpplleError(msg);
    }
    catch (ParseError err) {
      String msg = "Problems reading data file:" + path + "\n" + err.msg;
      throw new SimpplleError(msg);
    }
  }

  public static void read(BufferedReader fin) throws IOException, ParseError {
    String              line=null, str;
    StringTokenizerPlus strTok;
    ProcessType         process;
    int                 owner, roadStatus;
    boolean[]           suppressBool;
    boolean[][]         suppressRoadsBool;
    final int           NUM_KEYS  = 3;
    final int           NUM_OWNER = FireEvent.getNumOwnership();
    final int           NUM_ROAD  = 4;

    suppress.clear();
    suppressRoad.clear();

    try {
      int k,o,r;
      for (k=0; k<NUM_KEYS; k++) {
        for (o=0; o<NUM_OWNER; o++) {
          line = fin.readLine();
          if (line == null) {
            throw new ParseError("Invalid, none, or improper data in file.");
          }
          strTok = new StringTokenizerPlus(line,",");

          str     = strTok.getStringToken();
          process = (str != null) ? ProcessType.get(str) : null;
          if (process == null) { throw new ParseError("Invalid process found:" + str); }

          str   = strTok.getStringToken();
          owner = FireEvent.findOwnership(str);

          suppressBool = (boolean[])suppress.get(process);
          if (suppressBool == null) {
            suppressBool = new boolean[NUM_OWNER];
            suppress.put(process,suppressBool);
          }
          suppressBool[owner] = Boolean.valueOf(strTok.getToken()).booleanValue();

          for (r=0; r<NUM_ROAD; r++) {
            line   = fin.readLine();
            if (line == null) {
              throw new ParseError("Invalid, none, or improper data in file.");
            }
            strTok = new StringTokenizerPlus(line,",");

            str     = strTok.getStringToken();
            process = (str != null) ? ProcessType.get(str) : null;
            if (process == null) { throw new ParseError("Invalid process found:" + str); }

            str   = strTok.getStringToken();
            owner = FireEvent.findOwnership(str);

            str        = strTok.getStringToken();
            roadStatus = Roads.Status.lookup(str).getValue();

            suppressRoadsBool = (boolean[][])suppressRoad.get(process);
            if (suppressRoadsBool == null) {
              suppressRoadsBool = new boolean[NUM_OWNER][NUM_ROAD];
              suppressRoad.put(process,suppressRoadsBool);
            }
            suppressRoadsBool[owner][roadStatus] = Boolean.valueOf(strTok.getToken()).booleanValue();
          }
        }

      }
      setDataChanged(false);
    }
    catch (ParseError err) {
      initialize();
      throw new ParseError("While reading Fire Suppression Beyond Class-A data file\n" +
                           "In line: " + line + "\n" + err.msg);
    }
  }

}



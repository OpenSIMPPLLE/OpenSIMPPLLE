package simpplle.comcode;

import java.io.*;
import java.util.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Restoration.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class Restoration {
  private static final int SPECIES    = 0;
  private static final int SIZE_CLASS = 1;
  private static final int DENSITY    = 2;

  private static final int NUM_PROBS = 2;

  private static final int NUM_KIND  = 3;
  private static final int NUM_FILES = 2;

  private static final String RESTORATION = "R";
  private static final String MAINTENANCE = "M";
  private static final String CONVERSION  = "C";

  private static File[][] infile = new File[3][2];
  private static File     outfile;
/**
 * Default constructor.  Instantiates class.  Initializes no variables. 
 */
  public Restoration() {}
/**
 * 
 * private ProgField.  Declares probability and name which can be name, density, or size class name
 *
 */
  private static class ProbField {
    public int    prob;
    public String name; 
/**
 * overloaded ProbField method.  Is public and sets the probability to -1
 */
    public ProbField() { prob = -1; }
  }
/**
 * Loops through the kinds of restoration and the probability files and the probabilities in those files and finds the max. 
 * @param fin
 * @param maxProbs
 * @param label
 * @return the max probability unless there is no or only one probabilty which by default returns the first probability
 * @throws SimpplleError for improper probability or slink, id mismatch or IO problems.  Caught in GUI
 */
  private static int findMaxProbabilities(BufferedReader[][] fin,
                                       ProbField[][][] maxProbs, Vector[][] label)
    throws SimpplleError
  {
    ProbField           current = new ProbField();
    ProbField           tmp;
    int                 kind, filenum, j, k;
    int                 count, tmpId, firstId = -1;
    String              line;
    simpplle.comcode.utility.StringTokenizerPlus strTok;

    for(kind=0;kind<NUM_KIND;kind++) {
      for(filenum=0;filenum<NUM_FILES;filenum++) {
        try {
          line = fin[kind][filenum].readLine();
          if (line == null) { return -1; }

          strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
          if (strTok.countTokens() < 2) {
            throw new SimpplleError("Not Enough Fields");
          }

          tmpId = strTok.getIntToken();
          if (kind == 0 && filenum == 0) { firstId = tmpId; }
          if (tmpId != firstId) {
            throw new SimpplleError("Id Mis-match while reading files");
          }

          for(k=0;k<NUM_PROBS;k++) {
            maxProbs[kind][filenum][k] = new ProbField();
          }

          count = strTok.countTokens();
          for(j=0;j<count;j++) {
            current.prob = Integer.parseInt((String)strTok.nextToken());
            current.name = (String)label[kind][filenum].elementAt(j);

            for(k=0;k<NUM_PROBS;k++) {
              if (current.prob > maxProbs[kind][filenum][k].prob) {
                tmp = maxProbs[kind][filenum][k];
                maxProbs[kind][filenum][k] = current;
                current = tmp;
              }
            }
          }
        }
        catch (NumberFormatException e) {
          throw new SimpplleError("Invalid probability found");
        }
        catch (ParseError err) {
          throw new SimpplleError("Invalid Slink Found");
        }
        catch (IOException err) {
          throw new SimpplleError("Problems reading from input files");
        }
      }
    }
    return firstId;
  }
/**
 * Loops through kinds of restoration and restoration files and adds the field names to the label vector. 
 * @param fin
 * @return a 2d Vector 
 * @throws SimpplleError 
 */
  private static Vector[][] readFieldNames(BufferedReader[][] fin) throws SimpplleError {
    int                 kind, filenum, count, j;
    String              line = null, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    Vector[][]          label = new Vector[NUM_KIND][NUM_FILES];

    for(kind=0;kind<3;kind++) {
      for(filenum=0;filenum<2;filenum++) {
        try {
          line = fin[kind][filenum].readLine();
          if (line == null) { throw new IOException(); }
        }
        catch (IOException err) {
          throw new SimpplleError("Error reading input file: " +
                                  infile[kind][filenum].toString());
        }
        strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
        str = (String) strTok.nextToken();
        if (str.equalsIgnoreCase("SLINK") == false) {
          throw new SimpplleError("Invalid input file: " +
                                  infile[kind][filenum].toString());
        }

        label[kind][filenum] = new Vector();
        count = strTok.countTokens();
        for(j=0;j<count;j++) {
          label[kind][filenum].addElement((String) strTok.nextToken());
        }
      }
    }
    return label;
  }
/**
 * Loops through the kinds of restoration and probability matches the kind name to find a match
 * @param maxProbs
 * @return a boolean array of matched kinds
 */
  private static boolean[] findMatches(ProbField[][][] maxProbs) {
    boolean[] kindMatch = new boolean[NUM_KIND];
    int     kind, j, k;

    for(kind=0;kind<NUM_KIND;kind++) {
      kindMatch[kind] = false;
      j = 0; k = 0;
      while (j < NUM_PROBS && kindMatch[kind] == false) {
        while (k < NUM_PROBS && kindMatch[kind] == false) {
          kindMatch[kind] =
            maxProbs[kind][0][j].name.equals(maxProbs[kind][1][k].name);
          k++;
        }
        j++;
      }
    }
    return kindMatch;
  }
  /**
   * Method to compare mixtures of species, density, and size class to decide if it is restoration, conversion, or maintenance. 
   */

  public static void compare(BufferedReader[][] fin, PrintWriter fout) throws SimpplleError {
    Vector[][]          label;
    ProbField[][][]     maxProbs = new ProbField[NUM_KIND][NUM_FILES][NUM_PROBS];
    boolean[]           kindMatch;
    int                 id;

    label = readFieldNames(fin);

    do {
      id = findMaxProbabilities(fin,maxProbs,label);
      if (id == -1) { continue; }

      kindMatch = findMatches(maxProbs);

      if (kindMatch[SPECIES] == false) {
        fout.println(id + "," + CONVERSION);
      }
      else if ((kindMatch[SPECIES] == true) &&
               (kindMatch[DENSITY] == true) &&
               (kindMatch[SIZE_CLASS]    == false)) {
        fout.println(id + "," + RESTORATION);
      }
      else if ((kindMatch[SPECIES] == true) &&
               (kindMatch[DENSITY] == false) &&
               (kindMatch[SIZE_CLASS]    == true)) {
        fout.println(id + "," + RESTORATION);
      }
      else if ((kindMatch[SPECIES] == true) &&
               (kindMatch[DENSITY] == false) &&
               (kindMatch[SIZE_CLASS]    == false)) {
        fout.println(id + "," + RESTORATION);
      }
      else if ((kindMatch[SPECIES] == true) &&
               (kindMatch[DENSITY] == true) &&
               (kindMatch[SIZE_CLASS]    == true)) {
        fout.println(id + "," + MAINTENANCE);
      }
      // Impossible, all cases covered above.
      else {
        fout.println(id + "," + "UNKNOWN");
      }
    }
    while (id != -1);

    fout.flush();
    fout.close();
  }

  private static File generateInputFile(File prefix, String kindStr) {
    String dir  = prefix.getParent();
    String name = prefix.getName();

    return new File(dir, name + "-" + kindStr + ".txt");
  }
/**
 * checks if species, size, and canopy file exists
 * @param prefix
 * @return true if generate input file exists for a particular species, size, or canopy (density) exist.  
 */
  public static boolean doFilesExist(File prefix) {
    File file;

    file = generateInputFile(prefix, "species");
    if (file.exists() == false) { return false; }

    file = generateInputFile(prefix, "size");
    if (file.exists() == false) { return false; }

    file = generateInputFile(prefix, "canopy");
    if (file.exists() == false) { return false; }

    return true;
  }
/**
 * 
 * @param prefix1
 * @param prefix2
 * @param outfile the file to be written to
 * @throws SimpplleError caught in GUI
 */
  public static void doReport(File prefix1, File prefix2, File outfile)
    throws SimpplleError
  {
    infile[SPECIES][0]    = generateInputFile(prefix1, "species");
    infile[SPECIES][1]    = generateInputFile(prefix2, "species");

    infile[SIZE_CLASS][0] = generateInputFile(prefix1, "size");
    infile[SIZE_CLASS][1] = generateInputFile(prefix2, "size");

    infile[DENSITY][0]    = generateInputFile(prefix1, "canopy");
    infile[DENSITY][1]    = generateInputFile(prefix2, "canopy");

    BufferedReader[][] fin = new BufferedReader[NUM_KIND][NUM_FILES];
    PrintWriter          fout;

    try {
      for(int kind=0;kind<NUM_KIND;kind++) {
        fin[kind][0] = new BufferedReader(new FileReader(infile[kind][0]));
        fin[kind][1] = new BufferedReader(new FileReader(infile[kind][1]));
      }
      fout = new PrintWriter(new FileOutputStream(outfile));
      compare(fin,fout);

      for(int kind=0;kind<NUM_KIND;kind++) {
        fin[kind][0].close();
        fin[kind][1].close();
      }
    }
    catch (IOException err) {
      throw new SimpplleError("Problems opening data files");
    }

  }

}

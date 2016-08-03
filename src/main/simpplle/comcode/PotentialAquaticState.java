/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * This class has fields which hold Aquatic pathway state
 * information and methods to configure possible aquatic states.  There are also a number of methods primarily
 * dealing with reading pathway information from a file as well
 * as accessor methods.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class PotentialAquaticState {
  private LtaValleySegmentGroup group;
  private AquaticClass          aquaticClass;
  private AquaticAttribute      attribute;

  private Hashtable nextState;
  private Hashtable probability;
  private Hashtable positions;

  private static final String DELIM     = ",";
  private static final String LISTDELIM = ":";
  private static final char   NODATA    = '?';
  public static final String SLASH     = "/";
  private static final String DASH      = "-";

  private static final String XY_COORDINATE           = "XY-COORDINATE";
  private static final String POTENTIAL_AQUATIC_STATE = "POTENTIAL-AQUATIC-STATE";

  private static final int INIT_X = 40;
  private static final int INIT_Y = 40;

  /**
   * Primary constructor for Potential Aquatic State.  Initializes hash tables for next state, positions, and probability.  
   */
  public PotentialAquaticState() {
    nextState   = new Hashtable(10);
    probability = new Hashtable(10);
    positions   = new Hashtable(10);
  }
  /**
   * This constructor takes a LtaValleySegmentGroup as an argument,
   * which is stored in a class field, it also calls the default
   * constructor.
   * @param group is a LtaValleySegmentGroup instance.
   */

  public PotentialAquaticState(LtaValleySegmentGroup group) {
    this();
    this.group = group;
  }
/**
 * Overloaded constructor to take the name of state from string.
 * @param group is a LtaValleySegmentGroup instance
 * @param state the aquatic state
 * @throws ParseError caught in GUI
 */
  public PotentialAquaticState(LtaValleySegmentGroup group, String state)
    throws ParseError
  {
    this();
    this.group = group;
    parseStateString(state);
  }
/**
 * Overloaded constructor used to create a temporary instance to store an Eau after import of an invalid state.  
 * The invalid state needs to be stored so it can be corrected later.
 * @param aquaticClass
 * @param attribute
 */
  public PotentialAquaticState(AquaticClass aquaticClass, AquaticAttribute attribute) {
    this.aquaticClass = aquaticClass;
    this.attribute    = attribute;
    group             = null;
    nextState         = null;
    probability       = null;
    positions         = null;
  }
/**
 * Adds some aquatic process next states for stream development, debris event, ten year flood, fifty year flood, one hundred year flood, minor vegetative disturbance
 * severe vegetative disturbance, vegetative replacement and x and y coordinates for the aquatic state
 * @param group
 * @param aquaticClass
 * @param attribute
 */
  public PotentialAquaticState(LtaValleySegmentGroup group,
                               AquaticClass aquaticClass, AquaticAttribute attribute)
  {
    this(group);
    this.aquaticClass = aquaticClass;
    this.attribute    = attribute;

    RegionalZone zone = Simpplle.getCurrentZone();
    addProcessNextState(Process.findInstance(ProcessType.STREAM_DEVELOPMENT),this);
    addProcessNextState(Process.findInstance(ProcessType.DEBRIS_EVENT),this);
    addProcessNextState(Process.findInstance(ProcessType.TEN_YEAR_FLOOD),this);
    addProcessNextState(Process.findInstance(ProcessType.FIFTY_YEAR_FLOOD),this);
    addProcessNextState(Process.findInstance(ProcessType.ONE_HUNDRED_YEAR_FLOOD),this);
    addProcessNextState(Process.findInstance(ProcessType.MINOR_VEG_DISTURB),this);
    addProcessNextState(Process.findInstance(ProcessType.SEVERE_VEG_DISTURB),this);
    addProcessNextState(Process.findInstance(ProcessType.VEG_REPLACEMENT),this);
    setAquaticClassPosition(aquaticClass,new Point(INIT_X,INIT_Y));
  }
/**
 * Gets the point position of an aquatic class.  If there is no current point, will create a default Point (10,10) 
 * @param tmpAquaticClass the key into the positions hashtable
 * @return the x,y position coordinates 
 */
  public Point getAquaticClassPosition(AquaticClass tmpAquaticClass) {
    Point tmp = (Point) positions.get(tmpAquaticClass);

    if (tmp == null) {
      tmp = new Point(10,10);
    }
    return tmp;
  }
/**
 * Sets the (x,y) coordinates for an aquatic class.  
 * @param tmpAquaticClass the aquatic class at passed in position
 * @param newPosition the (x,y) coordinates to be set.  
 */
  public void setAquaticClassPosition(AquaticClass tmpAquaticClass, Point newPosition) {
    positions.put(tmpAquaticClass, newPosition);
    group.markChanged();
  }

  /**
   * Gets the next state probability associated with the given
   * Process argument.
   * @param p is a Process.
   * @return an int, the next state probability for Process p.
   */
  public int getProcessProbability(Process p) {
    Integer prob;

    prob = (Integer) probability.get(p);
    if (prob == null) {
      return 0;
    }
    else {
      return prob.intValue();
    }
  }

  /**
   * Gets the next state (a PotentialAquaticState) associated with
   * Process p.
   * @param p is a Process.
   * @return the next state for Process p.
   */
  public PotentialAquaticState getProcessNextState(Process p) {
    return (PotentialAquaticState) nextState.get(p);
  }
/**
 * Adds a process to the next state
 * @param p
 * @param newNextState
 */
  public void addProcessNextState(Process p, PotentialAquaticState newNextState) {
    probability.put(p,new Integer(0));
    setProcessNextState(p,newNextState);
  }
/**
 * First sets the probability of Potential Aquathabitic State into the probability hash table if not already there.  
 * Then puts the process and new potential aquatic state into the next state hash table.  Marks the LtaValleySegmentGroup group changed.  
 * @param p the process
 * @param newNextState the potential next aquatic state
 */
  public void setProcessNextState(Process p, PotentialAquaticState newNextState) {
    if (probability.get(p) == null) {
      probability.put(p,new Integer(0));
    }

    nextState.put(p, newNextState);
    group.markChanged();
  }
/**
 * Removes a process from the next state hash table. 
 * @param p
 */
  public void removeProcessNextState(Process p) {
    nextState.remove(p);
  }
/**
 * Gets the process in the next state from the next state hash table.  
 * @param vt potential aquatic state.  
 * @return 
 */
  public Process getNextStateProcess(PotentialAquaticState vt) {
    PotentialAquaticState ns;
    Enumeration    keys = nextState.keys();
    while (keys.hasMoreElements()) {
      Process p = (Process) keys.nextElement();
      ns = (PotentialAquaticState) nextState.get(p);
      if (ns == vt) {
        return p;
      }
    }
    return null;
  }
/**
 * Gets all the processes associated with Potential Aquatic State.  
 * @return
 */
  public Process[] getProcesses() {
    Enumeration e = nextState.keys();
    Process[]   processes = new Process[nextState.size()];

    int i=0;
    while (e.hasMoreElements()) {
      processes[i] = (Process)e.nextElement();
      i++;
    }
    return processes;
  }

  /**
   * Gets the LtaValleySegmentGroup of which this PotentialAquaticState
   * is a member.
   * @return a LtaValleySegmentGroup.
   */
  public LtaValleySegmentGroup getLtaVsGroup() {
    return group;
  }

  /**
   * Creates a current state from the Aquatic Class and Aquatic Attributes
   * of the PotentialAquaticState and return it as a string.
   * @return a String, the print representation of this PotentialAquaticState.
   */
  public String getCurrentState() {
    return (aquaticClass.toString() + "-" +  attribute.toString());
  }
/**
 * Gets the current state object and prints it.  
 * @param fout
 */
  public void printCurrentState(PrintWriter fout) {
    fout.print(getCurrentState());
  }

  /**
   * Gets the Aquatic Class.
   * @return a AquaticClass, the Aquatic Class.
   */
  public AquaticClass getAquaticClass() {
    return aquaticClass;
  }

  public AquaticAttribute getAttribute() {
    return attribute;
  }

  /**
   * Gets the Aquatic Attribute.
   * @return a AquaticAttribute.
   */
  public AquaticAttribute getAquaticAttribute() {
    return attribute;
  }

  private void parseStateString(String stateStr)
    throws ParseError
  {
    StringTokenizerPlus strTok = new StringTokenizerPlus(stateStr,DASH);
    RegionalZone zone = Simpplle.getCurrentZone();

    if (strTok.countTokens() != 2) {
      throw new ParseError(stateStr + " is not a valid Potential Aquatic State");
    }

    aquaticClass = AquaticClass.get(strTok.nextToken());
    String attStr = strTok.nextToken();
    attribute = AquaticAttribute.get(attStr);
    if (attribute == null) {
      attribute = new AquaticAttribute(attStr);
    }

    if (aquaticClass == null) {
      String msg =
        stateStr + " is not a valid Potential Aquatic State.\n" +
        "Aquatic Class portion is not valid";
      throw new ParseError(msg);
    }
    if (attribute == null) {
      String msg =
        stateStr + " is not a valid Potential Aquatic State.\n" +
        "Attribute portion is not valid";
      throw new ParseError(msg);
    }
  }


  // Change the value in the nextState hashtable
  // from a String to a PotentialAquaticState object.
  /**
   * This method changes the value of the nextState hashtable
   * from String's to PotentialAquaticState references.  When a pathway
   * data file is loaded the PotentialAquaticState object that the
   * String reprsentation refers to may not yet have been loaded.
   * As a result, it is necessary to associate the String with
   * Process keys, in the hashtable until all pathway states in
   * a given Habitat Type Group have been loaded.  Once all the
   * PotentialAquaticState instance have been created this method is
   * called to replace the Strings with the appropriate PotentialAquaticState
   * references.
   * @see simpplle.comcode.LtaValleySegmentGroup
   * @exception simpplle.comcode.ParseError
   *            This indicates that an error occurred while
   *            attempting to read a pathway state from the
   *            input file.
   */
  public void fixNextState(StringBuffer log) throws ParseError {
    Enumeration           e;
    Process               process;
    String                state;
    PotentialAquaticState newState;
    boolean               errorsFound = false;

    e = nextState.keys();
    while (e.hasMoreElements()) {
      process = (Process) e.nextElement();
      state   = (String) nextState.get(process);

      newState = group.getPotentialAquaticState(state);
      if (newState == null) {
        log.append("Invalid Process Next State: " + "POTENTIAL-AQUATIC-STATE " +
                   getCurrentState() + "   " + process + " " + state +
                   Simpplle.endl);
        errorsFound = true;
        continue;
      }
      nextState.remove(process);
      nextState.put(process,newState);
    }
    if (errorsFound) {
      throw new ParseError("POTENTIAL-AQUATIC-STATE " + getCurrentState() +
                           " has errors.  See log for details");
    }
  }

  // ** Start Parser Stuff **
  // ------------------------

  public String importPotentialAquaticState(BufferedReader fin)
    throws ParseError
  {
    String              line = "", firstValue;
    StringTokenizerPlus strTok;
    int                 count;
    Process             process;
    RegionalZone        zone = Simpplle.getCurrentZone();

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }
        if (line.trim().length() == 0) { continue; }

        strTok = new StringTokenizerPlus(line);
        count  = strTok.countTokens();
        firstValue = strTok.nextToken().toUpperCase();

        if (count == 2 && firstValue.equals(POTENTIAL_AQUATIC_STATE)) {
          return line;
        }
        else if (count == 3 && firstValue.equals(XY_COORDINATE)) {
          importPositions(strTok);
        }
        else {
          process = Process.findInstance(ProcessType.get(firstValue));
          if (process == null) {
            throw new ParseError("Invalid Process found in line: " + line);
          }
          else if (count != 2 && count != 3) {
            throw new ParseError("Incorrect number of fields in line: " + line);
          }
          else {
            importProcessNextState(firstValue, strTok);
          }
        }
      }
      while (line != null);
    }
    catch (ParseError pErr) {
      String msg =
          "An error occurred in this line: " + line + "\n" + pErr.msg;
      throw new ParseError(msg);
    }
    catch (IOException err) {
      throw new ParseError("Problems reading input file");
    }
    return null;
  }

  private void importPositions(StringTokenizerPlus strTok)
   throws ParseError {
    String       str;
    AquaticClass posAquaticClass;

    posAquaticClass = AquaticClass.get(strTok.nextToken());

    StringTokenizerPlus posStrTok = new StringTokenizerPlus(strTok.nextToken(),",");
    if (posStrTok.countTokens() != 2) {
      throw new ParseError("Invalid positions found");
    }
    int x = posStrTok.getIntToken();
    int y = posStrTok.getIntToken();
    if (x == -1 || y == -1) {
      throw new ParseError("Invalid positions found");
    }
    positions.put(posAquaticClass,new Point(x,y));
  }

  private void importProcessNextState(String processName, StringTokenizerPlus strTok)
    throws ParseError
  {
    RegionalZone        zone = Simpplle.getCurrentZone();
    Process             process;
    String              nextStateStr;
    int                 nextStateProb;

    process       = Process.findInstance(ProcessType.get(processName));
    if (process == null) {
      throw new ParseError("Invalid process found: " + processName);
    }
    nextStateStr  = strTok.nextToken();
    nextStateProb = strTok.getIntToken();

    if (nextStateProb == -1) {
      throw new ParseError("Invalid probability found");
    }
    nextState.put(process,nextStateStr);
    probability.put(process,new Integer(nextStateProb));
  }

  private void readProcessNextState(StringTokenizerPlus strTok)
    throws ParseError
  {
    String              value = null;
    Process             process;
    String              state;
    Integer             prob;
    StringTokenizerPlus listStrTok;
    int                 count = -1;
    RegionalZone        zone = Simpplle.getCurrentZone();

    try {
      value = strTok.getToken();
      listStrTok = new StringTokenizerPlus(value,LISTDELIM);

      count = listStrTok.countTokens();

      // All valid states have at least one
      // process next state (i.e. STREAM-DEVELOPMENT).
      if ( (count % 3) != 0 || count == 0) {
        throw new ParseError("Not enough elements in next state list.");
      }

      for(int i=0;i<count;i+=3) {
        value   = listStrTok.nextToken();
        process = Process.findInstance(ProcessType.get(value));
        if (process == null) {
          throw new ParseError(value + " is not a valid process name.");
        }

        state   = listStrTok.nextToken();
        prob    = Integer.valueOf(listStrTok.nextToken());

        nextState.put(process,state);
        probability.put(process,prob);
      }
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid probability value.");
    }
  }

  private void readPositions(StringTokenizerPlus strTok) throws ParseError {
    String              value = null;
    AquaticClass        posAquaticClass;
    int                 x,y;
    int                 count;
    StringTokenizerPlus listStrTok;
    RegionalZone        zone = Simpplle.getCurrentZone();

    try {
      value = strTok.getToken();
      listStrTok = new StringTokenizerPlus(value,LISTDELIM);

      count = listStrTok.countTokens();

      // All valid states have at least one
      // position. (i.e. the position of itself);
      if ( (count % 3) != 0 || count == 0) {
        throw new ParseError("Not enough elements in Positions list.");
      }

      for(int i=0;i<count;i+=3) {
        value           = listStrTok.nextToken();
        posAquaticClass = AquaticClass.get(value);
        x               = Integer.parseInt(listStrTok.nextToken());
        y               = Integer.parseInt(listStrTok.nextToken());

        // There are numerous invalid Aquatic Classes in the position lists,
        // most likely caried over from the original lisp files.
        // This weeds those problems out.
        if (posAquaticClass == null) {
          continue;
        }
        positions.put(posAquaticClass,new Point(x,y));
      }
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid positions coordinate value.");
    }
  }

  /**
   * This method reads an processes a line from the input
   * pathway data file.  The line contains all the information
   * for the PotentialAquaticState state.  The input line is processes
   * and fields are initialized.
   * @exception simpplle.comcode.ParseError
   *            Indicates that an error occurred while parsing
   *            the line of data from the input file.
   */
  public void readData(BufferedReader fin) throws ParseError {
    String              line,value;
    StringTokenizerPlus strTok;

    try {
      line = fin.readLine().trim();
      if (line == null) {
        throw new ParseError ("Not enough Pathway records in file.");
      }

      strTok = new StringTokenizerPlus(line,DELIM);
      String str = strTok.getToken();

      aquaticClass = AquaticClass.get(str);
      if (aquaticClass == null) {
        aquaticClass = new AquaticClass(str);
      }

      str = strTok.getToken();
      attribute = AquaticAttribute.get(str);
      if (attribute == null) {
        attribute = new AquaticAttribute(str);
      }

      readProcessNextState(strTok);
      readPositions(strTok);
    }
    catch (ParseError PE) {
      throw new ParseError ("Error Potential Aquatic State " +
                            getCurrentState() + " " +
                            PE.msg);
    }
    catch (IOException IOX) {
      throw new ParseError ("Could not read a line in Potential Aquatic State "
                            + getCurrentState());
    }
  }

  public void save(PrintWriter fout) {
    fout.print(this.getAquaticClass().toString() + "," +
               this.getAquaticAttribute().toString());
    saveProcessNextState(fout);
    savePositions(fout);
  }

  private void saveProcessNextState(PrintWriter fout) {
    fout.print(",");

    Enumeration e = nextState.keys();
    Process     process;
    while (e.hasMoreElements()) {
      process = (Process) e.nextElement();
      fout.print(process + ":" +
                 (PotentialAquaticState)nextState.get(process) + ":" +
                 (Integer)probability.get(process));
      if (e.hasMoreElements()) { fout.print(":"); }
    }
  }

  private void savePositions(PrintWriter fout) {
    fout.print(",");

    Enumeration  e = positions.keys();
    AquaticClass posAquaticClass;
    Point        p;
    while (e.hasMoreElements()) {
      posAquaticClass = (AquaticClass) e.nextElement();
      p               = (Point) positions.get(posAquaticClass);
      fout.print(posAquaticClass + ":" + p.x + ":" + p.y);
      if (e.hasMoreElements()) { fout.print(":"); }
    }
    fout.println();
  }

  // ** End Parser Stuff **
  // ----------------------

  /**
   * Creates a String representation of this Potential Aquatic State.
   * @return a String.
   */
  public String toString() {
    return getCurrentState();
  }

  public void export(PrintWriter fout) {
    fout.println(POTENTIAL_AQUATIC_STATE + " " + getCurrentState());

    Process               process;
    PotentialAquaticState state;
    Integer               prob;
    Enumeration           keys = nextState.keys();
    while (keys.hasMoreElements()) {
      process = (Process) keys.nextElement();
      state   = (PotentialAquaticState) nextState.get(process);
      prob    = (Integer) probability.get(process);
      fout.print("  " + Formatting.fixedField(process.toString(),25,true) + " ");
      fout.print(Formatting.fixedField(state.toString(),36,true) + " ");
      fout.println(Formatting.fixedField(prob,3));
    }

    AquaticClass posAquaticClass;
    Point p;
    keys = positions.keys();
    while (keys.hasMoreElements()) {
      posAquaticClass = (AquaticClass) keys.nextElement();
      p               = (Point) positions.get(posAquaticClass);

      fout.println("  " + XY_COORDINATE + " " + posAquaticClass + " " + p.x + "," + p.y);
    }
    fout.println();
  }

  public void setAquaticClass(AquaticClass aquaticClass) {
    this.aquaticClass = aquaticClass;
  }

  public void setAttribute(AquaticAttribute attribute) {
    this.attribute = attribute;
  }

}




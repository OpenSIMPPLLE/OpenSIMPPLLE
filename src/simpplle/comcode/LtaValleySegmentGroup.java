package simpplle.comcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Lta Valley Segment Group type..  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */


public class LtaValleySegmentGroup {
  public static final String FILE_EXT = "aquapath";

  private LtaValleySegmentGroupType groupType;
  private Hashtable                 states;
//  private boolean                   systemGroup;
  private File                      inputFile;
  private boolean                   changed = false;
  private boolean                   isUserData = false;

  private static Hashtable groups = new Hashtable();

  private static String KEYWORD[] = {"CLASS","END", "NAME", "SYSTEM",
                                     "LTA-VALLEY-SEGMENT-GROUP",
                                     "ALL-POTENTIAL-AQUATIC-STATES",
                                     "POTENTIAL-AQUATIC-STATE"};

  private static final int CLASS                        = 0;
  private static final int END                          = 1;
  private static final int NAME                         = 2;
  private static final int SYSTEM                       = 3;
  private static final int LTA_VALLEY_SEGMENT_GROUP     = 4;
  private static final int ALL_POTENTIAL_AQUATIC_STATES = 5;
  private static final int POTENTIAL_AQUATIC_STATE      = 6;

  private static final int EOF                          = 7;

  public LtaValleySegmentGroup () {
    groupType   = null;
    states      = null;
//    systemGroup = false;
  }

  // Used when creating a new area.  If the group does not exist
  // we need to create one to store in the evu so that it can be
  // corrected the user later on.
  public LtaValleySegmentGroup (String newName) {
    groupType = LtaValleySegmentGroupType.get(newName);
    if (groupType == null) {
      groupType = new LtaValleySegmentGroupType(newName);
    }
    groups.put(groupType,this);

    states = null;
  }

  public static boolean loaded() {
    return (groups.size() > 0);
  }
  public static LtaValleySegmentGroup findInstance(String groupName) {
    return findInstance(LtaValleySegmentGroupType.get(groupName));
  }
  public static LtaValleySegmentGroup findInstance(LtaValleySegmentGroupType groupType) {
    return (LtaValleySegmentGroup)groups.get(groupType);
  }

  public boolean isValid() { return states != null; }

  public boolean isSystemGroup() {
    return ( (groupType.isUserGroup() == false) || (!isUserData));
  }
  public LtaValleySegmentGroupType getType() { return groupType; }

  public static void clearGroups() {
    groups.clear();
  }

  public static void removeGroup(LtaValleySegmentGroup group) {
    groups.remove(group);
  }
  public static void removeGroup(String groupName) {
    LtaValleySegmentGroup group = findInstance(groupName);
    if (group != null) { groups.remove(group); }
  }

  public static Vector getLoadedGroups() {
    if (groups.size() == 0) { return null; }
    return new Vector(groups.values());
  }
  public static Vector getAllLoadedTypes() {
    if (groups.size() == 0) { return null; }

    Enumeration e = groups.elements();
    Vector      v = new Vector(groups.size());
    while (e.hasMoreElements()) {
      v.addElement( ( (LtaValleySegmentGroup) e.nextElement()).getType());
    }

    return v;
  }
  public static String[] getLoadedGroupNames() {
    if (groups.size() == 0) { return null; }

    Enumeration e     = groups.elements();
    String[]    names = new String[groups.size()];
    int         i=0;
    while (e.hasMoreElements()) {
      names[i] = ( (LtaValleySegmentGroup) e.nextElement()).getName();
      i++;
    }

    return names;
  }

  public void closeFile() {
    clearFilename();
    setChanged(false);
  }

  public File getFilename() { return inputFile; }
  public void setFilename(File file) { inputFile = file; }
  public void clearFilename() { inputFile = null; }

  public boolean hasChanged() { return changed; }
  public void markChanged() {
    setChanged(true);
    setIsUserData(true);
  }
  public void setChanged(boolean value) { changed = value; }

  public boolean isUserData() { return isUserData; }
  public void setIsUserData(boolean value) { isUserData = value; }

  public String getName () { return groupType.toString(); }

  public PotentialAquaticState getPotentialAquaticState (String stateStr) {
    if (stateStr == null) { return null;}

    return ( (PotentialAquaticState) states.get(stateStr));
  }
  public PotentialAquaticState getPotentialAquaticState (AquaticClass aquaticClass,
                                                         AquaticAttribute attribute) {
    String str = aquaticClass.toString() + "-" +  attribute.toString();
    return getPotentialAquaticState(str);
  }

  public int getStatesCount() { return states.size(); }

   // ** Parsing Stuff **

  private boolean keyMatch (String key, int keyid) {
    return key.equalsIgnoreCase(KEYWORD[keyid]);
  }

  private int getKeyword (StringTokenizerPlus strTok) throws ParseError, IOException {
    String value;

    value = strTok.nextToken();

    if      (keyMatch(value,CLASS))          { return CLASS; }
    else if (keyMatch(value,NAME))           { return NAME;}
    else if (keyMatch(value,SYSTEM))         { return SYSTEM; }
    else if (keyMatch(value,END))            { return END;}
    // imported text file only.
    else if (keyMatch(value,LTA_VALLEY_SEGMENT_GROUP)) { return LTA_VALLEY_SEGMENT_GROUP; }
    else if (keyMatch(value,POTENTIAL_AQUATIC_STATE))  { return POTENTIAL_AQUATIC_STATE; }
    else {
      throw new ParseError("Unknown Keyword: " + value);
    }
  }


  private void readGroup (BufferedReader fin) throws ParseError, IOException {
    int                 key = EOF;
    String              value, line, name;
    StringTokenizerPlus strTok;

    // loop until we find the "END", "CLASS", or EOF.
    do {
      line   = fin.readLine();
      if (line == null) { key = EOF; continue;}

      strTok = new StringTokenizerPlus(line," ");
      if (strTok.hasMoreTokens() == false) {continue;}

      key = getKeyword(strTok);
      if (key != END && strTok.hasMoreTokens() == false) {
        throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
      }

      switch (key) {
        case NAME:
          name = strTok.nextToken();
          if (name == null) { throw new ParseError("Invalid group Name"); }

          groupType = LtaValleySegmentGroupType.get(name);
          if (groupType == null) { groupType = new LtaValleySegmentGroupType(name); }

          groups.put(groupType,this);

          break;
        case SYSTEM:
          String str = strTok.getToken();
//          systemGroup = (str.equalsIgnoreCase("true")) ? true : false;
          break;
        case END:
          break;
        default:
          throw new ParseError("Unknown keyword in input file.");
      }
    }
    while (key != EOF && key != CLASS && key != END);

    if (key == CLASS) {
      throw new ParseError("No END keyword found while reading Lta Valley Segment Group data.");
    }
    else if (key == EOF) {
      throw new ParseError("Invalid Pathway file, no Pathway records found.");
    }
  }

  private void readStates(BufferedReader fin) throws ParseError {
    PotentialAquaticState data;
    String                line;
    int                   id = 0, numStates = 0;

    try {
      line = fin.readLine();
      numStates = Integer.parseInt(line.trim());
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid value for number of records");
    }
    catch (IOException IOX) {
      throw new ParseError("Problems reading Pathway records.");
    }
    states = new Hashtable(numStates);

    for(int i=0;i<numStates;i++) {
      data = new PotentialAquaticState(this);
      data.readData(fin);
      addPotentialAquaticState(data);
    }
  }

  public void addPotentialAquaticState(PotentialAquaticState newState) {
    states.put(newState.getCurrentState(),newState);
  }
  public void deletePotentialAquaticState(PotentialAquaticState newState) {
    states.remove(newState.getCurrentState());
  }

  public static LtaValleySegmentGroup read(BufferedReader fin) {
    LtaValleySegmentGroup newGroup = new LtaValleySegmentGroup();
    newGroup.readData(fin);
    return newGroup;
  }

  private void readData(BufferedReader fin) {
    int                 key = EOF, i;
    String              value, line;
    StringTokenizerPlus strTok;

    // Create a log file name
    String dir  = System.getProperty("user.dir");
    String name = "Pathways";

    File logFile = new File(dir,name + ".log");
    int n = 1;
    while (logFile.exists()) {
      logFile = new File(dir,name + "-" + n++ + ".log");
    }

    try {
      do {
        line   = fin.readLine();
        if (line == null) { key = EOF; continue;}

        strTok = new StringTokenizerPlus(line," ");
        if (strTok.hasMoreTokens() == false) {continue;}

        key = getKeyword(strTok);
        if (strTok.hasMoreTokens() == false) {
          throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
        }

        if (key == CLASS) {
          value = strTok.nextToken();
        }
        else {
          throw new ParseError("Invalid record, first keyword must be CLASS");
        }

        if (keyMatch(value,LTA_VALLEY_SEGMENT_GROUP)) {
          readGroup(fin);
        }
        else if (keyMatch(value,ALL_POTENTIAL_AQUATIC_STATES)) {
          readStates(fin);
        }
        else {
          throw new ParseError ("Invalid Class Specified:" + value);
        }
      }
      while (key != EOF);
      fixNextStates(logFile);
      setChanged(false);
    }
    catch (ParseError PX) {
      System.out.println("Input file processing problem:");
      System.out.println(PX.msg);
    }
    catch (IOException IOE) {
      System.out.println("Problems while trying to read input file.");
    }
  }

  public void importTextFile(File infile) throws SimpplleError {
    BufferedReader        fin;
    int                   key = EOF, i;
    String                value, line;
    StringTokenizerPlus   strTok;
    boolean               foundGroup = false, foundState = false;
    LtaValleySegmentGroup newGroup;


    // Create a log file name
    String dir  = infile.getParent();
    String name = infile.getName();

    File logFile = new File(dir,name + ".log");
    int n = 1;
    while (logFile.exists()) {
      logFile = new File(dir,name + "-" + n++ + ".log");
    }

    try {
      fin    = new BufferedReader(new FileReader(infile));
      states = new Hashtable();

      line = fin.readLine();
      do {
        if (line == null) { key = EOF; continue;}

        strTok = new StringTokenizerPlus(line);
        if (strTok.hasMoreTokens() == false) {
          line = fin.readLine();
          continue;
        }

        key = getKeyword(strTok);
        if (key == LTA_VALLEY_SEGMENT_GROUP && strTok.hasMoreTokens() == false) {
          throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
        }

        if (key == LTA_VALLEY_SEGMENT_GROUP) {
          line = importGroup(strTok.nextToken(), fin);
          foundGroup = true;
        }
        else if (key == POTENTIAL_AQUATIC_STATE) {
          line = importPotentialAquaticState(strTok.nextToken(), fin);
          foundState = true;
        }
        else {
          line = fin.readLine();
          continue;
        }
      }
      while (key != EOF);

      fin.close();
      if (foundGroup == false || foundState == false) {
        String msg = "Could not find data in input file\n" +
                     "Check the file format and try again";
        throw new ParseError(msg);
      }
      fixNextStates(logFile);
      setChanged(false);
    }
    catch (ParseError err) {
      states = null;
      throw new SimpplleError(err.msg);
    }
    catch (IOException IOE) {
      states = null;
      throw new SimpplleError("Problems while trying to read input file.");
    }
  }

  private String importGroup(String groupName, BufferedReader fin)
    throws IOException, ParseError
  {
    int                 key = -1;
    String              value, line;
    StringTokenizerPlus strTok;

    String name = groupName.trim().toUpperCase();
    if (name == null) { throw new ParseError("Invalid Group Name Found."); }

    groupType = LtaValleySegmentGroupType.get(name);
    if (groupType == null) { groupType = new LtaValleySegmentGroupType(name); }

    groups.put(groupType,this);

    do {
      line   = fin.readLine();
      if (line == null) { key = EOF; continue;}

      strTok = new StringTokenizerPlus(line);
      if (strTok.hasMoreTokens() == false) {continue;}

      key = getKeyword(strTok);
      if (strTok.hasMoreTokens() == false) {
        throw new ParseError("Keyword: " + KEYWORD[key] + " has no value.");
      }

      switch (key) {
        case POTENTIAL_AQUATIC_STATE:
          break;
        default:
          throw new ParseError("Unknown keyword in import text file.");
      }
    }
    while (key != EOF && key != POTENTIAL_AQUATIC_STATE);

    if (key == EOF) {
      throw new ParseError("Invalid Pathway file, no Pathway records found.");
    }

    return line;
  }

//  private Vector parseList(StringTokenizerPlus strTok)
//    throws ParseError
//  {
//    return parseList(strTok,false);
//  }

  private Vector parseList(StringTokenizerPlus strTok, boolean isInteger)
    throws ParseError
  {
    Vector  result = null;
    Integer intVal;
    String  value = null;
    boolean moreTokens;

    if (strTok.hasMoreTokens()) {
      value = strTok.nextToken();
    }
    if (value == null) {return null;}

    result = new Vector(strTok.countTokens() + 1);

    do {
      if (!isInteger) {
        result.addElement(value);
      }
      else {
        try {
          intVal = Integer.valueOf(value);
          result.addElement(intVal);
        }
        catch (NumberFormatException NFE) {
          throw new ParseError("Invalid number found when reading a list.");
        }
      }
      moreTokens = strTok.hasMoreTokens();
      if (moreTokens) { value = strTok.nextToken();}
    }
    while (moreTokens);

    return result;
  }

  private String importPotentialAquaticState(String stateStr, BufferedReader fin)
    throws ParseError
  {
    PotentialAquaticState data;
    String                line;
    int                   id = 0;

    data = new PotentialAquaticState(this, stateStr);
    line = data.importPotentialAquaticState(fin);
    states.put(data.getCurrentState(),data);

    return line;
  }

  public void saveAs(File outfile) throws SimpplleError {
    setFilename(Utility.makeSuffixedPathname(outfile,"",FILE_EXT));
    save();
  }

  public void save() throws SimpplleError {
    PrintWriter fout;
    try {
      fout = Utility.openPrintWriter(getFilename());
    }
    catch (SimpplleError err) {
      clearFilename();
      throw err;
    }

    save(fout);
    setChanged(false);
    fout.flush();
    fout.close();
  }

  public void save(PrintWriter fout) {
    saveGroupInfo(fout);
    saveStateInfo(fout);
  }

  private void saveGroupInfo(PrintWriter fout) {
    fout.println("CLASS LTA-VALLEY-SEGMENT-GROUP");
    fout.println("NAME " + getName());
    fout.println("END");
    fout.println();
  }

  private void saveStateInfo(PrintWriter fout) {
    fout.println("CLASS ALL-POTENTIAL-AQUATIC-STATES");
    fout.println(states.size());

    PotentialAquaticState state;
    Enumeration e = states.keys();
    String      key;
    while (e.hasMoreElements()) {
      key = (String) e.nextElement();
      state  = (PotentialAquaticState) states.get(key);
      state.save(fout);
    }
  }

  private void fixNextStates (File logFile) throws ParseError {
    Enumeration           e;
    String                stateStr;
    PotentialAquaticState state;
    RegionalZone          zone = Simpplle.currentZone;
    StringBuffer          strBuf = new StringBuffer();
    boolean               foundErrors = false;

    e = states.keys();
    while (e.hasMoreElements()) {
      stateStr = (String) e.nextElement();
      state = (PotentialAquaticState) states.get(stateStr);
      try {
        state.fixNextState(strBuf);
      }
      catch (ParseError err) {
        foundErrors = true;
      }
    }

    if (foundErrors) {
      PrintWriter fout;
      try {
        FileWriter fwrite;
        fout = new PrintWriter(new FileWriter(logFile));
        fout.println(strBuf.toString());
        fout.flush();
        fout.close();
      }
      catch (IOException err) {
        System.err.println("Error trying to write log file, log follows: " +
                           strBuf.toString());
      }

      String msg = "Invalid States were found.\n" +
                   "Please refer to the log file: " + logFile + "\n" +
                   "For specific information.";
      throw new ParseError(msg);
    }
  }

  public String toString() {
    return getName();
  }

  public void export(File outfile) throws SimpplleError {
    PrintWriter fout;
    try {
      fout = new PrintWriter(new FileWriter(outfile));
    }
    catch (IOException err) {
      throw new SimpplleError("Problems writing file.");
    }

    fout.println(KEYWORD[LTA_VALLEY_SEGMENT_GROUP] + " " + groupType.toString());

    PotentialAquaticState state;
    Enumeration           keys = states.keys();
    Vector                v = new Vector();
    String[]              stateStrings;

    while (keys.hasMoreElements()) {
      v.addElement((String)keys.nextElement());
    }
    stateStrings = (String[]) v.toArray(new String[v.size()]);
    Utility.sort(stateStrings);

    for(int i=0; i<stateStrings.length; i++) {
      state = (PotentialAquaticState) states.get(stateStrings[i]);
      state.export(fout);
    }
    fout.flush();
    fout.close();
  }

  public Hashtable getAllAquaticClassHt() {
    return getAllAquaticClassHt(new Hashtable());
  }
  public Hashtable getAllAquaticClassHt(Hashtable ht) {
    Enumeration           e = states.keys();
    String                key;
    AquaticClass          aquaticClass;
    PotentialAquaticState state;
    int                   i;

    while (e.hasMoreElements()) {
      key          = (String) e.nextElement();
      state        = (PotentialAquaticState) states.get(key);
      aquaticClass = state.getAquaticClass();
      ht.put(aquaticClass,aquaticClass);
      state = null;
    }
    return ht;
  }
  public AquaticClass[] getAllAquaticClass() {
    return LtaValleySegmentGroup.getAllAquaticClass(getAllAquaticClassHt());
  }
  public static AquaticClass[] getAllAquaticClass(Hashtable ht) {
    AquaticClass[] allAquaticClass = new AquaticClass[ht.size()];
    Enumeration e = ht.keys();
    int         i = 0;

    while (e.hasMoreElements()) {
      allAquaticClass[i] = (AquaticClass) e.nextElement();
      i++;
    }
    ht = null;

    Arrays.sort(allAquaticClass);
    return allAquaticClass;
  }

  /**
    * Find Processes that have a next state in
    * those Potential Aquatic State that match the given Aquatic Class.
    * This is used primarily in the pathways dialog.
    */
  public String[] getAllProcesses(AquaticClass aquaticClass) {
    Hashtable             ht = new Hashtable();
    Enumeration           e = states.keys();
    String[]              allProcesses;
    Process[]             processes;
    String                key;
    PotentialAquaticState state;
    int                   i;

    while (e.hasMoreElements()) {
      key   = (String) e.nextElement();
      state = (PotentialAquaticState) states.get(key);
      if (aquaticClass == state.getAquaticClass()) {
        processes = state.getProcesses();
        for(i=0;i<processes.length;i++) {
          ht.put(processes[i].toString(),processes[i].toString());
        }
      }
      state = null;
    }

    allProcesses = new String[ht.size()];
    e            = ht.keys();
    i            = 0;
    while (e.hasMoreElements()) {
      allProcesses[i] = (String) e.nextElement();
      i++;
    }
    ht = null;

    return allProcesses;
  }
}




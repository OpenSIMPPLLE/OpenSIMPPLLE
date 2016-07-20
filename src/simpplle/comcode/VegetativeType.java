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
import org.apache.commons.collections.map.*;
import org.apache.commons.collections.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p> This class has fields which hold Vegetative pathway state
 * information.  There are also a number of methods primarily
 * dealing with reading pathway information from a file as well
 * as accessor methods.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */

public final class VegetativeType implements Comparable, Externalizable {
  public static final long badSerialVersionUID = -5936001845626671540L;
  static final long serialVersionUID = 590817789717342164L;
  static final int  version          = 2;

  private HabitatTypeGroup htGrp;
  private Species          species;
  private SizeClass        sizeClass;
  private Density          density;
  private int              age;
  private String           printName;

  private Hashtable        nextState;
  private Hashtable        probability;
  private Hashtable        positions;
  private HashMap<ProcessType,HashMap<InclusionRuleSpecies,Float>> speciesChange;
  private HashMap<InclusionRuleSpecies,Range> speciesRange;

  private static final int COUNT = 3;
  private static final int INCLUSION_RULES_COLUMN_COUNT = 3;  // aka species range

  private static boolean limitedSerialization = false;

  private static final String DELIM     = ",";
  public static final String LISTDELIM = ":";
  private static final char   NODATA    = '?';
  public static final String SLASH     = "/";

  private static final String XY_COORDINATE   = "XY-COORDINATE";
  private static final String VEGETATIVE_TYPE = "VEGETATIVE-TYPE";
  private static final String SPECIES_CHANGE  = "SPECIES-CHANGE";
  private static final String SPECIES_RANGE   = "INCLUSION-RULE";

  private static final int INIT_X = 40;
  private static final int INIT_Y = 40;

  public static VegetativeType UNKNOWN = new VegetativeType(Species.UNKNOWN,
      SizeClass.UNKNOWN, 1, Density.UNKNOWN);

  public static VegetativeType ND = new VegetativeType(Species.ND,
      SizeClass.ND, 1, Density.ONE);

  // These avoid creation of temp Strings for age when writing out files
  // during simulations.
  private static final String TWO       = "2";
  private static final String THREE     = "3";
  private static final String FOUR      = "4";
  private static final String FIVE      = "5";
  private static final String SIX       = "6";
  private static final String SEVEN     = "7";
  private static final String EIGHT     = "8";
  private static final String NINE      = "9";
  private static final String TEN       = "10";
  private static final String ELEVEN    = "11";
  private static final String TWELVE    = "12";
  private static final String THIRTEEN  = "13";
  private static final String FOURTEEN  = "14";
  private static final String FIFTEEN   = "15";
  private static final String SIXTEEN   = "16";
  private static final String SEVENTEEN = "17";
  private static final String EIGHTEEN  = "18";
  private static final String NINETEEN  = "19";
  private static final String TWENTY    = "20";


  /**
   * This is the default constructor, it initializes some fields.
   */
  public VegetativeType() {
    species   = null;
    sizeClass = null;
    density   = null;
    age       = -1;
  }

  /*
   * This constructor takes a HabitatTypeGroup as an argument,
   * which is stored in a class field, it also calls the default
   * constructor.
   * @param htGrp is a HabitatTypeGroup instance.
   */
  public VegetativeType(HabitatTypeGroup htGrp) {
    this();
    nextState   = new Hashtable(20);
    probability = new Hashtable(20);
    positions   = new Hashtable(10);
    this.htGrp = htGrp;
  }

  /**
   * This constructor is used when importing text files containing
   * Vegetative Type information.
   * @param htGrp is a HabitatTypeGroup instance
   * @param vegTypeStr is a String representation of a Vegetative Type.
   */
  public VegetativeType(HabitatTypeGroup htGrp, String vegTypeStr)
    throws ParseError
  {
    this();
    nextState   = new Hashtable(20);
    probability = new Hashtable(20);
    positions   = new Hashtable(10);
    this.htGrp = htGrp;
    parseVegetativeTypeString(vegTypeStr);
    printName = vegTypeStr;
    printName = printName.intern();
  }
/**
 * This constructor is used to create a temporary instance to store in an Evu after import of an invalid state. The invalid state needs to 
 * be stored so it can be corrected later.  
 * @param newSpecies 
 * @param newSizeClass
 * @param newAge
 * @param newDensity
 */

  public VegetativeType(Species newSpecies, SizeClass newSizeClass,
                        int newAge, Density newDensity) {
    species     = newSpecies;
    sizeClass   = newSizeClass;
    density     = newDensity;
    age         = newAge;
    makePrintName();

    htGrp       = null;
    nextState   = null;
    probability = null;
    positions   = null;
  }

  public VegetativeType(Species newSpecies, SizeClass newSizeClass, Density newDensity) {
    this(newSpecies,newSizeClass,1,newDensity);
  }

  public VegetativeType(HabitatTypeGroup htGrp, Species newSpecies,
                        SizeClass newSizeClass, int newAge, Density newDensity)
  {
    this(htGrp);
    species     = newSpecies;
    sizeClass   = newSizeClass;
    density     = newDensity;
    age         = newAge;
    makePrintName();

    addProcessNextState(Process.findInstance(ProcessType.SUCCESSION),this);
    addProcessNextState(Process.findInstance(ProcessType.STAND_REPLACING_FIRE),this);
    addProcessNextState(Process.findInstance(ProcessType.MIXED_SEVERITY_FIRE),this);
    addProcessNextState(Process.findInstance(ProcessType.LIGHT_SEVERITY_FIRE),this);
    setSpeciesPosition(species,new Point(INIT_X,INIT_Y));
  }
/**
 * Method to compare this vegetative type and another by their species, size class, density and age.  
 */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof VegetativeType) {
      if (species == null || sizeClass == null || density == null || age == -1 ||
          obj == null) { return false; }

      VegetativeType vt = (VegetativeType)obj;
      return (species.equals(vt.getSpecies()) &&
              sizeClass.equals(vt.getSizeClass()) &&
              density.equals(vt.getDensity()) &&
              age == vt.getAge());
    }
    return false;
  }
/**
 * Gets the hash code for a particular Vegetative Type by its name.  
 */
  public int hashCode() {
    return printName.hashCode();
  }
/**
 * Requisite compareTo method.  Compares two vegetative type objects by their toStrings.
 */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    if (o instanceof VegetativeType) {
      return toString().compareTo(o.toString());
    }
    return -1;
  }
/**
 * Used in the Pathway shape GUI.  Gets the coordinates of parameter species. Default is (10,10 - the default for pathway shape) if null, otherwise will return the species x,y coordinate
 * @param tmpSpecies the species at sought position
 * @return x,y coordinate of species
 */
  public Point getSpeciesPosition(Species tmpSpecies) {
    Point tmp = (Point) positions.get(tmpSpecies);

    if (tmp == null) {
      tmp = new Point(10,10);
    }
    return tmp;
  }
/**
 * Used in the pathway shape GUI.  Sets the position of a species within the positions hashtable to a parameter position.  This hashtable is keyed by the species, with position as the value.  
 * @param tmpSpecies species whose position will be set (key into hashtable)
 * @param newPosition the position of the species (value in hashtable)
 */
  public void setSpeciesPosition(Species tmpSpecies, Point newPosition) {
    positions.put(tmpSpecies, newPosition);
    htGrp.markChanged();
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

  public int calculateTimeToState(VegetativeType vt, String process) {
    return calculateTimeToState(vt, Process.findInstance(process));
  }
/**
 * Calculates the time from this vegetative type to parameter vegetative state.  Vegetative type state - current veg state = time to state.   
 * @param vt the vegetative type marking the end of differce 
 * @param process
 * @return
 */
  public int calculateTimeToState(VegetativeType vt, Process process) {
    if (equals(vt)) {
      return 0;
    }

    int t = 0;

    VegetativeType current = this; //t=0
    VegetativeType next = getProcessNextState(process);
    t++;//next state = t + 1
    while (vt.equals(next) == false && current.equals(next) == false) {
      current = next;
      next = next.getProcessNextState(process);
      t++;
    }
    return (vt.equals(next) ? t : -1);
  }



  /**
   * Gets the next state (a VegetativeType) associated with
   * Process p.
   * @param p is a Process.
   * @return a VegetativeType, the next state for Process p.
   */
  public VegetativeType getProcessNextState(Process p) {
    return (VegetativeType) nextState.get(p);
  }
  public VegetativeType getProcessNextState(ProcessType p) {
    return getProcessNextState(Process.findInstance(p));
  }
/**
 * Add a process to the vegetative states next state.   
 * @param p the process  to be added
 * @param newNextState the next state.  
 */
  public void addProcessNextState(Process p, VegetativeType newNextState) {
    probability.put(p,new Integer(0));
    setProcessNextState(p,newNextState);
  }
/**
 * Sets a state within the vegetative type next state.  Marks the habitat type group changed.    
 * @param p
 * @param newNextState
 */
  public void setProcessNextState(Process p, VegetativeType newNextState) {
    if (probability.get(p) == null) {
      probability.put(p,new Integer(0));
    }

    nextState.put(p, newNextState);
    htGrp.markChanged();
  }
/**
 * Removes a process from the next state hashtable.  Marks the habitat gropu changed. 
 * @param p
 */
  public void removeProcessNextState(Process p) {
    nextState.remove(p);
    htGrp.markChanged();
  }
/**
 * Gets a process within the next state of a vegetative type.  
 * @param vt
 * @return
 */
  public Process getNextStateProcess(VegetativeType vt) {
    VegetativeType ns;
    Enumeration    keys = nextState.keys();
    while (keys.hasMoreElements()) {
      Process p = (Process) keys.nextElement();
      ns = (VegetativeType) nextState.get(p);
      if (ns == vt) {
        return p;
      }
    }
    return null;
  }
/**
 * Finds the previous states of a habitat group.  
 * @return vector with the previous states of this vegetative type.  
 */
  public Vector findPreviousStates() {
    return htGrp.findPreviousStates(this);
  }
/**
 * Gets an array of the processes in the next state.  
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
   * Gets the HabitatTypeGroup of which this VegetativeType
   * is a member.
   * @return a HabitatTypeGroup.
   */
  public HabitatTypeGroup getHtGrp() {
    return htGrp;
  }

  /**
   * Creates a current state from the species, size class,
   * age, and density of the VegetativeType and return it
   * as a string.
   * @return a String, the print representation of this
   *         VegetativeType.
   */
  public String getCurrentState() {
    return printName;
  }
/**
 * Method to form the print name of a vegetative type. If age is 1 this will be name '/' size class '/' density (canopy cover).
 * If greater than 1 this will be species '/' size class age '/' density (canopy cover)
 */
  private void makePrintName() {
    if (age == 1) {
      printName = species + SLASH + sizeClass + SLASH + density.toString();
    }
    else {
      printName = species + SLASH + sizeClass + age + SLASH + density.toString();
    }
    printName = printName.intern();
  }
/**
 * Print the age of vegetative state.  
 * @param fout
 */
  public void printAge(PrintWriter fout) {
    switch (age) {
      case 2:  fout.print(TWO);       break;
      case 3:  fout.print(THREE);     break;
      case 4:  fout.print(FOUR);      break;
      case 5:  fout.print(FIVE);      break;
      case 6:  fout.print(SIX);       break;
      case 7:  fout.print(SEVEN);     break;
      case 8:  fout.print(EIGHT);     break;
      case 9:  fout.print(NINE);      break;
      case 10: fout.print(TEN);       break;
      case 11: fout.print(ELEVEN);    break;
      case 12: fout.print(TWELVE);    break;
      case 13: fout.print(THIRTEEN);  break;
      case 14: fout.print(FOURTEEN);  break;
      case 15: fout.print(FIFTEEN);   break;
      case 16: fout.print(SIXTEEN);   break;
      case 17: fout.print(SEVENTEEN); break;
      case 18: fout.print(EIGHTEEN);  break;
      case 19: fout.print(NINETEEN);  break;
      case 20: fout.print(TWENTY);    break;
    }
  }
  public void printCurrentState(PrintWriter fout) {
    fout.print(species);
    fout.print(SLASH);
    fout.print(sizeClass);
    printAge(fout);
    fout.print(SLASH);
    fout.print(density.toString());
  }

  /**
   * Gets the Species. 
   * @return a Species, the Species.
   */
  public Species getSpecies() {
    return species;
  }

  /**
   * Gets the Size Class.
   * @return a SizeClass, the Size Class.
   */
  public SizeClass getSizeClass() {
    return sizeClass;
  }

  /**
   * Gets the Canopy Coverage (Density).
   * @return an int, the Density.
   */
  public Density getDensity() {
    return density;
  }

  /**
   * Gets the Age.
   * @return an int, the Age.
   */
  public int getAge() {
    return age;
  }
/**
 * Gets string literal name of the vegetative type.  This will be a string of species / sizeClass  age / density.toString() for those above age = 1.  
 * If in first year does not include age.  
 * @return
 */
  public String getPrintName() {
    return printName;
  }
/**
 * This function is used when importing user created text files containing vegetative types.  
 * Takes in the print name in form species + SLASH + sizeClass + age + SLASH + density.toString() parses, checks to make sure there are three tokens, or strings split 
 * by delimiter SLASH.  IF not throws an error, if it is 3 then gets the species (if species is null, creates a user created species, then parses the size class
 * the size class which above age = 1 will include an age.   
 * @param vegTypeStr above age = 1 in form species + SLASH + sizeClass + age + SLASH + density.toString(), else excludes age
 * @throws ParseError - if not a valid veg type (strings split by SLASH token !=3) 
 */
  private void parseVegetativeTypeString(String vegTypeStr)
    throws ParseError
  {
    StringTokenizerPlus strTok = new StringTokenizerPlus(vegTypeStr,SLASH);
    RegionalZone zone = Simpplle.getCurrentZone();

    if (strTok.countTokens() != 3) {
      throw new ParseError(vegTypeStr + " is not a valid Vegetative Type");
    }

    String speciesStr = strTok.nextToken();
    species   = Species.get(speciesStr);
    if (species == null) {
      species = new Species(speciesStr,true);
    }
    parseSizeClass(strTok.nextToken());
    String str = strTok.getToken();
    density = Density.get(str);

    if (density == null) {
      density = new Density(str,true);
    }
  }

  /**
   * Separate the size class from the age in the given
   * string.  Then set the class members with the extracted
   * values.  This function used to parse the size class
   * portion of the current state when importing user created
   * text files containing vegetative types.
   */
  private void parseSizeClass(String str)
    throws ParseError
  {
    boolean      foundNumber = false;

    int i = 0;
    while (i < str.length() && foundNumber == false) {
      foundNumber = Character.isDigit(str.charAt(i));
      i++;
    }

    if (i == 1 && foundNumber) {
      throw new ParseError("Invalid Size Class in " + str);
    }

    if (foundNumber) {
      i--;
      try {
        age       = Integer.parseInt(str.substring(i));
        sizeClass = SizeClass.get(str.substring(0,i));
        if (sizeClass == null) {
          sizeClass = new SizeClass(str.substring(0,i),true);
        }
      }
      catch (NumberFormatException e) {
        throw new ParseError("Invalid Age in " + str);
      }
    }
    else {
      age       = 1;
      sizeClass = SizeClass.get(str);
      if (sizeClass == null) {
        sizeClass = new SizeClass(str,true);
      }
    }

  }

  // Change the value in the nextState hashtable
  // from a String to a VegetativeType object.
  /**
   * This method changes the value of the nextState hashtable
   * from String's to VegetativeType references.  When a pathway
   * data file is loaded the VegetativeType object that the
   * String reprsentation refers to may not yet have been loaded.
   * As a result, it is necessary to associate the String with
   * Process keys, in the hashtable until all pathway states in
   * a given Habitat Type Group have been loaded.  Once all the
   * VegetativeType instance have been created this method is
   * called to replace the Strings with the appropriate VegetativeType
   * references.
   * @see simpplle.comcode.HabitatTypeGroup
   * @exception simpplle.comcode.ParseError
   *            This indicates that an error occurred while
   *            attempting to read a pathway state from the
   *            input file.
   */
  public void fixNextState(StringBuffer log) throws ParseError {
    Enumeration e;
    Process        process;
    String         state;
    VegetativeType newState;
    boolean        errorsFound = false;

    e = nextState.keys();
    while (e.hasMoreElements()) {
      process = (Process) e.nextElement();
      state   = (String) nextState.get(process);

      newState = htGrp.getVegetativeType(state);
      if (newState == null) {
        log.append("Invalid Process Next State: " + "VEGETATIVE-TYPE " +
                   getCurrentState() + "   " + process + " " + state +
                   Simpplle.endl);
        errorsFound = true;
        continue;
      }
      nextState.remove(process);
      nextState.put(process,newState);
    }
    if (errorsFound) {
      throw new ParseError("VEGETATIVE-TYPE " + getCurrentState() +
                           " has errors.  See log for details");
    }
  }

  // ** Start Parser Stuff **
  // ------------------------
  public void importSpeciesChangeNew(StringTokenizerPlus strTok) throws ParseError, IOException {
    String errMsg = "VEGETATIVE-TYPE " + getCurrentState() +
                    " has errors in the " + SPECIES_CHANGE + " section";

    String str = strTok.getToken();
    if (str == null) { throw new ParseError(errMsg); }
    ProcessType proc = ProcessType.get(str.toUpperCase());
    if (proc == null) { throw new ParseError(errMsg); }

    str = strTok.getToken();
    if (str == null) { throw new ParseError(errMsg); }
    InclusionRuleSpecies sp = InclusionRuleSpecies.get(str,true);

    float pct = strTok.getFloatToken(Float.NaN);
    if (pct == Float.NaN) { throw new ParseError(errMsg); }

    if (speciesChange == null) {
      speciesChange = new HashMap<ProcessType,HashMap<InclusionRuleSpecies,Float>>();
    }
    HashMap<InclusionRuleSpecies,Float> hm = speciesChange.get(proc);
    if (hm == null) {
      hm = new HashMap<InclusionRuleSpecies,Float>();
      speciesChange.put(proc,hm);
    }
    hm.put(sp,pct);

  }
  public void importSpeciesRange(StringTokenizerPlus strTok) throws ParseError, IOException {
    String errMsg = "VEGETATIVE-TYPE " + getCurrentState() +
                    " has errors in the " + SPECIES_RANGE + " section";

    String str = strTok.getToken();
    if (str == null) { throw new ParseError(errMsg); }
    InclusionRuleSpecies sp = InclusionRuleSpecies.get(str,true);

    int lower = strTok.getIntToken(-9999);
    if (lower == -9999) { throw new ParseError(errMsg);  }

    int upper = strTok.getIntToken(-9999);
    if (upper == -9999) { throw new ParseError(errMsg);  }

    if (speciesRange == null) { speciesRange = new HashMap<InclusionRuleSpecies,Range>(); }

    speciesRange.put(sp,new Range(lower,upper));
  }


  public void importSpeciesChange(BufferedReader fin) throws ParseError, IOException {
    String line;

    line = fin.readLine();
    if (line == null || line.trim().length() == 0) {
      throw new ParseError("Species Change section empty");
    }
    line = line.trim();

    StringTokenizerPlus strTok = new StringTokenizerPlus(line);
    importSpeciesChange(fin,strTok);
  }
  public boolean importSpeciesChange(BufferedReader fin, StringTokenizerPlus strTok) throws ParseError, IOException {
    int count = strTok.countTokens();
    if (count == 0) {
      throw new ParseError("No Species List is SPECIES-CHANGE section");
    }

    ArrayList<InclusionRuleSpecies> speciesList = new ArrayList<InclusionRuleSpecies>(count);
    for (int i = 0; i < count; i++) {
      speciesList.add(InclusionRuleSpecies.get(strTok.getToken(), true));
    }

    String line = fin.readLine();
    if (line != null) { line = line.trim(); }

    speciesChange = new HashMap<ProcessType, HashMap<InclusionRuleSpecies, Float>>();
    HashMap<InclusionRuleSpecies, Float> changeMap;

    ProcessType process;
    while (line != null) {
      strTok = new StringTokenizerPlus(line,", \t\n\r\f");
      if (strTok.countTokens() != (speciesList.size() + 1)) {
        throw new ParseError("Not enough data in line: " + line);
      }

      process = ProcessType.get(strTok.getToken());
      if (process == null) {
        throw new ParseError("Invalid Process found in line: " + line);
      }

      changeMap = speciesChange.get(process);
      if (changeMap == null) {
        changeMap = new HashMap<InclusionRuleSpecies, Float>(speciesList.size());
        speciesChange.put(process, changeMap);
      }
      float pct;
      for (InclusionRuleSpecies sp : speciesList) {
        pct = (float)strTok.getFloatToken(Float.NaN);
        if (pct == Float.NaN) {
          throw new ParseError("Invalid percent value found in line: " + line);
        }
        changeMap.put(sp, pct);
      }

      line = fin.readLine();
      if (line != null) { line = line.trim(); }

      if (line.equalsIgnoreCase("END")) { line = null; }
      else if (line.equalsIgnoreCase("END-GROUP")) { return true; }
    }
    return false;
  }

  public String importVegetativeType(BufferedReader fin)
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

        if (count == 2 && firstValue.equals(VEGETATIVE_TYPE)) {
          return line;
        }
        else if (count == 3 && firstValue.equals(XY_COORDINATE)) {
          importPositions(strTok);
        }
        else if (count == 4 && firstValue.equals(SPECIES_CHANGE)) {
          importSpeciesChangeNew(strTok);
        }
        else if (count == 4 && firstValue.equals(SPECIES_RANGE)) {
          importSpeciesRange(strTok);
        }
        else {
          process = Process.findInstance(ProcessType.get(firstValue));
          if (process == null) {
            System.out.println("Skipping Invalid Process found in line: " + line);
            continue;
//            throw new ParseError("Invalid Process found in line: " + line);
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
    String  str;
    Species posSpecies;

    str = strTok.nextToken();
    posSpecies = Species.get(str);
    if (posSpecies == null) {
      posSpecies = new Species(str,true);
    }

    StringTokenizerPlus posStrTok = new StringTokenizerPlus(strTok.nextToken(),",");
    if (posStrTok.countTokens() != 2) {
      throw new ParseError("Invalid positions found");
    }
    int x = posStrTok.getIntToken();
    int y = posStrTok.getIntToken();
    if (x == -1 || y == -1) {
      throw new ParseError("Invalid positions found");
    }
    positions.put(posSpecies,new Point(x,y));
  }

  private void importProcessNextState(String processName, StringTokenizerPlus strTok)
    throws ParseError
  {
    RegionalZone        zone = Simpplle.getCurrentZone();
    Process             process;
    String              nextStateStr;
    int                 nextStateProb=0;

    process = Process.findInstance(ProcessType.get(processName));
    if (process == null) {
      throw new ParseError("Invalid process found: " + processName);
    }
    nextStateStr  = strTok.nextToken();
    if (strTok.hasMoreTokens()) {
      nextStateProb = strTok.getIntToken();
    }

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

      // All valid vegetative types have at least one
      // process next state (i.e. SUCCESSION).
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

  private void readSpeciesChange(StringTokenizerPlus mainTok) throws ParseError {
    String str = mainTok.getToken();
    int    processCount;

    if (str == null) { return; }
    else { processCount = Integer.parseInt(str); }

    speciesChange = new HashMap<ProcessType,HashMap<InclusionRuleSpecies,Float>>(processCount);

    StringTokenizerPlus  strTok;
    ProcessType          process;
    InclusionRuleSpecies sp;
    HashMap<InclusionRuleSpecies,Float> hm;

    for (int i=0; i<processCount; i++) {
      strTok = new StringTokenizerPlus(mainTok.getToken(),LISTDELIM);
      process = ProcessType.get(strTok.getToken());
      int speciesCount = strTok.getIntToken();
      hm = new HashMap<InclusionRuleSpecies,Float>(speciesCount);
      for (int j=0; j<speciesCount; j++) {
        sp = InclusionRuleSpecies.get(strTok.getToken(),true);
        hm.put(sp,strTok.getFloatToken());
      }
      speciesChange.put(process,hm);
    }
  }
  private void readSpeciesRange(StringTokenizerPlus mainTok) throws ParseError {
    String str = mainTok.getToken();
    int    count;

    if (str == null) { return; }
    else { count = Integer.parseInt(str); }

    speciesRange = new HashMap<InclusionRuleSpecies,Range>(count);

    StringTokenizerPlus  strTok;
    InclusionRuleSpecies sp;
    Range                range;

    for (int i=0; i<count; i++) {
      strTok = new StringTokenizerPlus(mainTok.getToken(),LISTDELIM);
      sp = InclusionRuleSpecies.get(strTok.getToken(),true);
      range = new Range(strTok.getIntToken(),strTok.getIntToken());

      speciesRange.put(sp,range);
    }
  }

  private void readPositions(StringTokenizerPlus strTok) throws ParseError {
    String              value = null;
    Species             species;
    int                 x,y;
    int                 count;
    StringTokenizerPlus listStrTok;
    RegionalZone        zone = Simpplle.getCurrentZone();

    try {
      // Somehow pathway files are get created that have no Position info,
      // this corrects that problem.
      if (strTok.hasMoreTokens() == false) {
        positions.put(this.species,new Point(200,200));
        return;
      }
      value = strTok.getToken();
      if (value == null) {
        positions.put(this.species,new Point(200,200));
        return;
      }

      listStrTok = new StringTokenizerPlus(value,LISTDELIM);

      count = listStrTok.countTokens();

      // All valid vegetative types have at least one
      // position. (i.e. the position of itself);
      if ( (count % 3) != 0 || count == 0) {
        throw new ParseError("Not enough elements in Positions list.");
      }

      for(int i=0;i<count;i+=3) {
        value   = listStrTok.nextToken();
        species = Species.get(value);
        x       = Integer.parseInt(listStrTok.nextToken());
        y       = Integer.parseInt(listStrTok.nextToken());

        // There are numerous invalid species in the position lists,
        // most likely caried over from the original lisp files.
        // This weeds those problems out.
        if (species == null) {
          continue;
        }
        positions.put(species,new Point(x,y));
      }
    }
    catch (NumberFormatException NFE) {
      throw new ParseError("Invalid positions coordinate value.");
    }
  }

  /**
   * This method reads an processes a line from the input
   * pathway data file.  The line contains all the information
   * for the VegetativeType state.  The input line is processes
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

      // 4/19/02 Modified the code below to parse the Size Class to
      // look for age.  This due to a bug that incorrectly parsed input
      // files and thus generated incorrect output pathway files which now
      // have to be supported.
      strTok = new StringTokenizerPlus(line,DELIM);
      int tmpAge = 1;

      String str = strTok.getToken();
      species    = Species.get(str);
      if (species == null) {
        species = new Species(str,true);
      }

      parseSizeClass(strTok.getToken());
      tmpAge    = strTok.getIntToken();
      if (age == 1) { age = tmpAge; }

      str = strTok.getToken();
      density = Density.get(str);
      if (density == null) { density = new Density(str,true); }

      makePrintName();
      readProcessNextState(strTok);
      readPositions(strTok);
      if (strTok.hasMoreTokens()) {
        readSpeciesChange(strTok);
      }
      if (strTok.hasMoreTokens()) {
        readSpeciesRange(strTok);
      }
    }
    catch (ParseError PE) {
      throw new ParseError ("Error reading Vegetative Type " +
                            getCurrentState() + " " +
                            PE.msg);
    }
    catch (IOException IOX) {
      throw new ParseError ("Could not read a line in Vegetative Type "
                            + getCurrentState());
    }
  }

  public void save(PrintWriter fout) {
    fout.print(getSpecies() + "," + getSizeClass() + "," +
               getAge() + "," + getDensity());
    saveProcessNextState(fout);

    savePositions(fout);
    saveSpeciesChange(fout);
    saveSpeciesRange(fout);

    fout.println();
  }

  private void saveProcessNextState(PrintWriter fout) {
    fout.print(",");

    Enumeration e = nextState.keys();
    Process     process;
    while (e.hasMoreElements()) {
      process = (Process) e.nextElement();
      fout.print(process + ":" +
                 (VegetativeType)nextState.get(process) + ":" +
                 (Integer)probability.get(process));
      if (e.hasMoreElements()) { fout.print(":"); }
    }
  }

  private void savePositions(PrintWriter fout) {
    fout.print(",");
    if (positions == null || positions.size() == 0) {
      fout.print(NODATA);
      return;
    }

    Enumeration e = positions.keys();
    Species     species;
    Point       p;
    while (e.hasMoreElements()) {
      species = (Species) e.nextElement();
      p       = (Point) positions.get(species);
      fout.print(species + ":" + p.x + ":" + p.y);
      if (e.hasMoreElements()) { fout.print(":"); }
    }
  }

  private void saveSpeciesChange(PrintWriter fout) {
    fout.print(",");
    if (speciesChange == null || speciesChange.size() == 0) {
      fout.print(NODATA);
      return;
    }

    //  private HashMap<ProcessType,HashMap<Species,Integer>> speciesChange;

    fout.print(speciesChange.size());
    for (ProcessType process : speciesChange.keySet()) {
      fout.print("," + process + ":");
      HashMap<InclusionRuleSpecies,Float> hm = speciesChange.get(process);
      fout.print(hm.size());
      for (InclusionRuleSpecies sp : hm.keySet()) {
        float pct = hm.get(sp);
        fout.print(":" + sp + ":" + pct);
      }
    }
  }

  private void saveSpeciesRange(PrintWriter fout) {
    fout.print(",");
    if (speciesRange == null || speciesRange.size() == 0) {
      fout.print(NODATA);
      return;
    }

    fout.print(speciesRange.size());
    for (InclusionRuleSpecies sp : speciesRange.keySet()) {
      fout.print("," + sp + ":");
      Range range = speciesRange.get(sp);
      fout.print(range.getLower() + ":" + range.getUpper());
    }
  }

  // ** End Parser Stuff **
  // ----------------------

  /**
   * Creates a current state from the species, size class, age, and density of the VegetativeType and return it as a string.
   * @return a String.
   */
  public String toString() {
    return getCurrentState();
  }

  public void export(PrintWriter fout) {
    fout.println(VEGETATIVE_TYPE + " " + getCurrentState());

    Process        process;
    VegetativeType state;
    Integer        prob;
    ProcessType[]  processes = Process.getLegalProcesses();

    for (int i=0; i<processes.length; i++) {
      process = Process.findInstance(processes[i]);
      state   = (VegetativeType) nextState.get(process);
      if (state == null) { continue; }

      prob    = (Integer) probability.get(process);
      fout.print("  " + Formatting.fixedField(process.toString(),25,true) + " ");
      fout.print(Formatting.fixedField(state.toString(),36,true) + " ");
      if (prob.intValue() != 0) {
        fout.print(Formatting.fixedField(prob, 3));
      }
      fout.println();
    }



//    Enumeration    keys = nextState.keys();
//    while (keys.hasMoreElements()) {
//      process = (Process) keys.nextElement();
//      state   = (VegetativeType) nextState.get(process);
//      prob    = (Integer) probability.get(process);
//      fout.print("  " + Formatting.fixedField(process.toString(),25,true) + " ");
//      fout.print(Formatting.fixedField(state.toString(),36,true) + " ");
//      fout.println(Formatting.fixedField(prob,3));
//    }

    Species posSpecies;
    Point p;
    Enumeration keys = positions.keys();
    while (keys.hasMoreElements()) {
      posSpecies = (Species) keys.nextElement();
      p          = (Point) positions.get(posSpecies);

      fout.println("  " + XY_COORDINATE + " " + posSpecies + " " + p.x + "," + p.y);
    }

    if (speciesChange != null) {
      for (ProcessType proc : speciesChange.keySet()) {
        HashMap<InclusionRuleSpecies, Float> hm = speciesChange.get(proc);
        for (InclusionRuleSpecies sp : hm.keySet()) {
          Formatter formatter = new Formatter(fout, Locale.US);

          Float pct = hm.get(sp);
          formatter.format("  %s %s %s %3.2f", SPECIES_CHANGE, proc, sp, pct);
          fout.println();
        }
      }
    }

    if (speciesRange != null) {
      for (InclusionRuleSpecies sp : speciesRange.keySet()) {
        Formatter formatter = new Formatter(fout, Locale.US);

        Range range = speciesRange.get(sp);
        formatter.format("  %s %s %d %d", SPECIES_RANGE, sp, range.lower,
                         range.upper);
        fout.println();
      }
    }
    fout.println();
  }

  public void printMagisAll(PrintWriter fout) {
    Enumeration    keys = nextState.keys();
    VegetativeType vt;
    Process        p;
    while(keys.hasMoreElements()) {
      fout.print(htGrp);
      fout.print(",");
      fout.print(species);
      fout.print(",");
      fout.print(sizeClass);
      fout.print(",");
      fout.print(density);
      fout.print(",");
      fout.print(age);
      fout.print(",");

      p = (Process)keys.nextElement();
      vt = (VegetativeType)nextState.get(p);

      fout.print(p);
      fout.print(",");
      fout.print(vt.getSpecies());
      fout.print(",");
      fout.print(vt.getSizeClass());
      fout.print(",");
      fout.print(vt.getDensity());
      fout.print(",");
      fout.print(vt.getAge());

      fout.println();
    }
  }

  public static void setLimitedSerialization() { limitedSerialization = true; }
  public static void clearLimitedSerialization() { limitedSerialization = false; }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    // Get the Treatment Saved State
    HabitatTypeGroupType htGrpType;
    if (version == 1) {
      htGrpType = (HabitatTypeGroupType) in.readObject();
    }
    else {
      htGrpType = HabitatTypeGroupType.readExternalSimple(in);
    }

    htGrp = HabitatTypeGroup.findInstance(htGrpType);
    if (htGrp == null) {
      htGrp = new HabitatTypeGroup(htGrpType.toString());
    }

    if (version == 1) {
      species = (Species) in.readObject();
      sizeClass = (SizeClass) in.readObject();
      age = in.readInt();
      density = (Density) in.readObject();
      makePrintName();
    }
    else {
      printName = (String) in.readObject();
      printName = printName.intern();
    }

    // Check to see if we need to read anything else.
    if (in.readBoolean()) { return; }
  }
  private Object readResolve () throws java.io.ObjectStreamException
  {
    try {
      VegetativeType vegType = htGrp.getVegetativeType(printName);
      return (vegType != null ? vegType : new VegetativeType(htGrp, printName));
    }
    // Will never happen, as we are parsing a veg type string that we know
    // for certain is valid.
    catch (ParseError ex) {
      return null;
    }
  }

  public static VegetativeType readExternalData(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    // Get the Treatment Saved State
//    HabitatTypeGroupType htGrpType = (HabitatTypeGroupType)in.readObject();
    HabitatTypeGroupType htGrpType = HabitatTypeGroupType.readExternalSimple(in);
    HabitatTypeGroup     htGrp = HabitatTypeGroup.findInstance(htGrpType);
    if (htGrp == null) {
      htGrp = new HabitatTypeGroup(htGrpType.toString());
    }

    String printName = (String) in.readObject();
    printName = printName.intern();

    // currently not used
    boolean moreData = in.readBoolean();

    try {
      VegetativeType vegType = htGrp.getVegetativeType(printName);
      return (vegType != null ? vegType : new VegetativeType(htGrp, printName));
    }
    // Will never happen, as we are parsing a veg type string that we know
    // for certain is valid.
    catch (ParseError ex) {
      return null;
    }
  }
/**
 * Write to an external source this vegetative type objects information.  Writes the print name which will be in form (if age is 1) 
 * species '/' size class '/' density (canopy cover).
 * If greater than 1 this will be species '/' size class age '/' density (canopy cover)
 * 
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    if (htGrp == null) {
      System.out.println(printName);
    }
    htGrp.getType().writeExternalSimple(out);
    out.writeObject(printName);

    out.writeBoolean(limitedSerialization);
    if (limitedSerialization) { return; }
  }
/**
 * Calculates the species change based on process (succession: wet succession, dry succession) and if process is Stand replacing fire in spring, summer, fall, winter - YEAR not included 
 * in calculation)  
 * @param process the process type: (succession: wet succession, dry succession)
 * @param season
 * @param sp
 * @return
 */
  public float getSpeciesChange(ProcessType process, Climate.Season season, InclusionRuleSpecies sp) {
    if (speciesChange == null) { return 0; }

    if (process == ProcessType.SRF) {
      switch (season) {
        case SPRING: process = ProcessType.SRF_SPRING; break;
        case SUMMER: process = ProcessType.SRF_SUMMER; break;
        case FALL:   process = ProcessType.SRF_FALL;   break;
        case WINTER: process = ProcessType.SRF_WINTER; break;
      }
    }
    if (process == ProcessType.SUCCESSION) {
      if (Simpplle.getClimate().isWetSuccession()) {
        process = ProcessType.WET_SUCCESSION;
      }
      else if (Simpplle.getClimate().isDrySuccession()) {
        process = ProcessType.DRY_SUCCESSION;
      }
    }

    HashMap<InclusionRuleSpecies,Float> hm = speciesChange.get(process);
    if (hm == null) { return 0; }

    Float ch = hm.get(sp);
    if (ch != null) {
      return ch.floatValue();
    }

    return 0.0f;
  }
/**
 *  Gets the range for a particular species.  
 */
  public Range getSpeciesRange(InclusionRuleSpecies sp) {
    if (speciesRange == null) { return null; }

    Range range = speciesRange.get(sp);
    if (range != null) {
      return range;
    }
    return null;
  }
/**
 * Clears the species range hashmap.  
 */
  public void clearSpeciesRange() {
    speciesRange = null;
  }
  public void addSpeciesRange(InclusionRuleSpecies species, int lower, int upper) {
    if (speciesRange == null) {
      speciesRange = new HashMap<InclusionRuleSpecies,Range>(3);
    }
    speciesRange.put(species,new Range(lower,upper));
  }

  public boolean validSpeciesFit(Flat3Map trkSpecies) {
    Range   range;
    if (speciesRange == null) { return false; }

    MapIterator it = trkSpecies.mapIterator();
    while (it.hasNext()) {
      InclusionRuleSpecies sp = (InclusionRuleSpecies)it.next();
      Float pct = (Float)it.getValue();
      if (pct == null) { continue; }

      range = speciesRange.get(sp);
      if (range == null) { continue; }

      if (range.inRange(pct) == false) { return false; }
    }
    return true;
  }
/**
 * Sets the species for this vegetative type.
 * @param species 
 */
  public void setSpecies(Species species) {
    this.species = species;
  }
/**
 * Sets the size class for this vegetative type.  
 * @param sizeClass
 */
  public void setSizeClass(SizeClass sizeClass) {
    this.sizeClass = sizeClass;
  }

  public void setDensity(Density density) {
    this.density = density;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setPrintName(String printName) {
    this.printName = printName;
  }
/**
 * Gets the column count for vegetative type.  This is a static final variable and returns 3
 * @return 3
 */
  public static int getColumnCount() {
    return COUNT;
  }
  /**
   * Gets the inclusion rules column count.  This is a static final variable and returns 3
   * @return 3
   */
  public static int getInclusionRulesColumnCount() {
    return INCLUSION_RULES_COLUMN_COUNT;
  }

  public int getSpeciesChangeRowCount() {
    return (speciesChange != null) ? speciesChange.size() : 0;
  }
  public int getInclusionRulesRowCount() {
    return (speciesRange != null) ? speciesRange.size() : 0;
  }

  public ArrayList makeSpeciesChangeArray() {
    if (speciesChange == null || speciesChange.size() == 0) { return null; }
    ArrayList rows = new ArrayList();
    Object[] colData;

    for (ProcessType process : speciesChange.keySet()) {
      HashMap<InclusionRuleSpecies,Float> hm = speciesChange.get(process);
      for (InclusionRuleSpecies sp : hm.keySet()) {
        colData = new Object[3];
        colData[0] = process;
        colData[1] = sp;
        colData[2] = hm.get(sp);
        rows.add(colData);
      }
    }
    return rows;
  }
  /**
   * Makes an inclusion rules arraylist containing a column data array from the speciesRange hashmap.  
   * The arraylist will have have the arrays with three indexes - 0=species, 1 = lower range, 2 = upper range.  
   * @return the arraylist with inclusion rules.  
   */
  public ArrayList makeInclusionRulesArray() {
    if (speciesRange == null || speciesRange.size() == 0) { return null; }
    ArrayList rows = new ArrayList();
    Object[] colData;

    for (InclusionRuleSpecies sp : speciesRange.keySet()) {
      colData = new Object[3];
      colData[0] = sp;
      Range range = speciesRange.get(sp);
      colData[1] = range.getLower();
      colData[2] = range.getUpper();
      rows.add(colData);
    }
    return rows;
  }

/**
 * Used in GUI table models.  Gets the species change name  by column Id.
 * @param col
 * @return Choices are "Process", "Inclusion Rule Tracking Species", "Percent Change", or null.
 */
  public static String getSpeciesChangeColumnName(int col) {
    switch (col) {
      case 0:  return "Process";
      case 1:  return "Inclusion Rule Tracking Species";
      case 2:  return "Percent Change";
      default: return null;
    }
  }
  /**
   * Used in GUI table models.  Gets the inclusion rules by column Id.  
   * Choices for this are "Inclusion Rule Species",  "Lower",  "Upper" or null
   */
  public static String getInclusionRulesColumnName(int col) {
    switch (col) {
      case 0:  return "Inclusion Rule Species";
      case 1:  return "Lower";
      case 2:  return "Upper";
      default: return null;
    }
  }

  public void addSpeciesChange(ProcessType process, InclusionRuleSpecies species) {
    if (speciesChange == null) {
      speciesChange = new HashMap<ProcessType,
                                  HashMap<InclusionRuleSpecies, Float>>();
    }
    HashMap<InclusionRuleSpecies,Float> hm = speciesChange.get(process);
    if (hm == null) {
      hm = new HashMap<InclusionRuleSpecies,Float>();
      speciesChange.put(process,hm);
    }
    hm.put(species,0.0f);
  }
  /**
   * Adds an inclusion rules species by making a new hashmap with species as key and range as value.  
   * @param species inclusion rule species to be added to the species range hashmap.  
   */
  public void addInclusionRule(InclusionRuleSpecies species) {
    if (speciesRange == null) { speciesRange = new HashMap<InclusionRuleSpecies,Range>(); }
    speciesRange.put(species,new Range(0,0));
  }


  public boolean removeSpeciesChange(ProcessType process, InclusionRuleSpecies species) {
    HashMap<InclusionRuleSpecies,Float> hm = speciesChange.get(process);
    if (hm == null) { return false; }
    if (hm.remove(species) == null) {
      return false;
    }
    return true;
  }
  /**
   * Checks if an inclusion rule exists for a species and therefore can be removed using species from the speciesRange (inclusion rules) hashmap.  This is keyed by the species, with range being the value.  
   * @param species the species checked for an inclusion rule
   * @return true if there is a inclusion rule for a particular species.  
   */
  public boolean removeInclusionRule(InclusionRuleSpecies species) {
    if (speciesRange.remove(species) == null) {
      return false;
    }
    return true;
  }
/**
 * Changes an inclusion rule for species 
 * @param process
 * @param species
 * @param value
 */
  public void setSpeciesChange(ProcessType process, InclusionRuleSpecies species, Float value) {
    HashMap<InclusionRuleSpecies, Float> hm = speciesChange.get(process);
    if (hm == null) { return; }

    hm.put(species,value);
  }
  public void setInclusionRule(InclusionRuleSpecies species, Integer lower, Integer upper) {
    speciesRange.put(species,new Range(lower,upper));
  }
/**
 * Gets a set of all tracking species with an inclusion rule.  
 * @return
 */
  public Set<InclusionRuleSpecies> getTrackingSpecies() {
    if (speciesChange == null) { return null; }
    for (ProcessType p : speciesChange.keySet()) {
      return speciesChange.get(p).keySet();
    }
    return null;
  }

  public boolean isTrackingSpecies(InclusionRuleSpecies sp) {
    if (speciesChange == null) { return false; }

    HashMap<InclusionRuleSpecies,Float> hm = speciesChange.get(ProcessType.SUCCESSION);
    return hm.containsKey(sp);
  }
}



/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.IOException;
import java.util.Vector;
import static simpplle.comcode.Evu.*;

/**
 * This class defines the the legacy Evu object.  It will most likely be eliminated in OpenSimpplle 1.0.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class LegacyEvu {
  private Evu              evu;

  private HabitatTypeGroup htGrp;
  private VegStateArray2D simulatedStates;
  private VegetativeState vegState;

  private String           unitNumber;
  private AdjacentData[]   adjacentData;

  private static ProcessProbability[] tempProcessProb;
  private ProcessProbability[]        needLaterProcessProb;

  private String           ownership;
  private int              roadStatus;
  private int              ignitionProb;
  private Fmz              fmz;
  private String           specialArea;

  private String           source;
  private String           associatedLandtype;
  private int[]            location;
  private Vector           timberVolume;
  private Vector           volumeRemovals;
  private Vector           treatment;  // simulation
  private int              lpMpbHazard;
  private int              ppMpbHazard;

  private VegStateArray2D accumState; // simulation


  private static String KEYWORD[] = {"END"};

  private static final int END = 0;

  private static final char   DELIM     = ',';
  private static final String LISTDELIM = ":";
  private static final String NODATA    = "?";

  public LegacyEvu(Evu evu) {
    this.evu = evu;
  }

  private boolean keyMatch (String key, int keyid) {
    return key.equalsIgnoreCase(KEYWORD[keyid]);
  }

  public void readAdjacentData (MyStringTokenizer strTok) throws ParseError, IOException {
    Vector       theList;
    int          id;
    char         position, wind;
    String       str,msg;

    adjacentData = null;

    theList = strTok.getListValue();
    if (theList == null) {
      return;
    }
    else {
      // Make sure the list is divisible by 3.
      // For each unit id, there should be two Characters.
      if ((theList.size() % 3) != 0) {
        throw new ParseError("Not enough elements in Adjacent Units list.");
      }
    }

    for(int i=0;i<theList.size();i=i+3) {
      try {
        id  = Integer.parseInt((String) theList.elementAt(i));
      }
      catch (NumberFormatException NFE) {
        msg = "Invalid integer found when reading Adjacent Units.";
        throw new ParseError(msg);
      }

      str = (String) theList.elementAt(i+1);
      if (str.length() > 1) {
        throw new ParseError("Invalid Position data in reading Adjacent Units.");
      }
      position = str.charAt(0);
      if (position != ABOVE && position != NEXT_TO && position != BELOW) {
        throw new ParseError("Invalid Position data in reading Adjacent Units.");
      }

      str = (String) theList.elementAt(i+2);
      if (str.length() > 1) {
        throw new ParseError("Invalid Wind data in reading Adjacent Units.");
      }
      wind = str.charAt(0);
      if (wind != DOWNWIND && wind != NO_WIND) {
        throw new ParseError("Invalid Wind data in reading Adjacent Units.");
      }

      Simpplle.getCurrentArea().addAdjacentData(evu,id,position,wind);
    }
    theList.removeAllElements();
    theList = null;
  }

  private VegetativeType readState (String state) throws ParseError {
    VegetativeType veg = null;

    if (state != null) {
      veg = htGrp.getVegetativeType(state);
    }

    if (veg == null) {
      throw new ParseError("Invalid Pathway State found: " + state);
    }
    return veg;
  }

  private ProcessType readProcess (String processName) throws ParseError {
    ProcessType pt;

    pt = ProcessType.get(processName);
    if (pt == null) {
      throw new ParseError("Invalid Process found: " + processName);
    }
    return pt;
  }

  private void readCurrentState(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector         v;
    VegetativeType veg;
    int            numStates = 0;

    v = strTok.getListValue();
    if (v != null) { numStates = v.size();}
    if (numStates < 1) {
      throw new ParseError("No Current State specified.");
    }

    if (numStates == 1) {
      vegState = new VegetativeState(readState((String) v.elementAt(0)));
      simulatedStates = null;
    }
    else {
      simulatedStates = new VegStateArray2D(1,numStates);
      for (int i=numStates-1; i>=0; i--) {
        veg = readState((String)v.elementAt(i));
        if (i==0) { vegState = new VegetativeState(veg); }
        simulatedStates.setVegetativeType(i,veg);
      }
    }
  }

  private void readAccumState(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector         v;
    int            numRuns, numSteps, j;

    v = strTok.getListValue();
    if (v == null) return;

    if (simulatedStates == null) {
      throw new ParseError("Invalid Current State found while attempting " +
                           "to read Accumulated state." + Simpplle.endl +
                           "Current State should have more than one item.");
    }
    numSteps = simulatedStates.getNumTimeSteps();
    numRuns = v.size() / numSteps;
    if ( (numRuns * numSteps) != v.size()) {
      throw new ParseError("Not enough items in Accumulated State.");
    }

    accumState = new VegStateArray2D(numRuns,numSteps,true);

    VegetativeType veg;
    for(int r=1; r<=numRuns; r++) {
      for(int ts=0; ts<numSteps; ts++) {
        veg = readState((String) v.elementAt(ts + ((r-1) * numSteps)));
        accumState.setVegetativeType(r,ts,veg);
      }
    }

    v.removeAllElements();
    v = null;
  }

  private void readAccumProcess(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector       v;
    int          numRuns, numSteps, j;
    RegionalZone zone = Simpplle.getCurrentZone();
    int          areaFileVersion = Simpplle.getCurrentArea().getFileVersion();

    v = strTok.getListValue();
    if (v == null) return;

    if (simulatedStates == null) {
      throw new ParseError("Invalid Current State found while attempting " +
                           "to read Accumulated Processes." + Simpplle.endl +
                           "Current State should have more than one item.");
    }
    numSteps = simulatedStates.getNumTimeSteps()-1;
    if (areaFileVersion != 1) { numSteps++; }

    numRuns = v.size() / numSteps;
    if ((numRuns * numSteps) != v.size()) {
      throw new ParseError("Not enough items in Accumulated Process.");
    }

    ProcessType  process;

    for(int r=1; r<=numRuns; r++) {
      for(int ts=0; ts<numSteps; ts++) {
        if ((areaFileVersion == 1) && (ts == 0)) {
          process = evu.getDefaultInitialProcess();
        }
        else {
          process = readProcess((String) v.elementAt(ts + ((r-1) * numSteps)));
        }
        accumState.setProcessType(r,ts,process);
      }
    }

    v.removeAllElements();
    v = null;
  }
  private void readProbList(MyStringTokenizer strTok) throws ParseError, IOException {
    String str;

    Vector v = strTok.getListValue();
    if (v == null) { return; }

    int size = v.size();
    int prob;

    for(int i=0;i<size;i++) {
      str = (String) v.elementAt(i);
      if (str.equals("D")) {
        prob = Evu.D;
      }
      else if (str.equals("L")) {
        prob = Evu.L;
      }
      else if (str.equals("S")) {
        prob = Evu.S;
      }
      else if (str.equals("SUPP")) {
        prob = Evu.SUPP;
      }
      else if (str.equals("SE")) {
        prob = Evu.SE;
      }
      else {
        try {
          prob = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
          prob = Evu.NOPROB;
        }
      }
      simulatedStates.setProbability(i+1,(short)prob);

    }
  }

  private void readProcessList(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector       v;
    String       val;
    int          size;
    ProcessType  process;
    Area         area = Simpplle.getCurrentArea();
    RegionalZone zone = Simpplle.getCurrentZone();

    v = strTok.getListValue();
    if (v == null) { return; }

    size     = v.size();
    if (area.getFileVersion() == 1) {
      size++;
      v.insertElementAt(Evu.getDefaultInitialProcess(),0);
    }

    for(int i=0;i<size;i++) {
      val = (String) v.elementAt(i);
      process = ProcessType.get(val);
      if (process == null) {
        throw new ParseError("In Evu: " + evu.getId() + " process: " + val +
                             "is not valid.");
      }
      simulatedStates.setProcessType(i,process);
    }
  }

  private void readRoadStatus(MyStringTokenizer strTok) throws ParseError, IOException {
    String val;

    val = strTok.getToken();
    if (val == null) {
      roadStatus = Roads.Status.UNKNOWN.getValue();
      return;
    }
    val = val.toUpperCase();

    roadStatus = Roads.Status.lookup(val).getValue();
  }

  private void readLocation(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector  v;
    Integer x, y;

    v = strTok.getListValue(true);
    if (v == null) { return; }

    if (v.size() < 2) {
      return;
    }

    x = (Integer) v.elementAt(0);
    y = (Integer) v.elementAt(1);
    if (x == null) { x = -1; }
    if (y == null) { y = -1; }
    location = new int[2];
    location[X] = x.intValue();
    location[Y] = y.intValue();
  }

  /**
   * Processes a line from an Area's input data file, which contain's
   * information defining a Evu.
   * @param strBuf a BufferedReader
   * @return a boolean, true if end of data reached.
   */
  public boolean readDelimitedData(StringBuffer strBuf) throws ParseError {
    String              value;
    MyStringTokenizer   strTok;
    RegionalZone        zone;

    zone = Simpplle.currentZone;
    if (zone == null) {
      throw new ParseError("A zone must be loaded first!.");
    }

    try {
      strTok = new MyStringTokenizer(strBuf,DELIM);

      value = strTok.nextToken();
      if (value == null) {
        throw new ParseError("Error reading line in file: " + strBuf.toString());
      }
      value = value.trim();
      if (keyMatch(value,END)) {return true;}
      strTok.reset();

      // Get the unit id number.
      evu.setId(strTok.getIntToken());

      // Get the Habitat Type Group.
      value = strTok.getToken();
      htGrp = HabitatTypeGroup.findInstance(value);
      if (htGrp == null) {
        throw new ParseError("Habitat Type group: " + value +
                             " is invalid.");
      }
      evu.setHabitatTypeGroup(htGrp);

      // Current State (A list)
      readCurrentState(strTok);

      // Get the Vegetative Type.
      // This should be the same as the first item
      // in the current state, but we will check later
      // and fix if necessary.
      value = strTok.getToken();
      VegetativeType vegType = htGrp.getVegetativeType(value);
      if (vegType == null) {
        throw new ParseError("Vegetative Type: " + value + " is invalid.");
      }
      // Probably not necessary but doesn't hurt to set it anyway.
      // Should be same as set by reading current state.
      vegState.setVegetativeType(vegType);

      evu.setState(vegType);

      // The rest of these are either Strings, Vectors, or ints.
      readProbList(strTok);
      readProcessList(strTok);

      if (simulatedStates != null) {
        int nSteps = simulatedStates.getNumTimeSteps();
        evu.initSimDataLegacy(nSteps);
        for (int ts = 1; ts <= nSteps; ts++) {
          // Run here is not correct, but there is no simple way of knowing
          // what the correct run number is.
          evu.newState(ts,0,
                       simulatedStates.getVegType(0, ts),
                       simulatedStates.getProcessType(0, ts),
                       simulatedStates.getProbability(0, ts),
                       Climate.Season.YEAR);
        }
        evu.setInitialProcess(simulatedStates.getProcessType(0,0));
      }

      unitNumber      = strTok.getToken();
      if (unitNumber != null) {
        unitNumber = unitNumber.intern();
      }
      evu.setUnitNumber(unitNumber);

      readAdjacentData(strTok);
      evu.setAcres(strTok.getIntToken());
      ownership    = strTok.getToken();
      if (ownership != null) {
        ownership = ownership.intern();
      }

      readRoadStatus(strTok);
      evu.setRoadStatus(Roads.Status.lookup(roadStatus));
      ignitionProb = strTok.getIntToken();
      evu.setIgnitionProb(ignitionProb);

      // ** FMZ **
      value = strTok.getToken();
      fmz   = zone.getFmz(value);
      // If this fmz does not exist, than create one with
      // its name which is a duplicate of the default fmz.
      if (fmz == null && value != null) {
        fmz = zone.getDefaultFmz().duplicate(value);
        zone.addFmz(fmz);
      }
      else if (fmz == null) {
        fmz = zone.getDefaultFmz();
      }
      evu.setFmz(fmz);

      specialArea  = strTok.getToken();
      if (specialArea != null) {
        specialArea = specialArea.intern();
      }
      evu.setSpecialArea(specialArea);

      source             = strTok.getToken();
      associatedLandtype = strTok.getToken();
      readLocation(strTok);
      evu.setLocationX(location[X]);
      evu.setLocationY(location[Y]);
      timberVolume       = strTok.getListValue();
      volumeRemovals     = strTok.getListValue();

      readTreatment(strTok);

      readAccumState(strTok);
      readAccumProcess(strTok);

    }
    catch (ParseError PE) {
      throw new ParseError ("Error reading EVU: " + evu.getName() + " " +
                            PE.msg);
    }
    catch (IOException IOX) {
      throw new ParseError ("Could not read a line in EVU: " + evu.getName());
    }
    return false;
  }

  private void readTreatment(MyStringTokenizer strTok)
    throws ParseError, IOException
  {
    Vector v = strTok.getListValue();
    if (v == null) {
      treatment = null;
      return;
    }
    Treatment treat;

    treatment = new Vector(v.size());
    for(int i=0; i<v.size(); i++) {
      treat = Treatment.read(evu,(String)v.elementAt(i));
      treatment.addElement(treat);
    }
  }

}

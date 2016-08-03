/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.text.*;
import java.util.*;
import org.hibernate.HibernateException;
import java.sql.SQLException;
import org.hibernate.*;

/**
 * This class contains fields and method relating to Existing Aquatic States.  The class is utilized to provide a water source for bison grazing logic.
 * Eau's must be classified as perennial or intermitent.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class ExistingAquaticUnit extends NaturalElement implements Externalizable {
  static final long serialVersionUID = -5064083681640375775L;
  static final int  version          = 1;

  private static final String COMMA         = ",";
  private static final String COLON         = ":";
  private static final String SEMICOLON     = ";";
  private static final String QUESTION_MARK = "?";

  public static enum AllStatus { PERENNIAL, INTERMITTENT };

  public static AllStatus PERENNIAL    = AllStatus.PERENNIAL;
  public static AllStatus INTERMITTENT = AllStatus.INTERMITTENT;

  private LtaValleySegmentGroup     group;
  private int                       segmentNumber;
  private PotentialAquaticState     currentState;
  private PotentialAquaticState[]   pastState;
  private ArrayList                 adjacentEvus;
  private ArrayList                 uplandEvus;
  private int[]                     probList;
  private Process                   initialProcess;
  private Process[]                 processList;
  private ArrayList                 predecessors;
  private ArrayList                 successors;
  private PotentialAquaticState     aquaticState;
  private LinkedList                processProb;
  private Vector                    treatment;
  private PotentialAquaticState[][] accumState;
  private Process[][]               accumProcess;

  private AllStatus status;

  // ** Simulation Related **
//  private Evu              originUnit;
//  private Process          originProcess;

  // Special note on length
  // Acres is really a float with n digits of precision.
  // It is stored as an int, to avoid the complications
  // that floating point numbers can cause, especially
  // with respect to comparisons.
  // Number of digits of precision is set in a static
  // variable in the Area class.
  // Acres is displayed to the user as a floating
  // point number by dividing acres by (10^n)
  private int length;




  // Making this static avoids constant allocation/deallocation.
  private static LinkedList cumulProb = new LinkedList();

  // used in probList;
  public static final int NOPROB = -1;
  public static final int D      = -2;
  public static final int L      = -3; // Locked in Process
  public static final int S      = -4; // Spread

  // For use in writing files
  public static final String D_STR      = "D";
  public static final String L_STR      = "L"; // Locked in Process
  public static final String S_STR      = "S"; // Spread
  public static final String NOPROB_STR = "NA";

  private static final ProcessType defaultInitialProcess = ProcessType.STREAM_DEVELOPMENT;

  // ** Parsing Stuff **
  private static String KEYWORD[] = {"END"};

  private static final int END = 0;

  private static final char   DELIM     = ',';
  private static final String LISTDELIM = ":";
  private static final String NODATA    = "?";
  // ** End Parsing Stuff **

  /**
   * Constructor Initializes fields to default values.
   */
  public ExistingAquaticUnit() {
    group             = null;
    currentState      = null;
    pastState         = null;
    probList          = null;
    initialProcess    = null;
    processList       = null;
    predecessors = new ArrayList(0);
    successors   = new ArrayList(0);
    adjacentEvus = new ArrayList(0);
    uplandEvus   = new ArrayList(0);
    aquaticState      = null;
    length            = 0;
    processProb       = new LinkedList();
    treatment         = null;
    accumState        = null;
    accumProcess      = null;
    status            = PERENNIAL;
//    originUnit        = null;
//    originProcess     = null;
  }

  public ExistingAquaticUnit(int newId) {
    this();
    id = newId;
  }

  public static ProcessType getDefaultInitialProcess() { return defaultInitialProcess; }

  public boolean equals(Object obj) {
    if ((obj != null) && (obj instanceof ExistingAquaticUnit)) {
      return id == ((ExistingAquaticUnit)obj).id;
    }
    return false;
  }

  public int hashCode() {
    return id;
  }

  public boolean isValid() {
    return(isLtaValleySegmentGroupValid() &&
           isCurrentStateValid()     &&
           isLengthValid()           &&
           isInitialProcessValid());
  }

  /**
   * Gets the print name of the Eau.
   * @return the Eau's name. (e.g. "EAU-1")
   */
  public String getName () { return "EAU-" + id; }

  /**
   * Gets the Eau's id.
   * @return the Eau id.
   */
  public int getId () { return id; }

  public int getSegmentNumber() { return segmentNumber; }
  public void setSegmentNumber(int value) { segmentNumber = value; }

  public AllStatus getStatus() { return status; }
  public void setStatus(AllStatus status) { this.status = status; }

  public boolean setStatus(String statusStr) {
    if (ExistingAquaticUnit.PERENNIAL.toString().equalsIgnoreCase(statusStr)) {
      setStatus(PERENNIAL);
    }
    else if (ExistingAquaticUnit.INTERMITTENT.toString().equalsIgnoreCase(statusStr)) {
      setStatus(INTERMITTENT);
    }
    else {
      return false;
    }
    return true;
  }

  public LtaValleySegmentGroup getLtaValleySegmentGroup () { return group; }

 /**
  * Used to set the eau data to a group the doesn't exists.
  * This allows the user to correct the incorrect data.
  * @param groupStr
  */
 
  private void setInvalidLtaValleySegmentGroup(String groupStr) {
    LtaValleySegmentGroup group = new LtaValleySegmentGroup(groupStr);
    setLtaValleySegmentGroup(group);
  }

 /**
  * Used by the gui to set the habitat type group
  * @param groupStr habitat group type
  */
  public void setLtaValleySegmentGroup(String groupStr) {
    LtaValleySegmentGroup group;
    PotentialAquaticState state;

    group = LtaValleySegmentGroup.findInstance(groupStr);
    if (group == null) {
      setInvalidLtaValleySegmentGroup(groupStr);
    }
    else {
      setLtaValleySegmentGroup(group);

      // State may have been invalid due to invalid group.
      // Need to check if that was the case.
      state = group.getPotentialAquaticState(getCurrentState().toString());
      if (state != null) {
        setCurrentState(state);
        setPotentialAquaticState(state);
      }
    }
  }

  public void setLtaValleySegmentGroup(LtaValleySegmentGroup group) { this.group = group; }

  public boolean isLtaValleySegmentGroupValid() {
    return group.isValid();
  }

  /**
   * Gets the Eau's Current State.
   * @return a PotentialAquaticState, the Eau's current state.
   */
  public PotentialAquaticState getCurrentState () {
    return currentState;
  }
  public void setCurrentState(PotentialAquaticState state) {
    currentState = state;
    pastState    = null;
  }
/**
 * Overloaded getCurrentState function.
 * If there is no past state or the past state is the same as this time step gets the Eau's current state at specified time step
 * othewise gets past state at the parameter time step. 
 * if no past states or gets current state based on number of past states in array. 
 * @param tStep time step for state sought
 * @return if paststate [] is null returns current state
 */
  public PotentialAquaticState getCurrentState(int tStep) {
    if (pastState == null) {
      return getCurrentState();
    }

    if (tStep == pastState.length) {
      return getCurrentState();
    }
    else {
      return getPastState(tStep);
    }
  }
/**
 * check to see if LtaValley segmnt is valid, if not assumes current Potential Aquatic State is valid
 * @return true if LtaValley segment is valid, or the current string literal of currentstate is not null
 */
  public boolean isCurrentStateValid() {
    if (isLtaValleySegmentGroupValid() == false) { return false; }

    return (group.getPotentialAquaticState(getCurrentState().toString()) != null);
  }
/**
 * 
 * @param tStep time step for state sought
 * @return
 */
  private PotentialAquaticState getPastState(int tStep) {
    PotentialAquaticState state;

    if (tStep > pastState.length) {
//      return htGrp.getVegetativeType("ND/ND/1");
      /** @todo find a better solution */
      return null;
    }
    else if (tStep < 0) {
      /** @todo find a better solution */
      return null;
//      return htGrp.getVegetativeType("ND/ND/1");
    }
    else {
      return pastState[tStep];
    }
  }

  /**
   * Gets the Eau's Aquatic Class in the current state.
   * @return an AquaticClass.
   */
  public AquaticClass getAquaticClass() { return getCurrentState().getAquaticClass();}
  /**
   * Gets the Eau's Aquatic Class at a specific time step.
   * @param tStep time step
   * @return
   */
  public AquaticClass getAquaticClass(int tStep) {
    return getCurrentState(tStep).getAquaticClass();
  }
  /**
   * Sets an aquatic class.  Choices are A1, A2, A3, A4, B2, B3, B4, C4,  D4, E4, G3, or G4.
   * @param aquaticClass Choices are A1, A2, A3, A4, B2, B3, B4, C4,  D4, E4, G3, or G4.
   */
  public void setAquaticClass(AquaticClass aquaticClass) {
    PotentialAquaticState state = null;

    if (this.isLtaValleySegmentGroupValid()) {
      state = group.getPotentialAquaticState(aquaticClass,getAquaticAttribute());
    }
    if (state == null) {
      state = new PotentialAquaticState(aquaticClass,getAquaticAttribute());
    }
    setCurrentState(state);
    setPotentialAquaticState(state);
  }
  /**
   * Chcks if an aquatic class is valid.  Choices are A1, A2, A3, A4, B2, B3, B4, C4,  D4, E4, G3, or G4.
   * @return true if valid
   */
  public boolean isAquaticClassValid() {
    return getAquaticClass().isValid();
  }

  /**
   * Gets the Eau's Aquatic Attribute.
   * @return an AquaticAttribute.
   */
  public AquaticAttribute getAquaticAttribute() { return getCurrentState().getAquaticAttribute();}
  public AquaticAttribute getAquaticAttribute(int tStep) {
    return getCurrentState(tStep).getAquaticAttribute();
  }
  public void setAquaticAttribute(AquaticAttribute attribute) {
    PotentialAquaticState state = null;

    if (isLtaValleySegmentGroupValid()) {
      state = group.getPotentialAquaticState(getAquaticClass(),attribute);
    }
    if (state == null) {
      state = new PotentialAquaticState(getAquaticClass(),attribute);
    }
    setCurrentState(state);
    setPotentialAquaticState(state);
  }

  public boolean isAquaticAttributeValid() {
    return getAquaticAttribute().isValid();
  }

  /**
   * Gets int value of the Eau's Length
   * @return length
   */
  public int getLength() { return length; }
  /**
   * 
   * @return a float value of Eau's length
   */
  public float getFloatLength() {
    return ( (float)length / (float)Utility.pow(10,Area.getLengthPrecision()) );
  }

  public void setLength(int val) { length = val; }
  public void setLength(float val) {
    length = Math.round(val * Utility.pow(10,Area.getLengthPrecision()));
  }

  public boolean isLengthValid() { return  (getLength() > 0); }

  /**
   * Gets the Process that occurred during the simulation time
   * step provided as the parameter.
   * @param tStep an int, the time step.
   * @return a Process or null.
   */
  public Process getProcess(int tStep) {
    if (processList == null && tStep == 0) {
      return initialProcess;
    }

    if (processList == null) {
      return null;
    }

    if (tStep >= processList.length) {
      return null;
    }
    else if (tStep < 0) {
      return null;
    }
    else {
      return processList[tStep];
    }
  }

  /**
   * Gets the Process that has occurred in the
   * current time step.
   * @return a Process, or null.
   */
  public Process getCurrentProcess () {
    Simulation simulation = Simpplle.getCurrentSimulation();

    if (processList == null || simulation == null) {
      return initialProcess;
    }
    else if (processList.length == 0) {
      return initialProcess;
    }
    else {
      return processList[simulation.getCurrentTimeStep()];
    }
  }

  /**
   * Updates the Current Process.
   * @param p a Process, the new current process.
   */
  public void updateCurrentProcess(Process p) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    processList[simulation.getCurrentTimeStep()] = p;
  }

  public Process getInitialProcess() { return initialProcess; }

  public void setInitialProcess(String processName) {
    ProcessType pt = ProcessType.get(processName);
    if (pt == null) {
      pt = ProcessType.makeInvalidInstance(processName);
      Process p = new InvalidProcess(pt,processName);
      setInitialProcess(p);
      return;
    }
    setInitialProcess(pt);
  }

  public void setInitialProcess(ProcessType processType) {
    Process p = Process.findInstance(processType);
    setInitialProcess(p);
  }

  public void setInitialProcess(Process p) {
    initialProcess = p;
    if (processList != null) {
      processList[0] = initialProcess;
    }
  }

  public boolean isInitialProcessValid() {
    return ((getInitialProcess() instanceof InvalidProcess) == false);
  }

  /**
   * Gets probability for the current process.
   * @return the probability.
   */
  public int getCurrentProb () {
    Simulation simulation = Simpplle.getCurrentSimulation();

    if (probList == null || simulation == null) {
      return NOPROB;
    }
    else if (probList.length == 0) {
      return NOPROB;
    }
    else {
      return probList[simulation.getCurrentTimeStep() - 1];
    }
  }
/**
 * gets probability for a designated time step
 * @param tStep the time step whose probablity is sought
 * @return probability of index into probability list or -1 if no probability (for initial state, or either an error in time step or probability calculation, 
 */
  public int getProb(int tStep) {
    if (tStep == 0) { return NOPROB; }

    if (probList == null || probList.length == 0) {
      return -1;
    }
    else if (tStep < 1 || tStep > probList.length) {
      return -1;
    }
    else {
      return probList[tStep - 1];
    }
  }

  /**
   * Updates the Current Process's probability.
   * @param prob the new probability.
   */
  public void updateCurrentProb(int prob) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    if (simulation == null) { return; }

    probList[simulation.getCurrentTimeStep() - 1] = prob;
  }

  public ProcessProbability findProcessProb(ProcessType pt) {
    if (pt == null) { return null; }
    ProcessProbability data;
    for (int i=0; i<processProb.size(); i++) {
      data = (ProcessProbability)processProb.get(i);
      if (data.processType.equals(pt)) { return data; }
    }
    return null;
  }
  /**
   * Gets the previously calculated probability of Process p occurring
   * for this Evu.
   * @param p a Process.
   * @return the probability.
   */
  public int getProcessProb(ProcessType p) {
    ProcessProbability probData = findProcessProb(p);
    if (probData != null) { return probData.probability; }
    return 0;
  }
/**
 * Gets the previously calculated probability of Process p occurring
 * for this Evu.
 * @param p
 * @return float version of the process probablity 
 */
  public float getFloatProcessProb(ProcessType p) {
    return ( (float)getProcessProb(p) / (float)Utility.pow(10,2) );
  }

  /**
   * Saves process and state data for the current run.
   * This information is saved during multiple run
   * simulation and is used to generate probabilities.
   */
  public void updateAccumData() {
    Simulation            simulation = Simpplle.getCurrentSimulation();
    int                   numSteps, cRun;
    int                   i, j;

    cRun     = simulation.getCurrentRun();
    numSteps = simulation.getNumTimeSteps();

    // Items are place in the vector in ascending order.
    // Primary order by run #, secondary order by time step.
    for(i=0;i<=numSteps;i++) {
      accumProcess[cRun][i] = getProcess(i);
      accumState[cRun][i]   = getCurrentState(i);
    }
  }

  /**
   * Gets the accumulated Current States that occurred during
   * a multiple run simulation.
   * @return a PotentialAquaticState[]
   */
  public PotentialAquaticState[][] getAccumState() { return accumState; }

  public PotentialAquaticState getAccumState(int run, int step) {
    run--;  // Runs start counting at 1, but we start at 0 in array
    if (run < 0 || step < 0 ||
        run  >= accumState.length ||
        step >= accumState[0].length) {
      return null;
    }
    return accumState[run][step];
  }

  public SimpplleType[][][] getAccumStateAttributes() {
    int nRuns  = Simpplle.getCurrentSimulation().getNumSimulations();
    int nSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    SimpplleType[][][] attribs = new SimpplleType[nRuns][nSteps+1][SimpplleType.MAX];

    for(int r=0; r<nRuns; r++) {
      for(int s=0; s<=nSteps; s++) {
        attribs[r][s][SimpplleType.AQUATIC_CLASS.ordinal()]     = accumState[r][s].getAquaticClass();
        attribs[r][s][SimpplleType.AQUATIC_ATTRIBUTE.ordinal()] = accumState[r][s].getAquaticAttribute();
        attribs[r][s][SimpplleType.AQUATIC_PROCESS.ordinal()]   = ProcessType.get(accumProcess[r][s].toString());
      }
    }
    return attribs;
  }

  /**
   * Gets the accumulated Processes that occurred during a
   * multiple run simulation.
   * @return a Process[]
   */
  public Process[][] getAccumProcess() { return accumProcess; }

  public Process getAccumProcess(int run, int step) {
    run--;  // Runs start counting at 1, but we start at 0 in array
    if (run < 0 || step < 0 ||
        run  >= accumProcess.length ||
        step >= accumProcess[0].length) {
      return null;
    }
    return accumProcess[run][step];
  }

  public void addAdjacentEvu(Evu unit) {
    if (adjacentEvus == null) { adjacentEvus = new ArrayList(); }
    if (adjacentEvus.contains(unit) == false) {
      adjacentEvus.add(unit);
    }
  }
  public void setAdjacentEvus(ArrayList newAdjEvus) {
    adjacentEvus = newAdjEvus;
  }
  public ArrayList getAdjacentEvus() { return adjacentEvus; }

  public void addUplandEvu(Evu unit) {
    if (uplandEvus == null) { uplandEvus = new ArrayList(); }
    if (uplandEvus.contains(unit) == false) {
      uplandEvus.add(unit);
    }
  }
  public void setUplandEvus(ArrayList newUplandEvus) {
    uplandEvus = newUplandEvus;
  }
  public ArrayList getUplandEvus() { return uplandEvus; }

  public void addPredecessor(ExistingAquaticUnit unit) {
    if (predecessors == null) { predecessors = new ArrayList(); }
    if (predecessors.contains(unit) == false) {
      predecessors.add(unit);
    }
  }
  public void setPredecessors(ArrayList newPredecessors) {
    if (newPredecessors == null) {
      predecessors = new ArrayList(0);
    }
    else {
      predecessors = newPredecessors;
    }
  }

  public ArrayList getPredecessors() {
    return predecessors;
  }

  public void addSuccessor(ExistingAquaticUnit unit) {
    if (successors == null) { successors = new ArrayList(); }
    if (successors.contains(unit) == false) {
      successors.add(unit);
    }
  }
  public void setSuccessors(ArrayList newSuccessors) {
    if (newSuccessors == null) {
      successors = new ArrayList(0);
    }
    else {
      successors = newSuccessors;
    }
  }
  public ArrayList getSuccessors() { return successors; }

  /**
   * Changes the current state of the evu to a new value.
   * @param state a PotentialAquaticState, the new state.
   */
  private void newCurrentState(PotentialAquaticState state) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    if (simulation == null) { return; }

    int cTime = simulation.getCurrentTimeStep() - 1;

    pastState[cTime] = currentState;
    currentState     = state;
  }

  /**
   * Updates the current Potential Aquatic State of the Aquatic Unit.
   * This is used during simulations.
   * @param state is a PotentialAquaticState.
   */
  public void updateCurrentState(PotentialAquaticState state) {
    if (state == null) {
      return;
    }
    else {
      currentState = state;
    }
  }

  /**
   * Gets the Potential Aquatic State for this Aquatic Unit.
   * @return a PotentialAquaticState.
   */
  public PotentialAquaticState getPotentialAquaticState() {
    return aquaticState;
  }

  public void setPotentialAquaticState(PotentialAquaticState state) { aquaticState = state; }

  public boolean isPotentialAquaticStateValid() {
    if (isLtaValleySegmentGroupValid() == false) { return false; }

    return (group.getPotentialAquaticState(aquaticState.toString()) != null);
  }

  /**
   * Called if the user loads their own pathway file which replaces
   * one already loaded by the system.  The function makes sure that
   * the Potential Aquatic States are remapped to the newly loaded ones and that
   * Potential Aquatic States that do not exist are created so they can be identified
   * to the user as invalid states and corrected via the unit editor
   * or by loading a pathway file that has the missing data.
   * Note:  Since we have to change the current state, all simulation
   *        will be cleared before this function is called.
   * @return returns true if loading of the new pathways created an invalid PotentialAquaticState
   */

  public boolean updatePathwayData() {
    AquaticClass     aquaticClass = getAquaticClass();
    AquaticAttribute attribute    = getAquaticAttribute();
    String           stateStr     = aquaticState.toString();
    boolean          isInvalid    = false;

    if (updateLtaVsGroup()) { isInvalid = true; }

    aquaticState = group.getPotentialAquaticState(stateStr);
    if (aquaticState == null) {
      aquaticState = new PotentialAquaticState(aquaticClass,attribute);
      setCurrentState(aquaticState);
      isInvalid = true;
    }

    return isInvalid;
  }

  /**
   * Remap the Lta Valley Segment Group reference to the newly loaded pathway.
   * if the Lta Vs Group does not exist then mark create
   * a new one.  The area will later be marked invalid because
   * we created an invalid Lta Vs Group.
   */
  public boolean updateLtaVsGroup() {
    RegionalZone     zone = Simpplle.getCurrentZone();
    String           groupName = group.getName();

    group = LtaValleySegmentGroup.findInstance(groupName);
    if (group == null) {
      group = new LtaValleySegmentGroup(groupName);
      return true;
    }
    return false;
  }

  // ** Parsing Stuff **

  private static boolean keyMatch (String key, int keyid) {
    return key.equalsIgnoreCase(KEYWORD[keyid]);
  }

  public void readAdjacentEvus (MyStringTokenizer mainStrTok) throws ParseError, IOException {
    adjacentEvus = readEvus(mainStrTok);
  }
  public void readUplandEvus (MyStringTokenizer mainStrTok) throws ParseError, IOException {
    uplandEvus = readEvus(mainStrTok);
  }

  private ArrayList readEvus (MyStringTokenizer mainStrTok) throws ParseError, IOException {
    StringTokenizerPlus strTok;
    int                 id;
    Evu                 evu;
    ArrayList           units = null;

    String str = mainStrTok.getToken();
    if (str == null) { return null; }
    strTok = new StringTokenizerPlus(str,COLON);

    try {
      units = new ArrayList(strTok.countTokens());
      while (strTok.hasMoreTokens()) {
        id = strTok.getIntToken();
        evu  = Simpplle.getCurrentArea().getEvu(id);
        if (evu == null)  { continue; }
        units.add(evu);
      }
      return units;
    }
    catch (Exception err) {
      throw new ParseError(err.getMessage() +
                           "Invalid data found while reading Adjacent Evu's");
    }
  }

  public void readPredecessors(MyStringTokenizer mainStrTok) throws ParseError, IOException {
    predecessors = readAquaticUnits(mainStrTok);
  }
  public void readSuccessors(MyStringTokenizer mainStrTok) throws ParseError, IOException {
    successors = readAquaticUnits(mainStrTok);
  }

  private ArrayList readAquaticUnits (MyStringTokenizer mainStrTok) throws ParseError, IOException {
    StringTokenizerPlus   strTok;
    int                   id;
    ExistingAquaticUnit   unit;
    ArrayList             units = new ArrayList(0);
    Area                  area = Simpplle.getCurrentArea();

    String str = mainStrTok.getToken();
    if (str == null) { return units; }
    strTok = new StringTokenizerPlus(str,COLON);

    try {
      units = new ArrayList(strTok.countTokens());
      while (strTok.hasMoreTokens()) {
        id = strTok.getIntToken();
        if (id < 0) {
          throw new ParseError("Invalid Aqautic Unit id found in predecessors/successors list");
        }
        unit = area.getEau(id);
        if (unit == null) { unit = area.getNewEau(id); }
        units.add(unit);
      }
      return units;
    }
    catch (Exception err) {
      throw new ParseError(err.getMessage() +
                           "Invalid data found while reading predecessors/successors");
    }
  }

  private PotentialAquaticState readState (String stateStr) throws ParseError {
    PotentialAquaticState state = null;

    if (stateStr != null) {
      state = group.getPotentialAquaticState(stateStr);
    }

    if (state == null) {
      throw new ParseError("Invalid Pathway State found: " + stateStr);
    }
    return state;
  }

  private Process readProcess (String processName) throws ParseError {
    Process     p = null;
    ProcessType pt;

    pt = ProcessType.get(processName);
    if (pt != null) {
      p = Process.findInstance(pt);
    }

    if (p == null) {
      throw new ParseError("Invalid Process found: " + processName);
    }
    return p;
  }

  private void readCurrentState(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector                v;
    PotentialAquaticState state;
    int                   numStates = 0, i, j;

    v = strTok.getListValue();
    if (v != null) { numStates = v.size();}
    if (numStates < 1) {
      throw new ParseError("No Current State specified.");
    }
    currentState = readState((String) v.elementAt(0));
    pastState    = null;

    if (numStates > 1) {
      pastState = new PotentialAquaticState[numStates - 1];

      j = numStates - 2;
      for (i=1;i<numStates;i++) {
        state = readState((String) v.elementAt(i));
        pastState[j] = state;
        j--;
      }
    }
  }

  private void readAccumState(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector                v;
    PotentialAquaticState state;
    int                   numRuns, numSteps, j;

    v = strTok.getListValue();
    if (v == null) return;

    if (pastState == null) {
      throw new ParseError("Invalid Current State found while attempting " +
                           "to read Accumulated state." + Simpplle.endl +
                           "Current State should have more than one item.");
    }
    numSteps = pastState.length;
    numSteps++;  // time step 0.
    numRuns = v.size() / numSteps;
    if ( (numRuns * numSteps) != v.size()) {
      throw new ParseError("Not enough items in Accumulated State.");
    }
    accumState = new PotentialAquaticState[numRuns][numSteps];

    for(int i=0;i<numRuns;i++) {
      for(j=0;j<numSteps;j++) {
        state = readState((String) v.elementAt(j + (i * numSteps)));
        accumState[i][j] = state;
      }
    }
  }

  private void readAccumProcess(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector       v;
    Process      process;
    int          numRuns, numSteps, j;
    RegionalZone zone = Simpplle.getCurrentZone();
    int          areaFileVersion = Simpplle.getCurrentArea().getFileVersion();

    v = strTok.getListValue();
    if (v == null) return;

    if (pastState == null) {
      throw new ParseError("Invalid Current State found while attempting " +
                           "to read Accumulated Processes." + Simpplle.endl +
                           "Current State should have more than one item.");
    }
    numSteps = pastState.length;
    if (areaFileVersion != 1) { numSteps++; }

    numRuns = v.size() / numSteps;
    if ((numRuns * numSteps) != v.size()) {
      throw new ParseError("Not enough items in Accumulated Process.");
    }
    accumProcess = new Process[numRuns][numSteps];

    for(int i=0;i<numRuns;i++) {
      for(j=0;j<numSteps;j++) {
        if ((areaFileVersion == 1) && (j == 0)) {
          process = Process.findInstance(defaultInitialProcess);
        }
        else {
          process = readProcess((String) v.elementAt(j + (i * numSteps)));
        }
        accumProcess[i][j] = process;
      }
    }
  }

  private void readProbList(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector  v;
    String  str;
    int     size;

    v = strTok.getListValue();
    if (v == null) { return; }

    size     = v.size();
    probList = new int[size];

    for(int i=0;i<size;i++) {
      str = (String) v.elementAt(i);
      if (str.equals("D")) {
        probList[i] = D;
      }
      else if (str.equals("L")) {
        probList[i] = L;
      }
      else if (str.equals("S")) {
        probList[i] = S;
      }
      else {
        try {
          probList[i] = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
          throw new ParseError("Invalid Probability value.");
        }
      }
    }
  }

  private void readProcessList(MyStringTokenizer strTok) throws ParseError, IOException {
    Vector       v;
    String       val;
    int          size;
    Process      process;
    Area         area = Simpplle.getCurrentArea();
    RegionalZone zone = Simpplle.getCurrentZone();

    initialProcess = Process.findInstance(defaultInitialProcess);

    v = strTok.getListValue();
    if (v == null) { return; }

    size     = v.size();
    if (area.getFileVersion() == 1) {
      size++;
      v.insertElementAt(defaultInitialProcess,0);
    }
    processList = new Process[size];

    for(int i=0;i<size;i++) {
      val = (String) v.elementAt(i);
      process = Process.findInstance(val);
      if (process == null) {
        throw new ParseError("In Existing Aquatic Unit: " + id + " process: " + val +
                             "is not valid.");
      }
      processList[i] = process;
    }
    if (area.getFileVersion() != 1) {
      initialProcess = processList[0];
    }
  }

  /**
   * Processes a line from an Area's input data file, which contain's
   * information defining a Evu.
   * @param fin a BufferedReader
   * @return a boolean, true if end of data reached.
   */
  public static ExistingAquaticUnit readDelimitedData(StringBuffer strBuf)
    throws ParseError
  {
    String              value;
    MyStringTokenizer   strTok;
    RegionalZone        zone;
    ExistingAquaticUnit unit=null;
    Area                area = Simpplle.getCurrentArea();

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
      if (keyMatch(value,END)) {return null;}
      strTok.reset();

      // Get the unit id number.
      int id = strTok.getIntToken();
      unit = area.getEau(id);
      if (unit == null) {
        unit = area.getNewEau(id);
      }
      unit.readDelimitedData(strTok);
    }
    catch (ParseError PE) {
      String name = (unit != null) ? unit.getName() : "Unknown";
      throw new ParseError ("Error reading Existing Aquatic Unit: " + name + " " +
                            PE.msg);
    }
    catch (IOException IOX) {
      String name = (unit != null) ? unit.getName() : "Unknown";
      throw new ParseError ("Could not read a line in Existing Aquatic Unit: " + name);
    }
    return unit;

  }
  //,pred,succ,adj-evu,upland,evu,pas-state,length

  private void readDelimitedData(MyStringTokenizer strTok)
    throws ParseError, IOException
  {
    String value;

    // Get the Lta Valley Segment Group
    value = strTok.getToken();
    group = LtaValleySegmentGroup.findInstance(value);
    if (group == null) {
      throw new ParseError("Lta Valley Segment Group: " + value +
                           " is invalid.");
    }

    // Current State (A list)
    readCurrentState(strTok);

    segmentNumber = strTok.getIntToken();

    readPredecessors(strTok);
    readSuccessors(strTok);
    readAdjacentEvus(strTok);
    readUplandEvus(strTok);

    // Get the Potential Aquatic State.
    // This should be the same as the first item
    // in the current state, but we will check later
    // and fix if necessary.
    value        = strTok.getToken();
    aquaticState = group.getPotentialAquaticState(value);
    if (aquaticState == null) {
      throw new ParseError("Potential Aquatic State: " + value + " is invalid.");
    }

    length = strTok.getIntToken();

    readProbList(strTok);
    readProcessList(strTok);

    readAccumState(strTok);
    readAccumProcess(strTok);
  }


  // ** End Parser Stuff **
  // ----------------------

  private void printEvus(PrintWriter fout, ArrayList data) {
    for (int i=0; i<data.size(); i++) {
      fout.print(" " + ((ExistingAquaticUnit)data.get(i)).getId());
    }
  }
  /**
   * Prints the Eau's by Id.  
   * @param fout
   * @param data
   */
  private void printAquaticUnits(PrintWriter fout, ArrayList data) {
    ExistingAquaticUnit unit;
    for (int i=0; i<data.size(); i++) {
      unit = (ExistingAquaticUnit)data.get(i);
      fout.print(" " + unit.getId());
    }
  }

  /**
   * Print out an human readable representation of the Evu.
   * @param fout a PrintWriter.
   */
  public void printAll(PrintWriter fout) {
    int          i=0, j, prob;
    String       str;
    int          adjId;
    double       tmpProb;
    NumberFormat nf;

    fout.println("Note: Times steps are shown in ascending order" +
                 " (left to right).");

    fout.println();
    fout.println("CLASS TYPE               = Existing Aquatic Unit");
    fout.println("ID                       = " + id);
    fout.println("LTA VALLEY SEGMENT GROUP = " + group);
    fout.println("CURRENT STATE:");
    if (pastState != null) {
      for(i=0;i<pastState.length;i++) {
        if (pastState[i] != null) {
          fout.print("  " + pastState[i]);
        }
      }
    }
    fout.println("  " + currentState);

    if (adjacentEvus != null) {
      fout.println("ADJACENT EVU:");
      printEvus(fout,adjacentEvus);
      fout.println();
    }
    if (uplandEvus != null) {
      fout.println("UPLAND EVU:");
      printEvus(fout,uplandEvus);
      fout.println();
    }
    if (predecessors != null) {
      fout.println("PREDECESSORS:");
      printAquaticUnits(fout,predecessors);
      fout.println();
    }
    if (successors != null) {
      fout.println("SUCCESSORS:");
      printAquaticUnits(fout,successors);
      fout.println();
    }

    if (probList != null) {
      nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(2);
      fout.print("PROB LIST: ");
      for(i=0;i<probList.length;i++) {
        prob = probList[i];
        if      (prob == D)    { str = "D"; }
        else if (prob == L)    { str = "L"; }
        else if (prob == S)    { str = "S"; }
        else {
          tmpProb = prob / 100.0;
          str = nf.format(tmpProb);
        }
        fout.print("  " + str);
      }
      fout.println();
    }

    if (processList != null) {
      fout.println("PROCESS LIST:");
      for(i=0;i<processList.length;i++) {
        fout.print("  " + processList[i]);
      }
      fout.println();
    }

    if (accumState != null && accumProcess != null) {
      fout.println("ACCUM-STATE:");
      for(i=0; i<accumState.length; i++) {
        for(j=0; j<accumState[i].length; j++) {
          fout.println("  " + accumState[i][j] + " ");
        }
      }
      fout.println();
      fout.println("ACCUM-PROCESS:");
      for(i=0;i<accumProcess.length;i++) {
        for(j=0;j<accumProcess[i].length;j++) {
          fout.println("  " + accumProcess[i][j] + " ");
        }
      }
      fout.println();
    }

    fout.println("POTENTIAL AQUATIC STATE = " + aquaticState);
    fout.println("LENGTH                  = " + length);
    fout.println("=========================================");
    fout.println();
  }


  // ------------------------
  // ** Simulation Methods **
  // ------------------------

  /**
   * release some memory that's no longer needed.
   */
  public void cleanup() {
    processProb = null;
  }

  public void exportNeighbors(PrintWriter fout) {
    // Unit, Adjacent, Flow Director (P, S) Pred/Succ
    ExistingAquaticUnit unit;
    int i=0;
    if (predecessors != null) {
      for (i = 0; i < predecessors.size(); i++) {
        unit = (ExistingAquaticUnit)predecessors.get(i);
        fout.print(getId());
        fout.print(COMMA);
        fout.print(unit.getId());
        fout.print(COMMA);
        fout.println("P");
      }
    }

    if (successors != null) {
      for (i = 0; i < successors.size(); i++) {
        unit = (ExistingAquaticUnit)successors.get(i);
        fout.print(getId());
        fout.print(COMMA);
        fout.print(unit.getId());
        fout.print(COMMA);
        fout.println("S");
      }
    }

    if (predecessors == null && successors == null) {
      fout.print(getId());
      fout.print(COMMA);
      fout.print(getId());
      fout.print(COMMA);
      fout.println("N");
    }
  }
  public void exportNeighborsVegetation(PrintWriter fout) {
    // ID,ADJ, U or A (upland or Adjacent)
    int i;
    if (uplandEvus != null) {
      for (i = 0; i < uplandEvus.size(); i++) {
        fout.print(getId());
        fout.print(COMMA);
        fout.print(((Evu)uplandEvus.get(i)).getId());
        fout.print(COMMA);
        fout.println("U");
      }
    }

    if (adjacentEvus != null) {
      for (i = 0; i < adjacentEvus.size(); i++) {
        fout.print(getId());
        fout.print(COMMA);
        fout.print(((Evu)adjacentEvus.get(i)).getId());
        fout.print(COMMA);
        fout.println("A");
      }
    }
  }
  public void exportAttributes(PrintWriter fout) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);

    fout.print(getId());
    fout.print(COMMA);
    fout.print(nf.format(Area.getFloatLength(getLength())));
    fout.print(COMMA);
    fout.print(getLtaValleySegmentGroup());
    fout.print(COMMA);
    fout.print(getAquaticClass());
    fout.print(COMMA);
    fout.print(getAquaticAttribute());
    fout.print(COMMA);
    fout.print(getSegmentNumber());
    fout.print(COMMA);
    if (getInitialProcess() != null) {
      fout.print(getInitialProcess());
    }
    else { fout.print(QUESTION_MARK); }

    fout.print(COMMA);
    fout.print(status.toString());
    fout.println();
  }

  public String[] getPredecessorDisplay() {
    if (predecessors == null) { return null; }

    String[] strList = new String[predecessors.size()];
    ExistingAquaticUnit unit;

    for(int i=0;i<predecessors.size();i++) {
      unit = (ExistingAquaticUnit)predecessors.get(i);
      strList[i] = Integer.toString(unit.getId()) + "      ";
    }
    return strList;
  }
  public String[] getSuccessorDisplay() {
    if (successors == null) { return null; }

    String[] strList = new String[successors.size()];
    ExistingAquaticUnit unit;

    for(int i=0;i<successors.size();i++) {
      unit = (ExistingAquaticUnit)successors.get(i);
      strList[i] = Integer.toString(unit.getId()) + "      ";
    }
    return strList;
  }
  public String[] getUplandVegDisplay() {
    if (uplandEvus == null) { return null; }

    String[] strList = new String[uplandEvus.size()];

    for(int i=0;i<uplandEvus.size();i++) {
      strList[i] = Integer.toString(((Evu)uplandEvus.get(i)).getId()) + "      ";
    }
    return strList;
  }
  public String[] getAdjVegDisplay() {
    if (adjacentEvus == null) { return null; }

    String[] strList = new String[adjacentEvus.size()];

    for(int i=0;i<adjacentEvus.size();i++) {
      strList[i] = Integer.toString(((Evu)adjacentEvus.get(i)).getId()) +
                              "      ";
    }
    return strList;
  }


/**
 * Gets the EAU Id.  
 */
  public String toString() {
    return id + "-" + getCurrentState();
  }
/**
 * Reads in the adjacent Eau info.  
 * @param in
 * @param area
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public void readExternalNeighbors(ObjectInput in, Area area) throws IOException, ClassNotFoundException {
    int size = in.readInt();
    predecessors = new ArrayList(size);
    for (int i=0; i<size; i++) {
      predecessors.add(area.getEau(in.readInt()));
    }

    size = in.readInt();
    successors = new ArrayList(size);
    for (int i=0; i<size; i++) {
      successors.add(area.getEau(in.readInt()));
    }
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    group = LtaValleySegmentGroup.findInstance((String)in.readObject());
    segmentNumber = in.readInt();

    currentState = group.getPotentialAquaticState((String)in.readObject());
    aquaticState = currentState;
    initialProcess = Process.findInstance(ProcessType.readExternalSimple(in));

    length = in.readInt();

    Area area = Simpplle.getCurrentArea();

    int size = in.readInt();
    uplandEvus = new ArrayList(size);
    for (int i=0; i<size; i++) {
      uplandEvus.add(area.getEvu(in.readInt()));
    }

    size = in.readInt();
    adjacentEvus = new ArrayList(size);
    for (int i=0; i<size; i++) {
      adjacentEvus.add(area.getEvu(in.readInt()));
    }

    String statusStr = (String)in.readObject();
    if (statusStr.equals("PERENNIAL")) {
      status = PERENNIAL;
    }
    else if (statusStr.equals("INTERMITTENT")) {
      status = INTERMITTENT;
    }
  }

  public void writeExternalNeighbors(ObjectOutput out) throws IOException {
    out.writeInt(predecessors.size());
    for (Object item : predecessors) {
      out.writeInt(((ExistingAquaticUnit)item).getId());
    }

    out.writeInt(successors.size());
    for (Object item : successors) {
      out.writeInt(((ExistingAquaticUnit)item).getId());
    }
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeObject(group.toString());
    out.writeInt(segmentNumber);
    out.writeObject(currentState.toString());
    initialProcess.getType().writeExternalSimple(out);

    out.writeInt(length);

    if (uplandEvus == null) {
      out.writeInt(0);
    }
    else {
      out.writeInt(uplandEvus.size());
      for (Object item : uplandEvus) {
        out.writeInt(((Evu) item).getId());
      }
    }

    if (adjacentEvus == null) {
      out.writeInt(0);
    }
    else {
      out.writeInt(adjacentEvus.size());
      for (Object item : adjacentEvus) {
        out.writeInt(((Evu) item).getId());
      }
    }

    switch (status) {
      case PERENNIAL: out.writeObject("PERENNIAL"); break;
      case INTERMITTENT: out.writeObject("INTERMITTENT"); break;
    }
  }

  public float getFloatProb(int tStep) {
    return ( (float)getProb(tStep) / (float)Utility.pow(10,Area.getLengthPrecision()) );
  }

  public String getProbString(int tStep) {
    int prob = getProb(tStep);
    switch (prob) {
      case D:    return D_STR;
      case L:    return L_STR;
      case S:    return S_STR;
      case NOPROB: return NOPROB_STR;
      default:   return IntToString.get(prob);
    }
  }

  public void writeAccumDatabase(Session session) throws HibernateException, SQLException {
    int run = Simpplle.getCurrentSimulation().getCurrentRun();
    int nSteps = (probList != null) ? probList.length : 0;
    AccumDataAquatic.writeDatabase(session,this,run,nSteps);
  }
/**
 * True if Eau is perennial.  
 * @return
 */
  public boolean isWaterPresent() {
    if (getStatus() == PERENNIAL) {
      return true;
    }
// JChew 19 Aug 2005  Haufler requested this turned off.
//    else if (getStatus() == INTERMITTENT) {
//      return Simpplle.getClimate().isWet();
//    }
    return false;
  }

  public boolean isPermanentWater() {
    return (getStatus() == PERENNIAL);
  }

  /**
   * Currently does nothing.
   */
  public void initSimulation() {

  }

  public void doFuture() {
  }
}






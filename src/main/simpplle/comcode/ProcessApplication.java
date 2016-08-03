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

/**
 * This class represents a user scheduled process that is to be applied to a fixed set of units.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ProcessApplication {
  private Vector  units;
  private int     timeStep;
  private Process process;

  /**
   * The primary Process Application constructor. 
   */
  public ProcessApplication() {
    units    = new Vector();
    timeStep = -1;
  }
/**
 * Overloaded constructor for process application.  Allows user to select a process to be applied.  
 * Gets the current zone and sets the process for application.  
 * @param processType
 */
  public ProcessApplication (ProcessType processType) {
    this();
    RegionalZone zone = Simpplle.getCurrentZone();

    process = Process.findInstance(processType);
  }
/**
 * Duplicates this process application.  
 * @return the duplicate version of this process application.  
 */
  public ProcessApplication duplicate() {
    ProcessApplication newApp;

    newApp = new ProcessApplication(getProcessType());

    // will not copy time step, we want user to set new one.
    newApp.units = (Vector) units.clone();

    return newApp;
  }
/**
 * Adds a evu Id to the units which will have the user scheduled process applied to it. 
 * @param unitId
 */
  public void addUnitId(int unitId) {
    Integer newId = new Integer(unitId);

    if (units.contains(newId)) { return; }
    units.addElement(newId);
  }
/**
 * Gets all the evu Id's which will have the process applied to it.  
 * @return evu Id where user scheduled process will be applied.  
 */
  public Vector getUnitId() { return units; }

  /**
   * Removes the unitId from for this process application.
   */
  public void removeUnitId(Integer unitId) {
    units.removeElement(unitId);
  }
/**
 * Takes an Evu out of the user scheduled applications
 * @param unitId EVU ID
 */
  public void removeUnitId(int unitId) {
    removeUnitId(new Integer(unitId));
  }
/**
 * Sets the time steps.  
 * @param tStep time steps
 */
  public void setTimeStep(int tStep) { timeStep = tStep; }
  /**
   * Gets the time steps.  
   * @return
   */
  public int getTimeStep() { return timeStep; }
  /**
   * Checks if a time step is currently set for the application.  
   * @return true if not -1, which is the default flag variable for no time steps set (set in primary constructor).  
   */
  public boolean isTimeStepSet() { return (timeStep != -1); }

  public Process getProcess() { return process; }
  public ProcessType getProcessType() { return process.getType(); }
/**
 * Gets all the Evu's the user has selected for the process application.  
 * @return a vector (need vector because variable size) of Evu's for user selected processes.  
 */
  public Vector getUserChosenUnits() {
    Area    area = Simpplle.getCurrentArea();
    int     cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    Integer id;
    Evu     evu;
    Vector  chosenUnits;

    if (cStep != timeStep) { return null; }

    chosenUnits = new Vector();
    for(int i=0;i<units.size();i++) {
      id  = (Integer) units.elementAt(i);
      evu = area.getEvu(id);
      chosenUnits.addElement(evu);
    }
    return chosenUnits;
  }

  private void readGeneral(StringTokenizerPlus strTok) throws ParseError {
    StringTokenizerPlus subStrTok;
    String              str;
    int                 tStep, count, i;

    // Time Step
    tStep = strTok.getIntToken();
    if (tStep < 1 || tStep > Simulation.MAX_TIME_STEPS) {
      throw new ParseError("Time step must be between 1 and 50.");
    }
    setTimeStep(tStep);

    // Process
    str = strTok.getToken();
    process = Process.findInstance(ProcessType.get(str));
    if (process == null) {
      throw new ParseError(str + " is not a valid process.");
    }
  }
/**
 * Reads the Evu ID's where the process will be applied.  
 * @param strTok
 * @throws ParseError
 * @throws IOException
 */
  private void readUnitIds(StringTokenizerPlus strTok)
                                throws ParseError, IOException {
    String str;
    int    i, id, count;
    Area   currentArea = Simpplle.getCurrentArea();

    str    = strTok.getToken();
    if (str == null) { return; }  // i.e. a schedule with no units

    strTok = new StringTokenizerPlus(str,":");
    count  = strTok.countTokens();

    for(i=0;i<count;i++) {
      try {
        id = strTok.getIntToken();
      }
      catch (NumberFormatException nfe) {
        throw new ParseError("Unit id must be a number");
      }

      if (currentArea.isValidUnitId(id) == false) {
        throw new ParseError(id + " is not a valid unit id.");
      }
      addUnitId(id);
    }
  }

  public void read(BufferedReader fin) throws ParseError, IOException {
    StringTokenizerPlus strTok;
    String              line;

    // Get the General Information.
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid treatment Schedule file.");
    }

    strTok = new StringTokenizerPlus(line,",");
    readGeneral(strTok);

    // Get the list of unit id's (if any)
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Process Schedule file.");
    }
    strTok = new StringTokenizerPlus(line,",");
    readUnitIds(strTok);/*
     * 
     */
  }
/**
 * Method to save time steps and processes for user selected process.  Then all the Evu's where the process will be applied 
 * are saved separated by a ":".
 * @param fout
 */
  public void save(PrintWriter fout) {
    fout.println(getTimeStep() + "," + getProcessType().toString());

    if (units.size() > 0) {
      fout.print(units.elementAt(0));
      for(int i=1;i<units.size();i++) {
        fout.print(":" + units.elementAt(i));
      }
      fout.println();
    }
    else {
      fout.println("?");
    }
  }

}

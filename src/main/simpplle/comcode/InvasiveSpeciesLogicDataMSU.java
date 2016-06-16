package simpplle.comcode;

import java.io.Externalizable;
import java.util.ArrayList;
import java.util.Collections;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import simpplle.comcode.Climate.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Formatter;
import java.util.Locale;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods to handle Invasive Species Logic Data for MSU, a type of Logic Data
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *
 */
public class InvasiveSpeciesLogicDataMSU extends LogicData implements Externalizable {
  static final long serialVersionUID = 8502417277522760056L;
  static final int  version          = 1;

  protected ArrayList<Species> invasiveSpeciesList;
  protected String             invasiveSpeciesDesc;
  protected boolean            defaultInvasiveSpeciesDesc;
  protected Species            repSpecies;
  protected int                startValue;

  protected double intercept;
  protected double elevCoeff;
  protected double slopeCoeff;
  protected double cosaspCoeff;
  protected double sinaspCoeff;
  protected double annradCoeff;
  protected double distroadCoeff;
  protected double disttrailCoeff;
  protected double shrubCoeff;
  protected double grassCoeff;
  protected double treeCoeff;

  protected ArrayList<InvasiveSpeciesSimpplleTypeCoeffData> processCoeffData;
  protected String             processCoeffDataDesc;
  protected boolean            defaultProcessCoeffDataDesc;

  protected ArrayList<InvasiveSpeciesSimpplleTypeCoeffData> treatCoeffData;
  protected String             treatCoeffDataDesc;
  protected boolean            defaultTreatCoeffDataDesc;
/**
 * Constructor.  Initializes invasive species arraylist, invasive species description [], default invasive species description
 * process coefficient data arraylist, process coefficient data descritpion [], default process coeffdata dexciption, and system knowledge kind
 */
  public InvasiveSpeciesLogicDataMSU() {
    invasiveSpeciesList        = new ArrayList<Species>();
    invasiveSpeciesDesc        = "[]";
    defaultInvasiveSpeciesDesc = true;

    processCoeffData            = new ArrayList<InvasiveSpeciesSimpplleTypeCoeffData>();
    processCoeffDataDesc        = "[]";
    defaultProcessCoeffDataDesc = true;

    treatCoeffData            = new ArrayList<InvasiveSpeciesSimpplleTypeCoeffData>();
    treatCoeffDataDesc        = "[]";
    defaultTreatCoeffDataDesc = true;

    startValue   = 1;
    sysKnowKind     = SystemKnowledge.Kinds.INVASIVE_SPECIES_LOGIC_MSU;
  }
/**
 * Duplicates Invasive species logic data. Copies information from this class.  Much of this data pertains to coefficients (i.e. elevation)
 */
  public AbstractLogicData duplicate() {
    InvasiveSpeciesLogicDataMSU logicData = new InvasiveSpeciesLogicDataMSU();
    super.duplicate(logicData);

    logicData.invasiveSpeciesList        = new ArrayList<Species>(invasiveSpeciesList);
    logicData.invasiveSpeciesDesc        = this.invasiveSpeciesDesc;
    logicData.defaultInvasiveSpeciesDesc = this.defaultInvasiveSpeciesDesc;

    logicData.repSpecies = this.repSpecies;
    logicData.startValue = this.startValue;

    logicData.intercept      = this.intercept;
    logicData.elevCoeff      = this.elevCoeff;
    logicData.slopeCoeff     = this.slopeCoeff;
    logicData.cosaspCoeff    = this.cosaspCoeff;
    logicData.sinaspCoeff    = this.sinaspCoeff;
    logicData.annradCoeff    = this.annradCoeff;
    logicData.distroadCoeff  = this.distroadCoeff;
    logicData.disttrailCoeff = this.disttrailCoeff;
    logicData.shrubCoeff     = this.shrubCoeff;
    logicData.grassCoeff     = this.grassCoeff;
    logicData.treeCoeff      = this.treeCoeff;

    logicData.processCoeffData = new ArrayList<InvasiveSpeciesSimpplleTypeCoeffData>();
    for (InvasiveSpeciesSimpplleTypeCoeffData elem : processCoeffData) {
      logicData.processCoeffData.add(elem.duplicate());
    }
    logicData.processCoeffDataDesc        = this.processCoeffDataDesc;
    logicData.defaultProcessCoeffDataDesc = this.defaultProcessCoeffDataDesc;

    logicData.treatCoeffData = new ArrayList<InvasiveSpeciesSimpplleTypeCoeffData>();
    for (InvasiveSpeciesSimpplleTypeCoeffData elem : treatCoeffData) {
      logicData.treatCoeffData.add(elem.duplicate());
    }
    logicData.treatCoeffDataDesc        = this.treatCoeffDataDesc;
    logicData.defaultTreatCoeffDataDesc = this.defaultTreatCoeffDataDesc;

    return logicData;
  }
/**
 * Checks if parameter species is on the invasive species list.  
 * @param species to be evaluated
 * @return true if species is in the invasive species list
 */
  public boolean isMemberInvasiveSpecies(Species species) {
    return invasiveSpeciesList.contains(species);
  }
/**
 * Adds a species to the invasive species list  and makes the system knowledge changed.
 * @param species to be added
 */
  public void addInvasiveSpecies(Species species) {
    if (isMemberInvasiveSpecies(species) == false) {
      invasiveSpeciesList.add(species);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes a species from the invasive species list and marks system knowledge changed.
   * @param species to be removed
   */
  public void removeInvasiveSpecies(Species species) {
    if (invasiveSpeciesList.contains(species)) {
      invasiveSpeciesList.remove(species);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Returns a count representation by getting the size of the invasive species arraylist.
   * @return the size of invasive species arraylist.
   */
  public int getInvasiveSpeciesCount() { return invasiveSpeciesList.size(); }
/**
 * Gets the invasive species arraylist.  
 * @return invasive species arraylist for MSU
 */
  public ArrayList<Species> getInvasiveSpeciesList() { return invasiveSpeciesList; }
/**
 * checks if a process type is equals process coefficient data
 * @param process process type to be evaluated 
 * @return true if member of the process type
 */
  private boolean isMemberProcessType(ProcessType process) {
    if (process == null || processCoeffData == null) {
      return false;
    }

    for (int i=0; i<processCoeffData.size(); i++) {
      if (process.equals(processCoeffData.get(i).getSimpplleType())) {
        return true;
      }
    }
    return false;
  }
  /**
   * If process coefficient data for a particular process, initializes a new instance of invasive species simpplle type, sets its process, 
   * and adds to the data to it, then makes the system knowledge changed.
   * @param process
   */
  private void addProcessType(ProcessType process) {
    if (getProcessCoeffData(process) == null) {
      InvasiveSpeciesSimpplleTypeCoeffData data
        = new InvasiveSpeciesSimpplleTypeCoeffData();
      data.setSimpplleType(process);
      processCoeffData.add(data);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * chcecks to see if treatment type is a member of treatment coefficent data type
 * @param treat 
 * @return true if is a member of the treatment type
 */
  private boolean isMemberTreatmentType(TreatmentType treat) {
    if (treat == null || treatCoeffData == null) {
      return false;
    }

    for (int i=0; i<treatCoeffData.size(); i++) {
      if (treat.equals(treatCoeffData.get(i).getSimpplleType())) {
        return true;
      }
    }
    return false;
  }
  /**
   * adds a treatment type.  if null instantiates a new instance of Invasive species simpplle type coefficient data and adds the treatment type, else adds it to existing 
   * and marks system knowledge changed
   * @param treat the treatment type being added
   */
  private void addTreatmentType(TreatmentType treat) {
    if (getTreatmentCoeffData(treat) == null) {
      InvasiveSpeciesSimpplleTypeCoeffData data
        = new InvasiveSpeciesSimpplleTypeCoeffData();
      data.setSimpplleType(treat);
      treatCoeffData.add(data);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * checks if the passed simpplle type is a process type or treatment type
 * @param data
 * @return true if data is a treatment type of process type
 */
  public boolean isMemberSimpplleType(SimpplleType data) {
    if (data instanceof ProcessType) {
      return isMemberProcessType((ProcessType)data);
    }
    else if (data instanceof TreatmentType) {
      return isMemberTreatmentType((TreatmentType)data);
    }
    return false;
  }
  /**
   * adds simpplle type object to either process type of treatment type
   * @param data object (note cast to either process type or treatment type)
   */
  public void addSimpplleType(SimpplleType data) {
    if (data instanceof ProcessType) {
      addProcessType((ProcessType)data);
    }
    else if (data instanceof TreatmentType) {
      addTreatmentType((TreatmentType)data);
    }
  }
/**
 * removes coefficient data of specified process type and markes system knowledge changed
 * @param process the process type to be removed
 */
  private void removeProcessType(ProcessType process) {
    InvasiveSpeciesSimpplleTypeCoeffData data = getProcessCoeffData(process);
    if (data != null) {
      processCoeffData.remove(data);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes coefficient data of a  specified treatment type and makes system knowledge type changed.  
   * @param treat
   */
  private void removeTreatmentType(TreatmentType treat) {
    InvasiveSpeciesSimpplleTypeCoeffData data = getTreatmentCoeffData(treat);
    if (data != null) {
      treatCoeffData.remove(data);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Passes to removeProcessType of removeTreatmentType depending on type of simpplletype object passed
   * @param data
   */
  public void removeSimpplleType(SimpplleType data) {
    if (data instanceof ProcessType) {
      removeProcessType((ProcessType)data);
    }
    else if (data instanceof TreatmentType) {
      removeTreatmentType((TreatmentType)data);
    }
  }
/**
 * Gets coefficient data for either process type of treatment type depending on object type passed
 * @param sType the type of object either process type or treatment type
 * @return coeffient data
 */
  private InvasiveSpeciesSimpplleTypeCoeffData getCoeffDataStructure(SimpplleType sType)
  {
    InvasiveSpeciesSimpplleTypeCoeffData data;
    if (sType instanceof ProcessType) {
      return getProcessCoeffData((ProcessType)sType);
    }
    else if (sType instanceof TreatmentType) {
      return getTreatmentCoeffData((TreatmentType)sType);
    }
    return null;
  }
/**
 * 
 * @param sType 
 * @return coeffient data as a double
 */
  public double getCoeffData(SimpplleType sType) {
    InvasiveSpeciesSimpplleTypeCoeffData data = getCoeffDataStructure(sType);

    return (data != null ? data.getCoeff() : 0.0);
  }
  /**
   * Sets coefficient data and makes system knowledge changed.  This is called from the GUI table model.  
   * @param sType the data object 
   * @param coeff the coefficient data
   */
  public void setCoeffData(SimpplleType sType, double coeff) {
    InvasiveSpeciesSimpplleTypeCoeffData data = getCoeffDataStructure(sType);
    if (data == null) {
      addSimpplleType(sType);
      data = getCoeffDataStructure(sType);
    }

    if (data != null) {
      data.setCoeff(coeff);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * Gets the time step data if there is any, if not returns 1 for initial time staep.
 * @param sType
 * @return total time steps, unless null then returns 1 for initial time step
 */
  public int getTimeStepsData(SimpplleType sType) {
    InvasiveSpeciesSimpplleTypeCoeffData data = getCoeffDataStructure(sType);

    return (data != null ? data.getTimeSteps() : 1);
  }
  /**
   * Sets the data at a particular time step and marks the system knowledge changed. 
   * Note: if initial, time step not set - default = 1 meaning initial state.
   * @param sType
   * @param timeSteps
   */
  public void setTimeStepsData(SimpplleType sType, int timeSteps) {
    InvasiveSpeciesSimpplleTypeCoeffData data = getCoeffDataStructure(sType);
    if (data == null) {
      addSimpplleType(sType);
      data = getCoeffDataStructure(sType);
    }

    if (data != null) {
      data.setTimeSteps(timeSteps);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }

/**
 * Gets the invasive species simpplle type coefficient data according to a particular process type.  This is used within this class 
 * @param process the process to find coefficient data for.  
 * @return if there is process coefficient data returns it, otherwise returns null
 */
  private InvasiveSpeciesSimpplleTypeCoeffData getProcessCoeffData(ProcessType process) {
    for (int i=0; i<processCoeffData.size(); i++) {
      InvasiveSpeciesSimpplleTypeCoeffData data = processCoeffData.get(i);
      if (data.getSimpplleType() == process) {
        return data;
      }
    }
    return null;
  }
  /**
   *  This is set from GUI Logic Invasive Species Simpplle type chooser.  If the description entered is a PROCESS and is not null it will bring in the string description(trimmed) 
   *  of the process and use this to set the process coefficient data.  
   *  It then turns off processCoefficient Data default description, and marks system knowledge changed. 
   * @param desc
   */
  public void setProcessCoeffDataDescription(String desc) {
    if (desc == null || desc.length() == 0) {
      setProcessCoeffDataDescDefault();
      return;
    }
    processCoeffDataDesc = desc;
    defaultProcessCoeffDataDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Checks if static boolean for default process coefficient data description is true or false. 
   * @return True if static boolean for default process coefficient data description is true.
   */
  public boolean isDefaultProcessCoeffDataDescription() {
    return defaultProcessCoeffDataDesc;
  }
  /**
   * Gets the Process Coefficient data description
   * @return string representation of process coefficient data description
   */
  public String getProcessCoeffDataDescription() { return processCoeffDataDesc; }
  /**
   * Sets the default process coefficient data description to the process coefficient data if there is any.  
   * Sets it to empty array brackets, if none.  
   */
  public void setProcessCoeffDataDescDefault() {
    processCoeffDataDesc = (processCoeffData != null) ? processCoeffData.toString() : "[]";
    defaultProcessCoeffDataDesc = true;
  }
  /**
   * Gets the invasive species simpplle type coefficient data according to a particular treatment.  
   * @param treatment the treatment to find coefficient data for.  
   * @return if there is process coefficient data returns it, otherwise returns null
   */
  private InvasiveSpeciesSimpplleTypeCoeffData getTreatmentCoeffData(TreatmentType treatment) {
    for (int i=0; i<treatCoeffData.size(); i++) {
      InvasiveSpeciesSimpplleTypeCoeffData data = treatCoeffData.get(i);
      if (data.getSimpplleType() == treatment) {
        return data;
      }
    }
    return null;
  }
  /**
   *  This is set from GUI Logic Invasive Species Simpplle type chooser.  If the description entered is a TREATMENT and is not null it will bring in the string description(trimmed) 
   *  of the treatment and use this to set the treatment coefficient data, and turn off treatCoefficient Data default description, then mark system knowledge changed. 
   * @param desc
   */
  public void setTreatCoeffDataDescription(String desc) {
    if (desc == null || desc.length() == 0) {
      setTreatCoeffDataDescDefault();
      return;
    }
    treatCoeffDataDesc = desc;
    defaultTreatCoeffDataDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Checks if static boolean for default treatment coefficient data description is true or false. 
   * @return True if static boolean for default treatment coefficient data description is true.
   */
  public boolean isDefaultTreatCoeffDataDescription() {
    return defaultTreatCoeffDataDesc;
  }
  /**
   * Gets the Treatment Coefficient data description
   * @return string representation of process coefficient data description
   */
  public String getTreatCoeffDataDescription() { return treatCoeffDataDesc; }
/**
 * Sets the default treatment coefficient data description to the treatment coefficient data if there is any.  Sets it to empty array, if none.  
 */
  public void setTreatCoeffDataDescDefault() {
    treatCoeffDataDesc = (treatCoeffData != null) ? treatCoeffData.toString() : "[]";
    defaultTreatCoeffDataDesc = true;
  }
/**
 * Gets the representative species.
 * @return representative species
 */
  public Species getRepSpecies() {
    return repSpecies;
  }

/*
    protected ArrayList<Species> invasiveSpeciesList;
    protected String             invasiveSpeciesDesc;
    protected boolean            defaultInvasiveSpeciesDesc;
    protected Species            repSpecies;
    protected int                startValue;

    protected double intercept;
    protected double elevCoeff;
    protected double slopeCoeff;
    protected double cosaspCoeff;
    protected double sinaspCoeff;
    protected double annradCoeff;
    protected double distroadCoeff;
    protected double disttrailCoeff;
    protected double shrubCoeff;
    protected double grassCoeff;
    protected double treeCoeff;

    protected ArrayList<InvasiveSpeciesSimpplleTypeCoeffData> processCoeffData;
    protected String             processCoeffDataDesc;
    protected boolean            defaultProcessCoeffDataDesc;

    protected ArrayList<InvasiveSpeciesSimpplleTypeCoeffData> treatCoeffData;
    protected String             treatCoeffDataDesc;
    protected boolean            defaultTreatCoeffDataDesc;
*/
  public boolean isMatch(Evu evu) {
    if (super.isMatch(evu) == false) { return false; }

    return true;
  }
/**
 * Prints the probability in CSV form to a file indexed by -invasiveprob.txt.  It will include evu, current time step, simulation float probability
 * @param evu the existing vegetative unit that the probability is for.
 * @param prob the float representation of invasive species probability
 */
  private void printProbabilityData(Evu evu,int prob) {
    File outfile = Simulation.getInstance().getOutputFile();
    File path = new File(outfile+"-invasiveprob.txt");

    try {
      PrintWriter fout = new PrintWriter(new FileWriter(path,true));

      fout.print(evu);
      fout.print(",");
      fout.print(Simulation.getCurrentTimeStep());
      fout.print(",");
      fout.print(Simulation.getFloatProbability(prob));
      fout.flush();
      fout.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
  }
/**
 * method to calculate probability based on elevation, slope, aspect, rad aspect, cosine and sine aspect, anrad, shrub, grass, tree, process value, and treatment value
 * @param evu
 * @param distRoad distance to road
 * @param distTrail distance to trail
 * @return
 */
  public int calculateProbability(Evu evu, double distRoad, double distTrail) {
    ArrayList<ExistingLandUnit> landUnits = evu.getAssociatedLandUnits();
    if (landUnits == null || landUnits.size() == 0) { return 0; }

    ExistingLandUnit elu = landUnits.get(0);

    double prob=0.0;
    double elev = elu.getElevation();
    double slope = elu.getSlope();
    double aspect = elu.getAspect();
    double radAspect = Math.toRadians(aspect);
    double cosasp = Math.cos(radAspect);
    double sinasp = Math.sin(radAspect);
    double annrad = elu.getANNRAD();
    double shrub=0;
    double grass=0;
    double tree=0;
    double processValue=0.0;
    double treatValue=0.0;

    distRoad = distRoad * 0.3048;  // Convert feet to meters
    distTrail = distTrail * 0.3048; // Convert feet to meters

    int ts = Simulation.getCurrentTimeStep();

    for (int i=0; i<processCoeffData.size(); i++) {
      InvasiveSpeciesSimpplleTypeCoeffData data = processCoeffData.get(i);
      int timeStep = ts - data.getTimeSteps();
      if (timeStep < 0) { timeStep = 0; }

      ProcessType process = (ProcessType)data.getSimpplleType();

      if (evu.hasProcessAnyLifeform(timeStep,process)) {
        processValue = data.getCoeff();
        break;
      }
    }

    for (int i=0; i<treatCoeffData.size(); i++) {
      InvasiveSpeciesSimpplleTypeCoeffData data = treatCoeffData.get(i);
      int timeStep = ts - data.getTimeSteps();
      if (timeStep < 0) { timeStep = 0; }

      TreatmentType treatment = (TreatmentType)data.getSimpplleType();

      Treatment treat = evu.getTreatment(timeStep,true);
      if (treat != null && treat.getType() == treatment) {
        treatValue = data.getCoeff();
        break;
      }
    }

    VegSimStateData state;

    if (Simpplle.getCurrentArea().multipleLifeformsEnabled()) {
      state = evu.getState(ts, Lifeform.TREES);
      if (state != null) { tree = 1; }

      state = evu.getState(ts, Lifeform.SHRUBS);
      if (state != null) { shrub = 1; }

      state = evu.getState(ts, Lifeform.HERBACIOUS);
      if (state != null) { grass = 1; }
    }
    else {
      state = evu.getState(ts);
      Lifeform life = state.getVeg().getSpecies().getLifeform();
      tree = (life == Lifeform.TREES ? 1 : 0);
      shrub = (life == Lifeform.SHRUBS ? 1 : 0);
      grass = (life == Lifeform.HERBACIOUS ? 1 : 0);
    }

    double elevResult      = (elev * elevCoeff);
    double slopeResult     = (slope * slopeCoeff);
    double cosaspResult    = (cosasp * cosaspCoeff);
    double sinaspResult    = (sinasp * sinaspCoeff);
    double annradResult    = (annrad * annradCoeff);
    double distRoadResult  = (distRoad * distroadCoeff);
    double distTrailResult = (distTrail * disttrailCoeff);
    double shrubResult     = (shrub * shrubCoeff);
    double grassResult     = (grass * grassCoeff);
    double treeResult      = (tree * treeCoeff);

    double numerator = intercept + elevResult + slopeResult + cosaspResult +
                       sinaspResult + annradResult + distRoadResult +
                       distTrailResult + processValue + treatValue +
                       shrubResult + grassResult + treeResult;
    numerator = Math.exp(numerator);

    double denominator = numerator + 1;

    prob = numerator / denominator;
    prob *= 100;  // Calculated value is between 0-1 SIMPPLLE needs 0-100

    int ratProb = Simulation.getRationalProbability(prob);

    PrintWriter fout = Simulation.getInstance().getInvasiveSpeciesMSUPrintWriter();
    if (fout != null) {
      Formatter formatter = new Formatter(fout, Locale.US);

      formatter.format("%s,%d,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f%n",
                       repSpecies.toString(),evu.getId(), Simulation.getCurrentTimeStep(), intercept,
                       aspect, elev, elevResult, slope, slopeResult, cosasp, cosaspResult, sinasp,
                       sinaspResult, annrad, annradResult, distRoad, distRoadResult,
                       distTrail, distTrailResult, processValue, treatValue,
                       shrub, shrubResult, grass, grassResult, tree,
                       treeResult, prob);
    }

    return ratProb;
  }
  /**
   * method to sort invasive species, process coefficient, and treatment coefficient lists.  This uses the default Java Collections.sort method of ascending order.
   */
  public void sortLists() {
    super.sortLists();
    Collections.sort(invasiveSpeciesList);
    Collections.sort(processCoeffData);
    Collections.sort(treatCoeffData);
    setInvasiveSpeciesDescDefault();
    setProcessCoeffDataDescDefault();
    setTreatCoeffDataDescDefault();
  }

  public void setInvasiveSpeciesDescription(String desc) {
    if (desc == null || desc.length() == 0) {
      setInvasiveSpeciesDescDefault();
      return;
    }
    invasiveSpeciesDesc = desc;
    defaultInvasiveSpeciesDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  public boolean isDefaultInvasiveSpeciesDescription() {
    return defaultInvasiveSpeciesDesc;
  }
  public String getInvasiveSpeciesDescription() { return invasiveSpeciesDesc; }
/**
 * sets the devault invasive species descritpion
 */
  public void setInvasiveSpeciesDescDefault() {
    invasiveSpeciesDesc = (invasiveSpeciesList != null) ? invasiveSpeciesList.toString() : "[]";
    defaultInvasiveSpeciesDesc = true;
  }
/**
 * Gets the start value 
 * @return
 */
  public int getStartValue() {
    return startValue;
  }

  public void setRepSpecies(Species repSpecies) {
    this.repSpecies = repSpecies;
  }
/**
 * gets the value at a specified column.  those referred to in this class are 
 * invasive species, intercept, slope, cosine aspect, sine aspect, annrad, distance to road, distance to trail, process coefficient, treament coeffient, shrub, grass, tree coefficients and start value
 * 
 */
  public Object getValueAt(int col) {
    switch (col) {
      case InvasiveSpeciesLogicMSU.INVASIVE_SPECIES_COL: return invasiveSpeciesDesc;
      case InvasiveSpeciesLogicMSU.INTERCEPT_COL:        return intercept;
      case InvasiveSpeciesLogicMSU.ELEV_COL:             return elevCoeff;
      case InvasiveSpeciesLogicMSU.SLOPE_COL:            return slopeCoeff;
      case InvasiveSpeciesLogicMSU.C0SASP_COL:           return cosaspCoeff;
      case InvasiveSpeciesLogicMSU.SINASP_COL:           return sinaspCoeff;
      case InvasiveSpeciesLogicMSU.ANNRAD_COL:           return annradCoeff;
      case InvasiveSpeciesLogicMSU.DISTROAD_COL:         return distroadCoeff;
      case InvasiveSpeciesLogicMSU.DISTTRAIL_COL:        return disttrailCoeff;
      case InvasiveSpeciesLogicMSU.PROCESS_COEFF_COL:    return processCoeffDataDesc;
      case InvasiveSpeciesLogicMSU.TREATMENT_COEFF_COL:  return treatCoeffDataDesc;
      case InvasiveSpeciesLogicMSU.SHRUB_COL:            return shrubCoeff;
      case InvasiveSpeciesLogicMSU.GRASS_COL:            return grassCoeff;
      case InvasiveSpeciesLogicMSU.TREE_COL:             return treeCoeff;
      case InvasiveSpeciesLogicMSU.START_VALUE_COL:      return startValue;
      default:
        return super.getValueAt(col);
    }
  }
  public void setValueAt(int col, Object value) {
    switch (col) {
      case InvasiveSpeciesLogicMSU.INVASIVE_SPECIES_COL: break; // set in table editor;
      case InvasiveSpeciesLogicMSU.INTERCEPT_COL:
        intercept = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.ELEV_COL:
        elevCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.SLOPE_COL:
        slopeCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.C0SASP_COL:
        cosaspCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.SINASP_COL:
        sinaspCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.ANNRAD_COL:
        annradCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.DISTROAD_COL:
        distroadCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.DISTTRAIL_COL:
        disttrailCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.SHRUB_COL:
        shrubCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.GRASS_COL:
        grassCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.TREE_COL:
        treeCoeff = (Double)value;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case InvasiveSpeciesLogicMSU.START_VALUE_COL:
        if (startValue != (Integer)value) {
          startValue = (Integer)value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case InvasiveSpeciesLogicMSU.PROCESS_COEFF_COL:    break; // set in table editor;
      case InvasiveSpeciesLogicMSU.TREATMENT_COEFF_COL:  break; // set in table editor;
      default:
        super.setValueAt(col,value);
    }
  }

  /**
   *
 * writes objects according to following order: 
 * version, invasive species arraylist, invasive species descriptoin, default invasive species description, representative species,
 * start value, intercept, elevation, slope, cosine aspect, sine aspec, annrad, distance to road, distance to trail, 
 * shrup grass, and tree coefficients, plus pocess coefficient data arraylist, process coefficient data descripition, 
 * default process coefficient data, treatment coefficient data arraylist, treatment coefficient data description
 
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(version);

    out.writeObject(invasiveSpeciesList);
    out.writeObject(invasiveSpeciesDesc);
    out.writeBoolean(defaultInvasiveSpeciesDesc);

    out.writeObject(repSpecies);
    out.writeInt(startValue);

    out.writeDouble(intercept);
    out.writeDouble(elevCoeff);
    out.writeDouble(slopeCoeff);
    out.writeDouble(cosaspCoeff);
    out.writeDouble(sinaspCoeff);
    out.writeDouble(annradCoeff);
    out.writeDouble(distroadCoeff);
    out.writeDouble(disttrailCoeff);
    out.writeDouble(shrubCoeff);
    out.writeDouble(grassCoeff);
    out.writeDouble(treeCoeff);

    out.writeObject(processCoeffData);
    out.writeObject(processCoeffDataDesc);
    out.writeBoolean(defaultProcessCoeffDataDesc);

    out.writeObject(treatCoeffData);
    out.writeObject(treatCoeffDataDesc);
    out.writeBoolean(defaultTreatCoeffDataDesc);
  }
/**
 * reads in objects according to following order: 
 * version, invasive species arraylist, invasive species descriptoin, default invasive species description, representative species,
 * start value, intercept, elevation, slope, cosine aspect, sine aspec, annrad, distance to road, distance to trail, 
 * shrup grass, and tree coefficients, plus pocess coefficient data arraylist, process coefficient data descripition, 
 * default process coefficient data, treatment coefficient data arraylist, treatment coefficient data description
 */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(in);
    int version = in.readInt();

    invasiveSpeciesList = (ArrayList<Species>) in.readObject();
    invasiveSpeciesDesc        = (String)in.readObject();
    defaultInvasiveSpeciesDesc = in.readBoolean();

    repSpecies = (Species) in.readObject();
    startValue   = in.readInt();

    intercept = in.readDouble();
    elevCoeff = in.readDouble();
    slopeCoeff = in.readDouble();
    cosaspCoeff = in.readDouble();
    sinaspCoeff = in.readDouble();
    annradCoeff = in.readDouble();
    distroadCoeff = in.readDouble();
    disttrailCoeff = in.readDouble();
    shrubCoeff = in.readDouble();
    grassCoeff = in.readDouble();
    treeCoeff = in.readDouble();

    processCoeffData            = (ArrayList<InvasiveSpeciesSimpplleTypeCoeffData>)in.readObject();
    processCoeffDataDesc        = (String)in.readObject();
    defaultProcessCoeffDataDesc = in.readBoolean();

    treatCoeffData            = (ArrayList<InvasiveSpeciesSimpplleTypeCoeffData>)in.readObject();
    treatCoeffDataDesc        = (String)in.readObject();
    defaultTreatCoeffDataDesc = in.readBoolean();

  }

}

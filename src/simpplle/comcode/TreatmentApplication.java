package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
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
 * <p>This class defines methods for Treatment application.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public final class TreatmentApplication {
  private Vector units;

  //private int       id;
  private int           timeStep;
  private TreatmentType treatment;
  private int           acres;

  private boolean useAttributes;
  private boolean useUnits;
  private boolean useProcessProb;
  private boolean useRoadStatus;

  private boolean preventReTreatment;
  private int     preventReTreatmentSteps;
  private static int DEF_PREVENT_TIME_STEPS = 5;

  // Required but can be null.
  // Where null be all possible.
  private ArrayList<String> specialAreaList;
  private ArrayList<String> ownershipList;

  private int    roadStatus;
  private Vector htGroups;
  private Vector species;
  private Vector sizeClass;
  private Vector density;
  private Vector prevProcess;
  private Range aggregateAcres;
  //private int    maxArea;

  private boolean notHtGroups;
  private boolean notSpecies;
  private boolean notSizeClass;
  private boolean notDensity;
  private boolean notPrevProcess;

  private Hashtable processProb;
  private Vector    probProcess;

  // Optional
  private int                  waitSteps;
  private TreatmentType        nextTreatment;
  private TreatmentApplication nextApplication;
  private boolean              systemGenerated;

  private boolean useNextTreatment;

  private int treatedAcres;

  /**
   * The class constructor.
   */
  public TreatmentApplication() {
    timeStep       = -1;
    treatment      = null;
    acres = 0;
    if (Simpplle.getCurrentArea() != null) {
      acres = Simpplle.getCurrentArea().getAcres();
    }
    units          = new Vector();

    specialAreaList = new ArrayList<String>();
    ownershipList   = new ArrayList<String>();
    roadStatus     = simpplle.comcode.element.Roads.Status.UNKNOWN.getValue();

    preventReTreatment      = false;
    preventReTreatmentSteps = 5;

    // Vectors will be created later as needed.
    htGroups       = new Vector();
    species        = new Vector();
    sizeClass      = new Vector();
    density        = new Vector();
    prevProcess    = new Vector();
    aggregateAcres = new Range(0,1000000);
    //maxArea        = 99999999;

    notHtGroups = false;
    notSpecies  = false;
    notSizeClass = false;
    notDensity   = false;
    notPrevProcess = false;

    waitSteps        = 1;
    nextTreatment    = null;
    useNextTreatment = true;
    nextApplication  = null;
    systemGenerated  = false;

    processProb = new Hashtable();
    probProcess = new Vector();

    treatedAcres = 0;
  }

  public TreatmentApplication(TreatmentType treatment) {
    this();
    RegionalZone zone = Simpplle.getCurrentZone();

    this.treatment = treatment;
    nextTreatment  = Treatment.getFollowUpTreatment(zone,treatment);
    if (nextTreatment != null) {
      useNextTreatment = true;
      waitSteps     = 1;
    }
    else {
      useNextTreatment = false;
    }

    setUseAttributes(false);
    setUseUnits(false);
    setUseProcessProb(false);
    setUseRoadStatus(false);
  }

  public TreatmentApplication duplicate() {
    TreatmentApplication newApp;

    newApp = new TreatmentApplication(getTreatmentType());

    newApp.setAcres(getAcres());
    newApp.setRoadStatus(getRoadStatus());
    newApp.setWaitSteps(getWaitSteps());
    newApp.setUseAttributes(useAttributes());
    newApp.setUseUnits(useUnits());
    newApp.setUseProcessProb(useProcessProb());
    newApp.setUseRoadStatus(useRoadStatus());
    // will not copy time step, we want user to set new one.

    newApp.specialAreaList = new ArrayList<String>(specialAreaList);
    newApp.ownershipList   = new ArrayList<String>(ownershipList);
    newApp.htGroups    = (Vector) htGroups.clone();
    newApp.species     = (Vector) species.clone();
    newApp.sizeClass   = (Vector) sizeClass.clone();
    newApp.density     = (Vector) density.clone();
    newApp.prevProcess = (Vector) prevProcess.clone();
    newApp.units       = (Vector) units.clone();

    return newApp;
  }

  public boolean isNextTreatment() { return (nextTreatment != null); }

  public boolean useFollowUpTreatment() { return useNextTreatment; }
  public void    setUseFollowUpTreatment(boolean value) {
    useNextTreatment = value;
  }

  public boolean preventReTreatment() { return preventReTreatment; }
  public void setPreventReTreatment(boolean value) { preventReTreatment = value; }

  public int getPreventReTreatmentTimeSteps() { return preventReTreatmentSteps; }
  public void setPreventReTreatmentTimeSteps(int value) { preventReTreatmentSteps = value; }

  public boolean isSystemGenerated() { return systemGenerated; }
  public void makeSystemGenerated() { systemGenerated = true; }

  //public void setId(int newId) {
  //  id = newId;
  //}

  //public int getId() { return id; }

  public void setAcres(int newAcres) {
    acres = newAcres;
  }
  public void setAcres(float newAcres) {
    acres = Math.round(newAcres * simpplle.comcode.utility.Utility.pow(10,Area.getAcresPrecision()));
  }

  public int getAcres() { return acres; }
  public float getFloatAcres() {
    return ( (float)acres / (float) simpplle.comcode.utility.Utility.pow(10,Area.getAcresPrecision()) );
  }

  public boolean isAcresSet() { return (acres != -1); }

  public void setTimeStep(int newTime) {
    timeStep = newTime;
  }

  public int getTimeStep() { return timeStep; }

  public boolean isTimeStepSet() { return (timeStep != -1); }

  public TreatmentType getTreatmentType() {
    return treatment;
  }

  public TreatmentType getNextTreatment() {
    return nextTreatment;
  }
  public void setNextTreatment(TreatmentType treat) {
    nextTreatment = treat;
  }

  public ArrayList<String> getSpecialAreaList() {
    return specialAreaList;
  }
  public void addSpecialArea(String str) {
    if (!specialAreaList.contains(str)) {
      specialAreaList.add(str);
    }
  }
  public void clearSpecialArea() {
    specialAreaList.clear();
  }

  public ArrayList<String> getOwnershipList() {
    return ownershipList;
  }
  public void addOwnership(String str) {
    if (!ownershipList.contains(str)) {
      ownershipList.add(str);
    }
  }
  public void clearOwnership() {
    ownershipList.clear();
  }

  public boolean useRoadStatus() { return useRoadStatus; }
  public void setUseRoadStatus(boolean val) { useRoadStatus = val; }

  public void setRoadStatus(simpplle.comcode.element.Roads.Status status) {
    setRoadStatus(status.getValue());
  }

  public void setRoadStatus(int val) {
    roadStatus = val;
  }

  public int getRoadStatus() { return roadStatus; }

  public int getWaitSteps() { return waitSteps; }

  public void setWaitSteps(int val) {
    waitSteps = val;
  }

  public Vector getProcessProbList() {
    Vector  result = new Vector(probProcess.size());
    String  processName, str;
    Integer prob;

    for(int i=0;i<probProcess.size();i++) {
      prob = (Integer) processProb.get(probProcess.elementAt(i));
      str  = probProcess.elementAt(i) + " >= " + prob;
      result.addElement(str);
    }
    return result;
  }

  public Integer getProcessProb(String processName) {
    return (Integer)processProb.get(processName);
  }

  public void addProcessProb(String processName, Integer prob) {
    if (probProcess.contains(processName)) { return; }

    processProb.put(processName,prob);
    probProcess.addElement(processName);
  }

  public void insertProcessProb(String processName, Integer prob,
                                String insertItem)
  {
    if (probProcess.contains(processName)) { return; }

    int index = probProcess.indexOf(insertItem);
    if (index != -1) {
      processProb.put(processName,prob);
      probProcess.insertElementAt(processName,index);
    }
  }

  public void removeProcessProb(String processName) {
    processProb.remove(processName);
    probProcess.removeElement(processName);
  }

  public boolean useAttributes() { return useAttributes; }
  public void setUseAttributes(boolean val) { useAttributes = val; }

  public boolean useUnits() { return useUnits; }
  public void setUseUnits(boolean val) { useUnits = val; }

  public boolean useProcessProb() { return useProcessProb; }
  public void setUseProcessProb(boolean val) { useProcessProb = val; }

  public boolean isNotHtGroups() { return notHtGroups; }
  public void setNotHtGroups(boolean notHtGroups) { this.notHtGroups = notHtGroups; }

  public boolean isNotSpecies() { return notSpecies; }
  public void setNotSpecies(boolean notSpecies) { this.notSpecies = notSpecies; }

  public boolean isNotSizeClass() { return notSizeClass; }
  public void setNotSizeClass(boolean notSizeClass) { this.notSizeClass = notSizeClass; }

  public boolean isNotDensity() { return notDensity; }
  public void setNotDensity(boolean notDensity) { this.notDensity = notDensity; }

  public boolean isNotPrevProcess() { return notPrevProcess; }
  public void setNotPrevProcess(boolean notPrevProcess) { this.notPrevProcess = notPrevProcess; }

  public int getMinAggregateAcres() {
    return aggregateAcres.getLower();
  }
  public void setMinAggregateAcres(int minAggregateAcres) {
    aggregateAcres.setLower(minAggregateAcres);
  }

  public int getMaxAggregateAcres() {
    return aggregateAcres.getUpper();
  }
  public void setMaxAggregateAcres(int maxAggregateAcres) {
    aggregateAcres.setUpper(maxAggregateAcres);
  }

  public void removeNextApplication() {
    nextApplication = null;
  }

  private void scheduleFollowUpTreatments(Vector treatedUnits) {
    simpplle.comcode.element.Evu evu;

    if (nextTreatment != null) {
      nextApplication.setUseUnits(true);
      for(int i=0;i<treatedUnits.size();i++) {
        evu = (simpplle.comcode.element.Evu) treatedUnits.elementAt(i);
        nextApplication.addUnitId(evu.getId());
      }
    }
  }

  public Vector getUserChosenUnits() {
    Area       area = Simpplle.getCurrentArea();
    Simulation simulation = Simpplle.getCurrentSimulation();
    int        cStep      = simulation.getCurrentTimeStep();
    int        nSteps     = simulation.getNumTimeSteps();
    int        nextStep;
    simpplle.comcode.element.Evu evu;
    int        acres, tmpMaxAcres = getAcres();
    Vector     treatedUnits = new Vector();
    int        i;
    Integer    id;

    if (cStep != getTimeStep()) { return null; }

    // Initialize here because user sheduled units are always done first.
    treatedAcres = 0;
    if (nextTreatment != null && nextApplication == null) {
      nextStep = cStep + getWaitSteps();
      if (nextStep <= nSteps && nextApplication == null) {
        nextApplication = new TreatmentApplication(nextTreatment);
        nextApplication.makeSystemGenerated();
        nextApplication.nextTreatment = null;
        nextApplication.setTimeStep(nextStep);
        nextApplication.setAcres(getAcres());
        nextApplication.setWaitSteps(getWaitSteps());
        area.getTreatmentSchedule().addApplication(nextApplication);
      }
    }

    int    tmpRoadStatus  = getRoadStatus();
    String evuSpecialArea, evuOwnership;

    if (useUnits()) {
      for(i=0;i<units.size();i++) {
        id = (Integer) units.elementAt(i);
        evu   = area.getEvu(id);
        acres = evu.getAcres();

        evuSpecialArea = evu.getSpecialArea();
        if (specialAreaList.size() > 0 && !specialAreaList.contains(evuSpecialArea)) {
          continue;
        }
        evuOwnership = evu.getOwnership();
        if (ownershipList.size() > 0 && !ownershipList.contains(evuOwnership)) {
          continue;
        }
        if (useRoadStatus() && (evu.getRoadStatusNew(cStep).getValue() != tmpRoadStatus)) {
          continue;
        }

        //if ((treatedAcres + acres) > tmpMaxAcres) { continue; }
        treatedUnits.addElement(evu);
        treatedAcres += evu.getAcres();
      }
    }
    if (treatedUnits.size() == 0) {
      treatedUnits = null;
    }
    if (treatedUnits != null && nextTreatment != null &&
        nextApplication != null && useFollowUpTreatment()) {
      scheduleFollowUpTreatments(treatedUnits);
    }
    return treatedUnits;
  }

  public Vector getAttributeChosenUnits() {
    if (!useAttributes()) { return null; }

    int cStep = Simulation.getCurrentTimeStep();
    if (cStep != getTimeStep()) { return null; }

    RegionalZone zone = Simpplle.getCurrentZone();
    simpplle.comcode.element.Evu[]        allEvu = Simpplle.getCurrentArea().getAllEvu();
    simpplle.comcode.element.Evu evu;
    Vector       treatedUnits = new Vector();
    int          tmpMaxAcres = getAcres(), acres;

    ProcessType          process;
    HabitatTypeGroupType htGrp;
    String               specialArea;
    int                  roadStatus;
    Treatment            treat;
    int[]                indexes;

    Hashtable htGrpHt     = getSelectedAttributes(zone,RegionalZone.HTGRP);
    Hashtable speciesHt   = getSelectedAttributes(zone,RegionalZone.SPECIES);
    Hashtable sizeClassHt = getSelectedAttributes(zone,RegionalZone.SIZE_CLASS);
    Hashtable densityHt   = getSelectedAttributes(zone,RegionalZone.DENSITY);
    Hashtable processHt   = getSelectedAttributes(zone,RegionalZone.PROCESS);

    int       chosenRoadStatus  = getRoadStatus();

    indexes = simpplle.comcode.utility.Utility.makeRandomIndexSequence(allEvu.length-1);
    for (int i=0; i<allEvu.length; i++) {
      evu = allEvu[indexes[i]];
      if (evu == null) { continue; }

      // Make sure unit hasn't already been a treated.
      if (evu.getCurrentTreatment() != null) { continue; }

      // Only pick treatments that are feasible.
      if (Treatment.isFeasible(evu,this.treatment) == false) { continue; }

      // If a treatment was done on this unit in the previous n time steps
      // then make sure that that treatment allows follow up treatments.
      Vector v = evu.getTreatments();
      if (v != null) {
        boolean skipUnit=false;
        int     ns;
        for (int p=0; p<v.size(); p++) {
          treat = (Treatment)v.elementAt(p);
          if (treat.getStatus() == Treatment.INFEASIBLE ||
              treat.getStatus() == Treatment.NOT_APPLIED) { continue; }

          if (treat.preventReTreatment()) {
            ns = treat.getPreventReTreatmentTimeSteps();
            if ((timeStep - treat.getTimeStep()) <= ns) {
              skipUnit = true;
              break;
            }
          }
        }
        if (skipUnit) { continue; }
      }

       // Check the Acres
      acres = evu.getAcres();
//      if ((treatedAcres + acres) > tmpMaxAcres) { break; }

      VegSimStateData priorState = evu.getState(cStep-1);
      if (priorState == null) { continue; }

      process = priorState.getProcess();

      VegSimStateData state = evu.getState();
      if (state == null) { continue; }

      Species     species     = state.getVeg().getSpecies();
      SizeClass   sizeClass   = state.getVeg().getSizeClass();
      Density     density     = state.getVeg().getDensity();

      htGrp       = HabitatTypeGroupType.get(evu.getHabitatTypeGroup());
      specialArea = evu.getSpecialArea();
      String ownership = evu.getOwnership();
      roadStatus  = evu.getRoadStatusNew(cStep).getValue();

      boolean htGrpEmpty       = (htGrpHt.size() == 0);
      boolean speciesEmpty     = (speciesHt.size() == 0);
      boolean sizeClassEmpty   = (sizeClassHt.size() == 0);
      boolean densityEmpty     = (densityHt.size() == 0);
      boolean prevProcessEmpty = (processHt.size() == 0);


      boolean htGrpMatch     = (htGrpEmpty || htGrpHt.containsKey(htGrp));
      boolean speciesMatch   = (speciesEmpty || speciesHt.containsKey(species));
      boolean sizeClassMatch = (sizeClassEmpty || sizeClassHt.containsKey(sizeClass));
      boolean densityMatch   = (densityEmpty || densityHt.containsKey(density));
      boolean processMatch   = (prevProcessEmpty || processHt.containsKey(process));

      boolean specialAreaMatch = (specialAreaList.size() == 0 || specialAreaList.contains(specialArea));
      boolean ownershipMatch = (ownershipList.size() == 0 || ownershipList.contains(ownership));

      htGrpMatch     = (!htGrpEmpty && notHtGroups) ? !htGrpMatch : htGrpMatch;
      speciesMatch   = (!speciesEmpty && notSpecies) ? !speciesMatch : speciesMatch;
      sizeClassMatch = (!sizeClassEmpty && notSizeClass) ? !sizeClassMatch : sizeClassMatch;
      densityMatch   = (!densityEmpty && notDensity) ? !densityMatch : densityMatch;
      processMatch   = (!prevProcessEmpty && notPrevProcess) ? !processMatch : processMatch;

      if (htGrpMatch && speciesMatch && sizeClassMatch && densityMatch && processMatch &&
          specialAreaMatch && ownershipMatch &&
          ((!useRoadStatus()) || roadStatus == chosenRoadStatus))

      {
        treatedUnits.addElement(evu);
        treatedAcres += acres;
      }
    }

    determineFinalAttributeSet(treatedUnits);

    if (treatedUnits.size() == 0) {
      treatedUnits = null;
    }
    if (treatedUnits != null && nextTreatment != null &&
        nextApplication != null && useFollowUpTreatment()) {
      scheduleFollowUpTreatments(treatedUnits);
    }
    return treatedUnits;
  }

  private boolean isClusterMember(ArrayList<simpplle.comcode.element.Evu> cluster, simpplle.comcode.element.Evu unit) {
    if (cluster == null || cluster.size() == 0) {
      return false;
    }

    for (int i=0; i<cluster.size(); i++) {
      simpplle.comcode.element.Evu clusterEvu = cluster.get(i);

      if (unit.isNeighbor(clusterEvu)) {
        return true;
      }
    }
    return false;
  }

  private int getClusterAcresRational(ArrayList<simpplle.comcode.element.Evu> cluster) {
    int ratTotAcres=0;

    for (int i=0; i<cluster.size(); i++) {
      ratTotAcres += cluster.get(i).getAcres();
    }

    return ratTotAcres;
  }
  private int getClusterAcres(ArrayList<simpplle.comcode.element.Evu> cluster) {
    int rationalAcres = getClusterAcresRational(cluster);
    return Math.round(Area.getFloatAcres(rationalAcres));
  }

  /**
   * Find clusters of units that meet the minimum aggregate acres.
   * Once found remove random clusters of units from treated lists until
   * we meet the maximum treated acres requirement.
   * @param treatedUnits list of units to potentially treat.
   */
  private void determineFinalAttributeSet(Vector treatedUnits) {
    if (treatedUnits == null) {
      return;
    }

    ArrayList<ArrayList<simpplle.comcode.element.Evu>> unitClusters = new ArrayList<ArrayList<simpplle.comcode.element.Evu>>();

    if (aggregateAcres == null) {
      return;
    }

    for (int i=0; i<treatedUnits.size(); i++) {
      simpplle.comcode.element.Evu unit = (simpplle.comcode.element.Evu)treatedUnits.get(i);

      boolean isClusterMember = false;
      for (int uc=0; uc<unitClusters.size(); uc++) {
        ArrayList<simpplle.comcode.element.Evu> cluster = unitClusters.get(uc);

        if (isClusterMember(cluster,unit)) {
          cluster.add(unit);
          isClusterMember = true;
          break;
        }
      }

      if (!isClusterMember) {
        ArrayList<simpplle.comcode.element.Evu> cluster = new ArrayList<simpplle.comcode.element.Evu>();
        cluster.add(unit);
        unitClusters.add(cluster);
      }
    }

    ArrayList<ArrayList<simpplle.comcode.element.Evu>> finalUnitClusters = new ArrayList<ArrayList<simpplle.comcode.element.Evu>>();

    for (int uc=0; uc<unitClusters.size(); uc++) {
      ArrayList<simpplle.comcode.element.Evu> cluster = unitClusters.get(uc);

      int clusterAcres = getClusterAcres(cluster);

      if (!aggregateAcres.inRange(clusterAcres)) {
        for (int c=0; c<cluster.size(); c++) {
          simpplle.comcode.element.Evu clusterUnit = cluster.get(c);
          if (treatedUnits.contains(clusterUnit)) {
            treatedUnits.remove(clusterUnit);
            treatedAcres -= clusterUnit.getAcres();
          }
        }
      }
      else {
        finalUnitClusters.add(cluster);
      }
    }

    int tmpMaxAcres = getAcres();
    if (treatedAcres <= tmpMaxAcres) {
      return;
    }

    // Too Many acres, need to remove some clusters.

    // Randomize list of clusters first.
    Collections.shuffle(finalUnitClusters);

    int totClusterAcres = 0;
    for (int uc=0; uc<finalUnitClusters.size(); uc++) {
      ArrayList<simpplle.comcode.element.Evu> cluster = finalUnitClusters.get(uc);

      int clusterAcres = getClusterAcresRational(cluster);

      totClusterAcres += clusterAcres;

      if (totClusterAcres > tmpMaxAcres) {
        for (int c=0; c<cluster.size(); c++) {
          simpplle.comcode.element.Evu clusterUnit = cluster.get(c);
          if (treatedUnits.contains(clusterUnit)) {
            treatedUnits.remove(clusterUnit);
            treatedAcres -= clusterUnit.getAcres();
          }
        }
        // Now that they have been removed these acres no longer count.
        // while not necessary this will allow us to get as close as we can to the max acres.
        totClusterAcres -= clusterAcres;
      }
    }
  }

  public Vector getProcessProbChosenUnits() {
    Area              area = Simpplle.getCurrentArea();
    RegionalZone      zone = Simpplle.getCurrentZone();
    int               cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    simpplle.comcode.element.Evu[]             allEvu = area.getAllEvu();
    simpplle.comcode.element.Evu evu;
    String            processName;
    ProcessType       processType;
    Integer           prob;
    int               evuProb;
    int               id, i, j;
    int               tmpMaxAcres = getAcres(), acres;
    TreatmentSchedule schedule = area.getTreatmentSchedule();
    Vector            treatedUnits = new Vector();
    Treatment         treat;
    int[]             indexes;

    if (cStep != getTimeStep()) { return null; }

    if (useProcessProb()){
      for(i=0;i<probProcess.size();i++) {
        processName = (String)probProcess.elementAt(i);
        processType = ProcessType.get(processName);
        prob        = (Integer) processProb.get(processName);

        indexes = simpplle.comcode.utility.Utility.makeRandomIndexSequence(allEvu.length-1);
        for(j=0;j<allEvu.length;j++) {
          evu = allEvu[indexes[j]];
          if (evu == null) { continue; }

          // Don't treat an already treated unit.
          if (evu.getCurrentTreatment() != null) { continue; }

          // If a treatment was done on this unit in the previous n time steps
          // then make sure that that treatment allows follow up treatments.
          Vector v = allEvu[i].getTreatments();
          if (v != null) {
            boolean skipUnit=false;
            int     ns;
            for (int p=0; p<v.size(); p++) {
              treat = (Treatment)v.elementAt(p);
              if (treat.getStatus() == Treatment.INFEASIBLE ||
                  treat.getStatus() == Treatment.NOT_APPLIED) { continue; }

              if (treat.preventReTreatment()) {
                ns = treat.getPreventReTreatmentTimeSteps();
                if ((timeStep - treat.getTimeStep()) <= ns) {
                  skipUnit = true;
                  break;
                }
              }
            }
            if (skipUnit) { continue; }
          }

          // Check the Acres
          id    = evu.getId();
          acres = evu.getAcres();
          if ((treatedAcres + acres) > tmpMaxAcres) { continue; }

          evuProb = schedule.getProbData(id,processType);
          if (evuProb >= prob.intValue()) {
            treatedUnits.addElement(evu);
            treatedAcres += acres;
          }
        }
      }
    }
    if (treatedUnits.size() == 0) {
      treatedUnits = null;
    }
    if (treatedUnits != null && nextTreatment != null &&
        nextApplication != null && useFollowUpTreatment()) {
      scheduleFollowUpTreatments(treatedUnits);
    }
    return treatedUnits;
  }

  private Vector getSelectedHtGroups() {
    Vector v = getHtGroups();
    if (v.size() == 0) {
      v = Treatment.getValidHabitatTypeGroups(treatment);
    }
    return v;
  }
  private Vector getSelectedSpecies() {
    Vector v = getSpecies();
    if (v.size() == 0) {
      v = Treatment.getValidSpecies(treatment);
    }
    return v;
  }
  private Vector getSelectedSizeClass() {
    Vector v = getSizeClass();
    if (v.size() == 0) {
      v = Treatment.getValidSizeClass(treatment);
    }
    return v;
  }
  private Vector getSelectedDensity() {
    Vector v = getDensity();
    if (v.size() == 0) {
      v = Treatment.getValidDensity(treatment);
    }
    return v;
  }

  private Vector getSelectedProcess() {
    Vector v = getPrevProcess();
    if (v.size() == 0) { v = getValidProcesses(); }
    return v;
  }

  private Hashtable getSelectedAttributes(RegionalZone zone, int kind) {
    Vector v;
    switch (kind) {
      case RegionalZone.HTGRP:      v = getSelectedHtGroups();  break;
      case RegionalZone.SPECIES:    v = getSelectedSpecies();   break;
      case RegionalZone.SIZE_CLASS: v = getSelectedSizeClass(); break;
      case RegionalZone.DENSITY:    v = getSelectedDensity();   break;
      case RegionalZone.PROCESS:    v = getSelectedProcess();   break;
      default: return null;
    }

//    int[] ids = new int[v.size()];
    Hashtable ht = new Hashtable(v.size());
    for(int i=0; i<v.size(); i++) {
      ht.put((SimpplleType)v.elementAt(i),(SimpplleType)v.elementAt(i));
//      ids[i] = ((SimpplleType)v.elementAt(i)).getId();
    }
//    return ids;
    return  ht;
  }

  // --------------------------------------------------------------

  public Vector getValidHabitatTypeGroups() {
    return Treatment.getValidHabitatTypeGroups(treatment);
  }
  public Vector getValidSpecies() {
    return Treatment.getValidSpecies(treatment);
  }
  public Vector getValidSizeClass() {
    return Treatment.getValidSizeClass(treatment);
  }
  public Vector getValidDensity() {
    return Treatment.getValidDensity(treatment);
  }
  public Vector getValidProcesses() {
    ProcessType[] processes = Process.getSummaryProcesses();
    Vector        valid = new Vector(processes.length);

    for(int i=0;i<processes.length;i++) {
      valid.addElement(processes[i]);
    }
    return valid;
  }

  // ------------------------------
  public Vector getHtGroups() { return htGroups; }

  public void addHtGrp(HabitatTypeGroupType htGrp) {
    if (htGroups.contains(htGrp)) { return; }
    htGroups.addElement(htGrp);
  }

  public void insertHtGrp(HabitatTypeGroupType newItem, HabitatTypeGroupType insertItem) {
    if (htGroups.contains(newItem)) { return; }

    int index = htGroups.indexOf(insertItem);
    if (index != -1) {
      htGroups.insertElementAt(newItem,index);
    }
  }

  public void removeHtGrp(HabitatTypeGroupType htGrp) {
    htGroups.removeElement(htGrp);
  }
  // ------------------------------

  public Vector getSpecies() { return species; }

  public void addSpecies(Species newSpecies) {
    if (species.contains(newSpecies)) { return; }
    species.addElement(newSpecies);
  }
  public void insertSpecies(Species newItem, Species insertItem) {
    if (species.contains(newItem)) { return; }

    int index = species.indexOf(insertItem);
    if (index != -1) {
      species.insertElementAt(newItem,index);
    }
  }

  public void removeSpecies(Species item) {
    species.removeElement(item);
  }
  // ------------------------------

  public Vector getSizeClass() { return sizeClass; }

  public void addSizeClass(SizeClass newSizeClass) {
    if (sizeClass.contains(newSizeClass)) { return; }
    sizeClass.addElement(newSizeClass);
  }

  public void insertSizeClass(SizeClass newItem, SizeClass insertItem) {
    if (sizeClass.contains(newItem)) { return; }

    int index = sizeClass.indexOf(insertItem);
    if (index != -1) {
      sizeClass.insertElementAt(newItem,index);
    }
  }

  public void removeSizeClass(SizeClass size) {
    sizeClass.removeElement(size);
  }
  // ------------------------------

  public Vector getDensity() { return density; }

  public void addDensity(Density newDensity) {
    if (density.contains(newDensity)) { return; }
    density.addElement(newDensity);
  }

  public void insertDensity(Density newItem, Density insertItem) {
    if (density.contains(newItem)) { return; }

    int index = density.indexOf(insertItem);
    if (index != -1) {
      density.insertElementAt(newItem,index);
    }
  }

  public void removeDensity(Density item) {
    density.removeElement(item);
  }
  // ------------------------------

  public Vector getPrevProcess() { return prevProcess; }

  public void addPrevProcess(ProcessType newItem) {
    if (prevProcess.contains(newItem)) { return; }
    prevProcess.addElement(newItem);
  }

  public void insertPrevProcess(ProcessType newItem, ProcessType insertItem) {
    if (prevProcess.contains(newItem)) { return; }

    int index = prevProcess.indexOf(insertItem);
    if (index != -1) {
      prevProcess.insertElementAt(newItem,index);
    }
  }

  public void removePrevProcess(ProcessType process) {
    prevProcess.removeElement(process);
  }

  // Units List.
  public void addUnitId(int unitId) {
    Integer newId = new Integer(unitId);

    if (units.contains(newId)) { return; }
    units.addElement(newId);
  }

  public Vector getUnitId() { return units; }

  /**
   * Removes the unitId from for this treatment.
   */
  public void removeUnitId(Integer unitId) {
    units.removeElement(unitId);
  }

  public void removeUnitId(int unitId) {
    removeUnitId(new Integer(unitId));
  }

  // If the treatment was not successfully applied to
  // A unit then we do not want to apply the follow up either.
  public void removeFollowUpTreatmentUnitId(int unitId) {
    if (nextApplication != null) {
      nextApplication.removeUnitId(unitId);
    }
  }

  public void readGeneral(simpplle.comcode.utility.StringTokenizerPlus strTok, String line, int fileVersion) throws ParseError {
    simpplle.comcode.utility.StringTokenizerPlus subStrTok;
    String              str;
    int                 tStep, count, i;
    RegionalZone        zone = Simpplle.currentZone;
    Boolean             bool;

    // Time Step
    tStep = strTok.getIntToken();
    if (tStep < 1 || tStep > Simulation.MAX_TIME_STEPS) {
      throw new ParseError("Time step must be between 1 and 50.");
    }
    setTimeStep(tStep);

    // Treatment
    str = strTok.getToken();
    treatment = TreatmentType.get(str);
    if (treatment == null) {
      treatment = Treatment.addLegalTreatment(str.toUpperCase());
    }
    nextTreatment = Treatment.getFollowUpTreatment(zone,treatment);

    // Acres
    int tmpAcres = strTok.getIntToken();
    if (tmpAcres == -1) { tmpAcres = Simpplle.getCurrentArea().getAcres(); }
    setAcres(tmpAcres);

    // Special Area
    if (fileVersion < 3) {
      str = strTok.getToken();
      if (str != null) {
        addSpecialArea(str);
      }
    }
    else {
      readSpecialArea(strTok);
    }

    // Road Status
    str = strTok.getToken();
    if (str != null) {
      setUseRoadStatus(true);
      setRoadStatus(simpplle.comcode.element.Roads.Status.lookup(str).getValue());
    }

    // Wait Steps
    {
      int ws = strTok.getIntToken();
      if (ws == -1) { ws = 1; }
      setWaitSteps(ws);
    }

   // Should we use the follow up treatment, if any?
   setUseFollowUpTreatment(nextTreatment !=null);
   if (strTok.hasMoreTokens()) {
      str = strTok.getToken();
      if (str != null) {
        bool = Boolean.valueOf(str);
        if (bool != null) { setUseFollowUpTreatment(bool.booleanValue()); }
      }
    }

   // Should we use the prevent re-treatment?
   setPreventReTreatment(false);
   if (strTok.hasMoreTokens()) {
      str = strTok.getToken();
      if (str != null) {
        bool = Boolean.valueOf(str);
        if (bool != null) { setPreventReTreatment(bool.booleanValue()); }
      }
    }

   setPreventReTreatmentTimeSteps(DEF_PREVENT_TIME_STEPS);
   if (strTok.hasMoreTokens()) {
      int num = strTok.getIntToken();
      if (num == -1) { num = DEF_PREVENT_TIME_STEPS;}
      setPreventReTreatmentTimeSteps(num);
    }

    if (strTok.hasMoreTokens()) {
      str = strTok.getToken();
      if (str != null) {
        nextTreatment = TreatmentType.get(str,true);
      }
    }
    else if (nextTreatment == null) {
      setUseFollowUpTreatment(false);
    }

    if (strTok.hasMoreTokens()) {
      if (fileVersion < 3) {
        str = strTok.getToken();
        if (str != null) {
          addOwnership(str);
        }
      }
      else {
        readOwnership(strTok);
      }
    }
  }

  private void readSpecialArea(simpplle.comcode.utility.StringTokenizerPlus strTok) throws ParseError {
    String str = strTok.getToken();
    if (str != null) {
      simpplle.comcode.utility.StringTokenizerPlus subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
      int count = subStrTok.countTokens();

      for(int i=0;i<count;i++) {
        str = subStrTok.getToken();
        addSpecialArea(str);
      }
    }

  }
  private void readOwnership(simpplle.comcode.utility.StringTokenizerPlus strTok) throws ParseError {
    String str = strTok.getToken();
    if (str != null) {
      simpplle.comcode.utility.StringTokenizerPlus subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
      int count = subStrTok.countTokens();

      for(int i=0;i<count;i++) {
        str = subStrTok.getToken();
        addOwnership(str);
      }
    }
  }

  private void readUnitIds(simpplle.comcode.utility.StringTokenizerPlus strTok)
                                throws ParseError, IOException {
    String str;
    int    i, id, count;
    Area   currentArea = Simpplle.getCurrentArea();

    str    = strTok.getToken();
    if (str == null) {
      setUseUnits(true);
      return;
    }
    strTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
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
    setUseUnits(true);
  }

  private void readAttributes(simpplle.comcode.utility.StringTokenizerPlus strTok, int fileVersion)
                                  throws ParseError, IOException {
    simpplle.comcode.utility.StringTokenizerPlus subStrTok;
    String              str;
    int                 count, i;
    RegionalZone        zone = Simpplle.currentZone;

    // Habitat Type Group
    str = strTok.getToken();
    if (str != null) {
      subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
      count = subStrTok.countTokens();

      for(i=0;i<count;i++) {
        str = subStrTok.getToken();
        if (HabitatTypeGroup.findInstance(str) == null) {
          throw new ParseError(str + " is not a valid Ecological Grouping");
        }
        addHtGrp(HabitatTypeGroupType.get(str));
      }
    }

    // Species
    str = strTok.getToken();
    if (str != null) {
      subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
      count = subStrTok.countTokens();

      for(i=0;i<count;i++) {
        str = subStrTok.getToken();
        if (Species.get(str) == null) {
          throw new ParseError(str + " is not a valid Species");
        }
        addSpecies(Species.get(str));
      }
    }

    // Size Class
    str = strTok.getToken();
    if (str != null) {
      subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
      count = subStrTok.countTokens();

      for(i=0;i<count;i++) {
        str = subStrTok.getToken();
        if (SizeClass.get(str) == null) {
          throw new ParseError(str + " is not a valid Size Class");
        }
        addSizeClass(SizeClass.get(str));
      }
    }

    // Density
    str = strTok.getToken();
    if (str != null) {
      subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
      count = subStrTok.countTokens();

      for(i=0;i<count;i++) {
        str = subStrTok.getToken();
        if (Density.get(str) == null) {
          throw new ParseError(str + " is not a valid Density");
        }
        addDensity(Density.get(str));
      }
    }

    // Process
    str = strTok.getToken();
    if (str != null) {
      subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
      count = subStrTok.countTokens();

      for(i=0;i<count;i++) {
        str = subStrTok.getToken();
        if (ProcessType.get(str) == null) {
          throw new ParseError(str + " is not a valid Process");
        }
        addPrevProcess(ProcessType.get(str));
      }
    }

    // Aggregate Acres Range
    if (fileVersion >= 2) {
      str = strTok.getToken();
      if (str != null) {
        subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");
        count = subStrTok.countTokens();
        if (count != 2) {
          throw new ParseError(str + " is not a valid Aggregate Acres Range (format:  min:max");
        }

        int minAcres = subStrTok.getIntToken();
        int maxAcres = subStrTok.getIntToken();

        if (minAcres == -1 || maxAcres == -1) {
          throw new ParseError(str + " Either min or max acres is not a valid number");
        }

        aggregateAcres = new Range(minAcres,maxAcres);
      }

      // true/false values for not of list values
      notHtGroups    = false;
      notSpecies     = false;
      notSizeClass   = false;
      notDensity     = false;
      notPrevProcess = false;

      if (strTok.countTokens() == 5) {
        notHtGroups    = Boolean.parseBoolean(strTok.getStringToken());
        notSpecies     = Boolean.parseBoolean(strTok.getStringToken());
        notSizeClass   = Boolean.parseBoolean(strTok.getStringToken());
        notDensity     = Boolean.parseBoolean(strTok.getStringToken());
        notPrevProcess = Boolean.parseBoolean(strTok.getStringToken());
      }
    }

    setUseAttributes(true);
  }

  public void readProcessProb(simpplle.comcode.utility.StringTokenizerPlus strTok)
                                  throws ParseError, IOException {
    simpplle.comcode.utility.StringTokenizerPlus subStrTok;
    String              str;
    int                 prob, count;

    count = strTok.countTokens();
    for(int i=0;i<count;i++) {
      str = strTok.getToken();
      if (str == null && count == 1) {
        setUseProcessProb(true);
        return;
      }
      else if (str == null) {
        throw new ParseError("Invalid Process Probability Info");
      }
      subStrTok = new simpplle.comcode.utility.StringTokenizerPlus(str,":");

      str  = subStrTok.getToken();
      prob = subStrTok.getIntToken();
      addProcessProb(str,new Integer(prob));
    }
    setUseProcessProb(true);
  }

  public void read(BufferedReader fin, int fileVersion) throws ParseError, IOException {
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    String              line;

    // Get the General Information.
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid treatment Schedule file.");
    }

    strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
    readGeneral(strTok,line,fileVersion);

    // Get the Attributes (if any)
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid treatment Schedule file.");
    }

    strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
    setUseAttributes((line.equals("NIL") == false));
    if (useAttributes()) {
      readAttributes(strTok,fileVersion);
    }

    // Get the list of unit id's (if any)
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid treatment Schedule file.");
    }
    strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
    setUseUnits((line.equals("NIL") == false));
    if (useUnits()) {
      readUnitIds(strTok);
    }

    // Get the Process Probability information (if any)
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid treatment Schedule file.");
    }
    strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
    this.setUseProcessProb((line.equals("NIL") == false));
    if (useProcessProb()) {
      readProcessProb(strTok);
    }

  }

  public void save(PrintWriter fout) {
    int      i=0,j=0;
    Vector[] v = new Vector[5];
    String   status, str;
    String   processName;
    Integer  prob;


    fout.print(getTimeStep());
    fout.print("," + treatment.toString());
    fout.print("," + getAcres());

    // Special Area
    if (specialAreaList.size() > 0) {
      fout.print(",");
      for (int s=0; s<specialAreaList.size(); s++) {
        if (s > 0) { fout.print(":"); }

        fout.print(specialAreaList.get(s));
      }
    }
    else {
      fout.print(",?");
    }

    if (useRoadStatus()) {
      fout.print("," + simpplle.comcode.element.Roads.Status.lookup(getRoadStatus()).toString());
    }
    else {
      fout.print(",?");
    }

    fout.print("," + this.getWaitSteps());

    str = (useFollowUpTreatment()) ? "true" : "false";
    fout.print("," + str);

    str = (preventReTreatment()) ? "true" : "false";
    fout.print("," + str);

    fout.print(",");
    fout.print(getPreventReTreatmentTimeSteps());

    if (nextTreatment != null) {
      fout.print(",");
      fout.print(nextTreatment.toString());
    }

    // Ownership
    if (ownershipList.size() > 0) {
      fout.print(",");
      for (int o=0; o<ownershipList.size(); o++) {
        if (o > 0) { fout.print(":"); }

        fout.print(ownershipList.get(o));
      }
    }
    else {
      fout.print(",?");
    }

    fout.println();
    // End General Section


    if (useAttributes()) {
      v[0] = getHtGroups();
      v[1] = getSpecies();
      v[2] = getSizeClass();
      v[3] = getDensity();
      v[4] = getPrevProcess();

      for(i=0;i<v.length;i++) {
        if (i != 0) { fout.print(","); }
        if (v[i].size() == 0) {
          fout.print("?");
        }
        else {
          fout.print(v[i].elementAt(0));
        }
        for(j=1;j<v[i].size();j++) {
          fout.print(":" + v[i].elementAt(j));
        }
      }

      // Aggregate Acres
      if (aggregateAcres == null) {
        fout.print(",?");
      }
      else {
        fout.print("," + aggregateAcres.getLower() + ":" + aggregateAcres.getUpper());
      }

      // true/false for not of lists values
      fout.print(",");
      fout.print(notHtGroups);
      fout.print(",");
      fout.print(notSpecies);
      fout.print(",");
      fout.print(notSizeClass);
      fout.print(",");
      fout.print(notDensity);
      fout.print(",");
      fout.print(notPrevProcess);

      fout.println();
    }
    else {
      fout.println("NIL");
    }

    if (useUnits()) {
      // Now print a line with specific unit id's (if any)
      Vector idv = getUnitId();
      if (idv.size() > 0) {
        fout.print(idv.elementAt(0));
        for(i=1;i<idv.size();i++) {
          fout.print(":" + idv.elementAt(i));
        }
        fout.println();
      }
      else {
        fout.println("?");
      }
    }
    else {
      fout.println("NIL");
    }

    if (useProcessProb()) {
      for(i=0;i<probProcess.size();i++) {
        if (i != 0) { fout.print(","); }
        processName = (String) probProcess.elementAt(i);
        prob        = (Integer) processProb.get(processName);

        fout.print(processName + ":" + prob);
      }
      if (probProcess.size() == 0) { fout.print("?"); }
      fout.println();
    }
    else {
      fout.println("NIL");
    }
  }

  public void printAll() {
  /*
    Enumeration e = getApplicationUnits();
    Integer     id;

    while (e.hasMoreElements()) {
      id = (Integer) e.nextElement();
      System.out.println("id = " + id);
      System.out.println("Treatment = " + application.get(id));
    }
    */
  }

  public String getDescription(int roadStatusLength) {
    StringBuffer buf = new StringBuffer();
    String       str;

    buf.append(simpplle.comcode.utility.Formatting.fixedField(getTimeStep(),4,true));
    buf.append(" ");

    buf.append(simpplle.comcode.utility.Formatting.fixedField(getTreatmentType().toString(),42,true));
    buf.append(" ");

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(0);

    str = nf.format(getFloatAcres());
    buf.append(simpplle.comcode.utility.Formatting.fixedField(str,10,true));
    buf.append(" ");

//    str = (useSpecialArea) ? getSpecialArea() : "n/a";
//    buf.append(Formatting.fixedField(str,specialAreaLength+1,true));
//    buf.append(" ");
//
//    str = (useOwnership) ? getOwnership() : "n/a";
//    buf.append(Formatting.fixedField(str,ownershipLength+1,true));
//    buf.append(" ");

    str = (useRoadStatus) ? simpplle.comcode.element.Roads.Status.lookup(this.getRoadStatus()).toString() : "n/a";
    buf.append(simpplle.comcode.utility.Formatting.fixedField(str,roadStatusLength+1,true));

    return buf.toString();
  }
}




/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

import org.apache.commons.collections.map.MultiKeyMap;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.collections.keyvalue.*;

/**
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class AreaSummary implements Externalizable {
  static final long serialVersionUID = -1016876609810906198L;
  static final int  version          = 6;

  /*
   First dimension is time step, second is evu id.
   Time step dimension:       n=currentStep, n-1=currentStep-1, etc)
   Size time step dimension:  Simulation.pastTimeStepsInMemory + 1;

   Type is either ProcessOccurrence or array of ProcessOccurrence
   This is because fire can overtake other processes so on some occasions
   you can have units with 2 process events.
    */
  private Object[][]   processEvents;
//  private ArrayList[]  spreadProcesses;
//  private ArrayList[]  fireSpreadProcesses;

  private HashMap spreadFromHt;  // Temporary used for created spread files.

  private int[] eventAcres;

  private int[][][] fireEventSummaryData;

 /*
  private class ProcessEvent {
    public ProcessType process;
    public boolean     isOrigin;
    public Object      data;

    public ProcessEvent(ProcessType process, boolean isOrigin, Object data) {
      this.process  = process;
      this.isOrigin = isOrigin;
      this.data     = data;
    }
  }
  private class SpreadEventData {
    public ProcessType process;
    public Evu         fromEvu;
    public Evu         toEvu;

    public SpreadEventData(ProcessType process, Evu fromEvu, Evu toEvu) {
      this.process = process;
      this.fromEvu = fromEvu;
      this.toEvu   = toEvu;
    }
  }
  private class SpreadEvent {
    public int        acres;
    public LinkedList eventList; // instances of SpreadEventData

    public SpreadEvent(int acres, LinkedList eventList) {
      this.acres     = acres;
      this.eventList = eventList;
    }
  }
  */
  /*
   * First dimension is timestep, second is evu id.
   * Each element of the LinkedList is instance of ProcessEvent
   * The data field of ProcessEvent is either an instance of SpreadEventData
   * or an instance of SpreadEvent
   */
//  private LinkedList[][] processEvents;

  // For use in writing gis spread files.
  public static final String ORIGINATED_IN_STR = "ORIGINATED-IN";
  public static final String SPREAD_FROM_STR   = "SPREAD-FROM";

  private Vector[] suppressedFires;

  // The keys to this hashtable will be fmz's.
  private HashMap fireSuppressionCost;

  // The keys to this hashtable will be special area.
  private HashMap fireSuppressionCostSA;

  private double[] fireEmissions;
  private double[] treatmentEmissions;

  // Keys (1-4): evu, lifeform, run, timestep
  private static MultiKeyMap gappedProcesses = new MultiKeyMap();
  private static MultiKeyMap DProcesses = new MultiKeyMap();

  // Used in saving simulations
  private static final String COMMA         = ",";
  private static final String COLON         = ":";
  private static final String SEMICOLON     = ";";
  private static final String QUESTION_MARK = "?";
  private static final String NIL_STR       = "NIL";
  private static final String SRF_STAR_STR  = "SRF*";
  private static final String SRF_STR       = "SRF";
  private static final String MSF_STAR_STR  = "MSF*";
  private static final String MSF_STR       = "MSF";
  private static final String LSF_STAR_STR  = "LSF*";
  private static final String LSF_STR       = "LSF";

  public AreaSummary() {
    this(Simpplle.getCurrentSimulation());
  }

  public AreaSummary(Simulation currentSimulation) {
    if (currentSimulation == null) { return; }

    gappedProcesses.clear();
    DProcesses.clear();

    int          tSteps, j;
    RegionalZone zone = Simpplle.getCurrentZone();
    Area         area = Simpplle.getCurrentArea();
    boolean      suppression;
    Fmz[]        allFmz;
    String       fmzName;

    suppression = currentSimulation.fireSuppression();

    // Area summary will not be created unless a simulation exists,
    // so this is safe.
    tSteps = currentSimulation.getNumTimeSteps();
    allFmz = zone.getAllFmz();

    fireEventSummaryData = new int[tSteps+1][Fmz.getNumClasses()][2];
    for (int ts = 0; ts<fireEventSummaryData.length; ts++) {
      for (int i = 0; i < Fmz.getNumClasses(); i++) {
        fireEventSummaryData[ts][i][0] = 0;
        fireEventSummaryData[ts][i][1] = 0;
      }
    }

    eventAcres = new int[tSteps+1];
    eventAcres[0] = 0;
    suppressedFires = new Vector[tSteps];
    if (suppression) {
      fireSuppressionCost = new HashMap();
    }

    if (Simulation.getInstance().isDiscardData()) {
      int pastInMem = Simulation.getInstance().getPastTimeStepsInMemory();
      processEvents = new Object[pastInMem+1][area.getMaxEvuId()+1];
    }
    else {
      processEvents = new Object[tSteps+1][area.getMaxEvuId()+1];
    }


    fireEmissions      = new double[tSteps];
    treatmentEmissions = new double[tSteps];
    for(int i=0;i<tSteps;i++) {
      fireEmissions[i]      = 0.0;
      treatmentEmissions[i] = 0.0;
    }

    for(int i=0;i<tSteps;i++) { suppressedFires[i] = new Vector(); }

    if (suppression) {
      long[] costByFmz;
      for(int i=0;i<allFmz.length;i++) {
        fmzName   = allFmz[i].getName();
        costByFmz = new long[tSteps];
        for(j=0;j<tSteps;j++) {
          costByFmz[j] = 0;
        }
        fireSuppressionCost.put(fmzName,costByFmz);
      }
    }

    if (currentSimulation.trackSpecialArea() && suppression) {
      fireSuppressionCostSA = new HashMap();
    }

    initSummaryData();
  }

  /**
   * If we are writing data to the database we are also only keeping a
   * limited number of times steps in memory.  This method will shift
   * process events down one index to make room for the current time step data.
   * called by simpplle.comcode.Simulation.doFuture()
   */
  public void doBeginTimeStepInitialize() {
    int cStep = Simulation.getCurrentTimeStep();

    if (Simulation.getInstance().isDiscardData() &&
        cStep > processEvents.length) {
      for (int i=0; i<processEvents.length-1; i++) {
        processEvents[i] = processEvents[i+1];
      }

      for (int j=0; j<processEvents[processEvents.length-1].length; j++) {
        processEvents[processEvents.length-1][j] = null;
      }
    }
  }

  /**
   *
   * Gets the current simulation instance and its current time step, subtracts the parameter teme step from current time step and then subtracts that from the process events length to get the index of data.
   * @param timeStep
   * @return
   */
  private int getDataIndex(int timeStep) {
    Simulation simulation = Simulation.getInstance();
    if (simulation == null ||
        simulation.isDiscardData() == false) {
      return timeStep;
    }

    int cStep = simulation.getCurrentTimeStep();
    if (cStep <= processEvents.length) {
      return timeStep - 1;
    }

    int diff  = cStep - timeStep;

    return ((processEvents.length - 1) - diff);
  }

  private int getDataCount(int timeStep) {
    int index = getDataIndex(timeStep);
    if (index < 0) { return 0; }

    return processEvents[index].length;
  }

  /**
   * Gets a single process occurrence or an array of process occurrences. Process occurrences are
   * recorded for each evu at each time step.
   *
   * @param timeStep a time step index
   * @param evuId a unique EVU number
   * @return ProcessOccurrence, ProcessOccurrence[], or null
   */
  private Object getProcessEvents(int timeStep, int evuId) {
    int index = getDataIndex(timeStep);
    if (index > -1) {
      return processEvents[index][evuId];
    } else {
      return null;
    }
  }

  /**
   * Records a single process occurrence of an array of process occurrences.
   *
   * @param timeStep a time step index
   * @param evuId a unique EVU number
   * @param events a ProcessOccurrence or ProcessOccurrence[]
   */
  private void setProcessEvents(int timeStep, int evuId, Object events) {
    int index = getDataIndex(timeStep);
    processEvents[index][evuId] = events;
  }

  public void updateSuppressedFires(Evu evu) {
    int cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep() - 1;

    suppressedFires[cStep].addElement(evu);
  }


  private void initSpreadEvents(Object eventData, ArrayList events) {
    ProcessOccurrence event = null;

    if (eventData instanceof ProcessOccurrenceSpreading ||
        eventData instanceof ProcessOccurrenceSpreadingFire) {
      events.add((ProcessOccurrenceSpreading)eventData);
    }
    else if (eventData instanceof ArrayList) {
      ArrayList list = (ArrayList)eventData;
      Object elem;
      for (int i = list.size() - 1; i >= 0; i--) {
        elem = list.get(i);
        if (elem instanceof ProcessOccurrenceSpreadingFire) {
          event = (ProcessOccurrenceSpreading)elem;
          if (event.getLifeform() == event.getUnit().getDominantLifeformFire()) {
            events.add( (ProcessOccurrenceSpreading)event);
          }
        }
        else if (elem instanceof ProcessOccurrenceSpreading) {
          events.add( (ProcessOccurrenceSpreading)elem);
        }
      }
      event = null;
    }
  }

  private ProcessOccurrenceSpreading isSpreadEvent(Object eventData, boolean searchList) {
    ProcessOccurrence event = null;
    Simulation simulation = Simpplle.getCurrentSimulation();
    boolean simulationRunning = simulation.isSimulationRunning();
    Season currentSeason = simulation.getCurrentSeason();

    if (eventData instanceof ProcessOccurrenceSpreading ||
        eventData instanceof ProcessOccurrenceSpreadingFire) {
      event = (ProcessOccurrenceSpreading)eventData;
    }
    else if (eventData instanceof ArrayList) {
      ArrayList list = (ArrayList)eventData;
      if (!searchList) {
        Object elem = list.get(list.size() - 1);
        if (elem instanceof ProcessOccurrenceSpreading) {
          if (simulationRunning &&
              ((ProcessOccurrenceSpreading)elem).getSeason() != currentSeason) {
            return null;
          }
          return (ProcessOccurrenceSpreading) elem;
        }
      }
      else {
        if (Area.multipleLifeformsEnabled()) {
          return findMostDominantEvent(list);
        }
        Object elem;
        for (int i = list.size() - 1; i >= 0; i--) {
          elem = list.get(i);
          if (elem instanceof ProcessOccurrenceSpreading) {
            return (ProcessOccurrenceSpreading) elem;
          }
        }
      }
      event = null;
    }

    return (ProcessOccurrenceSpreading)event;
  }
  private ProcessOccurrenceSpreadingFire isFireEvent(Object eventData, boolean searchList) {
    ProcessOccurrenceSpreading event=isSpreadEvent(eventData,searchList);
    return (event instanceof ProcessOccurrenceSpreadingFire) ?
        (ProcessOccurrenceSpreadingFire)event : null;
  }

  private ProcessOccurrenceSpreading findMostDominantEvent(ArrayList list) {
    Lifeform maxLife = Lifeform.NA;
    ProcessOccurrenceSpreading result=null;

    for (int i=list.size()-1; i>=0; i--) {
      Object elem = list.get(i);
      if (elem instanceof ProcessOccurrenceSpreading) {
        ProcessOccurrenceSpreading event = (ProcessOccurrenceSpreading)elem;
        maxLife = Lifeform.getMostDominant(event.getLifeform(),maxLife);
        result = event;
      }

    }
    return result;
  }

  public void doSuppressionCosts() {
    int numSteps = Simulation.getInstance().getNumTimeSteps();

    for (int i=1; i<=numSteps; i++) {
      doSuppressionCosts(i);
    }
    doSuppressionCostsFinal();
  }
  public void doSuppressionCosts(int timeStep) {
    doSuppressionCostsFMZ(timeStep);
    if (Simulation.getInstance().trackSpecialArea()) {
      doSuppressionCostsSA(timeStep);
    }
  }
  public void doSuppressionCostsFinal() {
    doSuppressionCostsFMZFinal();
    if (Simulation.getInstance().trackSpecialArea()) {
      doSuppressionCostsSAFinal();
    }
  }
  private void doSuppressionCostsSA(int ts) {
    int    eventAcres;
    Fmz    fmz;
    String sa;
    long   cost;
    long[] costBySA;
    int    numSteps = Simulation.getInstance().getNumTimeSteps();
    Evu    unit;

    ProcessOccurrenceSpreadingFire fireEvent=null;
    ProcessOccurrence[]            fireEventOccurrences;

    int size = getDataCount(ts);
    for (int f = 0; f < size; f++) {
      fireEvent = isFireEvent(getProcessEvents(ts,f), true);
      if (fireEvent == null || !fireEvent.isOriginUnit(f)) {
        continue;
      }

      fireEventOccurrences = fireEvent.getProcessOccurrences();
      eventAcres = 0;

      for (int j = 0; j < fireEventOccurrences.length; j++) {
        unit = fireEventOccurrences[j].getUnit();
        eventAcres += unit.getAcres();
        fmz = unit.getFmz();
        sa = unit.getSpecialArea();
        cost = (long) unit.getAcres() *
               (long) fmz.getRationalSuppressionCost(eventAcres);
        costBySA = (long[]) fireSuppressionCostSA.get(sa);
        if (costBySA == null) {
          costBySA = new long[numSteps];
          for (int k = 0; k < costBySA.length; k++) {
            costBySA[k] = 0;
          }
          fireSuppressionCostSA.put(sa, costBySA);
        }
        costBySA[ts - 1] += cost;
      }
    }

    for (int j = 0; j < suppressedFires[ts - 1].size(); j++) {
      unit = (Evu) suppressedFires[ts - 1].elementAt(j);
      fmz = unit.getFmz();
      sa = unit.getSpecialArea();
      // Fires suppressed at class A are always set to 0.1 acres.
      cost = (long) Area.getRationalAcres(0.1f) *
             (long) fmz.getRationalSuppressionCost(Area.getRationalAcres(0.1f));
      costBySA = (long[]) fireSuppressionCostSA.get(sa);
      if (costBySA == null) {
        costBySA = new long[numSteps];
        for (int k = 0; k < costBySA.length; k++) {
          costBySA[k] = 0;
        }
        fireSuppressionCostSA.put(sa, costBySA);
      }
      costBySA[ts - 1] += cost;
    }

  }
  private void doSuppressionCostsSAFinal() {
    String sa;
    long[] costBySA;
    int    numSteps = Simulation.getInstance().getNumTimeSteps();

    double[]     costBySAf;
    int         divisor;

    for (Object elem : fireSuppressionCostSA.keySet()) {
      sa        = (String)elem;
      costBySA  = (long[])fireSuppressionCostSA.get(sa);
      costBySAf = new double[numSteps];
      if (costBySA == null) {
        costBySA = new long[numSteps];
        for (int k=0; k<costBySA.length; k++) { costBySA[k] = 0; }
      }
      for (int k=0; k<costBySA.length; k++) {
        // determine divisor necessary to convert back to float.
        divisor  = Area.getRationalAcres(1.0f);
        divisor *= Fmz.floatToRationalCost(1.0f);
        costBySAf[k] = Fmz.rationalToFloatCost(costBySA[k],(double)divisor);
      }
      fireSuppressionCostSA.put(sa,costBySAf);
    }
  }

  private void doSuppressionCostsFMZ(int ts) {
    Evu    unit;
    int    eventAcres;
    Fmz    fmz;
    long   cost;
    long[] costByFmz;
    int    numSteps = Simulation.getInstance().getNumTimeSteps();

    ProcessOccurrenceSpreadingFire fireEvent;
    ProcessOccurrence[]            fireEventOccurrences;

    int size = getDataCount(ts);
    for (int f = 0; f < size; f++) {
      fireEvent = isFireEvent(getProcessEvents(ts,f), true);
      if (fireEvent == null || !fireEvent.isOriginUnit(f)) {
        continue;
      }

      fireEventOccurrences = fireEvent.getProcessOccurrences();
      eventAcres = 0;

      for (int j = 0; j < fireEventOccurrences.length; j++) {
        unit = fireEventOccurrences[j].getUnit();
        eventAcres += unit.getAcres();
        fmz = unit.getFmz();
        cost = (long) unit.getAcres() *
               (long) fmz.getRationalSuppressionCost(eventAcres);

        costByFmz = (long[]) fireSuppressionCost.get(fmz.getName());       
        costByFmz[ts - 1] += cost;
      }
    }

    for (int j = 0; j < suppressedFires[ts - 1].size(); j++) {
      unit = (Evu) suppressedFires[ts - 1].elementAt(j);
      fmz = unit.getFmz();
      // Fires suppressed at class A are always set to 0.1 acres.
      cost = (long) Area.getRationalAcres(0.1f) *
             (long) fmz.getRationalSuppressionCost(Area.getRationalAcres(0.1f));
      costByFmz = (long[]) fireSuppressionCost.get(fmz.getName());
      costByFmz[ts - 1] += cost;
    }
  }
  private void doSuppressionCostsFMZFinal() {
    long[] costByFmz;
    int    numSteps = Simulation.getInstance().getNumTimeSteps();

    double[]    costByFmzf;
    int         divisor;
    String      fmzName;
    Fmz[]       allFmz = Simpplle.getCurrentZone().getAllFmz();
    for (int i=0; i<allFmz.length; i++) {
      fmzName    = allFmz[i].getName();
      costByFmz  = (long[])fireSuppressionCost.get(fmzName);
      costByFmzf = new double[numSteps];
      for (int k=0; k<costByFmz.length; k++) {
        // determine divisor necessary to convert back to float.
        divisor  = Area.getRationalAcres(1.0f);
        divisor *= Fmz.floatToRationalCost(1.0f);
        costByFmzf[k] = Fmz.rationalToFloatCost(costByFmz[k],(double)divisor);
      }
      fireSuppressionCost.put(fmzName,costByFmzf);
    }
  }

  // If we get a discount of 1.0 then just return the cost Hashtable.
  // Otherwise discount the costs and place the result in a new Hashtable.
  // Return the new Hashtable.
  // This way different discount rates can be applied to the original
  // costs without having to recompute the costs.
  public HashMap getFireSuppressionCostSA(float discount) {
    return getFireSuppressionCostSA(discount,Simpplle.getCurrentSimulation());
  }
  public HashMap getFireSuppressionCostSA(float discount, Simulation simulation) {
    // if discount is not greater than 1.0 than don't discount.
    // Written this way because one should never test for equality of floats.
    if (((discount < 1.0f) || (discount > 1.0f)) == false) { return fireSuppressionCostSA; }

    int         nSteps = simulation.getNumTimeSteps();
    HashMap   fscHt = new HashMap();
    double[]     fsc;
    double[]     costBySA;
    String      key;
    float       raisedDiscount;
    int         i;

    for (Object elem : fireSuppressionCostSA.keySet()) {
      key = (String)elem;
      fsc = (double[])fscHt.get(key);
      if (fsc == null) {
        fsc = new double[nSteps];
        for (i=0; i<fsc.length; i++) { fsc[i] = 0.0f; }
        fscHt.put(key,fsc);
      }
      costBySA = (double[])fireSuppressionCostSA.get(key);
      for (i=0; i<costBySA.length; i++) {
        raisedDiscount = (float) Math.pow(discount, (((i+1) * 10) - 5));
        fsc[i] = costBySA[i] / raisedDiscount;
      }
    }
    return fscHt;
  }

  // If we get a discount of 1.0 then just return the cost Hashtable.
  // Otherwise discount the costs and place the result in a new Hashtable.
  // Return the new Hashtable.
  // This way different discount rates can be applied to the original
  // costs without having to recompute the costs.
  public HashMap getFireSuppressionCost(float discount) {
    return getFireSuppressionCost(discount,Simpplle.getCurrentSimulation());
  }
  public HashMap getFireSuppressionCost(float discount, Simulation simulation) {
    int       numSteps = simulation.getNumTimeSteps();
    Fmz[]     allFmz   = Simpplle.getCurrentZone().getAllFmz();
    String    fmzName;
    int       i, j;
    float     raisedDiscount;
    HashMap   fscHt;
    double[]   fsc;
    double[]   costByFmz;

    // if discount is not greater than 1.0 than don't discount.
    // Written this way because one should never test for equality of floats.
    if (((discount < 1.0f) || (discount > 1.0f)) == false) { return fireSuppressionCost; }

    fscHt = new HashMap();
    for(i=0;i<allFmz.length;i++) {
      fmzName = allFmz[i].getName();
      fsc = new double[numSteps];
      for(j=0;j<numSteps;j++) { fsc[j] = 0.0f; }
      fscHt.put(fmzName,fsc);
    }

    for(i=0;i<numSteps;i++) {
      raisedDiscount = (float) Math.pow(discount, (((i+1) * 10) - 5));
      for(j=0;j<allFmz.length;j++) {
        fmzName = allFmz[j].getName();
        costByFmz = (double[]) fireSuppressionCost.get(fmzName);
        fsc       = (double[]) fscHt.get(fmzName);
        fsc[i] = costByFmz[i] / raisedDiscount;
      }
    }
    return fscHt;
  }

  public HashMap getFireSuppressionCostSA() {
    Simulation simulation = Simpplle.getCurrentSimulation();

    if (simulation == null) { return null; }

    return getFireSuppressionCostSA(simulation.getDiscount());
  }
  public HashMap getFireSuppressionCost() {
    Simulation simulation = Simpplle.getCurrentSimulation();

    if (simulation == null) { return null; }

    return getFireSuppressionCost(simulation.getDiscount());
  }

  public MultiKeyMap getGappedProcesses() {
    return gappedProcesses;
  }

  public MultiKeyMap getDProcesses() {
    return DProcesses;
  }

  public void updateEmissions(Evu evu) {
    int cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    updateEmissions(evu, cStep);
  }

  public void updateEmissions(Evu evu, int tStep) {
    fireEmissions[tStep-1]      += Emissions.getProcessPM10(evu,tStep);
    treatmentEmissions[tStep-1] += Emissions.getTreatmentPM10(evu,tStep);
  }

  /**
   * gets the current simulation time step and sends to method to convert emissions figures to tons
   */
  public void finishEmissions() {
    int cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    finishEmissions(cStep);
  }
/**
 * converts the emissions figures to tons
 * @param tStep
 */
  public void finishEmissions(int tStep) {
//    int zoneId = Simpplle.getCurrentZone().getId();
    fireEmissions[tStep-1] /= 2000.0;
    treatmentEmissions[tStep-1] /= 2000.0;
  }
/**
 * method to getthe fire emissions - this will be a double - not yet in tons
 * @param tStep
 * @return
 */
  public double getFireEmissions(int tStep) {
    return fireEmissions[tStep-1];
  }

  public double getTreatmentEmissions(int tStep) {
    return treatmentEmissions[tStep-1];
  }

  /**
   * This method find the event in which a unit was a member (if any)
   *
   * @param unit The unit we are searching for.
   * @param process is the processType we are searching for.
   * @return an ArrayList of the ProcessOccurrence's (if any)
   */
  public ProcessOccurrenceSpreading findSpreadingProcessEvent(Evu unit, ProcessType process) {
    int         tStep   = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    if (process.isFireProcess()) {
      return isFireEvent(getProcessEvents(tStep,unit.getId()),false);
    }

    return isSpreadEvent(getProcessEvents(tStep,unit.getId()),false);
  }

  public ProcessOccurrence findSpreadingProcessEvent(Evu unit) {
    ProcessType process = (ProcessType)unit.getState(SimpplleType.PROCESS);
    if (process == null) { return null; }
    return findSpreadingProcessEvent(unit,process);
  }

  // Note that any unit that has more than once process event could not be
  // an origin unit, simply because that can only happen if something spreads
  // into a unit with another process.
  private boolean isUnitProcessEventOrigin(Evu unit, int tStep, ProcessType process) {
    ProcessOccurrence event = getProcessEvent(unit,tStep,process);
    if (event == null) { return false; }
    if (event.isOriginUnit(unit) == false) { return false; }
    return (event.getProcess().equals(process));
  }

  public ProcessOccurrence getProcessEvent(Evu originUnit, int tStep, ProcessType process) {
    if (getProcessEvents(tStep,originUnit.getId()) instanceof ProcessOccurrence) {
      return (ProcessOccurrence)getProcessEvents(tStep,originUnit.getId());
    }
    else if (getProcessEvents(tStep,originUnit.getId()) instanceof ArrayList) {
      ArrayList events = (ArrayList)getProcessEvents(tStep,originUnit.getId());
      if (process == null) { return (ProcessOccurrence)events.get(events.size()-1); }
      ProcessOccurrence event=null;
      for (int i=0; i<events.size(); i++) {
        event = (ProcessOccurrence) events.get(i);
        if (event.getProcess() == process) { break; }
      }
      return event;
    }
    return null;
  }

  public ProcessOccurrence getProcessEventSpreading(Evu originUnit, int tStep) {
    ProcessOccurrence processEvent = getProcessEvent(originUnit,tStep,null);

    if (processEvent instanceof ProcessOccurrenceSpreading) {
      return processEvent;
    }
    return null;
 }

  public ProcessOccurrence getProcessEventSpreadingFire(Evu originUnit, int tStep) {
    ProcessOccurrence processEvent = getProcessEventSpreading(originUnit,tStep);

    if (processEvent instanceof ProcessOccurrenceSpreadingFire) {
      return processEvent;
    }
    return null;
  }


  public int getOriginFireSpreadType(Evu originEvu, int tStep) {
    ProcessOccurrenceSpreadingFire fireEvent =
        (ProcessOccurrenceSpreadingFire)getProcessEventSpreadingFire(originEvu,tStep);

    return ( (fireEvent != null) ? fireEvent.getProcessProbability() : Evu.S);
  }

  public void addProcessEvent(int tStep, int evuId, ProcessOccurrence event) {
    Object eventsObj = getProcessEvents(tStep,evuId);
    if (eventsObj != null) {
      ArrayList events;
      if (eventsObj instanceof ProcessOccurrence) {
        events = new ArrayList(3);
        events.add((ProcessOccurrence)eventsObj);
        events.add(event);
        setProcessEvents(tStep,evuId,events);
      }
      // This will not happen because there currently can be no more
      // than two events per unit, but I wanted to think ahead and write
      // this so it could handle more if need be.
      else if (eventsObj instanceof ArrayList) {
        ((ArrayList)eventsObj).add(event);
      }
    }
    else {
      setProcessEvents(tStep,evuId,event);
    }
  }
  public void updateProcessOriginatedIn(Evu evu, Lifeform lifeform, ProcessProbability processData,
                                        int timeStep) {
    ProcessOccurrence event;

    if (processData.processType.equals(ProcessType.NONE)) { return; }

    if (processData.processType.isFireProcess()) {
      switch (Simulation.fireSpreadModel) {
        case BASIC:
          event = new BasicFireEvent(evu,lifeform,processData,timeStep);
          break;
        case KEANE:
          event = new KeaneFireEvent(evu,lifeform,processData,timeStep);
          break;
        default:
          event = new BasicFireEvent(evu,lifeform,processData,timeStep);
      }
    } else if (processData.processType.isSpreading()) {
      event = new ProcessOccurrenceSpreading(evu,lifeform,processData,timeStep);
    }
    else {
      event = new ProcessOccurrence(evu,lifeform,processData,timeStep);
    }

    addProcessEvent(timeStep,evu.getId(),event);
  }


  public void collectGisSpreadData(int tStep) {
    spreadFromHt = new HashMap();

    ArrayList events;
    ProcessOccurrenceSpreading event;
    HashMap                    ht;
    Evu                        unit;
    Area                       area = Simpplle.getCurrentArea();

    int size = getDataCount(tStep);
    for (int id = 0; id < size; id++) {
      unit = area.getEvu(id);
      if (unit == null) { continue; }
      events = null;

      Object eventsObj = getProcessEvents(tStep,id);
      if (eventsObj instanceof ArrayList) {
        events = (ArrayList) eventsObj;
      }
      else {
        event = isSpreadEvent(eventsObj,true);
        if (event != null) {
          events = new ArrayList(1);
          events.add(event);
        }
      }
      if (events == null) { continue; }

      for (int e = 0; e < events.size(); e++) {
        event = isSpreadEvent(events.get(e),true);
        if (event == null) { continue; }

        ht = (HashMap) spreadFromHt.get(unit);
        if (ht == null) {
          ht = new HashMap();
          spreadFromHt.put(unit, ht);
        }
        ht.put(event.getProcess(), event.getProcess());
      }
    }
  }
  public void clearGisSpreadData() { spreadFromHt = null; }

  public String getGisSpreadData(Evu evu, ProcessType process, int tStep) {
    if (isUnitProcessEventOrigin(evu, tStep, process)) {
      return ORIGINATED_IN_STR;
    }

    HashMap ht = (HashMap)spreadFromHt.get(evu);
    if (ht != null && ht.get(process) != null) {
      return SPREAD_FROM_STR;
    }
    else { return NIL_STR; }

  }

  // -------------
  // ** Reports **
  // -------------

  private String getFireProcessString(ProcessType process) {
    if (process == ProcessType.SRF)           { return SRF_STR; }
    else if (process == ProcessType.SPOT_SRF) { return SRF_STAR_STR; }
    else if (process == ProcessType.MSF)      { return MSF_STR; }
    else if (process == ProcessType.SPOT_MSF) { return MSF_STAR_STR; }
    else if (process == ProcessType.LSF)      { return LSF_STR; }
    else if (process == ProcessType.SPOT_LSF) { return LSF_STAR_STR; }
    else { return ""; }
  }

  public void emissionsReport(File outputFile) throws SimpplleError {
    PrintWriter fout;

    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      emissionsReport(fout);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      String msg = "Problems writing output file.";
      throw new SimpplleError(msg);
    }
  }

  public void emissionsReportCDF(File outputFile) throws SimpplleError {
    PrintWriter fout;

    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      emissionsReportCDF(fout);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      String msg = "Problems writing output file.";
      throw new SimpplleError(msg);
    }
  }

  private void emissionsReport(PrintWriter fout) {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    int          tSteps = simulation.getNumTimeSteps();
    int          lastTime, i, j, k;
    double       pm2_5;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    fout.println("EMISSIONS REPORT");
    fout.println();
    fout.println();

    for(i=0;i<tSteps;i += 5) {
      if ( (i + 5) > tSteps) {
        lastTime = tSteps;
      }
      else {
        lastTime = i + 5;
      }

      fout.println(Formatting.padLeft("time",54));

      fout.print(Formatting.padLeft("",25));
      for(j=i+1;j<(lastTime+1);j++) {
        fout.print(Formatting.fixedField(j,11));
      }
      fout.println();
      fout.println();

      fout.println("Fire Emissions (Tons)");
      fout.print(Formatting.fixedField("  PM 10",26,true));
      for(k=i;k<lastTime;k++) {
        fout.print(Formatting.fixedField(nf.format(fireEmissions[k]),11));
      }
      fout.println();

      fout.print(Formatting.fixedField("  PM 2.5",26,true));
      for(k=i;k<lastTime;k++) {
        pm2_5 = Emissions.getPM2_5(fireEmissions[k]);
        fout.print(Formatting.fixedField(nf.format(pm2_5),11));
      }
      fout.println();

      fout.println("Treatment Emissions (Tons)");
      fout.print(Formatting.fixedField("  PM 10",26,true));
      for(k=i;k<lastTime;k++) {
        fout.print(Formatting.fixedField(nf.format(treatmentEmissions[k]),11));
      }
      fout.println();

      fout.print(Formatting.fixedField("  PM 2.5",26,true));
      for(k=i;k<lastTime;k++) {
        pm2_5 = Emissions.getPM2_5(treatmentEmissions[k]);
        fout.print(Formatting.fixedField(nf.format(pm2_5),11));
      }
      fout.println();
      fout.println();
      fout.println();
    }
  }

  private void emissionsReportCDF(PrintWriter fout) {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    int          tSteps = simulation.getNumTimeSteps();
    int          lastTime, i;
    float        pm2_5;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    fout.println("Fire Emissions (Tons)");
    fout.println("Time,PM 10,PM 2.5");
    for(i=0;i<tSteps;i++) {
      fout.print((i+1));
      fout.print("," + nf.format(fireEmissions[i]));
      fout.print("," + nf.format(Emissions.getPM2_5(fireEmissions[i])));
      fout.println();
    }
    fout.println();

    fout.println("Treatment Emissions (Tons)");
    fout.println("Time,PM 10,PM 2.5");
    for(i=0;i<tSteps;i++) {
      fout.print((i+1));
      fout.print("," + nf.format(treatmentEmissions[i]));
      fout.print("," + nf.format(Emissions.getPM2_5(treatmentEmissions[i])));
      fout.println();
    }
    fout.println();
  }

  public void fireSuppressionCostReport(File outputFile) {
    PrintWriter fout;

    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      fireSuppressionCostReport(fout);
      if (Simpplle.getCurrentSimulation().trackSpecialArea()) {
        fireSuppressionCostReportSA(fout);
      }

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }

  private void fireSuppressionCostReportSA(PrintWriter fout) {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    RegionalZone zone = Simpplle.getCurrentZone();
    int          tSteps = simulation.getNumTimeSteps();
    int          lastTime, lastName, i, j, k;
    HashMap    costHt = getFireSuppressionCostSA();
    double[]     costBySA, totalCosts = new double[tSteps];
    Enumeration  keys;
    String       sa;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    fout.println("FIRE SUPPRESSION COST REPORT (by Special Area)");
    fout.println();
    fout.println();

    for(i=0;i<tSteps;i++) { totalCosts[i] = 0; }

    for(i=0;i<tSteps;i += 5) {
      if ( (i + 5) > tSteps) {
        lastTime = tSteps;
      }
      else {
        lastTime = i + 5;
      }

      fout.print(Formatting.padLeft("SPECIAL-AREA",2));
      fout.println(Formatting.padLeft("time",16));

      fout.print(Formatting.padLeft("",7));
      for(j=i+1;j<(lastTime+1);j++) {
        fout.print(Formatting.fixedField(j,11));
      }
      fout.println();
      fout.println();

      for (Object elem : costHt.keySet()) {
        sa       = (String)elem;
        costBySA = (double[]) costHt.get(sa);

        fout.print(Formatting.fixedField(sa,12,true));

        for(k=i;k<lastTime;k++) {
          totalCosts[k] += costBySA[k];
          fout.print(Formatting.fixedField(nf.format(costBySA[k]),11));
        }
        fout.println();
      }
      fout.print(Formatting.fixedField("TOTAL",12,true));
      for(k=i;k<lastTime;k++) {
        fout.print(Formatting.fixedField(nf.format(totalCosts[k]),11));
      }
      fout.println();
      fout.println();
      fout.println();
    }
    fout.println();
    fout.println();
  }
  private void fireSuppressionCostReport(PrintWriter fout) {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    RegionalZone zone = Simpplle.getCurrentZone();
    int          tSteps = simulation.getNumTimeSteps();
    int          lastTime, lastName, i, j, k;
    HashMap      fmzHt = getFireSuppressionCost();
    Vector       fmzNames = zone.getAllFmzNames();
    double[]     fsc, totalCosts = new double[tSteps];
    String       str;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    fout.println("FIRE SUPPRESSION COST REPORT");
    fout.println();
    fout.println();

    for(i=0;i<tSteps;i++) { totalCosts[i] = 0; }

    for(i=0;i<tSteps;i += 5) {
      if ( (i + 5) > tSteps) {
        lastTime = tSteps;
      }
      else {
        lastTime = i + 5;
      }

      fout.print(Formatting.padLeft("FMZ",2));
      fout.println(Formatting.padLeft("time",16));

      fout.print(Formatting.padLeft("",7));
      for(j=i+1;j<(lastTime+1);j++) {
        fout.print(Formatting.fixedField(j,11));
      }
      fout.println();
      fout.println();

      for(j=0;j<fmzNames.size();j++) {
        str = (String) fmzNames.elementAt(j);
        fsc = (double[]) fmzHt.get(str);

        fout.print(Formatting.fixedField(str,12,true));

        for(k=i;k<lastTime;k++) {
          totalCosts[k] += fsc[k];
          fout.print(Formatting.fixedField(nf.format(fsc[k]),11));
        }
        fout.println();
      }
      fout.print(Formatting.fixedField("TOTAL",12,true));
      for(k=i;k<lastTime;k++) {
        fout.print(Formatting.fixedField(nf.format(totalCosts[k]),11));
      }
      fout.println();
      fout.println();
      fout.println();
    }
    fout.println();
    fout.println();
  }

  public void fireSpreadReportHeader(File outfile) {
    PrintWriter fout=null;
    try {
      fout = new PrintWriter(new FileOutputStream(outfile));

      fout.println("DETAILED FIRE EVENT REPORT");
      fout.println();
      fout.println();

      if (Simulation.getInstance().fireSuppression()) {
        fout.println("Fires Were Suppressed.");
      }
      else {
        fout.println("Fires Were NOT Suppressed.");
      }
      fout.println();
      fout.println();

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }
  public void fireSpreadReportUpdate(File outfile) throws SimpplleError {
    PrintWriter fout=null;
    try {
      fout = new PrintWriter(new FileOutputStream(outfile, true));

      int ts = Simulation.getCurrentTimeStep();

      fout.printf("%4s  %9s  %7s  %11s%n%n", "Time", "Origin ID", "Process", "Total Acres");

      for (int j = 0; j < suppressedFires[ts - 1].size(); j++) {
        Evu evu = (Evu) suppressedFires[ts - 1].elementAt(j);
        fout.printf("%4d  %9d  %7s  %11.0f%n", ts, evu.getId(),"CLASS-A",0.0);       
      }
      printFireEvent(fout, ProcessType.SRF, ts);
      printFireEvent(fout, ProcessType.MSF, ts);
      printFireEvent(fout, ProcessType.LSF, ts);

      fout.flush();
      fout.close();

    }
    catch (IOException IOX) {
      throw new SimpplleError("Problems writing output file.");
    }
  }
  public void fireSpreadReportFinish(File outfile) throws SimpplleError {
    PrintWriter fout=null;
    try {
      fout = new PrintWriter(new FileOutputStream(outfile,true));

      printFireEventSummary(fout, Simulation.getInstance().getNumTimeSteps());

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      throw new SimpplleError("Problems writing output file.");
    }
  }

  /**
   * Prints a Report of Fire Spreading to a File.
   * Requires all Area Summary Data to be present in memory.
   * @param outputFile is a File.
   */
  public void fireSpreadReport (File outputFile) {
    PrintWriter      fout;

    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      fireSpreadReport(fout);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }

  private void fireSpreadReport(PrintWriter fout) {
    int        tSteps, j;
    Evu        evu;
    Simulation currentSimulation = Simpplle.getCurrentSimulation();

    tSteps = currentSimulation.getNumTimeSteps();

    fout.println("DETAILED FIRE EVENT REPORT");
    fout.println();
    fout.println();

    if (currentSimulation.fireSuppression()) {
      fout.println("Fires Were Suppressed.");
    }
    else {
      fout.println("Fires Were NOT Suppressed.");
    }
    fout.println();
    fout.println();

    fout.printf("%4s  %9s  %7s  %11s%n%n", "Time", "Origin ID", "Process", "Total Acres");
    
//    fout.print("Time");
//    fout.print(Formatting.padLeft(" Originated in ",5));
//    fout.print(Formatting.padLeft("Total Acres", 25));
//    fout.println(Formatting.padLeft("Spread to", 39));
//    fout.println();

    for (int i = 1; i <= tSteps; i++) {
      for (j = 0; j < suppressedFires[i - 1].size(); j++) {
        evu = (Evu) suppressedFires[i - 1].elementAt(j);
        fout.printf("%4d  %9d  %7s  %11.0f%n", i, evu.getId(),"CLASS-A",0.0);       
      }
      printFireEvent(fout,ProcessType.SRF,i);
      printFireEvent(fout,ProcessType.MSF,i);
      printFireEvent(fout,ProcessType.LSF,i);
    }
    printFireEventSummary(fout, tSteps);
  }

  private void printFireEvent(PrintWriter fout, ProcessType process, int tStep) {

    Evu    unit;

    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);

    ProcessOccurrenceSpreadingFire fireEvent;
    ProcessOccurrence[]            fireOccurrences;

    boolean firstLoop = true;
    
    int size = getDataCount(tStep);   
    for (int i=0; i<size; i++) {
      fireEvent = isFireEvent(getProcessEvents(tStep,i),true);
      if (fireEvent == null || !fireEvent.isOriginUnit(i) ||
          fireEvent.getProcess().equals(process) == false) { continue; }

      if (firstLoop) {
        fout.printf("%n ** %s **%n",process.getProcessName());
        fout.printf("%4s  %9s  %7s  %11s  %8s  %7s  %7s  %9s  %10s   %s%n%n", "Time", "Origin ID", "Process", "Total Acres", "Type", "Season", "Line", "Perimeter", "STOP CAUSE", "Spread to");
        firstLoop = false;
      }

      unit = fireEvent.getUnit();

      String procStr = fireEvent.getProcess().getShortName();
      if (fireEvent.getProcessProbability() == Evu.SFS) {
        procStr = procStr + "*";
      }
           
      fout.printf("%4d  %9d  %7s  ", tStep,unit.getId(),procStr);
      
//      fout.print(Formatting.fixedField(tStep, 3));
//      fout.print(Formatting.fixedField(unit.getId(),9));
//      fout.print("-" + fireEvent.getProcess().getShortName());
//      if (fireEvent.getProcessProbability() == Evu.SFS) {
//        fout.print("*");
//      }
//      fout.print(Formatting.padLeft(" ", 6));

      float acres = Area.getFloatAcres(fireEvent.getEventAcres());
      String spreadType = fireEvent.isExtremeEvent() ? "Extreme" : "Average";
      Season season = fireEvent.getFireSeason();
      String seasonStr = (season != null) ? season.toString() : "n/a";

      fireOccurrences = fireEvent.getProcessOccurrences();
      if (fireOccurrences == null) {
//        str = nf.format(Area.getFloatAcres(fireEvent.getEventAcres()));
        fout.printf("%11.0f  %8s  %7s   %s%n",acres,"n/a","n/a","No Spread");
//        fout.print(Formatting.fixedField(str,6));
//        fout.print(Formatting.padLeft(" ", 8));
//        fout.println("No Spread");
        continue;
      }

      // Output total acres,spread type, and season for fire event.

      fout.printf("%11.0f  %8s  %7s  ",acres,spreadType,seasonStr);
      
//      str = nf.format(Area.getFloatAcres(fireEvent.getEventAcres()));
//      fout.print(Formatting.fixedField(str,6));
//      fout.print(Formatting.padLeft(" ", 8));

      int lineProduced = fireEvent.getLineProduced();
      int perimeter    = fireEvent.calculateApproxPerimeter();
      String eventStopReason = fireEvent.getEventStopReason();

      fout.printf("%7d  %9d  %10s   ", lineProduced,perimeter,eventStopReason);

      for (int j=0; j<fireOccurrences.length; j++) {
        if (fireOccurrences[j] == null) { continue; }
        unit = fireOccurrences[j].getUnit();
        
        procStr = fireOccurrences[j].getProcess().getShortName();
        if (fireOccurrences[j].getProcessProbability() == Evu.SFS) {
          procStr = procStr + "*";
        }      
        
        fout.printf("%d-%s ", unit.getId(),procStr);
        
//        fout.print(unit.getId() + "-");
//        fout.print(fireOccurrences[j].getProcess().getShortName());
//        if (fireOccurrences[j].getProcessProbability() == Evu.SFS) {
//          fout.print("*");
//        }
//        fout.print(" ");
      }
      fout.println();
      fout.println();
    }
  }

  public int getTotalFireEventAcres(int timeStep) {
    if (eventAcres == null || timeStep < 0 || timeStep > eventAcres.length-1) {
      return 0;
    }
    return eventAcres[timeStep];
  }
  public void updateTotalFireEventAcres() {
    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    eventAcres[cTime] = calculateTotalFireEventAcres(cTime);
  }
  public int calculateTotalFireEventAcres(int timeStep) {
    ProcessOccurrenceSpreadingFire fireEvent;
    int totAcres=0;

    int size = getDataCount(timeStep);
    for (int i = 0; i < size; i++) {
      fireEvent = isFireEvent(getProcessEvents(timeStep,i), true);
      if (fireEvent == null || !fireEvent.isOriginUnit(i)) {
        continue;
      }

      totAcres += fireEvent.getEventAcres();
    }

    return totAcres;

  }
  public void doFireEventSummary(int ts) {
    ProcessOccurrenceSpreadingFire fireEvent;
    int                            fmzClass;

    int size = getDataCount(ts);
    for (int i = 0; i < size; i++) {
      fireEvent = isFireEvent(getProcessEvents(ts,i), true);
      if (fireEvent == null || !fireEvent.isOriginUnit(i)) {
        continue;
      }

      fmzClass = Fmz.getSizeClass(Area.getFloatAcres(fireEvent.getEventAcres()));
      fireEventSummaryData[ts][fmzClass][0] += fireEvent.getEventAcres();
      fireEventSummaryData[ts][fmzClass][1]++;
    }

    // Now do the Class A fires.
    fireEventSummaryData[ts][Fmz.A][0] += suppressedFires[ts-1].size() * Area.getRationalAcres(0.25f);
    fireEventSummaryData[ts][Fmz.A][1] += suppressedFires[ts-1].size();
  }

  public long getPercentLandscapeBurned(int timeStep) {
    int fireAcres = getTotalFireEventAcres(timeStep);
    int totAcres = Simpplle.getCurrentArea().getAcres();

    return Math.round(((double)fireAcres / (double)totAcres) * 100.0);
  }
  /**
   * The summary report constructed here is sent to the Fire Reports class which then prints it in GUI.  
   * @param fout
   * @param nSteps
   */
  public void printFireEventSummary(PrintWriter fout, int nSteps) {
    int       totalAcres = 0, totalEvents = 0;
    int       j;
    float     acres;

    String A     = "0-0.25";
    String B     = "0.26-9";
    String C     = "10-99";
    String D     = "100-299";
    String E     = "300-999";
    String F     = "1000+";
    String TOTAL = "TOTAL";
    String PCTBURN = "% Landscape";


    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);

    fout.println();
    fout.println();
    fout.println("Fire Event Summary");
    fout.println("------------------");
    fout.println();
    fout.print(Formatting.fixedField(" ", 13));
    fout.print(Formatting.fixedField(A,8) + " ");
    fout.print(Formatting.fixedField(B,8) + " ");
    fout.print(Formatting.fixedField(C,8) + " ");
    fout.print(Formatting.fixedField(D,8) + " ");
    fout.print(Formatting.fixedField(E,8) + " ");
    fout.print(Formatting.fixedField(F,8) + " ");
    fout.print(Formatting.fixedField(TOTAL,8));
    fout.println(Formatting.fixedField(PCTBURN,19));

    for(int i=1;i<=nSteps;i++) {
      fout.println(i);
      fout.print(Formatting.fixedField("Acres",13));
      totalAcres  = 0;
      totalEvents = 0;
      for(j=0;j<Fmz.getNumClasses();j++) {
        totalAcres += fireEventSummaryData[i][j][0];
        acres = Area.getFloatAcres(fireEventSummaryData[i][j][0]);
        fout.print(Formatting.fixedField(nf.format(acres),8) + " ");
      }
      acres = Area.getFloatAcres(totalAcres);
      fout.print(Formatting.fixedField(nf.format(acres),8));
      fout.println(Formatting.fixedField(getPercentLandscapeBurned(i),19));

      fout.print(Formatting.fixedField("# Events",13));
      for(j=0;j<Fmz.getNumClasses();j++) {
        totalEvents += fireEventSummaryData[i][j][1];
        fout.print(Formatting.fixedField(fireEventSummaryData[i][j][1],8) + " ");
      }
      fout.println(Formatting.fixedField(totalEvents,8));

    }
  }
/**
 * This is the comma delineated format for the fire event summary.
 * @param fout
 * @param nSteps
 */
  public void printFireEventSummaryCDF(PrintWriter fout, int nSteps) {
    int       totalAcres = 0, totalEvents = 0;
    int       i, j;
    float     acres;


    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);

    fout.println("Fire Event Acres");
    fout.println("Time,0-0.25,0.26-9,10-99,100-299,300-999,1000+,TOTAL,Percent-Landscape");

    for(i=1; i<=nSteps; i++) {
      fout.print(i);
      totalAcres  = 0;
      for(j=0;j<Fmz.getNumClasses();j++) {
        totalAcres += fireEventSummaryData[i][j][0];
        acres       = Area.getFloatAcres(fireEventSummaryData[i][j][0]);
        fout.print("," + nf.format(acres));
      }
      acres = Area.getFloatAcres(totalAcres);
      fout.print("," + nf.format(acres));
      fout.println("," + getPercentLandscapeBurned(i));
    }
    fout.println();

    fout.println("# Fire Events");
    fout.println("Time,0-0.25,0.26-9,10-99,100-299,300-999,1000+,TOTAL");
    for(i=1; i<=nSteps; i++) {
      fout.print(i+1);
      totalEvents = 0;
      for(j=0;j<Fmz.getNumClasses();j++) {
        totalEvents += fireEventSummaryData[i][j][1];
        fout.print("," + fireEventSummaryData[i][j][1]);
      }
      fout.println("," + totalEvents);
    }
    fout.println();
  }

  public void readSimulation(BufferedReader fin) throws ParseError, IOException {
    readSimulation(fin,Simpplle.getCurrentSimulation());
  }
  public void readSimulation(BufferedReader fin, Simulation simulation)
    throws ParseError, IOException
  {
    String line = fin.readLine();
    if (line == null) {
      throw new ParseError("Area summary is empty");
    }
    if (line.trim().toUpperCase().equals("VERSION 2")) {
      readSimulationV2(fin,simulation);
    }
    else if (line.trim().toUpperCase().equals("VERSION 3")) {
      readSimulationV3(fin,simulation);
    }
    else {
      readSimulationV1(fin,line,simulation);
    }
  }

  private void readSimulationV1(BufferedReader fin, String firstLine,
                                Simulation simulation)
    throws ParseError, IOException
  {
    Area                currentArea = Simpplle.getCurrentArea();
    int                 timeSteps   = simulation.getNumTimeSteps();
    ProcessType[]       processes   = Process.getSummaryProcesses();
    int                 ts, i, j, k, l;
    int[]               acres;
    Evu                 evu, fromEvu, toEvu;
    String              line=null, str;
    StringTokenizerPlus strTok, strSubTok, spreadTok, eventTok;
    int                 time, count, subCount, eventCount, spreadCount;
    int                 eventAcres;
    ProcessType         process, spreadProcess;

    try {
      for (i = 0; i < processes.length; i++) {
        // First get the Process Acres data
        if (i == 0) {
          line = firstLine;
        }
        else {
          line = fin.readLine();
        }
        if (line == null) {
          throw new ParseError("Invalid Area Summary data in file");
        }
        // Don't use this stuff anymore.
//      strTok = new StringTokenizerPlus(line,",");
//
//      process = ProcessType.get(strTok.getToken());
//      acres = new int[timeSteps];
//      for(ts=0;ts<timeSteps;ts++) {
//        acres[ts] = strTok.getIntToken();
//      }
//      processAcres.put(process,acres);

        // Next get the Process Originated In Data.
        // SUCCESSION,5,2:5:6,2:5,2:5,50:100,25
        line = fin.readLine();
        if (line == null) {
          throw new ParseError("Invalid Area Summary data in file");
        }
        strTok = new StringTokenizerPlus(line, ",");

        process = ProcessType.get(strTok.getToken());
        count = strTok.countTokens();
        if (count != timeSteps) {
          throw new ParseError("Invalid Process Origin data in Area Summary");
        }

        for (ts = 0; ts < timeSteps; ts++) {
          str = strTok.getToken();
          if (str == null) {
            continue;
          }
          strSubTok = new StringTokenizerPlus(str, ":");
          count = strSubTok.countTokens();
          for (j = 0; j < count; j++) {
            evu = currentArea.getEvu(strSubTok.getIntToken());
            if (evu == null) {
              continue;
            }

            ProcessProbability processData = new ProcessProbability(process,evu.getState(ts).getProb());
            updateProcessOriginatedIn(evu, Lifeform.NA, processData, ts);
          }
        }

        // Next get the spread origin information.
        line = fin.readLine();
        if (line == null) {
          throw new ParseError("Invalid Area Summary data in file");
        }

        // ** This is redundant info and can be skipped.

//      strTok = new StringTokenizerPlus(line,";");
//      str    = strTok.getToken();
//      if (str.equals("NIL")) {
//        continue;
//      }
//
//      process = ProcessType.get(str);
//      count   = strTok.countTokens();
//
//      for(j=0;j<count;j++) {
//        str       = strTok.getToken();
//        spreadTok = new StringTokenizerPlus(str,",");
//        time      = spreadTok.getIntToken() - 1;
//        str       = spreadTok.getToken();
//        if (str == null) { continue; }
//        eventTok = new StringTokenizerPlus(str,":");
//        eventCount = eventTok.countTokens();
//        for(k=0;k<eventCount;k+=3) {
//          evu        = currentArea.getEvu(eventTok.getIntToken());
//          process    = ProcessType.get(eventTok.getToken());
//          eventAcres = eventTok.getIntToken();
//          processEvent = findProcessEvent(process,processEvents[time][evu.getId()]);
//          if (processEvent == null) {
//            processEvent = new ProcessEvent(process,true,null);
//            processEvents[time][evu.getId()].add(processEvent);
//          }
//          spreadEvent = (SpreadEvent)processEvent.data;
//          if (spreadEvent == null) {
//            spreadEvent = new SpreadEvent(eventAcres,null);
//            processEvent.data = spreadEvent;
//          }
//          spreadEvent.acres = eventAcres;
//        }
//      }

        ProcessOccurrenceSpreading processEvent;
        // Next get the spread to information
        line = fin.readLine();
        if (line == null) {
          throw new ParseError("Invalid Area Summary data in file");
        }
        strTok = new StringTokenizerPlus(line, ";");
        str = strTok.getToken();
        if (str.equals("NIL")) {
          continue;
        }

        process = ProcessType.get(str);
        count = strTok.countTokens();
        for (j = 0; j < count; j++) {
          str = strTok.getToken();
          spreadTok = new StringTokenizerPlus(str, ",");
          spreadCount = spreadTok.countTokens() - 1;
          time = spreadTok.getIntToken();
          for (k = 0; k < spreadCount; k += 2) {
            evu = currentArea.getEvu(spreadTok.getIntToken());
            if (evu == null) {
              continue;
            }

            processEvent = (ProcessOccurrenceSpreading)getProcessEventSpreading(evu, time);
            if (processEvent == null) {
              throw new ParseError("Invalid origin unit found in area summary data");
            }

            str = spreadTok.getToken();
            if (str == null) { continue; }

            eventTok = new StringTokenizerPlus(str, ":");
            eventCount = eventTok.countTokens();
            for (l = 0; l < eventCount; l += 3) {
              fromEvu = currentArea.getEvu(eventTok.getIntToken());
              toEvu = currentArea.getEvu(eventTok.getIntToken());
              str = eventTok.getToken();
              if (str.equals("SRF*") || str.equals("MSF*") || str.equals("LSF*")) {
                spreadProcess = ProcessType.get(str.substring(0,3));
                processEvent.addLegacySpreadEvent(fromEvu, toEvu, spreadProcess,Evu.SFS, time);
              }
              else {
                spreadProcess = ProcessType.get(str);
                processEvent.addLegacySpreadEvent(fromEvu, toEvu, spreadProcess, toEvu.getState().getProb(), time);
              }

            }
            processEvent.finishedAddingLegacySpreadEvents();
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println(line);
    }

    ProcessOccurrenceSpreading.finishedLoadingLegacyFiles();

    Vector v;
    // Next get the Class A Fire information
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }
    if (line.equals("NIL") == false) {
      strTok = new StringTokenizerPlus(line,",");
      count = strTok.countTokens();

      for(j=0;j<count;j++) {
        v = currentArea.parseEvuIdList(strTok.getToken());
        if (v == null) { continue; }
        suppressedFires[j] = v;
      }
    }

    // Next get the Fire Emissions Information
    line = fin.readLine();

    // Older versions generated files without emissions data.
    // If this info is missing then we want to recalculate it.
    if (line == null || line.trim().length() == 0) {
      currentArea.doEmissions();
      return;
    }

    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();

    for(j=0; j<count; j++) {
      fireEmissions[j] = strTok.getFloatToken();
    }

    // Next get the Treatment Emissions Information
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }

    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();

    for(j=0; j<count; j++) {
      treatmentEmissions[j] = strTok.getFloatToken();
    }
  }

  /**
   * This reads version 2 of the data file.  The data structure holding
   * process information had to be changed to be more memory efficient.
   * The new data structure necessited changing the file format to make
   * writing fast and efficient as well.
   * @param fin
   * @throws ParseError
   * @throws IOException
   */
  private void readSimulationV2(BufferedReader fin, Simulation simulation)
    throws ParseError, IOException
  {
    String line, str;

    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }
    while(line.trim().length() > 0) {
//      strTok = new StringTokenizerPlus(line,",");
//
//      process = ProcessType.get(strTok.getToken());
//      if (process == null) {
//        throw new ParseError("Unknown process is line: \n" + line);
//      }
//      count = strTok.countTokens();
//      acres = new int[count];
//      for(ts=0; ts<timeSteps; ts++) {
//        acres[ts] = strTok.getIntToken();
//      }
//      processAcres.put(process,acres);
      line = fin.readLine();
      if (line == null) {
        throw new ParseError("Invalid Area Summary data in file in Process Acres section");
      }
    }

    int                        i, count, tStep, eventAcres;
    Area                       currentArea = Simpplle.getCurrentArea();
    Evu                        fromEvu, toEvu, originEvu;
    StringTokenizerPlus        strTok, strSubTok, eventTok;
    ProcessType                process;
    ProcessOccurrence          processOccurrence;
    ProcessOccurrenceSpreading spreadEvent;

    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }

    // Evu id,timestep,process,event acres,<event-data>;<event-data>
    // where <event-data> is process:<from evu id>:<to evu id>
    // note: event acres and event-data only appears if the process spread.
    while (line.trim().length() > 0) {
      strTok = new StringTokenizerPlus(line,",");

      originEvu = currentArea.getEvu(strTok.getIntToken());
      tStep     = strTok.getIntToken();
      process   = ProcessType.get(strTok.getToken());

      ProcessProbability processData = new ProcessProbability(process,originEvu.getState(tStep).getProb());
      updateProcessOriginatedIn(originEvu,Lifeform.NA,processData,tStep);

      count = strTok.countTokens();
      if (count > 0) {
        eventAcres = strTok.getIntToken(); // Don't need anymore, but still have to read.
        spreadEvent = (ProcessOccurrenceSpreading)getProcessEventSpreading(originEvu,tStep);

        if (count > 1) {
          strSubTok = new StringTokenizerPlus(strTok.getToken(),";");
          count = strSubTok.countTokens();
          for(i=0; i<count; i++) {
            eventTok = new StringTokenizerPlus(strSubTok.getToken(),":");
            str      = eventTok.getToken();
            fromEvu  = currentArea.getEvu(eventTok.getIntToken());
            toEvu    = currentArea.getEvu(eventTok.getIntToken());

            if (str.equals("SRF*") || str.equals("MSF*") || str.equals("LSF*")) {
              process = ProcessType.get(str.substring(0,3));
              spreadEvent.addLegacySpreadEvent(fromEvu,toEvu,process,Evu.SFS,tStep);
            }
            else {
              process = ProcessType.get(str);
              spreadEvent.addLegacySpreadEvent(fromEvu,toEvu,process,toEvu.getState().getProb(),tStep);
            }
          }
        }
        spreadEvent.finishedAddingLegacySpreadEvents();
      }

      line = fin.readLine();
      if (line == null) {
        throw new ParseError("Invalid Area Summary data in file");
      }
    }
    ProcessOccurrenceSpreading.finishedLoadingLegacyFiles();

    int    j;
    Vector v;
    // Next get the Class A Fire information
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }
    if (line.equals("NIL") == false) {
      strTok = new StringTokenizerPlus(line,",");
      count = strTok.countTokens();

      for(j=0;j<count;j++) {
        v = currentArea.parseEvuIdList(strTok.getToken());
        if (v == null) { continue; }
        suppressedFires[j] = v;
      }
    }

    // Next get the Fire Emissions Information
    line = fin.readLine();

    // Older versions generated files without emissions data.
    // If this info is missing then we want to recalculate it.
    if (line == null || line.trim().length() == 0) {
      currentArea.doEmissions();
      return;
    }

    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();

    for(j=0; j<count; j++) {
      fireEmissions[j] = strTok.getFloatToken();
    }

    // Next get the Treatment Emissions Information
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }

    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();

    for(j=0; j<count; j++) {
      treatmentEmissions[j] = strTok.getFloatToken();
    }
  }

  private void readSimulationV3(BufferedReader fin, Simulation simulation)
    throws ParseError, IOException
  {
    int                        i, count, tStep;
    Area                       currentArea = Simpplle.getCurrentArea();
    Evu                        fromEvu, toEvu, originEvu;
    StringTokenizerPlus        strTok;
    ProcessType                process;
    int                        processProb;
    ProcessOccurrenceSpreading spreadEvent=null;

    String str;
    String line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }

    // Evu id,timestep,process,<event-data>,<event-data>
    // where <event-data> is <from evu id>,<to evu id>,process,process probability
    while (line.trim().length() > 0) {
      strTok = new StringTokenizerPlus(line, ",");

      originEvu = currentArea.getEvu(strTok.getIntToken());
      tStep = strTok.getIntToken();
      process = ProcessType.get(strTok.getToken());

      ProcessProbability processData = new ProcessProbability(process,originEvu.getState(tStep).getProb());
      updateProcessOriginatedIn(originEvu, Lifeform.NA, processData, tStep);

      count = strTok.countTokens();
      if (count > 0) {
        spreadEvent = (ProcessOccurrenceSpreading) getProcessEventSpreading(
            originEvu, tStep);

        count = strTok.countTokens();
        for (i = 0; i < count; i += 4) {
          fromEvu = currentArea.getEvu(strTok.getIntToken());
          toEvu = currentArea.getEvu(strTok.getIntToken());
          process = ProcessType.get(strTok.getToken());
          processProb = strTok.getIntToken();

          spreadEvent.addLegacySpreadEvent(fromEvu, toEvu, process, processProb,
                                           tStep);
        }
        spreadEvent.finishedAddingLegacySpreadEvents();
      }

      line = fin.readLine();
      if (line == null) {
        throw new ParseError("Invalid Area Summary data in file");
      }
    }

    ProcessOccurrenceSpreading.finishedLoadingLegacyFiles();

    int    j;
    Vector v;
    // Next get the Class A Fire information
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }
    if (line.equals("NIL") == false) {
      strTok = new StringTokenizerPlus(line,",");
      count = strTok.countTokens();

      for(j=0;j<count;j++) {
        v = currentArea.parseEvuIdList(strTok.getToken());
        if (v == null) { continue; }
        suppressedFires[j] = v;
      }
    }

    // Next get the Fire Emissions Information
    line = fin.readLine();

    // Older versions generated files without emissions data.
    // If this info is missing then we want to recalculate it.
    if (line == null || line.trim().length() == 0) {
      currentArea.doEmissions();
      return;
    }

    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();

    for(j=0; j<count; j++) {
      fireEmissions[j] = strTok.getFloatToken();
    }

    // Next get the Treatment Emissions Information
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Invalid Area Summary data in file");
    }

    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();

    for(j=0; j<count; j++) {
      treatmentEmissions[j] = strTok.getFloatToken();
    }
  }
/*
  public void saveSimulation(PrintWriter fout) {
    fout.println();
    fout.println("CLASS AREA-SUMMARY");
    fout.println("VERSION 3");

    // Evu id,timestep,process,<event-data>,<event-data>
    // where <event-data> is <from evu id>,<to evu id>,process,process probability
    ProcessOccurrence[]        processOccurrences;
    ProcessOccurrenceSpreading spreadEvent;

    int timeSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    for(int ts=0; ts<=timeSteps; ts++) {
      for(int id=0; id<processes[ts].length; id++) {
        if (processes[ts][id] == null) { continue; }

        fout.print(processes[ts][id].getUnit().getId());
        fout.print(COMMA);
        fout.print(ts);
        fout.print(COMMA);
        fout.print(processes[ts][id].getProcess());

        spreadEvent = (ProcessOccurrenceSpreading)getProcessEventSpreading(processes[ts][id].getUnit(),ts);
        if (spreadEvent != null) { spreadEvent.save(fout); }
        fout.println();
      }
    }
    fout.println();  // blank line to seperate sections.

    // Print Out Class A fire information.
    // unit id : unit id : unit id, unit-id : unit-id, etc.
    Evu evu;
    int i, j;

    for(i=0; i<timeSteps; i++) {
      if (i > 0) { fout.print(COMMA); }
      if (suppressedFires[i].size() == 0) {
        fout.print(QUESTION_MARK);
      }
      else {
        evu = (Evu)suppressedFires[i].elementAt(0);
        fout.print(evu.getId());
      }
      for(j=1; j<suppressedFires[i].size(); j++) {
        evu = (Evu)suppressedFires[i].elementAt(j);
        fout.print(COLON);
        fout.print(evu.getId());
      }
    }
    fout.println();

    // *** These 2 items added 8/15/01
    // ***    fire/treatment emissions

    // Print Out Fire Emissions information.
    for(i=0; i<fireEmissions.length; i++) {
      if (i > 0) { fout.print(COMMA); }
      fout.print(fireEmissions[i]);
    }
    fout.println();

    // Print Out Treatment Emissions information.
    for(i=0; i<treatmentEmissions.length; i++) {
      if (i > 0) { fout.print(COMMA); }
      fout.print(treatmentEmissions[i]);
    }
    fout.println();
  }
*/
  public Season getFireOccurrenceSeason(Evu unit) {
    ProcessOccurrenceSpreadingFire event;

    event = (ProcessOccurrenceSpreadingFire)findSpreadingProcessEvent(unit);
    return event.getFireSeason();
  }

  /**
   * Applies spreading processes to each EVU until the spreading is complete.
   */
  public void doSpread() {

    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    ArrayList events = new ArrayList();

//    MyThreadGroup tg = new MyThreadGroup("ProcessSpreading",numSpreadProcesses);
//    Thread       t;

    ProcessOccurrenceSpreading event;

    int size = getDataCount(cTime);
    for (int id = 0; id < size; id++) {
      if (Area.multipleLifeformsEnabled()) {
        initSpreadEvents(getProcessEvents(cTime,id),events);
      } else {
        event = isSpreadEvent(getProcessEvents(cTime,id), false);
        if (event != null) events.add(event);
      }
    }

//    t = new Thread(tg,event,event.getUnit().toString());
//    event.setThread(t);

    int activeEvents = events.size();
    while (activeEvents > 0) {
      for (int i = 0; i < events.size(); i++) {
        event = (ProcessOccurrenceSpreading) events.get(i);
        if (event.isFinished() == false) {
          event.doSpread();
          if (event.isFinished()) {
            activeEvents--;
          }
        }
      }
    }

//    for (i=0; i<events.size(); i++) {
//      event = (ProcessOccurrenceSpreading)events.get(i);
//      t     = event.getThread();
//      t.start();
//    }

//    try {
//      synchronized (tg) {
//        tg.wait();
//      }
//    }
//    catch (InterruptedException ex) {
//    }
  }

  /*
        private ProcessOccurrence[][] processes;
        private ArrayList[]           spreadProcesses;
        private ArrayList[]           fireSpreadProcesses;
        private Vector[] suppressedFires;
        // The keys to this hashtable will be fmz's.
        private Hashtable fireSuppressionCost;
        // The keys to this hashtable will be special area.
        private Hashtable fireSuppressionCostSA;
        private double[] fireEmissions;
        private double[] treatmentEmissions;
  */
  public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {
    int version = in.readInt();
    processEvents = (Object[][])in.readObject();

    /**
     * Version 1 of this array only held events in origin unit id's
     * Since then I have placed events in every id, so this code attempts,
     * to fill in the blanks.
     * It should also be noted that, since I failed to save the data in
     * the not removed spreading events array lists, some events will have
     * been lost if they were overwritten by fire; Since Previously the array
     * only held the most recent event.
     */
    if (version == 1) {
      ArrayList events = new ArrayList();
      ProcessOccurrence[]        nodes;
      ProcessOccurrenceSpreading event;
      for (int ts = 0; ts < processEvents.length; ts++) {
        events.clear();
        for (int id = 0; id < processEvents[ts].length; id++) {
          if (processEvents[ts][id] == null) { continue; }
          if (processEvents[ts][id] instanceof ProcessOccurrenceSpreading) {
            events.add(processEvents[ts][id]);
          }
        }
        for (int i=0; i<events.size(); i++) {
          event = (ProcessOccurrenceSpreading) events.get(i);
          nodes = event.getProcessOccurrences();
          for (int n=0; n<nodes.length; n++) {
            processEvents[ts][nodes[n].getUnit().getId()] = event;
          }
        }
      }
    }

    suppressedFires = (Vector[])in.readObject();

    fireSuppressionCost = null;
    fireSuppressionCostSA = null;

    if (in.readBoolean()) {
      int size = in.readInt();
      fireSuppressionCost = new HashMap(size);
      for (int i = 0; i < size; i++) {
        fireSuppressionCost.put((String) in.readObject(),
                                (double[]) in.readObject());
      }
    }

    if (in.readBoolean()) {
      int size = in.readInt();
      fireSuppressionCostSA = new HashMap(size);
      for (int i = 0; i < size; i++) {
        fireSuppressionCostSA.put((String) in.readObject(),
                                  (double[]) in.readObject());
      }
    }

    fireEmissions      = (double[])in.readObject();
    treatmentEmissions = (double[])in.readObject();

    if (version > 2) {
      Area area = Simpplle.getCurrentArea();
      {
        gappedProcesses.clear();
        int size = in.readInt();
        for (int i=0; i<size; i++) {
          Evu         evu      = area.getEvu(in.readInt());
          Lifeform    lifeform = (Lifeform)in.readObject();
          int         run      = in.readInt();
          int         ts       = in.readInt();
          ProcessType process = ProcessType.readExternalSimple(in);

          gappedProcesses.put(evu,lifeform,run,ts,process);
        }
      }

      {
        DProcesses.clear();
        int size = in.readInt();
        for (int i=0; i<size; i++) {
          Evu         evu      = area.getEvu(in.readInt());
          Lifeform    lifeform = (Lifeform)in.readObject();
          int         run      = in.readInt();
          int         ts       = in.readInt();
          ProcessType process = ProcessType.readExternalSimple(in);

          DProcesses.put(evu,lifeform,run,ts,process);
        }
      }

    }

    if (version > 3) {
      initSummaryData();
      readExternalSummary(in,processSummary);
      readExternalSummarySpecial(in,processSummarySpecialArea);
      readExternalSummarySpecial(in,processSummaryOwnership);
      readExternalSummarySpecial(in,processSummarySpecialOwner);

      readExternalSummary(in,speciesSummary);
      readExternalSummarySpecial(in,speciesSummarySpecialArea);
      readExternalSummarySpecial(in,speciesSummaryOwnership);
      readExternalSummarySpecial(in,speciesSummarySpecialOwner);

      readExternalSummary(in,sizeClassSummary);
      readExternalSummarySpecial(in,sizeClassSummarySpecialArea);
      readExternalSummarySpecial(in,sizeClassSummaryOwnership);
      readExternalSummarySpecial(in,sizeClassSummarySpecialOwner);

      readExternalSummary(in,densitySummary);
      readExternalSummarySpecial(in,densitySummarySpecialArea);
      readExternalSummarySpecial(in,densitySummaryOwnership);
      readExternalSummarySpecial(in,densitySummarySpecialOwner);

      readExternalSummary(in,speciesSummaryCL);
      readExternalSummarySpecial(in,speciesSummarySpecialAreaCL);
      readExternalSummarySpecial(in,speciesSummaryOwnershipCL);
      readExternalSummarySpecial(in,speciesSummarySpecialOwnerCL);

      readExternalSummary(in,sizeClassSummaryCL);
      readExternalSummarySpecial(in,sizeClassSummarySpecialAreaCL);
      readExternalSummarySpecial(in,sizeClassSummaryOwnershipCL);
      readExternalSummarySpecial(in,sizeClassSummarySpecialOwnerCL);

      readExternalSummary(in,densitySummaryCL);
      readExternalSummarySpecial(in,densitySummarySpecialAreaCL);
      readExternalSummarySpecial(in,densitySummaryOwnershipCL);
      readExternalSummarySpecial(in,densitySummarySpecialOwnerCL);

      {
        int size = in.readInt();
        allStatesReportData = new ArrayList<AllStatesReportData>(size);
        for (int i=0; i<size; i++) {
          allStatesReportData.add((AllStatesReportData)in.readObject());
        }
      }

    }

    if (version > 4) {
      if (in.readBoolean()) {
        int size1 = in.readInt();
        int size2 = in.readInt();
        int size3 = in.readInt();
        fireEventSummaryData = new int[size1][size2][size3];

        for (int ts = 0; ts<size1; ts++) {
          for (int i = 0; i < size2; i++) {
            fireEventSummaryData[ts][i][0] = in.readInt();
            fireEventSummaryData[ts][i][1] = in.readInt();
          }
        }

      }
    }
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeObject(processEvents);
    out.writeObject(suppressedFires);

    Enumeration e;

    out.writeBoolean(fireSuppressionCost != null);
    if (fireSuppressionCost != null) {
      out.writeInt(fireSuppressionCost.size());
      for (Object elem : fireSuppressionCost.keySet()) {
        String key = (String)elem;
        out.writeObject(key);
        out.writeObject((double[]) fireSuppressionCost.get(key));
      }
    }

    out.writeBoolean(fireSuppressionCostSA != null);
    if (fireSuppressionCostSA != null) {
      out.writeInt(fireSuppressionCostSA.size());
      for (Object elem : fireSuppressionCostSA.keySet()) {
        String key = (String)elem;
        out.writeObject(key);
        out.writeObject((double[]) fireSuppressionCostSA.get(key));
      }
    }

    out.writeObject(fireEmissions);
    out.writeObject(treatmentEmissions);

    {
      int size = gappedProcesses.size();
      out.writeInt(size);
      for (Object elem : gappedProcesses.keySet()) {
        MultiKey mkey = (MultiKey) elem;

        Evu evu = (Evu) mkey.getKey(0);
        Lifeform lifeform = (Lifeform) mkey.getKey(1);
        int run = (Integer) mkey.getKey(2);
        int ts = (Integer) mkey.getKey(3);

        ProcessType process = (ProcessType) gappedProcesses.get(evu, lifeform, run, ts);

        out.writeInt(evu.getId());
        out.writeObject(lifeform);
        out.writeInt(run);
        out.writeInt(ts);
        process.writeExternalSimple(out);
      }
    }
    {
      int size = DProcesses.size();
      out.writeInt(size);
      for (Object elem : DProcesses.keySet()) {
        MultiKey mkey = (MultiKey) elem;

        Evu evu = (Evu) mkey.getKey(0);
        Lifeform lifeform = (Lifeform) mkey.getKey(1);
        int run = (Integer) mkey.getKey(2);
        int ts = (Integer) mkey.getKey(3);

        ProcessType process = (ProcessType) DProcesses.get(evu, lifeform, run, ts);

        out.writeInt(evu.getId());
        out.writeObject(lifeform);
        out.writeInt(run);
        out.writeInt(ts);
        process.writeExternalSimple(out);
      }
    }

    writeExternalSummary(out,processSummary);
    writeExternalSummarySpecial(out,processSummarySpecialArea);
    writeExternalSummarySpecial(out,processSummaryOwnership);
    writeExternalSummarySpecial(out,processSummarySpecialOwner);

    writeExternalSummary(out,speciesSummary);
    writeExternalSummarySpecial(out,speciesSummarySpecialArea);
    writeExternalSummarySpecial(out,speciesSummaryOwnership);
    writeExternalSummarySpecial(out,speciesSummarySpecialOwner);

    writeExternalSummary(out,sizeClassSummary);
    writeExternalSummarySpecial(out,sizeClassSummarySpecialArea);
    writeExternalSummarySpecial(out,sizeClassSummaryOwnership);
    writeExternalSummarySpecial(out,sizeClassSummarySpecialOwner);

    writeExternalSummary(out,densitySummary);
    writeExternalSummarySpecial(out,densitySummarySpecialArea);
    writeExternalSummarySpecial(out,densitySummaryOwnership);
    writeExternalSummarySpecial(out,densitySummarySpecialOwner);

    writeExternalSummary(out,speciesSummaryCL);
    writeExternalSummarySpecial(out,speciesSummarySpecialAreaCL);
    writeExternalSummarySpecial(out,speciesSummaryOwnershipCL);
    writeExternalSummarySpecial(out,speciesSummarySpecialOwnerCL);

    writeExternalSummary(out,sizeClassSummaryCL);
    writeExternalSummarySpecial(out,sizeClassSummarySpecialAreaCL);
    writeExternalSummarySpecial(out,sizeClassSummaryOwnershipCL);
    writeExternalSummarySpecial(out,sizeClassSummarySpecialOwnerCL);

    writeExternalSummary(out,densitySummaryCL);
    writeExternalSummarySpecial(out,densitySummarySpecialAreaCL);
    writeExternalSummarySpecial(out,densitySummaryOwnershipCL);
    writeExternalSummarySpecial(out,densitySummarySpecialOwnerCL);

    //  private ArrayList<AllStatesReportData> allStatesReportData
    {
      int size = (allStatesReportData != null ? allStatesReportData.size() : 0);
      out.writeInt(size);
      for (int i=0; i<size; i++) {
        out.writeObject(allStatesReportData.get(i));
      }
    }

    out.writeBoolean(fireEventSummaryData != null);
    if (fireEventSummaryData != null) {
      out.writeInt(fireEventSummaryData.length);
      out.writeInt(fireEventSummaryData[0].length);
      out.writeInt(fireEventSummaryData[0][0].length);
    }
    for (int ts = 0; ts<fireEventSummaryData.length; ts++) {
      for (int i = 0; i < fireEventSummaryData[ts].length; i++) {
        out.writeInt(fireEventSummaryData[ts][i][0]);
        out.writeInt(fireEventSummaryData[ts][i][1]);
      }
    }
  }

  public void readExternalSummarySpecial(ObjectInput in, HashMap specialHm) throws IOException,ClassNotFoundException
  {
    int size = in.readInt();
    for (int i=0; i<size; i++) {
      Object key = in.readObject();
      HashMap value = readExternalSummary(in);

      specialHm.put(key,value);
    }
  }
  private void writeExternalSummarySpecial(ObjectOutput out, HashMap specialHm)
    throws IOException
  {
    int size = (specialHm != null ? specialHm.size() : 0);
    out.writeInt(size);
    if (size > 0) {
      for (Object key : specialHm.keySet()) {
        HashMap hm = (HashMap)specialHm.get(key);
        out.writeObject(key);
        writeExternalSummary(out,hm);
      }
    }
  }

  public HashMap readExternalSummary(ObjectInput in)
    throws IOException,ClassNotFoundException
  {
    return readExternalSummary(in,null);
  }
  public HashMap readExternalSummary(ObjectInput in, HashMap hm) throws IOException,ClassNotFoundException
  {
    int size = in.readInt();
    for (int i=0; i<size; i++) {
      Object  key;
      boolean isSimpplleType = in.readBoolean();
      if (isSimpplleType) {
        key = (SimpplleType)SimpplleType.readExternalSimple(in,SimpplleType.PROCESS);
      }
      else {
        key = in.readObject();
      }

      if (i==0 && hm==null) {
        if (isSimpplleType) {
          hm = new HashMap<SimpplleType,String>();
        }
        else {
          hm = new HashMap<String,String>();
        }
      }

      int[] acres = (int[])in.readObject();

      hm.put(key,acres);
    }

    return hm;
  }

  private void writeExternalSummary(ObjectOutput out, HashMap hm) throws IOException {
    int size = (hm != null ? hm.size() : 0);
    out.writeInt(size);
    if (size > 0) {
      for (Object key : hm.keySet()) {
        int[] acres = (int[])hm.get(key);
        if (key instanceof SimpplleType) {
          out.writeBoolean(true);
          ((SimpplleType)key).writeExternalSimple(out);
        }
        else {
          out.writeBoolean(false);
          out.writeObject(key);
        }
        out.writeObject(acres);
      }
    }
  }

  public void writeDatabase()
    throws SimpplleError
  {
    Session session = DatabaseCreator.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();

    int cRun  = Simpplle.getCurrentSimulation().getCurrentRun();
    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    int size = getDataCount(cTime);
    for (int id=0; id<size; id++) {
      Object eventObj = getProcessEvents(cTime,id);
      if (eventObj == null) { continue; }
      if (eventObj instanceof ProcessOccurrence) {
        writeDatabaseProcessOccurrence(session,id,eventObj,cRun,cTime);
      }
      else if (eventObj instanceof ArrayList) {
        ArrayList events = (ArrayList)eventObj;
        for (int i=0; i<events.size(); i++) {
          writeDatabaseProcessOccurrence(session,id,events.get(i),cRun,cTime);
        }
      }
    }

    tx.commit();
    session.close();
  }
  public void writeAccessFiles(PrintWriter fout) throws SimpplleError
  {
    int cRun  = Simpplle.getCurrentSimulation().getCurrentRun()+1;
    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    int size = getDataCount(cTime);
    for (int id=0; id<size; id++) {
      Object eventObj = getProcessEvents(cTime,id);
      if (eventObj == null) { continue; }
      if (eventObj instanceof ProcessOccurrence) {
        writeAccessFilesProcessOccurrence(fout,id,eventObj,cRun,cTime);
      }
      else if (eventObj instanceof ArrayList) {
        ArrayList events = (ArrayList)eventObj;
        for (int i=0; i<events.size(); i++) {
          writeAccessFilesProcessOccurrence(fout,id,events.get(i),cRun,cTime);
        }
      }
    }
  }

  public void cleanup() {
//    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
//    Object event;
//
//    for (int id=0; id<processEvents[cTime].length; id++) {
//      event = processEvents[cTime][id];
//
//      if (processEvents[cTime][id] instanceof ArrayList) {
//        ArrayList list = (ArrayList)event;
//        for (int i=0; i<list.size(); i++) {
//          event = list.get(i);
//          destoryProcessOccurrence(event,id);
//        }
//      }
//      else if (event instanceof ProcessOccurrence) {
//        destoryProcessOccurrence(processEvents[cTime][id],id);
//      }
//      processEvents[cTime][id] = null;
//    }
  }

//  private void destoryProcessOccurrence(Object event, int id) {
//    if (event instanceof ProcessOccurrenceSpreadingFire) {
//      if ( ((ProcessOccurrenceSpreadingFire)event).isOriginUnit(id) ) {
//        ((ProcessOccurrenceSpreadingFire)event).destoryMe();
//      }
//    }
//    else if (event instanceof ProcessOccurrenceSpreading) {
//      if ( ((ProcessOccurrenceSpreading)event).isOriginUnit(id) ) {
//        ((ProcessOccurrenceSpreading)event).destoryMe();
//      }
//    }
//    else if (event instanceof ProcessOccurrence) {
//      ((ProcessOccurrence)event).destoryMe();
//    }
//  }

  private void writeDatabaseProcessOccurrence(Session session, int id,
                                              Object event,
                                              int cRun, int cTime)
    throws SimpplleError
  {
    if (event instanceof ProcessOccurrenceSpreading) {
      ProcessOccurrenceSpreading spreadEvent = (ProcessOccurrenceSpreading)event;
      if (spreadEvent.isOriginUnit(id)) {
        spreadEvent.writeEventDatabase(session,cRun,cTime);
      }
    }
    else if (event instanceof ProcessOccurrence) {
      ((ProcessOccurrence)event).writeEventDatabase(session,cRun,cTime);
    }
  }
  private void writeAccessFilesProcessOccurrence(PrintWriter fout, int id,
                                                 Object event, int cRun, int cTime)
    throws SimpplleError
  {
    if (event instanceof ProcessOccurrenceSpreading) {
      ProcessOccurrenceSpreading spreadEvent = (ProcessOccurrenceSpreading)event;
      if (spreadEvent.isOriginUnit(id)) {
        spreadEvent.writeEventAccessFiles(fout,cRun,cTime);
      }
    }
    else if (event instanceof ProcessOccurrence) {
      ((ProcessOccurrence)event).writeEventAccessFiles(fout,cRun,cTime);
    }
  }

  // The int[] here is an array of rationalized acres by time step.
  private HashMap<SimpplleType,int[]>                 processSummary;
  private HashMap<String,HashMap<SimpplleType,int[]>> processSummarySpecialArea;
  private HashMap<String,HashMap<SimpplleType,int[]>> processSummaryOwnership;
  private HashMap<String,HashMap<SimpplleType,int[]>> processSummarySpecialOwner;

  private HashMap<String,int[]> speciesSummary;
  private HashMap<String,HashMap<String,int[]>> speciesSummarySpecialArea;
  private HashMap<String,HashMap<String,int[]>> speciesSummaryOwnership;
  private HashMap<String,HashMap<String,int[]>> speciesSummarySpecialOwner;

  private HashMap<String,int[]> sizeClassSummary;
  private HashMap<String,HashMap<String,int[]>> sizeClassSummarySpecialArea;
  private HashMap<String,HashMap<String,int[]>> sizeClassSummaryOwnership;
  private HashMap<String,HashMap<String,int[]>> sizeClassSummarySpecialOwner;


  private HashMap<String,int[]> densitySummary;
  private HashMap<String,HashMap<String,int[]>> densitySummarySpecialArea;
  private HashMap<String,HashMap<String,int[]>> densitySummaryOwnership;
  private HashMap<String,HashMap<String,int[]>> densitySummarySpecialOwner;

  private HashMap<String,int[]> speciesSummaryCL;
  private HashMap<String,HashMap<String,int[]>> speciesSummarySpecialAreaCL;
  private HashMap<String,HashMap<String,int[]>> speciesSummaryOwnershipCL;
  private HashMap<String,HashMap<String,int[]>> speciesSummarySpecialOwnerCL;

  private HashMap<String,int[]> sizeClassSummaryCL;
  private HashMap<String,HashMap<String,int[]>> sizeClassSummarySpecialAreaCL;
  private HashMap<String,HashMap<String,int[]>> sizeClassSummaryOwnershipCL;
  private HashMap<String,HashMap<String,int[]>> sizeClassSummarySpecialOwnerCL;

  private HashMap<String,int[]> densitySummaryCL;
  private HashMap<String,HashMap<String,int[]>> densitySummarySpecialAreaCL;
  private HashMap<String,HashMap<String,int[]>> densitySummaryOwnershipCL;
  private HashMap<String,HashMap<String,int[]>> densitySummarySpecialOwnerCL;

  public static AreaSummary tmpInstance;
  public static AreaSummary getTempInstance() { return tmpInstance; }

  public static void newAreaSummaryTemp() {
    tmpInstance = new AreaSummary();
    tmpInstance.initSummaryData();
    tmpInstance.updateEvuSummaryData();
  }
  public static void clearTempInstance() { tmpInstance = null; }

  private void initSummaryData() {
    processSummary = new HashMap<SimpplleType,int[]>();
    processSummarySpecialArea  = new HashMap<String,HashMap<SimpplleType,int[]>>();
    processSummaryOwnership    = new HashMap<String,HashMap<SimpplleType,int[]>>();
    processSummarySpecialOwner = new HashMap<String,HashMap<SimpplleType,int[]>>();

    speciesSummary   = new HashMap<String,int[]>();
    speciesSummarySpecialArea  = new HashMap<String,HashMap<String,int[]>>();
    speciesSummaryOwnership    = new HashMap<String,HashMap<String,int[]>>();
    speciesSummarySpecialOwner = new HashMap<String,HashMap<String,int[]>>();

    sizeClassSummary = new HashMap<String,int[]>();
    sizeClassSummarySpecialArea  = new HashMap<String,HashMap<String,int[]>>();
    sizeClassSummaryOwnership    = new HashMap<String,HashMap<String,int[]>>();
    sizeClassSummarySpecialOwner = new HashMap<String,HashMap<String,int[]>>();


    densitySummary   = new HashMap<String,int[]>();
    densitySummarySpecialArea  = new HashMap<String,HashMap<String,int[]>>();
    densitySummaryOwnership    = new HashMap<String,HashMap<String,int[]>>();
    densitySummarySpecialOwner = new HashMap<String,HashMap<String,int[]>>();

    speciesSummaryCL   = new HashMap<String,int[]>();
    speciesSummarySpecialAreaCL  = new HashMap<String,HashMap<String,int[]>>();
    speciesSummaryOwnershipCL    = new HashMap<String,HashMap<String,int[]>>();
    speciesSummarySpecialOwnerCL = new HashMap<String,HashMap<String,int[]>>();

    sizeClassSummaryCL = new HashMap<String,int[]>();
    sizeClassSummarySpecialAreaCL  = new HashMap<String,HashMap<String,int[]>>();
    sizeClassSummaryOwnershipCL    = new HashMap<String,HashMap<String,int[]>>();
    sizeClassSummarySpecialOwnerCL = new HashMap<String,HashMap<String,int[]>>();

    densitySummaryCL   = new HashMap<String,int[]>();
    densitySummarySpecialAreaCL  = new HashMap<String,HashMap<String,int[]>>();
    densitySummaryOwnershipCL    = new HashMap<String,HashMap<String,int[]>>();
    densitySummarySpecialOwnerCL = new HashMap<String,HashMap<String,int[]>>();
  }

  private static ArrayList<ProcessType> doneSummaryProcesses = new ArrayList<ProcessType>();

  public void updateEvuSummaryData() {
   Evu[] units = Simpplle.getCurrentArea().getAllEvu();
   for (int i=0; i<units.length; i++) {
     if (units[i] != null) {
       updateEvuProcessSummary(units[i]);
       updateEvuProcessSummarySpecial(units[i],Reports.OWNERSHIP);
       updateEvuProcessSummarySpecial(units[i],Reports.SPECIAL_AREA);
       updateEvuProcessSummarySpecial(units[i],Reports.OWNER_SPECIAL);

       updateEvuStateSummary(units[i],Reports.NORMAL);
       updateEvuStateSummaryCombineLives(units[i],Reports.NORMAL);

       updateEvuStateSummary(units[i],Reports.OWNERSHIP);
       updateEvuStateSummary(units[i],Reports.SPECIAL_AREA);
       updateEvuStateSummary(units[i],Reports.OWNER_SPECIAL);

       updateEvuStateSummaryCombineLives(units[i],Reports.OWNERSHIP);
       updateEvuStateSummaryCombineLives(units[i],Reports.SPECIAL_AREA);
       updateEvuStateSummaryCombineLives(units[i],Reports.OWNER_SPECIAL);
     }
   }
  }
  private void updateEvuProcessSummary(Evu evu) {
    int cStep = Simulation.getCurrentTimeStep();

    for (Season s : Climate.allSeasons) {
      // Time Step 0 is always YEAR, so skip others.
      if ((cStep == 0) && s != Season.YEAR) {
        continue;
      }
      VegSimStateData trees = evu.getState(cStep, Lifeform.TREES, s);
      VegSimStateData shrubs = evu.getState(cStep, Lifeform.SHRUBS, s);
      VegSimStateData grass = evu.getState(cStep, Lifeform.HERBACIOUS, s);
      VegSimStateData agr = evu.getState(cStep, Lifeform.AGRICULTURE, s);
      VegSimStateData na = evu.getState(cStep, Lifeform.NA, s);

      ProcessType treeProcess = (trees != null) ? trees.getProcess() : ProcessType.NONE;
      ProcessType shrubProcess = (shrubs != null) ? shrubs.getProcess() : ProcessType.NONE;
      ProcessType grassProcess = (grass != null) ? grass.getProcess() : ProcessType.NONE;
      ProcessType agrProcess = (agr != null) ? agr.getProcess() : ProcessType.NONE;
      ProcessType naProcess = (na != null) ? na.getProcess() : ProcessType.NONE;

      int fireCount = 0;
      if (treeProcess.isFireProcess()) {
        fireCount++;
      }
      if (shrubProcess.isFireProcess()) {
        fireCount++;
      }
      if (grassProcess.isFireProcess()) {
        fireCount++;
      }

      if (Simpplle.getCurrentArea().multipleLifeformsEnabled() == false) {
        if (naProcess.isFireProcess()) {
          fireCount++;
        }
      }

      doneSummaryProcesses.clear();
      if (fireCount > 1) {
        ProcessType unitProcess =
                Process.determineUnitFireProcess(treeProcess, shrubProcess,
                        grassProcess);

        if (unitProcess != null) {
          updateSummaryHm(processSummary, null, unitProcess, cStep, evu.getAcres());
        }
        continue;
      }

      Lifeform[] lives = Lifeform.getAllValues();
      for (int i = 0; i < lives.length; i++) {
        ProcessType process = ProcessType.NONE;
        if (lives[i] == Lifeform.TREES) {
          process = treeProcess;
        } else if (lives[i] == Lifeform.SHRUBS) {
          process = shrubProcess;
        } else if (lives[i] == Lifeform.HERBACIOUS) {
          process = grassProcess;
        } else if (lives[i] == Lifeform.AGRICULTURE) {
          process = agrProcess;
        } else if (lives[i] == Lifeform.NA) {
          process = naProcess;
        }

        if ((process.isFireProcess() == false || fireCount == 1) &&
                process != ProcessType.SUCCESSION &&
                process != ProcessType.NONE) {
          if (doneSummaryProcesses.contains(process) == false) {
            updateSummaryHm(processSummary, null, process, cStep, evu.getAcres());
            doneSummaryProcesses.add(process);
          }
        }
      }

    }
  }
  private void updateSummaryHm(HashMap dataHm, String specialKey, Object key, int tStep, int acres)
  {
    HashMap innerDataHm;
    if (specialKey != null) {
      innerDataHm = (HashMap)dataHm.get(specialKey);
      if (innerDataHm == null) {
        if (key instanceof SimpplleType) {
          innerDataHm = new HashMap<SimpplleType, int[]>();
        }
        else {
          innerDataHm = new HashMap<String, int[]>();
        }
        dataHm.put(specialKey,innerDataHm);
      }
    }
    else {
      innerDataHm = dataHm;
    }

    Simulation simulation = Simulation.getInstance();
    int nSteps = 0;
    if (simulation != null) {
      nSteps = simulation.getNumTimeSteps();
    }

    int[] acresData = (int[]) innerDataHm.get(key);
    if (acresData == null) {
      acresData = new int[nSteps + 1];
      for (int k = 0; k <= nSteps; k++) { acresData[k] = 0; }
      innerDataHm.put(key, acresData);
    }
    acresData[tStep] += acres;
  }

  public HashMap getProcessSummaryHm(int reportOption) {
    switch (reportOption) {
      case Reports.OWNERSHIP:
        return processSummaryOwnership;
      case Reports.SPECIAL_AREA:
        return processSummarySpecialArea;
      case Reports.OWNER_SPECIAL:
        return processSummarySpecialOwner;
      case Reports.NORMAL:
        return processSummary;
    }
    return null;
  }

  public HashMap getStateSummaryHm(int reportOption, SimpplleType.Types stateOption, boolean combineLives) {
    switch (reportOption) {
      case Reports.OWNERSHIP:
        switch (stateOption) {
          case SPECIES:
            return (combineLives ? speciesSummaryOwnershipCL : speciesSummaryOwnership);
          case SIZE_CLASS:
            return (combineLives ? sizeClassSummaryOwnershipCL : sizeClassSummaryOwnership);
          case DENSITY:
            return (combineLives ? densitySummaryOwnershipCL : densitySummaryOwnership);
        }
        break;
      case Reports.SPECIAL_AREA:
        switch (stateOption) {
          case SPECIES:
            return (combineLives ? speciesSummarySpecialAreaCL : speciesSummarySpecialArea);
          case SIZE_CLASS:
            return (combineLives ? sizeClassSummarySpecialAreaCL : sizeClassSummarySpecialArea);
          case DENSITY:
            return (combineLives ? densitySummarySpecialAreaCL : densitySummarySpecialArea);
        }
        break;
      case Reports.OWNER_SPECIAL:
        switch (stateOption) {
          case SPECIES:
            return (combineLives ? speciesSummarySpecialOwnerCL : speciesSummarySpecialOwner);
          case SIZE_CLASS:
            return (combineLives ? sizeClassSummarySpecialOwnerCL : sizeClassSummarySpecialOwner);
          case DENSITY:
            return (combineLives ? densitySummarySpecialOwnerCL : densitySummarySpecialOwner);
        }
        break;
      case Reports.NORMAL:
        switch (stateOption) {
          case SPECIES:
            return (combineLives ? speciesSummaryCL : speciesSummary);
          case SIZE_CLASS:
            return (combineLives ? sizeClassSummaryCL : sizeClassSummary);
          case DENSITY:
            return (combineLives ? densitySummaryCL : densitySummary);
        }
        break;
    }
    return null;
  }
  private String determineOwnerSpecialKey(Evu evu, int option) {
    switch (option) {
      case Reports.OWNERSHIP:    return evu.getOwnership();
      case Reports.SPECIAL_AREA: return evu.getSpecialArea();
      case Reports.OWNER_SPECIAL:
        return evu.getOwnership() + "/" + evu.getSpecialArea();
      default:
        return null;
    }
  }

  public void updateEvuProcessSummarySpecial(Evu evu, int specialOption) {
    int cStep = Simulation.getCurrentTimeStep();

    HashMap dataHm;
    String  specialKey = determineOwnerSpecialKey(evu,specialOption);
    if (specialKey == null) { return; }

    switch (specialOption) {
      case Reports.OWNERSHIP:
        dataHm = processSummaryOwnership;
        break;
      case Reports.SPECIAL_AREA:
        dataHm = processSummarySpecialArea;
        break;
      case Reports.OWNER_SPECIAL:
        dataHm = processSummarySpecialOwner;
        break;
      default:
        return;
    }

    VegSimStateData trees  = evu.getState(cStep,Lifeform.TREES);
    VegSimStateData shrubs = evu.getState(cStep,Lifeform.SHRUBS);
    VegSimStateData grass  = evu.getState(cStep,Lifeform.HERBACIOUS);
    VegSimStateData agr    = evu.getState(cStep,Lifeform.AGRICULTURE);
    VegSimStateData na     = evu.getState(cStep,Lifeform.NA);

    ProcessType treeProcess  = (trees != null) ? trees.getProcess() : ProcessType.NONE;
    ProcessType shrubProcess = (shrubs != null) ? shrubs.getProcess() : ProcessType.NONE;
    ProcessType grassProcess = (grass != null) ? grass.getProcess() : ProcessType.NONE;
    ProcessType agrProcess   = (agr != null) ? agr.getProcess() : ProcessType.NONE;
    ProcessType naProcess    = (na != null) ? na.getProcess() : ProcessType.NONE;

    int fireCount = 0;
    if (treeProcess.isFireProcess()) { fireCount++; }
    if (shrubProcess.isFireProcess()) { fireCount++; }
    if (grassProcess.isFireProcess()) { fireCount++; }

    if (Simpplle.getCurrentArea().multipleLifeformsEnabled() == false) {
      if (naProcess.isFireProcess()) { fireCount++; }
    }

    doneSummaryProcesses.clear();
    if (fireCount > 1) {
      ProcessType unitProcess =
        Process.determineUnitFireProcess(treeProcess, shrubProcess, grassProcess);

      if (unitProcess != null) {
        updateSummaryHm(dataHm, specialKey, unitProcess, cStep, evu.getAcres());
      }
    }

    Lifeform[] lives = Lifeform.getAllValues();
    for(int i=0; i<lives.length; i++) {
      ProcessType process = ProcessType.NONE;
      if (lives[i] == Lifeform.TREES) { process = treeProcess; }
      else if (lives[i] == Lifeform.SHRUBS) { process = shrubProcess; }
      else if (lives[i] == Lifeform.HERBACIOUS) { process = grassProcess; }
      else if (lives[i] == Lifeform.AGRICULTURE) { process = agrProcess; }
      else if (lives[i] == Lifeform.NA) { process = naProcess; }

      if ((process.isFireProcess() == false || fireCount == 1) &&
          process != ProcessType.SUCCESSION &&
          process != ProcessType.NONE) {
        if (doneSummaryProcesses.contains(process) == false) {
          updateSummaryHm(dataHm, specialKey, process, cStep, evu.getAcres());
          doneSummaryProcesses.add(process);
        }
      }
    }


  }

//  private void updateEvuStateSummary(Evu evu) {
//    Lifeform[] lives = Lifeform.getAllValues();
//
//    int cStep = Simulation.getCurrentTimeStep();
//
//    for (int l = 0; l < lives.length; l++) {
//      if (evu.hasLifeform(lives[l], cStep) == false) { continue; }
//      VegSimStateData state = evu.getState(cStep, lives[l]);
//      if (state == null) { continue; }
//
//      {
//        SimpplleType key = state.getVeg().getSpecies();
//        updateSummaryHm(speciesSummary, key, cStep, evu.getAcres());
//      }
//      {
//        SimpplleType key = state.getVeg().getSizeClass();
//        updateSummaryHm(sizeClassSummary, key, cStep, evu.getAcres());
//      }
//      {
//        SimpplleType key = state.getVeg().getSpecies();
//        updateSummaryHm(densitySummary, key, cStep, evu.getAcres());
//      }
//    }
//  }

//  private void updateEvuStateSummaryCombineLives(Evu evu) {
//    Lifeform[] lives = Lifeform.getAllValues();
//
//    int cStep = Simulation.getCurrentTimeStep();
//
//    StringBuffer speciesBuf = new StringBuffer("");
//    StringBuffer sizeClassBuf = new StringBuffer("");
//    StringBuffer densityBuf = new StringBuffer("");
//
//    for (int l = 0; l < lives.length; l++) {
//      if (evu.hasLifeform(lives[l], cStep) == false) { continue; }
//
//      VegSimStateData state = evu.getState(cStep, lives[l]);
//      if (state == null) { continue; }
//
//      if (speciesBuf.length() > 0) { speciesBuf.append("-"); }
//      speciesBuf.append(state.getVeg().getSpecies().toString());
//
//      if (sizeClassBuf.length() > 0) { sizeClassBuf.append("-"); }
//      sizeClassBuf.append(state.getVeg().getSizeClass().toString());
//
//      if (densityBuf.length() > 0) { densityBuf.append("-"); }
//      densityBuf.append(state.getVeg().getDensity().toString());
//    }
//
//    updateSummaryHm(speciesSummaryCL,speciesBuf.toString(),cStep,evu.getAcres());
//    updateSummaryHm(sizeClassSummaryCL,sizeClassBuf.toString(),cStep,evu.getAcres());
//    updateSummaryHm(densitySummaryCL,densityBuf.toString(),cStep,evu.getAcres());
//  }

  private void updateEvuStateSummary(Evu evu, int reportOption)
  {
    Lifeform[] lives = Lifeform.getAllValues();
    int cStep = Simulation.getCurrentTimeStep();

    for (int l = 0; l < lives.length; l++) {
      if (evu.hasLifeform(lives[l], cStep) == false) { continue; }

      VegSimStateData state = evu.getState(cStep, lives[l]);
      if (state == null) { continue; }

      String key=null;
      if (reportOption != Reports.NORMAL) {
        key = determineOwnerSpecialKey(evu, reportOption);
      }

      { // Species
        HashMap dataHm = getStateSummaryHm(reportOption,SimpplleType.SPECIES,false);
        updateSummaryHm(dataHm, key, state.getVeg().getSpecies().toString(), cStep, evu.getAcres());
      }
      { // Size Class
        HashMap dataHm = getStateSummaryHm(reportOption,SimpplleType.SIZE_CLASS,false);
        updateSummaryHm(dataHm, key, state.getVeg().getSizeClass().toString(), cStep, evu.getAcres());
      }
      { // Density
        HashMap dataHm = getStateSummaryHm(reportOption,SimpplleType.DENSITY,false);
        updateSummaryHm(dataHm, key, state.getVeg().getDensity().toString(), cStep, evu.getAcres());
      }
    }
  }
  private void updateEvuStateSummaryCombineLives(Evu evu, int reportOption)
  {
    Lifeform[] lives = Lifeform.getAllValues();
    int cStep = Simulation.getCurrentTimeStep();

    StringBuffer speciesBuf = new StringBuffer("");
    StringBuffer sizeClassBuf = new StringBuffer("");
    StringBuffer densityBuf = new StringBuffer("");

    for (int l = 0; l < lives.length; l++) {
      if (evu.hasLifeform(lives[l], cStep) == false) { continue; }

      VegSimStateData state = evu.getState(cStep, lives[l]);
      if (state == null) { continue; }

      if (speciesBuf.length() > 0) { speciesBuf.append("-"); }
      speciesBuf.append(state.getVeg().getSpecies().toString());

      if (sizeClassBuf.length() > 0) { sizeClassBuf.append("-"); }
      sizeClassBuf.append(state.getVeg().getSizeClass().toString());

      if (densityBuf.length() > 0) { densityBuf.append("-"); }
      densityBuf.append(state.getVeg().getDensity().toString());
    }

    String key=null;
    if (reportOption != Reports.NORMAL) {
      key = determineOwnerSpecialKey(evu, reportOption);
    }

    { // Species
      HashMap dataHm = getStateSummaryHm(reportOption,SimpplleType.SPECIES,true);
      updateSummaryHm(dataHm, key, speciesBuf.toString(), cStep, evu.getAcres());
    }
    { // Size Class
      HashMap dataHm = getStateSummaryHm(reportOption,SimpplleType.SIZE_CLASS,true);
      updateSummaryHm(dataHm, key, sizeClassBuf.toString(), cStep, evu.getAcres());
    }
    { // Density
      HashMap dataHm = getStateSummaryHm(reportOption,SimpplleType.DENSITY,true);
      updateSummaryHm(dataHm, key, densityBuf.toString(), cStep, evu.getAcres());
    }
  }

  private static ArrayList<AllStatesReportData> allStatesReportData;

  public static void initializeAllStateReportSummary(File rulesFile) throws SimpplleError {
    allStatesReportData = AllStatesReportData.readFile(rulesFile);
  }
  public static void addAllStateReportRules(File rulesFile) throws SimpplleError {
    if (allStatesReportData == null) {
      allStatesReportData = AllStatesReportData.readFile(rulesFile);
    }
    else {
      ArrayList<AllStatesReportData>
        dataList = AllStatesReportData.readFile(rulesFile);
      allStatesReportData.addAll(dataList);
    }
  }


  public static void updateAllStatesReportSummary() {
    updateAllStatesReportSummary(Simulation.getCurrentTimeStep());
  }
  public static void updateAllStatesReportSummary(int timeStep) {
    for (int j=0; j<allStatesReportData.size(); j++) {
      AllStatesReportData data = allStatesReportData.get(j);
      if (data.isSummaryFinished()) { continue; }

      Evu[] units = Simpplle.getCurrentArea().getAllEvu();
      for (int i=0; i<units.length; i++) {
        if (units[i] != null) {
          data.updateSummary(units[i], timeStep);
        }
      }
      // While the fact that the summarization is finished may seem obvious.
      // It must be remembered that the user can later on, after the simulation
      // finishes, load a new rule file. This new rule will need to be
      // processed (via this method) therefore this ensures that we don't
      // summarized things twice.
      data.setSummaryFinished(timeStep == Simulation.getInstance().getNumTimeSteps());
    }
  }

  public static ArrayList<AllStatesReportData> getAllStatesReportData() {
    return allStatesReportData;
  }

  public static void clearAllStatesReportSummaryData() {
    if (allStatesReportData == null) { return; }

    for (int j=0; j<allStatesReportData.size(); j++) {
      AllStatesReportData data = allStatesReportData.get(j);
      data.clearSummaryData();
    }
  }

  public static TrackingSpeciesReportData trackingSpeciesReportData;

  public static void clearTrackingSpeciesReportSummaryData() {
    TrackingSpeciesReportData.getInstance().clearSummaryData();
  }

  public static void updateTrackingSpeciesReportSummary() {
    updateTrackingSpeciesReportSummary(Simulation.getCurrentTimeStep());
  }
  public static void updateTrackingSpeciesReportSummary(int timeStep) {
    TrackingSpeciesReportData data = TrackingSpeciesReportData.getInstance();

    Evu[] units = Simpplle.getCurrentArea().getAllEvu();
    for (int i=0; i<units.length; i++) {
      if (units[i] != null) {
        data.updateSummary(units[i], timeStep);
      }
    }
  }


}




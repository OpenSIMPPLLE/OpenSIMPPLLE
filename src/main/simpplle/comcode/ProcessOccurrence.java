/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import org.hibernate.Session;

import java.io.*;

/**
 * This class is the base class for ProcessOccurrenceSpreading and ProcessOccurrenceSpreadingFire.
 *
 * <p>It is necessary to know all the units in an event, and that can be achieved
 * via a simply ArrayList.
 * The from to should be stored in a way that can be used in a database:
 * The key would be the from unit and the value would be the to id's in
 * the form of a string (e.g.  25:30:50)
 * It isn't necessary to store the type of fire either as that can be
 * obtained from the unit, need to check if the type can't change during
 * a time step.
 * <p>Also the fact whether a fire is spotting can either be stored along with
 * the from or add a new fire prob category to the Evu (i.e like Evu.SFS)
 * meaning spread via fire spotting.
 * If this plan turns out to be achievable it should dramatically reduce
 * memory usage to store the spread data.
 * Also should add a parent class to this one (ProcessOccurrence) as fire
 * is not the only process that spread and has information store in the
 * AreaSummary class.
 *
 *
 * <p>Perhaps storing events as a graph.  Each node would store unit, process,
 * process prob info, a link to the from node and an arrayList storing
 * links to the to nodes.  This would be best with a with the unit
 * being the key for quick lookup of a given node.
 *
 * <p>The collection of events will be stored in a simple arrayList
 * as the number would be small enough that searching for a given unit
 * would not be a significant performance penalty.
 *
 * <p>There will be envision three classes, the base class is ProcessOccurrence, is
 * subclass would be SpreadingProcessOccurrence, and its subclass would
 * be FireSpreadingProcessOccurrence.
 *
 * <p>In the areaSummary class, which would hold the top level variables,
 * we would have an array with unit id as index for simple non-spreading
 * process events since every unit will start that way.  Of course spreading
 * origin units would appear in two places, but that should be just fine, the
 * idea is to achieve a record of all that happened to a unit as it went
 * thru a time step.  The other process events can be stored as an arrayList, as a HashMap is not needed since 
 * there will be very few of them.  Even with fire there are generally no
 * more than a few dozen events, which is small enough that the overhead
 * of a HashMap is too much.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ProcessOccurrence implements Externalizable {
  static final long serialVersionUID = 5069102844547741053L;
  static final int  version          = 3;

  protected Evu            unit;
  protected ProcessType    process;
  protected int            processProb;
  protected Season season;
  protected Lifeform       lifeform;

//  public void destoryMe() {
//    unit = null;
//    process = null;
//    season  = null;
//  }

  /**
   * Constructor for ProcessOccurrence.  Initializes the units involved in an event and the type
   * of event (process), sets the process probability to 0,
   * and the season to YEAR, which is a default starting point for season.  
   */
  public ProcessOccurrence() {
    unit        = null;
    process     = null;
    processProb = 0;
    season      = Season.YEAR;
  }
  /**
   * overloaded constructor.  Sets the evu, lifeform, process probability, season, and time step.  
   * @param evu the unit involved in the event
   * @param lifeform 
   * @param processData probability data for the process
   * @param timeStep 
   */
  public ProcessOccurrence(Evu evu, Lifeform lifeform, ProcessProbability processData, int timeStep) {
    this.unit         = evu;
    this.process      = processData.processType;
    this.processProb  = processData.probability;
    this.season       = Simpplle.getCurrentSimulation().getCurrentSeason();
    this.lifeform     = lifeform;
  }
/**
 * gets unit involved in event
 * @return the EVU
 */
  public Evu getUnit() {
    return unit;
  }
/**
 * This class designates the unit the event originated in.  
 * @param id the id of originating unit
 * @return true if EVU id is originating unit ID
 */
  public boolean isOriginUnit(int id) {
    if (unit == null) { return false; }
    return (getUnit().getId() == id);
  }
  /**
   * overloaded isOriginUnit function, takes as argument EVU instead of id.  
   * @param evu
   * @return true if EVU is originating unit
   */
  public boolean isOriginUnit(Evu evu) {
    return isOriginUnit(evu.getId());
  }
/**
 * 
 * @return the process (event)
 */
  public ProcessType getProcess() { return process; }
/**
 * Choices for season are Spring, Summer, Fall, Winter, and Year
 * @return the current season
 */
  public Season getSeason() {
    return season;
  }

  /**
   * gets the lifeform
   * @return the lifeform
   */
  public Lifeform getLifeform() {
    return lifeform;
  }

  public int getProcessProbability() { return processProb; }
/**
 * reads in from external source, information pertinent to this class are: unit, process, process probability, season, lifeform
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    unit        = Simpplle.getCurrentArea().getEvu(in.readInt());
    process     = (ProcessType)in.readObject();
    processProb = in.readInt();
    season      = Season.YEAR;
    lifeform    = Lifeform.NA;

    if (version == 2) {
      season = Season.valueOf((String) in.readObject());
    }
    else if (version > 2) {
      season = Season.valueOf((String) in.readObject());
      lifeform = Lifeform.get((String)in.readObject());
    }
  }
  /**
   * Writes to external source those objects of concern in Process Occurrence.  
   * <p>This are in order, version, unitID, process, process probability, season, lifeform
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeInt(unit.getId());
    out.writeObject(process);
    out.writeInt(processProb);
    out.writeObject(season.toString());
    out.writeObject(lifeform.toString());
  }
/**
 * sets the season for process occurrence
 * @param season choices are Spring, Fall, Summer, Winter, or Yea
 */
  public void setSeason(Season season) {
    this.season = season;
  }

  public void setLifeform(Lifeform lifeform) {
    this.lifeform = lifeform;
  }
/**
 * writes the event to a database, uses hibernate.  
 * @param session hibernate session
 * @param run 
 * @param timeStep
 * @throws SimpplleError caught in GUI
 */
  public void writeEventDatabase(Session session, int run, int timeStep)
      throws SimpplleError
  {
      // ** Work Section **
      AreaSummaryDataNew data = new AreaSummaryDataNew();
      data.setRun((short)run);
      data.setTimeStep((short)timeStep);
      data.setOriginUnitId(unit.getId());
      data.setUnitId(unit.getId());
      data.setProcess(process);
      data.setRationalProb((short)processProb);
      data.setSeason(season);

      data.setFmz(unit.getFmz());
      data.setSpecialArea(SpecialArea.get(unit.getSpecialArea(),true));
      data.setOwnership(Ownership.get(unit.getOwnership(),true));
      data.setGroup(unit.getHabitatTypeGroup().getType());
      data.setRationalAcres(unit.getAcres());
      data.setToUnitId(-1);

      AreaSummaryDataNew.writeDatabase(session,data);
      // ** End Work **
  }

  /**
   * Write process events to a file.
   * <p> Fields:
   * <ul>"RUN,TIMESTEP,ORIGINUNITID,UNITID,TOUNITID,PROCESS_ID,PROB,ACRES,SEASON_ID,GROUP_ID,OWNERSHIP_ID,SPECIAL_AREA_ID,FMZ_ID"</ul>
   * </p>
   * @param fout Print writer open to file
   * @param run int
   * @param timeStep int
   */
  public void writeEventAccessFiles(PrintWriter fout, int run, int timeStep)
  throws SimpplleError
  {
    Simulation sim = Simulation.getInstance();

    // Add attributes to lookup files
    sim.addAccessEcoGroup(unit.getHabitatTypeGroup().getType());
    sim.addAccessOwnership(Ownership.get(unit.getOwnership(),true));
    sim.addAccessSpecialArea(SpecialArea.get(unit.getSpecialArea(),true));
    sim.addAccessFmz(unit.getFmz());
    sim.addAccessProcess(process);

    int processId = process.getSimId();
    // skip printing of succession to reduce file size
    if(process == ProcessType.SUCCESSION){
      return;
    }

    // get ids for all attributes
    int rootId    = unit.getId();
    int toId      = -1;
    int seasonId  = season.ordinal();
    int groupId   = unit.getHabitatTypeGroup().getType().getSimId();
    int ownerId   = Ownership.get(unit.getOwnership(),true).getSimId();
    int specialId = SpecialArea.get(unit.getSpecialArea(),true).getSimId();
    int fmzId     = unit.getFmz().getSimId();
    float acres   = unit.getFloatAcres();
    float fProb    = (processProb < 0) ? processProb : ( (float)processProb / (float)Utility.pow(10,Area.getAcresPrecision()) );

    // print to file
    fout.printf("%d,%d,%d,%d,%d,",run,timeStep,rootId,rootId,toId);
    fout.printf("%d,%.1f,%.1f,%d,%d,%d,%d,%d%n", processId,fProb,acres,seasonId,groupId,ownerId,specialId,fmzId);
  }

}



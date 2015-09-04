package simpplle.comcode;

import org.hibernate.*;
import java.sql.*;
import simpplle.comcode.Climate.*;

/**

 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * <p>This is the new area summary data class which replaces the old Area SummaryData class deprecated in V2.5
 * This class contains getters and setters for 
 * <li> season
 * <li> acres
 * <li> unit id
 * <li>process
 * <li> group
 * <li> ownership
 * <li> rational probablility
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @since V2.5
 *
 * 
 */
public class AreaSummaryDataNew {
  private long                 id;
  private int                  originUnitId;
  private int                  unitId;
  private int                  toUnitId;
  private ProcessType          process;
  private short                rationalProb;
  private int                  rationalAcres;
  private Season               season;
  private short                seasonOrd;
  private short                timeStep;
  private short                run;
  private HabitatTypeGroupType group;
  private Ownership            ownership;
  private SpecialArea          specialArea;
  private Fmz                  fmz;

  private static int writeCount=0;

  /**
   * default constructor 
   */
  public AreaSummaryDataNew() {}

  /**
   * 
   * @param data Area summary data used to initialize variables 
   */
  public AreaSummaryDataNew(AreaSummaryDataNew data) {
    originUnitId  = data.originUnitId;
    unitId        = data.unitId;
    toUnitId      = data.toUnitId;
    process       = data.process;
    rationalProb          = data.rationalProb;
    rationalAcres         = data.rationalAcres;
    season        = data.season;
    timeStep      = data.timeStep;
    run           = data.run;
    group         = data.group;
    ownership     = data.ownership;
    specialArea   = data.specialArea;
    fmz           = data.fmz;

  }

  /**
   * Season choice is spring, summer, fall, winter, and year
   * @return ordinal into the season switch 
   */
  public short getSeasonOrd() {
    return (short)season.ordinal();
  }
  
  /**
   * 
   * @param ordinal 
   */
  public void setSeasonOrd(short ordinal) {
    switch (ordinal) {
      case 0: season = Season.SPRING; break;
      case 1: season = Season.SUMMER; break;
      case 2: season = Season.FALL;   break;
      case 3: season = Season.WINTER; break;
      case 4: season = Season.YEAR;   break;
    }
  }
/**
 * Sets the area summary Id.
 * @param id area summary Id
 */
  public void setId(long id) {
    this.id = id;
  }
/**
 * Sets the rational acreage of area
 * @param acres
 */
  public void setRationalAcres(int acres) { this.rationalAcres = acres; }
/**
 * Sets the Evu Id.
 * @param unitId
 */
  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }
/**
 * Sets the Evu destination Id
 * @param toUnitId
 */
  public void setToUnitId(int toUnitId) {
    this.toUnitId = toUnitId;
  }
/**
 * Sets the time step
 * @param timeStep
 */
  public void setTimeStep(short timeStep) {
    this.timeStep = timeStep;
  }
/**
 * Sets the special area.  
 * @param specialArea
 */
  public void setSpecialArea(SpecialArea specialArea) {
    this.specialArea = SpecialArea.get(specialArea.getName(),true);
  }
/**
 * Sets the season for this area summary. 
 * @param season
 */
  public void setSeason(Season season) {
    this.season = season;
  }
/**
 * Sets the run
 * @param run
 */
  public void setRun(short run) {
    this.run = run;
  }
/**
 * Sets the process
 * @param process
 */
  public void setProcess(ProcessType process) {
    this.process = ProcessType.get(process.toString(),true);
  }
/**
 * Sets the rational probability
 * @param prob
 */
  public void setRationalProb(short prob) {
    this.rationalProb = prob;
  }
/**
 * Sets the ownership.  
 * @param ownership
 */
  public void setOwnership(Ownership ownership) {
    this.ownership = Ownership.get(ownership.getName(),true);
  }
/**
 * Sets originating Evu Id.
 * @param originUnitId
 */
  public void setOriginUnitId(int originUnitId) {
    this.originUnitId = originUnitId;
  }
/**
 * Sets the habitat type group type.  
 * @param group
 */
  public void setGroup(HabitatTypeGroupType group) {
    this.group = HabitatTypeGroupType.get(group.getName(),true);
  }

  /**
   * sets fire management zone
   * @param fmz the fire management zone to be set
   */
  public void setFmz(Fmz fmz) {
    this.fmz = Simpplle.getCurrentZone().getFmz(fmz.getName());
  }
/**
 * Gets the area  summary Id
 * @return
 */
  public long getId() {
    return id;
  }

  /**
   * Gets the area rational acres
   * @return
   */
  public int getRationalAcres() {
    return rationalAcres;
  }
/**
 * Gets the fire management zone
 * @return
 */
  public Fmz getFmz() {
    return fmz;
  }
/**
 * Gets the habitat type group type.
 * @return
 */
  public HabitatTypeGroupType getGroup() {
    return group;
  }
/**
 * Gets the originting Evu
 * @return
 */
  public int getOriginUnitId() {
    return originUnitId;
  }
/**
 * Gets the ownership
 * @return
 */
  public Ownership getOwnership() {
    return ownership;
  }
/**
 * Gets the rational probability.
 * @return
 */
  public short getRationalProb() {
    return rationalProb;
  }
/**
 * Gets the process
 * @return
 */
  public ProcessType getProcess() {
    return process;
  }
/**
 * Gets the run
 * @return
 */
  public short getRun() {
    return run;
  }
/**
 * Gets the special area
 * @return
 */
  public SpecialArea getSpecialArea() {
    return specialArea;
  }
/**
 * Gets the time step
 * @return
 */
  public short getTimeStep() {
    return timeStep;
  }

  public int getToUnitId() {
    return toUnitId;
  }
/**
 * Gets the Evu Id
 * @return
 */
  public int getUnitId() {
    return unitId;
  }

  public static void clearWriteCount() {
    writeCount=0;
  }
  
  /**
   * method to export data to database using hibernate
   * @param session current hibernate session
   * @param data the area summary data to be exported
   * @throws SimpplleError if a hibernate exception error occurs throws a simppleeError which will be caught by the gui 
   * 
   */
  public static void writeDatabase(Session session, AreaSummaryDataNew data)
    throws SimpplleError
  {
    try {
      session.saveOrUpdate(data.process);
      session.saveOrUpdate(data.group);
      session.saveOrUpdate(data.ownership);
      session.saveOrUpdate(data.specialArea);
      session.saveOrUpdate(data.fmz);


      session.saveOrUpdate(data);
      writeCount++;

      if (writeCount % 30 == 0) {
        session.flush();
        session.clear();
      }
    }
    catch (HibernateException ex) {
      throw new SimpplleError("Problems writing Area Summary Data to database", ex);
    }
  }


}



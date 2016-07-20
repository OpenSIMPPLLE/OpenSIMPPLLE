/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.sql.*;
import java.util.*;

import org.hibernate.*;

/**
 * This class contains getters and setters for accumulating data to be written to database.
 * Uses the object-relational mapping open source software Hibernate.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @link http://www.hibernate.org/
 */

public class AccumData {
  private long id;
  private HabitatTypeGroupType group;
  private int slink;
  private int rationalAcres;
  private int timeStep;
  private int run;
  private Lifeform lifeform;
  private String season;
  private VegetativeType vegType;
  private ProcessType process;
  private short rationalProb;
  private int prob;
  private String probStr;
  private Map trkSpecies;
  private float acres;

  /**
   * Constructor must be without arguments to fulfill Hibernate requirements.  
   */
  public AccumData() {
  }

  public void setId(long id) {
    this.id = id;
  }
/**
 * Sets the time step
 * @param timeStep
 */
  public void setTimeStep(int timeStep) {
    this.timeStep = timeStep;
  }
/**
 * Sets the run of this Acummulate data
 * @param run
 */
  public void setRun(int run) {
    this.run = run;
  }
/**
 * Sets the lifeform for this AcumData
 * @param lifeform
 */
  public void setLifeform(Lifeform lifeform) {
    this.lifeform = lifeform;
  }
/**
 * Sets teh vegetative type.
 * @param vegType the vegetative type
 */
  public void setVegType(VegetativeType vegType) {
    this.vegType = vegType;
  }
/**
 * Sets the process.
 * @param process
 */
  public void setProcess(ProcessType process) {
    this.process = process;
  }
/**
 * Sets the rational probability
 * @param rationalProb
 */
  public void setRationalProb(short rationalProb) {
    this.rationalProb = rationalProb;
  }
/**
 * Sets the tracking species.  
 * @param trkSpecies
 */
  public void setTrkSpecies(Map trkSpecies) {
    this.trkSpecies = trkSpecies;
  }
/**
 * Sets the season (choices are spring, summer, fall, winter, year
 * @param season
 */
  public void setSeason(String season) {
    this.season = season;
  }
/**
 * Sets teh rational acres.  
 * @param rationalAcres
 */
  public void setRationalAcres(int rationalAcres) {

    this.rationalAcres = rationalAcres;
  }
/**
 * Sets the slink, which is the link between GIS and Simpplle
 * @param slink
 */
  public void setSlink(int slink) {
    this.slink = slink;
  }
/**
 * Sets the string version of probability
 * @param probStr string version of probabilty
 */
  public void setProbStr(String probStr) {
    this.probStr = probStr;
  }
/**
 * Sets the float acres.  
 * @param acres
 */
  public void setAcres(float acres) {
    this.acres = acres;
  }
/**
 * Set the probability
 * @param prob
 */
  public void setProb(int prob) {
    this.prob = prob;
  }
/**
 * Sets the Habitat type group type.  
 * @param group
 */
  public void setGroup(HabitatTypeGroupType group) {
    this.group = group;
  }
/**
 * Gets Id
 * @return
 */
  public long getId() {
    return id;
  }
/**
 * gets the time step
 * @return time step
 */
  public int getTimeStep() {
    return timeStep;
  }
/**
 * Gets a particular run
 * @return run
 */
  public int getRun() {
    return run;
  }
/**
 * Gets the lifeform.  Choices are Trees, Shrubs, Herbacious, Agriculture, or NA (no classification)
 * @return the lifeform 
 */
  public Lifeform getLifeform() {
    return lifeform;
  }
/**
 * Gets the vegetative type.
 * @return the vegetative type
 */
  public VegetativeType getVegType() {
    return vegType;
  }
/**
 * Gets the process.
 * @return the process
 */
  public ProcessType getProcess() {
    return process;
  }
/**
 * Gets the rational probability.
 * @return the rational probability
 */
  public short getRationalProb() {
    return rationalProb;
  }
/**
 * Gets the Tracking Species
 * @return
 */
  public Map getTrkSpecies() {
    return trkSpecies;
  }
/**
 * Gets the Season (choices for this are spring, summer, fall, winter, or year)
 * @return
 */
  public String getSeason() {
    return season;
  }
/**
 * Gets rational acres
 * @return the rational acres.  
 */
  public int getRationalAcres() {

    return rationalAcres;
  }
/**
 * This is the link between GIS and Simpplle.  It contains an Evu Id
 * @return the evu Id
 */
  public int getSlink() {
    return slink;
  }
/**
 * Gets the string version of probability.
 * @return
 */
  public String getProbStr() {
    return probStr;
  }
/**
 * Get acres as a float.  Note: this is stored in OpenSimpplle as an int)
 * @return acres as a float
 */
  public float getAcres() {
    return acres;
  }
/**
 * Gets the probability as an int.  
 * @return
 */
  public int getProb() {
    return prob;
  }
/**
 * Habitat type group type.  
 * @return habitat type group type
 */
  public HabitatTypeGroupType getGroup() {
    return group;
  }

  /**
   * Method to interact with database using Hibernate.  
   * @param session hibernate session
   * @param evu evaluated vegetation unit
   * @param run 
   * @param data contains an arrayList of hashmaps mapping Lifeforms to ArrayList of Vegetative Simulations State data
   * @throws HibernateException
   * @throws SQLException
   */
  public static void writeDatabase(Session session, Evu evu, int run,
                                   ArrayList<HashMap<Lifeform,ArrayList<VegSimStateData>>> data)
      throws HibernateException, SQLException
  {
    int ts=0;
    for (HashMap<Lifeform,ArrayList<VegSimStateData>> item : data) {
      for (Lifeform lifeform : item.keySet()) {
        ArrayList<VegSimStateData> simStateData = item.get(lifeform);
        for (VegSimStateData state : simStateData) {
          AccumData accumData = new AccumData();

          accumData.setSlink(evu.getId());
          accumData.setGroup(evu.getHabitatTypeGroup().getType());
          accumData.setRationalAcres(evu.getAcres());
          accumData.setAcres(evu.getFloatAcres());
          accumData.setTimeStep(ts);
          accumData.setRun(run);
          accumData.setLifeform(lifeform);
          accumData.setSeason(state.getSeasonString());
          accumData.setVegType(state.getVegType());
          accumData.setProcess(state.getProcess());
          accumData.setRationalProb((short)state.getProb());
          accumData.setProb(Math.round(state.getFloatProb()));
          accumData.setProbStr(state.getProbString());
          accumData.setTrkSpecies(state.getAccumDataSpeciesMap());

          session.save(accumData);
        }
      }
      ts++;
    }

  }
}

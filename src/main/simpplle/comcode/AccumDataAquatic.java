/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import org.hibernate.Transaction;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

/**
 * This class contains getters and setters for aquatic  data to be written to database.
 * Uses the object-relational mapping open-source library Hibernate.
 *
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 *
 * @link http://www.hibernate.org/ 
 */

public class AccumDataAquatic {
  private long id;
  private int slink;
  private int rationalLength;
  private int timeStep;
  private int run;
  private PotentialAquaticState state;
  private ProcessType process;
  private short rationalProb;
  private int prob;
  private String probStr;
  private float length;

  /**
   * Constructor must be without arguments to fulfill Hibernate requirements.  
   */
  public AccumDataAquatic() {
  }
/**
 * Get Id of Eau
 * @return Id Eau Id
 */
  public long getId() {
    return id;
  }
/**
 * Get length of Eau
 * @return
 */
  public float getLength() {
    return length;
  }
/**
 * Gets probabiliy.
 * @return probability
 */
  public int getProb() {
    return prob;
  }
  /**
   * Gets the string version of probability.
   * @return probability as a string
   */
  public String getProbStr() {
    return probStr;
  }
/**
 * Gets the process
 * @return process
 */
  public ProcessType getProcess() {
    return process;
  }
/**
 * Gets the rational length of Eau. 
 * @return the rational length of aquatic element
 */
  public int getRationalLength() {
    return rationalLength;
  }
/**
 * Gets the rational probability
 * @return rational version of probability
 */
  public short getRationalProb() {
    return rationalProb;
  }
/**
 * Gets the simulation run.
 * @return simulation run
 */
  public int getRun() {
    return run;
  }
/**
 * Gets the slink, which is the simpplle link to GIS
 * @return 
 */
  public int getSlink() {
    return slink;
  }
/**
 * Gets the potential aquatic state.  
 * @return potential aquatic state
 */
  public PotentialAquaticState getState() {
    return state;
  }
/**
 * Gets the time step
 * @return the time step
 */
  public int getTimeStep() {
    return timeStep;
  }
/**
 * Sets the time step
 * @param timeStep
 */
  public void setTimeStep(int timeStep) {
    this.timeStep = timeStep;
  }
/**
 * Sets the potential aquatic state.  
 * @param state the potential aquatic state
 */
  public void setState(PotentialAquaticState state) {
    this.state = state;
  }
/**
 * Sets the slink
 * @param slink 
 */
  public void setSlink(int slink) {
    this.slink = slink;
  }
/**
 * Sets the simulation run
 * @param run simulation run
 */
  public void setRun(int run) {
    this.run = run;
  }
/**
 * Sets the rational probability
 * @param rationalProb rational probability
 */
  public void setRationalProb(short rationalProb) {
    this.rationalProb = rationalProb;
  }
/**
 * Sets the rational length of the aquatic element.   
 * @param rationalLength the length of the aquatic element
 */
  public void setRationalLength(int rationalLength) {
    this.rationalLength = rationalLength;
  }
/**
 * Sets the process
 * @param process 
 */
  public void setProcess(ProcessType process) {
    this.process = process;
  }
/**
 * Sets the string version or probability.
 * @param probStr string version or probability
 */
  public void setProbStr(String probStr) {
    this.probStr = probStr;
  }
/**
 * Sets the probability,
 * @param prob probability,
 */
  public void setProb(int prob) {
    this.prob = prob;
  }
/**
 * Sets the float length of aquatic element.  
 * @param length float length of aquatic element
 */
  public void setLength(float length) {
    this.length = length;
  }
/**
 * Sets the Eau Id.  
 * @param id Eau Id
 */
  public void setId(long id) {
    this.id = id;
  }


}

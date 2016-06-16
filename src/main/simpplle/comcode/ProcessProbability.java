package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class contains methods for a Process Probability. 
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */
public class ProcessProbability {
  public ProcessType processType;
  public int         probability;
/**
 * Primary constructor.  Initializes the process type and probability.    
 * @param processType
 * @param prob
 */
  public ProcessProbability(ProcessType processType, int prob) {
    this.processType = processType;
    this.probability = prob;
  }
  /**
   * Overloaded constructor.  References the primary constructor, sets the process type and initializes the probability to 0.   
   * @param processType
   */
  public ProcessProbability(ProcessType processType) {
    this(processType,0);
  }
}

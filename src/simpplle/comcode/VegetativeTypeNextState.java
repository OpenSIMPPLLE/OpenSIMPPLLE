/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/** 
 * This class contains the next state of a vegetative type.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class VegetativeTypeNextState {
  private Process        process;
  private VegetativeType nextState;
  private int            probability;

  public VegetativeTypeNextState(Process process, VegetativeType nextState, int probability) {
    this.process     = process;
    this.nextState   = nextState;
    this.probability = probability;
  }

  public Process getProcess() { return process; }

  public VegetativeType getNextState() { return nextState; }

  public int getProbability() { return probability; }

  public String toString() {
    return (nextState.toString() + " -- " + process.toString());
  }
}

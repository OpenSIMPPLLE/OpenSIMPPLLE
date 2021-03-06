/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class is used to hold spreading information when process
 *  spreading is run during a simulation.
 *   (i.e. from-unit origin-id origin-process)
 *   
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class SpreadData {

  public Evu     evu;
  public int     originId;
  public Process originProcess;

  public SpreadData() {}

  public SpreadData(Evu evu, int originId, Process originProcess) {
    this.evu           = evu;
    this.originId      = originId;
    this.originProcess = originProcess;
  }
}

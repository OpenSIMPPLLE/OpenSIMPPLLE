package simpplle.comcode.process;

import simpplle.comcode.logic.BaseLogic;
import simpplle.comcode.Process;
import simpplle.comcode.logic.ProcessProbLogic;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Low Mortality Prairie Dog, a type of Disturbance Process.  
 *  <p>Low Mortality Prairie Dog is used in Colorado Front Range. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 */


public class LMPrairieDog extends Process {
  private static final String printName = "LM-PD";
  /**
   * Constructor.  Inherits from Process superclass and initializes description, spreading to false, 
   * and the default visible columns.
   */
  public LMPrairieDog() {
    super();
    description = "Low Mortality Prairie Dog";

    spreading = false;

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
  /**
   * outputs  "LM-PD"
   */
  public String toString () {
    return printName;
  }
}

package simpplle.comcode.process;

import simpplle.comcode.logic.BaseLogic;
import simpplle.comcode.Process;
import simpplle.comcode.logic.ProcessProbLogic;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods for Black Stain Root Disease a type of process
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.Process
 */

public class BlackStainRD extends Process {
  private static final String printName = "BLACK-STAIN-RD";
  
  /**
   * Constructor for Black stain root disease.  The default visible columns are ROW_COL and PROB_COL.
   */
  public BlackStainRD() {
    super();
    description = "Black Stain Root Disease";

    spreading = false;

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
  
  /**
   * returns "BLACK-STAIN-RD"
   */
  public String toString () {
    return printName;
  }
}

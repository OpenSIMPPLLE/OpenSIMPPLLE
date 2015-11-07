package simpplle.comcode.process;


import simpplle.comcode.logic.BaseLogic;
import simpplle.comcode.Process;
import simpplle.comcode.logic.ProcessProbLogic;

/** The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Elk Browsing, a type of Disturbance Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 * @see simpplle.comcode.Process
 */

public class ElkHerbivory extends Process {
  private static final String printName = "ELK-HERBIVORY";
 /**
  * Elk Herbivory constructor.  Initializes some variables inherited from Process superclass.
  * 
  */
  public ElkHerbivory() {
    super();

    spreading = false;
    description = "Elk Herbivory";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  /**
   * outputs "ELK-HERBIVORY"
   */
  public String toString () {
    return printName;
  }
}

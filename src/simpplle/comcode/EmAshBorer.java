/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/** 
 * This class contains methods for Emerald Ash Borer, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class EmAshBorer extends Process {
  private static final String printName = "EM-ASH-BORER";
  
  /**
  * Constructor for Emerald Ash Borer.  Initializes methods from Process superclass.
   */
  public EmAshBorer() {
    super();

    spreading   = false;
    description = "Em Ash Borer";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

}

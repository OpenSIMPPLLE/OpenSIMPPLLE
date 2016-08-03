/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class defines methods for Pinyon Juniper Decline, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */

public class PJDecline extends Process {
  private static final String printName = "PJ-DECLINE";
  /**
   * Constructor for Pinyon Juniper Decline.  
   * Inherits from Process superclass and initializes spreading to false and sets default visible columns.
   * 
   */
  public PJDecline() {
    super();

    spreading   = false;
    description = "Pinyon Juniper Decline";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
  /**
   * outputs "PJ-DECLINE"
   */
  public String toString () {
    return printName;
  }
}

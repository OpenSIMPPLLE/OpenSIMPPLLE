package simpplle.comcode.process;


import simpplle.comcode.Process;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-001.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class is for Beech Bark Disease, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @since SIMPPLLE V2.3
 * 
 *   
 */

public class BbDisease extends Process {
  private static final String printName = "BB-DISEASE";
  
  /**
   * Constructor for Beech Bark Disease.  Sets spreading to true and sets its description.   
   */
  public BbDisease() {
    super();

    spreading = true;
    description = "BEECH BARK DISEASE";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * If the Bark Beetles are from an adjacent Evu that is downwind spread returns true, otherwise false.  
 */
  protected boolean doSpread (simpplle.comcode.zone.ColoradoFrontRange zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {

    return fromEvu.isAdjDownwind(evu);
  }

}

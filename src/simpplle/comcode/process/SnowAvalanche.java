package simpplle.comcode.process;

import simpplle.comcode.Process;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines methods for Snow Avalanche, a type of Process.  This process is not tracked in many of the zones, 
 * therefore returns 0 for probability and false for spread.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *@see simpplle.comcode.Process
 *
 */

public class SnowAvalanche extends Process {
  private static final String printName = "SNOW-AVALANCHE";
  /**
   * Constructor for snow avalanche.  Inherits from process superclass and initializes spreading to false, and visible columns for row and probability.  
   * 
   */
  public SnowAvalanche() {
    super();

    spreading   = false;
    description = "Snow Avalanche";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }

 /**
  * Snow Avalanche is not tracked in Westside Region 1 therefore returns probability of 0.
  */
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu)  { return 0; }
  /**
   * Snow Avalanche is not tracked in Eastside Region 1 therefore returns probability of 0.
   */
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu)  { return 0; }
  /**
   * Snow Avalanche is not tracked in Sierra Nevada therefore returns probability of 0.
   */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu)       { return 0; }
  /**
   * Snow Avalanche is not tracked in Southern California therefore returns probability of 0.
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) { return 0; }
  /**
   * Snow Avalanche is not tracked in Gila therefore returns probability of 0.
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu)               { return 0; }

  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) {
    return doProbability(evu);
  }

  /**
   * Snow Avalanche is not tracked in Westside Region 1 therefore returns spread of false.   
   */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)  { return false; }
  /**
   *  Snow Avalanche is not tracked in Eastside Region 1 therefore returns spread of false. 
   */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)  { return false; }
  /**
   *  Snow Avalanche is not tracked in Sierra Nevada therefore returns spread of false. 
   */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)       { return false; }
  /**
   *  Snow Avalanche is not tracked in Southern California therefore returns spread of false. 
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }
  /**
   *  Snow Avalanche is not tracked in Gila therefore returns spread of false. 
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu)               { return false; }

  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }
/**
 * outputs "SNOW-AVALANCHE"
 */
  public String toString () {
    return printName;
  }

}

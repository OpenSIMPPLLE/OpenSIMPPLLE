

package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;


/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class is for Bark Beetle Root Disease in Dwarf Mistletoe Complex, a type of Process.  
 * It is done for the Southern California and Sierra Nevada regional zones.  
 * Probability method is common to both zones, spread is false for both.  
 * All others return default values of 0 for probability and false for spread.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *  
 * @see simpplle.comcode.Process
 */


public class BbRdDmComplex extends Process {
  private static final String printName = "BB-RD-DM-COMPLEX";
  /**
   * Constructor - inherits from Process superclass 
   */
  public BbRdDmComplex() {
    super();

    spreading   = false;
    description = "Bark Beetle Root Disease Dwarf Mistletoe Complex";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * Does the probability for Bark Beetle Root disease Dwarf Mistletoe complex.  
 * Factors figuring into the probability are current evu state, species, size class, and treatment type
 * 
 * @param zone the regional zone, choices are:
 * <li> Sierra Nevada
 * <li> Southern California
 * @param evu existing vegetation unit
 * @return probability data from via probability formula common to both Southern California and Sierra Nevada zones.
 */
  public int doProbabilityCommon(RegionalZone zone, simpplle.comcode.element.Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    int       cTime     = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    int       page      = -1, row = -1, col = 0;

    Treatment     treat = evu.getTreatment(cTime - 1);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    if ((species == Species.PP || species == Species.MC_PP) &&
        (sizeClass == SizeClass.MTS  || sizeClass == SizeClass.MMU ||
         sizeClass == SizeClass.LTS  || sizeClass == SizeClass.LMU ||
         sizeClass == SizeClass.VLMU || sizeClass == SizeClass.VLTS)) {
      page = 0;
      row  = 0;
    }
    else {
      return 0;
    }

    if (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
        treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
        treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN ||
        treatType == TreatmentType.NONE) {
      col++;
    }

    return getProbData(page,row,col);
  }

 /**
  *"Bark Beetle Root Disease Dwarf Mistletoe Complex is not done in Westside Region 1, so returns 0 
  */
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  /**
   *"Bark Beetle Root Disease Dwarf Mistletoe Complex is not done in Eastside Region 1, so returns 0 
   */
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
/**
 * Calculates the probability for Sierra Nevada zone by calling doProbabilityCommon
 */
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(zone,evu);
  }
  /**
   * Calculates the probability for Southern California zone by calling doProbabilityCommon
   */
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  /**
   *"Bark Beetle Root Disease Dwarf Mistletoe Complex does not occur in Gila, so returns 0 
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu) { return 0; }
  
  /**
   *"Bark Beetle Root Disease Dwarf Mistletoe Complex is not calculated in South Central Alaska, so returns 0 
   */
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) { return 0; }
/**
 * Invoked by both Southern California and Sierra Nevada zones.
 * @return false - meaning no spread
 */
  private boolean doSpreadCommon () {
    return false;
  }

  /**
   *Returns false 
   */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }

  /**
   *Returns false
   */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }
/**
 * Returns false.  
 */
  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }
  /**
   * Returns false.  
   */
  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }

  /**
   *Returns false
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }
  /**
   *Returns false
   */
  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }

  
  /**
   * outputs "BB-RD-DM-COMPLEX"
   */
  public String toString () {
    return printName;
  }
}

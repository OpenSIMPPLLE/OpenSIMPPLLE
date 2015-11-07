package simpplle.comcode.process;


import simpplle.comcode.*;
import simpplle.comcode.Process;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Moderate Bison Grazing, a type of Process. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */

public class ModerateBisonGrazing extends Process {
 /**
  * Constructor for Moderate Bison Grazing.  
  * Inherits from Process superclass and initializes spreading to false, yearly process and unique UI to true
  */
	public ModerateBisonGrazing() {
    super();

    spreading   = false;
    description = "Moderate Bison Grazing";
    yearlyProcess = true;
    uniqueUI    = true;
  }
	/**
	 * This is the probability common to all wyoming zones.  
	 * Bison Grazing by default is -1.  This indicates the probability must be calculated.
	 * @param evu
	 * @return probability of moderate bison grazing in wyoming.  
	 */
  public int doProbabilityWyoming (Evu evu) {
    int prob = simpplle.comcode.BisonGrazing.getModerate();

    if (prob == -1) {
      simpplle.comcode.BisonGrazing.calcProbability(evu);
      prob = simpplle.comcode.BisonGrazing.getModerate();
    }
    simpplle.comcode.BisonGrazing.resetModerate();
    return prob;
  }
  public int doProbability (simpplle.comcode.zone.WesternGreatPlainsSteppe zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
  public int doProbability (simpplle.comcode.zone.GreatPlainsSteppe zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
  public int doProbability (simpplle.comcode.zone.MixedGrassPrairie zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
}

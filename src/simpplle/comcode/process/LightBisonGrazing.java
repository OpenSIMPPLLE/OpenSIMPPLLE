package simpplle.comcode.process;

import simpplle.comcode.Process;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Light Bison Grazing, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.Process
 */

public class LightBisonGrazing extends Process {
  /**
   * Constructor for Light Bison Grazing.  Inherits from Process superclass, and initializes spreading to false, description, yearly process and uniqueUI to true
   */
	public LightBisonGrazing() {
    super();

    spreading   = false;
    description = "Light Bison Grazing";
    yearlyProcess = true;
    uniqueUI    = true;
  }
	/**
	 * by default probability of light is -1, so if it is still -1 means probability has not yet been calculated
	 * @param evu ecological vegetative unit being evaluated
	 * @return the probability of light bison grazing in the choosen evu
	 */
  
  public int doProbabilityWyoming (simpplle.comcode.element.Evu evu) {
    int prob = simpplle.comcode.BisonGrazing.getLight();

    if (prob == -1) {
      simpplle.comcode.BisonGrazing.calcProbability(evu);
      prob =  simpplle.comcode.BisonGrazing.getLight();
    }
    simpplle.comcode.BisonGrazing.resetLight();
    return prob;
  }
  public int doProbability (simpplle.comcode.zone.WesternGreatPlainsSteppe zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityWyoming(evu);
  }
  public int doProbability (simpplle.comcode.zone.GreatPlainsSteppe zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityWyoming(evu);
  }
  public int doProbability (simpplle.comcode.zone.MixedGrassPrairie zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityWyoming(evu);
  }
}

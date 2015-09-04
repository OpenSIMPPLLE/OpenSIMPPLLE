package simpplle.comcode;

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
  
  public int doProbabilityWyoming (Evu evu) {
    int prob = BisonGrazing.getLight();

    if (prob == -1) {
      BisonGrazing.calcProbability(evu);
      prob =  BisonGrazing.getLight();
    }
    BisonGrazing.resetLight();
    return prob;
  }
  public int doProbability (WesternGreatPlainsSteppe zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
  public int doProbability (GreatPlainsSteppe zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
  public int doProbability (MixedGrassPrairie zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
}

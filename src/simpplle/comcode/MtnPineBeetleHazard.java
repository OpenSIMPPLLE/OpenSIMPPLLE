package simpplle.comcode;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Mountain Pine Beetle Hazard.  Choices for the hazard are LOW, MODERATE, HIGH.  
 * These are all Hazard objects from Hazard.java
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */
public class MtnPineBeetleHazard {
  public enum Hazard { LOW, MODERATE, HIGH }

  public static final Hazard LOW = Hazard.LOW;
  public static final Hazard MODERATE = Hazard.MODERATE;
  public static final Hazard HIGH = Hazard.HIGH;

  public MtnPineBeetleHazard() {
  }
}

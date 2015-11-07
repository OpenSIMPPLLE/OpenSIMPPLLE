package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the Fire Logic.  It is abstract and contains variables for forest structure, resistance to fire, forest ownership,
 * and a method to determine ownership.
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */
public abstract class FireLogicDefines {
  // Structure
  public static final int NON_FOREST     = simpplle.comcode.process.FireEvent.NON_FOREST.ordinal();
  public static final int SINGLE_STORY   = simpplle.comcode.process.FireEvent.SINGLE_STORY.ordinal();
  public static final int MULTIPLE_STORY = simpplle.comcode.process.FireEvent.MULTIPLE_STORY.ordinal();

  // Resistance to Fire
  public static final int LOW      = simpplle.comcode.process.FireEvent.LOW;
  public static final int MODERATE = simpplle.comcode.process.FireEvent.MODERATE;
  public static final int HIGH     = simpplle.comcode.process.FireEvent.HIGH;

  // Ownership
  public static final int NF_WILDERNESS = simpplle.comcode.process.FireEvent.NF_WILDERNESS;
  public static final int NF_OTHER      = simpplle.comcode.process.FireEvent.NF_OTHER;
  public static final int OTHER         = simpplle.comcode.process.FireEvent.OTHER;



  /**
   * Determines ownership of an Evu
   * @param evu existing vegetative unit
   * @return the owner choices are NF_OTHER, NF_WILDERNESS, OTHER.  by default returns OTHER
   */
  protected static int determineOwnership(simpplle.comcode.element.Evu evu) {
    String ownershipStr = evu.getOwnership();
    if (ownershipStr.equalsIgnoreCase("NF-OTHER"))           { return NF_OTHER; }
    else if (ownershipStr.equalsIgnoreCase("NF-WILDERNESS")) { return NF_WILDERNESS; }
    else if (ownershipStr.equalsIgnoreCase("OTHER"))         { return OTHER; }
    else { return OTHER; }
  }
}



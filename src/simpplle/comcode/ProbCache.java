package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines methods for Probability Cache.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */

public abstract class ProbCache {
  private static final Short[] values;

  private static final Short NOPROB = new Short((short) simpplle.comcode.element.Evu.NOPROB);
  private static final Short D      = new Short((short) simpplle.comcode.element.Evu.D);
  private static final Short L      = new Short((short) simpplle.comcode.element.Evu.L);
  private static final Short S      = new Short((short) simpplle.comcode.element.Evu.S);
  private static final Short SUPP   = new Short((short) simpplle.comcode.element.Evu.SUPP);
  private static final Short SE     = new Short((short) simpplle.comcode.element.Evu.SE);
  private static final Short SFS    = new Short((short) simpplle.comcode.element.Evu.SFS);
  private static final Short COMP   = new Short((short) simpplle.comcode.element.Evu.COMP);
  private static final Short GAP    = new Short((short) simpplle.comcode.element.Evu.GAP);

  static {
    values = new Short[10100];
    for (short i=0; i<values.length; i++) {
      values[i] = new Short(i);
    }
  }
/**
 * Uses parameter integer value to get the short probability.  
 * Integer values are NOPROB -1, D -2, L -3, S -4, SUPP -5, SE -6, SFS -7, COMP -8, GAP -9.  Default is all short values in short array.  
 * @param value
 * @return
 */
  public static Short get(int value) {
    switch (value) {
      case simpplle.comcode.element.Evu.NOPROB: return NOPROB;
      case simpplle.comcode.element.Evu.D:      return D;
      case simpplle.comcode.element.Evu.L:      return L;
      case simpplle.comcode.element.Evu.S:      return S;
      case simpplle.comcode.element.Evu.SUPP:   return SUPP;
      case simpplle.comcode.element.Evu.SE:     return SE;
      case simpplle.comcode.element.Evu.SFS:    return SFS;
      case simpplle.comcode.element.Evu.COMP:   return COMP;
      case simpplle.comcode.element.Evu.GAP:    return GAP;
      default:         return values[value];
    }
  }

}



/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import org.apache.commons.collections.keyvalue.*;
import simpplle.comcode.Climate.*;

/**
 * This class contains the methods to control the multi keys data structure used throughout code
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class LifeformSeasonKeys {
  private static MultiKey[][] keys = new MultiKey[Lifeform.numValues()][Climate.Season.numValues()];

  static {
    Lifeform[] lives = Lifeform.getAllValues();

    for (int l=0; l<lives.length; l++) {
      for (int s=0; s<Climate.allSeasons.length; s++) {
        keys[lives[l].getDominance()][Climate.allSeasons[s].ordinal()] =
          new MultiKey(lives[l],Climate.allSeasons[s]);
      }

    }
  }

/**
 * Gets the multikey which will be an multidimensional array based on lifeform.id and season.ordinal()
 * The possible choices for lifeform id are 0 - trees, 1 shrubs, 2- herbacious, 3 - agriculture, 4 - no classification.
 * The possible choices for season.ordinal are 0 - SPRING, 1 - SUMMER, 2 - FALL, 3 - WINTER, 4 - YEAR
 *
 * @param lifeform
 * @param season
 * @return the multikey composed of a multidimensional array of size lifeform id and season ordinal.
 */
  public static MultiKey getKey(Lifeform lifeform, Season season) {
    return keys[lifeform.getDominance()][season.ordinal()];
  }

}

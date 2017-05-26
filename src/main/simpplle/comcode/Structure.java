/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * A forest structure for use in vegetation size classification.
 */
public enum Structure {

  NON_FOREST,
  SINGLE_STORY,
  MULTIPLE_STORY;

  /**
   * Looks up a forest structure by an ordinal value.
   *
   * @param ordinal an ordinal value
   * @return a forest structure, or null
   */
  public static Structure lookupOrdinal(int ordinal) {
    switch (ordinal) {
      case 0:
        return NON_FOREST;
      case 1:
        return SINGLE_STORY;
      case 2:
        return MULTIPLE_STORY;
      default:
        return null;
    }
  }
}

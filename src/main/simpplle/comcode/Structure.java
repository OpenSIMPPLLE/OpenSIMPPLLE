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

package simpplle.comcode;

/**
 * A season represents a division of a year. This enumeration accounts for either four seasons
 * (spring, summer, fall, winter) or one season (year).
 */
public enum Season {

  // Warning: Do not rearrange these values.

  SPRING,
  SUMMER,
  FALL,
  WINTER,
  YEAR;

  public Season getPriorSeason() {
    switch (this) {
      case SPRING:
        return WINTER;
      case SUMMER:
        return SPRING;
      case FALL:
        return SUMMER;
      case WINTER:
        return FALL;
      default:
        return YEAR;
    }
  }
}

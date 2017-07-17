package simpplle.comcode;

/**
 * A season represents a division of a year. This enumeration accounts for either four seasons
 * (spring, summer, fall, winter) or one season (year).
 */
public enum Season {

  SPRING,
  SUMMER,
  FALL,
  WINTER,
  YEAR;

  public static Season getPriorSeason(Season season) {
    switch (season) {
      case SPRING:
        return WINTER;
      case SUMMER:
        return SPRING;
      case FALL:
        return SUMMER;
      case WINTER:
        return FALL;
      case YEAR:
        return YEAR;
      default:
        return YEAR;
    }
  }
}

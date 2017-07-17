package simpplle.comcode;

/**
 * Created by greg on 7/17/17.
 */
public enum Season {

  SPRING,
  SUMMER,
  FALL,
  WINTER,
  YEAR;

  public static int numValues() {
    return 5;
  }

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

package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a process occurrence that spreads fire using a cellular percolation technique developed
 * by Robert Keane. The technique factors in wind speed, wind direction, and slope along with fire
 * spread rules to approximate real-life fire spreading behavior.
 */
public class KeaneFireEvent extends ProcessOccurrenceSpreadingFire {

  /**
   * The maximum wind speed in miles per hour
   */
  private static final double MAX_WIND_SPEED = 30;

  /**
   * A factor that multiplies the wind speed when the fire event is extreme.
   */
  public static double extremeWindMultiplier = 4.0;

  /**
   * The variability in wind speed in degrees.
   */
  public static double windDirectionVariability = 45.0;

  /**
   * The variability in wind speed in miles per hour.
   */
  public static double windSpeedVariability = 5.0;

  /**
   * An offset in degrees to add to the 'from' unit's wind direction.
   */
  private double windDirectionOffset;

  /**
   * An offset in miles per hour to add to the 'from' unit's wind speed.
   */
  private double windSpeedOffset;

  /**
   * Counter to track when the fire event tries to spread outside of the existing landscape
   */
  private int missingAdjacencies;


  /**
   * Creates a Keane fire event, with random wind speed and direction offsets.
   *
   * @param evu source of fire
   * @param lifeform life form
   * @param processData process probability
   * @param timeStep time step
   */
  public KeaneFireEvent(Evu evu, Lifeform lifeform, ProcessProbability processData, int timeStep) {

    super(evu,lifeform,processData,timeStep);

    // Compute wind speed and direction offsets. The nextGaussian method returns random numbers
    // with a mean of 0 and standard deviation of 1. Multiplying by 1/3 ensures that > 99% of
    // the values fall between -1 and 1.

    Random random = new Random();
    windSpeedOffset = random.nextGaussian() * (1.0 / 3.0) * windSpeedVariability;
    windDirectionOffset = random.nextGaussian() * (1.0 / 3.0) * windDirectionVariability;

  }

  /**
   * Spreads fire from a burning vegetation unit to adjacent vegetation units using a cellular
   * percolation algorithm developed by Robert Keane. The algorithm spreads columns of fire in
   * six directions around the originating unit. The distance to spread in each direction is
   * derived from the wind speed, wind direction, and slope of the terrain with a range of
   * variability. Fire spread rules are applied to each unit along a direction until there is
   * no applicable rule, the distance is travelled, or a boundary is reached. Burning units are
   * appended to the 'toUnits' list.
   *
   * @param source a burning vegetation unit
   * @param burned a list to store units that have been spread to
   * @param isExtreme indicates if fire is intense enough to influence weather
   */
  @Override
  void spreadToNeighbors(Evu source, ArrayList<Evu> burned, boolean isExtreme) {

    AdjacentData[] neighborhood = source.getNeighborhood();
    // TODO populate neighborhood, different methods to spread to existing or missing


    for (int i = 0; i < neighborhood.length; i++) {  // TODO use evu.NUM_NEIGHBORS ?
      AdjacentData adjacent = neighborhood[i];

      if(adjacent != null){
        double windSpeed = adjacent.getWindSpeed();
        double windDirection = adjacent.getWindDirection();
        double spreadDirection = adjacent.getSpread();
        double slope = adjacent.getSlope();

        // Add an event specific wind speed offset
        windSpeed += windSpeedOffset;

        // Multiply wind speed for extreme fires
        if (isExtreme) windSpeed *= extremeWindMultiplier;

        // Clamp to the maximum wind speed
        windSpeed = Math.min(MAX_WIND_SPEED, windSpeed);

        // Offset the wind direction and truncate angle within 360 degrees
        windDirection = (windDirection + windDirectionOffset) % 360;

        // Compute the number of pixels to spread to
        double spix = calcSpix(windSpeed, windDirection, spreadDirection, slope);

        // Reduce number of pixels on corners, since diagonals cover longer distances
        if (spreadDirection == 45.0 ||
            spreadDirection == 135.0 ||
            spreadDirection == 225.0 ||
            spreadDirection == 315.0) {

          spix /= Math.sqrt(2);

        }

        List<AdjacentData> neighbors = source.getNeighborsAlongDirection(adjacent.getSpread(), probabilisticRound(spix));
        Evu prevUnit = source;
        for (AdjacentData neighbor : neighbors) {
          if (hasSuppressionLine(neighbor.evu)) break;
          neighbor.setWind(prevUnit.isDownwind(spreadDirection, windDirection));
          boolean toUnitWasBurning = neighbor.evu.hasFireAnyLifeform();
          if (Evu.doSpread(prevUnit, neighbor.evu, prevUnit.getDominantLifeformFire())) {
            if (!toUnitWasBurning) {
              burned.add(neighbor.evu);
            }
          } else {
            break;
          }
          prevUnit = neighbor.evu;
        }
      } else {
        missingAdjacencies++;
      }
  }
  }

  /**
   * Compute the number of pixels that a fire should attempt to spread in a direction. The
   * calculation uses the wind speed, wind direction, spread direction, and slope along the
   * spread direction. Fire spreads more quickly uphill and along the direction of the wind.
   *
   * @param windSpeed speed of the wind in miles per hour
   * @param windDirection source direction of the wind in degrees azimuth
   * @param spreadDirection source direction of the spread in degrees azimuth
   * @param slope slope of the land along the spread (percentage / 100)
   * @return number of pixels to spread
   */
  private double calcSpix(double windSpeed,
                          double windDirection,
                          double spreadDirection,
                          double slope) {

    double windSpread;
    double slopeSpread;

    if (windSpeed > 0.5) {

      // Get the difference between the spread and wind direction in radians [0,PI)
      double diff = Math.toRadians(Math.abs(spreadDirection - windDirection) % 360);

      // Compute a coefficient that reflects wind direction between 0 and 1
      double coeff = (Math.cos(diff) + 1.0) / 2.0;

      // Compute the length to width ratio from Rothermal (1991 p.16)
      double lwr = 1.0 + (0.125 * windSpeed);

      // Scale the function based on wind speed between 1 and 10
      windSpread = lwr * Math.pow(coeff, Math.pow(windSpeed,0.6));

    } else {

      // Fire should spread even with little or no wind
      windSpread = 1.0;

    }

    if (slope > 0.0) {

      // Fire spreads more quickly uphill
      slopeSpread = 4.0 / (1.0 + 3.5 * Math.exp(-10 * slope));

    } else {

      // Fire spreads more slowly downhill
      slopeSpread = Math.exp(-3 * slope * slope);

    }

    return windSpread + slopeSpread;

  }

  /**
   * Casts a floating-point number to an integer. The fractional portion is interpreted as the
   * probability that a number will be rounded to the next whole number. For example, the number
   * 1.3 has a 30% chance of being rounded up to 2.
   *
   * @param number a floating-point number
   * @return a whole number
   */
  private int probabilisticRound(double number) {
    double probability = number % 1;
    double draw = Math.random();
    return (draw <= probability) ? (int)Math.floor(number) : (int)Math.ceil(number);
  }

  /**
   * Saves global parameters to a single line of text.
   *
   * @param printWriter an open writer
   */
  public static void saveParameters(PrintWriter printWriter){
    printWriter.println(extremeWindMultiplier + ", "
                      + windSpeedVariability + ", "
                      + windDirectionVariability);
  }

  /**
   * Loads global parameters from a single line of text.
   *
   * @param bufferedReader an open reader
   */
  public static void loadParameters(BufferedReader bufferedReader) throws SimpplleError {
    try {
      String line = bufferedReader.readLine();
      if (line == null) {
        throw new SimpplleError("Missing keane fire event parameters");
      }
      String[] parameters = line.split(",");
      if (parameters.length != 3) {
        throw new SimpplleError("Expected 3 keane parameters, found " + parameters.length);
      } else {
        extremeWindMultiplier = Double.parseDouble(parameters[0]);
        windSpeedVariability = Double.parseDouble(parameters[1]);
        windDirectionVariability = Double.parseDouble(parameters[2]);
      }
    } catch (IOException e) {
      throw new SimpplleError("Error loading Keane fire event parameters : " + e.getMessage());
    }
  }

}

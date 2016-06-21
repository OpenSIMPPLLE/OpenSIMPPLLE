package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * <p> Strategy for parsing spatialrelate files with the "KEANE" designation (version 2). Format:
 * FROM_POLY, TO_POLY, ELEV, SPREAD_DEG, BASE_WIND_SPEED, BASE_WIND_DIR
 *
 */
public class ParseAreaV2 implements IParseArea{

  boolean hasAttributes;

  /**
   * reads in area information for new neighbors:
   * FROM_POLY, TO_POLY, ELEV, SPREAD_DEG, BASE_WIND_SPEED, BASE_WIND_DIR
   *
   * SPREAD_DEG is supposed to be the “Degrees Azimuth” between the FROM_POLY and the TO_POLY
   * (POLY is the same as SLINK).
   * BASE_WIND_DIR is the direction of the wind is coming from.
   *
   * Adds all info to current area.
   *
   * @param area the current area whose neighbors are defined in this method
   * @param in   Buffered reader with spatialrelate file in stream
   * @param log  Open file to log errors/warnings
   * @return true if area file found (sets neighbor information as well)
   * @throws ParseError  caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readNeighborsNew(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String line, str;
    char wind, pos = 'E';
    StringTokenizerPlus st;
    HashMap<Integer, Evu> unitHm = new HashMap<>();
    Evu[] allEvu;
    int maxEvuId = -1,
        from,
        to,
        elevation,
        spread,
        windSpeed,
        windDirection;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File. (file is empty))");
      log.println();
      return false;
    }
    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) {
        line = in.readLine();
        continue;
      }
      st = new StringTokenizerPlus(line, ",");

      // FROM and TO_POLY
      from = st.getIntToken();
      to = st.getIntToken();
      if (from == -1 || to == -1) {
        log.println(line + "\n  One of the polygon id's in above line is invalid.\n");
        return false;
      }
      if (from > maxEvuId) maxEvuId = from;
      if (to > maxEvuId)   maxEvuId = to;

      // ELEV
      str = st.getToken();
      try {
        elevation = Integer.parseInt(str);
      } catch (NumberFormatException ex) {
        elevation = NaturalElement.INVALID_ELEV;
      }

      // SPREAD_DEG
      str = st.getToken();
      try {
        spread = Integer.parseInt(str);
      } catch (NumberFormatException ex) {
        log.println("Invalid value for Spread Degrees: " + str +
            "\nIn line: " + line);
        return false;
      }

      // BASE_WIND_SPEED
      str = st.getToken();
      try {
        windSpeed = Integer.parseInt(str);
      } catch (NumberFormatException ex) {
        log.println("invalid value for base wind speed : " + str +
            "\nIn line: " + line);
        return false;
      }

      // BASE_WIND_DIR
      str = st.getToken();
      try{
        windDirection = Integer.parseInt(str);
      } catch (NumberFormatException ex){
        log.println("invalid value for base wind direction : " + str +
            "\nIn line: " + line);
        return false;
      }

      // Calculate if a unit is downwind based on spread and windDirection.
      // TODO: define angle threshold for downwind
      int threshold = 90;
      if(getAngleDifference(spread, windDirection)>threshold) wind = 'D';
      else wind = 'N';

      // Make evu or load existing
      Evu evu = unitHm.get(from);
      if (evu == null) {
        evu = new Evu(from);
        evu.setElevation(elevation);
        unitHm.put(from, evu);
      }

      // Add info to current area
      area.addAdjacentData(evu, to, pos, wind, spread, windSpeed, windDirection);

      line = in.readLine();
    }
    hasAttributes = line != null;
    allEvu = new Evu[maxEvuId + 1];
    for (Evu evu : unitHm.values()) {
      from = evu.getId();
      allEvu[from] = evu;
    }

    area.setMaxEvuId(maxEvuId);
    area.setAllEvu(allEvu);
    area.finishAddingAdjacentData(log);
    return true;
  }

  /**
   * Helper Method
   * @param a angle
   * @param b angle 2
   * @return UNSIGNED Angle difference between given angles
   */
  private int getAngleDifference(int a, int b){
    int diff = Math.abs(a-b);
    return (diff < 180) ? diff : 360 - diff;
  }

  @Override
  public boolean readLandNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }

  @Override
  public boolean readAquaticNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }

  @Override
  public boolean readVegLandRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }

  @Override
  public boolean readAquaticVegRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }

  @Override
  public boolean readRoadNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }

  @Override
  public boolean readTrailNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }

  @Override
  public boolean readVegRoadRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }

  @Override
  public boolean readVegTrailRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
  }
}

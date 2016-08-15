/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Reads in area information for new neighbors.
 */

public class ParseNewNeighbors implements RelationParser {

  /**
   * reads in area information for new neighbors:
   * area id, adjacent area id, elevation, existing land units,
   * position (valid are N = next to, A =above, or B =below),
   * wind (valid are D =downwind or N =no wind), evu, and then sets evu elevation.
   * Adds all info to current area. Also checks elevation to make sure it is valid.
   *
   * @param area the current area whose neighbors are defined in this method
   * @param in   Buffered reader with spatialrelate file in stream
   * @param log  Open file to log errors/warnings
   * @return true if area file found (sets neighbor information as well)
   * @throws ParseError  caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {

    String line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty)");
      log.println();
      return false;
    }

    HashMap<Integer, Evu> unitHm = new HashMap<>();
    int maxEvuId = -1;

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) {
        line = in.readLine();
        continue;
      }
      StringTokenizerPlus strTok = new StringTokenizerPlus(line, ",");

      int id = strTok.getIntToken();
      if (id < 0) {
        log.println(line + "\n " + id + " is an invalid id.\n");
        return false;
      }
      maxEvuId = Math.max(maxEvuId,id);

      int adjId = strTok.getIntToken();
      if (adjId < 0) {
        log.println(line + "\n " + adjId + " is an invalid adjacent id.\n");
        return false;
      }
      maxEvuId = Math.max(maxEvuId,adjId);

      String str = strTok.getToken();
      int elevation;
      try {
        elevation = Integer.parseInt(str);
      } catch (NumberFormatException ex) {
        elevation = NaturalElement.INVALID_ELEV;
      }

      char pos = 'N';
      if (elevation == NaturalElement.INVALID_ELEV) {
        String posStr = str;
        if (posStr.length() > 1) {
          int index = posStr.lastIndexOf('\'');
          if (index == -1) {
            index = posStr.length() - 1;
          }
          posStr = posStr.substring(1, index);
        }
        if (posStr.length() > 1) {
          log.println(line);
          log.println("   " + posStr + " is not a valid position or Elevation");
          log.println("   Valid values are:  N, A, B or Integer Elevation");
          return false;
        }
        pos = posStr.charAt(0);

        if (pos != Evu.ABOVE && pos != Evu.BELOW && pos != Evu.NEXT_TO) {
          log.println(line);
          log.println("   " + pos + " is not a valid position or Elevation value.");
          log.println("   Valid values are:  A, B, N or Integer Elevation");
          return false;
        }
      }

      String windStr = strTok.getToken();
      if (windStr.length() > 1) {
        int index = windStr.lastIndexOf('\'');
        if (index == -1) {
          index = windStr.length() - 1;
        }
        windStr = windStr.substring(1, index);
      }
      if (windStr.length() > 1) {
        log.println(line);
        log.println("   " + windStr + " is not valid wind value");
        log.println("   Valid values are:  D, N");
        return false;
      } else if (windStr.length() == 0) {
        windStr = "N";
      }
      char wind = windStr.charAt(0);

      if (wind != Evu.DOWNWIND && wind != Evu.NO_WIND) {
        log.println(line);
        log.println("   " + wind + " is not a valid wind value.");
        log.println("   Valid values are:  D, N");
        return false;
      }

      Evu evu = unitHm.get(id);
      if (evu == null) {
        evu = new Evu(id);
        unitHm.put(id, evu);

        // Need only set elevation once.
        evu.setElevation(elevation);
      }

      area.addAdjacentData(evu, adjId, pos, wind);

      // Get the next line.
      line = in.readLine();
    }

    Evu[] allEvu = new Evu[maxEvuId + 1];

    for (Evu evu : unitHm.values()) {
      int id = evu.getId();
      allEvu[id] = evu;
    }

    area.setMaxEvuId(maxEvuId);
    area.setAllEvu(allEvu);
    area.finishAddingAdjacentData(log);
    return true;
  }

}



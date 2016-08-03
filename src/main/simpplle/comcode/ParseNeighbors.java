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
import java.util.HashMap;

/**
 *
 */

public class ParseNeighbors implements RelationParser{

  /**
   * reads in area id, adjacent area id's position(above, below, next to), wind value (down wind, no wind)
   * evu id,
   *  @param area the current area whose neighbors are defined in this method
   * @return false if no area file, true if there is a file (sets neighbor information as well if true)
   * @throws ParseError caught in gui
   * @throws IOException caught in gui
   */
  public boolean readSection(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    String              line, str;
    StringTokenizerPlus strTok;
    HashMap<Integer,Evu> unitHm = new HashMap<Integer, Evu>();
    Evu[]               allEvu;
    int                 maxEvuId = -1, i, id, adjId, index;
    String              posStr, windStr;
    char                pos, wind;

    line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      strTok = new StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }
      if (id > maxEvuId) { maxEvuId = id; }// changes id to maxID
      if (adjId > maxEvuId) { maxEvuId = adjId; }

      posStr = strTok.getToken();
      if (posStr.length() > 1) {
        index = posStr.lastIndexOf('\'');
        if (index == -1) {
          index = posStr.length() - 1;
        }
        posStr = posStr.substring(1, index);
      }
      if (posStr.length() > 1) {
        logFile.println(line);
        logFile.println("   " + posStr + " is not a valid position");
        logFile.println("   Valid values are:  N, A, B");
        return false;
      }
      pos = posStr.charAt(0);

      windStr = strTok.getToken();
      if (windStr.length() > 1) {
        index = windStr.lastIndexOf('\'');
        if (index == -1) {
          index = windStr.length() - 1;
        }
        windStr = windStr.substring(1, index);
      }
      if (windStr.length() > 1) {
        logFile.println(line);
        logFile.println("   " + windStr + " is not valid wind value");
        logFile.println("   Valid values are:  D, N");
        return false;
      }
      wind = windStr.charAt(0);

      if (pos != Evu.ABOVE && pos != Evu.BELOW && pos != Evu.NEXT_TO) {
        logFile.println(line);
        logFile.println("   " + pos + " is not a valid position value.");
        logFile.println("   Valid values are:  A, B, N");
        return false;
      }
      if (wind != Evu.DOWNWIND && wind != Evu.NO_WIND) {
        logFile.println(line);
        logFile.println("   " + wind + " is not a valid wind value.");
        logFile.println("   Valid values are:  D, N");
        return false;
      }

      Evu evu = unitHm.get(id);
      if (evu == null) {
        evu = new Evu(id);
        unitHm.put(id, evu);
      }
      area.addAdjacentData(evu,adjId,pos,wind);

      // Get the next line.
      line = fin.readLine();
    }

    allEvu = new Evu[maxEvuId+1];

    for (Evu evu : unitHm.values()) {
      id = evu.getId();
      allEvu[id] = evu;
    }

    area.setMaxEvuId(maxEvuId);
    area.setAllEvu(allEvu);
    area.finishAddingAdjacentData(logFile);

    return true;
  }

}

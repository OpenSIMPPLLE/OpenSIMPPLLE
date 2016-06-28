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
 * Reads in the neighboring roads to a specified area.
 */
public class ParseRoadNeighbors implements RelationParser{

  private boolean hasAttributes;
  /**
   * Reads in the neighboring roads to a specified area.  Sets Area id, adjacent area id,
   * road unit id, adjacent road unit id. * Also creates an array of roads.
   * @param area the current area whose road neighbors are defined in this method.
   * @return true if area file found (sets road neighbor relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    HashMap<Integer,Roads> units = new HashMap<Integer,Roads>();
    int                    maxId = -1;

    String line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      int   id, adjId;
      Roads roadUnit, adjRoadUnit;

      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        log.println(line + "\n  One of the id's in above line is invalid.\n");
        return false;
      }
      if (id > maxId) { maxId = id; }
      if (adjId > maxId) { maxId = adjId; }

      roadUnit = units.get(id);
      if (roadUnit == null) {
        roadUnit = new Roads(id);
        units.put(id,roadUnit);
      }

      if (adjId != -9999) {
        adjRoadUnit = units.get(adjId);
        if (adjRoadUnit == null) {
          adjRoadUnit = new Roads(adjId);
          units.put(adjId, adjRoadUnit);
        }
      }
      // Get the next line.
      line = in.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    Roads[] allRoads = new Roads[maxId+1];
    Roads   roadUnit;
    for (Integer keyId : units.keySet()) {
      roadUnit = units.get(keyId);
      allRoads[roadUnit.getId()] = roadUnit;

    }

    area.setAllRoads(allRoads);

    return true;
  }

}

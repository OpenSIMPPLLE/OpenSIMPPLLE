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
 * <p> Reads in the neighboring trails to a specified area.
 */
public class ParseTrailNeighbors implements RelationParser{

  private boolean hasAttributes;
  /**
   * Reads in the neighboring trails to a specified area.  Sets Area id, adjacent area id, trail units, adjacent trail units.
   * Also makes a trail unit array.
   * @param area the current area whose trail neighbors are defined in this method.
   * @return true if area file found (sets trail neighbor information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    HashMap<Integer,Trails> units = new HashMap<Integer,Trails>();
    int                    maxId = -1;

    String line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      int   id, adjId;
      Trails trailUnit, adjTrailUnit;

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

      trailUnit = units.get(id);
      if (trailUnit == null) {
        trailUnit = new Trails(id);
        units.put(id,trailUnit);
      }

      if (adjId != -9999) {
        adjTrailUnit = units.get(adjId);
        if (adjTrailUnit == null) {
          adjTrailUnit = new Trails(adjId);
          units.put(adjId, adjTrailUnit);
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

    Trails[] allTrails = new Trails[maxId+1];
    Trails   trailUnit;
    for (Integer keyId : units.keySet()) {
      trailUnit = units.get(keyId);
      allTrails[trailUnit.getId()] = trailUnit;

    }

    area.setAllTrails(allTrails);

    return true;
  }

}

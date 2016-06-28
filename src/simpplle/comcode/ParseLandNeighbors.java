package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * Reads in Land Neighbors of the specified area.
 */
public class ParseLandNeighbors implements RelationParser{

  private boolean hasAttributes;
  /**
   * @return true if no area file found (sets neighbor information as well)
   * @throws ParseError
   * @throws IOException
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String line;
    StringTokenizerPlus strTok;
    ExistingLandUnit elu, adjElu;
    HashMap units = new HashMap();
    ExistingLandUnit[] allElu;
    int maxId = -1, id, adjId;
    int elevation;
    Integer idObj, adjIdObj;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) {
        line = in.readLine();
        continue;
      }
      strTok = new StringTokenizerPlus(line, ",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        log.println(line);
        log.println("  One of the id's in above line is invalid");
        log.println();
        return false;
      }
      if (id > maxId) {
        maxId = id;
      }
      if (adjId > maxId) {
        maxId = adjId;
      }

      elevation = strTok.getIntToken();
      if (elevation == -1) {
        log.println(line);
        log.println("  Elevation in the above line is invalid");
        log.println();
        return false;
      }

      idObj = new Integer(id);
      elu = (ExistingLandUnit) units.get(idObj);
      if (elu == null) {
        elu = new ExistingLandUnit(id);
        units.put(idObj, elu);
      }

      adjIdObj = new Integer(adjId);
      adjElu = (ExistingLandUnit) units.get(adjIdObj);
      if (adjElu == null) {
        adjElu = new ExistingLandUnit(adjId);
        units.put(adjIdObj, adjElu);
      }

      elu.addNeighbor(adjElu);
      elu.setElevation(elevation);

      // Get the next line.
      line = in.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    } else {
      hasAttributes = true;
    }

    allElu = new ExistingLandUnit[maxId + 1];

    Iterator it = units.keySet().iterator();
    while (it.hasNext()) {
      idObj = (Integer) it.next();
      elu = (ExistingLandUnit) units.get(idObj);
      allElu[elu.getId()] = elu;
    }

    area.setAllElu(allElu);

    return true;
  }
}

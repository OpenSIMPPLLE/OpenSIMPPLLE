package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * <p>
 *
 */
public class ParseAreaLegacy implements IParseArea {

  private boolean hasAttributes;

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
  public boolean readNeighborsNew(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {

    String line, str;
    StringTokenizerPlus strTok;
    HashMap<Integer, Evu> unitHm = new HashMap<Integer, Evu>();
    Evu[] allEvu;
    int maxEvuId = -1, i, id, adjId, index;
    String windStr;
    char pos = 'N', wind;
    int elevation;

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
        log.println(line + "\n  One of the id's in above line is invalid.\n");
        return false;
      }
      if (id > maxEvuId) {
        maxEvuId = id;
      }
      if (adjId > maxEvuId) {
        maxEvuId = adjId;
      }

      str = strTok.getToken();
      try {
        elevation = Integer.parseInt(str);
      } catch (NumberFormatException ex) {
        elevation = NaturalElement.INVALID_ELEV;
      }

      if (elevation == NaturalElement.INVALID_ELEV) {
        String posStr = str;
        if (posStr.length() > 1) {
          index = posStr.lastIndexOf('\'');
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


      windStr = strTok.getToken();
      if (windStr.length() > 1) {
        index = windStr.lastIndexOf('\'');
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
      wind = windStr.charAt(0);

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
    if (line == null) {
      hasAttributes = false;
    } else {
      hasAttributes = true;
    }

    allEvu = new Evu[maxEvuId + 1];

    for (Evu evu : unitHm.values()) {
      id = evu.getId();
      allEvu[id] = evu;
    }

    area.setMaxEvuId(maxEvuId);
    area.setAllEvu(allEvu);
    area.finishAddingAdjacentData(log);
    return true;
  }

  /**
   * @return true if no area file found (sets neighbor information as well)
   * @throws ParseError
   * @throws IOException
   */
  @Override
  public boolean readLandNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String line, str;
    StringTokenizerPlus strTok;
    ExistingLandUnit elu, adjElu;
    HashMap units = new HashMap();
    ExistingLandUnit[] allElu;
    int maxId = -1, i, id, adjId, index;
    int elevation;
    Integer idObj, adjIdObj;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
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

  /**
   * Reads in the aquatic neighbors of a specified area.  Area id, adjacent id, flow (P or S or N = no flow)
   * Sets existing aquatic unit.  Sets the predecessor, and successor units.
   * @param area the current area whose neighbors are defined in this method.
   * @return true if area file found (sets aquatic neighbor information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readAquaticNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {

    String              line, str;
    StringTokenizerPlus strTok;
    ExistingAquaticUnit eau = null, adjEau = null;
    HashMap             units = new HashMap();
    ExistingAquaticUnit[] allEau;
    int                 maxId = -1, id, adjId, index;
    Integer             idObj, adjIdObj;
    String              flowStr;
    char                flow;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      strTok = new StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        log.println(line);
        log.println("  One of the id's in above line is invalid");
        log.println();
        return false;
      }
      if (id > maxId) { maxId = id; }// sets the current id as new max if greater than current max
      if (adjId > maxId) { maxId = adjId; }

      flowStr = strTok.getToken();
      if (flowStr.length() > 1) {
        index = flowStr.lastIndexOf('\'');
        if (index == -1) {
          index = flowStr.length() - 1;
        }
        flowStr = flowStr.substring(1, index);
      }
      if (flowStr.length() > 1) {
        log.println(line);
        log.println("   " + flowStr + " is not a valid Flow Direction");
        log.println("   Valid values are:  P, S, N");
        return false;
      }
      flow = flowStr.charAt(0);

      idObj = new Integer(id);
      eau = (ExistingAquaticUnit)units.get(idObj);
      if (eau == null) {
        eau = new ExistingAquaticUnit(id);
        units.put(idObj,eau);
      }

      if (adjId != -9999) {
        adjIdObj = new Integer(adjId);
        adjEau = (ExistingAquaticUnit) units.get(adjIdObj);
        if (adjEau == null) {
          adjEau = new ExistingAquaticUnit(adjId);
          units.put(adjIdObj, adjEau);
        }

        if (flow == 'P') {
          eau.addPredecessor(adjEau);
        }
        else if (flow == 'S') {
          eau.addSuccessor(adjEau);
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

    allEau = new ExistingAquaticUnit[maxId+1];

    Iterator it = units.keySet().iterator();
    while (it.hasNext()) {
      idObj = (Integer)it.next();
      eau   = (ExistingAquaticUnit)units.get(idObj);
      allEau[eau.getId()] = eau;
    }

    area.setAllEau(allEau);

    // In the situation where two stream come together at the same
    // Arc Node to form one stream we need to make sure the upper streams
    // do not reference each other as Predecessors or Successors.
    for (int i=0; i<allEau.length; i++) {
      if (allEau[i] == null) { continue; }

      ArrayList preds = allEau[i].getPredecessors();
      if (preds != null && preds.size() > 0) {
        for (int a=0; a<preds.size(); a++) {
          ExistingAquaticUnit unit = (ExistingAquaticUnit)preds.get(a);
          for (int b=a+1; b<preds.size(); b++) {
            ExistingAquaticUnit unit2 = (ExistingAquaticUnit)preds.get(b);

            ArrayList unitPreds = unit.getPredecessors();
            if (unitPreds != null) { unitPreds.remove(unit2); }

            ArrayList unit2Preds = unit2.getPredecessors();
            if (unit2Preds != null) { unit2Preds.remove(unit); }

            ArrayList unitSuccs = unit.getSuccessors();
            if (unitSuccs != null) { unitSuccs.remove(unit2); }

            ArrayList unit2Succs = unit2.getSuccessors();
            if (unit2Succs != null) { unit2Succs.remove(unit); }
          }
        }
      }
    }

    return true;
  }

  /**
   * Reads in Vegetative Land Relations of a specified Area.  Reads evu ID, elu ID, then sets the evu's
   * associated land unit to the elu and sets the elu's associated evu to the evuID
   * @param area the current area whose Vegetative Land Relations are defined in this method.
   * @return true if area file found (sets vegetative land relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readVegLandRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {

    String              line;
    StringTokenizerPlus strTok;
    ExistingLandUnit    elu;
    Evu                 evu;
    int                 evuId, eluId;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      strTok = new StringTokenizerPlus(line,",");

      evuId = strTok.getIntToken();
      eluId = strTok.getIntToken();
      if (evuId == -1 || eluId == -1) {
        log.println(line + "\n  One of the id's in above line is invalid.\n");
        return false;
      }

      evu = area.getEvu(evuId);
      if (evu == null) {
        log.println(line);
        log.println("In Vegetation-Landform Evu-" + evuId + " is not valid");
        log.println();
      }

      elu = area.getElu(eluId);
      if (elu == null) {
        log.println(line);
        log.println("In Vegetation-Landform Elu-" + eluId + " is not valid");
        log.println();
      }

      if (evu != null && elu != null) {
        evu.addAssociatedLandUnit(elu);
        elu.addAssociatedVegUnit(evu);
      }

      // Get the next line.
      line = in.readLine();
    }
    return true;
  }

  /**
   * Reads in the Aquatic vegetation relations of a specified area.  Sets EVU id, upland adjacent value (valid are A = adjacent or U Upland),
   * gets the existing aquatic unit for this area and adds the evu to it.
   * @param area the current area whose Aquatic vegetation relations are defined in this method.
   * @return true if area file found (sets Aquatic Vegetation relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readAquaticVegRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String              line, str;
    StringTokenizerPlus strTok;
    ExistingAquaticUnit eau = null;
    Evu                 evu = null;
    int                 i, id, evuId, index;
    String              upAdjStr;
    char                upAdj;

    line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      strTok = new StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      evuId = strTok.getIntToken();
      if (id == -1 || evuId == -1) {
        log.println(line);
        log.println("  One of the id's in above line is invalid");
        log.println();
        return false;
      }

      upAdjStr = null;
      if (strTok.hasMoreTokens()) {
        upAdjStr = strTok.getToken();
      }
      // Currently the amls don't include this info, so use A as default.
      if (upAdjStr == null) { upAdjStr = "A"; }

      if (upAdjStr.length() > 1) {
        index = upAdjStr.lastIndexOf('\'');
        if (index == -1) {
          index = upAdjStr.length() - 1;
        }
        upAdjStr = upAdjStr.substring(1, index);
      }
      if (upAdjStr.length() > 1) {
        log.println(line);
        log.println("   " + upAdjStr + " is not a Upland/Adjacent Value");
        log.println("   Valid values are:  A, U");
        return false;
      }
      upAdj = upAdjStr.charAt(0);

      eau = area.getEau(id);
      if (eau == null) {
        eau = new ExistingAquaticUnit(id);
        area.setEau(eau);
      }

      evu = area.getEvu(evuId);
      if (evu == null) {
        log.println(line);
        log.println("Vegetative Unit " + evuId + " is not valid");
        log.println();
        return false;
      }

      evu.addAssociatedAquaticUnit(eau);
      if (upAdj == 'A') {
        eau.addAdjacentEvu(evu);
      }
      else if (upAdj == 'U') {
        eau.addUplandEvu(evu);
      }

      // Get the next line.
      line = in.readLine();
    }
    return true;
  }


  /**
   * Reads in the neighboring roads to a specified area.  Sets Area id, adjacent area id,
   * road unit id, adjacent road unit id. * Also creates an array of roads.
   * @param area the current area whose road neighbors are defined in this method.
   * @return true if area file found (sets road neighbor relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readRoadNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
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

  /**
   * Reads in the neighboring trails to a specified area.  Sets Area id, adjacent area id, trail units, adjacent trail units.
   * Also makes a trail unit array.
   * @param area the current area whose trail neighbors are defined in this method.
   * @return true if area file found (sets trail neighbor information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readTrailNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
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

  /**
   * Reads the Vegetative Road Relations for a specified Area.  Reads in road Id, and evu ID,
   * adds the adds the evu as an associated vegetative unit to the road and adds the roads as associated road units to the evu
   * @param area the current area whose Vegetative Road  Relations are defined in this method.
   * @return true if area file found (sets Vegetative Trail Relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readVegRoadRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

      int roadId = strTok.getIntToken();
      int evuId = strTok.getIntToken();
      if (evuId == -1 || roadId == -1) {
        log.println(line + "\n  One of the id's in above line is invalid. \n");
        return false;
      }

      Evu evu = area.getEvu(evuId);
      if (evu == null) {
        log.println(line);
        log.println("In Vegetation-Roads Evu-" + evuId + " is not valid");
        log.println();
      }

      Roads roadUnit = area.getRoadUnit(roadId);
      if (roadUnit == null) {
        log.println(line);
        log.println("In Vegetation-Roads Road Id: " + roadId + " is not valid");
        log.println();
      }

      if (evu != null && roadUnit != null) {
        evu.addAssociatedRoadUnit(roadUnit);
        roadUnit.addAssociatedVegUnit(evu);
      }

      // Get the next line.
      line = in.readLine();
    }
    return true;
  }

  /**
   * Reads in and sets the vegetative trail relations of a specified Area.  Read in are the trial ID's, evu ID's
   * then the evu is assigned the associated trail units (trail ID) and the trails are assigned the associated evu
   * @param area the current area whose Vegetative Trail Relations are defined in this method.
   * @return true if area file found (sets Vegetative Trail Relations information as well)
   * @throws ParseError caught in GUI
   * @throws IOException caught in GUI
   */
  @Override
  public boolean readVegTrailRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    String line = in.readLine();
    if (line == null) {
      log.println("Invalid New Area File (file is empty))");
      log.println();
      return false;
    }

    while (line != null && !line.trim().equals("END")) {
      if (line.trim().length() == 0) { line = in.readLine(); continue; }
      StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

      int trailId = strTok.getIntToken();
      int evuId = strTok.getIntToken();
      if (evuId == -1 || trailId == -1) {
        log.println(line + "\n  One of the id's in above line is invalid.\n");
        return false;
      }

      Evu evu = area.getEvu(evuId);
      if (evu == null) {
        log.println(line);
        log.println("In Vegetation-Trails Evu-" + evuId + " is not valid");
        log.println();
      }

      Trails trailUnit = area.getTrailUnit(trailId);
      if (trailUnit == null) {
        log.println(line);
        log.println("In Vegetation-Trails Trail Id: " + trailId + " is not valid");
        log.println();
      }

      if (evu != null && trailUnit != null) {
        evu.addAssociatedTrailUnit(trailUnit);
        trailUnit.addAssociatedVegUnit(evu);
      }

      // Get the next line.
      line = in.readLine();
    }
    return true;
  }
}



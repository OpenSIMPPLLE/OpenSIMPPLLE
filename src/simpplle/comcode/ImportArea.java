package simpplle.comcode;

import java.io.*;
import java.util.*;

import simpplle.comcode.element.ExistingAquaticUnit;
import simpplle.comcode.element.Trails;
import simpplle.gui.ElevationRelativePosition;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>The purpose of this class is to read an input file
 * that provides information needed to create a new area.
 * <p>This class handles all aspects of creating the new area
 * including verifying and correcting states if necessary.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 */

public class ImportArea {
  private static final int EVU = 0;
  private static final int ELU = 1;
  private static final int ERU = 2;

  private boolean hasAttributes;

  public ImportArea() {
  }

  public boolean attributesAdded() {
    return hasAttributes;
  }
/**
 * reads in area id, adjacent area id's position(above, below, next to), wind value (down wind, no wind)
 * evu id, 
 *  @param area the current area whose neighbors are defined in this method
 * @param fin
 * @param logFile 
 * @return false if no area file, true if there is a file (sets neighbor information as well if true)
 * @throws ParseError caught in gui
 * @throws IOException caught in gui
 */
  private boolean readNeighbors(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    HashMap<Integer, simpplle.comcode.element.Evu>  unitHm = new HashMap<Integer, simpplle.comcode.element.Evu>();
    simpplle.comcode.element.Evu[]               allEvu;
    int                 maxEvuId = -1, i, id, adjId, index;
    String              posStr, windStr;
    char                pos, wind;

    line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

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

      if (pos != simpplle.comcode.element.Evu.ABOVE && pos != simpplle.comcode.element.Evu.BELOW && pos != simpplle.comcode.element.Evu.NEXT_TO) {
        logFile.println(line);
        logFile.println("   " + pos + " is not a valid position value.");
        logFile.println("   Valid values are:  A, B, N");
        return false;
      }
      if (wind != simpplle.comcode.element.Evu.DOWNWIND && wind != simpplle.comcode.element.Evu.NO_WIND) {
        logFile.println(line);
        logFile.println("   " + wind + " is not a valid wind value.");
        logFile.println("   Valid values are:  D, N");
        return false;
      }

      simpplle.comcode.element.Evu evu = unitHm.get(id);
      if (evu == null) {
        evu = new simpplle.comcode.element.Evu(id);
        unitHm.put(id, evu);
      }
      area.addAdjacentData(evu,adjId,pos,wind);

      // Get the next line.
      line = fin.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    allEvu = new simpplle.comcode.element.Evu[maxEvuId+1];

    for (simpplle.comcode.element.Evu evu : unitHm.values()) {
      id = evu.getId();
      allEvu[id] = evu;
    }

    area.setMaxEvuId(maxEvuId);
    area.setAllEvu(allEvu);
    area.finishAddingAdjacentData(logFile);

    return true;
  }
/**
 * reads in area information for new neighbors: area id, adjacent area id, elevation, existing land units, position (valid are N = next to, A =above, or B =below), 
 * wind (valid are D =downwind or N =no wind), evu, and then sets evu elevation.  Adds all info to current area.  
 * Also checks elevation to make sure it is valid.
 * @param area the current area whose neighbors are defined in this method 
 * @param fin
 * @param logFile 
 * @return false if no area file found, true otherwise (sets neighbor information as well if true)
 * @throws ParseError caught in GUI
 * @throws IOException caught in GUI
 */
  private boolean readNeighborsNew(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    HashMap<Integer, simpplle.comcode.element.Evu>  unitHm = new HashMap<Integer, simpplle.comcode.element.Evu>();
    simpplle.comcode.element.Evu[]               allEvu;
    int                 maxEvuId = -1, i, id, adjId, index;
    String              windStr;
    char                pos='N', wind;
    int                 elevation;

    line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }
      if (id > maxEvuId) { maxEvuId = id; }
      if (adjId > maxEvuId) { maxEvuId = adjId; }

      str = strTok.getToken();
      try {
        elevation = Integer.parseInt(str);
      }
      catch (NumberFormatException ex) {
        elevation = simpplle.comcode.element.NaturalElement.INVALID_ELEV;
      }

      if (elevation == simpplle.comcode.element.NaturalElement.INVALID_ELEV) {
        String posStr = str;
        if (posStr.length() > 1) {
          index = posStr.lastIndexOf('\'');
          if (index == -1) {
            index = posStr.length() - 1;
          }
          posStr = posStr.substring(1, index);
        }
        if (posStr.length() > 1) {
          logFile.println(line);
          logFile.println("   " + posStr + " is not a valid position or Elevation");
          logFile.println("   Valid values are:  N, A, B or Integer Elevation");
          return false;
        }
        pos = posStr.charAt(0);

        if (pos != simpplle.comcode.element.Evu.ABOVE && pos != simpplle.comcode.element.Evu.BELOW && pos != simpplle.comcode.element.Evu.NEXT_TO) {
          logFile.println(line);
          logFile.println("   " + pos + " is not a valid position or Elevation value.");
          logFile.println("   Valid values are:  A, B, N or Integer Elevation");
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
        logFile.println(line);
        logFile.println("   " + windStr + " is not valid wind value");
        logFile.println("   Valid values are:  D, N");
        return false;
      }
      else if (windStr.length() == 0) {
        windStr = "N";
      }
      wind = windStr.charAt(0);

      if (wind != simpplle.comcode.element.Evu.DOWNWIND && wind != simpplle.comcode.element.Evu.NO_WIND) {
        logFile.println(line);
        logFile.println("   " + wind + " is not a valid wind value.");
        logFile.println("   Valid values are:  D, N");
        return false;
      }

      simpplle.comcode.element.Evu evu = unitHm.get(id);
      if (evu == null) {
        evu = new simpplle.comcode.element.Evu(id);
        unitHm.put(id, evu);

        // Need only set elevation once.
        evu.setElevation(elevation);
      }
      
      area.addAdjacentData(evu,adjId,pos,wind);

      // Get the next line.
      line = fin.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    allEvu = new simpplle.comcode.element.Evu[maxEvuId+1];

    for (simpplle.comcode.element.Evu evu : unitHm.values()) {
      id = evu.getId();
      allEvu[id] = evu;
    }

    area.setMaxEvuId(maxEvuId);
    area.setAllEvu(allEvu);
    area.finishAddingAdjacentData(logFile);
    return true;
  }
/**
 * 
 * @param area
 * @param fin
 * @param logFile
 * @return
 * @throws ParseError
 * @throws IOException
 */
  private boolean readLandNeighbors(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    simpplle.comcode.element.ExistingLandUnit elu = null, adjElu = null;
    HashMap             units = new HashMap();
    simpplle.comcode.element.ExistingLandUnit[]  allElu;
    int                 maxId = -1, i, id, adjId, index;
    int                 elevation;
    Integer             idObj, adjIdObj;

    line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }
      if (id > maxId) { maxId = id; }
      if (adjId > maxId) { maxId = adjId; }

      elevation = strTok.getIntToken();
      if (elevation == -1) {
        logFile.println(line);
        logFile.println("  Elevation in the above line is invalid");
        logFile.println();
        return false;
      }

      idObj = new Integer(id);
      elu = (simpplle.comcode.element.ExistingLandUnit)units.get(idObj);
      if (elu == null) {
        elu = new simpplle.comcode.element.ExistingLandUnit(id);
        units.put(idObj,elu);
      }

      adjIdObj = new Integer(adjId);
      adjElu = (simpplle.comcode.element.ExistingLandUnit)units.get(adjIdObj);
      if (adjElu == null) {
        adjElu = new simpplle.comcode.element.ExistingLandUnit(adjId);
        units.put(adjIdObj,adjElu);
      }

      elu.addNeighbor(adjElu);
      elu.setElevation(elevation);

      // Get the next line.
      line = fin.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    allElu = new simpplle.comcode.element.ExistingLandUnit[maxId+1];

    Iterator it = units.keySet().iterator();
    while (it.hasNext()) {
      idObj = (Integer)it.next();
      elu   = (simpplle.comcode.element.ExistingLandUnit)units.get(idObj);
      allElu[elu.getId()] = elu;
    }

    area.setAllElu(allElu);

    return true;
  }
/**
 * Reads in the aquatic neighbors of a specified area.  Area id, adjacent id, flow (P or S or N = no flow)   
 * Sets existing aquatic unit.  Sets the predecessor, and successor units.  
 * @param area the current area whose neighbors are defined in this method.  
 * @param fin
 * @param logFile
 * @return false if no area file found, true otherwise (sets aquatic neighbor information as well if true)
 * @throws ParseError caught in GUI
 * @throws IOException caught in GUI
 */
  private boolean readAquaticNeighbors(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    simpplle.comcode.element.ExistingAquaticUnit eau = null, adjEau = null;
    HashMap             units = new HashMap();
    simpplle.comcode.element.ExistingAquaticUnit[] allEau;
    int                 maxId = -1, id, adjId, index;
    Integer             idObj, adjIdObj;
    String              flowStr;
    char                flow;

    line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
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
        logFile.println(line);
        logFile.println("   " + flowStr + " is not a valid Flow Direction");
        logFile.println("   Valid values are:  P, S, N");
        return false;
      }
      flow = flowStr.charAt(0);

      idObj = new Integer(id);
      eau = (simpplle.comcode.element.ExistingAquaticUnit)units.get(idObj);
      if (eau == null) {
        eau = new simpplle.comcode.element.ExistingAquaticUnit(id);
        units.put(idObj,eau);
      }

      if (adjId != -9999) {
        adjIdObj = new Integer(adjId);
        adjEau = (simpplle.comcode.element.ExistingAquaticUnit) units.get(adjIdObj);
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
      line = fin.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    allEau = new simpplle.comcode.element.ExistingAquaticUnit[maxId+1];

    Iterator it = units.keySet().iterator();
    while (it.hasNext()) {
      idObj = (Integer)it.next();
      eau   = (simpplle.comcode.element.ExistingAquaticUnit)units.get(idObj);
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
          simpplle.comcode.element.ExistingAquaticUnit unit = (simpplle.comcode.element.ExistingAquaticUnit)preds.get(a);
          for (int b=a+1; b<preds.size(); b++) {
            simpplle.comcode.element.ExistingAquaticUnit unit2 = (simpplle.comcode.element.ExistingAquaticUnit)preds.get(b);

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
   * Reads in the neighboring roads to a specified area.  Sets Area id, adjacent area id, road unit id, adjacent road unit id.
   * Also creates an array of roads.  
   * @param area the current area whose road neighbors are defined in this method.  
   * @param fin
   * @param logFile
   * @return false if no area file found, true otherwise (sets road neighbor information as well if true)
   * @throws ParseError caught in GUI
   * @throws IOException caught in GUi
   */
  private boolean readRoadNeighbors(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    HashMap<Integer, simpplle.comcode.element.Roads> units = new HashMap<Integer, simpplle.comcode.element.Roads>();
    int                    maxId = -1;

    String line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      int   id, adjId;
      simpplle.comcode.element.Roads roadUnit, adjRoadUnit;

      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      simpplle.comcode.utility.StringTokenizerPlus strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }
      if (id > maxId) { maxId = id; }
      if (adjId > maxId) { maxId = adjId; }

      roadUnit = units.get(id);
      if (roadUnit == null) {
        roadUnit = new simpplle.comcode.element.Roads(id);
        units.put(id,roadUnit);
      }

      if (adjId != -9999) {
        adjRoadUnit = units.get(adjId);
        if (adjRoadUnit == null) {
          adjRoadUnit = new simpplle.comcode.element.Roads(adjId);
          units.put(adjId, adjRoadUnit);
        }
      }
      // Get the next line.
      line = fin.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    simpplle.comcode.element.Roads[] allRoads = new simpplle.comcode.element.Roads[maxId+1];
    simpplle.comcode.element.Roads roadUnit;
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
 * @param fin
 * @param logFile
 * @returnfalse false if no area file found, true otherwise (sets trail neighbor information as well if true)
 * @throws ParseError caught in GUI
 * @throws IOException caught in GUI
 */
  private boolean readTrailNeighbors(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    HashMap<Integer, simpplle.comcode.element.Trails> units = new HashMap<Integer, simpplle.comcode.element.Trails>();
    int                    maxId = -1;

    String line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      int   id, adjId;
      Trails trailUnit, adjTrailUnit;

      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      simpplle.comcode.utility.StringTokenizerPlus strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      adjId = strTok.getIntToken();
      if (id == -1 || adjId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }
      if (id > maxId) { maxId = id; }
      if (adjId > maxId) { maxId = adjId; }

      trailUnit = units.get(id);
      if (trailUnit == null) {
        trailUnit = new simpplle.comcode.element.Trails(id);
        units.put(id,trailUnit);
      }

      if (adjId != -9999) {
        adjTrailUnit = units.get(adjId);
        if (adjTrailUnit == null) {
          adjTrailUnit = new simpplle.comcode.element.Trails(adjId);
          units.put(adjId, adjTrailUnit);
        }
      }
      // Get the next line.
      line = fin.readLine();
    }
    if (line == null) {
      hasAttributes = false;
    }
    else {
      hasAttributes = true;
    }

    simpplle.comcode.element.Trails[] allTrails = new simpplle.comcode.element.Trails[maxId+1];
    simpplle.comcode.element.Trails trailUnit;
    for (Integer keyId : units.keySet()) {
      trailUnit = units.get(keyId);
      allTrails[trailUnit.getId()] = trailUnit;

    }

    area.setAllTrails(allTrails);

    return true;
  }
/**
 * Reads in the Aquatic vegetation relations of a specified area.  Sets EVU id, upland adjacent value (valid are A = adjacent or U Upland), 
 * gets the existing aquatic unit for this area and adds the evu to it.
 * @param area the current area whose Aquatic vegetation relations are defined in this method.
 * @param fin
 * @param logFile
 * @returnfalse if no area file found, true otherwise (sets Aquatic vegetation relations information as well if true)
 * @throws ParseError caught in GUI
 * @throws IOException caught in GUI
 */
  private boolean readAquaticVegRelations(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    simpplle.comcode.element.ExistingAquaticUnit eau = null;
    simpplle.comcode.element.Evu evu = null;
    int                 i, id, evuId, index;
    String              upAdjStr;
    char                upAdj;

    line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      evuId = strTok.getIntToken();
      if (id == -1 || evuId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
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
        logFile.println(line);
        logFile.println("   " + upAdjStr + " is not a Upland/Adjacent Value");
        logFile.println("   Valid values are:  A, U");
        return false;
      }
      upAdj = upAdjStr.charAt(0);

      eau = area.getEau(id);
      if (eau == null) {
        eau = new simpplle.comcode.element.ExistingAquaticUnit(id);
        area.setEau(eau);
      }

      evu = area.getEvu(evuId);
      if (evu == null) {
        logFile.println(line);
        logFile.println("Vegetative Unit " + evuId + " is not valid");
        logFile.println();
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
      line = fin.readLine();
    }
    return true;
  }
/**
 * Reads in Vegetative Land Relations of a specified Area.  Reads evu ID, elu ID, then sets the evu's associated land unit to the elu and sets the elu's associated evu to the evuID
 * @param area the current area whose Vegetative Land Relations are defined in this method.
 * @param fin
 * @param logFile
 * @return false if no area file found, true otherwise (sets Vegetative Land Relations information as well if true)
 * @throws ParseError caught in GUI
 * @throws IOException caught in GUI
 */
  private boolean readVegLandRelations(Area area, BufferedReader fin,
                                       PrintWriter logFile)
      throws ParseError, IOException
  {
    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    simpplle.comcode.element.ExistingLandUnit elu = null;
    simpplle.comcode.element.Evu evu = null;
    int                 evuId, eluId;

    line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      evuId = strTok.getIntToken();
      eluId = strTok.getIntToken();
      if (evuId == -1 || eluId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }

      evu = area.getEvu(evuId);
      if (evu == null) {
        logFile.println(line);
        logFile.println("In Vegetation-Landform Evu-" + evuId + " is not valid");
        logFile.println();
      }

      elu = area.getElu(eluId);
      if (elu == null) {
        logFile.println(line);
        logFile.println("In Vegetation-Landform Elu-" + eluId + " is not valid");
        logFile.println();
      }

      if (evu != null && elu != null) {
        evu.addAssociatedLandUnit(elu);
        elu.addAssociatedVegUnit(evu);
      }

      // Get the next line.
      line = fin.readLine();
    }
    return true;
  }
/**
 * Reads the Vegetative Road Relations for a specified Area.  Reads in road Id, and evu ID,
 * adds the adds the evu as an associated vegetative unit to the road and adds the roads as associated road units to the evu 
 * @param area the current area whose Vegetative Road  Relations are defined in this method.
 * @param fin
 * @param logFile
 * @returnfalse if no area file found, true otherwise (sets Vegetative Road Relations information as well if true)
 * @throws ParseError caught in GUI
 * @throws IOException caught in GUi
 */
  private boolean readVegRoadRelations(Area area, BufferedReader fin,
                                       PrintWriter logFile)
      throws ParseError, IOException
  {
    String line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      simpplle.comcode.utility.StringTokenizerPlus strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      int roadId = strTok.getIntToken();
      int evuId = strTok.getIntToken();
      if (evuId == -1 || roadId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }

      simpplle.comcode.element.Evu evu = area.getEvu(evuId);
      if (evu == null) {
        logFile.println(line);
        logFile.println("In Vegetation-Roads Evu-" + evuId + " is not valid");
        logFile.println();
      }

      simpplle.comcode.element.Roads roadUnit = area.getRoadUnit(roadId);
      if (roadUnit == null) {
        logFile.println(line);
        logFile.println("In Vegetation-Roads Road Id: " + roadId + " is not valid");
        logFile.println();
      }

      if (evu != null && roadUnit != null) {
        evu.addAssociatedRoadUnit(roadUnit);
        roadUnit.addAssociatedVegUnit(evu);
      }

      // Get the next line.
      line = fin.readLine();
    }
    return true;
  }
/**
 * Reads in and sets the vegetative trail relations of a specified Area.  Read in are the trial ID's, evu ID's 
 * then the evu is assigned the associated trail units (trail ID) and the trails are assigned the associated evu
 * @param area the current area whose Vegetative Trail Relations are defined in this method.
 * @param fin
 * @param logFile
 * @returnfalse if no area file found, true otherwise (sets Vegetative Trail Relations information as well if true)
 * @throws ParseError caught in GUI
 * @throws IOException caught in GUI
 */
  private boolean readVegTrailRelations(Area area, BufferedReader fin,
                                        PrintWriter logFile)
      throws ParseError, IOException
  {
    String line = fin.readLine();
    if (line == null) {
      logFile.println("Invalid New Area File (file is empty))");
      logFile.println();
      return false;
    }

    while (line != null && line.trim().equals("END") == false) {
      if (line.trim().length() == 0) { line = fin.readLine(); continue; }
      simpplle.comcode.utility.StringTokenizerPlus strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

      int trailId = strTok.getIntToken();
      int evuId = strTok.getIntToken();
      if (evuId == -1 || trailId == -1) {
        logFile.println(line);
        logFile.println("  One of the id's in above line is invalid");
        logFile.println();
        return false;
      }

      simpplle.comcode.element.Evu evu = area.getEvu(evuId);
      if (evu == null) {
        logFile.println(line);
        logFile.println("In Vegetation-Trails Evu-" + evuId + " is not valid");
        logFile.println();
      }

      simpplle.comcode.element.Trails trailUnit = area.getTrailUnit(trailId);
      if (trailUnit == null) {
        logFile.println(line);
        logFile.println("In Vegetation-Trails Trail Id: " + trailId + " is not valid");
        logFile.println();
      }

      if (evu != null && trailUnit != null) {
        evu.addAssociatedTrailUnit(trailUnit);
        trailUnit.addAssociatedVegUnit(evu);
      }

      // Get the next line.
      line = fin.readLine();
    }
    return true;
  }
/**
 * Check to see if this has both a row & a column.  
 * Note: if Field #5 is a number than we have row & col, however if it is a string it is not a row & col
 * @param line
 * @return true if have both a row and column (field #5 is a number), false if do not have a row & col (field #5 is a string)
 * @throws ParseError is caught in GUI, if trouble parsing line, however if it throws a parse error on field #5 while expecting a number it means we have a string which is caught here
 */
  private boolean hasRowCol(String line) throws ParseError {
    simpplle.comcode.utility.StringTokenizerPlus strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");

    // If field #5 is a number than we have row & col,
    // however if it is a string we do not have row & col.
    String str;
    try {
      str = strTok.getToken();
      str = strTok.getToken();
      str = strTok.getToken();
      str = strTok.getToken();
    }
    catch (ParseError e) {
      throw new ParseError(e.getMessage() + "\nError parsing line: " + line);
    }

    try {
      // Field #5: Either ACRES or SPECIES
      str = strTok.getToken();
      if (str == null) {
        throw new ParseError("Not enough fields in line: " + line);
      }
      float value = Float.valueOf(str).floatValue();
      // if no exception, than field #5 was a number.
      return true;
    }
    catch (NumberFormatException err) {
      return false;
    }
  }
/**
 * Checks to see if the string passed contains digits throughout.  If so it substrings from 0 to index-1
 * @param str string to be checked if has digits
 * @return a digit in string form, designates size class
 */
  private String parseSizeClass(String str) {
    if (str == null) { return str; }

    boolean foundNumber = false;

    int i = 0;
    while (i < str.length() && foundNumber == false) {
      foundNumber = Character.isDigit(str.charAt(i));
      i++;
    }

    if (foundNumber) {
      return str.substring(0,(i-1));
    }
    else { return str; }
  }
/**
 * Parses a passed string which is converted to an int and designates age
 * @param size
 * @return the age parsed from passed string
 * @throws NumberFormatException - caught here, used to designate if age is 1
 */
  private int parseAge(String size) {
    if (size == null) { return 1; }

    boolean foundNumber = false;
    int     age = 1;

    int i = 0;
    while (i < size.length() && foundNumber == false) {
      foundNumber = Character.isDigit(size.charAt(i));
      i++;
    }

    if (foundNumber) {
      i--;
      try {
        age = Integer.parseInt(size.substring(i));
      }
      catch (NumberFormatException NFE) {
        age = 1;
      }
    }
    return age;
  }
/**
 *  
 *  Attributes of an area, they are stored in the file in the following order:
 * +slink, ++row#, ++col#, +unit#, +acres, +htgrp, +species, +size class,
 * +density, ownership, road status, ignition prob,
 * fmz, special area, landtype, initial process, initial Treatment
 * Longitude, Latitude.
 * +  = Required Field
  * ++ = Required only in GRASSLAND Zone.
 *
 * @param area whose attributes are being read in.
 * @param fin
 * @param logFile
 * @throws ParseError line where parse error occurred is added in message caught in GUI
 * @throws IOException caught in GUI
 */
  private void readAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
   

    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    int                 value, count;
    float               fValue;
    simpplle.comcode.element.Evu evu;
    RegionalZone        zone = Simpplle.getCurrentZone();
    HabitatTypeGroup    htGrp;
    VegetativeType      state = null;
    String              speciesStr, sizeClassStr, densityStr;
    int                 age, id;
    ProcessType         process;
    Treatment           treatment;
    TreatmentType       treatType;
    Fmz                 fmz;
    StringBuffer        strBuf;
    int                 begin, index;
    int                 numReqFields = -1;
    boolean             hasRowCol=false;

    boolean             processedAsMultipleLife=false;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = simpplle.comcode.utility.Utility.preProcessInputLine(line);

        if (numReqFields == -1) {
          // Find out if we have row and col.
          hasRowCol = hasRowCol(line);
          numReqFields = (hasRowCol) ? 9 : 7;
        }

        strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          if (zone.getId() == ValidZones.GRASSLAND) {
            logFile.println("     slink, row #, col #, unit #, acres,");
            logFile.println("     habitat type group, species, size class, density");
          }
          else {
            logFile.println("     slink, unit #, acres,");
            logFile.println("     habitat type group, species, size class, density");
            logFile.println("   note: (row, col) can optionally be placed after slink:");
            logFile.println("    (e.g. slink, row, col, unit #, acres, ...");
          }
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the evu id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        evu = area.getEvu(id);
        if (evu == null) {
          logFile.println(line);
          logFile.println("  An Evu with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        if (hasRowCol) {
          // get the Location
          // Note: not all areas will have x,y values
          //       so we don't really need to check anything.
          //       Also, location is not really used right now.
          int row, col;
          row = Math.round(strTok.getFloatToken());
          col = Math.round(strTok.getFloatToken());

          evu.setLocationX(col);

          evu.setLocationY(row);
        }

        // Get the Unit Number (which is actually a string)
        str = strTok.getToken();
        if (str == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + ":");
          logFile.println("  The Unit number is missing (a required field)");
          logFile.println("  The value \"UNKNOWN\" was assigned");

          str = "UNKNOWN";
        }
        str = str.trim();
        evu.setUnitNumber(str);

        // Get the Acres
        fValue  = strTok.getFloatToken();
        evu.setAcres(fValue);
        // Acres check is done later, because we need to know species for check.

        // Get the Habitat Type Group
        str = strTok.getToken();
        str = (str != null) ? str.trim().toUpperCase() : "UNKNOWN";
        htGrp = HabitatTypeGroup.findInstance(str);
        if (htGrp == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + ":");
          logFile.println("  " + htGrp + " is not a valid Ecological Grouping.");
          logFile.println("  The value \"" + str + "\" was assigned");
          logFile.println();

          // Need to set this anyway so that user can correct it later.
          evu.setHabitatTypeGroup(new HabitatTypeGroup(str));
        }
        else {
          evu.setHabitatTypeGroup(htGrp);
        }

        // Get the species, size class, and density;
        boolean newFileFormat=false;
        speciesStr = strTok.getToken();
        if (speciesStr != null && speciesStr.equals("#")) {
          processedAsMultipleLife = true;
          area.setDisableMultipleLifeforms(false);
          readMultiLifeformState(logFile,strTok,line,evu,htGrp);
          newFileFormat = true;
        }
        else {

          if (speciesStr == null) {
            speciesStr = "UNKNOWN";
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Species value is missing.");
            logFile.println("  The value \"UNKNOWN\" was assigned");
            logFile.println();
          }
          else {
            speciesStr = speciesStr.trim().toUpperCase();
          }

          str = strTok.getToken();
          if (str != null) {
            str = str.trim().toUpperCase();
          }
          sizeClassStr = parseSizeClass(str);
          if (sizeClassStr == null) {
            sizeClassStr = "UNKNOWN";
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Size Class value is missing.");
            logFile.println("  The value \"UNKNOWN\" was assigned");
            logFile.println();
          }
          age = parseAge(str);

          densityStr = strTok.getToken();
          if (densityStr == null) {
            densityStr = "UNKNOWN";
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Density value is missing.");
            logFile.println("  The value \"UNKNOWN\" was assigned");
            logFile.println();
          }
          Species species = Species.get(speciesStr);
          if (species == null) {
            species = new Species(speciesStr);
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Species \"" + speciesStr +
                            "\" is unknown");
          }

          SizeClass sizeClass = SizeClass.get(sizeClassStr);
          if (sizeClass == null) {
            sizeClass = new SizeClass(sizeClassStr);
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Size Class \"" + sizeClassStr +
                            "\" is unknown");
          }

          Density density = Density.get(densityStr);
          if (density == null) {
            density = new Density(densityStr);
            logFile.println(line);
            logFile.println("  In Evu-" + id + " Density \"" + densityStr +
                            "\" is unknown");
          }

          state = null;
          if (species != null && sizeClass != null && density != null && htGrp != null) {
            state = htGrp.getVegetativeType(species, sizeClass, age, density);
          }
          if (state == null) {
            logFile.println(line);
            logFile.println("  In Evu-" + id + "Could not build a valid state.");
            logFile.println("  One or more of the following must be invalid:");
            logFile.println(
                "  Species, Size Class, Density, or Ecological Grouping");
            logFile.println();

            // Even invalid states need to be set in the evu, in order to
            // allow for later correction.
            state = new VegetativeType(species, sizeClass, age, density);
            evu.setState(state,Climate.Season.YEAR);
          }
          else {
            evu.setState(state,Climate.Season.YEAR);
          }

          // In case there is not Initial Process.
          evu.setInitialProcess(simpplle.comcode.element.Evu.getDefaultInitialProcess());
        }

        // Acres check depends on species being available.
        if (evu.isAcresValid() == false) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + " Acres is Invalid");
          logFile.println("  Acres must be a value greater than 0.0");
          logFile.println("    - acres can be equal to 0 if species=ND");
          logFile.println();
        }

        // End of required fields
        if (count == numReqFields) { line = fin.readLine(); continue; }

        // Get the Ownership information (if any)
        str = strTok.getToken();
        evu.setOwnership(str);
        if (count == (numReqFields+1)) { line = fin.readLine(); continue; }

        // Get the Road Status information (if any)
        str = strTok.getToken();
        if (str != null) { str = str.trim().toUpperCase(); }

        if (str == null) {
          evu.setRoadStatus(simpplle.comcode.element.Roads.Status.UNKNOWN);
        }
        else {
          evu.setRoadStatus(simpplle.comcode.element.Roads.Status.lookup(str));
        }
        if (count == (numReqFields+2)) { line = fin.readLine(); continue; }

        // Get the Ignition Prob
        // value not used anymore.
        str = strTok.getToken();
        evu.setIgnitionProb(0);

        if (count == (numReqFields+3)) { line = fin.readLine(); continue; }

        // Get the Fmz
        str = strTok.getToken();
        if (str == null) {
          fmz = zone.getDefaultFmz();
        }
        else {
          fmz = zone.getFmz(str);
        }
        if (fmz == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + id + ":");
          logFile.println("  " + str + " is not a valid Fire Management Zone.");
          logFile.println();

          evu.setFmz(new Fmz(str));
        }
        else {
          evu.setFmz(fmz);
        }
        if (count == (numReqFields+4)) { line = fin.readLine(); continue; }

        // Get the Special Area
        str = strTok.getToken();
        evu.setSpecialArea(str);
        if (count == (numReqFields+5)) { line = fin.readLine(); continue; }

        // Read Landtype
        str = strTok.getToken();
        evu.setLandtype(str);
        if (count == (numReqFields+6)) { line = fin.readLine(); continue; }

        if (!newFileFormat) {
          // Read Initial Process
          str = strTok.getToken();
          if (str == null) {
            str = simpplle.comcode.element.Evu.getDefaultInitialProcess().toString();
          }
          process = ProcessType.get(str.toUpperCase());
          if (process == null) {
            logFile.println(line);
            logFile.println("  In Evu-" + id + ":");
            logFile.println("  " + str + " is not a Process.");
            logFile.println();

            evu.setInitialProcess(str);
          }
          else {
            evu.setInitialProcess(process);
          }
          if (count == (numReqFields + 7)) { line = fin.readLine(); continue; }

          // Read Initial Treatment
          str = strTok.getToken();
          if (str != null) {
            treatType = TreatmentType.get(str.toUpperCase());
            if (treatType == null) {
              logFile.println(line);
              logFile.println("  In Evu-" + id + ":");
              logFile.println("  " + str + " is not a Treatment.");
              logFile.println();
            }
            else {
              /** TODO need to update state later if it is an invalid one. */
              /** TODO carry process & treatment as initial when make sim ready */
              treatment = Treatment.createInitialTreatment(treatType, state);
              evu.addTreatment(treatment);
            }
          }
        }
        else {
          double longitude = strTok.getDoubleToken(Double.NaN); // POINT_X Field
          evu.setLongitude(longitude);
          if (count == (numReqFields + 7)) { line = fin.readLine(); continue; }

          double latitude = strTok.getDoubleToken(Double.NaN); // POINT_Y Field
          evu.setLatitude(latitude);
        }

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
    area.updateArea();

    area.setMultipleLifeformStatus();
    if ((area.multipleLifeformsEnabled() == false) &&
        processedAsMultipleLife) {
      // Need to change Evu's to be single lifeform;
      simpplle.comcode.element.Evu[] evus = area.getAllEvu();
      for (int i=0; i<evus.length; i++) {
        if (evus[i] != null) { evus[i].makeSingleLife(); }
      }
    }
  }
/**
 * Reads in the Multi Life Form State.  Sets species, size Class, and density 
 * @param logFile
 * @param strTok
 * @param line
 * @param evu
 * @param htGrp
 * @throws ParseError caught in GUI
 */
  private void readMultiLifeformState(PrintWriter logFile,
                                      simpplle.comcode.utility.StringTokenizerPlus strTok,
                                      String line, simpplle.comcode.element.Evu evu,
                                      HabitatTypeGroup htGrp) throws ParseError {
    String              str;
    VegetativeType      vegState = null;
    String              speciesStr, sizeClassStr=null, densityStr;
    int                 age=1;
    ProcessType         process;
    Treatment           treatment;
    TreatmentType       treatType;
    boolean             emptyState=false;

    int numLifeforms = Math.round(strTok.getFloatToken());
    if (numLifeforms == -1) {
      logFile.println(line);
      logFile.println("  In Evu-" + evu.getId() + " Number of Lifeforms is missing.");
      logFile.println();
      throw new ParseError("Data for Evu-" + evu.getId() + " Missing, see logfile");
    }

    for (int lf=0; lf<numLifeforms; lf++) {
      speciesStr   = null;
      sizeClassStr = null;
      densityStr   = null;

      VegSimStateData state = new VegSimStateData(evu.getId());

      speciesStr = strTok.getToken();
      if (speciesStr != null) {
        speciesStr = speciesStr.trim().toUpperCase();
      }

      str = strTok.getToken();
      if (str != null) {
        str = str.trim().toUpperCase();
        sizeClassStr = parseSizeClass(str);
        age = parseAge(str);
      }

      densityStr = strTok.getToken();

      vegState = null;
      emptyState = (speciesStr == null && sizeClassStr == null && densityStr == null);


      if (!emptyState)
      {
        if (speciesStr == null) { speciesStr = "UNKNOWN"; }
        if (sizeClassStr == null) { sizeClassStr = "UNKNOWN"; }
        if (densityStr == null) { densityStr = "UNKNOWN"; }

        Species species = Species.get(speciesStr, true);
        SizeClass sizeClass = SizeClass.get(sizeClassStr, true);
        Density density = Density.get(densityStr, true);

        if (species != null && sizeClass != null && density != null && htGrp != null) {
          vegState = htGrp.getVegetativeType(species, sizeClass, age, density);
        }

        if (vegState == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + evu.getId() +
                          "Could not build a valid state.");
          logFile.println("  One or more of the following must be invalid:");
          logFile.println("  Species, Size Class, Density, or Ecological Grouping");
          logFile.println();

          // Even invalid states need to be set in the evu, in order to
          // allow for later correction.
          vegState = new VegetativeType(species, sizeClass, age, density);
        }
        state.setVegType(vegState);
      }
      // Skip the rest of the tokens.
      if (emptyState) {
        strTok.nextToken();
        strTok.nextToken();
        strTok.nextToken();
        strTok.nextToken();
      }

      if (!emptyState)
      {
        // Read Initial Process
        str = strTok.getToken();
        if (str == null) {
          str = simpplle.comcode.element.Evu.getDefaultInitialProcess().toString();
        }

        process = ProcessType.get(str.toUpperCase());
        if (process == null) {
          logFile.println(line);
          logFile.println("  In Evu-" + evu.getId() + ":");
          logFile.println("  " + str + " is not a Process.");
          logFile.println();

          process = ProcessType.makeInvalidInstance(str);
        }
        state.setProcess(process);

        // Read process time Steps in past.
        int ts = Math.round(strTok.getFloatToken());

        // Read Initial Treatment
        str = strTok.getToken();
        if (str != null) {
          treatType = TreatmentType.get(str.toUpperCase());
          if (treatType == null) {
            logFile.println(line);
            logFile.println("  In Evu-" + evu.getId() + ":");
            logFile.println("  " + str + " is not a Treatment.");
            logFile.println();
          }
          else {
            /** TODO need to update state later if it is an invalid one. */
            /** TODO carry process & treatment as initial when make sim ready */
            treatment = Treatment.createInitialTreatment(treatType, vegState);
            evu.addTreatment(treatment);
          }
        }
        // Read treatment time steps in past
        ts = Math.round(strTok.getFloatToken());
      }

      if (!emptyState) {
        evu.setState(state, Climate.Season.YEAR);
      }

      int numTrackSpecies = Math.round(strTok.getFloatToken());

      // Skip the tracking species info if no valid state information.
      if (emptyState) {
        for (int i=0; i<numTrackSpecies; i++) {
          strTok.nextToken();  // Species
          strTok.nextToken();  // Percent
        }
        continue;
      }

      int percent;
      if (numTrackSpecies != -1) {
        state.initializeTrackingSpecies(numTrackSpecies);
        for (int i = 0; i < numTrackSpecies; i++) {
          str = strTok.getToken();
          if (str != null) {
            str = str.trim().toUpperCase();
          }

          percent = Math.round(strTok.getFloatToken());
          if (str == null || percent == -1) {
            continue;
          }

          InclusionRuleSpecies trk_species = InclusionRuleSpecies.get(str,true);

          Range range = vegState.getSpeciesRange(trk_species);
          if (range != null) {
            if (percent < range.getLower()) {
              percent = range.getLower();
            }
            else if (percent > range.getUpper()) {
              percent = range.getUpper();
            }
          }
          state.addTrackSpecies(trk_species, percent);
        }
        state.addMissingTrackSpecies();
        state.removeInvalidTrackSpecies();
      }

    }
    // This should be the ending #
    str = strTok.getToken();

    // If we have multiple lifeforms they use different columns and the
    // count will always be five.
    if (numLifeforms == 1) {
      evu.makeSingleLife();
    }
  }
  /**
   * reads in Land Attributes.  They are stored in file in the following order: slink (ID), acres, soilType, landform, aspect, slope, parent material, depth
   * @param area
   * @param fin
   * @param logFile
   * @throws ParseError
   * @throws IOException
   */
  private void readLandAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {

    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    simpplle.comcode.element.ExistingLandUnit elu;
    float               fValue;
    int                 numReqFields = 8;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Land Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = simpplle.comcode.utility.Utility.preProcessInputLine(line);

        strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.print("     slink, acres, name, landform,");
          logFile.println("   aspect, slope, parent material");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        elu = area.getElu(id);
        if (elu == null) {
          logFile.println(line);
          logFile.println("  An Existing Land Unit with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the Acres
        fValue  = strTok.getFloatToken();
        elu.setAcres(fValue);
        // Acres check is done later, because we need to know species for check.

        // Get the Soil Type
        str = strTok.getToken();
        if (str != null) {
          elu.setSoilType(SoilType.get(str,true));
        }

        // Get the Landform
        str = strTok.getToken();
        if (str != null) {
          elu.setLandform(str);
        }

        // Get the aspect (can be either a double or String)
        str = strTok.getToken();
        if (str != null) {
          try {
            double value = Double.parseDouble(str);
            elu.setAspect(value);
          }
          catch (NumberFormatException ex) {
            elu.setAspectName(str);
          }
        }

        // Get the slope
        fValue = strTok.getFloatToken(Float.NaN);
        if (Float.isNaN(fValue)) {
          logFile.println(line);
          logFile.println("  In Elu-" + id + " Slope is Invalid");
          logFile.println("  Value of 0.0 assigned");
          logFile.println();
          fValue = 0.0f;
        }
        elu.setSlope(fValue);

        // Get the parent material
        str = strTok.getToken();
        if (str != null) {
          elu.setParentMaterial(str);
        }

        // Get the Depth
        str = strTok.getToken();
        if (str != null) {
          elu.setDepth(str);
        }

        if (strTok.hasMoreTokens()) {
          double longitude = strTok.getDoubleToken(Double.NaN); // POINT_X Field
          elu.setLongitude(longitude);
        }

        if (strTok.hasMoreTokens()) {
          double latitude = strTok.getDoubleToken(Double.NaN); // POINT_Y Field
          elu.setLatitude(latitude);
        }

       // Acres check depends on species being available.
        if (elu.isAcresValid() == false) {
          logFile.println(line);
          logFile.println("  In Elu-" + id + " Acres is Invalid");
          logFile.println("  Acres must be a value greater than 0.0");
          logFile.println();
        }

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
/**
 * Reads in the road attributes.  They are stored in the file in the following order: slink (id), Status, Kind
 * @param area
 * @param fin
 * @param logFile
 * @throws ParseError
 * @throws IOException
 */
  private void readRoadsAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
/*
    public enum Status { OPEN, CLOSED, PROPOSED, ELIMINATED, UNKNOWN };
    public enum Kind { SINGLE_LANE, DOUBLE_LANE, UNIMPROVED, SYSTEM, NONSYSTEM, UNKNOWN};
*/
    // attributes in the file in the following order:
    // slink(id),Status,Kind

    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    simpplle.comcode.element.Roads roadUnit;
    float               fValue;
    int                 numReqFields = 3;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Roads Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = simpplle.comcode.utility.Utility.preProcessInputLine(line);

        strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.println("     slink, Status, Kind");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        roadUnit = area.getRoadUnit(id);
        if (roadUnit == null) {
          logFile.println(line);
          logFile.println("  An Road Unit with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the Status
        simpplle.comcode.element.Roads.Status status = simpplle.comcode.element.Roads.Status.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          status = simpplle.comcode.element.Roads.Status.lookup(str);
          if (status == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Status: " + str);
            logFile.println();
          }
        }
        roadUnit.setStatus(status);

        // Get the Kind
        simpplle.comcode.element.Roads.Kind kind = simpplle.comcode.element.Roads.Kind.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          kind = simpplle.comcode.element.Roads.Kind.valueOf(str);
          if (kind == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Kind: " + str);
            logFile.println();
          }
        }
        roadUnit.setKind(kind);

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
  /**
   * Reads in the trail attributes.  They are stored in the area file in the following order: slink (ID) status, kind 
   * Default status is UNKNOWN, default kind is UNKNOWN
   * @param area
   * @param fin
   * @param logFile
   * @throws ParseError
   * @throws IOException
   */
  private void readTrailsAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
/*
    public enum Status { OPEN, CLOSED, PROPOSED, ELIMINATED, UNKNOWN };
    public enum Kind { HIKE};
*/
    // attributes in the file in the following order:
    // slink(id),Status,Kind

    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    simpplle.comcode.element.Trails trailUnit;
    float               fValue;
    int                 numReqFields = 3;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Trails Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = simpplle.comcode.utility.Utility.preProcessInputLine(line);

        strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.println("     slink, Status, Kind");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        trailUnit = area.getTrailUnit(id);
        if (trailUnit == null) {
          logFile.println(line);
          logFile.println("  An Road Unit with the id: " + id + " does not exist.");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the Status
        simpplle.comcode.element.Trails.Status status = simpplle.comcode.element.Trails.Status.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          status = simpplle.comcode.element.Trails.Status.valueOf(str);
          if (status == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Status: " + str);
            logFile.println();
          }
        }
        trailUnit.setStatus(status);

        // Get the Kind
        simpplle.comcode.element.Trails.Kind kind = simpplle.comcode.element.Trails.Kind.UNKNOWN;
        str = strTok.getToken();
        if (str != null) {
          kind = simpplle.comcode.element.Trails.Kind.valueOf(str);
          if (kind == null) {
            logFile.println(line);
            logFile.println("  Invalid Road Kind: " + str);
            logFile.println();
          }
        }
        trailUnit.setKind(kind);

        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
/**
 * Reads in aquatic attributes of a specified area.  The attributes are stored in a file in the following order:
 * slink (ID), length, lta valley segment group, aquatic class, aquatic attribute, segment #, initial process, status.
 * @param area
 * @param fin
 * @param logFile
 * @throws ParseError
 * @throws IOException
 */
  private void readAquaticAttributes(Area area, BufferedReader fin, PrintWriter logFile) throws ParseError, IOException {
    

    String              line, str;
    simpplle.comcode.utility.StringTokenizerPlus strTok;
    int                 count;
    int                 id;
    simpplle.comcode.element.ExistingAquaticUnit eau;
    float               fValue;
    int                 numReqFields = 7;

    line = fin.readLine();
    if (line == null) {
      logFile.println("No Aquatic Attribute data found in input file.");
      logFile.println();
      hasAttributes = false;
      return;
    }

    while(line != null && line.trim().equals("END") == false) {
      try {
        line = line.trim();
        if (line.length() == 0) { line = fin.readLine(); continue; }

        line = simpplle.comcode.utility.Utility.preProcessInputLine(line);

        strTok = new simpplle.comcode.utility.StringTokenizerPlus(line,",");
        count  = strTok.countTokens();
        if (count < numReqFields) {
          logFile.println(line);
          logFile.println("   Not enough fields in attribute data.");
          logFile.println("   Required fields are:");
          logFile.print("     slink, length, Lta Valley Segment Group,");
          logFile.println("   aquatic class, aquatic attribute,");
          logFile.println("   segment number, initial Process");
          logFile.println();
          line = fin.readLine();
          continue;
        }

        // Get the unit id
        id = strTok.getIntToken();
        if (id == -1) {
          logFile.println(line);
          logFile.println("  Invalid Slink");
          logFile.println();
          continue;
        }
        eau = area.getEau(id);
        if (eau == null) {
          eau = new simpplle.comcode.element.ExistingAquaticUnit(id);
          area.setEau(eau);
          logFile.println(line);
          logFile.println("  Aquatic unit #" + id + " not found in spatialrelate file" +
                          " creating a default(island) unit");
          logFile.println();
        }

        // Get the Length
        fValue  = strTok.getFloatToken();
        eau.setLength(fValue);

        // Get the Lta Valley Segment Group
        str = strTok.getToken();
        str = (str != null) ? str.trim().toUpperCase() : "UNKNOWN";
        LtaValleySegmentGroup group = LtaValleySegmentGroup.findInstance(str);
        if (group == null) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + ":");
          logFile.println("  " + group + " is not a valid Lta Valley Segment Group.");
          logFile.println("  The value \"UNKNOWN\" was assigned");
          logFile.println();

          // Need to set this anyway so that user can correct it later.
          eau.setLtaValleySegmentGroup(new LtaValleySegmentGroup(str));
        }
        else {
          eau.setLtaValleySegmentGroup(group);
        }

        // Get the aquatic class and attribute.
        String aquaticClassStr = strTok.getToken();
        if (aquaticClassStr == null) {
          aquaticClassStr = "UNKNOWN";
          logFile.println(line);
          logFile.println("  In Eau-" + id + " Aquatic Class value is missing.");
          logFile.println("  The value \"UNKNOWN\" was assigned");
          logFile.println();
        }
        else { aquaticClassStr = aquaticClassStr.trim().toUpperCase(); }

        String aquaticAttributeStr = strTok.getToken();
        if (aquaticAttributeStr == null) {
          aquaticAttributeStr = "UNKNOWN";
          logFile.println(line);
          logFile.println("  In Eau-" + id + " Aquatic Attribute value is missing.");
          logFile.println("  The value \"UNKNOWN\" was assigned");
          logFile.println();
        }
        else { aquaticAttributeStr = aquaticAttributeStr.trim().toUpperCase(); }


        AquaticClass aquaticClass = AquaticClass.get(aquaticClassStr);
        if (aquaticClass == null) {
          aquaticClass = new AquaticClass(aquaticClassStr);
          logFile.println(line);
          logFile.println("  In Evu-" + id + " Aquatic Class \"" + aquaticClassStr + "\" is unknown");
        }

        AquaticAttribute aquaticAttribute = AquaticAttribute.get(aquaticAttributeStr);
        if (aquaticAttribute == null) {
          aquaticAttribute = new AquaticAttribute(aquaticAttributeStr);
          logFile.println(line);
          logFile.println("  In Evu-" + id + " Aquatic Attribute \"" + aquaticAttributeStr + "\" is unknown");
        }

        PotentialAquaticState state = null;
        if (aquaticClass != null && aquaticAttribute != null && group != null) {
          state = group.getPotentialAquaticState(aquaticClass,aquaticAttribute);
        }
        if (state == null) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + "Could not build a valid state.");
          logFile.println("  One or more of the following must be invalid:");
          logFile.println("  Aquatic Class, Aquatic Attribute, Lta Valley Segment Group");
          logFile.println();

          // Even invalid states need to be set in the evu, in order to
          // allow for later correction.
          state = new PotentialAquaticState(aquaticClass,aquaticAttribute);
        }
        eau.setCurrentState(state);
        eau.setPotentialAquaticState(state);

        // Get the Segment Number
        int value = strTok.getIntToken();
        if (value == -1) { value = id; }
        eau.setSegmentNumber(value);

        if (eau.isLengthValid() == false) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + " Length is Invalid");
          logFile.println("  Length must be a value greater than 0.0");
          logFile.println();
        }

        // Read Initial Process
        str = strTok.getToken();
        if (str == null) {
          str = simpplle.comcode.element.ExistingAquaticUnit.getDefaultInitialProcess().toString();
        }

        Process process = Process.findInstance(ProcessType.get(str.toUpperCase()));
        if (process == null) {
          logFile.println(line);
          logFile.println("  In Eau-" + id + ":");
          logFile.println("  " + str + " is not a Process.");
          logFile.println();

          eau.setInitialProcess(str);
        }
        else {
          eau.setInitialProcess(process);
        }

        // Read the Status
        str = strTok.getToken();
        if (str == null) {
          eau.setStatus(simpplle.comcode.element.ExistingAquaticUnit.PERENNIAL);
        }
        else if (eau.setStatus(str) == false) {
          eau.setStatus(simpplle.comcode.element.ExistingAquaticUnit.PERENNIAL);
        }


        // Get the Next Line.
        line = fin.readLine();
      }
      catch (ParseError err) {
        String msg = "The following error occurred in this line: \n" +
                     line + "\n" + err.getMessage();
        throw new ParseError(msg);
      }
    }
  }
  public void readAtributesFile(File prefix) throws SimpplleError {
    File file, log, logFile;
    Area area = Simpplle.getCurrentArea();

    hasAttributes = false;

    // Veg Attributes
    file = simpplle.comcode.utility.Utility.makeSuffixedPathname(prefix, "", "attributes");
    logFile = simpplle.comcode.utility.Utility.makeUniqueLogFile(prefix, "vegatt");
    read(area, file, logFile, EVU, true);

    hasAttributes = true;
  }

  public Area importNewFiles(File filename) throws SimpplleError {
    Area newArea = importSpatialRelations(filename);

    File prefix     = simpplle.comcode.utility.Utility.stripExtension(filename);
    File logFile    = simpplle.comcode.utility.Utility.makeUniqueLogFile(prefix, "attrib");
    File attribFile = simpplle.comcode.utility.Utility.makeSuffixedPathname(prefix, "", "attributesall");

    if (attribFile.exists() == false) {
      hasAttributes = false;
    }
    readAttributesNew(newArea,attribFile,logFile);

    // Set the Elevation Relative Position
    // For use in later calculating Above, Below, Next-to
    newArea.setElevationRelativePositionDefault();
    ElevationRelativePosition dlg = new ElevationRelativePosition(simpplle.JSimpplle.getSimpplleMain(),"Elevation Relative Position",true,newArea);
    
    dlg.setVisible(true);
    
    int elevRelativePos = dlg.getValue();
    
    newArea.initPolygonWidth();
    newArea.setElevationRelativePosition(elevRelativePos);
    return newArea;
  }

  private Area importSpatialRelations(File filename) throws SimpplleError {
    PrintWriter    log=null;
    BufferedReader fin=null;
    Area           newArea = new Area(Area.USER);
    boolean        attributesAdded = false;
    File           prefix = simpplle.comcode.utility.Utility.stripExtension(filename);
    File           logFile = simpplle.comcode.utility.Utility.makeUniqueLogFile(prefix,"");
    boolean        success = false;

    try {
      log = new PrintWriter(new FileWriter(logFile));
      fin = new BufferedReader(new FileReader(filename));

      String line = fin.readLine();

      while (line != null) {
        while (line != null && line.trim().length() == 0) {
          line = fin.readLine();
        }
        if (line == null) {
          throw new SimpplleError("File is empty");
        }

        StringTokenizer strTok = new StringTokenizer(line.trim());
        String str = strTok.nextToken();
        if (str == null || str.equalsIgnoreCase("BEGIN") == false) {
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing BEGIN in line:" +
              line);
        }

        str = strTok.nextToken();
        if (str == null) {
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing KEYWORD in line:" +
              line);
        }

        if (str.equalsIgnoreCase("VEGETATION-VEGETATION")) {
          success = readNeighborsNew(newArea, fin, log);
        }
        else if (str.equalsIgnoreCase("LANDFORM-LANDFORM")) {
          success = readLandNeighbors(newArea, fin, log);
        }
        else if (str.equalsIgnoreCase("AQUATIC-AQUATIC")) {
          success = readAquaticNeighbors(newArea, fin, log);
        }
        else if (str.equalsIgnoreCase("VEGETATION-LANDFORM")) {
          success = readVegLandRelations(newArea,fin,log);
        }
        else if (str.equalsIgnoreCase("VEGETATION-AQUATIC")) {
          success = readAquaticVegRelations(newArea,fin,log);
        }
        else if (str.equalsIgnoreCase("ROADS-ROADS")) {
          success = readRoadNeighbors(newArea, fin, log);
        }
        else if (str.equalsIgnoreCase("TRAILS-TRAILS")) {
          success = readTrailNeighbors(newArea, fin, log);
        }
        else if (str.equalsIgnoreCase("VEGETATION-ROADS")) {
          success = readVegRoadRelations(newArea,fin,log);
        }
        else if (str.equalsIgnoreCase("VEGETATION-TRAILS")) {
          success = readVegTrailRelations(newArea,fin,log);
        }
        else {
          line = fin.readLine();
        }

        if (!success) {
          fin.close();
          log.flush();
          log.close();
          throw new SimpplleError("Could not load files. Please check log file for details.");
        }


        line = fin.readLine();
        while (line != null && line.trim().toUpperCase().startsWith("BEGIN") == false) {
          line = fin.readLine();
        }
      }
      fin.close();
      return newArea;
    }
    catch (FileNotFoundException ex) {
      String msg = "Could not open input file: " + filename;
      log.println(msg);
      log.flush();
      log.close();
      throw new SimpplleError(msg);
    }
    catch (ParseError e) {
      String msg = "The following error occurred while trying to create the area:";
      log.println(msg);
      log.println(e.msg);
      log.flush();
      log.close();
      throw new SimpplleError(msg + "\n" + e.msg,e);
    }
    catch (IOException ex) {
      throw new SimpplleError("Could write to log file: " + logFile);
    }
  }

  private void readAttributesNew(Area area, File filename, File logFile) throws SimpplleError {
    PrintWriter    log=null;
    BufferedReader fin=null;

    hasAttributes = false;

    try {
      log = new PrintWriter(new FileWriter(logFile));
      fin = new BufferedReader(new FileReader(filename));

      String line = fin.readLine();
      while (line != null) {
        while (line != null && line.trim().length() == 0) {
          line = fin.readLine();
        }
        if (line == null) {
          log.flush();
          log.close();
          fin.close();
          throw new SimpplleError("File is empty");
        }

        StringTokenizer strTok = new StringTokenizer(line.trim());
        String str = strTok.nextToken();
        if (str == null || str.equalsIgnoreCase("BEGIN") == false) {
          log.flush();
          log.close();
          fin.close();
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing BEGIN in line:" +
              line);
        }

        str = strTok.nextToken();
        if (str == null) {
          log.flush();
          log.close();
          fin.close();
          throw new SimpplleError(
              "Invalid Spatial Relationships file: missing KEYWORD in line:" +
              line);
        }

        if (str.equalsIgnoreCase("VEGETATION")) {
          readAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("LANDFORM")) {
          readLandAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("ROADS") ||
                 str.equalsIgnoreCase("ROAD")) {
          readRoadsAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("TRAILS")) {
          readTrailsAttributes(area,fin,log);
        }
        else if (str.equalsIgnoreCase("AQUATIC")) {
          readAquaticAttributes(area,fin,log);
        }

        line = fin.readLine();
        while (line != null && line.trim().toUpperCase().startsWith("BEGIN") == false) {
          line = fin.readLine();
        }
      }
      log.flush();
      log.close();
      fin.close();
      hasAttributes = true;
    }
    catch (FileNotFoundException ex) {
      String msg = "Could not open input file: " + filename;
      log.println(msg);
      log.flush();
      log.close();
      throw new SimpplleError(msg);
    }
    catch (ParseError e) {
      String msg = "The following error occurred while trying to create the area:";
      log.println(msg);
      log.println(e.msg);
      log.flush();
      log.close();
      throw new SimpplleError(msg + "\n" + e.msg);
    }
    catch (IOException ex) {
      log.flush();
      log.close();
      throw new SimpplleError("Could write to log file: " + logFile);
    }
  }

  public Area readFiles(File prefix) throws SimpplleError {
    File file, log, logFile;
    Area newArea = new Area(Area.USER);

    boolean attributesAdded = false;

    file = simpplle.comcode.utility.Utility.makeSuffixedPathname(prefix,"","nbr");
    logFile = simpplle.comcode.utility.Utility.makeUniqueLogFile(prefix,"veg");
    if (read(newArea,file,logFile,EVU,false) == false) {
      return null;
    }

    if (!hasAttributes) {
      file = simpplle.comcode.utility.Utility.makeSuffixedPathname(prefix,"","attributes");
      if (file.exists() == false) {
        hasAttributes = false;
        return newArea;
      }
      logFile = simpplle.comcode.utility.Utility.makeUniqueLogFile(prefix,"vegatt");
      attributesAdded = read(newArea,file,logFile,EVU,true);
      if (!attributesAdded) { return null; }
    }

    hasAttributes = attributesAdded;
    return newArea;
  }

  private boolean read(Area newArea, File inputFile, File logFile, int kind, boolean attribOnly) {
    BufferedReader fin;
    PrintWriter    log;
    int            n=1;
    boolean        success;

    try {
      log = new PrintWriter(new FileWriter(logFile));
    }
    catch (IOException e) {
      e.printStackTrace();
      System.out.println("Could not Open log file for writing: logFile");
      return false;
    }

    try {
      fin = new BufferedReader(new FileReader(inputFile));

      if (!attribOnly) {
        switch (kind) {
          case EVU:
            success = readNeighbors(newArea, fin, log);
            break;
          case ELU:
            success = readLandNeighbors(newArea, fin, log);
            break;
          default:
            success = false;
        }
        if (!success) {
          fin.close();
          log.flush();
          log.close();
          return false;
        }
      }
      if (hasAttributes || attribOnly) {
        switch (kind) {
          case EVU:
            readAttributes(newArea,fin,log);
            break;
          case ELU:
            readLandAttributes(newArea,fin,log);
            break;
        }
      }
      fin.close();
    }
    catch (ParseError e) {
      log.println("The following error occurred while trying to create the area:");
      log.println(e.msg);
    }
    catch (FileNotFoundException e) {
      log.println("Could not open file: " + inputFile);
    }
    catch (IOException e) {
      log.println("The following error occurred while trying to create the area:");
      e.printStackTrace(log);
    }
    log.flush();
    log.close();

    return true;
  }

}

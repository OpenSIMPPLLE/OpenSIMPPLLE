package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * Reads the Vegetative Road Relations for a specified Area.
 */
public class ParseVegRoadRelations implements RelationParser{

  /**
   * Reads the Vegetative Road Relations for a specified Area.  Reads in road Id, and evu ID,
   * adds the adds the evu as an associated vegetative unit to the road and adds the roads as associated road units to the evu
   * @param area the current area whose Vegetative Road  Relations are defined in this method.
   * @return true if area file found (sets Vegetative Trail Relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
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

}

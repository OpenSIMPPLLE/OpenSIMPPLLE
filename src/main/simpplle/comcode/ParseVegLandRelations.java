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
 * Reads in Vegetative Land Relations of a specified Area.
 *
 */
public class ParseVegLandRelations implements RelationParser{

  /**
   * Reads in Vegetative Land Relations of a specified Area.  Reads evu ID, elu ID, then sets the evu's
   * associated land unit to the elu and sets the elu's associated evu to the evuID
   * @param area the current area whose Vegetative Land Relations are defined in this method.
   * @return true if area file found (sets vegetative land relations information as well)
   * @throws ParseError caught in ImportArea
   * @throws IOException caught in ImportArea
   */
  @Override
  public boolean readSection(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {

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

}

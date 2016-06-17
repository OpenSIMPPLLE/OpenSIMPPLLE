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
 * <p> Strategy for parsing spatialrelate files with the "KEANE" designation (version 2). Format:
 * FROM_POLY, TO_POLY, ELEV, SPREAD_DEG, BASE_WIND_SPEED, BASE_WIND_DIR
 *
 */
public class ParseAreaV2 implements IParseArea{
  @Override
  public boolean readNeighborsNew(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException {
    return false;
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

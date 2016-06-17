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
 *
 * <p> Defines an interface for processing .spatialrelate files. Strategy Pattern
 * is used to select the appropriate class at runtime based on the file version.
 *
 * @author Michael Kinsey
 *
 */
public interface IParseArea {

  boolean readNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readLandNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readAquaticNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readVegLandRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readAquaticVegRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readRoadNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readTrailNeighbors(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readVegRoadRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

  boolean readVegTrailRelations(Area area, BufferedReader in, PrintWriter log) throws ParseError, IOException;

}

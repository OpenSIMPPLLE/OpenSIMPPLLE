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

/**
 * Defines an interface for processing .spatialrelate files. Appropriate implementation
 * is selected at runtime based on file keyword.
 */

public interface RelationParser {

  boolean readSection(Area area, BufferedReader in, PrintWriter log) throws  ParseError, IOException;

}

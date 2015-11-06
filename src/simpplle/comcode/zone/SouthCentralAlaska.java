package simpplle.comcode.zone;

import simpplle.comcode.Area;
import simpplle.comcode.ProcessType;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.ValidZones;

import java.util.Hashtable;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the zone for South Central Alaska
 * The primary purpose is to initialize class fields with values unique to South Central Alaska.  
 * <p>As a result most member functions are private.
 * <p>No ecological stratification was used.  
 * <p>Densities - Tree W = 10-24%, O = 25-59%, C = 60-100%
 * <p>Disturbance Processes LSF, MSF, SRF (fire events) low, medium, high windthrow.  Wildlife browsing in aspen and birch, root diseas, light, medium, high spruce beetle.   
 * 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.RegionalZone
 *
 */

public final class SouthCentralAlaska extends RegionalZone {
  private static final String arcviewDir  = "gis/south-central-alaska";
  private static final String homeDir = "knowledge/zones/south-central-alaska";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor for South Central Alaska.  Initializes variables name, available to true, directories, and jar file pathways
   */
  public SouthCentralAlaska () {
    super();
    name         = "South Central Alaska";
    available    = true;
    zoneDir      = homeDir;
    pathwayKnowFile = "zones/south-central-alaska-pathways.jar";
    sysKnowFile  = "zones/south-central-alaska.jar";
    zoneDefnFile = "zones/south-central-alaska-defn.jar";
    gisExtraFile = "zones/south-central-alaska-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.ROOT_DISEASE
    };

    createSampleAreas();
  }

  /**
   * file directory is "gis/south-central-alaska"
   */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("Kenai","SAMPLE-AREAS/KENAI.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return the id of this zone.
   */
  public int getId () {
    return ValidZones.SOUTH_CENTRAL_ALASKA;
  }

}



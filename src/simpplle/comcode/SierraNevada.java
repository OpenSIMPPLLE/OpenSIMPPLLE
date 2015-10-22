
package simpplle.comcode;

import java.util.Hashtable;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>  This class defines the zone for Sierra Nevada, a type of regional zone.
 * The primary purpose is to initialize class fields with
 * values unique to Sierra Nevada.  As a result
 * most member functions are private.
 * Disturbance processes calculated are SRF, MSF, LSF (fire events), Drought mortality, severe and light bark beetles, succession, root diseas, white pine blister rust.  
 * Densities are 1(0-10%), 2(11-39%), 3(40-69%), 4(70-100%)
 * Ecological Stratification = Fth-x, Fth-m,, Lm-x, Lm-m, Um-x, Um-m, Sa
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class SierraNevada extends RegionalZone {
  private static final String arcviewDir  = "gis/sierra-nevada";
  private static final String homeDir = "knowledge/zones/sierra-nevada";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor for Sierra Nevada zone.  Inherits from Regional Zone superclass and initializes availability to true, 
   * the directories, jar file paths, and processes light bark beetles, bb rd dm complex, root disease
   */
  public SierraNevada() {
    super();
    name         = "Sierra Nevada";
    available    = true;
    zoneDir      = homeDir;
    pathwayKnowFile = "zones/sierra-nevada-pathways.jar";
    sysKnowFile  = "zones/sierra-nevada.jar";
    zoneDefnFile = "zones/sierra-nevada-defn.jar";
    gisExtraFile = "zones/sierra-nevada-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.LIGHT_BARK_BEETLES,
      ProcessType.BB_RD_DM_COMPLEX,
      ProcessType.ROOT_DISEASE
    };

    createSampleAreas();
  }
/**
 *  directory is "gis/sierra-nevada"
 */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }
/**
 * Sample areas for Sierra Nevada are Yosemite-NW and Yosemite.  
 */
  private void createSampleAreas () {
    sampleAreas    = new Area[2];
    sampleAreas[0] = new Area("Yosemite NW","SAMPLE-AREAS/YOSEMITE-NW.AREA",Area.SAMPLE);
    sampleAreas[1] = new Area("Yosemite","SAMPLE-AREAS/YOSEMITE.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return the id of this zone.
   */
  public int getId () {
    return ValidZones.SIERRA_NEVADA;
  }

}

package simpplle.comcode;

import java.util.Hashtable;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * <p>This class defines the zone for Gila, a Regional Zone object.
 * The primary purpose is to initialize class fields with values unique to Gila.  
 * As a result most member functions are private.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.RegionalZone
 */

public final class Gila extends RegionalZone {
  private static final String arcviewDir  = "gis/gila";
  private static final String homeDir = "knowledge/zones/gila";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor.  Inherits form Regional Zone superclass and initializes name, available = true, zoneDir, and the various jar file pathways. 
   */
  public Gila () {
    super();
    name      = "Gila";
    available = true;
    zoneDir = homeDir;
    pathwayKnowFile = "zones/gila-pathways.jar";
    sysKnowFile = "zones/gila.jar";
    zoneDefnFile = "zones/gila-defn.jar";
    gisExtraFile = "zones/gila-gis.jar";

    probDataProcesses = new ProcessType[] {
//      ProcessType.LIGHT_LP_MPB,a int representing 
//      ProcessType.DF_BEETLE,
//      ProcessType.ROOT_DISEASE,
//      ProcessType.SPRUCE_BEETLE,
//      ProcessType.WBP_MPB,
      ProcessType.PP_MPB
    };
    createSampleAreas();
  }
/**
 * gets ArcView files at "gis/gila"
 */
  public String getArcviewDir() { return arcviewDir; }
/**
 * Gets all the user probability processes for Gila zone.  
 */
  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }
/**
 * gets the GIS files at listed above 
 */
  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("Gila","SAMPLE-AREAS/GILA.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.  Gila = 4
   * @see simpplle.comcode.ValidZones
   * @return the id of this zone = 4
   */
  public int getId () {
    return ValidZones.GILA;
  }

}



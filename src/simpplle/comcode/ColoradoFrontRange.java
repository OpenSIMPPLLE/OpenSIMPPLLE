package simpplle.comcode;

import java.util.Hashtable;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods pertaining to the Colorado Front Range. This is a regional zone. 
 * @author Documentation by Brian Losi
 * 
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.RegionalZone
 */
public class ColoradoFrontRange extends RegionalZone {
  private static final String arcviewDir  = "gis/colorado-front-range";
  private static final String homeDir = "knowledge/zones/colorado-front-range";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor - Initializes several fields inherited from superclass RegionalZone.
   */
  public ColoradoFrontRange () {
    super();
    name            = "Colorado Front Range";
    available       = true;
    zoneDir         = homeDir;
    pathwayKnowFile = "zones/colorado-front-range-pathways.jar";
    sysKnowFile     = "zones/colorado-front-range.jar";
    zoneDefnFile    = "zones/colorado-front-range-defn.jar";
    gisExtraFile    = "zones/colorado-front-range-gis.jar";

/*
     SUCCESSION,
     FIRE-EVENT, (all)
     STAND-REPLACING-FIRE, (all)
     MIXED-SEVERITY-FIRE, (all)
     LIGHT-SEVERITY-FIRE, (all)
     WILDLIFE-BROWSING, (all)
     WET-SUCCESSION,
     DRY-SUCCESSION,
 */
    probDataProcesses = new ProcessType[] {
      ProcessType.LIGHT_LP_MPB,
      ProcessType.DF_BEETLE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.PIED_BB
    };

    createSampleAreas();
  }

  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[2];
    sampleAreas[0] = new Area("Wet Mtns","SAMPLE-AREAS/WETMTN.AREA",Area.SAMPLE);
    sampleAreas[1] = new Area("Trout","SAMPLE-AREAS/TROUT.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return a int representing the id of this zone.
   */
  public int getId () {
    return ValidZones.COLORADO_FRONT_RANGE;
  }


}

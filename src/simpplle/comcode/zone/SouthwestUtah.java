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
 * <p>This class defines the zone for Southwest Utah
 * The primary purpose is to initialize class fields with values unique to Southwest Utah.  
 * <p>As a result most member functions are private.
 * <p> Ecological Stratification - 0 (Misc special habitates),1(Pinyon pine - Juniper),2 (Montane maple-oak),3 (Ponderosa-pine),4(Dry Douglas-fir),5 (cool or moist Douglas-fir),6 ( White fir and blue spruce),7(Aspen dominated)
 * <p> Range Density - 1-Open, 2-Closed
 * <p>Forest Density 1 = <40%, 2 = 41-70%, 3 = >70%
 * Disturbance processes = LSF, MSF, SRF (fire events), ponderosa pine mountain pine beetle.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.RegionalZone
 *
 */

public class SouthwestUtah extends RegionalZone {
  private static final String arcviewDir  = "gis/southwest-utah";
  private static final String homeDir = "knowledge/zones/southwest-utah";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  private static final int NUM_TREATMENTS = 11;

  /**
   * Constructor for Southwest Utah.  Initializes name, available to true, directory, and jar pathways.
   */
  public SouthwestUtah () {
    super();
    name         = "Southwest Utah";
    available    = true;
    zoneDir      = homeDir;
    pathwayKnowFile = "zones/southwest-utah-pathways.jar";
    sysKnowFile  = "zones/southwest-utah.jar";
    zoneDefnFile = "zones/southwest-utah-defn.jar";
    gisExtraFile = "zones/southwest-utah-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.PP_MPB
    };

    createSampleAreas();
  }

  /**
   * director address is "gis/southwest-utah"
   */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("Utah","SAMPLE-AREAS/UTAH.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return the id of this zone.
   */
  public int getId () {
    return ValidZones.SOUTHWEST_UTAH;
  }


}

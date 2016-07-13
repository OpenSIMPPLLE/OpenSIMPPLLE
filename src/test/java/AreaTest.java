import org.junit.Test;
import static org.junit.Assert.assertEquals;

import simpplle.comcode.AdjacentData;
import simpplle.comcode.Area;
import simpplle.comcode.Evu;


/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * <p> Contains tests pertaining to the Area class
 */
public class AreaTest {

  @Test
  public void calculatesSlope(){
    Area area = new Area();

    // Set up to evu
    Evu evu = new Evu();
    evu.setAcres((float)22.950);  // should result in 1000 ft sides
    evu.setElevation(305);  // about 1000 ft

    // Set up adjacent data
    Evu neighborEvu = new Evu();
    neighborEvu.setElevation(274); // about 900
    AdjacentData adjacentData = new AdjacentData(neighborEvu, 'E', 'N', 270,
        4, 270);

    assertEquals(-.101, area.calcSlope(evu, adjacentData), .001);

    adjacentData.setSpread(315);

    assertEquals(-.0719, area.calcSlope(evu, adjacentData), .001);
  }
}

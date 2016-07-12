import org.junit.Test;
import static org.junit.Assert.assertEquals;

import simpplle.comcode.Evu;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * <p> Contains tests pertaining to the Evu class
 */
public class EvuTest {

  @Test
  public void getsAzimuthDifference(){
    Evu evu = new Evu();
    assertEquals(evu.getAzimuthDifference(270.0, 1.0), 91.0, 0);
    assertEquals(evu.getAzimuthDifference(90.0,  1.0), 89.0, 0);
    assertEquals(evu.getAzimuthDifference(181.0, 1.0), 180.0, 0);
    assertEquals(evu.getAzimuthDifference(181.0, 0.0), 179.0, 0);
  }

  @Test
  public void isActuallyDownwind(){
    Evu evu = new Evu();
    assertEquals(evu.isDownwind(0,0), 'D');
    assertEquals(evu.isDownwind(0,90), 'D');
    assertEquals(evu.isDownwind(0,91), 'N');
  }
}

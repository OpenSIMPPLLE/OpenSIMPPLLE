/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

import org.junit.Test;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SimpplleError;

import static junit.framework.TestCase.assertEquals;

/**
 * Contains tests pertaining to the Simpplle class
 */
public class SimpplleTest {

  /**
   * This test simply checks that all the available zones are loaded without returning and error,
   * or null.
   *
   * It must increment indexes >= 8 as the result of a hack from early OpenSIMPPLLE.
   * @see simpplle.gui.NewZoneDialog#loadZone()
   * @throws SimpplleError
   */
  @Test
  public void loadsAllZones() throws SimpplleError {
    boolean isSuccess = true;
    // Need to call JSimpplle constructor to instantiate static members
    for (int i = 0; i < RegionalZone.availableZones().length; i++){
      if (i >= 8) {
       if(!loadAZone(i+2)){
         isSuccess = false;
       }
      } else {
        if(!loadAZone(i)){
          isSuccess = false;
        }
      }
    }
    assertEquals(true, isSuccess);
  }

  /**
   * Helper method. Loads an individual zone, and returns true if zone was loaded successfully.
   * In this instance, success constitutes the currentZone attribute being initialized.
   *
   * @param id - Calls the loadZone method with the given id. Zones are loaded by their id in
   *           the ValidZones class.
   * @return true if Simpplle.currentZone is instantiated.
   * @throws SimpplleError
   */
  private boolean loadAZone(int id) throws SimpplleError {
    Simpplle simpplle = new Simpplle();
    simpplle.loadZone(id);
    return (Simpplle.getCurrentZone() != null);
  }
}

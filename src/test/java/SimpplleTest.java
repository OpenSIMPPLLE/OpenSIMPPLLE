import org.junit.Test;
import simpplle.JSimpplle;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SimpplleError;
import simpplle.gui.NewZoneDialog;

import static junit.framework.TestCase.assertEquals;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * <p> Contains tests pertaining to the Simpplle class
 */
public class SimpplleTest {

  /**
   * This test simply checks that all the available zones are loaded without returning and error,
   * or null.
   *
   * It must increment indexes >= 8 as the result of a hack from early OpenSIMPPLLE.
   * @see NewZoneDialog#loadZone()
   *
   * @throws SimpplleError
   */
  @Test
  public void loadsAllZones() throws SimpplleError {
    boolean isSuccess = true;
    JSimpplle jSimpplle = new JSimpplle();
    for (int i = 0; i < RegionalZone.availableZones().length; i++){
      if (i >= 8) {
       if(!loadZone(i+2)){
         isSuccess = false;
       }
      }
      else {
        if(!loadZone(i)){
          isSuccess = false;
        }
      }
    }
    assertEquals(true, isSuccess);
  }

  private boolean loadZone(int index) throws SimpplleError {
    Simpplle simpplle = new Simpplle();
    simpplle.loadZone(index);
    return (Simpplle.getCurrentZone() != null);
  }
}

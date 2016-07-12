import org.junit.Test;
import simpplle.JSimpplle;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SimpplleError;

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

  @Test
  public void loadsAllZones() throws SimpplleError {
    JSimpplle jSimpplle = new JSimpplle();
    for (int i = 0; i < RegionalZone.availableZones().length; i++){
      if (i >= 8) loadZone(i+2);
      else loadZone(i);
    }
  }

  private boolean loadZone(int index) throws SimpplleError {
    JSimpplle jSimpplle = new JSimpplle();
    jSimpplle.getComcode().loadZone(index);
    return (Simpplle.getCurrentZone() != null);
  }
}

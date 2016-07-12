import static org.junit.Assert.assertEquals;
import org.junit.Test;
import simpplle.gui.SimpplleMain;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * <p>
 * <p> Contains tests pertaining to the SimpplleMain Class
 */
public class SimpplleMainTest {

  /**
   * Here is an example unit test: call setStatusMessage and confirm that its
   * contract is fulfilled by using JUnit's assertion library.
   */
  @Test
  public void setsStatusMessage(){
    SimpplleMain s = new SimpplleMain();
    String text = "JUnit";
    s.setStatusMessage(text);
    assertEquals(s.getStatusBar().getText(), text);
  }

  @Test
  public void clearsStatusMessage(){
    SimpplleMain s = new SimpplleMain();
    s.setStatusMessage("Garbage");
    s.clearStatusMessage();
    assertEquals(s.getStatusBar().getText(), "");
  }

  // TODO
  @Test
  public void loadsNewZone(){
    SimpplleMain s = new SimpplleMain();

  }
}

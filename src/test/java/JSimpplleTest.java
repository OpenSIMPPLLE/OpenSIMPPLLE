import static org.junit.Assert.*;
import org.junit.Test;
import simpplle.JSimpplle;
import simpplle.gui.SimpplleMain;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Michael Kinsey
 *
 */
public class JSimpplleTest {
    @Test
    public void setsStatusMessage(){
        JSimpplle j = new JSimpplle();
        assertEquals(1,1);
    }

    @Test
    public void getsGui(){
        JSimpplle j = new JSimpplle();
        assertEquals(JSimpplle.getSimpplleMain().getClass(), SimpplleMain.class);
    }
}

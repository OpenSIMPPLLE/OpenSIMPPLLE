/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;
//import com.sun.java.swing.plaf.mac.MacLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;

//import com.sun.java.swing.plaf.mac.MacLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 *   
 *     
 */
public class JSimpplleApp {
  private boolean      packFrame = false;
  private SimpplleMain frame;

  //Construct the application
  public JSimpplleApp(SimpplleMain frame) {
    this.frame = frame;
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame)
      frame.pack();
    else
      frame.validate();

    // Make sure frame is centered on screen.
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width/2 - frame.getSize().width/2,
                      screenSize.height/2 - frame.getSize().height/2);

    frame.setVisible(true);
  }

  public SimpplleMain getMainWindow() {
    return frame;
  }

  public static void initialize () {
    String lookFeel = System.getProperty("simpplle.lookfeel");

    try  {
      if (lookFeel == null) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        return;
      }
      else {
        lookFeel = lookFeel.toLowerCase();
      }

/*
      if (lookFeel.indexOf("mac") != -1) {
        //UIManager.setLookAndFeel(new MacLookAndFeel());
        MyMacLookAndFeel mac = new MyMacLookAndFeel();
        UIManager.setLookAndFeel(mac);
      }
*/
      if (lookFeel.indexOf("metal") != -1) {
        UIManager.setLookAndFeel(new MetalLookAndFeel());
      }
      else if (lookFeel.indexOf("motif") != -1) {
        UIManager.setLookAndFeel(new MotifLookAndFeel());
      }
      else {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
    }
    catch(Exception e) {
    }
  }
}

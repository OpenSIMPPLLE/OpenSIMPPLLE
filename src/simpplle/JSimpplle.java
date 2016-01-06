package simpplle;

import java.util.StringTokenizer;
import java.io.*;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JWindow;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URLDecoder;
import java.security.ProtectionDomain;
import java.security.CodeSource;
import java.net.URL;
import simpplle.gui.SimpplleMain;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *

 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * // Properties that OpenSIMPPLLE uses:
//   simpplle.fixedrandom=enabled    ;; Read random #'s from file
//   simpplle.fire.spotting=disabled ;; disable fire spotting
//   simpplle.lookfeel=mac           ;; use mac look & feel
//   simpplle.lookfeel=motif         ;; use motif look & feel
//   simpplle.lookfeel=metal         ;; use metal look & feel

 * This is the Main starting point for the JSimpplle application.
 * This will determine whether to start the gui or command-line
 * based on properties.
 * This class also provides some utilities and certain data that
 * needs to be accessed throughout the application.
 *
 */

public final class JSimpplle {
  private static simpplle.comcode.Simpplle    comcode;
  @SuppressWarnings("unused")
  private static simpplle.gui.JSimpplleApp    jSimpplleApp;
  private static simpplle.gui.SimpplleMain    simpplleMain;

  private static File homeDirectory;
  private static String argHomeDirectory;

  public static final String MAC_OS_X = "Mac OS X";

  private static boolean debug = false;
  private static boolean zoneEdit = false;

  private static JWindow splashScreen;
  private static File   workingDirectory;
  public  static final String endl=simpplle.comcode.Simpplle.endl;

  private static PrintStream          systemOut;
  private static PrintStream          systemErr;
  private static ByteArrayOutputStream systemOutBuffer;

  
  public JSimpplle() {
  }
/**
 * Sets whether the user wishes to edit an regional zone ("zone edit mode").  
 * @param value boolean value designates if zone will be edited.  true = to be edited 
 */
  public static void setZoneEdit(boolean value) { zoneEdit = value; }
  
  /**
   * boolean function, true if zone can be edited
   * @return true if zone can be edited.  
   */
  public static boolean isZoneEditable() { return zoneEdit; }

/**
 * Displays the current status of system whether command line or gui.
 * @param msg designates current status of system.
 */
  public static void setStatusMessage(String msg) {
    setStatusMessage(msg,false);
  }
  
  /** 
   * 
   * @param msg String message.  
   * @param wait boolean  
   */
  public static void setStatusMessage(String msg, boolean wait) {
    simpplleMain.setStatusMessage(msg,wait);
  }

  /**
   * clears the status message
   */
  public static void clearStatusMessage() {
    simpplleMain.clearStatusMessage();
  }

  /**
   * 
   * @return comcode from OpenSimpplle.Comcode package.  This will give access to all the classes in the comcode, which is the main engine for OpenSimpplle
   */
  public static simpplle.comcode.Simpplle getComcode() {
    return comcode;
  }
/**
 * 
 * @return gui for OpenSimpplle interface
 */
  public static simpplle.gui.SimpplleMain getSimpplleMain() {
    return simpplleMain;
  }

  /**
   * Gets the current working directory
   * @return working directory file in current working directory. 
   */
  public static File getWorkingDir() { return workingDirectory; }

  /**  
   * Sets the working directory file for Simpplle main.
   * @param dir File of current working directory.  
   */
  public static void setWorkingDir(File dir) { workingDirectory = dir; }

  /**
   * Operating system check to ensure current operating system .equals user-entered operating system.  
   * @param osname String with operating system name choosen by software user.  
   * @return True if current operating system and operating system name are same, false otherwise.  
   *@link http://docs.oracle.com/javase/7/docs/api/java/lang/System.html#setProperties(java.util.Properties)
   */
  public static boolean isCurrentOS(String osname) {
    return (System.getProperty("os.name").equalsIgnoreCase(osname));
  }

  /** 
   * Returns true if the operating system key is Windows.
   * @return True if index of windows does not equal -1.  
   * @link 
   */
  public static boolean isWindowsOS() {
    return (System.getProperty("os.name").indexOf("Windows") != -1);
  }

  /**
   * Convert buffer contents into a string using default UTF-8 encoding.
   * @return toString from buffer contents.   
   */
  public static String getSystemOutBuffer() {
    return systemOutBuffer.toString();
  }

  /**
   * creates an output stream Byte Array, then prints to a PrintStream 
   */
  public static void redirectSystemIO() {
    systemOutBuffer = new ByteArrayOutputStream();

    systemOut = System.out; // save original System output stream
    systemErr = System.err;

    PrintStream newOut = new PrintStream(systemOutBuffer);
    System.setOut(newOut);
    System.setErr(newOut);
  }

  /**
   * Reassigns system outputs to the normal output stream.  
   */
  public static void restoreNormalSystemIO() {
    System.setOut(systemOut);
    System.setErr(systemErr);
    systemOutBuffer = null;
  }

  /**  
   * Install directory is home directory. 
   * @return File in home directory path
   */
  public static File getInstallDirectory() {
    return homeDirectory;
  }

  /**  
   * Puts Simpplle into developer mode to allow editing.  Sets the system property to development mode. 
   * @return boolean true if System property is not null and developer equals true. 
   */
  public static boolean developerMode() {
    String developer = System.getProperty("simpplle.development");
    return (developer != null && developer.equals("true"));
  }
  /**  
   * Sets System to logging simulation mode.
   * @return true if logfile is not null and logfile is this current file directory.  
   */
  public static boolean simLoggingFile() {
    String logfile = System.getProperty("simpplle.simulationlogging");
    return (logfile != null && logfile.equals("true"));
  }
  
/**Sets system to invasive species Montana State University Problem File.  
 * 
 * @return true if Montana State University Problem file exists and is the current file.  
 */
  
  public static boolean invasiveSpeciesMSUProbFile() {
    String msuProbFile = System.getProperty("simpplle.comcode.InvasiveSpeciesLogicDataMSU.probFile");
    return (msuProbFile != null && msuProbFile.equals("true"));
  }

  /**Determines the install directory for Simpplle software package.  The code source is set at JSimpplle protection domain.  
   * Sets code source location and URl to decode the URL file in UTF-8.  If home directory is null sets to the file  
   * command line arguments.  If Simpplle is not in development mode the home directory is at the home directory parent.  
   *@throws UnsupportedEncodingException
   */
  private static void determineInstallDirectory() {
    String developer = System.getProperty("simpplle.development");

    ProtectionDomain pDomain = simpplle.JSimpplle.class.getProtectionDomain();
    CodeSource codeSource = pDomain.getCodeSource();
    URL        loc=null;
    String     homeClass=null;

    if (codeSource != null) {
      loc = codeSource.getLocation();
    }
    try {
      if (loc != null) {
        homeClass = URLDecoder.decode(loc.getFile(), "UTF-8");
      }
    }
    catch (UnsupportedEncodingException ex) {
      homeClass = null;
    }

    if (homeClass == null) {
      homeDirectory = new File(argHomeDirectory);
    }
    else {
      homeDirectory = (new File(homeClass)).getParentFile();
      if (developer == null || developer.equals("true") == false) {
        homeDirectory = homeDirectory;
      }
    }

  }
  
  /**
   * Simpplle Main method.   
   * 
   * @param args
   * @throws IOException
   * @see readXmlPropertiesFile()
   */

  //Main method
  public static void main(String[] args) throws IOException {
    System.setProperty("sun.awt.exception.handler","simpplle.gui.MyErrorHandler");

    readXmlPropertiesFile();

    
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
/**
 * Method to create and paint the gui interface. 
 * @see showSplashScreen(), determineInstallDirectory(), hideSplashScreen(), readPropertiesFile()
 */
  private static void createAndShowGUI() {
    showSplashScreen();
    determineInstallDirectory();

    comcode = new simpplle.comcode.Simpplle();

    readPropertiesFile();

    simpplle.gui.JSimpplleApp.initialize();
    simpplleMain = new SimpplleMain();
    jSimpplleApp = new simpplle.gui.JSimpplleApp(simpplleMain);
    hideSplashScreen();
  }

  /**
   * Reads the XML properties file with the following characteristics:
   * <li> System.getProperty at "user.home"
   * <li> file at key of home directory and default value "properties.xml"
   * <li> directory designated for installation
   * <li> file seperation
   * <li> EOF
   * @throws Exception
   */
  private static void readXmlPropertiesFile() {
    String homeDir = System.getProperty("user.home");
    File   file = new File(homeDir,"properties.xml");
    BufferedReader fin;

    try {
      if (file.exists() == false) { return; }
      else {
        fin = new BufferedReader(new FileReader(file));
        String line = fin.readLine();
        
        while (line != null) {
          int begin = line.indexOf("<APPDIR>");
          if (begin != -1) {
            int end = line.indexOf("</APPDIR>");

            String installDir = line.substring(begin+8,end);

            String fileSep = System.getProperty("file.separator");

            if (installDir.endsWith(fileSep)) {
              argHomeDirectory = installDir.substring(0,installDir.length()-1);
            }
            else {
              argHomeDirectory = installDir;
            }
            break;
          }

          line = fin.readLine();
        }
        fin.close();
      }
    }
    catch (Exception e) { return; }
  }
  
  
  public static void readPropertiesFile() {
    String homeDir = System.getProperty("user.home");
    File   file = new File(homeDir,"opensimpplle.properties");
    BufferedReader fin;

    setWorkingDir(new File(homeDir));
    debug = false;
    try {
      if (!file.exists()) { return; }
      else {
        fin = new BufferedReader(new FileReader(file));
        String line = fin.readLine();
        StringTokenizer strTok;
        String property;
        while (line != null) {
          strTok = new StringTokenizer(line, ",");
          property = strTok.nextToken();
          if (property == null) {
            fin.close();
            return;
          }
          property = property.trim();

          if (property.equals("WORKING_DIRECTORY")) {
            String dir = strTok.nextToken();
            if (dir.equalsIgnoreCase("DEFAULT")) {
              dir = homeDir;
            }
            file = new File(dir);
            if (file.exists()) {
              setWorkingDir(file);
            }
          }
          else if (property.equalsIgnoreCase("DEBUG")) {
            debug = true;
          }
          else if (property.equalsIgnoreCase("SIMULATION_LOGGING")) {
            String value = strTok.nextToken();
            System.setProperty("simpplle.simulationlogging", value);
          }
          else if (property.equalsIgnoreCase("InvasiveSpeciesLogicDataMSU_probFile")) {
            String value = strTok.nextToken();
            System.setProperty("simpplle.comcode.InvasiveSpeciesLogicDataMSU.probFile", value);
          }
          
          
          line = fin.readLine();
        }
        fin.close();
      }
    }
    catch (Exception e) { return; }
  }

  /**
   * 
   * @param filename String
   * @throws Exception
   */
  private static void writeMainDebugInfo(String filename) {
    String dir = System.getProperty("user.home");
    File   file = new File(dir,filename);
    PrintWriter fout;

    try {
      fout = new PrintWriter(new FileWriter(file));
      fout.println(argHomeDirectory);

      fout.flush();
      fout.close();
    }
    catch (Exception e) { return; }
    
  }
  
  /**
   * 
   */
  public static void writePropertiesFile() {
    String dir = System.getProperty("user.home");
    File   file = new File(dir,"opensimpplle.properties");
    PrintWriter fout;

    try {
      fout = new PrintWriter(new FileWriter(file));
      fout.println("WORKING_DIRECTORY," + getWorkingDir());
      if (debug) {
        fout.println("DEBUG");
      }
      {
        String value = System.getProperty("simpplle.simulationlogging");
        if (value == null) { value = "false";}
        fout.println("SIMULATION_LOGGING," + value);
      }
      {
        String value = System.getProperty("simpplle.comcode.InvasiveSpeciesLogicDataMSU.probFile");
        if (value == null) { value = "false"; }
        fout.println("InvasiveSpeciesLogicDataMSU_probFile," + value);
      }

      fout.flush();
      fout.close();
    }
    catch (Exception e) { return; }
  }

  /**
   * Debug mode check.  
   * @return boolean if in debug mode
   */
  public static boolean debug() { return debug; }
  
  public static void showSplashScreen() {
    ImageIcon splash = new ImageIcon(simpplle.JSimpplle.class.getResource("gui/images/splash.jpg"));
    JLabel label = new JLabel(splash);

    splashScreen = new JWindow();
    splashScreen.getContentPane().add(label);
    splashScreen.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    splashScreen.setLocation(screenSize.width/2 - splashScreen.getSize().width/2,
                             screenSize.height/2 - splashScreen.getSize().height/2);

    splashScreen.setVisible(true);
  }

  /**
   * Sets the visibility of Simpplle image logo to false.  
   */
  public static void hideSplashScreen() {
    splashScreen.setVisible(false);
    splashScreen = null;
  }

}

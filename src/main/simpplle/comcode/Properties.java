/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.StringTokenizer;

/**
 *  Properties is responsible for the reading and writing of the opensimpplle.properties file, as
 *  well as encapsulating the related properties.
 */
public class Properties {

  private final String propertiesFileName = "opensimpplle.properties";
  private boolean debug;
  // Sets system to invasive species Montana State University Problem File.
  private boolean invasiveMSU;
  private boolean simulationLogging;
  private static File   workingDirectory;

  public void readPropertiesFile(){
    String homeDir = System.getProperty("user.home");
    File   file = new File(homeDir, propertiesFileName);
    BufferedReader fin;

    setWorkingDir(new File(homeDir));
    debug = false;

    try {
      if(file.exists()) {
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
            simulationLogging = Boolean.parseBoolean(value);
          }
          else if (property.equalsIgnoreCase("InvasiveSpeciesLogicDataMSU_probFile")) {
            String value = strTok.nextToken();
            System.setProperty("simpplle.comcode.InvasiveSpeciesLogicDataMSU.probFile", value);
            invasiveMSU = Boolean.parseBoolean(value);
          }
          line = fin.readLine();
        }
        fin.close();
      }
    }
    catch (Exception e) { return; }
  }

  /**
   * Write current user settings to properties file. Called every time the application closes.
   */
  public void writePropertiesFile() {
    String dir = System.getProperty("user.home");
    File   file = new File(dir, propertiesFileName);
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

  /*
        Accessor  Methods
   */
  public boolean isDebug() {
    return debug;
  }

  /**
   * Gets the current working directory
   * @return working directory file in current working directory.
   */
  public File getWorkingDir() { return workingDirectory; }

  /**
   * Sets the working directory file for Simpplle main.
   * @param dir File of current working directory.
   */
  public void setWorkingDir(File dir) { workingDirectory = dir; }

  public boolean isSimulationLogging() {
    return simulationLogging;
    // previously: return (logfile != null && logfile.equals("true"));
  }

  public void setSimulationLogging(boolean simulationLogging) {
    this.simulationLogging = simulationLogging;
    System.setProperty("simpplle.simulationlogging", String.valueOf(simulationLogging));
  }

  public boolean isInvasiveMSU() {
    return invasiveMSU;
    // previously: return (msuProbFile != null && msuProbFile.equals("true"));
  }

  public void setInvasiveMSU(boolean invasiveMSU) {
    this.invasiveMSU = invasiveMSU;
    System.setProperty("simpplle.comcode.InvasiveSpeciesLogicDataMSU.probFile", String.valueOf(invasiveMSU));
  }
}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This class defines an Object to hold process schedule information.  Many of these will be called in the GUI for process schedule, which allows
 * users to create, open, and modify process schedules.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ProcessSchedule {
  private int    appIndex;
  private int    numApps;
  private Vector applications;

  /**
   * Constructor for ProcessSchedule.  A process schedule has a vector for applications, an application index and number of applications variables.
   */
  public ProcessSchedule() {
    applications = new Vector();
    appIndex     = 0;
    numApps      = 0;
  }

  /**
    *  returns an array of the process applications
    *  that are to be applied.
    *  @return a array of ProcessApplication
    *  @see simpplle.comcode.ProcessApplication
    */
  public Vector getApplications() { return applications; }
/**
 * Goes through the applications vector, and finds a particular process application by its process type and time step.
 * @param processType
 * @param tStep time step used to find process application.  
 * @return the process application sought
 */
  public ProcessApplication findApplication(ProcessType processType, int tStep) {
    ProcessApplication app;
    if (applications == null) { return null; }

    for(int i=0; i<applications.size(); i++) {
      app = (ProcessApplication) applications.elementAt(i);
      if (app.getProcessType().equals(processType) &&
          app.getTimeStep() == tStep) {
        return app;
      }
    }
    return null;
  }

  public void readUnitIdFile(File input_file) throws SimpplleError {
    BufferedReader fin;

    try {
      fin = new BufferedReader(new FileReader(input_file));
    }
    catch (IOException IOX) {
      throw new SimpplleError("Problems opening input file.");
    }
    readUnitIdFile(fin);
  }

  private void readUnitIdFile(BufferedReader fin) throws SimpplleError {
    ProcessApplication   app;
    RegionalZone         zone = Simpplle.getCurrentZone();
    Area                 area = Simpplle.getCurrentArea();
    String               line = null;
    int                  tStep, unitId;
    int                  count = 0;
    String               processName;
    ProcessType          processType;
    StringTokenizerPlus  strTok;
    boolean              newProcessApp = true;

    try {
      line = fin.readLine();
      if (line == null) {
        throw new SimpplleError("input file is empty.");
      }
      line = line.trim();

      do {
        strTok = new StringTokenizerPlus(line,",");
        if (strTok.countTokens() != 2) {
          throw new ParseError("Invalid line in input file: " + line);
        }

        // Get the time step.
        tStep = strTok.getIntToken();
        if (tStep == -1) {
          throw new ParseError("Invalid time step in file: " + line);
        }
        // Get the process name, make sure its valid.
        processName = strTok.getToken();
        if (processName != null) {
          processName = processName.toUpperCase().trim();
        }
        processType = ProcessType.get(processName);
        if (processType == null) {
          throw new ParseError("Invalid Process in file: " + line);
        }

        app = this.findApplication(processType,tStep);
        newProcessApp = false;
        if (app == null) {
          app = new ProcessApplication(processType);
          app.setTimeStep(tStep);
          newProcessApp = true;
        }

        // Get all of the unit id's
        // Loop until end of file or we reach another process app.
        do {
          line = fin.readLine();
          if (line == null) { continue; }
          line = line.trim();
          if (line.length() == 0) {
            line = null;
            continue;
          }

          strTok = new StringTokenizerPlus(line,",");
          count  = strTok.countTokens();
          if (count != 1 && count != 2) {
            throw new ParseError("Invalid line in file: " + line);
          }
          else if (count == 2) {
            continue;
          }

          unitId = strTok.getIntToken();
          if (area.isValidUnitId(unitId)) {
            app.addUnitId(unitId);
          }
          else {
            throw new ParseError("Invalid unit id: " + line);
          }
        }
        while (count != 2 && line != null);

        if (newProcessApp) {
          addApplication(app);
        }
      }
      while (line != null);
    }
    catch (IOException e) {
      throw new SimpplleError("Problems reading input file.");
    }
    catch (ParseError e) {
      throw new SimpplleError(e.msg);
    }
    catch (NumberFormatException e) {
      throw new SimpplleError("Invalid time step in file: " + line);
    }
  }

  public void read(File input_file) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(input_file));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      read(fin);
      setCurrentFile(input_file);
      fin.close();
      gzip_in.close();
    }
    catch (IOException IOX) {
      throw new SimpplleError("Problems opening input file.");
    }
  }

  public void read(BufferedReader fin) throws SimpplleError {
    ProcessApplication app;
    RegionalZone         zone;
    String               line, zoneName;
    int                  numApps;

    try {
      // Get the name of the zone
      zoneName = fin.readLine();
      zone     = Simpplle.getCurrentZone();

      if (zone.getName().equals(zoneName) == false) {
        String msg = "This Process Schedule is for the following zone only: "
                     + zoneName;
        throw new ParseError(msg);
      }
      // Get Number of Applications in file
      line = fin.readLine();
      numApps = Integer.parseInt(line);

      for(int i=0;i<numApps;i++) {
        app = new ProcessApplication();
        app.read(fin);
        addApplication(app);
      }
    }
    catch (IOException e) {
      String msg = "Problems reading input file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (ParseError e) {
      System.out.println(e.msg);
      throw new SimpplleError(e.msg);
    }
    catch (NumberFormatException e) {
      String msg = "Invalid Treatment Schedule File.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  public void save(File filename) {
    File             outfile = Utility.makeSuffixedPathname(filename,"","process");
    GZIPOutputStream out;
    PrintWriter      fout;

    try {
      out = new GZIPOutputStream(new FileOutputStream(outfile));
      fout = new PrintWriter(out);

      save(fout);
      setCurrentFile(outfile);

      fout.flush();
      fout.close();
      out.close();
    }
    catch (IOException e) {
      System.out.println("Problems opening output file.");
      return;
    }

  }

  public void save(PrintWriter fout) {
    ProcessApplication app;

    fout.println(Simpplle.getCurrentZone().getName());
    fout.println(applications.size());

    for(int i=0;i<applications.size();i++) {
      app = (ProcessApplication) applications.elementAt(i);
      app.save(fout);
    }
  }
/**
 * Makes a new process applicaton for a particular process and adds it to the process application vector.  
 * @param processType the process to be scheduled
 * @return the new process application
 */
  public ProcessApplication newApplication(ProcessType processType) {
    ProcessApplication app;

    app = new ProcessApplication(processType);
    addApplication(app);
    return app;
  }
/**
 * Adds a process application to the vector of process applications, changes the appIndex to the size of process applicatin vector - 1, and increments the
 * number of applicatons.  
 * @param app the process application being added
 */
  public void addApplication(ProcessApplication app) {
    applications.addElement(app);
    appIndex = applications.size() - 1;
    numApps++;
  }

  public void copyCurrentApplication() {
    ProcessApplication currentApp = getCurrentApplication();
    ProcessApplication newApp = currentApp.duplicate();

    addApplication(newApp);
  }

  public void removeCurrentApplication() {
    applications.removeElementAt(appIndex);
    if (appIndex >= applications.size()) { appIndex--; }
    numApps--;
  }

  public void setCurrentFile(File saveFile) {
    SystemKnowledge.setFile(SystemKnowledge.PROCESS_SCHEDULE,saveFile);
  }

  public ProcessApplication getNextApplication() {
    appIndex++;
    if (appIndex >= applications.size()) { appIndex = 0; }
    return getCurrentApplication();
  }

  public ProcessApplication getPrevApplication() {
    appIndex--;
    if (appIndex < 0) { appIndex = applications.size() - 1; }
    return getCurrentApplication();
  }
/**
 * Gets the element at a particular index, if there are any.  
 * @return the process application at a particular index.  
 */
  public ProcessApplication getCurrentApplication() {
    if (numApps == 0) { return null; }
    return (ProcessApplication) applications.elementAt(appIndex);
  }

}




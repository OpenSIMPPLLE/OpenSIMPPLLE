/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/** The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 *
 */


public class TreatmentSchedule {
  // 2 is the first file version and add a file version number to the file.
  // 3 (aggregate acres(min/max), not options for attribute lists, multiple special area and ownership
  private static final int FILE_VERSION = 3;

  private int    numApps;
  private Hashtable applications;

  private LinkedList[] probData;
  private boolean      probDataLoaded;

  public TreatmentSchedule() {
    applications = new Hashtable();
    numApps      = 0;
    probDataLoaded = false;
  }

  public boolean hasApplications() { return applications.size() > 0; }

  public void removeFollowUpApplications() {
    TreatmentApplication app;
    Vector               apps;
    int                  i;

    Enumeration elements = applications.elements();
    while (elements.hasMoreElements()) {
      apps = (Vector)elements.nextElement();
      for (i=0; i<apps.size(); i++) {
        app = (TreatmentApplication)apps.elementAt(i);
        if (app.isSystemGenerated()) {
          removeApplication(app);
          app.removeNextApplication();
        }
        else {
          app.removeNextApplication();
        }
      }
    }
  }

  public ProcessProbability findProcessProb(int evuId, ProcessType pt) {
    LinkedList probList = probData[evuId];
    if (probList == null) { return null; }

    ProcessProbability data;
    for (int i=0; i<probList.size(); i++) {
      data = (ProcessProbability)probList.get(i);
      if (data.processType.equals(pt)) { return data; }
    }
    return null;
  }

  public int getProbData(int evuId, ProcessType processType) {
    ProcessProbability data = findProcessProb(evuId, processType);
    if (data != null) { return data.probability; }
    return 0;
  }

  public boolean isProbDataLoaded() { return probDataLoaded; }

  public void readProbData(File input_file) {
    RegionalZone   zone         = Simpplle.getCurrentZone();
    Area           area         = Simpplle.getCurrentArea();
    int            maxEvuId     = area.getMaxEvuId();
    BufferedReader fin;

    probData = new LinkedList[maxEvuId+1];

    try {
      fin = new BufferedReader(new FileReader(input_file));
      readProbData(fin);
      probDataLoaded = true;
    }
    catch (IOException IOX) {
      System.out.println("Problems opening/reading input file.");
      return;
    }
    catch (ParseError e) {
      System.out.println(e.msg);
    }
  }

  private void readProbData(BufferedReader fin) throws IOException, ParseError {
    RegionalZone        zone = Simpplle.getCurrentZone();
    StringTokenizerPlus strTok;
    String              line, str;
    int                 i, count, id;
    ProcessType[]       processTypes;
    ProcessProbability  data;

    // First get the array of process id's
    line = fin.readLine();
    if (line == null) {
      throw new ParseError("Input file is empty");
    }

    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();
    str    = strTok.getToken();  // Don't need this, discard.
    if (str.equalsIgnoreCase("SLINK") == false) {
      System.out.println(str);
      throw new ParseError("Not a valid Process Probability file.");
    }

    processTypes = new ProcessType[count-1];
    for(i=0; i<processTypes.length; i++) {
      str = strTok.getToken();
      str = Utility.underscoresToDashes(str);
      processTypes[i] = ProcessType.get(str);
      if (processTypes[i] == null) {
        System.out.println(str);
        throw new ParseError("Not a valid Process Probability file.");
      }
    }

    // Now get the probability values for each unit
    line = fin.readLine();
    while (line != null) {
      strTok = new StringTokenizerPlus(line,",");

      id = strTok.getIntToken();
      if (id > probData.length) {
        probData = null;
        throw new ParseError("Invalid unit id found in file.");
      }
      probData[id] = new LinkedList();
      for(i=0; i<processTypes.length; i++) {
        data = new ProcessProbability(processTypes[i]);
        data.probability = strTok.getIntToken();
        probData[id].add(data);
      }
      line = fin.readLine();
    }
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
    TreatmentApplication app;
    RegionalZone         zone = Simpplle.getCurrentZone();
    Area                 area = Simpplle.getCurrentArea();
    String               line = null;
    int                  tStep, unitId;
    int                  count = 0;
    String               treatName;
    StringTokenizerPlus  strTok;
    boolean              newTreatmentApp = true;

    try {
      line = fin.readLine();
      if (line == null) {
        throw new SimpplleError("input file is empty.");
      }
      line = line.trim();

      do {
        strTok = new StringTokenizerPlus(line,",");
        if (strTok.countTokens() != 2) {
          throw new SimpplleError("Invalid line in input file: " + line);
        }

        // Get the time step.
        tStep = strTok.getIntToken();
        if (tStep == -1) {
          throw new SimpplleError("Invalid time step in file: " + line);
        }

        // Get the treatment Type, make sure its valid.
        TreatmentType treatType = null;
        String str = strTok.getToken();
        if (str != null) {
          str = str.trim().toUpperCase();
          treatType = TreatmentType.get(str);
        }
        if (treatType == null) {
          throw new SimpplleError("Invalid Treatment in file: " + line);
        }

        app = this.findApplication(treatType,tStep);
        newTreatmentApp = false;
        if (app == null) {
          app = new TreatmentApplication(treatType);
          app.setTimeStep(tStep);
          newTreatmentApp = true;
        }

        // Get all of the unit id's
        // Loop until end of file or we reach another treatment app.
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
            throw new SimpplleError("Invalid line in file: " + line);
          }
          else if (count == 2) {
            continue;
          }

          unitId = strTok.getIntToken();
          if (area.isValidUnitId(unitId)) {
            app.addUnitId(unitId);
          }
          else {
            throw new SimpplleError("Invalid unit id: " + line);
          }
        }
        while (count != 2 && line != null);

        app.setUseUnits(true);
        if (newTreatmentApp) {
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
    catch (IOException err) {
      throw new SimpplleError("Problems opening input file.");
    }
  }

  public void read(BufferedReader fin) throws SimpplleError {
    TreatmentApplication app;
    RegionalZone         zone;
    String               line, zoneName;
    int                  numApps;

    try {
      // Get the file version if any
      line = fin.readLine();
      if (line == null) {
        throw new ParseError("First line of file is empty, should have version number or zone name (for older file versions)");
      }
      line = line.trim();

      int fileVersion = 1;
      if (Character.isDigit(line.charAt(0))) {
        fileVersion = Integer.parseInt(line);
        line = fin.readLine();
        if (line != null) { line = line.trim(); }
      }

      // Get the name of the zone
      zoneName = line;
      zone     = Simpplle.getCurrentZone();

      if (!zone.getName().equals(zoneName)) {
        String msg = "This treatment is for the following zone only: "
                     + zoneName;
        throw new SimpplleError(msg);
      }
      // Get Number of Applications in file
      line = fin.readLine();
      if (line != null) { line = line.trim(); }

      numApps = Integer.parseInt(line);

      for(int i=0;i<numApps;i++) {
        app = new TreatmentApplication();
        app.read(fin,fileVersion);
        addApplication(app);
      }
    }
    catch (IOException e) {
      throw new SimpplleError("Problems reading input file.");
    }
    catch (ParseError e) {
      throw new SimpplleError(e.msg);
    }
    catch (NumberFormatException e) {
      throw new SimpplleError("Invalid Treatment Schedule File.");
    }
  }

  public void save(File filename) {
    File             outfile = Utility.makeSuffixedPathname(filename,"","treatment");
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
    fout.println(FILE_VERSION);

    TreatmentApplication app;
    int                  appCount = 0, i;
    Vector               apps;
    Enumeration          elements;

    fout.println(Simpplle.getCurrentZone().getName());

    elements = applications.elements();
    while (elements.hasMoreElements()) {
      apps = (Vector)elements.nextElement();
      appCount += apps.size();
    }
    fout.println(appCount);

    elements = applications.elements();
    while (elements.hasMoreElements()) {
      apps = (Vector)elements.nextElement();

      for(i=0; i<apps.size(); i++) {
        app = (TreatmentApplication)apps.elementAt(i);
        app.save(fout);
      }
    }
  }

  public TreatmentApplication newTreatment(TreatmentType treatType, int timeStep) {
    TreatmentApplication treatment;

    treatment = new TreatmentApplication(treatType);
    treatment.setTimeStep(timeStep);
    addApplication(treatment);
    return treatment;
  }

  public void addApplication(TreatmentApplication app) {
    MyInteger timeStep;
    Vector    apps;

    timeStep = new MyInteger(app.getTimeStep());
    apps     = (Vector)applications.get(timeStep);
    if (apps == null) {
      apps = new Vector();
      applications.put(timeStep,apps);
    }
    apps.addElement(app);
    numApps++;
  }

  public TreatmentApplication findApplication(TreatmentType treatType, int tStep) {
    TreatmentApplication app;
    Vector               apps;
    MyInteger            timeStep = new MyInteger(tStep);

    if (applications == null) { return null; }
    apps = (Vector)applications.get(timeStep);
    if (apps == null) { return null; }

    for(int i=0; i<apps.size(); i++) {
      app = (TreatmentApplication) apps.elementAt(i);
      if (app.getTreatmentType() == treatType) { return app; }
    }
    return null;
  }

  public Vector getApplications(int tStep) {
    return getApplications(new MyInteger(tStep));
  }
  public Vector getApplications(MyInteger tStep) {
    return (Vector)applications.get(tStep);
  }

  public MyInteger[] getAllTimeSteps() {
    if (applications.size() == 0) { return null; }
    Enumeration keys = applications.keys();
    MyInteger[]  ts = new MyInteger[applications.size()];
    int         i=0;

    while (keys.hasMoreElements()) {
      ts[i] = (MyInteger)keys.nextElement();
      i++;
    }
    Arrays.sort(ts);
    return ts;
  }

  public TreatmentApplication copyApplication(TreatmentApplication app, int newTimeStep) {
    TreatmentApplication newApp = app.duplicate();

    newApp.setTimeStep(newTimeStep);
    addApplication(newApp);
    return newApp;
  }

  public void removeApplication(TreatmentApplication app) {
    Vector apps = getApplications(app.getTimeStep());
    if (apps == null) { return; }

    if (apps.removeElement(app)) {
      numApps--;
      if (apps.size() == 0) {
        applications.remove(new MyInteger(app.getTimeStep()));
        apps = null;
      }
    }
  }

  public void setCurrentFile(File saveFile) {
    SystemKnowledge.setFile(SystemKnowledge.TREATMENT_SCHEDULE,saveFile);
    SystemKnowledge.markChanged(SystemKnowledge.TREATMENT_SCHEDULE);
  }

}

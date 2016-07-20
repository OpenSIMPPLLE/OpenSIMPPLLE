/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.text.NumberFormat;
import org.apache.commons.collections.map.MultiKeyMap;

/**
 * This class is the parent for all report classes.  It contains methods common to all reports
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public abstract class Reports {
  public static final int NORMAL        = 0;
  public static final int OWNERSHIP     = 1;
  public static final int SPECIAL_AREA  = 2;
  public static final int OWNER_SPECIAL = 3;

  protected Simulation   simulation;
  protected int          timeSteps;
  protected RegionalZone currentZone;
  protected Area         currentArea;

  /**
   * Constructor for reports.  Initialized the report to current zone, area, simulation and # of timeSteps.  
   */
  Reports () {
    currentZone = Simpplle.getCurrentZone();
    currentArea = Simpplle.getCurrentArea();
    simulation  = Simpplle.getCurrentSimulation();
    if (simulation != null) {
      timeSteps = simulation.getNumTimeSteps();
    }
    else timeSteps = 0;
  }

  /**
   * 
   * @param kind
   * @return
   */
  protected String getDivisionName(int kind) {
    switch (kind) {
      case OWNERSHIP:     return "Ownership";
      case SPECIAL_AREA:  return "Special Area";
      case OWNER_SPECIAL: return "Ownership/Special-Area";
      default: return "UNKNOWN";
    }
  }
/**
 * Defines the type of report to be generated process, state, treatment, or fire.  
 * @param filename the destination file where the report will be written.  
 * @param option the type of report 
 * @param combineLifeforms if true will combine life forms for state report
 * @throws SimpplleError if problem writing output file, caught here then thrown to be caught in GUI
 */
  public static void generateSummaryReport(File filename, int option,
                                           boolean combineLifeforms)
    throws SimpplleError
  {
    PrintWriter fout;

    ProcessReports   pReport = new ProcessReports();
    StateReports     sReport = new StateReports();
    TreatmentReports tReport = new TreatmentReports();
    FireReports      fReport = new FireReports();

    try {
      fout = new PrintWriter(new FileOutputStream(filename));

      pReport.summaryReport(fout,option);
      sReport.summaryReport(fout,option,combineLifeforms);
      tReport.summaryReport(fout,option);
      fReport.fireEventReport(fout);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      String msg = "Problems writing output file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }

  }
/**
 * Primary generateAllStatesReport summary by looping through the instances for an area states.  
 * @param rulesFile passed into addAllStatesReportRules
 * @param filename file where report will be written
 * @throws SimpplleError caught in GUI
 */
  public static void generateAllStatesReport(File rulesFile, File filename)
    throws SimpplleError
  {
    Simulation.getInstance().getAreaSummary().addAllStateReportRules(rulesFile);
    int nSteps = Simulation.getInstance().getNumTimeSteps();
    for (int ts=0; ts<=nSteps; ts++) {
      Simulation.getInstance().getAreaSummary().updateAllStatesReportSummary(ts);
    }

    generateAllStatesReport(filename);
  }
  /**
   * generateAllStatesReport to print out to file.    
   * @param filename
   * @throws SimpplleError
   */
  public static void generateAllStatesReport(File filename)
    throws SimpplleError
  {
    PrintWriter  fout;
    StateReports sReport = new StateReports();

    try {
      ArrayList<AllStatesReportData>
        dataList = Simulation.getInstance().getAreaSummary().getAllStatesReportData();

      for (int i = 0; i < dataList.size(); i++) {
        AllStatesReportData data = (AllStatesReportData) dataList.get(i);
        String desc = data.description;
        if (desc == null || desc.length() == 0) {
          desc = Integer.toString(i + 1);
        }
        String name = Utility.spacesToUnderscores(desc);
        File path;

        int nRuns = Simulation.getInstance().getNumSimulations();
        if (nRuns > 1) {
          String runStr = Integer.toString(Simulation.getCurrentRun());
          path = new File(filename + "-allstates-" + name + "-tmp" + runStr + ".txt");

          fout = new PrintWriter(new FileOutputStream(path, false));
          sReport.accumStateReport(fout, data);
          fout.flush();
          fout.close();

          if (Simulation.getCurrentRun() == (nRuns-1)) {
            path = new File(filename + "-allstates-" + name + ".txt");
            fout = new PrintWriter(new FileOutputStream(path, false));

            int totStatesCount = sReport.writeAccumStatesReportFirstLine(fout,data);

            for (int r=0; r<nRuns; r++) {
              runStr = Integer.toString(r);
              File tmpPath = new File(filename + "-allstates-" + name + "-tmp" + runStr + ".txt");
              BufferedReader fin = new BufferedReader(new FileReader(tmpPath));

              String line = fin.readLine();
              while (line != null) {
                int count = 0;
                StringTokenizer strTok = new StringTokenizer(line,",");
                while (strTok.hasMoreTokens()) {
                  if (count > 0) {
                    fout.print(",");
                  }
                  String token = strTok.nextToken();
                  fout.print(token);
                  count++;
                }
                int missingStatesCount = totStatesCount - count + 5;
                for (int m=0; m<missingStatesCount; m++) {
                  fout.print(",0");
                }
                fout.println();
                line = fin.readLine();
              }
              fin.close();
              tmpPath.delete();
            }
            fout.flush();
            fout.close();
          }
        }
        else {
          path = new File(filename + "-allstates-" + name + ".txt");

          fout = new PrintWriter(new FileOutputStream(path, true));
          sReport.accumStateReport(fout, data);
          fout.flush();
          fout.close();
        }
      }

    }
    catch (IOException IOX) {
      String msg = "Problems writing output file.";
      System.out.println(msg);
      throw new SimpplleError(msg,IOX);
    }

  }
  /**
   * TODO - find out why this method exists
   * @param descName
   */
  private void combineAllStatesFiles(String descName) {

  }
/**
 * 
 * @param filename
 * @param option
 * @param combineLifeforms
 * @throws SimpplleError
 */
  public static void generateSummaryReportCDF(File filename, int option,
                                              boolean combineLifeforms)
    throws SimpplleError
  {
    PrintWriter fout;

    ProcessReports   pReport = new ProcessReports();
    StateReports     sReport = new StateReports();
    TreatmentReports tReport = new TreatmentReports();
    FireReports      fReport = new FireReports();

    try {
      fout = new PrintWriter(new FileOutputStream(filename));

      pReport.summaryReportCDF(fout,option);
      sReport.summaryReportCDF(fout,option,combineLifeforms);
      tReport.summaryReportCDF(fout,option);
      fReport.fireEventReportCDF(fout);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      String msg = "Problems writing output file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  /**
   * Generates the tracking species report and writes to file.    
   * @param filename file the report will be written to.  
   * @throws SimpplleError IOexception that is caught here and thrown as a simpplleerror caught in GUI
   */
  public static void generateTrackingSpeciesReport(File filename)
    throws SimpplleError
  {
    PrintWriter  fout;
    try {
      {
        File path;

        int nRuns = Simulation.getInstance().getNumSimulations();
        if (nRuns > 1) {
          String runStr = Integer.toString(Simulation.getCurrentRun());
          path = new File(filename + "-trackspecies" + "-tmp" + runStr + ".txt");

          fout = new PrintWriter(new FileOutputStream(path, false));
          trackingSpeciesReport(fout);
          fout.flush();
          fout.close();

          if (Simulation.getCurrentRun() == (nRuns-1)) {
            path = new File(filename + "-trackspecies" + ".txt");
            fout = new PrintWriter(new FileOutputStream(path, false));

            int totCategoryCount = writeTrackingSpeciesReportFirstLine(fout);

            for (int r=0; r<nRuns; r++) {
              runStr = Integer.toString(r);
              File tmpPath = new File(filename + "-trackspecies" + "-tmp" + runStr + ".txt");
              BufferedReader fin = new BufferedReader(new FileReader(tmpPath));

              String line = fin.readLine();
              while (line != null) {
                int count = 0;
                StringTokenizer strTok = new StringTokenizer(line,",");
                while (strTok.hasMoreTokens()) {
                  if (count > 0) {
                    fout.print(",");
                  }
                  String token = strTok.nextToken();
                  fout.print(token);
                  count++;
                }
                int missingCategoryCount = totCategoryCount - count + 5;
                for (int m=0; m<missingCategoryCount; m++) {
                  fout.print(",0");
                }
                fout.println();
                line = fin.readLine();
              }
              fin.close();
              tmpPath.delete();
            }
            fout.flush();
            fout.close();
          }
        }
        else {
          path = new File(filename + "-trackspecies" + ".txt");

          fout = new PrintWriter(new FileOutputStream(path, true));
          trackingSpeciesReport(fout);
          fout.flush();
          fout.close();
        }
      }

    }
    catch (IOException IOX) {
      String msg = "Problems writing output file.";
      System.out.println(msg);
      throw new SimpplleError(msg,IOX);
    }

  }
/**
 * Method to write tracking species report to GUI
 * @param fout
 * @throws SimpplleError 
 */
  public static void trackingSpeciesReport(PrintWriter fout)
    throws SimpplleError
  {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0); // Don't show fractional part.
    nf.setGroupingUsed(false);

    TrackingSpeciesReportData data = TrackingSpeciesReportData.getInstance();

    HashMap<String,int[]> dataHm = data.getSummaryHm();

    if (dataHm.size() == 0) {
      fout.println("\"No Data to Report\"");
      return;
    }

    int nRuns = Simulation.getInstance().getNumSimulations();
    int cRun = Simulation.getCurrentRun();
    int nSteps = Simulation.getInstance().getNumTimeSteps() + 1;

    ArrayList<String> allCategories = data.getAllCategories();

    // run #, time #, group, state1, state2, ...
    if (nRuns == 1) {
      writeTrackingSpeciesReportFirstLine(fout);
    }
    for (int ts = 0; ts < nSteps; ts++) {
            // Check to see if this row has any data, if not skip it.
            boolean skip = true;
            for (int i = 0; i < allCategories.size(); i++) {
              String catName = allCategories.get(i);
              int[] acresData = dataHm.get(catName);

              if (acresData != null && acresData[ts] > 0) {
                skip = false;
                break;
              }
            }
            if (skip) { continue; }

            fout.print(cRun + 1);
            fout.print(",");
            fout.print(ts);

            for (int i = 0; i < allCategories.size(); i++) {
              String catName = allCategories.get(i);
              int[] acresData = (int[]) dataHm.get(catName);

              fout.print(",");
              if (acresData == null) {
                fout.print("0");
                continue;
              }
              fout.print(nf.format(Area.getFloatAcres(acresData[ts])));
            }
            fout.println();
    }
  }
/**
 * method to write the first line of a tracking species reports.  Used in tracking species report and generating tracking species report.    
 * @param fout printwriter used to write to GUI
 * @return the size of string array containing the first line of tracking species report first lines.  
 */
  public static int writeTrackingSpeciesReportFirstLine(PrintWriter fout) {
    TrackingSpeciesReportData data = TrackingSpeciesReportData.getInstance();
    ArrayList<String> allCategories = data.getAllCategories();
    fout.print("Run#,Step#");
    for (int i = 0; i < allCategories.size(); i++) {
      fout.print(",");
      fout.print((String) allCategories.get(i));
    }
    fout.println();

    return allCategories.size();
  }


}


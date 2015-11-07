package simpplle.comcode.reports;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

import simpplle.comcode.*;
import simpplle.comcode.Process;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class contains methods for a Process Reports, a type of Report.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see Reports
 */
public final class ProcessReports extends Reports {
  // simulation, currentZone, currentArea, timeSteps are inherited.
/**
 * Constructor for Process Reports. Initializes all the Reports constructor variables.
 */
  public ProcessReports () {
    super();
  }

  /**
   * Prints a Summary Report of Processes by time step and Acres
   * to standard out.
   */
  public void summaryReport () {
    PrintWriter fout = new PrintWriter(System.out);

    summaryReportNormal(fout);
    fout.flush();
  }

  /**
    * Print a Summary of Processes by time step and Acres
    * to a String.  This is called in the GUI, a specific example would be in simpplle.gui.VegSummary.java
    * This is used to display results in the GUI.
    */
  public String getSummaryReport() {
    Simulation simulation = Simpplle.getCurrentSimulation();

    if (simulation != null && simulation.existsMultipleRunSummary()) {
      return simulation.getMultipleRunSummary().processSummaryReport();
    }
    else {
      return getSingleSummaryReport();
    }
  }
/**
 * This method is private because it is only used within Process Reports to construct a single summary report for use in 
 * in the GUI.  
 * @return 
 */
  private String getSingleSummaryReport() {
    PrintWriter  fout;
    StringWriter strOut = new StringWriter();
    StringBuffer strBuf;

    fout = new PrintWriter(strOut);
    summaryReport(fout,NORMAL);
    fout.flush();
    strOut.flush();
    //strOut.close();
    return strOut.toString();
  }

  public void summaryReport(PrintWriter fout, int option) {
    if (option == NORMAL) {
      summaryReportNormal(fout);
    }
    else {
      summaryReportOwnerSpecial(fout,option);
    }
  }

  public void summaryReportCDF(PrintWriter fout, int option) {
    if (option == NORMAL) {
      summaryReportNormalCDF(fout);
    }
    else {
      summaryReportOwnerSpecialCDF(fout,option);
    }
  }
/**
 * This method is private because it is used in summaryReport method.  Prints a acres of processes, process, and time steps.   
 * @param fout
 */
  private void summaryReportNormal(PrintWriter fout) {
    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    ProcessType[] summaryProcesses = Process.getSummaryProcesses();

    Simulation simulation = Simulation.getInstance();
    AreaSummary areaSummary;
    if (simulation != null) {
      areaSummary = simulation.getAreaSummary();
    }
    else {
      areaSummary = AreaSummary.getTempInstance();
    }
    HashMap<SimpplleType,int[]> dataHm =
        areaSummary.getProcessSummaryHm(Reports.NORMAL);

    fout.println("Summary Report");
    fout.println();
    fout.println(simpplle.comcode.utility.Formatting.padLeft("ACRES OF PROCESSES (rounded)",31));
    fout.println(simpplle.comcode.utility.Formatting.padLeft("----------------------------",31));
    fout.println();

    for(int i=0;i<=timeSteps;i += 5) {
      int lastTime;
      if ( (i + 4) > timeSteps) {
        lastTime = timeSteps;
      }
      else {
        lastTime = i + 4;
      }

      fout.print(simpplle.comcode.utility.Formatting.padLeft("PROCESS",11));
      fout.println(simpplle.comcode.utility.Formatting.padLeft("time",38));

      fout.print(simpplle.comcode.utility.Formatting.padLeft("",40));
      for(int j=i;j<=lastTime;j++) {
        fout.print(simpplle.comcode.utility.Formatting.fixedField(j,9));
      }
      fout.println();
      fout.println();

      for(int j=0;j<summaryProcesses.length;j++) {
        int[] acresData = dataHm.get(summaryProcesses[j]);
        if (acresData == null) { continue; }

        String str = summaryProcesses[j].toString();
        fout.print(simpplle.comcode.utility.Formatting.fixedField(str,41,true));

        for(int k=i;k<=lastTime;k++) {
          float acres = Area.getFloatAcres(acresData[k]);
          fout.print(simpplle.comcode.utility.Formatting.fixedField(nf.format(acres),9));
        }
        fout.println();
      }
      fout.println();
      fout.println();
    }
    fout.println();
    fout.println();

    areaSummary = null;
  }
  /**
   * 
   * @param fout
   * @param kind
   */

  private void summaryReportOwnerSpecial(PrintWriter fout, int kind) {
    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    ProcessType[] summaryProcesses = Process.getSummaryProcesses();

    AreaSummary areaSummary = Simulation.getInstance().getAreaSummary();
    HashMap<String,HashMap<SimpplleType,int[]>> dataHm =
        areaSummary.getProcessSummaryHm(kind);

    fout.println("Summary Report");
    fout.println();
    fout.println(simpplle.comcode.utility.Formatting.padLeft("ACRES OF PROCESSES (rounded)",31));
    fout.println(simpplle.comcode.utility.Formatting.padLeft("----------------------------",31));
    fout.println();

    String divisionName = getDivisionName(kind);

    for (String key : dataHm.keySet()) {
      HashMap<SimpplleType,int[]> innerDataHm = dataHm.get(key);

      fout.println(divisionName + " " + key);
      fout.println();

      for(int i=0;i<=timeSteps;i += 5) {
        int lastTime;
        if ( (i + 4) > timeSteps) {
          lastTime = timeSteps;
        }
        else {
          lastTime = i + 4;
        }

        fout.print(simpplle.comcode.utility.Formatting.padLeft("PROCESS",11));
        fout.println(simpplle.comcode.utility.Formatting.padLeft("time",38));

        fout.print(simpplle.comcode.utility.Formatting.padLeft("",40));
        for(int j=i;j<=lastTime;j++) {
          fout.print(simpplle.comcode.utility.Formatting.fixedField(j,9));
        }
        fout.println();
        fout.println();

        for(int j=0;j<summaryProcesses.length;j++) {
          int[] acresData = innerDataHm.get(summaryProcesses[j]);
          if (acresData == null) { continue; }

          String str = summaryProcesses[j].toString();
          fout.print(simpplle.comcode.utility.Formatting.fixedField(str,41,true));

          for(int k=i;k<=lastTime;k++) {
            float acres = Area.getFloatAcres(acresData[k]);
            fout.print(simpplle.comcode.utility.Formatting.fixedField(nf.format(acres),9));
          }
          fout.println();
        }
        fout.println();
        fout.println();
      }
      fout.println();
      fout.println();
    }
  }

  private void summaryReportNormalCDF(PrintWriter fout) {
    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.
    nf.setGroupingUsed(false);

    ProcessType[] summaryProcesses = Process.getSummaryProcesses();

    AreaSummary areaSummary = Simulation.getInstance().getAreaSummary();
    HashMap<SimpplleType,int[]> dataHm = areaSummary.getProcessSummaryHm(Reports.NORMAL);

    fout.print("Time");
    for(int i=0; i<summaryProcesses.length; i++) {
      fout.print("," + summaryProcesses[i].toString());
    }
    fout.println();

    for(int i=0; i<=timeSteps; i++) {
      fout.print(i);

      for(int j=0;j<summaryProcesses.length;j++) {
        int[] acresData = dataHm.get(summaryProcesses[j]);
        float acres = 0;

        if (acresData != null) {
          acres = Area.getFloatAcres(acresData[i]);
        }
        fout.print("," + nf.format(acres));
      }
      fout.println();
    }
    fout.println();
  }
/**
 * Creates a special summary report for a particular ownership.  Will be in CSF (termed in OpenSimpplle as Comma Delineated Form, CDF)
 * @param fout
 * @param option
 */
  private void summaryReportOwnerSpecialCDF(PrintWriter fout, int option) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.
    nf.setGroupingUsed(false);

    ProcessType[] summaryProcesses = Process.getSummaryProcesses();

    AreaSummary areaSummary = Simulation.getInstance().getAreaSummary();
    HashMap<String,HashMap<SimpplleType,int[]>> dataHm = areaSummary.getProcessSummaryHm(option);

    String divisionName = getDivisionName(option);

    for (String key : dataHm.keySet()) {
      HashMap<SimpplleType,int[]> processTypeHm = dataHm.get(key);

      fout.println(divisionName + "-" + key);
      fout.print("Time");
      for(int i=0; i<summaryProcesses.length; i++) {
        fout.print("," + summaryProcesses[i].toString());
      }
      fout.println();

      for(int i=0; i<=timeSteps; i++) {
        fout.print(i);

        for(int j=0;j<summaryProcesses.length;j++) {
          int[] acresData = processTypeHm.get(summaryProcesses[j]);
          float acres = 0;
          if (acresData != null) {
            acres = Area.getFloatAcres(acresData[i]);
          }
          fout.print("," + nf.format(acres));
        }
        fout.println();
      }
      fout.println();
    }
  }
}



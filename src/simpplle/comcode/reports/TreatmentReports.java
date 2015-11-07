package simpplle.comcode.reports;

import simpplle.comcode.*;

import java.io.*;
import java.util.*;
import java.text.NumberFormat;

/** The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * <p>This class creates treatment reports, a type of Report.  These are called from the GUI.  For an example of this see simpplle.gui.VegSummary.java  
 *
 */


public final class TreatmentReports extends Reports {
  // simulation, currentZone, currentArea, timeSteps are inherited.

  public TreatmentReports() {
    super();
  }

  /**
    * Print a summary report in a human readable format.  This allows  an optional condition for Normal, Ownership, special area, and special-owner reports.
    * @param outputFile is a File.
    */
  public void summaryReport(PrintWriter fout, int option) {
    if (option == NORMAL) {
      summaryReportNormal(fout,Treatment.APPLIED);
      summaryReportNormal(fout,Treatment.NOT_APPLIED);
      summaryReportNormal(fout,Treatment.INFEASIBLE);
    }
    else {
      summaryReport(fout,Treatment.APPLIED,option);
      summaryReport(fout,Treatment.NOT_APPLIED,option);
      summaryReport(fout,Treatment.INFEASIBLE,option);
    }
  }

  /**
    * Output the summary report in a comma delimeted format (CDF)
    * suitable for reading into a spreadsheet program.  This also includes a variable to choose options for reports which are Normal, ownership, special area, and owner-special.
    * @param outputFile is a File.
    */
  public void summaryReportCDF(PrintWriter fout, int option) {
    if (option == NORMAL) {
      summaryReportNormalCDF(fout,Treatment.APPLIED);
      summaryReportNormalCDF(fout,Treatment.NOT_APPLIED);
      summaryReportNormalCDF(fout,Treatment.INFEASIBLE);
    }
    else {
      summaryReportCDF(fout,Treatment.APPLIED,option);
      summaryReportCDF(fout,Treatment.NOT_APPLIED,option);
      summaryReportCDF(fout,Treatment.INFEASIBLE,option);
    }
  }
/**
 * This gets the treatment summary report which is used in the Vegetative Summary JDialog of the GUI.  If it is a multiple run summary returns the string from here,
 * else it sends to getSingleSummaryReport()
 * @return a string version of the report.  
 */
  public String getSummaryReport() {
    Simulation         simulation = Simpplle.getCurrentSimulation();
    MultipleRunSummary mrSummary;

    if (simulation != null && simulation.existsMultipleRunSummary()) {
      mrSummary = simulation.getMultipleRunSummary();
      return mrSummary.treatmentSummaryReport();
    }
    else {
      return getSingleSummaryReport();
    }
  }
/**
 * Gets a single treatment summary report with normal options.  This is called in the Vegetative Summary JDialog.    
 * @return
 */
  public String getSingleSummaryReport() {
    PrintWriter  fout;
    StringWriter strOut = new StringWriter();
    StringBuffer strBuf;

    fout = new PrintWriter(strOut);
    summaryReport(fout,NORMAL);
    fout.flush();
    strOut.flush();
    return strOut.toString();
  }

  private void summaryReportNormal(PrintWriter fout, int kind) {
    Hashtable   data;
    Enumeration keys;
    String      kindName;
    String      key, str;
    int[]       acresArray;
    float       acres;
    int         lastTime, j, k;

    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    switch (kind) {
      case Treatment.APPLIED:     kindName = "Applied"; break;
      case Treatment.NOT_APPLIED: kindName = "Not Applied"; break;
      case Treatment.INFEASIBLE:  kindName = "Infeasible"; break;
      default:                    kindName = "Applied";
    }

    data = currentArea.collectTreatmentData(timeSteps,kind);
    if (data == null || data.size() == 0) {
      fout.println("No " + kindName + " Treatments");
      return;
    }

    fout.println("Treatment Summary Report");
    fout.println();
    str = "Acres of " + kindName + " Treatments (rounded)";
    fout.println(simpplle.comcode.utility.Formatting.padLeft(str,31));
    fout.println(simpplle.comcode.utility.Formatting.padLeft("------------------------------",31));
    fout.println();

    for(int i=0;i<=timeSteps;i += 5) {
      if ( (i + 4) > timeSteps) {
        lastTime = timeSteps;
      }
      else {
        lastTime = i + 4;
      }

      fout.print(simpplle.comcode.utility.Formatting.padLeft("Treatment",11));
      fout.println(simpplle.comcode.utility.Formatting.padLeft("time",38));

      fout.print(simpplle.comcode.utility.Formatting.padLeft("",40));
      for(j=i;j<=lastTime;j++) {
        fout.print(simpplle.comcode.utility.Formatting.fixedField(j,9));
      }
      fout.println();
      fout.println();

      keys = data.keys();
      while (keys.hasMoreElements()) {
        key = (String) keys.nextElement();
        acresArray = (int[]) data.get(key);
        fout.print(simpplle.comcode.utility.Formatting.fixedField(key,41,true));

        for(k=i;k<=lastTime;k++) {
          acres = Area.getFloatAcres(acresArray[k]);
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
/**
 * IF this is called, it creates a summary report, which covers the treatments applied, not applied, or infeasible.  It will include acres, treatments
 * @param fout
 * @param kind
 * @param option
 */
  private void summaryReport(PrintWriter fout, int kind, int option) {
    Hashtable   data, optionHt;
    Enumeration keys;
    String      kindName;
    String      key, str;
    int[]       acresArray;
    float       acres;
    int         lastTime, j, k;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    switch (kind) {
      case Treatment.APPLIED:     kindName = "Applied"; break;
      case Treatment.NOT_APPLIED: kindName = "Not Applied"; break;
      case Treatment.INFEASIBLE:  kindName = "Infeasible"; break;
      default:                    kindName = "Applied";
    }

    optionHt = currentArea.collectTreatmentData(timeSteps,kind,option);
    if (optionHt == null || optionHt.size() == 0) {
      fout.println("No " + kindName + " Treatments");
      return;
    }

    fout.println("Treatment Summary Report");
    fout.println();
    str = "Acres of " + kindName + " Treatments (rounded)";
    fout.println(simpplle.comcode.utility.Formatting.padLeft(str,31));
    fout.println(simpplle.comcode.utility.Formatting.padLeft("------------------------------",31));
    fout.println();

    Enumeration optionKeys = optionHt.keys();
    String      divisionName = getDivisionName(option);
    String      optionKey;

    while(optionKeys.hasMoreElements()) {
      optionKey = (String) optionKeys.nextElement();
      data = (Hashtable) optionHt.get(optionKey);

      fout.println(divisionName + " " + optionKey);
      fout.println();

      for(int i=0;i<=timeSteps;i += 5) {
        if ( (i + 4) > timeSteps) {
          lastTime = timeSteps;
        }
        else {
          lastTime = i + 4;
        }

        fout.print(simpplle.comcode.utility.Formatting.padLeft("Treatment",11));
        fout.println(simpplle.comcode.utility.Formatting.padLeft("time",38));

        fout.print(simpplle.comcode.utility.Formatting.padLeft("",40));
        for(j=i;j<=lastTime;j++) {
          fout.print(simpplle.comcode.utility.Formatting.fixedField(j,9));
        }
        fout.println();
        fout.println();

        keys = data.keys();
        while (keys.hasMoreElements()) {
          key = (String) keys.nextElement();
          acresArray = (int[]) data.get(key);
          fout.print(simpplle.comcode.utility.Formatting.fixedField(key,41,true));

          for(k=i;k<=lastTime;k++) {
            acres = Area.getFloatAcres(acresArray[k]);
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
/**
 * Creates a normal summary report in comma delineated form suitable for exporting to a spreadsheet.  
 * @param fout
 * @param kind
 */
  private void summaryReportNormalCDF(PrintWriter fout, int kind) {
    Hashtable   data;
    Enumeration keys;
    String      kindName;
    String      key, str;
    int[]       acresArray;
    float       acres;

    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.
    nf.setGroupingUsed(false);

    switch (kind) {
      case Treatment.APPLIED:     kindName = "Applied"; break;
      case Treatment.NOT_APPLIED: kindName = "Not Applied"; break;
      case Treatment.INFEASIBLE:  kindName = "Infeasible"; break;
      default:                    kindName = "Applied";
    }

    data = currentArea.collectTreatmentData(timeSteps,kind);
    if (data == null || data.size() == 0) {
      fout.println("No " + kindName + " Treatments");
      fout.println();
      return;
    }

    fout.println(kindName + "-Treatments");
    fout.print("Time");
    keys = data.keys();
    while (keys.hasMoreElements()) {
      key = (String) keys.nextElement();
      fout.print("," + key);
    }
    fout.println();

    for(int i=0; i<=timeSteps; i++) {
      fout.print(i);

      keys = data.keys();
      while (keys.hasMoreElements()) {
        key        = (String) keys.nextElement();
        acresArray = (int[]) data.get(key);
        acres      = Area.getFloatAcres(acresArray[i]);
        fout.print("," + nf.format(acres));
      }
      fout.println();
    }
    fout.println();
  }

  private void summaryReportCDF(PrintWriter fout, int kind, int option) {
    Hashtable   data, optionHt;
    Enumeration keys;
    String      kindName;
    String      key, str;
    int[]       acresArray;
    float       acres;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.
    nf.setGroupingUsed(false);

    switch (kind) {
      case Treatment.APPLIED:     kindName = "Applied"; break;
      case Treatment.NOT_APPLIED: kindName = "Not Applied"; break;
      case Treatment.INFEASIBLE:  kindName = "Infeasible"; break;
      default:                    kindName = "Applied";
    }

    optionHt = currentArea.collectTreatmentData(timeSteps,kind,option);
    if (optionHt == null || optionHt.size() == 0) {
      fout.println("No " + kindName + " Treatments");
      fout.println();
      return;
    }

    Enumeration optionKeys = optionHt.keys();
    String      divisionName = getDivisionName(option);
    String      optionKey;

    while(optionKeys.hasMoreElements()) {
      optionKey = (String) optionKeys.nextElement();
      data = (Hashtable) optionHt.get(optionKey);

      fout.println(kindName + "-Treatments--" + divisionName + "-" + optionKey);

      fout.print("Time");
      keys = data.keys();
      while (keys.hasMoreElements()) {
        key = (String) keys.nextElement();
        fout.print("," + key);
      }
      fout.println();

      for(int i=0; i<=timeSteps; i++) {
        fout.print(i);

        keys = data.keys();
        while (keys.hasMoreElements()) {
          key        = (String) keys.nextElement();
          acresArray = (int[]) data.get(key);
          acres      = Area.getFloatAcres(acresArray[i]);
          fout.print("," + nf.format(acres));
        }
        fout.println();
      }
      fout.println();
    }
  }

}

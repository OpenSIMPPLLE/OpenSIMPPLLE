package simpplle.comcode.reports;

import simpplle.comcode.AreaSummary;
import simpplle.comcode.Simpplle;

import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-EarthSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines a Fire Report, a type of Report
 *
 * Fire Process logic
 * determine all process probabilities for each evu ->use probabilities to select process
 * if selected process is fire event->if fire suppresssion ->determine probability of staying class size A due to weather or fire suppression → if yes change process for evu to succession and record a class A fire with suppression costs
 * if not suppressed at Class A level → determine type of fire and fire spread → at end of simulation calculate fire suppression costs and emissions

 * if selected process is fire and fire suppression is no, determine probability of staying class A size due to weather → if it spreads beyond class A size determine type of firefighter and fire → at end of simulation calculate emissions

 * if stays at class A size due to weather->change process for evu to succession and record class A fire
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see Reports
 */

public final class FireReports extends Reports {
  // simulation, currentZone, currentArea, timeSteps are inherited.

	/**
	 * primary constructor.  inherits from Reports superclass
	 */
  public FireReports() {
    super();
  }
/**
 * This is called in Vegetative Summary dialog if 'Fire Events' radio button selected.  
 * @return the toString of the fire report.  
 */
  public String getFireEventReport() {
    PrintWriter  fout;
    StringWriter strOut = new StringWriter();
    StringBuffer strBuf;

    fout = new PrintWriter(strOut);
    fireEventReport(fout);
    fout.flush();
    strOut.flush();
    return strOut.toString();
  }
/**
 * The report generated here is sent to the getFireEventReport() method which is then sent to the Vegetative Summary dialog if 'Fire Events' is selected.   
 * @param fout
 */
  public void fireEventReport(PrintWriter fout) {
    AreaSummary summary = Simpplle.getAreaSummary();

    if (summary == null) {
      fout.println("No Data.");
    }
    else {
      summary.printFireEventSummary(fout, timeSteps);
    }
  }
/**
 * Comma separated form (called CDF - comma delineated form)  of the Fire Event report. Used for export to spreadsheet 
 * @param fout
 */
  public void fireEventReportCDF(PrintWriter fout) {
    AreaSummary summary = Simpplle.getAreaSummary();

    summary.printFireEventSummaryCDF(fout,timeSteps);
  }
}

package simpplle.comcode;

import java.io.*;
import java.util.*;
import java.text.NumberFormat;
import org.hibernate.*;
import static simpplle.comcode.SimpplleType.*;
import org.apache.commons.collections.map.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines States Reports, a type of Report.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Reports
 *
 *
 */


public final class StateReports extends Reports {
  // simulation, currentZone, currentArea, timeSteps are inherited.

  public StateReports () {
    super();
  }

  /**
    * Print a summary report.  
    * @param outputFile is a File.
    */
  public void summaryReport(PrintWriter fout, int option, boolean combineLifeforms) {
    if (option == NORMAL) {
      stateReport(fout,SPECIES,combineLifeforms);
      stateReport(fout,SIZE_CLASS,combineLifeforms);
      stateReport(fout,DENSITY,combineLifeforms);
    }
    else {
      stateReport(fout,SPECIES,option,combineLifeforms);
      stateReport(fout,SIZE_CLASS,option,combineLifeforms);
      stateReport(fout,DENSITY,option,combineLifeforms);
    }
  }

  /**
    * Output the summary report in a comma separated form. 
    * suitable for reading into a spreadsheet program.
    * @param outputFile is a File.
    */
  public void summaryReportCDF(PrintWriter fout, int option,
                               boolean combineLifeforms) {
    if (option == NORMAL) {
      stateReportCDF(fout,SPECIES,combineLifeforms);
      stateReportCDF(fout,SIZE_CLASS,combineLifeforms);
      stateReportCDF(fout,DENSITY,combineLifeforms);
    }
    else {
      stateReportCDF(fout,SPECIES,option,combineLifeforms);
      stateReportCDF(fout,SIZE_CLASS,option,combineLifeforms);
      stateReportCDF(fout,DENSITY,option,combineLifeforms);
    }
  }

  public String getStateReport(SimpplleType.Types kind, boolean combineLifeforms) {
    Simulation         simulation = Simpplle.getCurrentSimulation();
    MultipleRunSummary mrSummary;

    if (simulation != null && simulation.existsMultipleRunSummary()) {
      mrSummary = simulation.getMultipleRunSummary();
      switch (kind) {
        case SPECIES:    return mrSummary.speciesSummaryReport();
        case SIZE_CLASS: return mrSummary.sizeClassSummaryReport();
        case DENSITY:    return mrSummary.densitySummaryReport();
        default:         return "";
      }
    }
    else {
      return getSingleStateReport(kind,combineLifeforms);
    }
  }

  private String getSingleStateReport(SimpplleType.Types kind, boolean combineLifeforms) {
    PrintWriter  fout;
    StringWriter strOut = new StringWriter();
    StringBuffer strBuf;

    fout = new PrintWriter(strOut);
    stateReport(fout,kind,combineLifeforms);
    fout.flush();
    strOut.flush();
    //strOut.close();
    return strOut.toString();
  }

  private void stateReport(PrintWriter fout, SimpplleType.Types kind, boolean combineLifeforms) {
    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    String kindName;
    switch (kind) {
      case SPECIES:    kindName = "SPECIES";    break;
      case SIZE_CLASS: kindName = "SIZE CLASS"; break;
      case DENSITY:    kindName = "DENSITY";    break;
      default: return;
    }

    Simulation simulation = Simulation.getInstance();
    AreaSummary areaSummary;
    if (simulation != null) {
      areaSummary = simulation.getAreaSummary();
    }
    else {
      areaSummary = AreaSummary.getTempInstance();
    }

    HashMap<String,int[]> dataHm =
        areaSummary.getStateSummaryHm(Reports.NORMAL,kind,combineLifeforms);


    fout.println("Summary Report");
    fout.println();
    fout.println(Formatting.padLeft("ACRES OF " + kindName + " (rounded)",31));
    fout.println(Formatting.padLeft("------------------",31));
    fout.println();

    for(int i=0;i<=timeSteps;i += 5) {
      int lastTime;
      if ( (i + 4) > timeSteps) {
        lastTime = timeSteps;
      }
      else {
        lastTime = i + 4;
      }

      fout.print(Formatting.padLeft(kindName,11));
      fout.println(Formatting.padLeft("time",38));

      fout.print(Formatting.padLeft("",40));
      for(int j=i;j<=lastTime;j++) {
        fout.print(Formatting.fixedField(j,10));
      }
      fout.println();
      fout.println();

      for (String key : dataHm.keySet()) {
        int[] acresArray = dataHm.get(key);
        fout.print(Formatting.fixedField(key,41,true));

        for(int k=i;k<=lastTime;k++) {
          float acres = Area.getFloatAcres(acresArray[k]);
          fout.print(Formatting.fixedField(nf.format(acres),10));
        }
        fout.println();
      }
      fout.println();
      fout.println();
    }
    fout.println();
    fout.println();
  }

  private void stateReport(PrintWriter fout, SimpplleType.Types stateKind, int kind,
                           boolean combineLifeforms) {
    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.

    AreaSummary areaSummary = Simulation.getInstance().getAreaSummary();
    HashMap<String,HashMap<String,int[]>> dataHm =
        areaSummary.getStateSummaryHm(kind,stateKind,combineLifeforms);


    fout.println("Summary Report");
    fout.println();
    String str = "ACRES OF " + stateKind.toString() + " (rounded)";
    fout.println(Formatting.padLeft(str,31));
    fout.println(Formatting.padLeft("------------------",31));
    fout.println();

    String  divisionName = getDivisionName(kind);
    for (String key : dataHm.keySet()) {
      HashMap<String,int[]> innerDataHm = dataHm.get(key);

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

        fout.print(Formatting.padLeft(stateKind.toString(),11));
        fout.println(Formatting.padLeft("time",38));

        fout.print(Formatting.padLeft("",40));
        for(int j=i;j<=lastTime;j++) {
          fout.print(Formatting.fixedField(j,10));
        }
        fout.println();
        fout.println();

        for (String stateKey : innerDataHm.keySet()) {
          int[] acresArray = innerDataHm.get(stateKey);
          fout.print(Formatting.fixedField(stateKey.toString(),41,true));

          for(int k=i;k<=lastTime;k++) {
            float acres = Area.getFloatAcres(acresArray[k]);
            fout.print(Formatting.fixedField(nf.format(acres),10));
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

  private void stateReportCDF(PrintWriter fout, SimpplleType.Types kind,
                              boolean combineLifeforms) {
    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.
    nf.setGroupingUsed(false);

    AreaSummary areaSummary = Simulation.getInstance().getAreaSummary();
    HashMap dataHm = areaSummary.getStateSummaryHm(Reports.NORMAL,kind,combineLifeforms);


    fout.print("Time");
    for (Object elem : dataHm.keySet()) {
      String key = (String)elem;
      fout.print("," + key);
    }
    fout.println();

    for(int i=0; i<=timeSteps; i++) {
      fout.print(i);

      for (Object elem : dataHm.keySet()) {
        String key = (String)elem;

        int[] acresArray = (int[]) dataHm.get(key);
        float acres      = Area.getFloatAcres(acresArray[i]);

        fout.print("," + nf.format(acres));
      }
      fout.println();
    }
    fout.println();
  }

  private void stateReportCDF(PrintWriter fout, SimpplleType.Types kind,
                              int option, boolean combineLifeforms) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);  // Don't show fractional part.
    nf.setGroupingUsed(false);

    String idString;
    switch (kind) {
      case SPECIES:    idString = "Species-by-";    break;
      case SIZE_CLASS: idString = "Size-Class-by-"; break;
      case DENSITY:    idString = "Density-by-";    break;
      default:
        return;
    }

    AreaSummary areaSummary = Simulation.getInstance().getAreaSummary();
    HashMap<String,HashMap<String,int[]>>
      dataHm = areaSummary.getStateSummaryHm(option,kind,combineLifeforms);

    String      divisionName = getDivisionName(option);

    for (String optionKey : dataHm.keySet()) {
      HashMap<String,int[]> innerDataHm = dataHm.get(optionKey);

      fout.println(idString + divisionName + "-" + optionKey);

      fout.print("Time");
      for (String key : innerDataHm.keySet()) {
        fout.print("," + key.toString());
      }
      fout.println();

      for(int i=0; i<=timeSteps; i++) {
        fout.print(i);

        for (String key : innerDataHm.keySet()) {
          int[] acresArray = innerDataHm.get(key);
          float acres      = Area.getFloatAcres(acresArray[i]);

          fout.print("," + nf.format(acres));
        }
        fout.println();
      }
      fout.println();
    }
  }

  public int writeAccumStatesReportFirstLine(PrintWriter fout, AllStatesReportData data) {
    ArrayList<String> allStates = data.getAllStates();
    fout.print("Run#,Step#,SpecialArea,Ownership,Group");
    for (int i = 0; i < allStates.size(); i++) {
      fout.print(",");
      fout.print((String) allStates.get(i));
    }
    fout.println();

    return allStates.size();
  }

  public void accumStateReport(PrintWriter fout, AllStatesReportData data)
    throws SimpplleError
  {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0); // Don't show fractional part.
    nf.setGroupingUsed(false);

    MultiKeyMap dataHm = data.getSummaryData();

    if (dataHm.size() == 0) {
      fout.println("\"No Matching States\"");
      return;
    }

    int nRuns = Simulation.getInstance().getNumSimulations();
    int cRun = Simulation.getCurrentRun();
    int nSteps = Simulation.getInstance().getNumTimeSteps() + 1;

    ArrayList<String> allStates = data.getAllStates();

    ArrayList<String> allGroups = data.getAllGroups();
    ArrayList<String> allSpecialArea = data.getAllSpecialArea();
    ArrayList<String> allOwnership = data.getAllOwnership();

    // run #, time #, group, state1, state2, ...
    if (nRuns == 1) {
      writeAccumStatesReportFirstLine(fout,data);
    }
    for (int ts = 0; ts < nSteps; ts++) {
      for (String saStr : allSpecialArea) {
        for (String ownStr : allOwnership) {
          for (String groupStr : allGroups) {

            // Check to see if this row has any data, if not skip it.
            boolean skip = true;
            for (int i = 0; i < allStates.size(); i++) {
              String state = allStates.get(i);
              int[] acresData = (int[]) dataHm.get(saStr, ownStr, groupStr, state);

              if (acresData != null && acresData[ts] > 0) {
                skip = false;
                break;
              }
            }
            if (skip) { continue; }

            fout.print(cRun + 1);
            fout.print(",");
            fout.print(ts);
            fout.print(",");

            fout.print(saStr);
            fout.print(",");
            fout.print(ownStr);
            fout.print(",");
            fout.print(groupStr);

            for (int i = 0; i < allStates.size(); i++) {
              String state = allStates.get(i);
              int[] acresData = (int[]) dataHm.get(saStr, ownStr, groupStr, state);

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
      }
    }
  }

}

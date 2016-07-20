/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the Process chooser, which allows user to select the kind of probability to be done for processes.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class ProcessChooser {
  private static ArrayList<CumulativeProcessProb> cumulProb =
      new ArrayList<CumulativeProcessProb>();
/**
 * Initializes the variables for an evu's process types.  
 * @param evu existing vegetative unit
 * @param processes the list of processes in an evu
 * @return
 */
  private static ProcessType init(Evu evu, ArrayList<ProcessType> processes) {
    int totalProb = 0;
    CumulativeProcessProb cumulProbData;

    cumulProb.clear();
    for (int i=0; i<processes.size(); i++) {
      ProcessProbability data = evu.findProcessProb(processes.get(i));
      totalProb += data.probability;

      cumulProbData       = new CumulativeProcessProb(data.processType);
      cumulProbData.lower = data.probability;
      cumulProb.add(cumulProbData);
    }

    ProcessProbability processProbData = evu.findProcessProb(ProcessType.SUCCESSION);
    cumulProbData = findCumulProb(ProcessType.SUCCESSION);
    processProbData.probability += 10000 - totalProb;
    cumulProbData.lower          = processProbData.probability;

    int upper=-1, lower=0;
    for(int i=0; i<processes.size(); i++) {
      cumulProbData = findCumulProb(processes.get(i));
      if (cumulProbData.lower == 0) {
        cumulProbData.lower = -1;
        cumulProbData.upper = -1;
      }
      else {
        upper += cumulProbData.lower;
        cumulProbData.lower = lower;
        cumulProbData.upper = upper;
        lower = upper + 1;
      }
    }

    return null;
  }
  /**
   * Does the stochastic probability of a process type in the simulation evu.  Returns process type succession as default
   * @param evu
   * @param processes
   * @return the process type, returns succession as default
   */
  public static ProcessType doStochastic(Evu evu, ArrayList<ProcessType> processes) {
    init(evu,processes);

    int                   randNum = Simulation.getInstance().random();
    CumulativeProcessProb cumulProbData;

    for(int i=0; i<processes.size(); i++) {
      cumulProbData = findCumulProb(processes.get(i));

      if ((randNum >= cumulProbData.lower) && (randNum <= cumulProbData.upper)) {
        return processes.get(i);
      }
    }

    // just in case.
    return ProcessType.SUCCESSION;
  }
  /**
   * Calculates the highest probability for the processes within a given EVU.   
   * @param evu used to find the process probabilities.  
   * @param processes all the processes in a particular evu
   * @return the highest process probability.  By default this is set to SUCCESSION.  
   */
  public static ProcessType doHighest(Evu evu, ArrayList<ProcessType> processes) {
    ProcessType   selectedType = ProcessType.SUCCESSION;
    int           highestProb = 0, prob;

    for(int i=0; i<processes.size(); i++) {
      ProcessProbability data = evu.findProcessProb(processes.get(i));
      if (data.probability > highestProb) {
        highestProb = data.probability;
        selectedType = processes.get(i);
      }
    }

    return selectedType;
  }

  private static CumulativeProcessProb findCumulProb(ProcessType pt) {
    if (pt == null) { return null; }

    CumulativeProcessProb data;
    for (int i=0; i<cumulProb.size(); i++) {
      data = (CumulativeProcessProb)cumulProb.get(i);
      if (data.processType.equals(pt)) { return data; }
    }
    return null;
  }

}



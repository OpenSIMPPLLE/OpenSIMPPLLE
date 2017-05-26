/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.ArrayList;
import java.io.*;
import java.util.zip.*;
import java.util.*;

/**
 *
 *
 * @deprecated No longer used except for loading old data.
 */

public class FireSpreadDataNewerLegacy {
  public static final Structure NON_FOREST     = FireEvent.NON_FOREST;
  public static final Structure SINGLE_STORY   = FireEvent.SINGLE_STORY;
  public static final Structure MULTIPLE_STORY = FireEvent.MULTIPLE_STORY;

  public static final Integer NF_OBJ = new Integer(NON_FOREST.ordinal());
  public static final Integer SS_OBJ = new Integer(SINGLE_STORY.ordinal());
  public static final Integer MS_OBJ = new Integer(MULTIPLE_STORY.ordinal());

  public static final int LOW      = FireEvent.LOW;
  public static final int MODERATE = FireEvent.MODERATE;
  public static final int HIGH     = FireEvent.HIGH;

  public static final int LMSF_CLASS = FireEvent.LMSF_CLASS;
  public static final int SRF_CLASS  = FireEvent.SRF_CLASS;

  // Relative Position
  public static final int A  = FireEvent.A;
  public static final int BN = FireEvent.BN;

  private static final int NUM_STRUCTURE      = 3;
  private static final int NUM_RESISTANCE     = 3;
  private static final int NUM_PROCESS_CLASS  = 2;
  private static final int NUM_POSITION       = 2;

  private static boolean dataChanged;

  public static boolean hasDataChanged() { return dataChanged; }
  private static void markDataChanged() {
    setDataChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A);
  }
  private static void setDataChanged(boolean value) { dataChanged = value; }

  private static void setDataFilename(File file) {
    SystemKnowledge.setFile(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A,file);
    SystemKnowledge.markChanged(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A);
  }

  public static void clearDataFilename() {
    SystemKnowledge.clearFile(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A);
  }

  public static void closeDataFile() {
    clearDataFilename();
    setDataChanged(false);
  }

  public static ArrayList getData(int fireClass, int relativePosition,
                                  Structure structure, int resistance) {
      return data[fireClass][relativePosition][structure.ordinal()][resistance];
  }

  private static ArrayList[][][][] data =
      new ArrayList[NUM_PROCESS_CLASS][NUM_POSITION][NUM_STRUCTURE][NUM_RESISTANCE];

  public static void clearData() {
    data = new ArrayList[NUM_PROCESS_CLASS][NUM_POSITION][NUM_STRUCTURE][NUM_RESISTANCE];
  }
  public static class FireSpreadDataEntry {
    public class DensityRule {
      public SizeClass[]   sizeClasses;
      public ProcessType[] processes;
      public Climate.Season season; // Added 4 Mar 2005
      public ProcessType   average, extreme;
      public int           position;

      public DensityRule() {
        sizeClasses = null;
        processes   = null;
        season      = Climate.Season.YEAR;
        average     = ProcessType.LSF;
        extreme     = ProcessType.LSF;
      }

      public void setAverage(ProcessType value) {
        markDataChanged();
        average = value;
      }

      public void setExtreme(ProcessType value) {
        markDataChanged();
        extreme = value;
      }

      public void setSeason(Climate.Season season) {
        this.season = season;
      }

      public void setFromLegacyData(int fireClass, int position, int resistance) {
        if (densities == null) { return; }

        SizeClass     sizeClass = sizeClasses[0];
        Density       density   = densities[0];
        ProcessType   process   = (processes != null) ? processes[0] : ProcessType.SUCCESSION;

        average = FireSpreadDataLegacy.getData(
            fireClass,position,resistance,sizeClass,density,process,false);
        extreme = FireSpreadDataLegacy.getData(
            fireClass,position,resistance,sizeClass,density,process,true);
      }

      public void setPosition(int newPosition) { position = newPosition; }
      public int getPosition() { return position; }

      public ProcessType getFireType(boolean isExtreme) {
        return (isExtreme) ? extreme : average;
      }
      public boolean isMatch(SizeClass sizeClass, ProcessType process, Climate.Season season) {
        int i;
        boolean match=false;
        // First check for a Size Class match.
        if (sizeClass != null) {
          if (sizeClasses == null) { return false; }
          for (i=0; i<sizeClasses.length; i++) {
            if (sizeClasses[i].equals(sizeClass)) {
              match = true;
              break;
            }
          }
          if (!match) { return false; }
        }

        match = false;
        if (processes != null) {
          if (process == null) { return false; }
          for (i=0; i<processes.length; i++) {
            if (processes[i] != null && processes[i].equals(process)) {
              match = true;
              break;
            }
          }
          if (!match) { return false; }
        }
        if (!match) { return false; }

        if (this.season != Climate.Season.YEAR && this.season != season) { return false; }

        return true;
      }

      public void update(SizeClass[] sizeClasses, ProcessType[] processes) {
        if (sizeClasses == null && processes == null) {
          return;
        }

        if (Arrays.equals(sizeClasses,this.sizeClasses) == false) {
          this.sizeClasses = sizeClasses;
          markDataChanged();
        }

        if (Arrays.equals(processes,this.processes) == false) {
          this.processes = processes;
          markDataChanged();
        }
      }

      public DensityRule duplicate() {
        DensityRule rule = new DensityRule();
        int i;

        if (sizeClasses != null) {
          rule.sizeClasses = new SizeClass[sizeClasses.length];
          for (i=0; i<sizeClasses.length; i++) {
            rule.sizeClasses[i] = sizeClasses[i];
          }
        }

        if (processes != null) {
          rule.processes = new ProcessType[processes.length];
          for (i=0; i<processes.length; i++) {
            rule.processes[i] = processes[i];
          }
        }

        rule.average = average;
        rule.extreme = extreme;

        return rule;
      }
    }

    public Density[] densities;
    public ArrayList densityRules;
    public Structure structure;

    public FireSpreadDataEntry(int numDensities, int numRules, Structure structure) {
      densities    = new Density[numDensities];
      densityRules = new ArrayList(numRules);
      this.structure = structure;
    }
    public FireSpreadDataEntry(int numDensities, Structure structure) {
      this(numDensities,5,structure);
    }

    public void moveRule(int oldPosition, int newPosition) {
      DensityRule rule = (DensityRule)densityRules.get(oldPosition);
      densityRules.remove(oldPosition);
      if (newPosition > densityRules.size()) {
        densityRules.add(0,rule);
        rule.setPosition(0);
      }
      else if (newPosition == -1) {
        rule.setPosition(densityRules.size());
        densityRules.add(rule);
      }
      else {
        rule.setPosition(newPosition);
        densityRules.add(newPosition, rule);
      }
      markDataChanged();
    }
    public void updateDensities(Vector v) {
      Density[] newData = (Density[])v.toArray(new Density[v.size()]);
      updateDensities(newData);
    }
    public void updateDensities(Density[] densities) {
      if (Arrays.equals(densities,this.densities)) {
        return;
      }

      this.densities = densities;
      markDataChanged();
    }

    public boolean isMemberDensity(Density density) {
      if (densities == null) { return false; }
      for (int i=0; i<densities.length; i++) {
        if (densities[i].equals(density)) { return true; }
      }
      return false;
    }

    public FireSpreadDataEntry duplicate() {
      FireSpreadDataEntry entry =
          new FireSpreadDataEntry(densities.length,densityRules.size(),structure);

      int i;
      for (i=0; i<entry.densities.length; i++) {
        entry.densities[i] = densities[i];
      }

      DensityRule rule;
      for (i=0; i<densityRules.size(); i++) {
        rule = (DensityRule)densityRules.get(i);
        rule.setPosition(entry.densityRules.size());
        entry.densityRules.add(rule.duplicate());
      }
      return entry;
    }

    public DensityRule makeNewRule() {
      DensityRule rule = new DensityRule();
//      rule.setPosition(densityRules.size());
//      densityRules.add(rule);
      return rule;
    }

    public void addDensityRule(SizeClass[] sizeClasses, ProcessType[] processes) {
      DensityRule rule = makeNewRule();

      rule.sizeClasses = sizeClasses;
      rule.processes   = processes;
      rule.position    = densityRules.size();
      densityRules.add(rule);
      markDataChanged();
    }

    public void addDensityRule(DensityRule rule) {
      markDataChanged();
      rule.setPosition(densityRules.size());
      densityRules.add(rule);
    }

    public void deleteRule(DensityRule rule) {
      boolean updatePosition=false;
      DensityRule tmpRule;
      int         deleteIndex=-1;

      for (int i=0; i<densityRules.size(); i++) {
        tmpRule = (DensityRule)densityRules.get(i);
        if (updatePosition) {
          tmpRule.setPosition(tmpRule.getPosition()-1);
        }
        if (tmpRule == rule) {
          deleteIndex = i;
          updatePosition = true;
        }
      }
      if (deleteIndex != -1) { densityRules.remove(deleteIndex); }
    }

    public DensityRule findRule(SizeClass sizeClass, ProcessType process, Climate.Season season) {
      DensityRule rule;
      for (int i=0; i<densityRules.size(); i++) {
        rule = (DensityRule)densityRules.get(i);
        if (rule.isMatch(sizeClass,process,season)) { return rule; }
      }
      return null;
    }
    public void setFromLegacyData(int fireClass, int position, int resistance) {
      DensityRule rule;
      for (int i=0; i<densityRules.size(); i++) {
        rule = (DensityRule)densityRules.get(i);
        rule.setFromLegacyData(fireClass,position,resistance);
      }
    }
  }

  public static FireSpreadDataEntry findEntry(int fireClass, int position,
                                              Structure structure, int resistance,
                                              Density density) {
    FireSpreadDataEntry entry;
    ArrayList           entryList = getData(fireClass,position,structure,resistance);
    if (entryList == null) { return null; }

    for (int i=0; i<entryList.size(); i++) {
      entry = (FireSpreadDataEntry)entryList.get(i);
      if (entry.isMemberDensity(density)) { return entry; }
    }
    return null;
  }

  public static ProcessType getTypeOfFire(ProcessType process, int position,
                                          int resistance, boolean extreme,
                                          Evu evu, Climate.Season season)
  {
    VegSimStateData state = evu.getState();
    if (state == null) { return null; }
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();

    int           cTime       = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    ProcessType   prevProcess = evu.getProcessLastSeason(cTime);
    int           fireClass;

    if (process.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
        process.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
      fireClass = LMSF_CLASS;
    }
    else if (process.equals(ProcessType.STAND_REPLACING_FIRE)) {
      fireClass = SRF_CLASS;
    }
    else { return null; }

    FireSpreadDataEntry.DensityRule rule;
    FireSpreadDataEntry             entry;

    entry = findEntry(fireClass,position,sizeClass.getStructure(),resistance,density);
    if (entry != null) {
      rule = entry.findRule(sizeClass,prevProcess,season);
      if (rule != null) { return rule.getFireType(extreme); }
    }
    return null;
  }
  public static void deleteEntry(int fireClass, int relativePosition,
                                 Structure structure, int resistance,
                                 FireSpreadDataEntry entry) {
    ArrayList dataList =
        getData(fireClass,relativePosition,structure,resistance);
    for (int i=0; i<dataList.size(); i++) {
      if ((FireSpreadDataEntry)dataList.get(i) == entry) {
        dataList.remove(i);
        return;
      }
    }
  }

  public static void copyTo(int copyFireClass, int copyRelativePosition,
                            Structure copyStructure, int copyResistance,
                            int toFireClass, int toRelativePosition,
                            Structure toStructure, int toResistance)
  {
    ArrayList copyData = getData(copyFireClass, copyRelativePosition,
                                 copyStructure, copyResistance);

    if (getData(toFireClass,toRelativePosition,toStructure,toResistance) == null) {
      data[toFireClass][toRelativePosition][toStructure.ordinal()][toResistance] =
          new ArrayList(copyData.size());
    }
    ArrayList toData = getData(toFireClass,toRelativePosition,toStructure,toResistance);
    toData.clear();
    toData.ensureCapacity(copyData.size());

    for (int i=0; i<copyData.size(); i++) {
      toData.add(((FireSpreadDataEntry)copyData.get(i)).duplicate());
    }
    markDataChanged();
  }

  // Modified this 19 May 2004 to copy only to all screens within the copy structure.
  // Most of the time the screens are the same except for the different structures
  // have different size class groups.
  public static void copyToAll(int copyFireClass, int copyRelativePosition,
                               Structure copyStructure, int copyResistance) {
    Structure structure = copyStructure;

    for (int fireClass=0; fireClass<data.length; fireClass++) {
      for (int relativePosition=0; relativePosition<data[fireClass].length; relativePosition++) {
        for (int resistance=0; resistance<data[fireClass][relativePosition][structure.ordinal()].length; resistance++) {
          if ((fireClass == copyFireClass) &&
              (copyRelativePosition == relativePosition) &&
              (copyResistance == resistance)) {
            continue;
          }
          copyTo(copyFireClass, copyRelativePosition, copyStructure,
                 copyResistance, fireClass, relativePosition, structure,
                 resistance);
        }
      }
    }
    markDataChanged();
  }

// // Old version.
//  public static void copyToAll(int copyFireClass, int copyRelativePosition,
//                               int copyStructure, int copyResistance) {
//    for (int fireClass=0; fireClass<data.length; fireClass++) {
//      for (int relativePosition=0; relativePosition<data[fireClass].length; relativePosition++) {
//        for (int structure=0; structure<data.length; structure++) {
//          for (int resistance=0; resistance<data[structure].length; resistance++) {
//            if ((structure == copyStructure) && (resistance == copyResistance)) {
//              continue;
//            }
//            copyTo(copyFireClass, copyRelativePosition, copyStructure,
//                   copyResistance, fireClass, relativePosition, structure,
//                   resistance);
//          }
//        }
//      }
//    }
//    markDataChanged();
//  }

  public static FireSpreadDataEntry addDensityGroup(int fireClass,
                                                    int relativePosition, Structure structure, int resistance, Density[] densities)
  {
    if (data[fireClass][relativePosition][structure.ordinal()][resistance] == null) {
      data[fireClass][relativePosition][structure.ordinal()][resistance] = new ArrayList();
    }
    FireSpreadDataEntry entry = new FireSpreadDataEntry(densities.length,structure);
    entry.updateDensities(densities);
    data[fireClass][relativePosition][structure.ordinal()][resistance].add(entry);
    return entry;
  }

  private static boolean isMemberDensity(Density density, int fireClass,
                                         int relativePosition, Structure structure,
                                         int resistance) {
    FireSpreadDataEntry entry;

    if (data[fireClass][relativePosition][structure.ordinal()][resistance] == null) {
      return false;
    }
    for (int i=0; i<data[fireClass][relativePosition][structure.ordinal()][resistance].size(); i++) {
      entry = (FireSpreadDataEntry) data[fireClass][relativePosition][structure.ordinal()][resistance].get(i);
      if (entry.isMemberDensity(density)) {
        return true;
      }
    }
    return false;
  }

  public static Vector getPossibleDensities(int fireClass, int relativePosition,
                                            Structure structure, int resistance) {
    Vector    densities = HabitatTypeGroup.getValidDensity();
    Vector    v = new Vector();
    Density density;
    for (int i=0; i<densities.size(); i++) {
      density = (Density)densities.elementAt(i);
      if (isMemberDensity(density,fireClass,relativePosition,structure,resistance) == false) {
        v.addElement(density);
      }
    }
    return v;
  }

  public static void readData(File file) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(file));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      setDataFilename(file);
      readData(fin);
    }
    catch (IOException ex) {
      throw new SimpplleError("Unable to write output file");
    }
  }

  public static void setFromLegacyData() {
    FireSpreadDataEntry entry;

    for (int fc=0; fc<data.length; fc++) {
      for (int pos=0; pos<data[fc].length; pos++) {
        for (int s=0; s<data[fc][pos].length; s++) {
          for (int r=0; r<data[fc][pos][s].length; r++) {
            if (data[fc][pos][s][r] == null) { continue; }
            for (int i=0; i<data[fc][pos][s][r].size(); i++) {
              entry = (FireSpreadDataEntry) data[fc][pos][s][r].get(i);
              entry.setFromLegacyData(fc,pos,r);
            }
          }
        }
      }
    }
  }
  public static void readData(BufferedReader fin) {
    String              line, str;
    StringTokenizerPlus strTok, strListTok;
    int                 fireClass=0, relativePosition=0, resistance=0;
    Structure structure;
    int                 numEntries=0, numRules=0;
    ProcessType         fireType;

    try {
      line = fin.readLine();
      if (line.startsWith(SystemKnowledge.KNOWLEDGE_SOURCE_KEYWORD)) {
        SystemKnowledge.readKnowledgeSource(fin,SystemKnowledge.FIRE_SPREAD_LOGIC);
        line = fin.readLine();
      }
      else {
        SystemKnowledge.setKnowledgeSource(SystemKnowledge.FIRE_SPREAD_LOGIC,"");
      }

      while (line != null) {

        strTok = new StringTokenizerPlus(line, ",");
        str = strTok.getToken();
        if (str.equals("LMSF")) {
          fireClass = LMSF_CLASS;
        }
        else if (str.equals("SRF")) {
          fireClass = SRF_CLASS;
        }

        str = strTok.getToken();
        if (str.equals("A")) {
          relativePosition = A;
        }
        else if (str.equals("BN")) {
          relativePosition = BN;
        }

        str = strTok.getToken();
        if (str.equals("NF")) {
          structure = NON_FOREST;
        }
        else if (str.equals("SS")) {
          structure = SINGLE_STORY;
        }
        else if (str.equals("MS")) {
          structure = MULTIPLE_STORY;
        }
        else { structure = NON_FOREST; }

        str = strTok.getToken();
        if (str.equals("LOW")) {
          resistance = LOW;
        }
        else if (str.equals("MODERATE")) {
          resistance = MODERATE;
        }
        else if (str.equals("HIGH")) {
          resistance = HIGH;
        }

        numEntries = strTok.getIntToken();
        data[fireClass][relativePosition][structure.ordinal()][resistance] = new ArrayList(numEntries);

        int i, j, k, count;
        FireSpreadDataEntry entry;
        FireSpreadDataEntry.DensityRule entryRule;

        for (i = 0; i < numEntries; i++) {
          line = fin.readLine();
          strTok = new StringTokenizerPlus(line, ",");

          numRules = strTok.getIntToken();

          strListTok = new StringTokenizerPlus(strTok.getToken(), ":");
          count = strListTok.countTokens();
          entry = new FireSpreadDataEntry(count, numRules,structure);

          data[fireClass][relativePosition][structure.ordinal()][resistance].add(entry);
          for (j = 0; j < count; j++) {
            entry.densities[j] = Density.getOrCreate(strListTok.getToken());
          }

          for (j = 0; j < numRules; j++) {
            entryRule = entry.makeNewRule();
            entry.addDensityRule(entryRule);

            line = fin.readLine();
            strTok = new StringTokenizerPlus(line, ",");

            // Read the Size Class list
            str = strTok.getToken();
            if (str != null) {
              strListTok = new StringTokenizerPlus(str, ":");
              count = strListTok.countTokens();
              entryRule.sizeClasses = new SizeClass[count];
              for (k = 0; k < count; k++) {
                entryRule.sizeClasses[k] = SizeClass.get(strListTok.getToken(), true);
              }
            }

            // Read the Process List (if any)
            str = strTok.getToken();
            if (str != null) {
              strListTok = new StringTokenizerPlus(str, ":");
              count = strListTok.countTokens();
              entryRule.processes = new ProcessType[count];
              for (k = 0; k < count; k++) {
                ProcessType p = ProcessType.get(strListTok.getToken());
                if (p == ProcessType.SRF_SPRING) {
                  p = ProcessType.STAND_REPLACING_FIRE;
                  entryRule.season = Climate.Season.SPRING;
                }
                else if (p == ProcessType.SRF_SUMMER) {
                  p = ProcessType.STAND_REPLACING_FIRE;
                  entryRule.season = Climate.Season.SUMMER;
                }
                else if (p == ProcessType.SRF_FALL) {
                  p = ProcessType.STAND_REPLACING_FIRE;
                  entryRule.season = Climate.Season.FALL;
                }
                else if (p == ProcessType.SRF_WINTER) {
                  p = ProcessType.STAND_REPLACING_FIRE;
                  entryRule.season = Climate.Season.WINTER;
                }
                entryRule.processes[k] = p;
              }
            }

            // Read the Type of Fire Values
            // Average
            str = strTok.getToken();
            if (str != null) {
              fireType = ProcessType.get(str);
              if (fireType != null) {
                entryRule.average = fireType;
              }
            }

            // Extreme
            str = strTok.getToken();
            if (str != null) {
              fireType = ProcessType.get(str);
              if (fireType != null) {
                entryRule.extreme = fireType;
              }
            }

            // Season
            if (strTok.hasMoreTokens()) {
              str = strTok.getToken();
              if (str != null) {
                entryRule.season = Climate.Season.valueOf(str);
              }
            }
          }
        }
        line = fin.readLine();
      }
      setDataChanged(false);
    }
    catch (IOException ex) {
    }
    catch (ParseError ex) {
    }

  }

//  public static void saveData(File file) throws SimpplleError {
//    File             outfile = Utility.makeSuffixedPathname(file,"","firespreaddata");
//    GZIPOutputStream out;
//    PrintWriter      fout;
//
//    try {
//      out = new GZIPOutputStream(new FileOutputStream(outfile));
//      fout = new PrintWriter(out);
//      saveData(fout);
//      setDataFilename(file);
//      fout.flush();
//      fout.close();
//    }
//    catch (IOException ex) {
//      throw new SimpplleError("Unable to open output file.");
//    }
//  }
//  public static void saveData(PrintWriter fout) {
//    int i,j,k,l,m,n;
//    FireSpreadDataEntry entry;
//    FireSpreadDataEntry.DensityRule entryRule;
//
//    SystemKnowledge.writeKnowledgeSource(fout,SystemKnowledge.FIRE_SPREAD_LOGIC);
//
//    for (i=0; i<data.length; i++) {
//      for (j = 0; j < data[i].length; j++) {
//        for (k = 0; k < data[i][j].length; k++) {
//          for (l = 0; l < data[i][j][k].length; l++) {
//            if (data[i][j][k][l] == null || data[i][j][k][l].size() == 0) {
//              continue;
//            }
//
//            switch (i) {
//              case LMSF_CLASS:
//                fout.print("LMSF,");
//                break;
//              case SRF_CLASS:
//                fout.print("SRF,");
//                break;
//            }
//            switch (j) {
//              case A:
//                fout.print("A,");
//                break;
//              case BN:
//                fout.print("BN,");
//                break;
//            }
//            SizeClass.Structure structure = SizeClass.Structure.lookupOrdinal(k);
//            switch (structure) {
//              case NON_FOREST:
//                fout.print("NF,");
//                break;
//              case SINGLE_STORY:
//                fout.print("SS,");
//                break;
//              case MULTIPLE_STORY:
//                fout.print("MS,");
//                break;
//            }
//            switch (l) {
//              case LOW:
//                fout.print("LOW,");
//                break;
//              case MODERATE:
//                fout.print("MODERATE,");
//                break;
//              case HIGH:
//                fout.print("HIGH,");
//                break;
//            }
//            fout.println(data[i][j][k][l].size());
//
//            for (m = 0; m < data[i][j][k][l].size(); m++) {
//              entry = (FireSpreadDataEntry) data[i][j][k][l].get(m);
//
//              fout.print(entry.densityRules.size());
//              fout.print(",");
//              Utility.printArray(fout, entry.densities, ":");
//              fout.println();
//
//              for (n = 0; n < entry.densityRules.size(); n++) {
//                entryRule = (FireSpreadDataEntry.DensityRule) entry.densityRules.get(n);
//                Utility.printArray(fout, entryRule.sizeClasses, ":");
//                fout.print(",");
//                Utility.printArray(fout, entryRule.processes, ":");
//                fout.print(",");
//                fout.print(entryRule.average);
//                fout.print(",");
//                fout.print(entryRule.extreme);
//                fout.print(",");
//                fout.print(entryRule.season.toString());
//                fout.println();
//              }
//            }
//          }
//        }
//      }
//    }
//    setDataChanged(false);
//  }

  public static void convertToFireSpreadLogic() {
    FireSpreadDataEntry entry;

    FireEventLogic.getInstance().clearData(FireEventLogic.SPREAD_STR);
    for (int p=0; p<NUM_PROCESS_CLASS; p++) {
      ArrayList<ProcessType> originProcessList = new ArrayList<ProcessType>(3);
      if (p == LMSF_CLASS) {
        originProcessList.add(ProcessType.LSF);
        originProcessList.add(ProcessType.MSF);
      }
      else if (p == SRF_CLASS) {
        originProcessList.add(ProcessType.SRF);
      }
      for (int pos=0; pos < NUM_POSITION; pos++) {
        for (int s = 0; s < NUM_STRUCTURE; s++) {
          for (int r = 0; r < NUM_RESISTANCE; r++) {
            ArrayList speciesList = null;
            FireResistance fireResist;
            switch (r) {
              case LOW:
                fireResist = FireResistance.LOW;
                break;
              case MODERATE:
                fireResist = FireResistance.MODERATE;
                break;
              case HIGH:
                fireResist = FireResistance.HIGH;
                break;
              default:
                continue;
            }
            if (data[p][pos][s][r] == null) {
              continue;
            }
            for (int i = 0; i < data[p][pos][s][r].size(); i++) {
              entry = (FireSpreadDataEntry) data[p][pos][s][r].get(i);

              ArrayList<SimpplleType> densities = null;
              if (entry.densities != null) {
                densities = new ArrayList<SimpplleType>();
                for (Density density : entry.densities) {
                  densities.add(density);
                }
                if (densities.size() == 0) {
                  densities = null;
                }
              }
              FireSpreadDataEntry.DensityRule rule;

              ArrayList<FireEvent.Position> positions;
              for (int j = 0; j < entry.densityRules.size(); j++) {
                positions = new ArrayList<FireEvent.Position>(3);
                rule = (FireSpreadDataEntry.DensityRule) entry.densityRules.get(j);
                if (pos == FireEvent.A) {
                  positions.add(FireEvent.ABOVE);
                 }
                else {
                  positions.add(FireEvent.BELOW);
                  positions.add(FireEvent.NEXT_TO);
                }

                FireEventLogic.getInstance().addLegacyData(fireResist,
                                            rule.sizeClasses,
                                            densities,
                                            rule.processes,
                                            rule.season,
                                            originProcessList,
                                            positions,
                                            rule.average,
                                            rule.extreme);
              }
            }
          }
        }
      }
    }
//    FireSpreadLogic.combineCompatibleRules();
  }


  public FireSpreadDataNewerLegacy() {

  }
}









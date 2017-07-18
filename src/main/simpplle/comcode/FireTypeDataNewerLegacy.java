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
 * This class defines Fire Type Data Newer Legacy,.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.BaseLogic
 */

public abstract class FireTypeDataNewerLegacy {
  public static final Structure NON_FOREST     = Structure.NON_FOREST;
  public static final Structure SINGLE_STORY   = Structure.SINGLE_STORY;
  public static final Structure MULTIPLE_STORY = Structure.MULTIPLE_STORY;

  public static final Integer NF_OBJ = new Integer(NON_FOREST.ordinal());
  public static final Integer SS_OBJ = new Integer(SINGLE_STORY.ordinal());
  public static final Integer MS_OBJ = new Integer(MULTIPLE_STORY.ordinal());


  public static final int LOW      = FireEvent.LOW;
  public static final int MODERATE = FireEvent.MODERATE;
  public static final int HIGH     = FireEvent.HIGH;

  private static final int NUM_STRUCTURE      = 3;
  private static final int NUM_RESISTANCE     = 3;

  private static boolean dataChanged;

  private static Hashtable structureSizeClassHt = new Hashtable();

  public static boolean hasDataChanged() { return dataChanged; }
  private static void markDataChanged() {
    setDataChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.FIRE_TYPE_LOGIC);
  }
  private static void setDataChanged(boolean value) { dataChanged = value; }

  private static void setDataFilename(File file) {
    SystemKnowledge.setFile(SystemKnowledge.FIRE_TYPE_LOGIC,file);
    SystemKnowledge.markChanged(SystemKnowledge.FIRE_TYPE_LOGIC);
  }

  public static void clearDataFilename() {
    SystemKnowledge.clearFile(SystemKnowledge.FIRE_TYPE_LOGIC);
  }

  public static void closeDataFile() {
    clearDataFilename();
    setDataChanged(false);
  }

  public static class FireTypeDataEntry {
    public class SizeClassRule {
      public Density[]       densities;
      public boolean         anyTreatmentExcept;
      public TreatmentType[] treatments;
      public ProcessType[]   processes;
      public Climate.Season  season;
      public ProcessType     wetter, normal, drier;
      public int             position;

      public SizeClassRule() {
        densities          = null;
        anyTreatmentExcept = false;
        treatments         = null;
        processes          = null;
        season             = Climate.Season.YEAR;
        wetter             = ProcessType.LSF;
        normal             = ProcessType.LSF;
        drier              = ProcessType.LSF;
      }

      public void setWetter(ProcessType value) {
        markDataChanged();
        wetter = value;
      }

      public void setNormal(ProcessType value) {
        markDataChanged();
        normal = value;
      }

      public void setDrier(ProcessType value) {
        markDataChanged();
        drier = value;
      }

      public void setSeason(Climate.Season season) {
        this.season = season;
      }

      public void setFromLegacyData(int resistance) {
        if (densities == null) { return; }

        SizeClass     sizeClass = sizeClasses[0];
        Density       density   = densities[0];
        TreatmentType treatment = (treatments != null) ? treatments[0] : TreatmentType.NONE;
        ProcessType   process   = (processes != null) ? processes[0] : ProcessType.SUCCESSION;

        wetter = FireTypeDataLegacy.getData(resistance,sizeClass,density,treatment,process, Moisture.WETTER);
        normal = FireTypeDataLegacy.getData(resistance,sizeClass,density,treatment,process, Moisture.NORMAL);
        drier  = FireTypeDataLegacy.getData(resistance,sizeClass,density,treatment,process, Moisture.DRIER);
      }

      public void setPosition(int newPosition) { position = newPosition; }
      public int getPosition() { return position; }

      public void update(Density[] densities, boolean anyTreatmentExcept,
                         TreatmentType[] treatments, ProcessType[] processes) {
        if (densities == null && treatments == null && processes == null) {
          return;
        }

        if (Arrays.equals(densities,this.densities) == false) {
          this.densities = densities;
          markDataChanged();
        }

        if (anyTreatmentExcept != this.anyTreatmentExcept) {
          this.anyTreatmentExcept = anyTreatmentExcept;
          markDataChanged();
        }

        if (Arrays.equals(treatments,this.treatments) == false) {
          this.treatments = treatments;
          markDataChanged();
        }

        if (Arrays.equals(processes,this.processes) == false) {
          this.processes = processes;
          markDataChanged();
        }
      }
      public SizeClassRule duplicate() {
        SizeClassRule rule = new SizeClassRule();
        int i;

        if (densities != null) {
          rule.densities = new Density[densities.length];
          for (i=0; i<densities.length; i++) {
            rule.densities[i] = densities[i];
          }
        }

        rule.anyTreatmentExcept = anyTreatmentExcept;

        if (treatments != null) {
          rule.treatments = new TreatmentType[treatments.length];
          for (i=0; i<treatments.length; i++) {
            rule.treatments[i] = treatments[i];
          }
        }

        if (processes != null) {
          rule.processes = new ProcessType[processes.length];
          for (i=0; i<processes.length; i++) {
            rule.processes[i] = processes[i];
          }
        }

        rule.wetter = wetter;
        rule.normal = normal;
        rule.drier  = drier;

        return rule;
      }

      public ProcessType getFireType(Moisture moisture) {
        if (moisture == Moisture.WETTER) { return wetter; }
        else if (moisture == Moisture.NORMAL) { return normal; }
        else if (moisture == Moisture.DRIER) { return drier; }
        return wetter;
      }
      public boolean isMatch(Density density, ProcessType process, TreatmentType treatment, Climate.Season season) {
        int i;
        boolean match=false;
        // First check for a density match.
        if (density != null) {
          if (densities == null) { return false; }
          for (i=0; i<densities.length; i++) {
            if (densities[i].equals(density)) {
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
            if (processes[i].equals(process)) {
              match = true;
              break;
            }
          }
          if (!match) { return false; }
        }

        match = false;
        if (treatments != null) {
          if (treatment == null) { return false; }
          for (i=0; i<treatments.length; i++) {
            if (treatments[i].equals(treatment)) {
              match = true;
              break;
            }
          }
          if ((anyTreatmentExcept && match) ||
              (!anyTreatmentExcept && !match)) { return false; }
        }

        if (this.season != Climate.Season.YEAR && this.season != season) { return false; }

        return true;
      }
    }

    public SizeClass[]         sizeClasses;
    public ArrayList           sizeClassRules;
    public Structure structure;

    public FireTypeDataEntry(int numSizeClasses, int numRules, Structure structure) {
      sizeClasses    = new SizeClass[numSizeClasses];
      sizeClassRules = new ArrayList(numRules);
      this.structure = structure;
    }
    public FireTypeDataEntry(int numSizeClasses, Structure structure) {
      this(numSizeClasses,5,structure);
    }

    public void moveRule(int oldPosition, int newPosition) {
      SizeClassRule rule = (SizeClassRule)sizeClassRules.get(oldPosition);
      sizeClassRules.remove(oldPosition);
      if (newPosition > sizeClassRules.size()) {
        sizeClassRules.add(0,rule);
        rule.setPosition(0);
      }
      else if (newPosition == -1) {
        rule.setPosition(sizeClassRules.size());
        sizeClassRules.add(rule);
      }
      else {
        rule.setPosition(newPosition);
        sizeClassRules.add(newPosition, rule);
      }
      markDataChanged();
    }
    public void updateSizeClasses(Vector v) {
      SizeClass[] newData = (SizeClass[])v.toArray(new SizeClass[v.size()]);
      updateSizeClasses(newData);
    }
    public void updateSizeClasses(SizeClass[] sizeClasses) {
      if (Arrays.equals(sizeClasses,this.sizeClasses)) {
        return;
      }

      this.sizeClasses = sizeClasses;
      updateEntrySizeClassHt();
      markDataChanged();
    }

    private void updateEntrySizeClassHt() {
      for (int i=0; i<sizeClasses.length; i++) {
        structureSizeClassHt.put(sizeClasses[i],structure);
      }
    }

    private void clearEntrySizeClassHt() {
      for (int i=0; i<sizeClasses.length; i++) {
        structureSizeClassHt.remove(sizeClasses[i]);
      }
    }

    public boolean isMemberSizeClass(SizeClass sizeClass) {
      if (sizeClasses == null) { return false; }
      for (int i=0; i<sizeClasses.length; i++) {
        if (sizeClasses[i].equals(sizeClass)) { return true; }
      }
      return false;
    }

    public FireTypeDataEntry duplicate() {
      FireTypeDataEntry entry =
          new FireTypeDataEntry(sizeClasses.length,sizeClassRules.size(),structure);

      int i;
      for (i=0; i<entry.sizeClasses.length; i++) {
        entry.sizeClasses[i] = sizeClasses[i];
      }

      SizeClassRule rule;
      for (i=0; i<sizeClassRules.size(); i++) {
        rule = (SizeClassRule)sizeClassRules.get(i);
        rule.setPosition(entry.sizeClassRules.size());
        entry.sizeClassRules.add(rule.duplicate());
      }
      entry.updateEntrySizeClassHt();
      return entry;
    }

    public SizeClassRule makeNewRule() {
      SizeClassRule rule = new SizeClassRule();
      rule.setPosition(sizeClassRules.size());
      sizeClassRules.add(rule);
      return rule;
    }

    public void addSizeClassRule(Density[] densities,
                                 boolean anyTreatmentExcept,
                                 TreatmentType[] treatments,
                                 ProcessType[] processes) {
      SizeClassRule rule = makeNewRule();

      rule.densities          = densities;
      rule.anyTreatmentExcept = anyTreatmentExcept;
      rule.treatments         = treatments;
      rule.processes          = processes;
      rule.position           = sizeClassRules.size();
      sizeClassRules.add(rule);
      markDataChanged();
    }

    public void addSizeClassRule(SizeClassRule rule) {
      markDataChanged();
      rule.setPosition(sizeClassRules.size());
      sizeClassRules.add(rule);
    }

    public void deleteRule(SizeClassRule rule) {
      boolean       updatePosition=false;
      SizeClassRule tmpRule;
      int           deleteIndex=-1;

      for (int i=0; i<sizeClassRules.size(); i++) {
        tmpRule = (SizeClassRule)sizeClassRules.get(i);
        if (updatePosition) {
          tmpRule.setPosition(tmpRule.getPosition()-1);
        }
        if (tmpRule == rule) {
          deleteIndex = i;
          updatePosition = true;
        }
      }
      if (deleteIndex != -1) { sizeClassRules.remove(deleteIndex); }
    }

    public SizeClassRule findRule(Density density, ProcessType process, TreatmentType treatment, Climate.Season season) {
      SizeClassRule rule;
      for (int i=0; i<sizeClassRules.size(); i++) {
        rule = (SizeClassRule)sizeClassRules.get(i);
        if (rule.isMatch(density,process,treatment,season)) { return rule; }
      }
      return null;
    }
    public void setFromLegacyData(int resistance) {
      SizeClassRule rule;
      for (int i=0; i<sizeClassRules.size(); i++) {
        rule = (SizeClassRule)sizeClassRules.get(i);
        rule.setFromLegacyData(resistance);
      }
    }
  }

  public static void deleteEntry(Structure structure, int resistance,
                                 FireTypeDataEntry entry) {
    ArrayList dataList =
        getData(structure,resistance);
    for (int i=0; i<dataList.size(); i++) {
      if ((FireTypeDataEntry)dataList.get(i) == entry) {
        entry.clearEntrySizeClassHt();
        dataList.remove(i);
        return;
      }
    }
  }

  public static void copyTo(Structure copyStructure, int copyResistance,
                            Structure toStructure, int toResistance) {
    ArrayList copyData = getData(copyStructure,copyResistance);

    if (getData(toStructure,toResistance) == null) {
      data[toStructure.ordinal()][toResistance] = new ArrayList(copyData.size());
    }
    ArrayList toData = getData(toStructure,toResistance);
    toData.clear();
    toData.ensureCapacity(copyData.size());

    for (int i=0; i<copyData.size(); i++) {
      toData.add(((FireTypeDataEntry)copyData.get(i)).duplicate());
    }
    markDataChanged();
  }
  public static void copyToAll(Structure copyStructure, int copyResistance) {
    for (int s=0; s<data.length; s++) {
      Structure structure = Structure.lookupOrdinal(s);
      for (int resistance=0; resistance<data[structure.ordinal()].length; resistance++) {
        if ((structure == copyStructure) && (resistance == copyResistance)) {
          continue;
        }
        copyTo(copyStructure,copyResistance,structure,resistance);
      }
    }
    markDataChanged();
  }

  public static ArrayList getData(Structure structure, int resistance) {
    return data[structure.ordinal()][resistance];
  }

  private static ArrayList[][] data = new ArrayList[NUM_STRUCTURE][NUM_RESISTANCE];

  public static void convertToFireTypeLogic() {
//    Vector allSpecies = Species.getAllZoneSpecies();
//    ArrayList lowResistSpecies = new ArrayList();
//    ArrayList modResistSpecies = new ArrayList();
//    ArrayList highResistSpecies = new ArrayList();
//
//    for (Object elem : allSpecies) {
//      Species species = (Species)elem;
//      if (species.getFireResistance() == FireResistance.LOW) {
//        lowResistSpecies.add(species);
//      }
//      if (species.getFireResistance() == FireResistance.MODERATE) {
//        modResistSpecies.add(species);
//      }
//      if (species.getFireResistance() == FireResistance.HIGH) {
//        highResistSpecies.add(species);
//      }
//    }

    FireEventLogic.getInstance().clearData(FireEventLogic.TYPE_STR);
    FireTypeDataEntry entry;
    for (int s=0; s<NUM_STRUCTURE; s++) {
      Structure structure = Structure.lookupOrdinal(s);
      for (int r=0; r<NUM_RESISTANCE; r++) {
        ArrayList speciesList=null;
        FireResistance fireResist;
        switch(r) {
          case LOW:      fireResist = FireResistance.LOW; break;
          case MODERATE: fireResist = FireResistance.MODERATE; break;
          case HIGH:     fireResist = FireResistance.HIGH; break;
          default: continue;
        }
        if (data[s][r] == null) { continue; }
        for (int i=0; i<data[s][r].size(); i++) {
          entry = (FireTypeDataEntry)data[s][r].get(i);

          ArrayList<SimpplleType> sizeClasses=null;
          if (entry.sizeClasses != null) {
            sizeClasses = new ArrayList<SimpplleType>();
            for (SizeClass sizeClass : entry.sizeClasses) {
//              if (sizeClass.getStructure() != structure) {
                sizeClasses.add(sizeClass);
//              }
            }
            if (sizeClasses.size() == 0) { sizeClasses = null; }
          }
          FireTypeDataEntry.SizeClassRule rule;
          for (int j=0; j<entry.sizeClassRules.size(); j++) {
            rule = (FireTypeDataEntry.SizeClassRule)entry.sizeClassRules.get(j);

            FireEventLogic.getInstance().addLegacyDataTypeOfFire(fireResist,
                                        sizeClasses,
                                        rule.densities,
                                        rule.processes,
                                        rule.anyTreatmentExcept,
                                        rule.treatments,
                                        rule.season,
                                        rule.wetter,
                                        rule.normal,
                                        rule.drier);
          }
        }
      }
    }
//    FireTypeLogic.combineCompatibleRules();
  }

  public static void clearData() {
    data = new ArrayList[NUM_STRUCTURE][NUM_RESISTANCE];
  }
  public static FireTypeDataEntry findEntry(Structure structure, int resistance,
                                            SizeClass sizeClass) {
    FireTypeDataEntry entry;
    ArrayList         entryList = getData(structure,resistance);
    if (entryList == null) { return null; }

    for (int i=0; i<entryList.size(); i++) {
      entry = (FireTypeDataEntry)entryList.get(i);
      if (entry.isMemberSizeClass(sizeClass)) { return entry; }
    }
    return null;
  }
  public static ProcessType getTypeOfFire(int resistance, Evu evu, Climate.Season season) {
    VegSimStateData state = evu.getState();
    if (state == null) { return null; }

    SizeClass     sizeClass = state.getVeg().getSizeClass();
    Density       density   = state.getVeg().getDensity();

    int           cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    VegSimStateData priorState = evu.getState(cTime-1);
    if (priorState == null) { return null; }

    ProcessType   process = evu.getState(cTime-1).getProcess();

    Treatment     treat = evu.getLastTreatment();
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    Moisture moisture = Simpplle.getClimate().getMoisture(season);

    FireTypeDataEntry.SizeClassRule rule;
    FireTypeDataEntry               entry;

    entry = findEntry(sizeClass.getStructure(),resistance,sizeClass);
    if (entry != null) {
      rule = entry.findRule(density,process,treatType,season);
      if (rule != null) { return rule.getFireType(moisture); }
    }
    return null;
  }
  public static FireTypeDataEntry addSizeClassGroup(Structure structure, int resistance, SizeClass[] sizeClasses) {
    if (data[structure.ordinal()][resistance] == null) {
      data[structure.ordinal()][resistance] = new ArrayList();
    }
    FireTypeDataEntry entry = new FireTypeDataEntry(sizeClasses.length,structure);
    entry.updateSizeClasses(sizeClasses);
    data[structure.ordinal()][resistance].add(entry);
    markDataChanged();
    return entry;
  }

  private static boolean isMemberSizeClass(SizeClass sizeClass, Structure structure,
                                           int resistance) {
    FireTypeDataEntry entry;

    if (data[structure.ordinal()][resistance] == null) { return false; }
    for (int i=0; i<data[structure.ordinal()][resistance].size(); i++) {
      entry = (FireTypeDataEntry)data[structure.ordinal()][resistance].get(i);
      if (entry.isMemberSizeClass(sizeClass)) { return true; }
    }
    return false;
  }

  public static Vector getPossibleSizeClasses(Structure structure, int resistance) {
    Vector    sizeClasses = HabitatTypeGroup.getValidSizeClass();
    Vector    v = new Vector();
    SizeClass sizeClass;
    for (int i=0; i<sizeClasses.size(); i++) {
      sizeClass = (SizeClass)sizeClasses.elementAt(i);
      if (isMemberSizeClass(sizeClass,structure,resistance) == false) {
        v.addElement(sizeClass);
      }
    }
    return v;
  }

  public static void setFromLegacyData() {
    FireTypeDataEntry entry;

    for (int s=0; s<data.length; s++) {
      for (int r=0; r<data[s].length; r++) {
        for (int i=0; i<data[s][r].size(); i++) {
          entry = (FireTypeDataEntry) data[s][r].get(i);
          entry.setFromLegacyData(r);
        }
      }
    }
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
  public static void readData(BufferedReader fin) {
    String              line, str;
    StringTokenizerPlus strTok, strListTok;
    int                 resistance=0, numEntries=0, numRules=0;
    ProcessType         fireType;
    Structure structure;

    try {
      line = fin.readLine();

      if (line.startsWith(SystemKnowledge.KNOWLEDGE_SOURCE_KEYWORD)) {
        SystemKnowledge.readKnowledgeSource(fin,SystemKnowledge.FIRE_TYPE_LOGIC);
        line = fin.readLine();
      }
      else {
        SystemKnowledge.setKnowledgeSource(SystemKnowledge.FIRE_TYPE_LOGIC,"");
      }

      while (line != null) {

        strTok = new StringTokenizerPlus(line, ",");
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
        data[structure.ordinal()][resistance] = new ArrayList(numEntries);

        int i, j, k, count;
        FireTypeDataEntry entry;
        FireTypeDataEntry.SizeClassRule entryRule;

        for (i = 0; i < numEntries; i++) {
          line = fin.readLine();
          strTok = new StringTokenizerPlus(line, ",");

          numRules = strTok.getIntToken();

          strListTok = new StringTokenizerPlus(strTok.getToken(), ":");
          count = strListTok.countTokens();
          entry = new FireTypeDataEntry(count, numRules,structure);

          data[structure.ordinal()][resistance].add(entry);
          for (j = 0; j < count; j++) {
            entry.sizeClasses[j] = SizeClass.get(strListTok.getToken(),true);
          }
          entry.updateEntrySizeClassHt();

          for (j = 0; j < numRules; j++) {
            entryRule = entry.makeNewRule();

            line = fin.readLine();
            strTok = new StringTokenizerPlus(line, ",");

            // Read the density list
            strListTok = new StringTokenizerPlus(strTok.getToken(), ":");
            count = strListTok.countTokens();
            entryRule.densities = new Density[count];
            for (k = 0; k < count; k++) {
              entryRule.densities[k] = Density.getOrCreate(strListTok.getToken());
            }

            // Read the boolean for any treatment except.
            entryRule.anyTreatmentExcept = Boolean.valueOf(strTok.getToken()).booleanValue();

            // Read the Treatment List (if any)
            str = strTok.getToken();
            if (str != null) {
              strListTok = new StringTokenizerPlus(str, ":");
              count = strListTok.countTokens();
              entryRule.treatments = new TreatmentType[count];
              for (k = 0; k < count; k++) {
                entryRule.treatments[k] = TreatmentType.get(strListTok.getToken());
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
                if (entryRule.processes[k] == null) {
                  entryRule.processes[k] = ProcessType.UNKNOWN;
                }
              }
            }

            // Read the Type of Fire Values
            // Wetter
            str = strTok.getToken();
            if (str != null) {
              fireType = ProcessType.get(str);
              if (fireType != null) {
                entryRule.wetter = fireType;
              }
            }

            // Normal
            str = strTok.getToken();
            if (str != null) {
              fireType = ProcessType.get(str);
              if (fireType != null) {
                entryRule.normal = fireType;
              }
            }

            // Drier
            str = strTok.getToken();
            if (str != null) {
              fireType = ProcessType.get(str);
              if (fireType != null) {
                entryRule.drier = fireType;
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
//    File             outfile = Utility.makeSuffixedPathname(file,"","firetypedata");
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
//    int i,j,k,l;
//    FireTypeDataEntry entry;
//    FireTypeDataEntry.SizeClassRule entryRule;
//
//    SystemKnowledge.writeKnowledgeSource(fout,SystemKnowledge.FIRE_TYPE_LOGIC);
//
//    for (i=0; i<data.length; i++) {
//      for (j=0; j<data[i].length; j++) {
//        if (data[i][j] == null || data[i][j].size() == 0) {
//          continue;
//        }
//
//        SizeClass.Structure structure = SizeClass.Structure.lookupOrdinal(i);
//        switch (structure) {
//          case NON_FOREST:     fout.print("NF,"); break;
//          case SINGLE_STORY:   fout.print("SS,"); break;
//          case MULTIPLE_STORY: fout.print("MS,"); break;
//        }
//        switch (j) {
//          case LOW:      fout.print("LOW,");      break;
//          case MODERATE: fout.print("MODERATE,"); break;
//          case HIGH:     fout.print("HIGH,");     break;
//        }
//        fout.println(data[i][j].size());
//
//        for (k=0; k<data[i][j].size(); k++) {
//          entry = (FireTypeDataEntry)data[i][j].get(k);
//
//          fout.print(entry.sizeClassRules.size());
//          fout.print(",");
//          Utility.printArray(fout,entry.sizeClasses,":");
//          fout.println();
//
//          for (l=0; l<entry.sizeClassRules.size(); l++) {
//            entryRule = (FireTypeDataEntry.SizeClassRule)entry.sizeClassRules.get(l);
//            Utility.printArray(fout,entryRule.densities,":");
//            fout.print(",");
//            fout.print(entryRule.anyTreatmentExcept);
//            fout.print(",");
//            Utility.printArray(fout,entryRule.treatments,":");
//            fout.print(",");
//            Utility.printArray(fout,entryRule.processes,":");
//            fout.print(",");
//            fout.print(entryRule.wetter);
//            fout.print(",");
//            fout.print(entryRule.normal);
//            fout.print(",");
//            fout.print(entryRule.drier);
//            fout.print(",");
//            fout.print(entryRule.season.toString());
//            fout.println();
//          }
//        }
//      }
//    }
//    setDataChanged(false);
//  }

}




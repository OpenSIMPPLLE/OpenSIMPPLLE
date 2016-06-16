package simpplle.comcode;

import java.io.*;
import java.util.*;
import java.text.NumberFormat;
import org.hibernate.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Multiple Run Summary.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public final class MultipleRunSummary implements Externalizable {
  static final long serialVersionUID = -2220229668610021867L;;
  private final int version = 1;

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    summary = readExternalSimpplleTypeHashMaps(in);
    stats = readExternalSimpplleTypeHashMaps(in);

    specialSummary = new HashMap[2][];
    specialSummary[SPECIAL_AREA] = readExternalSpecialSimpplleTypeHashMaps(in);
    specialSummary[OWNERSHIP] = readExternalSpecialSimpplleTypeHashMaps(in);

    specialStats = new HashMap[2][];
    specialStats[SPECIAL_AREA] = readExternalSpecialSimpplleTypeHashMaps(in);
    specialStats[OWNERSHIP] = readExternalSpecialSimpplleTypeHashMaps(in);

    fscSummary   = readExternalFscSummaryHashMaps(in);
    fscSummarySA = readExternalFscSummaryHashMaps(in);

    emissionsSummary = readExternalEmissionData(in);

    all        = readExternalAllHashMap(in);
    specialAll = readExternalSpecialAllHashMap(in);
    allAgeHm   = readExternalAllAgeHm(in);

    freqCountSpecies   = readExternalFrequencyCount(in,SimpplleType.SPECIES);
    freqCountSizeClass = readExternalFrequencyCount(in,SimpplleType.SIZE_CLASS);
    freqCountDensity   = readExternalFrequencyCount(in,SimpplleType.DENSITY);
    freqCountProcess   = readExternalFrequencyCount(in,SimpplleType.PROCESS);

    readExternalFrequencyTotCount(in);
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    writeExternalSimpplleTypeHashMaps(out, summary);
    writeExternalSimpplleTypeHashMaps(out, stats);

    writeExternalSpecialSimpplleTypeHashMaps(out, specialSummary[SPECIAL_AREA]);
    writeExternalSpecialSimpplleTypeHashMaps(out, specialSummary[OWNERSHIP]);
    writeExternalSpecialSimpplleTypeHashMaps(out, specialStats[SPECIAL_AREA]);
    writeExternalSpecialSimpplleTypeHashMaps(out, specialStats[OWNERSHIP]);

    writeExternalFscSummaryHashMaps(out,fscSummary);
    writeExternalFscSummaryHashMaps(out,fscSummarySA);

    writeExternalEmissionData(out);

    writeExternalAllHashMap(out);
    writeExternalSpecialAllHashMap(out);
    writeExternalallAgeHm(out);

    writeExternalFrequencyCount(out,freqCountSpecies);
    writeExternalFrequencyCount(out,freqCountSizeClass);
    writeExternalFrequencyCount(out,freqCountDensity);
    writeExternalFrequencyCount(out,freqCountProcess);

    writeExternalFrequencyTotCount(out);
  }

  private void writeExternalSpecialSimpplleTypeHashMaps(ObjectOutput out, HashMap[] hmData)
    throws IOException
  {
    SimpplleType.Types[] types =
      new SimpplleType.Types[] {SimpplleType.PROCESS, SimpplleType.SPECIES,
                                SimpplleType.SIZE_CLASS,SimpplleType.DENSITY,
                                SimpplleType.TREATMENT};

    out.writeInt(types.length);
    for (SimpplleType.Types kind : types) {
      out.writeObject(SimpplleType.getTypeName(kind));
      HashMap hm = hmData[kind.ordinal()];
      int size = (hm != null) ? hm.size() : 0;
      out.writeInt(size);
      if (size == 0) { continue; }

      for (Object elem : hm.keySet()) {
        String key = (String)elem;
        out.writeObject(key);

        HashMap dataHm = (HashMap)hm.get(key);
        size = (dataHm != null) ? dataHm.size() : 0;
        out.writeInt(size);
        if (size == 0) { continue; }

        for (Object obj : dataHm.keySet()) {
          SimpplleType sKey = (SimpplleType) obj;
          sKey.writeExternalSimple(out);

          int[][] data = (int[][]) dataHm.get(sKey);
          out.writeInt(data.length);
          out.writeInt(data[0].length);
          for (int ts = 0; ts < data.length; ts++) {
            for (int run = 0; run < data[ts].length; run++) {
              out.writeInt(data[ts][run]);
            }
          }
        }
      }
    }
  }
  private HashMap[] readExternalSpecialSimpplleTypeHashMaps(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int numTypes = in.readInt();
    HashMap[] hmData = new HashMap[numTypes];
    for (int t=0; t<numTypes; t++) {
      SimpplleType.Types kind = SimpplleType.getTypeFromString((String)in.readObject());
      int size = in.readInt();
      hmData[kind.ordinal()] = new HashMap(size);
      for (int k=0; k<size; k++) {
        String key = (String)in.readObject();

        int hmSize = in.readInt();
        HashMap hm = new HashMap(hmSize);
        for (int j=0; j<hmSize; j++) {
          SimpplleType skey = SimpplleType.readExternalSimple(in, kind);

          int nSteps = in.readInt();
          int nRuns = in.readInt();
          int[][] data = new int[nSteps][nRuns];

          for (int ts = 0; ts < data.length; ts++) {
            for (int run = 0; run < data[ts].length; run++) {
              data[ts][run] = in.readInt();
            }
          }
          hm.put(skey, data);
        }
        hmData[kind.ordinal()].put(key, hm);
      }
    }

    return hmData;
  }


  private void writeExternalSimpplleTypeHashMaps(ObjectOutput out, HashMap[] hmData)
    throws IOException
  {
    SimpplleType.Types[] types =
      new SimpplleType.Types[] {SimpplleType.PROCESS, SimpplleType.SPECIES,
                          SimpplleType.SIZE_CLASS,SimpplleType.DENSITY,
                          SimpplleType.TREATMENT};

    out.writeInt(types.length);
    for (SimpplleType.Types kind : types) {
      out.writeObject(SimpplleType.getTypeName(kind));
      HashMap hm = hmData[kind.ordinal()];
      int size = (hm != null) ? hm.size() : 0;
      out.writeInt(size);
      if (size == 0) { continue; }

      for (Object elem : hm.keySet()) {
        SimpplleType key = (SimpplleType)elem;
        key.writeExternalSimple(out);

        int[][] data = (int[][])hm.get(key);
        out.writeInt(data.length);
        out.writeInt(data[0].length);
        for (int ts=0; ts<data.length; ts++) {
          for (int run=0; run<data[ts].length; run++) {
            out.writeInt(data[ts][run]);
          }
        }
      }
    }
  }
  private HashMap[] readExternalSimpplleTypeHashMaps(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int numTypes = in.readInt();
    HashMap[] hmData = new HashMap[numTypes];
    for (int t=0; t<numTypes; t++) {
      SimpplleType.Types kind = SimpplleType.getTypeFromString((String)in.readObject());
      int size = in.readInt();
      hmData[kind.ordinal()] = new HashMap(size);
      for (int k=0; k<size; k++) {
        SimpplleType key = SimpplleType.readExternalSimple(in,kind);

        int nSteps = in.readInt();
        int nRuns   = in.readInt();
        int[][] data = new int[nSteps][nRuns];

        for (int ts=0; ts<data.length; ts++) {
          for (int run=0; run<data[ts].length; run++) {
            data[ts][run] = in.readInt();
          }
        }

        hmData[kind.ordinal()].put(key, data);
      }
    }

    return hmData;
  }

  private void writeExternalFscSummaryHashMaps(ObjectOutput out, HashMap[] hmData)
    throws IOException
  {
    int size = (hmData != null) ? hmData.length : 0;
    out.writeInt(size);
    if (size == 0) { return; }

    for (int run=0; run<hmData.length; run++) {
      size = (hmData[run] != null) ? hmData[run].size() : 0;
      out.writeInt(size);
      if (size == 0) { continue; }

      for (Object elem : hmData[run].keySet()) {
        String key = (String)elem;
        out.writeObject(key);

        double[] data = (double[])hmData[run].get(key);
        out.writeInt(data.length);
        for (int ts=0; ts<data.length; ts++) {
          out.writeDouble(data[ts]);
        }
      }
    }
  }
  private HashMap[] readExternalFscSummaryHashMaps(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int nRuns = in.readInt();
    HashMap[] hmData = new HashMap[nRuns];


    for (int run = 0; run < nRuns; run++) {
      int size = in.readInt();
      hmData[run] = new HashMap(size);

      for (int i = 0; i < size; i++) {
        String key = (String) in.readObject();

        double[] data = new double[in.readInt()];
        for (int j=0; j<data.length; j++) {
          data[j] = in.readDouble();
        }
        hmData[run].put(key,data);
      }
   }

   return hmData;

  }


  private void writeExternalEmissionData(ObjectOutput out) throws IOException {
    //    emissionsSummary = new double[emissionTypes.length][numSteps][numRuns];
    out.writeInt(emissionsSummary.length);
    out.writeInt(emissionsSummary[0].length);
    out.writeInt(emissionsSummary[0][0].length);
    for (int et=0; et<emissionsSummary.length; et++) {
      for (int ts=0; ts<emissionsSummary[et].length; ts++) {
        for (int run=0; run<emissionsSummary[et][ts].length; run++) {
          out.writeDouble(emissionsSummary[et][ts][run]);
        }
      }
    }
  }
  private double[][][] readExternalEmissionData(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    double[][][] data = new double[in.readInt()][in.readInt()][in.readInt()];
    for (int et=0; et<data.length; et++) {
      for (int ts = 0; ts < data[et].length; ts++) {
        for (int run = 0; run < data[et][ts].length; run++) {
          data[et][ts][run] = in.readDouble();
        }
      }
    }
    return data;
  }

  private void writeExternalAllHashMap(ObjectOutput out)
    throws IOException
  {
    SimpplleType.Types[] types =
      new SimpplleType.Types[] {SimpplleType.PROCESS, SimpplleType.SPECIES,
                          SimpplleType.SIZE_CLASS,SimpplleType.DENSITY,
                          SimpplleType.TREATMENT};

    out.writeInt(types.length);
    for (SimpplleType.Types kind : types) {
      out.writeObject(SimpplleType.getTypeName(kind));
      HashMap hm = all[kind.ordinal()];
      int size = (hm != null) ? hm.size() : 0;
      out.writeInt(size);
      if (size == 0) { continue; }

      for (Object elem : hm.keySet()) {
        SimpplleType key = (SimpplleType)elem;
        key.writeExternalSimple(out);

        SimpplleType value = (SimpplleType)hm.get(key);
        value.writeExternalSimple(out);
      }
    }
  }
  private HashMap[] readExternalAllHashMap(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    HashMap[] hm = new HashMap[in.readInt()];
    for (int i=0; i<hm.length; i++) {
      String typeName = (String)in.readObject();
      SimpplleType.Types kind = SimpplleType.getTypeFromString(typeName);
      int size = in.readInt();
      hm[kind.ordinal()] = new HashMap(size);
      for (int j=0; j<size; j++) {
        hm[kind.ordinal()].put(SimpplleType.readExternalSimple(in,kind),
                     SimpplleType.readExternalSimple(in,kind));
      }
    }


    return hm;
  }


  private void writeExternalSpecialAllHashMap(ObjectOutput out)
    throws IOException
  {
    int[] types =
      new int[] {OWNERSHIP, SPECIAL_AREA};

    out.writeInt(types.length);
    for (int kind : types) {
      switch(kind) {
        case OWNERSHIP: out.writeObject("OWNERSHIP"); break;
        case SPECIAL_AREA: out.writeObject("SPECIAL_AREA"); break;
      }

      HashMap hm = specialAll[kind];
      int size = (hm != null) ? hm.size() : 0;
      out.writeInt(size);
      if (size == 0) { continue; }

      for (Object elem : hm.keySet()) {
        String key = (String)elem;
        out.writeObject(key);

        String value = (String)hm.get(key);
        out.writeObject(value);
      }
    }

  }
  private HashMap[] readExternalSpecialAllHashMap(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    HashMap[] hm = new HashMap[in.readInt()];
    for (int i=0; i<hm.length; i++) {
      String typeName = (String)in.readObject();
      int    kind;
      if (typeName.equals("OWNERSHIP")) {
        kind = OWNERSHIP;
      }
      else {  // "SPECIAL_AREA"
        kind = SPECIAL_AREA;
      }

      int size = in.readInt();
      hm[kind] = new HashMap(size);
      for (int j=0; j<size; j++) {
        hm[kind].put((String)in.readObject(),
                     (String)in.readObject());
      }
    }


    return hm;
  }

  private void writeExternalallAgeHm(ObjectOutput out) throws IOException {
    int size = (allAgeHm != null) ? allAgeHm.size() : 0;
    out.writeInt(size);
    if (size == 0) { return; }

    for (String key : allAgeHm.keySet()) {
      out.writeObject(key);
      out.writeObject(allAgeHm.get(key));
    }
  }
  private HashMap<String,Integer> readExternalAllAgeHm(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    int size = in.readInt();
    HashMap<String,Integer> hm = new HashMap<String,Integer>(size);

    for (int i=0; i<size; i++) {
      hm.put((String)in.readObject(), (Integer)in.readObject());
    }

    return hm;
  }

  // HashMap<SimpplleType,MyInteger>[time step][SimpplleType kind]
//  private HashMap<Evu,HashMap[][]> frequencyCount;
//  private HashMap<Evu,int[]>       frequencyTotCount;

  private void writeExternalFrequencyTotCount(ObjectOutput out)
    throws IOException
  {
    int size = (frequencyTotCount != null) ? frequencyTotCount.size() : 0;
    out.writeInt(size);
    if (size == 0) { return; }

    for (Evu evu : frequencyTotCount.keySet()) {
      out.writeInt(evu.getId());

      int[] counts = frequencyTotCount.get(evu);
      out.writeInt(counts.length);

      for (int ts=0; ts<counts.length; ts++) {
        out.writeInt(counts[ts]);
      }
    }
  }
  private void readExternalFrequencyTotCount(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    Area area = Simpplle.getCurrentArea();
    int size = in.readInt();

    frequencyTotCount = new HashMap<Evu,int[]>(size);
    for (int i=0; i<size; i++) {
      Evu evu = area.getEvu(in.readInt());

      int[] counts = new int[in.readInt()];
      for (int j=0; j<counts.length; j++) {
        counts[j] = in.readInt();
      }
      frequencyTotCount.put(evu,counts);
    }
  }

  //  private HashMap<Evu,HashMap<SimpplleType,int[]>> freqCountSpecies;

  private void writeExternalFrequencyCount(ObjectOutput out,
                                           HashMap<Evu,HashMap<SimpplleType,int[]>> dataHm)
    throws IOException
  {
    int size = (dataHm != null) ? dataHm.size() : 0;
    out.writeInt(size);
    if (size == 0) { return; }
    for (Evu evu : dataHm.keySet()) {
      out.writeInt(evu.getId());

      HashMap<SimpplleType,int[]> hm = dataHm.get(evu);
      size = (hm != null) ? hm.size() : 0;
      out.writeInt(size);
      if (size == 0) { continue; }

      for (SimpplleType key : hm.keySet()) {
        key.writeExternalSimple(out);

        int[] counts = hm.get(key);
        size = (counts != null) ? counts.length : 0;
        out.writeInt(size);
        if (size == 0) { continue; }
        for (int i=0; i<size; i++) {
          out.writeInt(counts[i]);
        }
      }
    }
  }
  private HashMap<Evu,HashMap<SimpplleType,int[]>> readExternalFrequencyCount(ObjectInput in, SimpplleType.Types kind)
      throws IOException, ClassNotFoundException
  {
    Area area = Simpplle.getCurrentArea();
    int size = in.readInt();
    HashMap<Evu,HashMap<SimpplleType,int[]>> hmData =
        new HashMap<Evu,HashMap<SimpplleType,int[]>>(size);

    for (int i=0; i<size; i++) {
      Evu evu = area.getEvu(in.readInt());

      int hmSize = in.readInt();
      HashMap<SimpplleType,int[]> hm = new HashMap<SimpplleType,int[]>(hmSize);

      for (int j=0; j<hmSize; j++) {
        SimpplleType key = SimpplleType.readExternalSimple(in,kind);

        int[] counts = new int[in.readInt()];
        for (int ts=0; ts<counts.length; ts++) {
          counts[ts] = in.readInt();
        }
        hm.put(key,counts);
      }
      hmData.put(evu,hm);
    }
    return hmData;
  }


  // Process, Species, Size Class, Density and Treatment Data;
  private HashMap[] summary;
  private HashMap[] stats;

  // HashMap<SimpplleType,MyInteger>[time step][SimpplleType kind]
  private HashMap<Evu,HashMap<SimpplleType,int[]>> freqCountSpecies;
  private HashMap<Evu,HashMap<SimpplleType,int[]>> freqCountSizeClass;
  private HashMap<Evu,HashMap<SimpplleType,int[]>> freqCountDensity;
  private HashMap<Evu,HashMap<SimpplleType,int[]>> freqCountProcess;

  private HashMap<Evu,int[]>       frequencyTotCount;

  // Process, Species, Size Class, Density and Treatment Data;
  // by special Area and Ownership.
  private HashMap[][] specialSummary;
  private HashMap[][] specialStats;

  // index is run, key is fmzName, value is double[]
  // Fire Suppression Cost Data.
  private HashMap[] fscSummary;
  private HashMap[] fscSummarySA;

  // Emissions Data.
  private double[][][] emissionsSummary;

  private HashMap[] all;        // all processes, all species, etc.
  private HashMap[] specialAll; // All Special Area and Ownership.
  private HashMap<String,Integer> allAgeHm;

  // Frequency data
  HashMap<Integer,HashMap<SimpplleType,Integer>> speciesFreq;
  HashMap<Integer,HashMap<SimpplleType,Integer>> sizeClassFreq;
  HashMap<Integer,HashMap<SimpplleType,Integer>> densityFreq;
  HashMap<Integer,HashMap<SimpplleType,Integer>> processFreq;

  private static final int MEAN = 0;
  private static final int MIN  = 1;
  private static final int MAX  = 2;

  public static final SimpplleType.Types PROCESS    = SimpplleType.PROCESS;
  public static final SimpplleType.Types SPECIES    = SimpplleType.SPECIES;
  public static final SimpplleType.Types SIZE_CLASS = SimpplleType.SIZE_CLASS;
  public static final SimpplleType.Types DENSITY    = SimpplleType.DENSITY;
  public static final SimpplleType.Types TREATMENT    = SimpplleType.TREATMENT;

  public static final int SPECIAL_AREA = 0;
  public static final int OWNERSHIP    = 1;

  // These must start at 0.
  private static final int FIRE_PM10       = 0;
  private static final int FIRE_PM2_5      = 1;
  private static final int TREATMENT_PM10  = 2;
  private static final int TREATMENT_PM2_5 = 3;

  private static final String emissionTypes[] = {"FIRE-PM10",
                                                  "FIRE-PM2.5",
                                                  "TREATMENT-PM10",
                                                  "TREATMENT-PM2.5"};

  public MultipleRunSummary() {
  }
  public MultipleRunSummary(Simulation simulation) {
    RegionalZone zone       = Simpplle.getCurrentZone();
    int          numSteps, numRuns;
    int          numFmz;
    boolean      specialArea, ownership;

    numSteps         = simulation.getNumTimeSteps();
    numRuns          = simulation.getNumSimulations();
    numFmz           = zone.getAllFmz().length;
    specialArea      = simulation.trackSpecialArea();
    ownership        = simulation.trackOwnership();

    summary = new HashMap[5];
    stats   = new HashMap[5];
    all     = new HashMap[5];

    specialSummary = new HashMap[2][5];
    specialStats   = new HashMap[2][5];
    specialAll     = new HashMap[2];

    if (specialArea) {
      initializeSpecialData(SPECIAL_AREA);
    }
    if (ownership) {
      initializeSpecialData(OWNERSHIP);
    }

    initializeData(SPECIES);
    initializeData(SIZE_CLASS);
    initializeData(DENSITY);
    initializeData(PROCESS);
    initializeData(TREATMENT);

    int numEvu = Simpplle.getCurrentArea().getMaxEvuId();

    freqCountSpecies   = new HashMap<Evu,HashMap<SimpplleType,int[]>>(numEvu);
    freqCountSizeClass = new HashMap<Evu,HashMap<SimpplleType,int[]>>(numEvu);
    freqCountDensity   = new HashMap<Evu,HashMap<SimpplleType,int[]>>(numEvu);
    freqCountProcess   = new HashMap<Evu,HashMap<SimpplleType,int[]>>(numEvu);

    frequencyTotCount = new HashMap<Evu,int[]>(numEvu);

    fscSummary = new HashMap[numRuns];
    fscSummarySA = new HashMap[numRuns];
    emissionsSummary = new double[emissionTypes.length][numSteps][numRuns];
  }

  private void initializeData(SimpplleType.Types kind) {
    summary[kind.ordinal()] = new HashMap();
    stats[kind.ordinal()]   = new HashMap();
    all[kind.ordinal()]     = new HashMap();
    allAgeHm      = new HashMap<String,Integer>();
  }

  private void initializeSpecialData(int kind) {
    specialSummary[kind][PROCESS.ordinal()]    = new HashMap();
    specialSummary[kind][SPECIES.ordinal()]    = new HashMap();
    specialSummary[kind][SIZE_CLASS.ordinal()] = new HashMap();
    specialSummary[kind][DENSITY.ordinal()]    = new HashMap();
    specialSummary[kind][TREATMENT.ordinal()]  = new HashMap();

    specialStats[kind][PROCESS.ordinal()]    = new HashMap();
    specialStats[kind][SPECIES.ordinal()]    = new HashMap();
    specialStats[kind][SIZE_CLASS.ordinal()] = new HashMap();
    specialStats[kind][DENSITY.ordinal()]    = new HashMap();
    specialStats[kind][TREATMENT.ordinal()]  = new HashMap();

    specialAll[kind] = new HashMap();
  }

  public Vector getAllStateProcessNames(int kind) {
    return getAllAttributeNames(kind);
  }

  public SimpplleType[][] getAllAttributes() {
    SimpplleType[][] attribs = new SimpplleType[SimpplleType.MAX][];

    attribs[SimpplleType.SPECIES.ordinal()]    = getAttributes(SimpplleType.SPECIES.ordinal());
    attribs[SimpplleType.SIZE_CLASS.ordinal()] = getAttributes(SimpplleType.SIZE_CLASS.ordinal());
    attribs[SimpplleType.DENSITY.ordinal()]    = getAttributes(SimpplleType.DENSITY.ordinal());
    attribs[SimpplleType.PROCESS.ordinal()]    = getAttributes(SimpplleType.PROCESS.ordinal());

    return attribs;
  }

  public SimpplleType[] getAttributes(int kind) {
    ArrayList list = new ArrayList(all[kind].values());
    SimpplleType[] result = new SimpplleType[all[kind].size()];

    Collections.sort(list);
    list.toArray(result);
    return result;
  }

  public HashMap getAllSpecies()   { return all[SPECIES.ordinal()];   }
  public HashMap getAllSizeClass() { return all[SIZE_CLASS.ordinal()]; }
  public HashMap getAllDensity()   { return all[DENSITY.ordinal()];   }
  public HashMap getAllProcess()   { return all[PROCESS.ordinal()];   }

  public Collection<Integer> getAllAge() { return allAgeHm.values(); }

  private Vector getAllAttributeNames(int kind) {
    Iterator it = all[kind].values().iterator();
    Vector   names = new Vector();
    while (it.hasNext()) {
      names.addElement(((SimpplleType)it.next()).toString());
    }

    return names;
  }

  public void updateAllSpecialArea(String specialArea) {
    updateSpecialAll(SPECIAL_AREA, specialArea);
  }

  public void updateAllOwnership(String ownership) {
    updateSpecialAll(OWNERSHIP, ownership);
  }

  private void updateSpecialAll(int kind, String value) {
    if (specialAll[kind].containsKey(value) == false) {
      specialAll[kind].put(value,value);
    }
  }

  public void updateFireSuppressionCostSummary(float discount) {
    updateFireSuppressionCostSummary(discount,Simpplle.getCurrentSimulation());
  }
  public void updateFireSuppressionCostSummary(float discount, Simulation simulation) {
    AreaSummary areaSummary;
    int         cRun;

    areaSummary = simulation.getAreaSummary();
    cRun        = simulation.getCurrentRun();

    fscSummary[cRun] = areaSummary.getFireSuppressionCost(discount,simulation);

    if (simulation.trackSpecialArea()) {
      fscSummarySA[cRun] = areaSummary.getFireSuppressionCostSA(discount,simulation);
    }
  }

  private void updateStateSummary(HashMap ht, SimpplleType key, int acres,
                                  int cStep) {
    int[][]    data;
    int        i,j;
    int        cRun, nSteps, nRuns;
    Simulation simulation = Simpplle.getCurrentSimulation();

    cRun   = simulation.getCurrentRun();
    nRuns  = simulation.getNumSimulations();
    nSteps = simulation.getNumTimeSteps() + 1;

    data = (int[][]) ht.get(key);
    if (data == null) {
      data = new int[nSteps][nRuns];
      ht.put(key,data);
      for(i=0;i<nSteps;i++) {
        for(j=0;j<nRuns;j++) {
          data[i][j] = 0;
      }}
    }
    data[cStep][cRun] += acres;
  }


  private void incFrequencyCount(Evu evu, int cStep, SimpplleType.Types kind, SimpplleType sType) {
    HashMap<SimpplleType,int[]> hm = getFreqCountData(evu,kind);
    int[] counts = hm.get(sType);
    if (counts == null) {
      counts = new int[Simpplle.getCurrentSimulation().getNumTimeSteps()+1];
      for (int i=0; i<counts.length; i++) {
        counts[i] = 0;
      }
      hm.put(sType,counts);
    }
    counts[cStep]++;
  }
  private HashMap<SimpplleType,int[]> getFreqCountData(Evu evu, SimpplleType.Types kind) {
    switch (kind) {
      case SPECIES:    return freqCountSpecies.get(evu);
      case SIZE_CLASS: return freqCountSizeClass.get(evu);
      case DENSITY:    return freqCountDensity.get(evu);
      case PROCESS:    return freqCountProcess.get(evu);
      default: return null;
    }
  }

  public void updateAll(SimpplleType value, int kind) {
    all[kind].put(value,value);
  }

  //  private HashMap<Evu,HashMap<SimpplleType,int[]>> freqCountSpecies;

  public void initFrequencyCount(Evu evu) {
    int nSteps = Simpplle.getCurrentSimulation().getNumTimeSteps();

    int[] totCount = new int[nSteps+1];

    for (int ts=0; ts<nSteps; ts++) {
      totCount[ts] = 0;
    }
    frequencyTotCount.put(evu,totCount);

    freqCountSpecies.put(evu,new HashMap<SimpplleType,int[]>());
    freqCountSizeClass.put(evu,new HashMap<SimpplleType,int[]>());
    freqCountDensity.put(evu,new HashMap<SimpplleType,int[]>());
    freqCountProcess.put(evu,new HashMap<SimpplleType,int[]>());
  }
  public void updateSummaries(Evu evu) {
    Simulation     simulation = Simpplle.getCurrentSimulation();
    int            cStep, acres;
    ProcessType    processType;
    Species        species;
    SizeClass      sizeClass;
    Density        density;
    Treatment      treatment;
    VegetativeType state;

    cStep  = simulation.getCurrentTimeStep();
    acres  = evu.getAcres();

    int[] totCount = frequencyTotCount.get(evu);
    totCount[cStep]++;

    MyInteger count;
      // Process
    ArrayList<ProcessType> summaryProcesses = evu.getSummaryProcesses(cStep);
    for (int i=0; i<summaryProcesses.size(); i++) {
      processType = summaryProcesses.get(i);
      if (all[PROCESS.ordinal()].containsKey(processType) == false) {
        all[PROCESS.ordinal()].put(processType, processType);
      }
      incFrequencyCount(evu, cStep, PROCESS, processType);

      updateStateSummary(summary[PROCESS.ordinal()], processType, acres, cStep);
    }

      // Treatment.
      treatment = evu.getTreatment(cStep);
      if (treatment != null) {
        if (all[TREATMENT.ordinal()].containsKey(treatment.getType()) == false) {
          all[TREATMENT.ordinal()].put(treatment.getType(), treatment.getType());
        }
        updateStateSummary(summary[TREATMENT.ordinal()], treatment.getType(), acres, cStep);
      }

    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      if (evu.hasLifeform(allLives[i],cStep) == false) { continue; }

      state = evu.getState(cStep,allLives[i]).getVeg();
      if (state == null) { continue; }

      // Species.
      species = state.getSpecies();
      if (all[SPECIES.ordinal()].containsKey(species) == false) {
        all[SPECIES.ordinal()].put(species, species);
      }
      incFrequencyCount(evu, cStep, SPECIES, species);
      if (simulation.isLastSeason()) {
        updateStateSummary(summary[SPECIES.ordinal()], species, acres, cStep);
      }
      // Size Class
      sizeClass = state.getSizeClass();
      if (all[SIZE_CLASS.ordinal()].containsKey(sizeClass) == false) {
        all[SIZE_CLASS.ordinal()].put(sizeClass, sizeClass);
      }
      incFrequencyCount(evu, cStep, SIZE_CLASS, sizeClass);
      if (simulation.isLastSeason()) {
        updateStateSummary(summary[SIZE_CLASS.ordinal()], sizeClass, acres,
                           cStep);
      }

      // Density.
      density = state.getDensity();
      if (all[DENSITY.ordinal()].containsKey(density) == false) {
        all[DENSITY.ordinal()].put(density, density);
      }
      incFrequencyCount(evu, cStep, DENSITY, density);
      if (simulation.isLastSeason()) {
        updateStateSummary(summary[DENSITY.ordinal()], density, acres, cStep);
      }

      // Age
      int age = state.getAge();
      if (allAgeHm.containsKey(IntToString.get(age)) == false) {
        allAgeHm.put(IntToString.get(age), age);
      }
    }

  }
  public void computeFrequencies() {
//    Simulation simulation = Simpplle.getCurrentSimulation();
//    int totalCount = simulation.getNumSimulations() *
//                     (simulation.getNumTimeSteps() + 1);
//
//    for (int kind=0; kind<frequency.size(); kind++) {
//      for (MyInteger value : frequency.get(kind).values()) {
//        int count = value.intValue();
//        float freq = (float)count / (float)totalCount;
//        freq *= 100.0f;
//        value.setValue(Math.round(freq));
//      }
//    }

  }

  public void updateSpecialAreaSummaries(Evu evu) {
    updateSpecialSummaries(evu,SPECIAL_AREA);
  }

  public void updateOwnershipSummaries(Evu evu) {
    updateSpecialSummaries(evu,OWNERSHIP);
  }

  private void updateSpecialSummaries(Evu evu, int kind) {
    Simulation     simulation = Simpplle.getCurrentSimulation();
    int            numSteps;
    int            cStep, acres;
    VegetativeType state;
    Treatment      treatment;
    String         specialKey;
    HashMap      processHt, speciesHt, sizeClassHt, densityHt, treatmentHm;

    numSteps = simulation.getNumTimeSteps();

    if (kind == SPECIAL_AREA) {
      specialKey = evu.getSpecialArea();
    }
    else {
      specialKey = evu.getOwnership();
    }

    processHt   = (HashMap) specialSummary[kind][PROCESS.ordinal()].get(specialKey);
    speciesHt   = (HashMap) specialSummary[kind][SPECIES.ordinal()].get(specialKey);
    sizeClassHt = (HashMap) specialSummary[kind][SIZE_CLASS.ordinal()].get(specialKey);
    densityHt   = (HashMap) specialSummary[kind][DENSITY.ordinal()].get(specialKey);
    treatmentHm = (HashMap) specialSummary[kind][TREATMENT.ordinal()].get(specialKey);

    if (processHt == null) {
      processHt = new HashMap();
      specialSummary[kind][PROCESS.ordinal()].put(specialKey,processHt);
    }
    if (speciesHt == null) {
      speciesHt = new HashMap();
      specialSummary[kind][SPECIES.ordinal()].put(specialKey,speciesHt);
    }
    if (sizeClassHt == null) {
      sizeClassHt = new HashMap();
      specialSummary[kind][SIZE_CLASS.ordinal()].put(specialKey,sizeClassHt);
    }
    if (densityHt == null) {
      densityHt = new HashMap();
      specialSummary[kind][DENSITY.ordinal()].put(specialKey,densityHt);
    }
    if (treatmentHm == null) {
      treatmentHm = new HashMap();
      specialSummary[kind][TREATMENT.ordinal()].put(specialKey,treatmentHm);
    }

    acres = evu.getAcres();

    for(cStep=0;cStep<=numSteps;cStep++) {
      // Process
      ArrayList<ProcessType> summaryProcesses = evu.getSummaryProcesses(cStep);
      for (int i=0; i<summaryProcesses.size(); i++) {
        updateStateSummary(processHt,summaryProcesses.get(i),acres,cStep);
      }

      if (simulation.isLastSeason()) {
        Lifeform[] allLives = Lifeform.getAllValues();
        for (int i = 0; i < allLives.length; i++) {
          if (evu.hasLifeform(allLives[i],cStep) == false) { continue; }

          state = evu.getState(cStep,allLives[i]).getVeg();
          if (state == null) { continue; }

          // Species.
          updateStateSummary(speciesHt, state.getSpecies(), acres, cStep);

          // Size Class
          updateStateSummary(sizeClassHt, state.getSizeClass(), acres, cStep);

          // Density.
          updateStateSummary(densityHt, state.getDensity(), acres, cStep);
        }
      }

      // Treatment
      treatment = evu.getTreatment(cStep);
      if (treatment != null) {
        updateStateSummary(treatmentHm, treatment.getType(), acres, cStep);
      }
    }
  }

  public void finishEmissionsSummary() {
    Simulation  simulation = Simpplle.getCurrentSimulation();
    int         numSteps, cRun;
    int         zoneId = Simpplle.getCurrentZone().getId();
    AreaSummary areaSummary = Simpplle.getAreaSummary();

    cRun     = simulation.getCurrentRun();
    numSteps = simulation.getNumTimeSteps();

    for(int tStep=1;tStep<=numSteps;tStep++) {
      emissionsSummary[FIRE_PM10][tStep-1][cRun] =
         areaSummary.getFireEmissions(tStep);
      emissionsSummary[TREATMENT_PM10][tStep-1][cRun] =
         areaSummary.getTreatmentEmissions(tStep);

      emissionsSummary[FIRE_PM2_5][tStep-1][cRun] =
        Emissions.getPM2_5(emissionsSummary[FIRE_PM10][tStep-1][cRun]);
      emissionsSummary[TREATMENT_PM2_5][tStep-1][cRun] =
        Emissions.getPM2_5(emissionsSummary[TREATMENT_PM10][tStep-1][cRun]);
    }
  }


  public void calculateStatistics() {
    Simulation simulation = Simpplle.getCurrentSimulation();

    calculateStatistics(PROCESS);
    calculateStatistics(SPECIES);
    calculateStatistics(SIZE_CLASS);
    calculateStatistics(DENSITY);
    calculateStatistics(TREATMENT);

    if (simulation.trackSpecialArea()) {
      calculateSpecialStats(SPECIAL_AREA,PROCESS);
      calculateSpecialStats(SPECIAL_AREA,SPECIES);
      calculateSpecialStats(SPECIAL_AREA,SIZE_CLASS);
      calculateSpecialStats(SPECIAL_AREA,DENSITY);
      calculateSpecialStats(SPECIAL_AREA,TREATMENT);
    }
    if (simulation.trackOwnership()) {
      calculateSpecialStats(OWNERSHIP,PROCESS);
      calculateSpecialStats(OWNERSHIP,SPECIES);
      calculateSpecialStats(OWNERSHIP,SIZE_CLASS);
      calculateSpecialStats(OWNERSHIP,DENSITY);
      calculateSpecialStats(OWNERSHIP,TREATMENT);
    }
  }

  private void calculateStatistics(SimpplleType.Types kind) {
    int          numRuns, numSteps;
    int          i, j, k;
    Simulation   simulation = Simpplle.getCurrentSimulation();
    RegionalZone zone       = Simpplle.getCurrentZone();
    Iterator     keys;
    int          acres, mean, min, max;
    int[][]      statData, data;
    SimpplleType key;

    numRuns  = simulation.getNumSimulations();
    numSteps = simulation.getNumTimeSteps();
    keys     = summary[kind.ordinal()].keySet().iterator();

    while (keys.hasNext()) {
      statData = new int[numSteps+1][3];
      key      = (SimpplleType) keys.next();
      data     = (int[][]) summary[kind.ordinal()].get(key);

      for(i=0;i<=numSteps;i++) {
        acres = data[i][0];
        mean  = acres;
        min   = acres;
        max   = acres;
        for(j=1;j<numRuns;j++) {
          acres  = data[i][j];
          mean  += acres;
          if (acres < min) { min = acres; }
          if (acres > max) { max = acres; }
        }
        mean = Math.round(mean / numRuns);
        statData[i][MEAN] = mean;
        statData[i][MIN]  = min;
        statData[i][MAX]  = max;
      }
      stats[kind.ordinal()].put(key,statData);
    }
  }

  private void calculateSpecialStats(int specialKind, SimpplleType.Types kind) {
    int          numRuns, numSteps;
    int          i, j;
    Simulation   simulation = Simpplle.getCurrentSimulation();
    Iterator     keys, specialKeys;
    int          acres, mean, min, max;
    int[][]      statData, data;
    String       specialKey;
    SimpplleType key;
    HashMap    statHt, ht;

    numRuns     = simulation.getNumSimulations();
    numSteps    = simulation.getNumTimeSteps();
    specialKeys = specialSummary[specialKind][kind.ordinal()].keySet().iterator();

    while (specialKeys.hasNext()) {
      statHt     = new HashMap();
      specialKey = (String) specialKeys.next();
      ht = (HashMap) specialSummary[specialKind][kind.ordinal()].get(specialKey);

      keys = ht.keySet().iterator();
      while (keys.hasNext()) {
        statData = new int[numSteps+1][3];
        key      = (SimpplleType) keys.next();
        data     = (int[][]) ht.get(key);

        for(i=0;i<=numSteps;i++) {
          acres = data[i][0];
          mean  = acres;
          min   = acres;
          max   = acres;
          for(j=1;j<numRuns;j++) {
            acres  = data[i][j];
            mean  += acres;
            if (acres < min) { min = acres; }
            if (acres > max) { max = acres; }
          }
          mean = Math.round(mean / numRuns);
          statData[i][MEAN] = mean;
          statData[i][MIN]  = min;
          statData[i][MAX]  = max;
        }
        statHt.put(key,statData);
      }
      specialStats[specialKind][kind.ordinal()].put(specialKey,statHt);
    }
  }

  public String processSummaryReport()   { return getSummaryReport(PROCESS); }
  public String speciesSummaryReport()   { return getSummaryReport(SPECIES); }
  public String sizeClassSummaryReport() { return getSummaryReport(SIZE_CLASS);}
  public String densitySummaryReport()   { return getSummaryReport(DENSITY); }
  public String treatmentSummaryReport()   { return getSummaryReport(TREATMENT); }

  private String getSummaryReport(SimpplleType.Types kind) {
    PrintWriter  fout;
    StringWriter strOut = new StringWriter();
    StringBuffer strBuf;

    fout = new PrintWriter(strOut);
    summaryReport(fout,kind);
    fout.flush();
    strOut.flush();
    //strOut.close();
    return strOut.toString();
  }

  /**
   * Prints the Multiple Run Summary Report
   * @param outputFile is a File.
   */
  public void summaryReport (File outputFile) {
    PrintWriter      fout;

    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      summaryReport(fout,PROCESS);
      summaryReport(fout,SPECIES);
      summaryReport(fout,SIZE_CLASS);
      summaryReport(fout,DENSITY);
      summaryReport(fout,TREATMENT);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }

  private void summaryReport(PrintWriter fout, SimpplleType.Types kind) {
    int          numSteps;
    Simulation   simulation = Simpplle.getCurrentSimulation();
    int          i, j, lastTime;
    int          numCol=2, startPos=19;

    numSteps = simulation.getNumTimeSteps();

    if (kind == PROCESS) {  startPos = 30;  }

    fout.print(Formatting.padLeft(" ",20));
    switch (kind) {
      case PROCESS:    fout.print("PROCESS    "); break;
      case SPECIES:    fout.print("SPECIES    "); break;
      case SIZE_CLASS: fout.print("SIZE CLASS "); break;
      case DENSITY:    fout.print("DENSITY    "); break;
      case TREATMENT:  fout.print("TREATMENT  "); break;
    }

    fout.println("MEANS AND MIN-MAX VALUES");
    fout.println();
    fout.println(Formatting.padLeft("-Time-",40));
    fout.println();

    for(i=0;i<=numSteps;i += numCol) {
      if ( (i + numCol) > numSteps) {
        lastTime = numSteps;
      }
      else {
        lastTime = i + numCol - 1;
      }

      fout.print(Formatting.padLeft(" ",startPos));
      for(j=i;j<=lastTime;j++) {
        fout.print("MEAN -" + j +     "-  MIN-MAX        ");
      }
      fout.println();
      fout.println();
      printStats(fout,stats[kind.ordinal()],kind,i,lastTime);
      fout.println();
      fout.println();
    }
  }

  private void printStats(PrintWriter fout, HashMap ht, SimpplleType.Types kind, int tStep,
                          int lastTime) {
    int          i;
    Iterator     keys;
    String       str;
    SimpplleType key;
    int[][]      data;
    RegionalZone zone;
    float        acres;

    NumberFormat nf = NumberFormat.getInstance();
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    //nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(0);

    zone = Simpplle.getCurrentZone();
    keys = ht.keySet().iterator();

    while (keys.hasNext()) {
      key  = (SimpplleType) keys.next();
      data = (int[][]) ht.get(key);

      fout.print(Formatting.fixedField(key.toString(),25,true));

      for(i=tStep;i<=lastTime;i++) {
        acres = Area.getFloatAcres(data[i][MEAN]);
        fout.print(Formatting.fixedField(nf.format(acres),7) + " ");

        acres = Area.getFloatAcres(data[i][MIN]);
        str = "\"" + nf.format(acres);
        fout.print(Formatting.fixedField(str,8));

        acres = Area.getFloatAcres(data[i][MAX]);
        str = "-" + nf.format(acres) + "\"";
        fout.print(Formatting.fixedField(str,10,true));
      }
      fout.println();
    }
  }


  // **** Special Summary Reports. ****

  public void specialAreaSummaryReport(File outputFile) {
    specialSummaryReport(outputFile, SPECIAL_AREA);
  }

  public void ownershipSummaryReport(File outputFile) {
    specialSummaryReport(outputFile, OWNERSHIP);
  }

  private void specialSummaryReport (File outputFile, int kind) {
    PrintWriter      fout;

    try {
      fout = new PrintWriter(new FileOutputStream(outputFile));

      specialSummaryReport(fout,PROCESS,kind);
      specialSummaryReport(fout,SPECIES,kind);
      specialSummaryReport(fout,SIZE_CLASS,kind);
      specialSummaryReport(fout,DENSITY,kind);
      specialSummaryReport(fout,TREATMENT,kind);

      fout.flush();
      fout.close();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }

  private void specialSummaryReport(PrintWriter fout, SimpplleType.Types kind, int specialKind) {
    int           numSteps;
    Simulation    simulation = Simpplle.getCurrentSimulation();
    int           i, j, lastTime;
    int           numCol=2, startPos=19;
    HashMap     specialHt, ht;
    Iterator    keys;
    String        divisionName, key;
    int[][][]     statData;

    numSteps  = simulation.getNumTimeSteps();
    keys      = specialStats[specialKind][kind.ordinal()].keySet().iterator();
    specialHt = specialStats[specialKind][kind.ordinal()];

    if (specialKind == SPECIAL_AREA) {
      divisionName = "SPECIAL AREA";
    }
    else {
      divisionName = "OWNERSHIP";
    }

    if (kind == PROCESS) { startPos = 30; }

    fout.print(Formatting.padLeft(" ",20));
    switch (kind) {
      case PROCESS:    fout.print("PROCESS    "); break;
      case SPECIES:    fout.print("SPECIES    "); break;
      case SIZE_CLASS: fout.print("SIZE CLASS "); break;
      case DENSITY:    fout.print("DENSITY    "); break;
      case TREATMENT:  fout.print("TREATMENT  "); break;
    }

    fout.println("MEANS AND MIN-MAX VALUES BY " + divisionName);
    fout.println();
    fout.println(Formatting.padLeft("-Time-",40));
    fout.println();

    while (keys.hasNext()) {
      key = (String) keys.next();
      ht  = (HashMap) specialHt.get(key);
      for(i=0;i<=numSteps;i += numCol) {
        if ( (i + numCol) > numSteps) {
          lastTime = numSteps;
        }
        else {
          lastTime = i + numCol - 1;
        }

        fout.print(Formatting.padLeft(" ",startPos));
        for(j=i;j<=lastTime;j++) {
          fout.print("MEAN -" + j +     "-  MIN-MAX        ");
        }
        fout.println();
        fout.println(key);
        fout.println();
        printStats(fout,ht,kind,i,lastTime);
        fout.println();
        fout.println();
      }
    }
  }


  /**
   * Prints the Multiple Run Summary Report in spreadsheed format
   * @param outputFile is a File.
   */
  public void asciiSummaryReport (File outputFile) {
    PrintWriter fout;
    File        newFile;

    try {
      newFile = Utility.makeSuffixedPathname(outputFile,"-ls","txt");
      fout = new PrintWriter(new FileOutputStream(newFile));

      Simpplle.setStatusMessage("Generating Area Summary data file ...");
      fout.println("PROCESS");
      printStatsAscii(fout,PROCESS);
      fout.println("SPECIES");
      printStatsAscii(fout,SPECIES);
      fout.println("SIZECLASS");
      printStatsAscii(fout,SIZE_CLASS);
      fout.println("DENSITY");
      printStatsAscii(fout,DENSITY);
      if (summary[TREATMENT.ordinal()].size() > 0) {
        fout.println("TREATMENT");
      }
      printStatsAscii(fout,TREATMENT);
      if (Simpplle.getCurrentSimulation().fireSuppression()) {
        fout.println("SUPPRESSIONCOST");
        asciiFireSuppressionCostSummaryReport(fout);
      }
      fout.println("EMISSIONS");
      asciiEmissionsReport(fout);

      fout.flush();
      fout.close();
      Simpplle.clearStatusMessage();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }

  private void printStatsAscii(PrintWriter fout, SimpplleType.Types kind) {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    RegionalZone zone       = Simpplle.getCurrentZone();
    Iterator     keys;
    Species      species;
    int[][]      data;
    int          numRuns, numSteps, j, k;
    SimpplleType key;
    float        acres;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);

    numRuns  = simulation.getNumSimulations();
    numSteps = simulation.getNumTimeSteps();
    keys     = summary[kind.ordinal()].keySet().iterator();

    while (keys.hasNext()) {
      key  = (SimpplleType) keys.next();
      data = (int[][]) summary[kind.ordinal()].get(key);
      if (data == null) { continue; }

      fout.print("TIME");
      for(j=0;j<numRuns;j++) {
        if (kind == DENSITY) {
          fout.print(" " + ((Density)key).getGisPrintName() + "-" + (j+1));
        }
        else {
          fout.print(" " + key.toString() + "-" + (j + 1));
        }
      }
      fout.println();
      for(j=0;j<=numSteps;j++) {
        fout.print(j);
        for(k=0;k<numRuns;k++) {
          acres = Area.getFloatAcres(data[j][k]);
          fout.print(" " + nf.format(acres));
        }
        fout.println();
      }
    }
  }

  public void asciiFireSuppressionCostSummaryReport(PrintWriter fout) {
    asciiFireSuppressionCostSummaryReport(fout,Simpplle.getCurrentSimulation());
  }
  public void asciiFireSuppressionCostSummaryReport(PrintWriter fout, Simulation simulation) {
    asciiFireSuppressionCostSummaryReportFMZ(fout,simulation);
    if (simulation.trackSpecialArea()) {
      asciiFireSuppressionCostSummaryReportSA(fout,simulation);
    }
  }

  private void asciiFireSuppressionCostSummaryReportSA(PrintWriter fout, Simulation simulation) {
    RegionalZone zone       = Simpplle.getCurrentZone();
    int          nRuns, nSteps, j, k;
    double[]     costBySA;
    String       sa;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);
    nf.setGroupingUsed(false);

    nRuns  = simulation.getNumSimulations();
    nSteps = simulation.getNumTimeSteps();

    Iterator keys = specialAll[SPECIAL_AREA].keySet().iterator();
    while (keys.hasNext()) {
      sa = (String)keys.next();

      fout.print("TIME");
      for(j=0;j<nRuns;j++) {
        fout.print(" " + sa + "-" + (j+1));
      }
      fout.println();
      for(j=0;j<nSteps;j++) {
        fout.print((j+1));
        for(k=0;k<nRuns;k++) {
          costBySA = (double[]) fscSummarySA[k].get(sa);
          if (costBySA == null) {
            fout.print(" 0.00");
          }
          else {
            fout.print(" " + nf.format(costBySA[j]));
          }
        }
        fout.println();

      }
    }
  }
  private void asciiFireSuppressionCostSummaryReportFMZ(PrintWriter fout, Simulation simulation) {
    Fmz[]        allFmz;
    RegionalZone zone       = Simpplle.getCurrentZone();
    int          numRuns, numSteps, i, j, k;
    String       fmzName;
    double[]     costByFmz;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);
    nf.setGroupingUsed(false);

    numRuns  = simulation.getNumSimulations();
    numSteps = simulation.getNumTimeSteps();
    allFmz   = zone.getAllFmz();

    for(i=0;i<allFmz.length;i++) {
      fmzName = allFmz[i].getName();

      fout.print("TIME");
      for(j=0;j<numRuns;j++) {
        fout.print(" " + allFmz[i].toString() + "-" + (j+1));
      }
      fout.println();
      for(j=0;j<numSteps;j++) {
        fout.print((j+1));
        for(k=0;k<numRuns;k++) {
          costByFmz = (double[]) fscSummary[k].get(fmzName);
          fout.print(" " + nf.format(costByFmz[j]));
        }
        fout.println();

      }
    }
  }

  private void asciiEmissionsReport(PrintWriter fout) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int        numRuns, numSteps, i, j, k;

    numRuns  = simulation.getNumSimulations();
    numSteps = simulation.getNumTimeSteps();

    for(i=0;i<emissionTypes.length;i++) {
      fout.print("TIME");
      for(j=0;j<numRuns;j++) {
        fout.print(" " + emissionTypes[i] + "-" + (j+1));
      }
      fout.println();
      for(j=0;j<numSteps;j++) {
        fout.print((j+1));
        for(k=0;k<numRuns;k++) {
          fout.print(" " + Math.round(emissionsSummary[i][j][k]));
        }
        fout.println();
      }
    }
  }

  // **** ascii reports by Special Area and Ownership. ****

  /**
   * Generates ascii reports by Special Area.
   * @param outputFile is the output File.
   */
   public void asciiSpecialAreaSummaryReport (File outputFile) {
    asciiSpecialSummaryReport(outputFile, SPECIAL_AREA);
  }

  /**
   * Generates ascii reports by Ownership.
   * @param outputFile is the output File.
   */
  public void asciiOwnershipSummaryReport (File outputFile) {
    asciiSpecialSummaryReport(outputFile, OWNERSHIP);
  }

  private void asciiSpecialSummaryReport (File outputFile, int kind) {
    PrintWriter fout;
    File        newFile;
    String      own, msg, suffix;

    if (kind == SPECIAL_AREA) {
      suffix = "-ls-sa";
      msg    = "(by Special Area)...";
    }
    else {
      suffix = "-ls-own";
      msg    = "(by Ownership)...";
    }

    try {
      newFile = Utility.makeSuffixedPathname(outputFile,suffix,"txt");
      fout = new PrintWriter(new FileOutputStream(newFile));

      msg = Simpplle.endl + "Generating Area Summary data file " + msg;
      Simpplle.setStatusMessage(msg);

      fout.println("PROCESS");
      printSpecialStatsAscii(fout,kind,PROCESS);
      fout.println("SPECIES");
      printSpecialStatsAscii(fout,kind,SPECIES);
      fout.println("SIZECLASS");
      printSpecialStatsAscii(fout,kind,SIZE_CLASS);
      fout.println("DENSITY");
      printSpecialStatsAscii(fout,kind,DENSITY);
      if (summary[TREATMENT.ordinal()].size() > 0) {
        fout.println("TREATMENT");
      }
      printSpecialStatsAscii(fout,kind,TREATMENT);

      fout.flush();
      fout.close();
      Simpplle.clearStatusMessage();
    }
    catch (IOException IOX) {
      System.out.println("Problems writing output file.");
    }
  }

  private void printSpecialStatsAscii(PrintWriter fout, int specialKind,
                                      SimpplleType.Types kind) {
    Process[]     summaryProcesses;
    Iterator      keys, specialKeys;
    int[][]       data;
    HashMap     specialHt, ht;
    Simulation    simulation = Simpplle.getCurrentSimulation();
    RegionalZone  zone       = Simpplle.getCurrentZone();
    int           processId, numRuns, numSteps, i, j, k;
    String        specialKey;
    SimpplleType  key;
    float         acres;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    //nf.setMaximumFractionDigits(Area.getAcresPrecision());
    nf.setMaximumFractionDigits(0);

    numRuns          = simulation.getNumSimulations();
    numSteps         = simulation.getNumTimeSteps();
    specialKeys      = specialSummary[specialKind][kind.ordinal()].keySet().iterator();
    specialHt        = specialSummary[specialKind][kind.ordinal()];

    while (specialKeys.hasNext()) {
      specialKey = (String) specialKeys.next();
      ht         = (HashMap) specialHt.get(specialKey);
      keys       = ht.keySet().iterator();

      while (keys.hasNext()) {
        key  = (SimpplleType) keys.next();
        data = (int[][]) ht.get(key);

        fout.print("TIME-" + specialKey);
        for(j=0;j<numRuns;j++) {
          fout.print(" " + key.toString() + "-" + (j+1));
        }
        fout.println();
        for(j=0;j<=numSteps;j++) {
          fout.print(j);
          for(k=0;k<numRuns;k++) {
            acres = Area.getFloatAcres(data[j][k]);
            fout.print(" " + nf.format(acres));
          }
          fout.println();
        }
      }
    }
  }

  public HashMap<SimpplleType,MyInteger> getFrequency(Evu evu, SimpplleType.Types kind, int tStep) throws SimpplleError {
    HashMap<SimpplleType,int[]> hm = getFreqCountData(evu,kind);
    HashMap<SimpplleType,MyInteger> freqHm = new HashMap<SimpplleType,MyInteger>();
    MyInteger count;
    int[]     counts;

    int       totalCount=0;

    for (SimpplleType key : hm.keySet()) {
      count = freqHm.get(key);
      if (count == null) {
        count = new MyInteger(0);
        freqHm.put(key, count);
      }
      counts = hm.get(key);
      count.plus(counts[tStep]);
    }
    int[] totalCounts = frequencyTotCount.get(evu);
    totalCount += totalCounts[tStep];

    // Convert count to a frequency;
    for (SimpplleType key : freqHm.keySet()) {
      count = freqHm.get(key);
      float freq = (float)count.intValue() / (float)totalCount;
      freq *= 100.0f;
      count.setValue(Math.round(freq));
    }

    return freqHm;
  }


  public HashMap<SimpplleType,MyInteger> getFrequency(Evu evu, SimpplleType.Types kind) throws SimpplleError {
    HashMap<SimpplleType,int[]> hm = getFreqCountData(evu,kind);
    HashMap<SimpplleType,MyInteger> freqHm = new HashMap<SimpplleType,MyInteger>();
    MyInteger count;
    int[]     counts;

    for (SimpplleType key : hm.keySet()) {
      count = freqHm.get(key);
      if (count == null) {
        count = new MyInteger(0);
        freqHm.put(key, count);
      }

      counts = hm.get(key);
      for (int ts = 0; ts < counts.length; ts++) {
        count.plus(counts[ts]);
      }
    }

    int[] totalCounts = frequencyTotCount.get(evu);
    int totalCount = 0;
    for (int ts=0; ts<totalCounts.length; ts++) {
      totalCount += totalCounts[ts];
    }

    // Convert count to a frequency;
    for (SimpplleType key : freqHm.keySet()) {
      count = freqHm.get(key);
      float freq = (float)count.intValue() / (float)totalCount;
      freq *= 100.0f;
      count.setValue(Math.round(freq));
    }

    return freqHm;
//    switch (kind) {
//      case SPECIES:     return speciesFreq.get(evu.getId());
//      case SIZE_CLASS:  return sizeClassFreq.get(evu.getId());
//      case DENSITY:     return densityFreq.get(evu.getId());
//      case PROCESS:     return processFreq.get(evu.getId());
//      default: return null;
//    }
  }
//  public void calculateFrequency() throws SimpplleError {
//    try {
//      Session session = DatabaseCreator.getSessionFactory().openSession();
//
//      speciesFreq   = calculateFrequency(session, SPECIES, -1);
//      sizeClassFreq = calculateFrequency(session, SIZE_CLASS, -1);
//      densityFreq   = calculateFrequency(session, DENSITY, -1);
//      processFreq   = calculateFrequency(session, PROCESS, -1);
//
//      session.close();
//    }
//    catch (HibernateException ex) {
//      throw new SimpplleError("Error accessing database" + ex.getMessage(),ex);
//    }
//  }
//
//  public HashMap<Integer,HashMap<SimpplleType,Integer>> calculateFrequency(Session session, int kind, int tStep)
//  throws HibernateException
//  {
//    Simulation simulation = Simpplle.getCurrentSimulation();
//    MultipleRunSummary mrSummary = simulation.getMultipleRunSummary();
//
//    SimpplleType[] attribs = mrSummary.getAttributes(kind);
//    HashMap<Integer,HashMap<SimpplleType,Integer>> hm =
//      new HashMap<Integer,HashMap<SimpplleType,Integer>>();
//
//    String var;
//    switch (kind) {
//      case SPECIES:     var = "vegType.species.species"; break;
//      case SIZE_CLASS:  var = "vegType.sizeClass.sizeClass"; break;
//      case DENSITY:     var = "vegType.density.density"; break;
//      case PROCESS:     var = "process.processName"; break;
//      default: return null;
//    }
//
//    try {
//      StringBuffer strBuf = new StringBuffer();
//      strBuf.append("select accum.slink, count(*) from AccumData as accum");
//      if (tStep >= 0) {
//        strBuf.append(" where accum.timeStep = ");
//        strBuf.append(tStep);
//      }
//      strBuf.append(" group by accum.slink");
//
//      Query q = session.createQuery(strBuf.toString());
//      List totList = q.list();
//
//      HashMap <Integer,Integer> totHm = new HashMap<Integer,Integer>(totList.size());
//      for (Object elem : totList) {
//        Object[] items = (Object[])elem;
//        totHm.put((Integer)items[0],(Integer)items[1]);
//      }
//
//
//      for (SimpplleType attrib : attribs) {
//        strBuf = new StringBuffer();
//        strBuf.append("select accum.slink, count(*) from AccumData as accum");
//
//        strBuf.append(" where accum.");
//        strBuf.append(var);
//        strBuf.append(" = ");
//        strBuf.append("'");
//        strBuf.append(attrib.toString());
//        strBuf.append("'");
//        if (tStep >= 0) {
//          strBuf.append(" and accum.timeStep = ");
//          strBuf.append(tStep);
//        }
//        strBuf.append(" group by accum.slink");
//
//        q = session.createQuery(strBuf.toString());
//        List list = q.list();
//
//        for (Object elem : list) {
//          Object[] items = (Object[])elem;
//          int id = (Integer)items[0];
//          int tmpCount = (Integer)items[1];
//
//          HashMap<SimpplleType, Integer> tmpHm = hm.get(id);
//          if (tmpHm == null) {
//            tmpHm = new HashMap<SimpplleType, Integer>();
//          }
//          hm.put(id,tmpHm);
//
//          Integer totCount = totHm.get(id);
//
//          float freq = (float) tmpCount / (float) totCount;
//          freq *= 100.0f;
//
//          tmpHm.put(attrib, Math.round(freq));
//        }
//      }
//
//      return hm;
//    }
//    catch (HibernateException ex) {
//      throw ex;
//    }
//  }

}




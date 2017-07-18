/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.Flat3Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import simpplle.comcode.Climate.Season;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * VegSimStateData describes the state of an existing vegetation unit (EVU) during a single time step.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class VegSimStateData implements Externalizable {

  static final long serialVersionUID = -159138068111213685L;
  static final int version = 3;

  private long           dbid; // Database row id
  private int            slink;
  private short          timeStep;
  private short          run;
  private Lifeform       lifeform;
  private VegetativeType veg;
  private ProcessType    process;
  private short          prob;
  private Climate.Season season;
  private short          seasonOrd;  // Used for hibernate mapping only!

  private int fireSpreadRuleIndex = -1;
  private int fireRegenRuleIndex  = -1;
  private int succRegenRuleIndex  = -1;

  // Object[Species][Integer]
  // Flat3Map
  // Key: InclusionRuleSpecies, Value: Percent(float)
  private Map<InclusionRuleSpecies, Float> trackingSpecies;

  private static int writeCount=0;

  public void destroy() {
    lifeform        = null;
    veg             = null;
    process         = null;
    season          = null;
    trackingSpecies = null;
  }

  public VegSimStateData() {
    this.process         = ProcessType.SUCCESSION;
    this.prob            = Evu.NOPROB;
    this.trackingSpecies = null;
    this.season          = Climate.Season.YEAR;
  }

  public VegSimStateData(int slink) {
    this(slink,0,0);
  }

  public VegSimStateData(int slink, int timeStep, int run) {
    this();
    this.slink    = slink;
    this.timeStep = (short)timeStep;
    this.run      = (short)run;
  }

  public VegSimStateData(int slink, VegetativeType veg) {
    this(slink);
    this.veg = veg;
    if (Area.multipleLifeformsEnabled()) {
      this.lifeform = veg.getSpecies().getLifeform();
    } else {
      this.lifeform = Lifeform.NA;
    }
  }

  public VegSimStateData(int slink, int timeStep, int run, VegetativeType veg, ProcessType process, int prob) {
    this(slink,veg);
    this.timeStep = (short)timeStep;
    this.run      = (short)run;
    this.process  = process;
    this.prob     = (short)prob;
  }

  public VegSimStateData(int slink, int timeStep, int run, VegetativeType vegType, ProcessType process, int prob, Climate.Season season) {
    this(slink,timeStep,run,vegType,process,prob);
    this.season = season;
  }

  public VegSimStateData(int slink,int timeStep, int run, VegSimStateData state) {
    this(slink,timeStep,run,state.veg,state.process,state.prob,state.season);
    if (state.trackingSpecies != null) {
      setTrackingSpecies(state.trackingSpecies);
    }
  }

  public void initializeTrackingSpecies(int numSpecies) {
    trackingSpecies = new Flat3Map();
  }

//  public void setSpecies(InclusionRuleSpecies[] trkSpecies, float[] trkSpeciesPct) {
//    if (trkSpecies == null) { return; }
//
//    this.species = new InclusionRuleSpecies[trkSpecies.length];
//    this.speciesPct = new float[trkSpeciesPct.length];
//
//    for (int i=0; i<trkSpecies.length; i++) {
//      this.species[i]    = trkSpecies[i];
//      this.speciesPct[i] = trkSpeciesPct[i];
//    }
//  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    int version = in.readInt();

    if (version > 2) {

      slink    = in.readInt();
      timeStep = (short)in.readInt();
      run      = (short)in.readInt();
      lifeform = (Lifeform)in.readObject();

    } else {

      // For old versions these values are set when this class is loaded
      // by simpplle.comcode.Evu

    }

    veg = VegetativeType.readExternalData(in);
    process = ProcessType.readExternalSimple(in);

    String str = (String)in.readObject();

    if      (str.equals(Evu.D_STR))      prob = Evu.D;
    else if (str.equals(Evu.L_STR))      prob = Evu.L;
    else if (str.equals(Evu.S_STR))      prob = Evu.S;
    else if (str.equals(Evu.SE_STR))     prob = Evu.SE;
    else if (str.equals(Evu.SFS_STR))    prob = Evu.SFS;
    else if (str.equals(Evu.SUPP_STR))   prob = Evu.SUPP;
    else if (str.equals(Evu.COMP_STR))   prob = Evu.COMP;
    else if (str.equals(Evu.GAP_STR))    prob = Evu.GAP;
    else if (str.equals(Evu.NOPROB_STR)) prob = Evu.NOPROB;
    else {
      try {
        prob = (short)Integer.parseInt(str);
      } catch (NumberFormatException ex) {
        prob = Evu.NOPROB;
      }
    }

    int size = in.readInt();

    if (size > 0) {

      trackingSpecies = new Flat3Map();

      for (int i = 0; i < size; i++) {

        String spStr = (String)in.readObject();

        float pct;
        if (version == 1) {
          pct = (float)in.readInt();
        } else {
          pct = in.readFloat();
        }

        trackingSpecies.put(InclusionRuleSpecies.get(spStr, true), pct);

      }
    }

    str = (String)in.readObject();

    switch (str) {

      case "SPRING": season = Climate.Season.SPRING; break;
      case "SUMMER": season = Climate.Season.SUMMER; break;
      case "FALL":   season = Climate.Season.FALL;   break;
      case "WINTER": season = Climate.Season.WINTER; break;

    }

  }

  public void writeExternal(ObjectOutput out) throws IOException {

    out.writeInt(version);
    out.writeInt(slink);
    out.writeInt(timeStep);
    out.writeInt(run);
    out.writeObject(lifeform);

    veg.writeExternal(out);
    process.writeExternalSimple(out);
    out.writeObject(getProbString());

    if (trackingSpecies != null) {

      Flat3Map map = (Flat3Map)trackingSpecies;
      out.writeInt(map.size());

      MapIterator it = map.mapIterator();
      while (it.hasNext()) {
        InclusionRuleSpecies sp = (InclusionRuleSpecies)it.next();
        out.writeObject(sp.toString());
        out.writeFloat((Float)it.getValue());
      }

    } else {

      out.writeInt(0);

    }

    out.writeObject(getSeasonString());

  }

  public VegetativeType getVegType() {
    return veg;
  }

  /**
   * Sets the simulation vegetative type.  If the area being evaluated has multiple life forms sets the life from for this area to 
   * @param veg
   */
  public void setVegType(VegetativeType veg) {

    this.veg = veg;

    if (Area.multipleLifeformsEnabled()) {
      this.lifeform = this.veg.getSpecies().getLifeform();
    } else {
      this.lifeform = Lifeform.NA;
    }
  }

  /**
   * Gets the simulation vegetative type.
   * @return
   */
  public VegetativeType getVeg() {
    return veg;
  }

  /**
   * Used by hibernate
   */
  public void setVeg(VegetativeType veg) {
    // Make sure we get the existing instance, not a newly created one.
    Evu evu = Simpplle.getCurrentArea().getEvu(slink);
    HabitatTypeGroup group = evu.getHabitatTypeGroup();

    this.veg = group.getVegetativeType(veg.getSpecies(),veg.getSizeClass(),veg.getAge(),veg.getDensity());
  }

  /**
   * Gets the process type.
   * @return process type
   */
  public ProcessType getProcess() {
    return process;
  }

  /**
   * Gets the probability
   * @return probability
   */
  public short getProb() {
    return prob;
  }

  /**
   * Sets the probability for this vegetative simulation state data.  
   * @param prob probability
   */
  public void setProb(short prob) {
    this.prob = prob;
  }

  public void setProb(int prob) {
    this.prob = (short)prob;
  }

  public float getFloatProb() {
    return ((float)prob / (float)Utility.pow(10,Area.getAcresPrecision()));
  }

  public String getProbString() {
    switch (prob) {
      case Evu.D:      return Evu.D_STR;
      case Evu.L:      return Evu.L_STR;
      case Evu.S:      return Evu.S_STR;
      case Evu.SE:     return Evu.SE_STR;
      case Evu.SFS:    return Evu.SFS_STR;
      case Evu.SUPP:   return Evu.SUPP_STR;
      case Evu.COMP:   return Evu.COMP_STR;
      case Evu.GAP:    return Evu.GAP_STR;
      case Evu.NOPROB: return Evu.NOPROB_STR;
      default:         return IntToString.get(prob);
    }
  }

  public Climate.Season getSeason() {
    return season;
  }

  public void setSeason(Climate.Season season) {
    this.season = season;
  }

  public int getFireSpreadRuleIndex() {
    return fireSpreadRuleIndex;
  }

  public void setFireSpreadRuleIndex(int index) {
    fireSpreadRuleIndex = index;
  }

  public int getFireRegenerationRuleIndex() {
    return fireRegenRuleIndex;
  }

  public void setFireRegenerationRuleIndex(int index) {
    fireRegenRuleIndex = index;
  }

  public int getSuccessionRegenerationRuleIndex() {
    return succRegenRuleIndex;
  }

  public void setSuccessionRegenerationRuleIndex(int index) {
    succRegenRuleIndex = index;
  }

  public void resetRegenRules(int resetValue) {
    fireRegenRuleIndex = resetValue;
    succRegenRuleIndex = resetValue;
  }

  public String getSeasonString() {
    return season.toString();
  }

  public void setProcess(ProcessType process) {
    this.process = ProcessType.get(process.toString());
  }

  public void addTrackSpecies(InclusionRuleSpecies trk_species, float percent) {
    if (trackingSpecies == null) {
      trackingSpecies = new Flat3Map();
    }
    trackingSpecies.put(trk_species, percent);
  }

  public float getSpeciesPercent(InclusionRuleSpecies trk_species) {
    if (trackingSpecies == null) { return -1; }

    Float pct = trackingSpecies.get(trk_species);
    return (pct != null) ? pct : -1;

  }

  public void addMissingTrackSpecies() {
    Set<InclusionRuleSpecies> allSpecies = veg.getTrackingSpecies();
    if (allSpecies != null) {
      for (InclusionRuleSpecies sp : allSpecies) {
        if (getSpeciesPercent(sp) == -1) {
          addTrackSpecies(sp,0);
        }
      }
    }
  }

  public void removeInvalidTrackSpecies() {

    if (trackingSpecies == null) return;

    ArrayList<InclusionRuleSpecies> removeList = new ArrayList<>();

    for (Object spObj : trackingSpecies.keySet()) {

      InclusionRuleSpecies sp = (InclusionRuleSpecies)spObj;

      if (veg.isTrackingSpecies(sp)) continue;

      Float pct = trackingSpecies.get(sp);
      if (pct != null && pct == 0) {
        removeList.add(sp);
      }
    }

    for (int i=0; i<removeList.size(); i++) {
      trackingSpecies.remove(removeList.get(i));
    }
  }

  /** For backward compatability
   *
   */
  public Map getAccumDataSpeciesMap() {
    return new HashMap<>(trackingSpecies);
  }

  /**
   * Returns a generic Map of Tracking Species for use in Hibernate.
   * @return Map
   */
  public Map getTrackingSpecies() {
    return trackingSpecies;
  }

  public InclusionRuleSpecies[] getTrackingSpeciesArray() {
    if (trackingSpecies != null) {
      return trackingSpecies.keySet().toArray(new InclusionRuleSpecies[trackingSpecies.size()]);
    }
    return null;
  }

  public Flat3Map getTrackingSpeciesMap() {
    return (Flat3Map)trackingSpecies;
  }

  public void setTrackingSpecies(Map trkSpecies) {
    trackingSpecies = new Flat3Map();
    for (Object elem : trkSpecies.keySet()) {
      InclusionRuleSpecies sp = (InclusionRuleSpecies)elem;
      Float pct = (Float)trkSpecies.get(sp);
      trackingSpecies.put(sp,pct);
    }
  }

  public float updateTrackingSpecies(InclusionRuleSpecies trkSpecies, float change) {

    Float pct = trackingSpecies.get(trkSpecies);
    float newPct = change;

    if (pct != null) {
      newPct += pct;
    }

    if (newPct < 0) {
      newPct = 0.0f;
    } else if (newPct > 100) {
      newPct = 100.0f;
    }

    trackingSpecies.put(trkSpecies,newPct);

    return newPct;
  }

  /**
   * This one is called only in the case of invasive species.
   * In addition to the change as Percent difference this one also removes
   * species that have been reduced to zero.
   */
  public float updateTrackingSpecies(InclusionRuleSpecies trkSpecies, float change, boolean changeAsPercent) {

    float newPct;

    if (changeAsPercent) {

      Float pct = trackingSpecies.get(trkSpecies);

      if (pct == null) {
        pct = 0.0f;
      }

      float pctChange = pct * (change / 100.0f);
      newPct = pct + pctChange;

      if (newPct < 0) {
        newPct = 0.0f;
      } else if (newPct > 100) {
        newPct = 100.0f;
      }

      trackingSpecies.put(trkSpecies, newPct);

    } else {

      newPct = updateTrackingSpecies(trkSpecies,change);

    }

    if (newPct < 0.005) {
      trackingSpecies.remove(trkSpecies);
      newPct = 0.0f;
    }

    return newPct;
  }

  public short getTimeStep() {
    return timeStep;
  }

  public void setTimeStep(short timeStep) {
    this.timeStep = timeStep;
  }

  public void setTimeStep(int timeStep) {
    this.timeStep = (short)timeStep;
  }

  public short getRun() {
    return run;
  }

  public void setRun(short run) {
    this.run = run;
  }

  public void setRun(int run) {
    this.run = (short) run;
  }

  public Lifeform getLifeform() {
    return lifeform;
  }

  public void setLifeform(Lifeform lifeform) {
    this.lifeform = Lifeform.findByName(lifeform.getName());
  }

  public int getSlink() {
    return slink;
  }

  public void setSlink(int slink) {
    this.slink = slink;
  }

  public short getSeasonOrd() {
    return (short)season.ordinal();
  }

  public void setSeasonOrd(short ordinal) {
    switch (ordinal) {
      case 0: season = Season.SPRING; break;
      case 1: season = Season.SUMMER; break;
      case 2: season = Season.FALL;   break;
      case 3: season = Season.WINTER; break;
      case 4: season = Season.YEAR;   break;
    }
  }

  public long getDbid() {
    return dbid;
  }

  public void setDbid(long dbid) {
    this.dbid = dbid;
  }

  public static void clearWriteCount() {
    writeCount=0;
  }

  public static void writeDatabase(Session session, VegSimStateData state) throws SimpplleError {
    try {
      session.saveOrUpdate(state.lifeform);
      session.saveOrUpdate(state.veg.getSpecies());
      session.saveOrUpdate(state.veg.getSizeClass());
      session.saveOrUpdate(state.veg.getDensity());
      session.saveOrUpdate(state.process);

      if (state.trackingSpecies != null) {
        MapIterator it = ((Flat3Map) state.trackingSpecies).mapIterator();
        while (it.hasNext()) {
          InclusionRuleSpecies sp = (InclusionRuleSpecies) it.next();
          session.saveOrUpdate(sp);
        }
      }

      session.saveOrUpdate(state);
      writeCount++;

      if (writeCount % 30 == 0) {
        session.flush();
        session.clear();
      }
    } catch (HibernateException ex) {
      throw new SimpplleError("Problems writing database", ex);
    }
  }

  public static void writeAccessFiles(PrintWriter fout, PrintWriter trackOut, Evu evu, VegSimStateData state) {

    Simulation sim = Simulation.getInstance();

    int run = Simulation.getCurrentRun() + 1;  // TODO: Use VegSimStateData's value
    int ts  = Simulation.getCurrentTimeStep(); // TODO: Use VegSimStateData's value

    //if (run != state.run) System.out.println("False");

    sim.addAccessLifeform(state.lifeform);
    sim.addAccessSpecies(state.veg.getSpecies());
    sim.addAccessSizeClass(state.veg.getSizeClass());
    sim.addAccessDensity(state.veg.getDensity());
    sim.addAccessProcess(state.process);
    sim.addAccessOwnership(Ownership.get(evu.getOwnership(),true));
    sim.addAccessSpecialArea(SpecialArea.get(evu.getSpecialArea(),true));

    Treatment treatment = evu.getTreatment(ts);
    int treatmentId = -1;
    if (treatment != null) {
      sim.addAccessTreatment(treatment.getType());
      treatmentId = treatment.getType().getSimId();
    }

    // state data
    int   lifeId    = state.lifeform.getSimId();
    int   speciesId = state.getVeg().getSpecies().getSimId();
    int   sizeId    = state.getVeg().getSizeClass().getSimId();
    int   age       = state.getVeg().getAge();
    int   densityId = state.getVeg().getDensity().getSimId();
    int   processId = state.process.getSimId();
    int   seasonId  = state.season.ordinal();
    int   prob      = state.getProb();
    float fProb     = state.getFloatProb();
    int   firerule  = state.getFireSpreadRuleIndex();
    int   fireRegenRuleIndex        = state.getFireRegenerationRuleIndex();
    int   successionRegenRuleIndex  = state.getSuccessionRegenerationRuleIndex();

    // Evu Data
    float acres     = evu.getFloatAcres();
    int originUnitId  = (evu.getOriginUnit() != null) ? evu.getOriginUnit().getId() : -1;
    int fromUnitId    = evu.fromEvuId;

    String probStr = "n/a";
    if (prob < 0) {
      fProb = 0.0f;
      probStr = state.getProbString();
    }

    fout.printf("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%.1f,%s,%d,%d,%d,%d,%d,%d%n",
        run ,ts, seasonId, state.slink, lifeId, speciesId, sizeId, age, densityId, processId, fProb,
        probStr, treatmentId, originUnitId, fromUnitId, firerule, fireRegenRuleIndex, successionRegenRuleIndex);

    state.resetRegenRules(-1);

    if (state.trackingSpecies != null) {

      MapIterator it = ((Flat3Map) state.trackingSpecies).mapIterator();

      while (it.hasNext()) {

        InclusionRuleSpecies sp = (InclusionRuleSpecies) it.next();

        int spId = sp.getSimId();
        float pct = (Float)it.getValue();
        
        trackOut.printf("%d,%d,%d,%d,%d,%.1f%n",run,ts,state.slink,lifeId,spId,pct);
        
        sim.addAccessIncRuleSpecies(sp);
      }
    }
    writeCount++;

    if (writeCount % 30 == 0) {
      fout.flush();
    }
  }

  // do we really need all these commented out methods in here still?  Isn't that what version control is for?
//  public static long writeRandomAccessFile(RandomAccessFile simFile, VegSimStateData state)
//    throws SimpplleError
//  {
//    long filePos;
//    try {
//      Simulation.getInstance().makeSimFileWriteReady();
//      filePos = simFile.getFilePointer();
//
//      simFile.writeInt(state.slink);
//      simFile.writeShort(state.timeStep);
//      simFile.writeShort(state.run);
//      simFile.writeShort(state.lifeform.getSimId());
//      simFile.writeShort(state.veg.getSpecies().getSimId());
//      simFile.writeShort(state.veg.getSizeClass().getSimId());
//      simFile.writeShort((short)state.veg.getAge());
//      simFile.writeShort(state.veg.getDensity().getSimId());
//      simFile.writeShort(state.process.getSimId());
//      simFile.writeShort(state.prob);
//      simFile.writeShort(state.getSeasonOrd());
//
//      if (state.trackingSpecies != null) {
//        simFile.writeShort(state.trackingSpecies.size());
//        MapIterator it = state.getTrackingSpeciesMap().mapIterator();
//        while (it.hasNext()) {
//          InclusionRuleSpecies sp = (InclusionRuleSpecies) it.next();
//          Float pctObj = (Float) it.getValue();
//          float pct = (pctObj != null) ? pctObj : 0;
//
//          simFile.writeShort(sp.getSimId());
//          simFile.writeFloat(pct);
//        }
//      }
//      Simulation.getInstance().updateSimFileWritePos();
//    }
//    catch (IOException ex) {
//      throw new SimpplleError(ex.getMessage(),ex);
//    }
//
//    return filePos;
//  }
//
//  public static VegSimStateData readRandomAccessFile(RandomAccessFile simFile, long filePos)
//    throws SimpplleError
//  {
//    VegSimStateData state = new VegSimStateData();
//    try {
//      simFile.seek(filePos);
//
//      state.slink    = simFile.readInt();
//      state.timeStep = simFile.readShort();
//      state.run      = simFile.readShort();
//      state.lifeform = Lifeform.lookUpLifeform(simFile.readShort());
//
//      // Vegetative Type
//      Species   species   = Species.lookUpSpecies(simFile.readShort());
//      SizeClass sizeClass = SizeClass.lookUpSizeClass(simFile.readShort());
//      int       age       = simFile.readShort();
//      Density   density   = Density.lookUpDensity(simFile.readShort());
//
//      Evu evu = Simpplle.getCurrentArea().getEvu(state.slink);
//      HabitatTypeGroup group = evu.getHabitatTypeGroup();
//      state.veg = group.getVegetativeType(species,sizeClass,age,density);
//
//      state.process = ProcessType.lookUpProcessType(simFile.readShort());
//      state.prob    = simFile.readShort();
//      state.setSeasonOrd(simFile.readShort());
//    }
//    catch (IOException ex) {
//      throw new SimpplleError(ex.getMessage(),ex);
//    }
//
//    return state;
//  }

//  public Object[][] getSpecies() {
//    return species;
//  }

}

package simpplle.comcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;
import java.util.List;
import org.hibernate.*;
import java.sql.SQLException;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Spreading Process Occurrence, a type of Process Occurrence.  There are a couple of classes that work as a unit.  
 * They are Process Occurrence, Process Occurrence Spreading, and Process Occurrence Spreading Fire. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */

public class ProcessOccurrenceSpreading extends ProcessOccurrence implements Externalizable {
  static final long serialVersionUID = 8554069880579897721L;
  static final int  version          = 2;

  protected int        eventAcres;
  protected LinkedList spreadQueue;
  protected boolean    finished;
  protected Node       root;
  protected int        nodeCount=0;

//  protected HashMap    nodeLookup;
  protected static HashMap    tmpNodeLookup;
  protected static HashMap    tmpToNodesHm;
  protected static ArrayList  tmpToUnits = new ArrayList(25);

 
  public class Node  implements Externalizable {
    static final long serialVersionUID = 9013071459014314578L;
    static final int  version          = 2;
    public Node              fromNode;
    public ProcessOccurrence data;
    public int               relElevation;  // relative to the origin unit
    public Node[]            toNodes;

    public Node() {
      fromNode = null;
      data     = null;
      toNodes  = null;
    }

    public void setToNodes(final Node[] toNodes) {
      this.toNodes = toNodes;
      nodeCount += toNodes.length;
    }
    public void setToNode(int index, Node node) {
      toNodes[index] = node;
    }
/**
 * reads in objects pertinent to this class from an external source.  
 * These are, in order: version, EVU of the unit the event came from, process occurrence data, 
 */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
      Area area = Simpplle.getCurrentArea();

      int version = in.readInt();

      Evu fromUnit = area.getEvu(in.readInt());
      data = (ProcessOccurrence)in.readObject();

      // There were cases in old format files where there were not only
      // duplicate spread to units but circularity in from Units with no
      // clear root.  Fortunately the variable unit from
      // ProcessOccurrence identifies the correct root unit.
      if (fromUnit == null || data.getUnit() == unit) {
        fromNode = null;
        root = this;
        nodeCount += 1;
      }
      else {
        fromNode = (Node)tmpNodeLookup.get(fromUnit);
        if (fromNode == null) {
          fromNode = new Node();
          tmpNodeLookup.put(fromUnit,fromNode);
        }
      }


      Node thisNode = (Node)tmpNodeLookup.get(data.getUnit());
      if (thisNode == null) {
        thisNode = this;
        tmpNodeLookup.put(data.getUnit(),thisNode);
      }
      else {
        thisNode.fromNode  = this.fromNode;
        thisNode.data      = this.data;
        if (root == this) { root = thisNode; }
        this.fromNode = null;
        this.data     = null;
      }

      thisNode.relElevation = in.readInt();

      Node node;
      Evu  tmpUnit;
      int size = in.readInt();
      if (size == 0) { return; }

      tmpToUnits.clear();
      for (int i=0; i<size; i++) {
        tmpUnit = area.getEvu(in.readInt());
        node = (Node)tmpNodeLookup.get(tmpUnit);
        if (tmpToUnits.contains(tmpUnit) == false) {
          tmpToUnits.add(tmpUnit);
        }
      }

      thisNode.setToNodes(new Node[tmpToUnits.size()]);
      for (int i=0; i<tmpToUnits.size(); i++) {
        tmpUnit = (Evu)tmpToUnits.get(i);
        node = (Node)tmpNodeLookup.get(tmpUnit);
        if (node == null) {
          node = new Node();
          tmpNodeLookup.put(tmpUnit,node);
        }
        thisNode.setToNode(i,node);
      }
      tmpToUnits.clear();
    }
    public void writeExternal(ObjectOutput out) throws IOException {
      out.writeInt(version);
      if (fromNode != null) {
        out.writeInt(fromNode.data.getUnit().getId());
      }
      else { out.writeInt(-1); }

      out.writeObject(data);
      out.writeInt(relElevation);

      int size = (toNodes != null) ? toNodes.length : 0;
      out.writeInt(size);
      for (int i = 0; i < size; i++) {
        out.writeInt(toNodes[i].data.getUnit().getId());
      }

    }
    public void printMe() {
      if (fromNode != null) {
        System.out.print("parent=" + fromNode.data.getUnit().getId());
      }
      System.out.print(",from=" + data.getUnit().getId());
      System.out.print(",to=");
      if (toNodes == null) { System.out.println(); return; }

      for (int i=0; i<toNodes.length; i++) {
        System.out.print("," + toNodes[i].data.getUnit().getId());
      }
      System.out.println();
    }

 //   /**
  //   * This method is used to save this simulation data to a file.
   //  * This will recursively print the whole network of nodes to the
  //   * provided PrintWriter.
   //  * @param fout a PrintWriter the output stream.
   //  */
//    public void save(PrintWriter fout) {
//      Node toNode;
//      if (toNodes.size() == 0) { return; }
//
//      for (int i=0; i<toNodes.size(); i++) {
//        toNode = (Node)toNodes.get(i);
//        fout.print(',');
//        fout.print(data.getUnit().getId());
//        fout.print(',');
//        fout.print(toNode.data.getUnit().getId());
//        fout.print(',');
//        fout.print(toNode.data.getProcess());
//        fout.print(',');
//        fout.print(toNode.data.getProcessProbability());
//        toNode.save(fout);
//      }
//    }

//    public void save2(PrintWriter fout) {
//      if (fromNode == null) { return; }
//      fout.print(',');
//      fout.print(fromNode.data.getUnit().getId());
//      fout.print(',');
//      fout.print(data.getUnit().getId());
//      fout.print(',');
//      fout.print(data.getProcess());
//      fout.print(',');
//      fout.print(data.getProcessProbability());
//
//    }
//
  }

  /**
   * This constructor used only for purposes of Serialization.
   */
  public ProcessOccurrenceSpreading() {
    super();
    spreadQueue = new LinkedList();
    finished    = true;
    nodeCount   = 0;
  }
  /**
   * Overloaded constructor.  References Process Occurrence superclass.  
   * @param evu 
   * @param lifeform
   * @param processData
   * @param timeStep
   */
  public ProcessOccurrenceSpreading(Evu evu, Lifeform lifeform,
                                    ProcessProbability processData, int timeStep) {
    super(evu,lifeform,processData,timeStep);
    init();
  }
  /**
   * initializes the event occurrence.  points to the root graph node, the data inside it, and initializes the spreadQueue which will start at the root.  
   * Then it initializes the node count to 1, the elevation to 0 and finished to false.    
   */
  protected void init() {
    eventAcres        = unit.getAcres();
//    nodeLookup        = new HashMap();
    root              = new Node();
    root.data         = this;
    root.relElevation = 0;

//    nodeLookup.put(unit,root);
    spreadQueue = new LinkedList();
    spreadQueue.add(root);
    finished = false;
    nodeCount = 1;
  }
/**
 * 
 * @return true if finished
 */
  public boolean isFinished() { return finished; }
/**
 * 
 * @param fromUnit evu where the process is coming from
 * @param toUnit evu where the process is spreading to
 * @return either 1 for above, 0 for next to, or -1 for below, by default returns 0
 */
  protected int determineElevation(Evu fromUnit, Evu toUnit) {
    char position = fromUnit.getAdjPosition(toUnit);

    if (position == Evu.ABOVE) {
      return 1;
    }
    else if (position == Evu.NEXT_TO) {
      return 0;
    }
    else if (position == Evu.BELOW) {
      return -1;
    }
    return 0;
  }
//  public void save(PrintWriter fout) {
// //   root.save(fout);
//    Iterator it = nodeLookup.values().iterator();
//    Node     node;
//
//    while (it.hasNext()) {
//      node = (Node)it.next();
//      node.save2(fout);
//    }
//  }

  public int getEventAcres() { return eventAcres; }
/**
 * method to make the get process occurrence graph into an array
 * @return an array of process occurrences
 */
  public ProcessOccurrence[] getProcessOccurrences() {
    if (root == null) { return null; }
    LinkedList queue= new LinkedList();
    ProcessOccurrence[] items = new ProcessOccurrence[nodeCount];
    ArrayList           visited = new ArrayList(nodeCount);

    queue.add(root);
    Node node;
    int itemsIndex=0;
    while (queue.size() > 0) {
      node = (Node)queue.removeFirst();
      if (visited.contains(node)) { continue; }
      visited.add(node);
      items[itemsIndex] = node.data;
      itemsIndex++;
      if (node.toNodes == null) { continue; }
      for (int i=0; i<node.toNodes.length; i++) {
        queue.add(node.toNodes[i]);
      }
    }
    return items;
  }

//  public void addSpreadEvent(Evu fromUnit, Evu toUnit) {
//    int timeStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
//    addSpreadEvent(fromUnit,toUnit,toUnit.getProcessType(timeStep),toUnit.getProb(timeStep),timeStep);
//  }
//  public void addSpreadEvent(Evu fromUnit, Evu toUnit, int timeStep) {
//    addSpreadEvent(fromUnit,toUnit,toUnit.getProcessType(timeStep),toUnit.getProb(timeStep),timeStep);
//  }
  public void addLegacySpreadEvent(Evu fromUnit, Evu toUnit, ProcessType process, int timeStep) {
    addLegacySpreadEvent(fromUnit,toUnit,process,toUnit.getState(timeStep).getProb(),timeStep);
  }
  /**
   * This particular function is used to build the process event tree from data
   * read in from old area simulation files.
   * @param fromUnit Evu
   * @param toUnit Evu
   * @param process ProcessType
   * @param processProb int
   * @param timeStep int
   */
  public void addLegacySpreadEvent(Evu fromUnit, Evu toUnit, ProcessType process,
                             int processProb, int timeStep) {
    if (tmpNodeLookup == null) { tmpNodeLookup = new HashMap(); }
    if (tmpToNodesHm == null) { tmpToNodesHm = new HashMap(); }

    Node fromNode;
    if (root.data.unit == fromUnit) {
      fromNode = root;
      tmpNodeLookup.put(fromUnit,root);
    }
    else {
      fromNode = (Node) tmpNodeLookup.get(fromUnit);
      // This is necessary because information is not saved in order so the
      // from node might not exist yet.
      if (fromNode == null) {
        fromNode = new Node();
        tmpNodeLookup.put(fromUnit,fromNode);
      }
    }
    Node toNode = (Node) tmpNodeLookup.get(toUnit);
    if (toNode == null) {
      toNode = new Node();
      tmpNodeLookup.put(toUnit, toNode);
    }
    toNode.fromNode = (fromNode == root ? null : fromNode);
    ProcessProbability processData = new ProcessProbability(process,processProb);
    toNode.data = new ProcessOccurrence(toUnit, Lifeform.NA, processData, timeStep);
    toNode.relElevation = determineElevation(fromUnit, toUnit) +
        fromNode.relElevation;
    LinkedList list = (LinkedList)tmpToNodesHm.get(fromNode);
    if (list == null) {
      list = new LinkedList();
      tmpToNodesHm.put(fromNode,list);
    }
    list.add(toNode);

    eventAcres += toUnit.getAcres();
  }
  /**
   * 
   */
  public void finishedAddingLegacySpreadEvents() {
    for (Iterator i=tmpToNodesHm.keySet().iterator(); i.hasNext(); ) {
      Node fromNode = (Node)i.next();
      LinkedList list = (LinkedList)tmpToNodesHm.get(fromNode);
      if (list == null) { continue; }

      fromNode.setToNodes((Node[])list.toArray(new Node[list.size()]));
    }
    tmpToNodesHm.clear();
  }
  public static void finishedLoadingLegacyFiles() {
    tmpToNodesHm  = null;
    tmpNodeLookup = null;
  }

  public void addSpreadEvent(Node fromNode, ArrayList toUnits, Lifeform lifeform) {
    Node[] toNodes = new Node[toUnits.size()];

    AreaSummary areaSummary = Simpplle.getAreaSummary();
    Evu unit;
    int tStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    for (int i=0; i<toNodes.length; i++) {
      unit = (Evu)toUnits.get(i);
      toNodes[i] = new Node();
      toNodes[i].fromNode = fromNode;
      if (process.isFireProcess()) {
        Lifeform newLife = unit.getDominantLifeformFire();
        if (newLife != null) { lifeform = newLife; }
      }

      VegSimStateData state = unit.getState(tStep,lifeform);
      ProcessProbability processData =
          new ProcessProbability(state.getProcess(),state.getProb());
      toNodes[i].data = new ProcessOccurrence(unit, lifeform,processData, tStep);
      eventAcres += unit.getAcres();
      spreadQueue.add(toNodes[i]);
      areaSummary.addProcessEvent(tStep,unit.getId(),this);
    }
    fromNode.setToNodes(toNodes);
  }

//  public void setThread(Thread t) { thread = t; }
//  public Thread getThread() { return thread; }
  /**
   * Pop a node off the queue and spread try to spread to its adjacent units,
   * then return to allow spreading of other events to occur.
   *
   * BB-DISEASE only spread from origin to neighbors and stops.
   */
  public void doSpread() {
    Node           spreadingNode;
    AdjacentData[] adjData;
    Evu            fromUnit, toUnit;

//    while (spreadQueue.size() > 0) {
    if (spreadQueue.size() == 0) {
      finished = true;
      return;
    }
    tmpToUnits.clear();

    spreadingNode = (Node) spreadQueue.removeFirst();

    fromUnit = spreadingNode.data.getUnit();

    // If fire overtakes the unit make sure we don't spread.
    if (fromUnit.getState(Simulation.getCurrentTimeStep(),lifeform).getProcess() != process) {
      spreadQueue.clear();
      finished = true;
      return;
    }

    adjData = fromUnit.getAdjacentData();
    if (adjData != null) {
      for (int i = 0; i < adjData.length; i++) {
        toUnit = adjData[i].evu;
        if (toUnit.hasLifeform(lifeform) == false) { continue; }
        if (Evu.doSpread(fromUnit, toUnit, lifeform)) {

//          addSpreadEvent(fromUnit, toUnit);
//          spreadQueue.add( (Node) nodeLookup.get(toUnit));
        }
      }
      addSpreadEvent(spreadingNode,tmpToUnits,lifeform);
    }
    finished = (spreadQueue.size() == 0);
//      thread.yield();  // Give other thread a chance to run.

//    }
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    eventAcres = in.readInt();
    int size  = in.readInt();
    if (tmpNodeLookup == null) {
      tmpNodeLookup = new HashMap(size);
    }
    else {
      tmpNodeLookup.clear();
    }
    for (int i=0; i<size; i++) {
      (new Node()).readExternal(in);
    }
    nodeCount = size;
    tmpNodeLookup.clear();
  }
/**
 * method to print out process occurrence graph.  
 */
  public void printTree() {
    System.out.println("-------------------------PRINT TREE--------------------------------------------");
    if (root == null) { return;  }
    System.out.println(root.data.getUnit().getId());
    LinkedList queue= new LinkedList();
    ArrayList  visited = new ArrayList(nodeCount);

    queue.add(root);
    Node node;
    while (queue.size() > 0) {
      node = (Node)queue.removeFirst();
      if (visited.contains(node)) { continue; }
      visited.add(node);
      node.printMe();

      if (node.toNodes == null) { continue; }
      for (int i=0; i<node.toNodes.length; i++) {
        queue.add(node.toNodes[i]);
      }
    }
    System.out.println("--------------------------END PRINT TREE---------------------------------------");
  }

  public void writeEventAccessFiles(PrintWriter fout, int run, int timeStep) throws SimpplleError {
    //Fields
    //"RUN,TIMESTEP,ORIGINUNITID,UNITID,TOUNITID,PROCESS_ID,RATIONALPROB,RATIONALACRES,SEASON_ID,GROUP_ID,OWNERSHIP_ID,SPECIAL_AREA_ID,FMZ_ID"
    LinkedList queue= new LinkedList();
    ArrayList  visited = new ArrayList(nodeCount);

    int rootId = getUnit().getId();
    Simulation sim = Simulation.getInstance();

    queue.add(root);
    Node node;
    while (queue.size() > 0) {
      node = (Node)queue.removeFirst();
      if (visited.contains(node)) { continue; }
      visited.add(node);

      
      Evu evu = node.data.getUnit();

      sim.addAccessProcess(node.data.getProcess());
      sim.addAccessEcoGroup(evu.getHabitatTypeGroup().getType());
      sim.addAccessOwnership(Ownership.get(evu.getOwnership(),true));
      sim.addAccessSpecialArea(SpecialArea.get(evu.getSpecialArea(),true));
      sim.addAccessFmz(evu.getFmz());

      int processId = node.data.getProcess().getSimId();
      int prob      = node.data.getProcessProbability();
      int seasonId  = node.data.getSeason().ordinal();
      int groupId   = evu.getHabitatTypeGroup().getType().getSimId();
      int ownerId   = Ownership.get(evu.getOwnership(),true).getSimId();
      int specialId = SpecialArea.get(evu.getSpecialArea(),true).getSimId();
      int fmzId     = evu.getFmz().getSimId();
      float acres   = evu.getFloatAcres();

      float fProb    = (prob < 0) ? prob : ( (float)prob / (float)Utility.pow(10,Area.getAcresPrecision()) );

      // ** Work Section **
      if (node.toNodes == null || node.toNodes.length == 0) {
        fout.printf("%d,%d,%d,%d,",run,timeStep,rootId,node.data.getUnit().getId());        
        fout.print("-1,");
        fout.printf("%d,%.1f,%.1f,%d,%d,%d,%d,%d%n", processId,fProb,acres,seasonId,groupId,ownerId,specialId,fmzId);
      }
      else {
        for (int i = 0; i < node.toNodes.length; i++) {
          fout.printf("%d,%d,%d,%d,",run,timeStep,rootId,node.data.getUnit().getId());
          fout.printf("%d,",node.toNodes[i].data.getUnit().getId());
          fout.printf("%d,%.1f,%.1f,%d,%d,%d,%d,%d%n", processId,fProb,acres,seasonId,groupId,ownerId,specialId,fmzId);
          queue.add(node.toNodes[i]);
        }
      }
      
      // ** End Work **
    }

  }
  public void writeEventDatabase(Session session, int run, int timeStep)
      throws SimpplleError
  {
    LinkedList queue= new LinkedList();
    ArrayList  visited = new ArrayList(nodeCount);

    int rootId = getUnit().getId();

    queue.add(root);
    Node node;
    while (queue.size() > 0) {
      node = (Node)queue.removeFirst();
      if (visited.contains(node)) { continue; }
      visited.add(node);

      // ** Work Section **
      AreaSummaryDataNew data = new AreaSummaryDataNew();
      data.setRun((short)run);
      data.setTimeStep((short)timeStep);
      data.setOriginUnitId(rootId);
      data.setUnitId(node.data.getUnit().getId());
      data.setProcess(node.data.getProcess());
      int prob = node.data.getProcessProbability();
      data.setRationalProb((short)prob);
      data.setSeason(node.data.getSeason());

      Evu evu = node.data.getUnit();
      data.setFmz(evu.getFmz());
      data.setSpecialArea(SpecialArea.get(evu.getSpecialArea(),true));
      data.setOwnership(Ownership.get(evu.getOwnership(),true));
      data.setGroup(evu.getHabitatTypeGroup().getType());
      data.setRationalAcres(evu.getAcres());

      if (node.toNodes == null || node.toNodes.length == 0) {
        data.setToUnitId(-1);
        AreaSummaryDataNew.writeDatabase(session,data);
      }
      else {
        for (int i = 0; i < node.toNodes.length; i++) {
          AreaSummaryDataNew dataNew = new AreaSummaryDataNew(data);
          dataNew.setToUnitId(node.toNodes[i].data.getUnit().getId());
          AreaSummaryDataNew.writeDatabase(session,dataNew);
          queue.add(node.toNodes[i]);
        }
      }
      // ** End Work **
    }
  }

//  public void destroyMe() {
//    if (tmpNodeLookup != null) { tmpNodeLookup.clear();  tmpNodeLookup = null;}
//    if (tmpToNodesHm != null) { tmpToNodesHm.clear(); tmpToNodesHm = null;}
//    if (tmpToUnits != null) { tmpToUnits.clear(); tmpToUnits = null;}
//    if (spreadQueue != null) { spreadQueue.clear(); spreadQueue = null;}
//
//    if (root == null) { return;  }
//    LinkedList queue= new LinkedList();
//    ArrayList  visited = new ArrayList(nodeCount);
//
//    queue.add(root);
//    root = null;
//    Node node;
//    while (queue.size() > 0) {
//      node = (Node)queue.removeFirst();
//      if (visited.contains(node)) { continue; }
//      visited.add(node);
//
//      node.fromNode = null;
//      node.data.destoryMe();
//      node.data = null;
//
//      if (node.toNodes == null) { continue; }
//      for (int i=0; i<node.toNodes.length; i++) {
//        queue.add(node.toNodes[i]);
//        node.toNodes[i] = null;
//      }
//      node.toNodes = null;
//    }
//    queue.clear();
//    queue = null;
//  }
  private void writeTree(ObjectOutput out) throws IOException {
    if (root == null) { return;  }
    LinkedList queue= new LinkedList();
    ArrayList  visited = new ArrayList(nodeCount);

    queue.add(root);
    Node node;
    while (queue.size() > 0) {
      node = (Node)queue.removeFirst();
      if (visited.contains(node)) { continue; }
      visited.add(node);
      node.writeExternal(out);

      if (node.toNodes == null) { continue; }
      for (int i=0; i<node.toNodes.length; i++) {
        queue.add(node.toNodes[i]);
      }
    }
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeInt(eventAcres);
    out.writeInt(nodeCount);
    writeTree(out);
  }

//  public void run() {
//    doSpread();
//    ((MyThreadGroup)thread.getThreadGroup()).decThreadCount();
//  }
}




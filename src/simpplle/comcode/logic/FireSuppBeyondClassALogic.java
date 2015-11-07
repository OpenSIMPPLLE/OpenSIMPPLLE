package simpplle.comcode.logic;

import simpplle.comcode.*;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppersion Beyond Class A Logic, a type of Base Logic.  Determining whether a fire stays at class A or goes beyond is an
 * important factor in Fire logic defined below
 *
 * Fire Process logic
 * determine all process probabilities for each evu ->use probabilities to select process
 * if selected process is fire event->if fire suppresssion ->determine probability of staying class size A due to weather or fire suppression → if yes change process for evu to succession and record a class A fire with suppression costs
 * if not suppressed at Class A level → determine type of fire and fire spread → at end of simulation calculate fire suppression costs and emissions

 * if selected process is fire and fire suppression is no, determine probability of staying class A size due to weather → if it spreads beyond class A size determine type of firefighter and fire → at end of simulation calculate emissions

 * if stays at class A size due to weather->change process for evu to succession and record class A fire
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *  
 * @see BaseLogic
 * 
 */
public class FireSuppBeyondClassALogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { FIRE_SUPP_BEYOND_CLASS_A };

  public static final Kinds  FIRE_SUPP_BEYOND_CLASS_A = Kinds.FIRE_SUPP_BEYOND_CLASS_A;

  public static final int FIRE_TYPE_COL   = BaseLogic.LAST_COL+1;
  public static final int SPREAD_KIND_COL = BaseLogic.LAST_COL+2;
  public static final int SUPPRESS_COL    = BaseLogic.LAST_COL+3;
  public static final int PROB_COL        = BaseLogic.LAST_COL+4;

  private static FireSuppBeyondClassALogic instance;
  public static void initialize() {
    instance = new FireSuppBeyondClassALogic();
  }
  public static FireSuppBeyondClassALogic getInstance() { return instance; }
/**
 * Constructor for Fire Suppression Beyond class A logic class, inherits from 
 */
  public FireSuppBeyondClassALogic() {
    super(new String[] { "FIRE_SUPP_BEYOND_CLASS_A" });
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_BEYOND_CLASS_A_LOGIC;

    addColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),"FIRE_TYPE_COL");
    addColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),"SPREAD_KIND_COL");
    addColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),"SUPPRESS_COL");
    addColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),"PROB_COL");

    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),ROW_COL);
    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),SIZE_CLASS_COL);

    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),OWNERSHIP_COL);

    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),FIRE_TYPE_COL);
    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),SPREAD_KIND_COL);
    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),ROAD_STATUS_COL);
    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),SUPPRESS_COL);
    addVisibleColumn(FIRE_SUPP_BEYOND_CLASS_A.toString(),PROB_COL);
  }

  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new FireSuppBeyondClassALogicData();
    super.addRow(insertPos,kind,logicData);
  }

  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }
/**
 * gets column name.  columns designated in this class are Suppress, Spread kind, Fire type, and Probability
 * else references Base Logic superclass
 */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Suppress")) {
      return SUPPRESS_COL;
    }
    else if (name.equalsIgnoreCase("Spread Kind")) {
      return SPREAD_KIND_COL;
    }
    else if (name.equalsIgnoreCase("Fire Type")) {
      return FIRE_TYPE_COL;
    }
    else if (name.equalsIgnoreCase("Prob")) {
      return PROB_COL;
    }
     else {
      return super.getColumnNumFromName(name);
    }
  }
  /**
   * returns string literal version of column name: choices defined in this class are Fire Type, Spread Kind, Suppress, and Probability
   * else references Base Logic superclass
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case FIRE_TYPE_COL:   return "Fire Type";
      case SPREAD_KIND_COL: return "Spread Kind";
      case SUPPRESS_COL:    return "Suppress";
      case PROB_COL:        return "Prob";
      default:
        return super.getColumnName(col);
    }
  }
/**
 *Method to calculate if fire is suppressed 
 * @param vegType vegetative type being evaluated
 * @param processType
 * @param fromEvu originating evu
 * @param evu designation evu
 * @param tStep time step
 * @param lifeform
 * @return true if suppressed, false otherwise
 */
  public boolean isSuppressed(VegetativeType vegType, ProcessType processType,
                              simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu,
                              int tStep, Lifeform lifeform)
  {
    
    if (!simpplle.comcode.process.FireEvent.currentEvent.isSuppressed()) {
      return false;
    }
    
    ArrayList<AbstractLogicData> dataList = getData(FIRE_SUPP_BEYOND_CLASS_A.toString());
    if (dataList == null) { return false; }

    boolean isExtreme = Simpplle.getCurrentArea().extremeFireEvent(fromEvu);

    for (int i=0; i<dataList.size(); i++) {
      FireSuppBeyondClassALogicData logicData = (FireSuppBeyondClassALogicData)dataList.get(i);
      if (logicData.isMatch(processType,isExtreme,evu,tStep,lifeform,vegType)) {
        boolean suppressed = logicData.isSuppressed();
        
        if (suppressed && Simulation.getInstance().isDoSimLoggingFile()) {
          PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
          logOut.printf("Time: %d, Spread From: %d, To: %d, To Life: %s, Type: %s, Beyond Class A Suppression%n",
              tStep, fromEvu.getId(), evu.getId(), lifeform.toString(), processType.toString());
        }
      
        
        return suppressed;
      }
    }
    return false;

  }
  public boolean isSuppressedUniform(ProcessOccurrenceSpreadingFire event, VegetativeType vegType, ProcessType processType,
                                     boolean isExtreme, simpplle.comcode.element.Evu evu, int tStep, Lifeform lifeform)
  {

    if (!event.isSuppressed()) {
      return false;
    }

    ArrayList<AbstractLogicData> dataList = getData(FIRE_SUPP_BEYOND_CLASS_A.toString());
    if (dataList == null) { return false; }

    for (int i=0; i<dataList.size(); i++) {
      FireSuppBeyondClassALogicData logicData = (FireSuppBeyondClassALogicData)dataList.get(i);
      if (logicData.isMatch(processType,isExtreme,evu,tStep,lifeform,vegType)) {
        boolean suppressed = logicData.isSuppressed();

        return suppressed;
      }
    }
    return false;

  }

}



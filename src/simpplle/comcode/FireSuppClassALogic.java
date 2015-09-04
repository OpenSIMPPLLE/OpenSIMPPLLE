package simpplle.comcode;

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
 * <p>This class defines Fire Suppression Beyond Class A Logic Data, a type of Logic Data.
 *
 * Fire Process logic
 * determine all process probabilities for each evu ->use probabilities to select process
 * if selected process is fire event->if fire suppresssion ->determine probability of staying class size A due to weather or fire suppression → if yes change process for evu to succession and record a class A fire with suppression costs
 * if not suppressed at Class A level → determine type of fire and fire spread → at end of simulation calculate fire suppression costs and emissions
 *
 * if selected process is fire and fire suppression is no, determine probability of staying class A size due to weather → if it spreads beyond class A size determine type of firefighter and fire → at end of simulation calculate emissions
 *
 * if stays at class A size due to weather->change process for evu to succession and record class A fire
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 * @see simpplle.comcode.LogicData
 * 
 */
public class FireSuppClassALogic extends BaseLogic {
  static final int  version = 1;

  public enum Kinds { FIRE_SUPP_CLASS_A };

  public static final Kinds  FIRE_SUPP_CLASS_A = Kinds.FIRE_SUPP_CLASS_A;

  public static final int PROB_COL = BaseLogic.LAST_COL+1;

  private static FireSuppClassALogic instance;
  /**
   * Creates a new FireSuppClassALogic object.  
   */
  public static void initialize() {
    instance = new FireSuppClassALogic();
  }
  /**
   * Gets the instance of FireSuppClassALogic object.  
   * @return
   */
  public static FireSuppClassALogic getInstance() { return instance; }
/**
 * Constructor for Fire Suppression Class A Logic, a type of Base Logic.  Inherits from the Base Logic superclass, and initializes 
 * system knowledge kind to Fire Suppression Class A Logic, then adds columns for probability, row, size class, road status, density, process, 
 * treatment, moisture. 
 * Note: there are two probability columns, one is visible the other not visible.
 */
  public FireSuppClassALogic() {
    super(new String[] { "FIRE_SUPP_CLASS_A" });
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_CLASS_A_LOGIC;

    addColumn(FIRE_SUPP_CLASS_A.toString(),"PROB_COL");

    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),ROW_COL);
    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),SIZE_CLASS_COL);
    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),ROAD_STATUS_COL);

    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),DENSITY_COL);
    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),PROCESS_COL);
    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),TREATMENT_COL);
    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),MOISTURE_COL);

    addVisibleColumn(FIRE_SUPP_CLASS_A.toString(),PROB_COL);
  }

  public void addRow(int insertPos, String kind) {
    AbstractLogicData logicData = new FireSuppClassALogicData();
    super.addRow(insertPos,kind,logicData);
  }

  public void duplicateRow(int row, int insertPos, String kind) {
    AbstractLogicData logicData = getValueAt(row,kind);
    super.addRow(insertPos,kind,logicData.duplicate());
  }
/**
 * gets column Id  using string literal "Probability" or references the superclass to get the column Id.
 */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Probability")) {
      return PROB_COL;
    }
     else {
      return super.getColumnNumFromName(name);
    }
  }
  /**
   * gets the column from visible column hashmap
   */
  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    switch (col) {
      case PROB_COL:        return "Probability";
      default:
        return super.getColumnName(col);
    }
  }
/**
 * calculates whether a fire is suppressed or not
 * @param vegType vegetative type being evaluated
 * @param evu ecological vegetative unit being evaluated
 * @param tStep time step
 * @param lifeform
 * @return true if suppressed
 */
  public boolean isSuppressed(VegetativeType vegType, Evu evu,
                              int tStep, Lifeform lifeform)
  {
    int random = Simulation.getInstance().random();
    if (!FireSuppEventLogic.getInstance().isSuppressed(evu,random)) {
      return false;
    }
    
    ArrayList<AbstractLogicData> dataList = getData(FIRE_SUPP_CLASS_A.toString());
    if (dataList == null) { return false; }

    for (int i=0; i<dataList.size(); i++) {
      FireSuppClassALogicData logicData = (FireSuppClassALogicData)dataList.get(i);
      if (logicData.isMatch(evu,tStep,lifeform,vegType)) {

        if (Simulation.getInstance().isDoSimLoggingFile()) {
          PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
          logOut.printf("Time: %d, Unit: %d, Life: %s, Class A Suppression%n",
              tStep, evu.getId(), lifeform.toString());
        }
      
        return logicData.isSuppressed();
      }
    }
    return false;

  }

}



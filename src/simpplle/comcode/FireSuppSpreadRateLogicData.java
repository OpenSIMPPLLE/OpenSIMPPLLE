package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppression Spread Rate Logic Data, a type of Logic Data
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.BaseLogic
 * 
 */
public class FireSuppSpreadRateLogicData extends LogicData {
  static final long serialVersionUID = -6423043959321101472L;
  static final int  version          = 1;

  private float slope;
  private int   averageRate;
  private int   extremeRate;
/**
 * Constructor for Fire suppression Spread Rate Logic Data.  Initializes the slope, averageRate, extremeRate to 0 and the system knowledge
 * to FIRE_SUPP_SPREAD_RATE_LOGIC
 */
  public FireSuppSpreadRateLogicData() {
    super();

    slope       = 0.0f;
    averageRate = 0;
    extremeRate = 0;

    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_SPREAD_RATE_LOGIC;
  }
/**
 * Duplicates this Fire suppression Spread Rate Logic Data object, by creating a new Fire suppression Spread Rate Logic Data object and transfering 
 * all the info from this particular Fire suppression Spread Rate Logic Data object.  THen returns it.  
 */
  public AbstractLogicData duplicate() {
    FireSuppSpreadRateLogicData logicData = new FireSuppSpreadRateLogicData();
    super.duplicate(logicData);

    logicData.slope       = slope;
    logicData.averageRate = averageRate;
    logicData.extremeRate = extremeRate;

    return logicData;
  }
/**
 * Used in GUI table model.  Gets the value of a variable for slope, averageRate, extremeRate by column Id.  
 * Choices are SLOPE_COL (16) (Float), AVERAGE_RATE_COL(17) (Integer), or EXTREME_RATE_COL(18)(Integer).
 */
  public Object getValueAt(int col) {
    switch (col) {
      case FireSuppSpreadRateLogic.SLOPE_COL:        return slope;
      case FireSuppSpreadRateLogic.AVERAGE_RATE_COL: return averageRate;
      case FireSuppSpreadRateLogic.EXTREME_RATE_COL: return extremeRate;
      default: return super.getValueAt(col);
    }
  }
  /**
   * Sets the Object value of Fire Suppression Spread Rate Logic Data by column Id. 
   * Choices are SLOPE_COL (16) - Float, AVERAGE_RATE_COL(17) - Integer, or EXTREME_RATE_COL(18) - Integer. 
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireSuppSpreadRateLogic.SLOPE_COL:
      {
        slope = (Float)value;
        markChanged();
        break;
      }
      case FireSuppSpreadRateLogic.AVERAGE_RATE_COL:
      {
        int newValue = (Integer) value;
        if (averageRate != newValue) {
          averageRate = newValue;
          markChanged();
        }
        break;
      }
      case FireSuppSpreadRateLogic.EXTREME_RATE_COL:
      {
        int newValue = (Integer) value;
        if (extremeRate != newValue) {
          extremeRate = newValue;
          markChanged();
        }
        break;
      }
      default:
        super.setValueAt(col,value);
    }
  }

  public boolean isMatch(Evu evu,
                         Integer tStep,Lifeform lifeform,
                         VegetativeType vegType)
  {
    FireResistance resistance =
      FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);

    if (super.isMatch(resistance,evu,tStep,lifeform,vegType) == false) {
      return false;
    }

    float evuSlope = evu.getSlope();
    if (evuSlope < slope) {
      return false;
    }

    return true;
  }
/**
 * Reads from external source the Fire Suppression Spread Rate Logic Data in following order slope, averageRate, extremeRate.  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    slope       = in.readFloat();
    averageRate = in.readInt();
    extremeRate = in.readInt();
  }
  /**
   * Writes to external source this Fire Suppression Spread Rate Logic Data in following order slope, averageRate, extremeRate.  
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeFloat(slope);
    out.writeInt(averageRate);
    out.writeInt(extremeRate);
  }
/**
 * Gets rate.  If boolean isExtreme is true, returns the extreme rate, otherwise returns averageRate
 * @param isExtreme true fire is extreme.
 * @return If boolean isExtreme is true, returns the extreme rate, otherwise returns averageRate
 */
  public int getRate(boolean isExtreme) {
    return (isExtreme ? extremeRate : averageRate);
  }
/**
 * Sets average spread rate for this Fire Suppression Spread Rate Logic Data object
 * @param averageRate the average spread rate
 */
  public void setAverageRate(int averageRate) {
    this.averageRate = averageRate;
  }
/**
 * Sets extreme spread rate for this Fire Suppression Spread Rate Logic Data object
 * @param extremeRate the extreme spread rate
 */
  public void setExtremeRate(int extremeRate) {
    this.extremeRate = extremeRate;
  }
/**
 * Sets the slope of this Fire Suppression Spread Rate Logic Data object
 * @param slope the slope
 */
  public void setSlope(float slope) {
    this.slope = slope;
  }

}

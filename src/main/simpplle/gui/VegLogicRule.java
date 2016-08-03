/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.util.ArrayList;
import simpplle.comcode.Species;
import simpplle.comcode.SizeClass;
import simpplle.comcode.Density;
import simpplle.comcode.HabitatTypeGroupType;
import simpplle.comcode.VegetativeType;

/** 
 * This class sets up Vegetative Logic Rule, a type of Base Logic Panel, which inherits directly from JPanel.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class VegLogicRule {
  private ArrayList speciesList=null;
  private ArrayList sizeClassList=null;
  private ArrayList densityList=null;
  private ArrayList htGroupList=null;

  // (e.g. speciesList = [DF, LP], sizeClassList=[MEDIUM, MTS], speciesListOR = true;
  //     [DF, LP] OR [MEDIUM, MTS]
  private boolean sizeClassListOR=true;
  private boolean densityListOR=true;
  private boolean htGroupListOR=true;

  // (e.g if list is (DF, LP) & speciesListNot is true is means:
  //        !(DF || LP) or (species != DF && species != LP)
  private boolean speciesListNot=false;
  private boolean sizeClassListNot=false;
  private boolean densityListNot=false;
  private boolean htGroupListNot=false;

  public VegLogicRule() {
  }
/**
 * Basically this method returns a boolean which evaluates for the presence of vegetative and habitat type gropus. While this method returns a boolean, it's main goal is to create a boolean Arraylist with 4-7 booleans that designate whether 
 * there is a species, size class, density, habitat group.  There are rules which govern the placement of booleans within the arraylist.  See comments in code.  
 *First checks the vegetative type has a species, size class, density and 
 * habitat group, then goes through a series of steps to calculate boolean value of evaluate. 
 * @param vegType The vegetative type being evalutated
 * @param htGroup the habitat group being evaluated
 * @return 
 */
  public boolean evaluate(VegetativeType vegType, HabitatTypeGroupType htGroup) {
    Species   species   = vegType.getSpecies();
    SizeClass sizeClass = vegType.getSizeClass();
    Density   density   = vegType.getDensity();

    boolean haveSpecies   = (speciesList   != null && speciesList.size()   > 0);
    boolean haveSizeClass = (sizeClassList != null && sizeClassList.size() > 0);
    boolean haveDensity   = (densityList   != null && densityList.size()   > 0);
    boolean haveHtGroup   = (htGroupList   != null && htGroupList.size()   > 0);

    ArrayList evalList = new ArrayList(7);

    if (haveSpecies) {
      boolean speciesResult = speciesList.contains(species);
      if (speciesListNot) { speciesResult = !speciesResult; }
      evalList.add(Boolean.valueOf(speciesResult));
    }
//if has a size class checks if size class list contains size class.  If size class not is true changes the size class result.  If has species adds true to arraylist.  Then adds size class resultIf have species adds 
    if (haveSizeClass) {
      boolean sizeClassResult = sizeClassList.contains(sizeClass);
      if (sizeClassListNot) { sizeClassResult = !sizeClassResult; }

      if (haveSpecies) {
        evalList.add(Boolean.valueOf(sizeClassListOR));
      }
      evalList.add(Boolean.valueOf(sizeClassResult));
    }
// if there is a density, checks if the density list contains that density.  Then checks if densityListNot is true.  Which it should not be since by default it is false.  
    //then adds true if there is a species or size class and finally adds the density result.  
    if (haveDensity) {
      boolean densityResult = densityList.contains(density);
      if (densityListNot) { densityResult = !densityResult; }

      if (haveSpecies || haveSizeClass) {
        evalList.add(Boolean.valueOf(densityListOR));
      }
      evalList.add(Boolean.valueOf(densityResult));
    }
// If there is a habitat group checks to see if the habitat group list contains the habitat.  If htGroupListNot is true negates the result of the habitat group check.  
    //This is set to false by default.  If there is a species, size class, or density places a true boolean in arraylist, then puts the result of habitat group check in arraylist.
    if (haveHtGroup) {
      boolean htGroupResult = htGroupList.contains(htGroup);
      if (htGroupListNot) { htGroupResult = !htGroupResult; }

      if (haveSpecies || haveSizeClass || haveDensity) {
        evalList.add(new Boolean(densityListOR));
      }
      evalList.add(new Boolean(htGroupResult));
    }

    if (evalList.size() == 0) { return false; }

    boolean value, ORbool;
    //result of species check
    boolean result = ((Boolean)evalList.get(0)).booleanValue();
//result == species boolean.  
    //Species or size class and size class OR 
    //
    for (int i=1; i<evalList.size(); i+=2) {
      ORbool = ((Boolean)evalList.get(i)).booleanValue();
      value = ((Boolean)evalList.get(i+1)).booleanValue();
      if (evalList.size() == 1) { return value; }

      result = (result || value) &&
               (ORbool || (result && value));
    }
    return result;
  }

}




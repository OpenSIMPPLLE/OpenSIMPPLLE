package simpplle.comcode;

import java.util.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class is one of the OpenSIMPPLLE's aquatic types.  
 * It represents the status of temperature(TEMP), stream bank stability (STAB), 
 * large woody debris (LWD), and stream bed conditions(SUB).  With absence of initial inventory, all attributes were considered within the reference range of conditions (REF)
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class AquaticAttribute extends SimpplleType {
  private AquaticAttribute[] attributes;
  private String             printName;

  private static final String SLASH = PotentialAquaticState.SLASH;

  private static Hashtable allAttributeHt = new Hashtable(4);

  public static final AquaticAttribute LWD  = new AquaticAttribute("LWD",true);
  public static final AquaticAttribute STAB = new AquaticAttribute("STAB",true);
  public static final AquaticAttribute SUB  = new AquaticAttribute("SUB",true);
  public static final AquaticAttribute TEMP = new AquaticAttribute("TEMP",true);
  public static final AquaticAttribute REF  = new AquaticAttribute("REF",true);

  /**
   * Default constructor.  Sets attributes and name to null.  
   */
  public AquaticAttribute() {
    attributes = null;
    printName  = null;
  }
  /**
   * Overloaded constructor of Aquatic Attribute Class.  adds names to attribute hash table. 
   * @param attributePrintName name of aquatic attribute formatted for printing.  
   * @param valid true if attribute name is valid
   * @throws ParseError
   */
  public AquaticAttribute(String attributePrintName, boolean valid) {
    this.printName = attributePrintName;
    if (attributePrintName == null) {
      attributes = null;
      return;
    }

    if (valid) {
      allAttributeHt.put(printName,this);
    }

    simpplle.comcode.utility.StringTokenizerPlus strTok = new simpplle.comcode.utility.StringTokenizerPlus(printName,SLASH);

    int count = strTok.countTokens();
    if (count == 1) {
      attributes = null;
      return;
    }

    attributes = new AquaticAttribute[count];

    String str;
    for(int i=0; i<count; i++) {
      try {
        str = strTok.getToken().toUpperCase();
      }
      catch (ParseError err) {
        str = "UNKNOWN";
      }
      attributes[i] = get(str);
      if (attributes[i] == null) {
        attributes[i] = new AquaticAttribute(str);
      }
    }
    if (this.isValid()) {  // Check to see if each attribute is valid.
      allAttributeHt.put(printName,this);
    }
  }
  
  /**
   * Overloaded constructor which calls default constructor and passes attribute PrintName and boolean false (meaning name is invalid).  
   * Used if aquatic attribute name is not valid and should not be put in hash table.  
   * @param attributePrintName 
   */
  public AquaticAttribute(String attributePrintName) {
    this(attributePrintName,false);
  }
/**
 * Overrides the hashCode function in SimpplleType
 */
  public int hashCode() {
    return printName.hashCode();
  }
  /**
   * Method to check if AquaticAttribute object is equal to the current object.  Must cast from Object to AquaticAttribute object.  
   * @return false if name is null or no object, true if name equals passed in AquaticAttribute object
   */
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof AquaticAttribute) {
      if (printName == null || obj == null) { return false; }

      return printName.equals(((AquaticAttribute)obj).printName);
    }
    return false;
  }
  
  /**
   * method to compare the names of aquatic attributes
   * @return -1 if object is null, 0 if they are equal, 
   */
  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return printName.compareTo(o.toString());
  }
  public String toString() { return printName; }

  public static int count() { return allAttributeHt.size(); }
/**
 * 
 * @return true if there are no attributes and there is an aquatic attribute, otherwise false
 */
  public boolean isValid() {
    if (attributes == null) {
       return (AquaticAttribute.get(printName) != null);
    }
    for (int i=0; i<attributes.length; i++) {
      if (attributes[i].isValid() == false) { return false; }
    }
    return true;
  }
/**
 * 
 * @param attributeStr name of attribute toUpperCase.  Must be cast to AquaticAttribute object
 * @return AquaticAttribute object.  
 */
  public static AquaticAttribute get(String attributeStr) {
    return ( (AquaticAttribute)allAttributeHt.get(attributeStr.toUpperCase()) );
  }

  public void setPrintName(String printName) {
    this.printName = printName;
  }

  public String getPrintName() {
    return printName;
  }
}




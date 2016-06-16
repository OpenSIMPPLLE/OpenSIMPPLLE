package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class has methods to set the current state of OpenSIMPPLLE.  
 *It consists of a default constructor and some overloaded constructor which call the default.  
 *It also has a series of setters and overloaded setters to set both the overall state, and specific aspects of the state.
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class CurrentState {
  private Species   species;
  private SizeClass sizeClass;
  private int       age;
  private Density   density;

  /**
   * default constructor. initializes species, sizeClass, age, and density to default values 
   */
  public CurrentState() {
    species   = null;
    sizeClass = null;
    age       = -1;
    density   = null;
  }

  public int hashCode() {
    StringBuffer strBuf = new StringBuffer();
    strBuf.append(species.toString());
    strBuf.append(sizeClass.toString());
    strBuf.append(age);
    strBuf.append(density.toString());

    int hashCode = strBuf.toString().hashCode();
    strBuf = null;
    return hashCode;
  }

  /**
   * checks to see if passed in object is equal to current state by comparing species, sizeClass, age, and density.
   * because the equality is between a CurrentState object and an undefined object, the passed Object o must first be cast to a CurrentState 
   * @return true if equal to current state, false if passed object is not a CurrentState object or not equal to Current State
   */
  public boolean equals(Object o) {
    if (o instanceof CurrentState) {
      CurrentState state = (CurrentState)o;
      return (species.equals(state.species) &&
              sizeClass.equals(state.sizeClass) &&
              age == state.age &&
              density.equals(state.density));
    }
    else {
      return false;
    }
  }
  /**
   * overloaded constructor.  does not reference the default construct but passes to set state species, sizeClass, age, and density
   * @param species 
   * @param sizeClass
   * @param age
   * @param density
   */
  public CurrentState(Species species, SizeClass sizeClass, int age, Density density) {
    setState(species,sizeClass,age,density);
  }
/**
 * overloaded constructor.  references the default constructor but sets the age to 1
 * @param species
 * @param sizeClass
 * @param density
 */
  public CurrentState(Species species, SizeClass sizeClass, Density density) {
    this(species,sizeClass,1,density);
  }
/**
 * overloaded constructor references default constructor and sets the current state with the passed species, and sizeClass
 * @param species
 * @param sizeClass
 * @param age
 * @param density
 */
  public CurrentState(String species, String sizeClass, int age, Density density) {
    this(Species.get(species),SizeClass.get(sizeClass),age,density);
  }

  /**
   * overlaoded constructor references default constructor and sets the age to 1
   * @param species
   * @param sizeClass
   * @param density
   */
  public CurrentState(String species, String sizeClass, Density density) {
    this(species,sizeClass,1,density);
  }
/**
 * set state function sets the current state with passed in species, sizeClass, age, and density
 * @param species
 * @param sizeClass
 * @param age
 * @param density
 */
  public void setState(Species species, SizeClass sizeClass, int age, Density density) {
    this.species   = species;
    this.sizeClass = sizeClass;
    this.age       = age;
    this.density   = density;
  }
  /**
   * overloaded setState function sets the current year to 1
   * @param species
   * @param sizeClass
   * @param density
   */
  public void setState(Species species, SizeClass sizeClass, Density density) {
    setState(species,sizeClass,1,density);
  }
  
  /**
   * overloaded setState function sets the current state to the passed in species, sizeClass, age, and density by the string name of species and sizeClass
   * @param species
   * @param sizeClass
   * @param age
   * @param density
   */
  public void setState(String species, String sizeClass, int age, Density density) {
    setState(Species.get(species),SizeClass.get(sizeClass),age,density);
  }
  
  /**
   * overloaded setState function sets the age to 1
   * @param species
   * @param sizeClass
   * @param density
   */
  public void setState(String species, String sizeClass, Density density) {
    setState(species,sizeClass,1,density);
  }

  
  public void setSpecies(String newSpecies) { species = Species.get(newSpecies); }
  public void setSpecies(Species newSpecies) { species = newSpecies; }
  public Species getSpecies() { return species; }

  public void setSizeClass(String newSizeClass) { sizeClass = SizeClass.get(newSizeClass); }
  public void setSizeClass(SizeClass newSizeClass) { sizeClass = newSizeClass; }
  public SizeClass getSizeClass() { return sizeClass; }

  public void setAge(int newAge) { age = newAge; }
  public int getAge() { return age; }

  public void setDensity(int newDensity) { density = Density.get(newDensity); }
  public void setDensity(Density newDensity) { density = newDensity; }
  public Density getDensity() { return density; }

  public String toString() {
    if (age == 1) {
      return species + "/" + sizeClass + "/" + density;
    }
    else {
      return species + "/" + sizeClass + Integer.toString(age) + "/" + density;
    }

  }

}



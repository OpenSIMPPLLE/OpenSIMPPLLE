package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.util.jar.JarInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;
import java.util.ArrayList;
import java.util.jar.JarOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;


/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This is an abstract class which creates methods to handle the Simpplle Types.  
 * Since it is abstract it cannot be instantiated directly, but is extended by other classes in OpenSimpplle. 
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */


public abstract class SimpplleType implements Comparable {
    private static final int version = 1;

    public enum Types {SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP};

    public enum TypesAqua { AQUATIC_CLASS, AQUATIC_ATTRIBUTE, AQUATIC_PROCESS,
        AQUATIC_TREATMENT, LTA_VS_GROUP};

    public static final Types SPECIES    = Types.SPECIES;
    public static final Types SIZE_CLASS = Types.SIZE_CLASS;
    public static final Types DENSITY    = Types.DENSITY;
    public static final Types PROCESS    = Types.PROCESS;
    public static final Types TREATMENT  = Types.TREATMENT;
    public static final Types HTGRP      = Types.GROUP;
    public static final Types GROUP      = Types.GROUP;
    public static final int MAX = Types.GROUP.ordinal() + 1;

    public static final TypesAqua AQUATIC_CLASS      = TypesAqua.AQUATIC_CLASS;
    public static final TypesAqua AQUATIC_ATTRIBUTE  = TypesAqua.AQUATIC_ATTRIBUTE;
    public static final TypesAqua AQUATIC_PROCESS    = TypesAqua.AQUATIC_PROCESS;
    public static final TypesAqua AQUATIC_TREATMENT  = TypesAqua.AQUATIC_TREATMENT;
    public static final TypesAqua LTA_VS_GROUP       = TypesAqua.LTA_VS_GROUP;
    public static final int MAX_AQUATIC = TypesAqua.LTA_VS_GROUP.ordinal() + 1;

    public abstract String toString();
    /**
     * If this GIS name is an instance of the Density class, prints underscores.
     * @return String of underscores.
     */

    public String getGISPrintName() {
        if (this instanceof Density) {
            return ((Density)this).getGisPrintName();
        }
        return Utility.dashesToUnderscores(toString());
    }

    public abstract boolean equals(Object obj);
    public abstract int hashCode();

    public abstract int compareTo(Object o);

    protected static ArrayList<SimpplleType> allSpeciesList = new ArrayList<SimpplleType>();
    protected static ArrayList<SimpplleType> allSizeClassList = new ArrayList<SimpplleType>();
    protected static ArrayList<SimpplleType> allDensityList = new ArrayList<SimpplleType>();
    protected static ArrayList<SimpplleType> allProcessList = new ArrayList<SimpplleType>();
    protected static ArrayList<SimpplleType> allTreatmentList = new ArrayList<SimpplleType>();
    protected static ArrayList<SimpplleType> allGroupList = new ArrayList<SimpplleType>();

    protected static HashMap<String,SimpplleType> allSpeciesHm = new HashMap<String,SimpplleType>();
    protected static HashMap<String,SimpplleType> allSizeClassHm = new HashMap<String,SimpplleType>();
    protected static HashMap<String,SimpplleType> allDensityHm = new HashMap<String,SimpplleType>();
    protected static HashMap<String,SimpplleType> allProcessHm = new HashMap<String,SimpplleType>();
    protected static HashMap<String,SimpplleType> allTreatmentHm = new HashMap<String,SimpplleType>();
    protected static HashMap<String,SimpplleType> allGroupHm = new HashMap<String,SimpplleType>();

    /**
     * Gets the simpplle type object by passed in kind (choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP) and the string type name)
     * @param kind
     * @param typeName
     * @return SimpplleType.  By default this returns null.
     * Otherwise will return the type of species, class size, density, process, treatment, or habitate group type.
     * for a description of these see the following classes:

     */
    public static SimpplleType get(Types kind, String typeName) {
        switch (kind) {
            case SPECIES:    return Species.get(typeName);
            case SIZE_CLASS: return SizeClass.get(typeName);
            case DENSITY:    return Density.get(typeName);
            case PROCESS:    return ProcessType.get(typeName);
            case TREATMENT:  return TreatmentType.get(typeName);
            case GROUP:      return HabitatTypeGroupType.get(typeName);
            default: return null;
        }
    }
    /**
     * Gets the Simpplle type string name.  This is used often in the GUI.  Choces are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
     * @param kind
     * @return
     */
    public static String getTypeName(Types kind) {
        switch(kind) {
            case PROCESS:    return "PROCESS";
            case SPECIES:    return "SPECIES";
            case SIZE_CLASS: return "SIZE-CLASS";
            case DENSITY:    return "DENSITY";
            case TREATMENT:  return "TREATMENT";
            case GROUP:      return "HTGRP";
            default: return null;
        }
    }

    /**
     * public int getKind()
     * <p>Replaced by getTypeID(String name)
     *
     *
     */
//  public int getKind() {
//    if (this instanceof Species) {
//      return SPECIES;
//    }
//    else if (this instanceof SizeClass) {
//      return SIZE_CLASS;
//    }
//    else if (this instanceof Density) {
//      return DENSITY;
//    }
//    else if (this instanceof ProcessType) {
//      return PROCESS;
//    }
//    else if (this instanceof TreatmentType) {
//      return TREATMENT;
//    }
//    else if (this instanceof HabitatTypeGroupType) {
//      return HTGRP;
//    }
//    else { return -1; }
//  }
    /**
     * Method to find the ordinal which represents the SimpplleType ID (place in Enum - indexed starting at 0 ("SPECIES"))
     * in Simpple Types enumeration.
     * @param name  Simpplle Type name
     * @return Ordinal number which represents the Simpplle Type ID.  Indexed starting at 0 by default.
     * If name does not equal any of the Simpple Types in the enumeration method returns a -1 for error checking.
     */

    public static int getTypeId(String name) {
        if (name.equalsIgnoreCase("PROCESS")) {
            return SimpplleType.PROCESS.ordinal();
        }
        else if (name.equalsIgnoreCase("SPECIES")) {
            return SimpplleType.SPECIES.ordinal();
        }
        else if (name.equalsIgnoreCase("SIZE-CLASS")) {
            return SimpplleType.SIZE_CLASS.ordinal();
        }
        else if (name.equalsIgnoreCase("DENSITY")) {
            return SimpplleType.DENSITY.ordinal();
        }
        else if (name.equalsIgnoreCase("TREATMENT")) {
            return SimpplleType.TREATMENT.ordinal();
        }
        else if (name.equalsIgnoreCase("HTGRP")) {
            return SimpplleType.HTGRP.ordinal();
        }

        return -1;
    }

    /**
     * Added this method due to an error in how MultipleRunSummary wrote
     * out Types.SIZE_CLASS as the String SIZE-CLASS.  This method is used
     * to make sure that the correct Types value is returned.
     * @param name
     * @return
     */
    public static Types getTypeFromString(String name) {
        int id = getTypeId(name);
        if (id == -1) { return null; }

        return Types.values()[id];
    }
    /**
     * Gets the kind of Simpplle Type.  Choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
     * @param obj the object to have its type found.
     * @return the simpplle type.  Choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
     */
    public static Types getKind(Object obj) {
        if (obj instanceof Species) {
            return SPECIES;
        }
        else if (obj instanceof SizeClass) {
            return SIZE_CLASS;
        }
        else if (obj instanceof Density) {
            return DENSITY;
        }
        else if (obj instanceof ProcessType) {
            return PROCESS;
        }
        else if (obj instanceof TreatmentType) {
            return TREATMENT;
        }
        else if (obj instanceof HabitatTypeGroupType) {
            return GROUP;
        }
        else {
            return null;
        }
    }
    /**
     * Reads from external object source the SimpplleType object which is then passed the get
     * @param in
     * @param kind
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static SimpplleType readExternalSimple(ObjectInput in, Types kind)
            throws IOException, ClassNotFoundException
    {
        int version = in.readInt();
        return get(kind,(String)in.readObject());
    }

    /**
     * Writes Simpplle Type object to external source.  These are written by their toStrings.  from external source then
     * @param out the simpplle type object to be written
     * @throws IOException
     */
    public void writeExternalSimple(ObjectOutput out) throws IOException {
        out.writeInt(version);

        out.writeObject(toString());
    }
    /**
     * Returns the arraylist corresponding to the parameter Simpplle Types.
     * @param kind the simpplle type.  Choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP.
     * @return the arraylist with simpplle types based on the simpplle type
     */
    public static ArrayList<SimpplleType> getList(Types kind) {
        switch (kind) {
            case SPECIES:    return allSpeciesList;
            case SIZE_CLASS: return allSizeClassList;
            case DENSITY:    return allDensityList;
            case PROCESS:    return allProcessList;
            case TREATMENT:  return allTreatmentList;
            case GROUP:      return allGroupList;
            default: return null;
        }
    }
    /**
     * Mapping from key String kind to type of species, class size, density, process, treatment, group.
     * @param kind Type which acts as value for switch to get designated hashmap of Types.
     * @return HashMap from SimpplleType
     */
    private static HashMap<String,SimpplleType> getHm(Types kind) {
        switch (kind) {
            case SPECIES:    return allSpeciesHm;
            case SIZE_CLASS: return allSizeClassHm;
            case DENSITY:    return allDensityHm;
            case PROCESS:    return allProcessHm;
            case TREATMENT:  return allTreatmentHm;
            case GROUP:      return allGroupHm;
            default: return null;
        }
    }
    /**
     * Method which takes in a simpplle type object and gets the corresponding list of that type.  If arraylist is empty it is a new
     * arraylist created in this class.  they data is then added to the arraylist.  The data is then added to the simpplle types hash map.
     * This method is called from the constructors of some simpplle types to create new simpplle type objects.  Ex Size Class.
     * @param data simpplle type
     * @param kind the simpplle type - passed as upercase simpplle type from Enum Types.
     */
    public static void updateAllData(SimpplleType data, Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);

        if (allList.contains(data) == false) {
            allList.add(data);
        }

        HashMap<String,SimpplleType> allHm = getHm(kind);
        if (allHm.containsKey(data.toString()) == false) {
            allHm.put(data.toString(),data);
        }

        switch (kind) {
            case SPECIES:    Species.update((Species)data); break;
            case SIZE_CLASS: break;
            case DENSITY:    break;
            case PROCESS:    break;
            case TREATMENT:  break;
            case GROUP:      break;
            default: break;
        }

    }

    /**
     * Contrary to the name, this does not initialize all lists. It initializes the lists of Type Size class, Density, Group.
     */
    public static void initializeAllLists() {
//    initializeAllList(Types.SPECIES);
        initializeAllList(Types.SIZE_CLASS);
        initializeAllList(Types.DENSITY);
        initializeAllList(Types.GROUP);
    }

    /**
     * Initializes the process and treatments arraylist.
     */
    public static void initializeProcessTreatmentLists() {
        initializeAllList(Types.PROCESS);
        initializeAllList(Types.TREATMENT);
    }
    /**
     * Initializes the Treatments List.
     */
    public static void initializeTreatmentList() {
        initializeAllList(Types.TREATMENT);
    }

    /**
     * Gets the current zone and uses its species, size class, density, processes, treatments, or habitat group to form a vector.
     * This vector will be then looped through to update all the arraylists for the types and then the all types hash map. ough create and initialize lists of simpplle types in that zone.
     * Species - gets all species from vector, clear invasive species for the zone
     * <li>Size_Class - gets all size class for the zone.
     * <li>Density - gets all density for the zone.
     * Note:  Processes and Treatment types are handled differently than species, size class, density, and habitat group.
     * @param kind Type used in switch to
     */
    protected static void initializeAllList(Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);
        allList.clear();
        List v;
        RegionalZone zone = Simpplle.getCurrentZone();
        switch (kind) {
            case SPECIES:
                v = zone.getAllSpecies();
                Species.clearInvasive();
                break;
            case SIZE_CLASS: v = zone.getAllSizeClass(); break;
            case DENSITY:    v = zone.getAllDensity(); break;
            case PROCESS:
            {
                List tmp = Process.getLegalProcessesList();
                v = new ArrayList<SimpplleType>(tmp);
                v.add(ProcessType.WET_SUCCESSION);
                v.add(ProcessType.DRY_SUCCESSION);
                break;
            }
            case TREATMENT:
            {
                List tmp = Treatment.getLegalTreatmentList();
                v = new ArrayList<SimpplleType>(tmp);
                v.add(TreatmentType.NONE);
                break;
            }
            case GROUP:      v = HabitatTypeGroup.getAllLoadedTypes(); break;

            default: return;
        }
        if (v == null) { return; }

        for (int i=0; i<v.size(); i++) {
            updateAllData((SimpplleType)v.get(i),kind);
        }
    }
    /**
     * Reads simpplle type dates jars.
     * @param stream
     * @param kind
     */

    public static void readData(JarInputStream stream, Types kind) {
        int          size;
        SimpplleType data;
        try {
            initializeAllList(kind);
            ObjectInputStream s = new ObjectInputStream(stream);
            size = s.readInt();
            for (int i = 0; i < size; i++) {
                data = (SimpplleType) s.readObject();
                updateAllData(data,kind);
            }
            if (kind == SPECIES) {
                Species.sort();
            }
            else {
                ArrayList<SimpplleType> allList = getList(kind);
                Collections.sort(allList);
            }
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Saves a particular simpplle type data to a jar output stream
     * @param stream
     * @param kind the simpplle type
     */
    public static void saveData(JarOutputStream stream, Types kind) {
        try {
            ArrayList<SimpplleType> allList = getList(kind);
            ObjectOutputStream s = new ObjectOutputStream(stream);

            s.writeInt(allList.size());
            for (int i = 0; i < allList.size(); i++) {
                s.writeObject(allList.get(i));
            }
        }
        catch (IOException ex) {
        }
    }

    // *** JTable Stuff ***
    // ********************
//  private Class getClass(Types kind) {
//    switch (kind) {
//      case SIZE_CLASS: return SizeClass.class;
//      default: return null;
//    }
//  }
    /**
     * Gets the simpplle type object info in a column.
     * @param col
     * @return null.
     */
    public Object getColumnData(int col) { return null; }
    /**
     * Sets the simpplle type object data in a particular column of the GUI table model.
     * @param value simpplle type
     * @param col
     */
    public void setColumnData(Object value, int col) {}
    /**
     * Gets the simpplle type object a a particular cell in the GUI table model.
     * @param row row used to locate the cell
     * @param col column used to locate the cell
     * @param kind simpplle type.  Choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
     * @return simpple type.
     */
    public static Object getValueAt(int row, int col, Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);

        return ((SimpplleType)allList.get(row)).getColumnData(col);
    }
    /**
     * Gets the simpplle type object a a particular cell in the GUI table model.
     * @param row
     * @param kind simppllet type
     * @return the simpplle type at a partcular cell
     */
    public static SimpplleType getValueAt(int row, Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);

        return (SimpplleType)allList.get(row);
    }
    /**
     * Sets value at a particular cell in GUI table model
     * @param value
     * @param row row used to find cell
     * @param col column used to locate cell
     * @param kind simpplle type.  Choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
     */
    public static void setValueAt(Object value, int row, int col, Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);

        ((SimpplleType)allList.get(row)).setColumnData(value,col);
    }

    /**
     * Deletes data at a particular row.  It removes the simpplle type object from the particular hash map of simpplle type.  Then
     * removes the value at particular row from the all types list and all types hash map.
     */
    public static void deleteDataRow(int row, Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);
        HashMap<String,SimpplleType> allHm = getHm(kind);

        SimpplleType data = getValueAt(row,kind);
        allList.remove(data);
        allHm.remove(data);
    }
    /**
     * Calculates the row count by getting arraylist containing all the simpplle type objects for a particular kind of simpplle type.
     * @param kind choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
     * @return int size of simpplle type list for a particular kind.  Represents the row count.
     */
    public static int getRowCount(Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);
        return allList.size();
    }

}


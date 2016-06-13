package simpplle.comcode;

import java.io.*;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> SimpplleType is the base class for fundamental data types in OpenSIMPPLLE. The primary purpose of this class
 * is to reuse instances of subclasses.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public abstract class SimpplleType implements Comparable {

    private static final int version = 1;

    public enum Types {
        SPECIES,
        SIZE_CLASS,
        DENSITY,
        PROCESS,
        TREATMENT,
        GROUP
    }

    public enum TypesAqua {
        AQUATIC_CLASS,
        AQUATIC_ATTRIBUTE,
        AQUATIC_PROCESS,
        AQUATIC_TREATMENT,
        LTA_VS_GROUP
    }

    public static final Types SPECIES    = Types.SPECIES;
    public static final Types SIZE_CLASS = Types.SIZE_CLASS;
    public static final Types DENSITY    = Types.DENSITY;
    public static final Types PROCESS    = Types.PROCESS;
    public static final Types TREATMENT  = Types.TREATMENT;
    public static final Types HTGRP      = Types.GROUP;
    public static final Types GROUP      = Types.GROUP;

    public static final int MAX = Types.GROUP.ordinal() + 1;

    public static final TypesAqua AQUATIC_CLASS     = TypesAqua.AQUATIC_CLASS;
    public static final TypesAqua AQUATIC_ATTRIBUTE = TypesAqua.AQUATIC_ATTRIBUTE;
    public static final TypesAqua AQUATIC_PROCESS   = TypesAqua.AQUATIC_PROCESS;
    public static final TypesAqua AQUATIC_TREATMENT = TypesAqua.AQUATIC_TREATMENT;
    public static final TypesAqua LTA_VS_GROUP      = TypesAqua.LTA_VS_GROUP;

    public static final int MAX_AQUATIC = TypesAqua.LTA_VS_GROUP.ordinal() + 1;

    protected static ArrayList<SimpplleType> allSpeciesList = new ArrayList<>();
    protected static ArrayList<SimpplleType> allSizeClassList = new ArrayList<>();
    protected static ArrayList<SimpplleType> allDensityList = new ArrayList<>();
    protected static ArrayList<SimpplleType> allProcessList = new ArrayList<>();
    protected static ArrayList<SimpplleType> allTreatmentList = new ArrayList<>();
    protected static ArrayList<SimpplleType> allGroupList = new ArrayList<>();

    protected static HashMap<String,SimpplleType> allSpeciesHm = new HashMap<>();
    protected static HashMap<String,SimpplleType> allSizeClassHm = new HashMap<>();
    protected static HashMap<String,SimpplleType> allDensityHm = new HashMap<>();
    protected static HashMap<String,SimpplleType> allProcessHm = new HashMap<>();
    protected static HashMap<String,SimpplleType> allTreatmentHm = new HashMap<>();
    protected static HashMap<String,SimpplleType> allGroupHm = new HashMap<>();

    /**
     * @return The GIS name of this instance, which contains dashes instead of underscores
     */
    public String getGISPrintName() {
        if (this instanceof Density) {
            return ((Density)this).getGisPrintName();
        }
        return Utility.dashesToUnderscores(toString());
    }

    /**
     * @param kind A SimpplleType kind
     * @param typeName The name of the subclass instance
     * @return A simpplle type instance with a matching type and name
     */
    public static SimpplleType get(Types kind, String typeName) {
        switch (kind) {
            case SPECIES:    return Species.get(typeName);
            case SIZE_CLASS: return SizeClass.get(typeName);
            case DENSITY:    return Density.get(typeName);
            case PROCESS:    return ProcessType.get(typeName);
            case TREATMENT:  return TreatmentType.get(typeName);
            case GROUP:      return HabitatTypeGroupType.get(typeName);
            default:         return null;
        }
    }

    /**
     * @param kind A SimpplleType kind
     * @return The kind represented as a string
     */
    public static String getTypeName(Types kind) {
        switch(kind) {
            case PROCESS:    return "PROCESS";
            case SPECIES:    return "SPECIES";
            case SIZE_CLASS: return "SIZE-CLASS";
            case DENSITY:    return "DENSITY";
            case TREATMENT:  return "TREATMENT";
            case GROUP:      return "HTGRP";
            default:         return null;
        }
    }

    /**
     * @param name A SimpplleType kind name
     * @return The ID of the kind, or -1 if a kind isn't found
     */
    public static int getTypeId(String name) {
        if      (name.equalsIgnoreCase("PROCESS"))    return SimpplleType.PROCESS.ordinal();
        else if (name.equalsIgnoreCase("SPECIES"))    return SimpplleType.SPECIES.ordinal();
        else if (name.equalsIgnoreCase("SIZE-CLASS")) return SimpplleType.SIZE_CLASS.ordinal();
        else if (name.equalsIgnoreCase("DENSITY"))    return SimpplleType.DENSITY.ordinal();
        else if (name.equalsIgnoreCase("TREATMENT"))  return SimpplleType.TREATMENT.ordinal();
        else if (name.equalsIgnoreCase("HTGRP"))      return SimpplleType.HTGRP.ordinal();
        else                                          return -1;
    }

    /**
     * @param name A SimpplleType kind name
     * @return The kind given the kind name, or null if a kind isn't found
     */
    public static Types getTypeFromString(String name) {
        int id = getTypeId(name);
        if (id == -1) {
            return null;
        } else {
            return Types.values()[id];
        }
    }

    /**
     * @param obj The object to inspect
     * @return A kind if the object is an instance of a SimpplleType subclass
     */
    public static Types getKind(Object obj) {
        if      (obj instanceof Species)              return SPECIES;
        else if (obj instanceof SizeClass)            return SIZE_CLASS;
        else if (obj instanceof Density)              return DENSITY;
        else if (obj instanceof ProcessType)          return PROCESS;
        else if (obj instanceof TreatmentType)        return TREATMENT;
        else if (obj instanceof HabitatTypeGroupType) return GROUP;
        else                                          return null;
    }

    /**
     * @param in An object input stream containing a kind string
     * @param kind A kind of instance to search for
     * @return A SimpplleType subclass instance
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static SimpplleType readExternalSimple(ObjectInput in, Types kind) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        return get(kind,(String)in.readObject());
    }

    /**
     * @param out An object output stream for writing a kind string
     * @throws IOException
     */
    public void writeExternalSimple(ObjectOutput out) throws IOException {
        out.writeInt(version);
        out.writeObject(toString());
    }

    /**
     * @param kind A kind that will be in the list
     * @return An array of SimpplleType subclass instances
     */
    public static ArrayList<SimpplleType> getList(Types kind) {
        switch (kind) {
            case SPECIES:    return allSpeciesList;
            case SIZE_CLASS: return allSizeClassList;
            case DENSITY:    return allDensityList;
            case PROCESS:    return allProcessList;
            case TREATMENT:  return allTreatmentList;
            case GROUP:      return allGroupList;
            default:         return null;
        }
    }

    /**
     * @param kind A kind that will be in the map
     * @return A map of strings to SimpplleType subclass instances
     */
    private static HashMap<String,SimpplleType> getHm(Types kind) {
        switch (kind) {
            case SPECIES:    return allSpeciesHm;
            case SIZE_CLASS: return allSizeClassHm;
            case DENSITY:    return allDensityHm;
            case PROCESS:    return allProcessHm;
            case TREATMENT:  return allTreatmentHm;
            case GROUP:      return allGroupHm;
            default:         return null;
        }
    }

    /**
     * Adds a SimpplleType subclass instance to a typed array and hash map if it isn't a member. If the instance is
     * a species, then Species.update() is called.
     * @param data An instance of a SimpplleType subclass
     * @param kind A kind of SimpplleType
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
        }
    }

    /**
     * Copies a list of global SimpplleType instances from another class into SimpplleType's own global list.
     * @param kind A kind of SimpplleType
     */
    protected static void initializeAllList(Types kind) {

        ArrayList<SimpplleType> allList = getList(kind);
        if (allList == null) return;
        allList.clear();

        // The objects should really be passed in as an argument.

        List<SimpplleType> objects;

        switch (kind) {

            case SPECIES:    objects = Simpplle.getCurrentZone().getAllSpecies(); break;
            case SIZE_CLASS: objects = Simpplle.getCurrentZone().getAllSizeClass(); break;
            case DENSITY:    objects = Simpplle.getCurrentZone().getAllDensity(); break;
            case PROCESS:    objects = Process.getLegalProcessesList(); break;
            case TREATMENT:  objects = Treatment.getLegalTreatmentList(); break;
            case GROUP:      objects = HabitatTypeGroup.getAllLoadedTypes(); break;

            default: return;

        }

        if (objects == null) return;

        for (SimpplleType object : objects) {
            updateAllData(object,kind);
        }

        // These are special cases that should be dealt with outside of this method.

        switch (kind) {

            case SPECIES:
                Species.clearInvasive();
                break;

            case PROCESS:
                updateAllData(ProcessType.WET_SUCCESSION,PROCESS);
                updateAllData(ProcessType.DRY_SUCCESSION,PROCESS);
                break;

            case TREATMENT:
                updateAllData(TreatmentType.NONE,TREATMENT);
                break;

        }
    }

    /**
     * Populates the size class list.
     */
    public static void initializeSizeClassList() {
        initializeAllList(Types.SIZE_CLASS);
    }

    /**
     * Populates the density list.
     */
    public static void initializeDensityList() {
        initializeAllList(Types.DENSITY);
    }

    /**
     * Populates the process list.
     */
    public static void initializeProcessList() {
        initializeAllList(Types.PROCESS);
    }

    /**
     * Populates the treatment list.
     */
    public static void initializeTreatmentList() {
        initializeAllList(Types.TREATMENT);
    }

    /**
     * Populates the group list.
     */
    public static void initializeGroupList() {
        initializeAllList(Types.GROUP);
    }

    /**
     * Reads instances from a jar.
     * @param jarStream
     * @param kind
     */
    public static void readData(JarInputStream jarStream, Types kind) {
        try {

            initializeAllList(kind);

            ObjectInputStream objStream = new ObjectInputStream(jarStream);

            int size = objStream.readInt();
            for (int i = 0; i < size; i++) {
                SimpplleType data = (SimpplleType)objStream.readObject();
                updateAllData(data,kind);
            }

            if (kind == SPECIES) {
                Species.sort();
            } else {
                ArrayList<SimpplleType> allList = getList(kind);
                Collections.sort(allList);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves instances to a jar.
     * @param jarStream
     * @param kind the simpplle type
     */
    public static void saveData(JarOutputStream jarStream, Types kind) {
        try {
            ArrayList<SimpplleType> allList = getList(kind);

            ObjectOutputStream s = new ObjectOutputStream(jarStream);

            s.writeInt(allList.size());

            for (SimpplleType object : allList) {
                s.writeObject(object);
            }

        } catch (IOException ex) {
            // Do nothing
        }
    }

    // *** JTable Stuff ***
    // ********************

    /**
     * Gets the simpplle type object info in a column.
     * @param col
     * @return null.
     */
    public Object getColumnData(int col) {
        return null;
    }

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
        return allList.get(row);
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
        allList.get(row).setColumnData(value,col);
    }

    /**
     * Deletes data at a particular row.  It removes the simpplle type object from the particular hash map of simpplle type.  Then
     * removes the value at particular row from the all types list and all types hash map.
     */
    public static void deleteDataRow(int row, Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);
        HashMap<String,SimpplleType> allHm = getHm(kind);

        SimpplleType data = getValueAt(row,kind);
        if (allList != null) allList.remove(data);
        if (allHm != null) allHm.remove(data);
    }

    /**
     * Calculates the row count by getting arraylist containing all the simpplle type objects for a particular kind of simpplle type.
     * @param kind choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
     * @return int size of simpplle type list for a particular kind.  Represents the row count.
     */
    public static int getRowCount(Types kind) {
        ArrayList<SimpplleType> allList = getList(kind);
        if (allList != null) {
            return allList.size();
        } else {
            return 0;
        }
    }
}


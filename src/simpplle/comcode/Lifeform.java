package simpplle.comcode;

import java.beans.*;
import java.io.*;
import java.util.*;
/**
 *
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Lifeforms.  Choices of lifeforms are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA.
 * Lifeforms will have individual species.  Methods for these can be found in species.java
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class Lifeform implements Externalizable {
    static final long serialVersionUID = 8809364504123901646L;
    static final int  version          = 1;

    private String name;
    private int    id;

    public static HashMap<Short,Lifeform> simIdHm = new HashMap<Short,Lifeform>();
    private short simId=-1; // Random Access File ID
    public static short nextSimId=0;
    public short getSimId() {
        if (simId == -1) {
            simId = nextSimId;
            nextSimId++;
            simIdHm.put(simId,this);
        }
        return simId;
    }

    /**
     * Needs to be present for database, does nothing.
     * @param id short
     */
    public void setSimId(short id) {}

    public static Lifeform lookUpLifeform(short simId) { return simIdHm.get(simId); }

    public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();

        int size = in.readInt();
        for (int i=0; i<size; i++) {
            short id = in.readShort();
            Lifeform life = (Lifeform)in.readObject();
            simIdHm.put(id,life);
            if ( (id+1) > nextSimId) {
                nextSimId = (short)(id+1);
            }
        }
    }
    public static void writeExternalSimIdHm(ObjectOutput out) throws IOException {
        out.writeInt(version);

        out.writeInt(simIdHm.size());
        for (Short id : simIdHm.keySet()) {
            out.writeShort(id);
            Lifeform life = simIdHm.get(id);
            out.writeObject(life);
        }
    }

    private static transient HashMap allLifeforms = new HashMap(5);

    public static Lifeform TREES       = new Lifeform("trees",0);
    public static Lifeform SHRUBS      = new Lifeform("shrubs",1);
    public static Lifeform HERBACIOUS  = new Lifeform("herbacious",2);
    public static Lifeform AGRICULTURE = new Lifeform("agriculture",3);
    public static Lifeform NA          = new Lifeform("no classification",4);

    private static Lifeform[] allValues =
            new Lifeform[] {TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA};

    public static int numValues() { return 5; }

    public Lifeform() {}
    private Lifeform(String name, int id) {
        this.name = name;
        this.id = id;

        allLifeforms.put(name,this);
    }
    /**
     * Gets the lifeform id.  Choices are 0 - trees, 1 shrubs, 2- herbacious, 3 - agriculture, 4 - no classification.
     * @return lifeform ID
     */
    public int getId() { return id; }
    /**
     * Returns the string name of lifefrom, one of the two identifiers of lifeforms (along with Id).
     * Choices are trees, shrubs, herbacious, agriculture, or no classification.
     *
     */
    public String toString() { return name; }

    public static Lifeform[] getAllValues() { return allValues; }
    /**
     * creates an arraylist of life forms choices are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA
     *
     */
    public static ArrayList<Lifeform> getAllValuesList() {
        ArrayList<Lifeform> lives = new ArrayList<Lifeform>();
        for (int i = 0; i < allValues.length; i++) {
            lives.add(allValues[i]);
        }
        return lives;
    }
    /**
     * Gets the name of lifefrom.  Choices are trees, shrubs, herbacious, agriculture, or no classification.
     *
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the lifeform object from the string name.
     * Objects returned will be trees, shrubs, herbacious, agriculture, no classification.
     * The corresponding lifeforms strings are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA
     * @param name name of life forms (uppercase)
     * @return lifeform object from allLifeforms hash map (lowercase)
     */
    public static Lifeform get(String name) {
        if (name.equalsIgnoreCase("NA")) { name = "no classification"; }

        return (Lifeform) allLifeforms.get(name.toLowerCase());
    }
    /**
     * returns the lifeform in all values [] based on indexing lifeform ID.  Choices are 0 - TREES, 1 - SHRUBS, 2 -HERBACIOUS, 3 -AGRICULTURE, 4 -NA
     * @param id the lifeform id (0,1,2, or 3)
     * @return lifeform object
     */
    public static Lifeform get(int id) { return allValues[id]; }
    /**
     * Finds the dominant lifeform by first setting the dominant lifeform to the highest ID value but lowest dominance (3 = NA)
     * then loops through the lifeforms id's to find the minimum ID.  THis will be the dominant lifeform because lower ID's have higher dominance.
     * @param lives the collection of lifeforms
     * @return the dominant lifeform
     */
    public static Lifeform findDominant(Set<Lifeform> lives) {
        Lifeform dominant=Lifeform.NA;
        for (Lifeform lifeform : lives) {
            if (lifeform.getId() < dominant.getId()) {
                dominant = lifeform;
            }
        }
        return dominant;
    }
    /**
     * Method to make an arraylist of life forms that are more dominant (more dominant = lower Id..  The list will be in ascending order
     * @param lifeform
     * @return arraylist of increasingly more dominant species than passed life form
     */
    public static ArrayList<Lifeform> getMoreDominant(Lifeform lifeform) {
        ArrayList<Lifeform> result = new ArrayList<Lifeform>();
        for (int i=0; i<allValues.length; i++) {
            if (allValues[i].getId() < lifeform.getId()) {
                result.add(allValues[i]);
            }
        }
        return result;
    }
    /**
     * Finds a lower life form.  Loops through all the lifeforms to find a lower dominance lifeform if there is any.
     * Higher ID's correspond to lower dominance.
     * @param lifeform the lifeform to be compared.  Choices for lifeforms are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA
     * @return lifeform with higher ID meaning less dominant than parameter lifeform, if any
     */
    public static Lifeform getLowerLifeform(Lifeform lifeform) {
        for (int i=0; i<allValues.length; i++) {
            if (allValues[i].getId() > lifeform.getId()) {
                return allValues[i];
            }
        }
        return null;
    }
    /**
     * Gets the more dominant of two lifeforms.  Higher dominance corresponds to lower lifeform ID's
     * @param life first life form to be compared
     * @param life2 second life form to be compared
     * @return the lifeform with lesser ID and therefore higher dominance
     */
    public static Lifeform getMostDominant(Lifeform life, Lifeform life2) {
        if (life == null) { return life2; }
        if (life2 == null) { return life; }

        if (life2.getId() < life.getId()) {
            return life2;
        }
        return life;
    }
    /**
     * Reads in lifeforms.  These are read in following order: lifeform name, lifeform ID
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();

        name = (String)in.readObject();
        id   = in.readInt();
    }
    /**
     * Writes a lifeform (name and Id) to external source.
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(version);

        out.writeObject(name);
        out.writeInt(id);
    }

    public void writeXML(XMLEncoder e) {
        e.writeObject(version);
        e.writeObject(name);
    }
    /**
     * Reads a lifeform object object from object stream.
     * @return
     * @throws java.io.ObjectStreamException - not caught in this class
     */
    private Object readResolve () throws java.io.ObjectStreamException
    {
        return Lifeform.get(name);
    }
    /**
     * Sets the name for this lifeform.  Choices are trees, shrubs, herbacious, agriculture, no classification.
     */
    public void setName(String name) {
        this.name = name;
    }

}

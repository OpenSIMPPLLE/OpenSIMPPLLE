/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.beans.*;
import java.io.*;
import java.util.*;

/**
 * This class contains methods for Lifeforms.  Choices of lifeforms are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA.
 * Lifeforms will have individual species.  Methods for these can be found in species.java
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class Lifeform implements Externalizable {

    static final long serialVersionUID = 8809364504123901646L;
    static final int  version          = 1;

    private String name;
    private int    id;

    public static HashMap<Short,Lifeform> simIdHm = new HashMap<Short,Lifeform>();
    private short simId=-1; // Random Access File ID
    public static short nextSimId=0;

    public static void resetSimIds() {
        nextSimId = 0;
        for (Lifeform lifeform : simIdHm.values()) {
            lifeform.simId = -1;
        }
        simIdHm.clear();
    }

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

    public  static Lifeform   TREES       = new Lifeform("trees",0);
    public  static Lifeform   SHRUBS      = new Lifeform("shrubs",1);
    public  static Lifeform   HERBACIOUS  = new Lifeform("herbacious",2);
    public  static Lifeform   AGRICULTURE = new Lifeform("agriculture",3);
    public  static Lifeform   NA          = new Lifeform("no classification",4);
    private static Lifeform[] allValues   = new Lifeform[] {TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA};

    public static int numValues() { return 5; }

    public Lifeform() {}

    private Lifeform(String name, int id) {

        this.name = name;
        this.id = id;

        allLifeforms.put(name,this);
    }

    /**
     * Gets the lifeform id. Choices are 0 - trees, 1 shrubs, 2- herbacious, 3 - agriculture, 4 - no classification.
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
     * creates an arraylist of lifeforms choices are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA
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
     * Gets the name of lifeform. Choices are trees, shrubs, herbacious, agriculture, or no classification.
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
     * Returns a less dominant life form. Lifeforms are stored in an array from most dominant to
     * least dominant. The life forms in this array are looped through starting from the highest
     * dominance. The first life form with a lower dominance is returned.
     *
     * @param lifeform the life form to be compared
     * @return a less dominant life form, or null if it is the least dominant.
     */
    public static Lifeform getLessDominant(Lifeform lifeform) {
        for (Lifeform other : allValues) {
            if (lifeform.getId() < other.getId()) {
                return other;
            }
        }
        return null;
    }

    /**
     * Returns a list of more dominant lifeforms.
     *
     * @param lifeform the life form to be compared
     * @return a list of more dominant lifeforms, sorted from most to least dominant
     */
    public static List<Lifeform> getMoreDominant(Lifeform lifeform) {
        List<Lifeform> moreDominant = new ArrayList<>();
        for (Lifeform other : allValues) {
            if (other.getId() < lifeform.getId()) {
                moreDominant.add(other);
            }
        }
        return moreDominant;
    }

    /**
     * Returns the most dominant of two life forms.
     *
     * @param a first life form to be compared
     * @param b second life form to be compared
     * @return the life form with the lesser ID and therefore higher dominance
     */
    public static Lifeform getMostDominant(Lifeform a, Lifeform b) {
        if (a == null) { return b; }
        if (b == null) { return a; }

        if (a.getId() < b.getId()) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * Returns the most dominant from a set of life forms. The lifeform with the lowest ID has the
     * highest dominance. The search starts by setting the most dominant life form to the life form
     * with the lowest dominance. The life form with the highest dominance is returned.
     *
     * @param lifeforms a set of life forms
     * @return the life form with the lesser ID and therefore higher dominance
     */
    public static Lifeform getMostDominant(Set<Lifeform> lifeforms) {
        Lifeform dominant = Lifeform.NA;
        for (Lifeform lifeform : lifeforms) {
            dominant = getMostDominant(dominant, lifeform);
        }
        return dominant;
    }

    /**
     * Reads in lifeforms.  These are read in following order: lifeform name, lifeform ID
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        name = (String)in.readObject();
        id = in.readInt();
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
    private Object readResolve () throws java.io.ObjectStreamException {
        return Lifeform.get(name);
    }

    /**
     * Sets the name for this lifeform.  Choices are trees, shrubs, herbacious, agriculture, no classification.
     */
    public void setName(String name) {
        this.name = name;
    }

}

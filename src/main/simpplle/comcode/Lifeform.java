/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.*;

/**
 * A life form is a classification of a set of species. The default classifications are trees,
 * shrubs, herbs, agriculture, and no classification. The dominance, or amount of influence that a
 * life form has on an ecological region, is specified by a number. Lower numbers correspond to a
 * higher dominance.
 */

public class Lifeform implements Externalizable {

    static final long serialVersionUID = 8809364504123901646L;
    static final int  version = 1;

    private static final Map<String, Lifeform> lifeformsByName = new HashMap<>();
    private static final Map<Integer, Lifeform> lifeformsByDominance = new TreeMap<>();
    private static final Map<Short, Lifeform> lifeformsBySimId = new HashMap<>();

    public static final Lifeform TREES       = new Lifeform(0, "trees");
    public static final Lifeform SHRUBS      = new Lifeform(1, "shrubs");
    public static final Lifeform HERBACIOUS  = new Lifeform(2, "herbacious");
    public static final Lifeform AGRICULTURE = new Lifeform(3, "agriculture");
    public static final Lifeform NA          = new Lifeform(4, "no classification");

    private static short nextSimId = 0;

    private String name;
    private int dominance;
    private short simId = -1;

    /**
     * Creates a life form. This constructor is required for the externalizable interface.
     */
    public Lifeform() {}

    /**
     * Creates a life form and adds it to an internal life form collection.
     *
     * @param dominance the influence that a life form has; lower numbers = greater influence
     * @param name the name of the life form
     */
    private Lifeform(int dominance, String name) {

        this.dominance = dominance;
        this.name = name;

        lifeformsByName.put(name.toLowerCase(), this);
        lifeformsByDominance.put(dominance, this);
    }

    public static Lifeform findByName(String name) {
        if (name.equalsIgnoreCase("NA")) {
            return lifeformsByName.get("no classification");
        } else {
            return lifeformsByName.get(name.toLowerCase());
        }
    }

    public static Lifeform[] getLifeformsByDominance() {
        return (Lifeform[]) lifeformsByDominance.values().toArray();
    }

    public static ArrayList<Lifeform> getLifeformsByDominanceList() {
        ArrayList<Lifeform> list = new ArrayList<>();
        for (Lifeform lifeform : lifeformsByDominance.values()) {
            list.add(lifeform);
        }
        return list;
    }

    public int getDominance() {
        return dominance;
    }

    /**
     * Returns a less dominant life form. Lifeforms are stored in an array from most dominant to
     * least dominant. The life forms in this array are looped through starting from the highest
     * dominance. The first life form with a lower dominance is returned.
     *
     * @param lifeform the life form to be compared
     * @return a less dominant life form, or null if it is the least dominant.
     */
    public static Lifeform getLessDominant(Lifeform lifeform) {
        for (Lifeform other : lifeformsByDominance.values()) {
            if (lifeform.getDominance() < other.getDominance()) {
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
        for (Lifeform other : lifeformsByDominance.values()) {
            if (other.getDominance() < lifeform.getDominance()) {
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

        if (a.getDominance() < b.getDominance()) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getSimId() {
        if (simId == -1) {
            simId = nextSimId;
            nextSimId++;
            lifeformsBySimId.put(simId,this);
        }
        return simId;
    }

    public void setSimId(short id) {}

    public static Lifeform lookUpLifeform(short simId) {
        return lifeformsBySimId.get(simId);
    }

    public static int numValues() {
        return lifeformsByName.size();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        name = (String)in.readObject();
        dominance = in.readInt();
    }

    public static void readExternalSimIdHm(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        int size = in.readInt();
        for (int i=0; i<size; i++) {
            short id = in.readShort();
            Lifeform life = (Lifeform)in.readObject();
            lifeformsBySimId.put(id,life);
            if ( (id+1) > nextSimId) {
                nextSimId = (short)(id+1);
            }
        }
    }

    private Object readResolve () throws java.io.ObjectStreamException {
        return Lifeform.findByName(name);
    }

    public static void resetSimIds() {
        nextSimId = 0;
        for (Lifeform lifeform : lifeformsBySimId.values()) {
            lifeform.simId = -1;
        }
        lifeformsBySimId.clear();
    }

    public String toString() {
        return name;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(version);
        out.writeObject(name);
        out.writeInt(dominance);
    }

    public static void writeExternalSimIdHm(ObjectOutput out) throws IOException {
        out.writeInt(version);
        out.writeInt(lifeformsBySimId.size());
        for (Short id : lifeformsBySimId.keySet()) {
            out.writeShort(id);
            Lifeform life = lifeformsBySimId.get(id);
            out.writeObject(life);
        }
    }
}

/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.Hashtable;

/**
 * This class contains methods for Lt Valley Segment Group Type, a Simpplle Type.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class LtaValleySegmentGroupType extends SimpplleType {
  private final  String    group;
  private final boolean    userGroup;
  private static Hashtable allGroupHt = new Hashtable(9);

  public static final LtaValleySegmentGroupType _12FMA    = new LtaValleySegmentGroupType("12FMA",false);
  public static final LtaValleySegmentGroupType _12FWB    = new LtaValleySegmentGroupType("12FWB",false);
  public static final LtaValleySegmentGroupType _12FWC    = new LtaValleySegmentGroupType("12FWC",false);
  public static final LtaValleySegmentGroupType _45_41FMA = new LtaValleySegmentGroupType("45-41FMA",false);
  public static final LtaValleySegmentGroupType _45_41FNA = new LtaValleySegmentGroupType("45-41FNA",false);
  public static final LtaValleySegmentGroupType _51FMA    = new LtaValleySegmentGroupType("51FMA",false);
  public static final LtaValleySegmentGroupType _51FWB    = new LtaValleySegmentGroupType("51FWB",false);
  public static final LtaValleySegmentGroupType _51UWB    = new LtaValleySegmentGroupType("51UWB",false);
  public static final LtaValleySegmentGroupType _62_72FNA = new LtaValleySegmentGroupType("62-72FNA",false);


  public LtaValleySegmentGroupType(String groupStr) {
    this(groupStr,true);
  }

  public LtaValleySegmentGroupType(String groupStr, boolean userGroup) {
    this.group     = groupStr.toUpperCase();
    this.userGroup = userGroup;
    allGroupHt.put(this.group,this);
  }

  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof LtaValleySegmentGroupType) {
      if (group == null || obj == null) { return false; }

      return group.equals(((LtaValleySegmentGroupType)obj).group);
    }
    return false;
  }

  public int hashCode() {
    return group.hashCode();
  }

  public int compareTo(Object o) {
    if (o == null) { return -1; }
    return group.compareTo(o.toString());
  }

  public String toString() { return group; }

  public static LtaValleySegmentGroupType get(LtaValleySegmentGroup aGroup) {
    return get(aGroup.toString());
  }
  public static LtaValleySegmentGroupType get(String groupName) {
    return ( (LtaValleySegmentGroupType)allGroupHt.get(groupName.toUpperCase()) );
  }

  public boolean isUserGroup() { return userGroup; }
}





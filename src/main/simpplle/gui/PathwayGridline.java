/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Point;
import javax.swing.JPanel;
import java.util.Hashtable;

/**
 * This class implements the grid lines in the pathway dialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class PathwayGridline {
	public  static final char	 HORIZONTAL_DIR = 'H';
	public  static final char	 VERTICAL_DIR = 'V';
	private static final Color LINE_COLOR = Color.black;
	private static final int	 LINE_WIDTH = 2;
	private static final int 	 SPACER = 5;

	private char 	direction;
	private int		position;
	private JPanel parent;
	
	public PathwayGridline(char dir, Point pos, JPanel parentPanel){
		this(dir, 0, parentPanel);
		if(pos!=null) {
			switch(direction){
				case HORIZONTAL_DIR:
					position = pos.y;
					break;
				case VERTICAL_DIR:
					position = pos.x;
					break;
			}
		}
	}

	public PathwayGridline(char dir, int pos, JPanel parentPanel){
		direction = dir;
		position = pos;
		parent = parentPanel;
	}

	public int getPosition() {
		return position;
	}
	
	public static String getKey(char dir, int pos){
		return dir + Integer.toString(pos);		
	}
	
	public static String getKey(char dir, Point pos, int offset){
		switch(dir){
			case HORIZONTAL_DIR:
				return getKey(dir, pos.y + offset);
			case VERTICAL_DIR:
				return getKey(dir, pos.x + offset);
			default:
				return null;
		}
	}
	public static String getKey(char dir, Point pos)
	{
		return getKey(dir, pos, 0);
	}
	
	public String getKey(){
		return getKey(direction, position);
	}
	
	public Cursor getSelectedCursor()
	{
		switch(direction){
			case HORIZONTAL_DIR:
				return new Cursor(Cursor.N_RESIZE_CURSOR | Cursor.S_RESIZE_CURSOR);
			case VERTICAL_DIR:
				return new Cursor(Cursor.W_RESIZE_CURSOR | Cursor.E_RESIZE_CURSOR);
			default:
				return new Cursor(Cursor.DEFAULT_CURSOR);
		}
	}
	/**
	 * Returns the new key if the line was successfully moved, null otherwise.  
	 * @param newPointPos
	 * @return new key if line successfully moved.  
	 */
	public String moveLine(Point newPointPos){
		int newPos=-1;
		switch(direction){
			case HORIZONTAL_DIR:
				newPos = newPointPos.y;
				break;
			case VERTICAL_DIR:
				newPos = newPointPos.x;
				break;
		}
		return moveLine(newPos);
	}
	
	public String moveLine(int newPos){
		String retval = null;
		switch(direction){
			case HORIZONTAL_DIR:
				if(newPos >= 0 && newPos < parent.getWidth()){
					position = newPos;
					retval = getKey();
				}
				break;
			case VERTICAL_DIR:
				if(newPos >= 0 && newPos < parent.getHeight()){
					position = newPos;
					retval = getKey(); 
				}
				break;
		}
		return retval;
	}
	
	public void paint(Graphics g) {
		g.setColor(LINE_COLOR);
		switch(direction){
			case VERTICAL_DIR:
				if(position >= 0 && position < parent.getWidth())
					g.fillRect(position, 0, LINE_WIDTH, parent.getHeight()-position);
				break;
			case HORIZONTAL_DIR:
				if(position >= 0 && position < parent.getHeight())
					g.fillRect(0, position, parent.getWidth()-position, LINE_WIDTH);
				break;
		}
	}

	public boolean isLineCrossingRectangles(Rectangle[] rectangles){
		boolean retval = false;
		for(int i=0; i<rectangles.length && !retval; i++) {
			switch(direction){
				case VERTICAL_DIR:
					if(rectangles[i].contains(position, rectangles[i].y))
						retval = true;
					break;
				case HORIZONTAL_DIR:
					if(rectangles[i].contains(rectangles[i].x, position))
						retval = true;
					break;
			}
		}
		return retval;		
	}

	public static Hashtable getLinesAroundRectangles(Rectangle[] rectangles, JPanel parentPanel) {
		Hashtable retval = new Hashtable(getLinesAroundRectangles(VERTICAL_DIR, rectangles, parentPanel));
		retval.putAll(getLinesAroundRectangles(HORIZONTAL_DIR, rectangles, parentPanel));
		return retval;
	}

	private static Hashtable getLinesAroundRectangles(char dir, Rectangle[] rectangles, JPanel parentPanel) {
		Hashtable retval = new Hashtable();
		PathwayGridline curLine;
		String key;

		for(int i=0; i<rectangles.length; i++) {
			curLine = new PathwayGridline(dir, rectangles[i].getLocation(), parentPanel);
			key = curLine.moveLine(curLine.getPosition()-1);
			if(key!=null && !curLine.isLineCrossingRectangles(rectangles)) {
				key = curLine.moveLine(curLine.getPosition()-SPACER);
				if(key!=null)
					retval.put(key, curLine);
			}
		}
		return retval;
	}
	
	public static PathwayGridline grabLine(Hashtable lines, char dir, Point pt)
	{
		PathwayGridline retval = null;
		String searchKey;
		
		for(int i=0; i < LINE_WIDTH; i++) {
			if(retval!=null)
				break;
			searchKey = PathwayGridline.getKey(dir, pt, i);
			retval = (PathwayGridline)lines.get(searchKey);
		}
		return retval;
	}

}

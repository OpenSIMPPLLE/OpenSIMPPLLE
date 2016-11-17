/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;
import simpplle.comcode.VegetativeType;
import simpplle.comcode.Process;
import simpplle.comcode.Species;

/** 
 * This class implements the Pathway shape for collapsed shapes, a type of PathwayShape
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class CollapsedPathwayShape extends PathwayShape {

        private static final Color COLLAPSED_COLOR     = Color.WHITE;
        private Hashtable		detailedShapes = new Hashtable();
/**
 * Constructor for Collapsed Pathway Shape.  Inherits from Pathway Shape superclass and passes in the detailed shape veg state, width, and height.
 * sets the detailed shape x, y and the color to the Collapsed_Color
 * @param detailedShape
 */
        public CollapsedPathwayShape(PathwayShape detailedShape)
        {
                super(detailedShape.getState(), detailedShape.width, detailedShape.height);
                setPosition(new Point(detailedShape.x, detailedShape.y));
                addDetailShape(detailedShape);
                normalColor     = COLLAPSED_COLOR;
                color           = normalColor;
        }
/**
 * The detailedShapes hash table is keyed by detailed shapes current vegetative state, the value is detailed shape.
 * @param detailedShape
 * @return
 */
        public boolean addDetailShape(PathwayShape detailedShape){
                boolean retval = isRelated(detailedShape);
                String test;
                if(retval)
                {
                        detailedShapes.put(detailedShape.getState().getCurrentState(), detailedShape);
                        // Move the youngest state as the main node
                        if(detailedShape.getState().compareTo(state) < 0)
                                state = detailedShape.getState();
                }
                return retval;
        }
/**
 * Gets the detailed shapes hash table.  This is keyed by the shapes current vegetative state with the value as the detailed shape.  
 * 
 * @return the hash table of detailed shapes
 */
        public Hashtable getDetailedShapes(){
                return detailedShapes;
        }

        
        /**
         * Calculates the top left corner and bottom right corner of a Rectangle.  Detailed shapes is a hastable with key value being the shapes current veg state and value is a detailed shape.
         * If has only one element will be caught in first conditional.  The top left and bottom right will both be set to the shapes x and y.  If there is more than one element 
         * the top left will be the least x coordinate and y coordinate.  The bottom left X will be the greatest (x coordinate + the shape width) and Y will be greatest (y coordinate +the shape width)
         * @return an rectangle with 2 elements, the first element is the top left corner and the 2nd element is the bottom right corner
         * 
         */
        
        
        public Rectangle getCorners()
        {
                Point topLeft = null, bottomRight = null;
                String key;
                PathwayShape shape;
                Enumeration e = detailedShapes.keys();
                if(e.hasMoreElements())
                {
                        key = (String)e.nextElement();
                        shape = (PathwayShape)detailedShapes.get(key);
                        topLeft = new Point(shape.x, shape.y);
                        bottomRight = new Point(shape.x, shape.y);
                }
                while(e.hasMoreElements()) {
                        key = (String)e.nextElement();
                        shape = (PathwayShape)detailedShapes.get(key);
                        if(shape.x < topLeft.x)
                                topLeft.x = shape.x;
                        if(shape.y < topLeft.y)
                                topLeft.y = shape.y;
                        if((shape.x + shape.width) > bottomRight.x)
                                bottomRight.x = shape.x + shape.width;
                        if((shape.y + shape.height) > bottomRight.y)
                                bottomRight.y = shape.y + shape.height;
                }
                return new Rectangle(topLeft, new Dimension(bottomRight.x-topLeft.x, bottomRight.y-topLeft.y));
        }
/**
 * Checks if a shape is in the same vegetative state as this collapsed pathway shape 
 * @param inState
 * @return
 */
        public boolean isRelated(PathwayShape inState)
        {
                boolean retval = false;
                VegetativeType inVegType = inState.getState();
                if (!(inState instanceof CollapsedPathwayShape) &&
                                state.getSpecies().equals(inVegType.getSpecies()) &&
                                state.getSizeClass().equals(inVegType.getSizeClass()) &&
                                state.getDensity().equals(inVegType.getDensity()))
                          retval = true;
                return retval;
        }
/**
 * Sets the position of a species in this Collapsed Pathway shape and sets the color of the shape to collapsed_color.
 */
        public void setPosition(Species species) {
                super.setPosition(species);
                normalColor = COLLAPSED_COLOR;
        }
/**
 * Gets the next vegetative state for a process within this shape.  
 */
        public VegetativeType getNextState(Process p) {
                VegetativeType curState = state;
                VegetativeType nextState = curState.getProcessNextState(p);
                Hashtable statesAlreadyViewed = new Hashtable();
                statesAlreadyViewed.put(curState.getCurrentState(), curState);
                while(nextState!=null && !curState.equals(nextState) &&
                                        detailedShapes.get(nextState.getCurrentState())!=null &&
                                        statesAlreadyViewed.get(nextState.getCurrentState())==null)
                {
                        statesAlreadyViewed.put(nextState.getCurrentState(), nextState);
                        curState = nextState;
                        nextState = curState.getProcessNextState(p);
                }
                if(statesAlreadyViewed.get(nextState.getCurrentState())!=null)
                        return state;
                else
                  return nextState;
        }
/**
 * Gets the name of vegetative state within this shape.  This will be a string with state species +" /" + age + ":" + max age +"/" + density
 */
        public String getStateName() {
          return state.getSpecies() + "/" +
              state.getSizeClass() + state.getAge() + ":" +
              Integer.toString(getMaxAge()) + "/" + state.getDensity();
        }
/**
 * Calculates max age of shapes in the detailed shapes hash table.
 * @return the maximum age of vegetative state within the detailed shape.  
 */
        private int getMaxAge() {
                int retval = state.getAge();
                Enumeration e = detailedShapes.keys();
                Object key;
                PathwayShape shape;

                while(e.hasMoreElements())
                {
                        key = e.nextElement();
                        shape = (PathwayShape)detailedShapes.get(key);
                        if(shape.getState().getAge()>retval)
                                retval = shape.getState().getAge();
                }
                return retval;
        }
/**
 * Collapses all the shapes in current shape into a Collapsed Pathway Shape.
 * @param inStates
 */
        public static void collapseAll(Hashtable inStates) {
                String key;
                PathwayShape shape;
                Enumeration e;
                boolean stillCollapsing = true;
                while(stillCollapsing)
                {
                        stillCollapsing = false;
                        e = inStates.keys();
                  while (e.hasMoreElements()) {
                         key   = (String) e.nextElement();
                         shape = (PathwayShape) inStates.get(key);
                         if(!(shape instanceof CollapsedPathwayShape))
                         {
                           collapse(shape, inStates);
                           stillCollapsing = true;
                           break;
                         }
                  }
                }
        }
/**
 * Takes a hash table of all the shapes in shape and puts all the detailed shapes into it.
 * @param inStates
 */
        public static void detailAll(Hashtable inStates) {
                String key;
                PathwayShape shape;
                Enumeration e;
                e = inStates.keys();
          while (e.hasMoreElements()) {
                 key   = (String) e.nextElement();
                 shape = (PathwayShape) inStates.get(key);
                 if(shape instanceof CollapsedPathwayShape)
                 {
                         inStates.remove(key);
                   inStates.putAll(((CollapsedPathwayShape)shape).getDetailedShapes());
                 }
          }
        }
/**
 * Makes a collapsed pathway shape from the current shape and any shapes that are in it.
 * @param curShape
 * @param inStates
 * @return
 */
        public static CollapsedPathwayShape collapse(PathwayShape curShape, Hashtable inStates)
        {
                String key;
                PathwayShape shape;
                CollapsedPathwayShape collapsedShape = null;
                if(!(curShape instanceof CollapsedPathwayShape)) {
                        collapsedShape = new CollapsedPathwayShape(curShape);
                        Enumeration e = inStates.keys();

                        while (e.hasMoreElements()) {
                          key   = (String) e.nextElement();
                          shape = (PathwayShape) inStates.get(key);
                                if(collapsedShape.addDetailShape(shape))
                                inStates.remove(key);
                        }
                        inStates.put(collapsedShape.getState().getCurrentState(), collapsedShape);
                }
                return collapsedShape;
        }

}

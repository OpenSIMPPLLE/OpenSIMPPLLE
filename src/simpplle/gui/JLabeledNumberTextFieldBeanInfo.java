/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Image;
import java.beans.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates JLabeledNumberTextFieldBeanInfo, a SimpleBeanInfo.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 * @since Simpplle v2.4
 *   
 *  @see java.beans.SimpleBeanInfo
 */
public class JLabeledNumberTextFieldBeanInfo extends SimpleBeanInfo {
  Class beanClass = JLabeledNumberTextField.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public JLabeledNumberTextFieldBeanInfo() {
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _value = new PropertyDescriptor("value", beanClass,
          "getValue", "setValue");

      PropertyDescriptor[] pds = new PropertyDescriptor[] {
                                 _value
      };
      return pds;
    }
    catch (Exception exception) {
      exception.printStackTrace();
      return null;
    }
  }
/**
 * Method to get the o,age based on dimensions.  
 */
  public Image getIcon(int iconKind) {
    switch (iconKind) {
      case BeanInfo.ICON_COLOR_16x16:
        return ((iconColor16x16Filename != null) ?
                loadImage(iconColor16x16Filename) : null);

      case BeanInfo.ICON_COLOR_32x32:
        return ((iconColor32x32Filename != null) ?
                loadImage(iconColor32x32Filename) : null);

      case BeanInfo.ICON_MONO_16x16:
        return ((iconMono16x16Filename != null) ?
                loadImage(iconMono16x16Filename) : null);

      case BeanInfo.ICON_MONO_32x32:
        return ((iconMono32x32Filename != null) ?
                loadImage(iconMono32x32Filename) : null);
    }

    return null;
  }

  public BeanInfo[] getAdditionalBeanInfo() {
    Class superclass = beanClass.getSuperclass();
    try {
      BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
      return (new BeanInfo[] {superBeanInfo});
    }
    catch (IntrospectionException introspectionException) {
      introspectionException.printStackTrace();
      return null;
    }
  }
}

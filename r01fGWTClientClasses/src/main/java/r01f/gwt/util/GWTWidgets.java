package r01f.gwt.util;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

public class GWTWidgets {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	public static void removeAllWidgetsFrom(final HasWidgets container) {
		Iterator<Widget> widgetIterator = container.iterator();
		for (Widget w = widgetIterator.next(); widgetIterator.hasNext(); widgetIterator.next()) {
			container.remove(w);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds a dependant style name
	 * @param style the style to be added
	 * @param widgets the widgets where the style is to be added
	 */
	public static void addStyleDependentName(final String style,final Widget... widgets) {
		_setDependantStyleName(style,true,widgets);
	}
	/**
	 * Removes a dependant style name
	 * @param style the style to remove
	 * @param widgets the widgets where the style is to be added
	 */
	public static void removeDependantStyleName(final String style,final Widget... widgets) {
		_setDependantStyleName(style,false,widgets);
	}
	/**
	 * Adds or remove a dependant style name
	 * The style name is determined by: getStylePrimaryName() + '-' + styleSuffix
	 * @param styleSuffix the suffix added to the primary style
	 * @param add true if the style is to be added, false if is to be removed
	 * @param widgets the widgets where the change has to be done
	 */
	private static void _setDependantStyleName(final String styleSuffix,final boolean add,final Widget... widgets) {
		if (widgets != null && widgets.length > 0) {
			for (Widget w : widgets) {
				if (w != null) w.setStyleDependentName(styleSuffix,add);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds a primary style name
	 * @param style the style to add
	 * @param widgets the widgets where the style is to be added
	 */
	public static void addPrimaryStyleName(final String style,final Widget... widgets) {
		_setPrimaryStyleName(style,false,widgets);
	}
	/**
	 * Removes a primary style name
	 * @param style the style to remove
	 * @param widgets the widgets where the style is to be added
	 */
	public static void removePrimaryStyleName(final String style,final Widget... widgets) {
		_setPrimaryStyleName(style,false,widgets);
	}
	/**
	 * adds or removes a primary style name
	 * @param style the style to be added
	 * @param add true if the style is to be added, false if is to be removed
	 * @param widgets the widgets where the change has to be done
	 */
	private static void _setPrimaryStyleName(final String style,final boolean add,final Widget... widgets) {
		if (widgets != null && widgets.length > 0) {
			for (Widget w : widgets) {
				if (w != null) w.setStyleName(style,add);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the horizontal alignment for a widget collection
	 * @param align 
	 * @param widgets 
	 */
	public static void setHorizontalAlignment(final HorizontalAlignmentConstant align,final HasHorizontalAlignment... widgets) {
		if (widgets != null && widgets.length > 0) {
			for (HasHorizontalAlignment w : widgets) {
				if (w != null) w.setHorizontalAlignment(align);
			}
		}
	}
	/**
	 * Sets the width for a widget collection
	 * @param width 
	 * @param widgets 
	 */
	public static void setWidth(final String width,final UIObject... widgets) {
		if (width != null && widgets != null && widgets.length > 0) {
			for (UIObject w : widgets) {
				if (w != null) w.setWidth(width);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the date format in multiple date-picker widgets
	 * @param fmt the date format as a {@link DefaultFormat} instance
	 * @param boxes the {@link DateBox} widgets
	 */
	public static void setDateBoxFormat(final DateBox.Format fmt,final DateBox... boxes) {
		DateBox.Format theFmt = fmt == null ? new DateBox.DefaultFormat() : fmt; 
		if (boxes != null) {
			for (DateBox box : boxes) {
				box.setFormat(theFmt);
			}
		}
	}
	/**
	 * Sets the date format in multiple date-picker widgets
	 * @param pattern the pattern for the dates
	 * @param boxes the {@link DateBox} widgets
	 */
	public static void setDateBoxFormat(final String pattern,final DateBox... boxes) {
		String thePattern = pattern == null ? "dd/MM/yyyy" : pattern;
		DateTimeFormat fmt = DateTimeFormat.getFormat(thePattern);
		DateBox.DefaultFormat theFmt = new DateBox.DefaultFormat(fmt);
		GWTWidgets.setDateBoxFormat(theFmt,boxes);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the focus() event handler in many widgets
	 * @param handler the handler
	 * @param widgets the widgets
	 */
	public static void addFocusHandler(final FocusHandler handler,final HasFocusHandlers... widgets) {
		if (handler != null && widgets != null && widgets.length > 0) {
			for (HasFocusHandlers w : widgets) {
				if (w != null) w.addFocusHandler(handler);
			}
		}
	}
	/**
	 * Sets the KeyDown event handler in many widgets
	 * @param handler the handler
	 * @param widgets the widgets
	 */
	public static void addKeyDownHandler(final KeyDownHandler handler,final HasKeyDownHandlers... widgets) {
		if (handler != null && widgets != null && widgets.length > 0) {
			for (HasKeyDownHandlers w : widgets) {
				if (w != null) w.addKeyDownHandler(handler);
			}
		}
	}
	/**
	 * Sets the KeyPress() event handler in many widgets
	 * @param handler the handler
	 * @param widgets the widgets
	 */
	public static void addKeyPressHandler(final KeyPressHandler handler,final HasKeyPressHandlers... widgets) {
		if (handler != null && widgets != null && widgets.length > 0) {
			for (HasKeyPressHandlers w : widgets) {
				if (w != null) w.addKeyPressHandler(handler);
			}
		}
	}
	/**
	 * Sets the KeyUp() event handler in many widgets
	 * @param handler the handler
	 * @param widgets the widgets
	 */
	public static void addKeyUpHandler(final KeyUpHandler handler,final HasKeyUpHandlers... widgets) {
		if (handler != null && widgets != null && widgets.length > 0) {
			for (HasKeyUpHandlers w : widgets) {
				if (w != null) w.addKeyUpHandler(handler);
			}
		}
	}
	/**
	 * Sets the Change() event handler in many widgets
	 * @param handler the handler
	 * @param widgets the widgets
	 */
	public static void addChangeHandler(final ChangeHandler handler,final HasChangeHandlers... widgets) {
		if (handler != null && widgets != null && widgets.length > 0) {
			for (HasChangeHandlers w : widgets) {
				if (w != null) w.addChangeHandler(handler);
			}
		}
	}
	/**
	 * Sets the ValueChange() event handler in many widgets
	 * @param handler the handler
	 * @param widgets the widgets
	 */
	public static <T> void addValueChangeHandler(final ValueChangeHandler<T> handler,final HasValueChangeHandlers<T>... widgets) {
		if (handler != null && widgets != null && widgets.length > 0) {
			for (HasValueChangeHandlers<T> w : widgets) {
				if (w != null) w.addValueChangeHandler(handler);
			}
		}
	}
}

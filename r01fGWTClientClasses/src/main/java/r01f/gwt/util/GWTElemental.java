package r01f.gwt.util;

import java.util.ArrayList;
import java.util.Collection;

import r01f.util.types.collections.CollectionUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;


public class GWTElemental {
	/**
	 * Gets the child {@link Element}s of another
	 * @param el
	 * @return
	 */
	public static Collection<Element> getChildElements(final Element el) {
		if (el == null || DOM.getChildCount(el) == 0) return null;
		
		Collection<Element> outEls = new ArrayList<Element>(DOM.getChildCount(el));
		for (int i=0; i < DOM.getChildCount(el); i++) {
			outEls.add(DOM.getChild(el,i));
		}
		return outEls;
	}
	/**
	 * Gets the child {@link Element}s of another with a certain type
	 * @param el
	 * @param type
	 * @return
	 */
	public static <E extends Element> Collection<E> getChildElementsOfType(final Element el,
																		   final Class<E> type) {
		if (el == null) return null;
		Collection<Element> childEls = GWTElemental.getChildElements(el);
		return GWTElemental.filterElementsOfType(childEls,
												 type);
	}
	/**
	 * Gets the child {@link Element}s of another with a certain type
	 * @param el
	 * @param type
	 * @param className
	 * @return
	 */
	public static <E extends Element> Collection<E> getChildElementsOfTypeWithClass(final Element el,
																		   			final Class<E> type,final String className) {
		if (el == null) return null;
		Collection<Element> childEls = GWTElemental.getChildElements(el);
		return GWTElemental.filterElementsOfTypeWithClass(childEls,
												 		  type,className);
	}
	/**
	 * Disables all the provided inputEls
	 * @param inputEls
	 */
	public static void disableInputs(final Collection<InputElement> inputEls) {
		if (CollectionUtils.isNullOrEmpty(inputEls)) return;
		for (InputElement inputEl : inputEls) {
			inputEl.setChecked(false);	// ?¿ this is NOT the same as getValue
			inputEl.setDisabled(true);
		}
	}
	/**
	 * Enables all the provided inputEls
	 * @param inputEls
	 */
	public static void enableInputs(final Collection<InputElement> inputEls) {
		if (CollectionUtils.isNullOrEmpty(inputEls)) return;
		for (InputElement inputEl : inputEls) {
			inputEl.setChecked(true);	// ?¿ this is NOT the same as getValue
			inputEl.setDisabled(false);
		}
	}
	@SuppressWarnings("unchecked")
	public static <E extends Element> Collection<E> filterElementsOfType(final Collection<Element> els,
																  		 final Class<E> type) {
		return (Collection<E>)Collections2.filter(els,
								   				  new Predicate<Element>() {
															@Override
															public boolean apply(final Element el) {
																return el.getClass().equals(type);
															}
												  });
	}
	@SuppressWarnings("unchecked")
	public static <E extends Element> Collection<E> filterElementsOfTypeWithClass(final Collection<Element> els,
																  		 		  final Class<E> type,final String className) {
		return (Collection<E>)Collections2.filter(els,
								   				  new Predicate<Element>() {
															@Override
															public boolean apply(final Element el) {
																return el.getClass().equals(type) && el.hasClassName(className);
															}
												  });
	}
	
}

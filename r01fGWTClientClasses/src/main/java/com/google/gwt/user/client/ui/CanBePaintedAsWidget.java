package com.google.gwt.user.client.ui;

import r01f.view.CanBePainted;
import r01f.view.ViewObject;

/**
 * Interface for types that transforms from a {@link CanBePainted} {@link ViewObject} (ie R01MStructureLabel)
 * to a GWT {@link Widget}
 * For example, a R01MStructureLabel is transformed to a {@link SimpleLabel} widget (a text)
 */
public interface CanBePaintedAsWidget<T extends CanBePainted> {
	/**
	 * Transforms a {@link CanBePainted} {@link ViewObject} to a GWT {@link Widget}
	 * @param canBePainted
	 * @return
	 */
	public Widget toWidget(T canBePainted);
}

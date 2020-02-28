package com.google.gwt.user.client.ui;

import lombok.RequiredArgsConstructor;
import r01f.view.CanBePainted;
import r01f.view.ViewObject;

/**
 * Transforms a {@link CanBePainted} {@link ViewObject} that also {@link HasCaption} to a {@link SimpleLabel} GWT {@link Widget}
 */
@RequiredArgsConstructor
public class CanBePaintedAsLabel<T extends CanBePainted>
  implements CanBePaintedAsWidget<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final String _defaultValue;

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SimpleLabel toWidget(final T canBePainted) {
		if (canBePainted != null) assert(canBePainted instanceof r01f.model.facets.view.HasCaption);

		String labelText = null;

		// Get the ViewObject as a HasCaption object and access the label
		if (canBePainted != null) {
			r01f.model.facets.view.HasCaption hasCaption = (r01f.model.facets.view.HasCaption)canBePainted;
			labelText = hasCaption.getCaption();
		}
		labelText = labelText != null ? labelText : _defaultValue;

		return new SimpleLabel(labelText);
	}

}

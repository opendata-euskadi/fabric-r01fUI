package com.google.gwt.user.client.ui;

import java.util.Collection;

import com.google.gwt.dom.client.InputElement;

import lombok.RequiredArgsConstructor;
import r01f.gwt.util.GWTElemental;
import r01f.view.CanBeDisabled;

@RequiredArgsConstructor
     class ViewEnableDisableDelegate
implements CanBeDisabled {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    private final UIObject _uiObj;
    private boolean _disabled = false;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void enable() {
		_disabled = false;
		_uiObj.getElement().removeClassName("disabled");
		_uiObj.getElement().addClassName("enabled");		// enable the element

		// Enable all input elements
		Collection<InputElement> childInputEls = GWTElemental.getChildElementsOfTypeWithClass(_uiObj.getElement(),
																					 		  InputElement.class,TreeViewUtils.CHECKER_CLASS_NAME);
		GWTElemental.enableInputs(childInputEls);
	}
	@Override
	public void disable() {
		_disabled = true;
		_uiObj.getElement().removeClassName("enabled");
		_uiObj.getElement().addClassName("disabled");	// disable the element

		// Disable all input elements
		Collection<InputElement> childInputEls = GWTElemental.getChildElementsOfTypeWithClass(_uiObj.getElement(),
																					 		  InputElement.class,TreeViewUtils.CHECKER_CLASS_NAME);
		GWTElemental.disableInputs(childInputEls);
	}
	@Override
	public boolean isEnabled() {
		return !this.isDisabled();
	}
	@Override
	public boolean isDisabled() {
		return _disabled;
	}
}

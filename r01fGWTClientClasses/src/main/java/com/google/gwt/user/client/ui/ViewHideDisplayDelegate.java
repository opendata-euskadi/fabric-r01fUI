package com.google.gwt.user.client.ui;

import lombok.RequiredArgsConstructor;
import r01f.view.CanBeHidden;

@RequiredArgsConstructor
     class ViewHideDisplayDelegate
implements CanBeHidden {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
    private final UIObject _uiObj;
    private boolean _hidden = false;
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void hide() {
		_hidden = true;
		_uiObj.setVisible(false);
		//_uiObj.getElement().addClassName("hide");		// hide the element
	}
	@Override
	public void display() {
		_hidden = false;
		_uiObj.setVisible(true);
		//_uiObj.getElement().removeClassName("display");	// display the element
	}
	@Override
	public boolean isVisible() {
		return !this.isHidden();
	}
	@Override
	public boolean isHidden() {
		return _hidden;
	}
}

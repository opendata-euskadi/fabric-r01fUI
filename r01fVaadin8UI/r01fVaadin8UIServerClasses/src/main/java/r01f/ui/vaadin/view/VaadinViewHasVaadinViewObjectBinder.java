package r01f.ui.vaadin.view;

import r01f.ui.vaadin.viewobject.VaadinViewHasViewObject;
import r01f.ui.vaadin.viewobject.VaadinViewObject;

public interface VaadinViewHasVaadinViewObjectBinder<M extends VaadinViewObject>
  		 extends VaadinViewHasViewObject<M> {	// if binding the view object...
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public void bindViewTo(final M obj);
	
	public boolean writeBeanIfValid(final M viewObject);
}

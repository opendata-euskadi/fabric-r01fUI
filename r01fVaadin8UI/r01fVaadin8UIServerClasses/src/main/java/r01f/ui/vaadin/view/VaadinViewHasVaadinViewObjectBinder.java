package r01f.ui.vaadin.view;

import r01f.ui.viewobject.UIViewHasViewObject;
import r01f.ui.viewobject.UIViewObject;

public interface VaadinViewHasVaadinViewObjectBinder<M extends UIViewObject>
  		 extends UIViewHasViewObject<M> {	// if binding the view object...
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public void bindViewTo(final M obj);
	
	public boolean writeBeanIfValid(final M viewObject);
	
	@Override
	public default void setViewObject(final M viewObject) {
		this.writeBeanIfValid(viewObject);
	}
}

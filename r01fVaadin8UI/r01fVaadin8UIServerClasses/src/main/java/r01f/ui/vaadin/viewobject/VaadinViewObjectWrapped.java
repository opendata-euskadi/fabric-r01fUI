package r01f.ui.vaadin.viewobject;

import r01f.model.ModelObject;

public interface VaadinViewObjectWrapped<T extends ModelObject> 
		 extends VaadinViewObject {
	
	public T getWrappedModelObject();
}

package r01f.ui.viewobject;

import r01f.model.ModelObject;

public interface UIViewObjectWrapped<T extends ModelObject> 
		 extends UIViewObject {
	
	public T getWrappedModelObject();
}

package r01f.ui.viewobject;

public interface UIViewObjectWrapped<T> 
		 extends UIViewObject {
	
	public T getWrappedModelObject();
}

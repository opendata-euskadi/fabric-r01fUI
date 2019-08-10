package r01f.ui.viewobject;

import r01f.model.search.SearchFilter;

public interface UIViewObjectForSearchFilter<F extends SearchFilter>
		 extends UIViewObjectWrapped<F> {
	public F getWrappedSearchFilter();
	// public default F getWrappedSearchFilter() {
	//		return this.getWrappedObject();
	// }
	
}

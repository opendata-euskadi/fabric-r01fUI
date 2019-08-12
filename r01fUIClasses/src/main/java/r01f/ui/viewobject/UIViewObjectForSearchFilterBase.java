package r01f.ui.viewobject;

import lombok.experimental.Accessors;
import r01f.model.search.SearchFilter;

@Accessors(prefix="_")
public abstract class UIViewObjectForSearchFilterBase<F extends SearchFilter> 
			  extends UIViewObjectBase<F>
           implements UIViewObjectForSearchFilter<F> {

	private static final long serialVersionUID = 1141228509063913807L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public UIViewObjectForSearchFilterBase(final F searchFilter) {
		super(searchFilter);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public F getWrappedSearchFilter() {
		return this.getWrappedModelObject();
	}
}

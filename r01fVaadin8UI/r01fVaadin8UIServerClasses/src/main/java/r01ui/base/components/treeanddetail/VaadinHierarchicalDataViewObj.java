package r01ui.base.components.treeanddetail;

import r01f.facets.HasID;
import r01f.facets.LangInDependentNamed;
import r01f.facets.LangInDependentNamed.HasLangInDependentNamedFacet;
import r01f.guids.OID;
import r01ui.base.components.tree.VaadinTreeNodeID;

public interface VaadinHierarchicalDataViewObj<SELF_TYPE extends VaadinHierarchicalDataViewObj<SELF_TYPE>> 
		 extends HasID<VaadinTreeNodeID>,		// an id is needed to match the edited [view obj] with a [tree view obj]
				 HasLangInDependentNamedFacet {
/////////////////////////////////////////////////////////////////////////////////////////
//	TRANSFER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	public void copyDataFrom(SELF_TYPE other);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	default LangInDependentNamed asLangInDependentNamed() {
		throw new UnsupportedOperationException();
	}
	@Override
	default void setName(final String name) {
		throw new UnsupportedOperationException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	default void unsafeSetId(final OID id) {
		throw new UnsupportedOperationException();
	}
}

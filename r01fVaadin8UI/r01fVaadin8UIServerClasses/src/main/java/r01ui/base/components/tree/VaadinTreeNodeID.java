package r01ui.base.components.tree;

import com.vaadin.ui.TreeGrid;

import r01f.guids.OIDBaseImmutable;
import r01f.guids.OIDs;

/**
 * A convenience id used to identify an item at a {@link TreeGrid}
 * Sometimes an id is needed to find an item at the grid, maybe because the [view object] being "painted" 
 * at the [tree] does NOT have an id
 */
public class VaadinTreeNodeID 
	 extends OIDBaseImmutable<String> {

	private static final long serialVersionUID = 4282161562912118936L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	protected VaadinTreeNodeID(final String id) {
		super(id);
	}
	public static final VaadinTreeNodeID valueOf(final String id) {
		return new VaadinTreeNodeID(id);
	}
	public static final VaadinTreeNodeID forId(final String id) {
		return new VaadinTreeNodeID(id);
	}
	public static final VaadinTreeNodeID supplyWithRandomOid() {
		return VaadinTreeNodeID.forId(OIDs.supplyOid());
	}
}

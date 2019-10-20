package r01ui.base.components.tree;

import java.io.Serializable;

import com.google.common.base.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.guids.OID;

/**
 * A vaadin's tree grid item
 * @param <O>
 */
@Accessors(prefix="_")
@RequiredArgsConstructor
public class VaadinTreeItem<O extends OID>
  implements Serializable {

	private static final long serialVersionUID = 2088054272092288877L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final O _oid;
	@Getter private final String _caption;
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS & HASHCODE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof VaadinTreeItem)) return false;
		VaadinTreeItem<O> otherItem = (VaadinTreeItem<O>)obj;
		return _oid.is(otherItem.getOid());
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(_oid);
	}
}

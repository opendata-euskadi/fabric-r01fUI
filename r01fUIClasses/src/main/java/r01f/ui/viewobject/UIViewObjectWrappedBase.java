package r01f.ui.viewobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.facets.HasOID;
import r01f.types.dirtytrack.DirtyTrackAdapter;

@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class UIViewObjectWrappedBase<T>
           implements UIViewObjectWrapped<T> {

	private static final long serialVersionUID = 5054336695644200270L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final transient T _wrappedModelObject;

/////////////////////////////////////////////////////////////////////////////////////////
//	CAST
/////////////////////////////////////////////////////////////////////////////////////////
//	@SuppressWarnings("unchecked")
//	public <C extends T> C getWrappedModelObjectAs(final Class<C> type) {
//		return (C)_wrappedModelObject;
//	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS & HASHCODE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public boolean equals(final Object other) {
		if (other == null) return false;
		if (this == other) return true;
		if (this.getClass() != other.getClass()) return false;

		boolean outEq = false;
		UIViewObjectWrapped<T> otherWrapped = (UIViewObjectWrapped<T>)other;
		if (this.getWrappedModelObject() instanceof HasOID<?>
		 && otherWrapped.getWrappedModelObject() instanceof HasOID<?>) {
			HasOID<?> thisHasOid = (HasOID<?>)this.getWrappedModelObject();
			HasOID<?> otherHasOid = (HasOID<?>)otherWrapped.getWrappedModelObject();

			outEq = thisHasOid.getOid() != null
						? thisHasOid.getOid().is(otherHasOid.getOid())
						: otherHasOid.getOid() != null
								? false						// this oid = null / other oid != null
								: this.getWrappedModelObject().equals(otherWrapped.getWrappedModelObject());		// both oid null
		}
		return outEq;
	}
	@Override
	public int hashCode() {
		int outHash = 0;
		if (this.getWrappedModelObject() instanceof HasOID<?>) {
			HasOID<?> thisHasOid = (HasOID<?>)this.getWrappedModelObject();
			if (thisHasOid.getOid() != null) {
				outHash = thisHasOid.getOid().hashCode();
			} else {
				outHash = this.getWrappedModelObject().hashCode();
			}
		} else {
			outHash = super.hashCode();
		}
		return outHash;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CHECK NEW & DIRRY
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isNew() {
		boolean newObj = DirtyTrackAdapter.adapt(_wrappedModelObject)
										  .getTrackingStatus()
										  .isThisNew();
		return newObj;
	}
	public boolean isNOTNew() {
		return !this.isNew();
	}
	public boolean isDirty() {
		boolean dirty = DirtyTrackAdapter.adapt(_wrappedModelObject)
										 .getTrackingStatus()
										 .isThisDirty();
		return dirty;
	}
	public boolean isNOTDirty() {
		return !this.isDirty();
	}
}

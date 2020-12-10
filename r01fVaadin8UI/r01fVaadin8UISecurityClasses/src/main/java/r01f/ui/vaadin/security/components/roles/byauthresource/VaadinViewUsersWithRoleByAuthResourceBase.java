package r01f.ui.vaadin.security.components.roles.byauthresource;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Accessors(prefix="_")
public class VaadinViewUsersWithRoleByAuthResourceBase<U extends User,V extends VaadinViewUser<U>,
													   I extends VaadinViewUsersWithRoleInAuthResourceBase<U,V,I>,
												  	   SELF_TYPE extends VaadinViewUsersWithRoleByAuthResourceBase<U,V,I,SELF_TYPE>>
  implements UIViewObject {

	private static final long serialVersionUID = -5125194616891915973L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private Collection<I> _byAuthResource;
	@Getter private Collection<I> _deletedAuthResources;	// used to track changes
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewUsersWithRoleByAuthResourceBase(final Collection<I> byAuthResource) {
		_byAuthResource = byAuthResource;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET
/////////////////////////////////////////////////////////////////////////////////////////
	public I getUsersWithRoleInAuthResource(final AuthorizationTargetResourceOID resOid) {
		return this.getUsersWithRoleInAuthResourceMatching(res -> res.getAuthResourceOid().is(resOid));
	}
	public I getUsersWithRoleInAuthResourceMatching(final Predicate<I> filter) {
		return CollectionUtils.hasData(_byAuthResource)
					? _byAuthResource.stream()
							 .filter(filter)
							 .findFirst().orElse(null)
					: null;
	}
	public boolean hasUsersWithRoleInAuthResource(final AuthorizationTargetResourceOID resOid) {
		return this.hasUsersWithRoleMatching(res -> res.getAuthResourceOid().is(resOid));
	}
	public boolean hasUsersWithRoleMatching(final Predicate<I> filter) {
		return this.getUsersWithRoleInAuthResourceMatching(filter) != null;
	}
	public Collection<I> getActualByAuthResource() {
		if (CollectionUtils.isNullOrEmpty(_deletedAuthResources)) return _byAuthResource;

		Collection<AuthorizationTargetResourceOID> deletedResources = _deletedAuthResources.stream()
																				   .map(I::getAuthResourceOid)
																				   .collect(Collectors.toSet());
		return CollectionUtils.hasData(_byAuthResource) ? _byAuthResource.stream()
														 .filter(inRes -> inRes.getAuthResourceOid().isNOTContainedIn(deletedResources))
														 .collect(Collectors.toList())
												: null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ADD
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public SELF_TYPE addUsersWithRoleInArea(final Collection<I> inRes) {
		if (CollectionUtils.isNullOrEmpty(inRes)) return (SELF_TYPE)this;
		inRes.forEach(res -> this.addUsersWithRoleInArea(res));
		return (SELF_TYPE)this;
	}
	@SuppressWarnings("unchecked")
	public SELF_TYPE addUsersWithRoleInArea(final I inRes) {
		if (inRes == null) return (SELF_TYPE)this;
		if (_byAuthResource == null) _byAuthResource = Lists.newArrayList();
		_byAuthResource.add(inRes);

		return (SELF_TYPE)this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DELETE
/////////////////////////////////////////////////////////////////////////////////////////
	public I getDeletedUsersWithRoleInAuthResource(final AuthorizationTargetResourceOID resOid) {
		return this.getDeletedUsersWithRoleInResourceMatching(res -> res.getAuthResourceOid().is(resOid));
	}
	public I getDeletedUsersWithRoleInResourceMatching(final Predicate<I> filter) {
		return CollectionUtils.hasData(_deletedAuthResources)
					? _deletedAuthResources.stream()
							 .filter(filter)
							 .findFirst().orElse(null)
					: null;
	}
	public boolean deleteUsersWithRoleInAuthResourceMatching(final Predicate<I> filter) {
		if (CollectionUtils.isNullOrEmpty(_byAuthResource)) return false;
		I existing = this.getUsersWithRoleInAuthResourceMatching(filter);
		boolean removed = existing != null ? _byAuthResource.remove(existing) : false;

		// store the removed area to track changes
		if (_deletedAuthResources == null) _deletedAuthResources = Lists.newArrayList();
		_deletedAuthResources.add(existing);

		return removed;
	}
	public I undeleteUsersWithRoleInAuthResource(final AuthorizationTargetResourceOID resOid) {
		return this.undeleteUsersWithRoleInAuthResourceMatching(res -> res.getAuthResourceOid().is(resOid));
	}
	public I undeleteUsersWithRoleInAuthResourceMatching(final Predicate<I> filter) {
		I deleted = this.getDeletedUsersWithRoleInResourceMatching(filter);
		if (deleted != null) {
			this.addUsersWithRoleInArea(deleted);
			_deletedAuthResources.remove(deleted);
		}
		return deleted;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET
/////////////////////////////////////////////////////////////////////////////////////////
	public void reset() {
		if (CollectionUtils.isNullOrEmpty(_byAuthResource)) return;
		_byAuthResource.forEach(inArea -> inArea.reset());
	}
	public void reset(final Predicate<I> filter) {
		if (CollectionUtils.isNullOrEmpty(_byAuthResource)) return;
		I existing = this.getUsersWithRoleInAuthResourceMatching(filter);
		if (existing != null) existing.reset();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CHANGES SAVED
/////////////////////////////////////////////////////////////////////////////////////////
	public void changesSaved() {
		// remove the deleted ones
		if (_byAuthResource != null
		 && CollectionUtils.hasData(_deletedAuthResources)) {
			Collection<AuthorizationTargetResourceOID> deletedResources = _deletedAuthResources.stream()
																					   .map(I::getAuthResourceOid)
																					   .collect(Collectors.toSet());
			_byAuthResource = _byAuthResource.stream()
							 .filter(inRes -> inRes.getAuthResourceOid().isContainedIn(deletedResources))
							 .collect(Collectors.toList());
		}
		// save changes on the remaining
		if (CollectionUtils.hasData(_byAuthResource)) _byAuthResource.forEach(inArea -> inArea.changesSaved());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HAS CHANGES
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasChanges() {
		if (CollectionUtils.hasData(_deletedAuthResources)) return true;
		if (CollectionUtils.isNullOrEmpty(_byAuthResource)) return false;
		boolean anyChanged = false;
		for (I inRes : _byAuthResource) {
			if (inRes.hasChanges()) {
				anyChanged = true;
				break;
			}
		}
		return anyChanged;
	}
}

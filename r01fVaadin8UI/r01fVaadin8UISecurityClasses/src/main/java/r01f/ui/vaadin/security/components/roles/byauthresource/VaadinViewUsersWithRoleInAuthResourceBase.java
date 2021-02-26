package r01f.ui.vaadin.security.components.roles.byauthresource;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Accessors(prefix="_")
public abstract class VaadinViewUsersWithRoleInAuthResourceBase<U extends User,V extends VaadinViewUser<U>,
																SELF_TYPE extends VaadinViewUsersWithRoleInAuthResourceBase<U,V,SELF_TYPE>>
  		   implements UIViewObject,
  			 		  Debuggable {

	private static final long serialVersionUID = -4275543471734819905L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected String _authResourceName;

	@Getter protected final Collection<UserRole> _roles;

	@Getter protected AuthorizationTargetResourceOID _authResourceOid;	// this field is updated when the [area] is new and it's related [auth resource] is effectivelly created

	@Getter protected Map<UserRole,Collection<V>> _originalUsers;
	@Getter protected Map<UserRole,Collection<V>> _usersWithAddedRole;
	@Getter protected Map<UserRole,Collection<V>> _usersWithRemovedRole;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewUsersWithRoleInAuthResourceBase(final AuthorizationTargetResourceOID authResourceOid,final String authResourceName,
													 final UserRole... roles) {
		this(authResourceOid,authResourceName,
			 Lists.newArrayList(roles));
	}
	public VaadinViewUsersWithRoleInAuthResourceBase(final AuthorizationTargetResourceOID authResourceOid,final String authResourceName,
													 final Collection<UserRole> roles) {
		_authResourceOid = authResourceOid;
		_authResourceName = authResourceName;

		_roles = roles;

		_originalUsers = Maps.newHashMap();
		_usersWithAddedRole = Maps.newHashMap();
		_usersWithRemovedRole = Maps.newHashMap();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * when the [auth resource] is NEW, it's oid is NULL
	 * ... once the [auth resource] is SAVED, the oid can be set
	 * @param resOid
	 */
	public void setAuthResourceOid(final AuthorizationTargetResourceOID resOid) {
		_authResourceOid = resOid;
	}
	
	public void setAuthResourceName(final String resName) {
		_authResourceName = resName;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET
/////////////////////////////////////////////////////////////////////////////////////////
	public void reset() {
		_usersWithAddedRole.clear();

		_usersWithRemovedRole.clear();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CHANGES SAVED
/////////////////////////////////////////////////////////////////////////////////////////
	public void changesSaved() {
		if (CollectionUtils.isNullOrEmpty(_originalUsers)) return;

		_originalUsers = _roles.stream()
							   .map(role -> {
								  		Collection<V> usersBeforeSave = _originalUsers.get(role);

								  		// get the changes
								  		Collection<V> newUsersWithRoleAdded = _usersWithAddedRole.get(role);
										Collection<V> usersWithRemovedRole = _usersWithRemovedRole.get(role);

										// get the state after commiting the changes
										Collection<V> usersAfterSave = _changesSaved(usersBeforeSave,
						  									  									 newUsersWithRoleAdded,usersWithRemovedRole);
										// the changes were commited
										if (_usersWithAddedRole != null && _usersWithAddedRole.get(role) != null) _usersWithAddedRole.get(role).clear();
										if (_usersWithRemovedRole != null && _usersWithRemovedRole.get(role) != null) _usersWithRemovedRole.get(role).clear();

										// return the new state
										return new VsWithRole(role,usersAfterSave);
							   		})
							   .collect(Collectors.toMap(VsWithRole::getUserRole,
									   					 VsWithRole::getUsers));
	}
	@Accessors(prefix="_")
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	private class VsWithRole {
		@Getter private final UserRole _userRole;
		@Getter private final Collection<V> _users;
	}
	private Collection<V> _changesSaved(final Collection<V> original,
							   			final Collection<V> added,final Collection<V> removed) {
		Collection<V> newOriginal = Lists.newArrayList();
		if (CollectionUtils.hasData(added)) newOriginal.addAll(added);
		if (original != null
		 && CollectionUtils.hasData(removed)) {
			// add not removed
			Collection<UserOID> removedOids = removed.stream()
													 .map(V::getOid)
													 .collect(Collectors.toSet());
			newOriginal.addAll(original.stream()
									   .filter(viewUser -> viewUser.getOid().isNOTContainedIn(removedOids))
									   .collect(Collectors.toList()));
		} else if (original != null) {
			// nothing removed
			newOriginal.addAll(original);
		}
		return newOriginal != null ? newOriginal : Lists.newArrayList();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COUNT
/////////////////////////////////////////////////////////////////////////////////////////
	public int countActualUsersWithRole(final UserRole role) {
		Collection<V> actualUsers = this.getActualUsersWithRole(role);
		return CollectionUtils.hasData(actualUsers) ? actualUsers.size() : 0;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSOLIDATED
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<V> getActualUsersWithRole(final UserRole role) {
		Collection<V> original = _originalUsers != null ? _originalUsers.get(role) : null;
		Collection<V> added = _usersWithAddedRole != null ? _usersWithAddedRole.get(role) : null;
		Collection<V> removed = _usersWithRemovedRole != null ? _usersWithRemovedRole.get(role) : null;

		return _getActualUsersFrom(original,
								   added,removed);
	}
	private Collection<V> _getActualUsersFrom(final Collection<V> original,
											  final Collection<V> added,final Collection<V> removed) {
		Collection<V> originalWithoutRemoved = CollectionUtils.hasData(removed)
																? CollectionUtils.hasData(original)
																		? original.stream()
																				// filter removed objects
													   							.filter(viewOrigUser -> removed.stream()
													   														   .anyMatch(viewRemovedUser -> viewRemovedUser.getOid().is(viewOrigUser.getOid())))
													   							.collect(Collectors.toList())
																		: null
															   	: original != null ? Lists.newArrayList(original)
															   					   : null;
		Collection<V> out = null;
		if (CollectionUtils.hasData(added)) {
			if (originalWithoutRemoved == null) {
				out = Lists.newArrayList(added);
			} else {
				out = Lists.newArrayList(Iterables.concat(originalWithoutRemoved,
														  added));
			}
		} else if (original != null) {
			out = Lists.newArrayList(original);
		}
		return out;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ORIGINAL
/////////////////////////////////////////////////////////////////////////////////////////
////////// get
	public V getOriginalUserWithRole(final UserOID userOid,
												 final UserRole role) {
		if (_originalUsers == null) return null;

		Collection<V> users = _originalUsers.get(role);

		if (CollectionUtils.isNullOrEmpty(users)) return null;
		return users.stream()
				    .filter(viewUser -> viewUser.getOid().is(userOid))
				    .findFirst().orElse(null);
	}
////////// HAS
	public boolean hasUserWithRole(final UserOID userOid,
								   final UserRole role) {
		return this.getOriginalUserWithRole(userOid,role) != null;
	}
////////// ADD
	public Collection<V> getUsersWithAddedRole(final UserRole role) {
		if (CollectionUtils.isNullOrEmpty(_usersWithAddedRole)) return null;

		return _usersWithAddedRole.get(role);
	}
	@SuppressWarnings("unchecked")
	public SELF_TYPE addOriginalUsersWithRole(final Collection<V> viewUsers,
											  final UserRole role) {
		if (CollectionUtils.isNullOrEmpty(viewUsers)) return (SELF_TYPE)this;
		viewUsers.forEach(viewUser -> this.addOriginalUserWithRole(viewUser,role));
		return (SELF_TYPE)this;
	}
	@SuppressWarnings("unchecked")
	public SELF_TYPE addOriginalUserWithRole(final V viewUser,
											 final UserRole role) {
		if (_originalUsers == null) _originalUsers = Maps.newHashMap();

		Collection<V> users = _originalUsers.get(role);
		if (users == null) {
			users = Lists.newArrayList();
			_originalUsers.put(role,users);
		}
		users.add(viewUser);
		return (SELF_TYPE)this;
	}
////////// REMOVE
	public Collection<V> getUsersWithRemovedRole(final UserRole role) {
		if (CollectionUtils.isNullOrEmpty(_usersWithRemovedRole)) return null;

		return _usersWithRemovedRole.get(role);
	}
	public boolean removeOriginalUserWithRole(final UserOID userOid,
									  		  final UserRole role) {
		if (_originalUsers == null) return false;

		Collection<V> users = _originalUsers.get(role);
		if (users == null) return false;

		V viewUser = this.getOriginalUserWithRole(userOid,
															  role);
		return viewUser != null ? users.remove(viewUser) : false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	TRACK CHANGES
/////////////////////////////////////////////////////////////////////////////////////////
////////// Get Removed
	public boolean hasRemovedUserWithRole(final UserOID userOid,
									   	  final UserRole role) {
		if (_usersWithRemovedRole == null) return false;

		Collection<V> removed = _usersWithRemovedRole.get(role);
		if (removed == null) return false;
		return removed.stream()
					  .filter(viewUser -> viewUser.getOid().is(userOid))
					  .count() > 0;
	}
////////// Add Removed
	public void addRemovedUserWithRole(final V viewUser,
									   final UserRole role) {
		if (_usersWithRemovedRole == null) _usersWithRemovedRole = Maps.newHashMap();

		Collection<V> removed = _usersWithRemovedRole.get(role);
		if (removed == null) {
			removed = Sets.newLinkedHashSet();
			_usersWithRemovedRole.put(role,removed);
		}
		removed.add(viewUser);

		// maybe the user was a previously added NEW user: remove it from the [new users] collection
		Collection<V> added = _usersWithAddedRole != null ? _usersWithAddedRole.get(role) : null;
		if (added != null
		 && added.contains(viewUser)) added.remove(viewUser);
	}
////////// Get Added
	public boolean hasNewUserWithRole(final UserOID userOid,
									  final UserRole role) {
		if (_usersWithAddedRole == null) return false;

		Collection<V> added = _usersWithAddedRole.get(role);
		if (added == null) return false;
		return added.stream()
				    .filter(viewUser -> viewUser.getOid().is(userOid))
				    .count() > 0;
	}
////////// Added
	public void addNewUserWithRole(final V viewUser,
								   final UserRole role) {
		if (_usersWithAddedRole == null) _usersWithAddedRole = Maps.newHashMap();

		Collection<V> added = _usersWithAddedRole.get(role);
		if (added == null) {
			added = Sets.newLinkedHashSet();
			_usersWithAddedRole.put(role,added);
		}
		added.add(viewUser);

		// maybe the user was a previously removed: remove it from the [removed] collection
		Collection<V> removed = _usersWithRemovedRole != null ? _usersWithRemovedRole.get(role) : null;
		if (removed != null
		 && removed.contains(viewUser)) removed.remove(viewUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HAS CHANGES
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasChanges() {
		boolean anyUserRoledAdded = false;
		for (UserRole role : _roles) {
			Collection<V> added = _usersWithAddedRole != null ? _usersWithAddedRole.get(role) : null;
			if (CollectionUtils.hasData(added)) {
				anyUserRoledAdded = true;
				break;
			}
		}
		boolean anyUserRoleRemoved = false;
		for (UserRole role : _roles) {
			Collection<V> added = _usersWithRemovedRole != null ? _usersWithRemovedRole.get(role) : null;
			if (CollectionUtils.hasData(added)) {
				anyUserRoleRemoved = true;
				break;
			}
		}
		return anyUserRoledAdded
			|| anyUserRoleRemoved;
	}
	public boolean isNew() {
		return _authResourceOid == null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		StringBuilder sb = new StringBuilder(_originalUsers.size() * 30);
		_roles.forEach(role -> {
							Collection<V> originals = _originalUsers != null ? _originalUsers.get(role) : null;
							Collection<V> added = _usersWithAddedRole != null ? _usersWithAddedRole.get(role) : null;
							Collection<V> removed = _usersWithRemovedRole != null ? _usersWithRemovedRole.get(role) : null;

							sb.append("-").append(role).append(" > ").append(_debugInfo(originals,added,removed)).append("\n");
					   });
		return sb;
	}
	private String _debugInfo(final Collection<V> original,
							  final Collection<V> added,final Collection<V> removed) {
		return Strings.customized("original > {} | added > {} | removed > {}",
								  original != null ? original.size() : 0,
								  added != null ? added.size() : 0,
								  removed != null ? removed.size() : 0);
	}
}

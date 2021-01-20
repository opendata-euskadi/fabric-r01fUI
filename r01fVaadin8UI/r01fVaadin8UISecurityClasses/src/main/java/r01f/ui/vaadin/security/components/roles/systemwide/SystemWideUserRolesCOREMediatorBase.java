package r01f.ui.vaadin.security.components.roles.systemwide;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.user.User;
import r01f.model.security.user.UserRoleInModule;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.coremediator.UICOREMediatorBase;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Slf4j
public abstract class SystemWideUserRolesCOREMediatorBase<U extends User,
														  A extends SecurityAPIBase<U,?,?,?,?,?,?,?>>
	 		  extends UICOREMediatorBase<A> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final AppComponent _appComponent;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SystemWideUserRolesCOREMediatorBase(final AppComponent appComponent,
											   final A api) {
		super(api);
		_appComponent = appComponent;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads all users with a [system wide role] in a single operation
	 * @return
	 */
	public Map<UserRole,Collection<U>> loadUsersWithSystemWideRole(final UserRole... userRoles) {
		return this.loadUsersWithSystemWideRole(Lists.newArrayList(userRoles));
	}
	/**
	 * Loads all users with a [system wide role] in a single operation
	 * @return
	 */
	public Map<UserRole,Collection<U>> loadUsersWithSystemWideRole(final Collection<UserRole> userRoles) {
		// load all users with system-wide role
		Collection<U> allUsers = _loadUsersWithSystemWideRoleIn(_appComponent);
		log.info("[system-wide uicore mediator] > {} users with a system-wide role",
				 allUsers != null ? allUsers.size() : 0);
		if (CollectionUtils.isNullOrEmpty(allUsers)) return Maps.newHashMap();

		// group by user role
		Map<UserRole,Collection<U>> outMap = Maps.newHashMapWithExpectedSize(userRoles.size());
		userRoles.forEach(userRole -> {
							Collection<U> usersWithRole = _filterUsersWithSystemWideRole(allUsers,
																						 userRole);
							log.info("\t- {} users with a {} system-wide role",
									 usersWithRole != null ? usersWithRole.size() : 0,
									 userRole);
							outMap.put(userRole,
									   usersWithRole);
						  });

		// return
		return outMap;
	}
	protected abstract Collection<U> _loadUsersWithSystemWideRoleIn(final AppComponent appComponent);

	protected Collection<U> _filterUsersWithSystemWideRole(final Collection<U> users,
														   final UserRole role) {
		if (CollectionUtils.isNullOrEmpty(users)) return null;
		return users.stream()
					.filter(user -> user.hasUserSystemWideRoleIn(_appComponent,	// beware!!
												   				 role))
					.collect(Collectors.toList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SAVE
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "null","unused" })
	public void updateUsersWithSystemWideRole(final UserRole role,
											  final Collection<UserOID> addedUsers,final Collection<UserOID> deletedUsers) {
		log.info("[system-wide roles uicore mediator] > update role={} with {} added users and {} deleted users",
				 role,
				 addedUsers != null ? addedUsers.size() : 0,
				 deletedUsers != null ? deletedUsers.size() : 0);

		// [1] - add the [role] to the users
		if (CollectionUtils.hasData(addedUsers)) {
			addedUsers.stream()
					  .forEach(userOid -> {
						  			// load the user
									U existingUser = _api.getUsersApi()
														 .getForCrud()
														 .load(userOid);
									// add the role
									existingUser.addUserSystemWideRoleInModule(new UserRoleInModule(_appComponent,	// beware!! convert to appComponent
																						  			role));
									// update
									U updatedUser = _api.getUsersApi()
															   .getForCrud()
															   .save(existingUser);
							   });
		}
		// [2] - remove the [role] to the users
		if (CollectionUtils.hasData(deletedUsers)) {
			deletedUsers.stream()
						.forEach(userOid -> {
						  			// load the user
									U existingUser = _api.getUsersApi()
														 .getForCrud()
														 .load(userOid);
									// add the role
									existingUser.removeUserSystemWideRoleIn(_appComponent,	// beware!! convert to appComponent
															  				role);

									// update
									U updatedUser = _api.getUsersApi()
													    .getForCrud()
													    .save(existingUser);
							   });
		}
	}
}

package r01f.ui.vaadin.security.components.roles.systemwide;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Slf4j
public abstract class SystemWideUserRolesPresenterBase<U extends User,V extends VaadinViewUser<U>,
													   C extends SystemWideUserRolesCOREMediatorBase<U,
													   												 ? extends SecurityAPIBase<U,?,?,?,?,?>>>
  		   implements UIPresenter {

	private static final long serialVersionUID = 2059320036793300763L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient C _systemWideUserRolesCOREMediator;
	protected final transient FactoryFrom<U,V> _viewUserFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SystemWideUserRolesPresenterBase(final C systemWideUserRolesCOREMediator,
											final FactoryFrom<U,V> viewUserFactory) {
		_systemWideUserRolesCOREMediator = systemWideUserRolesCOREMediator;
		_viewUserFactory = viewUserFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	public void onUsersWithSystemWideRoleNeeded(final Collection<UserRole> userRoles,
												final UIPresenterSubscriber<Map<UserRole,Collection<V>>> presenterSubscriber) {
		try {
			Map<UserRole,Collection<U>> usersByRole = _systemWideUserRolesCOREMediator.loadUsersWithSystemWideRole(userRoles);

			// transform from [model obj] to [view obj]
			Map<UserRole,Collection<V>> viewUsersByRole = Maps.newLinkedHashMap();
			if (CollectionUtils.hasData(usersByRole)) {
				for (Map.Entry<UserRole,Collection<U>> me : usersByRole.entrySet()) {
					Collection<V> viewUsers = CollectionUtils.hasData(me.getValue())
													? me.getValue()
														.stream()
														.map(user -> _viewUserFactory.from(user))
														.collect(Collectors.toList())
													: Lists.newArrayList();
					viewUsersByRole.put(me.getKey(),viewUsers);
				}
			}
			// tell the subscriber
			presenterSubscriber.onSuccess(viewUsersByRole);
		} catch (Throwable th) {
			presenterSubscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UPDATE
/////////////////////////////////////////////////////////////////////////////////////////
	public void onUsersWithSystemWideRoleUpdateRequested(final UserRole role,
														 final Collection<V> viewAddedUsers,final Collection<V> viewDeletedUsers) {
		try {
			Collection<UserOID> addedUsers = CollectionUtils.hasData(viewAddedUsers) ? viewAddedUsers.stream()
																									 .map(V::getOid)
																									 .collect(Collectors.toSet())
																					 : null;
			Collection<UserOID> deletedUsers = CollectionUtils.hasData(viewDeletedUsers) ? viewDeletedUsers.stream()
																										   .map(V::getOid)
																										   .collect(Collectors.toSet())
																					   : null;
			_systemWideUserRolesCOREMediator.updateUsersWithSystemWideRole(role,
																		   addedUsers,deletedUsers);
		} catch (Throwable th) {
			log.error("Error while updating users system-wide roles: {}",
					  th.getMessage(),th);
		}
	}
}

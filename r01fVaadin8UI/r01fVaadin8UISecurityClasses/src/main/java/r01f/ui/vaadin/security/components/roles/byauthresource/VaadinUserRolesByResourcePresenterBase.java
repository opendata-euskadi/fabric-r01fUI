package r01f.ui.vaadin.security.components.roles.byauthresource;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.model.security.authorization.AuthorizationTargetResourceBase;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.summaries.SummarizedAuthorizationResourceBase;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Slf4j
public abstract class VaadinUserRolesByResourcePresenterBase<U extends User,V extends VaadinViewUser<U>,
															 R extends AuthorizationTargetResourceBase<R>,RS extends SummarizedAuthorizationResourceBase<R>,VR extends VaadinViewAuthResource<RS>,
															 O extends UserAuthorizationOnResource,
															 I extends VaadinViewUsersWithRoleInAuthResourceBase<U,V,I>,B extends VaadinViewUsersWithRoleByAuthResourceBase<U,V,I,B>,
															 C extends VaadinUserRolesByAuthResourceCOREMediatorBase<U,R,RS,O,?,?>>
  		   implements UIPresenter {

	private static final long serialVersionUID = 2059320036793300763L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@FunctionalInterface
	protected interface VaadinCreatesViewUsersWithRoleInAuthResource<U extends User,V extends VaadinViewUser<U>,
																	 R extends AuthorizationTargetResourceBase<R>,
																	 I extends VaadinViewUsersWithRoleInAuthResourceBase<U,V,I>> {
		public I createFrom(final R authRes,
							final Map<UserRole,Collection<V>> usersByRole);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient C _coreMediator;

	private final FactoryFrom<U,V> _viewUserFactory;
	private final FactoryFrom<RS,VR> _viewAuthResourceFactory;
	private final VaadinCreatesViewUsersWithRoleInAuthResource<U,V,R,I> _viewUsersWithRoleInAuthResourceFactory;
	private final FactoryFrom<Collection<I>,B> _viewUsersWithRoleByAuthResourceFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUserRolesByResourcePresenterBase(final C userRolesByAreaCoreMediator,
												  final FactoryFrom<U,V> viewUserFactory,final FactoryFrom<RS,VR> viewAuthResourceFactory,
												  final VaadinCreatesViewUsersWithRoleInAuthResource<U,V,R,I> viewUsersWithRoleInResourceFactory,
												  final FactoryFrom<Collection<I>,B> viewUsersWithRoleByResourceFactory) {
		_coreMediator = userRolesByAreaCoreMediator;

		_viewUserFactory = viewUserFactory;
		_viewAuthResourceFactory = viewAuthResourceFactory;
		_viewUsersWithRoleInAuthResourceFactory = viewUsersWithRoleInResourceFactory;
		_viewUsersWithRoleByAuthResourceFactory = viewUsersWithRoleByResourceFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	AUTH RESOURCES LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	public void onLoadViewAuthResourcesRequested(final Language lang,
										 	 	 final UIPresenterSubscriber<Collection<VR>> subscriber) {
		try {
			log.info("[roles by resource presenter] > load resources...");
			Collection<RS> authRes = _coreMediator.loadSummarizedAuthResources(lang);
			Collection<VR> viewAuthRes = CollectionUtils.hasData(authRes)
												? authRes.stream()
														 .map(r -> _viewAuthResourceFactory.from(r))
														 .collect(Collectors.toList())
												: Lists.newArrayList();
			subscriber.onSuccess(viewAuthRes);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	USERS WITH ROLE IN RESOURCE LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	public void onLoadViewUsersWithRoleInAuthResourceNeeded(final AuthorizationTargetResourceOID authResOid,
															final Collection<UserRole> roles,
															final UIPresenterSubscriber<I> subscriber) {
		try {
			log.info("[roles by resource presenter] > load view [users] with role in [auth resource]...");
			R authRes = _coreMediator.loadAuthResource(authResOid);
			I viewUsersWithRoleInResource = _createViewUsersWithRoleInAuthResourceFrom(authRes,
																					   roles);
			subscriber.onSuccess(viewUsersWithRoleInResource);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onLoadViewUsersWithRoleByAuthResourceNeeded(final Collection<UserRole> roles,
															final UIPresenterSubscriber<B> subscriber) {
		try {
			log.info("[roles by resource presenter] > load view [users] with role by [auth resource]...");

			// [1] - Load all [auth resources]
			Collection<R> authRes = _coreMediator.loadAuthResources();

			// [2] - Load the [auths on resources]
			Collection<I> viewUsersWithRoleByResource = CollectionUtils.hasData(authRes)
															? authRes.stream()
																	 // get a collection of [auth on resource] for each [auth resource]
																	 .map(theAuthRes -> _createViewUsersWithRoleInAuthResourceFrom(theAuthRes,
																			 													   roles))
																	 .collect(Collectors.toList())
															: null;
			// [3] - Create the return value
			B outViewUsersWithRoleByAuthRes = _viewUsersWithRoleByAuthResourceFactory.from(viewUsersWithRoleByResource);

			// [99] - Tell the subscriber
			subscriber.onSuccess(outViewUsersWithRoleByAuthRes);

		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected I _createViewUsersWithRoleInAuthResourceFrom(final R authRes,
														   final Collection<UserRole> roles) {
	 	// load the [user auths on resource]
	 	Collection<O> usersAuthsOnRes = _coreMediator.loadUsersAuthsOnResource(authRes.getOid());

	 	log.info("\t - {} [users] with auth on [resource] oid={}",
	 			 usersAuthsOnRes != null ? usersAuthsOnRes.size() : 0,
	 			 authRes.getOid());

	 	// get [users] by [role]
	 	Map<UserRole,Collection<V>> usersByRole = Maps.newHashMapWithExpectedSize(roles.size());
	 	roles.forEach(role -> {
						 	Collection<V> usersWithRole = _loadViewUsersWithRoleInAuthResource(usersAuthsOnRes,
						 																		  		   role);
						 	log.info("\t\t - {} {}",
						 			 usersWithRole != null ? usersWithRole.size() : 0,
						 			 role);
						 	usersByRole.put(role,usersWithRole);
	 				  });
	 	// use a the [auth resource] and the [users by role] to create the [view users with role in auth resource]
	 	I out = _viewUsersWithRoleInAuthResourceFactory.createFrom(authRes,
	 															   usersByRole);
		log.info("\n{}",out.debugInfo());
		return out;
	}
	protected Collection<V> _loadViewUsersWithRoleInAuthResource(final Collection<O> usersAuthsOnRes,
																	 		 final UserRole role) {
		if (CollectionUtils.isNullOrEmpty(usersAuthsOnRes)) return null;
	 	Collection<V> outViewUsers = usersAuthsOnRes.stream()
													.filter(userAuthOnRes -> userAuthOnRes.getRole().is(role))
													.map(userAuthOnRes -> {
																// use the security api to load the user data
																U user = _coreMediator.loadUser(userAuthOnRes.getUserOid());

																// transform to a [view user]
																return _viewUserFactory.from(user);
														 })
													.collect(Collectors.toList());
	 	return outViewUsers;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USERS WITH ROLE IN RESOURCE SAVE
/////////////////////////////////////////////////////////////////////////////////////////
	public void onSaveViewUsersWithRoleInAuthResourceRequested(final I viewUsersWithRoleInResource,
													   	   	   final UIPresenterSubscriber<I> subscriber) {
		log.info("[roles by resource presenter] > saving [users] with [role] in [auth resource] oid={}\n{}",
				 viewUsersWithRoleInResource.getAuthResourceOid(),
				 viewUsersWithRoleInResource.debugInfo());
		// save
		_saveViewUsersWithRoleInResource(viewUsersWithRoleInResource);

		// tell the subscriber
		subscriber.onSuccess(viewUsersWithRoleInResource);
	}
	public void onSaveViewUsersWithRoleByAuthResourceRequested(final B viewUsersWithRoleByResource,
													   	   	   final UIPresenterSubscriber<B> subscriber) {
		// [1] - Delete the removed [auth resources] (just the previously-existing ones)
		Collection<I> deletedAuthRes = viewUsersWithRoleByResource.getDeletedAuthResources();
		if (CollectionUtils.hasData(deletedAuthRes)) {
			int deleted = _coreMediator.deleteAuthResources(deletedAuthRes.stream()
																	 	  .filter(authRes -> authRes.getAuthResourceOid() != null)	// it's a previously-existing resource
																	 	  .map(I::getAuthResourceOid)
																	 	  .collect(Collectors.toSet()));
		}

		// [2] - Create or update the new or existing [auth resources]
		Collection<I> actualResources = viewUsersWithRoleByResource.getActualByAuthResource();
		if (CollectionUtils.hasData(actualResources)) {
			actualResources.stream()
					   .forEach(viewUsersWithRoleInAuthRes -> _saveViewUsersWithRoleInResource(viewUsersWithRoleInAuthRes));
		}

		// [3] - Once everything is persisted, tell the in-memory model objects
		//		 that they're saved
		viewUsersWithRoleByResource.changesSaved();

		// [99] - Return
		subscriber.onSuccess(viewUsersWithRoleByResource);
	}
	public void onDeleteViewUsersWithRoleInAuthResourceRequested(final AuthorizationTargetResourceOID authResOid,
														 		 final UIPresenterSubscriber<Boolean> subscriber) {
		// delete
		boolean deleted = _coreMediator.deleteAuthResource(authResOid);

		// tell the subscriber
		subscriber.onSuccess(deleted);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _saveViewUsersWithRoleInResource(final I viewUsersWithRoleInResource) {
		// [1] - if it's a NEW [auth resource], create it beforehand
		if (viewUsersWithRoleInResource.isNew()) {
			// Create the [auth resource]
			String authResourceName = _getAuthResourceName(viewUsersWithRoleInResource);
			LanguageTexts authResourceDescription = _getAuthResourceDescription(viewUsersWithRoleInResource);
			String resource = _getAuthResourceResources(viewUsersWithRoleInResource);
			R newAuthRes = _coreMediator.createAuthResource(authResourceName,authResourceDescription,
																						  resource);

			// once created, update the [auth resource oid] at the [view object] with the created [auth resource] oid
			viewUsersWithRoleInResource.setAuthResourceOid(newAuthRes.getOid());
		}
		// [2] - update the [users]
		// a) create an [auth on resource] for every NEW user
		viewUsersWithRoleInResource.getRoles()
								   .forEach(role -> {
									   			// add an [auth on resource] for each [user]
												_addAuthOnResourceForEachOf(viewUsersWithRoleInResource.getUsersWithAddedRole(role),role,
																			viewUsersWithRoleInResource.getAuthResourceOid());
								   			});

		// b) delete the [auth on resource] for every DELETED user
		viewUsersWithRoleInResource.getRoles()
								   .forEach(role -> {
									   			// remove an [auth on resource] for each [user]
												_removeAuthOnResourceForEachOf(viewUsersWithRoleInResource.getUsersWithRemovedRole(role),role,
																			   viewUsersWithRoleInResource.getAuthResourceOid());
								   			});
		// [3] - Once everything is persisted, tell the in-memory model objects
		//		 that they're saved
		viewUsersWithRoleInResource.changesSaved();
	}
	protected void _removeAuthOnResourceForEachOf(final Collection<V> viewUsers,final UserRole role,
												  final AuthorizationTargetResourceOID resOid) {
		if (CollectionUtils.isNullOrEmpty(viewUsers)) return;

		int deleted = _coreMediator.removeUserAuthsOnResource(viewUsers.stream()
																	   .map(V::getOid)
																	   .collect(Collectors.toSet()),
															  role,
															  resOid);
	}
	protected void _addAuthOnResourceForEachOf(final Collection<V> viewUsers,final UserRole role,
											   final AuthorizationTargetResourceOID resOid) {
		if (CollectionUtils.isNullOrEmpty(viewUsers)) return;

		Collection<UserOID> userOids = viewUsers.stream()
											    .map(V::getOid)
											    .collect(Collectors.toSet());
		int added = _coreMediator.addUserAuthsOnResource(userOids,
														 role,
														 resOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS CALLED WHEN CREATING AN [AUTH RESOURCE]
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the [auth resource] name
	 * @param viewUsersWithRoleInResource
	 * @return
	 */
	protected abstract String _getAuthResourceName(final I viewUsersWithRoleInResource);
	/**
	 * Returns the [auth resource] description
	 * @param viewUsersWithRoleInResource
	 * @return
	 */
	protected LanguageTexts _getAuthResourceDescription(final I viewUsersWithRoleInResource) {
		return null;
	}
	/**
	 * Returns the [auth resource] resource (ie an [structure label], a [portal oid] or whatever)
	 * @param viewUsersWithRoleInResource
	 * @return
	 */
	protected abstract String _getAuthResourceResources(final I viewUsersWithRoleInResource);
}

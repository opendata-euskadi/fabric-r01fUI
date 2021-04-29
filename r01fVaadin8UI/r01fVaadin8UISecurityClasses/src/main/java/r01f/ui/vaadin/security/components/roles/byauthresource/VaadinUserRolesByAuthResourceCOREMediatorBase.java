package r01f.ui.vaadin.security.components.roles.byauthresource;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.model.security.authorization.AuthorizationTargetResourceBase;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.summaries.SummarizedAuthorizationResourceBase;
import r01f.model.security.user.User;
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.coremediator.UICOREMediator;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class VaadinUserRolesByAuthResourceCOREMediatorBase<U extends User,
																	R extends AuthorizationTargetResourceBase<R>,RS extends SummarizedAuthorizationResourceBase<R>,
																	O extends UserAuthorizationOnResource,
																	S extends SecurityContext,
																	A extends SecurityAPIBase<U,?,R,RS,O>>
  		   implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final AppCode _appCode;
	protected final AppComponent _appComponent;
	protected final Class<S> _securityContextType;

	protected final Factory<R> _authTargetResourceFactory;
	protected final Factory<O> _userAuthOnResourceFactory;

	protected final A _securityApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUserRolesByAuthResourceCOREMediatorBase(final AppCode appCode,final AppComponent appComponent,
														 final Class<S> securityContextType,
														 final A securityApi,
														 final Factory<R> authTargetResourceFactory,final Factory<O> userAuthOnResourceFactory) {
		_appCode = appCode;
		_appComponent = appComponent;
		_securityContextType = securityContextType;
		_securityApi = securityApi;

		_authTargetResourceFactory = authTargetResourceFactory;
		_userAuthOnResourceFactory = userAuthOnResourceFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ABSTRACT METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a filter that filters [auth resources] depending on whether the user
	 * has access or not
	 * @param securityContext
	 * @return
	 */
	protected abstract Predicate<RS> _createSummarizedAuthResourcesFilterFor(final S securityContext);
	/**
	 * Creates a filter that filters [auth resources] depending on whether the user
	 * has access or not
	 * @param securityContext
	 * @return
	 */
	protected abstract Predicate<R> _createAuthResourcesFilterFor(final S securityContext);
	/**
	 * Gives an opportunity to change the [auth resource] name (ie: use another name different from the one stored at the [auth resource])
	 * @param authResource
	 * @return
	 */
	@SuppressWarnings("static-method")
	protected String _getAuthResourceName(final R authResource) {
		return authResource.getName();
	}
	/**
	 * Gives an opportunity to change the [auth resource] description (ie: use another name different from the one stored at the [auth resource])
	 * @param authResource
	 * @return
	 */
	@SuppressWarnings("static-method")
	protected LanguageTexts _getAuthResourceDescription(final R authResource) {
		return authResource.getDescription();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	AUTH TARGET RESOURCES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Load all [auth resources]
	 * @return
	 */
	public Collection<RS> loadSummarizedAuthResources(final Language lang) {
		// Load ALL the [auth resources]
		Collection<RS> authRes = _securityApi.getAuthorizationTargetResourcesApi()
											 .getForFind()
											 .findSummarizedBy(_appCode,_appComponent,
													  		   lang);
		// filter [auth resources] for which the user has access
		S securityContext = _securityApi.getSecurityContext()
										.as(_securityContextType);
		authRes = CollectionUtils.hasData(authRes)
						? authRes.stream()
								 .filter(_createSummarizedAuthResourcesFilterFor(securityContext))
								 .collect(Collectors.toList())
						: null;
		return authRes;
	}
	/**
	 * Load all [auth resources]
	 * @return
	 */
	public Collection<R> loadAuthResources() {
		// Load all [auth resources]
		Collection<R> authRes = _securityApi.getAuthorizationTargetResourcesApi()
											.getForFind()
										    .findBy(_appCode,_appComponent);

		// filter [auth resources] for which the user has access
		S securityContext = _securityApi.getSecurityContext()
										.as(_securityContextType);
		authRes = authRes.stream()
						 .filter(_createAuthResourcesFilterFor(securityContext))
						 .collect(Collectors.toList());
		// maybe the [auth resource] name & description can be loaded from an external db (ie structures / portals / etc)
		// to get a more accurate name or description other than the ones stored at the [auth resoruce]
		if (CollectionUtils.hasData(authRes)) authRes.forEach(authR -> {
																	String moreAccurateName = _getAuthResourceName(authR);
																	LanguageTexts moreAccurateDescription = _getAuthResourceDescription(authR);

																	authR.setName(moreAccurateName);
																	authR.setDescription(moreAccurateDescription);
															  });

		log.info("[roles by resource core mediator] > load ALL auth target resources: {} loaded",
				 authRes != null ? authRes.size() : 0);
		return authRes;
	}
	public R loadAuthResource(final AuthorizationTargetResourceOID authResOid) {
		R authRes = _securityApi.getAuthorizationTargetResourcesApi()
							    .getForCrud()
							    .load(authResOid);
		return authRes;
	}
	public boolean deleteAuthResource(final AuthorizationTargetResourceOID resOid) {
		log.info("[roles by resource core mediator] > delete [resource] oid={}",
				 resOid);
		_securityApi.getAuthorizationTargetResourcesApi()
					.getForCrud()
					.delete(resOid);
		return true;
	}
	/**
	 * Deleted the given [auth resources]
	 * @param resOids
	 * @return
	 */
	public int deleteAuthResources(final Collection<AuthorizationTargetResourceOID> resOids) {
		if (CollectionUtils.isNullOrEmpty(resOids)) return 0;

		log.info("[roles by resource core mediator] > {} delete areas",
				 resOids.size());
		final AtomicInteger deletedCount = new AtomicInteger(0);
		resOids.forEach(resOid -> {
							boolean deleted = this.deleteAuthResource(resOid);
							if (deleted) deletedCount.incrementAndGet();
						});
		return deletedCount.get();
	}
	/**
	 * Creates a NEW [auth resource].
	 * @param name
	 * @param description
	 * @param resources
	 * @return
	 */
	public R createAuthResource(final String name,final LanguageTexts description,
								final String resources) {
		// create a NEW [auth resource]
		R authResToCreate = _authTargetResourceFactory.create();
		authResToCreate.setAppCode(_appCode);
		authResToCreate.setAppComponent(_appComponent);
		authResToCreate.setResources(resources);
		authResToCreate.setDescription(description);
		authResToCreate.setName(name);

		log.info("[roles by resource core mediator] > create [resource] for resources={}",
				 resources);
		R createdRes = _securityApi.getAuthorizationTargetResourcesApi()
								   .getForCrud()
								   .create(authResToCreate);
		return createdRes;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USERS WITH ROLE IN AREA  LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads all [users] with some [auth] on the given [auth resource]
	 * @param authResOid
	 * @return
	 */
	public Collection<O> loadUsersAuthsOnResource(final AuthorizationTargetResourceOID authResOid) {
		Collection<O> usersAuthsOnRes = _securityApi.getUserAuthorizationsOnResourcesApi()
																				  .getForFind()
																				  .findBy(authResOid);
		log.info("[roles by area core mediator] > load ALL uses with any auth on resource oid={} > {} users loaded",
				 authResOid,
				 usersAuthsOnRes != null ? usersAuthsOnRes.size() : 0);
		return usersAuthsOnRes;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USERS WITH ROLE IN AREA  SAVE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds an [auth on resource] for each of the given [users]
	 * @param userOids
	 * @param role
	 * @param resOid
	 * @return
	 */
	public int addUserAuthsOnResource(final Collection<UserOID> userOids,
									  final UserRole role,
									  final AuthorizationTargetResourceOID resOid) {
		if (CollectionUtils.isNullOrEmpty(userOids)) return 0;

		final AtomicInteger createdCount = new AtomicInteger(0);
		userOids.forEach(userOid -> {
							// create a [user auth on resource]
							O userAuthOnRes = _userAuthOnResourceFactory.create();
							userAuthOnRes.setAuthorizationTargetResourceOid(resOid);
							userAuthOnRes.setUserOid(userOid);
							userAuthOnRes.setRole(role);

							// persist
							log.info("[roles by area core mediator] > create [auth on resource] [resource]/[user]/[role]={}/{}/{}",
									 resOid,userOid,role);
							O createdUserAuthOnRes = _securityApi.getUserAuthorizationsOnResourcesApi()
																							   .getForCrud()
																							   .create(userAuthOnRes);
							createdCount.incrementAndGet();
						 });
		return createdCount.get();
	}
	/**
	 * Removes an [auth on resource] for each of the given [users]
	 * @param userOids
	 * @param role
	 * @param resOid
	 * @return
	 */
	public int removeUserAuthsOnResource(final Collection<UserOID> userOids,
									  	 final UserRole role,
									  	 final AuthorizationTargetResourceOID resOid) {
		if (CollectionUtils.isNullOrEmpty(userOids)) return 0;

		final AtomicInteger deletedCount = new AtomicInteger(0);
		userOids.forEach(userOid -> {
							// persist
							O authToDelete = _securityApi.getUserAuthorizationsOnResourcesApi()
																					   .getForCrud()
																					   .loadBy(userOid,resOid,role);
							if (authToDelete == null) throw new IllegalStateException("There does NOT exists an [auth on resource] for [auth resource]/[user]/[role]=" + resOid + "/" + userOid + "/" + role);

							log.info("[roles by resource core mediator] > delete [auth on resource] [resource]/[user]/[role]={}/{}/{}",
									 resOid,userOid,role);
							_securityApi.getUserAuthorizationsOnResourcesApi()
									    .getForCrud()
									    .delete(authToDelete.getOid());
							deletedCount.incrementAndGet();
						 });
		return deletedCount.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USERS
/////////////////////////////////////////////////////////////////////////////////////////
	public U loadUser(final UserOID userOid) {
		return _securityApi.getUsersApi()
						   .getForCrud()
						   .load(userOid);
	}
}

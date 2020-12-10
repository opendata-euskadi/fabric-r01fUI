package r01f.ui.vaadin.security.components.roles.byauthresource;

import java.util.Collection;

import com.vaadin.ui.Component;

import r01f.model.security.user.User;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01ui.base.components.form.VaadinViewTracksChanges;

/**
 * A [form] used to edit the [user roles] associated with an [auth resource]
 * Usually it's something like:
 * <pre>
 * 		+========================================================+
 * 		| Obj [..............................................\/] <---- Auth resource selector (ie a combo of business objs)
 * 		|--------------------------------------------------------|
 * 		| Role 1         | Role 2            | Role 3            |
 * 		| [Add]          | [Add]             | [Add]             |
 * 		|+--------------+ +-----------------+ +-----------------+|
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		|+--------------+ +-----------------+ +-----------------+|
 * 		+========================================================+
 * </pre>
 * @param <U>
 * @param <V>
 * @param <T> The type of the [business obj] related with the [auth resource]; usually it's an [structure label oid], a [portal oid] or whatever
 * @param <I>
 */
public interface VaadinUserRolesInAuthResourceForm<U extends User,V extends VaadinViewUser<U>,
												   T,
												   I extends VaadinViewUsersWithRoleInAuthResourceBase<U,V,I>>
		 extends Component, 	// it's a vaadin component
		 		 VaadinViewTracksChanges {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Enters the [form] in create mode
	 * A list of ALREADY-EXISTING business objs is handled so the user cannot create
	 * another [auth resource] with for the SAME business object
	 * @param existingBusinessObj the list of currently EXISTING business objects (ie: structure labels, or portals, or whatever)
	 * 							  ... the NEW [auth resource] cannot be one of these
	 */
	public void enterCreate(final Collection<T> existingBusinessObj);

	/**
	 * Puts the [view object] data about the given [area] into the [grid]
	 * @param areaLabelOid
	 */
	public void enterEdit(final I viewUsersWithRole);
/////////////////////////////////////////////////////////////////////////////////////////
//	DATA
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the managed object
	 * @param viewUsersWithRole
	 */
	public void setUsersWithRoleInAuthResource(final I viewUsersWithRole);
	/**
	 * Returns the managed object
	 * @return
	 */
	public I getUsersWithRoleInAuthResource();
	/**
	 * Returns true if the managed object has changes
	 * @return
	 */
	public boolean hasChanges();
	/**
	 * Commits changes in [grid]s to the [view object]
	 */
	public void commitChanges();
}

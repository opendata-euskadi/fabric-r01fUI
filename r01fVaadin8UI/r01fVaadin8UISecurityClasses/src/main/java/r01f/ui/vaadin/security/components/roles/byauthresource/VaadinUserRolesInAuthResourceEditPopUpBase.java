package r01f.ui.vaadin.security.components.roles.byauthresource;

import java.util.Collection;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import r01f.locale.I18NKey;
import r01f.model.security.user.User;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01ui.base.components.window.VaadinProceedGateDialogWindow;

/**
 * A pop-up that shows the [user roles] edit [form] in a given [auth resource]
 * Usually it's something like:
 *
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
 * 		|									    	[Save] [Undo]|
 * 		+========================================================+
 * </pre>
 *
 *
 * @param <T> The type of the [business obj] related with the [auth resource]; usually it's an [structure label oid], a [portal oid] or whatever
 * @param <I> the [view users with role in auth resource] obj
 * @param <F> the form
 */
public abstract class VaadinUserRolesInAuthResourceEditPopUpBase<U extends User,V extends VaadinViewUser<U>,
																 T,
																 I extends VaadinViewUsersWithRoleInAuthResourceBase<U,V,I>,
																 F extends VaadinUserRolesInAuthResourceForm<U,V,
																 											 T,I>>
		 	  extends Window
	       implements VaadinViewI18NMessagesCanBeUpdated,
	       			  Listener {

	private static final long serialVersionUID = -5423050396649827819L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final F _form;

	protected final Button _btnPopUpSave = new Button();
	protected final Button _btnPopUpCancel = new Button();

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinUserRolesInAuthResourceEditPopUpBase(final UII18NService i18n,
											  			 final F form) {
		_i18n = i18n;

		////////// Form
		_form = form;
		_form.addListener(this); // listen for component events to recenter the window

		////////// Style
		_btnPopUpSave.setStyleName(ValoTheme.BUTTON_PRIMARY);

		////////// Layout
		HorizontalLayout lyButtons = new HorizontalLayout(_btnPopUpCancel,_btnPopUpSave);

		VerticalLayout ly = new VerticalLayout(form,
											   lyButtons);
		ly.setComponentAlignment(lyButtons, Alignment.MIDDLE_RIGHT);
		this.setContent(ly);

		// behavior
		_setBehavior();

		// i18n
		this.updateI18NMessages(i18n);

		// style
		this.setModal(true);
		this.center();
		this.setWidth(80,Unit.PERCENTAGE);
		this.setCaption(i18n.getMessage("select"));
	}
	private void _setBehavior() {
		// if CANCEL, just close the window (no data is committed to the [view object])
		_btnPopUpCancel.addClickListener(clickEvent -> {
												if (_form.hasChanges()) {
													VaadinProceedGateDialogWindow window = new VaadinProceedGateDialogWindow(_i18n,
																															 I18NKey.named("undo"),
																													 		 I18NKey.named("form.undo-changes"),
																													 		  // ok listener
																										   			  		  () -> {
																										   			  			  // close the pop-up
																										   			  			  VaadinUserRolesInAuthResourceEditPopUpBase.this.close();
																										   			  		  },
																										   			  		  // cancel listener
																										   			  		  () -> {
																										   			  			 // just close this pop-up
																										   			  			  this.close();
																															  },
																										   			  		  // puzzle
																										   			  		  null);
													window.setupProceedButtonWith(I18NKey.named("yes"));
													window.setupNOTProceedButtonWith(I18NKey.named("no"));

													UI.getCurrent().addWindow(window);
												} else {
													this.close();
												}
										 });
		// if OK, commit the [form] changes to the [view object]
		_btnPopUpSave.addClickListener(clickEvent -> {
											// commit the form changes to the underlying data structure
											_form.commitChanges();

											if (!_form.hasChanges()) {
												// no changes
												_showNotification(_i18n.getMessage("form.no-changes"));
											} else {
												I viewUsersWithRoleInAuthRes = _form.getUsersWithRoleInAuthResource();
												_saveData(viewUsersWithRoleInAuthRes);
											}
											// close this popup
											this.close();
									  });
	}
	public void showForCreating() {
		// the [area] combo at the form MUST NOT show the already-created areas
		// ... so that areas must be handed to the form's #enterCreate() method
		Collection<T> existingObjs = _getExistingAuthResourceRelatedBusinessObjs();
		_form.enterCreate(existingObjs);
		
		UI.getCurrent()
		  .addWindow(this);
	}
	public void showForEditing(final I viewUsersWithRoleInArea) {
		_form.enterEdit(viewUsersWithRoleInArea);
		UI.getCurrent()
		  .addWindow(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ABSTRACT METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected abstract Collection<T> _getExistingAuthResourceRelatedBusinessObjs();
	protected abstract void _saveData(final I viewUsersWithRoleInAuthRes);
/////////////////////////////////////////////////////////////////////////////////////////
//	NOTIFICATIONS
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _showNotification(final String msg) {
		Notification notif = new Notification(msg);
		notif.setDelayMsec(1000);
		notif.show(UI.getCurrent().getPage());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_btnPopUpSave.setCaption(i18n.getMessage("save"));
		_btnPopUpCancel.setCaption(i18n.getMessage("cancel"));
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
	public void componentEvent(Event event) {
         // _form can fire a component event when a resource is selected
         if (event.getSource() == _form) {
        	 this.center();
         }
     }
}

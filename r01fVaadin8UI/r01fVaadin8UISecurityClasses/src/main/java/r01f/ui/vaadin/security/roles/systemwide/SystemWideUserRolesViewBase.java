package r01f.ui.vaadin.security.roles.systemwide;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.security.VaadinUIIsSecurityContextAware;
import r01f.ui.vaadin.security.components.VaadinUserRolesFormBase;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01ui.base.components.window.VaadinProceedGateDialogWindow;

/**
 * Creates the [system wide role] admin view
 * <pre>
 * 		+========================================================+
 * 		| Master Role    |  Horizontal Admin |  Customer service |
 * 		|                |  Role             |  Role             |
 * 		| [Add]          | [Add]             |  [Add]            |
 * 		|+--------------+ +-----------------+ +-----------------+|
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		|+--------------+ +-----------------+ +-----------------+|
 * 		|                                         [Cancel] [Save]|
 * 		+========================================================+
 * </pre>
 */
@Accessors(prefix = "_")
public abstract class SystemWideUserRolesViewBase<F extends VaadinUserRolesFormBase>
	 		  extends VerticalLayout
	 	   implements View,
  			 		  VaadinViewI18NMessagesCanBeUpdated,
  			 		  VaadinUIIsSecurityContextAware {

	private static final long serialVersionUID = 3830231442201577197L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Button _btnSave;
	protected final Button _btnCancel;

	protected final F _form;
/////////////////////////////////////////////////////////////////////////////////////////
//	 CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SystemWideUserRolesViewBase(final UII18NService i18n,
											final F form) {
		////////// Services
		_i18n = i18n;

		////////// UI
		_form = form;
		_btnSave = _buildSaveButton();
		_btnCancel = _buildCancelButton();

		////////// Layout
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(false);
		rootLayout.setWidth("98%");
		HorizontalLayout lyFooter = new HorizontalLayout(_btnSave,_btnCancel);
		rootLayout.addComponents(_form,
						   		 lyFooter);
		rootLayout.setComponentAlignment(lyFooter,Alignment.MIDDLE_RIGHT);

		this.addComponents(rootLayout);
		this.setComponentAlignment(rootLayout,Alignment.MIDDLE_CENTER);

		////////// Behavior
		_setBehavior();
	}
	protected Button _buildSaveButton() {
		Button btnSave = new Button();
		btnSave.setSizeUndefined();
		btnSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.setCaption(_i18n.getMessage("save"));

		return btnSave;
	}
	protected Button _buildCancelButton() {
		Button btnCancel = new Button();
		btnCancel.setSizeUndefined();
		btnCancel.setIcon(VaadinIcons.ARROW_BACKWARD);
		btnCancel.setCaption(_i18n.getMessage("undo"));

		btnCancel.setEnabled(false);		// disabled until any grid is changed

		return btnCancel;
	}
	private void _setBehavior() {
		// when the [form] is modified enable the [cancel] button
		_form.setFormModifiedListener(() -> {
											// the form (any grid) has been modified: enable the [cancel] button
											_btnCancel.setEnabled(true);
									  });

		// what happens when the user clicks [save]
		_btnSave.addClickListener(clickEvent -> {
										if (_form.hasViewDataChanged()) {
											// save users
											_saveUsersByRole();

											// tell the form (and every grid) that the changes are commited to the db
											_form.changesCommited();

											// show message
											_showNotification(_i18n.getMessage("saved"));

											// disable the cancel button
											_btnCancel.setEnabled(false);
										} else {
											_showNotification(_i18n.getMessage("form.no-changes"));
										}
								  });
		// what happens when the user clicks [cancel]
		_btnCancel.addClickListener(clickEvent -> {
										if (!_form.hasViewDataChanged()) return;
										VaadinProceedGateDialogWindow window = new VaadinProceedGateDialogWindow(_i18n,
																												 I18NKey.named("confirm"),
																										 		 I18NKey.named("form.undo-changes"),
																										 		  // ok listener
																							   			  		  () -> {
																							   			  			  // Load the data again
																							   			  			  _loadUsersByRole();
																							   			  		  },
																							   			  		  // cancel listener
																							   			  		  () -> {
																							   			  			  _showNotification(_i18n.getMessage("saved"));
																												  },
																							   			  		  // puzzle
																							   			  		  null);
										UI.getCurrent().addWindow(window);
									});
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected abstract void _saveUsersByRole();
	protected abstract void _loadUsersByRole();
/////////////////////////////////////////////////////////////////////////////////////////
//	BEFORE LEAVE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public void beforeLeave(final ViewBeforeLeaveEvent event) {
		if (_form.hasViewDataChanged()) {
			VaadinProceedGateDialogWindow window = new VaadinProceedGateDialogWindow(_i18n,
																					 I18NKey.named("confirm"),
																			 		 I18NKey.named("form.save-changes"),
																			 		  // ok listener
																   			  		  () -> event.navigate(),
																   			  		  // cancel listener
																   			  		  null,
																   			  		  // puzzle
																   			  		  null);
			UI.getCurrent().addWindow(window);
		} else {
			event.navigate();
		}
	 }
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
//		_btnCancel.setCaption(_i18n.getMessage("cancel"));
		_btnSave.setCaption(_i18n.getMessage("save"));
		_btnCancel.setCaption(_i18n.getMessage("undo"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	NOTIFICATIONS
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _showNotification(final String msg) {
		Notification notif = new Notification(msg);
		notif.setDelayMsec(1000);
		notif.show(UI.getCurrent().getPage());
	}
}
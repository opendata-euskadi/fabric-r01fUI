package r01ui.base.components.user;

import com.vaadin.data.Binder;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Notification;

import lombok.Getter;
import r01f.model.security.user.User;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViews;
import r01f.util.types.Strings;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormEditsViewObject;

public abstract class VaadinUserFormBase<U extends User,V extends VaadinViewUser<U>> 
	 		  extends Composite 
	 	   implements VaadinDetailForm<V>,
  			 		  VaadinFormEditsViewObject<V>,
  			 		  VaadinViewI18NMessagesCanBeUpdated {
	
	private static final long serialVersionUID = -3491587021927656161L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
			protected final Class<V> _viewObjType;
	@Getter protected final Binder<V> _vaadinUIBinder;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUserFormBase(final Class<V> viewObjType,
						  	  final UII18NService i18n) {
		////////// services
		_i18n = i18n;
		_viewObjType = viewObjType;
		
		_vaadinUIBinder = new Binder<>(viewObjType);
	}
	protected void _bindFormUIControls() {
		VaadinViews.using(_vaadinUIBinder,_i18n)
	 			   .bindComponentsOf(this)
	 			   .toViewObjectOfType(_viewObjType);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	protected abstract Component _buildUI(final UII18NService i18n);
	
	public abstract void clearForm();
/////////////////////////////////////////////////////////////////////////////////////////
//	Binding
/////////////////////////////////////////////////////////////////////////////////////////
	////////// [viewObject] > [UI control] --------------
	@Override
	public void editViewObject(final V viewObj) {
		// Set the [ui control] values from [view object]'s properties
		// [ui controls] are binded to [view object] properties so when an [ui control] changes 
		// the corresponding [view object]'s property is changed 
		// BUT contrary to #bindViewTo(bean) method, if a [view object]'s property changes,
		// the [ui control] is NOT CHANGED
		// ... so changes in the [UI controls] are NOT reflected to the received [view object] 
		// so it's possible to CANCEL editions
		_vaadinUIBinder.readBean(viewObj);
	}
	////////// [UI control] > [viewObject] --------------
	@Override
	public void writeAsDraftEditedViewObjectTo(final V viewObj) {
		// writes the [ui control] field values to the corresponding [view object properties]
		_vaadinUIBinder.writeBeanAsDraft(viewObj);
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(final V viewObj) {
		if (!this.isValid()) {
			Notification.show(_i18n.getMessage("user.form.not-valid"),
											   Notification.Type.ERROR_MESSAGE);
			return false;
		}
		
		// writes the [ui control] field values to the corresponding [view object properties]
		return _vaadinUIBinder.writeBeanIfValid(viewObj);
	}
	public boolean isValid() {
		return _vaadinUIBinder.isValid();
	}
	protected static boolean _verifyPasswords(final String password,
										      final String confirmPassword,
										      final boolean mandatoryPassword) {
		if (!mandatoryPassword) return Strings.isNullOrEmpty(password) 
									&& Strings.isNullOrEmpty(confirmPassword);
		if (Strings.isNullOrEmpty(password)) return false;
		if (Strings.isNullOrEmpty(confirmPassword)) return false;
		
		return password.equals(confirmPassword);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		VaadinViews.using(_i18n)
				   .setI18NLabelsOf(this);
	}
}

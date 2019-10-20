package r01ui.base.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

import lombok.experimental.Accessors;
import r01f.guids.PersistableObjectOID;
import r01f.model.PersistableModelObject;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIObjectDetailLanguageDependentPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.viewobject.UIViewObject;

@Accessors( prefix="_" )
public abstract class VaadinDeleteConfirmDialogBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>,
												    V extends UIViewObject,
												    P extends UIObjectDetailLanguageDependentPresenter<O,V>>		// core mediator
	 		  extends Window {

	private static final long serialVersionUID = 67118991862242129L;
/////////////////////////////////////////////////////////////////////////////////////////
//  I18N
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient UII18NService _i18n;
/////////////////////////////////////////////////////////////////////////////////////////
// 	PRESENTER
/////////////////////////////////////////////////////////////////////////////////////////
	private final P _presenter;

/////////////////////////////////////////////////////////////////////////////////////////
//  OUTSIDE WORLD SUBSCRIBERS & DATA
/////////////////////////////////////////////////////////////////////////////////////////
	private O _objToBeDeletedOid;
	private UIPresenterSubscriber<V> _subscriber;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDeleteConfirmDialogBase(final UII18NService i18n,
										 final P presenter) {
		_i18n = i18n;
		_presenter = presenter;

		this.center();
		this.setModal( true );
		this.setWidth( 50,Unit.PERCENTAGE );
		this.setCaption( i18n.getMessage( "confirm" ) );
		this.setResizable( false );

		// DELETE
		final Button acceptButton = new Button();
		acceptButton.setCaption(i18n.getMessage("delete"));
		acceptButton.setStyleName( "danger" );
		acceptButton.addClickListener(event -> {
													_presenter.onDeleteRequested(_objToBeDeletedOid,
																				 _i18n.getCurrentLanguage(),
																				 _subscriber);			// the presenter will tell the subscriber...
													VaadinDeleteConfirmDialogBase.this.close();
												});
		// CANCEL
		final Button cancelButton = new Button();
		cancelButton.setCaption( i18n.getMessage( "cancel" ) );
		cancelButton.addClickListener(event -> VaadinDeleteConfirmDialogBase.this.close());

		// layout
		HorizontalLayout layoutDeleteConfirm = new HorizontalLayout();
		layoutDeleteConfirm.addComponents(cancelButton,
										  acceptButton);
		layoutDeleteConfirm.setExpandRatio(acceptButton,
										   1);
		layoutDeleteConfirm.setComponentAlignment(acceptButton,
												  Alignment.MIDDLE_RIGHT);

		layoutDeleteConfirm.setMargin( true );
		layoutDeleteConfirm.setWidth(100,Unit.PERCENTAGE);

		this.setContent(layoutDeleteConfirm);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  PUBLIC ENTRY POINT
/////////////////////////////////////////////////////////////////////////////////////////
	public void setObjToBeDeletedOid(final O oid,
									 final UIPresenterSubscriber<V> subscriber) {
		_objToBeDeletedOid = oid;
		_subscriber = subscriber;
	}

}
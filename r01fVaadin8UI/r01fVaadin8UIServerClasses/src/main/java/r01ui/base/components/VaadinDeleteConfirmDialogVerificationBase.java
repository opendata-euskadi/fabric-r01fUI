package r01ui.base.components;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import lombok.experimental.Accessors;
import r01f.guids.PersistableObjectOID;
import r01f.model.PersistableModelObject;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIObjectDetailLanguageDependentPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.viewobject.UIViewObject;

@Accessors( prefix="_" )
public abstract class VaadinDeleteConfirmDialogVerificationBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>,
												   				V extends UIViewObject,
												   				P extends UIObjectDetailLanguageDependentPresenter<O,V>>		// core mediator
	 		  extends Window {

	private static final long serialVersionUID = 7440334832528162438L;
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
	public VaadinDeleteConfirmDialogVerificationBase(final UII18NService i18n,
													final P presenter,
													final String name) {
		_i18n = i18n;
		_presenter = presenter;

		this.center();
		this.setModal( true );
		this.setWidth( 30,Unit.PERCENTAGE );
		this.setCaption( _i18n.getMessage( "confirm" ) );
		this.setResizable( false );

		//	DELETE CONFIRM DIALOG
		Label deleteConfirmDialog = new Label();
		deleteConfirmDialog.setValue( _i18n.getMessage( "delete.confirm.verification" ) );

		//	TEXT LABEL
		Label textLabel = new Label();
		textLabel.setValue( _i18n.getMessage( "delete.message" ) );

		//	CONFIRM TEXTFIELD
		TextField dialogTextField = new TextField();

		// DELETE
		final Button acceptButton = new Button();
		acceptButton.setCaption(_i18n.getMessage("delete"));
		acceptButton.setStyleName( "danger" );
		acceptButton.setEnabled(false);
		acceptButton.addClickListener( new ClickListener() {
												private static final long serialVersionUID = -6340662642025847569L;

												@Override
												public void buttonClick(final ClickEvent event) {
													_presenter.onDeleteRequested(_objToBeDeletedOid,
																				 _i18n.getCurrentLanguage(),
																				 _subscriber);			// the presenter will tell the subscriber...
													VaadinDeleteConfirmDialogVerificationBase.this.close();
													dialogTextField.clear();
												}
										} );
		// CANCEL
		final Button cancelButton = new Button();
		cancelButton.setCaption( _i18n.getMessage( "cancel" ) );
		cancelButton.addClickListener(new ClickListener() {
												private static final long serialVersionUID = 858133840434594589L;

												@Override
												public void buttonClick(final ClickEvent event) {
													VaadinDeleteConfirmDialogVerificationBase.this.close();
												}
									   });

		dialogTextField.addValueChangeListener(new ValueChangeListener<String>() {
														private static final long serialVersionUID = 4765979744611459272L;

														@Override
														public void valueChange(final ValueChangeEvent<String> event) {
															if (dialogTextField.getValue().equals("Eliminar")) {
																acceptButton.setEnabled(true);
															}
															else {
																acceptButton.setEnabled(false);
															}
														}
												});

		HorizontalLayout textDialogLayout = new HorizontalLayout();
		textDialogLayout.addComponent(textLabel);
		textDialogLayout.addComponent(dialogTextField);
		textDialogLayout.setComponentAlignment(textLabel,Alignment.MIDDLE_CENTER);
		textDialogLayout.setComponentAlignment(dialogTextField,Alignment.MIDDLE_CENTER);

		// layout
		HorizontalLayout layoutDeleteConfirm = new HorizontalLayout();
		layoutDeleteConfirm.addComponents(cancelButton,
										  acceptButton);
		layoutDeleteConfirm.setExpandRatio(acceptButton,
										   1);
		layoutDeleteConfirm.setComponentAlignment(acceptButton,
												  Alignment.MIDDLE_RIGHT);

		layoutDeleteConfirm.setMargin( false );
		layoutDeleteConfirm.setWidth(100,Unit.PERCENTAGE);

		VerticalLayout layoutDeleteConfirmVerification = new VerticalLayout();
		layoutDeleteConfirmVerification.addComponents(deleteConfirmDialog,
										  			  textDialogLayout,
										  			  layoutDeleteConfirm);

		this.setContent(layoutDeleteConfirmVerification);
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
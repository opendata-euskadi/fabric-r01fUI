package r01ui.base.components.window;

import java.io.Serializable;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * A window used for ask user permission to proceed with something
 * The window is like:
 * <pre>
 *     ++++++++++++++++++++++++++++++++++++++++++++++
 *     + Caption                                    +
 *     ++++++++++++++++++++++++++++++++++++++++++++++
 *     + Message (ie: delete??)                     +
 *     +                                            +
 *     +                                            +
 *     +                             [Cancel] [OK]  +
 *     ++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * Optionally a puzzle can be provides so the user must have to resolve the puzzle
 * to proceed:
 * <pre>
 *     ++++++++++++++++++++++++++++++++++++++++++++++
 *     + Caption                                    +
 *     ++++++++++++++++++++++++++++++++++++++++++++++
 *     + Message (ie: delete??)                     +
 *     +                                            +
 *     +                                            +
 *     + [puzzle txt          ]      [Cancel] [OK]  +
 *     ++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * The [OK] button is NOT enabled until the user solves the puzzle
 */
public class VaadinProceedGateDialogWindow 
	 extends Window 
  implements VaadinViewI18NMessagesCanBeUpdated {  
	
	private static final long serialVersionUID = 2953083925723451730L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private final I18NKey _i18nKeyForCaption; 
	private final I18NKey _i18nKeyForMessage;
	private final Object[] _paramsForMessage;
	private I18NKey _i18nKeyForBtnProceed = I18NKey.forId("yes");
	private I18NKey _i18nKeyForBtnNOTProceed = I18NKey.forId("no");
	
	private final Button _btnNOTProceed = new Button();
	private final Button _btnProceed = new Button();
	private final Label _lblWindowMessage = new Label();
	private final TextField _txtPuzzle = new TextField();
	private R01UIProceedPuzzleCheck _puzzleCheck;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									     final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									     // what happens when the user solves the puzzle
									     final R01UIProceedGateProceed proceed) {
		this(i18n,
			 i18nKeyForCaption,i18nKeyForMessage,
			 proceed,
			 null,		// opened listener
			 null);		// no puzzle
	}
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									     final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									     final R01UIProceedGateProceed proceed,
									     final R01UIProceedGateCancel cancel) {
		this(i18n,
			 i18nKeyForCaption,i18nKeyForMessage,
			 proceed,
			 cancel,
			 null);		// no puzzle
	}
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									     final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									     final R01UIProceedGateProceed proceed,
									     final R01UIProceedPuzzleCheck puzzleCheck) {
		this(i18n,
			 i18nKeyForCaption,i18nKeyForMessage, 
			 proceed,
			 null,		// closed listener
			 puzzleCheck);
	}	
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									     final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									     final R01UIProceedGateProceed proceed,
									     final R01UIProceedGateCancel cancel,
									     final R01UIProceedPuzzleCheck puzzleCheck) {
		this(i18n,
			i18nKeyForCaption,i18nKeyForMessage,
			proceed,
			cancel,
			puzzleCheck,
			(Object[])null);
	}
	
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									     final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									     final R01UIProceedGateProceed proceed,
									     final R01UIProceedGateCancel cancel,
									     final R01UIProceedPuzzleCheck puzzleCheck,
									     final Object... paramsForMessage) {
		this.setCaption(i18n.getMessage(i18nKeyForCaption));
		this.setModal(true);
		this.setResizable(false);
		this.setClosable(false);
		this.center();
		
		_i18nKeyForCaption = i18nKeyForCaption;
		_i18nKeyForMessage = i18nKeyForMessage;
		_paramsForMessage = paramsForMessage;
		
		_puzzleCheck = puzzleCheck;
		_initLayout(i18n);
		_initBehavior(proceed,
					  cancel);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Creates the window structure
	 * @param message
	 */
	private void _initLayout(final UII18NService i18n) {
		// label
		
		_lblWindowMessage.setValue(_paramsForMessage == null
									 ? i18n.getMessage(_i18nKeyForMessage)
									 : i18n.getMessage(_i18nKeyForMessage,_paramsForMessage));
		_lblWindowMessage.setContentMode(ContentMode.HTML);
		
		// buttons
		_btnNOTProceed.setIcon(VaadinIcons.CLOSE_SMALL);
		_btnNOTProceed.setCaption(i18n.getMessage(_i18nKeyForBtnNOTProceed));
		
		_btnProceed.setIcon(VaadinIcons.CHECK);
		_btnProceed.setCaption(i18n.getMessage(_i18nKeyForBtnProceed));
		_btnProceed.addStyleName(ValoTheme.BUTTON_DANGER);
		
		// Puzzle
		_txtPuzzle.setValueChangeMode(ValueChangeMode.TIMEOUT);
		_txtPuzzle.setWidth(90, Unit.PERCENTAGE); 
		_txtPuzzle.setVisible(false);
		if (_puzzleCheck != null) {
			_btnProceed.setEnabled(false);	// the button is NOT enabled by default
			
			_txtPuzzle.setVisible(true);
			_txtPuzzle.focus();
			// only activate the operation button if the puzzle is solved
			_txtPuzzle.addValueChangeListener(valChangeEvent -> {
													String txt = valChangeEvent.getValue();
													boolean solved = _puzzleCheck.check(txt);
													_btnProceed.setEnabled(solved);
											  });
		}
		
		// Layout
		HorizontalLayout hLayoutForButtons = new HorizontalLayout(_btnNOTProceed,
															  	  _btnProceed);
		VerticalLayout vLayout = new VerticalLayout(_lblWindowMessage,
													_txtPuzzle,
													hLayoutForButtons);
		vLayout.setComponentAlignment(hLayoutForButtons,Alignment.BOTTOM_RIGHT);
		this.setContent(vLayout);
	}
	private void _initBehavior(final R01UIProceedGateProceed proceed,
							   final R01UIProceedGateCancel cancel) {
		_btnNOTProceed.addClickListener(clickEvent -> {
											if (cancel != null) cancel.cancel();
											this.close();
									    });
		_btnProceed.addClickListener(clickEvent -> {	// pass the event & close
										if (proceed != null) proceed.proceed();
										this.close();	
									});
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	SHOW
/////////////////////////////////////////////////////////////////////////////////////////
	public void show() {
		UI.getCurrent()
		  .addWindow(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BUTTONS & MESSAGES 
/////////////////////////////////////////////////////////////////////////////////////////	
	public void setMessage(final String message) {
		_lblWindowMessage.setValue(message);
	}
	public void setupProceedButtonWith(final I18NKey proceedBtnI18NKey) {
		this.setupProceedButtonWith(proceedBtnI18NKey,
									null);		// no icon
	}
	public void setupProceedButtonWith(final I18NKey proceedBtnI18NKey,
									   final Resource proceedBtnIcon) {
		_i18nKeyForBtnProceed = proceedBtnI18NKey;
		_btnProceed.setIcon(proceedBtnIcon);
	}
	public void setupNOTProceedButtonWith(final I18NKey notProceedBtnI18NKey) {
		this.setupNOTProceedButtonWith(notProceedBtnI18NKey,
									   null);	// no icon
	}
	public void setupNOTProceedButtonWith(final I18NKey notProceedBtnI18NKey,
										  final Resource notProceedBtnIcon) {
		_i18nKeyForBtnNOTProceed = notProceedBtnI18NKey;
		_btnNOTProceed.setIcon(notProceedBtnIcon);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		this.setCaption(i18n.getMessage(_i18nKeyForCaption));
		_lblWindowMessage.setValue(i18n.getMessage(_i18nKeyForMessage));
		_btnProceed.setCaption(i18n.getMessage(_i18nKeyForBtnProceed));
		_btnNOTProceed.setCaption(i18n.getMessage(_i18nKeyForBtnNOTProceed));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@FunctionalInterface
	public interface R01UIProceedGateProceed 
	  		 extends Serializable {
		void proceed();
	}
	@FunctionalInterface
	public interface R01UIProceedGateCancel 
	  		 extends Serializable {
		void cancel();
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	PUZZLE CHECK
/////////////////////////////////////////////////////////////////////////////////////////
	@FunctionalInterface
	public interface R01UIProceedPuzzleCheck {
		public boolean check(final String text);
	}
	public void setProceedPuzzleCheck(final R01UIProceedPuzzleCheck check) {
		_puzzleCheck = check;
		if (_puzzleCheck != null) {
			_btnProceed.setEnabled(false);	// the button is NOT enabled by default
			
			_txtPuzzle.clear();
			_txtPuzzle.setVisible(true);
			_txtPuzzle.focus();
			// only activate the operation button if the puzzle is solved
			_txtPuzzle.addValueChangeListener(valChangeEvent -> {
													String txt = valChangeEvent.getValue();
													boolean solved = _puzzleCheck.check(txt);
													_btnProceed.setEnabled(solved);
											  });
		}
		else {
			_btnProceed.setEnabled(false);
			_txtPuzzle.setVisible(false);
		}
	}
}
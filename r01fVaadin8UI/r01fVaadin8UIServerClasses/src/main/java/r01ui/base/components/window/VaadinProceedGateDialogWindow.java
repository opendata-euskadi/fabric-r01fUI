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
									    final R01UIProceedGateOpendEventListener proceedGateOpenedListener) {
		this(i18n,
			 i18nKeyForCaption,i18nKeyForMessage,
			 proceedGateOpenedListener,
			 null,
			 null);		// no puzzle
	}
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									    final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									    final R01UIProceedGateOpendEventListener proceedGateOpenedListener,
									    final R01UIProceedGateClosedEventListener proceedGateClosedListener) {
		this(i18n,
			 i18nKeyForCaption,i18nKeyForMessage,
			 proceedGateOpenedListener,
			 proceedGateClosedListener,
			 null);		// no puzzle
	}
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									    final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									    final R01UIProceedGateOpendEventListener proceedGateOpenedListener,
									    final R01UIProceedPuzzleCheck puzzleCheck) {
		this(i18n,
			 i18nKeyForCaption,i18nKeyForMessage, 
			 proceedGateOpenedListener,
			 null,		// closed listener
			 puzzleCheck);
	}	
	public VaadinProceedGateDialogWindow(final UII18NService i18n,
									    final I18NKey i18nKeyForCaption,final I18NKey i18nKeyForMessage, 
									    final R01UIProceedGateOpendEventListener proceedGateOpenedListener,
									    final R01UIProceedGateClosedEventListener proceedGateClosedListener,
									    final R01UIProceedPuzzleCheck puzzleCheck) {
		this.setCaption(i18n.getMessage(i18nKeyForCaption));
		this.setModal(true);
		this.setResizable(false);
		this.setClosable(false);
		this.center();
		
		_i18nKeyForCaption = i18nKeyForCaption;
		_i18nKeyForMessage = i18nKeyForMessage;
		
		_puzzleCheck = puzzleCheck;
		_initLayout(i18n);
		_initBehavior(proceedGateOpenedListener,
					  proceedGateClosedListener);
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
		_lblWindowMessage.setValue(i18n.getMessage(_i18nKeyForMessage));
		_lblWindowMessage.setContentMode(ContentMode.HTML);
		
		// buttons
		_btnNOTProceed.setIcon(VaadinIcons.CLOSE_SMALL);
		_btnNOTProceed.setCaption(i18n.getMessage(_i18nKeyForBtnNOTProceed));
		
		_btnProceed.setIcon(VaadinIcons.CHECK);
		_btnProceed.setCaption(i18n.getMessage(_i18nKeyForBtnProceed));
		_btnProceed.addStyleName(ValoTheme.BUTTON_DANGER);
		
		// Puzzle
		_txtPuzzle.setValueChangeMode(ValueChangeMode.TIMEOUT);
		_txtPuzzle.setVisible(false);
		if (_puzzleCheck != null) {
			_btnProceed.setEnabled(false);	// the button is NOT enabled by default
			
			_txtPuzzle.setVisible(true);
			_txtPuzzle.focus();
			// only activate the operation button if the puzzle is solved
			_txtPuzzle.addValueChangeListener(e -> {
														String txt = e.getValue();
														boolean solved = _puzzleCheck.check(txt);
														_btnProceed.setEnabled(solved);
												   });
		}
		
		// Layout
		final HorizontalLayout hLayoutForButtons = new HorizontalLayout(_btnNOTProceed,
															  		    _btnProceed);
		final VerticalLayout vLayout = new VerticalLayout(_lblWindowMessage,
														  _txtPuzzle,
														  hLayoutForButtons);
		vLayout.setComponentAlignment(hLayoutForButtons,Alignment.BOTTOM_RIGHT);
		this.setContent(vLayout);
	}
	private void _initBehavior(final R01UIProceedGateOpendEventListener proceedGateOpenedListener,
							   final R01UIProceedGateClosedEventListener proceedGateClosedListener) {
		_btnNOTProceed.addClickListener(e ->{ 
												if (proceedGateClosedListener!=null)
													proceedGateClosedListener.closed();
												this.close();
											});
		_btnProceed.addClickListener(e -> {	// pass the event & close
												proceedGateOpenedListener.opened();
												this.close();	
											});
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public void setMessage(final String message) {
		_lblWindowMessage.setValue(message);
	}
	public void setupProceedButtonWith(final I18NKey proceedBtnI18NKey,
									   final Resource proceedBtnIcon) {
		_i18nKeyForBtnProceed = proceedBtnI18NKey;
		_btnProceed.setIcon(proceedBtnIcon);
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
	public interface R01UIProceedGateOpendEventListener 
	  		 extends Serializable {
		void opened();
	}
	@FunctionalInterface
	public interface R01UIProceedGateClosedEventListener 
	  		 extends Serializable {
		void closed();
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
			
			_txtPuzzle.setVisible(true);
			_txtPuzzle.focus();
			// only activate the operation button if the puzzle is solved
			_txtPuzzle.addValueChangeListener(e -> {
													String txt = e.getValue();
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
package com.google.gwt.user.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * Label widget (see https://code.google.com/p/cobogw/)
 */
public class SimpleLabel 
     extends Widget 
  implements HasText {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The attached state is locally maintained to trick the super class {@link Widget} and 
	 * force NO EventListener set
	 */
	private boolean _attached;

	private com.google.gwt.dom.client.LabelElement _labelElement = null;
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Default constructor. Use {@link #setText(String)} to set the text.
	 */
	public SimpleLabel() {
	}
	/**
	 * Creates a new TextNode with the text assigned.
	 * @param text Text to be assigned to the TextNode
	 */
	public SimpleLabel(final String text) {
		this.setText(text);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getText() {
		return _labelElement.getInnerText();
	}
	@Override
	public void setText(final String text) {
		if (_labelElement == null) {
			_labelElement = Document.get().createLabelElement();
			_labelElement.setInnerText(text);
			this.setElement(_labelElement.<Element>cast());
		} else {
			_labelElement.setInnerHTML(text);
		}
	}
	/**
	 * Determines whether this widget is currently attached to the browser's
	 * document (i.e., there is an unbroken chain of widgets between this widget
	 * and the underlying browser document).
	 * @return <code>true</code> if the widget is attached
	 */
	@Override
	public boolean isAttached() {
		return _attached;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called when a widget is attached to the browser's document.
	 * <p>
	 * To receive notification after a Widget has been added to the document, 
	 * override the {@link #onLoad} method.
	 * </p>
	 * <p>
	 * Subclasses that override this method must call <code>super.onAttach()</code> to ensure that
	 * the Widget has been attached to the underlying Element.
	 * </p>
	 * 
	 * @throws IllegalStateException if this widget is already attached
	 */
	@Override
	protected void onAttach() {
		if (this.isAttached()) throw new IllegalStateException("Should only call onAttach when the widget is detached from the browser's document");
		_attached = true;
		this.onLoad();	// Now that the widget is attached, call onLoad().
	}
	/**
	 * This method is called when a widget is detached from the browser's document. 
	 * <p>
	 * To receive notification before a Widget is removed from the document, 
	 * override the {@link #onUnload} method.
	 * </p>
	 * <p>
	 * Subclasses that override this method must call <code>super.onDetach()</code> to ensure that the Widget has been detached
	 * from the underlying Element. 
	 * Failure to do so will result in application memory leaks due to circular references between DOM Elements and
	 * JavaScript objects.
	 * </p>
	 * 
	 * @throws IllegalStateException if this widget is already detached
	 */
	@Override
	protected void onDetach() {
		if (!this.isAttached()) throw new IllegalStateException("Should only call onDetach when the widget is attached to the browser's document");
		try {
			this.onUnload();	// Give the user a chance to clean up
		} finally {
			_attached = false; 	// don't trust the code to not throw
		}
	}
}

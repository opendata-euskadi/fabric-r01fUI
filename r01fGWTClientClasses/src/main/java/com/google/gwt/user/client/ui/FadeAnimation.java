package com.google.gwt.user.client.ui;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;

/**
 * An animation See http://map-notes.blogspot.com.es/2012/11/fade-animation.html
 * Usage:
 * <pre class='brush:java'>
 *		final FadeAnimation fadeAnimation = new FadeAnimation(panel.getElement());
 *		panel.addDomHandler(new MouseOverHandler() {
 *									@Override
 *									public void onMouseOver(MouseOverEvent event) {
 *										fadeAnimation.fade(1500,	// duration 
 *														   1.0);	// target opacity
 *									}
 *							}, 
 *							MouseOverEvent.getType());
 * 
 *		panel.addDomHandler(new MouseOutHandler() {
 *									 @Override
 *									 public void onMouseOut(MouseOutEvent event) {
 *										 fadeAnimation.fade(1500,	// duration
 *												 			0.3);	// targe opacity
 *									 }
 *							}, MouseOutEvent.getType());
 *		}
 * </pre>
 * Using the fluent API:
 * <pre class='brush:java'>
 * 		FadeAnimation fadeAnimation = FadeAnimation.forElement(el)
 * 												   .withDuration(1500)
 * 												   .toOpacity(0,3);
 * </pre>
 */
public class FadeAnimation 
	 extends Animation {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final Element _element;
	private double _opacityIncrement;
	private double _targetOpacity;
	private double _baseOpacity;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected FadeAnimation(final Element element) {
		_element = element;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  FLUENT-API
/////////////////////////////////////////////////////////////////////////////////////////
	public static FadeAnimationDurationStep forElement(final Element element) {
		return new FadeAnimationDurationStep(element);
	}
	@RequiredArgsConstructor
	public static class FadeAnimationDurationStep {
		private final Element _element;
		public FadeAnimationOpacityStep during(final int duration) {
			return new FadeAnimationOpacityStep(_element,
												duration);
		}
	}
	@RequiredArgsConstructor
	public static class FadeAnimationOpacityStep {
		private final Element _element;
		private final int _duration;
		public FadeAnimation toOpacity(final double targetOpacity) {
			// do the fading
			FadeAnimation animation = new FadeAnimation(_element);
			animation.fade(_duration,targetOpacity);
			return animation;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	public void fade(final int duration,final double targetOpacity) {
		double theTargetOpacity = 0.0;
		if (targetOpacity > 1.0) {
			theTargetOpacity = 1.0;
		} else if (targetOpacity < 0.0) {
			theTargetOpacity = 0.0;
		} else {
			theTargetOpacity = targetOpacity;
		}
		_targetOpacity = theTargetOpacity;
		String opacityStr = _element.getStyle()
									.getOpacity();
		try {
			_baseOpacity = new BigDecimal(opacityStr).doubleValue();
			_opacityIncrement = targetOpacity - _baseOpacity;
			super.run(duration);
		} catch (NumberFormatException nfEx) {
			this.onComplete();	// set opacity directly
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onUpdate(final double progress) {
		_element.getStyle()
				.setOpacity(_baseOpacity + progress * _opacityIncrement);
	}
	@Override
	protected void onComplete() {
		super.onComplete();
		_element.getStyle().setOpacity(_targetOpacity);
	}
}

package r01f.ui.vaadin.view;

import r01f.ui.i18n.UII18NService;

public interface VaadinViewI18NMessagesCanBeUpdated {
	 default void updateI18NMessages(final UII18NService i18n) {
		 // just do nothing
	 }
}

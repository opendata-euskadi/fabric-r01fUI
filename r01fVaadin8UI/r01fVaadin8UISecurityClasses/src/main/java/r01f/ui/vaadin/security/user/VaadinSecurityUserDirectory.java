package r01f.ui.vaadin.security.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;

@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public enum VaadinSecurityUserDirectory {
	LOCAL		(I18NKey.named("security.directory.local")),
	CORPORATE	(I18NKey.named("security.directory.corporate"));

	private final I18NKey _i18nKey;

	public String getNameUsing(final UII18NService i18n) {
		return i18n.getMessage(_i18nKey);
	}
}
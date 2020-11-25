package r01ui.base.components.contact;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.ui.i18n.UII18NService;
import r01f.util.types.Strings;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinContactMeanValidators {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor
	public static class VaadinPhoneValidator
	   	      implements Validator<String> {

		private static final long serialVersionUID = -6088142603048630416L;

		private final UII18NService _i18n;

		@Override
		public ValidationResult apply(final String value,
									  final ValueContext context) {
			if (Strings.isNullOrEmpty(value)) return ValidationResult.error(_i18n.getMessage("user.login.registry.validation.phone.mandatory"));

			// beware!! this is called for every keystroke on the text field
			return VaadinContactMeanValidators.isValidPhones(value) ? ValidationResult.ok()
																	: ValidationResult.error(_i18n.getMessage("user.login.registry.validation.phone"));
		}
	}
	public static boolean isValidPhones(final String value) {
		Phone phone = Phone.of(value);
		boolean allValid = true;
		if (phone == null
		 || !phone.isValid()) {
			allValid = false;
		}
		return allValid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EMAIL
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor
	public static class VaadinEMailValidator
	   	 	 implements Validator<String> {

		private static final long serialVersionUID = -6088142603048630416L;

		private final UII18NService _i18n;

		@Override
		public ValidationResult apply(final String value,
									  final ValueContext context) {
			if (Strings.isNullOrEmpty(value)) return ValidationResult.error(_i18n.getMessage("user.login.registry.validation.email.mandatory"));

			// beware!! this is called for every keystroke on the text field
			return VaadinContactMeanValidators.isValidEmails(value) ? ValidationResult.ok()
																	: ValidationResult.error(_i18n.getMessage("user.login.registry.validation.email"));
		}
	}
	public static boolean isValidEmails(final String value) {
		EMail email = EMail.of(value);
		boolean allValid = true;
		if (email == null || !email.isValid()) {
			allValid = false;
		}
		return allValid;
	}
}

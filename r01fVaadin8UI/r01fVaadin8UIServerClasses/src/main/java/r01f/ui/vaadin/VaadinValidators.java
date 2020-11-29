package r01f.ui.vaadin;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.types.contact.NIFValidator;
import r01f.types.contact.PersonID;
import r01f.ui.i18n.UII18NService;
import r01f.util.types.Strings;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinValidators {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor
	public static class VaadinStringValidator
	   	 	 implements Validator<String> {

		private static final long serialVersionUID = -6088142603048630416L;
		
		private final UII18NService _i18n;
		
		@Override
		public ValidationResult apply(final String value,
									  final ValueContext context) {
			if (Strings.isNullOrEmpty(value)) return ValidationResult.error(_i18n.getMessage("user.login.registry.validation.field"));

			// beware!! this is called for every keystroke on the text field
			return ValidationResult.ok();
		}
	}
	@RequiredArgsConstructor
	public static class VaadinSpanishNIFValidator
	   	 	 implements Validator<PersonID> {

		private static final long serialVersionUID = -6088142603048630416L;
		
		private final UII18NService _i18n;
		
		@Override
		public ValidationResult apply(final PersonID value,
									  final ValueContext context) {
			if (value == null || Strings.isNullOrEmpty(value.asString())) return ValidationResult.error(_i18n.getMessage("user.login.registry.validation.field"));

			return NIFValidator.isValidNif(value.asString()) ? ValidationResult.ok()
															 : ValidationResult.error(_i18n.getMessage("user.login.registry.validation.field"));
		}
	}
}

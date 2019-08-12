package r01f.ui.vaadin.annotations;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A void vaadin {@link Validator} type used to set a default validator
 * at @VaadinViewField annotation
 * DO NOT USE!
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinVoidViewFieldValidator
		   implements Validator<Object> {

	private static final long serialVersionUID = 7938937501929827216L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ValidationResult apply(final Object value,
								  final ValueContext context) {
		throw new UnsupportedOperationException();
	}
}

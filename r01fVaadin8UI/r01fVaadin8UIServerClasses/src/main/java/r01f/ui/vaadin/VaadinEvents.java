package r01f.ui.vaadin;

import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.TextField;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.util.types.Numbers;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class VaadinEvents {

	/**
	 * Creates a TextField's {@link ValueChangeListener} that just allows numbers
	 * @param txtField
	 * @return
	 */
	public static final ValueChangeListener<String> createOnlyNumbersTextFieldValueChangeListener(final TextField txtField) {
		return valChangeEvent -> {
						// ensure only ints
						String val = valChangeEvent.getValue();
						if (val == null || !Numbers.isInteger(val)) {
							txtField.setValue("");
						}
			   };
	}
}

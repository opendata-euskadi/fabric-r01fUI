package r01f.ui.vaadin;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import com.vaadin.ui.renderers.LocalDateRenderer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.util.types.locale.Languages;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinDates {	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static LocalDateRenderer createLocalDateRenderer(final Locale locale,
															final FormatStyle style) {
		return new LocalDateRenderer(() -> DateTimeFormatter.ofLocalizedDate(style)
		        											.withLocale(locale));
	}
	public static LocalDateRenderer createLocalDateRenderer(final Language lang,
															final FormatStyle style) {
		return VaadinDates.createLocalDateRenderer(Languages.getLocale(lang),
												   style);
	}
}

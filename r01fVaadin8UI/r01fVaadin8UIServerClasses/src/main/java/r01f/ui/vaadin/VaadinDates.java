package r01f.ui.vaadin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

import com.vaadin.ui.renderers.LocalDateRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.util.types.locale.Languages;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinDates {
/////////////////////////////////////////////////////////////////////////////////////////
//	LOCAL DATE TIME CONVERSION
/////////////////////////////////////////////////////////////////////////////////////////
	public static LocalDateTime localDateTimeFrom(final Date date) {
		return date != null ? date.toInstant()
								  .atZone(ZoneId.systemDefault())
								  .toLocalDateTime()
							: null;
	}
	public static Date dateFrom(final LocalDateTime localDate) {
		return localDate != null ? Date.from(localDate.atZone(ZoneId.systemDefault())
									   .toInstant())
								 : null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOCAL DATE CONVERSION
/////////////////////////////////////////////////////////////////////////////////////////
	public static LocalDate localDateFrom(final Date date) {
		return date != null ? date.toInstant()
								  .atZone(ZoneId.systemDefault())
								  .toLocalDate()
							: null;
	}
	public static Date dateFrom(final LocalDate localDate) {
		return localDate != null ? Date.from(localDate.atStartOfDay()
									   				  .atZone(ZoneId.systemDefault())
									   .toInstant())
								 : null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RENDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static LocalDateTimeRenderer createLocalDateTimeRenderer(final Locale locale,
																	final FormatStyle style) {
		return new LocalDateTimeRenderer(() -> DateTimeFormatter.ofLocalizedDateTime(style)
		        												.withLocale(locale));
	}
	public static LocalDateRenderer createLocalDateRenderer(final Locale locale,
															final FormatStyle style) {
		return new LocalDateRenderer(() -> DateTimeFormatter.ofLocalizedDate(style)
		        											.withLocale(locale));
	}
	public static LocalDateTimeRenderer createLocalDateTimeRenderer(final Language lang,
																	final FormatStyle style) {
		return VaadinDates.createLocalDateTimeRenderer(Languages.getLocale(lang),
												   	   style);
	}
	public static LocalDateRenderer createLocalDateRenderer(final Language lang,
															final FormatStyle style) {
		return VaadinDates.createLocalDateRenderer(Languages.getLocale(lang),
												   style);
	}
}

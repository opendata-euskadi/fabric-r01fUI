package r01f.ui.vaadin.styles;

import java.util.stream.Stream;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Layout.MarginHandler;
import com.vaadin.ui.Layout.SpacingHandler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinStyler {
/////////////////////////////////////////////////////////////////////////////////////////
//	MARGIN & SPACING
/////////////////////////////////////////////////////////////////////////////////////////	
	public static void setNoMargin(final MarginHandler... components) {
		if (CollectionUtils.isNullOrEmpty(components)) return;
		Stream.of(components)
			  .forEach(component -> component.setMargin(false));
	}
	public static void setNoSpacing(final SpacingHandler... components) {
		if (CollectionUtils.isNullOrEmpty(components)) return;
		Stream.of(components)
			  .forEach(component -> component.setSpacing(false));		
	}
	public static void setSizeFull(final Sizeable... components) {
		if (CollectionUtils.isNullOrEmpty(components)) return;
		Stream.of(components)
			  .forEach(component -> component.setSizeFull());		
	}	
}

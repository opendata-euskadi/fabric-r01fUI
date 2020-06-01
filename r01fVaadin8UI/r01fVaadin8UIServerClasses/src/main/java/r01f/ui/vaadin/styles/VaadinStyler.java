package r01f.ui.vaadin.styles;

import java.util.Collection;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout.AlignmentHandler;
import com.vaadin.ui.Layout.MarginHandler;
import com.vaadin.ui.Layout.SpacingHandler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinStyler {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Multiple ops in a row
	 * <pre class='brush:java'>
	 * 		VaadinStyler.on(comp1,comp2).setNoMargin();
	 * </pre>
	 * @param comps
	 * @return
	 */
	public static VaadinStylerStyleStep on(final Collection<? extends Component> comps) {
		return new VaadinStylerStyleStep(comps);
	}
	/**
	 * Multiple ops in a row
	 * <pre class='brush:java'>
	 * 		VaadinStyler.on(comp1,comp2).setNoMargin();
	 * </pre>
	 * @param comps
	 * @return
	 */
	public static VaadinStylerStyleStep on(final Component... comps) {
		return VaadinStyler.on(Lists.newArrayList(comps));
	}
	/**
	 * Same as <code>component.setMargin(false)</code>
	 * @param components
	 */
	public static void setNoMargin(final MarginHandler... components) {
		if (CollectionUtils.isNullOrEmpty(components)) return;
		Stream.of(components)
			  .forEach(component -> component.setMargin(false));
	}
	/**
	 * Same as <code>component.setSpacing(false)</code>
	 * @param components
	 */
	public static void setNoSpacing(final SpacingHandler... components) {
		if (CollectionUtils.isNullOrEmpty(components)) return;
		Stream.of(components)
			  .forEach(component -> component.setSpacing(false));		
	}
	/**
	 * Same as <code>component.setSizeFull(false)</code>
	 * @param components
	 */
	public static void setSizeFull(final Sizeable... components) {
		if (CollectionUtils.isNullOrEmpty(components)) return;
		Stream.of(components)
			  .forEach(component -> component.setSizeFull());		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MARGIN & SPACING
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class VaadinStylerStyleStep {
		private final Collection<? extends Component> _components;
		
		public VaadinStylerStyleStep setNoMargin() {
			if (CollectionUtils.isNullOrEmpty(_components)) return this;
			for (Component comp : _components) {
  				((MarginHandler)comp).setMargin(false);
			}
			return this;
		}
		public VaadinStylerStyleStep setNoSpacing() {
			if (CollectionUtils.isNullOrEmpty(_components)) return this;
			for (Component comp : _components) {
  				((SpacingHandler)comp).setSpacing(false);
			}
			return this;
		}
		public VaadinStylerStyleStep setSizeFull() {
			if (CollectionUtils.isNullOrEmpty(_components)) return this;
			for (Component comp : _components) {
  				((Sizeable)comp).setSizeFull();
			}
			return this;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets alignment of multiple child components
	 * <pre>
	 * 		VerticalLayout vly = new VerticalLayout(comp1,comp2,comp3);
	 * 		VaadinStyler.alignmentOfChildComponentsOf(vly)
	 * 					.setAlignment(Alignment.BOTOM_LEFT)
	 * 					.on(comp1,comp2,comp3);
	 * </pre>
	 * @param container
	 * @return
	 */
	public static VaadinStylerAlginmentStep alignmentOfChildComponentsOf(final AlignmentHandler container) {
		return new VaadinStylerAlginmentStep(container);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class VaadinStylerAlginmentStep {
		private final AlignmentHandler _container;
		
		public VaadinStylerChildComponentStep setAlignment(final Alignment alignment) {
			return new VaadinStylerChildComponentStep(_container,
													  alignment);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class VaadinStylerChildComponentStep {
		private final AlignmentHandler _container;
		private final Alignment _alignment;
		
		public void to(final Component... comps) {
			if (CollectionUtils.hasData(comps)) {
				for (Component comp : comps) {
					_container.setComponentAlignment(comp,_alignment);
				}
			}
		}
	}
}

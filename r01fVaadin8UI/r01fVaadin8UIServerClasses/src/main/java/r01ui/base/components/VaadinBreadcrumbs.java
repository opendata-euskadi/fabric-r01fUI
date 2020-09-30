package r01ui.base.components;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Lists;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinComponentHasCaption;
import r01f.ui.vaadin.view.VaadinComponentHasIcon;
import r01f.ui.vaadin.view.VaadinNavigator;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViewID;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * Breadcrumbs component like:
 * <pre>
 * 		Link > Link > Link
 * </pre>
 */
public class VaadinBreadcrumbs
	 extends Composite
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 2809187788911721517L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static final String BREAD_CRUMBS = "r01breadcrumbs";
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient UII18NService _i18n;

	private final Collection<R01UIBreadCrumb> _crumbs = Lists.newArrayList();
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinBreadcrumbs(final UII18NService i18n) {
		_i18n = i18n;
		super.setCompositionRoot(new HorizontalLayout());
		this.getCompositionRoot().setStyleName(BREAD_CRUMBS);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected HorizontalLayout getCompositionRoot() {
		return (HorizontalLayout)super.getCompositionRoot();
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	UNLINKED CRUMBS
/////////////////////////////////////////////////////////////////////////////////////////
	public void addUnLinkedCrumb(final String captionText) {
		_addCrumb(captionText,
				  null,null,	// fixed caption
				  null);		// no click listener
	}
	public void addUnLinkedCrumb(final I18NKey key) {
		_addCrumb(null,			// i18n-managed caption
				  key,null,
				  null);		// no click listener
	}
	public void addUnLinkedCrumb(final VaadinViewID to) {
		this.addUnLinkedCrumb(I18NKey.forId(to));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LINKED CRUMBS
/////////////////////////////////////////////////////////////////////////////////////////
	public void addLinkedCrumb(final String captionText,
							   final VaadinViewID to) {
		this.addLinkedCrumb(captionText,
							to,
							(Collection<String>)null);
	}
	public void addLinkedCrumb(final String captionText,
							   final VaadinViewID to,
							   final Map<String,String> navParams) {
		String urlPathParam = VaadinNavigator.vaadinViewUrlPathFragmentOf(to,navParams);
		this.addLinkedCrumb(captionText,
							selCrumb -> UI.getCurrent().getNavigator()
													   .navigateTo(urlPathParam));
	}
	public void addLinkedCrumb(final String captionText,
							   final VaadinViewID to,
							   final Collection<String> navParams) {
		String urlPathParam = VaadinNavigator.vaadinViewUrlPathParamOf(to,navParams);
		this.addLinkedCrumb(captionText,
							selCrumb -> UI.getCurrent().getNavigator()
													   .navigateTo(urlPathParam));
	}
	public void addLinkedCrumb(final I18NKey i18nKey,
							   final VaadinViewID to) {
		this.addLinkedCrumb(i18nKey,
							to,
							(Collection<String>)null);
	}
	public void addLinkedCrumb(final I18NKey i18nKey,
							   final VaadinViewID to,
							   final Map<String,String> navParams) {
		String urlPathParam = VaadinNavigator.vaadinViewUrlPathFragmentOf(to,navParams);
		this.addLinkedCrumb(i18nKey,
							selCrumb -> UI.getCurrent().getNavigator()
													   .navigateTo(urlPathParam));
	}
	public void addLinkedCrumb(final I18NKey i18nKey,
							   final VaadinViewID to,
							   final Collection<String> navParams) {
		String urlPathParam = VaadinNavigator.vaadinViewUrlPathParamOf(to,navParams);
		this.addLinkedCrumb(i18nKey,
							selCrumb -> UI.getCurrent().getNavigator()
													   .navigateTo(urlPathParam));
	}
	public void addLinkedCrumb(final VaadinViewID to) {
		this.addLinkedCrumb(to,
							(Collection<String>)null);	// no params
	}
	public void addLinkedCrumb(final VaadinViewID to,
							   final Collection<String> navParams) {
		this.addLinkedCrumb(I18NKey.forId(to),
							to,
							navParams);
	}
	public void addLinkedCrumb(final VaadinViewID to,
							   final Map<String,String> navParams) {
		this.addLinkedCrumb(I18NKey.forId(to),
							to,
							navParams);
	}
	public void addLinkedCrumb(final String captionText,
							   final ECI00UIBreadcrumSelectedEvent cmd) {
		_addCrumb(captionText,	// fixed caption
				  null,null,
				  cmd);
	}
	public void addLinkedCrumb(final I18NKey captionI18NKey,
							   final ECI00UIBreadcrumSelectedEvent cmd) {
		_addCrumb(null,
				  captionI18NKey,null,	// i18n-managed caption
				  cmd);
	}
	public void addLinkedCrumb(final I18NKey captionI18NKey,final I18NKey descI18NKey,
							   final ECI00UIBreadcrumSelectedEvent cmd) {
		_addCrumb(null,
				  captionI18NKey,descI18NKey,
				  cmd);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ADD
/////////////////////////////////////////////////////////////////////////////////////////
	private R01UIBreadCrumb _addCrumb(final String captionText,
									  final I18NKey captionI18NKey,final I18NKey descI18NKey,
							 		  final ECI00UIBreadcrumSelectedEvent cmd) {
		// add a separator if there already exists any crumb
		boolean hasBreadCrumbs = this.getCompositionRoot()
								   	 .getComponentCount() > 0;
		if (hasBreadCrumbs) {
			Component separator = _createSeparator();
			this.getCompositionRoot().addComponents(separator);
			this.getCompositionRoot().setComponentAlignment(separator,
															Alignment.MIDDLE_CENTER);
		}
		// store the crumb for later use
		R01UIBreadCrumb crumb = new R01UIBreadCrumb(captionText,
													captionI18NKey,descI18NKey,
													cmd);
		_crumbs.add(crumb);

		// add to the layout
		this.getCompositionRoot().addComponents(crumb.getComponent());
		this.getCompositionRoot().setComponentAlignment(crumb.getComponent(),
														Alignment.MIDDLE_CENTER);

		// add the click listener
		return crumb;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected Component _createSeparator() {
//		Label separator = new Label(VaadinIcons.CHEVRON_RIGHT.getHtml());
//		separator.setContentMode(ContentMode.HTML);
		Label separator = new Label();
		separator.setIcon(VaadinIcons.CHEVRON_RIGHT);
		return separator;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		if (CollectionUtils.isNullOrEmpty(_crumbs)) return;
		for (R01UIBreadCrumb crumb : _crumbs) {
			crumb.updateI18NMessages(i18n);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public class R01UIBreadCrumb
	  implements VaadinViewI18NMessagesCanBeUpdated,
	  			 VaadinComponentHasCaption,VaadinComponentHasIcon {

		@Getter private final Component _component;

		@Getter private final String _captionText;

		@Getter private final I18NKey _captionI18nKey;
		@Getter private final I18NKey _descriptionI18nkey;

		private R01UIBreadCrumb(final I18NKey captionI18NKey,final I18NKey descriptionI18NKey,
								final ECI00UIBreadcrumSelectedEvent cmd) {
			this(null,
				 captionI18NKey,descriptionI18NKey,
				 cmd);
		}
		private R01UIBreadCrumb(final String captionText,
								final I18NKey captionI18NKey,final I18NKey descriptionI18NKey,
								final ECI00UIBreadcrumSelectedEvent cmd) {
			_captionText = captionText;

			_captionI18nKey = captionI18NKey;
			_descriptionI18nkey	= descriptionI18NKey;

			// create the crumb
			String theCaption = Strings.isNOTNullOrEmpty(captionText) ? captionText
																	  : _captionI18nKey != null ? _i18n.getMessage(_captionI18nKey)
																			  			 		: "";

			String theDescription = _descriptionI18nkey != null ? _i18n.getMessage(_descriptionI18nkey) : "";

			// create the component
			if (cmd != null) {
				Button btn = _createCrumbButton(theCaption,
												theDescription);
				btn.addClickListener(e -> cmd.crumbSelected(this));
				_component = btn;
			}
			else {
				Label label = _createCrumbLabel(theCaption,
												theDescription);
				_component = label;
			}
		}
		private Button _createCrumbButton(final String caption,
										  final String description) {
			final Button crumb = new Button();
			crumb.setCaption(caption);
			crumb.setDescription(description);
			crumb.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			return crumb;
		}
		private Label _createCrumbLabel(final String caption,
										final String description) {
			final Label crumb = new Label();
			crumb.setValue(caption);
			crumb.setDescription(description);
			crumb.addStyleNames(ValoTheme.LABEL_HUGE, ValoTheme.LABEL_BOLD);
			return crumb;
		}
		////////// ---------- CAPTION & ICON
		@Override
		public String getCaption() {
			return _component.getCaption();
		}
		@Override
		public void setCaption(final String caption) {
			_component.setCaption(caption);
		}
		@Override
		public Resource getIcon() {
			return _component.getIcon();
		}
		@Override
		public void setIcon(final Resource resource) {
			_component.setIcon(resource);
		}
		////////// ---------- CAPTION & ICON
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			String theCaption = _captionI18nKey != null ? i18n.getMessage(_captionI18nKey)
											 	 : _captionText;
			if (_component instanceof Label) { // unlinked
				((Label)_component).setValue(theCaption);
			}
			else { // linked
				_component.setCaption(theCaption);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT
/////////////////////////////////////////////////////////////////////////////////////////
    @FunctionalInterface
    public interface ECI00UIBreadcrumSelectedEvent
             extends Serializable {
        public void crumbSelected(R01UIBreadCrumb selCrumb);
    }
}

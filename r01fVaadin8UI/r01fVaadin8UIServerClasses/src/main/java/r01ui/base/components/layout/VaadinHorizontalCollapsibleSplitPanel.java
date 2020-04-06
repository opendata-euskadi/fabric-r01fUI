package r01ui.base.components.layout;

import java.util.Iterator;
import java.util.function.Consumer;

import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractSplitPanel.SplitPositionChangeListener;
import com.vaadin.ui.AbstractSplitPanel.SplitterClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;

/**
 * An horizontal split panel whose left column can be hidden
 * <pre class='brush:java'>
 * 
 * 		 [<<] +-------+----------------------+
 * 			  |       |                      |
 * 			  |       |                      |
 * 			  |       |                      |
 * 			  |       |                      |
 * 			  |       |                      |
 * 			  +-------+----------------------+
 * 		when the [<<] button is clicked the left panel is hidden
 * 		 [>>] +------------------------------+
 * 			  |                              |
 * 			  |                              |
 * 			  |                              |
 * 			  |                              |
 * 			  |                              |
 * 			  +------------------------------+
 * </pre>
 */
public class VaadinHorizontalCollapsibleSplitPanel 
	 extends Composite {

	private static final long serialVersionUID = 7176525670560907498L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
	
	protected final Button _btnToggleLeftPanel;
	protected final HorizontalSplitPanel _hSplit;
	
	private float _prevSplitPosition;
	private Unit _prevSplitPositionUnit;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHorizontalCollapsibleSplitPanel(final UII18NService i18n) {
		_i18n = i18n;
		
		// button
		_btnToggleLeftPanel = new Button(VaadinIcons.CHEVRON_LEFT);
		_btnToggleLeftPanel.addStyleNames(ValoTheme.BUTTON_BORDERLESS,
									      ValoTheme.BUTTON_ICON_ONLY,
									      "collapsible-split-btn");
		// split panel
		_hSplit = new HorizontalSplitPanel();
		
		// layout
		_createLayout(_btnToggleLeftPanel,_hSplit);
	}
	public VaadinHorizontalCollapsibleSplitPanel(final UII18NService i18n,
												 final Component firstComponent,final Component secondComponent) {
		_i18n = i18n;

		// button
		_btnToggleLeftPanel = new Button(VaadinIcons.CHEVRON_LEFT);
		_btnToggleLeftPanel.addStyleNames(ValoTheme.BUTTON_BORDERLESS,
									      ValoTheme.BUTTON_ICON_ONLY);
		// split panel
		_hSplit = new HorizontalSplitPanel(firstComponent,
										   secondComponent);
		// layout
		_createLayout(_btnToggleLeftPanel,_hSplit);
	}
	private void _createLayout(final Button btn,final HorizontalSplitPanel hsplit) {
		HorizontalLayout hly = new HorizontalLayout(btn,hsplit);
		hly.setExpandRatio(btn,0);		// exact
		hly.setExpandRatio(hsplit,1);
		
		hly.setWidthFull();
		hly.setHeightFull();
		
		hly.setMargin(false);
		hly.setSpacing(false);
		this.setCompositionRoot(hly);
		
		// behavior
		_setBehavior();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEHAVIOR
/////////////////////////////////////////////////////////////////////////////////////////
	private void _setBehavior() {
		_btnToggleLeftPanel.addClickListener(clickEvent -> _toggleLeftPanelVisibility());
	}
	protected void _toggleLeftPanelVisibility() {
		if (_hSplit.getStyleName().contains(VaadinValoTheme.MENU_HIDE)) {
			// show left panel
			_btnToggleLeftPanel.setIcon(VaadinIcons.CHEVRON_LEFT);
			_btnToggleLeftPanel.setDescription(_i18n.getMessage("hide"));
			_hSplit.removeStyleName("hide-left-panel");
			_hSplit.setSplitPosition(_prevSplitPosition,_prevSplitPositionUnit,
									 false);	// reverse
		} else {
			// hide left panel
			_btnToggleLeftPanel.setIcon(VaadinIcons.CHEVRON_RIGHT);
			_btnToggleLeftPanel.setDescription(_i18n.getMessage("show"));
			_hSplit.addStyleName("hide-left-panel");
			_hSplit.setSplitPosition(0,Unit.PIXELS,
									 false);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATED: GET / SET COMPONENTS
/////////////////////////////////////////////////////////////////////////////////////////	
	public void addComponents(final Component... components) {
		_hSplit.addComponents(components);
	}
	public void addComponent(final Component c) {
		_hSplit.addComponent(c);
	}
	public void removeAllComponents() {
		_hSplit.removeAllComponents();
	}
	public void setFirstComponent(final Component c) {
		_hSplit.setFirstComponent(c);
	}
	public void setSecondComponent(final Component c) {
		_hSplit.setSecondComponent(c);
	}
	public void removeComponent(final Component c) {
		_hSplit.removeComponent(c);
	}
	public void replaceComponent(final Component oldComponent, final Component newComponent) {
		_hSplit.replaceComponent(oldComponent, newComponent);
	}
	public Component getFirstComponent() {
		return _hSplit.getFirstComponent();
	}
	public Component getSecondComponent() {
		return _hSplit.getSecondComponent();
	}
	public void forEachPanel(final Consumer<? super Component> action) {
		_hSplit.forEach(action);
	}
	public Iterator<Component> panelIterator() {
		return _hSplit.iterator();
	}
	public int getComponentCount() {
		return _hSplit.getComponentCount();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE: SPLIT POSITION
/////////////////////////////////////////////////////////////////////////////////////////	
	public float getSplitPosition() {
		return _hSplit.getSplitPosition();
	}
	public Unit getSplitPositionUnit() {
		return _hSplit.getSplitPositionUnit();
	}
	public boolean isSplitPositionReversed() {
		return _hSplit.isSplitPositionReversed();
	}
	public void setSplitPosition(final float pos) {
		_hSplit.setSplitPosition(pos);
		_prevSplitPosition = pos;
		_prevSplitPositionUnit = _hSplit.getSplitPositionUnit();
	}
	public void setSplitPosition(final float pos,final boolean reverse) {
		_hSplit.setSplitPosition(pos,reverse);
		_prevSplitPosition = pos;
	}
	public void setSplitPosition(final float pos,final Unit unit) {
		_hSplit.setSplitPosition(pos,unit);
		_prevSplitPosition = pos;
		_prevSplitPositionUnit = _hSplit.getSplitPositionUnit();
	}
	public void setSplitPosition(final float pos,final Unit unit,
								 final boolean reverse) {
		_hSplit.setSplitPosition(pos,unit,
								 reverse);
		_prevSplitPosition = pos;
		_prevSplitPositionUnit = _hSplit.getSplitPositionUnit();
	}
	public float getMinSplitPosition() {
		return _hSplit.getMinSplitPosition();
	}
	public Unit getMinSplitPositionUnit() {
		return _hSplit.getMinSplitPositionUnit();
	}
	public void setMaxSplitPosition(final float pos,final Unit unit) {
		_hSplit.setMaxSplitPosition(pos,unit);
	}
	public float getMaxSplitPosition() {
		return _hSplit.getMaxSplitPosition();
	}
	public Unit getMaxSplitPositionUnit() {
		return _hSplit.getMaxSplitPositionUnit();
	}
	public void setLocked(final boolean locked) {
		_hSplit.setLocked(locked);
	}
	public boolean isLocked() {
		return _hSplit.isLocked();
	}
	public Registration addSplitterClickListener(final SplitterClickListener listener) {
		return _hSplit.addSplitterClickListener(listener);
	}
	public Registration addSplitPositionChangeListener(final SplitPositionChangeListener listener) {
		return _hSplit.addSplitPositionChangeListener(listener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE: STYLING
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getStyleName() {
		return _hSplit.getStyleName();
	}
	@Override
	public void setStyleName(final String style, final boolean add) {
		_hSplit.setStyleName(style, add);
	}

	@Override
	public void setStyleName(final String style) {
		_hSplit.setStyleName(style);
	}
	@Override
	public void setPrimaryStyleName(final String style) {
		_hSplit.setPrimaryStyleName(style);
	}
	@Override
	public String getPrimaryStyleName() {
		return _hSplit.getPrimaryStyleName();
	}
	@Override
	public void addStyleName(final String style) {
		_hSplit.addStyleName(style);
	}
	@Override
	public void removeStyleName(final String style) {
		_hSplit.removeStyleName(style);
	}
	@Override
	public void addStyleNames(final String... styles) {
		_hSplit.addStyleNames(styles);
	}
	@Override
	public void removeStyleNames(final String... styles) {
		_hSplit.removeStyleNames(styles);
	}
	@Override
	public void setWidth(final float width,final Unit unit) {
		_hSplit.setWidth(width,unit);
	}
	@Override
	public void setHeight(final float height, final Unit unit) {
		_hSplit.setHeight(height, unit);
	}
	@Override
	public void setWidth(final String width) {
		_hSplit.setWidth(width);
	}
	@Override
	public void setHeight(final String height) {
		_hSplit.setHeight(height);
	}
	@Override
	public float getHeight() {
		return _hSplit.getHeight();
	}
	@Override
	public Unit getHeightUnits() {
		return _hSplit.getHeightUnits();
	}
	@Override
	public float getWidth() {
		return _hSplit.getWidth();
	}
	@Override
	public Unit getWidthUnits() {
		return _hSplit.getWidthUnits();
	}
	@Override
	public void setSizeFull() {
		_hSplit.setSizeFull();
	}
	@Override
	public void setWidthFull() {
		_hSplit.setWidthFull();
	}
	@Override
	public void setHeightFull() {
		_hSplit.setHeightFull();
	}
	@Override
	public void setSizeUndefined() {
		_hSplit.setSizeUndefined();
	}
	@Override
	public void setWidthUndefined() {
		_hSplit.setWidthUndefined();
	}
	@Override
	public void setHeightUndefined() {
		_hSplit.setHeightUndefined();
	}
	@Override
	public void setResponsive(final boolean responsive) {
		_hSplit.setResponsive(responsive);
	}
	@Override
	public boolean isResponsive() {
		return _hSplit.isResponsive();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Resource getIcon() {
		return _hSplit.getIcon();
	}
	@Override
	public void setIcon(final Resource icon) {
		_hSplit.setIcon(icon);
	}
	@Override
	public String getCaption() {
		return _hSplit.getCaption();
	}
	@Override
	public void setCaption(final String caption) {
		_hSplit.setCaption(caption);
	}
	@Override
	public void setCaptionAsHtml(final boolean captionAsHtml) {
		_hSplit.setCaptionAsHtml(captionAsHtml);
	}
	@Override
	public boolean isCaptionAsHtml() {
		return _hSplit.isCaptionAsHtml();
	}
	@Override
	public String getDescription() {
		return _hSplit.getDescription();
	}	
	@Override
	public void setDescription(final String description) {
		_hSplit.setDescription(description);
	}
	@Override
	public void setDescription(final String description, final ContentMode mode) {
		_hSplit.setDescription(description, mode);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATES
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Registration addShortcutListener(final ShortcutListener shortcut) {
		return _hSplit.addShortcutListener(shortcut);
	}
	@Override
	public Registration addContextClickListener(final ContextClickListener listener) {
		return _hSplit.addContextClickListener(listener);
	}
}

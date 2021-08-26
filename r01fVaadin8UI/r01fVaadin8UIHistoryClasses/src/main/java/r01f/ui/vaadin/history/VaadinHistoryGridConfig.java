package r01f.ui.vaadin.history;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
public class VaadinHistoryGridConfig 
  implements Serializable  {
	
	private static final long serialVersionUID = -6737712673359944056L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Show the details column
	 */
	@Getter @Setter private boolean _showDetailsColumn = true;
/////////////////////////////////////////////////////////////////////////////////////////
// 	BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public static VaadinHistoryPopUpConfigBuilderShowStep builder() {
		return new VaadinHistoryPopUpConfigBuilderShowStep(new VaadinHistoryGridConfig());
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class VaadinHistoryPopUpConfigBuilderShowStep {
		private final VaadinHistoryGridConfig _cfg;
		
		public VaadinHistoryPopUpConfigBuilderShowStep showDetailsColumn() {
			_cfg.setShowDetailsColumn(true);
			return this;
		}
		public VaadinHistoryPopUpConfigBuilderShowStep doNOTShowDetailsColumn() {
			_cfg.setShowDetailsColumn(false);
			return this;
		}
		public VaadinHistoryGridConfig build() {
			return _cfg;
		}		
	}
}

package r01ui.base.components.url.weblink;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Options to configure the [weblink builder]
 */
@Accessors(prefix="_")
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public class VaadinWebLinkFormFeatures 
  implements Serializable {

	private static final long serialVersionUID = 2935521576908437829L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Show the link text input
	 */
	@Getter private final boolean _showLinkTextInput;
	/**
	 * Show the link description input
	 */
	@Getter private final boolean _showLinkDescriptionInput;
	/**
	 * Show link opening features
	 */
	@Getter private final boolean _showLinkOpeningFeatures;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static VaadinWebLinkFormFeatures byDefault() {
		return new VaadinWebLinkFormFeatures(true,
													  true,
													  true);
	}
	public static R01UIUrlBuilderWebLinkFormFeaturesDescriptionShowStep showLinkTextInput(final boolean show) {
		return new R01UIUrlBuilderWebLinkFormFeaturesDescriptionShowStep(show);
	}	
	public static R01UIUrlBuilderWebLinkFormFeaturesDescriptionShowStep showLinkTextInput() {
		return new R01UIUrlBuilderWebLinkFormFeaturesDescriptionShowStep(true);
	}	
	public static R01UIUrlBuilderWebLinkFormFeaturesDescriptionShowStep doNOTShowLinkTextInput() {
		return new R01UIUrlBuilderWebLinkFormFeaturesDescriptionShowStep(false);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class R01UIUrlBuilderWebLinkFormFeaturesDescriptionShowStep {
		private final boolean _showLinkTextInput;
		
		public R01UIUrlBuilderWebLinkFormFeaturesOpeningFeaturesShowStep showLinkDescriptionInput(final boolean show) {
			return new R01UIUrlBuilderWebLinkFormFeaturesOpeningFeaturesShowStep(_showLinkTextInput,
																				 show);
		}
		public R01UIUrlBuilderWebLinkFormFeaturesOpeningFeaturesShowStep showLinkDescriptionInput() {
			return new R01UIUrlBuilderWebLinkFormFeaturesOpeningFeaturesShowStep(_showLinkTextInput,
																				 true);
		}
		public R01UIUrlBuilderWebLinkFormFeaturesOpeningFeaturesShowStep doNOTShowLinkDescriptionInput() {
			return new R01UIUrlBuilderWebLinkFormFeaturesOpeningFeaturesShowStep(_showLinkTextInput,
																				 false);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class R01UIUrlBuilderWebLinkFormFeaturesOpeningFeaturesShowStep {
		private final boolean _showLinkTextInput;
		private final boolean _showLinkDescriptionInput;
		
		public R01UIUrlBuilderWebLinkFormFeaturesBuildShowStep showLinkOpeningFeatures(final boolean show) {
			return new R01UIUrlBuilderWebLinkFormFeaturesBuildShowStep(_showLinkTextInput,
																	   _showLinkDescriptionInput,
																	   show);
		}
		public R01UIUrlBuilderWebLinkFormFeaturesBuildShowStep showLinkOpeningFeatures() {
			return new R01UIUrlBuilderWebLinkFormFeaturesBuildShowStep(_showLinkTextInput,
																	   _showLinkDescriptionInput,
																	   true);
		}
		public R01UIUrlBuilderWebLinkFormFeaturesBuildShowStep doNOTShowLinkOpeningFeatures() {
			return new R01UIUrlBuilderWebLinkFormFeaturesBuildShowStep(_showLinkTextInput,
																	   _showLinkDescriptionInput,
																	   false);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class R01UIUrlBuilderWebLinkFormFeaturesBuildShowStep {
		private final boolean _showLinkTextInput;
		private final boolean _showLinkDescriptionInput;
		private final boolean _showLinkOpeningFeatures;
		
		public VaadinWebLinkFormFeatures build() {
			return new VaadinWebLinkFormFeatures(_showLinkTextInput,
														  _showLinkDescriptionInput,
														  _showLinkOpeningFeatures);
		}
	}
}

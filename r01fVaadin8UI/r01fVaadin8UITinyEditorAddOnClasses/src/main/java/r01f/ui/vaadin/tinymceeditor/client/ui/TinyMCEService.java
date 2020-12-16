package r01f.ui.vaadin.tinymceeditor.client.ui;

import com.google.gwt.dom.client.NativeEvent;

/**
 * GWT wrapper for TinyMCE. 
 *
 */
public class TinyMCEService {
	/**
	 * Use this method to load editor to given identifier.
	 * 
	 * @param id the identifier for the element you want to replace with TinyMCE
	 * @param listener this listener will get notified by changes in editor
	 * @param cc possible custom configuration for editor as string (eg. {theme : "simple"}
	 */
	public static native void loadEditor(String id, OnChangeListener listener, String cc)
	/*-{

		var conf = {
			auto_focus: id,	
			selector: '#' + id,
			plugins: [
				'advlist autolink lists image charmap preview hr anchor pagebreak',
				'searchreplace wordcount visualblocks visualchars code fullscreen',
				'insertdatetime media link nonbreaking save table directionality',
				'emoticons template paste textcolor colorpicker textpattern imagetools'
				],			 			
 			menu: {    			
    			edit: {title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall'},
    			insert: {title: 'Insert', items: 'link media image | charmap hr anchor pagebreak date'},
    			view: {title: 'View', items: ' visualchars visualblocks visualaid | preview'},
    			format: {title: 'Format', items: 'bold italic underline strikethrough superscript subscript | formats | removeformat'},
    			table: {title: 'Table', items: 'inserttable tableprops deletetable | cell row column'},
    			tools: {title: 'Tools', items: 'spellchecker code'}
  			},  							
  							
			toolbar1: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link unlink image media gallery geoeuskadi',
			toolbar2: 'preview',			
			image_advtab: true,
			table_advtab: false,
			table_cell_advtab: false,
			table_row_advtab: false,
			table_appearance_options: false,
			valid_elements: "*[*]",
			extended_valid_elements: 'script[language|type|src]',
			valid_children: '-p[script]',
			relative_urls: false,
			verify_html: false,
			allow_script_urls:true,			
			remove_script_host : false,				
			convert_urls : false,	
			code_dialog_width: 1024,
			code_dialog_height: 600,			
									 
			setup: function(editor) {
				editor.on('change', function(e) {
					listener.@r01f.ui.vaadin.tinymceeditor.client.ui.TinyMCEService.OnChangeListener::onChange()();
				});
				editor.on('setContent', function(e) {
					listener.@r01f.ui.vaadin.tinymceeditor.client.ui.TinyMCEService.OnChangeListener::onChange()();
				});
			}
		}; 

		try {
			if (cc) {
				var customConfig = eval('('+cc+')');
				for (var j in customConfig) {
					conf[j] = customConfig[j];
				}
			}
		} catch (e) {}
		$wnd.tinymce.init(conf);
	}-*/
	;
	
	/**
	 * Returns a javascript overlay of TinyMCE editor for given identifier.
	 * 
	 * @param id
	 * @return the overlay for TinyMCE.Editor or null in not yet inited
	 */
	public static native TinyMCEditor get(String id)
	/*-{
		return $wnd.tinymce.get(id);
	}-*/;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public interface OnChangeListener {
		
		public void onChange();
		
		public void onEvent(NativeEvent event);
	}

}

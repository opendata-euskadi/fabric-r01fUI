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
				'r01advlist autolink lists r01image charmap preview hr anchor pagebreak',
				'searchreplace wordcount visualblocks visualchars code fullscreen',
				'insertdatetime r01media r01link nonbreaking save r01table directionality',
				'emoticons template paste textcolor colorpicker textpattern imagetools r01gallery r01geoeuskadi ctic_taw ctic_legibilidad axe_frame'
				],			 			
 			menu: {    			
    			edit: {title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall'},
    			insert: {title: 'Insert', items: 'r01geoeuskadi r01gallery r01link r01media r01image | charmap hr anchor pagebreak date'},
    			view: {title: 'View', items: ' visualchars visualblocks visualaid | preview'},
    			format: {title: 'Format', items: 'bold italic underline strikethrough superscript subscript | formats | removeformat'},
    			table: {title: 'Table', items: 'inserttable tableprops deletetable | cell row column'},
    			tools: {title: 'Tools', items: 'spellchecker code'}
  			},  							
  							
			toolbar1: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | r01link unlink r01image r01media r01gallery r01geoeuskadi',
			toolbar2: 'preview | ctic_taw ctic_legibilidad axe_frame',			
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
			formats : {			
				alignleft: [
							{selector : 'p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li,table', classes : 'r01AlignLeft'},
							{selector : 'img', classes : 'r01AlignImgLeft', ceFalseOverride: true},
							{selector: 'figure.image', collapsed: false, classes: 'align-left', ceFalseOverride: true},
							{selector: 'figure,p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li',
								styles: {textAlign: 'left'},
								inherit: false,
								defaultBlock: 'div'
							},
							{selector: 'img,table', collapsed: false, styles: {'float': 'left'}}
						],		
				aligncenter: [
								{selector : 'p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li,table,img', classes : 'r01AlignCenter'},								
								{selector: 'figure,p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li',
									styles: {textAlign: 'center'},
									inherit: false,
									defaultBlock: 'div'
								},
								{selector: 'figure.image', collapsed: false, classes: 'align-center', ceFalseOverride: true},
								{selector: 'img', collapsed: false, styles: {display: 'block', marginLeft: 'auto', marginRight: 'auto'}},
								{selector: 'table', collapsed: false, styles: {marginLeft: 'auto', marginRight: 'auto'}}
							],					
				alignright: [
				             	{selector : 'p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li,table', classes : 'r01AlignRight'},
				             	{selector : 'img', classes : 'r01AlignImgRight', ceFalseOverride: true},
								{selector: 'figure.image', collapsed: false, classes: 'align-right', ceFalseOverride: true},
								{selector: 'figure,p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li',
									styles: {textAlign: 'right'},
									inherit: false,
									defaultBlock: 'div'
								},
								{selector: 'img,table', collapsed: false, styles: {'float': 'right'}}
							],						
				alignjustify: [
				               	{selector : 'p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li,table', classes : 'r01AlignJustify'},
								{selector: 'figure,p,h1,h2,h3,h4,h5,h6,td,th,tr,div,ul,ol,li',
									styles: {textAlign: 'justify'},
									inherit: false,
									defaultBlock: 'div'
								}
							],
				valigntop: [
					{selector: 'td,th', classes: 'r01VAlignTop'},        
					{selector: 'td,th', styles: {'verticalAlign': 'top'}}
				],

				valignmiddle: [
					{selector: 'td,th', classes: 'r01VAlignMiddle'},           
					{selector: 'td,th', styles: {'verticalAlign': 'middle'}}
				],

				valignbottom: [
					{selector: 'td,th', classes: 'r01VAlignBottom'},           
					{selector: 'td,th', styles: {'verticalAlign': 'bottom'}}
				],			
				bold : [
					{inline: 'strong', remove: 'all'},
					{inline: 'span', classes: 'r01Bold'},
					{inline: 'span', styles: {fontWeight: 'bold'}},									
					{inline: 'b', remove: 'all'}
				],
				italic : [
					{inline: 'em', remove: 'all'},	
					{inline: 'span', classes : 'r01Italic'},
					{inline: 'span', styles: {fontStyle: 'italic'}},							
					{inline: 'i', remove: 'all'}								
				],
				underline : [					
					{inline: 'span', classes : 'r01Underline'},
					{inline: 'span', styles: {textDecoration: 'underline'}, exact: true},
					{inline: 'u', remove: 'all'}
				],
				strikethrough: [
					{inline: 'del', remove: 'all'},
					{inline: 'span', styles: {textDecoration: 'line-through'}, exact: true},
					{inline: 'strike', remove: 'all'}
				]			
			},
			image_class_list: [
    			{title: '', value: ''},
    			{title: 'Align left', value: 'r01AlignImgLeft'},
    			{title: 'Align left (for list)', value: 'r01AlignImgLeftList'},
    			{title: 'Align right', value: 'r01AlignImgRight'}
  			],
				
			content_css: ["/comun/tinymce/jscripts/tiny_mce/themes/r01advanced/css/r01gTinyContent.css", "/comun/tinymce/jscripts/tiny_mce/plugins/r01omedia/css/r01ocontent.css"],

			 
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
			if(cc) {
				var customConfig = eval('('+cc+')');
				for(var j in customConfig) {
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

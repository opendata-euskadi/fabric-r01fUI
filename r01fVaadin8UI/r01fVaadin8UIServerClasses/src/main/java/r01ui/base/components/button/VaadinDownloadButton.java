package r01ui.base.components.button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.ui.Button;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix="_",chain=true)
public class VaadinDownloadButton 
	 extends Button {

	private static final long serialVersionUID = -6075512584826294059L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public interface R01UIContentWriter 
			 extends Serializable {
		void write(OutputStream stream);
	}
	public interface R01UIDownloadCompletedListener 
			 extends Serializable {
		void onCompleted();
	}
	public interface FileNameProvider 
			 extends Serializable {
		String getFileName();
	}
	public interface MimeTypeProvider 
			 extends Serializable {
		String getMimeType();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private Collection<R01UIDownloadCompletedListener> _downloadCompletedListeners;

	@Getter @Setter private R01UIContentWriter _writer;
	@Getter @Setter private MimeTypeProvider _mimeTypeProvider;
	@Getter @Setter private FileNameProvider _fileNameProvider;
	
	// override a vaadin's StreamResource used for download from a stream
	private final StreamResource _streamResource = new StreamResource(// the stream source
																	  new StreamResource.StreamSource() {
																				private static final long serialVersionUID = 3641967669172064511L;
																		
																				@Override @SuppressWarnings("resource")
																				public InputStream getStream() {
																					try {
																						// data is writtern to the outputstream
																						// and readed at the inputstream
																						PipedOutputStream out = new PipedOutputStream();
																						PipedInputStream in = new PipedInputStream(out);
																						VaadinDownloadButton.this.writeResponse(out);
																						return in;
																					} catch (IOException ex) {
																						throw new RuntimeException(ex);
																					}
																				}
																	  },
																	  // the file name
																	  "") {
			private static final long serialVersionUID = -8221900203840804581L;
	
			@Override
			public String getFilename() {
				return _fileNameProvider != null ? _fileNameProvider.getFileName()
												 : VaadinDownloadButton.this.getFileName();
			}
			@Override
			public String getMIMEType() {
				return _mimeTypeProvider != null ? _mimeTypeProvider.getMimeType()
												 : super.getMIMEType();
			}
	};
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructs a new Download button without ContentWriter. Be sure to set the
	 * ContentWriter or override its getter, before instance is actually used.
	 */
	public VaadinDownloadButton() {
		// Extension that starts a download when the extended component is clicked
		// This is used to overcome two challenges:
		// 		- Resource should be bound to a component to allow it to be garbage
		//		  collected when there are no longer any ways of reaching the resource.
		//		- Download should be started directly when the user clicks e.g. a Button
		//		   without going through a server-side click listener to avoid triggering
		//		   security warnings in some browsers.
		// Please note that the download will be started in an iframe, which means that
		// care should be taken to avoid serving content types that might make the
		// browser attempt to show the content using a plugin instead of downloading it
		new FileDownloader(_streamResource) {
			private static final long serialVersionUID = -362906453359065643L;

			@Override
			public boolean handleConnectorRequest(final VaadinRequest request,
												  final VaadinResponse response, 
												  final String path) throws IOException {
				boolean handleConnectorRequest = super.handleConnectorRequest(request, 
																			  response, 
																			  path);
				// tell [download completed listeners] that the response has been written
				if (handleConnectorRequest) VaadinDownloadButton.this.afterResponseWritten();
				return handleConnectorRequest;
			}
		}.extend(this);

	}
	public VaadinDownloadButton(final R01UIContentWriter writer) {
		this();
		_writer = writer;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DOWNLOAD COMPLETE LISTENER
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Adds a listener that is notified when the download has been sent to the client. 
	 * Note that you need to enable push connection or use UI.setPollingInterval to make 
	 * UI modifications visible automatically.
	 * 
	 * @param listener the listener to be notified
	 * @return the download button
	 */
	public VaadinDownloadButton addDownloadCompletedListener(final R01UIDownloadCompletedListener listener) {
		if (_downloadCompletedListeners == null) _downloadCompletedListeners = new LinkedHashSet<>();
		_downloadCompletedListeners.add(listener);
		return this;
	}
	public void removeDownloadCompletedListener(final R01UIDownloadCompletedListener listener) {
		_downloadCompletedListeners.remove(listener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * A hook for extension to do something after the response has been sent to the
	 * client.
	 */
	protected void afterResponseWritten() {
		if (_downloadCompletedListeners == null) return;
		this.getUI()
			// provides exclusive access to this UI from outside a request handling thread
			// (beware that this method is called from a spawned thread at #writeResponse method)
			.access(() -> {
						_downloadCompletedListeners.forEach(R01UIDownloadCompletedListener::onCompleted);
					});
	}
	/**
	 * By default just spans a new raw thread to get the input. 
	 * For strict Java EE fellows, this might not suite, so override and use executor service.
	 *
	 * @param out the output stream where the output is targeted
	 */
	protected void writeResponse(final PipedOutputStream out) {
		// spawn a new thread used to write the output to the client
		new Thread() {
				@Override
				public void run() {
					try {
						_writer.write(out);
						out.close();
					} catch (Exception e) {
						getUI().access(() -> {
											_handleErrorInFileGeneration(e);
									   });
					}
				}
		}.start();
	}
	protected void _handleErrorInFileGeneration(final Exception e) {
		this.setComponentError(new UserError(e.getMessage()));
		this.fireComponentErrorEvent();
		throw new RuntimeException(e);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET & SET
/////////////////////////////////////////////////////////////////////////////////////////	
	public String getMimeType() {
		return _streamResource.getMIMEType();
	}
	public VaadinDownloadButton setMimeType(final String mimeType) {
		_streamResource.setMIMEType(mimeType);
		return this;
	}
	public VaadinDownloadButton setCacheTime(final long cacheTime) {
		_streamResource.setCacheTime(cacheTime);
		return this;
	}
	public String getFileName() {
		return _fileNameProvider != null ? _fileNameProvider.getFileName() 
										 : "file";
	}
	public VaadinDownloadButton setFileName(final String fileName) {
		_fileNameProvider = new FileNameProvider() {
								private static final long serialVersionUID = -3449552786114328636L;
					
								@Override
								public String getFileName() {
									return fileName;
								}
							};
		return this;
	}
	public VaadinDownloadButton withIcon(final Resource icon) {
		this.setIcon(icon);
		return this;
	}
	public VaadinDownloadButton withCaption(final String caption) {
		this.setCaption(caption);
		return this;
	}
}
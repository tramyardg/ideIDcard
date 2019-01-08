package org.ideidcard;

import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class AppMainWindow {

    private final Logger logger = Logger.getLogger(AppMainWindow.class.getName());

    protected Display display;
    protected Shell shell;
    protected Group grpImagePreview;
    protected Group grpExtractedData;
    protected Label lblSelectedImageDirectory;
    protected Label lblSelectedImageDirectorySub;
    protected Label lblImageHere;
    protected ProgressBar progressBar;

    protected Menu menuBar;
    protected Menu fileMenu;
    protected Menu importMenu;
    protected Menu saveMenu;
    private Text extractedText;
    private MenuItem mnExtract;
    private MenuItem mnSaveExtracted;

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
	try {
	    AppMainWindow window = new AppMainWindow();
	    window.run();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Open the window.
     */
    public void run() {
	display = new Display();
	shell = new Shell(display, SWT.SHELL_TRIM);
	menuBar = new Menu(shell, SWT.BAR);
	fileMenu = new Menu(shell, SWT.DROP_DOWN);
	importMenu = new Menu(shell, SWT.DROP_DOWN);
	saveMenu = new Menu(shell, SWT.DROP_DOWN);

	mainMenuFile();
	importImageMenuItem();
	exitMenuItem();
	saveMenuItem();

	shell.setMenuBar(menuBar);
	createContents(shell);

	grpImagePreview = new Group(shell, SWT.NONE);
	grpImagePreview.setText("Image preview");
	grpImagePreview.setBounds(10, 10, 519, 383);

	lblImageHere = new Label(grpImagePreview, SWT.NONE);
	lblImageHere.setAlignment(SWT.CENTER);
	lblImageHere.setBounds(10, 20, 499, 353);
	lblImageHere.setText("");

	lblSelectedImageDirectory = new Label(shell, SWT.NONE);
	lblSelectedImageDirectory.setBounds(10, 399, 136, 15);
	lblSelectedImageDirectory.setText("Selected image directory");

	lblSelectedImageDirectorySub = new Label(shell, SWT.NONE);
	lblSelectedImageDirectorySub.setBounds(10, 420, 519, 15);
	lblSelectedImageDirectorySub.setText("");

	grpExtractedData = new Group(shell, SWT.NONE);
	grpExtractedData.setText("Extracted data");
	grpExtractedData.setBounds(535, 10, 383, 383);

	extractedText = new Text(grpExtractedData, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.BORDER);
	extractedText.setBounds(10, 21, 363, 353);

	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) {
		display.sleep();
	    }
	}
    }

    class SelectImage implements SelectionListener {
	private String readImage(String imageLocation) {
	    ITesseract instance = new Tesseract();
	    try {
		return instance.doOCR(new File(imageLocation));
	    } catch (TesseractException e) {
		e.getMessage();
		return "Error while reading image";
	    }
	}

	public void widgetSelected(SelectionEvent event) {
	    FileDialog openImageDialog = new FileDialog(shell, SWT.OPEN);
	    openImageDialog.setText("Open");
	    String homeDir = System.getProperty("user.home");
	    openImageDialog.setFilterPath(homeDir);
	    String[] filterExt = { "*.JPG", "*.PNG", "*.GIF", "*.BMP" };
	    openImageDialog.setFilterExtensions(filterExt);
	    final String selected = openImageDialog.open();
	    if (selected != null) {
		mnExtract.setEnabled(true);
		lblSelectedImageDirectorySub.setText(selected);
		lblSelectedImageDirectorySub.pack();
		Image image = new Image(display, selected);

		mnExtract.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
			extractedText.setText(readImage(selected));
			mnSaveExtracted.setEnabled(true);
		    }
		});

		int imgWidth = image.getBounds().width;
		int imgHeight = image.getBounds().height;
		Image scaled050 = new Image(display,
			image.getImageData().scaledTo((int) (imgWidth * 0.5), (int) (imgHeight * 0.5)));
		lblImageHere.setImage(scaled050);
	    }
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent event) {
	    throw new UnsupportedOperationException();
	}
    }

    /**
     * Create contents of the window.
     */
    protected void createContents(Shell shell) {
	shell.setSize(960, 546);
	shell.setText("Image Data Extraction Application");
	shell.open();
	shell.layout();
    }

    private void mainMenuFile() {
	MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
	cascadeFileMenu.setText("&File");
	cascadeFileMenu.setMenu(fileMenu);
    }

    private void importImageMenuItem() {
	MenuItem importImageItem = new MenuItem(fileMenu, SWT.CASCADE);
	importImageItem.setText("Import image");
	importImageItem.setMenu(importMenu);
	MenuItem openImageItem = new MenuItem(importMenu, SWT.PUSH);
	openImageItem.setText("&Open\tCTRL+O");
	openImageItem.setAccelerator(SWT.CTRL + 'O');
	openImageItem.addSelectionListener(new SelectImage());
    }

    class SaveAsTxtListener implements SelectionListener {
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
	    String fileName = Utils.timestampFileName() + "\n";
	    String path = Utils.replaceBacklashWithDouble(lblSelectedImageDirectorySub.getText()) + "\n";
	    String content = extractedText.getText();

	    byte[] data = (fileName + path + content).getBytes();
	    Path p = null;
	    p = Paths.get("./data.log/" + Utils.timestampFileName());
	    try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE))) {
		out.write(data, 0, data.length);
	    } catch (IOException x) {
		logger.log(Level.WARNING, "Exception found!", x);
	    }
	}
    }

    class SaveAsCSVListener implements SelectionListener {
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
	    String fileName = Utils.timestampFileName();
	    String dateProcessed = fileName.substring(0, fileName.indexOf('.')).trim();
	    String path = lblSelectedImageDirectorySub.getText();
	    String content = extractedText.getText();

	    ImageData data = new ImageData();
	    data.setId(fileName.substring(0, fileName.lastIndexOf('.')).trim());
	    data.setDateProcessed(dateProcessed);
	    data.setImagePath(path);
	    data.setImageContent(content);

	    FaceDetection fd = new FaceDetection(path);
	    fd.detectFace();
	    if (fd.isFaceDetected()) {
		data.setCroppedImg(fd.convertedImageToBase64());
	    }
	    
	    List<ImageData> imgDataList = new ArrayList<>();
	    imgDataList.add(data);

	    ImageDataCSV imgToCSV = new ImageDataCSV();
	    imgToCSV.saveDataAsCSV(imgDataList);

	    logger.info(data.toString());
	}
    }

    private void saveMenuItem() {
	mnExtract = new MenuItem(menuBar, SWT.PUSH);
	mnExtract.setText("Extract");
	mnExtract.setEnabled(false);

	mnSaveExtracted = new MenuItem(menuBar, SWT.CASCADE);
	mnSaveExtracted.setText("&Save");
	mnSaveExtracted.setMenu(saveMenu);
	mnSaveExtracted.setEnabled(false);
	MenuItem saveAsText = new MenuItem(saveMenu, SWT.PUSH);
	saveAsText.setText("&Save as text file");
	saveAsText.addSelectionListener(new SaveAsTxtListener());

	MenuItem saveAsCSV = new MenuItem(saveMenu, SWT.PUSH);
	saveAsCSV.setText("&Save as CSV");
	saveAsCSV.addSelectionListener(new SaveAsCSVListener());
    }

    private void exitMenuItem() {
	MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	exitItem.setText("&Exit");
	exitItem.addListener(SWT.Selection, event -> {
	    shell.getDisplay().dispose();
	    System.exit(0);
	});
    }
}

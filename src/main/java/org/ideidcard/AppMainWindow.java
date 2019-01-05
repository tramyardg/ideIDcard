package org.ideidcard;

import java.util.ArrayList;
import java.util.List;
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

public class AppMainWindow {

    private static final Logger LOGGER = Logger.getLogger(AppMainWindow.class.getName());
    
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
	public void widgetSelected(SelectionEvent event) {
	    FileDialog openImageDialog = new FileDialog(shell, SWT.OPEN);
	    openImageDialog.setText("Open");
	    String homeDir = System.getProperty("user.home");
	    openImageDialog.setFilterPath(homeDir);
	    String[] filterExt = { "*.JPG", "*.PNG", "*.GIF", "*.BMP" };
	    openImageDialog.setFilterExtensions(filterExt);
	    String selected = openImageDialog.open();
	    if (selected != null) {
		mnExtract.setEnabled(true);
		lblSelectedImageDirectorySub.setText(selected);
		lblSelectedImageDirectorySub.pack();
		Image image = new Image(display, selected);

		mnExtract.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
			extractedText.setText(ReadImageData.readImage(selected));
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

	public void widgetDefaultSelected(SelectionEvent event) {
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
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
	    SaveAs saveTxt = new SaveAs();
	    String fileName = Util.timestampFileName() + "\n";
	    String path = Util.replaceBacklashWithDouble(lblSelectedImageDirectorySub.getText()) + "\n";
	    String content = extractedText.getText();
	    saveTxt.saveDataAs((fileName + path + content), false);
	}
    }

    class SaveAsCSVListener implements SelectionListener {
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
	    String fileName = Util.timestampFileName();
	    String dateProcessed = fileName.substring(0, fileName.indexOf(".")).trim();
	    String path = lblSelectedImageDirectorySub.getText();
	    String content = extractedText.getText();
	    
	    ImageData data = new ImageData();
	    data.setId("4");
	    data.setDateProcessed(dateProcessed);
	    data.setImagePath(path);
	    data.setImageContent(content);
	    
	    List<ImageData> imgDataList = new ArrayList<>();
	    imgDataList.add(data);
	    
	    SaveAs saveAsCSV = new SaveAs();
	    saveAsCSV.saveDataAs((new ImageDataCSV().writer(imgDataList).toString()), true);
	    
	    LOGGER.info(data.toString());
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

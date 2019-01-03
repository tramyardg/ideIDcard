package org.ideidcard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class AppMainWindow {

    protected Display display;
    protected Shell shell;
    protected Group grpImagePreview;
    protected Group grpExtractedData;
    protected Label lblSelectedImageDirectory;
    protected Label lblSelectedImageDirectorySub;
    protected Label lblImageHere;

    protected Menu menuBar;
    protected Menu fileMenu;
    protected Menu importMenu;
    protected Menu saveMenu;
    private Text extractedText;

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
	lblImageHere.setBounds(10, 20, 499, 355);
	lblImageHere.setText("");

	lblSelectedImageDirectory = new Label(shell, SWT.NONE);
	lblSelectedImageDirectory.setBounds(10, 411, 136, 15);
	lblSelectedImageDirectory.setText("Selected image directory");

	lblSelectedImageDirectorySub = new Label(shell, SWT.NONE);
	lblSelectedImageDirectorySub.setBounds(10, 432, 493, 15);
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
		lblSelectedImageDirectorySub.setText(selected);
		lblSelectedImageDirectorySub.pack();

		Image image = new Image(display, selected);
		extractedText.setText(ReadImageData.readImage(selected));

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
    
    private void saveMenuItem() {
	MenuItem mnSaveExtracted = new MenuItem(menuBar, SWT.CASCADE);
	mnSaveExtracted.setText("&Save");
	mnSaveExtracted.setMenu(saveMenu);
	MenuItem saveAsText = new MenuItem(saveMenu, SWT.PUSH);
	saveAsText.setText("&Save as text file");
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

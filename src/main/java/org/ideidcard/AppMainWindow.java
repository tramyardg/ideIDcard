package org.ideidcard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class AppMainWindow {

    protected Display display;
    protected Shell shell;
    protected Group grpImagePreview;
    protected Label lblSelectedImageDirectory;
    protected Label lblSelectedImageDirectorySub;

    protected Menu menuBar;
    protected Menu fileMenu;
    protected Menu importMenu;

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

	MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
	cascadeFileMenu.setText("&File");
	cascadeFileMenu.setMenu(fileMenu);

	MenuItem importImageItem = new MenuItem(fileMenu, SWT.CASCADE);
	importImageItem.setText("Import image");
	importImageItem.setMenu(importMenu);
	MenuItem openImageItem = new MenuItem(importMenu, SWT.PUSH);
	openImageItem.setText("&Open\tCTRL+O");
	openImageItem.setAccelerator(SWT.CTRL + 'O');
	openImageItem.addSelectionListener(new SelectImage());

	MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	exitItem.setText("&Exit");

	shell.setMenuBar(menuBar);
	exitItem.addListener(SWT.Selection, event -> {
	    shell.getDisplay().dispose();
	    System.exit(0);
	});

	createContents(shell);

	grpImagePreview = new Group(shell, SWT.NONE);
	grpImagePreview.setText("Image preview");
	grpImagePreview.setBounds(10, 10, 493, 421);

	lblSelectedImageDirectory = new Label(shell, SWT.NONE);
	lblSelectedImageDirectory.setBounds(10, 438, 136, 15);
	lblSelectedImageDirectory.setText("Selected image directory");

	lblSelectedImageDirectorySub = new Label(shell, SWT.NONE);
	lblSelectedImageDirectorySub.setBounds(10, 459, 493, 15);
	lblSelectedImageDirectorySub.setText("");

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
}

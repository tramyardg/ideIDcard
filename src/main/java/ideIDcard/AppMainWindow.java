package ideIDcard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class AppMainWindow {

    protected Shell shell;

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
	try {
	    AppMainWindow window = new AppMainWindow();
	    window.open();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Open the window.
     */
    public void open() {
	Display display = new Display();
	Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);

	Menu menuBar = new Menu(shell, SWT.BAR);
	Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	Menu importMenu = new Menu(shell, SWT.DROP_DOWN);

	MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
	cascadeFileMenu.setText("&File");
	cascadeFileMenu.setMenu(fileMenu);

	MenuItem importImageItem = new MenuItem(fileMenu, SWT.CASCADE);
	importImageItem.setText("Import image");
	importImageItem.setMenu(importMenu);
	MenuItem feedItem = new MenuItem(importMenu, SWT.PUSH);
	feedItem.setText("&Open...");

	MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
	exitItem.setText("&Exit");

	shell.setMenuBar(menuBar);
	exitItem.addListener(SWT.Selection, event -> {
	    shell.getDisplay().dispose();
	    System.exit(0);
	});

	createContents(shell);
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) {
		display.sleep();
	    }
	}
    }

    /**
     * Create contents of the window.
     */
    protected void createContents(Shell shell) {
	shell.setSize(960, 500);
	shell.setText("Image Data Extraction Application");
	shell.open();
	shell.layout();
    }

}

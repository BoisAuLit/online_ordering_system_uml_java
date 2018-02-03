package mvc.controller;

import mvc.view.LoginPage;
import javax.swing.SwingUtilities;
import mvc.view.CustomerGUI;
import mvc.view.RegisterPage;

/**
 *
 * @author bohao
 */
public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LoginPage().setVisible(true);
//				new CustomerGUI().setVisible(true);
//				new RegisterPage().setVisible(true);
			}
		});
	}
}

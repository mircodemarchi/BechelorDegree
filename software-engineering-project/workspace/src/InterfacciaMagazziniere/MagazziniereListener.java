package InterfacciaMagazziniere;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import Login.LoginPage;

public class MagazziniereListener implements WindowListener {
	private FrameMagazziniere frame;
	
	public MagazziniereListener(FrameMagazziniere frame) {
		this.frame = frame;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		LoginPage.getLoginPage().setVisible(true);
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}

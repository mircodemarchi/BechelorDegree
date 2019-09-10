package InterfacciaMagazziniere;
import javax.swing.JFrame;

public class FrameMagazziniere extends JFrame {
	public FrameMagazziniere() {
		super("Magazziniere");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		this.setSize(800, 600);
		this.addWindowListener(new MagazziniereListener(this));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

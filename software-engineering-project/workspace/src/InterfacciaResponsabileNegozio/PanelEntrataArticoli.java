package InterfacciaResponsabileNegozio;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import Login.LoginPage;
import MainClass.Negozio;
import MainClass.Ordine;
import MyList.MyList;
import MyList.MyListProxyFactory;

public class PanelEntrataArticoli extends JPanel{
	private FrameResponsabileNegozio frame;
	
	private final JPanel nord = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static final JLabel entrataArticolo = new JLabel ("Seleziona un ordine, seleziona l'articolo in entrata e inserisci la sua quantità per aggiungerlo al tuo Negozio");

	private final JPanel ovest = new JPanel(new BorderLayout());
	
	private final JPanel ovestNord = new JPanel();
	private static final JLabel mostraOrdini = new JLabel("Ordini in sospeso");
	
	private final JPanel ovestOvest = new JPanel();
	
	private final JPanel ovestCentro = new JPanel();
	private static final String[] columnNames = {"Codice Ordine", "Data Ordine", "Numero Articoli", "Prezzo Totale"};
	private Map<String, Ordine> mapOrder = new HashMap<>();
	private JTable table;
	
	private final JPanel centro = new JPanel(new BorderLayout());
	
	private final JPanel centroNord = new JPanel();
	private static final JLabel articoliOrdine = new JLabel ("Articoli dell'ordine selezionato");
	
	private final JPanel centroCentro = new JPanel(new BorderLayout());
	
	private final JPanel panelTableArticoliOrdine = new JPanel();
	private static final String[] columnNamesArticleQuantity = {"Articolo", "Codice", "Prezzo", "Quantità"};
	private JTable tableArticoliOrdine;
	
	private final JPanel panelAddArticle = new JPanel(new BorderLayout());
	private final JPanel panelErrorZoneQuantità1 = new JPanel(new GridLayout(2,1));
	private final JPanel errorZone1 = new JPanel();
	private final JPanel panelQuantità1 = new JPanel();
	private static final JLabel testoQuantità1 = new JLabel("Quantità articolo:"); 
	private static final JSpinner campoQuantità1 = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
	private final JPanel panelAddArticleButton = new JPanel();
	private final JButton addArticleButton = new JButton("Aggiungi al negozio"); 
	
	private final JPanel est = new JPanel(new BorderLayout());
	
	private final JPanel estNord = new JPanel();
	private static final JLabel articoliNegozio = new JLabel("Articoli nel negozio");
	
	private final JPanel estCentro = new JPanel(new BorderLayout());
	private final JPanel panelTableArticoliNegozio = new JPanel();
	private static final String[] columnNamesArticle = {"Nome", "Descrizione", "Quantità", "Sport", "Materiali", "Codice", "Prezzo", "Data Produzione"};
	private JTable tableArticoliNegozio;
	private final JPanel panelRemoveArticle = new JPanel(new BorderLayout());
	private final JPanel panelErrorZoneQuantità2 = new JPanel(new GridLayout(2,1));
	private final JPanel errorZone2 = new JPanel();
	private final JPanel panelQuantità2 = new JPanel();
	private static final JLabel testoQuantità2 = new JLabel("Quantità articolo:"); 
	private static final JSpinner campoQuantità2 = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
	private final JPanel panelRemoveArticleButton = new JPanel();
	private final JButton removeArticleButton = new JButton("Vendi articolo"); 
	
	private static final JPanel estEst = new JPanel();
	
	public PanelEntrataArticoli(FrameResponsabileNegozio frame) {
		super(new BorderLayout());
		this.frame = frame;
		
		nord.add(new JLabel("   "));
		entrataArticolo.setFont(new Font("Arial", Font.BOLD, 14));
		nord.add(entrataArticolo);
		nord.setPreferredSize(new Dimension(0, 50));
		
		ovestNord.add(mostraOrdini);
		ovestNord.setPreferredSize(new Dimension(0, 35));
		updateTableOrdini();
		
		ovest.add(ovestNord, BorderLayout.NORTH);
		ovest.add(ovestOvest, BorderLayout.WEST);
		ovest.add(ovestCentro, BorderLayout.CENTER);
		
		centroNord.add(articoliOrdine);
		centroNord.setPreferredSize(new Dimension(0, 35));
		
		updateTableArticoliOrdine(new Object[0][4]);
		
		panelQuantità1.add(testoQuantità1);
		panelQuantità1.add(campoQuantità1);
		
		errorZone1.setPreferredSize(new Dimension(150, 30));
		panelErrorZoneQuantità1.add(errorZone1);
		panelErrorZoneQuantità1.add(panelQuantità1);
		
		addArticleButton.setPreferredSize(new Dimension(260, 40));
		panelAddArticleButton.add(addArticleButton);
		
		panelAddArticle.add(panelErrorZoneQuantità1, BorderLayout.NORTH);
		panelAddArticle.add(panelAddArticleButton, BorderLayout.CENTER);
		
		centroCentro.add(panelTableArticoliOrdine, BorderLayout.NORTH);
		centroCentro.add(panelAddArticle, BorderLayout.CENTER);
		
		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(centroCentro, BorderLayout.CENTER);
		
		estNord.add(articoliNegozio);
		estNord.setPreferredSize(new Dimension(0, 35));

		updateTableArticoliNegozio();
		
		panelQuantità2.add(testoQuantità2);
		panelQuantità2.add(campoQuantità2);
		
		removeArticleButton.setPreferredSize(new Dimension(500, 40));
		panelRemoveArticleButton.add(removeArticleButton);
		
		errorZone2.setPreferredSize(new Dimension(150, 30));
		panelErrorZoneQuantità2.add(errorZone2);
		panelErrorZoneQuantità2.add(panelQuantità2);
		
		panelRemoveArticle.add(panelErrorZoneQuantità2, BorderLayout.NORTH);
		panelRemoveArticle.add(panelRemoveArticleButton, BorderLayout.CENTER);
		
		estCentro.add(panelTableArticoliNegozio, BorderLayout.NORTH);
		estCentro.add(panelRemoveArticle, BorderLayout.CENTER);
		
		est.add(estNord, BorderLayout.NORTH);
		est.add(estCentro, BorderLayout.CENTER);
		est.add(estEst, BorderLayout.EAST);
		
		nord.setBackground(Color.WHITE);
		ovest.setBackground(Color.WHITE);
		centroNord.setBackground(Color.WHITE);
		centroCentro.setBackground(Color.WHITE);
		panelQuantità1.setBackground(Color.WHITE);
		panelQuantità2.setBackground(Color.WHITE);
		panelTableArticoliOrdine.setBackground(Color.WHITE);
		panelAddArticleButton.setBackground(Color.WHITE);
		estNord.setBackground(Color.WHITE);
		errorZone1.setBackground(Color.WHITE);
		errorZone2.setBackground(Color.WHITE);
		panelRemoveArticleButton.setBackground(Color.WHITE);
		estCentro.setBackground(Color.WHITE);
		panelTableArticoliNegozio.setBackground(Color.WHITE);
		estEst.setBackground(Color.WHITE);
		ovestNord.setBackground(Color.WHITE);
		ovestCentro.setBackground(Color.WHITE);
		ovestOvest.setBackground(Color.WHITE);
		
		this.add(nord, BorderLayout.NORTH);
		this.add(est, BorderLayout.EAST);
		this.add(centro, BorderLayout.CENTER);
		this.add(ovest, BorderLayout.WEST);
		
		addArticleButton.addActionListener(new EntrataArticoliListener(this));
		removeArticleButton.addActionListener(new EntrataArticoliListener(this));
	}
	
	public JTable getTable() {
		return table;
	}
	
	public JTable getTableArticoliOrdine() {
		return tableArticoliOrdine;
	}

	public void updateTableOrdini() {
		for(Object o: frame.getProxyListOrdineNegozio().getArrayList()) {
			if( o instanceof Ordine) {
				mapOrder.put(((Ordine) o).getCodiceOrdine(), (Ordine) o);
			}
		}
		
		table = frame.getProxyListOrdineNegozio().display(ovestCentro, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(400, 450));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getSelectionModel().addListSelectionListener(new EntrataArticoliListener(this));
	}
	
	public void updateTableArticoliOrdine(Object[][] matrixArticleQuantity) {
		tableArticoliOrdine = LoginPage.getProxyListArticoli().display(panelTableArticoliOrdine, columnNamesArticleQuantity, matrixArticleQuantity);
		tableArticoliOrdine.setPreferredScrollableViewportSize(new Dimension(260, 340));
		tableArticoliOrdine.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableArticoliOrdine.getColumnModel().getColumn(1).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(2).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(3).setPreferredWidth(60);
	}
	
	public void updateTableArticoliNegozio() {
		tableArticoliNegozio = frame.getProxyListArticoliNegozio().display(panelTableArticoliNegozio, columnNamesArticle);
		tableArticoliNegozio.setPreferredScrollableViewportSize(new Dimension(500, 325));
		tableArticoliNegozio.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableArticoliNegozio.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableArticoliNegozio.getColumnModel().getColumn(2).setPreferredWidth(70);
		tableArticoliNegozio.getColumnModel().getColumn(3).setPreferredWidth(150);
		tableArticoliNegozio.getColumnModel().getColumn(4).setPreferredWidth(50);
		tableArticoliNegozio.getColumnModel().getColumn(5).setPreferredWidth(50);
		tableArticoliNegozio.getColumnModel().getColumn(6).setPreferredWidth(70);
		tableArticoliNegozio.getColumnModel().getColumn(7).setPreferredWidth(120);
	}

	public JPanel getErrorZone1() {
		return errorZone1;
	}
	
	public JPanel getErrorZone2() {
		return errorZone2;
	}

	public Map<String, Ordine> getMapOrder() {
		return mapOrder;
	}

	public FrameResponsabileNegozio getFrame() {
		return frame;
	}

	public String[] getColumnNamesArticleQuantity() {
		return columnNamesArticleQuantity;
	}
	
	public String[] getColumnNamesArticle() {
		return columnNamesArticle;
	}


	public JPanel getPanelListaArticoli() {
		return panelTableArticoliOrdine;
	}
	
	public JPanel getPanelListaArticoliNegozio() {
		return estCentro;
	}

	public JSpinner getCampoQuantità1() {
		return campoQuantità1;
	}
	
	public JSpinner getCampoQuantità2() {
		return campoQuantità2;
	}

	public JTable getTableArticoliNegozio() {
		return tableArticoliNegozio;
	}
	
}

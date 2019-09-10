package InterfacciaResponsabileNegozio;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import MainClass.Articolo;
import MainClass.Negozio;
import MyList.MyList;
import MyList.MyListProxyFactory;

public class PanelOrdinaArticoli extends JPanel{
	private Negozio negozio;
	private FrameResponsabileNegozio frame;
	
	private final JPanel nord = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private static final JLabel aggiungiArticolo = new JLabel ("Seleziona un articolo e inserisci la quantità per aggiungerlo al nuovo ordine");
	
	private final JPanel centro = new JPanel(new BorderLayout());
	
	private final JPanel centroNord = new JPanel();
	private static final JLabel mostraArticoli = new JLabel("Articoli in DataBase");
	
	private final JPanel centroCentro = new JPanel();
	private final JPanel panelTable = new JPanel();
	private static final MyList proxyListArticolo = new MyListProxyFactory("ArticleDataBase");
	private static final String[] columnNames = {"Nome", "Descrizione", "Sport", "Materiali", "Codice", "Prezzo", "Data Produzione"};
	private Map<String, Articolo> mapArticle = new HashMap<>();
	private JTable table;
	
	private final JPanel centroSud = new JPanel(new BorderLayout());
	private final JPanel panelErrorZonePanelQuantità = new JPanel(new GridLayout(2,1));
	private final JPanel errorZone1 = new JPanel();
	private final JPanel panelQuantità = new JPanel();
	private static final JLabel testoQuantità = new JLabel("Quantità articolo:"); 
	private final JSpinner campoQuantità = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
	private final JPanel panelAddArticleButton = new JPanel();
	private final JButton addArticleButton = new JButton("Aggiungi a ordine"); 
	
	private final JPanel est = new JPanel(new BorderLayout());
	
	private final JPanel estNord = new JPanel();
	private static final JLabel nuovoOrdine = new JLabel ("Nuovo Ordine");
	
	private final JPanel estCentro = new JPanel(new BorderLayout());
	
	private final JPanel panelListaArticoliRemoveButton = new JPanel(new BorderLayout());
	private final JPanel panelListaArticoli = new JPanel();
	private static final String[] columnNamesArticleQuantity = {"Articolo", "Codice", "Prezzo", "Quantità"};
	private JTable tableArticoliOrdine;
	private Set<Articolo> setArticolo = new HashSet<>();
	private final JPanel panelRemoveArticleButton = new JPanel();
	private final JButton removeArticleButton = new JButton("Rimuovi da ordine"); 
	
	private final JPanel panelPrezzo = new JPanel(new GridLayout(2,1));
	private final JPanel errorZone2 = new JPanel();
	private final JLabel prezzo = new JLabel("Totale: 0€");
	
	private final JPanel estEst = new JPanel();
	
	private final JPanel estSud = new JPanel();
	private final JButton confirmOrderButton = new JButton("Conferma Ordine"); 
	
	private final JPanel sud = new JPanel();
	
	public PanelOrdinaArticoli(Negozio negozio, FrameResponsabileNegozio frame) {
		super(new BorderLayout());
		this.negozio = negozio;
		this.frame = frame;
		
		nord.add(new JLabel("   "));
		aggiungiArticolo.setFont(new Font("Arial", Font.BOLD, 14));
		nord.add(aggiungiArticolo);
		nord.setPreferredSize(new Dimension(0, 50));
		
		centroNord.add(mostraArticoli);
		centroNord.setPreferredSize(new Dimension(0, 35));
		
		for(Object a: proxyListArticolo.getArrayList()) {
			if( a instanceof Articolo) {
				mapArticle.put(((Articolo) a).getCode(), (Articolo) a);
			}
		}
		table = proxyListArticolo.display(panelTable, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(750, 330));
		table.getColumnModel().getColumn(0).setPreferredWidth(130);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(50);
		table.getColumnModel().getColumn(5).setPreferredWidth(50);
		table.getColumnModel().getColumn(6).setPreferredWidth(120);
		
		centroCentro.add(panelTable);
		
		panelQuantità.add(testoQuantità);
		panelQuantità.add(campoQuantità);
		
		addArticleButton.setPreferredSize(new Dimension(750, 40));
		panelAddArticleButton.add(addArticleButton);
		
		errorZone1.setPreferredSize(new Dimension(0, 30));
		panelErrorZonePanelQuantità.add(errorZone1);
		panelErrorZonePanelQuantità.add(panelQuantità);
		centroSud.add(panelErrorZonePanelQuantità, BorderLayout.CENTER);
		centroSud.add(panelAddArticleButton, BorderLayout.SOUTH);
		
		centro.add(centroNord, BorderLayout.NORTH);
		centro.add(centroCentro, BorderLayout.CENTER);
		centro.add(centroSud, BorderLayout.SOUTH);
		
		estNord.add(nuovoOrdine);
		estNord.setPreferredSize(new Dimension(0, 40));
		
		updateTableArticoliOrdine(new Object[0][4]);
		
		removeArticleButton.setPreferredSize(new Dimension(400, 40));
		panelRemoveArticleButton.add(removeArticleButton);
		
		panelListaArticoliRemoveButton.add(panelListaArticoli, BorderLayout.NORTH);
		panelListaArticoliRemoveButton.add(panelRemoveArticleButton, BorderLayout.CENTER);
		
		errorZone2.setPreferredSize(new Dimension(0, 30));
		panelPrezzo.add(errorZone2);
		prezzo.setHorizontalAlignment(JLabel.CENTER);
		panelPrezzo.add(prezzo);
		
		estCentro.add(panelListaArticoliRemoveButton, BorderLayout.CENTER);
		estCentro.add(panelPrezzo, BorderLayout.SOUTH);
		
		confirmOrderButton.setPreferredSize(new Dimension(400, 40));
		estSud.add(confirmOrderButton);
		
		est.add(estNord, BorderLayout.NORTH);
		est.add(estCentro, BorderLayout.CENTER);
		est.add(estSud, BorderLayout.SOUTH);
		est.add(estEst, BorderLayout.EAST);
		
		nord.setBackground(Color.WHITE);
		centroNord.setBackground(Color.WHITE);
		centroCentro.setBackground(Color.WHITE);
		panelTable.setBackground(Color.WHITE);
		centroSud.setBackground(Color.WHITE);
		panelAddArticleButton.setBackground(Color.WHITE);
		estNord.setBackground(Color.WHITE);
		errorZone1.setBackground(Color.WHITE);
		errorZone2.setBackground(Color.WHITE);
		panelQuantità.setBackground(Color.WHITE);
		estCentro.setBackground(Color.WHITE);
		panelListaArticoli.setBackground(Color.WHITE);
		panelRemoveArticleButton.setBackground(Color.WHITE);
		panelPrezzo.setBackground(Color.WHITE);
		estEst.setBackground(Color.WHITE);
		estSud.setBackground(Color.WHITE);
		sud.setBackground(Color.WHITE);
		
		this.add(nord, BorderLayout.NORTH);
		this.add(est, BorderLayout.EAST);
		this.add(centro, BorderLayout.CENTER);
		this.add(sud, BorderLayout.SOUTH);
		
		addArticleButton.addActionListener(new OrdinaArticoliListener(this));
		confirmOrderButton.addActionListener(new OrdinaArticoliListener(this));
		removeArticleButton.addActionListener(new OrdinaArticoliListener(this));

	}
	
	public void updateTableArticoliOrdine(Object[][] matrix) {
		tableArticoliOrdine = proxyListArticolo.display(panelListaArticoli, columnNamesArticleQuantity, matrix);
		tableArticoliOrdine.setPreferredScrollableViewportSize(new Dimension(400, 290));
		tableArticoliOrdine.getColumnModel().getColumn(0).setPreferredWidth(200);
		tableArticoliOrdine.getColumnModel().getColumn(1).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(2).setPreferredWidth(50);
		tableArticoliOrdine.getColumnModel().getColumn(3).setPreferredWidth(100);
	}
	
	public FrameResponsabileNegozio getFrame() {
		return frame;
	}
	
	public JPanel getErrorZone1(){
		return errorZone1;
	}
	
	public JPanel getErrorZone2(){
		return errorZone2;
	}
	
	public JTable getTable(){
		return table;
	}
	
	public JTable getTableArticoliOrdine(){
		return tableArticoliOrdine;
	}
	
	public MyList getProxyListArticolo(){
		return proxyListArticolo;
	}
	
	public JLabel getPrezzo(){
		return prezzo;
	}

	public JPanel getCentroCentro(){
		return centroCentro;
	}
	
	public Map<String, Articolo> getMapArticle(){
		return mapArticle;
	}

	public Negozio getNegozio(){
		return negozio;
	}
	
	public JSpinner getCampoQuantità(){
		return campoQuantità;
	}
	
	public Set<Articolo> getSetArticolo(){
		return setArticolo;
	}

}

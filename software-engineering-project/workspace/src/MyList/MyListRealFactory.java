package MyList;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import DataBase.ArticleDataBase;
import DataBase.ArticleNegozioDataBase;
import DataBase.DataBase;
import DataBase.NegoziDataBase;
import DataBase.OrderDataBase;
import DataBase.UserDataBase;
import MainClass.Articolo;
import MainClass.ArticoloCount;
import MainClass.Negozio;
import MainClass.Ordine;
import Utenti.Utente;

import java.util.*;
import javax.swing.*;

public class MyListRealFactory implements MyList {
	private String filename;
	private String user;
	private ArrayList<Object> arrayList = new ArrayList<Object>();
	private DataBase dataBase;
	
	private SortedSet<Object> articoliSorted = new TreeSet<Object>(new Comparator <Object>() {
		public int compare(Object a1, Object a2) {
			return Integer.parseInt(((Articolo) a1).getCode()) - Integer.parseInt(((Articolo) a2).getCode());
		}
	});
	
	private SortedSet<Object> negoziSorted = new TreeSet<Object>(new Comparator<Object>() {
		public int compare(Object n1, Object n2) {
			return Integer.parseInt(((Negozio) n1).getCode()) - Integer.parseInt(((Negozio) n2).getCode());
		}
	});
	
	private SortedSet<Object> ordiniSorted = new TreeSet<Object>(new Comparator<Object>() {
		public int compare(Object o1, Object o2) {
			return Integer.parseInt(((Ordine) o1).getCodiceOrdine()) - Integer.parseInt(((Ordine) o2).getCodiceOrdine());
		}
	});
	
	private SortedSet<Object> negoziInOrderSorted = new TreeSet<Object>(new Comparator<Object>() {
		public int compare(Object o1, Object o2) {
			int ret = Integer.parseInt(((Ordine) o1).getNegozio().getCode()) - Integer.parseInt(((Ordine) o2).getNegozio().getCode());
			if(ret == 0) {
				return Integer.parseInt(((Ordine) o1).getCodiceOrdine()) - Integer.parseInt(((Ordine) o2).getCodiceOrdine());
			}
			
			return ret;
		}
	});
	
	public MyListRealFactory(String filename) {
		this.filename = filename;
		loadDataBase();
	}
	
	public MyListRealFactory(String filename, String user) {
		this.user = user;
		this.filename = filename;
		loadDataBase();
	}

	public void display(JList<Object> list, JPanel listContainer ) {
		JScrollPane scrollPane;
		Object[] array = new Object[arrayList.size()];
		arrayList.toArray(array);
		list.setListData(array);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(20);
		listContainer.removeAll();
		scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(300, 400));
		listContainer.add(scrollPane);
	}
	
	public JTable display(JPanel tableContainer, String[] column) {
		JScrollPane scrollPane;
		
		MyTableModel model = new MyTableModel(column, filename, this);
		JTable table = new JTable(model);
		//table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setBackground(new Color(250, 251, 232));
		tableContainer.removeAll();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPane.setPreferredSize(new Dimension(500, 400));
		tableContainer.add(scrollPane);
		
		return table;

	}
	
	public JTable display(JPanel tableContainer, String[] column, Object[][] data) {
		JScrollPane scrollPane;
		
		MyTableModel model = new MyTableModel(column, data);
		
		JTable table = new JTable(model);
		//table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setBackground(new Color(250, 251, 232));
		tableContainer.removeAll();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPane.setPreferredSize(new Dimension(500, 400));
		tableContainer.add(scrollPane);
		
		return table;

	}
	
	public void addObject(Object object){
		arrayList.add(object);
	}

	public void removeObject(Object object) {
		arrayList.remove(object);
	}
	
	public void loadDataBase() {
		
		if(filename.equalsIgnoreCase("ArticleDataBase")) {
			dataBase = new ArticleDataBase(filename);
		}
		else if(filename.equalsIgnoreCase("UserDataBase")) {
			dataBase = new UserDataBase();
		}
		else if(filename.equalsIgnoreCase("OrderDataBase")) {
			dataBase = new OrderDataBase(filename);
		}
		else if(filename.equalsIgnoreCase("NegoziDataBase")) {
			dataBase = new NegoziDataBase();
		}
		else if(filename.equalsIgnoreCase("ArticleNegozioDataBase")) {
			dataBase = new ArticleNegozioDataBase(user + "/" + filename);
		}
		else if(filename.equalsIgnoreCase("OrderNegozioDataBase")) {
			dataBase = new OrderDataBase(user + "/" + filename);
		}
		else {
			dataBase = null;
		}
		
		if(dataBase != null) {
			arrayList = new ArrayList<Object>(Arrays.asList(dataBase.readData()));
		}	
	}
	
	public ArrayList<Object> getArrayList(){
		return arrayList;
	}

	public void saveData() {
		if(filename.equalsIgnoreCase("ArticleDataBase")) {
			Articolo[] arrayArticolo = new Articolo[arrayList.size()];
			arrayList.toArray(arrayArticolo);
			dataBase.writeData(arrayArticolo);
		}
		else if(filename.equalsIgnoreCase("UserDataBase")) {
			Utente[] arrayUtente = new Utente[arrayList.size()];
			arrayList.toArray(arrayUtente);
			dataBase.writeData(arrayUtente);
		}
		else if(filename.equalsIgnoreCase("NegoziDataBase")) {
			Negozio[] arrayNegozio = new Negozio[arrayList.size()];
			arrayList.toArray(arrayNegozio);
			dataBase.writeData(arrayNegozio);
		}
		else if(filename.equalsIgnoreCase("OrderDataBase")  || filename.equalsIgnoreCase("OrderNegozioDataBase")) {
			Ordine[] arrayOrdine = new Ordine[arrayList.size()];
			arrayList.toArray(arrayOrdine);
			dataBase.writeData(arrayOrdine);
		}
		else if(filename.equalsIgnoreCase("ArticleNegozioDataBase")) {
			ArticoloCount[] arrayArticleNegozio = new ArticoloCount[arrayList.size()];
			arrayList.toArray(arrayArticleNegozio);
			dataBase.writeData(arrayArticleNegozio);
		}
	}
	
	public void displayArticoli(TableModel tableModel, JTable table, JPanel listContainer) {
		
		((DefaultTableModel) tableModel).setRowCount(0);
		
		
		getSorted(articoliSorted);
		
		for(Object a: arrayList) {
			if(a instanceof Articolo) {
				((DefaultTableModel) tableModel).addRow(((Articolo) a).getArrayString());
			}
		}
		
		table.setModel(tableModel);
		table.getCellSelectionEnabled();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
		table.setBackground(new Color(250, 251, 232));
		
		listContainer.removeAll();
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(600, 385));
		listContainer.add(scrollPane);
	}
	
	/*---------------------------------------------------------------*/
	
	/* DISPLAY GENERALE PER NEGOZI */
	
	public void displayNegozi(TableModel tableModel, JTable table, JPanel listContainer) {
		
		((DefaultTableModel) tableModel).setRowCount(0);
		
		getSorted(negoziSorted);
		
		for(Object n: arrayList) {
			if(n instanceof Negozio) {
				((DefaultTableModel) tableModel).addRow(((Negozio) n).getArrayString());
			}
		}
		
		table.setModel(tableModel);
		table.getCellSelectionEnabled();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
		table.setBackground(new Color(250, 251, 232));
		listContainer.removeAll();
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(600, 465));
		listContainer.add(scrollPane);
	}
	
	/*---------------------------------------------------------------*/
	
	/* DISPLAY ORDINATO PER ORDINI */
	
	public void displayForOrder(JTable table, JPanel listContainer, String[] columnNames) {
		DefaultTableModel tableModel = new DefaultTableModel() {
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		tableModel.setRowCount(0);
		tableModel.setColumnIdentifiers(columnNames);
		getSorted(ordiniSorted);
		
		for(Object a: arrayList) {
			if(a instanceof Ordine) {
				((DefaultTableModel) tableModel).addRow(((Ordine) a).getLongArrayString());
			}
		}
		
		table.setModel(tableModel);
		table.getCellSelectionEnabled();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
		table.setBackground(new Color(250, 251, 232));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.setPreferredScrollableViewportSize(new Dimension(600, 340));
		
		listContainer.removeAll();
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listContainer.add(scrollPane);
		
	}
	
	/*---------------------------------------------------------------*/
	
	/* DISPLAY ORDINATO PER NEGOZI */
	
	public void displayForNegozio(JTable table, JPanel listContainer, String[] columnNames) {
		DefaultTableModel tableModel = new DefaultTableModel() {
		    public boolean isCellEditable(int row, int column) {
		       
		       return false;
		    }
		};
		
		tableModel.setRowCount(0);
		tableModel.setColumnIdentifiers(columnNames);
		
		getSorted(negoziInOrderSorted);
		
		for(Object a: arrayList) {
			if(a instanceof Ordine) {
				((DefaultTableModel) tableModel).addRow(((Ordine) a).getLongArrayString());
			}
		}
		
		table.setModel(tableModel);
		table.getCellSelectionEnabled();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
		table.setBackground(new Color(250, 251, 232));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.setPreferredScrollableViewportSize(new Dimension(600, 340));
		
		listContainer.removeAll();
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listContainer.add(scrollPane);
		
	}
	
	private ArrayList<Object> getSorted(SortedSet<Object> sortedList) {
		sortedList.clear();
		for (Object o : arrayList)
			sortedList.add((Object) o);
		arrayList.clear();
		for (Object o : sortedList)
			arrayList.add((Object) o);
		return arrayList;
	}

	public JTable display(JPanel tableContainer, String[] column, Negozio negozio) {
		JScrollPane scrollPane;
		
		MyTableModel model = new MyTableModel(column, filename, this, negozio);
		JTable table = new JTable(model);
		//table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setBackground(new Color(250, 251, 232));
		tableContainer.removeAll();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPane.setPreferredSize(new Dimension(500, 400));
		tableContainer.add(scrollPane);
		
		return table;
		
	}
}

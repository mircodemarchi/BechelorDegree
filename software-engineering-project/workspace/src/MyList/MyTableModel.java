package MyList;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import MainClass.Articolo;
import MainClass.ArticoloCount;
import MainClass.Negozio;
import MainClass.Ordine;
import Utenti.Utente;

public class MyTableModel extends AbstractTableModel{
	private String[] columnNames;
	private Object[][] data = new Object[0][0];
	private MyList list;
	private String filename;
	
	public MyTableModel(String[] columnNames, String filename, MyList list) {
		this.columnNames = columnNames;
		this.filename = filename;
		this.list = list;
		update();
	}
	
	public MyTableModel(String[] columnNames, Object[][] data) {
		this.columnNames = columnNames;
		this.data = data;
	}
	
	public MyTableModel(String[] columnNames, String filename, MyList list, Negozio negozio) {
		this.columnNames = columnNames;
		this.filename = filename;
		this.list = list;
		update(negozio);
	}

	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int col) {
        return columnNames[col];
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
    }
	
	public void setArrayAt(Object[] value, int row) {
        data[row] = value;
    }
	
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
	
	private void update(Negozio negozio) {
		ArrayList<Ordine> arrayListOrdini = new ArrayList<Ordine>();
		for(Object o: list.getArrayList()) {
			if( o instanceof Ordine && ((Ordine) o).getNegozio().equals(negozio)) {
				arrayListOrdini.add((Ordine) o);
			}
		}
		
		data = new Object[arrayListOrdini.size()][columnNames.length];
		int i = 0;
		for(Ordine o: arrayListOrdini) {
			setArrayAt(o.getLongArrayString(), i);
			i++;
		}
	}
	
	private void update() {
		data = new Object[list.getArrayList().size()][columnNames.length];
		int i = 0;
		for(Object o: list.getArrayList()) {
			if(filename.equalsIgnoreCase("ArticleDataBase")) {
				setArrayAt(((Articolo) o).getArrayString(), i);
			}
			else if(filename.equalsIgnoreCase("OrderDataBase")  || filename.equalsIgnoreCase("OrderNegozioDataBase")) {
				setArrayAt(((Ordine) o).getArrayString(), i);
			}
			else if(filename.equalsIgnoreCase("ArticleNegozioDataBase")) {
				setArrayAt(((ArticoloCount) o).getArrayString(), i);
			}
			else if(filename.equalsIgnoreCase("UserDataBase")) {
				setArrayAt(((Utente) o).getArrayString(), i);
			}
			i++;
		}
	}
	
}

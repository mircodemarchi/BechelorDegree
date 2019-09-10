package MyList;

import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import MainClass.Negozio;

public interface MyList {
	public void display(JList<Object> list, JPanel containerList );
	public void addObject(Object object);
	public void removeObject(Object object);
	public void saveData();
	public ArrayList<Object> getArrayList();
	public JTable display(JPanel centroCentro, String[] columnnames);
	public JTable display(JPanel tableContainer, String[] columnNames, Object[][] data);
	public void displayArticoli(TableModel tableModel, JTable table, JPanel containerList );
	public void displayNegozi(TableModel tableModel, JTable table, JPanel containerList );
	public void displayForOrder(JTable table, JPanel listContainer, String[] columnNames);
	public void displayForNegozio(JTable table, JPanel listContainer, String[] columnNames);
	public JTable display(JPanel tableContainer, String[] columnNames, Negozio negozio);
}

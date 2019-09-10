package MyList;

import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.TableModel;

import MainClass.Negozio;

public class MyListProxyFactory implements MyList{
	private MyListRealFactory realList = null;
	private String filename;
	
	public MyListProxyFactory(String filename) {
		this.filename = filename;
	}
	
	public void addObject(Object object){
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.addObject(object);
	}
	
	public void removeObject(Object object) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.removeObject(object);
	}
	
	public ArrayList<Object> getArrayList(){
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		return realList.getArrayList();
	}
	
	public void display(JList<Object> list, JPanel containerList ) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.display(list, containerList);
	}
	
	public JTable display(JPanel tableContainer, String[] columnNames) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		return realList.display(tableContainer, columnNames);
	}
	
	public JTable display(JPanel tableContainer, String[] columnNames, Object[][] data) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		return realList.display(tableContainer, columnNames, data);
	}

	public void saveData() {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.saveData();
	}
	
	public void displayArticoli(TableModel tableModel, JTable table, JPanel containerList ) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.displayArticoli(tableModel, table, containerList);
	}
	
	public void displayNegozi(TableModel tableModel, JTable table, JPanel containerList ) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.displayNegozi(tableModel, table, containerList);
	}
	
	public void displayForOrder(JTable table, JPanel listContainer, String[] columnNames) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.displayForOrder(table, listContainer, columnNames);
	}

	public void displayForNegozio(JTable table, JPanel listContainer, String[] columnNames) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		realList.displayForNegozio(table, listContainer, columnNames);
	}

	public JTable display(JPanel tableContainer, String[] columnNames, Negozio negozio) {
		if(realList == null) {
			realList = new MyListRealFactory(filename);
		}
		
		return realList.display(tableContainer, columnNames, negozio);
	}


}

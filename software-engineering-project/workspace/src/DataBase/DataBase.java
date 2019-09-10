package DataBase;

public interface DataBase {
	public static final String path = "/Users/mircodemarchi/Desktop/EclipseWorkspace/IngegneriaDelSoftware/data/";
	
	public Object[] readData();
	public void writeData(Object[] objectArray);
}

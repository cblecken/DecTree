import java.util.List;

public interface DataSet {

	/**
	 * get all the values in a column
	 * 
	 * @param column
	 * @return
	 */
    public List<Object> getValues(String column);
    
    /**
     * get the header array
     * 
     * @return String[] header array
     */
	public String[] getHeader();
    
//    public ArrayList<String> getValues(String column);

//    public Label getLabel();
    
    /**
     * Find all the distinct values of the values in the column
     * 
     * @param column
     * @return
     */
	public List<Object> getDistinctValues(String column);
	
	/**
	 * Get a subset of the data with a certain value (key) on a column
	 * 
	 * @param column : column name
	 * @param key 
	 * @param clasColumn
	 * @return
	 */
    public List<Object> getSubset(String column, String key, String clasColumn);
    
	/**
	 * Return the row count of this data set
	 * @return
	 */
	public int getRowCount();
	
	/**
	 * Return the for the this row by count
	 * @param count
	 * @return
	 */
	public Object[] getRow(int count);
	
	/**
	 * 
	 * @param oldDataSet
	 * @param columnToBeRemoved
	 * @param key
	 * @return
	 */
	public DataSet newSubset(DataSet oldDataSet, String columnToBeRemoved, String key);
}
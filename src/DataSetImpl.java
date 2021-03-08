import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataSetImpl implements DataSet {

	String[] header = null;
	List<Object[]> rows = new ArrayList<Object[]>();
	
    /** Column name which contains data labels. */
    private String labelColumn;
	
    /** 
     * create new data set when the data needs to be constricted due to removing an already
     * processed column and the values associated with tree value chosen
     * @param oldDataSet - existing data set
     * @param columnToBeRemoved - column name
     * @param key - value for the column which also need to be removed
     * @return DataSet
     */
	public static DataSet newSubset(DataSet oldDataSet, String columnToBeRemoved, String key) {
		return null;
	}
	
	/**
	 * Standard factory constructor
	 * 
	 * @param labelColumn
	 * @param header
	 * @param inrows
	 * @return
	 */
	public static DataSet newDataSetImpl(String labelColumn, String[] header, Object[]... inrows) {
		return new DataSetImpl(labelColumn, header, inrows);
	}
	
	/** 
	 * private constructor 
	 * @param labelColumn
	 * @param header
	 * @param inrows
	 */
    private DataSetImpl(String labelColumn, String[] header, Object[]... inrows) {
        this.labelColumn = labelColumn;
        this.header = header;
        for (int i = 0; i < header.length; i++) {
        	rows.add(inrows[i]);
        }     
    }
    
	@Override
	public List<Object> getValues(String column) {
		return createColumnList(getColumnIndex(column));
	}

	private List<Object> createColumnList(int columnIndex) {
		List<Object> retList = new ArrayList<Object>(); 
		for (Object[] row : rows) {
			retList.add(row[columnIndex]);
		}
		return retList;
	}

	private int getColumnIndex(String column) {
		for (int i=0; i<header.length; i++) {
			if (header[i].equals(column)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public String[] getHeader() {
		return header;
	}

	@Override
	public List<Object> getDistinctValues(String column) {
		//get column index
		int ind = getColumnIndex(column);
		Set<Object> mySet = new HashSet<Object>();
		for (Object[] row : rows) {
			Object val = row[ind];
			if (!mySet.contains(val)) {
				mySet.add(val);
			}
		}
		return Arrays.asList(mySet.toArray());
	}

	@Override
	public List<Object> getSubset(String column, String key, String classificationColumn) {
		// given the column which classifies the data return the subset
		// for specific column which is classified 
		int hind = -1;
		int ind = -1;
		for (int k=0; k<header.length; k++) {
			if (classificationColumn.equals(header[k])) {
				hind = k;
				break;
			}
		}
		List<Object> ret = new ArrayList<Object>();
		ind = getColumnIndex(column);
		for (int j=0; j<rows.size(); j++) {
			String[] row = (String[])rows.get(j);
			if (row[ind].equals(key)) {
				Object obj = row[hind];
				ret.add(obj);
			}
		}
		return ret;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getRow(int count) {
		return rows.get(count);
	}



}

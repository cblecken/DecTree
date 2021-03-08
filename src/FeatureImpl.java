
public class FeatureImpl implements Feature {

	private String column;
	
	public FeatureImpl(String column) {
		this.column = column;
	}
	
	@Override
	public String getColumn() {
		// TODO Auto-generated method stub
		return column;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	public static Feature newFeature(String column) {
		return new FeatureImpl(column);
	}

}

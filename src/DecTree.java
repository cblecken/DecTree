import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DecTree {
	
	Node root = null;
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		runTests();
        DataSet trainingData = readData(true);
        DecTree tree = new DecTree();
         
        List<Feature> features = getFeatures();
        
        tree.train(trainingData, features);
//         
//        // print tree after training
        tree.printTree();
//         
//        // read test data
        DataSet testingData = readData(false);
        System.out.println(testingData);
        
//      // classify all test data
        tree.classify(testingData);
         
    }

	private void classify(DataSet dataSample) {
		int count = dataSample.getRowCount();
		String[] headers = dataSample.getHeader();
		Node node = root;
		for (int i =0; i<count; i++) {
			Object[] row = dataSample.getRow(i);
			int rowInd = findFeatureIndex(headers, node.getFeatureName());
			if (rowInd!=-1) { 
				traverseTree(node, row[rowInd], row, headers);
			} else {
				// error
			}
		}			
	}
	
	private void traverseTree(Node node, Object object, Object[] row, String[] headers) {
		List<Node> children = node.getChildren();
		for (Node child : children) {
			if (child.getPredicate().equals(object)) {
				if (child.isLeaf()) {
					System.out.println("Record " + ((String)row[0]) + " classified as " + child.getPredicate());
				} else {					
					System.out.println("Match on node " + node.getFeatureName() + " " + child.getPredicate());
					int rowInd = findFeatureIndex(headers, child.getFeatureName());
					traverseTree(child, row[rowInd], row, headers);
				}
			}
		}
	}

	private int findFeatureIndex(String[] headers, String featureName) {
		for (int i = 0; i<headers.length; i++) 
			if (headers[i].equals(featureName))
				return i;
		return -1;
	}

	private void printTree() {
		// TODO Auto-generated method stub
		// BFS traversal
		traverse(root, 1);
	}

	private void traverse(Node node, int depth) {
		// TODO Auto-generated method stub
		visit(node, depth);
		for (Node cnode : node.getChildren()) {
			traverse(cnode, depth+1);
		}
	}

	private void visit(Node node, int depth) {
		for (int i =0; i<depth; i++) System.out.print("-");
		System.out.println(node.getVal() + " " + node.leaf);
	}

	private static void runTests() {
		List<Object> clas = new ArrayList<Object>();
		for (int i =0; i < 9; i++) {
//			Object[] objArr = new Object[2];
//			objArr[0] = "val";
//			objArr[1] = "YES";
			Object o = "YES";
			clas.add(o);
		}	
		for (int i =0; i < 5; i++) {
//			Object[] objArr = new Object[2];
//			objArr[0] = "val";
//			objArr[1] = "NO";
			Object o = "NO";
			clas.add(o);
		}	
		DecTree dt = new DecTree();
		Double en = dt.entropy(clas);
		System.out.println(en);
	}

	private static List<Feature> getFeatures() {
		Feature exercise = FeatureImpl.newFeature("exercising");
		Feature fatIntake = FeatureImpl.newFeature("fatIntake");
		Feature smoking = FeatureImpl.newFeature("smoking");
		Feature video = FeatureImpl.newFeature("videoAddiction");
		return Arrays.asList(exercise, fatIntake, smoking, video);
		
	}
//	private static List<Feature> getFeatures() {
//		Feature exer_reg = PredicateFeature.newFeature("exercising", "regularly");
//		Feature exer_occ = PredicateFeature.newFeature("exercising", "occasionally");
//		Feature exer_nev = PredicateFeature.newFeature("exercising", "never");		
//		Feature fatIn_hea = PredicateFeature.newFeature("fatIntake", "heavy");
//		Feature fatIn_med = PredicateFeature.newFeature("fatIntake", "medium");
//		Feature fatIn_low = PredicateFeature.newFeature("fatIntake", "low");
//		Feature smoke_hea = PredicateFeature.newFeature("smoking", "heavy");
//		Feature smoke_med = PredicateFeature.newFeature("smoking", "medium");
//		Feature smoke_lig = PredicateFeature.newFeature("smoking", "light");
//		Feature smoke_nev = PredicateFeature.newFeature("smoking", "never");
//		Feature vide_hea = PredicateFeature.newFeature("videoAddiction", "heavy");
//		Feature vide_med = PredicateFeature.newFeature("videoAddiction", "medium");
//		Feature vide_low = PredicateFeature.newFeature("videoAddiction", "low");
//		Feature vide_non = PredicateFeature.newFeature("videoAddiction", "none");
//		return Arrays.asList(exer_reg,exer_occ,
//				exer_nev,fatIn_hea,
//				fatIn_med,fatIn_low,
//				smoke_hea,smoke_med,
//				smoke_lig,smoke_nev,
//				vide_hea,vide_med,
//				vide_low,vide_non);
//		return Arrays.asList(exer_reg,fatIn_hea,
//				smoke_hea,vide_hea);}

	private static DataSet readData(boolean isTraining) throws IOException {
		// Expecting single file
		DataSet sds = null;
		Path path;
		if (isTraining)
			path = Paths.get(".","training.dat");
		else
			path = Paths.get(".","testdata.dat");
		try (Stream<String> lines = Files.lines(path)) {
			Iterator<String> iter = lines.iterator();
			String[] header = null;
			List<Object[]> arr = new ArrayList<>();
			String[] frag = null;
			int columns = 0;
			if (isTraining)
				while (!iter.next().startsWith("Training Data:"));
			else 
				while (!iter.next().startsWith("Feature Order For Data:"));
			
			while (iter.hasNext()) {
				frag = iter.next().split("[ ]+");
				if (!(frag.length==0 || frag.length==1)) {
					if (header == null) {
						header = frag;
						columns = header.length;
//						for (int i=0; i<columns; i++) {
//							arr.add(new ArrayList<String>());
//						}
					} else {
						arr.add(frag);
					}
				}
			}
			sds = DataSetImpl.newDataSetImpl(header[1], header, arr.toArray());
			System.out.println(sds);
		}
		return sds;
	}

	public void train(DataSet trainingData, List<Feature> features) {
	    root = buildTree(trainingData, features, 1, "root");
	}
	
	private Node buildTree(DataSet data, List<Feature> features, int currentDepth, String predicate) {
		// see if all features have been used - stop recursion here
		if (features.size()==0) {
			List<Object> labelList = data.getValues("class");
			String label = highestCountLabel(labelList);
			System.out.println("Create leaf node (feat=0): " +label);
			return new Node("Leaf with " + predicate, null, predicate, true);
		}
		if (allSameValues(data.getValues("class"))) {
			System.out.println("Create leaf node (same class): " + data.getValues("class").get(0));
			return new Node("Leaf with " + data.getValues("class").get(0), null, data.getValues("class").get(0), true);
		}
		
		Feature selFeature = findFeatureWithMostGain(data, features);
		List<Feature> copyOfFeatures = new ArrayList<Feature> (features.size());
		copyOfFeatures.addAll(features);
//		Collections.copy(copyOfFeatures, features);
		copyOfFeatures.remove(selFeature);
		
		Node parentNode = new Node(selFeature.getColumn() + " , " + predicate, selFeature.getColumn(), predicate, false);
		
		if (currentDepth<=4) {
			// iterate over all the values
			List<Object> distinctVals = data.getDistinctValues(selFeature.getColumn());
			for (Object singleVal : distinctVals) {
				// get the applicable data
				DataSet singleValData = data.newSubset(data, selFeature.getColumn(), (String)singleVal);
				Node childNode = buildTree(singleValData, copyOfFeatures, currentDepth+1, singleVal.toString());
				parentNode.getChildren().add(childNode);
			}
		}
		
		
		return parentNode;
	}
	
	private boolean allSameValues(List<Object> values) {
		if (values==null && values.size()==0)
			return false;
		else {
			String val = null;
			Iterator iter = values.iterator();
			while (iter.hasNext()) {
			// for (String v : values) {
				String v = (String)iter.next();
				if (val==null) val = v;
				else {
					if (!v.equals(val))
						return false;
				}
			}
		}
		return true;
	}

	private String highestCountLabel(List<Object> labelList) {
		Map<String, Integer> freq = new HashMap<String, Integer>();
		int maxCount = 0;
		String maxVal = null;
		for (Object valObj : labelList) {
			String val = (String)valObj;
			if (freq.containsKey(val)) {
				if (freq.get(val) + 1> maxCount) {
					maxCount = freq.get(val) + 1; 
					maxVal = val;
				}
				freq.put(val, freq.get(val) + 1);
			} else {
				if ( 1> maxCount) {
					maxCount =  1; 
					maxVal = val;
				}
				freq.put(val, 1);
			}	
		}
		return maxVal;
	}

//	private Feature findFeatureWithMostGain(DataSet sample, List<Feature> features) {
//		Feature featMostGain = null;
//		Double largestGain = 0.0;
//		for (Feature feature : features) {
//			if (featMostGain==null) featMostGain=feature;    
//				Optional<Object> opt = sample.getValue(feature.getColumn());
//				Object obj = opt.get();
//				List<String> ll = (List<String>)obj;
//				List<String> clas = sample.getValues("class");
//				//List<String> clasList = Arrays.asList(clas);
//				double partEntropy = 0.0;
//				Map<String, Double> freq = new HashMap<String, Double>();
//				for (String val : ll) {
//					if (freq.containsKey(val)) 
//						freq.put(val, freq.get(val) + 1.0);
//					else
//						freq.put(val, 1.0);
//				}
//				List<Object> total = new ArrayList<Object>();
//				for (String key :freq.keySet()) {
//					// probability of occurrence
//					Double prop = calcProp(freq, key); 
//					List<Object> subset = sample.getSubset(feature.getColumn(), key, "class");
//					partEntropy += prop * entropy(subset);
//					total.addAll(subset);
//				}
//				
//			//	System.out.println("Entropy for " + feature.getColumn() + " " + partEntropy);
//				
//				Double gain = entropy(total) - partEntropy;
//			//	System.out.println("Tot " + feature.getColumn() + " " + gain);
//				if (gain>largestGain) {
//					largestGain = gain;
//					featMostGain = feature;
//				}
////				if (feature.belongsTo(sample)) {
////					calculateGain(trainingData, feature);
////				}
//		}
//		System.out.println("Biggest Gain " + largestGain + " " + (featMostGain==null ? "null" : featMostGain.getColumn()));
//		return featMostGain;
//	}

	private Double entropy(List<Object> subset) {
		Map<String, Double> freq = new HashMap<String, Double>();
		Double entropy = 0.0;
		for (Object valObj : subset) {
			String val = (String)valObj;
			if (freq.containsKey(val))  
				freq.put(val, freq.get(val) + 1.0);
			else 
				freq.put(val, 1.0);	
		}
		for (Double fre : freq.values()) {
			entropy += (-fre/subset.size()) * Math.log((fre)/subset.size()) / Math.log(2);
		}
		return entropy;
	}

	private List<String> createSubset(List<String> ll, String key) {
		List<String> ret = new ArrayList<String>();
//		for (String val : ll) {
//			if (val.equals(key)) ret.add(e)
//		}
		return ret;
	}

	private double calcProp(Map<String, Double> freq, String key) {
		double total = 0.0;
		for (Double count : freq.values()) { total += count; }
		return freq.get(key) / total;
	}

}

package anomalydetection.train;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;

public class AdUtils {
	
    /**
     * 
     * @param line
     * @return
     */
	public static List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<List<String>> getRecords(String file) throws FileNotFoundException{
		List<List<String>> records = new ArrayList<>();

		int count = 0;
		try (Scanner scanner = new Scanner(new File(file));) {
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
				count++;
			}
		}
		return records;
	}
	
	/**
	 * 
	 * @param records
	 * @return
	 */
	public static DataSet getDataset(List<List<String>> records) {
		INDArray TotalInputs = Nd4j.zeros(records.size(), 4);
		INDArray TotalOutputs = Nd4j.zeros(records.size(), 4);
		
		for (int i = 0; i < records.size(); i++) {
			List<String> record = records.get(i);
	        double[] array = new double[record.size()];
	        
	        for(int j = 0; j < record.size(); j++) 
	        	array[j] = Double.valueOf(record.get(j));
	        
			TotalInputs.putRow(i, Nd4j.create(array));
			TotalOutputs.putRow(i, Nd4j.create(array));			
		}
		DataSet myData = new DataSet(TotalInputs, TotalOutputs);
		
		return myData;
	}

	/**
	 * 
	 * @param records
	 * @return
	 */
	public static DataSet getTrainingDataset(List<List<String>> records, DataNormalization normalizer) {
		INDArray input = Nd4j.zeros(records.size(), 4);
		
		for (int i = 0; i < records.size(); i++) {
			List<String> record = records.get(i);
	        double[] array = new double[record.size()];
	        
	        for(int j = 0; j < record.size(); j++) 
	        	array[j] = Double.valueOf(record.get(j));
	       
			input.putRow(i, Nd4j.create(array));
		}
		normalizer.transform(input);
		DataSet myData = new DataSet(input, input);
		
		return myData;
	}
	

	/**
	 * 
	 * @param records
	 * @return
	 */
	public static List<DataSet> getDatasets(List<List<String>> records) {
		List<DataSet> output = new ArrayList<DataSet>();
		
		for (int i = 0; i < records.size(); i++) {	
			INDArray tmpArray = Nd4j.zeros(records.size(), 4);
			List<String> record = records.get(i);
	   
			double[] array = new double[record.size()];
	        
	        for(int j = 0; j < record.size(); j++) 
	        	array[j] = Double.valueOf(record.get(j));
	        
	        tmpArray.putRow(i, Nd4j.create(array));
	      
	        output.add(new DataSet(tmpArray, tmpArray));	        
		}
	
		return output;
	}
	
	 public static String readLineByLineJava8(String filePath) 
	    {
	        StringBuilder contentBuilder = new StringBuilder();
	 
	        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
	        {
	            stream.forEach(s -> contentBuilder.append(s).append("\n"));
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	 
	        return contentBuilder.toString();
	    }
}

package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Parameter {
	
	public static long SEED = 1234556789;
	
	public String name;
	public String type;
	public double lowerBound;
	public double upperBound;
	public double lowerIncreaseRate;
	public double upperIncreaseRate;
	public List range;
	public int index = -1;
	public double nowValue = -1;
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setRange(String format){
		if (this.type.equals("FLT")){
			String org = format.substring(1, format.length()-1);
			String[] item = org.split(",");
			this.lowerBound = Double.valueOf(item[0]);
			this.upperBound = Double.valueOf(item[1]);
			return;
		}
		if (this.type.equals("ENUM")){
			this.range = new ArrayList<String>();
			String org = format.substring(1, format.length()-1);
			String[] item = org.split(",");
			for (int i=0; i<item.length; i++) this.range.add(item[i]);
		}
	}
	
	public void setIncreaseRate(String format){
		if (this.type.equals("FLT")){
			String org = format.substring(1, format.length()-1);
			String[] item = org.split(",");
			this.lowerIncreaseRate = Double.valueOf(item[0]);
			this.upperIncreaseRate = Double.valueOf(item[1]);
			return;
		}
	}
	
	public void rewind(){
		if (this.type.equals("FLT")) nowValue = -1;
		else if (this.type.equals("ENUM")) index = -1;
	}
	
	public void rewindTo(double target){
		if (this.type.equals("FLT")) nowValue = target;
	}
	
	public Map nextVale(){
		Map result = new HashMap();
		if (this.type.equals("FLT")){
			if ( nowValue == -1 ) {
				Random r = new Random();
//				r.setSeed(SEED);
				double randomBound = lowerBound + r.nextDouble()*(upperBound-lowerBound);
				nowValue = randomBound;
			}
			else {
				Random r = new Random();
//				r.setSeed(SEED);
				double randomIRate = lowerIncreaseRate + r.nextDouble()*(upperIncreaseRate-lowerIncreaseRate);
				nowValue = nowValue*(1+randomIRate);
			}
			result.put(name,nowValue);
		}
		else if (this.type.equals("ENUM")){
			index++;
			if (index >= range.size()) return null;
			else{
				result.put(name,range.get(index));
			}
		}
		return result;
	}
	
}

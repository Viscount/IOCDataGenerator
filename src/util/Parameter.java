package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Parameter {
	
	public String name;
	public String type;
	public double lowerBound;
	public double increaseRate;
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
			this.lowerBound = Double.valueOf(format);
			return;
		}
		if (this.type.equals("ENUM")){
			this.range = new ArrayList<String>();
			String org = format.substring(1, format.length()-1);
			String[] item = org.split(",");
			for (int i=0; i<item.length; i++) this.range.add(item[i]);
		}
	}
	
	public void setIncreaseRate(double iRate){
		this.increaseRate = iRate;
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
			if ( nowValue == -1 ) nowValue = lowerBound;
			else nowValue = nowValue*(1+increaseRate);
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

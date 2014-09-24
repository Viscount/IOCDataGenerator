package core;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Parameter;

import conf.Config;

public class Generator {
	
	public Config config;

	public List combineItem;
	public Map nowItem;
	public List finalResult;
	
	private void configuration(){
		config = new Config();
		config.readingConf();
	}
	
	private void generate(){
		System.out.println("Start Generating...");
		finalResult = new ArrayList();
		combineItem = new ArrayList();
		nowItem = new HashMap();
		buildItem(0);
		for (int i=0; i<combineItem.size(); i++){
			nowItem = new HashMap();
			nowItem.putAll((Map) combineItem.get(i));
			config.timer.rewind();
			for (int j=0; j<config.floatPara.size();j++){
				Parameter p = (Parameter) config.floatPara.get(j);
				p.rewind();
			}
			Map nowTime = config.timer.nextTime();
			while ( nowTime!=null ){
				nowItem.putAll(nowTime);
				for (int j=0; j<config.floatPara.size();j++){
					Parameter p = (Parameter) config.floatPara.get(j);
					nowItem.putAll(p.nextVale());
					nowTime = config.timer.nextTime();
				}
				Map finalItem = new HashMap();
				finalItem.putAll(nowItem);
				finalResult.add(finalItem);
			}
		}
		System.out.println("Generation Done.");
	}
	
	private void toJson(){ 
	    JSONArray jsonMembers = new JSONArray();
	    try{
	    	for (int i=0; i<finalResult.size();i++){
	    		Map item = (Map) finalResult.get(i);
				jsonMembers.put(i, item);
	    	}
	    	File f = new File("data.json");
	    	FileWriter fos = new FileWriter(f);
	    	fos.write(jsonMembers.toString());
	    	fos.close();
	    }
	    catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void buildItem(int level){
		if (level == config.enumPara.size()){
			Map addItem = new HashMap();
			addItem.putAll(nowItem);
			combineItem.add(addItem);
			return;
		}
		Parameter para = (Parameter)config.enumPara.get(level);
		para.rewind();
		Map nextvalue = para.nextVale();
		while ( nextvalue!=null ){
			nowItem.putAll(nextvalue);
			buildItem(level+1);
			nextvalue = para.nextVale();
		}
		nowItem.remove(para.name);
	}
	
	public static void main(String[] args){
		Generator generator = new Generator();
		generator.configuration();
		generator.generate();
		generator.toJson();
	}

}

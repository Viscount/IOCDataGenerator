package conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import util.Parameter;
import util.Timer;

public class Config {
	
	public static String Path = "config";
	
	public Timer timer;
	public List parameters;
	public List enumPara;
	public List floatPara;
	
	public void readingConf(){
		File f = new File(Path);
		try{
			System.out.println("Loading Configuration from "+Path+" ...");
			BufferedReader bf = new BufferedReader(new FileReader(f));
			String content;
			boolean flag = false;
			while((content = bf.readLine())!= null){
				if (content.startsWith("#")) continue;
				if (content.startsWith("Time:")){
					timer = new Timer();
					boolean yActive = false;
					boolean mActive = false;
					boolean dActive = false;
					for (int i=0; i<3; i++){
						content = bf.readLine();
						if (i==0){
							// year info
							String[] spliter = content.split(",");
							if (spliter[0].charAt(spliter[0].length()-1)=='0') yActive = false;
							else yActive = true;
							timer.setYearRange(Integer.valueOf(spliter[1]), Integer.valueOf(spliter[2]));
						}
						else if (i==1){
							// month info
							if (content.charAt(content.length()-1)=='0') mActive = false;
							else mActive = true;
						}
						else{
							// day info
							if (content.charAt(content.length()-1)=='0') dActive = false;
							else dActive = true;
						}
					}
					timer.setActive(yActive, mActive, dActive);
				}
				if (content.startsWith("Parameter:")){
					flag = true;
					parameters = new ArrayList<Parameter>();
					enumPara = new ArrayList<Parameter>();
					floatPara = new ArrayList<Parameter>();
					continue;
				}
				if (flag){
					Parameter p = new Parameter();
					String[] spliter = content.split(";");
					p.setName(spliter[0]);
					p.setType(spliter[1]);
					p.setRange(spliter[2]);
					if(!p.type.equals("ENUM"))p.setIncreaseRate(spliter[3]);
					parameters.add(p);
					if ( p.type.equals("ENUM") ) enumPara.add(p);
					else if ( p.type.equals("FLT") ) floatPara.add(p);
				}
			}
			System.out.println("Loading complete.");
			bf.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}

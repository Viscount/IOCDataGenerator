package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timer {
	
	private static int[] daysInNormal = {31,28,31,30,31,30,31,31,30,31,30,31};
	private static int[] daysInLeap = {31,29,31,30,31,30,31,31,30,31,30,31};
	
	public int year = -1;
	public int month = -1;
	public int day = -1;
	
	public boolean yearActive,monthActive,dayActive;
	public List yearRange;
	public int yearIndex;
	
	public void setYearRange(int startYear, int endYear){
		yearRange = new ArrayList<Integer>();
		for (int i=startYear;i<=endYear;i++){
			yearRange.add(i);
		}
	}
	
	public void setActive(boolean yearAct, boolean monthAct, boolean dayAct){
		this.yearActive = yearAct;
		this.monthActive = monthAct;
		this.dayActive = dayAct;
	}
	
	public void rewind(){
		this.year = -1;
	}
	
	public Map nextTime(){
		// generator next time, return null for year exceed limit
		Map result = new HashMap<String,Integer>();
		if ( year == -1 ){
			yearIndex = 0;
			if (yearActive) year = (int) yearRange.get(yearIndex);
			if (monthActive) month = 1;
			if (dayActive) day = 1;
		}
		else {
			if (dayActive){
				day++;
				if (((!isLeapYear(year))&&(day > daysInNormal[month])) || ((isLeapYear(year))&&(day > daysInLeap[month]))){
					day = 1;
					month++;
					if (month > 12) {
						month = 1;
						yearIndex++;
						if (yearIndex < yearRange.size()) year = (int) yearRange.get(yearIndex);
						else return null;
					}
				}
			}else if (monthActive){
				month++;
				if (month > 12) {
					month = 1;
					yearIndex++;
					if (yearIndex < yearRange.size()) year = (int) yearRange.get(yearIndex);
					else return null;
				}
			}else {
				yearIndex++;
				if (yearIndex < yearRange.size()) year = (int) yearRange.get(yearIndex);
				else return null;
			}
		}
		if (yearActive) result.put("Year", year);
		if (monthActive) result.put("Month", month);
		if (dayActive) result.put("Day", day);
		return result;
	}
	
	public boolean isLeapYear(int year){
		return (year%4==0&&year%100!=0)||year%400==0;
	}
}

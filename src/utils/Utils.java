package utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Utils {
	
	public static String[] parseString(String s){
		String[] rslt;
		String name = s.substring(0,s.indexOf("("));
		String[] params = s.substring(s.indexOf("(")+1, s.length()-1).split(",");
		rslt = new String[params.length + 1];
		rslt[0] = name;
		
		int i = 1;
		for(String p : params){
			rslt[i] = p;
			i++;
		}
		return rslt;
	}

	public static String[] parseItems(String items){
		String[] split = items.substring(1, items.length() - 1).split("item");
		ArrayList<String> res = new ArrayList<String>(); 
		for(int i = 0; i < split.length; i++){
			if(split[i].endsWith(",")){
				split[i] = split[i].substring(0, split[i].length()-1);
			}
			if(split[i].startsWith("(") && split[i].endsWith(")")){
				split[i] = split[i].substring(1, split[i].length() - 1);
			}
			if (split[i].length() != 0){
				res.add(split[i]);
			}
		}
		
		return res.toArray(new String[res.size()]); 
	}
	
	
	
	public static void main(String[] args) {
		String s = "[item(material2,3),item(material1,3)]";
		for (String i : Utils.parseItems(s)){
			System.out.println("w : "+ i );
		}
	}
}

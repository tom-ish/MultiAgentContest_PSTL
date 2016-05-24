package utils;

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
		return items.split(",");
	}
	
	public static void main(String[] args) {
		for(String s : Utils.parseString("item(material3,2)")){
			System.out.println(s);			
		}
	}
}

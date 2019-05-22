package CallDroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;


public class Get_Feature extends Get_File {
	ArrayList<String> Intent_Feature = new ArrayList<>();
	public static int option;
	String target;
	static String load = "";
	int index = 0;
	int tmpI = 0;
	static ArrayList<String> com_class = new ArrayList<>();
	int space_count = 0;
	int line_count = 0;
	int check = 0;
	String success = "";
		
	Get_Feature() {
		target = "";
	}
	
	public boolean struct(String line) {
		String result = "";
		Pattern p1 = Pattern.compile("package=\"[A-Za-z]");
		Matcher m1 = p1.matcher(line);
		if(m1.find()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String tree(String line) {
		String []tmp = new String[100];
		String result = "";
		Pattern p1 = Pattern.compile("package=\".+[A-Za-z]\"");
		Pattern p2 = Pattern.compile("\"+.+[A-Za-z]+\"");
		Matcher m1 = p1.matcher(line);

		if(m1.find()) {
			result = m1.group();
		}
		
		Matcher m2 = p2.matcher(result);
		
		if(m2.find()) {
			result = m2.group();
		}
		tmp = result.split(" ");
		result = tmp[0];
		if(result.length() > 2) {
			result = result.substring(1, result.length()-1);
		}
		else {
			result = "";
		}
		
		return result;
	}
	
	public boolean isIntent(String line) {
		Pattern p = Pattern.compile("<intent-filter>");
		Matcher m = p.matcher(line);
		if(m.find()){
			return true;
		}
		return false;
	}
	
	public int intentIndex(String[] line, int index) {
		
		Pattern p1 = Pattern.compile("name=");		
		Matcher m1 = p1.matcher(line[index-1]);
		
		if(m1.find() && !line[index-1].contains("meta-data") && !line[index-1].contains("android.intent") && 
				((line[index-1].contains("activity") || line[index-1].contains("service") || line[index-1].contains("receiver")))) {
			return index;
		}
		return intentIndex(line, index-1);
	}
	
	public boolean isOverlap(ArrayList<String> list, String tar, int index) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i) != null) {
				if((list.get(i)).equals(tar)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String name_Feature(String line) {
		//System.out.println(line);
		
		String result = "";
		Pattern p = Pattern.compile("android:name=\".+[A-Za-z]\"");
		Pattern p2 = Pattern.compile("\".+[A-Za-z]\"");
		Pattern p3 = Pattern.compile("[A-Z]");
		//Pattern p4 = Pattern.compile("[a-z]");
		Matcher m = p.matcher(line);
		
		if(m.find()) {
			result = m.group(0);
		}
		
		Matcher m2 = p2.matcher(result);
		
		if(m2.find()) {
			result = m2.group(0);
		}
		
		String[] ret = result.split("\\b+android");
		String[] filter;
		String[] filter2;
		
		
		//System.out.println("=========");
		//System.out.println(result);
		
		/*
		filter2 = result.split("\\.");
		Matcher m4 = p4.matcher(filter2[0]);
		*/
		
		if(result.contains("android.")) {
			if(result.contains("android.permission")) {
				result = ret[0];
			}
			else {
				result = ret[1];
				filter = result.split("\\.");
				for(int i = 0; i < filter.length; i++) {
					Matcher m3 = p3.matcher(filter[i]);
					if(m3.find()) {
						result = filter[i];
						result = "\"" + result;
					}
				}
			}
		}
		
		else {
			result = ret[0];
		}
		
		
		
		filter2 = result.split("\\.");		
		
		if(filter2.length > 1) {
			for(int i = 0; i < filter2.length; i++) {
				Matcher m3 = p3.matcher(filter2[i]);
				if(m3.find()) {
					result = filter2[i];
					result = "\"" + result;
				}
			}
		}		
		
		String[] last = result.split("\\.");
		result = last[last.length-1];

		return result;
	}
	
	public void classList(String xml_name) throws IOException {
		
		String line = "";
		String main_Name = "";
		String [] all_line;
		space_count = 0;
		line_count = 0;
		int i = 0;
		target = "";
		int option = 0;
		
		try {		
			File xml_single = new File(xml_name);
			File tmp_line = new File(xml_name);
			File real_line = new File(xml_name);
			FileReader xmlReader = new FileReader(xml_single);
			FileReader tmpReader = new FileReader(tmp_line);
			FileReader realReader = new FileReader(real_line);
			BufferedReader tmpBuf = new BufferedReader(tmpReader);
			BufferedReader realBuf = new BufferedReader(realReader);
			int singleCh = 0;
			
			
			//line count read
			while((line = tmpBuf.readLine()) != null) {
				line_count++;
				Intent_Feature.add("P");
				Intent_Feature.add("P");
			}
			
			line = "";
			all_line = new String[line_count+1];
			
			//real line save
			while((line = realBuf.readLine()) != null) {
				
				all_line[i] = line;
				//System.out.println(all_line[i]);
				if (isIntent(all_line[i]) && i > 1) {
					
					int tmpIndex = intentIndex(all_line, i);
					String []tmp = new String[10];		
					String feature = name_Feature(all_line[tmpIndex-1]);
						
					tmp = feature.split(" ");
					feature = tmp[0];
					if(feature.length() >= 2) {
						feature = feature.substring(1, feature.length()-1);
						
						if(!isOverlap(Intent_Feature,feature,tmpIndex)) {							
							com_class.add(index,feature);												
							Intent_Feature.add(tmpIndex-1, feature);
							
							//System.out.println(feature);
							
							index++;
						}
																
					}
							
				}
				else {
				
				}
				if(struct(all_line[i])) {
					load = tree(all_line[i]);
					load = load.replace(".", "\\\\");
				}
				i++;
			}			
			i = 0;
					
								
						
		}catch(FileNotFoundException e) {	
			
		}
	}
	
	
	public String get_Invoke(String name) throws IOException {
		Pattern p1 = Pattern.compile("[A-Za-z]");
		Matcher m1 = p1.matcher(name);
		String line = "";
		String result = "";
		
		if(m1.find()) {
			String smali_path = name;
			File smali = new File(smali_path);
			FileReader sReader = new FileReader(smali);
			BufferedReader sBuf = new BufferedReader(sReader);
		
			
			try {
				while ((line = sBuf.readLine()) != null) {
					/*
					Pattern p3 = Pattern.compile("->[A-Za-z][1]\\(");
					Pattern p4 = Pattern.compile("\\?\\?");
					*/
					Pattern p2 = Pattern.compile("invoke-+[A-Za-z]");
					Matcher m2 = p2.matcher(line);
					if (m2.find()) {
						String []tmp = new String[3];

						tmp = line.split("\\}, ");
						
						
						//Matcher m3 = p3.matcher(tmp[1]);
						//Matcher m4 = p4.matcher(tmp[1]);
					
						result += ("\n" + tmp[1]);
												
					}
				}
			} catch (FileNotFoundException fn) {
				fn.printStackTrace();
			}
		
		}
		return result;
	}
	
	public void fileRecur(String name, String target, String ret) {
		
		File dir = new File(name);
		File[] fileList = dir.listFiles();
		if(fileList == null) { return; }
		if(dir != null) {
			for (int j = 0; j < fileList.length; j++) {						
				File tmpFile = fileList[j];
				if(tmpFile.isFile() && tmpFile.getName().equals(target)) {
					ret = ret + "\\" + target;
					success = ret;
					return;
				}
				
				else if (tmpFile.isDirectory()) {
					ret += "\\" +tmpFile.getName();
					fileRecur(name + "\\" +tmpFile.getName(), target, ret);
					ret = name;
				}
			}
		}
	}
	
	public Node get_Smali(String name) throws IOException {
		String tmp = "";
		int dbCheck = 0;
		int dbIndex = 0;
		int sIndex = 0;
		int real_Intent = 0;
		
		for (int i = 0; i < line_count; i++) {
			if(Intent_Feature.get(i) != "P") {
				real_Intent++;
			}
		}

		Node apk = new Node();
		ArrayList<String> remove = new ArrayList<>();
		apk.name = name;
		apk.IC_size = real_Intent;
		
		//all smali file roop
		for (int i = 0; i < line_count; i++) {		
			if(Intent_Feature.get(i) != "P") {
				String smali_target = Intent_Feature.get(i) + ".smali";
				//tmpBox[i][sIndex] = target;
				//System.out.println(smali_target);
				String find = "";
				String parse[];
				
				fileRecur(name, smali_target, name);
				find = success;
				tmp = get_Invoke(find);
				parse = tmp.split("\n");
				
				// one smali file roop
				for (int j = 1; j < parse.length; j++) {
					
					//System.out.println(parse[j]);
					
					for (int k = 0; k < remove.size(); k++) {
						if(remove.get(k).contains(parse[j])) {
							dbIndex = k;
							dbCheck = 1;
							break;
						}
					}
					
					if(dbCheck == 1) {
						String countS = remove.get(dbIndex);
						String dum = countS.substring(0,countS.length() - 1);
						countS = countS.substring(countS.length() -1, countS.length());
						int count = Integer.parseInt(countS);
						count++;
						countS = String.valueOf(count);
						remove.set(dbIndex, dum + countS);
					}
					else if (dbCheck == 0) {
						remove.add(parse[j] + "1");
					}
					dbCheck = 0;
					dbIndex = 0;
				}									
			}			
		}
		//System.out.println(result);
		
		apk.feature = new String[real_Intent][remove.size() + 1];
		apk.IM_count = new float[real_Intent][remove.size() + 1];
		apk.C_count = new float[real_Intent];
		
		for(int i = 0; i < line_count; i++) {
			
			String smali_target = Intent_Feature.get(i) + ".smali";
			
			if(Intent_Feature.get(i) != "P") {
				apk.feature[sIndex][0] = smali_target;
				apk.IM_count[sIndex][0] = 0;
		
				for (int j = 1; j  <= remove.size(); j++) {	
					
					String countS = remove.get(j - 1);
					countS = countS.substring(countS.length() -1, countS.length());
					int count = Integer.parseInt(countS);
					float countF = (float)count;
					apk.IM_count[sIndex][j] = countF;
					apk.IM_size += countF;
					apk.C_count[sIndex] += countF;
					//apk.feature[sIndex][j] = getSHA256(remove.get(j - 1));
					apk.feature[sIndex][j] = remove.get(j - 1);
					//System.out.println(apk.feature[sIndex][j]);
				}
			
				sIndex++;
			
			}
		}
		
		/*
		for(int i = 0; i < apk.IC_size; i++) {
			System.out.println(apk.feature[i][0]);
		}
		*/
		//System.out.println(apk.IM_size);
		
		sIndex = 0;
		
		return apk;
		
	}
	public String getSHA256(String str){

		String SHA = ""; 

		try{

			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 

			sh.update(str.getBytes()); 

			byte byteData[] = sh.digest();

			StringBuffer sb = new StringBuffer(); 

			for(int i = 0 ; i < byteData.length ; i++){

				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

			}

			SHA = sb.toString();

			

		}catch(NoSuchAlgorithmException e){

			e.printStackTrace(); 

			SHA = null; 

		}

		return SHA;

	}
	
}


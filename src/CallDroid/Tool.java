package CallDroid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tool extends Get_Feature {
	
	public static void createCSV(String[] data, String title, String filepath, float fp) {
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(filepath + "/" + title + ".csv", true));
			
			for(String tmp : data) {
				if(tmp != null) {
				fw.write(tmp + ",");
				fw.newLine();
				
				}
			}
			
			fw.flush();
			fw.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		Compare_Algorithm compare = new Compare_Algorithm();
		Get_File list = new Get_File();
		//String ori_path = "D:\\Kbc_OSS_Tool\\apk_test\\";
		//String obf_path = "D:\\Kbc_OSS_Tool\\apk_test_obf\\";
		
		String ori_path = args[0] + "\\";
		String obf_path = args[1] + "\\";
		String path = args[2];
		String fileName = args[3];
		
		String[] obf_list = list.get_FileList(obf_path);
		String[] ori_list = list.get_FileList(ori_path);
		String obf_xml[] = null;
		String ori_xml[] = null;
		String obf_inv[] = null;
		String ori_inv[] = null;
		String[] data = null;
		Node [] ori_str = new Node[ori_list.length + 2];
		Node [] obf_str = new Node[obf_list.length + 2];
		ClassNode [] ori_class = new ClassNode[ori_list.length * 50];
		ClassNode [] obf_class = new ClassNode[obf_list.length * 50];
		
		data = new String[ori_list.length * 500];
		
		ori_xml = new String[ori_list.length + 1];
		obf_xml = new String[obf_list.length + 1];		
		ori_inv = new String[ori_list.length + 1];
		obf_inv = new String[obf_list.length + 1];
		int ori_cSize = 0; int ori_cIndex = 0;
		int obf_cSize = 0; int obf_cIndex = 0;
		
		for (int i = 0; i < obf_list.length; i++) {
			Get_Feature test = new Get_Feature();
			obf_xml[i] = obf_path + obf_list[i] + "\\AndroidManifest.xml";
			test.classList(obf_xml[i]);
			String nameObf = obf_path + obf_list[i] + "\\smali";
			obf_str[i] = test.get_Smali(nameObf);
		}
		
		
		for (int i = 0; i < ori_list.length; i++) {
			Get_Feature test = new Get_Feature();
			ori_xml[i] = ori_path + ori_list[i] + "\\AndroidManifest.xml";
			test.classList(ori_xml[i]);
			String nameOri = ori_path + ori_list[i] + "\\smali";
			ori_str[i] = test.get_Smali(nameOri);
			test = null;
		}
		
		for (int i = 0; i < ori_list.length * 50; i++) {
			ClassNode initNode = new ClassNode();
			ori_class[i] = initNode;
		}
		for (int i = 0; i < obf_list.length * 50; i++) {
			ClassNode initNode = new ClassNode();
			obf_class[i] = initNode;
		}
		
		Get_Feature sha = new Get_Feature();
		
		for (int i = 0; i < obf_list.length; i++) {
			for (int j = 0; j < obf_str[i].feature.length; j++) {
				for (int k = 1; k < obf_str[i].feature[j].length; k++) {
					obf_class[obf_cIndex].apkName = obf_list[i];
					int tmp = obf_str[i].feature[j][0].length();
					//if(obf_class[j].className == "" ) {
						obf_class[obf_cIndex].className = obf_str[i].feature[j][0].substring(0, tmp - 6);
					//}
					obf_class[obf_cIndex].feature += obf_str[i].feature[j][k] + "\n";
				}
				obf_cIndex += 1;
			}
			obf_cSize += obf_str[i].feature.length;
		}
		
		for (int i = 0; i < ori_list.length; i++) {
			for (int j = 0; j < ori_str[i].feature.length; j++) {
				for (int k = 1; k < ori_str[i].feature[j].length; k++) {
					ori_class[ori_cIndex].apkName = ori_list[i];
					int tmp = ori_str[i].feature[j][0].length();
					//if(obf_class[j].className == "" ) {
						ori_class[ori_cIndex].className = ori_str[i].feature[j][0].substring(0, tmp - 6);
					//}
					ori_class[ori_cIndex].feature += ori_str[i].feature[j][k] + "\n";
				}
				ori_cIndex += 1;
			}
			ori_cSize += ori_str[i].feature.length;
		}
		/*
		
		for(int i = 0; i < 10; i++) {
			String tmp1[] = ori_class[i].feature.split("\\n");
			String tmp2[] = obf_class[i].feature.split("\\n");
			System.out.println(ori_class[i].className);
			System.out.println(tmp1[0]);
			System.out.println(obf_class[i].className);
			System.out.println(tmp2[0]);
			System.out.println("=================");
		}*/
		
		int set_size = obf_list.length;
		int all_count = 0;
		int success_all = 0;
		float tp = 0;
		float fp = 0;
		float tn = 0;
		float fn = 0;
		float ac = 0;
		float pc = 0;
		float rc = 0;
		float sum_ac = 0;
		float sum_pc = 0;
		float sum_rc = 0;

		int same_c = 0;	
		
		/*
		System.out.println("===== CallDroid LCS result ====");
		// System.out.println("LCS : " +lcs.distance(s1, s2));
		for(int i = 0; i < ori_list.length; i++) {
			for(int j = 0; j < obf_list.length; j++) {
				
				all_count++;
				
				String[] parse = obf_list[j].split("-");
				float thres = 0.5f;
				float result = 0;
				result = compare.simScore(ori_str[i], obf_str[j]);
				
				//System.out.println(ori_list[i] + ": " + ori_str[i].IM_size);
				//System.out.println(obf_list[j] + ": " + obf_str[j].IM_size);
				
				if((ori_list[i].contains(parse[0])) && result >= thres) {					
					tp += 1.0;
				}
				else if ((ori_list[i].contains(parse[0])) && result < thres) {			
					fn += 1.0;			
				}
				else if(!ori_list[i].contains(parse[0]) && result >= thres) {
					fp += 1.0;
				}		
				else if(!ori_list[i].contains(parse[0]) && result < thres) {						
					tn += 1.0;
				}
				result = (float) 0.0;											
			}			
		}
		*/
		// Class Detection Option
		int check = 0;
		int cop_count = 0;
		info.debatty.java.stringsimilarity.MetricLCS lcs = 
			    new info.debatty.java.stringsimilarity.MetricLCS();
		System.out.println("===== CallDroid Class Option LCS result ====");
		System.out.println(ori_cSize);
		System.out.println(obf_cSize);
		for (int i = 0; i < ori_cSize; i++) {
			for (int j = 0; j < obf_cSize; j++) {
				String cur_oriA = ori_class[i].apkName;
				String cur_oriC = ori_class[i].className;
				String cur_oriF = ori_class[i].feature;
				String cur_obfA = obf_class[j].apkName;
				String cur_obfC = obf_class[j].className;
				String cur_obfF = obf_class[j].feature;
				String[] parse = cur_obfA.split("-");
				float thres = 50.0f;
				float result = 0;
				String line_oriF[] = cur_oriF.split("\\n");
				String line_obfF[] = cur_obfF.split("\\n");
				float small_roop = 0.0f;
				float div = 0.0f;
				
				if (line_oriF.length <= line_obfF.length) { small_roop = (float)line_oriF.length;}
				else { small_roop = (float)line_obfF.length; }
				
				for (int k = 0; k < small_roop; k++) {
					float tmp = 0.0f;
					tmp = (float)((100-lcs.distance(line_oriF[k], line_obfF[k]) * 100));
					div += 1.0f;
					result += tmp;
					
				}
				result = result / div;

				if (cur_obfA != "" && cur_oriA != "") {
					check ++;
				if (ori_class[i].className.contains(obf_class[j].className) && cur_oriA.contains(parse[0])) {

					if (result >= thres) {
						tp += 1;			
						
					}
					else if (result < thres) {
						fn += 1;
					}
				}
				else if (!ori_class[i].className.contains(obf_class[j].className) && !cur_oriA.contains(parse[0])) {
					if (result < thres) {
						tn += 1;
					}
					else if (result >= thres) {
						/*
						System.out.println("APK = " + ori_class[i].apkName + " || " + obf_class[j].apkName);
						System.out.println("Class = " + ori_class[i].className + " || " + obf_class[j].className);
						System.out.println(result);
						System.out.println("========================");*/
						cop_count += 1;
						String cur = Integer.toString(cop_count);
						String ori = ori_list[i];
						String cop = obf_list[j];
						data[(int) fp] = cur + "," + ori + "," + cop;
						
						fp += 1;
					}
				}
				result = 0; div = 0;
				}
			}
		}
		System.out.println(check);
		ac = (float)((tp + tn) / (tp + tn + fp + fn));
		pc = (float)(tp  / (tp + fp));
		rc = (float)(tp / (tp + fn));
		float fpr = (float)(fp / (fp + tn));
		float tpr = (float)(tp / (fn + tp));
		float npc = (float)((tp + tn) / (tp + tn + fp));
		

		System.out.println("all compare case : " + all_count);
		System.out.println("Data Set size(original) : " + set_size);
		System.out.println("Pass Set size(original) : " + tp / obf_list.length);
		System.out.println("true positive case : " + tp);
		System.out.println("false negative case : " + fn);
		System.out.println("false positive case : " + fp);
		System.out.println("Accuracy(정확도) : " + ac);
		System.out.println("Precision(정밀도) : " + pc);
		System.out.println("Recall(재현율) : " + rc);
		System.out.println("True negative rate(정밀도 대체) : " + npc);
		System.out.println("F1 score : " + ((2 * pc * rc)/(pc + rc)));
		System.out.println("tpr : " + tpr);
		System.out.println("fpr : " + fpr*100);
		
		createCSV(data, fileName, path, fp);
		
		
	}

}

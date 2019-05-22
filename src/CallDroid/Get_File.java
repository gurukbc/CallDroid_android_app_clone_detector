package CallDroid;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

public class Get_File {
	
	String [] paths;
	String file_path = "D:\\Kbc_OSS_Tool\\apk_nsri";
	String obf_path = "D:\\Kbc_OSS_Tool\\apk_fdroid_Dexguard_decompile";
	int count = 0;
	
	public String[] get_FileList(String p) {
		File f = null;
		int i = 0;
		
		try {
			f = new File(p);
			paths = f.list();
			for(String path:paths) {
				//System.out.println(paths[i] + " : " +path);
				i++;
				count ++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return paths;
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

	public String getSHA1(String str){

		String SHA = ""; 

		try{

			MessageDigest sh = MessageDigest.getInstance("SHA-1"); 

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

	public void test() {
		String [] tests = get_FileList(file_path);
		for(String test:tests) {
			System.out.println(test);
		}
	}
}

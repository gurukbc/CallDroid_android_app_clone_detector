package CallDroid;

public class Compare_Algorithm {
	public float simScore(Node ori, Node obf) {
		float result = 0;
		float ori_set = 0;
		float obf_set = 0;
		float common = 0;
		int flag = 0;
		int cIndex = 0;
		obf_set = obf.IM_size;
		ori_set = ori.IM_size;
		
		
		for (int i = 0; i < ori.feature.length; i++) {
			
			//ArrayList<Integer> cIndex = new ArrayList<>();
			
			for (int j = 0; j < obf.feature.length; j++) {
				if (obf.feature[j][0].equals(ori.feature[i][0])) {
					//cIndex.add(j);
					cIndex = j;
					flag = 1;
				}
			}
			
			if (flag == 1 && cIndex >= 0) {
				for (int j = 1; j < ori.feature[i].length; j++) {
					for (int p = 1; p < obf.feature[cIndex].length; p++) {
						if (ori.feature[i][j].equals(obf.feature[cIndex][p])) {
						//	System.out.println(ori.IM_count[i][j]);
							common = common + ori.IM_count[i][j];
						}
						else {
							int indexR = ori.feature[i][j].length();
							int indexB = obf.feature[cIndex][p].length();
							String tmpOri = ori.feature[i][j].substring(0, indexR - 1);
							String tmpObf = obf.feature[cIndex][p].substring(0, indexB - 1);
							if(tmpOri.equals(tmpObf)) {
								int MAX = 0;
								if(ori.IM_count[i][j] >= obf.IM_count[cIndex][p]) { MAX = (int) ori.IM_count[i][j]; }
								else { MAX = (int) obf.IM_count[cIndex][p]; }
								common = common + MAX;
							}
						}
					}
				}
			}
			flag = 0;
			cIndex = -1;
		}
		
		result = (common / ((ori_set - common) + (obf_set - common) + common));
		/*
		System.out.println(common);
		System.out.println(ori_set);
		System.out.println(obf_set);
		System.out.println(result);
		System.out.println("=================");
		*/
		return result;
	}
}

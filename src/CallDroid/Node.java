package CallDroid;

public class Node {
	String name = "";
	String [][] feature;
	float IC_size = 0;
	float IM_size = 0;
	float [][] IM_count;
	float [] C_count;
	Node() {}
	Node(int size) {
		C_count = new float[size];
		this.IC_size = size;
	}
}

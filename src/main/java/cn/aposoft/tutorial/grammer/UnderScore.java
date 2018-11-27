package cn.aposoft.tutorial.grammer;

public class UnderScore {
	//'_' should not be used as an identifier, 
	// since it is a reserved keyword from source level 1.8 on
	//	static int _;
	static int _1;
	public static void main(String[] args) {
		System.out.println(_1);
	}
}

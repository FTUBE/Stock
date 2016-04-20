
public class Current {
	double JPY;
	double RMB;
	
	public Current(double J, double R){
		JPY = J;
		RMB = R;
	}
	public static void main(String[] args){
		for(double a = 0;a<=1; a = (Math.round((0.2+a)*100)/100.0)){
			System.out.println(a);
		}
	}
}

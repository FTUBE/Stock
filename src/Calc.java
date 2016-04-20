import java.util.ArrayList;
import java.util.Collection;

public class Calc {	
	static boolean thinking = true;
	static double gamma = 0.08;
	static ArrayList<Double> trend = new ArrayList<Double>();
	static ArrayList<Double> finalchoice;
	static ArrayList<Double> anotherchoice;
	static double finalJPY = Double.MIN_VALUE,finalRMB = Double.MIN_VALUE;
	
	public static void main(String[] args) throws InterruptedException{
		Current c = new Current(170000,3000);
		double RMB = 1000.00;
		double JPY = 172044.00;
		double believe = 0.0000;
		double count = 0.0;
		while(true){
			count++;
			ArrayList<Double> toret = predi(16.8580,14);
			toret.add(0, 16.8580);
			believe += deci(toret);
			if(count % 10000 == 0){
				System.out.println("After " + count +" enumerations. Program suggests you ");
				if(believe > 0) System.out.println(" BUY "+believe/count*RMB+" RMB.");
				else System.out.println(" SELL "+(believe/count*JPY)*(-1)+" JPY.");
				Thread.sleep(5000);
			}
		}
	
		
	}
	
	public static ArrayList<Double> predi(double init, int span){
		
		ArrayList<Double> toret = new ArrayList<Double>();
		for(int i = 1;  i <= span; i++){
			double base = 0.0025;//POSITIVE 0.005 proper
			double delta = (Math.round((10-psG(10,0.15))*10000.0))/10000.0;
			double percent = Math.round((base+delta)*10000.0)/10000.0;
			double redu = Math.round((percent/100.0)*1000000.0)/1000000.0;
			init = Math.round((init*(1+redu))*10000.0)/10000.0;
			//System.out.println(init);
			toret.add(init);
		}
		
		return toret;
	}
	public static double deci(ArrayList<Double> tre){
		ArrayList<Double> toret = new ArrayList<Double>();
		double delta = 0;
		for(int i = 0; i < tre.size();i++){
			if(i == tre.size()-1){
				if(delta >= 0){
					toret.add(1.0);
					continue;
				}
				if(delta < 0){
					toret.add(0.0);
					continue;
				}
			}
			double now = tre.get(i), fut = tre.get(i+1);
			if(fut-now < 0 && delta >= 0){
				toret.add(1.0);
				delta = fut-now;
				continue;
			}
			if(fut-now > 0 && delta <= 0){
				toret.add(-1.0);
				delta = fut-now;
				continue;
			}
			toret.add(0.0);
			delta = fut-now;
		}
		if(thinking){
			for(double e: toret) {
				if(e==1.0) System.out.print("BUY ");
				if(e==0.0) System.out.print("HOLD ");
				if(e==-1.0) System.out.print("SELL ");
			}
			System.out.println();
		}
		return toret.get(0);
	}
	
	public static void enumerate(int day, Current c, ArrayList<Double> choice){
		if(day == trend.size()){
			for(double ch : choice){
				//System.out.print(ch + " ");
			}
			//System.out.println("\nRest : " + c.JPY + " RMB : " + c.RMB + "\n=============");
			if(c.JPY > finalJPY){
				finalJPY = c.JPY;
				finalRMB = c.RMB;
				finalchoice = (ArrayList<Double>) choice.clone();
			}
			return;
		}
		for(double ch = -1.0; ch <= 1.0; ch = (Math.round((0.2+ch)*100)/100.0)){
			if(c.JPY <=0 && ch < 0) continue;
			if(c.RMB <=0 && ch > 0) continue;
			choice.add(ch);
			Current newday = operation(c, trend.get(day),ch);
			enumerate(day+1,newday,choice);
			choice.remove(choice.size()-1);
		}
	}
	
	public static Current operation(Current today, double r_buy, double deci){
		
		Current c = null;
		double r_sell = r_buy + gamma;
		if(deci > 0){ // Buy
			double J_after = today.JPY+today.RMB*deci*r_buy;
			double R_after = today.RMB*(1-deci);
			c = new Current(J_after,R_after);
		}
		
		else if(deci < 0){// Sell
			deci = -deci;
			double J_after = today.JPY*(1-deci);
			double R_after = today.RMB+ (today.JPY*deci)/r_sell;
			c = new Current(J_after,R_after);
		}
		else{
			c = new Current(today.JPY,today.RMB);
		}
		return c;
	}
	
	public static double psG(double miu, double sigma2){ 
		  double N = 12; 
		  double x=0,temp=N; 
		  do{ 
		  x=0; 
		  for(int i=0;i <N;i++) 
		    x=x+(Math.random()); 
		  x=(x-temp/2)/(Math.sqrt(temp/12)); 
		  x=miu+x*Math.sqrt(sigma2); 
		  }while(x <=0);          
		  return x; 
	} 
}

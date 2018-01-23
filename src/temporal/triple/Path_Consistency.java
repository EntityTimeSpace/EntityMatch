package temporal.triple;

import java.util.ArrayList;
import java.util.Set;

public class Path_Consistency {
	public boolean PATHCONSISTENCY(ArrayList<mytriple> mt){
		boolean flag=false;
		if(mt.size()==0){
			return true;
		}
		ArrayList<mytriple> Q=complete(mt);
		ArrayList<mytriple> N=Q;
		while(Q.size()!=0){
			mytriple Rab=Q.get(0);
			Q.remove(0);
			if(!ISCONSISTENCY(N,Q,Rab)){
				return false;
			}
		}
		return flag;
	}
	public boolean ISCONSISTENCY(ArrayList<mytriple> N,ArrayList<mytriple> Q,mytriple Rab){
		for(mytriple Sbc:N){
			if(Sbc.obj==Rab.sub){
				my2triple m2t=new my2triple(Rab, Sbc);
				Set<String> TacString=m2t.doCompositionTable();
				//mytriple Tac=mytriple();
				//ADD(N, Q, Tac);
				if(!ISCONSISTENCY(N, Q, Rab)){
					return false;
				}
			}
		}
		return true;
	} 
	
	public ArrayList<mytriple> complete(ArrayList<mytriple> mt){
		ArrayList<mytriple> com=new ArrayList<>();
		for(mytriple m:mt){
			com.add(m);
			com.add(m.getInverse());
		}
		return com;
	}
	public static void ADD(ArrayList<mytriple> N,ArrayList<mytriple> Q,mytriple Tac){
		//if T=* then return;
		for(mytriple n:N){
			//if(n.obj==)
		}
	}
}

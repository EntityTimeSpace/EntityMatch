package triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class mytriple {
	public static String DC = "DC";//"Disconnected";
	public static String EC = "EC";//"ExternallyConnected";
	public static String EQ = "EQ";//"Equal";
	public static String PO = "PO";//"PartiallyOverlapping";
	public static String TPP = "TPP";//"TangentialProperPart";
	public static String TPPi = "TPPi";//"TangentialProperPartInverse";
	public static String NTPP = "NTPP";//"Non-tangentialProperPart";
	public static String NTPPi = "NTPPi";//"Non-tangentialProperPartInverse";
	public static Map<String, String> maps =new HashMap<String, String>()  ; 
	public String sub, pred, obj;
	public boolean type;// 0 right relation; 1 prob relation
	public Set<String> probrela;
	public ArrayList<String> probrelalist;
	public static void initMap(){
		maps.put("DC", "相离");
		maps.put("EC", "外切");
		maps.put("PO", "部分重合");
		maps.put("TPP", "内切");
		maps.put("TPPi", "内切的逆关系");
		maps.put("EQ", "相等");
		maps.put("NTPP", "真包含");
		maps.put("NTPPi", "真包含的逆关系");
	}
	public mytriple(String a, String b, String c) {
		sub = a;
		obj = c;
		pred = b;
		type = false;
		//setProb(b);
	}

	public mytriple(String a, Set<String> prob, String b) {
		sub = a;
		obj = b;
		type = true;
		probrela=prob;
		//setProb(prob);
	}

	public String getSub() {
		return sub;
	}

	public String getObj() {
		return obj;
	}

	public String getPred() {
		return pred;
	}

	public void setPred(String a) {
		pred = a;
	}

	public Set<String> getProb() {
		return probrela;
	}

	public int getProbNum() {
		return probrela.size();
	}
	
	public int getProbListNum() {
		return probrelalist.size();
	}
	
	public void setProbList(ArrayList<String> list) {
		probrelalist=list;
	}

	public ArrayList<String> settolist(Set<String> set) {
		ArrayList<String> ret=new ArrayList<String>();
		if(set==null) {
			ret.add(pred);
			return ret;
		}
		for(String str:set) {
			ret.add(str);
		}
		/*if(set.size()==1) {
			System.out.println("in settolist set size"+ret);
			ret.add(pred);
			System.out.println("in settolist set size"+ret);
		}*/
		return ret;
	}
	
	public String getNumPred(int num) {
		if(getProbNum()==1) {
			return pred;
		}
		return probrelalist.get(num);
	}
	
	public ArrayList<String> getProbList() {
		return probrelalist;
	}
	
	public void setProb(String one) {
		if (!type) {
			type=true;
			probrela = new HashSet<String>();
			probrela.add(one);
		}
	}

	public void setProb(Set<String> one) {
		if (type) {
			type=true;
			probrela = one;
			probrelalist=new ArrayList<String>();
			for (String entry : one) {
				probrelalist.add(entry);
			}
		}
	}
	
	public void setType(boolean a) {
		type = a;
	}

	public boolean getType() {
		return type;
	}

	public void doInverse() {
		if(pred!=null&&probrela==null) {
			String tmp = sub;
			sub = obj;
			obj = tmp;
			tmp = getInverseRelation(pred);
			pred = tmp;
		}
		else if(probrela!=null) {
			doSetInverse();
			setProbList(settolist(probrela));
		}
	}
	
	/*public void doInverse() {
		if(pred!=null) {
			String tmp = sub;
			sub = obj;
			obj = tmp;
			tmp = getInverseRelation(pred);
			pred = tmp;
		}
		else if(probrela!=null) {
			doSetInverse();
		}
	}*/

	public mytriple getInverse() {
		String tmp = getInverseRelation(pred);
		return new mytriple(obj, tmp, sub);
	}

	public boolean doSetInverse() {
		String tmp = sub;
		sub = obj;
		obj = tmp;
		//System.out.println(set+"\t\t\t\t"+probrela);
		if(probrela==null&&pred!=null) {
			//System.out.println("doSetInverse error!");
			setProb(pred);
			//System.exit(0);
		}
		Set<String> p=new HashSet<String>();
		for (Iterator<String> it = probrela.iterator(); it.hasNext();) {
			String str = it.next();
			tmp = getInverseRelation(str);
			it.remove();// probrela.remove(str);
			p.add(tmp);
		}
		probrela=p;
		return true;
	}

	public static String getInverseRelation(String x) {
		if (x.equals(DC)) {
			return DC;
		} else if (x.equals(EC)) {
			return EC;
		} else if (x.equals(PO)) {
			return PO;
		} else if (x.equals(NTPPi)) {
			return NTPP;
		} else if (x.equals(TPP)) {
			return TPPi;
		} else if (x.equals(TPPi)) {
			return TPP;
		} else if (x.equals(NTPP)) {
			return NTPPi;
		} else {
			return EQ;
		}
	}

	public boolean simequals(mytriple mt) {
		return mt.getObj().equals(obj) && mt.getSub().equals(sub);
	}

	public boolean equals(mytriple mt) {
		if (mt.getObj().equals(obj) && mt.getSub().equals(sub)) {
			// System.out.println("in equals =");
			return true;
		} else if (mt.getObj().equals(sub) && mt.getSub().equals(obj)) {
			System.out.println("after dosetinverse");
			doSetInverse();
			System.out.println("after dosetinverse");
			return true;
		} else
			return false;
	}

	public boolean setequals(Set<String> set) {
		/*if(set.size()==probrela.size()&&set.size()==13) {
			System.out.println("\t\t\t==============setequals\t\t");
			System.out.println(set+"\t\t\t"+probrela);
			for (Iterator<String> it1 = set.iterator(); it1.hasNext();) {
				String i1=it1.next();
				if(!probrela.contains(i1))
					return false;
			}
		}*/
		if(set.size()!=probrela.size())
			return false;
		for (Iterator<String> it1 = set.iterator(); it1.hasNext();) {
			String i1=it1.next();
			if(!probrela.contains(i1))
				return false;
		}
		return true;
	}
	
	public boolean strequals(mytriple mt) {
		//System.out.println("after dosetinverse\t"+infoString());
		if (mt.getObj().equals(obj) && mt.getSub().equals(sub)) {
			if(type==false) {
				return true;
			}
		} else if (mt.getObj().equals(sub) && mt.getSub().equals(obj)) {
			if(type==false) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inverse(mytriple mt) {
		return mt.getObj().equals(sub) && mt.getSub().equals(obj);
	}

	public String infoString() {
		String predicate = "";
		String[] strs;
		String a;
		if (!type){
			//a = sub + " " + pred + " " + obj;
			strs = pred.replace("[", "").replace("]","").split(",");
		}
		else{
			//a = sub + " " + probrela + " " + obj;
			strs = probrela.toString().replace("[", "").replace("]","").split(",");
		}
			
		//System.out.println("a========="+a);
		for(String str:strs){
			predicate+=maps.get(str.trim())+" ";
		}
		a = sub + " [ " + predicate + "] " + obj;
		//System.out.println("a========="+a);
		return a;
	}

	public void printinfo() {
		if (!type)
			System.out.println("                    "+sub + pred + obj);
		else
			System.out.println("                    "+sub + probrela + obj);
	}

	@Override
	public String toString() {
		return "mytriple [sub=" + sub + ", pred=" + pred + ", obj=" + obj + "]";
	}
	
}

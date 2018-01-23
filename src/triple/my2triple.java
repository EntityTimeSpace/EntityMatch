package triple;

import java.util.HashSet;
import java.util.Set;

public class my2triple {
	public mytriple tri1;
	public mytriple tri2;
	
	public mytriple gettri1() {
		return tri1;
	}
	
	public mytriple gettri2() {
		return tri2;
	}	

	public Set<String> doCompositionTable() {
		Set<String> al=new HashSet<String>();
		if(tri1.getPred().equals(mytriple.DC)) { //DC mate
			if(tri2.getPred().equals(mytriple.TPPi) || tri2.getPred().equals(mytriple.NTPPi) ||
					tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.DC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EC)||tri2.getPred().equals(mytriple.PO)||
					tri2.getPred().equals(mytriple.TPP)||tri2.getPred().equals(mytriple.NTPP)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.DC)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.NTPP);al.add(mytriple.TPPi);
				al.add(mytriple.NTPPi);al.add(mytriple.EQ);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.EC)) {//EC mate 
			if(tri2.getPred().equals(mytriple.DC)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EC)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.TPPi);al.add(mytriple.EQ);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.PO)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPP)){
				al.add(mytriple.EC);al.add(mytriple.PO);al.add(mytriple.TPP);
				al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPP)) {
				al.add(mytriple.PO);al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPPi)) {
				al.add(mytriple.DC);al.add(mytriple.EC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPPi)) {
				al.add(mytriple.DC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.EC);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.PO)) { //PO mate
			if(tri2.getPred().equals(mytriple.DC) || tri2.getPred().equals(mytriple.EC)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.PO)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.NTPP);al.add(mytriple.TPPi);
				al.add(mytriple.NTPPi);al.add(mytriple.EQ);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPP)||tri2.getPred().equals(mytriple.NTPP)) {
				al.add(mytriple.PO);al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPPi) || tri2.getPred().equals(mytriple.NTPPi)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.PO);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.TPP)) { //TPP mate
			if(tri2.getPred().equals(mytriple.DC)) {
				al.add(mytriple.DC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EC)) {
				al.add(mytriple.DC);al.add(mytriple.EC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.PO)||tri2.getPred().equals(mytriple.TPPi)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPP)) {
				al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPP)) {
				al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPPi)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.TPP);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.NTPP)) { //NTPP mate
			if(tri2.getPred().equals(mytriple.DC) || tri2.getPred().equals(mytriple.EC)) {
				al.add(mytriple.DC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.PO) || tri2.getPred().equals(mytriple.TPPi)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPP) || tri2.getPred().equals(mytriple.NTPP)
					|| tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPPi)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPP);al.add(mytriple.NTPP);al.add(mytriple.TPPi);
				al.add(mytriple.NTPPi);al.add(mytriple.EQ);
				return al;
			}
			
		}
  		else if(tri1.getPred().equals(mytriple.TPPi)) { //TPPi mate
			if(tri2.getPred().equals(mytriple.DC)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EC)) {
				al.add(mytriple.EC);al.add(mytriple.PO);al.add(mytriple.TPPi);
				al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.PO)) {
				al.add(mytriple.PO);al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPP)) {
				al.add(mytriple.EQ);al.add(mytriple.PO);al.add(mytriple.TPPi);
				al.add(mytriple.TPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPP)) {
				al.add(mytriple.PO);al.add(mytriple.TPP);al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPPi)) {
				al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPPi)) {
				al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.TPPi);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.NTPPi)) { //NTPPi mate
			if(tri2.getPred().equals(mytriple.DC)) {
				al.add(mytriple.DC);al.add(mytriple.EC);al.add(mytriple.PO);
				al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EC) || tri2.getPred().equals(mytriple.PO)||
					tri2.getPred().equals(mytriple.TPP)) {
				al.add(mytriple.PO);al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPP)) {
				al.add(mytriple.PO);al.add(mytriple.TPP);al.add(mytriple.NTPP);
				al.add(mytriple.EQ);al.add(mytriple.TPPi);al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPPi) || tri2.getPred().equals(mytriple.NTPPi)||
					tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.NTPPi);
				return al;
			}
			
		}
		else if(tri1.getPred().equals(mytriple.EQ)) { //EQ mate
			if(tri2.getPred().equals(mytriple.DC)) {
				al.add(mytriple.DC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EC)) {
				al.add(mytriple.EC);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.PO)) {
				al.add(mytriple.PO);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPP)) {
				al.add(mytriple.TPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPP)) {
				al.add(mytriple.NTPP);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.TPPi)) {
				al.add(mytriple.TPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.NTPPi)) {
				al.add(mytriple.NTPPi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.EQ)) {
				al.add(mytriple.EQ);
				return al;
			}
		}
		
		return al;
	}

	public my2triple(mytriple x,mytriple y) {
		tri1=x;
		tri2=y;
	}
	
	public boolean to1223() {
		if(tri1.getObj().equals(tri2.getSub())) {
			return true;
		}//1223->1223	1232->1223
		else if(tri1.getObj().equals(tri2.getObj())){
			tri2.doInverse();
			return true;
		}//1231->2113	1213->2113
		else if(tri1.getSub().equals(tri2.getObj())){
			tri1.doInverse();
			tri2.doInverse();
			return true;
		}
		else if(tri1.getSub().equals(tri2.getSub())){
			tri1.doInverse();
			return true;
		}//1234->null
		else {
			return false;
		}
	}

	public void printinfo() {
		System.out.println(tri1.infoString()+"\t"+tri2.infoString());
	}
	
	public String infoString() {
		return tri1.infoString()+"\t"+tri2.infoString();
	}
	
}

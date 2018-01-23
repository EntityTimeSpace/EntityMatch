package temporal.triple;

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
		if(tri1.getPred().equals(mytriple.B)) {
			if(tri2.getPred().equals(mytriple.B) || tri2.getPred().equals(mytriple.M) ||
					tri2.getPred().equals(mytriple.O) || tri2.getPred().equals(mytriple.S) ||
					tri2.getPred().equals(mytriple.Si) || tri2.getPred().equals(mytriple.Di) ||
					tri2.getPred().equals(mytriple.Fi) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.B);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A)) {
				al.add(mytriple.B);al.add(mytriple.A);al.add(mytriple.M);
				al.add(mytriple.Mi);al.add(mytriple.O);al.add(mytriple.Oi);
				al.add(mytriple.S);al.add(mytriple.Si);al.add(mytriple.D);
				al.add(mytriple.Di);al.add(mytriple.F);al.add(mytriple.Fi);
				al.add(mytriple.Eq);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Mi) || tri2.getPred().equals(mytriple.Oi) ||
					tri2.getPred().equals(mytriple.D) || tri2.getPred().equals(mytriple.F)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.S);al.add(mytriple.D);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.A)) {
			if(tri2.getPred().equals(mytriple.A) || tri2.getPred().equals(mytriple.Mi) ||
					tri2.getPred().equals(mytriple.Oi) || tri2.getPred().equals(mytriple.Si) ||
					tri2.getPred().equals(mytriple.Di) || tri2.getPred().equals(mytriple.F) ||
					tri2.getPred().equals(mytriple.Fi) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.A);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.B)) {
				al.add(mytriple.B);al.add(mytriple.A);al.add(mytriple.M);
				al.add(mytriple.Mi);al.add(mytriple.O);al.add(mytriple.Oi);
				al.add(mytriple.S);al.add(mytriple.Si);al.add(mytriple.D);
				al.add(mytriple.Di);al.add(mytriple.F);al.add(mytriple.Fi);
				al.add(mytriple.Eq);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.M) || tri2.getPred().equals(mytriple.O) ||
					tri2.getPred().equals(mytriple.S) || tri2.getPred().equals(mytriple.D)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.F);al.add(mytriple.D);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.M)) {
			if(tri2.getPred().equals(mytriple.B) || tri2.getPred().equals(mytriple.M) ||
					tri2.getPred().equals(mytriple.O) || tri2.getPred().equals(mytriple.Di) ||
					tri2.getPred().equals(mytriple.Fi)) {
				al.add(mytriple.B);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.S) || tri2.getPred().equals(mytriple.Si) ||
					tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.M);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Oi) || tri2.getPred().equals(mytriple.D) ||
					tri2.getPred().equals(mytriple.F)) {
				al.add(mytriple.O);al.add(mytriple.S);al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Mi)) {
				al.add(mytriple.F);al.add(mytriple.Fi);al.add(mytriple.Eq);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.Mi)) {
			if(tri2.getPred().equals(mytriple.A) || tri2.getPred().equals(mytriple.Mi) ||
					tri2.getPred().equals(mytriple.Oi) || tri2.getPred().equals(mytriple.Si) ||
					tri2.getPred().equals(mytriple.Di)) {
				al.add(mytriple.A);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.F) || tri2.getPred().equals(mytriple.Fi) ||
					tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.Mi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.B)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.O) || tri2.getPred().equals(mytriple.S) ||
					tri2.getPred().equals(mytriple.D)) {
				al.add(mytriple.Oi);al.add(mytriple.F);al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.M)) {
				al.add(mytriple.S);al.add(mytriple.Si);al.add(mytriple.Eq);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.O)) {
			if(tri2.getPred().equals(mytriple.B) || tri2.getPred().equals(mytriple.M)) {
				al.add(mytriple.B);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.S) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.O);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.D) || tri2.getPred().equals(mytriple.F)) {
				al.add(mytriple.S);al.add(mytriple.D);al.add(mytriple.O);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.O) || tri2.getPred().equals(mytriple.Fi)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Mi)) {
				al.add(mytriple.Oi);al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Oi)) {
				al.add(mytriple.O);al.add(mytriple.Oi);al.add(mytriple.S);
				al.add(mytriple.Si);al.add(mytriple.D);al.add(mytriple.Di);
				al.add(mytriple.F);al.add(mytriple.Fi);al.add(mytriple.Eq);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Si)) {
				al.add(mytriple.O);al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Di)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.Oi)) {
			if(tri2.getPred().equals(mytriple.A) || tri2.getPred().equals(mytriple.Mi)) {
				al.add(mytriple.A);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.F) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.Oi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.S) || tri2.getPred().equals(mytriple.D)) {
				al.add(mytriple.Oi);al.add(mytriple.D);al.add(mytriple.F);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Oi) || tri2.getPred().equals(mytriple.Si)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.B)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.Fi);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.M)) {
				al.add(mytriple.O);al.add(mytriple.Fi);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.O)) {
				al.add(mytriple.O);al.add(mytriple.Oi);al.add(mytriple.S);
				al.add(mytriple.Si);al.add(mytriple.D);al.add(mytriple.Di);
				al.add(mytriple.F);al.add(mytriple.Fi);al.add(mytriple.Eq);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Fi)) {
				al.add(mytriple.Oi);al.add(mytriple.Di);al.add(mytriple.Si);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Di)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.S)) {
			if(tri2.getPred().equals(mytriple.B) || tri2.getPred().equals(mytriple.M)) {
				al.add(mytriple.B);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.S) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.S);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.D) || tri2.getPred().equals(mytriple.F)) {
				al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.O) || tri2.getPred().equals(mytriple.Fi)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A)) {
				al.add(mytriple.A);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Mi)) {
				al.add(mytriple.Mi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Oi)) {
				al.add(mytriple.Oi);al.add(mytriple.D);al.add(mytriple.F);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Si)) {
				al.add(mytriple.S);al.add(mytriple.Si);al.add(mytriple.Eq);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Di)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.Si)) {
			if(tri2.getPred().equals(mytriple.Oi) || tri2.getPred().equals(mytriple.F)) {
				al.add(mytriple.Oi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Si) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.Si);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Di) || tri2.getPred().equals(mytriple.Fi)) {
				al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.M) || tri2.getPred().equals(mytriple.O)) {
				al.add(mytriple.O);al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.B)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A)) {
				al.add(mytriple.A);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Mi)) {
				al.add(mytriple.Mi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.S)) {
				al.add(mytriple.S);al.add(mytriple.Si);al.add(mytriple.Eq);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.D)) {
				al.add(mytriple.Oi);al.add(mytriple.D);al.add(mytriple.F);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.D)) {
			if(tri2.getPred().equals(mytriple.B) || tri2.getPred().equals(mytriple.M)) {
				al.add(mytriple.B);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A) || tri2.getPred().equals(mytriple.Mi)) {
				al.add(mytriple.A);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.S) || tri2.getPred().equals(mytriple.D) ||
					tri2.getPred().equals(mytriple.F) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.O) || tri2.getPred().equals(mytriple.Fi)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.S);al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Oi) || tri2.getPred().equals(mytriple.Si)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.D);al.add(mytriple.F);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Di)) {
				al.add(mytriple.B);al.add(mytriple.A);al.add(mytriple.M);
				al.add(mytriple.Mi);al.add(mytriple.O);al.add(mytriple.Oi);
				al.add(mytriple.S);al.add(mytriple.Si);al.add(mytriple.D);
				al.add(mytriple.Di);al.add(mytriple.F);al.add(mytriple.Fi);
				al.add(mytriple.Eq);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.Di)) {
			if(tri2.getPred().equals(mytriple.Si) || tri2.getPred().equals(mytriple.Di) ||
					tri2.getPred().equals(mytriple.Fi) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.M) || tri2.getPred().equals(mytriple.O) ||
					tri2.getPred().equals(mytriple.S)) {
				al.add(mytriple.O);al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Mi) || tri2.getPred().equals(mytriple.Oi) ||
					tri2.getPred().equals(mytriple.F)) {
				al.add(mytriple.Oi);al.add(mytriple.Di);al.add(mytriple.Si);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.B)) {
				al.add(mytriple.B);al.add(mytriple.M);al.add(mytriple.O);
				al.add(mytriple.Di);al.add(mytriple.Fi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.D)) {
				al.add(mytriple.O);al.add(mytriple.Oi);al.add(mytriple.S);
				al.add(mytriple.Si);al.add(mytriple.D);al.add(mytriple.Di);
				al.add(mytriple.F);al.add(mytriple.Fi);al.add(mytriple.Eq);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.F)) {
			if(tri2.getPred().equals(mytriple.A) || tri2.getPred().equals(mytriple.Mi)) {
				al.add(mytriple.A);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.S) || tri2.getPred().equals(mytriple.D)) {
				al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.F) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.F);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Oi) || tri2.getPred().equals(mytriple.Si)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.B)) {
				al.add(mytriple.B);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.M)) {
				al.add(mytriple.M);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.O)) {
				al.add(mytriple.O);al.add(mytriple.S);al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Di)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Fi)) {
				al.add(mytriple.F);al.add(mytriple.Fi);al.add(mytriple.Eq);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.Fi)) {
			if(tri2.getPred().equals(mytriple.O) || tri2.getPred().equals(mytriple.S)) {
				al.add(mytriple.O);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Fi) || tri2.getPred().equals(mytriple.Eq)) {
				al.add(mytriple.Fi);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Mi) || tri2.getPred().equals(mytriple.Oi)) {
				al.add(mytriple.Oi);al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.B)) {
				al.add(mytriple.B);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.A)) {
				al.add(mytriple.A);al.add(mytriple.Mi);al.add(mytriple.Oi);
				al.add(mytriple.Si);al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.M)) {
				al.add(mytriple.M);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Si)) {
				al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.D)) {
				al.add(mytriple.O);al.add(mytriple.S);al.add(mytriple.D);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.Di)) {
				al.add(mytriple.Di);
				return al;
			}
			else if(tri2.getPred().equals(mytriple.F)) {
				al.add(mytriple.F);al.add(mytriple.Fi);al.add(mytriple.Eq);
				return al;
			}
		}
		else if(tri1.getPred().equals(mytriple.Eq)) {
			al.add(tri2.getPred());
			return  al;
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

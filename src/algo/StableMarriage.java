package algo;

import java.util.LinkedList;
import java.util.List;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.tint.impl.DenseIntMatrix1D;
import cern.colt.matrix.tint.impl.DenseIntMatrix2D;
import data.EntityPair;
import util.ArraySort;

public class StableMarriage {
	// Number of source1 
    private int n1;
    // Number of source2
    private int n2;
    public DoubleMatrix2D sim;
    public static List<EntityPair> pairs;

    // source1 Preference tables (size n1xn2)
    DenseIntMatrix2D Pref1;
    // source2 Preference tables (size n2xn1)
    DenseIntMatrix2D Pref2;
    
    public static void init(List<EntityPair> ep) {
    	pairs = ep;
    }
    public static void main(String[] args) {
    	if (args.length != 2) {
    	    System.out.println("Usage: java StableMarriage n");
    	    return;
    	}
    }
    public StableMarriage(DoubleMatrix2D sim,int n1,int n2) {
    	this.n1 = n1;
    	this.n2 = n2;
    	Pref1 = new DenseIntMatrix2D(n1,n2);
    	Pref2 = new DenseIntMatrix2D(n2,n1);
    	for(int i=0;i<n1;i++) {
    		ArraySort.sort(sim.viewRow(i), Pref1.viewRow(i), n2);
    	}
    	for(int i=0;i<n2;i++) {
    		ArraySort.sort(sim.viewColumn(i), Pref2.viewRow(i), n1);
    	}    	
    }
    public StableMarriage(DoubleMatrix1D pref,int start,int count,int n1,int n2,int m1,int m2) {
    	this.n1 = n1;
    	this.n2 = n2;
    	sim = new DenseDoubleMatrix2D(n1,n2);
    	int k = start;
    	Pref1 = new DenseIntMatrix2D(n1,n2);
    	Pref2 = new DenseIntMatrix2D(n2,n1);
    	for(int s = start; s < start+count; s++) {
    		int i = pairs.get(s).getEntity1() - m1;
    		int j = pairs.get(s).getEntity2() - m2;
    		sim.setQuick(i, j, pref.getQuick(k++));
    	}
    	for(int i=0;i<n1;i++) {
    		ArraySort.sort(sim.viewRow(i), Pref1.viewRow(i), n2);
    	}
    	for(int i=0;i<n2;i++) {
    		ArraySort.sort(sim.viewColumn(i), Pref2.viewRow(i), n1);
    	}    	
    }
    
    public DenseIntMatrix1D stable() {
		// Indicates that source2 i is currently matched to
		// the source1 v[i].
	    DenseIntMatrix1D current = new DenseIntMatrix1D(n2);
		final int NOT_ENGAGED = -1;
		for (int i = 0; i < current.size(); i++)
		    current.setQuick(i, NOT_ENGAGED);
		// List of source1 that are not currently matched.
		LinkedList<Integer> freeEntity = new LinkedList<Integer>();
		for (int i = 0; i < n1; i++)
		    freeEntity.add(i);
	
		// next[i] is the next entity to whom source1 i has not yet proposed.
		int[] next = new int[n1];
		while (!freeEntity.isEmpty()) {
		    int k = freeEntity.remove();
		    int w = 0;
		    if(next[k] < n2)
		    	w = Pref1.getQuick(k, next[k]);
		    else
		    	break;
		    next[k]++;
		    if (current.getQuick(w) == NOT_ENGAGED) {
		    	current.setQuick(w, k);
		    } else {
			int k1 = current.getQuick(w);
			if (prefers(w, k, k1)) {
			    current.setQuick(w, k);
			    freeEntity.add(k1);
			} else {
			    freeEntity.add(k);
			}
		    }	    
		}
		return current;	
    }

    /**
     * Returns true iff w prefers x to y.
     */
    private boolean prefers(int w, int x, int y) {
		for (int i = 0; i < n1; i++) {
		    int pref = Pref2.getQuick(w, i);
		    if (pref == x)
			return true;
		    if (pref == y)
			return false;	    
		}
		// This should never happen.
		System.out.println("Error in source2Pref list " + w);
		return false;
    }

    public void printPrefTables() {
    	System.out.println("source1Pref:");
    	printMatrix(Pref1);
    	System.out.println("source2Pref:");
    	printMatrix(Pref2);
    }

    public void printMarriage(DenseIntMatrix1D marriage) {
		System.out.println("Matched couples (source2 + source1): ");
		for (int i = 0; i < marriage.size(); i++)
		    System.out.println(i + " + " + marriage.getQuick(i));
	    }

    private void printMatrix(DenseIntMatrix2D manPref2) {
		if (manPref2 == null) {
		    System.out.println("<null>");
		    return;
		}
		for (int i = 0; i < manPref2.rows(); i++) {
		    for (int j = 0; j < manPref2.columns(); j++)
			System.out.print(manPref2.getQuick(i, j) + " ");
		    System.out.println();
		}
    }
}


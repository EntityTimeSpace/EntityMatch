package match;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.google.gson.Gson;

import algo.ComputeWeight;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import data.Mapping;
import data.ReadOntology;
import data.ReadRDF;
import data.Alignment;
import util.CompareResult;
import util.XMLHelper;

public class NormalMatch {
	 private List<Mapping> result = new ArrayList<Mapping>();
	 private List<Alignment> ref = new ArrayList<Alignment>();
	 public String match(String file1,String file2) throws Exception{
		//  ReadRDF ont1 = new ReadRDF(file1);
	     // ReadRDF ont2 = new ReadRDF(file2);
	      ReadOntology ont1 = new ReadOntology(file1);
	      ReadOntology ont2 = new ReadOntology(file2);
	      int rdf1InstanceSize = ont1.instances.size();
	      int rdf2InstanceSize = ont2.instances.size();
	      System.out.println("第一个文件中包含实体数量�?" + rdf1InstanceSize);
	      System.out.println("第二个文件中包含实体数量�?" + rdf2InstanceSize);
	      //计算实例间的相似�?
	      ComputeWeight cm = new ComputeWeight(ont1.instances,ont2.instances);
		  double[][] w = cm.compute();
		//  DataToFile.writeToFile(w, "result/matrix.txt", false);
	      DoubleMatrix2D x = new DenseDoubleMatrix2D(w);
	      for(int i = 0; i < rdf1InstanceSize; i++) {
	    	  DoubleMatrix1D temp = x.viewRow(i).viewPart(0, rdf2InstanceSize);
	    	  double[] rs = temp.getMaxLocation();
	    	  if(rs[0] >= 0.8) {
	    		  result.add(new Mapping(ont1.instances.get(i),ont2.instances.get((int)rs[1]),rs[0]));
	    		  ref.add(new Alignment(ont1.instances.get(i).getSubject(),ont2.instances.get((int)rs[1]).getSubject(),
	    				  "=","http://www.w3.org/2001/XMLSchema#float","1.0"));
	    	  }
	      }
	      System.out.println("匹配对数�?:" + result.size());
	      Gson gson = new Gson();
	      String s = gson.toJson(result);
	      return s;
	 }
	 public String eval(String file) throws DocumentException {
		 XMLHelper xmlHelper = new XMLHelper(file);
		 List<Alignment> reals = xmlHelper.run();
		 CompareResult compare = new CompareResult();
		 compare.compareResult(reals,ref);
		 Gson gson = new Gson();
	     String s = gson.toJson(compare);
		 return s;
	 }
}

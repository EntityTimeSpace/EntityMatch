package match;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.google.gson.Gson;

import algo.ComputeMatrix;
import algo.ComputePairMatrix;
import algo.RandomWalk;
import algo.StableMarriage;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tint.impl.DenseIntMatrix1D;
import data.Mapping;
import data.ReadOntology;
import data.Alignment;
import util.CompareResult;
import util.XMLHelper;

public class RWMatch {
	 private List<Mapping> result = new ArrayList<Mapping>();
	 private List<Alignment> ref = new ArrayList<Alignment>();
	public String match(String file1,String file2) throws Exception {
		ReadOntology ont1 = new ReadOntology(file1);
		ReadOntology ont2 = new ReadOntology(file2);
		int owl1ClassesSize = ont1.classes.size();
	    int owl2ClassesSize = ont2.classes.size();
	    int owl1PropertiesSize = ont1.properties.size();
	    int owl2PropertiesSize = ont2.properties.size();
	    int rdf1InstanceSize = ont1.instances.size();
	    int rdf2InstanceSize = ont2.instances.size();
	    ComputeMatrix cw = new ComputeMatrix();
	    int classPairsCount = cw.computeClasses(ont1.classes, ont2.classes,0.95);
	    int propertyPairsCount = cw.computeProperties(ont1.properties, ont2.properties,0.95);
	 //   int instancePairsCount = cw.computeInstances(ont1.instances, ont2.instances,0.70);
	    int instancePairsCount = cw.computeInstances(ont1.instances, ont2.instances,0.50);
	    System.out.println("相似度阈值过滤后的匹配对数：" + instancePairsCount);
	    StableMarriage.init(cw.es);
	    //根据实体对间的关系和相似度计算传递矩阵W
	    ComputePairMatrix cm = new ComputePairMatrix(ont1.classes,ont2.classes,ont1.properties,ont2.properties,ont1.instances,ont2.instances,cw.es);
	    //使用随机游走模型计算得到匹配结果（每个匹配对的相似度值）
	    double[][] w = cm.compute();
	    RandomWalk rw = new RandomWalk(cw.es,owl1ClassesSize,owl2ClassesSize,owl1PropertiesSize,owl2PropertiesSize,rdf1InstanceSize,
	    		   rdf2InstanceSize,classPairsCount,propertyPairsCount,instancePairsCount);
	    DoubleMatrix1D es = rw.randomWalk(w,30,100,0.85);
	    int index = 0;
	    double sim = 0.0;
	    StableMarriage csm = new StableMarriage(es,0,classPairsCount,owl1ClassesSize,owl2ClassesSize,0,0);
	    DenseIntMatrix1D cmarriage = csm.stable();
	    for (int i = 0; i < cmarriage.size(); i++) {
	    	sim = 0.0;
	    	int m = cmarriage.getQuick(i);
	    	String s = m + "-" + i;
	    	if(rw.epMap.get(s) != null) {
	    		index = rw.epMap.get(s).intValue();
	    		sim = cw.es.get(index).getSim();
	    	}
	    	if(m != -1) {
	    		//	results.add(new Result(owl1.classes.get(m).getSubject(),owl2.classes.get(i).getSubject(),"=","http://www.w3.org/2001/XMLSchema#float","1.0"));
	    //		System.out.println(ont1.classes.get(m).getSubject() + "--" + ont2.classes.get(i).getSubject()+"--"+(sim+1)/2);
	    	}
	    }
	    StableMarriage psm = new StableMarriage(es,classPairsCount,propertyPairsCount,owl1PropertiesSize,owl2PropertiesSize,owl1ClassesSize,owl2ClassesSize);
	    DenseIntMatrix1D pmarriage = psm.stable();
	    for (int i = 0; i < pmarriage.size(); i++) {
	    	sim = 0.0;
	    	int m = pmarriage.getQuick(i);
	    	String s = (m+owl1ClassesSize) + "-" + (i+owl2ClassesSize);
	    	if(rw.epMap.get(s) != null) {
	    		index = rw.epMap.get(s).intValue();
	    		sim = cw.es.get(index).getSim();
	    	}
	    	if(m != -1) {
	    		//	results.add(new Result(owl1.properties.get(m).getSubject(),owl2.properties.get(i).getSubject(),"=","http://www.w3.org/2001/XMLSchema#float","1.0"));
	    	//	System.out.println(ont1.properties.get(m).getSubject()+ "--" + ont2.properties.get(i).getSubject()+"--"+(sim+1)/2);
	    	}
	    }
	    StableMarriage ism = new StableMarriage(es,classPairsCount+propertyPairsCount,instancePairsCount,rdf1InstanceSize,rdf2InstanceSize,owl1ClassesSize+owl1PropertiesSize,owl2ClassesSize+owl2PropertiesSize);
	    DenseIntMatrix1D imarriage = ism.stable();
	    for (int i = 0; i < imarriage.size(); i++) {
	    	sim = 0.0;
	    	int m = imarriage.getQuick(i);
	    	String s = (m+owl1ClassesSize+owl1PropertiesSize) + "-" + (i+owl2ClassesSize+owl2PropertiesSize);
	    	if(rw.epMap.get(s) != null) {
	    		index = rw.epMap.get(s).intValue();
	    		sim = cw.es.get(index).getSim();
	    	}
		    if(m != -1) {
		    	sim = (sim+1)/2;
		    //	if(sim >= 0.80) {
		    	if(sim >= 0.60) {
		    		ref.add(new Alignment(ont1.instances.get(m).getSubject(),ont2.instances.get(i).getSubject(),
		        	  "=","http://www.w3.org/2001/XMLSchema#float","1.0"));
	    		result.add(new Mapping(ont1.instances.get(m),ont2.instances.get(i),(sim+1)/2));
		    	}
		    }
	    }
	    System.out.println("匹配对数量:" + result.size());
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


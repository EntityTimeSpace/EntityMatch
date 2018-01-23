package algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.tint.impl.DenseIntMatrix1D;
import data.EntityPair;

public class RandomWalk {
	public Map<String,Integer> epMap;
	public int owl1ClassesCount;
	public int owl2ClassesCount;
	public int owl1PropertiesCount;
	public int owl2PropertiesCount;
	public int rdf1InstancesCount;
	public int rdf2InstancesCount;
	public int classPairsCount;
	public int propertyPairsCount;
	public int instancePairsCount;
	
	public RandomWalk(List<EntityPair> ep,int owl1ClassesCount, int owl2ClassesCount, int owl1PropertiesCount,
			int owl2PropertiesCount, int rdf1InstancesCount, int rdf2InstancesCount,int classPairsCount,
			int propertyPairsCount, int instancePairsCount) {
		epMap = new HashMap<String,Integer>();
		for(int i = 0; i < ep.size();i++) {
			int a = ep.get(i).getEntity1();
			int b = ep.get(i).getEntity2();
			epMap.put(a+"-"+b,i);
		}
		this.owl1ClassesCount = owl1ClassesCount;
		this.owl2ClassesCount = owl2ClassesCount;
		this.owl1PropertiesCount = owl1PropertiesCount;
		this.owl2PropertiesCount = owl2PropertiesCount;
		this.rdf1InstancesCount = rdf1InstancesCount;
		this.rdf2InstancesCount = rdf2InstancesCount;
		this.classPairsCount = classPairsCount;
		this.propertyPairsCount = propertyPairsCount;
		this.instancePairsCount = instancePairsCount;
	}
	public static void main(String[] args) {
		double[][] d = { {0.1,0.7,0.3,0.8,0},
						 {0.7,0.4,0.9,0.5,0},
						 {0.3,0.9,0.5,0.2,0},
						 {0.8,0.5,0.2,0.6,0},
						 {0,0,0,0,0},
		};
		DoubleMatrix1D w = new DenseDoubleMatrix1D(d[2]);
		DenseDoubleAlgebra alg = new DenseDoubleAlgebra();
		System.out.println(alg.norm2(w));
		System.out.println(alg.normF(w));
	//	randomWalk(4,d);
	}
	public static boolean coverge(DoubleMatrix1D x,DoubleMatrix1D y,int m) {
		double a = 0.0;
		for(int i = 0; i < m; i++) {
			a += Math.abs(y.getQuick(i) - x.getQuick(i));
		}
		if(a > 1e-20)
			return false;
		else
			return true;		
	}
	public DoubleMatrix1D randomWalk(double[][] d,int B,int M,double omg) {
		int m = epMap.size();
		int m1 = owl1ClassesCount+owl1PropertiesCount;
		int m2 = owl2ClassesCount+owl2PropertiesCount;
		//将转移概率的二维数组d转成矩阵w
		DoubleMatrix2D w = new DenseDoubleMatrix2D(d);
		DenseDoubleAlgebra alg = new DenseDoubleAlgebra();
		//获取d_max= max_di
		double d_max = alg.norm1(w);
		//Norm.One 表示取最大的列和， Norm.Infinity表示取最大的行和
		//double s = alg.norm(w,Norm.Infinity);
		//根据矩阵w得到矩阵p，执行P=W/d_max
		for(int i = 0; i < m; i++)
			w.setQuick(i, m, d_max - alg.norm1(w.viewRow(i)));
		w.setQuick(m, m, d_max);
		int rows = w.rows();
		int columns = w.columns();
		for(int i=0; i < rows; i++)
			for(int j=0; j < columns; j++)
				w.setQuick(i, j, w.getQuick(i, j)/d_max);
		//对归一化后的矩阵W进行转置（因为目前W是对称的，所以无需转置，不对，因为增加了absorb节点，所以矩阵并不是对称的，必须转置）
		DoubleMatrix2D p = alg.transpose(w);
		//初始化概率向量x_n
		DoubleMatrix1D x = new DenseDoubleMatrix1D(m+1);
		x.assign(1.0/m);
		x.setQuick(m, 1.0);
		DoubleMatrix1D x_1 = new DenseDoubleMatrix1D(m+1);
		x_1.assign(x);
		DoubleMatrix1D y = new DenseDoubleMatrix1D(m+1);
		y.assign(x);
		int j = 0;
		for(; j < M; j++) {
			if(j < B) {
				y.assign(x);
				x_1.assign(alg.mult(p, y));
			/*	if(j == 25) {
					System.out.print("迭代" + j +"次之后：");
					System.out.println("x_1:" + x_1);
					StableMarriage sm = new StableMarriage(x_1,0,classPairsCount,owl1ClassesCount,owl2ClassesCount,0,0);
					DenseIntMatrix1D marriage = sm.stable();
					sm.printMarriage(marriage);
					StableMarriage psm = new StableMarriage(x_1,classPairsCount,propertyPairsCount,owl1PropertiesCount,owl2PropertiesCount,owl1ClassesCount,owl2ClassesCount);
					DenseIntMatrix1D pmarriage = psm.stable();	
					psm.printMarriage(pmarriage);
				}*/
			}
			else {
				//获取向量x'的最大值
				double[] maxxX = x_1.getMaxLocation();
				double max_xX = maxxX[0];
				
				DoubleMatrix1D r = new DenseDoubleMatrix1D(m+1);
				r.assign(x_1);
				StableMarriage csm = new StableMarriage(r,0,classPairsCount,owl1ClassesCount,owl2ClassesCount,0,0);
				DenseIntMatrix1D cmarriage = csm.stable();
				for(int i = 0; i < cmarriage.size();i++) {
					int in = cmarriage.getQuick(i);
					if((epMap.get(in+"-"+i))!= null) {
						int index = epMap.get(in+"-"+i);
						r.setQuick(index, Math.exp(r.getQuick(index)*max_xX));
					}
				}
				StableMarriage psm = new StableMarriage(r,classPairsCount,propertyPairsCount,owl1PropertiesCount,owl2PropertiesCount,owl1ClassesCount,owl2ClassesCount);
				DenseIntMatrix1D pmarriage = psm.stable();	
				for(int i = 0; i < pmarriage.size();i++) {
					int in = pmarriage.get(i);
					if((epMap.get((in+owl1ClassesCount)+"-"+(i+owl2ClassesCount)))!= null) {
						int index = epMap.get((in+owl1ClassesCount)+"-"+(i+owl2ClassesCount));
						r.setQuick(index, Math.exp(r.getQuick(index)*max_xX));
					}
				}
				StableMarriage ism = new StableMarriage(r,classPairsCount+propertyPairsCount,instancePairsCount,rdf1InstancesCount,rdf2InstancesCount,m1,m2);
				DenseIntMatrix1D imarriage = ism.stable();	
				for(int i = 0; i < imarriage.size();i++) {
					int in = imarriage.get(i);
					if((epMap.get((in+m1)+"-"+(i+m2)))!= null) {
						int index = epMap.get((in+m1)+"-"+(i+m2));
						r.setQuick(index, Math.exp(r.getQuick(index)*max_xX));
					}
				}
				for(int i=0; i < r.size(); i++)
					r.setQuick(i, r.getQuick(i)/r.zSum());
				
				//更新x
				//******修改过，不知是否有误
				y.assign(alg.mult(p, x));
				for(int i = 0; i <= m; i++) {
					x_1.setQuick(i, omg*y.getQuick(i)+(1-omg)*r.getQuick(i));
				} 
			/*	if(j == 30 || j == 31) {
					System.out.print("迭代" + j +"次之后：");
					System.out.println("x_1:" + x_1);
					csm.printMarriage(cmarriage);
					psm.printMarriage(pmarriage);
					ism.printMarriage(imarriage);
				}*/
			}
			for(int i=0; i<= m; i++)
				x_1.setQuick(i, x_1.getQuick(i)/x_1.zSum());
			if(coverge(x_1,x,m)){
				System.out.println("收敛迭代次数：" + j);
				return x_1;
			}
			x.assign(x_1);
		}
		System.out.print("迭代" + M + "次之后才收敛");
		System.out.println("加油，一定可以实现的");
		return x_1;
	}
}

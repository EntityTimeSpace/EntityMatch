package algo;

import java.util.List;
import java.util.Map;

import com.wcohen.ss.TFIDF;

import data.Entity;
/*
 * 计算实例与实例之间的相似度，得到n1*n2的实例相似度矩阵,用于normal匹配和sm匹配
 * 用于PR数据集，在实例的类别上，源数据集与目的数据集的uri不同
 */
public class ComputeWeight {
	public List<Entity> owl1instances;
	public int owl1instanceSize;
	public List<Entity> owl2instances;
	public int owl2instanceSize;
	
	public ComputeWeight(List<Entity> owl1instances,List<Entity> owl2instances) {
		super();
		this.owl1instances = owl1instances;
		this.owl2instances = owl2instances;
		this.owl1instanceSize = this.owl1instances.size();
		this.owl2instanceSize = this.owl2instances.size();
	}
	private double getSim(Map<String, List<String>> predList1,Map<String, List<String>> predList2) {
		int count1 = predList1.size();
		int count2 = predList2.size();
		int count = (count1>count2)?count1:count2;
		double sim = 0.0;
		for(Map.Entry<String,List<String>> pred:predList1.entrySet()) {
			String pred1 = pred.getKey();
			/*原本是统计相同属性个数的，但是存在这样的情况，当某两个实例很相似，相同属性个数也较多，但属性相似度低的会拉低相似度高的，
			 * 如另一个实例与该实例只有一个相同属性，比如说类，那么相似度值就会很高，这实际上是不对的。
			*/
			count++;
			List<String> obj2;
			if((obj2 = predList2.get(pred1)) != null){
				List<String> obj1 = predList1.get(pred1);
			//	count++;
				double maxSim = 0;
				for (int i = 0; i < obj1.size(); ++i) {
					String o1 = obj1.get(i);
					for (int j = 0; j < obj2.size(); ++j) {
						String o2 = obj2.get(j);						
						double s = new TFIDF().score(o1, o2);
						if (s > maxSim)
							maxSim = s;
					}
				}
				sim += maxSim;
			}
		}
		if(count > 0)
			sim = sim/count;
		return sim;
	}
	public double[][] compute() {
		double[][] w = new double[this.owl1instanceSize][this.owl2instanceSize];
		int i = 0, j = 0;
		for(Entity i1:owl1instances) {
			j = 0;
			Map<String, List<String>> predList1 = i1.getPredicate_object();
			List<String> type1 = predList1.get("type");
			for(Entity i2:owl2instances) {
				Map<String, List<String>> predList2 = i2.getPredicate_object();
				List<String> type2 = predList2.get("type");
				boolean flag = false;
				if(type1 != null) {
					for(String t1:type1) {
						String t11 = t1.substring(t1.lastIndexOf("#")+1);
						if(type2 != null) {
							for(String t2:type2) {
								String t22 = t2.substring(t2.lastIndexOf("#")+1);
								if(t22.equals(t11))
									flag = true;
							}
						}

					}
				}
				if(flag)
					w[i][j] = getSim(predList1,predList2);
				j++;
			}
			i++;
		}
		return w;
	}	
}

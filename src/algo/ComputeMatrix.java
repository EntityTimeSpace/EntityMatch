package algo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wcohen.ss.Jaro;
import com.wcohen.ss.TFIDF;

import data.Entity;
import data.EntityPair;
//计算得到本体中所有类匹配对，属性匹配对，实例匹配对的相似度，用于RWR匹配的前期准备
public class ComputeMatrix {
	private int i;
	private int j;
	private int k;
	public List<EntityPair> es = new ArrayList<EntityPair>();
	
	public ComputeMatrix() {
		i = 0;
		j = 0;
		k = 0;
	}
	
	//使用class 的label计算相似度
	public int computeClasses(List<Entity> entities1 , List<Entity> entities2,double simThreshold) throws IOException {
		int count = 0;
		for(Entity entity1:entities1) {
			j = 0;
			String s1 = entity1.getSubject();
			for(Entity entity2:entities2) {
				String s2 = entity2.getSubject();
				double sim =  new Jaro().score(s1, s2);
				if(sim >= simThreshold) {
					es.add(new EntityPair(i,j,sim));
					count++;
				}
				j++;
			}
			i++;
		}
		k = j;
		return count;
	}
	public int computeProperties(List<Entity> entities1 , List<Entity> entities2,double simThreshold) throws IOException {
		int count = 0;
		for(Entity entity1:entities1) {
			j = k;
			String s1 = entity1.getSubject();
			for(Entity entity2:entities2) {
				String s2 = entity2.getSubject();
				double sim =  new Jaro().score(s1, s2);
				if(sim >= simThreshold) {
					es.add(new EntityPair(i,j,sim));
					count++;
				}
				j++;
			}
			i++;
		}
		k = j;
		return count;
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
			List<String> obj2;
			if((obj2 = predList2.get(pred1)) != null){
				List<String> obj1 = predList1.get(pred1);
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
		if(count>0)
			sim = sim/count;
		return sim;
	}
	/*
	 * 使用instance 的属性间的相似度
	 *******这里计算两个不同数据源间实例的相似度是基于属性的，如果两个数据源中的属性名差异很大，那么这个计算相似度会有比较大的误差*******
	 *(可以将实例的所有属性和属性值组成一个字符串，再计算两个字符串间的相似度，得到实例间的相似度)
	 */
	public int computeInstances(List<Entity> owl1instances, List<Entity> owl2instances, double simThreshold) throws IOException {
	//	int n1 = owl1instances.size();
	//	int n2 = owl2instances.size();
		int count = 0;
		for(Entity i1:owl1instances) {
			j = k;
			Map<String, List<String>> predList1 = i1.getPredicate_object();
			List<String> type1 = predList1.get("type");
			for(Entity i2:owl2instances) {
				double sim = 0.0;
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
					sim = getSim(predList1,predList2);
				if(sim >= simThreshold) {
					count++;
					es.add(new EntityPair(i,j,sim));
				}
				j++;
			}
			i++;
		}
	//	System.out.println("i:" + i);
	//	System.out.println("j:" + j);
		return count;
	}
}

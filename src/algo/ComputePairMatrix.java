package algo;

import java.io.IOException;
import java.util.List;

import data.Entity;
import data.EntityPair;

//用于得到PK_RWR的随机游走图对应的矩阵，用于PK_RWR匹配
public class ComputePairMatrix {

	public List<Entity> owl1classes;
	public List<Entity> owl2classes;
	public List<Entity> owl1properties;
	public List<Entity> owl2properties;
	public List<Entity> owl1instances;
	public List<Entity> owl2instances;
	public int owl1classSize;
	public int owl2classSize;
	public int owl1propertySize;
	public int owl2propertySize;
	public int owl1instanceSize;
	public int owl2instanceSize;
	public List<EntityPair> ep;
	
	
	public ComputePairMatrix(List<Entity> owl1classes, List<Entity> owl2classes, List<Entity> owl1properties,
			List<Entity> owl2properties, List<Entity> owl1instances, List<Entity> owl2instances, List<EntityPair> ep) {
		super();
		this.owl1classes = owl1classes;
		this.owl2classes = owl2classes;
		this.owl1properties = owl1properties;
		this.owl2properties = owl2properties;
		this.owl1instances = owl1instances;
		this.owl2instances = owl2instances;
		this.owl1classSize = this.owl1classes.size();
		this.owl2classSize = this.owl2classes.size();
		this.owl1propertySize = this.owl1properties.size();
		this.owl2propertySize = this.owl2properties.size();
		this.owl1instanceSize = this.owl1instances.size();
		this.owl2instanceSize = this.owl2instances.size();
		this.ep = ep;
	}
	/*
	 * 计算可能的匹配对与匹配对之间的相似度
	 */
	public double[][] compute() throws IOException {
		int n = ep.size();
	//	System.out.println(n);
		int m = owl1classSize+owl2classSize+owl1propertySize+owl2propertySize+owl1instanceSize+owl2instanceSize;
		double[][] w = new double[n+1][n+1];
		for(int i = 0 ; i < n; i++) {
			EntityPair p1 = ep.get(i);
    		Double es1 = p1.getSim();
    		int ei = p1.getEntity1();
    		int ea = p1.getEntity2();
    		for(int j = i+1; j < n; j++) {
    			EntityPair p2 = ep.get(j);
    			double es2 = p2.getSim();
        		int ej = p2.getEntity1();
        		int eb = p2.getEntity2();
        		if(hasRelation(ei,ea,ej,eb)) {
        			double sim = (es1+es2)/2;
        			if(sim != 0.0) {
        				w[i][j] = Math.exp(sim);
        				w[j][i] = Math.exp(sim);
        			}
        			else {
        				w[i][j] = 1.0/m;
        				w[j][i] = 1.0/m;
        			}
        		}
        		else {
        			w[i][j] = 1.0/n;
        			w[j][i] = 1.0/n;
        		}
    		}
		}
		for(int i = 0; i < n; i++)
			w[i][i] = Math.exp(ep.get(i).getSim());
		return w;  	
	}
	/*
	 * 判定两个匹配对之间是否存在关系
	 */
	public boolean hasRelation(int i,int a,int j,int b) {
		int m1 = owl1classSize+owl1propertySize;
		int m2 = owl2classSize+owl2propertySize;
		if(i < owl1classSize) {
			String ui = owl1classes.get(i).getSubject();//类名
			String ua = owl2classes.get(a).getSubject();
			/*
			 * 类匹配对与类匹配对（is-a关系）
			 * <ci,ca>与<cj,cb>返回true：ci==subclassof(cj) && ca==subclassof(cb) || cj==subclassof(ci) && cb==subclassof(ca)
			 */
			if(j < owl1classSize) {
				String uj = owl1classes.get(j).getSubject();
				String ub = owl2classes.get(b).getSubject();
				List<String> ci = owl1classes.get(i).getPredicate("subClassof");//父类
				List<String> ca = owl2classes.get(a).getPredicate("subClassof");
				List<String> cj = owl1classes.get(j).getPredicate("subClassof");
				List<String> cb = owl2classes.get(b).getPredicate("subClassof");
				if(((ci != null) && (ca != null) && ci.contains(uj) && ca.contains(ub))
				||((cj != null) && (cb != null) && cj.contains(ui) && cb.contains(ua)))
					return true;
				else
					return false;
			}
			//类匹配对与属性匹配对（has-property关系）
			else if(j < m1) {
				List<String> dj = owl1properties.get(j-owl1classSize).getPredicate("domain");
				List<String> rj = owl1properties.get(j-owl1classSize).getPredicate("range");
				List<String> db = owl2properties.get(b-owl2classSize).getPredicate("domain");
				List<String> rb = owl2properties.get(b-owl2classSize).getPredicate("range");
				if(((dj != null) && (db != null) && dj.contains(ui)&&db.contains(ua))
				||((rj != null) && (rb != null) && rj.contains(ui)&&rb.contains(ua)))
					return true;
				else
					return false;
			}
			//类匹配对与实例匹配对（has-instance关系）
			else {
				List<String> cj = owl1instances.get(j-m1).getPredicate("type");
				List<String> cb = owl2instances.get(b-m2).getPredicate("type");
				if((cj != null) &&  (cb != null) && cj.contains(ui) && cb.contains(ua)) {
				//	System.out.println("实例与类成功匹配");
					return true;
				}
				else
					return false;
			}
		}		
		else if(i < m1){
			String ui = owl1properties.get(i-owl1classSize).getSubject();
			String ua = owl2properties.get(a-owl2classSize).getSubject();
			/*
			 * 属性匹配对与类匹配对（isPropertyof关系）
			 * 基于对称性，这里应该不能被执行到，可以作为检测
			 * 当矩阵不对称，或者边是对之间的边是有向边时，需要用到
			 */
		/*	if(j < owl1ClassesCount) {
				String di = owl1Properties.get(i).getPredicte("domain");
				String ri = owl1Properties.get(i).getPredicte("range");
				String da = owl2Properties.get(a).getPredicte("domain");
				String ra = owl2Properties.get(a).getPredicte("range");
				if((uj.equals(di)&&ub.equals(da))||(ub.equals(ri)&&ub.equals(ra)))
					return true;
				else
					return false;
			}
			//属性对与属性对（is-a关系）
			else if(j < m1) {*/
			if(j < m1)	{
				String uj = owl1properties.get(j-owl1classSize).getSubject();
				String ub = owl2properties.get(b-owl2classSize).getSubject();
				List<String> ci = owl1properties.get(i-owl1classSize).getPredicate("subPropertyof");//父属性
				List<String> ca = owl2properties.get(a-owl2classSize).getPredicate("subPropertyof");
				List<String> cj = owl1properties.get(j-owl1classSize).getPredicate("subPropertyof");
				List<String> cb = owl2properties.get(b-owl2classSize).getPredicate("subPropertyof");
				if(((ui != null) && (ua != null) && ui.equals(cj) && ua.equals(cb))
				||((uj != null) && (ub != null) && uj.equals(ci) && ub.equals(ca)))
					return true;
				else
					return false;
			}
			//属性对与实例对（has-property关系）
			else {
				List<String> cj = owl1instances.get(j-m1).getPredicate(ui);
				List<String> cb = owl2instances.get(b-m2).getPredicate(ua);
				if(cj != null && cb != null)
					return true;
				else
					return false;
			}
		}
		else {
			//实例对与实例对（hasTripleRelation关系。即<ui,p,uj>and<ua,p,ub> || <uj,p,ui>and<ub,p,ua>）
			String ui = owl1instances.get(i-m1).getSubject();//实例名
			String ua = owl2instances.get(a-m2).getSubject();
			String uj = owl1instances.get(j-m1).getSubject();
			String ub = owl2instances.get(b-m2).getSubject();
			boolean fi = owl1instances.get(i-m1).containsObject(uj);
			boolean fj = owl1instances.get(j-m1).containsObject(ui);
			boolean fa = owl2instances.get(a-m2).containsObject(ub);
			boolean fb = owl2instances.get(b-m2).containsObject(ua);
			if((fi&&fa) || (fj&&fb)) {
				//System.out.println("ComputePair 189 is true");
				return true;
			}
			else
				return false;
		}
	}

}

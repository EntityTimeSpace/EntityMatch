package temporal.triple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class mytemp {
	public static String B = "Before";
	public static String M = "Meets";
	public static String Mi = "MetBy";
	public static String O = "Overlaps";
	public static String Oi = "OverlappedBy";
	public static String S = "Starts";
	public static String Si = "StartedBy";
	public static String D = "During";
	public static String Di = "Contains";
	public static String F = "Finishes";
	public static String Fi = "FinishedBy";
	public static String Eq = "Equals";
	public static boolean iscontinue = true;//循环求交集何时停止
	public static boolean is2continue = true;//多次推理何时停止
	public static ArrayList<mytriple> original;
	public static ArrayList<mytriple> end;
	//public static boolean is2continue = true;

	public static void main(String arg[]) throws IOException, InterruptedException, ParseException {
		/*
		 * 1.取出文件中的一个个三元组信息放入mytriple数组 getMytriple(String filename)
		 * 2.遍历mytriple数组两两组合成my2triple数组 getMy2triple(ArrayList<mytriple> mt)
		 * 3.遍历my2triple数组，用to1223()方法转成1223的形式方便查表，true是成功转成1223，可以继续进行查表前往4;
		 * false是没有关联关系，不查表且移出数组，到5 getRelationList(ArrayList<my2triple> m2t)
		 * 4.查表：调用方法doCompositionTable()，得到可能的关系集合
		 * 5.对关系集合数组遍历,相同的归到一起，求交集，放入map，交集为空则出现矛盾，清空map；交集不为空整理打印结果
		 * doIntersectionLoop(ArrayList<mytriple> relal)
		 */
		/*
		 * BUG=.= 1.当只遇到一组ab的可能关系集时，map为空，但实际上这唯一的一组关系集即推理结果。 DONE
		 * 增加flag标志这种特殊不冲突情况，将唯一的一组关系集加入map DONE
		 * 2.没有判断推理出的结果中是否有已知关系的三元组，有的话可能发生了冲突，没有则正常。 进行两次求交集 DONE
		 * 3.对结果进行排序。DONE 4.需不需要进行二次推理，即两重可能的关系下进行推理 TODO
		 * 二次推理求的应该是并集，并集再与一次推理结果求交集 不断地进行循环求交集，直至结果不再出现新的元素，利用全局变量标志位iscontinue
		 * DONE
		 */
		String path = ".//input//";
		String file="news_1.txt";
		String filename = path + file;
		//String outfile = path + "upd" + file;
		String outsortfile=path+"output_"+file;
		original = getMytriple(filename);
		/*System.out.println("---------------------original");
		for(mytriple one:original) {
			one.printinfo();
		}*/
		for(mytriple mt:original){
			System.out.println(mt.obj+" "+mt.pred+" "+mt.sub);
		}
		Scanner sc=new Scanner(System.in);
		//String tmp=sc.nextLine();
		ArrayList<my2triple> m2tl = getMy2triple(original);
		/*for(my2triple mt:m2tl){
			System.out.println(mt.tri1.obj+" "+mt.tri1.pred+" "+mt.tri1.sub);
			System.out.println(mt.tri2.obj+" "+mt.tri2.pred+" "+mt.tri2.sub);
			System.out.println("_________");
		}*/
		//String tmp=sc.nextLine();
		ArrayList<mytriple> relal = getRelationList(m2tl);
		ArrayList<mytriple> map = doIntersectionLoop(relal);
		while (iscontinue) {
			map = doIntersectionLoop(map);
		}
		System.out.println("---------------------doIntersectionLoop");
		for(mytriple one:map) {
			one.printinfo();
		}
		System.out.println("---------------------doIntersectionLoop end");
		if (map.isEmpty()) {
			System.out.println("Inconsistent!");
			System.exit(0);
		} else {
			for (mytriple one : map) {
				if (one.probrela == null && one.getPred() == null) {
					System.out.println("                    ---------------------before doTwiceReasoning null");
				}
			}
			ArrayList<mytriple> map1 = addOriginalArray(map,original);
			System.out.println("---------------------addOriginalArray");
			for(mytriple one:map1) {
				one.printinfo();
			}
			System.out.println("---------------------addOriginalArray end");
			ArrayList<mytriple> map2 = doTwiceReasoning(map1);
			System.out.println("---------------------first doTwiceReasoning");
			for(mytriple one:map2) {
				one.printinfo();
			}
			System.out.println("---------------------first doTwiceReasoning end");
			System.exit(0);
			//二次推理
			while(is2continue) {
				map2 = doTwiceReasoning(map2);
			}
			if (map2.isEmpty()) {
				System.out.println("Inconsistent!");
				System.exit(0);
			}
			for (mytriple one : map2) {
					System.out.println("                    ---------------------after doTwiceReasoning null");
			}
			// 整理打印结果
			Map<mytriple, Set<String>> onemap = listtomap(map2);
			Set<String> ret = doArrangementStrEquals(original, onemap);
			getOutputFileStrSort(ret, outsortfile);
		}
	}
	
	public static ArrayList<mytriple> addOriginalArray(ArrayList<mytriple> arr,ArrayList<mytriple> add) {
		for(mytriple one:add) {
			arr.add(one);
		}
		return arr;
	}

	public static ArrayList<mytriple> doTwiceReasoning(ArrayList<mytriple> once) throws InterruptedException {
		/*
		 * 生成对应的set的数组，便于按顺序取出元素 直接将进行二次推理的triple对放入my2triple getMy2triple
		 * 对每个三元组对进行求二次推理的关系集，一个三元组对遍历每种可能的两两关系组合，进行查表，对结果求并集，
		 * getRelationListTwice
		 * 接着和第一次推理一样，对getRelationListTwice得到的结果进行循环求交集，直至没有产生新的推理结果。
		 */
		is2continue=false;
		System.out.println("                    ---------------------in doTwiceReasoning begin");
		// 生成可能关系的数组
		for (mytriple mt : once) {
			mt.setProbList(mt.settolist(mt.probrela));//若推测关系为空则放入一直关系中的谓词pred
			for (String str : mt.getProbList()) {//获得上一步的所有谓词列表
				if (str == null) {
					System.out.println("\tNULL!!!");
				}
			}
		}
		ArrayList<my2triple> m2t = getMy2triple(once);
		/*System.out.println("---------------------original to my2triple");
		for(my2triple one:m2t) {
			one.printinfo();
		}
		System.out.println("---------------------original to my2triple end");*/
		ArrayList<mytriple> rela = getRelationListTwice(m2t,once);//is2continue=true
		/*System.out.println("---------------------getRelationListTwice");
		for(mytriple one:rela) {
			one.printinfo();
		}
		System.out.println("---------------------getRelationListTwice end");*/
		if(!is2continue) {
			System.out.println("                    ---------------------\t\t\tis2continue false");
			return once;
		}
		ArrayList<mytriple> map = doIntersectionLoop(rela);
		while (iscontinue) {
			if (map.isEmpty()) {
				System.out.println("Inconsistent!");
				System.exit(0);
			}
			map = doIntersectionLoop(map);
		}
		if (map.isEmpty()) {
			System.out.println("Inconsistent!");
			System.exit(0);
		}
		/*for (int i = 0; i < once.size(); i++) {
			mytriple one = once.get(i);
			if (one.getPred() == null && one.probrela == null) {
				System.out.println("                    ---------------------in doTwiceReasoning null!!!");
				System.exit(0);
			}
			for (int j = i + 1; j < once.size(); j++) {
				mytriple two = once.get(j);
				if (two.getPred() == null && two.probrela == null) {
					System.out.println("                    ---------------------in doTwiceReasoning null!!!");
					System.exit(0);
				}
			}
		}*/
		System.out.println("                    ---------------------in doTwiceReasoning end");
		return map;
	}

	public static void getOutputFile(ArrayList<mytriple> ret, String outfile) throws IOException {
		File file = new File(outfile);
		file.createNewFile();
		FileWriter fw = new FileWriter(outfile);
		BufferedWriter bw = new BufferedWriter(fw);
		for (mytriple my : ret) {
			bw.write(my.getSub() + " " + my.getPred() + " " + my.getObj());
			bw.newLine();
			bw.flush();
		}
		bw.close();
		fw.close();
	}

	public static void getOutputFileStr(Set<String> ret, String outfile) throws IOException {
		File file = new File(outfile);
		file.createNewFile();
		FileWriter fw = new FileWriter(outfile);
		BufferedWriter bw = new BufferedWriter(fw);
		for (String my : ret) {
			bw.write(my);
			bw.newLine();
			bw.flush();
		}
		bw.close();
		fw.close();
	}

	public static void getOutputFileStrSort(Set<String> ret, String outfile) throws IOException {
		String[] sret = new String[ret.size()];
		ret.toArray(sret);
		List<String> forsort = Arrays.asList(sret);
		Collections.sort(forsort);
		File file = new File(outfile);
		file.createNewFile();
		FileWriter fw = new FileWriter(outfile);
		BufferedWriter bw = new BufferedWriter(fw);
		for (String my : forsort) {
			bw.write(my);
			bw.newLine();
			bw.flush();
		}
		bw.close();
		fw.close();
	}

	public static ArrayList<mytriple> doArrangement(ArrayList<mytriple> mtl, Map<mytriple, Set<String>> map) {
		// Map<String,String> sub=new HashMap<String,String>();
		ArrayList<mytriple> ret = new ArrayList<mytriple>();
		for (mytriple mt : mtl) {
			ret.add(mt);
			ret.add(mt.getInverse());
		}
		for (Map.Entry<mytriple, Set<String>> entry : map.entrySet()) {
			Set<String> set = entry.getValue();
			mytriple my = entry.getKey();
			for (String str : set) {
				my.setPred(str);
				ret.add(my);
				ret.add(my.getInverse());
			}
		}
		return ret;
	}

	public static Set<String> doArrangementStr(ArrayList<mytriple> mtl, Map<mytriple, Set<String>> map) {
		// Map<String,String> sub=new HashMap<String,String>();
		Set<String> ret = new HashSet<String>();
		for (mytriple mt : mtl) {
			ret.add(mt.infoString());
			ret.add(mt.getInverse().infoString());
		}
		for (Map.Entry<mytriple, Set<String>> entry : map.entrySet()) {
			Set<String> set = entry.getValue();
			mytriple my = entry.getKey();
			for (String str : set) {
				my.setType(false);
				my.setPred(str);
				ret.add(my.infoString());
				ret.add(my.getInverse().infoString());
			}
		}
		return ret;
	}

	public static Set<String> doArrangementStrEquals(ArrayList<mytriple> mtl, Map<mytriple, Set<String>> map) {
		// Map<String,String> sub=new HashMap<String,String>();
		Set<String> ret = new HashSet<String>();
		for (mytriple mt : mtl) {
			ret.add(mt.infoString());
			ret.add(mt.getInverse().infoString());
			mytriple e1 = new mytriple(mt.getSub(), mytriple.Eq, mt.getSub());
			mytriple e2 = new mytriple(mt.getObj(), mytriple.Eq, mt.getObj());
			ret.add(e1.infoString());
			ret.add(e2.infoString());
		}
		for (Map.Entry<mytriple, Set<String>> entry : map.entrySet()) {
			Set<String> set = entry.getValue();
			mytriple my = entry.getKey();
			for (String str : set) {
				my.setType(false);
				my.setPred(str);
				ret.add(my.infoString());
				ret.add(my.getInverse().infoString());
			}
		}
		return ret;
	}

	public static Map<mytriple, Set<String>> doIntersection(ArrayList<mytriple> relal) {
		Map<mytriple, Set<String>> map = new HashMap<mytriple, Set<String>>();
		System.out.println("                                                                              here");
		for (int i = 0; i < relal.size(); i++) {
			mytriple one = relal.get(i);
			boolean flag = false;
			for (int j = i + 1; j < relal.size(); j++) {
				mytriple two = relal.get(j);
				// one.printinfo();
				// two.printinfo();
				// System.out.println(" "+" if");
				if (one.equals(two)) {
					flag = true;
					System.out.println("                                                                           if");
					// 判断两个关系集主语宾语一样，再求交集 one and two
					Set<String> ret = new HashSet<String>();
					ret.clear();
					ret.addAll(one.probrela);
					ret.retainAll(two.probrela);
					System.out.println("one haha " + one.probrela);
					System.out.println("two hehe " + two.probrela);
					if (map.containsKey(one)) {
						Set<String> last = map.get(one);
						// 与两条关系的交集作交集运算
						Set<String> ret2 = new HashSet<String>();
						ret2.clear();
						ret2.addAll(ret);
						ret.retainAll(last);
						// 将新的交集覆盖掉旧的
						map.put(one, ret2);
						System.out.println("intersection " + ret2);
						if (ret2.isEmpty()) {
							System.out.println("exists do retain empty!");
							one.printinfo();
							map.clear();
							return map;
						}
					} else {
						System.out.println("intersection " + ret);
						map.put(one, ret);
						if (ret.isEmpty()) {
							System.out.println("Intersection is empty!");
							map.clear();
							return map;
						}
					}
				}
			}
			if (!flag) {
				if (!map.containsKey(one)) {
					one.printinfo();
					map.put(one, one.getProb());
				}
			}
		}
		System.out.println("                    ---------------------in doIntersection end");
		return map;
	}

	public static ArrayList<mytriple> doIntersectionLoop(ArrayList<mytriple> relal) {
		System.out.println("                    ---------------------in doIntersectionLOOP begin");
		iscontinue = false;
		ArrayList<mytriple> map = new ArrayList<mytriple>();
		// System.out.println(" here");
		ArrayList<Integer> mapflag=new ArrayList<Integer>();
		for(mytriple one:relal) {
			mapflag.add(0);
			one.printinfo();//System.out.println(x);
		}
		for (int i = 0; i < relal.size(); i++) {
			mytriple one = relal.get(i);
			//System.out.println("                    ---------------------"+one.infoString());
			boolean flag = false;
			if (one.probrela == null && one.getPred() == null) {
				// System.out.println("null loop4 null");
			}
			for (int j = i + 1; j < relal.size(); j++) {
				mytriple two = relal.get(j);
				if (one.equals(two)) {
					mapflag.set(j, 1);
					flag = true;
					// System.out.println(" if");
					// 判断两个关系集主语宾语一样，再求交集 one and two
					if (two.getType()) {// true right；false set
						two.setProb(two.getPred());
					}
					Set<String> ret = new HashSet<String>();
					ret.clear();
					ret.addAll(one.probrela);
					ret.retainAll(two.probrela);
					System.out.println("one haha " + one.probrela);
					System.out.println("two hehe " + two.probrela);
					int index = map.indexOf(one);
					iscontinue = true;
					if (index != -1) {// 存在one
						Set<String> last = map.get(index).getProb();
						// 与两条关系的交集作交集运算
						Set<String> ret2 = new HashSet<String>();
						ret2.clear();
						ret2.addAll(ret);
						ret.retainAll(last);
						// 将新的交集覆盖掉旧的
						one.setProb(ret2);
						if (one.probrela == null && one.getPred() == null) {
							// System.out.println("null loop null");
						}
						map.set(index, one);
						System.out.println("	one exists intersection " + ret2);
						if (ret2.isEmpty()) {
							System.out.println("exists do retain empty!");
							one.printinfo();
							map.clear();
							return map;
						}
					} else {
						System.out.println("	intersection " + ret);
						one.setProb(ret);
						if (one.probrela == null && one.getPred() == null) {
							// System.out.println("null loop2 null");
						}
						map.add(one);
						if (ret.isEmpty()) {
							System.out.println("Intersection is empty!");
							map.clear();
							return map;
						}
					}
				}
			}
			if (!flag&&mapflag.get(i)!=1) {
				if (!map.contains(one)) {
					System.out.println("!flag");
					one.printinfo();
					if (one.probrela == null && one.getPred() == null) {
						// System.out.println("null loop3 null");
					}
					map.add(one);
				}
			}
		}
		System.out.println("                    ---------------------in doIntersectionLOOP end");
		return map;
	}

	public static ArrayList<mytriple> getRelationList(ArrayList<my2triple> m2t) {
		ArrayList<mytriple> ret = new ArrayList<mytriple>();
		for (Iterator<my2triple> iterator1 = m2t.iterator(); iterator1.hasNext();) {
			my2triple one = iterator1.next();
			//one.printinfo();
			if (one.to1223()) {
				//to1223为了让m2t里的两对关系中间两个实体是一样的
				Set<String> prob = one.doCompositionTable();
				if (prob == null) {
					// System.out.println("\n null loop6 null");
				}
				//同构方法中的方法2，说明中间参数加入的关系列表是预测关系
				mytriple my = new mytriple(one.gettri1().getSub(), prob, one.gettri2().getObj());
				if (my.probrela == null && my.getPred() == null) {
					//即预测关系为空，并且输入中不存在约束关系??为什么重复判断两次??
					// System.out.println("null loop5 null");
				}
				ret.add(my);
				// System.out.println("in getRelationList\t\t\t" + my.getSub() +
				// prob + my.getObj());
			} else {
				//如果不能转成1223格式则删除这对关系
				iterator1.remove();
			}
		}
		/*for(mytriple r:ret){
			System.out.println(r.obj+" "+r.sub);
			for(String pString:r.probrela){
				System.out.println(pString);
			}
			System.out.println("_____________________");
		}
		Scanner scanner=new Scanner(System.in);
		scanner.nextLine();*/
		System.out.println("                    ---------------------in getRelationList end");
		return ret;
	}

	public static ArrayList<mytriple> getRelationListTwice(ArrayList<my2triple> m2t,ArrayList<mytriple> once) {
		System.out.println("                    ---------------------in getRelationListTwice begin");
		ArrayList<mytriple> ret = new ArrayList<mytriple>();
		for(my2triple mt:m2t) {
			mt.printinfo();
		}
		for (Iterator<my2triple> iterator1 = m2t.iterator(); iterator1.hasNext();) {
			my2triple it = iterator1.next();
			//it.printinfo();
			if (it.to1223()) {
				it.printinfo();
				ArrayList<mytriple> un = new ArrayList<mytriple>();
				mytriple one = it.gettri1(), two = it.gettri2();
				for (int k = 0; k < one.getProbListNum(); k++) {
					if (one.getPred() == null && one.probrela == null) {
						System.out.println("                    ---------------------in getRelationListTwice null!!!");
						System.exit(0);
					}
					//System.out.println("-----------before loop\t"+one.infoString()+"\t"+two.infoString());
					for (int l = 0; l < two.getProbListNum(); l++) {
						// 开始两两搭配循环查表
						//System.out.println("-----------before loop\t"+one.infoString()+"\t"+two.infoString());
						mytriple one1 = new mytriple(one.getSub(), one.getProbList().get(k), one.getObj());
						mytriple two1 = new mytriple(two.getSub(), two.getProbList().get(l), two.getObj());
						my2triple my = new my2triple(one1, two1);
						Set<String> prob = my.doCompositionTable();
					/*	System.out.println("---------------------before get union,do composition");
						System.out.println(one1.infoString()+"\t"+two1.infoString());
						System.out.println(prob);*/
						if (prob == null) {
							System.out.println(
									"                    ---------------------in getRelationListTwice doComposirion null!!!");
						}
						mytriple myt = new mytriple(one1.getSub(), prob, two1.getObj());
						un.add(myt);
						//System.out.println("-----------before union\t"+one1.infoString()+"\t"+two1.infoString()+myt.infoString());
					}
				}
				// 求并集后再加入ret
				Set<String> set = new HashSet<String>();
				set.clear();
				set.addAll(un.get(0).probrela);
				/*System.out.println("---------------------get union");
				for (mytriple o : un) {
					set.addAll(o.probrela);
					System.out.println(set+"\t"+o.infoString());
				}
				System.out.println("---------------------get union end");*/
				un.get(0).setProb(set);
				//un.get(0).printinfo();
				boolean flag=false;
				for(mytriple on:once) {
					if(on.probrela==null) {
						on.printinfo();
					}
					if(!on.strequals(un.get(0)))
						flag=true;
					System.out.println(flag+"\t"+on.infoString()+"\t"+un.get(0).infoString());
				}
				ret.add(un.get(0));
				is2continue=flag;
			} else {
				// System.out.println("in getRelationList\t\t\tno association");
				iterator1.remove();
			}
		}
		System.out.println("                    ---------------------in getRelationListTwice end");
		return ret;
	}

	public static ArrayList<my2triple> getMy2triple(ArrayList<mytriple> mt) {
		ArrayList<my2triple> m2t = new ArrayList<my2triple>();
		for (int i = 0; i < mt.size(); i++) {
			mytriple one = mt.get(i);
			for (int j = i + 1; j < mt.size(); j++) {
				mytriple two = mt.get(j);
				my2triple m2 = new my2triple(one, two);
				// m2.printinfo();
				m2t.add(m2);
			}
		}
		System.out.println("                    ---------------------in getMy2triple end");
		return m2t;
	}

	public static ArrayList<mytriple> getMytriple(String filename) throws IOException, ParseException {
		ArrayList<mytriple> ll = new ArrayList<mytriple>();
		File file = new File(filename);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		String tempString = null;
		ArrayList<Event> events=new ArrayList<Event>();
		while ((tempString = reader.readLine()) != null) {
			String[] t = tempString.split(" ");
			events.add(new Event(t[0], t[1], t[2]));
			//t[0]是事件主体，t[1]是开始时间，t[2]是结束时间	
			//mytriple mt = new mytriple(t[0], t[1], t[2]);
			//ll.add(mt);
		}
		reader.close();
		return GetContrainNetWork(events);
	}
	
	public static ArrayList<mytriple> GetContrainNetWork(ArrayList<Event> events) {
		ArrayList<mytriple> ll = new ArrayList<mytriple>();
		int i,j;
		for (i=0;i<events.size()-1;i++) {
			Event event1=events.get(i);
			//ll.add(new mytriple(event1.EventName, "Equals", event1.EventName));
			for(j=i+1;j<events.size();j++){
				Event event2=events.get(j);
				//这一步结束之后是不是不需要把关系补充完整
				if((event1.StartTime.getTime()>event2.StartTime.getTime())
						&(event1.EndTime.getTime()>event2.EndTime.getTime())
						&(event1.EndTime.getTime()<event2.StartTime.getTime())){
					ll.add(new mytriple(event1.EventName, "Overlaps", event2.EventName));
					ll.add(new mytriple(event2.EventName, "OverlappedBy", event1.EventName));
				}
				else if((event1.StartTime.getTime()==event2.StartTime.getTime())
						&(event1.EndTime.getTime()>event2.EndTime.getTime())){
					ll.add(new mytriple(event1.EventName, "Starts", event2.EventName));
					ll.add(new mytriple(event2.EventName, "StartedBy", event1.EventName));
				}
				else if((event1.StartTime.getTime()<event2.StartTime.getTime())
						&(event1.EndTime.getTime()>event2.EndTime.getTime())){
					ll.add(new mytriple(event1.EventName, "During", event2.EventName));
					ll.add(new mytriple(event2.EventName, "Contains", event1.EventName));
				}
				else if((event1.StartTime.getTime()<event2.StartTime.getTime())
						&(event1.EndTime.getTime()==event2.EndTime.getTime())){
					ll.add(new mytriple(event1.EventName, "Finishes", event2.EventName));
					ll.add(new mytriple(event2.EventName, "FinishedBy", event1.EventName));
				}
				else if((event1.StartTime.getTime()==event2.StartTime.getTime())
						&(event1.EndTime.getTime()==event2.EndTime.getTime())){
					ll.add(new mytriple(event1.EventName, "Equals", event2.EventName));
					ll.add(new mytriple(event2.EventName, "Equals", event1.EventName));
				}
				else if(event1.EndTime.getTime()>event2.StartTime.getTime()){
					ll.add(new mytriple(event1.EventName, "Before", event2.EventName));
					ll.add(new mytriple(event2.EventName, "After", event1.EventName));
				}
				else if(event1.EndTime.getTime()==event2.StartTime.getTime()){
					ll.add(new mytriple(event1.EventName, "Meets", event2.EventName));
					ll.add(new mytriple(event2.EventName, "MetBy", event1.EventName));
				}
			}
		}
		//ll.add(new mytriple(events.get(i).EventName, "Equals", events.get(i).EventName));
		return ll;
	}
	
	public static Map<mytriple, Set<String>> listtomap(ArrayList<mytriple> mt) {
		Map<mytriple, Set<String>> ret = new HashMap<mytriple, Set<String>>();
		for (mytriple my : mt) {
			ret.put(my, my.getProb());
		}
		return ret;
	}

}

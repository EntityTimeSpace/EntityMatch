package Test;

import java.util.HashMap;
import java.util.HashSet;

import com.hp.hpl.jena.vocabulary.RDF;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(RDF.type.getURI());
		HashMap<String,HashSet<Integer>> map = new HashMap<>();
		HashSet<Integer> set = new HashSet<>();
		set.add(1);
		set.add(2);
		set.add(3);
		map.put("key", set);
		set = map.get("key");
		set.add(5);
		set.add(6);
		System.out.println(map);
	}

}

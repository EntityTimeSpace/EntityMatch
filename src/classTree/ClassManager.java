/**
 * 
 */
package classTree;

import java.util.ArrayList;
import java.util.List;

import classTree.TreeNode;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
/**
 * @author "Cunxin Jia"
 *
 */
public class ClassManager {
	private static ClassManager instance = null;
	
	private OntModel model; 
	
	public static ClassManager getInstance() {
		if(instance == null) {
			instance = new ClassManager();
		}
		return instance;
	}
	
	private ClassManager() {
		model = ModelFactory.createOntologyModel();
		String path = ClassManager.class.getClassLoader().getResource("ont.owl").getPath();
		model.read("file:" + path);
		//model.write(System.out);
	}
	
	public List<OntClass> listClasses() {
		ExtendedIterator<OntClass> iter =  model.listClasses();
//		System.out.println("iter=========="+iter.toString()+"iter===========");
		List<OntClass> clsList = new ArrayList<OntClass> ();
		while(iter.hasNext()) {
			OntClass cls = iter.next();		
			//System.out.println("cls=========="+cls.toString()+"===========cls");
			if(getNumOfSupClasses(cls) == 1) {
				clsList.add(cls);
			}
		}
		return clsList;
	}
	
	public List<OntClass> listSubClasses(OntClass cls) {
		List<OntClass> clsList = new ArrayList<OntClass> ();
		ExtendedIterator<OntClass> iter =  cls.listSubClasses();
		int clsLayer = getNumOfSupClasses(cls);
		while(iter.hasNext()) {
			OntClass subcls = iter.next();
			int subclsLayer = getNumOfSupClasses(subcls);
			if(subclsLayer == (clsLayer + 1)) {
				clsList.add(subcls);
			}
		}
		return clsList;
	}

	
	public int getNumOfSupClasses(OntClass cls) {
		int count = 0;
		ExtendedIterator<OntClass> supClsIter = cls.listSuperClasses();
		while(supClsIter.hasNext()) {
			supClsIter.next();
			count ++;
		}
		return count;
	}
	
	
	public static void main(String[] args) {
		List<OntClass> clsList = ClassManager.getInstance().listClasses();
		for(OntClass cls : clsList) {
			String uri = cls.getURI();
			String label = cls.getLabel(null);
			System.out.println(label);
			List<OntClass> subClsList = ClassManager.getInstance().listSubClasses(cls);
			for(OntClass subCls : subClsList) {
				String suburi = subCls.getURI();
				String sublabel = subCls.getLabel(null);
				System.out.println("\t" + sublabel);
			}
		}
		
		//ClassManager.getInstance();
	}
}

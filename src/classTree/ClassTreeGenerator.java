/**
 * 
 */
package classTree;

import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * @author "Cunxin Jia"
 *
 */
public class ClassTreeGenerator {
	
	private static ClassTreeGenerator instance = null;
	
	public static ClassTreeGenerator getInstance() {
		if(instance == null) {
			instance = new ClassTreeGenerator();
		}
		return instance;
	}
	
	private ClassTreeGenerator() {
		
	}
	
	public ClassTree getClassTree() {
		ClassTree classTree = new ClassTree();
		List<OntClass> clsList = ClassManager.getInstance().listClasses();
		for(OntClass cls : clsList) {
			String uri = cls.getURI();
			String label = cls.getLabel(null);
			TreeNode node = new TreeNode(label, uri);
			ClassTree subTree = constructSubTree(cls);
			node.setSubTree(subTree);
			classTree.addNode(node);
		}
		classTree.sortNodes();
		return classTree;
	}
	
	public ClassTree constructSubTree(OntClass cls) {
		ClassTree classTree = null;
		List<OntClass> clsList = ClassManager.getInstance().listSubClasses(cls);
		if(clsList.size() != 0) {
			classTree = new ClassTree();
			for(OntClass subCls : clsList) {
				String uri = subCls.getURI();
				String label = subCls.getLabel(null);
				TreeNode node = new TreeNode(label, uri);
				ClassTree subTree = constructSubTree(subCls);
				node.setSubTree(subTree);
				classTree.addNode(node);
			}
		}
		if(classTree != null)
			classTree.sortNodes();
		return classTree;
	}
}

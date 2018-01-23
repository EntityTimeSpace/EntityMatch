/**
 * 
 */
package storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import triple.Triple;
import triple.BlankNode;
import triple.ImplicitPredicate;
import triple.Node;
import triple.TrunkNode;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.JDBC;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author "Cunxin Jia"
 *
 */
public class TripleStorage {
	private static TripleStorage instance = null;
	private Model model;
	private String prefix = "http://ws.nju.edu.cn/nju28_intel/";
	private int iid;
	private List<Resource> trunkResources;
	private SDBConnection conn = null;
	
	public static TripleStorage getInstance() {
		if(instance == null) {
			instance = new TripleStorage();
		}
		instance.reset();
		return instance;
	}
	
	private TripleStorage() {
//		model = ModelFactory.createDefaultModel();
//		model.setNsPrefix("nju28", "http://ws.nju.edu.cn/nju28/");
//		model.setNsPrefix("", prefix);
//		trunkResources = new ArrayList<Resource> ();
	}
	
	private void reset() {
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("nju28", "http://ws.nju.edu.cn/nju28/");
		model.setNsPrefix("", prefix);
		trunkResources = new ArrayList<Resource> ();
	}
	
	private Store getSDBStore() {
		StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash,
                DatabaseType.MySQL) ;
		JDBC.loadDriverMySQL();
		Properties prop = new Properties();
		Store store = null;
		try {
			if(conn == null) {
				prop.load(TripleStorage.class.getClassLoader().getResource("conf.properties").openStream());
				String jdbcURL = prop.getProperty("url");
				String username = prop.getProperty("username");
				String password = prop.getProperty("password");
				conn = new SDBConnection(jdbcURL, username, password) ;
			}
			store = SDBFactory.connectStore(conn, storeDesc);
		} catch (IOException e) {
			store = null;
		}
		
		
		
		
		return store;

	}
	
	public void storeWithSDB() {
		Store store = getSDBStore();
//		store.getTableFormatter().create();
		Dataset dataset = SDBFactory.connectDataset(store);
		dataset.getNamedModel("intel-"+iid).removeAll();
		dataset.getNamedModel("intel-"+iid).add(model);
//		dataset.getDefaultModel().add(model);
		store.close();
	}
	
	public Model getRDFFromSDB(int iid) {
		Store store = getSDBStore();
	    Model model = SDBFactory.connectNamedModel(store, "intel-" + iid);
	    store.close();
	    return model;
	}
	
	public void storeTriples(List<Entry<String,String>>trunks, List<Triple> triples, String source) {
		List<String> trunkList = new ArrayList<String>();
		for(Entry<String, String> trunk : trunks) {
			trunkList.add(trunk.getKey());
		}
		iid = IntelStorage.getInstance().storeTrunk(trunkList, source);
		generateResourceForTrunks(trunks);
		
		for(Triple triple : triples) {
			System.out.println(triple);
			Resource s = getRDFNode(triple.getSubject()).asResource();
			Property p = getRDFNode(triple.getPredicate()).as(Property.class);
			RDFNode  o = getRDFNode(triple.getObject());
			
			model.add(s, p, o);
			
			
		}
		storeWithSDB();
		Model saved = getRDFFromSDB(iid);
		StmtIterator iter = saved.listStatements();
		while(iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
	
	public List<String> getTriples(int iid) {
		List<String> triples = new ArrayList<String> ();
		Model model = getRDFFromSDB(iid);
		StmtIterator sIter = model.listStatements() ;
	    for ( ; sIter.hasNext() ; ) {
	        Statement stmt = sIter.nextStatement() ;
	        Property prop = stmt.getPredicate();
		
	        if(!prop.equals(RDFS.label)) {
	        	String subjectLabel = getLabel(model, stmt.getSubject());
	        	String propertyLabel = getLabel(model, stmt.getPredicate());
	        	String objectLabel = getLabel(model, stmt.getObject());
	        	if(subjectLabel == null || propertyLabel == null || objectLabel == null ||
	        			subjectLabel.equals("") || propertyLabel.equals("") || objectLabel.equals("") ||
	        			subjectLabel.replaceAll("\\?", "").equals("") ||
	        			propertyLabel.replaceAll("\\?", "").equals("") ||
	        			objectLabel.replaceAll("\\?", "").equals("") ) {
	        		continue;
	        	}
	        	triples.add(subjectLabel + "\t" + propertyLabel + "\t" + objectLabel);
	        }
	    }
		return triples;
	}
	
	private String getLabel(Model model, RDFNode node) {
		String label = node.toString();
		if(label.equals(RDF.type.toString())) {
			label = "RDF:Type";
		}
		else if(label.contains("#_")) {
			String blankNodeIndex = label.substring(label.indexOf("#_") + 2);
			label = "BlankNode_" + blankNodeIndex;
		}
		else if(node.isLiteral()) {
			label = node.asLiteral().getLexicalForm();
		}
		else if(node.isResource()) {
			NodeIterator iter = model.listObjectsOfProperty(node.asResource(), RDFS.label);
        	if(iter.hasNext()) {
        		label = iter.next().asLiteral().getLexicalForm();
        	}
		}
		else if(node.isAnon()) {
			label = node.asNode().getBlankNodeLabel();
		}
		return label;
	}
	
	private RDFNode getRDFNode(Node node) {
		RDFNode rdfNode = null;
		if(node instanceof TrunkNode) {
			int tid = ((TrunkNode) node).getTrunkIndex();
			rdfNode = trunkResources.get(tid);
		}
		
		else if(node instanceof BlankNode) {
			int bid = ((BlankNode) node).getId();
			rdfNode = model.createResource(AnonId.create(iid + "#_" + bid));
		}
		
		else if(node instanceof ImplicitPredicate) {
			String nodeLabel = ((ImplicitPredicate) node).getName();
			rdfNode = model.createProperty(prefix + iid + "#I" + nodeLabel.hashCode());
			rdfNode.asResource().addProperty(RDFS.label, nodeLabel);
		}
		return rdfNode;
	}
	
	private void generateResourceForTrunks(List<Entry<String, String>> trunks) {
		int tid = 0;
		for(Entry<String,String> trunk : trunks) {
			String uri = prefix + iid + "#T" + tid;
			Resource r = model.createResource(uri);
			r.addLiteral(RDFS.label, trunk.getKey());
			if(!trunk.getValue().equals("Undefined")) {
				Resource type = model.createResource(trunk.getValue());
				r.addProperty(RDF.type, type);
			}
			trunkResources.add(tid, r);
			tid++;
		}
	}

	public static void main(String[] args){
	}
}

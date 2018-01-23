package util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import data.Alignment;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class XMLHelper {

    private String fileName;

    public XMLHelper(String fileName){
        this.fileName = fileName;
    }


    public List<Alignment> run() throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(fileName));
        Element root = document.getRootElement();

        List<Alignment> results = new ArrayList<Alignment>();
        listNodes(root,results);
        return results;
    }


    //遍历当前节点下的所有节点
    private void listNodes(Element node , List<Alignment> results){
//       System.out.println("当前节点的名称：" + node.getName());

        if(node.getName().equals("Cell")){


            results.add(new Alignment( node.element("entity1").attributeValue("resource"),
                                    node.element("entity2").attributeValue("resource"),
                                    node.element("relation").getData().toString(),
                                    node.element("measure").attributeValue("datatype"),
                                    node.element("measure").getData().toString()));
        }

        Iterator<Element> iterator = node.elementIterator();
        while(iterator.hasNext()){
            Element e = iterator.next();
            listNodes(e,results);
        }
    }
}

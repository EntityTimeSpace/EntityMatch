package search;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class LabelUriBiMap {
	private static BiMap<String,String> luMap;
	
	private static void init() throws DocumentException {
		if(luMap == null) {
			luMap = HashBiMap.create();
			SAXReader reader = new SAXReader();
			//这里暂时使用绝对路径，可移植性差，后期要解决这个问题。
//	        Document document = reader.read(new File("src/config/class.xml"));
	        Document document = reader.read(LabelUriBiMap.class.getClassLoader().getResourceAsStream("config/class.xml"));
	        Element classList = document.getRootElement();  
	        @SuppressWarnings("unchecked")
			List<Element> cls = classList.elements("class");
	        for(Element e:cls) {
	            luMap.put(e.attributeValue("label"),e.attributeValue("uri"));
	        }
	        document = reader.read(LabelUriBiMap.class.getClassLoader().getResourceAsStream("config/property.xml"));
	        Element propertyList = document.getRootElement();  
	        @SuppressWarnings("unchecked")
			List<Element> pls = propertyList.elements("property");
	        for(Element e:pls) {
	            luMap.put(e.attributeValue("label"),e.attributeValue("uri"));
	        }
		}
	}
	public static String getUriByLabel(String label) throws DocumentException {
		init();
		return luMap.get(label);
	}
	public static String getLabelByUri(String uri) throws DocumentException {
		init();
		return luMap.inverse().get(uri);
	}
	public static void main(String[] args) throws DocumentException {
		String str = "��װֱ����";
		System.out.println(str);
		System.out.println(LabelUriBiMap.getUriByLabel(str));
		System.out.println(LabelUriBiMap.getLabelByUri("http://ws.nju.edu.cn/qademo/Weapon/Warship/hwj/f_22p"));
	}
}

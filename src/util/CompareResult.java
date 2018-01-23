package util;

import java.util.List;

import data.Alignment;

public class CompareResult {
	 private String pre;
	 private String rec;
	 private String f;
	 public String getPre() {
		return pre;
	}
	public void setPre(String pre) {
		this.pre = pre;
	}
	public String getRec() {
		return rec;
	}
	public void setRec(String rec) {
		this.rec = rec;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public void compareResult(List<Alignment> reals , List<Alignment> exps){

	        int matchNum = 0;
	        int notmatchNum = 0;

	        for(Alignment exp : exps){
	            if(reals.contains(exp)){
	                matchNum++;
	            }else{
	                notmatchNum++;
	            }
	        }

	        double precision = matchNum/(double)exps.size()*100;
	        double recall = matchNum/(double)reals.size()*100;
	        double F1 = 2*precision*recall/(precision+recall);
	        pre = String.format("%.2f", precision).toString()+"%";
	        rec = String.format("%.2f", recall).toString()+"%";
	        f = String.format("%.2f", F1).toString()+"%";
	        System.out.println("真实结果匹配对数："+reals.size());
	        System.out.println("实验结果匹配对数："+exps.size());
	        System.out.println("正确匹配对数："+matchNum);
	        System.out.println("错误匹配对数："+notmatchNum);
	        System.out.println("未匹配实体对数："+(reals.size()-matchNum));
	        System.out.println("Precision："+String.format("%.2f", precision).toString()+"%");
	        System.out.println("Recall："+String.format("%.2f", recall).toString()+"%");
	        System.out.println("F1："+String.format("%.2f", F1).toString()+"%");
	    }
}

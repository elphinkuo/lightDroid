package com.elphin.framework.util.jsonparser.type;

import com.elphin.framework.util.jsonparser.BaseObject;

/**
 * 根据模板<br/>
 * {
 * <br/>	"poiId":String,
 * <br/>	"content":String,s
 * <br/>	"recomType":int,
 * <br/>	"averagePay":int,
 * <br/>	"userId":String,
 * <br/>}
 * <br/>
 * <br/>自动生成的JSON访问VO对象
 * @author elphin
 */
public class CommentSubmitData implements BaseObject {
	 public CommentSubmitData() {
	    }
     public CommentSubmitData(String des,int pay,int type,String id,String uid){
    	 content=des;
    	 averagePay=pay;
    	 recomType=type;
    	 poiId=id;
    	 userId=uid;
     }
	 public String content="";


	 public int averagePay=0;


		public int recomType;


		public String poiId="";


		public String userId="";
}

package com.elphin.framework.util.jsonparser.type;

import com.elphin.framework.util.jsonparser.BaseObject;


/**
 * 根据模板<br/>
 * {
 * <br/>    "result": {
 * <br/>		errorCode: int,
 * <br/>		type: int,
 * <br/>		}
 * <br/>}
 * <br/>
 * <br/>自动生成的JSON访问VO对象
 * @author elphin
 */
public class CommentSubmitFeedback implements BaseObject {
	  public CommentSubmitFeedback() {
	    }

		public CommentSubmitFeedbackResult mFeedbackResult;
		public int mError;
}


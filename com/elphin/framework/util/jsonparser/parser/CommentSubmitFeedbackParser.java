package com.elphin.framework.util.jsonparser.parser;
import com.elphin.framework.util.jsonparser.BaseParser;
import com.elphin.framework.util.jsonparser.type.CommentSubmitFeedback;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentSubmitFeedbackParser extends BaseParser<CommentSubmitFeedback> {

    @Override
    public CommentSubmitFeedback parse(JSONObject json) throws JSONException {

    	CommentSubmitFeedback comment_submit_feedbackType = new CommentSubmitFeedback();
    	try{
        if (json.has("result")) {
            comment_submit_feedbackType.mFeedbackResult = new CommentSubmitFeedbackResultParser().parse(json.getJSONObject("result"));
        }
        if (json.has("errorNo")) {
            comment_submit_feedbackType.mError = json.getInt("errorNo");
        }

    	}catch(Exception e){
    		comment_submit_feedbackType.mError=-2;
    		return comment_submit_feedbackType;
    		
    	}
        return comment_submit_feedbackType;
    }

}
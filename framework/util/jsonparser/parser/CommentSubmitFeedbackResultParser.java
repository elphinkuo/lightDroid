package com.elphin.framework.util.jsonparser.parser;
import com.elphin.framework.util.jsonparser.BaseParser;
import com.elphin.framework.util.jsonparser.type.CommentSubmitFeedbackResult;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentSubmitFeedbackResultParser  extends BaseParser<CommentSubmitFeedbackResult> {

    @Override
    public CommentSubmitFeedbackResult parse(JSONObject json) throws JSONException {

    	CommentSubmitFeedbackResult comment_submit_feedbackResultType = new CommentSubmitFeedbackResult();
        if (json.has("errorCode")) {
            comment_submit_feedbackResultType.mErrorCode = json.getInt("errorCode");
        }
        
        if (json.has("type")) {
            comment_submit_feedbackResultType.mType =  json.getString("type");
       }
      
        return comment_submit_feedbackResultType;
    }

}
package com.daddys40.util;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.daddys40.data.QnAData;
import com.daddys40.data.QnADataContainer;
/**
 * 서버로 부터 받아온 JSON 데이터 파싱
 * @author Kim
 *
 */
public class DataParser extends Parser {
	
	@Override
	public ArrayList<?> getListParsingData(Object obj) {
		return null;
	}

	@Override
	public ArrayList<String> getParsingData(Object obj) {
		JSONArray jsonArray = new JSONArray();
		jsonArray = (JSONArray) obj;
		ArrayList<String> mArrayList = new ArrayList<String>();
		try {
			for(int i =0 ; i < jsonArray.size() ; i++){
				String question = (String) ((JSONObject) jsonArray.get(i)).get("question");
				String answer = (String) ((JSONObject) jsonArray.get(i)).get("answer");
				String title = (String) ((JSONObject) jsonArray.get(i)).get("title");
				int week = Integer.parseInt((String) ((JSONObject) jsonArray.get(i)).get("week"));
				QnAData qnaData = new QnAData(question, answer, title, week);
				QnADataContainer.getInstance().addQnAData(qnaData);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mArrayList;
	}
}

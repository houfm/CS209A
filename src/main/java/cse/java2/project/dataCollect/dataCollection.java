package cse.java2.project.dataCollect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dataCollection {
  private cse.java2.project.dataCollect.databaseService databaseService;
  private int pageSize;
  private int pageStep;
  private int pageTotal;
  private List<JSONObject> questionList;
  private List<JSONObject> answerList;
  private List<JSONObject> commentList;

  public dataCollection(int pageSize, int pageStep, int pageTotal, cse.java2.project.dataCollect.databaseService databaseService) {
    this.pageSize = pageSize;
    this.pageStep = pageStep;
    this.pageTotal = pageTotal;
    this.databaseService = databaseService;
    questionList = new ArrayList<>();
    answerList = new ArrayList<>();
    commentList = new ArrayList<>();
  }

  public void collectData() throws IOException, SQLException {
    for (int i = 1; i <= pageTotal; i++) {
      String url = "https://api.stackexchange.com/2.3/questions";
      String params = String.format("page=%d&pagesize=%d&order=desc&sort=activity" +
              "&tagged=java&filter=withbody&site=stackoverflow&" +
              "key=X5JIQM2Vlsnh85sg*BQ6OA((", i * pageStep, pageSize);
      String apiURL = url + "?" + params;
      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet(apiURL);
      CloseableHttpResponse response = httpClient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String responseBody = EntityUtils.toString(entity);
        JSONObject data = JSON.parseObject(responseBody);
        if (data.getJSONArray("items") == null) continue;
        for (int j = 0; j < pageSize; j++) {
          JSONObject item = data.getJSONArray("items").getJSONObject(j);
          questionList.add(item);
        }
      }
      response.close();
      httpClient.close();
    }
    // get answers
    List<Integer> nowQuestionIdList = new ArrayList<>();
    List<Integer> AnswerIDList = new ArrayList<>();
    for (int i = 0; i < questionList.size(); i++) {
      if (i % 100 == 0 && i > 0) {
        String ids = "";
        for (int j = 0; j < nowQuestionIdList.size(); j++) {
          ids += nowQuestionIdList.get(j);
          if (j != nowQuestionIdList.size() - 1) {
            ids += ";";
          }
        }
        List<JSONObject> jsonObjectList = getAnswers(ids);
        for (JSONObject jsonObject : jsonObjectList) {
          JSONArray answers = jsonObject.getJSONArray("items");
          for (int j = 0; j < answers.size(); j++) {
            JSONObject item = answers.getJSONObject(j);
            if (item != null) {
              answerList.add(item);
              AnswerIDList.add(item.getInteger("answer_id"));
            }
          }
        }
        nowQuestionIdList.clear();
      }
      JSONObject item = questionList.get(i);
      int questionId = item.getInteger("question_id");
      nowQuestionIdList.add(questionId);
    }
    if (nowQuestionIdList.size() > 0) {
      String ids = "";
      for (int j = 0; j < nowQuestionIdList.size(); j++) {
        ids += nowQuestionIdList.get(j);
        if (j != nowQuestionIdList.size() - 1) {
          ids += ";";
        }
      }
      List<JSONObject> jsonObjectList = getAnswers(ids);
      for (JSONObject jsonObject : jsonObjectList) {
        JSONArray answers = jsonObject.getJSONArray("items");
        for (int j = 0; j < answers.size(); j++) {
          JSONObject item = answers.getJSONObject(j);
          if (item != null) {
            answerList.add(item);
            AnswerIDList.add(item.getInteger("answer_id"));
          }
        }
      }
      nowQuestionIdList.clear();
    }
    // get comments
    List<Integer> nowAnswerIdList = new ArrayList<>();
    for (int i = 0; i < AnswerIDList.size(); i++) {
      if (i % 100 == 0 && i > 0) {
        String ids = "";
        for (int j = 0; j < nowAnswerIdList.size(); j++) {
          ids += nowAnswerIdList.get(j);
          if (j != nowAnswerIdList.size() - 1) {
            ids += ";";
          }
        }
        List<JSONObject> jsonObjectList = getComments(ids);
        for (JSONObject jsonObject : jsonObjectList) {
          JSONArray answers = jsonObject.getJSONArray("items");
          for (int j = 0; j < answers.size(); j++) {
            JSONObject item = answers.getJSONObject(j);
            if (item != null) commentList.add(item);
          }
        }
        nowAnswerIdList.clear();
      }
      JSONObject item = answerList.get(i);
      int answerId = item.getInteger("answer_id");
      nowAnswerIdList.add(answerId);
    }
    if (nowAnswerIdList.size() > 0) {
      String ids = "";
      for (int j = 0; j < nowAnswerIdList.size(); j++) {
        ids += nowAnswerIdList.get(j);
        if (j != nowAnswerIdList.size() - 1) {
          ids += ";";
        }
      }
      List<JSONObject> jsonObjectList = getComments(ids);
      for (JSONObject jsonObject : jsonObjectList) {
        JSONArray answers = jsonObject.getJSONArray("items");
        for (int j = 0; j < answers.size(); j++) {
          JSONObject item = answers.getJSONObject(j);
          if (item != null) commentList.add(item);
        }
      }
      nowAnswerIdList.clear();
    }
    // data insertion
    for (int i = 0; i < questionList.size(); i++) {
      JSONObject questionItem = questionList.get(i);
      databaseService.insertQuestionRecord(questionItem);
    }
    for (int i = 0; i < answerList.size(); i++) {
      JSONObject answerItem = answerList.get(i);
      databaseService.insertAnswerRecord(answerItem);
    }
    for (int i = 0; i < commentList.size(); i++) {
      JSONObject commentItem = commentList.get(i);
      databaseService.insertCommentRecord(commentItem);
    }
  }

  public List<JSONObject> getComments(String ids) {
    List<JSONObject> jsonObjectList = new ArrayList<>();
    int page = 1;
    while (true) {
      String url = "https://api.stackexchange.com/2.3/answers/" + ids + "/comments";
      String params = String.format("page=%d&pagesize=%d&filter=withbody&order=desc&sort=creation&site=stackoverflow&key=X5JIQM2Vlsnh85sg*BQ6OA((", page, pageSize);
      String apiURL = url + "?" + params;
      System.out.println(apiURL);
      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet(apiURL);
      try {
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          String responseBody = EntityUtils.toString(entity);
          JSONObject data = JSON.parseObject(responseBody);
          jsonObjectList.add(data);
          if (!data.getBoolean("has_more")) break;
        }
        response.close();
        httpClient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      page++;
    }
    return jsonObjectList;
  }

  public List<JSONObject> getAnswers(String ids) {
    List<JSONObject> jsonObjectList = new ArrayList<>();
    int page = 1;
    while (true) {
      String url = "https://api.stackexchange.com/2.3/questions/" + ids + "/answers";
      String params = String.format("page=%d&pagesize=%d&filter=withbody&order=desc&sort=activity&site=stackoverflow&key=X5JIQM2Vlsnh85sg*BQ6OA((", page, pageSize);
      String apiURL = url + "?" + params;
      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet(apiURL);
      try {
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          String responseBody = EntityUtils.toString(entity);
          JSONObject data = JSON.parseObject(responseBody);
          jsonObjectList.add(data);
          if (!data.getBoolean("has_more")) break;
        }
        response.close();
        httpClient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      page++;
    }
    return jsonObjectList;
  }
}

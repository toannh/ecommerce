package vn.chodientu.component;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.chodientu.entity.data.GroupFacebook;
import vn.chodientu.entity.output.Response;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.service.ParameterKeyService;

/**
 *
 * @author Phan Anh
 */
@Component
public class FacebookClient {

    @Autowired
    private Gson gson;
    @Autowired
    private Viewer viewer;
    @Autowired
    private LoadConfig config;
    private String call(String url, Map<String, Object> params, String token) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (token != null) {
            post.setHeader("Authorization", "Bearer " + token);
        }
        List<NameValuePair> urlParameters = new ArrayList<>();
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    urlParameters.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
        }
        post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            text.append(line);
        }

        return text.toString();
    }

    private String call(String url, JSONObject params, String token) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            post.setHeader("Authorization", "Bearer " + token);
        }
        StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            text.append(line);
        }

        return text.toString();
    }

    private String callGet(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            text.append(line);
        }
        return text.toString();
    }
    public String getLoginUrl(String cid) throws Exception {
        try {
            JSONObject res = new JSONObject(callGet(config.getUrlFacebookApp()+"/api/user/signin?cid=" + cid));
            String url=res.getString("data");
            return url;
        } catch (Exception ex) {
            return "";
        }
    }
    public List<GroupFacebook> getGroup(String fid,String after) throws Exception {
//        try {
            Map<String, Object> params = new HashMap<>();
            params.put("fid", fid);
            if(after !=null){
            params.put("after", after);}
            List<GroupFacebook> list=new ArrayList<>();
            JSONObject res = new JSONObject(call(config.getUrlFacebookApp()+"/api/default/groups", params, null));
            JSONArray jArray = res.getJSONArray("data");
                for (int i = 0; i < jArray.length(); i++) {
                    String name = jArray.getJSONObject(i).getString("name");
                    String id = jArray.getJSONObject(i).getString("id");
                    GroupFacebook groupFacebook = new GroupFacebook(name, id);
                    list.add(groupFacebook);
                }
                return list;

//        } catch (Exception ex) {
//            throw new Exception("Lỗi");
//        }
    }
    
    public String booking(String message,String link,String name, String caption,String description,String image,String productId, String groups,String facebookId,String jobId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("message", message);
            params.put("link", link);
            params.put("name", name);
            params.put("caption", caption);
            params.put("description", description);
            params.put("image", image);
            params.put("productId", productId);
            params.put("groups", groups);
            params.put("facebookId", facebookId);
            params.put("jobId", jobId);
            JSONObject res = new JSONObject(call(config.getUrlFacebookApp()+"/api/default/booking", params, null));
                return res.getString("data");

        } catch (Exception ex) {
            throw new Exception("Lỗi");
        }
    }
    public List<String> getStatus(String fid,String pid,String jobId) throws Exception {
        try {
            JSONObject res = new JSONObject(callGet(config.getUrlFacebookApp()+"/api/result/success?fid=" + fid+"&pid="+pid+"&jobId="+jobId));
            int count=res.getInt("count");
            List<String> list=new ArrayList<>();
            JSONArray jArray = res.getJSONArray("list");
                for (int i = 0; i < jArray.length(); i++) {
                    String url = "https://www.facebook.com/"+jArray.getString(i);
                    
                    list.add(url);
                }
            return list;
        } catch (Exception ex) {
            return null;
        }
    }
    

}

package ceng453.frontend.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class RequestHandler {
    private static final String URL = "http://localhost:8080/api/";
    private String token = "";

    private static final class InstanceHolder {
        private static final RequestHandler instance = new RequestHandler();
    }

    /** get the instance of the class
     *
     * @return instance of the request handler
     */
    public static RequestHandler getRequestHandler(){
        //instance will be created at request time
        return InstanceHolder.instance;
    }

    /** POST request to the server
     *
     * @param token the token to be set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /** POST request to the server
     *
     * @param jsonObject the json object to be sent
     * @param endpoint  the endpoint to send the request to
     * @return the response from the server
     * @throws IOException when the request fails
     * @throws JSONException when the response is not a valid json
     */
    public JSONObject postRequest(JSONObject jsonObject, String endpoint) throws IOException, JSONException {
        HttpPost post = new HttpPost(URL + endpoint);
        post.addHeader("content-type", "application/json");
        post.addHeader("Authorization", "Bearer " + this.token);
        post.setEntity(new StringEntity(jsonObject.toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);

        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
        return new JSONObject(result);
    }

    /** GET request to the server
     *
     * @param endpoint the endpoint to send the request to
     * @return the response from the server
     * @throws IOException when the request fails
     * @throws JSONException when the response is not a valid json
     */
    public JSONObject getRequest(List<NameValuePair> nameValuePairs, String endpoint) throws IOException, JSONException, URISyntaxException {
        URIBuilder builder = new URIBuilder(URL + endpoint);
        builder.setParameters(nameValuePairs);

        HttpGet get = new HttpGet(builder.build());
        get.addHeader("Authorization", "Bearer " + this.token);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);

        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
        return new JSONObject(result);
    }
}

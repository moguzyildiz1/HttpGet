import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

	private final static String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) {
				
		try {
			for(String title : getMovieTitles("Spiderman")){
				System.out.println("* "+title+" *");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String[] getMovieTitles(String subStr) throws IOException {

		String url = "https://jsonmock.hackerrank.com/api/movies/search/?Title="+subStr;
		List<String> result = new ArrayList<>();
		List<String> result1 = new ArrayList<>();

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//System.out.println("Response : "+response.toString());

		//1st way to get JSon data from server
		Gson gson = new Gson();
		PagePojo mtp=(PagePojo)  gson.fromJson(response.toString(),PagePojo.class);
		
		for(MovieTitlePojo movieTitle : mtp.getData()) {
			//System.out.println("Movie Title : "+movieTitle.getTitle());
			result.add(movieTitle.getTitle());
		}
		
		// 2nd way to get Json data from server
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = (JsonElement) parser.parse(response.toString());
		
		//System.out.println("Total : "+jsonElement.getAsJsonObject().get("data").getAsJsonArray());
		
		JsonArray jArr = jsonElement.getAsJsonObject().get("data").getAsJsonArray();
		
		for(JsonElement title : jArr){
			result1.add(title.getAsJsonObject().get("Title").toString());
		}
		
		System.out.println("Result: "+result.toString());
		System.out.println("Result1: "+result1.toString());
		
		//System.out.println("Title : "+jArr.get(0).getAsJsonObject().get("Title"));
		String[] res=new String[result.size()];
		
		for(int i=0;i<result.size();++i){
			res[i]=result.get(i);
		}
		Arrays.sort(res);
		return res;
	}

}

package model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class Highscore {
	public static void pushScore(String name, int score) throws IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost("http://jtowerdefense.appspot.com/scores");
			List<NameValuePair> nameAndScore = new ArrayList<NameValuePair>();
			nameAndScore.add(new BasicNameValuePair("name", name));
			nameAndScore.add(new BasicNameValuePair("score", Integer.toString(score)));
			UrlEncodedFormEntity nAS = new UrlEncodedFormEntity(nameAndScore, StandardCharsets.UTF_8);
			httppost.setEntity(nAS);
			httpclient.execute(httppost);
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getHighscores() throws IOException {
		Document doc = Jsoup.connect("http://jtowerdefense.appspot.com/leaderboard").get();
		Elements names = doc.select("div.name");
		Elements scores = doc.select("div.score");
		List<String> data0 = new ArrayList<String>();
		for(Element name : names) {
			data0.add(name.getAllElements().text());
		}
		for(Element score : scores)
			data0.add(score.getAllElements().text());
		return data0;
	}
	
}


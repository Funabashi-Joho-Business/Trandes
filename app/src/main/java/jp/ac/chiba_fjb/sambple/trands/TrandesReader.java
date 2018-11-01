package jp.ac.chiba_fjb.sambple.trands;

import android.os.Handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@JsonIgnoreProperties(ignoreUnknown=true)

class TrandesReader{
	interface TrandeListener{
		void onTrande(TrandeData trandes);
	}
	static class Trandes{
		@JsonProperty("default")
		public TrandeData d;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class TrandeData{
		public TrandingSerhesDay[] trendingSearchesDays;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class TrandingSerhesDay{
		@JsonFormat(pattern = "yyyyMMdd")
		public Date date;
		public String formattedDate;
		public TrendingSearche[] trendingSearches;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class TrendingSearche{
		public Title title;

	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Title{
		public String query;
	}
	public static String getContent(String adr){
		try {
			URL url = new URL(adr);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			//con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6");
			//con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);

			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String str;
			while ( null != ( str = br.readLine() ) ) {
				sb.append(str+"\n");
			}
			br.close();
			con.disconnect();

			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static TrandeData getTrande(Date date){
		try {
			if(date == null)
				date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String values = getContent("https://trends.google.com/trends/api/dailytrends?geo=JP&ed="+sdf.format(date));
			if(values == null)
				return null;
			values = values.substring(6, values.length());
			ObjectMapper mapper = new ObjectMapper();
			Trandes t = mapper.readValue(values,Trandes.class);
			return t.d;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void recvTrandes(final Date date, final TrandeListener listener){
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				final TrandeData trande = getTrande(date);
				handler.post(new Runnable(){
					@Override
					public void run() {
						listener.onTrande(trande);
					}
				});
			}
		}.start();
	}
}

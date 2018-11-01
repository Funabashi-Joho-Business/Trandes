package jp.ac.chiba_fjb.sambple.trands;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TrandesReader.TrandeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TrandesReader.recvTrandes(null,this);
	}

	@Override
	public void onTrande(TrandesReader.TrandeData trandes) {
		TextView textView = findViewById(R.id.output);
		if(trandes == null) {
			textView.setText("エラー");
			return;
		}
		textView.setText("");
		for(int j=0;j<trandes.trendingSearchesDays.length;j++){
			TrandesReader.TrandingSerhesDay days = trandes.trendingSearchesDays[j];

			textView.append(days.formattedDate+"\n");
			for(int i=0;i< days.trendingSearches.length;i++){
				TrandesReader.TrendingSearche search = days.trendingSearches[i];
				textView.append(search.title.query+"\n");
			}
			textView.append("\n");
		}
	}
}

package jp.ac.chiba_fjb.sambple.trands;

import org.junit.Test;

public class ExampleUnitTest {
	@Test   //←これを書いたメソッドが実行される
	public void Main(){
		TrandesReader.TrandeData trande = TrandesReader.getTrande(null);
		if(trande != null){
			for(int j=0;j<trande.trendingSearchesDays.length;j++){
				TrandesReader.TrandingSerhesDay days = trande.trendingSearchesDays[j];
				System.out.println(days.formattedDate);
				for(int i=0;i< days.trendingSearches.length;i++){
					TrandesReader.TrendingSearche search = days.trendingSearches[i];
					System.out.print(search.title.query+" ");
				}
				System.out.println();
			}
		}
	}

}
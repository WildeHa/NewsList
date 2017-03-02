package com.example.newslist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

/**
 * ͨ���첽���أ���������UI�߳�
 * ͨ��LruCache����������ͼƬ�ŵ��ڴ��У�һ�����棩
 * ͨ���ж�ListView�Ļ���״̬��������ʱ����ͼƬ���ø��ӵ�ListViewҲ�������ļ��أ�
 * ��������ListView���κοؼ�������ʹ���첽����
 * @author Arvin
 *
 */
public class MainActivity extends Activity {
	
	private ListView mListView;
	private static String URL = 
			"http://www.imooc.com/api/teacher?type=4&num=30";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListView = (ListView) findViewById(R.id.lv_main);
		new NewsAsyncTask().execute(URL);
	}
	
	class NewsAsyncTask extends AsyncTask<String, Void, List<NewsBean>>
	{
		/**
		 * ʵ��������첽����
		 */
		@Override
		protected List<NewsBean> doInBackground(String... params) {
			return getJosnData(params[0]);
		}
		
		@Override
		protected void onPostExecute(List<NewsBean> newsbean) {
			// TODO Auto-generated method stub
			super.onPostExecute(newsbean);
			NewsAdapter adapter = new NewsAdapter(newsbean, MainActivity.this, mListView);
			mListView.setAdapter(adapter);
		}

		/**
		 * ͨ��InputStream������ҳ���ص�����
		 * @param is
		 * @return
		 */
		private String readString(InputStream is)
		{
			InputStreamReader isr;
			String result = "";
			try {
				String line = "";
				//�ֽ���ת��Ϊ�ַ���
				isr = new InputStreamReader(is,"utf-8");		
				//��Buffer����ʽ��ȡ
				BufferedReader br = new BufferedReader(isr);
				while((line = br.readLine()) != null)
				{
					result += line;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}
		/**
		 * ��url��Ӧ��JSON��ʽ����ת��Ϊ��������װ��NewsBean����
		 * @param url
		 * @return
		 */
		private List<NewsBean> getJosnData(String url) {
			List<NewsBean> newsBeanList = new ArrayList<NewsBean>();
			try {
				String jsonString = readString(new URL(url).
						openConnection().getInputStream());
//				String jsonString = readString(new URL(url).openStream());
//				Log.i("arvin", jsonString);
				JSONObject jsonObject;			
				try {	
					jsonObject = new JSONObject(jsonString);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i ++)
					{
						jsonObject = jsonArray.getJSONObject(i);
						NewsBean newsBean = new NewsBean();
						newsBean.setNewsIconUrl(jsonObject.getString("picSmall"));
						newsBean.setNewsTitle(jsonObject.getString("name"));
						newsBean.setNewsContent(jsonObject.getString("description"));	
						newsBeanList.add(newsBean);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();	
			}
			return newsBeanList;
		}
		
	}
}

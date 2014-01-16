package br.com.challengeaccepted.webservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class WebserviceActions {

	private WebserviceActions() { }
	
	private static HttpClient createHttpClient() {
	    HttpParams params = new BasicHttpParams();
	    
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
	    HttpProtocolParams.setUseExpectContinue(params, true);
	    
	    SchemeRegistry schReg = new SchemeRegistry();
	    
	    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
	    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

	    return new DefaultHttpClient(conMgr, params);
	}
	
	public static HttpResponse doPost(String protocol, String host, int port, String path,
			JSONObject jsonEntity, Header... headers) {
		
		return doPost(protocol, host, port, path, jsonEntity.toString(), headers);
	}
	
	
	public static HttpResponse doPost(String protocol, String host, int port, String path,
			String stringEntity, Header... headers) {
		
		URI uri = null;
		HttpResponse response = null;
		
		try {
			uri = URIUtils.createURI(protocol, host, port, path, null, null);
			HttpPost post = new HttpPost(uri);
			post.setHeaders(headers);
			post.setEntity(new StringEntity(stringEntity, "UTF-8"));
			HttpClient client = createHttpClient();
			response = client.execute(post);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static HttpResponse doPut(String protocol, String host, int port, String path, 
			JSONObject jsonEntity, Header... headers) {
		URI uri = null;
		HttpResponse response = null;
		
		try {
			uri = URIUtils.createURI(protocol, host, port, path, null, null);
			HttpPut put = new HttpPut(uri);
			put.setHeaders(headers);
			put.setEntity(new StringEntity(jsonEntity.toString(), "UTF-8"));
			HttpClient client = createHttpClient();
			response = client.execute(put);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static HttpResponse doGet(String protocol, String host, int port, String path, 
			List<NameValuePair> query, Header... headers) {
		
		URI uri = null;
		HttpResponse response = null;
		
		try {
			uri = URIUtils.createURI(protocol, host, port, path, 
					query == null ? null : URLEncodedUtils.format(query, "UTF-8"), null);
			HttpGet get = new HttpGet(uri);
			get.setHeaders(headers);
			HttpClient client = createHttpClient();
			response = client.execute(get);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static HttpResponse doDelete(String protocol, String host, int port, String path, 
			Header... headers) {
		URI uri = null;
		HttpResponse response = null;
		
		try {
			uri = URIUtils.createURI(protocol, host, port, path, null, null);
			HttpDelete del = new HttpDelete(uri);
			del.setHeaders(headers);
			HttpClient client = createHttpClient();
			response = client.execute(del);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
}
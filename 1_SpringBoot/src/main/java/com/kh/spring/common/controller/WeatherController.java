package com.kh.spring.common.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WeatherController {
	@GetMapping(value = "getWeather.do", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String getWeather() {
		StringBuilder sb = new StringBuilder();
		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=JrqTSJnDJKcVxSsA00G8Zx%2Bo%2BAhBZ7iZHMLXzFheQFJ50cA7%2BLzekCqN88C34wX44igZ7i71VW9lx6ZMO%2FVxZw%3D%3D"); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
	        
	        //오늘 날짜와 시간 추출 코드
	        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd HHmm");
	        String now = sdf.format(new Date());
	        String[] dayTime = now.split(" ");
	        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(dayTime[0], "UTF-8")); /*‘21년 6월 28일발표*/
	        
	        int[] baseTime = {200, 500, 800, 1100, 1400, 1700 ,2000 ,2300};
	        int index = 99;
	        for(int i=0; i<baseTime.length; i++) {
	        	if(Integer.parseInt(dayTime[1]) <= baseTime[i]) {
	        		index = i - 1;
	        		
	        		if(i == 0) {
	        			index = i;
	        		}
	        		
	        		dayTime[1] = ("0" + baseTime[index]).substring(("0" + baseTime[index]).length()-4, ("0" + baseTime[index]).length());
	        		break;
	        	}
	        }
	        
	        if(index == 99) {
	        	dayTime[1] = "2300";
	        }
	        
	        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(dayTime[1], "UTF-8")); /*05시 발표*/
	        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("60", "UTF-8")); /*예보지점의 X 좌표값*/
	        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
	        URL url = new URL(urlBuilder.toString());
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        System.out.println("Response code: " + conn.getResponseCode());
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        String line;
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        rd.close();
	        conn.disconnect();
	        
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}

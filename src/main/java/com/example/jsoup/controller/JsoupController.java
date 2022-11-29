package com.example.jsoup.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsoupController {
	// 썸네일 링크 : 데이터 자체를 파일이 아닌 url로 가져옴
	@GetMapping("/thumbnail")
	public String thumbnail() {
		final String inflearnUrl = "https://www.inflearn.com/courses";
		// 여기부터 시작
		Connection conn = Jsoup.connect(inflearnUrl);
		StringBuilder sb = new StringBuilder();
		try {
			// 최상위 객체인 document를 모두 가져옴
			Document document = conn.get();
			// 복수의 element를 for문을 이용해 하나씩 가져와 빌더에 넣음 : url에서 이미지 태그 자체를 가져옴
			Elements imgUrlElements = document.getElementsByClass("swiper-lazy");
			for (Element element : imgUrlElements) {
				sb.append(element.attr("abs:src")+"<br>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 문자열로 리턴하면 browser가 받음
		return sb.toString();
	}

	// 타이틀 가져오기
	@GetMapping("/title")
	public String title() {
		final String inflearnUrl = "https://www.inflearn.com/courses";
		// 여기부터 시작
		Connection conn = Jsoup.connect(inflearnUrl);
		StringBuilder sb = new StringBuilder();
		try {
			// 최상위 객체인 document를 모두 가져옴
			Document document = conn.get();
			// 복수의 element를 for문을 이용해 하나씩 가져와 빌더에 넣음 : url에서 이미지 태그 자체를 가져옴
			Elements titleElements = document.getElementsByClass("course_title");
			for (Element element : titleElements) {
				sb.append(element);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}

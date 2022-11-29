package com.example.jsoup.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

//@RestController
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
				// 이미지 속성 src만 가져옴
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
	public String course_title() {
		final String inflearnUrl = "https://www.inflearn.com/courses";
		// 여기부터 시작
		Connection conn = Jsoup.connect(inflearnUrl);
		StringBuilder sb = new StringBuilder();
		try {
			// 최상위 객체인 document를 모두 가져옴
			Document document = conn.get();
			// 복수의 element를 for문을 이용해 하나씩 가져와 빌더에 넣음 : url에서 이미지 태그 자체를 가져옴
//			Elements titleElements = document.getElementsByClass("course_title");
			// div, p 태그에 중복되어 있음 => div태그의 course_title만 가져오는 법
			Elements titleElements = document.select("div.card-content>div.course_title");
			for (Element element : titleElements) {
				sb.append(element.text()+"<br>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// 가격 가져오기 : 원데이터 형태에서 원, (,) 제외하기 위한 함수 필요
	@GetMapping("/price")
	public String saleprice() {
		final String inflearnUrl = "https://www.inflearn.com/courses";
		Connection conn = Jsoup.connect(inflearnUrl);
		StringBuilder sb = new StringBuilder();
		try {
			Document document = conn.get();
			Elements priceElement = document.select("div.price");
			for (Element element : priceElement) {
				String price = element.text();
				String realPrice = getRealPrice(price);
				String salePrice = getSalePrice(price);
				int nrealPrice = toInt(realPrice);
				int nsalePrice = toInt(salePrice);
				sb.append("가격:"+nrealPrice);
				if(nrealPrice!=nsalePrice){
					sb.append("&nbsp; 할인가격:"+nsalePrice);
				}
				sb.append("<br>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getRealPrice(String price){
		return price.split(" ")[0];
	}

	private String getSalePrice(String price){
		String[] prices = price.split(" ");
		// split 했을 때 1개이면 realprice = saleprice / 1이 아니면 prices[1]
		// 조건 ? true : false
		return prices.length==1?prices[0]:prices[1];
	}

	private int toInt(String str){
		str = str.replaceAll("₩", "");
		str = str.replaceAll(",", "");
		return Integer.parseInt(str);
	}

	// 상세페이지 링크 통해서 평점 가져오기
	@GetMapping("/link")
	public String course_link() {
		final String inflearnUrl = "https://www.inflearn.com/courses";
		// 여기부터 시작
		Connection conn = Jsoup.connect(inflearnUrl);
		StringBuilder sb = new StringBuilder();
		try {
			Document document = conn.get();
			Elements linkElements = document.select("a.course_card_front");
			for (Element element : linkElements) {
				String innerUrl = element.attr("abs:href");
				Connection innerConn = Jsoup.connect(innerUrl);
				Document innerDoc = innerConn.get();
				// return 타입 하나만 가져오게..?
				Element ratingElement = innerDoc.selectFirst("div.dashboard-star__num");
//				Element ratingElement = innerDoc.select("div.dashboard-star__num").get(0);

				// wrapper class : 변수를 class로, (소수점 있는)문자열을 자기 함수로 바꿔주는 역할
				// double(데이터타입) Double(클래스)
				// 평점이 없을 경우(isNull) 0.0으로 초기화
				double rating = Objects.isNull(ratingElement)? 0.0:Double.parseDouble(ratingElement.text());
				rating = Math.round(rating*10)/10.0;
				sb.append(innerUrl+", 평점:" +rating+"<br>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// 마우스 오버 내용 가져오기 : 강의자, 강의 부가설명, 기술스택
	@GetMapping("/etc")
	public String course_etc() {
		final String inflearnUrl = "https://www.inflearn.com/courses";

		Connection conn = Jsoup.connect(inflearnUrl);
		StringBuilder sb = new StringBuilder();
		try {
			Document document = conn.get();
			Elements instructorElements = document.getElementsByClass("instructor");
			Elements descriptionElements = document.select("p.course_description");
			Elements skillElements = document.select("div.course_skills>span");

			for (int i=0; i<instructorElements.size(); i++) {
				String instructor = instructorElements.get(i).text();
				String description = descriptionElements.get(i).text();
				String skills = skillElements.get(i).text().replace("\\s","");

				sb.append("강의자: "+instructor+"<br>");
				sb.append("강의 부가설명: "+description+"<br>");
				sb.append("기술스택: "+skills+"<br><br>");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}


}

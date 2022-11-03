package KQSX;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StagingSource {
	Document doc = Jsoup.connect("https://www.kqxs.vn/").timeout(6000).get();
	Elements result = doc.select("div.block-result");
	
	public StagingSource() throws IOException {
		
	}
	
	public String getDate() {
		Elements eDate = result.select("table.tbldata#result_1 thead tr");
		String date = eDate.select("th").text().replace("Xổ số Miền Bắc ", "").replace("Thứ Hai ", "")
				.replace("Thứ Ba ", "").replace("Thứ Tư ", "").replace("Thứ Năm ", "").replace("Thứ Sáu ", "")
				.replace("Thứ Bảy ", "").replace("Chủ Nhật ", "").replace("-", "/");
		return date;
	}
	
	public void kqxsMB() {
		
	}
	
	//Print to txt
	public void printTxt(String path) throws IOException {
		//Check path
		File file = new File(path);
		if(!file.exists()) {
			System.out.println("This file doesn't exist");
		}
		if(file.isDirectory()) {
			file = new File(path + "\\PlainText.txt");
		}
		
		// Write in Text file
		FileWriter fw = new FileWriter(file);
		PrintWriter pw = new PrintWriter(fw);

		// Connect to website
		Document doc = Jsoup.connect("https://www.kqxs.vn/").timeout(6000).get();
		Elements result = doc.select("div.block-result");

		// Get date
		pw.println(this.getDate());

		/*
		 * Kết quả xổ số miền Bắc
		 */
		Elements mb = result.select("table.tbldata#result_1");
		pw.println("----------");
		pw.println("Miền Bắc");
		Elements prizesMB = mb.select("tbody");
		outer:
			for (int prize = 0; prize < 8; prize++) {
			String prizeName, prizeNumber;
			List<String> prizeNumberList = new ArrayList<String>();
			Elements row = prizesMB.select("tr:eq(" + prize + ")");
			prizeName = row.select("td.prize").text();
			pw.println(prizeName);
			inner:
				for (int number = 0; number < 6; number++) {
				prizeNumber = row.select("td.results span.number:eq(" + number + ")").text();
				if (!prizeNumber.isBlank()) {
					prizeNumberList.add(prizeNumber);
				}
			}
			innerList: for (int i = 0; i < prizeNumberList.size(); i++) {
				if (i != prizeNumberList.size() - 1) {
					pw.print(prizeNumberList.get(i) + ", ");
				} else {
					if(prize == 7) {
						pw.print(prizeNumberList.get(i));
						pw.println();
					} else {
						pw.print(prizeNumberList.get(i) + "\n");
					}
				}
			}
		}
		
		/*
		 * Kết quả xổ số miền Nam
		 */
		Elements mn = result.select("table.tbldata#result_2");
		pw.println("----------");
		pw.println("Miền Nam");

		// Lấy tên nhà đài
		Elements stationsMN = mn.select("tbody tr").first().select("td.results div.quantity-of-number");
		List<String> stationListMN = new ArrayList<String>();
		for (Element station : stationsMN) {
			String stationName;
			stationName = stationsMN.select("span.wrap-text:eq(0)").text();
			stationListMN.add(stationName);
			stationName = stationsMN.select("span.wrap-text:eq(1)").text();
			stationListMN.add(stationName);
			stationName = stationsMN.select("span.wrap-text:eq(2)").text();
			stationListMN.add(stationName);
			stationName = stationsMN.select("span.wrap-text:eq(3)").text();
			stationListMN.add(stationName);
		}
		for (int i = 0; i < stationListMN.size(); i++) {
			if (stationListMN.get(i).isBlank()) {
				stationListMN.remove(i);
			}
		}

		// Lấy kết quả xổ số
		Elements prizesMN = mn.select("tbody");
		for (int station = 0; station < stationListMN.size(); station++) {
			if (station != 0) {
				pw.println();
			}
			pw.println(stationListMN.get(station));
			outer: for (int prize = 9; prize > 0; prize--) {
				String prizeName, prizeNumber;
				List<String> prizeNumberList = new ArrayList<String>();
				Elements row = prizesMN.select("tr:eq(" + prize + ")");
				prizeName = row.select("td.prize").text();
				pw.println(prizeName);
				inner: for (int number = 0; number < 7; number++) {
					int position = station + number * stationListMN.size();
					prizeNumber = row.select("td.results span.number:eq(" + position + ")").text();
					if (!prizeNumber.isBlank()) {
						prizeNumberList.add(prizeNumber);
					}
				}
				innerList: for (int i = 0; i < prizeNumberList.size(); i++) {
					if (i != prizeNumberList.size() - 1) {
						pw.print(prizeNumberList.get(i) + ", ");
					} else {
						pw.print(prizeNumberList.get(i) + "\n");
					}
				}
			}
		}

		/*
		 * Kết quả xổ số miền Trung
		 */
		Elements mt = result.select("table.tbldata#result_3");
		pw.println("----------");
		pw.println("Miền Trung");

		// Lấy tên nhà đài
		Elements stationsMT = mt.select("tbody tr").first().select("td.results div.quantity-of-number");
		List<String> stationListMT = new ArrayList<String>();
		for (Element station : stationsMT) {
			String stationName;
			stationName = stationsMT.select("span.wrap-text:eq(0)").text();
			stationListMT.add(stationName);
			stationName = stationsMT.select("span.wrap-text:eq(1)").text();
			stationListMT.add(stationName);
			stationName = stationsMT.select("span.wrap-text:eq(2)").text();
			stationListMT.add(stationName);
		}
		for (int i = 0; i < stationListMT.size(); i++) {
			if (stationListMT.get(i).isBlank()) {
				stationListMT.remove(i);
			}
		}

		// Lấy kết quả xổ số
		Elements prizes = mt.select("tbody");
		for (int station = 0; station < stationListMT.size(); station++) {
			if (station != 0) {
				pw.println();
			}
			pw.println(stationListMT.get(station));
			outer: for (int prize = 9; prize > 0; prize--) {
				String prizeName, prizeNumber;
				List<String> prizeNumberList = new ArrayList<String>();
				Elements row = prizes.select("tr:eq(" + prize + ")");
				prizeName = row.select("td.prize").text();
				pw.println(prizeName);
				inner: for (int number = 0; number < 7; number++) {
					int position = station + number * stationListMT.size();
					prizeNumber = row.select("td.results span.number:eq(" + position + ")").text();
					if (!prizeNumber.isBlank()) {
						prizeNumberList.add(prizeNumber);
					}
				}
				innerList: for (int i = 0; i < prizeNumberList.size(); i++) {
					if (i != prizeNumberList.size() - 1) {
						pw.print(prizeNumberList.get(i) + ", ");
					} else {
						pw.print(prizeNumberList.get(i) + "\n");
					}
				}
			}
		}
		
		pw.close();
		System.out.println("Print successfully");
	}
	
	public static void main(String[] args) throws IOException {
		StagingSource ss = new StagingSource();
		System.out.println(ss.getDate());
	}
}

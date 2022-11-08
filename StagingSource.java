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
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    List<Object> kqxs = new ArrayList<Object>();
	
	Document doc = Jsoup.connect("https://www.kqxs.vn").timeout(6000).get();
	Elements dataSource = doc.select("div.block-results");
	
	public StagingSource() throws IOException {
		kqxs.add(getKQSXMB());
		kqxs.add(getKQSXMN());
		kqxs.add(getKQSXMT());
	}
	
	public List<Object> getKqxs() {
		return kqxs;
	}
	
	public String getDate() {
		Elements eDate = dataSource.select("table.tbldata#result_1 thead tr");
		String date = eDate.select("th").text().toLowerCase().replace("xổ số miền bắc ", "").replace("thứ hai ", "")
				.replace("thứ ba ", "").replace("thứ tư ", "").replace("thứ năm ", "").replace("thứ sáu ", "")
				.replace("thứ bảy ", "").replace("chủ nhật ", "").replace("-", "/");
		return date;
	}
	
	// Lấy KQXS miền Bắc
	public List<List<String>> getKQSXMB() {
		List<List<String>> result = new ArrayList<List<String>>();
		
		Elements mb = dataSource.select("table.tbldata#result_1");
		Elements prizesMB = mb.select("tbody");
		
		List<String> subList = new ArrayList<String>();
		subList.add("Miền Bắc");
		subList.add(getDate());
		result.add(subList);
		outer:
			for (int prize = 0; prize < 8; prize++) {
			String prizeNumber;
			List<String> prizeNumberList = new ArrayList<String>();
			Elements row = prizesMB.select("tr:eq(" + prize + ")");
			inner:
				for (int number = 0; number < 6; number++) {
				prizeNumber = row.select("td.results span.number:eq(" + number + ")").text();
				if (!prizeNumber.isBlank()) {
					prizeNumberList.add(prizeNumber);
				}
			}
			result.add(prizeNumberList);
		}
		List<String> emptyList = new ArrayList<String>();
		emptyList.add("-");
		result.add(emptyList);
		
		return result;
	}
	
	// Lấy KQSX miền Nam
	public List<List<List<String>>> getKQSXMN() {
		List<List<List<String>>> result = new ArrayList<List<List<String>>>();
		Elements mn = dataSource.select("table.tbldata#result_2");
		
		// Lấy tên nhà đài
		Elements stationsMN = mn.select("tbody tr").first().select("td.results");
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
			//Tạo KQXS của từng đài
			List<List<String>> stationResultList = new ArrayList<List<String>>();
			List<String> subList = new ArrayList<String>();
			subList.add(stationListMN.get(station));
			subList.add(getDate());
			stationResultList.add(subList);
			
			outer: for (int prize = 9; prize > 0; prize--) {
				String prizeNumber;
				List<String> prizeNumberList = new ArrayList<String>();
				Elements row = prizesMN.select("tr:eq(" + prize + ")");
				inner: for (int number = 0; number < 7; number++) {
					int position = station + number * stationListMN.size();
					prizeNumber = row.select("td.results span.number:eq(" + position + ")").text();
					if (!prizeNumber.isBlank()) {
						prizeNumberList.add(prizeNumber);
					}
				}
				stationResultList.add(prizeNumberList);
			}
			result.add(stationResultList);
		}
		return result;
	}
	
	//Lấy KQXS miền Trung
	public List<List<List<String>>> getKQSXMT() {
		List<List<List<String>>> result = new ArrayList<List<List<String>>>();
		Elements mt = dataSource.select("table.tbldata#result_3");
		
		// Lấy tên nhà đài
		Elements stationsMT = mt.select("tbody tr").first().select("td.results");
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
		Elements prizesMT = mt.select("tbody");
		for (int station = 0; station < stationListMT.size(); station++) {
			//Tạo KQXS của từng đài
			List<List<String>> stationResultList = new ArrayList<List<String>>();
			List<String> subList = new ArrayList<String>();
			subList.add(stationListMT.get(station));
			subList.add(getDate());
			stationResultList.add(subList);
			
			outer: for (int prize = 9; prize > 0; prize--) {
				String prizeNumber;
				List<String> prizeNumberList = new ArrayList<String>();
				Elements row = prizesMT.select("tr:eq(" + prize + ")");
				inner: for (int number = 0; number < 7; number++) {
					int position = station + number * stationListMT.size();
					prizeNumber = row.select("td.results span.number:eq(" + position + ")").text();
					if (!prizeNumber.isBlank()) {
						prizeNumberList.add(prizeNumber);
					}
				}
				stationResultList.add(prizeNumberList);
			}
			result.add(stationResultList);
		}
		return result;
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

		// Get date
		pw.println(this.getDate());

		/*
		 * Kết quả xổ số miền Bắc
		 */
		pw.println("----------");
		List<List<String>> listMB = this.getKQSXMB();
		//Tên nhà đài
		pw.println(listMB.get(0).get(0));
		//Giải thưởng
		for (int i = 0; i < listMB.size()-1; i++) {
			String prizeName = dataSource.select("table.tbldata#result_1 tbody	tr:eq(" + i + ") td.prize").text();
			pw.println(prizeName);
			for (int j = 0; j < listMB.get(i+1).size(); j++) {
				if(j==listMB.get(i+1).size() - 1) {
					pw.print(listMB.get(i+1).get(j));
				} else {
					pw.print(listMB.get(i+1).get(j) + ", ");
				}
			}
			pw.println();
		}
		
		/*
		 * Kết quả xổ số miền Nam
		 */
		pw.println("----------");
		List<List<List<String>>> listMN = this.getKQSXMN();
		for (int i = 0; i < listMN.size(); i++) {
			pw.println(listMN.get(i).get(0).get(0));
			for (int j = 9; j > 0; j--) {
				String prizeName = dataSource.select("table.tbldata#result_2 tbody	tr:eq(" + j + ") td.prize").text();
				pw.println(prizeName);
				for (int k = 0; k < listMN.get(i).get(j).size(); k++) {
					if(k == listMN.get(i).get(j).size() - 1) {
						pw.print(listMN.get(i).get(j).get(k));
					} else {
						pw.print(listMN.get(i).get(j).get(k) + ", ");
					}
				}
				pw.println();
			}
			if(i != listMN.size() - 1) {
				pw.println();
			}
		}

		/*
		 * Kết quả xổ số miền Trung
		 */
		pw.println("----------");
		List<List<List<String>>> listMT = this.getKQSXMT();
		for (int i = 0; i < listMT.size(); i++) {
			pw.println(listMT.get(i).get(0).get(0));
			for (int j = 9; j > 0; j--) {
				String prizeName = dataSource.select("table.tbldata#result_3 tbody	tr:eq(" + j + ") td.prize").text();
				pw.println(prizeName);
				for (int k = 0; k < listMT.get(i).get(j).size(); k++) {
					if(k == listMT.get(i).get(j).size() - 1) {
						pw.print(listMT.get(i).get(j).get(k));
					} else {
						pw.print(listMT.get(i).get(j).get(k) + ", ");
					}
				}
				pw.println();
			}
			if(i != listMT.size() - 1) {
				pw.println();
			}
		}
		
		pw.close();
		System.out.println("Print successfully");
	}
	
	//Print to CSV
	public void printCSV(String path) {
		File file = new File(path);
		if(!file.exists()) {
			System.out.println("This is not a file");
		}
		if(file.isDirectory()) {
			String date = this.getDate().replace("/", "-");
			file = new File(path + "\\KQXS - " + date + ".csv");
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			List<List<List<String>>> dataList = new ArrayList<List<List<String>>>();
			
			List<List<String>> listMB = (List<List<String>>) kqxs.get(0);
			dataList.add(listMB);
			
			List<List<List<String>>> listMN = (List<List<List<String>>>) kqxs.get(1);
			for (int mn = 0; mn < listMN.size(); mn++) {
				dataList.add(listMN.get(mn));
			}
			
			List<List<List<String>>> listMT = (List<List<List<String>>>) kqxs.get(2);
			for (int mt = 0; mt < listMT.size(); mt++) {
				dataList.add(listMT.get(mt));
			}
			
			int count = 1;
			for (List<List<String>> list : dataList) {
				fw.append(Integer.toString(count));	count++;
				fw.append(COMMA_DELIMITER);
				String station = list.get(0).get(0);
				String nonUTFStation = this.replaceUTF8(station);
				fw.append(nonUTFStation);
				fw.append(COMMA_DELIMITER);
				fw.append(list.get(0).get(1));
				fw.append(COMMA_DELIMITER);
				for (int i = 1; i < list.size(); i++) {
					fw.append("\"");
					for (int j = 0; j < list.get(i).size(); j++) {
						if(j != list.get(i).size() - 1) {
							fw.append(list.get(i).get(j) + ", ");
						} else {
							fw.append(list.get(i).get(j));
						}
						
					}
					fw.append("\"");
					fw.append(COMMA_DELIMITER);
				}
				fw.append("da quet");
				fw.append(NEW_LINE_SEPARATOR);
			}
			
			System.out.println("CSV file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
		} finally {
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
	}
	
	//Replace UTF-8 characters
	public String replaceUTF8(String str) {
		String result;
		result = str.replace("á", "a").replace("à", "a").replace("ả", "a").replace("ã", "a").replace("ạ", "a")
					.replace("ă", "a").replace("ắ", "a").replace("ằ", "a").replace("ẳ", "a").replace("ẵ", "a").replace("ặ", "a")
					.replace("â", "a").replace("ấ", "a").replace("ầ", "a").replace("ẩ", "a").replace("ẫ", "a").replace("ậ", "a")
					.replace("đ", "d")
					.replace("é", "e").replace("è", "e").replace("ẻ", "e").replace("ẽ", "e").replace("ẹ", "e")
					.replace("ê", "e").replace("ế", "e").replace("ề", "e").replace("ể", "e").replace("ễ", "e").replace("ệ", "e")
					.replace("í", "i").replace("ì", "i").replace("ỉ", "i").replace("ĩ", "i").replace("ị", "i")
					.replace("ó", "o").replace("ò", "o").replace("ỏ", "o").replace("õ", "o").replace("ọ", "o")
					.replace("ô", "o").replace("ố", "o").replace("ồ", "o").replace("ổ", "o").replace("ỗ", "o").replace("ộ", "o")
					.replace("ơ", "o").replace("ớ", "o").replace("ờ", "o").replace("ở", "o").replace("ỡ", "o").replace("ợ", "o")
					.replace("ú", "u").replace("ù", "u").replace("ủ", "u").replace("ũ", "u").replace("ụ", "u")
					.replace("ư", "u").replace("ứ", "u").replace("ừ", "u").replace("ử", "u").replace("ữ", "u").replace("ự", "u")
					.replace("ý", "y").replace("ỳ", "y").replace("ỷ", "y").replace("ỹ", "y").replace("ỵ", "y")
					.replace("Đ", "D");
		
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		StagingSource ss = new StagingSource();
		
//		ss.printTxt("D:\\Data Warehouse");
		ss.printCSV("C:\\Users\\HP\\Desktop\\File bài tập\\Data Warehouse\\KQSX");
	}
}


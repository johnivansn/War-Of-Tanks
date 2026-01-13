package input;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import io.JSONParser;
import io.ScoreData;

public class Name {
	public static String name = getName();

	public static String getName() {
		String name;
		try {
			ArrayList<ScoreData> dataList = JSONParser.readFile();
			name = dataList.isEmpty() ? "" : dataList.get(dataList.size() - 1).getNickname();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			name = "";
		}
	
		return name;
	}
}
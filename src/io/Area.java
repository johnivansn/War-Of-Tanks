package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;

public class Area {
	public static ArrayList<ObjData> readFile(String link) throws FileNotFoundException {
		
		ArrayList<ObjData> dataList = new ArrayList<>();
		File file = new File(link);

		if (!file.exists() || file.length() == 0) {
			return dataList;
		}
		JSONTokener parser = new JSONTokener(new FileInputStream(file));
		JSONArray jsonList = new JSONArray(parser);

		for (int i = 0; i < jsonList.length(); i++) {
			JSONObject obj = (JSONObject) jsonList.get(i);
			ObjData data = new ObjData();
			
			data.setPositionX(obj.getInt("posX"));
			data.setPositionY(obj.getInt("posY"));
			data.setType(obj.getString("type"));
			dataList.add(data);
		}

		return dataList;
	}

	public static void writeFile(ArrayList<ObjData> dataList, String link) throws IOException {
		File outputFile = new File(link);

		outputFile.getParentFile().mkdir();
		outputFile.createNewFile();

		JSONArray jsonList = new JSONArray();

		for (ObjData data : dataList) {

			JSONObject obj = new JSONObject();

			obj.put("posX", data.getPositionX());
			obj.put("posY", data.getPositionY());
			obj.put("type", data.getType());

			jsonList.put(obj);
		}
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile.toURI()));
		jsonList.write(writer);
		writer.close();
	}
}

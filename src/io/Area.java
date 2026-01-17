package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;

public class Area {

	public static ArrayList<ObjData> readFile(String link) throws FileNotFoundException {
		ArrayList<ObjData> dataList = new ArrayList<>();
		File file = new File(link);

		if (!file.exists() || file.length() == 0)
			return dataList;

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
		Path outputPath = Paths.get(link);
		File outputFile = outputPath.toFile();

		File parentDir = outputFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			if (!parentDir.mkdirs()) {
				throw new IOException("No se pudo crear el directorio: " + parentDir.getAbsolutePath());
			}
		}

		if (outputFile.exists() && outputFile.length() > 0) {
			Path backupPath = Paths.get(link + ".backup");
			try {
				Files.copy(outputPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("ADVERTENCIA: No se pudo crear backup de " + link + ": " + e.getMessage());
			}
		}

		JSONArray jsonList = new JSONArray();
		for (ObjData data : dataList) {
			JSONObject obj = new JSONObject();
			obj.put("posX", data.getPositionX());
			obj.put("posY", data.getPositionY());
			obj.put("type", data.getType());
			jsonList.put(obj);
		}

		Path tempPath = Paths.get(link + ".tmp");
		try (BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
			jsonList.write(writer);
			writer.flush();
		} catch (IOException e) {
			try {
				Files.deleteIfExists(tempPath);
			} catch (IOException deleteError) {
				// Ignorar
			}
			throw new IOException("Error al escribir archivo temporal: " + e.getMessage(), e);
		}

		try {
			Files.move(tempPath, outputPath,
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			try {
				Files.deleteIfExists(tempPath);
			} catch (IOException deleteError) {
				// Ignorar
			}
			throw new IOException("Error al reemplazar archivo: " + e.getMessage(), e);
		}
	}

	public static boolean restoreFromBackup(String link) throws IOException {
		Path backupPath = Paths.get(link + ".backup");
		Path outputPath = Paths.get(link);

		if (!Files.exists(backupPath)) {
			return false;
		}

		Files.copy(backupPath, outputPath, StandardCopyOption.REPLACE_EXISTING);
		return true;
	}
}

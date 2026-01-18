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
import org.json.JSONObject;
import org.json.JSONTokener;

import input.Constants;

public class JSONParser {

	private static final int MAX_RETRIES = 5;
	private static final long RETRY_DELAY_MS = 100;

	public static ArrayList<ScoreData> readFile() throws FileNotFoundException {
		ArrayList<ScoreData> dataList = new ArrayList<ScoreData>();
		File file = new File(Constants.SCORE_PATH);

		if (!file.exists() || file.length() == 0) {
			return dataList;
		}
		try (FileInputStream fis = new FileInputStream(file)) {
			JSONTokener parser = new JSONTokener(fis);
			JSONArray jsonList = new JSONArray(parser);

			for (int i = 0; i < jsonList.length(); i++) {
				JSONObject obj = (JSONObject) jsonList.get(i);
				ScoreData data = new ScoreData();
				data.setScore(obj.getInt("score"));
				data.setDate(obj.getString("date"));
				data.setNickname(obj.getString("Nickname"));
				dataList.add(data);
			}
		} catch (IOException e) {
			if (!(e instanceof FileNotFoundException)) {
				throw new RuntimeException("Error reading file", e);
			}
			throw (FileNotFoundException) e;
		}

		return dataList;
	}

	public static void writeFile(ArrayList<ScoreData> dataList) throws IOException {
		Path outputPath = Paths.get(Constants.SCORE_PATH);
		File outputFile = outputPath.toFile();

		File parentDir = outputFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			if (!parentDir.mkdirs()) {
				throw new IOException("No se pudo crear el directorio: " + parentDir.getAbsolutePath());
			}
		}

		if (outputFile.exists() && outputFile.length() > 0) {
			Path backupPath = Paths.get(Constants.SCORE_PATH + ".backup");
			try {
				Files.copy(outputPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("ADVERTENCIA: No se pudo crear backup: " + e.getMessage());
			}
		}

		JSONArray jsonList = new JSONArray();
		for (ScoreData data : dataList) {
			JSONObject obj = new JSONObject();
			obj.put("score", data.getScore());
			obj.put("date", data.getDate());
			obj.put("Nickname", data.getNickname());
			jsonList.put(obj);
		}

		Path tempPath = Paths.get(Constants.SCORE_PATH + ".tmp");
		try (BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
			jsonList.write(writer);
			writer.flush();
		} catch (IOException e) {
			try {
				Files.deleteIfExists(tempPath);
			} catch (IOException deleteError) {
				// Ignorar error al eliminar temporal
			}
			throw new IOException("Error al escribir archivo temporal: " + e.getMessage(), e);
		}

		IOException lastException = null;
		for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
			try {
				if (attempt > 0) {
					System.gc();
					Thread.sleep(RETRY_DELAY_MS * attempt);
				}

				Files.move(tempPath, outputPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
				return;
			} catch (IOException e) {
				lastException = e;
				if (attempt < MAX_RETRIES - 1) {
					System.err.println("Intento " + (attempt + 1) + " fallido, reintentando...");
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IOException("Operación interrumpida", e);
			}
		}
		try {
			Files.deleteIfExists(tempPath);
		} catch (IOException deleteError) {
			// Ignorar error al eliminar temporal
		}
		throw new IOException(
				"Error al reemplazar archivo después de " + MAX_RETRIES + " intentos: " + lastException.getMessage(),
				lastException);
	}

	public static boolean restoreFromBackup() throws IOException {
		Path backupPath = Paths.get(Constants.SCORE_PATH + ".backup");
		Path outputPath = Paths.get(Constants.SCORE_PATH);

		if (!Files.exists(backupPath)) {
			return false;
		}

		Files.copy(backupPath, outputPath, StandardCopyOption.REPLACE_EXISTING);
		return true;
	}
}

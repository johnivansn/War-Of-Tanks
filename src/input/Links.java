package input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Links {
	public static ArrayList<String> links = getLinks();

	public static ArrayList<String> getLinks() {
		File file = new File(Constants.SAVE_CARP);
		ArrayList<String> aux = new ArrayList<>();
		if (file.exists()) {
			File[] list = file.listFiles();
			for (int i = 1; i <= list.length; i++) {
				aux.add(Constants.SAVE_CARP + "\\dataEdit" + i + ".json");
			}
		} else {
			String newLink = newLink();
			File newFile = new File(newLink);
			newFile.getParentFile().mkdirs();
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			aux.add(newLink);
		}
		return aux;
	}

	public static void deleteLink(int n) {
		File file = new File(Constants.SAVE_CARP + "\\dataEdit" + n + ".json");
		if (file.exists()) {
			if (file.delete()) {
				//
			} else {
				System.out.println("no se elimino, " + n);
			}
		}
	}

	public static String newLink() {
		File file = new File(Constants.SAVE_CARP);
		if (file.exists()) {
			File[] list = file.listFiles();
			return Constants.SAVE_CARP + "\\dataEdit" + (list.length + 1) + ".json";
		} else {
			return Constants.SAVE_CARP + "\\dataEdit1.json";
		}
	}
}

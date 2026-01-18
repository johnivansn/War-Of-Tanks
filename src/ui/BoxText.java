package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Assets;
import graphics.Text;
import input.KeyBoard;
import input.Name;
import math.Vector2D;

public class BoxText extends KeyBoard {
	private ArrayList<String> na = new ArrayList<>();
	public boolean act;
	private int a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, el;
	private int x1, y1;
	private static final int MAX_LENGTH = 12;
	private static final String VALID_CHARS = "abcdefghijklmnopqrstuvwxyz";

	public BoxText(int x, int y) {
		this.x1 = x;
		this.y1 = y;
	}

	public void update() {
		if (act) {
			if (na.size() < MAX_LENGTH) {
				String[] letras = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
						"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
				int[] contadores = { a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z };

				for (int i = 0; i < letras.length; i++) {
					if (KeyBoard.keys[i + 65]) {
						contadores[i]++;
						if (contadores[i] == 1) {
							String letra = letras[i];
							if (VALID_CHARS.contains(letra)) {
								na.add(letra);
							}
						}
					} else {
						contadores[i] = 0;
					}
				}
			}
		}

		if (!na.isEmpty()) {
			if (KeyBoard.el) {
				el++;
				if (el == 1) {
					na.remove(na.size() - 1);
				}
			} else {
				el = 0;
			}
		}
	}

	public void draw(Graphics g) {

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, 300, 45);

		for (int i = 0; i < na.size(); i++) {
			Text.drawText(
					g,
					na.get(i),
					new Vector2D(x1 + 14 + (i * 22.5), y1 + 35.5),
					false,
					Color.DARK_GRAY,
					Assets.fontBig);
		}
	}

	public String getName() {
		Name.name = "";

		for (int i = 0; i < na.size(); i++) {
			Name.name += na.get(i);
		}

		if (Name.name.trim().isEmpty()) {
			return "Player";
		}

		return Name.name;
	}
}

package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Assets;
import graphics.Text;
import input.KeyBoard;
import input.Name;
import math.Vector2D;

public class BoxText extends KeyBoard{
	private ArrayList<String> na = new ArrayList<>();
	public boolean act;
	private int a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, el;
	private int x1, y1;
	
	public BoxText(int x,int y) {
		this.x1 = x;
		this.y1 = y;
		//a = b = c = d = e = f = g = h = i = j = k = l = m = n = o = p = q = r = s = t = u = v = w = x = y = z = el = 0;
	}
	
	public void update() {
		if(act) {	//	activa/desactiva entrada de teclado
			if (na.size() < 8) {
					String[] letras = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
								"k", "l", "m", "n"/*, ñ*/, "o","p","q","r","s","t","u","v","w","x","y","z"};
				int[] contadores = {a, b, c, d, e, f, g, h, i, j , k , l , m , n/* , ñ*/ , o , p , q , r , s , t , u , v , w , x , y , z};
	
				/* for (int i = 0; i < letras.length; i++) {
					if (KeyBoard.keys[i+65]) { 
						//contadores[i]++; 
						a++;
						if (a == 1) { na.add(letras[i]); } 
					} else { 
						a = 0; 
					}
				} */
				for (int i = 0; i < letras.length; i++) {
					if (KeyBoard.keys[i+65]) { contadores[i]++; if (contadores[i] == 1) { na.add(letras[i]); }} else { contadores[i] = 0; }
				}
				/* if (KeyBoard.a) { a++; if (a == 1) { na.add("a"); }} else { a = 0; }
				if (KeyBoard.b) { b++; if (b == 1) { na.add("b"); }} else { b = 0; }
				if (KeyBoard.c) { c++; if (c == 1) { na.add("c"); }} else { c = 0; }
				if (KeyBoard.d) { d++; if (d == 1) { na.add("d"); }} else { d = 0; }
				if (KeyBoard.e) { e++; if (e == 1) { na.add("e"); }} else { e = 0; }
				if (KeyBoard.f) { f++; if (f == 1) { na.add("f"); }} else { f = 0; }
				if (KeyBoard.g) { g++; if (g == 1) { na.add("g"); }} else { g = 0; }
				if (KeyBoard.h) { h++; if (h == 1) { na.add("h"); }} else { h = 0; }
				if (KeyBoard.i) { i++; if (i == 1) { na.add("i"); }} else { i = 0; }
				if (KeyBoard.j) { j++; if (j == 1) { na.add("j"); }} else { j = 0; }
				if (KeyBoard.k) { k++; if (k == 1) { na.add("k"); }} else { k = 0; }
				if (KeyBoard.l) { l++; if (l == 1) { na.add("l"); }} else { l = 0; }
				if (KeyBoard.m) { m++; if (m == 1) { na.add("m"); }} else { m = 0; }
				if (KeyBoard.n) { n++; if (n == 1) { na.add("n"); }} else { n = 0; }
				//if (KeyBoard.ñ) { ñ++; if (ñ == 1) { na.add("ñ"); }} else { ñ = 0; }
				if (KeyBoard.o) { o++; if (o == 1) { na.add("o"); }} else { o = 0; }
				if (KeyBoard.p) { p++; if (p == 1) { na.add("p"); }} else { p = 0; }
				if (KeyBoard.q) { q++; if (q == 1) { na.add("q"); }} else { q = 0; }
				if (KeyBoard.r) { r++; if (r == 1) { na.add("r"); }} else { r = 0; }
				if (KeyBoard.s) { s++; if (s == 1) { na.add("s"); }} else { s = 0; }
				if (KeyBoard.t) { t++; if (t == 1) { na.add("t"); }} else { t = 0; }
				if (KeyBoard.u) { u++; if (u == 1) { na.add("u"); }} else { u = 0; }
				if (KeyBoard.v) { v++; if (v == 1) { na.add("v"); }} else { v = 0; }
				if (KeyBoard.w) { w++; if (w == 1) { na.add("w"); }} else { w = 0; }
				if (KeyBoard.x) { x++; if (x == 1) { na.add("x"); }} else { x = 0; }
				if (KeyBoard.y) { y++; if (y == 1) { na.add("y"); }} else { y = 0; }
				if (KeyBoard.z) { z++; if (z == 1) { na.add("z"); }} else { z = 0; }
			} */
		
		}
		if (!na.isEmpty()) {
			if (KeyBoard.el) { el++; if (el == 1) { na.remove(na.size() - 1); }} else { el = 0; }
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

		return Name.name;
	}
}
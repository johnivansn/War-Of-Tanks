package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoard implements KeyListener {

	public static boolean[] keys = new boolean[256];
	public static boolean UP, LEFT, RIGHT, SHOOT, ESC, BtEsc;
	public static boolean a, b, c, d, e, f, g, h, i, j, k, l, m, n, ñ, o, p, q, r, s, t, u, v, w, x, y, z, el;

	public KeyBoard() {
		/*
		 * UP = false;
		 * LEFT = false;
		 * RIGHT = false;
		 * SHOOT = false;
		 * ESC = false;
		 * BtEsc = false;
		 * a = b = c = d = e = f = g = h = i = j = k = l = m = n = o = p = q = r = s = t
		 * = u = v = w = x = y = z = el = false;
		 */
	}

	public void update() {
		a = keys[KeyEvent.VK_A];
		b = keys[KeyEvent.VK_B];
		c = keys[KeyEvent.VK_C];
		d = keys[KeyEvent.VK_D];
		e = keys[KeyEvent.VK_E];
		f = keys[KeyEvent.VK_F];
		g = keys[KeyEvent.VK_G];
		h = keys[KeyEvent.VK_H];
		i = keys[KeyEvent.VK_I];
		j = keys[KeyEvent.VK_J];
		k = keys[KeyEvent.VK_K];
		l = keys[KeyEvent.VK_L];
		m = keys[KeyEvent.VK_M];
		n = keys[KeyEvent.VK_N];
		o = keys[KeyEvent.VK_O];
		p = keys[KeyEvent.VK_P];
		q = keys[KeyEvent.VK_Q];
		r = keys[KeyEvent.VK_R];
		s = keys[KeyEvent.VK_S];
		t = keys[KeyEvent.VK_T];
		u = keys[KeyEvent.VK_U];
		v = keys[KeyEvent.VK_V];
		w = keys[KeyEvent.VK_W];
		x = keys[KeyEvent.VK_X];
		y = keys[KeyEvent.VK_Y];
		z = keys[KeyEvent.VK_Z];
		el = keys[KeyEvent.VK_BACK_SPACE];

		UP = keys[KeyEvent.VK_UP];
		LEFT = keys[KeyEvent.VK_LEFT];
		RIGHT = keys[KeyEvent.VK_RIGHT];
		SHOOT = keys[KeyEvent.VK_Z];
		ESC = keys[KeyEvent.VK_ESCAPE];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			keys[e.getKeyCode()] = !KeyBoard.BtEsc;
			KeyBoard.BtEsc = !KeyBoard.BtEsc;
		} else {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
			keys[e.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// document why this method is empty
	}
}

package graphics;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	private Clip clip;
	private FloatControl volume;

	public Sound(Clip clip) {
		this.clip = clip;

		if (clip != null && clip.isOpen()) {
			try {
				volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			} catch (IllegalArgumentException e) {
				// Algunos clips pueden no soportar control de volumen
				System.err.println("ADVERTENCIA: Clip sin control de volumen");
				volume = null;
			}
		}
	}

	public void play() {
		if (clip == null)
			return;

		if (!clip.isOpen()) {
			System.err.println("ADVERTENCIA: Intento de reproducir clip cerrado");
			return;
		}

		try {
			clip.setFramePosition(0);
			clip.start();
		} catch (IllegalStateException e) {
			System.err.println("ERROR al reproducir sonido: " + e.getMessage());
		}
	}

	public void loop() {
		if (clip == null) {
			return;
		}

		if (!clip.isOpen()) {
			System.err.println("ADVERTENCIA: Intento de hacer loop en clip cerrado");
			return;
		}

		try {
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (IllegalStateException e) {
			System.err.println("ERROR al hacer loop de sonido: " + e.getMessage());
		}
	}

	public void stop() {
		if (clip == null) {
			return;
		}

		if (!clip.isOpen())
			return;

		try {
			if (clip.isRunning()) {
				clip.stop();
			}
		} catch (IllegalStateException e) {
			System.err.println("ERROR al detener sonido: " + e.getMessage());
		}
	}

	public int getFramePosition() {
		if (clip == null || !clip.isOpen()) {
			return 0;
		}

		try {
			return clip.getFramePosition();
		} catch (IllegalStateException e) {
			System.err.println("ERROR al obtener posición de frame: " + e.getMessage());
			return 0;
		}
	}

	public void changeVolume(float value) {
		if (volume == null) {
			// No hay control de volumen disponible
			return;
		}

		try {
			// Limitar valor al rango válido del control
			float min = volume.getMinimum();
			float max = volume.getMaximum();
			float clampedValue = Math.max(min, Math.min(max, value));

			volume.setValue(clampedValue);
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR al cambiar volumen: " + e.getMessage());
		}
	}

	public boolean isRunning() {
		if (clip == null || !clip.isOpen()) {
			return false;
		}

		try {
			return clip.isRunning();
		} catch (IllegalStateException e) {
			return false;
		}
	}

	public boolean isAvailable() {
		return clip != null && clip.isOpen();
	}

	public void close() {
		if (clip != null && clip.isOpen()) {
			try {
				clip.stop();
				clip.close();
			} catch (Exception e) {
				System.err.println("ERROR al cerrar clip: " + e.getMessage());
			}
		}
	}
}

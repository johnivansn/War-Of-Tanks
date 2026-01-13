package io;

public class ObjData {

	private int positionX;
	private int positionY;
	private String type;
	public ObjData(String type, int positionX, int positionY) {
		this.type = type;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public ObjData() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	
	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
}

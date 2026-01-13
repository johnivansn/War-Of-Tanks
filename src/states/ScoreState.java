package states;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import math.Vector2D;
import graphics.Assets;
import graphics.Text;
import input.Constants;
import io.JSONParser;
import io.ScoreData;
import ui.Button;
import ui.Action;

public class ScoreState extends State {

	private Button returnButton;
	private PriorityQueue<ScoreData> highScores;
	private Comparator<ScoreData> scoreComparator;
	private ScoreData[] auxArray;
	
	public ScoreState() {
		returnButton = new Button(
				Assets.grey_button, 
				Assets.blue_button, 
				Assets.grey_button.getHeight(),
				(int) (Constants.HEIGHT - Assets.grey_button.getHeight() * 2.3), 
				Constants.RETURN, 
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new MenuState());
					}
				});

		scoreComparator = new Comparator<ScoreData>() {//comparador
			@Override
			public int compare(ScoreData e1, ScoreData e2) {

				return e1.getScore() < e2.getScore() ? -1 : e1.getScore() > e2.getScore() ? 1 : 0;
			}
		};

		highScores = new PriorityQueue<>(10, scoreComparator);

		try {
			ArrayList<ScoreData> dataList = JSONParser.readFile();
			for (ScoreData d : dataList) {
				highScores.add(d);
			}
			while (highScores.size() > 10) {
				highScores.poll();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float dt) {
		returnButton.update();

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(Assets.score, 0, 0, null);
		returnButton.draw(g);

		auxArray = highScores.toArray(new ScoreData[highScores.size()]);
		Arrays.sort(auxArray, scoreComparator);

		Vector2D namePos = new Vector2D(Constants.WIDTH / 2 - 340, 120);
		Vector2D datePos = new Vector2D(Constants.WIDTH / 2 + 350, 120);
		Vector2D scorePos = new Vector2D(Constants.WIDTH / 2 , 120);
		Text.drawText(
				g, 
				Constants.NICKNAME, 
				namePos, 
				true, 
				Color.BLACK, 
				Assets.fontBig);
		
		
		Text.drawText(
				g, 
				Constants.SCORE, 
				scorePos, 
				true, 
				Color.BLACK, 
				Assets.fontBig);
		
		Text.drawText(
				g, 
				Constants.DATE, 
				datePos, 
				true, 
				Color.BLACK, 
				Assets.fontBig);

		scorePos.setY(scorePos.getY() + 40);
		datePos.setY(datePos.getY() + 40);
		namePos.setY(namePos.getY() + 40);

		for (int i = auxArray.length - 1; i > -1; i--) {
			ScoreData d = auxArray[i];

			Text.drawText(
					g, 
					Integer.toString(d.getScore()), 
					scorePos, 
					true, 
					Color.BLACK, 
					Assets.fontMed);
			
			Text.drawText(
					g, 
					d.getDate(), 
					datePos, 
					true, 
					Color.BLACK, 
					Assets.fontMed);
			Text.drawText(
					g, 
					d.getNickname(), 
					namePos, 
					true, 
					Color.BLACK, 
					Assets.fontMed);

			scorePos.setY(scorePos.getY() + 40);
			datePos.setY(datePos.getY() + 40);
			namePos.setY(namePos.getY() + 40);
		}
	}
}

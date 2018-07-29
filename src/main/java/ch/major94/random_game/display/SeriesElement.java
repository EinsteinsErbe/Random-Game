package ch.major94.random_game.display;

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.data.xy.XYSeries;

public class SeriesElement extends XYSeries{

	private static final long serialVersionUID = -6017157705517984364L;

	private static HashMap<String, SeriesElement> map = new HashMap<>();

	public static ArrayList<SeriesElement> list = new ArrayList<>();

	private int count;

	public SeriesElement(String key) {
		super(key, false, false);

		count = 0;
	}

	public static SeriesElement getSeries(String key) {
		if(map.containsKey(key)) {
			return map.get(key);
		}
		SeriesElement se = new SeriesElement(key);
		map.put(key, se);
		list.add(se);
		return se;
	}

	public static void count(String key) {
		getSeries(key).count++;
	}

	public static void reset() {
		for (SeriesElement se : map.values()) {
			se.count = 0;
		}
	}

	public void addToData(int gen) {
		add(gen, count);
	}
}

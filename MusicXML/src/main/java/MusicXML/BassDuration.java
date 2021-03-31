package MusicXML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.util.Pair;

public class BassDuration {

	private Map<Integer, Integer> durationMap = new TreeMap<Integer, Integer>();
	private int difference;
	private boolean isDot = false;

	public BassDuration(Map<Pair<Integer, Integer>, List<Integer>> notesMap, int measureSpaces) {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for (Map.Entry<Pair<Integer, Integer>, List<Integer>> entry : notesMap.entrySet()) {
			Integer index = entry.getKey().getKey();
			tmp.add(index);
		}

		for (int i = 0; i < tmp.size(); i++) {
			if (i == tmp.size() - 1) {
				difference = measureSpaces - tmp.get(i);
			} else {
				difference = tmp.get(i + 1) - tmp.get(i);
			}
			durationMap.put(tmp.get(i), timeDuration(difference, measureSpaces));
		}
	}

	/*
	 * This part only goes up to eighth notes To modify for sixteenth notes and
	 * beyond, modify timeDuration() and getType()
	 */
	private int timeDuration(int diff, int spaces) {
		int div = spaces / 8;
		int mod = spaces % 8;
		int duration = 0;

		if (mod == 0) {
			if (diff <= div)
				duration = 1;
			else if (diff <= 2 * div)
				duration = 2;
			else if (diff <= 3 * div)
				duration = 3;
			else if (diff <= 4 * div)
				duration = 4;
			else if (diff <= 5 * div)
				duration = 4;
			else if (diff <= 6 * div)
				duration = 6;
			else if (diff <= 7 * div)
				duration = 6;
			else
				duration = 8;
		} else if (mod == 1) {
			if (diff <= div)
				duration = 1;
			else if (diff <= 2 * div + 1)
				duration = 2;
			else if (diff <= 3 * div)
				duration = 3;
			else if (diff <= 4 * div + 1)
				duration = 4;
			else if (diff <= 5 * div + 1)
				duration = 4;
			else if (diff <= 6 * div + 1)
				duration = 6;
			else if (diff <= 7 * div + 1)
				duration = 6;
			else
				duration = 8;
		} else if (mod == 2) {
			if (diff <= div)
				duration = 1;
			else if (diff <= 2 * div + 1)
				duration = 2;
			else if (diff <= 3 * div + 1)
				duration = 3;
			else if (diff <= 4 * div + 1)
				duration = 4;
			else if (diff <= 5 * div + 1)
				duration = 4;
			else if (diff <= 6 * div + 2)
				duration = 6;
			else if (diff <= 7 * div + 2)
				duration = 6;
			else
				duration = 8;
		} else if (mod == 3) {
			if (diff <= div + 1)
				duration = 1;
			else if (diff <= 2 * div + 1)
				duration = 2;
			else if (diff <= 3 * div + 1)
				duration = 3;
			else if (diff <= 4 * div + 2)
				duration = 4;
			else if (diff <= 5 * div + 2)
				duration = 4;
			else if (diff <= 6 * div + 2)
				duration = 6;
			else if (diff <= 7 * div + 3)
				duration = 6;
			else
				duration = 8;
		} else if (mod == 4) {
			if (diff <= div + 1)
				duration = 1;
			else if (diff <= 2 * div + 1)
				duration = 2;
			else if (diff <= 3 * div + 2)
				duration = 3;
			else if (diff <= 4 * div + 2)
				duration = 4;
			else if (diff <= 5 * div + 3)
				duration = 4;
			else if (diff <= 6 * div + 3)
				duration = 6;
			else if (diff <= 7 * div + 4)
				duration = 6;
			else
				duration = 8;
		} else if (mod == 5) {
			if (diff <= div + 1)
				duration = 1;
			else if (diff <= 2 * div + 1)
				duration = 2;
			else if (diff <= 3 * div + 2)
				duration = 3;
			else if (diff <= 4 * div + 3)
				duration = 4;
			else if (diff <= 5 * div + 3)
				duration = 4;
			else if (diff <= 6 * div + 4)
				duration = 6;
			else if (diff <= 7 * div + 4)
				duration = 6;
			else
				duration = 8;
		} else if (mod == 6) {
			if (diff <= div + 1)
				duration = 1;
			else if (diff <= 2 * div + 2)
				duration = 2;
			else if (diff <= 3 * div + 2)
				duration = 3;
			else if (diff <= 4 * div + 3)
				duration = 4;
			else if (diff <= 5 * div + 4)
				duration = 4;
			else if (diff <= 6 * div + 5)
				duration = 6;
			else if (diff <= 7 * div + 5)
				duration = 6;
			else
				duration = 8;
		} else if (mod == 7) {
			if (diff <= div + 1)
				duration = 1;
			else if (diff <= 2 * div + 2)
				duration = 2;
			else if (diff <= 3 * div + 3)
				duration = 3;
			else if (diff <= 4 * div + 4)
				duration = 4;
			else if (diff <= 5 * div + 4)
				duration = 4;
			else if (diff <= 6 * div + 5)
				duration = 6;
			else if (diff <= 7 * div + 6)
				duration = 6;
			else
				duration = 8;
		} else {
			duration = -1;		//debugging purposes
		}
		return duration;
	}

	public String getType(int dur) {
		String type = "";
		if (dur == 1) {
			type = "eighth";
		} else if (dur == 2) {
			type = "quarter";
		} else if (dur == 3) {
			type = "quarter";
		} else if (dur == 4) {
			type = "half";
		} else if (dur == 6) {
			type = "half";
		} else if (dur == 8) {
			type = "whole";
		}
		return type;
	}
	
	public boolean isDot(int dur) {
		if( dur == 3 || dur == 6) isDot = true;
		else isDot =false;
		return isDot;
	}

	public Integer getDuration(int index) {
		return durationMap.get(index);
	}

}

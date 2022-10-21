package com.utility;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapSorter {
	public static LinkedHashMap<String, Integer> getSortedMap(Map <String,Integer> changepattenr)
	{
		LinkedHashMap<String, Integer> reverseFixPattern = new LinkedHashMap<>();
		changepattenr.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseFixPattern.put(x.getKey(), x.getValue()));
		
		return reverseFixPattern;
		
	}	

}

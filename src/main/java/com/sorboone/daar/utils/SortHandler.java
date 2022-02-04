package com.sorboone.daar.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SortHandler {
	
	public static LinkedHashMap<Integer,Integer> sortByValues(Map<Integer,Integer> map){
		
		List<Integer> mapValues = new ArrayList<>(map.values());
		List<Integer> mapKeys = new ArrayList<>(map.keySet());
		
		Collections.sort(mapValues,Collections.reverseOrder());
		Collections.sort(mapKeys,Collections.reverseOrder());
		
		LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
		
		Iterator<Integer> iteratorValues = mapValues.iterator();
				
		while ( iteratorValues.hasNext()) {
			Integer val = iteratorValues.next();
			
			Iterator<Integer> iteratorKey = mapKeys.iterator();
			while (iteratorKey.hasNext()) {
				Integer key = iteratorKey.next();
				Integer value = map.get(key);
				
				if ( value == val) {
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}

}

package it.polito.nexa.pc.businessEntities;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils; 



public class Company {
	private HashMap<String, Integer> names = new HashMap<>(); 
	private HashMap<String, Integer> originalIds = new HashMap<>(); 
	public Company(String id, String name,  boolean isItalian) {
		super();
		this.id = id;
		this.italian = isItalian;
		names.put(name.toUpperCase(), 1); 
	}
	
	public boolean isItalian() {
		return italian;
	}

	public String getId() {
		return id;
	}
	public boolean hasOriginalId(){
		return !originalIds.isEmpty(); 
	}

	public Company( String id,String name, boolean isItalian, String originalId ) {
		super();
		this.id = id;
		this.italian = isItalian;
		names.put(name.toUpperCase(), 1);	
		originalIds.put(originalId, 1); 
	}
	
	private String id; 
	private boolean italian; 
	
	public void addName(String name){
		if ((names.containsKey(name) && (name.length()>1))){
			names.put(name.toUpperCase(), names.get(name.toUpperCase())+1); 
		} else {
			if (name.length()>1)
			names.put(name.toUpperCase(), 1);
		}
	}
	public void addOriginalId(String originalId){
		if (originalIds.containsKey(originalId)){
			originalIds.put(originalId, originalIds.get(originalId)+1); 
		} else {
			originalIds.put(originalId, 1);
		}
	}
	public HashMap<String, Integer> getNames(){
		return names; 
	}
	public Set<String> getOriginalIds(){
		return originalIds.keySet(); 
	}
	public String toString(){
		return  
		names.entrySet().stream().
			    sorted(Entry.comparingByValue())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new)).toString();   
		
	}
	public String getBestLabel(){
		double similarity; 
		HashMap<String, Double> comparedNames = new HashMap<>(); 
		for (Entry e : names.entrySet()){
			similarity=0; 
			for (Entry f : names.entrySet()){
				if (!f.getKey().equals(e.getKey()))	{
					similarity +=StringUtils.getLevenshteinDistance((String)e.getKey(), (String) f.getKey())*(Integer)e.getValue();
				//System.out.println(e.getKey()+"---"+ f.getKey()); 
				//System.out.println(StringUtils.getLevenshteinDistance((String)e.getKey(), (String) f.getKey())); 
				}
			}
			comparedNames.put((String)e.getKey(), similarity); 	
		}
		//System.out.println(comparedNames); 
		return comparedNames.entrySet().stream()
	     .max(Comparator.comparing(e -> e.getValue())).get().getKey()
	          ; 
		
	}
	public HashMap<String, Double> getAllLabels(){
		double similarity; 
		HashMap<String, Double> comparedNames = new HashMap<>(); 
		for (Entry e : names.entrySet()){
			similarity=0; 
			for (Entry f : names.entrySet()){
				if (!f.getKey().equals(e.getKey()))	{
					similarity +=StringUtils.getLevenshteinDistance((String)e.getKey(), (String) f.getKey())*(Integer)e.getValue();
				//System.out.println(e.getKey()+"---"+ f.getKey()); 
				//System.out.println(StringUtils.getLevenshteinDistance((String)e.getKey(), (String) f.getKey())); 
				}
			}
			comparedNames.put((String)e.getKey(), similarity); 	
		}
		//System.out.println(comparedNames); 
		return comparedNames; 
		
	}
	
	class ValueComparator implements Comparator<String> {

	    Map<String, Double> base;
	    public ValueComparator(Map<String, Double> base) {
	        this.base = base;
	    }

	 

		@Override
		public int compare(String a, String b) {
			Double result = base.get(a)-base.get(b); 
			return  result.intValue() ; 
		}
	}

}

package utility;

import java.util.ArrayList;
import java.util.HashMap;

public class Cache<K,V> {

	private HashMap<K,V> map;
	private ArrayList<K> queue;
	private int maxSize;
	
	public Cache(int maxSize)
	{
		map = new HashMap<K,V>();
		queue = new ArrayList<K>();
		this.maxSize = maxSize;
	}
	
	/**
	 * Returns the object corresponding to the key
	 * @param key the key of the object to get
	 * @return the value if the key is in the cache, null otherwise
	 */
	public V get(K key)
	{
		if(queue.contains(key))
		{
			queue.remove(key);
			queue.add(0, key);
			return map.get(key);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Adds a new key value pair to the cache
	 * @param key the key to add to the cache
	 * @param value the value corresponding to the key
	 */
	public void put(K key, V value)
	{
		if(queue.contains(key))
		{
			queue.remove(key);
		}
		queue.add(0, key);
		map.put(key, value);
		
		if(queue.size() > maxSize)
		{
			K old = queue.remove(queue.size()-1);
			map.remove(old);
		}
	}
	
}

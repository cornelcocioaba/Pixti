package com.CornelCocioaba.Pixti.Utils.Pool;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
	private final List<T> freeObjects;
	private final IPoolObjectFactory<T> factory;
	private final int maxSize;
	
	public Pool(IPoolObjectFactory<T> factory, int maxSize){
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	
	public T newObject(){
		T object = null;
		if(freeObjects.isEmpty()){
			object = factory.createObject();
		}else{
			object = freeObjects.remove(freeObjects.size() - 1);
		}
		
		return object;
	}
	
	public void free(T object){
		if(freeObjects.size() < maxSize){
			freeObjects.add(object);
		}
	}
	
}

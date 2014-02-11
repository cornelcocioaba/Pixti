package com.CornelCocioaba.Pixti.Utils.Pool;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
	private final List<T> recycledObjects;
	private final IPoolObjectFactory<T> factory;
	private final int maxSize;
	
	public Pool(IPoolObjectFactory<T> factory, int maxSize){
		this.factory = factory;
		this.maxSize = maxSize;
		this.recycledObjects = new ArrayList<T>(maxSize);
	}
	
	public T newObject(){
		T object = null;
		if(recycledObjects.isEmpty()){
			object = factory.createObject();
		}else{
			object = recycledObjects.remove(recycledObjects.size() - 1);
		}
		
		return object;
	}
	
	public void recycle(T object){
		if(recycledObjects.size() < maxSize){
			recycledObjects.add(object);
		}
	}
	
}

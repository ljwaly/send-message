package com.ljw.publish.vo;


public class Link {
	private Link next;
	private Object data;
	Link(Object it,Link nextval){
		this.next = nextval;
		this.data = it;
	}
	
	Link(Link nextval){
		this.next = nextval;
	}
	
	Link next(){
		return next;
	}
	
	Link setNext(Link nextval){
		return next = nextval;
	}
	
	Object data(){
		return data;
	}
	
	Object setData(Object it){
		return data = it;
	}
}

package com.mybank.accountservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PublisherDto<T> implements Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -5357092526581003802L;
	private T t;

	   public void add(T t) {
	      this.t = t;
	   }

	   public T get() {
	      return t;
	   }  
	
}

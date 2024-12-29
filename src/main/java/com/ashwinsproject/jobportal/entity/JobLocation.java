package com.ashwinsproject.jobportal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JobLocation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	private String city;
	private String state;
	private String Country;
	
	public JobLocation() {
		
	}

	public JobLocation(Integer id, String city, String state, String country) {
		Id = id;
		this.city = city;
		this.state = state;
		Country = country;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	@Override
	public String toString() {
		return "JobLocation [Id=" + Id + ", city=" + city + ", state=" + state + ", Country=" + Country + "]";
	}
	
	
	
	
}

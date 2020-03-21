package com.rindviechcontrol.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.time.ZonedDateTime;

import javax.persistence.*;

@Entity
@NamedQuery(name="findPositions", query="SELECT p FROM Position p WHERE p.latitude < :maxLatitude AND p.latitude > :minLatitude AND p.longitude < :maxLongitude AND p.longitude > :minLongitude")
public class Position extends PanacheEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long id;

	public String deviceId;

	public double latitude;
	public double longitude;

	public ZonedDateTime time = ZonedDateTime.now();

	public Position() {}

	public Position(String deviceId, double latitude, double longitude) {
		this.deviceId = deviceId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}

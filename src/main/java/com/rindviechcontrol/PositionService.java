package com.rindviechcontrol;

import java.util.List;


import com.rindviechcontrol.entity.Position;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@ApplicationScoped
public class PositionService
{

	// ("SELECT * FROM position WHERE p.latitude < " + maxLat + " AND p.latitude > " + minLat + " AND p.longitude < " + maxLong + " AND p.longitude > " + minLong);
	@Transactional
	public List<Position> getPositionsInRect(double minLat, double maxLat, double minLong, double maxLong){
		PanacheQuery pq = Position.findAll();
		System.out.println(pq.count());
        System.out.println(pq.toString());
        return pq.list();
	}

	@Transactional
	public void addPosition(Position p) {
		p.persistAndFlush();
	}

}

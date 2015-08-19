package com.stm.shorttermemployee.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.pojo.Train;

@Component
public class TrainDAO extends BasicDAO {
	public Long addTrain(Train t) {
		Session session = getSessionFactory().getCurrentSession();
		session.save(t);
		return t.getId();
	}
}

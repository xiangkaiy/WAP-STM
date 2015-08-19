package com.stm.shorttermemployee.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.pojo.SteTrain;

@Component
public class SteTrainDAO extends BasicDAO {
	public void deleteByTrainIdAndSteId(Long trainId, Long steId) {
		String hql = "delete from SteTrain where trainid=:trainid and steid=:steid";
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setLong("trainid", trainId);
		query.setLong("steid", steId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public SteTrain getByTrainIdAndSteId(Long trainId, Long steId) {
		SteTrain s = null;
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SteTrain.class);
		criteria = criteria.add(Restrictions.eq("trainid", trainId));
		criteria = criteria.add(Restrictions.eq("steid", steId));

		List<SteTrain> list = criteria.list();
		if (list != null && list.size() > 0) {
			s = list.get(0);
		}
		return s;
	}

	public void deleteBySteId(Long steid) {
		String hql = "delete from SteTrain where steid = :steid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("steid", steid).executeUpdate();
	}
}

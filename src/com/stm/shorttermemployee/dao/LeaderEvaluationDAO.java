package com.stm.shorttermemployee.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.pojo.LeaderEvaluation;

@Component
public class LeaderEvaluationDAO extends BasicDAO {
	public void deleteBySteId(Long steid) {
		String hql = "delete from LeaderEvaluation where steid = :steid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("steid", steid).executeUpdate();
	}

	public void deleteByLeaderId(Long leaderid) {
		String hql = "delete from LeaderEvaluation where leaderid = :leaderid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("leaderid", leaderid).executeUpdate();
	}

	public List<LeaderEvaluation> getBySteAndLeader(Long steid, Long leaderid) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(LeaderEvaluation.class);
		c.add(Restrictions.eq("steid", steid));
		c.add(Restrictions.eq("leaderid", leaderid));
		return c.list();
	}
}

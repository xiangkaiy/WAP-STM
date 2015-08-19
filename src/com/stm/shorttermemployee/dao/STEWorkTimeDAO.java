package com.stm.shorttermemployee.dao;

import java.sql.Timestamp;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.pojo.STEWorktime;

@Component
public class STEWorkTimeDAO extends BasicDAO {

	public Integer getSignInTimeAfterGivenTime(Long steid, Timestamp starttime, Timestamp endtime, Timestamp giventime) {
		Integer result = 0;
		Conjunction conj = Restrictions.conjunction();
		if (starttime != null) {
			conj.add(Restrictions.ge("signtime", starttime));
		}
		conj.add(Restrictions.eq("steid", steid));
		conj.add(Restrictions.lt("signtime", endtime));
		conj.add(Restrictions.ge("starttime", giventime));
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(STEWorktime.class);
		criteria.add(conj);
		result = criteria.list().size();
		return result;
	}

	public Integer getSignOutTimeBeforeGivenTime(Long steid, Timestamp starttime, Timestamp endtime, Timestamp giventime) {
		Integer result = 0;
		Conjunction conj = Restrictions.conjunction();
		if (starttime != null) {
			conj.add(Restrictions.ge("signtime", starttime));
		}
		conj.add(Restrictions.lt("signtime", endtime));
		conj.add(Restrictions.lt("endtime", giventime));
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(STEWorktime.class);
		criteria.add(conj);
		result = criteria.list().size();
		return result;
	}

	public void deleteBySteId(Long steid) {
		String hql = "delete from STEWorktime where steid = :steid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("steid", steid).executeUpdate();
	}

}

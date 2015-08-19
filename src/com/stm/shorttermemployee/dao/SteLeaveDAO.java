package com.stm.shorttermemployee.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.pojo.SteLeave;
import com.stm.shorttermemployee.util.Constants;

@Component
public class SteLeaveDAO extends BasicDAO {

	@SuppressWarnings("unchecked")
	public List<SteLeave> getLeaveAcross(Long steid, Timestamp starttime, Timestamp endtime) {
		List<SteLeave> result;
		Conjunction and = Restrictions.conjunction();
		if (starttime != null) {
			and.add(Restrictions.ge("endtime", starttime));
		}
		and.add(Restrictions.lt("starttime", endtime));
		and.add(Restrictions.eq("steid", steid));
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SteLeave.class);
		result = criteria.add(and).list();
		return result;
	}

	public void deleteBySteId(Long steid) {
		String hql = "delete from SteLeave where steid = :steid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("steid", steid).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<SteLeave> getPassedBySteId(Long steId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(SteLeave.class);
		c.add(Restrictions.eq("steid", steId));
		c.add(Restrictions.eq("status", Constants.ASK_FOR_LEAVE_PASS));
		return c.list();
	}

}

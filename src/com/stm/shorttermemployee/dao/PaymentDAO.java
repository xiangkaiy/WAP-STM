package com.stm.shorttermemployee.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.pojo.Payment;

@Component
public class PaymentDAO extends BasicDAO {
	public void deleteBySteId(Long steid) {
		String hql = "delete from Payment where steid = :steid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("steid", steid).executeUpdate();
	}

	public void deleteByHrId(Long hrid) {
		String hql = "delete from Payment where hrid = :hrid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("hrid", hrid).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Payment> getByGiveTimeAndHrId(Timestamp starttime, Timestamp endtime, Long hrId) {
		Conjunction conj = Restrictions.conjunction();
		if (starttime != null) {
			conj.add(Restrictions.ge("givetime", starttime));
		}
		if (endtime != null) {
			conj.add(Restrictions.lt("givetime", endtime));
		}
		conj.add(Restrictions.eq("hrid", hrId));
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Payment.class);
		return criteria.add(conj).list();
	}

}

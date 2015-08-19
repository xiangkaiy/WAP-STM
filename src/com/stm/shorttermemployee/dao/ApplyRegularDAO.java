package com.stm.shorttermemployee.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class ApplyRegularDAO extends BasicDAO {
	public void deleteBySteId(Long steId) {
		String hql = "delete from ApplyRegular where steid = :steid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("steid", steId).executeUpdate();
	}
}

package com.stm.shorttermemployee.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class BasicDAO extends HibernateDaoSupport {

	@Autowired
	public void init(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	public void create(Object o) throws HibernateException {
		Session session = getSessionFactory().getCurrentSession();
		session.save(o);
	}

	public void update(Object o) throws HibernateException {
		Session session = getSessionFactory().getCurrentSession();
		session.update(o);
	}

	@SuppressWarnings("rawtypes")
	public Object retrieve(Class class1, Serializable serializable) throws HibernateException {
		Session session = getSessionFactory().getCurrentSession();
		return session.get(class1, serializable);
	}

	public void delete(Object o) throws HibernateException {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(o);
	}

	public boolean deleteById(Class<?> type, Serializable id) {
		Session session = getSessionFactory().getCurrentSession();
		Object persistentInstance = session.load(type, id);
		if (persistentInstance != null) {
			session.delete(persistentInstance);
			return true;
		}
		return false;
	}

	public void saveOrUpdate(Object o) throws HibernateException {
		Session session = getSessionFactory().getCurrentSession();
		session.saveOrUpdate(o);
	}

	@SuppressWarnings({ "rawtypes" })
	public List retrieveByField(Class class1, String fieldName, Object fieldValue) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(class1);
		List result = criteria.add(Restrictions.eq(fieldName, fieldValue)).list();
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	public List retrieveByFieldSort(Class class1, String fieldName, Object fieldValue, String sortFieldName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(class1);
		criteria.add(Restrictions.eq(fieldName, fieldValue));
		criteria.addOrder(Order.desc(sortFieldName));
		return criteria.list();
	}

	@SuppressWarnings("rawtypes")
	public List retrieveByDateFieldAndPro(Class class1, String fieldName, Date fieldvalue, String filedName2, Object fieldvalue2) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String curDateStr = formatter.format(fieldvalue);
		// Create date 17-04-2011 - 00h00
		Date minDate;
		try {
			minDate = formatter.parse(curDateStr);

			Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
			Conjunction and = Restrictions.conjunction();
			// The order date must be >= 17-04-2011 - 00h00
			and.add(Restrictions.ge(fieldName, minDate));
			// And the order date must be < 18-04-2011 - 00h00
			and.add(Restrictions.lt(fieldName, maxDate));

			and.add(Restrictions.eq(filedName2, fieldvalue2));

			Session session = getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(class1);
			return criteria.add(and).list();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public List retrieveByFieldLike(Class class1, String fieldName, String fieldValue, MatchMode mode) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(class1);
		List result = criteria.add(Restrictions.ilike(fieldName, fieldValue, mode)).list();
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	public List retrieveAll(Class T) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria cri = session.createCriteria(T);
		// cri.addOrder(Order.desc());
		return cri.list();
	}

	@SuppressWarnings("rawtypes")
	public List retrieveAllDesc(Class T, String propertyName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria cri = session.createCriteria(T);
		cri.addOrder(Order.desc(propertyName));
		return cri.list();
	}

	@SuppressWarnings("rawtypes")
	public List retrieveAllAsc(Class T, String propertyName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria cri = session.createCriteria(T);
		cri.addOrder(Order.asc(propertyName));
		return cri.list();
	}
}

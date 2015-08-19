package com.stm.shorttermemployee.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.pojo.User;

@Component
@SuppressWarnings("unchecked")
public class UserDAO extends BasicDAO {

	public List<User> getByEmailAndPwd(String email, String pwd) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(User.class);
		c.add(Restrictions.eq("email", email));
		c.add(Restrictions.eq("pwd", pwd));
		return c.list();
	}

}

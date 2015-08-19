package com.stm.shorttermemployee.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.model.UserSTEView;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.util.Constants;

@Component
public class UserSTEDAO extends BasicDAO {
	@SuppressWarnings("rawtypes")
	public List<UserSTEView> getUserSTEViewByDepart(Department depart) {
		List<UserSTEView> result = new LinkedList<UserSTEView>();
		StringBuilder sql = new StringBuilder(Constants.SQL_JOIN_USERSTEVIEW);
		sql.append(" where c.id = "+depart.getId());
		sql.append(" and a.status = " + Constants.STE_WORK);
		Session session = getSessionFactory().getCurrentSession();
		List list = session.createSQLQuery(sql.toString()).addEntity("a", UserSTE.class).addEntity("b", User.class).addEntity("c", Department.class).list();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object[] objects = (Object[]) it.next();
			UserSTE ste = (UserSTE) objects[0];
			User user = (User) objects[1];
			Department departItem = (Department) objects[2];
			UserSTEView view = new UserSTEView(ste, user, departItem);
			result.add(view);
		}
		return result;
	}
}

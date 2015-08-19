package com.stm.shorttermemployee.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.stm.shorttermemployee.model.LeaderApplyView;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.LeaderApplySTE;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.util.Constants;

@Component
public class LeaderApplySTEDAO extends BasicDAO {

	@SuppressWarnings("rawtypes")
	public List<LeaderApplyView> getLeaderApplyListView() {
		StringBuilder sql = new StringBuilder(Constants.SQL_JOIN_LEADERAPPLY);

		Session session = getSessionFactory().getCurrentSession();
		List list = session.createSQLQuery(sql.toString()).addEntity("a", LeaderApplySTE.class).addEntity("b", User.class).addEntity("c", Department.class)
				.list();
		List<LeaderApplyView> result = new LinkedList<LeaderApplyView>();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object[] objects = (Object[]) it.next();
			LeaderApplySTE apply = (LeaderApplySTE) objects[0];
			User user = (User) objects[1];
			Department depart = (Department) objects[2];
			LeaderApplyView view = new LeaderApplyView(user, apply, depart);
			result.add(view);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List<LeaderApplyView> getApplyListViewByLeader(User leader) {
		StringBuilder sql = new StringBuilder(Constants.SQL_JOIN_LEADERAPPLY_LEADERID);

		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setLong("leaderid", leader.getId());
		List list = query.addEntity("a", LeaderApplySTE.class).addEntity("b", User.class).addEntity("c", Department.class).list();
		List<LeaderApplyView> result = new LinkedList<LeaderApplyView>();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object[] objects = (Object[]) it.next();
			LeaderApplySTE apply = (LeaderApplySTE) objects[0];
			User user = (User) objects[1];
			Department depart = (Department) objects[2];
			LeaderApplyView view = new LeaderApplyView(user, apply, depart);
			result.add(view);
		}
		return result;
	}

	public void deleteBySteId(Long steid) {
		String hql = "delete from LeaderApplySTE where steid = :steid";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setLong("steid", steid).executeUpdate();
	}
}

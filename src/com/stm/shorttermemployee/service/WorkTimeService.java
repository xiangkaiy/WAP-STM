package com.stm.shorttermemployee.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stm.shorttermemployee.dao.PropertyDAO;
import com.stm.shorttermemployee.dao.STEWorkTimeDAO;
import com.stm.shorttermemployee.dao.SteLeaveDAO;
import com.stm.shorttermemployee.dao.UserDAO;
import com.stm.shorttermemployee.dao.UserSTEDAO;
import com.stm.shorttermemployee.model.WorkTimeView;
import com.stm.shorttermemployee.pojo.Payment;
import com.stm.shorttermemployee.pojo.Property;
import com.stm.shorttermemployee.pojo.STEWorktime;
import com.stm.shorttermemployee.pojo.SteLeave;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.util.CalculateEngine;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.DateFormat;

@Component
public class WorkTimeService {
	@Autowired
	private PropertyDAO propertydao;

	@Autowired
	private STEWorkTimeDAO steworktimedao;

	@Autowired
	private UserDAO userdao;

	@Autowired
	private UserSTEDAO stedao;

	@Autowired
	private SteLeaveDAO leavedao;

	@SuppressWarnings("unchecked")
	@Transactional
	public void modifyWorkTime(Date starttime, Date endtime) {
		List<Property> stimeList = (List<Property>) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_START_TIME);
		if (stimeList != null && stimeList.size() > 0) {
			Property sTime = stimeList.get(0);
			sTime.setValue(DateFormat.date2Str(starttime, TimeZone.getDefault()));
			propertydao.update(sTime);
		}

		List<Property> etimeList = (List<Property>) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_END_TIME);
		if (etimeList != null && etimeList.size() > 0) {
			Property etime = etimeList.get(0);
			etime.setValue(DateFormat.date2Str(endtime, TimeZone.getDefault()));
			propertydao.update(etime);
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Transactional
	public Date getStartTime() {
		List<Property> stimeList = (List<Property>) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_START_TIME);
		if (stimeList != null && stimeList.size() > 0) {
			String stimeStr = stimeList.get(0).getValue();
			return DateFormat.str2Date(stimeStr, TimeZone.getDefault());
		}
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	@Transactional
	public Date getEndTime() {
		List<Property> stimeList = (List<Property>) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_END_TIME);
		if (stimeList != null && stimeList.size() > 0) {
			String stimeStr = stimeList.get(0).getValue();
			return DateFormat.str2Date(stimeStr, TimeZone.getDefault());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void signIn(Date today, Timestamp time, Long steId) {
		List<STEWorktime> worktimeList = steworktimedao.retrieveByDateFieldAndPro(STEWorktime.class, "signtime", today, "steid", steId);
		if (worktimeList != null && worktimeList.size() > 0) {
			STEWorktime worktime = worktimeList.get(0);
			worktime.setSigntime(today);
			worktime.setStarttime(time);
			worktime.setTotaltime(0L);
			steworktimedao.update(worktime);
		} else {
			STEWorktime worktime = new STEWorktime();
			worktime.setStarttime(time);
			worktime.setSigntime(today);
			worktime.setSteid(steId);
			worktime.setTotaltime(0L);
			steworktimedao.create(worktime);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void signOut(Date today, Timestamp time, Long steId) {
		List<STEWorktime> worktimeList = steworktimedao.retrieveByDateFieldAndPro(STEWorktime.class, "signtime", today, "steid", steId);
		if (worktimeList != null && worktimeList.size() > 0) {
			STEWorktime worktime = worktimeList.get(0);
			worktime.setSigntime(today);
			worktime.setEndtime(time);
			// unit is minute
			Long totaltime = (time.getTime() - worktime.getStarttime().getTime()) / (1000 * 60);
			worktime.setTotaltime(totaltime);
			steworktimedao.update(worktime);
		} else {
			STEWorktime worktime = new STEWorktime();
			worktime.setSigntime(today);
			worktime.setEndtime(time);
			worktime.setSteid(steId);
			Long totaltime = (time.getTime() - worktime.getStarttime().getTime()) / (1000 * 60);
			worktime.setTotaltime(totaltime);
			steworktimedao.create(worktime);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public boolean haveSignIn(User user, Date pDate) {
		List<STEWorktime> worktimeList = steworktimedao.retrieveByDateFieldAndPro(STEWorktime.class, "signtime", pDate, "steid", user.getId());
		if (worktimeList != null && worktimeList.size() != 0) {
			STEWorktime worktime = worktimeList.get(0);
			if (worktime.getStarttime() == null) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public boolean haveSignOut(User user, Date pDate) {
		List<STEWorktime> worktimeList = steworktimedao.retrieveByDateFieldAndPro(STEWorktime.class, "signtime", pDate, "steid", user.getId());
		if (worktimeList != null && worktimeList.size() != 0) {
			STEWorktime worktime = worktimeList.get(0);
			if (worktime.getEndtime() == null) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<STEWorktime> getWorkTimeList(User user) {
		List<STEWorktime> worktimeList = steworktimedao.retrieveByField(STEWorktime.class, "steid", user.getId());
		return worktimeList;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<WorkTimeView> getAllWorkTime() {
		List<WorkTimeView> result = new ArrayList<WorkTimeView>();
		List<STEWorktime> worktimeList = steworktimedao.retrieveAllDesc(STEWorktime.class, "signtime");
		List<User> userList = userdao.retrieveAll(User.class);
		List<UserSTE> steList = stedao.retrieveAll(UserSTE.class);

		for (STEWorktime worktime : worktimeList) {
			for (User u : userList) {
				for (UserSTE ste : steList) {
					if (worktime.getSteid().equals(u.getId()) && u.getId().equals(ste.getId())) {
						WorkTimeView view = new WorkTimeView(u, ste, worktime);
						result.add(view);
					}
				}
			}
		}
		return result;
	}

	private boolean isBetween(Date d, Date min, Date max) {
		if (min == null) {
			if (d.after(max) == false) {
				return true;
			} else {
				return false;
			}
		} else {
			return (d.before(min) == false) && (d.after(max) == false);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Payment getWorktimeInterval(User ste, Date startdate, Date enddate) {
		Payment newPay = new Payment();
		Long total = 0L;
		List<STEWorktime> worklist = steworktimedao.retrieveByField(STEWorktime.class, "steid", ste.getId());
		if (worklist != null && worklist.size() > 0) {
			Date minStartDate = worklist.get(0).getSigntime();
			for (STEWorktime t : worklist) {
				if (isBetween(t.getSigntime(), startdate, enddate)) {
					total += t.getTotaltime();
				}

				if (t.getSigntime().before(minStartDate)) {
					minStartDate = t.getSigntime();
				}
			}

			newPay.setStartdate(minStartDate);

		}
		newPay.setEnddate(enddate);
		newPay.setTotaltime(total);
		return newPay;
	}

	@Transactional
	public Long getLeavetimeInterval(User ste, Date startdate, Date enddate) {
		List<SteLeave> leaveList = null;
		if (startdate == null) {
			leaveList = leavedao.getLeaveAcross(ste.getId(), null, new Timestamp(enddate.getTime()));
		} else {
			leaveList = leavedao.getLeaveAcross(ste.getId(), new Timestamp(startdate.getTime()), new Timestamp(enddate.getTime()));
		}
		Long totalLeave = 0L;
		for (SteLeave s : leaveList) {
			Long cost = 0L;

			if (startdate != null) {
				cost = CalculateEngine.calculateCrossLength(new Timestamp(startdate.getTime()), new Timestamp(enddate.getTime()), s.getStarttime(),
						s.getEndtime());
			} else {
				cost = CalculateEngine.calculateCrossLength(null, new Timestamp(enddate.getTime()), s.getStarttime(), s.getEndtime());
			}
			totalLeave += cost;
		}
		return totalLeave;
	}

	@Transactional
	public Integer getLateAndLeaveEarlyCount(User ste, Date startdate, Date enddate) {
		return getLateCount(ste, startdate, enddate) + getLeaveEarlyCount(ste, startdate, enddate);
		// Property stimeProperty = (Property)
		// propertydao.retrieveByField(Property.class, "name",
		// Constants.PROPERTY_START_TIME).get(0);
		// Property etimeProperty = (Property)
		// propertydao.retrieveByField(Property.class, "name",
		// Constants.PROPERTY_END_TIME).get(0);
		// Date signInTime = DateFormat.str2Date(stimeProperty.getValue(),
		// TimeZone.getDefault());
		// Date signOutTime = DateFormat.str2Date(etimeProperty.getValue(),
		// TimeZone.getDefault());
		//
		// Timestamp startTime = (startdate == null ? null : new
		// Timestamp(startdate.getTime()));
		// Integer lateCount =
		// steworktimedao.getSignInTimeAfterGivenTime(ste.getId(), startTime,
		// new Timestamp(enddate.getTime()),
		// new Timestamp(signInTime.getTime()));
		// Integer earlyleaveCount =
		// steworktimedao.getSignOutTimeBeforeGivenTime(ste.getId(), startTime,
		// new Timestamp(enddate.getTime()), new Timestamp(
		// signOutTime.getTime()));
		// return lateCount + earlyleaveCount;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int getLateCount(User ste, Date startdate, Date enddate) {
		Property stimeProperty = (Property) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_START_TIME).get(0);
		Date signInTime = DateFormat.str2Date(stimeProperty.getValue(), TimeZone.getDefault());

		List<STEWorktime> allRecords = steworktimedao.retrieveByField(STEWorktime.class, "steid", ste.getId());
		int result = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(signInTime);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		for (STEWorktime worktime : allRecords) {
			Calendar tempCalen = Calendar.getInstance();
			tempCalen.setTime(worktime.getStarttime());
			int tempHour = tempCalen.get(Calendar.HOUR_OF_DAY);
			int tempMinute = tempCalen.get(Calendar.MINUTE);
			if (tempHour == hour && tempMinute > minutes) {
				result++;
			}
			if (tempHour > hour) {
				result++;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int getLeaveEarlyCount(User ste, Date startdate, Date enddate) {
		Property etimeProperty = (Property) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_END_TIME).get(0);
		Date signOutTime = DateFormat.str2Date(etimeProperty.getValue(), TimeZone.getDefault());
		List<STEWorktime> allRecords = steworktimedao.retrieveByField(STEWorktime.class, "steid", ste.getId());
		int result = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(signOutTime);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		for (STEWorktime worktime : allRecords) {
			Calendar tempCalen = Calendar.getInstance();
			tempCalen.setTime(worktime.getStarttime());
			int tempHour = tempCalen.get(Calendar.HOUR_OF_DAY);
			int tempMinute = tempCalen.get(Calendar.MINUTE);
			if (tempHour == hour && tempMinute < minutes) {
				result++;
			}
			if (tempHour < hour) {
				result++;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int getTotalSignCount(User ste, Date startdate, Date enddate) {
		int result = 0;
		List<STEWorktime> signRecordList = steworktimedao.retrieveByField(STEWorktime.class, "steid", ste.getId());
		if (signRecordList != null) {
			result = signRecordList.size();
		}
		return 2 * result;
	}

}

package com.stm.shorttermemployee.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stm.shorttermemployee.dao.PaymentDAO;
import com.stm.shorttermemployee.dao.PropertyDAO;
import com.stm.shorttermemployee.pojo.Payment;
import com.stm.shorttermemployee.pojo.Property;
import com.stm.shorttermemployee.pojo.User;

@Component
public class PaymentService {
	@Autowired
	private PaymentDAO paymentdao;

	@Autowired
	private PropertyDAO propertydao;

	@Transactional
	public void payToSTE(User hr, User ste, Timestamp givetime, Float money, Float fine, Date startdate, Date enddate, Long totaltime) {
		Payment pay = new Payment();
		pay.setHrid(hr.getId());
		pay.setEnddate(enddate);
		pay.setGivetime(givetime);
		pay.setMoney(money);
		pay.setStartdate(startdate);
		pay.setSteid(ste.getId());
		pay.setTotaltime(totaltime);
		pay.setFine(fine.toString());
		paymentdao.create(pay);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Payment getLatestPay(User ste) {
		List<Payment> paylist = paymentdao.retrieveByField(Payment.class, "steid", ste.getId());
		Payment result = new Payment();
		if (paylist != null && paylist.size() > 0) {
			Timestamp max = paylist.get(0).getGivetime();
			result = paylist.get(0);
			for (Payment p : paylist) {
				if (p.getGivetime().after(max)) {
					max = p.getGivetime();
					result = p;
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Payment> getPaymentList(User ste) {
		List<Payment> result = paymentdao.retrieveByField(Payment.class, "steid", ste.getId());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Property getFineUnit() {
		List<Property> propertyList = propertydao.retrieveByField(Property.class, "name", "fine");
		if (propertyList != null && propertyList.size() > 0) {
			return propertyList.get(0);
		}
		return null;
	}

	@Transactional
	public void modifyFineUnit(Property fineUnit) {
		propertydao.update(fineUnit);
	}

	@Transactional
	public List<Payment> getTotalSalaryByTimeAndHr(Timestamp starttime, Timestamp endtime, Long hrId) {
		List<Payment> paymentList = paymentdao.getByGiveTimeAndHrId(starttime, endtime, hrId);
		return paymentList;
	}

}

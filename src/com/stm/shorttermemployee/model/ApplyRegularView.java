package com.stm.shorttermemployee.model;

import com.stm.shorttermemployee.pojo.ApplyRegular;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;

public class ApplyRegularView {
	private ApplyRegular applyRegular;
	private User ste;
	private UserSTE userSte;
	private int totalScore;

	public ApplyRegular getApplyRegular() {
		return applyRegular;
	}

	public void setApplyRegular(ApplyRegular applyRegular) {
		this.applyRegular = applyRegular;
	}

	public User getSte() {
		return ste;
	}

	public void setSte(User ste) {
		this.ste = ste;
	}

	public UserSTE getUserSte() {
		return userSte;
	}

	public void setUserSte(UserSTE userSte) {
		this.userSte = userSte;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}

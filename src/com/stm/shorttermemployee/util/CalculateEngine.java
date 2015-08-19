package com.stm.shorttermemployee.util;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.stm.shorttermemployee.model.LeaderApplyView;
import com.stm.shorttermemployee.pojo.SteLeave;
import com.stm.shorttermemployee.pojo.UserSTE;

public class CalculateEngine {
	public static List<UserSTE> filtratedByLeaderApply(List<UserSTE> original, LeaderApplyView apply) {
		List<UserSTE> result = null;
		if (apply != null) {
			result = new LinkedList<UserSTE>();
			for (UserSTE u : original) {
				if (u.getLeasthoursweek() != null) {
					Long leasthourweek = u.getLeasthoursweek();
					Date joindate = u.getJoindate();
					String[] officeskill = u.getOfficeskill().split(Constants.SPLIT_STR);
					String[] programLan = u.getProgramlanguage().split(Constants.SPLIT_STR);
					List<String> officeskillList = Arrays.asList(officeskill);
					List<String> programLanList = Arrays.asList(programLan);
					List<String> requiredOfficeList = Arrays.asList(apply.getApply().getOfficeskill().split(Constants.SPLIT_STR));
					List<String> requiredProgramList = Arrays.asList(apply.getApply().getProgramlanguage().split(Constants.SPLIT_STR));
					if (leasthourweek < apply.getApply().getExpectworkhour()) {
						break;
					}
					if (joindate.after(apply.getApply().getExpectjoindate())) {
						break;
					}
					if (officeskillList.containsAll(requiredOfficeList) == false) {
						break;
					}
					if (programLanList.containsAll(requiredProgramList) == false) {
						break;
					}

					result.add(u);
				}
			}
		}
		return result;
	}

	public static Float calculatePayment(Long totaltime, Float salaryUnit) {
		Long hour = totaltime / 60;
		return hour * salaryUnit;
	}

	// unit is minute
	public static Long minusTimestamp(Timestamp start, Timestamp end) {
		return Math.abs((start.getTime() - end.getTime()) / (1000 * 60));
	}

	public static Long calculateCrossLength(Timestamp time1start, Timestamp time1end, Timestamp time2start, Timestamp time2end) {
		Long result = 0L;
		if (time1start != null) {
			if (time1start.before(time2start) && time1end.before(time2end)) {
				result = minusTimestamp(time2start, time1end);
			} else if (time1start.before(time2start) && time1end.after(time2end)) {
				result = minusTimestamp(time2start, time2end);
			} else if (time1start.after(time2start) && time1end.before(time2end)) {
				result = minusTimestamp(time1start, time1end);
			} else if (time1start.after(time2start) && time1end.after(time2end)) {
				result = minusTimestamp(time1start, time2end);
			}
		} else {
			if ((!time1end.before(time2start)) && (!time1end.after(time2end))) {
				result = minusTimestamp(time2start, time1end);
			} else if (time1end.after(time2end)) {
				result = minusTimestamp(time2start, time2end);
			}
		}
		return result;
	}

	public static int getTrainScore(List<String> correctAnswer, List<String> answer) {
		int score = 0;
		if (correctAnswer == null || answer == null || correctAnswer.size() == 0) {
			return score;
		}
		int everyQuestionScore = 100 / correctAnswer.size();
		int correctNum = 0;

		for (int i = 0; i < answer.size(); i++) {
			if (answer.get(i).equals(correctAnswer.get(i))) {
				correctNum++;
			}
		}
		score = everyQuestionScore * correctNum;

		return score;
	}

	public static boolean checkDateCross(Timestamp time1start, Timestamp time1end, Timestamp time2start, Timestamp time2end) {
		boolean result = false;
		result = !(time1end.before(time2start) || time1end.equals(time2start) || time1start.after(time2end) || time1start.equals(time2end));
		return result;
	}

	public static SteLeave checkAskForLeaveCross(SteLeave leave, List<SteLeave> list) {
		SteLeave result = null;
		for (SteLeave s : list) {
			boolean overlapped = checkDateCross(leave.getStarttime(), leave.getEndtime(), s.getStarttime(), s.getEndtime());
			if (overlapped) {
				result = s;
				break;
			}
		}

		return result;
	}

	public static String mapAnswerToAlpha(String answer) {
		String result = answer.replace(" ", "");
		result = result.replace("1", "A");
		result = result.replace("2", "B");
		result = result.replace("3", "C");
		result = result.replace("4", "D");
		return result;
	}
}

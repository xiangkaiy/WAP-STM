package com.stm.shorttermemployee.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final String PROJECT_NAME = "STE";

	// role list
	public static final String ROLENAME_ADMIN = "admin";
	public static final String ROLENAME_HR = "hr";
	public static final String ROLENAME_STE = "ste";
	public static final String ROLENAME_LEADER = "leader";

	// gender
	public static final String GENDER_MALE = "male";
	public static final String GENDER_FEMALE = "female";

	public static final String SESSION_USER = "user";

	// STE STATUS
	public static final int STE_PREPARE_WORK = 1;
	public static final int STE_WORK = 2;
	public static final int STE_IDEL = 3;
	public static final int STE_LEAVE = 4;
	public static final int STE_LEAVE_OFFICE = 5;

	// SPLIT CHAR
	public static final String SPLIT_STR = ",";

	public static final String TIPID = "tip";

	// office skills
	public static final List<String> officeskill = new ArrayList<String>(Arrays.asList("Word", "Excel", "PowerPoint"));

	// program language skills
	public static final List<String> programLan = new ArrayList<String>(Arrays.asList("Java", "C", "C++", "Python", "PHP", "Objective-C", "C#", "Basic",
			"JavaScript", "HTML5", "CSS"));

	public static final String PROPERTY_START_TIME = "starttime";
	public static final String PROPERTY_END_TIME = "endtime";
	public static final String PROPERTY_REFERENCESCORE = "referencescore";

	// leader apply ste status
	public static final int LEADERAPPLY_SUBMIT = 1;
	public static final int LEADERAPPLY_PASS = 2;
	public static final int LEADERAPPLY_REFUSED = 3;

	public static final String SQL_JOIN_USERSTEVIEW = "select a.*,b.*,c.* from ste_userste a inner join ste_user  b on a.id = b.id inner join ste_department  c on b.departmentid = c.id ";
	public static final String SQL_JOIN_LEADERAPPLY = "select a.*,b.*,c.* from ste_leaderapplyste a inner join ste_user  b on a.leaderid = b.id inner join ste_department  c on b.departmentid = c.id order by c.priority ";
	public static final String SQL_JOIN_LEADERAPPLY_LEADERID = "select a.*,b.*,c.* from ste_leaderapplyste a inner join ste_user  b on a.leaderid = b.id inner join ste_department  c on b.departmentid = c.id  where a.leaderid=:leaderid order by c.priority ";
	public static final String SQL_JOIN_WORKTIMEVIEW = "select a.*,b.* from ste_user a right outer join ste_steworktime b on a.id = b.steid order by b.signtime desc ";

	public static final String SCHEDULE_WORK = "work time";

	public static final String SPLIT_QUESTION = "@";
	public static final String SPLIT_BIGOPTION = "#";
	public static final String SPLIT_SMALLOPTION = "$";

	public static final String TRAIN_UNFINISHED = "unfinish";
	public static final String TRAIN_COMMIT = "commit";
	public static final String TRAIN_PASSED = "pass";
	public static final String TRAIN_FAILED = "fail";

	public static final String ANSWER_SMALL_SPLIT = " ";
	public static final String ANSWER_BIG_SPLIT = "@";

	public static final String ASK_FOR_LEAVE_SUBMIT = "submit";
	public static final String ASK_FOR_LEAVE_PASS = "pass";
	public static final String ASK_FOR_LEAVE_REFUSE = "refuse";

	public static final String APPLY_REGULAR_SUBMIT = "submit";
	public static final String APPLY_REGULAR_PASS = "pass";
	public static final String APPLY_REGULAR_REFUSE = "refuse";

	public static int MAX_TEXT_LENGTH = 2000;
}

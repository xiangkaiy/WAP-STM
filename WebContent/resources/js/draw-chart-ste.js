$(document)
		.ready(
				function() {
					if (document.getElementById("form:workEfficiency") != null) {
						var workEfficiency = parseInt(document
								.getElementById("form:workEfficiency").value);
						var studyAbility = parseInt(document
								.getElementById("form:studyAbility").value);
						var meetingParticipate = parseInt(document
								.getElementById("form:meetingParticipate").value);
						var teamWork = parseInt(document
								.getElementById("form:teamWork").value);
						var evaluationBarData = [
								[ 'WorkEfficiency', workEfficiency ],
								[ 'StudyAbility', studyAbility ],
								[ 'MeetingParticipate', meetingParticipate ],
								[ 'TeamWork', teamWork ] ];

						$('#chart')
								.jqplot(
										[ evaluationBarData ],
										{
											animate : true,
											title : 'Leader\'s Evaluation',
											// Provide a custom seriesColors
											// array
											// to override the
											// default colors.
											seriesColors : [ '#85802b',
													'#00749F', '#73C774',
													'#C7754C' ],
											seriesDefaults : {
												renderer : $.jqplot.BarRenderer,
												rendererOptions : {
													// Set varyBarColor to tru
													// to
													// use the custom
													// colors on the bars.
													varyBarColor : true
												}
											},

											highlighter : {
												show : true,
												sizeAdjust : 7.5,
												tooltipAxes : 'y'
											},

											axes : {
												xaxis : {
													renderer : $.jqplot.CategoryAxisRenderer
												},
												yaxis : {
													min : 0,
													max : 5,
													tickInterval : 1
												}
											}
										});
					}

					if (document.getElementById("form:lateCount") != null) {
						var lateCount = parseInt(document
								.getElementById("form:lateCount").value);
						var leaveEarlyCount = parseInt(document
								.getElementById("form:leaveEarlyCount").value);
						var onScheduleCount = parseInt(document
								.getElementById("form:onScheduleCount").value);

						var maxCount = lateCount > leaveEarlyCount ? (lateCount > onScheduleCount ? lateCount
								: onScheduleCount)
								: (leaveEarlyCount > onScheduleCount ? leaveEarlyCount
										: onScheduleCount);
						var worktimeBar = [ [ 'Late', lateCount ],
								[ 'LeaveEarly', leaveEarlyCount ],
								[ 'OnSchedule', onScheduleCount ] ];

						$('#worktimeChart').jqplot([ worktimeBar ], {
							animate : true,
							title : 'Work Time Statistics(times)',
							// Provide a custom seriesColors array
							// to override the
							// default colors.
							seriesColors : [ '#00749F', '#73C774', '#C7754C' ],
							seriesDefaults : {
								renderer : $.jqplot.BarRenderer,
								rendererOptions : {
									// Set varyBarColor to tru to
									// use the custom
									// colors on the bars.
									varyBarColor : true
								}
							},
							series : [ {
								pointLabels : {
									show : true,
									location : 's',
									ypadding : 5,
								}
							} ],
							// highlighter : {
							// show : true,
							// sizeAdjust : 7.5,
							// tooltipAxes : 'y'
							// },

							axes : {
								xaxis : {
									renderer : $.jqplot.CategoryAxisRenderer
								},
								yaxis : {
									min : 0,
									max : maxCount,
									tickInterval : 3
								}
							}
						});
					}
				});

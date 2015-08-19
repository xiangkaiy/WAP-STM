function checklist(ctrl) {
	var list = document.getElementById("leftmenu").getElementsByTagName('li');
	for ( var i = 0; i < list.length; i++) {
		list[i].className = '';
	}
	ctrl.addClass("selected");
}

function drawWorkTimeBar() {

	if (document.getElementById("detailForm:currentWorkTime") != null
			&& document.getElementById("detailForm:currentWorkTime").value != "") {
		var currentWorkTime = parseInt(document
				.getElementById("detailForm:currentWorkTime").value);
		var currentLeaveTime = parseInt(document
				.getElementById("detailForm:currentLeaveTime").value);
		var maxY = currentWorkTime < currentLeaveTime ? currentLeaveTime
				: currentWorkTime;
		var worktimeBar = [ [ 'Work Time', currentWorkTime ],
				[ 'Leave Time', currentLeaveTime ] ];

		$('#curWorkTimebarChart').jqplot([ worktimeBar ], {
			animate : true,
			title : 'Work Time Statistics(h)',
			// Provide a custom seriesColors array
			// to override the
			// default colors.
			seriesColors : [ '#00749F', '#73C774' ],
			seriesDefaults : {
				renderer : $.jqplot.BarRenderer,
				rendererOptions : {
					// Set varyBarColor to tru to
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
			markerOptions : {
				show : true,
				style : 'filledCircle',
				size : 9
			},
			axes : {
				xaxis : {
					renderer : $.jqplot.CategoryAxisRenderer
				},
				yaxis : {
					min : 0,
					max : maxY,
					tickInterval : maxY / 10
				}
			}
		});
	}
}

function test() {
	if (document.getElementById("form:allDepartment") != null
			&& document.getElementById("form:allDepartment").value != "") {
		var departList = document.getElementById("form:allDepartment").value
				.split("+");
		var amountList = document.getElementById("form:departApplyAmount").value
				.split("+");
		var departAmountData = new Array();
		for ( var i = 0; i < departList.length; i++) {
			var tempArr = new Array();
			tempArr.push(departList[i]);
			tempArr.push(parseInt(amountList[i]));
			departAmountData.push(tempArr);
		}

		var plot1 = jQuery.jqplot('leaderApplyChart', [ departAmountData ], {
			title : "Apply Amount by Department",
			seriesDefaults : {
				// Make this a pie chart.
				renderer : jQuery.jqplot.PieRenderer,
				rendererOptions : {
					// Put data labels on the pie
					// slices.
					// By default, labels show the
					// percentage of the slice.
					showDataLabels : true,
					highlightMouseOver : true
				}
			},
			highlighter : {
				show : true,
				// formatString : '%s,%P',
				tooltipLocation : 'sw',
				tooltipAxes : 'y',
				useAxesFormatters : false
			},
			legend : {
				show : true,
				location : 'e'
			}
		}

		);
	}
}

$(document)
		.ready(
				function() {
					if (document.getElementById("form:allDepartment") != null
							&& document.getElementById("form:allDepartment").value != "") {
						var departList = document
								.getElementById("form:allDepartment").value
								.split("+");
						var amountList = document
								.getElementById("form:departApplyAmount").value
								.split("+");
						var departAmountData = new Array();
						for ( var i = 0; i < departList.length; i++) {
							var tempArr = new Array();
							tempArr.push(departList[i]);
							tempArr.push(parseInt(amountList[i]));
							departAmountData.push(tempArr);
						}

						var plot1 = jQuery.jqplot('leaderApplyChart',
								[ departAmountData ], {
									title : "Apply Amount by Department",
									seriesDefaults : {
										// Make this a pie chart.
										renderer : jQuery.jqplot.PieRenderer,
										rendererOptions : {
											// Put data labels on the pie
											// slices.
											// By default, labels show the
											// percentage of the slice.
											showDataLabels : true,
											highlightMouseOver : true
										}
									},
									highlighter : {
										show : true,
										// formatString : '%s,%P',
										tooltipLocation : 'sw',
										tooltipAxes : 'y',
										useAxesFormatters : false
									},
									legend : {
										show : true,
										location : 'e'
									}
								}

						);
					}
					if (document.getElementById("form01:intervalSalary") != null
							&& document.getElementById("form01:intervalSalary").value != "") {
						var intervalSalary = parseInt(document
								.getElementById("form01:intervalSalary").value);
						var intervalFine = parseInt(document
								.getElementById("form01:intervalFine").value);

						var departList = document
								.getElementById("form01:departList").value
								.split("+");
						var departSalaryList = document
								.getElementById("form01:departSalaryList").value
								.split("+");
						var departFineList = document
								.getElementById("form01:departFineList").value
								.split("+");

						var departSalaryData = new Array();
						var departFineData = new Array();
						for ( var i = 0; i < departList.length; i++) {
							var tempArr = new Array();
							tempArr.push(departList[i]);
							tempArr.push(parseInt(departSalaryList[i]));
							departSalaryData.push(tempArr);

							var tempFineArr = new Array();
							tempFineArr.push(departList[i]);
							tempFineArr.push(parseInt(departFineList[i]));
							departFineData.push(tempFineArr);
						}

						var piedata = [ [ 'Paid Salary', intervalSalary ],
								[ 'Fine', intervalFine ] ];

						var plot = jQuery.jqplot('intervalSalaryDiv',
								[ piedata ], {
									title : "Total Salary($)",
									seriesDefaults : {
										// Make this a pie chart.
										renderer : jQuery.jqplot.PieRenderer,
										rendererOptions : {
											// Put data labels on the pie
											// slices.
											// By default, labels show the
											// percentage of the slice.
											showDataLabels : true,
											highlightMouseOver : true
										}
									},
									highlighter : {
										show : true,
										// formatString : '%s,%P',
										tooltipLocation : 'sw',
										tooltipAxes : 'y',
										useAxesFormatters : false
									},
									legend : {
										show : true,
										location : 'e'
									}
								}

						);

						var plot1 = jQuery.jqplot('departmentSalaryDiv',
								[ departSalaryData ], {
									title : "Salary by Department($)",
									seriesDefaults : {
										// Make this a pie chart.
										renderer : jQuery.jqplot.PieRenderer,
										rendererOptions : {
											// Put data labels on the pie
											// slices.
											// By default, labels show the
											// percentage of the slice.
											showDataLabels : true,
											highlightMouseOver : true
										}
									},
									highlighter : {
										show : true,
										// formatString : '%s,%P',
										tooltipLocation : 'sw',
										tooltipAxes : 'y',
										useAxesFormatters : false
									},
									legend : {
										show : true,
										location : 'e'
									}
								}

						);

						var plot2 = jQuery.jqplot('departmentFineDiv',
								[ departFineData ], {
									title : "Fine by Department($)",
									seriesDefaults : {
										// Make this a pie chart.
										renderer : jQuery.jqplot.PieRenderer,
										rendererOptions : {
											// Put data labels on the pie
											// slices.
											// By default, labels show the
											// percentage of the slice.
											showDataLabels : true,
											highlightMouseOver : true
										}
									},
									highlighter : {
										show : true,
										// formatString : '%s,%P',
										tooltipLocation : 'sw',
										tooltipAxes : 'y',
										useAxesFormatters : false
									},
									legend : {
										show : true,
										location : 'e'
									}
								}

						);
					}

					if (document.getElementById("form:workEfficiency") != null) {
						var workEfficiency = document
								.getElementById("form:workEfficiency").value;
						var studyAbility = document
								.getElementById("form:studyAbility").value;
						var meetingParticipate = document
								.getElementById("form:meetingParticipate").value;
						var teamWork = document.getElementById("form:teamWork").value;
						// teamWork = 3.2;
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

						var line = [ [ 'workEfficiency', workEfficiency ],
								[ 'studyAbility', studyAbility ],
								[ 'meetingParticipate', meetingParticipate ],
								[ 'teamWork', teamWork ] ];

						var worktimeBar = [ [ 'lateCount', lateCount ],
								[ 'leaveEarlyCount', leaveEarlyCount ],
								[ 'onScheduleCount', onScheduleCount ] ];

						$('#chart')
								.jqplot(
										[ line ],
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
												showMarker : false,
												rendererOptions : {
													// Set varyBarColor to tru
													// to
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

											axes : {
												xaxis : {
													renderer : $.jqplot.CategoryAxisRenderer
												},
												yaxis : {
													min : 0,
													max : 5,
													tickInterval : 1,
												/*
												 * tickOptions : { formatString :
												 * '%.1f' }
												 */
												}
											}
										});

						$('#worktimeChart').jqplot([ worktimeBar ], {
							animate : true,
							title : 'Work Time Statistics',
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

							axes : {
								xaxis : {
									renderer : $.jqplot.CategoryAxisRenderer
								},
								yaxis : {
									min : 0,
									max : maxCount,
									tickInterval : 1
								}
							}
						});
					}
				});

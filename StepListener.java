package com.jcp.automation.common.listeners;

import com.jcp.transformer.automation.step.QAFTestStepListener;
import com.jcp.transformer.automation.step.StepExecutionTracker;


public class StepListener implements QAFTestStepListener {

	// Date startDate = new Date();
	// Date endDate = new Date();
	@Override
	public void afterExecute(StepExecutionTracker stepExecutionTracker) {
		// endDate = new Date();
		try {
		} catch (Exception e) {

		}
	}

	@Override
	public void beforExecute(StepExecutionTracker stepExecutionTracker) {
		// perform initial data load to cassandra
		// dataload will be performed only once on first step invocation
		try {
//			CassandraDataLoad.loadAllDataToCassandra();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

		} catch (Exception e) {

		}
		// Reporter.log("TIME : "
		// + ((new Timestamp(endDate.getTime()).getDateTime()) - (new Timestamp(
		// startDate.getTime()).getDateTime())), MessageTypes.Info);
	}

	@Override
	public void onFailure(StepExecutionTracker stepExecutionTracker) {
		try {

		} catch (Exception e) {

		}
	}
}

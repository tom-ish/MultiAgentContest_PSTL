package agent;

import massim.javaagents.agents.CityUtil;
import eis.iilang.Action;

public class ActionUtils extends CityUtil {
	
	static public Action buyAction() {
		return new Action("buy");
	}

	static public Action giveAction() {
		return new Action("give");
	}

	static public Action receiveAction() {
		return new Action("receive");
	}

	static public Action storeAction() {
		return new Action("store");
	}

	static public Action retrieveAction() {
		return new Action("retrieve");
	}

	static public Action retrieve_deliveredAction() {
		return new Action("retrieve_delivered");
	}

	static public Action dumpAction() {
		return new Action("dump");
	}

	static public Action assembleAction() {
		return new Action("assemble");
	}

	static public Action assist_assembleAction() {
		return new Action("assist_assemble");
	}

	static public Action deliver_jobAction() {
		return new Action("deliver_job");
	}

	static public Action chargeAction() {
		return new Action("charge");
	}

	static public Action bid_for_jobAction() {
		return new Action("bid_for_job");
	}

	static public Action post_jobAction() {
		return new Action("post_job");
	}

	static public Action call_breakdown_serviceAction() {
		return new Action("call_breakdown_service");
	}

	static public Action continueAction() {
		return new Action("continue");
	}

	static public Action skipAction() {
		return new Action("skip");
	}

	static public Action abortAction() {
		return new Action("abort");
	}
	
}

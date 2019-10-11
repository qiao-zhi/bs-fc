package cn.qs.bean.fc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Index(name = "memberUserId") // 该注解来自Hibernate包
	private String userId; // 会员ID

	private String profit;

	/**
	 * 投注金额
	 */
	@Index(name = "memberBetAmount") // 该注解来自Hibernate包
	private String betAmount;
	@Index(name = "memberBetCount") // 该注解来自Hibernate包
	private String betCount;
	private String hbGetAmount;
	private String inCount;
	private String activityAmount;
	private String balance;
	private String tipAmount;
	private String parentId;
	private String sendAmount;

	/**
	 * 充值金额
	 */
	@Index(name = "memberInAmount") // 该注解来自Hibernate包
	private String inAmount;
	private String returnAmount;
	private String grade;
	private String userName; // 会员账号
	private String rebateAmount;
	private String hbSendAmount;
	private String outAmount;
	private String outCount;
	private String cancelAmount;
	private String parentName; // 上级名称
	private String getAmount;
	private String profitability;

	/**
	 * 同步日期(标记同步的是哪一天的数据)
	 */
	@Index(name = "memberSyncDate") // 该注解来自Hibernate包
	private String syncDate;

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(String betAmount) {
		this.betAmount = betAmount;
	}

	public String getBetCount() {
		return betCount;
	}

	public void setBetCount(String betCount) {
		this.betCount = betCount;
	}

	public String getHbGetAmount() {
		return hbGetAmount;
	}

	public void setHbGetAmount(String hbGetAmount) {
		this.hbGetAmount = hbGetAmount;
	}

	public String getInCount() {
		return inCount;
	}

	public void setInCount(String inCount) {
		this.inCount = inCount;
	}

	public String getActivityAmount() {
		return activityAmount;
	}

	public void setActivityAmount(String activityAmount) {
		this.activityAmount = activityAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(String tipAmount) {
		this.tipAmount = tipAmount;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(String sendAmount) {
		this.sendAmount = sendAmount;
	}

	public String getInAmount() {
		return inAmount;
	}

	public void setInAmount(String inAmount) {
		this.inAmount = inAmount;
	}

	public String getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(String rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public String getHbSendAmount() {
		return hbSendAmount;
	}

	public void setHbSendAmount(String hbSendAmount) {
		this.hbSendAmount = hbSendAmount;
	}

	public String getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(String outAmount) {
		this.outAmount = outAmount;
	}

	public String getOutCount() {
		return outCount;
	}

	public void setOutCount(String outCount) {
		this.outCount = outCount;
	}

	public String getCancelAmount() {
		return cancelAmount;
	}

	public void setCancelAmount(String cancelAmount) {
		this.cancelAmount = cancelAmount;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getGetAmount() {
		return getAmount;
	}

	public void setGetAmount(String getAmount) {
		this.getAmount = getAmount;
	}

	public String getProfitability() {
		return profitability;
	}

	public void setProfitability(String profitability) {
		this.profitability = profitability;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}

}

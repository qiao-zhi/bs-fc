package cn.qs.bean.fc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FixedReport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * 注册人数
	 */
	private String registerCount;

	/**
	 * 首充人数
	 */
	private String firstinCount;

	/**
	 * 总充金额
	 */
	private String inAmount;

	/**
	 * 总充人数
	 */
	private String inCount;

	/**
	 * 中奖金额
	 */
	private String returnAmount;

	/**
	 * 中将人数
	 */
	private String returnCount;

	/**
	 * 投注人数
	 */
	private String betCount;

	/**
	 * 投注金额
	 */
	private String betAmount;

	/**
	 * 盈利
	 */
	private String profit;

	/**
	 * 赢率
	 */
	private String profitability;

	/**
	 * 投注单量
	 */
	private String betCountAll;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(String registerCount) {
		this.registerCount = registerCount;
	}

	public String getFirstinCount() {
		return firstinCount;
	}

	public void setFirstinCount(String firstinCount) {
		this.firstinCount = firstinCount;
	}

	public String getInAmount() {
		return inAmount;
	}

	public void setInAmount(String inAmount) {
		this.inAmount = inAmount;
	}

	public String getInCount() {
		return inCount;
	}

	public void setInCount(String inCount) {
		this.inCount = inCount;
	}

	public String getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(String returnCount) {
		this.returnCount = returnCount;
	}

	public String getBetCount() {
		return betCount;
	}

	public void setBetCount(String betCount) {
		this.betCount = betCount;
	}

	public String getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(String betAmount) {
		this.betAmount = betAmount;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getProfitability() {
		return profitability;
	}

	public void setProfitability(String profitability) {
		this.profitability = profitability;
	}

	public String getBetCountAll() {
		return betCountAll;
	}

	public void setBetCountAll(String betCountAll) {
		this.betCountAll = betCountAll;
	}

}

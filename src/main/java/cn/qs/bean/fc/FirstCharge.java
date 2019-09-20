package cn.qs.bean.fc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

@Entity
public class FirstCharge {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * userId
	 */
	@Index(name = "firstChargeUserId") // 该注解来自Hibernate包
	private String userId;

	private String amount;
	private String balance;

	/**
	 * 充值时间
	 */
	@Index(name = "firstChargeGmtCreated") // 该注解来自Hibernate包
	private String gmtCreated;

	/**
	 * 充值方式
	 */
	private String remark;

	private String sourceName;

	private String sourceId;

	/**
	 * 会员账号
	 */
	@Index(name = "firstChargeUserName") // 该注解来自Hibernate包
	private String userName;

	/**
	 * 等级 (前面加 VIP )
	 */
	private String grade;

	/**
	 * 注册时间
	 */
	private String registerTime;

	/**
	 * 上级
	 */
	private String parentName;

	/**
	 * 子账号
	 */
	private String secondParentName;

	/**
	 * 描述
	 */
	private String secondParentNameRemark;

	private String remark2;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getGmtCreated() {
		return gmtCreated;
	}

	public void setGmtCreated(String gmtCreated) {
		this.gmtCreated = gmtCreated;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSecondParentName() {
		return secondParentName;
	}

	public void setSecondParentName(String secondParentName) {
		this.secondParentName = secondParentName;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getSecondParentNameRemark() {
		return secondParentNameRemark;
	}

	public void setSecondParentNameRemark(String secondParentNameRemark) {
		this.secondParentNameRemark = secondParentNameRemark;
	}

}

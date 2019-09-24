package cn.qs.bean.fc;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

@Entity
public class LoginLog {

	/**
	 * id 采用同步回来的ID(增量同步)
	 */
	@Id
	private Integer id;

	/**
	 * userId
	 */
	@Index(name = "lastLoginUserId") // 该注解来自Hibernate包
	private String userId;

	/**
	 * 会员账号
	 */
	@Index(name = "lastLoginUserName") // 该注解来自Hibernate包
	private String userName;

	/**
	 * 登录IP
	 */
	private String loginIp;

	/**
	 * 登录地址
	 */
	private String loginLocation;

	/**
	 * 登录网址
	 */
	private String loginUrl;

	/**
	 * 登录方式
	 */
	private String sourceId;

	/**
	 * 登录日期
	 */
	private String gmtModified;

	/**
	 * 同步日期(标记同步的是哪一天的数据)
	 */
	@Index(name = "lastLoginSyncDate") // 该注解来自Hibernate包
	private String syncDate;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginLocation() {
		return loginLocation;
	}

	public void setLoginLocation(String loginLocation) {
		this.loginLocation = loginLocation;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(String gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}

	@Override
	public String toString() {
		return "LastLoginLog [id=" + id + ", userId=" + userId + ", userName=" + userName + ", loginIp=" + loginIp
				+ ", loginLocation=" + loginLocation + ", loginUrl=" + loginUrl + ", sourceId=" + sourceId
				+ ", gmtModified=" + gmtModified + ", syncDate=" + syncDate + "]";
	}

}

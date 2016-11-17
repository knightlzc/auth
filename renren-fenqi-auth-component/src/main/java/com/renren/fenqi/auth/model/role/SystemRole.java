package com.renren.fenqi.auth.model.role;

import java.util.Date;

import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.TableName;

/**
 * Created by shuai.liu3 on 2015/8/21
 */
@TableName(tableName="system_role")
public class SystemRole {
	
	@Colum(columName = "id")
    private int id;
	
	@Colum(columName = "role_id")
    private String roleId;
	
	@Colum(columName = "role_name")
    private String roleName;
	
	@Colum(columName = "creator")
    private String creator;
	
	@Colum(columName = "realm")
	private String realm;
	
	@Colum(columName = "status")
    private int status;
	
	@Colum(columName = "create_time")
    private Date createTime;
	
	@Colum(columName = "update_time")
    private Date updateTime;

    /**
     * 可用
     */
    public static final int STATUS_USER = 0;
    /**
     * 删除
     */
    public static final int STATUS_DELETE = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SystemRole{" +
                "id=" + id +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", creator='" + creator + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

	public String getRealm(){
		return realm;
	}

	public void setRealm(String realm){
		this.realm = realm;
	}
}

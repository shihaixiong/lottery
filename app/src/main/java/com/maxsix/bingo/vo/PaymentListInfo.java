package com.maxsix.bingo.vo;

/**
 * Created by shihaixiong on 2016/6/14.
 */
public class PaymentListInfo {

    /**
     * id : 26
     * user : 2
     * money : 400
     * created : 2016-06-14T08:53:43
     * status : 1
     * method : 4
     */

    private int id;
    private int user;
    private int money;
    private String created;
    private int status;
    private int method;
    private String remark;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

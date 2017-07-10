package com.maxsix.bingo.vo;

/**
 * Created by shihaixiong on 2016/6/14.
 */
public class BettingListInfo {

    /**
     * id : 17
     * stage : {"id":14,"current":"16/064","opened":"","status":0,"created":"2016-06-06T15:12:28","ended":"2016-06-08T21:30:00","lottery":1}
     * created : 2016-06-14T08:53:43
     * times : 4
     * money : 400
     * status : 0
     * name : 1
     * odds : 41.9
     * code : numberbg
     */

    private int id;
    /**
     * id : 14
     * current : 16/064
     * opened :
     * status : 0
     * created : 2016-06-06T15:12:28
     * ended : 2016-06-08T21:30:00
     * lottery : 1
     */

    private StageBean stage;
    private String created;
    private int times;
    private int money;
    private int status;
    private String name;
    private double odds;
    private String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StageBean getStage() {
        return stage;
    }

    public void setStage(StageBean stage) {
        this.stage = stage;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOdds() {
        return odds;
    }

    public void setOdds(double odds) {
        this.odds = odds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class StageBean {
        private int id;
        private String current;
        private String opened;
        private int status;
        private String created;
        private String ended;
        private int lottery;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getOpened() {
            return opened;
        }

        public void setOpened(String opened) {
            this.opened = opened;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getEnded() {
            return ended;
        }

        public void setEnded(String ended) {
            this.ended = ended;
        }

        public int getLottery() {
            return lottery;
        }

        public void setLottery(int lottery) {
            this.lottery = lottery;
        }
    }
}

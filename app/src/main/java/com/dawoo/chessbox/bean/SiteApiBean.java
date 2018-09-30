package com.dawoo.chessbox.bean;

import java.util.List;

/**
 * archar  天纵神武
 **/
public class SiteApiBean {

    /**
     * banner : [{"cover":"/gavin/rcenter/mobile-api/images/ban-01.jpg"}]
     * language : zh_CN
     * phoneDialog : []
     * siteApiRelation : [{"type":"game","name":"德州扑克","cover":"game/QLY/43001.jpg","gameLink":"/mobile-api/origin/getGameLink.html?apiId=43&apiTypeId=5&gameId=430009&gameCode=43001","gameMsg":null,"relation":[],"autoPay":true},{"type":"apiType","name":"捕鱼","cover":"game/QLY/43001.jpg","gameLink":"","gameMsg":null,"relation":[{"type":"game","name":"AG捕鱼王","cover":"game/QLY/53001.jpg","gameLink":"/mobile-api/origin/getGameLink.html?apiId=43&apiTypeId=5&gameId=430009&gameCode=43001","gameMsg":null,"relation":[],"orderNum":null,"autoPay":true}],"autoPay":true},{"type":"apiType","name":"电子","cover":"game/QLY/43001.jpg","gameLink":"","gameMsg":null,"relation":[{"type":"api","name":"MG电子","cover":"game/QLY/53001.jpg","gameLink":"","gameMsg":null,"relation":[{"type":"game","name":"冰球突破","cover":"game/QLY/43001.jpg","gameLink":"/mobile-api/origin/getGameLink.html?apiId=43&apiTypeId=5&gameId=530009&gameCode=53001","gameMsg":null,"relation":[],"autoPay":true}],"orderNum":null,"autoPay":true},{"type":"api","name":"PT电子","cover":"game/QLY/52001.jpg","gameLink":"","gameMsg":null,"relation":[{"type":"game","name":"猫女王","cover":"game/QLY/52001.jpg","gameLink":"/mobile-api/origin/getGameLink.html?apiId=52&apiTypeId=5&gameId=530009&gameCode=53001","gameMsg":null,"relation":[],"autoPay":true},{"type":"game","name":"北极宝藏","cover":"game/QLY/52001.jpg","gameLink":"/mobile-api/origin/getGameLink.html?apiId=52&apiTypeId=5&gameId=530009&gameCode=53001","gameMsg":null,"relation":[],"autoPay":true}],"orderNum":null,"autoPay":true}],"autoPay":true}]
     */

    private String language;
    private List<BannerBean> banner;
    private List<?> phoneDialog;
    private List<SiteApiRelationBean> siteApiRelation;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<?> getPhoneDialog() {
        return phoneDialog;
    }

    public void setPhoneDialog(List<?> phoneDialog) {
        this.phoneDialog = phoneDialog;
    }

    public List<SiteApiRelationBean> getSiteApiRelation() {
        return siteApiRelation;
    }

    public void setSiteApiRelation(List<SiteApiRelationBean> siteApiRelation) {
        this.siteApiRelation = siteApiRelation;
    }

    public static class BannerBean {
        /**
         * cover : /gavin/rcenter/mobile-api/images/ban-01.jpg
         */

        private String cover;
        private String start_time;
        private String carousel_id;
        private String link;
        private String name;


        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getCarousel_id() {
            return carousel_id;
        }

        public void setCarousel_id(String carousel_id) {
            this.carousel_id = carousel_id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SiteApiRelationBean {
        /**
         * type : game
         * name : 德州扑克
         * cover : game/QLY/43001.jpg
         * gameLink : /mobile-api/origin/getGameLink.html?apiId=43&apiTypeId=5&gameId=430009&gameCode=43001
         * gameMsg : null
         * relation : []
         * autoPay : true
         */

        private int apiId;
        private int apiTypeId;
        private String gameId;
        private String type;
        private String name;
        private String cover;
        private String gameLink;
        private Object gameMsg;
        private boolean autoPay;
        private List<SiteApiRelationBean> relation;

        public int getApiId() {
            return apiId;
        }

        public void setApiId(int apiId) {
            this.apiId = apiId;
        }

        public int getApiTypeId() {
            return apiTypeId;
        }

        public void setApiTypeId(int apiTypeId) {
            this.apiTypeId = apiTypeId;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getGameLink() {
            return gameLink;
        }

        public void setGameLink(String gameLink) {
            this.gameLink = gameLink;
        }

        public Object getGameMsg() {
            return gameMsg;
        }

        public void setGameMsg(Object gameMsg) {
            this.gameMsg = gameMsg;
        }

        public boolean isAutoPay() {
            return autoPay;
        }

        public void setAutoPay(boolean autoPay) {
            this.autoPay = autoPay;
        }

        public List<SiteApiRelationBean> getRelation() {
            return relation;
        }

        public void setRelation(List<SiteApiRelationBean> relation) {
            this.relation = relation;
        }
    }


}

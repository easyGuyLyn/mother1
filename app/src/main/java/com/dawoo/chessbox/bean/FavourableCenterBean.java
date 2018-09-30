package com.dawoo.chessbox.bean;

import java.util.List;

public class FavourableCenterBean {
    private boolean success;
    private int code;
    private String message;
    private String title;
    private String version;
    public List<Data> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        private String activityKey;  //活动类型key
        private String activityTypeName;   //活动类型name
        private boolean isSelecte = false;
        public List<ActivityList> activityList; //下级活动列表

        public boolean isSelecte() {
            return isSelecte;
        }

        public void setSelecte(boolean selecte) {
            isSelecte = selecte;
        }

        public String getActivityKey() {
            return activityKey;
        }

        public void setActivityKey(String activityKey) {
            this.activityKey = activityKey;
        }

        public String getActivityTypeName() {
            return activityTypeName;
        }

        public void setActivityTypeName(String activityTypeName) {
            this.activityTypeName = activityTypeName;
        }

        public List<ActivityList> getActivityList() {
            return activityList;
        }

        public void setActivityList(List<ActivityList> activityList) {
            this.activityList = activityList;
        }

        public static class ActivityList {
            private String code;  //活动id
            private String name;    //活动标题
            private boolean isSelecte = false;
            private String photo;
            private String url;
            private String id;
            private String status;
            private String orderNum;
            private String time;
            private String searchId;
            private String activityDetaail;
            private boolean isStartAnimation = false;
            private boolean activityStatus;

            public boolean isStartAnimation() {
                return isStartAnimation;
            }

            public void setStartAnimation(boolean startAnimation) {
                isStartAnimation = startAnimation;
            }

            public boolean isActivityStatus() {
                return activityStatus;
            }

            public void setActivityStatus(boolean activityStatus) {
                this.activityStatus = activityStatus;
            }

            public String getActivityDetaail() {
                return activityDetaail;
            }

            public void setActivityDetaail(String activityDetaail) {
                this.activityDetaail = activityDetaail;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getSearchId() {
                return searchId;
            }

            public void setSearchId(String searchId) {
                this.searchId = searchId;
            }

            public boolean isSelecte() {
                return isSelecte;
            }

            public void setSelecte(boolean selecte) {
                isSelecte = selecte;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }


}

package com.dawoo.chessbox.bean;

import java.io.Serializable;
import java.util.List;

public class FavourableReslutBean implements Serializable{
    private String success;
    private String code;
    private String title;
    private String message;
    private Data data;
    private String version;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class Data implements Serializable{
        private String actibityTitle;
        private String applyResult;
        private String status;
        private String tips;
        private List<ApplyDetails> applyDetails;

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getActibityTitle() {
            return actibityTitle;
        }

        public void setActibityTitle(String actibityTitle) {
            this.actibityTitle = actibityTitle;
        }

        public String getApplyResult() {
            return applyResult;
        }

        public void setApplyResult(String applyResult) {
            this.applyResult = applyResult;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<ApplyDetails> getApplyDetails() {
            return applyDetails;
        }

        public void setApplyDetails(List<ApplyDetails> applyDetails) {
            this.applyDetails = applyDetails;
        }

        public static class ApplyDetails {
            public String condition;//条件信息
            public boolean showSchedule;//是否显示进度条
            public double standard;//满足条件标准值
            public double reached;//当前值
            public String checkTime;//订单成功时间
            public boolean mayApply;//是否展示申请按钮
            public String transactionNo;//交易订单号
            public boolean satisfy;//是否满足条件

            public String getCondition() {
                return condition;
            }

            public void setCondition(String condition) {
                this.condition = condition;
            }

            public boolean isShowSchedule() {
                return showSchedule;
            }

            public void setShowSchedule(boolean showSchedule) {
                this.showSchedule = showSchedule;
            }

            public double getStandard() {
                return standard;
            }

            public void setStandard(double standard) {
                this.standard = standard;
            }

            public double getReached() {
                return reached;
            }

            public void setReached(double reached) {
                this.reached = reached;
            }

            public String getCheckTime() {
                return checkTime;
            }

            public void setCheckTime(String checkTime) {
                this.checkTime = checkTime;
            }

            public boolean isMayApply() {
                return mayApply;
            }

            public void setMayApply(boolean mayApply) {
                this.mayApply = mayApply;
            }

            public String getTransactionNo() {
                return transactionNo;
            }

            public void setTransactionNo(String transactionNo) {
                this.transactionNo = transactionNo;
            }

            public boolean isSatisfy() {
                return satisfy;
            }

            public void setSatisfy(boolean satisfy) {
                this.satisfy = satisfy;
            }
        }
    }


}

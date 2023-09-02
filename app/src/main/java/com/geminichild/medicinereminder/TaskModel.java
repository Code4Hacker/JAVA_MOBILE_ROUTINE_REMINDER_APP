package com.geminichild.medicinereminder;

public class TaskModel {
    private String ActivityTitle, ActivityDescription, NotifyTime, NotifyDate, taskComplete;

    public TaskModel(String activityTitle, String activityDescription, String notifyTime, /*String notifyDate,*/ String taskComplete) {
        ActivityTitle = activityTitle;
        ActivityDescription = activityDescription;
        NotifyTime = notifyTime;
//        NotifyDate = notifyDate;
        this.taskComplete = taskComplete;
    }

    public String getActivityTitle() {
        return ActivityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        ActivityTitle = activityTitle;
    }

    public String getActivityDescription() {
        return ActivityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        ActivityDescription = activityDescription;
    }

    public String getNotifyTime() {
        return NotifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        NotifyTime = notifyTime;
    }

//    public String getNotifyDate() {
//        return NotifyDate;
//    }
//
//    public void setNotifyDate(String notifyDate) {
//        NotifyDate = notifyDate;
//    }

    public String getTaskComplete() {
        return taskComplete;
    }

    public void setTaskComplete(String taskComplete) {
        this.taskComplete = taskComplete;
    }
}

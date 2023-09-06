package com.geminichild.medicinereminder;

public class TaskModel {
    private String ActivityTitle, ActivityDescription, NotifyTime, ActivityId, taskComplete;

    public TaskModel(String activityTitle, String activityDescription, String notifyTime, String activityId, String taskComplete) {
        ActivityTitle = activityTitle;
        ActivityDescription = activityDescription;
        NotifyTime = notifyTime;
        ActivityId = activityId;
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

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getTaskComplete() {
        return taskComplete;
    }

    public void setTaskComplete(String taskComplete) {
        this.taskComplete = taskComplete;
    }
}

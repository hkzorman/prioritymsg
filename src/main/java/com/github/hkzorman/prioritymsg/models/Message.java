package com.github.hkzorman.prioritymsg.models;

import java.time.LocalTime;

public class Message {

    public static class AudioInfo {
        private boolean isSuccess;
        private String audioFilePath;
        private long duration;

        public AudioInfo() {}

        public AudioInfo(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public AudioInfo(boolean isSuccess, String audioFilePath, long duration) {
            this.isSuccess = isSuccess;
            this.audioFilePath = audioFilePath;
            this.duration = duration;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getAudioFilePath() {
            return audioFilePath;
        }

        public void setAudioFilePath(String audioFilePath) {
            this.audioFilePath = audioFilePath;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }

    private boolean isPriority;
    private String username;
    private LocalTime time;
    private String message;
    private AudioInfo audioInfo;

    public Message() {}

    public boolean isPriority() {
        return isPriority;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AudioInfo getAudioInfo() {
        return audioInfo;
    }

    public void setAudioInfo(AudioInfo audioInfo) {
        this.audioInfo = audioInfo;
    }
}

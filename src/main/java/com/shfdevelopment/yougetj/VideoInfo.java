package com.shfdevelopment.yougetj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class VideoInfo {

    private static final SimpleDateFormat UPLOAD_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public String service;
    public String title;
    public String description;
    public String image;
    public String convertedUrl;
    public Date uploadDate;
    public DownloadOption simpleDownloadOption = new DownloadOption();
    public DownloadOption[] downloadOptions;
    public int timeSeconds;
    public long likes;
    public long views;
    public double rating;

    public VideoInfo() {

    }

    /**
     * Sets {@link #timeSeconds} from a string in the format HOURS:MINUTES:SECONDS (example: 08:44:22)
     * If the input is in the wrong format, nothing happens
     *
     * @param timeStr
     */
    public void setTime(String timeStr) {
        int firstColon = timeStr.indexOf(':');
        int lastColon = timeStr.lastIndexOf(':');

        if (firstColon == lastColon || firstColon <= 0 || lastColon <= 2 || firstColon == lastColon - 1) {
            //not correct format
            return;
        }

        String hourStr = timeStr.substring(0, firstColon);
        String minuteStr = timeStr.substring(firstColon + 1, lastColon);
        String secondStr = timeStr.substring(lastColon + 1);
        int hours;
        int minutes;
        int seconds;

        try {
            hours = Integer.parseInt(hourStr);
            minutes = Integer.parseInt(minuteStr);
            seconds = Integer.parseInt(secondStr);
        } catch (NumberFormatException | NullPointerException e) {
            //not numbers
            return;
        }

        timeSeconds = (hours * 60 * 60) + (minutes * 60) + seconds;
    }

    /**
     * Sets {@link #uploadDate} from a string in the format YEAR/MONTH/DAY (example: 2017/6/17)
     * If the input is in the wrong format, nothing happens
     *
     * @param uploadDateStr
     */
    public void setUploadDate(String uploadDateStr) {
        try {
            uploadDate = UPLOAD_DATE_FORMAT.parse(uploadDateStr);
        } catch (ParseException e) {
            //do nothing
        }
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "service='" + service + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", uploadDate=" + uploadDate +
                ", simpleDownloadOption=" + simpleDownloadOption +
                ", downloadOptions=" + Arrays.toString(downloadOptions) +
                ", timeSeconds=" + timeSeconds +
                ", likes=" + likes +
                ", views=" + views +
                ", rating=" + rating +
                '}';
    }
}

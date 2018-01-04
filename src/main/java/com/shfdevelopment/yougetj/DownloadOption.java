package com.shfdevelopment.yougetj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadOption {

    /**
     * Pattern used in {@link #setFilesize(String)}
     */
    private static final Pattern FILESIZE_PATTERN = Pattern.compile("([\\d\\.]+)\\s+(\\w+)");

    //what unit is used for less than 1024 bytes?
    private static final String[] FILESIZE_UNITS = {"KB", "MB", "GB", "TB"};
    private static final long[] FILESIZE_MULTIPLIERS = {1024, 1024 * 1024, 1024 * 1024 * 1024, 1024 * 1024 * 1024 * 1024};

    public String url;
    public String filenameExtension;
    public String formatId;
    public String formatNote;
    public String audioCodec;
    public String videoCodec;
    public int width = -1;
    public int height = -1;
    public long approxSizeBytes = -1;
    public double bitrate = -1;

    /**
     * Sets the video width and height from a string in the format WIDTHxHEIGHT (example: 1280x720)
     * If the input is in the wrong format, nothing happens
     *
     * @param widthHeight
     */
    public void setWH(String widthHeight) {
        int xIndex = widthHeight.indexOf('x');
        if (xIndex <= 0 || xIndex == widthHeight.length()) {
            //not correct format - x doesn't exist, x is first, or x is last
            return;
        }

        String widthStr = widthHeight.substring(0, xIndex);
        String heightStr = widthHeight.substring(xIndex + 1, widthHeight.length());
        try {
            width = Integer.parseInt(widthStr);
            height = Integer.parseInt(heightStr);
        } catch (NumberFormatException | NullPointerException e) {
            //not valid numbers for width and height
        }
    }

    /**
     * Sets {@link #approxSizeBytes} from a string in a human-readable format (example: 2.97 MB)
     * If the input is in the wrong format, nothing happens
     *
     * @param filesize
     */
    public void setFilesize(String filesize) {

        Matcher matcher = FILESIZE_PATTERN.matcher(filesize);
        if (matcher.find()) {

            double amount = Float.parseFloat(matcher.group(1));
            String unit = matcher.group(2);
            long multiplier = -1;
            for (int i = 0; i < FILESIZE_UNITS.length; i++) {
                if (FILESIZE_UNITS[i].equals(unit)) {
                    multiplier = FILESIZE_MULTIPLIERS[i];
                    break;
                }
            }

            if (multiplier == -1) {
                //unknown unit
                return;
            }

            approxSizeBytes = (long) (amount * multiplier);
        }

    }

    @Override
    public String toString() {
        return "DownloadOption{" +
                "url='" + url + '\'' +
                ", filenameExtension='" + filenameExtension + '\'' +
                ", formatId='" + formatId + '\'' +
                ", formatNote='" + formatNote + '\'' +
                ", audioCodec='" + audioCodec + '\'' +
                ", videoCodec='" + videoCodec + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", approxSizeBytes=" + approxSizeBytes +
                ", bitrate=" + bitrate +
                '}';
    }
}

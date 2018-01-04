package com.shfdevelopment.yougetj;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouGetJ {

    private static final Pattern DOWNLOAD_OPTION_PATTERN = Pattern.compile("Video_(\\d+)_(.+)");

    public static void main(String[] args) throws IOException {
        Examples.downloadDespacitoConverted();
    }

    /**
     * Gets the json data for a video and returns the json parsed. Same as calling {@link #getJson(String, String...)} followed by {@link #parseVideoInfo(JSONObject)}
     *
     * @param videoUrl url of video
     * @param convert  A format to convert the video to. It seems that this only works when converting to audio. Only the first option will be used. Not required
     * @return a VideoInfo object
     * @throws IOException connection error
     */
    public static VideoInfo getDownloadInfo(String videoUrl, String... convert) throws IOException {
        return parseVideoInfo(getJson(videoUrl, convert));
    }

    /**
     * Creates a {@link VideoInfo} instance from json obtained from {@link #getJson(String, String...)}
     *
     * @param json data from {@link #getJson(String, String...)}. Can be null
     * @return a VideoInfo object
     */
    public static VideoInfo parseVideoInfo(JSONObject json) {
        VideoInfo info = new VideoInfo();

        if (json != null) {
            //map is used temporarily because of wierd format of json
            Map<Integer, DownloadOption> downloadOptionsMap = new TreeMap<Integer, DownloadOption>();
            for (String key : json.keySet()) {

                if (key.equals("Video_Time")) {
                    info.setTime(json.getString(key));
                } else if (key.equals("Video_Description")) {
                    info.description = json.getString(key);
                } else if (key.equals("Video_Title")) {
                    info.title = json.getString(key);
                } else if (key.equals("Video_FileName")) {
                    String filename = json.getString(key);
                    //filename is complete filename, need to extract extension
                    int dotIndex = filename.lastIndexOf('.');
                    if (dotIndex != -1) {
                        //dotIndex shouldn't be -1, but just in case it is, prevent the NullPointerException
                        info.simpleDownloadOption.filenameExtension = filename.substring(dotIndex + 1, filename.length());
                    }
                } else if (key.equals("Video_Image")) {
                    info.image = json.getString(key);
                } else if (key.equals("Video_Upload_Date")) {
                    info.setUploadDate(json.getString(key));
                } else if (key.equals("Video_Average_Rating")) {
                    info.rating = json.getDouble(key);
                } else if (key.equals("Video_Link_Count")) {
                    //actually amount of likes (tested on youtube)
                    info.likes = json.getLong(key);
                } else if (key.equals("Video_View_Count")) {
                    info.views = json.getLong(key);
                } else if (key.equals("Video_Key")) {
                    //actually the video service
                    info.service = json.getString(key);
                } else if (key.equals("Video_DownloadURL")) {
                    info.simpleDownloadOption.url = json.getString(key);
                } else if (key.equals("ConvertFile")) {
                    info.convertedUrl = json.getString(key);
                } else {
                    //probably download option info
                    Matcher matcher = DOWNLOAD_OPTION_PATTERN.matcher(key);
                    if (matcher.find()) {
                        int downloadOptionNum = Integer.parseInt(matcher.group(1));
                        DownloadOption downloadOption = downloadOptionsMap.get(downloadOptionNum);
                        if (downloadOption == null) {
                            downloadOption = new DownloadOption();
                            downloadOptionsMap.put(downloadOptionNum, downloadOption);
                        }
                        String attr = matcher.group(2);

                        if (attr.equals("WH")) {
                            //width and height
                            downloadOption.setWH(json.getString(key));
                        } else if (attr.equals("Url")) {
                            downloadOption.url = json.getString(key);
                        } else if (attr.equals("Format_Note")) {
                            downloadOption.formatNote = json.getString(key);
                        } else if (attr.equals("Ext")) {
                            downloadOption.filenameExtension = json.getString(key);
                        } else if (attr.equals("File_Size")) {
                            //file size is in human readable format, so it's only approximate
                            downloadOption.setFilesize(json.getString(key));
                        } else if (attr.equals("Vcodec")) {
                            String vcodec = json.getString(key);
                            if (!vcodec.equals("none")) {
                                downloadOption.videoCodec = vcodec;
                            }
                        } else if (attr.equals("Acodec")) {
                            String acodec = json.getString(key);
                            if (!acodec.equals("none")) {
                                downloadOption.audioCodec = acodec;
                            }
                        } else if (attr.equals("Format_ID")) {
                            downloadOption.formatId = json.getString(key);
                        } else if (attr.equals("Tbr")) {
                            Object bitrate = json.get(key);
                            if (!bitrate.equals(JSONObject.NULL) && bitrate instanceof Double) {
                                downloadOption.bitrate = (double) bitrate;
                            }
                        }
                    }
                }

            }
            info.downloadOptions = downloadOptionsMap.values().toArray(new DownloadOption[downloadOptionsMap.size()]);
        }
        return info;
    }

    /**
     * Gets json data about a video from online-downloader.com
     *
     * @param videoUrl url of video
     * @param convert  A format to convert the video to. It seems that this only works when converting to audio. Only the first option will be used. Not required
     * @return the video data, or null if the video was not found
     * @throws IOException connection error
     */
    public static JSONObject getJson(String videoUrl, String... convert) throws IOException {
        String url = "https://www.online-downloader.com/DL/YT.php?&videourl=" + URLEncoder.encode(videoUrl, "utf8")
                + (convert.length > 0 ? "&convert=" + URLEncoder.encode(convert[0], "utf8") : "");

        URLConnection conn = new URL(url).openConnection();
        //need to add referer to make converting work
        //youtube-avi uses the same api as online-downloader, but uses conversion
        conn.addRequestProperty("Referer", "http://www.youtube-avi.com/");
        conn.connect();
        InputStream in = conn.getInputStream();
        byte[] buf = new byte[8192];
        int amountRead;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((amountRead = in.read(buf)) != -1) {
            baos.write(buf, 0, amountRead);
        }
        String text = baos.toString("utf8");
        //text has parenthesis at beginning and end because it is jsonp
        JSONObject json = new JSONObject(text.substring(1, text.length() - 1));
        //failures aren't reported, so this is used instead
        if (json.getString("Video_Upload_Date").equals("1970/01/01") && json.getString("Video_Time").equals("00:00:00")) {
            return null;
        }
        return json;
    }

}

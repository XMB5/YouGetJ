package com.shfdevelopment.yougetj;

import java.io.*;
import java.net.URL;

public class Examples {

    public static final String VIDEO_URL = "https://www.youtube.com/watch?v=kJQP7kiw5Fk";
    public static final File DESKTOP = new File(System.getProperty("user.home"), "Desktop");

    public static void downloadDespacitoVideoStandalone() {
        //standalone for readme
        InputStream videoIn = null;
        OutputStream fileOut = null;
        try {
            System.out.println("Getting video info");
            //Luis Fonsi - Despacito ft. Daddy Yankee
            String videoUrl = "https://www.youtube.com/watch?v=kJQP7kiw5Fk";
            VideoInfo info = YouGetJ.getDownloadInfo(videoUrl);

            //there are many options of what to download (only video, video and audio, only audio)
            //simpleDownloadOption has video, audio, and good quality
            String downloadUrl = info.simpleDownloadOption.url;
            System.out.println("Downloading from " + downloadUrl);

            //create connection to download url
            videoIn = new URL(downloadUrl).openStream();

            //output on desktop to a file called DespacitoVideo.{video format}
            File desktop = new File(System.getProperty("user.home"), "Desktop");
            File outputFile = new File(desktop, "DespacitoVideo." + info.simpleDownloadOption.filenameExtension);
            fileOut = new FileOutputStream(outputFile);

            //read from input stream, and write to output stream
            byte[] buf = new byte[8192];
            int amountRead;
            while ((amountRead = videoIn.read(buf)) != -1) {
                fileOut.write(buf, 0, amountRead);
            }

            System.out.println("Downloaded");
        } catch (IOException e) {
            //most likely connection error
            e.printStackTrace();
        } finally {
            try {
                //close streams
                if (videoIn != null) {
                    videoIn.close();
                }
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                //error when closing, should not happen
                e.printStackTrace();
            }
        }
    }

    public static void downloadDespacitoAudioStandalone() {
        //standalone for readme
        InputStream videoIn = null;
        OutputStream fileOut = null;
        try {
            System.out.println("Getting video info");
            //Luis Fonsi - Despacito ft. Daddy Yankee
            String videoUrl = "https://www.youtube.com/watch?v=kJQP7kiw5Fk";
            VideoInfo info = YouGetJ.getDownloadInfo(videoUrl);

            //find the best option that is audio only
            //bitrate is used to compare audio quality
            DownloadOption bestAudioOption = null;
            for (DownloadOption option : info.downloadOptions) {
                if (option.videoCodec == null && option.audioCodec != null) {
                    //this option is audio only
                    if (bestAudioOption == null || bestAudioOption.bitrate < option.bitrate) {
                        //this option is better
                        bestAudioOption = option;
                    }
                }
            }

            String downloadUrl = bestAudioOption.url;
            System.out.println("Downloading from " + downloadUrl);

            //create connection to download url
            videoIn = new URL(downloadUrl).openStream();

            //output on desktop to a file called DespacitoBestAudio.{audio format}
            File desktop = new File(System.getProperty("user.home"), "Desktop");
            File outputFile = new File(desktop, "DespacitoBestAudio." + bestAudioOption.filenameExtension);
            fileOut = new FileOutputStream(outputFile);

            //read from input stream, and write to output stream
            byte[] buf = new byte[8192];
            int amountRead;
            while ((amountRead = videoIn.read(buf)) != -1) {
                fileOut.write(buf, 0, amountRead);
            }

            System.out.println("Downloaded");
        } catch (IOException e) {
            //most likely connection error
            e.printStackTrace();
        } finally {
            try {
                //close streams
                if (videoIn != null) {
                    videoIn.close();
                }
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                //error when closing, should not happen
                e.printStackTrace();
            }
        }
    }

    /**
     * Downloads despacito as an mp3
     * @throws IOException
     */
    public static void downloadDespacitoConverted() throws IOException {
        //very similar code to the example above
        InputStream videoIn = null;
        OutputStream fileOut = null;
        try {
            System.out.println("Getting video info");
            VideoInfo info = YouGetJ.getDownloadInfo(VIDEO_URL, "MP3");

            //use download link to converted video
            String downloadUrl = info.convertedUrl;
            System.out.println("Downloading from " + downloadUrl);

            //extract file extension from URL
            int dot = downloadUrl.lastIndexOf('.');
            String filenameExtension = downloadUrl.substring(dot + 1);

            //create connection to download url
            videoIn = new URL(downloadUrl).openStream();

            //output on desktop to a file called DespacitoConverted.{format}
            File outputFile = new File(DESKTOP, "DespacitoConverted." + filenameExtension);
            fileOut = new FileOutputStream(outputFile);

            readWrite(videoIn, fileOut);

            System.out.println("Downloaded");
        } finally {
            //close streams
            if (videoIn != null) {
                videoIn.close();
            }
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    /**
     * Reads data from an inputstream and writes it to an output stream
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void readWrite(InputStream in, OutputStream out) throws IOException {
        //read from input stream, and write to output stream
        byte[] buf = new byte[8192];
        int amountRead;
        while ((amountRead = in.read(buf)) != -1) {
            out.write(buf, 0, amountRead);
        }
    }

}

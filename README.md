# YouGetJ
Java video/audio downloader that uses [online-downloader](https://www.online-downloader.com)  

Works for youtube, facebook, vimeo, and a lot of other sites. The list of supported sites is [here](https://www.online-downloader.com/Supported-Sites). However, it seems that online-downloader uses [youtube-dl](https://github.com/rg3/youtube-dl), which supports [some other sites](https://rg3.github.io/youtube-dl/supportedsites.html) too.  
  
Uses java 8 and dependends on [org.json/json](https://mvnrepository.com/artifact/org.json/json)

## Examples
The video used in these examples is [Despacito](https://www.youtube.com/watch?v=kJQP7kiw5Fk)  
### Download a video
```java
InputStream videoIn = null;
OutputStream fileOut = null;
try {
    System.out.println("Getting video info");
    //Luis Fonsi - Despacito ft. Daddy Yankee
    String videoUrl = "https://www.youtube.com/watch?v=kJQP7kiw5Fk";
    VideoInfo info = getDownloadInfo(videoUrl);

    //there are many options of what to download (only video, video and audio, only audio)
    //simpleDownloadOption has video, audio, and good quality
    String downloadUrl = info.simpleDownloadOption.url;
    System.out.println("Downloading from " + downloadUrl);

    //create connection to download url
    videoIn = new URL(downloadUrl).openStream();

    //output on desktop to a file called DespacitoVideo.{video format}
    File desktop = new File (System.getProperty("user.home"), "Desktop");
    File outputFile = new File (desktop, "DespacitoVideo." + info.simpleDownloadOption.filenameExtension);
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
```
### Download audio of a video
```java
//very similar code to the example above
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
    for (DownloadOption option: info.downloadOptions) {
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
    File desktop = new File (System.getProperty("user.home"), "Desktop");
    File outputFile = new File (desktop, "DespacitoBestAudio." + bestAudioOption.filenameExtension);
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
```
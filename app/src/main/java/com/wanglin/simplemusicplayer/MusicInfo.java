package com.wanglin.simplemusicplayer;

/**
 * Created by wanglin on 15-7-21.
 */
public class MusicInfo {

    private String Filrname;                            //文件名
    private String Title;                                   //歌曲名称
    private String Singer;                               //歌手名称
    private  int Duration;                               //时间长度
    private String Location;                          //保存地址

    public String getTitle() {
        return Title;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public String getFilrname() {
        return Filrname;
    }

    public void setFilrname(String filrname) {
        Filrname = filrname;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setTitle(String title) {
        Title = title;
    }
}

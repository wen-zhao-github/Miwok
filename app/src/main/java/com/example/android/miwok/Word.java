package com.example.android.miwok;
/**
 * Created by wen-zhao on 9/2/2017.
 */

public class Word {

    private String miwokTranslation;
    private String defaultTranslation;
    //use -1 to be identifier of no resource id
    private int imageResourceId = -1;
    private int audioResourceId = -1;
    //constructor for three parameters, miwork, english and audio resource
    public Word(String miwokTranslation, String defaultTranslation,int audioResourceId){
        this.miwokTranslation = miwokTranslation;
        this.defaultTranslation = defaultTranslation;
        this.audioResourceId = audioResourceId;

    }
    //constructor for four parameters, miwork, english, image resource and audio resource
    public Word(String miwokTranslation, String defaultTranslation,int imageResourceId, int audioResourceId){
        this.miwokTranslation = miwokTranslation;
        this.defaultTranslation = defaultTranslation;
        this.imageResourceId = imageResourceId;
        this.audioResourceId = audioResourceId;
    }
    //getters
    public String getMiwokTranslation(){
        return this.miwokTranslation;
    }
    public String getDefautTranslation(){
        return this.defaultTranslation;
    }
    public int getImageResourceId(){
        return this.imageResourceId;
    }
    public int getAudioResourceId(){
        return this.audioResourceId;
    }

}

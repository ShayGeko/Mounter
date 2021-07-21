package com.example.mounter.data.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.mongodb.User;

public class UserInfoModel extends RealmObject {
    @PrimaryKey @Required
    ObjectId _id = new ObjectId();
    @Required
    String _userId;
    @Required
    private String _partition = "1";
    private String name;
    private String surname;
    private Integer pfp_id = 0;
    private Double rating;
    private Integer numberOfRatings = 0;
    private String sex;


    public UserInfoModel(){

    }
    public UserInfoModel(User user){
        this._userId = user.getId();
    }

    public ObjectId getId(){
        return _id;
    }

    /**
     *
     * @return id of the described user
     */
    public String getUserId(){
        return _userId;
    }

    /**
     *
     * @param userId
     */
    private void setUserId(String userId){
        this._userId = userId;
    }
    /**
     *
     * @return name of the described user
     */
    public String getName(){
        return name;
    }

    /**
     *  Change the name of the user
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }
    /**
     *
     * @return surname of the described user
     */
    public String getSurname(){
        return surname;
    }

    /**
     * Change the surname of the user
     * @param surname
     */
    public void setSurname(String surname){
        this.surname = surname;
    }
    /**
     *  Profile picture id represents the number of the chosen avatar from a predefined set
     * @return profile picture id of the described user
     */
    public int getPfpId(){
        return pfp_id;
    }

    /**
     * Change profile picture id. Changing the id changes the displayed avatar
     * @param pfpId
     */
    public void setPfpId(int pfpId){
        this.pfp_id = pfpId;
    }
    /**
     *
     * @return rating of the described user. Rating is between 0 and 1
     */
    public double getRating(){
        return rating;
    }

    /**
     * Increases the rating of the described user
     */
    public void upvote(){
        // rating = (1 * 1 + 1) / (1 + 1) = 1 -> the user received 2 positive reviews out of 2
        // rating = (0.5 * 2 + 1/ (2 + 1) = 2 / 3 -> the user received 2 positive reviews out of 3
        rating = (rating * numberOfRatings + 1) / ++numberOfRatings;
    }

    /**
     * Decreases the rating of the described user
     */
    public void downvote(){
        // rating = (1 * 2) / (2 + 1) = 2/3  -> the user received 2 positive reviews out of 3
        rating = (rating * numberOfRatings) / ++numberOfRatings;
    }
    /**
     *
     * @return sex of the described user: male or female
     */
    public String getSex(){
        return sex;
    }

    /**
     * change the sex of the specified user
     * @param sex
     */
    public void setSex(String sex){
        this.sex = sex;
    }
}

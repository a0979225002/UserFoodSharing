package tw.org.iii.yichun.foodsharing.Item;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 將AddFood頁面物件化
 */
public class  AddFood implements Serializable {

    private String addFoodImg;
    private String addFoodCategory;
    private String addFoodCity;
    private String addFoodDist;
    private String address;
    private String addFoodDatetime;
    private String addFoodTag;
    private String addFoodAmount;
    private boolean shareIt;
    private String addFoodMemo;
    private String addFoodName;
    private String merge_arrdress;

    public Bitmap getFoodimg() {
        return foodimg;
    }

    public void setFoodimg(Bitmap foodimg) {
        this.foodimg = foodimg;
    }

    private Bitmap foodimg;



    public String getMerge_arrdress() {
        return merge_arrdress;
    }

    public void setMerge_arrdress(String merge_arrdress) {
        this.merge_arrdress = merge_arrdress;
    }

    public String getAddFoodImg() {
        return addFoodImg;
    }

    public void setAddFoodImg(String addFoodImg) {
        this.addFoodImg = addFoodImg;
    }

    public String getAddFoodCategory() {
        return addFoodCategory;
    }

    public void setAddFoodCategory(String addFoodCategory) {
        this.addFoodCategory = addFoodCategory;
    }

    public String getAddFoodCity() {
        return addFoodCity;
    }

    public void setAddFoodCity(String addFoodCity) {
        this.addFoodCity = addFoodCity;
    }

    public String getAddFoodDist() {
        return addFoodDist;
    }

    public void setAddFoodDist(String addFoodDist) {
        this.addFoodDist = addFoodDist;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddFoodDatetime() {
        return addFoodDatetime;
    }

    public void setAddFoodDatetime(String addFoodDatetime) {
        this.addFoodDatetime = addFoodDatetime;
    }

    public String getAddFoodTag() {
        return addFoodTag;
    }

    public void setAddFoodTag(String addFoodTag) {
        this.addFoodTag = addFoodTag;
    }

    public String getAddFoodAmount() {
        return addFoodAmount;
    }

    public void setAddFoodAmount(String addFoodAmount) {
        this.addFoodAmount = addFoodAmount;
    }

    public boolean isShareIt() {
        return shareIt;
    }

    public void setShareIt(boolean shareIt) {
        this.shareIt = shareIt;
    }

    public String getAddFoodMemo() {
        return addFoodMemo;
    }

    public void setAddFoodMemo(String addFoodMemo) {
        this.addFoodMemo = addFoodMemo;
    }

    public String getAddFoodName() {
        return addFoodName;
    }

    public void setAddFoodName(String addFoodName) {
        this.addFoodName = addFoodName;
    }
}

package aohan.zl.echo.echo.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Echo on 2016/8/31.
 */
public class ContactPerson implements Serializable {

    private String contactName;

    private String phoneNumber;

    private Integer contactId;

    private Integer photoId;

    private Bitmap photo;

    public ContactPerson() {
    }


    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public ContactPerson(String contactName, String phoneNumber, Integer contactId, Integer photoId, Bitmap photo) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.contactId = contactId;
        this.photoId = photoId;
        this.photo = photo;
    }



}

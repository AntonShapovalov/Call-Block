package ru.org.adons.cblock.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import ru.org.adons.cblock.model.BlockListModel;

/**
 * DB entity for blocked number, used in {@link BlockListModel}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Entity
public class BlockListItem {

    @Id private Long id;

    @Index(unique = true)
    private String phoneNumber;

    private Long date;
    private String name;
    //
    @Generated(hash = 1704041967)
    public BlockListItem(Long id, String phoneNumber, Long date, String name) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.name = name;
    }
    @Generated(hash = 849339675)
    public BlockListItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Long getDate() {
        return this.date;
    }
    public void setDate(Long date) {
        this.date = date;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

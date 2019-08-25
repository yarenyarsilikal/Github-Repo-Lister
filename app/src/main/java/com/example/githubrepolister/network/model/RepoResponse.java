package com.example.githubrepolister.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepoResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("owner")
    @Expose
    private Owner owner;

    @SerializedName("stargazers_count")
    @Expose
    private Integer stargazersCount;

    @SerializedName("open_issues")
    @Expose
    private Integer openIssues;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Integer getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public Integer getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(Integer openIssues) {
        this.openIssues = openIssues;
    }

    protected RepoResponse(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        owner = (Owner) in.readValue(Owner.class.getClassLoader());
        stargazersCount = in.readByte() == 0x00 ? null : in.readInt();
        openIssues = in.readByte() == 0x00 ? null : in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeValue(owner);
        if (stargazersCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(stargazersCount);
        }
        if (openIssues == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(openIssues);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RepoResponse> CREATOR = new Parcelable.Creator<RepoResponse>() {
        @Override
        public RepoResponse createFromParcel(Parcel in) {
            return new RepoResponse(in);
        }

        @Override
        public RepoResponse[] newArray(int size) {
            return new RepoResponse[size];
        }
    };
}
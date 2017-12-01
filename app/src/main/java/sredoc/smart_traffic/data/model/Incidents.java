package sredoc.smart_traffic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rishabhshukla on 11/10/17.
 */

public class Incidents {
    @SerializedName("incidents") private List<Incident> incidents;
    @SerializedName("mqUrl") private String mqUrl;

    Incidents(){}

    public static class Incident implements Parcelable {

        Incident(){}

        @SerializedName("id") private int id;
        @SerializedName("type") private int type;
        @SerializedName("severity") private int severity;
        @SerializedName("lat") private double lat;
        @SerializedName("lng") private double lng;
        @SerializedName("startTime") private String startTime;
        @SerializedName("endTime") private String endTime;
        @SerializedName("fullDesc") private String fullDesc;
        @SerializedName("delayFromFreeFlow") private double delayFromFreeFlow;
        @SerializedName("delayFromTypical") private double delayFromTypical;
        @SerializedName("distance") private double distance;
        @SerializedName("iconURL") private String iconURL;

        public Incident(int id, int type, int severity, double lat, double lng, String startTime, String endTime, String fullDesc, double delayFromFreeFlow, double delayFromTypical, int distance, String iconURL) {
            this.id = id;
            this.type = type;
            this.severity = severity;
            this.lat = lat;
            this.lng = lng;
            this.startTime = startTime;
            this.endTime = endTime;
            this.fullDesc = fullDesc;
            this.delayFromFreeFlow = delayFromFreeFlow;
            this.delayFromTypical = delayFromTypical;
            this.distance = distance;
            this.iconURL = iconURL;
        }

        protected Incident(Parcel in) {
            id = in.readInt();
            type = in.readInt();
            severity = in.readInt();
            lat = in.readDouble();
            lng = in.readDouble();
            startTime = in.readString();
            endTime = in.readString();
            fullDesc = in.readString();
            delayFromFreeFlow = in.readDouble();
            delayFromTypical = in.readDouble();
            distance = in.readDouble();
            iconURL = in.readString();
        }

        public static final Creator<Incident> CREATOR = new Creator<Incident>() {
            @Override
            public Incident createFromParcel(Parcel in) {
                return new Incident(in);
            }

            @Override
            public Incident[] newArray(int size) {
                return new Incident[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSeverity() {
            return severity;
        }

        public void setSeverity(int severity) {
            this.severity = severity;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFullDesc() {
            return fullDesc;
        }

        public void setFullDesc(String fullDesc) {
            this.fullDesc = fullDesc;
        }

        public Double getDelayFromFreeFlow() {
            return delayFromFreeFlow;
        }

        public void setDelayFromFreeFlow(double delayFromFreeFlow) {
            this.delayFromFreeFlow = delayFromFreeFlow;
        }

        public Double getDelayFromTypical() {
            return delayFromTypical;
        }

        public void setDelayFromTypical(double delayFromTypical) {
            this.delayFromTypical = delayFromTypical;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getIconURL() {
            return iconURL;
        }

        public void setIconURL(String iconURL) {
            this.iconURL = iconURL;
        }

        @Override public String toString() {
            return "Incident{" +
                    "id=" + id +
                    ", type=" + type +
                    ", severity=" + severity +
                    ", lat=" + lat +
                    ", lng=" + lng +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", fullDesc='" + fullDesc + '\'' +
                    ", delayFromFreeFlow=" + delayFromFreeFlow +
                    ", delayFromTypical=" + delayFromTypical +
                    ", distance=" + distance +
                    ", iconURL='" + iconURL + '\'' +
                    '}';
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeInt(type);
            parcel.writeInt(severity);
            parcel.writeDouble(lat);
            parcel.writeDouble(lng);
            parcel.writeString(startTime);
            parcel.writeString(endTime);
            parcel.writeString(fullDesc);
            parcel.writeDouble(delayFromFreeFlow);
            parcel.writeDouble(delayFromTypical);
            parcel.writeDouble(distance);
            parcel.writeString(iconURL);
        }
    }

    public Incidents(List<Incident> incidents, String mqUrl) {
        this.incidents = incidents;
        this.mqUrl = mqUrl;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    public String getMqUrl() {
        return mqUrl;
    }

    public void setMqUrl(String mqUrl) {
        this.mqUrl = mqUrl;
    }

    @Override public String toString() {
        return "Incidents{" +
                "incidents=" + incidents +
                ", mqUrl='" + mqUrl + '\'' +
                '}';
    }
}

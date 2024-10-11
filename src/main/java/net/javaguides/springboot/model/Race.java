package net.javaguides.springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "racedata2")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="DATE")
    private String date;  // Keep the date as a string

    @Column(name="COURSE")
    private String course;
    @Column(name="COUNTRY")
    private String country;
    @Column(name="TIME")
    private String time;
    @Column(name="RACEID")
    private String raceId;
    @Column(name="HORSENAME")
    private String horseName;
    @Column(name="JOCKEYNAME")
    private String jockeyName;
    @Column(name="TRAINERNAME")
    private String trainerName;
    @Column(name="SPEED")
    private int speed;

    @Column(name="HORSEBALLOT")
    private String horseBallot;
    @Column(name="FINISHPOS")
    private String finishPos;
    @Column(name="DAYVALUE")
    private int dayValue;
    @Column(name="Racenumber")
    private String raceNumber;
    @Column(name="HORSEAGE")
    private int age;

    @Column(name="RACE DISTANCE")
    private int distance;

    @Column(name="HORSE WEIGHT")
    private int horseWeight;

    @Column(name="DRAWNSTALL")
    private int drawnStall;


//    @Override
//    public String toString() {
//        return "Race{" +
//                "id=" + id +
//                ", date='" + date + '\'' +
//                ", course='" + course + '\'' +
//                ", country='" + country + '\'' +
//                ", time='" + time + '\'' +
//                ", raceId='" + raceId + '\'' +
//                ", horseName='" + horseName + '\'' +
//                ", jockeyName='" + jockeyName + '\'' +
//                ", trainerName='" + trainerName + '\'' +
//                ", speed=" + speed +
//                ", horseBallot='" + horseBallot + '\'' +
//                ", finishPos='" + finishPos + '\'' +
//                ", dayValue=" + dayValue +
//                ", raceNumber='" + raceNumber + '\'' +
//                ", age=" + age +
//                ", distance=" + distance +
//                ", horseWeight=" + horseWeight +
//                ", drawnStall=" + drawnStall +
//                '}';
//    }
//
//    public int getDrawnStall() {
//        return drawnStall;
//    }
//    public void setDrawnStall(int drawnStall) {
//        this.drawnStall = drawnStall;
//    }
//
//    public int getDistance() {
//        return distance;
//    }
//
//    public void setDistance(int distance) {
//        this.distance = distance;
//    }
//
//    public int getHorseWeight() {
//        return horseWeight;
//    }
//
//    public void setHorseWeight(int horseWeight) {
//        this.horseWeight = horseWeight;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    //  Getters and setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getCourse() {
//        return course;
//    }
//
//    public void setCourse(String course) {
//        this.course = course;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getRaceId() {
//        return raceId;
//    }
//
//    public void setRaceId(String raceId) {
//        this.raceId = raceId;
//    }
//
//    public String getHorseName() {
//        return horseName;
//    }
//
//    public void setHorseName(String horseName) {
//        this.horseName = horseName;
//    }
//
//    public String getJockeyName() {
//        return jockeyName;
//    }
//
//    public void setJockeyName(String jockeyName) {
//        this.jockeyName = jockeyName;
//    }
//
//    public String getTrainerName() {
//        return trainerName;
//    }
//
//    public void setTrainerName(String trainerName) {
//        this.trainerName = trainerName;
//    }
//
//    public int getSpeed() {
//        return speed;
//    }
//
//    public void setSpeed(int speed) {
//        this.speed = speed;
//    }
//
//    public String getHorseBallot() {
//        return horseBallot;
//    }
//
//    public void setHorseBallot(String horseBallot) {
//        this.horseBallot = horseBallot;
//    }
//
//    public String getFinishPos() {
//        return finishPos;
//    }
//
//    public void setFinishPos(String finishPos) {
//        this.finishPos = finishPos;
//    }
//
//    public int getDayValue() {
//        return dayValue;
//    }
//
//    public void setDayValue(int dayValue) {
//        this.dayValue = dayValue;
//    }
//
//    public String getRaceNumber() {
//        return raceNumber;
//    }
//
//    public void setRaceNumber(String raceNumber) {
//        this.raceNumber = raceNumber;
//    }
}
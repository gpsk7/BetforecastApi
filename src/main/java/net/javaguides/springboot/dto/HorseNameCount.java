package net.javaguides.springboot.dto;


import jakarta.persistence.*;

@Entity
@Table(name = "racedata2")
public class HorseNameCount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="HORSENAME")
    private String horseName;
    @Column(name="HORSENAMECOUNT")
    private int horseNameCount;
    @Column(name="JOCKEYNAME")
    private String jockeyName;
    @Column(name="JOCKEYNAMECOUNT")
    private int jockeyNameCount;
    @Column(name="TRAINERNAME")
    private String trainerName;
    @Column(name="TRAINERNAMECOUNT")
    private int trainerNameCount;

    public HorseNameCount() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HorseNameCount(String horseName, int horseNameCount, String jockeyName, int jockeyNameCount, String trainerName, int trainerNameCount) {
        this.horseName = horseName;
        this.horseNameCount = horseNameCount;
        this.jockeyName = jockeyName;
        this.jockeyNameCount = jockeyNameCount;
        this.trainerName = trainerName;
        this.trainerNameCount = trainerNameCount;
    }

    // Getters and setters
    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public int getHorseNameCount() {
        return horseNameCount;
    }

    public void setHorseNameCount(int horseNameCount) {
        this.horseNameCount = horseNameCount;
    }

    public String getJockeyName() {
        return jockeyName;
    }

    public void setJockeyName(String jockeyName) {
        this.jockeyName = jockeyName;
    }

    public int getJockeyNameCount() {
        return jockeyNameCount;
    }

    public void setJockeyNameCount(int jockeyNameCount) {
        this.jockeyNameCount = jockeyNameCount;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public int getTrainerNameCount() {
        return trainerNameCount;
    }

    public void setTrainerNameCount(int trainerNameCount) {
        this.trainerNameCount = trainerNameCount;
    }
}

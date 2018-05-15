package hba;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "message", schema = "transmonitor", catalog = "")
public class MessageEntity {
    private Integer idMessage;
    private Double xCoord;
    private Double yCoord;
    private Double speed;
    private LocalDateTime datetime;
    private Integer idVehicle;
    private VehicleEntity vehicleByIdVehicle;

    @Id
    @GeneratedValue
    @Column(name = "id_message", nullable = false, insertable = false, updatable = false)
    public Integer getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Integer idMessage) {
        this.idMessage = idMessage;
    }

    @Basic
    @Column(name = "x_coord", nullable = false, precision = 0)
    public Double getxCoord() {
        return xCoord;
    }

    public void setxCoord(Double xCoord) {
        this.xCoord = xCoord;
    }

    @Basic
    @Column(name = "y_coord", nullable = false, precision = 0)
    public Double getyCoord() {
        return yCoord;
    }

    public void setyCoord(Double yCoord) {
        this.yCoord = yCoord;
    }

    @Basic
    @Column(name = "speed", nullable = false, precision = 0)
    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Basic
    @Column(name = "datetime", nullable = false)
    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    @Basic
    @Column(name = "id_vehicle", nullable = false, insertable = false, updatable = false)
    public Integer getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Integer idVehicle) {
        this.idVehicle = idVehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return Objects.equals(idMessage, that.idMessage) &&
                Objects.equals(xCoord, that.xCoord) &&
                Objects.equals(yCoord, that.yCoord) &&
                Objects.equals(speed, that.speed) &&
                Objects.equals(datetime, that.datetime) &&
                Objects.equals(idVehicle, that.idVehicle);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idMessage, xCoord, yCoord, speed, datetime, idVehicle);
    }

    @ManyToOne
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id_vehicle", nullable = false)
    public VehicleEntity getVehicleByIdVehicle() {
        return vehicleByIdVehicle;
    }

    public void setVehicleByIdVehicle(VehicleEntity vehicleByIdVehicle) {
        this.vehicleByIdVehicle = vehicleByIdVehicle;
    }
}

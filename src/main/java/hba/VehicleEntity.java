package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "vehicle", schema = "transmonitor", catalog = "")
public class VehicleEntity {
    private Integer idVehicle;
    private String model;
    private String regnum;
    private Collection<DriverEntity> driversByIdVehicle;
    private Collection<MessageEntity> messagesByIdVehicle;
    private Collection<WaybillEntity> waybillsByIdVehicle;

    @Id
    @GeneratedValue
    @Column(name = "id_vehicle", nullable = false, insertable = false, updatable = false)
    public Integer getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Integer idVehicle) {
        this.idVehicle = idVehicle;
    }

    @Basic
    @Column(name = "model", nullable = false, length = -1)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Basic
    @Column(name = "regnum", nullable = false, length = -1)
    public String getRegnum() {
        return regnum;
    }

    public void setRegnum(String regnum) {
        this.regnum = regnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleEntity that = (VehicleEntity) o;
        return Objects.equals(idVehicle, that.idVehicle) &&
                Objects.equals(model, that.model) &&
                Objects.equals(regnum, that.regnum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idVehicle, model, regnum);
    }

    @OneToMany(mappedBy = "vehicleByIdVehicle")
    public Collection<DriverEntity> getDriversByIdVehicle() {
        return driversByIdVehicle;
    }

    public void setDriversByIdVehicle(Collection<DriverEntity> driversByIdVehicle) {
        this.driversByIdVehicle = driversByIdVehicle;
    }

    @OneToMany(mappedBy = "vehicleByIdVehicle")
    public Collection<MessageEntity> getMessagesByIdVehicle() {
        return messagesByIdVehicle;
    }

    public void setMessagesByIdVehicle(Collection<MessageEntity> messagesByIdVehicle) {
        this.messagesByIdVehicle = messagesByIdVehicle;
    }

    @OneToMany(mappedBy = "vehicleByIdVehicle")
    public Collection<WaybillEntity> getWaybillsByIdVehicle() {
        return waybillsByIdVehicle;
    }

    public void setWaybillsByIdVehicle(Collection<WaybillEntity> waybillsByIdVehicle) {
        this.waybillsByIdVehicle = waybillsByIdVehicle;
    }
}

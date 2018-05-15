package hba;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "driver", schema = "transmonitor", catalog = "")
public class DriverEntity {
    private Integer idDriver;
    private String fio;
    private String vunum;
    private Integer idVehicle;
    private VehicleEntity vehicleByIdVehicle;

    @Id
    @GeneratedValue
    @Column(name = "id_driver", nullable = false, insertable = false, updatable = false)
    public Integer getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(Integer idDriver) {
        this.idDriver = idDriver;
    }

    @Basic
    @Column(name = "fio", nullable = false, length = -1)
    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Basic
    @Column(name = "vunum", nullable = false, length = -1)
    public String getVunum() {
        return vunum;
    }

    public void setVunum(String vunum) {
        this.vunum = vunum;
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
        DriverEntity that = (DriverEntity) o;
        return Objects.equals(idDriver, that.idDriver) &&
                Objects.equals(fio, that.fio) &&
                Objects.equals(vunum, that.vunum) &&
                Objects.equals(idVehicle, that.idVehicle);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idDriver, fio, vunum, idVehicle);
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

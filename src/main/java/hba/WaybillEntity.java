package hba;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "waybill", schema = "transmonitor", catalog = "")
public class WaybillEntity {
    private Integer idWaybill;
    private Integer idVehicle;
    private Integer idRoute;
    private VehicleEntity vehicleByIdVehicle;
    private RouteEntity routeByIdRoute;

    @Id
    @GeneratedValue
    @Column(name = "id_waybill", nullable = false, insertable = false, updatable = false)
    public Integer getIdWaybill() {
        return idWaybill;
    }

    public void setIdWaybill(Integer idWaybill) {
        this.idWaybill = idWaybill;
    }

    @Basic
    @Column(name = "id_vehicle", nullable = false, insertable = false, updatable = false)
    public Integer getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Integer idVehicle) {
        this.idVehicle = idVehicle;
    }

    @Basic
    @Column(name = "id_route", nullable = false, insertable = false, updatable = false)
    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaybillEntity that = (WaybillEntity) o;
        return Objects.equals(idWaybill, that.idWaybill) &&
                Objects.equals(idVehicle, that.idVehicle) &&
                Objects.equals(idRoute, that.idRoute);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWaybill, idVehicle, idRoute);
    }

    @ManyToOne
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id_vehicle", nullable = false)
    public VehicleEntity getVehicleByIdVehicle() {
        return vehicleByIdVehicle;
    }

    public void setVehicleByIdVehicle(VehicleEntity vehicleByIdVehicle) {
        this.vehicleByIdVehicle = vehicleByIdVehicle;
    }

    @ManyToOne
    @JoinColumn(name = "id_route", referencedColumnName = "id_route", nullable = false)
    public RouteEntity getRouteByIdRoute() {
        return routeByIdRoute;
    }

    public void setRouteByIdRoute(RouteEntity routeByIdRoute) {
        this.routeByIdRoute = routeByIdRoute;
    }
}

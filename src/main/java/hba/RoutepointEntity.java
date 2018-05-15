package hba;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "routepoint", schema = "transmonitor", catalog = "")
public class RoutepointEntity {
    private Integer idRoutepoint;
    private Integer idRoute;
    private Integer idPoint;
    private RouteEntity routeByIdRoute;
    private PointEntity pointByIdPoint;

    @Id
    @GeneratedValue
    @Column(name = "id_routepoint", nullable = false, insertable = false, updatable = false)
    public Integer getIdRoutepoint() {
        return idRoutepoint;
    }

    public void setIdRoutepoint(Integer idRoutepoint) {
        this.idRoutepoint = idRoutepoint;
    }

    @Basic
    @Column(name = "id_route", nullable = false, insertable = false, updatable = false)
    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    @Basic
    @Column(name = "id_point", nullable = false, insertable = false, updatable = false)
    public Integer getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(Integer idPoint) {
        this.idPoint = idPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutepointEntity that = (RoutepointEntity) o;
        return Objects.equals(idRoutepoint, that.idRoutepoint) &&
                Objects.equals(idRoute, that.idRoute) &&
                Objects.equals(idPoint, that.idPoint);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idRoutepoint, idRoute, idPoint);
    }

    @ManyToOne
    @JoinColumn(name = "id_route", referencedColumnName = "id_route", nullable = false)
    public RouteEntity getRouteByIdRoute() {
        return routeByIdRoute;
    }

    public void setRouteByIdRoute(RouteEntity routeByIdRoute) {
        this.routeByIdRoute = routeByIdRoute;
    }

    @ManyToOne
    @JoinColumn(name = "id_point", referencedColumnName = "id_point", nullable = false)
    public PointEntity getPointByIdPoint() {
        return pointByIdPoint;
    }

    public void setPointByIdPoint(PointEntity pointByIdPoint) {
        this.pointByIdPoint = pointByIdPoint;
    }
}

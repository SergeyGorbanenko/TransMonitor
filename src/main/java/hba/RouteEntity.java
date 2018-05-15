package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "route", schema = "transmonitor", catalog = "")
public class RouteEntity {
    private Integer idRoute;
    private String name;
    private Collection<RoutepointEntity> routepointsByIdRoute;
    private Collection<WaybillEntity> waybillsByIdRoute;

    @Id
    @GeneratedValue
    @Column(name = "id_route", nullable = false, insertable = false, updatable = false)
    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteEntity that = (RouteEntity) o;
        return Objects.equals(idRoute, that.idRoute) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idRoute, name);
    }

    @OneToMany(mappedBy = "routeByIdRoute")
    public Collection<RoutepointEntity> getRoutepointsByIdRoute() {
        return routepointsByIdRoute;
    }

    public void setRoutepointsByIdRoute(Collection<RoutepointEntity> routepointsByIdRoute) {
        this.routepointsByIdRoute = routepointsByIdRoute;
    }

    @OneToMany(mappedBy = "routeByIdRoute")
    public Collection<WaybillEntity> getWaybillsByIdRoute() {
        return waybillsByIdRoute;
    }

    public void setWaybillsByIdRoute(Collection<WaybillEntity> waybillsByIdRoute) {
        this.waybillsByIdRoute = waybillsByIdRoute;
    }
}

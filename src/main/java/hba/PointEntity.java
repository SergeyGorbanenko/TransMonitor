package hba;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "point", schema = "transmonitor", catalog = "")
public class PointEntity {
    private Integer idPoint;
    private Double xCoord;
    private Double yCoord;
    private String name;
    private Collection<RoutepointEntity> routepointsByIdPoint;

    @Id
    @GeneratedValue
    @Column(name = "id_point", nullable = false, insertable = false, updatable = false)
    public Integer getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(Integer idPoint) {
        this.idPoint = idPoint;
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
        PointEntity that = (PointEntity) o;
        return Objects.equals(idPoint, that.idPoint) &&
                Objects.equals(xCoord, that.xCoord) &&
                Objects.equals(yCoord, that.yCoord) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idPoint, xCoord, yCoord, name);
    }

    @OneToMany(mappedBy = "pointByIdPoint")
    public Collection<RoutepointEntity> getRoutepointsByIdPoint() {
        return routepointsByIdPoint;
    }

    public void setRoutepointsByIdPoint(Collection<RoutepointEntity> routepointsByIdPoint) {
        this.routepointsByIdPoint = routepointsByIdPoint;
    }
}

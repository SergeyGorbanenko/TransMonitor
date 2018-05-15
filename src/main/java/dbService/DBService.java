package dbService;

import hba.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Collection;

public class DBService {

    //Получить список Транспортных средств
    public static Collection<VehicleEntity> loadVehicleList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        Collection<VehicleEntity> vehicleEntityCollection = null;
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<VehicleEntity> query = builder.createQuery(VehicleEntity.class);
            Root<VehicleEntity> root = query.from(VehicleEntity.class);
            query.select(root);
            Query<VehicleEntity> q = session.createQuery(query);
            vehicleEntityCollection = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return vehicleEntityCollection;
    }

    //Получить список Водителей
    public static Collection<DriverEntity> loadDriverList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        Collection<DriverEntity> driverEntityCollection = null;
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<DriverEntity> query = builder.createQuery(DriverEntity.class);
            Root<DriverEntity> root = query.from(DriverEntity.class);
            query.select(root);
            Query<DriverEntity> q = session.createQuery(query);
            driverEntityCollection = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return driverEntityCollection;
    }

    //Получить список Точек
    public static Collection<PointEntity> loadPointList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        Collection<PointEntity> pointEntityCollection = null;
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<PointEntity> query = builder.createQuery(PointEntity.class);
            Root<PointEntity> root = query.from(PointEntity.class);
            query.select(root);
            Query<PointEntity> q = session.createQuery(query);
            pointEntityCollection = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return pointEntityCollection;
    }

    //Получить список Маршрутов
    public static Collection<RouteEntity> loadRouteList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        Collection<RouteEntity> routeEntityCollection = null;
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<RouteEntity> query = builder.createQuery(RouteEntity.class);
            Root<RouteEntity> root = query.from(RouteEntity.class);
            query.select(root);
            Query<RouteEntity> q = session.createQuery(query);
            routeEntityCollection = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return routeEntityCollection;
    }

    //Получить список Путевых листов
    public static Collection<WaybillEntity> loadWaybillList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        Collection<WaybillEntity> waybillEntityCollection = null;
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<WaybillEntity> query = builder.createQuery(WaybillEntity.class);
            Root<WaybillEntity> root = query.from(WaybillEntity.class);
            query.select(root);
            Query<WaybillEntity> q = session.createQuery(query);
            waybillEntityCollection = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return waybillEntityCollection;
    }

    //Получить список Сообщений от конкретного ТС на конкретную дату
    public static Collection<MessageEntity> loadMessageList(VehicleEntity vehicleEntity, LocalDateTime localDateTime) {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        Collection<MessageEntity> messageEntityCollection = null;
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MessageEntity> query = builder.createQuery(MessageEntity.class);
            Root<MessageEntity> root = query.from(MessageEntity.class);
            query.select(root);
            query.where(builder.equal(root.get("idVehicle"), vehicleEntity.getIdVehicle()), builder.greaterThan(root.get("datetime"), localDateTime), builder.lessThan(root.get("datetime"), localDateTime.plusDays(1)));
            Query<MessageEntity> q = session.createQuery(query);
            messageEntityCollection = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return messageEntityCollection;
    }

}

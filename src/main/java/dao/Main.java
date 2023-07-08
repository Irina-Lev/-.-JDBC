package dao;

import entity.Task;
import org.h2.jdbcx.JdbcDataSource;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("productStar");
        dataSource.setUser("user");
        dataSource.setPassword("password");

        initializeDb(dataSource);
        TaskDao taskDao = new TaskDao(dataSource);

        var task = new Task("test", false, LocalDateTime.now());
        taskDao.save(task);
        var task_0 = new Task("test 0", false, LocalDateTime.now());
        taskDao.save(task_0);
        var task_2 = new Task("test 2", false, LocalDateTime.now());
        taskDao.save(task_2);
        var tasks = taskDao.findAll();
        System.out.println(tasks);
        var newest = taskDao.findNewestTasks(2);
        System.out.println(newest);
        var task_1 = taskDao.getById(task.getId());
        System.out.println(task_1);
        taskDao.finishTask(task_1);
        var allNotFinished = taskDao.findAllNotFinished();
        System.out.println(allNotFinished);
        taskDao.deleteById(task_2.getId());
        tasks = taskDao.findAll();
        System.out.println(tasks);

        taskDao.deleteAll();
    }

    private static void initializeDb(DataSource dataSource) {
        try (InputStream inputStream = dataSource.getClass().getResource("/initial.sql").openStream()) {
            String sql = new String(inputStream.readAllBytes());
            try (
                    Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement()
            ) {
                statement.executeUpdate(sql);
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

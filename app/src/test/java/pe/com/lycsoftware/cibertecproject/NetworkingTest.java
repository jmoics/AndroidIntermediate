package pe.com.lycsoftware.cibertecproject;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.restService.NotificationService;
import pe.com.lycsoftware.cibertecproject.restService.TaskService;
import pe.com.lycsoftware.cibertecproject.restService.UserService;
import pe.com.lycsoftware.cibertecproject.util.Networking;
import retrofit2.Call;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkingTest
{

    private static final String FAKE_OBJECT_ID = "Fake_object_id";
    private static final String FAKE_OBJECT_TITULO = "Fake titulo";
    private static final DateTime FAKE_OBJECT_DATE_FROM = DateTime.now();
    private static final DateTime FAKE_OBJECT_DATE_TO = DateTime.now();
    private Networking networking;
    @Mock
    private TaskService mockTaskService;
    @Mock
    private UserService mockUserService;
    @Mock
    private NotificationService mockNotificationService;
    @Mock
    private Call<List<Task>> mockCallListTask;
    @Mock
    private Call<List<Notification>> mockCallListNotification;
    @Mock
    private Call<Task> mockCallTask;
    @Mock
    private Call<Notification> mockCallNotification;
    @Mock
    private Call<User> mockCallUser;
    @Mock
    private Call<List<User>> mockCallListUser;


    @Before
    public void setUp() {
        networking = new Networking(mockTaskService, mockNotificationService, mockUserService);
    }

    @Test
    public void testGetTask(){
        //cuando se llame al listar tasks vas a retornar mockCallListTask
        when(mockTaskService.getTasks()).thenReturn(mockCallListTask);

        networking.getTasks(null);

        verify(mockTaskService).getTasks();
    }

    @Test
    public void testUpdateTask(){
        Task fakeTask = crearFakeTask();
        when(mockTaskService.updateTask(FAKE_OBJECT_ID, fakeTask))
                .thenReturn(mockCallTask);

        networking.updateTask(fakeTask, null);

        verify(mockTaskService).updateTask(FAKE_OBJECT_ID, fakeTask);
    }

    @Test
    public void testCreateTask() {
        Task fakeTask = crearFakeTask();
        when(mockTaskService.createTask(fakeTask)).thenReturn(mockCallTask);

        networking.createTask(fakeTask, null);

        verify(mockTaskService).createTask(fakeTask);
    }

    @Test
    public void testDeleteTask() {
        Task fakeTask = crearFakeTask();
        when(mockTaskService.deleteTask(FAKE_OBJECT_ID)).thenReturn(mockCallTask);

        networking.deleteTask(fakeTask, null);

        verify(mockTaskService).deleteTask(FAKE_OBJECT_ID);
    }

    /*@Test
    public void testGetNotification(){
        //cuando se llame al listar tasks vas a retornar mockCallListNotification
        when(mockNotificationService.getNotification4Task(FAKE_OBJECT_ID)).thenReturn(mockCallListNotification);

        networking.getNotifications4Task(FAKE_OBJECT_ID, null);

        verify(mockNotificationService).getNotification4Task(FAKE_OBJECT_ID);
    }*/

    @Test
    public void testUpdateNotification(){
        Notification fakeNotification = crearFakeNotification();
        when(mockNotificationService.updateNotification(FAKE_OBJECT_ID, fakeNotification))
                .thenReturn(mockCallNotification);

        networking.updateNotification(fakeNotification, null);

        verify(mockNotificationService).updateNotification(FAKE_OBJECT_ID, fakeNotification);
    }

    @Test
    public void testCreateNotification() {
        Notification fakeNotification = crearFakeNotification();
        when(mockNotificationService.createNotification(fakeNotification)).thenReturn(mockCallNotification);

        networking.createNotification(fakeNotification, null);

        verify(mockNotificationService).createNotification(fakeNotification);
    }

    @Test
    public void testDeleteNotification() {
        Notification fakeNotification = crearFakeNotification();
        when(mockNotificationService.deleteNotification(FAKE_OBJECT_ID)).thenReturn(mockCallNotification);

        networking.deleteNotification(fakeNotification, null);

        verify(mockNotificationService).deleteNotification(FAKE_OBJECT_ID);
    }

    /*@Test
    public void testGetUser4Email(){
        //cuando se llame al listar tasks vas a retornar mockCallListUser
        when(mockUserService.getUsers()).thenReturn(mockCallListUser);

        networking.getUser4Email(FAKE_OBJECT_TITULO,null);

        verify(mockUserService).getUsers();
    }

    @Test
    public void testGet4Id(){
        //cuando se llame al listar tasks vas a retornar mockCallListUser
        when(mockUserService.getUsers()).thenReturn(mockCallListUser);

        networking.getUser4Email(FAKE_OBJECT_TITULO,null);

        verify(mockUserService).getUsers();
    }*/

    @Test
    public void testUpdateUser(){
        User fakeUser = crearFakeUser();
        when(mockUserService.updateUser(FAKE_OBJECT_ID, fakeUser))
                .thenReturn(mockCallUser);

        networking.updateUser(fakeUser, null);

        verify(mockUserService).updateUser(FAKE_OBJECT_ID, fakeUser);
    }

    private Task crearFakeTask() {
        Task task = new Task();
        task.setObjectId(FAKE_OBJECT_ID);
        task.setName(FAKE_OBJECT_TITULO);
        task.setTaskTimeStart(FAKE_OBJECT_DATE_FROM);
        task.setTaskTimeFinish(FAKE_OBJECT_DATE_TO);
        task.setActive(true);

        return task;
    }

    private Notification crearFakeNotification() {
        Notification notification = new Notification();
        notification.setObjectId(FAKE_OBJECT_ID);
        notification.setNotificationDate(FAKE_OBJECT_DATE_FROM);
        notification.setActive(true);

        return notification;
    }

    private User crearFakeUser() {
        User user = new User();
        user.setObjectId(FAKE_OBJECT_ID);
        user.setDisplayName(FAKE_OBJECT_TITULO);
        user.setUrlImage(FAKE_OBJECT_TITULO);
        user.setEmail(FAKE_OBJECT_TITULO);

        return user;
    }
}
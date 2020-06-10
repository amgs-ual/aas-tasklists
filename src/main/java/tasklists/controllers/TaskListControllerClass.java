package tasklists.controllers;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import tasklists.models.Task;
import tasklists.models.TaskList;
import tasklists.models.TaskListClass;

public class TaskListControllerClass implements TaskListController {
	private SessionFactory sessionFactory;
	
	public TaskListControllerClass() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()//"resource/hibernate.cfg.xml")
				.build();
        try {
        	this.sessionFactory = 
        			new MetadataSources(registry)
//        			.addAnnotatedClass(TaskListClass.class)
//        			.addAnnotatedClass(TaskClass.class)
        			.buildMetadata().buildSessionFactory();
        } catch(Exception e) {        	
        	e.printStackTrace();
        	StandardServiceRegistryBuilder.destroy(registry);
        	System.exit(1);
        }
	}
	
	@SuppressWarnings("unchecked")
	private List<TaskList> readTaskLists() {
		Session session = sessionFactory.openSession();
		List<TaskList> taskLists = null;
		try {
			taskLists = session.createQuery("FROM tasklists.models.TaskListClass").list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return taskLists;
	}
	
	private TaskList readTaskListById(String taskListId) {
		TaskList taskList = null;
		Session session = sessionFactory.openSession();
		try {			
			taskList = session.find(TaskListClass.class, Integer.parseInt(taskListId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return taskList;
	}
	
	private TaskList readTaskListByName(String name) {
		Session session = sessionFactory.openSession();
		TaskList taskList = null;
		try {
			Query query = session.createQuery("FROM tasklists.models.TaskListClass WHERE name=:name");
			query.setParameter("name", name);
			taskList = (TaskList)query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return taskList;
	}
	
	private void writeTaskList(TaskList taskList) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(taskList);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void close() {
		sessionFactory.close();
	}
	
	public List<TaskList> getTaskLists() {
		return this.readTaskLists();
	}

	public boolean hasTasksLists() {
		return !this.getTaskLists().isEmpty();
	}

	public boolean hasTaskListByName(String taskListName) {
		return this.getTaskListByName(taskListName) != null;
	}
	
	public boolean hasTaskListById(String taskListId) {
		return this.getTaskListById(taskListId) != null;
	}

	public TaskList createTaskList(String taskListName) {
		TaskList taskList = new TaskListClass(taskListName);
		this.writeTaskList(taskList);
		return taskList;
	}

	public TaskList getTaskListByName(String taskListname) {
		return this.readTaskListByName(taskListname);
	}
	
	public TaskList getTaskListById(String taskListId) {
		return this.readTaskListById(taskListId);
	}
	
	public TaskList changeTaskListNameByName(String taskListName, String newTaskListName) {
		TaskList taskList = this.readTaskListByName(taskListName);
		taskList.setName(newTaskListName);
		this.writeTaskList(taskList);
		return taskList;
	}
	
	public TaskList changeTaskListNameById(String taskLisId, String newTaskListName) {
		TaskList taskList = this.readTaskListById(taskLisId);
		taskList.setName(newTaskListName);
		this.writeTaskList(taskList);
		return taskList;
	}
	
	public void deleteTask(String taskListId, String taskId) {
		TaskList taskList = this.getTaskListById(taskListId);
		taskList.removeTask(taskId);
		this.writeTaskList(taskList);
	}
	
	public boolean hasTask(String taskListId, String taskId) {
		TaskList taskList = this.getTaskListById(taskListId);
		return taskList.hasTask(taskId);
	}

	public Task changeTaskDescription(String taskListId, String taskId, String taskDescription) {
		TaskList taskList = this.getTaskListById(taskListId);
		Task task = taskList.getTask(taskId);
		task.setDescription(taskDescription);
		this.writeTaskList(taskList);
		return task;
	}

	public Task changeTaskStatus(String taskListId, String taskId) {
		TaskList taskList = this.getTaskListById(taskListId);
		Task task = taskList.getTask(taskId);
		task.toggle();
		this.writeTaskList(taskList);
		return task;
	}

	public Task createTask(String taskListId, String taskDescription) {
		TaskList taskList = this.getTaskListById(taskListId);
		Task task = taskList.addTask(taskDescription);
		this.writeTaskList(taskList);
		return task;
	}

	public Task getTask(String taskListId, String taskId) {
		TaskList taskList = this.getTaskListById(taskListId);
		Task task = taskList.getTask(taskId);
		return task;
	}

	public void deleteTaskList(String taskListId) {
		TaskList taskList = getTaskListById(taskListId);
		Session session = sessionFactory.openSession();
		if (taskList != null) {
			try {
				session.getTransaction().begin();
				session.delete(taskList);
				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
		}
	}
	
	public JsonObjectBuilder getTaskListsJsonObjectBuilder() {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		for(TaskList taskList : this.getTaskLists()) {
			jsonArrayBuilder.add(taskList.getJsonObjectBuilder());
		}        
        jsonObjectBuilder.add("tasklists", jsonArrayBuilder);
        return jsonObjectBuilder;
	}

}

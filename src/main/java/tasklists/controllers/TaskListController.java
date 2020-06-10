package tasklists.controllers;

import java.util.List;

import javax.json.JsonObjectBuilder;

import tasklists.models.Task;
import tasklists.models.TaskList;

public interface TaskListController {

	List<TaskList> getTaskLists();

	boolean hasTasksLists();

	boolean hasTaskListByName(String taskListName);

	TaskList createTaskList(String taskListName);

	TaskList getTaskListByName(String taskListName);
	
	TaskList getTaskListById(String taskListId);

	TaskList changeTaskListNameByName(String taskListName, String newTaskListName);
	
	TaskList changeTaskListNameById(String taskListId, String newTaskListName);
	
	void deleteTask(String taskListId, String taskId);

	boolean hasTask(String taskListId, String taskId);

	Task changeTaskDescription(String taskListId, String taskId, String taskDescription);

	Task changeTaskStatus(String taskListId, String taskId);

	Task createTask(String taskListId, String taskDescription);
	
	Task getTask(String taskListId, String taskId);

	void deleteTaskList(String taskListId);
	
	void close();

	boolean hasTaskListById(String taskListId);
	
	JsonObjectBuilder getTaskListsJsonObjectBuilder();

}

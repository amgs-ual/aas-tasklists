package tasklists.models;

import java.util.List;

import javax.json.JsonObjectBuilder;

public interface TaskList {

	String getName();

	List<Task> getTasks();
	
	int getId();
	
	void setName(String name);

	void removeTask(String taskId);

	boolean hasTask(String taskId);

	Task getTask(String taskId);

	Task addTask(String description);
	
	JsonObjectBuilder getJsonObjectBuilder();
}

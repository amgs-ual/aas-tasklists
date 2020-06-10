package tasklists.models;

import javax.json.JsonObjectBuilder;

public interface Task {

	String getDescription();

	String getStatus();
	
	int getId();

	void setDescription(String taskDescription);

	void toggle();
	
	JsonObjectBuilder getJsonObjectBuilder();
}

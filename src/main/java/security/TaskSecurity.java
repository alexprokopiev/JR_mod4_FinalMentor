package security;

import model.dto.*;
import model.command.TaskCommand;

import java.util.List;
import java.security.InvalidParameterException;

import static constants.Constants.*;

public class TaskSecurity {

    public void validateTaskData(TaskCommand taskCommand) {
        if (taskCommand.getTitle().length() > 32 || taskCommand.getTitle().length() < 3)
            throw new InvalidParameterException(TASK_TITLE_VALIDATE_ERROR);
        if (taskCommand.getTagTitles().length == 0) throw new InvalidParameterException(UNTAGGED_TASK_ERROR);
    }

    public void validateTagsDelete(String[] tagsId, List<TaskDTO> tasks) {
        for (String tagId : tagsId) {
            for (TaskDTO task : tasks) {
                TagDTO[] tags = task.getTags();
                if (tags.length == 1 && tags[0].getId().equals(tagId)) {
                    throw new InvalidParameterException(TAG_DELETE_ERROR);
                }
            }
        }
    }
}

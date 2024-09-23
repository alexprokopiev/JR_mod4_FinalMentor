package model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.State;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO implements DTO {
    private String id;
    private State state;
    private String title;
    private UserDTO user;
    private TagDTO[] tags;
    private int commentsCount;
    private CommentDTO[] comments;

    public void initCommentsCount() {
        commentsCount = comments.length;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("state", state)
                .append("title", title)
                .append("user", user)
                .append("tags", tags)
                .append("commentsCount", commentsCount)
                .append("comments", comments)
                .toString();
    }
}

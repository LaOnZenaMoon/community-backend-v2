package me.lozm.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.code.CommentType;
import me.lozm.global.model.HierarchyResponseAble;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDetailDto {

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private final Long boardId;
        private final Long commentId;
        private final HierarchyResponseAble hierarchy;
        private final CommentType commentType;
        private final String content;
    }

}

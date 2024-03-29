package me.lozm.domain.board.vo;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.code.BoardType;
import me.lozm.domain.board.code.ContentType;
import me.lozm.global.model.HierarchyResponseAble;
import me.lozm.global.model.dto.PageQueryParameters;
import me.lozm.global.model.entity.HierarchicalEntity;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardPageVo {

    @Getter
    public static class Request {
        private final PageQueryParameters pageQueryParameters;

        public Request(PageQueryParameters pageQueryParameters) {
            Assert.notNull(pageQueryParameters, "PageQueryParameters 는 null 일 수 없습니다.");

            this.pageQueryParameters = pageQueryParameters;
        }
    }

    @Getter
    public static class Element {
        private final Long boardId;
        private final HierarchyResponseAble hierarchy;
        private final BoardType boardType;
        private final ContentType contentType;
        private final Long viewCount;
        private final String title;
        private final String content;

        @QueryProjection
        public Element(Long boardId, HierarchicalEntity hierarchicalBoard, BoardType boardType, ContentType contentType, Long viewCount, String title, String content) {
            this.boardId = boardId;
            this.hierarchy = hierarchicalBoard;
            this.boardType = boardType;
            this.contentType = contentType;
            this.viewCount = viewCount;
            this.title = title;
            this.content = content;
        }
    }

}

package me.lozm.app.board.controller;

import com.github.javafaker.Faker;
import me.lozm.app.board.service.BoardService;
import me.lozm.app.board.service.CommentService;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.documentation.BaseDocumentationTest;
import me.lozm.global.documentation.DocumentationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static me.lozm.app.board.TestUtils.createBoard;
import static me.lozm.app.board.TestUtils.createComment;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_DATA;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_PAGE_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends BaseDocumentationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentHelperService commentHelperService;


    @DisplayName("?????? ?????? ??????(?????????) ??????")
    @Test
    void getComments_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);
        for (int i = 0; i < 77; i++) {
            createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);
        }

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/boards/{boardId}/comments", boardDetailVo.getBoardId())
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        pathParameters(parameterWithName("boardId").description("????????? ID")),
                        DocumentationUtils.getPageParameters(),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, DocumentationUtils.getPageFieldDescriptor())
                                .andWithPrefix(PREFIX_PAGE_DATA, getCommentElementResponseDto())
                ));
    }

    @DisplayName("?????? ?????? ?????? > ??????")
    @Test
    void createComment_hierarchyOrigin_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);

        final Faker faker = new Faker();

        final CommentType commentType = CommentType.NOTICE;
        final String content = faker.lorem().sentence(10);

        final String requestBody = "{\n" +
                "  \"commentType\": \"" + commentType + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
                "}";

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/boards/{boardId}/comments", boardDetailVo.getBoardId())
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(this.documentationHandler.document(
                        pathParameters(parameterWithName("boardId").description("????????? ID")),
                        requestFields(
                                fieldWithPath("hierarchyType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("????????? ?????? ?????? ??????", HierarchyType.class)).optional(),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("????????? ????????? ?????? ?????? ?????? ID").optional(),
                                fieldWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("?????? ??????", CommentType.class)),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("??????")
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, getCommentDetailResponseDto())
                ));
    }

    @DisplayName("?????? ?????? ?????? > ????????? ?????? ??????")
    @Test
    void createComment_hierarchyReplyForOrigin_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);

        CommentDetailVo.Response commentDetailVo = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);

        final Faker faker = new Faker();

        final CommentType commentType = CommentType.NOTICE;
        final String content = faker.lorem().sentence(10);

        final String requestBody = "{\n" +
                "  \"hierarchyType\": \"" + HierarchyType.REPLY_FOR_ORIGIN + "\",\n" +
                "  \"parentId\": " + commentDetailVo.getCommentId() + ",\n" +
                "  \"commentType\": \"" + commentType + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
                "}";

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/boards/{boardId}/comments", boardDetailVo.getBoardId())
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(this.documentationHandler.document(
                        pathParameters(parameterWithName("boardId").description("????????? ID")),
                        requestFields(
                                fieldWithPath("hierarchyType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("????????? ?????? ?????? ??????", HierarchyType.class)).optional(),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("????????? ????????? ?????? ?????? ?????? ID").optional(),
                                fieldWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("?????? ??????", CommentType.class)),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("??????")
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, getCommentDetailResponseDto())
                ));
    }

    @DisplayName("?????? ?????? ?????? > ????????? ?????? ??????")
    @Test
    void createComment_hierarchyReplyForReply_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);

        CommentDetailVo.Response commentDetailVo_origin = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);

        CommentDetailVo.Response commentDetailVo_reply_for_origin = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo_origin.getCommentId(), CommentType.GENERAL, commentService);

        final Faker faker = new Faker();

        final CommentType commentType = CommentType.NOTICE;
        final String content = faker.lorem().sentence(10);

        final String requestBody = "{\n" +
                "  \"hierarchyType\": \"" + HierarchyType.REPLY_FOR_REPLY + "\",\n" +
                "  \"parentId\": " + commentDetailVo_reply_for_origin.getCommentId() + ",\n" +
                "  \"commentType\": \"" + commentType + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
                "}";

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/boards/{boardId}/comments", boardDetailVo.getBoardId())
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(this.documentationHandler.document(
                        pathParameters(parameterWithName("boardId").description("????????? ID")),
                        requestFields(
                                fieldWithPath("hierarchyType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("????????? ?????? ?????? ??????", HierarchyType.class)).optional(),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("????????? ????????? ?????? ?????? ?????? ID").optional(),
                                fieldWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("?????? ??????", CommentType.class)),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("??????")
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, getCommentDetailResponseDto())
                ));
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void updateComment_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);
        CommentDetailVo.Response commentDetailVo = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);

        final Long commentId = commentDetailVo.getCommentId();

        final Faker faker = new Faker();

        final CommentType commentType = CommentType.NOTICE;
        final String content = faker.lorem().sentence(10);

        final String requestBody = "{\n" +
                "  \"commentType\": \"" + commentType + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
                "}";

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/boards/{boardId}/comments/{commentId}", boardDetailVo.getBoardId(), commentId)
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        pathParameters(
                                parameterWithName("boardId").description("????????? ID"),
                                parameterWithName("commentId").description("?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("?????? ??????", CommentType.class)),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("??????")
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, getCommentDetailResponseDto())
                ));

        Comment comment = commentHelperService.getComment(commentId);
        assertEquals(commentType, comment.getCommentType());
        assertEquals(content, comment.getContent());
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void deleteComment_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);
        CommentDetailVo.Response commentDetailVo = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);

        final Long commentId = commentDetailVo.getCommentId();

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/boards/{boardId}/comments/{commentId}", boardDetailVo.getBoardId(), commentId)
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        pathParameters(
                                parameterWithName("boardId").description("????????? ID"),
                                parameterWithName("commentId").description("?????? ID")
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                ));

        assertThrows(IllegalArgumentException.class, () -> commentHelperService.getComment(commentId));
    }

    private List<FieldDescriptor> getCommentDetailResponseDto() {
        return List.of(
                subsectionWithPath("boardId").type(JsonFieldType.NUMBER).description("????????? ID"),
                subsectionWithPath("commentId").type(JsonFieldType.NUMBER).description("?????? ID"),
                subsectionWithPath("hierarchy").type(JsonFieldType.OBJECT).description("?????? ?????????").optional(),
                subsectionWithPath("hierarchy.commonParentId").type(JsonFieldType.NUMBER).description("?????? ?????? ????????? ID").optional(),
                subsectionWithPath("hierarchy.parentId").type(JsonFieldType.NUMBER).description("?????? ????????? ID").optional(),
                subsectionWithPath("hierarchy.groupOrder").type(JsonFieldType.NUMBER).description("?????? ??????").optional(),
                subsectionWithPath("hierarchy.groupLayer").type(JsonFieldType.NUMBER).description("?????? ?????????").optional(),
                subsectionWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("?????? ??????", CommentType.class)),
                subsectionWithPath("content").type(JsonFieldType.STRING).description("??????")
        );
    }

    private List<FieldDescriptor> getCommentElementResponseDto() {
        return List.of(
                subsectionWithPath("commentId").type(JsonFieldType.NUMBER).description("?????? ID"),
                subsectionWithPath("hierarchy").type(JsonFieldType.OBJECT).description("?????? ?????????").optional(),
                subsectionWithPath("hierarchy.commonParentId").type(JsonFieldType.NUMBER).description("?????? ?????? ????????? ID").optional(),
                subsectionWithPath("hierarchy.parentId").type(JsonFieldType.NUMBER).description("?????? ????????? ID").optional(),
                subsectionWithPath("hierarchy.groupOrder").type(JsonFieldType.NUMBER).description("?????? ??????").optional(),
                subsectionWithPath("hierarchy.groupLayer").type(JsonFieldType.NUMBER).description("?????? ?????????").optional(),
                subsectionWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("?????? ??????", CommentType.class)),
                subsectionWithPath("content").type(JsonFieldType.STRING).description("??????")
        );
    }

}

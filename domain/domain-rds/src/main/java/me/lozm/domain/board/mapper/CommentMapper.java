package me.lozm.domain.board.mapper;

import me.lozm.domain.board.dto.CommentCreateDto;
import me.lozm.domain.board.dto.CommentDetailDto;
import me.lozm.domain.board.dto.CommentPageDto;
import me.lozm.domain.board.dto.CommentUpdateDto;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.vo.CommentCreateVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.domain.board.vo.CommentPageVo;
import me.lozm.domain.board.vo.CommentUpdateVo;
import me.lozm.global.model.dto.PageQueryParameters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "boardId", target = "boardId")
    @Mapping(source = "pageQueryParameters", target = "pageQueryParameters")
    CommentPageVo.Request toPageVo(Long boardId, PageQueryParameters pageQueryParameters);

    CommentPageDto.Response toPageDto(CommentPageVo.Element element);


    @Mapping(source = "requestDto.hierarchyType", target = "hierarchyType")
    @Mapping(source = "requestDto.parentId", target = "parentId")
    @Mapping(source = "requestDto.commentType", target = "commentType")
    @Mapping(source = "requestDto.content", target = "content")
    CommentCreateVo.Request toCreateVo(Long boardId, CommentCreateDto.Request requestDto);

    CommentDetailDto.Response toDetailDto(CommentDetailVo.Response commentDetailVo);

    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "id", target = "commentId")
    CommentDetailVo.Response toDetailVo(Comment comment);

    @Mapping(source = "requestDto.commentType", target = "commentType")
    @Mapping(source = "requestDto.content", target = "content")
    CommentUpdateVo.Request toUpdateVo(Long boardId, Long commentId, CommentUpdateDto.Request requestDto);
}

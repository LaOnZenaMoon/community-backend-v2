package me.lozm.app.board.controller;

import lombok.RequiredArgsConstructor;
import me.lozm.app.board.service.CommentService;
import me.lozm.domain.board.dto.CommentCreateDto;
import me.lozm.domain.board.dto.CommentDetailDto;
import me.lozm.domain.board.dto.CommentPageDto;
import me.lozm.domain.board.dto.CommentUpdateDto;
import me.lozm.domain.board.mapper.CommentMapper;
import me.lozm.domain.board.vo.CommentCreateVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.domain.board.vo.CommentPageVo;
import me.lozm.domain.board.vo.CommentUpdateVo;
import me.lozm.global.model.CommonResponseDto;
import me.lozm.global.model.dto.CommonPageResponseDto;
import me.lozm.global.model.dto.PageQueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("boards/{boardId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @GetMapping
    public ResponseEntity<CommonResponseDto<CommonPageResponseDto<CommentPageDto.Response>>> getComments(PageQueryParameters pageQueryParameters,
                                                                                                         @PathVariable("boardId") Long boardId) {

        CommentPageVo.Request requestVo = commentMapper.toPageVo(boardId, pageQueryParameters);
        Page<CommentPageVo.Element> responsePageVo = commentService.getComments(requestVo);

        List<CommentPageDto.Response> responseDtoList = responsePageVo.getContent().stream().map(commentMapper::toPageDto).collect(Collectors.toList());
        return CommonResponseDto.ok(new CommonPageResponseDto<>(responsePageVo, responseDtoList));
    }

    @PostMapping
    public ResponseEntity<CommonResponseDto<CommentDetailDto.Response>> createComment(@RequestBody @Validated CommentCreateDto.Request requestDto,
                                                                                      @PathVariable("boardId") Long boardId) {

        CommentCreateVo.Request commentCreateVo = commentMapper.toCreateVo(boardId, requestDto);
        CommentDetailVo.Response commentDetailVo = commentService.createComment(commentCreateVo);
        CommentDetailDto.Response responseDto = commentMapper.toDetailDto(commentDetailVo);
        return CommonResponseDto.created(responseDto);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<CommonResponseDto<CommentDetailDto.Response>> updateComment(@RequestBody @Validated CommentUpdateDto.Request requestDto,
                                                                                      @PathVariable("boardId") Long boardId,
                                                                                      @PathVariable("commentId") Long commentId) {

        CommentUpdateVo.Request commentUpdateVo = commentMapper.toUpdateVo(boardId, commentId, requestDto);
        CommentDetailVo.Response commentDetailVo = commentService.updateComment(commentUpdateVo);
        CommentDetailDto.Response responseDto = commentMapper.toDetailDto(commentDetailVo);
        return CommonResponseDto.ok(responseDto);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<CommonResponseDto<Object>> deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(boardId, commentId);
        return CommonResponseDto.ok();
    }

}

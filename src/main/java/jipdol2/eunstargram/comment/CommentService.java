package jipdol2.eunstargram.comment;

import jipdol2.eunstargram.comment.dto.response.CommentFindResponseDTO;
import jipdol2.eunstargram.exception.MemberNotFound;
import jipdol2.eunstargram.exception.PostNotFound;
import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.post.entity.PostRepository;
import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.comment.entity.CommentRepository;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    public EmptyJSON join(Long memberId,CommentSaveRequestDTO commentSaveRequestDTO){

        Post post = postRepository.findByOne(commentSaveRequestDTO.getPostId())
                .orElseThrow(()->new PostNotFound());

        Member member = memberRepository.findByOne(memberId)
                .orElseThrow(()->new MemberNotFound());

        Comment comment = Comment.builder()
                .content(commentSaveRequestDTO.getContent())
                .likeNumber(0L)
                .deleteYn("N")
                .post(post)
                .member(member)
                .build();

        commentRepository.save(comment);
        return new EmptyJSON();
    }

    @Transactional(readOnly = true)
    public List<CommentFindResponseDTO> findByAllComments(String nickname,Long postId){

        Post post = postRepository.findByOne(postId)
                .orElseThrow(() -> new PostNotFound());

        List<Comment> findByComments = post.getComments();
        List<CommentFindResponseDTO> comments = findByComments.stream()
                .map((c) -> CommentFindResponseDTO.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .likeNumber(c.getLikeNumber())
                        .deleteYn(c.getDeleteYn())
                        .nickname(nickname)
                        .build())
                .collect(Collectors.toList());
        return comments;
    }
}

package jipdol2.eunstargram.comment;

import jipdol2.eunstargram.article.entity.Post;
import jipdol2.eunstargram.article.entity.PostRepository;
import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.comment.entity.CommentRepository;
import jipdol2.eunstargram.common.EmptyJSON;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentSerivce {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    public EmptyJSON join(CommentSaveRequestDTO commentSaveRequestDTO){

        Post post = postRepository.findByOne(commentSaveRequestDTO.getPostId())
                .orElseThrow(()->new IllegalArgumentException("postId가 존재하지 않습니다."));

        Member member = memberRepository.findByOne(commentSaveRequestDTO.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("memberId가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .content(commentSaveRequestDTO.getComment())
                .likeNumber(commentSaveRequestDTO.getLikeNumber())
                .post(post)
                .member(member)
                .build();

        commentRepository.save(comment);
        return new EmptyJSON();
    }
}

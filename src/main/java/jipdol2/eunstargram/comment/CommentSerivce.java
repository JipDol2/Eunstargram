package jipdol2.eunstargram.comment;

import jipdol2.eunstargram.comment.entity.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentSerivce {

    private final CommentRepository commentRepository;


}

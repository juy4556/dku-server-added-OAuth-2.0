package com.dku.council.domain.post.repository.spec;

import com.dku.council.domain.post.model.entity.posttype.Notice;
import com.dku.council.domain.post.repository.post.NoticeRepository;
import com.dku.council.domain.tag.model.entity.PostTag;
import com.dku.council.domain.tag.model.entity.Tag;
import com.dku.council.domain.tag.repository.PostTagRepository;
import com.dku.council.domain.tag.repository.TagRepository;
import com.dku.council.domain.user.model.entity.Major;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.MajorRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.mock.MajorMock;
import com.dku.council.mock.NoticeMock;
import com.dku.council.mock.TagMock;
import com.dku.council.mock.UserMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostSpecTest {

    @Autowired
    private NoticeRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    private User user1;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    void setup() {
        Major major = majorRepository.save(MajorMock.create());

        user1 = UserMock.create(major);
        user1 = userRepository.save(user1);

        User user2 = UserMock.create(major);
        user2 = userRepository.save(user2);

        List<Notice> notice1 = NoticeMock.createList("notice-1-", user1, 5);
        postRepository.saveAll(notice1);

        tag1 = TagMock.create();
        createPostsWithTag("notice-2-", tag1, user1, 6);

        tag2 = TagMock.create();
        createPostsWithTag("notice-3-", tag2, user2, 7);
    }

    private void createPostsWithTag(String prefix, Tag tag, User user, int size) {
        List<Notice> loticeList = NoticeMock.createList(prefix, user, size);
        tag = tagRepository.save(tag);
        loticeList = postRepository.saveAll(loticeList);

        for (Notice notice : loticeList) {
            PostTag relation = new PostTag(tag);
            relation.changePost(notice);
            postTagRepository.save(relation);
        }
    }

    @Test
    void findByKeyword() {
        // given
        Specification<Notice> spec = PostSpec.withTitleOrBody("notice-1");

        // when
        List<Notice> all = postRepository.findAll(spec);

        // then
        assertThat(all.size()).isEqualTo(5);
    }

    @Test
    void findBySingleTags() {
        // given
        Specification<Notice> spec = PostSpec.withTag(tag1.getId());

        // when
        List<Notice> all = postRepository.findAll(spec);

        // then
        assertThat(all.size()).isEqualTo(6);
    }

    @Test
    void findByMultipleTags() {
        // given
        Specification<Notice> spec = PostSpec.withTags(List.of(tag1.getId(), tag2.getId()));

        // when
        List<Notice> all = postRepository.findAll(spec);

        // then
        assertThat(all.size()).isEqualTo(13);
    }

    @Test
    void findByAuthor() {
        // given
        Specification<Notice> spec = PostSpec.withAuthor(user1.getId());

        // when
        List<Notice> all = postRepository.findAll(spec);

        // then
        assertThat(all.size()).isEqualTo(11);
    }
}
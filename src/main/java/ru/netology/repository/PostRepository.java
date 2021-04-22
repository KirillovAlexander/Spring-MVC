package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class PostRepository {

  private List<Post> postList = new CopyOnWriteArrayList<>();
  private AtomicInteger index = new AtomicInteger(0);

  public List<Post> all() {
    return postList.stream()
            .filter(post -> !(post.isRemoved()))
            .collect(Collectors.toList());
  }

  public Optional<Post> getById(long id) {
    return postList.stream()
            .filter(post -> (!post.isRemoved()))
            .filter(post -> post.getId() == id)
            .findFirst();
  }

  public Post save(Post post) throws NotFoundException {
    if (post.getId() == 0) {
      post.setId(index.incrementAndGet());
      postList.add(post);
      return post;
    } else {
      for (int i = 0; i < postList.size(); i++) {
        Post currentPost = postList.get(i);
        if (currentPost.getId() == post.getId() && !currentPost.isRemoved()) {
          currentPost.setContent(post.getContent());
          return currentPost;
        }
      }
      throw new NotFoundException();
    }
  }

  public void removeById(long id) {
    for (int i = 0; i < postList.size(); i++) {
      if (postList.get(i).getId() == id) postList.get(i).setRemoved(true);
    }
  }
}

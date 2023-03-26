package com.socialnetwork.post;

import com.socialnetwork.post.model.SocialNetworkPost;
import com.socialnetwork.post.repository.SocialNetworkPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableCaching
public class PostApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(SocialNetworkPostRepository repository) {
		return (args) -> {
			List<SocialNetworkPost> posts = new ArrayList<>();
			Random random = new Random();
			for (int i = 0; i < 1000; i++) {
				Date postDate = new Date();
				String author = "Author " + i;
				String content = "Content " + i;
				Long viewCount = random.nextLong(1000);
				SocialNetworkPost post = new SocialNetworkPost(postDate, author, content, viewCount);
				posts.add(post);
			}

			repository.saveAll(posts);
		};
	}

}

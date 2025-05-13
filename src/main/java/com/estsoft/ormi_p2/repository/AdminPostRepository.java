package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminPostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
}

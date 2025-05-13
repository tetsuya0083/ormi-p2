package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}

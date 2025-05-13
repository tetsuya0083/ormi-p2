package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostKeywordRepository  extends JpaRepository<PostKeyword, Long> {

}

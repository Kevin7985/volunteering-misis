package ru.misis.skill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.misis.skill.model.Skill;

import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    @Query("select s from Skill s " +
            "where upper(s.title) like upper(concat('%', ?1, '%'))")
    Page<Skill> search(String text, Pageable pageable);

    @Query("select s from Skill s " +
            "where upper(s.title) like upper(concat('%', ?1, '%'))")
    List<Skill> search(String text);
}

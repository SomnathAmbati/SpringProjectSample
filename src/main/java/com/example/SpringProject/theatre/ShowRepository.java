package com.example.SpringProject.theatre;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepository extends JpaRepository<ShowEntity, Long> {

    List<ShowEntity> findByMovieId(Long movieId);

    List<ShowEntity> findByScreenTheatreIdAndMovieId(Long theatreId, Long movieId);

}

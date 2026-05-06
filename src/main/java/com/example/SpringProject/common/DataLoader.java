// package com.example.SpringProject.common;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.util.*;
// import java.util.stream.Collectors;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.core.JdbcTemplate;

// import com.example.SpringProject.movie.MovieEntity;
// import com.example.SpringProject.movie.MovieRepository;
// import com.example.SpringProject.seating.SeatRepository;
// import com.example.SpringProject.show.Show;
// import com.example.SpringProject.show.ShowRepository;
// import com.example.SpringProject.theatre.TheatreEntity;
// import com.example.SpringProject.theatre.TheatreRepository;

// @Configuration
// public class DataLoader {

//     private static final int[] SLOTS               = {8, 11, 14, 17, 20};
//     private static final int   SHOW_DAYS           = 3;
//     private static final int   TOTAL_SLOTS         = SLOTS.length * SHOW_DAYS; // 15 per theatre
//     private static final int   MAX_THEATRES_PER_MOVIE = 2;
//     private static final int   MAX_SHOWS_PER_THEATRE  = 2;

//     // ── RUNNER ──────────────────────────────────────────────────────────────

//     @Bean
//     CommandLineRunner loadInitialData(
//             MovieRepository   movieRepository,
//             TheatreRepository theatreRepository,
//             ShowRepository    showRepository,
//             SeatRepository    seatRepository,   // kept for JPA wiring; not used directly
//             JdbcTemplate      jdbcTemplate       // ← native batch inserts
//     ) {
//         return args -> {
//             loadMovies(movieRepository);
//             loadTheatres(theatreRepository);
//             loadShows(movieRepository, theatreRepository, showRepository, jdbcTemplate);
//         };
//     }

//     // ── MOVIES ──────────────────────────────────────────────────────────────

//     private void loadMovies(MovieRepository movieRepository) {

//         if (movieRepository.count() > 0) return;

//         // ⚠️  "Interstellar" appears TWICE in your list — removed the duplicate
//         List<MovieEntity> movies = List.of(
//             new MovieEntity(null, "3 Idiots",
//                 "Three friends navigate life, friendship, and engineering college.",
//                 "Comedy,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/3idiots.jpg",
//                 LocalDate.of(2009, 12, 25), AppEnums.CensorRating.U, 450, 100, 4.5),

//             new MovieEntity(null, "Akhanda",
//                 "A fierce devotee of Lord Shiva confronts evil to protect his people.",
//                 "Action,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/A_2.jpg",
//                 LocalDate.of(2021, 12, 2), AppEnums.CensorRating.UA, 470, 105, 4.5),

//             new MovieEntity(null, "Animal",
//                 "A violent father-son relationship spirals into a dark journey of revenge.",
//                 "Action,Crime,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Animal.jpg",
//                 LocalDate.of(2023, 12, 1), AppEnums.CensorRating.A, 450, 100, 4.5),

//             new MovieEntity(null, "Avatar",
//                 "A paraplegic marine is dispatched to the moon Pandora.",
//                 "Sci-Fi,Adventure", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/avatar.jpg",
//                 LocalDate.of(2009, 12, 18), AppEnums.CensorRating.UA, 430, 100, 4.3),

//             new MovieEntity(null, "Avatar: The Way of Water",
//                 "Jake Sully lives with his newfound family formed on Pandora.",
//                 "Sci-Fi,Adventure", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/avatar2.jpg",
//                 LocalDate.of(2022, 12, 16), AppEnums.CensorRating.UA, 410, 95, 4.3),

//             new MovieEntity(null, "Avengers",
//                 "Earth's mightiest heroes unite to stop a global threat.",
//                 "Action,Sci-Fi", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Avengers.jpg",
//                 LocalDate.of(2012, 5, 4), AppEnums.CensorRating.UA, 480, 110, 4.6),

//             new MovieEntity(null, "Baahubali: The Beginning",
//                 "An orphan learns about his royal heritage.",
//                 "Action,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Bahubali.jpg",
//                 LocalDate.of(2015, 7, 10), AppEnums.CensorRating.UA, 470, 105, 4.5),

//             new MovieEntity(null, "The Conjuring",
//                 "Paranormal investigators help a family terrorized by a dark presence.",
//                 "Horror,Thriller", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Conjuring.jpg",
//                 LocalDate.of(2013, 7, 19), AppEnums.CensorRating.UA, 460, 105, 4.4),

//             new MovieEntity(null, "Dangal",
//                 "A former wrestler trains his daughters to become world-class wrestlers.",
//                 "Biography,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/dangal.jpg",
//                 LocalDate.of(2016, 12, 23), AppEnums.CensorRating.U, 480, 110, 4.4),

//             new MovieEntity(null, "The Dark Knight",
//                 "Batman faces the Joker, a criminal mastermind.",
//                 "Action,Crime", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/dark_knight.jpg",
//                 LocalDate.of(2008, 7, 18), AppEnums.CensorRating.UA, 495, 110, 4.5),

//             new MovieEntity(null, "Dhurandhar",
//                 "A fearless man challenges a powerful criminal empire.",
//                 "Action,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Dhurandhar.jpg",
//                 LocalDate.of(2024, 1, 1), AppEnums.CensorRating.UA, 420, 95, 4.4),

//             new MovieEntity(null, "Drishyam",
//                 "A man protects his family after an unexpected crime.",
//                 "Thriller,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/drishyam.jpg",
//                 LocalDate.of(2015, 7, 31), AppEnums.CensorRating.UA, 460, 105, 4.4),

//             new MovieEntity(null, "Drishyam 2",
//                 "Past secrets resurface, threatening a family.",
//                 "Thriller,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/drishyam2.jpg",
//                 LocalDate.of(2022, 11, 18), AppEnums.CensorRating.UA, 445, 100, 4.4),

//             new MovieEntity(null, "Eega",
//                 "A man reincarnated as a fly seeks revenge against his killer.",
//                 "Fantasy,Action,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Eega.jpg",
//                 LocalDate.of(2012, 7, 6), AppEnums.CensorRating.UA, 470, 105, 4.5),

//             new MovieEntity(null, "Extraction",
//                 "A black-market mercenary is hired to rescue a kidnapped boy.",
//                 "Action,Thriller", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Extraction.jpg",
//                 LocalDate.of(2020, 4, 24), AppEnums.CensorRating.A, 440, 100, 4.4),

//             new MovieEntity(null, "Fauzi",
//                 "A patriotic action film centered on a soldier's life.",
//                 "Action,War", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/fauzi.jpg",
//                 LocalDate.of(2026, 4, 1), AppEnums.CensorRating.UA, 0, 0, 0.0),

//             new MovieEntity(null, "Gladiator",
//                 "A Roman general seeks revenge.",
//                 "Action,Drama", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/gladiator.jpg",
//                 LocalDate.of(2000, 5, 5), AppEnums.CensorRating.UA, 440, 100, 4.4),

//             new MovieEntity(null, "Inception",
//                 "A thief who steals corporate secrets through dream-sharing technology.",
//                 "Sci-Fi,Thriller", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Inception.jpg",
//                 LocalDate.of(2010, 7, 16), AppEnums.CensorRating.UA, 480, 110, 4.4),

//             new MovieEntity(null, "Interstellar",
//                 "A team travels through a wormhole in space.",
//                 "Sci-Fi,Drama", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Interstellar.jpg",
//                 LocalDate.of(2014, 11, 7), AppEnums.CensorRating.UA, 475, 105, 4.5),

//             new MovieEntity(null, "Kantara",
//                 "A man clashes with forest officers over land and traditions.",
//                 "Action,Drama", "Kannada",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/kantara.jpg",
//                 LocalDate.of(2022, 9, 30), AppEnums.CensorRating.UA, 465, 105, 4.4),

//             new MovieEntity(null, "KGF: Chapter 1",
//                 "A gangster rises to power in the Kolar Gold Fields.",
//                 "Action,Drama", "Kannada",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/kgf1.jpg",
//                 LocalDate.of(2018, 12, 21), AppEnums.CensorRating.UA, 455, 100, 4.6),

//             new MovieEntity(null, "KGF: Chapter 2",
//                 "Rocky consolidates his empire.",
//                 "Action,Drama", "Kannada",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/kgf2.jpg",
//                 LocalDate.of(2022, 4, 14), AppEnums.CensorRating.UA, 485, 105, 4.6),

//             new MovieEntity(null, "Mufasa: The Lion King",
//                 "The origin story of Mufasa and his rise to become king.",
//                 "Animation,Adventure,Drama", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Mufasa.jpg",
//                 LocalDate.of(2024, 12, 20), AppEnums.CensorRating.U, 420, 95, 4.4),

//             new MovieEntity(null, "Nuvvu Naaku Nachav",
//                 "A light-hearted romantic comedy where love blooms through misunderstandings.",
//                 "Romance,Comedy,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/NNN.jpg",
//                 LocalDate.of(2001, 9, 6), AppEnums.CensorRating.U, 470, 110, 4.6),

//             new MovieEntity(null, "Oye!",
//                 "A carefree young man learns the meaning of love and responsibility.",
//                 "Romance,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Oye!.jpg",
//                 LocalDate.of(2009, 7, 3), AppEnums.CensorRating.U, 430, 95, 4.4),

//             new MovieEntity(null, "Peddi",
//                 "An upcoming action drama film.",
//                 "Action,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/peddi.jpg",
//                 LocalDate.of(2026, 4, 1), AppEnums.CensorRating.UA, 0, 0, 0.0),

//             new MovieEntity(null, "PK",
//                 "An alien questions religious dogmas.",
//                 "Comedy,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/pk.jpg",
//                 LocalDate.of(2014, 12, 19), AppEnums.CensorRating.U, 430, 100, 4.3),

//             new MovieEntity(null, "Ramayana",
//                 "Epic mythological tale based on the Ramayana.",
//                 "Mythology,Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/ramayana.jpg",
//                 LocalDate.of(2026, 3, 1), AppEnums.CensorRating.U, 0, 0, 0.0),

//             new MovieEntity(null, "RRR",
//                 "Two revolutionaries fight against British rule and destiny.",
//                 "Action,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/RRR.jpg",
//                 LocalDate.of(2022, 3, 25), AppEnums.CensorRating.UA, 490, 110, 4.6),

//             new MovieEntity(null, "Salaar",
//                 "A violent man rises in a brutal criminal underworld.",
//                 "Action,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Salaar.jpg",
//                 LocalDate.of(2023, 12, 22), AppEnums.CensorRating.UA, 480, 105, 4.6),

//             new MovieEntity(null, "Sita Ramam",
//                 "An orphan soldier's love story unfolds through letters.",
//                 "Romance,Drama", "Telugu",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/SR.jpg",
//                 LocalDate.of(2022, 8, 5), AppEnums.CensorRating.U, 480, 110, 4.6),

//             new MovieEntity(null, "Spider-Man",
//                 "A teenager gains spider-like abilities.",
//                 "Action,Adventure", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/spiderman.jpg",
//                 LocalDate.of(2002, 5, 3), AppEnums.CensorRating.UA, 420, 95, 4.4),

//             new MovieEntity(null, "Titanic",
//                 "A love story aboard the ill-fated RMS Titanic.",
//                 "Romance,Drama", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/titanic.jpg",
//                 LocalDate.of(1997, 12, 19), AppEnums.CensorRating.UA, 470, 105, 4.5),

//             new MovieEntity(null, "Varanasi",
//                 "A spiritual journey set in the ancient city of Varanasi.",
//                 "Drama", "Hindi",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/varanasi.jpg",
//                 LocalDate.of(2026, 5, 1), AppEnums.CensorRating.UA, 0, 0, 0.0),

//             new MovieEntity(null, "Zootopia",
//                 "A rookie bunny cop uncovers a major conspiracy.",
//                 "Animation,Adventure,Comedy", "English",
//                 "https://res.cloudinary.com/dfebaqo69/image/upload/v1777625429/Zootopia.jpg",
//                 LocalDate.of(2016, 3, 4), AppEnums.CensorRating.U, 475, 110, 4.6)
//         );

//         movieRepository.saveAll(movies);
//     }

//     // ── THEATRES ─────────────────────────────────────────────────────────────

//     private void loadTheatres(TheatreRepository theatreRepository) {

//         if (theatreRepository.count() > 0) return;

//         List<TheatreEntity> theatres = List.of(
//             new TheatreEntity(null, "PVR Cinemas",      "Bangalore"),
//             new TheatreEntity(null, "INOX",             "Hyderabad"),
//             new TheatreEntity(null, "Cinepolis",        "Mumbai"),
//             new TheatreEntity(null, "AGS Cinemas",      "Chennai"),
//             new TheatreEntity(null, "SPI Cinemas",      "Chennai"),
//             new TheatreEntity(null, "Carnival Cinemas", "Delhi"),
//             new TheatreEntity(null, "Forum Mall PVR",   "Bangalore"),
//             new TheatreEntity(null, "Sathyam Cinemas",  "Chennai"),
//             new TheatreEntity(null, "Escape Cinemas",   "Chennai"),
//             new TheatreEntity(null, "Wave Cinemas",     "Noida"),
//             new TheatreEntity(null, "IMAX",             "Mysore"),
//             new TheatreEntity(null, "KKR",              "Mysore"),
//             new TheatreEntity(null, "IMAX",             "Vizag"),
//             new TheatreEntity(null, "INOX",             "Vizag"),
//             new TheatreEntity(null, "PVR",              "Pune"),
//             new TheatreEntity(null, "INOX",             "Bangalore")
//         );

//         theatreRepository.saveAll(theatres);
//     }

//     // ── SHOWS + SEATS ────────────────────────────────────────────────────────

//     private void loadShows(
//             MovieRepository   movieRepository,
//             TheatreRepository theatreRepository,
//             ShowRepository    showRepository,
//             JdbcTemplate      jdbcTemplate
//     ) {
//         if (showRepository.count() > 0) return;

//         // ── 1. Load lookups into memory (2 SELECTs total, not one per show) ──
//         Map<Long, MovieEntity> movieMap = movieRepository.findAll()
//                 .stream()
//                 .collect(Collectors.toMap(MovieEntity::getId, m -> m));

//         Map<Long, TheatreEntity> theatreMap = theatreRepository.findAll()
//                 .stream()
//                 .collect(Collectors.toMap(TheatreEntity::getId, t -> t));

//         List<Long> movieIds   = new ArrayList<>(movieMap.keySet());
//         List<Long> theatreIds = new ArrayList<>(theatreMap.keySet());

//         LocalDate baseDate = LocalDate.now().plusDays(1);

//         // ── 2. Build ALL Show objects in memory — zero DB calls here ──────────
//         Map<Long, Integer> theatreSlotIndex = new HashMap<>();
//         List<Show> allShows = new ArrayList<>();

//         for (Long movieId : movieIds) {

//             int theatresUsed = 0;

//             // Always pick least-loaded theatres first for even distribution
//             List<Long> sortedTheatres = theatreIds.stream()
//                     .sorted(Comparator.comparingInt(
//                             t -> theatreSlotIndex.getOrDefault(t, 0)))
//                     .collect(Collectors.toList());

//             for (Long theatreId : sortedTheatres) {

//                 if (theatresUsed >= MAX_THEATRES_PER_MOVIE) break;

//                 int usedSlots      = theatreSlotIndex.getOrDefault(theatreId, 0);
//                 int availableSlots = TOTAL_SLOTS - usedSlots;
//                 if (availableSlots <= 0) continue;

//                 int showsToCreate = Math.min(availableSlots, MAX_SHOWS_PER_THEATRE);

//                 for (int i = 0; i < showsToCreate; i++) {
//                     int virtualSlot = usedSlots + i;
//                     int dayOffset   = virtualSlot / SLOTS.length;   // 0, 1, or 2
//                     int slotIdx     = virtualSlot % SLOTS.length;   // 0–4
//                     int hour        = SLOTS[slotIdx];

//                     LocalDateTime showTime = LocalDateTime.of(
//                             baseDate.plusDays(dayOffset),
//                             LocalTime.of(hour, 0)
//                     );

//                     Show show = new Show();
//                     show.setMovie(movieMap.get(movieId));
//                     show.setTheatre(theatreMap.get(theatreId));
//                     show.setShowTime(showTime);
//                     allShows.add(show);
//                 }

//                 theatreSlotIndex.put(theatreId, usedSlots + showsToCreate);
//                 theatresUsed++;
//             }
//         }

//         // ── 3. ONE batch insert for all shows — returns saved entities with IDs
//         List<Show> savedShows = showRepository.saveAll(allShows);

//         // ── 4. ONE native JDBC batch insert for ALL seats ─────────────────────
//         batchInsertSeats(savedShows, jdbcTemplate);
//     }

//     /**
//      * Builds all seat rows in memory and sends them to Supabase in a
//      * single batchUpdate call — one network round-trip for all seats.
//      */
//     private void batchInsertSeats(List<Show> savedShows, JdbcTemplate jdbcTemplate) {

//         final int totalRows   = 10;   // A–J
//         final int totalCols   = 12;   // 1–12
//         final int premiumRows = 2;    // rows A, B

//         List<Object[]> args = new ArrayList<>(savedShows.size() * totalRows * totalCols);

//         for (Show show : savedShows) {
//             for (int row = 0; row < totalRows; row++) {
//                 char   rowChar   = (char) ('A' + row);
//                 String seatType  = row < premiumRows ? "PREMIUM" : "REGULAR";

//                 for (int col = 1; col <= totalCols; col++) {
//                     args.add(new Object[]{
//                         show.getId(),
//                         rowChar + String.valueOf(col),
//                         seatType,
//                         "AVAILABLE"
//                     });
//                 }
//             }
//         }

//         jdbcTemplate.batchUpdate(
//             "INSERT INTO seats (show_id, seat_number, seat_type, status) VALUES (?, ?, ?, ?)",
//             args
//         );
//     }
// }
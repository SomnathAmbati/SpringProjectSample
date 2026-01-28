package com.example.SpringProject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.theatre.TheatreDTO;
import com.example.SpringProject.theatre.TheatreEntity;
import com.example.SpringProject.theatre.TheatreRepository;
import com.example.SpringProject.theatre.TheatreServiceImpl;


@ExtendWith(MockitoExtension.class)
public class TheatreServiceImplTest {

    @Mock
    private TheatreRepository theatreRepository;

    @InjectMocks
    private TheatreServiceImpl theatreService;

    // ==================== CREATE THEATRE TESTS ====================

    @Test
    public void createTheatreTestValid() {
        // Arrange
        TheatreDTO dto = new TheatreDTO();
        dto.setName("PVR Cinemas");
        dto.setLocation("Bangalore");

        TheatreEntity theatre = new TheatreEntity();
        theatre.setId(1L);
        theatre.setName("PVR Cinemas");
        theatre.setLocation("Bangalore");

        Mockito.when(theatreRepository.save(Mockito.any(TheatreEntity.class))).thenReturn(theatre);

        // Act
        TheatreDTO result = theatreService.createTheatre(dto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("PVR Cinemas", result.getName());
        Assertions.assertEquals("Bangalore", result.getLocation());
        Mockito.verify(theatreRepository, Mockito.times(1)).save(Mockito.any(TheatreEntity.class));
    }

    // ==================== GET ALL THEATRES TESTS ====================

    @Test
    public void getAllTheatresTestValid() {
        // Arrange
        TheatreEntity theatre1 = new TheatreEntity();
        theatre1.setId(1L);
        theatre1.setName("PVR Cinemas");
        theatre1.setLocation("Bangalore");

        TheatreEntity theatre2 = new TheatreEntity();
        theatre2.setId(2L);
        theatre2.setName("INOX");
        theatre2.setLocation("Mumbai");

        List<TheatreEntity> theatres = Arrays.asList(theatre1, theatre2);
        Mockito.when(theatreRepository.findAll()).thenReturn(theatres);

        // Act
        List<TheatreDTO> result = theatreService.getAllTheatres();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("PVR Cinemas", result.get(0).getName());
        Assertions.assertEquals("INOX", result.get(1).getName());
        Mockito.verify(theatreRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAllTheatresTestEmptyList() {
        // Arrange
        Mockito.when(theatreRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<TheatreDTO> result = theatreService.getAllTheatres();

        // Assert
        Assertions.assertEquals(0, result.size());
        Mockito.verify(theatreRepository, Mockito.times(1)).findAll();
    }

    // ==================== GET THEATRE BY ID TESTS ====================

    @Test
    public void getTheatreByIdTestValid() throws ResourceNotFoundException {
        // Arrange
        TheatreEntity theatre = new TheatreEntity();
        theatre.setId(1L);
        theatre.setName("PVR Cinemas");
        theatre.setLocation("Bangalore");

        Mockito.when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));

        // Act
        TheatreDTO result = theatreService.getTheatreById(1L);

        // Assert
        Assertions.assertEquals("PVR Cinemas", result.getName());
        Assertions.assertEquals("Bangalore", result.getLocation());
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getTheatreByIdTestInvalidId() {
        // Arrange
        Mockito.when(theatreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> theatreService.getTheatreById(999L)
        );
        Assertions.assertEquals("Service.THEATRE_NOT_FOUND", exception.getMessage());
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(999L);
    }

    // ==================== CREATE (ADMIN) TESTS ====================

    @Test
    public void createAdminTestValid() {
        // Arrange
        TheatreDTO dto = new TheatreDTO();
        dto.setName("Cinepolis");
        dto.setLocation("Delhi");

        TheatreEntity theatre = new TheatreEntity();
        theatre.setId(1L);
        theatre.setName("Cinepolis");
        theatre.setLocation("Delhi");

        Mockito.when(theatreRepository.save(Mockito.any(TheatreEntity.class))).thenReturn(theatre);

        // Act
        TheatreEntity result = theatreService.create(dto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Cinepolis", result.getName());
        Mockito.verify(theatreRepository, Mockito.times(1)).save(Mockito.any(TheatreEntity.class));
    }

    // ==================== UPDATE THEATRE TESTS ====================

    @Test
    public void updateTheatreTestValid() throws ResourceNotFoundException {
        // Arrange
        TheatreEntity existingTheatre = new TheatreEntity();
        existingTheatre.setId(1L);
        existingTheatre.setName("Old Name");
        existingTheatre.setLocation("Old Location");

        TheatreDTO updateDto = new TheatreDTO();
        updateDto.setName("New Name");
        updateDto.setLocation("New Location");

        Mockito.when(theatreRepository.findById(1L)).thenReturn(Optional.of(existingTheatre));
        Mockito.when(theatreRepository.save(Mockito.any(TheatreEntity.class))).thenReturn(existingTheatre);

        // Act
        TheatreEntity result = theatreService.update(1L, updateDto);

        // Assert
        Assertions.assertEquals("New Name", result.getName());
        Assertions.assertEquals("New Location", result.getLocation());
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(theatreRepository, Mockito.times(1)).save(existingTheatre);
    }

    @Test
    public void updateTheatreTestInvalidId() {
        // Arrange
        TheatreDTO updateDto = new TheatreDTO();
        updateDto.setName("New Name");

        Mockito.when(theatreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> theatreService.update(999L, updateDto)
        );
        Assertions.assertEquals("Service.THEATRE_NOT_FOUND", exception.getMessage());
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(999L);
        Mockito.verify(theatreRepository, Mockito.times(0)).save(Mockito.any());
    }

    // ==================== DELETE THEATRE TESTS ====================

    @Test
    public void deleteTheatreTestValid() throws ResourceNotFoundException {
        // Arrange
        TheatreEntity theatre = new TheatreEntity();
        theatre.setId(1L);
        theatre.setName("PVR Cinemas");

        Mockito.when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        Mockito.doNothing().when(theatreRepository).deleteById(1L);

        // Act
        theatreService.delete(1L);

        // Assert
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(theatreRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void deleteTheatreTestInvalidId() {
        // Arrange
        Mockito.when(theatreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> theatreService.delete(999L)
        );
        Assertions.assertEquals("Service.THEATRE_NOT_FOUND", exception.getMessage());
        Mockito.verify(theatreRepository, Mockito.times(1)).findById(999L);
        Mockito.verify(theatreRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }
}


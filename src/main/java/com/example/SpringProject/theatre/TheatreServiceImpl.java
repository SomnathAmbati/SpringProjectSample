package com.example.SpringProject.theatre;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TheatreServiceImpl implements TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TheatreDTO createTheatre(TheatreDTO dto) {
        // Map DTO → Entity
        TheatreEntity theatre = modelMapper.map(dto, TheatreEntity.class);

        // Save entity
        TheatreEntity saved = theatreRepository.save(theatre);

        // Map Entity → DTO
        return modelMapper.map(saved, TheatreDTO.class);
    }

    @Override
    public List<TheatreDTO> getAllTheatres() {
        return theatreRepository.findAll()
                .stream()
                .map(theatre -> modelMapper.map(theatre, TheatreDTO.class))
                .toList();
    }

    @Override
    public TheatreDTO getTheatreById(Long id) {
        TheatreEntity theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        return modelMapper.map(theatre, TheatreDTO.class);
    }

    //Admin Methods
    @Override
    public TheatreEntity create(TheatreDTO dto) {
        return theatreRepository.save(modelMapper.map(dto, TheatreEntity.class));
    }

    @Override
    public TheatreEntity update(Long id, TheatreDTO dto) {
        TheatreEntity t = theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));
        t.setName(dto.getName());
        t.setLocation(dto.getLocation());
        return theatreRepository.save(t);
    }

    @Override
    public void delete(Long id) {
        theatreRepository.deleteById(id);
    }

}
